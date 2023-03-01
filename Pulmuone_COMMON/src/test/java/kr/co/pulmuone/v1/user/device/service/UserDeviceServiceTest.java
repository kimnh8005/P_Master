package kr.co.pulmuone.v1.user.device.service;

import kr.co.pulmuone.v1.comm.CommonServiceTestBaseForJunit5;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.*;

class UserDeviceServiceTest extends CommonServiceTestBaseForJunit5 {

    @Autowired
    private UserDeviceService userDeviceService;

    @Test
    void 등록되어있는_디바이스정보_갱신_성공 () throws Exception {
        // given
        String deviceId = "B4309E2E-8A62-4FFE-85C5-4D0EC2C3E28F" ;
        String urUserId = "1646893";

        // when
        int cnt = userDeviceService.putMemberMapping(deviceId, urUserId);

        // then
        assertTrue(cnt > 0);
    }

}