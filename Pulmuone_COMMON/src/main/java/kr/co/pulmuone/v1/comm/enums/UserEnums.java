package kr.co.pulmuone.v1.comm.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * <PRE>
 * Forbiz Korea
 * Java 에서 코드성 값을 사용해야 할때 여기에 추가해서 사용
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :   작성일                :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 *  1.0    2020. 7. 15.                jg          최초작성
 * =======================================================================
 * </PRE>
 */
public class UserEnums
{
	// 회원 알림 타입
	@Getter
	@RequiredArgsConstructor
	public enum UserNotiType implements CodeCommEnum {
		BOS_NOTI("USER_NOTI_TYPE.BOS_NOTI", "BOS공지" , "관리자 공지사항을 확인해 주세요."),
		EVENT_END("USER_NOTI_TYPE.EVENT_END", "이벤트종료" , "이벤트가 종료되었습니다. {이벤트명}"),
		EVENT_START("USER_NOTI_TYPE.EVENT_START", "이벤트시작" , "이벤트가 시작되었습니다. {이벤트명}"),
		EXHIBIT_START("USER_NOTI_TYPE.EXHIBIT_START", "기획전시작" , "기획전이 시작되었습니다. {기획전명}");

		private final String code;
		private final String codeName;
		private final String msg;
	}

	// 회원가입
	@Getter
	@RequiredArgsConstructor
	public enum Join implements MessageCommEnum
	{
		NEED_LOGIN("0001", "로그인필요")/**/
		, NICE_CHECK_FAIL("1201", "본인인증실패")/**/
		, UNDER_14_AGE_NOT_ALLOW("1202", "14세 미만은 회원가입불가")/**/
		, NOT_ANY_CERTI("1203", "본인 인증정보 없음")/**/
		, NORMAL_MEMBER_CI_DUPLICATE("1204", "정상 회원 CI 중복입니다.")/**/
		, STOP_MEMBER_CI_DUPLICATE("1205", "정지 회원 CI 중복입니다.")/**/
		, WITHDRAWAL_MEMBER_CI_DUPLICATE("1206", "탈퇴 회원 CI 중복입니다.")/**/
		, NEED_INFO_CHG_GUIDE("1207", "통합몰 전환 가입 안내 필요")/**/
		, LOGIN_FAIL("1208", "로그인 실패")/**/
		, OVER5_FAIL_PASSWORD("1209", "5회 연속 실패")/**/
		, STOP_MEMEBER("1210", "정지계정")/**/
		, SLEEP_MEMEBER("1211", "휴먼계정")/**/
		, TEMP_PASSWORD("1212", "임시비밀번호")/**/
		, CHECK_VERSION_UP_CLAUSE("1213", "계정된 약관 동의체크")/**/
		, RECAPTCHA_FAIL("1214", "캡차 인증 실패")/**/
		, NON_MEMBER_LOGIN_FAIL("1216", "비회원 로그인 실패")/**/
		, NO_FIND_USERID("1221", "아이디 찾을수 없음")/**/
		, NO_FIND_INFO_USER("1222", "사용자 정보 없음")/**/
		, SAME_PASSWORD_NOTI("1224", "이전 비밀번호 동일 사용")/**/
		, ID_PW_SAME_NOTI("1225", "비밀번호에 아이디는 사용하실 수 없습니다")/**/
		, ID_DUPLICATE("1301", "아이디 중복입니다.")/**/
		, ID_NOT_AVAILABLE("ID_NOT_AVAILABLE", "이용 불가능 아이디")/**/
		, EMAIL_DUPLICATE("1302", "이메일 중복입니다")/**/
		, NO_FIND_RECOMENDID("1303", "추천인 아이디가 존재하지 않습니다")/**/
		, ALREADY_EMPLOYEE_CERTIFI_DONE("1401", "이미 임직원인증을 완료")/**/
		, FAIL_EMPLOYEE_CERTIFI_CODE("1402", "유효하지 않은 인증코드")/**/
		, OVER5_FAIL_CERTIFI_CODE("1403", "5회 연속 실패")/**/
		, NO_FIND_EMPLOYEE("1404","임직원정보가 없음")/**/
		, NO_FIND_USER_MOVE("1501", "휴면 본인인증 정보 불일치")/**/
		, NO_SNS_SYNC("1701", "SNS 계정연동되어 있는 계정 없음")/**/
		, NO_SNS_SESSION("1702", "SNS 계정 정보 없음")/**/
		, CHECK_PASSWORD("1801", "비밀번호 확인을 해주세요") /**/
		, NO_GOODS("4401", "상품정보 없음")/**/
		, EMPLOYEE_NUMBER_DUPLICATE("EMPLOYEE_NUMBER_DUPLICATE", "사번 중복입니다.")
		, TEMP_PASSWORD_DATE_EXPIRATION("TEMP_PASSWORD_DATE_EXPIRATION","임시 비밀번호 유효시간 만료")
		, EMPTY_EMPLOYEE_MESSAGE_SEND_INFO("EMPTY_EMPLOYEE_MESSAGE_SEND_INFO","임직원회원정보에 휴대폰번호와 이메일 정보가 없습니다.<br/>고객기쁨센터에 문의 부탁드립니다.")
		, BOS_TWO_FACTOR_AUTH_NO_DATA("BOS_TWO_FACTOR_AUTH_NO_DATA","2차인증을 위한 연락처 정보가 없습니다.")
		, BOS_TWO_FACTOR_AUTH_SUCCESS("BOS_TWO_FACTOR_AUTH_SUCCESS","2차인증 성공")
		, BOS_TWO_FACTOR_AUTH_FAIL("BOS_TWO_FACTOR_AUTH_FAIL","잘못된 2차인증 코드 입니다.")
		, BOS_TWO_FACTOR_AUTH_FIVE_FAIL("BOS_TWO_FACTOR_AUTH_FIVE_FAIL","2차인증 입력이 5회 실패되어 로그인페이지로 돌아갑니다.")
		;

		private final String code;
		private final String message;
	}

