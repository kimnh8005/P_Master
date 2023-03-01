package kr.co.pulmuone.v1.goods.item.dto.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class MigrationItemSpecVo {

    /*
     * MIGRATION_ITEM_SPEC 테이블 Vo : 마이그레이션 종료 후 삭제 예정
     */

    private int migrationItemSpecId; // MIGRATION_ITEM_SPEC 테이블 PK

    private String ilItemCode; // 품목 코드

    private int ilSpecFieldId; // 상품정보제공고시항목 PK

    private String specFieldValue; // 상품정보제공고시 상세 항목 정보

    // IL_ITEM_SPEC_VALUE 테이블의 DIRECT_YN 컬럼에 대응하는 필드
    // 기본값 false : 화면에서 '직접입력' 체크박스 출력하지 않음
    private Boolean directYn = false;

}
