package kr.co.pulmuone.v1.comm.constants;

public final class SystemConstants {
    private SystemConstants(){}

	//BATCH
	public static final Long BATCH_ILLEGAL_DETECT_HOUR = 1L;				// 부정거래탐지 - 탐지시간
	public static final int BATCH_ILLEGAL_USER_JOIN_CRITERIA = 5;			// 부정거래탐지 - 회원가입 기준
	public static final int BATCH_ILLEGAL_LOGIN_FAIL_CRITERIA = 15;			// 부정거래탐지 - 로그인 실패 기준
	public static final int BATCH_ILLEGAL_ORDER_COUNT_CRITERIA = 10;		// 부정거래탐지 - 주문건수 기준
	public static final int BATCH_ILLEGAL_ORDER_PRICE_CRITERIA = 500000;	// 부정거래탐지 - 주문금액 기준

	public static final String AUTH_CSRF_SMS_TARGET_MOBILE = "0";	// AuthInterceptor CSRF ERROR SMS 전송 대상자 전화번호

}
