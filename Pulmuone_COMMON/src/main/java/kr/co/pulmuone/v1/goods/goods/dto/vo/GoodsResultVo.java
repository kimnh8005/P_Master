package kr.co.pulmuone.v1.goods.goods.dto.vo;

import java.util.List;

import kr.co.pulmuone.v1.comm.enums.GoodsEnums;
import kr.co.pulmuone.v1.goods.goods.dto.ArrivalScheduledDateDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class GoodsResultVo {

	/**
	 * 상품PK
	 */
	private Long ilGoodsId;

	/**
	 * 품목코드
	 */
	private String ilItemCode;

	/**
	 * 상품유형 공통코드
	 */
	private String goodsType;

	/**
	 * 판매유형 공통코드
	 */
	private String saleType;

	/**
	 * 상품 정상가
	 */
	private int recommendedPrice;

	/**
	 * 상품 판매가
	 */
	private int salePrice;

	/**
	 * 상품 적용된 할인유형
	 */
	private String discountType;

	/**
	 * 상품 적용된 할인유형명
	 */
	private String discountTypeName;

	/**
	 * 상품 원가
	 */
	private int standardPrice;

	/**
	 * 상품명
	 */
	private String goodsName;

	/**
	 * 상품 상태
	 */
	private String saleStatus;

	/**
	 * 최소 구매수량
	 */
	private int limitMinimumCount;

	/**
	 * 최대 구매수량
	 */
	private int limitMaximumCount;

	/**
	 * 최대 구매수량유형 공통코드
	 */
	private String limitMaximumType;

	/**
	 * 최대 구매수량 제한 여부
	 */
	private String limitMaximumCountYn;

	/**
	 * 브랜드PK
	 */
	private Long urBrandId;

	/**
	 * 브랜드 명
	 */
	private String brandName;

	/**
	 * 상품 간략 설명
	 */
	private String goodsDescription;

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
	 * 동영상 URL
	 */
	private String videoUrl;

	/**
	 * 출고처 PK
	 */
	private Long urWareHouseId;

	/**
	 * 새벽배송가능여부
	 */
	private String dawnDeliveryYn;

	/**
	 * 동영상 자동재생 유무
	 */
	private String videoAutoplayYn;

	/**
	 * 묶음상품 상품상세 기본정보 직접등록 여부
	 */
	private String goodsPackageBasicDescYn;

	/**
	 * 묶음상품 상품상세 기본정보
	 */
	private String goodsPackageBasicDesc;

	/**
	 * 묶음상품 이미지 타입
	 */
	private String goodsPackageImageType;

	/**
	 * 일일상품 유형
	 */
	private String goodsDailyType;

	/**
	 * 알러지 식단 선택여부
	 */
	private String goodsDailyAllergyYn;

	/**
	 * 일괄 배송 가능 여부
	 */
	private String goodsDailyBulkYn;

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
	 * 배송불가지역 유형
	 */
	private String undeliverableAreaType;

	/**
	 * 공급처 PK
	 */
	private Long urSupplierId;

	/**
	 * 새벽배송 가능 여부
	 */
	public boolean canDawnDelivery() {
		boolean canDawnDelivery = false;

		// 판매유형이 일반,정기,예약일 경우 새벽배송 가능
		if (this.dawnDeliveryYn.equals("Y") && (this.saleType.equals(GoodsEnums.SaleType.NORMAL.getCode())
				|| this.saleType.equals(GoodsEnums.SaleType.REGULAR.getCode())
				|| this.saleType.equals(GoodsEnums.SaleType.RESERVATION.getCode()))) {
			canDawnDelivery = true;
		}
		return canDawnDelivery;
	}

	/**
	 * 재고
	 */
	private int stockQty;

	/**
	 * 배송 가능 도착 예정일 DTO 리스트
	 */
	private List<ArrivalScheduledDateDto> arrivalScheduledDateDtoList;

	/**
	 * 상품 배송 도착 예정일 DTO
	 */
	private ArrivalScheduledDateDto arrivalScheduledDateDto;

	/**
	 * 쿠폰 사용 허용 여부
	 */
	private String couponUseYn;

	/**
	 * 과세여부
	 */
	private String taxYn;

	/**
	 * 카테고리 PK
	 */
	private Long ilCtgryId;
}
