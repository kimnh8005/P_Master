package kr.co.pulmuone.v1.comm.constants;

public final class BuyerConstants {
    private BuyerConstants(){}

    public static final Long UR_MOVE_REASON_ID = 100L;  // 회원탈퇴사유 - 관리자 탈퇴 PK 값

	public static final int EMPLOYEE_MAX_POINT = 999999999;

    public static final int MYPAGE_MAIN_MONTH = 3;  // MALL - 마이페이지 - 메인 - 주문정보 집계기준 (월)

    //BATCH
    public static final int USER_GROUP_BATCH_USER_COUNT = 100;  // 회원등급 갱신 인원 단위 수

    public static final int BATCH_TARGET_MOVE_DAY = 365;        // 휴면전환 대상 일자

    public static final int USER_JOIN_DEPOSIT_DAY = 30;         // 신규 가입 첫주문 조회 기준

    public static final String COOKIE_PCID_CODE_KEY = "urPcidCd"; // PCID 코드 쿠키 키
}
