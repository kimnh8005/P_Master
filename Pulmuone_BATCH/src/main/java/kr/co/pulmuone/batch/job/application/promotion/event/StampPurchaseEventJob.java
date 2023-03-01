package kr.co.pulmuone.batch.job.application.promotion.event;

import kr.co.pulmuone.v1.batch.promotion.event.PromotionEventBatchBiz;
import kr.co.pulmuone.batch.job.BaseJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("stampPurchaseEventJob")
@Slf4j
public class StampPurchaseEventJob implements BaseJob {
    /*
    배치명 : MALL - 이벤트 - 스탬프(구매) - 스탬프 발급
    배치번호 : 33
     */

    @Autowired
    private PromotionEventBatchBiz promotionEventBatchBiz;

    @Override
    public void run(String[] params) {
        promotionEventBatchBiz.runStampPurchaseEvent();
    }

}
