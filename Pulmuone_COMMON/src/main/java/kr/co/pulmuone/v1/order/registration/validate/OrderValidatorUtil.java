package kr.co.pulmuone.v1.order.registration.validate;

import java.util.List;

import kr.co.pulmuone.v1.comm.enums.OrderEnums;
import kr.co.pulmuone.v1.comm.enums.ShoppingEnums;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import kr.co.pulmuone.v1.comm.enums.GoodsEnums;
import kr.co.pulmuone.v1.comm.enums.GoodsEnums.GoodsDeliveryType;
import kr.co.pulmuone.v1.comm.enums.OrderEnums.OrderStatusDetailType;
import kr.co.pulmuone.v1.comm.util.StringUtil;
import kr.co.pulmuone.v1.goods.goods.dto.ArrivalScheduledDateDto;
import kr.co.pulmuone.v1.order.order.dto.vo.OrderDetlVo;
import kr.co.pulmuone.v1.order.registration.dto.OrderBindOrderDetlPackDto;

/**
 * <PRE>
 * Forbiz Korea
 * 주문 로직 체크 관련 Class
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :   작성일                :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 *  1.0    2021. 01. 07.            이명수         최초작성
 * =======================================================================
 * </PRE>
 */
@Slf4j
@Service
public class OrderValidatorUtil {

