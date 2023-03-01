package kr.co.pulmuone.bos.statics.sale;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.constants.Constants;
import kr.co.pulmuone.v1.comm.enums.StaticsEnums.SaleSataticsMessage;
import kr.co.pulmuone.v1.comm.framework.dto.ExcelDownloadDto;
import kr.co.pulmuone.v1.comm.framework.dto.ExcelWorkSheetDto;
import kr.co.pulmuone.v1.comm.framework.view.ExcelDownloadView;
import kr.co.pulmuone.v1.comm.util.BindUtil;
import kr.co.pulmuone.v1.comm.util.DateUtil;
import kr.co.pulmuone.v1.comm.util.StringUtil;
import kr.co.pulmuone.v1.statics.sale.dto.SaleStaticsRequestDto;
import kr.co.pulmuone.v1.statics.sale.dto.SaleStaticsResponseDto;
import kr.co.pulmuone.v1.statics.sale.dto.vo.SaleStaticsVo;
import kr.co.pulmuone.v1.statics.sale.service.SaleStaticsBiz;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
* <PRE>
* Forbiz Korea
* 통계관리 매출통계 BOS Controller
*
* </PRE>
*
* <PRE>
* <B>History:</B>
* =======================================================================
*  버전  :   작성일                :  작성자      :  작성내역
* -----------------------------------------------------------------------
*  1.0       2021.03.19.              dgyoun         최초작성
* =======================================================================
* </PRE>
*/

@Slf4j
@RestController
public class SaleStaticsController {

  final String SINGLE_SECTION = "singleSection";
  final String MULTI_SECTION  = "multiSection";

  //@SuppressWarnings("unused")
  //@Autowired(required = true)
  //private HttpServletRequest request;

  @Autowired(required=true)
  private HttpServletRequest request;

  @Autowired
  private SaleStaticsBiz saleStaticsBiz;

  @Autowired
  private ExcelDownloadView excelDownloadView; // 엑셀 다운로드 뷰

  /**
   * 판매현황통계 리스트 조회
   * @param saleStaticsRequestDto
   * @return
   * @throws Exception
   */
  @RequestMapping(value="/admin/statics/sale/selectSaleStaticsList")
  @ApiOperation(value = "판매현황통계")
  @ApiResponses(value = { @ApiResponse(code = 900, message = "response data", response = SaleStaticsResponseDto.class),
                          @ApiResponse(code = 901, message = ""
                              + "SALE_STATICS_PARAM_NO_INPUT - 입력정보가 존재하지 않습니다."
                              + "SALE_STATICS_PARAM_NO_SEARCH_TP - 검색기준이 존재하지 않습니다."
                          )
  })
  @ResponseBody
  public ApiResult<?> selectSaleStaticsList(SaleStaticsRequestDto saleStaticsRequestDto) throws Exception {
    log.debug("# ######################################");
    log.debug("# SaleStaticsController.selectSaleStaticsList Start");
    log.debug("# ######################################");
    if (saleStaticsRequestDto != null) {
      log.debug("# In.saleStaticsRequestDto     :: " + saleStaticsRequestDto.toString());
    }
    else {
      log.debug("# In.saleStaticsRequestDto is Null");
    }

    // ========================================================================
    // # 초기화
    // ========================================================================
    // # 입력값 체크
    if (saleStaticsRequestDto == null) {
      // 입력정보가 존재하지 않습니다.
      return ApiResult.result(SaleSataticsMessage.SALE_STATICS_PARAM_NO_INPUT);
    }
    //if (StringUtil.isEmpty(saleStaticsRequestDto.getSearchTp())) {
    //  // 검색기준이 존재하지 않습니다
    //  return ApiResult.result(SaleSataticsMessage.SALE_STATICS_PARAM_NO_SEARCH_TP);
    //}

    // ------------------------------------------------------------------------
    // 조회조건 Set - filter
    // ------------------------------------------------------------------------
    // 조회조건 filter
    SaleStaticsRequestDto reqDto = genParamSaleGoodsStaticsList(request, null);
    log.debug("# SaleStaticsController.reqDto[2] :: " + reqDto.toString());

    // ========================================================================
    // # 처리 및 반환
    // ========================================================================
    // # BOS 서비스 호출
    return saleStaticsBiz.selectSaleStaticsList(reqDto);

  }

