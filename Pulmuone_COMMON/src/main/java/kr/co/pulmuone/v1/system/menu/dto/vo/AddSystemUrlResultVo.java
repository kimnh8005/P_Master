package kr.co.pulmuone.v1.system.menu.dto.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "AddSystemUrlResultVo")
public class AddSystemUrlResultVo {

	@ApiModelProperty(value = "")
	String id;

	@ApiModelProperty(value = "")
	String url;

	@ApiModelProperty(value = "")
	String urlName;

	@ApiModelProperty(value = "")
	String content;
}
