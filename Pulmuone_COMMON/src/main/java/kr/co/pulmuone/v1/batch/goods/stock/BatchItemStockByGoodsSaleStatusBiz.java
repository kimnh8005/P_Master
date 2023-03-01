package kr.co.pulmuone.v1.batch.goods.stock;

import kr.co.pulmuone.v1.comm.exception.BaseException;

public interface BatchItemStockByGoodsSaleStatusBiz {

	//상품재고 관련
	void goodsStockMinutelyJob() throws BaseException;
}