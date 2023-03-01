package kr.co.pulmuone.v1.user.certification.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "AddSessionUserCertificationRequestDto")
public class AddSessionUserCertificationRequestDto {

  @ApiModelProperty(value = "회원이름")
  private String userName;

  @ApiModelProperty(value = "핸드폰번호")
  private String mobile;

  @ApiModelProperty(value = "성별")
  private String gender;

  @ApiModelProperty(value = "CI")
  private String ci;

  @ApiModelProperty(value = "생년월일")
  private String birthday;

  @ApiModelProperty(value = "기존 탈퇴 회원 여부(Y)")
  private String beforeUserDropYn;

}
