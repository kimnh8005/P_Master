package kr.co.pulmuone.v1.shopping.cart.dto;

import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "장바구니 추가 구성상품 삭제 요청 DTO")
public class DelCartAddGoodsRequestDto
{
	@ApiModelProperty(value = "장바구니 추가 구성상품 PK", required = true)
	private List<Long> spCartAddGoodsId;
}
