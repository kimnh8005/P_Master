package kr.co.pulmuone.v1.promotion.serialnumber.service;

import kr.co.pulmuone.v1.comm.CommonServiceTestBaseForJunit5;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.enums.CouponEnums;
import kr.co.pulmuone.v1.comm.enums.PointEnums;
import kr.co.pulmuone.v1.comm.enums.SerialEnums;
import kr.co.pulmuone.v1.comm.mapper.promotion.serialnumber.SerialNumberMapper;
import kr.co.pulmuone.v1.promotion.coupon.dto.CouponValidationByUserResponseDto;
import kr.co.pulmuone.v1.promotion.coupon.service.PromotionCouponBiz;
import kr.co.pulmuone.v1.promotion.point.dto.CommonCheckAddPointValidationByUserResponseDto;
import kr.co.pulmuone.v1.promotion.point.service.PointBiz;
import kr.co.pulmuone.v1.promotion.point.service.PromotionPointBiz;
import kr.co.pulmuone.v1.promotion.serialnumber.dto.vo.CommonGetSerialNumberInfoVo;
import kr.co.pulmuone.v1.user.buyer.service.UserBuyerBiz;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

class SerialNumberServiceTest extends CommonServiceTestBaseForJunit5 {

    @Autowired
    SerialNumberService serialNumberService;

    @InjectMocks
    private SerialNumberService mockSerialNumberService;

    @Mock
    SerialNumberMapper mockSerialNumberMapper;

    @Mock
    PromotionPointBiz mockPromotionPointBiz;

    @Mock
    PromotionCouponBiz mockPromotionCouponBiz;

    @Mock
    UserBuyerBiz mockUserBuyerBiz;

    @Mock
    PointBiz mockPointBIz;

    @BeforeEach
    void setUp() {
        mockSerialNumberService = new SerialNumberService(mockSerialNumberMapper, mockPromotionPointBiz, mockPromotionCouponBiz, mockUserBuyerBiz, mockPointBIz);
    }

    @Test
    void addPromotionByUser_정상_적립금() throws Exception {
        //given
        CommonGetSerialNumberInfoVo serialNumberInfoVo = new CommonGetSerialNumberInfoVo();
        serialNumberInfoVo.setUseSerial(0);
        serialNumberInfoVo.setCouponSerial(null);
        serialNumberInfoVo.setPointSerial(1L);
        serialNumberInfoVo.setCouponFixSerial(null);
        serialNumberInfoVo.setPointFixSerial(null);
        serialNumberInfoVo.setPmSerialNumberId(null);
        given(mockSerialNumberMapper.getSerialNumberInfo(any())).willReturn(serialNumberInfoVo);

        CommonCheckAddPointValidationByUserResponseDto pointDto = new CommonCheckAddPointValidationByUserResponseDto();
        pointDto.setValidationEnum(PointEnums.AddPointValidation.PASS_VALIDATION);
        given(mockPromotionPointBiz.checkPointValidationByUser(any(), any())).willReturn(pointDto);

        ApiResult pointBizResponse = ApiResult.success(true);
        given(mockPointBIz.depositPointsBySerialNumber(any(), any())).willReturn(pointBizResponse);

        //when
        ApiResult<?> result = mockSerialNumberService.addPromotionByUser("HAPPY", 1L);

        //then
        Assertions.assertEquals(SerialEnums.AddPromotion.SUCCESS_ADD_POINT.getCode(), result.getCode());
    }

    @Test
    void addPromotionByUser_정상_쿠폰() throws Exception {
        //given
        CommonGetSerialNumberInfoVo serialNumberInfoVo = new CommonGetSerialNumberInfoVo();
        serialNumberInfoVo.setUseSerial(0);
        serialNumberInfoVo.setCouponSerial(1L);
        serialNumberInfoVo.setPointSerial(null);
        serialNumberInfoVo.setCouponFixSerial(null);
        serialNumberInfoVo.setPointFixSerial(null);
        serialNumberInfoVo.setPmSerialNumberId(null);
        given(mockSerialNumberMapper.getSerialNumberInfo(any())).willReturn(serialNumberInfoVo);

        CouponValidationByUserResponseDto couponDto = new CouponValidationByUserResponseDto();
        couponDto.setValidationEnum(CouponEnums.AddCouponValidation.PASS_VALIDATION);

        given(mockPromotionCouponBiz.checkCouponValidationByUser(any(), any())).willReturn(couponDto);

        //when
        ApiResult<?> result = mockSerialNumberService.addPromotionByUser("HAPPY", 1L);

        //then
        Assertions.assertEquals(SerialEnums.AddPromotion.SUCCESS_ADD_COUPON.getCode(), result.getCode());
    }

