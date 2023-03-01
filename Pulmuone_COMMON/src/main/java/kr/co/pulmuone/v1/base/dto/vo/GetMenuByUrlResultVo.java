package kr.co.pulmuone.v1.base.dto.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "GetMenuByUrlResultVo")
public class GetMenuByUrlResultVo {

	@ApiModelProperty(value = "")
	private String menuName;

	@ApiModelProperty(value = "")
	private String stMenuId;

	@ApiModelProperty(value = "")
	private String url;

}
