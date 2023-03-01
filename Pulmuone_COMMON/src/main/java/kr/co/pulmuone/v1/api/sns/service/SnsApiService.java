package kr.co.pulmuone.v1.api.sns.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.scribejava.apis.NaverApi;
import com.github.scribejava.core.builder.ServiceBuilder;
import com.github.scribejava.core.model.OAuth2AccessToken;
import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.model.Response;
import com.github.scribejava.core.model.Verb;
import com.github.scribejava.core.oauth.OAuth20Service;
import io.jsonwebtoken.Jwts;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.base.vo.BuyerVo;
import kr.co.pulmuone.v1.comm.enums.ApiEnums;
import kr.co.pulmuone.v1.comm.util.JsonUtil;
import kr.co.pulmuone.v1.comm.util.RestTemplateUtil;
import kr.co.pulmuone.v1.comm.util.SessionUtil;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.RSAPublicKeySpec;
import java.util.Base64;
import java.util.Map;
import java.util.UUID;


/**
* <PRE>
* Forbiz Korea
* SNS API Service
* </PRE>
*
* <PRE>
* <B>History:</B>
* =======================================================================
*  버전  :   작성일                :  작성자      :  작성내역
* -----------------------------------------------------------------------
*  1.0    2021. 06. 03.              천혜현         최초작성
* =======================================================================
* </PRE>
*/
@Service
@Slf4j
public class SnsApiService {

    @Autowired
    RestTemplateUtil restTemplateUtil;

    private static final ObjectMapper OBJECT_MAPPER = JsonUtil.OBJECT_MAPPER;

    @Value("${naver.login.userProfileUrl}")
    private String naverUserProfileUrl;

    @Value("${naver.login.clientId}")
    private String naverClientId;

    @Value("${naver.login.clientSecret}")
    private String naverClientSecret;

    @Value("${naver.login.redirectUrl}")
    private String naverRedirectUrl;

    @Value("${naver.login.redirectBuyerUrl}")
    private String naverRedirectBuyerUrl;

    @Value("${kakao.login.userProfileUrl}")
    private String kakaoUserProfileUrl;

    @Value("${google.login.userProfileUrl}")
    private String googleUserProfileUrl;

    @Value("${facebook.login.userProfileUrl}")
    private String facebookUserProfileUrl;

    @Value("${apple.login.userProfileUrl}")
    private String appleUserProfileUrl;


    /**
     * 네이버 네아로 인증 URL 생성
     */
    protected String getAuthorizationUrlNaver(String urlType) {
        BuyerVo buyerVo = SessionUtil.getBuyerUserVO();

        String state = generateRandomString();
        /* 생성한 난수 값을 session에 저장 */
        buyerVo.setSnsAuthorizationState(state);
        SessionUtil.setUserVO(buyerVo);

        OAuth20Service oauthService;

        String callbackUrl = naverRedirectUrl;
        if("MYPAGE".equals(urlType)){
            callbackUrl = naverRedirectBuyerUrl;
        }

        /* 앞서 생성한 난수값을 인증 URL생성시 사용함 */
        oauthService = new ServiceBuilder().apiKey(naverClientId)
                .apiSecret(naverClientSecret)
                .callback(callbackUrl)
                .state(state)
                .build(NaverApi.instance());

        /* Scribe에서 제공하는 인증 URL 생성 기능을 이용하여 네아로 인증 URL 생성 */
        String str = oauthService.getAuthorizationUrl();
        return str;
    }

    /**
     * 세션 유효성 검증을 위한 난수 생성
     * */
    protected String generateRandomString() {
        return UUID.randomUUID().toString();
    }

    /**
     *  네이버 네아로 Callback 처리 및 AccessToken 획득
     */
    protected OAuth2AccessToken getAccessTokenNaver(String code, String state, String urlType) throws Exception{

        /* Callback으로 전달받은 세선검증용 난수값과 세션에 저장되어있는 값이 일치하는지 확인 */
        BuyerVo buyerVo = SessionUtil.getBuyerUserVO();
        String sessionState = buyerVo.getSnsAuthorizationState();
        if (StringUtils.equals(sessionState, state)) {
            OAuth20Service oauthService;
            String callbackUrl = naverRedirectUrl;

            if("MYPAGE".equals(urlType)){
                callbackUrl = naverRedirectBuyerUrl;
            }

            /* 앞서 생성한 난수값을 인증 URL생성시 사용함 */
            oauthService = new ServiceBuilder().apiKey(naverClientId)
                    .apiSecret(naverClientSecret)
                    .callback(callbackUrl)
                    .state(state)
                    .build(NaverApi.instance());

            /* Scribe에서 제공하는 AccessToken 획득 기능으로 네아로 Access Token을 획득 */
            OAuth2AccessToken accessToken = oauthService.getAccessToken(code);
            return accessToken;
        }
        return null;
    }

