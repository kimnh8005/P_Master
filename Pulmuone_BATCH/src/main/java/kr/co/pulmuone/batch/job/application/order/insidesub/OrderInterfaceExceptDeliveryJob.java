package kr.co.pulmuone.batch.job.application.order.insidesub;

import kr.co.pulmuone.batch.job.BaseJob;
import kr.co.pulmuone.v1.batch.order.insidesub.OrderInterfaceExceptDeliveryBiz;
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
 * 주문 I/F 외 배송준비중 배치 Job
 *
 * 배치번호:
 *
 * </PRE>
 */

@Component("OrderInterfaceExceptDeliveryJob")
@Slf4j
@RequiredArgsConstructor
public class OrderInterfaceExceptDeliveryJob implements BaseJob {

    @Qualifier("orderInterfaceExceptDeliveryBizImpl")
    @Autowired
    private final OrderInterfaceExceptDeliveryBiz orderInterfaceExceptDeliveryBiz;

    public void run(String[] params) throws Exception {

        log.info("======주문 I/F 외 배송준비중 배치======");
        orderInterfaceExceptDeliveryBiz.orderDeliveryPreparingUpdate();

    }
}
