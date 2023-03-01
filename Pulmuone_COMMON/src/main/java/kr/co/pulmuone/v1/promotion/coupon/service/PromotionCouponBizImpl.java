package kr.co.pulmuone.v1.promotion.coupon.service;

import com.github.pagehelper.Page;
import kr.co.pulmuone.v1.approval.auth.dto.vo.ApprovalAuthManagerVo;
import kr.co.pulmuone.v1.approval.auth.dto.vo.ApprovalStatusVo;
import kr.co.pulmuone.v1.approval.auth.service.ApprovalAuthBiz;
import kr.co.pulmuone.v1.comm.aop.service.UserMaskingRun;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.base.vo.UserVo;
import kr.co.pulmuone.v1.comm.constants.GoodsConstants;
import kr.co.pulmuone.v1.comm.enums.*;
import kr.co.pulmuone.v1.comm.enums.GoodsEnums.DeviceType;
import kr.co.pulmuone.v1.comm.enums.ShoppingEnums.CartPromotionType;
import kr.co.pulmuone.v1.comm.exception.BaseException;
import kr.co.pulmuone.v1.comm.exception.BosCustomException;
import kr.co.pulmuone.v1.comm.framework.dto.ExcelDownloadDto;
import kr.co.pulmuone.v1.comm.framework.dto.ExcelWorkSheetDto;
import kr.co.pulmuone.v1.comm.util.BindUtil;
import kr.co.pulmuone.v1.comm.util.ExcelUploadUtil;
import kr.co.pulmuone.v1.comm.util.SessionUtil;
import kr.co.pulmuone.v1.comm.util.StringUtil;
import kr.co.pulmuone.v1.goods.goods.dto.DiscountCalculationResultDto;
import kr.co.pulmuone.v1.goods.goods.dto.GoodsApplyCouponDto;
import kr.co.pulmuone.v1.goods.goods.dto.vo.ShippingDataResultVo;
import kr.co.pulmuone.v1.goods.goods.service.GoodsShippingTemplateBiz;
import kr.co.pulmuone.v1.order.claim.dto.OrderClaimGoodsInfoDto;
import kr.co.pulmuone.v1.order.claim.dto.OrderClaimMasterInfoDto;
import kr.co.pulmuone.v1.order.claim.dto.OrderClaimViewRequestDto;
import kr.co.pulmuone.v1.order.claim.service.ClaimProcessBiz;
import kr.co.pulmuone.v1.order.claim.service.ClaimRequestBiz;
import kr.co.pulmuone.v1.order.order.service.OrderDetailBiz;
import kr.co.pulmuone.v1.outmall.order.dto.vo.OutMallExcelInfoVo;
import kr.co.pulmuone.v1.promotion.coupon.dto.*;
import kr.co.pulmuone.v1.promotion.coupon.dto.vo.*;
import kr.co.pulmuone.v1.shopping.cart.dto.*;
import kr.co.pulmuone.v1.user.buyer.service.UserBuyerBiz;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;


@SuppressWarnings("rawtypes")
@Slf4j
@Service
public class PromotionCouponBizImpl implements PromotionCouponBiz {

	@Autowired
	private PromotionCouponService promotionCouponService;

    @Autowired
    private ApprovalAuthBiz approvalAuthBiz;

    @Autowired
    private OrderDetailBiz orderDetailBiz;

    @Autowired
    private ClaimProcessBiz claimProcessBiz;

    @Autowired
	private ClaimRequestBiz claimRequestBiz;

    @Autowired
    private UserBuyerBiz userBuyerBiz;

	@Autowired
	private GoodsShippingTemplateBiz goodsShippingTemplateBiz;

	private static final int APPROVE_POINT_BY_SYSTEM_AMOUNT = 10000;

	/**
	 * 쿠폰목록 조회
	 *
	 * @param couponRequestDto
	 * @return CouponListResponseDto
	 * @throws Exception
	 */
	@Override
	public ApiResult<?> getCpnMgm(CouponRequestDto couponRequestDto) throws Exception {
		CouponListResponseDto result = new CouponListResponseDto();

		Page<CouponListResultVo> couponList = promotionCouponService.getCpnMgm(couponRequestDto);

		result.setTotal(couponList.getTotal());
		result.setRows(couponList.getResult());

		return ApiResult.success(result);
	}

	/**
	 * 쿠폰상세 조회
	 *
	 * @param couponRequestDto
	 * @return CouponDetailResponseDto
	 * @throws Exception
	 */
	@Override
	@UserMaskingRun(system="BOS")
	public ApiResult<?> getCoupon(CouponRequestDto couponRequestDto) throws Exception {

		CouponDetailResponseDto result = new CouponDetailResponseDto();
		CouponDetailVo vo = new CouponDetailVo();

		vo = promotionCouponService.getCoupon(couponRequestDto);
		OrganizationListResultVo organization = promotionCouponService.getOrganizationList(couponRequestDto);
		if (organization != null) {
			vo.setUrErpOrganizationCode(organization.getUrErpOrganizationCode());
			vo.setUrErpOrganizationName(organization.getUrErpOrganizationName());
		}
		List<CoverageVo> coverageList = promotionCouponService.getCoverageList(couponRequestDto);
		if (!coverageList.isEmpty()) {
			vo.setCoverageList(coverageList);
		}
		List<AccountInfoVo> serialNumberList = promotionCouponService.getSerialNumberList(couponRequestDto);
		if (!serialNumberList.isEmpty()) {
			vo.setSerialNumberList(serialNumberList);
		}
		List<AccountInfoVo> userList = promotionCouponService.getUserList(couponRequestDto);
		if (!userList.isEmpty()) {
			vo.setUserList(userList);
		}

		if(vo.getApprUserId() != null) {
			couponRequestDto.setApprKindType(ApprovalEnums.ApprovalAuthType.APPR_KIND_TP_COUPON.getCode());
			couponRequestDto.setApprSubUserId(vo.getApprSubUserId());
			couponRequestDto.setApprUserId(vo.getApprUserId());
			// 승인관리자 정보 조회
			List<ApprovalAuthManagerVo> apprUserList = promotionCouponService.getApprUserList(couponRequestDto);
			if (!apprUserList.isEmpty()) {
				vo.setApprUserList(apprUserList);
			}
		}

		result.setRows(vo);

		return ApiResult.success(result);
	}

	/**
	 * 조직 조회
	 *
	 * @param organizationPopupListRequestDto
	 * @return CouponDetailResponseDto
	 * @throws Exception
	 */
	@Override
	public ApiResult<?> getOrganizationPopupList(OrganizationPopupListRequestDto organizationPopupListRequestDto)
			throws Exception {
		OrganizationPopupListResponseDto result = new OrganizationPopupListResponseDto();

		Page<OrganizationPopupListResultVo> organizationPopupListResultVo = promotionCouponService
				.getOrganizationPopupList(organizationPopupListRequestDto);

		result.setRows(organizationPopupListResultVo.getResult());
		result.setTotal(organizationPopupListResultVo.getTotal());

		return ApiResult.success(result);
	}

