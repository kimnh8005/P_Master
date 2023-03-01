package kr.co.pulmuone.v1.system.code.dto;

import kr.co.pulmuone.v1.comm.base.dto.BaseRequestPageDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "GetCodeMasterListRequestDto")
public class GetCodeMasterListRequestDto extends BaseRequestPageDto {

	@ApiModelProperty(value = "조건타입")
	private String conditionType;

	@ApiModelProperty(value = "조건값")
	private String conditionValue;

	@ApiModelProperty(value = "사용여부")
	private String useYn;

}
