package kr.co.pulmuone.batch.job.application.order.inside;

import kr.co.pulmuone.v1.comm.exception.BaseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import kr.co.pulmuone.batch.job.BaseJob;
import kr.co.pulmuone.v1.batch.order.inside.BuyFinalizedBiz;

/**
 * <PRE>
 * Forbiz Korea
 * 구매확정 배치 Job
 * </PRE>
 */

@Component("buyFinalizedOrderJob")
public class BuyFinalizedOrderJob implements BaseJob {

    @Autowired
    private BuyFinalizedBiz buyFinalizedBiz;		// 구매확정 배치 Biz

    public void run(String[] params) throws Exception {
    	// 구매확정 배치 실행
    	buyFinalizedBiz.runBuyFinalizedSetUp();
    }
}