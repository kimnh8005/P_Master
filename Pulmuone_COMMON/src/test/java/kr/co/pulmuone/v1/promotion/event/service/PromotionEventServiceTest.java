package kr.co.pulmuone.v1.promotion.event.service;

import kr.co.pulmuone.v1.comm.CommonServiceTestBaseForJunit5;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.enums.*;
import kr.co.pulmuone.v1.comm.mapper.promotion.event.PromotionEventMapper;
import kr.co.pulmuone.v1.policy.config.service.PolicyConfigBiz;
import kr.co.pulmuone.v1.promotion.coupon.service.PromotionCouponBiz;
import kr.co.pulmuone.v1.promotion.event.dto.*;
import kr.co.pulmuone.v1.promotion.event.dto.vo.*;
import kr.co.pulmuone.v1.promotion.point.service.PointBiz;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

class PromotionEventServiceTest extends CommonServiceTestBaseForJunit5 {
    @Autowired
    private PromotionEventService promotionEventService;

    @InjectMocks
    private PromotionEventService mockPromotionEventService;

    @Mock
    private PromotionEventMapper mockPromotionEventMapper;

    @Mock
    private PromotionCouponBiz mockPromotionCouponBiz;

    @Mock
    private PromotionEventConcurrencyBiz mockPromotionEventConcurrencyBiz;

    @Mock
    private PointBiz mockPointBiz;

    @Mock
    private PolicyConfigBiz mockPolicyConfigBiz;

    @BeforeEach
    void setUp() {
        mockPromotionEventService = new PromotionEventService(mockPromotionEventMapper, mockPromotionCouponBiz, mockPointBiz, mockPromotionEventConcurrencyBiz, mockPolicyConfigBiz);
    }

    @Test
    void getEventListByUser_조회_정상() throws Exception {
        //given
        EventListByUserRequestDto dto = new EventListByUserRequestDto();
        dto.setPage(1);
        dto.setLimit(20);

        //when
        EventListByUserResponseDto result = promotionEventService.getEventListByUser(dto);

        //then
        assertTrue(result.getTotal() > 0);
    }

    @Test
    void getNormalByUser_조회_정상() throws Exception {
        //given
        EventRequestDto dto = EventRequestDto.builder()
                .evEventId(1L)
                .urUserId(100L)
                .deviceType("PC")
                .build();

        //when
        ApiResult<?> result = promotionEventService.getNormalByUser(dto);

        //then
        assertEquals(BaseEnums.Default.SUCCESS.getCode(), result.getCode());
    }

    @Test
    void getNormalJoinValidation_조회_정상() throws Exception {
        //given
        Long evEventId = 1L;
        Long urUserId = 100L;

        //when
        NormalValidationVo result = promotionEventService.getNormalJoinValidation(evEventId, urUserId);

        //then
        assertTrue(result.getStartDate().length() > 0);
    }

    @Test
    void getNormalJoinValidation_조회_값없음() throws Exception {
        //given
        Long evEventId = null;
        Long urUserId = 100L;

        //when
        NormalValidationVo result = promotionEventService.getNormalJoinValidation(evEventId, urUserId);

        //then
        assertNull(result);
    }

    @Test
    void getNormalJoinValidation_조회_쿠폰() throws Exception {
        //given
        Long evEventId = 1L;
        Long urUserId = 1L;

        NormalValidationVo normalValidationVo = new NormalValidationVo();
        normalValidationVo.setEventBenefitType(EventEnums.EventBenefitType.COUPON.getCode());
        given(mockPromotionEventMapper.getNormalJoinValidation(any(), any())).willReturn(normalValidationVo);

        List<EventCouponVo> eventCouponVoList = new ArrayList<>();
        EventCouponVo eventCouponVo = new EventCouponVo();
        eventCouponVoList.add(eventCouponVo);
        given(mockPromotionEventMapper.getEventCoupon(any(), any())).willReturn(eventCouponVoList);

        //when
        NormalValidationVo result = mockPromotionEventService.getNormalJoinValidation(evEventId, urUserId);

        //then
        assertTrue(result.getCoupon().size() > 0);
    }

    @Test
    void eventValidation_오류_이벤트없음() throws Exception {
        //given, when
        MessageCommEnum result = promotionEventService.eventValidation(null, null);

        //then
        assertEquals(EventEnums.EventValidation.NO_EVENT, result);
    }

    @Test
    void eventValidation_오류_기간이전() throws Exception {
        //given
        EventValidationRequestDto dto = new EventValidationRequestDto();
        dto.setStartDate("2999-01-01");
        dto.setEndDate("2999-01-01");
        dto.setUrUserId(100L);
        String validationType = EventEnums.EventValidationType.GET.getCode();

        //when
        MessageCommEnum result = promotionEventService.eventValidation(dto, validationType);

        //then
        assertEquals(EventEnums.EventValidation.NOT_DATE, result);
    }

