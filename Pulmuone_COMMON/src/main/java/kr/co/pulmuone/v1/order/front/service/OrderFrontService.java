package kr.co.pulmuone.v1.order.front.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.page.PageMethod;
import kr.co.pulmuone.v1.comm.mapper.order.front.OrderFrontMapper;
import kr.co.pulmuone.v1.customer.feedback.dto.FeedbackTargetListByUserRequestDto;
import kr.co.pulmuone.v1.order.front.dto.OrderInfoFromEmployeeDiscountRequestDto;
import kr.co.pulmuone.v1.order.front.dto.OrderInfoFromMypageRewardRequestDto;
import kr.co.pulmuone.v1.order.front.dto.OrderInfoFromRewardRequestDto;
import kr.co.pulmuone.v1.order.front.dto.vo.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderFrontService {

    private final OrderFrontMapper orderFrontMapper;

    /**
     * 주문개수 조회 - 회원탈퇴
     *
     * @param urUserId Long
     * @return Integer
     * @throws Exception Exception
     */
    protected int getOrderCountFromUserDrop(Long urUserId) throws Exception {
        Integer result = orderFrontMapper.getOrderCountFromUserDrop(urUserId);
        if (result == null) {
            return 0;
        }
        return result;
    }

    /**
     * 주문개수 조회 - 마이페이지 메인
     *
     * @param urUserId      Long
     * @param startDateTime String
     * @param endDateTime   String
     * @return OrderCountFromMyPageVo
     * @throws Exception Exception
     */
    protected OrderCountFromMyPageVo getOrderCountFromMyPage(Long urUserId, String startDateTime, String endDateTime) throws Exception {
        return orderFrontMapper.getOrderCountFromMyPage(urUserId, startDateTime, endDateTime);
    }

    /**
     * 주문상태 조회 - 체험단
     *
     * @param evEventId Long
     * @param urUserId  Long
     * @return String
     * @throws Exception Exception
     */
    protected String getOrderStatusFromEvent(Long evEventId, Long urUserId) throws Exception {
        return orderFrontMapper.getOrderStatusFromEvent(evEventId, urUserId);
    }

    /**
     * 주문개수 조회 - 회원등급
     *
     * @param urUserId  Long
     * @param startDate String
     * @param endDate   String
     * @return OrderInfoFromUserGroupVo
     * @throws Exception Exception
     */
    protected OrderInfoFromUserGroupVo getOrderCountFromUserGroup(Long urUserId, String startDate, String endDate) throws Exception {
        return orderFrontMapper.getOrderCountFromUserGroup(urUserId, startDate, endDate);
    }

    /**
     * 주문정보 조회 - 스탬프(구매) 이벤트
     *
     * @param urUserId      Long
     * @param startDateTime String
     * @param endDateTime   String
     * @param orderPrice    int
     * @return List<OrderInfoFromStampPurchaseVo>
     * @throws Exception Exception
     */
    protected OrderInfoFromStampPurchaseVo getOrderInfoFromStampPurchase(Long urUserId, String startDateTime, String endDateTime, int orderPrice) throws Exception {
        OrderInfoFromStampPurchaseVo result = orderFrontMapper.getOrderInfoFromStampPurchase(urUserId, startDateTime, endDateTime, orderPrice);
        if (result == null) {
            result = new OrderInfoFromStampPurchaseVo();
            result.setUrUserId(urUserId);
            result.setOrderCount(0);
        }
        return result;
    }

    /**
     * 주문정보 조회 - 일반 응모 이벤트 주문고객
     *
     * @param urUserId      Long
     * @param evEventId     Long
     * @return OrderInfoFromStampPurchaseVo
     * @throws Exception Exception
     */
    protected OrderInfoFromStampPurchaseVo getOrderCountFromNormalEvent(Long urUserId, Long evEventId, String startDateTime, String endDateTime, int orderPrice, String goodsDeliveryTp) throws Exception {
        OrderInfoFromStampPurchaseVo result = orderFrontMapper.getOrderCountFromNormalEvent(urUserId, evEventId, startDateTime, endDateTime, orderPrice, goodsDeliveryTp);
        if (result == null) {
            result = new OrderInfoFromStampPurchaseVo();
            result.setUrUserId(urUserId);
            result.setOrderCount(0);
        }
        return result;
    }

    /**
     * 주문 정보 조회 - 메인 - 상품 구매개수
     *
     * @param ilGoodsId     Long
     * @param startDateTime String
     * @param endDateTime   String
     * @return int
     * @throws Exception Exception
     */
    protected int getOrderInfoFromMain(Long ilGoodsId, String startDateTime, String endDateTime) throws Exception {
        Integer result = orderFrontMapper.getOrderInfoFromMain(ilGoodsId, startDateTime, endDateTime);
        if (result == null) {
            return 0;
        }
        return result;
    }

    /**
     * 주문 정보 조회 - 증정행사
     *
     * @param ilGoodsId Long
     * @return int
     * @throws Exception Exception
     */
    protected int getOrderInfoFromGift(Long evExhibitId, List<Long> goodsIdList) throws Exception {
        Integer result = orderFrontMapper.getOrderInfoFromGift(evExhibitId, goodsIdList);
        if (result == null) {
            return 0;
        }
        return result;
    }

    /**
     * 주문 정보 조회 - 후기
     *
     * @param dto         FeedbackTargetListByUserRequestDto
     * @param feedbackDay int
     * @return Page<OrderInfoFromFeedbackVo>
     * @throws Exception Exception
     */
    protected Page<OrderInfoFromFeedbackVo> getOrderInfoFromFeedback(FeedbackTargetListByUserRequestDto dto, int feedbackDay) throws Exception {
        PageMethod.startPage(dto.getPage(), dto.getLimit());
        return orderFrontMapper.getOrderInfoFromFeedback(dto.getUrUserId(), feedbackDay);
    }

    /**
     * 주문 정보 조회 - 후기
     *
     * @param evEventId   Long
     * @param urUserId    Long
     * @param feedbackDay int
     * @return OrderInfoFromFeedbackVo
     * @throws Exception Exception
     */
    protected OrderInfoFromFeedbackVo getOrderInfoFromExperienceFeedback(Long evEventId, Long urUserId, int feedbackDay) throws Exception {
        return orderFrontMapper.getOrderInfoFromExperienceFeedback(evEventId, urUserId, feedbackDay);
    }

    /**
     * 주문 정보 조회 - 부정거래 탐지 - 주문건수
     *
     * @param startDateTime String
     * @param endDateTime   String
     * @param detectCount   Integer
     * @return List<OrderInfoFromIllegalLogVo>
     */
    protected List<OrderInfoFromIllegalLogVo> getOrderInfoFromIllegalCount(String startDateTime, String endDateTime, Integer detectCount) {
        List<OrderInfoFromIllegalLogVo> resultList = orderFrontMapper.getOrderInfoFromIllegalCount(startDateTime, endDateTime, detectCount);
        for (OrderInfoFromIllegalLogVo result : resultList) {
            result.setUserIdList(orderFrontMapper.getOrderInfoFromIllegalUserId(startDateTime, endDateTime, result.getUrPcidCd()));
            result.setOrderIdList(orderFrontMapper.getOrderInfoFromIllegalOrderId(startDateTime, endDateTime, result.getUrPcidCd()));
        }
        return resultList;
    }

    /**
     * 주문 정보 조회 - 부정거래 탐지 - 주문금액
     *
     * @param startDateTime String
     * @param endDateTime   String
     * @param detectPrice   Integer
     * @return List<OrderInfoFromIllegalLogVo>
     */
    protected List<OrderInfoFromIllegalLogVo> getOrderInfoFromIllegalPrice(String startDateTime, String endDateTime, Integer detectPrice) {
        List<OrderInfoFromIllegalLogVo> resultList = orderFrontMapper.getOrderInfoFromIllegalPrice(startDateTime, endDateTime, detectPrice);
        for (OrderInfoFromIllegalLogVo result : resultList) {
            result.setUserIdList(orderFrontMapper.getOrderInfoFromIllegalUserId(startDateTime, endDateTime, result.getUrPcidCd()));
            result.setOrderIdList(orderFrontMapper.getOrderInfoFromIllegalOrderId(startDateTime, endDateTime, result.getUrPcidCd()));
        }
        return resultList;
    }

    /**
     * 주문 정보 조회 - 고객보상
     *
     * @param dto OrderInfoFromRewardRequestDto
     * @return List<OrderInfoFromRewardGoodsVo>
     */
    protected List<OrderInfoFromRewardVo> getOrderInfoFromReward(OrderInfoFromRewardRequestDto dto) {
        return orderFrontMapper.getOrderInfoFromReward(dto);
    }

    /**
     * 주문 정보 조회 - 마이페이지 고객보상
     *
     * @param dto OrderInfoFromMypageRewardRequestDto
     * @return List<OrderInfoFromMypageRewardVo>
     */
    protected List<OrderInfoFromMypageRewardVo> getOrderInfoFromMyPageReward(OrderInfoFromMypageRewardRequestDto dto) {
        return orderFrontMapper.getOrderInfoFromMyPageReward(dto);
    }

    /**
     * 주문 정보 조회 - 임직원 할인
     *
     * @param dto OrderInfoFromEmployeeDiscountRequestDto
     * @return Integer
     */
    protected Integer getOrderInfoFromEmployeeDiscount(OrderInfoFromEmployeeDiscountRequestDto dto) {
        Integer result = orderFrontMapper.getOrderInfoFromEmployeeDiscount(dto);
        if (result == null) return 0;
        return result;
    }

}