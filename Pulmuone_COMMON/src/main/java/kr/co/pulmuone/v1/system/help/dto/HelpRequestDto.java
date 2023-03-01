package kr.co.pulmuone.v1.system.help.dto;

import kr.co.pulmuone.v1.comm.base.dto.BaseRequestPageDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "AddHelpRequestDto")
public class HelpRequestDto extends BaseRequestPageDto {

	@ApiModelProperty(value = "도움말 id")
	private String id;

	@ApiModelProperty(value = "도움말 구분 공통코드")
	private String inputDivisionCode;

	@ApiModelProperty(value = "도움말 구분 입력 id")
	private String divisionId;

	@ApiModelProperty(value = "도움말 제목")
	private String title;

	@ApiModelProperty(value = "도움말 내용")
	private String content;

	@ApiModelProperty(value = "도움말 내용(HTML 태그 제거)")
	private String contentPlain;

	@ApiModelProperty(value = "사용여부")
	private String inputUseYn;

}
