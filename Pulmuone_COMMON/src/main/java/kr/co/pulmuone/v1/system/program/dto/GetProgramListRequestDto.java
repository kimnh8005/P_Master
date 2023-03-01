package kr.co.pulmuone.v1.system.program.dto;

import kr.co.pulmuone.v1.comm.base.dto.BaseRequestPageDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@ApiModel(description = "프로그램관리 검색 조건 DTO")
public class GetProgramListRequestDto extends BaseRequestPageDto{

	@ApiModelProperty(value = "프로그램명")
	private String programName;

	@ApiModelProperty(value = "사용여부")
	private String useYn;

	@ApiModelProperty(value = "프로그램ID")
	private String programId;

	@ApiModelProperty(value = "프로그램URL")
	private String url;
	
	@Builder
	public GetProgramListRequestDto(String programId, String programName, String useYn, String url) {
		this.programId = programId;
		this.programName = programName;
		this.useYn = useYn;
		this.url = url;
	}
}
