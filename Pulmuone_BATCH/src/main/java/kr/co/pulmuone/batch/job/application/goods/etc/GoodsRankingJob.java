package kr.co.pulmuone.batch.job.application.goods.etc;

import kr.co.pulmuone.v1.batch.goods.etc.GoodsEtcBatchBiz;
import kr.co.pulmuone.batch.job.BaseJob;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("goodsRankingJob")
public class GoodsRankingJob implements BaseJob {

    @Autowired
    private GoodsEtcBatchBiz goodsEtcBatchBiz;

    @Override
    public void run(String[] params) {
        goodsEtcBatchBiz.runGoodsRanking();
    }
}
