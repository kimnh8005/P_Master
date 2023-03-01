package kr.co.pulmuone.v1.goods.goods.service;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import kr.co.pulmuone.v1.goods.fooditem.dto.vo.FooditemIconVo;
import kr.co.pulmuone.v1.goods.item.service.MealContsBiz;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.pulmuone.v1.comm.constants.GoodsConstants;
import kr.co.pulmuone.v1.comm.enums.GoodsEnums;
import kr.co.pulmuone.v1.comm.enums.GoodsEnums.GoodsCycleTermType;
import kr.co.pulmuone.v1.comm.enums.GoodsEnums.GoodsCycleTermTypeQtyByGreenJuice;
import kr.co.pulmuone.v1.comm.enums.GoodsEnums.GoodsCycleType;
import kr.co.pulmuone.v1.comm.enums.GoodsEnums.GoodsCycleTypeByGreenJuice;
import kr.co.pulmuone.v1.comm.enums.GoodsEnums.WeekCodeByGreenJuice;
import kr.co.pulmuone.v1.comm.enums.StoreEnums;
import kr.co.pulmuone.v1.comm.enums.StoreEnums.WeekCode;
import kr.co.pulmuone.v1.comm.mapper.goods.goods.GoodsGoodsMapper;
import kr.co.pulmuone.v1.comm.util.PriceUtil;
import kr.co.pulmuone.v1.goods.goods.dto.ArrivalScheduledDateDto;
import kr.co.pulmuone.v1.goods.goods.dto.EmployeeDiscountInfoDto;
import kr.co.pulmuone.v1.goods.goods.dto.GoodsDailyCycleDto;
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
import kr.co.pulmuone.v1.order.order.dto.vo.OrderDetlVo;
import kr.co.pulmuone.v1.store.delivery.dto.ShippingPossibilityStoreDeliveryAreaDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * <PRE>
 * Forbiz Korea
 * Class의 기능과 역할을 상세히 기술한다.
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :   작성일          :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 *  1.0    20200824   	 천혜현            최초작성
 * =======================================================================
 * </PRE>
 */

@Slf4j
@Service
@SuppressWarnings("rawtypes")
@RequiredArgsConstructor
public class GoodsGoodsService {

	private final GoodsGoodsMapper goodsGoodsMapper;

	@Autowired
	private MealContsBiz mealContsBiz;

	/**
	 * 상품 기본 정보
	 *
	 * @param GoodsRequestDto
	 * @return BasicSelectGoodsVo
	 * @throws Exception
	 */
	protected BasicSelectGoodsVo getBasicSelectGoods(GoodsRequestDto goodsRequestDto) throws Exception {
		return goodsGoodsMapper.getBasicSelectGoods(goodsRequestDto);
	}

	/**
	 * 상품 판매상태
	 *
	 * @param saleStatus, stock
	 * @return String
	 * @throws Exception
	 */
	protected String getSaleStatus(String saleStatus, int stock) throws Exception {
		// 판매중인 상품에 대해 재고,구매수량으로 판매상태 다시 체크
		if (saleStatus.equals(GoodsEnums.SaleStatus.ON_SALE.getCode())) {
			// 재고가 0일때
			if (stock == 0) {
				return GoodsEnums.SaleStatus.OUT_OF_STOCK_BY_SYSTEM.getCode();
			}

		}
		return saleStatus;
	}

	/**
	 * 상품 상세 조회
	 *
	 * @param ilGoodsId
	 * @return DetailSelectGoodsVo
	 * @throws Exception
	 */
	protected DetailSelectGoodsVo getDetailSelectGoods(Long ilGoodsId) throws Exception {
		return goodsGoodsMapper.getDetailSelectGoods(ilGoodsId);
	}

	/**
	 * 상품 상세 조회(For Item_cd)
	 *
	 * @param ilItemCd
	 * @return DetailSelectGoodsVo
	 * @throws Exception
	 */
	protected DetailSelectGoodsVo getDetailSelectGoodsForItem(String ilItemCd) throws Exception {
		return goodsGoodsMapper.getDetailSelectGoodsForItemCd(ilItemCd);
	}

	/**
	 * 상품 상세 조회(For barCode)
	 *
	 * @param barCode
	 * @return DetailSelectGoodsVo
	 * @throws Exception
	 */
	protected DetailSelectGoodsVo getDetailSelectGoodsForBarCode(String barCode) throws Exception {
		return goodsGoodsMapper.getDetailSelectGoodsForBarCode(barCode);
	}

	/**
	 * 상품 인증정보
	 *
	 * @param ilGoodsId
	 * @return List<GoodsCertificationListResultVo>
	 * @throws Exception
	 */
	protected List<GoodsCertificationListResultVo> getGoodsCertificationList(Long ilGoodsId) throws Exception {
		return goodsGoodsMapper.getGoodsCertificationList(ilGoodsId);
	}

	/**
	 * 상품정보제공고시 항목
	 *
	 * @param ilGoodsId
	 * @return List<GoodsSpecListResultVo>
	 * @throws Exception
	 */
	protected List<GoodsSpecListResultVo> getGoodsSpecList(Long ilGoodsId) throws Exception {
		return goodsGoodsMapper.getGoodsSpecList(ilGoodsId);
	}

	/**
	 * 비회원구매가능 여부
	 *
	 * @param goodsType,saleType
	 * @return boolean
	 * @throws Exception
	 */
	protected boolean isBuyNonMember(String goodsType, String saleType, String limitMaxType) throws Exception {
		boolean result = true;

		// 상품 유형이 일일or매장, 판매유형이 정기, 최대구매수량 지정되어 있을 때 비회원구매불가
		if (goodsType.equals(GoodsEnums.GoodsType.DAILY.getCode())
				|| goodsType.equals(GoodsEnums.GoodsType.SHOP_ONLY.getCode())
				|| saleType.equals(GoodsEnums.SaleType.REGULAR.getCode())
				|| !limitMaxType.equals(GoodsEnums.PurchaseLimitMaxType.UNLIMIT.getCode())) {
			result = false;
		}

		return result;
	}

	/**
	 * 상품 영양정보
	 *
	 * @param ilItemCode
	 * @return List<GoodsNutritionListResultVo>
	 * @throws Exception
	 */
	protected List<GoodsNutritionListResultVo> getGoodsNutritionList(String ilItemCode) throws Exception {
		return goodsGoodsMapper.getGoodsNutritionList(ilItemCode);
	}

	/**
	 * 추가구성 상품 리스트
	 *
	 * @param ilGoodsId
	 * @return List<AdditionalGoodsResultVo>
	 * @throws Exception
	 */
	protected List<HashMap> getAdditionalGoodsList(Long ilGoodsId) throws Exception {
		return goodsGoodsMapper.getAdditionalGoodsList(ilGoodsId);
	}

