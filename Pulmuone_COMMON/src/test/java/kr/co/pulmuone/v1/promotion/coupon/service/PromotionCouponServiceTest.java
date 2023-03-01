package kr.co.pulmuone.v1.promotion.coupon.service;

import com.github.pagehelper.Page;
import kr.co.pulmuone.v1.comm.CommonServiceTestBaseForJunit5;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.base.vo.UserVo;
import kr.co.pulmuone.v1.comm.enums.BaseEnums;
import kr.co.pulmuone.v1.comm.enums.CouponEnums;
import kr.co.pulmuone.v1.comm.enums.GoodsEnums;
import kr.co.pulmuone.v1.comm.mapper.promotion.coupon.PromotionCouponMapper;
import kr.co.pulmuone.v1.comm.util.SessionUtil;
import kr.co.pulmuone.v1.goods.goods.dto.DiscountCalculationResultDto;
import kr.co.pulmuone.v1.goods.goods.dto.GoodsApplyCouponDto;
import kr.co.pulmuone.v1.promotion.coupon.dto.*;
import kr.co.pulmuone.v1.promotion.coupon.dto.vo.*;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@Slf4j
class PromotionCouponServiceTest extends CommonServiceTestBaseForJunit5 {
    @Autowired
    PromotionCouponService promotionCouponService;

    @InjectMocks
    private PromotionCouponService mockPromotionCouponService;

    @Mock
    PromotionCouponMapper mockPromotionCouponMapper;

    @BeforeEach
    void setUp() {
        mockPromotionCouponService = new PromotionCouponService(mockPromotionCouponMapper);
    }

    @Test
    void getCpnMgm_정상() throws Exception {

        CouponRequestDto dto = new CouponRequestDto();

        Page<CouponListResultVo> voList = promotionCouponService.getCpnMgm(dto);

        assertTrue(voList.getResult().size() > 0);

    }


    @Test
    void getCpnMgm_조회내역없음() throws Exception {

        CouponRequestDto dto = new CouponRequestDto();
        dto.setSearchSelect("SEARCH_SELECT.DISPLAY");
        dto.setFindKeyword("1999-01-01");

        Page<CouponListResultVo> voList = promotionCouponService.getCpnMgm(dto);

        assertEquals(0, voList.getResult().size());

    }


    @Test
    void getCoupon_정상() throws Exception {

        CouponRequestDto dto = new CouponRequestDto();
        dto.setPmCouponId("-1");

        CouponDetailVo vo = promotionCouponService.getCoupon(dto);

        Assertions.assertNull(vo);
    }


    @Test
    void getOrganizationPopupList_정상() throws Exception {

        OrganizationPopupListRequestDto dto = new OrganizationPopupListRequestDto();

        Page<OrganizationPopupListResultVo> voList = promotionCouponService.getOrganizationPopupList(dto);

        assertTrue(voList.getResult().size() > 0);

    }

    @Test
    void getOrganizationPopupList_조회내역없음() throws Exception {

        OrganizationPopupListRequestDto dto = new OrganizationPopupListRequestDto();
        dto.setSearchNameValue("1999-01-01");
        dto.setNameType("NAME_TYPE");

        Page<OrganizationPopupListResultVo> voList = promotionCouponService.getOrganizationPopupList(dto);

        assertNotEquals(voList.getResult().size(), 0);

    }


    @Test
    void addCoupon_정상_상품() throws Exception {


        UserVo userVO = new UserVo();
        userVO.setUserId("1");
        userVO.setLoginId("forbiz");
        userVO.setLangCode("1");
        SessionUtil.setUserVO(userVO);
        CouponRequestDto addDto = new CouponRequestDto();
        addDto.setCouponType(CouponEnums.CouponType.GOODS.getCode());                //상품
        addDto.setBosCouponName("TEST 상품 쿠폰명등록");
        addDto.setDisplayCouponName("TEST 상품  전시쿠폰명 등록");
        addDto.setIssueStartDate("2020-10-21 00:00:00");
        addDto.setIssueEndDate("2020-10-28 00:00:00");
        addDto.setValidityStartDate("2020-10-21 00:00:00");
        addDto.setValidityEndDate("2020-10-28 00:00:00");
        addDto.setIssuePurposeType(CouponEnums.PaymentType.GOODS_DETAIL.getCode());  //상품상세
        addDto.setIssueQtyLimit("COUPON_LIMIT.2");
        addDto.setIssueBudget("1000000");
        addDto.setUseMobileWebYn("Y");
        addDto.setUseMobileAppYn("Y");
        addDto.setUsePcYn("Y");
        addDto.setDiscountType(CouponEnums.DiscountType.FIXED_DISCOUNT.getCode());
        addDto.setDiscountVal("500");
        addDto.setMinPaymentAmount("10000");
        addDto.setIssuePurposeType("ISSUE_PURPOSE_TYPE.ALLIANCE");
        addDto.setErpOrganizationCode("24715");
        addDto.setPercentageFirst("20");
        addDto.setPaymentType("test");
        addDto.setValidityType("test");
        addDto.setCoverageType("test");
        addDto.setCreateId("1");

        //Coverage 쿠폰적용범위
        addDto.setInsertData("[{\"coverageName\":\" 테스트_포장단위 소수점\",\"includeYnName\":\"포함\",\"includeYn\":\"Y\",\"coverageId\":10000041,\"coverageType\":\"APPLYCOVERAGE.GOODS\"}]");


        promotionCouponService.addCoupon(addDto);

    }

