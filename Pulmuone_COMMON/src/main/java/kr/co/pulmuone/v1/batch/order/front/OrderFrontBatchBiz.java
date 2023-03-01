package kr.co.pulmuone.v1.batch.order.front;

import kr.co.pulmuone.v1.batch.order.front.dto.vo.OrderCountVo;
import kr.co.pulmuone.v1.batch.order.front.dto.vo.OrderInfoFromStampPurchaseBatchVo;
import kr.co.pulmuone.v1.batch.order.front.dto.vo.OrderInfoFromUserGroupBatchVo;
import kr.co.pulmuone.v1.batch.user.buyer.dto.vo.UserBuyerVo;

import java.util.List;

public interface OrderFrontBatchBiz {

    List<OrderCountVo> getOrderCountGoodsList(String startDateTime, String endDateTime, int lohasCategoryId);

    List<OrderCountVo> getOrderCountLohasGoodsList(String startDateTime, String endDateTime, int lohasCategoryId);

    List<OrderInfoFromStampPurchaseBatchVo> getOrderInfoFromStampPurchase(List<Long> userIdList, String startDateTime, String endDateTime, int orderPrice);

    List<OrderInfoFromUserGroupBatchVo> getOrderCountFromUserGroup(List<UserBuyerVo> userBuyerList, String startDate, String endDate);

    List<Long> getOrderInfoFromUserJoin(List<Long> userIdList, int userJoinDepositDay);

}
