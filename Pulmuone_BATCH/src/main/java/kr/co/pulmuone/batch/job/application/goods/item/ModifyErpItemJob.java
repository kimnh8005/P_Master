package kr.co.pulmuone.batch.job.application.goods.item;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import kr.co.pulmuone.v1.batch.goods.item.BatchGoodsItemBiz;
import kr.co.pulmuone.v1.comm.exception.BaseException;
import kr.co.pulmuone.batch.job.BaseJob;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component("modifyErpItemJob")
@Slf4j
@RequiredArgsConstructor
public class ModifyErpItemJob implements BaseJob {

    @Autowired
    private BatchGoodsItemBiz batchGoodsItemBiz;

    @Override
    public void run(String[] params) throws BaseException {

        try {

            batchGoodsItemBiz.modifyErpItemJob();

        } catch (BaseException e) {

            log.error(e.getMessage(), e);

        }

    }

}
