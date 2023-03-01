package kr.co.pulmuone.v1.user.certification.service;

import kr.co.pulmuone.v1.comm.CommonServiceTestBaseForJunit5;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.base.vo.BuyerVo;
import kr.co.pulmuone.v1.comm.enums.BaseEnums;
import kr.co.pulmuone.v1.comm.enums.UserEnums;
import kr.co.pulmuone.v1.comm.mapper.shopping.cart.ShoppingCartMapper;
import kr.co.pulmuone.v1.comm.mapper.user.certification.UserCertificationMapper;
import kr.co.pulmuone.v1.comm.util.SHA256Util;
import kr.co.pulmuone.v1.comm.util.SessionUtil;
import kr.co.pulmuone.v1.comm.util.google.Recaptcha;
import kr.co.pulmuone.v1.shopping.cart.service.ShoppingCartBiz;
import kr.co.pulmuone.v1.user.buyer.dto.DoLoginRequestDto;
import kr.co.pulmuone.v1.user.buyer.dto.vo.GetChangeClauseNoAgreeListResultVo;
import kr.co.pulmuone.v1.user.buyer.dto.vo.GetShippingAddressListResultVo;
import kr.co.pulmuone.v1.user.buyer.service.UserBuyerBiz;
import kr.co.pulmuone.v1.user.certification.dto.AddSessionShippingRequestDto;
import kr.co.pulmuone.v1.user.certification.dto.GetLoginRequestDto;
import kr.co.pulmuone.v1.user.certification.dto.vo.CertificationVo;
import kr.co.pulmuone.v1.user.certification.dto.vo.EmployeeCertificationResultVo;
import kr.co.pulmuone.v1.user.certification.dto.vo.GetBuyerSessionDataResultVo;
import kr.co.pulmuone.v1.user.certification.dto.vo.GetLoginDataResultVo;
import kr.co.pulmuone.v1.user.device.service.UserDeviceBiz;
import kr.co.pulmuone.v1.user.dormancy.dto.vo.GetIsCheckUserMoveResultVo;
import kr.co.pulmuone.v1.user.dormancy.service.UserDormancyBiz;
import kr.co.pulmuone.v1.user.join.service.UserJoinBiz;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletWebRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

class UserCertificationServiceTest extends CommonServiceTestBaseForJunit5 {

    @Autowired
    UserCertificationService userCertificationService;

    @InjectMocks
    private UserCertificationService mockUserCertificationService;

    @Mock
    UserCertificationMapper mockUserCertificationMapper;

    @Mock
    Recaptcha mockRecaptcha;

    @Mock
    UserDormancyBiz mockUserDormancyBiz;

    @Mock
    UserBuyerBiz mockUserBuyerBiz;

    @Mock
    UserJoinBiz mockUserJoinBiz;

    @Mock
    UserDeviceBiz mockUserDeviceBiz;

    @Mock
    ShoppingCartMapper mockShoppingCartMapper;

    @Test
    void 배송정보_세션_저장_true() {
        AddSessionShippingRequestDto addSessionShippingRequestDto = new AddSessionShippingRequestDto();
        addSessionShippingRequestDto.setReceiverName("홍길동");
        addSessionShippingRequestDto.setReceiverZipCode("123-123");
        addSessionShippingRequestDto.setReceiverAddress1("지구");
        addSessionShippingRequestDto.setReceiverAddress2("한국");
        addSessionShippingRequestDto.setBuildingCode("12");
        addSessionShippingRequestDto.setReceiverMobile("123-0123-1233");
        addSessionShippingRequestDto.setAccessInformationType("1");
        addSessionShippingRequestDto.setAccessInformationPassword("1");
        addSessionShippingRequestDto.setShippingComment("11111111111111");

        BuyerVo buyerVo = new BuyerVo();
        buyerVo.setSnsAuthorizationState("");
        buyerVo.setUrUserId("1646773");
        buyerVo.setUrErpEmployeeCode("");
        buyerVo.setSnsProvider("");
        buyerVo.setSnsSocialId("");
        buyerVo.setPersonalCertificationUserName("테스터");
        buyerVo.setPersonalCertificationMobile("01072721234");
        buyerVo.setPersonalCertificationGender("M");
        buyerVo.setPersonalCertificationCiCd("123qweasd");
        buyerVo.setPersonalCertificationBirthday("19771215");
        SessionUtil.setUserVO(buyerVo);

        userCertificationService.addSessionShipping(addSessionShippingRequestDto);

        SessionUtil.setUserVO(null);
    }

