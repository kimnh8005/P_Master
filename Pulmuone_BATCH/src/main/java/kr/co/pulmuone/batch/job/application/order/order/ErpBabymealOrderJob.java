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
 * 베이비밀 주문 배치 Job
 *
 * 배치번호: 54
 *
 * </PRE>
 */

@Component("ErpBabymealOrderJob")
@Slf4j
@RequiredArgsConstructor
public class ErpBabymealOrderJob implements BaseJob {

    @Qualifier("orderErpBizImpl")
    @Autowired
    private final OrderErpBiz orderErpBiz;

    public void run(String[] params) throws Exception {

        // 베이비밀 일일배송 주문
        log.info("======"+ErpApiEnums.ErpServiceType.ERP_BABYMEAL_DAILY_ORDER.getCodeName()+"======");
        orderErpBiz.addOrderIfCustordInpByErp(ErpApiEnums.ErpServiceType.ERP_BABYMEAL_DAILY_ORDER);

        // 베이비밀 택배배송 주문
        log.info("======"+ErpApiEnums.ErpServiceType.ERP_BABYMEAL_NORMAL_ORDER.getCodeName()+"======");
        orderErpBiz.addOrderIfCustordInpByErp(ErpApiEnums.ErpServiceType.ERP_BABYMEAL_NORMAL_ORDER);

    }
}
