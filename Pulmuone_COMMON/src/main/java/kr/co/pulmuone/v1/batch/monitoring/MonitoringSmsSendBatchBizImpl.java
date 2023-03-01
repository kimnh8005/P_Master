package kr.co.pulmuone.v1.batch.monitoring;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MonitoringSmsSendBatchBizImpl implements MonitoringSmsSendBatchBiz {

    @Autowired
    private MonitoringSmsSendBatchService monitoringSmsSendBatchService;


    /**
     * 회원가입자수, 주문건수, 결제금액 SMS로 발송
     * @param
     * @return void
     * @throws Exception
     */
    @Override
    public void smsMonitoringInfoSend(String stCommonCodeMasterId) throws Exception {
        addUserOrderPaymentInfo();

        execMonitoringInfoSend(stCommonCodeMasterId);
    }


    /**
     * 회원가입자수, 주문건수, 결제금액 table에 insert
     * @param
     * @return void
     * @throws Exception
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = { Exception.class })
    protected void addUserOrderPaymentInfo() throws Exception {
        monitoringSmsSendBatchService.addUserOrderPaymentInfo();
    }

    /**
     * 회원가입자수, 주문건수, 결제금액 데이터를 수신자에게 SMS발송
     * @param stCommonCodeMasterId
     * @return void
     * @throws Exception
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = { Exception.class })
    protected void execMonitoringInfoSend(String stCommonCodeMasterId) throws Exception {
        monitoringSmsSendBatchService.execMonitoringInfoSend(stCommonCodeMasterId);
    }
}