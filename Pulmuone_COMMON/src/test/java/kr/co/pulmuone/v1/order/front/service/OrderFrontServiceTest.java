package kr.co.pulmuone.v1.order.front.service;

import kr.co.pulmuone.v1.comm.CommonServiceTestBaseForJunit5;
import kr.co.pulmuone.v1.comm.constants.CustomerConstants;
import kr.co.pulmuone.v1.comm.mapper.order.front.OrderFrontMapper;
import kr.co.pulmuone.v1.order.front.dto.OrderInfoFromEmployeeDiscountRequestDto;
import kr.co.pulmuone.v1.order.front.dto.OrderInfoFromMypageRewardRequestDto;
import kr.co.pulmuone.v1.order.front.dto.OrderInfoFromRewardRequestDto;
import kr.co.pulmuone.v1.order.front.dto.vo.*;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;

@Slf4j
class OrderFrontServiceTest extends CommonServiceTestBaseForJunit5 {

    @Autowired
    private OrderFrontService orderFrontService;

    @InjectMocks
    OrderFrontService mockOrderFrontService;

    @Mock
    OrderFrontMapper mockOrderFrontMapper;

    @BeforeEach
    void setUp() {
        mockOrderFrontService = new OrderFrontService(mockOrderFrontMapper);
    }

    @Test
    void getOrderInfoFromMyPageReward() {
        //given
        OrderInfoFromMypageRewardRequestDto dto = OrderInfoFromMypageRewardRequestDto.builder()
                .odOrderId(130757L)
                .build();

        //when
        List<OrderInfoFromMypageRewardVo> result = orderFrontService.getOrderInfoFromMyPageReward(dto);

        //then
        assertTrue(result.size() > 0);
    }

    @Test
    void getOrderCountFromUserDrop_조회_성공() throws Exception {
        //given
        Long urUserId = 1647338L;

        //when
        int result = orderFrontService.getOrderCountFromUserDrop(urUserId);

        //then
        assertTrue(result > 0);
    }

    @Test
    void getOrderCountFromUserDrop_조회_값없음() throws Exception {
        //given
        Long urUserId = 1646893999L;

        //when
        int result = orderFrontService.getOrderCountFromUserDrop(urUserId);

        //then
        assertEquals(result, 0);
    }

    @Test
    void getOrderCountFromMyPage_조회_성공() throws Exception {
        //given
        Long urUserId = 1647338L;
        String startDate = "2021-06-01";
        String endDate = "2021-08-31";

        //when
        OrderCountFromMyPageVo result = orderFrontService.getOrderCountFromMyPage(urUserId, startDate, endDate);

        //then
        assertTrue(result.getDeliveryCompleteCount() > 0);
    }

    @Test
    void getOrderStatusFromEvent_조회_성공() throws Exception {
        //given
        Long evEventId = 311L;
        Long urUserId = 1647338L;

        //when
        String result = orderFrontService.getOrderStatusFromEvent(evEventId, urUserId);

        //then
        assertNotNull(result);
    }

    @Test
    void getOrderCountFromUserGroup_조회_성공() throws Exception {
        //given
        Long urUserId = 1646893L;
        String startDate = "2020-12-01";
        String endDate = "2020-12-31";

        OrderInfoFromUserGroupVo orderInfoFromUserGroupVo = new OrderInfoFromUserGroupVo();
        orderInfoFromUserGroupVo.setOrderCount(1);

        given(mockOrderFrontMapper.getOrderCountFromUserGroup(anyLong(), anyString(), anyString())).willReturn(orderInfoFromUserGroupVo);

        //when
        OrderInfoFromUserGroupVo result = mockOrderFrontService.getOrderCountFromUserGroup(urUserId, startDate, endDate);

        //then
        assertTrue(result.getOrderCount() > 0);
    }

    @Test
    void getOrderInfoFromStampPurchase_조회_성공() throws Exception {
        //given
        Long urUserId = 1646893L;
        String startDate = "2020-12-01";
        String endDate = "2021-12-31";
        int orderPrice = 1;

        OrderInfoFromStampPurchaseVo orderInfoFromStampPurchaseVo = new OrderInfoFromStampPurchaseVo();
        orderInfoFromStampPurchaseVo.setOrderCount(1);

        given(mockOrderFrontMapper.getOrderInfoFromStampPurchase(anyLong(), anyString(), anyString(), anyInt())).willReturn(orderInfoFromStampPurchaseVo);

        //when
        OrderInfoFromStampPurchaseVo result = mockOrderFrontService.getOrderInfoFromStampPurchase(urUserId, startDate, endDate, orderPrice);

        //then
        assertNotNull(result);
    }

