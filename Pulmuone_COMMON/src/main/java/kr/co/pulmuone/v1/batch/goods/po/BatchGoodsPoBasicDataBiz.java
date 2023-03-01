package kr.co.pulmuone.v1.batch.goods.po;

import kr.co.pulmuone.v1.comm.exception.BaseException;

public interface BatchGoodsPoBasicDataBiz {
    void runGoodsPoBasicDataJob(String baseDt) throws BaseException;
}
