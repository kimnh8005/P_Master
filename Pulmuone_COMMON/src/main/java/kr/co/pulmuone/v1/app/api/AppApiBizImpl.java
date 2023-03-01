package kr.co.pulmuone.v1.app.api;


import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.pulmuone.v1.app.api.dto.AppversionResponseDto;
import kr.co.pulmuone.v1.app.api.dto.PushTokenRequestDto;
import kr.co.pulmuone.v1.app.api.dto.vo.PushDeviceVo;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.constants.AppConstants;
import kr.co.pulmuone.v1.comm.constants.BuyerConstants;
import kr.co.pulmuone.v1.comm.enums.BaseEnums;
import kr.co.pulmuone.v1.comm.enums.SendEnums;
import kr.co.pulmuone.v1.comm.util.CookieUtil;
import kr.co.pulmuone.v1.comm.util.StringUtil;

/**
 * <PRE>
 * 모바일 앱 관련 API를 담당하는 서비스
 * 앱버전, 푸시키, 푸시 읽음처리의 API를 담당한다
 *
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 * 버전  :   작성일                :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 * 1.0		20210121		허성민              최초작성
 * <p>
 * =======================================================================
 * </PRE>
 */
@Service
public class AppApiBizImpl implements AppApiBiz {

    @Autowired
    AppApiService appApiService;

    @Override
    public ApiResult<?> appVersion(HttpServletRequest request) throws Exception {
        String osType = request.getHeader(AppConstants.OS_TYPE);
        if (StringUtil.isNvl(osType)) return ApiResult.result(BaseEnums.CommBase.MANDATORY_MISSING);

        String app = osType.equals(AppConstants.ANDROID) ? AppConstants.APP_ANDROID : AppConstants.APP_IOS;
        HashMap<String, String> environmentMap = appApiService.appVersion(app);
        AppversionResponseDto appversionResponseDto = new AppversionResponseDto();
        if (app.equals(AppConstants.APP_ANDROID)) {
            appversionResponseDto.setVersion(environmentMap.get(AppConstants.APP_ANDROID_VERSION));
            appversionResponseDto.setNecessary(environmentMap.get(AppConstants.APP_ANDROID_UPDATE_REQUIRED_YN));
            appversionResponseDto.setEventAllowed(environmentMap.get(AppConstants.APP_ANDROID_EVENT_IMG_YN));
            appversionResponseDto.setEventImg(environmentMap.get(AppConstants.APP_ANDROID_EVENT_IMG_URL));
        } else {
            appversionResponseDto.setVersion(environmentMap.get(AppConstants.APP_IOS_VERSION));
            appversionResponseDto.setAppleStore(environmentMap.get(AppConstants.APP_IOS_STORE_ID));
            appversionResponseDto.setNecessary(environmentMap.get(AppConstants.APP_IOS_UPDATE_REQUIRED_YN));
            appversionResponseDto.setEventAllowed(environmentMap.get(AppConstants.APP_IOS_EVENT_IMG_YN));
            appversionResponseDto.setEventImg(environmentMap.get(AppConstants.APP_IOS_EVENT_IMG_URL));
        }

        return ApiResult.success(appversionResponseDto);
    }

    @Override
    public ApiResult<?> pushService(PushTokenRequestDto pushTokenRequestDto, HttpServletRequest request) throws Exception {

        String osType = request.getHeader(AppConstants.OS_TYPE);
        if (StringUtil.isNvl(osType)) return ApiResult.result(BaseEnums.CommBase.MANDATORY_MISSING);

        String appOsType = request.getHeader(AppConstants.OS_TYPE).equals(AppConstants.ANDROID)?
                SendEnums.AppOsType.ANDROID.getCode() : SendEnums.AppOsType.IOS.getCode();
        String userCode = pushTokenRequestDto.getUserCode();

        PushDeviceVo pushDeviceVo = new PushDeviceVo();
        pushDeviceVo.setOsType(appOsType);
        pushDeviceVo.setUserCode(StringUtil.isNvl(userCode)? 0:Long.parseLong(userCode));
        pushDeviceVo.setPushKey(pushTokenRequestDto.getPushKey());
        pushDeviceVo.setDeviceId(pushTokenRequestDto.getDeviceId());
        pushDeviceVo.setPushAllowed(pushTokenRequestDto.getPushAllowed());
        pushDeviceVo.setUrPcidCd(CookieUtil.getCookie(request, BuyerConstants.COOKIE_PCID_CODE_KEY));
        pushDeviceVo.setWriteType(pushTokenRequestDto.getWriteType());
        appApiService.pushService(pushDeviceVo);
        return ApiResult.success();
    }

    @Override
    public ApiResult<?> readPush(String sequence, String deviceId) {
        int pushId = Integer.parseInt(sequence);
        appApiService.putOpenYn(deviceId, pushId);
        return ApiResult.success();
    }
}
