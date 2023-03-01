package kr.co.pulmuone.v1.system.auth.dto.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "GetRoleResultVo")
public class GetRoleResultVo {

	@ApiModelProperty(value = "역할그룹 PK")
	private String inputStRoleTypeId;

	@ApiModelProperty(value = "역할그룹명")
	private String inputRoleName;

	@ApiModelProperty(value = "사용여부")
	private String inputUseYn;

	@ApiModelProperty(value = "법인명")
	private String inputErpRegalName;

	@ApiModelProperty(value = "법인 코드")
	private String inputErpRegalCode;

	@ApiModelProperty(value = "부서명")
	private String inputErpOrganizationName;

	@ApiModelProperty(value = "부서 코드")
	private String inputErpOrganizationCode;

	@ApiModelProperty(value = "부문 전체 조회 가능여부")
	private String sectorAllViewYn;
}
