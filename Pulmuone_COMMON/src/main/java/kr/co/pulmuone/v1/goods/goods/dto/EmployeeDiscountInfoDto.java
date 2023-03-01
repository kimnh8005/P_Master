package kr.co.pulmuone.v1.goods.goods.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description ="임직원 할인 정보 Dto")
public class EmployeeDiscountInfoDto
{

	@ApiModelProperty(value = "초과금액")
	private int excessPrice;

	@ApiModelProperty(value = "할인금액")
	private int discountPrice;

	@ApiModelProperty(value = "임직원 상품 할인된 금액")
	private int discountAppliedPrice;

	@ApiModelProperty(value = "입직원 상품 할인된 단가")
	private int salePrice;

	public void setNoDiscountPrice(int discountAppliedPrice, int salePrice) {
		this.discountAppliedPrice = discountAppliedPrice;
		this.salePrice = salePrice;
	}
}
