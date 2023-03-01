package kr.co.pulmuone.v1.shopping.cart.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "품절/판매중지 삭제 요청 DTO")
public class DelCartSoldOutGoodsRequestDto
{
	@ApiModelProperty(value = "장바구니 추가 구성상품 PK", required = true)
	private int[] spCartAddGoodsId;
}
