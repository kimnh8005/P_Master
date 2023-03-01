package kr.co.pulmuone.v1.user.login.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = " DoLoginResponseDataCertificationDto")
public class DoLoginResponseDataCertificationDto
{
	@ApiModelProperty(value = "로그인아이디", required = true)
	private String loginId;

	@ApiModelProperty(value = "변경할비밀번호", required = true)
	private String passwordChangeCd;

	/**
	 * 비밀번호 변경 기간
	 */
	private String defaultPwdChangePeriod;

	/**
	 * 비밀번호 실패 최대 수
	 */
	private String defaultPwdFailLimitCount;
}
