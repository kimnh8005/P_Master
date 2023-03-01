package kr.co.pulmuone.v1.comm.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Indices {

    GOODS("goods", new String[]{"goods_full_name.nori", "goods_full_name.ngram", "search_keyword", "brand_name.nori", "brand_name.ngram"}),
    GOODS_SUGGESTION("suggest", new String[]{} ),
    PROMOTION_SUGGESTION("promotion_suggest", new String[]{}),
    CATEGORY_BOOST("category_boost", new String[]{}),
    STORE_GOODS("store_goods", new String[]{"goods_full_name.nori", "goods_full_name.ngram", "search_keyword", "brand_name.nori", "brand_name.ngram"});

    private final String alias;
    private final String[] multiMatchFields;

}
