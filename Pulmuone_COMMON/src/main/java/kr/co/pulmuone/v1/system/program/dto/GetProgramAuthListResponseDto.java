package kr.co.pulmuone.v1.system.program.dto;

import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseResponseDto;
import kr.co.pulmuone.v1.system.program.dto.vo.ProgramAuthVo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "GetProgramAuthListResponseDto")
public class GetProgramAuthListResponseDto extends BaseResponseDto{

	@ApiModelProperty(value = "리스트")
	private	List<ProgramAuthVo> rows;
}