	/**
	 * 쿠폰정보 등록
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = {BaseException.class, Exception.class})
	public ApiResult<?> addCoupon(CouponRequestDto couponRequestDto) throws Exception {
		CouponResponseDto result = new CouponResponseDto();


		if (couponRequestDto.getInsertData() != null) {
			if (!couponRequestDto.getInsertData().isEmpty()) {
				couponRequestDto.setInsertRequestDtoList((List<CoverageVo>) BindUtil
						.convertJsonArrayToDtoList(couponRequestDto.getInsertData(), CoverageVo.class));
			}
		}

		if (couponRequestDto.getUploadUser() != null) {
			if (!couponRequestDto.getUploadUser().isEmpty()) {
				couponRequestDto.setUploadUserList((List<UploadInfoVo>) BindUtil
						.convertJsonArrayToDtoList(couponRequestDto.getUploadUser(), UploadInfoVo.class));
			}
		}

		if (couponRequestDto.getUploadTicket() != null) {
			if (!couponRequestDto.getUploadTicket().isEmpty()) {
				couponRequestDto.setUploadTicketList((List<UploadInfoVo>) BindUtil
						.convertJsonArrayToDtoList(couponRequestDto.getUploadTicket(), UploadInfoVo.class));
			}
		}

		List<CoverageVo> insertRequestDtoList = couponRequestDto.getInsertRequestDtoList();

		if (CouponEnums.CouponType.GOODS.getCode().equals(couponRequestDto.getCouponType())) {
			couponRequestDto.setPaymentType(couponRequestDto.getPaymentType());
			couponRequestDto.setCoverageType(couponRequestDto.getGoodsCoverageType());
		} else if (CouponEnums.CouponType.CART.getCode().equals(couponRequestDto.getCouponType())) {
			couponRequestDto.setPaymentType(couponRequestDto.getPaymentTypeCart());
			couponRequestDto.setCoverageType(couponRequestDto.getGoodsCoverageType());
		} else if (CouponEnums.CouponType.SHIPPING_PRICE.getCode().equals(couponRequestDto.getCouponType())) {
			couponRequestDto.setPaymentType(couponRequestDto.getPaymentTypeShippingPrice());
			couponRequestDto.setCoverageType(couponRequestDto.getShippingCoverageType());
			couponRequestDto.setPercentageMaxDiscountAmount(couponRequestDto.getDiscountValueCart());
		} else {
			couponRequestDto.setPaymentType(couponRequestDto.getPaymentTypeSaleprice());
			couponRequestDto.setCoverageType(couponRequestDto.getAppintCoverageType());
		}

		if (CouponEnums.DiscountType.FIXED_DISCOUNT.getCode().equals(couponRequestDto.getDiscountType())) {
			couponRequestDto.setDiscountVal(couponRequestDto.getDiscountValueFixed());
		} else if (CouponEnums.DiscountType.PERCENTAGE_DISCOUNT.getCode().equals(couponRequestDto.getDiscountType())) {
			couponRequestDto.setDiscountVal(couponRequestDto.getDiscountValuePercent());
			if (CouponEnums.CouponType.SALEPRICE_APPPOINT.getCode().equals(couponRequestDto.getCouponType())) {
				couponRequestDto.setDiscountVal(couponRequestDto.getDiscountValueSalePrice());
			}
		}

		if (couponRequestDto.getSerialNumberType() == null) {
			couponRequestDto.setSerialNumberType("Y");
		}

		// 승인요청 체크 후 저장 시
		if (couponRequestDto.getApprovalCheck().equals("Y")) {
			couponRequestDto.setCouponMasterStat(CouponEnums.CouponMasterStatus.SAVE.getCode());
			couponRequestDto.setApprStat(ApprovalEnums.ApprovalStatus.REQUEST.getCode());
		} else {
			couponRequestDto.setCouponMasterStat(CouponEnums.CouponMasterStatus.SAVE.getCode());
			couponRequestDto.setApprStat(ApprovalEnums.ApprovalStatus.NONE.getCode());
		}

		// 자동발급 : 이벤트 케이스 issueQtyLimit 0 으로 설정
		if(CouponEnums.IssueDetailType.EVENT_AWARD.getCode().equals(couponRequestDto.getAutoIssueType())) {
			couponRequestDto.setIssueQtyLimit("0");
		}

		// 발급기간 종료일 시간
		if(couponRequestDto.getIssueEndDate() != null && !StringUtils.isEmpty(couponRequestDto.getIssueEndDate())) {
			couponRequestDto.setIssueEndDate(couponRequestDto.getIssueEndDate() + "235959");
		}

		// 유효기간 종료일 시간
		if(couponRequestDto.getValidityEndDate() != null && !StringUtils.isEmpty(couponRequestDto.getValidityEndDate())) {
			couponRequestDto.setValidityEndDate(couponRequestDto.getValidityEndDate() + "235959");
		}

		// Set 등록 ID
		couponRequestDto.setCreateId(couponRequestDto.getUserVo().getUserId());

		// 계정발급 회원 ID 체크
		if (CouponEnums.PaymentType.CHECK_PAYMENT.getCode().equals(couponRequestDto.getPaymentType())) {

			List<UploadInfoVo> accountUserList = couponRequestDto.getUploadUserList();
			int userIdCnt = promotionCouponService.getUserIdCnt(couponRequestDto);

			if(userIdCnt != accountUserList.size()) {
				throw new BosCustomException(CouponEnums.AddCouponValidation.USER_ID_FAIL.getCode(), CouponEnums.AddCouponValidation.USER_ID_FAIL.getMessage());
			}
		}

		if (CouponEnums.PaymentType.TICKET.getCode().equals(couponRequestDto.getPaymentType())) {
			if (CouponEnums.SerialNumberType.EXCEL_UPLOAD.getCode()
					.equals(couponRequestDto.getSerialNumberType())) { // 엑셀업로드

				couponRequestDto.setSerialNumberUseType(CouponEnums.SerialNumberUseType.COUPON.getCode()); // 사용타입 : 쿠폰

				couponRequestDto.setSerialNumberStatus(CouponEnums.SerialNumberStatus.ISSUED.getCode()); // 이용권 상태: 발급
				int seralCount = 0;
				List<UploadInfoVo> accountTicketList = couponRequestDto.getUploadTicketList();
				boolean duplicated = accountTicketList.stream()
						.map(UploadInfoVo::getSerialNumber)
						.distinct()
						.count() != accountTicketList.size();
				if(duplicated){
					return ApiResult.result(CouponEnums.FixedNumberValidation.SAME_NUMBER);
				}

				for (int i = 0; i < accountTicketList.size(); i++) {
					UploadInfoVo vo = new UploadInfoVo();
					vo.setSerialNumber(accountTicketList.get(i).getSerialNumber());
//					couponRequestDto.setSerialNumber(vo.getSerialNumber());
					seralCount = promotionCouponService.getDuplicateSerialNumber(vo);		//이용권 중복 SerialNumber 조회
					if (seralCount > 0) {
						return ApiResult.result(CouponEnums.FixedNumberValidation.DUPLICATE_NUMBER);
					}
				}

				if(seralCount == 0) {
					// 쿠폰 등록
					promotionCouponService.addCoupon(couponRequestDto);
					for (int i = 0; i < accountTicketList.size(); i++) {
						UploadInfoVo vo = new UploadInfoVo();
						vo.setSerialNumber(accountTicketList.get(i).getSerialNumber());
						couponRequestDto.setSerialNumber(vo.getSerialNumber());

						// 개별난수번호 등록 (엑셀에서 저장된 난수로 등록) : 이용권 번호 암호화 후 등록
						promotionCouponService.addSerialNumber(couponRequestDto);
					}
				}

			}else if(CouponEnums.SerialNumberType.FIXED_VALUE.getCode()
					.equals(couponRequestDto.getSerialNumberType())) { // 단일코드

				int fixSeralCount = promotionCouponService.getDuplicateFixedNumber(couponRequestDto);

				if(fixSeralCount > 0) {  // 단일코드 중복 체크
					return ApiResult.result(CouponEnums.FixedNumberValidation.DUPLICATE_NUMBER);
				}else {
					// 쿠폰 등록
					promotionCouponService.addCoupon(couponRequestDto);
//					couponRequestDto.setSerialNumberUseType(CouponEnums.SerialNumberUseType.COUPON.getCode()); // 사용타입 : 쿠폰
//					couponRequestDto.setSerialNumberStatus(CouponEnums.SerialNumberStatus.ISSUED.getCode()); // 이용권 상태: 발급
//
//					promotionCouponService.addSerialNumber(couponRequestDto);
				}

			}else {		// 자동발급
				// 쿠폰 등록
				promotionCouponService.addCoupon(couponRequestDto);
			}
		}else {
			// 쿠폰 등록
			promotionCouponService.addCoupon(couponRequestDto);
		}

		if (insertRequestDtoList != null && !insertRequestDtoList.isEmpty()) {
			for (int i = 0; i < insertRequestDtoList.size(); i++) {
				CoverageVo vo = insertRequestDtoList.get(i);
				vo.setPmCouponId(couponRequestDto.getPmCouponId());
				insertRequestDtoList.get(i).setPmCouponId(couponRequestDto.getPmCouponId());
				insertRequestDtoList.get(i).setUserId(couponRequestDto.getUserVo().getUserId());
			}

			// 쿠폰 적용범위 등록
			promotionCouponService.addCouponCoverage(insertRequestDtoList);
		}

		// 분담정보 등록
		promotionCouponService.addOrganization(couponRequestDto);

		if (CouponEnums.PaymentType.CHECK_PAYMENT.getCode().equals(couponRequestDto.getPaymentType())) {
			CouponIssueParamDto couponIssueParamDto = new CouponIssueParamDto();
			couponIssueParamDto.setPmCouponId(couponRequestDto.getPmCouponId());
			couponIssueParamDto.setCreateId(couponRequestDto.getCreateId());

			List<UploadInfoVo> accountUserList = couponRequestDto.getUploadUserList();

			if (accountUserList != null && !accountUserList.isEmpty()) {
				String issueQtyLimit = couponRequestDto.getIssueQtyLimit();
				int limit = 0;
				String qtyLimit = "";

				if(CouponEnums.IssueType.AUTO_PAYMENT.getCode().equals(couponRequestDto.getPaymentType())) {
					qtyLimit = couponRequestDto.getIssueQtyLimit();
				}else {
					limit = issueQtyLimit.indexOf(".");
					qtyLimit = issueQtyLimit.substring(limit+1);
				}
				int limitCnt = Integer.parseInt(qtyLimit);


				for (int i = 0; i < accountUserList.size(); i++) {

					couponIssueParamDto.setLoginId(accountUserList.get(i).getLoginId());
					couponIssueParamDto.setPmCouponId(couponRequestDto.getPmCouponId());
					couponIssueParamDto.setCouponIssueType(CouponEnums.CouponIssueType.NORMAL.getCode());
					couponIssueParamDto.setCouponStatus(CouponEnums.CouponStatus.NOTUSE.getCode());

					if (couponRequestDto.getValidityEndDate() != null
							&& !couponRequestDto.getValidityEndDate().isEmpty()) {						// 유효기간 [기간설정]
						// 상품, 장바구니 쿠폰만료일 설정
						couponIssueParamDto.setValidityStartDate(couponRequestDto.getValidityStartDate());
						couponIssueParamDto.setExpirationDate(couponRequestDto.getValidityEndDate());
					} else {																			// 유효기간 [유효일]
						Date date = null;
						DateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");

						LocalDateTime endDateTime = LocalDateTime.of(LocalDate.now(), LocalTime.of(23,59,59));

						date = dateFormat.parse(endDateTime.format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")));

						int validityDay = Integer.parseInt(couponRequestDto.getValidityDay());

						Calendar cal = Calendar.getInstance();
						cal.setTime(date);
						cal.add(Calendar.DATE, validityDay);
						String expirationDate = dateFormat.format(cal.getTime());

						// 배송비 쿠폰만료일 설정 (발급일 + 유효일)
						couponIssueParamDto.setValidityStartDate(dateFormat.format(date));
						couponIssueParamDto.setExpirationDate(expirationDate);
					}

					if(accountUserList.get(i).getUrUserId() != null ) {
						couponIssueParamDto.setUrUserId(accountUserList.get(i).getUrUserId());
	    			}else {
	    				couponIssueParamDto.setLoginId(accountUserList.get(i).getLoginId());
	    			}



					for(int k = 0 ; k < limitCnt ; k++ ) {

						// 쿠폰 발급/사용 등록
						promotionCouponService.putCouponIssueList(couponIssueParamDto);

						// 쿠폰 발급/사용 상태 이력 등록
						promotionCouponService.putCouponIssueHistoryList(couponIssueParamDto);
					}

				}
			}

		} else if (CouponEnums.PaymentType.TICKET.getCode().equals(couponRequestDto.getPaymentType())) {

			couponRequestDto.setSerialNumberUseType(CouponEnums.SerialNumberUseType.COUPON.getCode()); // 사용타입 : 쿠폰

			couponRequestDto.setSerialNumberStatus(CouponEnums.SerialNumberStatus.ISSUED.getCode()); // 이용권 상태: 발급

			if (CouponEnums.SerialNumberType.AUTO_CREATE.getCode().equals(couponRequestDto.getSerialNumberType())) { // 자동생성

				if (couponRequestDto.getIssueQty() > 0) {

					for (int i = 0; i < couponRequestDto.getIssueQty(); i++) {

						// 개별난수번호 등록 (등록시 난수 생성 function 호출) : 이용권 번호 암호화 후 등록
						promotionCouponService.addSerialNumber(couponRequestDto);
					}
				}

			}

		}

		// 이전 쿠폰정보 조회
		ApprovalStatusVo history = ApprovalStatusVo.builder() //
				.taskPk(couponRequestDto.getPmCouponId())
				.apprUserId(couponRequestDto.getApprUserId())
				.apprSubUserId(couponRequestDto.getApprSubUserId())
				.approvalRequestUserId(SessionUtil.getBosUserVO().getUserId())
				.masterStat(couponRequestDto.getCouponMasterStat())
				.apprStat(couponRequestDto.getApprStat())
				.createId(couponRequestDto.getCreateId())
				.build();

		// 쿠폰상태이력 등록
		promotionCouponService.addCouponStatusHistory(history);

		return ApiResult.success();
	}

	/**
	 * 쿠폰정보 수정
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = {BaseException.class, Exception.class})
	public ApiResult<?> putCoupon(CouponRequestDto couponRequestDto) throws Exception {
		CouponResponseDto result = new CouponResponseDto();


		if (couponRequestDto.getInsertData() != null) {
			if (!couponRequestDto.getInsertData().isEmpty()) {
				couponRequestDto.setInsertRequestDtoList((List<CoverageVo>) BindUtil
						.convertJsonArrayToDtoList(couponRequestDto.getInsertData(), CoverageVo.class));
			}
		}

		if (couponRequestDto.getUploadUser() != null) {
			if (!couponRequestDto.getUploadUser().isEmpty()) {
				couponRequestDto.setUploadUserList((List<UploadInfoVo>) BindUtil
						.convertJsonArrayToDtoList(couponRequestDto.getUploadUser(), UploadInfoVo.class));
			}
		}

		if (couponRequestDto.getUploadTicket() != null) {
			if (!couponRequestDto.getUploadTicket().isEmpty()) {
				couponRequestDto.setUploadTicketList((List<UploadInfoVo>) BindUtil
						.convertJsonArrayToDtoList(couponRequestDto.getUploadTicket(), UploadInfoVo.class));
			}
		}

		List<CoverageVo> updateRequestDtoList = couponRequestDto.getInsertRequestDtoList();

		if (CouponEnums.CouponType.GOODS.getCode().equals(couponRequestDto.getCouponType())) {
			couponRequestDto.setPaymentType(couponRequestDto.getPaymentType());
			couponRequestDto.setCoverageType(couponRequestDto.getGoodsCoverageType());
		} else if (CouponEnums.CouponType.CART.getCode().equals(couponRequestDto.getCouponType())) {
			couponRequestDto.setPaymentType(couponRequestDto.getPaymentTypeCart());
			couponRequestDto.setCoverageType(couponRequestDto.getGoodsCoverageType());
		} else if (CouponEnums.CouponType.SHIPPING_PRICE.getCode().equals(couponRequestDto.getCouponType())) {
			couponRequestDto.setPaymentType(couponRequestDto.getPaymentTypeShippingPrice());
			couponRequestDto.setCoverageType(couponRequestDto.getShippingCoverageType());
			couponRequestDto.setPercentageMaxDiscountAmount(couponRequestDto.getDiscountValueCart());
		} else {
			couponRequestDto.setPaymentType(couponRequestDto.getPaymentTypeSaleprice());
			couponRequestDto.setCoverageType(couponRequestDto.getAppintCoverageType());
		}

		if (CouponEnums.DiscountType.FIXED_DISCOUNT.getCode().equals(couponRequestDto.getDiscountType())) {
			couponRequestDto.setDiscountVal(couponRequestDto.getDiscountValueFixed());
		} else if (CouponEnums.DiscountType.PERCENTAGE_DISCOUNT.getCode().equals(couponRequestDto.getDiscountType())) {
			couponRequestDto.setDiscountVal(couponRequestDto.getDiscountValuePercent());
			if (CouponEnums.CouponType.SALEPRICE_APPPOINT.getCode().equals(couponRequestDto.getCouponType())) {
				couponRequestDto.setDiscountVal(couponRequestDto.getDiscountValueSalePrice());
			}
		}

		if (couponRequestDto.getSerialNumberType().isEmpty()) {
			couponRequestDto.setSerialNumberType("Y");
		}

		if (couponRequestDto.getApprovalCheck().equals("Y")) {
			couponRequestDto.setCouponMasterStat(CouponEnums.CouponMasterStatus.SAVE.getCode());
			couponRequestDto.setApprStat(ApprovalEnums.ApprovalStatus.REQUEST.getCode());
		} else {
			couponRequestDto.setCouponMasterStat(CouponEnums.CouponMasterStatus.SAVE.getCode());
			couponRequestDto.setApprStat(ApprovalEnums.ApprovalStatus.NONE.getCode());
		}

		if (couponRequestDto.getValidityDay().isEmpty()) {
			couponRequestDto.setValidityDay(null);
		}

		if (couponRequestDto.getValidityStartDate().isEmpty()) {
			couponRequestDto.setValidityStartDate(null);
		}
		if (couponRequestDto.getValidityEndDate().isEmpty()) {
			couponRequestDto.setValidityEndDate(null);
		}

		if (couponRequestDto.getPercentageMaxDiscountAmount().isEmpty()) {
			couponRequestDto.setPercentageMaxDiscountAmount(null);
		}

		if (couponRequestDto.getPgPromotionPayConfigId().isEmpty()) {
			couponRequestDto.setPgPromotionPayConfigId(null);
		}

		if (couponRequestDto.getPgPromotionPayGroupId().isEmpty()) {
			couponRequestDto.setPgPromotionPayGroupId(null);
		}

		if (couponRequestDto.getPgPromotionPayId().isEmpty()) {
			couponRequestDto.setPgPromotionPayId(null);
		}

		if (CouponEnums.PaymentType.TICKET.getCode().equals(couponRequestDto.getPaymentType())) {
		 	if (CouponEnums.SerialNumberType.EXCEL_UPLOAD.getCode()
					.equals(couponRequestDto.getSerialNumberType())) { // 엑셀업로드
				List<UploadInfoVo> accountTicketList = couponRequestDto.getUploadTicketList();
				boolean duplicated = accountTicketList.stream()
						.map(UploadInfoVo::getSerialNumber)
						.distinct()
						.count() != accountTicketList.size();
				if(duplicated){
					return ApiResult.result(CouponEnums.FixedNumberValidation.SAME_NUMBER);
				}

				for (int i = 0; i < accountTicketList.size(); i++) {
					UploadInfoVo vo = new UploadInfoVo();
					vo.setSerialNumber(accountTicketList.get(i).getSerialNumber());
//					couponRequestDto.setSerialNumber(vo.getSerialNumber());
					int seralCount = promotionCouponService.getDuplicateSerialNumber(vo);
					if (seralCount > 0) {
						return ApiResult.result(CouponEnums.FixedNumberValidation.DUPLICATE_NUMBER);
					}
				}
			}else if(CouponEnums.SerialNumberType.FIXED_VALUE.getCode()
					.equals(couponRequestDto.getSerialNumberType())){ // 단일코드
				int fixSeralCount = promotionCouponService.getDuplicateFixedNumber(couponRequestDto);		//
				if(fixSeralCount > 0) {  // 단일코드 중복 체크
					return ApiResult.result(CouponEnums.FixedNumberValidation.DUPLICATE_NUMBER);
				}
			}
		}

		// 계정발급 회원 ID 체크
		if (CouponEnums.PaymentType.CHECK_PAYMENT.getCode().equals(couponRequestDto.getPaymentType())) {

			List<UploadInfoVo> accountUserList = couponRequestDto.getUploadUserList();
			int userIdCnt = promotionCouponService.getUserIdCnt(couponRequestDto);

			if(userIdCnt != accountUserList.size()) {
				throw new BosCustomException(CouponEnums.AddCouponValidation.USER_ID_FAIL.getCode(), CouponEnums.AddCouponValidation.USER_ID_FAIL.getMessage());
			}

		}

		// 자동발급 : 이벤트 케이스 issueQtyLimit 0 으로 설정
		if(CouponEnums.IssueDetailType.EVENT_AWARD.getCode().equals(couponRequestDto.getAutoIssueType())) {
			couponRequestDto.setIssueQtyLimit("0");
		}

		// 발급기간 종료일 시간
		if(couponRequestDto.getIssueEndDate() != null && !StringUtils.isEmpty(couponRequestDto.getIssueEndDate())) {
			couponRequestDto.setIssueEndDate(couponRequestDto.getIssueEndDate() + "235959");
		}

		// 유효기간 종료일 시간
		if(couponRequestDto.getValidityEndDate() != null && !StringUtils.isEmpty(couponRequestDto.getValidityEndDate())) {
			couponRequestDto.setValidityEndDate(couponRequestDto.getValidityEndDate() + "235959");
		}


		// 쿠폰 등록
		promotionCouponService.putCoupon(couponRequestDto);

		for (int i = 0; i < updateRequestDtoList.size(); i++) {
			CoverageVo vo = updateRequestDtoList.get(i);
			vo.setPmCouponId(couponRequestDto.getPmCouponId());
			updateRequestDtoList.get(i).setPmCouponId(couponRequestDto.getPmCouponId());
			updateRequestDtoList.get(i).setUserId(couponRequestDto.getUserVo().getUserId());
		}
		if (!updateRequestDtoList.isEmpty()) {
			// 쿠폰 적용범위 삭제
			promotionCouponService.removeCouponCoverage(couponRequestDto);
			// 쿠폰 적용범위 등록
			promotionCouponService.addCouponCoverage(updateRequestDtoList);
		}
		// 분담정보 삭제
		promotionCouponService.removeOrganization(couponRequestDto);
		// 분담정보 등록
		promotionCouponService.addOrganization(couponRequestDto);

		if (CouponEnums.PaymentType.CHECK_PAYMENT.getCode().equals(couponRequestDto.getPaymentType())) {
			CouponIssueParamDto couponIssueParamDto = new CouponIssueParamDto();
			couponIssueParamDto.setPmCouponId(couponRequestDto.getPmCouponId());
			couponIssueParamDto.setCreateId(couponRequestDto.getUserVo().getUserId());

			// 쿠폰 발급/사용 삭제
			promotionCouponService.removeCouponIssue(couponRequestDto);

			List<UploadInfoVo> accountUserList = couponRequestDto.getUploadUserList();

			if (accountUserList != null && !accountUserList.isEmpty()) {

				String issueQtyLimit = couponRequestDto.getIssueQtyLimit();
				int limit = 0;
				String qtyLimit = "";

				if(CouponEnums.IssueType.AUTO_PAYMENT.getCode().equals(couponRequestDto.getPaymentType())) {
					qtyLimit = couponRequestDto.getIssueQtyLimit();
				}else {
					limit = issueQtyLimit.indexOf(".");
					qtyLimit = issueQtyLimit.substring(limit+1);
				}
				int limitCnt = Integer.parseInt(qtyLimit);

				for (int i = 0; i < accountUserList.size(); i++) {

					// couponIssueParamDto.setUrUserId(accountUserList.get(i).getUrUserId());
					couponIssueParamDto.setLoginId(accountUserList.get(i).getLoginId());
					couponIssueParamDto.setPmCouponId(couponRequestDto.getPmCouponId());
					couponIssueParamDto.setCouponIssueType(CouponEnums.CouponIssueType.NORMAL.getCode());
					couponIssueParamDto.setCouponStatus(CouponEnums.CouponStatus.NOTUSE.getCode());

					if (couponRequestDto.getValidityEndDate() != null
							&& !couponRequestDto.getValidityEndDate().isEmpty()) {
						// 상품, 장바구니 쿠폰만료일 설정
						couponIssueParamDto.setValidityStartDate(couponRequestDto.getValidityStartDate());
						couponIssueParamDto.setExpirationDate(couponRequestDto.getValidityEndDate());

					} else {
						Date date = null;
						DateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");

						LocalDateTime endDateTime = LocalDateTime.of(LocalDate.now(), LocalTime.of(23,59,59));

						date = dateFormat.parse(endDateTime.format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")));

						int validityDay = Integer.parseInt(couponRequestDto.getValidityDay());

						Calendar cal = Calendar.getInstance();
						cal.setTime(date);
						cal.add(Calendar.DATE, validityDay);
						String expirationDate = dateFormat.format(cal.getTime());

						// 배송비 쿠폰만료일 설정
						couponIssueParamDto.setValidityStartDate(dateFormat.format(date));
						couponIssueParamDto.setExpirationDate(expirationDate);
					}

					if(accountUserList.get(i).getUrUserId() != null ) {
						couponIssueParamDto.setUrUserId(accountUserList.get(i).getUrUserId());
	    			}else {
	    				couponIssueParamDto.setLoginId(accountUserList.get(i).getLoginId());
	    			}

					for(int k = 0 ; k < limitCnt ; k++ ) {

						// 쿠폰 발급/사용 등록
						promotionCouponService.putCouponIssueList(couponIssueParamDto);

						// 쿠폰 발급/사용 상태 이력 등록
						promotionCouponService.putCouponIssueHistoryList(couponIssueParamDto);
					}
				}
			}

		} else if (CouponEnums.PaymentType.TICKET.getCode().equals(couponRequestDto.getPaymentType())) {

			couponRequestDto.setSerialNumberUseType(CouponEnums.SerialNumberUseType.COUPON.getCode()); // 사용타입 : 쿠폰

			// 개별난수번호 삭제
			promotionCouponService.removeSerialNumber(couponRequestDto);

			couponRequestDto.setSerialNumberStatus(CouponEnums.SerialNumberStatus.ISSUED.getCode()); // 이용권 상태: 발급

			if (CouponEnums.SerialNumberType.AUTO_CREATE.getCode().equals(couponRequestDto.getSerialNumberType())) { // 자동생성

				if (couponRequestDto.getIssueQty() > 0) {
					for (int i = 0; i < couponRequestDto.getIssueQty(); i++) {

						// 개별난수번호 등록 (등록시 난수 생성 function 호출) : 이용권 번호 암호화 후 등록
						promotionCouponService.addSerialNumber(couponRequestDto);
					}
				}

			} else if (CouponEnums.SerialNumberType.EXCEL_UPLOAD.getCode()
					.equals(couponRequestDto.getSerialNumberType())) {

				// 엑셀업로드
				List<UploadInfoVo> accountTicketList = couponRequestDto.getUploadTicketList();
				for (int i = 0; i < accountTicketList.size(); i++) {
					UploadInfoVo vo = new UploadInfoVo();
					vo.setSerialNumber(accountTicketList.get(i).getSerialNumber());
					couponRequestDto.setSerialNumber(vo.getSerialNumber());

					// 개별난수번호 등록 (엑셀에서 저장된 난수로 등록)  : 이용권 번호 암호화 후 등록
					promotionCouponService.addSerialNumber(couponRequestDto);
				}

			}
//			else {
//
//				int fixSeralCount = promotionCouponService.getDuplicateFixedNumber(couponRequestDto);
//
//				if(fixSeralCount > 0) {  // 단일코드 중복 체크
//					return ApiResult.result(CouponEnums.FixedNumberValidation.DUPLICATE_NUMBER);
//				}else {
//					couponRequestDto.setSerialNumberUseType(CouponEnums.SerialNumberUseType.COUPON.getCode()); // 사용타입 : 쿠폰
//					couponRequestDto.setSerialNumberStatus(CouponEnums.SerialNumberStatus.ISSUED.getCode()); // 이용권 상태: 발급
//
//					// 단일코드
//					promotionCouponService.addSerialNumber(couponRequestDto);
//				}
//			}
		}

		// 이전 쿠폰정보 조회
		ApprovalStatusVo history  = promotionCouponService.getCouponStatusHistory(couponRequestDto, "PREV");
		if(history != null) {
			// 쿠폰상태이력 등록
			history.setCreateId(couponRequestDto.getUserVo().getUserId());
			promotionCouponService.addCouponStatusHistory(history);
		}

		return ApiResult.success();
	}

	/**
	 * 쿠폰 삭제
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = {BaseException.class, Exception.class})
	public ApiResult<?> removeCoupon(CouponRequestDto couponRequestDto) throws Exception {

		// 이전 쿠폰정보 조회
		ApprovalStatusVo history  = promotionCouponService.getCouponStatusHistory(couponRequestDto, "PREV");
		if(history != null) {
			// 쿠폰상태이력 등록
//			history.setApprStat(ApprovalStatus.DISPOSAL.getCode());
			history.setCreateId(couponRequestDto.getUserVo().getUserId());
			promotionCouponService.addCouponStatusHistory(history);
		}

		promotionCouponService.removeCoupon(couponRequestDto);

		return ApiResult.success();

	}

	/**
	 * 쿠폰명 수정
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = {BaseException.class, Exception.class})
	public ApiResult<?> updateCouponName(CouponRequestDto couponRequestDto) throws Exception {

		promotionCouponService.updateCouponName(couponRequestDto);

		// 이전 쿠폰정보 조회
		ApprovalStatusVo history  = promotionCouponService.getCouponStatusHistory(couponRequestDto, "PREV");

		if(history != null) {
			history.setApprStat(history.getPrevApprStat());
			history.setMasterStat(history.getPrevMasterStat());
			history.setApprSubUserId(history.getApprSubUserId());
			history.setCreateId(couponRequestDto.getUserVo().getUserId());
			// 쿠폰상태이력 등록
			promotionCouponService.addCouponStatusHistory(history);
		}

		return ApiResult.success();

	}

	/**
	 * 쿠폰 복사
	 */
	@Override
	public ApiResult<?> getCopyCoupon(CouponRequestDto couponRequestDto) throws Exception {

		CouponDetailResponseDto result = new CouponDetailResponseDto();
		CouponDetailVo vo = new CouponDetailVo();
		vo = promotionCouponService.getCopyCoupon(couponRequestDto);
		result.setRows(vo);

		return ApiResult.success(result);

	}

