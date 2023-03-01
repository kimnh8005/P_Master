package kr.co.pulmuone.v1.goods.item.dto.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class MigrationItemVo {

    /*
     * MIGRATION_ITEM 테이블 Vo : 마이그레이션 종료 후 삭제 예정
     */

    private int migrationItemId; // MIGRATION_ITEM 테이블 PK

    private String ilItemCode; // 품목 코드

    private String itemName; // 품목명

    private int ilCategoryStandardId; // 표준 카테고리 PK

    private int bosCategoryStandardFirstId; // 표준 카테고리 대분류 ID
    private int bosCategoryStandardSecondId; // 표준 카테고리 중분류 ID
    private int bosCategoryStandardThirdId; // 표준 카테고리 소분류 ID
    private int bosCategoryStandardFourthId; // 표준 카테고리 세분류 PK

    private int ilSpecMasterId; // 상품정보제공고시분류 PK

    private String storageMethodType; // BOS 보관방법

    private String etcDescription; // 기타정보

    private String nutritionQuantityPerOnce; // 영양분석 단위 (1회 분량)

    private String nutritionQuantityTotal; // 영양분석 단위 (총분량)

    private String nutritionEtc; // 영양성분 기타

}