package kr.co.pulmuone.v1.order.claim.service;

import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.enums.*;
import kr.co.pulmuone.v1.comm.enums.OrderEnums.OrderStatus;
import kr.co.pulmuone.v1.comm.enums.OrderEnums.PayType;
import kr.co.pulmuone.v1.comm.exception.BaseException;
import kr.co.pulmuone.v1.comm.util.StringUtil;
import kr.co.pulmuone.v1.order.claim.dto.*;
import kr.co.pulmuone.v1.order.claim.dto.vo.*;
import kr.co.pulmuone.v1.order.create.dto.OrderCardPayRequestDto;
import kr.co.pulmuone.v1.order.create.service.OrderCreateBiz;
import kr.co.pulmuone.v1.order.order.dto.vo.OrderDetlVo;
import kr.co.pulmuone.v1.order.status.service.OrderStatusBiz;
import kr.co.pulmuone.v1.pg.dto.BasicDataResponseDto;
import kr.co.pulmuone.v1.policy.claim.dto.PolicyClaimMallDetailResponseDto;
import kr.co.pulmuone.v1.policy.claim.dto.vo.PolicyClaimMallVo;
import kr.co.pulmuone.v1.policy.claim.service.PolicyClaimBiz;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


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
public class ClaimRequestProcessBizImpl implements ClaimRequestProcessBiz {

	@Autowired
	private ClaimProcessBiz claimProcessBiz;

    @Autowired
    private ClaimProcessService claimProcessService;

    @Autowired
    private ClaimRequestService claimRequestService;

    @Autowired
    private OrderCreateBiz orderCreateBiz;

	@Autowired
	private ClaimUtilProcessService claimUtilProcessService;

	@Autowired
	private ClaimUtilPriceService claimUtilPriceService;

	@Autowired
	private ClaimUtilRefundService claimUtilRefundService;

	@Autowired
	private ClaimCompleteProcessService claimCompleteProcessService;

	@Autowired
	private ClaimCompleteProcessBiz claimCompleteProcessBiz;

	@Autowired
	private PolicyClaimBiz policyClaimBiz;

	@Autowired
	private OrderStatusBiz orderStatusBiz;

	/**
	 * 환불 금액 정보 조회
	 * @param requestDto
	 * @return
	 * @throws Exception
	 */
	@Override
	public OrderClaimPriceInfoDto getRefundPriceInfo(OrderClaimRegisterRequestDto requestDto) throws Exception {

		claimUtilProcessService.getUserInfo(requestDto);

		//환불정보
		log.debug("--- 환불정보 얻기 시작 ! ---");
		OrderClaimViewRequestDto claimViewReqDto = claimUtilProcessService.getOrderClaimViewReq(requestDto);

		// 상품정보 조회
		List<OrderClaimGoodsInfoDto> goodsList = claimRequestService.getOrderClaimReqGoodsInfoList(claimViewReqDto);

		// 결제정보 조회
		OrderClaimPaymentInfoDto paymentInfoDto = claimRequestService.getOrderClaimPaymentInfo(claimViewReqDto.getOdOrderId());

		// 환불정보 조회
		List<OrderClaimTargetGoodsListDto> targetGoodsList = claimRequestService.getOrderClaimTargetGoodsList(claimViewReqDto);
		OrderClaimPriceInfoDto refundInfoDto = claimUtilPriceService.getRefundInfo(claimViewReqDto, goodsList, paymentInfoDto, targetGoodsList);

		return refundInfoDto;
	}

