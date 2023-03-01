package kr.co.pulmuone.v1.goods.goods.service;

import kr.co.pulmuone.v1.comm.CommonServiceTestBaseForJunit5;
import kr.co.pulmuone.v1.comm.enums.GoodsEnums;
import kr.co.pulmuone.v1.comm.enums.GoodsEnums.GoodsCycleTypeByGreenJuice;
import kr.co.pulmuone.v1.comm.enums.StoreEnums;
import kr.co.pulmuone.v1.comm.util.DeviceUtil;
import kr.co.pulmuone.v1.goods.goods.dto.*;
import kr.co.pulmuone.v1.goods.goods.dto.vo.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SuppressWarnings("rawtypes")
class GoodsGoodsServiceTest extends CommonServiceTestBaseForJunit5{

	@Autowired
	private GoodsGoodsService goodsGoodsService;

	@Test
	void getBasicSelectGoods_성공() throws Exception {
		Long ilGoodsId = 175L;
		boolean isMember = true;
		boolean isEmployee= false;

		GoodsRequestDto goodsRequestDto = GoodsRequestDto.builder()
														.ilGoodsId(ilGoodsId)
														.deviceInfo(DeviceUtil.getDirInfo())
														.isApp(DeviceUtil.isApp())
														.isMember(isMember)
														.isEmployee(isEmployee)
														.build();

		BasicSelectGoodsVo goodsResultVo = goodsGoodsService.getBasicSelectGoods(goodsRequestDto);

		assertNotNull(goodsResultVo.getIlGoodsId());

	}

	@Test
	void getBasicSelectGoods_조회결과없음() throws Exception{
		Long ilGoodsId = 999L;
		boolean isMember = true;
		boolean isEmployee= false;

		GoodsRequestDto goodsRequestDto = GoodsRequestDto.builder()
														.ilGoodsId(ilGoodsId)
														.deviceInfo(DeviceUtil.getDirInfo())
														.isApp(DeviceUtil.isApp())
														.isMember(isMember)
														.isEmployee(isEmployee)
														.build();

		BasicSelectGoodsVo goodsResultVo = goodsGoodsService.getBasicSelectGoods(goodsRequestDto);

		assertNull(goodsResultVo);

	}

	@Test
	void getSaleStatus_판매중상품_재고있을때() throws Exception{
		String saleStatus = GoodsEnums.SaleStatus.ON_SALE.getCode();
		int stock = 10;

		String getSaleStatus = goodsGoodsService.getSaleStatus(saleStatus, stock);

		assertEquals(getSaleStatus,GoodsEnums.SaleStatus.ON_SALE.getCode());

	}

	@Test
	void getSaleStatus_판매중상품_재고없을때() throws Exception{
		String saleStatus = GoodsEnums.SaleStatus.ON_SALE.getCode();
		int stock = 0;

		String getSaleStatus = goodsGoodsService.getSaleStatus(saleStatus, stock);

		assertEquals(getSaleStatus,GoodsEnums.SaleStatus.OUT_OF_STOCK_BY_SYSTEM.getCode());
	}

	@Test
	void getDetailSelectGoods_성공() throws Exception{
		Long ilGoodsId = 175L;

		DetailSelectGoodsVo goodsResultVo = goodsGoodsService.getDetailSelectGoods(ilGoodsId);

		assertNotNull(goodsResultVo.getIlGoodsId());
	}

	@Test
	void getDetailSelectGoods_조회결과없음() throws Exception{
		Long ilGoodsId = 999L;

		DetailSelectGoodsVo goodsResultVo = goodsGoodsService.getDetailSelectGoods(ilGoodsId);

		assertNull(goodsResultVo);
	}

	@Test
	void getGoodsCertificationList_성공() throws Exception{
		Long ilGoodsId = 175L;

		List<GoodsCertificationListResultVo> goodsCertificationList = goodsGoodsService.getGoodsCertificationList(ilGoodsId);

		assertTrue(goodsCertificationList.size() > 0);
	}

	@Test
	void getGoodsCertificationList_조회결과없음() throws Exception{
		Long ilGoodsId = 1L;

		List<GoodsCertificationListResultVo> goodsCertificationList = goodsGoodsService.getGoodsCertificationList(ilGoodsId);

		assertTrue(goodsCertificationList.size() == 0);
	}

	@Test
	void getGoodsSpecList_성공() throws Exception{
		Long ilGoodsid = 175L;

		List<GoodsSpecListResultVo> goodsSpecList = goodsGoodsService.getGoodsSpecList(ilGoodsid);

		assertTrue(goodsSpecList.size() > 0);
	}

