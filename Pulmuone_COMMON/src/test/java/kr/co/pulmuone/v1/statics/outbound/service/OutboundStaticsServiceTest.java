package kr.co.pulmuone.v1.statics.outbound.service;

import kr.co.pulmuone.v1.comm.CommonServiceTestBaseForJunit5;
import kr.co.pulmuone.v1.comm.exception.BaseException;
import kr.co.pulmuone.v1.statics.outbound.dto.MissOutboundStaticsRequestDto;
import kr.co.pulmuone.v1.statics.outbound.dto.MissOutboundStaticsResponseDto;
import kr.co.pulmuone.v1.statics.outbound.dto.OutboundStaticsRequestDto;
import kr.co.pulmuone.v1.statics.outbound.dto.OutboundStaticsResponseDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class OutboundStaticsServiceTest extends CommonServiceTestBaseForJunit5 {

    @Autowired
    private OutboundStaticsService outboundStaticsService;

    @Test
    public void test_출고통계리스트조회() throws BaseException {
        //given
        OutboundStaticsRequestDto dto = new OutboundStaticsRequestDto();
        dto.setSearchType("WAREHOUSE");
        dto.setSearchDateType("ORDER_DT");
        dto.setFindYear("2021");
        dto.setFindMonth("04");
        dto.setSupplierFilter("ALL");
        dto.setStorageMethodFilter("ALL");

        //when
        OutboundStaticsResponseDto result = outboundStaticsService.getOutboundStaticsList(dto);

        //then
        assertTrue(result.getRows().size() > 0);
    }

    @Test
    public void test_미출통계리스트조회() throws BaseException {
        //given
        MissOutboundStaticsRequestDto dto = new MissOutboundStaticsRequestDto();
        dto.setStartDe("20210501");
        dto.setEndDe("20210515");
        dto.setSupplierFilter("ALL");
        dto.setSearchDelivFilter("ALL");

        //when
        MissOutboundStaticsResponseDto result = outboundStaticsService.getMissOutboundStaticsList(dto);

        //then
        assertTrue(result.getRows().size() > 0);
    }

}
