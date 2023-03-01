package kr.co.pulmuone.v1.goods.goods.service;

import kr.co.pulmuone.v1.comm.constants.GoodsSortComparator;
import kr.co.pulmuone.v1.comm.mapper.goods.goods.GoodsEtcMapper;
import kr.co.pulmuone.v1.goods.goods.dto.GoodsRankingRequestDto;
import kr.co.pulmuone.v1.search.searcher.dto.GoodsSearchResultDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

/**
 * <PRE>
 * Forbiz Korea
 * Class의 기능과 역할을 상세히 기술한다.
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 * 버전  :   작성일          :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 * 1.0    20200824   	 천혜현            최초작성
 * =======================================================================
 * </PRE>
 */

@Slf4j
@Service
@RequiredArgsConstructor
public class GoodsEtcService {

    private final GoodsEtcMapper goodsEtcMapper;


    /**
     * 추천상품리스트 ID 조회
     *
     * @param categoryIdDepth2
     * @return List<Long>
     * @throws Exception
     */
    protected List<Long> getRecommendedGoodsList(String categoryIdDepth2) throws Exception {
        return goodsEtcMapper.getRecommendedGoodsList(categoryIdDepth2);
    }


    /**
     * 추천상품리스트 랭킹 순서대로 정렬
     *
     * @param goodsIdList, searchResultDto
     * @return SearchResultDto
     * @throws Exception
     */
    protected void goodsListSortByGoodsId(List<Long> goodsIdList, List<GoodsSearchResultDto> searchResultDto) throws Exception {
        Collections.sort(searchResultDto, new GoodsSortComparator(goodsIdList));
    }

    /**
     * 상품랭킹 조회 - 대카테고리
     *
     * @param mallDiv          String
     * @param categoryIdDepth1 Long
     * @param total            int
     * @param bestYn           String
     * @return List<Long>
     * @throws Exception Exception
     */
    protected List<Long> getGoodsRankingByCategoryIdDepth1(GoodsRankingRequestDto dto) throws Exception {
        return goodsEtcMapper.getGoodsRankingByCategoryIdDepth1(dto);
    }

    /**
     * 상품랭킹 조회 - 전시브랜드
     *
     * @param mallDiv   String
     * @param dpBrandId Long
     * @param total     int
     * @return List<Long>
     * @throws Exception Exception
     */
    protected List<Long> getGoodsRankingByDpBrandId(String mallDiv, Long dpBrandId, int total) throws Exception {
        return goodsEtcMapper.getGoodsRankingByDpBrandId(mallDiv, dpBrandId, total);
    }

}
