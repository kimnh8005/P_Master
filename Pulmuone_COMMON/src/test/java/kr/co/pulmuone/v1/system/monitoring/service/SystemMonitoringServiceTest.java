package kr.co.pulmuone.v1.system.monitoring.service;

import kr.co.pulmuone.v1.comm.CommonServiceTestBaseForJunit5;
import kr.co.pulmuone.v1.system.monitoring.dto.SystemMonitoringRequestDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.assertTrue;

class SystemMonitoringServiceTest extends CommonServiceTestBaseForJunit5 {

    @Autowired
    private SystemMonitoringService systemMonitoringService;

    @Test
    void getOrder_조회_성공() {
        //given
        SystemMonitoringRequestDto dto = SystemMonitoringRequestDto.builder()
                .count("1000")
                .minute("60")
                .build();

        //when
        Boolean result = systemMonitoringService.getOrder(dto);

        //then
        assertTrue(result);
    }

    @Test
    void getDailyOrder_조회_성공() {
        //given
        SystemMonitoringRequestDto dto = SystemMonitoringRequestDto.builder()
                .count("1")
                .minute("60")
                .build();

        //when
        Boolean result = systemMonitoringService.getDailyOrder(dto);

        //then
        assertTrue(result);
    }

    @Test
    void getStoreOrder_조회_성공() {
        //given
        SystemMonitoringRequestDto dto = SystemMonitoringRequestDto.builder()
                .count("1")
                .minute("60")
                .build();

        //when
        Boolean result = systemMonitoringService.getStoreOrder(dto);

        //then
        assertTrue(result);
    }

    @Test
    void getUserJoin_조회_성공() {
        //given
        SystemMonitoringRequestDto dto = SystemMonitoringRequestDto.builder()
                .count("1")
                .minute("60")
                .build();

        //when
        Boolean result = systemMonitoringService.getUserJoin(dto);

        //then
        assertTrue(result);
    }

    @Test
    void getMallLogin_조회_성공() {
        //given
        SystemMonitoringRequestDto dto = SystemMonitoringRequestDto.builder()
                .count("1000")
                .minute("60")
                .build();

        //when
        Boolean result = systemMonitoringService.getMallLogin(dto);

        //then
        assertTrue(result);
    }

    @Test
    void getBosLogin_조회_성공() {
        //given
        SystemMonitoringRequestDto dto = SystemMonitoringRequestDto.builder()
                .count("1000")
                .minute("60")
                .build();

        //when
        Boolean result = systemMonitoringService.getBosLogin(dto);

        //then
        assertTrue(result);
    }

}