package kr.co.pulmuone.v1.comm.mapper.system.monitoring;

import kr.co.pulmuone.v1.system.monitoring.dto.SystemMonitoringRequestDto;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SystemMonitoringMapper {

    Boolean getOrder(SystemMonitoringRequestDto dto);

    Boolean getDailyOrder(SystemMonitoringRequestDto dto);

    Boolean getStoreOrder(SystemMonitoringRequestDto dto);

    Boolean getUserJoin(SystemMonitoringRequestDto dto);

    Boolean getMallLogin(SystemMonitoringRequestDto dto);

    Boolean getBosLogin(SystemMonitoringRequestDto dto);

}
