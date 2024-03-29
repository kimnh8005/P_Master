package kr.co.pulmuone.v1.system.menu.dto.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "GetMenuUrlListResultVo")
public class GetMenuUrlListResultVo {

	@ApiModelProperty(value = "")
	private Long stMenuUrlId;

	@ApiModelProperty(value = "")
	private String url;

	@ApiModelProperty(value = "")
	private String urlName;
}
