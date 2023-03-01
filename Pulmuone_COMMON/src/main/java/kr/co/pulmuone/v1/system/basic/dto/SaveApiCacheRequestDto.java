package kr.co.pulmuone.v1.system.basic.dto;

import java.util.ArrayList;
import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseRequestDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = " SaveApiCacheRequestDto")
public class SaveApiCacheRequestDto extends BaseRequestDto
{
	
	@ApiModelProperty(value = "등록데이터", required = false)
	String insertData;
	
	@ApiModelProperty(value = "변경데이터", required = false)
	String updateData;
	
	@ApiModelProperty(value = "삭제데이터", required = false)
	String deleteData;
	
	@ApiModelProperty(value = "등록데이터 리스트", hidden = true)
	List<SaveApiCacheRequestSaveDto> insertRequestDtoList = new ArrayList<SaveApiCacheRequestSaveDto>();
	
	@ApiModelProperty(value = "변경데이터 리스트", hidden = true)
	List<SaveApiCacheRequestSaveDto> updateRequestDtoList = new ArrayList<SaveApiCacheRequestSaveDto>();
	
	@ApiModelProperty(value = "삭제데이터 리스트", hidden = true)
	List<SaveApiCacheRequestSaveDto> deleteRequestDtoList = new ArrayList<SaveApiCacheRequestSaveDto>();
	
}
