package kr.co.pulmuone.v1.search.searcher.constants;


import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.elasticsearch.search.sort.SortOrder;


@Getter
@RequiredArgsConstructor
public enum  SortCode {

    NEW("신상품순", "create_date_time", SortOrder.DESC, "_score", SortOrder.DESC),
    LOW_PRICE("낮은가격순", "sale_price", SortOrder.ASC, "_score", SortOrder.DESC),
    HIGH_PRICE("높은가격순", "sale_price", SortOrder.DESC, "_score", SortOrder.DESC),
    POPULARITY("인기순", "_score", SortOrder.DESC, "popularity_score", SortOrder.DESC),
    EMPLOYEE_LOW_PRICE("임직원가_낮은가격순", "employee_discount_price", SortOrder.ASC, "_score", SortOrder.DESC),
    EMPLOYEE_HIGH_PRICE("임직원가_높은가격순", "employee_discount_price", SortOrder.DESC, "_score", SortOrder.DESC),
    HIGH_DISCOUNT_RATE("할인율_높은순", "discount_rate", SortOrder.DESC, "popularity_score", SortOrder.DESC),
    HIGH_EMPLOYEE_DISCOUNT_RATE("임직원할인율_높은순", "employee_discount_rate", SortOrder.DESC, "popularity_score", SortOrder.DESC),
    ;

    private final String name;
    private final String field;
    private final SortOrder order;
    private final String secondField;
    private final SortOrder secondOrder;

}
