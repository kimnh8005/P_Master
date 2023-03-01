package kr.co.pulmuone.v1.system.menu.dto.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "GetSystemUrlListResultVo")
public class GetSystemUrlListResultVo {

	@ApiModelProperty(value = "")
	String id;

	@ApiModelProperty(value = "")
	String url;

	@ApiModelProperty(value = "")
	String urlName;

	@ApiModelProperty(value = "")
	String content;

	@ApiModelProperty(value = "")
	String createDate;

	@ApiModelProperty(value = "")
	String modifyDate;
}
