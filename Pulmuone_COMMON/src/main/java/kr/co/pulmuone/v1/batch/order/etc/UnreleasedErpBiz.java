package kr.co.pulmuone.v1.batch.order.etc;

import kr.co.pulmuone.v1.comm.exception.BaseException;

/**
 * <PRE>
 * Forbiz Korea
 * 미출조회 ERP API배치 Biz
 * </PRE>
 */

public interface UnreleasedErpBiz {
	/** 미출 조회/저장 배치 */
	void runUnreleasedSetUp() throws Exception;

}
