package kr.co.pulmuone.v1.user.certification.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = " GetCheckDuplicateCIRequestDto")
public class GetCheckDuplicateCIRequestDto
{

	@ApiModelProperty(value = "로그인아이디", required = true)
	private String loginId;

	@ApiModelProperty(value = "이메일", required = true)
	private String mail;

	@ApiModelProperty(value = "추천인아이디", required = true)
	private String recommendLoginId;

	@ApiModelProperty(value = "회원이름", required = true)
	private String userName;

	@ApiModelProperty(value = "휴대폰번호", required = true)
	private String mobile;

	@ApiModelProperty(value = "CICD", required = true)
	private String ciCd;
}
