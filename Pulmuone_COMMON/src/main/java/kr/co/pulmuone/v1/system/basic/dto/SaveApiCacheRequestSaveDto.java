package kr.co.pulmuone.v1.system.basic.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseRequestDto;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ApiModel(description = "SaveApiCacheRequestSaveDto")
public class SaveApiCacheRequestSaveDto extends BaseRequestDto {
	
	@JsonSerialize(using = ToStringSerializer.class)
	@ApiModelProperty(value = "PK")
	String stApiCacheId;
	
	@JsonSerialize(using = ToStringSerializer.class)
	@ApiModelProperty(value = "cache 적용 url")
	String apiUrl;
	
	@JsonSerialize(using = ToStringSerializer.class)
	@ApiModelProperty(value = "cache 파일 경로")
	String casheFilePath;
	
	@JsonSerialize(using = ToStringSerializer.class)
	@ApiModelProperty(value = "api url response json")
	String casheData;
	
	@JsonSerialize(using = ToStringSerializer.class)
	@ApiModelProperty(value = "cache 시간")
	String casheTime;
	
	@JsonSerialize(using = ToStringSerializer.class)
	@ApiModelProperty(value = "메모")
	String memo;
	
	@JsonSerialize(using = ToStringSerializer.class)
	@ApiModelProperty(value = "사용유무")
	String useYn;
}
