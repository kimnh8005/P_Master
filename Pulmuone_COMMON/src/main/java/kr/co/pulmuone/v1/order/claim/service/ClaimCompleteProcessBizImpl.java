package kr.co.pulmuone.v1.order.claim.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.protobuf.Api;

import kr.co.pulmuone.v1.approval.auth.dto.ApprovalCsRefundRequestDto;
import kr.co.pulmuone.v1.approval.auth.dto.ApprovalExhibitRequestDto;
import kr.co.pulmuone.v1.approval.auth.dto.vo.ApprovalStatusVo;
import kr.co.pulmuone.v1.approval.auth.service.ApprovalAuthBiz;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.base.vo.UserVo;
import kr.co.pulmuone.v1.comm.constants.Constants;
import kr.co.pulmuone.v1.comm.enums.*;
import kr.co.pulmuone.v1.comm.enums.OrderEnums.OrderPresentErrorCode;
import kr.co.pulmuone.v1.comm.exception.BaseException;
import kr.co.pulmuone.v1.comm.util.SessionUtil;
import kr.co.pulmuone.v1.comm.util.StringUtil;
import kr.co.pulmuone.v1.order.claim.dto.*;
import kr.co.pulmuone.v1.order.claim.dto.vo.*;
import kr.co.pulmuone.v1.order.create.dto.OrderCardPayRequestDto;
import kr.co.pulmuone.v1.order.create.service.OrderCreateBiz;
import kr.co.pulmuone.v1.order.order.dto.PgApprovalOrderDataDto;
import kr.co.pulmuone.v1.order.order.dto.vo.OrderDetlVo;
import kr.co.pulmuone.v1.order.order.dto.vo.OrderPaymentVo;
import kr.co.pulmuone.v1.order.order.service.OrderOrderBiz;
import kr.co.pulmuone.v1.order.present.service.OrderPresentBiz;
import kr.co.pulmuone.v1.order.status.service.OrderStatusBiz;
import kr.co.pulmuone.v1.pg.dto.BasicDataResponseDto;
import kr.co.pulmuone.v1.pg.dto.CancelRequestDto;
import kr.co.pulmuone.v1.pg.dto.CancelResponseDto;
import kr.co.pulmuone.v1.pg.dto.ReceiptCancelResponseDto;
import kr.co.pulmuone.v1.pg.service.PgAbstractService;
import kr.co.pulmuone.v1.pg.service.PgBiz;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;


