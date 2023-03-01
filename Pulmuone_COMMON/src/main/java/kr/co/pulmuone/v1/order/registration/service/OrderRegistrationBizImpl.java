package kr.co.pulmuone.v1.order.registration.service;


import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.enums.GoodsEnums;
import kr.co.pulmuone.v1.comm.enums.OrderEnums;
import kr.co.pulmuone.v1.comm.enums.OrderScheduleEnums;
import kr.co.pulmuone.v1.comm.enums.ShoppingEnums;
import kr.co.pulmuone.v1.comm.enums.OrderEnums.OrderRegistrationResult;
import kr.co.pulmuone.v1.comm.util.DateUtil;
import kr.co.pulmuone.v1.comm.util.StringUtil;
import kr.co.pulmuone.v1.order.order.dto.vo.*;
import kr.co.pulmuone.v1.order.present.service.OrderPresentBiz;
import kr.co.pulmuone.v1.order.registration.dto.*;
import kr.co.pulmuone.v1.order.schedule.dto.OrderDetailScheduleDayOfWeekListDto;
import kr.co.pulmuone.v1.order.schedule.dto.OrderDetailScheduleUpdateRequestDto;
import kr.co.pulmuone.v1.order.schedule.service.OrderScheduleBiz;
import kr.co.pulmuone.v1.order.status.service.OrderStatusService;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * <PRE>
 * Forbiz Korea
 * 주문 데이터 생성 관련 Implements
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :   작성일                :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 *  1.0    2020. 12. 22.            이명수         최초작성
 * =======================================================================
 * </PRE>
 */
@Slf4j
@Service
public class OrderRegistrationBizImpl implements OrderRegistrationBiz {

    @Autowired
    private OrderRegistrationService orderRegistrationService;

    @Autowired
    private OrderRegistrationSeqBiz orderRegistrationSeqBiz;

    @Autowired
    private OrderStatusService orderStatusService;

    @Autowired
	private OrderScheduleBiz orderScheduleBiz;

    @Autowired
    private OrderPresentBiz orderPresentBiz;

