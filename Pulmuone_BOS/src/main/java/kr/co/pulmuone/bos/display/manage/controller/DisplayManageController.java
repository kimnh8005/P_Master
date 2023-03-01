package kr.co.pulmuone.bos.display.manage.controller;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import kr.co.pulmuone.bos.display.manage.service.DisplayManageBosService;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.enums.DisplayEnums;
import kr.co.pulmuone.v1.comm.enums.UserEnums;
import kr.co.pulmuone.v1.comm.exception.BaseException;
import kr.co.pulmuone.v1.comm.framework.view.ExcelDownloadView;
import kr.co.pulmuone.v1.comm.util.BindUtil;
import kr.co.pulmuone.v1.comm.util.StringUtil;
import kr.co.pulmuone.v1.display.manage.dto.DisplayContsResponseDto;
import kr.co.pulmuone.v1.display.manage.dto.DisplayInventoryResponseDto;
import kr.co.pulmuone.v1.display.manage.dto.DisplayManageRequestDto;
import kr.co.pulmuone.v1.display.manage.dto.DisplayPageResponseDto;
import kr.co.pulmuone.v1.display.manage.dto.vo.InventoryVo;
import kr.co.pulmuone.v1.goods.goods.dto.GoodsListRequestDto;
import lombok.extern.slf4j.Slf4j;

/**
* <PRE>
* Forbiz Korea
* 전시관리 BOS Controller
*
* </PRE>
*
* <PRE>
* <B>History:</B>
* =======================================================================
*  버전  :   작성일                :  작성자      :  작성내역
* -----------------------------------------------------------------------
*  1.0    2020.09.24.    dgyoun   최초작성
* =======================================================================
* </PRE>
*/

@Slf4j
@RestController
public class DisplayManageController {

  //@SuppressWarnings("unused")
  //@Autowired(required = true)
  //private HttpServletRequest request;

  @Resource
  private DisplayManageBosService displayManageBosService;

  @Autowired
  private ExcelDownloadView excelDownloadView; // 엑셀 다운로드 뷰

  // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
  // 전시페이지관리
  // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
  /**
   * 전시페이지 리스트조회
   * @param displayManageRequestDto
   * @return
   * @throws BaseException
   */
  @RequestMapping(value="/admin/display/manage/selectDpPageList")
  //@PostMapping(value = "/admin/display/manage/selectDpPageList")
  //@RequestMapping(value="/admin/display/manage/selectDpPageList", method= {RequestMethod.POST, RequestMethod.GET})
  @ApiOperation(value = "전시페이지리스트조회")
  @ApiResponses(value = { @ApiResponse(code = 900, message = "response data", response = DisplayPageResponseDto.class),
                          @ApiResponse(code = 901, message = ""
                              + "DISPLAY_MANAGE_PAGE_PARAM_NO_INPUT - 페이지 입력정보가 존재하지 않습니다. \n"
                              + "DISPLAY_MANAGE_PAGE_PARAM_NO_PAGE_ID - 페이지 ID가 존재하지 않습니다. \n"
                              + "DISPLAY_MANAGE_PAGE_LIST_NO_EXIST - 대상 페이지 목록이 존재하지 않습니다. \n"
                              + "DISPLAY_MANAGE_PAGE_LIST_NO_EXIST - 대상 페이지 목록이 존재하지 않습니다. "
                          )
  })
  @ResponseBody
  public ApiResult<?> selectDpPageList(DisplayManageRequestDto displayManageRequestDto) throws BaseException{
    log.info("# ######################################");
    log.info("# DisplayManageController.selectDpPageList Start");
    log.info("# ######################################");
    if (displayManageRequestDto != null) {
      log.debug("# In.displayManageRequestDto     :: " + displayManageRequestDto.toString());
    }
    else {
      log.debug("# In.displayManageRequestDto is Null");
    }

    // ========================================================================
    // # 초기화
    // ========================================================================
    // # 입력값 체크
    if (displayManageRequestDto == null) {
      // 입력정보가 존재하지 않습니다.
      return ApiResult.result(UserEnums.DisplayManage.DISPLAY_MANAGE_PAGE_PARAM_NO_INPUT);
    }
    if (StringUtil.isEmpty(displayManageRequestDto.getDpPageId())) {
      // 페이지ID가 존재하지 않습니다.
      return ApiResult.result(UserEnums.DisplayManage.DISPLAY_MANAGE_PAGE_PARAM_NO_PAGE_ID);
    }

    // ========================================================================
    // # 처리 및 반환
    // ========================================================================
    // # BOS 서비스 호출
    return displayManageBosService.selectDpPageList(displayManageRequestDto);

  }

  /**
   * 전시카테고리 리스트조회
   * @param displayManageRequestDto
   * @return
   * @throws BaseException
   */
  @RequestMapping(value = "/admin/display/manage/selectDpCategoryList")
  @ApiOperation(value = "전시카테고리리스트조회")
  @ApiResponses(value = { @ApiResponse(code = 900, message = "response data", response = DisplayPageResponseDto.class),
                          @ApiResponse(code = 901, message = ""
                              + "DISPLAY_MANAGE_PAGE_PARAM_NO_INPUT - 페이지 입력정보가 존재하지 않습니다. \n"
                              + "DISPLAY_MANAGE_PAGE_LIST_NO_EXIST - 대상 페이지 목록이 존재하지 않습니다."
                          )
  })
  @ResponseBody
  public ApiResult<?> selectDpCategoryList(DisplayManageRequestDto displayManageRequestDto) throws BaseException{
    log.debug("# ######################################");
    log.debug("# DisplayManageController.selectDpCategoryList Start");
    log.debug("# ######################################");
    if (displayManageRequestDto != null) {
      log.debug("# In.displayManageRequestDto     :: " + displayManageRequestDto.toString());
    }
    else {
      log.debug("# In.displayManageRequestDto is Null");
    }

    // ========================================================================
    // # 초기화
    // ========================================================================
    // # 입력값 체크
    if (displayManageRequestDto == null) {
      // 입력정보가 존재하지 않습니다.
      return ApiResult.result(UserEnums.DisplayManage.DISPLAY_MANAGE_PAGE_PARAM_NO_INPUT);
    }
    //if (StringUtil.isEmpty(displayManageRequestDto.getDepth())) {
    //  // DEPTH가 존재하지 않습니다.
    //  return ApiResult.result(UserEnums.DisplayManage.DISPLAY_MANAGE_PAGE_PARAM_NO_DEPTH);
    //}

    // ========================================================================
    // # 처리 및 반환
    // ========================================================================
    // # BOS 서비스 호출
    return displayManageBosService.selectDpCategoryList(displayManageRequestDto);

  }

  /**
   * 페이지 상세조회
   * @param displayManageRequestDto
   * @return
   * @throws BaseException
   */
  @RequestMapping(value = "/admin/display/manage/selectPageInfo")
  @ApiOperation(value = "페이지상세조회")
  @ApiResponses(value = { @ApiResponse(code = 900, message = "response data", response = DisplayPageResponseDto.class),
                          @ApiResponse(code = 901, message = ""
                              + "DISPLAY_MANAGE_PAGE_PARAM_NO_INPUT - 페이지 입력정보가 존재하지 않습니다. \n"
                              + "DISPLAY_MANAGE_PAGE_PARAM_NO_PAGE_ID - 페이지 ID가 존재하지 않습니다."
                          )
  })
  @ResponseBody
  public ApiResult<?> selectPageInfo(DisplayManageRequestDto displayManageRequestDto) throws BaseException{
    log.debug("# ######################################");
    log.debug("# DisplayManageController.selectPageInfo Start");
    log.debug("# ######################################");
    if (displayManageRequestDto != null) {
      log.debug("# In.displayManageRequestDto     :: " + displayManageRequestDto.toString());
    }
    else {
      log.debug("# In.displayManageRequestDto is Null");
    }

    // ========================================================================
    // # 초기화
    // ========================================================================
    // # 입력값 체크
    if (displayManageRequestDto == null) {
      // 입력정보가 존재하지 않습니다.
      return ApiResult.result(UserEnums.DisplayManage.DISPLAY_MANAGE_PAGE_PARAM_NO_INPUT);
    }
    if (StringUtil.isEmpty(displayManageRequestDto.getDpPageId())) {
      // 페이지ID가 존재하지 않습니다.
      return ApiResult.result(UserEnums.DisplayManage.DISPLAY_MANAGE_PAGE_PARAM_NO_PAGE_ID);
    }

    // ========================================================================
    // # 처리 및 반환
    // ========================================================================
    // # BOS 서비스 호출
    return displayManageBosService.selectPageInfo(displayManageRequestDto);

  }

