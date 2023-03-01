package kr.co.pulmuone.v1.policy.shiparea.dto.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "GetBackCountryResultVo")
public class GetBackCountryResultVo {

	@ApiModelProperty(value = "")
	private String zipCode;

	@ApiModelProperty(value = "")
	private String jejuYn;

	@ApiModelProperty(value = "")
	private String islandYn;

	@ApiModelProperty(value = "")
	private String createDate;
}
