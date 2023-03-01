package kr.co.pulmuone.v1.shopping.cart.dto;


import java.util.List;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "장바구니 쿠폰 적용 상품 DTO")
public class CartCouponApplicationGoodsDto {

	/**
	 * 장바구니 PK
	 */
	private Long spCartId;

	/**
	 * 장바구니 쿠폰 할인 금액
	 */

	private int discountPrice;

	/**
	 * 장바구니 쿠폰 할인 적용된 금액
	 */
	private int paymentPrice;

	/**
	 * 묶음상품
	 */
	private List<CartGoodsPackageDto> goodsPackage;

}
