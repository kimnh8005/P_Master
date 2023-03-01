package kr.co.pulmuone.v1.base.service;

import kr.co.pulmuone.v1.base.dto.CategoryRequestDto;
import kr.co.pulmuone.v1.comm.base.ApiResult;

public interface IlCommonBiz {

    ApiResult<?> getDropDownCategoryList(CategoryRequestDto categoryRequestDto);
    ApiResult<?> getDropDownCategoryStandardList(CategoryRequestDto categoryRequestDto);

}
