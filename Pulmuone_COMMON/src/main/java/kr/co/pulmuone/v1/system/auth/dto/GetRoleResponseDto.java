package kr.co.pulmuone.v1.system.auth.dto;

import kr.co.pulmuone.v1.comm.base.dto.BaseResponseDto;
import kr.co.pulmuone.v1.system.auth.dto.vo.GetRoleResultVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "GetRoleResponseDto")
public class GetRoleResponseDto extends BaseResponseDto
{

	@ApiModelProperty(value = "조회 데이터")
	private GetRoleResultVo rows;

}