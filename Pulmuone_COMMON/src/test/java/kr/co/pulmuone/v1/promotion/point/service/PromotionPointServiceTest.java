package kr.co.pulmuone.v1.promotion.point.service;

import com.github.pagehelper.Page;
import kr.co.pulmuone.v1.comm.CommonServiceTestBaseForJunit5;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.base.vo.UserVo;
import kr.co.pulmuone.v1.comm.enums.ApprovalEnums;
import kr.co.pulmuone.v1.comm.enums.BaseEnums;
import kr.co.pulmuone.v1.comm.enums.PointEnums;
import kr.co.pulmuone.v1.comm.mapper.approval.auth.ApprovalAuthMapper;
import kr.co.pulmuone.v1.comm.mapper.promotion.point.PointUseMapper;
import kr.co.pulmuone.v1.comm.mapper.promotion.point.PromotionPointMapper;
import kr.co.pulmuone.v1.comm.mapper.user.join.UserJoinMapper;
import kr.co.pulmuone.v1.comm.util.SessionUtil;
import kr.co.pulmuone.v1.policy.config.service.PolicyConfigBiz;
import kr.co.pulmuone.v1.promotion.point.dto.*;
import kr.co.pulmuone.v1.promotion.point.dto.vo.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

class PromotionPointServiceTest extends CommonServiceTestBaseForJunit5 {
    @Autowired
    PromotionPointService promotionPointService;

    @InjectMocks
    private PromotionPointService mockPromotionPointService;

    @Mock
    PolicyConfigBiz policyConfigBiz;

    @Mock
    PromotionPointMapper mockPromotionPointMapper;

    @Mock
    ApprovalAuthMapper approvalAuthMapper;

    @Mock
    PointUseMapper mockPointUseMapper;

    @Mock
    UserJoinMapper mockUserJoinMapper;

    @BeforeEach
    void setUp() {
        preLogin();
        mockPromotionPointService = new PromotionPointService(policyConfigBiz, mockPromotionPointMapper, approvalAuthMapper, mockUserJoinMapper, mockPointUseMapper);
    }

    @Test
    void getPointSettingList_정상() throws Exception {
    	//given
    	PointSettingListRequestDto dto = new PointSettingListRequestDto();
    	dto.setSearchPointStatus("POINT_APPROVAL_STAT");

    	//when
    	Page<PointSettingResultVo> result = promotionPointService.getPointSettingList(dto);

        //then
        Assertions.assertFalse(result.getTotal()> 0);
    }


    @Test
    void getPointSettingList_조회내역없음() throws Exception {
    	//given
    	PointSettingListRequestDto dto = new PointSettingListRequestDto();
    	dto.setSearchPointName("--------------");

    	//when
    	Page<PointSettingResultVo> result = promotionPointService.getPointSettingList(dto);

        //then
        assertTrue(result.getResult().size()== 0);
    }

    @Test
    void getPointDetail_조회() throws Exception {
    	//given
    	PointRequestDto dto = new PointRequestDto();
    	dto.setPmPointId("42");

    	//when
    	PointResponseDto result = promotionPointService.getPointDetail(dto);

    	//then
    	assertEquals(dto.getPmPointId(), result.getRows().getPmPointId());
    }

    @Test
    void addPointSetting_정상_이용권() throws Exception {
        //given
    	PointSettingMgmRequestDto dto = new PointSettingMgmRequestDto();

    	dto.setApprovalCheck("N");
    	dto.setPointType("POINT_TYPE.SERIAL_NUMBER");
        dto.setPointName("JUNIT TEST");
        dto.setIssueStartDate("20200929");
        dto.setIssueEndDate("20200930");
        dto.setIssueBudget("100000000");
        dto.setIssueQtyLimit("10");
        dto.setIssueValue("500");
        dto.setValidityDay("3");
        dto.setValidityType("VALIDITY_TYPE.VALIDITY");
        dto.setIssueQty(7);
        dto.setSerialNumberType("SERIAL_NUMBER_TYPE.FIXED_VALUE");
        dto.setFixSerialNumber("JUNIT FIX SERIAL NUMBER");
        dto.setUserId("1");
        dto.setErpOrganizationCode("23014");
        dto.setUploadUser("");
        dto.setUploadTicket("");

        UserVo userVO = new UserVo();
        userVO.setUserId("1");
        userVO.setLoginId("forbiz");
        userVO.setLangCode("1");
        SessionUtil.setUserVO(userVO);

        //when, then
        ApiResult<?> apiResult = promotionPointService.addPointSetting(dto);

        assertTrue(BaseEnums.Default.SUCCESS.getCode() == apiResult.getCode());


    }


