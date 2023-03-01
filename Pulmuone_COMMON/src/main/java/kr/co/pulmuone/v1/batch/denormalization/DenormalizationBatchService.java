package kr.co.pulmuone.v1.batch.denormalization;

import kr.co.pulmuone.v1.comm.mappers.batch.master.denormalization.DenormalizationBatchMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
public class DenormalizationBatchService {

    @Autowired
    private DenormalizationBatchMapper denormalizationBatchMapper;

    /**
     * 전시 카테고리 비정규화 배치
     *
     */
    void displayCategoryDenormalization () {
        denormalizationBatchMapper.displayCategoryDenormalizationBatch();
    }

}
