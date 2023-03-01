package kr.co.pulmuone.batch.eon.domain.service.system;

import kr.co.pulmuone.batch.eon.domain.model.system.BatchJobExecution;

public interface BatchJobExecutionService {

    boolean isStarted(long batchNo);

    BatchJobExecution startJobExecution(long batchNo, String userId);

    BatchJobExecution completeJobExecution(long jobExecutionId, long batchNo);

    BatchJobExecution failJobExecution(long jobExecutionId, long batchNo, String errorMsg);

    BatchJobExecution failJobExecutionLastJob(long batchNo);

//    BatchJobExecution isNotBatchExecCheck(long batchNo, Date startTime);
//
//    BatchJobExecution changeExecutionStatusToForceFailed(long batchNo);
}
