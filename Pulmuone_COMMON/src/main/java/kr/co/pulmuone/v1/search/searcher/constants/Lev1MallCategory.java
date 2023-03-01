package kr.co.pulmuone.v1.search.searcher.constants;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Lev1MallCategory {

    //fixme: 전시 도메인의 '몰인몰 카테고리'에 대한 상수 클래스가 정의되면 그 상수를 사용할 예정.
    ALL("MALL_DIV.PULMUONE", "전체"),
    ORGA("MALL_DIV.ORGA", "올가"),
    EATSSLIM("MALL_DIV.EATSLIM", "잇슬림"),
    BABYMEAL("MALL_DIV.BABYMEAL", "베이비밀");

    private final String code;
    private final String name;

}
