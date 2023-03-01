package kr.co.pulmuone.v1.batch.order.front;

import kr.co.pulmuone.v1.batch.order.front.dto.vo.OrderCountVo;
import kr.co.pulmuone.v1.batch.order.front.dto.vo.OrderInfoFromStampPurchaseBatchVo;
import kr.co.pulmuone.v1.batch.order.front.dto.vo.OrderInfoFromUserGroupBatchVo;
import kr.co.pulmuone.v1.batch.user.buyer.dto.vo.UserBuyerVo;
import kr.co.pulmuone.v1.comm.mappers.batch.master.order.OrderFrontBatchMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderFrontBatchService {
    private final OrderFrontBatchMapper orderFrontBatchMapper;

    /**
     * 판매량 순위
     *
     * @param startDate String
     * @param endDate   String
     * @return List<OrderCountVo>
     */
    protected List<OrderCountVo> getOrderCountGoodsList(String startDateTime, String endDateTime, int lohasCategoryId) {
        return orderFrontBatchMapper.getOrderCountGoodsList(startDateTime, endDateTime, lohasCategoryId);
    }

    /**
     * 판매량 순위 - 로하스 카테고리
     *
     * @param startDate String
     * @param endDate   String
     * @return List<OrderCountVo>
     */
    protected List<OrderCountVo> getOrderCountLohasGoodsList(String startDateTime, String endDateTime, int lohasCategoryId) {
        return orderFrontBatchMapper.getOrderCountLohasGoodsList(startDateTime, endDateTime, lohasCategoryId);
    }
    
    /**
     * 주문정보 조회 - 스탬프 구매 이벤트용
     *
     * @param userIdList    List<Long>
     * @param startDateTime String
     * @param endDateTime   String
     * @param orderPrice    int
     * @return List<OrderInfoByStampPurchaseVo>
     */
    protected List<OrderInfoFromStampPurchaseBatchVo> getOrderInfoFromStampPurchase(List<Long> userIdList, String startDateTime, String endDateTime, int orderPrice) {
        return orderFrontBatchMapper.getOrderInfoFromStampPurchase(userIdList, startDateTime, endDateTime, orderPrice);
    }

    /**
     * 주문정보 조회 - 등급
     *
     * @param userBuyerList List<UserBuyerVo>
     * @param startDate     String
     * @param endDate       String
     * @return List<OrderInfoFromUserGroupBatchVo>
     */
    protected List<OrderInfoFromUserGroupBatchVo> getOrderCountFromUserGroup(List<UserBuyerVo> userBuyerList, String startDate, String endDate) {
        return orderFrontBatchMapper.getOrderCountFromUserGroup(userBuyerList, startDate, endDate);
    }

    /**
     * 주문정보 조회 - 신규회원 추천인 적립금 지급
     *
     * @param userIdList         List<Long>
     * @param userJoinDepositDay int
     * @return List<Long>
     */
    protected List<Long> getOrderInfoFromUserJoin(List<Long> userIdList, int userJoinDepositDay) {
        return orderFrontBatchMapper.getOrderInfoFromUserJoin(userIdList, userJoinDepositDay);
    }

}
