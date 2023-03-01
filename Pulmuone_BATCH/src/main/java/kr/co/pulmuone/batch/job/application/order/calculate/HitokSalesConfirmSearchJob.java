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
 * 하이톡 매출확정 조회 배치 Job
 *
 * 배치번호: 145번
 *
 * </PRE>
 */

@Component("HitokSalesConfirmSearchJob")
@Slf4j
@RequiredArgsConstructor
public class HitokSalesConfirmSearchJob implements BaseJob {

    @Qualifier("calculateBizImpl")
    @Autowired
    private final CalculateBiz calculateBiz;

    public void run(String[] params) throws Exception {

        log.info("======하이톡 일배 매출확정 조회 배치======");
        calculateBiz.hitokDaliySalesConfirmSearch();

        log.info("======하이톡 택배 매출확정 조회 배치======");
        calculateBiz.hitokSalesConfirmSearch();

    }
}
