package kr.co.pulmuone.batch.eon.domain.service.system;

import kr.co.pulmuone.batch.eon.domain.model.system.BatchJob;

import java.util.Optional;

public interface BatchJobService {

    Optional<BatchJob> getBatchJobByBatchNoAndUsable(long batchNo);

//    List<BatchJob> getBatchJobUsableList(boolean useable);

}
