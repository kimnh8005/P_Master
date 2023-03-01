package kr.co.pulmuone.v1.system.menu.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseRequestDto;
import lombok.*;


@Getter
@Setter
@ToString
@ApiModel(description = "PutMenuRequestDto")
public class MenuRequestDto extends BaseRequestDto{

	@ApiModelProperty(value = "")
	private String id;

	@ApiModelProperty(value = "")
	private String stMenuGroupId;

	@ApiModelProperty(value = "")
	private String menuName;

	@ApiModelProperty(value = "")
	private String menuType;

	@ApiModelProperty(value = "")
	private String url;

	@ApiModelProperty(value = "")
	private String parentMenuId;

	@ApiModelProperty(value = "")
	private String businessType;

	@ApiModelProperty(value = "")
	private String stProgramId;

	@ApiModelProperty(value = "")
	private String gbDictionaryMasterId;

	@ApiModelProperty(value = "")
	private String sort;

	@ApiModelProperty(value = "")
	private String popYn;

	@ApiModelProperty(value = "")
	private String useYn;

	@ApiModelProperty(value = "")
	private String comment;

	@ApiModelProperty(value = "")
	private String addAuthorizationCheckData;

	@ApiModelProperty(value = "")
	private String stMenuId;

}
