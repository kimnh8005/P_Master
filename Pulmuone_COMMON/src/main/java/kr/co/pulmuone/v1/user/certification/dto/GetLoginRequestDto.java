package kr.co.pulmuone.v1.user.certification.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = " GetLoginRequestDto")
public class GetLoginRequestDto
{
	@ApiModelProperty(value = "로그인아이디", required = true )
	private String loginId;

	@ApiModelProperty(value = "비밀번호", required = true)
	private String password;

	@ApiModelProperty(value = "아이디 저장")
	private String saveLoginId;

	@ApiModelProperty(value = "자동로그인")
	private String autoLogin;

	@ApiModelProperty(value = "캡차 입력값")
	private String captcha;

}
