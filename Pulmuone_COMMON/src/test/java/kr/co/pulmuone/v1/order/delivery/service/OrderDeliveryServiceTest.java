package kr.co.pulmuone.v1.order.delivery.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import kr.co.pulmuone.v1.comm.CommonServiceTestBaseForJunit5;
import kr.co.pulmuone.v1.order.delivery.dto.vo.OrderTrackingNumberVo;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class OrderDeliveryServiceTest extends CommonServiceTestBaseForJunit5 {

	@Autowired
	OrderDeliveryService orderDeliveryService;

	@Test
	void 송장번호_등록_수정() {

    	// Request
		OrderTrackingNumberVo orderTrackingNumberVo = OrderTrackingNumberVo.builder()
																			.odOrderDetlId(17L)
																			.psShippingCompId(25L)
																			.trackingNo("123456")
																			.sort(1)
																			.createId(1646893L)
																			.build();

    	// result
    	int insertCnt = orderDeliveryService.addOrderDetailTrackingNumber(orderTrackingNumberVo);

    	log.info("송장번호_등록_수정 insertCnt : {}",  insertCnt);

    	// equals
    	Assertions.assertTrue(insertCnt > 0);
	}
}
