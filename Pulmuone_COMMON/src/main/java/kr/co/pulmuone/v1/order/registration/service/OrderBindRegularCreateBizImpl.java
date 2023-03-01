package kr.co.pulmuone.v1.order.registration.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import kr.co.pulmuone.v1.comm.constants.Constants;
import kr.co.pulmuone.v1.order.regular.dto.RegularShippingPriceInfoDto;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.pulmuone.v1.comm.constants.OrderShippingConstants;
import kr.co.pulmuone.v1.comm.enums.GoodsEnums;
import kr.co.pulmuone.v1.comm.enums.GoodsEnums.GoodsDiscountType;
import kr.co.pulmuone.v1.comm.enums.OrderEnums;
import kr.co.pulmuone.v1.comm.enums.SellersEnums;
import kr.co.pulmuone.v1.comm.util.PriceUtil;
import kr.co.pulmuone.v1.goods.goods.dto.ArrivalScheduledDateDto;
import kr.co.pulmuone.v1.goods.goods.service.GoodsGoodsBiz;
import kr.co.pulmuone.v1.goods.goods.service.GoodsShippingTemplateBiz;
import kr.co.pulmuone.v1.order.order.dto.vo.OrderDetlDiscountVo;
import kr.co.pulmuone.v1.order.order.dto.vo.OrderDetlVo;
import kr.co.pulmuone.v1.order.order.dto.vo.OrderDtVo;
import kr.co.pulmuone.v1.order.order.dto.vo.OrderPaymentVo;
import kr.co.pulmuone.v1.order.order.dto.vo.OrderShippingPriceVo;
import kr.co.pulmuone.v1.order.order.dto.vo.OrderShippingZoneVo;
import kr.co.pulmuone.v1.order.order.dto.vo.OrderShippingZoneVo.OrderShippingZoneVoBuilder;
import kr.co.pulmuone.v1.order.order.dto.vo.OrderVo;
import kr.co.pulmuone.v1.order.order.dto.vo.OrderVo.OrderVoBuilder;
import kr.co.pulmuone.v1.order.registration.dto.OrderBindDto;
import kr.co.pulmuone.v1.order.registration.dto.OrderBindOrderDetlDto;
import kr.co.pulmuone.v1.order.registration.dto.OrderBindShippingPriceDto;
import kr.co.pulmuone.v1.order.registration.dto.OrderBindShippingZoneDto;
import kr.co.pulmuone.v1.order.regular.dto.RegularResultCreateOrderGoodsListDto;
import kr.co.pulmuone.v1.order.regular.dto.RegularResultCreateOrderListDto;
import kr.co.pulmuone.v1.order.regular.service.OrderRegularDetailBiz;
import kr.co.pulmuone.v1.store.warehouse.service.StoreWarehouseBiz;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service("orderBindRegularCreateBiz")
public class OrderBindRegularCreateBizImpl implements OrderBindBiz<RegularResultCreateOrderGoodsListDto> {

	@Autowired
	private OrderRegularDetailBiz orderRegularBiz;

	@Autowired
	StoreWarehouseBiz storeWarehouseBiz;

