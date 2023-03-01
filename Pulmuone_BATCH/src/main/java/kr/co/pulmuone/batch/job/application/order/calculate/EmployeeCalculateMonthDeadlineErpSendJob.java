package kr.co.pulmuone.batch.job.application.order.calculate;

import kr.co.pulmuone.batch.job.BaseJob;
import kr.co.pulmuone.v1.batch.order.calculate.CalculateBiz;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

/**
 * <PRE>
 * Forbiz Korea
 * 임직원 정산 월마감 erp 전송 배치 Job
 *
 * 배치번호: 148번
 *
 * </PRE>
 */

@Component("EmployeeCalculateMonthDeadlineErpSendJob")
@Slf4j
@RequiredArgsConstructor
public class EmployeeCalculateMonthDeadlineErpSendJob implements BaseJob {

    @Qualifier("calculateBizImpl")
    @Autowired
    private final CalculateBiz calculateBiz;

    public void run(String[] params) throws Exception {

        log.info("======임직원 정산 월마감 erp 전송 배치======");
        calculateBiz.employeeCalculateMonthDeadlineErpSend();

    }
}
