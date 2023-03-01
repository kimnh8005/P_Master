package kr.co.pulmuone.v1.system.auth.dto.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "GetAuthMenuListResultVo")
public class GetAuthMenuListResultVo {

	@ApiModelProperty(value = "")
	private String menuName;

	@ApiModelProperty(value = "")
	private String stRoleTypeId;

	@ApiModelProperty(value = "")
	private String level;

	@ApiModelProperty(value = "")
	private String popYn;

	@ApiModelProperty(value = "")
	private String menuType;

	@ApiModelProperty(value = "")
	private String menuTypeName;

	@ApiModelProperty(value = "")
	private String stMenuId;

	@ApiModelProperty(value = "")
	private String treeMenuName;

	@ApiModelProperty(value = "할당된 권한 갯수")
	private String programAuthCount;
}
