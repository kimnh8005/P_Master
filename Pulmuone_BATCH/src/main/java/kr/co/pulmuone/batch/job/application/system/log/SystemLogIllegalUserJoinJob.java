package kr.co.pulmuone.batch.job.application.system.log;

import kr.co.pulmuone.batch.job.BaseJob;
import kr.co.pulmuone.v1.batch.system.log.SystemLogBatchBiz;
import kr.co.pulmuone.v1.comm.exception.BaseException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("systemLogIllegalUserJoinJob")
@Slf4j
public class SystemLogIllegalUserJoinJob implements BaseJob {
    /*
    배치명 : 시스템로그 - 부정거래탐지 - 회원가입
    배치번호 : 122
     */

    @Autowired
    private SystemLogBatchBiz systemLogBatchBiz;

    @Override
    public void run(String[] params) throws BaseException {
        systemLogBatchBiz.runDetectIllegalUserJoin();
    }
}
