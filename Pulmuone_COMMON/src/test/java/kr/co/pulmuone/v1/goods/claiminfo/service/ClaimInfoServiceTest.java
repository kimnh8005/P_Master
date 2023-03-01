package kr.co.pulmuone.v1.goods.claiminfo.service;

import kr.co.pulmuone.v1.comm.CommonServiceTestBaseForJunit5;
import kr.co.pulmuone.v1.goods.claiminfo.dto.ClaimInfoRequestDto;
import kr.co.pulmuone.v1.goods.claiminfo.dto.ClaimInfoResponseDto;
import kr.co.pulmuone.v1.goods.claiminfo.dto.vo.ClaimInfoVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
public class ClaimInfoServiceTest extends CommonServiceTestBaseForJunit5 {

    @Autowired
    private ClaimInfoService claimInfoService;

    @Test
    void 배송_반품_취소안내_목록조회_결과있음() {
        // given
        ClaimInfoRequestDto dto = new ClaimInfoRequestDto();
        dto.setGbLangId("1");

        // when
        List<ClaimInfoVo> result = claimInfoService.getClaimInfoList(dto);

        //then
        assertTrue(CollectionUtils.isNotEmpty(result));
    }

    @Test
    void 배송_반품_취소안내_상세조회_결과있음() {
        // given
        String ilClaimInfoId = "5";

        // when
        ClaimInfoVo dto = claimInfoService.getClaimInfo(ilClaimInfoId);

        // then
        assertNotNull(dto);
    }

    @Test
    void 배송_반품_취소안내_수정() {
        //given
        ClaimInfoRequestDto dto = new ClaimInfoRequestDto();
        dto.setIlClaimInfoId(1);
        dto.setTemplateName("test");
        dto.setUseYn("N");
        dto.setDescribe("설명");

        // when
        int count = claimInfoService.putClaimInfo(dto);

        // then
        assertTrue(count > 0);
    }

    @Test
    void getClaimInfoList() {
        List<ClaimInfoVo> result = claimInfoService.getClaimInfoList(null);
        assertTrue(result.size() > 0);
    }
}
