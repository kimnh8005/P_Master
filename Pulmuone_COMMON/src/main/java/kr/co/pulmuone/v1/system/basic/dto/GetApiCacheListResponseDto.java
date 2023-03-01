package kr.co.pulmuone.v1.system.basic.dto;

import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseResponseDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "GetApiCacheListResponseDto")
public class GetApiCacheListResponseDto extends BaseResponseDto {
	
	@ApiModelProperty(value = "리스트")
	private List<GetApiCacheListResultVo> rows;
	
	@ApiModelProperty(value = "총 개수")
	private int total;
	
}