package kr.co.pulmuone.v1.goods.goods.service;

import kr.co.pulmuone.v1.comm.enums.GoodsEnums;
import kr.co.pulmuone.v1.comm.util.PriceUtil;
import kr.co.pulmuone.v1.goods.goods.dto.DiscountCalculationResultDto;
import kr.co.pulmuone.v1.goods.goods.dto.GoodsRankingRequestDto;
import kr.co.pulmuone.v1.goods.goods.dto.GoodsSearchByGoodsIdRequestDto;
import kr.co.pulmuone.v1.goods.search.GoodsSearchBiz;
import kr.co.pulmuone.v1.search.searcher.dto.GoodsSearchResultDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class GoodsEtcBizImpl implements GoodsEtcBiz {

    @Autowired
    private GoodsEtcService goodsEtcService;

    @Autowired
    private GoodsSearchBiz goodsSearchBiz;


    /**
     * 추천상품 리스트
     *
     * @param categoryIdDepth2
     * @return List<GoodsSearchResultDto>
     * @throws Exception
     */
    @Override
    public List<GoodsSearchResultDto> getRecommendedGoodsList(String categoryIdDepth2) throws Exception {
        List<GoodsSearchResultDto> recommendedGoodsList = new ArrayList<>();

        //추천상품 ID 리스트 조회
        List<Long> recommendedGoodsIdList = goodsEtcService.getRecommendedGoodsList(categoryIdDepth2);


        //조회된 상품ID로 상품검색
        GoodsSearchByGoodsIdRequestDto goodsSearchByGoodsIdReqDto = GoodsSearchByGoodsIdRequestDto.builder()
                .goodsIdList(recommendedGoodsIdList)
                .build();

        recommendedGoodsList = goodsSearchBiz.searchGoodsByGoodsIdList(goodsSearchByGoodsIdReqDto);

        return recommendedGoodsList;
    }


    /**
     * 할인금액 계산
     *
     * @param salePrice, goodsDiscountMethodType, regularShippingBasicDiscountRate
     * @return DiscountCalculationResultDto
     * @throws Exception
     */
    @Override
    public DiscountCalculationResultDto discountCalculation(int salePrice, String goodsDiscountMethodType, int regularShippingBasicDiscountRate) throws Exception {
        DiscountCalculationResultDto discountCalculationResultDto = new DiscountCalculationResultDto();
        int discountAppliedPrice = 0;

        if (goodsDiscountMethodType.equals(GoodsEnums.GoodsDiscountMethodType.FIXED_RATE.getCode())) {
            discountAppliedPrice = PriceUtil.getPriceByRate(salePrice, regularShippingBasicDiscountRate);

        } else if (goodsDiscountMethodType.equals(GoodsEnums.GoodsDiscountMethodType.FIXED_PRICE.getCode())) {
            discountAppliedPrice = PriceUtil.getPriceByPrice(salePrice, regularShippingBasicDiscountRate);
        }

        discountCalculationResultDto.setDiscountAppliedPrice(discountAppliedPrice);

        return discountCalculationResultDto;
    }


    /**
     * 상품리스트 랭킹 순서대로 정렬
     *
     * @param goodsIdList, searchResultDto
     * @return SearchResultDto
     * @throws Exception
     */
    @Override
    public void goodsListSortByGoodsId(List<Long> goodsIdList, List<GoodsSearchResultDto> searchResultDto) throws Exception {
        goodsEtcService.goodsListSortByGoodsId(goodsIdList, searchResultDto);
    }

    @Override
    public List<Long> getGoodsRankingByCategoryIdDepth1(GoodsRankingRequestDto dto) throws Exception {
        return goodsEtcService.getGoodsRankingByCategoryIdDepth1(dto);
    }

    @Override
    public List<Long> getGoodsRankingByDpBrandId(String mallDiv, Long dpBrandId, int total) throws Exception {
        return goodsEtcService.getGoodsRankingByDpBrandId(mallDiv, dpBrandId, total);
    }

}
