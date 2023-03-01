package kr.co.pulmuone.v1.api.sns.service;


import com.github.scribejava.core.model.OAuth2AccessToken;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
* <PRE>
* Forbiz Korea
* SNS API BizImpl
* </PRE>
*
* <PRE>
* <B>History:</B>
* =======================================================================
*  버전  :   작성일                :  작성자      :  작성내역
* -----------------------------------------------------------------------
*  1.0    2021. 06. 03.		        천혜현          최초작성
* =======================================================================
* </PRE>
*/
@Service
public class SnsApiBizImpl implements SnsApiBiz {

    @Autowired
    SnsApiService snsLoginService;

    /**
     * 네이버 네아로 인증 URL 생성
     *
     * @param urlType
     * @return String
     * @throws Exception
     */
    public String getAuthorizationUrlNaver(String urlType){
        return snsLoginService.getAuthorizationUrlNaver(urlType);
    }

    /**
     * 세션 유효성 검증을 위한 난수 생성
     *
     * @return String
     * @throws Exception
     */
    public String generateRandomString(){
        return snsLoginService.generateRandomString();
    }

    /**
     * 네이버 네아로 Callback 처리 및 AccessToken 획득
     *
     * @param code
     * @param state
     * @param urlType
     * @return OAuth2AccessToken
     * @throws Exception
     */
    public OAuth2AccessToken getAccessTokenNaver(String code, String state, String urlType) throws Exception{
        return snsLoginService.getAccessTokenNaver(code,state,urlType);
    }

    /**
     * 네이버 사용자정보 조회 API 호출
     *
     * @param oauthToken
     * @param urlType
     * @return ApiResult<?>
     * @throws Exception
     */
    public ApiResult<?> getUserProfileNaver(OAuth2AccessToken oauthToken, String urlType) throws Exception{
        return snsLoginService.getUserProfileNaver(oauthToken, urlType);
    }

    /**
     * 카카오 사용자 정보 조회 API 호출
     *
     * @param accessToken
     * @return ApiResult<?>
     * @throws Exception
     */
    public ApiResult<?> getUserProfileKaKao(String accessToken) throws Exception{
        return snsLoginService.getUserProfileKaKao(accessToken);
    }

    /**
     * 구글 사용자 정보 조회 API 호출
     *
     * @param accessToken
     * @return ApiResult<?>
     * @throws Exception
     */
    public ApiResult<?> getUserProfileGoogle(String accessToken) throws Exception{
        return snsLoginService.getUserProfileGoogle(accessToken);
    }

    /**
     * 페이스북 사용자 정보 조회 API 호출
     *
     * @param accessToken
     * @return ApiResult<?>
     * @throws Exception
     */
    public ApiResult<?> getUserProfileFacebook(String accessToken) throws Exception{
        return snsLoginService.getUserProfileFacebook(accessToken);
    }

    /**
     * 애플 사용자 정보 조회 API 호출
     *
     * @param accessToken
     * @return ApiResult<?>
     * @throws Exception
     */
    public ApiResult<?> getUserProfileApple(String accessToken) throws Exception{
        return snsLoginService.getUserProfileApple(accessToken);
    }
}