  /**
   * 페이지 수정
   * @param displayManageRequestDto
   * @return
   * @throws BaseException
   */
  @RequestMapping(value = "/admin/display/manage/putPage")
  @ApiOperation(value = "페이지수정")
  @ApiResponses(value = { @ApiResponse(code = 900, message = "response data", response = DisplayPageResponseDto.class),
                          @ApiResponse(code = 901, message = ""
                              + "DISPLAY_MANAGE_PAGE_PARAM_NO_INPUT - 페이지 입력정보가 존재하지 않습니다. \n"
                              + "DISPLAY_MANAGE_PAGE_PARAM_NO_PAGE_ID - 페이지 ID가 존재하지 않습니다."
                          )
  })
  @ResponseBody
  public ApiResult<?> putPage(DisplayManageRequestDto displayManageRequestDto) throws BaseException{
    log.debug("# ######################################");
    log.debug("# DisplayManageController.putPage Start");
    log.debug("# ######################################");
    if (displayManageRequestDto != null) {
      log.debug("# In.displayManageRequestDto     :: " + displayManageRequestDto.toString());
    }
    else {
      log.debug("# In.displayManageRequestDto is Null");
    }

    // ========================================================================
    // # 초기화
    // ========================================================================
    // # 입력값 체크
    if (displayManageRequestDto == null) {
      // 입력정보가 존재하지 않습니다.
      return ApiResult.result(UserEnums.DisplayManage.DISPLAY_MANAGE_PAGE_PARAM_NO_INPUT);
    }
    // PageVo 객체 Set
    displayManageRequestDto.convertPageDataObject();

    //if (StringUtil.isEmpty(displayManageRequestDto.getDpPageId())) {
    if (StringUtil.isEmpty(displayManageRequestDto.getPageInfo().getDpPageId())) {
      // 페이지ID가 존재하지 않습니다.
      return ApiResult.result(UserEnums.DisplayManage.DISPLAY_MANAGE_PAGE_PARAM_NO_PAGE_ID);
    }

    // ========================================================================
    // # 처리 및 반환
    // ========================================================================
    // # BOS 서비스 호출
    return displayManageBosService.putPage(displayManageRequestDto);

  }

  /**
   * 페이지 순서변경
   * @param displayManageRequestDto
   * @return
   * @throws BaseException
   */
  @RequestMapping(value = "/admin/display/manage/putPageSort")
  @ApiOperation(value = "페이지순서변경")
  @ApiResponses(value = { @ApiResponse(code = 900, message = "response data", response = DisplayPageResponseDto.class),
                          @ApiResponse(code = 901, message = ""
                              + "DISPLAY_MANAGE_PAGE_PARAM_NO_INPUT - 페이지 입력정보가 존재하지 않습니다. \n"
                              + "DISPLAY_MANAGE_PAGE_PARAM_NO_SORT_TARGET - 페이지 순서변경 대상 목록이 존재하지 않습니다. \n"
                              + "DISPLAY_MANAGE_PAGE_FAIL_PUT_SORT_NO_TARGET - 페이지 순번 변경 대상이 없습니다."
                          )
  })
  @ResponseBody
  public ApiResult<?> putPageSort(DisplayManageRequestDto displayManageRequestDto) throws BaseException{
    log.debug("# ######################################");
    log.debug("# DisplayManageController.putPageSort Start");
    log.debug("# ######################################");
    if (displayManageRequestDto != null) {
      log.debug("# In.displayManageRequestDto     :: " + displayManageRequestDto.toString());
    }
    else {
      log.debug("# In.displayManageRequestDto is Null");
    }

    // ========================================================================
    // # 초기화
    // ========================================================================
    // # 입력값 체크
    if (displayManageRequestDto == null) {
      // 입력정보가 존재하지 않습니다.
      return ApiResult.result(UserEnums.DisplayManage.DISPLAY_MANAGE_PAGE_PARAM_NO_INPUT);
    }
    if (displayManageRequestDto.getPageList()== null || displayManageRequestDto.getPageList().size() <= 0) {
      // 순서변경 대상이 존재하지 않습니다.
      return ApiResult.result(UserEnums.DisplayManage.DISPLAY_MANAGE_PAGE_PARAM_NO_SORT_TARGET);
    }

    // ========================================================================
    // # 처리 및 반환
    // ========================================================================
    // # BOS 서비스 호출
    return displayManageBosService.putPageSort(displayManageRequestDto);

  }

  /**
   * 페이지 삭제
   * @param displayManageRequestDto
   * @return
   * @throws BaseException
   */
  @RequestMapping(value = "/admin/display/manage/delPage")
  @ApiOperation(value = "페이지삭제")
  @ApiResponses(value = { @ApiResponse(code = 900, message = "response data", response = DisplayPageResponseDto.class),
                          @ApiResponse(code = 901, message = ""
                              + "DISPLAY_MANAGE_PAGE_PARAM_NO_INPUT - 페이지 입력정보가 존재하지 않습니다. \n"
                              + "DISPLAY_MANAGE_PAGE_PARAM_NO_PAGE_ID - 페이지 ID가 존재하지 않습니다. \n"
                              + "DISPLAY_MANAGE_PAGE_FAIL_DEL_NO_TARGET - 페이지 삭제 대상이 없습니다. \n"
                              + "DISPLAY_MANAGE_PAGE_FAIL_DEL - 페이지 삭제 처리 중 실패하였습니다. "
                          )
  })
  @ResponseBody
  public ApiResult<?> delPage(DisplayManageRequestDto displayManageRequestDto) throws BaseException{
    log.debug("# ######################################");
    log.debug("# DisplayManageController.delPage Start");
    log.debug("# ######################################");
    if (displayManageRequestDto != null) {
      log.debug("# In.displayManageRequestDto     :: " + displayManageRequestDto.toString());
    }
    else {
      log.debug("# In.displayManageRequestDto is Null");
    }

    // ========================================================================
    // # 초기화
    // ========================================================================
    // # 입력값 체크
    if (displayManageRequestDto == null) {
      // 입력정보가 존재하지 않습니다.
      return ApiResult.result(UserEnums.DisplayManage.DISPLAY_MANAGE_PAGE_PARAM_NO_INPUT);
    }

    // PageVo 객체 Set
    displayManageRequestDto.convertPageDataObject();

    //if (StringUtil.isEmpty(displayManageRequestDto.getDpPageId())) {
    if (StringUtil.isEmpty(displayManageRequestDto.getPageInfo().getDpPageId())) {
      // 페이지ID가 존재하지 않습니다.
      return ApiResult.result(UserEnums.DisplayManage.DISPLAY_MANAGE_PAGE_PARAM_NO_PAGE_ID);
    }

    // ========================================================================
    // # 처리 및 반환
    // ========================================================================
    // # BOS 서비스 호출
    return displayManageBosService.delPage(displayManageRequestDto);

  }

  /**
   * 페이지 등록
   * @param displayManageRequestDto
   * @return
   * @throws BaseException
   */
  @RequestMapping(value = "/admin/display/manage/addPage")
  @ApiOperation(value = "페이지등록")
  @ApiResponses(value = { @ApiResponse(code = 900, message = "response data", response = DisplayPageResponseDto.class),
                          @ApiResponse(code = 901, message = ""
                              + "DISPLAY_MANAGE_PAGE_PARAM_NO_INPUT - 페이지 입력정보가 존재하지 않습니다. \n"
                              + "DISPLAY_MANAGE_PAGE_PARAM_NO_REG_TARGET - 페이지 ID가 존재하지 않습니다. \n"
                              + "DISPLAY_MANAGE_PAGE_FAIL_ADD_NO_PROC - 페이지 등록 중 실패하였습니다. \n"
                              + "DISPLAY_MANAGE_PAGE_FAIL_ADD - 페이지 등록 처리에 실패하였습니다. "
                          )
  })
  @ResponseBody
  public ApiResult<?> addPage(DisplayManageRequestDto displayManageRequestDto) throws BaseException{
    log.debug("# ######################################");
    log.debug("# DisplayManageController.addPage Start");
    log.debug("# ######################################");
    if (displayManageRequestDto != null) {
      log.debug("# In.displayManageRequestDto     :: " + displayManageRequestDto.toString());
    }
    else {
      log.debug("# In.displayManageRequestDto is Null");
    }

    // ========================================================================
    // # 초기화
    // ========================================================================
    // # 입력값 체크
    if (displayManageRequestDto == null) {
      // 입력정보가 존재하지 않습니다.
      return ApiResult.result(UserEnums.DisplayManage.DISPLAY_MANAGE_PAGE_PARAM_NO_INPUT);
    }

    // PageVo 객체 Set
    displayManageRequestDto.convertPageDataObject();
    // PageVo 객체 Set 하드코딩
    //PageVo pageInfo = new PageVo(SessionUtil.getBosUserVO());
    //pageInfo.setSort(StringUtil.nvlInt(displayManageRequestDto.getSort()));
    //pageInfo.setPageCd(displayManageRequestDto.getPageCd());
    //pageInfo.setPageNm(displayManageRequestDto.getPageNm());
    //pageInfo.setUseYn(displayManageRequestDto.getUseYn());
    //pageInfo.setPrntsPageId(displayManageRequestDto.getPrntsPageId());
    //pageInfo.setDepth(StringUtil.nvlInt(displayManageRequestDto.getDepth()));
    //displayManageRequestDto.setPageInfo(pageInfo);

    if (displayManageRequestDto.getPageInfo() == null) {
      // 페이지 등록 대상정보가 존재하지 않습니다.
      return ApiResult.result(UserEnums.DisplayManage.DISPLAY_MANAGE_PAGE_PARAM_NO_REG_TARGET);
    }
    else {
      log.debug("# getPageInfo :: " + displayManageRequestDto.getPageInfo().toString());
    }

    // ========================================================================
    // # 처리 및 반환
    // ========================================================================
    // # BOS 서비스 호출
    return displayManageBosService.addPage(displayManageRequestDto);

  }


  // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
  // 전시인벤토리관리
  // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
  /**
   * 인벤토리리스트 엑셀다운로드
   * @param displayManageRequestDto
   * @return
   * @throws BaseException
   */
  @RequestMapping(value = "/admin/display/manage/getExportExcelInventoryList")
  @ApiOperation(value = "인벤토리리스트엑셀다운로드")
  //public ModelAndView getExportExcelInventoryList(@RequestBody DisplayManageRequestDto displayManageRequestDto) throws BaseException {
  public ModelAndView getExportExcelInventoryList(DisplayManageRequestDto displayManageRequestDto, HttpServletRequest request, HttpServletResponse response) throws BaseException {
    log.debug("# ######################################");
    log.debug("# DisplayManageController.getExportExcelInventoryList Start");
    log.debug("# ######################################");
    if (displayManageRequestDto != null) {
      log.debug("# In.displayManageRequestDto     :: " + displayManageRequestDto.toString());
    }
    else {
      log.debug("# In.displayManageRequestDto is Null");
    }

    // ========================================================================
    // # 초기화
    // ========================================================================
    // # 입력값 체크
    if (displayManageRequestDto == null) {
      // 입력정보가 존재하지 않습니다.
      log.debug("# 입력정보 미존재");
      return new ModelAndView(excelDownloadView);
    }

    String pageTp = displayManageRequestDto.getPageTp();
    log.debug("# pageTp :: " + pageTp);

    if (StringUtil.isEmpty(pageTp)) {
      log.debug("# 페이지유형 is Null");
      return new ModelAndView(excelDownloadView);
    }

    // ========================================================================
    // # 처리 및 반환
    // ========================================================================
    displayManageRequestDto.setExcelDownYn("Y");
    return displayManageBosService.selectInventoryListForExcelExport(displayManageRequestDto);
  }

  /**
   * 인벤토리 리스트조회
   * @param displayManageRequestDto
   * @return
   * @throws BaseException
   */
  @RequestMapping(value = "/admin/display/manage/selectInventoryList")
  @ApiOperation(value = "인벤토리리스트조회")
  @ApiResponses(value = { @ApiResponse(code = 900, message = "response data", response = DisplayInventoryResponseDto.class),
                          @ApiResponse(code = 901, message = ""
                              + "DISPLAY_MANAGE_INVENTORY_PARAM_NO_INPUT - 인벤토리 입력정보가 존재하지 않습니다. \n"
                              + "DISPLAY_MANAGE_INVENTORY_PARAM_NO_PAGE_TP - 인벤토리 페이지유형이 존재하지 않습니다. \n"
                              + "DISPLAY_MANAGE_INVENTORY_LIST_NO_EXIST - 대상 인벤토리 목록이 존재하지 않습니다."
                          )
  })
  @ResponseBody
  public ApiResult<?> selectInventoryList(DisplayManageRequestDto displayManageRequestDto) throws BaseException{
    log.debug("# ######################################");
    log.debug("# DisplayManageController.selectInventoryList Start");
    log.debug("# ######################################");
    if (displayManageRequestDto != null) {
      log.debug("# In.displayManageRequestDto     :: " + displayManageRequestDto.toString());
    }
    else {
      log.debug("# In.displayManageRequestDto is Null");
    }

    // ========================================================================
    // # 초기화
    // ========================================================================
    // # 입력값 체크
    if (displayManageRequestDto == null) {
      // 입력정보가 존재하지 않습니다.
      return ApiResult.result(UserEnums.DisplayManage.DISPLAY_MANAGE_INVENTORY_PARAM_NO_INPUT);
    }
    if (StringUtil.isEmpty(displayManageRequestDto.getPageTp())) {
      // 인벤토리 페이지유형이 존재하지 않습니다.
      return ApiResult.result(UserEnums.DisplayManage.DISPLAY_MANAGE_INVENTORY_PARAM_NO_PAGE_TP);
    }

    // ========================================================================
    // # 처리 및 반환
    // ========================================================================
    // # BOS 서비스 호출
    return displayManageBosService.selectInventoryList(displayManageRequestDto);

  }

  /**
   * 인벤토리 상세조회
   * @param displayManageRequestDto
   * @return
   * @throws BaseException
   */
  @RequestMapping(value = "/admin/display/manage/selectInventoryInfo")
  @ApiOperation(value = "인벤토리상세조회")
  @ApiResponses(value = { @ApiResponse(code = 900, message = "response data", response = DisplayInventoryResponseDto.class),
                          @ApiResponse(code = 901, message = ""
                              + "DISPLAY_MANAGE_INVENTORY_PARAM_NO_INPUT - 인벤토리 입력정보가 존재하지 않습니다. \n"
                              + "DISPLAY_MANAGE_INVENTORY_PARAM_NO_INVENTORY_ID - 인벤토리 ID가 존재하지 않습니다."
                          )
  })
  @ResponseBody
  public ApiResult<?> selectInventoryInfo(DisplayManageRequestDto displayManageRequestDto) throws BaseException{
    log.debug("# ######################################");
    log.debug("# DisplayManageController.selectInventoryInfo Start");
    log.debug("# ######################################");
    if (displayManageRequestDto != null) {
      log.debug("# In.displayManageRequestDto     :: " + displayManageRequestDto.toString());
    }
    else {
      log.debug("# In.displayManageRequestDto is Null");
    }

    // ========================================================================
    // # 초기화
    // ========================================================================
    // # 입력값 체크
    if (displayManageRequestDto == null) {
      // 입력정보가 존재하지 않습니다.
      return ApiResult.result(UserEnums.DisplayManage.DISPLAY_MANAGE_INVENTORY_PARAM_NO_INPUT);
    }
    if (StringUtil.isEmpty(displayManageRequestDto.getDpInventoryId())) {
      // 인벤토리 ID가 존재하지 않습니다.
      return ApiResult.result(UserEnums.DisplayManage.DISPLAY_MANAGE_INVENTORY_PARAM_NO_INVENTORY_ID);
    }

    // ========================================================================
    // # 처리 및 반환
    // ========================================================================
    // # BOS 서비스 호출
    return displayManageBosService.selectInventoryInfo(displayManageRequestDto);

  }

  /**
   * 인벤토리 수정
   * @param displayManageRequestDto
   * @return
   * @throws BaseException
   */
  @RequestMapping(value = "/admin/display/manage/putInventory")
  @ApiOperation(value = "인벤토리수정")
  @ApiResponses(value = { @ApiResponse(code = 900, message = "response data", response = DisplayInventoryResponseDto.class),
                          @ApiResponse(code = 901, message = ""
                              + "DISPLAY_MANAGE_INVENTORY_PARAM_NO_INPUT - 인벤토리 입력정보가 존재하지 않습니다. \n"
                              + "DISPLAY_MANAGE_INVENTORY_PARAM_NO_INVENTORY_ID - 인벤토리 ID가 존재하지 않습니다."
                          )
  })
  @ResponseBody
  public ApiResult<?> putInventory(DisplayManageRequestDto displayManageRequestDto) throws BaseException{
    log.debug("# ######################################");
    log.debug("# DisplayManageController.putInventory Start");
    log.debug("# ######################################");
    if (displayManageRequestDto != null) {
      log.debug("# In.displayManageRequestDto     :: " + displayManageRequestDto.toString());
    }
    else {
      log.debug("# In.displayManageRequestDto is Null");
    }

    // ========================================================================
    // # 초기화
    // ========================================================================
    // # 입력값 체크
    if (displayManageRequestDto == null) {
      // 입력정보가 존재하지 않습니다.
      return ApiResult.result(UserEnums.DisplayManage.DISPLAY_MANAGE_INVENTORY_PARAM_NO_INPUT);
    }

    // InventoryVo 객체 Set
    displayManageRequestDto.convertInventoryDataObject();

    if (StringUtil.isEmpty(displayManageRequestDto.getInventoryInfo().getDpInventoryId())) {
      // 인벤토리 ID가 존재하지 않습니다.
      return ApiResult.result(UserEnums.DisplayManage.DISPLAY_MANAGE_INVENTORY_PARAM_NO_INVENTORY_ID);
    }

    // ========================================================================
    // # 처리 및 반환
    // ========================================================================
    // # BOS 서비스 호출
    return displayManageBosService.putInventory(displayManageRequestDto);

  }

