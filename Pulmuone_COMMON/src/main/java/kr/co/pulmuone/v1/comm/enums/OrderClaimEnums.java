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
 *  1.0    2021. 02. 15.         이명수          최초작성
 * =======================================================================
 * </PRE>
 */
public class OrderClaimEnums {
    // 귀책구분
    @Getter
    @RequiredArgsConstructor
    public enum ClaimTargetTp implements CodeCommEnum {
        TARGET_BUYER("B", "구매자"),
        TARGET_SELLER("S", "판매자"),
        ;

        private final String code;
        private final String codeName;
    }

    // 쿠폰종류
    @Getter
    @RequiredArgsConstructor
    public enum ClaimCouponTp implements CodeCommEnum {
        COUPON_GOODS("G", "상품(판매자지정포함)"),
        COUPON_CART("C", "장바구니"),
        ;

        private final String code;
        private final String codeName;
    }

    // 모든 종류 필드의 유무 Y, N
    @Getter
    @RequiredArgsConstructor
    public enum AllTypeYn implements CodeCommEnum {
        ALL_TYPE_Y("Y", "예"),
        ALL_TYPE_N("N", "아니오"),
        ;

        private final String code;
        private final String codeName;
    }

    // 모든 종류 필드의 유무 Y, N
    @Getter
    @RequiredArgsConstructor
    public enum ClaimValidYn implements CodeCommEnum {
        CLAIM_VALID_YN_Y("Y", "성공"),
        CLAIM_VALID_YN_N("N", "실패"),
        ;

        private final String code;
        private final String codeName;
    }

    // 환불수단 방법
    @Getter
    @RequiredArgsConstructor
    public enum RefundType implements CodeCommEnum {
        REFUND_TYPE_D("D", "원결제 내역"),
        REFUND_TYPE_C("C", "무통장입금"),
        ;

        private final String code;
        private final String codeName;
    }

    //클레임상태구분 (C : 취소, R : 반품)
    @Getter
    @RequiredArgsConstructor
    public enum ClaimStatusTp implements CodeCommEnum {
    	CANCEL("CLAIM_STATUS_TP.CANCEL", "취소"),
    	RETURN("CLAIM_STATUS_TP.RETURN", "반품"),
    	CS_REFUND("CLAIM_STATUS_TP.CS_REFUND", "CS환불"),
    	RETURN_DELIVERY("CLAIM_STATUS_TP.RETURN_DELIVERY", "재배송"),
        ;

        private final String code;
        private final String codeName;

        public static OrderClaimEnums.ClaimStatusTp findByCodeNm(String codeNm) {
            return Arrays.stream(OrderClaimEnums.ClaimStatusTp.values())
                    .filter(item -> item.getCodeName().equals(codeNm))
                    .findAny()
                    .orElse(null);
        }
    }

    //CS환불구분
    @Getter
    @RequiredArgsConstructor
    public enum CsRefundTp implements CodeCommEnum {
        CS_REFUND_TP_PRICE("CS_REFUND_TP.PAYMENT_PRICE_REFUND", "결제금액환불"),
        CS_REFUND_TP_POINT("CS_REFUND_TP.POINT_PRICE_REFUND", "적립금환불"),
        ;

        private final String code;
        private final String codeName;
    }

    //재배송구분
    @Getter
    @RequiredArgsConstructor
    public enum RedeliveryType implements CodeCommEnum {
    	RETURN_DELIVERY("R", "재배송"),
    	SUBSTITUTE_GOODS("S", "대체상품"),
        ;

        private final String code;
        private final String codeName;
    }

    // 취소가능 상태 'IR', 'IC', 'DR'
    @Getter
    @RequiredArgsConstructor
    public enum CancelAbleStatus implements CodeCommEnum {

        INCOM_READY("IR", "입금대기중"),
        INCOM_COMPLETE("IC", "결제완료"),
        DELIVERY_READY("DR", "배송준비중"),
        ;

        private final String code;
        private final String codeName;

        public static OrderClaimEnums.CancelAbleStatus findByCode(String code) {
            return Arrays.stream(OrderClaimEnums.CancelAbleStatus.values())
                    .filter(item -> item.getCode().equals(code))
                    .findAny()
                    .orElse(null);
        }
    }

    // 취소가능 변경 상태 'CA', 'CC'
    @Getter
    @RequiredArgsConstructor
    public enum CancelAbleTargetStatus implements CodeCommEnum {

        CANCEL_APPLY("CA", "취소요청"),
        CANCEL_COMPLETE("CC", "취소완료"),
        INCOM_BEFORE_CANCEL_COMPLETE("IB", "입금전 취소"),
        ;

        private final String code;
        private final String codeName;

