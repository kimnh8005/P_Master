package kr.co.pulmuone.v1.order.order.service;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import kr.co.pulmuone.v1.comm.CommonServiceTestBaseForJunit5;
import kr.co.pulmuone.v1.order.order.dto.OrderDetailListDto;
import kr.co.pulmuone.v1.order.order.dto.OrderDetailListResponseDto;
import kr.co.pulmuone.v1.order.order.dto.OrderListDto;
import kr.co.pulmuone.v1.order.order.dto.OrderListRequestDto;
import kr.co.pulmuone.v1.order.order.dto.OrderListResponseDto;
import lombok.extern.slf4j.Slf4j;

@Slf4j
class OrderListServiceTest extends CommonServiceTestBaseForJunit5 {

    @Autowired
    OrderListService orderListService;

    @BeforeEach
    void setUp(){
        preLogin();
    }

    @Test
    void 주문_목록_조회() {

    	// Request
    	OrderListRequestDto orderListRequestDto = new OrderListRequestDto();
    	orderListRequestDto.setPage(0);
    	orderListRequestDto.setPageSize(2);

    	// result
    	OrderListResponseDto orderGoodsList = orderListService.getOrderList(orderListRequestDto);
    	List<OrderListDto> orderList = orderGoodsList.getRows();

    	orderList.forEach(
            i -> log.info("주문_목록_조회  result : {}",  i)
        );

    	// equals
    	Assertions.assertTrue(orderList.size() > 0);
    }

    @Test
    void 주문상세_목록_조회() {
    	// Request
    	OrderListRequestDto orderListRequestDto = new OrderListRequestDto();
    	orderListRequestDto.setCsRefundApproveCdList(new ArrayList<>());
    	orderListRequestDto.setCsRefundTpList(new ArrayList<>());
    	orderListRequestDto.setOmSellersIdList(new ArrayList<>());
    	orderListRequestDto.setPaymentMethodCodeList(new ArrayList<>());
    	orderListRequestDto.setBuyerTypeCodeList(new ArrayList<>());
    	orderListRequestDto.setAgentTypeCodeList(new ArrayList<>());
    	orderListRequestDto.setDelivTypeList(new ArrayList<>());
    	orderListRequestDto.setOrderTypeCodeList(new ArrayList<>());
		orderListRequestDto.setPage(0);
		orderListRequestDto.setPageSize(2);

    	// result
    	OrderDetailListResponseDto orderDetailGoodsList = orderListService.getOrderDetailList(orderListRequestDto);
    	List<OrderDetailListDto> orderDetailList = orderDetailGoodsList.getRows();

    	orderDetailList.forEach(
            i -> log.info("주문상세_목록_조회  result : {}",  i)
        );

    	// equals
    	Assertions.assertTrue(orderDetailList.size() > 0);
    }
}