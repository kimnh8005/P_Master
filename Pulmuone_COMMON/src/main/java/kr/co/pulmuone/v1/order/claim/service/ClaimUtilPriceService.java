package kr.co.pulmuone.v1.order.claim.service;

import kr.co.pulmuone.v1.comm.enums.*;
import kr.co.pulmuone.v1.comm.mapper.order.claim.ClaimRequestMapper;
import kr.co.pulmuone.v1.comm.mapper.order.claim.OrderClaimMapper;
import kr.co.pulmuone.v1.comm.util.StringUtil;
import kr.co.pulmuone.v1.goods.goods.dto.ShippingPriceResponseDto;
import kr.co.pulmuone.v1.goods.goods.dto.vo.ShippingAreaVo;
import kr.co.pulmuone.v1.goods.goods.dto.vo.ShippingDataResultVo;
import kr.co.pulmuone.v1.goods.goods.service.GoodsShippingTemplateBiz;
import kr.co.pulmuone.v1.order.claim.dto.*;
import kr.co.pulmuone.v1.order.claim.util.ClaimPriceUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ClaimUtilPriceService {

	@Autowired
	private ClaimUtilRefundService claimUtilRefundService;

	@Autowired
	private GoodsShippingTemplateBiz goodsShippingTemplateBiz;

	@Autowired
	private ClaimRequestMapper claimRequestMapper;

	@Autowired
	private OrderClaimMapper orderClaimMapper;

	private ClaimPriceUtil claimPriceUtil = new ClaimPriceUtil();

	/**
	 * 환불정보 조회
	 * @return
	 */
	public OrderClaimPriceInfoDto getRefundInfo(OrderClaimViewRequestDto reqDto, List<OrderClaimGoodsInfoDto> goodsList, OrderClaimPaymentInfoDto paymentInfoDto, List<OrderClaimTargetGoodsListDto> targetGoodsList) throws Exception {

		log.debug(" 클레임 ID ::: <{}>", reqDto.getOdClaimId());

		// 요청 상품 목록 여부
		boolean isTargetGoodsList = CollectionUtils.isNotEmpty(reqDto.getGoodSearchList());
		// 배송비 재계산여부 Y로 설정
		reqDto.setReAccountShippingPriceYn(OrderClaimEnums.AllTypeYn.ALL_TYPE_Y.getCode());

		// 프론트에서 들어온 요청일 경우
		// 조회 구분이 부분취소, 선택 목록이 존재 하지 않을 경우 환불 정보 0값 세팅
		if(
			StringUtil.nvl(String.valueOf(reqDto.getFrontTp())).equals(ClaimEnums.ClaimFrontTp.FRONT.getCode())						&&	// 프론트 요청이고
			StringUtil.nvl(String.valueOf(reqDto.getGoodsChange())).equals(ClaimEnums.ClaimGoodsChangeType.PART_CANCEL.getCode())	&&	// 수량변경취소(부분취소) 이고
			!isTargetGoodsList					// 요청 상묵 목록이 존재하지 않을 경우
		) {
			OrderClaimPriceInfoDto orderClaimPriceInfo = new OrderClaimPriceInfoDto();
			orderClaimPriceInfo.setAddPaymentList(new ArrayList<>());
			return orderClaimPriceInfo;
		}

		// 요청 클레임 상태가 재배송 일 경우 PASS
		if(OrderEnums.OrderStatus.EXCHANGE_COMPLETE.getCode().equals(reqDto.getPutOrderStatusCd()) ||
			OrderEnums.OrderStatus.EXCHANGE_COMPLETE.getCode().equals(reqDto.getClaimStatusCd())) {
			OrderClaimPriceInfoDto orderClaimPriceInfo = new OrderClaimPriceInfoDto();
			orderClaimPriceInfo.setAddPaymentList(new ArrayList<>());
			return orderClaimPriceInfo;
		}

		boolean isReturns = OrderClaimEnums.ClaimStatusTp.RETURN.getCode().equals(reqDto.getClaimStatusTp());

		// 매장픽업여부
		boolean isShopPicup = goodsList.stream().filter(vo -> OrderEnums.OrderStatusDetailType.SHOP_PICKUP.getCode().equals(vo.getOrderStatusDeliTp())).count() > 0 ? false : true;

		OrderClaimPriceInfoDto refundInfoDto = null;
		if (reqDto.getOdClaimId() > 0) {

			// 클레임 신청 정보 얻기
			refundInfoDto = claimRequestMapper.getClaimRefundInfo(reqDto.getOdClaimId());
			reqDto.setAddPaymentCnt(refundInfoDto.getAddPaymentCnt());
			// 전체 취소일 경우 - 환불 신청 시의 배송비 정보 Set
			if (refundInfoDto.getClaimDetlCnt() == goodsList.size()) {
				// 배송비 재계산여부 N으로 설정
				reqDto.setReAccountShippingPriceYn(OrderClaimEnums.AllTypeYn.ALL_TYPE_N.getCode());
				// 반품이고, 주문상태가 반품승인이 아니고, 귀책구분 또는 회수여부가 다를 경우 배송비 재계산처리
				if (OrderClaimEnums.ClaimStatusTp.RETURN.getCode().equals(reqDto.getClaimStatusTp()) &&
					!OrderEnums.OrderStatus.RETURN_ING.getCode().equals(reqDto.getOrderStatusCd()) &&
					(!reqDto.getTargetTp().equals(refundInfoDto.getTargetTp()) ||
					!reqDto.getReturnsYn().equals(refundInfoDto.getReturnsYn()))
					) {
					reqDto.setReAccountShippingPriceYn(OrderClaimEnums.AllTypeYn.ALL_TYPE_Y.getCode());
				}
				reqDto.setRefundReqShippingPrice(refundInfoDto.getShippingPrice());   			 // 신청 시점의 계산 배송비
				reqDto.setRefundReqOrderShippingPrice(refundInfoDto.getOrderShippingPrice());    // 신청 시점의 환불 요청 배송비
			}

			OrderClaimPriceInfoDto addShippingPriceInfo = this.getCancelRefundInfo(reqDto, goodsList, paymentInfoDto, targetGoodsList, isReturns, isShopPicup, refundInfoDto.getTargetTp(), refundInfoDto.getReturnsYn(), true);
			addShippingPriceInfo.setDirectPaymentYn(refundInfoDto.getDirectPaymentYn());
			addShippingPriceInfo.setAddPaymentTp(refundInfoDto.getAddPaymentTp());
			addShippingPriceInfo.setAddPaymentCnt(refundInfoDto.getAddPaymentCnt());
			refundInfoDto.setTaxableOrderShippingPrice(addShippingPriceInfo.getTaxableOrderShippingPrice());
			refundInfoDto.setTaxablePrice(addShippingPriceInfo.getTaxablePrice());
			refundInfoDto.setNonTaxablePrice(addShippingPriceInfo.getNonTaxablePrice());

			// 취소일 경우
			if (OrderClaimEnums.ClaimStatusTp.CANCEL.getCode().equals(reqDto.getClaimStatusTp()) &&
				!reqDto.getTargetTp().equals(refundInfoDto.getTargetTp())
			) {
				return addShippingPriceInfo;
			}

			// 반품일 경우
			if (OrderClaimEnums.ClaimStatusTp.RETURN.getCode().equals(reqDto.getClaimStatusTp()) &&
				!OrderEnums.OrderStatus.RETURN_ING.getCode().equals(reqDto.getOrderStatusCd()) &&
				(!reqDto.getTargetTp().equals(refundInfoDto.getTargetTp()) ||
				!reqDto.getReturnsYn().equals(refundInfoDto.getReturnsYn()))
				) {
				return addShippingPriceInfo;
			}

			// 클레임 신청 상세 수와 현재 선택한 상품의 수가 동일 할 경우 해당클레임 전체 취소
			if (refundInfoDto.getClaimDetlCnt() == goodsList.size()) {

				// 클레임 상세 추가배송비 목록 조회
				List<OrderClaimAddPaymentShippingPriceDto> addPaymentList = claimRequestMapper.getClaimRefundAddPaymentList(reqDto.getOdClaimId());

				refundInfoDto.setAddPaymentList(addPaymentList);

				// 클레임 상세 상품 금액정보 Set
				refundInfoDto.setGoodsPriceList(claimRequestMapper.getClaimDetlRefundList(reqDto.getOdClaimId()));
				// 배송비 쿠폰 정보 Set
				//this.setDeliveryCouponList(reqDto, goodsList, targetGoodsList, refundInfoDto);
				this.setDeliveryCouponList(reqDto, targetGoodsList, refundInfoDto);

				// 클레임 정보로 환불 정보 Set
				this.setTaxablePriceInfo(reqDto, goodsList, paymentInfoDto, refundInfoDto, false);
			}
			// 클레임 신청 상세 수와 현재 선택한 상품의 수가 동일 하지 않을 경우 각각 취소 이므로 재계산
			else {
				return addShippingPriceInfo;
			}
		}
		else {
			refundInfoDto = this.getCancelRefundInfo(reqDto, goodsList, paymentInfoDto, targetGoodsList, isReturns, isShopPicup, "", "", false);
		}

		log.debug("refundInfoDto :: <{}>", refundInfoDto.toString());

		return refundInfoDto;
	}

	/**
	 * 환불 취소 금액 계산
	 * @param reqDto
	 * @param goodsList
	 * @param paymentInfoDto
	 * @param targetGoodsList
	 * @param isReturns
	 * @param isShopPicup
	 * @param prevTargetTp
	 * @param prevReturnsYn
	 * @return
	 * @throws Exception
	 */
	public OrderClaimPriceInfoDto getCancelRefundInfo(OrderClaimViewRequestDto reqDto, List<OrderClaimGoodsInfoDto> goodsList, OrderClaimPaymentInfoDto paymentInfoDto,
													  List<OrderClaimTargetGoodsListDto> targetGoodsList, boolean isReturns, boolean isShopPicup, String prevTargetTp, String prevReturnsYn, boolean claimFlag) throws Exception {

		// 취소환불정보 구하기
		OrderClaimPriceInfoDto refundInfoDto = new OrderClaimPriceInfoDto();
		refundInfoDto.setAddPaymentShippingPriceYn("N");
		// 선택상품 목록
		List<OrderClaimSearchGoodsDto> goodSearchList = reqDto.getGoodSearchList();
		// 배송비 쿠폰 목록
		List<OrderClaimCouponInfoDto> deliveryCouponList = new ArrayList<>();
		// 추가결제 배송비 목록
		List<OrderClaimAddPaymentShippingPriceDto> addPaymentList = new ArrayList<>();
		// 상품 금액 정보 목록
		List<OrderClaimGoodsPriceInfoDto> claimGoodsPriceInfoList = new ArrayList<>();
		// 요청 상품 목록 여부
		boolean isTargetGoodsList = CollectionUtils.isNotEmpty(goodSearchList);
		Map<Long, Map<Long, List<OrderClaimTargetGoodsListDto>>> shippingTemplate = null;
		// 판매자귀책 여부
		boolean isSellerFlag = OrderClaimEnums.ClaimTargetTp.TARGET_SELLER.getCode().equals(reqDto.getTargetTp());
		// 외부몰 주문 여부
		boolean isOutmallFlag = false;
		// 추가 결제 여부
		boolean isAddPayment = reqDto.getAddPaymentCnt() > 0;
		// 주문유형 조회
		OrderClaimOutmallPaymentInfoDto agentTypeInfo = orderClaimMapper.getOrderAgentType(reqDto.getOdOrderId());
		// 주문유형이 외부몰이고, 결제 방법이 외부몰 결제 일 경우 판매자 귀책 고정
		if(SystemEnums.AgentType.OUTMALL.getCode().equals(agentTypeInfo.getAgentTypeCd()) &&
			OrderEnums.PaymentType.COLLECTION.getCode().equals(agentTypeInfo.getPayTp())) {
			isSellerFlag = true;
			isOutmallFlag = true;
		}
		// 반품회수 여부
		boolean isReturnFlag = ClaimEnums.ReturnsYn.RETURNS_YN_Y.getCode().equals(reqDto.getReturnsYn());

		log.debug("getCancelRefundInfo GoodSearchList 유무 ::: <{}>", isTargetGoodsList);

		// 요청 상품 목록이 있을 경우
		if(isTargetGoodsList) {
			// 주문배송비PK, 배송정책 별 그룹
			shippingTemplate = targetGoodsList.stream().collect(	Collectors.groupingBy(OrderClaimTargetGoodsListDto::getOdShippingPriceId, LinkedHashMap::new,
																	Collectors.groupingBy(OrderClaimTargetGoodsListDto::getShippingTmplId, LinkedHashMap::new, Collectors.toList())));
		}
		// 요청 상품 목록이 없을 경우
		else {
			// 전체 취소 상품이 아닌 건만
			// 주문배송비PK, 배송정책 별 그룹
			shippingTemplate = targetGoodsList.stream().filter(x -> OrderClaimEnums.AllTypeYn.ALL_TYPE_N.getCode().equals(x.getCancelYn()))
														.collect(	Collectors.groupingBy(OrderClaimTargetGoodsListDto::getOdShippingPriceId, LinkedHashMap::new,
																	Collectors.groupingBy(OrderClaimTargetGoodsListDto::getShippingTmplId, LinkedHashMap::new, Collectors.toList())));
		}

		log.debug("----- 그룹핑 데이터 EMPTY 유무 :: <{}>", shippingTemplate.isEmpty());

		// 그룹핑 결과가 존재하지 않을 경우
		if(shippingTemplate.isEmpty()) {
			return refundInfoDto;
		}

		int goodsPrice 					= 0;    // 상품금액
		int goodsSalePriceSum 			= 0;    // 상품판매가금액합계
		int goodsCouponPrice 			= 0;    // 상품쿠폰금액
		int cartCouponPrice 			= 0;    // 장바구니쿠폰금액
		int directPrice 				= 0;    // 상품쿠폰금액, 장바구니쿠폰금액을 제외한 나머지 할인금액
		int employeePrice 				= 0;    // 임직원 지원금
		int shippingPrice 				= 0;    // 배송비
		int orderShippingPrice 			= 0;    // 원주문배송비 결제 금액
		int addShippingPrice			= 0;    // 추가결제배송비
		int prevPaymentShippingPrice	= 0;    // 이전추가결제배송비
		int returnRefundShippingPrice	= 0;    // 반품 시 추가 배송비 환불 처리할 배송비
		int totTaxParice				= 0;	// 상품과세금액 합계
		int totNonTaxParice				= 0;	// 상품비과세금액 합계

		log.debug("================================================================================================");
		// 주문배송비PK로 Loop
		for(long odShippingPriceId : shippingTemplate.keySet()) {
			log.debug("----------- 주문배송비 PK :: <{}>", odShippingPriceId);
			// 배송비 정책별 상품 목록 얻기
			Map<Long, List<OrderClaimTargetGoodsListDto>> tmplList = shippingTemplate.get(odShippingPriceId);
			int orgShippingPrice		= 0;	// 원 배송비
			int amountShippingPrice		= Integer.MAX_VALUE;	// 재계산 배송비
			int templateOrderCnt		= 0;	// 배송비PK 주문수량 합계
			int templateCancelCnt		= 0;	// 배송비PK 주문취소수량 합계
			int templateClaimCnt		= 0;	// 배송비PK 주문클레임수량 합계
			long deliveryCouponIssueId 	= 0;    // 배송비쿠폰 발급 ID
			String pmCouponNm 			= "";   // 배송비쿠폰명
			String pmCouponBenefit 		= "";   // 배송비쿠폰혜택정보
			int shippingDiscountPrice 	= 0;    // 배송비할인금액
			int prevAddPaymentPrice		= 0;	// 이전 추가 배송비 금액
			boolean conditionType5Flag	= false;	// 수량별배송비 여부
			ShippingDataResultVo returnShippingPriceDto = null;
			int returnsClaimShippingPrice = 0;	// 반품 클레임 배송비
			boolean isSelect			= false;	// 선택상품 포함 여부

			String conditionType		= "";	// 배송정책타입
			long urWarehouseId			= 0;	// 출고처PK
			long shippingTmplId			= 0;	// 배송정책PK

			// 배송 정책별 Loop
			for(long ilShippingTmplId : tmplList.keySet()) {

				// 해당 배송정책 상품 목록
				List<OrderClaimTargetGoodsListDto> tmplGoodsList	= tmplList.get(ilShippingTmplId);
				ShippingDataResultVo shippingPriceDto				= this.getShippingDataResult(ilShippingTmplId);	// 배송정책정보조회
				String recvZipCd									= tmplGoodsList.get(0).getRecvZipCd();			// 수령인 우편주소
				int claimShippingPrice								= tmplGoodsList.get(0).getClaimShippingPrice();	// 클레임배송비

				log.debug("----------- 배송정책 정보 :: <{}>, 클레임 배송비 :: <{}>, tmplGoodsList Size :: <{}>", shippingPriceDto.getConditionType(), claimShippingPrice, tmplGoodsList.size());

				int goodsOrderPrice			= 0;	// 상품 주문 금액
				int goodsRefundSalePrice	= 0;	// 상품 금액 (salePrice * 수량)
				int goodsRefundPrice		= 0;	// 상품 환불 금액
				int cartCouponRefundPrice	= 0;	// 장바구니쿠폰 환불 금액
				int goodsCouponRefundPrice	= 0;	// 상품쿠폰 환불 금액
				int directRefundPrice		= 0;	// 즉시할인 환불 금액
				int employeeRefundPrice		= 0;	// 임직원 지원금 환불 금액
				int orderCnt				= 0;	// 주문 수량
				int cancelCnt				= 0;	// 취소 수량
				int claimCnt				= 0;	// 클레임 수량
				int shippingStdPrice		= 0;	// 배송비 기준 금액
				int goodsTaxPrice			= 0;	// 상품 과세 금액
				int goodsNonTaxPrice		= 0;	// 상품 비과세 금액

				// 상품 별 Loop
				for(OrderClaimTargetGoodsListDto tmplGoodsItem : tmplGoodsList) {
					// 주문 상세 PK
					long odOrderDetlId = tmplGoodsItem.getOdOrderDetlId();

					boolean isEmployee 		= UserEnums.BuyerType.EMPLOYEE.getCode().equals(tmplGoodsItem.getBuyerTypeCd());			// 임직원 여부
					boolean isRedelivery 	= OrderClaimEnums.AllTypeYn.ALL_TYPE_Y.getCode().equals(tmplGoodsItem.getRedeliveryYn());	// 재배송 여부
					int salePrice 			= tmplGoodsItem.getSalePrice(); 															// 상품판매가 (isEmployee ? tmplGoodsItem.getRecommendedPrice() : tmplGoodsItem.getSalePrice());
					int totSalePrice		= tmplGoodsItem.getTotSalePrice();															// 판매가총합
					int goodsInfoSalePrice	= 0;
					int goodsInfoPaidPrice	= 0;
					int goodsInfoClaimCnt	= 0;
					int discountGoodsPrice	= 0;
					int discountCartPrice	= 0;
					int discountDirectPrice	= 0;
					int discountEmployeePrice	= 0;
					int stdSalePrice		= (isEmployee ? tmplGoodsItem.getRecommendedPrice() : tmplGoodsItem.getSalePrice());		// 계산기준금액
					int goodsShippingAccountPrice = 0;
					conditionType			= tmplGoodsItem.getConditionTp();															// 배송정책타입
					orgShippingPrice		= tmplGoodsItem.getShippingPrice();															// 원결제배송비
					orderCnt 				+= tmplGoodsItem.getOrgOrderCnt();															// 주문수량
					cancelCnt				+= tmplGoodsItem.getCancelCnt();															// 취소수량
					goodsOrderPrice 		+= stdSalePrice * (tmplGoodsItem.getOrgOrderCnt() - tmplGoodsItem.getCancelCnt());			// 판매가 * (주문수량 - 취소수량)
					prevAddPaymentPrice 	+= tmplGoodsItem.getAddPaymentShippingPrice();												// 추가 결제 배송비

					// 선택 상품 취소
					if(isTargetGoodsList) {
						OrderClaimSearchGoodsDto selectGoods = goodSearchList.stream().filter(x -> x.getOdOrderDetlId() == odOrderDetlId).findAny().orElse(null);
						// 해당 주문번호가 선택한 상품일경우
						if (!ObjectUtils.isEmpty(selectGoods)) {
							isSelect = true;
							if(urWarehouseId < 1) {
								urWarehouseId = tmplGoodsItem.getUrWarehouseId();
								shippingTmplId = ilShippingTmplId;
							}

							int goodsOrderCnt = tmplGoodsItem.getOrgOrderCnt() - tmplGoodsItem.getCancelCnt() - selectGoods.getClaimCnt();
							goodsInfoClaimCnt = selectGoods.getClaimCnt() == 0 ? goodsOrderCnt : selectGoods.getClaimCnt();

							// 주문수량 - 취소수량 - 클레임 수량 != 0 일 경우 -> 클레임 수량 비율 계산
							// 주문수량 - 취소수량 - 클레임 수량이 0이 아닐 경우 부분취소 이므로, 총 금액을 클레임 수량만큼 비율 계산 해준다.
							discountCartPrice		= claimPriceUtil.getFloorRatePrice(tmplGoodsItem.getCartCouponPrice(), goodsInfoClaimCnt, tmplGoodsItem.getOrgOrderCnt());
							discountDirectPrice		= claimPriceUtil.getFloorRatePrice(tmplGoodsItem.getDirectPrice(), goodsInfoClaimCnt, tmplGoodsItem.getOrgOrderCnt());
							discountEmployeePrice	= claimPriceUtil.getFloorRatePrice(tmplGoodsItem.getEmployeeDiscountPrice(), goodsInfoClaimCnt, tmplGoodsItem.getOrgOrderCnt());
							// 주문수량 - 취소수량 - 클레임 수량 = 0 일 경우 -> 전체 할인금액 - 기 취소된 할인금액
							// 주문수량 - 취소수량 - 클레임 수량이 0일 경우 전체 취소 이므로, 총금액 - (기 취소된 금액) 계산하여 현재 남아있는 잔액정보로 값을 설정한다.
							if (goodsOrderCnt == 0) {
								discountGoodsPrice = tmplGoodsItem.getGoodsCouponPrice();
								// 이미 이전에 클레임 완료처리 된 건이 존재할 경우
								if (tmplGoodsItem.getCancelCnt() > 0) {
									discountCartPrice = tmplGoodsItem.getCartCouponPrice() - claimPriceUtil.getFloorRatePrice(tmplGoodsItem.getCartCouponPrice(),
																																	tmplGoodsItem.getCancelCnt(),
																																	tmplGoodsItem.getOrgOrderCnt());
									discountDirectPrice = tmplGoodsItem.getDirectPrice() - claimPriceUtil.getFloorRatePrice(tmplGoodsItem.getDirectPrice(),
																															tmplGoodsItem.getCancelCnt(),
																															tmplGoodsItem.getOrgOrderCnt());
									discountEmployeePrice = tmplGoodsItem.getEmployeeDiscountPrice() - claimPriceUtil.getFloorRatePrice(tmplGoodsItem.getEmployeeDiscountPrice(),
																																		tmplGoodsItem.getCancelCnt(),
																																		tmplGoodsItem.getOrgOrderCnt());
								}
							}
							claimCnt += selectGoods.getClaimCnt();

							// 증정품 / 증정(식품마케팅) 이 아닐 경우
							if(!GoodsEnums.GoodsType.GIFT.getCode().equals(tmplGoodsItem.getGoodsTpCd()) &&
								!GoodsEnums.GoodsType.GIFT_FOOD_MARKETING.getCode().equals(tmplGoodsItem.getGoodsTpCd())) {
								// 재배송 상품일 경우 결제 금액 기준으로 환불
								if (isRedelivery) {
									log.debug("=============== 재배송 상품");
									// 주문수량 - 취소수량 - 클레임 수량이 0일 경우 전체 취소 이므로, 총금액 - (기 취소된 금액) 계산하여 현재 남아있는 잔액정보로 값을 설정한다.
									int reShippingTotSalePrice = 0;
									int reShippingPaidPrice = 0;
									if (goodsOrderCnt == 0) {
										reShippingTotSalePrice = tmplGoodsItem.getTotSalePrice() - claimPriceUtil.getFloorRatePrice(tmplGoodsItem.getTotSalePrice(),
																																	tmplGoodsItem.getCancelCnt(),
																																	tmplGoodsItem.getOrgOrderCnt());
										reShippingPaidPrice = tmplGoodsItem.getPaidPrice() - claimPriceUtil.getFloorRatePrice(tmplGoodsItem.getPaidPrice(),
																																tmplGoodsItem.getCancelCnt(),
																																tmplGoodsItem.getOrgOrderCnt());
									}
									// 주문수량 - 취소수량 - 클레임 수량이 0이 아닐 경우 부분취소 이므로, 총 금액을 클레임 수량만큼 비율 계산 해준다.
									else {
										reShippingTotSalePrice = claimPriceUtil.getFloorRatePrice(tmplGoodsItem.getTotSalePrice(),
																									goodsInfoClaimCnt,
																									tmplGoodsItem.getOrgOrderCnt());
										reShippingPaidPrice = claimPriceUtil.getFloorRatePrice(tmplGoodsItem.getPaidPrice(),
																									goodsInfoClaimCnt,
																									tmplGoodsItem.getOrgOrderCnt());
									}
									goodsInfoSalePrice 		= reShippingTotSalePrice;
									goodsRefundSalePrice 	+= reShippingTotSalePrice;
									discountEmployeePrice 	= tmplGoodsItem.getEmployeeDiscountPrice();
									goodsShippingAccountPrice	+= reShippingPaidPrice;
								}
								else {
									// 전체 취소일 경우
									if (goodsOrderCnt == 0) {
										log.debug("=============== if(goodsOrderCnt == 0) -> 전체취소");
										goodsInfoSalePrice 		= tmplGoodsItem.getTotSalePrice() - (salePrice * tmplGoodsItem.getCancelCnt());
										goodsRefundSalePrice 	+= tmplGoodsItem.getTotSalePrice() - (tmplGoodsItem.getSalePrice() * tmplGoodsItem.getCancelCnt());
										goodsShippingAccountPrice	+= tmplGoodsItem.getTotSalePrice() - (stdSalePrice * tmplGoodsItem.getCancelCnt());
									}
									// 부분 취소일 경우
									else {
										log.debug("=============== if(goodsOrderCnt != 0) -> 부분취소");
										goodsInfoSalePrice 		= salePrice * goodsInfoClaimCnt;
										goodsRefundSalePrice 	+= tmplGoodsItem.getSalePrice() * goodsInfoClaimCnt;
										goodsShippingAccountPrice	+= stdSalePrice * goodsInfoClaimCnt;
									}
								}
							}
							// 증정품일 경우 판매가 0 처리
							else {
								salePrice = 0;
							}
							goodsRefundPrice += goodsInfoSalePrice;
							goodsCouponRefundPrice += discountGoodsPrice;
							cartCouponRefundPrice += discountCartPrice;
							directRefundPrice += discountDirectPrice;
							employeeRefundPrice += discountEmployeePrice;

							shippingStdPrice += goodsShippingAccountPrice;
							//goodsInfoPaidPrice = (salePrice * goodsInfoClaimCnt) - discountGoodsPrice - discountCartPrice;
							goodsInfoPaidPrice = goodsInfoSalePrice;

							// 과세일 경우
							if(OrderClaimEnums.AllTypeYn.ALL_TYPE_Y.getCode().equals(tmplGoodsItem.getTaxYn())) {
								goodsTaxPrice += goodsInfoSalePrice - discountCartPrice - discountGoodsPrice;
							}
							// 비과세일 경우
							else {
								goodsNonTaxPrice += goodsInfoSalePrice - discountCartPrice - discountGoodsPrice;
							}

							/** 상품별 클레임 상품 금액 정보 SET START */
							OrderClaimGoodsPriceInfoDto claimGoodsPriceInfoDto = new OrderClaimGoodsPriceInfoDto();
							claimGoodsPriceInfoDto.setOdOrderDetlId(tmplGoodsItem.getOdOrderDetlId());
							claimGoodsPriceInfoDto.setIlGoodsId(tmplGoodsItem.getIlGoodsId());
							claimGoodsPriceInfoDto.setGoodsNm(tmplGoodsItem.getGoodsNm());
							claimGoodsPriceInfoDto.setClaimCnt(goodsInfoClaimCnt);
							claimGoodsPriceInfoDto.setSalePrice(salePrice);
							claimGoodsPriceInfoDto.setTotSalePrice(goodsInfoSalePrice);
							claimGoodsPriceInfoDto.setGoodsCouponPrice(discountGoodsPrice);
							claimGoodsPriceInfoDto.setCartCouponPrice(discountCartPrice);
							claimGoodsPriceInfoDto.setDirectPrice(discountDirectPrice + discountEmployeePrice);
							claimGoodsPriceInfoDto.setPaidPrice(goodsInfoPaidPrice);
							claimGoodsPriceInfoList.add(claimGoodsPriceInfoDto);
							/** 상품별 클레임 상품 금액 정보 SET END */

							// 발급 쿠폰 정보
							deliveryCouponIssueId	= tmplGoodsItem.getDeliveryCouponIssueId();
							pmCouponNm 				= tmplGoodsItem.getPmCouponNm();
							pmCouponBenefit			= tmplGoodsItem.getPmCouponBenefit();
							shippingDiscountPrice	= tmplGoodsItem.getShippingDiscountPrice();
						} // END if (!ObjectUtils.isEmpty(selectGoods))
					} // END if(isTargetGoodsList)
				} // END for(OrderClaimTargetGoodsListDto tmplGoodsItem : tmplGoodsList)

				goodsPrice 			+= goodsRefundPrice;		// 상품금액
				goodsSalePriceSum	+= goodsRefundSalePrice;	// 상품금액(salePrice * 수량)
				goodsCouponPrice	+= goodsCouponRefundPrice;	// 상품쿠폰금액
				cartCouponPrice		+= cartCouponRefundPrice;	// 장바구니쿠폰금액
				directPrice			+= directRefundPrice;		// 즉시할인금액
				employeePrice		+= employeeRefundPrice;		// 임직원지원금

				totTaxParice		+= goodsTaxPrice;			// 상품과세금액
				totNonTaxParice		+= goodsNonTaxPrice;		// 상품비과세금액

				templateOrderCnt	+= orderCnt;
				templateCancelCnt	+= cancelCnt;
				templateClaimCnt	+= claimCnt;

				ShippingPriceResponseDto orgShippingPriceInfo = null;

				// 수량별 배송비의 경우 claimCnt 수량 만큼 배송비를 환불 해주어야 함
				if(GoodsEnums.ConditionType.TYPE5.getCode().equals(shippingPriceDto.getConditionType())) {
					orgShippingPriceInfo = this.getGoodsShippingPriceInfo(shippingPriceDto, (goodsOrderPrice - shippingStdPrice), claimCnt, recvZipCd);
					amountShippingPrice = orgShippingPriceInfo.getShippingPrice();
					conditionType5Flag = true;
					returnShippingPriceDto = shippingPriceDto;
					returnsClaimShippingPrice = claimShippingPrice;
				}
				else {
					orgShippingPriceInfo = this.getGoodsShippingPriceInfo(shippingPriceDto, (goodsOrderPrice - shippingStdPrice), (orderCnt - cancelCnt), recvZipCd);
					// 현재 계산된 금액이 최소일 경우
					if(amountShippingPrice > orgShippingPriceInfo.getShippingPrice()) {
						returnShippingPriceDto = shippingPriceDto;
						returnsClaimShippingPrice = claimShippingPrice;
					}
					// 최소 금액으로 배송비 계산
					amountShippingPrice = Math.min(amountShippingPrice, orgShippingPriceInfo.getShippingPrice());
				}

				log.debug("-------------- [{}] :: orderCnt :: <{}>, cancelCnt :: <{}>, claimCnt :: <{}>", shippingPriceDto.getConditionType(), orderCnt, cancelCnt, claimCnt);
			} // END for(long ilShippingTmplId : tmplList.keySet())

			/**
			* 수량별 배송비는 합배송 무조건 제외
			* 수량별 배송비를 제외한 나머지 상품들은 해당 배송비PK에 포함된 상품이 전체 취소 될 때 환불 해줌
			* 추가 결제 금액이 발생했을 경우에도 해당 배송비PK에 포함된 상품이 전체 취소 될 때 환불 해줌
			*/
			log.debug("------ 수량별 배송비 여부 :: <{}>", conditionType5Flag);
			log.debug("------ templateOrderCnt :: <{}>, templateCancelCnt :: <{}>, templateClaimCnt :: <{}>, amountShippingPrice :: <{}>, prevAddPaymentPrice :: <{}>", templateOrderCnt, templateCancelCnt, templateClaimCnt, amountShippingPrice, prevAddPaymentPrice);
			Map<String, Integer> shippingMap = null;
			if(isSelect && isShopPicup) {
				if (!isReturns) {
					if(claimFlag) {
						shippingMap = this.getClaimCancelPriceInfo(conditionType5Flag, amountShippingPrice, (templateOrderCnt - templateCancelCnt - templateClaimCnt), orgShippingPrice, prevAddPaymentPrice, isOutmallFlag, isSellerFlag, prevTargetTp, isAddPayment, reqDto.getReAccountShippingPriceYn(), reqDto.getRefundReqShippingPrice(), reqDto.getRefundReqOrderShippingPrice());
					}
					else {
						shippingMap = this.getCancelPriceInfo(conditionType5Flag, amountShippingPrice, (templateOrderCnt - templateCancelCnt - templateClaimCnt), orgShippingPrice, prevAddPaymentPrice, isOutmallFlag, isSellerFlag);
					}
					if (!shippingMap.isEmpty()) {
						shippingPrice += shippingMap.get("shippingPrice");
						addShippingPrice += shippingMap.get("addShippingPrice");
						orderShippingPrice += shippingMap.get("orderShippingPrice");
						prevPaymentShippingPrice += shippingMap.get("prevPaymentShippingPrice");
					}
				} else {
					log.debug("--------------------------- 반품 배송비 정보 :: <{}>, 이전 결제 배송비 :: <{}>", shippingPrice, prevAddPaymentPrice);
					if(claimFlag) {
						shippingMap = this.getClaimReturnPriceInfo(conditionType, returnShippingPriceDto, prevAddPaymentPrice, returnsClaimShippingPrice, templateClaimCnt, orgShippingPrice,
																	amountShippingPrice, reqDto.getRecvZipCd(), isSellerFlag, isReturnFlag, prevTargetTp, prevReturnsYn, (templateOrderCnt - templateCancelCnt - templateClaimCnt), reqDto.getReAccountShippingPriceYn(), reqDto.getRefundReqShippingPrice(), reqDto.getRefundReqOrderShippingPrice());
					}
					else {
						shippingMap = this.getReturnPriceInfo(conditionType, returnShippingPriceDto, prevAddPaymentPrice, returnsClaimShippingPrice, templateClaimCnt, orgShippingPrice,
																amountShippingPrice, reqDto.getRecvZipCd(), isSellerFlag, isReturnFlag, (templateOrderCnt - templateCancelCnt - templateClaimCnt));
					}
					if (!shippingMap.isEmpty()) {
						shippingPrice += shippingMap.get("shippingPrice");
						addShippingPrice += shippingMap.get("addShippingPrice");
						orderShippingPrice += shippingMap.get("orderShippingPrice");
						//orderShippingPrice += shippingMap.get("addPaymentShippingPrice");
						//prevPaymentShippingPrice += shippingMap.get("prevPaymentShippingPrice");
					}
				}

				if(!ObjectUtils.isEmpty(shippingMap) && !shippingMap.isEmpty() && shippingMap.get("addShippingPrice") > 0) {
					// 추가 배송비 정보 Set
					OrderClaimAddPaymentShippingPriceDto addAPymentInfo = new OrderClaimAddPaymentShippingPriceDto();
					addAPymentInfo.setUrWarehouseId(urWarehouseId);
					addAPymentInfo.setIlShippingTmplId(shippingTmplId);
					addAPymentInfo.setAddPaymentShippingPrice(shippingMap.get("addShippingPrice"));
					addPaymentList.add(addAPymentInfo);
				}
			}
			// 배송 쿠폰 정보가 존재할 경우 ADD
			if (templateClaimCnt > 0 && ((templateOrderCnt - templateCancelCnt - templateClaimCnt) == 0) && deliveryCouponIssueId > 0) {
				//if(deliveryCouponIssueId > 0) {
				OrderClaimCouponInfoDto orderClaimCouponInfo = new OrderClaimCouponInfoDto();
				orderClaimCouponInfo.setOdOrderId(reqDto.getOdOrderId());
				orderClaimCouponInfo.setPmCouponIssueId(deliveryCouponIssueId);
				orderClaimCouponInfo.setPmCouponNm(pmCouponNm);
				orderClaimCouponInfo.setPmCouponBenefit(pmCouponBenefit);
				orderClaimCouponInfo.setDiscountPrice(shippingDiscountPrice);
				deliveryCouponList.add(orderClaimCouponInfo);
				if(shippingPrice > 0) {
					shippingPrice -= shippingDiscountPrice;
				}
			} // END if(deliveryCouponIssueId > 0)
		} // END for(long odShippingPriceId : shippingTemplate.keySet())
		log.debug("================================================================================================");

//		int refundTotalPrice = goodsPrice - goodsCouponPrice - cartCouponPrice - directPrice - prevPaymentShippingPrice + shippingPrice;
		int refundGoodsPrice = (goodsPrice < (goodsCouponPrice + cartCouponPrice)) ? 0 : goodsPrice - goodsCouponPrice - cartCouponPrice;
		int refundTotalPrice = refundGoodsPrice - prevPaymentShippingPrice + shippingPrice;
		log.debug("refundTotalPrice :: <{}>, goodsPrice :: <{}>, goodsCouponPrice :: <{}>, cartCouponPrice :: <{}>, directPrice :: <{}>", refundTotalPrice, goodsPrice, goodsCouponPrice, cartCouponPrice, directPrice);
		log.debug("prevPaymentShippingPrice :: <{}>, shippingPrice :: <{}>", prevPaymentShippingPrice, shippingPrice);

		refundInfoDto.setGoodsPrice					(goodsPrice);					// 상품금액
		refundInfoDto.setGoodsSalePriceSum			(goodsSalePriceSum);			// 상품판매가 합계 금액
		refundInfoDto.setGoodsCouponPrice			(goodsCouponPrice);				// 상품쿠폰금액
		refundInfoDto.setCartCouponPrice			(cartCouponPrice);				// 장바구니쿠폰금액
		refundInfoDto.setDirectPrice				(directPrice);					// 상품쿠폰금액, 장바구니쿠폰금액 제외한 나머지 할인금액
		refundInfoDto.setEmployeePrice				(employeePrice);				// 임직원지원금
		refundInfoDto.setShippingPrice				(shippingPrice);				// 배송비
		refundInfoDto.setOrderShippingPrice			(orderShippingPrice);			// 주문시 배송비
		refundInfoDto.setAddPaymentShippingPrice	(addShippingPrice);				// 추가배송비금액
		refundInfoDto.setPrevAddPaymentShippingPrice(prevPaymentShippingPrice);		// 기 결제한 추가배송비금액
		refundInfoDto.setAddPaymentList				(addPaymentList);				// 추가결제배송비 목록
		refundInfoDto.setDeliveryCouponList			(deliveryCouponList);			// 배송쿠폰목록
		refundInfoDto.setRefundTotalPrice			(refundTotalPrice);				// 환불금액합계
		refundInfoDto.setGoodsPriceList				(claimGoodsPriceInfoList);		// 클레임상품 금액 목록

		// 만약 총 결제금액 정보가 1보다 작고, 추가배송비가 존재할 경우 배송비를 추가로 결제 받고, 원 상품 금액은 환불 해준다
		if(refundInfoDto.getRefundTotalPrice() < 1 && refundInfoDto.getAddPaymentShippingPrice() > 0) {
			refundInfoDto.setAddPaymentShippingPriceYn(OrderClaimEnums.AllTypeYn.ALL_TYPE_Y.getCode());
			refundInfoDto.setShippingPrice(0);
			refundTotalPrice += refundInfoDto.getAddPaymentShippingPrice();
		}

		/** 2021.04.21 추가 START */
		claimUtilRefundService.setRefundPriceInfo(reqDto, paymentInfoDto, refundInfoDto, refundTotalPrice, totTaxParice, totNonTaxParice);
		/** 2021.04.21 추가 END */

		// 선택 상품 존재 시 과세 / 면세 금액정보 Set
		if(CollectionUtils.isNotEmpty(goodsList)) {
			this.setTaxablePriceInfo(reqDto, goodsList, paymentInfoDto, refundInfoDto, false);
		}

		refundInfoDto.setRefundRegPrice(refundInfoDto.getRefundRegPrice() + refundInfoDto.getRefundPrice());
		refundInfoDto.setRefundTotalPrice(refundInfoDto.getRefundRegPrice() + refundInfoDto.getRefundPointPrice());

		/** 프론트에서 사용하는 금액정보 START */
		int frontGoodsPrice = refundInfoDto.getGoodsPrice() - refundInfoDto.getGoodsCouponPrice() - refundInfoDto.getCartCouponPrice();// - refundInfoDto.getDirectPrice();
		// 할인금액이 상품 금액보다 클 경우 0원 처리
		if(refundInfoDto.getGoodsPrice() < (refundInfoDto.getGoodsCouponPrice() + refundInfoDto.getCartCouponPrice())) frontGoodsPrice = 0;
		refundInfoDto.setOrderShippingPrice(refundInfoDto.getOrderShippingPrice());														// 주문시 부과된 배송비
		refundInfoDto.setRefundReqPrice(frontGoodsPrice + refundInfoDto.getOrderShippingPrice());										// 환불신청금액 (환불예정상품금액 + 주문시부과된 배송비)
		refundInfoDto.setRefundGoodsPrice(frontGoodsPrice);																				// 환불예정상품금액 (할인금액 제외)
		refundInfoDto.setRefundAddShippingPrice(Math.abs(refundInfoDto.getAddPaymentShippingPrice()));									// 환불시 추가 배송비
		refundInfoDto.setTotalRefundPrice(refundInfoDto.getRefundPrice() + refundInfoDto.getRefundPointPrice());	// 총 환불 예정금액
		refundInfoDto.setPaymentRefundPrice(refundInfoDto.getRefundPrice());			// 결제수단 환불금액
		refundInfoDto.setPointRefundPrice(refundInfoDto.getRefundPointPrice());															// 적립금 환불금액
		/** 프론트에서 사용하는 금액정보 END */

		log.debug("환불 정보 최종 ::::: <{}>", refundInfoDto.toString());

		return refundInfoDto;
	}

	/**
	 * 배송비 쿠폰 정보 Set
	 * @param reqDto
	 * @param targetGoodsList
	 * @param refundInfoDto
	 */
	private void setDeliveryCouponList(OrderClaimViewRequestDto reqDto, List<OrderClaimTargetGoodsListDto> targetGoodsList, OrderClaimPriceInfoDto refundInfoDto) {

		// 선택상품 목록
		List<OrderClaimSearchGoodsDto> goodSearchList = reqDto.getGoodSearchList();
		// 배송비 쿠폰 목록
		List<OrderClaimCouponInfoDto> deliveryCouponList = new ArrayList<>();
		// 요청 상품 목록 여부
		boolean isTargetGoodsList = CollectionUtils.isNotEmpty(goodSearchList);
		Map<Long, Map<Long, List<OrderClaimTargetGoodsListDto>>> shippingTemplate = null;

		log.debug("getCancelRefundInfo GoodSearchList 유무 ::: <{}>", isTargetGoodsList);

		// 요청 상품 목록이 있을 경우
		if(isTargetGoodsList) {
			// 주문배송비PK, 배송정책 별 그룹
			shippingTemplate = targetGoodsList.stream().collect(	Collectors.groupingBy(OrderClaimTargetGoodsListDto::getOdShippingPriceId, LinkedHashMap::new,
					Collectors.groupingBy(OrderClaimTargetGoodsListDto::getShippingTmplId, LinkedHashMap::new, Collectors.toList())));
		}
		// 요청 상품 목록이 없을 경우
		else {
			// 전체 취소 상품이 아닌 건만
			// 주문배송비PK, 배송정책 별 그룹
			shippingTemplate = targetGoodsList.stream().filter(x -> OrderClaimEnums.AllTypeYn.ALL_TYPE_N.getCode().equals(x.getCancelYn()))
					.collect(	Collectors.groupingBy(OrderClaimTargetGoodsListDto::getOdShippingPriceId, LinkedHashMap::new,
							Collectors.groupingBy(OrderClaimTargetGoodsListDto::getShippingTmplId, LinkedHashMap::new, Collectors.toList())));
		}

		log.debug("----- 그룹핑 데이터 EMPTY 유무 :: <{}>", shippingTemplate.isEmpty());

		log.debug("================================================================================================");
		// 주문배송비PK로 Loop
		for(long odShippingPriceId : shippingTemplate.keySet()) {
			log.debug("----------- 주문배송비 PK :: <{}>", odShippingPriceId);
			// 배송비 정책별 상품 목록 얻기
			Map<Long, List<OrderClaimTargetGoodsListDto>> tmplList = shippingTemplate.get(odShippingPriceId);
			int templateOrderCnt		= 0;	// 배송비PK 주문수량 합계
			int templateCancelCnt		= 0;	// 배송비PK 주문취소수량 합계
			int templateClaimCnt		= 0;	// 배송비PK 주문클레임수량 합계
			long deliveryCouponIssueId 	= 0;    // 배송비쿠폰 발급 ID
			String pmCouponNm 			= "";   // 배송비쿠폰명
			String pmCouponBenefit 		= "";   // 배송비쿠폰혜택정보
			int shippingDiscountPrice 	= 0;    // 배송비할인금액

			// 배송 정책별 Loop
			for(long ilShippingTmplId : tmplList.keySet()) {

				// 해당 배송정책 상품 목록
				List<OrderClaimTargetGoodsListDto> tmplGoodsList	= tmplList.get(ilShippingTmplId);

				int orderCnt				= 0;	// 주문 수량
				int cancelCnt				= 0;	// 취소 수량
				int claimCnt				= 0;	// 클레임 수량

				// 상품 별 Loop
				for(OrderClaimTargetGoodsListDto tmplGoodsItem : tmplGoodsList) {

					// 주문 상세 PK
					long odOrderDetlId = tmplGoodsItem.getOdOrderDetlId();
					orderCnt 				+= tmplGoodsItem.getOrgOrderCnt();															// 주문수량
					cancelCnt				+= tmplGoodsItem.getCancelCnt();															// 취소수량

					// 선택 상품 취소
					if(isTargetGoodsList) {
						OrderClaimSearchGoodsDto selectGoods = goodSearchList.stream().filter(x -> x.getOdOrderDetlId() == odOrderDetlId).findAny().orElse(null);
						// 해당 주문번호가 선택한 상품일경우
						if (!ObjectUtils.isEmpty(selectGoods)) {
							claimCnt += selectGoods.getClaimCnt();

							// 발급 쿠폰 정보
							deliveryCouponIssueId	= tmplGoodsItem.getDeliveryCouponIssueId();
							pmCouponNm 				= tmplGoodsItem.getPmCouponNm();
							pmCouponBenefit			= tmplGoodsItem.getPmCouponBenefit();
							shippingDiscountPrice	= tmplGoodsItem.getShippingDiscountPrice();
						} // END if (!ObjectUtils.isEmpty(selectGoods))
					} // END if(isTargetGoodsList)
				} // END for(OrderClaimTargetGoodsListDto tmplGoodsItem : tmplGoodsList)

				templateOrderCnt	+= orderCnt;
				templateCancelCnt	+= cancelCnt;
				templateClaimCnt	+= claimCnt;
			} // END for(long ilShippingTmplId : tmplList.keySet())

			// 배송 쿠폰 정보가 존재할 경우 ADD
			if (templateClaimCnt > 0 && ((templateOrderCnt - templateCancelCnt - templateClaimCnt) == 0) && deliveryCouponIssueId > 0) {
				//if(deliveryCouponIssueId > 0) {
				OrderClaimCouponInfoDto orderClaimCouponInfo = new OrderClaimCouponInfoDto();
				orderClaimCouponInfo.setOdOrderId(reqDto.getOdOrderId());
				orderClaimCouponInfo.setPmCouponIssueId(deliveryCouponIssueId);
				orderClaimCouponInfo.setPmCouponNm(pmCouponNm);
				orderClaimCouponInfo.setPmCouponBenefit(pmCouponBenefit);
				orderClaimCouponInfo.setDiscountPrice(shippingDiscountPrice);
				deliveryCouponList.add(orderClaimCouponInfo);
			} // END if(deliveryCouponIssueId > 0)
		} // END for(long odShippingPriceId : shippingTemplate.keySet())
		log.debug("================================================================================================");

		refundInfoDto.setDeliveryCouponList(deliveryCouponList);
	}

	/**
	 * 과세 면세 금액 Set
	 * @param reqDto
	 * @param goodsList
	 * @param paymentInfoDto
	 * @param refundInfoDto
	 * @throws Exception
	 */
	private void setTaxablePriceInfo(OrderClaimViewRequestDto reqDto, List<OrderClaimGoodsInfoDto> goodsList, OrderClaimPaymentInfoDto paymentInfoDto, OrderClaimPriceInfoDto refundInfoDto, boolean csRefundFlag) throws Exception {
		// 과세 / 비과세 금액 얻기
		OrderClaimRegisterRequestDto taxNonTaxDto = new OrderClaimRegisterRequestDto();
		taxNonTaxDto.setOdOrderId(reqDto.getOdOrderId());
		taxNonTaxDto.setGoodsInfoList(goodsList);
		claimUtilRefundService.setTaxNonTaxPriceByRefundInfo(refundInfoDto, taxNonTaxDto);

		refundInfoDto.setRefundPrice(taxNonTaxDto.getRefundPrice());              //환불금액
		refundInfoDto.setRefundPointPrice(taxNonTaxDto.getRefundPointPrice());    //환불적립금액
		refundInfoDto.setTaxablePrice(taxNonTaxDto.getTaxablePrice());            //과세환불금액
		refundInfoDto.setNonTaxablePrice(taxNonTaxDto.getNonTaxablePrice());      //면세환불금액
		// CS환불의 경우 이전 프로세스에서 이미 계산 되어 나오기때문에 잔여금액 계산을 하지 않는다
		if(!csRefundFlag) {
			refundInfoDto.setRemainPaymentPrice(paymentInfoDto.getPaymentPrice() - taxNonTaxDto.getRefundPrice());    //잔여결제금액
			refundInfoDto.setRemainPointPrice(paymentInfoDto.getPointPrice() - taxNonTaxDto.getRefundPointPrice());    //잔여적립금액
			if(refundInfoDto.getRemainPaymentPrice() < 0) {
				refundInfoDto.setRemainPaymentPrice(0);
			}
			if(refundInfoDto.getRemainPointPrice() < 0) {
				refundInfoDto.setRemainPointPrice(0);
			}
		}
	}

	/**
	 * 클레임 취소 환불 금액 계산
	 * @param conditionType5Flag
	 * @param amountShippingPrice
	 * @param qty
	 * @param orgShippingPrice
	 * @param prevAddPaymentPrice
	 * @param isOutmallFlag
	 * @param isSellerFlag
	 * @param prevTargetTp
	 * @param isAddPayment
	 * @return
	 */
	public Map<String, Integer> getClaimCancelPriceInfo(boolean conditionType5Flag,
														int amountShippingPrice,
														int qty,
														int orgShippingPrice,
														int prevAddPaymentPrice,
														boolean isOutmallFlag,
														boolean isSellerFlag,
														String prevTargetTp,
														boolean isAddPayment,
														String reAccountShippingPriceYn,
														int refundReqShippingPrice,
														int refundReqOrderShippingPrice) {
		log.debug("1. 클레임 취소 환불 금액 계산 --------------------");
		Map<String, Integer> resultMap = new HashMap<>();
		if(!OrderClaimEnums.AllTypeYn.ALL_TYPE_Y.getCode().equals(reAccountShippingPriceYn)) {
			log.debug("배송비 재계산하지 않음 --------------------");
			resultMap.put("shippingPrice"				, refundReqShippingPrice);		// 배송비
			resultMap.put("addShippingPrice"			, 0);							// 추가배송비
			resultMap.put("orderShippingPrice"			, refundReqOrderShippingPrice);	// 주문시배송비
			resultMap.put("prevPaymentShippingPrice"	, 0);							// 이전결제배송비
			return resultMap;
		}
		int shippingPrice = 0;
		int addShippingPrice = 0;
		int orderShippingPrice = 0;
		int prevPaymentShippingPrice = 0;
		// 현재 구매자 귀책일때
		if(!isSellerFlag) {
			// 이전 귀책구분이 판매자일 경우 배송비 재계산
			if(OrderClaimEnums.ClaimTargetTp.TARGET_SELLER.getCode().equals(prevTargetTp)) {
				log.debug("취소 이전 판매자귀책 / 현재 구매자귀책, 배송비 재계산 처리");
				return this.getCancelPriceInfo(conditionType5Flag, amountShippingPrice, qty, orgShippingPrice, prevAddPaymentPrice, isOutmallFlag, isSellerFlag);
			}
		}
		// 현재 판매자 귀책 일때
		else {
			log.debug("취소 현재 판매자 귀책");
			resultMap = this.getCancelPriceInfo(conditionType5Flag, amountShippingPrice, qty, orgShippingPrice, prevAddPaymentPrice, isOutmallFlag, isSellerFlag);
			shippingPrice = resultMap.get("shippingPrice");
			orderShippingPrice = resultMap.get("orderShippingPrice");
			// 추가 결제 내역이 존재할 경우
			if (isAddPayment) {
				log.debug("취소 현재 판매자 귀책 > 추가 결제 내역 존재");
				shippingPrice = resultMap.get("shippingPrice") + prevAddPaymentPrice;
				orderShippingPrice = resultMap.get("orderShippingPrice") + prevAddPaymentPrice;
				prevPaymentShippingPrice = resultMap.get("prevPaymentShippingPrice") + prevAddPaymentPrice;
			}
		}

		log.debug("취소 최종 배송비 2 :: <{}>, addShippingPrice :: <{}>, orderShippingPrice :: <{}>, prevPaymentShippingPrice :: <{}>", shippingPrice, addShippingPrice, orderShippingPrice, prevPaymentShippingPrice);
		resultMap.put("shippingPrice"				, shippingPrice);				// 배송비
		resultMap.put("addShippingPrice"			, addShippingPrice);			// 추가배송비
		resultMap.put("orderShippingPrice"			, orderShippingPrice);			// 주문시배송비
		resultMap.put("prevPaymentShippingPrice"	, prevPaymentShippingPrice);	// 이전결제배송비
		return resultMap;
	}

	/**
	 * 취소 환불 금액 계산
	 * @param conditionType5Flag
	 * @param amountShippingPrice
	 * @param qty
	 * @param orgShippingPrice
	 * @param prevAddPaymentPrice
	 * @return
	 */
	public Map<String, Integer> getCancelPriceInfo(boolean conditionType5Flag,
												   int amountShippingPrice,
												   int qty,
												   int orgShippingPrice,
												   int prevAddPaymentPrice,
												   boolean isOutmallFlag,
												   boolean isSellerFlag) {
		log.debug("1. 취소 환불 금액 계산 --------------------");
		Map<String, Integer> resultMap = new HashMap<>();
		int shippingPrice = 0;
		int addShippingPrice = 0;
		int orderShippingPrice = 0;
		int prevPaymentShippingPrice = 0;
		if(conditionType5Flag) {
			log.debug("수량별 배송비 :: <{}>", amountShippingPrice);
			shippingPrice = amountShippingPrice;
			orderShippingPrice = amountShippingPrice;
		}
		// 수량별 배송비가 아닐 경우
		else {
			// 주문배송비PK 정보 내 상품목록 모두 취소일 경우 배송비 환불
			if(qty == 0) {
				log.debug("-----------주문배송비PK 정보 내 상품목록 모두 취소일 경우 배송비 환불");
				shippingPrice = orgShippingPrice;
				orderShippingPrice = orgShippingPrice;

				// 이전에 결제한 배송비가 존재할 경우
				if(prevAddPaymentPrice > 0) {
					log.debug("-----------주문배송비PK 정보 내 상품목록 모두 취소일 경우 배송비 환불 > 이전에 결제한 배송비가 존재");
					prevPaymentShippingPrice = prevAddPaymentPrice;
					shippingPrice += prevAddPaymentPrice;
					orderShippingPrice += prevAddPaymentPrice;
				}
			}
			// 주문배송비PK 정보 내 상품목록 부분 취소일 경우 배송비 환불
			else {
				log.debug("-----------주문배송비PK 정보 내 상품목록 부분 취소일 경우 배송비 환불");
				// 이전 결제 배송비가 존재하지 않을 경우, 귀책 여부가 구매자 귀책일 경우
				if(prevAddPaymentPrice < 1 && !isSellerFlag) {
					log.debug("-----------주문배송비PK 정보 내 상품목록 부분 취소일 경우 배송비 환불 > 이전 결제 배송비가 미존재");
					// 이전에 무료배송 이었으나 .. 현재 배송비가 발생했을 경우
					if (orgShippingPrice == 0 && (amountShippingPrice != Integer.MAX_VALUE && amountShippingPrice > 0) && !isOutmallFlag) {
						log.debug("-----------주문배송비PK 정보 내 상품목록 부분 취소일 경우 배송비 환불 > 이전 결제 배송비가 미존재 > 이전에 무료배송 이었으나 현재 배송비가 발생");
						shippingPrice = amountShippingPrice * -1;
						addShippingPrice = amountShippingPrice;
					}
				}
			}
		}
		log.debug("취소 최종 배송비 :: <{}>, addShippingPrice :: <{}>, orderShippingPrice :: <{}>, prevPaymentShippingPrice :: <{}>", shippingPrice, addShippingPrice, orderShippingPrice, prevPaymentShippingPrice);
		resultMap.put("shippingPrice"				, shippingPrice);				// 배송비
		resultMap.put("addShippingPrice"			, addShippingPrice);			// 추가배송비
		resultMap.put("orderShippingPrice"			, orderShippingPrice);			// 주문시배송비
		resultMap.put("prevPaymentShippingPrice"	, prevPaymentShippingPrice);	// 이전결제배송비
		return resultMap;
	}

	/**
	 * 반품 환불 금액 계산
	 * @param conditionTp
	 * @param shippingPriceDto
	 * @param prevAddPaymentPrice
	 * @param claimShippingPrice
	 * @param goodsClaimQty
	 * @param orgShippingPrice
	 * @param amountShippingPrice
	 * @param sendRecvZipCd
	 * @param isSellerFlag
	 * @param isReturnYn
	 * @param prevTargetTp
	 * @param prevReturnsYn
	 * @return
	 */
	public Map<String, Integer> getClaimReturnPriceInfo(String conditionTp,
													   ShippingDataResultVo shippingPriceDto,
													   int prevAddPaymentPrice,
													   int claimShippingPrice,
													   int goodsClaimQty,
													   int orgShippingPrice,
													   int amountShippingPrice,
													   String sendRecvZipCd,
													   boolean isSellerFlag,
													   boolean isReturnYn,
													   String prevTargetTp,
													   String prevReturnsYn,
													   int qty,
													   String reAccountShippingPriceYn,
													   int refundReqShippingPrice,
													   int refundReqOrderShippingPrice) {
		Map<String, Integer> resultMap = new HashMap<>();
		log.debug("1. 반품 환불 금액 계산 --------------------");

		if(!OrderClaimEnums.AllTypeYn.ALL_TYPE_Y.getCode().equals(reAccountShippingPriceYn)) {
			log.debug("배송비 재계산하지 않음 --------------------");
			resultMap.put("shippingPrice"				, refundReqShippingPrice);		// 배송비
			resultMap.put("addShippingPrice"			, 0);							// 추가배송비
			resultMap.put("orderShippingPrice"			, refundReqOrderShippingPrice);	// 주문시배송비
			resultMap.put("prevPaymentShippingPrice"	, 0);							// 이전결제배송비
			return resultMap;
		}

		// 판매자귀책 && 전체 수량 반품일 경우 주문시 결제한 배송비 환불
		int shippingPrice = (isSellerFlag && qty == 0) ? orgShippingPrice : 0;
		int addShippingPrice = 0;
		int returnRefundShippingPrice = 0;

		// 현재가 판매자 귀책일 때
		if (isSellerFlag) {
			// 이전 귀책구분이 구매자 이면 계산할 배송비 정보가 없음 : 회수 여부에 상관없이
			if (OrderClaimEnums.ClaimTargetTp.TARGET_BUYER.getCode().equals(prevTargetTp)) {
				//shippingPrice += prevAddPaymentPrice;
			}
		}
		// 현재가 구매자 귀책일 때
		else {
			shippingPrice = 0;
			// 이전 귀책구분이 판매자 이면
			if (OrderClaimEnums.ClaimTargetTp.TARGET_SELLER.getCode().equals(prevTargetTp)) {
				// 배송비 재계산
				return this.getReturnPriceInfo(conditionTp, shippingPriceDto, prevAddPaymentPrice, claimShippingPrice, goodsClaimQty, orgShippingPrice,
						amountShippingPrice, sendRecvZipCd, isSellerFlag, isReturnYn, qty);
			}
			// 이전 귀책구분이 구매자 이면
			else {
				// 이전 결제한 금액 기준으로 ..
				// 이전 회수 여부가 회수이고 현재 회수 여부가 회수 안함일 경우
				if (ClaimEnums.ReturnsYn.RETURNS_YN_Y.getCode().equals(prevReturnsYn) && !isReturnYn) {
					// 1. 계산된 배송비를 얻는다.
					// 2. 이전 결제된 배송비와 현재 배송비의 차이만큼 배송비 환불처리 해준다
					// 배송비 재계산
					resultMap = this.getReturnPriceInfo(conditionTp, shippingPriceDto, prevAddPaymentPrice, claimShippingPrice, goodsClaimQty, orgShippingPrice,
							amountShippingPrice, sendRecvZipCd, false, true, qty);
					addShippingPrice = resultMap.get("addShippingPrice");

					// 배송비 재계산
					resultMap = this.getReturnPriceInfo(conditionTp, shippingPriceDto, prevAddPaymentPrice, claimShippingPrice, goodsClaimQty, orgShippingPrice,
							amountShippingPrice, sendRecvZipCd, false, false, qty);
					shippingPrice = resultMap.get("shippingPrice");
					// 추가로 환불해야 하는 배송비가 존재할 경우 재계산한 배송비와 이전 계산된 배송비의 차이 만큼 차감해준다
					if (addShippingPrice != resultMap.get("addShippingPrice")) {
						addShippingPrice -= resultMap.get("addShippingPrice");
					}
					//					if(prevAddPaymentPrice > 0) {
					//						returnRefundShippingPrice = prevAddPaymentPrice - resultMap.get("addShippingPrice");
					//					}
					//					int priceGap = prevAddPaymentPrice - resultMap.get("addShippingPrice");
					//					shippingPrice += priceGap;
				}
				// 이전 회수 여부가 회수안함이고 현재 회수 여부가 회수 일 경우
				else if (ClaimEnums.ReturnsYn.RETURNS_YN_N.getCode().equals(prevReturnsYn) && isReturnYn) {
					// 1. 계산된 배송비를 얻는다.
					// 2. 이전 결제된 배송비와 현재 배송비의 차이만큼 배송비를 부과한다
					// 배송비 재계산
					resultMap = this.getReturnPriceInfo(conditionTp, shippingPriceDto, prevAddPaymentPrice, claimShippingPrice, goodsClaimQty, orgShippingPrice,
							amountShippingPrice, sendRecvZipCd, false, false, qty);
					addShippingPrice = resultMap.get("addShippingPrice");

					resultMap = this.getReturnPriceInfo(conditionTp, shippingPriceDto, prevAddPaymentPrice, claimShippingPrice, goodsClaimQty, orgShippingPrice,
							amountShippingPrice, sendRecvZipCd, false, true, qty);
					shippingPrice = resultMap.get("shippingPrice");
					// 추가로 결제해야 하는 배송비가 존재할 경우 재계산한 배송비와 이전 계산된 배송비의 차이 만큼 합산해준다
					if (addShippingPrice != resultMap.get("addShippingPrice")) {
						addShippingPrice += (resultMap.get("addShippingPrice") - addShippingPrice);
					}
					//					if(prevAddPaymentPrice > 0) {
					//						returnRefundShippingPrice = prevAddPaymentPrice - resultMap.get("addShippingPrice");
					//					}
					//					int priceGap = resultMap.get("addShippingPrice") - prevAddPaymentPrice;
					//					addShippingPrice += priceGap;
					//					shippingPrice -= priceGap;
				}
			}
		}
		log.debug("반품 최종 배송비 2 :: <{}>, addShippingPrice :: <{}>, prevAddPaymentPrice :: <{}>", shippingPrice, addShippingPrice, prevAddPaymentPrice);
		resultMap.put("shippingPrice"	, shippingPrice);					// 배송비
		resultMap.put("orderShippingPrice"	, isSellerFlag && (qty == 0) ? orgShippingPrice : 0);	// 주문시배송비
		resultMap.put("addShippingPrice", addShippingPrice);				// 추가배송비 + 도서산간 추가 배송비
		resultMap.put("addPaymentShippingPrice", prevAddPaymentPrice); 		// 추가 결제한 배송비
		resultMap.put("prevPaymentShippingPrice", prevAddPaymentPrice); 	// 추가 기결제한 배송비
		resultMap.put("returnRefundShippingPrice", returnRefundShippingPrice); 	// 반품 시 돌려줘야할 환불 배송비
		return resultMap;
	}

	/**
	 * 반품 환불 금액 계산
	 * @param conditionTp
	 * @param shippingPriceDto
	 * @param prevAddPaymentPrice
	 * @param claimShippingPrice
	 * @param goodsClaimQty
	 * @param orgShippingPrice
	 * @param amountShippingPrice
	 * @param sendRecvZipCd
	 * @param isSellerFlag
	 * @param isReturnYn
	 * @param qty
	 * @return
	 */
	public Map<String, Integer> getReturnPriceInfo(String conditionTp,
												   ShippingDataResultVo shippingPriceDto,
												   int prevAddPaymentPrice,
												   int claimShippingPrice,
												   int goodsClaimQty,
												   int orgShippingPrice,
												   int amountShippingPrice,
												   String sendRecvZipCd,
												   boolean isSellerFlag,
												   boolean isReturnYn,
												   int qty) {
		Map<String, Integer> resultMap = new HashMap<>();
		log.debug("1. 반품 환불 금액 계산 --------------------");

		int shippingPrice = isSellerFlag && (qty == 0) ? orgShippingPrice : 0;
		int addShippingPrice = 0;
		int areaShippingPrice = this.getShippingArea(shippingPriceDto, sendRecvZipCd);
		log.debug("귀책구분 :: <{}>, 보내는사람 우편번호 :: <{}>, 반품 회수 여부 :: <{}>, 추가 배송비 :: <{}>, 도서산간배송비 :: <{}>", isSellerFlag, sendRecvZipCd, isReturnYn, addShippingPrice, areaShippingPrice);
		log.debug("배송정책 배송비 :: <{}>, 계산된 배송비 :: <{}>, 원거래 배송비 :: <{}>", shippingPriceDto.getShippingPrice(), amountShippingPrice, shippingPrice);
		// 반품 회수 안할 경우 그대로 리턴
		log.debug("1-1. 배송 정책 클레임 배송비 :: <{}>, 이전 추가 결제 배송비 :: <{}>", claimShippingPrice, prevAddPaymentPrice);
		if (GoodsEnums.ConditionType.TYPE1.getCode().equals(conditionTp)) { // 무료 배송비
			log.debug("2. 반품 무료배송비 계산");
			// 판매자 귀책일경우 배송비 부과 안함
			if (isSellerFlag) {
				addShippingPrice = 0;
			}
			// 구매자 귀책일 경우
			else {
				// 회수 안할 경우 클레임 배송비
				if(!isReturnYn) {
					addShippingPrice = claimShippingPrice + areaShippingPrice;
					shippingPrice += addShippingPrice * -1;
				}
				// 회수 할 경우 왕복 클레임 배송비
				else {
					// 기 결제 배송비가 존재할 경우
					if(prevAddPaymentPrice > 0) {
						log.debug("2-1. 구매자귀책 > 회수 > 기결제배송비존재");
						addShippingPrice = claimShippingPrice + areaShippingPrice;
						shippingPrice += addShippingPrice * -1;
					}
					else {
						log.debug("2-1. 구매자귀책 > 회수 > 기결제배송비 미존재");
						addShippingPrice = (claimShippingPrice * 2) + areaShippingPrice;
						shippingPrice += addShippingPrice * -1;
					}
				}
			}
		}
		else if (GoodsEnums.ConditionType.TYPE2.getCode().equals(conditionTp)) { // 고정 배송비
			log.debug("3. 반품 고정배송비 계산");
			log.debug("고정 클레임 배송비 :: <{}>, 추가 배송비 :: <{}>, 배송비 :: <{}>, 원결제 배송비 :: <{}>", claimShippingPrice, addShippingPrice, shippingPrice, orgShippingPrice);
			// 판매자 귀책일경우 배송비 부과 안함
			if (isSellerFlag) {
				addShippingPrice = 0;
			}
			// 구매자 귀책일 경우
			else {
				addShippingPrice = claimShippingPrice + areaShippingPrice;
				shippingPrice += addShippingPrice * -1;
			}
		}
		else if (GoodsEnums.ConditionType.TYPE3.getCode().equals(conditionTp)) { // 결제금액당 배송비

			log.debug("4. 반품 결제금액당 배송비 계산");
			// 원결제 배송비 구한다
			log.debug("------------------------------------- 원결제 배송비 <{}>------------------------------", orgShippingPrice);

			// 해당 배송정책 모든 상품 금액 - 선택한 상품 금액 으로 배송비를 계산한다
			log.debug("판매자 귀책 여부 :: <{}>", isSellerFlag);
			// 판매자 귀책일경우 배송비 부과 안함
			if (isSellerFlag) {
				addShippingPrice = 0;
			}
			// 구매자 귀책일 경우
			else {
				// 원결제가 무료배송일 경우
				if(orgShippingPrice == 0) {
					log.debug("원결제 무료배송");
					// 원결제 무료배송인데 ... 현재는 배송비 발생
					if(amountShippingPrice > 0) {
						log.debug("4-1. 구매자귀책 > 원결제가 무료배송 > 회수 > 현재는 배송비 발생");
						// 회수 할 경우
						if (isReturnYn) {
							// 기 결제 배송비 존재할 경우
							if(prevAddPaymentPrice > 0) {
								log.debug("4-1. 구매자귀책 > 원결제가 무료배송 > 회수 > 기결제배송비존재");
//								addShippingPrice -= addPaymentShippingPrice;
								addShippingPrice = claimShippingPrice + areaShippingPrice;
							}
							else {
								log.debug("4-1. 구매자귀책 > 원결제가 무료배송 > 회수 > 기결제배송비미존재");
								// 현재 계산된 배송비 + 클레임 배송비 + 도서산간배송비
								addShippingPrice = amountShippingPrice + claimShippingPrice + areaShippingPrice;
							}
							shippingPrice += addShippingPrice * -1;
						}
						// 회수 안할 경우 클레임 배송비
						else {
							if(prevAddPaymentPrice > 0) {
								log.debug("4-2. 구매자귀책 > 원결제가 무료배송 > 회수안함 > 기결제배송비존재");
							}
							else {
								log.debug("4-2. 구매자귀책 > 원결제가 무료배송 > 회수안함 > 기결제배송비미존재");
							}
							addShippingPrice = claimShippingPrice + areaShippingPrice;
							shippingPrice += addShippingPrice * -1;
						}
					}
					else {
						log.debug("4-1. 구매자귀책 > 원결제가 무료배송 > 회수 > 현재 배송비 없음");
						// 회수 할 경우
						if (isReturnYn) {
							log.debug("4-2. 구매자귀책 > 원결제 무료배송 > 회수");
							if(prevAddPaymentPrice > 0) {
								log.debug("4-3. 구매자귀책 > 원결제 무료배송 > 회수 > 기결제배송비존재");
								addShippingPrice = claimShippingPrice + areaShippingPrice;
							}
							else {
								log.debug("4-3. 구매자귀책 > 원결제 무료배송 > 회수 > 기결제배송비미존재");
								// 전체 취소일 경우 :: 배송정책의 배송비 + 클레임배송비 + 도서산간 배송비
								// 부분 취소일 경우 :: 클레임 배송비 + 도서산간 배송비
								addShippingPrice = claimShippingPrice + (qty == 0 ? shippingPriceDto.getShippingPrice() : 0) + areaShippingPrice;
							}
						}
						// 회수 안할 경우 클레임 배송비
						else {
							log.debug("4-2. 구매자귀책 > 원결제 무료배송 > 회수 안함");
							addShippingPrice = claimShippingPrice + areaShippingPrice;
						}
						shippingPrice += addShippingPrice * -1;
					}
				}
				// 원결제가 무료배송이 아닐 경우
				else {
					log.debug("4-1. 구매자귀책 > 원결제 유료배송");
					if(prevAddPaymentPrice > 0) {
						log.debug("4-1. 구매자귀책 > 원결제 유료배송 > 기결제배송비 존재");
					}
					else {
						log.debug("4-1. 구매자귀책 > 원결제 유료배송 > 기결제배송비 미존재");
					}
					addShippingPrice = claimShippingPrice + areaShippingPrice;
					shippingPrice += addShippingPrice * -1;
				}
			}
		}
		else if (GoodsEnums.ConditionType.TYPE5.getCode().equals(conditionTp)) { // 상품1개단위별 배송비
			if(goodsClaimQty > 0) {
				shippingPrice = amountShippingPrice;
				orgShippingPrice = amountShippingPrice;
				// 판매자 귀책일 경우
				if(isSellerFlag) {
					addShippingPrice = 0;
				}
				// 구매자 귀책일 경우
				else {
					// 회수 할 경우
					if(isReturnYn) {
						// 수량만큼 클레임 배송비 부과
						addShippingPrice = (claimShippingPrice * goodsClaimQty) + areaShippingPrice;
					}
					// 회수 안할 경우 클레임 배송비
					else {
						addShippingPrice = claimShippingPrice + areaShippingPrice;
					}
					shippingPrice += addShippingPrice * -1;
				}
			}
		}

		log.debug("반품 최종 배송비 :: <{}>, addShippingPrice :: <{}>, prevAddPaymentPrice :: <{}>", shippingPrice, addShippingPrice, prevAddPaymentPrice);
		resultMap.put("shippingPrice"	, shippingPrice);					// 배송비
		resultMap.put("orderShippingPrice"	, isSellerFlag && (qty == 0) ? orgShippingPrice : 0);	// 주문시배송비
		resultMap.put("addShippingPrice", addShippingPrice);				// 추가배송비 + 도서산간 추가 배송비
		resultMap.put("addPaymentShippingPrice", prevAddPaymentPrice); 		// 추가 결제한 배송비
		resultMap.put("prevPaymentShippingPrice", prevAddPaymentPrice); 	// 추가 기결제한 배송비
		return resultMap;
	}

	/**
	 *  배송 정책 정보 얻기
	 * @param ilShippingId
	 * @return
	 * @throws Exception
	 */
	public ShippingDataResultVo getShippingDataResult(long ilShippingId) {

		ShippingDataResultVo shippingDataResultVo = null;

		try {
			shippingDataResultVo = goodsShippingTemplateBiz.getShippingInfoByShippingTmplId(ilShippingId);
		}
		catch(Exception e) {
			log.error(e.getMessage());
		}

		return shippingDataResultVo;
	}

	/**
	 *  배송비 계산
	 * @param shippingDataResultVo
	 * @param goodsPrice
	 * @param orderCnt
	 * @param recvZipCd
	 * @return
	 * @throws Exception
	 */
	public ShippingPriceResponseDto getGoodsShippingPriceInfo(ShippingDataResultVo shippingDataResultVo, int goodsPrice, int orderCnt, String recvZipCd) {

		ShippingPriceResponseDto shippingPriceDto = null;

		try {
			shippingPriceDto = goodsShippingTemplateBiz.getShippingPrice(shippingDataResultVo, goodsPrice, orderCnt, recvZipCd);
		}
		catch(Exception e) {
			log.error(e.getMessage());
		}

		return shippingPriceDto;
	}

	/**
	 * 도서산관 및 제주 배송 정보 조회
	 */
	public int getShippingArea(ShippingDataResultVo shippingDataResultVo, String zipCode) {

		 int areaShippingPrice = 0;

		try {
			ShippingAreaVo shippingAreaVo = goodsShippingTemplateBiz.getShippingArea(zipCode);

			if(OrderClaimEnums.AllTypeYn.ALL_TYPE_Y.getCode().equals(shippingAreaVo.getIslandYn())) {
				areaShippingPrice = shippingDataResultVo.getIslandShippingPrice();
			}
			else if(OrderClaimEnums.AllTypeYn.ALL_TYPE_Y.getCode().equals(shippingAreaVo.getJejuYn())) {
				areaShippingPrice = shippingDataResultVo.getJejuShippingPrice();
			}
		}
		catch(Exception e) {
			log.error(e.getMessage());
		}
		return areaShippingPrice;
	}
}
