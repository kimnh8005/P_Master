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
public class OrderCsEnums {
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

    // CS환불 구분
    @Getter
    @RequiredArgsConstructor
    public enum CsRefundTp implements CodeCommEnum {
        PAYMENT("CS_REFUND_TP.PAYMENT_PRICE_REFUND", "예치금환불"),
        POINT("CS_REFUND_TP.POINT_PRICE_REFUND", "적립금환불"),
        ;

        private final String code;
        private final String codeName;

        public static OrderCsEnums.CsRefundTp findByCode(String code) {
            return Arrays.stream(OrderCsEnums.CsRefundTp.values())
                    .filter(refundTp -> refundTp.getCode().equals(code))
                    .findAny()
                    .orElse(null);
        }
    }

    // CS환불 승인 상태
    @Getter
    @RequiredArgsConstructor
    public enum CsRefundApprCd implements CodeCommEnum {
        SAVE("CS_REFUND_APPR_CD.SAVE", "저장", "APPR_STAT.SAVE"),
        REQUEST("CS_REFUND_APPR_CD.REQUEST", "승인요청", "APPR_STAT.REQUEST"),
        CANCEL("CS_REFUND_APPR_CD.CANCEL", "요청철회", "APPR_STAT.CANCEL"),
        DENIED("CS_REFUND_APPR_CD.DENIED", "승인반려", "APPR_STAT.DENIED"),
        APPROVED("CS_REFUND_APPR_CD.APPROVED", "승인완료", "APPR_STAT.APPROVED_BY_SYSTEM"),
        ;

        private final String code;
        private final String codeName;
        private final String apprCode;

        public static OrderCsEnums.CsRefundApprCd findByCode(String code) {
            return Arrays.stream(OrderCsEnums.CsRefundApprCd.values())
                    .filter(refundApprCd -> refundApprCd.getCode().equals(code))
                    .findAny()
                    .orElse(null);
        }
    }

    // CS환불 에러
    @Getter
    @RequiredArgsConstructor
    public enum CsRefundError implements MessageCommEnum {
        SUCCESS                     ("SUCCESS"                  , "CS환불 정보 등록 성공"),
        NO_LOGIN                    ("NO_LOGIN"                 , "로그인 후 등록가능합니다."),
        NO_GOODS_LIST               ("NO_GOODS_LIST"            , "등록가능 상품이 존재하지 않습니다."),
        CS_INFO_REGIST_FAIL         ("CS_INFO_REGIST_FAIL"      , "CS정보 등록에 실패했습니다."),
        CS_INFO_UPDATE_FAIL         ("CS_INFO_UPDATE_FAIL"      , "CS정보 수정에 실패했습니다."),
        CS_INFO_DETL_REGIST_FAIL    ("CS_INFO_DETL_REGIST_FAIL" , "CS상세정보 등록에 실패했습니다."),
        CS_INFO_PG_REGIST_FAIL      ("CS_INFO_PG_REGIST_FAIL"   , "CS상세 금액정보 등록에 실패했습니다."),
        NO_ACCOUNT_INFO             ("NO_ACCOUNT_INFO"          , "환불 계좌정보를 확인해주세요."),
        ACCOUNT_INFO_REGIST_FAIL    ("ACCOUNT_INFO_REGIST_FAIL" , "환불 계좌정보 등록에 실패했습니다."),
        PAYMENT_FAIL                ("PAYMENT_FAIL"             , "무통장 계좌 송금에 실패했습니다."),
        NO_APPR_USER_ID             ("NO_APPR_USER_ID"          , "승인관리자를 지정해주세요."),
        INVALID_APPROCE_CD          ("INVALID_APPROCE_CD"       , "유효하지않은 승인코드 입니다."),
        ;

        private final String code;
        private final String message;

        public static OrderCsEnums.CsRefundError findByCode(String code) {
            return Arrays.stream(OrderCsEnums.CsRefundError.values())
                    .filter(item -> item.getCode().equals(code))
                    .findAny()
                    .orElse(null);
        }
    }

    // CS환불 구분
    @Getter
    @RequiredArgsConstructor
    public enum CsRefundApproveCd implements CodeCommEnum {
        APPROVED("CS_REFUND_APPR_CD.APPROVED", "승인")
        ;

        private final String code;
        private final String codeName;
    }

}