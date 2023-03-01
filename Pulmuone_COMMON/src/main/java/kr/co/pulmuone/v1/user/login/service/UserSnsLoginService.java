package kr.co.pulmuone.v1.user.login.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.scribejava.core.model.OAuth2AccessToken;
import kr.co.pulmuone.v1.api.sns.service.SnsApiBiz;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.base.vo.BuyerVo;
import kr.co.pulmuone.v1.comm.enums.BaseEnums;
import kr.co.pulmuone.v1.comm.enums.UserEnums;
import kr.co.pulmuone.v1.comm.enums.UserEnums.KakaoUnlinkType;
import kr.co.pulmuone.v1.comm.util.SHA256Util;
import kr.co.pulmuone.v1.comm.util.SessionUtil;
import kr.co.pulmuone.v1.comm.util.StringUtil;
import kr.co.pulmuone.v1.user.buyer.dto.CallbackSocialLoginResponseDataDto;
import kr.co.pulmuone.v1.user.buyer.dto.DoLoginRequestDto;
import kr.co.pulmuone.v1.user.buyer.dto.DoLoginResponseDto;
import kr.co.pulmuone.v1.user.certification.dto.GetSessionUserSnsCertificationResponseDto;
import kr.co.pulmuone.v1.user.certification.dto.GetSocialLoginDataRequestDto;
import kr.co.pulmuone.v1.user.certification.dto.UserSocialInformationDto;
import kr.co.pulmuone.v1.user.certification.dto.vo.GetSocialLoginDataResultVo;
import kr.co.pulmuone.v1.user.certification.service.UserCertificationBiz;
import kr.co.pulmuone.v1.user.login.dto.SyncAccountRequestDto;
import kr.co.pulmuone.v1.user.login.dto.SyncAccountResponseDataDto;
import kr.co.pulmuone.v1.user.login.dto.UnlinkAccountRequestDto;
import kr.co.pulmuone.v1.user.login.dto.vo.GetUrlNaverResultVo;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Base64;

@Service
@Slf4j
public class UserSnsLoginService {
  private static String urlType = "LOGIN";

  @Autowired
  private UserCertificationBiz userCertificationBiz;

  @Autowired
  private SnsApiBiz snsApiBiz;


  /**
   * SNS 로그인(네이버 네아로 인증 URL 생성)
   *
   * @param request
   * @return GetUrlNaverResultVo
   * @throws Exception
   */
  protected ApiResult<?> getUrlNaver(HttpServletRequest request) throws Exception {

    GetUrlNaverResultVo getUrlNaverResultVo = new GetUrlNaverResultVo();
    getUrlNaverResultVo.setRedirectionUrl(snsApiBiz.getAuthorizationUrlNaver(urlType));

    return ApiResult.success(getUrlNaverResultVo);
  }

  /**
   * SNS 로그인 응답 (네이버)
   *
   * @param request
   * @param response
   * @return ApiResult<?>
   * @throws Exception
   */
  protected ApiResult<?> callbackNaver(HttpServletRequest request, HttpServletResponse response) throws Exception {
    String provider = UserEnums.SnsProvider.NAVER.getCode();

    /* 네아로 인증이 성공적으로 완료되면 code 파라미터가 전달되며 이를 통해 access token을 발급 */
    String code = request.getParameter("code");
    String state = request.getParameter("state");
    String error = request.getParameter("error");

    if(StringUtils.isNotEmpty(error) || StringUtils.isEmpty(code) || StringUtils.isEmpty(state)){
      return ApiResult.fail();
    }

    /* 네아로 Callback 처리 및 AccessToken 획득 */
    OAuth2AccessToken oauthToken = snsApiBiz.getAccessTokenNaver(code, state, urlType);

    if (oauthToken == null) {
      return ApiResult.fail();
    }

    /* Access Token을 이용하여 네이버 사용자 프로필 API를 호출 */
    ApiResult<?> userProfileAPIResult = snsApiBiz.getUserProfileNaver(oauthToken, urlType);

    if(!BaseEnums.Default.SUCCESS.getCode().equals(userProfileAPIResult.getCode())){
      return userProfileAPIResult;
    }

    // 사용자 정보에서 social ID 조회
    JSONObject userProfileJson = (JSONObject)userProfileAPIResult.getData();
    String socialId = userProfileJson.get("id").toString();

    // SNS social ID로 기존 회원여부 체크 후 로그인처리
    return checkSocialLoginData(provider, socialId, request,response);
  }

