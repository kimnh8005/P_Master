package kr.co.pulmuone.v1.batch.promotion.exhibit;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class PromotionExhibitBatchBizImpl implements PromotionExhibitBatchBiz {

    @Autowired
    private PromotionExhibitBatchService promotionExhibitBatchService;

    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false)
    public void runExhibitTimeOver() {
        promotionExhibitBatchService.runExhibitTimeOver();
    }

}
