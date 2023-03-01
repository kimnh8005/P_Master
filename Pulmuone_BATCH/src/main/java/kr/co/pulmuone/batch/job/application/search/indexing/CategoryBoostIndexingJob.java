package kr.co.pulmuone.batch.job.application.search.indexing;

import kr.co.pulmuone.v1.search.indexer.service.IndexBiz;
import kr.co.pulmuone.batch.job.BaseJob;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("CategoryBoostIndexingJob")
@RequiredArgsConstructor
public class CategoryBoostIndexingJob implements BaseJob {

    /**
     * batchNo: 29
     */

    @Autowired
    private IndexBiz indexBiz;

    @Override
    public void run(String[] params) throws Exception {
        indexBiz.indexCategoryBoost();
    }
}
