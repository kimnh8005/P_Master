package kr.co.pulmuone.v1.goods.goods.service;

import kr.co.pulmuone.v1.goods.goods.dto.DiscountCalculationResultDto;
import kr.co.pulmuone.v1.goods.goods.dto.GoodsRankingRequestDto;
import kr.co.pulmuone.v1.search.searcher.dto.GoodsSearchResultDto;

import java.util.List;

public interface GoodsEtcBiz {

    List<GoodsSearchResultDto> getRecommendedGoodsList(String categoryIdDepth2) throws Exception;

    DiscountCalculationResultDto discountCalculation(int salePrice, String goodsDiscountMethodType, int regularShippingBasicDiscountRate) throws Exception;

    void goodsListSortByGoodsId(List<Long> recommendedGoodsIdList, List<GoodsSearchResultDto> searchResultDto) throws Exception;

    List<Long> getGoodsRankingByCategoryIdDepth1(GoodsRankingRequestDto dto) throws Exception;

    List<Long> getGoodsRankingByDpBrandId(String mallDiv, Long dpBrandId, int total) throws Exception;

}
