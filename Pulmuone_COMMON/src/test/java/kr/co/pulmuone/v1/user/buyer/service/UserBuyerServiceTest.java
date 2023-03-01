package kr.co.pulmuone.v1.user.buyer.service;

import kr.co.pulmuone.v1.base.dto.GetCodeListRequestDto;
import kr.co.pulmuone.v1.comm.CommonServiceTestBaseForJunit5;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.constants.Constants;
import kr.co.pulmuone.v1.comm.enums.BaseEnums;
import kr.co.pulmuone.v1.comm.enums.UserEnums;
import kr.co.pulmuone.v1.comm.util.asis.AsisUserApiUtil;
import kr.co.pulmuone.v1.promotion.point.service.PointBiz;
import kr.co.pulmuone.v1.user.buyer.dto.*;
import kr.co.pulmuone.v1.user.buyer.dto.vo.BuyerFromMypageResultVo;
import kr.co.pulmuone.v1.user.buyer.dto.vo.CodeInfoVo;
import kr.co.pulmuone.v1.user.buyer.dto.vo.CommonGetShippingAddressListResultVo;
import kr.co.pulmuone.v1.user.buyer.dto.vo.GetShippingAddressListResultVo;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

class UserBuyerServiceTest extends CommonServiceTestBaseForJunit5 {
    @Autowired
    UserBuyerService userBuyerService;

    @InjectMocks
    private UserBuyerService mockUserBuyerService;

    @Mock
    private AsisUserApiUtil mockAsisUserApiUtil;

    @Mock
    private PointBiz mockPointBiz;

    @Value("${database.encryption.key}")
    private String               databaseEncryptionKey;

    @BeforeEach
    void beforeEach() {
        buyerLogin();
        mockUserBuyerService = new UserBuyerService(mockAsisUserApiUtil, mockPointBiz);
    }

    @Test
    void 환불계좌_삭제_정상() throws Exception {
        //given
        Long urRefundBankId = 138L;

        //when, then
        userBuyerService.delRefundBank(urRefundBankId);
    }

    @Test
    void getBuyerFromMypage_정상() throws Exception {
        //given
        Long urUserId = 1646893L;

        //when
        BuyerFromMypageResultVo result = userBuyerService.getBuyerFromMypage(urUserId);

        //then
        assertEquals(result.getLoginId(), "test2580");
    }

    @Test
    void getBuyerFromMypage_조회내역없음() throws Exception {
        //given
        Long urUserId = null;

        //when
        BuyerFromMypageResultVo result = userBuyerService.getBuyerFromMypage(urUserId);

        //then
        assertTrue(Objects.isNull(result));
    }

    @Test
    void putBuyerFromMypage_수정_정상() throws Exception {
        //given
        CommonPutBuyerFromMypageRequestDto dto = new CommonPutBuyerFromMypageRequestDto();
        dto.setUrUserId(1646893L);
        dto.setUserName("홍기순");
        dto.setMobile("01012345678");
        dto.setMail("test@test.co.kr");
        dto.setRecentlyViewYn("Y");
        dto.setMailYn("Y");
        dto.setSmsYn("Y");

        //when. then
        userBuyerService.putBuyerFromMypage(dto);
    }

    @Test
    void getPromotionRecaptchaFailCount() throws Exception {
        //given, when
        int result = userBuyerService.getPromotionRecaptchaFailCount();

        //then
        assertEquals(0, result);
    }

    @Test
    void putPromotionRecaptchaFailCount() throws Exception {
        //given
        int value = 2;

        //when
        userBuyerService.putPromotionRecaptchaFailCount(value);

        //then
        assertEquals(value, userBuyerService.getPromotionRecaptchaFailCount());
    }

    @Test
    void 계좌인증_실패() throws Exception {
        //given
        String bankCode = "BANK_CODE.003";
        String accountNumber = "012345678";
        String holderName = "홍길동";

        //when, then
        Assertions.assertFalse(userBuyerService.isValidationBankAccountNumber(bankCode, accountNumber, holderName));
    }

    @Test
    void getNewBuyerSpecialsCouponByUser_성공() throws Exception{
    	Long ilGoodsId = 90018135L;
        String urUserId = "1646893";
        String deviceInfo = "pc";
        boolean isApp = false;

        int newBuyerSpecialsCouponByUser = userBuyerService.getNewBuyerSpecialsCouponByUser(ilGoodsId, urUserId, deviceInfo, isApp);

        assertTrue(newBuyerSpecialsCouponByUser > 0);
    }

