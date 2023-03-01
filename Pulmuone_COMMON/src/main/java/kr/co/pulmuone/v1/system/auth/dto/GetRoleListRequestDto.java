package kr.co.pulmuone.v1.system.auth.dto;

import kr.co.pulmuone.v1.comm.base.dto.BaseRequestPageDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "GetRoleListRequestDto")
public class GetRoleListRequestDto extends BaseRequestPageDto {

	@ApiModelProperty(value = "역할그룹 PK")
	private String stRoleTypeId;

	@ApiModelProperty(value = "역할 그룹명")
	private String roleName;

	@ApiModelProperty(value = "사용여부")
	private String useYn;

	@ApiModelProperty(value = "법인 코드")
	private String erpRegalCode;

	@ApiModelProperty(value = "부서 코드")
	private String erpOrganizationCode;
}
