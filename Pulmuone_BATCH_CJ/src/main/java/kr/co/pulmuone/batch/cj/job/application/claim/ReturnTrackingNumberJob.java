package kr.co.pulmuone.batch.cj.job.application.claim;

import kr.co.pulmuone.batch.cj.domain.service.claim.ReturnTrackingNumberBiz;
import kr.co.pulmuone.batch.cj.job.BaseJob;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component("returnTrackingNumberJob")  // Batch Job ID : 110
@Slf4j
@RequiredArgsConstructor
public class ReturnTrackingNumberJob implements BaseJob {

    private final ReturnTrackingNumberBiz returnTrackingNumberBiz;

    /**
     * 기본설정 추가 해야하는 부분은 "Batch CJ Default Setting" 로 검색하세요
     *
     */

    @Override
    @Transactional
    public void run(String[] params){
        masterDbTest();
    }

    private void masterDbTest(){

        log.info("ReturnTrackingNumberJob {}", "[시작]");

        returnTrackingNumberBiz.execute();

        log.info("ReturnTrackingNumberJob {}", "[끝]\n\n\n\n");
    }
}