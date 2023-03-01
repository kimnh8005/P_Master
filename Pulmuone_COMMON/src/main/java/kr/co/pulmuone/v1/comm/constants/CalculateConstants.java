package kr.co.pulmuone.v1.comm.constants;

public class CalculateConstants {
    private CalculateConstants() {}

    /* 임직원 지원금 정산 start */
    public static final String CORPORATION_CODE = "FF"; // 법인 구분
    public static final String INVOICE_BRIEF = "복리후생비-이샵"; // 송장 적요
    public static final String SUPPLIER_BUSINESS_NUMBER = "2009123104"; // 공급자사업자번호
    public static final String SUPPLIER_NAME = "E-shop[임직원정산용]"; // 공급자명
    public static final String PAYMENT_METHOD = "CHECK"; // 지급방법
    public static final String PAYMENT_TERMS = "익월25일"; // 결제조건
    public static final String PAYMENT_GROUP = "현금출금"; // 지급그룹
    public static final String CREDIT_ACCOUNT_CODE = "214411"; // 대변 계정코드
    public static final String CREDIT_DIVISION = "000000"; // 대변 사업부
    public static final String DEBIT_DIVISION = "000000"; // 차변 사업부
    /* 임직원 지원금 정산 end */

    /* 매출확정 조회 start */
    public static final String ITF_SALES_FLG = "N"; // 인터페이스 수신여부
    public static final int ITF_SALES_DATE_FROM_TERM = -32; // 조회기간 From (32일전)
    public static final String ITF_SALES_DATE_FROM_TIME = "000000"; // 조회기간 From시간 (00:00:00)
    public static final String ITF_SALES_DATE_TO_TIME = "235959"; // 조회기간 To시간 (23:59:59)
    /* 매출확정 조회 end */

}
