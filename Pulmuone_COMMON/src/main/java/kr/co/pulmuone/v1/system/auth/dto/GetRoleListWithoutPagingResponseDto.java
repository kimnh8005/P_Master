package kr.co.pulmuone.v1.system.auth.dto;

import kr.co.pulmuone.v1.comm.base.dto.BaseResponseDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.system.auth.dto.vo.getRoleListWithoutPagingResultVo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
@ApiModel(description = "GetRoleListWithoutPagingResponseDto")
public class GetRoleListWithoutPagingResponseDto extends BaseResponseDto
{

	@ApiModelProperty(value = "리스트")
	private List<getRoleListWithoutPagingResultVo> rows;

}