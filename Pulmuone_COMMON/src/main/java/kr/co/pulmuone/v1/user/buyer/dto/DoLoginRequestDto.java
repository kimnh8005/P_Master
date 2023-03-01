package kr.co.pulmuone.v1.user.buyer.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = " DoLoginRequestDto")
public class DoLoginRequestDto
{
	@ApiModelProperty(value = "로그인아이디", required = true)
	private String loginId;

	@ApiModelProperty(value = "비밀번호", required = true)
	private String password;

	@ApiModelProperty(value = "암호화된비밀번호", required = true)
	private String encryptPassword;

	@ApiModelProperty(value = "아이디 저장")
	private boolean saveLoginId = false;

	@ApiModelProperty(value = "자동로그인")
	private boolean autoLogin = false;

	@ApiModelProperty(value = "캡차 입력값")
	private String captcha;
}
