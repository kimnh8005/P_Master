package kr.co.pulmuone.batch.job.application.order.present;

import kr.co.pulmuone.batch.job.BaseJob;
import kr.co.pulmuone.v1.batch.order.present.OrderPresentBatchBiz;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 선물하기 만료처리 배치 Job
 * 배치번호: 137
 */

@Component("OrderPresentExpiredJob")
public class OrderPresentExpiredJob implements BaseJob {

    @Autowired
    private OrderPresentBatchBiz orderPresentBatchBiz;

    public void run(String[] params) throws Exception {
        orderPresentBatchBiz.runOrderPresentExpiredList();
    }

}
