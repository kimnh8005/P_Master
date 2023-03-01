package kr.co.pulmuone.batch.job.application.user.marketing;

import kr.co.pulmuone.v1.batch.user.marketing.MarketingInfoBatchBiz;
import kr.co.pulmuone.batch.job.BaseJob;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("maketingInfoJob")
@Slf4j
@RequiredArgsConstructor
public class MarketingInfoJob implements BaseJob {

    @Autowired
    private MarketingInfoBatchBiz marketingInfoBatchBiz;

    @Override
    public void run(String[] params) {
    	marketingInfoBatchBiz.runMarketingInfo();
    }
}
