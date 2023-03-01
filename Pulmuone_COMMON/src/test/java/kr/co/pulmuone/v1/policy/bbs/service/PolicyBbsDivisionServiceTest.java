package kr.co.pulmuone.v1.policy.bbs.service;

import kr.co.pulmuone.v1.comm.CommonServiceTestBaseForJunit5;
import kr.co.pulmuone.v1.comm.mapper.policy.bbs.PolicyBbsMapper;
import kr.co.pulmuone.v1.policy.bbs.dto.PolicyBbsDivisionDto;
import kr.co.pulmuone.v1.policy.bbs.dto.vo.PolicyBbsDivisionVo;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@Slf4j
class PolicyBbsDivisionServiceTest extends CommonServiceTestBaseForJunit5 {

    @Autowired
    private PolicyBbsDivisionService policyBbsDivisionService;

    @InjectMocks
    private PolicyBbsDivisionService mockPolicyBbsDivisionService;

    @Mock
    PolicyBbsMapper mockPolicyBbsMapper;

    @BeforeEach
    void setUp() {
        preLogin();
    }

    @Test
    public void test_키워드_결과없음()  {
    	String keyword = "453bh67dfbyh";

    	// given
    	PolicyBbsDivisionDto dto = new PolicyBbsDivisionDto();
    	dto.setBbsTp(keyword);
    	dto.setCategoryNm(keyword);

        // when
    	PolicyBbsDivisionDto result = policyBbsDivisionService.getPolicyBbsDivisionList(dto);

    	// then
        assertEquals(0, result.getTotal());
    }


    @Test
    public void test_키워드_결과있음()  {
    	String keyword = "";

    	// given
    	PolicyBbsDivisionDto dto = new PolicyBbsDivisionDto();
    	dto.setBbsTp(keyword);
    	dto.setCategoryNm(keyword);

        // when
    	PolicyBbsDivisionDto result = policyBbsDivisionService.getPolicyBbsDivisionList(dto);

    	// then
        assertNotNull(result.getRows());
    }

    @Test
    public void test_상위분류_결과있음()  {

    	// given
    	String bbsTp = "BBS_TP.GENERAL";

    	// when
    	PolicyBbsDivisionDto result = policyBbsDivisionService.getPolicyBbsDivisionParentCategoryList(bbsTp);

    	// then
    	assertNotNull(result.getRows());
    }
    @Test
    public void 게시판분류_신규등록() {
    	String bbsTp = "BBS_TP.GENERAL";
    	String categoryNm = "게시판분류신규등록";
    	String useYn = "Y";

    	 // given
    	PolicyBbsDivisionDto dto = new PolicyBbsDivisionDto();
    	dto.setBbsTp(bbsTp);
    	dto.setCategoryNm(categoryNm);
    	dto.setUseYn(useYn);
        log.info("dto: {}", dto);

        // when
        int count = policyBbsDivisionService.addPolicyBbsDivision(dto);

        // then
        assertEquals(1, count);
    }


    @Test
    public void 게시판분류_수정() {
        // given
    	PolicyBbsDivisionDto dto = new PolicyBbsDivisionDto();
    	dto.setCsCategoryId("8");
    	dto.setBbsTp("BBS_TP.GENERAL");
    	dto.setCategoryNm("게시판분류수정");
    	dto.setUseYn("N");

        // when
        int count = policyBbsDivisionService.putPolicyBbsDivision(dto);

        // then
        assertEquals(1, count);
    }

    @Test
    public void 게시판분류_삭제() {
    	// given
    	Long csCategoryId = 8L;

    	// when
    	int count = policyBbsDivisionService.delPolicyBbsDivision(csCategoryId);

    	// then
    	assertEquals(1, count);
    }

    @Test
    void 게시판분류설정_조회() {
        Long csCategoryId = 0L;
        PolicyBbsDivisionVo policyBbsDivisionVo = new PolicyBbsDivisionVo();

        given(mockPolicyBbsDivisionService.getPolicyBbsDivisionInfo(any())).willReturn(policyBbsDivisionVo);

        //when
        PolicyBbsDivisionVo policyBbsDivisionInfo = mockPolicyBbsMapper.getPolicyBbsDivisionInfo(csCategoryId);

        //then
        assertNotNull(policyBbsDivisionInfo);
    }

    @Test
    void 게시판분류설정_신규_등록() {
        PolicyBbsDivisionDto policyBbsDivisionDto = new PolicyBbsDivisionDto();
        policyBbsDivisionDto.setBbsTp("notice");
        policyBbsDivisionDto.setCategoryNm("과일");
        policyBbsDivisionDto.setUseYn("Y");

        given(mockPolicyBbsDivisionService.addPolicyBbsDivision(any())).willReturn(1);

        //when
        int result = mockPolicyBbsMapper.addPolicyBbsDivision(policyBbsDivisionDto);

        //then
        assertEquals(1, result);
    }
}
