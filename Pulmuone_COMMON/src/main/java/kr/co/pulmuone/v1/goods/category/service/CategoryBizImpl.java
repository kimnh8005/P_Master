package kr.co.pulmuone.v1.goods.category.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.exception.BaseException;
import kr.co.pulmuone.v1.goods.category.dto.CategoryRequestDto;
import kr.co.pulmuone.v1.goods.category.dto.CategoryResponseDto;
import kr.co.pulmuone.v1.goods.category.dto.CategoryStdRequestDto;
import kr.co.pulmuone.v1.goods.category.dto.CategoryStdResponseDto;
import kr.co.pulmuone.v1.goods.category.dto.vo.CategoryExcelVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * <PRE>
 * Forbiz Korea
 * 표준카테고리, 전시카테고리 관리 Impl
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

@Slf4j
@Service
@RequiredArgsConstructor
public class CategoryBizImpl implements CategoryBiz {

  @Autowired
  private CategoryService categoryService;

  // ==========================================================================
  // 전시카테고리
  // ==========================================================================
  /**
   * @Desc  전시카테고리 리스트 조회
   * @param dto
   * @return
   * @throws BaseException
   */
  @Override
  public ApiResult<?> getCategoryList(CategoryRequestDto dto) throws BaseException {
    //log.info("# ######################################");
    //log.info("# CategoryServiceImpl.getCategoryList Start");
    //log.info("# ######################################");
    //log.info("# categoryRequestDto :: " + dto.toString());

    CategoryResponseDto result = categoryService.getCategoryList(dto);

    return ApiResult.success(result);
  }

  /**
   * 전시카테고리 상세 조회
   */
  @Override
  public ApiResult<?> getCategory(CategoryRequestDto dto) throws BaseException {
      //log.debug("# " + dto.toString());

    CategoryResponseDto result = categoryService.getCategory(dto);

    return ApiResult.success(result);

  }

  /**
   * 전시카테고리 등록
   */
  @Override
  @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {BaseException.class, Exception.class})
  public ApiResult<?> addCategory(CategoryRequestDto dto) throws BaseException {

    CategoryResponseDto result = categoryService.addCategory(dto);

    return ApiResult.success(result);
  }

  /**
   * 전시카테고리 수정
   */
  @Override
  @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {BaseException.class, Exception.class})
  public ApiResult<?> putCategory(CategoryRequestDto dto) throws BaseException {

    CategoryResponseDto result = categoryService.putCategory(dto);

    return ApiResult.success(result);
  }

  /**
   * 전시카테고리 정렬 변경
   */
  @Override
  @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {BaseException.class, Exception.class})
  public ApiResult<?> putCategorySort(CategoryRequestDto dto) throws BaseException {

    CategoryResponseDto result = categoryService.putCategorySort(dto);

    return ApiResult.success(result);
  }

  /**
   * 전시카테고리 삭제
   */
  @Override
  @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {BaseException.class, Exception.class})
  public ApiResult<?> delCategory(CategoryRequestDto dto) throws BaseException {

      CategoryResponseDto result = categoryService.delCategory(dto);

      return ApiResult.success(result);
  }

  /**
   * @Desc  전시카테고리엑셀리스트조회
   * @param dto
   * @return
   * @throws BaseException
   */
  @Override
  public ApiResult<?> getCategoryListForExcel(CategoryRequestDto dto) throws BaseException {
    //log.info("# ######################################");
    //log.info("# CategoryServiceImpl.getCategoryListForExcel Start");
    //log.info("# ######################################");
    //log.info("# categoryRequestDto :: " + dto.toString());

    List<CategoryExcelVo> result = categoryService.getCategoryListForExcel(dto);

    return ApiResult.success(result);
  }

  // ==========================================================================
  // 표준카테고리
  // ==========================================================================
  /**
   * @Desc  표준카테고리 리스트 조회
   * @param dto
   * @return
   * @throws BaseException
   */
  @Override
  public ApiResult<?> getCategoryStdList(CategoryStdRequestDto dto) throws BaseException {
    //log.info("# ######################################");
    //log.info("# CategoryServiceImpl.getCategoryStdList Start");
    //log.info("# ######################################");
    //log.info("# dto :: " + dto.toString());

    CategoryStdResponseDto result = categoryService.getCategoryStdList(dto);

    return ApiResult.success(result);
  }

  /**
   * 표준카테고리 상세 조회
   */
  @Override
  public ApiResult<?> getCategoryStd(CategoryStdRequestDto dto) throws BaseException {
    //log.info("# dto :: " + dto.toString());

    CategoryStdResponseDto result = categoryService.getCategoryStd(dto);

    return ApiResult.success(result);
  }

  /**
   * 표준카테고리 등록
   */
  @Override
  @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {BaseException.class, Exception.class})
  public ApiResult<?> addCategoryStd(CategoryStdRequestDto dto) throws BaseException {

    CategoryStdResponseDto result = categoryService.addCategoryStd(dto);

    return ApiResult.success(result);
  }

  /**
   * 표준카테고리 수정
   */
  @Override
  @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {BaseException.class, Exception.class})
  public ApiResult<?> putCategoryStd(CategoryStdRequestDto dto) throws BaseException {

    CategoryStdResponseDto result = categoryService.putCategoryStd(dto);

    return ApiResult.success(result);
  }

  /**
   * 표준카테고리 정렬 변경
   */
  @Override
  @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {BaseException.class, Exception.class})
  public ApiResult<?> putCategoryStdSort(CategoryStdRequestDto dto) throws BaseException {

    CategoryStdResponseDto result = categoryService.putCategoryStdSort(dto);

    return ApiResult.success(result);
  }

  /**
   * 표준카테고리 삭제
   */
  @Override
  @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {BaseException.class, Exception.class})
  public ApiResult<?> delCategoryStd(CategoryStdRequestDto dto) throws BaseException {

    CategoryStdResponseDto result = categoryService.delCategoryStd(dto);

    return ApiResult.success(result);
  }

  /**
   * @Desc  표준카테고리엑셀리스트조회
   * @param dto
   * @return
   * @throws BaseException
   */
  @Override
  public ApiResult<?> getCategoryStdListForExcel(CategoryStdRequestDto dto) throws BaseException {
    //log.info("# ######################################");
    //log.info("# CategoryServiceImpl.getCategoryStdListForExcel Start");
    //log.info("# ######################################");
    //log.info("# dto :: " + dto.toString());

    List<CategoryExcelVo> result = categoryService.getCategoryStdListForExcel(dto);

    return ApiResult.success(result);
  }

}
