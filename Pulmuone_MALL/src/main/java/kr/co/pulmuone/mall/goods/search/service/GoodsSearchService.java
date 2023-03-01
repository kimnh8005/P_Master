package kr.co.pulmuone.mall.goods.search.service;

import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.search.searcher.dto.GoodsSearchRequestDto;
import kr.co.pulmuone.v1.goods.category.dto.GetCategoryGoodsListRequestDto;

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
 *  1.0    20200923		 	천혜현            최초작성
 * =======================================================================
 * </PRE>
 */

public interface GoodsSearchService
{

	ApiResult<?> getCategoryGoodsList(GetCategoryGoodsListRequestDto getCategoryGoodsListRequestDto) throws Exception;

	ApiResult<?> getAutoCompleteList(String keyword,Integer limit) throws Exception;

	ApiResult<?> getSearchGoodsList(GoodsSearchRequestDto goodsSearchRequestDto) throws Exception ;

}
