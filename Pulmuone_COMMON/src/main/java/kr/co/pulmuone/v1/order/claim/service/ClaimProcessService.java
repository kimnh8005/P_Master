package kr.co.pulmuone.v1.order.claim.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.co.pulmuone.v1.api.cjlogistics.dto.CJLogisticsOrderAcceptDto;
import kr.co.pulmuone.v1.comm.base.vo.UserVo;
import kr.co.pulmuone.v1.comm.constants.Constants;
import kr.co.pulmuone.v1.comm.enums.*;
import kr.co.pulmuone.v1.comm.exception.BaseException;
import kr.co.pulmuone.v1.comm.mapper.order.claim.ClaimProcessMapper;
import kr.co.pulmuone.v1.comm.mapper.order.status.OrderStatusMapper;
import kr.co.pulmuone.v1.comm.util.SessionUtil;
import kr.co.pulmuone.v1.comm.util.StringUtil;
import kr.co.pulmuone.v1.order.claim.dto.*;
import kr.co.pulmuone.v1.order.claim.dto.vo.*;
import kr.co.pulmuone.v1.order.create.dto.OrderClaimCardPayRequestDto;
import kr.co.pulmuone.v1.order.create.dto.OrderInfoDto;
import kr.co.pulmuone.v1.order.email.dto.OrderInfoForEmailResultDto;
import kr.co.pulmuone.v1.order.email.service.OrderEmailBiz;
import kr.co.pulmuone.v1.order.email.service.OrderEmailSendBiz;
import kr.co.pulmuone.v1.order.order.dto.OrderDetlDailySchArrivalInfoDto;
import kr.co.pulmuone.v1.order.order.dto.vo.OrderDetlHistVo;
import kr.co.pulmuone.v1.order.order.dto.vo.OrderDetlVo;
import kr.co.pulmuone.v1.order.order.dto.vo.OrderPaymentMasterVo;
import kr.co.pulmuone.v1.pg.service.inicis.dto.InicisNotiRequestDto;
import kr.co.pulmuone.v1.pg.service.inicis.dto.InicisVirtualAccountReturnRequestDto;
import kr.co.pulmuone.v1.pg.service.kcp.dto.KcpVirtualAccountReturnRequestDto;
import kr.co.pulmuone.v1.policy.claim.dto.vo.PolicyClaimMallVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