	// 로그인 관리
	@Getter
	@RequiredArgsConstructor
	public enum Login implements MessageCommEnum
	{
		LOGIN_STATUS_TEMPORARY_STOP("LOGIN_STATUS_TEMPORARY_STOP", "계정이 일시정지 되어 비밀번호 재설정 페이지로 이동합니다.")/**/
		, LOGIN_STATUS_ADMINISTRATIVE_LEAVE("LOGIN_STATUS_ADMINISTRATIVE_LEAVE", "해당 계정정보는 접속이 불가능한 계정입니다.<br>관리자에게 문의 바랍니다.")/**/
		, LOGIN_PASSWORD_CHANGE("LOGIN_PASSWORD_CHANGE", "비밀번호 변경이 필요합니다")/**/
		, LOGIN_PASSWORD_NO_DATA("LOGIN_PASSWORD_NO_DATA", "일치하는 계정정보가 없습니다. 다시 확인바랍니다.")/**/
		, LOGIN_PASSWORD_CHANGE_FAIL("LOGIN_PASSWORD_CHANGE_FAIL", "이미 사용했던 비밀번호 입니다. 새로운 비밀번호를 입력해주세요.")/**/
		, LOGIN_ANOTHER_ROUTE("LOGIN_ANOTHER_ROUTE", "다른 PC에서 로그인한 사용자가 있어 자동로그아웃 처리됩니다.")/**/
		, LOGIN_NO_DATA_FAIL("LOGIN_NO_DATA_FAIL", "아이디 또는 비밀번호가 일치하지 않습니다.")/**/
		, LOGIN_TEMPORARY_PASSWORD("LOGIN_TEMPORARY_PASSWORD", "임시 비밀번호입니다.")/**/
		, LOGIN_NO_DATA("LOGIN_NO_DATA", "로그인 정보가 없습니다.")/**/
		, LOGIN_BOS_TWO_FACTOR_AUTH_FAIL("LOGIN_BOS_TWO_FACTOR_AUTH_FAIL", "2차 인증 진행이 실패하였습니다.")/**/
		;

		private final String code;
		private final String message;
	}

	// 사용자 그룹 관리
	@Getter
	@RequiredArgsConstructor
	public enum UserGroup implements MessageCommEnum
	{
		USER_GROUP_DUP_GROUP("USER_GROUP_DUP_GROUP", "동일한 등급명이 존재합니다"),
		USER_GROUP_DUP_LEVEL("USER_GROUP_DUP_LEVEL", "회원레벨은 중복 선택할 수<BR>없습니다. 다른 레벨을 선택<BR>해 주세요."),
		USER_GROUP_DUP_NAME("USER_GROUP_DUP_NAME", "동일한 그룹명이 존재합니다"),
		USER_GROUP_DUP_DATE("USER_GROUP_DUP_DATE", "적용 시작일이 동일한 그룹정보가 존재합니다");
		;

		private final String code;
		private final String message;
	}

	/* HGRM-2017 : dgyoun : 신규 추가 */
	// 시스템설정
	@Getter
	@RequiredArgsConstructor
	public enum SystemSetting implements MessageCommEnum
	{
	  SYSTEM_SETTING_DUPLICATE_SEQ ("SYSTEM_SETTING_DUPLICATE_SEQ", "등록된 Seq가 존재합니다."),
	  SYSTEM_SETTING_DUPLICATE_WORD("SYSTEM_SETTING_DUPLICATE_WORD", "동일한 표준 용어 등록 불가 합니다.")
	  ;

	  private final String code;
	  private final String message;
	}

	// 상품가격
	@Getter
	@RequiredArgsConstructor
	public enum GoodsPrice implements MessageCommEnum
	{
	    GOODS_PRICE_SUCCESS            ("0000", "정상처리 되었습니다.")
	  , GOODS_PRICE_ITEM_NO_EXIST      ("0011", "대상 품목이 존재하지 않습니다.")
	  , GOODS_PRICE_GOODS_NO_EXIST     ("0002", "대상 상품이 존재하지 않습니다.")
	  , GOODS_PRICE_GOODS_LIST_NO_EXIST("0003", "대상 상품 목록이 존재하지 않습니다.")
	  , GOODS_PRICE_PERIOD_NO_EXIST    ("0004", "대상 가격구간이 없습니다.")
	  ;

	  private final String code;
	  private final String message;
	}

	// 회원 코드 타입
	@Getter
	@RequiredArgsConstructor
	public enum StatusCode implements CodeCommEnum
	{
		TEMPORARY_STOP("EMPLOYEE_STATUS.TEMPORARY_STOP", "일시정지")/**/
		, NORMAL("EMPLOYEE_STATUS.NORMAL", "정상")/**/
		, STOP("EMPLOYEE_STATUS.STOP", "정지")/**/
		, ADMINISTRATIVE_LEAVE("EMPLOYEE_STATUS.ADMINISTRATIVE_LEAVE", "휴직")/**/
		;

		private final String code;
		private final String codeName;
	}

	// 구매자 회원 코드 타입
	@Getter
	@RequiredArgsConstructor
	public enum BuyerStatusCode implements CodeCommEnum
	{
		BASIC("BASIC", "정상") // 1204
		, STOP("STOP", "정지") // 1205
		, MOVE("MOVE", "휴면") //
		, DROP("DROP", "탈퇴") // 1206
		;

		private final String code;
		private final String codeName;
	}

	// 설정값 타입
	@Getter
	@RequiredArgsConstructor
	public enum BosLoginType implements CodeCommEnum
	{
		BOS_UR_LOGIN_CONCURRENCY_YN("BOS_UR_LOGIN_CONCURRENCY_YN", "BOS 동시 로그인 가능여부"),
		BOS_UR_LOGIN_CONCURRENCY_N("N", "불가능"),
		BOS_UR_LOGIN_CONCURRENCY_Y("Y", "가능");
		;

		private final String code;
		private final String codeName;
	}

	// 회원기본정보 - 회원상태
	@Getter
	@RequiredArgsConstructor
	public enum UserStatus implements CodeCommEnum {
		ACTIVITY_POSSIBLE("1", "활동가능"),
		INACTIVITY("0", "활동불가");

		private final String code;
		private final String codeName;
	}

	// 회원기본정보 - 회원구분
	@Getter
	@RequiredArgsConstructor
	public enum UserType implements CodeCommEnum {
		BUYER("USER_TYPE.BUYER", "일반"),
		EMPLOYEE("USER_TYPE.EMPLOYEE", "관리자");

