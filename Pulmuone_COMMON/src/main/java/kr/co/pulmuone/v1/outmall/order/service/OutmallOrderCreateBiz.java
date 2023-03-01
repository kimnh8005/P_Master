package kr.co.pulmuone.v1.outmall.order.service;

import kr.co.pulmuone.v1.order.registration.dto.OrderBindDto;
import kr.co.pulmuone.v1.outmall.order.dto.OutMallOrderDto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 외부몰관리 > 외부몰주문관리 > 외부몰 주문 엑셀업로드 주문생성
 */
public interface OutmallOrderCreateBiz {
    /**
     * 외부몰 주문 엑셀업로드 주문생성
     */
    int createOrder(List<OrderBindDto> createOrderBindList, long ifOutmallExcelInfoId) throws Exception;

    /**
     * 주문 데이터 바인딩 및 주문생성
     */
    int orderDataBindAndCreateOrder(Map<String, List<OutMallOrderDto>> shippingPriceMap , long ifOutmallExcelInfoId) throws Exception;


}