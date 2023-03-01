package kr.co.pulmuone.batch.job.application.order.calculate;

import kr.co.pulmuone.batch.job.BaseJob;
import kr.co.pulmuone.v1.batch.order.calculate.CalculateBiz;
import kr.co.pulmuone.v1.comm.exception.BaseException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

/**
 * <PRE>
 * Forbiz Korea
 * 정산을 위한 임직원 정보 저장 배치 Job
 *
 * 배치번호: 81번
 *
 * </PRE>
 */

@Component("EmployeeCalculateInfoJob")
@Slf4j
@RequiredArgsConstructor
public class EmployeeCalculateInfoJob implements BaseJob {

    @Qualifier("calculateBizImpl")
    @Autowired
    private final CalculateBiz calculateBiz;

    public void run(String[] params) throws Exception {

        log.info("======정산을 위한 임직원 정보 저장 배치======");
        calculateBiz.addEmployeeCalculateInfo();

    }
}
