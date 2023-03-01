package kr.co.pulmuone.v1.shopping.cart.dto.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class CartDeliveryTypeGroupByVo {

	/**
	 * 배송 타입
	 */
	private String deliveryType;

	/**
	 * 배송 타입명
	 */
	private String deliveryTypeName;

	/**
	 * 배송 타입 갯수
	 */
	private String deliveryTypeTotal;
}
