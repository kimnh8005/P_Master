package kr.co.pulmuone.batch.job.application.monitoring;

import kr.co.pulmuone.batch.job.BaseJob;
import kr.co.pulmuone.v1.batch.monitoring.MonitoringSmsSendBatchBiz;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("monitoringSmsSend")
@Slf4j
@RequiredArgsConstructor
public class MonitoringSmsSendJob implements BaseJob {

    @Autowired
    private MonitoringSmsSendBatchBiz monitoringSmsSendBatchBiz;

    @Override
    public void run(String[] params) throws Exception {
        log.info("====== MonitoringSmsSendJob (당일 회원수,주문건수,결제금액 SMS전송) Start ======");
        log.info("params[2]====sms발송대상자 코드====="+params[2]);

        monitoringSmsSendBatchBiz.smsMonitoringInfoSend(params[2]);
    }
}