    @Test
    void 로그인성공_오류_통합몰안내가이드_ture() throws Exception {

        ServletWebRequest servletContainer = (ServletWebRequest) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = servletContainer.getRequest();
        HttpServletResponse response = servletContainer.getResponse();

        DoLoginRequestDto doLoginRequestDto = new DoLoginRequestDto();
        boolean useCaptcha = true;

        // 통합몰 안내 가이드
        doLoginRequestDto.setPassword("123qweasd!");
        doLoginRequestDto.setLoginId("sirilman24");
        ApiResult<?> result = userCertificationService.doLogin(doLoginRequestDto, useCaptcha, request, response);
        assertEquals(UserEnums.Join.LOGIN_FAIL, result.getMessageEnum());
    }

    @Test
    void 로그인_실패_케이스1() throws Exception {

        DoLoginRequestDto doLoginRequestDto = new DoLoginRequestDto();
        doLoginRequestDto.setLoginId("testId");
        doLoginRequestDto.setPassword("testPw");
        boolean useCaptcha = true;

        ServletWebRequest servletContainer = (ServletWebRequest) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = servletContainer.getRequest();
        HttpServletResponse response = servletContainer.getResponse();

        // 오류 실패 케이스 1
        int cnt = 0;
        //given(mockUserCertificationMapper.getisCheckLogin(any())).willReturn(cnt);
        ApiResult<?> result = userCertificationService.doLogin(doLoginRequestDto, useCaptcha, request, response);
        assertEquals(UserEnums.Join.LOGIN_FAIL, result.getMessageEnum());
    }

    @Test
    void 로그인_실패_케이스2_캡차가있을경우_캡차실패() throws Exception {

        ServletWebRequest servletContainer = (ServletWebRequest) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = servletContainer.getRequest();
        HttpServletResponse response = servletContainer.getResponse();

        GetLoginDataResultVo getLoginDataResultVo = new GetLoginDataResultVo();
        getLoginDataResultVo.setUrUserId("1");
        getLoginDataResultVo.setFailCnt(3);
        getLoginDataResultVo.setUrLoginFailCount(1);
        given(mockUserCertificationMapper.getLoginData(any())).willReturn(getLoginDataResultVo);
        given(mockUserCertificationMapper.getisCheckLogin(any())).willReturn(0);
        given(mockRecaptcha.siteVerify(any())).willReturn(false);

        DoLoginRequestDto doLoginRequestDto = new DoLoginRequestDto();
        doLoginRequestDto.setLoginId("sirilman23");
        doLoginRequestDto.setEncryptPassword("1");
        doLoginRequestDto.setCaptcha("captcha");

        boolean useCaptcha = true;

        ApiResult<?> result = mockUserCertificationService.doLogin(doLoginRequestDto, useCaptcha, request, response);
        assertEquals(UserEnums.Join.RECAPTCHA_FAIL, result.getMessageEnum());
    }