    @Test
    void addPromotionByUser_등록가능한이용권없음() throws Exception {
        //given
        CommonGetSerialNumberInfoVo serialNumberInfoVo = new CommonGetSerialNumberInfoVo();
        serialNumberInfoVo.setUseSerial(0);
        serialNumberInfoVo.setCouponSerial(null);
        serialNumberInfoVo.setPointSerial(null);
        serialNumberInfoVo.setCouponFixSerial(null);
        serialNumberInfoVo.setPointFixSerial(null);
        serialNumberInfoVo.setPmSerialNumberId(null);

        given(mockSerialNumberMapper.getSerialNumberInfo(any())).willReturn(serialNumberInfoVo);

        //when
        ApiResult<?> result = mockSerialNumberService.addPromotionByUser("HAPPY", 1L);

        //then
        Assertions.assertEquals(SerialEnums.AddPromotion.NOT_FIND_SERIAL_NUMBER.getCode(), result.getCode());
    }

    @Test
    void addPromotionByUser_사용한이용권() throws Exception {
        //given
        CommonGetSerialNumberInfoVo serialNumberInfoVo = new CommonGetSerialNumberInfoVo();
        serialNumberInfoVo.setUseSerial(1);
        serialNumberInfoVo.setCouponSerial(null);
        serialNumberInfoVo.setPointSerial(null);
        serialNumberInfoVo.setCouponFixSerial(null);
        serialNumberInfoVo.setPointFixSerial(null);
        serialNumberInfoVo.setPmSerialNumberId(null);

        given(mockSerialNumberMapper.getSerialNumberInfo(any())).willReturn(serialNumberInfoVo);

        //when
        ApiResult<?> result = mockSerialNumberService.addPromotionByUser("HAPPY", 1L);

        //then
        Assertions.assertEquals(SerialEnums.AddPromotion.USE_SERIAL_NUMBER.getCode(), result.getCode());
    }

    @Test
    void addPromotionByUser_적립금_사용기간지남() throws Exception {
        //given
        CommonGetSerialNumberInfoVo serialNumberInfoVo = new CommonGetSerialNumberInfoVo();
        serialNumberInfoVo.setUseSerial(0);
        serialNumberInfoVo.setCouponSerial(null);
        serialNumberInfoVo.setPointSerial(1L);
        serialNumberInfoVo.setCouponFixSerial(null);
        serialNumberInfoVo.setPointFixSerial(null);
        serialNumberInfoVo.setPmSerialNumberId(null);
        given(mockSerialNumberMapper.getSerialNumberInfo(any())).willReturn(serialNumberInfoVo);

        CommonCheckAddPointValidationByUserResponseDto pointDto = new CommonCheckAddPointValidationByUserResponseDto();
        pointDto.setValidationEnum(PointEnums.AddPointValidation.OVER_ISSUE_DATE);

        given(mockPromotionPointBiz.checkPointValidationByUser(any(), any())).willReturn(pointDto);

        //when
        ApiResult<?> result = mockSerialNumberService.addPromotionByUser("HAPPY", 1L);

        //then
        Assertions.assertEquals(SerialEnums.AddPromotion.OVER_ISSUE_DATE.getCode(), result.getCode());
    }

