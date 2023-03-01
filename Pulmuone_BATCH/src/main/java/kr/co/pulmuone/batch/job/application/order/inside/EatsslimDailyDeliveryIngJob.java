package kr.co.pulmuone.batch.job.application.order.inside;

import kr.co.pulmuone.batch.job.BaseJob;
import kr.co.pulmuone.v1.batch.order.inside.DeliveryCompleteBiz;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * <PRE>
 * Forbiz Korea
 * 잇슬림 일일배송 배송중 배치 Job
 *
 * 배치번호: 126
 * </PRE>
 */

@Component("eatsslimDailyDeliveryIngJob")
public class EatsslimDailyDeliveryIngJob implements BaseJob {

    @Autowired
    private DeliveryCompleteBiz deliveryCompleteBiz;

    public void run(String[] params) throws Exception {
    	deliveryCompleteBiz.runEatsslimDailyDeliveryIngSetUp();
    }
}