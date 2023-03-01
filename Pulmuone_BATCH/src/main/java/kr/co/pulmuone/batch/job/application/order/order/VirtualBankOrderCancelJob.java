package kr.co.pulmuone.batch.job.application.order.order;

import kr.co.pulmuone.batch.job.BaseJob;
import kr.co.pulmuone.v1.batch.order.order.VirtualBankOrderCancelBiz;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("virtualBankOrderCancelJob")
@Slf4j
@RequiredArgsConstructor
public class VirtualBankOrderCancelJob implements BaseJob {

    @Autowired
    private VirtualBankOrderCancelBiz virtualBankOrderCancelBiz;

    @Override
    public void run(String[] params) throws Exception {
        virtualBankOrderCancelBiz.runVirtualBankOrderCancel();
    }
}