package kr.co.pulmuone.batch.cj.job.application.claim;

import kr.co.pulmuone.batch.cj.domain.service.claim.ReturnDeliveryReceiptBiz;
import kr.co.pulmuone.batch.cj.job.BaseJob;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * <PRE>
 * Forbiz Korea
 * CJ 반품택배 접수 배치 Job
 *
 * 배치번호: 117
 *
 * </PRE>
 */
@Component("returnDeliveryReceiptJob")
@Slf4j
@RequiredArgsConstructor
public class ReturnDeliveryReceiptJob implements BaseJob {

    @Qualifier("returnDeliveryReceiptBizImpl")
    @Autowired
    private final ReturnDeliveryReceiptBiz returnDeliveryReceiptBiz;

    /**
     * 기본설정 추가 해야하는 부분은 "Batch CJ Default Setting" 로 검색하세요
     *
     */
    @Override
    public void run(String[] params){
        log.info("ReturnDeliveryReceiptJob {}", "[시작]");

        returnDeliveryReceiptBiz.returnDeliveryReceipt();

        log.info("ReturnDeliveryReceiptJob {}", "[끝]\n\n\n\n");
    }

}