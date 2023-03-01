package kr.co.pulmuone.bos.goods.category.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import io.swagger.annotations.ApiOperation;
import kr.co.pulmuone.bos.goods.category.service.CategoryBosService;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.framework.dto.ExcelDownloadDto;
import kr.co.pulmuone.v1.comm.framework.dto.ExcelWorkSheetDto;
import kr.co.pulmuone.v1.comm.framework.view.ExcelDownloadView;
import kr.co.pulmuone.v1.comm.util.DateUtil;
import kr.co.pulmuone.v1.goods.category.dto.CategoryRequestDto;
import kr.co.pulmuone.v1.goods.category.dto.CategoryStdRequestDto;
import kr.co.pulmuone.v1.goods.category.dto.vo.CategoryExcelVo;
import lombok.extern.slf4j.Slf4j;

/**
* <PRE>
* Forbiz Korea
* 카테고리관리 BOS Controller
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

@Slf4j
@RestController
public class CategoryController {

  @SuppressWarnings("unused")
  @Autowired(required = true)
  private HttpServletRequest request;

  //@Resource(name = "categoryBosService")
  @Autowired
  @Qualifier("categoryBosService")
  private CategoryBosService categoryBosService;

  @Autowired
  private ExcelDownloadView excelDownloadView; // 엑셀 다운로드 뷰

  // ==========================================================================
  // 전시카테고리
  // ==========================================================================

  /**
   * 전시카테고리 리스트 조회
   * @param CategoryRequestDto
   * @return CategoryResponseDto
   * @throws Exception
   */
  @ApiOperation(value = "전시카테고리리스트조회")
  @RequestMapping(value = "/admin/goods/category/getCategoryList")
  public ApiResult<?> getCategoryList(@ModelAttribute("CategoryRequestDto") CategoryRequestDto dto, @RequestParam Map<String, Object> inParam) throws Exception{
    //log.debug("# ######################################");
    //log.debug("# CategoryController.getCategoryList Start");
    //log.debug("# ######################################");
    //log.debug("# dto     :: " + dto.toString());
    //log.debug("# inParam :: " + inParam.toString());

    return categoryBosService.getCategoryList(dto);

  }

  /**
   * 전시카테고리 리스트 엑셀 다운로드
   * @param dto
   * @return
   * @throws Exception
   */
  @SuppressWarnings("unchecked")
  @ApiOperation(value = "전시카테고리리스트엑셀다운로드")
  @RequestMapping(value = "/admin/goods/category/getExportExcelCategoryList")
  public ModelAndView getExportExcelCategoryList(@RequestBody CategoryRequestDto dto, HttpServletRequest request, HttpServletResponse response) throws Exception {
    //log.debug("# ######################################");
    //log.debug("# CategoryController.getExportExcelCategoryList Start");
    //log.debug("# ######################################");
    //log.debug("# dto :: " + dto.toString());

    // 엑셀 파일 이름: 확장자는 xlsx 자동 설정됨
    String excelFileName   = "DisplayCategory" + "_" + DateUtil.getCurrentDate();
    //String excelFileName = new String(("전시카테고리" + "_" + DateUtil.getCurrentDate()).getBytes("utf-8"), "8859_1");
    //String excelFileName = new String(("전시카테고리" + "_" + DateUtil.getCurrentDate()).getBytes("euc-kr"), "8859_1");
    //HttpServletResponse response
    //response.setHeader("Content-Type", "application/octet-stream");
    //response.setHeader("Content-Disposition", "attachment; filename=\"" + excelFileName + ".xlsx\"");

    // 엑셀 파일 내 워크시트 이름
    String excelSheetName  = "sheet";

    //log.debug("# excelFileName :: " + excelFileName);
    /*
     * 컬럼별 width 목록 : 단위 pixel
     * ( 필수 아님 ) ExcelWorkSheetDto 에 지정하지 않을 경우 모든 컬럼 너비는 120 pixel 로 고정됨
     */
    Integer[] widthListOfFirstWorksheet = {150, 300, 300, 300, 300, 300};

    /*
     * 본문 데이터 컬럼별 정렬 목록
     * ( 필수 아님 ) ExcelWorkSheetDto 에 지정하지 않을 경우 모든 컬럼 정렬은 "left" (좌측 정렬) 로 고정
     * "left", "center", "right", "justify", "distributed" 가 아닌 다른 값 지정시 "left" (좌측 정렬) 로 지정됨
     */
    String[] alignListOfFirstWorksheet = {"center", "left", "left", "left", "left", "left"};

    /*
     * 본문 데이터 컬럼별 데이터 property 목록
     * ( 필수 ) 엑셀 데이터 JSON 변환시 각 json Object 의 key 값과 일치해야 함
     */
    String[] propertyListOfFirstWorksheet = {"categoryId", "categoryName0", "categoryName1", "categoryName2", "categoryName3", "categoryName4"};

    // 첫 번째 워크시트의 상단 헤더의 각 행별 정보 : 다단 구성, 셀 머지 가능
    // 첫 번째 헤더 컬럼 : 동일 이름 연속시 셀 머지
    String[] firstHeaderListOfFirstWorksheet = {"카테고리ID", "그룹명", "대분류", "중분류", "소분류", "세분류"};

    // 워크시트 DTO 생성 후 정보 세팅
    ExcelWorkSheetDto firstWorkSheetDto = ExcelWorkSheetDto.builder()   //
        .workSheetName(excelSheetName)                                  // 엑셀 파일내 워크시트 명
        .propertyList(propertyListOfFirstWorksheet)                     // 컬럼별 데이터 property 목록
        .widthList(widthListOfFirstWorksheet)                           // 컬럼별 너비 목록
        .alignList(alignListOfFirstWorksheet)                           // 컬럼별 정렬 목록
        .build();

    // 엑셀 다단 헤더 구성 : ( 헤더 행 index , 헤더 제목 배열 ) 형식으로 세팅
    // 첫 번째 헤더 컬럼
    firstWorkSheetDto.setHeaderList(0, firstHeaderListOfFirstWorksheet);

    /*
     * 엑셀 본문 샘플 데이터 생성 : List<?> 형식만 세팅 가능
     * excelData 를 세팅하지 않으면 샘플 엑셀로 다운로드됨
     */
    ApiResult<?> resultApi = null;
    List<CategoryExcelVo> resultList = null;

    try {

      //resultList = categoryBosService.getCategoryListForExcel(dto);
      resultApi = categoryBosService.getCategoryListForExcel(dto);
      //resultList = (List<CategoryExcelVo>) resultApi.getRows();
      resultList = (List<CategoryExcelVo>) resultApi.getData();

    } catch (Exception e) {
        log.error(e.getMessage());
        throw e; // 추후 CustomException 으로 변환 예정
    }
    firstWorkSheetDto.setExcelDataList(resultList);

    // xcelDownloadDto 생성 후 workSheetDto 추가
    ExcelDownloadDto excelDownloadDto = ExcelDownloadDto.builder()  //
        .excelFileName(excelFileName)                               //
        .build();

    excelDownloadDto.addExcelWorkSheet(firstWorkSheetDto);

    ModelAndView modelAndView = new ModelAndView(excelDownloadView);
    modelAndView.addObject(ExcelDownloadView.excelDownloadDtoKeyInModel, excelDownloadDto);

    return modelAndView;
  }

  /**
   * 전시카테고리 상세 조회
   * @param CategoryRequestDto
   * @return CategoryResponseDto
   * @throws Exception
   */
  @ApiOperation(value = "전시카테고리상세조회")
  @RequestMapping(value = "/admin/goods/category/getCategory")
  public ApiResult<?> getCategory(CategoryRequestDto dto) throws Exception{

    return categoryBosService.getCategory(dto);
  }

  /**
   * 전시카테고리 등록
   * @param CategoryRequestDto
   * @return CategoryResponseDto
   * @throws Exception
   */
  @ApiOperation(value = "전시카테고리등록")
  @RequestMapping(value = "/admin/goods/category/addCategory")
  public ApiResult<?> addCategory(CategoryRequestDto dto) throws Exception{
    //log.debug("# ######################################");
    //log.debug("# CategoryController.addCategory Start");
    //log.debug("# ######################################");
    //log.debug("# CategoryRequestDto :: " + dto.toString());

    return categoryBosService.addCategory(dto);
  }

  /**
   * 전시카테고리 수정
   * @param CategoryRequestDto
   * @return CategoryResponseDto
   * @throws Exception
   */
  @ApiOperation(value = "전시카테고리수정")
  @RequestMapping(value = "/admin/goods/category/putCategory")
  public ApiResult<?> putCategory(CategoryRequestDto dto) throws Exception{
    log.debug("# ######################################");
    log.debug("# CategoryController.putCategory Start");
    log.debug("# ######################################");
    log.debug("# CategoryRequestDto :: " + dto.toString());

    return categoryBosService.putCategory(dto);
  }

  /**
   * 전시카테고리 정렬 수정
   * @param CategoryRequestDto
   * @return CategoryResponseDto
   * @throws Exception
   */
  @ApiOperation(value = "전시카테고리정렬수정")
  @RequestMapping(value = "/admin/goods/category/putCategorySort")
  public ApiResult<?> putCategorySort(CategoryRequestDto dto) throws Exception{
    //log.debug("# ######################################");
    //log.debug("# CategoryController.putCategorySort Start");
    //log.debug("# ######################################");
    //log.debug("# CategoryRequestDto :: " + dto.toString());

    return categoryBosService.putCategorySort(dto);
  }

  /**
   * 전시카테고리 삭제
   * @param CategoryRequestDto
   * @return CategoryResponseDto
   * @throws Exception
   */
  @ApiOperation(value = "전시카테고리삭제")
  @RequestMapping(value = "/admin/goods/category/delCategory")
  public ApiResult<?> delCategory(CategoryRequestDto dto) throws Exception{
    // log.debug("# ######################################");
    //log.debug("# CategoryController.delCategory Start");
    //log.debug("# ######################################");
    //log.debug("# CategoryRequestDto :: " + dto.toString());

    return categoryBosService.delCategory(dto);
  }

  // ==========================================================================
  // 표준카테고리
  // ==========================================================================

  /**
   * 표준카테고리 리스트 조회
   * @param CategoryStdRequestDto
   * @return CategoryStdResponseDto
   * @throws Exception
   */
  @ApiOperation(value = "표준카테고리리스트조회")
  @RequestMapping(value = "/admin/goods/category/getCategoryStdList")
  public ApiResult<?> getCategoryStdList(@ModelAttribute("CategoryStdRequestDto") CategoryStdRequestDto dto) throws Exception{
    log.debug("# ######################################");
    log.debug("# CategoryController.getCategoryStdList Start");
    log.debug("# ######################################");

    return categoryBosService.getCategoryStdList(dto);
  }

  /**
   * 표준카테고리 리스트 엑셀 다운로드
   * @param dto
   * @return
   * @throws Exception
   */
  @SuppressWarnings("unchecked")
  @ApiOperation(value = "표준카테고리리스트엑셀다운로드")
  @RequestMapping(value = "/admin/goods/category/getExportExcelCategoryStdList")
  public ModelAndView getExportExcelCategoryStdList(@RequestBody CategoryStdRequestDto dto, HttpServletRequest request, HttpServletResponse response) throws Exception {
    //log.debug("# ######################################");
    //log.debug("# CategoryController.getExportExcelCategoryStdList Start");
    //log.debug("# ######################################");
    //log.debug("# dto :: " + dto.toString());

    // 엑셀 파일 이름: 확장자는 xlsx 자동 설정됨
    String excelFileName   = "StandardCategory" + "_" + DateUtil.getCurrentDate();
    //String excelFileName   = "표준카테고리" + "_" + DateUtil.getCurrentDate();
    // 엑셀 파일 내 워크시트 이름
    String excelSheetName  = "sheet";

    //log.debug("# excelFileName :: " + excelFileName);

    /*
     * 컬럼별 width 목록 : 단위 pixel
     * ( 필수 아님 ) ExcelWorkSheetDto 에 지정하지 않을 경우 모든 컬럼 너비는 120 pixel 로 고정됨
     */
    Integer[] widthListOfFirstWorksheet = {150, 300, 300, 300, 300};

    /*
     * 본문 데이터 컬럼별 정렬 목록
     * ( 필수 아님 ) ExcelWorkSheetDto 에 지정하지 않을 경우 모든 컬럼 정렬은 "left" (좌측 정렬) 로 고정
     * "left", "center", "right", "justify", "distributed" 가 아닌 다른 값 지정시 "left" (좌측 정렬) 로 지정됨
     */
    String[] alignListOfFirstWorksheet = {"center", "left", "left", "left", "left"};

    /*
     * 본문 데이터 컬럼별 데이터 property 목록
     * ( 필수 ) 엑셀 데이터 JSON 변환시 각 json Object 의 key 값과 일치해야 함
     */
    String[] propertyListOfFirstWorksheet = {"categoryId", "categoryName1", "categoryName2", "categoryName3", "categoryName4"};

    // 첫 번째 워크시트의 상단 헤더의 각 행별 정보 : 다단 구성, 셀 머지 가능
    // 첫 번째 헤더 컬럼 : 동일 이름 연속시 셀 머지
    String[] firstHeaderListOfFirstWorksheet = {"카테고리ID", "대분류", "중분류", "소분류", "세분류"};

    // 워크시트 DTO 생성 후 정보 세팅
    ExcelWorkSheetDto firstWorkSheetDto = ExcelWorkSheetDto.builder()   //
        .workSheetName(excelSheetName)                                  // 엑셀 파일내 워크시트 명
        .propertyList(propertyListOfFirstWorksheet)                     // 컬럼별 데이터 property 목록
        .widthList(widthListOfFirstWorksheet)                           // 컬럼별 너비 목록
        .alignList(alignListOfFirstWorksheet)                           // 컬럼별 정렬 목록
        .build();

    // 엑셀 다단 헤더 구성 : ( 헤더 행 index , 헤더 제목 배열 ) 형식으로 세팅
    // 첫 번째 헤더 컬럼
    firstWorkSheetDto.setHeaderList(0, firstHeaderListOfFirstWorksheet);

    /*
     * 엑셀 본문 샘플 데이터 생성 : List<?> 형식만 세팅 가능
     * excelData 를 세팅하지 않으면 샘플 엑셀로 다운로드됨
     */
    ApiResult<?> resultApi = null;
    List<CategoryExcelVo> resultList = null;

    try {

      resultApi = categoryBosService.getCategoryStdListForExcel(dto);
      //resultList = (List<CategoryExcelVo>) resultApi.getRows();
      resultList = (List<CategoryExcelVo>) resultApi.getData();
    } catch (Exception e) {
        log.error(e.getMessage());
        throw e; // 추후 CustomException 으로 변환 예정
    }
    firstWorkSheetDto.setExcelDataList(resultList);

    // xcelDownloadDto 생성 후 workSheetDto 추가
    ExcelDownloadDto excelDownloadDto = ExcelDownloadDto.builder()  //
        //.excelFileName(URLEncoder.encode(excelFileName, "UTF-8"))   //
        .excelFileName(excelFileName)   //
        .build();

    excelDownloadDto.addExcelWorkSheet(firstWorkSheetDto);

    ModelAndView modelAndView = new ModelAndView(excelDownloadView);
    modelAndView.addObject(ExcelDownloadView.excelDownloadDtoKeyInModel, excelDownloadDto);

    return modelAndView;
  }



  /**
   * 표준카테고리 상세 조회
   * @param CategoryStdRequestDto
   * @return CategoryStdResponseDto
   * @throws Exception
   */
  @ApiOperation(value = "표준카테고리상세조회")
  @RequestMapping(value = "/admin/goods/category/getCategoryStd")
  public ApiResult<?> getCategoryStd(CategoryStdRequestDto dto) throws Exception{
    //log.debug("# ######################################");
    //log.debug("# CategoryController.getCategoryStd Start");
    //log.debug("# ######################################");
    //log.debug("# dto :: " + dto.toString());

    return categoryBosService.getCategoryStd(dto);
  }

  /**
   * 표준카테고리 등록
   * @param CategoryStdRequestDto
   * @return CategoryStdResponseDto
   * @throws Exception
   */
  @ApiOperation(value = "표준카테고리등록")
  @RequestMapping(value = "/admin/goods/category/addCategoryStd")
  public ApiResult<?> addCategoryStd(CategoryStdRequestDto dto) throws Exception{
    //log.debug("# ######################################");
    //log.debug("# CategoryController.addCategoryStd Start");
    //log.debug("# ######################################");
    //log.debug("# dto :: " + dto.toString());

    return categoryBosService.addCategoryStd(dto);
  }

  /**
   * 표준카테고리 수정
   * @param CategoryStdRequestDto
   * @return CategoryStdResponseDto
   * @throws Exception
   */
  @ApiOperation(value = "표준카테고리수정")
  @RequestMapping(value = "/admin/goods/category/putCategoryStd")
  public ApiResult<?> putCategoryStd(CategoryStdRequestDto dto) throws Exception{
    //log.debug("# ######################################");
    //log.debug("# CategoryController.putCategoryStd Start");
    //log.debug("# ######################################");
    //log.debug("# dto :: " + dto.toString());

    return categoryBosService.putCategoryStd(dto);
  }

  /**
   * 표준카테고리 정렬 수정
   * @param CategoryStdRequestDto
   * @return CategoryStdResponseDto
   * @throws Exception
   */
  @ApiOperation(value = "표준카테고리정렬수정")
  @RequestMapping(value = "/admin/goods/category/putCategoryStdSort")
  public ApiResult<?> putCategoryStdSort(CategoryStdRequestDto dto) throws Exception{
    //log.debug("# ######################################");
    //log.debug("# CategoryController.putCategoryStdSort Start");
    //log.debug("# ######################################");
    //log.debug("# dto :: " + dto.toString());

    return categoryBosService.putCategoryStdSort(dto);
  }

  /**
   * 표준카테고리 삭제
   * @param CategoryStdRequestDto
   * @return CategoryStdResponseDto
   * @throws Exception
   */
  @ApiOperation(value = "표준카테고리상세삭제")
  @RequestMapping(value = "/admin/goods/category/delCategoryStd")
  public ApiResult<?> delCategoryStd(CategoryStdRequestDto dto) throws Exception{

    return categoryBosService.delCategoryStd(dto);
  }


}
