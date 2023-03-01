package kr.co.pulmuone.v1.outmall.ezadmin.service;

import kr.co.pulmuone.v1.order.registration.dto.OrderBindDto;
import kr.co.pulmuone.v1.outmall.ezadmin.dto.EZAdminOrderDto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 외부몰관리 > 외부몰주문관리 > 주문생성
 */
public interface EZAdminOrderRegistrationBiz {

    /**
     * 배치 대상 조회
     * @param ifEasyadminInfoId
     * @return
     */
    Map<String,Integer> setBindOrderOrder(long ifEasyadminInfoId, LocalDateTime startTime) throws Exception;

    List<OrderBindDto> orderDataBind(Map<String, List<EZAdminOrderDto>> shippingPriceMap) throws Exception;

    void failCreateOrder(List<OrderBindDto> orderBindList, long ifEasyadminInfoId, String failMessage) throws Exception;

    void failCreateOrderByEZAdminOrderDtoList(List<EZAdminOrderDto> ezAdminOrderDtoList, String failMessage) throws Exception;
}