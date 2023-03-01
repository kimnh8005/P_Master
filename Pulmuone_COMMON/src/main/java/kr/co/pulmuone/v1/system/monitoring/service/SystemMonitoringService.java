package kr.co.pulmuone.v1.system.monitoring.service;

import kr.co.pulmuone.v1.comm.mapper.system.monitoring.SystemMonitoringMapper;
import kr.co.pulmuone.v1.system.monitoring.dto.SystemMonitoringRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class SystemMonitoringService {

    private final SystemMonitoringMapper systemMonitoringMapper;

    /**
     * 모니터링 - 주문
     *
     * @param dto SystemMonitoringRequestDto
     * @return Boolean
     */
    protected Boolean getOrder(SystemMonitoringRequestDto dto) {
        return systemMonitoringMapper.getOrder(dto);
    }

    /**
     * 모니터링 - 일일배송주문
     *
     * @param dto SystemMonitoringRequestDto
     * @return Boolean
     */
    protected Boolean getDailyOrder(SystemMonitoringRequestDto dto) {
        return systemMonitoringMapper.getDailyOrder(dto);
    }

    /**
     * 모니터링 - 매장배송 주문
     *
     * @param dto SystemMonitoringRequestDto
     * @return Boolean
     */
    protected Boolean getStoreOrder(SystemMonitoringRequestDto dto) {
        return systemMonitoringMapper.getStoreOrder(dto);
    }

    /**
     * 모니터링 - 가입
     *
     * @param dto SystemMonitoringRequestDto
     * @return Boolean
     */
    protected Boolean getUserJoin(SystemMonitoringRequestDto dto) {
        return systemMonitoringMapper.getUserJoin(dto);
    }

    /**
     * 모니터링 - 로그인
     *
     * @param dto SystemMonitoringRequestDto
     * @return Boolean
     */
    protected Boolean getMallLogin(SystemMonitoringRequestDto dto) {
        return systemMonitoringMapper.getMallLogin(dto);
    }

    /**
     * 모니터링 - BOS 로그인
     *
     * @param dto SystemMonitoringRequestDto
     * @return Boolean
     */
    protected Boolean getBosLogin(SystemMonitoringRequestDto dto) {
        return systemMonitoringMapper.getBosLogin(dto);
    }

}