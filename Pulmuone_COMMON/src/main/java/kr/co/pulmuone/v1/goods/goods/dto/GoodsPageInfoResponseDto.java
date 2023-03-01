package kr.co.pulmuone.v1.goods.goods.dto;

import java.util.HashMap;
import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.customer.feedback.dto.FeedbackScorePercentDto;
import kr.co.pulmuone.v1.goods.goods.dto.vo.GoodsCertificationListResultVo;
import kr.co.pulmuone.v1.goods.goods.dto.vo.GoodsNutritionListResultVo;
import kr.co.pulmuone.v1.goods.goods.dto.vo.GoodsSpecListResultVo;
import kr.co.pulmuone.v1.goods.notice.dto.GoodsNoticeResponseDto;
import kr.co.pulmuone.v1.promotion.exhibit.dto.GiftListResponseDto;
import kr.co.pulmuone.v1.search.searcher.dto.GoodsSearchResultDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "상품 상세 조회 응답 Dto")
public class GoodsPageInfoResponseDto {

  @ApiModelProperty(value = "상품 PK")
  private Long                                 ilGoodsId;

  @ApiModelProperty(value = "상품 타입")
  private String                               goodsType;

  @ApiModelProperty(value = "판매유형")
  private String                               saleType;

  @ApiModelProperty(value = "상품 이미지")
  private List<HashMap>                        goodsImage;

  @ApiModelProperty(value = "공급처 PK")
  private Long                               urSupplierId;

  @ApiModelProperty(value = "전시 브랜드명")
  private String                               brandName;

  @ApiModelProperty(value = "표준 브랜드명")
  private String                               urBrandName;

  @ApiModelProperty(value = "표준 브랜드 PK")
  private Long                                 urBrandId;

  @ApiModelProperty(value = "전시 브랜드 PK")
  private Long                                 dpBrandId;

  @ApiModelProperty(value = "상품 명")
  private String                               goodsName;

  @ApiModelProperty(value = "상품 간략 설명")
  private String                               goodsDesc;

  @ApiModelProperty(value = "정가")
  private int                                  recommendedPrice;

  @ApiModelProperty(value = "판매가")
  private int                                  salePrice;

  @ApiModelProperty(value = "할인율")
  private int                                  discountRate;

  @ApiModelProperty(value = "비회원 구매 가능 여부")
  private Boolean                              isBuyNonMember;

  @ApiModelProperty(value = "회원 구매 가능 여부")
  private Boolean                              isBuyMember;

  @ApiModelProperty(value = "입직원 구매 가능 여부")
  private Boolean                              isBuyEmployee;

  @ApiModelProperty(value = "매장배송 상품 구매 가능 여부")
  private Boolean                              isBuyStoreGoods;

  @ApiModelProperty(value = "찜한상품 PK")
  private Long                                 spFavoritesGoodsId;

  @ApiModelProperty(value = "할인명")
  private String                               discountTypeName;

  @ApiModelProperty(value = "할인금액")
  private int                                  discountPrice;

  @ApiModelProperty(value = "쿠폰 할인금액")
  private int                                  couponDiscountPrice;

  @ApiModelProperty(value = "일반회원 상품 결제 예정 금액")
  private int                                  buyerPaymentExpectedPrice;

  @ApiModelProperty(value = "임직원 할인금액")
  private int                                  employeeDiscountPrice;

  @ApiModelProperty(value = "임직원 상품 결제 예정금액")
  private int                                  employeePaymentExpectedPrice;

  @ApiModelProperty(value = "매장 판매가")
  private int                                  storeSalePrice;

  @ApiModelProperty(value = "매장 할인 금액")
  private int                                  storeDiscountPrice;

  @ApiModelProperty(value = "매장 쿠폰 할인 금액")
  private int                                  storeCouponDiscountPrice;

  @ApiModelProperty(value = "매장 상품 결제 예정금액")
  private int                                  storePaymentExpectedPrice;