  /**
   * 인벤토리 순서변경
   * @param displayManageRequestDto
   * @return
   * @throws BaseException
   */
  @RequestMapping(value = "/admin/display/manage/putInventorySort")
  @ApiOperation(value = "인벤토리순서변경")
  @ApiResponses(value = { @ApiResponse(code = 900, message = "response data", response = DisplayInventoryResponseDto.class),
                          @ApiResponse(code = 901, message = ""
                              + "DISPLAY_MANAGE_INVENTORY_PARAM_NO_INPUT - 인벤토리 입력정보가 존재하지 않습니다. \n"
                              + "DISPLAY_MANAGE_INVENTORY_PARAM_NO_SORT_TARGET - 인벤토리 순서변경 대상 목록이 존재하지 않습니다. \n"
                              + "DISPLAY_MANAGE_INVENTORY_FAIL_PUT_SORT_NO_TARGET - 인벤토리 순번 변경 대상이 없습니다. \n"
                              + "DISPLAY_MANAGE_INVENTORY_FAIL_PUT_SORT_NO_PROC - 인벤토리 순번 변경 중 실패하였습니다. \n"
                              + "DISPLAY_MANAGE_INVENTORY_FAIL_PUT_SORT - 인벤토리 순번 변경 처리에 실패하였습니다."
                          )
  })
  @ResponseBody
  public ApiResult<?> putInventorySort(DisplayManageRequestDto displayManageRequestDto) throws BaseException{
    log.debug("# ######################################");
    log.debug("# DisplayManageController.putInventorySort Start");
    log.debug("# ######################################");
    if (displayManageRequestDto != null) {
      log.debug("# In.displayManageRequestDto     :: " + displayManageRequestDto.toString());
    }
    else {
      log.debug("# In.displayManageRequestDto is Null");
    }

    // ========================================================================
    // # 초기화
    // ========================================================================
    // # 입력값 체크
    if (displayManageRequestDto == null) {
      // 입력정보가 존재하지 않습니다.
      return ApiResult.result(UserEnums.DisplayManage.DISPLAY_MANAGE_INVENTORY_PARAM_NO_INPUT);
    }

    // # JsonData 변환
    displayManageRequestDto.convertDataList();

    if (displayManageRequestDto.getInventoryList()== null || displayManageRequestDto.getInventoryList().size() <= 0) {
      // 순서변경 대상이 존재하지 않습니다.
      return ApiResult.result(UserEnums.DisplayManage.DISPLAY_MANAGE_INVENTORY_PARAM_NO_SORT_TARGET);
    }

    // ========================================================================
    // # 처리 및 반환
    // ========================================================================
    // # BOS 서비스 호출
    return displayManageBosService.putInventorySort(displayManageRequestDto);

  }

  /**
   * 인벤토리 삭제
   * @param displayManageRequestDto
   * @return
   * @throws BaseException
   */
  @RequestMapping(value = "/admin/display/manage/delInventory")
  @ApiOperation(value = "인벤토리삭제")
  @ApiResponses(value = { @ApiResponse(code = 900, message = "response data", response = DisplayInventoryResponseDto.class),
                          @ApiResponse(code = 901, message = ""
                              + "DISPLAY_MANAGE_INVENTORY_PARAM_NO_INPUT - 인벤토리 입력정보가 존재하지 않습니다. \n"
                              + "DISPLAY_MANAGE_INVENTORY_PARAM_NO_INVENTORY_ID - 인벤토리 ID가 존재하지 않습니다. \n"
                              + "DISPLAY_MANAGE_INVENTORY_FAIL_DEL_NO_TARGET - 인벤토리 삭제 대상이 없습니다. \n"
                              + "DISPLAY_MANAGE_INVENTORY_FAIL_DEL_NO_PROC - 인벤토리 삭제 처리 중 실패하였습니다. \n"
                              + "DISPLAY_MANAGE_INVENTORY_FAIL_DEL - 인벤토리 삭제 처리에 실패하였습니다."
                          )
  })
  @ResponseBody
  public ApiResult<?> delInventory(DisplayManageRequestDto displayManageRequestDto) throws BaseException{
    log.debug("# ######################################");
    log.debug("# DisplayManageController.delInventory Start");
    log.debug("# ######################################");
    if (displayManageRequestDto != null) {
      log.debug("# In.displayManageRequestDto     :: " + displayManageRequestDto.toString());
    }
    else {
      log.debug("# In.displayManageRequestDto is Null");
    }

    // ========================================================================
    // # 초기화
    // ========================================================================
    // # 입력값 체크
    if (displayManageRequestDto == null) {
      // 입력정보가 존재하지 않습니다.
      return ApiResult.result(UserEnums.DisplayManage.DISPLAY_MANAGE_INVENTORY_PARAM_NO_INPUT);
    }

    // InventoryVo 객체 Set
    displayManageRequestDto.convertInventoryDataObject();

    if (StringUtil.isEmpty(displayManageRequestDto.getInventoryInfo().getDpInventoryId())) {
      // 인벤토리 ID가 존재하지 않습니다.
      return ApiResult.result(UserEnums.DisplayManage.DISPLAY_MANAGE_INVENTORY_PARAM_NO_INVENTORY_ID);
    }

    // ========================================================================
    // # 처리 및 반환
    // ========================================================================
    // 단건씩 삭제하지만 추후 확장을 위해서 List 구조로 Set
    InventoryVo inventoryVo = new InventoryVo();
    inventoryVo.setDpInventoryId(displayManageRequestDto.getInventoryInfo().getDpInventoryId());
    inventoryVo.setDelYn("Y");
    List<InventoryVo> inventoryList = new ArrayList<InventoryVo>();
    inventoryList.add(inventoryVo);
    displayManageRequestDto.setInventoryList(inventoryList);

    log.debug("# In.displayManageRequestDto.inventoryList :: " + displayManageRequestDto.getInventoryList().toString());

    // # BOS 서비스 호출
    return displayManageBosService.delInventory(displayManageRequestDto);

  }

  /**
   * 인벤토리 등록
   * @param displayManageRequestDto
   * @return
   * @throws BaseException
   */
  @RequestMapping(value = "/admin/display/manage/addInventory")
  @ApiOperation(value = "인벤토리등록")
  @ApiResponses(value = { @ApiResponse(code = 900, message = "response data", response = DisplayInventoryResponseDto.class),
                          @ApiResponse(code = 901, message = ""
                              + "DISPLAY_MANAGE_INVENTORY_PARAM_NO_INPUT - 인벤토리 입력정보가 존재하지 않습니다. \n"
                              + "DISPLAY_MANAGE_INVENTORY_PARAM_NO_REG_TARGET - 인벤토리 등록 대상정보가 존재하지 않습니다. \n"
                              + "DISPLAY_MANAGE_INVENTORY_FAIL_ADD_NO_PROC - 인벤토리 등록 중 실패하였습니다. \n"
                              + "DISPLAY_MANAGE_INVENTORY_FAIL_ADD - 인벤토리 등록 처리에 실패하였습니다."
                          )
  })
  @ResponseBody
  public ApiResult<?> addInventory(DisplayManageRequestDto displayManageRequestDto) throws BaseException{
    log.debug("# ######################################");
    log.debug("# DisplayManageController.addInventory Start");
    log.debug("# ######################################");
    if (displayManageRequestDto != null) {
      log.debug("# In.displayManageRequestDto     :: " + displayManageRequestDto.toString());
    }
    else {
      log.debug("# In.displayManageRequestDto is Null");
    }

    // ========================================================================
    // # 초기화
    // ========================================================================
    // # 입력값 체크
    if (displayManageRequestDto == null) {
      // 입력정보가 존재하지 않습니다.
      return ApiResult.result(UserEnums.DisplayManage.DISPLAY_MANAGE_INVENTORY_PARAM_NO_INPUT);
    }

    // InventoryVo 객체 Set
    displayManageRequestDto.convertInventoryDataObject();

    if (displayManageRequestDto.getInventoryInfo() == null) {
      // 인벤토리 등록 대상정보가 존재하지 않습니다.
      return ApiResult.result(UserEnums.DisplayManage.DISPLAY_MANAGE_INVENTORY_PARAM_NO_REG_TARGET);
    }

    // ========================================================================
    // # 처리 및 반환
    // ========================================================================
    // # BOS 서비스 호출
    return displayManageBosService.addInventory(displayManageRequestDto);

  }


  // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
  // 전시컨텐츠관리
  // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
  /**
   * 컨텐츠리스트 엑셀다운로드
   * @param displayManageRequestDto
   * @return
   * @throws BaseException
   */
  @RequestMapping(value = "/admin/display/manage/getExportExcelContsList")
  @ApiOperation(value = "컨텐츠리스트엑셀다운로드")
  //public ModelAndView getExportExcelContsList(@RequestBody DisplayManageRequestDto displayManageRequestDto, HttpServletRequest request, HttpServletResponse response) throws BaseException {
  public ModelAndView getExportExcelContsList(DisplayManageRequestDto displayManageRequestDto, HttpServletRequest request, HttpServletResponse response) throws BaseException {
    log.debug("# ######################################");
    log.debug("# DisplayManageController.getExportExcelContsList Start");
    log.debug("# ######################################");
    if (displayManageRequestDto != null) {
      log.debug("# In.displayManageRequestDto     :: " + displayManageRequestDto.toString());
    }
    else {
      log.debug("# In.displayManageRequestDto is Null");
    }

    // ========================================================================
    // # 초기화
    // ========================================================================
    // # 입력값 체크
    if (displayManageRequestDto == null) {
      // 입력정보가 존재하지 않습니다.
      log.debug("# 입력정보 미존재");
      return new ModelAndView(excelDownloadView);
    }

    String contsTp = displayManageRequestDto.getContsTp();
    log.debug("# contsTp :: " + contsTp);

    if (StringUtil.isEmpty(contsTp)) {
      log.debug("# 컨텐츠유형 is Null");
      return new ModelAndView(excelDownloadView);
    }

    // ========================================================================
    // # 처리 및 반환
    // ========================================================================
    displayManageRequestDto.setExcelDownYn("Y");
    return displayManageBosService.selectContsListForExcelExport(displayManageRequestDto);
  }

