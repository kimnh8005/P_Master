package kr.co.pulmuone.v1.user.join.service;

import kr.co.pulmuone.v1.comm.CommonServiceTestBaseForJunit5;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.base.vo.BuyerVo;
import kr.co.pulmuone.v1.comm.enums.CouponEnums;
import kr.co.pulmuone.v1.comm.enums.UserEnums;
import kr.co.pulmuone.v1.comm.mapper.user.join.UserJoinMapper;
import kr.co.pulmuone.v1.comm.util.SessionUtil;
import kr.co.pulmuone.v1.promotion.coupon.dto.vo.CouponInfoByUserJoinVo;
import kr.co.pulmuone.v1.promotion.coupon.service.PromotionCouponBiz;
import kr.co.pulmuone.v1.user.buyer.dto.AddBuyerRequestDto;
import kr.co.pulmuone.v1.user.buyer.service.UserBuyerBiz;
import kr.co.pulmuone.v1.user.certification.dto.GetClauseArrayRequestDto;
import kr.co.pulmuone.v1.user.certification.dto.GetSessionUserCertificationResponseDto;
import kr.co.pulmuone.v1.user.certification.service.UserCertificationBiz;
import kr.co.pulmuone.v1.user.join.dto.*;
import kr.co.pulmuone.v1.user.join.dto.vo.AccountVo;
import kr.co.pulmuone.v1.user.join.dto.vo.JoinResultVo;
import kr.co.pulmuone.v1.user.join.dto.vo.UserVo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

class UserJoinServiceTest extends CommonServiceTestBaseForJunit5 {

    @Autowired
    private UserJoinService userJoinService;

    @InjectMocks
    private UserJoinService mockUserJoinService;

    @Mock
    private UserJoinMapper mockUserJoinMapper;

    @Mock
    private UserCertificationBiz mockUserCertificationBiz;

    @Mock
    private UserBuyerBiz mockUserBuyerBiz;

    @Mock
    private PromotionCouponBiz mockPromotionCouponBiz;

