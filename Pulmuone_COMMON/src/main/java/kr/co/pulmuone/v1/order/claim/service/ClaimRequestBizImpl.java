package kr.co.pulmuone.v1.order.claim.service;

import com.google.gson.Gson;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.enums.*;
import kr.co.pulmuone.v1.comm.enums.OrderEnums.OrderStatus;
import kr.co.pulmuone.v1.goods.goods.dto.ArrivalScheduledDateDto;
import kr.co.pulmuone.v1.goods.goods.service.GoodsGoodsBiz;
import kr.co.pulmuone.v1.order.claim.dto.*;
import kr.co.pulmuone.v1.order.claim.dto.vo.OdAddPaymentReqInfo;
import kr.co.pulmuone.v1.pg.dto.BasicDataResponseDto;
import kr.co.pulmuone.v1.policy.claim.dto.PolicyClaimMallRequestDto;
import kr.co.pulmuone.v1.policy.claim.dto.PolicyClaimMallResponseDto;
import kr.co.pulmuone.v1.policy.claim.service.PolicyClaimBiz;
import kr.co.pulmuone.v1.policy.clause.dto.GetClauseRequestDto;
import kr.co.pulmuone.v1.policy.clause.dto.vo.GetLatestJoinClauseListResultVo;
import kr.co.pulmuone.v1.policy.clause.service.PolicyClauseBiz;
import kr.co.pulmuone.v1.policy.payment.dto.PayUseListDto;
import kr.co.pulmuone.v1.policy.payment.service.PolicyPaymentBiz;
import kr.co.pulmuone.v1.store.warehouse.service.StoreWarehouseBiz;
import kr.co.pulmuone.v1.store.warehouse.service.dto.vo.UrWarehouseVo;
import kr.co.pulmuone.v1.user.buyer.dto.CommonGetCodeListResponseDto;
import kr.co.pulmuone.v1.user.buyer.dto.CommonGetRefundBankRequestDto;
import kr.co.pulmuone.v1.user.buyer.dto.vo.CodeInfoVo;
import kr.co.pulmuone.v1.user.buyer.dto.vo.CommonGetRefundBankResultVo;
import kr.co.pulmuone.v1.user.buyer.service.UserBuyerBiz;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;


/**
 * <PRE>
 * Forbiz Korea
 * 주문 클레임 상세정보 Implements
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :   작성일                :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 *  1.0    2020. 12. 15.  강상국         최초작성
 * =======================================================================
 * </PRE>
 */
@Slf4j
@Service
public class ClaimRequestBizImpl implements ClaimRequestBiz {

    @Autowired
    private ClaimRequestService claimRequestService;

    @Autowired
    private UserBuyerBiz userBuyerBiz;

	@Autowired
	private GoodsGoodsBiz goodsGoodsBiz;

	@Autowired
	private PolicyPaymentBiz policyPaymentbiz;

	@Autowired
	private ClaimUtilPriceService claimUtilPriceService;

	@Autowired
	private ClaimUtilRequestService claimUtilRequestService;

	@Autowired
	private ClaimUtilRefundService claimUtilRefundService;

	@Autowired
	private ClaimProcessService claimProcessService;

	@Autowired
	private PolicyClauseBiz policyClauseBiz;

	@Autowired
	private PolicyClaimBiz policyClaimBiz;

	@Autowired
	private StoreWarehouseBiz storeWarehouseBiz;

	/**
	 * @Desc 주문클레임 신청 화면에서 사유 목록 조회
	 * @return
	 */
	@Override
	public ApiResult<?> getOrderClaimReasonList(PolicyClaimMallRequestDto dto) {
		return ApiResult.success(claimRequestService.getOrderClaimReasonList(dto));
	}