    @Test
    void getOrderInfoFromMain_조회_성공() throws Exception {
        //given
        Long ilGoodsId = 900055L;
        String startDateTime = "2020-12-03 09:00";
        String endDateTime = "2021-08-04 11:41";

        //when
        int result = orderFrontService.getOrderInfoFromMain(ilGoodsId, startDateTime, endDateTime);

        //then
        assertTrue(result > 0);
    }

    @Test
    void getOrderInfoFromMain_조회_값없음() throws Exception {
        //given
        Long ilGoodsId = 15480999L;
        String startDateTime = "2020-12-03 09:00";
        String endDateTime = "2021-01-04 11:41";

        //when
        int result = orderFrontService.getOrderInfoFromMain(ilGoodsId, startDateTime, endDateTime);

        //then
        assertEquals(result, 0);
    }

    @Test
    void getOrderInfoFromGift_조회_성공() throws Exception {
        //given
        Long evExhibitId = 161L;
        List<Long> goodsIdList = Arrays.asList(900055L, 15481L);

        //when
        int result = orderFrontService.getOrderInfoFromGift(evExhibitId, goodsIdList);

        //then
        assertTrue(result > 0);
    }

    @Test
    void getOrderInfoFromExperienceFeedback_조회_성공() throws Exception {
        //given
        Long evEventId = 316L;
        Long urUserId = 1646893L;
        int feedbackDay = CustomerConstants.FEEDBACK_DAY;

        OrderInfoFromFeedbackVo orderInfoFromFeedbackVo = new OrderInfoFromFeedbackVo();
        orderInfoFromFeedbackVo.setEvEventId(evEventId);

        given(mockOrderFrontMapper.getOrderInfoFromExperienceFeedback(anyLong(), anyLong(), anyInt())).willReturn(orderInfoFromFeedbackVo);

        // when
        OrderInfoFromFeedbackVo result = mockOrderFrontService.getOrderInfoFromExperienceFeedback(evEventId, urUserId, feedbackDay);

        // then
        assertTrue(result.getEvEventId() > 0);
    }

    @Test
    void getOrderInfoFromIllegalCount_조회_성공() {
        //given
        String startDateTime = "2021-06-01 00:00:00";
        String endDateTime = "2021-06-25 23:59:59";
        Integer detectCount = 2;

        //when
        List<OrderInfoFromIllegalLogVo> result = orderFrontService.getOrderInfoFromIllegalCount(startDateTime, endDateTime, detectCount);
    }

    @Test
    void getOrderInfoFromReward_조회_성공() {
        //given
        OrderInfoFromRewardRequestDto dto = OrderInfoFromRewardRequestDto.builder()
                .csRewardId(36L)
                .urUserId(1647338L)
                .startDate("2021-07-01")
                .endDate("2021-08-01")
                .build();

        //when
        List<OrderInfoFromRewardVo> result = orderFrontService.getOrderInfoFromReward(dto);

        //then
        assertTrue(result.size() > 0);
    }

    @Test
    void getOrderInfoFromIllegalPrice_조회_성공() {
        //given
        String startDateTime = "2021-06-01 00:00:00";
        String endDateTime = "2021-06-25 23:59:59";
        Integer detectPrice = 1000;

        //when
        List<OrderInfoFromIllegalLogVo> result = orderFrontService.getOrderInfoFromIllegalPrice(startDateTime, endDateTime, detectPrice);

        //then
        assertTrue(result.size() > 0);
    }

    @Test
    void getOrderInfoFromEmployeeDiscount_조회_성공() {
        //given
        OrderInfoFromEmployeeDiscountRequestDto dto = OrderInfoFromEmployeeDiscountRequestDto.builder()
                .urErpEmployeeCd("157766")
                .psEmplDiscGrpId(293L)
                .startDate("2021-08-01")
                .endDate("2021-08-30")
                .build();

        //when
        Integer result = orderFrontService.getOrderInfoFromEmployeeDiscount(dto);

        //then
        assertTrue(result > 0);
    }

}