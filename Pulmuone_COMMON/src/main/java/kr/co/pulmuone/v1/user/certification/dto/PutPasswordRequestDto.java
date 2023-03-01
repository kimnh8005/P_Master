package kr.co.pulmuone.v1.user.certification.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = " PutPasswordRequestDto")
public class PutPasswordRequestDto
{
	@ApiModelProperty(value = "유지시퀀스아이디")
	private String urUserId;
	
	@ApiModelProperty(value = "", required = true)
	private String passwordChangeCd;

	@ApiModelProperty(value = "", required = true)
	private String password;

}
