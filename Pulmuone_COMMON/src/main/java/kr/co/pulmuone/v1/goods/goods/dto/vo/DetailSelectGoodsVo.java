package kr.co.pulmuone.v1.goods.goods.dto.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class DetailSelectGoodsVo {

	/**
	 * 상품PK
	 */
	private Long ilGoodsId;

	/**
	 * 품목코드
	 */
	private String ilItemCode;

	/**
	 * 상품명
	 */
	private String goodsName;

	/**
	 * 전시 브랜드 명
	 */
	private String brandName;

	/**
	 * 표준 브랜드 명
	 */
	private String urBrandName;

	/**
	 * 상품 간략 설명
	 */
	private String goodsDescription;

	/**
	 * 상품 검색키워드
	 */
	private String goodsSearchKeyword;

	/**
	 * 보관방법
	 */
	private String storageMethodTypeName;

	/**
	 * 상품 상세 기본정보
	 */
	private String basicDescription;

	/**
	 * 상품 상세 주요정보
	 */
	private String detailDescription;

	/**
	 * 배송/환불 정보
	 */
	private String claimDescription;

	/**
	 * 상품 중카테고리 PK
	 */
	private String categoryIdDepth2;

	/**
	 * 영양정보 표시여부
	 */
	private String nutritionDisplayYn;

	/**
	 * 영양분석단위(1회분량)
	 */
	private String nutritionQtyPerOnce;

	/**
	 * 영양분석단위(총분량)
	 */
	private String nutritionQtyTotal;

	/**
	 * 영양성분 기타
	 */
	private String nutritionEtc;

	/**
	 * 영양정보 기본정보
	 */
	private String nutritionDisplayDefault;

	/**
	 * 일반공지 배너1
	 */
	private String noticeBelow1ImageUrl;

	/**
	 * 일반공지 배너2
	 */
	private String noticeBelow2ImageUrl;

	/**
	 * 동영상 자동재생 유무
	 */
	private String videoAutoplayYn;

	/**
	 * 동영상 URL
	 */
	private String videoUrl;

	/**
	 * 묶음상품 이미지 타입
	 */
	private String goodsPackageImageType;

	/**
	 * 묶음상품 상품상세 기본정보 직접등록 여부
	 */
	private String goodsPackageBasicDescYn;

	/**
	 * 묶음상품 상품상세 기본정보
	 */
	private String goodsPackageBasicDesc;

	/**
	 * MD추천 여부
	 */
	private Boolean isRecommendedGoods;

	/**
	 * 신상품 여부
	 */
	private Boolean isNewGoods;

	/**
	 * 베스트상품 여부
	 */
	private Boolean isBestGoods;

	/**
	 * 상품유형
	 */
	private String goodsTp;

	/**
	 * 판매유형
	 */
	private String saleType;

	/**
	 * 공급처 PK
	 */
	private Long urSupplierId;

	/**
	 * 렌탈료/월
	 */
	private Integer rentalFeePerMonth;

	/**
	 * 렌탈 의무사용기간(월)
	 */
	private Integer rentalDueMonth;

	/**
	 * 렌탈 계약금
	 */
	private Integer rentalDeposit;

	/**
	 * 렌탈 설치비
	 */
	private Integer rentalInstallFee;

	/**
	 * 렌탈 등록비
	 */
	private Integer rentalRegistFee;

	/**
	 * 정상가
	 */
	private int recommendedPrice;

	/**
	 * 원가
	 */
	private int standardPrice;

	/**
	 * 상품 이미지 경로
	 */
	private String goodsImage;

}