    @Test
    void addPointSetting_정상_후기() throws Exception {
        //given
    	PointSettingMgmRequestDto dto = new PointSettingMgmRequestDto();

    	dto.setApprovalCheck("N");
    	dto.setPointType("POINT_TYPE.FEEDBACK");
        dto.setPointName("JUNIT TEST");
        dto.setIssueStartDate("20200929");
        dto.setIssueEndDate("20200930");
        dto.setNormalAmount("100");
        dto.setNormalValidityDay("5");
        dto.setPhotoAmount("200");
        dto.setPhotoValidityDay("3");
        dto.setPremiumAmount("300");
        dto.setPremiumValidityDay("2");
        dto.setIssueDayCount("10");

        String userVo = "";
        userVo = "[{" + "normalAmount:'100',"
        		     + "photoAmount:'100',"
        		     + "premiumAmount:'100',"
        		     + "normalValidityDay:'1',"
        		     + "photoValidityDay:'1',"
        		     + "premiumValidityDay:'1',"
        		     + "reviewType:'POINT_USERGRADE_TYPE.USER_GRADE',"
        		     + "reviewDetailType:'USER_GRADE.NORMAL'"
        		     + "}]"
        		;
        dto.setUserGradeList(userVo);
        dto.setValidityType("VALIDITY_TYPE.VALIDITY");
        dto.setUrUserId("1");
        dto.setErpOrganizationCode("23014");
        dto.setUploadUser("");
        dto.setUploadTicket("");

        //when, then
        ApiResult<?> apiResult = promotionPointService.addPointSetting(dto);

        assertTrue(BaseEnums.Default.SUCCESS.getCode() == apiResult.getCode());

    }


    @Test
    void addPointSetting_정상_자동지급() throws Exception {
        //given
    	PointSettingMgmRequestDto dto = new PointSettingMgmRequestDto();

    	dto.setApprovalCheck("N");
    	dto.setPointType("POINT_TYPE.AUTO");
        dto.setPointName("JUNIT TEST");
        dto.setIssueStartDate("20200929");
        dto.setIssueEndDate("20200930");
        dto.setIssueBudget("100000000");
        dto.setIssueQtyLimit("10");
        dto.setIssueValue("500");
        dto.setValidityDay("3");
        dto.setValidityType("VALIDITY_TYPE.VALIDITY");
        dto.setPointPaymentDetailType("POINT_AUTO_ISSUE_TP.RECOMMENDER_FAVOR");
        dto.setUrUserId("1");
        dto.setErpOrganizationCode("23014");
        dto.setUploadUser("");
        dto.setUploadTicket("");

        //when, then
        ApiResult<?> apiResult = promotionPointService.addPointSetting(dto);

        assertTrue(BaseEnums.Default.SUCCESS.getCode() == apiResult.getCode());
    }

    @Test
    void addPointSetting_정상_관리자지급차감() throws Exception {
        //given
    	PointSettingMgmRequestDto dto = new PointSettingMgmRequestDto();

    	dto.setApprovalCheck("N");
    	dto.setPointType("POINT_TYPE.AUTO");
        dto.setPointName("JUNIT TEST");
        dto.setIssueValue("500");
        dto.setValidityDay("3");
        dto.setValidityType("VALIDITY_TYPE.VALIDITY");
        dto.setPointPaymentDetailType("POINT_ADMIN_ISSUE_TP.EVENT_AWARD");
        dto.setPointPaymentType("POINT_PAYMENT_TP.DEDUCTION");
        dto.setIssueReasonType("PROVISION_DEDUCTION_REASON.ORDER_USE");
        dto.setIssueReason("TEST");
        dto.setUrUserId("1");
        dto.setErpOrganizationCode("23014");
        dto.setUploadUser("");
        dto.setUploadTicket("");

        //when, then
        ApiResult<?> apiResult = promotionPointService.addPointSetting(dto);

        assertTrue(BaseEnums.Default.SUCCESS.getCode() == apiResult.getCode());
    }


