package kr.co.pulmuone.v1.send.device.service;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import kr.co.pulmuone.v1.comm.CommonServiceTestBaseForJunit5;
import kr.co.pulmuone.v1.comm.enums.SystemEnums;
import kr.co.pulmuone.v1.send.device.dto.GetBuyerDeviceListRequestDto;
import kr.co.pulmuone.v1.send.device.dto.GetBuyerDeviceListResponseDto;
import kr.co.pulmuone.v1.send.device.dto.GetDeviceListRequestDto;
import kr.co.pulmuone.v1.send.device.dto.GetDeviceListResponseDto;
import kr.co.pulmuone.v1.send.device.dto.GetDeviceRequestDto;
import kr.co.pulmuone.v1.send.device.dto.GetDeviceResponseDto;
import lombok.extern.slf4j.Slf4j;

@Slf4j
class SendDeviceServiceTest extends CommonServiceTestBaseForJunit5 {

    @Autowired
    private SendDeviceService sendDeviceService;

    @Test
    void APP_설치_단말기_목록_조회() throws Exception {
        //given
        GetDeviceListRequestDto getDeviceListRequestDto = new GetDeviceListRequestDto();

        //when
        GetDeviceListResponseDto result = sendDeviceService.getDeviceList(getDeviceListRequestDto);

        //then
        assertTrue(result.getTotal() > 0);
    }

    @Test
    void PUSH_가능_회원_조회() throws Exception {
        //given
        GetBuyerDeviceListRequestDto dto = new GetBuyerDeviceListRequestDto();
        dto.setCondiValue("userName");
        dto.setMobile("010-1234-5678");
        dto.setMail("ab2305@hanmail.net");
        dto.setUserType("USER_TYPE.BUYER");
        dto.setUserLevel("1");
        dto.setJoinDateStart("2019-12-01");
        dto.setJoinDateEnd("2019-12-31");
        dto.setLastVisitDateStart("2020-02-01");
        dto.setLastVisitDateEnd("2020-02-28");
        dto.setDeviceType("APP_OS_TYPE.1");
        dto.setPushReception("N");

        //when
        GetBuyerDeviceListResponseDto result = sendDeviceService.getBuyerDeviceList(dto);

        //then
        assertTrue(result.getTotal() > 0);
    }


    @Test
    void getDeviceEvnetImage_조회() throws Exception {
        //given
    	GetDeviceListRequestDto getDeviceListRequestDto = new GetDeviceListRequestDto();

        //when
    	GetDeviceResponseDto result = sendDeviceService.getDeviceEvnetImage(getDeviceListRequestDto);


        //then
        assertTrue(result.getRows().size() > 0);
    }


    @Test
    void setDeviceEventImage_성공 () throws Exception {

    	GetDeviceRequestDto dto = new GetDeviceRequestDto();
    	dto.setEnvKey(SystemEnums.DeviceEventImageType.ANDROID_YN.getCode());
    	dto.setEnvVal("Y");

    	GetDeviceResponseDto result = new GetDeviceResponseDto();

    	result = sendDeviceService.setDeviceEventImage(dto);

    	 assertTrue(result.getTotal() > 0);
    }
}