    @Override
    public List<OrderBindDto> orderDataBind(RegularResultCreateOrderGoodsListDto regularResultCreateOrderGoodsListDto) throws Exception {

        List<OrderBindDto> orderBindDtoList = new ArrayList<>();

		long odRegularResultId = regularResultCreateOrderGoodsListDto.getOdRegularResultId();
		Map<Long, List<RegularResultCreateOrderListDto>> childGoodsList = regularResultCreateOrderGoodsListDto.getChildGoodsList();

        List<OrderBindShippingZoneDto> orderShippingZoneList = new ArrayList<>();	//배송지 정보
        List<OrderBindShippingPriceDto> shippingPriceList = new ArrayList<>();		//배송비 정보
		OrderVoBuilder orderVo = OrderVo.builder();
		OrderShippingZoneVoBuilder orderShippingZoneVo = OrderShippingZoneVo.builder();

		// 출고처별 주문상세
		Map<String, List<RegularResultCreateOrderListDto>> wareList = regularResultCreateOrderGoodsListDto.getGoodsList();

		// 총 금액정보
		int totalSalePrice = 0;
		int totalShippingPrice = 0;
		int totalDirectPrice = 0;
		int totalPaidPrice = 0;
		int totalTaxPrice = 0;
		int nonTaxPrice = 0;

		// 출고처별 loop
		for(String grpUrWarehouseId : wareList.keySet()) {
		//wareList.entrySet().forEach(ware -> {

			long urWarehouseId = Long.parseLong(grpUrWarehouseId.split(Constants.ARRAY_SEPARATORS)[0]);
			List<RegularResultCreateOrderListDto> goodsList = wareList.get(grpUrWarehouseId);
			// 배송정책 id List
			List<Long> ilShippingTtmplIdList =  goodsList.stream().map(x -> x.getIlShippingTmplId()).distinct().collect(Collectors.toList());

	        List<OrderBindOrderDetlDto> orderDetlList = new ArrayList<>();				//주문상세 목록

			int totGoodsPrice = 0;
			int totDiscountPrice = 0;
			int totOrderCnt = 0;
			int totPaidPrice = 0;
			int totTaxPrice = 0;
			int totNonTaxPrice = 0;
			String recvZipCd = "";

			orderVo.ilGoodsId(goodsList.get(0).getIlGoodsId());

			// 상품 목록 loop
			for(RegularResultCreateOrderListDto goodsItem : goodsList) {

				// 상품 할인정보
				List<OrderDetlDiscountVo> orderDetlDiscountList = new ArrayList<>();

				recvZipCd = goodsItem.getRecvZipCd();

				// 주문정보 Set
				orderVo.sellersGroupCd(SellersEnums.SellersGroupCd.MALL.getCode())
						.urGroupId(goodsItem.getUrGroupId())
						.urGroupNm(goodsItem.getUrGroupNm())
						.urUserId(goodsItem.getUrUserId())
						.buyerNm(goodsItem.getBuyerNm())
						.buyerHp(goodsItem.getBuyerHp())
						.buyerTel(goodsItem.getBuyerTel())
						.buyerMail(goodsItem.getBuyerMail())
						.goodsNm(goodsItem.getGoodsNm())
						.orderPaymentType(goodsItem.getPaymentTypeCd())
						.buyerTypeCd(goodsItem.getBuyerTypeCd())
						.agentTypeCd(goodsItem.getAgentTypeCd())
						.urPcidCd(goodsItem.getUrPcidCd())
						.giftYn("N")
						.orderYn("Y")
						.orderCopyYn("N")
						.orderCreateYn("N");

				// 배송지정보 Set
				orderShippingZoneVo.deliveryType(goodsItem.getDeliveryType())
								.shippingType(1)
								.recvNm(goodsItem.getRecvNm())
								.recvHp(goodsItem.getRecvHp())
								.recvTel(goodsItem.getRecvTel())
								.recvMail(goodsItem.getRecvMail())
								.recvZipCd(goodsItem.getRecvZipCd())
								.recvAddr1(goodsItem.getRecvAddr1())
								.recvAddr2(goodsItem.getRecvAddr2())
								.recvBldNo(goodsItem.getRecvBldNo())
								.deliveryMsg(goodsItem.getDeliveryMsg())
								.doorMsgCd(goodsItem.getDoorMsgCd())
								.doorMsg(goodsItem.getDoorMsg());

				// 할인정보 있을 경우 할인정보 Set
				if(!GoodsDiscountType.NONE.getCode().equals(goodsItem.getDiscountTp())) {
					OrderDetlDiscountVo orderDetlDiscountVo = OrderDetlDiscountVo.builder()
																				.discountTp(goodsItem.getDiscountTp())
																				.discountPrice((goodsItem.getRecommendedPrice() - goodsItem.getSalePrice()) * goodsItem.getOrderCnt())
																				.build();
					orderDetlDiscountList.add(orderDetlDiscountVo);
				}

				// 상품 판매 금액
				int beforeSalePrice = goodsItem.getSalePrice();
				// 정기배송 기본 할인 금액
				int afterPaymentPrice = PriceUtil.getPriceByRate(beforeSalePrice, goodsItem.getBasicDiscountRate());
				int addAfterPaymentPrice = 0;
				// 추가할인 기준 회차보다 크거나 같을 경우 추가할인 적용
				if(goodsItem.getReqRound() >= regularResultCreateOrderGoodsListDto.getAddDiscountStdReqRound()) {
					// 할인한 상품금액 정보에 추가 할인 적용
					addAfterPaymentPrice = PriceUtil.getPriceByRate(afterPaymentPrice, goodsItem.getAddDiscountRate());
				}

				int afterOrderPrice = afterPaymentPrice * goodsItem.getOrderCnt();
				int regularDiscountPrice = (beforeSalePrice - afterPaymentPrice) * goodsItem.getOrderCnt();
				// 정기배송 기본 할인금액
				if(regularDiscountPrice > 0) {
					OrderDetlDiscountVo orderDetlDiscountVo = OrderDetlDiscountVo.builder()
																					.discountTp(GoodsDiscountType.REGULAR_DEFAULT.getCode())
																					.pmCouponNm("정기배송 " + goodsItem.getBasicDiscountRate() + "% 할인")
																					.discountPrice(regularDiscountPrice)
																					.build();
					orderDetlDiscountList.add(orderDetlDiscountVo);
				}
				// 정기배송 추가 할인금액
				if(addAfterPaymentPrice > 0) {
					afterOrderPrice = addAfterPaymentPrice * goodsItem.getOrderCnt();
					regularDiscountPrice = (beforeSalePrice - addAfterPaymentPrice) * goodsItem.getOrderCnt();
					int addDiscountPrice = (afterPaymentPrice - addAfterPaymentPrice) * goodsItem.getOrderCnt();
					OrderDetlDiscountVo orderDetlDiscountVo = OrderDetlDiscountVo.builder()
																					.discountTp(GoodsDiscountType.REGULAR_ADD.getCode())
																					.pmCouponNm("정기배송 " + goodsItem.getAddDiscountRound() + "회 이상 결제 추가 " + goodsItem.getAddDiscountRate() + "% 할인")
																					.discountPrice(addDiscountPrice)
																					.build();
					orderDetlDiscountList.add(orderDetlDiscountVo);
				}

				totGoodsPrice += beforeSalePrice * goodsItem.getOrderCnt();
				totDiscountPrice += regularDiscountPrice;
				totPaidPrice += afterOrderPrice;
				if("Y".equals(goodsItem.getTaxYn())) {
					totTaxPrice += afterOrderPrice;
				} else if ("N".equals(goodsItem.getTaxYn())) {
					totNonTaxPrice += afterOrderPrice;
				}

				// 주문 상세 정보 Set
				OrderDetlVo orderDetlVo = OrderDetlVo.builder()
													.odOrderDetlDepthId(1)
													.odOrderDetlParentId(0)
													.ilGoodsShippingTemplateId(goodsItem.getIlShippingTmplId())
													.urWarehouseId(goodsItem.getUrWarehouseId())
													.urSupplierId(goodsItem.getUrSupplierId())
													.orderStatusCd(OrderEnums.OrderStatus.INCOM_READY.getCode())
													.urWarehouseGrpCd(goodsItem.getWarehouseGrpCd())
													.storageTypeCd(goodsItem.getStorageMethodTp())
													.goodsTpCd(goodsItem.getGoodsTp())
													.saleTpCd(goodsItem.getSaleTp())
													.ilCtgryStdId(goodsItem.getIlCtgryStdId())
													.ilCtgryDisplayId(goodsItem.getIlCtgryId())
													.itemBarcode(goodsItem.getItemBarcode())
													.ilItemCd(goodsItem.getIlItemCd())
													.ilGoodsId(goodsItem.getIlGoodsId())
													.goodsNm(goodsItem.getGoodsNm())
													.orderStatusDeliTp(OrderEnums.OrderStatusDetailType.NORMAL.getCode())
													.goodsDeliveryType(GoodsEnums.GoodsDeliveryType.REGULAR.getCode())
													.taxYn(goodsItem.getTaxYn())
													.orderCnt(goodsItem.getOrderCnt())
													.cancelCnt(0)
													.standardPrice(goodsItem.getStandardPrice())
													.recommendedPrice(goodsItem.getRecommendedPrice())
													.salePrice(goodsItem.getSalePrice())
													.totSalePrice(afterOrderPrice)
													.directPrice(regularDiscountPrice)
													.paidPrice(afterOrderPrice)
													.batchExecFl("N")
													.batchExecDt(null)
													.orderIfDt((!Objects.isNull(goodsItem.getOrderIfDt()) ? goodsItem.getOrderIfDt() : null))
													.shippingDt((!Objects.isNull(goodsItem.getShippingDt()) ? goodsItem.getShippingDt() : null))
													.deliveryDt((!Objects.isNull(goodsItem.getDeliveryDt()) ? goodsItem.getDeliveryDt() : null))
													.build();

				OrderBindOrderDetlDto orderBindOrderDetlDto = OrderBindOrderDetlDto.builder()
																					.orderDetlVo(orderDetlVo)
																					.orderDetlDiscountList(orderDetlDiscountList)
																					.build();

				orderDetlList.add(orderBindOrderDetlDto);

				// 추가상품 존재할 경우
				if(childGoodsList.containsKey(goodsItem.getIlGoodsId())) {

					for(RegularResultCreateOrderListDto childGoodsItem : childGoodsList.get(goodsItem.getIlGoodsId())) {

						totGoodsPrice += childGoodsItem.getSalePrice() * childGoodsItem.getOrderCnt();
						totPaidPrice += childGoodsItem.getSalePrice() * childGoodsItem.getOrderCnt();

						if("Y".equals(childGoodsItem.getTaxYn())) {
							totTaxPrice += childGoodsItem.getSalePrice() * childGoodsItem.getOrderCnt();
						} else if ("N".equals(childGoodsItem.getTaxYn())) {
							totNonTaxPrice += childGoodsItem.getSalePrice() * childGoodsItem.getOrderCnt();
						}

						// 주문 상세 정보 Set
						OrderDetlVo orderDetlSubVo = OrderDetlVo.builder()
															.odOrderDetlDepthId(2)
															.odOrderDetlParentId(goodsItem.getIlGoodsId())
															.ilGoodsShippingTemplateId(childGoodsItem.getIlShippingTmplId())
															.urWarehouseId(childGoodsItem.getUrWarehouseId())
															.urSupplierId(childGoodsItem.getUrSupplierId())
															.orderStatusCd(OrderEnums.OrderStatus.INCOM_READY.getCode())
															.urWarehouseGrpCd(childGoodsItem.getWarehouseGrpCd())
															.storageTypeCd(childGoodsItem.getStorageMethodTp())
															.goodsTpCd(childGoodsItem.getGoodsTp())
															.saleTpCd(childGoodsItem.getSaleTp())
															.ilCtgryStdId(childGoodsItem.getIlCtgryStdId())
															.ilCtgryDisplayId(childGoodsItem.getIlCtgryId())
															.itemBarcode(childGoodsItem.getItemBarcode())
															.ilItemCd(childGoodsItem.getIlItemCd())
															.ilGoodsId(childGoodsItem.getIlGoodsId())
															.goodsNm(childGoodsItem.getGoodsNm())
															.taxYn(childGoodsItem.getTaxYn())
															.orderCnt(childGoodsItem.getOrderCnt())
															.cancelCnt(0)
															.standardPrice(childGoodsItem.getStandardPrice())
															.recommendedPrice(childGoodsItem.getRecommendedPrice())
															.salePrice(childGoodsItem.getSalePrice())
															.totSalePrice(childGoodsItem.getSalePrice()*childGoodsItem.getOrderCnt())
															.build();

						OrderBindOrderDetlDto orderBindOrderDetlSubDto = OrderBindOrderDetlDto.builder()
																							.orderDetlVo(orderDetlSubVo)
																							.build();

						orderDetlList.add(orderBindOrderDetlSubDto);
					}
				}
			}

			// 배송비 계산
			int shippingPrice = 0;
			try {
				RegularShippingPriceInfoDto shippingPriceInfo = orderRegularBiz.getShippingPrice(ilShippingTtmplIdList, totGoodsPrice, totOrderCnt, recvZipCd);
				shippingPrice = shippingPriceInfo.getShippingPrice();
				OrderShippingPriceVo orderShippingPriceVo = OrderShippingPriceVo.builder()
																				.ilShippingTmplId(shippingPriceInfo.getIlShippingTmplId())
																				.paymentMethod(OrderShippingConstants.ORDER_SHIPPING_PAYMETHOD_PREPAYMENT)
																				.method(OrderShippingConstants.ORDER_SHIPPING_METHOD_DELIVERY)
																				.shippingPrice(shippingPrice)
																				.setlYn(storeWarehouseBiz.getWarehouseSetlYn(urWarehouseId).getCode())
																				.build();
				OrderBindShippingPriceDto orderBindShippingPriceDto = OrderBindShippingPriceDto.builder()
																								.orderShippingPriceVo(orderShippingPriceVo)
																								.orderDetlList(orderDetlList)
																								.build();
				shippingPriceList.add(orderBindShippingPriceDto);
			}
			catch(Exception e) {
				log.error("total shipping price error : {}", e.getMessage());
			}

			totalSalePrice += totGoodsPrice;
			totalShippingPrice += shippingPrice;
			totalDirectPrice += totDiscountPrice;
			totalPaidPrice += totPaidPrice;
			totalTaxPrice += totTaxPrice;
			nonTaxPrice += totNonTaxPrice;
		}
		//});

		OrderBindShippingZoneDto orderBindShippingZoneDto = OrderBindShippingZoneDto.builder()
																					.orderShippingZoneVo(orderShippingZoneVo.build())
																					.shippingPriceList(shippingPriceList)
																					.build();
		orderShippingZoneList.add(orderBindShippingZoneDto);

		OrderPaymentVo orderPaymentVo = OrderPaymentVo.builder()
													.salePrice(totalSalePrice)
													.directPrice(totalDirectPrice)
													.paidPrice(totalPaidPrice)
													.shippingPrice(totalShippingPrice)
													.taxablePrice(totalTaxPrice + totalShippingPrice)
													.nonTaxablePrice(nonTaxPrice)
													.paymentPrice(totalPaidPrice + totalShippingPrice)
													.build();

        OrderBindDto orderBind = OrderBindDto.builder()
											.order(orderVo.build())
											.orderDt(OrderDtVo.builder().irId(0).ibId(0).icId(0).build())
											.orderShippingZoneList(orderShippingZoneList)
											.orderPaymentVo(orderPaymentVo)
											.build();
		orderBindDtoList.add(orderBind);

        return orderBindDtoList;
    }
}
