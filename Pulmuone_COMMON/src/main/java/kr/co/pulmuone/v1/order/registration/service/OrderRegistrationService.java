package kr.co.pulmuone.v1.order.registration.service;


import kr.co.pulmuone.v1.comm.enums.OrderEnums.OrderRegistrationResult;
import kr.co.pulmuone.v1.comm.mapper.order.registration.OrderRegistrationMapper;
import kr.co.pulmuone.v1.order.order.dto.vo.*;
import kr.co.pulmuone.v1.order.registration.dto.OrderBindDto;
import kr.co.pulmuone.v1.order.registration.dto.OrderBindOrderDetlDto;
import kr.co.pulmuone.v1.order.registration.dto.OrderBindShippingPriceDto;
import kr.co.pulmuone.v1.order.registration.dto.OrderBindShippingZoneDto;
import kr.co.pulmuone.v1.order.registration.dto.OrderDetlDiscountInfoDto;
import kr.co.pulmuone.v1.order.schedule.dto.OrderDetailScheduleListDto;
import kr.co.pulmuone.v1.order.schedule.dto.OrderDetailScheduleUpdateRequestDto;
import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.stereotype.Service;

/**
 * <PRE>
 * Forbiz Korea
 * 주문 데이터 생성 관련 Service
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
@Service
@RequiredArgsConstructor
public class OrderRegistrationService {
    private final OrderRegistrationMapper orderRegistrationMapper;

    /**
     * 주문 벨리데이션 체크
     *
     * @param orderBindList
     * @return
     */
	protected OrderRegistrationResult validationOrder(List<OrderBindDto> orderBindList) {
//		OrderRegistrationResult result = null;
//
//		for (OrderBindDto dto : orderBindList) {
//			// 주문 VO
//			result = validationOrderVo(dto.getOrder());
//			if (!OrderRegistrationResult.SUCCESS.equals(result)) {
//				return result;
//			}
//
//			// 주문 결제 VO
//			result = validationOrderPaymentVo(dto.getOrderPaymentVo());
//			if (!OrderRegistrationResult.SUCCESS.equals(result)) {
//				return result;
//			}
//
//			for (OrderBindShippingZoneDto shippingZoneDto : dto.getOrderShippingZoneList()) {
//				// 주문 배송지 VO
//				result = validationOrderShippingZoneVo(shippingZoneDto.getOrderShippingZoneVo());
//				if (!OrderRegistrationResult.SUCCESS.equals(result)) {
//					return result;
//				}
//
//				for (OrderBindShippingPriceDto shippingPriceDto : shippingZoneDto.getShippingPriceList()) {
//					// 주문 배송비 VO
//					result = validationOrderShippingPriceVo(shippingPriceDto.getOrderShippingPriceVo());
//					if (!OrderRegistrationResult.SUCCESS.equals(result)) {
//						return result;
//					}
//
//					for (OrderBindOrderDetlDto orderDetlDto : shippingPriceDto.getOrderDetlList()) {
//						// 주문 상세 VO
//						result = validationOrderDetlVo(orderDetlDto.getOrderDetlVo());
//						if (!OrderRegistrationResult.SUCCESS.equals(result)) {
//							return result;
//						}
//					}
//				}
//			}
//		}

		return OrderRegistrationResult.SUCCESS;
	}


	protected OrderRegistrationResult validationOrderVo(OrderVo vo) {
		return OrderRegistrationResult.SUCCESS;
	}

	protected OrderRegistrationResult validationOrderPaymentVo(OrderPaymentVo vo) {
		return OrderRegistrationResult.SUCCESS;
	}

	protected OrderRegistrationResult validationOrderShippingZoneVo(OrderShippingZoneVo vo) {
		return OrderRegistrationResult.SUCCESS;
	}

	protected OrderRegistrationResult validationOrderShippingPriceVo(OrderShippingPriceVo vo) {
		return OrderRegistrationResult.SUCCESS;
	}

	protected OrderRegistrationResult validationOrderDetlVo(OrderDetlVo vo) {
		return OrderRegistrationResult.SUCCESS;
	}

    /**
     * 주문 등록
     * OD_ORDER
     * @param orderVo
     * @return
     */
    protected int addOrder(OrderVo orderVo) {
        return orderRegistrationMapper.addOrder(orderVo);
    }

    /**
     * 주문 날짜 등록
     * OD_ORDER_DT
     * @param orderDtVo
     * @return
     */
    protected int addOrderDt(OrderDtVo orderDtVo) {
        return orderRegistrationMapper.addOrderDt(orderDtVo);
    }

    /**
     * 주문 선물하기 등록
     * OD_ORDER_PRESENT
     * @param orderPresentVo
     * @return
     */
    protected int addOrderPresent(OrderPresentVo orderPresentVo) {
        return orderRegistrationMapper.addOrderPresent(orderPresentVo);
    }

    /**
     * 주문 배송지 등록
     * OD_SHIPPING_ZONE
     * @param orderShippingZoneVo
     * @return
     */
    protected int addShippingZone(OrderShippingZoneVo orderShippingZoneVo) {
        return orderRegistrationMapper.addShippingZone(orderShippingZoneVo);
    }

    /**
     * 주문 배송지 이력 등록
     * OD_SHIPPING_ZONE_HIST
     * @param orderShippingZoneHistVo
     * @return
     */
    protected int addShippingZoneHist(OrderShippingZoneHistVo orderShippingZoneHistVo) {
        return orderRegistrationMapper.addShippingZoneHist(orderShippingZoneHistVo);
    }

    /**
     * 주문 배송비 등록
     * OD_SHIPPING_PRICE
     * @param orderShippingPriceVo
     * @return
     */
    protected int addShippingPrice(OrderShippingPriceVo orderShippingPriceVo) {
        return orderRegistrationMapper.addShippingPrice(orderShippingPriceVo);
    }

    /**
     * 주문 상세 등록
     * OD_ORDER_DETL
     * @param orderDetlVo
     * @return
     */
    protected int addOrderDetl(OrderDetlVo orderDetlVo) {
        return orderRegistrationMapper.addOrderDetl(orderDetlVo);
    }

    /**
     * 주문 상세 묶음/골라담기 타이틀 상품 등록
     * OD_ORDER_DETL
     * @param orderDetlPackVo
     * @return
     */
    protected int addOrderDetlPack(OrderDetlPackVo orderDetlPackVo) {
        return orderRegistrationMapper.addOrderDetlPack(orderDetlPackVo);
    }

    /**
     * 주문 상세 할인정보 등록
     * OD_ORDER_DETL_DISCOUNT
     * @param orderDetlDiscountVo
     * @return
     */
    protected int addOrderDetlDiscount(OrderDetlDiscountVo orderDetlDiscountVo) {
        return orderRegistrationMapper.addOrderDetlDiscount(orderDetlDiscountVo);
    }

    /**
     * 주문 결제 정보 등록
     * OD_PAYMENT
     * @param orderPaymentVo
     * @return
     */
    protected int addPayment(OrderPaymentVo orderPaymentVo) {
        return orderRegistrationMapper.addPayment(orderPaymentVo);
    }

    /**
     * 주문 결제 마스터 정보 등록
     * OD_PAYMENT_MASTER
     * @param orderPaymentMasterVo
     * @return
     */
    protected int addPaymentMaster(OrderPaymentMasterVo orderPaymentMasterVo) {
        return orderRegistrationMapper.addPaymentMaster(orderPaymentMasterVo);
    }

    /**
     * 주문서 승인 주문정보 업데이트
     * @param orderVo
     */
    protected int putApprovalOrder(OrderVo orderVo) {
        return orderRegistrationMapper.putApprovalOrder(orderVo);
    }

    /**
     * 주문서 승인 주문상세 정보 업데이트
     * @param orderDetlVo
     */
    protected int putApprovalOrderDetl(OrderDetlVo orderDetlVo) {
        return orderRegistrationMapper.putApprovalOrderDetl(orderDetlVo);
    }

    /**
     * 주문서 승인 결제 마스터 정보 업데이트
     * @param orderPaymentMasterVo
     */
    protected int putApprovalPaymentMaster(OrderPaymentMasterVo orderPaymentMasterVo) {
        return orderRegistrationMapper.putApprovalPaymentMaster(orderPaymentMasterVo);
    }

    /**
     * 주문서 결제 승인 결제 마스터 정보 업데이트
     * @param orderPaymentMasterVo
     */
    protected int putPayApprovalPaymentMaster(OrderPaymentMasterVo orderPaymentMasterVo) {
        return orderRegistrationMapper.putPayApprovalPaymentMaster(orderPaymentMasterVo);
    }

    /**
     * 주문서 결제 입금대기 일자 업데이트
     * @param orderPaymentMasterVo
     */
    protected int putApprovalOrderDtIr(Long odOrderId) {
        return orderRegistrationMapper.putApprovalOrderDtIr(odOrderId);
    }

    /**
     * 주문서 결제 결제완료 일자 업데이트
     * @param orderPaymentMasterVo
     */
    protected int putApprovalOrderDtIc(Long odOrderId) {
        return orderRegistrationMapper.putApprovalOrderDtIc(odOrderId);
    }

    /**
     * 주문 상세 등록
     * OD_ORDER_DETL_DAILY
     * @param orderDetlDailyVo
     */
    protected int addOrderDetlDaily(OrderDetlDailyVo orderDetlDailyVo) {
        return orderRegistrationMapper.addOrderDetlDaily(orderDetlDailyVo);
    }

    /**
     * 주문 상세 라인번호 업데이트
     * putOrderDetlSeq
     * @param odOrderId
     * @return
     */
    protected int putOrderDetlSeq(long odOrderId) {
        return orderRegistrationMapper.putOrderDetlSeq(odOrderId);
    }

    /**
     * 주문 상세 일일배송 라인번호 업데이트
     * putOrderDetlSeq
     * @param odOrderId
     * @return
     */
    protected int putOrderDetlDailySeq(long odOrderId) {
        return orderRegistrationMapper.putOrderDetlDailySeq(odOrderId);
    }


    /**
     * 주문 상세 일일배송 스케쥴 등록
     * addOrderDetlDailySch
     * @param orderDetlDailySchVo
     * @return
     */
    protected int addOrderDetlDailySch(OrderDetlDailySchVo orderDetlDailySchVo) {
        return orderRegistrationMapper.addOrderDetlDailySch(orderDetlDailySchVo);
    }

    /**
     * 주문 녹즙 스캐줄 요일 리스트 조회
     * @param orderDetailScheduleUpdateRequestDto
     * @return
     */
    protected List<OrderDetailScheduleListDto> getOrderDetailScheduleDayOfWeekList(OrderDetailScheduleUpdateRequestDto orderDetailScheduleUpdateRequestDto) {
    	return orderRegistrationMapper.getOrderDetailScheduleDayOfWeekList(orderDetailScheduleUpdateRequestDto);
    }
    /**
     * 주문상세스텝ID 업데이트 처리
     * @return
     */
    protected int putOdOrderDetlStepId(long odOrderId, long odOrderDetlStepId) {
    	return orderRegistrationMapper.putOdOrderDetlStepId(odOrderId, odOrderDetlStepId);
    }

    /**
     * 주문상세 등록 - Insert Select
     * @param orderDetlVo
     * @return
     */
    protected int selectAddOrderDetl(OrderDetlVo orderDetlVo, long prevOdOrderDetlId) {
    	return orderRegistrationMapper.selectAddOrderDetl(orderDetlVo, prevOdOrderDetlId);
    }

    /**
     * 주문상세할인정보 등록 - Insert Select
     * @param orderDetlDiscountInfoDto
     * @return
     */
    protected int selectAddOrderDetlDiscount(OrderDetlDiscountInfoDto orderDetlDiscountInfoDto) {
    	return orderRegistrationMapper.selectAddOrderDetlDiscount(orderDetlDiscountInfoDto);
    }

    /**
     * 주문상세배송금액 등록 - Insert Select
     * @param orderShippingPriceVo
     * @return
     */
    protected int selectAddOrderDetlShippingPrice(OrderShippingPriceVo orderShippingPriceVo, long prevOdOrderDetlId) {
    	return orderRegistrationMapper.selectAddOrderDetlShippingPrice(orderShippingPriceVo, prevOdOrderDetlId);
    }

    /**
     * 주문 환불 정보 등록
     * @param orderAccountVo
     * @return
     */
    protected int addAccount(OrderAccountVo orderAccountVo) {
    	return orderRegistrationMapper.addAccount(orderAccountVo);
    }

    protected int addOrderCashReceipt(OrderCashReceiptVo orderCashReceiptVo) {
    	return orderRegistrationMapper.addOrderCashReceipt(orderCashReceiptVo);
    }

    /**
     * 주문복사 에서 주문 등록
     * @param orderVo
     * @param serchOdOrderId
     * @return
     */
    protected int addOrderCopyOdOrder(OrderVo orderVo, long srchOdOrderId) {
    	return orderRegistrationMapper.addOrderCopyOdOrder(orderVo, srchOdOrderId);
    }

    /**
     * 주문복사에서 주문상세 등록
     * @param orderDetlVo
     * @param srchOdOrderDetlId
     * @return
     */
    protected int addOrderCopyOrderDetl(OrderDetlVo orderDetlVo, long srchOdOrderDetlId) {
    	return orderRegistrationMapper.addOrderCopyOrderDetl(orderDetlVo, srchOdOrderDetlId);
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
    protected int addOrderCopyOrderDetlDiscount(OrderDetlDiscountVo orderDetlDiscountVo, long srchOdOrderId, long srchOdOrderDetlId, long goodsCouponPrice, long cartCouponPrice) {
    	return orderRegistrationMapper.addOrderCopyOrderDetlDiscount(orderDetlDiscountVo, srchOdOrderId, srchOdOrderDetlId, goodsCouponPrice, cartCouponPrice);
    }

    /**
     * 주문복사에서 주문상세묶음상품 신규등록
     * @param orderDetlPackVo
     * @param srchOdOrderDetlId
     * @return
     */
    protected int addOrderCopyOrderDetlPack(OrderDetlPackVo orderDetlPackVo, long srchOdOrderDetlId) {
    	return orderRegistrationMapper.addOrderCopyOrderDetlPack(orderDetlPackVo, srchOdOrderDetlId);
    }

    /**
     * 주문복사에서 주문상세 일일배송 패턴 신규등록
     * @param orderDetlDailyVo
     * @param srchOdOrderId
     * @param srchOdOrderDetlId
     * @return
     */
    protected int addOrderCopyOrderDetlDaily(OrderDetlDailyVo orderDetlDailyVo, long srchOdOrderId, long srchOdOrderDetlId) {
    	return orderRegistrationMapper.addOrderCopyOrderDetlDaily(orderDetlDailyVo, srchOdOrderId, srchOdOrderDetlId);
    }

    /**
     * 주문복사에서 주문상세 일일배송 스케쥴
     * @param orderDetlDailySchVo
     * @param srchOdOrderDetlDailyId
     * @return
     */
    protected int addOrderCopyOrderDetlDailySch(OrderDetlDailySchVo orderDetlDailySchVo, long srchOdOrderDetlDailyId) {
    	return orderRegistrationMapper.addOrderCopyOrderDetlDailySch(orderDetlDailySchVo, srchOdOrderDetlDailyId);
    }

    /**
     * 주문복사에서 주문 배송지 등록
     * @param orderShippingZoneVo
     * @param srchOdShippingZoneId
     * @return
     */
    protected int addOrderCopyShippingZone(OrderShippingZoneVo orderShippingZoneVo, long srchOdShippingZoneId) {
    	return orderRegistrationMapper.addOrderCopyShippingZone(orderShippingZoneVo, srchOdShippingZoneId);
    }

    /**
     * 주문복사에서 주문 배송지 이력 등록
     * @param OrderShippingZoneHistVo
     * @param srchOdShippingZoneId
     * @return
     */
    protected int addOrderCopyShippingZoneHist(OrderShippingZoneHistVo orderShippingZoneHistVo, long srchOdShippingZoneId) {
    	return orderRegistrationMapper.addOrderCopyShippingZoneHist(orderShippingZoneHistVo, srchOdShippingZoneId);
    }

    /**
     * 주문복사에서 주문 배송비 등록
     * @param orderShippingPriceVo
     * @param srchOdShippingPriceId
     * @return
     */
    protected int addOrderCopyShippingPrice(OrderShippingPriceVo orderShippingPriceVo, long srchOdShippingPriceId, String sellersGroupCd) {
    	return orderRegistrationMapper.addOrderCopyShippingPrice(orderShippingPriceVo, srchOdShippingPriceId, sellersGroupCd);
    }

    /**
     * 주문복사에서 주문결제 등록
     * @param orderPaymentVo
     * @param srchOdOrderId
     * @param srchOdPaymentMasterId
     * @return
     */
    protected int addOrderCopyPayment(OrderPaymentVo orderPaymentVo, long srchOdOrderId,  long srchOdPaymentMasterId) {
    	return orderRegistrationMapper.addOrderCopyPayment(orderPaymentVo, srchOdOrderId, srchOdPaymentMasterId);
    }


    /**
     * 주문 상세 등록
     * OD_ORDER_DETL_DAILY
     * @param odOrderId
     * @param promotionTp
     */
    protected int addOrderDetlDailyZone(long odOrderId, String promotionTp) {
        return orderRegistrationMapper.addOrderDetlDailyZone(odOrderId, promotionTp);
    }

    public OrderVo getOrderCopySalIfYn(Long odOrderId) {
        return orderRegistrationMapper.getOrderCopySalIfYn(odOrderId);
    }


    public int putOrderDetailStatusHist(OrderDetlHistVo orderDetlHistVo) {
        return orderRegistrationMapper.putOrderDetailStatusHist(orderDetlHistVo);
    }
}
