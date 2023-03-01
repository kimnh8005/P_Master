package kr.co.pulmuone.v1.comm.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

public class ClaimEnums {

    /**
     * 귀책사유
     */
    @Getter
    @RequiredArgsConstructor
    public enum ReasonAttributableType implements CodeCommEnum {
        COMPANY("COMPANY" , "판매자귀책" ,"S"),
        BUYER("BUYER"	  , "구매자귀책" ,"B")
        ;

        private final String code;
        private final String codeName;
        private final String type;

        public static ClaimEnums.ReasonAttributableType findByCode(String code) {
            return Arrays.stream(ClaimEnums.ReasonAttributableType.values())
                    .filter(reasonAttributableType -> reasonAttributableType.getCode().equals(code))
                    .findAny()
                    .orElse(null);
        }
    }

    /**
     * 반품회수여부
     */
    @Getter
    @RequiredArgsConstructor
    public enum ReturnsYn implements CodeCommEnum {
        RETURNS_YN_Y("Y"  , "회수"),
        RETURNS_YN_N("N"  , "회수안함")
        ;

        private final String code;
        private final String codeName;
    }

    /**
     * 프론트요청 구분
     */
    @Getter
    @RequiredArgsConstructor
    public enum ClaimFrontTp implements CodeCommEnum {
    	BOS("0"		, "BOS"),
    	FRONT("1"	, "FRONT"),
        BATCH("2"	, "BATCH")
    	;

    	private final String code;
    	private final String codeName;
    }

    /**
     * 프론트 취소 구분
     */
    @Getter
    @RequiredArgsConstructor
    public enum ClaimGoodsChangeType implements CodeCommEnum {
    	ALL_CANCEL("0"	, "전체취소"),
    	PART_CANCEL("1"	, "부분취소")
    	;

    	private final String code;
    	private final String codeName;
    }

    /**
     * 프론트 취소 구분
     */
    @Getter
    @RequiredArgsConstructor
    public enum ClaimReasonMsg implements CodeCommEnum {
    	CLAIM_REASON_DI("DI"	, "이미 출고되어 취소가 불가합니다."),
    	CLAIM_REASON_CW("CW"	, "취소 철회 요청"),
    	CLAIM_REASON_CE("CE"	, "취소 거부"),
    	CLAIM_REASON_RW("RW"	, "반품 철회 요청"),
    	CLAIM_REASON_RE("RE"	, "반품 거부"),
    	CLAIM_UNRELEASED_REASON_RE("RE"	, "미출데이터 수신 반품 거부"),
    	;

    	private final String code;
    	private final String codeName;
    }

    /**
     * 택배 접수구분 (01:일반,02:반품)
     */
    @Getter
    @RequiredArgsConstructor
    public enum DeliveryReceiptType implements CodeCommEnum {
        NORMAL("01"	, "일반"),
        RETURN("02"	, "반품")
        ;

        private final String code;
        private final String codeName;
    }

    /**
     * 택배 작업구분코드	01:일반,02:교환,03:A/S
     */
    @Getter
    @RequiredArgsConstructor
    public enum DeliveryWorkTypeCode implements CodeCommEnum {
        NORMAL("01"	, "일반"),
        EXCHANGE("02"	, "교환"),
        AFTER_SALES_SERVICE("03"	, "A/S")
        ;

        private final String code;
        private final String codeName;
    }

    /**
     * 택배 요청구분코드	01:요청,02:취소
     */
    @Getter
    @RequiredArgsConstructor
    public enum DeliveryRequestTypeCode implements CodeCommEnum {
        REQUEST("01"	, "요청"),
        CANCEL("02"	, "취소")
        ;

        private final String code;
        private final String codeName;
    }

    /**
     * 택배 정산구분코드	01:계약운임,02:자료운임
     */
    @Getter
    @RequiredArgsConstructor
    public enum DeliveryCalculateTypeCode implements CodeCommEnum {
        CONTRACT_FARE("01"	, "계약운임"),
        MATERIAL_FARE("02"	, "자료운임")
        ;

        private final String code;
        private final String codeName;
    }

    /**
     * 택배 운임구분코드	01:선불,02:착불,03:신용
     */
    @Getter
    @RequiredArgsConstructor
    public enum DeliveryFareTypeCode implements CodeCommEnum {
        PREPAYMENT("01"	, "선불"),
        CASH_ON_DELIVERY("02"	, "착불"),
        CREDIT("03"	, "신용")
        ;

