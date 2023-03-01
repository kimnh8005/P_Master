package kr.co.pulmuone.v1.batch.order.order;

import kr.co.pulmuone.v1.comm.exception.BaseException;

/**
 * <PRE>
 * Forbiz Korea
 * 녹즙 동기화 배치 Biz
 * </PRE>
 */

public interface GreenJuiceSyncBiz {

	/**
	 * 녹즙 동기화
	 * @param
	 * @return void
	 * @throws Exception
	 */
	void runGreenJuiceSync() throws Exception;
}