    @Test
    void putPointSetting_정상_이용권() throws Exception {
        //given
    	PointSettingMgmRequestDto dto = new PointSettingMgmRequestDto();

    	dto.setApprovalCheck("N");
    	dto.setPointType("POINT_TYPE.SERIAL_NUMBER");
        dto.setPointName("JUNIT TEST UPDATE");
        dto.setIssueStartDate("20200930");
        dto.setIssueEndDate("20201001");
        dto.setIssueBudget("100000000");
        dto.setIssueQtyLimit("10");
        dto.setIssueValue("500");
        dto.setValidityDay("3");
        dto.setValidityType("VALIDITY_TYPE.VALIDITY");
        dto.setIssueQty(7);
        dto.setSerialNumberType("SERIAL_NUMBER_TYPE.FIXED_VALUE");
        dto.setFixSerialNumber("JUNIT FIX SERIAL NUMBER");
        dto.setUrUserId("1");
        dto.setErpOrganizationCode("23014");
        dto.setUploadUser("");
        dto.setUploadTicket("");
        dto.setPmPointId("39");

        //when, then
        ApiResult<?> apiResult = promotionPointService.putPointSetting(dto);

        assertTrue(BaseEnums.Default.SUCCESS.getCode() == apiResult.getCode());
    }

    @Test
    void putPointSetting_정상_후기() throws Exception {
        //given
    	PointSettingMgmRequestDto dto = new PointSettingMgmRequestDto();

    	dto.setApprovalCheck("N");
    	dto.setPointType("POINT_TYPE.FEEDBACK");
        dto.setPointName("JUNIT TEST UPDATE");
        dto.setIssueStartDate("20200928");
        dto.setIssueEndDate("20210930");
        dto.setNormalAmount("100");
        dto.setNormalValidityDay("5");
        dto.setPhotoAmount("200");
        dto.setPhotoValidityDay("3");
        dto.setPremiumAmount("300");
        dto.setPremiumValidityDay("2");
        dto.setIssueDayCount("10");

        String userVo = "";
        userVo = "[{" + "normalAmount:'100',"
        		     + "photoAmount:'100',"
        		     + "premiumAmount:'100',"
        		     + "normalValidityDay:'1',"
        		     + "photoValidityDay:'1',"
        		     + "premiumValidityDay:'1',"
        		     + "reviewType:'POINT_USERGRADE_TYPE.USER_GRADE',"
        		     + "reviewDetailType:'USER_GRADE.NORMAL'"
        		     + "},"
        		     + "{"
		     		 + "normalAmount:'200',"
        		     + "photoAmount:'200',"
        		     + "premiumAmount:'200',"
        		     + "normalValidityDay:'2',"
        		     + "photoValidityDay:'2',"
        		     + "premiumValidityDay:'2',"
        		     + "reviewType:'POINT_USERGRADE_TYPE.USER_GRADE',"
        		     + "reviewDetailType:'USER_GRADE.BEST'"
        		     + "}"
        		     + "]"
        		;
        dto.setUserGradeList(userVo);
        dto.setValidityType("VALIDITY_TYPE.VALIDITY");
        dto.setUrUserId("1");
        dto.setErpOrganizationCode("23014");
        dto.setUploadUser("");
        dto.setUploadTicket("");
        dto.setPmPointId("42");
        dto.setIssueValue("900");
        dto.setIssueBudget("9000000");
        dto.setIssueQtyLimit("0");

        //when, then
        ApiResult<?> apiResult = promotionPointService.putPointSetting(dto);

        assertTrue(BaseEnums.Default.SUCCESS.getCode() == apiResult.getCode());
    }

    @Test
    void putPointSetting_정상_자동지급() throws Exception {
        //given
    	PointSettingMgmRequestDto dto = new PointSettingMgmRequestDto();

    	dto.setApprovalCheck("N");
    	dto.setPointType("POINT_TYPE.AUTO");
        dto.setPointName("JUNIT TEST UPDATE");
        dto.setIssueStartDate("20200930");
        dto.setIssueEndDate("20201001");
        dto.setIssueBudget("100000000");
        dto.setIssueQtyLimit("10");
        dto.setIssueValue("500");
        dto.setValidityDay("3");
        dto.setValidityType("VALIDITY_TYPE.VALIDITY");
        dto.setPointPaymentDetailType("POINT_AUTO_ISSUE_TP.RECOMMENDER_FAVOR");
        dto.setUrUserId("1");
        dto.setErpOrganizationCode("23014");
        dto.setUploadUser("");
        dto.setUploadTicket("");
        dto.setPmPointId("43");

        //when, then
        ApiResult<?> apiResult = promotionPointService.putPointSetting(dto);

        assertTrue(BaseEnums.Default.SUCCESS.getCode() == apiResult.getCode());
    }