    @Test
    void eventValidation_오류_기간지남() throws Exception {
        //given
        EventValidationRequestDto dto = new EventValidationRequestDto();
        dto.setStartDate("1999-01-01");
        dto.setEndDate("1999-01-01");
        dto.setUrUserId(100L);
        dto.setEventJoinType(EventEnums.EventJoinType.DAY_1.getCode());
        dto.setUserJoinCount(0);
        dto.setEmployeeJoinYn("Y");
        dto.setDisplayWebPcYn("Y");
        dto.setDisplayWebMobileYn("Y");
        dto.setDisplayAppYn("Y");
        dto.setEvEmployeeType(EventEnums.EvEmployeeType.NO_LIMIT.getCode());
        dto.setUserGroupList(null);
        dto.setUserStatus(UserEnums.UserStatusType.MEMBER.getCode());
        dto.setDeviceType(GoodsEnums.DeviceType.PC.getCode());
        dto.setUrGroupId(8L);
        dto.setEventNotAvailableYn("N");
        dto.setEndYn("Y");

        String validationType = EventEnums.EventValidationType.ADD.getCode();

        //when
        MessageCommEnum result = promotionEventService.eventValidation(dto, validationType);

        //then
        assertEquals(EventEnums.EventValidation.NOT_DATE, result);
    }

    @Test
    void eventValidation_오류_임직원참여불가() throws Exception {
        //given
        EventValidationRequestDto dto = new EventValidationRequestDto();
        dto.setStartDate("1999-01-01");
        dto.setEndDate("2199-01-01");
        dto.setEventJoinType(EventEnums.EventJoinType.RANGE_1.getCode());
        dto.setUserJoinCount(0);
        dto.setEmployeeJoinYn("N");
        dto.setUserStatus(UserEnums.UserStatusType.EMPLOYEE.getCode());
        dto.setUrUserId(100L);
        dto.setEndYn("N");
        dto.setEvEmployeeType(EventEnums.EvEmployeeType.EMPLOYEE_EXCEPT.getCode());
        String validationType = EventEnums.EventValidationType.GET.getCode();

        //when
        MessageCommEnum result = promotionEventService.eventValidation(dto, validationType);

        //then
        assertEquals(EventEnums.EventValidation.NOT_EMPLOYEE, result);
    }

    @Test
    void eventValidation_오류_임직원전용() throws Exception {
        //given
        EventValidationRequestDto dto = new EventValidationRequestDto();
        dto.setStartDate("1999-01-01");
        dto.setEndDate("2199-01-01");
        dto.setEventJoinType(EventEnums.EventJoinType.RANGE_1.getCode());
        dto.setUserJoinCount(0);
        dto.setEmployeeJoinYn("Y");
        dto.setUserStatus(UserEnums.UserStatusType.MEMBER.getCode());
        dto.setUrUserId(100L);
        dto.setEvEmployeeType(EventEnums.EvEmployeeType.EMPLOYEE_ONLY.getCode());
        dto.setUserGroupList(null);
        String validationType = EventEnums.EventValidationType.GET.getCode();

        //when
        MessageCommEnum result = promotionEventService.eventValidation(dto, validationType);

        //then
        assertEquals(EventEnums.EventValidation.ONLY_EMPLOYEE, result);
    }

    @Test
    void eventValidation_오류_디바이스_PC() throws Exception {
        //given
        EventValidationRequestDto dto = new EventValidationRequestDto();
        dto.setStartDate("1999-01-01");
        dto.setEndDate("2199-01-01");
        dto.setEventJoinType(EventEnums.EventJoinType.RANGE_1.getCode());
        dto.setUserJoinCount(0);
        dto.setEmployeeJoinYn("Y");
        dto.setEvEmployeeType(EventEnums.EvEmployeeType.NO_LIMIT.getCode());
        dto.setUserGroupList(null);
        dto.setDisplayWebPcYn("N");
        dto.setUserStatus(UserEnums.UserStatusType.MEMBER.getCode());
        dto.setDeviceType(GoodsEnums.DeviceType.PC.getCode());
        dto.setUrUserId(100L);
        String validationType = EventEnums.EventValidationType.GET.getCode();

        //when
        MessageCommEnum result = promotionEventService.eventValidation(dto, validationType);

        //then
        assertEquals(EventEnums.EventValidation.NOT_DEVICE, result);
    }

