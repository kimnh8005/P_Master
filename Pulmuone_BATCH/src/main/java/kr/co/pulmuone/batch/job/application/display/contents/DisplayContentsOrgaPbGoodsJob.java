package kr.co.pulmuone.batch.job.application.display.contents;

import kr.co.pulmuone.batch.job.BaseJob;
import kr.co.pulmuone.v1.batch.display.contents.DisplayContentsBatchBiz;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("displayContentsOrgaPbGoodsJob")
public class DisplayContentsOrgaPbGoodsJob implements BaseJob {
    /*
    배치명 : MALL - 메인 - 올가 - PB관 - 자동상품
    배치번호 : 100
     */

    @Autowired
    private DisplayContentsBatchBiz displayContentsBatchBiz;

    @Override
    public void run(String[] params) throws Exception {
        displayContentsBatchBiz.runDisplayContentsOrgaPbGoods();
    }
}
