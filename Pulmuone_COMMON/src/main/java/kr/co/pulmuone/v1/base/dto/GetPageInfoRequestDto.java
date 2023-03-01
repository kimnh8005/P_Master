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
@ApiModel(description = "GetPageInfoRequestDto")
public class GetPageInfoRequestDto extends BaseRequestDto{

	@ApiModelProperty(value = "")
	String programId;

	@ApiModelProperty(value = "")
	String stMenuId;
}
