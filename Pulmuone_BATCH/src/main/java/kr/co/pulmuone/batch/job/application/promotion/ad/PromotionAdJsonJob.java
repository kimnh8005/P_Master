package kr.co.pulmuone.batch.job.application.promotion.ad;

import kr.co.pulmuone.batch.job.BaseJob;

import kr.co.pulmuone.v1.batch.promotion.ad.PromotionAdJsonBiz;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import kr.co.pulmuone.v1.comm.exception.BaseException;

import java.io.IOException;

@Component("promotionAdJsonJob")
@Slf4j
public class PromotionAdJsonJob implements BaseJob {

    @Autowired
    private PromotionAdJsonBiz companyPromotionAdBiz;

    @Override
    public void run(String[] params) throws BaseException  {
        try {
            companyPromotionAdBiz.runMakeJson();
        } catch (Exception e) {
            throw new BaseException(e.getMessage());
        }
    }
}
