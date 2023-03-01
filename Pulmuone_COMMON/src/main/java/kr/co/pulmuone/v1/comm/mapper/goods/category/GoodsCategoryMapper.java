package kr.co.pulmuone.v1.comm.mapper.goods.category;

import kr.co.pulmuone.v1.goods.category.dto.GetCategoryResultDto;
import kr.co.pulmuone.v1.goods.category.dto.OrgaStoreCategoryRequestDto;
import kr.co.pulmuone.v1.goods.category.dto.vo.CategoryDepth1Vo;
import kr.co.pulmuone.v1.goods.category.dto.vo.GetCategoryListResultVo;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface GoodsCategoryMapper {
  GetCategoryResultDto getCategory(Long ilCategoryId) throws Exception;

  List<GetCategoryListResultVo> getCategoryList(String mallDiv) throws Exception;

  int putCategoryDispGoodsCnt(Long ilCategoryId) throws Exception;

  int putCategoryDispGoodsCnt1(Long ilCategoryId) throws Exception;

  int putCategoryDispGoodsCnt2(Long ilCategoryId) throws Exception;

  int putCategoryDispGoodsCnt3(Long ilCategoryId) throws Exception;

  List<CategoryDepth1Vo> getOrgaStoreCategoryDepth1(OrgaStoreCategoryRequestDto dto) throws Exception;

  String getGoodsCategory(@Param("ilGoodsId") Long ilGoodsId, @Param("mallDiv") String mallDiv, @Param("ilCategoryId") String ilCategoryId) throws Exception;
}
