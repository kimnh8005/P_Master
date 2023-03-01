package kr.co.pulmuone.v1.comm.mapper.goods.goods;

import java.util.List;

import kr.co.pulmuone.v1.goods.goods.dto.GoodsRankingRequestDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface GoodsEtcMapper {

	List<Long> getRecommendedGoodsList(String categoryIdDepth2) throws Exception;

	List<Long> getGoodsRankingByCategoryIdDepth1(GoodsRankingRequestDto dto) throws Exception;

	List<Long> getGoodsRankingByDpBrandId(@Param("mallDiv") String mallDiv, @Param("dpBrandId") Long dpBrandId, @Param("total") int total) throws Exception;

}
