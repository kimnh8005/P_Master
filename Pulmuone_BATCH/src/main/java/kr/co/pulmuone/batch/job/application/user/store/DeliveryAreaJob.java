package kr.co.pulmuone.batch.job.application.user.store;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import kr.co.pulmuone.batch.job.BaseJob;
import kr.co.pulmuone.v1.batch.user.store.StoreDeliveryZoneBatchBiz;
import kr.co.pulmuone.v1.comm.exception.BaseException;
import lombok.extern.slf4j.Slf4j;

@Component("deliveryAreaJob")
@Slf4j
public class DeliveryAreaJob implements BaseJob{

	@Autowired
    private StoreDeliveryZoneBatchBiz storeDeliveryZoneBatchBiz;

    @Override
    public void run(String[] params) throws BaseException {

    	// 배송권역 배치 실행
    	storeDeliveryZoneBatchBiz.runDeliveryAreaSetUp();
    }

}
