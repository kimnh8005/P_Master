package kr.co.pulmuone.v1.user.buyer.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.pulmuone.v1.user.certification.dto.GetClauseArrayRequestDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = " AddChangeClauseAgreeRequestDto")
public class AddChangeClauseAgreeRequestDto
{

	@ApiModelProperty(value = "로그인 아이디", required = true)
	private String loginId;

	@ApiModelProperty(value = "비밀번호", required = true)
	private String password;

	@ApiModelProperty(value = "아이디저장")
	private String saveLoginId;

	@ApiModelProperty(value = "자동로그인")
	private String autoLogin;

	@ApiModelProperty(value = "약관동의리스트")
	private GetClauseArrayRequestDto[] clause;
}
