package kr.co.pulmuone.v1.goods.goods.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "정기배송정보 Dto")
public class RegularShippingConfigDto
{
	@ApiModelProperty(value = "정기배송정보.기본 할인정보")
	private int regularShippingBasicDiscountRate;

	@ApiModelProperty(value = "정기배송정보.추가 할인 회차 정보")
	private int regularShippingAdditionalDiscountApplicationTimes;

	@ApiModelProperty(value = "정기배송정보.추가 할인 정보")
	private int regularShippingAdditionalDiscountRate;
}
