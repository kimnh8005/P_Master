package kr.co.pulmuone.v1.batch.order.regular;

import kr.co.pulmuone.v1.comm.exception.BaseException;

/**
 * <PRE>
 * Forbiz Korea
 * 정기배송 주문 배치 Biz
 * </PRE>
 */
public interface RegularOrderBiz {

	/**
	 * 정기배송 주문 생성
	 * @return void
	 * @throws BaseException
	 */
	void putRegularOrderResult() throws Exception;

	/**
	 * 정기배송 주문 결제
	 * @return void
	 * @throws BaseException
	 */
	void putRegularOrderResultPayment() throws Exception;

}