    @Test
    void addCoupon_정상_장바구니() throws Exception {

        UserVo userVO = new UserVo();
        userVO.setUserId("1");
        userVO.setLoginId("forbiz");
        userVO.setLangCode("1");
        SessionUtil.setUserVO(userVO);
        CouponRequestDto addDto = new CouponRequestDto();
        addDto.setCouponType(CouponEnums.CouponType.CART.getCode());                //장바구니
        addDto.setBosCouponName("TEST 상품 쿠폰명등록");
        addDto.setDisplayCouponName("TEST 상품  전시쿠폰명 등록");
        addDto.setIssueStartDate("2020-10-21 00:00:00");
        addDto.setIssueEndDate("2020-10-28 00:00:00");
        addDto.setValidityStartDate("2020-10-21 00:00:00");
        addDto.setValidityEndDate("2020-10-28 00:00:00");
        addDto.setIssuePurposeType(CouponEnums.PaymentType.AUTO_PAYMENT.getCode());  //자동지급
        addDto.setIssueDetailType(CouponEnums.IssueDetailType.RECOMMENDER.getCode());    //추천인
        addDto.setIssueQtyLimit("COUPON_LIMIT.2");
        addDto.setIssueBudget("1000000");
        addDto.setUseMobileWebYn("Y");
        addDto.setUseMobileAppYn("Y");
        addDto.setUsePcYn("Y");
        addDto.setDiscountType(CouponEnums.DiscountType.FIXED_DISCOUNT.getCode());
        addDto.setDiscountVal("500");
        addDto.setMinPaymentAmount("10000");
        addDto.setIssuePurposeType("ISSUE_PURPOSE_TYPE.ALLIANCE");
        addDto.setErpOrganizationCode("24715");
        addDto.setPercentageFirst("20");
        addDto.setPaymentType("test");
        addDto.setValidityType("test");
        addDto.setCoverageType("test");
        addDto.setCreateId("1");

        //Coverage 쿠폰적용범위
        addDto.setInsertData("[{\"coverageName\":\" 테스트_포장단위 소수점\",\"includeYnName\":\"포함\",\"includeYn\":\"Y\",\"coverageId\":10000041,\"coverageType\":\"APPLYCOVERAGE.GOODS\"}]");

        promotionCouponService.addCoupon(addDto);

    }


    @Test
    void addCoupon_정상_배송비() throws Exception {

        UserVo userVO = new UserVo();
        userVO.setUserId("1");
        userVO.setLoginId("forbiz");
        userVO.setLangCode("1");
        SessionUtil.setUserVO(userVO);
        CouponRequestDto addDto = new CouponRequestDto();
        addDto.setCouponType(CouponEnums.CouponType.SHIPPING_PRICE.getCode());                //배송비
        addDto.setBosCouponName("TEST 상품 쿠폰명등록");
        addDto.setDisplayCouponName("TEST 상품  전시쿠폰명 등록");
        addDto.setIssueStartDate("2020-10-21 00:00:00");
        addDto.setIssueEndDate("2020-10-28 00:00:00");
        addDto.setValidityStartDate("2020-10-21 00:00:00");
        addDto.setValidityEndDate("2020-10-28 00:00:00");
        addDto.setIssuePurposeType(CouponEnums.PaymentType.CHECK_PAYMENT.getCode());   //계정발급
        addDto.setUploadUser("[{\"loginId\":\"rlaalstnek1\"},{\"loginId\":\"test2580\"}]");
        addDto.setIssueQtyLimit("COUPON_LIMIT.2");
        addDto.setIssueBudget("1000000");
        addDto.setUseMobileWebYn("Y");
        addDto.setUseMobileAppYn("Y");
        addDto.setUsePcYn("Y");
        addDto.setDiscountType(CouponEnums.DiscountType.FIXED_DISCOUNT.getCode());
        addDto.setDiscountVal("500");
        addDto.setMinPaymentAmount("10000");
        addDto.setIssuePurposeType("ISSUE_PURPOSE_TYPE.ALLIANCE");
        addDto.setErpOrganizationCode("24715");
        addDto.setPercentageFirst("20");
        addDto.setMinPaymentAmount("6000");
        addDto.setDiscountVal("1000");
        addDto.setPaymentType("test");
        addDto.setValidityType("test");
        addDto.setCoverageType("test");
        addDto.setCreateId("1");

        //Coverage 쿠폰적용범위
        addDto.setInsertData("[{\"coverageName\":\"용인 통합물류\",\"includeYnName\":\"포함\",\"includeYn\":\"Y\",\"coverageId\":\"120\",\"coverageType\":\"APPLYCOVERAGE.WAREHOUSE\"}]");


        promotionCouponService.addCoupon(addDto);

    }

