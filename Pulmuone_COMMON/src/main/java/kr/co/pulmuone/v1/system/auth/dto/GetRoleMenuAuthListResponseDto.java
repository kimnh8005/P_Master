package kr.co.pulmuone.v1.system.auth.dto;

import java.util.List;

import kr.co.pulmuone.v1.comm.base.dto.BaseResponseDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.system.auth.dto.vo.GetRoleMenuAuthListResultVo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@Getter
@Setter
@ToString
@ApiModel(description = "GetRoleMenuAuthListResponseDto")
public class GetRoleMenuAuthListResponseDto extends BaseResponseDto{

	@ApiModelProperty(value = "리스트")
	private List<GetRoleMenuAuthListResultVo> rows;
}
