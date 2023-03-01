package kr.co.pulmuone.v1.goods.category.service;

import java.util.List;

import org.springframework.stereotype.Service;

import kr.co.pulmuone.v1.comm.enums.BaseEnums;
import kr.co.pulmuone.v1.comm.exception.BaseException;
import kr.co.pulmuone.v1.comm.mapper.goods.category.CategoryMapper;
import kr.co.pulmuone.v1.comm.util.SessionUtil;
import kr.co.pulmuone.v1.comm.util.StringUtil;
import kr.co.pulmuone.v1.goods.category.dto.CategoryRequestDto;
import kr.co.pulmuone.v1.goods.category.dto.CategoryResponseDto;
import kr.co.pulmuone.v1.goods.category.dto.CategoryStdRequestDto;
import kr.co.pulmuone.v1.goods.category.dto.CategoryStdResponseDto;
import kr.co.pulmuone.v1.goods.category.dto.vo.CategoryExcelVo;
import kr.co.pulmuone.v1.goods.category.dto.vo.CategoryStdVo;
import kr.co.pulmuone.v1.goods.category.dto.vo.CategoryVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * <PRE>
 * Forbiz Korea
 * 표준카테고리, 전시카테고리 관리
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
public class CategoryService {

  final String YES = "Y";
  final String NO  = "N";

  private final CategoryMapper CategoryMapper;

  // ==========================================================================
  // 전시카테고리
  // ==========================================================================
  /**
   * @Desc  전시카테고리 리스트 조회
   * @param dto
   * @return
   * @throws BaseException
   */
  public CategoryResponseDto getCategoryList(CategoryRequestDto dto) throws BaseException {
    //log.info("# ######################################");
    //log.info("# CategoryServiceImpl.getCategoryList Start");
    //log.info("# ######################################");
    //log.info("# categoryRequestDto :: " + dto.toString());

    CategoryResponseDto result = new CategoryResponseDto();
    List<CategoryVo> rows = CategoryMapper.getCategoryList(dto);

    result.setRows(rows);

    return result;
  }

  /**
   * 전시카테고리 상세 조회
   */
  public CategoryResponseDto getCategory(CategoryRequestDto dto) throws BaseException {
      //log.debug("# " + dto.toString());

    CategoryResponseDto result = new CategoryResponseDto();
    CategoryVo detail = CategoryMapper.getCategory(dto);

      result.setDetail(detail);

      return result;
  }

  /**
   * 전시카테고리 등록
   */
  public CategoryResponseDto addCategory(CategoryRequestDto dto) throws BaseException {

    CategoryResponseDto result = new CategoryResponseDto();

      // 입력값 Set
      if (StringUtil.isEquals(dto.getLinkYn(), "N")) {
        dto.setLinkUrl("");
      }
      dto.setDeleteYn("N");
      // (SessionUtil.getBosUserVO()).getUserId()
      dto.setCreateId((SessionUtil.getBosUserVO()).getUserId());
      dto.setModifyId((SessionUtil.getBosUserVO()).getUserId());

      // ----------------------------------------------------------------------
      // 1.카테고리등록
      // ----------------------------------------------------------------------
      int resultInt = CategoryMapper.addCategory(dto);

      if (resultInt > 0) {

        // --------------------------------------------------------------------
        // 2.카테고리상하정보등록
        // --------------------------------------------------------------------
        resultInt = CategoryMapper.addCategoryPrntsInfo(dto);

        // --------------------------------------------------------------------
        // 3.등록된카테고리상세정보조회
        // --------------------------------------------------------------------
        CategoryVo detail = CategoryMapper.getCategory(dto);
        result.setDetail(detail);
      }

      return result;
  }

