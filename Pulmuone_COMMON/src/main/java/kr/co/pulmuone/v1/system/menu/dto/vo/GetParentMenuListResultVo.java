package kr.co.pulmuone.v1.system.menu.dto.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@Getter
@Setter
@ToString
@ApiModel(description = "GetParentMenuListResultVo")
public class GetParentMenuListResultVo {

	@ApiModelProperty(value = "")
	private String stMenuId;

	@ApiModelProperty(value = "")
	private String menuName;

	@ApiModelProperty(value = "")
	private String stMenuGroupId;

	@ApiModelProperty(value = "")
	private String menuGroupName;

	@ApiModelProperty(value = "")
	private String parentMenuId;

	@ApiModelProperty(value = "")
	private String parentMenuName;

	@ApiModelProperty(value = "")
	private String treeMenuName;

	@ApiModelProperty(value = "")
	private String level;
}