	/**
	 * 공통 클레임요청 처리프로세스
	 * @param requestDto
	 * @return ClaimRequestProcessDto
	 * @throws Exception
	 */
	@Override
	public ClaimRequestProcessDto setOrderClaimRequest(OrderClaimRegisterRequestDto requestDto) throws Exception {
		log.debug("공통 클레임요청 처리프로세스 일 때 변수 ::: <{}>", requestDto);
		// 취소요청 상태 적용
		//requestDto.setClaimStatusCd(OrderStatus.CANCEL_APPLY.getCode());

		claimUtilProcessService.getUserInfo(requestDto);

		// 프론트 클레임 신청이면 보스 클레임사유 조회
		PolicyClaimMallVo claimBosresultVo = null;
		if(OrderClaimEnums.OrderClaimFrontTpCd.FRONT_TP_FRONT.getCodeValue() == requestDto.getFrontTp() &&
			StringUtils.isNotEmpty(String.valueOf(requestDto.getPsClaimMallId()))) {
			PolicyClaimMallVo policyClaimMallVo = new PolicyClaimMallVo();
			policyClaimMallVo.setPsClaimMallId(requestDto.getPsClaimMallId());
			PolicyClaimMallDetailResponseDto resultResponseDto = (PolicyClaimMallDetailResponseDto) policyClaimBiz.getFrontPolicyClaimMall(policyClaimMallVo).getData();
			claimBosresultVo = resultResponseDto.getRows();
			requestDto.setTargetTp(claimBosresultVo.getTargetType());
		}
		else {
			this.setClaimTargetTp(requestDto);
		}

		//환불정보
		log.debug("--- 환불정보 얻기 시작 ! ---");
		OrderClaimViewRequestDto claimViewReqDto = claimUtilProcessService.getOrderClaimViewReq(requestDto);
		claimViewReqDto.setOrderStatusCd(requestDto.getOrderStatusCd());
		// 상품정보 조회
		List<OrderClaimGoodsInfoDto> goodsList = claimRequestService.getOrderClaimReqGoodsInfoList(claimViewReqDto);

		// 결제정보 조회
		OrderClaimPaymentInfoDto paymentInfoDto = claimRequestService.getOrderClaimPaymentInfo(claimViewReqDto.getOdOrderId());

		// 환불정보 조회
		List<OrderClaimTargetGoodsListDto> targetGoodsList = claimRequestService.getOrderClaimTargetGoodsList(claimViewReqDto);

		OrderClaimPriceInfoDto refundInfoDto = claimUtilPriceService.getRefundInfo(claimViewReqDto, goodsList, paymentInfoDto, targetGoodsList);
		log.debug("환불금액 :: <{}>, 배송비 :: <{}>, 나머지 잔액 :: <{}>", refundInfoDto.getRefundPrice(), refundInfoDto.getShippingPrice(), refundInfoDto.getRemainPaymentPrice());

		// 환불정보를 request에 넣어준다
		requestDto.setGoodsPrice(refundInfoDto.getGoodsPrice());
		requestDto.setCartCouponPrice(refundInfoDto.getCartCouponPrice());
		requestDto.setGoodsCouponPrice(refundInfoDto.getGoodsCouponPrice());
		requestDto.setRefundGoodsPrice(refundInfoDto.getRefundGoodsPrice());
		requestDto.setOrderShippingPrice(refundInfoDto.getOrderShippingPrice());
		requestDto.setPrevAddPaymentShippingPrice(refundInfoDto.getPrevAddPaymentShippingPrice());
		requestDto.setRefundPrice(refundInfoDto.getRefundPrice());
		requestDto.setTaxableOrderShippingPrice(refundInfoDto.getTaxableOrderShippingPrice());
		requestDto.setTaxablePrice(refundInfoDto.getTaxablePrice());
		requestDto.setNonTaxablePrice(refundInfoDto.getNonTaxablePrice());
		requestDto.setShippingPrice(refundInfoDto.getShippingPrice());
		requestDto.setRemaindPrice(refundInfoDto.getRemainPaymentPrice());
		requestDto.setRefundPointPrice(refundInfoDto.getRefundPointPrice());
		requestDto.setRemainPointPrice(refundInfoDto.getRemainPointPrice());
		requestDto.setDeliveryCouponList(refundInfoDto.getDeliveryCouponList());
		log.debug("배송비 쿠폰 재발급 정보 :: <{}>", requestDto.getDeliveryCouponList());
		log.debug("--- 환불정보 얻기 종료 ! <{}> ---", refundInfoDto.toString());

		log.debug("=== 주문취소, 부분취소 일 때 클레임 마스터  1 !!!===");

		ClaimVo claimVo = claimUtilProcessService.setClaimVo(requestDto, goodsList);
		claimProcessService.addOrderClaim(claimVo);
		long odClaimId = claimVo.getOdClaimId();
		requestDto.setOdClaimId(odClaimId);

		List<OrderClaimGoodsPriceInfoDto> goodsPriceList = refundInfoDto.getGoodsPriceList();

		log.debug("=== 주문취소, 부분취소 일 때 주문클레임 상세 2 !!!===");
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

				if(claimBosresultVo != null) {
					goodsInfo.setPsClaimBosId(StringUtil.nvlInt(claimBosresultVo.getPsClaimBosId()));			//BOS 클레임 사유 PK
					goodsInfo.setBosClaimLargeId(StringUtil.nvlInt(claimBosresultVo.getLclaimCtgryId())); 		//BOS 클레임 대분류 ID
					goodsInfo.setBosClaimMiddleId(StringUtil.nvlInt(claimBosresultVo.getMclaimCtgryId())); 		//BOS 클레임 중분류 ID
					goodsInfo.setBosClaimSmallId(StringUtil.nvlInt(claimBosresultVo.getSclaimCtgryId()));		//BOS 클레임 소분류 ID
				}

				ClaimDetlVo claimDetlVo = claimUtilProcessService.setClaimDetl(requestDto, goodsInfo, orderClaimGoodsPriceInfoDto);

				log.debug("클레임 상세 PK 1. :: <{}>", claimDetlVo.getOdClaimDetlId());

				claimProcessService.addOrderClaimDetl(claimDetlVo);
				odClaimDetlId = claimDetlVo.getOdClaimDetlId();
				odClaimId = claimDetlVo.getOdClaimId();
				goodsInfo.setOdClaimDetlId(odClaimDetlId);
				goodsInfo.setOdClaimId(odClaimId);

				log.debug("클레임 상세 PK 2. :: <{}>", goodsInfo.getOdClaimDetlId());

				// claim history add
				ClaimDetlHistVo claimDetlHistVo = claimUtilProcessService.setClaimDetlHist(requestDto, odClaimDetlId, goodsInfo);
				claimProcessService.addOrderClaimDetlHist(claimDetlHistVo);
			}

