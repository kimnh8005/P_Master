package kr.co.pulmuone.v1.system.menu.dto.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "GetMenuListResultVo")
public class GetMenuPopupListResultVo {

	@ApiModelProperty(value = "")
	private String menuGroupName;

	@ApiModelProperty(value = "")
	private String menuName;

	@ApiModelProperty(value = "")
	private String menuType;

	@ApiModelProperty(value = "")
	private String stMenuId;
}
