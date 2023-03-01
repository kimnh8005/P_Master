package kr.co.pulmuone.v1.user.certification.dto.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "GetFindPasswordResultVo")
public class GetFindPasswordResultVo
{

	@ApiModelProperty(value = "로그인아이디")
	private String loginId;

}
