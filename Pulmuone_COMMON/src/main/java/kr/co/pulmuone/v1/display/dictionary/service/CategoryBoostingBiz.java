package kr.co.pulmuone.v1.display.dictionary.service;

import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.base.dto.PagingListDataDto;
import kr.co.pulmuone.v1.display.dictionary.dto.BoostingTargetCategoryListDto;
import kr.co.pulmuone.v1.display.dictionary.dto.CategoryBoostingGridDto;
import kr.co.pulmuone.v1.display.dictionary.dto.CategoryBoostingSaveRequestDto;
import kr.co.pulmuone.v1.display.dictionary.dto.CategoryBoostingSearchRequestDto;

public interface CategoryBoostingBiz {

    PagingListDataDto<CategoryBoostingGridDto> getCategoryBoostingPagingList(CategoryBoostingSearchRequestDto searchRequestDto);

    ApiResult<?> saveCategoryBoosting(CategoryBoostingSaveRequestDto saveRequestDto);

    BoostingTargetCategoryListDto getBoostingCategoryList();
}
