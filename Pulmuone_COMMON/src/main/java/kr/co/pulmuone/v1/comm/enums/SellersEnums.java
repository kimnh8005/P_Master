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
 *  1.0    2021. 01. 23.               이명수        주문상태 추가 (OrderStatus)
 * =======================================================================
 * </PRE>
 */
public class SellersEnums {
     /**
      * 판매처 (외부몰) 그룹
      * 공통코드 : SELLERS_GROUP
      */
    @Getter
    @RequiredArgsConstructor
    public enum SellersGroupCd implements CodeCommEnum {
        MALL("SELLERS_GROUP.MALL", "통합몰"),
        DIRECT_MNG("SELLERS_GROUP.DIRECT_MNG", "직관리"),
        VENDOR("SELLERS_GROUP.VENDOR", "벤더"),
        DIRECT_BUY("SELLERS_GROUP.DIRECT_BUY", "직매입"),
        ;

        private final String code;
        private final String codeName;

    }



    // 주문등록경로
    @Getter
    @RequiredArgsConstructor
    public enum OrderRoute implements CodeCommEnum {
        FRONT_CART("ORDER_ROUTE.FRONT_CART", "장바구니주문"),
        BOS_CREATE("ORDER_ROUTE.BOS_CREATE", "주문생성 업로드"),
        BOS_SELLER("ORDER_ROUTE.BOS_SELLER", "판매처별 업로드"),
        BOS_COPY("ORDER_ROUTE.BOS_COPY", "주문복사"),
        EASY_ADMIN("ORDER_ROUTE.EASY_ADMIN", "이지어드민 주문"),
        REGULAR_DELI("ORDER_ROUTE.REGULAR_DELI", "정기배송 주문"),
        ;

        private final String code;
        private final String codeName;
    }

	// 정기배송 배송 주기
    @Getter
    @RequiredArgsConstructor
    public enum RegularShippingCycle implements CodeCommEnum{
    	WEEK1("REGULAR_SHIPPING_CYCLE_TYPE.WEEK1", "1주일에 한 번"),
    	WEEK2("REGULAR_SHIPPING_CYCLE_TYPE.WEEK2", "2주일에 한 번"),
    	WEEK3("REGULAR_SHIPPING_CYCLE_TYPE.WEEK3", "3주일에 한 번"),
    	WEEK4("REGULAR_SHIPPING_CYCLE_TYPE.WEEK4", "4주일에 한 번"),
        ;

        private final String code;
        private final String codeName;

    }

    // 정기배송 배송 기간
    @Getter
    @RequiredArgsConstructor
    public enum RegularShippingCycleTerm implements CodeCommEnum{
    	MONTH1("REGULAR_SHIPPING_CYCLE_TERM_TYPE.MONTH1", "1개월"),
    	MONTH2("REGULAR_SHIPPING_CYCLE_TERM_TYPE.MONTH2", "2개월"),
    	MONTH3("REGULAR_SHIPPING_CYCLE_TERM_TYPE.MONTH3", "3개월"),
    	MONTH4("REGULAR_SHIPPING_CYCLE_TERM_TYPE.MONTH4", "4개월"),
    	MONTH5("REGULAR_SHIPPING_CYCLE_TERM_TYPE.MONTH5", "5개월"),
    	MONTH6("REGULAR_SHIPPING_CYCLE_TERM_TYPE.MONTH6", "6개월"),
        ;

        private final String code;
        private final String codeName;

    }


    // 현금 영수증
    @Getter
    @RequiredArgsConstructor
    public enum CashReceipt implements CodeCommEnum{
    	DEDUCTION("CASH_RECEIPT.DEDUCTION", "소득공재용", "0", "1"),
    	PROOF("CASH_RECEIPT.PROOF", "지출증빙", "1", "2");

        private final String code;
        private final String codeName;
        private final String kcpCode;
        private final String inicisCode;
    }

    // 결제 종류
    @Getter
    @RequiredArgsConstructor
    public enum PaymentType implements CodeCommEnum{
    	CARD("PAY_TP.CARD", "카드"),
    	BANK("PAY_TP.BANK", "계좌이체"),
    	VIRTUAL_BANK("PAY_TP.VIRTUAL_BANK", "가상계좌"),
    	FREE("PAY_TP.FREE", "무료결제");

        private final String code;
        private final String codeName;

        public static PaymentType findByCode(String code) {
 			return Arrays.stream(PaymentType.values())
 		            .filter(paymentType -> paymentType.getCode().equals(code))
 		            .findAny()
 		            .orElse(null);
        }
    }

    // 정기 결제 카드 등록시 에러 코드 정의
    @Getter
    @RequiredArgsConstructor
    public enum ApplyRegularBatchKey implements MessageCommEnum {
        NEED_LOGIN("NEED_LOGIN", "로그인필요")
        ;

        private final String code;
        private final String message;
    }

    // 주문 완료 가상계좌 정보 조회
    @Getter
    @RequiredArgsConstructor
    public enum virtualAccount implements MessageCommEnum {
        NOT_SAME_USER("NOT_SAME_USER", "본인 주문 아님")
        ;

        private final String code;
        private final String message;
    }

    // 일괄송장 엑셀업로드 에러메시지
    @Getter
    @RequiredArgsConstructor
    public enum OrderBulkTrackingExcelUploadErrMsg implements MessageCommEnum {

    	VALUE_EMPTY("VALUE_EMPTY", "필수값이 없음"),
    	ORDER_DETL_ID_EMPTY("ORDER_DETL_ID_EMPTY", "주문상세 번호가 유효하지 않음"),
    	LOGISTICS_CD_EMPTY("LOGISTICS_CD_EMPTY", "택배사 코드가 유효하지 않음");

    	private final String code;
  		private final String message;
    }

    //PG 종류 공통코드
    @Getter
    @RequiredArgsConstructor
    public enum PgService implements CodeCommEnum{
    	CARD("PG_SERVICE.CARD", "카드"),
    	BANK("PG_SERVICE.BANK", "계좌이체"),
    	VIRTUAL_BANK("PAY_TP.VIRTUAL_BANK", "가상계좌"),
    	FREE("PAY_TP.FREE", "무료결제");

        private final String code;
        private final String codeName;

        public static PaymentType findByCode(String code) {
 			return Arrays.stream(PaymentType.values())
 		            .filter(paymentType -> paymentType.getCode().equals(code))
 		            .findAny()
 		            .orElse(null);
        }
    }

    // 주문상태 G : 결제, F : 환불 , A : 추가
    @Getter
    @RequiredArgsConstructor
    public enum PayType implements CodeCommEnum {
        G("G", "결제"),
        F("F", "환불"),
        A("A", "추가"),
        ;

        private final String code;
        private final String codeName;

    }

    // 택배사코드 CJ : CJ택배, LOTTE : LOTTE택배
    @Getter
    @RequiredArgsConstructor
    public enum LogisticsCd implements CodeCommEnum {
        CJ("CJ", "CJ택배"),
        LOTTE("LOTTE", "LOTTE택배"),
        ;

        private final String code;
        private final String codeName;

    }

    // 외부몰 등록 메시지 코드
    @Getter
    @RequiredArgsConstructor
    public enum SellersMessage implements MessageCommEnum {
    	DUPLICATE_SELLER_NAME("DUPLICATE_SELLER_NAME", "동일한 외부몰명이 등록 되어있습니다.")
    	;

    	private final String code;
  		private final String message;
    }
}