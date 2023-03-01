package kr.co.pulmuone.batch.job.application.goods.item;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import kr.co.pulmuone.batch.job.BaseJob;
import kr.co.pulmuone.v1.batch.goods.item.BatchGoodsItemPriceBiz;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component("ModifyErpItemPriceJob")
@Slf4j
@RequiredArgsConstructor
public class ModifyErpItemPriceJob implements BaseJob {

	 @Autowired
	 private BatchGoodsItemPriceBiz batchGoodsItemPriceBiz;

	@Override
	public void run(String[] params) throws Exception {
		// TODO Auto-generated method stub
		batchGoodsItemPriceBiz.modifyGoodsItemPriceJob();
	}


}
