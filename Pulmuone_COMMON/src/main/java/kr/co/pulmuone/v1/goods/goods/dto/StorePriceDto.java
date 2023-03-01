package kr.co.pulmuone.v1.goods.goods.dto;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "추가구성상품 Dto")
public class StorePriceDto
{
	/**
	 * 상품 판매가
	 */
	private int salePrice;

	/**
	 * 상품 적용된 할인유형
	 */
	private String discountType;

	/**
	 * 상품 적용된 할인유형명
	 */
	private String discountTypeName;
}