    @Test
    void putPointSetting_정상_관리자지급차감() throws Exception {
        //given
    	PointSettingMgmRequestDto dto = new PointSettingMgmRequestDto();

    	dto.setApprovalCheck("N");
    	dto.setPointType("POINT_TYPE.AUTO");
        dto.setPointName("JUNIT TEST UPDATE");
        dto.setIssueValue("500");
        dto.setValidityDay("3");
        dto.setValidityType("VALIDITY_TYPE.VALIDITY");
        dto.setPointPaymentDetailType("POINT_ADMIN_ISSUE_TP.EVENT_AWARD");
        dto.setPointPaymentType("POINT_PAYMENT_TP.DEDUCTION");
        dto.setIssueReasonType("PROVISION_DEDUCTION_REASON.ORDER_USE");
        dto.setIssueReason("TEST");
        dto.setUrUserId("1");
        dto.setErpOrganizationCode("23014");
        dto.setUploadUser("");
        dto.setUploadTicket("");
        dto.setPmPointId("40");

        //when, then
        ApiResult<?> apiResult = promotionPointService.putPointSetting(dto);

        assertTrue(BaseEnums.Default.SUCCESS.getCode() == apiResult.getCode());
    }

    @Test
    void putPointStatus_승인요청() throws Exception {
    	 //given
    	PointSettingMgmRequestDto dto = new PointSettingMgmRequestDto();

    	dto.setPmPointId("1");
    	dto.setStatus("POINT_APPROVAL_STAT.REQUEST_APPROVAL");
    	dto.setUrUserId("1");
    	dto.setPointType(PointEnums.PointType.ADMIN.getCode());
    	dto.setStatusContent("1999");
    	dto.setPointName("1999");

        //when, then
        ApiResult<?> apiResult = promotionPointService.putPointStatus(dto);

        assertTrue(BaseEnums.Default.SUCCESS.getCode() == apiResult.getCode());
    }

    @Test
    void putPointStatus_승인() throws Exception {
    	 //given
    	PointSettingMgmRequestDto dto = new PointSettingMgmRequestDto();

    	dto.setPmPointId("1");
    	dto.setStatus("POINT_APPROVAL_STAT.ACCEPT_APPROVAL");
    	dto.setPointType("POINT_TYPE.SERIAL_NUMBER");
    	dto.setUrUserId("1");
    	dto.setStatusContent("1999");
    	dto.setPointName("1999");

        //when, then
    	 ApiResult<?> apiResult = promotionPointService.putPointStatus(dto);

         assertTrue(BaseEnums.Default.SUCCESS.getCode() == apiResult.getCode());
    }


    @Test
    void getPointUsable_정상() throws Exception {
        //given
        Long urUserId = 1646893L;

        //when
        int result = promotionPointService.getPointUsable(urUserId);

        //then
        assertTrue(result > 0);
    }

    @Test
    void getPointUsable_조회내역없음() throws Exception {
        //given
        Long urUserId = null;

        //when
        int result = promotionPointService.getPointUsable(urUserId);

        //then
        assertEquals(0, result);
    }

    @Test
    void getPointExpectExpired_정상() throws Exception {
        //given
        Long urUserId = 1646893L;
        String startDate = "2020-09-01";
        String endDate = "2021-12-31";

        //when
        String result = promotionPointService.getPointExpectExpired(urUserId, startDate, endDate);

        //then
        assertTrue(result.length() > 0);
    }

    @Test
    void getPointExpectExpired_조회내역없음() throws Exception {
        //given
        Long urUserId = null;
        String startDate = "2020-09-01";
        String endDate = "2020-10-01";

        //when
        String result = promotionPointService.getPointExpectExpired(urUserId, startDate, endDate);

        //then
        assertTrue(Objects.isNull(result));
    }

