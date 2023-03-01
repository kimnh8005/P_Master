package kr.co.pulmuone.v1.api.storedelivery.service;

import kr.co.pulmuone.v1.comm.exception.BaseException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * <PRE>
 * Forbiz Korea
 * 매장배송 API BizImpl
 * </PRE>
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class StoreDeliveryApiBizImpl implements StoreDeliveryApiBiz {

	@Autowired
	private final StoreDeliveryApiService storeDeliveryApiService;

	/**
	 * 매장배송 취소 API 송신
	 * @param odOrderId
	 * @param odClaimId
	 * @return boolean
	 * @throws BaseException
	 */
	@Override
	public boolean addStoreDeliveryCancelApiSend(long odOrderId, long odClaimId) throws BaseException {
		return storeDeliveryApiService.addStoreDeliveryCancelApiSend(odOrderId, odClaimId);
	}

	/**
	 * 매장배송 반품 API 송신
	 * @param odOrderId
	 * @param odClaimId
	 * @return boolean
	 * @throws BaseException
	 */
	@Override
	public boolean addStoreDeliveryReturnApiSend(long odOrderId, long odClaimId) throws BaseException {
		return storeDeliveryApiService.addStoreDeliveryReturnApiSend(odOrderId, odClaimId);
	}

	/**
	 * 매장배송 재배송 API 송신
	 * @param odOrderId
	 * @param odClaimId
	 * @return boolean
	 * @throws BaseException
	 */
	@Override
	public boolean addStoreDeliveryRedeliveryApiSend(long odOrderId, long odClaimId) throws BaseException {
		return storeDeliveryApiService.addStoreDeliveryRedeliveryApiSend(odOrderId, odClaimId);
	}

}