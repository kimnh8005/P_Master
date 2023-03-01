package kr.co.pulmuone.v1.user.buyer.service;

import kr.co.pulmuone.v1.comm.CommonServiceTestBaseForJunit5;
import kr.co.pulmuone.v1.comm.base.vo.UserVo;
import kr.co.pulmuone.v1.comm.util.SessionUtil;
import kr.co.pulmuone.v1.user.buyer.dto.*;
import kr.co.pulmuone.v1.user.buyer.dto.vo.BuyerStatusResultVo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.*;

class UserBuyerStatusServiceTest extends CommonServiceTestBaseForJunit5 {

    @Autowired
    private UserBuyerStatusService userBuyerStatusService;

    @Test
    void 정지회원_목록_조회_성공() throws Exception {
        // given
        GetUserStopListRequestDto searchDto = new GetUserStopListRequestDto();
        searchDto.setStartCreateDate("20200620");
        searchDto.setEndCreateDate("20200920");

        // when
        GetUserStopListResponseDto responseDto = userBuyerStatusService.getBuyerStopList(searchDto);

        // then
        assertTrue(responseDto.getTotal() > 0);
    }

    @Test
    void 정지회원_변경이력_로그_조회_데이터있음() throws Exception {
        // given
        GetUserStopHistoryRequestDto searchDto = new GetUserStopHistoryRequestDto();
        searchDto.setUrBuyerStatusLogId("201");

        // when
        GetUserStopHistoryResponseDto result = userBuyerStatusService.getBuyerStopLog(searchDto);

        // then
        assertNotNull(result.getRows());
    }

    @Test
    void 정지회원_이력_목록_조회_성공() throws Exception {
        // given
        GetUserStopHistoryListRequestDto searchDto = new GetUserStopHistoryListRequestDto();
        searchDto.setStartStopDate("20200620");
        searchDto.setEndStopDate("20200920");

        // when
        GetUserStopHistoryListResponseDto responseDto = userBuyerStatusService.getBuyerStopHistoryList(searchDto);

        // then
        assertTrue(responseDto.getTotal() > 0);
    }

    @Test
    void 정지회원_변경이력_목록_조회_성공() throws Exception {
        // given
        GetBuyerStatusHistoryListRequestDto searchDto = new GetBuyerStatusHistoryListRequestDto();
        searchDto.setUrUserId("1646974");

        // when
        GetBuyerStatusHistoryListResponseDto responseDto = userBuyerStatusService.getBuyerStatusHistoryList(searchDto);

        // then
        assertTrue(responseDto.getRows().size() > 0);
    }

    @Test
    void 일반회원을_정지상태로_변경_성공() throws Exception {
        // given
        UserVo userVO = new UserVo();
        userVO.setUserId("1");
        userVO.setLoginId("forbiz");
        userVO.setLangCode("1");
        SessionUtil.setUserVO(userVO);

        PutBuyerStopRequestDto dto = new PutBuyerStopRequestDto();
        dto.setUrUserId("1646974");
        dto.setReason("테스트케이스에서 정지상태전환 자동작성");

        // when
        PutBuyerStopResponseDto responseDto = userBuyerStatusService.putBuyerStop(dto);

        // then
        assertTrue(dto.getUrUserId().equals(responseDto.getUrUserId()));
        SessionUtil.setUserVO(null);
    }

    @Test
    void 회원상태_결과_조회() {
    	//given
    	String urUserId = "1647263";

    	//when
    	BuyerStatusResultVo buyerStatusResultVo = userBuyerStatusService.getBuyerStatusConvertInfo(urUserId);

    	//then
    	assertEquals("210118 정지설정", buyerStatusResultVo.getReason());

    }

    @Test
    void 정지회원을_정상상태로_변경_성공() throws Exception {
        // given
        UserVo userVO = new UserVo();
        userVO.setUserId("1");
        userVO.setLoginId("forbiz");
        userVO.setLangCode("1");
        SessionUtil.setUserVO(userVO);

        PutBuyerNormalRequestDto dto = new PutBuyerNormalRequestDto();
        dto.setUrUserId("1646974");
        dto.setReason("테스트케이스에서 정상상태전환 자동작성");

        // when
        PutBuyerNormalResponseDto responseDto = userBuyerStatusService.putBuyerNormal(dto);

        // then
        assertTrue(dto.getUrUserId().equals(responseDto.getUrUserId()));
        SessionUtil.setUserVO(null);
    }
}