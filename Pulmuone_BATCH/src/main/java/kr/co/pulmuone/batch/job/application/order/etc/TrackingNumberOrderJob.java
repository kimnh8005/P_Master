package kr.co.pulmuone.batch.job.application.order.etc;

import kr.co.pulmuone.batch.job.BaseJob;
import kr.co.pulmuone.v1.batch.order.etc.TrackingNumberErpBiz;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * <PRE>
 * Forbiz Korea
 * 송장 조회 배치 Job
 * </PRE>
 */

@Component("trackingNumberOrderJob")
public class TrackingNumberOrderJob implements BaseJob {

    @Autowired
    private TrackingNumberErpBiz trackingNumberErpBiz;		// 송장조회 ERP API배치 Biz

    public void run(String[] params) throws Exception {
    	// 송장조회/저장 배치 실행
    	trackingNumberErpBiz.runTrackingNumberSetUp();
    	// 하이톡 송장 처리 실행 (하이톡에서 변경한 스케줄이 출고 확정된 경우 처리 - 중계서버 srcSvc 값이 하이톡인 경우)
		trackingNumberErpBiz.runHitokTrackingNumberSetUp();
    	// 기타송장 조회/저장 배치 실행
    	trackingNumberErpBiz.runEtcTrackingNumberSetUp();
    }
}
