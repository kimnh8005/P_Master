package kr.co.pulmuone.batch.job.application.display.contents;

import kr.co.pulmuone.batch.job.BaseJob;
import kr.co.pulmuone.v1.batch.display.contents.DisplayContentsBatchBiz;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("displayContentsOrgaMainNewGoodsJob")
public class DisplayContentsOrgaMainNewGoodsJob implements BaseJob {
    /*
    배치명 : MALL - 메인 - 올가메인 - 제일먼저만나요 자동상품
    배치번호 : 97
     */

    @Autowired
    private DisplayContentsBatchBiz displayContentsBatchBiz;

    @Override
    public void run(String[] params) throws Exception {
        displayContentsBatchBiz.runDisplayContentsOrgaMainNewGoodsJob();
    }

}