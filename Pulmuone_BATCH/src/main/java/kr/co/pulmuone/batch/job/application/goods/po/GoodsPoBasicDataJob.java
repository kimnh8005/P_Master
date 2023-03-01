package kr.co.pulmuone.batch.job.application.goods.po;

import kr.co.pulmuone.batch.job.BaseJob;
import kr.co.pulmuone.v1.batch.goods.po.BatchGoodsPoBasicDataBiz;
import kr.co.pulmuone.v1.comm.exception.BaseException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component("goodsPoBasicDataJob")
@Slf4j
@RequiredArgsConstructor
public class GoodsPoBasicDataJob implements BaseJob {

    @Autowired
    private BatchGoodsPoBasicDataBiz batchGoodsPoBasicDataBiz;

    @Override
    public void run(String[] params) {

        LocalDate now = LocalDate.now();//현재일짜
        String baseDt = now.toString().replaceAll("-", "");//조회기간

        try {
            batchGoodsPoBasicDataBiz.runGoodsPoBasicDataJob(baseDt);
        } catch(BaseException e) {
            log.error(e.getMessage(), e);
        }
    }
}
