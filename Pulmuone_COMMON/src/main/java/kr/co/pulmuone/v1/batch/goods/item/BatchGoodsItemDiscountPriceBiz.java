package kr.co.pulmuone.v1.batch.goods.item;

import kr.co.pulmuone.v1.comm.exception.BaseException;

public interface BatchGoodsItemDiscountPriceBiz {

    // ERP 연동 품목의 가격정보 일괄 수정 배치 Job
    public void modifyGoodsItemDiscountPriceJob() throws BaseException;


}