    @Test
    void 로그인_실패_케이스3_캡차가있을경우_캡차성공_5회실패() throws Exception {

        ServletWebRequest servletContainer = (ServletWebRequest) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = servletContainer.getRequest();
        HttpServletResponse response = servletContainer.getResponse();

        GetLoginDataResultVo getLoginDataResultVo = new GetLoginDataResultVo();
        getLoginDataResultVo.setUrUserId("1");
        getLoginDataResultVo.setFailCnt(5);
        getLoginDataResultVo.setUrLoginFailCount(5);
        given(mockUserCertificationMapper.getLoginData(any())).willReturn(getLoginDataResultVo);
        given(mockUserCertificationMapper.getisCheckLogin(any())).willReturn(0);
        given(mockRecaptcha.siteVerify(any())).willReturn(true);

        DoLoginRequestDto doLoginRequestDto = new DoLoginRequestDto();
        doLoginRequestDto.setLoginId("sirilman23");
        doLoginRequestDto.setEncryptPassword("1");
        doLoginRequestDto.setCaptcha("captcha");

        boolean useCaptcha = true;

        ApiResult<?> result = mockUserCertificationService.doLogin(doLoginRequestDto, useCaptcha, request, response);
        assertEquals(UserEnums.Join.OVER5_FAIL_PASSWORD, result.getMessageEnum());
    }

    @Test
    void 로그인_실패_케이스4_캡차가있을경우_캡차성공_3회실패() throws Exception {

        ServletWebRequest servletContainer = (ServletWebRequest) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = servletContainer.getRequest();
        HttpServletResponse response = servletContainer.getResponse();

        GetLoginDataResultVo getLoginDataResultVo = new GetLoginDataResultVo();
        getLoginDataResultVo.setUrUserId("1");
        getLoginDataResultVo.setFailCnt(3);
        getLoginDataResultVo.setUrLoginFailCount(5);
        given(mockUserCertificationMapper.getLoginData(any())).willReturn(getLoginDataResultVo);
        given(mockUserCertificationMapper.getisCheckLogin(any())).willReturn(0);
        given(mockRecaptcha.siteVerify(any())).willReturn(true);

        DoLoginRequestDto doLoginRequestDto = new DoLoginRequestDto();
        doLoginRequestDto.setLoginId("sirilman23");
        doLoginRequestDto.setEncryptPassword("1");
        doLoginRequestDto.setCaptcha("captcha");

        boolean useCaptcha = true;

        ApiResult<?> result = mockUserCertificationService.doLogin(doLoginRequestDto, useCaptcha, request, response);
        assertEquals(UserEnums.Join.LOGIN_FAIL, result.getMessageEnum());
    }

    @Test
    void 로그인_실패_케이스5_비번은맞으나_캡차실패1() throws Exception {

        ServletWebRequest servletContainer = (ServletWebRequest) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = servletContainer.getRequest();
        HttpServletResponse response = servletContainer.getResponse();

        GetLoginDataResultVo getLoginDataResultVo = new GetLoginDataResultVo();
        getLoginDataResultVo.setUrUserId("1");
        getLoginDataResultVo.setFailCnt(3);
        getLoginDataResultVo.setUrLoginFailCount(5);
        given(mockUserCertificationMapper.getLoginData(any())).willReturn(getLoginDataResultVo);
        given(mockUserCertificationMapper.getisCheckLogin(any())).willReturn(1);
        given(mockRecaptcha.siteVerify(any())).willReturn(false);

        DoLoginRequestDto doLoginRequestDto = new DoLoginRequestDto();
        doLoginRequestDto.setLoginId("sirilman23");
        doLoginRequestDto.setEncryptPassword("1");
        doLoginRequestDto.setCaptcha("captcha");

        boolean useCaptcha = true;

        ApiResult<?> result = mockUserCertificationService.doLogin(doLoginRequestDto, useCaptcha, request, response);
        assertEquals(UserEnums.Join.RECAPTCHA_FAIL, result.getMessageEnum());
    }

