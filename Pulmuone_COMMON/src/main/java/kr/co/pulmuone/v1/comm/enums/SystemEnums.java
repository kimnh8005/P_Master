package kr.co.pulmuone.v1.comm.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * <PRE>
 * Forbiz Korea
 * 시스템 Enums
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :   작성일                :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 *  1.0    2020. 9. 14.                손진구          최초작성
 * =======================================================================
 * </PRE>
 */
public class SystemEnums
{

	// 사용자 역할권한
	@Getter
	@RequiredArgsConstructor
	public enum UserAuth implements MessageCommEnum
	{
		NEED_USER_AUTH("NEED_USER_AUTH", "권한역할은 필수 입니다.")
		;

		private final String code;
		private final String message;
	}


	// 사용자 그룹 관리
	@Getter
	@RequiredArgsConstructor
	public enum HelpData implements MessageCommEnum
	{
		HELP_DUPLICATE_DATA("HELP_DUPLICATE_DATA", "이미 등록 되어있습니다");
		;

		private final String code;
		private final String message;
	}

	// 회원기본정보 - 구매자구분
	@Getter
	@RequiredArgsConstructor
	public enum AgentType implements CodeCommEnum {
		ADMIN("AGENT_TYPE.A", "관리자 주문"),
		OUTMALL("AGENT_TYPE.O", "외부몰 주문"),
		APP("AGENT_TYPE.APP", "APP"),
		PC("AGENT_TYPE.W", "PC"),
		MOBILE("AGENT_TYPE.M", "MOBILE");

		private final String code;
		private final String codeName;
	}

	// Device Type
	@Getter
	@RequiredArgsConstructor
	public enum DeviceType implements CodeCommEnum {
		IOS("iOS", "IOS"),
		ANDROID("ANDROID", "ANDROID");


		private final String code;
		private final String codeName;
	}

	// APP .이벤트 이미지 환경변수
	@Getter
	@RequiredArgsConstructor
	public enum DeviceEventImageType implements CodeCommEnum {
		IOS_YN("APP_IOS_EVENT_IMG_YN", "IOS 사용여부"),
		ANDROID_YN("APP_ANDROID_EVENT_IMG_YN", "ANDROID 사용여부"),
		IOS_URL("APP_IOS_EVENT_IMG_URL", "IOS IMAGE URL"),
		ANDROID_URL("APP_ANDROID_EVENT_IMG_URL", "ANDROID IMAGE URL");


		private final String code;
		private final String codeName;
	}

	// ITGC 대상 메뉴관리
	@Getter
	@RequiredArgsConstructor
	public enum ItgcMenu implements CodeCommEnum {
		BOS_ACCOUNT("BOS_ACCOUNT", "BOS 계정관리", 1247L),
		ADMIN_AUTH("ADMIN_AUTH", "관리자 권한관리", 1248L),
		MENU_AUTH("MENU_AUTH", "메뉴권한관리", 1250L)
		;

		private final String code;
		private final String codeName;
		private final Long stMenuId;
	}

	// ITGC 구분
	@Getter
	@RequiredArgsConstructor
	public enum ItgcType implements CodeCommEnum {
		ACCOUNT_ADD("ITGC_TP.ACCOUNT_ADD", "계정등록"),
		PERSONAL_INFO("ITGC_TP.PERSONAL_INFO", "개인정보 열람권한"),
		AUTH("ITGC_TP.AUTH", "권한설정"),
		ROLE_ADD("ITGC_TP.ROLE_ADD", "역할생성"),
		AUTH_GET("ITGC_TP.AUTH_GET", "권한설정 조회"),
		AUTH_ADD("ITGC_TP.AUTH_ADD", "권한설정 저장"),
		AUTH_DEL("ITGC_TP.AUTH_DEL", "권한설정 삭제"),
		AUTH_EXCEL_DOWN("ITGC_TP.AUTH_EXCEL_DOWN", "권한설정 엑셀다운로드"),
		SUPER_ROLE("ITGC_TP.SUPER_ROLE", "Super 역할 그룹 생성"),
		ADMIN_DROP("ITGC_TP.ADMIN_DROP", "퇴사관리자"),
		SUPPLIER("ITGC_TP.SUPPLIER", "공급처"),
		WAREHOUSE("ITGC_TP.WAREHOUSE", "출고처")
		;

		private final String code;
		private final String codeName;
	}

	// ITGC 구분 - 상세
	@Getter
	@RequiredArgsConstructor
	public enum ItgcDetailType implements CodeCommEnum {
		ACCOUNT_ADD("ACCOUNT_ADD", "등록"),
		PERSONAL_INFO("PERSONAL_INFO", "변경상태"),
		AUTH("AUTH", "역할그룹명"),
		ROLE_ADD("ROLE_ADD", "역할그룹"),
		MENU("MENU", "메뉴페이지")
		;

