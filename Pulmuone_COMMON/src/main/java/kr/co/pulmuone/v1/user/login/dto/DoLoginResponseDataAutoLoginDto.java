package kr.co.pulmuone.v1.user.login.dto;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = " DoLoginResponseDataAutoLoginDto")
public class DoLoginResponseDataAutoLoginDto
{
	//프론트 자동로그인 엑션 코드 (INSERT: 저장, DELETE: 삭제)
	private String autoLoginKeyActionCode;

	//자동로그인 KEY
	private String autoLoginKey;
}
