package kr.co.pulmuone.v1.shopping.favorites.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ShoppingFavoritesParamDto
{

	/**
	 * 상품 PK
	 */
	private Long ilGoodsId;

	/**
	 * 회원 PK
	 */
	private String urUserId;

	/**
	 * 찜상품 PK
	 */
	private Long spFavoritesGoodsId;

}