    @Test
    void eventValidation_오류_등급() throws Exception {
        //given
        EventValidationRequestDto dto = new EventValidationRequestDto();
        dto.setStartDate("1999-01-01");
        dto.setEndDate("2199-01-01");
        dto.setEventJoinType(EventEnums.EventJoinType.RANGE_1.getCode());
        dto.setUserJoinCount(0);
        dto.setEmployeeJoinYn("Y");
        dto.setDisplayWebPcYn("Y");
        dto.setDisplayWebMobileYn("Y");
        dto.setDisplayAppYn("Y");
        dto.setEvEmployeeType(EventEnums.EvEmployeeType.NO_LIMIT.getCode());
        EventUserGroupByUserVo vo1 = new EventUserGroupByUserVo();
        vo1.setUrGroupId(1L);
        EventUserGroupByUserVo vo2 = new EventUserGroupByUserVo();
        vo2.setUrGroupId(2L);
        dto.setUserGroupList(Arrays.asList(vo1, vo2));
        dto.setUserStatus(UserEnums.UserStatusType.MEMBER.getCode());
        dto.setDeviceType(GoodsEnums.DeviceType.PC.getCode());
        dto.setUrGroupId(8L);
        dto.setUrUserId(100L);
        String validationType = EventEnums.EventValidationType.GET.getCode();

        //when
        MessageCommEnum result = promotionEventService.eventValidation(dto, validationType);

        //then
        assertEquals(EventEnums.EventValidation.NOT_GROUP, result);
    }

    @Test
    void eventValidation_오류_참여정보_1일1회() throws Exception {
        //given
        EventValidationRequestDto dto = new EventValidationRequestDto();
        dto.setStartDate("1999-01-01");
        dto.setEndDate("2199-01-01");
        dto.setEventJoinType(EventEnums.EventJoinType.DAY_1.getCode());
        dto.setUserJoinCount(1);
        dto.setEmployeeJoinYn("Y");
        dto.setDisplayWebPcYn("Y");
        dto.setDisplayWebMobileYn("Y");
        dto.setDisplayAppYn("Y");
        dto.setEvEmployeeType(EventEnums.EvEmployeeType.NO_LIMIT.getCode());
        dto.setUserGroupList(null);
        dto.setUserStatus(UserEnums.UserStatusType.MEMBER.getCode());
        dto.setDeviceType(GoodsEnums.DeviceType.PC.getCode());
        dto.setUrGroupId(8L);
        dto.setEventNotAvailableYn("N");
        dto.setEndYn("N");
        String validationType = EventEnums.EventValidationType.ADD.getCode();

        //when
        MessageCommEnum result = promotionEventService.eventValidation(dto, validationType);

        //then
        assertEquals(EventEnums.EventValidation.ALREADY_JOIN_DATE, result);
    }

    @Test
    void eventValidation_오류_참여정보_1이벤트1회() throws Exception {
        //given
        EventValidationRequestDto dto = new EventValidationRequestDto();
        dto.setStartDate("1999-01-01");
        dto.setEndDate("2199-01-01");
        dto.setEventJoinType(EventEnums.EventJoinType.RANGE_1.getCode());
        dto.setUserJoinCount(1);
        dto.setEmployeeJoinYn("Y");
        dto.setDisplayWebPcYn("Y");
        dto.setDisplayWebMobileYn("Y");
        dto.setDisplayAppYn("Y");
        dto.setEvEmployeeType(EventEnums.EvEmployeeType.NO_LIMIT.getCode());
        dto.setUserGroupList(null);
        dto.setUserStatus(UserEnums.UserStatusType.MEMBER.getCode());
        dto.setDeviceType(GoodsEnums.DeviceType.PC.getCode());
        dto.setUrGroupId(8L);
        dto.setEventNotAvailableYn("N");
        dto.setEndYn("N");
        String validationType = EventEnums.EventValidationType.ADD.getCode();

        //when
        MessageCommEnum result = promotionEventService.eventValidation(dto, validationType);

        //then
        assertEquals(EventEnums.EventValidation.ALREADY_JOIN_EVENT, result);
    }