	@Test
	void getGoodsSpecList_조회결과없음() throws Exception{
		Long ilGoodsid = 999L;

		List<GoodsSpecListResultVo> goodsSpecList = goodsGoodsService.getGoodsSpecList(ilGoodsid);

		assertTrue(goodsSpecList.size() == 0);
	}

	@Test
	void isBuyNonMember_비회원구매가능() throws Exception{
		String goodsType = GoodsEnums.GoodsType.NORMAL.getCode();
		String saleType = GoodsEnums.SaleType.NORMAL.getCode();
		String limitMaxType = GoodsEnums.PurchaseLimitMaxType.UNLIMIT.getCode();

		boolean result = goodsGoodsService.isBuyNonMember(goodsType, saleType, limitMaxType);

		assertTrue(result);
	}

	@Test
	void isBuyNonMember_상품유형_일일이면_비회원구매불가() throws Exception{
		String goodsType = GoodsEnums.GoodsType.DAILY.getCode();
		String saleType = GoodsEnums.SaleType.NORMAL.getCode();
		String limitMaxType = GoodsEnums.PurchaseLimitMaxType.UNLIMIT.getCode();

		boolean result = goodsGoodsService.isBuyNonMember(goodsType, saleType, limitMaxType);

		assertFalse(result);
	}

	@Test
	void isBuyNonMember_상품유형_매장이면_비회원구매불가() throws Exception{
		String goodsType = GoodsEnums.GoodsType.SHOP_ONLY.getCode();
		String saleType = GoodsEnums.SaleType.NORMAL.getCode();
		String limitMaxType = GoodsEnums.PurchaseLimitMaxType.UNLIMIT.getCode();

		boolean result = goodsGoodsService.isBuyNonMember(goodsType, saleType, limitMaxType);

		assertFalse(result);
	}

	@Test
	void isBuyNonMember_판매유형_정기이면_비회원구매불가() throws Exception{
		String goodsType = GoodsEnums.GoodsType.NORMAL.getCode();
		String saleType = GoodsEnums.SaleType.REGULAR.getCode();
		String limitMaxType = GoodsEnums.PurchaseLimitMaxType.UNLIMIT.getCode();

		boolean result = goodsGoodsService.isBuyNonMember(goodsType, saleType, limitMaxType);

		assertFalse(result);
	}

	@Test
	void isBuyNonMember_최대구매수량_지정이면_비회원구매불가() throws Exception{
		String goodsType = GoodsEnums.GoodsType.NORMAL.getCode();
		String saleType = GoodsEnums.SaleType.NORMAL.getCode();
		String limitMaxType = GoodsEnums.PurchaseLimitMaxType.DURATION.getCode();

		boolean result = goodsGoodsService.isBuyNonMember(goodsType, saleType, limitMaxType);

		assertFalse(result);
	}

	@Test
	void getGoodsNutritionList_성공() throws Exception{
		String ilItemCode = "ERP20200804142843";

		List<GoodsNutritionListResultVo> goodsNutritionList = goodsGoodsService.getGoodsNutritionList(ilItemCode);

		assertTrue(goodsNutritionList.size() > 0);
	}

	@Test
	void getGoodsNutritionList_조회결과없음() throws Exception{
		String ilItemCode = "ERP00000";

		List<GoodsNutritionListResultVo> goodsNutritionList = goodsGoodsService.getGoodsNutritionList(ilItemCode);

		assertTrue(goodsNutritionList.size() == 0);
	}

	@Test
	void getAdditionalGoodsList_성공() throws Exception{
		Long ilGoodsId = 15584L;

		List<HashMap> additionalGoodsResultList = goodsGoodsService.getAdditionalGoodsList(ilGoodsId);

		assertTrue(additionalGoodsResultList.size() > 0);
	}

	@Test
	void getAdditionalGoodsList_조회결과없음() throws Exception{
		Long ilGoodsId = 1L;

		List<HashMap> additionalGoodsResultList = goodsGoodsService.getAdditionalGoodsList(ilGoodsId);
		
		assertTrue(additionalGoodsResultList.size() == 0);
	}


	@Test
	void getPackagGoodsInfoList_성공() throws Exception{
		Long ilGoodsId = 11506L;

		List<PackageGoodsResultVo> packageGoodsIdList = goodsGoodsService.getPackageGoodsList(ilGoodsId);

		assertTrue(packageGoodsIdList.size() > 0);
	}

	@Test
	void getPackagGoodsInfoList_조회결과없음() throws Exception{
		Long ilGoodsId = 1L;

		List<PackageGoodsResultVo> packageGoodsIdList = goodsGoodsService.getPackageGoodsList(ilGoodsId);

		assertTrue(packageGoodsIdList.size() == 0);
	}

