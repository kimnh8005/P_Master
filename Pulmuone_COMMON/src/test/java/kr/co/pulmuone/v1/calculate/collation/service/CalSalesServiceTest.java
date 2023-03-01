package kr.co.pulmuone.v1.calculate.collation.service;

import kr.co.pulmuone.v1.calculate.collation.dto.CalSalesListRequestDto;
import kr.co.pulmuone.v1.calculate.collation.dto.CalSalesListResponseDto;
import kr.co.pulmuone.v1.comm.CommonServiceTestBaseForJunit5;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.assertTrue;

class CalSalesServiceTest extends CommonServiceTestBaseForJunit5 {
    @Autowired
    private CalSalesService calSalesService;

    @Test
    void getSalesList_조회_성공() {
        //given
        CalSalesListRequestDto dto = new CalSalesListRequestDto();
        dto.setErpDateSearchStart("20210813");
        dto.setErpDateSearchEnd("20210815");
        dto.setDateSearchStart("20210813");
        dto.setDateSearchEnd("20210815");

        //when
        CalSalesListResponseDto result = calSalesService.getSalesList(dto);

        //then
        assertTrue(result.getTotal() > 0);
    }

}