package kr.co.pulmuone.bos.display.manage.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;

import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.enums.DisplayEnums;
import kr.co.pulmuone.v1.comm.exception.BaseException;
import kr.co.pulmuone.v1.comm.framework.dto.ExcelDownloadDto;
import kr.co.pulmuone.v1.comm.framework.dto.ExcelWorkSheetDto;
import kr.co.pulmuone.v1.comm.framework.view.ExcelDownloadView;
import kr.co.pulmuone.v1.comm.util.DateUtil;
import kr.co.pulmuone.v1.comm.util.StringUtil;
import kr.co.pulmuone.v1.display.manage.dto.DisplayContsResponseDto;
import kr.co.pulmuone.v1.display.manage.dto.DisplayInventoryResponseDto;
import kr.co.pulmuone.v1.display.manage.dto.DisplayManageRequestDto;
import kr.co.pulmuone.v1.display.manage.dto.vo.ContsVo;
import kr.co.pulmuone.v1.display.manage.dto.vo.InventoryVo;
import kr.co.pulmuone.v1.display.manage.service.DisplayManageBiz;
import kr.co.pulmuone.v1.goods.goods.dto.GoodsListRequestDto;
import kr.co.pulmuone.v1.goods.goods.service.GoodsListBiz;
import lombok.extern.slf4j.Slf4j;

/**
* <PRE>
* Forbiz Korea
* 전시관리 BOS Service
*
* </PRE>
*
* <PRE>
* <B>History:</B>
* =======================================================================
*  버전  :   작성일                :  작성자      :  작성내역
* -----------------------------------------------------------------------
*  1.0    2020.09.23.    dgyoun   최초작성
* =======================================================================
* </PRE>
*/

@Slf4j
@Service
public class DisplayManageBosService {

  @Autowired
  private DisplayManageBiz displayManageBiz;

  @Autowired
  private ExcelDownloadView excelDownloadView; // 엑셀 다운로드 뷰

  @Autowired
  private GoodsListBiz goodsListBiz;

  // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
  // 전시페이지관리
  // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
  /**
   * 전시페이지 리스트조회
   * @param dpPageId
   * @param useAllYn
   * @return
   * @throws Exception
   */
  public ApiResult<?> selectDpPageList (DisplayManageRequestDto displayManageRequestDto) throws BaseException {

    return displayManageBiz.selectDpPageList(displayManageRequestDto);
  }

  /**
   * 전시카테고리 리스트조회
   * @param depth
   * @param useAllYn
   * @return
   * @throws Exception
   */
  public ApiResult<?> selectDpCategoryList (DisplayManageRequestDto displayManageRequestDto) throws BaseException {

    return displayManageBiz.selectDpCategoryList(displayManageRequestDto);
  }

  /**
   * 페이지 상세조회
   * @param dpPageId
   * @return
   * @throws Exception
   */
  public ApiResult<?> selectPageInfo (DisplayManageRequestDto displayManageRequestDto) throws BaseException {

    return displayManageBiz.selectPageInfo(displayManageRequestDto);
  }

  /**
   * 페이지 수정
   * @param pageVo
   * @return
   * @throws Exception
   */
  public ApiResult<?> putPage (DisplayManageRequestDto displayManageRequestDto) throws BaseException {

    return displayManageBiz.putPage(displayManageRequestDto);
  }

  /**
   * 페이지 순서변경
   * @param pageVoList
   * @return
   * @throws Exception
   */
  public ApiResult<?> putPageSort (DisplayManageRequestDto displayManageRequestDto) throws BaseException {

    return displayManageBiz.putPageSort(displayManageRequestDto);
  }

  /**
   * 페이지 삭제
   * @param pageVoList
   * @return
   * @throws Exception
   */
  public ApiResult<?> delPage (DisplayManageRequestDto displayManageRequestDto) throws BaseException {

    return displayManageBiz.delPage(displayManageRequestDto);
  }

  /**
   * 페이지 등록
   * @param pageVo
   * @return
   * @throws Exception
   */
  public ApiResult<?> addPage (DisplayManageRequestDto displayManageRequestDto) throws BaseException {

    return displayManageBiz.addPage(displayManageRequestDto);
  }

  // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
  // 전시인벤토리관리
  // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
  /**
   * 인벤토리리스트 엑셀다운로드
   * @param displayManageRequestDto
   * @return
   * @throws Exception
   */
  public ModelAndView selectInventoryListForExcelExport (DisplayManageRequestDto displayManageRequestDto) throws BaseException {
    log.debug("# ######################################");
    log.debug("# DisplayManageBosService.selectInventoryListForExcelExport Start");
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
    ModelAndView modelAndView = new ModelAndView(excelDownloadView);

    // # 입력값 체크
    if (displayManageRequestDto == null) {
      // 입력정보가 존재하지 않흡니다.
      log.debug("# 입력정보 미존재");
      return modelAndView;
    }

    String pageTp = displayManageRequestDto.getPageTp();
    log.debug("# pageTp :: " + pageTp);

    if (StringUtil.isEmpty(pageTp)) {
      log.debug("# 페이지유형 is Null");
      return modelAndView;
    }

    // ========================================================================
    // # 처리
    // ========================================================================
    // ------------------------------------------------------------------------
    // * 파일명/시트명
    // ------------------------------------------------------------------------
    String pageTpNm = "";

    if (DisplayEnums.PageTp.PAGE.getCode().equals(pageTp)) {
      pageTpNm = "페이지";
    }
    else {
      pageTpNm = "카테고리";
    }

    // 엑셀 파일 이름: 확장자는 xlsx 자동 설정됨
    String excelFileName   = "[전시인벤토리_" + pageTpNm +  "]-" + displayManageRequestDto.getPageNm() + "-" + DateUtil.getCurrentDate();
    // 엑셀 파일 내 워크시트 이름
    String excelSheetName  = "sheet";

    // ------------------------------------------------------------------------
    // 컬럼별 width 목록 : 단위 pixel
    //  - ( 필수 아님 ) ExcelWorkSheetDto 에 지정하지 않을 경우 모든 컬럼 너비는 120 pixel 로 고정됨
    // ------------------------------------------------------------------------
    // TODO : size 조정
    Integer[] widthListOfFirstWorksheet = null;
    //Integer[] widthListOfFirstWorksheet = {150, 300, 300, 300, 300, 300, 300, 300, 300, 300};

    // ------------------------------------------------------------------------
    // 본문 데이터 컬럼별 정렬 목록
    //  - ( 필수 아님 ) ExcelWorkSheetDto 에 지정하지 않을 경우 모든 컬럼 정렬은 "left" (좌측 정렬) 로 고정
    //  - "left", "center", "right", "justify", "distributed" 가 아닌 다른 값 지정시 "left" (좌측 정렬) 로 지정됨
    // ------------------------------------------------------------------------
    String[] alignListOfFirstWorksheet = null;
    //String[] alignListOfFirstWorksheet = {"center", "center", "center", "center", "left", "center", "center", "center", "center", "center"};

    // ------------------------------------------------------------------------
    // 본문 데이터 컬럼별 데이터 property 목록
    //  - ( 필수 ) 엑셀 데이터 JSON 변환시 각 json Object 의 key 값과 일치해야 함
    // ------------------------------------------------------------------------
    String[] propertyListOfFirstWorksheet = null;
    // String[] propertyListOfFirstWorksheet = {"sort", "inventoryCd", "inventoryNm", "pageNm", "pageFullPath", "contsLevel1TpNm", "contsLevel2TpNm", "contsLevel3TpNm", "dpRangeTpNm", "useYn"};

    // ------------------------------------------------------------------------
    // 첫 번째 워크시트의 상단 헤더의 각 행별 정보 : 다단 구성, 셀 머지 가능
    // ------------------------------------------------------------------------
    // 첫 번째 헤더 컬럼 : 동일 이름 연속시 셀 머지
    String[] firstHeaderListOfFirstWorksheet = null;
    // String[] firstHeaderListOfFirstWorksheet = {"순번", "인벤토리 코드", "인벤토리 명", "페이지 명", "Lv", "Lv2", "Lv3", "인벤토리 전시범위", "사용여부"};

    // ------------------------------------------------------------------------
    // * 페이지코너
    // ------------------------------------------------------------------------

    // ------------------------------------------------------------------------
    // 컬럼별 width 목록 : 단위 pixel
    //  - ( 필수 아님 ) ExcelWorkSheetDto 에 지정하지 않을 경우 모든 컬럼 너비는 120 pixel 로 고정됨
    // ------------------------------------------------------------------------
    // TODO : size 조정
    widthListOfFirstWorksheet = new Integer[10];
    widthListOfFirstWorksheet[0] = 150;
    widthListOfFirstWorksheet[1] = 300;
    widthListOfFirstWorksheet[2] = 300;
    widthListOfFirstWorksheet[3] = 300;
    widthListOfFirstWorksheet[4] = 300;
    widthListOfFirstWorksheet[5] = 300;
    widthListOfFirstWorksheet[6] = 300;
    widthListOfFirstWorksheet[7] = 300;
    widthListOfFirstWorksheet[8] = 300;
    widthListOfFirstWorksheet[9] = 300;
    // ------------------------------------------------------------------------
    // 본문 데이터 컬럼별 정렬 목록
    //  - ( 필수 아님 ) ExcelWorkSheetDto 에 지정하지 않을 경우 모든 컬럼 정렬은 "left" (좌측 정렬) 로 고정
    //  - "left", "center", "right", "justify", "distributed" 가 아닌 다른 값 지정시 "left" (좌측 정렬) 로 지정됨
    // ------------------------------------------------------------------------
    alignListOfFirstWorksheet = new String[10];
    alignListOfFirstWorksheet[0] = "center";
    alignListOfFirstWorksheet[1] = "center";
    alignListOfFirstWorksheet[2] = "center";
    alignListOfFirstWorksheet[3] = "center";
    alignListOfFirstWorksheet[4] = "center";
    alignListOfFirstWorksheet[5] = "center";
    alignListOfFirstWorksheet[6] = "center";
    alignListOfFirstWorksheet[7] = "center";
    alignListOfFirstWorksheet[8] = "center";
    alignListOfFirstWorksheet[9] = "center";
    // ------------------------------------------------------------------------
    // 본문 데이터 컬럼별 데이터 property 목록
    //  - ( 필수 ) 엑셀 데이터 JSON 변환시 각 json Object 의 key 값과 일치해야 함
    // ------------------------------------------------------------------------
    propertyListOfFirstWorksheet = new String[10];
    propertyListOfFirstWorksheet[0] = "sort";
    propertyListOfFirstWorksheet[1] = "excelDispinventoryCd";
    propertyListOfFirstWorksheet[2] = "inventoryNm";
    propertyListOfFirstWorksheet[3] = "pageNm";
    propertyListOfFirstWorksheet[4] = "pageFullPath";
    propertyListOfFirstWorksheet[5] = "contsLevel1TpNm";
    propertyListOfFirstWorksheet[6] = "contsLevel2TpNm";
    propertyListOfFirstWorksheet[7] = "contsLevel3TpNm";
    propertyListOfFirstWorksheet[8] = "dpRangeTpNm";
    propertyListOfFirstWorksheet[9] = "useYn";
    // ------------------------------------------------------------------------
    // 첫 번째 워크시트의 상단 헤더의 각 행별 정보 : 다단 구성, 셀 머지 가능
    // ------------------------------------------------------------------------
    // 첫 번째 헤더 컬럼 : 동일 이름 연속시 셀 머지
    firstHeaderListOfFirstWorksheet = new String[10];
    firstHeaderListOfFirstWorksheet[0] = "순번";
    firstHeaderListOfFirstWorksheet[1] = "인벤토리 코드";
    firstHeaderListOfFirstWorksheet[2] = "인벤토리 명";
    firstHeaderListOfFirstWorksheet[3] = "페이지 명";
    firstHeaderListOfFirstWorksheet[4] = "페이지 경로";
    firstHeaderListOfFirstWorksheet[5] = "Lv1";
    firstHeaderListOfFirstWorksheet[6] = "Lv2";
    firstHeaderListOfFirstWorksheet[7] = "Lv3";
    firstHeaderListOfFirstWorksheet[8] = "인벤토리 전시범위";
    firstHeaderListOfFirstWorksheet[9] = "사용여부";

    // ------------------------------------------------------------------------
    // 워크시트 DTO 생성 후 정보 세팅
    // ------------------------------------------------------------------------
    ExcelWorkSheetDto firstWorkSheetDto = ExcelWorkSheetDto.builder()   //
        .workSheetName(excelSheetName)                                  // 엑셀 파일내 워크시트 명
        .propertyList(propertyListOfFirstWorksheet)                     // 컬럼별 데이터 property 목록
        .widthList(widthListOfFirstWorksheet)                           // 컬럼별 너비 목록
        .alignList(alignListOfFirstWorksheet)                           // 컬럼별 정렬 목록
        .build();

    // ------------------------------------------------------------------------
    // 엑셀 다단 헤더 구성 : ( 헤더 행 index , 헤더 제목 배열 ) 형식으로 세팅
    // ------------------------------------------------------------------------
    // 첫 번째 헤더 컬럼
    firstWorkSheetDto.setHeaderList(0, firstHeaderListOfFirstWorksheet);

    // ------------------------------------------------------------------------
    // 엑셀 본문 샘플 데이터 생성 : List<?> 형식만 세팅 가능
    // excelData 를 세팅하지 않으면 샘플 엑셀로 다운로드됨
    // ------------------------------------------------------------------------
    ApiResult<?> resultApi = null;
    List<InventoryVo> resultInventoryList = null;

    try {

      if ((DisplayEnums.PageSchTp.GROUP.getCode()).equals(StringUtil.nvl(displayManageRequestDto.getPageSchTp()))) {
        // --------------------------------------------------------------------
        // 페이지검색유형.페이지별 그룹 검색
        // --------------------------------------------------------------------
        resultApi = displayManageBiz.selectDpGroupInventoryList(displayManageRequestDto);

        //log.debug("# resultApi.getRows :: " + (resultApi.getRows()).getClass().getName());
        //DisplayInventoryResponseDto resultDto = (DisplayInventoryResponseDto)resultApi.getRows();
        log.debug("# resultApi.getRows :: " + (resultApi.getData()).getClass().getName());
        DisplayInventoryResponseDto resultDto = (DisplayInventoryResponseDto)resultApi.getData();
        //resultList = (List<InventoryVo>) resultApi.getRows();
        resultInventoryList = (List<InventoryVo>)resultDto.getRows();

        //// 그룹 검색의 경우 페이지명과 페이지경로는 화면으로부터 들어온 값을 Set 한다.
        //if (resultInventoryList != null && resultInventoryList.size() > 0) {
        //  for(InventoryVo unitVo : resultInventoryList) {
        //
        //    unitVo.setPageNm(displayManageRequestDto.getPageNm());
        //    unitVo.setPageFullPath(displayManageRequestDto.getPageFullPath());
        //  }
        //}

      }
      else {
        // --------------------------------------------------------------------
        // 페이지검색유형.페이지별 인벤토리 검색
        // --------------------------------------------------------------------
        resultApi = displayManageBiz.selectInventoryList(displayManageRequestDto);

        //log.debug("# resultApi.getRows :: " + (resultApi.getRows()).getClass().getName());
        //resultList = (List<InventoryVo>) resultApi.getRows();
        //DisplayInventoryResponseDto resultDto = (DisplayInventoryResponseDto)resultApi.getRows();
        log.debug("# resultApi.getRows :: " + (resultApi.getData()).getClass().getName());
        DisplayInventoryResponseDto resultDto = (DisplayInventoryResponseDto)resultApi.getData();
        resultInventoryList = (List<InventoryVo>)resultDto.getRows();
      }



    } catch (Exception e) {
        log.error(e.getMessage());
        throw e; // 추후 CustomException 으로 변환 예정
    }

    // Dto에 Set
    firstWorkSheetDto.setExcelDataList(resultInventoryList);

    // xcelDownloadDto 생성 후 workSheetDto 추가
    ExcelDownloadDto excelDownloadDto = ExcelDownloadDto.builder().excelFileName(excelFileName).build();

    excelDownloadDto.addExcelWorkSheet(firstWorkSheetDto);

    // ========================================================================
    // # 반환
    // ========================================================================
    modelAndView.addObject(ExcelDownloadView.excelDownloadDtoKeyInModel, excelDownloadDto);

    // ========================================================================
    // # 반환
    // ========================================================================
    return modelAndView;
  }

