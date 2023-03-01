package kr.co.pulmuone.v1.goods.item.dto;

import java.util.List;

import kr.co.pulmuone.v1.goods.item.dto.vo.MigrationItemNutritionVo;
import kr.co.pulmuone.v1.goods.item.dto.vo.MigrationItemSpecVo;
import kr.co.pulmuone.v1.goods.item.dto.vo.MigrationItemVo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class MigrationItemResponseDto {

    /*
     * 마스터 품목 데이터 마이그레이션 response dto : 마이그레이션 종료 후 삭제 예정
     */

    // MIGRATION_ITEM 테이블에서 해당 품목코드의 품목 정보
    private MigrationItemVo migrationItemVo;

    // MIGRATION_ITEM_NUTRITION 테이블에서 해당 품목코드의 영양정보 목록
    private List<MigrationItemNutritionVo> migrationItemNutritionVoList;

    // MIGRATION_ITEM_SPEC 테이블에서 해당 품목코드의 상품정보 제공고시 정보 목록
    private List<MigrationItemSpecVo> migrationItemSpecVoList;

}
