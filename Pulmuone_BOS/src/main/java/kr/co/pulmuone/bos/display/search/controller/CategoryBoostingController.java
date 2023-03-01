package kr.co.pulmuone.bos.display.search.controller;

import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.base.dto.PagingListDataDto;
import kr.co.pulmuone.v1.comm.util.BindUtil;
import kr.co.pulmuone.v1.display.dictionary.dto.BoostingTargetCategoryListDto;
import kr.co.pulmuone.v1.display.dictionary.dto.CategoryBoostingGridDto;
import kr.co.pulmuone.v1.display.dictionary.dto.CategoryBoostingSaveRequestDto;
import kr.co.pulmuone.v1.display.dictionary.dto.CategoryBoostingSearchRequestDto;
import kr.co.pulmuone.v1.display.dictionary.service.CategoryBoostingBiz;
import kr.co.pulmuone.v1.search.indexer.service.IndexBiz;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestController
public class CategoryBoostingController {

    @Autowired
    IndexBiz indexBiz;

    @Autowired
    CategoryBoostingBiz categoryBoostingBiz;


    /**
     * 전시관리 > 카테고리 부스팅 데이터 그리드 > 카테고리 콤보 리스트
     * @return
     * @throws Exception
     */
    @GetMapping(value = "/admin/dp/categoryBoosting/getCategoryList")
    public ApiResult<BoostingTargetCategoryListDto> getBoostingCategoryList() {
        BoostingTargetCategoryListDto result  = categoryBoostingBiz.getBoostingCategoryList();
        return ApiResult.success(result);
    }


    /**
     * 전시관리 > 카테고리 부스팅 데이터 목록 조회
     * @param request
     * @return
     * @throws Exception
     */
    @PostMapping(value = "/admin/dp/categoryBoosting/getCategoryBoostingList")
    public ApiResult<PagingListDataDto<CategoryBoostingGridDto>> getCategoryBoostingList(HttpServletRequest request) throws Exception {
        CategoryBoostingSearchRequestDto searchRequestDto = (CategoryBoostingSearchRequestDto) BindUtil.convertRequestToObject(request, CategoryBoostingSearchRequestDto.class);
        return ApiResult.success(categoryBoostingBiz.getCategoryBoostingPagingList(searchRequestDto));
    }

    /**
     * 전시관리 > 카테고리 부스팅 데이터 편집
     * @param saveRequestDto
     * @return
     * @throws Exception
     */
    @ApiResponses(value = {
            @ApiResponse(code = 901, message = "" +
                    "[DUPLICATE_DATA] DUPLICATE_DATA - 중복 데이터가 있습니다."
            )
    })
    @PostMapping(value = "/admin/dp/categoryBoosting/saveCategoryBoosting")
    public ApiResult<?> saveCategoryBoosting(CategoryBoostingSaveRequestDto saveRequestDto) throws Exception {
        saveRequestDto.convertDataList();
        return categoryBoostingBiz.saveCategoryBoosting(saveRequestDto);
    }

    /**
     * 전시관리 >  카테고리 부스팅 엔진 반영
     * @return
     * @throws Exception
     */
    @PostMapping(value = "/admin/dp/categoryBoosting/callReflectionCategoryBoosting")
    public ApiResult<?> saveCategoryBoosting() throws Exception {
        return indexBiz.indexCategoryBoost();
    }

}
