package kr.co.pulmuone.batch.job.application.order.order;

import kr.co.pulmuone.batch.job.BaseJob;
import kr.co.pulmuone.v1.batch.order.order.OrderErpBiz;
import kr.co.pulmuone.v1.comm.enums.ErpApiEnums;
import kr.co.pulmuone.v1.comm.exception.BaseException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

/**
 * <PRE>
 * Forbiz Korea
 * 용인물류 새벽배송 주문 배치 Job
 *
 * 배치번호: 44
 *
 * </PRE>
 */

@Component("ErpDawnDeliveryOrderJob")
@Slf4j
@RequiredArgsConstructor
public class ErpDawnDeliveryOrderJob implements BaseJob {

    @Qualifier("orderErpBizImpl")
    @Autowired
    private final OrderErpBiz orderErpBiz;

    public void run(String[] params) throws Exception {

        log.info("======"+ErpApiEnums.ErpServiceType.ERP_DAWN_DELIVERY_ORDER.getCodeName()+"======");
        orderErpBiz.addOrderIfCustordInpByErp(ErpApiEnums.ErpServiceType.ERP_DAWN_DELIVERY_ORDER);

    }
}
