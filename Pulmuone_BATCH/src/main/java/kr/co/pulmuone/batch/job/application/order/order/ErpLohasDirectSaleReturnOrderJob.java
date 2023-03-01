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
 * 풀무원건강생활(LDS) 반품 주문 배치 Job
 * - 풀무원로하스, 이씰린, 로하스리빙, 브리엔
 *
 * 배치번호: 68
 *
 * </PRE>
 */

@Component("ErpLohasDirectSaleReturnOrderJob")
@Slf4j
@RequiredArgsConstructor
public class ErpLohasDirectSaleReturnOrderJob implements BaseJob {

    @Qualifier("orderErpBizImpl")
    @Autowired
    private final OrderErpBiz orderErpBiz;

    public void run(String[] params) throws Exception {

        log.info("======"+ErpApiEnums.ErpServiceType.ERP_LOHAS_DIRECT_SALE_RETRUN_ORDER.getCodeName()+"======");
        orderErpBiz.addOrderIfCustordInpByErp(ErpApiEnums.ErpServiceType.ERP_LOHAS_DIRECT_SALE_RETRUN_ORDER);

    }
}
