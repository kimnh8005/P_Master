package kr.co.pulmuone.v1.system.itgc.service;

import kr.co.pulmuone.v1.comm.CommonServiceTestBaseForJunit5;
import kr.co.pulmuone.v1.comm.enums.SystemEnums;
import kr.co.pulmuone.v1.system.itgc.dto.ItgcRequestDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

class SystemItgcServiceTest extends CommonServiceTestBaseForJunit5 {

    @Autowired
    private SystemItgcService systemItgcService;

    @Test
    void addItgcList() {
        //given
        List<ItgcRequestDto> insertList = new ArrayList<>();
        ItgcRequestDto dto1 = ItgcRequestDto.builder()
                .stMenuId(1L)
                .itgcType(SystemEnums.ItgcType.ACCOUNT_ADD)
                .itsmId("1111")
                .itgcDetail("등록")
                .itgcAddType(SystemEnums.ItgcAddType.ADD)
                .targetInfo("역할그룹명")
                .createId(0L)
                .build();
        insertList.add(dto1);

        //when
        int result = systemItgcService.addItgcList(insertList);

        //then
        assertTrue(result > 0);
    }

}