  /**
   * 인벤토리 리스트조회
   * @param depth
   * @param useAllYn
   * @return
   * @throws Exception
   */
  public ApiResult<?> selectInventoryList (DisplayManageRequestDto displayManageRequestDto) throws BaseException {

    return displayManageBiz.selectInventoryList(displayManageRequestDto);
  }

  /**
   * 인벤토리 상세조회
   * @param dpPageId
   * @return
   * @throws Exception
   */
  public ApiResult<?> selectInventoryInfo (DisplayManageRequestDto displayManageRequestDto) throws BaseException {

    return displayManageBiz.selectInventoryInfo(displayManageRequestDto);
  }

  /**
   * 인벤토리 수정
   * @param pageVo
   * @return
   * @throws Exception
   */
  public ApiResult<?> putInventory (DisplayManageRequestDto displayManageRequestDto) throws BaseException {

    return displayManageBiz.putInventory(displayManageRequestDto);
  }

  /**
   * 인벤토리 순서변경
   * @param pageVoList
   * @return
   * @throws Exception
   */
  public ApiResult<?> putInventorySort (DisplayManageRequestDto displayManageRequestDto) throws BaseException {

    return displayManageBiz.putInventorySort(displayManageRequestDto);
  }

  /**
   * 인벤토리 삭제
   * @param pageVoList
   * @return
   * @throws Exception
   */
  public ApiResult<?> delInventory (DisplayManageRequestDto displayManageRequestDto) throws BaseException {

    return displayManageBiz.delInventory(displayManageRequestDto);
  }

  /**
   * 인벤토리 등록
   * @param pageVo
   * @return
   * @throws Exception
   */
  public ApiResult<?> addInventory (DisplayManageRequestDto displayManageRequestDto) throws BaseException {

    return displayManageBiz.addInventory(displayManageRequestDto);
  }


  // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
  // 전시컨텐츠관리
  // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
  /**
   * 컨텐츠리스트 엑셀다운로드
   * @param displayManageRequestDto
   * @return
   * @throws Exception
   */
  public ModelAndView selectContsListForExcelExport (DisplayManageRequestDto displayManageRequestDto) throws BaseException {
    log.debug("# ######################################");
    log.debug("# DisplayManageBosService.selectContsListForExcelExport Start");
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
    ModelAndView modelAndView = new ModelAndView(excelDownloadView);

    // # 입력값 체크
    if (displayManageRequestDto == null) {
      // 입력정보가 존재하지 않흡니다.
      log.debug("# 입력정보 미존재");
      return modelAndView;
    }

    String contsTp = displayManageRequestDto.getContsTp();
    log.debug("# contsTp :: " + contsTp);

    if (StringUtil.isEmpty(contsTp)) {
      log.debug("# 컨텐츠유형 is Null");
      return modelAndView;
    }

    // ========================================================================
    // # 처리
    // ========================================================================
    // ------------------------------------------------------------------------
    // * 파일명/시트명
    // ------------------------------------------------------------------------
    // 엑셀 파일 이름: 확장자는 xlsx 자동 설정됨
    String excelFileName  = "[전시컨텐츠-" + displayManageRequestDto.getInventoryNm() + "-Lv" + displayManageRequestDto.getContsLevel() + ".";
    String contsNm        = "";
    // 엑셀 파일 내 워크시트 이름
    String excelSheetName  = "sheet";

    // ------------------------------------------------------------------------
    // 컬럼별 width 목록 : 단위 pixel
    //  - ( 필수 아님 ) ExcelWorkSheetDto 에 지정하지 않을 경우 모든 컬럼 너비는 120 pixel 로 고정됨
    // ------------------------------------------------------------------------
    // TODO : size 조정
    Integer[] widthListOfFirstWorksheet = null;
    //Integer[] widthListOfFirstWorksheet = {150, 300, 300, 300, 300, 300, 300, 300, 300, 300};

    // ------------------------------------------------------------------------
    // 본문 데이터 컬럼별 정렬 목록
    //  - ( 필수 아님 ) ExcelWorkSheetDto 에 지정하지 않을 경우 모든 컬럼 정렬은 "left" (좌측 정렬) 로 고정
    //  - "left", "center", "right", "justify", "distributed" 가 아닌 다른 값 지정시 "left" (좌측 정렬) 로 지정됨
    // ------------------------------------------------------------------------
    String[] alignListOfFirstWorksheet = null;
    //String[] alignListOfFirstWorksheet = {"center", "center", "center", "center", "left", "center", "center", "center", "center", "center"};

    // ------------------------------------------------------------------------
    // 본문 데이터 컬럼별 데이터 property 목록
    //  - ( 필수 ) 엑셀 데이터 JSON 변환시 각 json Object 의 key 값과 일치해야 함
    // ------------------------------------------------------------------------
    String[] propertyListOfFirstWorksheet = null;
    // String[] propertyListOfFirstWorksheet = {"sort", "inventoryCd", "inventoryNm", "pageNm", "pageFullPath", "contsLevel1TpNm", "contsLevel2TpNm", "contsLevel3TpNm", "dpRangeTpNm", "useYn"};

    // ------------------------------------------------------------------------
    // 첫 번째 워크시트의 상단 헤더의 각 행별 정보 : 다단 구성, 셀 머지 가능
    // ------------------------------------------------------------------------
    // 첫 번째 헤더 컬럼 : 동일 이름 연속시 셀 머지
    String[] firstHeaderListOfFirstWorksheet = null;
    // String[] firstHeaderListOfFirstWorksheet = {"순번", "인벤토리 코드", "인벤토리 명", "페이지 명", "Lv", "Lv2", "Lv3", "인벤토리 전시범위", "사용여부"};



    if (DisplayEnums.DpContentsTp.TEXT.getCode().equals(contsTp)) {
      // ----------------------------------------------------------------------
      // * 전시컨텐츠유형-Text
      // ----------------------------------------------------------------------

      // 파일명
      contsNm = "TEXT";
      excelFileName += contsNm +  "]-" + DateUtil.getCurrentDate();

      // ----------------------------------------------------------------------
      // 컬럼별 width 목록 : 단위 pixel
      //  - ( 필수 아님 ) ExcelWorkSheetDto 에 지정하지 않을 경우 모든 컬럼 너비는 120 pixel 로 고정됨
      // ----------------------------------------------------------------------
      // TODO : size 조정
      widthListOfFirstWorksheet = new Integer[11];
      widthListOfFirstWorksheet[0]  = 150;
      widthListOfFirstWorksheet[1]  = 300;
      widthListOfFirstWorksheet[2]  = 300;
      widthListOfFirstWorksheet[3]  = 300;
      widthListOfFirstWorksheet[4]  = 300;
      widthListOfFirstWorksheet[5]  = 300;
      widthListOfFirstWorksheet[6]  = 300;
      widthListOfFirstWorksheet[7]  = 300;
      widthListOfFirstWorksheet[8]  = 300;
      widthListOfFirstWorksheet[9]  = 300;
      widthListOfFirstWorksheet[10] = 300;
      // ----------------------------------------------------------------------
      // 본문 데이터 컬럼별 정렬 목록
      //  - ( 필수 아님 ) ExcelWorkSheetDto 에 지정하지 않을 경우 모든 컬럼 정렬은 "left" (좌측 정렬) 로 고정
      //  - "left", "center", "right", "justify", "distributed" 가 아닌 다른 값 지정시 "left" (좌측 정렬) 로 지정됨
      // ----------------------------------------------------------------------
      alignListOfFirstWorksheet = new String[11];
      alignListOfFirstWorksheet[0]  = "center";
      alignListOfFirstWorksheet[1]  = "center";
      alignListOfFirstWorksheet[2]  = "center";
      alignListOfFirstWorksheet[3]  = "center";
      alignListOfFirstWorksheet[4]  = "center";
      alignListOfFirstWorksheet[5]  = "center";
      alignListOfFirstWorksheet[6]  = "center";
      alignListOfFirstWorksheet[7]  = "center";
      alignListOfFirstWorksheet[8]  = "center";
      alignListOfFirstWorksheet[9]  = "center";
      alignListOfFirstWorksheet[10] = "center";
      // ----------------------------------------------------------------------
      // 본문 데이터 컬럼별 데이터 property 목록
      //  - ( 필수 ) 엑셀 데이터 JSON 변환시 각 json Object 의 key 값과 일치해야 함
      // ----------------------------------------------------------------------
      propertyListOfFirstWorksheet = new String[11];
      propertyListOfFirstWorksheet[0]   = "sort";
      propertyListOfFirstWorksheet[1]   = "dpContsId";
      propertyListOfFirstWorksheet[2]   = "titleNm";
      propertyListOfFirstWorksheet[3]   = "dpStartDt";
      propertyListOfFirstWorksheet[4]   = "dpEndDt";
      propertyListOfFirstWorksheet[5]   = "dpRangeTpNm";
      propertyListOfFirstWorksheet[6]   = "statusNm";
      propertyListOfFirstWorksheet[7]   = "text1String";
      propertyListOfFirstWorksheet[8]   = "text2String";
      propertyListOfFirstWorksheet[9]   = "text3String";
      propertyListOfFirstWorksheet[10]  = "linkUrlPc";
      // ----------------------------------------------------------------------
      // 첫 번째 워크시트의 상단 헤더의 각 행별 정보 : 다단 구성, 셀 머지 가능
      // ----------------------------------------------------------------------
      // 첫 번째 헤더 컬럼 : 동일 이름 연속시 셀 머지
      firstHeaderListOfFirstWorksheet = new String[11];
      firstHeaderListOfFirstWorksheet[0]  = "순번";
      firstHeaderListOfFirstWorksheet[1]  = "컨텐츠번호";
      firstHeaderListOfFirstWorksheet[2]  = "타이틀명";
      firstHeaderListOfFirstWorksheet[3]  = "시작일";
      firstHeaderListOfFirstWorksheet[4]  = "종료일";
      firstHeaderListOfFirstWorksheet[5]  = "전시범위";
      firstHeaderListOfFirstWorksheet[6]  = "진행상태";
      firstHeaderListOfFirstWorksheet[7]  = "노출 텍스트1";
      firstHeaderListOfFirstWorksheet[8]  = "노출 텍스트2";
      firstHeaderListOfFirstWorksheet[9]  = "노출 텍스트3";
      firstHeaderListOfFirstWorksheet[10] = "링크 URL";

    }
    else if (DisplayEnums.DpContentsTp.HTML.getCode().equals(contsTp)) {
      // ----------------------------------------------------------------------
      // * 전시컨텐츠유형-Html
      // ----------------------------------------------------------------------

      // 파일명
      contsNm = "HTML";
      excelFileName += contsNm +  "]-" + DateUtil.getCurrentDate();

      // ----------------------------------------------------------------------
      // 컬럼별 width 목록 : 단위 pixel
      //  - ( 필수 아님 ) ExcelWorkSheetDto 에 지정하지 않을 경우 모든 컬럼 너비는 120 pixel 로 고정됨
      // ----------------------------------------------------------------------
      // TODO : size 조정
      widthListOfFirstWorksheet = new Integer[9];
      widthListOfFirstWorksheet[0]  = 150;
      widthListOfFirstWorksheet[1]  = 300;
      widthListOfFirstWorksheet[2]  = 300;
      widthListOfFirstWorksheet[3]  = 300;
      widthListOfFirstWorksheet[4]  = 300;
      widthListOfFirstWorksheet[5]  = 300;
      widthListOfFirstWorksheet[6]  = 300;
      widthListOfFirstWorksheet[7]  = 300;
      widthListOfFirstWorksheet[8]  = 300;
      // ----------------------------------------------------------------------
      // 본문 데이터 컬럼별 정렬 목록
      //  - ( 필수 아님 ) ExcelWorkSheetDto 에 지정하지 않을 경우 모든 컬럼 정렬은 "left" (좌측 정렬) 로 고정
      //  - "left", "center", "right", "justify", "distributed" 가 아닌 다른 값 지정시 "left" (좌측 정렬) 로 지정됨
      // ----------------------------------------------------------------------
      alignListOfFirstWorksheet = new String[9];
      alignListOfFirstWorksheet[0]  = "center";
      alignListOfFirstWorksheet[1]  = "center";
      alignListOfFirstWorksheet[2]  = "center";
      alignListOfFirstWorksheet[3]  = "center";
      alignListOfFirstWorksheet[4]  = "center";
      alignListOfFirstWorksheet[5]  = "center";
      alignListOfFirstWorksheet[6]  = "center";
      alignListOfFirstWorksheet[7]  = "center";
      alignListOfFirstWorksheet[8]  = "center";
      // ----------------------------------------------------------------------
      // 본문 데이터 컬럼별 데이터 property 목록
      //  - ( 필수 ) 엑셀 데이터 JSON 변환시 각 json Object 의 key 값과 일치해야 함
      // ----------------------------------------------------------------------
      propertyListOfFirstWorksheet = new String[9];
      propertyListOfFirstWorksheet[0]   = "sort";
      propertyListOfFirstWorksheet[1]   = "dpContsId";
      propertyListOfFirstWorksheet[2]   = "titleNm";
      propertyListOfFirstWorksheet[3]   = "dpStartDt";
      propertyListOfFirstWorksheet[4]   = "dpEndDt";
      propertyListOfFirstWorksheet[5]   = "dpRangeTpNm";
      propertyListOfFirstWorksheet[6]   = "statusNm";
      propertyListOfFirstWorksheet[7]   = "htmlPc";
      propertyListOfFirstWorksheet[8]   = "htmlMobile";
      // ----------------------------------------------------------------------
      // 첫 번째 워크시트의 상단 헤더의 각 행별 정보 : 다단 구성, 셀 머지 가능
      // ----------------------------------------------------------------------
      // 첫 번째 헤더 컬럼 : 동일 이름 연속시 셀 머지
      firstHeaderListOfFirstWorksheet = new String[9];
      firstHeaderListOfFirstWorksheet[0]  = "순번";
      firstHeaderListOfFirstWorksheet[1]  = "컨텐츠번호";
      firstHeaderListOfFirstWorksheet[2]  = "타이틀명";
      firstHeaderListOfFirstWorksheet[3]  = "시작일";
      firstHeaderListOfFirstWorksheet[4]  = "종료일";
      firstHeaderListOfFirstWorksheet[5]  = "전시범위";
      firstHeaderListOfFirstWorksheet[6]  = "진행상태";
      firstHeaderListOfFirstWorksheet[7]  = "PC";
      firstHeaderListOfFirstWorksheet[8]  = "MOBILE";
    }
    else if (DisplayEnums.DpContentsTp.BANNER.getCode().equals(contsTp)) {
      // ----------------------------------------------------------------------
      // * 전시컨텐츠유형-BANNER
      // ----------------------------------------------------------------------

      // 파일명
      contsNm = "BANNER";
      excelFileName += contsNm +  "]-" + DateUtil.getCurrentDate();

      // ----------------------------------------------------------------------
      // 컬럼별 width 목록 : 단위 pixel
      //  - ( 필수 아님 ) ExcelWorkSheetDto 에 지정하지 않을 경우 모든 컬럼 너비는 120 pixel 로 고정됨
      // ----------------------------------------------------------------------
      // TODO : size 조정
      widthListOfFirstWorksheet = new Integer[14];
      widthListOfFirstWorksheet[0]  = 150;
      widthListOfFirstWorksheet[1]  = 300;
      widthListOfFirstWorksheet[2]  = 300;
      widthListOfFirstWorksheet[3]  = 300;
      widthListOfFirstWorksheet[4]  = 300;
      widthListOfFirstWorksheet[5]  = 300;
      widthListOfFirstWorksheet[6]  = 300;
      widthListOfFirstWorksheet[7]  = 300;
      widthListOfFirstWorksheet[8]  = 300;
      widthListOfFirstWorksheet[9]  = 300;
      widthListOfFirstWorksheet[10] = 300;
      widthListOfFirstWorksheet[11] = 300;
      widthListOfFirstWorksheet[12] = 300;
      widthListOfFirstWorksheet[13] = 300;
      // ----------------------------------------------------------------------
      // 본문 데이터 컬럼별 정렬 목록
      //  - ( 필수 아님 ) ExcelWorkSheetDto 에 지정하지 않을 경우 모든 컬럼 정렬은 "left" (좌측 정렬) 로 고정
      //  - "left", "center", "right", "justify", "distributed" 가 아닌 다른 값 지정시 "left" (좌측 정렬) 로 지정됨
      // ----------------------------------------------------------------------
      alignListOfFirstWorksheet = new String[14];
      alignListOfFirstWorksheet[0]  = "center";
      alignListOfFirstWorksheet[1]  = "center";
      alignListOfFirstWorksheet[2]  = "center";
      alignListOfFirstWorksheet[3]  = "center";
      alignListOfFirstWorksheet[4]  = "center";
      alignListOfFirstWorksheet[5]  = "center";
      alignListOfFirstWorksheet[6]  = "center";
      alignListOfFirstWorksheet[7]  = "center";
      alignListOfFirstWorksheet[8]  = "center";
      alignListOfFirstWorksheet[9]  = "center";
      alignListOfFirstWorksheet[10] = "center";
      alignListOfFirstWorksheet[11] = "center";
      alignListOfFirstWorksheet[12] = "center";
      alignListOfFirstWorksheet[13] = "center";
      // ----------------------------------------------------------------------
      // 본문 데이터 컬럼별 데이터 property 목록
      //  - ( 필수 ) 엑셀 데이터 JSON 변환시 각 json Object 의 key 값과 일치해야 함
      // ----------------------------------------------------------------------
      propertyListOfFirstWorksheet = new String[14];
      propertyListOfFirstWorksheet[0]   = "sort";
      propertyListOfFirstWorksheet[1]   = "dpContsId";
      propertyListOfFirstWorksheet[2]   = "titleNm";
      propertyListOfFirstWorksheet[3]   = "dpStartDt";
      propertyListOfFirstWorksheet[4]   = "dpEndDt";
      propertyListOfFirstWorksheet[5]   = "dpRangeTpNm";
      propertyListOfFirstWorksheet[6]   = "statusNm";
      propertyListOfFirstWorksheet[7]   = "imgPathMobile";
      propertyListOfFirstWorksheet[8]   = "gifImgPathMobile";
      propertyListOfFirstWorksheet[9]   = "imgPathPc";
      propertyListOfFirstWorksheet[10]  = "gifImgPathPc";
      propertyListOfFirstWorksheet[11]  = "text1String";
      propertyListOfFirstWorksheet[12]  = "text2String";
      propertyListOfFirstWorksheet[13]  = "text3String";
      // ----------------------------------------------------------------------
      // 첫 번째 워크시트의 상단 헤더의 각 행별 정보 : 다단 구성, 셀 머지 가능
      // ----------------------------------------------------------------------
      // 첫 번째 헤더 컬럼 : 동일 이름 연속시 셀 머지
      firstHeaderListOfFirstWorksheet = new String[14];
      firstHeaderListOfFirstWorksheet[0]  = "순번";
      firstHeaderListOfFirstWorksheet[1]  = "컨텐츠번호";
      firstHeaderListOfFirstWorksheet[2]  = "타이틀명";
      firstHeaderListOfFirstWorksheet[3]  = "시작일";
      firstHeaderListOfFirstWorksheet[4]  = "종료일";
      firstHeaderListOfFirstWorksheet[5]  = "전시범위";
      firstHeaderListOfFirstWorksheet[6]  = "진행상태";
      firstHeaderListOfFirstWorksheet[7]  = "이미지1";
      firstHeaderListOfFirstWorksheet[8]  = "Mobile gif";
      firstHeaderListOfFirstWorksheet[9]  = "이미지2";
      firstHeaderListOfFirstWorksheet[10] = "PC gif";
      firstHeaderListOfFirstWorksheet[11] = "노출 텍스트1";
      firstHeaderListOfFirstWorksheet[12] = "노출 텍스트2";
      firstHeaderListOfFirstWorksheet[13] = "노출 텍스트3";

    }
    else if (DisplayEnums.DpContentsTp.BRAND.getCode().equals(contsTp)) {
      // ----------------------------------------------------------------------
      // * 전시컨텐츠유형-BRAND
      // ----------------------------------------------------------------------
      // 파일명
      contsNm = "BRAND";
      excelFileName += contsNm +  "]-" + DateUtil.getCurrentDate();

      // ----------------------------------------------------------------------
      // 컬럼별 width 목록 : 단위 pixel
      //  - ( 필수 아님 ) ExcelWorkSheetDto 에 지정하지 않을 경우 모든 컬럼 너비는 120 pixel 로 고정됨
      // ----------------------------------------------------------------------
      // TODO : size 조정
      widthListOfFirstWorksheet = new Integer[11];
      widthListOfFirstWorksheet[0]  = 150;
      widthListOfFirstWorksheet[1]  = 300;
      widthListOfFirstWorksheet[2]  = 300;
      widthListOfFirstWorksheet[3]  = 300;
      widthListOfFirstWorksheet[4]  = 300;
      widthListOfFirstWorksheet[5]  = 300;
      widthListOfFirstWorksheet[6]  = 300;
      widthListOfFirstWorksheet[7]  = 300;
      widthListOfFirstWorksheet[8]  = 300;
      widthListOfFirstWorksheet[9]  = 300;
      widthListOfFirstWorksheet[10] = 300;
      widthListOfFirstWorksheet[11] = 300;
      widthListOfFirstWorksheet[12] = 300;
      // ----------------------------------------------------------------------
      // 본문 데이터 컬럼별 정렬 목록
      //  - ( 필수 아님 ) ExcelWorkSheetDto 에 지정하지 않을 경우 모든 컬럼 정렬은 "left" (좌측 정렬) 로 고정
      //  - "left", "center", "right", "justify", "distributed" 가 아닌 다른 값 지정시 "left" (좌측 정렬) 로 지정됨
      // ----------------------------------------------------------------------
      alignListOfFirstWorksheet = new String[11];
      alignListOfFirstWorksheet[0]  = "center";
      alignListOfFirstWorksheet[1]  = "center";
      alignListOfFirstWorksheet[2]  = "center";
      alignListOfFirstWorksheet[3]  = "center";
      alignListOfFirstWorksheet[4]  = "center";
      alignListOfFirstWorksheet[5]  = "center";
      alignListOfFirstWorksheet[6]  = "center";
      alignListOfFirstWorksheet[7]  = "center";
      alignListOfFirstWorksheet[8]  = "center";
      alignListOfFirstWorksheet[9]  = "center";
      alignListOfFirstWorksheet[10] = "center";
      alignListOfFirstWorksheet[11] = "center";
      alignListOfFirstWorksheet[12] = "center";
      // ----------------------------------------------------------------------
      // 본문 데이터 컬럼별 데이터 property 목록
      //  - ( 필수 ) 엑셀 데이터 JSON 변환시 각 json Object 의 key 값과 일치해야 함
      // ----------------------------------------------------------------------
      propertyListOfFirstWorksheet = new String[11];
      propertyListOfFirstWorksheet[0]   = "sort";
      propertyListOfFirstWorksheet[1]   = "dpContsId";
      propertyListOfFirstWorksheet[2]   = "titleNm";
      propertyListOfFirstWorksheet[3]   = "contsNm";
      propertyListOfFirstWorksheet[4]   = "dpStartDt";
      propertyListOfFirstWorksheet[5]   = "dpEndDt";
      propertyListOfFirstWorksheet[6]   = "dpRangeTpNm";
      propertyListOfFirstWorksheet[7]   = "statusNm";
      propertyListOfFirstWorksheet[8]   = "imgPathMobile";
      propertyListOfFirstWorksheet[9]   = "imgPathPc";
      propertyListOfFirstWorksheet[10]  = "text1String";
      propertyListOfFirstWorksheet[11]  = "text2String";
      propertyListOfFirstWorksheet[12]  = "text3String";
      // ----------------------------------------------------------------------
      // 첫 번째 워크시트의 상단 헤더의 각 행별 정보 : 다단 구성, 셀 머지 가능
      // ----------------------------------------------------------------------
      // 첫 번째 헤더 컬럼 : 동일 이름 연속시 셀 머지
      firstHeaderListOfFirstWorksheet = new String[13];
      firstHeaderListOfFirstWorksheet[0]  = "순번";
      firstHeaderListOfFirstWorksheet[1]  = "컨텐츠번호";
      firstHeaderListOfFirstWorksheet[2]  = "타이틀명";
      firstHeaderListOfFirstWorksheet[3]  = "브랜드명";
      firstHeaderListOfFirstWorksheet[4]  = "시작일";
      firstHeaderListOfFirstWorksheet[5]  = "종료일";
      firstHeaderListOfFirstWorksheet[6]  = "전시범위";
      firstHeaderListOfFirstWorksheet[7]  = "진행상태";
      firstHeaderListOfFirstWorksheet[8]  = "이미지1";
      firstHeaderListOfFirstWorksheet[9]  = "이미지2";
      firstHeaderListOfFirstWorksheet[10] = "노출 텍스트1";
      firstHeaderListOfFirstWorksheet[11] = "노출 텍스트2";
      firstHeaderListOfFirstWorksheet[12] = "노출 텍스트3";

    }
    else if (DisplayEnums.DpContentsTp.CATEGORY.getCode().equals(contsTp)) {
      // ----------------------------------------------------------------------
      // * 전시컨텐츠유형-CATEGORY
      // ----------------------------------------------------------------------

      // 파일명
      contsNm = "CATEGORY";
      excelFileName += contsNm +  "]-" + DateUtil.getCurrentDate();

      // ----------------------------------------------------------------------
      // 컬럼별 width 목록 : 단위 pixel
      //  - ( 필수 아님 ) ExcelWorkSheetDto 에 지정하지 않을 경우 모든 컬럼 너비는 120 pixel 로 고정됨
      // ----------------------------------------------------------------------
      // TODO : size 조정
      widthListOfFirstWorksheet = new Integer[12];
      widthListOfFirstWorksheet[0]  = 150;
      widthListOfFirstWorksheet[1]  = 300;
      widthListOfFirstWorksheet[2]  = 300;
      widthListOfFirstWorksheet[3]  = 300;
      widthListOfFirstWorksheet[4]  = 300;
      widthListOfFirstWorksheet[5]  = 300;
      widthListOfFirstWorksheet[6]  = 300;
      widthListOfFirstWorksheet[7]  = 300;
      widthListOfFirstWorksheet[8]  = 300;
      widthListOfFirstWorksheet[9]  = 300;
      widthListOfFirstWorksheet[10] = 300;
      widthListOfFirstWorksheet[11] = 300;
      // ----------------------------------------------------------------------
      // 본문 데이터 컬럼별 정렬 목록
      //  - ( 필수 아님 ) ExcelWorkSheetDto 에 지정하지 않을 경우 모든 컬럼 정렬은 "left" (좌측 정렬) 로 고정
      //  - "left", "center", "right", "justify", "distributed" 가 아닌 다른 값 지정시 "left" (좌측 정렬) 로 지정됨
      // ----------------------------------------------------------------------
      alignListOfFirstWorksheet = new String[12];
      alignListOfFirstWorksheet[0]  = "center";
      alignListOfFirstWorksheet[1]  = "center";
      alignListOfFirstWorksheet[2]  = "center";
      alignListOfFirstWorksheet[3]  = "center";
      alignListOfFirstWorksheet[4]  = "center";
      alignListOfFirstWorksheet[5]  = "center";
      alignListOfFirstWorksheet[6]  = "center";
      alignListOfFirstWorksheet[7]  = "center";
      alignListOfFirstWorksheet[8]  = "center";
      alignListOfFirstWorksheet[9]  = "center";
      alignListOfFirstWorksheet[10] = "center";
      alignListOfFirstWorksheet[11] = "center";
      // ----------------------------------------------------------------------
      // 본문 데이터 컬럼별 데이터 property 목록
      //  - ( 필수 ) 엑셀 데이터 JSON 변환시 각 json Object 의 key 값과 일치해야 함
      // ----------------------------------------------------------------------
      propertyListOfFirstWorksheet = new String[12];
      propertyListOfFirstWorksheet[0]   = "sort";
      propertyListOfFirstWorksheet[1]   = "dpContsId";
      propertyListOfFirstWorksheet[2]   = "titleNm";
      propertyListOfFirstWorksheet[3]   = "dpStartDt";
      propertyListOfFirstWorksheet[4]   = "dpEndDt";
      propertyListOfFirstWorksheet[5]   = "dpRangeTpNm";
      propertyListOfFirstWorksheet[6]   = "statusNm";
      propertyListOfFirstWorksheet[7]   = "mallDivNm";
      propertyListOfFirstWorksheet[8]   = "ctgryFullNm";
      propertyListOfFirstWorksheet[9]   = "text1String";
      propertyListOfFirstWorksheet[10]  = "text2String";
      propertyListOfFirstWorksheet[11]  = "text3String";
      // ----------------------------------------------------------------------
      // 첫 번째 워크시트의 상단 헤더의 각 행별 정보 : 다단 구성, 셀 머지 가능
      // ----------------------------------------------------------------------
      // 첫 번째 헤더 컬럼 : 동일 이름 연속시 셀 머지
      firstHeaderListOfFirstWorksheet = new String[12];
      firstHeaderListOfFirstWorksheet[0]  = "순번";
      firstHeaderListOfFirstWorksheet[1]  = "컨텐츠번호";
      firstHeaderListOfFirstWorksheet[2]  = "타이틀명";
      firstHeaderListOfFirstWorksheet[3]  = "시작일";
      firstHeaderListOfFirstWorksheet[4]  = "종료일";
      firstHeaderListOfFirstWorksheet[5]  = "전시범위";
      firstHeaderListOfFirstWorksheet[6]  = "진행상태";
      firstHeaderListOfFirstWorksheet[7]  = "몰인몰구분";
      firstHeaderListOfFirstWorksheet[8]  = "전시카테고리";
      firstHeaderListOfFirstWorksheet[9]  = "노출 텍스트1";
      firstHeaderListOfFirstWorksheet[10] = "노출 텍스트2";
      firstHeaderListOfFirstWorksheet[11] = "노출 텍스트3";

    }
    else if (DisplayEnums.DpContentsTp.GOODS.getCode().equals(contsTp)) {
      // ----------------------------------------------------------------------
      // * 전시컨텐츠유형-GOODS
      // ----------------------------------------------------------------------

      // 파일명
      contsNm = "GOODS";
      excelFileName += contsNm +  "]-" + DateUtil.getCurrentDate();

      // ----------------------------------------------------------------------
      // 컬럼별 width 목록 : 단위 pixel
      //  - ( 필수 아님 ) ExcelWorkSheetDto 에 지정하지 않을 경우 모든 컬럼 너비는 120 pixel 로 고정됨
      // ----------------------------------------------------------------------
      // TODO : size 조정
      widthListOfFirstWorksheet = new Integer[25];
      widthListOfFirstWorksheet[0]  = 150;
      widthListOfFirstWorksheet[1]  = 300;
      widthListOfFirstWorksheet[2]  = 300;
      widthListOfFirstWorksheet[3]  = 300;
      widthListOfFirstWorksheet[4]  = 300;
      widthListOfFirstWorksheet[5]  = 300;
      widthListOfFirstWorksheet[6]  = 300;
      widthListOfFirstWorksheet[7]  = 300;
      widthListOfFirstWorksheet[8]  = 300;
      widthListOfFirstWorksheet[9]  = 300;
      widthListOfFirstWorksheet[10] = 300;
      widthListOfFirstWorksheet[11] = 300;
      widthListOfFirstWorksheet[12] = 300;
      widthListOfFirstWorksheet[13] = 300;
      widthListOfFirstWorksheet[14] = 300;
      widthListOfFirstWorksheet[15] = 300;
      widthListOfFirstWorksheet[16] = 300;
      widthListOfFirstWorksheet[17] = 300;
      widthListOfFirstWorksheet[18] = 300;
      widthListOfFirstWorksheet[19] = 300;
      widthListOfFirstWorksheet[20] = 300;
      widthListOfFirstWorksheet[21] = 300;
      widthListOfFirstWorksheet[22] = 300;
      widthListOfFirstWorksheet[23] = 300;
      widthListOfFirstWorksheet[24] = 300;
      // ----------------------------------------------------------------------
      // 본문 데이터 컬럼별 정렬 목록
      //  - ( 필수 아님 ) ExcelWorkSheetDto 에 지정하지 않을 경우 모든 컬럼 정렬은 "left" (좌측 정렬) 로 고정
      //  - "left", "center", "right", "justify", "distributed" 가 아닌 다른 값 지정시 "left" (좌측 정렬) 로 지정됨
      // ----------------------------------------------------------------------
      alignListOfFirstWorksheet = new String[25];
      alignListOfFirstWorksheet[0]  = "center";
      alignListOfFirstWorksheet[1]  = "center";
      alignListOfFirstWorksheet[2]  = "center";
      alignListOfFirstWorksheet[3]  = "center";
      alignListOfFirstWorksheet[4]  = "center";
      alignListOfFirstWorksheet[5]  = "center";
      alignListOfFirstWorksheet[6]  = "center";
      alignListOfFirstWorksheet[7]  = "center";
      alignListOfFirstWorksheet[8]  = "center";
      alignListOfFirstWorksheet[9]  = "center";
      alignListOfFirstWorksheet[10] = "center";
      alignListOfFirstWorksheet[11] = "center";
      alignListOfFirstWorksheet[12] = "center";
      alignListOfFirstWorksheet[13] = "center";
      alignListOfFirstWorksheet[14] = "center";
      alignListOfFirstWorksheet[15] = "center";
      alignListOfFirstWorksheet[16] = "center";
      alignListOfFirstWorksheet[17] = "center";
      alignListOfFirstWorksheet[18] = "center";
      alignListOfFirstWorksheet[19] = "center";
      alignListOfFirstWorksheet[20] = "center";
      alignListOfFirstWorksheet[21] = "center";
      alignListOfFirstWorksheet[22] = "center";
      alignListOfFirstWorksheet[23] = "center";
      alignListOfFirstWorksheet[24] = "center";
      // ----------------------------------------------------------------------
      // 본문 데이터 컬럼별 데이터 property 목록
      //  - ( 필수 ) 엑셀 데이터 JSON 변환시 각 json Object 의 key 값과 일치해야 함
      // ----------------------------------------------------------------------
      propertyListOfFirstWorksheet = new String[25];
      propertyListOfFirstWorksheet[0]   = "sort";
      propertyListOfFirstWorksheet[1]   = "dpContsId";
      propertyListOfFirstWorksheet[2]   = "titleNm";
      propertyListOfFirstWorksheet[3]   = "contsId";
      propertyListOfFirstWorksheet[4]   = "contsNm";
      propertyListOfFirstWorksheet[5]   = "dpStartDt";
      propertyListOfFirstWorksheet[6]   = "dpEndDt";
      propertyListOfFirstWorksheet[7]   = "dpRangeTpNm";
      propertyListOfFirstWorksheet[8]   = "statusNm";
      propertyListOfFirstWorksheet[9]   = "dpCondTpNm";
      propertyListOfFirstWorksheet[10]  = "dpSortTpNm";
      propertyListOfFirstWorksheet[11]  = "goodsTypeName";
      propertyListOfFirstWorksheet[12]  = "supplierName";
      propertyListOfFirstWorksheet[13]  = "goodsBrandNm";
      propertyListOfFirstWorksheet[14]  = "recommendedPrice";
      propertyListOfFirstWorksheet[15]  = "salePrice";
      propertyListOfFirstWorksheet[16]  = "dispRangeNm";
      propertyListOfFirstWorksheet[17]  = "purchaseRangeNm";
      propertyListOfFirstWorksheet[18]  = "goodsBasicCtgryFullNm";
      propertyListOfFirstWorksheet[19]  = "saleStatusName";
      propertyListOfFirstWorksheet[20]  = "dispYnNm";
      propertyListOfFirstWorksheet[21]  = "linkUrlPc";
      propertyListOfFirstWorksheet[22]  = "text1String";
      propertyListOfFirstWorksheet[23]  = "text2String";
      propertyListOfFirstWorksheet[24]  = "text3String";
      // ----------------------------------------------------------------------
      // 첫 번째 워크시트의 상단 헤더의 각 행별 정보 : 다단 구성, 셀 머지 가능
      // ----------------------------------------------------------------------
      // 첫 번째 헤더 컬럼 : 동일 이름 연속시 셀 머지
      firstHeaderListOfFirstWorksheet = new String[25];
      firstHeaderListOfFirstWorksheet[0]  = "순번";
      firstHeaderListOfFirstWorksheet[1]  = "컨텐츠번호";
      firstHeaderListOfFirstWorksheet[2]  = "타이틀명";
      firstHeaderListOfFirstWorksheet[3]  = "상품코드";
      firstHeaderListOfFirstWorksheet[4]  = "상품명";
      firstHeaderListOfFirstWorksheet[5]  = "시작일";
      firstHeaderListOfFirstWorksheet[6]  = "종료일";
      firstHeaderListOfFirstWorksheet[7]  = "전시범위";
      firstHeaderListOfFirstWorksheet[8]  = "진행상태";
      firstHeaderListOfFirstWorksheet[9]  = "노출조건";
      firstHeaderListOfFirstWorksheet[10] = "노출순서";
      firstHeaderListOfFirstWorksheet[11] = "상품유형";
      firstHeaderListOfFirstWorksheet[12] = "공급업체";
      firstHeaderListOfFirstWorksheet[13] = "브랜드";
      firstHeaderListOfFirstWorksheet[14] = "정상가";
      firstHeaderListOfFirstWorksheet[15] = "판매가";
      firstHeaderListOfFirstWorksheet[16] = "판매가허용범위";
      firstHeaderListOfFirstWorksheet[17] = "구매허용범위";
      firstHeaderListOfFirstWorksheet[18] = "전시카테고리";
      firstHeaderListOfFirstWorksheet[19] = "판매상태";
      firstHeaderListOfFirstWorksheet[20] = "전시여부";
      firstHeaderListOfFirstWorksheet[21] = "링크URL";
      firstHeaderListOfFirstWorksheet[22] = "노출 텍스트1";
      firstHeaderListOfFirstWorksheet[23] = "노출 텍스트2";
      firstHeaderListOfFirstWorksheet[24] = "노출 텍스트3";

    }
    else {
      // 오류처리
    }

    // ------------------------------------------------------------------------
    // 워크시트 DTO 생성 후 정보 세팅
    // ------------------------------------------------------------------------
    ExcelWorkSheetDto firstWorkSheetDto = ExcelWorkSheetDto.builder()   //
        .workSheetName(excelSheetName)                                  // 엑셀 파일내 워크시트 명
        .propertyList(propertyListOfFirstWorksheet)                     // 컬럼별 데이터 property 목록
        .widthList(widthListOfFirstWorksheet)                           // 컬럼별 너비 목록
        .alignList(alignListOfFirstWorksheet)                           // 컬럼별 정렬 목록
        .build();

    // ------------------------------------------------------------------------
    // 엑셀 다단 헤더 구성 : ( 헤더 행 index , 헤더 제목 배열 ) 형식으로 세팅
    // ------------------------------------------------------------------------
    // 첫 번째 헤더 컬럼
    firstWorkSheetDto.setHeaderList(0, firstHeaderListOfFirstWorksheet);

    // ------------------------------------------------------------------------
    // 엑셀 본문 샘플 데이터 생성 : List<?> 형식만 세팅 가능
    // excelData 를 세팅하지 않으면 샘플 엑셀로 다운로드됨
    // ------------------------------------------------------------------------
    ApiResult<?> resultApi = null;
    List<ContsVo> resultList = null;

    try {

      //resultList = categoryBosService.getCategoryListForExcel(dto);
      resultApi = displayManageBiz.selectDpContsList(displayManageRequestDto);
      //resultList = (List<CategoryExcelVo>) resultApi.getRows();

      //DisplayContsResponseDto resultDto = (DisplayContsResponseDto)resultApi.getRows();
      DisplayContsResponseDto resultDto = (DisplayContsResponseDto)resultApi.getData();
      resultList = (List<ContsVo>)resultDto.getRows();

      // ----------------------------------------------------------------------
      // 상품인경우 판매허용범위/구매허용범위 노출문구 작성
      // ----------------------------------------------------------------------
      if (DisplayEnums.DpContentsTp.GOODS.getCode().equals(contsTp)) {
        if (resultList != null && resultList.size() > 0) {

          String dispYnRangeVal = "";
          int    dispYesCnt     = 0;
          String dispSepStr     = "";

          String purchYnRangeVal = "";
          int    purchYesCnt     = 0;
          String purchSepStr     = "";

          for (ContsVo row : resultList) {

            // ----------------------------------------------------------------
            // 판매허용범위
            // ----------------------------------------------------------------
            dispSepStr = "";
            // PC
            if ("Y".equals(StringUtil.nvl(row.getDispWebPcYn()))) {
              dispYnRangeVal = StringUtil.nvl(row.getDispWebPcYnNm());
              dispYesCnt++;
            }
            // Mobile
            if ("Y".equals(StringUtil.nvl(row.getDispWebMobileYn()))) {
              if (dispYesCnt > 0) {
                dispSepStr = "/";
              }
              dispYnRangeVal += (dispSepStr + StringUtil.nvl(row.getDispWebMobileYnNm()));
              dispYesCnt++;
            }
            // APP
            if ("Y".equals(StringUtil.nvl(row.getDispAppYn()))) {

              if (dispYesCnt > 0) {
                dispSepStr = "/";
              }
              dispYnRangeVal += (dispSepStr + StringUtil.nvl(row.getDispAppYnNm()));
            }
            row.setDispRangeNm(dispYnRangeVal);

            // ----------------------------------------------------------------
            // 구매허용범위
            // ----------------------------------------------------------------
            purchSepStr = "";
            // 일반회원
            if ("Y".equals(StringUtil.nvl(row.getPurchaseMemberYn()))) {
              purchYnRangeVal = StringUtil.nvl(row.getPurchaseMemberYnNm());
              purchYesCnt++;
            }
            // 임직원
            if ("Y".equals(StringUtil.nvl(row.getPurchaseEmployeeYn()))) {
              if (purchYesCnt > 0) {
                purchSepStr = "/";
              }
              purchYnRangeVal += (purchSepStr + StringUtil.nvl(row.getPurchaseEmployeeYnNm()));
              purchYesCnt++;
            }
            // 비회원
            if ("Y".equals(StringUtil.nvl(row.getPurchaseNonmemberYn()))) {

              if (purchYesCnt > 0) {
                purchSepStr = "/";
              }
              purchYnRangeVal += (purchSepStr + StringUtil.nvl(row.getPurchaseNonmemberYnNm()));
            }
            row.setPurchaseRangeNm(purchYnRangeVal);

          } // End of for (ContsVo row : resultList)
        } // End of if (resultList != null && resultList.size() > 0)
      } // End of if (DisplayEnums.DpContentsTp.GOODS.getCode().equals(contsTp))

    } catch (Exception e) {
        log.error(e.getMessage());
        throw e; // 추후 CustomException 으로 변환 예정
    }
    firstWorkSheetDto.setExcelDataList(resultList);

    // xcelDownloadDto 생성 후 workSheetDto 추가
    ExcelDownloadDto excelDownloadDto = ExcelDownloadDto.builder().excelFileName(excelFileName).build();

    excelDownloadDto.addExcelWorkSheet(firstWorkSheetDto);

    // ========================================================================
    // # 반환
    // ========================================================================
    modelAndView.addObject(ExcelDownloadView.excelDownloadDtoKeyInModel, excelDownloadDto);

    // ========================================================================
    // # 반환
    // ========================================================================
    return modelAndView;
  }