    @Test
    void eventValidation_오류_이벤트참여제한회원() {
//        //given -- SPEC OUT
//        EventValidationRequestDto dto = new EventValidationRequestDto();
//        dto.setStartDate("1999-01-01");
//        dto.setEndDate("2199-01-01");
//        dto.setEventJoinType(EventEnums.EventJoinType.RANGE_1.getCode());
//        dto.setUserJoinCount(1);
//        dto.setEmployeeJoinYn("Y");
//        dto.setDisplayWebPcYn("Y");
//        dto.setDisplayWebMobileYn("Y");
//        dto.setDisplayAppYn("Y");
//        dto.setEvEmployeeType(EventEnums.EvEmployeeType.NO_LIMIT.getCode());
//        dto.setUserGroupList(null);
//        dto.setUserStatus(UserEnums.UserStatusType.MEMBER.getCode());
//        dto.setDeviceType(GoodsEnums.DeviceType.PC.getCode());
//        dto.setUrGroupId(8L);
//        dto.setEventNotAvailableYn("Y");
//        dto.setEndYn("N");
//        String validationType = EventEnums.EventValidationType.ADD.getCode();
//
//        //when
//        MessageCommEnum result = promotionEventService.eventValidation(dto, validationType);
//
//        //then
//        assertEquals(EventEnums.EventValidation.NOT_AVAILABLE, result);
    }

    @Test
    void eventValidation_정상() throws Exception {
        //given
        EventValidationRequestDto dto = new EventValidationRequestDto();
        dto.setStartDate("1999-01-01");
        dto.setEndDate("2199-01-01");
        dto.setEventJoinType(EventEnums.EventJoinType.RANGE_1.getCode());
        dto.setUserJoinCount(0);
        dto.setEmployeeJoinYn("Y");
        dto.setDisplayWebPcYn("Y");
        dto.setDisplayWebMobileYn("Y");
        dto.setDisplayAppYn("Y");
        dto.setEvEmployeeType(EventEnums.EvEmployeeType.NO_LIMIT.getCode());
        dto.setUserGroupList(null);
        dto.setUserStatus(UserEnums.UserStatusType.MEMBER.getCode());
        dto.setDeviceType(GoodsEnums.DeviceType.PC.getCode());
        dto.setUrGroupId(8L);
        dto.setEventNotAvailableYn("N");
        dto.setEndYn("N");
        String validationType = EventEnums.EventValidationType.ADD.getCode();

        //when
        MessageCommEnum result = promotionEventService.eventValidation(dto, validationType);

        //then
        assertEquals(BaseEnums.Default.SUCCESS, result);
    }

    @Test
    void addNormalJoinValidation_오류_댓글구분값() throws Exception {
        //given
        NormalValidationVo vo = new NormalValidationVo();
        NormalJoinRequestDto dto = new NormalJoinRequestDto();

        vo.setStartDate("1999-01-01");
        vo.setEndDate("2199-01-01");
        vo.setEventJoinType(EventEnums.EventJoinType.RANGE_1.getCode());
        vo.setUserJoinCount(0);
        vo.setNormalEventType(EventEnums.NormalEventType.COMMENT.getCode());
        vo.setCommentCodeYn("Y");

        dto.setComment("test");

        //when
        MessageCommEnum result = promotionEventService.addNormalJoinValidation(vo, dto);

        //then
        assertEquals(EventEnums.EventValidation.EMPTY_COMMENT_VALUE, result);
    }

    @Test
    void addNormalJoinValidation_성공() throws Exception {
        //given
        NormalValidationVo vo = new NormalValidationVo();
        NormalJoinRequestDto dto = new NormalJoinRequestDto();

        vo.setStartDate("1999-01-01");
        vo.setEndDate("2199-01-01");
        vo.setEventJoinType(EventEnums.EventJoinType.RANGE_1.getCode());
        vo.setUserJoinCount(0);
        vo.setNormalEventType(EventEnums.NormalEventType.COMMENT.getCode());
        vo.setCommentCodeYn("Y");

        dto.setCommentValue("test");
        dto.setComment("test");

        //when
        MessageCommEnum result = promotionEventService.addNormalJoinValidation(vo, dto);

        //then
        assertEquals(BaseEnums.Default.SUCCESS, result);
    }

    @Test
    void addEventJoin_저장_정상() throws Exception {
        //given
        EventJoinVo dto = EventJoinVo.builder()
                .evEventId(1L)
                .commentValue("test")
                .comment("test")
                .urUserId(0L)
                .build();

        //when, then
        promotionEventService.addEventJoin(dto);
    }

    @Test
    void addNormalBenefit_혜택제공_쿠폰() throws Exception {
        //given
        NormalValidationVo eventInfo = new NormalValidationVo();
        eventInfo.setEventDrawType(EventEnums.EventDrawType.AUTO.getCode());
        eventInfo.setEventBenefitType(EventEnums.EventBenefitType.COUPON.getCode());
        eventInfo.setAwardRate(100D);
        eventInfo.setExpectJoinUserCount(5000);

        List<EventCouponVo> couponList = new ArrayList<>();
        EventCouponVo couponVo = new EventCouponVo();
        couponVo.setEvEventCouponId(1L);
        couponVo.setPmCouponId(1L);
        couponVo.setDisplayCouponName("test");
        couponVo.setCouponCount(2);
        couponList.add(couponVo);
        eventInfo.setCoupon(couponList);

        NormalJoinRequestDto dto = new NormalJoinRequestDto();
        dto.setUrUserId(1L);

        CouponEnums.AddCouponValidation couponValidation = CouponEnums.AddCouponValidation.PASS_VALIDATION;
        given(mockPromotionCouponBiz.addCouponByOne(any(), any())).willReturn(couponValidation);

        //when
        BenefitResponseDto result = mockPromotionEventService.addNormalBenefit(eventInfo, dto);

        //then
        assertTrue(result.getCoupon().size() > 0);
    }

