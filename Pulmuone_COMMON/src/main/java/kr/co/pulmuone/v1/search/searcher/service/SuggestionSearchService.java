package kr.co.pulmuone.v1.search.searcher.service;

import kr.co.pulmuone.v1.comm.enums.Indices;
import kr.co.pulmuone.v1.search.searcher.dto.*;
import kr.co.pulmuone.v1.search.searcher.parser.KoreanParser;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class SuggestionSearchService extends SearchService {

    protected SuggestionSearchResultDto search(SuggestionSearchRequestDto suggestionSearchRequestDto) {

        if(StringUtils.isBlank(suggestionSearchRequestDto.getKeyword())) return null;

        PromotionSuggestionSearchRequestDto eventSuggestionSearchRequestDto = PromotionSuggestionSearchRequestDto.builder()
                                                                                .keyword(suggestionSearchRequestDto.getKeyword())
                                                                                .typeCode("EVENT")
                                                                                .build();

        PromotionSuggestionSearchRequestDto exhibitionSuggestionSearchRequestDto = PromotionSuggestionSearchRequestDto.builder()
                                                                                .keyword(suggestionSearchRequestDto.getKeyword())
                                                                                .typeCode("EXHIBITION")
                                                                                .build();

        SuggestionSearchResultDto result = SuggestionSearchResultDto.builder()
                .item(searchItemSuggestion(suggestionSearchRequestDto))
                .event(searchEventSuggestion(eventSuggestionSearchRequestDto))
                .exhibition(searchEventSuggestion(exhibitionSuggestionSearchRequestDto))
                .build();

        return result;
    }

    protected SearchResultDto searchItemSuggestion(SuggestionSearchRequestDto suggestionSearchRequestDto) {
        return super.search(Indices.GOODS_SUGGESTION.getAlias(), makeSuggestionSearchQuery(suggestionSearchRequestDto), GoodsSuggestionSearchResultDto.class);
    }

    protected SearchResultDto searchEventSuggestion(PromotionSuggestionSearchRequestDto suggestionSearchRequestDto) {
        return super.search(Indices.PROMOTION_SUGGESTION.getAlias(), makePromotionSuggestionSearchQuery(suggestionSearchRequestDto), PromotionSuggestionSearchResultDto.class);
    }

    private BoolQueryBuilder makeDefaultSuggestionQuery(String keyword) {
        BoolQueryBuilder query = QueryBuilders.boolQuery();

        query.should(QueryBuilders.matchQuery("word", keyword));
        KoreanParser parser = new KoreanParser();
        query.should(QueryBuilders.matchQuery("separated_word", parser.parse(keyword)));
        query.should(QueryBuilders.matchQuery("trimmed_word", parser.parse(keyword)));
        query.minimumShouldMatch(1);

        return query;
    }

    private SearchSourceBuilder makeSuggestionSearchQuery(SuggestionSearchRequestDto suggestionSearchRequestDto) {
        BoolQueryBuilder query = makeDefaultSuggestionQuery(suggestionSearchRequestDto.getKeyword());
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(query);
        searchSourceBuilder.highlighter(new HighlightBuilder().field("word"));

        return searchSourceBuilder;
    }

    private SearchSourceBuilder makePromotionSuggestionSearchQuery(PromotionSuggestionSearchRequestDto suggestionSearchRequestDto) {

        BoolQueryBuilder query = makeDefaultSuggestionQuery(suggestionSearchRequestDto.getKeyword());

        if (StringUtils.isNotEmpty(suggestionSearchRequestDto.getTypeCode())) {
            QueryBuilder typeQuery = QueryBuilders.termsQuery("type_code", suggestionSearchRequestDto.getTypeCode());
            query.must(typeQuery);
        }

        switch (suggestionSearchRequestDto.getDeviceType()) {
            case PC: {
                QueryBuilder typeQuery = QueryBuilders.termsQuery("display_web_pc_yn", "Y");
                query.must(typeQuery);
            }
            case MOBILE: {
                QueryBuilder typeQuery = QueryBuilders.termsQuery("display_web_mobile_yn", "Y");
                query.must(typeQuery);
            }
            case APP: {
                QueryBuilder typeQuery = QueryBuilders.termsQuery("display_app_yn", "Y");
                query.must(typeQuery);
            }
        }

        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(query);
        searchSourceBuilder.highlighter(new HighlightBuilder().field("word"));
        searchSourceBuilder.from(suggestionSearchRequestDto.getOffset()).size(suggestionSearchRequestDto.getLimit());
        searchSourceBuilder.sort(new FieldSortBuilder("create_date").order(SortOrder.DESC));

        return searchSourceBuilder;
    }

}
