package kr.co.pulmuone.v1.api.sns.service;

import com.github.scribejava.core.model.OAuth2AccessToken;
import kr.co.pulmuone.v1.comm.base.ApiResult;

public interface SnsApiBiz {

	/**
	 *  네이버 네아로 인증 URL 생성
	 */
	String getAuthorizationUrlNaver(String urlType);

	/**
	 * 세션 유효성 검증을 위한 난수 생성
	 * */
	String generateRandomString();

	/**
	 *  네이버 네아로 Callback 처리 및 AccessToken 획득
	 */
	OAuth2AccessToken getAccessTokenNaver(String code, String state, String urlType) throws Exception;

	/**
	 *  네이버 사용자정보 조회 API 호출
	 */
	ApiResult<?> getUserProfileNaver(OAuth2AccessToken oauthToken, String urlType) throws Exception;

	/**
	 *  카카오 사용자정보 조회 API 호출
	 */
	ApiResult<?> getUserProfileKaKao(String accessToken) throws Exception;

	/**
	 *  구글 사용자정보 조회 API 호출
	 */
	ApiResult<?> getUserProfileGoogle(String accessToken) throws Exception;

	/**
	 *  페이스북 사용자정보 조회 API 호출
	 */
	ApiResult<?> getUserProfileFacebook(String accessToken) throws Exception;

	/**
	 *  애플 사용자정보 조회 API 호출
	 */
	ApiResult<?> getUserProfileApple(String accessToken) throws Exception;
}

