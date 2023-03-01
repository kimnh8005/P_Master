package kr.co.pulmuone.v1.order.order.service;

import kr.co.pulmuone.v1.comm.CommonServiceTestBaseForJunit5;
import kr.co.pulmuone.v1.order.order.dto.mall.*;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Objects;

@Slf4j
class MallOrderDetailServiceTest extends CommonServiceTestBaseForJunit5 {

    @Autowired
    MallOrderDetailService mallOrderDetailService;

    @Test
    void 주문정보_조회() {

    	// Request
    	long odOrderId = 21033;
    	String urUserId = "1646950";

    	// result
    	MallOrderDto orderMap = mallOrderDetailService.getOrder(odOrderId, urUserId, "");

		log.info("주문정보_조회 result : {}",  orderMap);

        // equals
        Assertions.assertNotNull(orderMap);
    }

    @Test
    void 주문상세_목록_조회() throws Exception {

    	// Request
    	long odOrderId = 21182;

    	// result
    	List<MallOrderDetailGoodsDto> orderDetlList = mallOrderDetailService.getOrderDetailGoodsList(odOrderId);

    	orderDetlList.stream().forEach(
            i -> log.info("주문상세_목록_조회 result : {}",  i)
        );

        // equals
        Assertions.assertTrue(orderDetlList.size() > 0);
    }

    @Test
    void 주문상세_배송지_목록_조회() throws Exception {

    	// Request
    	long odOrderId = 21182;

    	// result
    	MallOrderDetailShippingZoneDto orderShippingZoneInfo = mallOrderDetailService.getOrderDetailShippingInfo(odOrderId);

		log.info("주문상세_배송지_목록_조회 result : {}",  orderShippingZoneInfo);

        // equals
        Assertions.assertTrue(!Objects.isNull(orderShippingZoneInfo));
    }

    @Test
    void 주문상세_결제정보_조회() throws Exception {

    	// Request
    	long odOrderId = 1013;

    	// result
    	MallOrderDetailPayResultDto orderPaymentInfo = mallOrderDetailService.getOrderDetailPayInfo(odOrderId);

    	log.info("주문상세_결제정보_조회 result : {}",  orderPaymentInfo);

    	// equals
    	Assertions.assertTrue(!Objects.isNull(orderPaymentInfo));
    }

    @Test
    void 주문상세_주문취소_반품_신청내역_조회() throws Exception {

    	// Request
    	long odOrderId = 1013;

    	// result
    	List<MallOrderDetailClaimListDto> orderPaymentInfo = mallOrderDetailService.getOrderDetailClaimList(odOrderId);

    	orderPaymentInfo.stream().forEach(
            i -> log.info("주문상세_주문취소_반품_신청내역_조회  result : {}",  i)
        );

    	// equals
    	Assertions.assertTrue(!Objects.isNull(orderPaymentInfo));
    }
}