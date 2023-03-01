package kr.co.pulmuone.batch.job.application.system.log;

import kr.co.pulmuone.batch.job.BaseJob;
import kr.co.pulmuone.v1.batch.system.log.SystemLogBatchBiz;
import kr.co.pulmuone.v1.comm.exception.BaseException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("systemLogIllegalOrderPriceJob")
@Slf4j
public class SystemLogIllegalOrderPriceJob implements BaseJob {
    /*
    배치명 : 시스템로그 - 부정거래탐지 - 거래금액
    배치번호 : 125
     */

    @Autowired
    private SystemLogBatchBiz systemLogBatchBiz;

    @Override
    public void run(String[] params) throws BaseException {
        systemLogBatchBiz.runDetectIllegalOrderPrice();
    }
}