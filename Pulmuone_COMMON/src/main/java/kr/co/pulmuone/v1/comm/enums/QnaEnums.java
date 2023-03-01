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
 *  1.0    2020. 10. 6.               이원호          최초작성
 * =======================================================================
 * </PRE>
 */
public class QnaEnums {
	//QNA 공개여부
    @Getter
    @RequiredArgsConstructor
    public enum QnaSecretType implements CodeCommEnum {
        OPEN("QNA_SECRET_TP.OPEN", "공개"),
        CLOSE_ADMIN("QNA_SECRET_TP.CLOSE_ADMIN", "비공개 관리자"),
        CLOSE_CUSTOMER("QNA_SECRET_TP.CLOSE_CUSTOMER","비공개 고객");

        private final String code;
        private final String codeName;
    }

    //QNA 처리 상태
    @Getter
    @RequiredArgsConstructor
    public enum QnaStatus implements CodeCommEnum {
        RECEPTION("QNA_STATUS.RECEPTION", "접수"),
        ANSWER_CHECKING("QNA_STATUS.ANSWER_CHECKING", "답변확인중"),
        ANSWER_COMPLETED_1ST("QNA_STATUS.ANSWER_COMPLETED_1ST","답변완료(1차)"),
        ANSWER_COMPLETED_2ND("QNA_STATUS.ANSWER_COMPLETED_2ND","답변완료(2차)"),
        ANSWER_COMPLETED("QNA_STATUS.ANSWER_COMPLETED","답변완료");

        private final String code;
        private final String codeName;
    }

    //상품 QNA 등록 Validation
    @Getter
    @RequiredArgsConstructor
    public enum AddQnaValidation implements MessageCommEnum {
        INTERFACE_ERROR("INTERFACE_ERROR", "인터페이스 에러 발생, 관리자에게 문의하세요.");

        private final String code;
        private final String message;
    }
    
    //상품 QNA 수정 Validation
    @Getter
    @RequiredArgsConstructor
    public enum PutQnaValidation implements MessageCommEnum {
        USER_CECHK_FAIL("USER_CECHK_FAIL", "입력한 사용자가 아님"),
        NOT_QNA("NOT_QNA", "해당하는 문의가 없음"),
        ANSWER_CHECKING("ANSWER_CHECKING","관리자 답변 확인중"),
        ANSWER_COMPLETED("ANSWER_COMPLETED","관리자 답변 완료"),
        ALREADY_CHECK("LOCAL_DEFINE_ALREADY_CHECK","다른 관리자가 답변했습니다. 답변 내용을 확인해 주세요");

        private final String code;
        private final String message;
    }

    //QNA 문의 유형
    @Getter
    @RequiredArgsConstructor
    public enum QnaType implements CodeCommEnum {
    	PRODUCT("QNA_TP.PRODUCT", "상품문의"),
    	ONETOONE("QNA_TP.ONETOONE","1:1문의"),
    	OUTMALL("QNA_TP.OUTMALL","외부몰문의");

        private final String code;
        private final String codeName;
    }


    //상품입점 문의 승인유형
    @Getter
    @RequiredArgsConstructor
    public enum StndPointApprType implements CodeCommEnum {
    	RECEIVE("STAND_PNT_STAT.RECEIVE", "문의접수"),
    	REVIEW("STAND_PNT_STAT.REVIEW", "검토중"),
    	APPROVED("STAND_PNT_STAT.APPROVED", "승인완료"),
    	DENIED("STAND_PNT_STAT.DENIED", "승인반려");

        private final String code;
        private final String codeName;
    }
}
