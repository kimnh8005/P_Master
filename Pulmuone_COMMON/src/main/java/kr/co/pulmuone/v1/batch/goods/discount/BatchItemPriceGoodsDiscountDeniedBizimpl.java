package kr.co.pulmuone.v1.batch.goods.discount;

import kr.co.pulmuone.v1.comm.exception.BaseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * <PRE>
 * Forbiz Korea
 * 상품할인 승인 내역에서 할인승인 상태가 '승인요청'이고 시작일이 이미 도래한 내역에 대한 Batch 처리
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :   작성일          :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 *  1.0   2021-05-14   임상건            최초작성
 * =======================================================================
 * </PRE>
 */

@Service
public class BatchItemPriceGoodsDiscountDeniedBizimpl implements BatchItemPriceGoodsDiscountDeniedBiz {

	@Autowired
	private BatchItemPriceGoodsDiscountDeniedService batchItemStockByGoodsSaleStatusService;

	@Override
	@Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false, rollbackFor = { BaseException.class, Exception.class })
	public void goodsDiscountApprPastStartDateDeniedJob() throws BaseException {
		//상품할인 승인 내역에서 할인승인 상태가 '승인요청'이고 시작일이 이미 도래한 내역에 대한 Batch 처리
		batchItemStockByGoodsSaleStatusService.runGoodsDiscountApprPastStartDateDenied();
	}
}
