package kr.co.pulmuone.v1.goods.item.service;

import kr.co.pulmuone.v1.comm.base.ApiResult;

/**
 * <PRE>
 * Forbiz Korea
 * 마스터 품목 데이터 마이그레이션 전용 Biz : 마이그레이션 종료 후 삭제 예정
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

public interface GoodsItemMigrationBiz {

    // 마스터 품목 데이터 마이그레이션 정보 조회
    ApiResult<?> getMigrationItem(String ilItemCode);

}
