package kr.co.pulmuone.v1.shopping.cart.dto;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "장바구니 담기 응답 DTO")
public class AddCartInfoResponseDto {

	/**
	 * 장바구니 PK
	 */
	private Long spCartId;

	/**
	 * 장바구니 타입
	 */
	private String cartType;

	/**
	 * 최초 장바구니 담기 여부
	 */
	private boolean isFirstAdd;

	/**
	 * 장바구니 담은 수량
	 */
	private int cartCount;
}