    /**
     *  네이버 사용자정보 조회 API 호출
     */
    protected ApiResult<?> getUserProfileNaver(OAuth2AccessToken oauthToken, String urlType) throws Exception {
        OAuth20Service oauthService;

        String callbackUrl = naverRedirectUrl;
        if("MYPAGE".equals(urlType)){
            callbackUrl = naverRedirectBuyerUrl;
        }

        oauthService = new ServiceBuilder().apiKey(naverClientId)
                .apiSecret(naverClientSecret)
                .callback(callbackUrl)
                .build(NaverApi.instance());

        OAuthRequest req = new OAuthRequest(Verb.GET, naverUserProfileUrl);
        oauthService.signRequest(oauthToken, req);
        Response response = oauthService.execute(req);
        String responseBody = response.getBody();

        if(!ApiEnums.naverUserProfileAPIResponseCode.SUCCESS.getCode().equals(String.valueOf(response.getCode()))){
            return ApiResult.result(ApiEnums.Default.HTTP_STATUS_FAIL);
        }

        JSONObject json = new JSONObject().fromObject(responseBody);
        JSONObject resultsJson = (JSONObject) json.get("response");

        return ApiResult.success(resultsJson);
    }

    /**
     * 카카오 사용자 정보 조회
     */
    protected ApiResult<?> getUserProfileKaKao (String accessToken) throws Exception{
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "bearer " + accessToken);
        HttpEntity<Object> requestEntity = new HttpEntity<>(headers);
        String url = kakaoUserProfileUrl;
        ResponseEntity<String> responseEntity = restTemplateUtil.get(url,requestEntity);

        // 통신 결과 체크
        if( responseEntity.getStatusCode() != HttpStatus.OK ) {
            return ApiResult.result(ApiEnums.Default.HTTP_STATUS_FAIL);
        }

        String responseBody = responseEntity.getBody();
        log.info("responseBody={}", responseBody);

        JsonNode jsonNode;

        try {
            jsonNode = OBJECT_MAPPER.readTree(responseBody);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return ApiResult.result(ApiEnums.Default.HTTP_STATUS_FAIL);
        }

        return ApiResult.success(jsonNode);
    }

    /**
     * 구글 사용자 정보 조회
     */
    protected ApiResult<?> getUserProfileGoogle(String accessToken) throws Exception{
        String url = googleUserProfileUrl + accessToken;
        ResponseEntity<String> responseEntity = restTemplateUtil.get(url);

        // 통신 결과 체크
        if( responseEntity.getStatusCode() != HttpStatus.OK ) {
            return ApiResult.result(ApiEnums.Default.HTTP_STATUS_FAIL);
        }

        String responseBody = responseEntity.getBody();
        log.info("responseBody={}", responseBody);

        JsonNode jsonNode;

        try {
            jsonNode = OBJECT_MAPPER.readTree(responseBody);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return ApiResult.result(ApiEnums.Default.HTTP_STATUS_FAIL);
        }

        return ApiResult.success(jsonNode);
    }

    /**
     * 페이스북 사용자 정보 조회
     */
    protected ApiResult<?> getUserProfileFacebook(String accessToken) throws Exception{
        String url = facebookUserProfileUrl + accessToken;
        ResponseEntity<String> responseEntity = restTemplateUtil.get(url);

        // 통신 결과 체크
        if( responseEntity.getStatusCode() != HttpStatus.OK ) {
            return ApiResult.result(ApiEnums.Default.HTTP_STATUS_FAIL);
        }

        String responseBody = responseEntity.getBody();
        log.info("responseBody={}", responseBody);

        JsonNode jsonNode;

        try {
            jsonNode = OBJECT_MAPPER.readTree(responseBody);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return ApiResult.result(ApiEnums.Default.HTTP_STATUS_FAIL);
        }

        return ApiResult.success(jsonNode);
    }

    /**
     * 애플 사용자 정보 조회
     */
    protected ApiResult<?> getUserProfileApple(String accessToken) throws Exception{
        String url = appleUserProfileUrl;
        ResponseEntity<String> responseEntity = restTemplateUtil.get(url);

        // 통신 결과 체크
        if( responseEntity.getStatusCode() != HttpStatus.OK ) {
            return ApiResult.result(ApiEnums.Default.HTTP_STATUS_FAIL);
        }

        String responseBody = responseEntity.getBody();
        log.info("responseBody={}", responseBody);

        JsonNode jsonNode;
        JSONObject resultsJson = null;

        try {
            jsonNode = OBJECT_MAPPER.readTree(responseBody);

            String headerOfIdentityToken = accessToken.substring(0, accessToken.indexOf("."));
            Map<String, String> header = new ObjectMapper().readValue(new String(Base64.getDecoder().decode(headerOfIdentityToken), "UTF-8"), Map.class);

            JsonNode jsonArray = jsonNode.get("keys");
            for(JsonNode node : jsonArray){
                if(node.get("kid").asText().equals(header.get("kid")) && node.get("alg").asText().equals(header.get("alg"))){
                    byte[] nBytes = Base64.getUrlDecoder().decode(node.get("n").asText());
                    byte[] eBytes = Base64.getUrlDecoder().decode(node.get("e").asText());

                    BigInteger n = new BigInteger(1, nBytes);
                    BigInteger e = new BigInteger(1, eBytes);

                    RSAPublicKeySpec publicKeySpec = new RSAPublicKeySpec(n, e);
                    KeyFactory keyFactory = KeyFactory.getInstance(node.get("kty").asText());
                    PublicKey publicKey = keyFactory.generatePublic(publicKeySpec);
                    resultsJson = new JSONObject().fromObject(Jwts.parser().setSigningKey(publicKey).parseClaimsJws(accessToken).getBody());
                }
            }
        } catch (Exception e) {
            // 액세스토큰 검증 유효기간 끝났거나 API 통신 오류인 경우
            return ApiResult.result(ApiEnums.Default.HTTP_STATUS_FAIL);
        }

        return ApiResult.success(resultsJson);
    }
}
