package kr.co.pulmuone.batch.job.application.order.order;

import kr.co.pulmuone.batch.job.BaseJob;
import kr.co.pulmuone.v1.batch.order.order.GreenJuiceSyncBiz;
import kr.co.pulmuone.v1.batch.order.order.VirtualBankOrderCancelBiz;
import kr.co.pulmuone.v1.comm.enums.OrderEnums;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("greenJuiceSyncJob")
@Slf4j
@RequiredArgsConstructor
public class GreenJuiceSyncJob implements BaseJob {

    @Autowired
    private GreenJuiceSyncBiz greenJuiceSyncBiz;

    @Override
    public void run(String[] params) throws Exception {

        log.info("======"+ OrderEnums.GreenJuiceSyncBatchTypeCd.GREENJUICE_SYNC.getCodeName()+"======");

        try {

            // 녹즙 동기화 배치
            greenJuiceSyncBiz.runGreenJuiceSync();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }
}