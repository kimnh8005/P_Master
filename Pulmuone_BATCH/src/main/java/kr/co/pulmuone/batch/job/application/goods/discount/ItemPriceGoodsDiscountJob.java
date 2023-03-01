package kr.co.pulmuone.batch.job.application.goods.discount;

import kr.co.pulmuone.batch.job.BaseJob;
import kr.co.pulmuone.v1.batch.goods.discount.BatchItemPriceGoodsDiscountDeniedBiz;
import kr.co.pulmuone.v1.comm.exception.BaseException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("itemPriceGoodsDiscountJob")
@Slf4j
@RequiredArgsConstructor
public class ItemPriceGoodsDiscountJob implements BaseJob {

	@Autowired
	private BatchItemPriceGoodsDiscountDeniedBiz batchItemPriceGoodsDiscountDeniedBiz;

	@Override
	public void run(String[] params) {
		try {
			//상품할인 승인 내역에서 할인승인 상태가 '승인요청'이고 시작일이 이미 도래한 내역에 대한 Batch 처리
			batchItemPriceGoodsDiscountDeniedBiz.goodsDiscountApprPastStartDateDeniedJob();
		}
		catch (BaseException e) {
			log.error(e.getMessage(), e);
		}
	}
}
