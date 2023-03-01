package kr.co.pulmuone.v1.system.menu.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseRequestPageDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "GetSystemUrlListRequestDto")
public class GetSystemUrlListRequestDto extends BaseRequestPageDto {

	@ApiModelProperty(value = "")
	String url;

	@ApiModelProperty(value = "")
	String urlName;
}
