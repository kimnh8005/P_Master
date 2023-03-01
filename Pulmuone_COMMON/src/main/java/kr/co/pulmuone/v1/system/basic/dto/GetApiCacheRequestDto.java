package kr.co.pulmuone.v1.system.basic.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseRequestPageDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "GetApiCacheDictionaryRequestDto")
public class GetApiCacheRequestDto extends BaseRequestPageDto {
	
	@ApiModelProperty(value = "API URL")
	private String apiUrl;
	
	@ApiModelProperty(value = "사용여부")
	private String useYn;
	
}
