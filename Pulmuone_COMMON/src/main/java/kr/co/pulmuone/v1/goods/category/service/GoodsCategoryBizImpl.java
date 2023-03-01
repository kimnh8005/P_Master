package kr.co.pulmuone.v1.goods.category.service;

import kr.co.pulmuone.v1.goods.category.dto.GetCategoryResultDto;
import kr.co.pulmuone.v1.goods.category.dto.OrgaStoreCategoryRequestDto;
import kr.co.pulmuone.v1.goods.category.dto.vo.CategoryDepth1Vo;
import kr.co.pulmuone.v1.goods.category.dto.vo.GetCategoryListResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GoodsCategoryBizImpl implements GoodsCategoryBiz {

    @Autowired
    private GoodsCategoryService goodsCategoryService;


    /**
     * 카테고리별 상품 리스트 초기 정보
     *
     * @param	ilCategoryId
     * @throws	Exception
     */
    @Override
    public GetCategoryResultDto getCategory(Long ilCategoryId) throws Exception {

        return goodsCategoryService.getCategory(ilCategoryId);
    }


	/**
	 * 카테고리 리스트
	 *
	 * @param mallDiv
	 * @return List<GetCategoryListResultVo>
	 * @throws Exception
	 */
    @Override
    public List<GetCategoryListResultVo> getCategoryList(String mallDiv) throws Exception {

        return goodsCategoryService.getCategoryList(mallDiv);
    }

    /**
     * 배치 - 카테고리 전시 상품수 업데이트
     *
     * @param ilCategoryId
     * @throws  Exception
     */
    @Override
    public int putCategoryDispGoodsCnt(Long ilCategoryId) throws Exception {

        return goodsCategoryService.putCategoryDispGoodsCnt(ilCategoryId);
    }

    @Override
    public List<CategoryDepth1Vo> getOrgaStoreCategoryDepth1(OrgaStoreCategoryRequestDto dto) throws Exception {
        return goodsCategoryService.getOrgaStoreCategoryDepth1(dto);
    }

    /**
     * 상품 카테고리 progress (ex -> 두부 > 콩나물 > 달걀)
     *
     * @param ilCategoryId
     * @return String
     * @throws Exception
     */
    @Override
    public String getGoodsCategory(Long ilGoodsId, String mallDiv, String ilCategoryId) throws Exception {

        return goodsCategoryService.getGoodsCategory(ilGoodsId, mallDiv, ilCategoryId);
    }

}
