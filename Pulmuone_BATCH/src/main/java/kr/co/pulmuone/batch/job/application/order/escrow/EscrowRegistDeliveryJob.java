package kr.co.pulmuone.batch.job.application.order.escrow;

import kr.co.pulmuone.v1.batch.order.escrow.EscrowRegistDeliveryBatchBiz;
import kr.co.pulmuone.batch.job.BaseJob;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("escrowRegistDeliveryJob")
@Slf4j
@RequiredArgsConstructor
public class EscrowRegistDeliveryJob implements BaseJob {

    @Autowired
    private EscrowRegistDeliveryBatchBiz escrowRegistDeliveryBatchBiz;

    @Override
    public void run(String[] params) {
    	escrowRegistDeliveryBatchBiz.runEscrowRegistDelivery();
    }
}