	/**
	 * 쿠폰지급 내역 조회
	 *
	 * @param couponRequestDto
	 * @return CouponListResponseDto
	 * @throws Exception
	 */
	@Override
	@UserMaskingRun(system = "MUST_MASKING")
	public ApiResult<?> getCpnMgmList(CouponRequestDto couponRequestDto) throws Exception {
		CouponResponseDto result = new CouponResponseDto();

		if (!couponRequestDto.getCondiValue().isEmpty()) {
			ArrayList<String> array = new ArrayList<>();
			StringTokenizer st = new StringTokenizer(couponRequestDto.getCondiValue(), "\n|,");
			while (st.hasMoreElements()) {
				String object = (String) st.nextElement();
				array.add(object);
			}
			couponRequestDto.setCondiValueArray(array);
		}

		Page<IssueListResultVo> couponList = promotionCouponService.getCpnMgmList(couponRequestDto);

		result.setTotal(couponList.getTotal());
		result.setRows(couponList.getResult());

		return ApiResult.success(result);
	}

	/**
	 * 쿠폰지급 (선택회원)
	 *
	 * @param couponRequestDto
	 * @return CouponListResponseDto
	 * @throws Exception
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = {BaseException.class, Exception.class})
	public ApiResult<?> putCouponIssueList(CouponRequestDto couponRequestDto) throws Exception {
		BuyerInfoResponseDto result = new BuyerInfoResponseDto();

		couponRequestDto.setUpdateRequestDtoList(BindUtil.convertJsonArrayToDtoList(couponRequestDto.getUpdateData(), BuyerInfoListResultVo.class));

		List<BuyerInfoListResultVo> updateRequestDtoList = couponRequestDto.getUpdateRequestDtoList();

		result.setRows(updateRequestDtoList);
		result.setUpdateCount(updateRequestDtoList.size());

		return ApiResult.success(result);
	}

	/**
	 * 쿠폰지정 조회
	 *
	 * @param buyerInfoRequestDto
	 * @return CouponListResponseDto
	 * @throws Exception
	 */
	@Override
	public ApiResult<?> getCpnMgmIssueList(BuyerInfoRequestDto buyerInfoRequestDto) throws Exception {
		BuyerInfoResponseDto result = new BuyerInfoResponseDto();

		if (!buyerInfoRequestDto.getCondiValue().isEmpty()) {
			ArrayList<String> array = new ArrayList<>();
			StringTokenizer st = new StringTokenizer(buyerInfoRequestDto.getCondiValue(), "\n|,");
			while (st.hasMoreElements()) {
				String object = (String) st.nextElement();
				array.add(object);
			}
			buyerInfoRequestDto.setCondiValueArray(array);
		}

		Page<BuyerInfoListResultVo> couponList = promotionCouponService.getCpnMgmIssueList(buyerInfoRequestDto);

		result.setTotal(couponList.getTotal());
		result.setRows(couponList.getResult());

		return ApiResult.success(result);
	}

	/**
	 * 쿠폰지급 (검색회원)
	 *
	 * @param issueUserRequestDto
	 * @return CouponListResponseDto
	 * @throws Exception
	 */
	@Override
	public ApiResult<?> putSearchCouponIssueList(IssueUserRequestDto issueUserRequestDto) throws Exception {
		BuyerInfoResponseDto result = new BuyerInfoResponseDto();

		if (issueUserRequestDto.getCondiValue() != null && !issueUserRequestDto.getCondiValue().isEmpty()) {
			ArrayList<String> array = new ArrayList<>();
			StringTokenizer st = new StringTokenizer(issueUserRequestDto.getCondiValue(), "\n|,");
			while (st.hasMoreElements()) {
				String object = (String) st.nextElement();
				array.add(object);
			}
			issueUserRequestDto.setCondiValueArray(array);
		}

		// 검색회원 리스트
		List<BuyerInfoListResultVo> buyerInfoListResultVoList = promotionCouponService
				.getCpnMgmIssueDuplicateParamList(issueUserRequestDto);

		result.setUpdateCount(buyerInfoListResultVoList.size());
		result.setRows(buyerInfoListResultVoList);

		return ApiResult.success(result);
	}