	@Test
	void getGoodsReserveOptionList_성공() throws Exception{
		Long ilGoodsId = 181L;

		List<GoodsReserveOptionListDto> goodsReserveOptionList = goodsGoodsService.getGoodsReserveOptionList(ilGoodsId);

		assertTrue(goodsReserveOptionList.size() > 0);
	}

	@Test
	void getGoodsReserveOptionList_조회결과없음() throws Exception{
		Long ilGoodsId = 1L;

		List<GoodsReserveOptionListDto> goodsReserveOptionList = goodsGoodsService.getGoodsReserveOptionList(ilGoodsId);

		assertTrue(goodsReserveOptionList.size() == 0);
	}

	@Test
	void getGoodsReserveOption_성공() throws Exception{
		Long ilGoodsReserveOptnId = 1L;

		GoodsReserveOptionVo goodsReserveOption = goodsGoodsService.getGoodsReserveOption(ilGoodsReserveOptnId);

		assertNotNull(goodsReserveOption);
	}

	@Test
	void getGoodsReserveOption_조회결과없음() throws Exception{
		Long ilGoodsReserveOptnId = 0L;

		GoodsReserveOptionVo goodsReserveOption = goodsGoodsService.getGoodsReserveOption(ilGoodsReserveOptnId);

		assertNull(goodsReserveOption);
	}

	@Test
	void getGoodsDailyBulkList_성공() throws Exception{
		Long ilGoodsId = 185L;

		List<HashMap<String,String>> goodsDailyBulkList = goodsGoodsService.getGoodsDailyBulkList(ilGoodsId);

		assertTrue(goodsDailyBulkList.size() > 0);
	}

	@Test
	void getGoodsDailyBulkList_조회결과없음() throws Exception{
		Long ilGoodsId = 1L;

		List<HashMap<String,String>> goodsDailyBulkList = goodsGoodsService.getGoodsDailyBulkList(ilGoodsId);

		assertTrue(goodsDailyBulkList.size() == 0);
	}

	@Test
	void getGoodsDailyCycleList_성공() throws Exception{
		Long ilGoodsId = 185L;

		List<HashMap<String, String>> goodsDailyCycleTypeList = goodsGoodsService.getGoodsDailyCycleList(ilGoodsId);

		assertTrue(goodsDailyCycleTypeList.size() > 0);
	}

	@Test
	void getGoodsDailyCycleList_조회결과없음() throws Exception{
		Long ilGoodsId = 1L;

		List<HashMap<String, String>> goodsDailyCycleTypeList = goodsGoodsService.getGoodsDailyCycleList(ilGoodsId);

		assertTrue(goodsDailyCycleTypeList.size() == 0);
	}


	@Test
	void getGoodsDailyCycleGreenJuiceList_성공() throws Exception{

		List<HashMap<String, String>> goodsDailyCycleGreenJuiceList = goodsGoodsService.getGoodsDailyCycleGreenJuiceList();

		assertTrue(goodsDailyCycleGreenJuiceList.size() > 0);
	}

	@Test
	void getGoodsDailyCycleGreenJuiceList_조회결과없으면_안됨() throws Exception{

		List<HashMap<String, String>> goodsDailyCycleGreenJuiceList = goodsGoodsService.getGoodsDailyCycleGreenJuiceList();

		assertFalse(goodsDailyCycleGreenJuiceList.size() == 0);
	}

	@Test
	void getGoodsDailyCycleTermList_성공() throws Exception{
		Long ilGoodsId = 185L;
		String goodsCycleType = GoodsEnums.GoodsCycleTypeByGreenJuice.DAYS3_PER_WEEK.getCode();

		List<HashMap<String, String>> goodsDailyCycleTermList = goodsGoodsService.getGoodsDailyCycleTermList(ilGoodsId, goodsCycleType);

		assertTrue(goodsDailyCycleTermList.size() > 0);
	}

	@Test
	void getGoodsDailyCycleTermList_조회결과없음() throws Exception{
		Long ilGoodsId = 1L;
		String goodsCycleType = GoodsEnums.GoodsCycleTypeByGreenJuice.DAYS3_PER_WEEK.getCode();

		List<HashMap<String, String>> goodsDailyCycleTermList = goodsGoodsService.getGoodsDailyCycleTermList(ilGoodsId, goodsCycleType);

		assertTrue(goodsDailyCycleTermList.size() == 0);
	}

