package kr.co.pulmuone.v1.batch.goods.stock;

import kr.co.pulmuone.v1.comm.exception.BaseException;

public interface BatchGoodsStockReCalQtyBiz {

	void runGoodsStockReCalQtyJob() throws BaseException;

}
