package kr.co.pulmuone.v1.comm.constants;

public final class Constants {
    private Constants(){}

	public static final String ST_SHOP_ID = "1"; // 상점관리 KEY

    public static final String GB_LANG_ID = "1"; // 국가언어코드 KEY (1.한국어)

	public static final String[] MASTER_AUTH_ST_ROLE_TP_ID = {"8"}; // 마스터 권한 ST_ROLE_TP_ID

	// ### 작업 Start : YoonHyunhee ###
	public static final String ARRAY_SEPARATORS = "∀";
	public static final String ARRAY_LINE_BREAK_OR_COMMA_SEPARATORS = "\n|,";
	public static final long BATCH_CREATE_ID = 0;
	// ### 작업 End : YoonHyunhee ###

    public static final int DEPOSIT_MAXIMUM_POSSIBLE_POINT = 500000; //보유금 가능한 최대 적립금

    public static final String PULMUONE_TRANSFER_POINTS_COMMENT = "풀무원 회원 포인트 이관";

    public static final String SITE_NO_PULMUONE = "0000000000";     // 풀샵 siteno

    public static final String PULMUONE_SETTLEMENT_CORPORATION_CODE = "6291"; //풀무원 적립금 정산 인사 조직코드 PM_COUPON_POINT_SHARE_ORGANIZATION.

    public static final int PRIVIUS_PULMUONE_MEMBER_POINT_EXPIRE_DAY = 365; //풀무원 기존 회원 적립금 이관 시 적립금 사용 만료일

    public static final String ORGA_TRANSFER_POINTS_COMMENT = "올가 회원 포인트 이관";

    public static final String SITE_NO_ORGA = "0000200000";         // 올가 siteno

    public static final String ORGA_SETTLEMENT_CORPORATION_CODE = "9205"; //올가 적립금 정산 회계 조직코드 PM_COUPON_POINT_SHARE_ORGANIZATION.

    public static final int PRIVIUS_ORGA_MEMBER_POINT_EXPIRE_DAY = 365; //올가 기존 회원 적립금 이관 시 적립금 사용 만료일

    public static final int CUSTOMER_SERVICE_REFUND_ORDER_POINT_EXPIRE_DAY = 30; //관리자 CS환불 적립금 사용 만료일

    public static final String ONLINE_MARKETING_SETTLEMENT_CORPORATION_CODE = "6291"; //온라인마케팅팀 정산 회계 조직코드

    public static final String STRING_ARRAY_SEPARATORS_DOT = "\\."; // String 문자열 split 구분자 dot

    public static final int ERP_API_CUT_ADDR_ALL_MAX_BYTE_SIZE = 240; // 중계서버 API 전체 주소 MAX byte size
    public static final int ERP_API_CUT_ADDR_DETAIL_MAX_BYTE_SIZE = 100; // 중계서버 API 상세 주소 MAX byte size
    public static final int ERP_API_CUT_NAME_MAX_BYTE_SIZE = 50; // 중계서버 API 이름 MAX byte size
    public static final int ERP_API_CUT_DLV_NAME_MAX_BYTE_SIZE = 30; // 중계서버 API 기타주문 수령자명 MAX byte size
    public static final int ERP_API_CUT_EMAIL_MAX_BYTE_SIZE = 30; // 중계서버 API 이메일 MAX byte size

	/* ECS DATA 정의*/
    public final static String RECEIPT_ROOT_ESHOP_HP = "00004";
    public final static String SEC_CODE = "11850"; //부문코드값 (풀무원_NFB Region BU)

	public static final String[] NOT_AVAILABLE_LOGIN_ID = { "admin", "test", "pulmuone", "orga", "eatslim", "babymeal",
			"lohas", "system", "operator", "manager", "bos", "pmo", "fuck", "bitch" };

	/* 스캐출 */
	public final static String SCHDULE_DELIVERY_DATE_CODE = "KOR";

	/* 배치 등록자 ID */
	public final static long BATCH_CREATE_USER_ID = 9000000000000000000L;