	/**
	 * 발급된 쿠폰 선택 회수 (상태값 변경)
	 *
	 * @param couponRequestDto
	 * @return CouponListResponseDto
	 * @throws Exception
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = {BaseException.class, Exception.class})
	public ApiResult<?> putCancelDepositList(CouponRequestDto couponRequestDto) throws Exception {
		CouponResponseDto result = new CouponResponseDto();

		if (!couponRequestDto.getUpdateData().isEmpty()) {
			couponRequestDto.setUpdateRequestDtoList((List<BuyerInfoListResultVo>) BindUtil
					.convertJsonArrayToDtoList(couponRequestDto.getUpdateData(), BuyerInfoListResultVo.class));
		}

		// 선택된 쿠폰리스트
		List<BuyerInfoListResultVo> updateRequestDtoList = couponRequestDto.getUpdateRequestDtoList();

		if (!updateRequestDtoList.isEmpty()) {
			CouponIssueParamDto couponIssueParamDto = new CouponIssueParamDto();
			couponIssueParamDto.setCreateId(couponRequestDto.getUserVo().getUserId());
			for (BuyerInfoListResultVo vo : updateRequestDtoList) {

				couponIssueParamDto.setPmCouponIssueId(vo.getPmCouponIssueId());
				couponIssueParamDto.setCouponStatus(CouponEnums.CouponStatus.CANCEL.getCode());

				// 쿠폰_발급/사용 정보 수정(미사용->발급취소)
				promotionCouponService.putCancelDepositList(couponIssueParamDto);
				// 쿠폰_발급/사용 상태이력 등록(미사용->발급취소)
				promotionCouponService.putCouponIssueHistoryList(couponIssueParamDto);

			}
		}
		return ApiResult.success(result);
	}

	/**
	 * 쿠폰 승인요청
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = {BaseException.class, Exception.class})
	public ApiResult<?> putCouponStatus(CouponRequestDto couponRequestDto) throws Exception {

		if (CouponEnums.CouponMasterStatus.STOP_WITHDRAW.getCode().equals(couponRequestDto.getCouponMasterStat())) {

			couponRequestDto.setCouponMasterStat(CouponEnums.CouponMasterStatus.STOP.getCode());

			// 쿠폰 상태변경 (쿠폰중지)
			promotionCouponService.putPmCouponStatus(couponRequestDto);

			couponRequestDto.setCouponStatus(CouponEnums.CouponStatus.CANCEL.getCode()); // 취소상태로 변경

			List<IssueListResultVo> couponIssueList = promotionCouponService.getCouponIssueInfo(couponRequestDto);

			if (!couponIssueList.isEmpty()) {
				for (int i = 0; i < couponIssueList.size(); i++) {
					CouponIssueParamDto couponIssueParamDto = new CouponIssueParamDto();
					couponIssueParamDto.setCreateId(couponRequestDto.getUserVo().getUserId());
					couponIssueParamDto.setPmCouponIssueId(couponIssueList.get(i).getPmCouponIssueId());
					couponIssueParamDto.setCouponStatus(CouponEnums.CouponStatus.CANCEL.getCode()); // 취소상태로 변경
					// 쿠폰 발급/사용 상태이력 등록
					promotionCouponService.putCouponIssueHistoryList(couponIssueParamDto);
				}
			}
			// 쿠폰 발급/사용 상태변경
			promotionCouponService.putPmCouponIssueStatus(couponRequestDto);
		} else {
			// 쿠폰 상태변경 (쿠폰중지)
			promotionCouponService.putPmCouponStatus(couponRequestDto);

		}

		// 이전 쿠폰정보 조회
		ApprovalStatusVo history  = promotionCouponService.getCouponStatusHistory(couponRequestDto, "PREV");

		if(history != null) {
			// 쿠폰상태이력 등록
			history.setCreateId(couponRequestDto.getUserVo().getUserId());
			promotionCouponService.addCouponStatusHistory(history);
		}

		return ApiResult.success();

	}

	/**
	 * 쿠폰 지급내역 엑셀 선택 다운로드
	 */
	@Override
	public ExcelDownloadDto getCouponIssueListExcelDownload(CouponRequestDto couponRequestDto) throws Exception {
		String excelFileName = "쿠폰("+couponRequestDto.getPmCouponId() +")발급내역_"+LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")); // 엑셀 파일 이름: 확장자는 xlsx 	자동 설정됨
		String excelSheetName = "sheet"; // 엑셀 파일 내 워크시트 이름

		/*
		 * 배열 내 순서가 엑셀 본문의 컬럼 순서와 매칭됨
		 *
		 */

		/*
		 * 컬럼별 width 목록 : 단위 pixel
		 * ( 필수 아님 ) ExcelWorkSheetDto 에 지정하지 않을 경우 모든 컬럼 너비는 120 pixel 로 고정됨
		 */
		Integer[] widthListOfFirstWorksheet = { //
				400, 250, 350, 250, 250, 250, 250, 250, 250, 250, 250, 500 };

		/*
		 * 본문 데이터 컬럼별 정렬 목록
		 * ( 필수 아님 ) ExcelWorkSheetDto 에 지정하지 않을 경우 모든 컬럼 정렬은 "left" (좌측 정렬) 로 고정
		 * "left", "center", "right", "justify", "distributed" 가 아닌 다른 값 지정시 "left" (좌측 정렬) 로 지정됨
		 */
		String[] alignListOfFirstWorksheet = { //
				"center", "center", "center", "center", "center", "center", "center", "center", "center", "center", "center", "center", "left" };

		/*
		 * 본문 데이터 컬럼별 데이터 property 목록
		 * ( 필수 ) 엑셀 데이터 JSON 변환시 각 json Object 의 key 값과 일치해야 함
		 */
		String[] propertyListOfFirstWorksheet = { //
				"bosCouponName", "discountValue", "validityPeroid", "issueDate", "remainingPeriod", "goodsNm", "couponUseDate", "couponUseTime", "userInfo", "odid", "ilGoodsId", "statusComment" };

		/*
		 * 첫 번째 워크시트의 상단 헤더의 각 행별 정보 : 다단 구성, 셀 머지 가능
		 *
		 */
		String[] firstHeaderListOfFirstWorksheet = { // 첫 번째 헤더 컬럼 : 동일 이름 연속시 셀 머지
				"쿠폰명", "할인", "유효기간", "발급일자", "잔여기간", "쿠폰적용 상품명", "사용일자", "사용시각", "회원정보(ID)", "주문번호", "적용상품코드", "사유"
		};

		/*
		 * 워크시트 DTO 생성 후 정보 세팅
		 *
		 */
		ExcelWorkSheetDto firstWorkSheetDto = ExcelWorkSheetDto.builder() //
				.workSheetName(excelSheetName) // 엑셀 파일내 워크시트 명
				.propertyList(propertyListOfFirstWorksheet) // 컬럼별 데이터 property 목록
				.widthList(widthListOfFirstWorksheet) // 컬럼별 너비 목록
				.alignList(alignListOfFirstWorksheet) // 컬럼별 정렬 목록
				.build();

		firstWorkSheetDto.setHeaderList(0, firstHeaderListOfFirstWorksheet);

		if (!couponRequestDto.getCondiValue().isEmpty()) {
			ArrayList<String> array = new ArrayList<>();
			StringTokenizer st = new StringTokenizer(couponRequestDto.getCondiValue(), "\n|,");
			while (st.hasMoreElements()) {
				String object = (String) st.nextElement();
				array.add(object);
			}
			couponRequestDto.setCondiValueArray(array);
		}


		List<IssueListResultVo> couponIssueList = null;
		try
		{
			couponIssueList = promotionCouponService.getCouponIssueListExcel(couponRequestDto);
			couponIssueList.forEach(vo -> {
				vo.setUserInfo(vo.getUserNm() + "(" + vo.getLoginId() + ")");
			});
			log.info("couponIssueList {}", couponIssueList);
		}
		catch (Exception e)
		{
			log.error(e.getMessage());
			throw e; // 추후 CustomException 으로 변환 예정
		}
		firstWorkSheetDto.setExcelDataList(couponIssueList);

		ExcelDownloadDto excelDownloadDto = ExcelDownloadDto.builder().excelFileName(excelFileName).build();

		excelDownloadDto.addExcelWorkSheet(firstWorkSheetDto);

		return excelDownloadDto;
	}

	@Override
	public List<IssueListResultVo> getCouponIssueListExcel(CouponRequestDto couponRequestDto) throws Exception {
		return promotionCouponService.getCouponIssueListExcel(couponRequestDto);
	}

	/**
	 * 상품 적용가능한 쿠폰 리스트
	 *
	 * @param ilGoodsId
	 * @return List<GoodsApplyCouponDto>
	 * @throws Exception
	 */
	@Override
	public List<GoodsApplyCouponDto> getGoodsApplyCouponList(Long ilGoodsId, Long urUserId) throws Exception {

		return promotionCouponService.getGoodsApplyCouponList(ilGoodsId, urUserId);

	}

	/**
	 * 할인금액 계산
	 *
	 * @param salePrice, couponDiscountStatus, couponDiscountRate
	 * @return DiscountCalculationResultDto
	 * @throws Exception
	 */
	@Override
	public DiscountCalculationResultDto discountCalculation(int salePrice, String couponDiscountStatus,
			int couponDiscountRate, int maxDiscountValue) throws Exception {
		return promotionCouponService.calculationDiscount(salePrice, couponDiscountStatus, couponDiscountRate, maxDiscountValue);
	}

	/**
	 * 신규회원특가 쿠폰 조회 - 비회원일 경우
	 *
	 * @param ilGoodsId
	 * @return HashMap
	 * @throws Exception
	 */
	@Override
	public int getNewBuyerSpecialsCouponByNonMember(Long ilGoodsId, String deviceInfo, boolean isApp) throws Exception {
		return promotionCouponService.getNewBuyerSpecialsCouponByNonMember(ilGoodsId, deviceInfo, isApp);
	}

	/**
	 * 쿠폰목록조회
	 *
	 * @param dto CommonGetCouponListByUserRequestDto
	 * @return CouponListByUserResponseDto
	 * @throws Exception exception
	 */
	@Override
	public CouponListByUserResponseDto getCouponListByUser(CouponListByUserRequestDto dto) throws Exception {
		return promotionCouponService.getCouponListByUser(dto);
	}

	/**
	 * 쿠폰 적용대상
	 *
	 * @param pmCouponId Long
	 * @return CouponCoverageResponseDto
	 * @throws Exception exception
	 */
	@Override
	public CouponCoverageResponseDto getCouponCoverage(Long pmCouponId) throws Exception {
		return promotionCouponService.getCouponCoverage(pmCouponId);
	}

	/**
	 * 유저 쿠폰등록 검증
	 *
	 * @param urUserId   Long
	 * @param pmCouponId Long
	 * @return CouponEnums.AddCouponValidation
	 * @throws Exception exception
	 */
	@Override
	public CouponValidationByUserResponseDto checkCouponValidationByUser(Long urUserId, Long pmCouponId) throws Exception {
		return promotionCouponService.checkAddCouponValidationByUser(urUserId, pmCouponId);
	}

	/**
	 * 유저 쿠폰등록
	 *
	 * @param dto              CommonGetAddCouponValidationInfoVo
	 * @param urUserId         Long
	 * @param pmSerialNumberId Long
	 * @throws Exception exception
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = {BaseException.class, Exception.class})
	public void addCouponWithoutValidation(CouponValidationInfoVo dto, Long urUserId, Long pmSerialNumberId) throws Exception {
		promotionCouponService.addCouponByUser(dto, urUserId, pmSerialNumberId);
	}

	/**
	 * 쿠폰수량 조회
	 *
	 * @param urUserId Long
	 * @param status   String
	 * @return CommonGetCouponCountByUserVo
	 * @throws Exception exception
	 */
	@Override
	public CouponCountByUserVo getCouponCountByUser(Long urUserId, String status) throws Exception {
		CouponCountByUserVo response = promotionCouponService.getCouponCountByUser(urUserId, status);

		if(response == null){
			response = new CouponCountByUserVo();
		}

		return response;
	}

