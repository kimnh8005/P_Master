package kr.co.pulmuone.v1.search.searcher.service;

import kr.co.pulmuone.v1.search.searcher.dto.*;

public interface SearchBiz {

    /**
     * 상품 검색
     * @param searchRequestDto
     * @return
     * @throws Exception
     */
    SearchResultDto searchGoods(GoodsSearchRequestDto searchRequestDto) throws Exception;

    /**
     * 검색 키워드 검색
     * @param suggestionSearchRequestDto
     * @return
     * @throws Exception
     */
    SuggestionSearchResultDto searchSuggestion(SuggestionSearchRequestDto suggestionSearchRequestDto) throws Exception;

    /**
     * 매장 상품 검색
     * @param searchRequestDto
     * @return
     * @throws Exception
     */
    SearchResultDto searchStoreGoods(StoreGoodsSearchRequestDto searchRequestDto) throws Exception;
}