  /**
   * SNS 로그인 응답 (카카오)
   *
   * @param userSocialInformationDto
   * @param request
   * @param response
   * @throws Exception
   */
  protected ApiResult<?> callbackKakao(UserSocialInformationDto userSocialInformationDto,
                                       HttpServletRequest request,
                                       HttpServletResponse response) throws Exception {
    String provider = UserEnums.SnsProvider.KAKAO.getCode();

    if (StringUtils.isEmpty(userSocialInformationDto.getAccessToken())) {
      return ApiResult.fail();
    }
    
    // accessToken으로 사용자 정보 조회 API 호출
    ApiResult<?> userProfileAPIResult = snsApiBiz.getUserProfileKaKao(userSocialInformationDto.getAccessToken());

    if(!BaseEnums.Default.SUCCESS.getCode().equals(userProfileAPIResult.getCode())){
      return userProfileAPIResult;
    }

    // SOCAIL_ID가 기존 SNS회원인지 확인
    JsonNode userProfileJson = (JsonNode)userProfileAPIResult.getData();
    String socialId = userProfileJson.get("id").toString();

    // SNS social ID로 기존 회원여부 체크 후 로그인처리
    return checkSocialLoginData(provider, socialId, request,response);
  }

  /**
   * SNS회원 정보 세션에 저장
   *
   * @param userSocialInformationDto
   * @throws Exception
   */
  protected void addSessionUserSnsCertification(UserSocialInformationDto userSocialInformationDto) throws Exception {
    BuyerVo buyerVo = SessionUtil.getBuyerUserVO();
    buyerVo.setSnsProvider(userSocialInformationDto.getProvider());
    buyerVo.setSnsSocialId(userSocialInformationDto.getSocialId());
    SessionUtil.setUserVO(buyerVo);
  }

  /**
   * SNS회원 정보 세션에 조회
   */
  protected GetSessionUserSnsCertificationResponseDto getSessionUserSnsCertification() throws Exception {
    GetSessionUserSnsCertificationResponseDto getSessionUserSnsCertificationResponseDto =
                                                                                        new GetSessionUserSnsCertificationResponseDto();

    BuyerVo buyerVo = SessionUtil.getBuyerUserVO();
    getSessionUserSnsCertificationResponseDto.setProvider(StringUtil.nvl(buyerVo.getSnsProvider()));
    getSessionUserSnsCertificationResponseDto.setSocialId(StringUtil.nvl(buyerVo.getSnsSocialId()));

    return getSessionUserSnsCertificationResponseDto;
  }

  /**
   * SNS 로그인 계정연동
   *
   * @param syncAccountRequestDto
   * @return SyncAccountResponseDto
   * @throws Exception
   */
  protected ApiResult<?> syncAccount(SyncAccountRequestDto syncAccountRequestDto,
                                     HttpServletRequest request,
                                     HttpServletResponse response) throws Exception {
    SyncAccountResponseDataDto syncAccountResponseDataDto = new SyncAccountResponseDataDto();

    GetSessionUserSnsCertificationResponseDto getSessionUserSnsCertificationResponseDto = getSessionUserSnsCertification();
    if (getSessionUserSnsCertificationResponseDto == null
        || StringUtil.nvl(getSessionUserSnsCertificationResponseDto.getSocialId()).equals("")) {
      return ApiResult.result(UserEnums.Join.NO_SNS_SESSION);
    } else {
      DoLoginRequestDto doLoginRequestDto = new DoLoginRequestDto();
      doLoginRequestDto.setLoginId(syncAccountRequestDto.getLoginId());
      doLoginRequestDto.setPassword(syncAccountRequestDto.getPassword());
      doLoginRequestDto.setEncryptPassword(SHA256Util.getUserPassword(syncAccountRequestDto.getPassword()));
      doLoginRequestDto.setAutoLogin(false);
      doLoginRequestDto.setSaveLoginId(false);
      doLoginRequestDto.setCaptcha(syncAccountRequestDto.getCaptcha());

      // 로그인 처리
      ApiResult<?> result = userCertificationBiz.doLogin(doLoginRequestDto, true, request, response);

      if (!result.getCode().equals(BaseEnums.Default.SUCCESS.getCode())) {
        return result;
      }

      DoLoginResponseDto doLoginResponseDto = ((DoLoginResponseDto) result.getData());
      syncAccountResponseDataDto.setUrUserId(doLoginResponseDto.getUrUserId());
      if (doLoginResponseDto.getCertification() != null) {
        syncAccountResponseDataDto.setCertification(doLoginResponseDto.getCertification());
      }
      if (doLoginResponseDto.getClause() != null) {
        syncAccountResponseDataDto.setClause(doLoginResponseDto.getClause());
      }
      if (doLoginResponseDto.getMaketting() != null) {
        syncAccountResponseDataDto.setMaketting(doLoginResponseDto.getMaketting());
      }
      if (doLoginResponseDto.getNoti() != null) {
        syncAccountResponseDataDto.setNoti(doLoginResponseDto.getNoti());
      }
      if (doLoginResponseDto.getStop() != null) {
        syncAccountResponseDataDto.setStop(doLoginResponseDto.getStop());
      }

      // 회원 SNS정보 테이블에 저장
      if (result.getCode().equals(BaseEnums.Default.SUCCESS.getCode())) {
        UserSocialInformationDto userSocialInformationDto = new UserSocialInformationDto();
        userSocialInformationDto.setUrUserId(doLoginResponseDto.getUrUserId());
        userSocialInformationDto.setProvider(getSessionUserSnsCertificationResponseDto.getProvider());
        userSocialInformationDto.setSocialId(getSessionUserSnsCertificationResponseDto.getSocialId());
        userSocialInformationDto.setName(getSessionUserSnsCertificationResponseDto.getSocialName());
        userCertificationBiz.addUserSocial(userSocialInformationDto);
      }
    }
    return ApiResult.success(syncAccountResponseDataDto);
  }

