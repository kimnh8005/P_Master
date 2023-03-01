package kr.co.pulmuone.v1.shopping.cart.service;

import kr.co.pulmuone.v1.comm.CommonServiceTestBaseForJunit5;
import kr.co.pulmuone.v1.comm.enums.ShoppingEnums.DeliveryType;
import kr.co.pulmuone.v1.comm.mapper.shopping.cart.ShoppingCartMapper;
import kr.co.pulmuone.v1.shopping.cart.dto.*;
import kr.co.pulmuone.v1.shopping.cart.dto.vo.*;
import kr.co.pulmuone.v1.shopping.restock.service.ShoppingRestockService;
import org.apache.commons.collections.CollectionUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

class ShoppingCartServiceTest extends CommonServiceTestBaseForJunit5{

	@Autowired
	private ShoppingCartService shoppingCartService;

	@InjectMocks
	private ShoppingCartService mockShoppingCartService;

	@Mock
	ShoppingCartMapper mockShoppingCartMapper;

	@Autowired
	private ShoppingRestockService shoppingRestockService;


	@BeforeEach
	void setUp(){
		mockShoppingCartService = new ShoppingCartService(mockShoppingCartMapper);
	}

	@Test
	void getSaveShippingCostGoodsList_성공() throws Exception{
		GetSaveShippingCostGoodsListRequestDto saveShippingCostGoodsListRequestDto = new GetSaveShippingCostGoodsListRequestDto();
		saveShippingCostGoodsListRequestDto.setUrWarehouseId(4L);
		saveShippingCostGoodsListRequestDto.setSaleType("SALE_TYPE.NORMAL");

		List<Long> saveShippingCostGoodsList = shoppingCartService.getSaveShippingCostGoodsList(saveShippingCostGoodsListRequestDto);

		assertTrue(saveShippingCostGoodsList.size() > 0);
	}

	@Test
	void getSaveShippingCostGoodsList_조회결과없음() throws Exception{
		GetSaveShippingCostGoodsListRequestDto saveShippingCostGoodsListRequestDto = new GetSaveShippingCostGoodsListRequestDto();
		saveShippingCostGoodsListRequestDto.setUrWarehouseId(0L);
		saveShippingCostGoodsListRequestDto.setSaleType("SALE_TYPE");

		List<Long> saveShippingCostGoodsList = shoppingCartService.getSaveShippingCostGoodsList(saveShippingCostGoodsListRequestDto);

		assertTrue(saveShippingCostGoodsList.size() == 0);
	}

	@Test
	void putCart_성공() throws Exception{
		SpCartVo spCartVo = new SpCartVo();
		spCartVo.setSpCartId(4347L);
		spCartVo.setQty(10);

		shoppingCartService.putCart(spCartVo);
	}

	@Test
	void delCart_성공() throws Exception{
		Long spCartId = 4348L;

		shoppingCartService.delCart(spCartId);
	}

	@Test
	void delCartAddGoodsBySpCartId_성공() throws Exception{
		Long spCartId = 4347L;

		shoppingCartService.delCartAddGoodsBySpCartId(spCartId);
	}

	@Test
	void getCartTypeSummary_성공() throws Exception{
		List<String> deliveryTypeList = new ArrayList<>();
		deliveryTypeList.add("DELIVERY_TYPE.NORMAL");
		List<Long> spCartIds = new ArrayList<>();
		spCartIds.add(4347L);
		spCartIds.add(4348L);
		spCartIds.add(4349L);

		given(mockShoppingCartMapper.getCartTypeSummary(any(),any())).willReturn(1);

		int cartTypeSummaryCnt = mockShoppingCartService.getCartTypeSummary(deliveryTypeList, spCartIds);

		assertEquals(1,cartTypeSummaryCnt);
	}

	@Test
	void getCartTypeSummary_조회결과없음() throws Exception{
		List<String> deliveryTypeList = new ArrayList<>();
		deliveryTypeList.add("DELIVERY_TYPE.RESERVATION");
		List<Long> spCartIds = new ArrayList<>();
		spCartIds.add(4347L);
		spCartIds.add(4348L);
		spCartIds.add(4349L);

		int cartTypeSummaryCnt = shoppingCartService.getCartTypeSummary(deliveryTypeList, spCartIds);

		assertEquals(0,cartTypeSummaryCnt);
	}

	@Test
	void addCart_성공() throws Exception{
		SpCartVo spCartVo = new SpCartVo();
		spCartVo.setUrPcidCd("PCID_CD");
		spCartVo.setUrUserId(100L);
		spCartVo.setDeliveryType("DELIVERY_TYPE.NORMAL");
		spCartVo.setIlGoodsId(175L);
		spCartVo.setQty(1);
		spCartVo.setBuyNowYn("N");

		int addCartInt = shoppingCartService.addCart(spCartVo);

		assertEquals(1,addCartInt);
	}

	@Test
	void addCart_실패() throws Exception{
		SpCartVo spCartVo = new SpCartVo();
		spCartVo.setUrUserId(100L);
		spCartVo.setDeliveryType("DELIVERY_TYPE.NORMAL");
		spCartVo.setIlGoodsId(175L);
		spCartVo.setQty(1);
		spCartVo.setBuyNowYn("N");

		assertThrows(Exception.class, () -> {
			shoppingCartService.addCart(spCartVo);
		});
	}

	@Test
	void addCartAddGoods_성공() throws Exception{
		SpCartAddGoodsVo SpCartAddGoodsVo = new SpCartAddGoodsVo();
		SpCartAddGoodsVo.setSpCartId(4347L);
		SpCartAddGoodsVo.setIlGoodsId(15370L);
		SpCartAddGoodsVo.setQty(1);

		int addCartAddGoodsInt = shoppingCartService.addCartAddGoods(SpCartAddGoodsVo);

		assertEquals(1,addCartAddGoodsInt);
	}

