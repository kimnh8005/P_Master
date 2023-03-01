package kr.co.pulmuone.v1.app.api;

import kr.co.pulmuone.v1.app.api.dto.vo.PushDeviceVo;
import kr.co.pulmuone.v1.comm.constants.AppConstants;
import kr.co.pulmuone.v1.comm.mapper.app.api.AppApiMapper;
import kr.co.pulmuone.v1.system.basic.dto.GetEnvironmentListRequestDto;
import kr.co.pulmuone.v1.system.basic.dto.GetEnvironmentListResponseDto;
import kr.co.pulmuone.v1.system.basic.dto.vo.GetEnvironmentListResultVo;
import kr.co.pulmuone.v1.system.basic.service.SystemBasicEnvironmentBiz;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

/**
 * <PRE>
 * 모바일 앱 관련 API를 담당하는 서비스
 * 앱버전, 푸시키, 푸시 읽음처리의 데이터 바인딩 처리한다
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
public class AppApiService {

    @Autowired
    private AppApiMapper appApiMapper;

    @Autowired
    SystemBasicEnvironmentBiz systemBasicEnvironmentBiz;

    protected HashMap<String, String> appVersion(String osType) throws Exception {
        GetEnvironmentListRequestDto getEnvironmentListRequestDto = new GetEnvironmentListRequestDto();
        getEnvironmentListRequestDto.setSearchEnvironmentKey(osType);
        GetEnvironmentListResponseDto resultVo = systemBasicEnvironmentBiz.getEnvironmentList(getEnvironmentListRequestDto);
        List<GetEnvironmentListResultVo> list = resultVo.getRows();
        HashMap<String, String> environmentMap = new HashMap<>();
        for (GetEnvironmentListResultVo vo : list) {
            environmentMap.put(vo.getEnvironmentKey(), vo.getEnvironmentValue());
        }
        return environmentMap;
    }

    protected void pushService(PushDeviceVo pushDeviceVo) {
        AppConstants.WriteType type = pushDeviceVo.getWriteType().toUpperCase().equals(AppConstants.WriteType.INSERT.name()) ?
                AppConstants.WriteType.INSERT : AppConstants.WriteType.DELETE;
        switch (type) {
            case INSERT:
                appApiMapper.insertPushToken(pushDeviceVo);
                break;
            case DELETE:
                appApiMapper.deletePushToken(pushDeviceVo.getDeviceId());
                break;
        }
    }

    protected void putOpenYn(String deviceId, int pushId) {
        appApiMapper.updateOpenYn(deviceId, pushId);
    }

}
