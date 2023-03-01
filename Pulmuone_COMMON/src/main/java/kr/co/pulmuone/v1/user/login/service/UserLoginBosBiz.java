package kr.co.pulmuone.v1.user.login.service;

import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.user.certification.dto.UserSocialInformationDto;
import kr.co.pulmuone.v1.user.login.dto.LoginDto;
import kr.co.pulmuone.v1.user.login.dto.LoginRequestDto;
import kr.co.pulmuone.v1.user.login.dto.LoginResponseDto;
import kr.co.pulmuone.v1.user.login.dto.vo.LoginResultVo;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface UserLoginBosBiz {

    int chkLogin(LoginDto dto) throws Exception;

    String urUserId(LoginDto dto) throws Exception;

    ApiResult<?> hasLoginData(LoginRequestDto loginRequestDto, HttpServletRequest req) ;

    ApiResult<?> getPassWordByData(LoginRequestDto loginRequestDto) throws Exception ;

    ApiResult<?> putPassWordByLogin(LoginRequestDto loginRequestDto) throws Exception ;

    ApiResult<?> delLoginData(LoginRequestDto loginRequestDto, HttpServletRequest req) throws Exception ;

    LoginResultVo getPasswordByPassword(LoginRequestDto loginRequestDto)  ;

    ApiResult<?> getRecentlyLoginData() throws Exception;

    ApiResult<?> bosTwoFactorAuthentificationVeriyfy(String authCertiNo) throws Exception;
}