  /**
   * 상품별 판매현황통계 리스트 조회
   * @param saleStaticsRequestDto
   * @return
   * @throws Exception
   */
  @RequestMapping(value="/admin/statics/sale/selectSaleGoodsStaticsList")
  @ApiOperation(value = "상품별판매현황통계")
  @ApiResponses(value = { @ApiResponse(code = 900, message = "response data", response = SaleStaticsResponseDto.class),
                          @ApiResponse(code = 901, message = ""
                              + "SALE_STATICS_PARAM_NO_INPUT - 입력정보가 존재하지 않습니다."
                              + "SALE_STATICS_PARAM_NO_SEARCH_TP - 검색기준이 존재하지 않습니다."
                          )
  })
  @ResponseBody
  public ApiResult<?> selectSaleGoodsStaticsList(SaleStaticsRequestDto saleStaticsRequestDto) throws Exception {
    log.debug("# ######################################");
    log.debug("# SaleStaticsController.selectSaleGoodsStaticsList Start");
    log.debug("# ######################################");
    if (saleStaticsRequestDto != null) {
      log.debug("# In.saleStaticsRequestDto     :: " + saleStaticsRequestDto.toString());
    }
    else {
      log.debug("# In.saleStaticsRequestDto is Null");
    }

    // ========================================================================
    // # 초기화
    // ========================================================================
    // # 입력값 체크
    if (saleStaticsRequestDto == null) {
      // 입력정보가 존재하지 않습니다.
      return ApiResult.result(SaleSataticsMessage.SALE_STATICS_PARAM_NO_INPUT);
    }
    //if (StringUtil.isEmpty(saleStaticsRequestDto.getSearchTp())) {
    //  // 검색기준이 존재하지 않습니다
    //  return ApiResult.result(SaleSataticsMessage.SALE_STATICS_PARAM_NO_SEARCH_TP);
    //}

    // ------------------------------------------------------------------------
    // 조회조건 Set - filter
    // ------------------------------------------------------------------------
    // 조회조건 filter
    SaleStaticsRequestDto reqDto = genParamSaleGoodsStaticsList(request, null);
    log.debug("# SaleStaticsController.reqDto[2] :: " + reqDto.toString());

    // ========================================================================
    // # 처리 및 반환
    // ========================================================================
    // # BOS 서비스 호출
    return saleStaticsBiz.selectSaleGoodsStaticsList(reqDto);

  }


