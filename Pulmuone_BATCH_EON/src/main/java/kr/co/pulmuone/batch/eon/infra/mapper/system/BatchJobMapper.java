package kr.co.pulmuone.batch.eon.infra.mapper.system;

import kr.co.pulmuone.batch.eon.domain.model.system.BatchJob;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@Mapper
public interface BatchJobMapper {

    Optional<BatchJob> findByBatchNoAndUsable(@Param("batchNo") Long batchNo);

//    List<BatchJob> findAllByUsableAndScheduleIsNotNullOrderByBatchNo(boolean usable);
}
