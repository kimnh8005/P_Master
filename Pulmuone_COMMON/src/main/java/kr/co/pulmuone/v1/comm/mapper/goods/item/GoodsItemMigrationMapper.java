package kr.co.pulmuone.v1.comm.mapper.goods.item;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import kr.co.pulmuone.v1.goods.item.dto.vo.MigrationItemNutritionVo;
import kr.co.pulmuone.v1.goods.item.dto.vo.MigrationItemSpecVo;
import kr.co.pulmuone.v1.goods.item.dto.vo.MigrationItemVo;

/**
 * <PRE>
 * Forbiz Korea
 * 마스터 품목 데이터 마이그레이션 전용 Mapper : 마이그레이션 종료 후 삭제 예정
 *
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :   작성일                :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 *  1.0    2020. 10. 29.               박주형         최초작성
 * =======================================================================
 * </PRE>
 */

@Mapper
public interface GoodsItemMigrationMapper {

    // MIGRATION_ITEM 테이블에서 해당 품목코드의 품목 정보 조회
    MigrationItemVo getMigrationItem(@Param("ilItemCode") String ilItemCode);

    // MIGRATION_ITEM_NUTRITION 테이블에서 해당 품목코드의 영양정보 목록 조회
    List<MigrationItemNutritionVo> getMigrationItemNutritionList(@Param("ilItemCode") String ilItemCode);

    // MIGRATION_ITEM_SPEC 테이블에서 해당 품목코드의 상품정보 제공고시 정보 목록 조회
    List<MigrationItemSpecVo> getMigrationItemSpecList(@Param("ilItemCode") String ilItemCode);

}
