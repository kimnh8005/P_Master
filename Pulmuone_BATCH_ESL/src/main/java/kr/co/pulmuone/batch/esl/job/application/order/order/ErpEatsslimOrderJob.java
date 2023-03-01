package kr.co.pulmuone.batch.esl.job.application.order.order;

import kr.co.pulmuone.batch.esl.common.enums.ErpApiEnums;
import kr.co.pulmuone.batch.esl.domain.service.order.order.OrderErpBiz;
import kr.co.pulmuone.batch.esl.job.BaseJob;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

/**
 * <PRE>
 * Forbiz Korea
 * 잇슬림 주문 배치 Job
 *
 * 배치번호: 74
 *
 * </PRE>
 */

@Component("ErpEatsslimOrderJob")
@Slf4j
@RequiredArgsConstructor
public class ErpEatsslimOrderJob implements BaseJob {

    @Qualifier("orderErpBizImpl")
    @Autowired
    private final OrderErpBiz orderErpBiz;

    public void run(String[] params) throws Exception {

        // 일일배송
        log.info("======"+ ErpApiEnums.ErpServiceType.ERP_EATSSLIM_ORDER.getCodeName()+"======");
        orderErpBiz.addEatsslimOrderIfInpByErp(ErpApiEnums.ErpServiceType.ERP_EATSSLIM_ORDER);

        // 택배배송
        log.info("======"+ErpApiEnums.ErpServiceType.ERP_EATSSLIM_NORMAL_DELIVERY_ORDER.getCodeName()+"======");
        orderErpBiz.addEatsslimOrderIfInpByErp(ErpApiEnums.ErpServiceType.ERP_EATSSLIM_NORMAL_DELIVERY_ORDER);

    }
}
