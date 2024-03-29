package kr.co.pulmuone.v1.system.program.dto;

import java.util.List;

import kr.co.pulmuone.v1.comm.base.dto.BaseResponseDto;
import kr.co.pulmuone.v1.system.program.dto.vo.GetProgramNameListResultVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "GetProgramNameListResponseDto")
public class GetProgramNameListResponseDto extends BaseResponseDto{

	@ApiModelProperty(value = "리스트")
	private	List<GetProgramNameListResultVo> rows;

	@ApiModelProperty(value = "전체개수")
	private int total;

}
