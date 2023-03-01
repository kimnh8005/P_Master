package kr.co.pulmuone.bos.goods.category.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.exception.BaseException;
import kr.co.pulmuone.v1.goods.category.dto.CategoryRequestDto;
import kr.co.pulmuone.v1.goods.category.dto.CategoryStdRequestDto;
import kr.co.pulmuone.v1.goods.category.service.CategoryBiz;

/**
* <PRE>
* Forbiz Korea
* 카테고리관리 BOS Service
* 1. 전시카테고리관리
* 2. 표준카테고리관리
* </PRE>
*
* <PRE>
* <B>History:</B>
* =======================================================================
*  버전  :   작성일                :  작성자      :  작성내역
* -----------------------------------------------------------------------
*  1.0    2020.09.08.    dgyoun   최초작성
* =======================================================================
* </PRE>
*/

@Service
public class CategoryBosService {

  @Autowired
  private CategoryBiz categoryBiz;

  // ==========================================================================
  // 전시카테고리
  // ==========================================================================
  /**
   * @Desc  전시카테고리 리스트 조회
   * @param dto
   * @return
   * @throws BaseException
   */
  public ApiResult<?> getCategoryList(CategoryRequestDto dto) throws BaseException {

    return categoryBiz.getCategoryList(dto);
  }

  /**
   * 전시카테고리 상세 조회
   */
  public ApiResult<?> getCategory(CategoryRequestDto dto) throws BaseException {

    return categoryBiz.getCategory(dto);
  }

  /**
   * 전시카테고리 등록
   */
  public ApiResult<?> addCategory(CategoryRequestDto dto) throws BaseException {

    return categoryBiz.addCategory(dto);
  }

  /**
   * 전시카테고리 수정
   */
  public ApiResult<?> putCategory(CategoryRequestDto dto) throws BaseException {

    return categoryBiz.putCategory(dto);
  }

  /**
   * 전시카테고리 정렬 변경
   */
  public ApiResult<?> putCategorySort(CategoryRequestDto dto) throws BaseException {

    return categoryBiz.putCategorySort(dto);
  }

  /**
   * 전시카테고리 삭제
   */
  public ApiResult<?> delCategory(CategoryRequestDto dto) throws BaseException {

      return categoryBiz.delCategory(dto);
  }

  /**
   * @Desc  전시카테고리엑셀리스트조회
   * @param dto
   * @return
   * @throws BaseException
   */
  public ApiResult<?> getCategoryListForExcel(CategoryRequestDto dto) throws BaseException {

    return categoryBiz.getCategoryListForExcel(dto);
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
  public ApiResult<?> getCategoryStdList(CategoryStdRequestDto dto) throws BaseException {

    return categoryBiz.getCategoryStdList(dto);
  }

  /**
   * 표준카테고리 상세 조회
   */
  public ApiResult<?> getCategoryStd(CategoryStdRequestDto dto) throws BaseException {

    return categoryBiz.getCategoryStd(dto);
  }

  /**
   * 표준카테고리 등록
   */
  public ApiResult<?> addCategoryStd(CategoryStdRequestDto dto) throws BaseException {

    return categoryBiz.addCategoryStd(dto);
  }

  /**
   * 표준카테고리 수정
   */
  public ApiResult<?> putCategoryStd(CategoryStdRequestDto dto) throws BaseException {

    return categoryBiz.putCategoryStd(dto);
  }

  /**
   * 표준카테고리 정렬 변경
   */
  public ApiResult<?> putCategoryStdSort(CategoryStdRequestDto dto) throws BaseException {

    return categoryBiz.putCategoryStdSort(dto);
  }

  /**
   * 표준카테고리 삭제
   */
  public ApiResult<?> delCategoryStd(CategoryStdRequestDto dto) throws BaseException {

    return categoryBiz.delCategoryStd(dto);
  }

  /**
   * @Desc  표준카테고리엑셀리스트조회
   * @param dto
   * @return
   * @throws BaseException
   */
  public ApiResult<?> getCategoryStdListForExcel(CategoryStdRequestDto dto) throws BaseException {

    return categoryBiz.getCategoryStdListForExcel(dto);
  }
}
