package kr.co.pulmuone.batch.job.application.order.regular;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import kr.co.pulmuone.batch.job.BaseJob;
import kr.co.pulmuone.v1.batch.order.regular.RegularOrderBiz;
import kr.co.pulmuone.v1.comm.enums.OrderEnums;
import kr.co.pulmuone.v1.comm.exception.BaseException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * <PRE>
 * Forbiz Korea
 * 정기배송 주문 생성 배치 Job
 * </PRE>
 */

@Component("RegularOrderCreateJob")
@Slf4j
@RequiredArgsConstructor
public class RegularOrderCreateJob implements BaseJob {

    @Qualifier("regularOrderBizImpl")
    @Autowired
    private final RegularOrderBiz regularOrderBiz;

    public void run(String[] params) {

        log.info("======"+OrderEnums.RegularOrderBatchTypeCd.CREATE_ORDER.getCodeName()+"======");

        try {

        	// 정기배송 주문 생성
        	regularOrderBiz.putRegularOrderResult();

        } catch (Exception e) {

            log.error(e.getMessage(), e);

        }

    }
}
