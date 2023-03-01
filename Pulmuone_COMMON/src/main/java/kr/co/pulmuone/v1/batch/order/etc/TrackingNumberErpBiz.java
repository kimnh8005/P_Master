package kr.co.pulmuone.v1.batch.order.etc;

import kr.co.pulmuone.v1.comm.exception.BaseException;

/**
 * <PRE>
 * Forbiz Korea
 * 송장조회 ERP API배치 Biz
 * </PRE>
 */

public interface TrackingNumberErpBiz {
	/** 송장 조회/저장 배치 */
	void runTrackingNumberSetUp() throws BaseException;
	/** 송장 조회/저장 배치 */
	void runHitokTrackingNumberSetUp() throws BaseException;
	/** 기타송장 조회/저장 배치 */
	void runEtcTrackingNumberSetUp() throws BaseException;
	/** 클레임 취소거부 처리  */
	void procClaimDenyDefer(String odid, long odClaimId) throws Exception;
}
