package kr.co.pulmuone.v1.comm.constants;

public final class GoodsConstants {
    private GoodsConstants(){}

	public static final Long GREEN_JUICE_UR_SUPPLIER_ID = 4L; // 녹즙 공급처 고정값
	public static final Long PDM_UR_SUPPLIER_ID = 5L; // 베이비밀&잇슬림 공급처 고정값
    public static final int LOHAS_IL_CTGRY_ID = 4888; // 운영중인 로하스 대카테고리 고정값
    public static final int NEW_GOODS_MONTH_INTERVAL_1 = 1; // 신상품 조회기준 1개월
    public static final int NEW_GOODS_MONTH_INTERVAL_3 = 3; // 신상품 조회기준 3개월

    //BATCH
    public static final int GOODS_RANKING_ALL_BEST_FLAG = 100;
    public static final int GOODS_RANKING_CATEGORY_BEST_FLAG = 20;

    // 딜상품 구분하는 전시 코드
    public static final String DEAL_GOODS_DISPLAY_INVENTORY_CODE = "21-Main-Nowsale-Gds";

    public static final int GOODS_STOCK_MAX_DDAY = 15; // 상품 재고관리 일자 MAX DDay
    
    public static final int GOODS_STOCK_ADD_DDAY = 14; // 출고일자 Add일

    public static final int GOODS_MAX_STOCK = 9999; // 상품 재고 MAX

    public static final int REGULAR_ARRIVAL_SCHEDULED_DATE_NOW_AFTER_DAY = 4; // 정기배송 도착예정일자 현시점 +4일 이후 부터 선택 가능해야함

    // COUPON
    public static final int MINIMUM_PRICE_FOR_SALEPRICE_APPPOINT = 10000;   // 판매가지정쿠폰을 사용하기위한 일반상품 장바구니최소금액
}
