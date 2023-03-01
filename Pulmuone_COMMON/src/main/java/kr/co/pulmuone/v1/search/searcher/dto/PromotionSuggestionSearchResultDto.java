package kr.co.pulmuone.v1.search.searcher.dto;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class PromotionSuggestionSearchResultDto {
    /**
     * 이벤트/기획전 seq
     */
    private Long seq;

    /**
     * 이벤트/기획전 구분 타입 코드
     */
    private String typeCode;

    /**
     * 검색 추천어
     */
    private String word;


    /**
     * 검색어와 매칭되는 부분 강조 처리된 문자열
     */
    private String highlight;

    /**
     * 이벤트/기획전 구분 상세 타입 코드
     */
    private String typeDetailCode;
}
