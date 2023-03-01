package kr.co.pulmuone.v1.user.certification.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "AddSessionEmployeeCertificationRequestDto")
public class AddSessionEmployeeCertificationRequestDto {
	@ApiModelProperty(value = "임직원코드")
	private String urErpEmployeeCode;

	@ApiModelProperty(value = "임시임직원코드")
	private String tempUrErpEmployeeCode;

	@ApiModelProperty(value = "임시발급번호")
	private String tempCertiNo;

	@ApiModelProperty(value = "실패건수")
	private String failCnt;

	@ApiModelProperty(value = "urUserId")
	private String urUserId;

}
