package kr.co.pulmuone.v1.shopping.cart.dto;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "간편 장바구니 추가 응답 DTO")
public class AddSimpleCartResponseDto {

	/**
	 * 장바구니 담은 수량
	 */
	private int cartCount;

	/**
	 * 최초 장바구니 담기 여부
	 */
	private boolean isFirstAdd;

	/**
	 * 배송타입
	 */
	private String deliveryType;
}
