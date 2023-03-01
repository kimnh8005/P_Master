package kr.co.pulmuone.v1.calculate.collation.service;

import kr.co.pulmuone.v1.calculate.collation.dto.CalPgDetlListRequestDto;
import kr.co.pulmuone.v1.calculate.collation.dto.CalPgDetlListResponseDto;
import kr.co.pulmuone.v1.comm.CommonServiceTestBaseForJunit5;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.assertTrue;

class CalPgDetlServiceTest extends CommonServiceTestBaseForJunit5 {
    @Autowired
    private CalPgDetlService calPgDetlService;

    @Test
    void getPgDetailList_조회_성공() {
        //given
        CalPgDetlListRequestDto dto = new CalPgDetlListRequestDto();
        dto.setDateSearchStart("20210701");
        dto.setDateSearchEnd("20210720");

        //when
        CalPgDetlListResponseDto result = calPgDetlService.getPgDetailList(dto);

        //then
        assertTrue(result.getTotal() > 0);
    }

}