  /**
   * SNS 로그인 계정 연결끊기
   */
  protected ApiResult<?>  unlinkAccount(UnlinkAccountRequestDto unlinkAccountRequestDto) throws Exception {

    // 카카오
    if (StringUtils.isNotEmpty(unlinkAccountRequestDto.getReferrer_type()) ) {
    	for( KakaoUnlinkType kakaoUnlinkType: UserEnums.KakaoUnlinkType.values()) {
    		if(kakaoUnlinkType.getCode().equals(unlinkAccountRequestDto.getReferrer_type())) {
    			unlinkAccountRequestDto.setProvider(UserEnums.SnsProvider.KAKAO.getCode());
    		}
    	}
    }

    // 페이스북
    if(StringUtils.isNotEmpty(unlinkAccountRequestDto.getSigned_request())){

      String data = unlinkAccountRequestDto.getSigned_request().split("\\.")[1];
      byte[] dataBytes = Base64.getUrlDecoder().decode(data);
      log.info("dataBytes === " + new String(dataBytes));

      JSONObject dataJson = new JSONObject().fromObject(new String(dataBytes));
      unlinkAccountRequestDto.setProvider(UserEnums.SnsProvider.FACEBOOK.getCode());
      unlinkAccountRequestDto.setUser_id(dataJson.get("user_id").toString());
    }

    userCertificationBiz.unlinkAccount(unlinkAccountRequestDto);
    return ApiResult.success();
  }

  /**
   * SNS 로그인 응답 (구글)
   *
   * @param userSocialInformationDto
   * @throws Exception
   */
  protected ApiResult<?> callbackGoogle(UserSocialInformationDto userSocialInformationDto,
                                       HttpServletRequest request,
                                       HttpServletResponse response) throws Exception {
    String provider = UserEnums.SnsProvider.GOOGLE.getCode();

    if (StringUtils.isEmpty(userSocialInformationDto.getAccessToken())) {
      return ApiResult.fail();
    }

    // accessToken으로 사용자 정보 조회 API 호출
    ApiResult<?> userProfileAPIResult = snsApiBiz.getUserProfileGoogle(userSocialInformationDto.getAccessToken());

    if(!BaseEnums.Default.SUCCESS.getCode().equals(userProfileAPIResult.getCode())){
      return userProfileAPIResult;
    }

    // 사용자 정보에서 social ID 조회
    JsonNode userProfileJson = (JsonNode)userProfileAPIResult.getData();
    String socialId = userProfileJson.get("sub").asText();

    // SNS social ID로 기존 회원여부 체크 후 로그인처리
    return checkSocialLoginData(provider, socialId, request,response);
  }

  /**
   * SNS 로그인 응답 (페이스북)
   *
   * @param userSocialInformationDto
   * @throws Exception
   */
  protected ApiResult<?> callbackFacebook(UserSocialInformationDto userSocialInformationDto,
                                          HttpServletRequest request,
                                          HttpServletResponse response) throws Exception {
    String provider = UserEnums.SnsProvider.FACEBOOK.getCode();

    if (StringUtils.isEmpty(userSocialInformationDto.getAccessToken())) {
      return ApiResult.fail();
    }

    // accessToken으로 사용자 정보 조회 API 호출
    ApiResult<?> userProfileAPIResult = snsApiBiz.getUserProfileFacebook(userSocialInformationDto.getAccessToken());

    if(!BaseEnums.Default.SUCCESS.getCode().equals(userProfileAPIResult.getCode())){
      return userProfileAPIResult;
    }

    // 사용자 정보에서 social ID 조회
    JsonNode userProfileJson = (JsonNode)userProfileAPIResult.getData();
    String socialId = userProfileJson.get("id").asText();

    // SNS social ID로 기존 회원여부 체크 후 로그인처리
    return checkSocialLoginData(provider, socialId, request,response);
  }

