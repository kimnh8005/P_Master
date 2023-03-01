package kr.co.pulmuone.v1.system.program.dto;

import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseRequestDto;
import kr.co.pulmuone.v1.system.program.dto.vo.ProgramAuthVo;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@ApiModel(description = "AddProgramRequestDto")
public class ProgramRequestDto  extends BaseRequestDto {

	@ApiModelProperty(value = "프로그램 PK")
	private Long stProgramId;

	@ApiModelProperty(value = "프로그램 ID")
	private String programId;

	@ApiModelProperty(value = "웹 URL")
	private String url;

	@ApiModelProperty(value = "사용여부")
	private String useYn;

	@ApiModelProperty(value = "업무구분")
	private String businessType;

	@ApiModelProperty(value = "HTML 경로")
	private String htmlPath;

	@ApiModelProperty(value = "프로그램명(표준용어 마스터 코드)")
	private Long gbDictionaryMasterId;

	@Builder
	public ProgramRequestDto(Long stProgramId, String programId, String url, String useYn, String businessType, String htmlPath, Long gbDictionaryMasterId) {
		this.stProgramId = stProgramId;
		this.programId = programId;
		this.url = url;
		this.useYn = useYn;
		this.businessType = businessType;
		this.htmlPath = htmlPath;
		this.gbDictionaryMasterId = gbDictionaryMasterId;
	}

	@ApiModelProperty(value = "권한 등록 데이터", required = false)
    String authInsertData;

    @ApiModelProperty(value = "권한 변경 데이터", required = false)
    String authUpdateData;

    @ApiModelProperty(value = "등록 코드 리스트", hidden = true)
    List<ProgramAuthVo> authInsertVoList;

    @ApiModelProperty(value = "변경 코드 리스트", hidden = true)
    List<ProgramAuthVo> authUpdateVoList;
}