		private final String code;
		private final String codeName;
	}

	// 회원기본정보 - 구매자구분
	@Getter
	@RequiredArgsConstructor
	public enum BuyerType implements CodeCommEnum {
		USER("BUYER_TYPE.USER", "회원"),
		EMPLOYEE("BUYER_TYPE.EMPLOYEE", "임직원"),
		EMPLOYEE_BASIC("BUYER_TYPE.EMPLOYEE_BASIC", "임직원(회원가)"),
		GUEST("BUYER_TYPE.GUEST", "비회원");

		private final String code;
		private final String codeName;
	}

	// 회원기본정보 - 회원구분
	@Getter
	@RequiredArgsConstructor
	public enum EmployeeStatus implements CodeCommEnum {
		NORMAL("EMPLOYEE_STATUS.NORMAL", "정상"),
		STOP("EMPLOYEE_STATUS.STOP", "정지"),
		RESIGN("EMPLOYEE_STATUS.RESIGN", "퇴사"),
		TEMPORARY_STOP("EMPLOYEE_STATUS.TEMPORARY_STOP", "일시정지"),
		ADMINISTRATIVE_LEAVE("EMPLOYEE_STATUS.ADMINISTRATIVE_LEAVE", "휴직");

		private final String code;
		private final String codeName;
	}

	// 회원기본정보 - 회원구분
	@Getter
	@RequiredArgsConstructor
	public enum DatabaseAction implements CodeCommEnum {
		INSERT("C", "등록"),
		DELETE("D", "삭제"),
		UPDATE("U", "수정");

		private final String code;
		private final String codeName;
	}

	// 공통코드 회원변경타입
	@Getter
	@RequiredArgsConstructor
	public enum StcomnCodeUrMoveType implements CodeCommEnum {
		TYPE_1("UR_MOVE_TYPE.TYPE_1", "탈퇴사유");

		private final String code;
		private final String codeName;
	}

    @Getter
    @RequiredArgsConstructor
    public enum Buyer implements MessageCommEnum {
        NEED_LOGIN("NEED_LOGIN", "로그인필요"),
		RECAPTCHA_FAIL("RECAPTCHA_FAIL", "캡차 인증 실패");

        private final String code;
        private final String message;
    }

	//회원 등급
	@Getter
	@RequiredArgsConstructor
	public enum UserGradeType implements CodeCommEnum {
		NORMAL("USER_GRADE.NORMAL", "일반"),
		BEST("USER_GRADE.BEST", "우수");

		private final String code;
		private final String codeName;
	}


	// 회원그룹 등급아이콘 파일 타입
	@Getter
	@RequiredArgsConstructor
	public enum UserGroupImageType implements CodeCommEnum {
		TOP_IMAGE_FILE("topImageFile", "상단 등급아이콘"),
		LIST_IMAGE_FILE("listImageFile", "리스트 등급아이콘");

		private final String code;
		private final String codeName;
	}

	// 회원그룹 레벨 유형
	@Getter
	@RequiredArgsConstructor
	public enum GroupLevelType implements CodeCommEnum {
		LEVEL01("GROUP_LEVEL_TP.LEVEL01", "LEVEL01"),
		LEVEL02("GROUP_LEVEL_TP.LEVEL02", "LEVEL02"),
		LEVEL03("GROUP_LEVEL_TP.LEVEL03", "LEVEL03"),
		LEVEL04("GROUP_LEVEL_TP.LEVEL04", "LEVEL04"),
		LEVEL05("GROUP_LEVEL_TP.LEVEL05", "LEVEL05"),
		LEVEL06("GROUP_LEVEL_TP.LEVEL06", "LEVEL06"),
		LEVEL07("GROUP_LEVEL_TP.LEVEL07", "LEVEL07"),
		LEVEL08("GROUP_LEVEL_TP.LEVEL08", "LEVEL08"),
		LEVEL09("GROUP_LEVEL_TP.LEVEL09", "LEVEL09"),
		LEVEL10("GROUP_LEVEL_TP.LEVEL10", "LEVEL10")
		;

		private final String code;
		private final String codeName;
	}

	// 회원그룹 혜택 유형
	@Getter
	@RequiredArgsConstructor
	public enum UserGroupBenefitType implements CodeCommEnum {
		COUPON("UR_GROUP_BENEFIT_TP.COUPON", "쿠폰"),
		POINT("UR_GROUP_BENEFIT_TP.POINT", "적립금");

		private final String code;
		private final String codeName;
	}

	// 회원그룹 배치 대상 유형
	@Getter
	@RequiredArgsConstructor
	public enum UserGroupBatchTargetType implements CodeCommEnum {
		NORMAL("BATCH_USER_TYPE.BUYER", "일반회원"),
		MOVE("BATCH_USER_TYPE.MOVE", "휴면회원");

		private final String code;
		private final String codeName;
	}

	// 카카오 계정 연결끊기 요청 경로
	@Getter
	@RequiredArgsConstructor
	public enum KakaoUnlinkType implements CodeCommEnum {
		ACCOUNT_DELETE("ACCOUNT_DELETE", "카카오계정 탈퇴"),
		UNLINK_FROM_ADMIN("UNLINK_FROM_ADMIN", "카카오 관리자로 인한 탈퇴 처리"),
		UNLINK_FROM_APPS("UNLINK_FROM_APPS", "카카오계정 페이지를 통한 서비스 연결 끊기");

		private final String code;
		private final String codeName;
	}