	@Test
	void addCartAddGoods_실패() throws Exception{
		SpCartAddGoodsVo SpCartAddGoodsVo = new SpCartAddGoodsVo();
		SpCartAddGoodsVo.setIlGoodsId(15370L);
		SpCartAddGoodsVo.setQty(1);

		assertThrows(Exception.class, () -> {
			shoppingCartService.addCartAddGoods(SpCartAddGoodsVo);
		});
	}

	@Test
	void ifCheckAddCartMerge_성공() throws Exception{
		Long ilGoodsId = 175L;

		assertTrue(shoppingCartService.ifCheckAddCartMerge(ilGoodsId));
	}

	@Test
	void getCartIdByIlGoodsId_성공() throws Exception{
		Long urUserId =	1646763L;
		Long ilGoodsId = 179L;
		String deliveryType = "DELIVERY_TYPE.NORMAL";

		given(mockShoppingCartMapper.getCartIdByIlGoodsId(any(),any(),any(),any(),any())).willReturn(1L);
		Long cartIdByIlGoodsId = mockShoppingCartService.getCartIdByIlGoodsId(null, urUserId, ilGoodsId, null,deliveryType);

		assertNotNull(cartIdByIlGoodsId);
	}

	@Test
	void getCartIdByIlGoodsId_조회결과없음() throws Exception{
		Long urUserId =	0L;
		Long ilGoodsId = 179L;
		String deliveryType = "DELIVERY_TYPE.NORMAL";

		Long cartIdByIlGoodsId = shoppingCartService.getCartIdByIlGoodsId(null, urUserId, ilGoodsId, null,deliveryType);

		assertNull(cartIdByIlGoodsId);
	}

	@Test
	void putCartPlusQty_성공() throws Exception{
		Long spCartId = 100L;
		int qty = 10;

		int putCartPlusQtyInt = shoppingCartService.putCartPlusQty(spCartId, qty);

		assertEquals(1,putCartPlusQtyInt);
	}

	@Test
	void putCartPlusQty_실패() throws Exception{
		Long spCartId = 1000L;
		int qty = 10;

		int putCartPlusQtyInt = shoppingCartService.putCartPlusQty(spCartId, qty);

		assertEquals(0,putCartPlusQtyInt);
	}

	@Test
	void getCartAddGoodsIdByIlGoodsId_성공() throws Exception{
		Long spCartId = 4347L;
		Long ilGoodsId = 175L;

		given(mockShoppingCartMapper.getCartAddGoodsIdByIlGoodsId(any(),any())).willReturn(1L);
		Long cartAddGoodsIdByIlGoodsId = mockShoppingCartService.getCartAddGoodsIdByIlGoodsId(spCartId, ilGoodsId);

		assertNotNull(cartAddGoodsIdByIlGoodsId);
	}

	@Test
	void getCartAddGoodsIdByIlGoodsId_조회결과없음() throws Exception{
		Long spCartId = 1L;
		Long ilGoodsId = 175L;

		Long cartAddGoodsIdByIlGoodsId = shoppingCartService.getCartAddGoodsIdByIlGoodsId(spCartId, ilGoodsId);

		assertNull(cartAddGoodsIdByIlGoodsId);
	}

	@Test
	void putCartAddGoodsPlusQty_성공() throws Exception{
		Long spCartAddGoodsId = 718L;
		int qty = 10;

		int putCartAddGoodsPlusQtyInt = shoppingCartService.putCartAddGoodsPlusQty(spCartAddGoodsId, qty);

		assertEquals(1,putCartAddGoodsPlusQtyInt);
	}

	@Test
	void putCartAddGoodsPlusQty_실패() throws Exception{
		Long spCartAddGoodsId = 100L;
		int qty = 10;

		int putCartAddGoodsPlusQtyInt = shoppingCartService.putCartAddGoodsPlusQty(spCartAddGoodsId, qty);

		assertEquals(0,putCartAddGoodsPlusQtyInt);
	}

	@Test
	void getDeliveryTypeBySaleType_성공() throws Exception{
		String saleType = "SALE_TYPE.NORMAL";

		assertEquals(DeliveryType.NORMAL,shoppingCartService.getDeliveryTypeBySaleType(saleType));
	}

	@Test
	void getDeliveryTypeBySaleType_실패() throws Exception{
		String saleType = "SALE_TYPE";

		assertNull(shoppingCartService.getDeliveryTypeBySaleType(saleType));
	}

	@Test
	void getCart_성공() throws Exception{
		Long spCartId = 4347L;
		SpCartVo resultVo = new SpCartVo();

		given(mockShoppingCartMapper.getCart(any())).willReturn(resultVo);
		SpCartVo cartVo = mockShoppingCartService.getCart(spCartId);

		assertNotNull(cartVo);
	}

	@Test
	void getCart_조회결과없음() throws Exception{
		Long spCartId = 1L;

		SpCartVo cartVo = shoppingCartService.getCart(spCartId);

		assertNull(cartVo);
	}

	@Test
	void isValidationAddCartInfo_성공() throws Exception{
		AddCartInfoRequestDto addCartInfoRequestDto = new AddCartInfoRequestDto();
		addCartInfoRequestDto.setUrUserId(100L);
		addCartInfoRequestDto.setSaleType("SALE_TYPE.NORMAL");
		addCartInfoRequestDto.setDeliveryType("NORMAL");
		addCartInfoRequestDto.setIlGoodsId(175L);
		addCartInfoRequestDto.setQty(1);
		addCartInfoRequestDto.setBuyNowYn("N");

		assertTrue(shoppingCartService.isValidationAddCartInfo(addCartInfoRequestDto));
	}

