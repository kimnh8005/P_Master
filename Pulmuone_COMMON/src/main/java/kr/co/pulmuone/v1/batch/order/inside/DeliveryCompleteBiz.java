package kr.co.pulmuone.v1.batch.order.inside;

import kr.co.pulmuone.v1.comm.exception.BaseException;

/**
 * <PRE>
 * Forbiz Korea
 * 배송완료 배치 Biz
 * </PRE>
 */

public interface DeliveryCompleteBiz {
	/** 배송완료 배치 */
	void runDeliveryCompleteSetUp() throws BaseException;
	/** 매장배송 배송완료 배치 */
	void runStoreDeliveryCompleteSetUp() throws BaseException;
	/** 배송완료(일일:녹즙, 베이비밀, 잇슬림) 배치 */
	void runDeliveryCompleteDailySetUp() throws BaseException;
	/** 배송완료(CJ/롯데 트래킹) 배치 */
	void runDeliveryCompleteTrackingSetUp() throws BaseException;
	/** 잇슬림 일일배송 배송중 배치 */
	void runEatsslimDailyDeliveryIngSetUp() throws BaseException;
}