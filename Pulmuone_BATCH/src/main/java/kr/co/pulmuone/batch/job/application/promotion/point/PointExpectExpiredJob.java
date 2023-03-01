package kr.co.pulmuone.batch.job.application.promotion.point;

import kr.co.pulmuone.v1.batch.promotion.point.PromotionPointBatchBiz;
import kr.co.pulmuone.batch.job.BaseJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("pointExpectExpiredJob")
@Slf4j
public class PointExpectExpiredJob implements BaseJob {

    @Autowired
    private PromotionPointBatchBiz promotionPointBatchBiz;

    @Override
    public void run(String[] params) {
    	promotionPointBatchBiz.runPointExpectExpired();
    }

}