    /**
     * 주문서 생성
     * @param orderBindList
     * @return
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class, readOnly = false)
    public OrderRegistrationResponseDto createOrder(List<OrderBindDto> orderBindList, String paymentApprovalYn) {
        return getOrderRegistrationResponseDto(orderBindList, paymentApprovalYn);
    }

    @Override
    public OrderRegistrationResponseDto createOrderNonTransaction(List<OrderBindDto> orderBindList, String paymentApprovalYn) {
        return getOrderRegistrationResponseDto(orderBindList, paymentApprovalYn);
    }

    private OrderRegistrationResponseDto getOrderRegistrationResponseDto(List<OrderBindDto> orderBindList, String paymentApprovalYn) {
        // validation
        OrderRegistrationResult validation = orderRegistrationService.validationOrder(orderBindList);
        if (!OrderRegistrationResult.SUCCESS.equals(validation)) {

            OrderRegistrationResponseDto orderRegistrationResult = OrderRegistrationResponseDto.builder()
                    .orderRegistrationResult(validation).build();
            return orderRegistrationResult;
        }

        // 주문 생성 처리
        return insertOrder(orderBindList, paymentApprovalYn);
    }

    /**
     * 주문서 생성 처리
     * @param orderBindList
     * @return
     */
    private OrderRegistrationResponseDto insertOrder(List<OrderBindDto> orderBindList, String paymentApprovalYn) {

        StringBuffer sbOdid = new StringBuffer();
        StringBuffer sbOdOrderId = new StringBuffer();
        OrderEnums.OrderRegistrationResult resutlCode = OrderEnums.OrderRegistrationResult.SUCCESS;
        String odid                 = "";
        long odOrderId              = 0;
        long odShippingZoneId       = 0;
        long odShippingPriceId      = 0;
        long odOrderDetlId          = 0;
        long odOrderDetlParentId    = 0;
        long prevOdOrderDetlParentId    = 0;
        long odOrderStepId          = 0;
        long odOrderDepthId         = 0;
        long odOrderDetlDailyId     = 0;
        int succCnt                 = 0;
        int failCnt                 = 0;

        int salePrice               = 0;
        int cartCouponPrice         = 0;
        int goodsCouponPrice        = 0;
        int directPrice             = 0;
        int paidPrice               = 0;
        int shippingPrice           = 0;
        int taxablePrice            = 0;
        int nonTaxablePrice         = 0;
        int paymentPrice            = 0;
        int pointPrice              = 0;

        String promotionTp = "";
        int totalPaidPrice          = 0;
        int totalShippingPrice      = 0;
        int totalTaxablePrice       = 0;
        int totalNonTaxablePrice    = 0;
        int totalPaymentPrice       = 0;
        int totalPointPrice         = 0;

        int resultTotalPaidPrice          = 0;
        int resultTotalShippingPrice      = 0;
        int resultTotalTaxablePrice       = 0;
        int resultTotalNonTaxablePrice    = 0;
        int resultTotalPaymentPrice       = 0;
        int resultTotalPointPrice         = 0;
        int resultTotalDirectPrice        = 0;

        // 결제 마스터 SEQ 조회
        long odPaymentMasterId = 0;
        if ("N".equals(StringUtil.nvl(paymentApprovalYn, "N"))) {
            odPaymentMasterId = orderRegistrationSeqBiz.getPaymentMasterSeq();
        }
        List<List<OrderOutmallMemoDto>> memoList = new ArrayList<>();
        for (OrderBindDto item:orderBindList) {
            try {
                AtomicInteger odOrderDetlSeq = new AtomicInteger(0);
                salePrice = 0;
                cartCouponPrice = 0;
                goodsCouponPrice = 0;
                directPrice = 0;
                paidPrice = 0;
                shippingPrice = 0;
                taxablePrice = 0;
                nonTaxablePrice = 0;
                paymentPrice = 0;
                pointPrice = 0;

                totalPaidPrice          = 0;
                totalShippingPrice      = 0;
                totalTaxablePrice       = 0;
                totalNonTaxablePrice    = 0;
                totalPaymentPrice       = 0;
                totalPointPrice         = 0;

                if ("Y".equals(StringUtil.nvl(paymentApprovalYn, "N"))) {
                    odPaymentMasterId = orderRegistrationSeqBiz.getPaymentMasterSeq();
                }

                memoList.add(item.getMemoList());
                OrderVo orderVo = item.getOrder();
                List<OrderBindShippingZoneDto> orderShippingZoneList = item.getOrderShippingZoneList().stream()
                        //.sorted(Comparator.comparing(OrderBindShippingZoneDto::getShippingZoneSeq, Comparator.reverseOrder()))
                        .collect(Collectors.toList());

                // #001: 주문.OD_ORDER
                // #001.01: 주문 SEQ 조회
                odOrderId = orderRegistrationSeqBiz.getOrderIdSeq();

                // #001.02: 주문번호생성
                odid = orderRegistrationSeqBiz.getOrderNumber(odOrderId);
                sbOdid.append("," + odid);
                sbOdOrderId.append("," + odOrderId);
                orderVo.setOdOrderId(odOrderId);
                orderVo.setOdid(odid);
                orderRegistrationService.addOrder(orderVo);

                // #002: 주문일자.OD_ORDER_DT
                OrderDtVo orderDtVo = item.getOrderDt();//OrderDtVo.builder().odOrderId(odOrderId).irId(0).ibId(0).ibId(0).build();
                orderDtVo.setOdOrderId(odOrderId);
                orderRegistrationService.addOrderDt(orderDtVo);

                // 주문 선물하기 생성
                OrderPresentVo orderPresentVo = item.getOrderPresentVo();
				if ("Y".equals(orderVo.getGiftYn())) {
	                orderPresentVo.setOdOrderId(odOrderId);
					orderPresentVo.setPresentId(orderPresentBiz.makePresentId(odOrderId));
	                orderRegistrationService.addOrderPresent(orderPresentVo);
                }


                for (OrderBindShippingZoneDto shippingZoneItem : orderShippingZoneList) {
                    OrderShippingZoneVo orderShippingZoneVo = shippingZoneItem.getOrderShippingZoneVo();

                    // #003.01: 주문배송지.OD_SHIPPING_ZONE
                    odShippingZoneId = orderRegistrationSeqBiz.getOrderShippingZoneSeq();
                    // #003.02: 주문배송지.OD_SHIPPING_ZONE


                    orderShippingZoneVo.setOdShippingZoneId(odShippingZoneId);
                    orderShippingZoneVo.setOdOrderId(odOrderId);

                    orderRegistrationService.addShippingZone(orderShippingZoneVo);


                    // #003.03: 주문배송지이력.OD_SHIPPING_ZONE_HIST
                    OrderShippingZoneHistVo orderShippingZoneHistVo = OrderShippingZoneHistVo.builder()
                            .odShippingZoneId(odShippingZoneId)
                            .odOrderId(odOrderId)
                            .deliveryType(orderShippingZoneVo.getDeliveryType())
                            .shippingType(orderShippingZoneVo.getShippingType())
                            .recvNm(orderShippingZoneVo.getRecvNm())
                            .recvHp(orderShippingZoneVo.getRecvHp())
                            .recvTel(orderShippingZoneVo.getRecvTel())
                            .recvMail(orderShippingZoneVo.getRecvMail())
                            .recvZipCd(orderShippingZoneVo.getRecvZipCd())
                            .recvAddr1(orderShippingZoneVo.getRecvAddr1())
                            .recvAddr2(orderShippingZoneVo.getRecvAddr2())
                            .recvBldNo(orderShippingZoneVo.getRecvBldNo())
                            .deliveryMsg(orderShippingZoneVo.getDeliveryMsg())
                            .doorMsgCd(orderShippingZoneVo.getDoorMsgCd())
                            .doorMsg(orderShippingZoneVo.getDoorMsg())
                            .build();
                    orderRegistrationService.addShippingZoneHist(orderShippingZoneHistVo);


                    List<OrderBindShippingPriceDto> shippingPriceList = shippingZoneItem.getShippingPriceList().stream()
                            .sorted(Comparator.comparing(OrderBindShippingPriceDto::getShippingPriceSeq, Comparator.reverseOrder()))
                            .collect(Collectors.toList());


                    for (OrderBindShippingPriceDto shippingPriceItem : shippingPriceList) {

                        OrderShippingPriceVo orderShippingPriceVo = shippingPriceItem.getOrderShippingPriceVo();

                        // #003.04: 주문배송비.OD_SHIPPING_PRICE SEQ 조회
                        odShippingPriceId = orderRegistrationSeqBiz.getOrderShippingPriceSeq();

                        orderShippingPriceVo.setOdShippingPriceId(odShippingPriceId);
                        orderRegistrationService.addShippingPrice(orderShippingPriceVo);

                        //List<OrderBindOrderDetlDto> orderDetlList = shippingPriceItem.getOrderDetlList();
                        List<OrderBindOrderDetlDto> orderDetlList = (shippingPriceItem.getOrderDetlList().stream()
                                .sorted(Comparator.comparing(OrderBindOrderDetlDto::getDetlSeq, Comparator.reverseOrder()))
                                .collect(Collectors.toList()));


                        for (OrderBindOrderDetlDto orderDetlItem : orderDetlList) {
                            OrderDetlVo orderDetlVo = orderDetlItem.getOrderDetlVo();
                            // #003.06: 주문상세.OD_ORDER_DETL SEQ 조회
                            odOrderDetlId = orderRegistrationSeqBiz.getOrderDetlSeq();
                            odOrderDetlParentId = odOrderDetlId;
                            odOrderStepId = (odOrderDetlId * 1000) + 999;
                            odOrderDepthId = 1;

                            //if (GoodsEnums.GoodsType.ADDITIONAL.getCode().equals(orderDetlVo.getGoodsTpCd())) {
                                //odOrderDepthId = 2;
                                //odOrderDetlParentId = prevOdOrderDetlParentId;
                            //}
                            prevOdOrderDetlParentId = odOrderDetlParentId;
                            if (!GoodsEnums.GoodsType.PACKAGE.getCode().equals(orderDetlVo.getGoodsTpCd())) {
                                orderDetlVo.setOdOrderDetlId(odOrderDetlId);
                                orderDetlVo.setOdOrderDetlSeq(odOrderDetlSeq.getAndIncrement());
                                orderDetlVo.setOdOrderId(odOrderId);
                                orderDetlVo.setOdid(odid);
                                orderDetlVo.setOdOrderDetlStepId(odOrderStepId);
                                orderDetlVo.setOdOrderDetlDepthId(odOrderDepthId);
                                orderDetlVo.setOdOrderDetlParentId(odOrderDetlParentId);
                                orderDetlVo.setOdShippingZoneId(odShippingZoneId);
                                orderDetlVo.setOdShippingPriceId(odShippingPriceId);
                                orderDetlVo.setCreateDt(DateUtil.getCurrentDate());




                                orderRegistrationService.addOrderDetl(orderDetlVo);

                                // #003.08: 주문상세 할인정보
                                if (orderDetlItem.getOrderDetlDiscountList() != null && orderDetlItem.getOrderDetlDiscountList().size() > 0) {
                                    for (OrderDetlDiscountVo orderDetlDiscountItem : orderDetlItem.getOrderDetlDiscountList()) {
                                        orderDetlDiscountItem.setOdOrderId(odOrderId);
                                        orderDetlDiscountItem.setOdOrderDetlId(odOrderDetlId);
                                        orderRegistrationService.addOrderDetlDiscount(orderDetlDiscountItem);
                                    }
                                }

                                if (orderDetlItem.getOrderDetlDailyVo() != null) {
                                    OrderDetlDailyVo orderDetlDailyVo = orderDetlItem.getOrderDetlDailyVo();
                                    orderDetlDailyVo.setOdOrderDetlDailyId(odOrderDetlDailyId);
                                    orderDetlDailyVo.setOdOrderId(odOrderId);
                                    orderDetlDailyVo.setOdOrderDetlSeq(odOrderDetlSeq.get());
                                    orderDetlDailyVo.setOdOrderDetlId(odOrderDetlId);
                                    orderDetlDailyVo.setDailyPackYn("N");
                                    this.setOrderDetlDaily(orderDetlVo, orderDetlDailyVo);
                                }
                            }


                            // 패키지 구성 상품 저장
                            if (orderDetlItem.getOrderDetlPackList() != null && orderDetlItem.getOrderDetlPackList().size() > 0) {
                                // 패키지 메인 상품인 경우에만 저장
                                if (GoodsEnums.GoodsType.PACKAGE.getCode().equals(orderDetlVo.getGoodsTpCd())) {
                                    OrderDetlPackVo orderDetlPackVo = OrderDetlPackVo.builder()
                                            .odOrderDetlId(odOrderDetlId)
                                            .odOrderDetlStepId(odOrderStepId)
                                            .odOrderId(odOrderId)
                                            .goodsTpCd(orderDetlVo.getGoodsTpCd())
                                            .goodsDeliveryType(orderDetlVo.getGoodsDeliveryType())
                                            .promotionTp(orderDetlVo.getPromotionTp())
                                            .ilGoodsId(orderDetlVo.getIlGoodsId())
                                            .goodsNm(orderDetlVo.getGoodsNm())
                                            .orderCnt(orderDetlVo.getOrderCnt())
                                            .standardPrice(orderDetlVo.getStandardPrice())
                                            .recommendedPrice(orderDetlVo.getRecommendedPrice())
                                            .salePrice(orderDetlVo.getSalePrice())
                                            .cartCouponPrice(orderDetlVo.getCartCouponPrice())
                                            .goodsCouponPrice(orderDetlVo.getGoodsCouponPrice())
                                            .directPrice(orderDetlVo.getDirectPrice())
                                            .paidPrice(orderDetlVo.getPaidPrice())
                                            .build();
                                    orderRegistrationService.addOrderDetlPack(orderDetlPackVo);
                                }

                                // 내맘대로 골라담기
                                if (GoodsEnums.GoodsDeliveryType.DAILY.getCode().equals(orderDetlVo.getGoodsDeliveryType())
                                        && ShoppingEnums.CartPromotionType.GREENJUICE_SELECT.getCode().equals(orderDetlVo.getPromotionTp())
                                ) {
                                    OrderDetlDailyVo orderDetlDailyVo = orderDetlItem.getOrderDetlDailyVo();
                                    orderDetlDailyVo.setOdOrderId(odOrderId);
                                    orderDetlDailyVo.setOdOrderDetlSeq(odOrderDetlSeq.get());
                                    orderDetlDailyVo.setOdOrderDetlId(odOrderDetlId);
                                    orderDetlDailyVo.setDailyPackYn("Y");
                                    orderDetlVo.setOdShippingZoneId(odShippingZoneId);
                                    this.setOrderDetlDaily(orderDetlVo, orderDetlDailyVo);

                                    promotionTp = orderDetlVo.getPromotionTp();
                                }


                                // #003.07: 주문상세 묶음상품 / 일일배송 골라담기
                                orderDetlItem.getOrderDetlPackList().stream().sorted(Comparator.comparing(OrderBindOrderDetlPackDto::getDetlSeq).reversed());
                                for (OrderBindOrderDetlPackDto orderDetlPackItem : orderDetlItem.getOrderDetlPackList()) {

                                    OrderDetlVo orderDetlPackGoodsVo = orderDetlPackItem.getOrderDetlVo();

                                    odOrderDetlId = orderRegistrationSeqBiz.getOrderDetlSeq();

                                    odOrderDepthId = 2;
                                    orderDetlPackGoodsVo.setOdOrderDetlId(odOrderDetlId);
                                    orderDetlPackGoodsVo.setOdOrderDetlSeq(odOrderDetlSeq.getAndIncrement());
                                    orderDetlPackGoodsVo.setOdOrderId(odOrderId);
                                    orderDetlPackGoodsVo.setOdid(odid);
                                    orderDetlPackGoodsVo.setOdOrderDetlStepId(--odOrderStepId);
                                    orderDetlPackGoodsVo.setOdOrderDetlDepthId(odOrderDepthId);
                                    orderDetlPackGoodsVo.setOdOrderDetlParentId(odOrderDetlParentId);
                                    orderDetlPackGoodsVo.setOdShippingZoneId(odShippingZoneId);
                                    orderDetlPackGoodsVo.setOdShippingPriceId(odShippingPriceId);
                                    orderDetlVo.setCreateDt(DateUtil.getCurrentDate());


                                    orderRegistrationService.addOrderDetl(orderDetlPackGoodsVo);

                                    //주문상세 일일배송 패턴 정보
    								if (orderDetlPackItem.getOrderDetlDailyVo() != null && orderDetlPackGoodsVo.getGoodsDailyTp().equals(GoodsEnums.GoodsDailyType.GREENJUICE.getCode())) {
                                        OrderDetlDailyVo orderDetlDailyVo = orderDetlPackItem.getOrderDetlDailyVo();
                                        orderDetlDailyVo.setOdOrderId(odOrderId);
                                        orderDetlDailyVo.setOdOrderDetlSeq(odOrderDetlSeq.get());
                                        orderDetlDailyVo.setOdOrderDetlId(odOrderDetlId);
                                        orderDetlDailyVo.setDailyPackYn("N");
                                        this.setOrderDetlDaily(orderDetlVo, orderDetlDailyVo);

                                	}

                                    // #003.08: 주문상세 할인정보
                                    if (orderDetlPackItem.getOrderDetlDiscountList() != null && orderDetlPackItem.getOrderDetlDiscountList().size() > 0) {
                                        for (OrderDetlDiscountVo orderDetlDiscountItem : orderDetlPackItem.getOrderDetlDiscountList()) {
                                            orderDetlDiscountItem.setOdOrderId(odOrderId);
                                            orderDetlDiscountItem.setOdOrderDetlId(odOrderDetlId);
                                            orderRegistrationService.addOrderDetlDiscount(orderDetlDiscountItem);
                                        }
                                    }

                                }
                            } else {


                            }
                        }
                    }
                }


                OrderPaymentVo orderPaymentVo = item.getOrderPaymentVo();
                orderPaymentVo.setOdPaymentMasterId(odPaymentMasterId);
                orderPaymentVo.setOdOrderId(odOrderId);
                orderPaymentVo.setOdClaimId(0);
                orderRegistrationService.addPayment(orderPaymentVo);

				orderRegistrationService.putOrderDetlSeq(odOrderId);

				orderRegistrationService.putOrderDetlDailySeq(odOrderId);

                // 일일배송 배송지 등록
                orderRegistrationService.addOrderDetlDailyZone(odOrderId, promotionTp);



                salePrice = salePrice + StringUtil.nvlInt(orderPaymentVo.getSalePrice());
                cartCouponPrice = cartCouponPrice + StringUtil.nvlInt(orderPaymentVo.getCartCouponPrice());
                goodsCouponPrice = goodsCouponPrice + StringUtil.nvlInt(orderPaymentVo.getGoodsCouponPrice());
                directPrice = directPrice + StringUtil.nvlInt(orderPaymentVo.getDirectPrice());
                paidPrice = paidPrice + StringUtil.nvlInt(orderPaymentVo.getPaidPrice());
                shippingPrice = shippingPrice + StringUtil.nvlInt(orderPaymentVo.getShippingPrice());
                taxablePrice = taxablePrice + StringUtil.nvlInt(orderPaymentVo.getTaxablePrice());
                nonTaxablePrice = nonTaxablePrice + StringUtil.nvlInt(orderPaymentVo.getNonTaxablePrice());
                paymentPrice = paymentPrice + StringUtil.nvlInt(orderPaymentVo.getPaymentPrice());
                pointPrice = pointPrice + StringUtil.nvlInt(orderPaymentVo.getPointPrice());
                totalPaidPrice += paidPrice;
                totalShippingPrice += shippingPrice;
                totalTaxablePrice  += taxablePrice;
                totalNonTaxablePrice  += nonTaxablePrice;
                totalPaymentPrice += paymentPrice;
                totalPointPrice += pointPrice;


                resultTotalPaidPrice          += paidPrice;
                resultTotalShippingPrice      += shippingPrice;
                resultTotalTaxablePrice       += taxablePrice;
                resultTotalNonTaxablePrice    += nonTaxablePrice;
                resultTotalPaymentPrice       += paymentPrice;
                resultTotalPointPrice         += pointPrice;
                resultTotalDirectPrice        += directPrice;
                // 결제 마스터 등록
                if ("Y".equals(StringUtil.nvl(paymentApprovalYn, "N"))) {
                    OrderPaymentMasterVo orderPaymentMasterVo = OrderPaymentMasterVo.builder()
                            .odPaymentMasterId(odPaymentMasterId)
                            .type("G") // TODO : Enum 으로 선언된거로 수정 해야
                            .payTp(OrderEnums.PaymentType.COLLECTION.getCode())
                            .escrowYn("N")
                            .status(OrderEnums.OrderStatus.INCOM_COMPLETE.getCode())
                            .salePrice(salePrice)
                            .cartCouponPrice(cartCouponPrice)
                            .goodsCouponPrice(goodsCouponPrice)
                            .directPrice(directPrice)
                            .paidPrice(totalPaidPrice)
                            .shippingPrice(totalShippingPrice)
                            .taxablePrice(totalTaxablePrice)
                            .nonTaxablePrice(totalNonTaxablePrice)
                            .paymentPrice(totalPaymentPrice)
                            .pointPrice(totalPointPrice)
                            .approvalDt(LocalDateTime.now())
                            .build();

                    orderRegistrationService.addPaymentMaster(orderPaymentMasterVo);
                }


                OrderAccountVo orderAccountVo = item.getOrderAccount();
				if (orderAccountVo != null) {
					orderAccountVo.setOdOrderId(odOrderId);
					orderRegistrationService.addAccount(orderAccountVo);
                }



                succCnt++;
            } catch (Exception e){
            	System.out.println(e);
                failCnt++;
            }
        }
        if ("N".equals(StringUtil.nvl(paymentApprovalYn, "N"))){
        // 결제 마스터 등록
        OrderPaymentMasterVo orderPaymentMasterVo = OrderPaymentMasterVo.builder()
                .odPaymentMasterId(odPaymentMasterId)
                .type(OrderEnums.PayType.G.getCode())
                .escrowYn("N")
                .status(OrderEnums.OrderStatus.INCOM_READY.getCode())
                .salePrice(salePrice)
                .cartCouponPrice(cartCouponPrice)
                .goodsCouponPrice(goodsCouponPrice)
                .directPrice(resultTotalDirectPrice)
                .paidPrice(resultTotalPaidPrice)
                .shippingPrice(resultTotalShippingPrice)
                .taxablePrice(resultTotalTaxablePrice)
                .nonTaxablePrice(resultTotalNonTaxablePrice)
                .paymentPrice(resultTotalPaymentPrice)
                .pointPrice(resultTotalPointPrice)
                .approvalDt(null)
                .build();


        orderRegistrationService.addPaymentMaster(orderPaymentMasterVo);
        }



        if (failCnt > 0){ resutlCode = OrderEnums.OrderRegistrationResult.FAIL; }

        OrderRegistrationResponseDto orderRegistrationResponseDto = OrderRegistrationResponseDto.builder()
                .orderRegistrationResult(resutlCode)
                .odids(sbOdid.substring(1))
                .odPaymentMasterId(odPaymentMasterId)
//                .paymentPrice(totalPaymentPrice)
                .paymentPrice(resultTotalPaymentPrice)
                .odOrderIds(sbOdOrderId.substring(1))
                .succCnt(succCnt)
                .failCnt(failCnt)
                .memoList(memoList)
                .build();
        return orderRegistrationResponseDto;
    }

