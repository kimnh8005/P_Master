package kr.co.pulmuone.v1.app.setting;

import kr.co.pulmuone.v1.app.api.dto.vo.PushDeviceVo;
import kr.co.pulmuone.v1.app.setting.dto.AppSettingResponseDto;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.constants.AppConstants;
import kr.co.pulmuone.v1.comm.enums.BaseEnums;
import kr.co.pulmuone.v1.comm.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

/**
 * <PRE>
 * 모바일 앱 설정 화면 API를 담당하는 서비스
 * 푸시 허용 on/off , 야간 푸시 허용 on/off
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
public class AppSettingBizImpl implements AppSettingBiz {

    @Autowired
    private AppSettingService appSettingService;

    @Override
    public ApiResult<?> getSettingInfo(HttpServletRequest request) throws Exception {
        String deviceId = request.getParameter(AppConstants.DEVICE_ID);
        PushDeviceVo vo = appSettingService.getSettingInfo(deviceId);

        String osType = request.getHeader(AppConstants.OS_TYPE);
        if (StringUtil.isNvl(osType)) return ApiResult.result(BaseEnums.CommBase.MANDATORY_MISSING);

        String app = osType.equals(AppConstants.ANDROID) ? AppConstants.APP_ANDROID_VERSION : AppConstants.APP_IOS_VERSION;
        String version = appSettingService.getAppVersion(app);

        AppSettingResponseDto dto = new AppSettingResponseDto();
        dto.setPushAllowed(vo.getPushAllowed());
        dto.setNightAllowed(vo.getNightAllowed());
        dto.setVersion(version);
        return ApiResult.success(dto);
    }

    @Override
    public ApiResult<?> updatePushAllowed(String deviceId, String pushAllowed) {
        appSettingService.updatePushAllowed(deviceId, pushAllowed);
        return ApiResult.success();
    }

    @Override
    public ApiResult<?> updateNightAllowed(String deviceId, String nightAllowed) {
        appSettingService.updateNightAllowed(deviceId, nightAllowed);
        return ApiResult.success();
    }
}
