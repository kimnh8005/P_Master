package kr.co.pulmuone.batch.eon.job.application.system.common;

import kr.co.pulmuone.batch.eon.domain.service.system.BatchJobExecutionService;
import kr.co.pulmuone.batch.eon.job.BaseJob;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component("systemBatchStatusFailJob")
@Slf4j
@RequiredArgsConstructor
public class SystemBatchStatusFailJob implements BaseJob {

    @Autowired
    private BatchJobExecutionService batchJobExecutionService;

    /***
     * STARTED 로 멈춰버린 오류배치 강제로 FAILED 처리
     * @param params
     * @throws Exception
     */
    @Override
    @Transactional
    public void run(String[] params) throws Exception  {

        long failBatchNo = 0L;

        if(params.length != 3) throw new Exception("파라미터 오류");

        if(params[2] == null || params[2].trim().length() == 0) throw new Exception("매개변수 오류");

        try {
            failBatchNo = Long.parseLong(params[2]);
            batchJobExecutionService.failJobExecutionLastJob(failBatchNo);
        } catch(Exception ex) {
            throw new Exception("매개변수 오류");
        }
    }

}
