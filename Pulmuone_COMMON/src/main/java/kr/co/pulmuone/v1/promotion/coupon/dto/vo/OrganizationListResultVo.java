package kr.co.pulmuone.v1.promotion.coupon.dto.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "GetCouponDetailVo")
public class OrganizationListResultVo {

	@ApiModelProperty(value = "조직코드")
	private String urErpOrganizationCode;

	@ApiModelProperty(value = "조직명")
	private String urErpOrganizationName;

	@ApiModelProperty(value = "분담율")
	private String percentage;


}
