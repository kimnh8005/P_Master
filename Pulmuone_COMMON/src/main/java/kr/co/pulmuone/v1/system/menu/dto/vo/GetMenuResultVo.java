package kr.co.pulmuone.v1.system.menu.dto.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "GetMenuGroupResultVo")
public class GetMenuResultVo {

	@ApiModelProperty(value = "")
	private String id;

	@ApiModelProperty(value = "")
	private String stMenuId;

	@ApiModelProperty(value = "")
	private String stProgramId;

	@ApiModelProperty(value = "")
	private String menuName;

	@ApiModelProperty(value = "")
	private String menuType;

	@ApiModelProperty(value = "")
	private String url;

	@ApiModelProperty(value = "")
	private String parentMenuId;

	@ApiModelProperty(value = "")
	private String parentMenuName;

	@ApiModelProperty(value = "")
	private String sort;

	@ApiModelProperty(value = "")
	private String popYn;

	@ApiModelProperty(value = "")
	private String useYn;

	@ApiModelProperty(value = "")
	private String addAuthorizationCheckData;

	@ApiModelProperty(value = "")
	private String gbDictionaryMasterId;

	@ApiModelProperty(value = "")
	private String comment;

	@ApiModelProperty(value = "")
	private String businessType;

	@ApiModelProperty(value = "")
	private String stMenuGroupId;

	@ApiModelProperty(value = "")
	private String programName;



}