    @Test
    void getPointListByUser_정상() throws Exception {
        //given
        CommonGetPointListByUserRequestDto dto = new CommonGetPointListByUserRequestDto();
        dto.setUrUserId(1646893L);
        dto.setStartDate("2020-09-01");
        dto.setEndDate("2020-09-30");
        dto.setPage(0);
        dto.setLimit(20);

        //when
        CommonGetPointListByUserResponseDto result = promotionPointService.getPointListByUser(dto);

        //then
        assertTrue(result.getRows().size() > 0);
    }

    @Test
    void getPointListByUser_조회내역없음() throws Exception {
        //given
        CommonGetPointListByUserRequestDto dto = new CommonGetPointListByUserRequestDto();
        dto.setUrUserId(null);
        dto.setStartDate("2020-09-01");
        dto.setEndDate("2020-09-30");
        dto.setPage(0);
        dto.setLimit(20);

        //when
        CommonGetPointListByUserResponseDto result = promotionPointService.getPointListByUser(dto);

        //then
        assertTrue(result.getRows().size() == 0);
    }

    @Test
    void getPointExpectExpireList_정상() throws Exception {
        //given
        Long urUserId = 1646893L;
        String startDate = "2020-09-01";
        String endDate = "2021-12-31";

        //when
        List<CommonGetPointExpectExpireListResultVo> result = promotionPointService.getPointExpectExpireList(urUserId, startDate, endDate);

        //then
        assertTrue(result.size() > 0);
    }

    @Test
    void getPointExpectExpireList_조회내역없음() throws Exception {
        //given
        Long urUserId = null;
        String startDate = "2020-09-01";
        String endDate = "2020-09-30";

        //when
        List<CommonGetPointExpectExpireListResultVo> result = promotionPointService.getPointExpectExpireList(urUserId, startDate, endDate);

        //then
        assertTrue(result.size() == 0);
    }

    @Test
    void checkAddPointValidationByUser_정상() throws Exception {
        //given
        CommonGetAddPointValidationInfoVo returnVo = new CommonGetAddPointValidationInfoVo();
        returnVo.setPointMasterStat(PointEnums.PointMasterStatus.APPROVED.getCode());
        returnVo.setIssueQty(10);
        returnVo.setIssueCnt(6);
        returnVo.setIssueQtyLimit(1);
        returnVo.setUserIssueCnt(0);
        returnVo.setIssueStartDate(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        returnVo.setIssueEndDate(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));

        given(mockPromotionPointMapper.getAddPointValidationInfo(any(), any())).willReturn(returnVo);

        //when
        CommonCheckAddPointValidationByUserResponseDto result = mockPromotionPointService.checkAddPointValidationByUser(1L, 1L);

        //then
        assertEquals(PointEnums.AddPointValidation.PASS_VALIDATION, result.getValidationEnum());
    }

    @Test
    void checkAddPointValidationByUser_승인상태아님() throws Exception {
        CommonGetAddPointValidationInfoVo returnVo = new CommonGetAddPointValidationInfoVo();
        returnVo.setPointApprStat(PointEnums.PointApprovalStatus.DENIED.getCode());
        given(mockPromotionPointMapper.getAddPointValidationInfo(any(), any())).willReturn(returnVo);

        //when
        CommonCheckAddPointValidationByUserResponseDto result = mockPromotionPointService.checkAddPointValidationByUser(1L, 1L);

        //then
        assertEquals(PointEnums.AddPointValidation.NOT_ACCEPT_APPROVAL, result.getValidationEnum());
    }

    @Test
    void checkAddPointValidationByUser_발급수량_초과() throws Exception {
        CommonGetAddPointValidationInfoVo returnVo = new CommonGetAddPointValidationInfoVo();
        returnVo.setPointMasterStat(PointEnums.PointMasterStatus.APPROVED.getCode());
        returnVo.setIssueQty(10);
        returnVo.setIssueCnt(10);
        given(mockPromotionPointMapper.getAddPointValidationInfo(any(), any())).willReturn(returnVo);

        //when
        CommonCheckAddPointValidationByUserResponseDto result = mockPromotionPointService.checkAddPointValidationByUser(1L, 1L);

        //then
        assertEquals(PointEnums.AddPointValidation.OVER_ISSUE_QTY, result.getValidationEnum());
    }

