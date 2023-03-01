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
public class SerialEnums {

    //이용권 상태
    @Getter
    @RequiredArgsConstructor
    public enum SerialNumberStatus implements CodeCommEnum {
        ISSUED("SERIAL_NUMBER_STATUS.ISSUED", "발급"),
        USE("SERIAL_NUMBER_STATUS.USE","사용");

        private final String code;
        private final String codeName;
    }

    //난수번호 타입
    @Getter
    @RequiredArgsConstructor
    public enum SerialNumberType implements CodeCommEnum {
        AUTO_CREATE("SERIAL_NUMBER_TYPE.AUTO_CREATE", "자동생성"),
        EXCEL_UPLOAD("SERIAL_NUMBER_TYPE.EXCEL_UPLOAD", "엑셀업로드"),
        FIXED_VALUE("SERIAL_NUMBER_TYPE.FIXED_VALUE","단일코드");

        private final String code;
        private final String codeName;
    }

    //개별난수 생성 사용 타입
    @Getter
    @RequiredArgsConstructor
    public enum SerialNumberUseType implements CodeCommEnum {
        COUPON("SERIAL_NUMBER_USE_TYPE.COUPON", "쿠폰"),
        POINT("SERIAL_NUMBER_USE_TYPE.POINT","적립금");

        private final String code;
        private final String codeName;
    }

    //시리얼번호 유형 판별
    @Getter
    @RequiredArgsConstructor
    public enum SerialNumberAddType implements CodeCommEnum {
        NONE("NONE", "이용권 없음"),
        USE("USE", "사용한 이용권"),
        POINT("POINT","적립금"),
        COUPON("COUPON","쿠폰"),
        FIX_POINT("FIX_POINT","적립금"),
        FIX_COUPON("FIX_COUPON","쿠폰");

        private final String code;
        private final String codeName;
    }

    //이용권 / 쿠폰 등록 시
    @Getter
    @RequiredArgsConstructor
    public enum AddPromotion implements MessageCommEnum {
        NEED_LOGIN("NEED_LOGIN", "로그인필요"),
        RECAPTCHA_FAIL("RECAPTCHA_FAIL", "캡차 인증 실패"),
        SUCCESS_ADD_POINT_PARTIAL("SUCCESS_ADD_POINT_PARTIAL", "적립금 부분 등록되었습니다."),
        MAXIMUM_DEPOSIT_POINT_EXCEEDED("DEPOSIT_POINT_EXCEEDED", "적립 가능 적립금 초과"),
        SUCCESS_ADD_POINT("SUCCESS_ADD_POINT", "이용권이 정상적으로 등록되었습니다."),
        SUCCESS_ADD_COUPON("SUCCESS_ADD_COUPON", "이용권이 정상적으로 등록되었습니다."),
        NOT_FIND_SERIAL_NUMBER("NOT_FIND_SERIAL_NUMBER", "인증번호를 잘못 입력하셨습니다." + "다시 입력해 주세요."),
        USE_SERIAL_NUMBER("USE_SERIAL_NUMBER", "이미 사용된 이용권입니다."),
        OVER_ISSUE_DATE("OVER_ISSUE_DATE", "이용권의 사용기간이 만료되었습니다.")
        ;

        private final String code;
        private final String message;
    }
}
