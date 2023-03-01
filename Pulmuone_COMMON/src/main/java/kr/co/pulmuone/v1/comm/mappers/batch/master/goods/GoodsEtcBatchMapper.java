package kr.co.pulmuone.v1.comm.mappers.batch.master.goods;

import kr.co.pulmuone.v1.batch.goods.etc.dto.vo.GoodsFeedbackVo;
import kr.co.pulmuone.v1.batch.goods.etc.dto.vo.GoodsRankingVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface GoodsEtcBatchMapper {

    void addGoodsRankingList(@Param("insertList") List<GoodsRankingVo> insertList);

    void delAllGoodsRanking();

    List<Long> getGoodsRankingByDpBrandId(@Param("mallDiv") String mallDiv, @Param("dpBrandId") Long dpBrandId, @Param("goodsList") List<Long> goodsList, @Param("limit") int limit);

    List<Long> getGoodsRankingFromNowSale(@Param("mallDiv") String mallDiv, @Param("goodsList") List<Long> goodsList, @Param("limit") int limit);

    List<Long> getGoodsRankingByDpCtgryId(@Param("mallDiv") String mallDiv, @Param("dpCtgryId") Long dpCtgryId, @Param("goodsList") List<Long> goodsList, @Param("limit") int limit, @Param("basicYn") String basicYn);

    List<Long> getGoodsRankingFromDirectGoods(@Param("mallDiv") String mallDiv, @Param("dpCtgryIdList") List<Long> dpCtgryIdList, @Param("goodsList") List<Long> goodsList, @Param("limit") int limit);

    List<GoodsFeedbackVo> getGoodsFromFeedback();

    List<GoodsFeedbackVo> getGoodsPackageFromFeedback();

}
