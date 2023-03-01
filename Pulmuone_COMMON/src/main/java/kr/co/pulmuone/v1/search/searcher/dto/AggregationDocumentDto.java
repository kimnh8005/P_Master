package kr.co.pulmuone.v1.search.searcher.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AggregationDocumentDto {

    /**
     * 검색 필터로 사용되는 항목 코드
     * 필터 그룹바이 코드
     */
    @Setter
    @JsonAlias({"main_lev1_category_id_name_list","brand_id","mall_id_name_list", "storage_method_id", "delivery_type_id_name_list","benefit_type_id_name_list","certification_type_id_name_list"})
    private String code;

    /**
     * 검색 필터로 사용되는 항목 명
     * 필터 그룹바이 명
     */
    @Setter
    @JsonAlias({"brand_name", "storage_method_name"})
    private String name;


    /**
     * 필터 그룹바이 건 수
     */
    private int count = 0;

    @Builder
    public AggregationDocumentDto(String code, String name, int count){
        this.code = code;
        this.name = name;
        this.count = count;
    }
}
