package kr.co.pulmuone.batch.eon.domain.service.system;

import kr.co.pulmuone.batch.eon.infra.mapper.system.BatchJobMapper;
import kr.co.pulmuone.batch.eon.domain.model.system.BatchJob;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class BatchJobServiceImpl implements BatchJobService {

    @Autowired
    private final BatchJobMapper mapper;

    public Optional<BatchJob> getBatchJobByBatchNoAndUsable(long batchNo) {
        return mapper.findByBatchNoAndUsable(batchNo);
    }

//    @Override
//    public List<BatchJob> getBatchJobUsableList(boolean usable) {
//        return mapper.findAllByUsableAndScheduleIsNotNullOrderByBatchNo(usable);
//    }
}