	/**
	 * 묶음상품 정보
	 *
	 * @param ilGoodsId
	 * @return List<PackageGoodsResultVo>
	 * @throws Exception
	 */
	protected List<PackageGoodsResultVo> getPackageGoodsList(Long ilGoodsId) throws Exception {
		return goodsGoodsMapper.getPackageGoodsList(ilGoodsId);
	}

	/**
	 * 예약상품 옵션 리스트 정보
	 *
	 * @param ilGoodsId
	 * @return List<GoodsReserveOptionListDto>
	 * @throws Exception
	 */
	protected List<GoodsReserveOptionListDto> getGoodsReserveOptionList(Long ilGoodsId) throws Exception {
		return goodsGoodsMapper.getGoodsReserveOptionList(ilGoodsId);
	}

	/**
	 * 예약상품 옵션 정보
	 *
	 * @param ilGoodsId
	 * @return
	 * @throws Exception
	 */
	protected GoodsReserveOptionVo getGoodsReserveOption(Long ilGoodsReserveOptnId) throws Exception {
		return goodsGoodsMapper.getGoodsReserveOption(ilGoodsReserveOptnId);
	}

	/**
	 * 일괄배송
	 *
	 * @param ilGoodsId
	 * @return List<HashMap>
	 * @throws Exception
	 */
	protected List<HashMap<String, String>> getGoodsDailyBulkList(Long ilGoodsId) throws Exception {
		return goodsGoodsMapper.getGoodsDailyBulkList(ilGoodsId);
	}

	/**
	 * 일일배송 주기
	 *
	 * @param ilGoodsId
	 * @return List<HashMap>
	 * @throws Exception
	 */
	protected List<HashMap<String, String>> getGoodsDailyCycleList(Long ilGoodsId) throws Exception {
		List<HashMap<String, String>> goodsDailyCycleList = goodsGoodsMapper.getGoodsDailyCycleList(ilGoodsId);

		for(HashMap<String,String> goodsDailyCycleMap : goodsDailyCycleList) {
			for(GoodsCycleType goodsCycleType : GoodsEnums.GoodsCycleType.values()) {
				if(goodsDailyCycleMap.get("GOODS_CYCLE_TP").equals(goodsCycleType.getCode())) {
					goodsDailyCycleMap.put("GOODS_CYCLE_TP_QTY", goodsCycleType.getTypeQty());
					goodsDailyCycleMap.put("GOODS_CYCLE_TP_DAY_QTY", goodsCycleType.getTypeQty());
				}
			}
		}

		goodsDailyCycleList = goodsDailyCycleList.stream()
				.sorted((o1, o2) -> o1.get("GOODS_CYCLE_TP_DAY_QTY").compareTo(o2.get("GOODS_CYCLE_TP_DAY_QTY")) )
				.collect(Collectors.toList());

		return goodsDailyCycleList;
	}

	/**
	 * 일일 배송주기 - 녹즙
	 *
	 * @param
	 * @return List<HashMap>
	 * @throws Exception
	 */
	protected List<HashMap<String, String>> getGoodsDailyCycleGreenJuiceList() throws Exception {
		List<HashMap<String, String>> goodsDailyCycleGreenJuiceList = new ArrayList<>();

		// 녹즙은 배송주기가 정해져있어서 공통코드로 하드코딩
		for (GoodsCycleTypeByGreenJuice goodsCycleType : GoodsEnums.GoodsCycleTypeByGreenJuice.values()) {
			HashMap<String, String> goodsCycleTypeMap = new HashMap<>();
			goodsCycleTypeMap.put("GOODS_CYCLE_TP", goodsCycleType.getCode());
			goodsCycleTypeMap.put("GOODS_CYCLE_TP_NM", goodsCycleType.getCodeName());
			goodsCycleTypeMap.put("GOODS_CYCLE_TP_QTY", goodsCycleType.getSelectableDay());
			goodsCycleTypeMap.put("GOODS_CYCLE_TP_DAY_QTY", goodsCycleType.getDayQty());
			goodsDailyCycleGreenJuiceList.add(goodsCycleTypeMap);
		}

		return goodsDailyCycleGreenJuiceList;
	}

	/**
	 * 일일 배송기간 - 베이비밀,잇슬림
	 *
	 * @param ilGoodsId
	 * @return List<HashMap>
	 * @throws Exception
	 */
	protected List<HashMap<String, String>> getGoodsDailyCycleTermList(Long ilGoodsId, String goodsCycleType)
			throws Exception {

		List<HashMap<String, String>> goodsDailyCycleTermList = goodsGoodsMapper.getGoodsDailyCycleTermList(ilGoodsId, goodsCycleType);

		for(HashMap<String,String> goodsDailyCycleTermMap : goodsDailyCycleTermList) {
			for(GoodsCycleTermType goodsCycleTermType : GoodsEnums.GoodsCycleTermType.values()) {
				if(goodsDailyCycleTermMap.get("goodsDailyCycleTermType").equals(goodsCycleTermType.getCode())) {
					goodsDailyCycleTermMap.put("goodsDailyCycleTermTypeQty", goodsCycleTermType.getTypeQty());
				}
			}
		}

		 goodsDailyCycleTermList = goodsDailyCycleTermList.stream()
				 								.sorted((o1, o2) -> o1.get("goodsDailyCycleTermTypeQty").compareTo(o2.get("goodsDailyCycleTermTypeQty")) )
				 								.collect(Collectors.toList());

		return goodsDailyCycleTermList;
	}

	/**
	 * 일일 배송기간 - 녹즙
	 *
	 * @param
	 * @return List<HashMap>
	 * @throws Exception
	 */
	protected List<HashMap<String, String>> getGoodsDailyCycleTermGreenJuiceList() throws Exception {
		List<HashMap<String, String>> goodsDailyCycleTermGreenJuiceList = new ArrayList<>();

		// 녹즙은 배송기간 정해져있어서 공통코드로 하드코딩
		for (GoodsCycleTermTypeQtyByGreenJuice goodsCycleTermType : GoodsEnums.GoodsCycleTermTypeQtyByGreenJuice.values()) {
			HashMap<String, String> goodsCycleTermTypeMap = new HashMap<>();
			goodsCycleTermTypeMap.put("goodsDailyCycleTermType", goodsCycleTermType.getCode());
			goodsCycleTermTypeMap.put("goodsDailyCycleTermTypeName", goodsCycleTermType.getWeekName());
			for (GoodsCycleTermTypeQtyByGreenJuice goodsCycleTermTypeQty : GoodsEnums.GoodsCycleTermTypeQtyByGreenJuice
					.values()) {
				if (goodsCycleTermType.getCode().equals(goodsCycleTermTypeQty.getCode())) {
					goodsCycleTermTypeMap.put("goodsDailyCycleTermTypeQty", goodsCycleTermTypeQty.getCodeName());
				}
			}
			goodsDailyCycleTermGreenJuiceList.add(goodsCycleTermTypeMap);
		}

		return goodsDailyCycleTermGreenJuiceList;
	}

