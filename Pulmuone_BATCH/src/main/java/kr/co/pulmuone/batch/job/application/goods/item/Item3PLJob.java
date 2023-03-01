package kr.co.pulmuone.batch.job.application.goods.item;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import kr.co.pulmuone.batch.job.BaseJob;
import kr.co.pulmuone.v1.batch.goods.item.BatchGoodsItem3PLBiz;
import kr.co.pulmuone.v1.comm.exception.BaseException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component("Item3PLJob")
@Slf4j
@RequiredArgsConstructor
public class Item3PLJob implements BaseJob {

	@Autowired
    private BatchGoodsItem3PLBiz batchItem3PLBiz;

	@Override
	public void run(String[] params) throws Exception {
		// TODO Auto-generated method stub
		try {
			batchItem3PLBiz.addPutItem3PLJob();
		} catch (BaseException e) {
			log.error(e.getMessage(), e);
		}
	}

}
