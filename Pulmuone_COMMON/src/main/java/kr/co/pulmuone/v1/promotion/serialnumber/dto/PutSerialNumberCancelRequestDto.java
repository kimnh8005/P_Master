package kr.co.pulmuone.v1.promotion.serialnumber.dto;

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
@ApiModel(description = " GetClauseRequestDto")
public class PutSerialNumberCancelRequestDto extends BaseRequestDto {

	@ApiModelProperty(value = "검색조건")
	private String searchSelect;

	@ApiModelProperty(value = "업데이트 데이터")
	private String updateData;

	@ApiModelProperty(value = "업데이트 데이터 리스트")
	private List<PutSerialNumberCancelRequestSaveDto> updateRequestDtoList;
}