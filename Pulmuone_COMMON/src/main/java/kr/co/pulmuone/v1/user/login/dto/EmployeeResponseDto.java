package kr.co.pulmuone.v1.user.login.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@ToString
@ApiModel(description = "EmployeeResponseDto")
public class EmployeeResponseDto implements Serializable {

	@ApiModelProperty(value = "발급회원코드")
    private String urUserId;

	@ApiModelProperty(value = "로그인 ID")
	private String loginId;

	@ApiModelProperty(value = "회원상태")
	private String statusType;

	@ApiModelProperty(value = "임직원 연락처")
	private String mobile;

	@ApiModelProperty(value = "임직원 이메일")
	private String email;

	@ApiModelProperty(value = "이차인증 코드")
	private String authCertiNo;
}
