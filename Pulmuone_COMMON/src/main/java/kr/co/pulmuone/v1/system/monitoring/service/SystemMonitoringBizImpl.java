package kr.co.pulmuone.v1.system.monitoring.service;

import kr.co.pulmuone.v1.system.monitoring.dto.SystemMonitoringRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class SystemMonitoringBizImpl implements SystemMonitoringBiz {

    private final SystemMonitoringService systemMonitoringService;

    @Override
    public Boolean getOrder(SystemMonitoringRequestDto dto) {
        return systemMonitoringService.getOrder(dto);
    }

    @Override
    public Boolean getDailyOrder(SystemMonitoringRequestDto dto) {
        return systemMonitoringService.getDailyOrder(dto);
    }

    @Override
    public Boolean getStoreOrder(SystemMonitoringRequestDto dto) {
        return systemMonitoringService.getStoreOrder(dto);
    }

    @Override
    public Boolean getUserJoin(SystemMonitoringRequestDto dto) {
        return systemMonitoringService.getUserJoin(dto);
    }

    @Override
    public Boolean getMallLogin(SystemMonitoringRequestDto dto) {
        return systemMonitoringService.getMallLogin(dto);
    }

    @Override
    public Boolean getBosLogin(SystemMonitoringRequestDto dto) {
        return systemMonitoringService.getBosLogin(dto);
    }
}