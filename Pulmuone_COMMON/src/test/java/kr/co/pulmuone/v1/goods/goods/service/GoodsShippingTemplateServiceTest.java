package kr.co.pulmuone.v1.goods.goods.service;

import kr.co.pulmuone.v1.comm.CommonServiceTestBaseForJunit5;
import kr.co.pulmuone.v1.comm.enums.GoodsEnums;
import kr.co.pulmuone.v1.goods.goods.dto.ShippingDataResultDto;
import kr.co.pulmuone.v1.goods.goods.dto.ShippingPriceResponseDto;
import kr.co.pulmuone.v1.goods.goods.dto.vo.ShippingAreaVo;
import kr.co.pulmuone.v1.goods.goods.dto.vo.ShippingDataResultVo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.*;

class GoodsShippingTemplateServiceTest extends CommonServiceTestBaseForJunit5{

	@Autowired
	private GoodsShippingTemplateService goodsShippingTemplateService;

	@Test
	void getShippingInfo_성공() throws Exception{
		String saleType = "SALE_TYPE.NORMAL";
		Long ilGoodsId = 175L;
		Long urWareHouseId = 4L;

		ShippingDataResultDto shippingInfo = goodsShippingTemplateService.getShippingInfo(saleType, ilGoodsId, urWareHouseId);

		assertEquals("N",shippingInfo.getAreaShippingYn());
	}

	@Test
	void getShippingInfo_판매유형_일일_일때() throws Exception{
		String saleType = "SALE_TYPE.DAILY";
		Long ilGoodsId = 175L;
		Long urWareHouseId = 4L;

		ShippingDataResultDto shippingInfo = goodsShippingTemplateService.getShippingInfo(saleType, ilGoodsId, urWareHouseId);

		assertEquals("DAILY",shippingInfo.getDeliveryType());
	}

	@Test
	void getShippingInfo_판매유형_일반_일때() throws Exception{
		String saleType = "SALE_TYPE.NORMAL";
		Long ilGoodsId = 175L;
		Long urWareHouseId = 4L;

		ShippingDataResultDto shippingInfo = goodsShippingTemplateService.getShippingInfo(saleType, ilGoodsId, urWareHouseId);

		assertEquals("NORMAL",shippingInfo.getDeliveryType());
	}

	@Test
	void getShippingInfoByShippingTmplId_성공() throws Exception{
		Long ilShippingTmplId = 6L;

		ShippingDataResultVo shippingInfoByShippingTmplId = goodsShippingTemplateService.getShippingInfoByShippingTmplId(ilShippingTmplId);

		assertNotNull(shippingInfoByShippingTmplId);
	}

	@Test
	void getShippingInfoByShippingTmplId_조회결과없음() throws Exception{
		Long ilShippingTmplId = 1L;

		ShippingDataResultVo shippingInfoByShippingTmplId = goodsShippingTemplateService.getShippingInfoByShippingTmplId(ilShippingTmplId);

		assertNull(shippingInfoByShippingTmplId);
	}

	@Test
	void getShippingPrice_조건배송비구분이_무료배송일때() throws Exception{
		ShippingDataResultVo shippingDataResultVo = new ShippingDataResultVo();
		shippingDataResultVo.setConditionType(GoodsEnums.ConditionType.TYPE1.getCode());
		int goodsPrice = 10000;
		int goodsQty = 1;
		ShippingPriceResponseDto shippingPrice = goodsShippingTemplateService.getShippingPrice(shippingDataResultVo, goodsPrice, goodsQty);

		assertEquals(0,shippingPrice.getBaiscShippingPrice());
	}

	@Test
	void getShippingPrice_조건배송비구분이_고정배송비일때() throws Exception{
		ShippingDataResultVo shippingDataResultVo = new ShippingDataResultVo();
		shippingDataResultVo.setConditionType(GoodsEnums.ConditionType.TYPE2.getCode());
		shippingDataResultVo.setShippingPrice(2500);
		int goodsPrice = 10000;
		int goodsQty = 1;
		ShippingPriceResponseDto shippingPrice = goodsShippingTemplateService.getShippingPrice(shippingDataResultVo, goodsPrice, goodsQty);

		assertEquals(2500,shippingPrice.getBaiscShippingPrice());
	}