    /**
     * 주문서 직접결제 승인
     * @param orderApprovalDto
     * @return
     */
    @Override
    public OrderRegistrationResponseDto approvalDirectOrder(OrderApprovalDto orderApprovalDto) {
    	OrderEnums.OrderRegistrationResult resutlCode = OrderEnums.OrderRegistrationResult.SUCCESS;
    	OrderVo orderVo                             = orderApprovalDto.getOrder();

    	orderRegistrationService.putApprovalOrder(orderVo);

    	OrderRegistrationResponseDto orderRegistrationResponseDto = OrderRegistrationResponseDto.builder()
                .orderRegistrationResult(resutlCode)
                .odids("")
                .succCnt(0)
                .failCnt(0)
                .build();

        return orderRegistrationResponseDto;
    }

    /**
     * 주문서 승인 처리
     * @param orderApprovalDto
     * @return
     */
    @Override
    //@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public OrderRegistrationResponseDto approvalOrder(OrderApprovalDto orderApprovalDto) {
        OrderEnums.OrderRegistrationResult resutlCode = OrderEnums.OrderRegistrationResult.SUCCESS;

        OrderVo orderVo                             = orderApprovalDto.getOrder();
        OrderDetlVo orderDetlVo                     = orderApprovalDto.getOrderDetlVo();
        OrderPaymentMasterVo orderPaymentMasterVo   = orderApprovalDto.getOrderPaymentMasterVo();
        OrderCashReceiptVo orderCashReceiptVo   	= orderApprovalDto.getOrderCashReceipt();