    @Test
    void getNormalJoinByUser_조회_정상() throws Exception {
        //given
        EventJoinByUserRequestDto dto = new EventJoinByUserRequestDto();
        dto.setEvEventId(1L);
        dto.setPage(1);
        dto.setLimit(20);

        //when
        EventJoinByUserResponseDto result = promotionEventService.getNormalJoinByUser(dto);

        //then
        assertTrue(result.getTotal() > 0);
    }

    @Test
    void getStampByUser_조회_정상() throws Exception {
        //given
        Long evEventId = 2L;
        Long urUserId = 1L;
        String deviceType = "";

        //when
        StampByUserVo result = promotionEventService.getStampByUser(evEventId, urUserId, deviceType);

        //then
        assertTrue(result.getTitle().length() > 0);
    }

    @Test
    void getStampValidation_조회_정상() throws Exception {
        //given
        Long evEventId = 2L;
        Long urUserId = 1L;

        //when
        StampValidationVo result = promotionEventService.getStampValidation(evEventId, urUserId);

        //then
        assertTrue(result.getStartDate().length() > 0);
    }

    @Test
    void addStampAttendBenefit_혜택없음() throws Exception {
        //given
        StampValidationVo eventInfo = new StampValidationVo();
        List<StampDetailValidation> stampList = new ArrayList<>();
        StampDetailValidation stamp = new StampDetailValidation();
        stamp.setStampCount(3);
        stamp.setEventBenefitType(EventEnums.EventBenefitType.GIFT.getCode());
        stamp.setBenefitName("경품노트북");
        stampList.add(stamp);
        eventInfo.setStamp(stampList);

        StampJoinRequestDto dto = new StampJoinRequestDto();

        //when
        BenefitResponseDto result = promotionEventService.addStampAttendBenefit(eventInfo, dto);

        //then
        assertEquals("N", result.getWinnerYn());
    }

    @Test
    void addStampAttendBenefit_혜택_선물() throws Exception {
        //given
        StampValidationVo eventInfo = new StampValidationVo();
        List<StampDetailValidation> stampList = new ArrayList<>();
        StampDetailValidation stamp = new StampDetailValidation();
        stamp.setStampCount(1);
        stamp.setEventBenefitType(EventEnums.EventBenefitType.GIFT.getCode());
        stamp.setBenefitName("경품노트북");
        stampList.add(stamp);
        eventInfo.setStamp(stampList);

        StampJoinRequestDto dto = new StampJoinRequestDto();
        dto.setStampCount(1);

        //when
        BenefitResponseDto result = promotionEventService.addStampAttendBenefit(eventInfo, dto);

        //then
        assertEquals("Y", result.getWinnerYn());
        assertEquals("경품노트북", result.getBenefitName());
    }

    @Test
    void addStampMissionBenefit_혜택_참가() throws Exception {
        //given
        StampValidationVo eventInfo = new StampValidationVo();
        List<StampDetailValidation> stampList = new ArrayList<>();
        StampDetailValidation stamp = new StampDetailValidation();
        stamp.setStampCount(3);
        stamp.setEventBenefitType(EventEnums.EventBenefitType.GIFT.getCode());
        stamp.setBenefitName("경품노트북");
        stampList.add(stamp);
        eventInfo.setStamp(stampList);
        eventInfo.setEventType(EventEnums.EventType.MISSION.getCode());

        StampJoinRequestDto dto = new StampJoinRequestDto();
        dto.setStampCount(1);
        dto.setUrlVisitYn("N");

        //when
        BenefitResponseDto result = promotionEventService.addStampMissionBenefit(eventInfo, dto);

        //then
        assertEquals(1, result.getStampCount());
    }