	/**
	 * 녹즙 요일코드
	 *
	 * @param storeDeliveryIntervalType String
	 * @return List<HashMap<String,String>>
	 */
	protected List<HashMap<String, String>> getDailyCycleGreenJuiceWeekList(String storeDeliveryIntervalType) {
		List<HashMap<String, String>> dailyCycleGreenJuiceWeekList = new ArrayList<>();

		// 요일코드
		// 녹즙은 요일이 정해져있어서 공통코드로 하드코딩
		// 스토어 배송권역정보가 격일일 경우 월,수,금 노출
		if (storeDeliveryIntervalType.equals(GoodsEnums.StoreDeliveryInterval.TWO_DAYS.getCode())) {
			for (WeekCodeByGreenJuice weekCodeByGreenJuice : GoodsEnums.WeekCodeByGreenJuice.values()) {
				if (weekCodeByGreenJuice.getCode().equals(GoodsEnums.WeekCodeByGreenJuice.MON.getCode())
						|| weekCodeByGreenJuice.getCode().equals(GoodsEnums.WeekCodeByGreenJuice.WED.getCode())
						|| weekCodeByGreenJuice.getCode().equals(GoodsEnums.WeekCodeByGreenJuice.FRI.getCode())) {
					HashMap<String, String> weekCodeByGreenJuiceMap = new HashMap<>();
					weekCodeByGreenJuiceMap.put("goodsDailyCycleGreenJuiceWeekType", weekCodeByGreenJuice.getCode());
					weekCodeByGreenJuiceMap.put("goodsDailyCycleGreenJuiceWeekTypeName",
							weekCodeByGreenJuice.getCodeName());
					dailyCycleGreenJuiceWeekList.add(weekCodeByGreenJuiceMap);
				}
			}

		// 스토어 배송권역정보가 매일일 경우  월,화,수,목,금 노출
		} else {
			for (WeekCodeByGreenJuice weekCodeByGreenJuice : GoodsEnums.WeekCodeByGreenJuice.values()) {
				HashMap<String, String> weekCodeByGreenJuiceMap = new HashMap<>();
				weekCodeByGreenJuiceMap.put("goodsDailyCycleGreenJuiceWeekType", weekCodeByGreenJuice.getCode());
				weekCodeByGreenJuiceMap.put("goodsDailyCycleGreenJuiceWeekTypeName",
						weekCodeByGreenJuice.getCodeName());
				dailyCycleGreenJuiceWeekList.add(weekCodeByGreenJuiceMap);
			}
		}

		return dailyCycleGreenJuiceWeekList;
	}

	/**
	 * 묶음상품의 품목이미지 중 기본이미지 리스트
	 *
	 * @param ilGoodsId
	 * @return List<GoodsImageDto>
	 * @throws Exception
	 */
	protected List<GoodsImageDto> getGoodsPackageBasicItemImageList(Long ilGoodsId) throws Exception {
		return goodsGoodsMapper.getGoodsPackageBasicItemImageList(ilGoodsId);
	}

	/**
	 * 묶음상품의 품목이미지 리스트
	 *
	 * @param ilGoodsId
	 * @return List<GoodsImageDto>
	 * @throws Exception
	 */
	protected List<GoodsImageDto> getGoodsPackageGoodsImageList(Long ilGoodsId) throws Exception {
		return goodsGoodsMapper.getGoodsPackageGoodsImageList(ilGoodsId);
	}

	/**
	 * 묶음상품의 품목이미지 중 기본 이미지
	 *
	 * @param ilGoodsId
	 * @return GoodsImageDto
	 * @throws Exception
	 */
	protected GoodsImageDto getGoodsBasicImage(Long ilGoodsId) throws Exception {
		return goodsGoodsMapper.getGoodsBasicImage(ilGoodsId);
	}

	/**
	 * 품목 이미지
	 *
	 * @param ilItemCode
	 * @return GoodsImageDto
	 * @throws Exception
	 */
	protected List<GoodsImageDto> getItemImageList(String ilItemCode) throws Exception {
		return goodsGoodsMapper.getItemImageList(ilItemCode);
	}

	/**
	 * 품목 기본 이미지
	 *
	 * @param ilItemCode
	 * @return
	 * @throws Exception
	 */
	protected GoodsImageDto getItemBaiscImage(String ilItemCode) throws Exception {
		return goodsGoodsMapper.getItemBaiscImage(ilItemCode);
	}

	/**
	 * 판매상태에 따른 스토어 타입
	 *
	 * @param saleType
	 * @return List<String>
	 * @throws Exception
	 */
	protected String getStoreTypeBySaleType(String saleType) throws Exception {

		// 판매유형이 일일판매일 때
		if (saleType.equals(GoodsEnums.SaleType.DAILY.getCode())) {
			// 스토어 배송유형 - 가맹점
			return StoreEnums.StoreType.BRANCH.getCode();

			// 판매유형이 매장판매일 때
		} else if (saleType.equals(GoodsEnums.SaleType.SHOP.getCode())) {
			// 스토어 배송유형 - 매장
			return StoreEnums.StoreType.DIRECT.getCode();
		}

		return "";
	}

	/**
	 * 배송가능품목유형
	 *
	 * @param urSupplierId
	 * @return List<String>
	 * @throws Exception
	 */
	protected List<String> getStoreDeliverableItemTypeBySupplierId(Long urSupplierId) throws Exception {
		List<String> storeDeliverableitemTypeList = new ArrayList<>();

		if (urSupplierId == GoodsConstants.GREEN_JUICE_UR_SUPPLIER_ID) {
			storeDeliverableitemTypeList.add(GoodsEnums.StoreDeliverableItem.ALL.getCode());
			storeDeliverableitemTypeList.add(GoodsEnums.StoreDeliverableItem.FD.getCode());

		} else if (urSupplierId == GoodsConstants.PDM_UR_SUPPLIER_ID) {
			storeDeliverableitemTypeList.add(GoodsEnums.StoreDeliverableItem.ALL.getCode());
			storeDeliverableitemTypeList.add(GoodsEnums.StoreDeliverableItem.DM.getCode());
		}

		return storeDeliverableitemTypeList;
	}