    /**
     * @Desc 프론트에서 주문상태가 배송중, 배송완료 일 때 반품신청 대상 상품 목록
     * @return
     */
    @Override
    public ApiResult<?> getOrderClaimInfo(OrderClaimViewRequestDto orderClaimViewRequestDto) throws Exception {

    	log.debug("getOrderClaimInfo requestParam === <{}>", orderClaimViewRequestDto.toString());
    	long odOrderId = orderClaimViewRequestDto.getOdOrderId();

    	String claimStatusTp = orderClaimViewRequestDto.getClaimStatusTp();

    	// MALL 에서 접근 시 본인 주문건이 아닐 경우 오류 처리
    	if(orderClaimViewRequestDto.getFrontTp() > 0) {
    		int selfOrderCnt = claimRequestService.getSelfOrderCnt(orderClaimViewRequestDto);
    		if(selfOrderCnt < 1) {
				return ApiResult.result(OrderEnums.OrderErrMsg.VALUE_EMPTY);
			}
		}

		OrderClaimInfoDto odClaimInfo = new OrderClaimInfoDto();

    	// 클레임PK가 존재할 경우 클레임 정보 조회
    	if(orderClaimViewRequestDto.getOdClaimId() > 0) {
			odClaimInfo = claimProcessService.getClaimInfo(orderClaimViewRequestDto.getOdClaimId());

			// 귀책구분 정보 존재하지 않을 경우 귀책 구분 정보 Set
			if(StringUtils.isEmpty(orderClaimViewRequestDto.getTargetTp())) {
				orderClaimViewRequestDto.setTargetTp(odClaimInfo.getTargetTp());
			}

			// 회수 정보 존재하지 않을 경우 회수 정보 Set
			if(StringUtils.isEmpty(orderClaimViewRequestDto.getReturnsYn())) {
				orderClaimViewRequestDto.setReturnsYn(odClaimInfo.getReturnsYn());
			}
		}

    	//상품정보
    	List<OrderClaimGoodsInfoDto> goodsList = claimRequestService.getOrderClaimReqGoodsInfoList(orderClaimViewRequestDto);

		//미출정보
		List<OrderClaimGoodsInfoDto> undeliveredList = claimRequestService.getUndeliveredInfoList(orderClaimViewRequestDto);

    	// 증정품 리스트
		List<OrderClaimGoodsInfoDto> giftGoodsList = claimUtilRequestService.getClaimGiftGoodsList(goodsList);

    	// 상품 리스트 변환 작업
		List<OrderClaimGoodsListDto> goodsResultList = claimUtilRequestService.getClaimGoodsList(goodsList, orderClaimViewRequestDto);

    	//상품쿠폰
    	OrderClaimViewRequestDto goodsCouponDto = new OrderClaimViewRequestDto();
    	BeanUtils.copyProperties(orderClaimViewRequestDto, goodsCouponDto);
    	goodsCouponDto.setCouponTp(OrderClaimEnums.ClaimCouponTp.COUPON_GOODS.getCode());
    	List<OrderClaimCouponInfoDto> goodsCouponList = claimRequestService.getCouponInfoList(goodsCouponDto);

    	//장바구니쿠폰
    	OrderClaimViewRequestDto cartCouponDto = new OrderClaimViewRequestDto();
    	BeanUtils.copyProperties(orderClaimViewRequestDto, cartCouponDto);
    	cartCouponDto.setCouponTp(OrderClaimEnums.ClaimCouponTp.COUPON_CART.getCode());
    	List<OrderClaimCouponInfoDto> cartCouponList = claimRequestService.getCouponInfoList(cartCouponDto);

    	//결제정보 구하기
    	OrderClaimPaymentInfoDto paymentInfoDto = claimRequestService.getOrderClaimPaymentInfo(odOrderId);

    	//환불정보 구하기
		List<OrderClaimTargetGoodsListDto> targetGoodsList = claimRequestService.getOrderClaimTargetGoodsList(orderClaimViewRequestDto);
    	OrderClaimPriceInfoDto refundInfoDto = claimUtilPriceService.getRefundInfo(orderClaimViewRequestDto, goodsList, paymentInfoDto, targetGoodsList);

    	// 증정품체크
		// MALL 조회 이고, 클레임 등록 건이 아닐 경우
		if(orderClaimViewRequestDto.getFrontTp() == OrderClaimEnums.OrderClaimFrontTpCd.FRONT_TP_FRONT.getCodeValue() && orderClaimViewRequestDto.getOdClaimId() < 1) {
			// 선택 상품이 존재 할 경우
			if(CollectionUtils.isNotEmpty(orderClaimViewRequestDto.getGoodSearchList())) {
				OrderClaimViewRequestDto orderGoodsListReq = new OrderClaimViewRequestDto();
				orderGoodsListReq.setFrontTp(OrderClaimEnums.OrderClaimFrontTpCd.FRONT_TP_FRONT.getCodeValue());
				orderGoodsListReq.setOdOrderId(orderClaimViewRequestDto.getOdOrderId());
				// 주문 전체 상품 목록 조회
				List<OrderClaimGoodsInfoDto> allGoodsInfoList = claimRequestService.getOrderClaimReqGoodsInfoList(orderGoodsListReq);
				giftGoodsList = claimUtilRequestService.setClaimGiftGoodsPsbYn(allGoodsInfoList, orderClaimViewRequestDto.getGoodSearchList(), refundInfoDto);
			}
		}

    	//요청주문상태 코드가 CS 환불일 경우
//    	if(OrderStatus.CUSTOMER_SERVICE_REFUND.getCode().equals(orderClaimViewRequestDto.getPutOrderStatusCd())) {
		//CS 환불 요청 금액 / 적립금 금액 조회
//		claimUtilRefundService.setCsRefundPriceInfo(orderClaimViewRequestDto, refundInfoDto);
//		log.debug("refundPrice :: <{}>, refundPointPrice :: <{}>", refundInfoDto.getRefundPrice(), refundInfoDto.getRefundPointPrice());
//		log.debug("remaindPrice :: <{}>, remaindPointPrice :: <{}>", refundInfoDto.getRemainPaymentPrice(), refundInfoDto.getRemainPointPrice());
//		}

    	//환불계좌 조회
		OrderAccountDto refundBankResult = claimUtilRefundService.getRefundBank(orderClaimViewRequestDto.getOdOrderId());
		OrderClaimAccountInfoDto accountInfoDto = new OrderClaimAccountInfoDto();
		if(orderClaimViewRequestDto.getOdClaimId() > 0) {
			accountInfoDto = claimRequestService.getOrderClaimAccountInfo(orderClaimViewRequestDto);
			if(ObjectUtils.isEmpty(accountInfoDto)) {
				accountInfoDto = new OrderClaimAccountInfoDto();
			}
		}
		else {
			if (ObjectUtils.isNotEmpty(refundBankResult)) {
				accountInfoDto.setBankCd(refundBankResult.getBankCode());
				accountInfoDto.setAccountHolder(refundBankResult.getHolderName());
				accountInfoDto.setAccountNumber(refundBankResult.getAccountNumber());
			}
			else {
				String urUserId = String.valueOf(claimRequestService.getOrderUrUserId(orderClaimViewRequestDto.getOdOrderId()));
				CommonGetRefundBankRequestDto dto = new CommonGetRefundBankRequestDto();
				dto.setUrUserId(urUserId);
				CommonGetRefundBankResultVo refundInfo = userBuyerBiz.getRefundBank(dto);
				if (ObjectUtils.isNotEmpty(refundInfo)) {
					accountInfoDto.setBankCd(refundInfo.getBankCode());
					accountInfoDto.setAccountHolder(refundInfo.getHolderName());
					accountInfoDto.setAccountNumber(refundInfo.getAccountNumber());
				}
			}
		}

    	//환불은행 조회
    	CommonGetCodeListResponseDto commonCodeList = (CommonGetCodeListResponseDto) userBuyerBiz.getRefundBankInfo().getData();
		List<CodeInfoVo> refundBankList = commonCodeList.getRows();

		//신용카드 회사 및 개월수
		PayUseListDto payUseListDto = policyPaymentbiz.getPayUseList();

    	//첨부파일 조회
    	List<OrderClaimAttcInfoDto> attcInfoList = new ArrayList<>();

    	//보내는 배송지 조회
    	OrderClaimSendShippingInfoDto sendShippingInfo = new OrderClaimSendShippingInfoDto();

    	//받는 배송지 조회
    	List<OrderClaimShippingInfoDto> receShippingInfoList = new ArrayList<>();

    	//if(OrderClaimEnums.ClaimStatusTp.RETURN.getCode().equals(claimStatusTp)) {

			attcInfoList = claimRequestService.getOrderClaimAttcInfoList(orderClaimViewRequestDto);

			log.debug("주문상태코드 :: <{}>, <{}>", orderClaimViewRequestDto.getOrderStatusCd(), OrderStatus.DELIVERY_ING.getCode());
			if (OrderStatus.DELIVERY_ING.getCode().equals(orderClaimViewRequestDto.getOrderStatusCd()) || OrderStatus.DELIVERY_COMPLETE.getCode().equals(orderClaimViewRequestDto.getOrderStatusCd())
					|| OrderStatus.BUY_FINALIZED.getCode().equals(orderClaimViewRequestDto.getOrderStatusCd()) ||
					OrderStatus.RETURN_APPLY.getCode().equals(orderClaimViewRequestDto.getOrderStatusCd()) ||
					OrderStatus.RETURN_ING.getCode().equals(orderClaimViewRequestDto.getOrderStatusCd()) ||
					OrderStatus.RETURN_COMPLETE.getCode().equals(orderClaimViewRequestDto.getOrderStatusCd()) ) {
				log.debug("보내는, 받는 배송지 조회 !!!");
				sendShippingInfo = claimRequestService.getOrderClaimSendShippingInfo(orderClaimViewRequestDto);
				if(Objects.isNull(sendShippingInfo)) {
					sendShippingInfo = claimRequestService.getSendShippingInfo(orderClaimViewRequestDto);
				}
				receShippingInfoList = claimRequestService.getReceShippingInfoList(orderClaimViewRequestDto, goodsList);
			}
		//}

		List<OrderClaimSearchGoodsDto> goodSearchList = orderClaimViewRequestDto.getGoodSearchList();

    	OrderClaimViewResponseDto orderClaimViewResponseDto = OrderClaimViewResponseDto.builder()
    			.orderMasterInfo(claimRequestService.getOrderMasterInfo(odOrderId))				//주문마스터정보
    			.odClaimInfo(odClaimInfo)				// 클레임정보
    			.orderGoodList(goodsList)									//상품 목록
				.goodList(goodsResultList)
				.undeliveredList(undeliveredList)			// 미출정보
				.giftList(giftGoodsList)
    			.paymentInfo(paymentInfoDto)								//결제정보
    			.priceInfo(refundInfoDto)									//환불정보
    			.refundBankList(refundBankList) 							//환불은행 목록 계좌
    			.paymentTypeList(payUseListDto.getPaymentType())			//결제방법 정보 리스트
    			.cardList(payUseListDto.getCardList())						//카드 정보 리스트
    			.installmentPeriodList(payUseListDto.getInstallmentPeriod())//할부기간
    			.goodsCouponList(goodsCouponList)							//상품쿠폰 정보
    			.cartCouponList(cartCouponList)								//장바구니쿠폰 정보
    			.accountInfo(accountInfoDto) 								//환불계좌정보 조회
				.deliveryInfoList(claimUtilRequestService.getDelivery(goodsList, goodSearchList))	//출고처정보 조회
				// 반품일경우에만 사용
				.attcInfoList(attcInfoList)									//첨부파일 조회
    			.sendShippingInfo(sendShippingInfo) 						//보내는배송지 조회
    			.receShippingInfoList(receShippingInfoList) 				//받는배송지 조회

				.build();
        return ApiResult.success(orderClaimViewResponseDto);
    }

