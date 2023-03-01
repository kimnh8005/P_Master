package kr.co.pulmuone.batch.job.application.order.ifday;


import kr.co.pulmuone.batch.job.BaseJob;
import kr.co.pulmuone.v1.batch.order.ifday.IfDayChangeExcelBiz;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
/**
 * BATCH ID : 108
 */
@Component("ifDayChangeJob")
@Slf4j
public class IfDayChangeJob implements BaseJob {
    @Autowired
    private IfDayChangeExcelBiz ifDayChangeExcelBiz;

    @Override
    public void run(String[] params) {


        try {

            ifDayChangeExcelBiz.putIfDayChange();

        } catch (Exception e) {
            e.printStackTrace();
            log.error("=========orderExcelInfoJob error============");
        }
    }

}