        public static OrderClaimEnums.CancelAbleTargetStatus findByCode(String code) {
            return Arrays.stream(OrderClaimEnums.CancelAbleTargetStatus.values())
                    .filter(item -> item.getCode().equals(code))
                    .findAny()
                    .orElse(null);
        }
    }

    // 반품가능 상태 'DI', 'DC', 'BF'
    @Getter
    @RequiredArgsConstructor
    public enum ReturnAbleStatus implements CodeCommEnum {

        DELIVERY_ING("DI", "배송중"),
        DELIVERY_COMPLETE("DC", "배송완료"),
        BUY_FINALIZED("BF", "구매확정"),
        ;

        private final String code;
        private final String codeName;

        public static OrderClaimEnums.ReturnAbleStatus findByCode(String code) {
            return Arrays.stream(OrderClaimEnums.ReturnAbleStatus.values())
                    .filter(item -> item.getCode().equals(code))
                    .findAny()
                    .orElse(null);
        }
    }

    // 반품가능 변경 상태 'RA', 'RI'
    @Getter
    @RequiredArgsConstructor
    public enum ReturnAbleTargetStatus implements CodeCommEnum {

        RETURN_APPLY("RA", "반품요청"),
        RETURN_ING("RI", "반품승인"),
        RETURN_COMPLETE("RC", "반품완료"),
        ;

        private final String code;
        private final String codeName;

        public static OrderClaimEnums.ReturnAbleTargetStatus findByCode(String code) {
            return Arrays.stream(OrderClaimEnums.ReturnAbleTargetStatus.values())
                    .filter(item -> item.getCode().equals(code))
                    .findAny()
                    .orElse(null);
        }
    }

    // 부분취소 처리 오류
    @Getter
    @RequiredArgsConstructor
    public enum PartCancelError implements CodeCommEnum {
        ACCOUNT_EMPTY_ERROR("ACCOUNT_EMPTY_ERROR", "계좌 정보 확인", "계좌 정보를 확인 해주세요."),
        ;

        private final String code;
        private final String codeName;
        private final String message;

        public static OrderClaimEnums.ReturnAbleTargetStatus findByCode(String code) {
            return Arrays.stream(OrderClaimEnums.ReturnAbleTargetStatus.values())
                    .filter(item -> item.getCode().equals(code))
                    .findAny()
                    .orElse(null);
        }
    }

    // 부분취소 가능 여부
    @Getter
    @RequiredArgsConstructor
    public enum PartCancelYn implements CodeCommEnum {

        PART_CANCEL_YN_Y("1", "부분취소가능", "Y"),
        PART_CANCEL_YN_N("0", "부분취소불가능", "N"),
        ;

        private final String code;
        private final String codeName;
        private final String codeValue;

        public static OrderClaimEnums.PartCancelYn findByCode(String code) {
            return Arrays.stream(OrderClaimEnums.PartCancelYn.values())
                    .filter(item -> item.getCode().equals(code))
                    .findAny()
                    .orElse(null);
        }
    }

    // 부분취소 처리 오류
    @Getter
    @RequiredArgsConstructor
    public enum AddPaymentClaimInfoError implements MessageCommEnum {
        NONE_ADD_PAYMENT_INFO("NONE_ADD_PAYMENT_INFO", "추가결제 정보가 존재하지 않습니다."),
        ;

        private final String code;
        private final String message;

        public static OrderClaimEnums.AddPaymentClaimInfoError findByCode(String code) {
            return Arrays.stream(OrderClaimEnums.AddPaymentClaimInfoError.values())
                    .filter(item -> item.getCode().equals(code))
                    .findAny()
                    .orElse(null);
        }
    }

    @Getter
    @RequiredArgsConstructor
    public enum GreenJuiceClaimType implements CodeCommEnum {

        CLAIM_TYPE_CANCEL("C", "취소"),
        CLAIM_TYPE_RETURN("R", "반품"),
        CLAIM_TYPE_EXCHANGE("E", "재배송"),
        ;

        private final String code;
        private final String codeName;

        public static OrderClaimEnums.GreenJuiceClaimType findByCode(String code) {
            return Arrays.stream(OrderClaimEnums.GreenJuiceClaimType.values())
                    .filter(item -> item.getCode().equals(code))
                    .findAny()
                    .orElse(null);
        }
    }

    @Getter
    @RequiredArgsConstructor
    public enum GreenJuiceScheduleStatus implements CodeCommEnum {
        SCHEDULE_STATUS_ORDER("1", "주문"),
        SCHEDULE_STATUS_CANCEL("2", "취소"),
        SCHEDULE_STATUS_RETURNS("3", "반품"),
        SCHEDULE_STATUS_EXCHANGE("4", "재배송"),
        ;

