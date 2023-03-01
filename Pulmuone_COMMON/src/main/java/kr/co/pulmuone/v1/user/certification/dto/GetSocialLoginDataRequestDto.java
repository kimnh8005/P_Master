package kr.co.pulmuone.v1.user.certification.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "SNS로그인 정보 조회 RequestDto")
public class GetSocialLoginDataRequestDto{

	@ApiModelProperty(value = "소셜서비스 제공사", required = true)
	private String provider;

	@ApiModelProperty(value = "소셜서비스 제공 ID", required = true)
	private String socialId;

}
