package kr.co.pulmuone.v1.batch.user.store;

import kr.co.pulmuone.v1.comm.exception.BaseException;

public interface StoreDeliveryZoneBatchBiz {

	void runStoreSetUp() throws BaseException;

	void runStoreDeliveryAreaSetUp() throws BaseException;

	void runStoreOrdtimeSetUp() throws BaseException;

	void runDeliveryAreaSetUp() throws BaseException;

	void runStoreUnDeliveryDateSetUp() throws BaseException;

}
