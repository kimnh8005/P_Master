package kr.co.pulmuone.v1.comm.mapper.goods.goods;

import java.util.HashMap;
import java.util.List;
import java.time.LocalDate;
import kr.co.pulmuone.v1.order.order.dto.vo.OrderDetlVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import kr.co.pulmuone.v1.goods.goods.dto.ArrivalScheduledDateDto;
import kr.co.pulmuone.v1.goods.goods.dto.GoodsDailyMealContentsDto;
import kr.co.pulmuone.v1.goods.goods.dto.GoodsDailyMealScheduleDataDto;
import kr.co.pulmuone.v1.goods.goods.dto.GoodsDailyMealScheduleDto;
import kr.co.pulmuone.v1.goods.goods.dto.GoodsDailyMealScheduleRequestDto;
import kr.co.pulmuone.v1.goods.goods.dto.GoodsImageDto;
import kr.co.pulmuone.v1.goods.goods.dto.GoodsPackageEmployeeDiscountDto;
import kr.co.pulmuone.v1.goods.goods.dto.GoodsPageInfoMealDto;
import kr.co.pulmuone.v1.goods.goods.dto.GoodsRequestDto;
import kr.co.pulmuone.v1.goods.goods.dto.GoodsReserveOptionListDto;
import kr.co.pulmuone.v1.goods.goods.dto.vo.BasicSelectGoodsVo;
import kr.co.pulmuone.v1.goods.goods.dto.vo.DetailSelectGoodsVo;
import kr.co.pulmuone.v1.goods.goods.dto.vo.GoodsCertificationListResultVo;
import kr.co.pulmuone.v1.goods.goods.dto.vo.GoodsNutritionListResultVo;
import kr.co.pulmuone.v1.goods.goods.dto.vo.GoodsReserveOptionVo;
import kr.co.pulmuone.v1.goods.goods.dto.vo.GoodsSpecListResultVo;
import kr.co.pulmuone.v1.goods.goods.dto.vo.ItemStoreInfoVo;
import kr.co.pulmuone.v1.goods.goods.dto.vo.OrderSelectGoodsVo;
import kr.co.pulmuone.v1.goods.goods.dto.vo.PackageGoodsResultVo;

@Mapper
@SuppressWarnings("rawtypes")
public interface GoodsGoodsMapper {

	BasicSelectGoodsVo getBasicSelectGoods(GoodsRequestDto goodsRequestDto) throws Exception;

	DetailSelectGoodsVo getDetailSelectGoods(Long ilGoodsId) throws Exception;

	DetailSelectGoodsVo getDetailSelectGoodsForItemCd(String ilItemCd) throws Exception;

	DetailSelectGoodsVo getDetailSelectGoodsForBarCode(String barCode) throws Exception;

	List<GoodsCertificationListResultVo> getGoodsCertificationList(Long ilGoodsId) throws Exception;

	List<GoodsSpecListResultVo> getGoodsSpecList(Long ilGoodsId) throws Exception;

	List<GoodsNutritionListResultVo> getGoodsNutritionList(String ilItemCode) throws Exception;

	List<HashMap> getAdditionalGoodsList(Long ilGoodsId) throws Exception;

	List<PackageGoodsResultVo> getPackageGoodsList(Long ilGoodsId) throws Exception;

	List<GoodsReserveOptionListDto> getGoodsReserveOptionList(Long ilGoodsId) throws Exception;

	List<HashMap<String, String>> getGoodsDailyCycleList(Long ilGoodsId) throws Exception;

	List<HashMap<String, String>> getGoodsDailyCycleTermList(@Param("ilGoodsId") Long ilGoodsId,
			@Param("goodsCycleType") String goodsCycleType) throws Exception;

	List<HashMap> getGoodsDailyCycleGreenJuiceList(Long ilGoodsId) throws Exception;

	List<HashMap<String, String>> getGoodsDailyBulkList(Long ilGoodsId) throws Exception;