	// 전시관리
  @Getter
  @RequiredArgsConstructor
  public enum DisplayManage implements MessageCommEnum
  {
      DISPLAY_MANAGE_SUCCESS                            ("0000", "정상처리 되었습니다.")
    , DISPLAY_MANAGE_PAGE_PARAM_JSON                    ("1000", "페이지 문자열정보 생성 오류입니다.")
    , DISPLAY_MANAGE_PAGE_PARAM_NO_INPUT                ("1001", "페이지 입력정보가 존재하지 않습니다.")
    , DISPLAY_MANAGE_PAGE_PARAM_NO_PAGE_ID              ("1002", "페이지 ID가 존재하지 않습니다.")
    , DISPLAY_MANAGE_PAGE_PARAM_NO_DEPTH                ("1003", "카테고리 DEPTH가 존재하지 않습니다.")
    , DISPLAY_MANAGE_PAGE_PARAM_NO_SORT_TARGET          ("1004", "페이지 순서변경 대상 목록이 존재하지 않습니다.")
    , DISPLAY_MANAGE_PAGE_PARAM_NO_REG_TARGET           ("1005", "페이지 등록 대상정보가 존재하지 않습니다.")
    , DISPLAY_MANAGE_PAGE_NO_EXIST                      ("1111", "대상  페이지가 존재하지 않습니다.")
    , DISPLAY_MANAGE_PAGE_LIST_NO_EXIST                 ("1112", "대상 페이지 목록이 존재하지 않습니다.")
    , DISPLAY_MANAGE_PAGE_FAIL_PUT                      ("1121", "페이지 정보 수정 처리에 실패하였습니다.")
    , DISPLAY_MANAGE_PAGE_FAIL_PUT_NO_PROC              ("1122", "페이지 정보 수정 처리건이 없습니다.")
    , DISPLAY_MANAGE_PAGE_FAIL_PUT_SORT                 ("1131", "페이지 순번 변경 처리에 실패하였습니다.")
    , DISPLAY_MANAGE_PAGE_FAIL_PUT_SORT_NO_PROC         ("1132", "페이지 순번 변경 중 실패하였습니다.")
    , DISPLAY_MANAGE_PAGE_FAIL_PUT_SORT_NO_TARGET       ("1133", "페이지 순번 변경 대상이 없습니다.")
    , DISPLAY_MANAGE_PAGE_FAIL_DEL                      ("1141", "페이지 삭제 처리에 실패하였습니다.")
    , DISPLAY_MANAGE_PAGE_FAIL_DEL_NO_PROC              ("1142", "페이지 삭제 처리 중 실패하였습니다.")
    , DISPLAY_MANAGE_PAGE_FAIL_DEL_NO_TARGET            ("1143", "페이지 삭제 대상이 없습니다.")
    , DISPLAY_MANAGE_PAGE_FAIL_ADD                      ("1151", "페이지 등록 처리에 실패하였습니다.")
    , DISPLAY_MANAGE_PAGE_FAIL_ADD_NO_PROC              ("1152", "페이지 등록 중 실패하였습니다.")
    , DISPLAY_MANAGE_PAGE_FAIL_ADD_AFTER_NO_PROC        ("1153", "페이지 등록 후처리 중 실패하였습니다.")
    , DISPLAY_MANAGE_PAGE_FAIL_DUP_PAGE_CD              ("1154", "페이지코드가 이미 존재합니다.")
    , DISPLAY_MANAGE_INVENTORY_PARAM_JSON               ("2000", "인벤토리 문자열정보 생성 오류입니다.")
    , DISPLAY_MANAGE_INVENTORY_PARAM_NO_INPUT           ("2001", "인벤토리 입력정보가 존재하지 않습니다.")
    , DISPLAY_MANAGE_INVENTORY_PARAM_NO_INVENTORY_ID    ("2002", "인벤토리 ID가 존재하지 않습니다.")
    , DISPLAY_MANAGE_INVENTORY_PARAM_NO_PAGE_TP         ("2003", "인벤토리 페이지유형이 존재하지 않습니다.")
    , DISPLAY_MANAGE_INVENTORY_PARAM_NO_SORT_TARGET     ("2004", "인벤토리 순서변경 대상 목록이 존재하지 않습니다.")
    , DISPLAY_MANAGE_INVENTORY_PARAM_NO_REG_TARGET      ("2004", "인벤토리 등록 대상정보가 존재하지 않습니다.")
    , DISPLAY_MANAGE_INVENTORY_NO_EXIST                 ("2111", "대상 인벤토리가 존재하지 않습니다.")
    , DISPLAY_MANAGE_INVENTORY_LIST_NO_EXIST            ("2112", "대상 인벤토리 목록이 존재하지 않습니다.")
    , DISPLAY_MANAGE_INVENTORY_FAIL_PUT                 ("2121", "인벤토리 정보 수정 처리에 실패하였습니다.")
    , DISPLAY_MANAGE_INVENTORY_FAIL_PUT_NO_PROC         ("2122", "인벤토리 정보 수정 처리건이 없습니다.")
    , DISPLAY_MANAGE_INVENTORY_FAIL_PUT_SORT            ("2131", "인벤토리 순번 변경 처리에 실패하였습니다.")
    , DISPLAY_MANAGE_INVENTORY_FAIL_PUT_SORT_NO_PROC    ("2132", "인벤토리 순번 변경 중 실패하였습니다.")
    , DISPLAY_MANAGE_INVENTORY_FAIL_PUT_SORT_NO_TARGET  ("2133", "인벤토리 순번 변경 대상이 없습니다.")
    , DISPLAY_MANAGE_INVENTORY_FAIL_DEL                 ("2141", "인벤토리 삭제 처리에 실패하였습니다.")
    , DISPLAY_MANAGE_INVENTORY_FAIL_DEL_NO_PROC         ("2142", "인벤토리 삭제 처리 중 실패하였습니다.")
    , DISPLAY_MANAGE_INVENTORY_FAIL_DEL_NO_TARGET       ("2143", "인벤토리 삭제 대상이 없습니다.")
    , DISPLAY_MANAGE_INVENTORY_FAIL_ADD                 ("2151", "인벤토리 등록 처리에 실패하였습니다.")
    , DISPLAY_MANAGE_INVENTORY_FAIL_ADD_NO_PROC         ("2152", "인벤토리 등록 중 실패하였습니다.")
    , DISPLAY_MANAGE_INVENTORY_FAIL_DUP_INVENTORY_CD    ("2153", "인벤토리코드가 이미 존재합니다.")
    , DISPLAY_MANAGE_CONTS_PARAM_JSON                   ("3000", "컨텑츠 문자열정보 생성 오류입니다.")
    , DISPLAY_MANAGE_CONTS_PARAM_NO_INPUT               ("3001", "컨텐츠 입력정보가 존재하지 않습니다.")
    , DISPLAY_MANAGE_CONTS_PARAM_NO_CONTS_ID            ("3002", "컨텐츠 ID가 존재하지 않습니다.")
    , DISPLAY_MANAGE_CONTS_PARAM_NO_INVENTORY_ID        ("3003", "컨텐츠 인벤토리ID가  존재하지 않습니다.")
    , DISPLAY_MANAGE_CONTS_PARAM_NO_PRNTS_CONTS_ID      ("3004", "컨텐츠 상위 컨텐츠ID가  존재하지 않습니다.")
    , DISPLAY_MANAGE_CONTS_PARAM_NO_SORT_TARGET         ("3005", "컨텐츠 순서변경 대상 목록이 존재하지 않습니다.")
    , DISPLAY_MANAGE_CONTS_PARAM_NO_REG_TARGET          ("3006", "컨텐츠 등록 대상정보가 존재하지 않습니다.")
    , DISPLAY_MANAGE_CONTS_NO_EXIST                     ("3111", "대상 컨텐츠가 존재하지 않습니다.")
    , DISPLAY_MANAGE_CONTS_LIST_NO_EXIST                ("3112", "대상 컨텐츠 목록이 존재하지 않습니다.")
    , DISPLAY_MANAGE_CONTS_FAIL_PUT                     ("3121", "컨텐츠 정보 수정 처리에 실패하였습니다.")
    , DISPLAY_MANAGE_CONTS_FAIL_PUT_NO_PROC             ("3122", "컨텐츠 정보 수정 처리건이 없습니다.")
    , DISPLAY_MANAGE_CONTS_FAIL_PUT_SORT                ("3131", "컨텐츠 순번 변경 처리에 실패하였습니다.")
    , DISPLAY_MANAGE_CONTS_FAIL_PUT_SORT_NO_PROC        ("3132", "컨텐츠 순번 변경 중 실패하였습니다.")
    , DISPLAY_MANAGE_CONTS_FAIL_PUT_SORT_NO_TARGET      ("3133", "컨텐츠 순번 변경 대상이 없습니다.")
    , DISPLAY_MANAGE_CONTS_FAIL_DEL                     ("3141", "컨텐츠 삭제 처리에 실패하였습니다.")
    , DISPLAY_MANAGE_CONTS_FAIL_DEL_NO_PROC             ("3142", "컨텐츠 삭제 처리 중 실패하였습니다.")
    , DISPLAY_MANAGE_CONTS_FAIL_DEL_NO_TARGET           ("3143", "컨텐츠 삭제 대상이 없습니다.")
    , DISPLAY_MANAGE_CONTS_FAIL_ADD                     ("3151", "컨텐츠 등록 처리에 실패하였습니다.")
    , DISPLAY_MANAGE_CONTS_FAIL_ADD_NO_PROC             ("3152", "컨텐츠 등록 중 실패하였습니다.")
    , DISPLAY_MANAGE_CONTS_FAIL_ADD_AFTER_NO_PROC       ("3153", "컨텐츠 등록 후처리 중 실패하였습니다.")
    , DISPLAY_MANAGE_CONTS_BRAND_LIST_NO_EXIST          ("3201", "브랜드 목록이 존재하지 않습니다.")
    , DISPLAY_MANAGE_CONTS_GOODS_LIST_NO_EXIST          ("3301", "상품 목록이 존재하지 않습니다.")
    , DISPLAY_MANAGE_GRP_PARAM_JSON                     ("4000", "인벤토리그룹 문자열정보 생성 오류입니다.")
    , DISPLAY_MANAGE_GRP_PARAM_NO_INPUT                 ("4001", "인벤토리그룹 입력정보가 존재하지 않습니다.")
    , DISPLAY_MANAGE_GRP_PARAM_NO_GRP_ID                ("4002", "인벤토리그룹 ID가 존재하지 않습니다.")
    , DISPLAY_MANAGE_GRP_PARAM_NO_DEPTH                 ("4003", "카테고리 DEPTH가 존재하지 않습니다.")
    , DISPLAY_MANAGE_GRP_PARAM_NO_SORT_TARGET           ("4004", "인벤토리그룹 순서변경 대상 목록이 존재하지 않습니다.")
    , DISPLAY_MANAGE_GRP_PARAM_NO_REG_TARGET            ("4005", "인벤토리그룹 등록 대상정보가 존재하지 않습니다.")
    , DISPLAY_MANAGE_GRP_NO_USER_INFO                   ("4006", "사용자정보가 없습니다.")
    , DISPLAY_MANAGE_GRP_INVENTORY_CD_PAGE_ERROR        ("4007", "존재하지 않거나 비정상 코드정보가 존재합니다. 인벤토리 코드정보를 확인해주세요.")
    , DISPLAY_MANAGE_GRP_INVENTORY_CD_CATEGORY_ERROR    ("4008", "유효하지 않는 카테고리코너의 인벤토리코드가 존재합니다. 인벤토리코드를 확인하세요.")
    , DISPLAY_MANAGE_GRP_INVENTORY_CD_ERROR             ("4009", "유효한 인벤토리코드가 존재하지 않습니다. 인벤토리코드를 확인하세요.")
    , DISPLAY_MANAGE_GRP_NO_EXIST                       ("4111", "대상 인벤토리그룹이 존재하지 않습니다.")
    , DISPLAY_MANAGE_GRP_LIST_NO_EXIST                  ("4112", "대상 인벤토리그룹 목록이 존재하지 않습니다.")
    , DISPLAY_MANAGE_GRP_LIST_NO_INVENTORY_EXIST        ("4113", "대상 인벤토리그룹의 인벤토리 목록이 존재하지 않습니다.")
    , DISPLAY_MANAGE_GRP_FAIL_PUT                       ("4121", "인벤토리그룹 정보 수정 처리에 실패하였습니다.")
    , DISPLAY_MANAGE_GRP_FAIL_PUT_NO_PROC               ("4122", "인벤토리그룹 정보 수정 처리건이 없습니다.")
    , DISPLAY_MANAGE_GRP_FAIL_PUT_SORT                  ("4131", "인벤토리그룹 순번 변경 처리에 실패하였습니다.")
    , DISPLAY_MANAGE_GRP_FAIL_PUT_SORT_NO_PROC          ("4132", "인벤토리그룹 순번 변경 중 실패하였습니다.")
    , DISPLAY_MANAGE_GRP_FAIL_PUT_SORT_NO_TARGET        ("4133", "인벤토리그룹 순번 변경 대상이 없습니다.")
    , DISPLAY_MANAGE_GRP_FAIL_DEL                       ("4141", "인벤토리그룹 삭제 처리에 실패하였습니다.")
    , DISPLAY_MANAGE_GRP_FAIL_DEL_NO_PROC               ("4142", "인벤토리그룹 삭제 처리 중 실패하였습니다.")
    , DISPLAY_MANAGE_GRP_FAIL_DEL_NO_TARGET             ("4143", "인벤토리그룹 삭제 대상이 없습니다.")
    , DISPLAY_MANAGE_GRP_FAIL_ADD                       ("4151", "인벤토리그룹 등록 처리에 실패하였습니다.")
    , DISPLAY_MANAGE_GRP_FAIL_ADD_NO_PROC               ("4152", "인벤토리그룹 등록 중 실패하였습니다.")
    ;

