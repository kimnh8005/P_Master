package kr.co.pulmuone.v1.batch.goods.etc;

import kr.co.pulmuone.v1.batch.goods.etc.dto.vo.GoodsFeedbackVo;

import java.util.List;

public interface GoodsEtcBatchBiz {

    void runGoodsRanking();

    List<Long> getGoodsRankingByDpBrandId(String mallDiv, Long dpBrandId, List<Long> goodsList, int limit);

    List<Long> getGoodsRankingFromNowSale(String mallDiv, List<Long> goodsList, int limit);

    List<Long> getGoodsRankingByDpCtgryId(String mallDiv, Long dpCtgryId, List<Long> goodsList, int limit, String basicYn);

    List<Long> getGoodsRankingFromDirectGoods(String mallDiv, List<Long> dpCtgryIdList, List<Long> goodsList, int limit);

    List<GoodsFeedbackVo> getGoodsFromFeedback();

    List<GoodsFeedbackVo> getGoodsPackageFromFeedback();

}