        orderRegistrationService.putApprovalOrder(orderVo);
        orderRegistrationService.putApprovalOrderDetl(orderDetlVo);
        orderRegistrationService.putApprovalPaymentMaster(orderPaymentMasterVo);
        orderRegistrationService.putApprovalOrderDtIr(orderDetlVo.getOdOrderId());
		if (OrderEnums.OrderStatus.INCOM_COMPLETE.getCode().equals(orderPaymentMasterVo.getStatus())) {
			orderRegistrationService.putApprovalOrderDtIc(orderVo.getOdOrderId());
		}

		OrderDetlHistVo orderDetlHistVo = OrderDetlHistVo.builder()
                .odOrderId(orderDetlVo.getOdOrderId())
                .statusCd(orderPaymentMasterVo.getStatus())
                .histMsg("-")
                .createId(0)
                .build();
        // TODO : 2021.05.19 확인필요
        orderRegistrationService.putOrderDetailStatusHist(orderDetlHistVo);

		if (orderCashReceiptVo != null) {
			orderRegistrationService.addOrderCashReceipt(orderCashReceiptVo);
		}

        OrderRegistrationResponseDto orderRegistrationResponseDto = OrderRegistrationResponseDto.builder()
                .orderRegistrationResult(resutlCode)
                .odids("")
                .succCnt(0)
                .failCnt(0)
                .build();



