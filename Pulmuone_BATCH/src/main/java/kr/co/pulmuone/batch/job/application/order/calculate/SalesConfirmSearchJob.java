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
 * 매출확정 조회 배치 Job
 *
 * 배치번호: 82번
 *
 * </PRE>
 */

@Component("SalesConfirmSearchJob")
@Slf4j
@RequiredArgsConstructor
public class SalesConfirmSearchJob implements BaseJob {

    @Qualifier("calculateBizImpl")
    @Autowired
    private final CalculateBiz calculateBiz;

    public void run(String[] params) throws Exception {

        log.info("======매출확정 조회 배치======");
        calculateBiz.salesConfirmSearch();

    }
}
