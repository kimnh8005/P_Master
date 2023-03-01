package kr.co.pulmuone.batch.cj.infra.mapper.system;

import kr.co.pulmuone.batch.cj.domain.model.system.BatchJobExecution;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface BatchJobExecutionMapper {

    BatchJobExecution findFirstByBatchNoOrderByJobExecutionIdDesc(@Param("batchNo") long batchNo);

    BatchJobExecution findByJobExecutionIdAndBatchNo(@Param("jobExecutionId") long jobExecutionId, @Param("batchNo") long batchNo);

//    BatchJobExecution findFirstByBatchNoAndStartTimeGreaterThanEqual(long batchNo, Date startTime);

    int putBatchJobExecution(BatchJobExecution batchJobExecution);

    int setBatchJobExecution(BatchJobExecution batchJobExecution);
}
