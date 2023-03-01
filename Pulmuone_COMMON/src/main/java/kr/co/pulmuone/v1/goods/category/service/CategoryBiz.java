package kr.co.pulmuone.v1.goods.category.service;

import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.exception.BaseException;
import kr.co.pulmuone.v1.goods.category.dto.CategoryRequestDto;
import kr.co.pulmuone.v1.goods.category.dto.CategoryStdRequestDto;

/**
 * <PRE>
 * Forbiz Korea
 * 표준카테고리, 전시카테고리 관리 I/F
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

public interface CategoryBiz {

  // ==========================================================================
  // 전시카테고리
  // ==========================================================================
  ApiResult<?> getCategoryList(CategoryRequestDto dto) throws BaseException;

  ApiResult<?> getCategory(CategoryRequestDto dto) throws BaseException;

  ApiResult<?> addCategory(CategoryRequestDto dto) throws BaseException;

  ApiResult<?> putCategory(CategoryRequestDto dto) throws BaseException;

  ApiResult<?> putCategorySort(CategoryRequestDto dto) throws BaseException;

  ApiResult<?> delCategory(CategoryRequestDto dto) throws BaseException;

  ApiResult<?> getCategoryListForExcel(CategoryRequestDto dto) throws BaseException;

  // ==========================================================================
  // 표준카테고리
  // ==========================================================================
  ApiResult<?> getCategoryStdList(CategoryStdRequestDto dto) throws BaseException;

  ApiResult<?> getCategoryStd(CategoryStdRequestDto dto) throws BaseException;

  ApiResult<?> addCategoryStd(CategoryStdRequestDto dto) throws BaseException;

  ApiResult<?> putCategoryStd(CategoryStdRequestDto dto) throws BaseException;

  ApiResult<?> putCategoryStdSort(CategoryStdRequestDto dto) throws BaseException;

  ApiResult<?> delCategoryStd(CategoryStdRequestDto dto) throws BaseException;

  ApiResult<?> getCategoryStdListForExcel(CategoryStdRequestDto dto) throws BaseException;
}
