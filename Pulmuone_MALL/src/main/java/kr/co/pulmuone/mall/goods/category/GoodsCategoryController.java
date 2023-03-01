package kr.co.pulmuone.mall.goods.category;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.goods.category.dto.GetCategoryResultDto;
import kr.co.pulmuone.mall.goods.category.service.GoodsCategoryService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

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
 *  1.0    20200824   	 	천혜현            최초작성
 * =======================================================================
 * </PRE>
 */
@RestController
public class GoodsCategoryController {

    @Autowired
    public GoodsCategoryService goodsCategoryService;


    /**
     * 카테고리별 상품 리스트 초기 정보
     *
     * @param	ilCategoryId
     * @throws 	Exception
     */
    @PostMapping(value = "/goods/category/getCategoryPageInfo")
    @ApiOperation(value = "카테고리별 상품 리스트 초기 정보")
    @ApiImplicitParams({ @ApiImplicitParam(name = "ilCategoryId", value = "카테고리 ID", required = true, dataType = "Long") })
    @ApiResponses(value = {
            @ApiResponse(code = 900, message = "response data", response = GetCategoryResultDto.class)
    })
    public ApiResult<?> getCategoryPageInfo(Long ilCategoryId) throws Exception {

        return goodsCategoryService.getCategory(ilCategoryId);
    }
}