	/**
	 * 주문상세 패키지 목록 금액 비율 별 계산
	 * @param orderDetlPackList
	 * @param subPackList
	 * @param arrivalScheduledDateDto
	 * @param paidPrice
	 * @param orderCnt
	 */
	public void makeOrderDetlPacklist(List<OrderBindOrderDetlPackDto> orderDetlPackList, List<OrderDetlVo> subPackList, ArrivalScheduledDateDto arrivalScheduledDateDto, int salePrice, int paidPrice, int orderCnt, boolean isDawnDelivery, String collectionMallDetailId, String outmallDetailId, String outmallType) {
		makeOrderDetlPacklist(orderDetlPackList, subPackList, arrivalScheduledDateDto, salePrice, paidPrice, orderCnt, isDawnDelivery, collectionMallDetailId, outmallDetailId, outmallType, 0);
	}
	public void makeOrderDetlPacklist(List<OrderBindOrderDetlPackDto> orderDetlPackList, List<OrderDetlVo> subPackList, ArrivalScheduledDateDto arrivalScheduledDateDto, int salePrice, int paidPrice, int orderCnt, boolean isDawnDelivery, String collectionMallDetailId, String outmallDetailId, String outmallType, int prdSeq) {
		double totalSalePrice	= salePrice;
		double totalPaidPrice	= paidPrice;
		double totalPrice			= paidPrice; //subPackList.stream().filter(x -> !GoodsEnums.GoodsType.GIFT.getCode().equals(x.getGoodsTpCd())).mapToInt(x -> x.getPaidPrice()).sum();
		double prevPrice			= Integer.MIN_VALUE;
		int targetIndex			= 0;
		int packOrderCnt		= 0;
		double totalGoodsPrice 	= 0;

		for(int i=0; i<subPackList.size(); i++) {
			OrderDetlVo packItem = subPackList.get(i);
			if (!GoodsEnums.GoodsType.GIFT.getCode().equals(packItem.getGoodsTpCd()) && !GoodsEnums.GoodsType.GIFT_FOOD_MARKETING.getCode().equals(packItem.getGoodsTpCd())) {
				totalGoodsPrice += (packItem.getRecommendedPrice()*packItem.getOrderCnt());
			}
		}

		int targetNum = 0;
		int packCnt = subPackList.size();
		for(int i=0; i<packCnt; i++) {

			OrderDetlVo packItem = subPackList.get(i);
			OrderDetlVo newPackItem = new OrderDetlVo();


			log.info("packItem.getSalePrice() : " + packItem.getSalePrice());

			newPackItem.setIlGoodsShippingTemplateId(packItem.getIlGoodsShippingTemplateId());
			newPackItem.setUrWarehouseId(packItem.getUrWarehouseId());
			newPackItem.setUrSupplierId(packItem.getUrSupplierId());
			newPackItem.setIlGoodsReserveOptnId(packItem.getIlGoodsReserveOptnId());
			newPackItem.setEvExhibitId(packItem.getEvExhibitId());
			newPackItem.setPromotionTp(packItem.getPromotionTp());
			newPackItem.setOrderStatusCd(packItem.getOrderStatusCd());
			newPackItem.setUrWarehouseGrpCd(packItem.getUrWarehouseGrpCd());
			newPackItem.setStorageTypeCd(packItem.getStorageTypeCd());
			newPackItem.setGoodsTpCd(packItem.getGoodsTpCd());
			newPackItem.setSaleTpCd(packItem.getSaleTpCd());
			newPackItem.setIlCtgryStdId(packItem.getIlCtgryStdId());
			newPackItem.setIlCtgryDisplayId(packItem.getIlCtgryDisplayId());
			newPackItem.setIlCtgryMallId(packItem.getIlCtgryMallId());
			newPackItem.setItemBarcode(packItem.getItemBarcode());
			newPackItem.setIlItemCd(packItem.getIlItemCd());
			newPackItem.setIlGoodsId(packItem.getIlGoodsId());
			newPackItem.setGoodsNm(packItem.getGoodsNm());
			newPackItem.setTaxYn(packItem.getTaxYn());
			newPackItem.setGoodsDiscountTp(packItem.getGoodsDiscountTp());
			newPackItem.setOrderCnt(packItem.getOrderCnt());
			newPackItem.setCancelCnt(packItem.getCancelCnt());

			newPackItem.setStandardPrice(packItem.getStandardPrice());
			newPackItem.setRecommendedPrice(packItem.getRecommendedPrice());
			newPackItem.setSalePrice(packItem.getSalePrice());
			newPackItem.setTotSalePrice(packItem.getTotSalePrice());
			newPackItem.setCartCouponPrice(packItem.getCartCouponPrice());
			newPackItem.setGoodsCouponPrice(packItem.getGoodsCouponPrice());
			newPackItem.setDirectPrice(packItem.getDirectPrice());
			newPackItem.setPaidPrice(packItem.getPaidPrice());
			newPackItem.setRedeliveryType(packItem.getRedeliveryType());
			newPackItem.setOutmallType(packItem.getOutmallType());
			newPackItem.setPmAdExternalCd(packItem.getPmAdExternalCd());
			newPackItem.setPmAdInternalPageCd(packItem.getPmAdInternalPageCd());
			newPackItem.setPmAdInternalContentCd(packItem.getPmAdInternalContentCd());

			newPackItem.setOutmallType(outmallType);

			packOrderCnt = StringUtil.nvlInt(orderCnt)*packItem.getOrderCnt();

			if(!GoodsEnums.GoodsType.GIFT.getCode().equals(newPackItem.getGoodsTpCd()) && !GoodsEnums.GoodsType.GIFT_FOOD_MARKETING.getCode().equals(newPackItem.getGoodsTpCd())) {
				/*
				int rate = (int) (packItem.getPaidPrice() / (double) totalPrice * 100);

				System.out.println("rate : " + rate);
				int packPaidPrice = (int) (rate / (double) 100 * paidPrice);

				System.out.println("packPaidPrice : " + packPaidPrice);

				packItem.setPaidPrice(packPaidPrice);
				modPrice -= packPaidPrice;

				System.out.println("modPrice : " + modPrice);

				System.out.println("packItem : " + packItem.toString());
				*/


				double goodsPrice = (newPackItem.getRecommendedPrice()*newPackItem.getOrderCnt());
				double packPaidPrice = Double.parseDouble(String.format("%.8f", totalPrice * (goodsPrice / totalGoodsPrice)));
				double packSalePrice = Double.parseDouble(String.format("%.8f", packPaidPrice / packOrderCnt));


				if(prevPrice < packSalePrice) {
					prevPrice = packSalePrice;
					targetIndex = targetNum;
				}


				newPackItem.setSalePrice(packSalePrice);
				newPackItem.setPaidPrice((int) packPaidPrice);
				newPackItem.setTotSalePrice((int) packPaidPrice);

				totalSalePrice	= totalSalePrice - packSalePrice;
				totalPaidPrice	= totalPaidPrice - packPaidPrice;



				targetNum++;
			} else {
				newPackItem.setSalePrice(0);
				newPackItem.setPaidPrice(0);
				newPackItem.setTotSalePrice(0);
			}

			//String goodsDeliveryType = getGoodsDeliveryType(deliveryDto.getDeliveryType(), shippingDto.getDawnDeliveryYn());
			//String orderStatusDetailType = getOrderStatusDetailType(goodsDto.getGoodsType(), deliveryDto.getDeliveryType(), shippingDto.getDawnDeliveryYn());

			newPackItem.setOrderCnt(packOrderCnt);
			newPackItem.setOrderIfDt(arrivalScheduledDateDto.getOrderDate());
			newPackItem.setShippingDt(arrivalScheduledDateDto.getForwardingScheduledDate());
			newPackItem.setDeliveryDt(arrivalScheduledDateDto.getArrivalScheduledDate());
			newPackItem.setCollectionMallDetailId(collectionMallDetailId);
			newPackItem.setOutmallDetailId(outmallDetailId);
			newPackItem.setPrdSeq(prdSeq);



			if (isDawnDelivery) {
				newPackItem.setGoodsDeliveryType(GoodsDeliveryType.DAWN.getCode());
				newPackItem.setOrderStatusDeliTp(OrderStatusDetailType.DAWN.getCode());
			} else {
				newPackItem.setGoodsDeliveryType(GoodsDeliveryType.NORMAL.getCode());
				newPackItem.setOrderStatusDeliTp(OrderStatusDetailType.NORMAL.getCode());
			}
			orderDetlPackList.add(OrderBindOrderDetlPackDto.builder()
					.orderDetlVo(newPackItem)
					.orderDetlDiscountList(null)
					.orderDetlDailyVo(null)
					.build()
			);
		}





		if (totalSalePrice > 0){
			//orderDetlPackList.get(targetIndex).getOrderDetlVo().setSalePrice(orderDetlPackList.get(targetIndex).getOrderDetlVo().getSalePrice() + totalSalePrice);
		} else if (totalSalePrice < 0){
			//orderDetlPackList.get(targetIndex).getOrderDetlVo().setSalePrice(orderDetlPackList.get(targetIndex).getOrderDetlVo().getPaidPrice() - Math.abs(totalSalePrice));
		}

		if(totalPaidPrice > 0){
			orderDetlPackList.get(targetIndex).getOrderDetlVo().setPaidPrice(orderDetlPackList.get(targetIndex).getOrderDetlVo().getPaidPrice() + (int)totalPaidPrice);
			orderDetlPackList.get(targetIndex).getOrderDetlVo().setTotSalePrice(orderDetlPackList.get(targetIndex).getOrderDetlVo().getTotSalePrice() + (int)totalPaidPrice);
		} else if(totalPaidPrice < 0){
			orderDetlPackList.get(targetIndex).getOrderDetlVo().setPaidPrice(orderDetlPackList.get(targetIndex).getOrderDetlVo().getPaidPrice() - Math.abs((int)totalPaidPrice));
			orderDetlPackList.get(targetIndex).getOrderDetlVo().setTotSalePrice(orderDetlPackList.get(targetIndex).getOrderDetlVo().getTotSalePrice() - Math.abs((int)totalPaidPrice));
		}


		int resultPaidPrice = orderDetlPackList.stream().map(m -> m.getOrderDetlVo().getPaidPrice()).mapToInt(Integer::intValue).sum();
		int modPrice = paidPrice - resultPaidPrice;



		if(modPrice > 0) {
			orderDetlPackList.get(0).getOrderDetlVo().setPaidPrice(orderDetlPackList.get(0).getOrderDetlVo().getPaidPrice() + modPrice);
			orderDetlPackList.get(0).getOrderDetlVo().setTotSalePrice(orderDetlPackList.get(0).getOrderDetlVo().getTotSalePrice() + modPrice);
		}
		else if(modPrice < 0) {
			orderDetlPackList.get(0).getOrderDetlVo().setPaidPrice(orderDetlPackList.get(0).getOrderDetlVo().getPaidPrice() - Math.abs(modPrice));
			orderDetlPackList.get(0).getOrderDetlVo().setTotSalePrice(orderDetlPackList.get(0).getOrderDetlVo().getTotSalePrice() - Math.abs(modPrice));
		}




	}