	@Test
	void getShippingPrice_조건배송비구분이_결제금액당_배송비이고_무료배송일때() throws Exception{
		ShippingDataResultVo shippingDataResultVo = new ShippingDataResultVo();
		shippingDataResultVo.setConditionType(GoodsEnums.ConditionType.TYPE3.getCode());
		shippingDataResultVo.setShippingPrice(2500);
		shippingDataResultVo.setConditionValue(5000);
		int goodsPrice = 10000;
		int goodsQty = 1;
		ShippingPriceResponseDto shippingPrice = goodsShippingTemplateService.getShippingPrice(shippingDataResultVo, goodsPrice, goodsQty);

		assertEquals(0,shippingPrice.getBaiscShippingPrice());
	}

	@Test
	void getShippingPrice_조건배송비구분이_결제금액당_배송비이고_무료배송아닐때() throws Exception{
		ShippingDataResultVo shippingDataResultVo = new ShippingDataResultVo();
		shippingDataResultVo.setConditionType(GoodsEnums.ConditionType.TYPE3.getCode());
		shippingDataResultVo.setShippingPrice(2500);
		shippingDataResultVo.setConditionValue(15000);
		int goodsPrice = 10000;
		int goodsQty = 1;
		ShippingPriceResponseDto shippingPrice = goodsShippingTemplateService.getShippingPrice(shippingDataResultVo, goodsPrice, goodsQty);

		assertEquals(2500,shippingPrice.getBaiscShippingPrice());
	}

	@Test
	void getShippingPrice_조건배송비구분이_상품1개단위별_배송비일때() throws Exception{
		ShippingDataResultVo shippingDataResultVo = new ShippingDataResultVo();
		shippingDataResultVo.setConditionType(GoodsEnums.ConditionType.TYPE5.getCode());
		shippingDataResultVo.setShippingPrice(2500);
		int goodsPrice = 10000;
		int goodsQty = 10;
		ShippingPriceResponseDto shippingPrice = goodsShippingTemplateService.getShippingPrice(shippingDataResultVo, goodsPrice, goodsQty);

		assertEquals(25000,shippingPrice.getBaiscShippingPrice());
	}

	@Test
	void getShippingPrice_지역별배송비_사용() throws Exception{
		ShippingDataResultVo shippingDataResultVo = new ShippingDataResultVo();
		shippingDataResultVo.setConditionType(GoodsEnums.ConditionType.TYPE1.getCode());
		shippingDataResultVo.setJejuShippingPrice(6000);
		shippingDataResultVo.setAreaShippingYn("Y");
		int goodsPrice = 10000;
		int goodsQty = 1;
		String zipCode = "63194";

		ShippingPriceResponseDto shippingPriceResponseDto = goodsShippingTemplateService.getShippingPrice(shippingDataResultVo, goodsPrice, goodsQty, zipCode);

		assertEquals(6000,shippingPriceResponseDto.getShippingPrice());
	}

	@Test
	void getShippingPrice_지역별배송비_사용안함() throws Exception{
		ShippingDataResultVo shippingDataResultVo = new ShippingDataResultVo();
		shippingDataResultVo.setConditionType(GoodsEnums.ConditionType.TYPE1.getCode());
		shippingDataResultVo.setJejuShippingPrice(6000);
		shippingDataResultVo.setAreaShippingYn("N");
		int goodsPrice = 10000;
		int goodsQty = 1;
		String zipCode = "12345";

		ShippingPriceResponseDto shippingPriceResponseDto = goodsShippingTemplateService.getShippingPrice(shippingDataResultVo, goodsPrice, goodsQty, zipCode);

		assertEquals(0,shippingPriceResponseDto.getShippingPrice());
	}


	@Test
	void isUnDeliverableArea_우편번호에_대한_권역정보_없을때() throws Exception{
		String undeliverableAreaType = "";
		String receiverZipCode = "0000";
		ShippingAreaVo shippingAreaVo = goodsShippingTemplateService.getShippingArea(receiverZipCode);
		boolean isUnDeliverableArea = goodsShippingTemplateService.isUnDeliverableArea(undeliverableAreaType, shippingAreaVo);

		assertFalse(isUnDeliverableArea);
	}

	@Test
	void isUnDeliverableArea_우편번호에_대한_권역정보_있을때() throws Exception{
		String undeliverableAreaType = "";
		String receiverZipCode = "535882";
		boolean isUnDeliverableArea = false;
		ShippingAreaVo shippingAreaVo = goodsShippingTemplateService.getShippingArea(receiverZipCode);
		undeliverableAreaType = GoodsEnums.UndeliverableAreaType.A1.getCode();
		isUnDeliverableArea = goodsShippingTemplateService.isUnDeliverableArea(undeliverableAreaType, shippingAreaVo);
		assertTrue(isUnDeliverableArea);
	}

}
