package kr.co.pulmuone.v1.base.dto;

import kr.co.pulmuone.v1.comm.base.dto.BaseRequestDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "GetCodeListRequestDto")
public class GetCodeListRequestDto extends BaseRequestDto{

	@ApiModelProperty(value = "")
	private String stCommonCodeMasterCode;

	@ApiModelProperty(value = "")
	private String useYn;

	@ApiModelProperty(value = "")
	private String orderBy;

	@ApiModelProperty(value = "추가속성1")
	private String attr1;

	@ApiModelProperty(value = "추가속성2")
	private String attr2;

	@ApiModelProperty(value = "추가속성3")
	private String attr3;

}
