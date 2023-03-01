package kr.co.pulmuone.v1.batch.goods.stock;

import kr.co.pulmuone.v1.comm.exception.BaseException;

public interface BatchGoodsStockBiz {

    void runGoodsStockDailyJob() throws BaseException;

    void runGoodsStockHourlyJob() throws BaseException;

    void runGoodsStockDisposalDailyJob() throws BaseException;

}
