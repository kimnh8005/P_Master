package kr.co.pulmuone.v1.batch.goods.po;

import kr.co.pulmuone.v1.comm.exception.BaseException;

public interface BatchGoodsPoSearchBiz {

	void runGoodsPoSearchJob() throws BaseException;

}