	/**
	 * @Desc 녹즙 클레임 신청 조회
	 * @return
	 */
	@Override
	public ApiResult<?> getOrderGreenJuiceClaimView(OrderClaimViewRequestDto orderClaimViewRequestDto) throws Exception {

		log.debug("getOrderClaimInfo requestParam === <{}>", orderClaimViewRequestDto.toString());
		long odOrderId = orderClaimViewRequestDto.getOdOrderId();

		// 녹즙 배송 완료 여부는 기본 N
		String deliveryYn = OrderClaimEnums.AllTypeYn.ALL_TYPE_N.getCode();

		// 녹즙 클레임 구분이 반품일 경우 배송완료 여부는 Y
		if(OrderClaimEnums.GreenJuiceClaimType.CLAIM_TYPE_RETURN.getCode().equals(orderClaimViewRequestDto.getClaimType())) {
			deliveryYn = OrderClaimEnums.AllTypeYn.ALL_TYPE_Y.getCode();
		}
		orderClaimViewRequestDto.setDeliveryYn(deliveryYn);
		orderClaimViewRequestDto.setDbStatusCheckYn(OrderClaimEnums.AllTypeYn.ALL_TYPE_N.getCode());

		//상품정보
		List<OrderClaimGoodsInfoDto> goodsList = claimRequestService.getClaimGreenJuiceGoodsInfoList(orderClaimViewRequestDto);

		// 증정품 리스트
		List<OrderClaimGoodsInfoDto> giftGoodsList = claimUtilRequestService.getClaimGiftGoodsList(goodsList);

		// 상품 리스트 변환 작업
		List<OrderClaimGoodsListDto> goodsResultList = claimUtilRequestService.getClaimGoodsList(goodsList,orderClaimViewRequestDto);

		//상품쿠폰
		OrderClaimViewRequestDto goodsCouponDto = new OrderClaimViewRequestDto();
		BeanUtils.copyProperties(orderClaimViewRequestDto, goodsCouponDto);
		goodsCouponDto.setCouponTp(OrderClaimEnums.ClaimCouponTp.COUPON_GOODS.getCode());
		List<OrderClaimCouponInfoDto> goodsCouponList = claimRequestService.getCouponInfoList(goodsCouponDto);

		//장바구니쿠폰
		OrderClaimViewRequestDto cartCouponDto = new OrderClaimViewRequestDto();
		BeanUtils.copyProperties(orderClaimViewRequestDto, cartCouponDto);
		cartCouponDto.setCouponTp(OrderClaimEnums.ClaimCouponTp.COUPON_CART.getCode());
		List<OrderClaimCouponInfoDto> cartCouponList = claimRequestService.getCouponInfoList(cartCouponDto);

		//결제정보 구하기
		OrderClaimPaymentInfoDto paymentInfoDto = claimRequestService.getOrderClaimPaymentInfo(odOrderId);

		//녹즙 주문건 전체 상품 조회
		List<OrderClaimTargetGoodsListDto> targetGreenJuiceGoodsList = claimRequestService.getOrderClaimTargetGreenJuiceGoodsList(orderClaimViewRequestDto);
		OrderClaimPriceInfoDto refundInfoDto = claimUtilPriceService.getRefundInfo(orderClaimViewRequestDto, goodsList, paymentInfoDto, targetGreenJuiceGoodsList);

		//환불계좌 조회
		//OrderClaimAccountInfoDto accountInfoDto = claimRequestService.getOrderClaimAccountInfo(orderClaimViewRequestDto);
		OrderAccountDto refundBankResult = claimUtilRefundService.getRefundBank(orderClaimViewRequestDto.getOdOrderId());
		OrderClaimAccountInfoDto accountInfoDto = new OrderClaimAccountInfoDto();
		if(ObjectUtils.isNotEmpty(refundBankResult)){
			accountInfoDto.setBankCd(refundBankResult.getBankCode());
			accountInfoDto.setAccountHolder(refundBankResult.getHolderName());
			accountInfoDto.setAccountNumber(refundBankResult.getAccountNumber());
		}

		//환불은행 조회
		CommonGetCodeListResponseDto commonCodeList = (CommonGetCodeListResponseDto) userBuyerBiz.getRefundBankInfo().getData();
		List<CodeInfoVo> refundBankList = commonCodeList.getRows();

		//신용카드 회사 및 개월수
		PayUseListDto payUseListDto = policyPaymentbiz.getPayUseList();

		OrderClaimViewResponseDto orderClaimViewResponseDto = OrderClaimViewResponseDto.builder()
				.orderMasterInfo(claimRequestService.getOrderMasterInfo(odOrderId))				//주문마스터정보
				.orderGoodList(goodsList)									//상품 목록
				.goodList(goodsResultList)
				.giftList(giftGoodsList)
				.paymentInfo(paymentInfoDto)								//결제정보
				.priceInfo(refundInfoDto)									//환불정보
				.refundBankList(refundBankList) 							//환불은행 목록 계좌
				.paymentTypeList(payUseListDto.getPaymentType())			//결제방법 정보 리스트
				.cardList(payUseListDto.getCardList())						//카드 정보 리스트
				.installmentPeriodList(payUseListDto.getInstallmentPeriod())//할부기간
				.goodsCouponList(goodsCouponList)							//상품쿠폰 정보
				.cartCouponList(cartCouponList)								//장바구니쿠폰 정보
				.accountInfo(accountInfoDto) 								//환불계좌정보 조회
				.build();
		return ApiResult.success(orderClaimViewResponseDto);
	}