	private String getGoodsDeliveryType(String deliveryTypeCode, String dawnDeliveryYn) throws Exception {
		GoodsEnums.GoodsDeliveryType goodsDeliveryType = GoodsEnums.GoodsDeliveryType.NORMAL;
		ShoppingEnums.DeliveryType deliveryType = ShoppingEnums.DeliveryType.findByCode(deliveryTypeCode);

		if(deliveryType.getCode().equals(ShoppingEnums.DeliveryType.RENTAL.getCode())) {
			goodsDeliveryType = GoodsEnums.GoodsDeliveryType.NORMAL;
		} else if (deliveryType.getCode().equals(ShoppingEnums.DeliveryType.DAILY.getCode())) {
			goodsDeliveryType = GoodsEnums.GoodsDeliveryType.DAILY;
		} else if ("Y".equals(dawnDeliveryYn)) {
			goodsDeliveryType = GoodsEnums.GoodsDeliveryType.DAWN;
		} else if (ShoppingEnums.DeliveryType.SHOP_DELIVERY.getCode().equals(deliveryTypeCode)) {
			goodsDeliveryType = GoodsEnums.GoodsDeliveryType.SHOP;
		} else if (ShoppingEnums.DeliveryType.SHOP_PICKUP.getCode().equals(deliveryTypeCode)) {
			goodsDeliveryType = GoodsEnums.GoodsDeliveryType.SHOP;
		}

		return goodsDeliveryType.getCode();
	}

