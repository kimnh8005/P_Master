package kr.co.pulmuone.v1.shopping.cart.dto.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class SpCartPickGoodsVo {

	/**
	 * 장바구니 기획전 유형 골라담기 상품 PK
	 */
	private Long spCartPickGoodsId;

	/**
	 * 장바구니 PK
	 */
	private Long spCartId;

	/**
	 * 장바구니 일일 배송 요일 코드(녹즙 내맘대로 일때)
	 */
	private String goodsDailyCycleWeekType;

	/**
	 * 상품 PK
	 */
	private Long ilGoodsId;

	/**
	 * 구매 수량
	 */
	private int qty;

	/**
	 * 배송 탬플릿 PK
	 */
	private Long ilShippingTmplId;

	/**
	 * 녹즙 골라담기 허용여부(Y:허용)
	 */
	private String pickableYn;
}