	/**
	 * @Desc 녹즙 클레임 반품 스케쥴 배송정보 조회
	 * @return
	 */
	@Override
	public ApiResult<?> getOrderGreenJuiceClaimReturnsScheduleView(OrderClaimViewRequestDto orderClaimViewRequestDto) throws Exception {

		log.debug("getOrderGreenJuiceClaimReturnsScheduleView requestParam === <{}>", orderClaimViewRequestDto.toString());
		orderClaimViewRequestDto.setDeliveryYn(OrderClaimEnums.AllTypeYn.ALL_TYPE_Y.getCode());

		List<OrderClaimGoodsInfoDto> greenjuiceReturnsList = claimRequestService.getOrderGreenJuiceClaimReturnsScheduleView(orderClaimViewRequestDto);

		OrderClaimViewResponseDto orderClaimViewResponseDto = OrderClaimViewResponseDto.builder()
				.orderMasterInfo(claimRequestService.getOrderMasterInfo(orderClaimViewRequestDto.getOdOrderId()))				//주문마스터정보
				.orderGoodList(greenjuiceReturnsList)
				.build();
		return ApiResult.success(orderClaimViewResponseDto);
	}

	/**
	 * @Desc 녹즙 클레임 재배송 스케쥴 도착예정일 정보 조회
	 * @return
	 */
	@Override
	public ApiResult<?> getOrderGreenJuiceClaimExchangeScheduleView(OrderClaimViewRequestDto orderClaimViewRequestDto) throws Exception {

		log.debug("getOrderGreenJuiceClaimExchangeScheduleView requestParam === <{}>", orderClaimViewRequestDto.toString());

		return ApiResult.success(claimRequestService.getOrderGreenJuiceClaimExchangeScheduleView(orderClaimViewRequestDto));
	}

