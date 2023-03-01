package kr.co.pulmuone.v1.search.searcher.dto;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class GoodsSuggestionSearchResultDto {

    /**
     * 검색 추천어
     */
    private String word;

    /**
     * 검색어와 매칭되는 부분 강조 처리된 문자열
     */
    private String highlight;

}
