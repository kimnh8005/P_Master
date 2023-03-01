package kr.co.pulmuone.v1.comm.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

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
public class CouponEnums {

    //쿠폰발급타입
    @Getter
    @RequiredArgsConstructor
    public enum CouponIssueType implements CodeCommEnum {
        NORMAL("COUPON_ISSUE_TYPE.NORMAL","일반"),
        REISSUE("COUPON_ISSUE_TYPE.REISSUE","재발급");

        private final String code;
        private final String codeName;
    }

    //쿠폰상태타입
    @Getter
    @RequiredArgsConstructor
    public enum CouponStatus implements CodeCommEnum {
        NOTUSE("COUPON_STATUS.NOTUSE","발급완료"),
        USE("COUPON_STATUS.USE","사용완료"),
        CANCEL("COUPON_STATUS.CANCEL","관리자회수");

        private final String code;
        private final String codeName;
    }

    //쿠폰승인/발급상태
    @Getter
    @RequiredArgsConstructor
    public enum CouponApprovalStatus implements CodeCommEnum {
        SAVE("COUPON_APPROVAL_STATUS.SAVE", "저장"),
        REQUEST_APPROVAL("COUPON_APPROVAL_STATUS.REQUEST_APPROVAL", "승인요청"),
        CANCEL_REQUEST("COUPON_APPROVAL_STATUS.CANCEL_REQUEST","요청취소"),
        ACCEPT_APPROVAL("COUPON_APPROVAL_STATUS.ACCEPT_APPROVAL","승인"),
        REJECT_APPROVAL("COUPON_APPROVAL_STATUS.REJECT_APPROVAL","반려"),
        ABORT("COUPON_APPROVAL_STATUS.ABORT","중단"),
        ABORT_WITHDRAW("COUPON_APPROVAL_STATUS.ABORT_WITHDRAW","중단회수"),

        //추후 삭제 해주세요
        NONE("COUPON_APPR_STAT.NONE", "대기"),
        REQUEST("COUPON_APPR_STAT.REQUEST", "승인요청"),
        CANCEL("COUPON_APPR_STAT.CANCEL", "승인철회"),
        DENIED("COUPON_APPR_STAT.DENIED", "승인반려"),
        SUB_APPROVED("COUPON_APPR_STAT.SUB_APPROVED", "승인완료(부)"),
        APPROVED("COUPON_APPR_STAT.APPROVED", "승인완료(정)");
    	//추후 삭제 해주세요

        private final String code;
        private final String codeName;
    }

    //쿠폰승인/발급상태
    @Getter
    @RequiredArgsConstructor
    public enum CouponMasterStatus implements CodeCommEnum {
    	SAVE("COUPON_MASTER_STAT.SAVE", "저장"),
    	APPROVED("COUPON_MASTER_STAT.APPROVED","발급"),
    	STOP("COUPON_MASTER_STAT.STOP","중단"),
    	STOP_WITHDRAW("COUPON_MASTER_STAT.STOP_WITHDRAW","중단회수");

        private final String code;
        private final String codeName;
    }

    //쿠폰종류
    @Getter
    @RequiredArgsConstructor
    public enum CouponType implements CodeCommEnum {
        GOODS("COUPON_TYPE.GOODS", "상품"),
        CART("COUPON_TYPE.CART","장바구니"),
        SHIPPING_PRICE("COUPON_TYPE.SHIPPING_PRICE","배송비"),
        SALEPRICE_APPPOINT("COUPON_TYPE.SALEPRICE_APPPOINT","판매가지정");

        private final String code;
        private final String codeName;
    }

    //할인방식
    @Getter
    @RequiredArgsConstructor
    public enum DiscountType implements CodeCommEnum {
        FIXED_DISCOUNT("COUPON_DISCOUNT_STATUS.FIXED_DISCOUNT", "정액"),
        PERCENTAGE_DISCOUNT("COUPON_DISCOUNT_STATUS.PERCENTAGE_DISCOUNT","정율");

        private final String code;
        private final String codeName;
    }

    //이용권 상태
    @Getter
    public enum SerialNumberStatus{
    	ISSUED("SERIAL_NUMBER_STATUS.ISSUED", "발급"),
    	USE("SERIAL_NUMBER_STATUS.USE","사용");

        private final String code;
        private final String codeName;

        SerialNumberStatus(String code, String codeName){
            this.code = code;
            this.codeName = codeName;
        }
    }

    //쿠폰 발급방법
    @Getter
    @RequiredArgsConstructor
    public enum PaymentType implements CodeCommEnum {
        GOODS_DETAIL("PAYMENT_TYPE.GOODS_DETAIL", "상품상세발급"),
        DOWNLOAD("PAYMENT_TYPE.DOWNLOAD", "다운로드"),
        AUTO_PAYMENT("PAYMENT_TYPE.AUTO_PAYMENT", "자동발급"),
        CHECK_PAYMENT("PAYMENT_TYPE.CHECK_PAYMENT", "계정발급"),
        TICKET("PAYMENT_TYPE.TICKET","이용권");

