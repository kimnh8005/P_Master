package kr.co.pulmuone.v1.order.delivery.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import kr.co.pulmuone.v1.comm.CommonServiceTestBaseForJunit5;
import kr.co.pulmuone.v1.order.order.dto.vo.OrderShippingZoneHistVo;
import kr.co.pulmuone.v1.order.order.dto.vo.OrderShippingZoneVo;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ShippingZoneServiceTest extends CommonServiceTestBaseForJunit5 {

	@Autowired
	ShippingZoneService shippingZoneService;

	@Test
	void 주문_배송지_수정() {

    	// Request
		OrderShippingZoneVo orderShippingZoneVo = OrderShippingZoneVo.builder()
																	.odShippingZoneId(0)
																	.recvNm("홍길동")
																	.recvHp("01012345678")
																	.recvZipCd("01234")
																	.recvAddr1("주소1")
																	.recvAddr2("주소2")
																	.recvBldNo("123")
																	.deliveryMsg("배송요청사항")
																	.doorMsgCd("ACCESS_INFORMATION.FRONT_DOOR_PASSWORD")
																	.doorMsg("*1234")
																	.build();

    	// result
    	int updateCnt = shippingZoneService.putShippingZone(orderShippingZoneVo);

    	log.info("주문_배송지_수정 updateCnt : {}",  updateCnt);

    	// equals
    	Assertions.assertTrue(updateCnt > 0);
	}

	@Test
	void 주문_배송지_이력_등록() {

    	// Request
		OrderShippingZoneHistVo orderShippingZoneHistVo = OrderShippingZoneHistVo.builder()
																				.odShippingZoneId(0)
																				.deliveryType("DELIVERY_TYPE.NORMAL")
																				.shippingType(1)
																				.recvNm("홍길동")
																				.recvHp("01012345678")
																				.recvZipCd("01234")
																				.recvAddr1("주소1")
																				.recvAddr2("주소2")
																				.recvBldNo("123")
																				.deliveryMsg("배송요청사항")
																				.doorMsgCd("ACCESS_INFORMATION.FRONT_DOOR_PASSWORD")
																				.doorMsg("*1234")
																				.build();

    	// result
    	int insertCnt = shippingZoneService.addShippingZoneHist(orderShippingZoneHistVo);

    	log.info("주문_배송지_이력_등록 insertCnt : {}",  insertCnt);

    	// equals
    	Assertions.assertTrue(insertCnt > 0);
	}

	@Test
	void getOrderShippingZone_성공() throws Exception{
		Long odShippingZoneId = 1L;
		OrderShippingZoneVo orderShippingZoneVo = shippingZoneService.getOrderShippingZone(odShippingZoneId);

		Assertions.assertNotNull(orderShippingZoneVo.getOdShippingZoneId());
	}

	@Test
	void getOrderShippingZone_조회결과없음() {
		Long odShippingZoneId = 1111111L;
		OrderShippingZoneVo orderShippingZoneVo = shippingZoneService.getOrderShippingZone(odShippingZoneId);

		Assertions.assertNull(orderShippingZoneVo);
	}
}
