package kr.co.pulmuone.batch.job.application.goods.item;

import kr.co.pulmuone.batch.job.BaseJob;
import kr.co.pulmuone.v1.batch.goods.item.BatchGoodsItemPriceBiz;
import kr.co.pulmuone.v1.batch.promotion.event.PromotionEventBatchBiz;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("itemPriceOrigCorrectionJob")
@Slf4j
public class ItemPriceOrigCorrectionJob implements BaseJob {
    /*
    배치명 : 품목가격 - 반영 전 원가보정 배치
    배치번호 : 115
     */

    @Autowired
    private BatchGoodsItemPriceBiz batchGoodsItemPriceBiz;

    @Override
    public void run(String[] params) {
        batchGoodsItemPriceBiz.runItemPriceOrigCorrection();
    }

}
