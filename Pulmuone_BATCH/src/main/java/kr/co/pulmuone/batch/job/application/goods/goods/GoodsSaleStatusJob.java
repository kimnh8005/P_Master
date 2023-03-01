package kr.co.pulmuone.batch.job.application.goods.goods;

import kr.co.pulmuone.batch.job.BaseJob;
import kr.co.pulmuone.v1.batch.goods.goods.GoodsSaleStatusBizBatch;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("goodsSaleStatusJob")
@Slf4j
@RequiredArgsConstructor
public class GoodsSaleStatusJob implements BaseJob {
    /**
     * 판매중지 30일 후 영구 판매 중지 배치
     * BATCH_NO 121
     */
    @Autowired
    private final GoodsSaleStatusBizBatch goodsSaleStatusBizBatch;

    @Override
    public void run(String[] params) throws Exception {
        // 판매중지 D+30 > 영구판매중지
        goodsSaleStatusBizBatch.runGoodsSaleStatusSetUp();
    }
}
