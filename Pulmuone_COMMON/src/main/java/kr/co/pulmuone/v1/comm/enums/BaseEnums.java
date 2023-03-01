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
public class BaseEnums {

    // 성공 여부
    @Getter
    @RequiredArgsConstructor
    public enum Default implements MessageCommEnum {
        SUCCESS("0000", "성공") /**/
        , FAIL("9999", "관리자에게 문의하세요.")/**/
        /* Exception 에러 발생시 사용 될 코드,
         * 9000번대 대역에서 상위 Exception 별로 100단위로 끊고,
         * 하위로 10단위로 끊어서 사용되면 어떨까? 의견제시 */
        , EXCEPTION_ISSUED("9000", "시스템에러 발생, 관리자에게 문의하세요.")
        , EXCEPTION_BIND("9001", "지원하지 않는 형식의 입력값이 존재합니다.")
        , EXCEPTION_ENUM("9002", "지원하지 않는 약어값이 존재합니다. 관리자에게 문의하세요.")
        , EXCEPTION_HTTP("9003", "지원하지 않는 HTTP 형식입니다. 관리자에게 문의하세요.")
        ;

        private final String code;
        private final String message;
    }

    // 공통 체크
    @Getter
    @RequiredArgsConstructor
    public enum CommBase implements MessageCommEnum {
        VALID_ERROR("888888888", "데이터가 유효하지 않습니다.")	// 신규 추가 코드
        ,DUPLICATE_DATA("777777777", "중복된 데이터가 존재합니다.")	// 신규 추가 코드
        ,FOREIGN_KEY_DATA("666666666", "다른테이블에서 사용중입니다.")	// 신규 추가 코드
        ,PROGRAM_ERROR("111111111", "프로그램 오류입니다.")	// 신규 추가 코드
        ,MENU_ERROR("222222222", "메뉴 오류입니다.")	// 신규 추가 코드
        ,MANDATORY_MISSING("333333333", "필수값을 입력해주세요.")	//신규 추가 코드
        ,NEED_LOGIN("NEED_LOGIN", "로그인이 필요합니다.")	//신규 추가 코드
        ,DUPLICATE_DATA_DO_REFRESH("DUPLICATE_DATA_DO_REFRESH", "중복된 데이터가 존재합니다.")	//신규 추가 코드
        ,CONVERT_JSON_ERROR("CONVERT_JSON_ERROR", "json 데이터 에러")
        ,PAGE_AUTH_FAIL("PAGE_AUTH_FAIL", "페이지 권한 없음")
        ,URL_AUTH_FAIL("URL_AUTH_FAIL", "URL 권한 없음")
        /*
		 * 기존 시스템 하드코딩을 추려서 아래에 적어 놓았음.
		 * 실제로 사용될때 마다 주석을 풀고 적절한 메세지 코드를 넣어서 사용해야 함.
		 *
		 *
		,CODE_001("000010001", "")	// PolicyUtil. //비밀번호 일치여부 검증, 계정사용여부 체크하기
		,CODE_002("000010000", "")	// PolicyUtil.	//비밀번호 변경주기 확인 여부
		,CODE_003("20000", "")		// JsonApiService.createProduct // 입력 성공하였습니다.
		,CODE_004("000010000", "")	// fnDefCallback
		,CODE_005("400000002", "")	// fnGetMenuList
		,CODE_006("999999999", "")	// resolveException
		//FRONT_SHOP
		,CODE_007("100000000", "")	// putCartDetl // 수량은 1개 이상 구매 하셔야 합니다.
		,CODE_008("900000020", "")	// putCartDetl // 세션이 만료되었거나 데이터 처리 중 문제가 발생하였습니다. 새로 고침 후 다시 주문 시도해주시기 바랍니다.
		,CODE_009("900000000", "")	// putCartDetl // 포인트를 사용할수 없는 고객입니다.
		,CODE_0010("900000001", "")	// putCartDetl // 포인트 사용금액 초과
		,CODE_0011("900000001", "")	// putCartDetl // 포인트 사용금액 초과
		*/
        ;

        private final String code;
        private final String message;
    }

    // 사용여부
    @Getter
    public enum UseYn {
        Y("1", true),
        N("0", false);

        private final String numberCode;
        private final boolean primitiveCode;

        UseYn(String numberCode, boolean primitiveCode){
            this.numberCode = numberCode;
            this.primitiveCode = primitiveCode;
        }
    }

    // 공통코드 등록타입
    @Getter
    @RequiredArgsConstructor
    public enum StcomnCodeHistTp implements CodeCommEnum {
        INSERT("INSERT", "등록"),
        UPDATE("UPDATE", "수정");

        private final String code;
        private final String codeName;
    }

    // 사이트 유형
    @Getter
    @RequiredArgsConstructor
    public enum EnumSiteType {
        BOS("BOS"),
        MALL("MALL");

        private final String code;
    }

    // 서버 유형
    @Getter
    @RequiredArgsConstructor
    public enum ServerType {
        LOCAL("local"),
        DEVELOP("dev"),
        QA("qa"),
        STAGE("stg"),
        PROD("prod");

        private final String code;
    }

    // 도서산간/배송불가 등록 or 삭제
    @Getter
    @RequiredArgsConstructor
    public enum EnumUseYn implements CodeCommEnum {
        INSERT("INSERT", "등록"),
        DELETE("DELETE", "삭제");

        private final String code;
        private final String codeName;
    }

    // 도서산간/배송불가 등록 or 삭제
    @Getter
    @RequiredArgsConstructor
    public enum AlternateDeliveryType implements CodeCommEnum {
        Y("ALTERNATE_DELIVERY_TP.Y", "대체배송"),
        CJ("ALTERNATE_DELIVERY_TP.CJ", "CJ 대체배송"),
        LOTTE("ALTERNATE_DELIVERY_TP.LOTTE", "LOTTE 대체배송"),
        DAWN_CJ("ALTERNATE_DELIVERY_TP.DAWN_CJ", "CJ 새벽 대체배송");

        private final String code;
        private final String codeName;
    }
}