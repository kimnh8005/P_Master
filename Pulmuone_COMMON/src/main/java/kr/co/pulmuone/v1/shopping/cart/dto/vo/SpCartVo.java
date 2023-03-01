package kr.co.pulmuone.v1.shopping.cart.dto.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

@Getter
@Setter
@ToString
public class SpCartVo {

	/**
	 * 정기배송 기존 상품 여부 (Y: 기존, N: 추가)
	 */
	private String ingRegularGoodsYn;

	/**
	 * 장바구니 PK
	 */
	private Long spCartId;

	/**
	 * 사용자 환경 정보 PK
	 */
	private String urPcidCd;

	/**
	 * 회원 PK
	 */
	private Long urUserId;

	/**
	 * 배송타입 - 공통코드 DELIVERY_TYPE
	 */
	private String deliveryType;

	/**
	 * 배송타입명 - 공통코드 DELIVERY_TYPE
	 */
	private String deliveryTypeName;

	/**
	 * 상품 PK
	 */
	private Long ilGoodsId;

	/**
	 * 구매 수량
	 */
	private int qty;

	/**
	 * 예약 정보 PK
	 */
	private Long ilGoodsReserveOptionId;

	/**
	 * 출고 예정일
	 */
	private LocalDate releaseDate;

	/**
	 * 도착 예정일
	 */
	private LocalDate arriveDate;

	/**
	 * 바로구매 여부 (Y: 바로구매)
	 */
	private String buyNowYn;

	/**
	 * 일일 배송주기코드
	 */
	private String goodsDailyCycleType;

	/**
	 * 일일 배송주기명
	 */
	private String goodsDailyCycleTypeName;

	/**
	 * 일일 배송기간코드
	 */
	private String goodsDailyCycleTermType;

	/**
	 * 일일 배송기간명
	 */
	private String goodsDailyCycleTermTypeName;

	/**
	 * 일일 배송 녹즙 요일 코드 리스트
	 */
	private String goodsDailyCycleGreenJuiceWeekType;

	/**
	 * 알러지 식단 여부
	 */
	private String goodsDailyAllergyYn;

	/**
	 * 일괄 배송 여부
	 */
	private String goodsDailyBulkYn;

	/**
	 * 일괄배송 배송 세트 코드
	 */
	private String goodsBulkType;

	/**
	 * 일괄배송 배송 세트 명
	 */
	private String goodsBulkTypeName;

	/**
	 * 상품 배송 탬플릿
	 */
	private Long ilShippingTmplId;

	/**
	 * 기획전 유형 골라담기 타입
	 */
	private String cartPromotionType;

	/**
	 * 골라담기 기획전 PK
	 */
	private Long evExhibitId;

	/**
	 * 외부광고코드PK
	 */
	private String pmAdExternalCd;

	/**
	 * 내부광고코드-페이지코드
	 */
	private String pmAdInternalPageCd;

	/**
	 * 내부광고코드-영역코드
	 */
	private String pmAdInternalContentCd;

	/**
	 * 재입고 신청여부
	 */
	private boolean isApplyRestockNotice;
}
