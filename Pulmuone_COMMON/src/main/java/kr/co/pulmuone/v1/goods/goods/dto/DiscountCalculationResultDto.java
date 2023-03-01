package kr.co.pulmuone.v1.goods.goods.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "할인계산 결과 Dto")
public class DiscountCalculationResultDto
{
	@ApiModelProperty(value = "쿠폰 사용 가능여부")
	private boolean  isActive;

	@ApiModelProperty(value = "할인금액")
	private int discountPrice;

	@ApiModelProperty(value = "할인적용된 금액")
	private int discountAppliedPrice;

}
