package kr.co.pulmuone.v1.order.order.service;

import kr.co.pulmuone.v1.comm.CommonServiceTestBaseForJunit5;
import kr.co.pulmuone.v1.order.order.dto.vo.OrderShopStoreVo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import static org.junit.jupiter.api.Assertions.assertEquals;

class OrderDetailServiceTest extends CommonServiceTestBaseForJunit5 {

    @Autowired
    OrderDetailService orderDetailService;

    @Test
    void 주문상_조회_정상() throws Exception {


        String odid = "2020121412345678";


//        Assertions.assertNull(orderDetailService.getOrder(odid));
    }

    @Test
    void 주문상_조회_없음() throws Exception {


        String odid = "111";


//        Assertions.assertNull(orderDetailService.getOrder(odid));
    }

    @Test
    void 주문번호PK로_매장배송픽업_스케쥴정보_조회() throws Exception {
        String odOrderId = "45473";

        OrderShopStoreVo vo = orderDetailService.getOrderShopStoreInfo(odOrderId);

        assertEquals(vo.getStoreScheduleNo(), 2L);

    }

}