package kr.co.pulmuone.batch.job.application.goods.goods;

import kr.co.pulmuone.batch.job.BaseJob;
import kr.co.pulmuone.v1.batch.goods.goods.GoodsDetailImageBatchBiz;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("goodsDetailImageJob")
@Slf4j
@RequiredArgsConstructor
public class GoodsDetailImageJob implements BaseJob {

    /**
     * 상품 상세 이미지 생성 배치
     * BATCH_NO
     */
    @Autowired
    private final GoodsDetailImageBatchBiz goodsDetailImageBatchBiz;

    @Override
    public void run(String[] params) throws Exception {
        //
        goodsDetailImageBatchBiz.runCreateGoodsDetailImage();
    }
}
