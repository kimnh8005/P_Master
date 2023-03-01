package kr.co.pulmuone.v1.display.dictionary.service;

import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.enums.SearchEnums;
import kr.co.pulmuone.v1.comm.mapper.display.CategoryBoostingMapper;
import kr.co.pulmuone.v1.display.dictionary.dto.BoostingTargetCategoryDto;
import kr.co.pulmuone.v1.display.dictionary.dto.CategoryBoostingGridDto;
import kr.co.pulmuone.v1.display.dictionary.dto.CategoryBoostingSearchRequestDto;
import kr.co.pulmuone.v1.display.dictionary.dto.vo.CategoryBoostingVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryBoostingService {

    @Autowired
    private CategoryBoostingMapper categoryBoostingMapper;

    protected List<CategoryBoostingGridDto> getCategoryBoostingList(CategoryBoostingSearchRequestDto searchRequestDto) {
        return categoryBoostingMapper.getCategoryBoostingList(searchRequestDto);
    }

    protected int getCategoryBoostingListCount(CategoryBoostingSearchRequestDto searchRequestDto) {
        return categoryBoostingMapper.getCategoryBoostingListCount(searchRequestDto);
    }

    protected ApiResult addCategoryBoosting(List<CategoryBoostingVo> list) {

        if (isDuplicateCategoryBoostingData(list)) {
            return ApiResult.result(SearchEnums.DictionaryMessage.DUPLICATE_DATA);
        }
        categoryBoostingMapper.addCategoryBoosting(list);

        return ApiResult.success();
    }

    protected ApiResult updateCategoryBoosting(List<CategoryBoostingVo> list) {
        if (isDuplicateCategoryBoostingData(list)) {
            return ApiResult.result(SearchEnums.DictionaryMessage.DUPLICATE_DATA);
        }
        categoryBoostingMapper.updateCategoryBoosting(list);

        return ApiResult.success();
    }

    protected ApiResult deleteCategoryBoosting(List<CategoryBoostingVo> list) {
        categoryBoostingMapper.deleteCategoryBoosting(list);
        return ApiResult.success();
    }


    private boolean isDuplicateCategoryBoostingData(List<CategoryBoostingVo> list) {
        List<String> reducedList = list.stream()
                .map(m -> m.getSearchWord().trim().concat(":").concat(String.valueOf(m.getIlCtgryId())))
                .distinct()
                .collect(Collectors.toList());

        if (list.size() > reducedList.size()) return true;

        if (categoryBoostingMapper.checkCategoryBoostingDuplicate(list) > 0) return true;

        return false;
    }

    protected List<BoostingTargetCategoryDto> getBoostingCategoryList() {
        return categoryBoostingMapper.getBoostingCategoryList();
    }
}
