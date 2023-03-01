package kr.co.pulmuone.batch.job.application.goods.stock;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import kr.co.pulmuone.v1.batch.goods.stock.BatchGoodsStock3PlSearchBiz;
import kr.co.pulmuone.v1.comm.exception.BaseException;
import kr.co.pulmuone.batch.job.BaseJob;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component("goodsStock3PlSearchJob")
@Slf4j
@RequiredArgsConstructor
public class GoodsStock3PlSearchJob implements BaseJob {

    @Autowired
    private BatchGoodsStock3PlSearchBiz batchGoodsStock3PlSearchBiz;

    @Override
    public void run(String[] params) {

        try {

        	batchGoodsStock3PlSearchBiz.runGoodsStock3PlSearchJob();

        } catch (BaseException e) {

            log.error(e.getMessage(), e);

        }

    }

}
