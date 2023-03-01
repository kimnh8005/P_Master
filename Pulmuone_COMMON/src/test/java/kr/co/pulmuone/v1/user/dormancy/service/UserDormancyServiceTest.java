package kr.co.pulmuone.v1.user.dormancy.service;

import kr.co.pulmuone.v1.comm.CommonServiceTestBaseForJunit5;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.base.vo.BuyerVo;
import kr.co.pulmuone.v1.comm.enums.BaseEnums;
import kr.co.pulmuone.v1.comm.enums.UserEnums;
import kr.co.pulmuone.v1.comm.util.SessionUtil;
import kr.co.pulmuone.v1.user.buyer.dto.GetBuyerRequestDto;
import kr.co.pulmuone.v1.user.buyer.dto.GetBuyerResponseDto;
import kr.co.pulmuone.v1.user.buyer.service.UserBuyerBosBiz;
import kr.co.pulmuone.v1.user.buyer.service.UserBuyerBosService;
import kr.co.pulmuone.v1.user.certification.dto.GetSessionUserCertificationResponseDto;
import kr.co.pulmuone.v1.user.certification.service.UserCertificationBiz;
import kr.co.pulmuone.v1.user.dormancy.dto.*;
import kr.co.pulmuone.v1.user.dormancy.dto.vo.GetIsCheckUserMoveResultVo;
import kr.co.pulmuone.v1.user.dormancy.dto.vo.UserDormancyResultVo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.*;

class UserDormancyServiceTest extends CommonServiceTestBaseForJunit5 {

    @Autowired
    private UserDormancyService userDormancyService;

    @Autowired
    private UserBuyerBosBiz userBuyerBosBiz;

    @Test
    void 본인인증_인증정보_없음() throws Exception {
        // given
        BuyerVo buyerVo = new BuyerVo();
        buyerVo.setPersonalCertificationMobile("");
        buyerVo.setPersonalCertificationGender("");
        buyerVo.setPersonalCertificationCiCd("");
        buyerVo.setPersonalCertificationBirthday("");
        buyerVo.setPersonalCertificationUserName("");
        SessionUtil.setUserVO(buyerVo);

        // when
        ApiResult<?> result = userDormancyService.putUserActive("12345");

        // then
        assertTrue(UserEnums.Join.NOT_ANY_CERTI.getCode().equals(result.getCode()));
        SessionUtil.setUserVO(null);
    }

    @Test
    void 휴면_본인인증_정보_불일치() throws Exception {
        // given
        BuyerVo buyerVo = new BuyerVo();
        buyerVo.setUrUserId("02021");
        buyerVo.setPersonalCertificationMobile("");
        buyerVo.setPersonalCertificationGender("");
        buyerVo.setPersonalCertificationCiCd("asdasd29123123");
        buyerVo.setPersonalCertificationBirthday("");
        buyerVo.setPersonalCertificationUserName("");
        SessionUtil.setUserVO(buyerVo);

        // when
        ApiResult<?> result = userDormancyService.putUserActive("12345");

        // then
        assertTrue(UserEnums.Join.NO_FIND_USER_MOVE.getCode().equals(result.getCode()));
    }

    @Test
    void 휴면회원_해제시_비밀번호가_아이디와같은경우_실패() throws Exception{
        // given
        BuyerVo buyerVo = new BuyerVo();
        buyerVo.setLoginId("forbiz_1");
        buyerVo.setPersonalCertificationMobile("");
        buyerVo.setPersonalCertificationGender("");
        buyerVo.setPersonalCertificationCiCd("123");
        buyerVo.setPersonalCertificationBirthday("");
        buyerVo.setPersonalCertificationUserName("");
        SessionUtil.setUserVO(buyerVo);

        // when
        ApiResult<?> result = userDormancyService.putUserActive("forbiz_1");

        // then
        assertTrue(result.getCode().equals(UserEnums.Join.ID_PW_SAME_NOTI.getCode()));
        SessionUtil.setUserVO(null);
    }

    @Test
    void 휴면회원_해제_성공() throws Exception{
        // given
        BuyerVo buyerVo = new BuyerVo();
        buyerVo.setPersonalCertificationMobile("");
        buyerVo.setPersonalCertificationGender("");
        buyerVo.setPersonalCertificationCiCd("123");
        buyerVo.setPersonalCertificationBirthday("");
        buyerVo.setPersonalCertificationUserName("");
        SessionUtil.setUserVO(buyerVo);

        // when
        ApiResult<?> result = userDormancyService.putUserActive("Platform12#");

        // then
        assertTrue(result.getCode().equals(BaseEnums.Default.SUCCESS.getCode()));
        SessionUtil.setUserVO(null);
    }

    @Test
    void 휴면회원_정상회원_전환_성공() throws Exception {
        // given
        CommonPutUserDormantRequestDto putDto = new CommonPutUserDormantRequestDto();
        putDto.setUrUserId("1069071");
        putDto.setUrUserMoveId("48");
        GetBuyerRequestDto searchDto = new GetBuyerRequestDto();
        searchDto.setUrUserId("1069071");

        // when
        userDormancyService.putUserDormant(putDto);
        GetBuyerResponseDto result = userBuyerBosBiz.getBuyer(searchDto);

        // then
        assertTrue(putDto.getUrUserId().equals(result.getRows().getUrUserId()));
    }

    @Test
    void 휴면회원_목록_조회_데이터있음() throws Exception {
        // given
        GetUserDormantListRequestDto searchDto = new GetUserDormantListRequestDto();

        // when
        GetUserDormantListResponseDto result = userDormancyService.getUserDormantList(searchDto);

        // then
        assertTrue(result.getTotal() > 0);
    }

    @Test
    void 휴면회원_이력목록_조회_데이터있음() throws Exception {
        // given
        GetUserDormantHistoryListRequestDto searchDto = new GetUserDormantHistoryListRequestDto();
        searchDto.setStartCreateDate("20200622");
        searchDto.setEndModifyDate("20200922");

        // when
        GetUserDormantHistoryListResponseDto result =userDormancyService.getUserDormantHistoryList(searchDto);

        // then
        assertTrue(result.getTotal() > 0);

    }

    @Test
    void 휴면회원_정보_존재함() throws Exception {
        // given
        String urUserId = "1069071";

        // when
        GetIsCheckUserMoveResultVo result = userDormancyService.isCheckUserMove(urUserId);

        // then
        assertNotNull(result);
    }

    @Test
    void getUserDormancyInfo_성공() throws Exception{
    	Long urUserId = 421702L;

    	UserDormancyResultVo userDormancyResultVo = userDormancyService.getUserDormancyInfo(urUserId);

    	assertEquals(userDormancyResultVo.getUrUserId(),urUserId);
    }


    @Test
    void getUserDormancyExpected_성공() throws Exception{
    	Long urUserId = 1069109L;

    	UserDormancyResultVo userDormancyResultVo = userDormancyService.getUserDormancyExpected(urUserId);

    	assertEquals(userDormancyResultVo.getUrUserId(),urUserId);
    }

}