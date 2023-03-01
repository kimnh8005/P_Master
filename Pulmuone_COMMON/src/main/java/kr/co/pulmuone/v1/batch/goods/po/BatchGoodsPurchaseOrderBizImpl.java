package kr.co.pulmuone.v1.batch.goods.po;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import kr.co.pulmuone.v1.comm.exception.BaseException;

/**
 * <PRE>
 * Forbiz Korea
 * ERP 구매발주 입력 배치 Biz
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :   작성일          :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 *  1.0   2020.12.21   이성준             최초작성
 * =======================================================================
 * </PRE>
 */
@Service
public class BatchGoodsPurchaseOrderBizImpl implements BatchGoodsPurchaseOrderBiz {

	@Autowired
    private BatchGoodsPurchaseOrderService batchGoodsPurchaseOrderService;

	@Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false, rollbackFor = { BaseException.class, Exception.class })
	public void addPffGoodsPurchaseOrderJob(String poBatchTp, String transferServerType) throws BaseException {
		batchGoodsPurchaseOrderService.addPffGoodsPurchaseOrderJob(poBatchTp, transferServerType);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false, rollbackFor = { BaseException.class, Exception.class })
	public void addOghGoodsPurchaseOrderJob(String poBatchTp, String transferServerType) throws BaseException {
		batchGoodsPurchaseOrderService.addOghGoodsPurchaseOrderJob(poBatchTp, transferServerType);
	}

	@Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false, rollbackFor = { BaseException.class, Exception.class })
	public void addGoodsPoOrderCalculate(String baseDt) throws BaseException {
		batchGoodsPurchaseOrderService.addGoodsPoOrderCalculate(baseDt);
	}

}
