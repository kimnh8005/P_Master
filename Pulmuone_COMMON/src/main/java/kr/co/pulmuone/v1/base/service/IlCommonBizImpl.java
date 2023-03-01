package kr.co.pulmuone.v1.base.service;

import kr.co.pulmuone.v1.base.dto.CategoryRequestDto;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class IlCommonBizImpl implements IlCommonBiz {

    @Autowired
    IlCommonService ilCommonService;

    @Override
    public ApiResult<?> getDropDownCategoryList(CategoryRequestDto categoryRequestDto) {
        return ilCommonService.getDropDownCategoryList(categoryRequestDto);
    }

    @Override
    public ApiResult<?> getDropDownCategoryStandardList(CategoryRequestDto categoryRequestDto) {
        return ilCommonService.getDropDownCategoryStandardList(categoryRequestDto);
    }
}
