package kr.co.pulmuone.batch.job.application.goods.stock;

import kr.co.pulmuone.batch.job.BaseJob;
import kr.co.pulmuone.v1.batch.goods.stock.BatchGoodsStockBiz;
import kr.co.pulmuone.v1.batch.goods.stock.BatchGoodsStockOrgaShopBiz;
import kr.co.pulmuone.v1.comm.exception.BaseException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("GoodsStockOrgaShopJob")
@Slf4j
@RequiredArgsConstructor
public class GoodsStockOrgaShopJob implements BaseJob {

    @Autowired
    private BatchGoodsStockOrgaShopBiz batchGoodsStockOrgaShopBiz;

    @Override
    public void run(String[] params) {

        try {
            batchGoodsStockOrgaShopBiz.runGoodsStockOrgaShopJob();
        } catch (BaseException e) {
            log.error(e.getMessage(), e);
        }

    }

}
