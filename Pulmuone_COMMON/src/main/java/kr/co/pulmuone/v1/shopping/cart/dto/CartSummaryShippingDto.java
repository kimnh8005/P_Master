package kr.co.pulmuone.v1.shopping.cart.dto;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "장바구니 집계 배송정보  DTO")
public class CartSummaryShippingDto {

	/**
	 * 배송타입
	 */
	private String deliveryType;

	/**
	 * 출고처 PK
	 */
	private Long urWarehouseId;

	/**
	 * 장바구니 배송비 탬플릿 PK
	 */
	private Long ilShippingTmplId;

	/**
	 * 상품 정가
	 */
	private int goodsRecommendedPrice;

	/**
	 * 상품 할인금액
	 */
	private int goodsDiscountPrice;

	/**
	 * 배송비
	 */
	private int shippingRecommendedPrice;

	/**
	 * 주문금액(결제금액)
	 */
	private int paymentPrice;

	/**
	 * 배송비 무료배송 조건을 위한 추가 결제금액 (0보다 클때 노출)
	 */
	private int freeShippingForNeedGoodsPrice;


}
