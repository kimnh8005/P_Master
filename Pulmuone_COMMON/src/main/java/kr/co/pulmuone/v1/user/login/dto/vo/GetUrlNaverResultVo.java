package kr.co.pulmuone.v1.user.login.dto.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "GetUrlNaverResultVo")
public class GetUrlNaverResultVo
{

	@ApiModelProperty(value = "Redirection URL")
	private String redirectionUrl;

}