    @Test
    void putCoupon_정상() throws Exception {
        UserVo userVO = new UserVo();
        userVO.setUserId("1");
        userVO.setLoginId("forbiz");
        userVO.setLangCode("1");
        SessionUtil.setUserVO(userVO);
        CouponRequestDto addDto = new CouponRequestDto();
        addDto.setCouponType(CouponEnums.CouponType.GOODS.getCode());                //상품
        addDto.setBosCouponName("TEST 상품 쿠폰명등록");
        addDto.setDisplayCouponName("TEST 상품  전시쿠폰명 등록");
        addDto.setIssueStartDate("2020-10-21 00:00:00");
        addDto.setIssueEndDate("2020-10-28 00:00:00");
        addDto.setValidityStartDate("2020-10-21 00:00:00");
        addDto.setValidityEndDate("2020-10-28 00:00:00");
        addDto.setIssuePurposeType(CouponEnums.PaymentType.GOODS_DETAIL.getCode());  //상품상세
        addDto.setIssueQtyLimit("COUPON_LIMIT.2");
        addDto.setIssueBudget("1000000");
        addDto.setUseMobileWebYn("Y");
        addDto.setUseMobileAppYn("Y");
        addDto.setUsePcYn("Y");
        addDto.setDiscountType(CouponEnums.DiscountType.FIXED_DISCOUNT.getCode());
        addDto.setDiscountVal("500");
        addDto.setMinPaymentAmount("10000");
        addDto.setIssuePurposeType("ISSUE_PURPOSE_TYPE.ALLIANCE");
        addDto.setErpOrganizationCode("24715");
        addDto.setPercentageFirst("20");

        //Coverage 쿠폰적용범위
        addDto.setInsertData("[{\"coverageName\":\" 테스트_포장단위 소수점\",\"includeYnName\":\"포함\",\"includeYn\":\"Y\",\"coverageId\":10000041,\"coverageType\":\"APPLYCOVERAGE.GOODS\"}]");


        promotionCouponService.putCoupon(addDto);
    }


    @Test
    void removeCoupon_정상() throws Exception {
        CouponRequestDto removeDto = new CouponRequestDto();
        removeDto.setPmCouponId("100");

        promotionCouponService.removeCoupon(removeDto);

    }


    @Test
    void updateCouponName_정상() throws Exception {

        UserVo userVO = new UserVo();
        userVO.setUserId("1");
        userVO.setLoginId("forbiz");
        userVO.setLangCode("1");
        SessionUtil.setUserVO(userVO);
        CouponRequestDto dto = new CouponRequestDto();
        dto.setPmCouponId("199");
        dto.setBosCouponName("UPDATE 테스트 쿠폰명 수정");
        dto.setDisplayCouponName("UPDATE 테스트 전시 쿠폰명 수정");
        promotionCouponService.updateCouponName(dto);
    }


    @Test
    void getCpnMgmList_정상() throws Exception {

        CouponRequestDto dto = new CouponRequestDto();

        Page<IssueListResultVo> voList = promotionCouponService.getCpnMgmList(dto);

        assertTrue(voList.getResult().size() > 0);
    }


    @Test
    void getCpnMgmList_조회내역없음() throws Exception {

        CouponRequestDto dto = new CouponRequestDto();
        dto.setSearchStatusComment("19990101");

        Page<IssueListResultVo> voList = promotionCouponService.getCpnMgmList(dto);

        assertEquals(voList.getResult().size(), 0);
    }


    @Test
    void getCpnMgmIssueList_정상() throws Exception {

        BuyerInfoRequestDto dto = new BuyerInfoRequestDto();
        dto.setSearchType("MOBILE");
        dto.setSearchValue("010");
        Page<BuyerInfoListResultVo> voList = promotionCouponService.getCpnMgmIssueList(dto);

        assertTrue(voList.getResult().size() > 0);

    }


