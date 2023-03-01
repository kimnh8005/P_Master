package kr.co.pulmuone.v1.comm.mappers.batch.master.order;

import kr.co.pulmuone.v1.batch.order.front.dto.vo.OrderCountVo;
import kr.co.pulmuone.v1.batch.order.front.dto.vo.OrderInfoFromStampPurchaseBatchVo;
import kr.co.pulmuone.v1.batch.order.front.dto.vo.OrderInfoFromUserGroupBatchVo;
import kr.co.pulmuone.v1.batch.user.buyer.dto.vo.UserBuyerVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface OrderFrontBatchMapper {

    List<OrderCountVo> getOrderCountGoodsList(@Param("startDateTime") String startDateTime, @Param("endDateTime") String endDateTime, @Param("lohasCategoryId") int lohasCategoryId);

    List<OrderCountVo> getOrderCountLohasGoodsList(@Param("startDateTime") String startDateTime, @Param("endDateTime") String endDateTime, @Param("lohasCategoryId") int lohasCategoryId);

    List<OrderInfoFromStampPurchaseBatchVo> getOrderInfoFromStampPurchase(@Param("userIdList") List<Long> userIdList, @Param("startDateTime") String startDateTime, @Param("endDateTime") String endDateTime, @Param("orderPrice") int orderPrice);

    List<OrderInfoFromUserGroupBatchVo> getOrderCountFromUserGroup(@Param("userBuyerList") List<UserBuyerVo> userBuyerList, @Param("startDate") String startDate, @Param("endDate") String endDate);

    List<Long> getOrderInfoFromUserJoin(@Param("userIdList") List<Long> userIdList, @Param("userJoinDepositDay") int userJoinDepositDay);

}
