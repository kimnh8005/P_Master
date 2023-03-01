package kr.co.pulmuone.v1.shopping.cart.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "장바구니 일괄추가 요청 DTO - 상품 DTO")
public class AddCartListGoodsRequestDto {

	@ApiModelProperty(value = "상품 PK")
	private Long ilGoodsId;

	@ApiModelProperty(value = "상품 구매 수량")
	private int qty;
}
