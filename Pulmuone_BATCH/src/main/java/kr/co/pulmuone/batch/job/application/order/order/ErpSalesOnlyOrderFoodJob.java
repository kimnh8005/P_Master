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
 * 매출만 연동 주문 (풀무원식품) 배치 Job
 *
 * 배치번호:
 *
 * </PRE>
 */

@Component("ErpSalesOnlyOrderFoodJob")
@Slf4j
@RequiredArgsConstructor
public class ErpSalesOnlyOrderFoodJob implements BaseJob {

    @Qualifier("orderErpBizImpl")
    @Autowired
    private final OrderErpBiz orderErpBiz;

    public void run(String[] params) throws Exception {

        log.info("======"+ErpApiEnums.ErpServiceType.ERP_SALES_ONLY_ORDER_FOOD.getCodeName()+"======");
        orderErpBiz.addOrderIfCustordInpByErp(ErpApiEnums.ErpServiceType.ERP_SALES_ONLY_ORDER_FOOD);

    }
}
