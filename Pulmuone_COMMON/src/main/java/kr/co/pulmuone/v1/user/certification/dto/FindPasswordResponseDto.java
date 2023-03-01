package kr.co.pulmuone.v1.user.certification.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = " FindPasswordResponseDto")
public class FindPasswordResponseDto
{

	@ApiModelProperty(value = "변경할비밀번호")
	private String passwordChangeCd;

}
