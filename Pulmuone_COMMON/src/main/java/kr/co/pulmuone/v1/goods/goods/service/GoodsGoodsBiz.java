package kr.co.pulmuone.v1.goods.goods.service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;

import kr.co.pulmuone.v1.goods.goods.dto.AdditionalGoodsDto;
import kr.co.pulmuone.v1.goods.goods.dto.ArrivalScheduledDateDto;
import kr.co.pulmuone.v1.goods.goods.dto.EmployeeDiscountInfoDto;
import kr.co.pulmuone.v1.goods.goods.dto.GoodsDailyCycleDto;
import kr.co.pulmuone.v1.goods.goods.dto.GoodsDailyMealContentsDto;
import kr.co.pulmuone.v1.goods.goods.dto.GoodsDailyMealScheduleDto;
import kr.co.pulmuone.v1.goods.goods.dto.GoodsDailyMealScheduleRequestDto;
import kr.co.pulmuone.v1.goods.goods.dto.GoodsImageDto;
import kr.co.pulmuone.v1.goods.goods.dto.GoodsPackageEmployeeDiscountDto;
import kr.co.pulmuone.v1.goods.goods.dto.GoodsPageInfoMealDto;
import kr.co.pulmuone.v1.goods.goods.dto.GoodsRequestDto;
import kr.co.pulmuone.v1.goods.goods.dto.GoodsReserveOptionListDto;
import kr.co.pulmuone.v1.goods.goods.dto.MallGoodsDetailDto;
import kr.co.pulmuone.v1.goods.goods.dto.PackageGoodsListDto;
import kr.co.pulmuone.v1.goods.goods.dto.RecalculationPackageDto;
import kr.co.pulmuone.v1.goods.goods.dto.StorePriceDto;
import kr.co.pulmuone.v1.goods.goods.dto.vo.BasicSelectGoodsVo;
import kr.co.pulmuone.v1.goods.goods.dto.vo.DetailSelectGoodsVo;
import kr.co.pulmuone.v1.goods.goods.dto.vo.GoodsCertificationListResultVo;
import kr.co.pulmuone.v1.goods.goods.dto.vo.GoodsNutritionListResultVo;
import kr.co.pulmuone.v1.goods.goods.dto.vo.GoodsReserveOptionVo;
import kr.co.pulmuone.v1.goods.goods.dto.vo.GoodsSpecListResultVo;
import kr.co.pulmuone.v1.goods.goods.dto.vo.ItemStoreInfoVo;
import kr.co.pulmuone.v1.goods.goods.dto.vo.OrderSelectGoodsVo;
import kr.co.pulmuone.v1.order.order.dto.vo.OrderDetlVo;
import kr.co.pulmuone.v1.policy.benefit.dto.vo.PolicyBenefitEmployeeByUserVo;
import kr.co.pulmuone.v1.store.delivery.dto.ShippingPossibilityStoreDeliveryAreaDto;

public interface GoodsGoodsBiz {

	BasicSelectGoodsVo getBasicSelectGoods(GoodsRequestDto goodsRequestDto) throws Exception;

	BasicSelectGoodsVo getGoodsBasicInfo(GoodsRequestDto goodsRequestDto) throws Exception;

	DetailSelectGoodsVo getDetailSelectGoods(Long ilGoodsId) throws Exception;

	DetailSelectGoodsVo getDetailSelectGoodsForItem(String ilItemCd) throws Exception;

	DetailSelectGoodsVo getDetailSelectGoodsForBarCode(String barCode) throws Exception;

	List<GoodsCertificationListResultVo> getGoodsCertificationList(Long ilGoodsId) throws Exception;

	List<GoodsSpecListResultVo> getGoodsSpecList(Long ilGoodsId) throws Exception;

	boolean isBuyNonMember(String goodsType, String saleType, String limitMaxType) throws Exception;

	List<GoodsNutritionListResultVo> getGoodsNutritionList(String ilItemCode) throws Exception;

	List<AdditionalGoodsDto> getAdditionalGoodsInfoList(Long ilGoodsId, boolean isMember, boolean isEmployee, boolean isDawnDelivery, LocalDate arrivalScheduledDate) throws Exception;

	List<PackageGoodsListDto> getPackagGoodsInfoList(Long ilGoodsId, boolean isMember, boolean isEmployee, boolean isDawnDelivery, LocalDate arrivalScheduledDate, int buyQty) throws Exception;

