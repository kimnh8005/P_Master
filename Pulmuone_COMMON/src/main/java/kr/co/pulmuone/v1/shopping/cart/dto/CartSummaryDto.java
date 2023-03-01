package kr.co.pulmuone.v1.shopping.cart.dto;

import java.util.List;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "장바구니 집계 DTO")
public class CartSummaryDto {

	/**
	 * 요약 상품명
	 */
	private String goodsSummaryName;


	/**
	 * 요약 상품 코드
	 */
	private long summaryGoodsId;

	/**
	 * 총 상품정가금액
	 */
	private int goodsRecommendedPrice;

	/**
	 * 총 상품 판매가
	 */
	private int goodsSalePrice;

	/**
	 * 총 상품 할인금액
	 */
	private int goodsDiscountPrice;

	/**
	 * 상품 결제 금액
	 */
	private int goodsPaymentPrice;

	/**
	 * 상품 결제 과세 금액
	 */
	private int goodsTaxPaymentPrice;

	/**
	 * 상품 결제 비과세 금액
	 */
	private int goodsTaxFreePaymentPrice;

	/**
	 * 총 배송비
	 */
	private int shippingRecommendedPrice;

	/**
	 * 총 배송비 할인금액
	 */
	private int shippingDiscountPrice;

	/**
	 * 총 배송비 결제금액
	 */
	private int shippingPaymentPrice;

	/**
	 * 사용 적립금
	 */
	private int usePoint;

	/**
	 * 사용 적립금 과세
	 */
	private int useTaxPoint;

	/**
	 * 사용 적립금 비과세
	 */
	private int useTaxFreePoint;

	/**
	 * 총 주문금액 (결제 예정금액)
	 */
	private int paymentPrice;

	/**
	 * 총 주문 과세 금액
	 */
	private int taxPaymentPrice;

	/**
	 * 총 주문 비과세 금액
	 */
	private int taxFreePaymentPrice;

	/**
	 * 할인 정보
	 */
	private List<CartGoodsDiscountDto> discount;
}
