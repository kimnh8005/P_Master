package kr.co.pulmuone.v1.shopping.cart.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "장바구니 담기 요청 DTO")
public class AddSimpleCartRequestDto {

	@ApiModelProperty(value = "상품 PK", required = true)
	private Long ilGoodsId;

	@ApiModelProperty(value = "배송 타입")
	private String deliveryType;

	@ApiModelProperty(value = "내부광고코드-페이지코드")
	private String pmAdInternalPageCd;

	@ApiModelProperty(value = "내부광고코드-영역코드")
	private String pmAdInternalContentCd;
}
