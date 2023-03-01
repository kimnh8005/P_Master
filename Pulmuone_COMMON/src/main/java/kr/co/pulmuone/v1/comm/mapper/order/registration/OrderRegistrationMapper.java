package kr.co.pulmuone.v1.comm.mapper.order.registration;

import java.util.List;

import kr.co.pulmuone.v1.order.order.dto.vo.*;
import kr.co.pulmuone.v1.order.registration.dto.OrderDetlDiscountInfoDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import kr.co.pulmuone.v1.order.schedule.dto.OrderDetailScheduleListDto;
import kr.co.pulmuone.v1.order.schedule.dto.OrderDetailScheduleUpdateRequestDto;

/**
 * <PRE>
 * Forbiz Korea
 * 주문 데이터 생성 관련 Mapper
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :   작성일                :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 *  1.0    2021. 01. 13.            이명수         최초작성
 * =======================================================================
 * </PRE>
 */
@Mapper
public interface OrderRegistrationMapper {

    int addOrder(OrderVo orderVo);

    int addOrderDt(OrderDtVo orderDtVo);

    int addOrderPresent(OrderPresentVo orderPresentVo);

    int addShippingZone(OrderShippingZoneVo orderShippingZoneVo);

    int addShippingZoneHist(OrderShippingZoneHistVo orderShippingZoneHistVo);

    int addShippingPrice(OrderShippingPriceVo orderShippingPriceVo);

    int addOrderDetl(OrderDetlVo orderDetlVo);

    int addOrderDetlPack(OrderDetlPackVo orderDetlPackVo);

    int addOrderDetlDiscount(OrderDetlDiscountVo orderDetlDiscountVo);

    int addPayment(OrderPaymentVo orderPaymentVo);

    int addPaymentMaster(OrderPaymentMasterVo orderPaymentMasterVo);

    int putApprovalOrder(OrderVo orderVo);

    int putApprovalOrderDetl(OrderDetlVo orderDetlVo);

    int putApprovalPaymentMaster(OrderPaymentMasterVo orderPaymentMasterVo);

    int putPayApprovalPaymentMaster(OrderPaymentMasterVo orderPaymentMasterVo);

    int putApprovalOrderDtIr(Long odOrderId);

    int putApprovalOrderDtIc(Long odOrderId);

    int addOrderDetlDaily(OrderDetlDailyVo orderDetlDailyVo);

    int putOrderDetlSeq(long odOrderId);

    int putOrderDetlDailySeq(long odOrderId);

    int addOrderDetlDailySch(OrderDetlDailySchVo orderDetlDailySchVo);

	/**
     * @Desc 주문 스케줄 요일 리스트
     * @param orderDetailScheduleUpdateRequestDto
     * @return OrderDetailScheduleListDto
     */
	List<OrderDetailScheduleListDto> getOrderDetailScheduleDayOfWeekList(OrderDetailScheduleUpdateRequestDto orderDetailScheduleUpdateRequestDto);

    /**
     * 주문상세스텝ID 업데이트 처리
     * @return
     */
    int putOdOrderDetlStepId(@Param(value ="odOrderId" ) long odOrderId, @Param(value = "odOrderDetlStepId" ) long odOrderDetlStepId);

    /**
     * 주문상세 등록 - Insert Select
     * @param orderDetlVo
     * @return
     */
    int selectAddOrderDetl(@Param(value ="orderDetlVo" ) OrderDetlVo orderDetlVo, @Param(value ="prevOdOrderDetlId" ) long prevOdOrderDetlId);

    /**
     * 주문상세할인정보 등록 - Insert Select
     * @param orderDetlDiscountInfoDto
     * @return
     */
    int selectAddOrderDetlDiscount(OrderDetlDiscountInfoDto orderDetlDiscountInfoDto);

    /**
     * 주문상세배송금액 등록 - Insert Select
     * @param orderDetlVo
     * @return
     */
    public int selectAddOrderDetlShippingPrice(@Param(value ="orderShippingPriceVo" ) OrderShippingPriceVo orderShippingPriceVo, @Param(value ="prevOdOrderDetlId" ) long prevOdOrderDetlId);

    int addAccount(OrderAccountVo orderAccountVo);

    int addOrderCashReceipt(OrderCashReceiptVo orderCashReceiptVo);

    /**
     * 주문복사 에서 주문 등록
     * @param orderVo
     * @param serchOdOrderId
     * @return
     */
    int addOrderCopyOdOrder(@Param(value ="orderVo" ) OrderVo orderVo, @Param(value ="srchOdOrderId" ) long srchOdOrderId);

