package kr.co.pulmuone.v1.promotion.point.dto.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@Getter
@Setter
@ToString
@ApiModel(description = "PmOrganizationVo")
public class PmOrganizationVo {

	@ApiModelProperty(value = "조직코드")
	private String urErpOrganizationCode;

	@ApiModelProperty(value = "법인코드")
	private String erpRegalCd;

	@ApiModelProperty(value = "분담율")
	private int percentage;

	@ApiModelProperty(value = "쿠폰 고유값")
	private Long pmCouponId;

	@ApiModelProperty(value = "적립금 고유값")
	private Long pmPointId;
}
