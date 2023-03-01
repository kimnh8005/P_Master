package kr.co.pulmuone.v1.batch.policy.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Service
public class PolicyConfigBatchBizImpl implements PolicyConfigBatchBiz {

    @Autowired
    private PolicyConfigBatchService policyConfigBatchService;

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false)
    public String getConfigValue(String psKey) {
        return policyConfigBatchService.getConfigValue(psKey);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false)
    public String getHolidayYn(String nowDate) {
        return policyConfigBatchService.getHolidayYn(nowDate);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false)
    public void runSetHoliday() throws IOException {
        policyConfigBatchService.runSetHoliday();
    }
}
