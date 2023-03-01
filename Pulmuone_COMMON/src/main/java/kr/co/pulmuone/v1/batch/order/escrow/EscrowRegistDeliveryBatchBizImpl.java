package kr.co.pulmuone.v1.batch.order.escrow;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class EscrowRegistDeliveryBatchBizImpl implements EscrowRegistDeliveryBatchBiz {

    @Autowired
    private EscrowRegistDeliveryBatchService escrowRegistDeliveryBatchService;

    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false)
    public void runEscrowRegistDelivery() {
    	try {
			escrowRegistDeliveryBatchService.runEscrowRegistDelivery();
		} catch (Exception e) {
			log.error("============ 에크스로 배송등록 배치 ERROR ===============");
			log.error(e.getMessage());
		}
    }

}
