package kr.co.pulmuone.v1.batch.policy.config;

import java.io.IOException;

public interface PolicyConfigBatchBiz {

    String getConfigValue(String psKey);

    String getHolidayYn(String nowDate);

    void runSetHoliday() throws IOException;

}
