package kr.co.pulmuone.batch.job.application.order.order;

import kr.co.pulmuone.batch.domain.service.system.SlackService;
import kr.co.pulmuone.batch.job.BaseJob;
import kr.co.pulmuone.v1.batch.order.order.OrderErpBiz;
import kr.co.pulmuone.v1.comm.enums.ErpApiEnums;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

/**
 * <PRE>
 * Forbiz Korea
 * 외부몰 주문 배치 Job
 *
 * 배치번호: 134
 *
 * </PRE>
 */

@Component("ErpOutMallDeliveryOrderJob")
@Slf4j
@RequiredArgsConstructor
public class ErpOutMallDeliveryOrderJob implements BaseJob {

    @Qualifier("orderErpBizImpl")
    @Autowired
    private final OrderErpBiz orderErpBiz;

    @Autowired
    private SlackService slackService;

    public void run(String[] params) throws Exception {

        log.info("======외부몰 주문 배치======");

        List<String> omSellersIdList = null;
        List<String> notSendOmSellersIdList = null;

        if(params.length > 2 && params[2].length() > 0) {
            omSellersIdList = Arrays.asList(params[2].split(","));
        } else {
            omSellersIdList = orderErpBiz.getErpOutMallIfSellerList("Y");
            notSendOmSellersIdList = orderErpBiz.getErpOutMallIfSellerList("N");
        }

        log.info("====== omSellersIdList : " + omSellersIdList);


        if(omSellersIdList.size() > 0) {
            log.info("======외부몰 용인물류 일반배송 주문 배치======");
            orderErpBiz.addOrderIfCustordInpByErp(ErpApiEnums.ErpServiceType.ERP_NORMAL_DELIVERY_ORDER, omSellersIdList);

            log.info("======외부몰 CJ백암물류 배송 주문 배치======");
            orderErpBiz.addOrderIfCustordInpByErp(ErpApiEnums.ErpServiceType.ERP_CJ_ORDER, omSellersIdList);

            log.info("======외부몰올가 산지직송 배송 주문 배치======");
            orderErpBiz.addEtcOrderIfInpByErp(ErpApiEnums.ErpServiceType.ERP_ORGA_ETC_ORDER, omSellersIdList);
        }

        // 물류 전송이 되지 않은리스트는 슬랙으로 노티
        try {
            if(notSendOmSellersIdList != null && notSendOmSellersIdList.size() > 0) {
                String content = "[배치번호 : 134] 물류 I/F 제외된 판매처코드 : " + notSendOmSellersIdList;
                slackService.notify(content);
            }
        } catch (Exception ex) {
            log.info("====== not send omSellersIdList : " + notSendOmSellersIdList);
        }
    }
}
