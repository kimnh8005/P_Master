package kr.co.pulmuone.v1.user.login.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "SNS 로그인 계정 RequestDto")
public class SyncAccountRequestDto {

	@ApiModelProperty(value = "회원 로그인 ID", required = true)
	private String loginId;

	@ApiModelProperty(value = "회원 로그인 비밀번호", required = true)
	private String password;

	@ApiModelProperty(value = "캡차")
	private String captcha;
}
