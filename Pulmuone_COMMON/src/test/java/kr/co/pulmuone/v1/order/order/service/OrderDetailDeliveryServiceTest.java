package kr.co.pulmuone.v1.order.order.service;

import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import kr.co.pulmuone.v1.comm.CommonServiceTestBaseForJunit5;
import kr.co.pulmuone.v1.order.order.dto.OrderDetailGoodsListDto;
import lombok.extern.slf4j.Slf4j;

@Slf4j
class OrderDetailDeliveryServiceTest extends CommonServiceTestBaseForJunit5 {

    @Autowired
    OrderDetailDeliveryService orderDetailDeliveryService;

    @Test
    void 주문상세_상품_목록_조회() throws Exception {

    	// Request
    	String odid = "1009";

    	// result
    	List<OrderDetailGoodsListDto> orderDetailGoodsList = orderDetailDeliveryService.getOrderDetailGoodsList(odid);

    	orderDetailGoodsList.stream().forEach(
            i -> log.info("주문상세_상품_목록_조회  result : {}",  i)
        );

    	// equals
    	Assertions.assertTrue(orderDetailGoodsList.size() > 0);
    }

    @Test
    void 주문상세_동일출고처_주문상세ID_목록_조회() {

    	// Request
    	long odOrderDetlId = 11;

    	// result
    	List<Long> odOrderDetlIdList = orderDetailDeliveryService.getUrWarehouseOdOrderDetlIdList(odOrderDetlId);

    	odOrderDetlIdList.forEach(
            i -> log.info("주문상세_동일출고처_주문상세ID_목록_조회  result : {}",  i)
        );

    	// equals
    	Assertions.assertTrue(odOrderDetlIdList.size() > 0);
    }

}