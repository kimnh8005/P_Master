package kr.co.pulmuone.v1.batch.user.marketing;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MarketingInfoBatchBizImpl implements MarketingInfoBatchBiz {

    @Autowired
    private MarketingInfoBatchService marketingInfoBatchService;

    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false)
    public void runMarketingInfo() {
    	marketingInfoBatchService.runMarketingInfo();
    }

}
