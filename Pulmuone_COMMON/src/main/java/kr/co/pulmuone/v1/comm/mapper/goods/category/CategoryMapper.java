package kr.co.pulmuone.v1.comm.mapper.goods.category;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import kr.co.pulmuone.v1.comm.exception.BaseException;
import kr.co.pulmuone.v1.goods.category.dto.CategoryRequestDto;
import kr.co.pulmuone.v1.goods.category.dto.CategoryStdRequestDto;
import kr.co.pulmuone.v1.goods.category.dto.vo.CategoryExcelVo;
import kr.co.pulmuone.v1.goods.category.dto.vo.CategoryStdVo;
import kr.co.pulmuone.v1.goods.category.dto.vo.CategoryVo;

/**
 * <PRE>
 * Forbiz Korea
 * 표준카테고리, 전시카테고리 관리 Mapper Interface
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :   작성일          :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 *  1.0   20200908     dgyoun    리팩토링
 * =======================================================================
 * </PRE>
 */

@Mapper
public interface CategoryMapper {

  // ==========================================================================
  // 전시카테고리
  // ==========================================================================
  List<CategoryVo> getCategoryList(CategoryRequestDto dto) throws BaseException;

  CategoryVo getCategory(CategoryRequestDto dto) throws BaseException;

  int addCategory(CategoryRequestDto dto) throws BaseException;

  int addCategoryPrntsInfo(CategoryRequestDto dto) throws BaseException;

  int putCategoryGroupUseYn(CategoryRequestDto dto) throws BaseException;

  int putCategory(CategoryRequestDto dto) throws BaseException;

  int putCategorySort(CategoryRequestDto dto) throws BaseException;

  int delCategory(CategoryRequestDto dto) throws BaseException;

  List<CategoryExcelVo> getCategoryListForExcel(CategoryRequestDto dto) throws BaseException;

  // ==========================================================================
  // 표준카테고리
  // ==========================================================================
  List<CategoryStdVo> getCategoryStdList(CategoryStdRequestDto dto) throws BaseException;

  CategoryStdVo getCategoryStd(CategoryStdRequestDto dto) throws BaseException;

  int addCategoryStd(CategoryStdRequestDto dto) throws BaseException;

  int addCategoryStdPrntsInfo(CategoryStdRequestDto dto) throws BaseException;

  int putCategoryStd(CategoryStdRequestDto dto) throws BaseException;

  int putCategoryStdSort(CategoryStdRequestDto dto) throws BaseException;

  int delCategoryStd(CategoryStdRequestDto dto) throws BaseException;

  List<CategoryExcelVo> getCategoryStdListForExcel(CategoryStdRequestDto dto) throws BaseException;
}