        private final String code;
        private final String codeName;
    }

    //난수번호 타입
    @Getter
    public enum SerialNumberType{
    	AUTO_CREATE("SERIAL_NUMBER_TYPE.AUTO_CREATE", "자동생성"),
    	EXCEL_UPLOAD("SERIAL_NUMBER_TYPE.EXCEL_UPLOAD", "엑셀업로드"),
    	FIXED_VALUE("SERIAL_NUMBER_TYPE.FIXED_VALUE","단일코드");

        private final String code;
        private final String codeName;

        SerialNumberType(String code, String codeName){
            this.code = code;
            this.codeName = codeName;
        }
    }

    //개별난수 생성 사용 타입
    @Getter
    public enum SerialNumberUseType{
    	COUPON("SERIAL_NUMBER_USE_TYPE.COUPON", "쿠폰"),
    	POINT("SERIAL_NUMBER_USE_TYPE.POINT","적립금");

        private final String code;
        private final String codeName;

        SerialNumberUseType(String code, String codeName){
            this.code = code;
            this.codeName = codeName;
        }
    }

    //쿠폰 유효기간
    @Getter
    @RequiredArgsConstructor
    public enum ValidityType implements CodeCommEnum {
        PERIOD("VALIDITY_TYPE.PERIOD", "기간 설정"),
        VALIDITY("VALIDITY_TYPE.VALIDITY", "유효일 설정");

        private final String code;
        private final String codeName;
    }

    //쿠폰 등록 검증
    @Getter
    @RequiredArgsConstructor
    public enum AddCouponValidation implements CodeCommEnum, MessageCommEnum {
        NOT_ACCEPT_APPROVAL("NOT_ACCEPT_APPROVAL", "사용 상태 아님", "사용 상태 아님"),
        OVER_ISSUE_QTY("OVER_ISSUE_QTY", "발급수량 제한", "발급수량 제한"),
        OVER_ISSUE_QTY_LIMIT("OVER_ISSUE_QTY_LIMIT","1인발급수량 제한", "1인발급수량 제한"),
        NOT_ISSUE_DATE("NOT_ISSUE_DATE","발급기간 아님", "발급기간 아님"),
        OVER_ISSUE_DATE("OVER_ISSUE_DATE","발급기간 지남", "발급기간 지남"),
        PASS_VALIDATION("PASS_VALIDATION","검증 통과", "검증 통과"),
        NOT_EXIST_COUPON("NOT_EXIST_COUPON", "존재하지 않는 쿠폰 정보", "존재하지 않는 쿠폰 정보"),
        MULTIPLE_COUPON_ERROR("MULTIPLE_COUPON_ERROR", "미발급 쿠폰을 제외한 전체쿠폰이 발급", "미발급 쿠폰을 제외한 전체쿠폰이 발급"),
        USER_ID_FAIL("USER_ID_FAIL", "유효하지 않은 회원ID가 있습니다.", "유효하지 않은 회원ID가 있습니다."),
        ETC("ETC","기타 오류", "기타 오류");

        private final String code;
        private final String codeName;
        private final String message;
    }

    //쿠폰 사용 검증
    @Getter
    @RequiredArgsConstructor
    public enum UseCouponValidation implements CodeCommEnum, MessageCommEnum {
        NOT_COUPON_STATUS("NOT_COUPON_STATUS", "쿠폰 사용 상태 아님", "쿠폰 사용 상태 아님"),
        USE_ISSUE_STATUS("USE_ISSUE_STATUS", "사용한 쿠폰", "사용한 쿠폰"),
        CANCEL_ISSUE_STATUS("CANCEL_ISSUE_STATUS", "취소된 쿠폰", "취소된 쿠폰"),
        OVER_EXPIRATION("OVER_EXPIRATION","유효기간 만료", "유효기간 만료"),
        NOT_EXIST_COUPON("NOT_EXIST_COUPON","존재하지 않는 쿠폰정보", "존재하지 않는 쿠폰정보"),
        PASS_VALIDATION("PASS_VALIDATION","검증 통과", "검증 통과");

        private final String code;
        private final String codeName;
        private final String message;
    }

    //쿠폰 발급방법
    @Getter
    @RequiredArgsConstructor
    public enum IssueType implements CodeCommEnum {
        GOODS_DETAIL("PAYMENT_TYPE.GOODS_DETAIL", "상품 상세 발급"),
        DOWNLOAD("PAYMENT_TYPE.DOWNLOAD", "다운로드"),
        AUTO_PAYMENT("PAYMENT_TYPE.AUTO_PAYMENT", "자동 지급"),
        CHECK_PAYMENT("PAYMENT_TYPE.CHECK_PAYMENT", "계정 발급"),
        TICKET("PAYMENT_TYPE.TICKET", "이용권");

        private final String code;
        private final String codeName;
    }