/**
 * <PRE>
 * Forbiz Korea
 * 주문 클레임 저장,수정,삭제 Implements
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
public class ClaimCompleteProcessBizImpl implements ClaimCompleteProcessBiz {

    @Autowired
    private ClaimProcessService claimProcessService;

    @Autowired
    private ClaimCompleteProcessService claimCompleteProcessService;

    @Autowired
    private OrderCreateBiz orderCreateBiz;

	@Autowired
	private ClaimUtilProcessService claimUtilProcessService;

	@Autowired
	private ClaimUtilRefundService claimUtilRefundService;

	@Autowired
	private PgBiz pgBiz;

	@Autowired
	private ApprovalAuthBiz approvalAuthBiz;

	@Autowired
	private ClaimProcessBiz claimProcessBiz;

	@Autowired
	private OrderOrderBiz orderOrderBiz;

	@Autowired
	private OrderPresentBiz orderPresentBiz;

	ObjectMapper objectMapper = new ObjectMapper();

	/**
	 * 취소/반품 완료처리
	 * @param requestDto
	 * @param isClaimSave
	 * @return ClaimCompleteProcessDto
	 * @throws Exception
	 */
	@Override
	public ClaimCompleteProcessDto setOrderClaimComplete(OrderClaimRegisterRequestDto requestDto, boolean isClaimSave, OrderClaimOutmallPaymentInfoDto agentTypeInfo) throws Exception {

		long odClaimId = requestDto.getOdClaimId();

		claimUtilProcessService.getUserInfo(requestDto);

		// 클레임 정보 조회
		OrderClaimInfoDto claimVo = claimProcessService.getClaimInfo(odClaimId);
		List<OrderClaimDetlListInfoDto> claimDetlList	= claimProcessService.getClaimDetlList(odClaimId);

		// 주문유형이 외부몰이고, 결제 방법이 외부몰 결제 일 경우
		if(SystemEnums.AgentType.OUTMALL.getCode().equals(agentTypeInfo.getAgentTypeCd()) &&
			OrderEnums.PaymentType.COLLECTION.getCode().equals(agentTypeInfo.getPayTp())) {
			// 외부볼 완료 처리 비지니스 로직 처리paymentType
			//claimUtilProcessService.setOutmallStatusCompleteProcess(requestDto, isClaimSave);
			claimUtilProcessService.setOutmallStatusCompleteProcess(requestDto, true);
		} else {
			// 완료 처리 비지니스 로직 처리
			claimUtilProcessService.setStatusCompleteProcess(requestDto, claimVo, claimDetlList, isClaimSave);
		}

		return ClaimCompleteProcessDto.builder()
				.odOrderId(requestDto.getOdOrderId())
				.odClaimId(odClaimId)
				.status(requestDto.getStatus())
				.claimResult(OrderEnums.OrderRegistrationResult.SUCCESS)
				.message(OrderEnums.OrderRegistrationResult.SUCCESS.getCodeName())
				.build();
	}

	/**
	 * 취소/반품 완료처리 이외
	 * @param requestDto
	 * @param isClaimSave
	 * @return ClaimCompleteProcessDto
	 * @throws Exception
	 */
	private ClaimCompleteProcessDto setOrderClaimCompleteOther(OrderClaimRegisterRequestDto requestDto, boolean isClaimSave) throws Exception {

		long odClaimId = requestDto.getOdClaimId();

		claimUtilProcessService.getUserInfo(requestDto);

		// 완료이외 처리 비지니스 로직 처리
		claimUtilProcessService.setStatusCompleteOtherProcess(requestDto, isClaimSave);

		return ClaimCompleteProcessDto.builder()
				.odOrderId(requestDto.getOdOrderId())
				.odClaimId(odClaimId)
				.status(requestDto.getStatus())
				.claimResult(OrderEnums.OrderRegistrationResult.SUCCESS)
				.message(OrderEnums.OrderRegistrationResult.SUCCESS.getCodeName())
				.build();
	}

	/**
	 * 반품승인 완료처리
	 * @param requestDto
	 * @param isClaimSave
	 * @return ClaimCompleteProcessDto
	 * @throws Exception
	 */
	private ClaimCompleteProcessDto setOrderReturnApplyClaimComplete(OrderClaimRegisterRequestDto requestDto, boolean isClaimSave) throws Exception {

		long odClaimId = requestDto.getOdClaimId();

		claimUtilProcessService.getUserInfo(requestDto);

		// 클레임 정보 조회
		List<OrderClaimDetlListInfoDto> claimDetlList = claimProcessService.getClaimDetlList(odClaimId);

		// 반품승인 처리 비지니스 로직 처리
		claimUtilProcessService.setStatusReturnApplyCompleteProcess(requestDto, isClaimSave, claimDetlList);

		return ClaimCompleteProcessDto.builder()
									.odOrderId(requestDto.getOdOrderId())
									.odClaimId(odClaimId)
									.status(requestDto.getStatus())
									.claimResult(OrderEnums.OrderRegistrationResult.SUCCESS)
									.message(OrderEnums.OrderRegistrationResult.SUCCESS.getCodeName())
									.build();
	}

	/**
	 * 재배송 처리
	 * @param requestDto
	 * @return
	 * @throws Exception
	 */
	@Override
	public ClaimCompleteProcessDto setOrderClaimRedeliveryComplete(OrderClaimRegisterRequestDto requestDto) throws Exception {

		ClaimDetlVo claimDetlVo = claimUtilProcessService.setOrderClaimRedeliveryComplete(requestDto);

		return ClaimCompleteProcessDto.builder()
										.odOrderId(0)
										.odClaimId(requestDto.getOdClaimId())
										//.status(requestDto.getStatus())
										.claimResult(OrderEnums.OrderRegistrationResult.SUCCESS)
										.build();
	}

	/**
	 * CS환불 계좌정보 등록
	 * @param requestDto
	 * @throws Exception
	 */
	public ClaimCompleteProcessDto setOrderClaimCsRefundAccountInfo(OrderClaimRegisterRequestDto requestDto) throws Exception {

		ClaimDetlVo claimDetlVo = claimUtilProcessService.setOrderClaimCsRefundAccountInfo(requestDto);

		return ClaimCompleteProcessDto.builder()
										.odOrderId(0)
										.odClaimId(requestDto.getOdClaimId())
										//.status(requestDto.getStatus())
										.claimResult(OrderEnums.OrderRegistrationResult.SUCCESS)
										.build();
	}

	/**
	 * CS환불 상태 변경 처리
	 * @param requestDto
	 * @return
	 * @throws Exception
	 */
	public ClaimCompleteProcessDto setOrderClaimCSRefundApproveCd(OrderClaimRegisterRequestDto requestDto) throws Exception {

		ClaimDetlVo claimDetlVo = claimUtilProcessService.setOrderClaimCSRefundApproveCd(requestDto);

		return ClaimCompleteProcessDto.builder()
				.odOrderId(0)
				.odClaimId(requestDto.getOdClaimId())
				//.status(requestDto.getStatus())
				.claimResult(OrderEnums.OrderRegistrationResult.SUCCESS)
				.build();
	}

	/**
	 * CS환불 처리
	 * @param requestDto
	 * @return
	 * @throws Exception
	 */
	@Override
	public ClaimCompleteProcessDto setOrderClaimCSRefundApprove(OrderClaimRegisterRequestDto requestDto, boolean isClaimSave) throws Exception {

		ClaimDetlVo claimDetlVo = claimUtilProcessService.setOrderClaimCSRefundApprove(requestDto, isClaimSave);

		return ClaimCompleteProcessDto.builder()
										.odOrderId(0)
										.odClaimId(requestDto.getOdClaimId())
										//.status(requestDto.getStatus())
										.claimResult(OrderEnums.OrderRegistrationResult.SUCCESS)
										.build();
	}

	/**
	 * 프론트 화면에서 추가 결제 할 때
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false, rollbackFor = {BaseException.class,Exception.class})
	public ApiResult<?> addPayment(OrderClaimRegisterRequestDto requestDto) throws Exception {
		log.debug("프론트 화면에서 추가 결제 할 때 :: <{}>", requestDto);

		claimUtilProcessService.getUserInfo(requestDto);

		log.debug("클레임 상태 코드 :: <{}>", requestDto.getClaimStatusCd());

		log.debug("=== 추가 결제 일 때 클레임 마스터  1 !!!===");
		ClaimVo claimVo = claimUtilProcessService.setClaimVo(requestDto, null);
		long odClaimId = claimProcessService.addOrderClaim(claimVo);
		requestDto.setOdClaimId(odClaimId);

		log.debug("=== 추가 결제 일 때 주문클레임 상세 2 !!!===");
		long odClaimDetlId = 0;
		long odClaimDetlDiscountId = 0;
		List<OrderClaimDetlInfoDto> detlInfoList = new ArrayList<>();
		if (CollectionUtils.isNotEmpty(requestDto.getGoodsInfoList())) {
			for (OrderClaimGoodsInfoDto goodsInfo : requestDto.getGoodsInfoList()) {
				ClaimDetlVo claimDetlVo = claimUtilProcessService.setClaimDetl(requestDto, goodsInfo, null);

				odClaimDetlId = claimProcessService.getOdClaimDetlId();
				claimDetlVo.setOdClaimDetlId(odClaimDetlId);

				claimProcessService.addOrderClaimDetl(claimDetlVo);

				// claim coupon add
				log.debug("=== 주문취소, 부분취소 일 때 주문클레임 할인금액 3 !!!===");
				for (OrderClaimCouponInfoDto goodsCouponInfo : requestDto.getGoodsCouponInfoList()) {
					log.debug("상품쿠폰 비교를 한다. odORderDetlId ::: coupon <{}> == claim <{}>", goodsCouponInfo.getOdOrderDetlId(), goodsInfo.getOdOrderDetlId());
					if (goodsCouponInfo.getOdOrderDetlId()== goodsInfo.getOdOrderDetlId()) {
						log.debug("상품정보와 상품쿠폰 조건이 맞는다 odORderDetlId ::: coupon <{}> == claim <{}>", goodsCouponInfo.getOdOrderDetlId(), goodsInfo.getOdOrderDetlId());

						//주문상세 할인금액 정보 [OD_ORDER_DETL_DISCOUNT]
						odClaimDetlDiscountId = claimProcessService.getOdClaimDetlDiscountId();

						ClaimDetlDiscountVo claimDetlDiscountVo = ClaimDetlDiscountVo.builder()
																					.odClaimDetlDiscountId(odClaimDetlDiscountId)		 			//주문클레임 상세 할인 PK
																					.odOrderId(requestDto.getOdOrderId())							//주문 PK
																					.odClaimId(odClaimId) 											//주문 클레임 PK
																					.odClaimDetlId(odClaimDetlId)									//주문클레임 상세 PK
																					.discountTp(goodsCouponInfo.getDiscountTp())					//상품할인 유형 공통코드(GOODS_DISCOUNT_TP - PRIORITY:우선할인, ERP_EVENT:올가할인, IMMEDIATE:즉시할인)
																					.pmCouponIssueId(goodsCouponInfo.getPmCouponIssueId())			//쿠폰 PK : PM_COUPON_ISSUE.PM_COUPON_ISSUE_ID
																					.pmCouponNm(goodsCouponInfo.getPmCouponNm())					//쿠폰명
																					.discountPrice(goodsCouponInfo.getDiscountPrice())				//할인금액
																					.psEmplDiscMasterId(goodsCouponInfo.getPsEmplDiscMasterId())	//임직원 혜택그룹 : PS_EMPL_DISC_MASTER.PS_EMPL_DISC_MASTER_ID
																					.urBrandId(goodsCouponInfo.getUrBrandId())						//임직원 혜택 표준 브랜드
																					.build();

						log.debug("주문클레임 상품 할인금액 정보 odClaimId  ::: <{}>, odClaimDetlId :::: <{}>, odClaimDetlDiscountId :: <{}>", odClaimId, odClaimDetlId, odClaimDetlDiscountId);
						log.debug("주문클레임 상품 할인금액 정보 OD_ORDER_DETL_DISCOUNT :::: <{}>", claimDetlDiscountVo);

						claimProcessService.addOrderClaimDetlDiscount(claimDetlDiscountVo);
					}
				}

				// claim history add
				ClaimDetlHistVo claimDetlHistVo = claimUtilProcessService.setClaimDetlHist(requestDto, odClaimDetlId, goodsInfo);
				claimProcessService.addOrderClaimDetlHist(claimDetlHistVo);

				OrderClaimDetlInfoDto detlInfoDto = OrderClaimDetlInfoDto.builder()
						.odClaimDetlId(odClaimDetlId)
						.odOrderDetlId(goodsInfo.getOdOrderDetlId())
						.urWarehouseId(goodsInfo.getUrWarehouseId())
						.build();
				detlInfoList.add(detlInfoDto);
			}
		}

		log.debug("=== 주문취소, 부분취소 일 때 주문 클레임 환불 계좌 4 !!!===");
		//주문 클레임 환불 계좌 관리 [OD_CLAIM_ACCOUNT] Insert
		ClaimAccountVo claimAccountVo = claimUtilProcessService.setClaimAccount(requestDto);
		if (claimAccountVo != null){
			log.debug("주문 클레임 환불 계좌 OD_CLAIM_ACCOUNT <{}>" , claimAccountVo);
			claimProcessService.addOrderClaimAccount(claimAccountVo);
		}

		//반품요청
    	if (OrderEnums.OrderStatus.RETURN_APPLY.getCode().equals(requestDto.getClaimStatusCd())) {
    		log.debug("=== 추가 결제 일 때 주문 클레임 파일 업로드 5 !!!===");
    		//주문 클레임 첨부파일 [OD_CLAIM_ATTC] Insert or Update
    		List<ClaimAttcVo> attcList = claimUtilProcessService.setOrderClaimAttc(requestDto, odClaimId);
    		for (ClaimAttcVo claimAttcVo: attcList){
				claimProcessService.addOrderClaimAttc(claimAttcVo);
			}

    		log.debug("=== 추가 결제 일 때 받는 사람 받는 사람 6 !!!===");
    		//주문 클레임 받는 배송지 [OD_CLAIM_SHIPPING_ZONE] Insert or Update
    		//orderClaimData.mergeOrderClaimShippingZone(requestDto, odClaimId, detlInfoList);
			List<ClaimShippingZoneVo> shippingZoneList = claimUtilProcessService.setOrderClaimShippingZone(requestDto, detlInfoList);
			for (ClaimShippingZoneVo shippingZoneVo: shippingZoneList){
				claimProcessService.addOrderClaimShippingZone(shippingZoneVo);
			}

    		log.debug("=== 추가 결제 일 때 보내는 사람 받는 사람 7 !!!===");
    		//주문 클레임 보내는 배송지 [OD_CLAIM_SEND_SHIPPING_ZONE] Insert or Update
			ClaimSendShippingZoneVo claimSendShippingZoneVo = claimUtilProcessService.setOrderClaimSendShippingZone(requestDto);
			claimProcessService.addOrderClaimSendShippingZone(claimSendShippingZoneVo);
    	}

		//취소완료 일 때 주문상세에 취소 수량을 입력 시킨다.
		if (requestDto.getFrontTp() == OrderClaimEnums.OrderClaimFrontTpCd.FRONT_TP_FRONT.getCodeValue() ||
				(OrderEnums.OrderStatus.CANCEL_COMPLETE.getCode().equals(requestDto.getClaimStatusCd()) ||
				OrderEnums.OrderStatus.RETURN_COMPLETE.getCode().equals(requestDto.getClaimStatusCd()))
		){ // 1: 프론트여부
			log.debug("=== 주문상세에서 취소수량을 입력 한다. !!!===");
			if (CollectionUtils.isNotEmpty(requestDto.getGoodsInfoList())) {
				for (OrderClaimGoodsInfoDto goodsInfo : requestDto.getGoodsInfoList()) {
					OrderDetlVo orgOrderDetlVo = OrderDetlVo.builder()
							.cancelCnt(goodsInfo.getClaimCnt())				//취소수량
							.odOrderDetlId(goodsInfo.getOdOrderDetlId())	//주문상세 pk
							.build();
					log.debug("취소 갯수를 입력 한다. <{}>", orgOrderDetlVo);
					claimProcessService.putOrderDetlCancel(orgOrderDetlVo);
				}
			}
		}

		//결제, 결제마스터를 입력 시킨다.
		requestDto.setType(OrderEnums.PayType.A.getCode());
		long odPaymentMasterId = claimUtilProcessService.putOrderPaymentInfo(requestDto);

		requestDto.setOdClaimId(odClaimId);
		requestDto.setOdPaymentMasterId(odPaymentMasterId);
		log.debug("결과값 requestDto odClaimId :: <{}>, odPaymentMaseterId ::: <{}>", requestDto.getOdClaimId(), requestDto.getOdPaymentMasterId());

		BasicDataResponseDto resDto = claimUtilRefundService.addPayment(requestDto, false);
		return ApiResult.success(resDto);
	}

	/**
	 * 비인증 결제 처리
	 * @param orderCardPayRequestDto
	 * @return
	 * @throws Exception
	 */
	public ApiResult<?> addCardPayOrderCreate(OrderCardPayRequestDto orderCardPayRequestDto) throws Exception {
		return orderCreateBiz.addCardPayOrderCreate(orderCardPayRequestDto);
	}

	/**
	 * 무통장 입금
	 * @param orderCardPayRequestDto
	 * @return
	 * @throws Exception
	 */
	public ApiResult<?> addBankBookOrderCreate(OrderCardPayRequestDto orderCardPayRequestDto) throws Exception {
		return orderCreateBiz.addBankBookOrderCreate(orderCardPayRequestDto);
	}

	/**
	 * 클레임 완료 처리
	 * @param claimStatusTp
	 * @param claimStatusCd
	 * @param requestDto
	 * @param isClaimSave
	 * @return ClaimCompleteProcessDto
	 * @throws Exception
	 */
	public ClaimCompleteProcessDto claimCompleteProcess(String claimStatusTp, String claimStatusCd, OrderClaimRegisterRequestDto requestDto, boolean isClaimSave) throws Exception  {

		ClaimCompleteProcessDto claimCompleteProcessDto = ClaimCompleteProcessDto.builder()
														 .odOrderId(requestDto.getOdOrderId())
														 .odClaimId(requestDto.getOdClaimId())
														 .status(requestDto.getStatus())
														 .claimResult(OrderEnums.OrderRegistrationResult.SUCCESS)
														 .message(OrderEnums.OrderRegistrationResult.SUCCESS.getCodeName())
														 .build();

		// 주문유형 조회
		OrderClaimOutmallPaymentInfoDto agentTypeInfo = claimCompleteProcessService.getOrderAgentType(requestDto.getOdOrderId());

		// 취소
		if(OrderClaimEnums.ClaimStatusTp.CANCEL.getCode().equals(claimStatusTp)){
			// 취소완료
			if(OrderEnums.OrderStatus.CANCEL_COMPLETE.getCode().equals(claimStatusCd)) {

				// 선물하기 체크
				if (orderPresentBiz.getOrderPresentByOdOrderId(requestDto.getOdOrderId()) != null) {
					// 선물하기 취소 처리
					OrderPresentErrorCode presentResult = orderPresentBiz.buyerCancel(requestDto.getOdOrderId());
					if(!OrderPresentErrorCode.SUCCESS.equals(presentResult)) {
						throw new BaseException(presentResult);
					}
				}

				// 취소 처리
				claimCompleteProcessDto = setOrderClaimComplete(requestDto, isClaimSave, agentTypeInfo);
			}
			// 취소거부
			else if(OrderEnums.OrderStatus.CANCEL_DENY_DEFE.getCode().equals(claimStatusCd)) {
				claimCompleteProcessDto = setOrderClaimCompleteOther(requestDto, isClaimSave);
			}
			// 입금 전 취소
			else if(OrderEnums.OrderStatus.INCOM_BEFORE_CANCEL_COMPLETE.getCode().equals(claimStatusCd)) {
				claimCompleteProcessDto = setOrderClaimCompleteOther(requestDto, isClaimSave);
			}

			// 취소완료 및 입금 전 취소시
			if(OrderEnums.OrderStatus.CANCEL_COMPLETE.getCode().equals(claimStatusCd)
					|| OrderEnums.OrderStatus.INCOM_BEFORE_CANCEL_COMPLETE.getCode().equals(claimStatusCd)) {

				// 일자별 출고처별 출고예정수량 업데이트
				// -- BOS 메뉴 내 업체/매장관리 > 출고처리스트 > 출고처(click) 후 노출되는 팝업의 일별출고한도 또는 새벽일별출고한도 정보의
				//    확인을 위한 집계 테이블내 정보 업데이트 용도
				orderOrderBiz.putWarehouseDailyShippingCount(Arrays.asList(requestDto.getOdOrderId()));
			}
		}
		// 반품
		else if(OrderClaimEnums.ClaimStatusTp.RETURN.getCode().equals(claimStatusTp)){
			// 반품승인
			if(OrderEnums.OrderStatus.RETURN_ING.getCode().equals(claimStatusCd)) {
				claimCompleteProcessDto = setOrderReturnApplyClaimComplete(requestDto, isClaimSave);
			}
			// 반품보류
			else if(OrderEnums.OrderStatus.RETURN_DEFER.getCode().equals(claimStatusCd)) {
				claimCompleteProcessDto = setOrderClaimCompleteOther(requestDto, isClaimSave);
			}
			// 반품거부
			else if(OrderEnums.OrderStatus.RETURN_DENY_DEFER.getCode().equals(claimStatusCd)) {
				claimCompleteProcessDto = setOrderClaimCompleteOther(requestDto, isClaimSave);
			}
			// 반품완료
			else if(OrderEnums.OrderStatus.RETURN_COMPLETE.getCode().equals(claimStatusCd)) {
				claimCompleteProcessDto = setOrderClaimComplete(requestDto, isClaimSave, agentTypeInfo);
			}
		}
		// 재배송
		else if(OrderClaimEnums.ClaimStatusTp.RETURN_DELIVERY.getCode().equals(claimStatusTp)) {
			claimCompleteProcessDto = setOrderClaimRedeliveryComplete(requestDto);
		}

		// 자동메일 발송
		claimUtilProcessService.claimSendEmail(requestDto.getStatus(), requestDto.getOdOrderId(), requestDto.getOdClaimId(), claimStatusCd, requestDto.getFrontTp(), requestDto.getGoodsInfoList());

		return claimCompleteProcessDto;
	}

	/**
	 * 클레임 철회 처리
	 * @param orderClaimRegisterRequestDto
	 * @return ApiResult<?>
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = {BaseException.class,Exception.class})
	public ApiResult<?> calimRestore(OrderClaimRegisterRequestDto orderClaimRegisterRequestDto) throws Exception {
		OrderClaimRestoreResponseDto orderClaimRestoreResponseDto = OrderClaimRestoreResponseDto.builder().build();

		orderClaimRestoreResponseDto.setResultCd(OrderEnums.OrderRegistrationResult.SUCCESS);
		long createId = Constants.GUEST_CREATE_USER_ID;

		// 자기 자신 주문인지 체크
		int userOrderCnt = claimCompleteProcessService.getOrderClaimUserOrderCnt(orderClaimRegisterRequestDto);
		log.debug("=======================userOrderCnt========================: "+userOrderCnt);
		if (userOrderCnt <= 0 && !orderClaimRegisterRequestDto.isNonMember()){
			orderClaimRestoreResponseDto.setResultCd(OrderEnums.OrderRegistrationResult.CALIM_RESTORE_NOT_MINE);
			orderClaimRestoreResponseDto.setMessage(OrderEnums.OrderRegistrationResult.CALIM_RESTORE_NOT_MINE.getCodeName());
			return ApiResult.success(orderClaimRestoreResponseDto);
		}

		List<OrderClaimRestoreDto> claimDetlList = claimCompleteProcessService.getOrderClaimDetlList(orderClaimRegisterRequestDto.getOdClaimId());

		int calimDetlCount		= claimDetlList.size();
		int resultCnt			= (int) claimDetlList.stream().filter(x -> x.getResultCnt() < 0).count();
		int sourceStatusCount	= (int) claimDetlList.stream().filter(x -> x.getClaimStatusCd().contains(orderClaimRegisterRequestDto.getSourceStatusCd())).count();
		int targetStatusCount	= (int) claimDetlList.stream().filter(x -> x.getClaimStatusCd().contains(orderClaimRegisterRequestDto.getTargetStatusCd())).count();

		// 취소/반품등 철회 완료 상태가 있는지 체크
		log.debug("=======================targetStatusCount========================: "+targetStatusCount);
		if (targetStatusCount > 0){
			orderClaimRestoreResponseDto.setResultCd(OrderEnums.OrderRegistrationResult.CALIM_RESTORE_TARGET_CD_FAIL);
			orderClaimRestoreResponseDto.setMessage(MessageFormat.format(OrderEnums.OrderRegistrationResult.CALIM_RESTORE_TARGET_CD_FAIL.getCodeName(), OrderEnums.OrderStatus.findByCode(orderClaimRegisterRequestDto.getTargetStatusCd()).getCodeName()));
			return ApiResult.success(orderClaimRestoreResponseDto);
		}

		// 취소/반품등 요청 상태 체크
		log.debug("=======================calimDetlCount========================: "+calimDetlCount);
		log.debug("=======================sourceStatusCount========================: "+sourceStatusCount);
		if (calimDetlCount != sourceStatusCount){
			orderClaimRestoreResponseDto.setResultCd(OrderEnums.OrderRegistrationResult.CALIM_RESTORE_SOURCE_CD_FAIL);
			orderClaimRestoreResponseDto.setMessage(OrderEnums.OrderRegistrationResult.CALIM_RESTORE_SOURCE_CD_FAIL.getCodeName());
			return ApiResult.success(orderClaimRestoreResponseDto);
		}
		// 취소 가능수량 이 마이너스인지 체크
		log.debug("=======================resultCnt========================: "+resultCnt);
		if (resultCnt > 0){
			orderClaimRestoreResponseDto.setResultCd(OrderEnums.OrderRegistrationResult.CALIM_RESTORE_CNT_FAIL);
			orderClaimRestoreResponseDto.setMessage(OrderEnums.OrderRegistrationResult.CALIM_RESTORE_CNT_FAIL.getCodeName());
			return ApiResult.success(orderClaimRestoreResponseDto);
		}

		if (calimDetlCount == sourceStatusCount){

			if(!orderClaimRegisterRequestDto.isNonMember()) {
				createId = Long.parseLong(orderClaimRegisterRequestDto.getUrUserId());
			}

			// 추가 결제 배송건이 존재할 경우 취소 처리
			this.addShippingPaymentCancel(orderClaimRegisterRequestDto.getOdClaimId(), orderClaimRegisterRequestDto.getSourceStatusCd(), orderClaimRegisterRequestDto.getFrontTp());

			orderClaimRestoreResponseDto.setResultCd(OrderEnums.OrderRegistrationResult.CALIM_RESTORE_SUCCESS);
			orderClaimRestoreResponseDto.setMessage(MessageFormat.format(OrderEnums.OrderRegistrationResult.CALIM_RESTORE_SUCCESS.getCodeName(), OrderEnums.OrderStatus.findByCode(orderClaimRegisterRequestDto.getTargetStatusCd()).getCodeName()));

			String claimStatusCd = OrderEnums.OrderStatus.CANCEL_WITHDRAWAL.getCode().equals(orderClaimRegisterRequestDto.getTargetStatusCd()) ? OrderEnums.OrderStatus.CANCEL_APPLY.getCode() : OrderEnums.OrderStatus.RETURN_APPLY.getCode();

			// 주문상세 취소 가능 수량 원복
			claimCompleteProcessService.putOrderDetlCancelCnt(orderClaimRegisterRequestDto.getOdClaimId(), claimStatusCd);

			// 주문상세 취소 미사용
			claimCompleteProcessService.putOdClaimDetlNoUse(orderClaimRegisterRequestDto.getOdClaimId(), claimStatusCd);

			// 철회상태변경
			claimUtilProcessService.setClaimRestoreStatus(orderClaimRegisterRequestDto.getOdClaimId(), orderClaimRegisterRequestDto.getTargetStatusCd(), createId, orderClaimRegisterRequestDto.getSourceStatusCd());

		} else {
			orderClaimRestoreResponseDto.setResultCd(OrderEnums.OrderRegistrationResult.CALIM_RESTORE_FAIL);
			orderClaimRestoreResponseDto.setMessage(MessageFormat.format(OrderEnums.OrderRegistrationResult.CALIM_RESTORE_FAIL.getCodeName(), OrderEnums.OrderStatus.findByCode(orderClaimRegisterRequestDto.getTargetStatusCd()).getCodeName()));
			return ApiResult.success(orderClaimRestoreResponseDto);
		}

		return ApiResult.success(orderClaimRestoreResponseDto);
	}

	/**
	 * 추가결제 취소 처리
	 * @param odClaimId
	 * @param prevStatusCd
	 * @throws Exception
	 */
	public void addShippingPaymentCancel(long odClaimId, String prevStatusCd, int frontTp) throws Exception{

		log.debug("결제금액 환불 시킨다 ");
		UserVo adminUser = null;
		// 프론트 타입이 BOS일 경우에만
		if(frontTp == OrderClaimEnums.OrderClaimFrontTpCd.FRONT_TP_BOS.getCodeValue()) {
			try {
				adminUser = SessionUtil.getBosUserVO();
			}
			catch (Exception e) {
				log.debug("refundPrice UserVo is Null :: <{}>", e.getMessage());
			}
		}

		OrderClaimRestoreRequestDto orderClaimRestoreRequestDto = new OrderClaimRestoreRequestDto();
		orderClaimRestoreRequestDto.setOdClaimId(odClaimId);
		orderClaimRestoreRequestDto.setType(OrderEnums.PayType.A.getCode());
		orderClaimRestoreRequestDto.setRefundType(OrderEnums.PayType.F.getCode());
		orderClaimRestoreRequestDto.setStatus(OrderEnums.OrderStatus.INCOM_COMPLETE.getCode());

		String reasonCodeName = ClaimEnums.ClaimReasonMsg.CLAIM_REASON_CW.getCodeName(); // 취소 철회
		if(OrderEnums.OrderStatus.RETURN_APPLY.getCode().equals(prevStatusCd)) {
			reasonCodeName = ClaimEnums.ClaimReasonMsg.CLAIM_REASON_RW.getCodeName(); // 반품 철회
		}
		else if(OrderEnums.OrderStatus.CANCEL_DENY_DEFE.getCode().equals(prevStatusCd)) {
			reasonCodeName = ClaimEnums.ClaimReasonMsg.CLAIM_REASON_CE.getCodeName(); // 취소 거부
		}
		else if(OrderEnums.OrderStatus.RETURN_DENY_DEFER.getCode().equals(prevStatusCd)) {
			reasonCodeName = ClaimEnums.ClaimReasonMsg.CLAIM_REASON_RE.getCodeName(); // 반품 거부
		}

		OrderEnums.PaymentType paymentType = null;
		CancelRequestDto cancelReqDto = null;
		// 클레임 ID에 매핑 된 추가 결제 정보 조회
		List<OrderClaimRestoreResultDto> orderPaymentMasterList = claimCompleteProcessService.getOrderClaimAddPaymentMaster(orderClaimRestoreRequestDto);
		// 추가 결제 내역이 존재할 경우
		if(orderPaymentMasterList != null && !orderPaymentMasterList.isEmpty()) {

			for(OrderClaimRestoreResultDto orderPaymentMasterInfo : orderPaymentMasterList) {
				// 추가 결제 금액이 존재하고, 기 환불건이 아닐 경우
				if(orderPaymentMasterInfo.getPaymentPrice() > 0 && orderPaymentMasterInfo.getRefundPaymentPrice() > 0) {

					paymentType = OrderEnums.PaymentType.findByCode(orderPaymentMasterInfo.getPayTp());

					PgEnums.PgAccountType pgAccountType = PgEnums.PgAccountType.findByCode(orderPaymentMasterInfo.getPgService());
					PgEnums.PgServiceType pgServiceType = PgEnums.PgServiceType.findByCode(pgAccountType.getPgServiceType());
					PgAbstractService<?, ?> pgService = pgBiz.getService(pgServiceType);

					cancelReqDto = new CancelRequestDto();
					cancelReqDto.setPartial(false);                                            		//부분취소 여부
					cancelReqDto.setCancelMessage(reasonCodeName);       							//취소사유
					cancelReqDto.setOdid(String.valueOf(orderPaymentMasterInfo.getOdid()));         //추문번호
					cancelReqDto.setCancelPrice(orderPaymentMasterInfo.getRefundPaymentPrice());    //취소금액
					cancelReqDto.setTid(orderPaymentMasterInfo.getTid());                           //거래번호
					cancelReqDto.setPaymentType(paymentType);                 						//취소지불수단
					cancelReqDto.setTaxCancelPrice(orderPaymentMasterInfo.getRefundPaymentPrice()); //취소 과세 금액
					cancelReqDto.setTaxFreecancelPrice(0);                							//취소 비과세 금액
					cancelReqDto.setExpectedRestPrice(0);                   						//취소후 남은 금액 (부분취소시 필수)
					cancelReqDto.setRefundBankNumber("");                             				//환불계좌번호 (가상계좌 환불 필수)
					cancelReqDto.setRefundBankCode("");                                				//환불계좌은행코드 - PG 은행 코드 (가상계좌 환불 필수)
					cancelReqDto.setRefundBankName("");                                 			//환불계좌 예금주명 (가상계좌 환불 필수)
					cancelReqDto.setEscrowYn(orderPaymentMasterInfo.getEscrowYn());                 //에스크로결제여부
					log.debug("환불할 정보 ::: <{}>", cancelReqDto);
					log.debug("PG TYPE :: <{}>", orderPaymentMasterInfo.getPgService());

					CancelResponseDto cardDto = pgService.cancel(pgAccountType.getCode(), cancelReqDto);

					log.info("카드 취소 결과 ::: <{}>", cardDto);

					if (cardDto.isSuccess()) {
						// 환불 후 해당 결제정보 환불 완료 처리
						OrderClaimRegisterRequestDto reqDto = new OrderClaimRegisterRequestDto();
						reqDto.setOdid(orderPaymentMasterInfo.getOdid());					//주문번호
						reqDto.setOdOrderId(orderPaymentMasterInfo.getOdOrderId());			//주문PK
						reqDto.setOdClaimId(orderPaymentMasterInfo.getOdClaimId());			//클레임PK
						reqDto.setType(OrderEnums.PayType.F.getCode());						//결제타입 : G- 결제, A- 추가결제, F- 환불
						reqDto.setPayTp(orderPaymentMasterInfo.getPayTp());					//결제방법
						reqDto.setTid(orderPaymentMasterInfo.getTid());						//거래번호
						reqDto.setPgService(orderPaymentMasterInfo.getPgService());			//PG Service
						reqDto.setShippingPrice(orderPaymentMasterInfo.getPaymentPrice());	//배송비
						reqDto.setTaxablePrice(orderPaymentMasterInfo.getPaymentPrice());	//과세금액
						reqDto.setRefundPrice(orderPaymentMasterInfo.getPaymentPrice());	//환불금액
						reqDto.setEscrowYn(orderPaymentMasterInfo.getEscrowYn());			//에스크로여부
						reqDto.setResponseData(objectMapper.writeValueAsString(cardDto));	//응답메시지
						claimUtilProcessService.putOrderPaymentInfo(reqDto);
					} else {
						// BOS 사용자 이고, 관리자_LEVEL_1 권한일 경우
						if(ObjectUtils.isNotEmpty(adminUser) && adminUser.getListRoleId().contains(Constants.ADMIN_LEVEL_1_AUTH_ST_ROLE_TP_ID)) {
							log.debug("====================== 관리자_LEVEL_1 권한 정상 처리");
							// 실패 여부와 상관없이 정상 처리
							// 환불 후 해당 결제정보 환불 완료 처리
							OrderClaimRegisterRequestDto reqDto = new OrderClaimRegisterRequestDto();
							reqDto.setOdid(orderPaymentMasterInfo.getOdid());					//주문번호
							reqDto.setOdOrderId(orderPaymentMasterInfo.getOdOrderId());			//주문PK
							reqDto.setOdClaimId(orderPaymentMasterInfo.getOdClaimId());			//클레임PK
							reqDto.setType(OrderEnums.PayType.F.getCode());						//결제타입 : G- 결제, A- 추가결제, F- 환불
							reqDto.setPayTp(orderPaymentMasterInfo.getPayTp());					//결제방법
							reqDto.setTid(orderPaymentMasterInfo.getTid());						//거래번호
							reqDto.setPgService(orderPaymentMasterInfo.getPgService());			//PG Service
							reqDto.setShippingPrice(orderPaymentMasterInfo.getPaymentPrice());	//배송비
							reqDto.setTaxablePrice(orderPaymentMasterInfo.getPaymentPrice());	//과세금액
							reqDto.setRefundPrice(orderPaymentMasterInfo.getPaymentPrice());	//환불금액
							reqDto.setEscrowYn(orderPaymentMasterInfo.getEscrowYn());			//에스크로여부
							reqDto.setResponseData(objectMapper.writeValueAsString(cardDto));	//응답메시지
							claimUtilProcessService.putOrderPaymentInfo(reqDto);
						}
						else {
							throw new BaseException(cardDto.getMessage());
						}
					}
				}
			}
		}
	}

	/**
     * 주문 CS환불 승인 리스트
     * @param requestDto
     * @return
     * @throws Exception
     */
	@Override
	public ApiResult<?> getApprovalCsRefundList(ApprovalCsRefundRequestDto requestDto) throws Exception {
		return ApiResult.success(claimCompleteProcessService.getApprovalCsRefundList(requestDto));
	}

	/**
     * CS환불 승인 요청철회
     * @param odCsIdList
     * @return
     * @throws Exception
     */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = {BaseException.class, Exception.class})
	public ApiResult<?> putCancelRequestApprovalCsRefund(List<String> odCsIdList) throws Exception {
		String taskCode = ApprovalEnums.ApprovalAuthType.APPR_KIND_TP_CS_REFUND.getCode();
		if (CollectionUtils.isNotEmpty(odCsIdList)) {
			for (String odCsId : odCsIdList) {
				ApiResult<?> apiResult = approvalAuthBiz.checkCancelable(taskCode, odCsId);

				if (apiResult.getCode().equals(ApprovalEnums.ApprovalValidation.CANCELABLE.getCode())) {
					ApprovalStatusVo approvalVo = (ApprovalStatusVo) apiResult.getData();
					MessageCommEnum enums = claimCompleteProcessService.putCancelRequestApprovalCsRefund(approvalVo);
					if (!BaseEnums.Default.SUCCESS.equals(enums)) {
						throw new BaseException(enums);
					}
				} else {
					return apiResult;
				}
			}
		} else {
			return ApiResult.fail();
		}

    	return ApiResult.success();
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = {BaseException.class, Exception.class})
	public ApiResult<?> putApprovalProcessCsRefund(String reqApprStat, List<String> odCsIdList, String statusComment) throws Exception {
    	if(!ApprovalEnums.ApprovalStatus.DENIED.getCode().equals(reqApprStat)
    			&& !ApprovalEnums.ApprovalStatus.APPROVED.getCode().equals(reqApprStat)) {
    		return ApiResult.result(ApprovalEnums.ApprovalValidation.NONE_REQUEST);
    	}

    	String taskCode = ApprovalEnums.ApprovalAuthType.APPR_KIND_TP_CS_REFUND.getCode();

    	if(CollectionUtils.isNotEmpty(odCsIdList)) {
    		for(String odCsId : odCsIdList) {
    			if(!StringUtil.isNumeric(odCsId)) continue;
    			ApiResult<?> apiResult = approvalAuthBiz.checkApprovalProcess(taskCode, odCsId);

    			if(apiResult.getCode().equals(ApprovalEnums.ApprovalValidation.APPROVABLE.getCode())) {
    				ApprovalStatusVo approvalVo = (ApprovalStatusVo)apiResult.getData();
					//반려
    				if(ApprovalEnums.ApprovalStatus.DENIED.getCode().equals(reqApprStat)) {
    					approvalVo.setApprStat(reqApprStat);
    					approvalVo.setStatusComment(statusComment);
						// CS 환불 반려 처리
						OrderCSRefundRegisterRequestDto csRefundRequestDto = new OrderCSRefundRegisterRequestDto();
						csRefundRequestDto.setOdCsId(Long.parseLong(odCsId));
						csRefundRequestDto.setApprStat(ApprovalEnums.ApprovalStatus.DENIED.getCode());
						csRefundRequestDto.setCsRefundApproveCd(OrderCsEnums.CsRefundApprCd.DENIED.getCode());
						csRefundRequestDto.setClaimReasonMsg(statusComment);
						claimProcessBiz.procOrderCSRefundApprove(csRefundRequestDto);
        			}
    				//승인
    				if(ApprovalEnums.ApprovalStatus.APPROVED.getCode().equals(reqApprStat)
    					&& ApprovalEnums.ApprovalStatus.APPROVED.getCode().equals(approvalVo.getApprStat())) {
    					approvalVo.setMasterStat(OrderCsEnums.CsRefundApproveCd.APPROVED.getCode());
						// CS 환불 승인 처리
						OrderCSRefundRegisterRequestDto csRefundRequestDto = new OrderCSRefundRegisterRequestDto();
						csRefundRequestDto.setOdCsId(Long.parseLong(odCsId));
						csRefundRequestDto.setApprStat(ApprovalEnums.ApprovalStatus.APPROVED.getCode());
						csRefundRequestDto.setCsRefundApproveCd(OrderCsEnums.CsRefundApprCd.APPROVED.getCode());
						claimProcessBiz.procOrderCSRefundApprove(csRefundRequestDto);
        			}

        			MessageCommEnum emums = claimCompleteProcessService.putApprovalProcessCsRefund(approvalVo);
        			if(!BaseEnums.Default.SUCCESS.equals(emums)) {
        				throw new BaseException(emums);
        			}
    			} else {
    				return apiResult;
    			}
    		}
    	}else return ApiResult.fail();
    	return ApiResult.success();
	}

	/**
	 * @Desc 주문 클레임 CS환불 승인 폐기처리
	 * @param approvalGoodsRequestDto
	 * @return
	 * @throws Exception
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = {BaseException.class, Exception.class})
	public ApiResult<?> putDisposalApprovalCsRefund(ApprovalCsRefundRequestDto requestDto) throws Exception {

    	String taskCode = ApprovalEnums.ApprovalAuthType.APPR_KIND_TP_CS_REFUND.getCode();

    	if(CollectionUtils.isNotEmpty(requestDto.getOdCsIdList())) {
    		for(String odCsId : requestDto.getOdCsIdList()) {
    			ApiResult<?> apiResult = approvalAuthBiz.checkDisposable(taskCode, odCsId);

    			if(apiResult.getCode().equals(ApprovalEnums.ApprovalValidation.DISPOSABLE.getCode())) {
    				ApprovalStatusVo approvalVo = (ApprovalStatusVo)apiResult.getData();
        			MessageCommEnum emums = claimCompleteProcessService.putDisposalApprovalCsRefund(approvalVo);
        			if(!BaseEnums.Default.SUCCESS.equals(emums)) {
        				throw new BaseException(emums);
        			}
    			}else {
    				return apiResult;
    			}
    		}
    	}
    	else return ApiResult.fail();

		return ApiResult.success();
	}

	/**
	 * 주문 클레임 CS환불승인상태 변경
	 * @param orderClaimCSRefundStatusUpdateDto
	 * @return
	 */