  /**
   * SNS 로그인 응답 (애플)
   *
   * @param userSocialInformationDto
   * @throws Exception
   */
  protected ApiResult<?> callbackApple(UserSocialInformationDto userSocialInformationDto,
                                          HttpServletRequest request,
                                          HttpServletResponse response) throws Exception {
    String provider = UserEnums.SnsProvider.APPLE.getCode();

    if (StringUtils.isEmpty(userSocialInformationDto.getAccessToken())) {
      return ApiResult.fail();
    }

    // accessToken으로 사용자 정보 조회 API 호출
    ApiResult<?> userProfileAPIResult = snsApiBiz.getUserProfileApple(userSocialInformationDto.getAccessToken());

    if(!BaseEnums.Default.SUCCESS.getCode().equals(userProfileAPIResult.getCode())){
      return userProfileAPIResult;
    }

    // 사용자 정보에서 social ID 조회
    JSONObject userProfileJson = (JSONObject)userProfileAPIResult.getData();
    String socialId = userProfileJson.get("sub").toString();

    // SNS social ID로 기존 회원여부 체크 후 로그인처리
    return checkSocialLoginData(provider, socialId, request,response);
  }
  
  /**
   * 로그인처리 이후 RESPONSE DATA setting
   * */
  protected CallbackSocialLoginResponseDataDto setCallbackResponseData(DoLoginResponseDto doLoginResponseDto) throws Exception{
    CallbackSocialLoginResponseDataDto callbackResponseData = new CallbackSocialLoginResponseDataDto();

    if (doLoginResponseDto != null) {
      callbackResponseData.setUrUserId(doLoginResponseDto.getUrUserId());
      if (doLoginResponseDto.getCertification() != null) {
        callbackResponseData.setCertification(doLoginResponseDto.getCertification());
      }
      if (doLoginResponseDto.getClause() != null){
        callbackResponseData.setClause(doLoginResponseDto.getClause());
      }
      if (doLoginResponseDto.getMaketting() != null){
        callbackResponseData.setMaketting(doLoginResponseDto.getMaketting());
      }
      if (doLoginResponseDto.getNoti() != null){
        callbackResponseData.setNoti(doLoginResponseDto.getNoti());
      }
      if (doLoginResponseDto.getStop() != null){
        callbackResponseData.setStop(doLoginResponseDto.getStop());
      }
    }
    return callbackResponseData;
  }


  /**
   * SNS social ID로 기존 회원여부 체크 후 로그인처리
   */
  protected ApiResult<?> checkSocialLoginData(String provider,String socialId, HttpServletRequest request, HttpServletResponse response) throws Exception{
    GetSocialLoginDataRequestDto getSocialLoginDataRequestDto = new GetSocialLoginDataRequestDto();
    getSocialLoginDataRequestDto.setProvider(provider);
    getSocialLoginDataRequestDto.setSocialId(socialId);
    GetSocialLoginDataResultVo getSocialLoginDataResultVo = userCertificationBiz.getSocialLoginData(getSocialLoginDataRequestDto);

    UserSocialInformationDto userSocialInformationDto = new UserSocialInformationDto();
    // 기존 SNS회원일때 로그인처리
    if (getSocialLoginDataResultVo != null) {

      DoLoginRequestDto doLoginRequestDto = new DoLoginRequestDto();

      doLoginRequestDto.setAutoLogin(false);
      doLoginRequestDto.setSaveLoginId(false);
      doLoginRequestDto.setEncryptPassword(getSocialLoginDataResultVo.getEncryptPassword());
      doLoginRequestDto.setLoginId(getSocialLoginDataResultVo.getLoginId());
      ApiResult<?> doLoginResponseResult = userCertificationBiz.doLogin(doLoginRequestDto, false, request, response);

      // 개정 약관 에러일때 새션 생성
      if (doLoginResponseResult.getCode().equals(UserEnums.Join.CHECK_VERSION_UP_CLAUSE.getCode())) {
        userSocialInformationDto.setProvider(provider);
        addSessionUserSnsCertification(userSocialInformationDto);
      }

      DoLoginResponseDto doLoginResponseDto = (DoLoginResponseDto)doLoginResponseResult.getData();
      CallbackSocialLoginResponseDataDto callbackSocialResponseDataDto = setCallbackResponseData(doLoginResponseDto);

      return ApiResult.result(callbackSocialResponseDataDto, doLoginResponseResult.getMessageEnum());
    } else {
      // 신규 SNS회원일때 세션에 정보 저장
      userSocialInformationDto.setProvider(provider);
      userSocialInformationDto.setSocialId(socialId);
      addSessionUserSnsCertification(userSocialInformationDto);

      return ApiResult.result(UserEnums.Join.NO_SNS_SYNC);
    }
  }


}
