package kr.co.pulmuone.v1.base.service;

import kr.co.pulmuone.v1.base.dto.CategoryResponseDto;
import kr.co.pulmuone.v1.base.dto.vo.CategoryVo;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.pulmuone.v1.base.dto.CategoryRequestDto;
import kr.co.pulmuone.v1.comm.mapper.base.IlCommonMapper;

import java.util.List;

@Service
public class IlCommonService {

    @Autowired
    IlCommonMapper ilCommonMapper;

	/**
	 * @Desc 전시 카테고리 조회 DropDown
	 * @param categoryRequestDto
	 * @return CategoryResponseDto
	 * @throws Exception
	 */
	protected ApiResult<?> getDropDownCategoryList(CategoryRequestDto categoryRequestDto) {
	    CategoryResponseDto result = new CategoryResponseDto();

		List<CategoryVo> rows = ilCommonMapper.getDropDownCategoryList(categoryRequestDto);

		result.setRows(rows);

		return ApiResult.success(result);
	}

	/**
	 * @Desc 표준 카테고리 조회 DropDown
	 * @param categoryRequestDto
	 * @return CategoryResponseDto
	 * @throws Exception
	 */
	protected ApiResult<?> getDropDownCategoryStandardList(CategoryRequestDto categoryRequestDto) {
	    CategoryResponseDto result = new CategoryResponseDto();

	    List<CategoryVo> rows = ilCommonMapper.getDropDownCategoryStandardList(categoryRequestDto);

	    result.setRows(rows);

	    return ApiResult.success(result);
	}
}
