package kr.co.pulmuone.v1.statics.claim.service;

import kr.co.pulmuone.v1.comm.CommonServiceTestBaseForJunit5;
import kr.co.pulmuone.v1.statics.claim.dto.ClaimReasonStaticsRequestDto;
import kr.co.pulmuone.v1.statics.claim.dto.ClaimReasonStaticsResponseDto;
import kr.co.pulmuone.v1.statics.claim.dto.ClaimStaticsRequestDto;
import kr.co.pulmuone.v1.statics.claim.dto.ClaimStaticsResponseDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.assertTrue;

class ClaimStaticsServiceTest extends CommonServiceTestBaseForJunit5 {

    @Autowired
    private ClaimStaticsService claimStaticsService;

    @Test
    void getClaimStaticsList_조회_성공() {
        //given
        ClaimStaticsRequestDto dto = new ClaimStaticsRequestDto();

        //when
        ClaimStaticsResponseDto response = claimStaticsService.getClaimStaticsList(dto);

        //then
        assertTrue(response.getTotal() > 0);
    }

    @Test
    void getClaimReasonStaticsList_조회_성공() {
        //given
        ClaimReasonStaticsRequestDto dto = new ClaimReasonStaticsRequestDto();

        //when
        ClaimReasonStaticsResponseDto response = claimStaticsService.getClaimReasonStaticsList(dto);

        //then
        assertTrue(response.getTotal() > 0);
    }

}