	/* 게스트 등록자 ID */
	public final static long GUEST_CREATE_USER_ID = 9100000000000000000L;

    /* 가상계좌입금 등록자 ID */
    public final static long VIRTUAL_ACCOUNT_USER_ID = 9200000000000000000L;

    // 발주용 상수 선언
    public static final String FRO_ORG_ID  = "7870";            // (ERP 필요정보) 재고이동조직
    public static final String TO_ORG_ID  = "7870";             // (ERP 필요정보) 재고보관조직
    public static final String TO_SUB_INV_CD = "O1098";         // (ERP 필요정보) 재고보관창고

    // 발주유형값
    public static final String SIPU = "SIPU";       // 풀무원식품 생산발주
    public static final String SIMO = "SIMO";       // 풀무원식품 이동발주
    public static final String OGMO = "OGMO";       // 올가 이동발주
    public static final String OGPUR1 = "OGPUR1";   // 올가 R1발주
    public static final String OGPUR2 = "OGPUR2";   // 올가 R2발주
    public static final String CJMO = "CJMO";       // CJ 물류 이동

    // 택배접수
    public static final int COMBINED_PACKING_ORDER = 1;       // 합포장순번
    public static final int DELIVERY_BOX_QUANTITY = 1;       // 택배박스수량
    public static final String CJ_DELIVERY_RETURN_ID = "PULMUONE";    // CJ택배반품ID

    // 미적립 적립금
    public final static String REDEPOSIT_NOT_ISSUE_POINT_COMMENT = "미지급으로 인한 재적립 처리";

    // PG 주문ODID 구분값
    public static final String ORDER_ODID_DIV_PAYMENT_MASTER = "PMID_";    // OD_PAYMENT_MASTER_ID

    // PG 추가결제 구분값
    public static final String ORDER_ODID_DIV_ADD_PAYMENT_MASTER = "_ADD";    // 추가결제 OD_PAYMENT_MASTER_ID 구분자

    // PG 이메일 대체값
    public static final String NOREPLY_EMAIL = "noreply@pulmuone.com";


    // 정책키
    public static final String PULSHOP_GOODS_MAPPING_KEY = "PULSHOP_MAPPING_USE_YN";    // 상품 맵핑 PS_CONFIG KEY
    public static final String ORGA_DP_BRAND_KEY = "ORGA_DP_BRAND";                     // 올가 전시브랜드 KEY
    public static final String ORGA_PB_DP_BRAND_KEY = "ORGA_PB_DP_BRAND";               // 올가 PB 전시브랜드 KEY
    public static final String EVENT_CICD_KEY = "EVENT_CICD_KEY";                       // 이벤트 CICD 적용 KEY
    public static final String POINT_MASTER_ROLL_IDS = "POINT_MASTER_ROLL_IDS";           // 적립금 마스터 롤 ID
    public static final String APPLY_DELIVERY_AREA_POLICY_DATE = "APPLY_DELIVERY_AREA_POLICY_DATE"; // 신규 테이블 배송권역정책(도서산간, 배송불가권역) 적용 날짜

    // CS환불 기준 금액
    public static final int CS_REFUND_STANDARD_PRICE = 10000;

    // 관리자 LEVEL 1 권한
    public static final String ADMIN_LEVEL_1_AUTH_ST_ROLE_TP_ID = "140";

    // 엑셀다운로드 유형
    public static final String BOS_EXCEL_DOWNLOAD_TYPE_NORMAL = "NOR";
    public static final String BOS_EXCEL_DOWNLOAD_TYPE_DETAIL = "DTL";

    // 외부몰 주문자 주문복사시 이메일주소 onlinecs@pulmuone.com로 등록
    public static final String PG_SERVICE_TEMPORARY_EMAIL = "onlinecs@pulmuone.com";

    // 샵라이브 JWT 알고리즘
    public static final String SHOPLIVE_ALGORITHM = "HS256";
}
