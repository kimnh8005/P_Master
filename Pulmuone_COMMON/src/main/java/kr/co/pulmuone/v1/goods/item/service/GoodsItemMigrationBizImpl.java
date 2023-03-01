package kr.co.pulmuone.v1.goods.item.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.goods.item.dto.MigrationItemResponseDto;
import lombok.extern.slf4j.Slf4j;

/**
 * <PRE>
 * Forbiz Korea
 * 마스터 품목 데이터 마이그레이션 전용 BizImpl : 마이그레이션 종료 후 삭제 예정
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

@Slf4j
@Service
public class GoodsItemMigrationBizImpl implements GoodsItemMigrationBiz {

    @Autowired
    private GoodsItemMigrationService goodsItemMigrationService;

    /**
     * @Desc 마스터 품목 데이터 마이그레이션 정보 조회
     *
     * @param String : 품목 코드
     *
     * @return ApiResult
     */
    @Override
    public ApiResult<?> getMigrationItem(String ilItemCode) {
            return goodsItemMigrationService.getMigrationItem(ilItemCode);
    }

}
