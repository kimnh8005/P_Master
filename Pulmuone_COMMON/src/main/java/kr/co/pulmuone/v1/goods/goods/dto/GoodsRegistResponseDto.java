package kr.co.pulmuone.v1.goods.goods.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.goods.goods.dto.vo.*;
import kr.co.pulmuone.v1.goods.item.dto.ItemStoreInfoDto;
import kr.co.pulmuone.v1.goods.item.dto.vo.MealPatternListVo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
@ApiModel(description = "상품 등록/수정 Response")
public class GoodsRegistResponseDto {

	@ApiModelProperty(value = "")
	private GoodsRegistVo row;

	@ApiModelProperty(value = "")
	private	List<GoodsRegistVo> rows = new ArrayList<GoodsRegistVo>();

	@ApiModelProperty(value = "묶음상품 구성정보 리스트")
	private List<GoodsPackageGoodsMappingVo> goodsPackageGoodsMappingList = new ArrayList<GoodsPackageGoodsMappingVo>();

	@ApiModelProperty(value = "묶음상품 상품정보 제공고시 리스트")
	private List<GoodsRegistVo> goodsInfoAnnounceList = new ArrayList<GoodsRegistVo>();

	@ApiModelProperty(value = "묶음상품 상품 영양정보 리스트")
	private List<GoodsRegistVo> goodsNutritionList = new ArrayList<GoodsRegistVo>();

	@ApiModelProperty(value = "")
	private	List<GoodsRegistItemWarehouseVo> warehouseRows = new ArrayList<GoodsRegistItemWarehouseVo>();

	@ApiModelProperty(value = "생성된 상품명")
	private String goodsName;

	@ApiModelProperty(value = "상품 유형")
	private String goodsType;

	@ApiModelProperty(value = "생성된 상품ID")
	private String ilGoodsId;

	@ApiModelProperty(value = "판매상태공통코드(SALE_STATUS)")
	private String saleStatus;

	@ApiModelProperty(value = "상품 정보")
	private GoodsRegistVo ilGoodsDetail;

	@ApiModelProperty(value = "상품 전시 카테고리 리스트")
	private List<GoodsRegistCategoryVo> ilGoodsDisplayCategoryList;

	@ApiModelProperty(value = "상품 몰인몰 카테고리 리스트")
	private List<GoodsRegistCategoryVo> ilGoodsMallinmallCategoryList;

	@ApiModelProperty(value = "판매 가격정보 내역")
	private List<GoodsPriceInfoResultVo> goodsPrice;

	@ApiModelProperty(value = "임직원 할인 가격정보 내역")
	private List<GoodsPriceInfoResultVo> goodsEmployeePrice;

	@ApiModelProperty(value = "행사/할인 내역 > 우선할인")
	private List<GoodsPriceInfoResultVo> goodsDiscountPriorityList;

	@ApiModelProperty(value = "행사/할인 내역 > ERP행사")
	private List<GoodsPriceInfoResultVo> goodsDiscountErpEventList;

	@ApiModelProperty(value = "행사/할인 내역 > 즉시할인")
	private List<GoodsPriceInfoResultVo> goodsDiscountImmediateList;

	@ApiModelProperty(value = "임직원 할인 가격정보")
	private List<GoodsPriceInfoResultVo> goodsDiscountEmployeeList;

	@ApiModelProperty(value = "묶음상품 > 묶음상품 기본 할인가")
	private List<GoodsPriceInfoResultVo> goodsPackagePriceList;

	@ApiModelProperty(value = "마스터 품목 가격정보")
	private List<GoodsPriceInfoResultVo> itemPriceList;

	@ApiModelProperty(value = "예약 판매 옵션 설정")
	private List<GoodsRegistReserveOptionVo> goodsReservationOptionList;

	@ApiModelProperty(value = "상품 배송 유형에 따른 배송 정책 값")
	private List<GoodsRegistShippingTemplateVo> goodsShippingTemplateList;

	@ApiModelProperty(value = "추가 상품")
	private List<GoodsRegistAdditionalGoodsVo> goodsAdditionalGoodsMappingList;

	@ApiModelProperty(value = "추천 상품")
	private List<GoodsRegistAdditionalGoodsVo> goodsRecommendList;

	@ApiModelProperty(value = "결과 Return Message")
	private String returnMessage;

	@ApiModelProperty(value = "묶음 상품 조합 배송 불가 지역 CODE")
	private String undeliverableAreaTypeAssembleCode;

	@ApiModelProperty(value = "묶음 상품 조합 배송 불가 지역")
	private String undeliverableAreaTypeAssembleCodeName;

	@ApiModelProperty(value = "묶음 상품 조합 반품 가능 기간")
	private String assembleReturnPeriodValue;

	@ApiModelProperty(value = "묶음 상품 전용 이미지 리스트")
	private List<GoodsRegistImageVo> goodsPackageImageList;

	@ApiModelProperty(value = "개별상품 상품별 이미지 리스트")
	private List<GoodsPackageGoodsMappingVo> goodsImageList;

	@ApiModelProperty(value = "일일 상품 전용 식단 주기 리스트")
	private List<GoodsDailyCycleBulkVo> goodsDailyCyclelist;

	@ApiModelProperty(value = "일일 상품 전용 일괄배달설정 리스트")
	private List<GoodsDailyCycleBulkVo> goodsDailyBulklist;

	@ApiModelProperty(value = "가격 계산 상품 리스트")
	private List<GoodsPackageCalcListVo> goodsPackageCalcList;

	@ApiModelProperty(value = "일반상품 > 임직원 할인 정보 > 임직원 기본할인 정보")
	private GoodsPriceInfoResultVo goodsBaseDiscountEmployeeList;

	@ApiModelProperty(value = "묶음상품 > 임직원 할인 정보 > 임직원 할인 가격정보")
	private List<GoodsPriceInfoResultVo> goodsPackageEmployeePriceList;

	@ApiModelProperty(value = "묶음상품 > 임직원 할인 정보 > 임직원 기본할인 정보")
	private List<GoodsPriceInfoResultVo> goodsPackageBaseDiscountEmployeeList;

	@ApiModelProperty(value = "묶음상품 > 임직원 할인 정보 > 임직원 개별할인 정보")
	private List<GoodsPriceInfoResultVo> goodsPackageDiscountEmployeeList;

	@ApiModelProperty(value = "일반, 일일상품 > 혜택/구매 정보 > 증정행사 정보")
	private List<ExhibitGiftResultVo> exhibitGiftList;

	@ApiModelProperty(value = "판매/전시 > 재고운영형태, 전일마감재고 정보")
	private GoodsRegistStockInfoVo goodsStockInfo;

	@ApiModelProperty(value = "판매/전시 > 승인 상태")
	private List<GoodsApprovalResultVo> goodsApprStatusList;

	@ApiModelProperty(value = "풀무원샵 상품코드 정보")
	private List<GoodsCodeVo> goodsCodeList;

	@ApiModelProperty(value = "품목 매장정보 조회")
	private List<ItemStoreInfoDto> itemStoreList;

	@ApiModelProperty(value = "품목 매장정보 조회")
	private List<MealPatternListVo> mealScheduleList;

}
