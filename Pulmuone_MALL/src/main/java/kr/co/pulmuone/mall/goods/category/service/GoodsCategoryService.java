package kr.co.pulmuone.mall.goods.category.service;

import kr.co.pulmuone.v1.comm.base.ApiResult;

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
 *  1.0    20200810		 	천혜현            최초작성
 * =======================================================================
 * </PRE>
 */

public interface GoodsCategoryService
{

	ApiResult<?> getCategory(Long ilCategoryId) throws Exception;

	 ApiResult<?> putCategoryDispGoodsCnt(Long ilCategoryId) throws Exception;

}
