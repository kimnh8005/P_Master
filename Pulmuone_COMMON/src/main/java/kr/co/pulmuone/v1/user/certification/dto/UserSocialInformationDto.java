package kr.co.pulmuone.v1.user.certification.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "SNS 사용자 정보 RequestDto")
public class UserSocialInformationDto {

  @ApiModelProperty(value = "회원 PK")
  private String urUserId;

  @ApiModelProperty(value = "소셜서비스 제공사")
  private String provider;

  @ApiModelProperty(value = "소셜서비스 아이디")
  private String socialId;

  @ApiModelProperty(value = "소셜서비스 이름")
  private String name;

  @ApiModelProperty(value = "액세스 토큰")
  private String accessToken;
}