    /*@BeforeEach
    void setUp() {
        mockUserJoinService = new UserJoinService(mockUserJoinMapper, mockUserCertificationBiz, mockUserBuyerBiz, mockPromotionCouponBiz);
    }

    @Test
    void 만14세미만_유효성검사_자리수_오류_false() throws Exception {

        assertFalse(userJoinService.isCheckUnderAge14("197712151"));
    }

    @Test
    void 만14세미만체크_true() throws Exception {

        DateFormat format = new SimpleDateFormat("yyyyMMdd");
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.YEAR, -14);
        cal.add(Calendar.DATE, 1);
        String str = format.format(cal.getTime());

        assertFalse(userJoinService.isCheckUnderAge14(str));
    }

    @Test
    void 만14세이상체크_true() throws Exception {

        DateFormat format = new SimpleDateFormat("yyyyMMdd");
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.YEAR, -14);
        String str = format.format(cal.getTime());

        assertTrue(userJoinService.isCheckUnderAge14(str));
    }

    @Test
    void 중복된_계정_false() throws Exception {

        String loginId = "sirilman23";

        GetIsCheckLoginIdRequestDto getIsCheckLoginIdRequestDto = new GetIsCheckLoginIdRequestDto();
        getIsCheckLoginIdRequestDto.setLoginId(loginId);

        Boolean isDup = userJoinService.getIsCheckLoginId(getIsCheckLoginIdRequestDto);

        assertEquals(true, isDup);
    }

    @Test
    void 중복이_되지않는_계정_true() throws Exception {

        String loginId = "sirilman23123123";

        GetIsCheckLoginIdRequestDto getIsCheckLoginIdRequestDto = new GetIsCheckLoginIdRequestDto();
        getIsCheckLoginIdRequestDto.setLoginId(loginId);

        Boolean isDup = userJoinService.getIsCheckLoginId(getIsCheckLoginIdRequestDto);

        assertEquals(false, isDup);
    }

    @Test
    void 이메일_중복이_되는경우_false() throws Exception {

        String mail = "sirilman23@naver.com";

        GetIsCheckMailRequestDto dto = new GetIsCheckMailRequestDto();
        dto.setMail(mail);

        assertTrue(userJoinService.getIsCheckMail(dto));
    }

    @Test
    void 이메일_중복이_되지않는경우_true() throws Exception {

        String mail = "sirilman23@naver.comxxxxx";

        GetIsCheckMailRequestDto dto = new GetIsCheckMailRequestDto();
        dto.setMail(mail);

        assertFalse(userJoinService.getIsCheckMail(dto));
    }

    @Test
    void 추천인_아이디_체크_true() throws Exception {

        GetIsCheckRecommendLoginIdRequestDto dto = new GetIsCheckRecommendLoginIdRequestDto();
        dto.setRecommendLoginId("sirilman23");

        assertEquals("1646773",
                Optional.ofNullable(userJoinService.getIsCheckRecommendLoginId(dto))
                        .map(c -> (GetIsCheckRecommendLoginIdResponseDto) c.getData())
                        .map(GetIsCheckRecommendLoginIdResponseDto::getRecommUserId)
                        .orElse("")
        );
    }

    @Test
    void 회원가입_CI_NULL_체크_true() throws Exception {

        BuyerVo buyerVo = new BuyerVo();
        buyerVo.setSnsAuthorizationState("");
        buyerVo.setUrUserId("1646773");
        buyerVo.setUrErpEmployeeCode("");
        buyerVo.setSnsProvider("");
        buyerVo.setSnsSocialId("");
        buyerVo.setSnsSocialName("최용호");
        buyerVo.setSnsSocialMail("sirilman23@naver.com");
        buyerVo.setSnsSocialProfileImage("");
        buyerVo.setPersonalCertificationUserName("테스터");
        buyerVo.setPersonalCertificationMobile("01072721234");
        buyerVo.setPersonalCertificationGender("M");
        buyerVo.setPersonalCertificationCiCd(null);
        buyerVo.setPersonalCertificationBirthday("19771215");
        SessionUtil.setUserVO(buyerVo);

        AddBuyerRequestDto dto = new AddBuyerRequestDto();

        assertEquals(UserEnums.Join.NO_FIND_INFO_USER.getCode(),
                Optional.ofNullable(userJoinService.addBuyer(dto))
                        .map(ApiResult::getCode)
                        .orElse("")
        );
    }

    @Test
    void 회원가입_CI_중복_체크_true() throws Exception {

        // 로그인정보 기본셋팅
        buyerLogin();

        AddBuyerRequestDto dto = new AddBuyerRequestDto();

        assertEquals(UserEnums.Join.NO_FIND_INFO_USER.getCode(),
                Optional.ofNullable(userJoinService.addBuyer(dto))
                        .map(ApiResult::getCode)
                        .orElse("")
        );
    }

    @Test
    void 회원가입_아이디_중복_체크_true() throws Exception {

        BuyerVo buyerVo = new BuyerVo();
        buyerVo.setSnsAuthorizationState("");
        buyerVo.setUrUserId("1646773");
        buyerVo.setUrErpEmployeeCode("");
        buyerVo.setSnsProvider("");
        buyerVo.setSnsSocialId("");
        buyerVo.setSnsSocialName("최용호");
        buyerVo.setSnsSocialMail("sirilman23@naver.com");
        buyerVo.setSnsSocialProfileImage("");
        buyerVo.setPersonalCertificationUserName("테스터");
        buyerVo.setPersonalCertificationMobile("01072721234");
        buyerVo.setPersonalCertificationGender("M");
        buyerVo.setPersonalCertificationCiCd("123qweasdxxx");
        buyerVo.setPersonalCertificationBirthday("19771215");
        SessionUtil.setUserVO(buyerVo);

        AddBuyerRequestDto dto = new AddBuyerRequestDto();
        dto.setLoginId("sirilman23");

        assertEquals(UserEnums.Join.ID_DUPLICATE.getCode(),
                Optional.ofNullable(userJoinService.addBuyer(dto))
                        .map(ApiResult::getCode)
                        .orElse("")
        );
    }

    @Test
    void 회원가입_이메일_중복_체크_true() throws Exception {

        BuyerVo buyerVo = new BuyerVo();
        buyerVo.setSnsAuthorizationState("");
        buyerVo.setUrUserId("1646773");
        buyerVo.setUrErpEmployeeCode("");
        buyerVo.setSnsProvider("");
        buyerVo.setSnsSocialId("");
        buyerVo.setSnsSocialName("최용호");
        buyerVo.setSnsSocialMail("sirilman23@naver.com");
        buyerVo.setSnsSocialProfileImage("");
        buyerVo.setPersonalCertificationUserName("테스터");
        buyerVo.setPersonalCertificationMobile("01072721234");
        buyerVo.setPersonalCertificationGender("M");
        buyerVo.setPersonalCertificationCiCd("123qweasdxxx");
        buyerVo.setPersonalCertificationBirthday("19771215");
        SessionUtil.setUserVO(buyerVo);

        AddBuyerRequestDto dto = new AddBuyerRequestDto();
        dto.setMail("sirilman23@naver.com");

        assertEquals(UserEnums.Join.EMAIL_DUPLICATE.getCode(),
                Optional.ofNullable(userJoinService.addBuyer(dto))
                        .map(ApiResult::getCode)
                        .orElse("")
        );
    }

    @Test
    void 회원가입_체크_true() throws Exception {

        BuyerVo buyerVo = new BuyerVo();
        buyerVo.setSnsAuthorizationState("");
        buyerVo.setUrUserId("1646773");
        buyerVo.setUrErpEmployeeCode("");
        buyerVo.setSnsProvider("");
        buyerVo.setSnsSocialId("");
        buyerVo.setSnsSocialName("최용호");
        buyerVo.setSnsSocialMail("sirilman23@naver.com");
        buyerVo.setSnsSocialProfileImage("");
        buyerVo.setPersonalCertificationUserName("테스터");
        buyerVo.setPersonalCertificationMobile("01072721234");
        buyerVo.setPersonalCertificationGender("M");
        buyerVo.setPersonalCertificationCiCd("123qweasdxxx");
        buyerVo.setPersonalCertificationBirthday("19771215");
        SessionUtil.setUserVO(buyerVo);

        GetClauseArrayRequestDto[] ary = new GetClauseArrayRequestDto[]{
                GetClauseArrayRequestDto.builder().psClauseGrpCd("123").executeDate("2020-09-21").build()
                , GetClauseArrayRequestDto.builder().psClauseGrpCd("345").executeDate("2020-09-21").build()
        };

        AddBuyerRequestDto dto = new AddBuyerRequestDto();
        dto.setClause(ary);
        dto.setPassword("123qweasdzxcase1234");
        dto.setSmsYn("Y");
        dto.setMailYn("Y");
        dto.setAddress1("asdfasdfasdf");
        dto.setAddress2("asdfasdfasdfasdf");
        dto.setRecommendLoginId("asdfasdf");
        dto.setZipCode("asdf");

        assertEquals(ApiResult.success().getCode(),
                Optional.ofNullable(userJoinService.addBuyer(dto))
                        .map(ApiResult::getCode)
                        .orElse("")
        );
    }

    @Test
    void 약관동의_로그_저장_true() throws Exception {

        SaveBuyerRequestDto dto = new SaveBuyerRequestDto();
        dto.setPsClauseGrpCd("test1");
        dto.setExecuteDate("2020-09-21");
        dto.setUrUserId("11111");

        assertTrue(userJoinService.addUrClauseAgreeLog(dto) > 0);
    }


    @Test
    void 사용자_정보_로그_등록_true() throws Exception {

        assertTrue(userJoinService.putUrAccount("1646773") > 0);
    }

    @Test
    void 회원기본정보_조회_성공() {
        UserVo userVo = userJoinService.getUserInfo("179527");

        assertEquals("179527", userVo.getLoginId());
    }

    @Test
    void 회원기본정보_조회_실패() {
        UserVo userVo = userJoinService.getUserInfo("");

        assertNull(userVo);
    }

    @Test
    void 회원기본정보_등록_성공() {
        int count = 0;

        UserVo userVo = new UserVo();
        userVo.setLoginId("0");
        userVo.setUserName("홍길동");
        userVo.setUserType(UserEnums.UserType.EMPLOYEE.getCode());
        userVo.setUserStatus(UserEnums.UserStatus.INACTIVITY.getCode());

        try {
            count = userJoinService.addUser(userVo);
        } catch (Exception e) {
        }

        assertEquals(1, count);
    }

    @Test
    void 회원기본정보_등록_실패() {

        UserVo userVo = new UserVo();
        userVo.setLoginId("179527");
        userVo.setUserName("홍길동");
        userVo.setUserType(UserEnums.UserType.EMPLOYEE.getCode());
        userVo.setUserStatus(UserEnums.UserStatus.INACTIVITY.getCode());

        assertThrows(Exception.class, () -> {
            userJoinService.addUser(userVo);
        });
    }

    @Test
    void 회원정보활동정보_등록_성공() {
        int count = 0;

        AccountVo accountVo = new AccountVo();
        accountVo.setUserId(419921L);

        try {
            count = userJoinService.addAccount(accountVo);
        } catch (Exception e) {

        }

        assertEquals(1, count);
    }

    @Test
    void 회원정보활동정보_등록_실패() {

        AccountVo accountVo = new AccountVo();
        accountVo.setUserId(1646799L);

        assertThrows(Exception.class, () -> {
            userJoinService.addAccount(accountVo);
        });
    }

    @Test
    void 회원기본정보_수정_성공() {
        int count = 0;

        UserVo userParamVo = new UserVo();
        userParamVo.setUserId(1646799L);
        userParamVo.setUserName("홍길동");

        try {
            count = userJoinService.putUser(userParamVo);
        } catch (Exception e) {

        }

        assertEquals(1, count);
    }

    @Test
    void 회원기본정보_수정_실패() throws Exception {
        int count = 0;

        UserVo userParamVo = new UserVo();
        userParamVo.setUserId(0L);
        userParamVo.setUserName("홍길동");

        count = userJoinService.putUser(userParamVo);

        assertNotEquals(1, count);
    }

    @Test
    void addBuyerAddPromotion_CI_인증오류() throws Exception {
        //given
        Long urUserId = 100L;
        GetSessionUserCertificationResponseDto getSessionUserCertificationResponseDto = new GetSessionUserCertificationResponseDto();
        getSessionUserCertificationResponseDto.setBeforeUserDropYn("Y");

        given(mockUserCertificationBiz.getSessionUserCertification()).willReturn(getSessionUserCertificationResponseDto);

        //when
        List<CouponInfoByUserJoinVo> resultVoList = mockUserJoinService.addBuyerAddPromotion(urUserId, urUserId);

        //then
        assertEquals(resultVoList.size(), 0);
    }

    @Test
    void addBuyerAddPromotion_탈퇴후_재가입() throws Exception {
        //given
        Long urUserId = 100L;
        GetSessionUserCertificationResponseDto getSessionUserCertificationResponseDto = new GetSessionUserCertificationResponseDto();
        getSessionUserCertificationResponseDto.setCi("test");
        getSessionUserCertificationResponseDto.setBeforeUserDropYn("Y");

        given(mockUserCertificationBiz.getSessionUserCertification()).willReturn(getSessionUserCertificationResponseDto);

        //when
        List<CouponInfoByUserJoinVo> resultVoList = mockUserJoinService.addBuyerAddPromotion(urUserId, urUserId);

        //then
        assertEquals(resultVoList.size(), 0);
    }

    @Test
    void addBuyerAddPromotion_회원가입쿠폰_오류() throws Exception {
        //given
        Long urUserId = 100L;

        GetSessionUserCertificationResponseDto getSessionUserCertificationResponseDto = new GetSessionUserCertificationResponseDto();
        getSessionUserCertificationResponseDto.setCi("test");
        getSessionUserCertificationResponseDto.setBeforeUserDropYn("N");

        List<CouponInfoByUserJoinVo> targetCouponList = new ArrayList<>();
        CouponInfoByUserJoinVo targetCouponVo = new CouponInfoByUserJoinVo();
        targetCouponVo.setPmCouponId(1L);
        targetCouponVo.setIssueDetailType(CouponEnums.IssueDetailType.USER_JOIN.getCode());
        targetCouponList.add(targetCouponVo);

        ApiResult apiResult = ApiResult.result(CouponEnums.AddCouponValidation.OVER_ISSUE_QTY);

        given(mockUserCertificationBiz.getSessionUserCertification()).willReturn(getSessionUserCertificationResponseDto);
        given(mockPromotionCouponBiz.getCouponInfoByUserJoin()).willReturn(targetCouponList);
        given(mockPromotionCouponBiz.addCouponByList(any(), any())).willReturn(apiResult);

        //when
        List<CouponInfoByUserJoinVo> resultVoList = mockUserJoinService.addBuyerAddPromotion(urUserId, urUserId);

        //then
        assertEquals(resultVoList.size(), 0);
    }


    @Test
    void addBuyerAddPromotion_회원가입쿠폰_정상() throws Exception {
        //given
        Long urUserId = 100L;

        GetSessionUserCertificationResponseDto getSessionUserCertificationResponseDto = new GetSessionUserCertificationResponseDto();
        getSessionUserCertificationResponseDto.setCi("test");
        getSessionUserCertificationResponseDto.setBeforeUserDropYn("N");

        List<CouponInfoByUserJoinVo> targetCouponList = new ArrayList<>();
        CouponInfoByUserJoinVo targetCouponVo = new CouponInfoByUserJoinVo();
        targetCouponVo.setPmCouponId(1L);
        targetCouponVo.setDisplayCouponName("couponName");
        targetCouponVo.setIssueDetailType(CouponEnums.IssueDetailType.USER_JOIN.getCode());
        targetCouponList.add(targetCouponVo);

        ApiResult apiResult = ApiResult.success();

        given(mockUserCertificationBiz.getSessionUserCertification()).willReturn(getSessionUserCertificationResponseDto);
        given(mockPromotionCouponBiz.getCouponInfoByUserJoin()).willReturn(targetCouponList);
        given(mockPromotionCouponBiz.addCouponByList(any(), any())).willReturn(apiResult);

        //when
        List<CouponInfoByUserJoinVo> resultVoList = mockUserJoinService.addBuyerAddPromotion(urUserId, urUserId);

        //then
        assertEquals(resultVoList.get(0).getDisplayCouponName(), "couponName");
    }

    @Test
    void addBuyerAddPromotion_추천인쿠폰_오류() throws Exception {
        //given
        Long urUserId = 100L;

        GetSessionUserCertificationResponseDto getSessionUserCertificationResponseDto = new GetSessionUserCertificationResponseDto();
        getSessionUserCertificationResponseDto.setCi("test");
        getSessionUserCertificationResponseDto.setBeforeUserDropYn("N");

        List<CouponInfoByUserJoinVo> targetCouponList = new ArrayList<>();
        CouponInfoByUserJoinVo targetCouponVo = new CouponInfoByUserJoinVo();
        targetCouponVo.setPmCouponId(1L);
        targetCouponVo.setIssueDetailType(CouponEnums.IssueDetailType.RECOMMENDER.getCode());
        targetCouponList.add(targetCouponVo);

        ApiResult apiResult = ApiResult.result(CouponEnums.AddCouponValidation.OVER_ISSUE_QTY);

        given(mockUserCertificationBiz.getSessionUserCertification()).willReturn(getSessionUserCertificationResponseDto);
        given(mockPromotionCouponBiz.getCouponInfoByUserJoin()).willReturn(targetCouponList);
        given(mockPromotionCouponBiz.addCouponByList(any(), any())).willReturn(apiResult);

        //when
        List<CouponInfoByUserJoinVo> resultVoList = mockUserJoinService.addBuyerAddPromotion(urUserId, null);

        //then
        assertEquals(resultVoList.size(), 0);
    }


    @Test
    void addBuyerAddPromotion_추천인쿠폰_정상() throws Exception {
        //given
        Long urUserId = 100L;

        GetSessionUserCertificationResponseDto getSessionUserCertificationResponseDto = new GetSessionUserCertificationResponseDto();
        getSessionUserCertificationResponseDto.setCi("test");
        getSessionUserCertificationResponseDto.setBeforeUserDropYn("N");

        List<CouponInfoByUserJoinVo> targetCouponList = new ArrayList<>();
        CouponInfoByUserJoinVo targetCouponVo = new CouponInfoByUserJoinVo();
        targetCouponVo.setPmCouponId(1L);
        targetCouponVo.setDisplayCouponName("couponName");
        targetCouponVo.setIssueDetailType(CouponEnums.IssueDetailType.RECOMMENDER.getCode());
        targetCouponList.add(targetCouponVo);

        ApiResult apiResult = ApiResult.success();

        given(mockUserCertificationBiz.getSessionUserCertification()).willReturn(getSessionUserCertificationResponseDto);
        given(mockPromotionCouponBiz.getCouponInfoByUserJoin()).willReturn(targetCouponList);
        given(mockPromotionCouponBiz.addCouponByList(any(), any())).willReturn(apiResult);

        //when
        List<CouponInfoByUserJoinVo> resultVoList = mockUserJoinService.addBuyerAddPromotion(urUserId, urUserId);

        //then
        assertEquals(resultVoList.get(0).getDisplayCouponName(), "couponName");
    }*/

    @Test
    void 회원가입_완료_시_전송할_정보_조회() {
    	//given
    	String urUserId = "1647263";

    	//when
    	JoinResultVo joinResultVo = userJoinService.getJoinCompletedInfo(urUserId);

    	//then
    	assertEquals("zxcasdasd2", joinResultVo.getLoginId());
    }
}