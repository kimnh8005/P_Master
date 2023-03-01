package kr.co.pulmuone.batch.erp.domain.service.system;

import kr.co.pulmuone.batch.erp.domain.model.system.BatchJobExecution;

public interface BatchJobExecutionService {

    boolean isStarted(long batchNo);

    BatchJobExecution startJobExecution(long batchNo, String userId);

    BatchJobExecution completeJobExecution(long jobExecutionId, long batchNo);

    BatchJobExecution failJobExecution(long jobExecutionId, long batchNo, String errorMsg);

//    BatchJobExecution isNotBatchExecCheck(long batchNo, Date startTime);
//
//    BatchJobExecution changeExecutionStatusToForceFailed(long batchNo);
}