    @Test
    void 로그인_실패_케이스6_휴먼회원() throws Exception {

        ServletWebRequest servletContainer = (ServletWebRequest) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = servletContainer.getRequest();
        HttpServletResponse response = servletContainer.getResponse();

        GetLoginDataResultVo getLoginDataResultVo = new GetLoginDataResultVo();
        getLoginDataResultVo.setUrUserId("1");
        given(mockUserCertificationMapper.getLoginData(any())).willReturn(getLoginDataResultVo);
        given(mockUserCertificationMapper.getisCheckLogin(any())).willReturn(1);
        given(mockRecaptcha.siteVerify(any())).willReturn(true);

        GetIsCheckUserMoveResultVo getIsCheckUserMoveResultVo = new GetIsCheckUserMoveResultVo();
        given(mockUserDormancyBiz.isCheckUserMove(any())).willReturn(getIsCheckUserMoveResultVo);

        DoLoginRequestDto doLoginRequestDto = new DoLoginRequestDto();
        doLoginRequestDto.setLoginId("sirilman23");
        doLoginRequestDto.setEncryptPassword("1");
        doLoginRequestDto.setCaptcha("captcha");

        boolean useCaptcha = true;

        ApiResult<?> result = mockUserCertificationService.doLogin(doLoginRequestDto, useCaptcha, request, response);
        assertEquals(UserEnums.Join.SLEEP_MEMEBER, result.getMessageEnum());
    }

    @Test
    void 로그인_실패_케이스7_정지회원() throws Exception {

        ServletWebRequest servletContainer = (ServletWebRequest) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = servletContainer.getRequest();
        HttpServletResponse response = servletContainer.getResponse();

        GetLoginDataResultVo getLoginDataResultVo = new GetLoginDataResultVo();
        getLoginDataResultVo.setUrUserId("1");
        getLoginDataResultVo.setStatus("BUYER_STATUS.STOP");
        given(mockUserCertificationMapper.getLoginData(any())).willReturn(getLoginDataResultVo);
        given(mockUserCertificationMapper.getisCheckLogin(any())).willReturn(1);
        given(mockRecaptcha.siteVerify(any())).willReturn(true);
        given(mockUserDormancyBiz.isCheckUserMove(any())).willReturn(null);

        DoLoginRequestDto doLoginRequestDto = new DoLoginRequestDto();
        doLoginRequestDto.setLoginId("sirilman23");
        doLoginRequestDto.setEncryptPassword("1");
        doLoginRequestDto.setCaptcha("captcha");

        boolean useCaptcha = true;

        ApiResult<?> result = mockUserCertificationService.doLogin(doLoginRequestDto, useCaptcha, request, response);
        assertEquals(UserEnums.Join.STOP_MEMEBER, result.getMessageEnum());
    }

    @Test
    void 로그인_실패_케이스8_임시비밀번호체크() throws Exception {

        ServletWebRequest servletContainer = (ServletWebRequest) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = servletContainer.getRequest();
        HttpServletResponse response = servletContainer.getResponse();

        GetLoginDataResultVo getLoginDataResultVo = new GetLoginDataResultVo();
        getLoginDataResultVo.setUrUserId("20");
        getLoginDataResultVo.setTmprrYn("Y");
        given(mockUserCertificationMapper.getLoginData(any())).willReturn(getLoginDataResultVo);
        given(mockUserCertificationMapper.getisCheckLogin(any())).willReturn(1);
        given(mockRecaptcha.siteVerify(any())).willReturn(true);
        given(mockUserDormancyBiz.isCheckUserMove(any())).willReturn(null);

        DoLoginRequestDto doLoginRequestDto = new DoLoginRequestDto();
        doLoginRequestDto.setLoginId("admin");
        doLoginRequestDto.setEncryptPassword("MoBwnx+H4XG6OMoIWiw+R052yfAsHIHG75RD4T8+7Fg=");
        doLoginRequestDto.setCaptcha("captcha");

        boolean useCaptcha = true;

        ApiResult<?> result = userCertificationService.doLogin(doLoginRequestDto, false, request, response);
        assertEquals(UserEnums.Join.TEMP_PASSWORD, result.getMessageEnum());
    }

