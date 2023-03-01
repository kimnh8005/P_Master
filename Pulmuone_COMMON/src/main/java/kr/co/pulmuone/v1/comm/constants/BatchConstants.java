package kr.co.pulmuone.v1.comm.constants;

public class BatchConstants {
    private BatchConstants() {}

    public static final int BATCH_FAIL_RETRY_COUNT = 3; //배치실패시 재시도 횟수
    public static final int BATCH_API_CACHE_COUNT = 5; //API Cache 처리 갯수
    public static final int BATCH_DELIVERY_COMPLETE_COUNT = 1000; //배송완료 Batch 처리 갯수

    /* order batch start */
    public static final String ERP_ORDER_KEY = "S-"; // ERP 주문 key
    public static final String ERP_RETURN_KEY = "C-"; // ERP 주문 key
    public static final String ERP_HITOK_ORDER_KEY = "H-"; // ERP 하이톡 주문 key
    public static final String ERP_BABYMEAL_ORDER_KEY = "B-"; // ERP 베이비밀 주문 key
    public static final String ERP_LOHAS_ORDER_KEY = "L-"; // ERP LDS 주문 key
    public static final String BATCH_CONTEXT = "NO"; // ONTEXT 값
    public static final String BUSINESS_CODE = "100"; // 사업부 코드
    public static final String SUBJECT_UNIT = "EA"; // 품목단위
    public static final int ORGA_ORGANIZATION_CODE = 3836; // 주문 조직코드
    public static final int FOOD_ORGANIZATION_CODE = 165; // 매출 조직코드
    public static final int PARTNERS_ORGANIZATION_CODE = 7870; // 협력업체 조직코드
    public static final int DIRECT_PARTNERS_ORGANIZATION_CODE = 7933; // 산지직송 협력업체 조직코드
    public static final String CJ_ACCOUNT_CODE = "O999"; // CJ 거래처 코드
    public static final int BABYMEAL_DELIVERY = 0; // 베이비밀 1회배달수량 기본값
    public static final int HITOK_SHIP_TO_ORG_ID = 663171; // 하이톡 자사몰 납품처 ID
    public static final String ORGA_ETC_ORDER_ACCOUNT_CODE = "O02000"; // 올가 기타주문(산지직송 매장코드)
    public static final String ORGA_ETC_ORDER_ACCOUNT_NAME = "쇼핑몰"; // 올가 기타주문(산지직송 매장명)
    public static final String PREFIX_ALTERNATE_DELIVERY = "대체배송 "; // 대체배송 prefix
    public static final int CJ_RECIPIENT_NAME_MAX_SIZE = 20; // CJ 수령인이름 최대 사이즈
    /* order batch end */

    /* TrackingNumberOrder batch start */
    // 송장정보 조회시 기본값
    public static final String ITF_DLV_FLG 				= "N"; 		// 송장처리 인터페이스 수신여부
    public static final int ITF_DLV_DATE_FROM_TERM 		= -15; 		// 송장 조회기간 From (15일전)
    public static final String ITF_DLV_DATE_FROM_TIME 	= "000000"; // 송장 조회기간 From시간 (00:00:00)
    public static final String ITF_DLV_DATE_TO_TIME 	= "235959"; // 송장 조회기간 To시간 (23:59:59)
    public static final String ETC 	= "etc";
    /* TrackingNumberOrder batch end */

    /* UnreleasedOrder batch start */
    // 미출정보 조회시 기본값
    public static final String ITF_MIS_FLG 				= "N"; 		// 미출 인터페이스 수신여부
    public static final int ITF_MIS_DATE_FROM_TERM 		= -15; 		// 미출 조회기간 From (15일전)
    public static final String ITF_MIS_DATE_FROM_TIME 	= "000000"; // 미출 조회기간 From시간 (00:00:00)
    public static final String ITF_MIS_DATE_TO_TIME 	= "235959"; // 미출 조회기간 To시간 (23:59:59)
    /* UnreleasedOrder batch end */

    /* store batch start */
    public static final String ERP_ITEM_NO_KEY = "shpCd";
    public static final String ERP_FLAG_KEY = "itfFlg";
    public static final String ERP_DELIVERY_AREA_KEY = "dlvAreCd";
    public static final String ERP_SCHEDULE_NO_KEY = "dlvSte";
    public static final String ERP_UNDELIVER_DT_KEY  = "schShiDat";
    public static final String ERP_STORE_TYPE_KEY = "srcSvr";
    public static final String ERP_UPDATE_DATE_KEY = "updDat_toChar_YYYYMMDD";
    /* store batch end */

    /* 발주 */
    public static final String ERP_ITEM_CD = "itmCd";

    /* 재고 */
    public static final String ERP_ITEM_NO = "erpItmNo";
    public static final String STR_KEY = "strKey";//고객사코드

    /* 주문상태 변경 */
    public static final String BUY_FINALIZED_DAY_KEY 		= "OD_SELL_CONFIRM_DAY";		// 자동 구매확정기간 설정 컬럼
    public static final String DELIVERY_COMPLETE_DAY_KEY 	= "OD_SHIPPING_AUTO_END_DAY";	// 자동 배송완료기간 설정 컬럼

    public static final int IL_ITEM_STOCK_BATCH_NO = 7;
}
