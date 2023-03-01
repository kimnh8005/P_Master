package kr.co.pulmuone.v1.app.setting;

import kr.co.pulmuone.v1.comm.base.ApiResult;

import javax.servlet.http.HttpServletRequest;

public interface AppSettingBiz {
    ApiResult<?> getSettingInfo(HttpServletRequest request) throws Exception;

    ApiResult<?> updatePushAllowed(String deviceId, String pushAllowed);

    ApiResult<?> updateNightAllowed(String deviceId, String nightAllowed);
}