	List<GoodsImageDto> getItemImageList(String ilItemCode) throws Exception;

	List<GoodsImageDto> getGoodsPackageBasicItemImageList(Long ilGoodsId) throws Exception;

	List<GoodsImageDto> getGoodsPackageGoodsImageList(Long ilGoodsId) throws Exception;

	GoodsImageDto getGoodsBasicImage(Long ilGoodsId) throws Exception;

	GoodsImageDto getItemBaiscImage(String ilItemCode) throws Exception;

	List<ArrivalScheduledDateDto> getArrivalScheduledDateDtoByShippingPattern(
			@Param("psShippingPatternId") Long psShippingPatternId,
			@Param("scheduledDateList") List<ArrivalScheduledDateDto> scheduledDateList,
			@Param("ilGoodsId") Long ilGoodsId, @Param("buyQty") int buyQty) throws Exception;

	List<ArrivalScheduledDateDto> setStoreArrivalScheduledDateDtoByShippingPattern(
			@Param("psShippingPatternId") Long psShippingPatternId,
			@Param("scheduledDateList") List<ArrivalScheduledDateDto> scheduledDateList, @Param("stock") int stock) throws Exception;

	List<ArrivalScheduledDateDto> setDaliyForwardingScheduledDateDtoByShippingPattern(
			@Param("psShippingPatternId") Long psShippingPatternId,
			@Param("scheduledDateList") List<ArrivalScheduledDateDto> scheduledDateList) throws Exception;

	GoodsReserveOptionVo getGoodsReserveOption(Long ilGoodsReserveOptnId) throws Exception;

	HashMap<String, String> getGoodsDisplayInfo(GoodsRequestDto goodsRequestDto) throws Exception;

	OrderSelectGoodsVo getOrderGoodsInfo(Long ilGoodsId) throws Exception;

	ArrivalScheduledDateDto getArrivalScheduledDateDtoByIlGoodsIdArrivalScheduledDate(
			@Param("ilGoodsId") Long ilGoodsId,
			@Param("arrivalScheduledDate") String arrivalScheduledDateStr
			);

	List<ItemStoreInfoVo> getItemStoreInfoList(@Param("urStoreId") String urStoreId, @Param("ilItemCds") List<String> ilItemCds) throws Exception;

	List<OrderDetlVo> getOutmallGoodsPackList(long ilGoodsId) throws Exception;

	int getGoodsEmployeeDiscountRatio(Long ilGoodsId) throws Exception;

	List<GoodsPackageEmployeeDiscountDto> getPackageGoodsEmployeeDiscountRatio(Long ilGoodsId) throws Exception;

	boolean isCleanseOption(Long ilGoodsId) throws Exception;

	GoodsPageInfoMealDto getGoodsPageInfoMeal(Long ilGoodsId) throws Exception;

	List<LocalDate> getMealPossibleDeliveryDateList(@Param("ilGoodsId") Long ilGoodsId, @Param("goodsArrivalScheduledDateList") List<LocalDate> goodsArrivalScheduledDateList) throws Exception;

	List<GoodsDailyMealScheduleDataDto> getGoodsDailyMealSchedule(GoodsDailyMealScheduleRequestDto reqDto) throws Exception;

	GoodsDailyMealContentsDto getGoodsDailyMealContents(String ilGoodsDailyMealContsCd) throws Exception;

	List<ArrivalScheduledDateDto> getArrivalScheduledDateDtoByShippingPatternAndWeekCode(@Param("scheduledDateList")List<ArrivalScheduledDateDto> list, @Param("urWarehouseId")Long urWarehouseId, @Param("weekCode")String weekCode);
	
	ArrivalScheduledDateDto getShippingPatternDay(
			@Param("psShippingPatternId") Long psShippingPatternId
			, @Param("orderDate") LocalDate orderDate
			, @Param("orderWeekCode") String orderWeekCode
	) throws Exception;
}