    @Test
    void checkAddPointValidationByUser_1인발급제한수량_초과() throws Exception {
        CommonGetAddPointValidationInfoVo returnVo = new CommonGetAddPointValidationInfoVo();
        returnVo.setPointMasterStat(PointEnums.PointMasterStatus.APPROVED.getCode());
        returnVo.setIssueQty(10);
        returnVo.setIssueCnt(6);
        returnVo.setIssueQtyLimit(1);
        returnVo.setUserIssueCnt(1);
        given(mockPromotionPointMapper.getAddPointValidationInfo(any(), any())).willReturn(returnVo);

        //when
        CommonCheckAddPointValidationByUserResponseDto result = mockPromotionPointService.checkAddPointValidationByUser(1L, 1L);

        //then
        assertEquals(PointEnums.AddPointValidation.OVER_ISSUE_QTY_LIMIT, result.getValidationEnum());
    }

    @Test
    void checkAddPointValidationByUser_발급시작일_이전() throws Exception {
        CommonGetAddPointValidationInfoVo returnVo = new CommonGetAddPointValidationInfoVo();
        returnVo.setPointMasterStat(PointEnums.PointMasterStatus.APPROVED.getCode());
        returnVo.setIssueQty(10);
        returnVo.setIssueCnt(6);
        returnVo.setIssueQtyLimit(1);
        returnVo.setUserIssueCnt(0);
        returnVo.setIssueStartDate("2099-01-01");
        returnVo.setIssueEndDate("2099-12-01");
        given(mockPromotionPointMapper.getAddPointValidationInfo(any(), any())).willReturn(returnVo);

        //when
        CommonCheckAddPointValidationByUserResponseDto result = mockPromotionPointService.checkAddPointValidationByUser(1L, 1L);

        //then
        assertEquals(PointEnums.AddPointValidation.NOT_ISSUE_DATE, result.getValidationEnum());
    }

    @Test
    void checkAddPointValidationByUser_발급종료일_지남() throws Exception {
        CommonGetAddPointValidationInfoVo returnVo = new CommonGetAddPointValidationInfoVo();
        returnVo.setPointMasterStat(PointEnums.PointMasterStatus.APPROVED.getCode());
        returnVo.setIssueQty(10);
        returnVo.setIssueCnt(6);
        returnVo.setIssueQtyLimit(1);
        returnVo.setUserIssueCnt(0);
        returnVo.setIssueStartDate("1999-01-01");
        returnVo.setIssueEndDate("1999-12-01");
        given(mockPromotionPointMapper.getAddPointValidationInfo(any(), any())).willReturn(returnVo);

        //when
        CommonCheckAddPointValidationByUserResponseDto result = mockPromotionPointService.checkAddPointValidationByUser(1L, 1L);

        //then
        assertEquals(PointEnums.AddPointValidation.OVER_ISSUE_DATE, result.getValidationEnum());
    }

    @Test
    void checkAddPointValidationByUser_1인발급제한수량_무제한() throws Exception {
        CommonGetAddPointValidationInfoVo returnVo = new CommonGetAddPointValidationInfoVo();
        returnVo.setPointMasterStat(PointEnums.PointMasterStatus.APPROVED.getCode());
        returnVo.setIssueQty(10);
        returnVo.setIssueCnt(6);
        returnVo.setIssueQtyLimit(0);
        returnVo.setUserIssueCnt(1);
        returnVo.setIssueStartDate(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        returnVo.setIssueEndDate(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));

        given(mockPromotionPointMapper.getAddPointValidationInfo(any(), any())).willReturn(returnVo);

        //when
        CommonCheckAddPointValidationByUserResponseDto result = mockPromotionPointService.checkAddPointValidationByUser(1L, 1L);

        //then
        assertEquals(PointEnums.AddPointValidation.PASS_VALIDATION, result.getValidationEnum());
    }

    @Test
    void getPointExpectExpiredForEmail_성공() throws Exception{
    	PointExpiredForEmailVo returnVo = new PointExpiredForEmailVo();
    	returnVo.setUrUserId(1L);

    	given(mockPromotionPointMapper.getPointExpectExpiredForEmail(any())).willReturn(returnVo);

        //when
    	PointExpiredForEmailVo result = mockPromotionPointService.getPointExpectExpiredForEmail(1L);

        //then
        assertEquals(result.getUrUserId(),1L);
    }

