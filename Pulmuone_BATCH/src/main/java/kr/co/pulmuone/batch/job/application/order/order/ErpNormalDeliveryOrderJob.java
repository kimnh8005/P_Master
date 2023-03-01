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
 * 용인물류 일반배송 주문 배치 Job
 *
 * 배치번호: 43
 *
 * </PRE>
 */

@Component("ErpNormalDeliveryOrderJob")
@Slf4j
@RequiredArgsConstructor
public class ErpNormalDeliveryOrderJob implements BaseJob {

    @Qualifier("orderErpBizImpl")
    @Autowired
    private final OrderErpBiz orderErpBiz;

    public void run(String[] params) throws Exception {

        log.info("======"+ErpApiEnums.ErpServiceType.ERP_NORMAL_DELIVERY_ORDER.getCodeName()+"======");

        // 파라미터 있으면 외부몰 분리주문배치
        if(params.length > 0) {
            orderErpBiz.addOrderIfCustordInpByErp(ErpApiEnums.ErpServiceType.ERP_NORMAL_DELIVERY_ORDER, params[2]);
        }
        // 없으면 전체주문배치
        else {
            orderErpBiz.addOrderIfCustordInpByErp(ErpApiEnums.ErpServiceType.ERP_NORMAL_DELIVERY_ORDER);
        }

    }
}