        private final String code;
        private final String codeName;
    }

    /**
     * 택배 계약품목코드01:일반품목
     */
    @Getter
    @RequiredArgsConstructor
    public enum DeliveryContractItemTypeCode implements CodeCommEnum {
        GENERAL_ITEM("01"	, "일반품목")
        ;

        private final String code;
        private final String codeName;
    }

    /**
     * 택배 박스타입코드	01:극소,02:소,03:중,04:대,05:특대
     */
    @Getter
    @RequiredArgsConstructor
    public enum DeliveryBoxTypeCode implements CodeCommEnum {
        VERY_SMALL("01"	, "극소"),
        SMALL("02"	, "소"),
        MEDIUM("03"	, "중"),
        LARGE("04"	, "대"),
        VERY_LARGE("05"	, "특대")
        ;

        private final String code;
        private final String codeName;
    }

    @Getter
    @RequiredArgsConstructor
    public enum ClaimValidationResult implements MessageCommEnum {
        SUCCESS("SUCCESS", "성공"),
        FAILED("FAILED", "실패"),
        NO_CHANGE_CLAIM_STATUS("NO_CHANGE_CLAIM_STATUS", "변경 클레임 상태가 없습니다."),
        NO_INCOM_COMPLETE("NO_IC", "주문상태가 결제완료 상태가 아닙니다."),
        NO_DELIVERY_READY("NO_DR", "주문상태가 배송준비중 상태가 아닙니다."),
        NO_DELIVERY_ING_OR_DELIVERY_COMPLETE_OR_BUY_FINALIZED("NO_DELIVERY_ING_OR_DELIVERY_COMPLETE_OR_BUY_FINALIZED", "주문상태가 배송중 또는 배송완료 또는 구매확정 상태가 아닙니다."),
        NO_CANCEL_APPLY("NO_CA", "클레임상태가 취소요청 상태가 아닙니다."),
        NO_RETURN_APPLY("NO_RA", "클레임상태가 반품요청 상태가 아닙니다."),
        NO_RETURN_ING("NO_RI", "클레임상태가 반품승인 상태가 아닙니다."),
        ALREADY_CANCEL_APPLY("ALREADY_CA", "이미 취소요청된 상태입니다."),
        ALREADY_CANCEL_COMPLETE("ALREADY_CC", "이미 취소완료된 상태입니다."),
        ALREADY_CANCEL_DENY_DEFE("ALREADY_CE", "이미 취소거부된 상태입니다."),
        ALREADY_CANCEL_WITHDRAWAL("ALREADY_CW", "이미 취소철회된 상태입니다."),
        ALREADY_RETURN_APPLY("ALREADY_RA", "이미 반품요청된 상태입니다."),
        ALREADY_RETURN_ING("ALREADY_RI", "이미 반품승인된 상태입니다."),
        ALREADY_RETURN_DEFER("ALREADY_RF", "이미 반품보류된 상태입니다."),
        ALREADY_RETURN_COMPLETE("ALREADY_RC", "이미 반품완료된 상태입니다."),
        ALREADY_RETURN_DENY_DEFER("ALREADY_RE", "이미 반품거부된 상태입니다."),
        ALREADY_RETURN_WITHDRAWAL("ALREADY_RW", "이미 반품철회된 상태입니다."),
        NO_CLAIM_CNT("NO_CLAIM_CNT", "클레임 처리할 수량이 없습니다."),
        NO_CLAIM_GOODS("NO_CLAIM_GOODS", "클레임 처리대상 상품이 없습니다.")
        ;

        private final String code;
        private final String message;

        public static ClaimEnums.ClaimValidationResult findByCode(String code) {
            return Arrays.stream(ClaimEnums.ClaimValidationResult.values())
                    .filter(ClaimValidationResult -> ClaimValidationResult.getCode().equals(code))
                    .findAny()
                    .orElse(null);
        }
    }

    @Getter
    @RequiredArgsConstructor
    public enum RecallType implements CodeCommEnum {
        RECALL_SUCCESS("RECALL_SUCCESS"	, "연동 성공"),
        RECALL_FAIL("RECALL_FAIL"	, "연동 실패"),
        RECALL_NONE("RECALL_NONE"	, "연동 안함")
        ;

        private final String code;
        private final String codeName;
    }

}
