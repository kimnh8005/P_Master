package kr.co.pulmuone.v1.policy.shiparea.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseRequestPageDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "PolicyShipareaDto")
public class PolicyShipareaDto extends BaseRequestPageDto{

	@ApiModelProperty(value = "")
	private String searchAreaType;

	@ApiModelProperty(value = "")
	private String searchZipCode;

	@ApiModelProperty(value = "")
	private String zipCode;

	@ApiModelProperty(value = "")
	private String areaType;

	@ApiModelProperty(value = "")
	private String zipCodeCsv;

	@ApiModelProperty(value = "")
	private String jejuYn = "N";

	@ApiModelProperty(value = "")
	private String islandYn = "N";

	@ApiModelProperty(value = "")
	private String zipCodes[];
}
