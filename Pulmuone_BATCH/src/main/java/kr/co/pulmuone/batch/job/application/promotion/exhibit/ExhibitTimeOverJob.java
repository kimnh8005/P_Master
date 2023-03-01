package kr.co.pulmuone.batch.job.application.promotion.exhibit;

import kr.co.pulmuone.v1.batch.promotion.exhibit.PromotionExhibitBatchBiz;
import kr.co.pulmuone.batch.job.BaseJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("exhibitTimeOverJob")
@Slf4j
public class ExhibitTimeOverJob implements BaseJob {

    @Autowired
    private PromotionExhibitBatchBiz promotionExhibitBatchBiz;

    @Override
    public void run(String[] params) {
        promotionExhibitBatchBiz.runExhibitTimeOver();
    }

}
