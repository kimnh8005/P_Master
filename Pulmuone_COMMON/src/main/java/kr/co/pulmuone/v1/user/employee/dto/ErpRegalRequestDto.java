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
@ApiModel(description = "법인정보 Request")
public class ErpRegalRequestDto extends BaseRequestPageDto{

	@ApiModelProperty(value = "검색조건")
	private String searchCondition;

	@ApiModelProperty(value = "검색키워드")
	private String findKeyword;

	@ApiModelProperty(value = "임직원 혜택적용")
	private String employeeBenefitsApply;

}
