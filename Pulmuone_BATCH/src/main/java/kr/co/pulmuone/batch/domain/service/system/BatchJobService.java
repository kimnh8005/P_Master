package kr.co.pulmuone.batch.domain.service.system;

import kr.co.pulmuone.batch.domain.model.system.BatchJob;

import java.util.Optional;

public interface BatchJobService {

    Optional<BatchJob> getBatchJobByBatchNoAndUsable(long batchNo);

//    List<BatchJob> getBatchJobUsableList(boolean useable);

}