  /**
   * 전시컨텐츠 리스트조회
   * @param depth
   * @param useAllYn
   * @return
   * @throws Exception
   */
  public ApiResult<?> selectDpContsList (DisplayManageRequestDto displayManageRequestDto) throws BaseException {

    return displayManageBiz.selectDpContsList(displayManageRequestDto);
  }

  /**
   * 컨텐츠 상세조회
   * @param dpPageId
   * @return
   * @throws Exception
   */
  public ApiResult<?> selectDpContsInfo (DisplayManageRequestDto displayManageRequestDto) throws BaseException {

    return displayManageBiz.selectDpContsInfo(displayManageRequestDto);
  }

  /**
   * 컨텐츠 수정
   * @param pageVo
   * @return
   * @throws Exception
   */
  public ApiResult<?> putConts (DisplayManageRequestDto displayManageRequestDto) throws BaseException {

    return displayManageBiz.putConts(displayManageRequestDto);
  }

  /**
   * 컨텐츠 순서변경
   * @param pageVoList
   * @return
   * @throws Exception
   */
  public ApiResult<?> putContsSort (DisplayManageRequestDto displayManageRequestDto) throws BaseException {

    return displayManageBiz.putContsSort(displayManageRequestDto);
  }

  /**
   * 컨텐츠 삭제
   * @param pageVoList
   * @return
   * @throws Exception
   */
  public ApiResult<?> delConts (DisplayManageRequestDto displayManageRequestDto) throws BaseException {

    return displayManageBiz.delConts(displayManageRequestDto);
  }

