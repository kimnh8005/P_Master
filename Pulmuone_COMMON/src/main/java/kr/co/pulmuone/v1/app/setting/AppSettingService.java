package kr.co.pulmuone.v1.app.setting;

import kr.co.pulmuone.v1.app.api.dto.vo.PushDeviceVo;
import kr.co.pulmuone.v1.comm.enums.SendEnums;
import kr.co.pulmuone.v1.comm.mapper.app.api.AppApiMapper;
import kr.co.pulmuone.v1.system.basic.dto.GetEnvironmentListRequestDto;
import kr.co.pulmuone.v1.system.basic.dto.vo.GetEnvironmentListResultVo;
import kr.co.pulmuone.v1.system.basic.service.SystemBasicEnvironmentBiz;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <PRE>
 * 모바일 앱 설정 화면의 데이터 바인딩을 담당하는 서비스
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
@Slf4j
@Service
@RequiredArgsConstructor
public class AppSettingService {

    @Autowired
    private AppApiMapper appApiMapper;

    @Autowired
    SystemBasicEnvironmentBiz systemBasicEnvironmentBiz;

    protected PushDeviceVo getSettingInfo(String deviceId) {
        return appApiMapper.findBySettingInfo(deviceId);
    }

    protected void updatePushAllowed(String deviceId, String pushAllowed) {
        appApiMapper.updatePushAllowed(deviceId, pushAllowed);
    }

    protected void updateNightAllowed(String deviceId, String nightAllowed) {
        appApiMapper.updateNightAllowed(deviceId, nightAllowed);
    }

    protected String getAppVersion(String osType) throws Exception {
        GetEnvironmentListRequestDto getEnvironmentListRequestDto = new GetEnvironmentListRequestDto();
        getEnvironmentListRequestDto.setSearchEnvironmentKey(osType);
        return systemBasicEnvironmentBiz.getEnvironment(getEnvironmentListRequestDto).getEnvironmentValue();
    }

}
