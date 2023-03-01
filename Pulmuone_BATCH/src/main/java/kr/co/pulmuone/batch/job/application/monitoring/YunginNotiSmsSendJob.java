package kr.co.pulmuone.batch.job.application.monitoring;

import kr.co.pulmuone.batch.job.BaseJob;
import kr.co.pulmuone.v1.batch.monitoring.YunginNotiSmsSendBatchBiz;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("yunginNotiSmsSend")
@Slf4j
@RequiredArgsConstructor
public class YunginNotiSmsSendJob implements BaseJob {

    @Autowired
    private YunginNotiSmsSendBatchBiz yunginNotiSmsSendBatchBiz;

    @Override
    public void run(String[] params) throws Exception {
        log.info("====== YunginNotiSmsSendJob (용인물류 D1, D2출고 주문 택배, 새벽을 기준으로 주문건수(냉동건수), 상품수량 SMS전송) Start ======");
        log.info("params[2]====sms발송대상자 코드====="+params[2]);

        yunginNotiSmsSendBatchBiz.yunginNotiSmsSend(params[2]);
    }

}
