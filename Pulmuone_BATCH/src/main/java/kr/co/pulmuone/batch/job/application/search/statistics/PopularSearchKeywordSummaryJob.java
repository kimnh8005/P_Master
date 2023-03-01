package kr.co.pulmuone.batch.job.application.search.statistics;

import kr.co.pulmuone.v1.display.dictionary.service.SearchWordLogBiz;
import kr.co.pulmuone.batch.job.BaseJob;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Calendar;

@Component
@RequiredArgsConstructor
public class PopularSearchKeywordSummaryJob implements BaseJob {

    /**
     * batchNo: 20
     */

    @Autowired
    private SearchWordLogBiz searchWordLogBiz;

    @Override
    public void run(String[] params) {

        searchWordLogBiz.deleteOldSearchWordLog();

        Calendar cal = Calendar.getInstance();
        cal.add(cal.DATE, -1);
        String targetDate = new SimpleDateFormat("yyyy-MM-dd").format(cal.getTime());

        searchWordLogBiz.makeSearchWordDailyStatistics(targetDate);
    }
}
