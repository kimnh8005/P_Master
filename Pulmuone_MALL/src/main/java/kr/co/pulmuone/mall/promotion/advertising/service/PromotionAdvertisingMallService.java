package kr.co.pulmuone.mall.promotion.advertising.service;

public interface PromotionAdvertisingMallService {

	String getRedirectAdExternalUrl(String pmAdExternalCd, String ilGoodsId) throws Exception;

	String getRedirectAdExternalUrlForLinkPrice(String lpinfo) throws Exception;
}