    @Test
    void getCpnMgmIssueList_조회내역없음() throws Exception {

        BuyerInfoRequestDto dto = new BuyerInfoRequestDto();
        dto.setSearchType("EMAIL");
        dto.setSearchValue("19990101");

        Page<BuyerInfoListResultVo> voList = promotionCouponService.getCpnMgmIssueList(dto);

        assertEquals(voList.getResult().size(), 0);

    }

    @Test
    void putCancelDepositList_정상() throws Exception {

        CouponIssueParamDto dto = new CouponIssueParamDto();

        UserVo userVO = new UserVo();
        userVO.setUserId("1");
        userVO.setLoginId("forbiz");
        userVO.setLangCode("1");
        SessionUtil.setUserVO(userVO);

        List<BuyerInfoListResultVo> resultList = new ArrayList<>();
        BuyerInfoListResultVo aVo = new BuyerInfoListResultVo();
        aVo.setPmCouponIssueId("10");
        BuyerInfoListResultVo bVo = new BuyerInfoListResultVo();
        bVo.setPmCouponIssueId("11");
        resultList.add(bVo);
//        dto.setUpdateRequestDtoList(resultList);

        promotionCouponService.putCancelDepositList(dto);

    }


    @Test
    void putCouponStatus_정상() throws Exception {

//        UserVo userVO = new UserVo();
//        userVO.setUserId("1");
//        userVO.setLoginId("forbiz");
//        userVO.setLangCode("1");
//        SessionUtil.setUserVO(userVO);
//        CouponRequestDto dto = new CouponRequestDto();
//
//        dto.setPmCouponId("199");
//        dto.setStatus(CouponEnums.CouponApprovalStatus.APPROVED.getCode());
//        dto.setDisplayCouponName("UPDATE 테스트 전시 쿠폰명 수정");
//
//        promotionCouponService.putCouponStatus(dto);
    }


    /**********************************************************************************************************************************************************************************************/
    /**********************************************************************************************************************************************************************************************/
    /**********************************************************************************************************************************************************************************************/
    /**********************************************************************************************************************************************************************************************/
    /**********************************************************************************************************************************************************************************************/


    @Test
    void getCouponListByUser_정상() throws Exception {
        //given
        CouponListByUserRequestDto dto = new CouponListByUserRequestDto();
        dto.setUrUserId(1647338L);

        //when
        CouponListByUserResponseDto resultDto = promotionCouponService.getCouponListByUser(dto);

        //then
        assertTrue(resultDto.getRows().size() > 0);
    }

    @Test
    void getCouponListByUser_조회내역없음() throws Exception {
        //given
        CouponListByUserRequestDto dto = new CouponListByUserRequestDto();
        dto.setUrUserId(null);

        //when
        CouponListByUserResponseDto resultDto = promotionCouponService.getCouponListByUser(dto);

        //then
        assertEquals(resultDto.getRows().size(), 0);
    }

    @Test
    void getCouponCoverage_정상() throws Exception {
        //given
        Long pmCouponId = 447L;

        //when
        CouponCoverageResponseDto resultDto = promotionCouponService.getCouponCoverage(pmCouponId);

        //then
        assertTrue(resultDto.getCoverage().size() > 0);
    }

    @Test
    void getCouponCoverage_조회내역없음() throws Exception {
        //given
        Long pmCouponId = null;

        //when
        CouponCoverageResponseDto resultDto = promotionCouponService.getCouponCoverage(pmCouponId);

        //then
        assertEquals(resultDto.getCoverage().size(), 0);
    }

    @Test
    void checkAddCouponValidationByUser_정상() throws Exception {
        //given
        CouponValidationInfoVo returnVo = new CouponValidationInfoVo();
        returnVo.setCouponMasterStat(CouponEnums.CouponMasterStatus.APPROVED.getCode());
        returnVo.setIssueQty(10);
        returnVo.setIssueCnt(6);
        returnVo.setIssueQtyLimit(1);
        returnVo.setUserIssueCnt(0);
        returnVo.setIssueStartDate(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        returnVo.setIssueEndDate(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));

        given(mockPromotionCouponMapper.getAddCouponValidationInfo(any(), any())).willReturn(returnVo);
        given(mockPromotionCouponMapper.putCouponIssue(any())).willReturn(1);

        //when
        CouponValidationByUserResponseDto result = mockPromotionCouponService.checkAddCouponValidationByUser(1L, 1L);

        //then
        assertEquals(CouponEnums.AddCouponValidation.PASS_VALIDATION, result.getValidationEnum());
    }