	@Test
	void isValidationAddCartInfo_goodsId_없음() throws Exception{
		AddCartInfoRequestDto addCartInfoRequestDto = new AddCartInfoRequestDto();
		addCartInfoRequestDto.setUrUserId(100L);
		addCartInfoRequestDto.setSaleType("SALE_TYPE.NORMAL");
		addCartInfoRequestDto.setDeliveryType("NORMAL");
		addCartInfoRequestDto.setQty(1);
		addCartInfoRequestDto.setBuyNowYn("N");

		assertFalse(shoppingCartService.isValidationAddCartInfo(addCartInfoRequestDto));
	}

	@Test
	void isValidationAddCartInfo_재고_없음() throws Exception{
		AddCartInfoRequestDto addCartInfoRequestDto = new AddCartInfoRequestDto();
		addCartInfoRequestDto.setUrUserId(100L);
		addCartInfoRequestDto.setSaleType("SALE_TYPE.NORMAL");
		addCartInfoRequestDto.setIlGoodsId(175L);
		addCartInfoRequestDto.setDeliveryType("NORMAL");
		addCartInfoRequestDto.setBuyNowYn("N");

		assertFalse(shoppingCartService.isValidationAddCartInfo(addCartInfoRequestDto));
	}

	@Test
	void isValidationAddCartInfo_바로구매_Y도_N도_아닐경우() throws Exception{
		AddCartInfoRequestDto addCartInfoRequestDto = new AddCartInfoRequestDto();
		addCartInfoRequestDto.setUrUserId(100L);
		addCartInfoRequestDto.setSaleType("SALE_TYPE.NORMAL");
		addCartInfoRequestDto.setDeliveryType("NORMAL");
		addCartInfoRequestDto.setIlGoodsId(175L);
		addCartInfoRequestDto.setQty(1);
		addCartInfoRequestDto.setBuyNowYn("A");

		assertFalse(shoppingCartService.isValidationAddCartInfo(addCartInfoRequestDto));
	}

	@Test
	void isValidationAddCartInfo_예약판매이고_예약정보PK가_없을경우() throws Exception{
		AddCartInfoRequestDto addCartInfoRequestDto = new AddCartInfoRequestDto();
		addCartInfoRequestDto.setUrUserId(100L);
		addCartInfoRequestDto.setSaleType("SALE_TYPE.RESERVATION");
		addCartInfoRequestDto.setDeliveryType("NORMAL");
		addCartInfoRequestDto.setIlGoodsId(175L);
		addCartInfoRequestDto.setQty(1);
		addCartInfoRequestDto.setBuyNowYn("N");

		assertFalse(shoppingCartService.isValidationAddCartInfo(addCartInfoRequestDto));
	}

	@Test
	void isValidationAddCartInfo_일일판매_베이비밀이고_일괄배송가능여부가_Y도N도_아닐경우() throws Exception{
		AddCartInfoRequestDto addCartInfoRequestDto = new AddCartInfoRequestDto();
		addCartInfoRequestDto.setUrUserId(100L);
		addCartInfoRequestDto.setSaleType("SALE_TYPE.DAILY");
		addCartInfoRequestDto.setGoodsDailyType("GOODS_DAILY_TP.BABYMEAL");
		addCartInfoRequestDto.setIlGoodsId(175L);
		addCartInfoRequestDto.setDeliveryType("NORMAL");
		addCartInfoRequestDto.setQty(1);
		addCartInfoRequestDto.setBuyNowYn("N");
		addCartInfoRequestDto.setGoodsDailyBulkYn("A");

		assertFalse(shoppingCartService.isValidationAddCartInfo(addCartInfoRequestDto));
	}

	@Test
	void isValidationAddCartInfo_일일판매_베이비밀이고_알러지식단선택여부가_Y도N도_아닐경우() throws Exception{
		AddCartInfoRequestDto addCartInfoRequestDto = new AddCartInfoRequestDto();
		addCartInfoRequestDto.setUrUserId(100L);
		addCartInfoRequestDto.setSaleType("SALE_TYPE.DAILY");
		addCartInfoRequestDto.setGoodsDailyType("GOODS_DAILY_TP.BABYMEAL");
		addCartInfoRequestDto.setIlGoodsId(175L);
		addCartInfoRequestDto.setDeliveryType("NORMAL");
		addCartInfoRequestDto.setQty(1);
		addCartInfoRequestDto.setBuyNowYn("N");
		addCartInfoRequestDto.setGoodsDailyBulkYn("Y");
		addCartInfoRequestDto.setGoodsDailyAllergyYn("A");

		assertFalse(shoppingCartService.isValidationAddCartInfo(addCartInfoRequestDto));
	}

	@Test
	void isValidationAddCartInfo_일일판매_베이비밀이고_일괄배송여부Y이고_일일배송주기가_없을경우() throws Exception{
		AddCartInfoRequestDto addCartInfoRequestDto = new AddCartInfoRequestDto();
		addCartInfoRequestDto.setUrUserId(100L);
		addCartInfoRequestDto.setSaleType("SALE_TYPE.DAILY");
		addCartInfoRequestDto.setGoodsDailyType("GOODS_DAILY_TP.BABYMEAL");
		addCartInfoRequestDto.setIlGoodsId(175L);
		addCartInfoRequestDto.setDeliveryType("NORMAL");
		addCartInfoRequestDto.setQty(1);
		addCartInfoRequestDto.setBuyNowYn("N");
		addCartInfoRequestDto.setGoodsDailyBulkYn("Y");
		addCartInfoRequestDto.setGoodsDailyAllergyYn("Y");

		assertFalse(shoppingCartService.isValidationAddCartInfo(addCartInfoRequestDto));
	}

