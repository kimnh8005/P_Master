package kr.co.pulmuone.v1.user.join.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = " GetIsCheckLoginIdRequestDto")
public class GetIsCheckLoginIdRequestDto
{

	@ApiModelProperty(value = "로그인아이디", required = true)
	private String loginId;

	@ApiModelProperty(value = "기존회원 로그인아이디")
	private String asisLoginId;

}
