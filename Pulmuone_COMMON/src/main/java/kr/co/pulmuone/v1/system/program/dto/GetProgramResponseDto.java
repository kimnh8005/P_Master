package kr.co.pulmuone.v1.system.program.dto;

import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseResponseDto;
import kr.co.pulmuone.v1.system.program.dto.vo.GetProgramResultVo;
import kr.co.pulmuone.v1.system.program.dto.vo.ProgramAuthVo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "GetProgramResponseDto")
public class GetProgramResponseDto extends BaseResponseDto{

	@ApiModelProperty(value = "데이터건수")
	private	GetProgramResultVo rows;

	@ApiModelProperty(value = "권한리스트")
	private List<ProgramAuthVo> authList;
}
