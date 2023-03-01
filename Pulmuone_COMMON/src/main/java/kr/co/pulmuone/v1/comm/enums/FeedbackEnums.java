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
 *  1.0    2020.10.20.               이원호          최초작성
 * =======================================================================
 * </PRE>
 */
public class FeedbackEnums {
	//후기 상품유형
    @Getter
    @RequiredArgsConstructor
    public enum FeedbackProductType implements CodeCommEnum {
        NORMAL("FEEDBACK_PRODUCT_TP.NORMAL", "일반상품후기"),
        TESTER("FEEDBACK_PRODUCT_TP.TESTER", "체험단상품후기");

        private final String code;
        private final String codeName;
    }

    //후기 유형
    @Getter
    @RequiredArgsConstructor
    public enum FeedbackType implements CodeCommEnum {
        NORMAL("FEEDBACK_TP.NORMAL", "일반후기"),
        PHOTO("FEEDBACK_TP.PHOTO", "포토후기"),
        PREMIUM("FEEDBACK_TP.PREMIUM","프리미엄후기"),
        NONE("NONE","후기유형없음");

        private final String code;
        private final String codeName;
    }

    //후기 유형
    @Getter
    @RequiredArgsConstructor
    public enum AddFeedbackMessage implements MessageCommEnum {
        FEEDBACK_EXIST("FEEDBACK_EXIST", "후기 기존 있음"),
        FEEDBACK_TYPE_ERROR("FEEDBACK_TYPE_ERROR", "후기 유형 오류"),
        DEPOSIT_POINT_ERROR("DEPOSIT_POINT_ERROR", "적립금 적립 오류")
        ;

        private final String code;
        private final String message;
    }

    //후기 합배송 유형
    @Getter
    @RequiredArgsConstructor
    public enum PackType implements CodeCommEnum {
        NORMAL("NORMAL", "일반"),
        PACKAGE("PACKAGE", "묶음상품"),
        EXHIBIT("EXHIBIT","골라담기"),
        GREENJUICE("GREENJUICE","녹즙");

        private final String code;
        private final String codeName;
    }
    
}
