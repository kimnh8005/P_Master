package kr.co.pulmuone.v1.user.join.service;

import kr.co.pulmuone.v1.comm.CommonServiceTestBaseForJunit5;

import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.enums.UserEnums;
import kr.co.pulmuone.v1.comm.util.asis.dto.UserInfoCheckResponseDto;
import kr.co.pulmuone.v1.promotion.coupon.service.PromotionCouponBiz;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.HashMap;

import static org.mockito.BDDMockito.given;
import static org.mockito.ArgumentMatchers.any;

class UserJoinBizImplTest extends CommonServiceTestBaseForJunit5 {

    @InjectMocks
    private UserJoinBizImpl mockUserJoinBizImpl;

    @Mock
    private UserJoinService mockUserJoinService;

    @Mock
    private PromotionCouponBiz mockPromotionCouponBiz;

    @BeforeEach
    void beforeEach() {
        buyerLogin();
    }

    @Test
    void 회원인증이벤트_ASIS인증_풀샵진입_풀샵성공() throws Exception {
        //given
        String loginid = "sirilman23";
        String password = "123qweasd!";
        String siteno = "0000000000";

        UserInfoCheckResponseDto asisUserCheckDto = new UserInfoCheckResponseDto();
        asisUserCheckDto.setResultCode("1");
        asisUserCheckDto.setCustomerNumber("22432444");
        asisUserCheckDto.setResultMessage("0000000000,0000200000");

        given(mockPromotionCouponBiz.isPmCouponJoinDup(any())).willReturn(false);
        given(mockUserJoinService.asisUserCheck(any(), any())).willReturn(asisUserCheckDto);

        //when
        ApiResult<?> apiResult = mockUserJoinBizImpl.asisUserCheck(loginid, password, null, siteno);

        //then
        Assertions.assertEquals(apiResult.getMessageEnum(), ApiResult.success().getMessageEnum());
        Assertions.assertEquals(((HashMap<String, String>)apiResult.getData()).get("result_code"), "0");
        Assertions.assertEquals(((HashMap<String, String>)apiResult.getData()).get("result_siteno"), "0000000000");
    }

    @Test
    void 회원인증이벤트_ASIS인증_올가진입_올가성공() throws Exception {
        //given
        String loginid = "sirilman23";
        String password = "123qweasd!";
        String siteno = "0000200000";

        UserInfoCheckResponseDto asisUserCheckDto = new UserInfoCheckResponseDto();
        asisUserCheckDto.setResultCode("1");
        asisUserCheckDto.setCustomerNumber("22432444");
        asisUserCheckDto.setResultMessage("0000000000,0000200000");

        given(mockPromotionCouponBiz.isPmCouponJoinDup(any())).willReturn(false);
        given(mockUserJoinService.asisUserCheck(any(), any())).willReturn(asisUserCheckDto);

        //when
        ApiResult<?> apiResult = mockUserJoinBizImpl.asisUserCheck(loginid, password, null, siteno);

        //then
        Assertions.assertEquals(apiResult.getMessageEnum(), ApiResult.success().getMessageEnum());
        Assertions.assertEquals(((HashMap<String, String>)apiResult.getData()).get("result_code"), "0");
        Assertions.assertEquals(((HashMap<String, String>)apiResult.getData()).get("result_siteno"), "0000200000");
    }

    @Test
    void 회원인증이벤트_ASIS인증_풀샵진입_올가성공() throws Exception {
        //given
        String loginid = "sirilman23";
        String password = "123qweasd!";
        String siteno = "0000000000";

        UserInfoCheckResponseDto asisUserCheckDto = new UserInfoCheckResponseDto();
        asisUserCheckDto.setResultCode("1");
        asisUserCheckDto.setCustomerNumber("22432444");
        asisUserCheckDto.setResultMessage("0000200000");

        given(mockPromotionCouponBiz.isPmCouponJoinDup(any())).willReturn(false);
        given(mockUserJoinService.asisUserCheck(any(), any())).willReturn(asisUserCheckDto);

        //when
        ApiResult<?> apiResult = mockUserJoinBizImpl.asisUserCheck(loginid, password, null, siteno);

        //then
        Assertions.assertEquals(apiResult.getMessageEnum(), ApiResult.success().getMessageEnum());
        Assertions.assertEquals(((HashMap<String, String>)apiResult.getData()).get("result_code"), "0");
        Assertions.assertEquals(((HashMap<String, String>)apiResult.getData()).get("result_siteno"), "0000200000");
    }

