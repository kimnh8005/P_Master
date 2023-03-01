package kr.co.pulmuone.v1.comm.constants;

import java.util.Arrays;
import java.util.List;

public final class PromotionConstants {
    private PromotionConstants(){}

	public static final long EXPIRED_DATE = 30; // 마이페이지 - 적립금 조회 - 만료일자

	public static final String COOKIE_AD_EXTERNAL_CODE_KEY = "adExternalCd"; // 외부 광고 코드 쿠키 키

	public static final String COOKIE_AD_EXTERNAL_LP_CODE_KEY = "LPINFO"; // 외부 광고 코드 쿠키 키 (링크프라이스 전용)

	public static final String AD_EXTERNAL_URL = "/ad/gateway/"; // 외부 광고코드 - 제휴 광고 코드 URL

	public static final int EVENT_RATIO_DECIMAL = 1000;	// MALL - 이벤트 - 확률계산식 - 소수점이하 자리수 보정

	public static final int LP_COOKIE_MAX_AGE_HOURS = 24 * 60 * 60;

	public static final String LP_COOKIE_NAME_LPINFO_GOODS = "LPINFO_GOODS";

	public static final String LP_COOKIE_VALUE_DELIMITER = "|";

	public static final String LP_REQUEST_URL = "http://service.linkprice.com/lppurchase_cps_v4.php";

	public static final String LP_MERCHANT_ID = "pulmuone";

	public static final String LP_CURRENCY_CODE = "KRW";

	public static final String LP_WEB_PC = "web-pc";

	public static final String LP_WEB_MOBILE = "web-mobile";

	public static final String LP_SEARCH_TYPE_PAID = "PAID";

	public static final String LP_SEARCH_TYPE_CONFIRMED = "CONFIRMED";

	public static final String LP_SEARCH_TYPE_CANCELED = "CANCELED";

	public static List<String> ignoreBrandList = Arrays.asList(new String[] {"95","99","129","130","161","105","106","162"});

	public static final String COOKIE_SHOPLIVE_CODE = "shoplive";
}
