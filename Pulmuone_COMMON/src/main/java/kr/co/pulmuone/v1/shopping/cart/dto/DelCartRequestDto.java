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
@ApiModel(description = "장바구니 삭제 요청 DTO")
public class DelCartRequestDto
{
	@ApiModelProperty(value = "장바구니 PK")
	private List<Long> spCartId;

	@ApiModelProperty(value = "회원 PK")
	private Long urUserId;

	@ApiModelProperty(value = "장바구니 추가 구성상품 PK")
	private List<Long> spCartAddGoodsId;
}