        return orderRegistrationResponseDto;
    }

    /**
     * 주문서 결제 승인 처리
     * @param orderApprovalDto
     * @return
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public OrderRegistrationResponseDto payApprovalOrder(OrderApprovalDto orderApprovalDto) {
        OrderEnums.OrderRegistrationResult resutlCode = OrderEnums.OrderRegistrationResult.SUCCESS;

        OrderDetlVo orderDetlVo                     = orderApprovalDto.getOrderDetlVo();
        OrderPaymentMasterVo orderPaymentMasterVo   = orderApprovalDto.getOrderPaymentMasterVo();
        OrderCashReceiptVo orderCashReceiptVo   	= orderApprovalDto.getOrderCashReceipt();

        orderRegistrationService.putApprovalOrderDetl(orderDetlVo);
        orderRegistrationService.putPayApprovalPaymentMaster(orderPaymentMasterVo);
        orderRegistrationService.putApprovalOrderDtIc(orderDetlVo.getOdOrderId());
        if (orderCashReceiptVo != null) {
			orderRegistrationService.addOrderCashReceipt(orderCashReceiptVo);
		}
        OrderDetlHistVo orderDetlHistVo = OrderDetlHistVo.builder()
                .odOrderId(orderDetlVo.getOdOrderId())
                .statusCd(orderPaymentMasterVo.getStatus())
                .histMsg("-")
                .createId(0)
                .build();

        orderStatusService.putOrderDetailStatusHist(orderDetlHistVo);

        OrderRegistrationResponseDto orderRegistrationResponseDto = OrderRegistrationResponseDto.builder()
                .orderRegistrationResult(resutlCode)
                .odids("")
                .succCnt(0)
                .failCnt(0)
                .build();

        return orderRegistrationResponseDto;
    }

    /**
     * 주문 상세번호 생성
     * @return
     */
    public long getOrderDetlSeq() {
    	return orderRegistrationSeqBiz.getOrderDetlSeq();
    }

    /**
     * 주문상세 등록
     * @param orderDetlVo
     * @return
     */
    @Override
    public int addOrderDetl(OrderDetlVo orderDetlVo) {
    	return orderRegistrationService.addOrderDetl(orderDetlVo);
    }

    /**
     * 주문
     * OD_ORDER.OD_ORDER_ID PK 조회
     * @return
     */
	@Override
	public long getOrderIdSeq() {
		return orderRegistrationSeqBiz.getOrderIdSeq();
	}

	/**
     * 주문번호 생성
     * @return
     */
	@Override
	public String getOrderNumber(long odOrderId) {
		return orderRegistrationSeqBiz.getOrderNumber(odOrderId);
	}

	@Override
	public void setOrderDetlDaily(OrderDetlVo orderDetlVo, OrderDetlDailyVo orderDetlDailyVo) throws Exception {
        long odOrderDetlDailyId = orderRegistrationSeqBiz.getOrderDetlDailySeq();

        orderDetlDailyVo.setOdOrderDetlDailyId(odOrderDetlDailyId);


        orderRegistrationService.addOrderDetlDaily(orderDetlDailyVo);


        if ("N".equals(orderDetlDailyVo.getDailyPackYn()) && !"Y".equals(orderDetlDailyVo.getDailyBulkYn())) {
            // 배송 주기
            GoodsEnums.GoodsCycleTermType goodsCycleTermTp = GoodsEnums.GoodsCycleTermType.findByCode(orderDetlDailyVo.getGoodsCycleTermTp());
            int goodsCycleTermTpWeekNum = StringUtil.nvlInt(goodsCycleTermTp.getTypeQty());

            // 배송 요일
            //GoodsEnums.GoodsCycleType goodsCycleTypeEnums = GoodsEnums.GoodsCycleType.findByCode(orderDetlDailyVo.getGoodsCycleTp());
            int goodsCycleTypeDayNum = ((orderDetlDailyVo.getMonCnt() > 0)? 1:0)
                                        + ((orderDetlDailyVo.getTueCnt()  > 0)? 1:0)
                                        + ((orderDetlDailyVo.getWedCnt()  > 0)? 1:0)
                                        + ((orderDetlDailyVo.getThuCnt()  > 0)? 1:0)
                                        + ((orderDetlDailyVo.getFriCnt() > 0)? 1:0)
                                        ; //StringUtil.nvlInt(goodsCycleTypeEnums.getTypeQty()) + 1;

            // 배송 횟수
            int deliveryCnt = goodsCycleTermTpWeekNum * goodsCycleTypeDayNum;//goodsCycleTermTpWeekNum  * goodsCycleTypeDayNum;
            String deliverableDate = orderDetlVo.getDeliveryDt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            List<String> deliveryDayOfWeekList = new ArrayList<String>();

            int dailyOrderCnt = 0;
            if (orderDetlDailyVo.getMonCnt() > 0) {
                deliveryDayOfWeekList.add(String.valueOf(GoodsEnums.WeekCodeByGreenJuice.MON.getDayNum()));
                dailyOrderCnt += orderDetlDailyVo.getMonCnt();
            }
            if (orderDetlDailyVo.getTueCnt() > 0) {
                deliveryDayOfWeekList.add(String.valueOf(GoodsEnums.WeekCodeByGreenJuice.TUE.getDayNum()));
                dailyOrderCnt += orderDetlDailyVo.getTueCnt();
            }
            if (orderDetlDailyVo.getWedCnt() > 0) {
                deliveryDayOfWeekList.add(String.valueOf(GoodsEnums.WeekCodeByGreenJuice.WED.getDayNum()));
                dailyOrderCnt += orderDetlDailyVo.getWedCnt();
            }
            if (orderDetlDailyVo.getThuCnt() > 0) {
                deliveryDayOfWeekList.add(String.valueOf(GoodsEnums.WeekCodeByGreenJuice.THU.getDayNum()));
                dailyOrderCnt += orderDetlDailyVo.getThuCnt();
            }
            if (orderDetlDailyVo.getFriCnt() > 0) {
                deliveryDayOfWeekList.add(String.valueOf(GoodsEnums.WeekCodeByGreenJuice.FRI.getDayNum()));
                dailyOrderCnt += orderDetlDailyVo.getFriCnt();
            }

            // 일일 상품 스케쥴
            //if (orderDetlVo.getGoodsDailyTp().equals(GoodsEnums.GoodsDailyType.GREENJUICE.getCode())) {
            OrderDetailScheduleUpdateRequestDto orderDetailScheduleUpdateRequestDto = new OrderDetailScheduleUpdateRequestDto();
            orderDetailScheduleUpdateRequestDto.setUrWarehouseId(orderDetlVo.getUrWarehouseId());
            orderDetailScheduleUpdateRequestDto.setChangeDate(deliverableDate);
            orderDetailScheduleUpdateRequestDto.setDeliveryDayOfWeekList(deliveryDayOfWeekList);

            ApiResult<?> apiResult = orderScheduleBiz.getOrderDetailScheduleDayOfWeekList(orderDetailScheduleUpdateRequestDto);
            List<OrderDetailScheduleDayOfWeekListDto> orderDeliverableScheduleList = (List<OrderDetailScheduleDayOfWeekListDto>) apiResult.getData();

            int dayCnt = 0;
            for (OrderDetailScheduleDayOfWeekListDto dailySchItem : orderDeliverableScheduleList) {

                LocalDate thisDay   = LocalDate.parse(dailySchItem.getDelvDate(), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                int thisWeekDayNum  = thisDay.getDayOfWeek().getValue();

                if (GoodsEnums.WeekCodeByGreenJuice.MON.getLocalDateDayNum() == thisWeekDayNum){
                    dailyOrderCnt = orderDetlDailyVo.getMonCnt();
                } else if (GoodsEnums.WeekCodeByGreenJuice.TUE.getLocalDateDayNum() == thisWeekDayNum) {
                    dailyOrderCnt = orderDetlDailyVo.getTueCnt();
                } else if (GoodsEnums.WeekCodeByGreenJuice.WED.getLocalDateDayNum() == thisWeekDayNum) {
                    dailyOrderCnt = orderDetlDailyVo.getWedCnt();
                } else if (GoodsEnums.WeekCodeByGreenJuice.THU.getLocalDateDayNum() == thisWeekDayNum) {
                    dailyOrderCnt = orderDetlDailyVo.getThuCnt();
                } else if (GoodsEnums.WeekCodeByGreenJuice.FRI.getLocalDateDayNum() == thisWeekDayNum) {
                    dailyOrderCnt = orderDetlDailyVo.getFriCnt();
                } else {
                    dailyOrderCnt = 0;
                }

                orderRegistrationService.addOrderDetlDailySch(OrderDetlDailySchVo.builder()
                        .odOrderDetlDailySchSeq(++dayCnt)
                        .odOrderDetlDailyId(odOrderDetlDailyId)
                        .deliveryDt(thisDay)
                        .psShippingCompId(0)
                        .trackingNo("")
                        .orderCnt(dailyOrderCnt)
                        .deliveryYn("N")
                        .orderSchStatus(OrderScheduleEnums.ScheduleOrderType.SCHEDULE_ORDER_TYPE_ORDER.getCode())
                        .odShippingZoneId(orderDetlVo.getOdShippingZoneId())
                        .useYn("Y")
                        .build());
                if (deliveryCnt <= dayCnt) {
                    break;
                }
            }



        }
        //}
    }

	/**
	 * 결제마스터 ID 생성
	 */
	@Override
	public long getPaymentMasterSeq() {
		return orderRegistrationSeqBiz.getPaymentMasterSeq();
	}

	/**
	 * 결제를 입력 한다.
	 * @param orderPaymentVo
	 * @return
	 */
	@Override
	public int addPayment(OrderPaymentVo orderPaymentVo) {
		return orderRegistrationService.addPayment(orderPaymentVo);
	}

	/**
	 * 결제 마스터를 입력 한다.
	 * @param orderPaymentMasterVo
	 * @return
	 */
	@Override
	public int addPaymentMaster(OrderPaymentMasterVo orderPaymentMasterVo) {
		return orderRegistrationService.addPaymentMaster(orderPaymentMasterVo);
	}

	/**
	 * 결제 마스터를 업데이트 한다.
	 * @param orderPaymentMasterVo
	 * @return
	 */
	@Override
	public int putApprovalPaymentMaster(OrderPaymentMasterVo orderPaymentMasterVo) {
		return orderRegistrationService.putApprovalPaymentMaster(orderPaymentMasterVo);
	}

    /**
     * 주문상세스텝ID 업데이트 처리
     * @return
     */
	@Override
	public int putOdOrderDetlStepId(long odOrderId, long odOrderDetlStepId) {
		return orderRegistrationService.putOdOrderDetlStepId(odOrderId, odOrderDetlStepId);
	}

    /**
     * 주문 배송비
     * OD_SHIPPING_PRICE.OD_SHIPPING_PRICE_ID PK 조회
     * @return
     */
	@Override
	public long getOrderShippingPriceSeq() {
		return orderRegistrationSeqBiz.getOrderShippingPriceSeq();
	}

    /**
     * 주문상세 등록 - Insert Select
     * @param orderDetlVo
     * @return
     */
	@Override
	public int selectAddOrderDetl(OrderDetlVo orderDetlVo, long prevOdOrderDetlId) {
		return orderRegistrationService.selectAddOrderDetl(orderDetlVo, prevOdOrderDetlId);
	}

    /**
     * 주문상세할인정보 등록 - Insert Select
     * @param orderDetlDiscountInfoDto
     * @return
     */
	@Override
    public int selectAddOrderDetlDiscount(OrderDetlDiscountInfoDto orderDetlDiscountInfoDto) {
		return orderRegistrationService.selectAddOrderDetlDiscount(orderDetlDiscountInfoDto);
	}

    /**
     * 주문상세배송금액 등록 - Insert Select
     * @param orderShippingPriceVo
     * @return
     */
	@Override
	public int selectAddOrderDetlShippingPrice(OrderShippingPriceVo orderShippingPriceVo, long prevOdOrderDetlId) {
		return orderRegistrationService.selectAddOrderDetlShippingPrice(orderShippingPriceVo, prevOdOrderDetlId);
	}

    /**
     * 주문 상세 일일배송 스케쥴 등록
     * addOrderDetlDailySch
     * @param orderDetlDailySchVo
     */
	@Override
    public int addOrderDetlDailySch(OrderDetlDailySchVo orderDetlDailySchVo) {
        return orderRegistrationService.addOrderDetlDailySch(orderDetlDailySchVo);
    }

    /**
     * 주문복사 에서 주문 등록
     * @param orderVo
     * @param serchOdOrderId
     * @return
     */
	@Override
    public int addOrderCopyOdOrder(OrderVo orderVo, long srchOdOrderId) {
    	return orderRegistrationService.addOrderCopyOdOrder(orderVo, srchOdOrderId);
    }

    /**
     * 주문복사에서 주문상세 등록
     * @param orderDetlVo
     * @param srchOdOrderDetlId
     * @return
     */
	@Override
    public int addOrderCopyOrderDetl(OrderDetlVo orderDetlVo, long srchOdOrderDetlId) {
    	return orderRegistrationService.addOrderCopyOrderDetl(orderDetlVo, srchOdOrderDetlId);
    }

    /**
     * 주문복사에서 주문상세할인금액 등록
     * @param orderDetlDiscountVo
     * @param srchOdOrderId
     * @param srchOdOrderDetlId
     * @param goodsCouponPrice
     * @param cartCouponPrice
     * @return
     */
	@Override
    public int addOrderCopyOrderDetlDiscount(OrderDetlDiscountVo orderDetlDiscountVo, long srchOdOrderId, long srchOdOrderDetlId, long goodsCouponPrice, long cartCouponPrice) {
    	return orderRegistrationService.addOrderCopyOrderDetlDiscount(orderDetlDiscountVo, srchOdOrderId, srchOdOrderDetlId, goodsCouponPrice, cartCouponPrice);
    }

    /**
     * 주문복사에서 주문상세묶음상품 신규등록
     * @param orderDetlPackVo
     * @param srchOdOrderDetlId
     * @return
     */
	@Override
    public int addOrderCopyOrderDetlPack(OrderDetlPackVo orderDetlPackVo, long srchOdOrderDetlId) {
    	return orderRegistrationService.addOrderCopyOrderDetlPack(orderDetlPackVo, srchOdOrderDetlId);
    }

    /**
     * 주문복사에서 주문상세 일일배송 패턴 신규등록
     * @param orderDetlDailyVo
     * @param srchOdOrderId
     * @param srchOdOrderDetlId
     * @return
     */
	@Override
    public int addOrderCopyOrderDetlDaily(OrderDetlDailyVo orderDetlDailyVo, long srchOdOrderId, long srchOdOrderDetlId) {
    	return orderRegistrationService.addOrderCopyOrderDetlDaily(orderDetlDailyVo, srchOdOrderId, srchOdOrderDetlId);
    }

    /**
     * 주문복사에서 주문상세 일일배송 스케쥴
     * @param orderDetlDailySchVo
     * @param srchOdOrderDetlDailyId
     * @return
     */
	@Override
	public int addOrderCopyOrderDetlDailySch(OrderDetlDailySchVo orderDetlDailySchVo, long srchOdOrderDetlDailyId) {
    	return orderRegistrationService.addOrderCopyOrderDetlDailySch(orderDetlDailySchVo, srchOdOrderDetlDailyId);
    }

    /**
     * 주문복사에서 주문 배송지 등록
     * @param orderShippingZoneVo
     * @param srchOdShippingZoneId
     * @return
     */
	@Override
	public int addOrderCopyShippingZone(OrderShippingZoneVo orderShippingZoneVo, long srchOdShippingZoneId) {
    	return orderRegistrationService.addOrderCopyShippingZone(orderShippingZoneVo, srchOdShippingZoneId);
    }

    /**
     * 주문복사에서 주문 배송지 이력 등록
     * @param OrderShippingZoneHistVo
     * @param srchOdShippingZoneId
     * @return
     */
	@Override
	public int addOrderCopyShippingZoneHist(OrderShippingZoneHistVo orderShippingZoneHistVo, long srchOdShippingZoneId) {
    	return orderRegistrationService.addOrderCopyShippingZoneHist(orderShippingZoneHistVo, srchOdShippingZoneId);
    }

    /**
     * 주문복사에서 주문 배송비 등록
     * @param orderShippingPriceVo
     * @param srchOdShippingPriceId
     * @return
     */
	@Override
	public int addOrderCopyShippingPrice(OrderShippingPriceVo orderShippingPriceVo, long srchOdShippingPriceId, String sellersGroupCd) {
    	return orderRegistrationService.addOrderCopyShippingPrice(orderShippingPriceVo, srchOdShippingPriceId, sellersGroupCd);
    }

    /**
     * 주문복사에서 주문결제 등록
     * @param orderPaymentVo
     * @param srchOdOrderId
     * @param srchOdPaymentMasterId
     * @return
     */
	@Override
	public int addOrderCopyPayment(OrderPaymentVo orderPaymentVo, long srchOdOrderId,  long srchOdPaymentMasterId) {
    	return orderRegistrationService.addOrderCopyPayment(orderPaymentVo, srchOdOrderId, srchOdPaymentMasterId);
    }

	/**
	 * 주문복사에서 주문 상세 주문상세 정렬 키 업데이트
	 * @param odOrderId
	 * @return
	 */
	@Override
	public int putOrderDetlSeq(long odOrderId) {
		return orderRegistrationService.putOrderDetlSeq(odOrderId);
	}

	/**
     * 주문상세 일일배송 배송지 정보 등록
     * OD_ORDER_DETL_DAILY_ZONE
     * @param odOrderId
     * @param promotionTp
     */
    @Override
    public int addOrderDetlDailyZone(long odOrderId, String promotionTp){
        return orderRegistrationService.addOrderDetlDailyZone(odOrderId, promotionTp);
    }

	/**
     * 주문 상태 일자  등록
     * OD_ORDER_DETL_DAILY_ZONE
     * @param odOrderId
     * @param promotionTp
     */
    @Override
    public int addOrderDt(OrderDtVo orderDtVo) {
    	return orderRegistrationService.addOrderDt(orderDtVo);
    }

    @Override
    public OrderVo getOrderCopySalIfYn(Long odOrderId) {
        return orderRegistrationService.getOrderCopySalIfYn(odOrderId);
    }

    /**
     * 현금영수증 발행
     * @param orderCashReceiptVo
     */
    @Override
    public int addOrderCashReceipt(OrderCashReceiptVo orderCashReceiptVo){
        return orderRegistrationService.addOrderCashReceipt(orderCashReceiptVo);
    }

    /**
     * 주문 환불 정보 등록
     */
    public int addAccount(OrderAccountVo orderAccountVo) {
        return orderRegistrationService.addAccount(orderAccountVo);
    }
}
