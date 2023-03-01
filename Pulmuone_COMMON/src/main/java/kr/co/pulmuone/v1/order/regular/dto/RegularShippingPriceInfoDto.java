package kr.co.pulmuone.v1.order.regular.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "정기배송 배송비정보 DTO")
public class RegularShippingPriceInfoDto {

	@ApiModelProperty(value = "배송정책PK")
	private long ilShippingTmplId;

	@ApiModelProperty(value = "배송비")
	private int shippingPrice;
}