    /**
     * 주문복사에서 주문상세 등록
     * @param orderDetlVo
     * @param srchOdOrderDetlId
     * @return
     */
    int addOrderCopyOrderDetl(@Param(value ="orderDetlVo" ) OrderDetlVo orderDetlVo, @Param(value ="srchOdOrderDetlId" ) long srchOdOrderDetlId);

    /**
     * 주문복사에서 주문상세할인금액 등록
     * @param orderDetlDiscountVo
     * @param srchOdOrderId
     * @param srchOdOrderDetlId
     * @param goodsCouponPrice
     * @param cartCouponPrice
     * @return
     */
    int addOrderCopyOrderDetlDiscount(@Param(value ="orderDetlDiscountVo" ) OrderDetlDiscountVo orderDetlDiscountVo
    		, @Param(value ="srchOdOrderId" ) long srchOdOrderId, @Param(value ="srchOdOrderDetlId" ) long srchOdOrderDetlId
    		, @Param(value ="goodsCouponPrice" ) long goodsCouponPrice, @Param(value ="cartCouponPrice" ) long cartCouponPrice);

    /**
     * 주문복사에서 주문상세묶음상품 신규등록
     * @param orderDetlPackVo
     * @param srchOdOrderDetlId
     * @return
     */
    int addOrderCopyOrderDetlPack(@Param(value ="orderDetlPackVo" ) OrderDetlPackVo orderDetlPackVo, @Param(value ="srchOdOrderDetlId" ) long srchOdOrderDetlId);

    /**
     * 주문복사에서 주문상세 일일배송 패턴 신규등록
     * @param orderDetlDailyVo
     * @param srchOdOrderId
     * @param srchOdOrderDetlId
     * @return
     */
    int addOrderCopyOrderDetlDaily(@Param(value ="orderDetlDailyVo" ) OrderDetlDailyVo orderDetlDailyVo, @Param(value ="srchOdOrderId" ) long srchOdOrderId, @Param(value ="srchOdOrderDetlId" ) long srchOdOrderDetlId);

    /**
     * 주문복사에서 주문상세 일일배송 스케쥴
     * @param orderDetlDailySchVo
     * @param srchOdOrderDetlDailyId
     * @return
     */
    int addOrderCopyOrderDetlDailySch(@Param(value ="orderDetlDailySchVo" ) OrderDetlDailySchVo orderDetlDailySchVo, @Param(value ="srchOdOrderDetlDailyId" ) long srchOdOrderDetlDailyId);

    /**
     * 주문복사에서 주문 배송지 등록
     * @param orderShippingZoneVo
     * @param srchOdShippingZoneId
     * @return
     */
    int addOrderCopyShippingZone(@Param(value ="orderShippingZoneVo" ) OrderShippingZoneVo orderShippingZoneVo, @Param(value ="srchOdShippingZoneId" ) long srchOdShippingZoneId);

    /**
     * 주문복사에서 주문 배송지 이력 등록
     * @param OrderShippingZoneHistVo
     * @param srchOdShippingZoneId
     * @return
     */
    int addOrderCopyShippingZoneHist(@Param(value ="orderShippingZoneHistVo" ) OrderShippingZoneHistVo orderShippingZoneHistVo, @Param(value ="srchOdShippingZoneId" ) long srchOdShippingZoneId);

    /**
     * 주문복사에서 주문 배송비 등록
     * @param orderShippingPriceVo
     * @param srchOdShippingPriceId
     * @return
     */
    int addOrderCopyShippingPrice(@Param(value ="orderShippingPriceVo" ) OrderShippingPriceVo orderShippingPriceVo, @Param(value ="srchOdShippingPriceId" ) long srchOdShippingPriceId, @Param(value ="sellersGroupCd" ) String sellersGroupCd);

    /**
     * 주문복사에서 주문결제 등록
     * @param orderPaymentVo
     * @param srchOdOrderId
     * @param srchOdPaymentMasterId
     * @return
     */
    int addOrderCopyPayment(@Param(value ="orderPaymentVo" ) OrderPaymentVo orderPaymentVo, @Param(value ="srchOdOrderId" ) long srchOdOrderId,  @Param(value ="srchOdPaymentMasterId" ) long srchOdPaymentMasterId);


    int addOrderDetlDailyZone(@Param(value ="odOrderId" ) long odOrderId, @Param("promotionTp") String promotionTp);

    OrderVo getOrderCopySalIfYn(Long odOrderId);

    int putOrderDetailStatusHist(OrderDetlHistVo orderDetlHistVo);
}
