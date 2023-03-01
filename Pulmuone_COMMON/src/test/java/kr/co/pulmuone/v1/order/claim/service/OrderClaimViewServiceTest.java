package kr.co.pulmuone.v1.order.claim.service;

import org.springframework.beans.factory.annotation.Autowired;

import kr.co.pulmuone.v1.comm.CommonServiceTestBaseForJunit5;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class OrderClaimViewServiceTest extends CommonServiceTestBaseForJunit5 {

    @Autowired
    OrderClaimViewServiceTest delOrderClaimViewService;

//    @Test
//    void 주문클레임신청_사유목록_조회() throws Exception {
//
//    	// Request
//
//    	// result
//    	List<OrderClaimReasonResponseDto> orderClaimReasonList = orderClaimViewService.getOrderClaimReasonList();
//
//    	orderClaimReasonList.stream().forEach(
//            i -> log.info("주문클레임신청_사유목록_조회 result : {}",  i)
//        );
//
//        // equals
//        Assertions.assertTrue(orderClaimReasonList.size() > 0);
//    }
//
//    @Test
//    void 주문클레임신청_BOS사유목록_조회() throws Exception {
//
//    	// Request
//
//    	// result
//    	List<OrderClaimBosReasonResponseDto> orderClaimReasonBOSList = orderClaimViewService.getOrderClaimBosReasonList();
//
//    	orderClaimReasonBOSList.stream().forEach(
//			i -> log.info("주문클레임신청_BOS사유목록_조회 result : {}",  i)
//		);
//
//    	// equals
//    	Assertions.assertTrue(orderClaimReasonBOSList.size() > 0);
//    }
//
//    @Test
//    void 주문클레임신청_공급업체사유목록_조회() throws Exception {
//
//    	// Request
//
//    	// result
//    	List<OrderClaimSupplyReasonResponseDto> orderClaimReasonSupplyList = orderClaimViewService.getOrderClaimSupplyReasonList();
//
//    	orderClaimReasonSupplyList.stream().forEach(
//			i -> log.info("주문클레임신청_공급업체사유목록_조회 result : {}",  i)
//		);
//
//    	// equals
//    	Assertions.assertTrue(orderClaimReasonSupplyList.size() > 0);
//    }
//
//    @Test
//    void 주문클레임신청_고객사유목록_조회() throws Exception {
//
//    	// Request
//    	OrderClaimViewRequestDto orderClaimViewRequestDto = new OrderClaimViewRequestDto();
//    	orderClaimViewRequestDto.setOdOrderId((long)1);
//
//    	// result
//    	OrderClaimCustomerReasonResponseDto orderClaimCustomerReason = orderClaimViewService.getOrderClaimCustomerReasonInfo(orderClaimViewRequestDto);
//
//    	log.info("주문클레임신청_고객사유목록_조회 result : {}",  orderClaimCustomerReason);
//
//    	// equals
//    	Assertions.assertTrue(!Objects.isNull(orderClaimCustomerReason));
//    }
//
//    @Test
//    void 주문클레임신청_상품목록_조회() throws Exception {
//
//    	// Request
//    	long odOrderId = 1;
//
//    	// result
//    	List<OrderClaimGoodsInfoDto> orderClaimGoodsList = orderClaimViewService.getOrderClaimGoodsInfoList(odOrderId);
//
//    	orderClaimGoodsList.stream().forEach(
//			i -> log.info("주문클레임신청_상품목록_조회 result : {}",  i)
//		);
//
//    	// equals
//    	Assertions.assertTrue(orderClaimGoodsList.size() > 0);
//    }
//
//    @Test
//    void 주문클레임신청_결제정보_조회() throws Exception {
//
//    	// Request
//    	long odOrderId = 1;
//
//    	// result
//    	OrderClaimPaymentInfoDto orderClaimPaymentInfo = orderClaimViewService.getOrderClaimPaymentInfo(odOrderId);
//
//    	log.info("주문클레임신청_결제정보_조회 result : {}",  orderClaimPaymentInfo);
//
//    	// equals
//    	Assertions.assertTrue(!Objects.isNull(orderClaimPaymentInfo));
//    }
//
//    @Test
//    void 주문클레임신청_상품환불정보_조회() throws Exception {
//
//    	// Request
//    	long odOrderId = 1;
//
//    	// result
//    	OrderClaimPriceInfoDto orderClaimPriceInfo = orderClaimViewService.getOrderClaimPriceInfo(odOrderId);
//
//    	log.info("주문클레임신청_상품환불정보_조회 result : {}",  orderClaimPriceInfo);
//
//    	// equals
//    	Assertions.assertTrue(!Objects.isNull(orderClaimPriceInfo));
//    }
//
//    @Test
//    void 주문클레임신청_쿠폰목록_조회() throws Exception {
//
//    	// Request
///*
//    	OrderClaimViewRequestDto orderClaimViewRequestDto = new OrderClaimViewRequestDto();
//    	orderClaimViewRequestDto.setOdOrderId((long)1);
//
//    	// result
//    	List<OrderClaimCouponInfoDto> orderClaimCouponList = orderClaimViewService.getOrderClaimCouponInfoList(orderClaimViewRequestDto);
//*/
//    	OrderClaimViewRequestDto dto = new OrderClaimViewRequestDto();
//    	dto.setOdOrderId(1);
//
//    	// result
//    	List<OrderClaimCouponInfoDto> orderClaimCouponList = orderClaimViewService.getOrderClaimCouponInfoList(dto);
//
//
//    	orderClaimCouponList.stream().forEach(
//			i -> log.info("주문클레임신청_쿠폰목록_조회 result : {}",  i)
//		);
//
//    	// equals
//    	Assertions.assertTrue(orderClaimCouponList.size() > 0);
//    }
//
//    @Test
//    void 주문클레임신청_상품수량변경_환불정보_조회() throws Exception {
//
//    	// Request
//    	OrderClaimViewRequestDto orderClaimViewRequestDto = new OrderClaimViewRequestDto();
//
//    	// result
//    	OrderClaimPriceInfoDto orderClaimPriceInfo = orderClaimViewService.getOrderClaimGoodsChangePriceInfo(orderClaimViewRequestDto);
//
//    	log.info("주문클레임신청_상품수량변경_환불정보_조회 result : {}",  orderClaimPriceInfo);
//
//    	// equals
//    	Assertions.assertTrue(!Objects.isNull(orderClaimPriceInfo));
//    }
}
