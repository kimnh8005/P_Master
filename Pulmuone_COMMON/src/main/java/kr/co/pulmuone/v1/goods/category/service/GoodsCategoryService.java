package kr.co.pulmuone.v1.goods.category.service;

import kr.co.pulmuone.v1.comm.mapper.goods.category.GoodsCategoryMapper;
import kr.co.pulmuone.v1.goods.category.dto.GetCategoryResultDto;
import kr.co.pulmuone.v1.goods.category.dto.OrgaStoreCategoryRequestDto;
import kr.co.pulmuone.v1.goods.category.dto.vo.CategoryDepth1Vo;
import kr.co.pulmuone.v1.goods.category.dto.vo.GetCategoryListResultVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

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
 *  1.0    20200824   	 천혜현            최초작성
 * =======================================================================
 * </PRE>
 */

@Slf4j
@Service
@RequiredArgsConstructor
public class GoodsCategoryService {

  private final GoodsCategoryMapper goodsCategoryMapper;

  /**
   * 카테고리 정보
   *
   * @param ilCategoryId
   * @return GetCategoryResultDto
   * @throws Exception
   */
  protected GetCategoryResultDto getCategory(Long ilCategoryId) throws Exception {
    return goodsCategoryMapper.getCategory(ilCategoryId);
  }

  /**
   * 카테고리 리스트
   *
   * @param mallDiv
   * @return List<GetCategoryListResultVo>
   * @throws Exception
   */
  protected List<GetCategoryListResultVo> getCategoryList(String mallDiv) throws Exception {
    return goodsCategoryMapper.getCategoryList(mallDiv);
  }

  /**
   * 배치 - 카테고리 전시 상품수 업데이트
   *
   * @param ilCategoryId
   * @return int
   * @throws Exception
   */
  protected int putCategoryDispGoodsCnt(Long ilCategoryId) throws Exception {
    goodsCategoryMapper.putCategoryDispGoodsCnt3(ilCategoryId);
    goodsCategoryMapper.putCategoryDispGoodsCnt2(ilCategoryId);
    goodsCategoryMapper.putCategoryDispGoodsCnt1(ilCategoryId);
    // goodsCategoryMapper.putCategoryDispGoodsCnt(ilCategoryId)
    return 0;
  }

  /**
   * 대카테고리 조회 - 올가매장
   *
   * @param dto OrgaStoreCategoryRequestDto
   * @return List<CategoryDepth1Vo>
   * @throws Exception Exception
   */
  protected List<CategoryDepth1Vo> getOrgaStoreCategoryDepth1(OrgaStoreCategoryRequestDto dto) throws Exception {
    return goodsCategoryMapper.getOrgaStoreCategoryDepth1(dto);
  }

  /**
   * 상품 카테고리 progress (ex -> 두부 > 콩나물 > 달걀)
   *
   * @param
   * @return String
   * @throws Exception Exception
   */
  protected String getGoodsCategory(Long ilGoodsId, String mallDiv, String ilCategoryId) throws Exception {
    return goodsCategoryMapper.getGoodsCategory(ilGoodsId, mallDiv, ilCategoryId);
  }

}