			// 클레임 사유 변경이력 저장
			claimProcessService.addOrderClaimBosReasonHist(requestDto);
		}

        // 재배송이 아닐 경우 OD_ORDER_DETL_DISCOUNT 저장 (할인취소내역)
		// 재배송 시 원거래 주문의 할인 금액 정보 추가 해주어야함. -> 추후 데이터 확인 필요
		log.debug("=== 주문클레임 할인금액 !!!===");
		List<Long> odOrderDetlIds = requestDto.getGoodsInfoList().stream().map(OrderClaimGoodsInfoDto::getOdOrderDetlId).collect(Collectors.toList());
		List<OrderClaimDetlDiscountListInfoDto> orderClaimDetlDiscountList = claimProcessService.getOrderClaimDetlDiscountList(requestDto.getOdOrderId(), odClaimId, odOrderDetlIds);
		if(CollectionUtils.isNotEmpty(orderClaimDetlDiscountList)) {
			for(OrderClaimDetlDiscountListInfoDto item : orderClaimDetlDiscountList) {
				item.setOdClaimDetlDiscountId(claimProcessBiz.getOdClaimDetlDiscountId());
			}
			claimProcessService.addOrderClaimDetlDiscountAll(orderClaimDetlDiscountList);
		}

        // 주문정보에 취소수량 저장
		if (CollectionUtils.isNotEmpty(requestDto.getGoodsInfoList())) {
			for (OrderClaimGoodsInfoDto goodsInfo : requestDto.getGoodsInfoList()) {
				if(goodsInfo.getOdOrderDetlId() < 1) {
					continue;
				}
				OrderDetlVo orgOrderDetlVo = OrderDetlVo.builder()
						.cancelCnt(goodsInfo.getClaimCnt())                //취소수량
						.odOrderDetlId(goodsInfo.getOdOrderDetlId())    //주문상세 pk
						.build();
				log.debug("취소 갯수를 입력 한다. <{}>", orgOrderDetlVo);
				claimProcessService.putOrderDetlCancel(orgOrderDetlVo);
			}
		}

		log.debug("=== 주문취소, 부분취소 일 때 주문 클레임 환불 계좌 4 !!!===");
		if((OrderClaimEnums.ClaimStatusTp.CANCEL.getCode().equals(requestDto.getClaimStatusTp()) ||
			OrderClaimEnums.ClaimStatusTp.RETURN.getCode().equals(requestDto.getClaimStatusTp())) &&		// 클레임 구분이 취소 / 반품 이고
			OrderClaimEnums.OrderClaimFrontTpCd.FRONT_TP_FRONT.getCodeValue() == requestDto.getFrontTp()) {	// 프론트 요청일 경우
			// 주문 시 등록한 환불 계좌 정보를 클레임 환불 계좌 정보로 설정한다
			OrderAccountDto refundBankResult = claimUtilRefundService.getRefundBank(requestDto.getOdOrderId());
			if(ObjectUtils.isNotEmpty(refundBankResult)){
				requestDto.setBankCd(refundBankResult.getBankCode());
				requestDto.setAccountHolder(refundBankResult.getHolderName());
				requestDto.setAccountNumber(refundBankResult.getAccountNumber());
			}
		}
		//주문 클레임 환불 계좌 관리 [OD_CLAIM_ACCOUNT] Insert
		ClaimAccountVo claimAccountVo = claimUtilProcessService.setClaimAccount(requestDto);
		if (claimAccountVo != null){
			log.debug("주문 클레임 환불 계좌 OD_CLAIM_ACCOUNT <{}>" , claimAccountVo);
			claimProcessService.addOrderClaimAccount(claimAccountVo);
		}

		// 취소 / 반품 요청 시
		if ((OrderStatus.CANCEL_APPLY.getCode().equals(requestDto.getClaimStatusCd())
				|| OrderStatus.RETURN_APPLY.getCode().equals(requestDto.getClaimStatusCd()))) {
			claimUtilProcessService.setClaimRefundPointPriceStatus(odClaimId, requestDto, OrderStatus.REFUND_APPLY.getCode(), requestDto.getGoodsInfoList(), false);
		}

		// 반품요청인 경우
		if(OrderEnums.OrderStatus.RETURN_APPLY.getCode().equals(requestDto.getClaimStatusCd())) {
			log.debug("=== 보낸 주소지 등록 !!! ===");
			claimUtilProcessService.mergeOrderClaimSendShippingZone(requestDto);

			log.debug("=== 클레임 파일 업로드 !!!===");
			claimUtilProcessService.orderClaimAttc(requestDto);
		}

		return ClaimRequestProcessDto.builder()
				.odOrderId(requestDto.getOdOrderId())
				.odClaimId(requestDto.getOdClaimId())
				.status(requestDto.getStatus())
				.claimResult(OrderEnums.OrderRegistrationResult.SUCCESS)
				.message(OrderEnums.OrderRegistrationResult.SUCCESS.getCodeName())
				.build();
	}

	/**
	 * 거부 클레임요청 처리프로세스
	 * @param requestDto
	 * @return ClaimRequestProcessDto
	 * @throws Exception
	 */
	private ClaimRequestProcessDto setOrderDenialClaimRequest(OrderClaimRegisterRequestDto requestDto) throws Exception {

		log.debug("거부 클레임요청 처리프로세스 일 때 변수 ::: <{}>", requestDto);
		claimUtilProcessService.getUserInfo(requestDto);

		long odClaimId = requestDto.getOdClaimId();

		// 추가 결제 배송건이 존재할 경우 취소 처리
		claimCompleteProcessBiz.addShippingPaymentCancel(odClaimId, requestDto.getClaimStatusCd(), requestDto.getFrontTp());

		if(OrderEnums.OrderStatus.CANCEL_DENY_DEFE.getCode().equals(requestDto.getClaimStatusCd())) {
			log.debug("=== 클레임 상태가 취소 거부 일 때 4 !!!===");
			//거부 사유가 없을때
			if (StringUtil.isEmpty(requestDto.getRejectReasonMsg())) {
				log.debug("=== 클레임 상태가 거부 사유가 없을때 5 !!!===");
				//택배사 pk 및 송장번호가 있을 때
				if (requestDto.getPsShippingCompId() != 0 && StringUtil.isNotEmpty(requestDto.getTrackingNo()) && !requestDto.getGoodsInfoList().isEmpty()) {
					claimUtilProcessService.putOrderStatusCdChange(requestDto);
					claimUtilProcessService.mergeTrackingNumber(requestDto);
					requestDto.setRejectReasonMsg(ClaimEnums.ClaimReasonMsg.CLAIM_REASON_DI.getCodeName());
					claimCompleteProcessService.putRejectReasonMsg(requestDto);

					// 2021.07.14 배송중으로 변경하면서 취소 거부 시 상품발송 SMS 추가
					List<Long> sendOdOrderDetlIdList = requestDto.getGoodsInfoList().stream().map(OrderClaimGoodsInfoDto::getOdOrderDetlId).collect(Collectors.toList());
					orderStatusBiz.sendOrderGoodsDelivery(sendOdOrderDetlIdList,false);
				}
			}
			//거부 사유가 있을 때
			else {
				log.debug("=== 거부 사유가 있을 때 6 !!!===");
				claimCompleteProcessService.putRejectReasonMsg(requestDto);
			}
		}else if(OrderEnums.OrderStatus.RETURN_DENY_DEFER.getCode().equals(requestDto.getClaimStatusCd())) {
			log.debug("=== 클레임 상태가 반품 거부 일 때 4 !!!===");
			//거부 사유가 없을때
			if(!StringUtil.isEmpty(requestDto.getRejectReasonMsg())) {
				log.debug("=== 거부 사유가 있을 때 6 !!!===");
				claimCompleteProcessService.putRejectReasonMsg(requestDto);
			}
		}

		String claimStatusCd = OrderEnums.OrderStatus.CANCEL_DENY_DEFE.getCode().equals(requestDto.getClaimStatusCd()) ? OrderEnums.OrderStatus.CANCEL_APPLY.getCode() : OrderEnums.OrderStatus.RETURN_APPLY.getCode();

		// 주문상세 취소 가능 수량 원복
		claimCompleteProcessService.putOrderDetlCancelCnt(odClaimId, claimStatusCd);

		// 주문상세 취소 미사용
		claimCompleteProcessService.putOdClaimDetlNoUse(odClaimId, claimStatusCd);

		return ClaimRequestProcessDto.builder()
				.odOrderId(requestDto.getOdOrderId())
				.odClaimId(requestDto.getOdClaimId())
				.status(requestDto.getStatus())
				.claimResult(OrderEnums.OrderRegistrationResult.SUCCESS)
				.message(OrderEnums.OrderRegistrationResult.SUCCESS.getCodeName())
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
								.odOrderId(requestDto.getOdOrderId())								//주문 PK
								.odClaimId(odClaimId) 											//주문 클레임 PK
								.odClaimDetlId(odClaimDetlId)						//주문클레임 상세 PK
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
    	if (OrderStatus.RETURN_APPLY.getCode().equals(requestDto.getClaimStatusCd())) {
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
		if (requestDto.getFrontTp() == 1 ||
				(OrderStatus.CANCEL_COMPLETE.getCode().equals(requestDto.getClaimStatusCd()) ||
				OrderStatus.RETURN_COMPLETE.getCode().equals(requestDto.getClaimStatusCd()))
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
		requestDto.setType(PayType.A.getCode());
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
	 * 귀책구분 설정
	 * @param requestDto
	 */
	private void setClaimTargetTp(OrderClaimRegisterRequestDto requestDto) {
        // 클레임 선택 상품이 존재할 경우
        if(CollectionUtils.isNotEmpty(requestDto.getGoodsInfoList())) {
            // 0번째 상품을 얻어온다
            OrderClaimGoodsInfoDto goodsInfo = requestDto.getGoodsInfoList().get(0);
            // 0번째 상품의 클레임 카테고리 정보가 존재할 경우
            if(	goodsInfo.getBosClaimLargeId() > 0 &&
                goodsInfo.getBosClaimMiddleId() > 0 &&
                goodsInfo.getBosClaimSmallId() > 0) {
                // 해당 클레임 카테고리 정보로 귀책 정보를 조회한다
                String targetTp = claimRequestService.getOdClaimTargetTpByOrderClaimGoodsInfo(goodsInfo);
                // 조회된 귀책 정보로 설정
                requestDto.setTargetTp(StringUtil.nvl(targetTp, requestDto.getTargetTp()));
            }
        }
	}

	/**
	 * 취소, 반품 클레임 정보 업데이트
	 * @param requestDto
	 * @return ClaimRequestProcessDto
	 * @throws Exception
	 */
	private ClaimRequestProcessDto setOrderReturnShippingPrice(OrderClaimRegisterRequestDto requestDto) throws Exception {
		log.debug("취소 / 반품 클레임 정보 업데이트 처리");

		long odClaimId = requestDto.getOdClaimId();

		claimUtilProcessService.getUserInfo(requestDto);

		this.setClaimTargetTp(requestDto);

		// 클레임 정보 업데이트
		claimCompleteProcessService.putOrderClaimTargetInfo(requestDto);

		// 클레임 상세 정보 업데이트
		claimProcessService.putOrderClaimDetlInfo(requestDto);

        // 클레임 선택 상품이 존재할 경우
        if(CollectionUtils.isNotEmpty(requestDto.getGoodsInfoList())) {
			// 클레임 사유 변경이력 저장
			claimProcessService.addOrderClaimBosReasonHist(requestDto);
		}

		return ClaimRequestProcessDto.builder()
				.odOrderId(requestDto.getOdOrderId())
				.odClaimId(odClaimId)
				.status(requestDto.getStatus())
				.claimResult(OrderEnums.OrderRegistrationResult.SUCCESS)
				.message(OrderEnums.OrderRegistrationResult.SUCCESS.getCodeName())
				.build();
	}

	/**
	 * 클레임 요청 처리
	 * @param claimStatusTp
	 * @param claimStatusCd
	 * @param requestDto
	 * @param isClaimSave
	 * @return ClaimRequestProcessDto
	 * @throws Exception
	 */
	public ClaimRequestProcessDto claimRequestProcess(String claimStatusTp, String claimStatusCd, OrderClaimRegisterRequestDto requestDto, boolean isClaimSave) throws Exception {

		ClaimRequestProcessDto claimRequestProcessDto = getClaimRequestProcessDto(requestDto);

		// 클레임정보 클레임 처리
		if(isClaimSave) {
			// 취소
			if(OrderClaimEnums.ClaimStatusTp.CANCEL.getCode().equals(claimStatusTp)) {
				// 취소요청
				if(OrderEnums.OrderStatus.CANCEL_APPLY.getCode().equals(claimStatusCd)) {
					claimRequestProcessDto = setOrderReturnShippingPrice(requestDto);
				}
				// 취소완료
				else if(OrderEnums.OrderStatus.CANCEL_COMPLETE.getCode().equals(claimStatusCd)) {
					claimRequestProcessDto = getClaimRequestProcessDto(requestDto);
				}
				// 취소거부
				else if(OrderEnums.OrderStatus.CANCEL_DENY_DEFE.getCode().equals(claimStatusCd)) {
					claimRequestProcessDto = setOrderDenialClaimRequest(requestDto);
				}

				// 취소거부가 아닐 경우 클레임 상태 및 클레임 상세 업데이트
				if(	OrderEnums.OrderStatus.CANCEL_COMPLETE.getCode().equals(claimStatusCd)) {
					claimRequestProcessDto = setOrderReturnShippingPrice(requestDto);
				}
			}
			// 반품
			else if(OrderClaimEnums.ClaimStatusTp.RETURN.getCode().equals(claimStatusTp)) {
				// 반품승인
				if(OrderEnums.OrderStatus.RETURN_ING.getCode().equals(claimStatusCd)) {
					claimRequestProcessDto = getClaimRequestProcessDto(requestDto);
				}
				// 반품거부
				else if(OrderEnums.OrderStatus.RETURN_DENY_DEFER.getCode().equals(claimStatusCd)) {
					claimRequestProcessDto = setOrderDenialClaimRequest(requestDto);
				}
				// 반품보류
				else if(OrderEnums.OrderStatus.RETURN_DEFER.getCode().equals(claimStatusCd)) {
					claimRequestProcessDto = getClaimRequestProcessDto(requestDto);
				}
				// 반품완료
				else if(OrderEnums.OrderStatus.RETURN_COMPLETE.getCode().equals(claimStatusCd)) {
					claimRequestProcessDto = getClaimRequestProcessDto(requestDto);
				}

				// 반품거부가 아닐 경우 클레임 상태 및 클레임 상세 업데이트
				if(	OrderEnums.OrderStatus.RETURN_APPLY.getCode().equals(claimStatusCd) ||
					OrderEnums.OrderStatus.RETURN_ING.getCode().equals(claimStatusCd) ||
					OrderEnums.OrderStatus.RETURN_COMPLETE.getCode().equals(claimStatusCd)) {
					claimRequestProcessDto = setOrderReturnShippingPrice(requestDto);
				}
			}
		}
		// 주문정보 클레임 처리
		else {
			// 취소
			if(OrderClaimEnums.ClaimStatusTp.CANCEL.getCode().equals(claimStatusTp)) {
				// 취소요청
				if(OrderEnums.OrderStatus.CANCEL_APPLY.getCode().equals(claimStatusCd)) {
					requestDto.setPriorityUndelivered("Y"); // 취소요청이면 무조건 선미출
					claimRequestProcessDto = setOrderClaimRequest(requestDto);
				}
				// 취소완료
				else if(OrderEnums.OrderStatus.CANCEL_COMPLETE.getCode().equals(claimStatusCd)) {
					claimRequestProcessDto = setOrderClaimRequest(requestDto);
				}
				// 입금 전 취소
				else if(OrderEnums.OrderStatus.INCOM_BEFORE_CANCEL_COMPLETE.getCode().equals(claimStatusCd)) {
					claimRequestProcessDto = setOrderClaimRequest(requestDto);
				}
			}
			// 반품
			else if(OrderClaimEnums.ClaimStatusTp.RETURN.getCode().equals(claimStatusTp)) {
				// 반품요청
				if(OrderEnums.OrderStatus.RETURN_APPLY.getCode().equals(claimStatusCd)) {
					claimRequestProcessDto = setOrderClaimRequest(requestDto);
				}
				// 반품승인
				if(OrderEnums.OrderStatus.RETURN_ING.getCode().equals(claimStatusCd)) {
					claimRequestProcessDto = setOrderClaimRequest(requestDto);
				}
				// 반품완료
				else if(OrderEnums.OrderStatus.RETURN_COMPLETE.getCode().equals(claimStatusCd)) {
					claimRequestProcessDto = setOrderClaimRequest(requestDto);
				}
			}
			// 재배송
			else if(OrderClaimEnums.ClaimStatusTp.RETURN_DELIVERY.getCode().equals(claimStatusTp)) {
				claimRequestProcessDto = setOrderClaimRequest(requestDto);
			}
		}

		return claimRequestProcessDto;
	}

	/**
	 * ClaimRequestProcessDto 셋팅
	 * @param requestDto
	 * @return
	 */
	private ClaimRequestProcessDto getClaimRequestProcessDto(OrderClaimRegisterRequestDto requestDto) throws Exception {

		OrderClaimPriceInfoDto refundInfoDto = this.getRefundPriceInfo(requestDto);

		requestDto.setGoodsPrice(refundInfoDto.getGoodsPrice());
		requestDto.setCartCouponPrice(refundInfoDto.getCartCouponPrice());
		requestDto.setGoodsCouponPrice(refundInfoDto.getGoodsCouponPrice());
		requestDto.setRefundGoodsPrice(refundInfoDto.getRefundGoodsPrice());
		requestDto.setOrderShippingPrice(refundInfoDto.getOrderShippingPrice());
		requestDto.setAddPaymentShippingPrice(refundInfoDto.getAddPaymentShippingPrice());
		requestDto.setPrevAddPaymentShippingPrice(refundInfoDto.getPrevAddPaymentShippingPrice());
		requestDto.setTaxableOrderShippingPrice(refundInfoDto.getTaxableOrderShippingPrice());
		requestDto.setTaxablePrice(refundInfoDto.getTaxablePrice());
		requestDto.setNonTaxablePrice(refundInfoDto.getNonTaxablePrice());
		requestDto.setRefundPrice(refundInfoDto.getRefundPrice());
		requestDto.setShippingPrice(refundInfoDto.getShippingPrice());
		requestDto.setRemaindPrice(refundInfoDto.getRemainPaymentPrice());
		requestDto.setRefundPointPrice(refundInfoDto.getRefundPointPrice());
		requestDto.setRemainPointPrice(refundInfoDto.getRemainPointPrice());
		requestDto.setEmployeePrice(refundInfoDto.getEmployeePrice());
		requestDto.setDeliveryCouponList(refundInfoDto.getDeliveryCouponList());
		requestDto.setAddPaymentList(refundInfoDto.getAddPaymentList());

		/** 배송비 쿠폰 정보 Set */
		requestDto.setDeliveryCouponList(refundInfoDto.getDeliveryCouponList());

		return ClaimRequestProcessDto.builder()
				.odOrderId(requestDto.getOdOrderId())
				.odClaimId(requestDto.getOdClaimId())
				.status(requestDto.getStatus())
				.claimResult(OrderEnums.OrderRegistrationResult.SUCCESS)
				.message(OrderEnums.OrderRegistrationResult.SUCCESS.getCodeName())
				.build();
	}
}

