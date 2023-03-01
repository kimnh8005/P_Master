package kr.co.pulmuone.v1.goods.item.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.enums.ItemEnums.Item;
import kr.co.pulmuone.v1.comm.mapper.goods.item.GoodsItemMigrationMapper;
import kr.co.pulmuone.v1.goods.item.dto.MigrationItemResponseDto;
import kr.co.pulmuone.v1.goods.item.dto.vo.MigrationItemVo;

/**
 * <PRE>
 * Forbiz Korea
 * 마스터 품목 데이터 마이그레이션 전용 Service : 마이그레이션 종료 후 삭제 예정
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

@Service
public class GoodsItemMigrationService {

    @Autowired
    private GoodsItemMigrationMapper goodsItemMigrationMapper;

    /**
     * @Desc 마스터 품목 데이터 마이그레이션 정보 조회
     *
     * @param String : 품목코드
     *
     * @return MigrationItemResponseDto : 마스터 품목 데이터 마이그레이션 response dto
     *
     */
    public ApiResult<?> getMigrationItem(String ilItemCode) {

        MigrationItemResponseDto migrationItemResponseDto = new MigrationItemResponseDto();

        // MIGRATION_ITEM 테이블에서 해당 품목코드의 품목 정보 조회
        MigrationItemVo migrationitemVo = goodsItemMigrationMapper.getMigrationItem(ilItemCode);
        if(migrationitemVo == null)
			return ApiResult.result(Item.ITEM_INFO_NOT_EXIST);

        migrationItemResponseDto.setMigrationItemVo(goodsItemMigrationMapper.getMigrationItem(ilItemCode));

        // MIGRATION_ITEM_NUTRITION 테이블에서 해당 품목코드의 영양정보 목록 조회
        migrationItemResponseDto.setMigrationItemNutritionVoList(goodsItemMigrationMapper.getMigrationItemNutritionList(ilItemCode));

        // MIGRATION_ITEM_SPEC 테이블에서 해당 품목코드의 상품정보 제공고시 정보 목록 조회
        migrationItemResponseDto.setMigrationItemSpecVoList(goodsItemMigrationMapper.getMigrationItemSpecList(ilItemCode));

        return ApiResult.success(migrationItemResponseDto);

    }

}
