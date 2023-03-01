package kr.co.pulmuone.batch.job.application.order.insidesub;

import kr.co.pulmuone.batch.job.BaseJob;
import kr.co.pulmuone.v1.batch.order.insidesub.OrderIsLimitAfterCheckBiz;
import kr.co.pulmuone.v1.comm.exception.BaseException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

/**
 * <PRE>
 * Forbiz Korea
 * 용인물류 일반배송 주문체크 후 제한 배치 Job
 *
 * 배치번호: 69
 *
 * </PRE>
 */

@Component("OrderIsLimitAfterCheckJob")
@Slf4j
@RequiredArgsConstructor
public class OrderIsLimitAfterCheckJob implements BaseJob {

    @Qualifier("orderIsLimitAfterCheckBizImpl")
    @Autowired
    private final OrderIsLimitAfterCheckBiz orderIsLimitAfterCheckBiz;

    public void run(String[] params) throws Exception {

        log.info("======용인물류 일반배송 주문체크 후 제한 배치======");
        orderIsLimitAfterCheckBiz.normalDeliveryOrderIsLimitAfterCheck();

    }
}