  /**
   * 전시컨텐츠 리스트조회
   * @param displayManageRequestDto
   * @return
   * @throws BaseException
   */
  @RequestMapping(value = "/admin/display/manage/selectDpContsList")
  @ApiOperation(value = "전시컨텐츠리스트조회")
  @ApiResponses(value = { @ApiResponse(code = 900, message = "response data", response = DisplayContsResponseDto.class),
                          @ApiResponse(code = 901, message = ""
                              + "DISPLAY_MANAGE_CONTS_PARAM_NO_INPUT - 컨텐츠 입력정보가 존재하지 않습니다. \n"
                              + "DISPLAY_MANAGE_CONTS_PARAM_NO_INVENTORY_ID - 컨텐츠 인벤토리ID가  존재하지 않습니다. \n"
                              + "DISPLAY_MANAGE_CONTS_PARAM_NO_PRNTS_CONTS_ID - 컨텐츠 상위 컨텐츠ID가  존재하지 않습니다. \n"
                              + "DISPLAY_MANAGE_CONTS_LIST_NO_EXIST - 대상 컨텐츠 목록이 존재하지 않습니다."
                          )
  })
  @ResponseBody
  public ApiResult<?> selectDpContsList(DisplayManageRequestDto displayManageRequestDto) throws BaseException{
    log.debug("# ######################################");
    log.debug("# DisplayManageController.selectDpContsList Start");
    log.debug("# ######################################");
    if (displayManageRequestDto != null) {
      log.debug("# In.displayManageRequestDto     :: " + displayManageRequestDto.toString());
    }
    else {
      log.debug("# In.displayManageRequestDto is Null");
    }

    // ========================================================================
    // # 초기화
    // ========================================================================
    // # 입력값 체크
    if (displayManageRequestDto == null) {
      // 입력정보가 존재하지 않습니다.
      return ApiResult.result(UserEnums.DisplayManage.DISPLAY_MANAGE_CONTS_PARAM_NO_INPUT);
    }
    if (StringUtil.isEmpty(displayManageRequestDto.getDpInventoryId())) {
      // 컨텐츠 인벤토리ID가  존재하지 않습니다.
      return ApiResult.result(UserEnums.DisplayManage.DISPLAY_MANAGE_CONTS_PARAM_NO_INVENTORY_ID);
    }
    if (StringUtil.isEmpty(displayManageRequestDto.getPrntsContsId())) {
      // 컨텐츠 상위 컨텐츠ID가  존재하지 않습니다.
      return ApiResult.result(UserEnums.DisplayManage.DISPLAY_MANAGE_CONTS_PARAM_NO_PRNTS_CONTS_ID);
    }


    // ========================================================================
    // # 처리 및 반환
    // ========================================================================
    // # BOS 서비스 호출
    return displayManageBosService.selectDpContsList(displayManageRequestDto);

  }

  /**
   * 컨텐츠 상세조회
   * @param displayManageRequestDto
   * @return
   * @throws BaseException
   */
  @RequestMapping(value = "/admin/display/manage/selectDpContsInfo")
  @ApiOperation(value = "컨텐츠상세조회")
  @ApiResponses(value = { @ApiResponse(code = 900, message = "response data", response = DisplayContsResponseDto.class),
                          @ApiResponse(code = 901, message = ""
                              + "DISPLAY_MANAGE_CONTS_PARAM_NO_INPUT - 컨텐츠 입력정보가 존재하지 않습니다. \n"
                              + "DISPLAY_MANAGE_CONTS_PARAM_NO_CONTS_ID - 컨텐츠 ID가 존재하지 않습니다. \n"
                              + "DISPLAY_MANAGE_CONTS_NO_EXIST - 대상 컨텐츠가 존재하지 않습니다. "
                          )
  })
  @ResponseBody
  public ApiResult<?> selectDpContsInfo(DisplayManageRequestDto displayManageRequestDto) throws BaseException{
    log.debug("# ######################################");
    log.debug("# DisplayManageController.selectDpContsInfo Start");
    log.debug("# ######################################");
    if (displayManageRequestDto != null) {
      log.debug("# In.displayManageRequestDto     :: " + displayManageRequestDto.toString());
    }
    else {
      log.debug("# In.displayManageRequestDto is Null");
    }

    // ========================================================================
    // # 초기화
    // ========================================================================
    // # 입력값 체크
    if (displayManageRequestDto == null) {
      // 입력정보가 존재하지 않습니다.
      return ApiResult.result(UserEnums.DisplayManage.DISPLAY_MANAGE_CONTS_PARAM_NO_INPUT);
    }
    if (StringUtil.isEmpty(displayManageRequestDto.getDpContsId())) {
      // 컨텐츠 ID가 존재하지 않습니다.
      return ApiResult.result(UserEnums.DisplayManage.DISPLAY_MANAGE_CONTS_PARAM_NO_CONTS_ID);
    }

    // ========================================================================
    // # 처리 및 반환
    // ========================================================================
    // # BOS 서비스 호출
    return displayManageBosService.selectDpContsInfo(displayManageRequestDto);

  }

  /**
   * 컨텐츠 수정
   * @param displayManageRequestDto
   * @return
   * @throws BaseException
   */
  @RequestMapping(value = "/admin/display/manage/putConts")
  @ApiOperation(value = "컨텐츠수정")
  @ApiResponses(value = { @ApiResponse(code = 900, message = "response data", response = DisplayContsResponseDto.class),
                          @ApiResponse(code = 901, message = ""
                              + "DISPLAY_MANAGE_CONTS_PARAM_NO_INPUT - 컨텐츠 입력정보가 존재하지 않습니다. \n"
                              + "DISPLAY_MANAGE_CONTS_PARAM_NO_CONTS_ID - 컨텐츠 ID가 존재하지 않습니다. \n"
                              + "DISPLAY_MANAGE_CONTS_FAIL_PUT_NO_PROC - 컨텐츠 정보 수정 처리건이 없습니다. \n"
                              + "DISPLAY_MANAGE_CONTS_FAIL_PUT - 컨텐츠 정보 수정 처리에 실패하였습니다. "
                          )
  })
  @ResponseBody
  public ApiResult<?> putConts(DisplayManageRequestDto displayManageRequestDto) throws BaseException{
    log.debug("# ######################################");
    log.debug("# DisplayManageController.putConts Start");
    log.debug("# ######################################");
    if (displayManageRequestDto != null) {
      log.debug("# In.displayManageRequestDto     :: " + displayManageRequestDto.toString());
    }
    else {
      log.debug("# In.displayManageRequestDto is Null");
    }

    // ========================================================================
    // # 초기화
    // ========================================================================
    // # 입력값 체크
    if (displayManageRequestDto == null) {
      // 입력정보가 존재하지 않습니다.
      return ApiResult.result(UserEnums.DisplayManage.DISPLAY_MANAGE_CONTS_PARAM_NO_INPUT);
    }

    // ContsVo 객체 Set
    displayManageRequestDto.convertContsDataObject();

    if (displayManageRequestDto.getContsInfo() == null || StringUtil.isEmpty(displayManageRequestDto.getContsInfo().getDpContsId())) {
      // 컨텐츠 ID가 존재하지 않습니다.
      return ApiResult.result(UserEnums.DisplayManage.DISPLAY_MANAGE_CONTS_PARAM_NO_CONTS_ID);
    }

    // ========================================================================
    // # 처리 및 반환
    // ========================================================================
    // # BOS 서비스 호출
    return displayManageBosService.putConts(displayManageRequestDto);

  }

  /**
   * 컨텐츠 순서변경
   * @param displayManageRequestDto
   * @return
   * @throws BaseException
   */
  @RequestMapping(value = "/admin/display/manage/putContsSort")
  @ApiOperation(value = "컨텐츠순서변경")
  @ApiResponses(value = { @ApiResponse(code = 900, message = "response data", response = DisplayContsResponseDto.class),
                          @ApiResponse(code = 901, message = ""
                              + "DISPLAY_MANAGE_CONTS_PARAM_NO_INPUT - 컨텐츠 입력정보가 존재하지 않습니다. \n"
                              + "DISPLAY_MANAGE_CONTS_PARAM_NO_SORT_TARGET - 컨텐츠 순서변경 대상 목록이 존재하지 않습니다. \n"
                              + "DISPLAY_MANAGE_CONTS_FAIL_PUT_SORT_NO_TARGET - 컨텐츠 순번 변경 대상이 없습니다. \n"
                              + "DISPLAY_MANAGE_CONTS_FAIL_PUT_SORT_NO_PROC - 컨텐츠 순번 변경 중 실패하였습니다. "
                          )
  })
  @ResponseBody
  public ApiResult<?> putContsSort(DisplayManageRequestDto displayManageRequestDto) throws BaseException{
    log.debug("# ######################################");
    log.debug("# DisplayManageController.putContsSort Start");
    log.debug("# ######################################");
    if (displayManageRequestDto != null) {
      log.debug("# In.displayManageRequestDto     :: " + displayManageRequestDto.toString());
    }
    else {
      log.debug("# In.displayManageRequestDto is Null");
    }

    // ========================================================================
    // # 초기화
    // ========================================================================
    // # 입력값 체크
    if (displayManageRequestDto == null) {
      // 입력정보가 존재하지 않습니다.
      return ApiResult.result(UserEnums.DisplayManage.DISPLAY_MANAGE_CONTS_PARAM_NO_INPUT);
    }

    // # JsonData 변환
    displayManageRequestDto.convertDataList();

    if (displayManageRequestDto.getContsList() == null || displayManageRequestDto.getContsList().size() <= 0) {
      // 순서변경 대상이 존재하지 않습니다.
      return ApiResult.result(UserEnums.DisplayManage.DISPLAY_MANAGE_CONTS_PARAM_NO_SORT_TARGET);
    }

    // ========================================================================
    // # 처리 및 반환
    // ========================================================================
    // # BOS 서비스 호출
    return displayManageBosService.putContsSort(displayManageRequestDto);

  }

