package kr.co.pulmuone.v1.order.delivery.service;

import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.order.delivery.dto.OrderTrackingNumberDto;

/**
 * <PRE>
 * Forbiz Korea
 * 주문 배송 관련 Interface
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :   작성일                :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 *  1.0    2020. 12. 29.            이명수         최초작성
 * =======================================================================
 * </PRE>
 */
public interface OrderDeliveryBiz {

	/**
	 * 송장번호 등록/업데이트
	 * @param orderTrackingNumberDto
	 * @return
	 */
	ApiResult<?> addOrderDetailTrackingNumber(OrderTrackingNumberDto orderTrackingNumberDto);
}
