package kr.co.pulmuone.v1.user.certification.dto.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "회원접속로그 vo")
public class ConnectionLogVo {

  @ApiModelProperty(value = "유저아이디")
  private String urUserId;

  @ApiModelProperty(value = "유저PCID코드")
  private String urPcidCd;

  @ApiModelProperty(value = "아이피")
  private String ip;

  @ApiModelProperty(value = "회원접속로그 성공여부")
  private String successYn;

  @ApiModelProperty(value = "회원접속로그 PK")
  private String urConnectLogId;

}
