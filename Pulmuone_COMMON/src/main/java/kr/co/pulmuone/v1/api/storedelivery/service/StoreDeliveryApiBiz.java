package kr.co.pulmuone.v1.api.storedelivery.service;

import kr.co.pulmuone.v1.comm.exception.BaseException;

/**
 * <PRE>
 * Forbiz Korea
 * 매장배송 API Biz
 * </PRE>
 */
public interface StoreDeliveryApiBiz {

	/**
	 * 매장배송 취소 API 송신
	 * @param odOrderId
	 * @param odClaimId
	 * @return boolean
	 * @throws BaseException
	 */
	boolean addStoreDeliveryCancelApiSend(long odOrderId, long odClaimId) throws BaseException;

	/**
	 * 매장배송 반품 API 송신
	 * @param odOrderId
	 * @param odClaimId
	 * @return boolean
	 * @throws BaseException
	 */
	boolean addStoreDeliveryReturnApiSend(long odOrderId, long odClaimId) throws BaseException;

	/**
	 * 매장배송 재배송 API 송신
	 * @param odOrderId
	 * @param odClaimId
	 * @return boolean
	 * @throws BaseException
	 */
	boolean addStoreDeliveryRedeliveryApiSend(long odOrderId, long odClaimId) throws BaseException;
}
