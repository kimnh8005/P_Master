package kr.co.pulmuone.batch.esl.common.enums;

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
 *  1.1    2020. 12. 14.               이명수        주문상태 추가 (OrderStatus)
 *  1.2    2020. 12. 17.               이명수        주문등록경로 추가 (OrderRoute)
 * =======================================================================
 * </PRE>
 */
public class OrderEnums {

    // 주문상태
    @Getter
    @RequiredArgsConstructor
    public enum OrderStatus {

        INCOM_READY("IR", "입금대기중"),
        INCOM_BEFORE_CANCEL_COMPLETE("IB", "입금전 취소"),
        INCOM_COMPLETE("IC", "결제완료"),
        DELIVERY_READY("DR", "배송준비중"),
        DELIVERY_ING("DI", "배송중"),
        DELIVERY_COMPLETE("DC", "배송완료"),
        BUY_FINALIZED("BF", "구매확정"),
        CANCEL_APPLY("CA", "취소요청"),
        CANCEL_COMPLETE("CC", "취소완료"),
        CANCEL_WITHDRAWAL("CW", "취소철회"),
        EXCHANGE_COMPLETE("EC", "재배송"),
        RETURN_APPLY("RA", "반품요청"),
        RETURN_ING("RI", "반품승인"),
        RETURN_DEFER("RF", "반품보류"),
        RETURN_COMPLETE("RC", "반품완료"),
        RETURN_DENY_DEFER("RE", "반품거부"),
        RETURN_WITHDRAWAL("RW", "반품철회"),
        CANCEL_DENY("CE", "취소거부"),
        CUSTOMER_SERVICE_REFUND("CS", "CS환불"),
        REFUND_APPLY("FA", "환불요청"),
        REFUND_COMPLETE("FC", "환불완료"),
        CLAIM_REQUEST("CR", "클레임요청"),
        CLAIM_APPROVED("CL", "클레임승인"),

        DELIVERY_DT_CHANGE("DDC", "도착예정일변경"),
        OUTMALL_ORDER_CREATE("OOC", "외부몰 주문 생성"),

        ;

        private final String code;
        private final String codeName;

        public static OrderStatus findByCode(String code) {
 			return Arrays.stream(OrderStatus.values())
 		            .filter(paymentType -> paymentType.getCode().equals(code))
 		            .findAny()
 		            .orElse(null);
        }
    }

    // 주문상세 상태 이력 메세지
    @Getter
    @RequiredArgsConstructor
    public enum OrderDetailStatusHistMsg implements MessageCommEnum {
        INCOM_COMPLETE_MSG("IC", "결제완료 상태변경"),
        DELIVERY_READY_MSG("DR", "배송준비중 상태변경"),
        DELIVERY_ING_MSG("DI", "[{0}] 송장번호 ({1}) 입력 "),
        DELIVERY_COMPLETE_MSG("DC", "배송완료 상태변경"),
        BUY_FINALIZED_MSG("BF", "구매확정 상태변경"),
        CANCEL_WITHDRAWAL_MSG("CW", "취소철회 상태변경"),
        RETURN_WITHDRAWAL_MSG("RW", "반품철회 상태변경"),
        DELIVERY_DT_CHANGE("DDC", "도착예정일 변경"),
        OUTMALL_ORDER_CREATE("OOC", "외부몰 주문 생성"),
        DELIVERY_ING_CANCEL_DENIAL("CE", "배송중으로 인한 취소거부"),
        CANCEL_DENY_DEFE("CE", "이미 출고되어 취소가 불가합니다."),
        ;
        private final String code;
        private final String message;

        public static OrderDetailStatusHistMsg findByCode(String code) {
            return Arrays.stream(OrderDetailStatusHistMsg.values())
                    .filter(item -> item.getCode().equals(code))
                    .findAny()
                    .orElse(null);
        }
    }
}