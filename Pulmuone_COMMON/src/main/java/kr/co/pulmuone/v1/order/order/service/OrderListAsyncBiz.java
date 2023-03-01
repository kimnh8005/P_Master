package kr.co.pulmuone.v1.order.order.service;

import kr.co.pulmuone.v1.order.order.dto.OrderListRequestDto;

public interface OrderListAsyncBiz {

    /**
     * 주문상세 - 엑셀파일 생성
     *
     * @param orderListRequestDto    OrderListRequestDto
     * @param stExcelDownloadAsyncId Long
     */
    void runOrderDetailExcelMake(OrderListRequestDto orderListRequestDto, Long stExcelDownloadAsyncId);

}