    @Test
    void 로그인_실패_케이스9_약관필수체크() throws Exception {

        ServletWebRequest servletContainer = (ServletWebRequest) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = servletContainer.getRequest();
        HttpServletResponse response = servletContainer.getResponse();

        GetLoginDataResultVo getLoginDataResultVo = new GetLoginDataResultVo();
        getLoginDataResultVo.setUrUserId("1");
        getLoginDataResultVo.setTmprrYn("N");
        given(mockUserCertificationMapper.getLoginData(any())).willReturn(getLoginDataResultVo);
        given(mockUserCertificationMapper.getisCheckLogin(any())).willReturn(1);
        given(mockRecaptcha.siteVerify(any())).willReturn(true);
        given(mockUserDormancyBiz.isCheckUserMove(any())).willReturn(null);

        List<GetChangeClauseNoAgreeListResultVo> getChangeClauseNoAgreeListResultVo = new ArrayList<>();
        getChangeClauseNoAgreeListResultVo.add(new GetChangeClauseNoAgreeListResultVo());
        given(mockUserBuyerBiz.getChangeClauseNoAgreeList(any())).willReturn(getChangeClauseNoAgreeListResultVo);

        DoLoginRequestDto doLoginRequestDto = new DoLoginRequestDto();
        doLoginRequestDto.setLoginId("sirilman23");
        doLoginRequestDto.setEncryptPassword("1");
        doLoginRequestDto.setCaptcha("captcha");

        boolean useCaptcha = true;

        ApiResult<?> result = mockUserCertificationService.doLogin(doLoginRequestDto, useCaptcha, request, response);
        assertEquals(UserEnums.Join.CHECK_VERSION_UP_CLAUSE, result.getMessageEnum());
    }

    @Test
    void 로그인_성공1_아이디저장_자동로그인저장() throws Exception {

        BuyerVo buyerVo = new BuyerVo();
        SessionUtil.setUserVO(buyerVo);

        ServletWebRequest servletContainer = (ServletWebRequest) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = servletContainer.getRequest();
        HttpServletResponse response = servletContainer.getResponse();

        GetLoginDataResultVo getLoginDataResultVo = new GetLoginDataResultVo();
        getLoginDataResultVo.setUrUserId("1");
        getLoginDataResultVo.setTmprrYn("N");
        getLoginDataResultVo.setPasswordChangeThreeMonOverYn("Y");
        getLoginDataResultVo.setSmsYn("Y");
        getLoginDataResultVo.setMailYn("Y");
        getLoginDataResultVo.setMailYnDateOneYearOverYn("Y");
        getLoginDataResultVo.setSmsYnDateOneYearOverYn("Y");
        given(mockUserCertificationMapper.getLoginData(any())).willReturn(getLoginDataResultVo);
        given(mockUserCertificationMapper.getisCheckLogin(any())).willReturn(1);
        given(mockRecaptcha.siteVerify(any())).willReturn(true);
        given(mockUserDormancyBiz.isCheckUserMove(any())).willReturn(null);
        given(mockUserBuyerBiz.getChangeClauseNoAgreeList(any())).willReturn(null);
        given(mockUserJoinBiz.putUrAccount(any())).willReturn(1);
        given(mockUserDeviceBiz.putMemberMapping(any(), any())).willReturn(1);

        GetShippingAddressListResultVo getShippingAddressListResultVo = new GetShippingAddressListResultVo();
        getShippingAddressListResultVo.setReceiverName("asdfasdf");
        getShippingAddressListResultVo.setReceiverZipCode("123123");
        getShippingAddressListResultVo.setReceiverAddress1("서울특별시 광진구 구의2동");
        getShippingAddressListResultVo.setReceiverAddress2("558-0호");
        getShippingAddressListResultVo.setBuildingCode("2");
        getShippingAddressListResultVo.setReceiverMobile("010-1233-1233");
        getShippingAddressListResultVo.setAccessInformationType("1");
        getShippingAddressListResultVo.setAccessInformationPassword("1111");
        getShippingAddressListResultVo.setShippingComment("테스트");
        given(mockUserBuyerBiz.getBasicShippingAddress(any(), any())).willReturn(getShippingAddressListResultVo);

        GetBuyerSessionDataResultVo getBuyerSessionDataResultVo = new GetBuyerSessionDataResultVo();
        getBuyerSessionDataResultVo.setUrUserId("1");
        getBuyerSessionDataResultVo.setUserName("최용호");
        getBuyerSessionDataResultVo.setLoginId("sirilman23");
        getBuyerSessionDataResultVo.setUrErpEmployeeCode("1");
        given(mockUserCertificationMapper.getBuyerSessionData(any(), any())).willReturn(getBuyerSessionDataResultVo);

        DoLoginRequestDto doLoginRequestDto = new DoLoginRequestDto();
        doLoginRequestDto.setLoginId("xptmxm1111");
        doLoginRequestDto.setEncryptPassword("MoBwnx+H4XG6OMoIWiw+R052yfAsHIHG75RD4T8+7Fg=");
        doLoginRequestDto.setCaptcha("captcha");
        doLoginRequestDto.setSaveLoginId(true);
        doLoginRequestDto.setAutoLogin(true);

        boolean useCaptcha = true;

        ApiResult<?> result = userCertificationService.doLogin(doLoginRequestDto, false, request, response);
        assertEquals(ApiResult.success().getMessageEnum(), result.getMessageEnum());
    }