  /**
   * 컨텐츠 삭제
   * @param displayManageRequestDto
   * @return
   * @throws BaseException
   */
  @RequestMapping(value = "/admin/display/manage/delConts")
  @ApiOperation(value = "컨텐츠삭제")
  @ApiResponses(value = { @ApiResponse(code = 900, message = "response data", response = DisplayContsResponseDto.class),
                          @ApiResponse(code = 901, message = ""
                              + "DISPLAY_MANAGE_CONTS_PARAM_NO_INPUT - 컨텐츠 입력정보가 존재하지 않습니다. \n"
                              + "DISPLAY_MANAGE_CONTS_PARAM_NO_CONTS_ID - 컨텐츠 ID가 존재하지 않습니다. \n"
                              + "DISPLAY_MANAGE_CONTS_FAIL_DEL_NO_TARGET - 컨텐츠 삭제 대상이 없습니다. \n"
                              + "DISPLAY_MANAGE_CONTS_FAIL_DEL_NO_PROC - 컨텐츠 삭제 처리 중 실패하였습니다. \n"
                              + "DISPLAY_MANAGE_CONTS_FAIL_DEL - 컨텐츠 삭제 처리에 실패하였습니다. "
                          )
  })
  @ResponseBody
  public ApiResult<?> delConts(DisplayManageRequestDto displayManageRequestDto) throws BaseException{
    log.debug("# ######################################");
    log.debug("# DisplayManageController.delConts Start");
    log.debug("# ######################################");
    if (displayManageRequestDto != null) {
      log.debug("# In.displayManageRequestDto     :: " + displayManageRequestDto.toString());
    }
    else {
      log.debug("# In.displayManageRequestDto is Null");
    }

    // ========================================================================
    // # 초기화
    // ========================================================================
    // # 입력값 체크
    if (displayManageRequestDto == null) {
      // 입력정보가 존재하지 않습니다.
      return ApiResult.result(UserEnums.DisplayManage.DISPLAY_MANAGE_CONTS_PARAM_NO_INPUT);
    }

    // # JsonData 변환
    displayManageRequestDto.convertDataList();
    // ContsVo 객체 Set
    //displayManageRequestDto.convertContsDataObject();

    if (displayManageRequestDto.getContsList()== null || displayManageRequestDto.getContsList().size() <= 0) {
      // 컨텐츠 ID가 존재하지 않습니다.
      return ApiResult.result(UserEnums.DisplayManage.DISPLAY_MANAGE_CONTS_PARAM_NO_CONTS_ID);
    }

    // ========================================================================
    // # 처리 및 반환
    // ========================================================================

    //// 단건인 경우도 List 구조로 Set
    //ContsVo contsVo = new ContsVo();
    //contsVo.setDpContsId(displayManageRequestDto.getContsInfo().getDpContsId());
    //contsVo.setDelYn("Y");
    //List<ContsVo> contsList = new ArrayList<ContsVo>();
    //contsList.add(contsVo);
    //displayManageRequestDto.setContsList(contsList);

    log.debug("# In.displayManageRequestDto.contsList :: " + displayManageRequestDto.getContsList().toString());

    // # BOS 서비스 호출
    return displayManageBosService.delConts(displayManageRequestDto);

  }

  /**
   * 컨텐츠 등록
   * @param displayManageRequestDto
   * @return
   * @throws BaseException
   */
  @RequestMapping(value = "/admin/display/manage/addConts")
  @ApiOperation(value = "컨텐츠등록")
  @ApiResponses(value = { @ApiResponse(code = 900, message = "response data", response = DisplayContsResponseDto.class),
                          @ApiResponse(code = 901, message = ""
                              + "DISPLAY_MANAGE_CONTS_PARAM_NO_INPUT - 컨텐츠 입력정보가 존재하지 않습니다. \n"
                              + "DISPLAY_MANAGE_CONTS_PARAM_NO_REG_TARGET - 컨텐츠 등록 대상정보가 존재하지 않습니다. \n"
                              + "DISPLAY_MANAGE_CONTS_FAIL_ADD_NO_PROC - 컨텐츠 등록 중 실패하였습니다. \n"
                              + "DISPLAY_MANAGE_CONTS_FAIL_ADD - 컨텐츠 등록 처리에 실패하였습니다. "
                          )
  })
  @ResponseBody
  public ApiResult<?> addConts(DisplayManageRequestDto displayManageRequestDto) throws BaseException{
    log.debug("# ######################################");
    log.debug("# DisplayManageController.addConts Start");
    log.debug("# ######################################");
    if (displayManageRequestDto != null) {
      log.debug("# In.displayManageRequestDto     :: " + displayManageRequestDto.toString());
    }
    else {
      log.debug("# In.displayManageRequestDto is Null");
    }

    // ========================================================================
    // # 초기화
    // ========================================================================
    // # 입력값 체크
    if (displayManageRequestDto == null) {
      // 입력정보가 존재하지 않습니다.
      return ApiResult.result(UserEnums.DisplayManage.DISPLAY_MANAGE_CONTS_PARAM_NO_INPUT);
    }

    log.debug("# contsTp :: " + StringUtil.nvl(displayManageRequestDto.getContsTp()));

    // JsonData 변환
    if (DisplayEnums.ContentsType.GOODS.getCode().equals(StringUtil.nvl(displayManageRequestDto.getContsTp()))) {
      // 컨텐츠유형 : 상품
      // List Set
      displayManageRequestDto.convertDataList();

      if (displayManageRequestDto.getContsList() == null || displayManageRequestDto.getContsList().size() <= 0) {
        // 인벤토리 등록 대상정보가 존재하지 않습니다.
        return ApiResult.result(UserEnums.DisplayManage.DISPLAY_MANAGE_CONTS_PARAM_NO_REG_TARGET);
      }
   }
    else {
      // 컨텐츠유형 : 상품 이외
      // Vo 객체 Set
      displayManageRequestDto.convertContsDataObject();

      if (displayManageRequestDto.getContsInfo() == null) {
        // 인벤토리 등록 대상정보가 존재하지 않습니다.
        return ApiResult.result(UserEnums.DisplayManage.DISPLAY_MANAGE_CONTS_PARAM_NO_REG_TARGET);
      }
    }

    // ========================================================================
    // # 처리 및 반환
    // ========================================================================
    // # BOS 서비스 호출
    return displayManageBosService.addConts(displayManageRequestDto);

  }

  /**
   * 브랜드 리스트조회(콤보용)
   * @param displayManageRequestDto
   * @return
   * @throws BaseException
   */
  @RequestMapping(value = "/admin/display/manage/selectBrandList")
  @ApiOperation(value = "브랜드리스트조회(콤보용)")
  @ApiResponses(value = { @ApiResponse(code = 900, message = "response data", response = DisplayContsResponseDto.class),
                          @ApiResponse(code = 901, message = ""
                              + "DISPLAY_MANAGE_CONTS_BRAND_LIST_NO_EXIST - 브랜드 목록이 존재하지 않습니다."
                          )
  })
  @ResponseBody
  public ApiResult<?> selectBrandList(DisplayManageRequestDto displayManageRequestDto) throws BaseException{
    log.debug("# ######################################");
    log.debug("# DisplayManageController.selectBrandList Start");
    log.debug("# ######################################");
    if (displayManageRequestDto != null) {
      log.debug("# In.displayManageRequestDto     :: " + displayManageRequestDto.toString());
    }
    else {
      log.debug("# In.displayManageRequestDto is Null");
    }

    // ========================================================================
    // # 초기화
    // ========================================================================
    // # 입력값 체크 : 없음

    // ========================================================================
    // # 처리 및 반환
    // ========================================================================
    // # BOS 서비스 호출
    return displayManageBosService.selectBrandList(displayManageRequestDto);

  }

  /**
   * 상품목록조회-키워드조회
   * @param displayManageRequestDto
   * @return
   * @throws BaseException
   */
  @RequestMapping(value = "/admin/display/manage/selectGoodsListByKeyword")
  @ApiOperation(value = "상품목록조회")
  @ApiResponses(value = { @ApiResponse(code = 900, message = "response data", response = DisplayContsResponseDto.class),
                          @ApiResponse(code = 901, message = ""
                              + "DISPLAY_MANAGE_CONTS_GOODS_LIST_NO_EXIST - 상품 목록이 존재하지 않습니다."
                          )
  })
  @ResponseBody
  public ApiResult<?> selectGoodsListByKeyword(DisplayManageRequestDto displayManageRequestDto) throws BaseException{
    log.debug("# ######################################");
    log.debug("# DisplayManageController.selectGoodsListByKeyword Start");
    log.debug("# ######################################");
    if (displayManageRequestDto != null) {
      log.debug("# In.displayManageRequestDto     :: " + displayManageRequestDto.toString());
    }
    else {
      log.debug("# In.displayManageRequestDto is Null");
    }

    // ========================================================================
    // # 초기화
    // ========================================================================
    // # 입력값 체크 : 없음

    // ========================================================================
    // # 처리 및 반환
    // ========================================================================
    // # BOS 서비스 호출
    ApiResult<?> result = displayManageBosService.selectGoodsListByKeyword(displayManageRequestDto);

    if (result == null) {
      log.debug("# result is null");
    }
    else {
      log.debug("# result is Not null");

//      if (result.getRows() == null) {
//        log.debug("# result.getRows is null");
//      }
      if (result.getData() == null) {
          log.debug("# result.getRows is null");
        }
      else {
        log.debug("# result.getRows is Not null");
//        log.debug("# result.getRows :: " + result.getRows().toString());
        log.debug("# result.getRows :: " + result.getData().toString());

      }

    }
    return result;
//    return displayManageBosService.selectGoodsListByKeyword(displayManageRequestDto);

  }



