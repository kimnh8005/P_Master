package kr.co.pulmuone.v1.system.auth.dto.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "GetRoleListResultVo")
public class GetRoleListResultVo {

	@ApiModelProperty(value = "역할 그룹 PK")
	private String stRoleTypeId;

	@ApiModelProperty(value = "역할 명")
	private String roleName;

	@ApiModelProperty(value = "사용여부")
	private String useYn;

	@ApiModelProperty(value = "법인")
	private String erpRegalName;

	@ApiModelProperty(value = "부서")
	private String erpOrganizationName;

}