	/**
	 * 주문클레임 재배송 상품 금액 정보 조회
	 * @param orderClaimReShippingPaymentInfoRequestDto
	 * @return
	 * @throws Exception
	 */
	@Override
	public ApiResult<?> getOrderClaimReShippingGoodsPriceInfo(OrderClaimReShippingPaymentInfoRequestDto orderClaimReShippingPaymentInfoRequestDto) throws Exception {

		log.debug("주문 클레임 재배송 상품정보 DTO :: <{}>", orderClaimReShippingPaymentInfoRequestDto.toString());
		OrderClaimReShippingPaymentInfoDto orderPaymentInfo = claimRequestService.getOrderClaimGoodsPaymentInfo(orderClaimReShippingPaymentInfoRequestDto.getOdOrderId(),
																												orderClaimReShippingPaymentInfoRequestDto.getOdOrderDetlId(),
																												orderClaimReShippingPaymentInfoRequestDto.getClaimCnt());
		OrderClaimReShippingPaymentInfoResponseDto responseDto = new OrderClaimReShippingPaymentInfoResponseDto();
		List<OrderClaimReShippingGoodsPaymentInfoDto> goodsInfoList = orderClaimReShippingPaymentInfoRequestDto.getGoodsInfoList();

		List<OrderClaimViewDeliveryDto> deliveryInfoList = new ArrayList<>();
		Map<Long, List<List<ArrivalScheduledDateDto>>> urWarehouseIdMap = new HashMap<>();

		// 원주문의 상품배송타입, 주문배송유형 조회
		OrderClaimGoodsInfoDto orderClaimGoodsInfoDto = claimRequestService.getOdOrderDetlGoodsDeliveryType(orderClaimReShippingPaymentInfoRequestDto.getOdOrderDetlId());
		String goodsDeliveryType = orderClaimGoodsInfoDto.getGoodsDeliveryType();	 		// 상품배송타입
		String orderStatusDeliveryType = orderClaimGoodsInfoDto.getOrderStatusDeliTp();		// 주문배송유형

		log.debug("-----------------------출고 예정일 조회 START----------------------------");
		for(int i=0; i<goodsInfoList.size(); i++) {

			OrderClaimReShippingGoodsPaymentInfoDto goodsInfo = goodsInfoList.get(i);

			boolean isDawnDelivery = false;		// 새벽배송여부

			// 출고처별 새벽배송가능여부 조회
			UrWarehouseVo warehouseVo = storeWarehouseBiz.getWarehouse(goodsInfo.getUrWarehouseId());

			// 원주문이 새벽배송일 경우
			if(GoodsEnums.GoodsDeliveryType.DAWN.getCode().equals(goodsDeliveryType)){

				// 출고처가 새벽배송이 가능한경우
				if("Y".equals(warehouseVo.getDawnDeliveryYn())){
					isDawnDelivery = true;

					// 원주문이 새벽배송인데 출고처가 새벽배송이 불가능한 경우 -> 상품배송타입,주문배송유형 일반으로 변경
				} else {
					goodsDeliveryType = GoodsEnums.GoodsDeliveryType.NORMAL.getCode();
					orderStatusDeliveryType = OrderEnums.OrderStatusDetailType.NORMAL.getCode();
				}
			}

			List<ArrivalScheduledDateDto> orderIfDtoList = goodsGoodsBiz.getArrivalScheduledDateDtoList(
																										goodsInfo.getUrWarehouseId(),	//출고처ID(출고처PK)
																										goodsInfo.getIlGoodsId(),		//상품ID(상품PK)
																										isDawnDelivery,					//새벽배송여부 (true/false)
																										goodsInfo.getClaimCnt(),		//주문수량
																										null);			//일일 배송주기코드
			DeliveryInfoDto deliveryInfo = DeliveryInfoDto.builder()
															.urWarehouseId(goodsInfo.getUrWarehouseId())
															.ilGoodsId(goodsInfo.getIlGoodsId())
															.arrivalScheduleList(orderIfDtoList)
															.build();

			OrderClaimViewDeliveryDto deliveryItem = OrderClaimViewDeliveryDto.builder()
																				.urWarehouseId(goodsInfo.getUrWarehouseId())
																				.urWarehouseNm(goodsInfo.getWarehouseNm())
																				.deliveryInfoList(deliveryInfo)
																				.goodsDeliveryType(goodsDeliveryType)
																				.orderStatusDeliTp(orderStatusDeliveryType)
																				.build();

			List<List<ArrivalScheduledDateDto>> orderIfList = new ArrayList<>();
			if(!urWarehouseIdMap.containsKey(goodsInfo.getUrWarehouseId())) {
				orderIfList.add(orderIfDtoList);
				deliveryInfoList.add(deliveryItem);
			}
			else {
				orderIfList = urWarehouseIdMap.get(goodsInfo.getUrWarehouseId());
				orderIfList.add(orderIfDtoList);
			}
			urWarehouseIdMap.put(goodsInfo.getUrWarehouseId(), orderIfList);
		}
		log.debug("-----------------------출고 예정일 조회 END----------------------------");

		List<OrderClaimReShippingGoodsPaymentInfoDto> selectGoodsList = goodsInfoList.stream().filter(x -> x.getOdOrderDetlId() == orderClaimReShippingPaymentInfoRequestDto.getOdOrderDetlId()).collect(Collectors.toList());

		// 상품금액 / 전체금액 * 100 = 상품의 금액 비율
		// 외부몰 주문일 경우 정상가(recommendedPrice) 기준
		if(SystemEnums.AgentType.OUTMALL.getCode().equals(orderPaymentInfo.getAgentTypeCd())) {
			// 정상가 기준 Sort
			goodsInfoList.sort(new Comparator<OrderClaimReShippingGoodsPaymentInfoDto>() {
				@Override
				public int compare(OrderClaimReShippingGoodsPaymentInfoDto dto1, OrderClaimReShippingGoodsPaymentInfoDto dto2) {
					return (dto1.getRecommendedPrice() <= dto2.getRecommendedPrice() ? -1 : 1);
				}
			});
		}
		// 통합몰 주문일 경우 판매가(salePrice) 기준
		else {
			// 판매가 기준 Sort
			goodsInfoList.sort(new Comparator<OrderClaimReShippingGoodsPaymentInfoDto>() {
				@Override
				public int compare(OrderClaimReShippingGoodsPaymentInfoDto dto1, OrderClaimReShippingGoodsPaymentInfoDto dto2) {
					return (dto1.getSalePrice() <= dto2.getSalePrice() ? -1 : 1);
				}
			});
		}

		// 총 판매가
		int totalRecommendedPrice = selectGoodsList.stream().mapToInt(x -> x.getRecommendedPrice()).sum();
		// 총 판매가
		int totalSalePrice = selectGoodsList.stream().mapToInt(x -> x.getSalePrice()).sum();
		// 상품 할인가
		int discountPrice = orderPaymentInfo.getDiscountPrice();
		// 주문금액
		int orderPrice = orderPaymentInfo.getOrderPrice();
		// 상품 결제금액
		int paidPrice = orderPaymentInfo.getPaidPrice();
		// 상품 정상가
		int recommendedPrice = orderPaymentInfo.getRecommendedPrice();
		// 상품 판매가
		int salePrice = orderPaymentInfo.getSalePrice();
		// 비율 총 합
		int rateSum = 0;

		log.debug("-----------------------주문금액 / 결제금액 비율 계산 START----------------------------");
		for(int i=0; i<selectGoodsList.size(); i++) {

			OrderClaimReShippingGoodsPaymentInfoDto goodsInfo = selectGoodsList.get(i);
			// 마지막일 경우
			if(i == (selectGoodsList.size() - 1)) {
				goodsInfo.setDiscountPrice(discountPrice);
				goodsInfo.setOrderPrice(orderPrice);
				goodsInfo.setPaidPrice(orderPrice - discountPrice);
				goodsInfo.setRecommendedPrice(recommendedPrice);
				goodsInfo.setSalePrice(salePrice);
				goodsInfo.setDiscountRate(100 - rateSum);
				break;
			}
			int rate = 0;
			// 외부몰 총 판매가 대비 현재 상품의 비율
			if(SystemEnums.AgentType.OUTMALL.getCode().equals(orderPaymentInfo.getAgentTypeCd())) {
				rate = (int) ((goodsInfo.getSalePrice() / (double) totalSalePrice) * 100);
			}
			// 통합몰 총 판매가 대비 현재 상품의 비율
			else {
				rate = (int) ((goodsInfo.getRecommendedPrice() / (double) totalRecommendedPrice) * 100);
			}
			log.debug("판매가 :: <{}>, 총판매금액 :: <{}>", goodsInfo.getSalePrice(), totalSalePrice);
			log.debug("정상가 :: <{}>, 총정상가금액 :: <{}>", goodsInfo.getRecommendedPrice(), totalRecommendedPrice);
			log.debug("goodsRate <{}>, <{}>", goodsInfo.getIlGoodsId(), rate);
			int rateDiscountPrice = (int) (orderPaymentInfo.getDiscountPrice() * (rate / (double) 100));
			int rateOrderPrice = (int) (orderPaymentInfo.getOrderPrice() * (rate / (double) 100));
			//int ratePaidPrice = (int) (orderPaymentInfo.getPaidPrice() * (rate / (double) 100));
			int rateRecommendedPrice = (int) (orderPaymentInfo.getRecommendedPrice() * (rate / (double) 100));
			int rateSalePrice = (int) (orderPaymentInfo.getSalePrice() * (rate / (double) 100));
			log.debug("rateDiscountPrice <{}>, rateOrderPrice <{}>, ratePaidPrice <{}>", rateDiscountPrice, rateOrderPrice, (rateOrderPrice - rateDiscountPrice));
			goodsInfo.setDiscountPrice(rateDiscountPrice);
			goodsInfo.setOrderPrice(rateOrderPrice);
			goodsInfo.setPaidPrice(rateOrderPrice - rateDiscountPrice);
			goodsInfo.setRecommendedPrice(rateRecommendedPrice);
			goodsInfo.setSalePrice(rateSalePrice);
			goodsInfo.setDiscountRate(rate);
			discountPrice -= rateDiscountPrice;
			orderPrice -= rateOrderPrice;
			//paidPrice -= ratePaidPrice;
			recommendedPrice -= rateRecommendedPrice;
			salePrice -= rateSalePrice;
			rateSum += rate;
		}
		log.debug("-----------------------주문금액 / 결제금액 비율 계산 END----------------------------");

		log.debug("-----------------------출고예정일 교집합 조회 START----------------------------");
		if(!urWarehouseIdMap.isEmpty()) {
			Map<Long, List<LocalDate>> scheduleList = new HashMap<>();
			for (long urWarehouseId : urWarehouseIdMap.keySet()) {
				scheduleList.put(urWarehouseId, goodsGoodsBiz.intersectionArrivalScheduledDateListByDto(urWarehouseIdMap.get(urWarehouseId)));
			}
			log.debug("scheduleList ::: <{}>", scheduleList.toString());

			log.debug("deliveryInfoList :: <{}>", deliveryInfoList.toString());

			for (OrderClaimReShippingGoodsPaymentInfoDto goodsInfo : goodsInfoList) {
				for (OrderClaimViewDeliveryDto deliveryInfo : deliveryInfoList) {
					if (deliveryInfo.getUrWarehouseId() == goodsInfo.getUrWarehouseId()) {
						List<ArrivalScheduledDateDto> arriveList = goodsGoodsBiz.intersectionArrivalScheduledDateDtoList(deliveryInfo.getDeliveryInfoList().getArrivalScheduleList(), scheduleList.get(deliveryInfo.getUrWarehouseId()));
						deliveryInfo.getDeliveryInfoList().setArrivalScheduleList(arriveList);
						goodsInfo.setOrderStatusDeliTp(deliveryInfo.getOrderStatusDeliTp());
						goodsInfo.setGoodsDeliveryType(deliveryInfo.getGoodsDeliveryType());
					}
				}
			}
		}
		log.debug("-----------------------출고예정일 교집합 조회 END----------------------------");

		responseDto.setTotal(goodsInfoList.size());
		responseDto.setGoodsInfoList(goodsInfoList);
		responseDto.setDeliveryInfoList(deliveryInfoList);
		responseDto.setOrderPaymentInfo(orderPaymentInfo);

		return ApiResult.success(responseDto);
	}

