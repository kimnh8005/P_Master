package kr.co.pulmuone.v1.order.front.service;

import com.github.pagehelper.Page;
import kr.co.pulmuone.v1.customer.feedback.dto.FeedbackTargetListByUserRequestDto;
import kr.co.pulmuone.v1.order.front.dto.OrderInfoFromEmployeeDiscountRequestDto;
import kr.co.pulmuone.v1.order.front.dto.OrderInfoFromMypageRewardRequestDto;
import kr.co.pulmuone.v1.order.front.dto.OrderInfoFromRewardRequestDto;
import kr.co.pulmuone.v1.order.front.dto.vo.*;

import java.util.List;

public interface OrderFrontBiz {

    int getOrderCountFromUserDrop(Long urUserId) throws Exception;

    OrderCountFromMyPageVo getOrderCountFromMyPage(Long urUserId, String startDateTime, String endDateTime) throws Exception;

    String getOrderStatusFromEvent(Long evEventId, Long urUserId) throws Exception;

    OrderInfoFromUserGroupVo getOrderCountFromUserGroup(Long urUserId, String startDate, String endDate) throws Exception;

    OrderInfoFromStampPurchaseVo getOrderInfoFromStampPurchase(Long urUserId, String startDateTime, String endDateTime, int orderPrice) throws Exception;

    OrderInfoFromStampPurchaseVo getOrderCountFromNormalEvent(Long urUserId, Long evEventId, String startDateTime, String endDateTime, int orderPrice, String goodsDeliveryTp) throws Exception;

    int getOrderInfoFromMain(Long ilGoodsId, String startDateTime, String endDateTime) throws Exception;

    int getOrderInfoFromGift(Long evExhibitId, List<Long> goodsIdList) throws Exception;

    Page<OrderInfoFromFeedbackVo> getOrderInfoFromFeedback(FeedbackTargetListByUserRequestDto dto, int feedbackDay) throws Exception;

    OrderInfoFromFeedbackVo getOrderInfoFromExperienceFeedback(Long evEventId, Long urUserId, int feedbackDay) throws Exception;

    List<OrderInfoFromIllegalLogVo> getOrderInfoFromIllegalCount(String startDateTime, String endDateTime, Integer detectCount);

    List<OrderInfoFromIllegalLogVo> getOrderInfoFromIllegalPrice(String startDateTime, String endDateTime, Integer detectCount);

    List<OrderInfoFromRewardVo> getOrderInfoFromReward(OrderInfoFromRewardRequestDto dto);

    List<OrderInfoFromMypageRewardVo> getOrderInfoFromMyPageReward(OrderInfoFromMypageRewardRequestDto dto);

    Integer getOrderInfoFromEmployeeDiscount(OrderInfoFromEmployeeDiscountRequestDto dto);

}
