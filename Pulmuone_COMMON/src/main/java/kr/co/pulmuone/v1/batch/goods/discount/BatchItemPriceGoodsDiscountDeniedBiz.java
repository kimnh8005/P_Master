package kr.co.pulmuone.v1.batch.goods.discount;

import kr.co.pulmuone.v1.comm.exception.BaseException;

public interface BatchItemPriceGoodsDiscountDeniedBiz {
	
	//상품할인 승인 내역에서 할인승인 상태가 '승인요청'이고 시작일이 이미 도래한 내역에 대한 Batch 처리
	void goodsDiscountApprPastStartDateDeniedJob() throws BaseException;
}