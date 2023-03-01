package kr.co.pulmuone.v1.comm.mapper.display;

import kr.co.pulmuone.v1.display.dictionary.dto.BoostingTargetCategoryDto;
import kr.co.pulmuone.v1.display.dictionary.dto.CategoryBoostingGridDto;
import kr.co.pulmuone.v1.display.dictionary.dto.CategoryBoostingSearchRequestDto;
import kr.co.pulmuone.v1.display.dictionary.dto.vo.CategoryBoostingVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CategoryBoostingMapper {

    List<CategoryBoostingGridDto> getCategoryBoostingList(CategoryBoostingSearchRequestDto searchRequestDto);

    int getCategoryBoostingListCount(CategoryBoostingSearchRequestDto searchRequestDto);

    int checkCategoryBoostingDuplicate(List<CategoryBoostingVo> list);

    int addCategoryBoosting(List<CategoryBoostingVo> list);

    int updateCategoryBoosting(List<CategoryBoostingVo> list);

    int deleteCategoryBoosting(List<CategoryBoostingVo> list);

    List<BoostingTargetCategoryDto> getBoostingCategoryList();
}
