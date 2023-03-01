package kr.co.pulmuone.v1.batch.goods.po;

import kr.co.pulmuone.v1.comm.api.constant.TransferServerTypes;
import kr.co.pulmuone.v1.comm.exception.BaseException;

public interface BatchGoodsPurchaseOrderBiz {

	// 풀무원식품(PFF)발주
	void addPffGoodsPurchaseOrderJob(String pobatchTp, String transferServerType) throws BaseException;

	// 올가홀푸드(OGH)발주
	void addOghGoodsPurchaseOrderJob(String pobatchTp, String transferServerType) throws BaseException;

	void addGoodsPoOrderCalculate(String baseDt) throws BaseException;
}
