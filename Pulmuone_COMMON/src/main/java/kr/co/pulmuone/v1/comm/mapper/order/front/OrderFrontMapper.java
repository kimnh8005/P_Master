package kr.co.pulmuone.v1.comm.mapper.order.front;

import com.github.pagehelper.Page;
import kr.co.pulmuone.v1.order.front.dto.OrderInfoFromEmployeeDiscountRequestDto;
import kr.co.pulmuone.v1.order.front.dto.OrderInfoFromMypageRewardRequestDto;
import kr.co.pulmuone.v1.order.front.dto.OrderInfoFromRewardRequestDto;
import kr.co.pulmuone.v1.order.front.dto.vo.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface OrderFrontMapper {

    Integer getOrderCountFromUserDrop(@Param("urUserId") Long urUserId) throws Exception;

    OrderCountFromMyPageVo getOrderCountFromMyPage(@Param("urUserId") Long urUserId, @Param("startDateTime") String startDateTime, @Param("endDateTime") String endDateTime) throws Exception;

    String getOrderStatusFromEvent(@Param("evEventId") Long evEventId, @Param("urUserId") Long urUserId) throws Exception;

    OrderInfoFromUserGroupVo getOrderCountFromUserGroup(@Param("urUserId") Long urUserId, @Param("startDate") String startDate, @Param("endDate") String endDate) throws Exception;

    OrderInfoFromStampPurchaseVo getOrderInfoFromStampPurchase(@Param("urUserId") Long urUserId, @Param("startDateTime") String startDateTime, @Param("endDateTime") String endDateTime, @Param("orderPrice") int orderPrice) throws Exception;

    OrderInfoFromStampPurchaseVo getOrderCountFromNormalEvent(@Param("urUserId") Long urUserId, @Param("evEventId") Long evEventId, @Param("startDateTime") String startDateTime, @Param("endDateTime") String endDateTime, @Param("orderPrice") int orderPrice, @Param("goodsDeliveryTp") String goodsDeliveryTp) throws Exception;

    Integer getOrderInfoFromMain(@Param("ilGoodsId") Long ilGoodsId, @Param("startDateTime") String startDateTime, @Param("endDateTime") String endDateTime) throws Exception;

    Integer getOrderInfoFromGift(@Param("evExhibitId") Long evExhibitId, @Param("goodsIdList") List<Long> goodsIdList) throws Exception;

    Page<OrderInfoFromFeedbackVo> getOrderInfoFromFeedback(@Param("urUserId") Long urUserId, @Param("feedbackDay") int feedbackDay) throws Exception;

    OrderInfoFromFeedbackVo getOrderInfoFromExperienceFeedback(@Param("evEventId") Long evEventId, @Param("urUserId") Long urUserId, @Param("feedbackDay") int feedbackDay) throws Exception;

    List<OrderInfoFromIllegalLogVo> getOrderInfoFromIllegalCount(@Param("startDateTime") String startDateTime, @Param("endDateTime") String endDateTime, @Param("detectCount") Integer detectCount);

    List<OrderInfoFromIllegalLogVo> getOrderInfoFromIllegalPrice(@Param("startDateTime") String startDateTime, @Param("endDateTime") String endDateTime, @Param("detectPrice") Integer detectPrice);

    List<Long> getOrderInfoFromIllegalUserId(@Param("startDateTime") String startDateTime, @Param("endDateTime") String endDateTime, @Param("urPcidCd") String urPcidCd);

    List<Long> getOrderInfoFromIllegalOrderId(@Param("startDateTime") String startDateTime, @Param("endDateTime") String endDateTime, @Param("urPcidCd") String urPcidCd);

    List<OrderInfoFromRewardVo> getOrderInfoFromReward(OrderInfoFromRewardRequestDto dto);

    List<OrderInfoFromMypageRewardVo> getOrderInfoFromMyPageReward(OrderInfoFromMypageRewardRequestDto dto);

    Integer getOrderInfoFromEmployeeDiscount(OrderInfoFromEmployeeDiscountRequestDto dto);
}
