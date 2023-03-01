package kr.co.pulmuone.v1.order.registration.service;


import org.springframework.stereotype.Service;

import kr.co.pulmuone.v1.comm.mapper.order.registration.OrderRegistrationSeqMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

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
 *  1.0    2021. 01. 13.            이명수         최초작성
 * =======================================================================
 * </PRE>
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class OrderRegistrationSeqService {
    private final OrderRegistrationSeqMapper orderRegistrationSeqMapper;

    /**
     * 주문
     * OD_ORDER.OD_ORDER_ID PK 조회
     * @return
     */
    protected long getOrderIdSeq(){
        return orderRegistrationSeqMapper.getOrderIdSeq();
    }

    /**
     * 주문번호 생성
     * @return
     */
    protected String getOrderNumber(long odOrderId) {
        return orderRegistrationSeqMapper.getOrderNumber(odOrderId);
    }

    /**
     * 주문 배송지
     * OD_SHIPPING_ZONE.OD_SHIPPING_ZONE_ID PK 조회
     * @return
     */
    protected long getOrderShippingZoneSeq(){
        return orderRegistrationSeqMapper.getOrderShippingZoneSeq();
    }

    /**
     * 주문 배송비
     * OD_SHIPPING_PRICE.OD_SHIPPING_PRICE_ID PK 조회
     * @return
     */
    protected long getOrderShippingPriceSeq(){
        return orderRegistrationSeqMapper.getOrderShippingPriceSeq();
    }

    /**
     * 주문상세
     * OD_ORDER_DETL.OD_ORDER_DETL_ID PK 조회
     * @return
     */
    protected long getOrderDetlSeq(){
        return orderRegistrationSeqMapper.getOrderDetlSeq();
    }

    /**
     * 주문 결제 마스터
     * OD_PAYMENT_MASTER.OD_PAYMENT_MASTER_ID
     * @return
     */
    protected long getPaymentMasterSeq(){
        return orderRegistrationSeqMapper.getPaymentMasterSeq();
    }


    /**
     * 주문 일일배송 패턴
     * OD_ORDER_DETL_DAILY.OD_ORDER_DETL_DAILY_ID
     * @return
     */
    protected long getOrderDetlDailySeq(){
        return orderRegistrationSeqMapper.getOrderDetlDailySeq();
    }
}
