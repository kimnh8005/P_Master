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
@ApiModel(description = "GetRoleRequestDto")
public class GetRoleRequestDto extends BaseRequestDto
{

	@ApiModelProperty(value = "역할그룹 PK")
	private String stRoleTypeId;

	@ApiModelProperty(value = "역할그룹명")
	private String roleName;

	@ApiModelProperty(value = "사용여부")
	private String useYn;

}
