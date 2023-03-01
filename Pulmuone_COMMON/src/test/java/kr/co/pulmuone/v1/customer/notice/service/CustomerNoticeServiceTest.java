package kr.co.pulmuone.v1.customer.notice.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.github.pagehelper.Page;

import kr.co.pulmuone.v1.comm.CommonServiceTestBaseForJunit5;
import kr.co.pulmuone.v1.comm.base.vo.UserVo;
import kr.co.pulmuone.v1.comm.util.SessionUtil;
import kr.co.pulmuone.v1.customer.notice.dto.GetNoticeByUserResponseDto;
import kr.co.pulmuone.v1.customer.notice.dto.GetNoticeListByUserRequsetDto;
import kr.co.pulmuone.v1.customer.notice.dto.NoticeBosRequestDto;
import kr.co.pulmuone.v1.customer.notice.dto.vo.GetNoticeListByUserResultVo;
import kr.co.pulmuone.v1.customer.notice.dto.vo.NoticeBosDetailVo;
import kr.co.pulmuone.v1.customer.notice.dto.vo.NoticeBosListVo;

public class CustomerNoticeServiceTest extends CommonServiceTestBaseForJunit5 {

	 @Autowired
	 private CustomerNoticeService customerNoticeService;

	 @Test
	 void getNoticeListByUser_정상() throws Exception {
		 GetNoticeListByUserRequsetDto getNoticeListByUserRequsetDto = new GetNoticeListByUserRequsetDto();
		 List<GetNoticeListByUserResultVo> getNoticeListByUserResultVo = customerNoticeService.getNoticeListByUser(getNoticeListByUserRequsetDto);

		 assertTrue(CollectionUtils.isNotEmpty(getNoticeListByUserResultVo));

	 }

	 @Test
	 void getNoticeByUser_정상() throws Exception {

		 Long csNoticeId = (long) 4;

		 GetNoticeByUserResponseDto getNoticeByUserResponseDto = customerNoticeService.getNoticeByUser(csNoticeId);

		 assertEquals("NOTICE_TP.EVENT", getNoticeByUserResponseDto.getNoticeType());

	 }

	 @Test
	 void getNoticeList_성공() throws Exception {

		 NoticeBosRequestDto dto = new NoticeBosRequestDto();

        // when
        Page<NoticeBosListVo> voList = customerNoticeService.getNoticeList(dto);

        assertNotNull(voList.getResult());
    }

	 @Test
	 void getDetailNotice_정상() throws Exception {

		 NoticeBosRequestDto dto = new NoticeBosRequestDto();
	   	dto.setNoticeId("-1");

	   	NoticeBosDetailVo vo = customerNoticeService.getDetailNotice(dto);

	   	Assertions.assertNull(vo);
	}

    @Test
    void addNoticeInfo_성공() throws Exception {
        // given
    	NoticeBosRequestDto dto = new NoticeBosRequestDto();
        dto.setNoticeType("NOTICE_TP.NORMAL");
        dto.setNoticeTitle("TEST");
        dto.setContent("CONTENT");
        dto.setDisplayYn("Y");
        UserVo userVO = new UserVo();
        userVO.setUserId("1");
        userVO.setLoginId("forbiz");
        userVO.setLangCode("1");
        SessionUtil.setUserVO(userVO);

        //when, then
        customerNoticeService.addNoticeInfo(dto);
    }

    @Test
    void putNoticeInfo_성공() throws Exception {
        // given
    	NoticeBosRequestDto dto = new NoticeBosRequestDto();
        dto.setNoticeType("NOTICE_TP.NORMAL");
        dto.setNoticeTitle("TEST UPDATE");
        dto.setContent("CONTENT UPDATE");
        dto.setDisplayYn("N");
        dto.setNoticeId("1");
        UserVo userVO = new UserVo();
        userVO.setUserId("1");
        userVO.setLoginId("forbiz");
        userVO.setLangCode("1");
        SessionUtil.setUserVO(userVO);

        //when, then
        customerNoticeService.putNoticeInfo(dto);
    }

    @Test
    void deleteNoticeInfo_성공() throws Exception {
        // given
    	NoticeBosRequestDto dto = new NoticeBosRequestDto();
    	dto.setNoticeId("-1");

        //when, then
    	customerNoticeService.deleteNoticeInfo(dto);
    }
}
