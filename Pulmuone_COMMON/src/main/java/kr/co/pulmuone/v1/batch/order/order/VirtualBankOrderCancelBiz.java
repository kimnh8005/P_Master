package kr.co.pulmuone.v1.batch.order.order;

import kr.co.pulmuone.v1.comm.exception.BaseException;

/**
 * <PRE>
 * Forbiz Korea
 * 주문 ERP API 배치 Biz
 * </PRE>
 */

public interface VirtualBankOrderCancelBiz {

	/**
	 * 입금기한 지난 가상계좌주문  취소
	 * @param
	 * @return void
	 * @throws BaseException
	 */
	void runVirtualBankOrderCancel() throws Exception;


}
