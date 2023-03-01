package kr.co.pulmuone.v1.shopping.cart.dto.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class SpCartAddGoodsVo {

	/**
	 * 장바구니 추가 구성 상품 PK
	 */
	private Long spCartAddGoodsId;

	/**
	 * 장바구니 PK
	 */
	private Long spCartId;

	/**
	 * 상품 PK
	 */
	private Long ilGoodsId;

	/**
	 * 구매 수량
	 */
	private int qty;

	/**
	 * 추가 구성상품 판매가
	 */
	private int salePrice;
}
