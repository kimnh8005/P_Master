package kr.co.pulmuone.v1.system.code.dto;

import kr.co.pulmuone.v1.comm.base.dto.BaseRequestPageDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ApiModel(description = "GetCodeMasterNameListRequestDto")
public class GetCodeMasterNameListRequestDto extends BaseRequestPageDto {

	@ApiModelProperty(value = "조건타입")
	private String conditionType;

	@ApiModelProperty(value = "조건값")
	private String conditionValue;

	@ApiModelProperty(value = "사용여부")
	private String useYn;
}
