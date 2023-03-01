package kr.co.pulmuone.v1.user.buyer.service;

import kr.co.pulmuone.v1.comm.CommonServiceTestBaseForJunit5;
import kr.co.pulmuone.v1.comm.base.vo.UserVo;
import kr.co.pulmuone.v1.comm.util.ExcelUploadUtil;
import kr.co.pulmuone.v1.comm.util.SessionUtil;
import kr.co.pulmuone.v1.user.dormancy.dto.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.*;

class UserBuyerBlackListServiceTest extends CommonServiceTestBaseForJunit5 {

    @Autowired
    private UserBuyerBlackListService userBuyerBlackListService;

    @Test
    void 블랙리스트로등록된_회원_조회_데이터있음() throws Exception {
        // given
        UserVo userVO = new UserVo();
        userVO.setUserId("1");
        userVO.setLoginId("forbiz");
        userVO.setLangCode("1");
        SessionUtil.setUserVO(userVO);

        GetUserBlackListRequestDto searchDto = new GetUserBlackListRequestDto();
        searchDto.setCondiType("userName");
        searchDto.setCondiValue("홍길동");
        searchDto.setMail("jihun001@naver.com");

        // when
        GetUserBlackListResponseDto responseDto = userBuyerBlackListService.getBlackListUserList(searchDto);

        // then
        assertTrue(responseDto.getTotal() > 0);
        SessionUtil.setUserVO(null);
    }

    @Test
    void 블랙리스트_이력_조회_데이터있음() throws Exception {
        // given
        GetUserBlackHistoryListRequestDto searchDto = new GetUserBlackHistoryListRequestDto();
        searchDto.setUrUserId("1646977");

        // when
        GetUserBlackHistoryListResponseDto responseDto = userBuyerBlackListService.getUserBlackHistoryList(searchDto);

        // then
        assertTrue(responseDto.getRows().size() > 0);
    }

    @Test
    void 회원_블랙리스트로_등록_성공() throws Exception {
        // given
        UserVo userVO = new UserVo();
        userVO.setUserId("1");
        userVO.setLoginId("forbiz");
        userVO.setLangCode("1");
        SessionUtil.setUserVO(userVO);

        AddUserBlackRequestDto addDto = AddUserBlackRequestDto.builder()
                .urUserId("1646769")
                .userBlackReason("테스트케이스에서 블랙리스트 등록")
                .build();

        // when
        AddUserBlackResponseDto responseDto = userBuyerBlackListService.addUserBlack(addDto);

        // then
        assertTrue(addDto.getUrUserId().equals(responseDto.getUrUserId()));
        SessionUtil.setUserVO(null);
    }
}