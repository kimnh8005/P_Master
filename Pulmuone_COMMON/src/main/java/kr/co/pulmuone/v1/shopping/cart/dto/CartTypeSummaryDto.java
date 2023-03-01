package kr.co.pulmuone.v1.shopping.cart.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "장바구니 타입 DTO")
public class CartTypeSummaryDto {

	@ApiModelProperty(value = "장바구니 타입")
	private String cartType;

	@ApiModelProperty(value = "장바구니 타입명")
	private String cartTypeName;

	@ApiModelProperty(value = "장바구니 담긴 총 상품건수")
	private int total;
}
