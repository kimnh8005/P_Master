package kr.co.pulmuone.v1.goods.goods.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.pulmuone.v1.goods.goods.dto.ShippingDataResultDto;
import kr.co.pulmuone.v1.goods.goods.dto.ShippingPriceResponseDto;
import kr.co.pulmuone.v1.goods.goods.dto.vo.ShippingAreaVo;
import kr.co.pulmuone.v1.goods.goods.dto.vo.ShippingDataResultVo;

@Service
public class GoodsShippingTemplateBizImpl implements GoodsShippingTemplateBiz {

	@Autowired
	private GoodsShippingTemplateService goodsShippingTemplateService;

	/**
	 * 배송정보 조회
	 *
	 * @param saleType, ilGoodsId, urWareHouseId
	 * @return ShippingDataListResultDto
	 * @throws Exception
	 */
	@Override
	public ShippingDataResultDto getShippingInfo(String saleType, Long ilGoodsId, Long urWareHouseId) throws Exception {
		return goodsShippingTemplateService.getShippingInfo(saleType, ilGoodsId, urWareHouseId);
	}

	/**
	 * 배송 정책 조회
	 */
	@Override
	public ShippingDataResultVo getShippingInfoByShippingTmplId(Long ilShippingTmplId) throws Exception {
		return goodsShippingTemplateService.getShippingInfoByShippingTmplId(ilShippingTmplId);
	}

	/**
	 * 배송비 금액 노출 문구
	 */
	@Override
	public String getShippingPriceText(ShippingDataResultVo shippingDataResultVo) throws Exception {
		return goodsShippingTemplateService.getShippingPriceText(shippingDataResultVo);
	}

	/**
	 * 배송비 조회 (지역배송비 제외)
	 */
	@Override
	public ShippingPriceResponseDto getShippingPrice(ShippingDataResultVo shippingDataResultVo, int goodsPrice,
			int goodsQty) throws Exception {
		return goodsShippingTemplateService.getShippingPrice(shippingDataResultVo, goodsPrice, goodsQty);
	}

	/**
	 * 배송비 조회 (지역배송비 포함)
	 */
	@Override
	public ShippingPriceResponseDto getShippingPrice(ShippingDataResultVo shippingDataResultVo, int goodsPrice,
			int goodsQty, String zipCode) throws Exception {
		return goodsShippingTemplateService.getShippingPrice(shippingDataResultVo, goodsPrice, goodsQty, zipCode);
	}

	/**
	 * 도서산관 및 제주 배송 정보 조회
	 */
	@Override
	public ShippingAreaVo getShippingArea(String zipCode) throws Exception {
		return goodsShippingTemplateService.getShippingArea(zipCode);
	}

	/**
	 * 주소기반 배송 가능여부
	 *
	 * @param undeliverableAreaType, receiverZipCode
	 * @return boolean
	 * @throws Exception
	 */
	@Override
	public boolean isUnDeliverableArea(String undeliverableAreaType, ShippingAreaVo shippingAreaVo) throws Exception {
		return goodsShippingTemplateService.isUnDeliverableArea(undeliverableAreaType, shippingAreaVo);
	}

	/**
	 * 주소기반 배송 가능여부
	 * @param undeliverableAreaType
	 * @param receiverZipCode
	 * @return
	 * @throws Exception
	 */
	public boolean isUnDeliverableArea(String undeliverableAreaType, String receiverZipCode) throws Exception {
		return goodsShippingTemplateService.isUnDeliverableArea(undeliverableAreaType, goodsShippingTemplateService.getShippingArea(receiverZipCode));
	}

	/**
	 * 배송불가 배송정책 정보
	 * @param ilGoodsId
	 * @param urWareHouseId
	 * @return
	 * @throws Exception
	 */
	public ShippingDataResultVo getShippingUndeliveryInfo(Long ilGoodsId, Long urWareHouseId) throws Exception {
		return goodsShippingTemplateService.getShippingUndeliveryInfo(ilGoodsId, urWareHouseId);
	}
}
