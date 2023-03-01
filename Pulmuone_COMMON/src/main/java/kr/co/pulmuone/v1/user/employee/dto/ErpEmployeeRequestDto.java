package kr.co.pulmuone.v1.user.employee.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.comm.base.dto.BaseRequestPageDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "ERP 임직원정보 Request")
public class ErpEmployeeRequestDto extends BaseRequestPageDto{

	@ApiModelProperty(value = "검색조건")
	private String searchCondition;

	@ApiModelProperty(value = "검색키워드")
	private String findKeyword;

}
