package kr.co.pulmuone.v1.user.certification.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = " GetEmployeesCertificationRequestDto")
public class GetEmployeesCertificationRequestDto
{
	@ApiModelProperty(value = "이메일 또는 이름", required = true)
	private String email;

	@ApiModelProperty(value = "사번", required = true)
	private String urErpEmployeeCode;

	@ApiModelProperty(value= "임직원 코드", hidden=true)
	private String tempCertiNo;

	@ApiModelProperty(value= "회원코드 pk", hidden=true)
	private String urUserId;

}
