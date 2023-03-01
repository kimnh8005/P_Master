package kr.co.pulmuone.v1.order.registration.service;


import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

/**
 * <PRE>
 * Forbiz Korea
 * 주문 데이터 생성 시퀀 관련 Service
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :   작성일                :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 *  1.0    2021. 04. 15.   강상국         최초작성
 * =======================================================================
 * </PRE>
 */
@Service
@RequiredArgsConstructor
public class OrderRegistrationSeqBizImpl implements OrderRegistrationSeqBiz {
    private final OrderRegistrationSeqService orderRegistrationSeqService;

    /**
     * 주문
     * OD_ORDER.OD_ORDER_ID PK 조회
     * @return
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = {Exception.class})
    public long getOrderIdSeq(){
        return orderRegistrationSeqService.getOrderIdSeq();
    }

    /**
     * 주문번호 생성
     * @return
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = {Exception.class})
    public String getOrderNumber(long odOrderId) {
        return orderRegistrationSeqService.getOrderNumber(odOrderId);
    }

    /**
     * 주문 배송지
     * OD_SHIPPING_ZONE.OD_SHIPPING_ZONE_ID PK 조회
     * @return
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = {Exception.class})
    public long getOrderShippingZoneSeq(){
        return orderRegistrationSeqService.getOrderShippingZoneSeq();
    }

    /**
     * 주문 배송비
     * OD_SHIPPING_PRICE.OD_SHIPPING_PRICE_ID PK 조회
     * @return
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = {Exception.class})
    public long getOrderShippingPriceSeq(){
        return orderRegistrationSeqService.getOrderShippingPriceSeq();
    }

    /**
     * 주문상세
     * OD_ORDER_DETL.OD_ORDER_DETL_ID PK 조회
     * @return
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = {Exception.class})
    public long getOrderDetlSeq(){
        return orderRegistrationSeqService.getOrderDetlSeq();
    }

    /**
     * 주문 결제 마스터
     * OD_PAYMENT_MASTER.OD_PAYMENT_MASTER_ID
     * @return
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = {Exception.class})
    public long getPaymentMasterSeq(){
        return orderRegistrationSeqService.getPaymentMasterSeq();
    }


    /**
     * 주문 일일배송 패턴
     * OD_ORDER_DETL_DAILY.OD_ORDER_DETL_DAILY_ID
     * @return
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = {Exception.class})
    public long getOrderDetlDailySeq(){
        return orderRegistrationSeqService.getOrderDetlDailySeq();
    }
}
