package kr.co.pulmuone.v1.batch.shopping.restock;

import kr.co.pulmuone.v1.comm.exception.BaseException;

public interface ShoppingRestockBatchBiz {

    void runRestock() throws BaseException;
}
