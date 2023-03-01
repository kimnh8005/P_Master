package kr.co.pulmuone.batch.job.application.goods.po;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import kr.co.pulmuone.batch.job.BaseJob;
import kr.co.pulmuone.v1.batch.goods.po.BatchGoodsPoOffSearchBiz;
import kr.co.pulmuone.v1.comm.exception.BaseException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component("goodsPoOffSearchJob")
@Slf4j
@RequiredArgsConstructor
public class GoodsPoOffSearchJob implements BaseJob {

	@Autowired
    private BatchGoodsPoOffSearchBiz batchGoodsPoOffSearchBiz;

    @Override
    public void run(String[] params) {

       try {

    	   batchGoodsPoOffSearchBiz.runGoodsPoOffSearchJob();

       } catch (BaseException e) {

           log.error(e.getMessage(), e);

       }

    }

}
