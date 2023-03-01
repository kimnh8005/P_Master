package kr.co.pulmuone.v1.system.auth.dto;

import kr.co.pulmuone.v1.comm.base.dto.BaseRequestDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "AddRoleRquestDto")
public class RoleRequestDto extends BaseRequestDto
{

	@ApiModelProperty(value = "역할그룹 PK")
	private String inputStRoleTypeId;

	@ApiModelProperty(value = "역할그룹명")
	private String inputRoleName;

	@ApiModelProperty(value = "사용여부")
	private String inputUseYn;

	@ApiModelProperty(value = "법인 코드")
	private String inputErpRegalCode;

	@ApiModelProperty(value = "부서 코드")
	private String inputErpOrganizationCode;

	@ApiModelProperty(value = "action (copy : 복사)")
	private String action;

	@ApiModelProperty(value = "복사일 경우 : 복사하려는 역할그룹 PK")
	private String originStRoleTypeId;

	@ApiModelProperty(value = "부문 전체 조회 가능여부")
	private String inputSectorAllViewYn;
}