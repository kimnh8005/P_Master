package kr.co.pulmuone.v1.shopping.cart.dto;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class GoodsCouponDto {

	/**
	 * 장바구니 PK
	 */
	private Long spCartId;

	/**
	 * 상품명
	 */
	private String goodsName;

	/**
	 * 상품 PK
	 */
	private Long ilGoodsId;

	/**
	 * 신규회원 특가 여부
	 */
	private boolean isNewBuyerSpecials;

	/**
	 * 신규회원 특가 판매가
	 */
	private int newBuyerSpecialsSalePrice;

	/**
	 * 상품 쿠폰 리스트
	 */
	private List<CouponDto> couponList;

}