	@Test
	void isValidationAddCartInfo_일일판매_베이비밀이고__일괄배송여부Y이고_일일배송기간이_없을경우() throws Exception{
		AddCartInfoRequestDto addCartInfoRequestDto = new AddCartInfoRequestDto();
		addCartInfoRequestDto.setUrUserId(100L);
		addCartInfoRequestDto.setSaleType("SALE_TYPE.DAILY");
		addCartInfoRequestDto.setGoodsDailyType("GOODS_DAILY_TP.BABYMEAL");
		addCartInfoRequestDto.setIlGoodsId(175L);
		addCartInfoRequestDto.setDeliveryType("NORMAL");
		addCartInfoRequestDto.setQty(1);
		addCartInfoRequestDto.setBuyNowYn("N");
		addCartInfoRequestDto.setGoodsDailyBulkYn("Y");
		addCartInfoRequestDto.setGoodsDailyAllergyYn("Y");
		addCartInfoRequestDto.setGoodsDailyCycleType("GOODS_CYCLE_TP.1DAY_PER_WEEK");

		assertFalse(shoppingCartService.isValidationAddCartInfo(addCartInfoRequestDto));
	}

	@Test
	void isValidationAddCartInfo_일일판매_베이비밀이고__일괄배송여부N이고_일괄배송세트코드가_없을경우() throws Exception{
		AddCartInfoRequestDto addCartInfoRequestDto = new AddCartInfoRequestDto();
		addCartInfoRequestDto.setUrUserId(100L);
		addCartInfoRequestDto.setSaleType("SALE_TYPE.DAILY");
		addCartInfoRequestDto.setGoodsDailyType("GOODS_DAILY_TP.BABYMEAL");
		addCartInfoRequestDto.setIlGoodsId(175L);
		addCartInfoRequestDto.setDeliveryType("NORMAL");
		addCartInfoRequestDto.setQty(1);
		addCartInfoRequestDto.setBuyNowYn("N");
		addCartInfoRequestDto.setGoodsDailyBulkYn("N");
		addCartInfoRequestDto.setGoodsDailyAllergyYn("Y");
		addCartInfoRequestDto.setGoodsDailyCycleType("GOODS_CYCLE_TP.1DAY_PER_WEEK");

		assertFalse(shoppingCartService.isValidationAddCartInfo(addCartInfoRequestDto));
	}

	@Test
	void isValidationAddCartInfo_일일판매_잇슬림이고_일일배송주기_없을경우() throws Exception{
		AddCartInfoRequestDto addCartInfoRequestDto = new AddCartInfoRequestDto();
		addCartInfoRequestDto.setUrUserId(100L);
		addCartInfoRequestDto.setSaleType("SALE_TYPE.DAILY");
		addCartInfoRequestDto.setGoodsDailyType("GOODS_DAILY_TP.EATSSLIM");
		addCartInfoRequestDto.setIlGoodsId(175L);
		addCartInfoRequestDto.setDeliveryType("NORMAL");
		addCartInfoRequestDto.setQty(1);
		addCartInfoRequestDto.setBuyNowYn("N");

		assertFalse(shoppingCartService.isValidationAddCartInfo(addCartInfoRequestDto));
	}

	@Test
	void isValidationAddCartInfo_일일판매_잇슬림이고_일일배송기간_없을경우() throws Exception{
		AddCartInfoRequestDto addCartInfoRequestDto = new AddCartInfoRequestDto();
		addCartInfoRequestDto.setUrUserId(100L);
		addCartInfoRequestDto.setSaleType("SALE_TYPE.DAILY");
		addCartInfoRequestDto.setGoodsDailyType("GOODS_DAILY_TP.EATSSLIM");
		addCartInfoRequestDto.setIlGoodsId(175L);
		addCartInfoRequestDto.setDeliveryType("NORMAL");
		addCartInfoRequestDto.setQty(1);
		addCartInfoRequestDto.setBuyNowYn("N");
		addCartInfoRequestDto.setGoodsDailyCycleType("GOODS_CYCLE_TP.1DAY_PER_WEEK");

		assertFalse(shoppingCartService.isValidationAddCartInfo(addCartInfoRequestDto));
	}

	@Test
	void isValidationAddCartInfo_일일판매_녹즙이고_일일배송주기_없을경우() throws Exception{
		AddCartInfoRequestDto addCartInfoRequestDto = new AddCartInfoRequestDto();
		addCartInfoRequestDto.setUrUserId(100L);
		addCartInfoRequestDto.setSaleType("SALE_TYPE.DAILY");
		addCartInfoRequestDto.setGoodsDailyType("GOODS_DAILY_TP.GREENJUICE");
		addCartInfoRequestDto.setIlGoodsId(175L);
		addCartInfoRequestDto.setDeliveryType("NORMAL");
		addCartInfoRequestDto.setQty(1);
		addCartInfoRequestDto.setBuyNowYn("N");

		assertFalse(shoppingCartService.isValidationAddCartInfo(addCartInfoRequestDto));
	}

	@Test
	void isValidationAddCartInfo_일일판매_녹즙이고_일일배송기간_없을경우() throws Exception{
		AddCartInfoRequestDto addCartInfoRequestDto = new AddCartInfoRequestDto();
		addCartInfoRequestDto.setUrUserId(100L);
		addCartInfoRequestDto.setSaleType("SALE_TYPE.DAILY");
		addCartInfoRequestDto.setGoodsDailyType("GOODS_DAILY_TP.GREENJUICE");
		addCartInfoRequestDto.setIlGoodsId(175L);
		addCartInfoRequestDto.setDeliveryType("NORMAL");
		addCartInfoRequestDto.setQty(1);
		addCartInfoRequestDto.setBuyNowYn("N");
		addCartInfoRequestDto.setGoodsDailyCycleType("GOODS_CYCLE_TP.1DAY_PER_WEEK");

		assertFalse(shoppingCartService.isValidationAddCartInfo(addCartInfoRequestDto));
	}

