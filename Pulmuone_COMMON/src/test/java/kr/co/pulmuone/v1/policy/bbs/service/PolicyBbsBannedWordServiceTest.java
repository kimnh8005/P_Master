package kr.co.pulmuone.v1.policy.bbs.service;

import kr.co.pulmuone.v1.comm.CommonServiceTestBaseForJunit5;
import kr.co.pulmuone.v1.policy.bbs.dto.PolicyBbsBannedWordDto;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.assertTrue;

@Slf4j
class PolicyBbsBannedWordServiceTest extends CommonServiceTestBaseForJunit5 {

    @Autowired
    private PolicyBbsBannedWordService policyBbsBannedWordService;

    @Test
    void 게시판금칙어설정_조회() throws Exception {

    	// given
    	PolicyBbsBannedWordDto dto = new PolicyBbsBannedWordDto();

        // when
    	dto = policyBbsBannedWordService.getPolicyBbsBannedWord(dto);

        // then
        assertTrue(dto != null);

    }

    @Test
    void 게시판금칙어설정_수정() throws Exception{
        // given
    	PolicyBbsBannedWordDto requestVo = new PolicyBbsBannedWordDto();
    	requestVo.setMallBannedWord("MALLBANNEDWORD");
    	requestVo.setMallUseYn("Y");
    	requestVo.setBosBannedWord("BOSBANNEDWORD");
    	requestVo.setBosUseYn("Y");
        log.info("requestVo: {}", requestVo);

        // when
        int updatedCount = policyBbsBannedWordService.putPolicyBbsBannedWord(requestVo);

        // then
        assertTrue(updatedCount > 0);
    }
}
