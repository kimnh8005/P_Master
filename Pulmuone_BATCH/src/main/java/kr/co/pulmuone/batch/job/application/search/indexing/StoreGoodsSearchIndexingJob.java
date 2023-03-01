package kr.co.pulmuone.batch.job.application.search.indexing;

import kr.co.pulmuone.batch.job.BaseJob;
import kr.co.pulmuone.v1.search.indexer.service.IndexBiz;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("StoreGoodsSearchIndexingJob")
@RequiredArgsConstructor
public class StoreGoodsSearchIndexingJob implements BaseJob {

    /**
     * batchNo: 113
     *
     */

    @Autowired
    private IndexBiz indexBiz;

    @Override
    public void run(String[] params) throws Exception {
            indexBiz.indexStoreGoods();
    }
}

