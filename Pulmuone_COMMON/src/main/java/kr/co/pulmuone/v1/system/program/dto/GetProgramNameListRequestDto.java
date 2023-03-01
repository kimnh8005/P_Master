package kr.co.pulmuone.v1.system.program.dto;

import kr.co.pulmuone.v1.comm.base.dto.BaseRequestPageDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@ApiModel(description = "GetProgramNameListRequestDto")
public class GetProgramNameListRequestDto extends BaseRequestPageDto{

	@ApiModelProperty(value = "프로그램명")
	private String programName;

	@ApiModelProperty(value = "사용여부")
	private String useYn;

	@ApiModelProperty(value = "프로그램 아이디")
	private String programId;

	@Builder
	public GetProgramNameListRequestDto(String programName, String useYn) {
		this.programName = programName;
		this.useYn = useYn;
	}
}