//	@Override
//	public ApiResult<?> putOrderClaimCsRefundApproveCd(OrderClaimCSRefundStatusUpdateDto orderClaimCSRefundStatusUpdateDto) throws Exception{
//		UserVo userVo = SessionUtil.getBosUserVO();
//		long userId = Long.parseLong(userVo.getUserId());
//
//		//주문클레임 마스터 상태 수정
//		claimCompleteProcessService.putOrderClaimCsRefundApproveCd(orderClaimCSRefundStatusUpdateDto, userId);
//
//		return ApiResult.success();
//	}

	/**
	 * 관리자 현금영수증발행 주문건 취소처리
	 * @param orderClaimRegisterReqDto
	 * @return
	 */
	@Override
	public ApiResult<?> receiptCancel(OrderClaimRegisterRequestDto orderClaimRegisterReqDto) throws Exception{

		// 현금영수증 발행정보 조회(관리자가 BOS에서 발급한 건에 한함)
		OrderClaimCashReceiptDto cashReceiptDto = claimCompleteProcessService.getOrderCashReceiptByBos(orderClaimRegisterReqDto.getOdOrderId());
		if(ObjectUtils.isNotEmpty(cashReceiptDto)){

			// 현금영수증 재발행 금액 조회
			OrderPaymentVo reissuePriceVo = claimCompleteProcessService.getOrderCashReceiptReissuePrice(orderClaimRegisterReqDto.getOdOrderId(), orderClaimRegisterReqDto.getTid());
			if(cashReceiptDto.getCashPrice() != reissuePriceVo.getPaymentPrice()){
				// 현금영수증 취소
				claimCompleteProcessService.receiptCancel(cashReceiptDto);

				// 부분취소일 경우 남은금액 현금영수증 재발행
				if(reissuePriceVo.getPaymentPrice() > 0){
					claimCompleteProcessService.receiptReissue(cashReceiptDto,reissuePriceVo);
				}
			}

		}

		return ApiResult.success();
	}
}

