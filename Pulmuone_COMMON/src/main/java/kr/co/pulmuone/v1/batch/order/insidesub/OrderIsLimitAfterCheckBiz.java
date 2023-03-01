package kr.co.pulmuone.v1.batch.order.insidesub;

import kr.co.pulmuone.v1.comm.exception.BaseException;

/**
 * <PRE>
 * Forbiz Korea
 * 용인물류 일반배송 주문체크 후 제한 배치 Biz
 * </PRE>
 */

public interface OrderIsLimitAfterCheckBiz {

	/**
	 * 용인물류 일반배송 주문체크 후 제한
	 * @param
	 * @return void
	 * @throws BaseException
	 */
	void normalDeliveryOrderIsLimitAfterCheck() throws BaseException;

}
