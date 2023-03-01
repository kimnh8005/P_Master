package kr.co.pulmuone.v1.user.certification.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = " GetSessionUserSnsCertificationResponseDto")
public class GetSessionUserSnsCertificationResponseDto
{
	@ApiModelProperty(value = "제공사")
	private String provider;

	@ApiModelProperty(value = "소셜아이디")
	private String socialId;

	@ApiModelProperty(value = "소셜명")
	private String socialName;

	@ApiModelProperty(value = "소셜이메일")
	private String socialMail;

	@ApiModelProperty(value = "소셜기본프로필이미지")
	private String socialProfileImage;

}
