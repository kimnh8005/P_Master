package kr.co.pulmuone.batch.job.application.goods.item;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import kr.co.pulmuone.batch.job.BaseJob;
import kr.co.pulmuone.v1.batch.goods.item.BatchGoodsItemDiscountPriceBiz;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component("ModifyErpItemDiscountPriceJob")
@Slf4j
@RequiredArgsConstructor
public class ModifyErpItemDiscountPriceJob implements BaseJob {

	@Autowired
	 private BatchGoodsItemDiscountPriceBiz batchGoodsItemDiscountPriceBiz;

	@Override
	public void run(String[] params) throws Exception {
		// TODO Auto-generated method stub
		batchGoodsItemDiscountPriceBiz.modifyGoodsItemDiscountPriceJob();
	}

}
