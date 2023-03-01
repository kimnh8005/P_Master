package kr.co.pulmuone.mall.shopping.favorites.service;

import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.shopping.favorites.dto.CommonGetFavoritesGoodsListByUserRequestDto;

/**
 * <PRE>
 * Forbiz Korea
 * Class의 기능과 역할을 상세히 기술한다.
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :   작성일                :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 *  1.0    20200826		 	천혜현            최초작성
 * =======================================================================
 * </PRE>
 */

public interface ShoppingFavoritesService
{

	ApiResult<?> addGoodsFavorites(Long ilGoodsId) throws Exception;

	ApiResult<?> delGoodsFavorites(Long spFavoritesGoodsId) throws Exception;

	ApiResult<?> getFavoritesGoodsListByUser(CommonGetFavoritesGoodsListByUserRequestDto dto) throws Exception;

	ApiResult<?> delFavoritesGoodsByGoodsId(Long ilGoodsId) throws Exception;
}