    @Test
    void checkAddCouponValidationByUser_사용상태아님() throws Exception {
        //given
        CouponValidationInfoVo returnVo = new CouponValidationInfoVo();
        returnVo.setCouponMasterStat(CouponEnums.CouponMasterStatus.STOP.getCode());
        given(mockPromotionCouponMapper.getAddCouponValidationInfo(any(), any())).willReturn(returnVo);

        //when
        CouponValidationByUserResponseDto result = mockPromotionCouponService.checkAddCouponValidationByUser(1L, 1L);

        //then
        assertEquals(CouponEnums.AddCouponValidation.NOT_ACCEPT_APPROVAL, result.getValidationEnum());
    }

    @Test
    void checkAddCouponValidationByUser_발급수량_초과() throws Exception {
        //given
        CouponValidationInfoVo returnVo = new CouponValidationInfoVo();
        returnVo.setCouponMasterStat(CouponEnums.CouponMasterStatus.APPROVED.getCode());
        returnVo.setIssueQty(10);
        returnVo.setIssueCnt(10);

        given(mockPromotionCouponMapper.getAddCouponValidationInfo(any(), any())).willReturn(returnVo);

        //when
        CouponValidationByUserResponseDto result = mockPromotionCouponService.checkAddCouponValidationByUser(1L, 1L);

        //then
        assertEquals(CouponEnums.AddCouponValidation.OVER_ISSUE_QTY, result.getValidationEnum());
    }

    @Test
    void checkAddCouponValidationByUser_1인발급제한수량_초과() throws Exception {
        //given
        CouponValidationInfoVo returnVo = new CouponValidationInfoVo();
        returnVo.setCouponMasterStat(CouponEnums.CouponMasterStatus.APPROVED.getCode());
        returnVo.setIssueQty(10);
        returnVo.setIssueCnt(6);
        returnVo.setIssueQtyLimit(1);
        returnVo.setUserIssueCnt(1);

        given(mockPromotionCouponMapper.getAddCouponValidationInfo(any(), any())).willReturn(returnVo);

        //when
        CouponValidationByUserResponseDto result = mockPromotionCouponService.checkAddCouponValidationByUser(1L, 1L);

        //then
        assertEquals(CouponEnums.AddCouponValidation.OVER_ISSUE_QTY_LIMIT, result.getValidationEnum());
    }

    @Test
    void checkAddCouponValidationByUser_발급시작일_이전() throws Exception {
        //given
        CouponValidationInfoVo returnVo = new CouponValidationInfoVo();
        returnVo.setCouponMasterStat(CouponEnums.CouponMasterStatus.APPROVED.getCode());
        returnVo.setIssueQty(10);
        returnVo.setIssueCnt(6);
        returnVo.setIssueQtyLimit(1);
        returnVo.setUserIssueCnt(0);
        returnVo.setIssueStartDate("2099-01-01");
        returnVo.setIssueEndDate("2099-12-01");

        given(mockPromotionCouponMapper.getAddCouponValidationInfo(any(), any())).willReturn(returnVo);

        //when
        CouponValidationByUserResponseDto result = mockPromotionCouponService.checkAddCouponValidationByUser(1L, 1L);

        //then
        assertEquals(CouponEnums.AddCouponValidation.NOT_ISSUE_DATE, result.getValidationEnum());
    }

    @Test
    void checkAddCouponValidationByUser_발급종료일_지남() throws Exception {
        //given
        CouponValidationInfoVo returnVo = new CouponValidationInfoVo();
        returnVo.setCouponMasterStat(CouponEnums.CouponMasterStatus.APPROVED.getCode());
        returnVo.setIssueQty(10);
        returnVo.setIssueCnt(6);
        returnVo.setIssueQtyLimit(1);
        returnVo.setUserIssueCnt(0);
        returnVo.setIssueStartDate("1999-01-01");
        returnVo.setIssueEndDate("1999-12-01");

        given(mockPromotionCouponMapper.getAddCouponValidationInfo(any(), any())).willReturn(returnVo);

        //when
        CouponValidationByUserResponseDto result = mockPromotionCouponService.checkAddCouponValidationByUser(1L, 1L);

        //then
        assertEquals(CouponEnums.AddCouponValidation.OVER_ISSUE_DATE, result.getValidationEnum());
    }

    @Test
    void addCouponByUser_정상() throws Exception {
        //given
        CouponValidationInfoVo dto = new CouponValidationInfoVo();
        dto.setPmCouponId(1L);
        dto.setValidityType(CouponEnums.ValidityType.PERIOD.getCode());
        dto.setValidityEndDate("1999-01-01");
        dto.setValidityDay(0);
        Long urUserId = 1L;
        Long pmSerialNumberId = 1L;

        //when, then
        promotionCouponService.addCouponByUser(dto, urUserId, pmSerialNumberId);
    }

