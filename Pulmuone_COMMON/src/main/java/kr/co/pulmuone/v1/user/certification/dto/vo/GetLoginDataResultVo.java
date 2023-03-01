package kr.co.pulmuone.v1.user.certification.dto.vo;

import java.time.LocalDateTime;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = "GetLoginDataResultVo")
public class GetLoginDataResultVo {

  @ApiModelProperty(value = "유저아이디")
  private String urUserId;

  @ApiModelProperty(value = "실패건수")
  private int    failCnt;

  @ApiModelProperty(value = "임의발행여부")
  private String tmprrYn;

  @ApiModelProperty(value = "비밀번호변경일자")
  private String pwdChangeDate;

  @ApiModelProperty(value = "비빌번호변경3개월 초과여부")
  private String passwordChangeThreeMonOverYn; // 비밀번호 바꾼지 3개월 넘어가면 Y

  @ApiModelProperty(value = "상태")
  private String status;                       // BUYER_STATUS.NORMAL : 정상,
                                               // BUYER_STATUS.STOP : 정지

  @ApiModelProperty(value = "SMS동의여부")
  private String smsYn;

  @ApiModelProperty(value = "SMS동의일자")
  private String smsYnDate;

  @ApiModelProperty(value = "SMS동의1년초과여부")
  private String smsYnDateOneYearOverYn;

  @ApiModelProperty(value = "이메일동의여부")
  private String mailYn;

  @ApiModelProperty(value = "이메일동의일자")
  private String mailYnDate;

  @ApiModelProperty(value = "이메일동의1년초과여부")
  private String mailYnDateOneYearOverYn;

  @ApiModelProperty(value = "관리자로정의된실패카우트")
  private int    urLoginFailCount;

  @ApiModelProperty(value = "BO 기초설정에서 셋팅한 비밀번호변경주기")
  private String defaultPwdChangePeriod;

  @ApiModelProperty(value = "BO 기초설정에서 셋팅한 실패제한횟수")
  private String defaultPwdFailLimitCount;

  @ApiModelProperty(value = "임시 비밀번호 유효시간 만료여부")
  private String tmprrExpirationYn;

}