    @Test
    void getNewBuyerSpecialsCouponByUser_조회결과없음() throws Exception{
    	Long ilGoodsId = 1L;
        String urUserId = "100";
        String deviceInfo = "pc";
        boolean isApp = false;

        int newBuyerSpecialsCouponByUser = userBuyerService.getNewBuyerSpecialsCouponByUser(ilGoodsId, urUserId, deviceInfo, isApp);

        assertEquals(0, newBuyerSpecialsCouponByUser);
    }

    @Test
    void getCommonCode_성공() throws Exception{
        GetCodeListRequestDto getCodeListRequestDto = new GetCodeListRequestDto();
        getCodeListRequestDto.setStCommonCodeMasterCode("ACCESS_INFORMATION");
        getCodeListRequestDto.setUseYn("Y");

        List<CodeInfoVo> commonGetCodeListResult = userBuyerService.getCommonCode(getCodeListRequestDto).getRows();

        assertTrue(commonGetCodeListResult.size() > 0);
    }

    @Test
    void getCommonCode_조회결과없음() throws Exception{
        GetCodeListRequestDto getCodeListRequestDto = new GetCodeListRequestDto();
        getCodeListRequestDto.setStCommonCodeMasterCode("ACCESS_TEST");
        getCodeListRequestDto.setUseYn("Y");

        List<CodeInfoVo> commonGetCodeListResult = userBuyerService.getCommonCode(getCodeListRequestDto).getRows();

        Assertions.assertFalse(commonGetCodeListResult.size() > 0);
    }

    @Test
    void getBasicShippingAddress_성공() throws Exception{
        String urUserId = "100";

        GetShippingAddressListResultVo basicShippingAddress = userBuyerService.getBasicShippingAddress(databaseEncryptionKey, urUserId);

        Assertions.assertNotNull(basicShippingAddress);
    }

    @Test
    void getBasicShippingAddress_조회결과없음() throws Exception{
        String urUserId = "10";

        GetShippingAddressListResultVo basicShippingAddress = userBuyerService.getBasicShippingAddress(databaseEncryptionKey, urUserId);

        Assertions.assertNull(basicShippingAddress);
    }


    @Test
    void 배송지삭제_정상() throws Exception {
        // given
        Long urShippingAddrId = 41L;
        // when
        userBuyerService.delShippingAddress(urShippingAddrId);
    }

    @Test
    void 기본배송지_설정_정상() throws Exception {
        // given
        Long urUserId = 1L;
        Long urShippingAddrId = 41L;
        // when
        userBuyerService.putShippingAddressSetDefault(urUserId, urShippingAddrId);
    }

    @Test
    void 배송지관리_목록조회_정상() throws Exception {
        // given
        ShippingAddressListFromMyPageRequestDto dto = new ShippingAddressListFromMyPageRequestDto();
        dto.setUrUserId(1L);
        dto.setPage(1);
        dto.setLimit(10);

        // when
        ShippingAddressListFromMyPageResponseDto result = userBuyerService.getShippingAddressListFromMyPage(dto);

        // then
        assertTrue(result.getRows().size() > 0);
    }

    @Test
    void 배송지관리_목록조회_조회내역없음() throws Exception {
        // given
        ShippingAddressListFromMyPageRequestDto dto = new ShippingAddressListFromMyPageRequestDto();
        dto.setUrUserId(999999999L);
        dto.setPage(1);
        dto.setLimit(10);

        // when
        ShippingAddressListFromMyPageResponseDto result = userBuyerService.getShippingAddressListFromMyPage(dto);

        // then
        assertEquals(result.getRows().size(), 0);
    }

    @Test
    void 배송지관련정보조회_목록조회_정상() throws Exception {
        // given

        GetCodeListRequestDto getCodeListRequestDto = new GetCodeListRequestDto();
        getCodeListRequestDto.setStCommonCodeMasterCode("ACCESS_INFORMATION");
        getCodeListRequestDto.setUseYn("Y");
        // when
        CommonGetCodeListResponseDto result = userBuyerService.getCommonCode(getCodeListRequestDto);

        // then
        assertTrue(result.getRows().size() > 0);

    }

