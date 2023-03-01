package kr.co.pulmuone.batch.job.application.user.store;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import kr.co.pulmuone.batch.job.BaseJob;
import kr.co.pulmuone.v1.batch.user.store.StoreDeliveryZoneBatchBiz;
import kr.co.pulmuone.v1.comm.exception.BaseException;
import lombok.extern.slf4j.Slf4j;

@Component("storeDeliveryZoneJob")
@Slf4j
public class StoreDeliveryZoneJob implements BaseJob{

	@Autowired
    private StoreDeliveryZoneBatchBiz storeDeliveryZoneBatchBiz;

    @Override
    public void run(String[] params) throws BaseException {

    	// 스토어(매장/가맹점) 매장배송관리정보 배치 실행
    	storeDeliveryZoneBatchBiz.runStoreDeliveryAreaSetUp();
    }
}
