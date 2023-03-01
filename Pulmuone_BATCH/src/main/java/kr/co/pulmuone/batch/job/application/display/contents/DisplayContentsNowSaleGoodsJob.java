package kr.co.pulmuone.batch.job.application.display.contents;

import kr.co.pulmuone.v1.batch.display.contents.DisplayContentsBatchBiz;
import kr.co.pulmuone.batch.job.BaseJob;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("displayContentsNowSaleGoodsJob")
public class DisplayContentsNowSaleGoodsJob implements BaseJob {
    /*
    배치명 : MALL - 메인 - 지금세일관 - 추천 자동상품
    배치번호 : 30
     */

    @Autowired
    private DisplayContentsBatchBiz displayContentsBatchBiz;

    @Override
    public void run(String[] params) {
        displayContentsBatchBiz.runDisplayContentsNowSaleGoods();
    }
}