  /**
   * 컨텐츠 등록
   * @param pageVo
   * @return
   * @throws Exception
   */
  public ApiResult<?> addConts (DisplayManageRequestDto displayManageRequestDto) throws BaseException {

    return displayManageBiz.addConts(displayManageRequestDto);
  }

  /**
   * 브랜드 리스트조회(콤보용)
   * @param depth
   * @param useAllYn
   * @return
   * @throws Exception
   */
  public ApiResult<?> selectBrandList (DisplayManageRequestDto displayManageRequestDto) throws BaseException {

    return displayManageBiz.selectBrandList(displayManageRequestDto);
  }

  /**
   * 상품목록조회-키워드조회
   * @param depth
   * @param useAllYn
   * @return
   * @throws Exception
   */
  public ApiResult<?> selectGoodsListByKeyword (DisplayManageRequestDto displayManageRequestDto) throws BaseException {

    return displayManageBiz.selectGoodsListByKeyword(displayManageRequestDto);
  }

  /**
   * 상품목록조회-복수조건조회
   * @param depth
   * @param useAllYn
   * @return
   * @throws Exception
   */
  public ApiResult<?> getGoodsList (GoodsListRequestDto goodsListRequestDto) throws BaseException {

    return goodsListBiz.getGoodsList(goodsListRequestDto);
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
  public ApiResult<?> selectDpInventoryGroupList (DisplayManageRequestDto displayManageRequestDto) throws BaseException {

    return displayManageBiz.selectDpInventoryGroupList(displayManageRequestDto);
  }

  /**
   * 인벤토리그룹 인벤토리 리스트조회
   * @param displayManageRequestDto
   * @return
   * @throws BaseException
   */
  public ApiResult<?> selectDpGroupInventoryList (DisplayManageRequestDto displayManageRequestDto) throws BaseException {

     return displayManageBiz.selectDpGroupInventoryList(displayManageRequestDto);
  }

  /**
   * 인벤토리그룹구성 리스트조회
   * @param displayManageRequestDto
   * @return
   * @throws BaseException
   */
  public ApiResult<?> selectDpGroupInventoryMappingList (DisplayManageRequestDto displayManageRequestDto) throws BaseException {

    return displayManageBiz.selectDpGroupInventoryMappingList(displayManageRequestDto);
  }

  /**
   * 인벤토리그룹 등록
   * @param pageVo
   * @return
   * @throws Exception
   */
  public ApiResult<?> addInventoryGroup (DisplayManageRequestDto displayManageRequestDto) throws BaseException {

    return displayManageBiz.addInventoryGroup(displayManageRequestDto);
  }

  /**
   * 인벤토리그룹 수정
   * @param pageVo
   * @return
   * @throws Exception
   */
  public ApiResult<?> putInventoryGroup (DisplayManageRequestDto displayManageRequestDto) throws BaseException {

    return displayManageBiz.putInventoryGroup(displayManageRequestDto);
  }

  /**
   * 인벤토리그룹 삭제
   * @param pageVoList
   * @return
   * @throws Exception
   */
  public ApiResult<?> delInventoryGroup (DisplayManageRequestDto displayManageRequestDto) throws BaseException {

    return displayManageBiz.delInventoryGroup(displayManageRequestDto);
  }

  /**
   * 인벤토리그룹 순서변경
   * @param displayManageRequestDto
   * @return
   * @throws BaseException
   */
  public ApiResult<?> putInventoryGroupSort (DisplayManageRequestDto displayManageRequestDto) throws BaseException {

    return displayManageBiz.putInventoryGroupSort(displayManageRequestDto);
  }


}
