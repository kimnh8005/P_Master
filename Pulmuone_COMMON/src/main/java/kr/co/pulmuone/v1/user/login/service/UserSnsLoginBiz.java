package kr.co.pulmuone.v1.user.login.service;

import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.user.certification.dto.GetSessionUserSnsCertificationResponseDto;
import kr.co.pulmuone.v1.user.certification.dto.UserSocialInformationDto;
import kr.co.pulmuone.v1.user.login.dto.SyncAccountRequestDto;
import kr.co.pulmuone.v1.user.login.dto.UnlinkAccountRequestDto;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface UserSnsLoginBiz {

    /**
     * SNS 로그인(네이버)
     */
    ApiResult<?> getUrlNaver(HttpServletRequest request) throws Exception;

    /**
     * SNS 로그인 응답 (네이버)
     */
    ApiResult<?> callbackNaver(HttpServletRequest request, HttpServletResponse response) throws Exception;

    /**
     * SNS회원 정보 세션에 저장
     */
    void addSessionUserSnsCertification(UserSocialInformationDto userSocialInformationDto) throws Exception;

    /**
     * SNS 로그인 계정연동
     */
    ApiResult<?> syncAccount(SyncAccountRequestDto syncAccountRequestDto, HttpServletRequest request, HttpServletResponse response) throws Exception;

    /**
     * SNS 로그인 응답 (카카오)
     */
    ApiResult<?> callbackKakao(UserSocialInformationDto userSocialInformationDto, HttpServletRequest request, HttpServletResponse response) throws Exception;

    /**
     *  SNS회원 정보 세션 가지고 오기
     */
    GetSessionUserSnsCertificationResponseDto getSessionUserSnsCertification() throws Exception;

    /**
     *  SNS 로그인 계정 연결끊기
     */
    ApiResult<?> unlinkAccount(UnlinkAccountRequestDto unlinkAccountRequestDto) throws Exception;

    /**
     * SNS 로그인 응답 (구글)
     */
    ApiResult<?> callbackGoogle(UserSocialInformationDto userSocialInformationDto, HttpServletRequest request, HttpServletResponse response) throws Exception;

    /**
     * SNS 로그인 응답 (페이스북)
     */
    ApiResult<?> callbackFacebook(UserSocialInformationDto userSocialInformationDto, HttpServletRequest request, HttpServletResponse response) throws Exception;

    /**
     * SNS 로그인 응답 (애플)
     */
    ApiResult<?> callbackApple(UserSocialInformationDto userSocialInformationDto, HttpServletRequest request, HttpServletResponse response) throws Exception;
}
