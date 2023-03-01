package kr.co.pulmuone.v1.order.registration.service;


import kr.co.pulmuone.v1.order.order.dto.vo.*;
import kr.co.pulmuone.v1.order.registration.dto.OrderApprovalDto;
import kr.co.pulmuone.v1.order.registration.dto.OrderBindDto;
import kr.co.pulmuone.v1.order.registration.dto.OrderDetlDiscountInfoDto;
import kr.co.pulmuone.v1.order.registration.dto.OrderRegistrationResponseDto;

import java.util.List;


/**
 * <PRE>
 * Forbiz Korea
 * 주문 데이터 생성 관련 Interface
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :   작성일                :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 *  1.0    2020. 12. 22.            이명수         최초작성
 * =======================================================================
 * </PRE>
 */
public interface OrderRegistrationBiz {
    /**
     * 주문서 생성
     * @param orderBindList
     * @return
     */
    OrderRegistrationResponseDto createOrder(List<OrderBindDto> orderBindList, String paymentApprovalYn);

    /**
     * 주문서 직접결제 승인
     * @param orderApprovalDto
     * @return
     */
    OrderRegistrationResponseDto approvalDirectOrder(OrderApprovalDto orderApprovalDto);

    /**
     * 주문서 승인
     * @param orderApprovalDto
     * @return
     */
    OrderRegistrationResponseDto approvalOrder(OrderApprovalDto orderApprovalDto);

    /**
     * 주문서 결제완료 승인
     * @param orderApprovalDto
     * @return
     */
    OrderRegistrationResponseDto payApprovalOrder(OrderApprovalDto orderApprovalDto);

    /**
     * 주문 상세번호 생성
     * @return
     */
    long getOrderDetlSeq();

    /**
     * 주문상세스텝ID 업데이트 처리
     * @return
     */
    int putOdOrderDetlStepId(long odOrderId, long odOrderDetlStepId);

    /**
     * 주문상세 등록
     * @param orderDetlVo
     * @return
     */
    int addOrderDetl(OrderDetlVo orderDetlVo);

    /**
     * 주문
     * OD_ORDER.OD_ORDER_ID PK 조회
     * @return
     */
    long getOrderIdSeq();

    /**
     * 주문번호 생성
     * @return
     */
    String getOrderNumber(long odOrderId);

    /**
     * 결제마스터 ID PK 조회
     * @return
     */
    long getPaymentMasterSeq();

	/**
	 * 결제를 입력 한다.
	 * @param orderPaymentMasterVo
	 * @return
	 */
	int addPayment(OrderPaymentVo orderPaymentVo);

	/**
	 * 결제 마스터를 입력 한다.
	 * @param orderPaymentMasterVo
	 * @return
	 */
	int addPaymentMaster(OrderPaymentMasterVo orderPaymentMasterVo);

	/**
	 * 결제 마스터를 업데이트 한다.
	 * @param orderPaymentMasterVo
	 * @return
	 */
	int putApprovalPaymentMaster(OrderPaymentMasterVo orderPaymentMasterVo);

    /**
     * 주문 배송비
     * OD_SHIPPING_PRICE.OD_SHIPPING_PRICE_ID PK 조회
     * @return
     */
	long getOrderShippingPriceSeq();

    /**
     * 주문상세 등록 - Insert Select
     * @param orderDetlVo
     * @return
     */
    int selectAddOrderDetl(OrderDetlVo orderDetlVo, long prevOdOrderDetlId);

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
    int selectAddOrderDetlShippingPrice(OrderShippingPriceVo orderShippingPriceVo, long prevOdOrderDetlId);

    /**
     * 주문 상세 일일배송 스케쥴 등록
     * @param orderDetlDailySchVo
     * @return
     */
    int addOrderDetlDailySch(OrderDetlDailySchVo orderDetlDailySchVo);

    /**
     * 주문복사 에서 주문 등록
     * @param orderVo
     * @param serchOdOrderId
     * @return
     */
    int addOrderCopyOdOrder(OrderVo orderVo, long srchOdOrderId);

    /**
     * 주문복사에서 주문상세 등록
     * @param orderDetlVo
     * @param srchOdOrderDetlId
     * @return
     */
    int addOrderCopyOrderDetl(OrderDetlVo orderDetlVo, long srchOdOrderDetlId);