    @Test
    void 로그인_성공2_login() throws Exception {

        BuyerVo buyerVo = new BuyerVo();
        SessionUtil.setUserVO(buyerVo);

        ServletWebRequest servletContainer = (ServletWebRequest) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = servletContainer.getRequest();
        HttpServletResponse response = servletContainer.getResponse();

        GetLoginDataResultVo getLoginDataResultVo = new GetLoginDataResultVo();
        getLoginDataResultVo.setUrUserId("1");
        getLoginDataResultVo.setTmprrYn("N");
        getLoginDataResultVo.setPasswordChangeThreeMonOverYn("Y");
        getLoginDataResultVo.setSmsYn("Y");
        getLoginDataResultVo.setMailYn("Y");
        getLoginDataResultVo.setMailYnDateOneYearOverYn("Y");
        getLoginDataResultVo.setSmsYnDateOneYearOverYn("Y");
        given(mockUserCertificationMapper.getLoginData(any())).willReturn(getLoginDataResultVo);
        given(mockUserCertificationMapper.getisCheckLogin(any())).willReturn(1);
        given(mockRecaptcha.siteVerify(any())).willReturn(true);
        given(mockUserDormancyBiz.isCheckUserMove(any())).willReturn(null);
        given(mockUserBuyerBiz.getChangeClauseNoAgreeList(any())).willReturn(null);
        given(mockUserJoinBiz.putUrAccount(any())).willReturn(1);
        given(mockUserDeviceBiz.putMemberMapping(any(), any())).willReturn(1);

        GetShippingAddressListResultVo getShippingAddressListResultVo = new GetShippingAddressListResultVo();
        getShippingAddressListResultVo.setReceiverName("asdfasdf");
        getShippingAddressListResultVo.setReceiverZipCode("123123");
        getShippingAddressListResultVo.setReceiverAddress1("서울특별시 광진구 구의2동");
        getShippingAddressListResultVo.setReceiverAddress2("558-0호");
        getShippingAddressListResultVo.setBuildingCode("2");
        getShippingAddressListResultVo.setReceiverMobile("010-1233-1233");
        getShippingAddressListResultVo.setAccessInformationType("1");
        getShippingAddressListResultVo.setAccessInformationPassword("1111");
        getShippingAddressListResultVo.setShippingComment("테스트");
        given(mockUserBuyerBiz.getBasicShippingAddress(any(), any())).willReturn(getShippingAddressListResultVo);

        GetBuyerSessionDataResultVo getBuyerSessionDataResultVo = new GetBuyerSessionDataResultVo();
        getBuyerSessionDataResultVo.setUrUserId("1");
        getBuyerSessionDataResultVo.setUserName("최용호");
        getBuyerSessionDataResultVo.setLoginId("sirilman23");
        getBuyerSessionDataResultVo.setUrErpEmployeeCode("1");
        given(mockUserCertificationMapper.getBuyerSessionData(any(), any())).willReturn(getBuyerSessionDataResultVo);

        DoLoginRequestDto doLoginRequestDto = new DoLoginRequestDto();
        doLoginRequestDto.setLoginId("sirilman23");
        doLoginRequestDto.setEncryptPassword("1");
        doLoginRequestDto.setCaptcha("captcha");
        doLoginRequestDto.setSaveLoginId(true);
        doLoginRequestDto.setAutoLogin(true);

        GetLoginRequestDto getLoginRequestDto = new GetLoginRequestDto();
        getLoginRequestDto.setLoginId("test2587");
        getLoginRequestDto.setPassword("q1w2e3r4!");
        getLoginRequestDto.setAutoLogin("Y");
        getLoginRequestDto.setSaveLoginId("Y");
        //getLoginRequestDto.setCaptcha("test");

        ApiResult<?> result = userCertificationService.login(getLoginRequestDto, request, response);
        assertEquals(ApiResult.success().getMessageEnum(), result.getMessageEnum());
    }