    @Test
    void getCouponCountByUser_조회_정상() throws Exception {
        //given
        Long urUserId = 1646893L;
        String status = "";

        //when
        CouponCountByUserVo resultDto = promotionCouponService.getCouponCountByUser(urUserId, status);

        //then
        assertFalse(Objects.isNull(resultDto.getGoodsCount()));
    }

    @Test
    void getGoodsApplyCouponList_성공() throws Exception {
        Long ilGoodsId = 13765L;
        Long urUserId = 1647338L;

        List<GoodsApplyCouponDto> goodsApplyCouponList = promotionCouponService.getGoodsApplyCouponList(ilGoodsId, urUserId);

        assertTrue(goodsApplyCouponList.size() > 0);
    }

    @Test
    void getGoodsApplyCouponList_조회결과없음() throws Exception {
        Long ilGoodsId = 1L;
        Long urUserId = 1L;

        List<GoodsApplyCouponDto> goodsApplyCouponList = promotionCouponService.getGoodsApplyCouponList(ilGoodsId, urUserId);

        assertEquals(goodsApplyCouponList.size(), 0);
    }

    @Test
    void getNewBuyerSpecialsCouponByNonMember_성공() throws Exception {
        //given
        Long ilGoodsId = 4553L;
        String deviceInfo = "pc";
        boolean isApp = false;

        //when
        int newBuyerSpecialsCouponByNonMember = promotionCouponService.getNewBuyerSpecialsCouponByNonMember(ilGoodsId, deviceInfo, isApp);

        //then
        assertTrue(newBuyerSpecialsCouponByNonMember > 0);
    }

    @Test
    void getNewBuyerSpecialsCouponByNonMember_조회결과없음() throws Exception {
        //given
        Long ilGoodsId = 1L;
        String deviceInfo = "pc";
        boolean isApp = false;

        //when
        int newBuyerSpecialsCouponByNonMember = promotionCouponService.getNewBuyerSpecialsCouponByNonMember(ilGoodsId, deviceInfo, isApp);

        //then
        assertEquals(0, newBuyerSpecialsCouponByNonMember);
    }

    @Test
    void getCouponInfoByUserJoin_조회_정상() throws Exception {
        //given

        //when
        List<CouponInfoByUserJoinVo> resultDto = promotionCouponService.getCouponInfoByUserJoin();

        //then
        assertTrue(resultDto.size() > 0);
    }

    @Test
    void checkUseCouponValidation_조회결과없음() throws Exception {
        //given
        given(mockPromotionCouponMapper.getUseCouponValidationInfo(any(), any())).willReturn(null);

        //when
        CouponEnums.UseCouponValidation result = mockPromotionCouponService.checkUseCouponValidation(1L, 1L);

        //then
        assertEquals(CouponEnums.UseCouponValidation.NOT_EXIST_COUPON, result);
    }

    @Test
    void checkUseCouponValidation_쿠폰사용상태아님() throws Exception {
        //given
        UseCouponValidationInfoVo returnVo = new UseCouponValidationInfoVo();
        returnVo.setCouponStatus(CouponEnums.CouponMasterStatus.STOP_WITHDRAW.getCode());

        given(mockPromotionCouponMapper.getUseCouponValidationInfo(any(), any())).willReturn(returnVo);

        //when
        CouponEnums.UseCouponValidation result = mockPromotionCouponService.checkUseCouponValidation(1L, 1L);

        //then
        assertEquals(CouponEnums.UseCouponValidation.NOT_COUPON_STATUS, result);
    }

    @Test
    void checkUseCouponValidation_사용한쿠폰() throws Exception {
        //given
        UseCouponValidationInfoVo returnVo = new UseCouponValidationInfoVo();
        returnVo.setCouponStatus(CouponEnums.CouponMasterStatus.STOP.getCode());
        returnVo.setIssueStatus(CouponEnums.CouponStatus.USE.getCode());

        given(mockPromotionCouponMapper.getUseCouponValidationInfo(any(), any())).willReturn(returnVo);

        //when
        CouponEnums.UseCouponValidation result = mockPromotionCouponService.checkUseCouponValidation(1L, 1L);

        //then
        assertEquals(CouponEnums.UseCouponValidation.USE_ISSUE_STATUS, result);
    }

