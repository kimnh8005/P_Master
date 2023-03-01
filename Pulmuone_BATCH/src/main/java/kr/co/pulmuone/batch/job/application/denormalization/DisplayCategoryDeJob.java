package kr.co.pulmuone.batch.job.application.denormalization;

import kr.co.pulmuone.batch.job.BaseJob;
import kr.co.pulmuone.v1.batch.denormalization.DenormalizationBatchBiz;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("DisplayCategoryDeJob")
@RequiredArgsConstructor
public class DisplayCategoryDeJob implements BaseJob {

    /**
     * 전시 카테고리 비정규화 배치
     * batchNo: 67
     */

    @Autowired
    private DenormalizationBatchBiz denormalizationBatchBiz;

    @Override
    public void run(String[] params) throws Exception {
        denormalizationBatchBiz.displayCategoryDenormalization();
    }
}