    @Test
    void addStampMissionBenefit_혜택_없음() throws Exception {
        //given
        StampValidationVo eventInfo = new StampValidationVo();
        List<StampDetailValidation> stampList = new ArrayList<>();
        StampDetailValidation stamp = new StampDetailValidation();
        stamp.setStampCount(3);
        stamp.setEventBenefitType(EventEnums.EventBenefitType.GIFT.getCode());
        stamp.setBenefitName("경품노트북");
        stampList.add(stamp);
        eventInfo.setStamp(stampList);
        eventInfo.setEventType(EventEnums.EventType.MISSION.getCode());

        StampJoinRequestDto dto = new StampJoinRequestDto();
        dto.setStampCount(1);
        dto.setUrlVisitYn("Y");

        //when
        BenefitResponseDto result = promotionEventService.addStampMissionBenefit(eventInfo, dto);

        //then
        assertEquals("N", result.getWinnerYn());
    }

    @Test
    void addStampMissionBenefit_혜택_선물() throws Exception {
        //given
        StampValidationVo eventInfo = new StampValidationVo();
        List<StampDetailValidation> stampList = new ArrayList<>();
        StampDetailValidation stamp = new StampDetailValidation();
        stamp.setStampCount(3);
        stamp.setEventBenefitType(EventEnums.EventBenefitType.GIFT.getCode());
        stamp.setBenefitName("경품노트북");
        stampList.add(stamp);
        eventInfo.setStamp(stampList);
        eventInfo.setEventType(EventEnums.EventType.MISSION.getCode());

        StampJoinRequestDto dto = new StampJoinRequestDto();
        dto.setStampCount(3);
        dto.setUrlVisitYn("Y");

        //when
        BenefitResponseDto result = promotionEventService.addStampMissionBenefit(eventInfo, dto);

        //then
        assertEquals("Y", result.getWinnerYn());
    }

    @Test
    void getRouletteByUser_조회_정상() throws Exception {
        //given
        Long evEventId = 3L;
        Long urUserId = 1L;
        String deviceType = "";

        //when
        RouletteByUserVo result = promotionEventService.getRouletteByUser(evEventId, urUserId, deviceType);

        //then
        assertTrue(result.getTitle().length() > 0);
    }

    @Test
    void getRouletteValidation_조회_정상() throws Exception {
        //given
        Long evEventId = 3L;
        Long urUserId = 1L;

        //when
        RouletteValidationVo result = promotionEventService.getRouletteValidation(evEventId, urUserId);

        //then
        assertTrue(result.getStartDate().length() > 0);
    }

    @Test
    void getSurveyByUser_조회_정상() throws Exception {
        //given
        Long evEventId = 5L;
        Long urUserId = 1L;
        String deviceType = "";

        //when
        SurveyByUserVo result = promotionEventService.getSurveyByUser(evEventId, urUserId, deviceType);

        //then
        assertTrue(result.getTitle().length() > 0);
    }

    @Test
    void getSurveyJoinValidation_조회_정상() throws Exception {
        //given
        Long evEventId = 5L;
        Long urUserId = 100L;

        //when
        SurveyValidationVo result = promotionEventService.getSurveyJoinValidation(evEventId, urUserId);

        //then
        assertTrue(result.getStartDate().length() > 0);
    }

    @Test
    void addSurveyBenefit_혜택제공_쿠폰() throws Exception {
        //given
        SurveyValidationVo eventInfo = new SurveyValidationVo();
        eventInfo.setEventDrawType(EventEnums.EventDrawType.AUTO.getCode());
        eventInfo.setEventBenefitType(EventEnums.EventBenefitType.COUPON.getCode());

        List<EventCouponVo> couponList = new ArrayList<>();
        EventCouponVo couponVo = new EventCouponVo();
        couponVo.setPmCouponId(1L);
        couponVo.setDisplayCouponName("test");
        couponVo.setCouponCount(2);
        couponList.add(couponVo);
        eventInfo.setCoupon(couponList);

        SurveyJoinRequestDto dto = new SurveyJoinRequestDto();
        dto.setUrUserId(1L);

        CouponEnums.AddCouponValidation couponValidation = CouponEnums.AddCouponValidation.PASS_VALIDATION;
        given(mockPromotionCouponBiz.addCouponByOne(any(), any())).willReturn(couponValidation);

        //when
        BenefitResponseDto result = mockPromotionEventService.addSurveyBenefit(eventInfo, dto);

        //then
        assertTrue(result.getCoupon().size() > 0);
    }

    @Test
    void getExperienceByUser_조회_정상() throws Exception {
        //given
        Long evEventId = 9L;
        Long urUserId = 1L;
        String deviceType = "";

        //when
        ExperienceByUserVo result = promotionEventService.getExperienceByUser(evEventId, urUserId, deviceType);

        //then
        assertTrue(result.getTitle().length() > 0);
    }