	private String getOrderStatusDetailType(String goodsTypeCode, String deliveryTypeCode, String dawnDeliveryYn) {
		OrderEnums.OrderStatusDetailType type = null;
		if (GoodsEnums.GoodsType.INCORPOREITY.getCode().equals(goodsTypeCode)) {
			type = OrderEnums.OrderStatusDetailType.BOX;
		} else if (GoodsEnums.GoodsType.RENTAL.getCode().equals(goodsTypeCode)) {
			type = OrderEnums.OrderStatusDetailType.RENTAL;
		} else if (ShoppingEnums.DeliveryType.DAILY.getCode().equals(deliveryTypeCode)) {
			type = OrderEnums.OrderStatusDetailType.DAILY;
		} else if ("Y".equals(dawnDeliveryYn)) {
			type = OrderEnums.OrderStatusDetailType.DAWN;
		} else if (ShoppingEnums.DeliveryType.SHOP_DELIVERY.getCode().equals(deliveryTypeCode)) {
			type = OrderEnums.OrderStatusDetailType.SHOP_DELIVERY;
		} else if (ShoppingEnums.DeliveryType.SHOP_PICKUP.getCode().equals(deliveryTypeCode)) {
			type = OrderEnums.OrderStatusDetailType.SHOP_PICKUP;
		} else {
			type = OrderEnums.OrderStatusDetailType.NORMAL;
		}
		return type.getCode();
	}
}