    @Test
    void addPromotionByUser_적립금_수량제한() throws Exception {
        //given
        CommonGetSerialNumberInfoVo serialNumberInfoVo = new CommonGetSerialNumberInfoVo();
        serialNumberInfoVo.setUseSerial(0);
        serialNumberInfoVo.setCouponSerial(null);
        serialNumberInfoVo.setPointSerial(1L);
        serialNumberInfoVo.setCouponFixSerial(null);
        serialNumberInfoVo.setPointFixSerial(null);
        serialNumberInfoVo.setPmSerialNumberId(null);
        given(mockSerialNumberMapper.getSerialNumberInfo(any())).willReturn(serialNumberInfoVo);

        CommonCheckAddPointValidationByUserResponseDto pointDto = new CommonCheckAddPointValidationByUserResponseDto();
        pointDto.setValidationEnum(PointEnums.AddPointValidation.OVER_ISSUE_QTY);

        given(mockPromotionPointBiz.checkPointValidationByUser(any(), any())).willReturn(pointDto);

        //when
        ApiResult<?> result = mockSerialNumberService.addPromotionByUser("HAPPY", 1L);

        //then
        Assertions.assertEquals(PointEnums.AddPointValidation.OVER_ISSUE_QTY.getCode(), result.getCode());
    }

    @Test
    void addPromotionByUser_적립금_사용기간이전() throws Exception {
        //given
        CommonGetSerialNumberInfoVo serialNumberInfoVo = new CommonGetSerialNumberInfoVo();
        serialNumberInfoVo.setUseSerial(0);
        serialNumberInfoVo.setCouponSerial(null);
        serialNumberInfoVo.setPointSerial(1L);
        serialNumberInfoVo.setCouponFixSerial(null);
        serialNumberInfoVo.setPointFixSerial(null);
        serialNumberInfoVo.setPmSerialNumberId(null);
        given(mockSerialNumberMapper.getSerialNumberInfo(any())).willReturn(serialNumberInfoVo);

        CommonCheckAddPointValidationByUserResponseDto pointDto = new CommonCheckAddPointValidationByUserResponseDto();
        pointDto.setValidationEnum(PointEnums.AddPointValidation.NOT_ISSUE_DATE);

        given(mockPromotionPointBiz.checkPointValidationByUser(any(), any())).willReturn(pointDto);

        //when
        ApiResult<?> result = mockSerialNumberService.addPromotionByUser("HAPPY", 1L);

        //then
        Assertions.assertEquals(PointEnums.AddPointValidation.NOT_ISSUE_DATE.getCode(), result.getCode());
    }

    @Test
    void addPromotionByUser_적립금_기타오류() throws Exception {
        //given
        CommonGetSerialNumberInfoVo serialNumberInfoVo = new CommonGetSerialNumberInfoVo();
        serialNumberInfoVo.setUseSerial(0);
        serialNumberInfoVo.setCouponSerial(null);
        serialNumberInfoVo.setPointSerial(1L);
        serialNumberInfoVo.setCouponFixSerial(null);
        serialNumberInfoVo.setPointFixSerial(null);
        serialNumberInfoVo.setPmSerialNumberId(null);
        given(mockSerialNumberMapper.getSerialNumberInfo(any())).willReturn(serialNumberInfoVo);

        CommonCheckAddPointValidationByUserResponseDto pointDto = new CommonCheckAddPointValidationByUserResponseDto();
        pointDto.setValidationEnum(PointEnums.AddPointValidation.ETC);

        given(mockPromotionPointBiz.checkPointValidationByUser(any(), any())).willReturn(pointDto);

        //when
        ApiResult<?> result = mockSerialNumberService.addPromotionByUser("HAPPY", 1L);

        //then
        Assertions.assertEquals(PointEnums.AddPointValidation.ETC.getCode(), result.getCode());
    }

    @Test
    void addPromotionByUser_쿠폰_사용기간지남() throws Exception {
        //given
        CommonGetSerialNumberInfoVo serialNumberInfoVo = new CommonGetSerialNumberInfoVo();
        serialNumberInfoVo.setUseSerial(0);
        serialNumberInfoVo.setCouponSerial(1L);
        serialNumberInfoVo.setPointSerial(null);
        serialNumberInfoVo.setCouponFixSerial(null);
        serialNumberInfoVo.setPointFixSerial(null);
        serialNumberInfoVo.setPmSerialNumberId(null);
        given(mockSerialNumberMapper.getSerialNumberInfo(any())).willReturn(serialNumberInfoVo);

        CouponValidationByUserResponseDto couponDto = new CouponValidationByUserResponseDto();
        couponDto.setValidationEnum(CouponEnums.AddCouponValidation.OVER_ISSUE_DATE);

        given(mockPromotionCouponBiz.checkCouponValidationByUser(any(), any())).willReturn(couponDto);

        //when
        ApiResult<?> result = mockSerialNumberService.addPromotionByUser("HAPPY", 1L);

        //then
        Assertions.assertEquals(SerialEnums.AddPromotion.OVER_ISSUE_DATE.getCode(), result.getCode());
    }