    @Test
    void getExperienceJoinByUser_조회_정상() throws Exception {
        //given
        EventJoinByUserRequestDto dto = new EventJoinByUserRequestDto();
        dto.setEvEventId(9L);
        dto.setPage(1);
        dto.setLimit(20);

        //when
        EventJoinByUserResponseDto result = promotionEventService.getExperienceJoinByUser(dto);

        //then
        assertTrue(result.getTotal() > 0);
    }

    @Test
    void getExperienceJoinValidation_조회_정상() throws Exception {
        //given
        Long evEventId = 9L;
        Long urUserId = 100L;

        //when
        ExperienceValidationVo result = promotionEventService.getExperienceJoinValidation(evEventId, urUserId);

        //then
        assertTrue(result.getStartDate().length() > 0);
    }

    @Test
    void addExperienceJoinValidation_오류_선착순마감() {
        //given
        ExperienceValidationVo vo = new ExperienceValidationVo();
        vo.setStartDate("1999-01-01");
        vo.setEndDate("2199-01-01");
        vo.setEventJoinType(EventEnums.EventJoinType.RANGE_1.getCode());
        vo.setUserJoinCount(0);
        vo.setEventDrawType(EventEnums.EventDrawType.FIRST_COME.getCode());
        vo.setRecruitCloseYn("Y");

        ExperienceJoinRequestDto dto = new ExperienceJoinRequestDto();
        dto.setCommentValue("test");
        dto.setComment("test");

        //when
        MessageCommEnum result = promotionEventService.addExperienceJoinValidation(vo, dto);

        //then
        assertEquals(EventEnums.EventValidation.FIRST_COME_CLOSE, result);
    }

    @Test
    void addExperienceJoinValidation_오류_댓글구분값() {
        //given
        ExperienceValidationVo vo = new ExperienceValidationVo();
        ExperienceJoinRequestDto dto = new ExperienceJoinRequestDto();

        vo.setStartDate("1999-01-01");
        vo.setEndDate("2199-01-01");
        vo.setEventJoinType(EventEnums.EventJoinType.RANGE_1.getCode());
        vo.setUserJoinCount(0);
        vo.setEventDrawType(EventEnums.EventDrawType.ADMIN.getCode());
        vo.setCommentCodeYn("Y");

        dto.setComment("test");

        //when
        MessageCommEnum result = promotionEventService.addExperienceJoinValidation(vo, dto);

        //then
        assertEquals(EventEnums.EventValidation.EMPTY_COMMENT_VALUE, result);
    }

    @Test
    void addExperienceJoinValidation_정상() {
        //given
        ExperienceValidationVo vo = new ExperienceValidationVo();
        vo.setStartDate("1999-01-01");
        vo.setEndDate("2199-01-01");
        vo.setEventJoinType(EventEnums.EventJoinType.RANGE_1.getCode());
        vo.setUserJoinCount(0);
        vo.setEventDrawType(EventEnums.EventDrawType.ADMIN.getCode());
        vo.setRecruitCloseYn("N");
        vo.setCommentCodeYn("Y");

        ExperienceJoinRequestDto dto = new ExperienceJoinRequestDto();
        dto.setCommentValue("test");
        dto.setComment("test");

        //when
        MessageCommEnum result = promotionEventService.addExperienceJoinValidation(vo, dto);

        //then
        assertEquals(BaseEnums.Default.SUCCESS, result);
    }

    @Test
    void putEventExperienceRecruitClose_저장() throws Exception {
        //given
        Long evEventId = 10L;

        //when, then
        promotionEventService.putEventExperienceRecruitClose(evEventId);
    }

    @Test
    void getEventListFromMyPage_조회_성공() throws Exception {
        //given
        EventListFromMyPageRequestDto dto = new EventListFromMyPageRequestDto();
        dto.setPage(1);
        dto.setLimit(20);
        dto.setStartDate("2020-01-01");
        dto.setEndDate("2020-12-30");
        dto.setUrUserId(1646893L);
        dto.setDeviceType(GoodsEnums.DeviceType.PC.getCode());

        //when
        EventListFromMyPageResponseDto result = promotionEventService.getEventListFromMyPage(dto);

        //then
        assertTrue(result.getTotal() > 0);
    }

    @Test
    void getEventCommentCodeByUser_조회_성공() throws Exception {
        //given
        Long evEventId = 1L;

        //when
        List<CommentCodeByUserVo> result = promotionEventService.getEventCommentCodeByUser(evEventId);

        //then
        assertTrue(result.size() > 0);
    }

    @Test
    void getStampDetailByUser_조회_성공() throws Exception {
        //given
        Long evEventStampId = 1L;

        //when
        List<StampDetailByUserVo> result = promotionEventService.getStampDetailByUser(evEventStampId);

        //then
        assertTrue(result.size() > 0);
    }