  @ApiModelProperty(value = "MD추천 여부")
  private Boolean                              isRecommendedGoods;

  @ApiModelProperty(value = "신상품 여부")
  private Boolean                              isNewGoods;

  @ApiModelProperty(value = "베스트상품 여부")
  private Boolean                              isBestGoods;

  @ApiModelProperty(value = "쿠폰정보")
  private List<GoodsApplyCouponResponseDto> coupon;

  @ApiModelProperty(value = "인증정보")
  private List<GoodsCertificationListResultVo> certification;

  @ApiModelProperty(value = "배송정보")
  private List<ShippingDataResultDto>          delivery;

  @ApiModelProperty(value = "보관방법")
  private String                               storageMethodTypeName;

  @ApiModelProperty(value = "증정 행사 여부")
  private Boolean                              isGift;

  @ApiModelProperty(value = "증정 행사")
  private List<GiftListResponseDto>            gift;

  @ApiModelProperty(value = "긴급 공지")
  private List<GoodsNoticeResponseDto>          notice;

  @ApiModelProperty(value = "일반공지 배너1")
  private String                               noticeBelow1ImgUrl;

  @ApiModelProperty(value = "일반공지 배너2")
  private String                               noticeBelow2ImgUrl;

  @ApiModelProperty(value = "상품 상태")
  private String                               saleStatus;

  @ApiModelProperty(value = "상품 상세 기본정보")
  private String                               basicDescription;

  @ApiModelProperty(value = "상품 상세 주요정보")
  private String                               detailDescription;

  @ApiModelProperty(value = "추천상품 리스트")
  private List<GoodsSearchResultDto>           recommendedGoodsList;

  @ApiModelProperty(value = "동영상 자동재생 유무")
  private String                               videoAutoPlayYn;

  @ApiModelProperty(value = "동영상 URL")
  private String                               videoUrl;

  @ApiModelProperty(value = "최소 구매수량")
  private int                                  limitMinCnt;

  @ApiModelProperty(value = "최대 구매 수량 제한 여부")
  private String                               limitMaxCntYn;

  @ApiModelProperty(value = "최대 구매 수량 유형")
  private String                               limitMaxType;

  @ApiModelProperty(value = "최대 구매 수량 (주문수량 계산)")
  private int                                  limitMaxCnt;

  @ApiModelProperty(value = "최대 구매 수량 (설정값)")
  private int                                  systemLimitMaxCnt;

  @ApiModelProperty(value = "재고수량")
  private int                                  stockQty;

  @ApiModelProperty(value = "매장재고수량")
  private int                                  storeStockQty;

  @ApiModelProperty(value = "상품정보제공고시")
  private List<GoodsSpecListResultVo>          spec;

  @ApiModelProperty(value = "영양정보 표시여부")
  private String                               nutritionDispYn;

  @ApiModelProperty(value = "영양정보 리스트")
  private List<GoodsNutritionListResultVo>     nutrition;

  @ApiModelProperty(value = "영양분석단위(1회분량)")
  private String                               nutritionQtyPerOnce;

  @ApiModelProperty(value = "영양분석단위(총분량)")
  private String                               nutritionQtyTotal;

  @ApiModelProperty(value = "영양성분 기타")
  private String                               nutritionEtc;

  @ApiModelProperty(value = "영양정보 기본정보 ")
  private String                               nutritionDispDefault;

  @ApiModelProperty(value = "배송/환불 정보")
  private String                               claimDescription;

  @ApiModelProperty(value = "추가 상품 리스트")
  private List<AdditionalGoodsDto>             additionalGoodsList;

  @ApiModelProperty(value = "묶음상품 상품상세 기본정보 직접등록 여부")
  private String                               goodsPackageBasicDescriptionYn;

  @ApiModelProperty(value = "묶음상품 상품상세 기본정보")
  private String                               goodsPackageBasicDescription;

