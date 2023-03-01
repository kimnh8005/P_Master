package kr.co.pulmuone.batch.job.application.goods.stock;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import kr.co.pulmuone.v1.batch.goods.stock.BatchGoodsStockReCalQtyBiz;
import kr.co.pulmuone.v1.comm.exception.BaseException;
import kr.co.pulmuone.batch.job.BaseJob;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component("goodsStockReCalQtyJob")
@Slf4j
@RequiredArgsConstructor
public class GoodsStockReCalQtyJob implements BaseJob {

    @Autowired
    private BatchGoodsStockReCalQtyBiz batchGoodsStockReCalQtyBiz;

    @Override
    public void run(String[] params) {

        try {

        	batchGoodsStockReCalQtyBiz.runGoodsStockReCalQtyJob();

        } catch (BaseException e) {

            log.error(e.getMessage(), e);

        }

    }

}