    private final String code;
    private final String message;
  }


  //후기 추천
  @Getter
  @RequiredArgsConstructor
  public enum Feedback implements MessageCommEnum {
       ALREADY_BEST("ALREADY_BEST", "기존에 추천한 건"),
      CANCEL_BEST("CANCEL_BEST", "이미 추천 취소된 건")
      ;
      private final String code;
      private final String message;
  }

  //회원접속로그 생성위한 로그인 성공 여부
  @Getter
  @RequiredArgsConstructor
  public enum LoginSuccessStatus implements MessageCommEnum {
      SUCCESS("Y", "성공"),
      FAIL("N", "실패")
      ;
      private final String code;
      private final String message;
  }

  //기존회원 가입시 아이디/비밀번호 체크 API 결과
  @Getter
  @RequiredArgsConstructor
  public enum AsisUserInfoCheckResult implements MessageCommEnum {
	  NO_FIND_INFO_USER("0", "없음"),
      SUCCESS("1", "존재"),
      ERROR("9","오류")
      ;
      private final String code;
      private final String message;
  }

	//유저 타입
	@Getter
	@RequiredArgsConstructor
	public enum UserStatusType implements CodeCommEnum {
		NONMEMBER("NONMEMBER", "비회원"),
		MEMBER("MEMBER", "회원"),
		EMPLOYEE("EMPLOYEE", "임직원");