		private final String code;
		private final String codeName;
	}

	// ITGC 등록 구분
	@Getter
	@RequiredArgsConstructor
	public enum ItgcAddType implements CodeCommEnum {
		ADD("ITGC_ADD_TP.ADD", "추가"),
		DEL("ITGC_ADD_TP.DEL", "삭제")
		;

		private final String code;
		private final String codeName;
	}

	// 디바이스 로그 유형
	@Getter
	@RequiredArgsConstructor
	public enum DeviceLogType implements CodeCommEnum {
		USER_JOIN("DEVICE_LOG_TYPE.USER_JOIN", "회원가입"),
		LOGIN_FAIL("DEVICE_LOG_TYPE.LOGIN_FAIL", "MALL 로그인 실패")
		;

		private final String code;
		private final String codeName;
	}

	// 부정거래탐지 - 부정거래 분류
	@Getter
	@RequiredArgsConstructor
	public enum IllegalType implements CodeCommEnum {
		USER("ILLEGAL_TYPE.USER", "회원"),
		ORDER("ILLEGAL_TYPE.ORDER", "주문/결제")
		;

		private final String code;
		private final String codeName;
	}

	// 부정거래탐지 - 부정거래 유형
	@Getter
	@RequiredArgsConstructor
	public enum IllegalDetailType implements CodeCommEnum {
		USER_JOIN("ILLEGAL_DETAIL_TYPE.USER_JOIN", "비정상 회원가입"),
		LOGIN_FAIL("ILLEGAL_DETAIL_TYPE.LOGIN_FAIL", "비정상 로그인"),
		STOLEN_LOST_CARD("ILLEGAL_DETAIL_TYPE.STOLEN_LOST_CARD", "도난분실카드"),
		TRANSACTION_NOT_CARD("ILLEGAL_DETAIL_TYPE.TRANSACTION_NOT_CARD", "거래불가카드"),
		ORDER_COUNT("ILLEGAL_DETAIL_TYPE.ORDER_COUNT", "비정상주문결제1.결제횟수"),
		ORDER_PRICE("ILLEGAL_DETAIL_TYPE.ORDER_PRICE", "비정상주문결제2.결제금액")
		;

		private final String code;
		private final String codeName;
	}

	// 부정거래탐지 - 진행상태
	@Getter
	@RequiredArgsConstructor
	public enum IllegalStatusType implements CodeCommEnum {
		DETECT("ILLEGAL_STATUS_TYPE.DETECT", "접수"),
		CLEAR("ILLEGAL_STATUS_TYPE.CLEAR", "정상완료"),
		ILLEGAL("ILLEGAL_STATUS_TYPE.ILLEGAL", "비정상완료")
		;

		private final String code;
		private final String codeName;
	}

	// 시스템 캐쉬 ERROR
	@Getter
	@RequiredArgsConstructor
	public enum CacheBatchError implements MessageCommEnum {
		CALL_API_FAIL("CALL_API_FAIL", "API 호출 실패"),
		CALL_API_CODE_FAIL("CALL_API_CODE_FAIL", "API 호출 응답 코드 오류"),
		CALL_API_GET_FAIL("CALL_API_GET_FAIL", "API 호출 조회 실패")
		;

		private final String code;
		private final String message;
	}

	// 시스템 모니터링
	@Getter
	@RequiredArgsConstructor
	public enum MonitoringMessage implements MessageCommEnum
	{
		ORDER("ORDER", "{주문} 장애 모니터링 배치에서 이상이 감지되었습니다.", 127L)
		,DAILY_ORDER("DAILY_ORDER", "{녹즙가맹점 주문} 장애 모니터링 배치에서 이상이 감지되었습니다.", 128L)
		,STORE_ORDER("STORE_ORDER", "{매장배송 주문} 장애 모니터링 배치에서 이상이 감지되었습니다.", 129L)
		,USER_JOIN("USER_JOIN", "{회원가입} 장애 모니터링 배치에서 이상이 감지되었습니다.", 130L)
		,MALL_LOGIN("MALL_LOGIN", "{MALL 로그인} 장애 모니터링 배치에서 이상이 감지되었습니다.", 131L)
		,BOS_LOGIN("BOS_LOGIN", "{BOS 로그인} 장애 모니터링 배치에서 이상이 감지되었습니다.", 132L)
		;

		private final String code;
		private final String message;
		private final Long batchNo;
	}

	// 시스템 모니터링 ERROR
	@Getter
	@RequiredArgsConstructor
	public enum MonitoringError implements MessageCommEnum {
		PARAM_NOT_ENOUGH("PARAM_NOT_ENOUGH", "파라미터 갯수 부족")
		;

		private final String code;
		private final String message;
	}
}