package kr.co.pulmuone.v1.app.api;

import kr.co.pulmuone.v1.app.api.dto.PushTokenRequestDto;
import kr.co.pulmuone.v1.comm.base.ApiResult;

import javax.servlet.http.HttpServletRequest;

public interface AppApiBiz {

    ApiResult<?> appVersion(HttpServletRequest request) throws Exception;

    ApiResult<?> pushService(PushTokenRequestDto pushTokenRequestDto, HttpServletRequest request) throws Exception;

    ApiResult<?> readPush(String sequence, String deviceId);
}
