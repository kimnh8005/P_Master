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
 * 반품매출 주문 배치 Job
 *
 * 배치번호: 63
 *
 * </PRE>
 */

@Component("ErpReturnSalesOrderJob")
@Slf4j
@RequiredArgsConstructor
public class ErpReturnSalesOrderJob implements BaseJob {

    @Qualifier("orderErpBizImpl")
    @Autowired
    private final OrderErpBiz orderErpBiz;

    public void run(String[] params) throws Exception {

        // 풀무원식품
        log.info("======"+ErpApiEnums.ErpServiceType.ERP_RETURN_SALES_ORDER_FOOD.getCodeName()+"======");
        orderErpBiz.addOrderIfCustordInpByErp(ErpApiEnums.ErpServiceType.ERP_RETURN_SALES_ORDER_FOOD);

        // 올가홀푸드
        log.info("======"+ErpApiEnums.ErpServiceType.ERP_RETURN_SALES_ORDER_ORGA.getCodeName()+"======");
        orderErpBiz.addOrderIfCustordInpByErp(ErpApiEnums.ErpServiceType.ERP_RETURN_SALES_ORDER_ORGA);

    }
}
