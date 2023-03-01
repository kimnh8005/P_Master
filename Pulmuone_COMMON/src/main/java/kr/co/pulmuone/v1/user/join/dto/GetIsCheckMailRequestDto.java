package kr.co.pulmuone.v1.user.join.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = " GetIsCheckMailRequestDto")
public class GetIsCheckMailRequestDto
{

	@ApiModelProperty(value = "이메일", required = true)
	private String mail;
}
