package kr.co.pulmuone.v1.comm.mapper.base;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import kr.co.pulmuone.v1.base.dto.CategoryRequestDto;
import kr.co.pulmuone.v1.base.dto.vo.CategoryVo;

@Mapper
public interface IlCommonMapper {

    /**
     * @Desc 전시 카테고리 조회 DropDown
     * @param categoryRequestDto
     * @return List<CategoryVo>
     */
    List<CategoryVo> getDropDownCategoryList(CategoryRequestDto categoryRequestDto);

    /**
     * @Desc 표준 카테고리 조회 DropDown
     * @param categoryRequestDto
     * @return List<CategoryVo>
     */
    List<CategoryVo> getDropDownCategoryStandardList(CategoryRequestDto categoryRequestDto);
}
