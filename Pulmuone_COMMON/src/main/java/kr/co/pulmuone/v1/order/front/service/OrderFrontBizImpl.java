package kr.co.pulmuone.v1.order.front.service;

import com.github.pagehelper.Page;
import kr.co.pulmuone.v1.customer.feedback.dto.FeedbackTargetListByUserRequestDto;
import kr.co.pulmuone.v1.order.front.dto.OrderInfoFromEmployeeDiscountRequestDto;
import kr.co.pulmuone.v1.order.front.dto.OrderInfoFromMypageRewardRequestDto;
import kr.co.pulmuone.v1.order.front.dto.OrderInfoFromRewardRequestDto;
import kr.co.pulmuone.v1.order.front.dto.vo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class OrderFrontBizImpl implements OrderFrontBiz {

    @Autowired
    private OrderFrontService orderFrontService;

    @Override
    public int getOrderCountFromUserDrop(Long urUserId) throws Exception {
        return orderFrontService.getOrderCountFromUserDrop(urUserId);
    }

    @Override
    public OrderCountFromMyPageVo getOrderCountFromMyPage(Long urUserId, String startDateTime, String endDateTime) throws Exception {
        return orderFrontService.getOrderCountFromMyPage(urUserId, startDateTime, endDateTime);
    }

    @Override
    public String getOrderStatusFromEvent(Long evEventId, Long urUserId) throws Exception {
        return orderFrontService.getOrderStatusFromEvent(evEventId, urUserId);
    }

    @Override
    public OrderInfoFromUserGroupVo getOrderCountFromUserGroup(Long urUserId, String startDate, String endDate) throws Exception {
        return orderFrontService.getOrderCountFromUserGroup(urUserId, startDate, endDate);
    }

    @Override
    public OrderInfoFromStampPurchaseVo getOrderInfoFromStampPurchase(Long urUserId, String startDateTime, String endDateTime, int orderPrice) throws Exception {
        return orderFrontService.getOrderInfoFromStampPurchase(urUserId, startDateTime, endDateTime, orderPrice);
    }

    @Override
    public OrderInfoFromStampPurchaseVo getOrderCountFromNormalEvent(Long urUserId, Long evEventId, String startDateTime, String endDateTime, int orderPrice, String goodsDeliveryTp) throws Exception {
        return orderFrontService.getOrderCountFromNormalEvent(urUserId, evEventId, startDateTime, endDateTime, orderPrice, goodsDeliveryTp);
    }

    @Override
    public int getOrderInfoFromMain(Long ilGoodsId, String startDateTime, String endDateTime) throws Exception {
        return orderFrontService.getOrderInfoFromMain(ilGoodsId, startDateTime, endDateTime);
    }

    @Override
    public int getOrderInfoFromGift(Long evExhibitId, List<Long> goodsIdList) throws Exception {
        return orderFrontService.getOrderInfoFromGift(evExhibitId, goodsIdList);
    }

    @Override
    public Page<OrderInfoFromFeedbackVo> getOrderInfoFromFeedback(FeedbackTargetListByUserRequestDto dto, int feedbackDay) throws Exception {
        return orderFrontService.getOrderInfoFromFeedback(dto, feedbackDay);
    }

    @Override
    public OrderInfoFromFeedbackVo getOrderInfoFromExperienceFeedback(Long evEventId, Long urUserId, int feedbackDay) throws Exception {
        return orderFrontService.getOrderInfoFromExperienceFeedback(evEventId, urUserId, feedbackDay);
    }

    @Override
    public List<OrderInfoFromIllegalLogVo> getOrderInfoFromIllegalCount(String startDateTime, String endDateTime, Integer detectCount) {
        return orderFrontService.getOrderInfoFromIllegalCount(startDateTime, endDateTime, detectCount);
    }

    @Override
    public List<OrderInfoFromIllegalLogVo> getOrderInfoFromIllegalPrice(String startDateTime, String endDateTime, Integer detectCount) {
        return orderFrontService.getOrderInfoFromIllegalPrice(startDateTime, endDateTime, detectCount);
    }

    public List<OrderInfoFromRewardVo> getOrderInfoFromReward(OrderInfoFromRewardRequestDto dto) {
        return orderFrontService.getOrderInfoFromReward(dto);
    }

    @Override
    public List<OrderInfoFromMypageRewardVo> getOrderInfoFromMyPageReward(OrderInfoFromMypageRewardRequestDto dto) {
        if(dto == null) return new ArrayList<>();
        return orderFrontService.getOrderInfoFromMyPageReward(dto);
    }

    @Override
    public Integer getOrderInfoFromEmployeeDiscount(OrderInfoFromEmployeeDiscountRequestDto dto) {
        return orderFrontService.getOrderInfoFromEmployeeDiscount(dto);
    }

}