  /**
   * 전시카테고리 수정
   */
  public CategoryResponseDto putCategory(CategoryRequestDto dto) throws BaseException {

    CategoryResponseDto result = new CategoryResponseDto();
    int resultInt = 0;

    // 입력값 Set
    if (StringUtil.isEquals(dto.getLinkYn(), "N")) {
      dto.setLinkUrl("");
    }
    dto.setModifyId((SessionUtil.getBosUserVO()).getUserId());

    // ----------------------------------------------------------------------
    // 그룹 사용여부 N-> Y 변경인 경우
    // ----------------------------------------------------------------------
    if (StringUtil.isEquals(dto.getDepth(), "0")) {
      // 그룹인 경우
      if (StringUtil.isEquals(dto.getBefUseYn(), NO)) {
        // 이전사용여부가 Y인 경우
        if (StringUtil.isEquals(dto.getUseYn(), YES)) {
          // 사용여부를 Y로의 변경인 경우
          // ----------------------------------------------------------------
          // 그룹 중 Y인것 N으로 변경
          // ----------------------------------------------------------------
          CategoryRequestDto useNoDto = new CategoryRequestDto();
          useNoDto.setModifyId((SessionUtil.getBosUserVO()).getUserId());
          useNoDto.setUseYn(NO);
          useNoDto.setMallDivision(dto.getMallDivision());
          useNoDto.setDepth("0");
          useNoDto.setBefUseYn(YES);
          resultInt = CategoryMapper.putCategoryGroupUseYn(useNoDto);
          log.debug("# 이전 사용중 Group 미사용처리 결과 :: " + resultInt);
        }
      }
    }

    // ----------------------------------------------------------------------
    // 수정
    // ----------------------------------------------------------------------
    resultInt = CategoryMapper.putCategory(dto);
    log.debug("# 수정 결과 :: " + resultInt);

    if (resultInt > 0) {
      CategoryVo detail = CategoryMapper.getCategory(dto);
      result.setDetail(detail);
    }

    //// TODO TEST Start
    //if (true) {
    //  log.debug("# ################################################");
    //  log.debug("# ################################################");
    //  log.debug("# 테스트 후 강제 오류 처리");
    //  log.debug("# ################################################");
    //  log.debug("# ################################################");
    //  throw new BaseException(BaseEnums.CommBase.VALID_ERROR);
    //}
    //// TODO TEST End

    return result;
  }

  /**
   * 전시카테고리 정렬 변경
   */
  public CategoryResponseDto putCategorySort(CategoryRequestDto dto) throws BaseException {

    CategoryResponseDto result = new CategoryResponseDto();
    int resultInt = 0;
    int resultTotInt = 0;
    String updateDataStr = StringUtil.nvl(dto.getUpdateData());
    //log.info("updateDataStr(A) :: " + updateDataStr);
    updateDataStr = updateDataStr.replaceAll("\\[",  "");
    //log.info("updateDataStr(B) :: " + updateDataStr);
    updateDataStr = updateDataStr.replaceAll("\"",  "");
    //log.info("updateDataStr(C) :: " + updateDataStr);
    updateDataStr = updateDataStr.replaceAll("]",  "");
    //log.info("updateDataStr(C) :: " + updateDataStr);
    String[] updateDataArr = updateDataStr.split(",");

    CategoryRequestDto unitDto = null;

    if (updateDataArr != null && updateDataArr.length > 0) {
      for (int i = 0; i < updateDataArr.length; i++) {
        unitDto = new CategoryRequestDto();
        unitDto.setCategoryId(updateDataArr[i]);
        unitDto.setSort((i+1)+"");
        unitDto.setModifyId((SessionUtil.getBosUserVO()).getUserId());
        resultInt = CategoryMapper.putCategorySort(unitDto);
        resultTotInt += resultInt;
      }
    }

    if (resultTotInt > 0) {
      // 마지막건 조회
      CategoryVo detail = CategoryMapper.getCategory(unitDto);
      result.setDetail(detail);
    }

    return result;
  }

  /**
   * 전시카테고리 삭제
   */
  public CategoryResponseDto delCategory(CategoryRequestDto dto) throws BaseException {

    CategoryResponseDto result = new CategoryResponseDto();
      dto.setModifyId((SessionUtil.getBosUserVO()).getUserId());
      int resultInt = CategoryMapper.delCategory(dto);

      if (resultInt > 0) {
        CategoryVo detail = CategoryMapper.getCategory(dto);
        result.setDetail(detail);
      }

      return result;
  }

