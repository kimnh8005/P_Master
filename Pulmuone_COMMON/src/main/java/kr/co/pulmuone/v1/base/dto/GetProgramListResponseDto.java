package kr.co.pulmuone.v1.base.dto;

import java.util.List;

import kr.co.pulmuone.v1.comm.base.dto.BaseResponseDto;
import kr.co.pulmuone.v1.base.dto.vo.GetProgramListResultVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "GetProgramListResponseDto")
public class GetProgramListResponseDto extends BaseResponseDto {

	@ApiModelProperty(value = "")
	private	List<GetProgramListResultVo> rows;

}
