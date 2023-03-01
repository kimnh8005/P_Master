package kr.co.pulmuone.mall.shopping.recently.service;

import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.shopping.recently.dto.CommonGetRecentlyViewListByUserRequestDto;

/**
 * <PRE>
 * Forbiz Korea
 * Class의 기능과 역할을 상세히 기술한다.
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 * 버전  :   작성일                :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 * 1.0    20200831   	 	이원호            최초작성
 * =======================================================================
 * </PRE>
 */

public interface ShoppingRecentlyService {
    ApiResult<?> getRecentlyViewListByUser(CommonGetRecentlyViewListByUserRequestDto dto) throws Exception;

    ApiResult<?> delRecentlyViewByGoodsId(Long ilGoodsId) throws Exception;
}