    @Test
    void getPointExpectExpireListForEmail_성공() throws Exception{
    	List<PointExpiredListForEmailVo> returnList = new ArrayList<>();
    	PointExpiredListForEmailVo vo = new PointExpiredListForEmailVo();
    	vo.setUrUserId(1L);
    	returnList.add(vo);

    	given(mockPromotionPointMapper.getPointExpectExpireListForEmail(any())).willReturn(returnList);

        //when
    	List<PointExpiredListForEmailVo> result = mockPromotionPointService.getPointExpectExpireListForEmail(1L);

        //then
        assertTrue(result.size()>0);
    }


    @Test
    void getCallEventPointInfo_정상() throws Exception {

    	PointRequestDto dto = new PointRequestDto();
        dto.setUseYn("Y");
        dto.setPointType("POINT_TYPE.AUTO");
        dto.setPointDetailType("POINT_AUTO_ISSUE_TP.EVENT");

        ApiResult<?> apiResult = mockPromotionPointService.getEventCallPointInfo(dto);

        assertTrue(BaseEnums.Default.SUCCESS.getCode() == apiResult.getCode());


    }


    @Test
    void updatePointName_정상() throws Exception {

        UserVo userVO = new UserVo();
        userVO.setUserId("1");
        userVO.setLoginId("forbiz");
        userVO.setLangCode("1");
        SessionUtil.setUserVO(userVO);
        PointSettingMgmRequestDto dto = new PointSettingMgmRequestDto();
        dto.setPmPointId("-1");
        dto.setPointName("UPDATE 테스트 수정");
        mockPromotionPointService.updatePointName(dto);
    }

    @Test
    void determinAddPointApprovalStatus_자동승인경우() {
        PointSettingMgmRequestDto pointSettingMgmRequestDto = new PointSettingMgmRequestDto();
        pointSettingMgmRequestDto.setPointType(PointEnums.PointType.ADMIN.getCode());
        pointSettingMgmRequestDto.setIssueValue("10000");
        pointSettingMgmRequestDto.setApprovalCheck("Y");
        PointSettingMgmRequestDto resultY = mockPromotionPointService.determinAddPointApprovalStatus(pointSettingMgmRequestDto);

        assertEquals(ApprovalEnums.ApprovalStatus.APPROVED_BY_SYSTEM.getCode(), resultY.getApprStat());

        pointSettingMgmRequestDto.setIssueValue("10000");
        pointSettingMgmRequestDto.setApprovalCheck("N");
        PointSettingMgmRequestDto resultN = mockPromotionPointService.determinAddPointApprovalStatus(pointSettingMgmRequestDto);

        assertEquals(ApprovalEnums.ApprovalStatus.APPROVED_BY_SYSTEM.getCode(), resultN.getApprStat());
    }

    @Test
    void determinAddPointApprovalStatus_자동승인아닌경우() throws Exception {
        PointSettingMgmRequestDto pointSettingMgmRequestDto = new PointSettingMgmRequestDto();
        pointSettingMgmRequestDto.setIssueValue("10001");
        pointSettingMgmRequestDto.setApprovalCheck("Y");
        PointSettingMgmRequestDto resultY = mockPromotionPointService.determinAddPointApprovalStatus(pointSettingMgmRequestDto);

        assertTrue(ApprovalEnums.ApprovalStatus.REQUEST.getCode().equals(resultY.getApprStat()));

        pointSettingMgmRequestDto.setIssueValue("10001");
        pointSettingMgmRequestDto.setApprovalCheck("N");
        PointSettingMgmRequestDto resultN = mockPromotionPointService.determinAddPointApprovalStatus(pointSettingMgmRequestDto);

        assertTrue(ApprovalEnums.ApprovalStatus.NONE.getCode().equals(resultY.getApprStat()));
    }



    @Test
    void getDuplicateFixedNumber_성공() throws Exception{
    	PointSettingMgmRequestDto dto = new PointSettingMgmRequestDto();
    	dto.setFixSerialNumber("abc123");

        //when
    	int selectCount = promotionPointService.getDuplicateFixedNumber(dto);

        //then
        assertTrue(selectCount>0);
    }


    @Test
    void getGoodsFeedbackPointRewardSettingList_조회_성공() {
        //given
        Long urGroupId = 37L;

        //when
        List<GoodsFeedbackPointRewardSettingVo> result = promotionPointService.getGoodsFeedbackPointRewardSettingList(urGroupId);

        //then
        assertTrue(result.size() > 0);
    }

}