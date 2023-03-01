package kr.co.pulmuone.v1.batch.goods.stock;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import kr.co.pulmuone.v1.comm.exception.BaseException;

/**
 * <PRE>
 * Forbiz Korea
 * 품목 연동재고 조회 후 품목 유통기한별 재고 재계산 배치 Biz
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :   작성일          :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 *  1.0    2020.12.15  이성준         최초작성
 * =======================================================================
 * </PRE>
 */
@Service
public class BatchGoodsStockReCalQtyBizImpl implements BatchGoodsStockReCalQtyBiz {

    @Autowired
    private BatchGoodsStockReCalQtyService batchGoodsStockReCalQtyService;

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false, rollbackFor = { BaseException.class, Exception.class })
	public void runGoodsStockReCalQtyJob() throws BaseException {
    	batchGoodsStockReCalQtyService.runGoodsStockReCalQtyJob();
	}

}
