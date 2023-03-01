package kr.co.pulmuone.v1.search.indexer.dto;

import lombok.Getter;

@Getter
public class CategoryBoostDocumentDto {

    /**
     * 키워드
     */
    private String keyword;


    /**
     * 대카테고리 ID
     */
    private String lev1CategoryId;


    /**
     * 부스팅 점수
     */
    private Integer score;

}
