package kr.co.pulmuone.v1.system.basic.dto;

import kr.co.pulmuone.v1.comm.base.dto.BaseRequestPageDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "GetPSKeyTypeRequestDto")
public class GetPSKeyTypeRequestDto extends BaseRequestPageDto{

	@ApiModelProperty(value = "서비스정책")
	private String psKey;

}
