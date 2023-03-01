package kr.co.pulmuone.mall.promotion.advertising.service;

import java.net.URLEncoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import kr.co.pulmuone.v1.comm.enums.AdvertisingEnums;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.github.pagehelper.util.StringUtil;

import kr.co.pulmuone.v1.comm.constants.PromotionConstants;
import kr.co.pulmuone.v1.comm.util.CookieUtil;
import kr.co.pulmuone.v1.promotion.advertising.dto.AdvertisingExternalRequestDto;
import kr.co.pulmuone.v1.promotion.advertising.dto.AdvertisingExternalResponseDto;
import kr.co.pulmuone.v1.promotion.advertising.dto.vo.AdvertisingExternalVo;
import kr.co.pulmuone.v1.promotion.advertising.service.PromotionAdvertisingBiz;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Slf4j
@Service
public class PromotionAdvertisingMallServiceImpl implements PromotionAdvertisingMallService {

	@Autowired
	private HttpServletResponse response;

	@Value("${app.domain}")
	private String appDomain;

	private String mainUrl = "/";
	private String goodsUrl = "/shop/goodsView?goods=";

	@Autowired
	PromotionAdvertisingBiz promotionAdvertisingBiz;

	@Override
	public String getRedirectAdExternalUrlForLinkPrice(String lpinfo) throws Exception {

		// 쿠키 생성
		setCookie(AdvertisingEnums.AdvertCompany.LINK_PRICE.getCode());
		setCookie(PromotionConstants.COOKIE_AD_EXTERNAL_LP_CODE_KEY, lpinfo, 24*60*60);

		return "any/ad/gateway/index";
	}

	@Override
	public String getRedirectAdExternalUrl(String pmAdExternalCd, String ilGoodsId) throws Exception {
		if (StringUtil.isNotEmpty(pmAdExternalCd)) {
			AdvertisingExternalRequestDto dto = new AdvertisingExternalRequestDto();
			dto.setPmAdExternalCd(pmAdExternalCd);
			AdvertisingExternalResponseDto advertisingExternalDto = promotionAdvertisingBiz.getAdvertisingExternal(dto);
			if (advertisingExternalDto != null && !advertisingExternalDto.getAdvertisingExternalList().isEmpty()) {
				if (StringUtil.isNotEmpty(ilGoodsId)) {	// 상품상세 링크인 경우에만 shoplive 관련 외부광고쿠키 존재 여부 체크
					ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
					HttpServletRequest request = servletRequestAttributes.getRequest();
					String adExternalCd = CookieUtil.getCookie(request, PromotionConstants.COOKIE_AD_EXTERNAL_CODE_KEY);
					// 이전 shoplive 외부광고코드가 존재하지 않는 경우만 외부광고코드 쿠키 생성 (반드시 'shoplive' 를 포함한 형태로 외부광고코드를 생성해야 한다.)
					if (StringUtil.isEmpty(adExternalCd) || adExternalCd.toLowerCase().indexOf(PromotionConstants.COOKIE_SHOPLIVE_CODE) < 0) {	setCookie(pmAdExternalCd);	}
				} else {
					setCookie(pmAdExternalCd);
				}
				AdvertisingExternalVo advertisingExternalVo = advertisingExternalDto.getAdvertisingExternalList().get(0);
				if (StringUtil.isNotEmpty(ilGoodsId)) {
					// 광고코드정보와 상품코드 존재시
					return makeRedirectUrl(advertisingExternalVo, ilGoodsId);
				} else {
					// 광고코드정보만 있을경우
					return makeRedirectUrl(advertisingExternalVo);
				}
			} else {
				if (StringUtil.isNotEmpty(ilGoodsId)) {
					// 광고코드 정보는 없고 상품정보 없을때
					return makeRedirectUrl(ilGoodsId);
				} else {
					// 광고코드 정보 및 상품정보 모두 없을때
					return mainUrl();
				}
			}
		} else {
			// 광고코드 없는경우 메인으로 이동
			return mainUrl();
		}
	}

	private String makeRedirectUrl(String ilGoodsId) throws Exception {
		return returnUrl(goodsUrl + ilGoodsId);
	}

	private String makeRedirectUrl(AdvertisingExternalVo advertisingExternalVo) throws Exception {
		return returnUrl(makeGaParameter(advertisingExternalVo.getRedirectUrl(), advertisingExternalVo));
	}

	private String makeRedirectUrl(AdvertisingExternalVo advertisingExternalVo, String ilGoodsId) throws Exception {
		return returnUrl(makeGaParameter(goodsUrl + ilGoodsId, advertisingExternalVo));
	}

	private String makeGaParameter(String url, AdvertisingExternalVo advertisingExternalVo) throws Exception {
		String div = "?";
		if (url.indexOf(div) > 0) {
			div = "&";
		}
		return url + new StringBuilder(div).append("utm_source=").append(urlEncode(advertisingExternalVo.getSource()))
				.append("&utm_medium=").append(urlEncode(advertisingExternalVo.getMedium()))
				.append("&utm_campaign=").append(urlEncode(advertisingExternalVo.getCampaign()))
                .append("&utm_term=").append(urlEncode(advertisingExternalVo.getTerm()))
				.append("&utm_content=").append(urlEncode(advertisingExternalVo.getContent())).toString();
	}

	private String mainUrl() {
		return returnUrl(mainUrl);
	}

	private String returnUrl(String url) {

		String returnUrl = url;

		if(url.indexOf("http") != 0) {
			returnUrl = appDomain + url;
		}

		return "redirect:" + returnUrl;
	}

	private String urlEncode(String input) throws Exception {
		if(input == null) return "";
		return URLEncoder.encode(input, "UTF-8");
	}

	private void setCookie(String pmAdExternalCd) throws Exception {
		// 1시간으로 설정
		CookieUtil.setCookie(response, PromotionConstants.COOKIE_AD_EXTERNAL_CODE_KEY, pmAdExternalCd, 3600);
	}

	private void setCookie(String keyName, String keyValue, int keepTime) throws Exception {
		// 1시간으로 설정
		CookieUtil.setCookie(response, keyName, keyValue, keepTime);
	}
}
