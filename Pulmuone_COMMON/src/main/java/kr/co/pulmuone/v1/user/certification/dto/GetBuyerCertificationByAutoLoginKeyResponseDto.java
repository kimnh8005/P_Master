package kr.co.pulmuone.v1.user.certification.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "GetBuyerCertificationByAutoLoginKeyResponseDto")
public class GetBuyerCertificationByAutoLoginKeyResponseDto
{
	@ApiModelProperty(value = "로그인아이디")
	private String loginId;

	@ApiModelProperty(value = "암호화된비밀번호")
	private String encryptPassword;
}
