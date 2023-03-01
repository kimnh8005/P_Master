package kr.co.pulmuone.v1.statics.user.service;

import kr.co.pulmuone.v1.comm.CommonServiceTestBaseForJunit5;
import kr.co.pulmuone.v1.statics.user.dto.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.assertTrue;

class UserStaticsServiceTest extends CommonServiceTestBaseForJunit5 {

    @Autowired
    private UserStaticsService userStaticsService;

    @Test
    void getUserTypeStaticsList_조회_성공() {
        //given
        UserTypeStaticsRequestDto dto = new UserTypeStaticsRequestDto();

        //when
        UserTypeStaticsResponseDto result = userStaticsService.getUserTypeStaticsList(dto);

        //then
        assertTrue(result.getTotal() > 0);
    }

    @Test
    void getUserGradeStaticsList_조회_성공() {
        //given
        UserGroupStaticsRequestDto dto = new UserGroupStaticsRequestDto();

        //when
        UserGroupStaticsResponseDto result = userStaticsService.getUserGroupStaticsList(dto);

        //then
        assertTrue(result.getTotal() > 0);
    }

    @Test
    void getUserCountStaticsList_조회_성공() {
        //given
        UserCountStaticsRequestDto dto = new UserCountStaticsRequestDto();

        //when
        UserCountStaticsResponseDto result = userStaticsService.getUserCountStaticsList(dto);

        //then
        assertTrue(result.getTotal() > 0);
    }
    
}