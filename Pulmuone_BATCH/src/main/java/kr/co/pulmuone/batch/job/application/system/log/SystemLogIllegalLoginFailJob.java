package kr.co.pulmuone.batch.job.application.system.log;

import kr.co.pulmuone.batch.job.BaseJob;
import kr.co.pulmuone.v1.batch.system.log.SystemLogBatchBiz;
import kr.co.pulmuone.v1.comm.exception.BaseException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("systemLogIllegalLoginFailJob")
@Slf4j
public class SystemLogIllegalLoginFailJob implements BaseJob {
    /*
    배치명 : 시스템로그 - 부정거래탐지 - 로그인실패
    배치번호 : 123
     */

    @Autowired
    private SystemLogBatchBiz systemLogBatchBiz;

    @Override
    public void run(String[] params) throws BaseException {
        systemLogBatchBiz.runDetectIllegalLoginFail();
    }
}