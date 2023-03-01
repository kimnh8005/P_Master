package kr.co.pulmuone.v1.goods.goods.dto.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "GoodsPriceInfoResultVo")
public class GoodsPriceInfoResultVo
{
	/**
	 * 가격정보 > 판매 가격정보 관련
	 **/
	@ApiModelProperty(value = "상품 가격 ID")
	private String ilGoodsPriceId;

	@ApiModelProperty(value = "임직원 가격 ID")
	private String ilGoodsEmployeePriceId;

	@ApiModelProperty(value = "상품 ID")
	private String ilGoodsId;

	@ApiModelProperty(value = "품목 가격 ID")
	private String ilItemPriceId;

	@ApiModelProperty(value = "상품 할인 ID")
	private String ilGoodsDiscountId;

	@ApiModelProperty(value = "할인유형명")
	private String goodsTypeName;

	@ApiModelProperty(value = "가격 시작일")
	private String priceStartDate;

	@ApiModelProperty(value = "가격 종료일")
	private String priceEndDate;

	@ApiModelProperty(value = "원가")
	private int standardPrice;

	@ApiModelProperty(value = "정상가")
	private int recommendedPrice;

	@ApiModelProperty(value = "정상가 총액")
	private int recommendedTotalPrice;

	@ApiModelProperty(value = "구성수량")
	private int goodsQuantity;

	@ApiModelProperty(value = "할인액")
	private int discountPrice;

	@ApiModelProperty(value = "할인총액")
	private int discountTotalPrice;

	@ApiModelProperty(value = "할인율")
	private int discountRate;

	@ApiModelProperty(value = "판매가")
	private int salePrice;

	/**
	 * 가격정보 > 행사/할인 내역 관련
	 **/
	@ApiModelProperty(value = "리스트 2개 이상 non visible")
	private boolean trListVisible;

	@ApiModelProperty(value = "리스트 2개 이상 display none")
	private String displayAllow;

	@ApiModelProperty(value = "row 갯수")
	private int rowCountNum;

	@ApiModelProperty(value = "상품 ID")
	private String goodsId;

	@ApiModelProperty(value = "상품할인 승인 ID")
	private String goodsDiscountApprId;

	@ApiModelProperty(value = "상품명")
	private String goodsName;

	@ApiModelProperty(value = "상품 할인 ID")
	private String goodsDiscountId;

	@ApiModelProperty(value = "상품할인 유형 공통코드")
	private String discountTypeCode;

	@ApiModelProperty(value = "상품할인 유형명")
	private String discountTypeCodeName;

	@ApiModelProperty(value = "승인코드 > 현재 미개발 상태")
	private String approvalStatusCode;

	@ApiModelProperty(value = "승인명 > 현재 미개발 상태")
	private String approvalStatusCodeName;

	@ApiModelProperty(value = "할인 시작일(시간, 분 포함)")
	private String discountStartDateTime;

	@ApiModelProperty(value = "할인 시작일")
	private String discountStartDate;

	@ApiModelProperty(value = "할인 시작시간")
	private String discountStartHour;

	@ApiModelProperty(value = "할인 시작분")
	private String discountStartMinute;

	@ApiModelProperty(value = "할인 종료일(시간, 분 포함)")
	private String discountEndDateTime;

	@ApiModelProperty(value = "할인 시작일")
	private String discountEndDate;

	@ApiModelProperty(value = "할인 시작시간")
	private String discountEndHour;

	@ApiModelProperty(value = "할인 시작분")
	private String discountEndMinute;

	@ApiModelProperty(value = "할인 시작일 원본")
	private String discountEndDateOriginal;

	@ApiModelProperty(value = "할인 시작시간 원본")
	private String discountEndHourOriginal;

	@ApiModelProperty(value = "할인 시작분 원본")
	private String discountEndMinuteOriginal;

	@ApiModelProperty(value = "할인가격 시작일(시간, 분 포함)")
	private String priceStartDateTime;

	@ApiModelProperty(value = "할인가격 종료일(시간, 분 포함)")
	private String priceEndDateTime;

	@ApiModelProperty(value = "상품할인 방법 유형 공통코드")
	private String discountMethodTypeCode;

	@ApiModelProperty(value = "상품할인 방법명")
	private String discountMethodTypeCodeName;

	@ApiModelProperty(value = "품목 정상가")
	private long itemRecommendedPrice;

	@ApiModelProperty(value = "품목 원가")
	private long itemStandardPrice;

	@ApiModelProperty(value = "할인액")
	private long discountAmount;

	@ApiModelProperty(value = "마진율")
	private long marginRate;

	private String approveId;
	private String approveName;

	@ApiModelProperty(value = "승인요청자 ID")
	private String apprReqUserId;

	@ApiModelProperty(value = "승인요청자명")
	private String apprReqNm;

	@ApiModelProperty(value = "승인요청자 LOGIN ID")
	private String apprReqUserLoginId;

	@ApiModelProperty(value = "승인요청자 정보")
	private String apprReqInfo;

	@ApiModelProperty(value = "1차승인자 ID")
	private String apprSubUserId;

	@ApiModelProperty(value = "1차승인자명")
	private String apprSubNm;

	@ApiModelProperty(value = "1차승인자 LOGIN ID")
	private String apprSubUserLoginId;

	@ApiModelProperty(value = "2차승인자 ID")
	private String apprUserId;

	@ApiModelProperty(value = "2차승인자명")
	private String apprNm;

	@ApiModelProperty(value = "2차승인자 LOGIN ID")
	private String apprUserLoginId;

	@ApiModelProperty(value = "승인관리자 정보")
	private String apprInfo;

	@ApiModelProperty(value = "할인율")
	private int discountRatio;

	@ApiModelProperty(value = "할인판매단가")
	private int discountUnitSalePrice;

	@ApiModelProperty(value = "할인판매가")
	private int discountSalePrice;

	/**
	 * 가격정보 > 마스터 품목 가격정보 관련
	 **/
	@ApiModelProperty(value = "아이템 CD")
	private String ilItemCode;

	@ApiModelProperty(value = "등록 구분")
	private String regType;

	@ApiModelProperty(value = "등록 구분명")
	private String regTypeName;

	@ApiModelProperty(value = "시작일")
	private String startDate;

	@ApiModelProperty(value = "종료일")
	private String endDate;

	@ApiModelProperty(value = "상태")
	private String status;

	@ApiModelProperty(value = "상태명")
	private String statusName;

	/**
	 * 임직원 할인 정보 > 임직원 할인 가격정보 관련
	 **/
	@ApiModelProperty(value = "할인가격정보 그룹 ID")
	private String employeePriceGroupId;

	/**
	 * 임직원 할인 정보 > 임직원 기본할인 정보 관련
	 **/
	@ApiModelProperty(value = "행번호")
	private int rowNum;

	@ApiModelProperty(value = "할인그룹별 번호")
	private int rowGroupNum;

	@ApiModelProperty(value = "묶음 상품 총 구성 갯수")
	private int rowCount;

	@ApiModelProperty(value = "현재 할인 정보 ROW")
	private boolean todayRow;

	@ApiModelProperty(value = "묶음상품 관리 ID")
	private String ilGoodsPackageGoodsMappingId;

	@ApiModelProperty(value = "브랜드 ID")
	private String urBrandId;

	@ApiModelProperty(value = "과세여부")
	private String taxYn;

	@ApiModelProperty(value = "과세여부 텍스트")
	private String taxName;

	@ApiModelProperty(value = "기준상품 여부")
	private String baseGoodsYn;
}
