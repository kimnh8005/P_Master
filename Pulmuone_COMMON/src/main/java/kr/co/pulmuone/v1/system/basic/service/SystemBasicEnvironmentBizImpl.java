package kr.co.pulmuone.v1.system.basic.service;

import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.system.basic.dto.GetEnvironmentListRequestDto;
import kr.co.pulmuone.v1.system.basic.dto.GetEnvironmentListResponseDto;
import kr.co.pulmuone.v1.system.basic.dto.SaveEnvironmentRequestDto;
import kr.co.pulmuone.v1.system.basic.dto.SaveEnvironmentRequestSaveDto;
import kr.co.pulmuone.v1.system.basic.dto.vo.GetEnvironmentListResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SystemBasicEnvironmentBizImpl implements SystemBasicEnvironmentBiz {

    @Autowired
    SystemBasicEnvironmentService systemBasicEnvironmentService;
    /**
     * 환경설정
     *
     * @param dto
     */
    @Override
    public GetEnvironmentListResponseDto getEnvironmentList(GetEnvironmentListRequestDto dto) throws Exception {
        return systemBasicEnvironmentService.getEnvironmentList(dto);
    }

    @Override
    public ApiResult<?> saveEnvironment(SaveEnvironmentRequestDto dto) throws Exception {
        return systemBasicEnvironmentService.saveEnvironment(dto);
    }

    @Override
    public GetEnvironmentListResultVo getEnvironment(GetEnvironmentListRequestDto dto) throws Exception {
        return systemBasicEnvironmentService.getEnvironment(dto);
    }

    @Override
    public GetEnvironmentListResponseDto putEnvironmentEnvVal(SaveEnvironmentRequestSaveDto saveEnvironmentRequestSaveDto) throws Exception {
        return systemBasicEnvironmentService.putEnvironmentEnvVal(saveEnvironmentRequestSaveDto);
    }
}
