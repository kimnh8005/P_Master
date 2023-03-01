package kr.co.pulmuone.v1.system.program.dto.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "GetProgramResultVo")
public class GetProgramResultVo {

	@ApiModelProperty(value = "")
	private Long stProgramId;

	@ApiModelProperty(value = "")
	private String programId;

	@ApiModelProperty(value = "")
	private String programName;

	@ApiModelProperty(value = "")
	private String htmlPath;

	@ApiModelProperty(value = "")
	private String useYn;

	@ApiModelProperty(value = "")
	private String url;

	@ApiModelProperty(value = "")
	private String businessType;

	@ApiModelProperty(value = "")
	private Long gbDictionaryMasterId;


}
