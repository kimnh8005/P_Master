package kr.co.pulmuone.v1.batch.order.front;

import kr.co.pulmuone.v1.batch.order.front.dto.vo.OrderCountVo;
import kr.co.pulmuone.v1.batch.order.front.dto.vo.OrderInfoFromStampPurchaseBatchVo;
import kr.co.pulmuone.v1.batch.order.front.dto.vo.OrderInfoFromUserGroupBatchVo;
import kr.co.pulmuone.v1.batch.user.buyer.dto.vo.UserBuyerVo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class OrderFrontBatchBizImpl implements OrderFrontBatchBiz {

    private final OrderFrontBatchService service;

    @Override
    public List<OrderCountVo> getOrderCountGoodsList(String startDateTime, String endDateTime, int lohasCategoryId) {
        return service.getOrderCountGoodsList(startDateTime, endDateTime, lohasCategoryId);
    }

    @Override
    public List<OrderCountVo> getOrderCountLohasGoodsList(String startDateTime, String endDateTime, int lohasCategoryId) {
        return service.getOrderCountLohasGoodsList(startDateTime, endDateTime, lohasCategoryId);
    }

    @Override
    public List<OrderInfoFromStampPurchaseBatchVo> getOrderInfoFromStampPurchase(List<Long> userIdList, String startDateTime, String endDateTime, int orderPrice) {
        return service.getOrderInfoFromStampPurchase(userIdList, startDateTime, endDateTime, orderPrice);
    }

    @Override
    public List<OrderInfoFromUserGroupBatchVo> getOrderCountFromUserGroup(List<UserBuyerVo> userBuyerList, String startDate, String endDate) {
        return service.getOrderCountFromUserGroup(userBuyerList, startDate, endDate);
    }

    @Override
    public List<Long> getOrderInfoFromUserJoin(List<Long> userIdList, int userJoinDepositDay) {
        return service.getOrderInfoFromUserJoin(userIdList, userJoinDepositDay);
    }

}
