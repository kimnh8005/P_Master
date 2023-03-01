package kr.co.pulmuone.v1.user.certification.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = " GetFindLoginIdRequestDto")
public class GetFindLoginIdRequestDto
{

	@ApiModelProperty(value = "회원이름", required = true)
	private String userName;

	@ApiModelProperty(value = "핸드폰번호", required = true)
	private String mobile;
}
