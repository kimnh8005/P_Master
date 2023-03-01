package kr.co.pulmuone.v1.system.monitoring.service;

import kr.co.pulmuone.v1.system.monitoring.dto.SystemMonitoringRequestDto;

public interface SystemMonitoringBiz {

    Boolean getOrder(SystemMonitoringRequestDto dto);

    Boolean getDailyOrder(SystemMonitoringRequestDto dto);

    Boolean getStoreOrder(SystemMonitoringRequestDto dto);

    Boolean getUserJoin(SystemMonitoringRequestDto dto);

    Boolean getMallLogin(SystemMonitoringRequestDto dto);

    Boolean getBosLogin(SystemMonitoringRequestDto dto);

}