    @Test
    void addPromotionByUser_쿠폰_수량제한() throws Exception {
        //given
        CommonGetSerialNumberInfoVo serialNumberInfoVo = new CommonGetSerialNumberInfoVo();
        serialNumberInfoVo.setUseSerial(0);
        serialNumberInfoVo.setCouponSerial(1L);
        serialNumberInfoVo.setPointSerial(null);
        serialNumberInfoVo.setCouponFixSerial(null);
        serialNumberInfoVo.setPointFixSerial(null);
        serialNumberInfoVo.setPmSerialNumberId(null);
        given(mockSerialNumberMapper.getSerialNumberInfo(any())).willReturn(serialNumberInfoVo);

        CouponValidationByUserResponseDto couponDto = new CouponValidationByUserResponseDto();
        couponDto.setValidationEnum(CouponEnums.AddCouponValidation.OVER_ISSUE_QTY_LIMIT);

        given(mockPromotionCouponBiz.checkCouponValidationByUser(any(), any())).willReturn(couponDto);

        //when
        ApiResult<?> result = mockSerialNumberService.addPromotionByUser("HAPPY", 1L);

        //then
        Assertions.assertEquals(CouponEnums.AddCouponValidation.OVER_ISSUE_QTY_LIMIT.getCode(), result.getCode());
    }

    @Test
    void addPromotionByUser_쿠폰_사용기간이전() throws Exception {
        //given
        CommonGetSerialNumberInfoVo serialNumberInfoVo = new CommonGetSerialNumberInfoVo();
        serialNumberInfoVo.setUseSerial(0);
        serialNumberInfoVo.setCouponSerial(1L);
        serialNumberInfoVo.setPointSerial(null);
        serialNumberInfoVo.setCouponFixSerial(null);
        serialNumberInfoVo.setPointFixSerial(null);
        serialNumberInfoVo.setPmSerialNumberId(null);
        given(mockSerialNumberMapper.getSerialNumberInfo(any())).willReturn(serialNumberInfoVo);

        CouponValidationByUserResponseDto couponDto = new CouponValidationByUserResponseDto();
        couponDto.setValidationEnum(CouponEnums.AddCouponValidation.NOT_ISSUE_DATE);

        given(mockPromotionCouponBiz.checkCouponValidationByUser(any(), any())).willReturn(couponDto);

        //when
        ApiResult<?> result = mockSerialNumberService.addPromotionByUser("HAPPY", 1L);

        //then
        Assertions.assertEquals(CouponEnums.AddCouponValidation.NOT_ISSUE_DATE.getCode(), result.getCode());
    }

    @Test
    void addPromotionByUser_쿠폰_기타오류() throws Exception {
        //given
        CommonGetSerialNumberInfoVo serialNumberInfoVo = new CommonGetSerialNumberInfoVo();
        serialNumberInfoVo.setUseSerial(0);
        serialNumberInfoVo.setCouponSerial(1L);
        serialNumberInfoVo.setPointSerial(null);
        serialNumberInfoVo.setCouponFixSerial(null);
        serialNumberInfoVo.setPointFixSerial(null);
        serialNumberInfoVo.setPmSerialNumberId(null);
        given(mockSerialNumberMapper.getSerialNumberInfo(any())).willReturn(serialNumberInfoVo);

        CouponValidationByUserResponseDto couponDto = new CouponValidationByUserResponseDto();
        couponDto.setValidationEnum(CouponEnums.AddCouponValidation.ETC);
        given(mockPromotionCouponBiz.checkCouponValidationByUser(any(), any())).willReturn(couponDto);

        //when
        ApiResult<?> result = mockSerialNumberService.addPromotionByUser("HAPPY", 1L);

        //then
        Assertions.assertEquals(CouponEnums.AddCouponValidation.ETC.getCode(), result.getCode());
    }

}