  /**
   * 상품목록조회-복수조건조회
   * @param displayManageRequestDto
   * @return
   * @throws BaseException
   */
  @RequestMapping(value = "/admin/display/manage/selectGoodsList")
  @ApiOperation(value = "상품목록조회")
  @ApiResponses(value = { @ApiResponse(code = 900, message = "response data", response = DisplayContsResponseDto.class),
                          @ApiResponse(code = 901, message = ""
                              + "DISPLAY_MANAGE_CONTS_GOODS_LIST_NO_EXIST - 상품 목록이 존재하지 않습니다."
                          )
  })
  @ResponseBody
  public ApiResult<?> getGoodsList(HttpServletRequest request, GoodsListRequestDto goodsListRequestDto) throws BaseException{
    log.debug("# ######################################");
    log.debug("# DisplayManageController.getGoodsList Start");
    log.debug("# ######################################");
    //if (goodsListRequestDto != null) {
    //  log.debug("# In.goodsListRequestDto     :: " + goodsListRequestDto.toString());
    //}
    //else {
    //  log.debug("# In.goodsListRequestDto is Null");
    //}

    // ========================================================================
    // # 초기화
    // ========================================================================
    //GoodsListRequestDto goodsListRequestDto = null;
    // # 입력값 체크 : 없음

    // ========================================================================
    // # 처리 및 반환
    // ========================================================================
    // # BOS 서비스 호출
    try {

      goodsListRequestDto = BindUtil.bindDto(request, GoodsListRequestDto.class);
      log.debug("# goodsListRequestDto :: " + goodsListRequestDto.toString());

      return displayManageBosService.getGoodsList(goodsListRequestDto);
    }
    catch (Exception e) {
      return ApiResult.fail();
    }

  }


  // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
  // 인벤토리그룹관리
  // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
  /**
   * 인벤토리그룹 리스트조회
   * @param displayManageRequestDto
   * @return
   * @throws BaseException
   */
  @RequestMapping(value="/admin/display/manage/selectDpInventoryGroupList")
  @ApiOperation(value = "인벤토리그룹리스트조회")
  @ApiResponses(value = { @ApiResponse(code = 900, message = "response data", response = DisplayPageResponseDto.class),
                          @ApiResponse(code = 901, message = ""
                              + "DISPLAY_MANAGE_GRP_PARAM_NO_INPUT - 인벤토리그룹 입력정보가 존재하지 않습니다. \n"
                              + "DISPLAY_MANAGE_GRP_LIST_NO_EXIST - 인벤토리그룹 목록이 존재하지 않습니다. "
                          )
  })
  @ResponseBody
  public ApiResult<?> selectDpInventoryGroupList(DisplayManageRequestDto displayManageRequestDto) throws BaseException {
    log.info("# ######################################");
    log.info("# DisplayManageController.selectDpInventoryGroupList Start");
    log.info("# ######################################");
    if (displayManageRequestDto != null) {
      log.debug("# In.displayManageRequestDto     :: " + displayManageRequestDto.toString());
    }
    else {
      log.debug("# In.displayManageRequestDto is Null");
    }

    // ========================================================================
    // # 초기화
    // ========================================================================
    // # 입력값 체크
    if (displayManageRequestDto == null) {
      // 입력정보가 존재하지 않습니다.
      return ApiResult.result(UserEnums.DisplayManage.DISPLAY_MANAGE_GRP_PARAM_NO_INPUT);
    }

    // ========================================================================
    // # 처리 및 반환
    // ========================================================================
    // # BOS 서비스 호출
    return displayManageBosService.selectDpInventoryGroupList(displayManageRequestDto);

  }

  /**
   * 인벤토리그룹 인벤토리리스트조회
   * @param displayManageRequestDto
   * @return
   * @throws BaseException
   */
  @RequestMapping(value="/admin/display/manage/selectDpGroupInventoryList")
  @ApiOperation(value = "인벤토리그룹인벤토리리스트조회")
  @ApiResponses(value = { @ApiResponse(code = 900, message = "response data", response = DisplayPageResponseDto.class),
      @ApiResponse(code = 901, message = ""
          + "DISPLAY_MANAGE_GRP_PARAM_NO_INPUT - 인벤토리그룹 입력정보가 존재하지 않습니다. \n"
          + "DISPLAY_MANAGE_GRP_LIST_NO_EXIST - 인벤토리그룹 목록이 존재하지 않습니다. "
          )
  })
  @ResponseBody
  public ApiResult<?> selectDpGroupInventoryList(DisplayManageRequestDto displayManageRequestDto) throws BaseException {
    log.info("# ######################################");
    log.info("# DisplayManageController.selectDpGroupInventoryList Start");
    log.info("# ######################################");
    if (displayManageRequestDto != null) {
      log.debug("# In.displayManageRequestDto     :: " + displayManageRequestDto.toString());
    }
    else {
      log.debug("# In.displayManageRequestDto is Null");
    }

    // ========================================================================
    // # 초기화
    // ========================================================================
    // # 입력값 체크
    if (displayManageRequestDto == null) {
      // 입력정보가 존재하지 않습니다.
      return ApiResult.result(UserEnums.DisplayManage.DISPLAY_MANAGE_GRP_PARAM_NO_INPUT);
    }

    // ========================================================================
    // # 처리 및 반환
    // ========================================================================
    // # BOS 서비스 호출
    return displayManageBosService.selectDpGroupInventoryList(displayManageRequestDto);

  }

  /**
   * 인벤토리그룹구성 리스트조회
   * @param displayManageRequestDto
   * @return
   * @throws BaseException
   */
  @RequestMapping(value="/admin/display/manage/selectDpGroupInventoryMappingList")
  @ApiOperation(value = "인벤토리그룹구성리스트조회")
  @ApiResponses(value = { @ApiResponse(code = 900, message = "response data", response = DisplayPageResponseDto.class),
      @ApiResponse(code = 901, message = ""
          + "DISPLAY_MANAGE_GRP_PARAM_NO_INPUT - 인벤토리그룹 입력정보가 존재하지 않습니다. \n"
          + "DISPLAY_MANAGE_GRP_LIST_NO_EXIST - 인벤토리그룹 목록이 존재하지 않습니다. "
          )
  })
  @ResponseBody
  public ApiResult<?> selectDpGroupInventoryMappingList(DisplayManageRequestDto displayManageRequestDto) throws BaseException {
    log.info("# ######################################");
    log.info("# DisplayManageController.selectDpGroupInventoryMappingList Start");
    log.info("# ######################################");
    if (displayManageRequestDto != null) {
      log.debug("# In.displayManageRequestDto     :: " + displayManageRequestDto.toString());
    }
    else {
      log.debug("# In.displayManageRequestDto is Null");
    }

    // ========================================================================
    // # 초기화
    // ========================================================================
    // # 입력값 체크
    if (displayManageRequestDto == null) {
      // 입력정보가 존재하지 않습니다.
      return ApiResult.result(UserEnums.DisplayManage.DISPLAY_MANAGE_GRP_PARAM_NO_INPUT);
    }

    // ========================================================================
    // # 처리 및 반환
    // ========================================================================
    // # BOS 서비스 호출
    return displayManageBosService.selectDpGroupInventoryMappingList(displayManageRequestDto);

  }

  /**
   * 인벤토리그룹 등록
   * @param displayManageRequestDto
   * @return
   * @throws BaseException
   */
  @RequestMapping(value = "/admin/display/manage/addInventoryGroup")
  @ApiOperation(value = "인벤토리그룹등록")
  @ApiResponses(value = { @ApiResponse(code = 900, message = "response data", response = DisplayInventoryResponseDto.class),
                          @ApiResponse(code = 901, message = ""
                              + "DISPLAY_MANAGE_GRP_PARAM_NO_INPUT - 인벤토리그룹 입력정보가 존재하지 않습니다. \n"
                              + "DISPLAY_MANAGE_GRP_PARAM_NO_REG_TARGET - 인벤토리그룹 등록 대상정보가 존재하지 않습니다. \n"
                              + "DISPLAY_MANAGE_GRP_FAIL_ADD_NO_PROC - 인벤토리그룹 등록 중 실패하였습니다. \n"
                              + "DISPLAY_MANAGE_GRP_FAIL_ADD - 인벤토리그룹 등록 처리에 실패하였습니다."
                          )
  })
  @ResponseBody
  public ApiResult<?> addInventoryGroup(DisplayManageRequestDto displayManageRequestDto) throws BaseException{
    log.debug("# ######################################");
    log.debug("# DisplayManageController.addInventoryGroup Start");
    log.debug("# ######################################");
    if (displayManageRequestDto != null) {
      log.debug("# In.displayManageRequestDto     :: " + displayManageRequestDto.toString());
    }
    else {
      log.debug("# In.displayManageRequestDto is Null");
    }

    // ========================================================================
    // # 초기화
    // ========================================================================
    // # 입력값 체크
    if (displayManageRequestDto == null) {
      // 입력정보가 존재하지 않습니다.
      return ApiResult.result(UserEnums.DisplayManage.DISPLAY_MANAGE_GRP_PARAM_NO_INPUT);
    }

    // InventoryVo 객체 Set
    displayManageRequestDto.convertInventoryDataObject();

    if (displayManageRequestDto.getInventoryInfo() == null) {
      // 인벤토리그룹 등록 대상정보가 존재하지 않습니다.
      return ApiResult.result(UserEnums.DisplayManage.DISPLAY_MANAGE_GRP_PARAM_NO_REG_TARGET);
    }
    log.debug("# In.displayManageRequestDto(2)  :: " + displayManageRequestDto.toString());
    // ========================================================================
    // # 처리 및 반환
    // ========================================================================
    // # BOS 서비스 호출
    return displayManageBosService.addInventoryGroup(displayManageRequestDto);

  }