	List<PackageGoodsListDto> getPackagGoodsInfoList(Long ilGoodsId, boolean isMember, boolean isEmployee, boolean isDawnDelivery, LocalDate arrivalScheduledDate, int buyQty, HashMap<String, Integer> overlapBuyItem) throws Exception;

	RecalculationPackageDto getRecalculationPackage(String goodsSaleStatus, List<PackageGoodsListDto> packageGoodsList) throws Exception;

	List<GoodsReserveOptionListDto> getGoodsReserveOptionList(Long ilGoodsId) throws Exception;

	List<GoodsDailyCycleDto> getGreenJuiceDailyCycleList(String zipCode, String buildingCode) throws Exception;

	List<GoodsDailyCycleDto> getGoodsDailyCycleList(Long ilGoodsId, String goodsDailyType, String zipCode, String buildingCode) throws Exception;

	List<HashMap<String,String>> getGoodsDailyBulkList(Long ilGoodsId) throws Exception;

	List<GoodsImageDto> getGoodsImageList(String goodsType, Long ilGoodsId, String ilItemCode, String goodsPackageImageType) throws Exception;

	GoodsImageDto getGoodsBasicImage(String goodsType, Long ilGoodsId, String ilItemCode, String goodsPackageImageType) throws Exception;

	String getStoreTypeBySaleType(String saleType) throws Exception;

	List<String> getStoreDeliverableItemTypeBySupplierId(Long urSupplierId) throws Exception;

	boolean isEveryOtherDay(String goodsCycleType) throws Exception;

	EmployeeDiscountInfoDto employeeDiscountCalculation(int employeeDiscountRate, int employeeRemainingPoint, int recommendedPrice, int qty) throws Exception;

	EmployeeDiscountInfoDto employeeDiscountCalculationPackage(Long ilGoodsId, List<PolicyBenefitEmployeeByUserVo> policyBenefitEmployeeInfo, List<PackageGoodsListDto> goodsPackageList, int qty) throws Exception;

	void employeeDiscountCalculationAddGoods(List<PolicyBenefitEmployeeByUserVo> policyBenefitEmployeeInfo, List<AdditionalGoodsDto> additionalGoodsList) throws Exception;

	EmployeeDiscountInfoDto sumEmployeeDiscountInfoDtoList(List<EmployeeDiscountInfoDto> list) throws Exception;

	List<ArrivalScheduledDateDto> getArrivalScheduledDateDtoList(Long urWarehouseId, Long ilGoodsId, boolean isDawnDelivery, String goodsDailyCycleType) throws Exception;
	
	List<ArrivalScheduledDateDto> getAddDeliveryScheduleDateForPreOrderGoods(Long urWarehouseId, boolean isDawnDelivery, int baseDay, int addDay) throws Exception;
	
	void getDailyGoodsFixedFastestOrderIfDate(LocalDate goodsDailyTpIfDate, List<ArrivalScheduledDateDto> scheduledDateList) throws Exception;

	List<ArrivalScheduledDateDto> getArrivalScheduledDateDtoListForBabymealBulk(Long urWarehouseId, Long ilGoodsId, boolean isDawnDelivery, int buyQty, String goodsDailyCycleType) throws Exception;

	Long getBasicStoreWarehouseStoreId() throws Exception;

	List<ArrivalScheduledDateDto> getStoreArrivalScheduledDateDtoList(Long urWarehouseId, String urStoreId, int stock, int buyQty) throws Exception;

	List<String> getDaliyForwardingScheduledDateDtoList(Long urWarehouseId, List<String> deliveryDateList) throws Exception;

	List<ArrivalScheduledDateDto> getDailyDeliveryArrivalScheduledDateDtoList(Long urWarehouseId, Long ilGoodsId, int buyQty) throws Exception;

	List<ArrivalScheduledDateDto> getArrivalScheduledDateDtoList(Long urWarehouseId, Long ilGoodsId, boolean isDawnDelivery, int buyQty, String goodsDailyCycleType) throws Exception;

	ArrivalScheduledDateDto getLatestArrivalScheduledDateDto(Long urWarehouseId, Long ilGoodsId, boolean isDawnDelivery, String goodsDailyCycleType) throws Exception;

	ArrivalScheduledDateDto getLatestArrivalScheduledDateDto(List<ArrivalScheduledDateDto> list) throws Exception;

	ArrivalScheduledDateDto getArrivalScheduledDateDtoByArrivalScheduledDate(List<ArrivalScheduledDateDto> list, LocalDate arrivalScheduledDate) throws Exception;