  /**
   * 판매현황 통계 엑셀다운로드
   * @param saleStaticsRequestDto
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  @SuppressWarnings({ "unchecked" })
  @RequestMapping(value="/admin/statics/sale/getExportExcelSaleStaticsList")
  @ApiOperation(value = "상품별판매현황통계엑셀다운로드")
  public ModelAndView getExportExcelSaleStaticsList(@RequestBody SaleStaticsRequestDto saleStaticsRequestDto, HttpServletRequest request, HttpServletResponse response) throws Exception {
    log.debug("# ######################################");
    log.debug("# SaleStaticsController.getExportExcelSaleGoodsStaticsList Start");
    log.debug("# ######################################");
    log.debug("# saleStaticsRequestDto :: " + saleStaticsRequestDto.toString());

    // ========================================================================
    // # 초기화
    // ========================================================================
    ApiResult<?> resultApi = null;
    List<SaleStaticsVo> resultList = new ArrayList<SaleStaticsVo>();

    // ------------------------------------------------------------------------
    // 조회조건 Set - filter
    // ------------------------------------------------------------------------
    // 조회조건 filter
    SaleStaticsRequestDto reqDto = genParamSaleGoodsStaticsList(request, saleStaticsRequestDto);

    // ------------------------------------------------------------------------
    // 엑셀 레이아웃 구성 - 데이터
    // ------------------------------------------------------------------------
    // 엑셀 파일 이름: 확장자는 xlsx 자동 설정됨
    String excelFileName   = "판매현황통계" + "_" + DateUtil.getCurrentDate();
    // 엑셀 파일 내 워크시트 이름
    String excelSheetName  = "sheet";

    // 컬럼별 width 목록 : 단위 pixel
    // 순번, 공급업체, 판매처그룹, 판매처, 카테고리, 상품명, 정상가, 판매가, 주문상품수량, 총판매금액(VAT포함), 총판매금액(VAT별도), 상품코드, 품목코드, 품목바코드
    Integer[] widthListOfFirstWorksheet = {200, 150, 150, 150, 150, 150, 150, 150, 150, 150, 150, 150};

    // 컬럼별 정렬
    String[] alignListOfFirstWorksheet = {"left", "left", "left", "right", "right", "right", "right", "right", "right", "right", "right", "center"};

    // 컬럼ID
    String[] propertyListOfFirstWorksheet = {"supplierNm", "sellersGroupCdNm", "sellersNm", "standardPaidPriceFm", "standardPaidPriceNonTaxFm", "standardOrderCntFm", "standardGoodsCntFm", "contrastPaidPriceFm", "contrastPaidPriceNonTaxFm", "contrastOrderCntFm", "contrastGoodsCntFm", "stretchRateFm"};

    // 타이틀 컬럼명1
    String[] firstHeaderListOfFirstWorksheet1 = {"공급업체", "판매처그룹", "판매처", "기준기간", "기준기간", "기준기간", "기준기간", "대비기간", "대비기간", "대비기간", "대비기간", "매출금액(VAT포함)신장율"};
    // 타이틀 컬럼명2
    String[] firstHeaderListOfFirstWorksheet2 = {"공급업체", "판매처그룹", "판매처", "매출금액(VAT포함)", "매출금액(VAT별도)", "주문건수", "주문상품수량", "매출금액(VAT포함)", "매출금액(VAT별도)", "주문건수", "주문상품수량", "매출금액(VAT포함)신장율"};

    // 워크시트 DTO 생성 후 정보 세팅
    ExcelWorkSheetDto firstWorkSheetDto = ExcelWorkSheetDto.builder()   //
        .workSheetName(excelSheetName)                                  // 엑셀 파일내 워크시트 명
        .propertyList(propertyListOfFirstWorksheet)                     // 컬럼별 데이터 property 목록
        .widthList(widthListOfFirstWorksheet)                           // 컬럼별 너비 목록
        .alignList(alignListOfFirstWorksheet)                           // 컬럼별 정렬 목록
        .build();

    // ------------------------------------------------------------------------
    // 엑셀 헤더구성
    // ------------------------------------------------------------------------
    // 엑셀 다단 헤더 구성 : ( 헤더 행 index , 헤더 제목 배열 ) 형식으로 세팅
    //
    // ------------------------------------------------------------------------
    // 첫 번째 헤더 컬럼 : 타이틀 (세번째 row가 헤더 타이틀)
    // ------------------------------------------------------------------------
    firstWorkSheetDto.setHeaderList(0, firstHeaderListOfFirstWorksheet1);
    firstWorkSheetDto.setHeaderList(1, firstHeaderListOfFirstWorksheet2);

    // ------------------------------------------------------------------------
    // 상품별 판매현황통계 리스트 조회
    // ------------------------------------------------------------------------
    resultApi = saleStaticsBiz.selectSaleStaticsList(reqDto);

    if (resultApi != null) {
      SaleStaticsResponseDto resDto = (SaleStaticsResponseDto)resultApi.getData();

      if (resDto != null) {

        List<SaleStaticsVo> resultSaleStaticsList = (List<SaleStaticsVo>)resDto.getRows();

        if (resultSaleStaticsList != null && resultSaleStaticsList.size() > 0) {
          // ------------------------------------------------------------------
          // 결과데이터 Set
          // ------------------------------------------------------------------
          int i = 0;
          for (SaleStaticsVo vo : resultSaleStaticsList) {
            vo.setNo((i+1)+"");
            vo.setStretchRateFm(vo.getStretchRateFm()+"%");
            i++;
            resultList.add(vo);
          } // End of for (SaleStaticsVo vo : resultSaleStaticsList)
        } // End of if (resultSaleStaticsList != null && resultSaleStaticsList.size() > 0)
      } // End of if (resDto != null)
    } // End of if (resultApi != null)

    // ------------------------------------------------------------------------
    // 엑셀 데이터 Set
    // ------------------------------------------------------------------------
    firstWorkSheetDto.setExcelDataList(resultList);

    // ------------------------------------------------------------------------
    // 로우 머징 옵션 설정
    // ------------------------------------------------------------------------
    // 머징여부
    firstWorkSheetDto.setMergeYn(true); // 머징여부
    // 머징대상 컬럼 Index
    int[] mergeIndexArr = {0, 1};       // 첫번째/두번째 컬럼
    firstWorkSheetDto.setMergeIndexArr(mergeIndexArr);

    // ------------------------------------------------------------------------
    // 조회조건정보 추가
    // ------------------------------------------------------------------------
    List<String> addInfoList = new ArrayList<String>();
    addInfoList.add(reqDto.getSearchInfo());
    addInfoList.add("매출금액(원)/주문건수(건)/주문상품수량(개)/판매금액신장율(%)");
    firstWorkSheetDto.setAddInfoList(addInfoList);

    // xcelDownloadDto 생성 후 workSheetDto 추가
    ExcelDownloadDto excelDownloadDto = ExcelDownloadDto.builder()
        //.excelFileName(URLEncoder.encode(excelFileName, "UTF-8"))
        .excelFileName(excelFileName)
        .build();
    excelDownloadDto.addExcelWorkSheet(firstWorkSheetDto);

    ModelAndView modelAndView = new ModelAndView(excelDownloadView);
    modelAndView.addObject(ExcelDownloadView.excelDownloadDtoKeyInModel, excelDownloadDto);

    return modelAndView;

  }

  /**
   * 상품별 판매현황 통계 엑셀다운로드
   * @param saleStaticsRequestDto
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  @SuppressWarnings({ "unchecked" })
  @RequestMapping(value="/admin/statics/sale/getExportExcelSaleGoodsStaticsList")
  @ApiOperation(value = "상품별판매현황통계엑셀다운로드")
  public ModelAndView getExportExcelSaleGoodsStaticsList(@RequestBody SaleStaticsRequestDto saleStaticsRequestDto, HttpServletRequest request, HttpServletResponse response) throws Exception {
    log.debug("# ######################################");
    log.debug("# SaleStaticsController.getExportExcelSaleGoodsStaticsList Start");
    log.debug("# ######################################");
    log.debug("# saleStaticsRequestDto :: " + saleStaticsRequestDto.toString());

    // ========================================================================
    // # 초기화
    // ========================================================================
    ApiResult<?> resultApi = null;
    List<SaleStaticsVo> resultList = new ArrayList<SaleStaticsVo>();

    // ------------------------------------------------------------------------
    // 조회조건 Set - filter
    // ------------------------------------------------------------------------
    // 조회조건 filter
    SaleStaticsRequestDto reqDto = genParamSaleGoodsStaticsList(request, saleStaticsRequestDto);

    // ------------------------------------------------------------------------
    // 엑셀 레이아웃 구성 - 데이터
    // ------------------------------------------------------------------------
    // 엑셀 파일 이름: 확장자는 xlsx 자동 설정됨
    String excelFileName   = "상품별 판매현황통계" + "_" + DateUtil.getCurrentDate();
    // 엑셀 파일 내 워크시트 이름
    String excelSheetName  = "sheet";

    // 컬럼별 width 목록 : 단위 pixel
    // 순번, 공급업체, 판매처그룹, 판매처, 카테고리, 상품명, 정상가, 판매가, 주문상품수량, 총판매금액(VAT포함), 총판매금액(VAT별도), 상품코드, 품목코드, 품목바코드
    Integer[] widthListOfFirstWorksheet = {80, 200, 150, 150, 100, 200, 200,200,200,400, 100, 100, 100, 80, 120, 120, 120, 150, 150};

    // 컬럼별 정렬
    String[] alignListOfFirstWorksheet = {"center", "center", "center", "center", "center", "left","left","left","left", "center", "left", "right", "right", "right", "right", "right", "center", "center", "center"};

    // 컬럼ID
    String[] propertyListOfFirstWorksheet = {"no", "supplierNm", "sellersGroupCdNm", "sellersNm", "buyerTypeNm", "ctgryNmEx1","ctgryNmEx2","ctgryNmEx3","ctgryNmEx4", "goodsNm", "taxTypeNm", "recommendedPriceFm", "salePriceFm", "goodsCntFm", "taxPaidPriceFm", "nonTaxPaidPriceFm", "ilGoodsId", "ilItemCd", "itemBarcode"};

    // 컬럼명
    String[] firstHeaderListOfFirstWorksheet = {"순번", "공급업체", "판매처그룹", "판매처", "회원유형", "카테고리1","카테고리2","카테고리3","카테고리4", "상품명", "과세구분", "정상가", "판매가", "주문상품수량", "총매출금액\n(VAT포함)", "총매출금액(VAT별도)", "상품코드", "품목코드", "품목바코드"};

    // 워크시트 DTO 생성 후 정보 세팅
    ExcelWorkSheetDto firstWorkSheetDto = ExcelWorkSheetDto.builder()   //
        .workSheetName(excelSheetName)                                  // 엑셀 파일내 워크시트 명
        .propertyList(propertyListOfFirstWorksheet)                     // 컬럼별 데이터 property 목록
        .widthList(widthListOfFirstWorksheet)                           // 컬럼별 너비 목록
        .alignList(alignListOfFirstWorksheet)                           // 컬럼별 정렬 목록
        .build();

    // ------------------------------------------------------------------------
    // 엑셀 헤더구성
    // ------------------------------------------------------------------------
    // 엑셀 다단 헤더 구성 : ( 헤더 행 index , 헤더 제목 배열 ) 형식으로 세팅
    //
    // ------------------------------------------------------------------------
    // 첫 번째 헤더 컬럼 : 타이틀 (세번째 row가 헤더 타이틀)
    // ------------------------------------------------------------------------
    firstWorkSheetDto.setHeaderList(0, firstHeaderListOfFirstWorksheet);

    // ------------------------------------------------------------------------
    // 상품별 판매현황통계 리스트 조회
    // ------------------------------------------------------------------------
    resultApi = saleStaticsBiz.selectSaleGoodsStaticsList(reqDto);

    if (resultApi != null) {
      SaleStaticsResponseDto resDto = (SaleStaticsResponseDto)resultApi.getData();

      if (resDto != null) {

        List<SaleStaticsVo> resultSaleStaticsList = (List<SaleStaticsVo>)resDto.getRows();

        if (resultSaleStaticsList != null && resultSaleStaticsList.size() > 0) {
          // ------------------------------------------------------------------
          // 결과데이터 Set
          // ------------------------------------------------------------------
          int i = 0;
          for (SaleStaticsVo vo : resultSaleStaticsList) {
            vo.setNo((i+1)+"");
            i++;
            resultList.add(vo);
          } // End of for (SaleStaticsVo vo : resultSaleStaticsList)
        } // End of if (resultSaleStaticsList != null && resultSaleStaticsList.size() > 0)
      } // End of if (resDto != null)
    } // End of if (resultApi != null)

    // ------------------------------------------------------------------------
    // 엑셀 데이터 Set
    // ------------------------------------------------------------------------
    firstWorkSheetDto.setExcelDataList(resultList);

    // ------------------------------------------------------------------------
    // 조회조건정보 추가
    // ------------------------------------------------------------------------
    List<String> addInfoList = new ArrayList<String>();
    addInfoList.add(reqDto.getSearchInfo());
    addInfoList.add("정상가/판매가/판매금액(단위: 원), 주문상품수량(단위: 개)");
    firstWorkSheetDto.setAddInfoList(addInfoList);

    // xcelDownloadDto 생성 후 workSheetDto 추가
    ExcelDownloadDto excelDownloadDto = ExcelDownloadDto.builder()
        //.excelFileName(URLEncoder.encode(excelFileName, "UTF-8"))
        .excelFileName(excelFileName)
        .build();
    excelDownloadDto.addExcelWorkSheet(firstWorkSheetDto);

    ModelAndView modelAndView = new ModelAndView(excelDownloadView);
    modelAndView.addObject(ExcelDownloadView.excelDownloadDtoKeyInModel, excelDownloadDto);

    return modelAndView;

  }


  // ##########################################################################
  // # Private
  // ##########################################################################
  /**
   * 상품별 판매현황통계 리스트 조회 파라메터 생성
   * @param request
   * @return
   * @throws Exception
   */
  @SuppressWarnings("unused")
  private SaleStaticsRequestDto genParamSaleGoodsStaticsList(HttpServletRequest request, SaleStaticsRequestDto saleStaticsRequestDto)  throws Exception {
    log.debug("# ######################################");
    log.debug("# SaleStaticsController.genParamSaleGoodsStaticsList Start");
    log.debug("# ######################################");

    // ------------------------------------------------------------------------
    // 조회조건 Set - filter
    // ------------------------------------------------------------------------
    SaleStaticsRequestDto reqDto  = null;

    // 조회조건 filter
    if (saleStaticsRequestDto == null) {
      reqDto  = (SaleStaticsRequestDto) BindUtil.convertRequestToObject(request, SaleStaticsRequestDto.class);
    }
    else {
      reqDto = saleStaticsRequestDto;
    }
    log.debug("# SaleStaticsController.reqDto[1] :: " + reqDto.toString());

    if (StringUtil.isEquals(reqDto.getSelectConditionType(), SINGLE_SECTION)) {
      // ----------------------------------------------------------------------
      // 단일조건 검색
      // ----------------------------------------------------------------------
      // 검색 키워드
      if (StringUtil.isNotEmpty(reqDto.getKeyword())) {

        List<String> keywordList = new ArrayList<String>();
        // 화면에서 전송한 품목코드 목록 문자열에서 공백 모두 제거 / 엔터 줄바꿈을 모두 "," 로 치환
        String keywordListStr = reqDto.getKeyword().replaceAll("\\p{Z}", "").replaceAll("(\r\n|\r|\n|\n\r)", ",");
        String regExp = "^[0-9]+$";
        String[] keywordArray = keywordListStr.split(",+");
        for(int i = 0; i < keywordArray.length; i++) {
          String kewywordVal = keywordArray[i];
          log.debug("# kewywordVal["+i+"] :: " + kewywordVal);
          if(kewywordVal.isEmpty()) {
            continue;
          }
          //if(kewywordVal.matches(regExp)) {
          keywordList.add(kewywordVal);
            //reqDto.getKeywordList().add(String.valueOf(Long.valueOf(kewywordVal)));
          //}
          //else {
          //  keywordList.add(kewywordVal);
          //  //reqDto.getKeywordList().add(kewywordVal);
          //}
        }
        reqDto.setKeyword(keywordListStr);
        reqDto.setKeywordList(keywordList);
      }
    }
    else {
      // ----------------------------------------------------------------------
      // 복수조건 검색
      // ----------------------------------------------------------------------
      // ------------------------------------------------------------------------
      // 코드값에 ALL 문자열이 포함되어 있어서  indexOf("ALL") != 0 으로 둠, 이 경우 ALL은 항상 맨 먼저 나타나게 해야 함
      // 참고
      //  1) indexOf("ALL") < 0 => ALL 이 아닌 경우
      //  2) indexOf("ALL") > 0 => ALL 이 아니지만, MALL_DIV에서 ALL을 인식한 경우
      // ------------------------------------------------------------------------
      // 판매채널유형(ListString -> List<String>)
      if(StringUtil.isNotEmpty(reqDto.getAgentTypeCd()) && reqDto.getAgentTypeCd().indexOf("ALL") != 0 ) {

        reqDto.setAgentTypeCdList(Stream.of(reqDto.getAgentTypeCd().split(Constants.ARRAY_SEPARATORS))
            .map(String::trim)
            .filter( x -> StringUtils.isNotEmpty(x) && !"ALL".equalsIgnoreCase(x) )
            .collect(Collectors.toList()));
      }

      // 회원유형(ListString -> List<String>)
      if(StringUtil.isNotEmpty(reqDto.getBuyerTypeCd()) && reqDto.getBuyerTypeCd().indexOf("ALL") != 0 ) {

        reqDto.setBuyerTypeCdList(Stream.of(reqDto.getBuyerTypeCd().split(Constants.ARRAY_SEPARATORS))
                .map(String::trim)
                .filter( x -> StringUtils.isNotEmpty(x) && !"ALL".equalsIgnoreCase(x) )
                .collect(Collectors.toList()));
      }

      // 상품유형(ListString -> List<String>)
      if(StringUtil.isNotEmpty(reqDto.getGoodsTpCd()) && reqDto.getGoodsTpCd().indexOf("ALL") != 0 ) {

        reqDto.setGoodsTpCdList(Stream.of(reqDto.getGoodsTpCd().split(Constants.ARRAY_SEPARATORS))
            .map(String::trim)
            .filter( x -> StringUtils.isNotEmpty(x) && !"ALL".equalsIgnoreCase(x) )
            .collect(Collectors.toList()));
      }

      // 진행기간-시작일자
      if (StringUtil.isNotEmpty(reqDto.getStartDe())) {
        reqDto.setStartDt(reqDto.getStartDe() + "000000");
      }
      // 진행기간-종료일자
      if (StringUtil.isNotEmpty(reqDto.getEndDe())) {
        reqDto.setEndDt(reqDto.getEndDe() + "235959");
      }
      // 진행기간-시작일자
      if (StringUtil.isNotEmpty(reqDto.getContrastStartDe())) {
        reqDto.setContrastStartDt(reqDto.getContrastStartDe() + "000000");
      }
      // 진행기간-종료일자
      if (StringUtil.isNotEmpty(reqDto.getContrastEndDe())) {
        reqDto.setContrastEndDt(reqDto.getContrastEndDe() + "235959");
      }
    }

    return reqDto;
  }



}
