package kr.co.pulmuone.v1.shopping.cart.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "장바구니 담기 요청 DTO - 추가 구성 상품 DTO")
public class AddCartInfoAdditionalGoodsRequestDto {

	@ApiModelProperty(value = "추가 구성 상품 PK")
	private Long ilGoodsId;

	@ApiModelProperty(value = "추가 구성 상품 구매 수량")
	private int qty;
}
