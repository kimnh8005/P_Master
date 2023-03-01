package kr.co.pulmuone.v1.user.buyer.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import kr.co.pulmuone.v1.comm.CommonServiceTestBaseForJunit5;
import kr.co.pulmuone.v1.user.buyer.dto.GetUserDropListRequestDto;
import kr.co.pulmuone.v1.user.buyer.dto.GetUserDropListResponseDto;
import kr.co.pulmuone.v1.user.buyer.dto.UserDropRequestDto;
import kr.co.pulmuone.v1.user.buyer.dto.vo.UserDropResultVo;

class UserBuyerDropServiceTest extends CommonServiceTestBaseForJunit5 {

    @Autowired
    private UserBuyerDropService userBuyerDropService;

    @Test
    void 탈퇴회원_목록_조회_있음() throws Exception {
        // given
        GetUserDropListRequestDto searchDto = new GetUserDropListRequestDto();
        searchDto.setStartCreateDate("20200620");
        searchDto.setEndCreateDate("20200920");

        // when
        GetUserDropListResponseDto result = userBuyerDropService.getUserDropList(searchDto);

        // then
        assertTrue(result.getRows().size() > 0);
    }

    @Test
    void progressUserDrop() throws Exception {
        //given
        UserDropRequestDto dto = new UserDropRequestDto();
        dto.setUrUserId(0L);
        dto.setCode(1L);
        dto.setComment("test");
        dto.setCreateId(1L);

        //when, then
        userBuyerDropService.progressUserDrop(dto);
    }

    @Test
    void 회원탈퇴_완료_시_회원탈퇴결과_조회() {
    	//given
    	Long urUserDropId = (long)1;

    	//when
    	UserDropResultVo userDropResultVo = userBuyerDropService.getUserDropInfo(urUserDropId);

    	//then
    	assertEquals(421606, userDropResultVo.getUrUserId());
    }

}