	/**
	 * 배송주기에 따른 격일 체크
	 *
	 * @param goodsCycleType
	 * @return boolean
	 * @throws Exception
	 */
	protected boolean isEveryOtherDay(String goodsCycleType) throws Exception {

		// 배송주기가 주1일, 주3일일 때, 격일
		if (goodsCycleType.equals(GoodsEnums.GoodsCycleTypeByGreenJuice.DAY1_PER_WEEK.getCode())
				|| goodsCycleType.equals(GoodsEnums.GoodsCycleTypeByGreenJuice.DAYS3_PER_WEEK.getCode())) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 출고예정일,도착 예정일 체크를 위한 일자 리스트
	 *
	 * @param goodsDailyCycleType
	 * @return
	 * @throws Exception
	 */
	protected List<ArrivalScheduledDateDto> getGheckRangeDateList() throws Exception {
		List<ArrivalScheduledDateDto> list = new ArrayList<ArrivalScheduledDateDto>();
		LocalDate now = LocalDate.now();
		for (int i = 0; i <= GoodsConstants.GOODS_STOCK_MAX_DDAY; i++) {
			ArrivalScheduledDateDto dto = new ArrivalScheduledDateDto();
			LocalDate orderDate = now.plusDays(i);
			dto.setOrderDate(orderDate);
			dto.setOrderWeekCode(StoreEnums.WeekCode.findByWeekValue(orderDate.getDayOfWeek().getValue()).getCode());
			list.add(dto);
		}
		return list;
	}
	
	/**
	 * 추가 출고예정일, 도착 예정일 생성
	 */
	protected List<ArrivalScheduledDateDto> getAddDeliveryScheduleRangeDateList(int baseDay, int addDay) throws Exception {
		List<ArrivalScheduledDateDto> list = new ArrayList<ArrivalScheduledDateDto>();
		LocalDate now = LocalDate.now();
		for (int i = baseDay+1; i <= baseDay + addDay; i++) {
			ArrivalScheduledDateDto dto = new ArrivalScheduledDateDto();
			LocalDate orderDate = now.plusDays(i);
			dto.setOrderDate(orderDate);
			dto.setOrderWeekCode(StoreEnums.WeekCode.findByWeekValue(orderDate.getDayOfWeek().getValue()).getCode());
			list.add(dto);
		}
		return list;
	}

    /**
     * 출고예정일,도착 예정일 체크를 위한 일자 리스트
     *
     * @param goodsDailyCycleType
     * @return
     * @throws Exception
     */
    protected List<ArrivalScheduledDateDto> getDaliyGheckRangeDateList(List<String> deliveryDateList) throws Exception {
        List<ArrivalScheduledDateDto> list = new ArrayList<ArrivalScheduledDateDto>();
        for (String deliveryDate : deliveryDateList) {
            ArrivalScheduledDateDto dto = new ArrivalScheduledDateDto();
            LocalDate orderDate = LocalDate.parse(deliveryDate);
            dto.setArrivalScheduledDate(orderDate);
            dto.setOrderWeekCode(StoreEnums.WeekCode.findByWeekValue(orderDate.getDayOfWeek().getValue()).getCode());
            list.add(dto);
        }
        return list;
    }

	/**
	 * 마감시간 30분전으로 체크
	 *
	 * @param time
	 * @return
	 * @throws Exception
	 */
	protected LocalTime convertCutoffTime(LocalTime time) throws Exception {
		if(time == null) {
			time = LocalTime.now();
		}
		return time.minusMinutes(0); //21.8.17 마감시간 30분체크 -> 0분으로 변경
	}

	/**
	 * 배송패던 데이터로 ArrivalScheduledDateDto set
	 *
	 * @param psShippingPatternId
	 * @param scheduledDateList
	 * @return
	 * @throws Exception
	 */
	protected List<ArrivalScheduledDateDto> setArrivalScheduledDateDtoByShippingPattern(Long psShippingPatternId,
			List<ArrivalScheduledDateDto> scheduledDateList, Long ilGoodsId, int buyQty) throws Exception {
		return goodsGoodsMapper.getArrivalScheduledDateDtoByShippingPattern(psShippingPatternId, scheduledDateList,
				ilGoodsId, buyQty);
	}

	protected List<ArrivalScheduledDateDto> setStoreArrivalScheduledDateDtoByShippingPattern(Long psShippingPatternId,
			List<ArrivalScheduledDateDto> scheduledDateList, int stock) throws Exception {
		return goodsGoodsMapper.setStoreArrivalScheduledDateDtoByShippingPattern(psShippingPatternId, scheduledDateList, stock);
	}

    protected List<ArrivalScheduledDateDto> setDaliyForwardingScheduledDateDtoByShippingPattern(Long psShippingPatternId,
                                                                                             List<ArrivalScheduledDateDto> scheduledDateList) throws Exception {
        return goodsGoodsMapper.setDaliyForwardingScheduledDateDtoByShippingPattern(psShippingPatternId, scheduledDateList);
    }
	
	/**
	 * 주문인터페이스 일자로 출고일 / 도착일 구하기
	 * @param scheduledDateList
	 * @return
	 */
	protected List<ArrivalScheduledDateDto> setShippingPatternDay(Long psShippingPatternId, List<ArrivalScheduledDateDto> scheduledDateList) throws Exception {
		List<ArrivalScheduledDateDto> arrivalScheduleList = new ArrayList<>();
		
		for(ArrivalScheduledDateDto arrivalScheduledDateDto : scheduledDateList) {
			ArrivalScheduledDateDto arrivalDate = goodsGoodsMapper.getShippingPatternDay(psShippingPatternId, arrivalScheduledDateDto.getOrderDate(), arrivalScheduledDateDto.getOrderWeekCode());
			
			if(arrivalDate != null) {
				arrivalScheduleList.add(arrivalDate);
			}
		}
		
		return arrivalScheduleList;
	}

	/**
	 * 임직원할인 계산
	 *
	 * @param employeeDiscountRate
	 * @param employeeRemainingPoint
	 * @param recommendedPrice
	 * @return
	 * @throws Exception
	 */
	protected EmployeeDiscountInfoDto employeeDiscountCalculation(int employeeDiscountRate, int employeeRemainingPoint,
			int recommendedPrice, int qty) throws Exception {
		EmployeeDiscountInfoDto employeeDiscountInfoDto = new EmployeeDiscountInfoDto();
		int excessPrice = 0; // 초과금액
		int discountPrice = 0; // 할인금액
		int discountAppliedPrice = 0; // 임직원 상품 할인 적용된 금액

		int recommendedPriceMltplQty = recommendedPrice * qty;

		// 임직원 상품 할인 적용된 금액
		int employeeDiscountSalePrice = PriceUtil.getPriceByRate(recommendedPrice, employeeDiscountRate);
		// 원단위 버림
		employeeDiscountSalePrice = (employeeDiscountSalePrice - (employeeDiscountSalePrice % 10)) * qty;

		// 판매 단가를 계산해야 되기때문에 나머지 빼기
//		int mod = employeeDiscountSalePrice % qty;
//		if (mod > 0) {
//			employeeDiscountSalePrice = employeeDiscountSalePrice - mod;
//		}

		// 임직원 상품 할인 적용된 금액
		if (recommendedPriceMltplQty - employeeDiscountSalePrice > employeeRemainingPoint) {
			excessPrice = recommendedPriceMltplQty - employeeDiscountSalePrice - employeeRemainingPoint;
			discountPrice = employeeRemainingPoint;
			discountAppliedPrice = recommendedPriceMltplQty - employeeRemainingPoint;
		} else {
			discountPrice = recommendedPriceMltplQty - employeeDiscountSalePrice;
			discountAppliedPrice = employeeDiscountSalePrice;
		}

		employeeDiscountInfoDto.setExcessPrice(excessPrice);
		employeeDiscountInfoDto.setDiscountPrice(discountPrice);
		employeeDiscountInfoDto.setDiscountAppliedPrice(discountAppliedPrice);
		employeeDiscountInfoDto.setSalePrice(discountAppliedPrice / qty);
		return employeeDiscountInfoDto;
	}

	/**
	 * get 첫번째 Dto
	 *
	 * @param list
	 * @return
	 * @throws Exception
	 */
	protected ArrivalScheduledDateDto getLatestArrivalScheduledDateDto(List<ArrivalScheduledDateDto> list)
			throws Exception {
		if (list == null || list.isEmpty()) {
			return null;
		} else {
			return list.get(0);
		}
	}

	/**
	 * 배송 예정일로 Dto 찾기
	 *
	 * @param list
	 * @param arrivalScheduledDate
	 * @return
	 * @throws Exception
	 */
	protected ArrivalScheduledDateDto getArrivalScheduledDateDtoByArrivalScheduledDate(
			List<ArrivalScheduledDateDto> list, LocalDate arrivalScheduledDate) throws Exception {
		return list.stream().filter(dto -> dto.getArrivalScheduledDate().equals(arrivalScheduledDate)).findAny()
				.orElse(null);
	}

	/**
	 * 주문(주문 I/F)일자 로 Dto 찾기
	 *
	 * @param list
	 * @param arrivalScheduledDate
	 * @return
	 * @throws Exception
	 */
	protected ArrivalScheduledDateDto getArrivalScheduledDateDtoByOrderDate(List<ArrivalScheduledDateDto> list, LocalDate orderDate) throws Exception {
		return list.stream().filter(dto -> dto.getOrderDate().equals(orderDate)).findAny().orElse(null);
	}

	/**
	 * 상품별 출고 예정일 합집합 리스트 리턴
	 *
	 * @param goodsArrivalScheduledDateList
	 * @return
	 * @throws Exception
	 */
	protected List<LocalDate> unionArrivalScheduledDateListByDto(
			List<List<ArrivalScheduledDateDto>> goodsArrivalScheduledDateList) throws Exception {
		List<LocalDate> returnList = new ArrayList<LocalDate>();
		for (List<ArrivalScheduledDateDto> arrivalScheduledDateList : goodsArrivalScheduledDateList) {
			if (arrivalScheduledDateList != null) {
				List<LocalDate> dateList = arrivalScheduledDateList.stream()
						.map(ArrivalScheduledDateDto::getArrivalScheduledDate).collect(Collectors.toList());
				if (returnList.size() == 0) {
					returnList = dateList;
				} else {
					returnList = union(returnList, dateList);
				}
			}
		}
		return returnList;
	}

	/**
	 * 상품별 출고 예정일 교집합 리스트 리턴
	 *
	 * @param goodsArrivalScheduledDateList
	 * @return
	 * @throws Exception
	 */
	protected List<LocalDate> intersectionArrivalScheduledDateListByDto(
			List<List<ArrivalScheduledDateDto>> goodsArrivalScheduledDateList) throws Exception {
		List<LocalDate> returnList = new ArrayList<LocalDate>();
		boolean first = true;
		for (List<ArrivalScheduledDateDto> arrivalScheduledDateList : goodsArrivalScheduledDateList) {
			if (arrivalScheduledDateList == null) {
				return new ArrayList<LocalDate>();
			} else {
				List<LocalDate> dateList = arrivalScheduledDateList.stream()
						.map(ArrivalScheduledDateDto::getArrivalScheduledDate).collect(Collectors.toList());
				if (first) {
					returnList = dateList;
					first = false;
				} else {
					returnList = intersection(returnList, dateList);
				}
			}
		}
		return returnList;
	}

	/**
	 * 출고 예정일 교집합 리스트 리턴
	 *
	 * @param goodsArrivalScheduledDateList
	 * @return
	 * @throws Exception
	 */
	protected List<LocalDate> intersectionArrivalScheduledDateList(List<List<LocalDate>> goodsArrivalScheduledDateList)
			throws Exception {
		List<LocalDate> returnList = new ArrayList<LocalDate>();
		boolean first = true;
		for (List<LocalDate> arrivalScheduledDateList : goodsArrivalScheduledDateList) {
			if (arrivalScheduledDateList == null) {
				return new ArrayList<LocalDate>();
			} else {
				if (first) {
					returnList = arrivalScheduledDateList;
					first = false;
				} else {
					returnList = intersection(returnList, arrivalScheduledDateList);
				}
			}
		}
		return returnList;
	}

	/**
	 * 출고 예정일 합집합 리스트 리턴
	 *
	 * @param list1
	 * @param list2
	 * @return
	 */
	protected List<LocalDate> union(List<LocalDate> list1, List<LocalDate> list2) {
		Set<LocalDate> set = new HashSet<LocalDate>();

		set.addAll(list1);
		set.addAll(list2);

		List<LocalDate> list = new ArrayList<LocalDate>(set);
		// 내림차순 정렬
		Collections.sort(list, (a, b) -> a.compareTo(b) > 0 ? 1 : -1);
		return list;
	}

	/**
	 * 출고 예정일 교집합 리스트 리턴
	 *
	 * @param list1
	 * @param list2
	 * @return
	 */
	protected List<LocalDate> intersection(List<LocalDate> list1, List<LocalDate> list2) {
		List<LocalDate> list = new ArrayList<LocalDate>();
		for (LocalDate t : list1) {
			if (list2.contains(t)) {
				list.add(t);
			}
		}
		return list;
	}

	/**
	 * 출고 예정일 Dto 리스트 추출
	 *
	 * @param list
	 * @param arrivalScheduledDateList
	 * @return
	 * @throws Exception
	 */
	protected List<ArrivalScheduledDateDto> intersectionArrivalScheduledDateDtoList(List<ArrivalScheduledDateDto> list,
			List<LocalDate> arrivalScheduledDateList) throws Exception {
		if (list != null && arrivalScheduledDateList != null) {
			return list.stream().filter(dto -> (arrivalScheduledDateList.indexOf(dto.getArrivalScheduledDate()) >= 0))
					.collect(Collectors.toList());
		} else {
			return null;
		}
	}

	/**
	 * 최소 수량으로 처리
	 *
	 * @param list1
	 * @param list2
	 * @return
	 */
	protected List<ArrivalScheduledDateDto> getMinStockArrivalScheduledDateDtoList(List<ArrivalScheduledDateDto> list1,
			List<ArrivalScheduledDateDto> list2) throws Exception {
		List<ArrivalScheduledDateDto> list = new ArrayList<ArrivalScheduledDateDto>();
		for (ArrivalScheduledDateDto dto1 : list1) {
			ArrivalScheduledDateDto dto2 = this.getArrivalScheduledDateDtoByArrivalScheduledDate(list2,
					dto1.getArrivalScheduledDate());
			if (dto2 != null) {
				ArrivalScheduledDateDto arrivalScheduledDateDto = dto1;
				if (dto1.getStock() > dto2.getStock()) {
					arrivalScheduledDateDto.setStock(dto2.getStock());
				}
				if (arrivalScheduledDateDto.getStock() > 0) {
					list.add(arrivalScheduledDateDto);
				}
			}
		}
		return list;
	}

	/**
	 * 상품 전시타입 정보 조회
	 *
	 */
	protected HashMap<String, String> getGoodsDisplayInfo(GoodsRequestDto goodsRequestDto) throws Exception {
		return goodsGoodsMapper.getGoodsDisplayInfo(goodsRequestDto);
	}

	/**
	 * 일일배송 주기에 따른 배송예정일 요일 필터
	 *
	 * @param list
	 * @return
	 * @throws Exception
	 */
	protected List<ArrivalScheduledDateDto> filterScheduledDateListByGoodsDailyCycleType(String goodsDailyCycleType,
			List<ArrivalScheduledDateDto> list) throws Exception {
		List<ArrivalScheduledDateDto> returnList = new ArrayList<ArrivalScheduledDateDto>();
		if (GoodsCycleType.DAYS3_PER_WEEK.getCode().equals(goodsDailyCycleType)) {
			for (ArrivalScheduledDateDto dto : list) {
				int weekValue = dto.getArrivalScheduledDate().getDayOfWeek().getValue();
				if (WeekCode.MON.getWeekValue() == weekValue || WeekCode.WED.getWeekValue() == weekValue
						|| WeekCode.FRI.getWeekValue() == weekValue) {
					returnList.add(dto);
				}
			}
		} else if (GoodsCycleType.DAYS4_PER_WEEK.getCode().equals(goodsDailyCycleType)) {
			for (ArrivalScheduledDateDto dto : list) {
				int weekValue = dto.getArrivalScheduledDate().getDayOfWeek().getValue();
				if (WeekCode.FRI.getWeekValue() != weekValue && WeekCode.SAT.getWeekValue() != weekValue
						&& WeekCode.SUN.getWeekValue() != weekValue) {
					returnList.add(dto);
				}
			}
		} else {
			for (ArrivalScheduledDateDto dto : list) {
				int weekValue = dto.getArrivalScheduledDate().getDayOfWeek().getValue();
				if (WeekCode.SAT.getWeekValue() != weekValue && WeekCode.SUN.getWeekValue() != weekValue) {
					returnList.add(dto);
				}
			}
		}
		return returnList;
	}

	protected OrderSelectGoodsVo getOrderGoodsInfo(Long ilGoodsId) throws Exception {
		return goodsGoodsMapper.getOrderGoodsInfo(ilGoodsId);
	}

	protected List<ArrivalScheduledDateDto> returnListByStoreAreaInfo(ShippingPossibilityStoreDeliveryAreaDto areaDto,
			List<ArrivalScheduledDateDto> list) throws Exception {
		if (areaDto != null && GoodsEnums.StoreDeliveryInterval.TWO_DAYS.getCode().equals(areaDto.getStoreDeliveryIntervalType())) {
			List<ArrivalScheduledDateDto> returnList = new ArrayList<ArrivalScheduledDateDto>();
			for (ArrivalScheduledDateDto dto : list) {
				int weekValue = dto.getArrivalScheduledDate().getDayOfWeek().getValue();
				if (WeekCode.MON.getWeekValue() == weekValue || WeekCode.WED.getWeekValue() == weekValue
						|| WeekCode.FRI.getWeekValue() == weekValue) {
					returnList.add(dto);
				}
			}
			return returnList;
		} else {
			return list;
		}
	}

	protected List<ArrivalScheduledDateDto> getArrivalScheduledDateDtoByWeekCode(List<ArrivalScheduledDateDto> list, List<String> weekCodes) throws Exception {
		if (list != null) {
			List<Integer> weekValues = new ArrayList<Integer>();
			for (String weekCode : weekCodes) {
				weekValues.add(WeekCode.findByCode(weekCode).getWeekValue());
			}
			return list.stream().filter(dto -> weekValues.contains(dto.getArrivalScheduledDate().getDayOfWeek().getValue()))
					.collect(Collectors.toList());
		} else {
			return new ArrayList<ArrivalScheduledDateDto>();
		}
	}

	protected ArrivalScheduledDateDto getArrivalScheduledDateDtoByIlGoodsIdArrivalScheduledDate(long ilGoodsId,
			LocalDate arrivalScheduledDate) throws Exception {
		DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		String arrivalScheduledDateStr = arrivalScheduledDate.format(dateFormatter);
		return goodsGoodsMapper.getArrivalScheduledDateDtoByIlGoodsIdArrivalScheduledDate(ilGoodsId, arrivalScheduledDateStr);
	}

	/**
	 * 정기배송일때 첫 결제 날짜 선택 일자 변경 (배치 주문 생성일자보다 빠른 도착예정일 제거)
	 *
	 * @param vo
	 * @throws Exception
	 */
	protected void convertRegularArrivalScheduledDate(BasicSelectGoodsVo vo) throws Exception {
		if (vo.getArrivalScheduledDateDtoList() != null && !vo.getArrivalScheduledDateDtoList().isEmpty()) {
			LocalDate now = LocalDate.now();
			// 도착 예정일이 + GoodsConstants.REGULAR_ARRIVAL_SCHEDULED_DATE_NOW_AFTER_DAY 일서부터 선택 가능 (배치에서 정기주문 회차별 주문생성,결제 하기 위한 Gap이 필요)
			vo.getArrivalScheduledDateDtoList().removeIf(dto -> now.isAfter(dto.getArrivalScheduledDate().minusDays(GoodsConstants.REGULAR_ARRIVAL_SCHEDULED_DATE_NOW_AFTER_DAY)));
			vo.setArrivalScheduledDateDto(getLatestArrivalScheduledDateDto(vo.getArrivalScheduledDateDtoList()));
		}
	}

	protected List<ArrivalScheduledDateDto> uniqueArrivalScheduledDateList(List<ArrivalScheduledDateDto> list) throws Exception {
		if (list != null && !list.isEmpty()) {
			List<ArrivalScheduledDateDto> returnList = new ArrayList<ArrivalScheduledDateDto>();
			List<LocalDate> dateList = list.stream().map(ArrivalScheduledDateDto::getArrivalScheduledDate).distinct()
					.collect(Collectors.toList());
			for (LocalDate date : dateList) {
				returnList.add(getArrivalScheduledDateDtoByArrivalScheduledDate(list, date));
			}
			return returnList;
		}
		return list;
	}

	protected List<ArrivalScheduledDateDto> convertScheduledDateListTuesday(List<ArrivalScheduledDateDto> list) throws Exception {
		if (list != null && !list.isEmpty()) {
			// 도착예정일이 화요일이 없고
			ArrivalScheduledDateDto checkTue = list.stream().filter(dto -> dto.getArrivalScheduledDate().getDayOfWeek().getValue() == WeekCode.TUE.getWeekValue()).findAny().orElse(null);
			if (checkTue == null) {
				List<ArrivalScheduledDateDto> returnList = new ArrayList<ArrivalScheduledDateDto>();
				// 도착예정일 월요일에 패턴이 두개가 있으면
				boolean action = false;
				for (ArrivalScheduledDateDto dto : list) {
					if (dto.getArrivalScheduledDate().getDayOfWeek().getValue() == WeekCode.MON.getWeekValue()) {
						action = true;
					}
					// 수요일이 두개 이상 있을수 있기 때문에 한번만 처리 되도록 action 값으로 처리
					if (action == true && dto.getArrivalScheduledDate().getDayOfWeek().getValue() != WeekCode.MON.getWeekValue()) {
						ArrivalScheduledDateDto tueDto = new ArrivalScheduledDateDto();
						tueDto.copy(returnList.get(returnList.size() - 1));
						tueDto.setArrivalScheduledDate(tueDto.getArrivalScheduledDate().plusDays(1));
						returnList.add(tueDto);
						action = false;
					}
					returnList.add(dto);
				}
				return returnList;
			}
		}
		return list;
	}

	protected List<ArrivalScheduledDateDto> getArrivalScheduledDateDtoAddWeek(List<ArrivalScheduledDateDto> list, int week) throws Exception {
		if (list != null && !list.isEmpty()) {
			ArrivalScheduledDateDto lastDto = list.get(list.size() - 1);
			int mi = week - list.size();
			for (int i = 1; i <= mi; i++) {
				ArrivalScheduledDateDto dto = new ArrivalScheduledDateDto();
				dto.copy(lastDto);
				dto.setOrderDate(dto.getOrderDate().plusWeeks(i));
				dto.setForwardingScheduledDate(dto.getForwardingScheduledDate().plusWeeks(i));
				dto.setArrivalScheduledDate(dto.getArrivalScheduledDate().plusWeeks(i));
				list.add(dto);
			}
		}
		return list;
	}

	protected List<ItemStoreInfoVo> getItemStoreInfoList(String urStoreId, List<String> ilItemCds) throws Exception {
		return goodsGoodsMapper.getItemStoreInfoList(urStoreId, ilItemCds);
	}

	/**
	 * 외부몰 업로드 묶음 구성상품 조회
	 * @param ilGoodsId
	 * @return
	 * @throws Exception
	 */
	protected List<OrderDetlVo> getOutmallGoodsPackList(long ilGoodsId) throws Exception {
		return goodsGoodsMapper.getOutmallGoodsPackList(ilGoodsId);
	}

	protected int getGoodsEmployeeDiscountRatio(long ilGoodsId) throws Exception {
		return goodsGoodsMapper.getGoodsEmployeeDiscountRatio(ilGoodsId);
	}

	protected List<GoodsPackageEmployeeDiscountDto> getPackageGoodsEmployeeDiscountRatio(Long ilGoodsId) throws Exception {
		return goodsGoodsMapper.getPackageGoodsEmployeeDiscountRatio(ilGoodsId);
	}

	protected boolean isCleanseOption(Long ilGoodsId) throws Exception {
		return goodsGoodsMapper.isCleanseOption(ilGoodsId);
	}

	protected void convertCleanseOption(String StoreDeliveryIntervalType, List<GoodsDailyCycleDto> goodsDailyCycleList) throws Exception {
		for (GoodsDailyCycleDto goodsDailyCycle : goodsDailyCycleList) {
			if (GoodsCycleType.DAY1_PER_WEEK.getCode().equals(goodsDailyCycle.getGoodsDailyCycleType())) {
				goodsDailyCycle.setTerm(getGoodsDailyCycleTermGreenJuiceList().stream()
						.filter(dto -> GoodsCycleTermType.WEEK4.getCode().equals(dto.get("goodsDailyCycleTermType"))
								|| GoodsCycleTermType.WEEK8.getCode().equals(dto.get("goodsDailyCycleTermType")))
						.collect(Collectors.toList()));
				goodsDailyCycle.setWeek(goodsDailyCycle.getWeek().stream()
						.filter(dto -> WeekCodeByGreenJuice.MON.getCode().equals(dto.get("goodsDailyCycleGreenJuiceWeekType"))
								|| (WeekCodeByGreenJuice.THU.getCode().equals(dto.get("goodsDailyCycleGreenJuiceWeekType")) && GoodsEnums.StoreDeliveryInterval.EVERY.getCode().equals(StoreDeliveryIntervalType) )
								|| WeekCodeByGreenJuice.FRI.getCode().equals(dto.get("goodsDailyCycleGreenJuiceWeekType")))
						.collect(Collectors.toList()));
			} else {
				goodsDailyCycle.setIsEnable(false);
			}
		}
	}

	/**
	 * 선물하기 가능 여부
	 *
	 * @param list
	 * @return
	 * @throws Exception
	 */
	protected boolean isPresentPossible(List<ArrivalScheduledDateDto> list) throws Exception {
		if (list == null || list.isEmpty()) {
			return false;
		} else {
			// 선물하기 마지막 배송 일자가 +5일이 아닌경우
			ArrivalScheduledDateDto lastArrivalScheduledDateDto = list.get(list.size() - 1);
			if (!LocalDate.now().isBefore(lastArrivalScheduledDateDto.getArrivalScheduledDate().minusDays(5))) {
				return false;
			}
		}
		return true;
	}

	protected void convertOverlapBuyItem(HashMap<String, Integer> overlapBuyItem, String ilItemCode, int buyQty, boolean isDawnDelivery, List<ArrivalScheduledDateDto> arrivalScheduledDateDtoList) {
		int overlapQty = 0;
		String key = isDawnDelivery + "|" + ilItemCode;
		if (overlapBuyItem.containsKey(key)) {
			overlapQty = overlapBuyItem.get(key);

			if (arrivalScheduledDateDtoList != null) {
				Iterator<ArrivalScheduledDateDto> iter = arrivalScheduledDateDtoList.iterator();
				while (iter.hasNext()) {
					ArrivalScheduledDateDto dto = iter.next();
					// 선주문 또는 무재한 재고는 패스
					if(dto.getStock() != 9999) {
						// 동일 주문건에 같은 품목 구매 수량 + 구매하려는 수량 비교
						if (dto.getStock() < overlapQty + buyQty) {
							iter.remove();
						} else {
							dto.setStock(dto.getStock() - overlapQty);
						}
					}
				}
			}
		}
		overlapBuyItem.put(key, overlapQty + buyQty);
	}

	protected void getDailyGoodsFixedFastestOrderIfDate(LocalDate goodsDailyTpIfDate, List<ArrivalScheduledDateDto> scheduledDateList) {
		for(ArrivalScheduledDateDto arrSchdDate : scheduledDateList) {
			arrSchdDate.setOrderDate(goodsDailyTpIfDate);
		}
	}

	protected GoodsPageInfoMealDto getGoodsPageInfoMeal(Long ilGoodsId) throws Exception {
		return goodsGoodsMapper.getGoodsPageInfoMeal(ilGoodsId);
	}

	protected List<LocalDate> getMealPossibleDeliveryDateList(Long ilGoodsId, List<LocalDate> goodsArrivalScheduledDateList) throws Exception {
		return goodsGoodsMapper.getMealPossibleDeliveryDateList(ilGoodsId, goodsArrivalScheduledDateList);
	}

	protected List<GoodsDailyMealScheduleDto> getGoodsDailyMealSchedule(GoodsDailyMealScheduleRequestDto reqDto) throws Exception {
		List<GoodsDailyMealScheduleDto> result = new ArrayList<GoodsDailyMealScheduleDto>();
		List<GoodsDailyMealScheduleDataDto> List = goodsGoodsMapper.getGoodsDailyMealSchedule(reqDto);

		if (List != null) {
			Map<LocalDate, Map<String, List<GoodsDailyMealScheduleDataDto>>> groupByList = List.stream()
					.collect(Collectors.groupingBy(GoodsDailyMealScheduleDataDto::getDeliveryDate, LinkedHashMap::new,
							groupingBy(GoodsDailyMealScheduleDataDto::getAllergyYn, LinkedHashMap::new, toList())));

			for (Map.Entry<LocalDate, Map<String, List<GoodsDailyMealScheduleDataDto>>> entry : groupByList.entrySet()) {
				LocalDate deliveryDate = entry.getKey();
				Map<String, List<GoodsDailyMealScheduleDataDto>> allergyGroupbyList = entry.getValue();
				GoodsDailyMealScheduleDto dto = new GoodsDailyMealScheduleDto();
				String holidayYn = null;
				String holidayName = null;
				List<GoodsDailyMealScheduleDataDto> basic = null;
				List<GoodsDailyMealScheduleDataDto> noAllergy = null;
				for (Map.Entry<String, List<GoodsDailyMealScheduleDataDto>> subEntry : allergyGroupbyList.entrySet()) {
					String allergyYn = subEntry.getKey();
					List<GoodsDailyMealScheduleDataDto> mealList = subEntry.getValue();
					if("Y".equals(allergyYn)) {
						noAllergy = mealList; 	// 알러지 대체 식단
					} else {
						basic = mealList;		// 일반식단
					}
					holidayYn = mealList.get(0).getHolidayYn();
					holidayName = mealList.get(0).getHolidayName();
				}

				// 일괄 배송일경우
				if("Y".equals(reqDto.getBulkYn())) {
					if (noAllergy != null && !noAllergy.isEmpty()) {
						List<String> noAllergySetNoList = noAllergy.stream().map(GoodsDailyMealScheduleDataDto::getSetNo).collect(toList());
						if (noAllergySetNoList != null && basic != null && !basic.isEmpty()) {
							noAllergy.addAll(basic.stream().filter(d->!noAllergySetNoList.contains(d.getSetNo())).collect(toList()));
						}
					}
				}

				dto.setDeliveryDate(deliveryDate);
				dto.setHolidayYn(holidayYn);
				dto.setHolidayName(holidayName);
				dto.setBasic(basic);
				dto.setNoAllergy(noAllergy);
				result.add(dto);
			}
		}

		return result;
	}

	protected GoodsDailyMealContentsDto getGoodsDailyMealContents(String ilGoodsDailyMealContsCd) throws Exception {
		GoodsDailyMealContentsDto goodsDailyMealContentsDto = goodsGoodsMapper.getGoodsDailyMealContents(ilGoodsDailyMealContsCd);

		// 식단컨텐츠 아이콘 조회
		List<FooditemIconVo> fooditemIconVoList = mealContsBiz.getFooditemIconList(ilGoodsDailyMealContsCd);
		goodsDailyMealContentsDto.setFooditemIconList(fooditemIconVoList);

		return goodsDailyMealContentsDto;
	}

	protected List<ArrivalScheduledDateDto> getArrivalScheduledDateDtoByShippingPatternAndWeekCode(List<ArrivalScheduledDateDto> list, Long urWarehouseId, String weekCode) throws Exception {
		return goodsGoodsMapper.getArrivalScheduledDateDtoByShippingPatternAndWeekCode(list, urWarehouseId, weekCode);
	}
}