	/**
	 * 쿠폰수량 등록
	 *
	 * @param pmCouponIds List<Long>
	 * @param urUserId    Long
	 * @return ApiResult<?>
	 * @throws Exception exception
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = {BaseException.class, Exception.class})
	public ApiResult<?> addCouponByList(List<Long> pmCouponIds, Long urUserId) throws Exception {
		boolean couponError = false;
		int couponErrorCount = 0;
		CouponEnums.AddCouponValidation couponErrorEnum = CouponEnums.AddCouponValidation.ETC;
		for (Long pmCouponId : pmCouponIds) {
			CouponValidationByUserResponseDto couponResponseDto = promotionCouponService
					.checkAddCouponValidationByUser(urUserId, pmCouponId);
			// 오류처리
			if (!CouponEnums.AddCouponValidation.PASS_VALIDATION.equals(couponResponseDto.getValidationEnum())) {
				couponError = true;
				couponErrorCount++;
				couponErrorEnum = couponResponseDto.getValidationEnum();
			} else{
				// 쿠폰 등록
				promotionCouponService.addCouponByUser(couponResponseDto.getData(), urUserId, null);
			}
		}

		if(couponError){
			if(pmCouponIds.size() == 1){
				return ApiResult.result(couponErrorEnum);
			}
			if(couponErrorCount == pmCouponIds.size()){
				return ApiResult.result(CouponEnums.AddCouponValidation.OVER_ISSUE_QTY_LIMIT);
			}else if(pmCouponIds.size() > 2){
				return ApiResult.result(CouponEnums.AddCouponValidation.MULTIPLE_COUPON_ERROR);
			}
		}

		return ApiResult.success();
	}

	/**
	 * 쿠폰 등록
	 *
	 * @param pmCouponId Long
	 * @param urUserId    Long
	 * @return ApiResult<?>
	 * @throws Exception exception
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = {BaseException.class, Exception.class})
	public CouponEnums.AddCouponValidation addCouponByOne(Long pmCouponId, Long urUserId) throws Exception {
		CouponValidationByUserResponseDto couponResponseDto = promotionCouponService.checkAddCouponValidationByUser(urUserId, pmCouponId);

		// 오류처리
		if (!CouponEnums.AddCouponValidation.PASS_VALIDATION.equals(couponResponseDto.getValidationEnum())) {
			return couponResponseDto.getValidationEnum();
		}

		// 쿠폰 등록
		promotionCouponService.addCouponByUser(couponResponseDto.getData(), urUserId, null);

		return couponResponseDto.getValidationEnum();
	}

	/**
	 * 쿠폰 리스트 사용 조건 체크
	 *
	 * @param pmCouponIssueIds
	 * @return
	 * @throws Exception
	 */
	@Override
	public boolean isUseCouponListValidation(Long urUserId, List<Long> pmCouponIssueIds) throws Exception {
		for (Long pmCouponIssueId : pmCouponIssueIds) {
			CouponEnums.UseCouponValidation validation = promotionCouponService.checkUseCouponValidation(urUserId, pmCouponIssueId);
			if (!CouponEnums.UseCouponValidation.PASS_VALIDATION.equals(validation)) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 쿠폰 사용
	 *
	 * @param pmCouponIssueId List<Long>
	 * @param urUserId    Long
	 * @return ApiResult<?>
	 * @throws Exception exception
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = {BaseException.class, Exception.class})
	public ApiResult<?> useCoupon(Long urUserId, Long pmCouponIssueId) throws Exception {
		// Validation Check
		CouponEnums.UseCouponValidation validation = promotionCouponService.checkUseCouponValidation(urUserId, pmCouponIssueId);
		if (!CouponEnums.UseCouponValidation.PASS_VALIDATION.equals(validation)) {
			// 오류처리
			return ApiResult.result(validation);
		}

		// 쿠폰 사용처리
		if(promotionCouponService.useCoupon(urUserId, pmCouponIssueId)) {
			return ApiResult.success();
		} else {
			return ApiResult.result(CouponEnums.UseCouponValidation.NOT_COUPON_STATUS);
		}
	}

	/**
	 * 회원가입 - 대상 쿠폰 조회
	 *
	 * @return CouponInfoByUserJoinVo
	 * @throws Exception exception
	 */
	@Override
	public List<CouponInfoByUserJoinVo> getCouponInfoByUserJoin() throws Exception {
		return promotionCouponService.getCouponInfoByUserJoin();
	}

	@Override
	public List<CouponGoodsByUserJoinVo> getUserJoinGoods() throws Exception {
		return promotionCouponService.getUserJoinGoods();
	}

	/**
	 * 주문 사용 가능 상품 쿠폰 조회
	 *
	 * @param urUserId
	 * @param List<CartGoodsDto>
	 * @param DeviceType
	 * @return List<GoodsCouponDto>
	 * @throws Exception
	 */
	@Override
	public List<GoodsCouponDto> getGoodsCouponApplicationListByUser(Long urUserId, List<CartGoodsDto> cartGoodsList,
			DeviceType deviceType) throws Exception {
		List<GoodsCouponDto> goodsCouponList = new ArrayList<>();

		int totalRecommendedPriceMltplQty = 0;

		for (CartGoodsDto goodsDto : cartGoodsList) {
			// 일일상품 제외인
			if (!GoodsEnums.GoodsType.DAILY.getCode().equals(goodsDto.getGoodsType())
					&& !CartPromotionType.EXHIBIT_SELECT.getCode().equals(goodsDto.getCartPromotionType())) {
				totalRecommendedPriceMltplQty += goodsDto.getRecommendedPriceMltplQty();
			}
		}

		for (CartGoodsDto goodsDto : cartGoodsList) {

			// 일일상품 제외인
			if (!GoodsEnums.GoodsType.DAILY.getCode().equals(goodsDto.getGoodsType())
					&& !CartPromotionType.EXHIBIT_SELECT.getCode().equals(goodsDto.getCartPromotionType())) {
				List<CouponDto> couponList = new ArrayList<>();

				// 주문 사용 가능 상품 쿠폰 리스트 조회
				CouponApplicationListRequestDto couponApplicationListRequestDto = new CouponApplicationListRequestDto();
				couponApplicationListRequestDto.setUrUserId(urUserId);
				couponApplicationListRequestDto.setIlGoodsId(goodsDto.getIlGoodsId());
				couponApplicationListRequestDto.setDpBrandId(goodsDto.getDpBrandId());
				couponApplicationListRequestDto.setUrWareHouseId(goodsDto.getUrWareHouseId());
				couponApplicationListRequestDto.setIlCtgryId(goodsDto.getIlCtgryId());

				List<String> couponTypeList = new ArrayList<>();
				couponTypeList.add(CouponEnums.CouponType.GOODS.getCode());
				couponTypeList.add(CouponEnums.CouponType.SALEPRICE_APPPOINT.getCode());
				couponApplicationListRequestDto.setCouponType(couponTypeList);

				List<CouponApplicationListByUserVo> goodsCouponApplicationList = promotionCouponService.getCouponApplicationListByUser(couponApplicationListRequestDto);

				for (CouponApplicationListByUserVo goodsCoupon : goodsCouponApplicationList) {

					// 판매가 지정할인 쿠폰은 장바구니 총 합계가 조건에 맞아야 사용 가능. SPMO-1232
					if("COUPON_TYPE.SALEPRICE_APPPOINT".equals(goodsCoupon.getCouponType())) {
						if(totalRecommendedPriceMltplQty < GoodsConstants.MINIMUM_PRICE_FOR_SALEPRICE_APPPOINT) {
							continue;
						}
					}

					CouponDto couponDto = goodsCouponApplication(goodsDto, goodsCoupon, deviceType);
					if(couponDto != null) {
						couponList.add(couponDto);
					}
				}

				if (!couponList.isEmpty()) {
					// 상품 쿠폰 리스트 정보 세팅
					GoodsCouponDto goodsCouponDto = new GoodsCouponDto();
					goodsCouponDto.setSpCartId(goodsDto.getSpCartId());
					goodsCouponDto.setGoodsName(goodsDto.getGoodsName());
					goodsCouponDto.setIlGoodsId(goodsDto.getIlGoodsId());
					goodsCouponDto.setNewBuyerSpecials(goodsDto.isNewBuyerSpecials());
					goodsCouponDto.setNewBuyerSpecialsSalePrice(goodsDto.getNewBuyerSpecialSsalePrice());
					goodsCouponDto.setCouponList(couponList);
					goodsCouponList.add(goodsCouponDto);
				}
			}
		}

		return goodsCouponList;
	}

	private CouponDto goodsCouponApplication(CartGoodsDto goodsDto, CouponApplicationListByUserVo goodsCoupon,DeviceType deviceType) throws Exception {

		// 발행구분(PC/MO/APP) 체크
		boolean isActive = promotionCouponService.isDeviceTypeActive(deviceType, goodsCoupon);

		// 상품 개당 판매금액
		int salePricePerUnit = goodsDto.getPaymentPrice() / goodsDto.getQty();

		//판매가지정쿠폰일 경우
		if(CouponEnums.CouponType.SALEPRICE_APPPOINT.getCode().equals(goodsCoupon.getCouponType())) {

			int discountPrice = salePricePerUnit - goodsCoupon.getDiscountValue(); //쿠폰 할인금액
			int paymentPrice = goodsDto.getPaymentPrice() - discountPrice;//쿠폰 할인이 적용된 최종 상품 결제금액

			// 쿠폰 정보 세팅
			CouponDto couponDto = new CouponDto();
			couponDto.setDisplayCouponName(goodsCoupon.getDisplayCouponName());
			couponDto.setPmCouponIssueId(goodsCoupon.getPmCouponIssueId());
			couponDto.setDiscountPrice(discountPrice);
			couponDto.setPaymentPrice(paymentPrice);
			couponDto.setUseCartCouponYn(goodsCoupon.getCartCouponApplyYn());
			couponDto.setActive(isActive);
			couponDto.setSalePrice(salePricePerUnit);
			return couponDto;

		//상품쿠폰일 경우
		} else {
			// 쿠폰 최소금액 충족 여부 체크
			if (goodsDto.getPaymentPrice() >= Integer.parseInt(String.valueOf(goodsCoupon.getMinPaymentAmount()))) {

				//쿠폰 할인금액 계산
				String discountMethodType = goodsCoupon.getDiscountType();
				int discountRate = goodsCoupon.getDiscountValue();
				DiscountCalculationResultDto discountResult = promotionCouponService.calculationDiscount(salePricePerUnit, discountMethodType, discountRate, StringUtil.nvlInt(goodsCoupon.getPercentageMaxDiscountAmount()));
				if (isActive) {
					isActive = discountResult.isActive();
				}

				int discountPrice = discountResult.getDiscountPrice(); //쿠폰 할인금액
				int paymentPrice = goodsDto.getPaymentPrice() - discountPrice; //쿠폰 할인이 적용된 최종 상품 결제금액

				// 쿠폰 정보 세팅
				CouponDto couponDto = new CouponDto();
				couponDto.setDisplayCouponName(goodsCoupon.getDisplayCouponName());
				couponDto.setPmCouponIssueId(goodsCoupon.getPmCouponIssueId());
				couponDto.setSalePrice(salePricePerUnit);
				couponDto.setDiscountPrice(discountPrice);
				couponDto.setPaymentPrice(paymentPrice);
				couponDto.setUseCartCouponYn(goodsCoupon.getCartCouponApplyYn());
				couponDto.setActive(isActive);
				return couponDto;
			}
		}

		return null;
	}

	/**
	 * 주문 사용 가능 장바구니 쿠폰 조회
	 *
	 * @param urUserId
	 * @param List<CartGoodsDto>
	 * @param DeviceType
	 * @return List<CouponDto>
	 * @throws Exception
	 */
	@Override
	public List<CouponDto> getCartCouponApplicationListByUser(Long urUserId, List<CartGoodsDto> cartGoodsList,
			DeviceType deviceType) throws Exception {
		List<CouponDto> cartCouponList = new ArrayList<>();

		// 회원이 가지고 있는 장바구니 쿠폰 리스트 조회
		List<CouponApplicationListByUserVo> cartCouponApplicationList = promotionCouponService.getCartCouponApplicationListByUser(urUserId);

		if (!cartCouponApplicationList.isEmpty()) {
			for (CouponApplicationListByUserVo cartCoupon : cartCouponApplicationList) {

				CouponDto couponDto = cartCouponApplication(urUserId, cartGoodsList, cartCoupon, deviceType);
				if(couponDto != null) {
					cartCouponList.add(couponDto);
				}
			}
		}

		return cartCouponList;
	}

	private CouponDto cartCouponApplication(Long urUserId, List<CartGoodsDto> cartGoodsList, CouponApplicationListByUserVo cartCoupon, DeviceType deviceType) throws Exception {
		int goodsTotalSalePrice = 0;
		String additionalNoticeYn = "N";

		List<CartCouponApplicationGoodsDto> cartCouponApplicationGoodsList = new ArrayList<>();

		// 장바구니 쿠폰이 적용 가능한 상품인지 확인
		for (CartGoodsDto goodsDto : cartGoodsList) {

			List<CouponApplicationListByUserVo> couponList = null;
			// 골라담기는 장바구니 적용 제외
			if (!ShoppingEnums.CartPromotionType.EXHIBIT_SELECT.getCode().equals(goodsDto.getCartPromotionType())
					&& goodsDto.getPaymentPrice() > 0) {
				// 주문 사용 가능 장바구니 쿠폰 리스트 조회
				CouponApplicationListRequestDto couponApplicationListRequestDto = new CouponApplicationListRequestDto();
				couponApplicationListRequestDto.setUrUserId(urUserId);
				couponApplicationListRequestDto.setIlGoodsId(goodsDto.getIlGoodsId());
				couponApplicationListRequestDto.setDpBrandId(goodsDto.getDpBrandId());
				couponApplicationListRequestDto.setUrWareHouseId(goodsDto.getUrWareHouseId());
				couponApplicationListRequestDto.setIlCtgryId(goodsDto.getIlCtgryId());
				couponApplicationListRequestDto.setPmCouponIssueId(cartCoupon.getPmCouponIssueId());

				List<String> couponTypeList = new ArrayList<>();
				couponTypeList.add(CouponEnums.CouponType.CART.getCode());
				couponApplicationListRequestDto.setCouponType(couponTypeList);

				couponList = promotionCouponService.getCouponApplicationListByUser(couponApplicationListRequestDto);
			}

			if (couponList != null && couponList.size() > 0) {
				goodsTotalSalePrice += goodsDto.getPaymentPrice();

				// 장바구니 쿠폰 적용한 상품 정보 DTO 생성
				CartCouponApplicationGoodsDto cartCouponApplicationGoods = new CartCouponApplicationGoodsDto();
				cartCouponApplicationGoods.setSpCartId(goodsDto.getSpCartId());
				cartCouponApplicationGoods.setPaymentPrice(goodsDto.getPaymentPrice());
				cartCouponApplicationGoods.setGoodsPackage(goodsDto.getGoodsPackage());
				cartCouponApplicationGoodsList.add(cartCouponApplicationGoods);
			} else {
				additionalNoticeYn = "Y";
			}
		}

		// 최소 결제 금액 충족 시에만 노출
		if (goodsTotalSalePrice > 0 && goodsTotalSalePrice >= cartCoupon.getMinPaymentAmount()) {
			// 발행구분(PC/MO/APP) 체크
			boolean isActive = promotionCouponService.isDeviceTypeActive(deviceType, cartCoupon);
			// 쿠폰 할인 금액 계산
			String discountMethodType = cartCoupon.getDiscountType();
			int discountRate = cartCoupon.getDiscountValue();
			DiscountCalculationResultDto discountResult = promotionCouponService.calculationDiscount(goodsTotalSalePrice, discountMethodType, discountRate, StringUtil.nvlInt(cartCoupon.getPercentageMaxDiscountAmount()));
			if(isActive) {
				isActive = discountResult.isActive();
			}
			int discountPrice = discountResult.getDiscountPrice();
			// 쿠폰 정보 세팅
			CouponDto couponDto = new CouponDto();
			couponDto.setDisplayCouponName(cartCoupon.getDisplayCouponName());
			couponDto.setPmCouponIssueId(cartCoupon.getPmCouponIssueId());
			couponDto.setSalePrice(goodsTotalSalePrice);
			couponDto.setDiscountPrice(discountPrice);
			couponDto.setPaymentPrice(discountResult.getDiscountAppliedPrice());
			couponDto.setPgPromotionYn(cartCoupon.getPgPromotionYn());
			couponDto.setPsPayCd(cartCoupon.getPgPromotionPayGrpId());
			couponDto.setCardCode(cartCoupon.getPgPromotionPayId());
			couponDto.setMinPaymentAmount(cartCoupon.getMinPaymentAmount());
			couponDto.setActive(isActive);
			couponDto.setAdditionalNoticeYn(additionalNoticeYn);
			couponDto.setCartCouponApplicationGoodsList(cartCouponApplicationGoodsList);
			return couponDto;
		}
		return null;
	}

	/**
	 * 주문 사용 가능 배송비 쿠폰 조회
	 *
	 * @param urUserId
	 * @param List<CartDeliveryDto>
	 * @param DeviceType
	 * @return List<ShippingCouponDto>
	 * @throws Exception
	 */
	@Override
	public List<ShippingCouponDto> getShippingCouponApplicationListByUser(Long urUserId,
			List<CartDeliveryDto> cartDataDto, DeviceType deviceType) throws Exception {
		List<ShippingCouponDto> shippingCouponList = new ArrayList<>();

		for (CartDeliveryDto deliveryDto : cartDataDto) {
			for (CartShippingDto shippingDto : deliveryDto.getShipping()) {

				// 무료배송 제외
				if (shippingDto.getShippingPaymentPrice() != 0) {
					List<CouponDto> couponList = new ArrayList<>();

					// 주문 사용 가능 배송비 쿠폰 리스트 조회
					List<CouponApplicationListByUserVo> shippingCouponApplicationList = promotionCouponService.getShippingCouponApplicationListByUser(urUserId, shippingDto.getUrWarehouseId());

					for (CouponApplicationListByUserVo shippingCouponApplication : shippingCouponApplicationList) {
						CouponDto couponDto = shippingCouponApplication(shippingDto, shippingCouponApplication, deviceType);
						if(couponDto != null) {
							couponList.add(couponDto);
						}
					}

					// 쿠폰리스트 있을 경우에만 노출
					if (!couponList.isEmpty()) {
						// 상품명 리스트 추출
						List<String> compositionGoodsNameList = shippingDto.getGoods().stream()
								.map(CartGoodsDto::getGoodsName)
								.collect(Collectors.toList());

						String compositionGoodsName = compositionGoodsNameList.get(0);
						if (compositionGoodsNameList.size() > 1) {
							compositionGoodsName = compositionGoodsNameList.get(0) + "외 "
									+ (compositionGoodsNameList.size() - 1) + "건";
						}

						ShippingCouponDto shippingCouponDto = new ShippingCouponDto();
						shippingCouponDto.setShippingIndex(shippingDto.getShippingIndex());
						shippingCouponDto.setCompositionGoodsName(compositionGoodsName);
						shippingCouponDto.setShippingRecommendedPrice(shippingDto.getShippingRecommendedPrice());
						shippingCouponDto.setCouponList(couponList);
						shippingCouponList.add(shippingCouponDto);
					}
				}

			}
		}

		return shippingCouponList;
	}

	private CouponDto shippingCouponApplication(CartShippingDto shippingDto, CouponApplicationListByUserVo shippingCouponApplication, DeviceType deviceType) throws Exception {
		// 배송정책별 상품 총 결제금액
		int shippingSalePrice = shippingDto.getShippingPaymentPrice();
		int targetSaleShippingPrice = shippingDto.getShippingPaymentPrice();

		ShippingDataResultVo subShippingDataResultVo = goodsShippingTemplateBiz.getShippingInfoByShippingTmplId(shippingDto.getIlShippingTmplId());
		// getShippingCouponApplicationListByUser 에서 필요한 금액 정보 SET
		if (GoodsEnums.ConditionType.TYPE5.getCode().equals(subShippingDataResultVo.getConditionType())) {
			// 상품 수량당 배송비는 1개 기준으로만 할인 되어야함
			targetSaleShippingPrice = subShippingDataResultVo.getShippingPrice();
		}

		// 최소 결제 금액 충족 시에만 노출
		if (shippingDto.getGoodsSalePrice() >= Integer
				.parseInt(String.valueOf(shippingCouponApplication.getMinPaymentAmount()))) {
			// 발행구분(PC/MO/APP) 체크
			boolean isActive = promotionCouponService.isDeviceTypeActive(deviceType, shippingCouponApplication);

			// 쿠폰 할인 금액 계산
			int discountPrice = Integer.valueOf(shippingCouponApplication.getPercentageMaxDiscountAmount()); // 쿠폰 최대 할인 금액

			// 최대할인금액이 0원인 경우 배송비 전액 할인
			if (discountPrice == 0 || targetSaleShippingPrice < discountPrice) {
				discountPrice = targetSaleShippingPrice;
			}

			int paymentPrice = shippingSalePrice - discountPrice; // 쿠폰적용한 배송비

			// 쿠폰 정보 세팅
			CouponDto couponDto = new CouponDto();
			couponDto.setDisplayCouponName(shippingCouponApplication.getDisplayCouponName());
			couponDto.setPmCouponIssueId(shippingCouponApplication.getPmCouponIssueId());
			couponDto.setSalePrice(shippingSalePrice);
			couponDto.setDiscountPrice(discountPrice);
			couponDto.setPaymentPrice(paymentPrice);
			couponDto.setActive(isActive);

			return couponDto;
		}
		return null;
	}

	/**
	 * 쿠폰발행PK로 주문 사용 가능 상품 쿠폰 조회
	 *
	 * @param urUserId
	 * @param CartGoodsDto
	 * @param DeviceType
	 * @param pmCouponIssueId
	 * @return CouponDto
	 * @throws Exception
	 */
	@Override
	public CouponDto getGoodsCouponApplicationListByUser(Long urUserId, CartGoodsDto cartGoods, DeviceType deviceType,
			Long pmCouponIssueId) throws Exception {

		// 쿠폰발행PK로 주문 사용 가능 상품 쿠폰 리스트 조회
		List<CouponApplicationListByUserVo> goodsCouponList = promotionCouponService.getCouponApplicationListByPmCouponIssueId(pmCouponIssueId);

		if (goodsCouponList == null || goodsCouponList.isEmpty()) {
			return null;
		}

		CouponApplicationListByUserVo goodsCoupon = goodsCouponList.get(0);

		CouponDto couponDto = goodsCouponApplication(cartGoods, goodsCoupon, deviceType);

		int discountPrice = couponDto.getDiscountPrice();
		int salePricePerUnit = couponDto.getSalePrice();

		//쿠폰 조직별 분담금액 계산
//		List<CouponMileageShareOrganizationDto> couponShareOrganizationList = getCouponShareorganizationList(discountPrice, goodsCouponList);

		//묶음상품일 경우 묶음상품으로 묶여있는 상품별 할인금액 계산
		//대표상품의 수량 1개로 금액 계산함
		float rate = (float) discountPrice / salePricePerUnit;
		List<CartCouponApplicationGoodsDto> cartCouponApplicationGoodsPackageList = new ArrayList<>();
		if(GoodsEnums.GoodsType.PACKAGE.getCode().equals(cartGoods.getGoodsType()) && !cartGoods.getGoodsPackage().isEmpty()) {

			int index = 0;
			int goodsPackageTotalDiscountPrice = 0; //묶음상품 총 할인금액

			for(CartGoodsPackageDto goodsPackageDto : cartGoods.getGoodsPackage()) {
				CartCouponApplicationGoodsDto cartCouponApplicationGoodsDto = new CartCouponApplicationGoodsDto();

				int goodsPackageDiscountPrice = 0; //묶음상품 할인금액
				int goodsPackagePaymentPrice = 0; //묶음상품 결제금액

				// 할인금액 계산
				if (index == cartGoods.getGoodsPackage().size() - 1) {
					goodsPackageDiscountPrice = discountPrice - goodsPackageTotalDiscountPrice;
				} else {
					goodsPackageDiscountPrice = Math.round(goodsPackageDto.getSalePrice() * rate);
					goodsPackageTotalDiscountPrice += goodsPackageDiscountPrice;
					index++;
				}
				goodsPackagePaymentPrice = goodsPackageDto.getSalePrice() - goodsPackageDiscountPrice;

				cartCouponApplicationGoodsDto.setSpCartId(cartGoods.getSpCartId());
				cartCouponApplicationGoodsDto.setDiscountPrice(goodsPackageDiscountPrice);
				cartCouponApplicationGoodsDto.setPaymentPrice(goodsPackagePaymentPrice);
				cartCouponApplicationGoodsPackageList.add(cartCouponApplicationGoodsDto);
			}
		}

		// 쿠폰 정보 세팅
//		couponDto.setCouponShareOrganizationList(couponShareOrganizationList);
		couponDto.setCartCouponApplicationGoodsPackageList(cartCouponApplicationGoodsPackageList);

		return couponDto;
	}

	/**
	 * 쿠폰발행PK로 주문 사용 가능 배송비 쿠폰 조회
	 *
	 * @param urUserId
	 * @param CartShippingDto
	 * @param DeviceType
	 * @param pmCouponIssueId
	 * @return CouponDto
	 * @throws Exception
	 */
	@Override
	public CouponDto getShippingCouponApplicationListByUser(Long urUserId, CartShippingDto cartShippingDto,
			DeviceType deviceType, Long pmCouponIssueId) throws Exception {

		// 주문 사용 가능 배송비 쿠폰 리스트 조회
		List<CouponApplicationListByUserVo> shippingCouponList = promotionCouponService.getCouponApplicationListByPmCouponIssueId(pmCouponIssueId);

		if (shippingCouponList == null || shippingCouponList.isEmpty()) {
			return null;
		}

		CouponApplicationListByUserVo shippingCoupon = shippingCouponList.get(0);

		CouponDto couponDto = shippingCouponApplication(cartShippingDto, shippingCoupon, deviceType);

		//쿠폰 조직별 분담금액 계산
//		List<CouponMileageShareOrganizationDto> couponShareOrganizationList = getCouponShareorganizationList(couponDto.getDiscountPrice(), shippingCouponList);
//		couponDto.setCouponShareOrganizationList(couponShareOrganizationList);
		return couponDto;
	}

	/**
	 * 쿠폰발행PK로 주문 사용 가능 장바구니 쿠폰 조회
	 *
	 * @param urUserId
	 * @param List<CartGoodsDto>
	 * @param DeviceType
	 * @param pmCouponIssueId
	 * @return CouponDto
	 * @throws Exception
	 */
	@Override
	public CouponDto getCartCouponApplicationListByUser(Long urUserId, List<CartGoodsDto> cartGoodsList,
			DeviceType deviceType, Long pmCouponIssueId) throws Exception {

		// 쿠폰발행PK로 주문 사용 가능 장바구니 쿠폰 리스트 조회
		List<CouponApplicationListByUserVo> cartCouponList = promotionCouponService.getCouponApplicationListByPmCouponIssueId(pmCouponIssueId);

		if (cartCouponList == null || cartCouponList.isEmpty()) {
			return null;
		}

		CouponApplicationListByUserVo cartCoupon = cartCouponList.get(0);

//		List<CartCouponApplicationGoodsDto> cartCouponApplicationGoodsPackageList = new ArrayList<>();

		CouponDto couponDto = cartCouponApplication(urUserId, cartGoodsList, cartCoupon, deviceType);

		List<CartCouponApplicationGoodsDto> cartCouponApplicationGoodsList = couponDto.getCartCouponApplicationGoodsList();

		int cartDiscountPrice = couponDto.getDiscountPrice();
		int goodsTotalSalePrice = couponDto.getSalePrice();

		// 장바구니 쿠폰 적용 상품 정보 DTO 세팅
		int index = 0;
		int goodsTotalDiscountPrice = 0;
		// 장바구니 계산 로직 마지막 상품 큰 금액 기준으로 - 차감 하기 위한 오름차순 정렬
		// 금액이 같은 상품은 장바구니 PK 낮은 상품이 가장 마지막
		cartCouponApplicationGoodsList.sort(new Comparator<CartCouponApplicationGoodsDto>() {
			@Override
			public int compare(CartCouponApplicationGoodsDto dto1, CartCouponApplicationGoodsDto dto2) {
				return (dto1.getPaymentPrice() <= dto2.getPaymentPrice() ? -1 : 1);
			}
		});

		float rate = (float) cartDiscountPrice / goodsTotalSalePrice;
		for (CartCouponApplicationGoodsDto cartCouponApplicationGoods : cartCouponApplicationGoodsList) {

			int salePrice = cartCouponApplicationGoods.getPaymentPrice();
			int discountPrice = 0;
			int paymentPrice = 0;
			// 할인금액 계산

			if (index == cartCouponApplicationGoodsList.size() - 1) {
				discountPrice = cartDiscountPrice - goodsTotalDiscountPrice;
			} else {
				discountPrice = Math.round(cartCouponApplicationGoods.getPaymentPrice() * rate);
				goodsTotalDiscountPrice += discountPrice;
				index++;
			}
			paymentPrice = cartCouponApplicationGoods.getPaymentPrice() - discountPrice;

			cartCouponApplicationGoods.setDiscountPrice(discountPrice);
			cartCouponApplicationGoods.setPaymentPrice(paymentPrice);


//			//묶음상품일 경우 상품별 할인금액 계산
//			if(cartCouponApplicationGoods.getGoodsPackage() != null) {
//				int goodsPackageIndex = 0;
//				int goodsPackageTotalDiscountPrice = 0;
//
//				for(CartGoodsPackageDto goodsPackageDto : cartCouponApplicationGoods.getGoodsPackage()) {
//					CartCouponApplicationGoodsDto cartCouponApplicationGoodsDto = new CartCouponApplicationGoodsDto();
//
//					int goodsPackageDiscountPrice = 0;
//					int goodsPackagePaymentPrice = 0;
//
//					// 할인금액 계산
//					if (goodsPackageIndex == cartCouponApplicationGoods.getGoodsPackage().size() - 1) {
//						goodsPackageDiscountPrice = discountPrice - goodsPackageTotalDiscountPrice;
//					} else {
//						goodsPackageDiscountPrice = goodsPackageDto.getSalePrice() * discountPrice / salePrice;
//						goodsPackageTotalDiscountPrice += goodsPackageDiscountPrice;
//						goodsPackageIndex++;
//					}
//					goodsPackagePaymentPrice = goodsPackageDto.getSalePrice() - goodsPackageDiscountPrice;
//
//					cartCouponApplicationGoodsDto.setSpCartId(cartCouponApplicationGoods.getSpCartId());
//					cartCouponApplicationGoodsDto.setDiscountPrice(goodsPackageDiscountPrice);
//					cartCouponApplicationGoodsDto.setPaymentPrice(goodsPackagePaymentPrice);
//					cartCouponApplicationGoodsPackageList.add(cartCouponApplicationGoodsDto);
//				}
//			}
		}

		//쿠폰 조직별 분담금액 계산
//		List<CouponMileageShareOrganizationDto> couponShareOrganizationList = getCouponShareorganizationList(cartDiscountPrice, cartCouponList);

		couponDto.setCartCouponApplicationGoodsList(cartCouponApplicationGoodsList);
//		couponDto.setCouponShareOrganizationList(couponShareOrganizationList);
//		couponDto.setCartCouponApplicationGoodsPackageList(cartCouponApplicationGoodsPackageList);
		return couponDto;
	}

	@Override
	public List<GoodsApplyCouponDto> getCouponByPmCouponIdList(List<Long> pmCouponIdList, Long urUserId) throws Exception {
		return promotionCouponService.getCouponByPmCouponIdList(pmCouponIdList, urUserId);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = {BaseException.class, Exception.class})
	public void putWithdrawalMemberCoupon(Long urUserid) throws Exception {
		promotionCouponService.putWithdrawalMemberCoupon(urUserid);
	}

	@Override
	public CouponApplicationListByUserVo getCouponApplicationListByPmCouponIssueId(Long pmCouponIssueId) throws Exception {
		List<CouponApplicationListByUserVo> goodsCouponList = promotionCouponService.getCouponApplicationListByPmCouponIssueId(pmCouponIssueId);

		if (goodsCouponList == null || goodsCouponList.isEmpty()) {
			return null;
		}

		return goodsCouponList.get(0);
	}

	/**
	 * 쿠폰 조직별 분담금액 계산
	 *
	 */
	private List<CouponMileageShareOrganizationDto> getCouponShareorganizationList(int discountPrice, List<CouponApplicationListByUserVo> couponList) throws Exception{
		List<CouponMileageShareOrganizationDto> couponShareOrganizationList = new ArrayList<>();

		int index = 0;
		int couponShareAppliedTotalPrice = 0;

		for(CouponApplicationListByUserVo couponMap : couponList) {
			CouponMileageShareOrganizationDto couponMileageShareOrganizationDto = new CouponMileageShareOrganizationDto();
			int couponShareAppliedPrice = 0 ;
			int shareRate = couponMap.getPercentage();

			if(index == couponList.size()-1) {
				couponShareAppliedPrice = discountPrice - couponShareAppliedTotalPrice;
			}else {
				couponShareAppliedPrice = discountPrice * shareRate / 100;
				couponShareAppliedTotalPrice += couponShareAppliedPrice;
				index++;
			}

			couponMileageShareOrganizationDto.setUrErpOrganizationCode(couponMap.getUrErpOrganizationCd());
			couponMileageShareOrganizationDto.setCouponShareAppliedPrice(couponShareAppliedPrice);
			couponShareOrganizationList.add(couponMileageShareOrganizationDto);
		}


		return couponShareOrganizationList;
	}


	/**
	 * 쿠폰승인 목록 조회
	 *
	 * @param CouponApprovalRequestDto
	 * @return CouponApprovalResultVo
	 */
	@Override
	public ApiResult<?> getApprovalCouponList(CouponApprovalRequestDto requestDto) {
		return ApiResult.success(promotionCouponService.getApprovalCouponList(requestDto));
	}

	/**
     * 쿠폰승인 요청철회
     *
     * @param CouponApprovalRequestDto
     * @return ApiResult
     * @throws 	Exception
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {BaseException.class, Exception.class})
    public ApiResult<?> putCancelRequestApprovalCoupon(CouponApprovalRequestDto requestDto) throws Exception {

    	if(CollectionUtils.isNotEmpty(requestDto.getPmCouponIdList())) {
    		//dto.getPmCouponIdList() 숫자만큼 업데이트 되었는지도 확인 필요 유무, 정책 결정 대기
    		for(String pmCouponId : requestDto.getPmCouponIdList()) {
    			ApiResult<?> apiResult = approvalAuthBiz.checkCancelable(ApprovalEnums.ApprovalAuthType.APPR_KIND_TP_COUPON.getCode(), pmCouponId);

    			if(ApprovalEnums.ApprovalValidation.CANCELABLE.getCode().equals(apiResult.getCode())) {
    				ApprovalStatusVo approvalVo = (ApprovalStatusVo)apiResult.getData();
        			MessageCommEnum emums = promotionCouponService.putCancelRequestApprovalCoupon(approvalVo);
        			if(!BaseEnums.Default.SUCCESS.equals(emums)) {
        				throw new BaseException(emums);
        			}
    			}else {
    				//스킵? 혹은 계속진행? 결정대기중
    				return apiResult;
    			}
    		}
    	}
    	else return ApiResult.fail();

    	return ApiResult.success();
    }

	/**
	 * 쿠폰승인 폐기 처리
	 *
	 * @param CouponApprovalRequestDto
	 * @return ApiResult
	 * @throws 	Exception
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = {BaseException.class, Exception.class})
	public ApiResult<?> putDisposalApprovalCoupon(CouponApprovalRequestDto requestDto) throws Exception {

		if(CollectionUtils.isNotEmpty(requestDto.getPmCouponIdList())) {
			//dto.getPmCouponIdList() 숫자만큼 업데이트 되었는지도 확인 필요 유무, 정책 결정 대기
			for(String pmCouponId : requestDto.getPmCouponIdList()) {
				ApiResult<?> apiResult = approvalAuthBiz.checkDisposable(ApprovalEnums.ApprovalAuthType.APPR_KIND_TP_COUPON.getCode(), pmCouponId);

				if(ApprovalEnums.ApprovalValidation.DISPOSABLE.getCode().equals(apiResult.getCode())) {
					ApprovalStatusVo approvalVo = (ApprovalStatusVo)apiResult.getData();
					MessageCommEnum emums = promotionCouponService.putDisposalApprovalCoupon(approvalVo);
					if(!BaseEnums.Default.SUCCESS.equals(emums)) {
						throw new BaseException(emums);
					}
				}else {
					//스킵? 혹은 계속진행? 결정대기중
					return apiResult;
				}
			}
		}
		else return ApiResult.fail();

		return ApiResult.success();
	}

	/**
     * 쿠폰승인처리
     *
     * @param CouponApprovalRequestDto
     * @return ApiResult
     * @throws 	Exception
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {BaseException.class, Exception.class})
    public ApiResult<?> putApprovalProcessCoupon(CouponApprovalRequestDto requestDto) throws Exception {

    	String reqApprStat = requestDto.getApprStat();
    	if(!ApprovalEnums.ApprovalStatus.DENIED.getCode().equals(reqApprStat)
    			&& !ApprovalEnums.ApprovalStatus.APPROVED.getCode().equals(reqApprStat)) {
    		return ApiResult.result(ApprovalEnums.ApprovalValidation.NONE_REQUEST);
    	}

    	if(CollectionUtils.isNotEmpty(requestDto.getPmCouponIdList())) {
    		//dto.getPmCouponIdList() 숫자만큼 업데이트 되었는지도 확인 필요 유무, 정책 결정 대기
    		for(String pmCouponId : requestDto.getPmCouponIdList()) {
    			ApiResult<?> apiResult = approvalAuthBiz.checkApprovalProcess(ApprovalEnums.ApprovalAuthType.APPR_KIND_TP_COUPON.getCode(), pmCouponId);

    			if(ApprovalEnums.ApprovalValidation.APPROVABLE.getCode().equals(apiResult.getCode())) {
    				ApprovalStatusVo approvalVo = (ApprovalStatusVo)apiResult.getData();
    				if(ApprovalEnums.ApprovalStatus.DENIED.getCode().equals(reqApprStat)) {
    					approvalVo.setApprStat(reqApprStat);
    					approvalVo.setStatusComment(requestDto.getStatusComment());
        			}
    				if(ApprovalEnums.ApprovalStatus.APPROVED.getCode().equals(reqApprStat)
        					&& ApprovalEnums.ApprovalStatus.SUB_APPROVED.getCode().equals(approvalVo.getApprStat())
            				) {

    					approvalVo.setMasterStat(CouponEnums.CouponMasterStatus.APPROVED.getCode());
    					CouponRequestDto dto = new CouponRequestDto();
    					dto.setPmCouponId(pmCouponId);
    					CouponDetailVo vo = promotionCouponService.getCoupon(dto);

    				}
    				if(ApprovalEnums.ApprovalStatus.APPROVED.getCode().equals(reqApprStat)
    					&& ApprovalEnums.ApprovalStatus.APPROVED.getCode().equals(approvalVo.getApprStat())
        				) {
    					approvalVo.setMasterStat(CouponEnums.CouponMasterStatus.APPROVED.getCode());
    					CouponRequestDto dto = new CouponRequestDto();
    					dto.setPmCouponId(pmCouponId);
    					CouponDetailVo vo = promotionCouponService.getCoupon(dto);


    					if(CouponEnums.PaymentType.TICKET.getCode().equals(vo.getIssueType()) ){
    						promotionCouponService.putSerialNumberStatus(dto);
    					}

    					//쿠폰 발급 유효성 체크
//    					int issueCnt = promotionCouponService.getIssueCouponChk(dto);
//    					if(issueCnt < 1) {
//    						promotionCouponService.putPmCouponIssueCancel(dto);
//    					}
    				}


        			MessageCommEnum emums = promotionCouponService.putApprovalProcessCoupon(approvalVo);
        			if(!BaseEnums.Default.SUCCESS.equals(emums)) {
        				throw new BaseException(emums);
        			}
    			}else {
    				//스킵? 혹은 계속진행? 결정대기중
    				return apiResult;
//    				AUTH_DENIED("AUTH_DENIED", "허가되지 않은 권한입니다."),
//    				NONE_REQUEST("NONE_REQUEST", "승인요청 상태가 없습니다."),
//    				ALREADY_APPROVAL_REQUEST("ALREADY_APPROVAL_REQUEST", "이미 승인요청 중인 상태입니다. 승인요청중인 정보는 수정이 불가합니다."),
//    				ALREADY_APPROVED("ALREADY_APPROVED", "이미 승인이 완료되어 철회가 불가능합니다."),
//    				ALREADY_DENIED("ALREADY_DENIED", "이미 승인이 반려되었습니다. 반려 정보를 확인해 주세요."),
//    				ALREADY_CANCEL_REQUEST("ALREADY_CANCEL_REQUEST", "승인요청자가 승인요청을 철회하였습니다."),
//    				REQUIRED_APPROVAL_USER("REQUIRED_APPROVAL_USER", "최종승인관리자는 1명이상 등록되거나 정상상태이어야 합니다.");
    			}
    		}
    	}else return ApiResult.fail();
    	return ApiResult.success();
    }


    @Override
    public ApiResult<?> addUserExcelUpload(MultipartFile file) throws Exception {
        //Session 정보
        long adminId = 0;
        UserVo userVo = SessionUtil.getBosUserVO();
        if (userVo != null) {
            adminId = Long.valueOf(userVo.getUserId());
        }

        //업로드 현황 정보 설정
        OutMallExcelInfoVo infoVo = new OutMallExcelInfoVo();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime startTime = LocalDateTime.now();
        infoVo.setUploadStartDateTime(startTime.format(dateTimeFormatter));
        infoVo.setCreateId(adminId);

        // Excel Import 정보 -> Dto 변환
        List<CouponIssueParamDto> excelList = new ArrayList<>();
        Sheet sheet = ExcelUploadUtil.excelParse(file);
        if (sheet == null) return ApiResult.fail();

        for (int i = sheet.getFirstRowNum(); i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);
            	if (i > 0) {
                // 값 설정
                if (!StringUtils.isEmpty(ExcelUploadUtil.cellValue(row.getCell(0)))) {
                	CouponIssueParamDto dto = new CouponIssueParamDto();
                	if(!ExcelUploadUtil.cellValue(row.getCell(0)).equals("false")) {
                		dto.setLoginId(ExcelUploadUtil.cellValue(row.getCell(0)));
                		dto.setIssueVal(ExcelUploadUtil.cellValue(row.getCell(1)));
                		excelList.add(dto);
                	}
                }
            }
        }
        //업로드 현황 1차 저장
        infoVo.setTotalCount(excelList.size());
        CouponUploadResponseDto responseDto = new CouponUploadResponseDto();
        responseDto.setRows(excelList);

        return ApiResult.success(responseDto);
    }


    @Override
    public ApiResult<?> addUserExcelUploadByAdm(MultipartFile file) throws Exception {
        //Session 정보
        long adminId = 0;
        UserVo userVo = SessionUtil.getBosUserVO();
        if (userVo != null) {
            adminId = Long.valueOf(userVo.getUserId());
        }

        //업로드 현황 정보 설정
        OutMallExcelInfoVo infoVo = new OutMallExcelInfoVo();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime startTime = LocalDateTime.now();
        infoVo.setUploadStartDateTime(startTime.format(dateTimeFormatter));
        infoVo.setCreateId(adminId);

        // Excel Import 정보 -> Dto 변환
        List<CouponIssueParamDto> excelList = new ArrayList<>();
        Sheet sheet = ExcelUploadUtil.excelParse(file);
        if (sheet == null) return ApiResult.fail();
        int noAmountCnt = 0;
        int okTotCnt = 0;
        int SystemAmountOverCnt = 0;	// 10000원 이상 Row Check
		int duplicateMemberCnt = 0 ;   // 중복 ID 건수

		CouponUploadResponseDto responseDto = new CouponUploadResponseDto();

        for (int i = sheet.getFirstRowNum(); i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);
            	if (i > 0) {
                // 값 설정
				CouponIssueParamDto dto = new CouponIssueParamDto();

                if (!StringUtils.isEmpty(ExcelUploadUtil.cellValue(row.getCell(0))) && StringUtils.isEmpty(ExcelUploadUtil.cellValue(row.getCell(1)))) {
                	//아이디 o 적립금 x
                	if(!ExcelUploadUtil.cellValue(row.getCell(0)).equals("false")) {
                		dto.setLoginId(ExcelUploadUtil.cellValue(row.getCell(0)));
                		excelList.add(dto);
                        noAmountCnt++;
                    }
                } else if (StringUtils.isEmpty(ExcelUploadUtil.cellValue(row.getCell(0))) && !StringUtils.isEmpty(ExcelUploadUtil.cellValue(row.getCell(1)))) {
					//아이디 x 적립금 o

					if(!ExcelUploadUtil.cellValue(row.getCell(1)).equals("false")) {
						dto.setUploadIssueValue(ExcelUploadUtil.cellValue(row.getCell(1)));
						excelList.add(dto);
						responseDto.setRETURN_CODE("NO_ID");
					}

                } else if (!StringUtils.isEmpty(ExcelUploadUtil.cellValue(row.getCell(0))) && !StringUtils.isEmpty(ExcelUploadUtil.cellValue(row.getCell(1)))) {
					//아이디 o 적립금 o
					if(!ExcelUploadUtil.cellValue(row.getCell(0)).equals("false") && !ExcelUploadUtil.cellValue(row.getCell(1)).equals("false")) {
						dto.setLoginId(ExcelUploadUtil.cellValue(row.getCell(0)));
						dto.setUploadIssueValue(ExcelUploadUtil.cellValue(row.getCell(1)));

						excelList.add(dto);
						okTotCnt++;

						// 만원이상 업로드 건수 체크
						if ( Integer.parseInt(ExcelUploadUtil.cellValue(row.getCell(1))) >= APPROVE_POINT_BY_SYSTEM_AMOUNT  ) {
							SystemAmountOverCnt++;
						}

					}

                } else if (StringUtils.isEmpty(ExcelUploadUtil.cellValue(row.getCell(0))) && StringUtils.isEmpty(ExcelUploadUtil.cellValue(row.getCell(1)))) {
					//아이디 x 적립금 x
					responseDto.setRETURN_CODE("NO_EXCEL");
				}
            }
        }
        //업로드 현황 1차 저장
        infoVo.setTotalCount(excelList.size());
        responseDto.setRows(excelList);
        if (noAmountCnt > 0 ) {
			responseDto.setRETURN_CODE("NO_AMOUNT" + "∀" + noAmountCnt);
		}

        if (okTotCnt > 0 ) {
			responseDto.setRETURN_CODE("OK_CNT" + "∀" + okTotCnt + "∬" + SystemAmountOverCnt + "∬" + duplicateMemberCnt);
		}

        return ApiResult.success(responseDto);
    }



    public ApiResult<?> addTicketExcelUpload(MultipartFile file) throws Exception {
        //Session 정보
        Long adminId = null;
        UserVo userVo = SessionUtil.getBosUserVO();
        if (userVo != null) {
            adminId = Long.valueOf(userVo.getUserId());
        }

        //업로드 현황 정보 설정
        OutMallExcelInfoVo infoVo = new OutMallExcelInfoVo();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime startTime = LocalDateTime.now();
        infoVo.setUploadStartDateTime(startTime.format(dateTimeFormatter));
        infoVo.setCreateId(adminId);

        // Excel Import 정보 -> Dto 변환
        List<CouponIssueParamDto> excelList = new ArrayList<>();
        Sheet sheet = ExcelUploadUtil.excelParse(file);
        if (sheet == null) return ApiResult.fail();

        for (int i = sheet.getFirstRowNum(); i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);
            // 헤더 Check
            	if (i > 0) {
                // 값 설정
                if (!StringUtils.isEmpty(ExcelUploadUtil.cellValue(row.getCell(0)))) {
                	CouponIssueParamDto dto = new CouponIssueParamDto();
                	if(!ExcelUploadUtil.cellValue(row.getCell(0)).equals("false")) {
	                	dto.setSerialNumber(ExcelUploadUtil.cellValue(row.getCell(0)));
	                	excelList.add(dto);
                	}
                }
            }
        }
        //업로드 현황 1차 저장
        infoVo.setTotalCount(excelList.size());

        CouponUploadResponseDto responseDto = new CouponUploadResponseDto();
        responseDto.setRows(excelList);

        return ApiResult.success(responseDto);
    }

    @Override
    public ApiResult<?> getEventCallCouponInfo(CouponRequestDto couponRequestDto) {
        return promotionCouponService.getEventCallCouponInfo(couponRequestDto);
    }


    @Override
    public ApiResult<?> getCouponSearchStatus(CouponRequestDto couponRequestDto) {
        return promotionCouponService.getCouponSearchStatus(couponRequestDto);
    }


    @Override
    public ApiResult<?> putTicketCollectStatus(CouponRequestDto couponRequestDto) throws Exception {
        promotionCouponService.putTicketCollectStatus(couponRequestDto);
        return ApiResult.success();
    }


	/**
	 * 쿠폰 재발급
	 *
	 * @param odOrderId
	 * @param pmCouponIssueId
	 * @param odClaimId
	 * @throws Exception
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = {BaseException.class, Exception.class})
	public void reissueCoupon(Long odOrderId, Long pmCouponIssueId, Long odClaimId, List<OrderClaimGoodsInfoDto> goodsInfoList, String customUrUserId) throws Exception {

		// 쿠폰 정보 조회
		CouponIssueVo couponIssueVo = promotionCouponService.getCouponIssueByPmCouponIssueId(pmCouponIssueId);

		// 쿠폰재발급 요청파라미터 생성
		CouponIssueParamDto couponIssueParamDto = null;

		// 상품쿠폰, 판매가지정쿠폰, 장바구니쿠폰일 경우
		if(CouponEnums.CouponType.GOODS.getCode().equals(couponIssueVo.getCouponType())
				|| CouponEnums.CouponType.SALEPRICE_APPPOINT.getCode().equals(couponIssueVo.getCouponType())
				|| CouponEnums.CouponType.CART.getCode().equals(couponIssueVo.getCouponType())) {

			// 주문PK, 쿠폰발급PK로 쿠폰재발급가능한지여부 확인
			int result = 0;
			List<Long> odClaimDetlIds = null;

			if(goodsInfoList != null) odClaimDetlIds = goodsInfoList.stream().map(OrderClaimGoodsInfoDto::getOdClaimDetlId).collect(Collectors.toList());
			if(CouponEnums.CouponType.CART.getCode().equals(couponIssueVo.getCouponType())) {
				result = orderDetailBiz.isPossibilityReissueCartCouponInOdOrderDetl(odOrderId, pmCouponIssueId, odClaimDetlIds);
			} else {
				result = orderDetailBiz.isPossibilityReissueCouponInOdOrderDetl(odOrderId, pmCouponIssueId);
			}
			if(result > 0){
				couponIssueParamDto = setCouponIssueParam(couponIssueVo, odClaimId);
			}else {
				// 장바구니 쿠폰 부분취소일 경우
				if(CouponEnums.CouponType.CART.getCode().equals(couponIssueVo.getCouponType())) {

					// 쿠폰발급PK, 클레임PK로 장바구니 재발급 쿠폰 할인금액 조회
					int discountPrice = claimProcessBiz.getOrderClaimDetlDiscountPrice(odOrderId, pmCouponIssueId, odClaimId, odClaimDetlIds);
					if(discountPrice > 0) {
						couponIssueVo.setDiscountValue(String.valueOf(discountPrice));

						// 1. 쿠폰 등록
						// 쿠폰생성 요청파라미터 세팅
						CouponRequestDto couponRequestDto = setCouponRequestDtoByReissueCoupon(couponIssueVo, String.valueOf(discountPrice), odOrderId);
						couponRequestDto.setCreateId(customUrUserId);
						// 쿠폰 등록
						promotionCouponService.addCoupon(couponRequestDto);

						// 2. 쿠폰 적용범위 등록 -> 쿠폰PK로 쿠폰적용범위 등록
						promotionCouponService.addPmCouponCoverageByPmCouponId(Long.parseLong(couponRequestDto.getPmCouponId()), couponIssueVo.getPmCouponId());

						// 3. 분담정보 등록 -> 쿠폰PK로 분담정보 등록
						promotionCouponService.addPmCouponPointShareOrganizaionByPmCouponId(Long.parseLong(couponRequestDto.getPmCouponId()), couponIssueVo.getPmCouponId());

						// 4. 쿠폰상태이력 등록
						// 쿠폰상태이력 요청파라미터 세팅
						ApprovalStatusVo history = ApprovalStatusVo.builder()
								.taskPk(couponRequestDto.getPmCouponId())
								.masterStat(couponRequestDto.getCouponMasterStat())
								.apprStat(couponRequestDto.getApprStat())
								.apprSubUserId(couponRequestDto.getApprSubUserId())
								.apprUserId(couponRequestDto.getApprUserId())
								.approvalRequestUserId(customUrUserId)
								.createId(customUrUserId)
								.build();

						// 쿠폰상태이력 등록
						promotionCouponService.addCouponStatusHistory(history);


						// 해당 쿠폰 재발급
						// 등록한 쿠폰값으로 vo 다시 세팅
						couponIssueVo.setPmCouponId(Long.valueOf(couponRequestDto.getPmCouponId()));
						couponIssueParamDto = setCouponIssueParam(couponIssueVo, odClaimId);
					}
				}

			}


		// 배송비 쿠폰일 경우
		}else if(CouponEnums.CouponType.SHIPPING_PRICE.getCode().equals(couponIssueVo.getCouponType())) {

			int result = orderDetailBiz.isPossibilityReissueCouponInOdshippingPrice(odOrderId, pmCouponIssueId);
			if(result > 0){
				couponIssueParamDto = setCouponIssueParam(couponIssueVo, odClaimId);
				// 배송비 쿠폰 재발급 여부 업데이트
				promotionCouponService.putShippingFeeCouponReIssueYn(pmCouponIssueId);
			}
		}

		if(couponIssueParamDto != null) {
			couponIssueParamDto.setCreateId(customUrUserId);
			// 쿠폰 발급/사용 등록
			promotionCouponService.putCouponIssueList(couponIssueParamDto);

			// 쿠폰 발급/사용 상태 이력 등록
			promotionCouponService.putCouponIssueHistoryList(couponIssueParamDto);
		}

	}

	/**
	 * 쿠폰 재발급 요청 파라미터 세팅
	 *
	 * @param CouponIssueVo
	 * @param odClaimId
	 * @return CouponIssueParamDto
	 */
	private CouponIssueParamDto setCouponIssueParam(CouponIssueVo couponIssueVo, Long odClaimId) {

		CouponIssueParamDto couponIssueParamDto = new CouponIssueParamDto();
		couponIssueParamDto.setPmCouponId(String.valueOf(couponIssueVo.getPmCouponId()));
		couponIssueParamDto.setUrUserId(String.valueOf(couponIssueVo.getUrUserId()));
		couponIssueParamDto.setCouponIssueType(CouponEnums.CouponIssueType.REISSUE.getCode());
		couponIssueParamDto.setCouponStatus(CouponEnums.CouponStatus.NOTUSE.getCode());
		if(couponIssueVo.getPmSerialnumberId() != null) {
			couponIssueParamDto.setPmSerialNumberId(String.valueOf(couponIssueVo.getPmSerialnumberId()));
		}

		// 쿠폰 유효기간 설정
		DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

		// 클레임ID로 귀책유형 조회
		OrderClaimViewRequestDto orderClaimViewRequestDto = new OrderClaimViewRequestDto();
		orderClaimViewRequestDto.setOdClaimId(odClaimId);
		OrderClaimMasterInfoDto orderClaimMasterInfo =  claimRequestBiz.getOrderClaimMasterInfo(orderClaimViewRequestDto);

		// 클레임 귀책유형 - 구매자일 경우(기존 쿠폰만료일 그대로)
		String expirationDate = couponIssueVo.getExpirationDate().format(dateTimeFormatter);
		String validityStartDate = couponIssueVo.getIssueValidityStartDate().format(dateTimeFormatter);
		log.debug("======================expirationDate 1===========================: "+expirationDate);
		// 클레임 귀책유형 - 판매자일 경우(쿠폰만료일-쿠폰사용일+쿠폰재발급일자)
		if(ClaimEnums.ReasonAttributableType.COMPANY.getType().equals(orderClaimMasterInfo.getTargetTp())){
			Long betweenDays = ChronoUnit.DAYS.between(couponIssueVo.getUseDate(), couponIssueVo.getExpirationDate());
			log.debug("======================betweenDays===========================: "+betweenDays);
			LocalDateTime expirationLocalDateTime = LocalDateTime.of(LocalDate.now().plusDays(betweenDays), LocalTime.of(23,59,59));
			expirationDate = expirationLocalDateTime.format(dateTimeFormatter);
		}

		couponIssueParamDto.setValidityStartDate(validityStartDate);
		couponIssueParamDto.setExpirationDate(expirationDate);
		log.debug("======================expirationDate 2===========================: "+expirationDate);

		return couponIssueParamDto;
	}

	/**
	 * 장바구니쿠폰 재발급시 사용할 쿠폰생성 요청 파라미터 세팅
	 *
	 * @param couponIssueVo
	 * @param discountPrice
	 * @return CouponRequestDto
	 * @throws Exception
	 */
	private CouponRequestDto setCouponRequestDtoByReissueCoupon(CouponIssueVo couponIssueVo, String discountPrice, Long odOrderId) throws Exception{
		CouponRequestDto couponRequestDto = new CouponRequestDto();

		// 관리자 쿠폰명 (쿠폰명 + {주문번호})
		String bosCouponName = CouponEnums.ReissueCouponName.BOS_CART.getCodeName()+"{"+odOrderId+"}";
		// 쿠폰제한 공통코드
		String issueQtyLimit = ("0".equals(couponIssueVo.getIssueQtyLimit()) ? couponIssueVo.getIssueQtyLimit() : CouponEnums.CouponLimit.findByCodeName(couponIssueVo.getIssueQtyLimit()).getCode());

		couponRequestDto.setOriginPmCouponId(couponIssueVo.getPmCouponId());
		couponRequestDto.setCouponType(couponIssueVo.getCouponType());
		couponRequestDto.setPaymentType(("0".equals(couponIssueVo.getIssueQtyLimit()) ? CouponEnums.PaymentType.AUTO_PAYMENT.getCode() : CouponEnums.PaymentType.CHECK_PAYMENT.getCode()));
		couponRequestDto.setIssueReason("CS발급");
		couponRequestDto.setBosCouponName(bosCouponName);
		couponRequestDto.setDisplayCouponName(CouponEnums.ReissueCouponName.DISPLAY_CART.getCodeName());
		couponRequestDto.setIssueStartDate(couponIssueVo.getIssueStartDate());
		couponRequestDto.setIssueEndDate(couponIssueVo.getIssueEndDate());
		couponRequestDto.setValidityType(CouponEnums.ValidityType.VALIDITY.getCode());
		couponRequestDto.setValidityDay("0");
		couponRequestDto.setIssueQtyLimit(issueQtyLimit);
		couponRequestDto.setIssueBudget(couponIssueVo.getIssueBudget());
		couponRequestDto.setIssueQty(couponIssueVo.getIssueQty());
		couponRequestDto.setIssuePurposeType(CouponEnums.IssuePurpose.CS_ISSUED.getCode());
		couponRequestDto.setCoverageType(couponIssueVo.getCoverageType());
		couponRequestDto.setUsePcYn(couponIssueVo.getUsePcYn());
		couponRequestDto.setUseMobileWebYn(couponIssueVo.getUseMoWebYn());
		couponRequestDto.setUseMobileAppYn(couponIssueVo.getUseMoAppYn());
		couponRequestDto.setDiscountType(CouponEnums.DiscountType.FIXED_DISCOUNT.getCode());
		couponRequestDto.setDiscountVal(discountPrice);
		couponRequestDto.setMinPaymentAmount(discountPrice);
		couponRequestDto.setUseYn("N");
		couponRequestDto.setCartCouponApplyYn(couponIssueVo.getCartCouponApplyYn());
		couponRequestDto.setPgPromotionYn(couponIssueVo.getPgPromotionYn());
		couponRequestDto.setPgPromotionPayConfigId(couponIssueVo.getPgPromotionPayConfigId());
		couponRequestDto.setPgPromotionPayGroupId(couponIssueVo.getPgPromotionPayGroupId());
		couponRequestDto.setPgPromotionPayId(couponIssueVo.getPgPromotionPayId());
		// 쿠폰승인
		couponRequestDto.setApprStat(ApprovalEnums.ApprovalStatus.APPROVED_BY_SYSTEM.getCode());
		couponRequestDto.setCouponMasterStat(CouponEnums.CouponMasterStatus.APPROVED.getCode());

		return couponRequestDto;
	}


	/**
	 * 분담조직 정보 조회
	 */
    @Override
    public ApiResult<?> getOrgInfo(CouponRequestDto couponRequestDto) {
        return promotionCouponService.getOrgInfo(couponRequestDto);
    }

	@Override
	public String getCouponNameById(Long pmCouponId) throws Exception {
		return promotionCouponService.getCouponNameById(pmCouponId);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = {BaseException.class, Exception.class})
	public int addPmCouponJoinEventListByJoinUrUserId(CouponJoinCertEventRequestDto couponJoinCertEventRequestDto) throws Exception {
		return promotionCouponService.addPmCouponJoinEventListByJoinUrUserId(couponJoinCertEventRequestDto);
	}

	@Override
	public boolean isPmCouponJoinDup(String customerNumber) throws Exception {
		//회원가입완료시 정보조회
		return promotionCouponService.isPmCouponJoinDup(customerNumber);
	}

	@Override
	public CouponEnums.UseCouponValidation checkUseCouponValidation(Long urUserId, Long pmCouponIssueId) throws Exception {
		return promotionCouponService.checkUseCouponValidation(urUserId, pmCouponIssueId);
	}

}