package kr.co.pulmuone.v1.goods.goods.service;

import kr.co.pulmuone.v1.goods.goods.dto.ShippingDataResultDto;
import kr.co.pulmuone.v1.goods.goods.dto.ShippingPriceResponseDto;
import kr.co.pulmuone.v1.goods.goods.dto.vo.ShippingAreaVo;
import kr.co.pulmuone.v1.goods.goods.dto.vo.ShippingDataResultVo;

public interface GoodsShippingTemplateBiz {

	ShippingDataResultDto getShippingInfo(String saleType, Long ilGoodsId, Long urWareHouseId) throws Exception;

	ShippingDataResultVo getShippingInfoByShippingTmplId(Long ilShippingTmplId) throws Exception;

	String getShippingPriceText(ShippingDataResultVo shippingDataResultVo) throws Exception;

	ShippingPriceResponseDto getShippingPrice(ShippingDataResultVo shippingDataResultVo, int goodsPrice, int goodsQty) throws Exception;

	ShippingPriceResponseDto getShippingPrice(ShippingDataResultVo shippingDataResultVo, int goodsPrice, int goodsQty, String zipCode) throws Exception;

	ShippingAreaVo getShippingArea(String zipCode) throws Exception;

	boolean isUnDeliverableArea(String undeliverableAreaType, ShippingAreaVo shippingAreaVo) throws Exception;

	boolean isUnDeliverableArea(String undeliverableAreaType, String receiverZipCode) throws Exception;

	ShippingDataResultVo getShippingUndeliveryInfo(Long ilGoodsId, Long urWareHouseId) throws Exception;
}