	@Test
	void getGoodsDailyCycleTermGreenJuiceList_성공() throws Exception{

		List<HashMap<String, String>> goodsDailyCycleTermGreenJuiceList = goodsGoodsService.getGoodsDailyCycleTermGreenJuiceList();

		assertTrue(goodsDailyCycleTermGreenJuiceList.size() > 0);
	}

	@Test
	void getGoodsDailyCycleTermGreenJuiceList_조회결과없으면_안됨() throws Exception{

		List<HashMap<String, String>> goodsDailyCycleTermGreenJuiceList = goodsGoodsService.getGoodsDailyCycleTermGreenJuiceList();

		assertFalse(goodsDailyCycleTermGreenJuiceList.size() == 0);
	}

	@Test
	void getDailyCycleGreenJuiceWeekList_성공() {
		String storeDeliveryIntervalType = GoodsEnums.StoreDeliveryInterval.TWO_DAYS.getCode();

		List<HashMap<String, String>> dailyCycleGreenJuiceWeekList = goodsGoodsService.getDailyCycleGreenJuiceWeekList(storeDeliveryIntervalType);

		assertTrue(dailyCycleGreenJuiceWeekList.size() > 0);
	}

	@Test
	void getDailyCycleGreenJuiceWeekList_조회결과없으면_안됨() {
		String storeDeliveryIntervalType = GoodsEnums.StoreDeliveryInterval.TWO_DAYS.getCode();

		List<HashMap<String, String>> dailyCycleGreenJuiceWeekList = goodsGoodsService.getDailyCycleGreenJuiceWeekList(storeDeliveryIntervalType);

		assertNotEquals(dailyCycleGreenJuiceWeekList.size(), 0);
	}


	@Test
	void getGoodsPackageBasicItemImageList_성공() throws Exception{
		Long ilGoodsId = 11506L;

		List<GoodsImageDto> goodsImageList = goodsGoodsService.getGoodsPackageBasicItemImageList(ilGoodsId);

		assertTrue(goodsImageList.size() > 0);
	}

	@Test
	void getGoodsPackageBasicItemImageList_조회결과없음() throws Exception{
		Long ilGoodsId = 1L;

		List<GoodsImageDto> goodsImageList = goodsGoodsService.getGoodsPackageBasicItemImageList(ilGoodsId);

		assertTrue(goodsImageList.size() == 0);
	}

//	@Test
//	void getGoodsPackageGoodsImageList_성공() throws Exception{
//		//현재 테이블에 데이터 없음
//		Long ilGoodsId = 179L;
//
//		List<GoodsImageDto> goodsImageList = goodsGoodsService.getGoodsPackageGoodsImageList(ilGoodsId);
//
//		assertTrue(goodsImageList.size() > 0);
//
//	}

	@Test
	void getGoodsPackageGoodsImageList_조회결과없음() throws Exception{
		Long ilGoodsId = 1L;

		List<GoodsImageDto> goodsImageList = goodsGoodsService.getGoodsPackageGoodsImageList(ilGoodsId);

		assertTrue(goodsImageList.size() == 0);
	}

//	@Test
//	void getGoodsBasicImage_성공() throws Exception{
		//현재 테이블에 데이터 없음
//		Long ilGoodsId = 1L;
//
//		GoodsImageDto goodsPackageBasicGoodsImage = goodsGoodsService.getGoodsBasicImage(ilGoodsId);
//
//		assertNotNull(goodsPackageBasicGoodsImage);
//	}

	@Test
	void getGoodsBasicImage_조회결과없음() throws Exception{
		Long ilGoodsId = 1L;

		GoodsImageDto goodsPackageBasicGoodsImage = goodsGoodsService.getGoodsBasicImage(ilGoodsId);

		assertNull(goodsPackageBasicGoodsImage);
	}

	@Test
	void getItemImageList_성공() throws Exception{
		String ilItemCode = "BOS20200807181955";

		List<GoodsImageDto> goodsImageList = goodsGoodsService.getItemImageList(ilItemCode);

		assertTrue(goodsImageList.size() > 0);
	}

	@Test
	void getItemImageList_조회결과없음() throws Exception{
		String ilItemCode = "0000";

		List<GoodsImageDto> goodsImageList = goodsGoodsService.getItemImageList(ilItemCode);

		assertTrue(goodsImageList.size() == 0);
	}

	@Test
	void getItemBaiscImage_성공() throws Exception{
		String ilItemCode = "BOS20200807181955";

		GoodsImageDto goodsImageDto = goodsGoodsService.getItemBaiscImage(ilItemCode);

		assertNotNull(goodsImageDto);
	}