    @Test
    void 회원인증이벤트_ASIS인증_올가진입_풀샵성공() throws Exception {
        //given
        String loginid = "sirilman23";
        String password = "123qweasd!";
        String siteno = "0000200000";

        UserInfoCheckResponseDto asisUserCheckDto = new UserInfoCheckResponseDto();
        asisUserCheckDto.setResultCode("1");
        asisUserCheckDto.setCustomerNumber("22432444");
        asisUserCheckDto.setResultMessage("0000000000");

        given(mockPromotionCouponBiz.isPmCouponJoinDup(any())).willReturn(false);
        given(mockUserJoinService.asisUserCheck(any(), any())).willReturn(asisUserCheckDto);

        //when
        ApiResult<?> apiResult = mockUserJoinBizImpl.asisUserCheck(loginid, password, null, siteno);

        //then
        Assertions.assertEquals(apiResult.getMessageEnum(), ApiResult.success().getMessageEnum());
        Assertions.assertEquals(((HashMap<String, String>)apiResult.getData()).get("result_code"), "0");
        Assertions.assertEquals(((HashMap<String, String>)apiResult.getData()).get("result_siteno"), "0000000000");
    }

    @Test
    void 회원인증이벤트_ASIS인증_진입값오류() throws Exception {
        //given
        String loginid = "sirilman23";
        String password = "123qweasd!";
        String siteno = "0000011111";

        UserInfoCheckResponseDto asisUserCheckDto = new UserInfoCheckResponseDto();
        asisUserCheckDto.setResultCode("1");
        asisUserCheckDto.setCustomerNumber("22432444");
        asisUserCheckDto.setResultMessage("0000000000,0000200000");

        given(mockPromotionCouponBiz.isPmCouponJoinDup(any())).willReturn(false);
        given(mockUserJoinService.asisUserCheck(any(), any())).willReturn(asisUserCheckDto);

        //when
        ApiResult<?> apiResult = mockUserJoinBizImpl.asisUserCheck(loginid, password, null, siteno);

        //then
        Assertions.assertEquals(apiResult.getMessageEnum(), ApiResult.success().getMessageEnum());
        Assertions.assertEquals(((HashMap<String, String>)apiResult.getData()).get("result_code"), "99");
        Assertions.assertEquals(((HashMap<String, String>)apiResult.getData()).get("result_siteno"), "");
    }

    @Test
    void 회원인증이벤트_ASIS인증_로그인실패() throws Exception {
        //given
        String loginid = "sirilman23";
        String password = "11111";
        String siteno = "0000011111";

        UserInfoCheckResponseDto asisUserCheckDto = new UserInfoCheckResponseDto();
        asisUserCheckDto.setResultCode("99");
        asisUserCheckDto.setCustomerNumber("");
        asisUserCheckDto.setResultMessage("");

        given(mockPromotionCouponBiz.isPmCouponJoinDup(any())).willReturn(false);
        given(mockUserJoinService.asisUserCheck(any(), any())).willReturn(asisUserCheckDto);

        //when
        ApiResult<?> apiResult = mockUserJoinBizImpl.asisUserCheck(loginid, password, null, siteno);

        //then
        Assertions.assertEquals(apiResult.getMessageEnum(), UserEnums.Join.LOGIN_FAIL);
    }

    @Test
    void 회원인증이벤트_ASIS인증_쿠폰미지급대상체크() throws Exception {
        //given
        String loginid = "sirilman23";
        String password = "123qweasd!";
        String siteno = "0000000000";

        UserInfoCheckResponseDto asisUserCheckDto = new UserInfoCheckResponseDto();
        asisUserCheckDto.setResultCode("1");
        asisUserCheckDto.setCustomerNumber("22432444");
        asisUserCheckDto.setResultMessage("0000300000");

        given(mockPromotionCouponBiz.isPmCouponJoinDup(any())).willReturn(false);
        given(mockUserJoinService.asisUserCheck(any(), any())).willReturn(asisUserCheckDto);

        //when
        ApiResult<?> apiResult = mockUserJoinBizImpl.asisUserCheck(loginid, password, null, siteno);

        //then
        Assertions.assertEquals(apiResult.getMessageEnum(), ApiResult.success().getMessageEnum());
        Assertions.assertEquals(((HashMap<String, String>)apiResult.getData()).get("result_code"), "91");
        Assertions.assertEquals(((HashMap<String, String>)apiResult.getData()).get("result_siteno"), "");
    }

    @Test
    void 회원인증이벤트_ASIS인증_쿠폰중복지급체크() throws Exception {
        //given
        String loginid = "sirilman23";
        String password = "123qweasd!";
        String siteno = "0000000000";

        UserInfoCheckResponseDto asisUserCheckDto = new UserInfoCheckResponseDto();
        asisUserCheckDto.setResultCode("1");
        asisUserCheckDto.setCustomerNumber("22432444");
        asisUserCheckDto.setResultMessage("0000000000,0000200000");

        given(mockPromotionCouponBiz.isPmCouponJoinDup(any())).willReturn(true);
        given(mockUserJoinService.asisUserCheck(any(), any())).willReturn(asisUserCheckDto);

        //when
        ApiResult<?> apiResult = mockUserJoinBizImpl.asisUserCheck(loginid, password, null, siteno);

        //then
        Assertions.assertEquals(apiResult.getMessageEnum(), ApiResult.success().getMessageEnum());
        Assertions.assertEquals(((HashMap<String, String>)apiResult.getData()).get("result_code"), "92");
        Assertions.assertEquals(((HashMap<String, String>)apiResult.getData()).get("result_siteno"), "");
    }
}
