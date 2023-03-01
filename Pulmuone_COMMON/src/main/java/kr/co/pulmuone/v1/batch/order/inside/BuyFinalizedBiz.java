package kr.co.pulmuone.v1.batch.order.inside;

import kr.co.pulmuone.v1.comm.exception.BaseException;

/**
 * <PRE>
 * Forbiz Korea
 * 구매확정 배치 Biz
 * </PRE>
 */

public interface BuyFinalizedBiz {
	/** 구매확정 배치 */
	void runBuyFinalizedSetUp() throws BaseException;
}