package kr.co.pulmuone.v1.goods.item.dto.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class MigrationItemNutritionVo {

    /*
     * MIGRATION_ITEM_NUTRITION 테이블 Vo : 마이그레이션 종료 후 삭제 예정
     */

    private int migrationItemNutritionId; // MIGRATION_ITEM_NUTRITION 테이블 PK

    private String ilItemCode; // 품목코드 PK

    private String nutritionCode; // 영양정보 분류코드 PK

    private boolean canDeleted = true; // 삭제 가능 여부 : 영양정보 마이그레이션 데이터는 삭제 가능

    private Double nutritionQuantity; // BOS 영양성분량

    private Double nutritionPercent; // BOS 영양성분 기준치대비 함량

    private int sort; // 정렬순서

}
