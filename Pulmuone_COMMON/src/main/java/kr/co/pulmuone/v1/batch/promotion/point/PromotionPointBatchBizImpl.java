package kr.co.pulmuone.v1.batch.promotion.point;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class PromotionPointBatchBizImpl implements PromotionPointBatchBiz {

    @Autowired
    private PromotionPointBatchService promotionPointBatchService;

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false)
    public void runPointExpectExpired() {
        promotionPointBatchService.runPointExpectExpired();
    }
}