  /**
   * 인벤토리그룹 수정
   * @param displayManageRequestDto
   * @return
   * @throws BaseException
   */
  @RequestMapping(value = "/admin/display/manage/putInventoryGroup")
  @ApiOperation(value = "인벤토리그룹수정")
  @ApiResponses(value = { @ApiResponse(code = 900, message = "response data", response = DisplayInventoryResponseDto.class),
      @ApiResponse(code = 901, message = ""
          + "DISPLAY_MANAGE_GRP_PARAM_NO_INPUT - 인벤토리그룹 입력정보가 존재하지 않습니다. \n"
          + "DISPLAY_MANAGE_GRP_PARAM_NO_REG_TARGET - 인벤토리그룹 등록 대상정보가 존재하지 않습니다. \n"
          + "DISPLAY_MANAGE_GRP_FAIL_DEL_NO_PROC - 인벤토리그룹 삭제 처리 중 실패하였습니다. \n"
          + "DISPLAY_MANAGE_GRP_FAIL_ADD_NO_PROC - 인벤토리그룹 등록 중 실패하였습니다. \n"
          + "DISPLAY_MANAGE_GRP_FAIL_ADD - 인벤토리그룹 등록 처리에 실패하였습니다."
          )
  })
  @ResponseBody
  public ApiResult<?> putInventoryGroup(DisplayManageRequestDto displayManageRequestDto) throws BaseException{
    log.debug("# ######################################");
    log.debug("# DisplayManageController.putInventoryGroup Start");
    log.debug("# ######################################");
    if (displayManageRequestDto != null) {
      log.debug("# In.displayManageRequestDto     :: " + displayManageRequestDto.toString());
    }
    else {
      log.debug("# In.displayManageRequestDto is Null");
    }

    // ========================================================================
    // # 초기화
    // ========================================================================
    // # 입력값 체크
    if (displayManageRequestDto == null) {
      // 입력정보가 존재하지 않습니다.
      return ApiResult.result(UserEnums.DisplayManage.DISPLAY_MANAGE_GRP_PARAM_NO_INPUT);
    }

    // InventoryVo 객체 Set
    displayManageRequestDto.convertInventoryDataObject();

    if (displayManageRequestDto.getInventoryInfo() == null) {
      // 인벤토리그룹 등록 대상정보가 존재하지 않습니다.
      return ApiResult.result(UserEnums.DisplayManage.DISPLAY_MANAGE_GRP_PARAM_NO_REG_TARGET);
    }
    log.debug("# In.displayManageRequestDto(2)  :: " + displayManageRequestDto.toString());
    // ========================================================================
    // # 처리 및 반환
    // ========================================================================
    // # BOS 서비스 호출
    return displayManageBosService.putInventoryGroup(displayManageRequestDto);

  }

  /**
   * 인벤토리그룹 삭제
   * @param displayManageRequestDto
   * @return
   * @throws BaseException
   */
  @RequestMapping(value = "/admin/display/manage/delInventoryGroup")
  @ApiOperation(value = "인벤토리그룹삭제")
  @ApiResponses(value = { @ApiResponse(code = 900, message = "response data", response = DisplayContsResponseDto.class),
                          @ApiResponse(code = 901, message = ""
                              + "DISPLAY_MANAGE_GRP_PARAM_NO_INPUT - 인벤토리그룹 입력정보가 존재하지 않습니다. \n"
                              + "DISPLAY_MANAGE_GRP_PARAM_NO_GRP_ID - 인벤토리그룹 ID가 존재하지 않습니다. \n"
                              + "DISPLAY_MANAGE_GRP_FAIL_DEL_NO_TARGET - 인벤토리그룹 삭제 대상이 없습니다. \n"
                              + "DISPLAY_MANAGE_GRP_FAIL_DEL_NO_PROC - 인벤토리그룹 삭제 처리 중 실패하였습니다. \n"
                              + "DISPLAY_MANAGE_GRP_FAIL_DEL - 인벤토리그룹 삭제 처리에 실패하였습니다. "
                          )
  })
  @ResponseBody
  public ApiResult<?> delInventoryGroup(DisplayManageRequestDto displayManageRequestDto) throws BaseException{
    log.debug("# ######################################");
    log.debug("# DisplayManageController.delInventoryGroup Start");
    log.debug("# ######################################");
    if (displayManageRequestDto != null) {
      log.debug("# In.displayManageRequestDto     :: " + displayManageRequestDto.toString());
    }
    else {
      log.debug("# In.displayManageRequestDto is Null");
    }

    // ========================================================================
    // # 초기화
    // ========================================================================
    // # 입력값 체크
    if (displayManageRequestDto == null) {
      // 입력정보가 존재하지 않습니다.
      return ApiResult.result(UserEnums.DisplayManage.DISPLAY_MANAGE_GRP_PARAM_NO_INPUT);
    }

    // # JsonData 변환
    displayManageRequestDto.convertDataList();
    // ContsVo 객체 Set
    //displayManageRequestDto.convertContsDataObject();

    if (displayManageRequestDto.getInventoryList() == null || displayManageRequestDto.getInventoryList().size() <= 0) {
      // 컨텐츠 ID가 존재하지 않습니다.
      return ApiResult.result(UserEnums.DisplayManage.DISPLAY_MANAGE_GRP_PARAM_NO_GRP_ID);
    }

    // ========================================================================
    // # 처리 및 반환
    // ========================================================================
    log.debug("# In.displayManageRequestDto.getInventoryList :: " + displayManageRequestDto.getInventoryList().toString());

    // # BOS 서비스 호출
    return displayManageBosService.delInventoryGroup(displayManageRequestDto);

  }

  /**
   * 인벤토리그룹 순서변경
   * @param displayManageRequestDto
   * @return
   * @throws BaseException
   */
  @RequestMapping(value = "/admin/display/manage/putInventoryGroupSort")
  @ApiOperation(value = "인벤토리순서변경")
  @ApiResponses(value = { @ApiResponse(code = 900, message = "response data", response = DisplayInventoryResponseDto.class),
                          @ApiResponse(code = 901, message = ""
                              + "DISPLAY_MANAGE_GRP_PARAM_NO_INPUT - 인벤토리그룹 입력정보가 존재하지 않습니다. \n"
                              + "DISPLAY_MANAGE_GRP_PARAM_NO_SORT_TARGET - 인벤토리그룹 순서변경 대상 목록이 존재하지 않습니다. \n"
                              + "DISPLAY_MANAGE_GRP_FAIL_PUT_SORT_NO_TARGET - 인벤토리그룹 순번 변경 대상이 없습니다. \n"
                              + "DISPLAY_MANAGE_GRP_FAIL_PUT_SORT_NO_PROC - 인벤토리그룹 순번 변경 중 실패하였습니다. \n"
                              + "DISPLAY_MANAGE_GRP_FAIL_PUT_SORT - 인벤토리그룹 순번 변경 처리에 실패하였습니다."
                          )
  })
  @ResponseBody
  public ApiResult<?> putInventoryGroupSort(DisplayManageRequestDto displayManageRequestDto) throws BaseException{
    log.debug("# ######################################");
    log.debug("# DisplayManageController.putInventoryGroupSort Start");
    log.debug("# ######################################");
    if (displayManageRequestDto != null) {
      log.debug("# In.displayManageRequestDto     :: " + displayManageRequestDto.toString());
    }
    else {
      log.debug("# In.displayManageRequestDto is Null");
    }

    // ========================================================================
    // # 초기화
    // ========================================================================
    // # 입력값 체크
    if (displayManageRequestDto == null) {
      // 입력정보가 존재하지 않습니다.
      return ApiResult.result(UserEnums.DisplayManage.DISPLAY_MANAGE_GRP_PARAM_NO_INPUT);
    }

    // # JsonData 변환
    displayManageRequestDto.convertDataList();

    if (displayManageRequestDto.getInventoryList()== null || displayManageRequestDto.getInventoryList().size() <= 0) {
      // 순서변경 대상이 존재하지 않습니다.
      return ApiResult.result(UserEnums.DisplayManage.DISPLAY_MANAGE_GRP_PARAM_NO_SORT_TARGET);
    }

    // ========================================================================
    // # 처리 및 반환
    // ========================================================================
    // # BOS 서비스 호출
    return displayManageBosService.putInventoryGroupSort(displayManageRequestDto);

  }


}