    //쿠폰 세부지급방법
    @Getter
    @RequiredArgsConstructor
    public enum IssueDetailType implements CodeCommEnum {
        USER_JOIN("AUTO_ISSUE_TYPE.USER_JOIN", "회원 가입"),
        EVENT_AWARD("AUTO_ISSUE_TYPE.EVENT_AWARD", "이벤트"),
        USER_GRADE("AUTO_ISSUE_TYPE.USER_GRADE", "회원 등급"),
        RECOMMENDER("AUTO_ISSUE_TYPE.RECOMMENDER", "추천인");

        private final String code;
        private final String codeName;
    }
    //쿠폰 승인 검증
    @Getter
    @RequiredArgsConstructor
    public enum ApprovalValidation implements MessageCommEnum {
    	NONE_REQUEST("NONE_REQUEST", "승인요청 상태가 없습니다."),
    	ALREADY_APPROVAL_REQUEST("ALREADY_APPROVAL_REQUEST", "이미 승인요청 중인 상태입니다. 승인요청중인 정보는 수정이 불가합니다."),
    	ALREADY_APPROVED("ALREADY_APPROVED", "이미 승인이 완료되어 철회가 불가능합니다."),
    	ALREADY_DENIED("ALREADY_DENIED", "이미 승인이 반려되었습니다. 반려 정보를 확인해 주세요."),
    	ALREADY_CANCEL_REQUEST("ALREADY_CANCEL_REQUEST", "승인요청자가 승인요청을 철회하였습니다.");

    	private final String code;
    	private final String message;
    }


    //단일코드 중복 메시지
    @Getter
    @RequiredArgsConstructor
    public enum FixedNumberValidation implements MessageCommEnum {
    	DUPLICATE_NUMBER("DUPLICATE_NUMBER", "사용중인 이용권 코드입니다."),
        SAME_NUMBER("SAME_NUMBER", "동일 한 이용권 번호는 사용할 수 없습니다.");

    	private final String code;
    	private final String message;
    }

    //쿠폰 발급목적
    @Getter
    @RequiredArgsConstructor
    public enum IssuePurpose implements  CodeCommEnum {
    	CS_ISSUED("ISSUE_PURPOSE_TYPE.CS_ISSUED", "CS발급"),
    	GRADE_FAVOR("ISSUE_PURPOSE_TYPE.GRADE_FAVOR", "등급혜택"),
    	EVENT("ISSUE_PURPOSE_TYPE.EVENT", "이벤트"),
    	PLAN_FAVOR_ISSUED("ISSUE_PURPOSE_TYPE.PLAN_FAVOR_ISSUED", "기획전 혜택 지급"),
    	CORPORATION_REQUEST("ISSUE_PURPOSE_TYPE.CORPORATION_REQUEST", "법인요청"),
    	ALLIANCE("ISSUE_PURPOSE_TYPE.ALLIANCE", "제휴지원");

        private final String code;
        private final String codeName;
    }

    //쿠폰 재발급시 쿠폰명
    @Getter
    @RequiredArgsConstructor
    public enum ReissueCouponName implements  CodeCommEnum {
    	BOS_CART("BOS_COUPON_NAME_CART", "주문취소 재발급 쿠폰"),
    	DISPLAY_CART("DISPLAY_COUPON_NAME_CART", "장바구니 쿠폰");

        private final String code;
        private final String codeName;
    }

    //쿠폰 제한
    @Getter
    @RequiredArgsConstructor
    public enum CouponLimit implements  CodeCommEnum {
    	LIMIT1("COUPON_LIMIT.1", "1"),
    	LIMIT2("COUPON_LIMIT.2", "2"),
    	LIMIT3("COUPON_LIMIT.3", "3"),
    	LIMIT4("COUPON_LIMIT.4", "4"),
    	LIMIT5("COUPON_LIMIT.5", "5"),
    	LIMIT6("COUPON_LIMIT.6", "6"),
    	LIMIT7("COUPON_LIMIT.7", "7"),
    	LIMIT8("COUPON_LIMIT.8", "8"),
    	LIMIT9("COUPON_LIMIT.9", "9"),
    	LIMIT10("COUPON_LIMIT.10", "10");

        private final String code;
        private final String codeName;

        public static CouponLimit findByCodeName(String codeName) {
			return Arrays.stream(CouponLimit.values())
		            .filter(couponLimit -> couponLimit.getCodeName().equals(codeName))
		            .findAny()
		            .orElse(null);
        }
    }

    //쿠폰 적용유형
    @Getter
    @RequiredArgsConstructor
    public enum ApplyCoverage implements  CodeCommEnum {
        GOODS("APPLYCOVERAGE.GOODS", "상품"),
        BRAND("APPLYCOVERAGE.BRAND", "브랜드"),
        DISPLAY_CATEGORY("APPLYCOVERAGE.DISPLAY_CATEGORY", "카테고리"),
        WAREHOUSE("APPLYCOVERAGE.WAREHOUSE", "출고처");

        private final String code;
        private final String codeName;
    }

}
