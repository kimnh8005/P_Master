package kr.co.pulmuone.batch.job.application.display.contents;

import kr.co.pulmuone.v1.batch.display.contents.DisplayContentsBatchBiz;
import kr.co.pulmuone.batch.job.BaseJob;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("displayContentsBranListJob")
public class DisplayContentsBranListJob implements BaseJob {
    /*
    배치명 : MALL - 메인 - 통합메인 - 브랜드 자동상품
    배치번호 : 28
     */

    @Autowired
    private DisplayContentsBatchBiz displayContentsBatchBiz;

    @Override
    public void run(String[] params) {
        displayContentsBatchBiz.runDisplayContentsBrandList();
    }
}