	@Test
	void isValidationAddCartInfo_일일판매_녹즙이고_녹즙요일코드_없을경우() throws Exception{
		AddCartInfoRequestDto addCartInfoRequestDto = new AddCartInfoRequestDto();
		addCartInfoRequestDto.setUrUserId(100L);
		addCartInfoRequestDto.setSaleType("SALE_TYPE.DAILY");
		addCartInfoRequestDto.setGoodsDailyType("GOODS_DAILY_TP.GREENJUICE");
		addCartInfoRequestDto.setIlGoodsId(175L);
		addCartInfoRequestDto.setDeliveryType("NORMAL");
		addCartInfoRequestDto.setQty(1);
		addCartInfoRequestDto.setBuyNowYn("N");
		addCartInfoRequestDto.setGoodsDailyCycleType("GOODS_CYCLE_TP.1DAY_PER_WEEK");
		addCartInfoRequestDto.setGoodsDailyCycleTermType("GOODS_CYCLE_TERM_TP.WEEK1");

		assertFalse(shoppingCartService.isValidationAddCartInfo(addCartInfoRequestDto));
	}

	@Test
	void getCartIdList_성공() throws Exception{
		GetCartDataRequestDto reqDto = new GetCartDataRequestDto();
		reqDto.setUrPcidCd("6ae03221-cb9e-4daa-8a77-2dca638927df");

		List<Long> resultList = new ArrayList<>();
		resultList.add(1L);

		given(mockShoppingCartMapper.getCartIdList(any())).willReturn(resultList);
		List<Long> cartIdList = mockShoppingCartService.getCartIdList(reqDto);

		assertTrue(cartIdList.size() > 0);
	}

	@Test
	void getCartIdList_조회결과없음() throws Exception{
		GetCartDataRequestDto reqDto = new GetCartDataRequestDto();
		reqDto.setUrPcidCd("1111");

		List<Long> cartIdList = shoppingCartService.getCartIdList(reqDto);

		assertTrue(cartIdList.size() == 0);
	}

	@Test
	void getCartDeliveryTypeGroupByList_성공() throws Exception{
		List<String> deliveryTypeList = new ArrayList<>();
		deliveryTypeList.add("DELIVERY_TYPE.NORMAL");
		List<Long> spCartIds = new ArrayList<>();
		spCartIds.add(100L);
		List<Long> gift = new ArrayList<>();

		CartDeliveryTypeGroupByVo[] resultList = new CartDeliveryTypeGroupByVo[1];
		resultList[0] = new CartDeliveryTypeGroupByVo();

		CartDeliveryTypeGroupByVo[] cartDeliveryTypeGroupByList = shoppingCartService.getCartDeliveryTypeGroupByList(deliveryTypeList, spCartIds,gift);

		assertTrue(cartDeliveryTypeGroupByList.length > 0);
	}

	@Test
	void getCartDeliveryTypeGroupByList_성공_with_gift() throws Exception{
		//given
		List<String> deliveryTypeList = new ArrayList<>();
		deliveryTypeList.add(DeliveryType.NORMAL.getCode());
		List<Long> spCartIds = new ArrayList<>();
		spCartIds.add(247885L);
		List<Long> gift = Collections.singletonList(4347L);

		//when
		CartDeliveryTypeGroupByVo[] cartDeliveryTypeGroupByList = shoppingCartService.getCartDeliveryTypeGroupByList(deliveryTypeList, spCartIds,gift);

		//then
		assertTrue(cartDeliveryTypeGroupByList.length > 0);
	}

	@Test
	void getCartDeliveryTypeGroupByList_조회결과없음() throws Exception{
		List<String> deliveryTypeList = new ArrayList<>();
		deliveryTypeList.add("DELIVERY_TYPE.NORMAL");
		List<Long> spCartIds = new ArrayList<>();
		spCartIds.add(1L);
		List<Long> gift = null;

		CartDeliveryTypeGroupByVo[] cartDeliveryTypeGroupByList = shoppingCartService.getCartDeliveryTypeGroupByList(deliveryTypeList, spCartIds,gift);

		assertTrue(cartDeliveryTypeGroupByList.length == 0);
	}

	@Test
	void getShippingTemplateGroupByListByDeliveryType_성공() throws Exception{
		ShippingTemplateGroupByVo resultVo = new ShippingTemplateGroupByVo();
		ShippingTemplateGroupByVo[] resultVoList = new ShippingTemplateGroupByVo[1];
		resultVoList[0] = resultVo;

		String deliveryType = "DELIVERY_TYPE.NORMAL";
		List<Long> spCartIds = new ArrayList<>();
		spCartIds.add(4376L);
		spCartIds.add(4347L);
		String bridgeYn = "N";
		Long urUserId = 1L;
		List<Long> gift = null;

		given(mockShoppingCartMapper.getShippingTemplateGroupByListByDeliveryType(any(),any(),any(),any(),any())).willReturn(resultVoList);

		ShippingTemplateGroupByVo[] shippingTemplateGroupByVos = mockShoppingCartService.getShippingTemplateGroupByListByDeliveryType(deliveryType,
				spCartIds, bridgeYn, urUserId, gift);

		assertTrue(shippingTemplateGroupByVos.length > 0);
	}

