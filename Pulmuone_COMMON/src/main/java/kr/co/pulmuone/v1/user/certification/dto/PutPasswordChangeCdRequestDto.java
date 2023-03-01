package kr.co.pulmuone.v1.user.certification.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = " PutPasswordChangeCdRequestDto")
public class PutPasswordChangeCdRequestDto
{

	@ApiModelProperty(value = "유저아이디", required = true)
	private String urUserId;

	@ApiModelProperty(value = "비밀번호변경코드", required = true)
	private String passwordChangeCd;

}
