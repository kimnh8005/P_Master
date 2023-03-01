package kr.co.pulmuone.v1.base.dto.vo;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "GetCodeListResultVo")
public class GetMenuListResultVo {

	@JsonProperty("TREE_MENU_NAME")
	@ApiModelProperty(value = "")
	private String treeMenuName;

	@JsonProperty("MENU_ID")
	@ApiModelProperty(value = "")
	private String menuId;

	@JsonProperty("MENU_NAME")
	@ApiModelProperty(value = "")
	private String menuName;

	@JsonProperty("P_MENU_ID")
	@ApiModelProperty(value = "")
	private String parentsMenuId;

	@JsonProperty("ST_PROGRAM_ID")
	@ApiModelProperty(value = "")
	private String stProgramId;

	@JsonProperty("MENU_TYPE")
	@ApiModelProperty(value = "")
	private String menuType;

	@JsonProperty("LV")
	@ApiModelProperty(value = "")
	private String level;

	@JsonProperty("URL")
	@ApiModelProperty(value = "")
	private String url;

	@JsonProperty("POP_YN")
	@ApiModelProperty(value = "")
	private String popYn;

	@JsonProperty("ST_MENU_GRP_ID")
	@ApiModelProperty(value = "")
	private String stMenuGroupId;

	@JsonProperty("MENU_GRP_NAME")
	@ApiModelProperty(value = "")
	private String menuGroupName;

}
