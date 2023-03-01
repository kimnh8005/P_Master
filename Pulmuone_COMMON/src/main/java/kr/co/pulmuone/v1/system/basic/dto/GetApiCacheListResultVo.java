package kr.co.pulmuone.v1.system.basic.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "GetApiCacheListResultVo")
public class GetApiCacheListResultVo {
	
	@ApiModelProperty(value = "순번")
	private int no;
	
	@ApiModelProperty(value = "PK")
	private String stApiCacheId;
	
	@ApiModelProperty(value = "cache 적용 url")
	private String apiUrl;
	
	@ApiModelProperty(value = "cache 파일 경로")
	private String casheFilePath;
	
	@ApiModelProperty(value = "api url response json")
	private String casheData;
	
	@ApiModelProperty(value = "cache 시간")
	private String casheTime;
	
	@ApiModelProperty(value = "메모")
	private String memo;
	
	@ApiModelProperty(value = "사용유무")
	private String useYn;
}
