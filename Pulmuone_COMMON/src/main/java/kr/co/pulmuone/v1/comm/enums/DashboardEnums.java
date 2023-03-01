package kr.co.pulmuone.v1.comm.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

public class DashboardEnums {

  @Getter
  @RequiredArgsConstructor
  public enum DashboardMessage implements MessageCommEnum {
      DASHBOARD_MANAGE_SUCCESS                                           ("0000", "정상처리 되었습니다.")
    , DASHBOARD_PARAM_NO_INPUT                                           ("1001", "입력정보가 존재하지 않습니다.")
    , DASHBOARD_PARAM_NO_PUT_DATA                                        ("1002", "대시보드 수정 대상이 없습니다.")
    , DASHBOARD_ERROR_PUT_PROC                                           ("1003", "대시보드 수정 오류입니다.")
    ;

    private final String code;
    private final String message;
  }

}