    /**
     * 주문복사에서 주문상세할인금액 등록
     * @param orderDetlDiscountVo
     * @param srchOdOrderId
     * @param srchOdOrderDetlId
     * @param goodsCouponPrice
     * @param cartCouponPrice
     * @return
     */
    int addOrderCopyOrderDetlDiscount(OrderDetlDiscountVo orderDetlDiscountVo, long srchOdOrderId, long srchOdOrderDetlId, long goodsCouponPrice, long cartCouponPrice);

    /**
     * 주문복사에서 주문상세묶음상품 신규등록
     * @param orderDetlPackVo
     * @param srchOdOrderDetlId
     * @return
     */
    int addOrderCopyOrderDetlPack(OrderDetlPackVo orderDetlPackVo, long srchOdOrderDetlId);

    /**
     * 주문복사에서 주문상세 일일배송 패턴 신규등록
     * @param orderDetlDailyVo
     * @param srchOdOrderId
     * @param srchOdOrderDetlId
     * @return
     */
    int addOrderCopyOrderDetlDaily(OrderDetlDailyVo orderDetlDailyVo, long srchOdOrderId, long srchOdOrderDetlId);

    /**
     * 주문복사에서 주문상세 일일배송 스케쥴
     * @param orderDetlDailySchVo
     * @param srchOdOrderDetlDailyId
     * @return
     */
    int addOrderCopyOrderDetlDailySch(OrderDetlDailySchVo orderDetlDailySchVo, long srchOdOrderDetlDailyId);

    /**
     * 주문복사에서 주문 배송지 등록
     * @param orderShippingZoneVo
     * @param srchOdShippingZoneId
     * @return
     */
    int addOrderCopyShippingZone(OrderShippingZoneVo orderShippingZoneVo, long srchOdShippingZoneId);

    /**
     * 주문복사에서 주문 배송지 이력 등록
     * @param OrderShippingZoneHistVo
     * @param srchOdShippingZoneId
     * @return
     */
	int addOrderCopyShippingZoneHist(OrderShippingZoneHistVo orderShippingZoneHistVo, long srchOdShippingZoneId);

    /**
     * 주문복사에서 주문 배송비 등록
     * @param orderShippingPriceVo
     * @param srchOdShippingPriceId
     * @return
     */
	int addOrderCopyShippingPrice(OrderShippingPriceVo orderShippingPriceVo, long srchOdShippingPriceId, String sellersGroupCd);

    /**
     * 주문복사에서 주문결제 등록
     * @param orderPaymentVo
     * @param srchOdOrderId
     * @param srchOdPaymentMasterId
     * @return
     */
	int addOrderCopyPayment(OrderPaymentVo orderPaymentVo, long srchOdOrderId,  long srchOdPaymentMasterId);

	/**
	 * 주문복사에서 주문 상세 주문상세 정렬 키 업데이트
	 * @param odOrderId
	 * @return
	 */
	int putOrderDetlSeq(long odOrderId);

	/**
     * 주문상세 일일배송 배송지 정보 등록
     * OD_ORDER_DETL_DAILY_ZONE
     * @param odOrderId
     * @param promotionTp
     */
    int addOrderDetlDailyZone(long odOrderId, String promotionTp);

	/**
     * 주문 상태 일자  등록
     * OD_ORDER_DETL_DAILY_ZONE
     * @param odOrderId
     * @param promotionTp
     */
    int addOrderDt(OrderDtVo orderDtVo);

    OrderVo getOrderCopySalIfYn(Long odOrderId);

    int addOrderCashReceipt(OrderCashReceiptVo orderCashReceiptVo);

    OrderRegistrationResponseDto createOrderNonTransaction(List<OrderBindDto> orderBindList, String paymentApprovalYn);

    /**
     * 주문 환불 정보 등록
     */
    int addAccount(OrderAccountVo orderAccountVo);

    /**
     * 일일상품 패턴 등록
     */
    void setOrderDetlDaily(OrderDetlVo orderDetlVo, OrderDetlDailyVo orderDetlDailyVo) throws Exception;
}
