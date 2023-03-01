package kr.co.pulmuone.v1.user.certification.dto;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(description = " GetSessionUserCertificationResponseDto")
public class GetSessionUserCertificationResponseDto {

  private String userName;

  private String mobile;

  private String gender;

  private String ci;

  private String birthday;

  // 기존 탈퇴 회원 여부(Y)
  private String beforeUserDropYn;

  // 임직원인증을 실패건수
  private String tempCertiNoForEmployee;

  // 임직원인증을 실패건수
  private String tempCertiNoForEmployeeFailCnt;


}