        private final String code;
        private final String codeName;

        public static OrderClaimEnums.GreenJuiceScheduleStatus findByCode(String code) {
            return Arrays.stream(OrderClaimEnums.GreenJuiceScheduleStatus.values())
                    .filter(item -> item.getCode().equals(code))
                    .findAny()
                    .orElse(null);
        }
    }

    // 녹즙 취소 / 반품 등록 에러
    @Getter
    @RequiredArgsConstructor
    public enum GreenJuiceRegistError implements MessageCommEnum {
        SUCCESS("SUCCESS", "녹즙 클레임 정보 등록 성공"),
        GOODS_SCHEDULE_NONE("GOODS_SCHEDULE_NONE", "취소 가능 스케쥴 정보 미존재"),
        ERP_IF_FAIL("ERP_IF_FAIL", "하이톡 API 응답 결과 실패"),
        SHOP_ERP_IF_FAIL("SHOP_ERP_IF_FAIL", "매장배송 API 응답 결과 실패"),
        RETUEN_LOTTE_FAIL("RETUEN_LOTTE_FAIL", "롯데택배 반품접수 실패"),
        RETUEN_LOTTE_FAIL_NULL("RETUEN_LOTTE_FAIL", "롯데택배 반품접수 실패 NULL")
        ;

        private final String code;
        private final String message;

        public static OrderClaimEnums.GreenJuiceRegistError findByCode(String code) {
            return Arrays.stream(OrderClaimEnums.GreenJuiceRegistError.values())
                    .filter(item -> item.getCode().equals(code))
                    .findAny()
                    .orElse(null);
        }
    }

    // 주문 클레임 생성 배치 타입 정보
    @Getter
    @RequiredArgsConstructor
    public enum OrderClaimBatchTypeCd implements CodeCommEnum{
        CREATE_CLAIM("ORDER_CLAIM_BATCH_TYPE.CREATE_CLAIM", "주문클레임 생성 배치"),
        ;

        private final String code;
        private final String codeName;
    }

    // 주문 클레임 프론트여부
    @Getter
    @RequiredArgsConstructor
    public enum OrderClaimFrontTpCd implements CodeCommEnum{
        FRONT_TP_BOS("0", "BOS", 0),
        FRONT_TP_FRONT("1", "프론트", 1),
        FRONT_TP_BATCH("2", "배치", 2)
        ;

        private final String code;
        private final String codeName;
        private final int codeValue;
    }

    // 배송비 추가 결제 조회 오류 메시지
    @Getter
    @RequiredArgsConstructor
    public enum AddPaymentShippingPriceError implements MessageCommEnum {
        SUCCESS("SUCCESS", "추가 결제 배송비 조회 성공"),
        DIRECT_PAYMENT_YN_FAIL("DIRECT_PAYMENT_YN_FAIL", "이미 결제된 주문건 입니다."),
        VALUE_EMPTY("VALUE_EMPTY", "해당 주문정보가 존재하지 않음"),
        ;

        private final String code;
        private final String message;

        public static OrderClaimEnums.AddPaymentShippingPriceError findByCode(String code) {
            return Arrays.stream(OrderClaimEnums.AddPaymentShippingPriceError.values())
                    .filter(item -> item.getCode().equals(code))
                    .findAny()
                    .orElse(null);
        }
    }

    // 추가결제구분
    @Getter
    @RequiredArgsConstructor
    public enum ClaimAddPaymentTp implements CodeCommEnum {
        CARD("ADD_PAYMENT_TP.CARD", "비인증결제"),
        VIRTUAL("ADD_PAYMENT_TP.VIRTUAL", "가상계좌"),
        DIRECT("ADD_PAYMENT_TP.DIRECT_PAY", "직접결제"),
        ;

        private final String code;
        private final String codeName;
    }

    // 주문취소시 쿠폰 재발급 오류 메시지
    @Getter
    @RequiredArgsConstructor
    public enum RefundCouponError implements MessageCommEnum {
        FAIL("FAIL", "오류가 발생하였습니다.<br />동일한 문제가 계속 발생할 경우 고객기쁨센터에 문의해 주세요."),
        ;

        private final String code;
        private final String message;

    }

    //주문생성 구분
    @Getter
    @RequiredArgsConstructor
    public enum OrderCreateTp implements CodeCommEnum {
        MEMBER("MEMBER", "회원"),
        NON_MEMBER("NON_MEMBER", "비회원"),
        EXCEL_UPLOAD("EXCEL_UPLOAD", "엑셀 업로드"),
        ORDER_COPY("ORDER_COPY", "주문 복사")
        ;

        private final String code;
        private final String codeName;
    }

}