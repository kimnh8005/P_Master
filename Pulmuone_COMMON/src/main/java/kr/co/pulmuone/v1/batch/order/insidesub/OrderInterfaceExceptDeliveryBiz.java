package kr.co.pulmuone.v1.batch.order.insidesub;

import kr.co.pulmuone.v1.comm.exception.BaseException;

/**
 * <PRE>
 * Forbiz Korea
 * 주문 I/F 외 배송준비중 배치 Biz
 * </PRE>
 */

public interface OrderInterfaceExceptDeliveryBiz {

	/**
	 * 주문 배송준비중 업데이트
	 * @param
	 * @return void
	 * @throws BaseException
	 */
	void orderDeliveryPreparingUpdate() throws BaseException;

}
