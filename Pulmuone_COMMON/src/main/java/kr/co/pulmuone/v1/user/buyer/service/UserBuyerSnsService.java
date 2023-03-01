package kr.co.pulmuone.v1.user.buyer.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.scribejava.core.model.OAuth2AccessToken;
import kr.co.pulmuone.v1.api.sns.service.SnsApiBiz;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.base.vo.BuyerVo;
import kr.co.pulmuone.v1.comm.enums.BaseEnums;
import kr.co.pulmuone.v1.comm.enums.UserEnums;
import kr.co.pulmuone.v1.comm.util.SessionUtil;
import kr.co.pulmuone.v1.comm.util.StringUtil;
import kr.co.pulmuone.v1.user.certification.dto.UserSocialInformationDto;
import kr.co.pulmuone.v1.user.certification.service.UserCertificationBiz;
import kr.co.pulmuone.v1.user.login.dto.UnlinkAccountRequestDto;
import kr.co.pulmuone.v1.user.login.dto.vo.GetUrlNaverResultVo;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

@Service
@Slf4j
public class UserBuyerSnsService {
  private static String urlType = "MYPAGE";

  @Autowired
  private UserCertificationBiz userCertificationBiz;

  @Autowired
  private SnsApiBiz snsApiBiz;


  /**
   * SNS 로그인(네이버 네아로 인증 URL 생성)
   *
   * @return GetUrlNaverResultVo
   * @throws Exception
   */
  protected ApiResult<?> getUrlNaver() throws Exception {

    GetUrlNaverResultVo getUrlNaverResultVo = new GetUrlNaverResultVo();
    getUrlNaverResultVo.setRedirectionUrl(snsApiBiz.getAuthorizationUrlNaver(urlType));

    return ApiResult.success(getUrlNaverResultVo);
  }

  /**
   * SNS 로그인 응답 (네이버)
   *
   * @param request
   * @return BaseResponseDto
   * @throws Exception
   */
  protected ApiResult<?> callbackNaver(HttpServletRequest request) throws Exception {
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

    // 사용자 정보 저장
    UserSocialInformationDto userSocialInformationDto = new UserSocialInformationDto();

    BuyerVo buyerVo = SessionUtil.getBuyerUserVO();
    if (StringUtil.isEmpty(buyerVo.getUrUserId())) {
      return ApiResult.result(UserEnums.Buyer.NEED_LOGIN);
    }
    userSocialInformationDto.setProvider(provider);
    userSocialInformationDto.setUrUserId(buyerVo.getUrUserId());
    userSocialInformationDto.setSocialId(socialId);

    userCertificationBiz.addUserSocial(userSocialInformationDto);

    return ApiResult.success(userSocialInformationDto);

  }

  /**
   * SNS 로그인 응답 (카카오)
   *
   * @param userSocialInformationDto
   * @return BaseResponseDto
   * @throws Exception
   */
  protected ApiResult<?> callbackKakao(UserSocialInformationDto userSocialInformationDto) throws Exception {
    String provider = UserEnums.SnsProvider.KAKAO.getCode();

    if (StringUtils.isEmpty(userSocialInformationDto.getAccessToken())) {
      return ApiResult.fail();
    }

    // accessToken으로 사용자 정보 조회 API 호출
    ApiResult<?> userProfileAPIResult = snsApiBiz.getUserProfileKaKao(userSocialInformationDto.getAccessToken());

    if(!BaseEnums.Default.SUCCESS.getCode().equals(userProfileAPIResult.getCode())){
      return userProfileAPIResult;
    }

    // 사용자 정보에서 social ID 조회
    JsonNode userProfileJson = (JsonNode)userProfileAPIResult.getData();
    String socialId = userProfileJson.get("id").toString();

    // 사용자 정보 저장
    BuyerVo buyerVo = SessionUtil.getBuyerUserVO();
    if (StringUtil.isEmpty(buyerVo.getUrUserId())) {
      return ApiResult.result(UserEnums.Buyer.NEED_LOGIN);
    }

    userSocialInformationDto.setUrUserId(buyerVo.getUrUserId());
    userSocialInformationDto.setProvider(provider);
    userSocialInformationDto.setSocialId(socialId);

    userCertificationBiz.addUserSocial(userSocialInformationDto);
    return ApiResult.success(userSocialInformationDto);
  }


  /**
   * SNS 로그인 응답 (구글)
   *
   * @param userSocialInformationDto
   * @return BaseResponseDto
   * @throws Exception
   */
  protected ApiResult<?> callbackGoogle(UserSocialInformationDto userSocialInformationDto) throws Exception {
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

    // 사용자 정보 저장
    BuyerVo buyerVo = SessionUtil.getBuyerUserVO();
    if (StringUtil.isEmpty(buyerVo.getUrUserId())) {
      return ApiResult.result(UserEnums.Buyer.NEED_LOGIN);
    }

    userSocialInformationDto.setUrUserId(buyerVo.getUrUserId());
    userSocialInformationDto.setProvider(provider);
    userSocialInformationDto.setSocialId(socialId);

    userCertificationBiz.addUserSocial(userSocialInformationDto);
    return ApiResult.success(userSocialInformationDto);
  }

  /**
   * SNS 로그인 응답 (페이스북)
   *
   * @param userSocialInformationDto
   * @return BaseResponseDto
   * @throws Exception
   */
  protected ApiResult<?> callbackFacebook(UserSocialInformationDto userSocialInformationDto) throws Exception {
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

    // 사용자 정보 저장
    BuyerVo buyerVo = SessionUtil.getBuyerUserVO();
    if (StringUtil.isEmpty(buyerVo.getUrUserId())) {
      return ApiResult.result(UserEnums.Buyer.NEED_LOGIN);
    }

    userSocialInformationDto.setUrUserId(buyerVo.getUrUserId());
    userSocialInformationDto.setProvider(provider);
    userSocialInformationDto.setSocialId(socialId);

    userCertificationBiz.addUserSocial(userSocialInformationDto);
    return ApiResult.success(userSocialInformationDto);
  }

  /**
   * SNS 로그인 응답 (애플)
   *
   * @param userSocialInformationDto
   * @return BaseResponseDto
   * @throws Exception
   */
  protected ApiResult<?> callbackApple(UserSocialInformationDto userSocialInformationDto) throws Exception {
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

    // 사용자 정보 저장
    BuyerVo buyerVo = SessionUtil.getBuyerUserVO();
    if (StringUtil.isEmpty(buyerVo.getUrUserId())) {
      return ApiResult.result(UserEnums.Buyer.NEED_LOGIN);
    }

    userSocialInformationDto.setUrUserId(buyerVo.getUrUserId());
    userSocialInformationDto.setProvider(provider);
    userSocialInformationDto.setSocialId(socialId);

    userCertificationBiz.addUserSocial(userSocialInformationDto);
    return ApiResult.success(userSocialInformationDto);
  }

  /**
   * SNS 로그인 계정 연결끊기
   */
  protected ApiResult<?> unlinkAccount(UnlinkAccountRequestDto unlinkAccountRequestDto) throws Exception {

    userCertificationBiz.unlinkAccount(unlinkAccountRequestDto);

    return ApiResult.success();
  }

}