    @Test
    void getStampJoinByUser_조회_성공() throws Exception {
        //given
        Long evEventId = 2L;
        Long urUserId = 1L;

        //when
        List<Integer> result = promotionEventService.getStampJoinByUser(evEventId, urUserId);

        //then
        assertTrue(result.size() > 0);
    }

    @Test
    void putStampJoin_수정_성공() throws Exception {
        //given
        StampJoinRequestDto dto = new StampJoinRequestDto();
        dto.setEvEventJoinId(1L);
        dto.setStampCount(1);

        //when, then
        promotionEventService.putStampJoin(dto);
    }

    @Test
    void getRouletteDetailByUser_조회_성공() throws Exception {
        //given
        Long evEventId = 1L;
        Long evEventRouletteId = 1L;

        //when
        List<RouletteItemByUserVo> result = promotionEventService.getRouletteDetailByUser(evEventId, evEventRouletteId);

        //then
        assertTrue(result.size() > 0);
    }

    @Test
    void addRouletteBenefit_혜택_제공() throws Exception {
        //given
        RouletteValidationVo eventInfo = new RouletteValidationVo();
        List<RouletteItemValidation> rouletteItemValidationList = new ArrayList<>();
        RouletteItemValidation rouletteItemValidation = new RouletteItemValidation();
        rouletteItemValidation.setAwardMaxCount(50);
        rouletteItemValidation.setWinnerCount(0);
        rouletteItemValidation.setAwardRate(100D);
        rouletteItemValidation.setEventBenefitType("test");
        rouletteItemValidation.setPmPointId(1L);
        rouletteItemValidation.setBenefitName("test");
        rouletteItemValidationList.add(rouletteItemValidation);
        eventInfo.setItem(rouletteItemValidationList);
        eventInfo.setEventNotAvailableYn("N");

        RouletteJoinRequestDto dto = new RouletteJoinRequestDto();
        dto.setUrUserId(1L);
        dto.setEvEventId(513L);

        //when
        BenefitResponseDto result = promotionEventService.processRouletteBenefit(eventInfo, dto);

        //then
        assertEquals("Y", result.getWinnerYn());
    }


    @Test
    void getSurveyQuestionByUser_조회_성공() throws Exception {
        //given
        Long evEventId = 5L;

        //when
        List<SurveyQuestionByUserVo> result = promotionEventService.getSurveyQuestionByUser(evEventId);

        //then
        assertTrue(result.size() > 0);
    }

    @Test
    void getSurveyItemByUser_조회_성공() throws Exception {
        //given
        Long evEventSurveyQuestionId = 1L;

        //when
        List<SurveyItemByUserVo> result = promotionEventService.getSurveyItemByUser(evEventSurveyQuestionId);

        //then
        assertTrue(result.size() > 0);
    }

    @Test
    void getStampJoinValidationByUser_조회_성공() throws Exception {
        //given
        Long evEventId = 6L;
        Long urUserId = 1646893L;

        //when
        List<StampJoinByUserVo> result = promotionEventService.getStampJoinValidationByUser(evEventId, urUserId);

        //then
        assertTrue(result.size() > 0);
    }

    @Test
    void getMissionStampByUser_조회_성공() throws Exception {
        //given
        Long urUserId = 0L;
        String deviceType = GoodsEnums.DeviceType.PC.getCode();
        CodeCommEnum userStatus = UserEnums.UserStatusType.NONMEMBER;
        long userGroup = 0L;

        //when
        List<MissionStampByUserResponseDto> result = promotionEventService.getMissionStampByUser(urUserId, deviceType, userStatus, userGroup);

        //then
        assertTrue(result.size() > 0);
    }

    @Test
    void getEventTitle_조회_성공() throws Exception {
        //given
        Long evEventId = 1L;
        String deviceType = "PC";

        //when
        EventInfoFromMetaVo result = promotionEventService.getEventInfoFromMeta(evEventId, deviceType);

        //then
        assertNotNull(result);
    }

    @Test
    void eventRouletteValidation_잔여당첨인원없음() throws Exception {
        //given
        Long evEventId = 508L;

        //when
        MessageCommEnum result = promotionEventService.eventRouletteValidation(evEventId);

        //then
        assertEquals(EventEnums.EventValidation.FULL_BENEFIT, result);
    }

    @Test
    void eventRouletteValidation_조회_성공() throws Exception {
        //given
        Long evEventId = 515L;

        //when
        MessageCommEnum result = promotionEventService.eventRouletteValidation(evEventId);

        //then
        assertEquals(BaseEnums.Default.SUCCESS, result);
    }
}