	/**
	 * 주문클레임 마스터 조회
	 * @param orderClaimViewRequestDto
	 * @return OrderClaimMasterInfoDto
	 */
	@Override
	public OrderClaimMasterInfoDto getOrderClaimMasterInfo(OrderClaimViewRequestDto orderClaimViewRequestDto) {
		return claimRequestService.getOrderClaimMasterInfo(orderClaimViewRequestDto);
	}

	/**
	 * 클레임정보 클레임번호에 의한 BOS클레임사유 조회
	 * @param orderClaimViewRequestDto
	 * @return OrderClaimMasterInfoDto
	 */
	@Override
	public ApiResult<?> getOrderClaimBosClaimReasonView(OrderClaimViewRequestDto orderClaimViewRequestDto) throws Exception {
		log.debug("getOrderClaimBosClaimReasonView requestParam === <{}>", orderClaimViewRequestDto.toString());
		long odOrderId = orderClaimViewRequestDto.getOdOrderId();

		String claimStatusTp = orderClaimViewRequestDto.getClaimStatusTp();

		// MALL 에서 접근 시 본인 주문건이 아닐 경우 오류 처리
		if (orderClaimViewRequestDto.getFrontTp() > 0) {
			int selfOrderCnt = claimRequestService.getSelfOrderCnt(orderClaimViewRequestDto);
			if (selfOrderCnt < 1) {
				return ApiResult.result(OrderEnums.OrderErrMsg.VALUE_EMPTY);
			}
		}

		//상품정보
		List<OrderClaimGoodsInfoDto> goodsList = claimRequestService.getBosCalimReasonGoodsInfoList(orderClaimViewRequestDto);

		OrderClaimViewResponseDto orderClaimViewResponseDto = OrderClaimViewResponseDto.builder()
				.orderMasterInfo(claimRequestService.getOrderMasterInfo(odOrderId))    //주문마스터정보
				.orderGoodList(goodsList)                                            //상품 목록
				.build();
		return ApiResult.success(orderClaimViewResponseDto);
	}
	/**
	 * 추가배송비 정보 조회
	 * @param orderClaimRegisterRequestDto
	 * @return
	 * @throws Exception
	 */
	@Override
	public MallOrderClaimAddPaymentResult getOrderClaimAddShippingPriceInfo(OrderClaimRegisterRequestDto orderClaimRegisterRequestDto) throws Exception {
		return claimRequestService.getOrderClaimAddShippingPrice(orderClaimRegisterRequestDto);
	}