    @Test
    void 로그인_인증정보_등록_성공() {
        int count = 0;

        CertificationVo certificationVo = new CertificationVo();
        certificationVo.setUserId(0L);
        certificationVo.setLoginId("179527");
        certificationVo.setPassword(SHA256Util.getUserPassword("11111"));
        certificationVo.setTempPasswordYn(BaseEnums.UseYn.Y.name());

        try {
            count = userCertificationService.addCertification(certificationVo);
        } catch (Exception e) {

        }

        assertEquals(1, count);
    }

    @Test
    void 로그인_인증정보_등록_실패() {
        int count = 0;

        CertificationVo certificationVo = new CertificationVo();
        certificationVo.setPassword(SHA256Util.getUserPassword("11111"));
        certificationVo.setTempPasswordYn(BaseEnums.UseYn.Y.name());

        try {
            count = userCertificationService.addCertification(certificationVo);
        } catch (Exception e) {

        }

        assertNotEquals(1, count);
    }

    @Test
    void 임직원회원_인증_후_정보조회() {
    	//given
    	String urErpEmployeeCd = "forbiz01";

    	//when
    	EmployeeCertificationResultVo employeeCertificationResultVo = userCertificationService.getEmployeeCertificationInfo(urErpEmployeeCd);

    	//then
    	assertEquals("wss@forbiz.co.kr", employeeCertificationResultVo.getMail());
    }

    @Test
    void 본인인증_정보_조회_14세아하인경우_실패() {

    }

    @Test
    void 정상회원_CI중복으로_본인인증_실패(){

    }

    @Test
    void 정지회원_CI중복으로_본인인증_실패(){

    }

    @Test
    void 탈퇴회원_CI중복으로_본인인증_실패(){

    }

    @Test
    void 마스킹처리된_어아다찾기_실패(){

    }

    @Test
    void 마스킹처리된_아이디찾기_성공(){

    }

    @Test
    void 어아다찾기_실패(){

    }

    @Test
    void 아이디찾기_성공(){

    }

    @Test
    void 비밀번호찾기_실패(){

    }

    @Test
    void 비밀번호찾기_사용자정보없음_실패(){

    }

    @Test
    void 일반회원로그인_성공(){

    }

    @Test
    void 일반회원로그인_실패(){

    }

    @Test
    void SNS_로그인아이디_조회_성공(){

    }

    @Test
    void SNS_사용자정보_추가_성공() {

    }

    @Test
    void 로그아웃_성공(){

    }

    @Test
    void 비밀번호변경_실패(){

    }

    @Test
    void 비밀번호변경_성공(){

    }

    @Test
    void 비밀번호_다음에_변경_성공(){

    }

    @Test
    void 비회원로그인_실패(){

    }

    @Test
    void 비회원로그인_성공(){

    }

    @Test
    void 비회원_주문조회_데이터없음(){

    }

    @Test
    void 휴면회원_CI조회_데이터있음(){

    }

    @Test
    void 회원_SNS계정_전체_삭제_성공(){
    	try {
    		userCertificationService.unlinkAllAccount(0L);
    		assertTrue(true);
        } catch (Exception e) {
        	assertTrue(false);
        }
    }
}