		private final String code;
		private final String codeName;
	}

	// 회원탈퇴
	@Getter
	@RequiredArgsConstructor
	public enum Drop implements MessageCommEnum {
		ORDER_EXIST("ORDER_EXIST","주문정보 존재")
		;

		private final String code;
		private final String message;
	}

	// ERP 회원상태(ERP 에서 상태값이 변경될 가망성 존재)
	@Getter
	@RequiredArgsConstructor
	public enum ErpEmployeeStatus implements CodeCommEnum {
		NORMAL1("EMPLOYEE_STATUS.NORMAL", "Active Assignment"),
		NORMAL2("EMPLOYEE_STATUS.NORMAL", "현재 발령"),
		RESIGN1("EMPLOYEE_STATUS.RESIGN", "발령 보류"),
		RESIGN2("EMPLOYEE_STATUS.RESIGN", "발령 종료"),
		RESIGN3("EMPLOYEE_STATUS.RESIGN", "종료"),
		RESIGN4("EMPLOYEE_STATUS.RESIGN", "Terminate Assignment"),
		ADMINISTRATIVE_LEAVE1("EMPLOYEE_STATUS.ADMINISTRATIVE_LEAVE", "무급휴직"),
		ADMINISTRATIVE_LEAVE2("EMPLOYEE_STATUS.ADMINISTRATIVE_LEAVE", "유급휴직");

		private final String code;
		private final String codeName;
	}

	// BUYER - 상태
	@Getter
	@RequiredArgsConstructor
	public enum BuyerStatus implements CodeCommEnum {
		NORMAL("BUYER_STATUS.NORMAL", "정상" , "정상회원"),
		STOP("BUYER_STATUS.STOP", "정지" , "정지회원");

		private final String code;
		private final String codeName;
		private final String type;
	}

	// 계좌인
	@Getter
	@RequiredArgsConstructor
	public enum ValidationBankAccountReturnType implements MessageCommEnum {
		NO_VALIDATION_BANK_ACCOUNT("NO_VALIDATION_BANK_ACCOUNT","은행계좌 검증 실패")
		;

		private final String code;
		private final String message;
	}

	// BOS에서 PW재발송할 때 사용자 타입
	@Getter
	@RequiredArgsConstructor
	public enum PasswordClearUserType implements CodeCommEnum {
		BUYER("BUYER", "구매자" ),
		EMPLOYEE("EMPLOYEE", "임직원");

		private final String code;
		private final String codeName;
	}

	//MALL - Asis 풀무원 적립금 처리
	@Getter
	@RequiredArgsConstructor
	public enum AsisPoint implements MessageCommEnum {
		ASIS_OVER_POINT("ASIS_OVER_POINT", "기존 적립금 보다 초과한 적립금" ),
		ASIS_POINT_ZERO("ASIS_POINT_ZERO", "기존 적립금 없음" ),
		ASIS_API_ERROR("API_ERROR", "기존 적립금 소멸 이상" ),
		DEPOSIT_POINT_EXCEEDED("DEPOSIT_POINT_EXCEEDED", "적립 금액 초과" ),
		DEPOSIT_POINT_ERROR("DEPOSIT_POINT_ERROR", "적립금 적립 오류" ),
		PARTIAL_DEPOSIT_OVER_LIMIT("PARTIAL_DEPOSIT_OVER_LIMIT", "보유 한도초과로 부분적립 되었습니다.")
		;

		private final String code;
		private final String message;
	}

	// 배치에서 로그인 정보 입력 할 때
	@Getter
	@RequiredArgsConstructor
	public enum BatchUserInfo implements CodeCommEnum {
		USERINFO("000000", "배치"),
		;
		private final String code;
		private final String codeName;
	}

