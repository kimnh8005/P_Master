package kr.co.pulmuone.v1.system.program.dto.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "GetProgramListResultVo")
public class GetProgramNameListResultVo {

	@ApiModelProperty(value = "프로그램시퀀스번호")
	private String stProgramId;

	@ApiModelProperty(value = "프로그램아이디")
	private String programId;

	@ApiModelProperty(value = "프로그램명")
	private String programName;
}
