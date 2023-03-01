package kr.co.pulmuone.v1.outmall.ezadmin.service;

import kr.co.pulmuone.v1.order.registration.dto.OrderBindDto;
import kr.co.pulmuone.v1.outmall.ezadmin.dto.EZAdminOrderDto;
import kr.co.pulmuone.v1.outmall.order.dto.OutMallOrderDto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 외부몰관리 > 외부몰주문관리 > 주문생성
 */
public interface EZAdminOrderCreateBiz {

    /**
     * 주문 데이터 바인딩 및 주문생성
     * @param shippingPriceMap
     * @param ifEasyadminInfoId
     * @return
     */
    int orderDataBindAndCreateOrder(Map<String, List<EZAdminOrderDto>> shippingPriceMap , Long ifEasyadminInfoId, Long reqDataId) throws Exception;
}