    @Test
    void checkUseCouponValidation_취소된쿠폰() throws Exception {
        //given
        UseCouponValidationInfoVo returnVo = new UseCouponValidationInfoVo();
        returnVo.setCouponStatus(CouponEnums.CouponMasterStatus.STOP.getCode());
        returnVo.setIssueStatus(CouponEnums.CouponStatus.CANCEL.getCode());

        given(mockPromotionCouponMapper.getUseCouponValidationInfo(any(), any())).willReturn(returnVo);

        //when
        CouponEnums.UseCouponValidation result = mockPromotionCouponService.checkUseCouponValidation(1L, 1L);

        //then
        assertEquals(CouponEnums.UseCouponValidation.CANCEL_ISSUE_STATUS, result);
    }

    @Test
    void checkUseCouponValidation_유효기간지남() throws Exception {
        //given
        UseCouponValidationInfoVo returnVo = new UseCouponValidationInfoVo();
        returnVo.setCouponStatus(CouponEnums.CouponMasterStatus.STOP.getCode());
        returnVo.setIssueStatus(CouponEnums.CouponStatus.NOTUSE.getCode());
        returnVo.setExpirationDate("2000-12-31");

        given(mockPromotionCouponMapper.getUseCouponValidationInfo(any(), any())).willReturn(returnVo);

        //when
        CouponEnums.UseCouponValidation result = mockPromotionCouponService.checkUseCouponValidation(1L, 1L);

        //then
        assertEquals(CouponEnums.UseCouponValidation.OVER_EXPIRATION, result);
    }

    @Test
    void checkUseCouponValidation_정상() throws Exception {
        //given
        UseCouponValidationInfoVo returnVo = new UseCouponValidationInfoVo();
        returnVo.setCouponStatus(CouponEnums.CouponMasterStatus.STOP.getCode());
        returnVo.setIssueStatus(CouponEnums.CouponStatus.NOTUSE.getCode());
        returnVo.setExpirationDate("2999-12-31");

        given(mockPromotionCouponMapper.getUseCouponValidationInfo(any(), any())).willReturn(returnVo);

        //when
        CouponEnums.UseCouponValidation result = mockPromotionCouponService.checkUseCouponValidation(1L, 1L);

        //then
        assertEquals(CouponEnums.UseCouponValidation.PASS_VALIDATION, result);
    }

    @Test
    void useCoupon_정상() throws Exception {
        // given
        Long urUserId = 1L;
        Long pmCouponIssueId = 1L;

        // when, then
        promotionCouponService.useCoupon(urUserId, pmCouponIssueId);
    }

    @Test
    void getCouponApplicationListByPmCouponIssueId_정상() throws Exception {
        //given
        Long pmCouponIssueId = 441665L;

        //when
        List<CouponApplicationListByUserVo> getCouponApplicationList = promotionCouponService.getCouponApplicationListByPmCouponIssueId(pmCouponIssueId);

        //then
        assertTrue(getCouponApplicationList.size() > 0);
    }

    @Test
    void getCouponApplicationListByPmCouponIssueId_조회결과없음() throws Exception {
        Long pmCouponIssueId = 1L;

        List<CouponApplicationListByUserVo> getCouponApplicationList = promotionCouponService.getCouponApplicationListByPmCouponIssueId(pmCouponIssueId);

        assertFalse(getCouponApplicationList.size() > 0);
    }

    @Test
    void getUserJoinGoods_조회_성공() throws Exception {
        //given, when
        List<CouponGoodsByUserJoinVo> result = promotionCouponService.getUserJoinGoods();

        //then
        assertTrue(result.size() > 0);
    }

    @Test
    void calculationDiscount_계산_Percent() throws Exception {
        //given
        int salePrice = 10000;
        String discountMethodType = GoodsEnums.CouponDiscountStatus.PERCENTAGE_DISCOUNT.getCode();
        int discountRate = 50;

        //when
        DiscountCalculationResultDto result = promotionCouponService.calculationDiscount(salePrice, discountMethodType, discountRate, 0);

        //then
        assertEquals(5000, result.getDiscountPrice());
    }

    @Test
    void calculationDiscount_계산_고정() throws Exception {
        //given
        int salePrice = 10000;
        String discountMethodType = GoodsEnums.CouponDiscountStatus.FIXED_DISCOUNT.getCode();
        int discountRate = 1000;

        //when
        DiscountCalculationResultDto result = promotionCouponService.calculationDiscount(salePrice, discountMethodType, discountRate, 0);

        //then
        assertEquals(1000, result.getDiscountPrice());
    }

    @Test
    void getShippingCouponApplicationListByUser_조회_성공() throws Exception {
        //given
        Long urUserId = 1647338L;
        Long urWarehouseId = 85L;

        //when
        List<CouponApplicationListByUserVo> result = promotionCouponService.getShippingCouponApplicationListByUser(urUserId, urWarehouseId);

        //then
        assertTrue(result.size() > 0);
    }

