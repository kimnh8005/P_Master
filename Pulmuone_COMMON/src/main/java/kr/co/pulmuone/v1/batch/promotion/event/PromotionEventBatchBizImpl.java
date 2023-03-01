package kr.co.pulmuone.v1.batch.promotion.event;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class PromotionEventBatchBizImpl implements PromotionEventBatchBiz {

    @Autowired
    private PromotionEventBatchService promotionEventBatchService;

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false)
    public void runEventTimeOver() {
        promotionEventBatchService.runEventTimeOver();
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false)
    public void runStampPurchaseEvent() {
        promotionEventBatchService.runStampPurchaseEvent();
    }

}