	@Test
	void getItemBaiscImage_조회결과없음() throws Exception{
		String ilItemCode = "0000";

		GoodsImageDto goodsImageDto = goodsGoodsService.getItemBaiscImage(ilItemCode);

		assertNull(goodsImageDto);
	}

	@Test
	void getStoreDeliveryTypeBySaleType_판매유형이_일일판매일때() throws Exception{
		String saleType = GoodsEnums.SaleType.DAILY.getCode();

		String storeType = goodsGoodsService.getStoreTypeBySaleType(saleType);

		assertTrue(storeType.equals(StoreEnums.StoreType.BRANCH.getCode()));
	}

	@Test
	void getStoreDeliveryTypeBySaleType_판매유형이_매장판매일때() throws Exception{
		String saleType = GoodsEnums.SaleType.SHOP.getCode();

		String storeType = goodsGoodsService.getStoreTypeBySaleType(saleType);

		assertTrue(storeType.equals(StoreEnums.StoreType.DIRECT.getCode()));
	}

	@Test
	void getStoreDeliveryTypeBySaleType_조회결과없음() throws Exception{
		String saleType = "SALE_TYPE.NONE";

		String storeType = goodsGoodsService.getStoreTypeBySaleType(saleType);

		assertTrue("".equals(storeType));
	}

	@Test
	void getStoreDeliverableItemTypeBySupplierId_공급처가_풀무원녹즙_FDD일때() throws Exception{
		Long urSupplierId = 4L;

		List<String> storeDeliverableItemTypeBySupplierId = goodsGoodsService.getStoreDeliverableItemTypeBySupplierId(urSupplierId);

		boolean anyMatch = storeDeliverableItemTypeBySupplierId.stream()
				 .anyMatch(m -> m.contains(GoodsEnums.StoreDeliverableItem.FD.getCode()));

		assertTrue(anyMatch);
	}

	@Test
	void getStoreDeliverableItemTypeBySupplierId_공급처가_풀무원녹즙_PDM일때() throws Exception{
		Long urSupplierId = 5L;

		List<String> storeDeliverableItemTypeBySupplierId = goodsGoodsService.getStoreDeliverableItemTypeBySupplierId(urSupplierId);

		boolean anyMatch = storeDeliverableItemTypeBySupplierId.stream()
				 .anyMatch(m -> m.contains(GoodsEnums.StoreDeliverableItem.DM.getCode()));

		assertTrue(anyMatch);
	}

	@Test
	void getStoreDeliverableItemTypeBySupplierId_공급처가_풀무원녹즙_FDD_PDM_둘다아닐때() throws Exception{
		Long urSupplierId = 1L;

		List<String> storeDeliverableItemTypeBySupplierId = goodsGoodsService.getStoreDeliverableItemTypeBySupplierId(urSupplierId);

		boolean allMatch = storeDeliverableItemTypeBySupplierId.stream()
				 .allMatch(m -> m.contains(GoodsEnums.StoreDeliverableItem.ALL.getCode()));

		assertTrue(allMatch);
	}

	@Test
	void isEveryOtherDay_true일때() throws Exception {
		String goodsCycleType = GoodsEnums.GoodsCycleTypeByGreenJuice.DAY1_PER_WEEK.getCode();

		boolean isEveryOtherDay = goodsGoodsService.isEveryOtherDay(goodsCycleType);

		assertTrue(isEveryOtherDay);
	}

	@Test
	void isEveryOtherDay_false일때() throws Exception{
		String goodsCycleType = GoodsEnums.GoodsCycleTypeByGreenJuice.DAYS5_PER_WEEK.getCode();

		boolean isEveryOtherDay = goodsGoodsService.isEveryOtherDay(goodsCycleType);

		assertFalse(isEveryOtherDay);

	}

	@Test
	void getGheckRangeDateList_성공() throws Exception{
		List<ArrivalScheduledDateDto> scheduledDateList = goodsGoodsService.getGheckRangeDateList();

		assertTrue(scheduledDateList.size() > 0);
	}


	@Test
	void setArrivalScheduledDateDtoByShippingPattern_조회결과없음() throws Exception{
		Long psShippingPatternId = 10L;
		List<ArrivalScheduledDateDto> scheduledDateList = goodsGoodsService.getGheckRangeDateList();
		Long ilGoodsId = 999L;

		scheduledDateList = goodsGoodsService.setArrivalScheduledDateDtoByShippingPattern(psShippingPatternId, scheduledDateList,ilGoodsId, 1);

		assertEquals(0, scheduledDateList.size());
	}

