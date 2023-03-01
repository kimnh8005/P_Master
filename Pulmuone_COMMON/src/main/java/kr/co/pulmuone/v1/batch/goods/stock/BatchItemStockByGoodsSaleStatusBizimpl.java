package kr.co.pulmuone.v1.batch.goods.stock;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import kr.co.pulmuone.v1.batch.goods.stock.dto.BatchItemStockByGoodsSaleStatusReqeustDto;
import kr.co.pulmuone.v1.comm.enums.GoodsEnums;
import kr.co.pulmuone.v1.comm.exception.BaseException;
import kr.co.pulmuone.v1.comm.util.DateUtil;

/**
 * <PRE>
 * Forbiz Korea
 * 품목 재고에 따른 상품 판매상태 변경
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :   작성일          :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 *  1.0   2021-02-23   임상건            최초작성
 * =======================================================================
 * </PRE>
 */

@Service
public class BatchItemStockByGoodsSaleStatusBizimpl implements BatchItemStockByGoodsSaleStatusBiz {

	@Autowired
	private BatchItemStockByGoodsSaleStatusService batchItemStockByGoodsSaleStatusService;

	@Override
	@Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false, rollbackFor = { BaseException.class, Exception.class })
	public void goodsStockMinutelyJob() throws BaseException {
		//품목 재고 수량 0에 따른 상품 상태 품절(시스템) 변경
		batchItemStockByGoodsSaleStatusService.runItemStockByGoodsUpdateOutOfStockBySystem();
		//품목 재고 수량 1 이상에 따른 상품 상태 판매중 변경
		batchItemStockByGoodsSaleStatusService.runItemStockByGoodsUpdateOnSale();
		//묶음상품의 구성품중에 하나라도 '판매중' 상태가 아니라면 묶음상품도 '판매중지(시스템)' 처리
		batchItemStockByGoodsSaleStatusService.runGoodsSaleStatusByGoodsPackageSaleStatusUpdate();
	}
}
