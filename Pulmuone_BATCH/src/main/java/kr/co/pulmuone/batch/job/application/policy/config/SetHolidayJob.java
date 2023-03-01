package kr.co.pulmuone.batch.job.application.policy.config;

import kr.co.pulmuone.v1.batch.policy.config.PolicyConfigBatchBiz;
import kr.co.pulmuone.v1.batch.promotion.event.PromotionEventBatchBiz;
import kr.co.pulmuone.v1.comm.exception.BaseException;
import kr.co.pulmuone.batch.job.BaseJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component("setHolidayJob")
@Slf4j
public class SetHolidayJob implements BaseJob {

    @Autowired
    private PolicyConfigBatchBiz policyConfigBatchBiz;

    @Override
    public void run(String[] params) throws BaseException {
        try {
            policyConfigBatchBiz.runSetHoliday();
        } catch (IOException e) {
            throw new BaseException(e.getMessage());
        }
    }

}
