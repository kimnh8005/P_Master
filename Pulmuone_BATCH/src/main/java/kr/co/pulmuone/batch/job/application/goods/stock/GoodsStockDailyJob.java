package kr.co.pulmuone.batch.job.application.goods.stock;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import kr.co.pulmuone.v1.batch.goods.stock.BatchGoodsStockBiz;
import kr.co.pulmuone.v1.comm.exception.BaseException;
import kr.co.pulmuone.batch.job.BaseJob;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component("goodsStockDailyJob")
@Slf4j
@RequiredArgsConstructor
public class GoodsStockDailyJob implements BaseJob {

    @Autowired
    private BatchGoodsStockBiz batchGoodsStockBiz;

    @Override
    public void run(String[] params) {

        try {

            batchGoodsStockBiz.runGoodsStockDailyJob();

        } catch (BaseException e) {

            log.error(e.getMessage(), e);

        }

    }

}
