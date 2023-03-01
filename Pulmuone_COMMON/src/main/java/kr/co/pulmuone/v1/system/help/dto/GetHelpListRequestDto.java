package kr.co.pulmuone.v1.system.help.dto;

import kr.co.pulmuone.v1.comm.base.dto.BaseRequestPageDto;

import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "GetHelpListRequestDto")
public class GetHelpListRequestDto extends BaseRequestPageDto {

	@ApiModelProperty(value = "도움말 구분")
	private String divisionCode;

	@ApiModelProperty(value = "사용여부")
	private String useYn;

	@ApiModelProperty(value = "검색조건 select 박스")
	private String conditionType;

	@ApiModelProperty(value = "검색어")
	private String conditionValue;

	@ApiModelProperty(value = "도움말 ID List")
	private List<String> systemHelpIdList;

	@ApiModelProperty(value = "도움말 ID")
	private String systemHelpId;



}
