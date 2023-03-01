package kr.co.pulmuone.batch.job.application.order.etc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import kr.co.pulmuone.batch.job.BaseJob;
import kr.co.pulmuone.v1.batch.order.etc.UnreleasedErpBiz;

/**
 * <PRE>
 * Forbiz Korea
 * 미출 조회 배치 Job
 * </PRE>
 */

@Component("unreleasedOrderJob")
public class UnreleasedOrderJob implements BaseJob {

	@Autowired
	private UnreleasedErpBiz unreleasedErpBiz;		// 미출조회 ERP API배치 Biz

    public void run(String[] params) throws Exception {
    	// 미출조회/저장 배치 실행
    	unreleasedErpBiz.runUnreleasedSetUp();
	}
}