  @ApiModelProperty(value = "묶음 상품 리스트")
  private List<PackageGoodsListDto>            goodsPackageList;

  @ApiModelProperty(value = "예약판매 옵션 리스트")
  private List<GoodsReserveOptionListDto>      reserveOption;

  @ApiModelProperty(value = "정기배송 판매가")
  private int                                  regularShippingSalePrice;

  @ApiModelProperty(value = "정기배송정보.기본 할인정보")
  private int                                  regularShippingBasicDiscountRate;

  @ApiModelProperty(value = "정기배송정보.추가 할인 회차 정보")
  private int                                  regularShippingAdditionalDiscountApplicationTimes;

  @ApiModelProperty(value = "정기배송정보.추가 할인 정보")
  private int                                  regularShippingAdditionalDiscountRate;

  @ApiModelProperty(value = "일일상품 유형")
  private String                               goodsDailyType;

  @ApiModelProperty(value = "일일배송 주기 정보 리스트")
  private List<GoodsDailyCycleDto>             goodsDailyCycle;

  @ApiModelProperty(value = "알러지 식단 선택여부")
  private String                               goodsDailyAllergyYn;

  @ApiModelProperty(value = "일괄 배송 가능 여부")
  private String                               goodsDailyBulkYn;

  @ApiModelProperty(value = "일괄배송 배송 세트 정보 리스트")
  private List<HashMap<String, String>>        goodsDailyBulk;

  @ApiModelProperty(value = "주소기반으로 배송 가능 여부")
  private Boolean                              isShippingPossibility;

  @ApiModelProperty(value = "주소기반으로 매장배송 가능 여부")
  private Boolean                              isStoreShippingPossibility;

  @ApiModelProperty(value = "신규회원 특가 여부")
  private Boolean                              isNewBuyerSpecials;

  @ApiModelProperty(value = "신규회원 특가 판매가")
  private int                                  newBuyerSpecialSsalePrice;

  @ApiModelProperty(value = "쿠폰 사용 허용 여부")
  private String                               couponUseYn;

  @ApiModelProperty(value = "상품 후기.총 갯수")
  private int                                  feedbackTotalCount;

  @ApiModelProperty(value = "상품 후기.점수")
  private float                                satisfactionScore;

  @ApiModelProperty(value = "상품 후기. 글쓴 사람 총 수")
  private int                                  satisfactionCount;

  @ApiModelProperty(value = "상품문의. 총갯수")
  private int                                  qnaTotalCount;

  @ApiModelProperty(value = "상품구매 후기 별점 점수 목록")
  private List<FeedbackScorePercentDto>     feedbackScorePercentList;

  @ApiModelProperty(value = "딜상품 여부")
  private Boolean                              isDealGoods;

  @ApiModelProperty(value = "건강기능식품 여부")
  private String                              healthGoodsYn;

  @ApiModelProperty(value = "렌탈료/월")
  private Integer                              rentalFeePerMonth;

  @ApiModelProperty(value = "렌탈 의무사용기간(월)")
  private Integer                              rentalDueMonth;

  @ApiModelProperty(value = "렌탈 계약금")
  private Integer                              rentalDeposit;

  @ApiModelProperty(value = "렌탈 설치비")
  private Integer                              rentalInstallFee;

  @ApiModelProperty(value = "렌탈 등록비")
  private Integer                              rentalRegistFee;

  @ApiModelProperty(value = "STORE_DELIVERY_INTERVAL")
  private String                              storeDeliveryInterval;

  @ApiModelProperty(value = "녹즙 택배배송 여부")
  private Boolean                              isGreenjuiceParcel;

  @ApiModelProperty(value = "선물하기여부")
  private String                              presentYn;

  @ApiModelProperty(value = "식단")
  private GoodsPageInfoMealDto                              meal;

  @ApiModelProperty(value = "카테고리")
  private String                              goodsCtgrPro;
}
