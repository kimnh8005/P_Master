package kr.co.pulmuone.v1.system.menu.dto.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "GetSystemUrlResultVo")
public class GetSystemUrlResultVo {

	@ApiModelProperty(value = "")
	String id;

	@ApiModelProperty(value = "")
	String inputUrl;

	@ApiModelProperty(value = "")
	String inputUrlName;

	@ApiModelProperty(value = "")
	String inputUrlUsageYn;

	@ApiModelProperty(value = "")
	String inputAuthority;

	@ApiModelProperty(value = "")
	String inputContent;
}
