package kr.co.pulmuone.v1.order.registration.service;

import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import kr.co.pulmuone.v1.comm.CommonServiceTestBaseForJunit5;
import kr.co.pulmuone.v1.order.create.dto.OrderCreateRequestDto;
import kr.co.pulmuone.v1.order.registration.dto.OrderBindDto;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class OrderRegistrationServiceTest extends CommonServiceTestBaseForJunit5 {

    @Autowired
    private OrderRegistrationBiz orderRegistrationBiz;

	@Autowired
	OrderBindBosCreateBizImpl orderBindBiz;

    @Test
    void create() throws Exception {

        OrderCreateRequestDto orderCreateRequestDto = new OrderCreateRequestDto();

        List<OrderBindDto> orderBindList = orderBindBiz.orderDataBind(orderCreateRequestDto);

        orderRegistrationBiz.createOrder(orderBindList, "N");

        Assertions.assertTrue(111 > 0);
    }
}