	@Test
	void employeeDiscountCalculation_성공() throws Exception{
		int employeeDiscountRate = 10;
		int employeeRemainingPoint = 1000000;
		int recommendedPrice = 10000;
		int qty = 1;
		EmployeeDiscountInfoDto employeeDiscountInfo = goodsGoodsService.employeeDiscountCalculation(employeeDiscountRate, employeeRemainingPoint, recommendedPrice, qty);

		assertEquals(9000, employeeDiscountInfo.getDiscountAppliedPrice());
	}

	@Test
	void getLatestArrivalScheduledDateDto_리스트가_없을때() throws Exception{
		List<ArrivalScheduledDateDto> list = null;

		ArrivalScheduledDateDto arrivalScheduledDateDto = goodsGoodsService.getLatestArrivalScheduledDateDto(list);

		assertNull(arrivalScheduledDateDto);
	}

	@Test
	void getLatestArrivalScheduledDateDto_리스트가_있을때() throws Exception{
		List<ArrivalScheduledDateDto> list = new ArrayList<>();
		ArrivalScheduledDateDto dto1 = new ArrivalScheduledDateDto();
		dto1.setOrderWeekCode("WEEK_CODE.MON");
		list.add(dto1);
		ArrivalScheduledDateDto dto2 = new ArrivalScheduledDateDto();
		dto2.setOrderWeekCode("WEEK_CODE.TUE");
		list.add(dto2);

		ArrivalScheduledDateDto arrivalScheduledDateDto = goodsGoodsService.getLatestArrivalScheduledDateDto(list);

		assertEquals("WEEK_CODE.MON",arrivalScheduledDateDto.getOrderWeekCode());
	}

	@Test
	void getGoodsDisplayInfo_성공() throws Exception{
		GoodsRequestDto goodsRequestDto = GoodsRequestDto.builder()
														.ilGoodsId(175L)
														.deviceInfo("pc")
														.isApp(true)
														.isMember(true)
														.isEmployee(true)
														.build();

		HashMap<String,String> getGoodsDisplayInfo = goodsGoodsService.getGoodsDisplayInfo(goodsRequestDto);

		assertNotNull(getGoodsDisplayInfo);
	}

	@Test
	void getGoodsDisplayInfo_조회결과없음() throws Exception{
		GoodsRequestDto goodsRequestDto = GoodsRequestDto.builder()
														.ilGoodsId(999L)
														.deviceInfo("pc")
														.isApp(true)
														.isMember(true)
														.isEmployee(true)
														.build();

		HashMap<String,String> getGoodsDisplayInfo = goodsGoodsService.getGoodsDisplayInfo(goodsRequestDto);

		assertNull(getGoodsDisplayInfo);
	}

	@Test
	void getArrivalScheduledDateDtoByArrivalScheduledDate_성공() throws Exception{
		List<ArrivalScheduledDateDto> list = new ArrayList<>();
		ArrivalScheduledDateDto dto1 = new ArrivalScheduledDateDto();
		dto1.setArrivalScheduledDate(LocalDate.of(2020, 9, 28));
		dto1.setOrderWeekCode("WEEK_CD.MON");
		list.add(dto1);
		ArrivalScheduledDateDto dto2 = new ArrivalScheduledDateDto();
		dto2.setArrivalScheduledDate(LocalDate.of(2020, 9, 27));
		dto2.setOrderWeekCode("WEEK_CD.TUE");
		list.add(dto2);
		LocalDate arrivalScheduledDate = LocalDate.of(2020, 9, 28);

		ArrivalScheduledDateDto arrivalScheduledDateDto =  goodsGoodsService.getArrivalScheduledDateDtoByArrivalScheduledDate(list,arrivalScheduledDate);

		assertEquals("WEEK_CD.MON",arrivalScheduledDateDto.getOrderWeekCode());
	}

	@Test
	void getArrivalScheduledDateDtoByOrderDate_성공() throws Exception{
		List<ArrivalScheduledDateDto> list = new ArrayList<>();
		ArrivalScheduledDateDto dto1 = new ArrivalScheduledDateDto();
		dto1.setOrderDate(LocalDate.of(2020, 9, 28));
		dto1.setOrderWeekCode("WEEK_CD.MON");
		list.add(dto1);
		ArrivalScheduledDateDto dto2 = new ArrivalScheduledDateDto();
		dto2.setOrderDate(LocalDate.of(2020, 9, 27));
		dto2.setOrderWeekCode("WEEK_CD.TUE");
		list.add(dto2);
		LocalDate orderDate = LocalDate.of(2020, 9, 28);

		ArrivalScheduledDateDto arrivalScheduledDateDto =  goodsGoodsService.getArrivalScheduledDateDtoByOrderDate(list,orderDate);

		assertEquals("WEEK_CD.MON",arrivalScheduledDateDto.getOrderWeekCode());
	}