	/**
	 * 추가 배송비 결제 금액 조회
	 * @param mallOrderClaimAddPaymentRequest
	 * @return
	 */
	@Override
	public ApiResult<?> getOrderClaimAddShippingPrice(MallOrderClaimAddPaymentRequest mallOrderClaimAddPaymentRequest) throws Exception {

		MallOrderClaimAddPaymentResponseDto mallOrderClaimAddPaymentResponseDto = new MallOrderClaimAddPaymentResponseDto();

		OrderClaimRegisterRequestDto orderClaimRegisterRequestDto = claimRequestService.setOrderClaimRegisterRequest(mallOrderClaimAddPaymentRequest);

		MallOrderClaimAddPaymentValidResult validResult = claimRequestService.validOrderClaimShippingPrice(orderClaimRegisterRequestDto);

		// 유효성체크 결과가 성공이 아닐 경우 return
		if(!OrderClaimEnums.AddPaymentShippingPriceError.SUCCESS.getCode().equals(validResult.getValidResult().getCode())) {
			return ApiResult.result(validResult.getValidResult());
		}

		MallOrderClaimAddPaymentResult mallOrderClaimAddPaymentResult = validResult.getAddShippingInfo();

		mallOrderClaimAddPaymentResponseDto.setOdOrderId(mallOrderClaimAddPaymentResult.getOdOrderId());	// 주문PK
		mallOrderClaimAddPaymentResponseDto.setOdClaimId(mallOrderClaimAddPaymentResult.getOdClaimId());	// 주문클레임PK
		mallOrderClaimAddPaymentResponseDto.setAddPaymentShippingPrice(mallOrderClaimAddPaymentResult.getAddPaymentShippingPrice());	// 추가배송비
		mallOrderClaimAddPaymentResponseDto.setGoodsNm(mallOrderClaimAddPaymentResult.getGoodsNm());		// 상품명

		log.debug("----------------- 3. 추가 결제 정보 Set -----------------");
		String urUserId = orderClaimRegisterRequestDto.getUrUserId();
		HashMap<String, String> userPayment = new HashMap<>();
		List<String> psClauseGrpCdList = new ArrayList<>();
		GetClauseRequestDto clauseReqDto = new GetClauseRequestDto();
		List<GetLatestJoinClauseListResultVo> clauseList = new ArrayList<>();
		if (StringUtils.isNotEmpty(urUserId)) {
			// 회원 사용 카드 정보 조회
			userPayment = userBuyerBiz.getUserPaymentInfo(urUserId);

			// 구매약관 정보 조회
			psClauseGrpCdList.add(ShoppingEnums.ClauseGroupCode.PURCHASE_TERMS.getCode());
		}
		else {
			// 비회원구매일 경우 이용약관 추가
			psClauseGrpCdList.add(ShoppingEnums.ClauseGroupCode.NON_MEMBER_PRIVACY_POLICY.getCode());
		}

		// 약관
		clauseReqDto.setPsClauseGrpCdList(psClauseGrpCdList);
		ApiResult<?> clauseResult = policyClauseBiz.getPurchaseTermsClauseList(clauseReqDto);
		clauseList = (List<GetLatestJoinClauseListResultVo>) clauseResult.getData();
		// 약관리스트
		mallOrderClaimAddPaymentResponseDto.setClause(clauseList);

		// 결제 및 카드 정보 조회
		PayUseListDto payUseListDto = policyPaymentbiz.getPayUseList();
		// 결제 및 카드 정보
		mallOrderClaimAddPaymentResponseDto.setPaymentType(payUseListDto.getPaymentType());
		mallOrderClaimAddPaymentResponseDto.setCardList(payUseListDto.getCardList());
		mallOrderClaimAddPaymentResponseDto.setInstallmentPeriod(payUseListDto.getInstallmentPeriod());
		mallOrderClaimAddPaymentResponseDto.setCartBenefit(payUseListDto.getCartBenefit());
		// 무통장입금결제가능여부
		mallOrderClaimAddPaymentResponseDto.setVirtualAccountYn(OrderClaimEnums.AllTypeYn.ALL_TYPE_N.getCode());
		// 회원 사용 결제방법 정보 Set
		mallOrderClaimAddPaymentResponseDto.setUserPayment(userPayment);

		log.debug("----------------- 추가 배송비 결제 금액 조회 END -----------------");
		return ApiResult.success(mallOrderClaimAddPaymentResponseDto);
	}

