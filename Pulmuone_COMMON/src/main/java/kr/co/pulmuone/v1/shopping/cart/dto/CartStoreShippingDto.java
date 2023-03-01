package kr.co.pulmuone.v1.shopping.cart.dto;

import java.util.List;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "장바구니 매장 배송 유형 DTO")
public class CartStoreShippingDto {

	/**
	 * 매장배송 배송유형(매장배송, 매장픽업)
	 */
	private String storeDeliveryType;

	/**
	 * 매장배송 배송유형명
	 */
	private String storeDeliveryTypeName;

	/**
	 * 매장 배송 가능 일자
	 */
	private List<CartStoreShippingDateDto> date;
}
