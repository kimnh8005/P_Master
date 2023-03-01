package kr.co.pulmuone.v1.base.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseRequestDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "담당자 팝업 Request")
public class GetGrantAuthEmployeePopupRequestDto extends BaseRequestDto{

	@ApiModelProperty(value = "검색조건")
	private String searchCondition;

	@ApiModelProperty(value = "검색어")
	private String findKeyword;

	@ApiModelProperty(value = "법인정보")
	private String regal;

	@ApiModelProperty(value = "조직정보")
	private String organization;
}
