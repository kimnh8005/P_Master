package kr.co.pulmuone.batch.job.application.goods.po;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import kr.co.pulmuone.batch.job.BaseJob;
import kr.co.pulmuone.v1.batch.goods.po.BatchGoodsPoSearchBiz;
import kr.co.pulmuone.v1.comm.exception.BaseException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component("goodsPoSearchJob")
@Slf4j
@RequiredArgsConstructor
public class GoodsPoSearchJob implements BaseJob {

	@Autowired
    private BatchGoodsPoSearchBiz batchGoodsPoSearchBiz;

    @Override
    public void run(String[] params) {

       try {

    	   batchGoodsPoSearchBiz.runGoodsPoSearchJob();

       } catch (BaseException e) {

           log.error(e.getMessage(), e);

       }

    }

}
