package kr.co.pulmuone.v1.search.searcher.service;

import kr.co.pulmuone.v1.comm.CommonServiceTestBaseForJunit5;
import kr.co.pulmuone.v1.search.searcher.dto.*;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Slf4j
class SuggestionSearchServiceTest extends CommonServiceTestBaseForJunit5 {

    @Autowired
    SuggestionSearchService suggestionSearchService;


    @Test
    void test_키워드_공백인경우_결과없음() throws Exception {
        String keyword = " ";

        SuggestionSearchRequestDto searchRequestDto = SuggestionSearchRequestDto.builder()
                .keyword(keyword)
                .build();

        Assertions.assertEquals(null, suggestionSearchService.search(searchRequestDto));

    }


    @Test
    void test_키워드_결과있음() throws Exception {

        String keyword = "테ㅅ";

        SuggestionSearchRequestDto searchRequestDto = SuggestionSearchRequestDto.builder()
                .keyword(keyword)
                .build();

        SuggestionSearchResultDto searchResult = suggestionSearchService.search(searchRequestDto);

        SearchResultDto goodsResultDto = searchResult.getItem();
        SearchResultDto eventResultDto = searchResult.getEvent();

        List<GoodsSuggestionSearchResultDto> goods = goodsResultDto.getDocument();
        goods.stream().forEach(
                k -> {
                    log.info("word: {}", k.getWord());
                    log.info("highlight: {}", k.getHighlight());
                }
        );
        List<PromotionSuggestionSearchResultDto> event = eventResultDto.getDocument();
        event.stream().forEach(
                k -> {
                    log.info("type: {}", k.getTypeCode());
                    log.info("word: {}", k.getWord());
                    log.info("highlight: {}", k.getHighlight());
                }
        );

        Assertions.assertNotNull(searchResult);
    }


    @Test
    void test_키워드_자음하나() throws Exception {

        String keyword = "ㅌ";

        SuggestionSearchRequestDto searchRequestDto = SuggestionSearchRequestDto.builder()
                                                            .keyword(keyword)
                                                            .build();

        SuggestionSearchResultDto searchResult = suggestionSearchService.search(searchRequestDto);
        Assertions.assertNotNull(searchResult);
    }

    @Test
    void test_키워드_영문_대소문자() throws Exception {

        String lowerKeyword = "qa";
        SuggestionSearchRequestDto searchRequestDto = SuggestionSearchRequestDto.builder()
                .keyword(lowerKeyword)
                .build();

        SuggestionSearchResultDto searchResult = suggestionSearchService.search(searchRequestDto);
        long lowerCaseCount = searchResult.getItem().getCount();

        String upperKeyword = "QA";
        SuggestionSearchRequestDto searchRequestDto2 = SuggestionSearchRequestDto.builder()
                .keyword(upperKeyword)
                .build();
        searchResult = suggestionSearchService.search(searchRequestDto2);
        long upperCaseCount = searchResult.getItem().getCount();

        Assertions.assertNotNull(lowerCaseCount == upperCaseCount);
    }


    @Test
    void test_키워드_and_FetchSize() throws Exception {

        String keyword = "ㅇ";
        int limit = 10;

        SuggestionSearchRequestDto searchRequestDto = SuggestionSearchRequestDto.builder()
                .keyword(keyword)
                .limit(10)
                .build();

        SuggestionSearchResultDto searchResult = suggestionSearchService.search(searchRequestDto);
        SearchResultDto goodsResultDto = searchResult.getItem();
        List<GoodsSuggestionSearchResultDto> goods = goodsResultDto.getDocument();

        Assertions.assertTrue(goods.size() == 10);
    }

    @Test
    void test_키워드_이벤트() throws Exception {

        String keyword = "따";
        int limit = 1;
        String typeCode = "EVENT";
        PromotionSuggestionSearchRequestDto searchRequestDto = PromotionSuggestionSearchRequestDto.builder()
                .keyword(keyword)
                .limit(limit)
                .typeCode(typeCode)
                .build();
        SearchResultDto resultDto = suggestionSearchService.searchEventSuggestion(searchRequestDto);
        List<PromotionSuggestionSearchResultDto> event = resultDto.getDocument();

        event.stream().forEach(
                e -> log.info(e.toString())
        );

        Assertions.assertTrue(event.size() == 1);
    }


    @Test
    void test_키워드_기획전() throws Exception {

        String keyword = "따";
        int limit = 1;
        String typeCode = "EXHIBITION";

        PromotionSuggestionSearchRequestDto searchRequestDto = PromotionSuggestionSearchRequestDto.builder()
                .keyword(keyword)
                .limit(limit)
                .typeCode(typeCode)
                .build();
        SearchResultDto resultDto = suggestionSearchService.searchEventSuggestion(searchRequestDto);
        List<PromotionSuggestionSearchResultDto> exhibition = resultDto.getDocument();

        exhibition.stream().forEach(
                e -> log.info(e.toString())
        );

        Assertions.assertTrue(exhibition.size() == 1);
    }

    @Test
    void test_키워드__상품_기획전_이벤트() throws Exception {

        String keyword = "만";

        SuggestionSearchRequestDto searchRequestDto = SuggestionSearchRequestDto.builder()
                .keyword(keyword)
                .limit(10)
                .build();

        SuggestionSearchResultDto searchResult = suggestionSearchService.search(searchRequestDto);

        SearchResultDto result = searchResult.getItem();
        List<GoodsSuggestionSearchResultDto> goods = result.getDocument();
        goods.stream().forEach(g -> log.info(g.toString()));

        SearchResultDto eventResult = searchResult.getEvent();
        List<PromotionSuggestionSearchResultDto> event = eventResult.getDocument();
        event.stream().forEach(e -> log.info(e.toString()));

        SearchResultDto exhibitionResult = searchResult.getExhibition();
        List<PromotionSuggestionSearchResultDto> exhibition = exhibitionResult.getDocument();
        exhibition.stream().forEach(e -> log.info(e.toString()));

        Assertions.assertTrue(goods.size() > 0);
        Assertions.assertTrue(event.size() == 1);
        Assertions.assertTrue(exhibition.size() == 1);
    }


}