package kr.co.pulmuone.v1.search.searcher.service;

import kr.co.pulmuone.v1.display.dictionary.service.SearchWordLogBiz;
import kr.co.pulmuone.v1.search.searcher.dto.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class SearchBizImpl implements SearchBiz {

    @Autowired
    private GoodsSearchService goodsSearchService;

    @Autowired
    private SuggestionSearchService suggestionSearchService;

    @Autowired
    private SearchWordLogBiz searchWordLogBiz;

    @Autowired
    private StoreGoodsSearchService storeGoodsSearchService;

    @Override
    public SearchResultDto searchGoods(GoodsSearchRequestDto goodsSearchRequestDto) throws Exception {

        SearchResultDto result = goodsSearchService.search(goodsSearchRequestDto);

        if (goodsSearchRequestDto.isFirstSearch() && goodsSearchRequestDto.hasKeyword()) {
            log.info("searchKeyword:{}", goodsSearchRequestDto.getKeyword());
            searchWordLogBiz.addSearchWordLog(goodsSearchRequestDto.getKeyword(), result.getCount());
        }

        return result;
    }

    @Override
    public SuggestionSearchResultDto searchSuggestion(SuggestionSearchRequestDto suggestionSearchRequestDto) {
        return suggestionSearchService.search(suggestionSearchRequestDto);
    }

    @Override
    public SearchResultDto searchStoreGoods(StoreGoodsSearchRequestDto storeGoodsSearchRequestDto) throws Exception {
        SearchResultDto result = storeGoodsSearchService.search(storeGoodsSearchRequestDto);

        if (storeGoodsSearchRequestDto.isFirstSearch() && storeGoodsSearchRequestDto.hasKeyword()) {
            log.info("storeGoodsSearchKeyword:{}", storeGoodsSearchRequestDto.getKeyword());
            searchWordLogBiz.addSearchWordLog(storeGoodsSearchRequestDto.getKeyword(), result.getCount());
        }

        return result;
    }
}