	@Test
	void union_성공() throws Exception{
		List<LocalDate> list1 = new ArrayList<>();
		list1.add(LocalDate.of(2020, 9, 26));
		list1.add(LocalDate.of(2020, 9, 27));
		list1.add(LocalDate.of(2020, 9, 28));
		List<LocalDate> list2 = new ArrayList<>();
		list2.add(LocalDate.of(2020, 9, 28));
		list2.add(LocalDate.of(2020, 9, 29));
		list2.add(LocalDate.of(2020, 9, 30));

		List<LocalDate> unionList = goodsGoodsService.union(list1, list2);

		assertTrue(unionList.size() == 5);
	}

	@Test
	void intersection_성공() throws Exception{
		List<LocalDate> list1 = new ArrayList<>();
		list1.add(LocalDate.of(2020, 9, 26));
		list1.add(LocalDate.of(2020, 9, 27));
		list1.add(LocalDate.of(2020, 9, 28));
		List<LocalDate> list2 = new ArrayList<>();
		list2.add(LocalDate.of(2020, 9, 28));
		list2.add(LocalDate.of(2020, 9, 29));
		list2.add(LocalDate.of(2020, 9, 30));

		List<LocalDate> intersectionList = goodsGoodsService.intersection(list1, list2);

		assertEquals(LocalDate.of(2020, 9, 28),intersectionList.get(0));
	}

	@Test
	void intersectionArrivalScheduledDateListByDto_성공() throws Exception{
		List<List<ArrivalScheduledDateDto>> goodsArrivalScheduledDateList = new ArrayList<>();
		List<ArrivalScheduledDateDto> arrivalScheduledDateDtoList1 = new ArrayList<>();
		List<ArrivalScheduledDateDto> arrivalScheduledDateDtoList2 = new ArrayList<>();
		ArrivalScheduledDateDto dto1 = new ArrivalScheduledDateDto();
		dto1.setArrivalScheduledDate(LocalDate.of(2020, 9, 26));
		ArrivalScheduledDateDto dto2 = new ArrivalScheduledDateDto();
		dto2.setArrivalScheduledDate(LocalDate.of(2020, 9, 27));
		arrivalScheduledDateDtoList1.add(dto1);
		arrivalScheduledDateDtoList1.add(dto2);
		goodsArrivalScheduledDateList.add(arrivalScheduledDateDtoList1);

		ArrivalScheduledDateDto dto3 = new ArrivalScheduledDateDto();
		dto3.setArrivalScheduledDate(LocalDate.of(2020, 9, 25));
		ArrivalScheduledDateDto dto4 = new ArrivalScheduledDateDto();
		dto3.setArrivalScheduledDate(LocalDate.of(2020, 9, 26));
		arrivalScheduledDateDtoList2.add(dto3);
		arrivalScheduledDateDtoList2.add(dto4);
		goodsArrivalScheduledDateList.add(arrivalScheduledDateDtoList2);

		List<LocalDate> intersectionArrivalScheduledDateListByDtoList = goodsGoodsService.intersectionArrivalScheduledDateListByDto(goodsArrivalScheduledDateList);

		assertEquals(1,intersectionArrivalScheduledDateListByDtoList.size());
	}

	@Test
	void intersectionArrivalScheduledDateList_성공() throws Exception{
		List<List<LocalDate>> goodsArrivalScheduledDateList = new ArrayList<>();
		List<LocalDate> localDateList1 = new ArrayList<>();
		List<LocalDate> localDateList2 = new ArrayList<>();
		localDateList1.add(LocalDate.of(2020, 9, 25));
		localDateList1.add(LocalDate.of(2020, 9, 26));
		localDateList2.add(LocalDate.of(2020, 9, 26));
		localDateList2.add(LocalDate.of(2020, 9, 27));

		goodsArrivalScheduledDateList.add(localDateList1);
		goodsArrivalScheduledDateList.add(localDateList2);

		List<LocalDate> intersectionArrivalScheduledDateList = goodsGoodsService.intersectionArrivalScheduledDateList(goodsArrivalScheduledDateList);

		assertEquals(1,intersectionArrivalScheduledDateList.size());
	}

