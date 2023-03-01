package kr.co.pulmuone.v1.search.indexer.service;

import kr.co.pulmuone.v1.comm.base.ApiResult;

public interface IndexBiz {
    /**
     * 상품 색인
     * @return
     * @throws Exception
     */
    ApiResult<?> indexGoods() throws Exception;

    /**
     * 검색어 추천 색인
     * @return
     * @throws Exception
     */
    ApiResult<?> indexGoodsSuggestion() throws Exception;


    /**
     * 이벤트/기획전 검색어 추천 색인
     * @return
     * @throws Exception
     */
    ApiResult<?> indexPromotionSuggestion() throws Exception;


    /**
     * 카테고리 부스팅 사전 색인
     * @return
     * @throws Exception
     */
    ApiResult<?> indexCategoryBoost() throws Exception;


    /**
     * 매장 상품 색인
     * @return
     * @throws Exception
     */
    ApiResult<?> indexStoreGoods() throws Exception;
}