    @Test
    void getCartCouponApplicationListByUser_조회_성공() throws Exception {
        //given
        Long urUserId = 1647338L;

        //when
        List<CouponApplicationListByUserVo> result = promotionCouponService.getCartCouponApplicationListByUser(urUserId);

        //then
        assertTrue(result.size() > 0);
    }

    @Test
    void getCouponByPmCouponIdList_조회_성공() throws Exception {
        //given
        List<Long> pmCouponIdList = Collections.singletonList(125L);
        Long urUserId = 0L;

        //when
        List<GoodsApplyCouponDto> result = promotionCouponService.getCouponByPmCouponIdList(pmCouponIdList, urUserId);

        //then
        assertTrue(result.size() > 0);
    }

    @Test
    void putWithdrawalMemberCoupon() throws Exception {
        //given
        Long urUserId = 1L;

        //when, then
        promotionCouponService.putWithdrawalMemberCoupon(urUserId);
    }


    @Test
    void getCallEventCouponInfo_정상() throws Exception {

        CouponRequestDto dto = new CouponRequestDto();
        dto.setUseYn("Y");
        dto.setIssueType("PAYMENT_TYPE.AUTO_PAYMENT");
        dto.setIssueDetailType("AUTO_ISSUE_TYPE.EVENT_AWARD");

        ApiResult<?> apiResult = promotionCouponService.getEventCallCouponInfo(dto);

        assertTrue(BaseEnums.Default.SUCCESS.getCode() == apiResult.getCode());


    }

    @Test
    void getCouponIssueByPmCouponIssueId_성공() throws Exception{
    	Long pmCouponIssueId = 440486L;

    	CouponIssueVo couponIssueVo = promotionCouponService.getCouponIssueByPmCouponIssueId(pmCouponIssueId);

    	assertNotNull(couponIssueVo);
    }

    @Test
    void getCouponIssueByPmCouponIssueId_조회결과없음() throws Exception{
    	Long pmCouponIssueId = 0L;

    	CouponIssueVo couponIssueVo = promotionCouponService.getCouponIssueByPmCouponIssueId(pmCouponIssueId);

    	assertNull(couponIssueVo);
    }

    @Test
    void addPmCouponCoverageByPmCouponId_성공() throws Exception{
    	Long pmCouponId = 1111L;
    	Long originPmCouponId = 447L;

    	int result = promotionCouponService.addPmCouponCoverageByPmCouponId(pmCouponId, originPmCouponId);

    	assertTrue(result > 0);
    }

    @Test
    void addPmCouponPointShareOrganizaionByPmCouponId_성공() throws Exception{
    	Long pmCouponId = 99L;
    	Long originPmCouponId = 457L;

    	int result = promotionCouponService.addPmCouponPointShareOrganizaionByPmCouponId(pmCouponId, originPmCouponId);

    	assertTrue(result > 0);
    }

    @Test
    void isDeviceTypeActive_성공() throws Exception{
    	GoodsEnums.DeviceType deviceType = GoodsEnums.DeviceType.PC;
    	CouponApplicationListByUserVo coupon = new CouponApplicationListByUserVo();
    	coupon.setUsePcYn("Y");

    	assertTrue(promotionCouponService.isDeviceTypeActive(deviceType, coupon));
    }

    @Test
    void isDeviceTypeActive_실패() throws Exception{
    	GoodsEnums.DeviceType deviceType = GoodsEnums.DeviceType.PC;
    	CouponApplicationListByUserVo coupon = new CouponApplicationListByUserVo();
    	coupon.setUsePcYn("");

    	assertFalse(promotionCouponService.isDeviceTypeActive(deviceType, coupon));
    }

    @Test
    void asis_쿠폰지급대상리스트저장_성공() throws Exception {
        CouponJoinCertEventRequestDto couponJoinCertEventRequestDto = new CouponJoinCertEventRequestDto();
        couponJoinCertEventRequestDto.setCustomerNumber("123456");
        couponJoinCertEventRequestDto.setUrUserId("123456");
        couponJoinCertEventRequestDto.setSiteNo("0000000000");

        assertTrue(promotionCouponService.addPmCouponJoinEventListByJoinUrUserId(couponJoinCertEventRequestDto) > 0);
    }


    @Test
    void getUserIdCnt_성공() throws Exception {
    	CouponRequestDto dto = new CouponRequestDto();
    	List<UploadInfoVo> resultList = new ArrayList<>();
    	UploadInfoVo uploadVo = new UploadInfoVo();
    	uploadVo.setLoginId("forbiz");
        resultList.add(uploadVo);
        dto.setUploadUserList(resultList);
        int result = mockPromotionCouponService.getUserIdCnt(dto);

        assertTrue(result < 1);
    }
}