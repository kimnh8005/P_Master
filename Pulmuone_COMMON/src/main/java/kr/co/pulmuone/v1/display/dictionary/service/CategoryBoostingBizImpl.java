package kr.co.pulmuone.v1.display.dictionary.service;

import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.base.dto.PagingListDataDto;
import kr.co.pulmuone.v1.comm.enums.BaseEnums;
import kr.co.pulmuone.v1.display.dictionary.dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CategoryBoostingBizImpl implements CategoryBoostingBiz {

    @Autowired
    private CategoryBoostingService categoryBoostingService;

    @Override
    public PagingListDataDto<CategoryBoostingGridDto> getCategoryBoostingPagingList(CategoryBoostingSearchRequestDto searchRequestDto) {

        List<CategoryBoostingGridDto> categoryBoostingList = categoryBoostingService.getCategoryBoostingList(searchRequestDto);
        int totalCount = categoryBoostingService.getCategoryBoostingListCount(searchRequestDto);

        return new PagingListDataDto<>(searchRequestDto.getPage()
                , searchRequestDto.getPageSize()
                , totalCount
                , categoryBoostingList);
    }

    @Transactional
    @Override
    public ApiResult<?> saveCategoryBoosting(CategoryBoostingSaveRequestDto saveRequestDto) {
        if (saveRequestDto.hasInsertData()) {
            ApiResult insertResult = categoryBoostingService.addCategoryBoosting(saveRequestDto.getInsertList());
            if (BaseEnums.Default.SUCCESS.getCode() != insertResult.getCode()) return insertResult;
        }

        if (saveRequestDto.hasUpdateData()) {
            ApiResult updateResult = categoryBoostingService.updateCategoryBoosting(saveRequestDto.getUpdateList());
            if (BaseEnums.Default.SUCCESS.getCode() != updateResult.getCode()) return updateResult;
        }

        if (saveRequestDto.hasDeleteData()) {
            ApiResult deleteResult = categoryBoostingService.deleteCategoryBoosting(saveRequestDto.getDeleteList());
            if (BaseEnums.Default.SUCCESS.getCode() !=  deleteResult.getCode()) return deleteResult;
        }

        return ApiResult.success();
    }

    @Override
    public BoostingTargetCategoryListDto getBoostingCategoryList() {
        List<BoostingTargetCategoryDto> list = categoryBoostingService.getBoostingCategoryList();
        BoostingTargetCategoryListDto result = new BoostingTargetCategoryListDto();
        result.setRows(list);
        return result;
    }

}
