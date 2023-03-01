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
public class GetMenuGroupListResultVo {

	@JsonProperty("MENU_GRP_ID")
	@ApiModelProperty(value = "")
	private String menuGroupId;

	@JsonProperty("MENU_IMG_ID")
	@ApiModelProperty(value = "")
	private String menuImageId;

	@JsonProperty("MENU_GRP_NAME")
	@ApiModelProperty(value = "")
	private String menuGroupName;

	@JsonProperty("SORT")
	@ApiModelProperty(value = "")
	private String sort;

	@JsonProperty("USE_YN")
	@ApiModelProperty(value = "")
	private String useYn;

	@JsonProperty("MENU_GRP_DEF_PG")
	@ApiModelProperty(value = "")
	private String menuGroupDefaultProgram;

	@JsonProperty("MENU_GRP_DEF_URL")
	@ApiModelProperty(value = "")
	private String menuGroupDefaultUrl;

}
