package kr.co.pulmuone.v1.user.login.service;

import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.user.certification.dto.GetSessionUserSnsCertificationResponseDto;
import kr.co.pulmuone.v1.user.certification.dto.UserSocialInformationDto;
import kr.co.pulmuone.v1.user.login.dto.SyncAccountRequestDto;
import kr.co.pulmuone.v1.user.login.dto.UnlinkAccountRequestDto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Service
public class UserSnsLoginBizImpl implements UserSnsLoginBiz {

    @Autowired
    private UserSnsLoginService userSnsLoginService;

    /**
     * SNS 로그인(네이버)
     */
    public ApiResult<?> getUrlNaver(HttpServletRequest request) throws Exception {
        return userSnsLoginService.getUrlNaver(request);
    }

    /**
     * SNS 로그인 응답 (네이버)
     */
    public ApiResult<?> callbackNaver(HttpServletRequest request, HttpServletResponse response) throws Exception {
        return userSnsLoginService.callbackNaver(request, response);
    }

    /**
     * SNS회원 정보 세션에 저장
     */
    public void addSessionUserSnsCertification(UserSocialInformationDto userSocialInformationDto) throws Exception {

        userSnsLoginService.addSessionUserSnsCertification(userSocialInformationDto);
    }

    /**
     * SNS 로그인 계정연동
     */
    public ApiResult<?> syncAccount(SyncAccountRequestDto syncAccountRequestDto, HttpServletRequest request, HttpServletResponse response) throws Exception {

        return userSnsLoginService.syncAccount(syncAccountRequestDto, request, response);
    }

    /**
     * SNS 로그인 응답 (카카오)
     */
    public ApiResult<?> callbackKakao(UserSocialInformationDto userSocialInformationDto, HttpServletRequest request, HttpServletResponse response) throws Exception {

        return userSnsLoginService.callbackKakao(userSocialInformationDto, request, response);
    }

    /**
     *  SNS회원 정보 세션 가지고 오기
     */
    public GetSessionUserSnsCertificationResponseDto getSessionUserSnsCertification() throws Exception {

        return userSnsLoginService.getSessionUserSnsCertification();
    }

    /**
     * SNS 로그인 계정 연결끊기
     */
    public ApiResult<?> unlinkAccount(UnlinkAccountRequestDto unlinkAccountRequestDto) throws Exception {

        return userSnsLoginService.unlinkAccount(unlinkAccountRequestDto);
    }

    /**
     * SNS 로그인 응답 (구글)
     */
    public ApiResult<?> callbackGoogle(UserSocialInformationDto userSocialInformationDto, HttpServletRequest request, HttpServletResponse response) throws Exception {
        return userSnsLoginService.callbackGoogle(userSocialInformationDto, request, response);
    }

    /**
     * SNS 로그인 응답 (페이스북)
     */
    public ApiResult<?> callbackFacebook(UserSocialInformationDto userSocialInformationDto, HttpServletRequest request, HttpServletResponse response) throws Exception {
        return userSnsLoginService.callbackFacebook(userSocialInformationDto, request, response);
    }

    /**
     * SNS 로그인 응답 (애플)
     */
    public ApiResult<?> callbackApple(UserSocialInformationDto userSocialInformationDto, HttpServletRequest request, HttpServletResponse response) throws Exception {
        return userSnsLoginService.callbackApple(userSocialInformationDto, request, response);
    }
}
