package kr.co.pulmuone.batch.job.application.goods.stock;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import kr.co.pulmuone.batch.job.BaseJob;
import kr.co.pulmuone.v1.batch.goods.stock.BatchItemStockByGoodsSaleStatusBiz;
import kr.co.pulmuone.v1.comm.exception.BaseException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component("goodsStockMinutelyJob")
@Slf4j
@RequiredArgsConstructor
public class GoodsStockMinutelyJob implements BaseJob {

	@Autowired
	private BatchItemStockByGoodsSaleStatusBiz batchItemStockByGoodsSaleStatusBiz;

	@Override
	public void run(String[] params) {
		try {
			batchItemStockByGoodsSaleStatusBiz.goodsStockMinutelyJob();
		}
		catch (BaseException e) {
			log.error(e.getMessage(), e);
		}
	}
}
