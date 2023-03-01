package kr.co.pulmuone.v1.api.ncp.service;

import kr.co.pulmuone.v1.api.ncp.dto.NcpSmsRequestDto;
import kr.co.pulmuone.v1.comm.CommonServiceTestBaseForJunit5;
import kr.co.pulmuone.v1.comm.enums.SendEnums;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class NCPApiSmsServiceTest extends CommonServiceTestBaseForJunit5 {
    @Autowired
    NCPApiSmsService ncpApiSmsService;

    @Test
    void ncp_api_성공() throws NoSuchAlgorithmException, InvalidKeyException, IOException {
        //given
        List<NcpSmsRequestDto> smsInfoList = new ArrayList<>();
        smsInfoList.add(NcpSmsRequestDto.builder()
                .content("testCase")
                .mobile("01012345678")
                .senderTelephone("0800220086")
                .build()
        );

        //when
        String result = ncpApiSmsService.sendMessage(ncpApiSmsService.getJSONObject(smsInfoList, SendEnums.SendNcpSmsType.SMS));

        //then
        assertEquals(SendEnums.SendNcpSmsResponseCode.ACCEPT.getCode(), result);
    }

}