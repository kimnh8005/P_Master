package kr.co.pulmuone.v1.system.log.dto;

import kr.co.pulmuone.v1.comm.base.dto.BaseRequestPageDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "GetBatchLogListRequestDto")
public class GetBatchLogListRequestDto extends BaseRequestPageDto {

	@ApiModelProperty(value = "배치결과")
	String status;

	@ApiModelProperty(value = "검색타입")
	String searchType;

	@ApiModelProperty(value = "검색어")
	String searchText;

	@ApiModelProperty(value = "시작 생성일")
	String startCreateDate;

	@ApiModelProperty(value = "종료 생성일")
	String endCreateDate;


}
