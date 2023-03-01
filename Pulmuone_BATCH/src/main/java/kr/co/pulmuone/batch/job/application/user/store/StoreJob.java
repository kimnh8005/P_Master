package kr.co.pulmuone.batch.job.application.user.store;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import kr.co.pulmuone.batch.job.BaseJob;
import kr.co.pulmuone.v1.batch.user.store.StoreDeliveryZoneBatchBiz;
import kr.co.pulmuone.v1.comm.exception.BaseException;
import lombok.extern.slf4j.Slf4j;

@Component("storeJob")
@Slf4j
public class StoreJob implements BaseJob{

	@Autowired
    private StoreDeliveryZoneBatchBiz storeDeliveryZoneBatchBiz;

    @Override
    public void run(String[] params) throws BaseException {

    	// 매장정보 배치 실행
    	storeDeliveryZoneBatchBiz.runStoreSetUp();

    	// 매장주문시간  배치 실행
    	storeDeliveryZoneBatchBiz.runStoreOrdtimeSetUp();

    	// 매장 휴무일정보 배치 실행
    	storeDeliveryZoneBatchBiz.runStoreUnDeliveryDateSetUp();
    }
}
