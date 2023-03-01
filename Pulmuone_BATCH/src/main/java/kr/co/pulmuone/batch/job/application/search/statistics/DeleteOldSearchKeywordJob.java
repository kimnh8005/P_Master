package kr.co.pulmuone.batch.job.application.search.statistics;

import kr.co.pulmuone.v1.display.dictionary.service.SearchWordLogBiz;
import kr.co.pulmuone.batch.job.BaseJob;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DeleteOldSearchKeywordJob implements BaseJob {

    /**
     * batchNo: 24
     */

    @Autowired
    private SearchWordLogBiz searchWordLogBiz;

    @Override
    public void run(String[] params) {
        searchWordLogBiz.deleteOldSearchWordLog();
    }
}