    @Test
    void 배송지관련정보조회_목록조회_조회내역없음() throws Exception {
        // given

        GetCodeListRequestDto getCodeListRequestDto = new GetCodeListRequestDto();
        getCodeListRequestDto.setStCommonCodeMasterCode("9999999999999");
        getCodeListRequestDto.setUseYn("Y");
        // when
        CommonGetCodeListResponseDto result = userBuyerService.getCommonCode(getCodeListRequestDto);

        // then
        assertEquals(result.getRows().size(), 0);

    }

    // ---------------
    @Test
    void 은행_목록조회_정상() throws Exception {
        // given

        GetCodeListRequestDto getCodeListRequestDto = new GetCodeListRequestDto();
        getCodeListRequestDto.setStCommonCodeMasterCode("BANK_CODE");
        getCodeListRequestDto.setUseYn("Y");
        // when
        CommonGetCodeListResponseDto result = userBuyerService.getCommonCode(getCodeListRequestDto);

        // then
        assertTrue(result.getRows().size() > 0);

    }

    @Test
    void 은행_목록조회_조회내역없음() throws Exception {
        // given

        GetCodeListRequestDto getCodeListRequestDto = new GetCodeListRequestDto();
        getCodeListRequestDto.setStCommonCodeMasterCode("9999999999999");
        getCodeListRequestDto.setUseYn("Y");
        // when
        CommonGetCodeListResponseDto result = userBuyerService.getCommonCode(getCodeListRequestDto);

        // then
        assertEquals(result.getRows().size(), 0);

    }


    @Test
    void 마이페이지_배송리스_조회내역있음() throws Exception {
        // given
        long urUserId = 1646939;
        // when
        List<CommonGetShippingAddressListResultVo> result = userBuyerService.getMypageShippingAddressList(urUserId);

        // then
        assertTrue(result.size() > 0);

    }
    @Test
    void 마이페이지_배송리스트_조회내역없음() throws Exception {
        // given
        long urUserId = 0;
        // when
        List<CommonGetShippingAddressListResultVo> result = userBuyerService.getMypageShippingAddressList(urUserId);

        // then
        assertEquals(result.size(), 0);

    }

    @Test
    void validationDepositPointByAsIsPoint_기존포인트없음() {
        //given
        AsisUserPointRequestDto dto = new AsisUserPointRequestDto();
        dto.setPulmuonePoint(5);
        dto.setAsIsPulmuonePoint(0);

        //when
        ApiResult result = userBuyerService.validationDepositPointByAsIsPoint(dto);

        //then
        assertEquals(UserEnums.AsisPoint.ASIS_POINT_ZERO, result.getMessageEnum());
    }

    @Test
    void validationDepositPointByAsIsPoint_적립포인트초과() {
        //given
        AsisUserPointRequestDto dto = new AsisUserPointRequestDto();
        dto.setPulmuonePoint(5);
        dto.setAsIsPulmuonePoint(1);

        //when
        ApiResult result = userBuyerService.validationDepositPointByAsIsPoint(dto);

        //then
        assertEquals(UserEnums.AsisPoint.ASIS_OVER_POINT, result.getMessageEnum());
    }

    @Test
    void validationDepositPointByAsIsPoint_적립금제한금액() {
        //given
        AsisUserPointRequestDto dto = new AsisUserPointRequestDto();
        dto.setPulmuonePoint(5);
        dto.setAsIsPulmuonePoint(0);

        ApiResult pointUse = ApiResult.success(Constants.DEPOSIT_MAXIMUM_POSSIBLE_POINT);
        given(mockPointBiz.getUserAvailablePoints(any())).willReturn(pointUse);

        //when
        ApiResult result = userBuyerService.validationDepositPointByAsIsPoint(dto);

        //then
        assertEquals(UserEnums.AsisPoint.ASIS_POINT_ZERO, result.getMessageEnum());
    }

    @Test
    void validationDepositPointByAsIsPoint_성공() {
        //given
        AsisUserPointRequestDto dto = new AsisUserPointRequestDto();
        dto.setPulmuonePoint(5);
        dto.setAsIsPulmuonePoint(5);

        ApiResult pointBiz = ApiResult.success(0);
        given(mockPointBiz.getUserAvailablePoints(any())).willReturn(pointBiz);

        //when
        ApiResult result = mockUserBuyerService.validationDepositPointByAsIsPoint(dto);

        //then
        assertEquals(BaseEnums.Default.SUCCESS, result.getMessageEnum());
    }

}