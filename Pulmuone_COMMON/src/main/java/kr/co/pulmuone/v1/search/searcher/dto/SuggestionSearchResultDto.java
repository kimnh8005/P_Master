package kr.co.pulmuone.v1.search.searcher.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SuggestionSearchResultDto {

    /**
     * 검색어 추천 결과; 상품명 기준
     */
    private SearchResultDto item;

    /**
     * 검색어 추천 결과; 이벤트명 기준
     */
    private SearchResultDto event;

    /**
     * 검색어 추천 결과; 기획전명 기준
     */
    private SearchResultDto exhibition;

}