	/**
	 * 추가 배송비 결제 요청
	 * @param mallOrderClaimAddPaymentRequest
	 * @return
	 * @throws Exception
	 */
	public ApiResult<?> applyOrderClaimAddShippingPrice(MallOrderClaimAddPaymentRequest mallOrderClaimAddPaymentRequest) throws Exception {

		OrderClaimRegisterRequestDto orderClaimRegisterRequestDto = claimRequestService.setOrderClaimRegisterRequest(mallOrderClaimAddPaymentRequest);

		MallOrderClaimAddPaymentValidResult validResult = claimRequestService.validOrderClaimShippingPrice(orderClaimRegisterRequestDto);

		// 유효성체크 결과가 성공이 아닐 경우 return
		if(!OrderClaimEnums.AddPaymentShippingPriceError.SUCCESS.getCode().equals(validResult.getValidResult().getCode())) {
			return ApiResult.result(validResult.getValidResult());
		}

		MallOrderClaimAddPaymentResult mallOrderClaimAddPaymentResult = validResult.getAddShippingInfo();

		log.debug("----------------- 3. 추가 결제 스크립트 PG 설정 -----------------");
		orderClaimRegisterRequestDto.setOdid(mallOrderClaimAddPaymentResult.getOdid());								// 주문번호
		orderClaimRegisterRequestDto.setGoodsNm(mallOrderClaimAddPaymentResult.getGoodsNm());						// 상품명
		orderClaimRegisterRequestDto.setBuyerNm(mallOrderClaimAddPaymentResult.getBuyerNm());						// 주문자명
		orderClaimRegisterRequestDto.setBuyerHp(mallOrderClaimAddPaymentResult.getBuyerHp());						// 주문자휴대폰
		orderClaimRegisterRequestDto.setBuyerMail(mallOrderClaimAddPaymentResult.getBuyerMail());					// 주문자메일
		orderClaimRegisterRequestDto.setRefundPrice(mallOrderClaimAddPaymentResult.getAddPaymentShippingPrice());	// 추가결제금액
		orderClaimRegisterRequestDto.setFrontTp(OrderClaimEnums.OrderClaimFrontTpCd.FRONT_TP_FRONT.getCodeValue());	// 프론트구분

		// 추가 결제 데이터 테이블 내 JSON 형태 저장 처리
		String reqJson = new Gson().toJson(orderClaimRegisterRequestDto);
		log.debug("reqJson :: <{}>", reqJson);
		OdAddPaymentReqInfo odAddPaymentReqInfo = OdAddPaymentReqInfo.builder()
				.reqJsonInfo(reqJson)
				.build();
		claimProcessService.addOdAddPaymentReqInfo(odAddPaymentReqInfo);
		orderClaimRegisterRequestDto.setOdAddPaymentReqInfoId(odAddPaymentReqInfo.getOdAddPaymentReqInfoId());

		BasicDataResponseDto resDto = claimUtilRefundService.addPayment(orderClaimRegisterRequestDto, true);

		return ApiResult.success(resDto);
	}

	/**
	 * 쇼핑몰 사유 목록 조회
	 * @param policyClaimMallRequestDto
	 * @return
	 * @throws Exception
	 */
	@Override
	public ApiResult<?> getPolicyClaimMallListForMall(PolicyClaimMallRequestDto policyClaimMallRequestDto) throws Exception{
		policyClaimMallRequestDto.setSearchUseYn("Y");
		PolicyClaimMallResponseDto responseDto = (PolicyClaimMallResponseDto)policyClaimBiz.getPolicyClaimMallList(policyClaimMallRequestDto).getData();
		return ApiResult.success(responseDto.getRows());
	}
}