	@Test
	void getShippingTemplateGroupByListByDeliveryType_장바구니배송타입_정기배송일때() throws Exception{
		ShippingTemplateGroupByVo resultVo = new ShippingTemplateGroupByVo();
		ShippingTemplateGroupByVo[] resultVoList = new ShippingTemplateGroupByVo[1];
		resultVoList[0] = resultVo;

		String deliveryType = "DELIVERY_TYPE.REGULAR";
		List<Long> spCartIds = new ArrayList<>();
		spCartIds.add(4376L);
		spCartIds.add(4377L);
		String bridgeYn = "Y";
		Long urUserId = 1L;

		given(mockShoppingCartMapper.getShippingTemplateGroupByListByDeliveryType(any(),any(),any(),any(),any())).willReturn(resultVoList);

		ShippingTemplateGroupByVo[] shippingTemplateGroupByListByDeliveryType = mockShoppingCartService.getShippingTemplateGroupByListByDeliveryType(deliveryType, spCartIds, bridgeYn, urUserId,null);

		assertTrue(shippingTemplateGroupByListByDeliveryType.length > 0);
	}

	@Test
	void getGoodsListByShippingPolicy_성공() throws Exception{
		ShippingTemplateGroupByVo shippingTemplateData = new ShippingTemplateGroupByVo();
		shippingTemplateData.setUrWarehouseId(4L);
		List<Long> spCartIds = new ArrayList<>();
		spCartIds.add(4347L);
		spCartIds.add(4356L);
		String deliveryType = "DELIVERY_TYPE.NORMAL";

		List<SpCartVo> resultVoList = new ArrayList<>();
		SpCartVo spCartVo = new SpCartVo();
		spCartVo.setSpCartId(4347L);
		resultVoList.add(spCartVo);

		given(mockShoppingCartMapper.getGoodsListByShippingPolicy(any(),any(),any())).willReturn(resultVoList);

		List<SpCartVo> goodsListByShippingPolicy = mockShoppingCartService.getGoodsListByShippingPolicy(deliveryType,shippingTemplateData, spCartIds);

		assertTrue(CollectionUtils.isNotEmpty(goodsListByShippingPolicy));
	}

	@Test
	void getGoodsListByShippingPolicy_조회결과없음() throws Exception{
		ShippingTemplateGroupByVo shippingTemplateData = new ShippingTemplateGroupByVo();
		shippingTemplateData.setUrWarehouseId(4L);
		List<Long> spCartIds = new ArrayList<>();
		spCartIds.add(1L);
		String deliveryType = "DELIVERY_TYPE.NORMAL";

		List<SpCartVo> goodsListByShippingPolicy = shoppingCartService.getGoodsListByShippingPolicy(deliveryType,shippingTemplateData, spCartIds);

		assertTrue(goodsListByShippingPolicy.size() == 0);
	}

	@Test
	void getShippingArrivalScheduledDateList_성공() throws Exception{
		List<CartDeliveryDto> cartDataDto = new ArrayList<>();
		CartDeliveryTypeGroupByVo cartVo = new CartDeliveryTypeGroupByVo();
		List<CartShippingDto> shippingList = new ArrayList<>();
		CartDeliveryDto cartDeliveryDto = new CartDeliveryDto(cartVo);
		ShippingTemplateGroupByVo vo = new ShippingTemplateGroupByVo();
		CartShippingDto cartShippingDto1 = new CartShippingDto(0, vo, true);
		List<LocalDate> localDateList1 = new ArrayList<>();
		localDateList1.add(LocalDate.of(2020, 9, 26));
		localDateList1.add(LocalDate.of(2020, 9, 27));
		localDateList1.add(LocalDate.of(2020, 9, 28));
		cartShippingDto1.setChoiceArrivalScheduledDateList(localDateList1);
		CartShippingDto cartShippingDto2 = new CartShippingDto(1, vo, true);
		List<LocalDate> localDateList2 = new ArrayList<>();
		localDateList2.add(LocalDate.of(2020, 10, 1));
		localDateList2.add(LocalDate.of(2020, 10, 2));
		localDateList2.add(LocalDate.of(2020, 10, 3));
		cartShippingDto2.setChoiceArrivalScheduledDateList(localDateList2);
		shippingList.add(cartShippingDto1);
		shippingList.add(cartShippingDto2);
		cartDeliveryDto.setShipping(shippingList);
		cartDataDto.add(cartDeliveryDto);

		List<List<LocalDate>> shippingArrivalScheduledDateList = shoppingCartService.getShippingArrivalScheduledDateList(cartDataDto);

		assertEquals(9,shippingArrivalScheduledDateList.get(0).get(0).getMonthValue());
	}

	@Test
	void getCartDataSummary_성공() throws Exception{
		List<CartDeliveryDto> cartDataDto = new ArrayList<>();
		CartDeliveryTypeGroupByVo vo = new CartDeliveryTypeGroupByVo();
		CartDeliveryDto cartDeliveryDto = new CartDeliveryDto(vo);
		ShippingTemplateGroupByVo shippingVo = new ShippingTemplateGroupByVo();
		CartShippingDto cartShippingDto1 = new CartShippingDto(0, shippingVo, true);
		cartShippingDto1.setGoodsRecommendedPrice(10000);
		cartShippingDto1.setGoodsDiscountPrice(1000);
		cartShippingDto1.setShippingRecommendedPrice(2500);
		cartShippingDto1.setPaymentPrice(11500);
		List<CartGoodsDto> goods1 = new ArrayList<>();
		CartGoodsDto goodsDto1 = new CartGoodsDto(new SpCartVo());
		goodsDto1.setGoodsType("GOODS_TYPE.NORMAL");
		goodsDto1.setGoodsName("TEST");
		goodsDto1.setIlGoodsId(1L);
		List<CartGoodsDiscountDto> discount = new ArrayList<>();
		goodsDto1.setDiscount(discount);
		List<CartAdditionalGoodsDto> additionalGoods = new ArrayList<>();
		goodsDto1.setAdditionalGoods(additionalGoods);
		goods1.add(goodsDto1);
		cartShippingDto1.setGoods(goods1);
		CartShippingDto cartShippingDto2 = new CartShippingDto(1, shippingVo, true);
		cartShippingDto2.setGoodsRecommendedPrice(5000);
		cartShippingDto2.setGoodsDiscountPrice(1000);
		cartShippingDto2.setShippingRecommendedPrice(2500);
		cartShippingDto2.setPaymentPrice(6500);
		cartShippingDto2.setGoods(goods1);
		List<CartShippingDto> cartShippingDtoList = new ArrayList<>();
		cartShippingDtoList.add(cartShippingDto1);
		cartShippingDtoList.add(cartShippingDto2);
		cartDeliveryDto.setShipping(cartShippingDtoList);
		cartDataDto.add(cartDeliveryDto);

		CartSummaryDto cartSummaryDto = shoppingCartService.getCartDataSummary(cartDataDto);

		assertEquals(15000,cartSummaryDto.getGoodsRecommendedPrice());
	}

