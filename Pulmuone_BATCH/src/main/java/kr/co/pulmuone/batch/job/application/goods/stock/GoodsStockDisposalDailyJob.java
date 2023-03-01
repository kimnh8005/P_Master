package kr.co.pulmuone.batch.job.application.goods.stock;

import kr.co.pulmuone.batch.job.BaseJob;
import kr.co.pulmuone.v1.batch.goods.stock.BatchGoodsStockBiz;
import kr.co.pulmuone.v1.comm.exception.BaseException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("goodsStockDisposalDailyJob")
@Slf4j
@RequiredArgsConstructor
public class GoodsStockDisposalDailyJob implements BaseJob {

    @Autowired
    private BatchGoodsStockBiz batchGoodsStockBiz;

    @Override
    public void run(String[] params) {

        try {

            batchGoodsStockBiz.runGoodsStockDisposalDailyJob();

        } catch (BaseException e) {

            log.error(e.getMessage(), e);

        }

    }

}
