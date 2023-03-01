package kr.co.pulmuone.v1.shopping.cart.dto;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ShippingCouponDto {

	/**
	 * 배송정책 index
	 */
	private int shippingIndex;

	/**
	 * 구성명
	 */
	private String compositionGoodsName;

	/**
	 * 배송비
	 */
	private int shippingRecommendedPrice;

	/**
	 * 배송비 쿠폰 리스트
	 */
	private List<CouponDto> couponList;
}
