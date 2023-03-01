package kr.co.pulmuone.v1.search.searcher.dto;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Getter
public class SearchResultDto<T> {

    /**
     * 검색 결과 수; document total count
     */
    private Long count;

    /**
     * 검색 결과 리스트
     */
    private List<T> document;


    /**
     * 싱글 필터 그룹 바이 리스트; aggregation
     */
    @Setter
    private Map<String, List<AggregationDocumentDto>> filter;


    @Builder
    public SearchResultDto(Long count, List<T> document, Map<String, List<AggregationDocumentDto>> filter ){
        this.count = count;
        this.document = document;
        this.filter = filter;
    }


}
