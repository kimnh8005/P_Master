package kr.co.pulmuone.v1.goods.category.service;

import kr.co.pulmuone.v1.goods.category.dto.GetCategoryResultDto;
import kr.co.pulmuone.v1.goods.category.dto.OrgaStoreCategoryRequestDto;
import kr.co.pulmuone.v1.goods.category.dto.vo.CategoryDepth1Vo;
import kr.co.pulmuone.v1.goods.category.dto.vo.GetCategoryListResultVo;

import java.util.List;

public interface GoodsCategoryBiz {

	GetCategoryResultDto getCategory(Long ilCategoryId) throws Exception;

	List<GetCategoryListResultVo> getCategoryList(String mallDiv) throws Exception;

	int putCategoryDispGoodsCnt(Long ilCategoryId)  throws Exception;

	List<CategoryDepth1Vo> getOrgaStoreCategoryDepth1(OrgaStoreCategoryRequestDto dto) throws Exception;

	String getGoodsCategory(Long ilGoodsId, String mallDiv, String ilCategoryId) throws Exception;
}