	@Test
	void intersectionArrivalScheduledDateDtoList_성공() throws Exception{
		List<ArrivalScheduledDateDto> list = new ArrayList<>();
		ArrivalScheduledDateDto dto1 = new ArrivalScheduledDateDto();
		dto1.setArrivalScheduledDate(LocalDate.of(2020, 9, 27));
		dto1.setOrderWeekCode("WEEK_CD.MON");
		list.add(dto1);
		ArrivalScheduledDateDto dto2 = new ArrivalScheduledDateDto();
		dto2.setArrivalScheduledDate(LocalDate.of(2020, 9, 28));
		dto2.setOrderWeekCode("WEEK_CD.TUE");
		list.add(dto2);
		List<LocalDate> arrivalScheduledDateList = new ArrayList<>();
		arrivalScheduledDateList.add(LocalDate.of(2020, 9, 28));

		List<ArrivalScheduledDateDto> intersectionArrivalScheduledDateDtoList = goodsGoodsService.intersectionArrivalScheduledDateDtoList(list, arrivalScheduledDateList);

		assertEquals("WEEK_CD.TUE",intersectionArrivalScheduledDateDtoList.get(0).getOrderWeekCode());
	}

	@Test
	void intersectionArrivalScheduledDateDtoList_리스트없음() throws Exception{
		List<ArrivalScheduledDateDto> list = new ArrayList<>();
		List<LocalDate> arrivalScheduledDateList = new ArrayList<>();

		List<ArrivalScheduledDateDto> intersectionArrivalScheduledDateDtoList = goodsGoodsService.intersectionArrivalScheduledDateDtoList(list, arrivalScheduledDateList);

		assertTrue(intersectionArrivalScheduledDateDtoList.size() == 0);
	}

	@Test
	void getMinStockArrivalScheduledDateDtoList_성공() throws Exception{
		List<ArrivalScheduledDateDto> list1 = new ArrayList<>();
		ArrivalScheduledDateDto dto1 = new ArrivalScheduledDateDto();
		dto1.setArrivalScheduledDate(LocalDate.of(2020, 9, 28));
		dto1.setStock(10);
		list1.add(dto1);
		ArrivalScheduledDateDto dto2 = new ArrivalScheduledDateDto();
		dto2.setArrivalScheduledDate(LocalDate.of(2020, 9, 29));
		dto2.setStock(2);
		list1.add(dto2);

		List<ArrivalScheduledDateDto> list2 = new ArrayList<>();
		ArrivalScheduledDateDto dto3 = new ArrivalScheduledDateDto();
		dto3.setArrivalScheduledDate(LocalDate.of(2020, 9, 28));
		dto3.setStock(1);
		list2.add(dto3);

		List<ArrivalScheduledDateDto> minStockArrivalScheduledDateDtoList = goodsGoodsService.getMinStockArrivalScheduledDateDtoList(list1, list2);

		assertEquals(1,minStockArrivalScheduledDateDtoList.get(0).getStock());
	}

	@Test
	void filterScheduledDateListByGoodsDailyCycleType_3DAY성공() throws Exception {
		List<ArrivalScheduledDateDto> list = new ArrayList<ArrivalScheduledDateDto>();

		LocalDate date = LocalDate.of(2020, 12, 16);
		for (int i = 0; i < 10; i++) {
			ArrivalScheduledDateDto dto = new ArrivalScheduledDateDto();
			dto.setArrivalScheduledDate(date.plusDays(i));
			list.add(dto);
		}

		List<ArrivalScheduledDateDto> returnList = goodsGoodsService
				.filterScheduledDateListByGoodsDailyCycleType(GoodsCycleTypeByGreenJuice.DAYS3_PER_WEEK.getCode(), list);

		assertEquals(5, returnList.size());
	}

	@Test
	void filterScheduledDateListByGoodsDailyCycleType_5DAY성공() throws Exception {
		List<ArrivalScheduledDateDto> list = new ArrayList<ArrivalScheduledDateDto>();

		LocalDate date = LocalDate.of(2020, 12, 16);
		for (int i = 0; i < 10; i++) {
			ArrivalScheduledDateDto dto = new ArrivalScheduledDateDto();
			dto.setArrivalScheduledDate(date.plusDays(i));
			list.add(dto);
		}

		List<ArrivalScheduledDateDto> returnList = goodsGoodsService
				.filterScheduledDateListByGoodsDailyCycleType(GoodsCycleTypeByGreenJuice.DAYS5_PER_WEEK.getCode(), list);

		assertEquals(8, returnList.size());
	}
	
	@Test
	void getAddDeliveryScheduleRangeDateList_도착예정일_추가_성공() throws Exception {
		int baseDay = 15;
		int addDay = 14;
		
		List<ArrivalScheduledDateDto> list = goodsGoodsService.getAddDeliveryScheduleRangeDateList(baseDay, addDay);
		
		assertEquals(LocalDate.now().plusDays(15 + 1), list.get(0).getOrderDate());
		
		assertEquals(addDay, list.size());
		
	}
}
