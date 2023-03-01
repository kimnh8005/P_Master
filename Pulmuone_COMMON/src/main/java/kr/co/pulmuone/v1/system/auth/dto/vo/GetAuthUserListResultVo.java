package kr.co.pulmuone.v1.system.auth.dto.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "GetAuthUserListResultVo")
public class GetAuthUserListResultVo {

	@ApiModelProperty(value = "")
	private String userName;

	@ApiModelProperty(value = "")
	private String urUserId;

	@ApiModelProperty(value = "")
	private String loginId;

	@ApiModelProperty(value = "")
	private String userType;

	@ApiModelProperty(value = "")
	private String chk;

	@ApiModelProperty(value = "")
	private String stRoleTypeId;

	@ApiModelProperty(value = "")
	private String companyName;

	@ApiModelProperty(value = "")
	private String email;

}
