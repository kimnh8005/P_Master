package kr.co.pulmuone.mall.goods.category.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.goods.category.dto.GetCategoryResultDto;
import kr.co.pulmuone.v1.goods.category.service.GoodsCategoryBiz;
import lombok.extern.slf4j.Slf4j;

/**
 * <PRE>
 * Forbiz Korea
 * Class의 기능과 역할을 상세히 기술한다.
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :   작성일          :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 *  1.0    20200810   	 천혜현            최초작성
 * =======================================================================
 * </PRE>
 */

@Slf4j
@Service
public class GoodsCategoryServiceImpl implements GoodsCategoryService
{

    @Autowired
    private GoodsCategoryBiz goodsCategoryBiz;


	/**
	 * 카테고리별 상품 리스트 초기 정보
	 *
	 * @param 	ilGoodsId
	 * @throws 	Exception
	 */
	@Override
	public ApiResult<?> getCategory(Long ilCategoryId) throws Exception
	{
		GetCategoryResultDto getCategoryResultDto = goodsCategoryBiz.getCategory(ilCategoryId);

		return ApiResult.success(getCategoryResultDto);
	}
  /**
   * 배치 - 카테고리 전시 상품수 업데이트
   *
   * @param   ilGoodsId
   * @throws  Exception
   */
  @Override
  public ApiResult<?> putCategoryDispGoodsCnt(Long ilCategoryId) throws Exception
  {
    int i = goodsCategoryBiz.putCategoryDispGoodsCnt(ilCategoryId);

    return ApiResult.success();
  }


}
