package kr.co.pulmuone.v1.comm.constants;


public class ApiConstants {
    private ApiConstants() { }

	public static final int WAYBILL_NUMBER_LENGTH = 12; // 운송장번호 길이

	public static final String IF_GOODS_SRCH = "IF_GOODS_SRCH"; // 상품검색

	public static final String IF_GOODS_FLAG = "IF_GOODS_FLAG"; // 상품검색완료

	public static final String IF_PRICE_SRCH = "IF_PRICE_SRCH"; // 가격검색

	public static final String IF_PRICE_FLAG = "IF_PRICE_FLAG"; // 가격검색조회완료

	public static final String IF_ORGASHOP_STOCK_SRCH = "IF_ORGASHOP_STOCK_SRCH"; // 올가매장상품정보 조회

	public static final String IF_ORGASHOP_STOCK_FLAG = "IF_ORGASHOP_STOCK_FLAG"; // 올가매장상품정보 조회 완료

	public static final String IF_CUSTORD_SRCH = "IF_CUSTORD_SRCH"; // 올가매장주문정보 조회 완료

	public static final String IF_BATCH = "IF_BATCH"; // 배치 실행 여부

	//NCP SMS Interface
	public static final int NCP_SMS_LENGTH = 80;	//SMS 길이


}
