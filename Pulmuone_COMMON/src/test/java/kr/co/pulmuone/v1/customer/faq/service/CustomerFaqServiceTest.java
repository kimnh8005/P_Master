package kr.co.pulmuone.v1.customer.faq.service;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.github.pagehelper.Page;

import kr.co.pulmuone.v1.comm.CommonServiceTestBaseForJunit5;
import kr.co.pulmuone.v1.comm.base.vo.UserVo;
import kr.co.pulmuone.v1.comm.util.SessionUtil;
import kr.co.pulmuone.v1.customer.faq.dto.FaqBosRequestDto;
import kr.co.pulmuone.v1.customer.faq.dto.GetFaqListByUserRequsetDto;
import kr.co.pulmuone.v1.customer.faq.dto.vo.FaqBosDetailVo;
import kr.co.pulmuone.v1.customer.faq.dto.vo.FaqBosListVo;
import kr.co.pulmuone.v1.customer.faq.dto.vo.GetFaqListByUserResultVo;

public class CustomerFaqServiceTest extends CommonServiceTestBaseForJunit5{

	@Autowired
    private CustomerFaqService customerFaqService;

	@BeforeEach
    void beforeEach() {
        preLogin();
    }

	@Test
	void FAQ_게시판_리스트_조회() throws Exception {
		GetFaqListByUserRequsetDto getFaqListByUserRequsetDto = new GetFaqListByUserRequsetDto();
		List<GetFaqListByUserResultVo> getFaqListByUserResultVo = customerFaqService.getFaqListByUser(getFaqListByUserRequsetDto);

		assertTrue(CollectionUtils.isNotEmpty(getFaqListByUserResultVo));
	}

	@Test
    void getFaqList_성공() throws Exception {

        FaqBosRequestDto dto = new FaqBosRequestDto();

        // when
        Page<FaqBosListVo> voList = customerFaqService.getFaqList(dto);

        assertNotNull(voList.getResult());
    }


    @Test
    void getDetailFaq_정상() throws Exception {

    	FaqBosRequestDto dto = new FaqBosRequestDto();

    	dto.setFaqId("-1");

    	FaqBosDetailVo vo = customerFaqService.getDetailFaq(dto);

    	Assertions.assertNull(vo);
    }

    @Test
    void addFaqInfo_성공() throws Exception {
        // given
    	FaqBosRequestDto dto = new FaqBosRequestDto();
        dto.setFaqType("FAQ_TP.MEMBER");
        dto.setFaqTitle("TEST");
        dto.setContent("CONTENT");
        dto.setDisplayYn("Y");
        UserVo userVO = new UserVo();
        userVO.setUserId("1");
        userVO.setLoginId("forbiz");
        userVO.setLangCode("1");
        SessionUtil.setUserVO(userVO);

        //when, then
        customerFaqService.addFaqInfo(dto);
    }


    @Test
    void putFaqInfo_성공() throws Exception {
        // given
    	FaqBosRequestDto dto = new FaqBosRequestDto();
        dto.setFaqType("FAQ_TP.MEMBER");
        dto.setFaqTitle("TEST UPDATE");
        dto.setContent("CONTENT UPDATE");
        dto.setDisplayYn("N");
        UserVo userVO = new UserVo();
        userVO.setUserId("1");
        userVO.setLoginId("forbiz");
        userVO.setLangCode("1");
        SessionUtil.setUserVO(userVO);

        //when, then
        customerFaqService.putFaqInfo(dto);
    }


    @Test
    void deleteFaqInfo_성공() throws Exception {
        // given
    	FaqBosRequestDto dto = new FaqBosRequestDto();
    	dto.setFaqId("-1");

        //when, then
        customerFaqService.deleteFaqInfo(dto);
    }
}
