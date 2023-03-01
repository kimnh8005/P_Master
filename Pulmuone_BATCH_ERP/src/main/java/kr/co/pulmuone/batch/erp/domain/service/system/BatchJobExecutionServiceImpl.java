package kr.co.pulmuone.batch.erp.domain.service.system;

import kr.co.pulmuone.batch.erp.common.Constants;
import kr.co.pulmuone.batch.erp.domain.model.system.BatchJobExecution;
import kr.co.pulmuone.batch.erp.domain.model.system.BatchStatus;
import kr.co.pulmuone.batch.erp.domain.service.system.exception.BatchJobNotFinishedException;
import kr.co.pulmuone.batch.erp.domain.service.system.exception.BatchJobNotStartedException;
import kr.co.pulmuone.batch.erp.domain.service.system.exception.NotExistsBatchNoException;
import kr.co.pulmuone.batch.erp.domain.service.system.exception.NotExistsJobExecutionException;
import kr.co.pulmuone.batch.erp.infra.mapper.system.BatchJobExecutionMapper;

import java.util.Arrays;
import java.util.Optional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class BatchJobExecutionServiceImpl implements BatchJobExecutionService {

    @Autowired
    private final BatchJobExecutionMapper mapper;
    private final BatchJobService batchJobService;
    private final SlackService slackService;

    @Override
    public boolean isStarted(long batchNo) {
        return isStartedExecution(mapper.findFirstByBatchNoOrderByJobExecutionIdDesc(batchNo));
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public BatchJobExecution startJobExecution(long batchNo, String userId) {
        if (!Constants.EXCLUDE_DUPLICATE_LIST.contains(batchNo) && isStarted(batchNo)) {
            sendSlackMessage(batchNo);
            throw new BatchJobNotFinishedException(batchNo);
        }
        return putBatchJobExecution(BatchJobExecution.start(batchNo, userId));
    }

    private void sendSlackMessage(long batchNo) {
        slackService.notify(new StringBuilder("`[배치번호: ").append(batchNo).append("]는 실행중입니다.`").toString());
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public BatchJobExecution completeJobExecution(long jobExecutionId, long batchNo) {
        if (!isStarted(batchNo)) {
            throw new BatchJobNotStartedException(batchNo);
        }
        return setBatchJobExecution(getBatchJobExecution(jobExecutionId, batchNo).complete());
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public BatchJobExecution failJobExecution(long jobExecutionId, long batchNo, String errorMsg) {
        if (!isStarted(batchNo)) {
            throw new BatchJobNotStartedException(batchNo);
        }
        return setBatchJobExecution(getBatchJobExecution(jobExecutionId, batchNo).fail(errorMsg));
    }

//    @Override
//    public BatchJobExecution isNotBatchExecCheck(long batchNo ,Date startTime) {
//         return batchJobExecutionRepository.findFirstByBatchNoAndStartTimeGreaterThanEqual(batchNo, startTime)
//            .orElseThrow(NotExistsJobExecutionException::new);
//   }


    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public BatchJobExecution putBatchJobExecution(BatchJobExecution batchJobExecution) {
        mapper.putBatchJobExecution(batchJobExecution);
        long batchJobExecutionId = batchJobExecution.getJobExecutionId();
        return Optional.of(mapper.findByJobExecutionIdAndBatchNo(batchJobExecutionId, batchJobExecution.getBatchNo()))
                .orElseThrow(NotExistsJobExecutionException::new);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public BatchJobExecution setBatchJobExecution(BatchJobExecution batchJobExecution) {
        mapper.setBatchJobExecution(batchJobExecution);
        return Optional.of(mapper.findByJobExecutionIdAndBatchNo(batchJobExecution.getJobExecutionId(), batchJobExecution.getBatchNo()))
                .orElseThrow(NotExistsJobExecutionException::new);
    }

    public BatchJobExecution getBatchJobExecution(long jobExecutionId, long batchNo) {
        return Optional.of(mapper.findByJobExecutionIdAndBatchNo(jobExecutionId, batchNo))
                .orElseThrow(NotExistsJobExecutionException::new);
    }

//    @Override
//    @Transactional(propagation = Propagation.REQUIRES_NEW)
//    public BatchJobExecution changeExecutionStatusToForceFailed(long batchNo) {
//        validateBatchNo(batchNo);
//        Optional<BatchJobExecution> batchJobExecution = batchJobExecutionRepository
//            .findFirstByBatchNoOrderByJobExecutionIdDesc(batchNo);
//
//        if (!isStartedExecution(batchJobExecution)) {
//            throw new RuntimeException("배치가 실행중인 상태에서만 가능합니다.");
//        }
//        return saveBatchJobExecution(batchJobExecution.get().fail("오류에 의해 강제로 실패 처리."));
//    }

    private boolean isStartedExecution(BatchJobExecution batchJobExecution) {
        return Optional.ofNullable(batchJobExecution)
                .map(BatchJobExecution::getStatus)
                .filter(status -> status == BatchStatus.STARTED)
                .isPresent();
    }

    private void validateBatchNo(long batchNo) {
        batchJobService.getBatchJobByBatchNoAndUsable(batchNo).orElseThrow(NotExistsBatchNoException::new);
    }
}
