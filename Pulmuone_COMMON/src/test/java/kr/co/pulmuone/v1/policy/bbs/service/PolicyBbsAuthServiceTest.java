package kr.co.pulmuone.v1.policy.bbs.service;

import kr.co.pulmuone.v1.comm.CommonServiceTestBaseForJunit5;
import kr.co.pulmuone.v1.comm.mapper.policy.bbs.PolicyBbsMapper;
import kr.co.pulmuone.v1.policy.bbs.dto.PolicyBbsAuthDto;
import kr.co.pulmuone.v1.policy.bbs.dto.PolicyBbsDivisionDto;
import kr.co.pulmuone.v1.policy.bbs.dto.vo.PolicyBbsAuthVo;
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
class PolicyBbsAuthServiceTest extends CommonServiceTestBaseForJunit5 {


	@Autowired
    private PolicyBbsAuthService policyBbsAuthService;

	@InjectMocks
	private PolicyBbsAuthService mockPolicyBbsAuthService;

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
    	PolicyBbsAuthDto dto = new PolicyBbsAuthDto();
    	dto.setBbsTp(keyword);
    	dto.setBbsNm(keyword);

        // when
    	PolicyBbsAuthDto result = policyBbsAuthService.getPolicyBbsAuthList(dto);

    	// then
		assertEquals(0, result.getTotal());
    }


    @Test
    public void test_키워드_결과있음()  {
    	String keyword = "";

    	// given
    	PolicyBbsAuthDto dto = new PolicyBbsAuthDto();
    	dto.setBbsTp(keyword);
    	dto.setBbsNm(keyword);

        // when
    	PolicyBbsAuthDto result = policyBbsAuthService.getPolicyBbsAuthList(dto);

    	// then
        assertNotNull(result.getRows());
    }

    @Test
    public void test_분류_결과있음()  {

    	// given
    	String bbsTp = "";

    	// when
    	PolicyBbsDivisionDto result = policyBbsAuthService.getPolicyBbsAuthCategoryList(bbsTp);

    	// then
    	assertNotNull(result.getRows());
    }
    @Test
    public void 게시판권한_신규등록() {
    	String bbsNm = "게시판권한신규등록";
    	String csCategoryId = "1";
    	String imageYn = "Y";
    	String attachYn = "Y";
    	String replyYn = "Y";
    	String commentYn = "Y";
    	String commentSecretYn = "Y";
    	String recommendYn = "Y";

    	 // given
    	PolicyBbsAuthDto dto = new PolicyBbsAuthDto();
    	dto.setBbsNm(bbsNm);
    	dto.setCsCategoryId(csCategoryId);
    	dto.setImageYn(imageYn);
    	dto.setAttachYn(attachYn);
    	dto.setReplyYn(replyYn);
    	dto.setCommentYn(commentYn);
    	dto.setCommentSecretYn(commentSecretYn);
    	dto.setRecommendYn(recommendYn);
        log.info("dto: {}", dto);

        // when
        int count = policyBbsAuthService.addPolicyBbsAuth(dto);

        // then
        assertEquals(1, count);
    }


    @Test
    public void 게시판권한_수정() {
    	 // given
    	PolicyBbsAuthDto dto = new PolicyBbsAuthDto();
    	dto.setCsBbsConfigId("20");
    	dto.setBbsNm("게시판권한테스트");
    	dto.setCsCategoryId("1");
    	dto.setImageYn("Y");
    	dto.setAttachYn("Y");
    	dto.setReplyYn("Y");
    	dto.setCommentYn("Y");
    	dto.setCommentSecretYn("Y");
    	dto.setRecommendYn("Y");
        log.info("dto: {}", dto);

        // when
        int count = policyBbsAuthService.putPolicyBbsAuth(dto);

        // then
        assertEquals(1, count);
    }

    @Test
    public void 게시판권한_삭제() {
    	// given
    	Long csBbsConfigId = 20L;

    	// when
    	int count = policyBbsAuthService.delPolicyBbsAuth(csBbsConfigId);

    	// then
    	assertEquals(1, count);
    }

    @Test
    void 게시판권한설정_조회_실패() {

		Long csBbsConfigId = 0L;
		PolicyBbsAuthVo policyBbsAuthVo = new PolicyBbsAuthVo();


		given(mockPolicyBbsAuthService.getPolicyBbsAuthInfo(any())).willReturn(policyBbsAuthVo);

		//when
		PolicyBbsAuthVo result = mockPolicyBbsMapper.getPolicyBbsAuthInfo(csBbsConfigId);

		//then
		assertNull(result.getBbsNm());
    }

    @Test
    void 게시판권한설정_신규_등록() {

		PolicyBbsAuthDto policyBbsAuthDto = new PolicyBbsAuthDto();
		policyBbsAuthDto.setCsCategoryId("1");
		policyBbsAuthDto.setBbsNm("notice");
		policyBbsAuthDto.setImageYn("N");
		policyBbsAuthDto.setAttachYn("N");
		policyBbsAuthDto.setReplyYn("Y");
		policyBbsAuthDto.setCommentYn("N");
		policyBbsAuthDto.setCommentSecretYn("N");
		policyBbsAuthDto.setRecommendYn("N");

		given(mockPolicyBbsAuthService.addPolicyBbsAuth(any())).willReturn(1);

		//when
		int result = mockPolicyBbsMapper.addPolicyBbsAuth(policyBbsAuthDto);

		//then
		assertEquals(1, result);
    }
}
