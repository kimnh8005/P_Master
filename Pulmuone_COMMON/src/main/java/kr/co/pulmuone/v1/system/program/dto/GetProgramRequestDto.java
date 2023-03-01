package kr.co.pulmuone.v1.system.program.dto;

import kr.co.pulmuone.v1.comm.base.dto.BaseRequestDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "GetProgramRequestDto")
public class GetProgramRequestDto extends BaseRequestDto{

	@ApiModelProperty(value = "프로그램시퀀스번호")
	private String stProgramId;
}