	ArrivalScheduledDateDto getArrivalScheduledDateDtoByOrderDate(List<ArrivalScheduledDateDto> list, LocalDate orderDate) throws Exception;

	List<LocalDate> unionArrivalScheduledDateListByDto(List<List<ArrivalScheduledDateDto>> goodsArrivalScheduledDateList) throws Exception;

	List<LocalDate> intersectionArrivalScheduledDateListByDto(List<List<ArrivalScheduledDateDto>> goodsArrivalScheduledDateList) throws Exception;

	List<ArrivalScheduledDateDto> intersectionArrivalScheduledDateDtoList(List<ArrivalScheduledDateDto> list, List<LocalDate> arrivalScheduledDateList) throws Exception;

	List<ArrivalScheduledDateDto> getMinStockArrivalScheduledDateDtoList(List<ArrivalScheduledDateDto> list1, List<ArrivalScheduledDateDto> list2) throws Exception;

	List<LocalDate> intersectionArrivalScheduledDateList(List<List<LocalDate>> goodsArrivalScheduledDateList) throws Exception;

	GoodsReserveOptionVo getGoodsReserveOption(Long ilGoodsReserveOptnId) throws Exception;

	HashMap<String,String> getGoodsDisplayInfo(GoodsRequestDto goodsRequestDto) throws Exception;

	OrderSelectGoodsVo getOrderGoodsInfo(Long ilGoodsId) throws Exception;

	MallGoodsDetailDto getMallGoodsDetail(Long ilGoodsId) throws Exception;

	LocalDate getNextArrivalScheduledDate(List<LocalDate> list, LocalDate date) throws Exception;

	List<ArrivalScheduledDateDto> returnListByStoreAreaInfo(ShippingPossibilityStoreDeliveryAreaDto areaDto, List<ArrivalScheduledDateDto> list) throws Exception;

	List<ArrivalScheduledDateDto> getArrivalScheduledDateDtoListByWeekCode(List<ArrivalScheduledDateDto> list, Long urWarehouseId, String GoodsDailyCycleType, String weekCode) throws Exception;

	ArrivalScheduledDateDto getArrivalScheduledDateDtoByIlGoodsIdArrivalScheduledDate(long ilGoodsId, LocalDate arrivalScheduledDate) throws Exception;

	void convertRegularArrivalScheduledDate(BasicSelectGoodsVo vo) throws Exception;

	List<OrderDetlVo> getOutmallGoodsPackList(long ilGoodsId) throws Exception;

	int getGoodsEmployeeDiscountRatio(Long ilGoodsId) throws Exception;

	List<GoodsPackageEmployeeDiscountDto> getPackageGoodsEmployeeDiscountRatio(Long ilGoodsId) throws Exception;

	GoodsPackageEmployeeDiscountDto findGoodsPackageEmployeeDiscountDto(List<GoodsPackageEmployeeDiscountDto> list, Long ilGoodsId) throws Exception;

	String getSaleStatus(String saleStatus, int stock) throws Exception;

	List<ItemStoreInfoVo> getItemStoreInfoList(String urStoreId, List<String> ilItemCds) throws Exception;

	ItemStoreInfoVo getItemStoreInfo(String urStoreId, String ilItemCd) throws Exception;

	StorePriceDto getStoreSalePrice(String mallDiscountType, int mallRecommendedPrice, int mallSalePrice, int storeSalePrice) throws Exception;

	boolean isPresentPossible(List<ArrivalScheduledDateDto> list) throws Exception;

	void convertOverlapBuyItem(HashMap<String, Integer> overlapBuyItem, String ilItemCode, int buyQty, boolean isDawnDelivery, List<ArrivalScheduledDateDto> arrivalScheduledDateDtoList);

	GoodsPageInfoMealDto getGoodsPageInfoMeal(Long ilGoodsId, List<ArrivalScheduledDateDto> goodsArrivalScheduledDateDtoList) throws Exception;

	List<GoodsDailyMealScheduleDto> getGoodsDailyMealSchedule(GoodsDailyMealScheduleRequestDto reqDto) throws Exception;

	GoodsDailyMealContentsDto getGoodsDailyMealContents(String ilGoodsDailyMealContsCd) throws Exception;

	List<ArrivalScheduledDateDto> getArrivalScheduledDateDtoByShippingPatternAndWeekCode(List<ArrivalScheduledDateDto> list, Long urWarehouseId, String weekCode) throws Exception;
}
