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
 *  1.0    2020. 9. 9.               이원호          최초작성
 * =======================================================================
 * </PRE>
 */
public class PointEnums {

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

	//적립금 회원등급 설정 타입
    @Getter
    public enum PointUsergradeType{
    	NORMAL("POINT_USERGRADE_TYPE.NORMAL", "일반"),
    	USER_GRADE("POINT_USERGRADE_TYPE.USER_GRADE","회원등급별");

        private final String code;
        private final String codeName;

        PointUsergradeType(String code, String codeName){
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



    //적립금 발급상태
    @Getter
    @RequiredArgsConstructor
    public enum PointApprovalStatus implements CodeCommEnum {
//        SAVE("POINT_APPROVAL_STAT.SAVE", "저장"),
//        REQUEST_APPROVAL("POINT_APPROVAL_STAT.REQUEST_APPROVAL", "승인요청"),
//        ACCEPT_APPROVAL("POINT_APPROVAL_STAT.ACCEPT_APPROVAL","지급"),
//        REJECT_APPROVAL("POINT_APPROVAL_STAT.REJECT_APPROVAL","승인반려"),
//        ABORT("POINT_APPROVAL_STAT.ABORT","중단"),
//        EXPIRE("POINT_APPROVAL_STAT.EXPIRE","지급기간만료");

    	//추후 삭제 해주세요
    	NONE("POINT_APPR_STAT.NONE", "대기"),
    	REQUEST("POINT_APPR_STAT.REQUEST", "승인요청"),
    	CANCEL("POINT_APPR_STAT.CANCEL", "승인철회"),
    	DENIED("POINT_APPR_STAT.DENIED", "승인반려"),
    	SUB_APPROVED("POINT_APPR_STAT.SUB_APPROVED", "승인완료(부)"),
    	APPROVED("POINT_APPR_STAT.APPROVED","승인완료(정)");
    	//추후 삭제 해주세요

        private final String code;
        private final String codeName;
    }

    //쿠폰승인/발급상태
    @Getter
    @RequiredArgsConstructor
    public enum PointMasterStatus implements CodeCommEnum {
    	SAVE("POINT_MASTER_STAT.SAVE", "저장"),
    	APPROVED("POINT_MASTER_STAT.APPROVED","지급"),
    	STOP("POINT_MASTER_STAT.STOP","중단");

        private final String code;
        private final String codeName;
    }

    //적립금 타입
    @Getter
    @RequiredArgsConstructor
    public enum PointType implements CodeCommEnum {
        ADMIN("POINT_TYPE.ADMIN", "관리자 지급/차감"),
        AUTO("POINT_TYPE.AUTO", "자동지급"),
        BUY("POINT_TYPE.BUY","구매"),
        FEEDBACK("POINT_TYPE.FEEDBACK","후기"),
        SERIAL_NUMBER("POINT_TYPE.SERIAL_NUMBER","이용권");

        private final String code;
        private final String codeName;
    }

    //적립금 유효기간
    @Getter
    @RequiredArgsConstructor
    public enum ValidityType implements CodeCommEnum {
        PERIOD("VALIDITY_TYPE.PERIOD", "기간 설정"),
        VALIDITY("VALIDITY_TYPE.VALIDITY", "유효일 설정");

        private final String code;
        private final String codeName;
    }

    //적립금 등록 검증
    @Getter
    @RequiredArgsConstructor
    public enum AddPointValidation implements CodeCommEnum, MessageCommEnum {
        NOT_ACCEPT_APPROVAL("NOT_ACCEPT_APPROVAL", "사용 상태 아님", "사용 상태 아님"),
        OVER_ISSUE_QTY("OVER_ISSUE_QTY", "발급수량 제한", "발급수량 제한"),
        OVER_ISSUE_QTY_LIMIT("OVER_ISSUE_QTY_LIMIT","1인발급수량 제한", "1인발급수량 제한"),
        NOT_ISSUE_DATE("NOT_ISSUE_DATE","발급기간 아님", "발급기간 아님"),
        OVER_ISSUE_DATE("OVER_ISSUE_DATE","발급기간 지남", "발급기간 지남"),
        PASS_VALIDATION("PASS_VALIDATION","검증 통과", "검증 통과"),
        NOT_EXIST_POINT("NOT_EXIST_POINT","존재하지 않는 적립금 정보", "존재하지 않는 적립금 정보"),
        ETC("ETC","기타 오류", "기타 오류");

        private final String code;
        private final String codeName;
        private final String message;
    }


    //적립금 메세지
    @Getter
    @RequiredArgsConstructor
    public enum PointMessage implements MessageCommEnum {
        MAXIMUM_DEPOSIT_POINT_EXCEEDED("DEPOSIT_POINT_EXCEEDED", "적립 가능 적립금 초과");

        private final String code;
        private final String message;
    }

    @Getter
    @RequiredArgsConstructor
    public enum PointProcessType implements CodeCommEnum {
        DEPOSIT_POINT_FEEDBACK_REWARD("POINT_PROCESS_TP.DPRR", "적립 후기 작성", true, "POINT_SETTLEMENT_TP.SDNC", "후기 작성 적립"), // DPRR : Deposit Point Feedback Reward
        DEPOSIT_POINT_CONFIRM_ORDER("POINT_PROCESS_TP.DPCO", "적립 구매 완료", true, "POINT_SETTLEMENT_TP.SDNC", "구매 적립"), // DPCO : Deposit Point Confirm Order
        DEPOSIT_POINT_MANUAL_REWARD("POINT_PROCESS_TP.DPMR", "적립 관리자 고객센터", true, "POINT_SETTLEMENT_TP.SDNC", "고객센터 지급 적립"), // DPMR : Deposit Point Manual Reward
        DEPOSIT_POINT_MANUAL_EVENT("POINT_PROCESS_TP.DPME", "적립 관리자 이벤트", true, "POINT_SETTLEMENT_TP.SDNC", "이벤트 적립"), // DPME : Deposit Point Manual Event
        DEPOSIT_POINT_AUTO_EVENT("POINT_PROCESS_TP.DPAE", "적립 자동지급 이벤트", true, "POINT_SETTLEMENT_TP.SDNC", "이벤트 적립"), // DPAE : Deposit Point Auto Event
        DEPOSIT_POINT_AUTO_RECOMMENDER("POINT_PROCESS_TP.DPAR", "적립 자동지급 추천인 혜택", true, "POINT_SETTLEMENT_TP.SDNC", "추천인 적립"), // DPAR : Deposit Point Auto Recommender
        DEPOSIT_VOUCHER_CHAGE("POINT_PROCESS_TP.DVCH", "적립 이용권", true, "POINT_SETTLEMENT_TP.SDVC", "이용권 적립"), // DVCH : Deposit Voucher CHarge
        DEPOSIT_POINT_REFUND_ORDER("POINT_PROCESS_TP.DPRO", "적립 주문 취소 적립금", true, "POINT_SETTLEMENT_TP.SDNC", "사용취소"), // DPRO : Deposit Point Refund Order 적립금 환불
        DEPOSIT_VOUCHER_REFUND_ORDER("POINT_PROCESS_TP.DVRO", "적립 주문 취소 이용권", true, "POINT_SETTLEMENT_TP.SDVC", "사용취소"), // DVRO : Deposit Voucher Refund Order 이용권 환불
        DEPOSIT_POINT_ORGA_TRANSFER("POINT_PROCESS_TP.DPOT", "적립 올가 포인트 이전", true, "POINT_SETTLEMENT_TP.SDNC", "구)올가 포인트 전환"), // DPOT : Deposit Point Orga Transfer 올가 포인트 이전
        DEPOSIT_POINT_PULMUONE_TRANSFER("POINT_PROCESS_TP.DPPT", "적립 풀무원 포인트 이전", true, "POINT_SETTLEMENT_TP.SDNC", "구)풀무원샵 적립금 전환"), // DPOT : Deposit Point Pulmuone Transfer 풀무원 포인트 이전
        DEPOSIT_POINT_CUSTOMER_SERVICE_REFUND_ORDER("POINT_PROCESS_TP.DPCR", "적립 관리자 CS환불", true, "POINT_SETTLEMENT_TP.SDNC", "관리자 CS환불"), // DPCR : Deposit Point Customer service Refund order 관리자 CS환불
        WITHDRAW_POINT_PAYMENT("POINT_PROCESS_TP.WPPA", "주문 결제 사용 적립금", false, "POINT_SETTLEMENT_TP.SWNP", "결제 시 사용"), // WPPA : Withdraw Point PAyment
        WITHDRAW_VOUCHER_PAYMENT("POINT_PROCESS_TP.WVPA", "주문 결제 사용 이용권", false, "POINT_SETTLEMENT_TP.SWVP", "결제 시 사용"), // WVPA : Withdraw Voucher PAyment
        WITHDRAW_POINT_FORCED_EXTINCTION("POINT_PROCESS_TP.WPFE", "고객센터 회수 적립금", false, "POINT_SETTLEMENT_TP.SWNP", "고객센터 지급 회수"), // WPFE : Withdraw Point Forced Extinction 조정, Abusing 등
        WITHDRAW_VOUCHER_FORCED_EXTINCTION("POINT_PROCESS_TP.WVFE", "고객센터 회수 이용권", false, "POINT_SETTLEMENT_TP.SWVP", "고객센터 지급 회수"), // WVFE : Withdraw Voucher Forced Extinction 조정, Abusing 등
        WITHDRAW_POINT_EVENT_FORCED_EXTINCTION("POINT_PROCESS_TP.WPEF", "이벤트 관리자 회수 적립금", false, "POINT_SETTLEMENT_TP.SWNP", "이벤트 회수"), // WPEF : Withdraw Point Event Forced extinction 조정, Abusing 등
        WITHDRAW_VOUCHER_EVENT_FORCED_EXTINCTION("POINT_PROCESS_TP.WVEF", "이벤트 관리자 회수 이용권", false, "POINT_SETTLEMENT_TP.SWVP", "이벤트 회수"), // WVEF : Withdraw Voucher Event Forced extinction 조정, Abusing 등
        WITHDRAW_POINT_PERIOD_EXPIRE("POINT_PROCESS_TP.WPPE", "유효기간 만료 적립금", false, "POINT_SETTLEMENT_TP.SWNE", "유효기간 만료"), // WPPE : Withdraw Point Period Expire
        WITHDRAW_VOUCHER_PERIOD_EXPIRE("POINT_PROCESS_TP.WVPE", "유효기간 만료 이용권", false, "POINT_SETTLEMENT_TP.SWVE", "유효기간 만료"), // WVPE : Withdraw Voucher Period Expire
        WITHDRAW_POINT_EXTINCTION("POINT_PROCESS_TP.WPEX", "탈퇴 소멸 적립금", false, "POINT_SETTLEMENT_TP.SWNE", "탈퇴 소멸"), // WPEX : Withdraw Point EXtinction 탈퇴 시 적립금 포기에 동의한 경우
        WITHDRAW_VOUCHER_EXTINCTION("POINT_PROCESS_TP.WVEX", "탈퇴 소멸 이용권", false, "POINT_SETTLEMENT_TP.SWVE", "탈퇴 소멸"); // WVEX : Withdraw Voucher EXtinction 탈퇴 시 적립금 포기에 동의한 경우

        private final String code;
        private final String codeName;
        private final boolean isPlus;
        private final String settlementCode;
        private final String displayName;

        /**
         * 환불 시 적립금 처리 적립 유형 코드 리턴
         * @param pointProcessType
         * @return
         */
        public static PointProcessType getRefundPointProcessType(PointProcessType pointProcessType){
            if (PointEnums.PointProcessType.WITHDRAW_VOUCHER_PAYMENT == pointProcessType) {
                return DEPOSIT_VOUCHER_REFUND_ORDER;
            }else{
                return DEPOSIT_POINT_REFUND_ORDER;
            }
        }


        /**
         * code에 대한 enums리턴
         */
        public static PointProcessType findByCode(String code) {
        	return Arrays.stream(PointProcessType.values())
        				.filter(pointProcessType -> pointProcessType.getCode().equals(code))
        				.findAny()
        				.orElse(null);
        }

    }

    //적립금 정산 유형
    @Getter
    @RequiredArgsConstructor
    public enum PointSettlementType implements CodeCommEnum {
        SETTLEMENT_DEPOSIT_NONCASH_POINT_CHARGE("POINT_SETTLEMENT_TP.SDNC", "무상 지급", "Point"), // SDNC : Settlement Deposit Noncash point Charge
        SETTLEMENT_WITHDRAW_NONCASH_POINT_PAYMENT("POINT_SETTLEMENT_TP.SWNP", "무상 사용", "Point"), // SWNP : Settlement Withdraw Noncash point Payment
        SETTLEMENT_WITHDRAW_NONCASH_POINT_EXPIRE("POINT_SETTLEMENT_TP.SWNE", "무상 소멸", "Point"), // SWNE : Settlement Withdraw Noncash point Expire
        SETTLEMENT_DEPOSIT_VOUCHER_POINT_CHARGE("POINT_SETTLEMENT_TP.SDVC", "이용권 지급", "Voucher"), // SDVC : Settlement Deposit Voucher point Charge 이용권으로 적립금 충전
        SETTLEMENT_WITHDRAW_VOUCHER_POINT_PAYMENT("POINT_SETTLEMENT_TP.SWVP", "이용권 사용", "Voucher"), // SWVP : Settlement Withdraw Voucher point Payment 이용권으로 충전된 적립금 사용
        SETTLEMENT_WITHDRAW_VOUCHER_POINT_EXPIRED("POINT_SETTLEMENT_TP.SWVE", "이용권(적립후) 소멸", "Voucher"); // SWVE : Settlement Withdraw Voucher point Expired(after charge)
        //SETTLEMENT_NOT_CHARGED_VOUCHER("SNCV", "이용권(적립전) 소멸"); // SNCV: : Settlement Not Charged Voucher

        private final String code;
        private final String codeName;
        private final String pointType;

        /**
         * 환불 시 적립금 정산 지급 유형 코드 리턴
         * @param pointSettlementType
         * @return
         */
        public static PointSettlementType getRefundPointSettlementType(PointSettlementType pointSettlementType){
            if (SETTLEMENT_WITHDRAW_VOUCHER_POINT_PAYMENT == pointSettlementType) {
                return SETTLEMENT_DEPOSIT_VOUCHER_POINT_CHARGE;
            } else {
                return SETTLEMENT_DEPOSIT_NONCASH_POINT_CHARGE;
            }
        }
    }

    //적립구분
    @Getter
    @RequiredArgsConstructor
    public enum PointPayment implements CodeCommEnum {
    	PROVISION("POINT_PAYMENT_TP.PROVISION", "지급"),
    	DEDUCTION("POINT_PAYMENT_TP.DEDUCTION", "차감");

        private final String code;
        private final String codeName;
    }

	// 적립금 관리
	@Getter
	@RequiredArgsConstructor
	public enum AdminPointCheck implements MessageCommEnum
	{
		USER_POINT_LACK("USER_POINT_LACK", "차감할 적립금이 부족합니다"),
		GROUP_POINT_LACK("GROUP_POINT_LACK", "명 차감할 적립금이 부족합니다"),
        APPROVAL_POINT_LACK("APPROVAL_POINT_LACK", "역할그룹 적립금이 부족하여 승인을 진행할 수 없습니다"),
        APPLY_ADMIN_POINT_FAIL("APPLY_ADMIN_POINT_FAIL", "관리자 적립/차감이 정상적으로 처리되지 않았습니다."),
		USER_ID_FAIL("USER_ID_FAIL", "유효하지 않은 회원ID가 있습니다."),
		INVALID_VALIDITY_DAY_FAIL("INVALID_VALIDITY_DAY_FAIL", "유효하지 않은 적립금 지급일입니다."),
		USER_GROUP_POINT_LACK("USER_GROUP_POINT_LACK", "역할그룹의 적립금 지급한도 부족으로 적립금이 지급되지 않습니다. 지급한도 증액 후 지급하여 주시기 바랍니다."),
		APPROVED_INCREASE_POINT_GROUP("APPROVED_INCREASE_POINT_GROUP", "관리자 월 지급한도 증액 후 승인 가능합니다.");

		private final String code;
		private final String message;
	}

    // 적립금 사용 관련 메시지
    @Getter
    @RequiredArgsConstructor
    public enum PointUseMessage implements MessageCommEnum
    {
        SUCCESS_DEPOSIT_POINT("SUCCESS_DEPOSIT_POINT", "포인트가 적립되었습니다. "),
        SUCCESS_MINUS_POINT("SUCCESS_DEPOSIT_POINT", "포인트가 사용 처리 되었습니다. "),
        USER_POINT_LACK("USER_POINT_LACK", "적립금 잔액이 부족합니다."),
        INVALID_POINT_AMOUNT("INVALID_POINT_AMOUNT", "유효하지 않은 금액이 입력되었습니다."),
        EXCEEDS_REFUNDABLE_POINT("EXCEEDS_REFUNDABLE_POINT", "환불 가능한 적립금 금액을 초과하였습니다."),
        INVALID_REFUND_REQUEST_ORDER_NO("INVALID_REFUND_REQUEST_ORDER_NO", "환불 가능한 주문 정보가 아닙니다."),
        INVALID_REFUND_REQUEST_PARAM("INVALID_REFUND_REQUEST_PARAM", "유효하지 않은 적립금 환불 요청 파라미터 입니다."),
        INVALID_MINUS_REQUEST_PARAM("INVALID_MINUS_REQUEST_PARAM", "유효하지 않은 적립금 사용 요청 파라미터 입니다."),
        INVALID_DEPOSIT_REQUEST_PARAM("INVALID_DEPOSIT_REQUEST_PARAM", "유효하지 않은 적립금 적립 요청 파라미터 입니다."),
        INVALID_POINT_PROCESS_TYPE("INVALID_POINT_PROCESS_TYPE", "유효하지 않은 적립 프로세스 코드 입니다."),
        INVALID_POINT_SERIAL_NUMBER("INVALID_POINT_SERIAL_NUMBER", "유효하지 않은 시리얼 번호 입니다."),
        ERROR_REDEEM_SERIAL_NUMBER("ERROR_REDEEM_SERIAL_NUMBER", "시리얼 번호 사용처리 중 에러가 발생했습니다."),
        EXCEEDS_ISSUE_LIMIT_PER_ACCOUNT("EXCEEDS_ISSUE_LIMIT_PER_ACCOUNT", "계정별 발급 한도를 초과했습니다."),
        EXCEEDS_ISSUE_LIMIT_ACCOUNT("EXCEEDS_ISSUE_LIMIT_ACCOUNT", "전체 발급 한도를 초과했습니다."),
        PARTIAL_DEPOSIT_OVER_LIMIT("PARTIAL_DEPOSIT_OVER_LIMIT", "보유 한도초과로 부분적립 되었습니다."),
        STOP_DEPOSIT_POINT("STOP_DEPOSIT_POINT", "적립 상태 중지"),
        INVALID_EXPIRE_IMMEDIATE_REFUND_REASON("INVALID_EXPIRE_IMMEDIATE_REFUND_REASON", "유효하지 않은 즉시 소멸 가능한 환불 적립금 귀책사유 파라미터 입니다."),
        INVALID_EXPIRE_IMMEDIATE_REFUND_REQUEST_ORDER("INVALID_EXPIRE_IMMEDIATE_REFUND_REQUEST_ORDER", "유효하지 않은 즉시 소멸 가능한 환불 적립금에 주문 정보입니다.");

        private final String code;
        private final String message;
    }

    //포인트 승인 검증
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
    	DUPLICATE_NUMBER("DUPLICATE_NUMBER", "사용중인 이용권 코드입니다.");

    	private final String code;
    	private final String message;
    }

    //마이페이지 노출용 적립금 메시지
    @Getter
    @RequiredArgsConstructor
    public enum BuyerDisplayName implements MessageCommEnum {
        DEPOSIT_POINT_CUSTOMER_SERVICE_REFUND_ORDER("POINT_PROCESS_TP.DPCR", "고객센터 지급");

        private final String code;
        private final String message;
    }

    //마이페이지 노출용 적립금 메시지
    @Getter
    @RequiredArgsConstructor
    public enum PayMethodTypeName implements MessageCommEnum {
        POINT_SINGLE_PAY("POINT_PAY_METHOD_TP.SINGLE_PAY", "단일 지급"),
        POINT_EXCEL_LARGE_PAY("POINT_PAY_METHOD_TP.EXCEL_LARGE_PAY", "엑셀 대량 지급");


        private final String code;
        private final String message;
    }



}
