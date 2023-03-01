package kr.co.pulmuone.batch.job.application.order.inside;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import kr.co.pulmuone.batch.job.BaseJob;
import kr.co.pulmuone.v1.batch.order.inside.DeliveryCompleteBiz;

/**
 * <PRE>
 * Forbiz Korea
 * 배송완료(일일) 배치 Job
 * </PRE>
 */

@Component("deliveryCompleteDailyOrderJob")
public class DeliveryCompleteDailyOrderJob implements BaseJob {

    @Autowired
    private DeliveryCompleteBiz deliveryCompleteBiz;		// 배송완료 배치 Biz

    public void run(String[] params) throws Exception {
    	// 배송완료(일일) 배치 실행
    	deliveryCompleteBiz.runDeliveryCompleteDailySetUp();
    }
}