  /**
   * @Desc  전시카테고리엑셀리스트조회
   * @param dto
   * @return
   * @throws BaseException
   */
  public List<CategoryExcelVo> getCategoryListForExcel(CategoryRequestDto dto) throws BaseException {
    //log.info("# ######################################");
    //log.info("# CategoryServiceImpl.getCategoryListForExcel Start");
    //log.info("# ######################################");
    //log.info("# categoryRequestDto :: " + dto.toString());

    List<CategoryExcelVo> rows = CategoryMapper.getCategoryListForExcel(dto);
    return rows;
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
  public CategoryStdResponseDto getCategoryStdList(CategoryStdRequestDto dto) throws BaseException {
    //log.info("# ######################################");
    //log.info("# CategoryServiceImpl.getCategoryStdList Start");
    //log.info("# ######################################");
    //log.info("# dto :: " + dto.toString());

    CategoryStdResponseDto result = new CategoryStdResponseDto();
    List<CategoryStdVo> rows = CategoryMapper.getCategoryStdList(dto);

    result.setRows(rows);

    return result;
  }

  /**
   * 표준카테고리 상세 조회
   */
  public CategoryStdResponseDto getCategoryStd(CategoryStdRequestDto dto) throws BaseException {
    //log.info("# dto :: " + dto.toString());

    CategoryStdResponseDto result = new CategoryStdResponseDto();
    CategoryStdVo detail = CategoryMapper.getCategoryStd(dto);

    result.setDetail(detail);

    return result;
  }

  /**
   * 표준카테고리 등록
   */
  public CategoryStdResponseDto addCategoryStd(CategoryStdRequestDto dto) throws BaseException {

    CategoryStdResponseDto result = new CategoryStdResponseDto();

    // 입력값 Set
    dto.setDeleteYn("N");
    dto.setCreateId((SessionUtil.getBosUserVO()).getUserId());
    dto.setModifyId((SessionUtil.getBosUserVO()).getUserId());

    // ------------------------------------------------------------------------
    // 1.표준카테고리등록
    // ------------------------------------------------------------------------
    int resultInt = CategoryMapper.addCategoryStd(dto);

    if (resultInt > 0) {

      // ----------------------------------------------------------------------
      // 2.카테고리상하정보등록
      // ----------------------------------------------------------------------
      resultInt = CategoryMapper.addCategoryStdPrntsInfo(dto);

      // --------------------------------------------------------------------
      // 3.등록된카테고리상세정보조회
      // --------------------------------------------------------------------
      CategoryStdVo detail = CategoryMapper.getCategoryStd(dto);
      result.setDetail(detail);
    }

    return result;
  }

  /**
   * 표준카테고리 수정
   */
  public CategoryStdResponseDto putCategoryStd(CategoryStdRequestDto dto) throws BaseException {

    CategoryStdResponseDto result = new CategoryStdResponseDto();

    // 입력값 Set
    dto.setModifyId((SessionUtil.getBosUserVO()).getUserId());

    int resultInt = CategoryMapper.putCategoryStd(dto);

    if (resultInt > 0) {

      CategoryStdVo detail = CategoryMapper.getCategoryStd(dto);
      result.setDetail(detail);
    }

    return result;
  }

  /**
   * 표준카테고리 정렬 변경
   */
  public CategoryStdResponseDto putCategoryStdSort(CategoryStdRequestDto dto) throws BaseException {

    CategoryStdResponseDto result = new CategoryStdResponseDto();
    int resultInt = 0;
    int resultTotInt = 0;
    String updateDataStr = StringUtil.nvl(dto.getUpdateData());
    //log.info("updateDataStr(A) :: " + updateDataStr);
    updateDataStr = updateDataStr.replaceAll("\\[",  "");
    //log.info("updateDataStr(B) :: " + updateDataStr);
    updateDataStr = updateDataStr.replaceAll("\"",  "");
    //log.info("updateDataStr(C) :: " + updateDataStr);
    updateDataStr = updateDataStr.replaceAll("]",  "");
    //log.info("updateDataStr(C) :: " + updateDataStr);
    String[] updateDataArr = updateDataStr.split(",");

    CategoryStdRequestDto unitDto = null;

    if (updateDataArr != null && updateDataArr.length > 0) {
      for (int i = 0; i < updateDataArr.length; i++) {
        unitDto = new CategoryStdRequestDto();
        unitDto.setStandardCategoryId(updateDataArr[i]);
        unitDto.setSort((i+1)+"");
        unitDto.setModifyId((SessionUtil.getBosUserVO()).getUserId());
        resultInt = CategoryMapper.putCategoryStdSort(unitDto);
        resultTotInt += resultInt;
      }
    }

    if (resultTotInt > 0) {
      // 마지막건 조회
      CategoryStdVo detail = CategoryMapper.getCategoryStd(unitDto);
      result.setDetail(detail);
    }

    return result;
  }

  /**
   * 표준카테고리 삭제
   */
  public CategoryStdResponseDto delCategoryStd(CategoryStdRequestDto dto) throws BaseException {

    CategoryStdResponseDto result = new CategoryStdResponseDto();
    dto.setModifyId((SessionUtil.getBosUserVO()).getUserId());
    int resultInt = CategoryMapper.delCategoryStd(dto);

    if (resultInt > 0) {
      CategoryStdVo detail = CategoryMapper.getCategoryStd(dto);
      result.setDetail(detail);
    }

    return result;
  }

  /**
   * @Desc  표준카테고리엑셀리스트조회
   * @param dto
   * @return
   * @throws BaseException
   */
  public List<CategoryExcelVo> getCategoryStdListForExcel(CategoryStdRequestDto dto) throws BaseException {
    //log.info("# ######################################");
    //log.info("# CategoryServiceImpl.getCategoryStdListForExcel Start");
    //log.info("# ######################################");
    //log.info("# dto :: " + dto.toString());

    List<CategoryExcelVo> rows = CategoryMapper.getCategoryStdListForExcel(dto);
    return rows;
  }
}
