package kr.co.pulmuone.v1.system.program.dto.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "ProgramAuthVo")
public class ProgramAuthVo {

	@ApiModelProperty(value = "프로그램 권한 PK")
	private Long stProgramAuthId;

	@ApiModelProperty(value = "프로그램 권한 코드")
	private String programAuthCode;

	@ApiModelProperty(value = "프로그램 권한 명")
	private String programAuthCodeName;

	@ApiModelProperty(value = "메모")
	private String programAuthCodeNameMemo;

	@ApiModelProperty(value = "사용여부")
	private String useYn;
}
