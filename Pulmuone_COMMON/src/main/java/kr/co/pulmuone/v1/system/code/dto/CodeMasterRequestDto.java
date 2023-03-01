package kr.co.pulmuone.v1.system.code.dto;

import kr.co.pulmuone.v1.comm.base.dto.BaseRequestDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ApiModel(description = "PutCodeMasterRequestDto")
public class CodeMasterRequestDto extends BaseRequestDto {

	@ApiModelProperty(value = "공통 코드 마스터 시퀀스 아이디")
	private String stComnCodeMstId;

	@ApiModelProperty(value = "공통 마스터 코드명")
	private String commonMasterName;

	@ApiModelProperty(value = "공통 마스터 코드")
	private String commonMasterCode;

	@ApiModelProperty(value = "설명")
	private String comment;

	@ApiModelProperty(value = "사용여부")
	private String useYn;

}