	//주문 배송지 변경
	@Getter
	@RequiredArgsConstructor
	public enum ChangeDeliveryAddress implements MessageCommEnum {
		NEED_ADDITIONAL_SHIPPING_PRICE("NEED_ADDITIONAL_SHIPPING_PRICE", "추가배송비 결제가 필요한 배송지로 배송지 변경이 불가합니다. </br>주문취소 후 재결제해주세요." ),
		PAID_ADDITIONAL_SHIPPING_PRICE("PAID_ADDITIONAL_SHIPPING_PRICE", "추가배송비가 결제된 주문건으로 배송지 변경이 불가합니다. </br>주문취소 후 재결제해주세요." ),
		EXIST_DELIVERY_NOT_POSSIBLE_GOODS("EXIST_DELIVERY_NOT_POSSIBLE_GOODS", "해당 배송지에 배송불가 상품이 있어 배송지 변경이 불가합니다. </br>고객센터로 문의해주세요." ),
		DIFFERENT_DELIVERY_TYPE("DIFFERENT_DELIVERY_TYPE", "선택하신 배송지는 기존 배송유형과 상이하여 배송지 변경이 불가합니다. </br>주문취소 후 재결제해주세요." ),
		EXIST_DELIVERY_NOT_POSSIBLE_GOODS_BOS("EXIST_DELIVERY_NOT_POSSIBLE_GOODS_BOS", "해당 배송지에 배송불가 상품으로 배송지 변경이 불가능합니다." ),
		SUCCESS_CHANGE_DELIVERY_ADDRESS("SUCCESS_CHANGE_DELIVERY_ADDRESS", "변경되었습니다." ),
		CHANGE_DAWN_TO_NORMAL("CHANGE_DAWN_TO_NORMAL", "택배 배송으로 변경 됩니다." ),
		WAREHOUSE_UNDELIVERABLE_AREA("WAREHOUSE_UNDELIVERABLE_AREA", "고객님의 배송지는 택배사 사정으로 배송이 불가능합니다. 다른배송지를 선택해주세요." ),
		;

		private final String code;
		private final String message;
	}

