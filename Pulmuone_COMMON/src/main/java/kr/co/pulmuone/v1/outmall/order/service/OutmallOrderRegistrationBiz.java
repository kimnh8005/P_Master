package kr.co.pulmuone.v1.outmall.order.service;

import kr.co.pulmuone.v1.order.registration.dto.OrderBindDto;
import kr.co.pulmuone.v1.outmall.order.dto.OutMallOrderDto;
import kr.co.pulmuone.v1.outmall.order.dto.vo.OutMallExcelSuccessVo;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 외부몰관리 > 외부몰주문관리 > 주문생성
 */
public interface OutmallOrderRegistrationBiz {
    /**
     * 배치 실행 대상 정보 조회
     */
    long getLastIfOutmallExcelInfo();

    /**
     * 배치 대상 조회
     * @param ifOutmallExcelInfoId
     * @return
     */
    Map<String,Integer> setBindOrderOrder(long ifOutmallExcelInfoId, LocalDateTime startTime) throws Exception;

    void failCreateOrder(List<OrderBindDto> orderBindList, long ifOutmallExcelInfoId, String failMessage) throws Exception;

    List<OrderBindDto> orderDataBind(Map<String, List<OutMallOrderDto>> shippingPriceMap) throws Exception;

    void verificationOrderCreateSuccess(long ifOutmallExcelInfoId) throws Exception;

    int getOrderCreateCount(long ifOutmallExcelInfoId) throws Exception;

}