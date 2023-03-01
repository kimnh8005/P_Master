package kr.co.pulmuone.v1.search.searcher.dto;


import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SuggestionSearchRequestDto {

    /**
     * 검색 키워드
     */
    private String keyword;

    private Integer offset = 0;
    private Integer limit = 5;

    @Builder
    public SuggestionSearchRequestDto(String keyword, Integer offset, Integer limit){
        this.keyword = keyword;
        this.offset = offset != null ? offset : 0;
        this.limit = limit != null ? limit : 5;
    }

}