	@Test
	void getCartDataSummary_실패() throws Exception{
		List<CartDeliveryDto> cartDataDto = new ArrayList<>();
		CartDeliveryTypeGroupByVo vo = new CartDeliveryTypeGroupByVo();
		CartDeliveryDto cartDeliveryDto = new CartDeliveryDto(vo);
		ShippingTemplateGroupByVo shippingVo = new ShippingTemplateGroupByVo();
		CartShippingDto cartShippingDto1 = new CartShippingDto(0, shippingVo, true);
		cartShippingDto1.setGoodsRecommendedPrice(10000);
		cartShippingDto1.setGoodsDiscountPrice(1000);
		cartShippingDto1.setShippingRecommendedPrice(2500);
		cartShippingDto1.setPaymentPrice(11500);
		List<CartGoodsDto> goods1 = new ArrayList<>();
		CartGoodsDto goodsDto1 = new CartGoodsDto(new SpCartVo());
		goodsDto1.setGoodsType("GOODS_TYPE.NORMAL");
		goodsDto1.setGoodsName("TEST");
		goodsDto1.setIlGoodsId(1L);
		List<CartGoodsDiscountDto> discount = new ArrayList<>();
		goodsDto1.setDiscount(discount);
		List<CartAdditionalGoodsDto> additionalGoods = new ArrayList<>();
		goodsDto1.setAdditionalGoods(additionalGoods);
		goods1.add(goodsDto1);
		cartShippingDto1.setGoods(goods1);
		CartShippingDto cartShippingDto2 = new CartShippingDto(1, shippingVo, true);
		cartShippingDto2.setGoodsRecommendedPrice(5000);
		cartShippingDto2.setGoodsDiscountPrice(1000);
		cartShippingDto2.setShippingRecommendedPrice(2500);
		cartShippingDto2.setPaymentPrice(6500);
		cartShippingDto2.setGoods(goods1);
		List<CartShippingDto> cartShippingDtoList = new ArrayList<>();
		cartShippingDtoList.add(cartShippingDto1);
		cartShippingDtoList.add(cartShippingDto2);
		cartDeliveryDto.setShipping(cartShippingDtoList);
		cartDataDto.add(cartDeliveryDto);

		CartSummaryDto cartSummaryDto = shoppingCartService.getCartDataSummary(cartDataDto);


		assertNotEquals(100,cartSummaryDto.getGoodsRecommendedPrice());
	}

	@Test
	void getcartAddGoodsIdList_성공() throws Exception{
		Long spCartId = 4347L;

		List<SpCartAddGoodsVo> resultVoList = new ArrayList<>();
		resultVoList.add(new SpCartAddGoodsVo());
		given(mockShoppingCartMapper.getCartAddGoodsIdList(any())).willReturn(resultVoList);
		List<SpCartAddGoodsVo> cartAddGoodsIdList = mockShoppingCartService.getCartAddGoodsIdList(spCartId);

		assertTrue(cartAddGoodsIdList.size() > 0);
	}

	@Test
	void getcartAddGoodsIdList_조회결과없음() throws Exception{
		Long spCartId = 1L;

		List<SpCartAddGoodsVo> cartAddGoodsIdList = shoppingCartService.getCartAddGoodsIdList(spCartId);

		assertTrue(cartAddGoodsIdList.size() == 0);
	}

	@Test
	void isEmployeeDiscountExceedingLimit_임직원_한도초과일경우() throws Exception{
		List<CartDeliveryDto> cartDataDto = new ArrayList<>();
		CartDeliveryTypeGroupByVo vo = new CartDeliveryTypeGroupByVo();
		CartDeliveryDto cartDeliveryDto = new CartDeliveryDto(vo);
		List<CartShippingDto> cartShippingDtoList = new ArrayList<>();
		ShippingTemplateGroupByVo shippingVo = new ShippingTemplateGroupByVo();
		CartShippingDto cartShippingDto = new CartShippingDto(0, shippingVo, true);
		List<CartGoodsDto> cartGoodsDtoList = new ArrayList<>();
		SpCartVo spCartVo = new SpCartVo();
		CartGoodsDto cartGoodsDto = new CartGoodsDto(spCartVo);
		List<CartGoodsDiscountDto> cartGoodsDiscountDtoList = new ArrayList<>();
		CartGoodsDiscountDto dto = new CartGoodsDiscountDto();
		dto.setDiscountType("GOODS_DISCOUNT_TP.EMPLOYEE");
		dto.setExceedingLimitPrice(1000);
		cartGoodsDiscountDtoList.add(dto);
		List<CartAdditionalGoodsDto> additionalGoods = new ArrayList<>();
		CartAdditionalGoodsDto cartAdditionalGoodsDto = new CartAdditionalGoodsDto();
		List<CartGoodsDiscountDto> discount = new ArrayList<>();
		discount.add(new CartGoodsDiscountDto());
		cartAdditionalGoodsDto.setDiscount(discount);
		additionalGoods.add(cartAdditionalGoodsDto);
		cartGoodsDto.setAdditionalGoods(additionalGoods);
		cartGoodsDto.setDiscount(cartGoodsDiscountDtoList);
		cartGoodsDtoList.add(cartGoodsDto);
		cartShippingDto.setGoods(cartGoodsDtoList);
		cartShippingDtoList.add(cartShippingDto);
		cartDeliveryDto.setShipping(cartShippingDtoList);
		cartDataDto.add(cartDeliveryDto);

		assertTrue(shoppingCartService.isEmployeeDiscountExceedingLimit(cartDataDto));
	}

