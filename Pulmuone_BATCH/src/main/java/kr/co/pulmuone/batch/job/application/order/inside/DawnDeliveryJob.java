package kr.co.pulmuone.batch.job.application.order.inside;

import kr.co.pulmuone.batch.job.BaseJob;
import kr.co.pulmuone.v1.batch.order.inside.service.DawnDeliveryBizImpl;
import lombok.RequiredArgsConstructor;
import kr.co.pulmuone.v1.comm.exception.BaseException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * <PRE>
 * Forbiz Korea
 * 새벽권역 업데이트 배치 Job
 * </PRE>
 */

@Component("DawnDeliveryJob")
@Slf4j
@RequiredArgsConstructor
public class DawnDeliveryJob implements BaseJob {

    @Autowired
    private final DawnDeliveryBizImpl dawnDeliveryBizImpl;

    public void run(String[] params) throws Exception {

    	log.info("======새벽권역 업데이트 배치 ======");
    	dawnDeliveryBizImpl.batchDwanDeliveryArea();

    }
}
