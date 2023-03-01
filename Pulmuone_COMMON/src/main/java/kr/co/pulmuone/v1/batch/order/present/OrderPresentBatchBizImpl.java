package kr.co.pulmuone.v1.batch.order.present;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class OrderPresentBatchBizImpl implements OrderPresentBatchBiz {

    @Autowired
    private OrderPresentBatchService orderPresentBatchService;

    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false)
    public void runOrderPresentExpiredList() throws Exception {
        orderPresentBatchService.runOrderPresentExpiredList();
    }

}