/**
 * <PRE>
 * Forbiz Korea
 * 주문 클레임 생성, 수정, 삭제 관련 Service
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :   작성일                :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 *  1.0    2020. 12. 15.   강상국         최초작성
 * =======================================================================
 * </PRE>
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ClaimProcessService {

	@Autowired
    private final ClaimProcessMapper claimProcessMapper;

	@Autowired
	private final OrderStatusMapper orderStatusMapper;

	@Autowired
	private OrderEmailBiz orderEmailBiz;

	@Autowired
	private OrderEmailSendBiz orderEmailSendBiz;

	@Autowired
	private ClaimUtilProcessService claimUtilProcessService;

	/**
	 * 주문 클레임 마스터 Seq
	 * @return
	 */
	protected long getOdClaimId() { return claimProcessMapper.getOdClaimId(); }

	/**
	 * 주문 클레임 상세 Seq
	 * @return
	 */
	protected long getOdClaimDetlId() {
		return claimProcessMapper.getOdClaimDetlId();
	}

	/**
	 * 주문클레임 상세 할인 SEQ
	 * @return
	 */
	protected long getOdClaimDetlDiscountId() {
		return claimProcessMapper.getOdClaimDetlDiscountId();
	}

	/**
	 * 주문 클레임 마스터 입력
	 * @param claimVo
	 * @return
	 */
	protected int addOrderClaim(ClaimVo claimVo) {
		return claimProcessMapper.addOrderClaim(claimVo);
	}

	/**
	 * 주문 클레임 상세 입력
	 * @param claimDetlVo
	 * @return
	 */
	protected int addOrderClaimDetl(ClaimDetlVo claimDetlVo) {
		return claimProcessMapper.addOrderClaimDetl(claimDetlVo);
	}

	/**
	 * 주문 클레임 상세 상태 수정
	 * @param claimDetlVo
	 * @return
	 */
	protected int putOrderClaimDetl(ClaimDetlVo claimDetlVo) {
		return claimProcessMapper.putOrderClaimDetl(claimDetlVo);
	}

	/**
	 * 반품 클레임 상세 정보 업데이트
	 * @param requestDto
	 */
	protected int putOrderClaimDetlInfo(OrderClaimRegisterRequestDto requestDto) {
		log.debug("------------------ 반품 클레임 상세 정보 업데이트");
		int updateCnt = 0;
		if (CollectionUtils.isNotEmpty(requestDto.getGoodsInfoList())) {
			for (OrderClaimGoodsInfoDto goodsInfo : requestDto.getGoodsInfoList()) {
				// 주문 상세 PK가 존재 하지 않을경우 클레임 등록 제외
				if(goodsInfo.getOdOrderDetlId() < 1) {
					continue;
				}
				ClaimDetlVo claimDetlVo = ClaimDetlVo.builder()
						.odClaimDetlId(goodsInfo.getOdClaimDetlId()) 			//주문클레임 상세 PK
						.odClaimId(goodsInfo.getOdClaimId())					//주문클레임 PK
						.odOrderDetlId(goodsInfo.getOdOrderDetlId()) 			//주문상세 PK
						.psClaimBosSupplyId(goodsInfo.getPsClaimBosSupplyId())	//BOS 클레임 사유 공급업체 PK
						.psClaimBosId(goodsInfo.getPsClaimBosId())				//BOS 클레임 사유 PK
						.bosClaimLargeId(goodsInfo.getBosClaimLargeId()) 		//BOS 클레임 대분류 ID
						.bosClaimMiddleId(goodsInfo.getBosClaimMiddleId()) 		//BOS 클레임 중분류 ID
						.bosClaimSmallId(goodsInfo.getBosClaimSmallId())		//BOS 클레임 소분류 ID
						.addPaymentShippingPrice(goodsInfo.getAddPaymentShippingPrice())//추가배송비
						.build();

				// 주문 클레임 상세 추가 배송비 정보 업데이트
				updateCnt += claimProcessMapper.putOrderClaimDetlAddShippingPriceInfo(claimDetlVo);
			}
		}
		return updateCnt;
	}

	/**
	 * 주문클레임 상세 할인금액 정보 입력
	 * @param claimDetlDiscountVo
	 * @return
	 */
	protected int addOrderClaimDetlDiscount(ClaimDetlDiscountVo claimDetlDiscountVo) {
		return claimProcessMapper.addOrderClaimDetlDiscount(claimDetlDiscountVo);
	}

	/**
	 * 주문클레임 상세 할인금액 목록조회
	 * @param odOrderId
	 * @param odClaimId
	 * @param odOrderDetlIds
	 * @return
	 */
	protected List<OrderClaimDetlDiscountListInfoDto> getOrderClaimDetlDiscountList(long odOrderId, long odClaimId, List<Long> odOrderDetlIds) {
		return claimProcessMapper.getOrderClaimDetlDiscountList(odOrderId, odClaimId, odOrderDetlIds);
	}

	/**
	 * 주문클레임 상세 할인금액 정보 입력 ALL
	 * @param orderClaimDetlDiscountList
	 * @return
	 */
	protected int addOrderClaimDetlDiscountAll(List<OrderClaimDetlDiscountListInfoDto> orderClaimDetlDiscountList) {
		return claimProcessMapper.addOrderClaimDetlDiscountAll(orderClaimDetlDiscountList);
	}

	/**
	 * 주문 클레임 처리 이력 입력
	 * @param claimDetlHistVo
	 * @return
	 */
	protected int addOrderClaimDetlHist(ClaimDetlHistVo claimDetlHistVo) {
		return claimProcessMapper.addOrderClaimDetlHist(claimDetlHistVo);
	}

	/**
	 * 주문 클레임 환불 계좌 입력
	 * @param claimAccountVo
	 * @return
	 */
	protected int addOrderClaimAccount(ClaimAccountVo claimAccountVo) {
		return claimProcessMapper.addOrderClaimAccount(claimAccountVo);
	}

	/**
	 * 주문상세 원주문번호 취소 갯수 입력
	 * @param orderDetlVo
	 * @return
	 */
	protected int putOrderDetlCancel(OrderDetlVo orderDetlVo) throws Exception {
		int updateCnt = claimProcessMapper.putOrderDetlCancel(orderDetlVo);
		if(updateCnt < 1) {
			throw new BaseException(ClaimEnums.ClaimValidationResult.NO_CLAIM_CNT);
		}
		return updateCnt;
	}

	/**
	 * 주문 클레임 첨부파일 입력
	 * @param claimAttcVo
	 * @return
	 */
	protected int addOrderClaimAttc(ClaimAttcVo claimAttcVo) {
		return claimProcessMapper.addOrderClaimAttc(claimAttcVo);
	}

	/**
	 * 주문 클레임 배송지 입력
	 * @param claimShippingZoneVo
	 * @return
	 */
	protected int addOrderClaimShippingZone(ClaimShippingZoneVo claimShippingZoneVo) {
		return claimProcessMapper.addOrderClaimShippingZone(claimShippingZoneVo);
	}

	/**
	 * 주문 클레임 보내는 배송지 입력
	 * @param claimSendShippingZoneVo
	 * @return
	 */
	protected int addOrderClaimSendShippingZone(ClaimSendShippingZoneVo claimSendShippingZoneVo) {
		return claimProcessMapper.addOrderClaimSendShippingZone(claimSendShippingZoneVo);
	}

	/**
	 * 클레임 정보 조회
	 * @param odClaimId
	 * @return
	 */
	protected OrderClaimInfoDto getClaimInfo(long odClaimId) {
		return claimProcessMapper.getClaimInfo(odClaimId);
	}


	/**
	 * 클레임 상세 상품 정보 조회
	 * @param odClaimId
	 * @return
	 */
	protected List<OrderClaimDetlListInfoDto> getClaimDetlList(long odClaimId) {
		return claimProcessMapper.getClaimDetlList(odClaimId);
	}

	/**
	 * 주문PK, 쿠폰발급PK로 주문클레임 상세 할인금액 조회
	 * @param odOrderId
	 * @param pmCouponIssueId
	 * @param odClaimId
	 * @param odClaimDetlIds
	 * @return int
	 */
	protected int getOrderClaimDetlDiscountPrice(Long odOrderId, Long pmCouponIssueId, Long odClaimId, List<Long> odClaimDetlIds) {
		return claimProcessMapper.getOrderClaimDetlDiscountPrice(odOrderId, pmCouponIssueId, odClaimId, odClaimDetlIds);
	}

	/**
	 * 주문상세 주문상세 순번(라인번호) 주문번호에 대한 순번 구하기
	 * @param odOrderId
	 * @return
	 */
	protected int getOrderDetlSeq(long odOrderId) {
		return claimProcessMapper.getOrderDetlSeq(odOrderId);
	}

	/**
	 * 클레임상세상태 변경 이력 등록
	 * @param claimDetlHistVo
	 * @return int
	 */
	protected int putClaimDetailStatusHist(ClaimDetlHistVo claimDetlHistVo) {
		return orderStatusMapper.putClaimDetailStatusHist(claimDetlHistVo);
	}

	/**
	 * 거부 클레임상세상태 변경 이력 등록
	 * @param claimDetlHistVo
	 * @return int
	 */
	protected int putDenyDefeClaimDetailStatusHist(ClaimDetlHistVo claimDetlHistVo) {
		return orderStatusMapper.putDenyDefeClaimDetailStatusHist(claimDetlHistVo);
	}

	/**
	 * 추가 결제 정보 조회
	 * @param odAddPaymentReqInfoId
	 * @return
	 */
	protected OdAddPaymentReqInfo getOdAddPaymentReqInfo(long odAddPaymentReqInfoId) {
		return claimProcessMapper.getOdAddPaymentReqInfo(odAddPaymentReqInfoId);
	}

	/**
	 * 추가 결제 정보 등록
	 * @param odAddPaymentReqInfo
	 * @return
	 */
	protected int addOdAddPaymentReqInfo(OdAddPaymentReqInfo odAddPaymentReqInfo) {
		return claimProcessMapper.addOdAddPaymentReqInfo(odAddPaymentReqInfo);
	}

	/**
	 * 주문자 정보 및 상품 정보 조회
	 * @param orderClaimCardPayRequestDto
	 * @return
	 */
	protected OrderInfoDto getOrderBuyerInfo(OrderClaimCardPayRequestDto orderClaimCardPayRequestDto) {
		return claimProcessMapper.getOrderBuyerInfo(orderClaimCardPayRequestDto);
	}

	/**
	 * 주문 상세 클레임 상품 정보 > BOS 클레임 사유 변경
	 * @param orderClaimRegisterRequestDto
	 * @return
	 */
	protected int putOrderClaimDetlBosClaimReason(OrderClaimRegisterRequestDto orderClaimRegisterRequestDto) throws Exception{
		return claimProcessMapper.putOrderClaimDetlBosClaimReason(orderClaimRegisterRequestDto);
	}

	/**
	 * 주문 상세 클레임 상품 정보 > BOS 클레임 사유 변경 이력 등록
	 * @param orderClaimRegisterRequestDto
	 * @return
	 */
	protected int addOrderClaimBosReasonHist(OrderClaimRegisterRequestDto orderClaimRegisterRequestDto) throws Exception{
		return claimProcessMapper.addOrderClaimBosReasonHist(orderClaimRegisterRequestDto);
	}

	/**
	 * 렌탈상품 주문취소 대상 상품 목록 조회 requestDto set
	 * @param orderClaimRegisterRequestDto
	 * @return OrderClaimViewRequestDto
	 */
	protected OrderClaimViewRequestDto setOrderClaimInfoRequestDto(OrderClaimRegisterRequestDto orderClaimRegisterRequestDto) throws Exception{
		OrderClaimViewRequestDto orderClaimViewRequestDto = new OrderClaimViewRequestDto();
		orderClaimViewRequestDto.setOdOrderId(orderClaimRegisterRequestDto.getOdOrderId());
		orderClaimViewRequestDto.setGoodsChange(Integer.parseInt(ClaimEnums.ClaimGoodsChangeType.ALL_CANCEL.getCode()));  // 조회구분 (전체취소수량 : 0, 수량변경취소 : 1)
		orderClaimViewRequestDto.setClaimStatusTp(OrderClaimEnums.ClaimStatusTp.CANCEL.getCode());
		orderClaimViewRequestDto.setFrontTp(Integer.parseInt(ClaimEnums.ClaimFrontTp.FRONT.getCode())); // 프론트 구분 (0 : Bos, 1:Front 2:Batch)

		return orderClaimViewRequestDto;
	}

	/**
	 * 렌탈상품 주문취소 requestDto set
	 * @param orderClaimRegisterRequestDto
	 * @return OrderClaimViewRequestDto
	 */
	protected void setOrderClaimRequestDto(OrderClaimRegisterRequestDto orderClaimRegisterRequestDto, OrderClaimViewResponseDto orderClaimResDto) throws Exception{
		orderClaimRegisterRequestDto.setOdid(orderClaimRegisterRequestDto.getOdid());
		orderClaimRegisterRequestDto.setClaimStatusTp(OrderClaimEnums.ClaimStatusTp.CANCEL.getCode());
		orderClaimRegisterRequestDto.setClaimStatusCd(OrderEnums.OrderStatus.CANCEL_COMPLETE.getCode()); // 취소완료
		orderClaimRegisterRequestDto.setStatus(OrderEnums.OrderStatus.INCOM_COMPLETE.getCode()); 		// 결제완료
		orderClaimRegisterRequestDto.setPsClaimMallId(0); 												// 렌탈상품 MALL 마이페이지에서 주문취소할 경우 -> MALL 클레임 PK 0 고정
		orderClaimRegisterRequestDto.setTargetTp(ClaimEnums.ReasonAttributableType.COMPANY.getType());  // 판매자귀책
		orderClaimRegisterRequestDto.setRefundType(OrderClaimEnums.RefundType.REFUND_TYPE_D.getCode()); // 환불방법 - 원결제수단
		orderClaimRegisterRequestDto.setGoodsNm(orderClaimRegisterRequestDto.getGoodsNm());
		orderClaimRegisterRequestDto.setGoodsPrice(orderClaimResDto.getPriceInfo().getGoodsPrice());
		orderClaimRegisterRequestDto.setGoodsCouponPrice(orderClaimResDto.getPriceInfo().getGoodsCouponPrice());
		orderClaimRegisterRequestDto.setCartCouponPrice(orderClaimResDto.getPriceInfo().getCartCouponPrice());
		orderClaimRegisterRequestDto.setShippingPrice(orderClaimResDto.getPriceInfo().getShippingPrice());
		orderClaimRegisterRequestDto.setRefundPrice(orderClaimResDto.getPriceInfo().getRefundPrice());
		orderClaimRegisterRequestDto.setRefundPointPrice(orderClaimResDto.getPriceInfo().getRefundPointPrice());
		orderClaimRegisterRequestDto.setPartial(true); // 부분취소여부 (true : 전체취소, false : 부분취소)
		orderClaimRegisterRequestDto.setHistMsg("렌탈상품 주문취소");
		orderClaimRegisterRequestDto.setGoodsInfoList(orderClaimResDto.getOrderGoodList());
		orderClaimRegisterRequestDto.setGoodsCouponInfoList(orderClaimResDto.getGoodsCouponList());
		orderClaimRegisterRequestDto.setCartCouponInfoList(orderClaimResDto.getCartCouponList());
		orderClaimRegisterRequestDto.setFrontTp(Integer.parseInt(ClaimEnums.ClaimFrontTp.FRONT.getCode()));
		orderClaimRegisterRequestDto.setUrUserId(orderClaimRegisterRequestDto.getUrUserId());
	}

	/**
	 * 주문클레임정보 업데이트 처리
	 * @param mallOrderClaimAddPaymentResult
	 * @return
	 */
	protected OrderClaimRegisterResponseDto putOrderClaimInfo(MallOrderClaimAddPaymentResult mallOrderClaimAddPaymentResult) throws Exception {

		OrderClaimRegisterResponseDto orderClaimRegisterResponseDto = OrderClaimRegisterResponseDto.builder()
																									.orderRegistrationResult(OrderEnums.OrderRegistrationResult.SUCCESS)
																									.build();

		int updateCnt = 0;
		ClaimVo claimVo = ClaimVo.builder()
								.odClaimId(mallOrderClaimAddPaymentResult.getOdClaimId())		// 주문클레임PK
								.directPaymentYn(OrderClaimEnums.AllTypeYn.ALL_TYPE_N.getCode())	// 직접결제여부
								.build();
		updateCnt = claimProcessMapper.putOrderClaimDirectPaymentYn(claimVo);
		if(updateCnt < 1) {
			orderClaimRegisterResponseDto.setOrderRegistrationResult(OrderEnums.OrderRegistrationResult.CALIM_DIRECT_PAYMENT_UPDATE_FAIL);
			return orderClaimRegisterResponseDto;
		}

		long urUserId = mallOrderClaimAddPaymentResult.getUrUserId();
		// 기본 취소 완료
		String claimStatusCd = OrderEnums.OrderStatus.CANCEL_COMPLETE.getCode();
		// 주문 클레임 상태 구분이 반품일 경우
		if(OrderClaimEnums.ClaimStatusTp.RETURN.getCode().equals(mallOrderClaimAddPaymentResult.getClaimStatusTp())) {
			claimStatusCd = OrderEnums.OrderStatus.RETURN_COMPLETE.getCode();
			// 회수 여부가 Y 일 경우 반품 승인
			if(OrderClaimEnums.AllTypeYn.ALL_TYPE_Y.getCode().equals(mallOrderClaimAddPaymentResult.getReturnsYn())) {
				claimStatusCd = OrderEnums.OrderStatus.RETURN_ING.getCode();
			}
		}
		mallOrderClaimAddPaymentResult.setClaimStatusCd(claimStatusCd);
		ClaimDetlVo claimDetlVo = ClaimDetlVo.builder()
											.odClaimId(mallOrderClaimAddPaymentResult.getOdClaimId())	// 주문클레임PK
											.claimStatusCd(claimStatusCd)								// 클레임상태코드
											.ccId(OrderEnums.OrderStatus.CANCEL_COMPLETE.getCode().equals(claimStatusCd)	? urUserId : 0)	// 취소완료ID
											.riId(OrderEnums.OrderStatus.RETURN_ING.getCode().equals(claimStatusCd)			? urUserId : 0)	// 반품승인ID
											.rcId(OrderEnums.OrderStatus.RETURN_COMPLETE.getCode().equals(claimStatusCd)	? urUserId : 0)	// 반품완료ID
											.build();

		updateCnt = this.putOrderClaimDetlDirectPaymentClaimStatus(claimDetlVo);
		if(updateCnt < 1) {
			orderClaimRegisterResponseDto.setOrderRegistrationResult(OrderEnums.OrderRegistrationResult.CALIM_DIRECT_PAYMENT_UPDATE_FAIL);
			return orderClaimRegisterResponseDto;
		}

		ClaimDetlHistVo claimDetlHistVo = ClaimDetlHistVo.builder()
															.odClaimId(mallOrderClaimAddPaymentResult.getOdClaimId())
															.statusCd(claimStatusCd)
															.histMsg("추가배송비 직접 결제 완료")
															.createId(urUserId)
															.build();
		this.addOdClaimDetlHistByOdClaimId(claimDetlHistVo);

		return orderClaimRegisterResponseDto;
	}

	/**
	 * 클레임 상세 업데이트 처리
	 * @param claimDetlVo
	 * @return
	 * @throws Exception
	 */
	protected int putOrderClaimDetlDirectPaymentClaimStatus(ClaimDetlVo claimDetlVo) throws Exception {
		return claimProcessMapper.putOrderClaimDetlDirectPaymentClaimStatus(claimDetlVo);
	}

	/**
	 * CS환불 유효성체크
	 * @param reqDto
	 */
	protected void validCSRefundData(OrderCSRefundRegisterRequestDto reqDto) throws Exception {
		UserVo userVo = SessionUtil.getBosUserVO();
		// 로그인 정보가 없을 경우
		if(ObjectUtils.isEmpty(userVo)) {
			 throw new BaseException(OrderCsEnums.CsRefundError.NO_LOGIN.getMessage());	// 로그인 후 이용 가능
		}
		reqDto.setUrUserId(Long.parseLong(userVo.getUserId()));
		// 상품 목록이 없을 경우
		if(reqDto.getGoodsInfoList().isEmpty()) {
			throw new BaseException(OrderCsEnums.CsRefundError.NO_GOODS_LIST.getMessage());	// 상품정보 미존재
		}
		// CS환불금액이 CS환불기준금액 이상이고 CS승인코드가 승인요청(APPR_STAT.REQUEST)이 아닐 경우
		if(reqDto.getRefundPrice() >= Constants.CS_REFUND_STANDARD_PRICE && !OrderCsEnums.CsRefundApprCd.REQUEST.getCode().equals(reqDto.getCsRefundApproveCd())) {
			throw new BaseException(OrderCsEnums.CsRefundError.INVALID_APPROCE_CD.getMessage());	// 유효하지않은 승인코드
		}
		// CS환불금액이 CS환불기준금액 이상이고 이고 승인2차담당자 정보가 존재하지 않을 경우
		if(reqDto.getRefundPrice() >= Constants.CS_REFUND_STANDARD_PRICE && reqDto.getApprUserId() < 1) {
			throw new BaseException(OrderCsEnums.CsRefundError.NO_APPR_USER_ID.getMessage());	// 승인2차관리자 미존재
		}
		// 예치금 환불일때 환불 계좌정보 누락건이 존재할 경우
		if (OrderCsEnums.CsRefundTp.PAYMENT.getCode().equals(reqDto.getCsRefundTp()) &&
			(StringUtil.isEmpty(reqDto.getBankCd()) || StringUtil.isEmpty(reqDto.getAccountHolder()) || StringUtil.isEmpty(reqDto.getAccountNumber()))) {
			throw new BaseException(OrderCsEnums.CsRefundError.NO_ACCOUNT_INFO.getMessage());    // 환불 계좌정보 누락
		}
	}

	/**
	 * CS환불정보 등록
	 * @param reqDto
	 * @return
	 */
	protected void addOrderCSRefundInfo(OrderCSRefundRegisterRequestDto reqDto) throws Exception {

		String apprvalCd = OrderCsEnums.CsRefundApprCd.findByCode(reqDto.getCsRefundApproveCd()).getApprCode();
		// CS환불 정보 Set
		OdCsInfoVo odCsInfoVo = OdCsInfoVo.builder()
											.odOrderId(reqDto.getOdOrderId())					// 주문PK
											.csRefundTp(reqDto.getCsRefundTp())					// CS환불구분
											.csRefundApproveCd(reqDto.getCsRefundApproveCd())	// CS환불승인상태
											.apprStat(apprvalCd)								// 승인마스터코드
											.refundPrice(reqDto.getRefundPrice())				// 환불금액
											.csReasonMsg(reqDto.getClaimReasonMsg())			// 상세사유
											.targetTp(reqDto.getTargetTp())						// 귀책구분
											.apprReqUserId(reqDto.getUrUserId())				// 승인요청자
											.apprSubUserId(reqDto.getApprSubUserId())			// 승인1차담당자
											.apprUserId(reqDto.getApprUserId())					// 승인2차담당자
											.odCsYn(OrderClaimEnums.AllTypeYn.ALL_TYPE_Y.getCode())	// CS환불 정상 여부
											.build();
		// Mall 클레임 사유가 존재할 경우
		if(StringUtil.isNotEmpty(reqDto.getPsClaimMallId())) {
			odCsInfoVo.setPsClaimMallId(Long.parseLong(reqDto.getPsClaimMallId()));				// MALL클레임사유PK
		}
		// 환불 금액이 기준금액 보다 작을 경우 2차 승인 담당자 Set
		if(reqDto.getRefundPrice() < Constants.CS_REFUND_STANDARD_PRICE) {
			odCsInfoVo.setApprUserId(0L);
			odCsInfoVo.setApprChgUserId(0L);
		}

		// CS환불 정보 등록
		int insertCnt = claimProcessMapper.addOrderCSRefundInfo(odCsInfoVo);
		// CS환불정보PK Set
		reqDto.setOdCsId(odCsInfoVo.getOdCsId());

		// CS환불정보가 등록되지 않았을 경우
		if(insertCnt < 1) {
			throw new BaseException(OrderCsEnums.CsRefundError.CS_INFO_REGIST_FAIL.getMessage());	// CS환불 정보 등록 실패
		}
	}

	/**
	 * CS환불 정보 조회
	 * @param reqDto
	 * @return
	 * @throws Exception
	 */
	protected OrderCSRefundInfoDto getCSRefundInfo(OrderCSRefundRegisterRequestDto reqDto) throws Exception {
		return claimProcessMapper.getCSRefundInfo(reqDto);
	}

	/**
	 * CS환불 상세 정보 조회
	 * @param reqDto
	 * @return
	 * @throws Exception
	 */
	protected List<OrderCSRefundGoodsInfoDto> getCSRefundInfoDetl(OrderCSRefundRegisterRequestDto reqDto) throws Exception {
		return claimProcessMapper.getCSRefundInfoDetl(reqDto);
	}

	/**
	 * CS환불정보 수정
	 * @param reqDto
	 * @return
	 */
	protected void putOrderCSRefundInfo(OrderCSRefundRegisterRequestDto reqDto) throws Exception {

		String apprvalCd = StringUtils.isEmpty(reqDto.getApprStat()) ? OrderCsEnums.CsRefundApprCd.findByCode(reqDto.getCsRefundApproveCd()).getApprCode() : reqDto.getApprStat();
		// CS환불 정보 Set
		OdCsInfoVo odCsInfoVo = OdCsInfoVo.builder()
											.odCsId(reqDto.getOdCsId())							// CS환불정보PK
											.csRefundApproveCd(reqDto.getCsRefundApproveCd())	// CS환불승인상태
											.apprStat(apprvalCd)								// 승인마스터코드
											.apprReqUserId(reqDto.getUrUserId())				// 승인요청자
											.apprSubChgUserId(reqDto.getApprSubChgUserId())		// 승인1차처리자
											.apprChgUserId(reqDto.getApprChgUserId())			// 승인2차처리자
											.odCsYn(reqDto.getOdCsYn())							// CS환불 정상 여부
											.build();

		// CS환불 정보 등록
		int insertCnt = claimProcessMapper.putOrderCSRefundInfo(odCsInfoVo);

		// CS환불정보가 수정 되지 않았을 경우
		if(insertCnt < 1) {
			throw new BaseException(OrderCsEnums.CsRefundError.CS_INFO_UPDATE_FAIL.getMessage());	// CS환불 정보 수정 실패
		}
	}

	/**
	 * CS환불 대상 상품 목록 내 BOS클레임 사유 정보 Set
	 * @param reqDto
	 * @param claimBosresultVo
	 */
	protected void setBosClaimReasonByGoodsInfoList(OrderCSRefundRegisterRequestDto reqDto, PolicyClaimMallVo claimBosresultVo) throws Exception {
		List<OrderCSRefundGoodsInfoDto> goodsInfoList = reqDto.getGoodsInfoList();
		if(!goodsInfoList.isEmpty()) {
			for(OrderCSRefundGoodsInfoDto goodsItem : goodsInfoList) {
				goodsItem.setPsClaimBosId(StringUtil.nvlLong(claimBosresultVo.getPsClaimBosId()));
				goodsItem.setBosClaimLargeId(StringUtil.nvlLong(claimBosresultVo.getLclaimCtgryId()));
				goodsItem.setBosClaimMiddleId(StringUtil.nvlLong(claimBosresultVo.getMclaimCtgryId()));
				goodsItem.setBosClaimSmallId(StringUtil.nvlLong(claimBosresultVo.getSclaimCtgryId()));
			}
		}
	}

	/**
	 * CS환불상세정보 등록
	 * @param reqDto
	 * @return
	 */
	protected void addOrderCSRefundInfoDetl(OrderCSRefundRegisterRequestDto reqDto) throws Exception {
		int insertCnt = 0;
		List<OrderCSRefundGoodsInfoDto> goodsInfoList = reqDto.getGoodsInfoList();
		if(!goodsInfoList.isEmpty()) {
			for(OrderCSRefundGoodsInfoDto goodsItem : goodsInfoList) {
				if(goodsItem.getCsRefundPrice() < 1){
					continue;
				}
				OdCsInfoDetlVo odCsInfoDetlVo = OdCsInfoDetlVo.builder()
																.odCsId(reqDto.getOdCsId())							// CS환불정보PK
																.odOrderDetlId(goodsItem.getOdOrderDetlId())		// 주문상세PK
																.refundPrice(goodsItem.getCsRefundPrice())			// 상품별환불금액
																.psClaimBosId(goodsItem.getPsClaimBosId())			// BOS클레임사유PK
																.bosClaimLargeId(goodsItem.getBosClaimLargeId())	// BOS클레임대분류ID
																.bosClaimMiddleId(goodsItem.getBosClaimMiddleId())	// BOS클레임중분류ID
																.bosClaimSmallId(goodsItem.getBosClaimSmallId())	// BOS클레임소분류ID
																.build();
				insertCnt += claimProcessMapper.addOrderCSRefundInfoDetl(odCsInfoDetlVo);
				goodsItem.setOdCsDetlId(odCsInfoDetlVo.getOdCsDetlId());
			}
		}
		if(insertCnt < 1) {
			throw new BaseException(OrderCsEnums.CsRefundError.CS_INFO_DETL_REGIST_FAIL.getMessage());	// CS환불상세 정보 등록 실패
		}
	}

	/**
	 * CS환불계좌정보 등록
	 * @param reqDto
	 * @return
	 */
	protected void addOrderCSRefundAccountInfo(OrderCSRefundRegisterRequestDto reqDto) throws Exception {
		OdCsAccountVo odCsAccountVo = OdCsAccountVo.builder()
													.odCsId(reqDto.getOdCsId())					// CS환불정보PK
													.bankCd(reqDto.getBankCd())					// 은행코드
													.accountHolder(reqDto.getAccountHolder())	// 예금주명
													.accountNumber(reqDto.getAccountNumber())	// 계좌번호
													.build();
		int insertCnt = claimProcessMapper.addOrderCSRefundAccountInfo(odCsAccountVo);
		if (insertCnt < 1) {
			throw new BaseException(OrderCsEnums.CsRefundError.ACCOUNT_INFO_REGIST_FAIL.getMessage());    // CS환불상세 PG 정보 등록 실패
		}
	}

	/**
	 * 회원 ID로 CS환불 역할그룹의 회계코드 조회
	 * @param urUserId
	 * @return
	 */
	protected OrderCSUseFinInfoDto getUserFinInfoByLoginId(long urUserId) {
		return claimProcessMapper.getUserFinInfoByLoginId(urUserId);
	}

	/**
	 * CS환불PG정보 등록
	 * @param reqDto
	 * @return
	 */
	protected void addOrderCSRefundPGInfo(OrderCSRefundRegisterRequestDto reqDto) throws Exception {
		OdCsPgInfoVo odCsPgInfoVo = OdCsPgInfoVo.builder()
												.odCsId(reqDto.getOdCsId())									// CS환불정보PK
												.type(OrderEnums.PayType.F.getCode())						// 타입
												.status(OrderEnums.OrderStatus.INCOM_COMPLETE.getCode())	// 상태
												.tid(reqDto.getTid())										// TID
												.refundPrice(reqDto.getRefundPrice())						// 금액
												.responseData(reqDto.getResponseData())						// 응답데이터
												.build();
		int insertCnt = claimProcessMapper.addOrderCSRefundPGInfo(odCsPgInfoVo);
		if (insertCnt < 1) {
			throw new BaseException(OrderCsEnums.CsRefundError.CS_INFO_PG_REGIST_FAIL.getMessage());    // CS환불상세 PG 정보 등록 실패
		}
	}

	/**
	 * CS환불상세 이력정보 등록
	 * @param reqDto
	 * @return
	 */
	protected void addOrderCSRefundInfoDetlHist(OrderCSRefundRegisterRequestDto reqDto) throws Exception {
		List<OrderCSRefundGoodsInfoDto> goodsInfoList = reqDto.getGoodsInfoList();
		boolean isDenied = ApprovalEnums.ApprovalStatus.DENIED.getCode().equals(reqDto.getApprStat());
		String apprvalCd = StringUtils.isEmpty(reqDto.getApprStat()) ? OrderCsEnums.CsRefundApprCd.findByCode(reqDto.getCsRefundApproveCd()).getApprCode() : reqDto.getApprStat();
		String approvalNm = ApprovalEnums.ApprovalStatus.findByCode(apprvalCd).getCodeName();
		if(!goodsInfoList.isEmpty()) {
			for(OrderCSRefundGoodsInfoDto goodsItem : goodsInfoList) {
				if(goodsItem.getCsRefundPrice() < 1){
					continue;
				}
				String histMsg = "[" + approvalNm + "] [" + OrderCsEnums.CsRefundTp.findByCode(reqDto.getCsRefundTp()).getCodeName() + "] [" + StringUtil.numberFormat(goodsItem.getCsRefundPrice()) + "원]";
				// CS승인 반려일 경우 claimReasonMsg로 Set
				if(isDenied) {
					histMsg = reqDto.getClaimReasonMsg();
				}
				OdCsInfoDetlHistVo odCsInfoDetlHistVo = OdCsInfoDetlHistVo.builder()
																			.odCsId(reqDto.getOdCsId())							// CS환불정보PK
																			.odCsDetlId(goodsItem.getOdCsDetlId())				// CS환불상세PK
																			.odOrderDetlId(goodsItem.getOdOrderDetlId())		// 주문상세PK
																			.refundPrice(goodsItem.getCsRefundPrice())			// 상품별환불금액
																			.psClaimBosId(goodsItem.getPsClaimBosId())			// BOS클레임사유PK
																			.bosClaimLargeId(goodsItem.getBosClaimLargeId())	// BOS클레임대분류ID
																			.bosClaimMiddleId(goodsItem.getBosClaimMiddleId())	// BOS클레임중분류ID
																			.bosClaimSmallId(goodsItem.getBosClaimSmallId())	// BOS클레임소분류ID
																			.histMsg(histMsg)									// 이력메시지
																			.createId(reqDto.getUrUserId())						// 등록자
																			.build();
				claimProcessMapper.addOrderCSRefundInfoDetlHist(odCsInfoDetlHistVo);
			}
		}
	}

	/**
	 * 클레임 환불 대상 정보 조회
	 * @param odOrderId
	 * @param odClaimId
	 * @return
	 * @throws Exception
	 */
	protected OrderClaimRegisterRequestDto getClaimRefundPaymentInfo(long odOrderId, long odClaimId) throws Exception {
		return claimProcessMapper.getClaimRefundPaymentInfo(odOrderId, odClaimId);
	}

	/**
	 * 클레임 상세 환불 대상 정보 조회
	 * @param odOrderId
	 * @param odClaimId
	 * @return
	 * @throws Exception
	 */
	protected List<OrderClaimGoodsInfoDto> getClaimDetlRefundPaymentInfo(long odOrderId, long odClaimId) throws Exception {
		return claimProcessMapper.getClaimDetlRefundPaymentInfo(odOrderId, odClaimId);
	}

	/**
	 * PG 정보 통합 DTO 변환
	 * @param pgObject
	 * @return
	 */
	protected OrderClaimAddPaymentPgInfoDto getChgClaimAddPaymentPgIntegrated(Object pgObject) throws Exception {
		ObjectMapper objectMapper = new ObjectMapper();
		OrderClaimAddPaymentPgInfoDto pgIntegratedInfo = new OrderClaimAddPaymentPgInfoDto();

		pgIntegratedInfo.setAuthCode("");
		pgIntegratedInfo.setApprovalDate(LocalDateTime.now());
		pgIntegratedInfo.setResponseData(objectMapper.writeValueAsString(pgObject));

		String odid = "";
		String odPaymentMasterId = "";
		String tid = "";
		String amt = "";
		String cashReceiptYn = "N";
		String cashReceiptNo = "";
		String cashReceiptAuthNo = "";

		// KCP 가상계좌일 경우
		if(pgObject.getClass().equals(KcpVirtualAccountReturnRequestDto.class)) {
			cashReceiptNo = ((KcpVirtualAccountReturnRequestDto) pgObject).getCash_no();
			cashReceiptAuthNo = ((KcpVirtualAccountReturnRequestDto) pgObject).getCash_a_no();
			if (!StringUtils.isEmpty(cashReceiptAuthNo)) {
				cashReceiptYn = "Y";
			}
			odid = ((KcpVirtualAccountReturnRequestDto) pgObject).getOrder_no();
			tid = ((KcpVirtualAccountReturnRequestDto) pgObject).getTno();
			amt = ((KcpVirtualAccountReturnRequestDto) pgObject).getTotl_mnyx();
		}
		// 이니시스 PC 가상계좌일 경우
		else if(pgObject.getClass().equals(InicisVirtualAccountReturnRequestDto.class)) {
			cashReceiptNo = ((InicisVirtualAccountReturnRequestDto) pgObject).getNo_cshr_appl();
			cashReceiptAuthNo = ((InicisVirtualAccountReturnRequestDto) pgObject).getNo_cshr_tid();
			if (StringUtils.isEmpty(cashReceiptNo) || StringUtils.isEmpty(cashReceiptAuthNo)) {
				cashReceiptNo = "";
			}
			else if(!StringUtils.isEmpty(cashReceiptNo) && !StringUtils.isEmpty(cashReceiptAuthNo)) {
				cashReceiptYn = "Y";
			}
			odid = ((InicisVirtualAccountReturnRequestDto) pgObject).getNo_oid();
			tid = ((InicisVirtualAccountReturnRequestDto) pgObject).getNo_tid();
			amt = ((InicisVirtualAccountReturnRequestDto) pgObject).getAmt_input();
		}
		// 이니시스 모바일 가상계좌일 경우
		else if(pgObject.getClass().equals(InicisNotiRequestDto.class)) {
			cashReceiptAuthNo = ((InicisNotiRequestDto) pgObject).getP_TID();
			if (!StringUtils.isEmpty(cashReceiptAuthNo)) {
				cashReceiptNo = ((InicisNotiRequestDto) pgObject).getP_CSHR_AUTH_NO();
				cashReceiptYn = "Y";
			}
			odid = ((InicisNotiRequestDto) pgObject).getP_OID();
			tid = ((InicisNotiRequestDto) pgObject).getP_TID();
			amt = ((InicisNotiRequestDto) pgObject).getP_AMT();
		}

		String[] odids = odid.split(Constants.ORDER_ODID_DIV_ADD_PAYMENT_MASTER);
		odid = odids[0];
		odPaymentMasterId = odids[1];

		pgIntegratedInfo.setOdid(odid);
		pgIntegratedInfo.setOdPaymentMasterId(odPaymentMasterId);
		pgIntegratedInfo.setTid(tid);
		pgIntegratedInfo.setAmt(amt);
		pgIntegratedInfo.setCashReceiptYn(cashReceiptYn);
		pgIntegratedInfo.setCashReceiptType(OrderEnums.CashReceipt.USER.getCode());
		pgIntegratedInfo.setCashReceiptNo(cashReceiptNo);
		pgIntegratedInfo.setCashReceiptAuthNo(cashReceiptAuthNo);

		return pgIntegratedInfo;
	}

	/**
	 * 추가 결제 클레임 정보 조회
	 * @param pgIntegratedInfo
	 * @return
	 */
	protected OrderClaimAddPaymentInfoDto getAddPaymentClaimInfo(OrderClaimAddPaymentPgInfoDto pgIntegratedInfo) {
		return claimProcessMapper.getAddPaymentClaimInfo(pgIntegratedInfo);
	}

	/**
	 * 결제 마스터 상태 정보 업데이트
	 * @param orderPaymentMasterVo
	 * @return
	 */
	protected int putAddPaymentPayStatus(OrderPaymentMasterVo orderPaymentMasterVo) {
		return claimProcessMapper.putAddPaymentPayStatus(orderPaymentMasterVo);
	}

	/**
	 * 클레임 상세 이력 등록
	 * @param claimDetlHistVo
	 * @return
	 */
	protected int addOdClaimDetlHistByOdClaimId(ClaimDetlHistVo claimDetlHistVo) {
		return claimProcessMapper.addOdClaimDetlHistByOdClaimId(claimDetlHistVo);
	}

	/**
	 * 부분취소 배송비 추가결제 가상계좌 발급
	 * @param orderClaimPaymentMasterVo
	 * @return
	 */
	protected void payAdditionalShippingPriceSendSms(OrderClaimPaymentMasterDto orderClaimPaymentMasterVo) throws Exception{

		if(OrderEnums.PaymentType.VIRTUAL_BANK.getCode().equals(orderClaimPaymentMasterVo.getPayTp())){
			OrderInfoForEmailResultDto orderInfoForSmsResultDto = orderEmailBiz.getPayAdditionalShippingPriceInfoForEmail(orderClaimPaymentMasterVo.getOdPaymentMasterId());
			orderEmailSendBiz.payAdditionalShippingPrice(orderInfoForSmsResultDto);
		}
	}

	/**
	 * 녹즙클레임 상세 정보 생성
	 * @param requestDto
	 * @param refundInfoDto
	 */
	protected void createGreenJuiceOrderClaimDetlInfo(OrderClaimRegisterRequestDto requestDto, OrderClaimPriceInfoDto refundInfoDto) throws Exception {

		log.debug("---------------------------- 녹즙 클레임 상세 정보 생성 -----------------------------");
		List<OrderClaimGoodsPriceInfoDto> goodsPriceList = refundInfoDto.getGoodsPriceList();
		List<OrderClaimGoodsScheduleInfoDto> goodSchList = requestDto.getGoodSchList();

		long odClaimId = requestDto.getOdClaimId();
		long odClaimDetlId = 0;

		if (CollectionUtils.isNotEmpty(requestDto.getGoodsInfoList())) {
			for (OrderClaimGoodsInfoDto goodsInfo : requestDto.getGoodsInfoList()) {
				// 주문 상세 PK가 존재 하지 않을경우 클레임 등록 제외
				if(goodsInfo.getOdOrderDetlId() < 1) {
					continue;
				}

				OrderClaimGoodsPriceInfoDto orderClaimGoodsPriceInfoDto = null;
				if(goodsPriceList != null) {
					orderClaimGoodsPriceInfoDto = goodsPriceList.stream().filter(x -> x.getOdOrderDetlId() == goodsInfo.getOdOrderDetlId()).findAny().orElse(null);
				}

				ClaimDetlVo claimDetlVo = claimUtilProcessService.setClaimDetl(requestDto, goodsInfo, orderClaimGoodsPriceInfoDto);

				log.debug("클레임 상세 PK 1. :: <{}>", claimDetlVo.getOdClaimDetlId());

				claimProcessMapper.addOrderClaimDetl(claimDetlVo);
				odClaimDetlId = claimDetlVo.getOdClaimDetlId();
				goodsInfo.setOdClaimDetlId(odClaimDetlId);

				// 클레임 상세 일일배송 스케쥴 정보 생성
				if(org.apache.commons.lang3.ObjectUtils.isNotEmpty(goodSchList)) {
					// 동일한 주문상세PK Filter
					List<OrderClaimGoodsScheduleInfoDto> odClaimDailySchList = goodSchList.stream().filter(x -> x.getOdOrderDetlId() == goodsInfo.getOdOrderDetlId()).collect(Collectors.toList());
					claimProcessMapper.selectAddOrderClaimDetl(odClaimId, odClaimDetlId, odClaimDailySchList);
				}

				log.debug("클레임 상세 PK 2. :: <{}>", goodsInfo.getOdClaimDetlId());

				// claim history add
				ClaimDetlHistVo claimDetlHistVo = claimUtilProcessService.setClaimDetlHist(requestDto, odClaimDetlId, goodsInfo);
				claimProcessMapper.addOrderClaimDetlHist(claimDetlHistVo);
			}
		}
	}

	/**
	 * 주문상세 일일배송 스케쥴 정보 업데이트
	 * @return
	 */
	protected int putOdOrderDetlDailySch(OrderClaimRegisterRequestDto orderClaimRegisterRequestDto) throws Exception {

		log.debug("---------------------------- 녹즙 주문상세 일일배송 스케쥴 정보 업데이트 -----------------------------");
		// 녹즙 클레임 구분이 반품이고, 스케쥴 정보가 존재하지 않을 경우 0 리턴
		if(OrderClaimEnums.GreenJuiceClaimType.CLAIM_TYPE_RETURN.getCode().equals(orderClaimRegisterRequestDto.getClaimType()) &&
			ObjectUtils.isEmpty(orderClaimRegisterRequestDto.getGoodSchList())) {
			return 0;
		}
		int updateCnt = 0;
		String orderSchStatus = OrderClaimEnums.GreenJuiceScheduleStatus.SCHEDULE_STATUS_CANCEL.getCode();
		// 클레임 타입이 반품일 경우
		if(OrderClaimEnums.GreenJuiceClaimType.CLAIM_TYPE_RETURN.getCode().equals(orderClaimRegisterRequestDto.getClaimType())) {
			orderSchStatus = OrderClaimEnums.GreenJuiceScheduleStatus.SCHEDULE_STATUS_RETURNS.getCode();
		}
		// 클레임상태코드가 재배송일 경우
		if(OrderEnums.OrderStatus.EXCHANGE_COMPLETE.getCode().equals(orderClaimRegisterRequestDto.getClaimStatusCd())) {
			orderSchStatus = OrderClaimEnums.GreenJuiceScheduleStatus.SCHEDULE_STATUS_EXCHANGE.getCode();
		}
		List<OrderClaimGoodsScheduleInfoDto> goodSchList = orderClaimRegisterRequestDto.getGoodSchList();
		List<OrderDetlDailySchArrivalInfoDto> orderDetlDailySchArrivalInfoList = new ArrayList<>();
		for(OrderClaimGoodsScheduleInfoDto orderClaimGoodsScheduleInfoDto : goodSchList) {
			OrderDetlDailySchArrivalInfoDto OrderDetlDailySchArrivalInfo = OrderDetlDailySchArrivalInfoDto.builder().build();
			OrderDetlDailySchArrivalInfo.setOdOrderDetlDailySchId(orderClaimGoodsScheduleInfoDto.getOdOrderDetlDailySchId());	// 주문상세일일배송패턴PK
			OrderDetlDailySchArrivalInfo.setOrderSchStatus(orderSchStatus);														// 스케쥴주문상태
			OrderDetlDailySchArrivalInfo.setCancelCnt(orderClaimGoodsScheduleInfoDto.getClaimCnt());							// 주문취소수량
			updateCnt += claimProcessMapper.putOdOrderDetlDailySch(OrderDetlDailySchArrivalInfo);
			OrderDetlDailySchArrivalInfo.setDeliveryDt(orderClaimGoodsScheduleInfoDto.getDeliveryDt().toString());              // 도착예정일자
			orderDetlDailySchArrivalInfoList.add(OrderDetlDailySchArrivalInfo);
		}

		// OrderCnt - CancelCnt == 0 인 건 사용여부 N 처리
		claimProcessMapper.putOdOrderDetlDailySchToUseYn(orderDetlDailySchArrivalInfoList);

		// 스케쥴상태가 재배송일 경우, 원 거래 정보 Select 해서 새로운 건으로 INSERT 처리
		if(OrderEnums.OrderStatus.EXCHANGE_COMPLETE.getCode().equals(orderClaimRegisterRequestDto.getClaimStatusCd()) && !orderDetlDailySchArrivalInfoList.isEmpty()) {
			for(OrderDetlDailySchArrivalInfoDto orderDetlDailySchArrivalInfoDto : orderDetlDailySchArrivalInfoList) {
				claimProcessMapper.addOdOrderDetlDailyExchangeSch(orderDetlDailySchArrivalInfoDto);
			}
		}

		return updateCnt;
	}

	/**
	 * Request 객체 내 환불 정보 Set
	 * @param requestDto
	 * @param refundInfoDto
	 */
	protected void setRequestInfoFromRefundPrice(OrderClaimRegisterRequestDto requestDto, OrderClaimPriceInfoDto refundInfoDto) {
		log.debug("---------------------------- 녹즙 환불 정보 SET -----------------------------");
		requestDto.setGoodsPrice(refundInfoDto.getGoodsPrice());
		requestDto.setCartCouponPrice(refundInfoDto.getCartCouponPrice());
		requestDto.setGoodsCouponPrice(refundInfoDto.getGoodsCouponPrice());
		requestDto.setRefundGoodsPrice(refundInfoDto.getRefundGoodsPrice());
		requestDto.setOrderShippingPrice(refundInfoDto.getOrderShippingPrice());
		requestDto.setRefundPrice(refundInfoDto.getRefundPrice());
		requestDto.setShippingPrice(refundInfoDto.getShippingPrice());
		requestDto.setRemaindPrice(refundInfoDto.getRemainPaymentPrice());
		requestDto.setRefundPointPrice(refundInfoDto.getRefundPointPrice());
		requestDto.setRemainPointPrice(refundInfoDto.getRemainPointPrice());
		requestDto.setDeliveryCouponList(refundInfoDto.getDeliveryCouponList());
	}

	/**
	 * 주문 상세 취소 수량 업데이트
	 * @param requestDto
	 */
	protected void putOrderDetlCancelCnt(OrderClaimRegisterRequestDto requestDto) throws Exception {
		log.debug("---------------------------- 녹즙 주문 상세 취소 수량 업데이트 -----------------------------");
		if (CollectionUtils.isNotEmpty(requestDto.getGoodsInfoList())) {
			for (OrderClaimGoodsInfoDto goodsInfo : requestDto.getGoodsInfoList()) {
				OrderDetlVo orgOrderDetlVo = OrderDetlVo.builder()
						.cancelCnt(goodsInfo.getClaimCnt())             //취소수량
						.odOrderDetlId(goodsInfo.getOdOrderDetlId())    //주문상세 pk
						.build();
				log.debug("취소 갯수를 입력 한다. <{}>", orgOrderDetlVo);
				this.putOrderDetlCancel(orgOrderDetlVo);
			}
		}
	}

	/**
	 * ERP 서비스 타입 정보 얻기
	 * @return
	 */
	protected ErpApiEnums.ErpServiceType getErpServiceType(OrderClaimRegisterRequestDto orderClaimRegisterRequestDto) {

		log.debug("---------------------------- 녹즙 ERP 서비스 타입 정보 얻기 -----------------------------");
		ErpApiEnums.ErpServiceType erpServiceType = ErpApiEnums.ErpServiceType.ERP_HITOK_DAILY_DELIVERY_RETRUN_ORDER;	// 하이톡 일일배송 반품 주문
		// 클레임상태코드가 재배송일 경우
		if(OrderEnums.OrderStatus.EXCHANGE_COMPLETE.getCode().equals(orderClaimRegisterRequestDto.getClaimStatusCd())) {
			erpServiceType = ErpApiEnums.ErpServiceType.ERP_HITOK_DAILY_DELIVERY_REDELIVERY_ORDER;	// 하이톡 일일배송 재배송 주문
		}
		return erpServiceType;
	}
}


