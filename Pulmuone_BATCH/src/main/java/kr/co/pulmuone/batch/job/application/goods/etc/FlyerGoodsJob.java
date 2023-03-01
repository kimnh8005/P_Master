package kr.co.pulmuone.batch.job.application.goods.etc;

import kr.co.pulmuone.batch.job.BaseJob;
import kr.co.pulmuone.v1.batch.goods.etc.FlyerGoodsBatchBiz;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("flyerGoodsJob")
public class FlyerGoodsJob implements BaseJob {

    @Autowired
    private FlyerGoodsBatchBiz flyerGoodsBatchBiz;

    @Override
    public void run(String[] params) {
        flyerGoodsBatchBiz.runFlyerGoods();
    }
}
