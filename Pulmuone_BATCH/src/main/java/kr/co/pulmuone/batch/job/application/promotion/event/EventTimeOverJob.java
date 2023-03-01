package kr.co.pulmuone.batch.job.application.promotion.event;

import kr.co.pulmuone.v1.batch.promotion.event.PromotionEventBatchBiz;
import kr.co.pulmuone.batch.job.BaseJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("eventTimeOverJob")
@Slf4j
public class EventTimeOverJob implements BaseJob {

    @Autowired
    private PromotionEventBatchBiz promotionEventBatchBiz;

    @Override
    public void run(String[] params) {
        promotionEventBatchBiz.runEventTimeOver();
    }

}