	// 메타 태그 모음
	@Getter
	@RequiredArgsConstructor
	public enum MetaType implements CodeCommEnum {
		MAIN("MAIN", "메인_메인", "로하스 프레시 마켓, #풀무원", "풀무원,신선식품,녹즙, 산지직송,일일배달, 친환경,로하스,새벽배송,극신선배달,풀무원식품,간편식,HMR,가정간편식,핫도그,만두,두부,콩나물", "풀무원 공식 온라인샵! 바른먹거리를 만나는 가장 신선한 방법, 새벽배송으로 내일 아침 문앞에서 만나요")
		,ORGA("ORGA", "메인_올가", "엄선한 친환경 먹거리 전문, 올가홀푸드", "올가,올가홀푸드,ORGA,로하스,LOHAS,유기농,동물복지,무항생제,무농약,친환경,농산물,저탄소, GAP인증, ASC인증, 제로웨이스트,산지직송,제철식품,풀비타,올가맘,풀무원", "풀무원 공식 온라인샵! LOHAS Fresh Market 올가홀푸드, 꼼꼼하게 엄선한 친환경 먹거리")
		,EATSLIM("EATSLIM", "메인_잇슬림", "칼로리 조절에 최적화된 영양설계 도시락, 잇슬림", "잇슬림,도시락배달,칼로리조절,칼로리조절식단,도시락,칼로리식단,식단배송,풀무원, 맞춤식단, 식단관리,밸런스도시락", "풀무원 공식 온라인샵! 칼로리 조절에 최적화된 영양설계 도시락, 매일아침 무료배송, 일일배달 도시락")
		,BABYMEAL("BABYMEAL", "메인_베이비밀", "유기농 홈메이드 이유식, 베이비밀", "베이비밀,맞춤이유식,이유식배달,이유식,아기식단,풀무원,이유식 무료체험", "풀무원 공식 온라인샵! 우리아이를 위한 유기농 홈메이드 이유식 배달, 월령별 맞춤 이유식 체험단 신청!")
		,SHOP_GOODSLIST("SHOP_GOODSLIST", "공통_상품목록", "{text}카테고리 - #풀무원", "", "상품목록_{text},#풀무원 ")
		,SHOP_GOODSVIEW("SHOP_GOODSVIEW", "공통_상품상세", "#풀무원", "", "{text}")
		,EVENTS("EVENTS", "공통_기획전/이벤트 목록", "기획전&이벤트 - #풀무원", "풀무원,풀무원행사,특가행사,구매혜택,{text}", "#풀무원의 다양한 기획상품과 경품혜택을 만나볼 수 있는 이벤트를 모았습니다.")
		,EVENTS_EXHIBIT("EVENTS_EXHIBIT", "공통_기획전 상세", "{text}", "", "진행기간:{text}")
		,EVENTS_EVENT("EVENTS_EVENT", "공통_이벤트 상세", "{text}", "", "진행기간:{text}")
		,USER_JOIN("USER_JOIN", "공통_회원가입", "회원가입 - #풀무원", "풀무원,신규회원,회원가입,신규혜택,가입혜택,100원딜,가입이벤트,첫구매, 가입", "지금 가입하면 신규회원 전용 혜택과 인기상품 100원 구매기회 까지!")
		,LOHAS("LOHAS", "통합몰코너_로하스관", "로하스관 - #풀무원", "LOHAS,로하스상품,풀무원,{text},유기농,친환경,무농약,무항생제,동물복지,ASC인증, GAP인증, 제로웨이스트, 저탄소", "지속가능한 건강한 라이프스타일! 풀무원 로하스상품의 모든 것, 풀무원 로하스관,{text}")
		,BEST("BEST", "통합몰코너_베스트", "잘나가요,이 상품 - #풀무원", "풀무원,{text}", "#풀무원에서 제일 잘 팔리는 카테고리별 베스트 상품을 소개합니다.")
		,KEYWORD("KEYWORD", "통합몰코너_키워드베스트", "요즘, 많이 찾는 상품 - #풀무원", "테마상품,풀무원,{text}", "테마별 베스트 상품을 소개합니다.{text}")
		,NEW("NEW", "통합몰코너_신제품", "NEW, 제일 먼저 만나요 - #풀무원", "신제품,신상품,풀무원,풀무원신제품, 풀무원신상품", "풀무원이 자신있게 소개하는 신상품을 만나보세요. ")
		,SALE("SALE", "통합몰코너_지금세일", "할인상품만 모아, 지금 세일 - #풀무원", "할인상품,특가상품,특가할인,특가세일, 풀무원, 40%이상,40%~20%, 20%~10%,10%미만", "#풀무원의 할인상품만 모았습니다. 40%이상,40%~20%, 20%~10%,10%미만")
		,PICKING("PICKING", "통합몰코너_균일가골라담기", "골라담아,묶음할인 - #풀무원", "균일가,할인,세일,골라담기,행사상품,균일가행사,풀무원,{text}", "#풀무원에서만 만날 수 있는 내맘대로 골라담는 균일가!")
		,PICKING_VIEW("PICKING_VIEW", "통합몰코너_균일가골라담기_상세", "{text} 골라담아,묶음할인 - #풀무원", "균일가,할인,세일,골라담기,행사상품,균일가행사,풀무원,{text}", "#풀무원에서만 만날 수 있는 내맘대로 골라담는 균일가!")
		,FREE_ORDER("FREE_ORDER", "통합몰코너_녹즙내맘대로주문", "녹즙,내맘대로 주문 - #풀무원", "녹즙주문,녹즙신청,녹즙프로그램,풀무원", "원하는 녹즙/날짜 고르기만 하세요. 아침마다 신선배송!")
		,DAILY_SHIPPING("DAILY_SHIPPING", "통합몰코너_일일배송", "아침마다 신선배송, 일일배송 - #풀무원", "일일배달,일일배송,풀무원", "풀무원 녹즙/도시락/이유식을 아침마다 신선배송! 원하는 상품/날짜 고르기만 하세요.")
		,SHIPPING_GUIDE("SHIPPING_GUIDE", "통합몰코너_배송안내", "배송안내 - #풀무원", "새벽배송,정기배송,일일배송,매장배송,매장픽업,매장정보,풀무원", "#풀무원 배송안내. #풀무원이 제공하는 다양한 배송서비스를 한 눈에 확인하세요.")
		,BRAND("BRAND", "통합몰코너_브랜드", "브랜드 - #풀무원", "풀무원,{text}", "브랜드_{text}, #풀무원의 다양한 브랜드를 만나보세요!")
		,NEW_MEMBER_BENEFIT("NEW_MEMBER_BENEFIT", "통합몰코너_신규가입혜택", "신규가입혜택 - #풀무원", "풀무원,신규회원,회원가입,신규혜택,가입혜택,100원딜,가입이벤트,첫구매, 가입", "지금 가입하면 신규회원 전용 혜택과 인기상품 100원 구매기회 까지!")
		,HOT_ITEM("HOT_ITEM", "올가_인기상품", "잘 나가요, 이 상품 - 올가", "풀무원,{text}", "올가에서 가장 잘 팔리는 카테고리별 베스트 상품을 소개합니다.")
		,FLYER_ITEM("FLYER_ITEM", "올가_전단행사", "놓치기 아쉬운 전단행사 - 올가", "올가,매장전단행사,전단행사,전단세일,매장행사", "매장 할인상품을 온라인에서도 그대로!")
		,ORGA_PB("ORGA_PB", "올가_PB", "정성을 담아,올가PB - #풀무원", "올가홀푸드,올가,ORGA,가공식품,간편식,올가PB,올가자체생산,올가프리미엄,올가생산", "원재료부터 꼼꼼하게 따져 만든 올가PB상품")
		,LOCAL_ITEM("LOCAL_ITEM", "올가_산지직송", "신선함 그대로, 산지직송 - #풀무원", "산지직송,산지배송,직접배송,업체배송", "산지에서 식탁까지! 신선함을 그대로")
		,ORGA_SHOP("ORGA_SHOP", "올가_매장배송", "매장상품을 빠르고 편하게, 매장배송 - #풀무원", "매장상품,매장배송,올가매장상품,올가매장배송,매장픽업,풀무원", "올가 매장 상품을 가장 빠르고 편하게 만나는 방법! 올가 매장 상품 온라인 주문 코너 입니다.")
		,ORGA_NEW("ORGA_NEW", "올가_신제품", "NEW,제일먼저 만나요 - 올가", "신제품,신상품,올가,올가신제품, 올가신상품", "올가가 자신있게 소개하는 신상품을 만나보세요.")
		,NONE_GROUP("NONE_GROUP", "등급없음", "", "", "")
		;

		private final String code;
		private final String codeName;
		private final String title;
		private final String keyword;
		private final String description;
	}

	// 메타 태그 모음
	@Getter
	@RequiredArgsConstructor
	public enum MetaUserStatusType implements CodeCommEnum {
		NOT_LOGIN("NOT_LOGIN", "비로그인"),
		NONMEMBER("NONMEMBER", "비회원"),
		MEMBER("MEMBER", "회원"),
		EMPLOYEE("EMPLOYEE", "임직원")
		;

		private final String code;
		private final String codeName;
	}

	//SNS구분
	@Getter
	@RequiredArgsConstructor
	public enum SnsProvider implements CodeCommEnum {
		NAVER("SNS_PROVIDER.NAVER", "네이버"),
		KAKAO("SNS_PROVIDER.KAKAO", "카카오"),
		GOOGLE("SNS_PROVIDER.GOOGLE", "구글"),
		FACEBOOK("SNS_PROVIDER.FACEBOOK", "페이스북"),
		APPLE("SNS_PROVIDER.APPLE", "애플");

		private final String code;
		private final String codeName;
	}

	//관리자 회원 공급처/출고처 권한 관리 구분
	@Getter
	@RequiredArgsConstructor
	public enum EmployeeAuthIdType implements CodeCommEnum {
		SUPPLIER("S", "공급업체"),
		WAREHOUSE("W", "출고처");

		private final String code;
		private final String codeName;
	}

	// 프론트 자동로그인 Key 엑션
	@Getter
	@RequiredArgsConstructor
	public enum AutoLoginKeyActionCode implements CodeCommEnum {
		INSERT("INSERT", "저장"),
		DELETE("DELETE", "삭제")
		;

		private final String code;
		private final String codeName;
	}
}