	@Test
	void isEmployeeDiscountExceedingLimit_임직원_한도초과_아닐경우() throws Exception{
		List<CartDeliveryDto> cartDataDto = new ArrayList<>();
		CartDeliveryTypeGroupByVo vo = new CartDeliveryTypeGroupByVo();
		CartDeliveryDto cartDeliveryDto = new CartDeliveryDto(vo);
		List<CartShippingDto> cartShippingDtoList = new ArrayList<>();
		ShippingTemplateGroupByVo shippingVo = new ShippingTemplateGroupByVo();
		CartShippingDto cartShippingDto = new CartShippingDto(0, shippingVo, true);
		List<CartGoodsDto> cartGoodsDtoList = new ArrayList<>();
		SpCartVo spCartVo = new SpCartVo();
		CartGoodsDto cartGoodsDto = new CartGoodsDto(spCartVo);
		List<CartGoodsDiscountDto> cartGoodsDiscountDtoList = new ArrayList<>();
		CartGoodsDiscountDto dto = new CartGoodsDiscountDto();
		dto.setDiscountType("GOODS_DISCOUNT_TP.NORMAL");
		dto.setExceedingLimitPrice(1000);
		cartGoodsDiscountDtoList.add(dto);
		List<CartAdditionalGoodsDto> additionalGoods = new ArrayList<>();
		CartAdditionalGoodsDto cartAdditionalGoodsDto = new CartAdditionalGoodsDto();
		List<CartGoodsDiscountDto> discount = new ArrayList<>();
		discount.add(new CartGoodsDiscountDto());
		cartAdditionalGoodsDto.setDiscount(discount);
		additionalGoods.add(cartAdditionalGoodsDto);
		cartGoodsDto.setAdditionalGoods(additionalGoods);
		cartGoodsDto.setDiscount(cartGoodsDiscountDtoList);
		cartGoodsDtoList.add(cartGoodsDto);
		cartShippingDto.setGoods(cartGoodsDtoList);
		cartShippingDtoList.add(cartShippingDto);
		cartDeliveryDto.setShipping(cartShippingDtoList);
		cartDataDto.add(cartDeliveryDto);

		assertFalse(shoppingCartService.isEmployeeDiscountExceedingLimit(cartDataDto));
	}

	@Test
	void delCartAddGoods_성공() throws Exception{
		Long spCartAddGoodsId = 6L;

		shoppingCartService.delCartAddGoods(spCartAddGoodsId);
	}

	@Test
	void getCartPickGoodsList_성공() throws Exception {
		Long spCartId = 6111L;

		List<SpCartPickGoodsVo> resultVoList = new ArrayList<>();
		resultVoList.add(new SpCartPickGoodsVo());
		given(mockShoppingCartMapper.getCartPickGoodsList(any())).willReturn(resultVoList);
		assertTrue(mockShoppingCartService.getCartPickGoodsList(spCartId).size() > 0);
	}

	@Test
	void getCartPickGoodsList_조회결과없음() throws Exception {
		Long spCartId = 1L;

		assertFalse(shoppingCartService.getCartPickGoodsList(spCartId).size() > 0);
	}

	@Test
	void getGiftGoodsListByShippingPolicy_성공() throws Exception{
		ShippingTemplateGroupByVo shippingTemplateData = new ShippingTemplateGroupByVo();
		shippingTemplateData.setUrWarehouseId(4L);
		shippingTemplateData.setBundleYn("Y");
		List<CartGiftDto> gift = new ArrayList<>();
		CartGiftDto cartGiftDto = new CartGiftDto();
		cartGiftDto.setEvExhibitId(34L);
		cartGiftDto.setIlGoodsId(175L);
		gift.add(cartGiftDto);

		assertTrue(shoppingCartService.getGiftGoodsListByShippingPolicy(shippingTemplateData, gift).size() > 0);
	}

	@Test
	void getGiftGoodsListByShippingPolicy_조회결과없음() throws Exception{
		ShippingTemplateGroupByVo shippingTemplateData = new ShippingTemplateGroupByVo();
		shippingTemplateData.setUrWarehouseId(4L);
		List<CartGiftDto> gift = new ArrayList<>();
		CartGiftDto cartGiftDto = new CartGiftDto();
		cartGiftDto.setEvExhibitId(5L);
		cartGiftDto.setIlGoodsId(1L);
		gift.add(cartGiftDto);

		assertFalse(shoppingCartService.getGiftGoodsListByShippingPolicy(shippingTemplateData, gift).size() > 0);
	}


	@Test
	void putRetockInfo_재입고알림등록() throws Exception{

		Long ilGoodsId = 1L;
		Long urUserId = 1L;

//		assertTrue(shoppingRestockService.putRetockInfo(ilGoodsId, urUserId) > 0);
	}

}
