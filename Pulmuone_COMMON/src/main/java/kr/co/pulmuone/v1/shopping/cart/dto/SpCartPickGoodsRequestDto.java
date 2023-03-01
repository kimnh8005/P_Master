package kr.co.pulmuone.v1.shopping.cart.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "장바구니 기획전 유형 상품 요청 DTO")
public class SpCartPickGoodsRequestDto {

	@ApiModelProperty(value = "선택 상품 PK")
	private Long ilGoodsId;

	@ApiModelProperty(value = "선택 상품 수량")
	private int qty;

	@ApiModelProperty(value = "선택 요일 코드")
	private String goodsDailyCycleWeekType;

}
