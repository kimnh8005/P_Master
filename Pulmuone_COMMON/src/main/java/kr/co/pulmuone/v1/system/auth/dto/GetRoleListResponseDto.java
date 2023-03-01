package kr.co.pulmuone.v1.system.auth.dto;

import java.util.List;

import kr.co.pulmuone.v1.comm.base.dto.BaseResponseDto;
import kr.co.pulmuone.v1.system.auth.dto.vo.GetRoleListResultVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "GetRoleListResponseDto")
public class GetRoleListResponseDto extends BaseResponseDto {

	@ApiModelProperty(value = "리스트")
	private List<GetRoleListResultVo> rows;

	@ApiModelProperty(value = "조회 데이터 수")
	private int total;
}