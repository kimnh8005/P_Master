/**-----------------------------------------------------------------------------
 * description      : 전시관리 - 전시 콘텐츠 설정
 * @
 * @ 수정일        수정자        수정내용
 * @ ------------------------------------------------------
 * @ 2020.10.15    dgyoun        최초생성
 * @
 * **/
'use strict';

var MAX_SORT_LENGTH = 6;
var allowedImageExtensionList = ['.jpg', '.jpeg', '.png', '.gif', '.JPG', '.JPEG', '.PNG', '.GIF']; // 업로드 가능한 이미지 확장자 목록
var treeView, parentNode, dataSource;

var pageSchTp           = 'PAGE_SCH_TP.INVENTORY';  // 페이지검색유형(기본값:인벤토리검색)
var pageTp              = 'PAGE_TP.PAGE';           // 페이지유형(기본값:페이지코너별)
var mallDivision        = 'MALL_DIV.PULMUONE';      // 몰구분(기본값:풀무원)
var selectedPageData;                               // 선택된페이지데이터
var selectedPageUid;                                // 선택된 페이지그리드 UID
var selectedDpPageId;                               // 선택된페이지ID
var selectedInventoryData;                          // 선택된인벤토리데이터
var selectedContsTp;                                // 신규생성/수정 버튼 클릭 시 콘텐츠유형
var selectedContsData;
var selectedContsLevel;                             // 선택한콘텐츠레벨(신규/수정 버튼)
var selectedContsLv1Data;                           // 선택된콘텐츠Lv1데이터
var selectedContsLv2Data;                           // 선택된콘텐츠Lv1데이터
var selectedContsLv3Data;                           // 선택된콘텐츠Lv1데이터
var selectedContsDelArr;                            // 선택된삭제대상콘텐츠Arr
var selectedInventoryGroupData;                     // 선택된인벤토리그룹데이터
var selectedInventoryGroupDelArr;                   // 선택된삭제대상콘텐츠Arr-인벤토리그룹
var selectedDelContsJsonStringData;
var useAllYn  = 'N';                                // 미사용포함여부(기본값:N)
var dpPageId;
var PAGE_SIZE = 20;
//var selectedPageDepth   = 0;                      // 페이지depth

// 그리드
var pageGridDs, pageGridOpt, pageGrid;
var inventoryGridDs, inventoryGridOpt, inventoryGrid;
var contsLv1GridDs, contsLv1GridOpt, contsLv1Grid;
var contsLv1GridOptText, contsLv1GridOptHtml, contsLv1GridOptCategory, contsLv1GridOptBanner, contsLv1GridOptBrand, contsLv1GridOptGoods;
var contsLv2GridDs, contsLv2GridOpt, contsLv2Grid;
var contsLv2GridOptText, contsLv2GridOptHtml, contsLv2GridOptCategory, contsLv2GridOptBanner, contsLv2GridOptBrand, contsLv2GridOptGoods;
var contsLv3GridDs, contsLv3GridOpt, contsLv3Grid;
var contsLv3GridOptText, contsLv3GridOptHtml, contsLv3GridOptCategory, contsLv3GridOptBanner, contsLv3GridOptBrand, contsLv3GridOptGoods;
var goodsGridDs, goodsGridOpt, goodsGrid;
var inventoryGroupGridDs, inventoryGroupGridOpt, inventoryGroupGrid;

// 날짜/시간
var todayDe;
var baseStartDe;
var baseEndDe;
var afterOneDayDe;
var afterSixDe;

// 파일업로드
var publicStorageUrl;                     // 이미지 업로드되는 public 저장소 url 경로
var bannerImageUploadMaxLimit = 5120000;  // 배너 이미지 첨부 가능 최대 용량 ( 단위 : byte )
var workindEditorId;                      // 상품 상세 기본 정보와 주요 정보 Editor 중 이미지 첨부를 클릭한 에디터 Id
// 파일업로드-배너
var imgPathMobileBanner;        // 이미지1-풀경로
var imgOriginNmMobileBanner;    // 이미지1-원본파일명
var gifImgPathMobileBanner;     // 이미지gif모바일-풀경로
var gifImgOriginNmMobileBanner; // 이미지gif모바일-원본파일명
var imgPathPcBanner;            // 이미지2-풀경로
var imgOriginNmPcBanner;        // 이미지2-원본파일명
var gifImgPathPcBanner;         // 이미지gifPC-풀경로
var gifImgOriginNmPcBanner;     // 이미지gifPC-원본파일명
// 파일업로드-브랜드
var imgPathMobileBrand;        // 이미지1-풀경로
var imgOriginNmMobileBrand;    // 이미지1-원본파일명
var imgPathPcBrand;            // 이미지2-풀경로
var imgOriginNmPcBrand;        // 이미지2-원본파일명

//fileUpload 클래스 명
var wrapper_cn = ".fileUpload__imgWrapper";
var container_cn = ".fileUpload-container";


$(document).ready(function() {

  //Initialize Page Call
  fnInitialize();

  // ==========================================================================
  // # Initialize PageR
  // ==========================================================================
  function fnInitialize() {
    $scope.$emit('fnIsMenu', { flag : 'true' });

    fnPageInfo({
      PG_ID  : 'displayContsMgm',
      callback : fnUI
    });
  }

  // ==========================================================================
  // # fnUI
  // ==========================================================================
  function fnUI() {

       fnTranslate();         // comm.lang.js 안에 있는 공통함수 다국어 변환 --

       fnInitButton();        // Initialize Button  ---------------------------

       fnInitOptionBox();     // Initialize Commbo/Radio/Checkboxk ------------

       fnInitEvent();         // Initialize Event -----------------------------

       fnInitTree();          // Initialize Tree ------------------------------

       fnInitGrid();         // Initialize All Grid --------------------------

       fnBtnClear();          // Data 초기화 ----------------------------------
  }

  // ==========================================================================
  // # Data 초기화
  // ==========================================================================
  function fnBtnClear() {
      // Tree 선택 초기화
      treeView.select($());
      // 입력값 초기화
      fnInitInput();
      // 파일업로드 public 저장소 경로
      publicStorageUrl = fnGetPublicStorageUrl();
      //console.log('# publicStorageUrl >>> :: ', publicStorageUrl);
  }

  // ==========================================================================
  // # 초기화 - 날짜
  // ==========================================================================
  function fnInitDateInfo() {

    // 현재일자
    //const FULL_DATE_FORMAT = "yyyy-MM-dd HH:mm";
    const FULL_DATE_FORMAT = "yyyy-MM-dd";
    var date = new Date();
    date.setHours(0,0,0,0);
    // 기본시작일자
    todayDe = date.oFormat(FULL_DATE_FORMAT);
    baseStartDe = todayDe;

    // 기본종료일자
    baseEndDe = "2999-12-31";

    // 현재기준 6일후
    var afterSixDate = new Date();
    afterSixDate.setHours(0,0,0,0);

    afterSixDate.setDate(afterSixDate.getDate() + 6);
    afterSixDe = afterSixDate.oFormat(FULL_DATE_FORMAT);

    // 현재기준 1일후
    var afterOneDate = new Date();
    afterOneDate.setHours(0,0,0,0);

    afterOneDate.setDate(afterOneDate.getDate() + 1);
    afterOneDayDe = afterOneDate.oFormat(FULL_DATE_FORMAT);
    //// 현재기준 한달전일자
    //date.setMonth(date.getMonth() - 1);
    //beforeOneMonthDe = date.oFormat(FULL_DATE_FORMAT);
    //// 현재기준 한달후일자
    //date.setMonth(date.getMonth() + 2);
    //afterOneMonthDe = date.oFormat(FULL_DATE_FORMAT);
  }

  // ==========================================================================
  // # 초기화 - 버튼
  // ==========================================================================
  function fnInitButton() {
    // Top페이지신규/수정팝업오픈, 하위탑페이지신규/수정팝업오픈(하위페이지추가,상세추가), 페이지삭제처리, 인벤토리순번저장처리
    $('#fnBtnSearchContsLv1, #fnBtnSearchContsLv2, #fnBtnSearchContsLv3, #fnBtnPageLink, #fnBtnInventoryPreview, #fnBtnExcelDownInventory, #fnBtnExcelDownContsLv1, #fnBtnExcelDownContsLv2, #fnBtnExcelDownContsLv3, #fnBtnContsLv1Del, #fnBtnContsLv2Del, #fnBtnContsLv3Del, #fnBtnSaveSortContsLv1, #fnBtnSaveSortContsLv2, #fnBtnSaveSortContsLv3, #fnBtnContsLv1New, #fnBtnContsLv2New, #fnBtnContsLv3New, #fnBtnContsLv1Del, #fnBtnContsLv2Del, #fnBtnContsLv3Del, #fnBtnSaveText, #fnBtnSaveHtml, #fnBtnSaveBanner, #fnBtnSaveBrand, #fnBtnSaveCategory, #fnBtnSaveGoods, #fnBtnInventoryGroupEdit, #fnBtnInventoryGroupDel').kendoButton();
    // 인벤토리그리드 : 인벤토리신규/수정팝업오픈, 인벤토리삭제
    //$('#fnBtnInventoryPreview').kendoButton({ enable: true });
  }

  // ==========================================================================
  // # 초기화 - 입력값
  // ==========================================================================
  function fnInitInput() {
      $('#inputForm').formClear(true);
      $('#inputForm input[name=parentsPageId]').val(0);
      $('#inputForm input[name=depth]').val(1);
      $('#inputForm input[name=gbDicMstId]').val(null);
      $('#inputForm input[name=gbDicMstNm]').val(null);
      $('#inputForm input[name=comboMallDivision]').val(mallDivision);
      // # 페이지유형 - hidden
      //$('#pageTp').val(pageTp);
      // # 몰구분 - hidden
      $('#mallDivision').val(mallDivision);
      // # 미사용포함 체크/언체크 설정 - hidden
      if (useAllYn == 'Y') {
          $('INPUT[name=checkUseAllYn]').prop("checked", true);
      }
      else {
          $('INPUT[name=checkUseAllYn]').prop("checked", false);
      }
      // # 미사용포함여부
      $('#useAllYn').val(useAllYn);
      // 인벤토리 사용여부
      $('#inventoryUseYn').data('kendoDropDownList').value('Y');
  }

  // ==========================================================================
  // # 초기화 - Grid
  // ==========================================================================
  function fnInitGrid() {

    // Inventory Grid
    fnInitInventoryGrid();

    // Conts Lv1 Grid
    //fnInitContsLv1Grid();

    // Conts Lv2 Grid
    //fnInitContsLv2Grid();

    // Conts Lv3 Grid
    //fnInitContsLv3Grid();
  }

  // ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
  // ---------------Initialize Option Box Start -------------------------------
  // ==========================================================================
  // # fnInitOptionBox
  // ==========================================================================
  function fnInitOptionBox(){

    // ------------------------------------------------------------------------
    // 날짜정보 초기화
    // ------------------------------------------------------------------------
    fnInitDateInfo();

    // ------------------------------------------------------------------------
    // 팝업 초기화
    // ------------------------------------------------------------------------
    $('#kendoPopup').kendoWindow({
        visible : false
      , modal   : true
    });

    // ------------------------------------------------------------------------
    // 페이지검색유형(페이지/카테고리 목록, 인벤토리 목록)
    // ------------------------------------------------------------------------
    fnKendoDropDownList({
        id          : "comboPageSchTp"
      , data        : [ {"CODE" : "PAGE_SCH_TP.INVENTORY" , "NAME" : "페이지/카테고리 목록"}
                      , {"CODE" : "PAGE_SCH_TP.GROUP"     , "NAME" : "인벤토리 목록"}
                      ]
      , valueField  : "CODE"
      , textField   : "NAME"
      , chkVal      : 'PAGE_SCH_TP.INVENTORY'
      , style       : {}
            /*blank : '선택',*/
    });
    // ------------------------------------------------------------------------
    // 페이지유형(페이지코너별, 카테고리코너별)
    // ------------------------------------------------------------------------
    if(fnIsProgramAuth("AUTH_PAGE") && !fnIsProgramAuth("AUTH_CATEGORY")) {
      fnKendoDropDownList({
          id          : 'comboPageTp'
        , data        : [{"CODE" : "PAGE_TP.PAGE", "NAME" : "페이지코너별"}]
        , tagId       : 'comboPageTp'
        , valueField  : 'CODE'
        , textField   : 'NAME'
        , style       : {}
        /*blank : '선택',*/
      });
    } else if(fnIsProgramAuth("AUTH_CATEGORY") && !fnIsProgramAuth("AUTH_PAGE")){
      fnKendoDropDownList({
          id          : 'comboPageTp'
        , data        : [{"CODE" : "PAGE_TP.CATEGORY", "NAME" : "카테고리코너별"}]
        , tagId       : 'comboPageTp'
        , valueField  : 'CODE'
        , textField   : 'NAME'
        , style       : {}
        /*blank : '선택',*/
      });
      $('#divComboMallDivision').show();
    } else if(fnIsProgramAuth("AUTH_PAGE") && fnIsProgramAuth("AUTH_CATEGORY")) {
      fnKendoDropDownList({
          id          : 'comboPageTp'
        , tagId       : 'comboPageTp'
        , url         : "/admin/comn/getCodeList"
        , params      : {"stCommonCodeMasterCode" : "PAGE_TP", "useYn" :"Y"}
        , autoBind    : true
        , valueField  : 'CODE'
        , textField   : 'NAME'
        , async       : true
        , isDupUrl    : 'Y'
        , chkVal      : pageTp
        , style       : {}
        /*blank : '선택',*/
      });
    }
    // ------------------------------------------------------------------------
    // 몰구분
    // ------------------------------------------------------------------------
    fnKendoDropDownList({
        id          : 'comboMallDivision'
      , tagId       : 'comboMallDivision'
      , url         : "/admin/comn/getCodeList"
      , params      : {"stCommonCodeMasterCode" : "MALL_DIV", "useYn" :"Y"}
      , autoBind    : true
      , valueField  : 'CODE'
      , textField   : 'NAME'
      , async       : true
      , isDupUrl    : 'Y'
      , chkVal      : mallDivision
      , style       : {}
      /*blank : '선택',*/
    });
    // ------------------------------------------------------------------------
    // 인벤토리.사용여부
    // ------------------------------------------------------------------------
    fnKendoDropDownList({
        id          : "inventoryUseYn"
      , data        : [ {"CODE" : "" , "NAME" : "사용여부 전체"}
                      , {"CODE" : "Y", "NAME" : "예"}
                      , {"CODE" : "N", "NAME" : "아니오"}
                      ]
      , valueField  : "CODE"
      , textField   : "NAME"
      , value       : "Y"
    });
    // ------------------------------------------------------------------------
    // 콘텐츠기간검색조건.Lv1 영역
    // ------------------------------------------------------------------------
    fnKendoDatePicker({
        id        : 'dpStartDtLv1'
      , format    : 'yyyy-MM-dd'
      , defVal    : baseStartDe
      , change    : onChangeDpStartDtLv1
    });
    fnKendoDatePicker({
        id        : 'dpEndDtLv1'
      , format    : 'yyyy-MM-dd'
      , btnStyle  : true
      , btnStartId: 'dpStartDtLv1'
      , btnEndId  : 'dpEndDtLv1'
      , defVal    : baseEndDe
      , defType   : "oneWeek"
      , minusCheck: true
      , nextDate  : true
      , change    : onChangeDpEndDtLv1
    });
    fbMakeTimePicker("#dpStartHourPickerLv1", "start", "hour", onChangeDpStartDtLv1);
    fbMakeTimePicker("#dpStartMinPickerLv1" , "start", "min" , onChangeDpStartDtLv1);
    fbMakeTimePicker("#dpEndHourPickerLv1"  , "end"  , "hour", onChangeDpEndDtLv1);
    fbMakeTimePicker("#dpEndMinPickerLv1"   , "end"  , "min" , onChangeDpEndDtLv1);
    function onChangeDpStartDtLv1(e) {
      fnOnChangeDateTimePicker( e, "start", "dpStartDtLv1", "dpStartHourPickerLv1", "dpStartMinPickerLv1",  "dpEndDtLv1", "dpEndHourPickerLv1", "dpEndMinPickerLv1" );
    }
    function onChangeDpEndDtLv1(e) {
      fnOnChangeDateTimePicker( e, "end"  , "dpStartDtLv1", "dpStartHourPickerLv1", "dpStartMinPickerLv1",  "dpEndDtLv1", "dpEndHourPickerLv1", "dpEndMinPickerLv1" );
    }
    // 종료 시/분 기본값 Set
    $("#dpEndHourPickerLv1").data("kendoDropDownList").select(23);
    $("#dpEndMinPickerLv1").data("kendoDropDownList").select(59);
    // ------------------------------------------------------------------------
    // 콘텐츠기간검색조건.Lv2 영역
    // ------------------------------------------------------------------------
    fnKendoDatePicker({
        id        : 'dpStartDtLv2'
      , format    : 'yyyy-MM-dd'
      , defVal    : baseStartDe
      , change    : onChangeDpStartDtLv2
    });
    fnKendoDatePicker({
        id        : 'dpEndDtLv2'
      , format    : 'yyyy-MM-dd'
      , btnStyle  : true
      , btnStartId: 'dpStartDtLv2'
      , btnEndId  : 'dpEndDtLv2'
      , defVal    : baseEndDe
      , defType   : "oneWeek"
      , minusCheck: true
      , nextDate  : true
      , change    : onChangeDpEndDtLv2
    });
    fbMakeTimePicker("#dpStartHourPickerLv2", "start", "hour", onChangeDpStartDtLv2);
    fbMakeTimePicker("#dpStartMinPickerLv2" , "start", "min" , onChangeDpStartDtLv2);
    fbMakeTimePicker("#dpEndHourPickerLv2"  , "end"  , "hour", onChangeDpEndDtLv2);
    fbMakeTimePicker("#dpEndMinPickerLv2"   , "end"  , "min" , onChangeDpEndDtLv2);
    function onChangeDpStartDtLv2(e) {
      fnOnChangeDateTimePicker( e, "start", "dpStartDtLv2", "dpStartHourPickerLv2", "dpStartMinPickerLv2",  "dpEndDtLv2", "dpEndHourPickerLv2", "dpEndMinPickerLv2" );
    }
    function onChangeDpEndDtLv2(e) {
      fnOnChangeDateTimePicker( e, "end"  , "dpStartDtLv2", "dpStartHourPickerLv2", "dpStartMinPickerLv2",  "dpEndDtLv2", "dpEndHourPickerLv2", "dpEndMinPickerLv2" );
    }
    // 종료 시/분 기본값 Set
    $("#dpEndHourPickerLv2").data("kendoDropDownList").select(23);
    $("#dpEndMinPickerLv2").data("kendoDropDownList").select(59);
    // ------------------------------------------------------------------------
    // 콘텐츠기간검색조건.Lv3 영역
    // ------------------------------------------------------------------------
    fnKendoDatePicker({
        id        : 'dpStartDtLv3'
      , format    : 'yyyy-MM-dd'
      , defVal    : baseStartDe
      , change    : onChangeDpStartDtLv3
    });
    fnKendoDatePicker({
        id        : 'dpEndDtLv3'
      , format    : 'yyyy-MM-dd'
      , btnStyle  : true
      , btnStartId: 'dpStartDtLv3'
      , btnEndId  : 'dpEndDtLv3'
      , defVal    : baseEndDe
      , defType   : "oneWeek"
      , minusCheck: true
      , nextDate  : true
      , change    : onChangeDpEndDtLv3
    });
    fbMakeTimePicker("#dpStartHourPickerLv3", "start", "hour", onChangeDpStartDtLv3);
    fbMakeTimePicker("#dpStartMinPickerLv3" , "start", "min" , onChangeDpStartDtLv3);
    fbMakeTimePicker("#dpEndHourPickerLv3"  , "end"  , "hour", onChangeDpEndDtLv3);
    fbMakeTimePicker("#dpEndMinPickerLv3"   , "end"  , "min" , onChangeDpEndDtLv3);
    function onChangeDpStartDtLv3(e) {
      fnOnChangeDateTimePicker( e, "start", "dpStartDtLv3", "dpStartHourPickerLv3", "dpStartMinPickerLv3",  "dpEndDtLv3", "dpEndHourPickerLv3", "dpEndMinPickerLv3" );
    }
    function onChangeDpEndDtLv3(e) {
      fnOnChangeDateTimePicker( e, "end"  , "dpStartDtLv3", "dpStartHourPickerLv3", "dpStartMinPickerLv3",  "dpEndDtLv3", "dpEndHourPickerLv3", "dpEndMinPickerLv3" );
    }
    // 종료 시/분 기본값 Set
    $("#dpEndHourPickerLv3").data("kendoDropDownList").select(23);
    $("#dpEndMinPickerLv3").data("kendoDropDownList").select(59);
    // ------------------------------------------------------------------------
    // 진행상태-Lv1
    // ------------------------------------------------------------------------
    fnKendoDropDownList({
        id          : "contsLv1Status"
      , data        : [ {"CODE" : "" , "NAME" : "진행범위 전체"}
                      , {"CODE" : "A", "NAME" : "진행예정"}
                      , {"CODE" : "B", "NAME" : "진행중"}
                      , {"CODE" : "C", "NAME" : "진행종료"}
                      ]
      , valueField  : "CODE"
      , textField   : "NAME"
      //, value       : "Y"
    });
    // ------------------------------------------------------------------------
    // 진행상태-Lv2
    // ------------------------------------------------------------------------
    fnKendoDropDownList({
        id          : "contsLv2Status"
      , data        : [ {"CODE" : "" , "NAME" : "진행범위 전체"}
                      , {"CODE" : "A", "NAME" : "진행예정"}
                      , {"CODE" : "B", "NAME" : "진행중"}
                      , {"CODE" : "C", "NAME" : "진행종료"}
                      ]
      , valueField  : "CODE"
      , textField   : "NAME"
      //, value       : "Y"
    });
    // ------------------------------------------------------------------------
    // 진행상태-Lv3
    // ------------------------------------------------------------------------
    fnKendoDropDownList({
        id          : "contsLv3Status"
      , data        : [ {"CODE" : "" , "NAME" : "진행범위 전체"}
                      , {"CODE" : "A", "NAME" : "진행예정"}
                      , {"CODE" : "B", "NAME" : "진행중"}
                      , {"CODE" : "C", "NAME" : "진행종료"}
                      ]
      , valueField  : "CODE"
      , textField   : "NAME"
      //, value       : "Y"
    });
    // ------------------------------------------------------------------------
    // 전시범위-Lv1
    // ------------------------------------------------------------------------
    fnKendoDropDownList({
        id          : 'contsLv1DpRangeTp'
      , tagId       : 'contsLv1DpRangeTp'
      , url         : "/admin/comn/getCodeList"
      , params      : {"stCommonCodeMasterCode" : "DP_RANGE_TP", "useYn" :"Y"}
      , autoBind    : true
      , valueField  : 'CODE'
      , textField   : 'NAME'
      , async       : true
      , isDupUrl    : 'Y'
      , blank       : '전시범위 전체'
      , style       : {}
    });
    // ------------------------------------------------------------------------
    // 전시범위-Lv2
    // ------------------------------------------------------------------------
    fnKendoDropDownList({
        id          : 'contsLv2DpRangeTp'
      , tagId       : 'contsLv2DpRangeTp'
      , url         : "/admin/comn/getCodeList"
      , params      : {"stCommonCodeMasterCode" : "DP_RANGE_TP", "useYn" :"Y"}
      , autoBind    : true
      , valueField  : 'CODE'
      , textField   : 'NAME'
      , async       : true
      , isDupUrl    : 'Y'
      , blank       : '전시범위 전체'
      , style       : {}
    });
    // ------------------------------------------------------------------------
    // 전시범위-Lv3
    // ------------------------------------------------------------------------
    fnKendoDropDownList({
        id          : 'contsLv3DpRangeTp'
      , tagId       : 'contsLv3DpRangeTp'
      , url         : "/admin/comn/getCodeList"
      , params      : {"stCommonCodeMasterCode" : "DP_RANGE_TP", "useYn" :"Y"}
      , autoBind    : true
      , valueField  : 'CODE'
      , textField   : 'NAME'
      , async       : true
      , isDupUrl    : 'Y'
      , blank       : '전시범위 전체'
      , style       : {}
    });
    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - --
    // 노출조건-콘텐츠1
    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - --
    fnKendoDropDownList({
        id          : 'contLv1DpCondTp'
      , tagId       : 'contLv1DpCondTp'
      , url         : "/admin/comn/getCodeList"
      , params      : {"stCommonCodeMasterCode" : "DP_COND_TP", "useYn" :"Y"}
      , autoBind    : true
      , valueField  : 'CODE'
      , textField   : 'NAME'
      , async       : true
      , isDupUrl    : 'Y'
      //, chkVal      : 'DP_COND_TP.ALL'
      , blank       : '노출조건 전체'
      , style       : {}
    });
    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - --
    // 노출조건-콘텐츠2
    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - --
    fnKendoDropDownList({
        id          : 'contLv2DpCondTp'
      , tagId       : 'contLv2DpCondTp'
      , url         : "/admin/comn/getCodeList"
      , params      : {"stCommonCodeMasterCode" : "DP_COND_TP", "useYn" :"Y"}
      , autoBind    : true
      , valueField  : 'CODE'
      , textField   : 'NAME'
      , async       : true
      , isDupUrl    : 'Y'
      //, chkVal      : 'DP_COND_TP.ALL'
      , blank       : '노출조건 전체'
      , style       : {}
    });
    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - --
    // 노출조건-콘텐츠3
    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - --
    fnKendoDropDownList({
        id          : 'contLv3DpCondTp'
      , tagId       : 'contLv3DpCondTp'
      , url         : "/admin/comn/getCodeList"
      , params      : {"stCommonCodeMasterCode" : "DP_COND_TP", "useYn" :"Y"}
      , autoBind    : true
      , valueField  : 'CODE'
      , textField   : 'NAME'
      , async       : true
      , isDupUrl    : 'Y'
      //, chkVal      : 'DP_COND_TP.ALL'
      , blank       : '노출조건 전체'
      , style       : {}
    });
    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - --
    // 노출순서-콘텐츠1
    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - --
    fnKendoDropDownList({
        id          : 'contLv1DpSortTp'
      , tagId       : 'contLv1DpSortTp'
      , url         : "/admin/comn/getCodeList"
      , params      : {"stCommonCodeMasterCode" : "DP_SORT_TP", "useYn" :"Y"}
      , autoBind    : true
      , valueField  : 'CODE'
      , textField   : 'NAME'
      , async       : true
      , isDupUrl    : 'Y'
      //, chkVal      : 'DP_COND_TP.ALL'
      , blank       : '노출순서 전체'
      , style       : {}
    });
    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - --
    // 노출순서-콘텐츠2
    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - --
    fnKendoDropDownList({
        id          : 'contLv2DpSortTp'
      , tagId       : 'contLv2DpSortTp'
      , url         : "/admin/comn/getCodeList"
      , params      : {"stCommonCodeMasterCode" : "DP_SORT_TP", "useYn" :"Y"}
      , autoBind    : true
      , valueField  : 'CODE'
      , textField   : 'NAME'
      , async       : true
      , isDupUrl    : 'Y'
      //, chkVal      : 'DP_COND_TP.ALL'
      , blank       : '노출순서 전체'
      , style       : {}
    });
    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - --
    // 노출순서-콘텐츠3
    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - --
    fnKendoDropDownList({
        id          : 'contLv3DpSortTp'
      , tagId       : 'contLv3DpSortTp'
      , url         : "/admin/comn/getCodeList"
      , params      : {"stCommonCodeMasterCode" : "DP_SORT_TP", "useYn" :"Y"}
      , autoBind    : true
      , valueField  : 'CODE'
      , textField   : 'NAME'
      , async       : true
      , isDupUrl    : 'Y'
      //, chkVal      : 'DP_COND_TP.ALL'
      , blank       : '노출순서 전체'
      , style       : {}
    });

    // ------------------------------------------------------------------------
    // 팝업초기화
    // ------------------------------------------------------------------------
    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - --
    // 팝업-Text-전시기간
    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - --
    fnKendoDatePicker({
        id        : 'dpStartDtText'
      , format    : 'yyyy-MM-dd'
      , defVal    : baseStartDe
      , change    : function(e) {
          onChangeDpStartDtText.call(this, e);
        }
    });
    fnKendoDatePicker({
        id        : 'dpEndDtText'
      , format    : 'yyyy-MM-dd'
      , btnStyle  : true
      , btnStartId: 'dpStartDtText'
      , btnEndId  : 'dpEndDtText'
      , defVal    : baseEndDe
      , defType   : "oneWeek"
      , minusCheck: true
      , nextDate  : true
      , change    : onChangeDpEndDtText
    });
    fbMakeTimePicker("#dpStartHourText", "start", "hour", onChangeDpStartDtText);
    fbMakeTimePicker("#dpStartMinText" , "start", "min" , onChangeDpStartDtText);
    fbMakeTimePicker("#dpEndHourText"  , "end"  , "hour", onChangeDpEndDtText);
    fbMakeTimePicker("#dpEndMinText"   , "end"  , "min" , onChangeDpEndDtText);
    function onChangeDpStartDtText(e) {
      // 신규 생성일 경우 오늘날짜 보다 이전 날짜 선택 불가
      const _mode = $('#mode').val();
      if( _mode === 'conts.insert' ) {
        const selectedDate = this.value();

        if( checkIsDatePast(selectedDate) ) {
          alert('신규 등록 시 현재 날짜보다 과거일을 선택할 수 없습니다.');
          this.value(new Date());
          return;
        }
      }

      fnOnChangeDateTimePicker( e, "start", "dpStartDtText", "dpStartHourText", "dpStartMinText",  "dpEndDtText", "dpEndHourText", "dpEndMinText" );
    }
    function onChangeDpEndDtText(e) {
      fnOnChangeDateTimePicker( e, "end"  , "dpStartDtText", "dpStartHourText", "dpStartMinText",  "dpEndDtText", "dpEndHourText", "dpEndMinText" );
    }
    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - --
    // 팝업-Text-전시범위
    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - --
    fnKendoDropDownList({
        id          : 'dpRangeTpText'
      , tagId       : 'dpRangeTpText'
      , url         : "/admin/comn/getCodeList"
      , params      : {"stCommonCodeMasterCode" : "DP_RANGE_TP", "useYn" :"Y"}
      , autoBind    : true
      , valueField  : 'CODE'
      , textField   : 'NAME'
      , chkVal      : 'DP_RANGE_TP.ALL'
      //, blank       : '전시범위 전체'
      , style       : {}
    });
    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - --
    // 팝업-Html-전시기간
    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - --
    fnKendoDatePicker({
        id        : 'dpStartDtHtml'
      , format    : 'yyyy-MM-dd'
      , defVal    : baseStartDe
      , change    : function(e) {
          onChangeDpStartDtHtml.call(this, e);
        }
    });
    fnKendoDatePicker({
        id        : 'dpEndDtHtml'
      , format    : 'yyyy-MM-dd'
      , btnStyle  : true
      , btnStartId: 'dpStartDtHtml'
      , btnEndId  : 'dpEndDtHtml'
      , defVal    : baseEndDe
      , defType   : "oneWeek"
      , minusCheck: true
      , nextDate  : true
      , change    : onChangeDpEndDtHtml
    });
    fbMakeTimePicker("#dpStartHourHtml", "start", "hour", onChangeDpStartDtHtml);
    fbMakeTimePicker("#dpStartMinHtml" , "start", "min" , onChangeDpStartDtHtml);
    fbMakeTimePicker("#dpEndHourHtml"  , "end"  , "hour", onChangeDpEndDtHtml);
    fbMakeTimePicker("#dpEndMinHtml"   , "end"  , "min" , onChangeDpEndDtHtml);
    function onChangeDpStartDtHtml(e) {
      // 신규 생성일 경우 오늘날짜 보다 이전 날짜 선택 불가
      const _mode = $('#mode').val();
      if( _mode === 'conts.insert' ) {
        const selectedDate = this.value();

        if( checkIsDatePast(selectedDate) ) {
          alert('신규 등록 시 현재 날짜보다 과거일을 선택할 수 없습니다.');
          this.value(new Date());
          return;
        }
      }

      fnOnChangeDateTimePicker( e, "start", "dpStartDtHtml", "dpStartHourHtml", "dpStartMinHtml",  "dpEndDtHtml", "dpEndHourHtml", "dpEndMinHtml" );
    }
    function onChangeDpEndDtHtml(e) {
      fnOnChangeDateTimePicker( e, "end"  , "dpStartDtHtml", "dpStartHourHtml", "dpStartMinHtml",  "dpEndDtHtml", "dpEndHourHtml", "dpEndMinHtml" );
    }
    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - --
    // 팝업-Html-전시범위
    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - --
    fnKendoDropDownList({
        id          : 'dpRangeTpHtml'
      , tagId       : 'dpRangeTpHtml'
      , url         : "/admin/comn/getCodeList"
      , params      : {"stCommonCodeMasterCode" : "DP_RANGE_TP", "useYn" :"Y"}
      , autoBind    : true
      , valueField  : 'CODE'
      , textField   : 'NAME'
      , async       : true
      , isDupUrl    : 'Y'
      , chkVal      : 'DP_RANGE_TP.ALL'
      //, blank       : '전시범위 전체'
      , style       : {}
    });
    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - --
    // 팝업-배너-전시기간
    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - --
    fnKendoDatePicker({
        id        : 'dpStartDtBanner'
      , format    : 'yyyy-MM-dd'
      , defVal    : baseStartDe
      , change    : function(e) {
          onChangeDpStartDtBanner.call(this, e);
        }
    });
    fnKendoDatePicker({
        id        : 'dpEndDtBanner'
      , format    : 'yyyy-MM-dd'
      , btnStyle  : true
      , btnStartId: 'dpStartDtBanner'
      , btnEndId  : 'dpEndDtBanner'
      , defVal    : baseEndDe
      , defType   : "oneWeek"
      , minusCheck: true
      , nextDate  : true
      , change    : onChangeDpEndDtBanner
    });
    fbMakeTimePicker("#dpStartHourBanner", "start", "hour", onChangeDpStartDtBanner);
    fbMakeTimePicker("#dpStartMinBanner" , "start", "min" , onChangeDpStartDtBanner);
    fbMakeTimePicker("#dpEndHourBanner"  , "end"  , "hour", onChangeDpEndDtBanner);
    fbMakeTimePicker("#dpEndMinBanner"   , "end"  , "min" , onChangeDpEndDtBanner);
    function onChangeDpStartDtBanner(e) {
      // 신규 생성일 경우 오늘날짜 보다 이전 날짜 선택 불가
      const _mode = $('#mode').val();
      if( _mode === 'conts.insert' ) {
        const selectedDate = this.value();

        if( checkIsDatePast(selectedDate) ) {
          alert('신규 등록 시 현재 날짜보다 과거일을 선택할 수 없습니다.');
          this.value(new Date());
          return;
        }
      }

      fnOnChangeDateTimePicker( e, "start", "dpStartDtBanner", "dpStartHourBanner", "dpStartMinBanner",  "dpEndDtBanner", "dpEndHourBanner", "dpEndMinBanner" );
    }
    function onChangeDpEndDtBanner(e) {
      fnOnChangeDateTimePicker( e, "end"  , "dpStartDtBanner", "dpStartHourBanner", "dpStartMinBanner",  "dpEndDtBanner", "dpEndHourBanner", "dpEndMinBanner" );
    }
    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - --
    // 팝업-배너-전시범위
    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - --
    fnKendoDropDownList({
        id          : 'dpRangeTpBanner'
      , tagId       : 'dpRangeTpBanner'
      , url         : "/admin/comn/getCodeList"
      , params      : {"stCommonCodeMasterCode" : "DP_RANGE_TP", "useYn" :"Y"}
      , autoBind    : true
      , valueField  : 'CODE'
      , textField   : 'NAME'
      , async       : true
      , isDupUrl    : 'Y'
      , chkVal      : 'DP_RANGE_TP.ALL'
      //, blank       : '전시범위 전체'
      , style       : {}
    });
    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - --
    // 팝업-브랜드-전시기간
    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - --
    fnKendoDatePicker({
        id        : 'dpStartDtBrand'
      , format    : 'yyyy-MM-dd'
      , defVal    : baseStartDe
      , change    : function(e) {
                      onChangeDpStartDtBrand.call(this, e);
                    }

    });
    fnKendoDatePicker({
        id        : 'dpEndDtBrand'
      , format    : 'yyyy-MM-dd'
      , btnStyle  : true
      , btnStartId: 'dpStartDtBrand'
      , btnEndId  : 'dpEndDtBrand'
      , defVal    : '2999-12-31' // baseEndDe
      //, defType   : "oneWeek"
      , minusCheck: true
      , nextDate  : true
      , change    : onChangeDpEndDtBrand
    });
    fbMakeTimePicker("#dpStartHourBrand", "start", "hour", onChangeDpStartDtBrand);
    fbMakeTimePicker("#dpStartMinBrand" , "start", "min" , onChangeDpStartDtBrand);
    fbMakeTimePicker("#dpEndHourBrand"  , "end"  , "hour", onChangeDpEndDtBrand);
    fbMakeTimePicker("#dpEndMinBrand"   , "end"  , "min" , onChangeDpEndDtBrand);
    function onChangeDpStartDtBrand(e) {
      // 신규 생성일 경우 오늘날짜 보다 이전 날짜 선택 불가
      const _mode = $('#mode').val();
      if( _mode === 'conts.insert' ) {
        const selectedDate = this.value();

        if( checkIsDatePast(selectedDate) ) {
          alert('신규 등록 시 현재 날짜보다 과거일을 선택할 수 없습니다.');
          this.value(new Date());
          return;
        }
      }

      fnOnChangeDateTimePicker( e, "start", "dpStartDtBrand", "dpStartHourBrand", "dpStartMinBrand",  "dpEndDtBrand", "dpEndHourBrand", "dpEndMinBrand" );
    }
    function onChangeDpEndDtBrand(e) {
      fnOnChangeDateTimePicker( e, "end"  , "dpStartDtBrand", "dpStartHourBrand", "dpStartMinBrand",  "dpEndDtBrand", "dpEndHourBrand", "dpEndMinBrand" );
    }
    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - --
    // 팝업-브랜드-전시범위
    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - --
    fnKendoDropDownList({
        id          : 'dpRangeTpBrand'
      , tagId       : 'dpRangeTpBrand'
      , url         : "/admin/comn/getCodeList"
      , params      : {"stCommonCodeMasterCode" : "DP_RANGE_TP", "useYn" :"Y"}
      , autoBind    : true
      , valueField  : 'CODE'
      , textField   : 'NAME'
      , async       : true
      , isDupUrl    : 'Y'
      , chkVal      : 'DP_RANGE_TP.ALL'
      //, blank       : '전시범위 전체'
      , style       : {}
    });
    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - --
    // 팝업-브랜드-브랜드목록
    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - --
    fnKendoDropDownList({
        id          : 'comboBrandIdBrand'
      , tagId       : 'comboBrandIdBrand'
      , url         : '/admin/ur/brand/searchDisplayBrandList'
      , params      : {"useYn" :"Y", "searchBrandPavilionYn" : "Y"}
      , autoBind    : true
      , valueField  : 'dpBrandId'
      , textField   : 'dpBrandName'
      , async       : true
      , isDupUrl    : 'Y'
      //, chkVal      : urBrandId
      , style       : {}
      , blank       : '선택'
    });
    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - --
    // 팝업-카테고리-전시기간
    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - --
    fnKendoDatePicker({
        id        : 'dpStartDtCategory'
      , format    : 'yyyy-MM-dd'
      , defVal    : baseStartDe
      , change    : function(e) {
          onChangeDpStartDtCategory.call(this, e);
        }
    });
    fnKendoDatePicker({
        id        : 'dpEndDtCategory'
      , format    : 'yyyy-MM-dd'
      , btnStyle  : true
      , btnStartId: 'dpStartDtCategory'
      , btnEndId  : 'dpEndDtCategory'
      , defVal    : baseEndDe
      , defType   : "oneWeek"
      , minusCheck: true
      , nextDate  : true
      , change    : onChangeDpEndDtCategory
    });
    fbMakeTimePicker("#dpStartHourCategory", "start", "hour", onChangeDpStartDtCategory);
    fbMakeTimePicker("#dpStartMinCategory" , "start", "min" , onChangeDpStartDtCategory);
    fbMakeTimePicker("#dpEndHourCategory"  , "end"  , "hour", onChangeDpEndDtCategory);
    fbMakeTimePicker("#dpEndMinCategory"   , "end"  , "min" , onChangeDpEndDtCategory);
    function onChangeDpStartDtCategory(e) {
      // 신규 생성일 경우 오늘날짜 보다 이전 날짜 선택 불가
      const _mode = $('#mode').val();
      if( _mode === 'conts.insert' ) {
        const selectedDate = this.value();

        if( checkIsDatePast(selectedDate) ) {
          alert('신규 등록 시 현재 날짜보다 과거일을 선택할 수 없습니다.');
          this.value(new Date());
          return;
        }
      }

      fnOnChangeDateTimePicker( e, "start", "dpStartDtCategory", "dpStartHourCategory", "dpStartMinCategory",  "dpEndDtCategory", "dpEndHourCategory", "dpEndMinCategory" );
    }
    function onChangeDpEndDtCategory(e) {
      fnOnChangeDateTimePicker( e, "end"  , "dpStartDtCategory", "dpStartHourCategory", "dpStartMinCategory",  "dpEndDtCategory", "dpEndHourCategory", "dpEndMinCategory" );
    }
    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - --
    // 팝업-카테고리-전시범위
    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - --
    fnKendoDropDownList({
        id          : 'dpRangeTpCategory'
      , tagId       : 'dpRangeTpCategory'
      , url         : "/admin/comn/getCodeList"
      , params      : {"stCommonCodeMasterCode" : "DP_RANGE_TP", "useYn" :"Y"}
      , autoBind    : true
      , valueField  : 'CODE'
      , textField   : 'NAME'
      , async       : true
      , isDupUrl    : 'Y'
      , chkVal      : 'DP_RANGE_TP.ALL'
      //, blank       : '전시범위 전체'
      , style       : {}
    });
    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - --
    // 팝업-카테고리-카테고리선택영역
    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - --
    fnInitComboCategory('MALL_DIV.PULMUONE');
    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - --
    // 팝업-상품-전시기간
    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - --
    fnKendoDatePicker({
        id        : 'dpStartDtGoods'
      , format    : 'yyyy-MM-dd'
      , defVal    : baseStartDe
      , change    : function(e) {
          onChangeDpStartDtGoods.call(this, e);
        }
    });
    fnKendoDatePicker({
        id        : 'dpEndDtGoods'
      , format    : 'yyyy-MM-dd'
      , btnStyle  : true
      , btnStartId: 'dpStartDtGoods'
      , btnEndId  : 'dpEndDtGoods'
      , defVal    : baseEndDe
      , defType   : "oneWeek"
      , minusCheck: true
      , nextDate  : true
      , change    : onChangeDpEndDtGoods
    });
    fbMakeTimePicker("#dpStartHourGoods", "start", "hour", onChangeDpStartDtGoods);
    fbMakeTimePicker("#dpStartMinGoods" , "start", "min" , onChangeDpStartDtGoods);
    fbMakeTimePicker("#dpEndHourGoods"  , "end"  , "hour", onChangeDpEndDtGoods);
    fbMakeTimePicker("#dpEndMinGoods"   , "end"  , "min" , onChangeDpEndDtGoods);
    function onChangeDpStartDtGoods(e) {
      // 신규 생성일 경우 오늘날짜 보다 이전 날짜 선택 불가
      const _mode = $('#mode').val();
      if( _mode === 'conts.insert' ) {
        const selectedDate = this.value();

        if( checkIsDatePast(selectedDate) ) {
          alert('신규 등록 시 현재 날짜보다 과거일을 선택할 수 없습니다.');
          this.value(new Date());
          return;
        }
      }

      fnOnChangeDateTimePicker( e, "start", "dpStartDtGoods", "dpStartHourGoods", "dpStartMinGoods",  "dpEndDtGoods", "dpEndHourGoods", "dpEndMinGoods" );
    }
    function onChangeDpEndDtGoods(e) {
      fnOnChangeDateTimePicker( e, "end"  , "dpStartDtGoods", "dpStartHourGoods", "dpStartMinGoods",  "dpEndDtGoods", "dpEndHourGoods", "dpEndMinGoods" );
    }
    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - --
    // 팝업-상품-전시범위
    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - --
    fnKendoDropDownList({
        id          : 'dpRangeTpGoods'
      , tagId       : 'dpRangeTpGoods'
      , url         : "/admin/comn/getCodeList"
      , params      : {"stCommonCodeMasterCode" : "DP_RANGE_TP", "useYn" :"Y"}
      , autoBind    : true
      , valueField  : 'CODE'
      , textField   : 'NAME'
      , async       : true
      , isDupUrl    : 'Y'
      , chkVal      : 'DP_RANGE_TP.ALL'
      //, blank       : '전시범위 전체'
      , style       : {}
    });
    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - --
    // 팝업-상품-단일/복수조건
    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - --
    fnTagMkRadio({
        id      : 'goodsSearchCond'
      , tagId   : 'goodsSearchCond'
      , data    : [ {"CODE" : "A" , "NAME" : "단일조건 검색"}
                  , {"CODE" : "B" , "NAME" : "복수조건 검색"}
                  ]
      //, async   : false
      , chkVal  : 'A'
      , style   : {}
    });
    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - --
    // 팝업-상품-검색코드유형
    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - --
    fnKendoDropDownList({
        id          : "searchCondition"
      , tagId       : "searchCondition"
      , data        : [ { "CODE" : ""             , "NAME" : "코드유형 선택" }
                      , { "CODE" : "GOODS_CODE"   , "NAME" : "상품코드" }
                      , { "CODE" : "ITEM_CODE"    , "NAME" : "마스터 품목코드 (ERP)" }
                      , { "CODE" : "ITEM_BARCODE" , "NAME" : "품목 바코드" }
                      ]
      , valueField  : "CODE"
      , textField   : "NAME"
      //, chkVal      : 'U'
      , value       : ""
      //, blank       : "코드유형 선택"
    });
    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - --
    // 팝업-상품-공급업체
    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - --
    fnKendoDropDownList({
        id          : "supplierId"
      , tagId       : "supplierId"
      , url         : "/admin/comn/getDropDownSupplierList"
      , textField   : "supplierName"
      , valueField  : "supplierId"
      , blank       : "공급업체 전체"
      , async       : true
      , isDupUrl    : 'Y'
    });
    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - --
    // 팝업-상품-브랜드목록
    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - --
    fnKendoDropDownList({
        id          : 'comboBrandIdGoods'
      , tagId       : 'comboBrandIdGoods'
      , url         : "/admin/display/manage/selectBrandList"
      , params      : {"useYn" :"Y"}
      , autoBind    : true
      , valueField  : 'CODE'
      , textField   : 'NAME'
      , async       : true
      , isDupUrl    : 'Y'
      //, chkVal      : urBrandId
      , style       : {}
      , blank       : '브랜드 전체'
    });
    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - --
    // 팝업-상품-몰구분
    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - --
    fnKendoDropDownList({
        id          : 'comboMallDivisionGoods'
      , tagId       : 'comboMallDivisionGoods'
      , url         : "/admin/comn/getCodeList"
      , params      : {"stCommonCodeMasterCode" : "MALL_DIV", "useYn" :"Y"}
      , autoBind    : true
      , valueField  : 'CODE'
      , textField   : 'NAME'
      , async       : true
      , isDupUrl    : 'Y'
      //, chkVal      : mallDivGoods
      , style       : {}
      , blank       : '몰구분 전체'
    });
    // 카테고리선택영역 (초기값 몰구분전체 이므로 카테고리 조회 안함)
    //fnInitComboCategoryGoods('X');
    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - --
    // 팝업-상품-상품기간구분
    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - --
    fnKendoDropDownList({
        id          : "searchDtTp"
      , data        : [ {"CODE" : "R" , "NAME" : "등록일"}
                      , {"CODE" : "U" , "NAME" : "최근수정일"}
                      ]
      , valueField  : "CODE"
      , textField   : "NAME"
      //, chkVal      : 'U'
      , value       : "R"
    });
    //$("#searchDtTp").data("kendoDropDownList").value('R');
    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - --
    // 팝업-상품-상품기간검색
    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - --
    // 전시기간
    fnKendoDatePicker({
        id        : 'searchStartDtGoods'
      , format    : 'yyyy-MM-dd'
      , defVal    : baseStartDe
      , change    : function(e) {
          onChangeSearchStartDtGoods.call(this, e);
        }
    });
    fnKendoDatePicker({
        id        : 'searchEndDtGoods'
      , format    : 'yyyy-MM-dd'
      , btnStyle  : true
      , btnStartId: 'searchStartDtGoods'
      , btnEndId  : 'searchEndDtGoods'
      , defVal    : baseEndDe
      , defType   : "oneWeek"
      , minusCheck: true
      , nextDate  : true
      , change    : onChangeSearchEndDtGoods
    });
    function onChangeSearchStartDtGoods(e) {
      // 신규 생성일 경우 오늘날짜 보다 이전 날짜 선택 불가
      const _mode = $('#mode').val();
      if( _mode === 'conts.insert' ) {
        const selectedDate = this.value();

        if( checkIsDatePast(selectedDate) ) {
          alert('신규 등록 시 현재 날짜보다 과거일을 선택할 수 없습니다.');
          this.value(new Date());
          return;
        }
      }

      fnOnChangeDatePicker(e, "start", "searchStartDtGoods", "searchEndDtGoods");
    }

    function onChangeSearchEndDtGoods(e) {
        fnOnChangeDatePicker(e, "end", "searchStartDtGoods", "searchEndDtGoods");
    }
    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - --
    // 팝업-상품-상품유형
    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - --
    fnTagMkChkBox({
        id          : 'goodsType'
      , tagId       : 'goodsType'
      , url         : '/admin/comn/getCodeList'
      , params      : {"stCommonCodeMasterCode" : "GOODS_TYPE", "useYn" :"Y"}
      , style       : {}
      , async       : true
      , isDupUrl    : 'Y'
      , beforeData  : [{ "CODE" : "ALL", "NAME" : "전체" }]
      , change      : function(e) {

                        var checedVal = e.value;
                        fnCheckTrigCheckbox(checedVal, 'goodsType');
                      }
    });
    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - --
    // 팝업-상품-판매상태
    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - --
    fnTagMkChkBox({
        id          : "saleStatus"
      , tagId       : "saleStatus"
      , url         : "/admin/comn/getCodeList"
      , params      : {"stCommonCodeMasterCode" : "SALE_STATUS", "useYn" : "Y"}
      , style       : {}
      , beforeData  : [{ "CODE" : "ALL", "NAME" : "전체" }]
      , async       : true
      , isDupUrl    : 'Y'
      , change      : function(e) {

                        var checedVal = e.value;
                        fnCheckTrigCheckbox(checedVal, 'saleStatus');
                      }
    });
    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - --
    // 팝업-상품-판매유형
    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - --
    fnTagMkChkBox({
        id          : "saleType"
      , tagId       : "saleType"
      , url         : "/admin/comn/getCodeList"
      , params      : {"stCommonCodeMasterCode" : "SALE_TYPE", "useYn" : "Y"}
      , style       : {}
      , beforeData  : [{ "CODE" : "ALL", "NAME" : "전체" }]
      , async       : true
      , isDupUrl    : 'Y'
      , change      : function(e) {

                        var checedVal = e.value;
                        fnCheckTrigCheckbox(checedVal, 'saleType');
                      }
    });
    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - --
    // 팝업-상품-전시여부
    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - --
    fnTagMkRadio({
        id    : "displayYn"
      , tagId : "displayYn"
      , data  : [ { "CODE" : ""   , "NAME" : "전체" }
                , { "CODE" : "Y"  , "NAME" : "전시" }
                , { "CODE" : "N"  , "NAME" : "미전시" }
                ]
    });
    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - --
    // 팝업-상품-구매허용범위
    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - --
    fnTagMkRadio({
        id    : "purchaseTargetAllYn"
      , tagId : "purchaseTargetAllYn"
      , data  : [ { "CODE" : "Y"  , "NAME" : "전체"     }
                , { "CODE" : "N"  , "NAME" : "조건검색" }
                ]
    });
    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - --
    // 팝업-상품-구매허용범위-일반회원
    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - --
    fnTagMkChkBox({
        id    : "purchaseMemberYn"
      , tagId : "purchaseMemberYn"
      , data  : [ { "CODE" : "Y", "NAME" : "일반회원" } ]
    });
    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - --
    // 팝업-상품-구매허용범위-임직원회원
    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - --
    fnTagMkChkBox({
        id    : "purchaseEmployeeYn"
      , tagId : "purchaseEmployeeYn"
      , data  : [ { "CODE" : "Y", "NAME" : "임직원회원" } ]
    });
    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - --
    // 팝업-상품-구매허용범위-비회원
    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - --
    fnTagMkChkBox({
        id    : "purchaseNonmemberYn"
      , tagId : "purchaseNonmemberYn"
      , data  : [ { "CODE" : "Y", "NAME" : "비회원" } ]
    });
    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - --
    // 팝업-상품-판매허용범위
    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - --
    fnTagMkRadio({
        id    : "salesAllowanceAllYn"
      , tagId : "salesAllowanceAllYn"
      , data  : [ { "CODE" : "Y"  , "NAME" : "전체"     }
                , { "CODE" : "N"  , "NAME" : "조건검색" }
                ]
    });
    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - --
    // 팝업-상품-판매허용범위-PC Web여부
    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - --
    fnTagMkChkBox({
        id    : "displayWebPcYn"
      , tagId : "displayWebPcYn"
      , data  : [ { "CODE" : "Y", "NAME" : "PC Web" } ]
    });
    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - --
    // 팝업-상품-판매허용범위-M Web여부
    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - --
    fnTagMkChkBox({
        id    : "displayWebMobileYn"
      , tagId : "displayWebMobileYn"
      , data  : [ { "CODE" : "Y", "NAME" : "M Web" } ]
    });
    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - --
    // 팝업-상품-판매허용범위-App여부
    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - --
    fnTagMkChkBox({
        id    : "displayAppYn"
      , tagId : "displayAppYn"
      , data  : [ { "CODE" : "Y", "NAME" : "App" } ]
    });
    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - --
    // 팝업-인벤토리그룹관리-사용여부
    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - --
    fnTagMkRadio({
        id      : 'inventoryGrpUseYn'
      , tagId   : 'inventoryGrpUseYn'
      , data    : [ { "CODE" : "Y"  , "NAME":'예'    }
                  , { "CODE" : "N"  , "NAME":'아니오'}
                  ]
      , chkVal  : "Y"
      , style   : {}
    });
    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - --
    // 팝업-상품-카테고리선택영역 (초기값 몰구분전체 이므로 카테고리 조회 안함)
    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - --
    fnInitComboCategoryGoods('X');

    // ------------------------------------------------------------------------
    // 켄도업로드 초기화 PJH Start
    // ------------------------------------------------------------------------
    fnInitKendoUpload(); // 배너 && 에디터 이미지 업로드시 사용할 kendoUpload 컴포넌트 초기화

  }

  //  ==========================================================================
  // # fnInitEvent
  // ==========================================================================
  function fnInitEvent() {

    // ------------------------------------------------------------------------
    // 이벤트 등록 - 메인
    // ------------------------------------------------------------------------
    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - --
    // # 페이지검색유형 이벤트 (인벤토리/그룹)
    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - --
    $("#comboPageSchTp").change(function() {

      fnChangeComboPageSchTp();
    });
    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - --
    // # 페이지유형 이벤트
    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - --
    $("#comboPageTp").change(function() {

      fnChangeComboPageTp();
    });

    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - --
    // # 몰구분변경 이벤트
    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - --
    $("#comboMallDivision").change(function() {

      pageTp          = 'PAGE_TP.CATEGORY';
      mallDivision = $("#comboMallDivision").val();
      //console.log("# mallDivision click :: " + mallDivision);
      treeView.destroy();
      fnInitTree();

      // 상세영역 초기화 (페이지상세, 인벤토리그리드)
      fnInitDetl();

    });
    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - --
    // # 인벤토리.사용여부 변경
    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - --
    $("#inventoryUseYn").change(function() {

      // 페이지 선택 체크
      if (selectedPageData == null || selectedPageData == undefined || selectedPageData == 'undefined') {
        fnMessage('', '전시 페이지를 선택해주세요.', '');
        return false;
      }
      // 인벤토리리스트조회
      selectInventoryList(selectedPageData);

    });
    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - --
    // # 입력제한
    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - --
    $(".lengthCheck").on("keyup", function (e) {
      //console.log('# bindKeyupEvents.keyup');
      // 길이 제한
      const $titleLength = $(this).parent().find(".currentInput-length");

      console.log($titleLength);

      const MAX_LENGTH = !!this.maxLength ? this.maxLength : null;
      const _value = $(this).val();
      if (MAX_LENGTH && _value.length > MAX_LENGTH) {
        $(this).val(_value.slice(0, MAX_LENGTH));
      }
      //$titleLength.innerHTML = $(this).val().length;

      // 문자 제한
      if (this.name == "text1Color" || this.name == "text2Color" || this.name == "text3Color") {
        const regExp = /[^0-9a-fA-F\#]/gi;
        const _value = $(this).val();

        if (_value.match(regExp)) {
          $(this).val(_value.replace(regExp, ""));
        }
      }

      if($titleLength.length) {
         $titleLength.text($(this).val().length);
      }

    });
    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - --
    // # 입력제한-숫자만
    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - --
    $(".onlyNum").on("keyup", function (e) {
      // 숫자만
      //console.log('# bindKeyupEvents this.name :: ' + this.name);
      //if (this.name == "pagePageCd" || this.name == "inventoryCd") {
        const regExp = /[^0-9]/gi;
        const _value = $(this).val();

        if (_value.match(regExp)) {
          $(this).val(_value.replace(regExp, ""));
        }
      //}
    });
    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - --
    // # 인벤토리그리드 로우 클릭
    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - --
    //$('#ng-view').on("click", "#inventoryGrid tbody tr", function(e) {
    $("#inventoryGrid").on("click", "tbody tr", function(e) {
      var rowElement  = this;
      fnClickInventoryGrid(rowElement);
    });
    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - --
    // # 콘텐츠Lv1그리드 로우 클릭
    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - --
    $("#contsLv1Grid").on("click", "tbody tr", function(e) {
      var rowElement  = this;
      fnClickContsLv1Grid(rowElement);
    });
    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - --
    // # 콘텐츠Lv2그리드 로우 클릭
    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - --
    $("#contsLv2Grid").on("click", "tbody tr", function(e) {
      var rowElement  = this;
      fnClickContsLv2Grid(rowElement);
    });

    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - --
    // # 전시범위-콘텐츠Lv1 이벤트
    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - --
    $("#contsLv1DpRangeTp").change(function() {
      // 콘텐츠Lv1그리드 조회
      fnBtnSearchContsLv1();
    });
    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - --
    // # 전시범위-콘텐츠Lv2 이벤트
    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - --
    $("#contsLv2DpRangeTp").change(function() {
      // 콘텐츠Lv2그리드 조회
      fnBtnSearchContsLv2();
    });
    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - --
    // # 전시범위-콘텐츠Lv3 이벤트
    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - --
    $("#contsLv3DpRangeTp").change(function() {
      // 콘텐츠Lv3그리드 조회
      fnBtnSearchContsLv3();
    });

    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - --
    // # 진행상태-콘텐츠Lv1 이벤트
    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - --
    $("#contsLv1Status").change(function() {
      // 콘텐츠Lv1그리드 조회
      fnBtnSearchContsLv1();
    });
    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - --
    // # 진행상태-콘텐츠Lv2 이벤트
    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - --
    $("#contsLv2Status").change(function() {
      // 콘텐츠Lv2그리드 조회
      fnBtnSearchContsLv2();
    });
    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - --
    // # 진행상태-콘텐츠Lv3 이벤트
    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - --
    $("#contsLv3Status").change(function() {
      // 콘텐츠Lv3그리드 조회
      fnBtnSearchContsLv3();
    });


    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - --
    // # 노출조건-콘텐츠Lv1 이벤트
    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - --
    $("#contLv1DpCondTp").change(function() {
      // 콘텐츠Lv1그리드 조회
      fnBtnSearchContsLv1();
    });
    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - --
    // # 노출조건-콘텐츠Lv2 이벤트
    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - --
    $("#contLv2DpCondTp").change(function() {
      // 콘텐츠Lv1그리드 조회
      fnBtnSearchContsLv2();
    });
    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - --
    // # 노출조건-콘텐츠Lv3 이벤트
    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - --
    $("#contLv3DpCondTp").change(function() {
      // 콘텐츠Lv1그리드 조회
      fnBtnSearchContsLv3();
    });

    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - --
    // # 노출순서-콘텐츠Lv1 이벤트
    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - --
    $("#contLv1DpSortTp").change(function() {
      // 콘텐츠Lv1그리드 조회
      fnBtnSearchContsLv1();
    });
    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - --
    // # 노출순서-콘텐츠Lv2 이벤트
    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - --
    $("#contLv2DpSortTp").change(function() {
      // 콘텐츠Lv1그리드 조회
      fnBtnSearchContsLv2();
    });
    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - --
    // # 노출순서-콘텐츠Lv3 이벤트
    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - --
    $("#contLv3DpSortTp").change(function() {
      // 콘텐츠Lv1그리드 조회
      fnBtnSearchContsLv3();
    });

    // ------------------------------------------------------------------------
    // 이벤트 등록 - 팝업
    // ------------------------------------------------------------------------
    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - --
    // # 팝업-카테고리-몰구분변경 이벤트
    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - --
    //$("#comboMallDivisionCategory").change(function() {
    //
    //  var selectedMallDivCategory = $('#comboMallDivisionCategory').val();
    //
    //    if (selectedMallDivCategory == null || selectedMallDivCategory == '') {
    //      selectedMallDivCategory = 'X';
    //    }
    //    // 팝업-카테고리-카테고리영역 초기화
    //    fnInitComboCategory(selectedMallDivCategory);
    //});
    $("#comboMallDivisionCategory").off().on('change', function() {

      var selectedMallDivCategory = $('#comboMallDivisionCategory').val();

      if (selectedMallDivCategory == null || selectedMallDivCategory == '') {
        selectedMallDivCategory = 'X';
      }
      // 팝업-카테고리-카테고리영역 변경
      fnInitComboCategory(selectedMallDivCategory);
    });
    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - --
    // # 팝업-상품-단일/복수조건클릭 이벤트
    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - --
    $('input:radio[name="goodsSearchCond"]').click(function() {
      // 조회조건 노츨/숨김 처리
      if($("input[name=goodsSearchCond]:checked").val() == "A"){
        $('#single-section').show();
        //$('#multi-section').hide();
      }
      else if($("input[name=goodsSearchCond]:checked").val() == "B"){
        $('#single-section').hide();
        //$('#multi-section').show();
      }
    });
    //// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - --
    //// # 팝업-상품-몰구분변경 이벤트
    //// - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - --
    //$("#comboMallDivisionGoods").change(function() {
    //
    //  var selectedMallDivGoods = $('#comboMallDivisionGoods').val();
    //
    //    if (selectedMallDivGoods == null || selectedMallDivGoods == '') {
    //      selectedMallDivGoods = 'X';
    //    }
    //    // 팝업-상품-카테고리영역 초기화
    //    fnInitComboCategoryGoods(selectedMallDivGoods);
    //});
    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - --
    // 팝업-상품-구매허용범위 클릭 이벤트
    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - --
    $('input:radio[name="purchaseTargetAllYn"]').click(function() {
      //console.log('# purchaseTargetAllYn 클릭 :: ', $("input[name=purchaseTargetAllYn]:checked").val());
      // 조회조건 노츨/숨김 처리
      if($("input[name=purchaseTargetAllYn]:checked").val() == "Y"){
        // 전체
        $('#purchaseTargetAllSpan').hide();
        // 구매허용범위-조건검색-일반회원
        $('INPUT[name=purchaseMemberYn]').prop("checked", true);
        // 구매허용범위-조건검색-임직원회원
        $('INPUT[name=purchaseEmployeeYn]').prop("checked", true);
        // 구매허용범위-조건검색-비회원
        $('INPUT[name=purchaseNonmemberYn]').prop("checked", true);
      }
      else if($("input[name=purchaseTargetAllYn]:checked").val() == "N"){
        // 조건검색
        $('#purchaseTargetAllSpan').show();
      }
    });
    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - --
    // 팝업-상품-판매허용범위 클릭 이벤트
    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - --
    $('input:radio[name="salesAllowanceAllYn"]').click(function() {
      //console.log('# salesAllowanceAllYn 클릭 :: ', $("input[name=salesAllowanceAllYn]:checked").val());
      // 조회조건 노츨/숨김 처리
      if($("input[name=salesAllowanceAllYn]:checked").val() == "Y"){
        // 전체
        $('#salesAllowanceAllSpan').hide();
        // 판매허용범위-조건검색-PC Web
        $('INPUT[name=displayWebPcYn]').prop("checked", true);
        // 판매허용범위-조건검색-M Web
        $('INPUT[name=displayWebMobileYn]').prop("checked", true);
        // 판매허용범위-조건검색-App
        $('INPUT[name=displayAppYn]').prop("checked", true);
      }
      else if($("input[name=salesAllowanceAllYn]:checked").val() == "N"){
        // 조건검색
        $('#salesAllowanceAllSpan').show();
      }
    });
    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - --
    // 팝업-인벤토리그룹관리-수정버튼 이벤트
    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - --
    // * 콘텐츠 Lv1 수정
    $('#inventoryGroupGrid').on("click", "button[kind=btnInventoryGroupEdit]", function(e) {
      e.preventDefault();
      e.stopImmediatePropagation();
      let dataItem = inventoryGroupGrid.dataItem($(e.currentTarget).closest("tr"));
      selectedInventoryGroupData = dataItem;
      fnBtnInventoryGroupEdit(dataItem);
    });
    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - --
    // 팝업-인벤토리그룹관리-삭제버튼 이벤트
    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - --
    // * 콘텐츠 Lv1 수정
    $('#inventoryGroupGrid').on("click", "button[kind=btnInventoryGroupDel]", function(e) {
      e.preventDefault();
      e.stopImmediatePropagation();
      let dataItem = inventoryGroupGrid.dataItem($(e.currentTarget).closest("tr"));
      selectedInventoryGroupData = dataItem;
      fnInventoryGroupDelSingle(dataItem);
    });
    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - --
    // # 인벤토리그룹그리드 로우 클릭
    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - --
    //$('#ng-view').on("click", "#inventoryGrid tbody tr", function(e) {
    $("#inventoryGroupGrid").on("click", "tbody tr", function(e) {
      //console.log('# 인벤토리그룹그리드 클릭');

      // mode 초기화
      $('#mode').val('inventoryGroup.update');
      // 저장버튼
      $('#spanInventoryBtnSaveLavel').text('저장');
      $('#fnBtnSaveInventoryGroup').hide();

      var rowElement  = this;
      //fnClickInventoryGrid(rowElement);
      var row           = $(rowElement);
      var grid          = $("#inventoryGroupGrid").getKendoGrid();
      var dataItem      = grid.dataItem(grid.select());
      selectedInventoryGroupData = dataItem;

      //var dataItem = new Object();
      //dataItem.dpPageId   = dataItem.dpPageId;
      //dataItem.pageNm     = dataItem.pageNm;
      //dataItem.useYn      = dataItem.useYn;
      //dataItem.groupDesc  = dataItem.groupDesc;
      //dataItem.sort       = dataItem.sort;
      // 버튼
      //dataItem.buttonNm = '';
      // 선택그룹정보 노출 Set
      fnSetInventoryGroupDetail(dataItem);
    });

    // 이미지 미리보기 로드 이벤트
    $('#imgView').on('load', function() {
      $('#kendoPopup').data('kendoWindow').center();
    });

  }


  // 아래로 이동 대상
  // ==========================================================================
  // 팝업-상품-상품유형 체크이벤트 처리
  // ==========================================================================
  function fnCheckTrigCheckbox(checedVal, itemNm) {
    //console.log('# fnCheckTrigCheckbox Start :: ', checedVal, ', itemNm ::', itemNm);

    if (checedVal == 'ALL') {
      // ----------------------------------------------------------------------
      // 전체를 선택했을 경우
      // ----------------------------------------------------------------------
      //console.log('# 전체 선택여부 :: ', $("input[name="+itemNm+"]:eq(0)").is(":checked"));
      if ($("input[name="+itemNm+"]:eq(0)").is(":checked") == true) {
        // 전체가 체크인 경우
        $("input[name="+itemNm+"]").each(function(idx) {
          if(idx != 0) {
            $(this).prop("checked", true);
          }
        });
      }
      else {
        // 전체가 체크가 아닌 경우
        $("input[name="+itemNm+"]").each(function(idx) {
          if(idx != 0) {
            $(this).prop("checked", false);
          }
        });
      }
    }
    else {
      // ----------------------------------------------------------------------
      // 전체 이외를 선택했을 경우
      // ----------------------------------------------------------------------
      var totalCnt = 0;       // 전체를 포함한 전체개수
      var itemCnt  = 0;       // 전체 항목이 제외된 항목개수
      var itemCheckedCnt = 0; // 항목개수 중 체크된 개수

      $("input[name="+itemNm+"]").each(function(idx) {
        if(idx != 0) {
          if($("input[name="+itemNm+"]:eq("+idx+")").is(":checked") == true) {
            itemCheckedCnt++;
          }
        }
        totalCnt++;
      });
      itemCnt = totalCnt - 1;

      if (itemCheckedCnt < itemCnt) {
        // 선택이 0
        $("input[name="+itemNm+"][value=ALL]").prop("checked", false);
      }
      else if(itemCheckedCnt == itemCnt) {
        // 선택이 all
        $("input[name="+itemNm+"][value=ALL]").prop("checked", true);
      }
    }
  }

  // ==========================================================================
  // 팝업-카테고리-카테고리선택영역 초기화 이벤트처리
  // ==========================================================================
  function fnInitComboCategory(inMallDivCategory) {
    //console.log('# fnInitComboCategory [', inMallDivCategory, ']');
    // 카테고리0(그룹)-그룹이 있을 경우
    if ($("#categoryDepth0").data("kendoDropDownList")) {
      $("#categoryDepth0").data("kendoDropDownList").destroy();
      $("#categoryDepth0").empty();
    }
    fnKendoDropDownList({
        id          : "categoryDepth0"
      , tagId       : "categoryDepth0"
      , url         : "/admin/comn/getDropDownCategoryList"
      , params      : { "depth" : "0", "mallDiv" : inMallDivCategory}
      , textField   : "categoryName"
      , valueField  : "categoryId"
      , blank       : "그룹"
      , async       : false
    });
    // 카테고리1(대분류)-그룹이 있을 경우
    if ($("#categoryDepth1").data("kendoDropDownList")) {
      $("#categoryDepth1").data("kendoDropDownList").destroy();
      $("#categoryDepth1").empty();
    }
    fnKendoDropDownList({
        id          : "categoryDepth1"
      , tagId       : "categoryDepth1"
      , url         : "/admin/comn/getDropDownCategoryList"
      , textField   : "categoryName"
      , valueField  : "categoryId"
      , blank       : "대분류"
      , async       : false
      , cscdId      : "categoryDepth0"
      , cscdField   : "categoryId"
    });
    //// 카테고리1(대분류)-그룹이 없을 경우
    //fnKendoDropDownList({
    //    id          : "categoryDepth1"
    //  , tagId       : "categoryDepth1"
    //  , url         : "/admin/comn/getDropDownCategoryList"
    //  , params      : { "depth" : "1", "mallDiv" : inMallDivCategory}
    //  , textField   : "categoryName"
    //  , valueField  : "categoryId"
    //  , blank       : "대분류"
    //  , async       : false
    //});
    // 카테고리2(중분류)
    if ($("#categoryDepth2").data("kendoDropDownList")) {
      $("#categoryDepth2").data("kendoDropDownList").destroy();
      $("#categoryDepth2").empty();
    }
    fnKendoDropDownList({
        id          : "categoryDepth2"
      , tagId       : "categoryDepth2"
      , url         : "/admin/comn/getDropDownCategoryList"
      , textField   : "categoryName"
      , valueField  : "categoryId"
      , blank       : "중분류"
      , async       : false
      , cscdId      : "categoryDepth1"
      , cscdField   : "categoryId"
    });
    // 카테고리3
    if ($("#categoryDepth3").data("kendoDropDownList")) {
      $("#categoryDepth3").data("kendoDropDownList").destroy();
      $("#categoryDepth3").empty();
    }
    fnKendoDropDownList({
        id          : "categoryDepth3"
      , tagId       : "categoryDepth3"
      , url         : "/admin/comn/getDropDownCategoryList"
      , textField   : "categoryName"
      , valueField  : "categoryId"
      , blank       : "소분류"
      , async       : false
      , cscdId      : "categoryDepth2"
      , cscdField   : "categoryId"
    });
    // 카테고리4
    if ($("#categoryDepth4").data("kendoDropDownList")) {
      $("#categoryDepth4").data("kendoDropDownList").destroy();
      $("#categoryDepth4").empty();
    }
    fnKendoDropDownList({
        id          : "categoryDepth4"
      , tagId       : "categoryDepth4"
      , url         : "/admin/comn/getDropDownCategoryList"
      , textField   : "categoryName"
      , valueField  : "categoryId"
      , blank       : "세분류"
      , async       : false
      , cscdId      : "categoryDepth3"
      , cscdField   : "categoryId"
    });
  }

  // ==========================================================================
  // 팝업-상품-카테고리선택영역 초기화 이벤트처리
  // ==========================================================================
  function fnInitComboCategoryGoods(inMallDivGoods) {
    //console.log('# fnInitComboCategoryGoods [', inMallDivGoods, ']');
    // 카테고리0(그룹)-그룹이 있을 경우
    fnKendoDropDownList({
        id          : "categoryDepth0Goods"
      , tagId       : "categoryDepth0Goods"
      , url         : "/admin/comn/getDropDownCategoryList"
      , params      : { "depth" : "0", "mallDiv" : inMallDivGoods}
      , textField   : "categoryName"
      , valueField  : "categoryId"
      , blank       : "그룹"
      , async       : false
    });
    // 카테고리1(대분류)-그룹이 있을 경우
    fnKendoDropDownList({
        id          : "categoryDepth1Goods"
      , tagId       : "categoryDepth1Goods"
      , url         : "/admin/comn/getDropDownCategoryList"
      , textField   : "categoryName"
      , valueField  : "categoryId"
      , blank       : "대분류"
      , async       : false
      , cscdId      : "categoryDepth0Goods"
      , cscdField   : "categoryId"
    });
    //// 카테고리1(대분류)-그룹이 없을 경우
    //fnKendoDropDownList({
    //    id          : "categoryDepth1"
    //  , tagId       : "categoryDepth1"
    //  , url         : "/admin/comn/getDropDownCategoryList"
    //  , params      : { "depth" : "1", "mallDiv" : inMallDivCategory}
    //  , textField   : "categoryName"
    //  , valueField  : "categoryId"
    //  , blank       : "대분류"
    //  , async       : false
    //});
    // 카테고리2(중분류)
    fnKendoDropDownList({
        id          : "categoryDepth2Goods"
      , tagId       : "categoryDepth2Goods"
      , url         : "/admin/comn/getDropDownCategoryList"
      , textField   : "categoryName"
      , valueField  : "categoryId"
      , blank       : "중분류"
      , async       : false
      , cscdId      : "categoryDepth1Goods"
      , cscdField   : "categoryId"
    });
    // 카테고리3
    fnKendoDropDownList({
        id          : "categoryDepth3Goods"
      , tagId       : "categoryDepth3Goods"
      , url         : "/admin/comn/getDropDownCategoryList"
      , textField   : "categoryName"
      , valueField  : "categoryId"
      , blank       : "소분류"
      , async       : false
      , cscdId      : "categoryDepth2Goods"
      , cscdField   : "categoryId"
    });
    // 카테고리4
    fnKendoDropDownList({
        id          : "categoryDepth4Goods"
      , tagId       : "categoryDepth4Goods"
      , url         : "/admin/comn/getDropDownCategoryList"
      , textField   : "categoryName"
      , valueField  : "categoryId"
      , blank       : "세분류"
      , async       : false
      , cscdId      : "categoryDepth3Goods"
      , cscdField   : "categoryId"
    });
  }

  // ==========================================================================
  // # 이벤트처리 - 페이지검색유형 변경
  // ==========================================================================
  function fnChangeComboPageSchTp() {

    // # 페이지검색유형
    pageSchTp = $("#comboPageSchTp").val();
    //console.log('# 페이지검색유형 :: ', pageSchTp);
    //console.log('# fnChangeComboPageSchTp :: ', pageSchTp);


    // Tree 상단 콤보/버튼 노출 설정
    if (pageSchTp == 'PAGE_SCH_TP.INVENTORY') {
      // 페이지검색유형=인벤토리
      $('.fb__tree-wrapper').show();
      $('#divComboPageTp').show();
      $('#divComboMallDivision').hide();
      $('#divBtnInventoryGroup').hide();
    }
    else {
      if(fnIsProgramAuth("MANAGE_INVENTORY")) {
        $('.fb__tree-wrapper').show();
      } else {
        $('.fb__tree-wrapper').hide();
      }
      $('#divComboPageTp').hide();
      $('#divComboMallDivision').hide();
      $('#divBtnInventoryGroup').show();
    }

//    // TODO 공사중 알림
//    if (pageSchTp == 'PAGE_SCH_TP.GROUP') {
//    PAGE_SCH_TP.INVENTORY
//      fnMessage('', '그룹 조회는 공사중입니다.', '');
//      pageSchTp = 'PAGE_SCH_TP.INVENTORY';
//
//      fnKendoDropDownList({
//          id          : "comboPageSchTp"
//        , data        : [ {"CODE" : "PAGE_SCH_TP.INVENTORY" , "NAME" : "페이지/카테고리 목록"}
//                        , {"CODE" : "PAGE_SCH_TP.GROUP"     , "NAME" : "인벤토리 목록"}
//                        ]
//        , valueField  : "CODE"
//        , textField   : "NAME"
//        , chkVal      : pageSchTp
//        , style       : {}
//          /*blank : '선택',*/
//      });
//      return false;
//    }

    // 페이지유형(페이지코너별, 카테고리코너별)
    pageTp =  'PAGE_TP.PAGE';
    
    //   fnKendoDropDownList({
    //       id          : 'comboPageTp'
    //     , url         : "/admin/comn/getCodeList"
    //     , params      : {"stCommonCodeMasterCode" : "PAGE_TP", "useYn" :"Y"}
    //     , tagId       : 'comboPageTp'
    //     , autoBind    : true
    //     , valueField  : 'CODE'
    //     , textField   : 'NAME'
    //     , chkVal      : pageTp
    //     , style       : {}
    //     /*blank : '선택',*/
    //   });

    // 페이지유형 변경 이벤트 처리
    fnChangeComboPageTp();
  }

  // ==========================================================================
  // # 이벤트처리 - 페이지유형 변경
  // ==========================================================================
  function fnChangeComboPageTp() {

    // # 페이지유형
    pageTp = $("#comboPageTp").val();

    if (pageTp == 'PAGE_TP.PAGE') {
      // --------------------------------------------------------------------
      // 페이지유형 == 페이지코너별
      // --------------------------------------------------------------------
      // 몰구분 초기화(값 무효화)
      mallDivision        = '';
      // 몰구분-숨김
      $('#divComboMallDivision').hide();
      // 페이지 수정버튼 노출
      $('#buttonPageUpd').show();
      $('#buttonPageDel').show();

    }
    else if (pageTp == 'PAGE_TP.CATEGORY') {
      // --------------------------------------------------------------------
      // 페이지유형 == 카테고리코너별
      // --------------------------------------------------------------------
      // 몰구분 초기화
      mallDivision        = 'MALL_DIV.PULMUONE';
      // 몰구분-노출
      $('#divComboMallDivision').show();
      // 페이지 수정버튼 숨김
      $('#buttonPageUpd').hide();
      $('#buttonPageDel').hide();

    }
    // 몰구분 콤보 Set
    $("#comboMallDivision").data("kendoDropDownList").value(mallDivision);
    //fnKendoDropDownList({
    //    id          : 'comboMallDivision'
    //  , url         : "/admin/comn/getCodeList"
    //  , params      : {"stCommonCodeMasterCode" : "MALL_DIV", "useYn" :"Y"}
    //  , tagId       : 'comboMallDivision'
    //  , autoBind    : true
    //  , valueField  : 'CODE'
    //  , textField   : 'NAME'
    //  , chkVal      : mallDivision
    //  , style       : {}
    //  /*blank : '선택',*/
    //});
    // 몰구분 값 Set
    $('#inputForm input[name=comboMallDivision]').val(mallDivision);
    //console.log('# pageSchTp :: ', pageSchTp);

    // Tree 데이터 초기화
    treeView.destroy();
    // Tree 재조회
    fnInitTree();

    // 상세영역 초기화 (페이지상세, 인벤토리그리드)
    fnInitDetl();
  }

  // ---------------Initialize Option Box End ---------------------------------
  // ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++


  // ==========================================================================
  // # 클릭 - Tree 로우 클릭
  // ==========================================================================
  function fnClickTree (event) {
    //console.log('# fnClickTree Start');

    // ------------------------------------------------------------------------
    // 선택 Tree Data조회 (selectedPageData Set)
    // ------------------------------------------------------------------------
    getTreeSelectData();
    //console.log('# selectedPageData :: ', JSON.stringify(selectedPageData));
    // ------------------------------------------------------------------------
    // 콘텐츠그리드초기화
    // ------------------------------------------------------------------------
    // 콘텐츠 그리드 초기화
    fnInitContsLv1Grid();
    fnInitContsLv2Grid();
    fnInitContsLv3Grid();
    // 콘텐츠 그리드 숨김
    $('#gridContsLv1').hide();
    $('#gridContsLv2').hide();
    $('#gridContsLv3').hide();

    if (selectedPageData != null) {
      // 페이지상세(Tree) 선택
      // --------------------------------------------
      // # 선택한 페이지정보 전역변수에 Set
      // --------------------------------------------
      //selectedPageData = data;
      //console.log('# selectedPageData :: ' + JSON.stringify(selectedPageData));
      // --------------------------------------------
      // # 인벤토리.사용여부 초기화(전체)
      // --------------------------------------------
      fnKendoDropDownList({
          id          : "inventoryUseYn"
        , data        : [ {"CODE" : "" , "NAME" : "사용여부 전체"}
                        , {"CODE" : "Y", "NAME" : "예"}
                        , {"CODE" : "N", "NAME" : "아니오"}
                        ]
        , valueField  : "CODE"
        , textField   : "NAME"
        , value       : ""
      });
      // ----------------------------------------------------------------------
      // # 선택한 Tree 페이지정보 Set
      // ----------------------------------------------------------------------
      // 실제 노출은 되지 않음 : 인벤토리리스트조회 포함
      fnSelectPageDataSet(selectedPageData);
    }
  }

  // ==========================================================================
  // # 클릭 - 인벤토리그리드 로우 클릭
  // ==========================================================================
  function fnClickInventoryGrid(rowElement){
    //console.log('# 인벤토리그리드 로우 클릭 Start');
    //var rowElement  = this;
    var row         = $(rowElement);
    var grid        = $("#inventoryGrid").getKendoGrid();
    var selected;

    if (row.hasClass("k-state-selected")) {
      selected = grid.select();

      if (selected.prevObject.context != undefined   && selected.prevObject.context != null   &&
          selected.prevObject.context != 'undefined' && selected.prevObject.context != 'null' &&
          selected.prevObject.context != '' ) {

        // 선택 데이터 초기화
        selectedInventoryData = null;

        // 선택값
        selectedInventoryData = grid.dataItem(selected);
        //console.log('# 선택된 인벤토리 Data :: ' + JSON.stringify(selectedInventoryData));
        //console.log('# contsLevel1Tp :: ', selectedInventoryData.contsLevel1Tp, ', contsLevel2Tp :: ', selectedInventoryData.contsLevel2Tp, ', contsLevel3Tp :: ', selectedInventoryData.contsLevel3Tp);

        if (selectedInventoryData != undefined   && selectedInventoryData != null &&
            selectedInventoryData != 'undefined' && selectedInventoryData != 'null' &&
            selectedInventoryData != '') {

          // ------------------------------------------------------------------
          // 콘텐츠그리드 노출/숨김
          // ------------------------------------------------------------------
          // Lv.1
          // ------------------------------------------------------------------
          if (selectedInventoryData.contsLevel1Tp != undefined   && selectedInventoryData.contsLevel1Tp != null &&
              selectedInventoryData.contsLevel1Tp != 'undefined' && selectedInventoryData.contsLevel1Tp != 'null' &&
              selectedInventoryData.contsLevel1Tp != ''          && selectedInventoryData.contsLevel1Tp != 'DP_CONTENTS_TP.NONE'){
            // 콘텐츠Lv1영역-노출
            $('#gridContsLv1').show();
            // 콘텐츠Lv1타이틀
            $('#spanContsLevel1TpNm').html("<h3>" + selectedInventoryData.contsLevel1TpNm + "</h3>");
            // 노출조건/노출순서 노출/숨김
            if(selectedInventoryData.contsLevel1Tp == 'DP_CONTENTS_TP.GOODS') {
              // 컨텐츠타입이 상품이면 노출
              $('#contLv1DpCondTpDiv').show();
              $('#contLv1DpSortTpDiv').show();
            }
            else {
              // 컨텐츠타입이 상품이 아니면 숨김
              $('#contLv1DpCondTpDiv').hide();
              $('#contLv1DpSortTpDiv').hide();
            }
            // 조회기간 초기화
            $('#dpStartDtLv1').data('kendoDatePicker').value(baseStartDe);
            $('#dpStartHourPickerLv1').data('kendoDropDownList').select(0);
            $('#dpStartMinPickerLv1').data('kendoDropDownList').select(0);
            $('#dpEndDtLv1').data('kendoDatePicker').value(baseEndDe);
            $('#dpEndHourPickerLv1').data('kendoDropDownList').select(23);
            $('#dpEndMinPickerLv1').data('kendoDropDownList').select(59);
            // 기간버튼 1주일로 선택
            $('#gridContsLv1').find('[fb-btn-active]').attr('fb-btn-active', false);        // 모두 미선택
            $('#gridContsLv1').find('[data-id="fnDateBtn3"]').attr('fb-btn-active', true);  // 1주일 선택
          }
          else {
            // 콘텐츠Lv1영역-숨김
            $('#gridContsLv1').hide();
          }
          // ------------------------------------------------------------------
          // Lv.2
          // ------------------------------------------------------------------
          if (selectedInventoryData.contsLevel2Tp != undefined   && selectedInventoryData.contsLevel2Tp != null &&
              selectedInventoryData.contsLevel2Tp != 'undefined' && selectedInventoryData.contsLevel2Tp != 'null' &&
              selectedInventoryData.contsLevel2Tp != ''          && selectedInventoryData.contsLevel2Tp != 'DP_CONTENTS_TP.NONE'){
            // 콘텐츠Lv2영역-노출
            $('#gridContsLv2').show();
            // 콘텐츠Lv2타이틀
            $('#spanContsLevel2TpNm').html("<h3>" + selectedInventoryData.contsLevel2TpNm + "</h3>");
            // 노출조건/노출순서 노출/숨김
            if(selectedInventoryData.contsLevel2Tp == 'DP_CONTENTS_TP.GOODS') {
              // 컨텐츠타입이 상품이면 노출
              $('#contLv2DpCondTpDiv').show();
              $('#contLv2DpSortTpDiv').show();
            }
            else {
              // 컨텐츠타입이 상품이 아니면 숨김
              $('#contLv2DpCondTpDiv').hide();
              $('#contLv2DpSortTpDiv').hide();
            }
            // 조회기간 초기화
            $('#dpStartDtLv2').data('kendoDatePicker').value(baseStartDe);
            $('#dpStartHourPickerLv2').data('kendoDropDownList').select(0);
            $('#dpStartMinPickerLv2').data('kendoDropDownList').select(0);
            $('#dpEndDtLv2').data('kendoDatePicker').value(baseEndDe);
            $('#dpEndHourPickerLv2').data('kendoDropDownList').select(23);
            $('#dpEndMinPickerLv2').data('kendoDropDownList').select(59);
            // 기간버튼 1주일로 선택
            $('#gridContsLv2').find('[fb-btn-active]').attr('fb-btn-active', false);        // 모두 미선택
            $('#gridContsLv2').find('[data-id="fnDateBtn3"]').attr('fb-btn-active', true);  // 1주일 선택
          }
          else {
            // 콘텐츠Lv2영역-숨김
            $('#gridContsLv2').hide();
          }
          // ------------------------------------------------------------------
          // Lv.3
          // ------------------------------------------------------------------
          if (selectedInventoryData.contsLevel3Tp != undefined   && selectedInventoryData.contsLevel3Tp != null &&
              selectedInventoryData.contsLevel3Tp != 'undefined' && selectedInventoryData.contsLevel3Tp != 'null' &&
              selectedInventoryData.contsLevel3Tp != ''          && selectedInventoryData.contsLevel3Tp != 'DP_CONTENTS_TP.NONE'){
            // 콘텐츠Lv3영역-노출
            $('#gridContsLv3').show();
            // 콘텐츠Lv3타이틀
            $('#spanContsLevel3TpNm').html("<h3>" + selectedInventoryData.contsLevel3TpNm + "</h3>");
            // 노출조건/노출순서 노출/숨김
            if(selectedInventoryData.contsLevel3Tp == 'DP_CONTENTS_TP.GOODS') {
              // 컨텐츠타입이 상품이면 노출
              $('#contLv3DpCondTpDiv').show();
              $('#contLv3DpSortTpDiv').show();
            }
            else {
              // 컨텐츠타입이 상품이 아니면 숨김
              $('#contLv3DpCondTpDiv').hide();
              $('#contLv3DpSortTpDiv').hide();
            }
            // 조회기간 초기화
            $('#dpStartDtLv3').data('kendoDatePicker').value(baseStartDe);
            $('#dpStartHourPickerLv3').data('kendoDropDownList').select(0);
            $('#dpStartMinPickerLv3').data('kendoDropDownList').select(0);
            $('#dpEndDtLv3').data('kendoDatePicker').value(baseEndDe);
            $('#dpEndHourPickerLv3').data('kendoDropDownList').select(23);
            $('#dpEndMinPickerLv3').data('kendoDropDownList').select(59);
            // 기간버튼 1주일로 선택
            $('#gridContsLv3').find('[fb-btn-active]').attr('fb-btn-active', false);        // 모두 미선택
            $('#gridContsLv3').find('[data-id="fnDateBtn3"]').attr('fb-btn-active', true);  // 1주일 선택
          }
          else {
            // 콘텐츠Lv3영역-숨김
            $('#gridContsLv3').hide();
          }
        }

        // --------------------------------------------------------------------
        // 콘텐츠Lv1그리드 조회
        // --------------------------------------------------------------------
        fnBtnSearchContsLv1();

      }
    }
  }

  // ==========================================================================
  // # 클릭 - 콘텐츠Lv1그리드 로우 클릭
  // ==========================================================================
  function fnClickContsLv1Grid(rowElement){

    //var rowElement  = this;
    var row         = $(rowElement);
    var grid        = $("#contsLv1Grid").getKendoGrid();
    var selected;

    if (row.hasClass("k-state-selected")) {
      selected = grid.select();

      if (selected != undefined   && selected != null   &&
          selected != 'undefined' && selected != 'null' &&
          selected != '' ) {
        // selected.prevObject.context

        // 선택값
        selectedContsLv1Data = grid.dataItem(selected);
        //console.log('# 선택된 콘텐츠Lv1 Data :: ' + JSON.stringify(selectedContsLv1Data));

        // --------------------------------------------------------------------
        // 콘텐츠Lv2그리드 조회
        // --------------------------------------------------------------------
        fnBtnSearchContsLv2();

      }
    }
  }

  // ==========================================================================
  // # 클릭 - 콘텐츠Lv2그리드 로우 클릭
  // ==========================================================================
  function fnClickContsLv2Grid(rowElement){

    //var rowElement  = this;
    var row         = $(rowElement);
    var grid        = $("#contsLv2Grid").getKendoGrid();
    var selected;

    if (row.hasClass("k-state-selected")) {
      selected = grid.select();

      if (selected != undefined   && selected != null   &&
          selected != 'undefined' && selected != 'null' &&
          selected != '' ) {
        // 선택 데이터 초기화(하위 포함)
        selectedContsLv2Data  = null;
        selectedContsLv3Data  = null;
        // 선택값
        selectedContsLv2Data = grid.dataItem(selected);
        //console.log('# 선택된 콘텐츠Lv2 Data :: ' + JSON.stringify(selectedContsLv2Data));

        // --------------------------------------------------------------------
        // 콘텐츠Lv3그리드 조회
        // --------------------------------------------------------------------
        fnBtnSearchContsLv3();

      }
    }
  }

  // ==========================================================================
  // # 버튼 - 인벤토리그룹관리팝업-수정
  // ==========================================================================
  function fnBtnInventoryGroupEdit(dataItem){
    //console.log('# 인벤토리그룹관리팝업 수정버튼 클릭 Start');
    //console.log('# 선택된 인벤토리그룹 Data :: ' + JSON.stringify(dataItem));

    // mode
    $('#mode').val('inventoryGroup.update');
    // 선택그룹정보 노출 Set
    fnSetInventoryGroupDetail(dataItem);
    // 버튼명
    $('#spanInventoryBtnSaveLavel').text('수정저장');
    $('#fnBtnSaveInventoryGroup').show();
  }

  // ==========================================================================
  // # 인벤토리그룹 상세정보 초기화 Set
  // ==========================================================================
  function fnInitInventoryGroupDetail() {

    // 인벤토리그룹정보 Set
    // 인벤토리그룹ID
    $('#dpInventoryGrpIdDiv').text('');
    // 인벤토리그룹명
    $('#inventoryGrpNm').val('');
    // 사용여부
    $('input:radio[name="inventoryGrpUseYn"]:input[value="Y"]').prop("checked", true);
    // 그룹설명
    $('#groupDesc').val('');
    // 순번
    $('#sortInventoryGrp').val('');
    // 인벤토리코드
    $('#inventoryCdsString').val('');
    // 저장버튼명
    $('#spanInventoryBtnSaveLavel').text('신규저장');
    $('#fnBtnSaveInventoryGroup').hide();
  }

  // ==========================================================================
  // # 인벤토리그룹 상세정보 Set
  // ==========================================================================
  function fnSetInventoryGroupDetail(dataItem) {

    // 인벤토리그룹정보 Set
    // 인벤토리그룹ID
    $('#dpInventoryGrpIdDiv').text(dataItem.dpPageId);
    // 인벤토리그룹명
    $('#inventoryGrpNm').val(dataItem.pageNm);
    // 사용여부
    $('input:radio[name="inventoryGrpUseYn"]:input[value="'+dataItem.useYn+'"]').prop("checked", true);
    // 그룹설명
    $('#groupDesc').val(dataItem.groupDesc);
    // 순번
    $('#sortInventoryGrp').val(dataItem.sort);

    // 인벤토리코드조회 및 Set
    fnAjax({
      url     : '/admin/display/manage/selectDpGroupInventoryMappingList'
        , params  : {dpInventoryGrpId : dataItem.dpPageId}
    , success : function( result ){
      //console.log('# 인벤토리코드조회 :: ', JSON.stringify(result));
      // 인벤토리코드
      $('#inventoryCdsString').val(result.inventoryCdsString);

    }
    , isAction : 'select'
    });
  }

  // ==========================================================================
  // # 버튼 - 인벤토리그룹관리팝업-신규등록
  // ==========================================================================
  function fnBtnInventoryGroupNew(){
    //console.log('# 인벤토리그룹관리팝업 신규등록 버튼 클릭 Start');

    // 선택한 인벤토리그룹Data 삭제
    selectedInventoryGroupData = null;

    // mode
    $('#mode').val('inventoryGroup.insert');
    // 인벤토리그룹정보 초기화 Set
    fnInitInventoryGroupDetail();
    // 저장버튼
    $('#spanInventoryBtnSaveLavel').text('신규저장');
    $('#fnBtnSaveInventoryGroup').show();

    //// 인벤토리그룹ID
    //$('#dpInventoryGrpIdDiv').text('');
    //// 인벤토리그룹명
    //$('#inventoryGrpNm').val('');
    //// 사용여부
    //$('input:radio[name="inventoryGrpUseYn"]:input[value="Y"]').prop("checked", true);
    //// 그룹설명
    //$('#groupDesc').val('');
    //// 순번
    //$('#sortInventoryGrp').val('');
    //// 인벤토리코드
    //$('#inventoryCdsString').val('');
    //// 저장버튼명
    //$('#spanInventoryBtnSaveLavel').text('신규저장');
  }

  // ==========================================================================
  // # 버튼 - 콘텐츠Lv1조회
  // ==========================================================================
  function fnBtnSearchContsLv1() {
    //console.log('# fnBtnSearchContsLv1 Start');
    // ------------------------------------------------------------------------
    // 상위 데이터 체크
    // ------------------------------------------------------------------------
    if (selectedInventoryData == null || selectedInventoryData.dpInventoryId == null ||
        selectedInventoryData == ''   || selectedInventoryData.dpInventoryId == '') {
      fnMessage('', '인벤토리 정보가 없습니다. 인벤토리를 선택하세요.', '');
      return false;
    }

    // ------------------------------------------------------------------------
    // 콘텐츠Lv1그리드 조회
    // ------------------------------------------------------------------------
    // 콘텐츠 그리드 초기화
    fnInitContsLv1Grid();
    fnInitContsLv2Grid();
    fnInitContsLv3Grid();
    // 콘텐츠Lv1그리드조회
    let data = $("#inputForm").formSerialize(true);
    contsLv1GridDs.read(data);

    // 그리드상단 조회기간 Set
    // var searchStartText = $('#dpStartDtLv1').val() + ' ' + $('#dpStartHourPickerLv1').val() + ":" + $('#dpStartMinPickerLv1').val();
    // var searchEndText   = $('#dpEndDtLv1').val()   + ' ' + $('#dpEndHourPickerLv1').val()   + ":" + $('#dpEndMinPickerLv1').val();
    var searchStartText = $('#dpStartDtLv1').val();
    var searchEndText   = $('#dpEndDtLv1').val();
    $('#spanPeriodConstLv1').text(searchStartText + ' ~ ' + searchEndText);
  }

  // ==========================================================================
  // # 버튼 - 콘텐츠Lv2조회
  // ==========================================================================
  function fnBtnSearchContsLv2() {

    // ------------------------------------------------------------------------
    // 상위 데이터 체크
    // ------------------------------------------------------------------------
    if (selectedInventoryData == null || selectedInventoryData.dpInventoryId == null ||
        selectedInventoryData == ''   || selectedInventoryData.dpInventoryId == '') {
      fnMessage('', '인벤토리 정보가 없습니다. 인벤토리를 선택하세요.', '');
      return false;
    }
    if (selectedContsLv1Data == null || selectedContsLv1Data.dpContsId == null ||
        selectedContsLv1Data == ''   || selectedContsLv1Data.dpContsId == '') {
      fnMessage('', '콘텐츠 Lv.1 정보가 없습니다. Lv.1 콘텐츠를 선택하세요.', '');
      return false;
    }

    // ------------------------------------------------------------------------
    // 콘텐츠Lv2그리드 조회
    // ------------------------------------------------------------------------
    // 콘텐츠 선택데이터 초기화
    selectedContsLv2Data  = null;
    selectedContsLv3Data  = null;
    // 콘텐츠 그리드 초기화
    fnInitContsLv2Grid();
    fnInitContsLv3Grid();
    // 콘텐츠Lv1그리드조회
    let data = $("#inputForm").formSerialize(true);
    contsLv2GridDs.read(data);

    // 그리드상단 조회기간 Set
    // var searchStartText = $('#dpStartDtLv2').val() + ' ' + $('#dpStartHourPickerLv2').val() + ":" + $('#dpStartMinPickerLv2').val();
    // var searchEndText   = $('#dpEndDtLv2').val()   + ' ' + $('#dpEndHourPickerLv2').val()   + ":" + $('#dpEndMinPickerLv2').val();
    var searchStartText = $('#dpStartDtLv2').val();
    var searchEndText   = $('#dpEndDtLv2').val();
    $('#spanPeriodConstLv2').text(searchStartText + ' ~ ' + searchEndText);
  }

  // ==========================================================================
  // # 버튼 - 콘텐츠Lv3조회
  // ==========================================================================
  function fnBtnSearchContsLv3() {

    // ------------------------------------------------------------------------
    // 상위 데이터 체크
    // ------------------------------------------------------------------------
    if (selectedInventoryData == null || selectedInventoryData.dpInventoryId == null ||
        selectedInventoryData == ''   || selectedInventoryData.dpInventoryId == '') {
      fnMessage('', '인벤토리 정보가 없습니다. 인벤토리를 선택하세요.', '');
      return false;
    }
    if (selectedContsLv1Data == null || selectedContsLv1Data.dpContsId == null ||
        selectedContsLv1Data == ''   || selectedContsLv1Data.dpContsId == '') {
      fnMessage('', '콘텐츠 Lv.1 정보가 없습니다. Lv.1 콘텐츠를 선택하세요.', '');
      return false;
    }
    if (selectedContsLv2Data == null || selectedContsLv2Data.dpContsId == null ||
        selectedContsLv2Data == ''   || selectedContsLv2Data.dpContsId == '') {
      fnMessage('', '콘텐츠 Lv.2 정보가 없습니다. Lv.2 콘텐츠를 선택하세요.', '');
      return false;
    }

    // ------------------------------------------------------------------------
    // 콘텐츠Lv3그리드 조회
    // ------------------------------------------------------------------------
    // 콘텐츠 선택데이터 초기화
    selectedContsLv3Data  = null;
    // 콘텐츠 그리드 초기화
    fnInitContsLv3Grid();
    // 콘텐츠Lv1그리드조회
    let data = $("#inputForm").formSerialize(true);
    contsLv3GridDs.read(data);

    // 그리드상단 조회기간 Set
    // var searchStartText = $('#dpStartDtLv3').val() + ' ' + $('#dpStartHourPickerLv3').val() + ":" + $('#dpStartMinPickerLv3').val();
    // var searchEndText   = $('#dpEndDtLv3').val()   + ' ' + $('#dpEndHourPickerLv3').val()   + ":" + $('#dpEndMinPickerLv3').val();
    var searchStartText = $('#dpStartDtLv3').val();
    var searchEndText   = $('#dpEndDtLv3').val();
    $('#spanPeriodConstLv3').text(searchStartText + ' ~ ' + searchEndText);
  }


  // ==========================================================================
  // # 버튼 - 인벤토리그리드 - 페이지바로가기
  // ==========================================================================
// TODO 페이지바로가기 로 로직 변경
  function fnBtnPageLink() {

    fnMessage('', '페이지바로가기 공사중입니다...', '');
    return false;
  }

  // ==========================================================================
  // # 버튼 - 인벤토리그리드-엑셀다운로드
  // ==========================================================================
  function fnBtnExcelDownInventory() {
    //console.log('# 인벤토리 엑셀다운로드버튼 클릭...');
    //console.log('# 인벤토리Grid :: ', inventoryGrid.dataSource._data.length);
    //console.log('# 인벤토리Grid :: ', inventoryGrid.dataSource._view.length);

    if (inventoryGrid != null && inventoryGrid.dataSource != null) {

      if( !$("#Tree").data("kendoTreeView").select().length ) {
        fnMessage('', '좌측 페이지 리스트에서 코너 선택 후 인벤토리 목록 다운로드가 가능합니다.', '');
        return;
      }

      if (inventoryGrid.dataSource._view.length <= 0) {
        fnMessage('', '다운로드할 인벤토리 목록이 없습니다.', '');
        return false;
      }
      //console.log('# inventoryGrid count :: ', inventoryGrid.dataSource._view.length);
      //console.log('# inventoryGrid count :: ', inventoryGrid.dataSource.total());
    }
    else {
      fnMessage('', '인벤토리를 먼저 조회해 주십시오.', '');
      return false;
    }

    // 세션 체크
    if (PG_SESSION == null || PG_SESSION == '' || PG_SESSION == undefined || PG_SESSION == 'undefined') {
      console.log('# PG_SESSION :: ' + JSON.stringify(PG_SESSION));
      //location.href = "/layout.html#/goodsMgm?ilGoodsId="+data.ilGoodsId;
      fnKendoMessage({ message : "세션이 종료되었습니다. \n다시 로그인 해 주십시오.", ok : function (){
        location.href = "/admVerify.html";
      }});
      return false;
    }
    //console.log('# comboPageTp :: ', $('#comboPageTp').val());

    // depth
    //$('#depth').val(selectedPageData.depth);

    var url = '/admin/display/manage/getExportExcelInventoryList'
            + '?' + 'dpPageId='         + selectedPageData.dpPageId
            + '&' + "pageTp="           + $('#comboPageTp').val()
            + '&' + "pageNm="           + selectedPageData.pageNm
            + '&' + "pageFullPath="     + selectedPageData.pageFullPath
            + '&' + "mallDiv="          + $('#comboMallDivision').val()
            + '&' + "depth="            + selectedPageData.depth
            + '&' + 'pageSchTp='        + $('#comboPageSchTp').val()        // 그룹 인벤토리리스트 관련항목
            + '&' + "dpInventoryGrpId=" + selectedPageData.dpPageId         // 그룹 인벤토리리스트 관련항목
            + '&' + "dpInventoryGrpId=" + selectedPageData.dpPageId         // 그룹 인벤토리리스트 관련항목
            ;

    var data = $('#inputFormExcelInventory').formSerialize(true);
    fnExcelDownload(url, data);
  }

  // ==========================================================================
  // # 버튼 - 콘텐츠1그리드-엑셀다운로드
  // ==========================================================================
  function fnBtnExcelDownContsLv1() {

    if (contsLv1Grid != null && contsLv1Grid.dataSource != null) {

      if (contsLv1Grid.dataSource._view.length <= 0) {
        fnMessage('', '다운로드할 콘텐츠(Lv.1) 목록이 없습니다.', '');
        return false;
      }
    }
    else {
      fnMessage('', '콘텐츠(Lv.1)를 먼저 조회해 주십시오.', '');
      return false;
    }

    var contsObj = new Object();
    contsObj.contsLevel     = 1;
    contsObj.dpInventoryId;
    contsObj.prntsContsId   = 0;
    contsObj.dpRangeTp      = $('#contsLv1DpRangeTp').val();
    contsObj.ilCtgryId      = '';
    contsObj.status         = $('#contsLv1Status').val();
    contsObj.contsTp        = '';

    if (selectedInventoryData != undefined && selectedInventoryData != null) {
      //console.log('# selectedInventoryData :: ' + JSON.stringify(selectedInventoryData));
      // 인벤토리ID
      if (selectedInventoryData.dpInventoryId != undefined && selectedInventoryData.dpInventoryId != null) {
        contsObj.dpInventoryId  = selectedInventoryData.dpInventoryId;
      }
      // 콘텐츠유형
      if (selectedInventoryData.contsLevel1Tp != null) {
        contsObj.contsTp = selectedInventoryData.contsLevel1Tp;
      }
      // 카테고리ID
      if (selectedInventoryData.pageTp == 'PAGE_TP.CATEGORY') {
        // 페이지유형:카테고리
        if (selectedPageData != undefined && selectedPageData != null) {
          // 선택페이지정보존재
          if (selectedPageData.dpPageId != undefined && selectedPageData.dpPageId != null) {
            contsObj.ilCtgryId      = selectedPageData.dpPageId;
          }
        }
      }
    }
    else {
      //console.log('# selectedInventoryData is Null');
    }

    fnExcelDownConts(contsObj);
  }
  // ==========================================================================
  // # 버튼 - 콘텐츠2그리드-엑셀다운로드
  // ==========================================================================
  function fnBtnExcelDownContsLv2() {

    if (contsLv2Grid != null && contsLv2Grid.dataSource != null) {

      if (contsLv2Grid.dataSource._view.length <= 0) {
        fnMessage('', '다운로드할 콘텐츠(Lv.2) 목록이 없습니다.', '');
        return false;
      }
    }
    else {
      fnMessage('', '콘텐츠(Lv.2)를 먼저 조회해 주십시오...', '');
      return false;
    }

    var contsObj = new Object();
    contsObj.contsLevel     = 2;
    contsObj.dpInventoryId;
    contsObj.prntsContsId;
    contsObj.dpRangeTp      = $('#contsLv2DpRangeTp').val();
    contsObj.ilCtgryId      = '';
    contsObj.status         = $('#contsLv2Status').val();
    contsObj.contsTp        = '';

    if (selectedInventoryData != undefined && selectedInventoryData != null) {
      //console.log('# selectedInventoryData :: ' + JSON.stringify(selectedInventoryData));
      // 인벤토리ID
      if (selectedInventoryData.dpInventoryId != undefined && selectedInventoryData.dpInventoryId != null) {
        contsObj.dpInventoryId  = selectedInventoryData.dpInventoryId;
      }

      // 콘텐츠유형
      if (selectedInventoryData.contsLevel2Tp != null) {
        contsObj.contsTp = selectedInventoryData.contsLevel2Tp;
      }

      // 카테고리ID
      if (selectedInventoryData.pageTp == 'PAGE_TP.CATEGORY') {
        // 페이지유형:카테고리
        if (selectedPageData != undefined && selectedPageData != null) {
          // 선택페이지정보존재
          if (selectedPageData.dpPageId != undefined && selectedPageData.dpPageId != null) {
            contsObj.ilCtgryId      = selectedPageData.dpPageId;
          }
        }
      }
    }
    else {
      //console.log('# selectedInventoryData is Null');
    }
    // 상위콘텐츠ID
    if (selectedContsLv1Data != undefined && selectedContsLv1Data != null) {
      if (selectedContsLv1Data.dpContsId != undefined && selectedContsLv1Data.dpContsId != null) {
        contsObj.prntsContsId  = selectedContsLv1Data.dpContsId;
      }
    }
    else {
      //console.log('# selectedContsLv1Data is Null');
    }

    fnExcelDownConts(contsObj);
  }
  // ==========================================================================
  // # 버튼 - 콘텐츠3그리드-엑셀다운로드
  // ==========================================================================
  function fnBtnExcelDownContsLv3() {

    if (contsLv3Grid != null && contsLv3Grid.dataSource != null) {

      if (contsLv3Grid.dataSource._view.length <= 0) {
        fnMessage('', '다운로드할 콘텐츠(Lv.3) 목록이 없습니다.', '');
        return false;
      }
    }
    else {
      fnMessage('', '콘텐츠(Lv.3)를 먼저 조회해 주십시오...', '');
      return false;
    }

    var contsObj = new Object();
    contsObj.contsLevel     = 3;
    contsObj.dpInventoryId;
    contsObj.prntsContsId;
    contsObj.dpRangeTp      = $('#contsLv3DpRangeTp').val();
    contsObj.ilCtgryId      = '';
    contsObj.status         = $('#contsLv3Status').val();
    contsObj.contsTp        = '';

    if (selectedInventoryData != undefined && selectedInventoryData != null) {
      //console.log('# selectedInventoryData :: ' + JSON.stringify(selectedInventoryData));
      // 인벤토리ID
      if (selectedInventoryData.dpInventoryId != undefined && selectedInventoryData.dpInventoryId != null) {
        contsObj.dpInventoryId  = selectedInventoryData.dpInventoryId;
      }

      // 콘텐츠유형
      if (selectedInventoryData.contsLevel3Tp != null) {
        contsObj.contsTp = selectedInventoryData.contsLevel3Tp;
      }

      // 카테고리ID
      if (selectedInventoryData.pageTp == 'PAGE_TP.CATEGORY') {
        // 페이지유형:카테고리
        if (selectedPageData != undefined && selectedPageData != null) {
          // 선택페이지정보존재
          if (selectedPageData.dpPageId != undefined && selectedPageData.dpPageId != null) {
            contsObj.ilCtgryId      = selectedPageData.dpPageId;
          }
        }
      }
    }
    else {
      //console.log('# selectedInventoryData is Null');
    }
    // 상위콘텐츠ID
    if (selectedContsLv2Data != undefined && selectedContsLv2Data != null) {
      if (selectedContsLv2Data.dpContsId != undefined && selectedContsLv2Data.dpContsId != null) {
        contsObj.prntsContsId  = selectedContsLv2Data.dpContsId;
      }
    }
    else {
      //console.log('# selectedContsLv2Data is Null');
    }

    fnExcelDownConts(contsObj);
  }
  // ==========================================================================
  // # 엑셀다운로드 - 콘텐츠1/2/3
  // ==========================================================================
  function fnExcelDownConts(contsObj) {
    //console.log('# 콘텐츠 엑셀다운로드 :: ', JSON.stringify(contsObj));
    //console.log('# PG_SESSION :: ', JSON.stringify(PG_SESSION));
    //세션 체크
    if (PG_SESSION == null || PG_SESSION == '' || PG_SESSION == undefined || PG_SESSION == 'undefined') {
      console.log('# PG_SESSION :: ' + JSON.stringify(PG_SESSION));
      //location.href = "/layout.html#/goodsMgm?ilGoodsId="+data.ilGoodsId;
      fnKendoMessage({ message : "세션이 종료되었습니다. \n다시 로그인 해 주십시오.", ok : function (){
        location.href = "/admVerify.html";
      }});
      return false;
    }
    //console.log('# comboPageTp :: ', $('#comboPageTp').val());
    //console.log('# 콘텐츠엑셀다운 :: ', JSON.stringify(selectedInventoryData));

    var inParamContsLevel     = contsObj.contsLevel;
    var inParamDpInventoryId  = contsObj.dpInventoryId;
    var inParamPrntsContsId   = contsObj.prntsContsId;
    var inParamDpRangeTp      = contsObj.dpRangeTp;
    var inParamIlCtgryId      = contsObj.ilCtgryId;
    var inParamStatus         = contsObj.status;
    var inParamContsTp        = contsObj.contsTp;
    var inParamInventoryNm    = selectedInventoryData.inventoryNm;
    var inParamInventoryCd    = selectedInventoryData.inventoryCd;
    var inParamDpStartDt      = '';
    var inParamDpEndDt        = '';

    // 검색기간 조건 - 시작
    if ($('#dpStartDtLv3').val() == null || $('#dpStartDtLv3').val() == undefined || $('#dpStartDtLv3').val() == '') {
      inParamDpStartDt = '';
    }
    else {
      inParamDpStartDt = ($('#dpStartDtLv3').val()).replace(/-/gi, '') + $('#dpStartHourPickerLv3').val() + $('#dpStartMinPickerLv3').val() + '00';
    }
    // 검색기간 조건 - 종료
    if ($('#dpEndDtLv3').val() == null || $('#dpEndDtLv3').val() == undefined || $('#dpEndDtLv3').val() == '') {
      // 년월일이 공백이면 종료일 조건 없음
      inParamDpEndDt = '';
    }
    else {
      inParamDpEndDt = ($('#dpEndDtLv3').val()).replace(/-/gi, '') + $('#dpEndHourPickerLv3').val() + $('#dpEndMinPickerLv3').val() + '59';
    }

    var url = '/admin/display/manage/getExportExcelContsList?' + 'dpInventoryId='    + inParamDpInventoryId
            + '&prntsContsId='    + inParamPrntsContsId
            + '&contsLevel='      + inParamContsLevel
            + '&dpStartDt='       + inParamDpStartDt
            + '&dpEndDt='         + inParamDpEndDt
            + '&dpRangeTp='       + inParamDpRangeTp
            + '&ilCtgryId='       + inParamIlCtgryId
            + '&status='          + inParamStatus
            + '&contsTp='         + inParamContsTp
            + '&inventoryNm='     + inParamInventoryNm
            + '&inventoryCd='     + inParamInventoryCd
            ;
    var data = $('#inputFormExcelConts').formSerialize(true);
    fnExcelDownload(url, data);
  }

  // ==========================================================================
  // # 버튼 - 그리드-인벤토리미리보기
  // ==========================================================================
// TODO : 미리보기로 변경 대상
  function fnBtnInventoryPreview(dataItem) {
    fnMessage('', '인벤토리 미리보기 공사중입니다...', '');
    return false;
  }


  // ==========================================================================
  // # 버튼-콘텐츠신규팝업 - Lv1/Lv2/Lv3
  // ==========================================================================
  function fnBtnContsLv1New() {
    // 콘텐츠Lv1
    $('#mode').val("conts.insert");
    selectedContsLevel = 1;
    fnPopupOpen(selectedInventoryData.contsLevel1Tp, null);
  }
  function fnBtnContsLv2New() {
    // 콘텐츠Lv2
    $('#mode').val("conts.insert");
    selectedContsLevel = 2;
    fnPopupOpen(selectedInventoryData.contsLevel2Tp, null);
  }
  function fnBtnContsLv3New() {
    // 콘텐츠Lv3
    $('#mode').val("conts.insert");
    selectedContsLevel = 3;
    fnPopupOpen(selectedInventoryData.contsLevel3Tp, null);
  }

  // ==========================================================================
  // # 파일업로드초기화 - 배너 && 에디터 이미지 업로드시 사용할 kendoUpload 컴포넌트 초기화
  // ==========================================================================
  function fnInitKendoUpload() {

    var uploadFileTagIdList = ['imgMobileBanner', 'gifImgMobileBanner', 'imgPcBanner', 'gifImgPcBanner', 'imgMobileBrand', 'imgPcBrand'];

    var selectFunction = function(e) {

      if (e.files && e.files[0]) {
        // 이미지 파일 선택시

        if (bannerImageUploadMaxLimit < e.files[0].size) { // 배너 이미지 업로드 용량 체크
          fnKendoMessage({
            message : '이미지 업로드 허용 최대 용량은 ' + parseInt(bannerImageUploadMaxLimit / 1024) + ' kb 입니다.',
            ok : function(e) {}
          });
          return;
        }

        // --------------------------------------------------------------------
        // 확장자 2중 체크 위치
        // --------------------------------------------------------------------
        // var imageExtension = e.files[0]['extension'].toLowerCase();
        // 전역변수에 선언한 허용 확장자와 비교해서 처리
        // itemMgmController.js 의 allowedImageExtensionList 참조

        //  켄도 이미지 업로드 확장자 검사
        if(!validateExtension(e)) {
          fnKendoMessage({
            message : '허용되지 않는 확장자 입니다.',
            ok : function(e) {}
          });
          return;
        }

        var fileTagId = e.sender.element[0].id;
        let reader = new FileReader();

        reader.onload = function(ele) {
          var itemImageScr = ele.target.result; // FileReader 로 생성한 상품 이미지 url
          var file = e.files[0].rawFile; // kendoUpload 로 가져온 상품 이미지 file 객체

          fnUploadBannerImage(file, fileTagId);
        };

        reader.readAsDataURL(e.files[0].rawFile);

      } // End of if (e.files && e.files[0])
    } // End of var selectFunction = function(e)

    for (var i = 0; i < uploadFileTagIdList.length; i++) {

      fnKendoUpload({
          id : uploadFileTagIdList[i]
        , select : selectFunction
      });
    } // End of for (var i = 0; i < uploadFileTagIdList.length; i++)


    // ------------------------------------------------------------------------
    // 에디터 파일 업로드 - Editor 의 이미지 첨부 File Tag 를 kendoUpload 로 초기화
    // ------------------------------------------------------------------------
    // 에디터에 파일 업로드 고려 안함(이석호M, 2020.11.03)
    //fnKendoUpload({
    //    id : "uploadImageOfEditor"
    //  , select :  function(e) {
    //
    //                if (e.files && e.files[0]) { // 이미지 파일 선택시
    //
    //                  // bannerImageUploadMaxLimit : 이미지 업로드 / 첨부 최대 용량 ( 단위 : byte )
    //                  if (bannerImageUploadMaxLimit < e.files[0].size) { // 용량 체크
    //                    fnKendoMessage({
    //                      message : '이미지 업로드 허용 최대 용량은 ' + parseInt(bannerImageUploadMaxLimit / 1048576) + ' MB 입니다.',
    //                      ok : function(e) {}
    //                    });
    //                    return;
    //                  }
    //
    //                  let reader = new FileReader();
    //                  reader.onload = function(ele) {
    //                    fnUploadImageOfEditor(); // 선택한 이미지 파일 업로드 함수 호출
    //                  };
    //
    //                  reader.readAsDataURL(e.files[0].rawFile);
    //                }
    //              }
    //});

  }

  // ==========================================================================
  // 파일업로드 처리
  // ==========================================================================
  // NOTE 파일 업로드 이벤트
  function fnUploadBannerImage(file, fileTagId) {
    //console.log('# fnUploadBannerImage Start[', fileTagId, '] :: ', file);

    var formData = new FormData();
    formData.append('bannerImage', file);
    formData.append('storageType', 'public'); // storageType 지정
    formData.append('domain', 'dp');          // domain 지정 - 전시
    // 관련 Class : BosStorageInfoEnum, BosDomainPrefixEnum

    $.ajax({
        url         : '/comn/fileUpload'
      , data        : formData
      , type        : 'POST'
      , contentType : false
      , processData : false
      , async       : false
      , success     : function(data) {

                        data = data.data;

                        var originalFileName  = '';
                        var fullPath          = '';

                        // ----------------------------------------------------
                        // 업로드파일 정보 Set
                        // ----------------------------------------------------
                        //console.log('# Uplaod Result          :: ', data['addFile'][0]);
                        //console.log('# fileTagId              :: ', fileTagId);
                        //console.log('# data.OriginalFileName  :: ', data['addFile'][0].originalFileName);
                        //console.log('# data.serverSubPath     :: ', data['addFile'][0].serverSubPath);
                        //console.log('# daga.physicalFileName  :: ', data['addFile'][0].physicalFileName);
                        //console.log('# fnUploadBannerImage Result[', fileTagId, '] :: ', JSON.stringify(file));

                        originalFileName = data['addFile'][0].originalFileName;
                        fullPath         = data['addFile'][0].serverSubPath + data['addFile'][0].physicalFileName;

                        if (fileTagId == 'imgMobileBanner') {
                          // 배너-이미지1
                          imgPathMobileBanner     = fullPath;             // 이미지1-풀경로
                          imgOriginNmMobileBanner = originalFileName;     // 이미지1-원본파일명
                        }
                        else if (fileTagId == 'gifImgMobileBanner') {
                          // 배너-이미지gif모바일
                          gifImgPathMobileBanner      = fullPath;         // 이미지gif모바일-풀경로
                          gifImgOriginNmMobileBanner  = originalFileName; // 이미지gif모바일-원본파일명
                        }
                        else if (fileTagId == 'imgPcBanner') {
                          // 배너-이미지2
                          imgPathPcBanner     = fullPath;                 // 이미지2-풀경로
                          imgOriginNmPcBanner = originalFileName;         // 이미지2-원본파일명
                        }
                        else if (fileTagId == 'gifImgPcBanner') {
                          // 배너-이미지gifPC
                          gifImgPathPcBanner      = fullPath;             // 이미지gifPC-풀경로
                          gifImgOriginNmPcBanner  = originalFileName;     // 이미지gifPC-원본파일명
                        }
                        else if (fileTagId == 'imgMobileBrand') {
                          // 브랜드-이미지1
                          imgPathMobileBrand     = fullPath;             // 이미지1-풀경로
                          imgOriginNmMobileBrand = originalFileName;     // 이미지1-원본파일명
                        }
                        else if (fileTagId == 'imgPcBrand') {
                          // 브랜드-이미지2
                          imgPathPcBrand     = fullPath;                 // 이미지2-풀경로
                          imgOriginNmPcBrand = originalFileName;         // 이미지2-원본파일명
                        }

                        // ----------------------------------------------------
                        // 업로드파일 노출
                        // ----------------------------------------------------
                        var imageUrl = publicStorageUrl + data['addFile'][0].serverSubPath + data['addFile'][0].physicalFileName;
                        $('#'+ fileTagId +'View').attr('src', imageUrl);
                        $('#'+ fileTagId +'View').closest('.fileUpload__imgWrapper').show();

                        // ----------------------------------------------------
                        // 업로드파일 제목 노출
                        // ----------------------------------------------------
                        var $title = $('#'+ fileTagId +'View').closest('.fileUpload-container').find(".fileUpload__title");
                        var $message = $('#'+ fileTagId +'View').closest('.fileUpload-container').find(".fileUpload__message");

                        $title.text(originalFileName);
                        $message.hide();
                        $title.show();
                      }
    });
  }

  // ==========================================================================
  // 에디터 초기화
  // ==========================================================================
  function fnInitKendoEditor(opt) {

    if  ( $('#' + opt.id).data("kendoEditor") ) { // 기존에 이미 Editor 로 생성되어 있는 경우 초기화

      $('#' + opt.id + 'Div').html('');  // 해당 editor TextArea 를 가지고 있는 td 내의 html 을 강제 초기화
      var textAreaHtml = '<textarea class="comm-textarea" id="' + opt.id + '" name="' + opt.id + '" style="height:100px;"></textarea>';
      $('#' + opt.id + 'Div').append(textAreaHtml);  // 새로운 editor TextArea 를 추가
    }

    $('#' + opt.id).kendoEditor({
        tools : [ { name : 'viewHtml'           , tooltip : 'HTML 소스보기'   }
                , { name : 'bold'               , tooltip : '진하게'          }
                , { name : 'italic'             , tooltip : '이탤릭'          }
                , { name : 'underline'          , tooltip : '밑줄'            }
                , { name : 'strikethrough'      , tooltip : '취소선'          }
                  //------------------- 구분선 ----------------------
                , { name : 'foreColor'          , tooltip : '글자색상'        }
                , { name : 'backColor'          , tooltip : '배경색상'        }
                  //------------------- 구분선 ----------------------
                , { name : 'justifyLeft'        , tooltip : '왼쪽 정렬'       }
                , { name : 'justifyCenter'      , tooltip : '가운데 정렬'     }
                , { name : 'justifyRight'       , tooltip : '오른쪽 정렬'     }
                , { name : 'justifyFull'        , tooltip : '양쪽 맞춤'       }
                  //------------------- 구분선 ----------------------
                , { name : 'insertUnorderedList', tooltip : '글머리기호'      }
                , { name : 'insertOrderedList'  , tooltip : '번호매기기'      }
                , { name : 'indent'             , tooltip : '들여쓰기'        }
                , { name : 'outdent'            , tooltip : '내어쓰기'        }
                  //------------------- 구분선 ----------------------
                , { name : 'createLink'         , tooltip : '하이퍼링크 연결' }
                , { name : 'unlink'             , tooltip : '하이퍼링크 제거' }
                , { name : 'insertImage'        , tooltip : '이미지 URL 첨부' }
                //, { name : 'file-image'         , tooltip : '이미지 파일 첨부'
                //                                , exec :  function(e) {
                //                                            e.preventDefault();
                //                                            workindEditorId = opt.id; // 이미지 첨부할 Editor 의 id 를 전역변수에 저장
                //                                            $('#uploadImageOfEditor').trigger('click'); // 파일 input Tag 클릭 이벤트 호출
                //                                          }
                //  }
                  //------------------- 구분선 ----------------------
                , { name : 'subscript'          , tooltip : '아래 첨자'       }
                , { name : 'superscript'        , tooltip : '위 첨자'         }
                  //------------------- 구분선 ----------------------
                , { name : 'tableWizard'    , tooltip : '표 수정' }
                , { name : 'createTable'        , tooltip : '표 만들기'       }
                , { name : 'addRowAbove'        , tooltip : '위 행 추가'      }
                , { name : 'addRowBelow'        , tooltip : '아래 행 추가'    }
                , { name : 'addColumnLeft'      , tooltip : '왼쪽 열 추가'    }
                , { name : 'addColumnRight'     , tooltip : '오른쪽 열 추가'  }
                , { name : 'deleteRow'          , tooltip : '행 삭제'         }
                , { name : 'deleteColumn'       , tooltip : '열 삭제'         }
                , { name : 'mergeCellsHorizontally'   , tooltip : '수평으로 셀 병합' }
    	        , { name : 'mergeCellsVertically'   , tooltip : '수직으로 셀 병합' }
    	        , { name : 'splitCellHorizontally'   , tooltip : '수평으로 셀 분할' }
    	        , { name : 'splitCellVertically'   , tooltip : '수직으로 셀 분할' }
                  //------------------- 구분선 ----------------------
                , 'formatting'
                , 'fontName'

                , { name  : 'fontSize'
                  , items : [ { text :  '8px', value :  '8px' }
                            , { text :  '9px', value :  '9px' }
                            , { text : '10px', value : '10px' }
                            , { text : '11px', value : '11px' }
                            , { text : '12px', value : '12px' }
                            , { text : '13px', value : '13px' }
                            , { text : '14px', value : '14px' }
                            , { text : '16px', value : '16px' }
                            , { text : '18px', value : '18px' }
                            , { text : '20px', value : '20px' }
                            , { text : '22px', value : '22px' }
                            , { text : '24px', value : '24px' }
                            , { text : '26px', value : '26px' }
                            , { text : '28px', value : '28px' }
                            , { text : '36px', value : '36px' }
                            , { text : '48px', value : '48px' }
                            , { text : '72px', value : '72px' }
                            ]
                  }
                ]
              , messages  : { formatting          : '포맷'
                            , formatBlock         : '포맷을 선택하세요.'
                            , fontNameInherit     : '폰트'
                            , fontName            : '글자 폰트를 선택하세요.'
                            , fontSizeInherit     : '글자크기'
                            , fontSize            : '글자 크기를 선택하세요.'
                            , print               : '출력'
                            , imageWebAddress     : '웹 주소'
                            , imageAltText        : '대체 문구'
                            , fileWebAddress      : '웹 주소'
                            , fileTitle           : '링크 문구'
                            , linkWebAddress      : '웹 주소'
                            , linkText            : '선택 문구'
                            , linkToolTip         : '풍선 도움말'
                            , linkOpenInNewWindow : '새 창에서 열기'
                            , dialogInsert        : '적용'
                            , dialogUpdate        : 'Update'
                            , dialogCancel        : '닫기'
                            }
    });

    $('<br/>').insertAfter($('.k-i-create-table').closest('li'));
  }

  // ==========================================================================
  // 에디터용 파일업로드 - 에디터에서 파일업로드 사용안함에 따른 주석처리
  // ==========================================================================
  //function fnUploadImageOfEditor(opt) {
  //  // Editor 에 첨부된 이미지 Upload 후 반환된 url 을 Editor 내에 img 태그로 추가
  //
  //  var formData = $('#uploadImageOfEditorForm').formSerialize(true);
  //
  //  fnAjaxSubmit({
  //      form        : "uploadImageOfEditorForm"
  //    , fileUrl     : "/fileUpload"
  //    , method      : 'GET'
  //    , url         : '/comn/getPublicStorageUrl'
  //    , storageType : "public"
  //    , domain      : "dp"
  //    , params      : formData
  //    , success     : function(result) {
  //
  //                      var uploadResult = result['addFile'][0];
  //                      var serverSubPath = uploadResult['serverSubPath'];
  //                      var physicalFileName = uploadResult['physicalFileName'];
  //                      var imageSrcUrl = result['publicStorageUrl'] + serverSubPath + physicalFileName; // 업로드 이미지 url
  //
  //                      var editor = $('#' + workindEditorId).data("kendoEditor"); // 이미지 첨부할 Editor
  //                      editor.exec('inserthtml', {
  //                        value : '<img src="' + imageSrcUrl + '" />'
  //                      });
  //
  //                    }
  //  , isAction : 'insert'
  //  });
  //}

  // **************************************************************************
  // ------------------------------- POPUP START ------------------------------
  // **************************************************************************
  // ==========================================================================
  // # 팝업 - 열기
  // ==========================================================================
  function fnPopupOpen(contsTp, selectContsData){
    //console.log('# fnPopupOpen Start ', ', selectedContsLevel::', selectedContsLevel, ', contsTp::', contsTp);

    // 상위콘텐츠 선택 체크
    var selectedPrntContsData;

    if (selectedContsLevel == 2 || selectedContsLevel == 3) {

      if (selectedContsLevel == 2) {
        selectedPrntContsData = selectedContsLv1Data;
      }
      else if (selectedContsLevel == 3) {
        selectedPrntContsData = selectedContsLv2Data;
      }

      if (selectedPrntContsData == null || selectedPrntContsData == '' || selectedPrntContsData.dpContsId == null || selectedPrntContsData.dpContsId == '') {
        fnMessage('', 'Lv.' + (selectedContsLevel - 1) + ' 콘텐츠를 선택해주세요.', '')
        return false;
      }
    }
    //console.log('# fnPopupOpen mode    :: ', $('#mode').val());
    //console.log('# fnPopupOpen contsTp :: ', contsTp);

    // 콘텐츠유형 Set
    selectedContsTp = contsTp;

    // 모든 팝업 숨김
    // Text
    $('#contsPopupText').hide();
    // Html
    $('#contsPopupHtml').hide();
    // Banner
    $('#contsPopupBanner').hide();
    // Brand
    $('#contsPopupBrand').hide();
    // Category
    $('#contsPopupCategory').hide();
    // Goods
    $('#contsPopupGoods').hide();
    // 미리보기
    $('#previewPopup').hide();
    // 인벤토리그룹관리
    $('#inventoryGroupPopup').hide();

    // 콘텐츠유형별 팝업 호출
    if (contsTp == 'DP_CONTENTS_TP.TEXT') {
      // 1.TEXT
      fnInitPopupText(selectContsData);
    }
    else if (contsTp == 'DP_CONTENTS_TP.HTML') {
      // 2.HTML
      fnInitPopupHtml(selectContsData);
    }
    else if (contsTp == 'DP_CONTENTS_TP.BANNER') {
      // 3.BANNER
      fnInitPopupBanner(selectContsData);
    }
    else if (contsTp == 'DP_CONTENTS_TP.BRAND') {
      // 4.BRAND
      fnInitPopupBrand(selectContsData);
    }
    else if (contsTp == 'DP_CONTENTS_TP.CATEGORY') {
      // 5.CATEGORY
      fnInitPopupCategory(selectContsData);
    }
    else if (contsTp == 'DP_CONTENTS_TP.GOODS') {
      // 6.GOODS
      fnInitPopupGoods(selectContsData)
    }

    // 전시기간
    if ($('#mode').val() != null && $('#mode').val() == 'conts.insert') {
      // 기간버튼 활성화
      $(".date-controller").attr('style', '');
    } else {
      // 기간버튼 비 활성화
      $(".date-controller").attr('style', 'display: none');
    }

    // 수정의 경우 텍스트 값이 존재하므로 길이 텍스트 표시
    $(".currentInput-length").each(function() {
      const $limitControl = $(this).closest(".limit-control");

      const input = $limitControl.length ? $limitControl.find(".lengthCheck") : null;

      if( !!input.length ) {
        $(this).text(input.val().length);
      }
    })
  }

  // ==========================================================================
  // 팝업-초기화/오픈-Text
  // ==========================================================================
  function fnInitPopupText(selectContsData) {

    // ------------------------------------------------------------------------
    // * 팝업제목
    // ------------------------------------------------------------------------
    var contsTpNm = "TEXT";
    var modeNm =  ($('#mode').val() == 'conts.insert') ? '등록' :
                  ($('#mode').val() == 'conts.update') ? '수정' :
                  ($('#mode').val() == 'conts.delete') ? '삭제' : '';

    //var popupTitle = '콘텐츠 Lv.' + selectedContsLevel + ' ' + modeNm + ' : [' + contsTpNm + '] ' + selectedInventoryData.inventoryNm;
    //if ($('#mode').val() != 'conts.insert') {
    //  if (selectContsData != null) {
    //    popupTitle += ' > ' + selectContsData.titleNm;
    //  }
    //}
    var popupTitle = '콘텐츠 등록';
    var postFix = 'Text';

    // ------------------------------------------------------------------------
    // 팝업상단 인벤토리정보 Set
    // ------------------------------------------------------------------------
    if (selectedInventoryData != null && selectedInventoryData != 'null' && selectedInventoryData != '') {
      // 페이지경로
      $('#divPagePath'+postFix).text(selectedInventoryData.pageFullPath);
      // 인벤토리명(코드)
      $('#divInventoryTitle'+postFix).text(selectedInventoryData.inventoryNm + ' (' + selectedInventoryData.inventoryCd + ')');
      // 구좌타입
      $('#divContsType'+postFix).text('Lv.' + selectedContsLevel + ' [' + contsTpNm + ']');
    }

    // ------------------------------------------------------------------------
    // * 변수초기화
    // ------------------------------------------------------------------------
    // 전시기간
    var dpStartDt;
    var dpStartDe   = '';
    var dpStartHour = 0;
    var dpStartMin  = 0;
    var dpEndDt;
    var dpEndDe     = '';
    var dpEndHour   = 0;
    var dpEndMin    = 0;
    // 전시범위
    var dpRangeTp;
    // 타이틀명
    var titleNm;
    // 링크URL
    var linkUrlPc;
    // 노출텍스트1
    var text1;
    var text1Color;
    // 노출텍스트2
    var text2;
    var text2Color;
    // 노출텍스트3
    var text3;
    var text3Color;
    // 순번
    var sort;
    // 구좌상세정보
    var contsDesc;

    if ($('#mode').val() != null && $('#mode').val() == 'conts.update') {
      // 수정인 경우 값 Set
      if (selectContsData != null && selectContsData != '') {

        // 전시기간-시작정보
        if (selectContsData.dpStartDt != undefined && selectContsData.dpStartDt != null && selectContsData.dpStartDt != '') {

          var strDpStartHour = '';
          var strDpStartMin = '';
          var len = selectContsData.dpStartDt.length;
          //console.log('# dpStartDt.len :: ', len);
          if (len >= 10) {
            dpStartDe   = selectContsData.dpStartDt.substring(0, 10);
          }
          if (len >= 13) {
            strDpStartHour = selectContsData.dpStartDt.substring(11, 13);
            dpStartHour    = Number(strDpStartHour);
          }
          if (len >= 16) {
            strDpStartMin = selectContsData.dpStartDt.substring (14, 16);
            dpStartMin    = Number(strDpStartMin);
          }
        }
        // 전시기간-종료정보
        if (selectContsData.dpEndDt != undefined && selectContsData.dpEndDt != null && selectContsData.dpEndDt != '') {

          var strDpEndHour = '';
          var strDpEndMin = '';
          var len = selectContsData.dpEndDt.length;
          //console.log('# dpEndDt.len :: ', len);
          if (len >= 10) {
            dpEndDe   = selectContsData.dpEndDt.substring(0, 10);
          }
          if (len >= 13) {
            strDpEndHour = selectContsData.dpEndDt.substring(11, 13);
            dpEndHour    = Number(strDpEndHour);
          }
          if (len >= 16) {
            strDpEndMin = selectContsData.dpEndDt.substring (14, 16);
            dpEndMin    = Number(strDpEndMin);
          }
        }
        // 전시범위
        dpRangeTp   = selectContsData.dpRangeTp;
        // 타이틀명
        titleNm     = selectContsData.titleNm;
        // 링크URL
        linkUrlPc   = selectContsData.linkUrlPc;
        // 노출텍스트1
        text1       = selectContsData.text1;
        text1Color  = selectContsData.text1Color;
        // 노출텍스트2
        text2       = selectContsData.text2;
        text2Color  = selectContsData.text2Color;
        // 노출텍스트3
        text3       = selectContsData.text3;
        text3Color  = selectContsData.text3Color;
        // 순번
        sort        = selectContsData.sort;
      }
    }

    // ------------------------------------------------------------------------
    // * 변수값 Set
    // ------------------------------------------------------------------------
    // 전시기간
    if ($('#mode').val() != null && $('#mode').val() == 'conts.insert') {
      // 신규
      $("#dpStartDt"+postFix).data("kendoDatePicker").value(baseStartDe);
      $("#dpStartHour"+postFix).data("kendoDropDownList").select(0);
      $("#dpStartMin"+postFix).data("kendoDropDownList").select(0);
      $("#dpEndDt"+postFix).data("kendoDatePicker").value(afterSixDe);
      $("#dpEndHour"+postFix).data("kendoDropDownList").select(23);
      $("#dpEndMin"+postFix).data("kendoDropDownList").select(59);

      // 시작일자 활성
      $('#dpStartDt'+postFix).data("kendoDatePicker").enable(true);
      $('#dpStartHour'+postFix).data("kendoDropDownList").enable(true);
      $('#dpStartMin'+postFix).data("kendoDropDownList").enable(true);

      // 기간버튼 1주일로 선택
      $('#inputContsFormText').find('[fb-btn-active]').attr('fb-btn-active', false);        // 모두 미선택
      $('#inputContsFormText').find('[data-id="fnDateBtn3"]').attr('fb-btn-active', true);  // 1주일 선택

    }
    else {
      // 수정
      $("#dpStartDt"+postFix).data("kendoDatePicker").value(dpStartDe);
      $("#dpStartHour"+postFix).data("kendoDropDownList").select(dpStartHour);
      $("#dpStartMin"+postFix).data("kendoDropDownList").select(dpStartMin);
      $("#dpEndDt"+postFix).data("kendoDatePicker").value(dpEndDe);
      $("#dpEndHour"+postFix).data("kendoDropDownList").select(dpEndHour);
      $("#dpEndMin"+postFix).data("kendoDropDownList").select(dpEndMin);

      // 시작일자 활성
      $('#dpStartDt'+postFix).data("kendoDatePicker").enable(true);
      $('#dpStartHour'+postFix).data("kendoDropDownList").enable(true);
      $('#dpStartMin'+postFix).data("kendoDropDownList").enable(true);
    }
    // 전시범위
    $("#dpRangeTp"+postFix).data("kendoDropDownList").value(dpRangeTp);
    //$("#dpRangeTpText").empty();
    //fnTagMkRadio({
    //    id      : 'dpRangeTpText'
    //  , url     : "/admin/comn/getCodeList"
    //  , params  : {"stCommonCodeMasterCode" : "DP_RANGE_TP", "useYn" :"Y"}
    //  , tagId   : 'dpRangeTpText'
    //  , async   : false
    //  , chkVal  : dpRangeTp
    //  , style   : {}
    //});
    //if (dpRangeTp == undefined || dpRangeTp == null || dpRangeTp == '') {
    //  dpRangeTp = 'DP_RANGE_TP.PC';
    //}
    //$('input:radio[name="dpRangeTpText"]:input[value="'+dpRangeTp+'"]').prop("checked", true);
    // 타이틀명
    $('#titleNm'+postFix).val(titleNm);
    // 링크URL
    $('#linkUrlPc'+postFix).val(linkUrlPc);
    // 노출텍스트1
    $('#text1'+postFix).val(text1);
    $('#text1Color'+postFix).val(text1Color);
    // 노출텍스트2
    $('#text2'+postFix).val(text2);
    $('#text2Color'+postFix).val(text2Color);
    // 노출텍스트3
    $('#text3'+postFix).val(text3);
    $('#text3Color'+postFix).val(text3Color);
    // 순번
    $('#sort'+postFix).val(sort);
    // 구좌상세정보
    if (selectedContsLevel == 1) {
      contsDesc = selectedInventoryData.contsLevel1Desc;
    }
    else if (selectedContsLevel == 2) {
      contsDesc = selectedInventoryData.contsLevel2Desc;
    }
    else if (selectedContsLevel == 3) {
      contsDesc = selectedInventoryData.contsLevel3Desc;
    }
    $('#contsDesc'+postFix).val(contsDesc);

    // ------------------------------------------------------------------------
    // * 팝업노출
    // ------------------------------------------------------------------------
    $('#contsPopup'+postFix).show();
    // * 팝업오픈
    document.documentElement.scrollTop = 0;
    document.documentElement.scrollTop = 0;
    //$('#inputForm').formClear(true);
    fnKendoInputPoup({height:"auto" ,width:"860px", title:{ nullMsg : popupTitle } } );
    // $("#kendoPopup").css("max-height", "800px");
  }

  // ==========================================================================
  // 팝업-초기화/오픈-Html
  // ==========================================================================
  function fnInitPopupHtml(selectContsData) {

    // ------------------------------------------------------------------------
    // * 팝업제목
    // ------------------------------------------------------------------------
    var contsTpNm = "HTML";
    var modeNm =  ($('#mode').val() == 'conts.insert') ? '등록' :
                  ($('#mode').val() == 'conts.update') ? '수정' :
                  ($('#mode').val() == 'conts.delete') ? '삭제' : '';
    //var popupTitle = '콘텐츠 Lv.' + selectedContsLevel + ' ' + modeNm + ' : [' + contsTpNm + '] ' + selectedInventoryData.inventoryNm;
    //if ($('#mode').val() != 'conts.insert') {
    //  if (selectContsData != null) {
    //    popupTitle += ' > ' + selectContsData.titleNm;
    //  }
    //}
    var popupTitle = '콘텐츠 등록';
    var postFix = 'Html';

    // ------------------------------------------------------------------------
    // 팝업상단 인벤토리정보 Set
    // ------------------------------------------------------------------------
    if (selectedInventoryData != null && selectedInventoryData != 'null' && selectedInventoryData != '') {
      // 페이지경로
      $('#divPagePath'+postFix).text(selectedInventoryData.pageFullPath);
      // 인벤토리명(코드)
      $('#divInventoryTitle'+postFix).text(selectedInventoryData.inventoryNm + ' (' + selectedInventoryData.inventoryCd + ')');
      // 구좌타입
      $('#divContsType'+postFix).text('Lv.' + selectedContsLevel + ' [' + contsTpNm + ']');
    }

    // ------------------------------------------------------------------------
    // * 변수초기화
    // ------------------------------------------------------------------------
    // 전시기간
    var dpStartDt;
    var dpStartDe   = '';
    var dpStartHour = 0;
    var dpStartMin  = 0;
    var dpEndDt;
    var dpEndDe     = '';
    var dpEndHour   = 0;
    var dpEndMin    = 0;
    // 전시범위
    var dpRangeTp;
    // 타이틀명
    var titleNm;
    // HTML-PC
    var htmlPc;
    // HTML-Mobile
    var htmlMobile;
    // 순번
    var sort;
    // 구좌상세정보
    var contsDesc;
    // 에디터-PC
    fnInitKendoEditor({id : 'htmlPcHtml'});
    // 에디터-MObile
    fnInitKendoEditor({id : 'htmlMobileHtml'});

    if ($('#mode').val() != null && $('#mode').val() == 'conts.update') {
      // 수정인 경우 값 Set
      if (selectContsData != null && selectContsData != '') {

        // 전시기간-시작정보
        if (selectContsData.dpStartDt != undefined && selectContsData.dpStartDt != null && selectContsData.dpStartDt != '') {

          var strDpStartHour = '';
          var strDpStartMin = '';
          var len = selectContsData.dpStartDt.length;
          //console.log('# dpStartDt.len :: ', len);
          if (len >= 10) {
            dpStartDe   = selectContsData.dpStartDt.substring(0, 10);
          }
          if (len >= 13) {
            strDpStartHour = selectContsData.dpStartDt.substring(11, 13);
            dpStartHour    = Number(strDpStartHour);
          }
          if (len >= 16) {
            strDpStartMin = selectContsData.dpStartDt.substring (14, 16);
            dpStartMin    = Number(strDpStartMin);
          }
        }
        // 전시기간-종료정보
        if (selectContsData.dpEndDt != undefined && selectContsData.dpEndDt != null && selectContsData.dpEndDt != '') {

          var strDpEndHour = '';
          var strDpEndMin = '';
          var len = selectContsData.dpEndDt.length;
          //console.log('# dpEndDt.len :: ', len);
          if (len >= 10) {
            dpEndDe   = selectContsData.dpEndDt.substring(0, 10);
          }
          if (len >= 13) {
            strDpEndHour = selectContsData.dpEndDt.substring(11, 13);
            dpEndHour    = Number(strDpEndHour);
          }
          if (len >= 16) {
            strDpEndMin = selectContsData.dpEndDt.substring (14, 16);
            dpEndMin    = Number(strDpEndMin);
          }
        }
        // 전시범위
        dpRangeTp       = selectContsData.dpRangeTp;
        // 타이틀명
        titleNm         = selectContsData.titleNm;
        // HTML-PC(에디터)
        var htmlPc      = $('#htmlPcHtml').data("kendoEditor").value(selectContsData.htmlPc);
        // HTML-Mobile(에디터)
        var htmlMobile  = $('#htmlMobileHtml').data("kendoEditor").value(selectContsData.htmlMobile);
        // 순번
        sort            = selectContsData.sort;
      }
    }

    // ------------------------------------------------------------------------
    // * 변수값 Set
    // ------------------------------------------------------------------------
    // 전시기간
    if ($('#mode').val() != null && $('#mode').val() == 'conts.insert') {
      // 신규
      $("#dpStartDt"+postFix).data("kendoDatePicker").value(baseStartDe);
      $("#dpStartHour"+postFix).data("kendoDropDownList").select(0);
      $("#dpStartMin"+postFix).data("kendoDropDownList").select(0);
      $("#dpEndDt"+postFix).data("kendoDatePicker").value(afterSixDe);
      $("#dpEndHour"+postFix).data("kendoDropDownList").select(23);
      $("#dpEndMin"+postFix).data("kendoDropDownList").select(59);

      // 시작일자 활성
      $('#dpStartDt'+postFix).data("kendoDatePicker").enable(true);
      $('#dpStartHour'+postFix).data("kendoDropDownList").enable(true);
      $('#dpStartMin'+postFix).data("kendoDropDownList").enable(true);

      // 기간버튼 1주일로 선택
      $('#inputContsFormHtml').find('[fb-btn-active]').attr('fb-btn-active', false);        // 모두 미선택
      $('#inputContsFormHtml').find('[data-id="fnDateBtn3"]').attr('fb-btn-active', true);  // 1주일 선택

    }
    else {
      // 수정
      $("#dpStartDt"+postFix).data("kendoDatePicker").value(dpStartDe);
      $("#dpStartHour"+postFix).data("kendoDropDownList").select(dpStartHour);
      $("#dpStartMin"+postFix).data("kendoDropDownList").select(dpStartMin);
      $("#dpEndDt"+postFix).data("kendoDatePicker").value(dpEndDe);
      $("#dpEndHour"+postFix).data("kendoDropDownList").select(dpEndHour);
      $("#dpEndMin"+postFix).data("kendoDropDownList").select(dpEndMin);

      // 시작일자 활성
      $('#dpStartDt'+postFix).data("kendoDatePicker").enable(true);
      $('#dpStartHour'+postFix).data("kendoDropDownList").enable(true);
      $('#dpStartMin'+postFix).data("kendoDropDownList").enable(true);
    }
    // 전시범위
    $("#dpRangeTp"+postFix).data("kendoDropDownList").value(dpRangeTp);
    // 타이틀명
    $('#titleNm'+postFix).val(titleNm);
    // HTML-PC
    $('#htmlPc'+postFix).html(htmlPc);
    // HTML-Mobile
    $('#htmlMobile'+postFix).html(htmlMobile);
    // 순번
    $('#sort'+postFix).val(sort);
    // 구좌상세정보
    if (selectedContsLevel == 1) {
      contsDesc = selectedInventoryData.contsLevel1Desc;
    }
    else if (selectedContsLevel == 2) {
      contsDesc = selectedInventoryData.contsLevel2Desc;
    }
    else if (selectedContsLevel == 3) {
      contsDesc = selectedInventoryData.contsLevel3Desc;
    }
    $('#contsDesc'+postFix).val(contsDesc);

    // ------------------------------------------------------------------------
    // * 팝업노출
    // ------------------------------------------------------------------------
    $('#contsPopup'+postFix).show();
    // * 팝업오픈
    document.documentElement.scrollTop = 0;
    document.documentElement.scrollTop = 0;
    //$('#inputForm').formClear(true);
    fnKendoInputPoup({height:"auto" ,width:"860px", title:{ nullMsg : popupTitle } } );
    // $("#kendoPopup").css("max-height", "800px");
  }

  // ==========================================================================
  // 팝업-초기화/오픈-Banner
  // ==========================================================================
  function fnInitPopupBanner(selectContsData) {

    // ------------------------------------------------------------------------
    // * 팝업제목
    // ------------------------------------------------------------------------
    var contsTpNm = "배너";
    var modeNm =  ($('#mode').val() == 'conts.insert') ? '등록' :
                  ($('#mode').val() == 'conts.update') ? '수정' :
                  ($('#mode').val() == 'conts.delete') ? '삭제' : '';
    //var popupTitle = '콘텐츠 Lv.' + selectedContsLevel + ' ' + modeNm + ' : [' + contsTpNm + '] ' + selectedInventoryData.inventoryNm;
    var popupTitle = '콘텐츠 등록';

    // ------------------------------------------------------------------------
    // 이미지 영역 display : none 처리
    // ------------------------------------------------------------------------
    $('#imgMobileBannerView').closest(wrapper_cn).hide();
    $('#imgMobileBannerView').closest(container_cn).find(".fileUpload__title").text("").hide();
    $('#imgMobileBannerView').closest(container_cn).find(".fileUpload__message").show();
    $('#gifImgMobileBannerView').closest(wrapper_cn).hide();
    $('#gifImgMobileBannerView').closest(container_cn).find(".fileUpload__title").text("").hide();
    $('#gifImgMobileBannerView').closest(container_cn).find(".fileUpload__message").show();
    $('#imgPcBannerView').closest(wrapper_cn).hide();
    $('#imgPcBannerView').closest(container_cn).find(".fileUpload__title").text("").hide();
    $('#imgPcBannerView').closest(container_cn).find(".fileUpload__message").show();
    $('#gifImgPcBannerView').closest(wrapper_cn).hide();
    $('#gifImgPcBannerView').closest(container_cn).find(".fileUpload__title").text("").hide();
    $('#gifImgPcBannerView').closest(container_cn).find(".fileUpload__message").show();

    var postFix = 'Banner';

    // ------------------------------------------------------------------------
    // 팝업상단 인벤토리정보 Set
    // ------------------------------------------------------------------------
    if (selectedInventoryData != null && selectedInventoryData != 'null' && selectedInventoryData != '') {
      // 페이지경로
      $('#divPagePath'+postFix).text(selectedInventoryData.pageFullPath);
      // 인벤토리명(코드)
      $('#divInventoryTitle'+postFix).text(selectedInventoryData.inventoryNm + ' (' + selectedInventoryData.inventoryCd + ')');
      // 구좌타입
      $('#divContsType'+postFix).text('Lv.' + selectedContsLevel + ' [' + contsTpNm + ']');
    }

    // ------------------------------------------------------------------------
    // * 변수초기화
    // ------------------------------------------------------------------------
    // 전시기간
    var dpStartDt;
    var dpStartDe   = '';
    var dpStartHour = 0;
    var dpStartMin  = 0;
    var dpEndDt;
    var dpEndDe     = '';
    var dpEndHour   = 0;
    var dpEndMin    = 0;
    // 전시범위
    var dpRangeTp;
    // 타이틀명
    var titleNm;
    // 링크URL
    var linkUrlPc;
    // 이미지변수 : 전역변수 선언됨
    // 노출텍스트1
    var text1;
    var text1Color;
    // 노출텍스트2
    var text2;
    var text2Color;
    // 노출텍스트3
    var text3;
    var text3Color;
    // 순번
    var sort;
    // 구좌상세정보
    var contsDesc;
    // 파일업로드초기화-선택한 파일을 지워야 하므로
    //fnInitKendoUpload();
    // 업로드파일객체-초기화
    $('#imgMobileBanner').val(null);
    $('#imgMobileBannerView').attr('src', '');
    $('#gifImgMobileBanner').val(null);
    $('#gifImgMobileBannerView').attr('src', '');
    $('#imgPcBanner').val(null);
    $('#imgPcBannerView').attr('src', '');
    $('#gifImgPcBanner').val(null);
    $('#gifImgPcBannerView').attr('src', '');

    if ($('#mode').val() != null && $('#mode').val() == 'conts.update') {
      // 수정인 경우 값 Set
      if (selectContsData != null && selectContsData != '') {

        // 전시기간-시작정보
        if (selectContsData.dpStartDt != undefined && selectContsData.dpStartDt != null && selectContsData.dpStartDt != '') {

          var strDpStartHour = '';
          var strDpStartMin = '';
          var len = selectContsData.dpStartDt.length;
          //console.log('# dpStartDt.len :: ', len);
          if (len >= 10) {
            dpStartDe   = selectContsData.dpStartDt.substring(0, 10);
          }
          if (len >= 13) {
            strDpStartHour = selectContsData.dpStartDt.substring(11, 13);
            dpStartHour    = Number(strDpStartHour);
          }
          if (len >= 16) {
            strDpStartMin = selectContsData.dpStartDt.substring (14, 16);
            dpStartMin    = Number(strDpStartMin);
          }
        }
        // 전시기간-종료정보
        if (selectContsData.dpEndDt != undefined && selectContsData.dpEndDt != null && selectContsData.dpEndDt != '') {

          var strDpEndHour = '';
          var strDpEndMin = '';
          var len = selectContsData.dpEndDt.length;
          //console.log('# dpEndDt.len :: ', len);
          if (len >= 10) {
            dpEndDe   = selectContsData.dpEndDt.substring(0, 10);
          }
          if (len >= 13) {
            strDpEndHour = selectContsData.dpEndDt.substring(11, 13);
            dpEndHour    = Number(strDpEndHour);
          }
          if (len >= 16) {
            strDpEndMin = selectContsData.dpEndDt.substring (14, 16);
            dpEndMin    = Number(strDpEndMin);
          }
        }
        // 전시범위
        dpRangeTp   = selectContsData.dpRangeTp;
        // 타이틀명
        titleNm     = selectContsData.titleNm;
        // 링크URL
        linkUrlPc   = selectContsData.linkUrlPc;
        // 이미지1
        imgPathMobileBanner         = selectContsData.imgPathMobile;
        imgOriginNmMobileBanner     = selectContsData.imgOriginNmMobile;
        // 이미지gif-모바일
        gifImgPathMobileBanner      = selectContsData.gifImgPathMobile;
        gifImgOriginNmMobileBanner  = selectContsData.gifImgOriginNmMobile;
        // 이미지2
        imgPathPcBanner             = selectContsData.imgPathPc;
        imgOriginNmPcBanner         = selectContsData.imgOriginNmPc;
        // 이미지gif-PC
        gifImgPathPcBanner          = selectContsData.gifImgPathPc;
        gifImgOriginNmPcBanner      = selectContsData.gifImgOriginNmPc;
        // 노출텍스트1
        text1       = selectContsData.text1;
        text1Color  = selectContsData.text1Color;
        // 노출텍스트2
        text2       = selectContsData.text2;
        text2Color  = selectContsData.text2Color;
        // 노출텍스트3
        text3       = selectContsData.text3;
        text3Color  = selectContsData.text3Color;
        // 순번
        sort        = selectContsData.sort;
      }

    } // End of if ($('#mode').val() != null && $('#mode').val() == 'conts.update')
    else {
      // 신규인 경우

      // 업로드파일변수-초기화 (전역변수이므로)
      imgPathMobileBanner         = '';     // 이미지1-풀경로
      imgOriginNmMobileBanner     = '';     // 이미지1-원본파일명
      gifImgPathMobileBanner      = '';     // 이미지gif모바일-풀경로
      gifImgOriginNmMobileBanner  = '';     // 이미지gif모바일-원본파일명
      imgPathPcBanner             = '';     // 이미지2-풀경로
      imgOriginNmPcBanner         = '';     // 이미지2-원본파일명
      gifImgPathPcBanner          = '';     // 이미지gifPC-풀경로
      gifImgOriginNmPcBanner      = '';     // 이미지gifPC-원본파일명
    }

    // ------------------------------------------------------------------------
    // * 변수값 Set
    // ------------------------------------------------------------------------
    // 전시기간
    if ($('#mode').val() != null && $('#mode').val() == 'conts.insert') {
      // 신규
      $("#dpStartDt"+postFix).data("kendoDatePicker").value(baseStartDe);
      $("#dpStartHour"+postFix).data("kendoDropDownList").select(0);
      $("#dpStartMin"+postFix).data("kendoDropDownList").select(0);

      $("#dpEndDt"+postFix).data("kendoDatePicker").value(afterOneDayDe);
      $("#dpEndHour"+postFix).data("kendoDropDownList").select(23);
      $("#dpEndMin"+postFix).data("kendoDropDownList").select(59);

      // 시작일자 활성
      $('#dpStartDt'+postFix).data("kendoDatePicker").enable(true);
      $('#dpStartHour'+postFix).data("kendoDropDownList").enable(true);
      $('#dpStartMin'+postFix).data("kendoDropDownList").enable(true);

      // 기간버튼 1주일로 선택
      $('#inputContsFormBanner').find('[fb-btn-active]').attr('fb-btn-active', false);        // 모두 미선택
      $('#inputContsFormBanner').find('[data-id="fnDateBtn3"]').attr('fb-btn-active', true);  // 1주일 선택
    }
    else {
      // 수정
      $("#dpStartDt"+postFix).data("kendoDatePicker").value(dpStartDe);
      $("#dpStartHour"+postFix).data("kendoDropDownList").select(dpStartHour);
      $("#dpStartMin"+postFix).data("kendoDropDownList").select(dpStartMin);
      $("#dpEndDt"+postFix).data("kendoDatePicker").value(dpEndDe);
      $("#dpEndHour"+postFix).data("kendoDropDownList").select(dpEndHour);
      $("#dpEndMin"+postFix).data("kendoDropDownList").select(dpEndMin);

      // 시작일자 활성
      $('#dpStartDt'+postFix).data("kendoDatePicker").enable(true);
      $('#dpStartHour'+postFix).data("kendoDropDownList").enable(true);
      $('#dpStartMin'+postFix).data("kendoDropDownList").enable(true);
    }
    // 전시범위
    $("#dpRangeTp"+postFix).data("kendoDropDownList").value(dpRangeTp);
    // 타이틀명
    $('#titleNm'+postFix).val(titleNm);

    try {
      //올가 메인 : 빌보드 배너, 메인 중간 바 배너 링크 url 초기값 세팅(신규등록시)
      $('#tooltipDiv').hide();
      if(selectedInventoryData != null && selectedInventoryData != 'null' && selectedInventoryData != ''
          && (selectedInventoryData.inventoryCd == '21-OMain-Billboard-Bn' || selectedInventoryData.inventoryCd == '21-OMain-Promotion-Bn')
          && $('#mode').val() != null && $('#mode').val() == 'conts.insert') {
        linkUrlPc = "https://shop.pulmuone.co.kr/orga/";

        $('#tooltipDiv').show();
        var tooltip = $('#tooltipDiv').kendoTooltip({ // 도움말 toolTip
          filter    : 'span'
          , width     : 340
          , position  : 'center'
          , content   : kendo.template($('#tooltip-template').html())
          , animation : {
            open  : {
              effects : 'zoom'
              , duration : 150
            }
          }
        }).data('kendoTooltip');
      }
    } catch(e) {
      console.log(e);
    }
    // 링크URL
    $('#linkUrlPc'+postFix).val(linkUrlPc);
    // 이미지1
    if (imgPathMobileBanner != undefined && imgPathMobileBanner != null && imgPathMobileBanner != '') {
      $('#imgMobileBannerView').closest(wrapper_cn).show();
      $('#imgMobileBannerView').attr('src', publicStorageUrl + imgPathMobileBanner);
    }
    else {
      $('#imgMobileBannerView').attr('src', '');
    }
    // 이미지gif-모바일
    if (gifImgPathMobileBanner != undefined && gifImgPathMobileBanner != null && gifImgPathMobileBanner != '') {
      $('#gifImgMobileBannerView').closest(wrapper_cn).show();
      $('#gifImgMobileBannerView').attr('src', publicStorageUrl + gifImgPathMobileBanner);
    }
    else {
      $('#gifImgMobileBannerView').attr('src', '');
    }
    // 이미지2
    if (imgPathPcBanner != undefined && imgPathPcBanner != null && imgPathPcBanner != '') {
      $('#imgPcBannerView').closest(wrapper_cn).show();
      $('#imgPcBannerView').attr('src', publicStorageUrl + imgPathPcBanner);
    }
    else {
      $('#imgPcBannerView').attr('src', '');
    }
    // 이미지gif-PC
    if (gifImgPathPcBanner != undefined && gifImgPathPcBanner != null && gifImgPathPcBanner != '') {
      $('#gifImgPcBannerView').closest(wrapper_cn).show();
      $('#gifImgPcBannerView').attr('src', publicStorageUrl + gifImgPathPcBanner);
    }
    else {
      $('#gifImgPcBannerView').attr('src', '');
    }
    // 노출텍스트1
    $('#text1'+postFix).val(text1);
    $('#text1Color'+postFix).val(text1Color);
    // 노출텍스트2
    $('#text2'+postFix).val(text2);
    $('#text2Color'+postFix).val(text2Color);
    // 노출텍스트3
    $('#text3'+postFix).val(text3);
    $('#text3Color'+postFix).val(text3Color);
    // 순번
    $('#sort'+postFix).val(sort);
    // 구좌상세정보
    if (selectedContsLevel == 1) {
      contsDesc = selectedInventoryData.contsLevel1Desc;
    }
    else if (selectedContsLevel == 2) {
      contsDesc = selectedInventoryData.contsLevel2Desc;
    }
    else if (selectedContsLevel == 3) {
      contsDesc = selectedInventoryData.contsLevel3Desc;
    }
    $('#contsDesc'+postFix).val(contsDesc);

    // ------------------------------------------------------------------------
    // * 팝업노출
    // ------------------------------------------------------------------------
    $('#contsPopup'+postFix).show();
    // * 팝업오픈
    document.documentElement.scrollTop = 0;
    document.documentElement.scrollTop = 0;
    //$('#inputForm').formClear(true);
    fnKendoInputPoup({height:"auto" ,width:"860px", title:{ nullMsg : popupTitle } } );
    // $("#kendoPopup").css("max-height", "800px");
  }

  // ==========================================================================
  // 팝업-초기화/오픈-Brand
  // ==========================================================================
  function fnInitPopupBrand(selectContsData) {
    //console.log('# fnInitPopupBrand Start');
    // ------------------------------------------------------------------------
    // * 팝업제목
    // ------------------------------------------------------------------------
    var contsTpNm = "브랜드";
    var modeNm =  ($('#mode').val() == 'conts.insert') ? '등록' :
                  ($('#mode').val() == 'conts.update') ? '수정' :
                  ($('#mode').val() == 'conts.delete') ? '삭제' : '';
    //var popupTitle = '콘텐츠 Lv.' + selectedContsLevel + ' ' + modeNm + ' : [' + contsTpNm + '] ' + selectedInventoryData.inventoryNm;
    var popupTitle = '콘텐츠 등록';

    // ------------------------------------------------------------------------
    // 이미지 영역 display : none 처리
    // ------------------------------------------------------------------------
    $('#imgMobileBrandView').closest(wrapper_cn).hide();
    $('#imgMobileBrandView').closest(container_cn).find(".fileUpload__title").text("").hide();
    $('#imgMobileBrandView').closest(container_cn).find(".fileUpload__message").show();
    $('#imgPcBrandView').closest(wrapper_cn).hide();
    $('#imgPcBrandView').closest(container_cn).find(".fileUpload__title").text("").hide();
    $('#imgPcBrandView').closest(container_cn).find(".fileUpload__message").show();

    var postFix = 'Brand';

    // ------------------------------------------------------------------------
    // 팝업상단 인벤토리정보 Set
    // ------------------------------------------------------------------------
    if (selectedInventoryData != null && selectedInventoryData != 'null' && selectedInventoryData != '') {
      // 페이지경로
      $('#divPagePath'+postFix).text(selectedInventoryData.pageFullPath);
      // 인벤토리명(코드)
      $('#divInventoryTitle'+postFix).text(selectedInventoryData.inventoryNm + ' (' + selectedInventoryData.inventoryCd + ')');
      // 구좌타입
      $('#divContsType'+postFix).text('Lv.' + selectedContsLevel + ' [' + contsTpNm + ']');
    }

    // ------------------------------------------------------------------------
    // * 변수초기화
    // ------------------------------------------------------------------------
    // 전시기간
    var dpStartDt;
    var dpStartDe   = '';
    var dpStartHour = 0;
    var dpStartMin  = 0;
    var dpEndDt;
    var dpEndDe     = '';
    var dpEndHour   = 0;
    var dpEndMin    = 0;
    // 전시범위
    var dpRangeTp;
    // 타이틀명
    var titleNm;
    // 브랜드
    var dpBrandId;
    // 이미지변수 : 전역변수 선언됨
    // 노출텍스트1
    var text1;
    var text1Color;
    // 노출텍스트2
    var text2;
    var text2Color;
    // 노출텍스트3
    var text3;
    var text3Color;
    // 순번
    var sort;
    // 구좌상세정보
    var contsDesc;
    // 파일업로드초기화-선택한 파일을 지워야 하므로
    fnInitKendoUpload();
    // 업로드파일객체-초기화
    $('#imgMobileBrand').val(null);
    $('#imgMobileBrandView').attr('src', '');
    $('#imgPcBrand').val(null);
    $('#imgPcBrandView').attr('src', '');

    if ($('#mode').val() != null && $('#mode').val() == 'conts.update') {
      // 수정인 경우 값 Set
      if (selectContsData != null && selectContsData != '') {

        // 전시기간-시작정보
        if (selectContsData.dpStartDt != undefined && selectContsData.dpStartDt != null && selectContsData.dpStartDt != '') {

          var strDpStartHour = '';
          var strDpStartMin = '';
          var len = selectContsData.dpStartDt.length;
          //console.log('# dpStartDt.len :: ', len);
          if (len >= 10) {
            dpStartDe   = selectContsData.dpStartDt.substring(0, 10);
          }
          if (len >= 13) {
            strDpStartHour = selectContsData.dpStartDt.substring(11, 13);
            dpStartHour    = Number(strDpStartHour);
          }
          if (len >= 16) {
            strDpStartMin = selectContsData.dpStartDt.substring (14, 16);
            dpStartMin    = Number(strDpStartMin);
          }
        }
        // 전시기간-종료정보
        if (selectContsData.dpEndDt != undefined && selectContsData.dpEndDt != null && selectContsData.dpEndDt != '') {

          var strDpEndHour = '';
          var strDpEndMin = '';
          var len = selectContsData.dpEndDt.length;
          //console.log('# dpEndDt.len :: ', len);
          if (len >= 10) {
            dpEndDe   = selectContsData.dpEndDt.substring(0, 10);
          }
          if (len >= 13) {
            strDpEndHour = selectContsData.dpEndDt.substring(11, 13);
            dpEndHour    = Number(strDpEndHour);
          }
          if (len >= 16) {
            strDpEndMin = selectContsData.dpEndDt.substring (14, 16);
            dpEndMin    = Number(strDpEndMin);
          }
        }
        // 전시범위
        dpRangeTp   = selectContsData.dpRangeTp;
        // 타이틀명
        titleNm     = selectContsData.titleNm;
        // 링크URL
        dpBrandId   = selectContsData.contsId;
        // 노출텍스트1
        text1       = selectContsData.text1;
        text1Color  = selectContsData.text1Color;
        // 노출텍스트2
        text2       = selectContsData.text2;
        text2Color  = selectContsData.text2Color;
        // 노출텍스트3
        text3       = selectContsData.text3;
        text3Color  = selectContsData.text3Color;
        // 순번
        sort        = selectContsData.sort;
        // 이미지1
        imgPathMobileBrand         = selectContsData.imgPathMobile;
        imgOriginNmMobileBrand     = selectContsData.imgOriginNmMobile;
        // 이미지2
        imgPathPcBrand             = selectContsData.imgPathPc;
        imgOriginNmPcBrand         = selectContsData.imgOriginNmPc;
      }
    }
    else {
      // 신규인 경우

      // 전시브랜드
      $("#comboBrandId"+postFix).data("kendoDropDownList").select(0);

      // 업로드파일변수-초기화 (전역변수이므로)
      imgPathMobileBrand      = '';    // 이미지1-풀경로
      imgOriginNmMobileBrand  = '';    // 이미지1-원본파일명
      imgPathPcBrand          = '';    // 이미지2-풀경로
      imgOriginNmPcBrand      = '';    // 이미지2-원본파일명
    }

    // ------------------------------------------------------------------------
    // * 변수값 Set
    // ------------------------------------------------------------------------
    // 전시기간
    if ($('#mode').val() != null && $('#mode').val() == 'conts.insert') {
      // 신규
      $("#dpStartDt"+postFix).data("kendoDatePicker").value(baseStartDe);
      $("#dpStartHour"+postFix).data("kendoDropDownList").select(0);
      $("#dpStartMin"+postFix).data("kendoDropDownList").select(0);
      //$("#dpEndDt"+postFix).data("kendoDatePicker").value(baseEndDe);   // 브랜드인 경우 종료일자 기본값 2999-12-31 (fnKendoDatePicker (dpEndDtBrand) 에서 기본값 2999-12-31 로 set 함
      $("#dpEndHour"+postFix).data("kendoDropDownList").select(23);
      $("#dpEndMin"+postFix).data("kendoDropDownList").select(59);

      // 시작일자 활성
      $('#dpStartDt'+postFix).data("kendoDatePicker").enable(true);
      $('#dpStartHour'+postFix).data("kendoDropDownList").enable(true);
      $('#dpStartMin'+postFix).data("kendoDropDownList").enable(true);
      // 기간버튼 모두 미선택
      $('#inputContsFormBrand').find('[fb-btn-active]').attr('fb-btn-active', false);        // 모두 미선택
      // 기간버튼 1주일로 선택
      //$('#inputContsFormBrand').find('[data-id="fnDateBtn3"]').attr('fb-btn-active', true);  // 1주일 선택
    }
    else {
      // 수정
      $("#dpStartDt"+postFix).data("kendoDatePicker").value(dpStartDe);
      $("#dpStartHour"+postFix).data("kendoDropDownList").select(dpStartHour);
      $("#dpStartMin"+postFix).data("kendoDropDownList").select(dpStartMin);
      $("#dpEndDt"+postFix).data("kendoDatePicker").value(dpEndDe);
      $("#dpEndHour"+postFix).data("kendoDropDownList").select(dpEndHour);
      $("#dpEndMin"+postFix).data("kendoDropDownList").select(dpEndMin);

      // 시작일자 활성
      $('#dpStartDt'+postFix).data("kendoDatePicker").enable(true);
      $('#dpStartHour'+postFix).data("kendoDropDownList").enable(true);
      $('#dpStartMin'+postFix).data("kendoDropDownList").enable(true);
    }
    // 전시범위
    $("#dpRangeTp"+postFix).data("kendoDropDownList").value(dpRangeTp);
    // 타이틀명
    $('#titleNm'+postFix).val(titleNm);
    // 브랜드목록조회
    $("#comboBrandId"+postFix).data("kendoDropDownList").value(dpBrandId);

    // 이미지1
    if (imgPathMobileBrand != undefined && imgPathMobileBrand != null && imgPathMobileBrand != '') {
      $('#imgMobileBrandView').closest(wrapper_cn).show();
      $('#imgMobileBrandView').attr('src', publicStorageUrl + imgPathMobileBrand);
    }
    else {
      $('#imgMobileBrandView').attr('src', '');
    }
    // 이미지2
    if (imgPathPcBrand != undefined && imgPathPcBrand != null && imgPathPcBrand != '') {
      $('#imgPcBrandView').closest(wrapper_cn).show();
      $('#imgPcBrandView').attr('src', publicStorageUrl + imgPathPcBrand);
    }
    else {
      $('#imgPcBrandView').attr('src', '');
    }
    // 노출텍스트1
    $('#text1'+postFix).val(text1);
    $('#text1Color'+postFix).val(text1Color);
    // 노출텍스트2
    $('#text2'+postFix).val(text2);
    $('#text2Color'+postFix).val(text2Color);
    // 노출텍스트3
    $('#text3'+postFix).val(text3);
    $('#text3Color'+postFix).val(text3Color);
    // 순번
    $('#sort'+postFix).val(sort);
    // 구좌상세정보
    if (selectedContsLevel == 1) {
      contsDesc = selectedInventoryData.contsLevel1Desc;
    }
    else if (selectedContsLevel == 2) {
      contsDesc = selectedInventoryData.contsLevel2Desc;
    }
    else if (selectedContsLevel == 3) {
      contsDesc = selectedInventoryData.contsLevel3Desc;
    }
    $('#contsDesc'+postFix).val(contsDesc);

    // ------------------------------------------------------------------------
    // * 팝업노출
    // ------------------------------------------------------------------------
    $('#contsPopup'+postFix).show();
    // * 팝업오픈
    document.documentElement.scrollTop = 0;
    document.documentElement.scrollTop = 0;
    //$('#inputForm').formClear(true);
    fnKendoInputPoup({height:"auto" ,width:"1100px", title:{ nullMsg : popupTitle } } );
    // $("#kendoPopup").css("max-height", "800px");
  }

  // ==========================================================================
  // 팝업-초기화/오픈-Category
  // ==========================================================================
  function fnInitPopupCategory(selectContsData) {

    // ------------------------------------------------------------------------
    // * 팝업제목
    // ------------------------------------------------------------------------
    var contsTpNm = "카테고리";
    var modeNm =  ($('#mode').val() == 'conts.insert') ? '등록' :
                  ($('#mode').val() == 'conts.update') ? '수정' :
                  ($('#mode').val() == 'conts.delete') ? '삭제' : '';
    //var popupTitle = '콘텐츠 Lv.' + selectedContsLevel + ' ' + modeNm + ' : [' + contsTpNm + '] ' + selectedInventoryData.inventoryNm;
    //if ($('#mode').val() != 'conts.insert') {
    //  if (selectContsData != null) {
    //    popupTitle += ' > ' + selectContsData.titleNm;
    //  }
    //}
    var popupTitle = '콘텐츠 등록';
    var postFix = 'Category';

    // ------------------------------------------------------------------------
    // 팝업상단 인벤토리정보 Set
    // ------------------------------------------------------------------------
    if (selectedInventoryData != null && selectedInventoryData != 'null' && selectedInventoryData != '') {
      // 페이지경로
      $('#divPagePath'+postFix).text(selectedInventoryData.pageFullPath);
      // 인벤토리명(코드)
      $('#divInventoryTitle'+postFix).text(selectedInventoryData.inventoryNm + ' (' + selectedInventoryData.inventoryCd + ')');
      // 구좌타입
      $('#divContsType'+postFix).text('Lv.' + selectedContsLevel + ' [' + contsTpNm + ']');
    }

    // ------------------------------------------------------------------------
    // * 변수초기화
    // ------------------------------------------------------------------------
    // 전시기간
    var dpStartDt;
    var dpStartDe   = '';
    var dpStartHour = 0;
    var dpStartMin  = 0;
    var dpEndDt;
    var dpEndDe     = '';
    var dpEndHour   = 0;
    var dpEndMin    = 0;
    // 전시범위
    var dpRangeTp;
    // 타이틀명
    var titleNm;
    // 전시카테고리유형
    var mallDivCategory = 'MALL_DIV.PULMUONE';
    // 카테고리
    var ctgryDepth;
    var categoryDepth0;
    var categoryDepth1;
    var categoryDepth2;
    var categoryDepth3;
    var categoryDepth4;
    var categoryDepth5;
    // 노출텍스트1
    var text1;
    var text1Color;
    // 노출텍스트2
    var text2;
    var text2Color;
    // 노출텍스트3
    var text3;
    var text3Color;
    // 순번
    var sort;
    // 구좌상세정보
    var contsDesc;

    if ($('#mode').val() != null && $('#mode').val() == 'conts.update') {
      // 수정인 경우 값 Set
      if (selectContsData != null && selectContsData != '') {

        // 전시기간-시작정보
        if (selectContsData.dpStartDt != undefined && selectContsData.dpStartDt != null && selectContsData.dpStartDt != '') {

          var strDpStartHour = '';
          var strDpStartMin = '';
          var len = selectContsData.dpStartDt.length;
          //console.log('# dpStartDt.len :: ', len);
          if (len >= 10) {
            dpStartDe   = selectContsData.dpStartDt.substring(0, 10);
          }
          if (len >= 13) {
            strDpStartHour = selectContsData.dpStartDt.substring(11, 13);
            dpStartHour    = Number(strDpStartHour);
          }
          if (len >= 16) {
            strDpStartMin = selectContsData.dpStartDt.substring (14, 16);
            dpStartMin    = Number(strDpStartMin);
          }
        }
        // 전시기간-종료정보
        if (selectContsData.dpEndDt != undefined && selectContsData.dpEndDt != null && selectContsData.dpEndDt != '') {

          var strDpEndHour = '';
          var strDpEndMin = '';
          var len = selectContsData.dpEndDt.length;
          //console.log('# dpEndDt.len :: ', len);
          if (len >= 10) {
            dpEndDe   = selectContsData.dpEndDt.substring(0, 10);
          }
          if (len >= 13) {
            strDpEndHour = selectContsData.dpEndDt.substring(11, 13);
            dpEndHour    = Number(strDpEndHour);
          }
          if (len >= 16) {
            strDpEndMin = selectContsData.dpEndDt.substring (14, 16);
            dpEndMin    = Number(strDpEndMin);
          }
        }
        // 전시범위
        dpRangeTp   = selectContsData.dpRangeTp;
        // 타이틀명
        titleNm     = selectContsData.titleNm;
        // mallDivCategory
        mallDivCategory     = selectContsData.mallDiv;
        // 카테고리0~5
        ctgryDepth;
        categoryDepth0  = selectContsData.ctgryIdDepth0;
        categoryDepth1  = selectContsData.ctgryIdDepth1;
        categoryDepth2  = selectContsData.ctgryIdDepth2;
        categoryDepth3  = selectContsData.ctgryIdDepth3;
        categoryDepth4  = selectContsData.ctgryIdDepth4;
        categoryDepth5  = selectContsData.ctgryIdDepth5;
        // 노출텍스트1
        text1       = selectContsData.text1;
        text1Color  = selectContsData.text1Color;
        // 노출텍스트2
        text2       = selectContsData.text2;
        text2Color  = selectContsData.text2Color;
        // 노출텍스트3
        text3       = selectContsData.text3;
        text3Color  = selectContsData.text3Color;
        // 순번
        sort        = selectContsData.sort;
      }
    }

    // ------------------------------------------------------------------------
    // * 변수값 Set
    // ------------------------------------------------------------------------
    // 전시기간
    if ($('#mode').val() != null && $('#mode').val() == 'conts.insert') {
      // 신규
      $("#dpStartDt"+postFix).data("kendoDatePicker").value(baseStartDe);
      $("#dpStartHour"+postFix).data("kendoDropDownList").select(0);
      $("#dpStartMin"+postFix).data("kendoDropDownList").select(0);
      $("#dpEndDt"+postFix).data("kendoDatePicker").value(afterSixDe);
      $("#dpEndHour"+postFix).data("kendoDropDownList").select(23);
      $("#dpEndMin"+postFix).data("kendoDropDownList").select(59);

      // 시작일자 활성
      $('#dpStartDt'+postFix).data("kendoDatePicker").enable(true);
      $('#dpStartHour'+postFix).data("kendoDropDownList").enable(true);
      $('#dpStartMin'+postFix).data("kendoDropDownList").enable(true);

      // 기간버튼 1주일로 선택
      $('#inputContsFormCategory').find('[fb-btn-active]').attr('fb-btn-active', false);        // 모두 미선택
      $('#inputContsFormCategory').find('[data-id="fnDateBtn3"]').attr('fb-btn-active', true);  // 1주일 선택

      // 전시카테고리 초기화
      fnInitComboCategory(mallDivCategory);
    }
    else {
      // 수정
      $("#dpStartDt"+postFix).data("kendoDatePicker").value(dpStartDe);
      $("#dpStartHour"+postFix).data("kendoDropDownList").select(dpStartHour);
      $("#dpStartMin"+postFix).data("kendoDropDownList").select(dpStartMin);
      $("#dpEndDt"+postFix).data("kendoDatePicker").value(dpEndDe);
      $("#dpEndHour"+postFix).data("kendoDropDownList").select(dpEndHour);
      $("#dpEndMin"+postFix).data("kendoDropDownList").select(dpEndMin);

      // 시작일자 활성
      $('#dpStartDt'+postFix).data("kendoDatePicker").enable(true);
      $('#dpStartHour'+postFix).data("kendoDropDownList").enable(true);
      $('#dpStartMin'+postFix).data("kendoDropDownList").enable(true);
    }
    // 전시범위
    $("#dpRangeTp"+postFix).data("kendoDropDownList").value(dpRangeTp);
    // 타이틀명
    $('#titleNm'+postFix).val(titleNm);
    // 팝업-카테고리-몰구분
    if ($("#comboMallDivisionCategory").data("kendoDropDownList")) {
      $("#comboMallDivisionCategory").data("kendoDropDownList").destroy();
      $("#comboMallDivisionCategory").empty();
    }
    fnKendoDropDownList({
        id          : 'comboMallDivisionCategory'
      , url         : "/admin/comn/getCodeList"
      , params      : {"stCommonCodeMasterCode" : "MALL_DIV", "useYn" :"Y"}
      , tagId       : 'comboMallDivisionCategory'
      , autoBind    : true
      , valueField  : 'CODE'
      , textField   : 'NAME'
      , chkVal      : mallDivCategory
      , style       : {}
      , blank       : '몰구분'
    });
    $("#comboMallDivision"+postFix).data("kendoDropDownList").value(mallDivCategory);
    // 전시카테고리1~4
    //fnInitComboCategory(mallDivCategory);
    $("#categoryDepth0").data("kendoDropDownList").value(categoryDepth0);
    $("#categoryDepth1").data("kendoDropDownList").value(categoryDepth1);
    $("#categoryDepth2").data("kendoDropDownList").value(categoryDepth2);
    $("#categoryDepth3").data("kendoDropDownList").value(categoryDepth3);
    $("#categoryDepth4").data("kendoDropDownList").value(categoryDepth4);
    // 노출텍스트1
    $('#text1'+postFix).val(text1);
    $('#text1Color'+postFix).val(text1Color);
    // 노출텍스트2
    $('#text2'+postFix).val(text2);
    $('#text2Color'+postFix).val(text2Color);
    // 노출텍스트3
    $('#text3'+postFix).val(text3);
    $('#text3Color'+postFix).val(text3Color);
    // 순번
    $('#sort'+postFix).val(sort);
    // 구좌상세정보
    if (selectedContsLevel == 1) {
      contsDesc = selectedInventoryData.contsLevel1Desc;
    }
    else if (selectedContsLevel == 2) {
      contsDesc = selectedInventoryData.contsLevel2Desc;
    }
    else if (selectedContsLevel == 3) {
      contsDesc = selectedInventoryData.contsLevel3Desc;
    }
    $('#contsDesc'+postFix).val(contsDesc);

    // ------------------------------------------------------------------------
    // * 팝업노출
    // ------------------------------------------------------------------------
    $('#contsPopup'+postFix).show();
    // * 팝업오픈
    document.documentElement.scrollTop = 0;
    document.documentElement.scrollTop = 0;
    //$('#inputForm').formClear(true);
    fnKendoInputPoup({height:"auto" ,width:"1260px", title:{ nullMsg : popupTitle } } );
    // $("#kendoPopup").css("max-height", "800px");
  }

  // ==========================================================================
  // 팝업-초기화/오픈-Goods
  // ==========================================================================
  function fnInitPopupGoods(selectContsData) {

    // ------------------------------------------------------------------------
    // * 팝업제목
    // ------------------------------------------------------------------------
    var contsTpNm = "상품";
    var modeNm =  ($('#mode').val() == 'conts.insert') ? '등록' :
                  ($('#mode').val() == 'conts.update') ? '수정' :
                  ($('#mode').val() == 'conts.delete') ? '삭제' : '';
    //var popupTitle = '콘텐츠 Lv.' + selectedContsLevel + ' ' + modeNm + ' : [' + contsTpNm + '] ' + selectedInventoryData.inventoryNm;
    //if ($('#mode').val() != 'conts.insert') {
    //  if (selectContsData != null) {
    //    popupTitle += ' > ' + selectContsData.titleNm;
    //  }
    //}
    var popupTitle = '콘텐츠 등록';
    var postFix = 'Goods';

    // ------------------------------------------------------------------------
    // 팝업상단 인벤토리정보 Set
    // ------------------------------------------------------------------------
    if (selectedInventoryData != null && selectedInventoryData != 'null' && selectedInventoryData != '') {
      // 페이지경로
      $('#divPagePath'+postFix).text(selectedInventoryData.pageFullPath);
      // 인벤토리명(코드)
      $('#divInventoryTitle'+postFix).text(selectedInventoryData.inventoryNm + ' (' + selectedInventoryData.inventoryCd + ')');
      // 구좌타입
      $('#divContsType'+postFix).text('Lv.' + selectedContsLevel + ' [' + contsTpNm + ']');
    }

    // ------------------------------------------------------------------------
    // * 변수초기화
    // ------------------------------------------------------------------------
    // 전시기간
    var dpStartDt;
    var dpStartDe   = '';
    var dpStartHour = 0;
    var dpStartMin  = 0;
    var dpEndDt;
    var dpEndDe     = '';
    var dpEndHour   = 0;
    var dpEndMin    = 0;
    // 전시범위
    var dpRangeTp;
    // 타이틀명
    var titleNm;
    //// 전시카테고리유형
    //var mallDivGoods;
    //// 카테고리
    //var ctgryDepthGoods;
    //var categoryDepth0Goods;
    //var categoryDepth1Goods;
    //var categoryDepth2Goods;
    //var categoryDepth3Goods;
    //var categoryDepth4Goods;
    //var categoryDepth5Goods;
    //// 링크URL
    //var linkUrlPc;
    // 상품코드
    var contsId;
    // 상품명
    var contsNm;
    // 노출텍스트1
    var text1;
    var text1Color;
    // 노출텍스트2
    var text2;
    var text2Color;
    // 노출텍스트3
    var text3;
    var text3Color;
    // 순번
    var sort;
    // 구좌상세정보
    var contsDesc;

    // 상품그리드 초기화
    fnInitContsGoodsGrid();

    if ($('#mode').val() != null && $('#mode').val() == 'conts.update') {
      // 수정인 경우 값 Set
      if (selectContsData != null && selectContsData != '') {

        // 전시기간-시작정보
        if (selectContsData.dpStartDt != undefined && selectContsData.dpStartDt != null && selectContsData.dpStartDt != '') {

          var strDpStartHour = '';
          var strDpStartMin = '';
          var len = selectContsData.dpStartDt.length;
          //console.log('# dpStartDt.len :: ', len);
          if (len >= 10) {
            dpStartDe   = selectContsData.dpStartDt.substring(0, 10);
          }
          if (len >= 13) {
            strDpStartHour = selectContsData.dpStartDt.substring(11, 13);
            dpStartHour    = Number(strDpStartHour);
          }
          if (len >= 16) {
            strDpStartMin = selectContsData.dpStartDt.substring (14, 16);
            dpStartMin    = Number(strDpStartMin);
          }
        }
        // 전시기간-종료정보
        if (selectContsData.dpEndDt != undefined && selectContsData.dpEndDt != null && selectContsData.dpEndDt != '') {

          var strDpEndHour = '';
          var strDpEndMin = '';
          var len = selectContsData.dpEndDt.length;
          //console.log('# dpEndDt.len :: ', len);
          if (len >= 10) {
            dpEndDe   = selectContsData.dpEndDt.substring(0, 10);
          }
          if (len >= 13) {
            strDpEndHour = selectContsData.dpEndDt.substring(11, 13);
            dpEndHour    = Number(strDpEndHour);
          }
          if (len >= 16) {
            strDpEndMin = selectContsData.dpEndDt.substring (14, 16);
            dpEndMin    = Number(strDpEndMin);
          }
        }
        // 전시범위
        dpRangeTp   = selectContsData.dpRangeTp;
        // 타이틀명
        titleNm     = selectContsData.titleNm;
        // 상품코드
        contsId = selectContsData.contsId;
        // 상품명
        contsNm = selectContsData.contsNm;
        // 노출텍스트1
        text1       = selectContsData.text1;
        text1Color  = selectContsData.text1Color;
        // 노출텍스트2
        text2       = selectContsData.text2;
        text2Color  = selectContsData.text2Color;
        // 노출텍스트3
        text3       = selectContsData.text3;
        text3Color  = selectContsData.text3Color;
        // 순번
        sort        = selectContsData.sort;
      }
    }

    // ------------------------------------------------------------------------
    // * 변수값 Set
    // ------------------------------------------------------------------------
    // 전시기간
    if ($('#mode').val() != null && $('#mode').val() == 'conts.insert') {
      // 신규
      $("#dpStartDt"+postFix).data("kendoDatePicker").value(baseStartDe);
      $("#dpStartHour"+postFix).data("kendoDropDownList").select(0);
      $("#dpStartMin"+postFix).data("kendoDropDownList").select(0);
      $("#dpEndDt"+postFix).data("kendoDatePicker").value(afterOneDayDe);
      $("#dpEndHour"+postFix).data("kendoDropDownList").select(23);
      $("#dpEndMin"+postFix).data("kendoDropDownList").select(59);

      // 시작일자 활성
      $('#dpStartDt'+postFix).data("kendoDatePicker").enable(true);
      $('#dpStartHour'+postFix).data("kendoDropDownList").enable(true);
      $('#dpStartMin'+postFix).data("kendoDropDownList").enable(true);

      // 기간버튼 1주일로 선택
      $('#inputContsFormGoods').find('[fb-btn-active]').attr('fb-btn-active', false);        // 모두 미선택
      $('#inputContsFormGoods').find('[data-id="fnDateBtn3"]').attr('fb-btn-active', true);  // 1주일 선택
    }
    else {
      // 수정
      $("#dpStartDt"+postFix).data("kendoDatePicker").value(dpStartDe);
      $("#dpStartHour"+postFix).data("kendoDropDownList").select(dpStartHour);
      $("#dpStartMin"+postFix).data("kendoDropDownList").select(dpStartMin);
      $("#dpEndDt"+postFix).data("kendoDatePicker").value(dpEndDe);
      $("#dpEndHour"+postFix).data("kendoDropDownList").select(dpEndHour);
      $("#dpEndMin"+postFix).data("kendoDropDownList").select(dpEndMin);

      // 시작일자 활성
      $('#dpStartDt'+postFix).data("kendoDatePicker").enable(true);
      $('#dpStartHour'+postFix).data("kendoDropDownList").enable(true);
      $('#dpStartMin'+postFix).data("kendoDropDownList").enable(true);
    }
    // 전시범위
    $("#dpRangeTp"+postFix).data("kendoDropDownList").value(dpRangeTp);
    // 타이틀명
    $('#titleNm'+postFix).val(titleNm);
    // 순번
    $('#sort'+postFix).val(sort);
    // 구좌상세정보
    if (selectedContsLevel == 1) {
      contsDesc = selectedInventoryData.contsLevel1Desc;
    }
    else if (selectedContsLevel == 2) {
      contsDesc = selectedInventoryData.contsLevel2Desc;
    }
    else if (selectedContsLevel == 3) {
      contsDesc = selectedInventoryData.contsLevel3Desc;
    }
    $('#contsDesc'+postFix).val(contsDesc);
    // 상품코드
    $('#contsId'+postFix).text(contsId);
    // 상품명
    $('#contsNm'+postFix).text(contsNm);
    // 노출텍스트1
    $('#text1Color'+postFix).val(text1Color);
    $('#text1'+postFix).val(text1);
    // 노출텍스트2
    $('#text2Color'+postFix).val(text2Color);
    $('#text2'+postFix).val(text2);
    // 노출텍스트3
    $('#text3Color'+postFix).val(text3Color);
    $('#text3'+postFix).val(text3);

    // * 조회조건 : 수정 시 변수값 Set 필요없이 초기화만 하면 됨
    // 단일/복수조건
    $('input:radio[name="goodsSearchCond"]:input[value="'+'A'+'"]').prop("checked", true);
    // 코드유형
    $("#searchCondition").data("kendoDropDownList").select(0);
    // 검색키워드
    $('#findKeywordListString').val('');
    //// 복수조건항목
    //// 브랜드목록조회
    //$("#comboBrandIdGoods").data("kendoDropDownList").value("");
    //// 몰구분 (팝업 열릴때 마다 몰구분 전체로 선택되어야 함)
    //$("#comboMallDivisionGoods").data("kendoDropDownList").value('');
    ////  전시카테고리1~4 (팝업 열릴때 마다 몰구분 전체 이므로 카테고리 조회 안함)
    //fnInitComboCategoryGoods('X');
    //$("#categoryDepth1Goods").data("kendoDropDownList").value(categoryDepth1Goods);
    //$("#categoryDepth2Goods").data("kendoDropDownList").value(categoryDepth2Goods);
    //$("#categoryDepth3Goods").data("kendoDropDownList").value(categoryDepth3Goods);
    //$("#categoryDepth4Goods").data("kendoDropDownList").value(categoryDepth4Goods);
    //// 상품유형-전체 선택
    //$("input[name=goodsType]:checkbox").prop("checked", true);
    //// 판매상태-전체 선택
    //$("input[name=saleStatus]:checkbox").prop("checked", true);
    //// 판매유형-전체 선택
    //$("input[name=saleType]:checkbox").prop("checked", true);
    //// 전시여부
    //$("input:radio[name='displayYn']:radio[value='']").attr("checked",true);
    //// 구매허용범위
    //$("input:radio[name='purchaseTargetAllYn']:radio[value='Y']").attr("checked",true);
    //// 구매허용범위-조건검색-일반회원
    //$('INPUT[name=purchaseMemberYn]').prop("checked", true);
    //// 구매허용범위-조건검색-임직원회원
    //$('INPUT[name=purchaseEmployeeYn]').prop("checked", true);
    //// 구매허용범위-조건검색-비회원
    //$('INPUT[name=purchaseNonmemberYn]').prop("checked", true);
    //// 구매허용범위-모든항목 숨김
    //$('#purchaseTargetAllSpan').hide();
    //// 판매허용범위
    //$("input:radio[name='salesAllowanceAllYn']:radio[value='Y']").attr("checked",true);
    //// 판매허용범위-조건검색-PC Web
    //$('INPUT[name=displayWebPcYn]').prop("checked", true);
    //// 판매허용범위-조건검색-M Web
    //$('INPUT[name=displayWebMobileYn]').prop("checked", true);
    //// 판매허용범위-조건검색-App
    //$('INPUT[name=displayAppYn]').prop("checked", true);
    //// 판매허용범위-모든항목 숨김
    //$('#salesAllowanceAllSpan').hide();

    // ------------------------------------------------------------------------
    // * 팝업노출
    // ------------------------------------------------------------------------
    $('#contsPopup'+postFix).show();
    // 등록/수정에 따른 노출 설정
    if ($('#mode').val() == 'conts.insert') {
      $('#contsPopupGoodsSortTr').hide();
      $('#contsPopupGoodsInsertSection').show();
      $('#contsPopupGoodsUpdateSection').hide();
    }
    else if ($('#mode').val() == 'conts.update') {
      $('#contsPopupGoodsSortTr').show();
      $('#contsPopupGoodsInsertSection').hide();
      $('#contsPopupGoodsUpdateSection').show();
    }

    // * 팝업오픈
    document.documentElement.scrollTop = 0;
    document.documentElement.scrollTop = 0;
    //$('#inputForm').formClear(true);
    fnKendoInputPoup({height:"auto" ,width:"1260px", title:{ nullMsg : popupTitle } } );
    // $("#kendoPopup").css("max-height", "800px");
  }

  // ==========================================================================
  // 팝업-초기화/오픈-Goods-검색조건초기화
  // ==========================================================================
  //function fnInitPopupGoodsSearchCond() {
  //
  //  var goodsSearchCond = $("input[name=goodsSearchCond]:checked").val();
  //  console.log('# goodsSearchCond :: ', goodsSearchCond);
  //
  //  if (goodsSearchCond == 'A') {
  //    // 단일조건 검색
  //
  //    // 코드유형 초기화 ''
  //
  //    // 검색어 초기화
  //
  //
  //  }
  //  else if (goodsSearchCond == 'B') {
  //    // 복수조건 검색
  //
  //    // 이것저것 모두다
  //  }
  //
  //}

  // ==========================================================================
  // 팝업-초기화/오픈-이미지미리보기
  // ==========================================================================
  function fnInitPopupImagePreview(info, fileNm, previewTp) {

    // ------------------------------------------------------------------------
    // * 입력값 체크
    // ------------------------------------------------------------------------
    if (info == undefined || info == null || info == 'null' || info == '') {

      fnMessage('', '미리보기 정보가 없습니다.', '');
      return false;
    }

    var heightStr;
    var widthStr;

    // ------------------------------------------------------------------------
    // * 팝업제목
    // ------------------------------------------------------------------------
    var popupTitle = '';

    if (previewTp == 'IMAGE') {
      popupTitle = "이미지 미리보기 : " + fileNm;
    }
    else if (previewTp == 'HTML') {
      popupTitle = "HTML 미리보기"
    }
    else {

      fnMessage('', '미리보기 유형을 확인하세요.', '');
      return false;
    }

    // ------------------------------------------------------------------------
    // * 팝업노출
    // ------------------------------------------------------------------------
    $('#contsPopupText').hide();
    $('#contsPopupHtml').hide();
    $('#contsPopupBanner').hide();
    $('#contsPopupBrand').hide();
    $('#contsPopupCategory').hide();
    $('#contsPopupGoods').hide();
    $('#previewPopup').show();
    $('#inventoryGroupPopup').hide();

    if (previewTp == 'IMAGE') {
      // 이미지 노출
      heightStr = 'auto';
      widthStr  = 'auto';
      $('#previewImgDiv').show();
      $('#previewHtmlDiv').hide();
console.log('#>>> publicStorageUrl :: ', publicStorageUrl);
console.log('#>>> info             :: ', info);
      $('#imgView').attr('src', publicStorageUrl + info);
    }
    else if (previewTp == 'HTML') {
      // HTML 노출
      heightStr = '800px';
      widthStr  = '700px';
      $('#previewImgDiv').hide();
      $('#previewHtmlDiv').show();
      $('#previewHtmlDiv').html(info);
    }

    // * 팝업오픈
    document.documentElement.scrollTop = 0;
    document.documentElement.scrollTop = 0;
    //$('#inputForm').formClear(true);
    fnKendoInputPoup({height:heightStr ,width:widthStr, title:{ nullMsg : popupTitle } } );
    if (previewTp == 'IMAGE') {
      // $("#kendoPopup").css("max-height", "700px");
    }
    else {
      // $("#kendoPopup").css("max-height", "");
    }

  }

  // ==========================================================================
  // 팝업-초기화/오픈-인벤토리그룹관리
  // ==========================================================================
  function fnInitPopupInventoryGroup() {

    // ------------------------------------------------------------------------
    // * 입력값 체크
    // ------------------------------------------------------------------------

    // ------------------------------------------------------------------------
    // * 인벤토리그룹 상세영역 초기화
    // ------------------------------------------------------------------------
    fnInitInventoryGroupDetail();

    // ------------------------------------------------------------------------
    // * 팝업제목
    // ------------------------------------------------------------------------
    var popupTitle = '인벤토리 그룹 관리';

    // ------------------------------------------------------------------------
    // * 팝업노출
    // ------------------------------------------------------------------------
    $('#contsPopupText').hide();
    $('#contsPopupHtml').hide();
    $('#contsPopupBanner').hide();
    $('#contsPopupBrand').hide();
    $('#contsPopupCategory').hide();
    $('#contsPopupGoods').hide();
    $('#previewPopup').hide();
    $('#inventoryGroupPopup').show();

    // 인벤토리그룹 그리드 초기화
    fnInitInventoryGroupGrid();

    // 인벤토리그룹 그리드 조회
    let data = $("#inputInventoryGroupForm").formSerialize(true);
    inventoryGroupGridDs.read(data);

    // * 팝업오픈
    // mode : 팝업 오픈 시 초기값 인벤토리그룹.insert
    //$('#mode').val('inventoryGroup.insert');
    document.documentElement.scrollTop = 0;
    document.documentElement.scrollTop = 0;
    //$('#inputForm').formClear(true);
    fnKendoInputPoup({height:"auto" ,width:"1000px", title:{ nullMsg : popupTitle } } );
  }


  // ==========================================================================
  // # 버튼-팝업-닫기
  // ==========================================================================
  function fnPopupClose(){

    var kendoWindow =$('#kendoPopup').data('kendoWindow');
    kendoWindow.close();
  }
  // **************************************************************************
  // ------------------------------- POPUP END --------------------------------
  // **************************************************************************


  // ==========================================================================
  // # 버튼-콘텐츠-수정
  // ==========================================================================
  function fnBtnContsEdit(contsLevel, selectContsData) {
    //console.log('##### fnBtnContsEdit Start');
    //console.log('# fnBtnContsEdit contTp :: ', selectContsData.contsTp);
    //  mode
    $('#mode').val("conts.update");
    selectedContsLevel = contsLevel;
    // 팝업오픈 호출
    fnPopupOpen(selectContsData.contsTp, selectContsData);
  }


  // ==========================================================================
  // # 버튼-콘텐츠순번저장
  // ==========================================================================
  // 콘텐츠 Lv1
  function fnBtnSaveSortContsLv1() {

    selectedContsLevel = 1;
    fnSaveSortConts();
  }
  // 콘텐츠 Lv2
  function fnBtnSaveSortContsLv2() {

    selectedContsLevel = 2;
    fnSaveSortConts();
  }
  // 콘텐츠 Lv3
  function fnBtnSaveSortContsLv3() {

    selectedContsLevel = 3;
    fnSaveSortConts();
  }

  // ==========================================================================
  // # 버튼-콘텐츠순번저장처리
  // ==========================================================================
  function fnSaveSortConts() {
    //console.log('# fnSaveSortConts Start :: ', selectedContsLevel);

    var contsGridArr;

    if (selectedContsLevel == 1) {
      // 콘텐츠Lv1
      contsGridArr = $("#contsLv1Grid").data("kendoGrid").dataSource.data();
    }
    else if (selectedContsLevel == 2) {
      // 콘텐츠Lv2
      contsGridArr = $("#contsLv2Grid").data("kendoGrid").dataSource.data();
    }
    else if (selectedContsLevel == 3) {
      // 콘텐츠Lv3
      contsGridArr = $("#contsLv3Grid").data("kendoGrid").dataSource.data();
    }
    else {
      fnMessage('', '콘텐츠 정보를 확인하세요.', '');
      return false;
    }

    //console.log('# contsGridArr.length :: ', contsGridArr.length);
    //console.log('# contsGridArr        :: ', JSON.stringify(contsGridArr));

    var sortArr = new Array() ;

    for (var i = 0; i < contsGridArr.length; i++) {

      //console.log('# contsGridArr[', i, '].sort :: ', contsGridArr[i].sort);
      var sortData = new Object();
      sortData.dpContsId  = contsGridArr[i].dpContsId;
      sortData.sort       = Number(contsGridArr[i].sort);

      sortArr.push(sortData);
    }

    var sortJsonData = JSON.stringify(sortArr);
    //console.log('# sortJsonData :: ' + sortJsonData);

    fnKendoMessage({message:'콘텐츠 Lv.' + selectedContsLevel + ' 순서를 저장하시겠습니까?', type : "confirm" , ok : function(){
      var url = '/admin/display/manage/putContsSort';
      var cbId = 'conts.sort';
      var inParam = {"contsListJsonString"  : sortJsonData}
      //var data = $('#inputForm').formSerialize(true);

      fnAjax({
          url     : url
        , params  : inParam
        , success : function( result ){
                      fnBizCallback(cbId, result);
                    }
        , isAction : 'update'
      });

    }});
  }


  // ==========================================================================
  // # 버튼-콘텐츠-다건삭제
  // ==========================================================================
  function fnBtnContsLv1Del() {
    selectedContsLevel = 1;
    fnContsDelMulti();
  }
  function fnBtnContsLv2Del() {
    selectedContsLevel = 2;
    fnContsDelMulti();
  }
  function fnBtnContsLv3Del() {
    selectedContsLevel = 3;
    fnContsDelMulti();
  }

  // ==========================================================================
  // # 처리-콘텐츠-다건삭제
  // ==========================================================================
  function fnContsDelMulti() {
    //console.log('# fnContsDelMulti Start :: ', selectedContsLevel);

    //var contsGridArr;
    var selectRows;
    var contsGrid;

    if (selectedContsLevel == 1) {
      // 콘텐츠Lv1
      //contsGridArr = $("#contsLv1Grid").data("kendoGrid").dataSource.data();
      selectRows = $("#contsLv1Grid").find('input[name=contsLv1Check]:checked').closest('tr');
      contsGrid = contsLv1Grid;
    }
    else if (selectedContsLevel == 2) {
      // 콘텐츠Lv2
      //contsGridArr = $("#contsLv2Grid").data("kendoGrid").dataSource.data();
      selectRows = $("#contsLv2Grid").find('input[name=contsLv2Check]:checked').closest('tr');
      contsGrid = contsLv2Grid;
    }
    else if (selectedContsLevel == 3) {
      // 콘텐츠Lv3
      //contsGridArr = $("#contsLv3Grid").data("kendoGrid").dataSource.data();
      selectRows = $("#contsLv3Grid").find('input[name=contsLv3Check]:checked').closest('tr');
      contsGrid = contsLv3Grid;
    }
    else {
      fnMessage('', '콘텐츠 정보를 확인하세요.', '');
      return false;
    }

    //console.log('# selectRows :: ', selectRows.length);

    var delArr = new Array() ;

    for (var i = 0; i < selectRows.length; i++) {
      var selectedRowData = contsGrid.dataItem($(selectRows[i]));
      //console.log('# selectRows[', i, '] :: ', JSON.stringify(contsGrid.dataItem($(selectRows[i]))));
      var delData = new Object();
      delData.dpContsId = selectedRowData.dpContsId;
      delData.delYn     = 'Y';
      delArr.push(delData);
    }

    // 삭제대상Arr
    selectedContsDelArr = delArr;
    // mode
    $('#mode').val('conts.delete');
    // 저장호출
    fnSaveConts(null);

    //var delJsonData = JSON.stringify(delArr);
    //console.log('# delJsonData :: ' + delJsonData);
  }

  // ==========================================================================
  // # 처리-콘텐츠-단건삭제
  // ==========================================================================
  function fnContsDelSingle(contsLevel) {
    //console.log('# fnContsDelSingle Start');

    selectedContsLevel = contsLevel;

    if (selectedContsLevel == 1) {
      selectedContsData = selectedContsLv1Data;
    }
    else if (selectedContsLevel == 2) {
      selectedContsData = selectedContsLv2Data;
    }
    else if (selectedContsLevel == 3) {
      selectedContsData = selectedContsLv3Data;
    }
    // contsLevel Set (Callback에서 사용)
    selectedContsData.contsLevel = selectedContsLevel;

    // 리스트구조 생성
    var delArr = new Array() ;
    var delData = new Object();
    delData.dpContsId  = selectedContsData.dpContsId;
    delData.delYn      = 'Y';
    delArr.push(delData);

    // 삭제대상Arr
    selectedContsDelArr = delArr;
    // mode
    $('#mode').val('conts.delete');
    // 저장호출
    fnSaveConts(null);
    //var delJsonData = JSON.stringify(delArr);
    //console.log('# delJsonData :: ' + delJsonData);
  }

  // ==========================================================================
  // # 버튼-인벤토리그룹 순번저장
  // ==========================================================================
  function fnBtnSaveSortInventoryGroup() {

    fnSaveSortInventoryGroup();
  }

  // ==========================================================================
  // # 버튼-인벤토리그룹 순번저장 처리
  // ==========================================================================
  function fnSaveSortInventoryGroup() {
    //console.log('# fnSaveSortInventoryGroup Start');

    var inventoryGroupGridArr  = $("#inventoryGroupGrid").data("kendoGrid").dataSource.data();
    //console.log('# inventoryGroupGridArr.length :: ', inventoryGroupGridArr.length);
    //console.log('# inventoryGroupGridArr        :: ', JSON.stringify(inventoryGroupGridArr));

    var sortArr = new Array() ;

    for (var i = 0; i < inventoryGroupGridArr.length; i++) {

      //console.log('# inventoryGroupGridArr[', i, '].sort :: ', inventoryGroupGridArr[i].sort);
      var sortData = new Object();
      sortData.dpInventoryGrpId = inventoryGroupGridArr[i].dpPageId;
      sortData.sort       = Number(inventoryGroupGridArr[i].sort);
      sortArr.push(sortData);
    }

    var sortJsonData = JSON.stringify(sortArr);
    //console.log('# sortJsonData :: ' + sortJsonData);

    fnKendoMessage({message:'인벤토리그룹의 순서를 저장하시겠습니까?', type : "confirm" , ok : function(){
      var url = '/admin/display/manage/putInventoryGroupSort';
      var cbId = 'inventoryGroup.sort';
      var inParam = {"inventoryListJsonString"  : sortJsonData}
      //var data = $('#inputForm').formSerialize(true);

      fnAjax({
          url     : url
        , params  : inParam
        , success : function( result ){
                      fnBizCallback(cbId, result);
                    }
        , isAction : 'update'
      });

    }});
  }

  // ==========================================================================
  // # 버튼-인벤토리그룹-다건삭제
  // ==========================================================================
  function fnBtnInventoryGroupDel() {
    //console.log('# fnBtnInventoryGroupDel Start');
    fnInventoryGroupDelMulti();
  }

  // ==========================================================================
  // # 처리-인벤토리그룹-다건삭제
  // ==========================================================================
  function fnInventoryGroupDelMulti() {
    //console.log('# fnInventoryGroupDelMulti Start');

    //var contsGridArr;
    var selectRows;

    selectRows = $("#inventoryGroupGrid").find('input[name=inventoryGroupCheck]:checked').closest('tr');

    if (selectRows == undefined || selectRows == 'undefined' || selectRows == null || selectRows == 'null' || selectRows == '' || selectRows.length <= 0) {
      fnMessage('', '인벤토리 그룹을 선택해주세요.', '');
      return false;
    }

    var delArr = new Array() ;

    for (var i = 0; i < selectRows.length; i++) {
      var selectedRowData = inventoryGroupGrid.dataItem($(selectRows[i]));
      //console.log('# selectRows[', i, '] :: ', JSON.stringify(inventoryGroupGrid.dataItem($(selectRows[i]))));
      var delData = new Object();
      delData.dpInventoryGrpId = selectedRowData.dpPageId;
      delData.delYn     = 'Y';
      delArr.push(delData);
    }

    // 삭제대상Arr
    selectedInventoryGroupDelArr = delArr;
    // mode
    $('#mode').val('inventoryGroup.delete');
    // 저장호출
    fnSaveInventoryGroup(null);

  }

  // ==========================================================================
  // # 처리-인벤토리그룹-단건삭제
  // ==========================================================================
  function fnInventoryGroupDelSingle(dataItem) {
    //console.log('# fnInventoryGroupDelSingle Start');

    // 선택그룹정보 노출 Set
    fnSetInventoryGroupDetail(dataItem);

    // 리스트구조 생성
    var delArr = new Array() ;
    var delData = new Object();
    delData.dpInventoryGrpId = selectedInventoryGroupData.dpPageId;
    delData.delYn     = 'Y';
    delArr.push(delData);

    // 삭제대상Arr
    selectedInventoryGroupDelArr = delArr;
    // mode
    $('#mode').val('inventoryGroup.delete');
    // 저장호출
    fnSaveInventoryGroup(null);
  }



  // ==========================================================================
  // # 상품콘텐츠팝업-상품목록조회
  // ==========================================================================
  function fnSearchGoods() {
    //console.log('# fnSearchGoods Start');

    // 조회 시 필수체크 해제
    $('#inputContsFormGoods input[name=dpStartDtGoods]'  ).prop("required", false);
    $('#inputContsFormGoods input[name=dpStartHourGoods]').prop("required", false);
    $('#inputContsFormGoods input[name=dpStartMinGoods]' ).prop("required", false);
    $('#inputContsFormGoods input[name=dpEndDtGoods]'    ).prop("required", false);
    $('#inputContsFormGoods input[name=dpEndHourGoods]'  ).prop("required", false);
    $('#inputContsFormGoods input[name=dpEndMinGoods]'   ).prop("required", false);
    $('#inputContsFormGoods input[name=dpRangeTpGoods]'  ).prop("required", false);
    $('#inputContsFormGoods input[name=titleNmGoods]'    ).prop("required", false);
    $('#inputContsFormGoods input[name=linkUrlPcGoods]'  ).prop("required", false);
    $('#inputContsFormGoods input[name=sortGoods]'       ).prop("required", false);

    var inCodeTp    = $('#searchCondition').val();
    var findKeywordListString = $('#findKeywordListString').val();
    //console.log('# 코드유형 :: ', inCodeTp);
    //console.log('# 키워드   :: ', findKeywordListString);


    if (inCodeTp == undefined || inCodeTp == null || inCodeTp == 'null' || inCodeTp == '') {

      fnMessage('', '코드유형을 선택해주세요.', '');
      $('#searchCondition').fucus();
      return false;
    }
    if (findKeywordListString == undefined || findKeywordListString == null || findKeywordListString == 'null' || findKeywordListString == '') {

      fnMessage('', '키워드를 입력해주세요.', '');
      $('#findKeywordListString').fucus();
      return false;
    }

    // 그리드 초기화
    fnInitContsGoodsGrid();

    // 그리드 조회 실행
    let data = $("#inputContsFormGoods").formSerialize(true);
    goodsGridDs.read(data);
  }

  // ==========================================================================
  // # 상품콘텐츠팝업-클리어
  // ==========================================================================
  function fnClearGoods() {
    //console.log('# fnClearGoods Start');

    // 팝업-상품-전시기간
    $("#dpStartDtGoods").data("kendoDatePicker").value(baseStartDe);
    $("#dpEndDtGoods").data("kendoDatePicker").value(baseEndDe);
    $("#dpStartHourGoods").data("kendoDropDownList").select(0);
    $("#dpStartMinGoods").data("kendoDropDownList").select(0);
    $("#dpEndHourGoods").data("kendoDropDownList").select(23);
    $("#dpEndMinGoods").data("kendoDropDownList").select(59);
    // 타이틀명
    $('#titleNmGoods').val('');
    // 전시범위
    $("#dpRangeTpGoods").data("kendoDropDownList").select(0);
    // 코드유형
    $("#searchCondition").data("kendoDropDownList").select(0);
    // 검색키워드
    $('#findKeywordListString').val('');
    // 상품그리드 초기화
    fnInitContsGoodsGrid();
  }

  // ##########################################################################
  // 콘텐츠 저장
  // ##########################################################################
  // --------------------------------------------------------------------------
  // # 버튼-콘텐츠팝업-Text-저장
  // --------------------------------------------------------------------------
  function fnBtnSaveText() {
    fnSaveConts(null);
  }
  // --------------------------------------------------------------------------
  // # 버튼-콘텐츠팝업-Html-저장
  // --------------------------------------------------------------------------
  function fnBtnSaveHtml() {
    fnSaveConts(null);
  }
  // --------------------------------------------------------------------------
  // # 버튼-콘텐츠팝업-Banner-저장
  // --------------------------------------------------------------------------
  function fnBtnSaveBanner() {
    fnSaveConts(null);
  }
  // --------------------------------------------------------------------------
  // # 버튼-콘텐츠팝업-Brand-저장
  // --------------------------------------------------------------------------
  function fnBtnSaveBrand() {
    fnSaveConts(null);
  }
  // --------------------------------------------------------------------------
  // # 버튼-콘텐츠팝업-Category-저장
  // --------------------------------------------------------------------------
  function fnBtnSaveCategory() {
    fnSaveConts(null);
  }
  // --------------------------------------------------------------------------
  // # 버튼-콘텐츠팝업-상품-등록
  // --------------------------------------------------------------------------
  function fnBtnSaveGoods() {
    //console.log('# fnBtnSaveGoods Start :: ', $('#mode').val());

    if ($('#mode').val() == 'conts.insert') {
      // ----------------------------------------------------------------------
      // 상품-등록
      // ----------------------------------------------------------------------
      var selectRows = $("#goodsGrid").find('input[name=goodsCheck]:checked').closest('tr');
      //console.log('# selectRows :: ', selectRows);

      if (selectRows != undefined && selectRows != null && selectRows != 'null' && selectRows != '') {
        if (selectRows.length <= 0) {
          fnMessage('', '상품 목록에서 상품을 선택해주세요.', '');
          return false;
        }
      }


      var contsGridArr = $("#goodsGrid").data("kendoGrid").dataSource.data();

      var regGoodsArr = new Array() ;

      for (var i = 0; i < selectRows.length; i++) {

        var selectedRowData = goodsGrid.dataItem($(selectRows[i]));
        //console.log($(selectRows[i]).find("input"));

        // ----------------------------------------------------------------------
        // 켄도그리드 틀고정일 경우 속성찾기
        // ----------------------------------------------------------------------
        var idx = $(selectRows[i]).index();

        var inputs = $("#goodsGrid .k-grid-content table").find("tr").eq(idx).find("input");
        //console.log('# selectRows['+i+'] :: ', JSON.stringify(selectRows[i]));
        //console.log('# selectedRowData['+i+'] :: ', JSON.stringify(selectedRowData));
        //console.log('# selectedRowData['+i+'] :: ', selectedRowData);
        //var _index = $("#goodsGrid").find(".k-grid-content-locked table").index($(selectRows[i]));
        //console.log(_index);
        //goodsGrid.select().find('input[name=text1]').val();
        //console.log('# selectRows[', i, '] :: ', JSON.stringify(contsGrid.dataItem($(selectRows[i]))));
        var goodsContsData = new Object();
        // 상품정보
        goodsContsData.dpContsId  = selectedRowData.ilGoodsId;
        goodsContsData.goodsNm    = selectedRowData.goodsNm;
        // 정렬정보
        goodsContsData.sort       = selectedRowData.sort;
        // text속성 6개 goodsContsData set
        // text1, text1Color, text2, text2Color, text3, text3Color
        inputs.each(function(){
          var key = $(this).attr("name");
          var value = $(this).val();

          goodsContsData[key] = value;

        });
        //console.log('# goodsContsData['+i+'] :: ', JSON.stringify(goodsContsData));
        regGoodsArr.push(goodsContsData);

      } // End of for (var i = 0; i < selectRows.length; i++)

    }
    else if ($('#mode').val() == 'conts.update') {
      // ----------------------------------------------------------------------
      // 상품-수정
      // ----------------------------------------------------------------------
    }
    else {
      var msg = '<div>' + '등록 처리 중 오류가 발생했습니다.' + '</div>'
              + '<div>' + '다시 시도해 주십시오.' + '</div>';
      fnMessage('', msg, '');
      return false;
    }

    // 저장 호출
    fnSaveConts(regGoodsArr);

  }

  // ==========================================================================
  // # 저장처리-Conts
  // ==========================================================================
  function fnSaveConts(addDataObjArray) {
    //console.log('# fnSaveConts Start [', $('#mode').val(), ']');

    var url;
    var cbId;
    var isAction;
    var confirmMsg;
    var mode = $('#mode').val();
    var dataObj;
    var data;
    var contsData;
    var contsListJsonString = '';

    if (mode != null && mode == 'conts.insert') {
      // ----------------------------------------------------------------------
      // 콘텐츠 등록
      // ----------------------------------------------------------------------
      // 확인메시지
      confirmMsg = '저장하시겠습니까?';
      // 파람Set
      dataObj = fnSetParamValue(addDataObjArray);
      data = dataObj.data;

      if (selectedContsTp == 'DP_CONTENTS_TP.GOODS') {
        // 상품인 경우 contsListJsonString 파람 Set
        contsListJsonString = JSON.stringify(dataObj.contsData);
        //console.log('#1 contsListJsonString :: ', contsListJsonString);
      }
      else {
        // 상품 이외인 경우 contsInfoJsonString 파람 Set
        contsData = dataObj.contsData;
      }
      // 접속
      url  = '/admin/display/manage/addConts';
      cbId = mode;
      isAction = 'insert';

    }
    else if (mode != null && mode == 'conts.update') {

      // ----------------------------------------------------------------------
      // 콘텐츠 수정
      // ----------------------------------------------------------------------
      // 확인메시지
      confirmMsg = '수정하시겠습니까?';
      // 파람Set
      dataObj = fnSetParamValue(addDataObjArray);
      data = dataObj.data;
      contsData = dataObj.contsData;
      // 접속
      url  = '/admin/display/manage/putConts';
      cbId = mode;
      isAction = 'update';
    }
    else if (mode != null && mode == 'conts.delete') {
      // ----------------------------------------------------------------------
      // 콘텐츠 삭제
      // ----------------------------------------------------------------------
      // 확인메시지
      confirmMsg = '하위 콘텐츠도 모두 삭제됩니다.\n 삭제하시겠습니까?';
      // Param Set
      var data = $('#inputForm').formSerialize(true);
      //contsData = new Object();
      //contsData.dpContsId   = Number(selectedContsData.dpContsId);  // 콘텐츠ID
      //{"contsListJsonString"  : selectedContsDelArr}
      contsListJsonString = JSON.stringify(selectedContsDelArr);

      // 접속
      url  = '/admin/display/manage/delConts';
      cbId = mode;
      isAction = 'update';
    }
    else {
      fnMessage('', '인벤토리 기능 오류입니다.', '');
      return false;
    }

    // ------------------------------------------------------------------------
    // Validation & Ajax Call
    // ------------------------------------------------------------------------
    if(data.rtnValid) {

        try {
          //올가 메인 : 빌보드 배너, 메인 중간 바 배너 링크 url 재확인
          if(selectedInventoryData != null && selectedInventoryData != 'null' && selectedInventoryData != ''
              && (selectedInventoryData.inventoryCd == '21-OMain-Billboard-Bn' || selectedInventoryData.inventoryCd == '21-OMain-Promotion-Bn')
              && mode != null && (mode == 'conts.insert' || mode == 'conts.update')
              && selectedContsTp == 'DP_CONTENTS_TP.BANNER' && contsData != undefined && contsData.linkUrlPc != undefined && contsData.linkUrlPc.indexOf('/orga/') < 0 ) {
            if(!window.confirm("링크 URL이 올가 몰인몰 경로가 아닙니다.(링크 URL 도움말 참조) \n이대로 등록 하시겠습니까? ")) {  return false; }
          }
        } catch(e) {
            console.log(e);
        }

      // Ajax Call
      fnKendoMessage({message:fnGetLangData({key :"",nullMsg : confirmMsg }), type : "confirm", ok :
        function(){
          //console.log('# selectedContsData :: ' + JSON.stringify(selectedContsData));
          //console.log('# fnAjax contsData :: ' + JSON.stringify(contsData));
          // 저장 처리 후 선택 콘텐츠레벨 초기화
          //selectedContsLevel = null;

          fnAjax({
              url     : url
            , params  : {"contsInfoJsonString" : JSON.stringify(contsData), "contsListJsonString"  : contsListJsonString, "contsTp" : selectedContsTp}
            //, params  : pageData
            , success : function(result){
                          //console.log('# insert result :: ' + JSON.stringify(result));
                          fnBizCallback(cbId, result);
                        }
            , isAction : isAction
          });
        }
      });
    } // End of if(data.rtnValid)

  } // End of function fnSaveConts(addDataObjArray)


  // ##########################################################################
  // 인벤토리그룹 저장
  // ##########################################################################

  // --------------------------------------------------------------------------
  // # 버튼-인벤토리그룹팝업--저장
  // --------------------------------------------------------------------------
  function fnBtnSaveInventoryGroup() {
    //console.log('# fnBtnSaveInventoryGroup Start');
    //console.log('# mode :: ', $('#mode').val());
    var mode = $('#mode').val();

    if (mode != 'inventoryGroup.insert' && mode != 'inventoryGroup.update') {

      fnMessage('', '신규등록 또는 수정 버튼을 선택해주세요.', '');
      return false;
    }

    fnSaveInventoryGroup();
  }

  // ==========================================================================
  // # 저장처리-InventoryGroup
  // ==========================================================================
  function fnSaveInventoryGroup() {
    //console.log('# fnSaveInventoryGroup Start [', $('#mode').val(), ']');

    var url;
    var cbId;
    var isAction;
    var confirmMsg;
    var mode = $('#mode').val();
    var data;
    var inventoryListJsonString;

    // ------------------------------------------------------------------------
    // 필수체크
    // ------------------------------------------------------------------------



    if (mode != null && mode == 'inventoryGroup.insert') {
      // ----------------------------------------------------------------------
      // 인벤토리그룹 등록
      // ----------------------------------------------------------------------
      // 필수체크
      $('#inputInventoryGroupForm input[name=inventoryGrpNm]'   ).prop("required", true);
      $('#inputInventoryGroupForm [name=inventoryCdsString]'    ).prop("required", true);
      $('#inputInventoryGroupForm input[name=inventoryGrpUseYn]').prop("required", true);
      $('#inputInventoryGroupForm input[name=sortInventoryGrp]' ).prop("required", true);
      data = $('#inputInventoryGroupForm').formSerialize(true);

      var inventoryData = new Object();
      inventoryData.inventoryGrpNm      = data.inventoryGrpNm;
      inventoryData.inventoryCdsString  = data.inventoryCdsString;
      inventoryData.useYn               = $("input[name=inventoryGrpUseYn]:checked").val();
      inventoryData.groupDesc           = data.groupDesc;
      inventoryData.sort                = data.sortInventoryGrp;
      //console.log('# 등록 :: ', JSON.stringify(inventoryData));

      // 확인메시지
      confirmMsg = '저장하시겠습니까?';
      // 접속
      url  = '/admin/display/manage/addInventoryGroup';
      cbId = mode;
      isAction = 'insert';

    }
    else if (mode != null && mode == 'inventoryGroup.update') {

      // ----------------------------------------------------------------------
      // 인벤토리그룹 수정
      // ----------------------------------------------------------------------
      // 필수체크
      $('#inputInventoryGroupForm input[name=inventoryGrpNm]'   ).prop("required", true);
      $('#inputInventoryGroupForm [name=inventoryCdsString]'    ).prop("required", true);
      $('#inputInventoryGroupForm input[name=inventoryGrpUseYn]').prop("required", true);
      $('#inputInventoryGroupForm input[name=sortInventoryGrp]' ).prop("required", true);
      data = $('#inputInventoryGroupForm').formSerialize(true);

      var inventoryData = new Object();
      inventoryData.dpInventoryGrpId    = selectedInventoryGroupData.dpPageId;
      inventoryData.inventoryGrpNm      = data.inventoryGrpNm;
      inventoryData.inventoryCdsString  = data.inventoryCdsString;
      inventoryData.useYn               = $("input[name=inventoryGrpUseYn]:checked").val();
      inventoryData.groupDesc           = data.groupDesc;
      inventoryData.sort                = data.sortInventoryGrp;
      //console.log('# 수정 :: ', JSON.stringify(inventoryData));

      // 확인메시지
      confirmMsg = '수정하시겠습니까?';
      // 파람Set
      // 접속
      url  = '/admin/display/manage/putInventoryGroup';
      cbId = mode;
      isAction = 'update';
    }
    else if (mode != null && mode == 'inventoryGroup.delete') {
      // ----------------------------------------------------------------------
      // 인벤토리그룹 삭제
      // ----------------------------------------------------------------------
      // 확인메시지
      confirmMsg = '삭제하시겠습니까?';
      data = $('#inputInventoryGroupForm').formSerialize(true);
      //var data = $('#inputForm').formSerialize(true);
      //contsData = new Object();
      //contsData.dpContsId   = Number(selectedContsData.dpContsId);  // 콘텐츠ID
      //{"contsListJsonString"  : selectedContsDelArr}
      inventoryListJsonString = JSON.stringify(selectedInventoryGroupDelArr);


      // 접속
      url  = '/admin/display/manage/delInventoryGroup';
      cbId = mode;
      isAction = 'update';
    }
    else {
      fnMessage('', '인벤토리그룹 기능 오류입니다.', '');
      return false;
    }

    // ------------------------------------------------------------------------
    // Validation & Ajax Call
    // ------------------------------------------------------------------------
    if(data.rtnValid) {

      // Ajax Call
      fnKendoMessage({message:fnGetLangData({key :"",nullMsg : confirmMsg }), type : "confirm", ok :
        function(){
          // 저장 처리 후 선택 콘텐츠레벨 초기화
          fnAjax({
              url     : url
            , params  : {"inventoryInfoJsonString" : JSON.stringify(inventoryData), "inventoryListJsonString"  : inventoryListJsonString}
            //, params  : pageData
            , success : function(result){
                          fnBizCallback(cbId, result);
                        }
            , isAction : isAction
          });
        }
      });
    } // End of if(data.rtnValid)

  }


  // --------------------------------------------------------------------------
  // # 콘텐츠별 저장 파람 Set
  // --------------------------------------------------------------------------
  function fnSetParamValue(addDataObjArray) {

    var resultDataObj = new Object();   // 반환 obj
    var resultDataObjArr = new Array()  // 반환 array

    var postFix;
    var data;
    var contsData = new Object();
    var paramDpStartDt   = '';
    var paramDpStartDe   = '';
    var paramDpStartHour = '';
    var paramDpStartMin  = '';
    var paramDpEndDt     = '';
    var paramDpEndDe     = '';
    var paramDpEndHour   = '';
    var paramDpEndMin    = '';
    var paramPrntsContsId;
    var paramLevel1ContsId;
    var paramLevel2ContsId;
    var paramLevel3ContsId;
    var paramIlCtgryId;
    var paramDpCtgryId;

    // 모드
    var mode = $('#mode').val();

    // 선택정보 Set - 상위콘텐츠ID/선택한콘텐츠정보
    if (selectedContsLevel == 1) {
      paramPrntsContsId  = 0;
      selectedContsData  = selectedContsLv1Data;
    }
    else if (selectedContsLevel == 2) {
      paramPrntsContsId  = selectedContsLv1Data.dpContsId;
      paramLevel1ContsId = selectedContsLv1Data.dpContsId;
      selectedContsData  = selectedContsLv2Data;
    }
    else if (selectedContsLevel == 3) {
      paramPrntsContsId  = selectedContsLv2Data.dpContsId;

      paramLevel1ContsId = selectedContsLv1Data.dpContsId;
      paramLevel2ContsId = selectedContsLv2Data.dpContsId;
      selectedContsData  = selectedContsLv3Data;
    }
    // 콘텐츠ID (수정인 경우만 Set)
    //console.log('#### selectedContsData :: ', JSON.stringify(selectedContsData));
    if (mode == 'conts.update') {
      contsData.dpContsId = selectedContsData.dpContsId;
    }
    // 카테고리ID
    if ($('#comboPageTp').val() == 'PAGE_TP.CATEGORY') {
      paramIlCtgryId = selectedPageData.dpPageId;
    }

    // 콘텐츠유형별 처리
    if (selectedContsTp == 'DP_CONTENTS_TP.TEXT') {
      // ----------------------------------------------------------------------
      // * TEXT
      // ----------------------------------------------------------------------
      postFix = 'Text';

      // ----------------------------------------------------------------------
      // 필수체크-콘텐츠유형별
      // ----------------------------------------------------------------------
      $('#inputContsForm'+postFix+' input[name=dpStartDt'+postFix+']'  ).prop("required", true);
      $('#inputContsForm'+postFix+' input[name=dpStartHour'+postFix+']').prop("required", true);
      $('#inputContsForm'+postFix+' input[name=dpStartMin'+postFix+']' ).prop("required", true);
      $('#inputContsForm'+postFix+' input[name=dpEndDt'+postFix+']'    ).prop("required", true);
      $('#inputContsForm'+postFix+' input[name=dpEndHour'+postFix+']'  ).prop("required", true);
      $('#inputContsForm'+postFix+' input[name=dpEndMin'+postFix+']'   ).prop("required", true);
      $('#inputContsForm'+postFix+' input[name=dpRangeTp'+postFix+']'  ).prop("required", true);
      $('#inputContsForm'+postFix+' input[name=titleNm'+postFix+']'    ).prop("required", true);
      //$('#inputContsForm'+postFix+' input[name=linkUrlPc'+postFix+']'  ).prop("required", true);  // 필수 제외 (2021.03.009)
      $('#inputContsForm'+postFix+' input[name=sort'+postFix+']'       ).prop("required", true);

      // ----------------------------------------------------------------------
      // 공통항목
      // ----------------------------------------------------------------------
      // 전시기간-시작
      paramDpStartDe   = $('#dpStartDt'+postFix).val();
      paramDpStartHour = $('#dpStartHour'+postFix).val();
      paramDpStartMin  = $('#dpStartMin'+postFix).val();
      if (paramDpStartDe != null) {
        paramDpStartDe = paramDpStartDe.replace(/-/gi, '');
      }
      //console.log('### paramDpStartDe :: ', paramDpStartDe);
      paramDpStartDt = paramDpStartDt.concat(paramDpStartDe, paramDpStartHour, paramDpStartMin, '00');
      //console.log('### paramDpStartDt :: ', paramDpStartDt);

      // 전시기간-종료
      paramDpEndDe   = $('#dpEndDt'+postFix).val();
      paramDpEndHour = $('#dpEndHour'+postFix).val();
      paramDpEndMin  = $('#dpEndMin'+postFix).val();
      if (paramDpEndDe != null) {
        paramDpEndDe = paramDpEndDe.replace(/-/gi, '');
      }
      paramDpEndDt = paramDpEndDt.concat(paramDpEndDe, paramDpEndHour, paramDpEndMin, '59');

      // 상위콘텐츠ID
      if (selectedContsLevel == 1) {
        paramPrntsContsId = 0;
      }
      else if (selectedContsLevel == 2) {
        paramPrntsContsId  = selectedContsLv1Data.dpContsId;
        paramLevel1ContsId = selectedContsLv1Data.dpContsId;
      }
      else if (selectedContsLevel == 3) {
        paramPrntsContsId = selectedContsLv2Data.dpContsId;

        paramLevel1ContsId = selectedContsLv1Data.dpContsId;
        paramLevel2ContsId = selectedContsLv2Data.dpContsId;
      }

      // ----------------------------------------------------------------------
      // 데이터 Set
      // ----------------------------------------------------------------------
      data = $('#inputContsForm'+postFix).formSerialize(true);
      // 필수항목
      contsData.dpInventoryId         = selectedInventoryData.dpInventoryId;
      contsData.ilCtgryId             = paramIlCtgryId;
      contsData.contsTp               = selectedContsTp;
      contsData.contsLevel            = selectedContsLevel;
      contsData.prntsContsId          = paramPrntsContsId;
      contsData.level1ContsId         = paramLevel1ContsId;
      contsData.level2ContsId         = paramLevel2ContsId;
      contsData.level3ContsId         = paramLevel3ContsId;
      contsData.dpStartDt             = paramDpStartDt;
      contsData.dpEndDt               = paramDpEndDt;
      contsData.dpRangeTp             = $('#dpRangeTp'+postFix).val();
      contsData.titleNm               = $('#titleNm'+postFix).val();
      contsData.linkUrlPc             = $('#linkUrlPc'+postFix).val();
      contsData.linkUrlMobile         = $('#linkUrlPc'+postFix).val();
      contsData.sort                  = $('#sort'+postFix).val();
      contsData.useYn                 = 'Y';
      contsData.delYn                 = 'N';
      // 비필수항목
      contsData.text1                 = $('#text1'+postFix).val();
      contsData.text1Color            = $('#text1Color'+postFix).val();
      contsData.text2                 = $('#text2'+postFix).val();
      contsData.text2Color            = $('#text2Color'+postFix).val();
      contsData.text3                 = $('#text3'+postFix).val();
      contsData.text3Color            = $('#text3Color'+postFix).val();

      // 반환
      resultDataObj.data = data;
      resultDataObj.contsData = contsData;
    }
    else if (selectedContsTp == 'DP_CONTENTS_TP.HTML') {
      // ----------------------------------------------------------------------
      // * HTML
      // ----------------------------------------------------------------------
      postFix = 'Html';

      // ----------------------------------------------------------------------
      // 필수체크-콘텐츠유형별
      // ----------------------------------------------------------------------
      $('#inputContsForm'+postFix+' input[name=dpStartDt'+postFix+']'  ).prop("required", true);
      $('#inputContsForm'+postFix+' input[name=dpStartHour'+postFix+']').prop("required", true);
      $('#inputContsForm'+postFix+' input[name=dpStartMin'+postFix+']' ).prop("required", true);
      $('#inputContsForm'+postFix+' input[name=dpEndDt'+postFix+']'    ).prop("required", true);
      $('#inputContsForm'+postFix+' input[name=dpEndHour'+postFix+']'  ).prop("required", true);
      $('#inputContsForm'+postFix+' input[name=dpEndMin'+postFix+']'   ).prop("required", true);
      $('#inputContsForm'+postFix+' input[name=dpRangeTp'+postFix+']'  ).prop("required", true);
      $('#inputContsForm'+postFix+' input[name=titleNm'+postFix+']'    ).prop("required", true);
      $('#inputContsForm'+postFix+' input[name=sort'+postFix+']'       ).prop("required", true);

      // ----------------------------------------------------------------------
      // 공통항목
      // ----------------------------------------------------------------------
      // 전시기간-시작
      paramDpStartDe   = $('#dpStartDt'+postFix).val();
      paramDpStartHour = $('#dpStartHour'+postFix).val();
      paramDpStartMin  = $('#dpStartMin'+postFix).val();
      if (paramDpStartDe != null) {
        paramDpStartDe = paramDpStartDe.replace(/-/gi, '');
      }
      paramDpStartDt = paramDpStartDt.concat(paramDpStartDe, paramDpStartHour, paramDpStartMin, '00');
      // 전시기간-종료
      paramDpEndDe   = $('#dpEndDt'+postFix).val();
      paramDpEndHour = $('#dpEndHour'+postFix).val();
      paramDpEndMin  = $('#dpEndMin'+postFix).val();
      if (paramDpEndDe != null) {
        paramDpEndDe = paramDpEndDe.replace(/-/gi, '');
      }
      paramDpEndDt = paramDpEndDt.concat(paramDpEndDe, paramDpEndHour, paramDpEndMin, '59');

      // 상위콘텐츠ID
      if (selectedContsLevel == 1) {
        paramPrntsContsId = 0;
      }
      else if (selectedContsLevel == 2) {
        paramPrntsContsId  = selectedContsLv1Data.dpContsId;
        paramLevel1ContsId = selectedContsLv1Data.dpContsId;
      }
      else if (selectedContsLevel == 3) {
        paramPrntsContsId = selectedContsLv2Data.dpContsId;

        paramLevel1ContsId = selectedContsLv1Data.dpContsId;
        paramLevel2ContsId = selectedContsLv2Data.dpContsId;
      }

      // ----------------------------------------------------------------------
      // 데이터 Set
      // ----------------------------------------------------------------------
      data = $('#inputContsForm'+postFix).formSerialize(true);
      // 필수항목
      contsData.dpInventoryId         = selectedInventoryData.dpInventoryId;
      contsData.ilCtgryId             = paramIlCtgryId;
      contsData.contsTp               = selectedContsTp;
      contsData.contsLevel            = selectedContsLevel;
      contsData.prntsContsId          = paramPrntsContsId;
      contsData.level1ContsId         = paramLevel1ContsId;
      contsData.level2ContsId         = paramLevel2ContsId;
      contsData.level3ContsId         = paramLevel3ContsId;
      contsData.dpStartDt             = paramDpStartDt;
      contsData.dpEndDt               = paramDpEndDt;
      contsData.dpRangeTp             = $('#dpRangeTp'+postFix).val();
      contsData.titleNm               = $('#titleNm'+postFix).val();
      contsData.sort                  = $('#sort'+postFix).val();
      contsData.useYn                 = 'Y';
      contsData.delYn                 = 'N';
      // 비필수항목
      contsData.htmlPc                = $('#htmlPc'+postFix).data("kendoEditor").value();
      contsData.htmlMobile            = $('#htmlMobile'+postFix).data("kendoEditor").value();

      // 반환
      resultDataObj.data = data;
      resultDataObj.contsData = contsData;
    }
    else if (selectedContsTp == 'DP_CONTENTS_TP.BANNER') {
      // ----------------------------------------------------------------------
      // * BANNER
      // ----------------------------------------------------------------------
      postFix = 'Banner';

      // ----------------------------------------------------------------------
      // 필수체크-콘텐츠유형별
      // ----------------------------------------------------------------------
      $('#inputContsForm'+postFix+' input[name=dpStartDt'+postFix+']'  ).prop("required", true);
      $('#inputContsForm'+postFix+' input[name=dpStartHour'+postFix+']').prop("required", true);
      $('#inputContsForm'+postFix+' input[name=dpStartMin'+postFix+']' ).prop("required", true);
      $('#inputContsForm'+postFix+' input[name=dpEndDt'+postFix+']'    ).prop("required", true);
      $('#inputContsForm'+postFix+' input[name=dpEndHour'+postFix+']'  ).prop("required", true);
      $('#inputContsForm'+postFix+' input[name=dpEndMin'+postFix+']'   ).prop("required", true);
      $('#inputContsForm'+postFix+' input[name=dpRangeTp'+postFix+']'  ).prop("required", true);
      $('#inputContsForm'+postFix+' input[name=titleNm'+postFix+']'    ).prop("required", true);
      $('#inputContsForm'+postFix+' input[name=linkUrlPc'+postFix+']'  ).prop("required", true);
      $('#inputContsForm'+postFix+' input[name=sort'+postFix+']'       ).prop("required", true);
      if(imgPathMobileBanner == undefined || imgPathMobileBanner == null || imgPathMobileBanner == '') {
        fnKendoMessage({ message : "<span style='font-weight: bold;color:red'>[이미지1]</span>필수 입력입니다." });
        return;
      }
      if(imgPathPcBanner == undefined || imgPathPcBanner == null || imgPathPcBanner == '') {
        fnKendoMessage({ message : "<span style='font-weight: bold;color:red'>[이미지2]</span>필수 입력입니다." });
        return;
      }

      // ----------------------------------------------------------------------
      // 공통항목
      // ----------------------------------------------------------------------
      // 전시기간-시작
      paramDpStartDe   = $('#dpStartDt'+postFix).val();
      paramDpStartHour = $('#dpStartHour'+postFix).val();
      paramDpStartMin  = $('#dpStartMin'+postFix).val();
      if (paramDpStartDe != null) {
        paramDpStartDe = paramDpStartDe.replace(/-/gi, '');
      }
      paramDpStartDt = paramDpStartDt.concat(paramDpStartDe, paramDpStartHour, paramDpStartMin, '00');
      // 전시기간-종료
      paramDpEndDe   = $('#dpEndDt'+postFix).val();
      paramDpEndHour = $('#dpEndHour'+postFix).val();
      paramDpEndMin  = $('#dpEndMin'+postFix).val();
      if (paramDpEndDe != null) {
        paramDpEndDe = paramDpEndDe.replace(/-/gi, '');
      }
      paramDpEndDt = paramDpEndDt.concat(paramDpEndDe, paramDpEndHour, paramDpEndMin, '59');

      // 상위콘텐츠ID
      if (selectedContsLevel == 1) {
        paramPrntsContsId = 0;
      }
      else if (selectedContsLevel == 2) {
        paramPrntsContsId  = selectedContsLv1Data.dpContsId;
        paramLevel1ContsId = selectedContsLv1Data.dpContsId;
      }
      else if (selectedContsLevel == 3) {
        paramPrntsContsId = selectedContsLv2Data.dpContsId;

        paramLevel1ContsId = selectedContsLv1Data.dpContsId;
        paramLevel2ContsId = selectedContsLv2Data.dpContsId;
      }

      // ----------------------------------------------------------------------
      // 데이터 Set
      // ----------------------------------------------------------------------
      data = $('#inputContsForm'+postFix).formSerialize(true);
      // 필수항목
      contsData.dpInventoryId         = selectedInventoryData.dpInventoryId;
      contsData.ilCtgryId             = paramIlCtgryId;
      contsData.contsTp               = selectedContsTp;
      contsData.contsLevel            = selectedContsLevel;
      contsData.prntsContsId          = paramPrntsContsId;
      contsData.level1ContsId         = paramLevel1ContsId;
      contsData.level2ContsId         = paramLevel2ContsId;
      contsData.level3ContsId         = paramLevel3ContsId;
      contsData.dpStartDt             = paramDpStartDt;
      contsData.dpEndDt               = paramDpEndDt;
      contsData.dpRangeTp             = $('#dpRangeTp'+postFix).val();
      contsData.titleNm               = $('#titleNm'+postFix).val();
      contsData.linkUrlPc             = $('#linkUrlPc'+postFix).val();
      contsData.linkUrlMobile         = $('#linkUrlPc'+postFix).val();
      contsData.sort                  = $('#sort'+postFix).val();
      contsData.useYn                 = 'Y';
      contsData.delYn                 = 'N';
      // 비필수항목
      contsData.text1                 = $('#text1'+postFix).val();
      contsData.text1Color            = $('#text1Color'+postFix).val();
      contsData.text2                 = $('#text2'+postFix).val();
      contsData.text2Color            = $('#text2Color'+postFix).val();
      contsData.text3                 = $('#text3'+postFix).val();
      contsData.text3Color            = $('#text3Color'+postFix).val();
      contsData.imgPathPc             = imgPathPcBanner;
      contsData.imgOriginNmPc         = imgOriginNmPcBanner;
      contsData.imgPathMobile         = imgPathMobileBanner;
      contsData.imgOriginNmMobile     = imgOriginNmMobileBanner;
      contsData.gifImgPathPc          = gifImgPathPcBanner;
      contsData.gifImgOriginNmPc      = gifImgOriginNmPcBanner;
      contsData.gifImgPathMobile      = gifImgPathMobileBanner;
      contsData.gifImgOriginNmMobile  = gifImgOriginNmMobileBanner;

      // 반환
      resultDataObj.data = data;
      resultDataObj.contsData = contsData;
    }
    else if (selectedContsTp == 'DP_CONTENTS_TP.BRAND') {
      // ----------------------------------------------------------------------
      // * BRAND
      // ----------------------------------------------------------------------
      postFix = 'Brand';

      // ----------------------------------------------------------------------
      // 필수체크-콘텐츠유형별
      // ----------------------------------------------------------------------
      $('#inputContsForm'+postFix+' input[name=dpStartDt'+postFix+']'  ).prop("required", true);
      $('#inputContsForm'+postFix+' input[name=dpStartHour'+postFix+']').prop("required", true);
      $('#inputContsForm'+postFix+' input[name=dpStartMin'+postFix+']' ).prop("required", true);
      $('#inputContsForm'+postFix+' input[name=dpEndDt'+postFix+']'    ).prop("required", true);
      $('#inputContsForm'+postFix+' input[name=dpEndHour'+postFix+']'  ).prop("required", true);
      $('#inputContsForm'+postFix+' input[name=dpEndMin'+postFix+']'   ).prop("required", true);
      $('#inputContsForm'+postFix+' input[name=dpRangeTp'+postFix+']'  ).prop("required", true);
      $('#inputContsForm'+postFix+' input[name=titleNm'+postFix+']'    ).prop("required", true);
      $('#inputContsForm'+postFix+' input[name=comboBrandId'+postFix+']'  ).prop("required", true);
      $('#inputContsForm'+postFix+' input[name=sort'+postFix+']'       ).prop("required", true);

      // ----------------------------------------------------------------------
      // 공통항목
      // ----------------------------------------------------------------------
      // 전시기간-시작
      paramDpStartDe   = $('#dpStartDt'+postFix).val();
      paramDpStartHour = $('#dpStartHour'+postFix).val();
      paramDpStartMin  = $('#dpStartMin'+postFix).val();
      if (paramDpStartDe != null) {
        paramDpStartDe = paramDpStartDe.replace(/-/gi, '');
      }
      paramDpStartDt = paramDpStartDt.concat(paramDpStartDe, paramDpStartHour, paramDpStartMin, '00');
      // 전시기간-종료
      paramDpEndDe   = $('#dpEndDt'+postFix).val();
      paramDpEndHour = $('#dpEndHour'+postFix).val();
      paramDpEndMin  = $('#dpEndMin'+postFix).val();
      if (paramDpEndDe != null) {
        paramDpEndDe = paramDpEndDe.replace(/-/gi, '');
      }
      paramDpEndDt = paramDpEndDt.concat(paramDpEndDe, paramDpEndHour, paramDpEndMin, '59');

      // 상위콘텐츠ID
      if (selectedContsLevel == 1) {
        paramPrntsContsId = 0;
      }
      else if (selectedContsLevel == 2) {
        paramPrntsContsId  = selectedContsLv1Data.dpContsId;
        paramLevel1ContsId = selectedContsLv1Data.dpContsId;
      }
      else if (selectedContsLevel == 3) {
        paramPrntsContsId = selectedContsLv2Data.dpContsId;

        paramLevel1ContsId = selectedContsLv1Data.dpContsId;
        paramLevel2ContsId = selectedContsLv2Data.dpContsId;
      }

      // ----------------------------------------------------------------------
      // 데이터 Set
      // ----------------------------------------------------------------------
      data = $('#inputContsForm'+postFix).formSerialize(true);
      // 필수항목
      contsData.dpInventoryId         = selectedInventoryData.dpInventoryId;
      contsData.ilCtgryId             = paramIlCtgryId;
      contsData.contsTp               = selectedContsTp;
      contsData.contsLevel            = selectedContsLevel;
      contsData.prntsContsId          = paramPrntsContsId;
      contsData.level1ContsId         = paramLevel1ContsId;
      contsData.level2ContsId         = paramLevel2ContsId;
      contsData.level3ContsId         = paramLevel3ContsId;
      contsData.dpStartDt             = paramDpStartDt;
      contsData.dpEndDt               = paramDpEndDt;
      contsData.dpRangeTp             = $('#dpRangeTp'+postFix).val();
      contsData.titleNm               = $('#titleNm'+postFix).val();
      contsData.contsId               = $('#comboBrandId'+postFix).val();
      contsData.sort                  = $('#sort'+postFix).val();
      contsData.useYn                 = 'Y';
      contsData.delYn                 = 'N';
      // 비필수항목
      contsData.text1                 = $('#text1'+postFix).val();
      contsData.text1Color            = $('#text1Color'+postFix).val();
      contsData.text2                 = $('#text2'+postFix).val();
      contsData.text2Color            = $('#text2Color'+postFix).val();
      contsData.text3                 = $('#text3'+postFix).val();
      contsData.text3Color            = $('#text3Color'+postFix).val();
      contsData.imgPathPc             = imgPathPcBrand;
      contsData.imgOriginNmPc         = imgOriginNmPcBrand;
      contsData.imgPathMobile         = imgPathMobileBrand;
      contsData.imgOriginNmMobile     = imgOriginNmMobileBrand;

      // 반환
      resultDataObj.data = data;
      resultDataObj.contsData = contsData;
    }
    else if (selectedContsTp == 'DP_CONTENTS_TP.CATEGORY') {
      // ----------------------------------------------------------------------
      // * CATEGORY
      // ----------------------------------------------------------------------
      postFix = 'Category';

      // ----------------------------------------------------------------------
      // 필수체크-콘텐츠유형별
      // ----------------------------------------------------------------------
      $('#inputContsForm'+postFix+' input[name=dpStartDt'+postFix+']'  ).prop("required", true);
      $('#inputContsForm'+postFix+' input[name=dpStartHour'+postFix+']').prop("required", true);
      $('#inputContsForm'+postFix+' input[name=dpStartMin'+postFix+']' ).prop("required", true);
      $('#inputContsForm'+postFix+' input[name=dpEndDt'+postFix+']'    ).prop("required", true);
      $('#inputContsForm'+postFix+' input[name=dpEndHour'+postFix+']'  ).prop("required", true);
      $('#inputContsForm'+postFix+' input[name=dpEndMin'+postFix+']'   ).prop("required", true);
      $('#inputContsForm'+postFix+' input[name=dpRangeTp'+postFix+']'  ).prop("required", true);
      $('#inputContsForm'+postFix+' input[name=titleNm'+postFix+']'    ).prop("required", true);
      $('#inputContsForm'+postFix+' input[name=comboMallDivisionCategory'+postFix+']').prop("required", true);
      $('#inputContsForm'+postFix+' input[name=categoryDepth1'+postFix+']'           ).prop("required", true);
      $('#inputContsForm'+postFix+' input[name=sort'+postFix+']'       ).prop("required", true);

      // ----------------------------------------------------------------------
      // 공통항목
      // ----------------------------------------------------------------------
      // 전시기간-시작
      paramDpStartDe   = $('#dpStartDt'+postFix).val();
      paramDpStartHour = $('#dpStartHour'+postFix).val();
      paramDpStartMin  = $('#dpStartMin'+postFix).val();
      if (paramDpStartDe != null) {
        paramDpStartDe = paramDpStartDe.replace(/-/gi, '');
      }
      paramDpStartDt = paramDpStartDt.concat(paramDpStartDe, paramDpStartHour, paramDpStartMin, '00');
      // 전시기간-종료
      paramDpEndDe   = $('#dpEndDt'+postFix).val();
      paramDpEndHour = $('#dpEndHour'+postFix).val();
      paramDpEndMin  = $('#dpEndMin'+postFix).val();
      if (paramDpEndDe != null) {
        paramDpEndDe = paramDpEndDe.replace(/-/gi, '');
      }
      paramDpEndDt = paramDpEndDt.concat(paramDpEndDe, paramDpEndHour, paramDpEndMin, '59');

      // 상위콘텐츠ID
      if (selectedContsLevel == 1) {
        paramPrntsContsId = 0;
      }
      else if (selectedContsLevel == 2) {
        paramPrntsContsId  = selectedContsLv1Data.dpContsId;
        paramLevel1ContsId = selectedContsLv1Data.dpContsId;
      }
      else if (selectedContsLevel == 3) {
        paramPrntsContsId = selectedContsLv2Data.dpContsId;

        paramLevel1ContsId = selectedContsLv1Data.dpContsId;
        paramLevel2ContsId = selectedContsLv2Data.dpContsId;
      }

      // 전시카테고리ID
      if ($('#categoryDepth1').val() != null && $('#categoryDepth1').val() != '') {
        paramDpCtgryId = $('#categoryDepth1').val()
      }
      if ($('#categoryDepth2').val() != null && $('#categoryDepth2').val() != '') {
        paramDpCtgryId = $('#categoryDepth2').val()
      }
      if ($('#categoryDepth3').val() != null && $('#categoryDepth3').val() != '') {
        paramDpCtgryId = $('#categoryDepth3').val()
      }
      if ($('#categoryDepth4').val() != null && $('#categoryDepth4').val() != '') {
        paramDpCtgryId = $('#categoryDepth4').val()
      }

      // ----------------------------------------------------------------------
      // 데이터 Set
      // ----------------------------------------------------------------------
      data = $('#inputContsForm'+postFix).formSerialize(true);
      // 필수항목
      contsData.dpInventoryId         = selectedInventoryData.dpInventoryId;
      contsData.ilCtgryId             = paramIlCtgryId;
      contsData.contsTp               = selectedContsTp;
      contsData.contsLevel            = selectedContsLevel;
      contsData.prntsContsId          = paramPrntsContsId;
      contsData.level1ContsId         = paramLevel1ContsId;
      contsData.level2ContsId         = paramLevel2ContsId;
      contsData.level3ContsId         = paramLevel3ContsId;
      contsData.dpStartDt             = paramDpStartDt;
      contsData.dpEndDt               = paramDpEndDt;
      contsData.dpRangeTp             = $('#dpRangeTp'+postFix).val();
      contsData.titleNm               = $('#titleNm'+postFix).val();
      contsData.dpCtgryId             = paramDpCtgryId;
      contsData.sort                  = $('#sort'+postFix).val();
      contsData.useYn                 = 'Y';
      contsData.delYn                 = 'N';
      // 비필수항목
      contsData.text1                 = $('#text1'+postFix).val();
      contsData.text1Color            = $('#text1Color'+postFix).val();
      contsData.text2                 = $('#text2'+postFix).val();
      contsData.text2Color            = $('#text2Color'+postFix).val();
      contsData.text3                 = $('#text3'+postFix).val();
      contsData.text3Color            = $('#text3Color'+postFix).val();

      // 반환
      resultDataObj.data = data;
      resultDataObj.contsData = contsData;
    }
    else if (selectedContsTp == 'DP_CONTENTS_TP.GOODS') {
      // ----------------------------------------------------------------------
      // * GOODS
      // ----------------------------------------------------------------------
      postFix = 'Goods';

      if ($('#mode').val() == 'conts.insert') {
        // --------------------------------------------------------------------
        // GOODS-등록
        // --------------------------------------------------------------------

        // --------------------------------------------------------------------
        // 필수체크-콘텐츠유형별
        // --------------------------------------------------------------------
        $('#inputContsForm'+postFix+' input[name=dpStartDt'+postFix+']'  ).prop("required", true);
        $('#inputContsForm'+postFix+' input[name=dpStartHour'+postFix+']').prop("required", true);
        $('#inputContsForm'+postFix+' input[name=dpStartMin'+postFix+']' ).prop("required", true);
        $('#inputContsForm'+postFix+' input[name=dpEndDt'+postFix+']'    ).prop("required", true);
        $('#inputContsForm'+postFix+' input[name=dpEndHour'+postFix+']'  ).prop("required", true);
        $('#inputContsForm'+postFix+' input[name=dpEndMin'+postFix+']'   ).prop("required", true);
        $('#inputContsForm'+postFix+' input[name=dpRangeTp'+postFix+']'  ).prop("required", true);
        $('#inputContsForm'+postFix+' input[name=titleNm'+postFix+']'    ).prop("required", true);
        //$('#inputContsForm'+postFix+' input[name=sort'+postFix+']'       ).prop("required", true);

        // --------------------------------------------------------------------
        // 공통항목
        // --------------------------------------------------------------------
        // 전시기간-시작
        paramDpStartDe   = $('#dpStartDt'+postFix).val();
        paramDpStartHour = $('#dpStartHour'+postFix).val();
        paramDpStartMin  = $('#dpStartMin'+postFix).val();
        if (paramDpStartDe != null) {
          paramDpStartDe = paramDpStartDe.replace(/-/gi, '');
        }
        paramDpStartDt = paramDpStartDt.concat(paramDpStartDe, paramDpStartHour, paramDpStartMin, '00');
        // 전시기간-종료
        paramDpEndDe   = $('#dpEndDt'+postFix).val();
        paramDpEndHour = $('#dpEndHour'+postFix).val();
        paramDpEndMin  = $('#dpEndMin'+postFix).val();
        if (paramDpEndDe != null) {
          paramDpEndDe = paramDpEndDe.replace(/-/gi, '');
        }
        paramDpEndDt = paramDpEndDt.concat(paramDpEndDe, paramDpEndHour, paramDpEndMin, '59');

        // 상위콘텐츠ID
        if (selectedContsLevel == 1) {
          paramPrntsContsId = 0;
        }
        else if (selectedContsLevel == 2) {
          paramPrntsContsId  = selectedContsLv1Data.dpContsId;
          paramLevel1ContsId = selectedContsLv1Data.dpContsId;
        }
        else if (selectedContsLevel == 3) {
          paramPrntsContsId = selectedContsLv2Data.dpContsId;
          paramLevel1ContsId = selectedContsLv1Data.dpContsId;
          paramLevel2ContsId = selectedContsLv2Data.dpContsId;
        }

        // --------------------------------------------------------------------
        // 데이터 Set
        // --------------------------------------------------------------------
        data = $('#inputContsForm'+postFix).formSerialize(true);
        // 필수항목
        contsData.dpInventoryId         = selectedInventoryData.dpInventoryId;
        contsData.ilCtgryId             = paramIlCtgryId;
        contsData.contsTp               = selectedContsTp;
        contsData.contsLevel            = selectedContsLevel;
        contsData.prntsContsId          = paramPrntsContsId;
        contsData.level1ContsId         = paramLevel1ContsId;
        contsData.level2ContsId         = paramLevel2ContsId;
        contsData.level3ContsId         = paramLevel3ContsId;
        contsData.dpStartDt             = paramDpStartDt;
        contsData.dpEndDt               = paramDpEndDt;
        contsData.dpRangeTp             = $('#dpRangeTp'+postFix).val();
        contsData.titleNm               = $('#titleNm'+postFix).val();
        //contsData.dpCondTp              = 'DP_COND_TP.MANUAL';            // 노출조건유형:메뉴얼(직접등록)
        //contsData.dpSortTp              = 'DP_SORT_TP.MANUAL';            // 노출순서유형:메뉴얼(직접등록)
        //contsData.useYn                 = 'Y';
        //contsData.delYn                 = 'N';


        var goodsContsDataArr = new Array();

        if (addDataObjArray != undefined && addDataObjArray != null && addDataObjArray != 'null' && addDataObjArray != '') {
          //console.log('#[1] fnSetParamValue.addDataObjArray :: ', JSON.stringify(addDataObjArray));
          for (var i = 0; i < addDataObjArray.length; i++) {

            //console.log('# addDataObjArray[', i, '].dpContsId :: ', addDataObjArray[i].dpContsId);
            var goodsContsData = new Object();
            //goodsContsData = contsData;
            // 전시콘텐츠정보
            goodsContsData.dpInventoryId         = contsData.dpInventoryId;
            goodsContsData.ilCtgryId             = contsData.ilCtgryId;
            goodsContsData.contsTp               = contsData.contsTp;
            goodsContsData.contsLevel            = contsData.contsLevel;
            goodsContsData.prntsContsId          = contsData.prntsContsId;
            goodsContsData.level1ContsId         = contsData.level1ContsId;
            goodsContsData.level2ContsId         = contsData.level2ContsId;
            goodsContsData.level3ContsId         = contsData.level3ContsId;
            goodsContsData.dpStartDt             = contsData.dpStartDt;
            goodsContsData.dpEndDt               = contsData.dpEndDt;
            goodsContsData.dpRangeTp             = contsData.dpRangeTp;
            goodsContsData.titleNm               = contsData.titleNm;
            goodsContsData.dpCondTp              = 'DP_COND_TP.MANUAL';            // 노출조건유형:메뉴얼(직접등록)
            goodsContsData.dpSortTp              = 'DP_SORT_TP.MANUAL';            // 노출순서유형:메뉴얼(직접등록)
            goodsContsData.useYn                 = 'Y';
            goodsContsData.delYn                 = 'N';
            // 상품입력정보
            goodsContsData.contsId               = Number(addDataObjArray[i].dpContsId);  // IL_GOODS_ID
            goodsContsData.goodsNm               = addDataObjArray[i].goodsNm;            // GOODS_NM
            goodsContsData.sort                  = Number(addDataObjArray[i].sort);
            goodsContsData.text1                 = addDataObjArray[i].text1;
            goodsContsData.text1Color            = addDataObjArray[i].text1Color;
            goodsContsData.text2                 = addDataObjArray[i].text2;
            goodsContsData.text2Color            = addDataObjArray[i].text2Color;
            goodsContsData.text3                 = addDataObjArray[i].text3;
            goodsContsData.text3Color            = addDataObjArray[i].text3Color;

            goodsContsDataArr.push(goodsContsData);
            //console.log('#[2] fnSetParamValue.goodsContsData[', i, '] :: ', JSON.stringify(goodsContsData));
          } // End of for (var i = 0; i < addDataObjArray.length; i++)

        } // End of if (addDataObjArray != undefined && addDataObjArray != null && addDataObjArray != 'null' && addDataObjArray != '')
        //console.log('#[3] fnSetParamValue.goodsContsDataArr[', i, '] :: ', JSON.stringify(goodsContsDataArr));
        // 반환
        resultDataObj.data = data;
        resultDataObj.contsData = goodsContsDataArr;

      }
      else {
        // --------------------------------------------------------------------
        // GOODS-수정
        // --------------------------------------------------------------------

        // --------------------------------------------------------------------
        // 필수체크-콘텐츠유형별
        // --------------------------------------------------------------------
        $('#inputContsForm'+postFix+' input[name=dpStartDt'+postFix+']'  ).prop("required", true);
        $('#inputContsForm'+postFix+' input[name=dpStartHour'+postFix+']').prop("required", true);
        $('#inputContsForm'+postFix+' input[name=dpStartMin'+postFix+']' ).prop("required", true);
        $('#inputContsForm'+postFix+' input[name=dpEndDt'+postFix+']'    ).prop("required", true);
        $('#inputContsForm'+postFix+' input[name=dpEndHour'+postFix+']'  ).prop("required", true);
        $('#inputContsForm'+postFix+' input[name=dpEndMin'+postFix+']'   ).prop("required", true);
        $('#inputContsForm'+postFix+' input[name=dpRangeTp'+postFix+']'  ).prop("required", true);
        $('#inputContsForm'+postFix+' input[name=titleNm'+postFix+']'    ).prop("required", true);
        $('#inputContsForm'+postFix+' input[name=sort'+postFix+']'       ).prop("required", true);

        // --------------------------------------------------------------------
        // 공통항목
        // --------------------------------------------------------------------
        // 전시기간-시작
        paramDpStartDe   = $('#dpStartDt'+postFix).val();
        paramDpStartHour = $('#dpStartHour'+postFix).val();
        paramDpStartMin  = $('#dpStartMin'+postFix).val();
        if (paramDpStartDe != null) {
          paramDpStartDe = paramDpStartDe.replace(/-/gi, '');
        }
        paramDpStartDt = paramDpStartDt.concat(paramDpStartDe, paramDpStartHour, paramDpStartMin, '00');
        // 전시기간-종료
        paramDpEndDe   = $('#dpEndDt'+postFix).val();
        paramDpEndHour = $('#dpEndHour'+postFix).val();
        paramDpEndMin  = $('#dpEndMin'+postFix).val();
        if (paramDpEndDe != null) {
          paramDpEndDe = paramDpEndDe.replace(/-/gi, '');
        }
        paramDpEndDt = paramDpEndDt.concat(paramDpEndDe, paramDpEndHour, paramDpEndMin, '59');

        // 상위콘텐츠ID
        if (selectedContsLevel == 1) {
          paramPrntsContsId = 0;
        }
        else if (selectedContsLevel == 2) {
          paramPrntsContsId  = selectedContsLv1Data.dpContsId;
          paramLevel1ContsId = selectedContsLv1Data.dpContsId;
        }
        else if (selectedContsLevel == 3) {
          paramPrntsContsId = selectedContsLv2Data.dpContsId;

          paramLevel1ContsId = selectedContsLv1Data.dpContsId;
          paramLevel2ContsId = selectedContsLv2Data.dpContsId;
        }

        // ----------------------------------------------------------------------
        // 데이터 Set
        // ----------------------------------------------------------------------
        data = $('#inputContsForm'+postFix).formSerialize(true);
        // 필수항목
        contsData.dpInventoryId         = selectedInventoryData.dpInventoryId;
        contsData.ilCtgryId             = paramIlCtgryId;
        contsData.contsTp               = selectedContsTp;
        contsData.contsLevel            = selectedContsLevel;
        contsData.prntsContsId          = paramPrntsContsId;
        contsData.level1ContsId         = paramLevel1ContsId;
        contsData.level2ContsId         = paramLevel2ContsId;
        contsData.level3ContsId         = paramLevel3ContsId;
        contsData.dpStartDt             = paramDpStartDt;
        contsData.dpEndDt               = paramDpEndDt;
        contsData.dpRangeTp             = $('#dpRangeTp'+postFix).val();
        contsData.titleNm               = $('#titleNm'+postFix).val();
        contsData.dpCtgryId             = paramDpCtgryId;
        contsData.sort                  = $('#sort'+postFix).val();
        contsData.useYn                 = 'Y';
        contsData.delYn                 = 'N';
        // 비필수항목
        contsData.text1                 = $('#text1'+postFix).val();
        contsData.text1Color            = $('#text1Color'+postFix).val();
        contsData.text2                 = $('#text2'+postFix).val();
        contsData.text2Color            = $('#text2Color'+postFix).val();
        contsData.text3                 = $('#text3'+postFix).val();
        contsData.text3Color            = $('#text3Color'+postFix).val();

        // 반환
        resultDataObj.data = data;
        resultDataObj.contsData = contsData;

      }
    }
    else {
      fnMessage('', '콘텐츠유형을 확인하세요.', '');
      return false;
    }
    //console.log('#[3] fnSetParamValue.resultDataObj.contsData[', i, '] :: ', JSON.stringify(resultDataObj.contsData));
    // ------------------------------------------------------------------------
    // 반환
    // ------------------------------------------------------------------------
    return resultDataObj;
  } // End of function fnSetParamValue()

  // ==========================================================================
  // # 콜백합수
  // ==========================================================================
  function fnBizCallback(id, result) {
    //console.log('# fnBizCallback Start ('+id+', data)');
    //console.log('# result['+id+'] :: ' + JSON.stringify(result));
    // 모드 초기화
    //$('#mode').val('');

    switch(id){
      case 'conts.delete':
        // --------------------------------------------------------------------
        // 콘텐츠 삭제
        // --------------------------------------------------------------------
        if (result.resultCode == '0000') {

          // 콘텐츠 재조회
          if (selectedContsLevel == 1) {
            // 콘텐츠 그리드 Lv1 조회
            fnBtnSearchContsLv1();
            // 선택삭제 버튼 비활성
            $('#fnBtnContsLv1Del').data('kendoButton').enable(false);
          }
          else if (selectedContsLevel == 2) {
            // 콘텐츠 그리드 Lv2 조회
            fnBtnSearchContsLv2();
            // 선택삭제 버튼 비활성
            $('#fnBtnContsLv2Del').data('kendoButton').enable(false);
          }
          else if (selectedContsLevel == 3) {
            // 콘텐츠 그리드 Lv3 조회
            fnBtnSearchContsLv3();
            // 선택삭제 버튼 비활성
            $('#fnBtnContsLv3Del').data('kendoButton').enable(false);
          }
          // 선택콘텐츠 초기화
          selectedContsData = null;
          selectedContsLevel = null;
          // 모드 초기화(성공인경우 초기화)
          $('#mode').val('');
          // 완료메시지
          fnMessage('', '콘텐츠가 삭제되었습니다.', '');
        }
        else {
          fnMessage('', '['+result.resultCode+'] ' + result.resultMessage, '');
        }
        break;
      case 'conts.insert':
        // --------------------------------------------------------------------
        // 콘텐츠 등록
        // --------------------------------------------------------------------
        if (result.resultCode == '0000') {
          //console.log('# ★★★★★★★★★★★★★★★★★★★★');
          //console.log('# result :: ', JSON.stringify(result));

          if ((result.dupTotal  != undefined && result.dupTotal  != null && result.dupTotal  != 'null' && result.dupTotal  != '' && result.dupTotal  > 0) ||
              (result.failTotal != undefined && result.failTotal != null && result.failTotal != 'null' && result.failTotal != '' && result.failTotal > 0)) {

            var msg = '';

            // 중복상품
            if (result.dupTotal != undefined && result.dupTotal != null && result.dupTotal != 'null' && result.dupTotal != '' && result.dupTotal > 0) {

              msg += '<div>중복된 상품이 ' + result.dupTotal + '건 존재합니다.</div>';
              msg += '<br/>';

              //var dupContsArr = new Array();
              var dupContsArr = result.dupRows;

              for (var i = 0; i < dupContsArr.length; i++) {
                msg += '<div class="textLeft">[' + dupContsArr[i].contsId + '] ' + dupContsArr[i].goodsNm + "</div>";
              }
              msg += '<br/>';
              msg += '<br/>';
            }

            // 오류상품
            if (result.failTotal != undefined && result.failTotal != null && result.failTotal != 'null' && result.failTotal != '' && result.failTotal > 0) {

              msg += '<div>등록오류 상품이 ' + result.failiTotal + '건 존재합니다.</div>';
              msg += '<br/>';

              //var failContsArr = new Array();
              var failContsArr = result.failRows;

              for (var i = 0; i < failContsArr.length; i++) {
                msg += '<div class="textLeft">[' + failContsArr[i].contsId + '] ' + failContsArr[i].goodsNm + '</div>';
              }
            }

            fnMessage('', msg, '');
            return false;
          }

          // 팝업닫기
          fnPopupClose();

          // 콘텐츠 재조회
          if (selectedContsLevel == 1) {
            // 콘텐츠 그리드 Lv1 조회
            fnBtnSearchContsLv1();
          }
          else if (selectedContsLevel == 2) {
            // 콘텐츠 그리드 Lv2 조회
            fnBtnSearchContsLv2();
          }
          else if (selectedContsLevel == 3) {
            fnBtnSearchContsLv3();
          }
          // 선택콘텐츠 초기화
          selectedContsData = null;
          // 모드 초기화(성공인경우 초기화)
          $('#mode').val('');
          // 완료메시지
          fnMessage('', '콘텐츠가 등록되었습니다.', '');
        }
        else {
          fnMessage('', '['+result.resultCode+'] ' + result.resultMessage, '');
        }
        break;
      case 'conts.update':
        // --------------------------------------------------------------------
        // 인벤토리 수정
        // --------------------------------------------------------------------
        if (result.resultCode == '0000') {

          fnMessage('', '콘텐츠가 수정되었습니다.', '');

          // 팝업닫기
          fnPopupClose();

          // 콘텐츠 재조회
          if (selectedContsLevel == 1) {
            // 콘텐츠 그리드 Lv1 조회
            fnBtnSearchContsLv1();
          }
          else if (selectedContsLevel == 2) {
            // 콘텐츠 그리드 Lv2 조회
            fnBtnSearchContsLv2();
          }
          else if (selectedContsLevel == 3) {
            fnBtnSearchContsLv3();
          }
          // 선택콘텐츠 초기화
          selectedContsData = null;
          // 모드 초기화(성공인경우 초기화)
          $('#mode').val('');
          // 완료메시지
          fnMessage('', '콘텐츠가 수정되었습니다.', '');
        }
        else {
          fnMessage('', '['+result.resultCode+'] ' + result.resultMessage, '');
        }
        break;
      case 'conts.sort':
        // --------------------------------------------------------------------
        // 콘텐츠 정렬
        // --------------------------------------------------------------------
          //console.log('# fnBizCallback.conts.sort :: ' + JSON.stringify(result));
          if (result.resultCode == '0000') {

            fnMessage('', '콘텐츠 순번이 변경되었습니다.', '');

            // * 콘텐츠조회
            // 페이지ID Set
            //dpPageId = selectedPageData.dpPageid;
            // 콘텐츠 그리드 초기화(이 때 dpPageId 가 Set 됨)
            if (selectedContsLevel == 1) {
              fnBtnSearchContsLv1();
            }
            else if (selectedContsLevel == 2) {
              fnBtnSearchContsLv2()
            }
            else if (selectedContsLevel == 3) {
              fnBtnSearchContsLv3()
            }
            // 모드 초기화(성공인경우 초기화)
            $('#mode').val('');
          }
          else {
            fnMessage('', '콘텐츠 순서변경이 실패하였습니다.\n다시 시도해 주십시오.', '');
          }
          // 순서변경 콘텐츠레벨 초기화
          selectedContsLevel = '';
        break;
      case 'inventoryGroup.insert':
        // --------------------------------------------------------------------
        // 인벤토리그룹 등록
        // --------------------------------------------------------------------
        if (result.resultCode == '0000') {
          //console.log('# ★★★★★★★★★★★★★★★★★★★★');
          //console.log('# result :: ', JSON.stringify(result));

          //if ((result.dupTotal  != undefined && result.dupTotal  != null && result.dupTotal  != 'null' && result.dupTotal  != '' && result.dupTotal  > 0) ||
          //    (result.failTotal != undefined && result.failTotal != null && result.failTotal != 'null' && result.failTotal != '' && result.failTotal > 0)) {
          //
          //  var msg = '';
          //
          //  // 중복상품
          //  if (result.dupTotal != undefined && result.dupTotal != null && result.dupTotal != 'null' && result.dupTotal != '' && result.dupTotal > 0) {
          //
          //    msg += '<div>중복된 상품이 ' + result.dupTotal + '건 존재합니다.</div>';
          //    msg += '<br/>';
          //
          //    //var dupContsArr = new Array();
          //    var dupContsArr = result.dupRows;
          //
          //    for (var i = 0; i < dupContsArr.length; i++) {
          //      msg += '<div class="textLeft">[' + dupContsArr[i].contsId + '] ' + dupContsArr[i].goodsNm + "</div>";
          //    }
          //    msg += '<br/>';
          //    msg += '<br/>';
          //  }
          //
          //  // 오류상품
          //  if (result.failTotal != undefined && result.failTotal != null && result.failTotal != 'null' && result.failTotal != '' && result.failTotal > 0) {
          //
          //    msg += '<div>등록오류 상품이 ' + result.failiTotal + '건 존재합니다.</div>';
          //    msg += '<br/>';
          //
          //    //var failContsArr = new Array();
          //    var failContsArr = result.failRows;
          //
          //    for (var i = 0; i < failContsArr.length; i++) {
          //      msg += '<div class="textLeft">[' + failContsArr[i].contsId + '] ' + failContsArr[i].goodsNm + '</div>';
          //    }
          //  }
          //
          //  fnMessage('', msg, '');
          //  return false;
          //}

          //// 팝업닫기
          //fnPopupClose();
          //// 팝업-저장버튼명
          //$('#spanInventoryBtnSaveLavel').text('신규저장');
          //$('#fnBtnSaveInventoryGroup').hide();
          // console.log('# result :: ', JSON.stringify(result));
          // 인벤토리그룹 그리드 재조회
          let data = $("#inputInventoryGroupForm").formSerialize(true);
          inventoryGroupGridDs.read(data);

          //
          selectedInventoryGroupData = result.detail;

          // 인베토리그룹ID Set (새로 등록된)
          $('#dpInventoryGrpIdDiv').text(result.detail.dpPageId);
          // 버튼명 변경
          $('#spanInventoryBtnSaveLavel').text('수정저장');

          // Tree 데이터 초기화
          treeView.destroy();
          // Tree 재조회
          fnInitTree();
          // mode 변경 (성공인 경우 수정)
          $('#mode').val('inventoryGroup.update');
          //완료메시지
          fnMessage('', '인벤토리그룹이 등록되었습니다.', '');
        }
        else {
          // mode 변경 (실패인 경우 등록 유지)
          $('#mode').val('inventoryGroup.insert');
          fnMessage('', '['+result.resultCode+'] ' + result.resultMessage, '');
        }
        // 필수 해제
        $('#inputInventoryGroupForm input[name=inventoryGrpNm]'   ).prop("required", false);
        $('#inputInventoryGroupForm [name=inventoryCdsString]'    ).prop("required", false);
        $('#inputInventoryGroupForm input[name=inventoryGrpUseYn]').prop("required", false);
        $('#inputInventoryGroupForm input[name=sortInventoryGrp]' ).prop("required", false);

        break;
      case 'inventoryGroup.update':
        // --------------------------------------------------------------------
        // 인벤토리그룹 수정
        // --------------------------------------------------------------------
        if (result.resultCode == '0000') {
          //console.log('# ★★★★★★★★★★');
          // 팝업닫기
          //fnPopupClose();
          // 팝업 그리드 조회 실행
          let data = $("#inputInventoryGroupForm").formSerialize(true);
          inventoryGroupGridDs.read(data);
          //// 선택그룹정보 초기화
          //fnInitInventoryGroupDetail();
          //// 팝업-저장버튼
          //$('#spanInventoryBtnSaveLavel').text('수정저장');
          //$('#fnBtnSaveInventoryGroup').hide();
          // Tree 데이터 초기화
          treeView.destroy();
          // Tree 재조회
          fnInitTree();
          //완료메시지
          fnMessage('', '인벤토리그룹이 수정되었습니다.', '');
        }
        else {
          fnMessage('', '['+result.resultCode+'] ' + result.resultMessage, '');
        }
        // 필수 해제
        $('#inputInventoryGroupForm input[name=inventoryGrpNm]'   ).prop("required", false);
        $('#inputInventoryGroupForm [name=inventoryCdsString]'    ).prop("required", false);
        $('#inputInventoryGroupForm input[name=inventoryGrpUseYn]').prop("required", false);
        $('#inputInventoryGroupForm input[name=sortInventoryGrp]' ).prop("required", false);
        // mode 유지(수정)
        $('#mode').val("inventoryGroup.update");
        break;
      case 'inventoryGroup.delete':
        // --------------------------------------------------------------------
        // 인벤토리그룹 삭제
        // --------------------------------------------------------------------
        if (result.resultCode == '0000') {

          // 팝업닫기
          //fnPopupClose();
          // 팝업저장버튼명 변경
          // 인벤토리그룹정보 초기화 Set
          fnInitInventoryGroupDetail();
          // 저장버튼명
          $('#spanInventoryBtnSaveLavel').text('저장');
          // 팝업 그리드 조회 실행
          let data = $("#inputInventoryGroupForm").formSerialize(true);
          inventoryGroupGridDs.read(data);
          // Tree 데이터 초기화
          treeView.destroy();
          // Tree 재조회
          fnInitTree();
          // 모드 초기화(성공인경우 초기화)
          $('#mode').val('');
          // 완료메시지
          fnMessage('', '인벤토리그룹이 삭제되었습니다.', '');
        }
        else {
          fnMessage('', '['+result.resultCode+'] ' + result.resultMessage, '');
        }
        break;
      case 'inventoryGroup.sort':
        if (result.resultCode == '0000') {

          // 팝업닫기
          //fnPopupClose();
          // 그리드 조회 실행
          let data = $("#inputInventoryGroupForm").formSerialize(true);
          inventoryGroupGridDs.read(data);
          // Tree 데이터 초기화
          treeView.destroy();
          // Tree 재조회
          fnInitTree();
          // 모드 초기화(성공인경우 초기화)
          $('#mode').val('');
          // 완료메시지
          fnMessage('', '인벤토리그룹 순번이 변경되었습니다.', '');
        }
        else {
          fnMessage('', '['+result.resultCode+'] ' + result.resultMessage, '');
        }
        break;
    }

  }

  // **************************************************************************
  // -------------------------------  TREE START  -----------------------------
  // **************************************************************************
  // ==========================================================================
  // # fnInitTree
  // ==========================================================================
  function fnInitTree() {
      //console.log('# fnInitTree Start');
      var callUrl = '';

      if (pageSchTp == 'PAGE_SCH_TP.INVENTORY') {
        // 페이지검색유형.인벤토리

        if (pageTp == 'PAGE_TP.CATEGORY') {
          // 페이지유형.카테고리 (전시콘텐츠관리에서는 전시카테고리 페이지의 Tree와 동일한 데이터를 조회한다.)
          callUrl = '/admin/display/manage/selectDpCategoryList?'+ "pageTp=" + pageTp + '&mallDiv=' + mallDivision + "&useAllYn=" + useAllYn + "&contsListYn=Y";
        }
        else {
          // 페이지유형.페이지
          if(fnIsProgramAuth("AUTH_CATEGORY") && !fnIsProgramAuth("AUTH_PAGE")) {
            callUrl = '/admin/display/manage/selectDpCategoryList?'+ "pageTp=" + pageTp + '&mallDiv=' + mallDivision + "&useAllYn=" + useAllYn + "&contsListYn=Y";
          } else {
            callUrl = '/admin/display/manage/selectDpPageList?' + "pageTp=" + pageTp+ "&useAllYn=" + useAllYn;
          }
        }
      }
      else {
        // 페이지검색유형.그룹
        callUrl = '/admin/display/manage/selectDpInventoryGroupList';
      }

      // 선택페이지 초기화
      selectedPageData = null;

      //console.log('# callUrl :: ' + callUrl);
      dataSource = fnKendoTreeDS({
              url      : callUrl
            , model_id : 'dpPageId'
      });

      fnKendoTreeView({
            id            : 'Tree'
          , dataSource    : dataSource
          , dataTextField : 'pageNm'
          , template      : kendo.template($("#treeview-template").html())
          , autoBind      : false
          , dragAndDrop   : false
          , autoScroll    : true
          , change        : function(e){

                              // ----------------------------------------------
                              // Tree 클릭 처리
                              // ----------------------------------------------
                              fnClickTree (e);
                            }
          //, expand        : function(e) {
          //                    //console.log('# 펼친다... :: ' + JSON.stringify(e.sender.dataItem(e.node)));
          //                    //if (e != null && e != undefined && e != 'undefined') {
          //                    //  selectedPageData = e.sender.dataItem(e.node);
          //                    //}
          //                  }
          //, dragstart     : function(e){
          //                    console.log('# dragstart Event');
          //                    parentNode = treeView.parent(e.sourceNode);
          //                }
          //, drop        : function(e){
          //                    console.log('# drop Event');
          //                    var bool = false;
          //                    var dropParentNode = treeView.parent(e.dropTarget);
          //
          //                    // 같은 부모에 위치 이동만 가능
          //                    if( parentNode.length > 0 && dropParentNode.length > 0
          //                        && parentNode.data().uid == dropParentNode.data().uid
          //                        && ( e.dropPosition == 'before' || e.dropPosition == 'after' ) ){
          //                        bool = true;
          //                    }
          //
          //                    //최상단 노드일때 예외처리
          //                    if ( parentNode.length == 0
          //                        && ( e.dropPosition == 'before' || e.dropPosition == 'after' ) ){
          //                        bool = true;
          //                    }
          //                    e.setValid(bool);
          //                }
          //, dragend     : function(e){
          //                    console.log('# dragend Event');
          //                    var childrenData;
          //                    var sortData = Array();
          //                    console.log('# parentNode.length :: ' + parentNode.length);
          //                    if( parentNode.length > 0 ){
          //                        childrenData = dataSource.getByUid( parentNode.data().uid ).children.data();
          //                    }else{
          //                        childrenData = dataSource.data();
          //                    }
          //
          //                    for (var i=0; i < childrenData.length; i++) {
          //                        sortData[i] = childrenData[i].dpPageId;
          //                    };
          //
          //                    putSort(sortData);
          //                }
      });

      treeView = $('#Tree').data('kendoTreeView');

      // 스크롤 설정
      const maxHeight = 600;
      $("#Tree").css({"maxHeight" : maxHeight + "px"});

      // --------------------------------------------------------------------
      // 조회 요청
      // --------------------------------------------------------------------
      // 초기 Tree 조회
      //console.log('# 초기 Tree 조회');
      dataSource.read({'dpPageId':0});

      // 엑셀다운로드 버튼 비활성
      //$('#fnBtnExcelDownInventory').data('kendoButton').enable(false);
  }

  // ==========================================================================
  // # Tree 항목 선택
  // ==========================================================================
  function getTreeSelectData() {
    //console.log('# 선택한 Tree Data 가져오기');

    var uid = treeView.select().data('uid');
    selectedPageUid = uid;

    // ------------------------------------------------------------------------
    // # 선택한 페이지정보 전역변수에 Set
    // ------------------------------------------------------------------------
    selectedPageData = dataSource.getByUid(uid);   // 선택한 로우 데이터

    if (selectedPageData != null) {
      selectedDpPageId  = selectedPageData.dpPageId;
    }
    else {
      //console.log('# selectedPageData is Null');
    }
  }

  // ==========================================================================
  // # Tree 정렬
  // ==========================================================================
  function compareTreeSort(a, b) {
    return Number(a.sort) - Number(b.sort);
  }

  // ==========================================================================
  // # Tree 항목 추가
  // ==========================================================================
  function fnTreeDataAdd(detail){
    //console.log('# fnTreeDataAdd.detail :: ' + JSON.stringify(detail));

    const sortOpt = [
                      {field : "sort"   , dir : "asc", compare : compareTreeSort}
                    , {field : "pageCd" , dir : "asc"}
                    ];

    detail.hasChildren = false;
    var uid = treeView.select().data('uid');
    //console.log('# fnTreeDataAdd.uid :: ' + uid);

    if( uid != undefined ){
      // ----------------------------------------------------------------------
      // Tree 선택했을 경우
      // ----------------------------------------------------------------------

      if (detail.depth == '1') {
        // --------------------------------------------------------------------
        // 최상위
        // --------------------------------------------------------------------
        // 하위없음 Set
        detail.isleaf = 'false';
        // 그룹생성인 경우 최상단에 추가
        treeView.append(detail, null, function() {
          // 정렬
          treeView.dataSource.sort(sortOpt);
        });
      }
      else {
        // --------------------------------------------------------------------
        // 선택 하위
        // --------------------------------------------------------------------
        // 하위 분류 생성 시 빈 폴더 없애기
        const target = $(treeView.select());

        if(target.length != 0){
          const isleaf = treeView.dataItem(treeView.select()).isleaf;

          if(isleaf == 'false'){
            // 하위 개수 없음
            const folderImage = target.find('.k-icon.k-no-expand');
            //folderImage.css('display','none');
            treeView.dataItem(treeView.select()).isleaf = "true";
            treeView.append(detail, treeView.select());
          } else {
            // 하위 개수 있음
            treeView.append(detail, treeView.select(), function(){
              const $target = treeView.select();

              if($target.length <= 0) return;

              treeView.dataItem($target).children.sort(sortOpt);
            });
          }
        }
          // 하위분류생성이면 선택한 카테고리 하위에 생성
          // treeView.append(detail, treeView.select());
      }
    }
    else{
      // ----------------------------------------------------------------------
      // 선택하지 않았을 경우
      // ----------------------------------------------------------------------
      // 값이 없을 경우 최상단에 생성
      treeView.append(detail, null, function() {
        treeView.dataSource.sort(sortOpt);
      });
    }
  }

  // ==========================================================================
  // # Tree 항목 수정 - row는 단건
  // ==========================================================================
  function fnTreeDataPut(row) {
      dataSource.pushUpdate(row);
  }

  // ==========================================================================
  // # Tree 항목 삭제
  // ==========================================================================
  function fnTreeDataDel(detail) {
    //console.log('# result.detail :: ' + JSON.stringify(detail));
    treeView.remove(treeView.select());
  }
  // **************************************************************************
  // -------------------------------  TREE End  -------------------------------
  // **************************************************************************



  // ==========================================================================
  // # 상세영역 초기화
  // ==========================================================================
  function fnInitDetl() {

    // 페이지 영역 초기화
    $('#sort').html("");
    $('#pageCd').html("");
    $('#pageNm').html("");
    $('#pageFullPath').html("");
    $('#useYnNm').html("");

    // 인벤토리 그리드 초기화(이 때 dpPageId 가 Set 됨)
    fnInitInventoryGrid();

    // 콘텐츠 그리드 초기화
    fnInitContsLv1Grid();
    fnInitContsLv2Grid();
    fnInitContsLv3Grid();
  }

  // ==========================================================================
  // # fnSelectPageDataSet(페이지상세선택)
  // ==========================================================================
  function fnSelectPageDataSet(inData) {

    if (inData != null) {
      //console.log('# fnSelectPageDataSet inData is not null');

      // ----------------------------------------------------------------------
      // 카테고리코너 && depth=0 인 경우 페이지영역, 인벤토리그리드 초기화
      // ----------------------------------------------------------------------
      if (pageTp == 'PAGE_TP.CATEGORY' && inData.depth == 0) {
        //alert('# pageTp :: ' + pageTp + ' # depth :: ' + inData.depth);
        // 상세영역 초기화 (페이지상세, 인벤토리그리드)
        fnInitDetl();
        // 페이지ID Set
        dpPageId = inData.dpPageId;   // 그룹인 경우에는 DP_INVENTORY_GRP_ID 값

        return false;
      }

      // ------------------------------------------------------------------------
      // 2. 인벤토리리스트조회
      // ------------------------------------------------------------------------
      selectInventoryList(inData);
    }
  }

  // ==========================================================================
  // # 인벤토리리스트조회
  // ==========================================================================
  function selectInventoryList(inData){

    if (inData.dpPageId != null && inData.dpPageId != 0) {
      //fnInitInventoryGrid(inData.dpPageId);

      // 인벤토리 선택 데이터 초기화
      selectedInventoryData = null;

      // 콘텐츠 그리드 초기화
      fnInitContsLv1Grid();
      fnInitContsLv2Grid();
      fnInitContsLv3Grid();

      // 페이지ID Set
      dpPageId = inData.dpPageId;
      // 인벤토리 그리드 초기화(이 때 dpPageId 가 Set 됨)
      fnInitInventoryGrid();
      // 조회 실행
      let data = $("#inputForm").formSerialize(true);
      inventoryGridDs.read(data);

    }
  }

  // ==========================================================================
  // # fnInitInventoryGrid(인벤토리그리드)
  // ==========================================================================
  function fnInitInventoryGrid(){
    //console.log('# 인벤토리리스트조회 Start ('+dpPageId+')[', pageSchTp, ']');

    // 선택데이터 초기화
    selectedInventoryData = null;

    var inParamDepth          = '';
    var inParamInventoryUseYn = '';
    var inParamPageNm         = '';
    var callUrl               = '';

    // 인벤토리 사용여부
    inParamInventoryUseYn = $('#inventoryUseYn').val();

    if (pageSchTp == 'PAGE_SCH_TP.INVENTORY') {
      // 페이지검색유형 = 인벤토리

      if (pageTp == 'PAGE_TP.CATEGORY') {
        //console.log('# depthInfo :: ' + JSON.stringify(selectedPageData));
        if (selectedPageData != null && selectedPageData != undefined && selectedPageData != 'undefined') {
          inParamDepth    = selectedPageData.depth;
        }
      }

      if (selectedPageData != null && selectedPageData != 'null' && selectedPageData != undefined && selectedPageData != 'undefined' && selectedPageData != '') {
        if (selectedPageData.pageNm != null && selectedPageData.pageNm != 'null' && selectedPageData.pageNm != undefined && selectedPageData.pageNm != 'undefined' && selectedPageData.pageNm != '') {
          inParamPageNm = selectedPageData.pageNm;
        }
      }

      callUrl = "/admin/display/manage/selectInventoryList?"  + "pageTp="    + pageTp
                                                              + "&dpPageId=" + dpPageId
                                                              + "&mallDiv="  + mallDivision
                                                              + "&depth="    + inParamDepth
                                                              + "&useYn="    + inParamInventoryUseYn
                                                              + "&pageNm="   + inParamPageNm
                                                              ;
      //console.log('# dpPageId :: ', dpPageId);
      //console.log('# pageNm   :: ', selectedPageData.pageNm);
    }
    else {
      // 페이지검색유형 = 그룹
      callUrl = "/admin/display/manage/selectDpGroupInventoryList?" + "dpInventoryGrpId=" + dpPageId
                                                                    + "&useYn="    + inParamInventoryUseYn;

    }
    //console.log('# fnInitInventoryGrid callUrl :: ', callUrl);

    // 페이징없는 그리드
    inventoryGridDs = fnGetDataSource({
      url      : callUrl
    });

    inventoryGridOpt = {
          dataSource  : inventoryGridDs
        , noRecordMsg : "검색된 목록이 없습니다."
        , navigatable : true
        , scrollable  : true
        , selectable  : true
        , editable    : false
        , resizable   : true
        , height      : 670
        , autobind    : false
        , columns     : [
                          { field : "sort"            , title : "순번"          , width:  "30px", attributes : {style : "text-align:center;"}, editable:function (dataItem) {return false;}}
                        , { field : "inventoryCd"     , title : "인벤토리 코드" , width: "100px", attributes : {style : "text-align:center;"}, editable:function (dataItem) {return false;}
                                                      , template :  function(result){
                                                                      // 인벤토리코드
                                                                      if (result.pageTp == 'PAGE_TP.CATEGORY') {
                                                                        // 페이지유형.카테고리코너별 : 인벤토리코드_카테고리ID(선택한 카테고리ID)
                                                                        var ilCtgryIdStr = '';

                                                                        if (selectedDpPageId != undefined && selectedDpPageId != null && selectedDpPageId != '') {
                                                                          ilCtgryIdStr = ':' + selectedDpPageId;
                                                                        }
                                                                        return result.inventoryCd + ilCtgryIdStr;
                                                                        //return result.inventoryCd + ":" + selectedPageData.dpPageId;
                                                                      }
                                                                      else {
                                                                        // 페이지유형.페이지코너별   : 인벤토리코드
                                                                        return result.inventoryCd;
                                                                      }
                                                                    }
                          }
                        , { field : "dpInventoryId"   , title : "인벤토리ID"    , width: "120px", attributes : {style : "text-align:center;"}, editable:function (dataItem) {return false;}, hidden:true}
                        , { field : "inventoryNm"     , title : "인벤토리 명"   , width: "120px", attributes : {style : "text-align:center;"}, editable:function (dataItem) {return false;}}
                        , { field : "pageNm"          , title : "페이지 명"     , width:  "70px", attributes : {style : "text-align:center;"}, editable:function (dataItem) {return false;}}
                        , { field : "pageFullPath"    , title : "페이지 경로"   , width: "100px", attributes : {style : "text-align:left;"}  , editable:function (dataItem) {return false;}}
                        , { field : "contsLevel1TpNm" , title : "Lv.1"          , width: "50px" , attributes : {style : "text-align:center;"}, editable:function (dataItem) {return false;}}
                        , { field : "contsLevel2TpNm" , title : "Lv.2"          , width: "50px" , attributes : {style : "text-align:center;"}, editable:function (dataItem) {return false;}}
                        , { field : "contsLevel3TpNm" , title : "Lv.3"          , width: "50px" , attributes : {style : "text-align:center;"}, editable:function (dataItem) {return false;}}
                        , { field : "dpRangeTpNm"     , title : "전시범위"      , width: "30px" , attributes : {style : "text-align:center;"}, editable:function (dataItem) {return false;}}
                        , { field : "useYnNm"         , title : "사용여부"      , width: "30px" , attributes : {style : "text-align:center;"}, editable:function (dataItem) {return false;}}
                        /*
                        , { field : "management"      , title : "관리"          , width: "50px" , attributes : {style : "text-align:center;"}, editable:function (dataItem) {return false;}
                                                      , template  : function(dataItem) {
                                                          return  '<div id="pageMgrButtonArea" class="btn-area textCenter">'
                                                                + '<button type="button" class="btn-point btn-s" kind="btnInventoryPreview">미리보기</button>'
                                                                + '</div>';

                                                        }
                          }
                        */
                        ]
      //, rowTemplate : kendo.template($("#rowTemplate").html())
    };

    inventoryGrid = $('#inventoryGrid').initializeKendoGrid( inventoryGridOpt ).cKendoGrid();
  }

  // ==========================================================================
  // # 콘텐츠Lv.1 그리드
  // ==========================================================================
  function fnInitContsLv1Grid(){
    //console.log('# fnInitContsLv1Grid Start');

    // 선택데이터 초기화
    selectedContsLv1Data = null;
    // 선택된 Data가 콘텐츠Lv.1 그리드이면 초기화
    if (selectedContsData != undefined && selectedContsData != null && selectedContsData != '') {
      if (selectedContsData.contsLevel == 1) {
        selectedContsData = null;
      }
    }

    // 콘텐츠그리드 초기화
    if ($("#contsLv1Grid").data("kendoGrid") != undefined ) {
      $("#contsLv1Grid").data("kendoGrid").destroy();
      $("#contsLv1Grid").empty();
    }

    // 콘텐츠타입
    var tmpContsTp = '';

    if (selectedInventoryData != null && selectedInventoryData.contsLevel1Tp != null) {
      tmpContsTp = selectedInventoryData.contsLevel1Tp;
    }
    //console.log('### Lv1 tmpContsTp :: ', tmpContsTp);

    var inParamContsLevel     = 1;
    var inParamDpInventoryId;   // selectedInventoryData.dpInventoryId
    var inParamPrntsContsId   = 0;
    var inParamDpStartDt;
    var inParamDpEndDt;
    var inParamDpRangeTp      = $('#contsLv1DpRangeTp').val();
    var inParamIlCtgryId      = '';
    var inParamStatus         = $('#contsLv1Status').val();
    var inParamContsTp        = '';


    var inParamGoodsConds     = '';

    if (selectedInventoryData != undefined && selectedInventoryData != null) {
      //console.log('# selectedInventoryData :: ' + JSON.stringify(selectedInventoryData));
      // 인벤토리ID
      if (selectedInventoryData.dpInventoryId != undefined && selectedInventoryData.dpInventoryId != null) {
        inParamDpInventoryId  = selectedInventoryData.dpInventoryId;
      }

      // 콘텐츠유형
      if (selectedInventoryData.contsLevel1Tp != null) {
        inParamContsTp = selectedInventoryData.contsLevel1Tp;
      }

      // 카테고리ID
      if (selectedInventoryData.pageTp == 'PAGE_TP.CATEGORY') {
        // 페이지유형:카테고리
        if (selectedPageData != undefined && selectedPageData != null) {
          // 선택페이지정보존재
          if (selectedPageData.dpPageId != undefined && selectedPageData.dpPageId != null) {
            inParamIlCtgryId      = selectedPageData.dpPageId;
          }
        }
      }
    }
    else {
      //console.log('# selectedInventoryData is Null');
    }

    // 검색기간 조건 - 시작
    if ($('#dpStartDtLv1').val() == null || $('#dpStartDtLv1').val() == undefined || $('#dpStartDtLv1').val() == '') {
      inParamDpStartDt = '';
    }
    else {
      inParamDpStartDt = ($('#dpStartDtLv1').val()).replace(/-/gi, '') + $('#dpStartHourPickerLv1').val() + $('#dpStartMinPickerLv1').val() + '00';
    }
    // 검색기간 조건 - 종료
    if ($('#dpEndDtLv1').val() == null || $('#dpEndDtLv1').val() == undefined || $('#dpEndDtLv1').val() == '') {
      // 년월일이 공백이면 종료일 조건 없음
      inParamDpEndDt = '';
    }
    else {
      inParamDpEndDt = ($('#dpEndDtLv1').val()).replace(/-/gi, '') + $('#dpEndHourPickerLv1').val() + $('#dpEndMinPickerLv1').val() + '59';
    }

    // 상품인경우 조건 추가
    if(tmpContsTp == 'DP_CONTENTS_TP.GOODS') {
      inParamGoodsConds = ''
                        + "&dpCondTp="        + $('#contLv1DpCondTp').val()
                        + "&dpSortTp="        + $('#contLv1DpSortTp').val();
    }

    // 페이징없는 그리드
    contsLv1GridDs = fnGetDataSource({
      url      : "/admin/display/manage/selectDpContsList?" + "dpInventoryId="    + inParamDpInventoryId
                                                            + "&prntsContsId="    + inParamPrntsContsId
                                                            + "&contsLevel="      + inParamContsLevel
                                                            + "&dpStartDt="       + inParamDpStartDt
                                                            + "&dpEndDt="         + inParamDpEndDt
                                                            + "&dpRangeTp="       + inParamDpRangeTp
                                                            + "&ilCtgryId="       + inParamIlCtgryId
                                                            + "&status="          + inParamStatus
                                                            + "&contsTp="         + inParamContsTp
                                                            + inParamGoodsConds
    });

    // ------------------------------------------------------------------------
    // 콘텐츠유형별 처리
    // ------------------------------------------------------------------------
    if(tmpContsTp == 'DP_CONTENTS_TP.HTML') {
      // ----------------------------------------------------------------------
      // 2.HTML
      // ----------------------------------------------------------------------
      contsLv1GridOptHtml = {
          dataSource  : contsLv1GridDs
        , noRecordMsg : "검색된 목록이 없습니다."
        , navigatable : true
        , scrollable  : true
        , selectable  : true
        , sortable    : true
        , editable    : "incell"
        , resizable   : true
        , columns     : [
                          {field: 'chk'        , headerTemplate : "<input type='checkbox' id='contsLv1CheckAll' />"
                                                                          , width:  40, attributes: {style: "text-align:center;" }, sortable:false, editable:function (dataItem) {return false;}
                                               , template :  function(dataItem) {
                                                               return '<input type="checkbox" name="contsLv1Check" class="couponGridChk" />'
                                                             }
                                               , locked: true, lockable: false
                          }
                        , {field: "sort"       , title: "순번"            , width:  60, attributes: {style: 'text-align:center;' }, headerAttributes: {style: "text-align: center;"}, sortable:false, locked: true, editor: sortEditor}
                        , {field: "dpContsId"  , title: "콘텐츠번호"      , width:  60, attributes: {style: 'text-align:center;' }, headerAttributes: {style: "text-align: center;"}, sortable:false, editable:function (dataItem) {return false;}, locked: true}
                        , {field: "titleNm"    , title: "타이틀명"        , width: 200, attributes: {style: 'text-align:center;' }, headerAttributes: {style: "text-align: center;"}, sortable:false, editable:function (dataItem) {return false;}, locked: true}
                        , {field: "dpStartDt"  , title: "시작일"          , width: 140, attributes: {style: 'text-align:center;' }, headerAttributes: {style: "text-align: center;"}, sortable:true, editable:function (dataItem) {return false;}}
                        , {field: "dpEndDt"    , title: "종료일"          , width: 140, attributes: {style: 'text-align:center;' }, headerAttributes: {style: "text-align: center;"}, sortable:true, editable:function (dataItem) {return false;}}
                        , {field: "dpRangeTpNm", title: "전시범위"        , width:  60, attributes: {style: 'text-align:center;' }, headerAttributes: {style: "text-align: center;"}, sortable:false, editable:function (dataItem) {return false;}}
                        , {field: "statusNm"   , title: "진행상태"        , width:  80, attributes: {style: 'text-align:center;' }, headerAttributes: {style: "text-align: center;"}, sortable:false, editable:function (dataItem) {return false;}}
                        , {field: "htmlMobile" , title: "Mobile"          , width:  90, attributes: {style: 'text-align:left;'   }, headerAttributes: {style: "text-align: center;"}, sortable:false, editable:function (dataItem) {return false;}
                        , template :  function(dataItem) {
                                        if (dataItem.htmlMobile == null || dataItem.htmlMobile == '') {
                                          return '';
                                        }
                                        else {
                                          return  '<div id="pageMgrButtonArea" class="btn-area textCenter">'
                                                + '<button type="button" class="btn-gray btn-s" kind="btnContsPreviewHtmlMobile">미리보기 </button>'
                                                + '</div>';
                                        }
                                      }
   }

                        , {field: "htmlPc"     , title: "PC"              , width:  90, attributes: {style: 'text-align:left;'   }, headerAttributes: {style: "text-align: center;"}, sortable:false, editable:function (dataItem) {return false;}
                                               , template :  function(dataItem) {
                                                               if (dataItem.htmlPc == null || dataItem.htmlPc == '') {
                                                                 return '';
                                                               }
                                                               else {
                                                                 return  '<div id="pageMgrButtonArea" class="btn-area textCenter">'
                                                                       + '<button type="button" class="btn-gray btn-s" kind="btnContsPreviewHtmlPc">미리보기 </button>'
                                                                       + '</div>';
                                                               }
                                                             }
                          }
                        , {field: "management" , title: "관리"            , width: 140, attributes: {style: "text-align:center;" }, sortable:false, editable:function (dataItem) {return false;}
                                               , template :  function(dataItem) {

                                            	   			var saveBtn = '<button type="button" class="btn-gray btn-s" kind="btnContsLv1Edit">수정 </button>'+ '&nbsp;';
                                            	   			var deleteBtn = '<button type="button" class="btn-red btn-s" kind="btnContsLv1Del">삭제 </button>';

                                            	   			var returnStr = '';

                                            	   			if(fnIsProgramAuth("SAVE")){
                                            	   				returnStr = saveBtn;
	                                                      	}
	                                                      	if(fnIsProgramAuth("DELETE")){
	                                                      		returnStr += deleteBtn;
	                                                      	}

	                                                      	return '<div id="pageMgrButtonArea" class="btn-area textCenter">' + returnStr + '</div>';

//                                                               return  '<div id="pageMgrButtonArea" class="btn-area textCenter">'
//                                                                     + '<button type="button" class="btn-gray btn-s" kind="btnContsLv1Edit">수정 </button>'
//                                                                     + '&nbsp;'
//                                                                     + '<button type="button" class="btn-red btn-s" kind="btnContsLv1Del">삭제 </button>'
//                                                                     + '</div>';
                                                             }
                                               , lockable: false
                          }
                        ]
      //, rowTemplate : kendo.template($("#rowTemplate").html())
      };

      contsLv1GridOpt = contsLv1GridOptHtml;

      // ----------------------------------------------------------------------
      // Lv.1-배너-미리보기 클릭이벤트
      // ----------------------------------------------------------------------
      // HTML-PC-Mobile
      $('#contsLv1Grid').on("click", "button[kind=btnContsPreviewHtmlMobile]", function(e) {
        e.preventDefault();
        e.stopImmediatePropagation();
        let dataItem = contsLv1Grid.dataItem($(e.currentTarget).closest("tr"));
         // 미리보기 팝업오픈
        var html    = dataItem.htmlMobile;
        fnInitPopupImagePreview(html, '', 'HTML');
      });
      // HTML-PC
      $('#contsLv1Grid').on("click", "button[kind=btnContsPreviewHtmlPc]", function(e) {
        e.preventDefault();
        e.stopImmediatePropagation();
        let dataItem = contsLv1Grid.dataItem($(e.currentTarget).closest("tr"));
        // 미리보기 팝업오픈
        var html    = dataItem.htmlPc;
        fnInitPopupImagePreview(html, '', 'HTML');
      });
    }
    else if(tmpContsTp == 'DP_CONTENTS_TP.BANNER') {
      // ----------------------------------------------------------------------
      // 3.BANNER
      // ----------------------------------------------------------------------
      contsLv1GridOptBanner = {
            dataSource  : contsLv1GridDs
          , noRecordMsg : "검색된 목록이 없습니다."
          , navigatable : true
          , scrollable  : true
          , selectable  : true
          , sortable : true
          , editable    : "incell"
          , resizable   : true
          , columns     : [
                            {field: 'chk'             , headerTemplate : "<input type='checkbox' id='contsLv1CheckAll' />"
                                                                              , width:  40, attributes: {style: "text-align:center;" }, sortable:false, editable:function (dataItem) {return false;}
                                                      , template :  function(dataItem) {
                                                                      return '<input type="checkbox" name="contsLv1Check" class="couponGridChk" />'
                                                                    }
                                                      , locked: true, lockable: false
                            }
                          , {field: "sort"            , title: "순번"         , width:  60, attributes: {style: 'text-align:center;' }, headerAttributes: {style: "text-align: center;"}, sortable:false, locked: true, editor: sortEditor}
                          , {field: "dpContsId"       , title: "콘텐츠번호"   , width:  60, attributes: {style: 'text-align:center;' }, headerAttributes: {style: "text-align: center;"}, sortable:false, editable:function (dataItem) {return false;}, locked: true}
                          , {field: "titleNm"         , title: "타이틀명"     , width: 200, attributes: {style: 'text-align:center;' }, headerAttributes: {style: "text-align: center;"}, sortable:false, editable:function (dataItem) {return false;}, locked: true}
                          , {field: "dpStartDt"       , title: "시작일"       , width: 140, attributes: {style: 'text-align:center;' }, headerAttributes: {style: "text-align: center;"}, sortable:true, editable:function (dataItem) {return false;}}
                          , {field: "dpEndDt"         , title: "종료일"       , width: 140, attributes: {style: 'text-align:center;' }, headerAttributes: {style: "text-align: center;"}, sortable:true, editable:function (dataItem) {return false;}}
                          , {field: "dpRangeTpNm"     , title: "전시범위"     , width:  60, attributes: {style: 'text-align:center;' }, headerAttributes: {style: "text-align: center;"}, sortable:false, editable:function (dataItem) {return false;}}
                          , {field: "statusNm"        , title: "진행상태"     , width:  80, attributes: {style: 'text-align:center;' }, headerAttributes: {style: "text-align: center;"}, sortable:false, editable:function (dataItem) {return false;}}
                          , {field: "management"      , title: "관리"         , width: 140, attributes: {style: "text-align:center;" }, sortable:false, editable:function (dataItem) {return false;}
                              , template :  function(dataItem) {

                                  var saveBtn = '<button type="button" class="btn-gray btn-s" kind="btnContsLv1Edit">수정 </button>' + '&nbsp;';
                                  var deleteBtn = '<button type="button" class="btn-red btn-s" kind="btnContsLv1Del">삭제 </button>';

                                  var returnStr = '';

                                  if(fnIsProgramAuth("SAVE")){
                                    returnStr = saveBtn;
                                  }
                                  if(fnIsProgramAuth("DELETE")){
                                    returnStr += deleteBtn;
                                  }

                                  return '<div id="pageMgrButtonArea" class="btn-area textCenter">' + returnStr + '</div>';
                //                return  '<div id="pageMgrButtonArea" class="btn-area textCenter">'
                //                + '<button type="button" class="btn-gray btn-s" kind="btnContsLv1Edit">수정 </button>'
                //                                                                            + '&nbsp;'
                //                                                                            + '<button type="button" class="btn-red btn-s" kind="btnContsLv1Del">삭제 </button>'
                //                                                                            + '</div>';
                            }
                            , lockable: false
                          }
                          , {field: "imgPathMobile"   , title: "이미지1"      , width:  90, attributes: {style: 'text-align:left;'   }, headerAttributes: {style: "text-align: center;"}, sortable:false, editable:function (dataItem) {return false;}
                                                      , template :  function(dataItem) {
                                                                      if (dataItem.imgPathMobile == null || dataItem.imgPathMobile == '') {
                                                                        return '';
                                                                      }
                                                                      else {
                                                                        return  '<div id="pageMgrButtonArea" class="btn-area textCenter">'
                                                                              + '<button type="button" class="btn-gray btn-s" kind="btnContsPreviewImgMobile">미리보기 </button>'
                                                                              + '</div>';
                                                                      }
                                                                    }
                            }
                          , {field: "imgPathPc"       , title: "이미지2"      , width:  90, attributes: {style: 'text-align:left;'   }, headerAttributes: {style: "text-align: center;"}, sortable:false, editable:function (dataItem) {return false;}
                                                      , template :  function(dataItem) {
                                                                      if (dataItem.imgPathPc == null || dataItem.imgPathPc == '') {
                                                                        return '';
                                                                      }
                                                                      else {
                                                                        return  '<div id="pageMgrButtonArea" class="btn-area textCenter">'
                                                                              + '<button type="button" class="btn-gray btn-s" kind="btnContsPreviewImgPc">미리보기 </button>'
                                                                              + '</div>';
                                                                      }
                                                                    }
                            }
                          , {field: "gifImgPathMobile", title: "Mobile gif"   , width:  90, attributes: {style: 'text-align:left;'   }, headerAttributes: {style: "text-align: center;"}, sortable:false, editable:function (dataItem) {return false;}
                                                      , template :  function(dataItem) {
                                                                      if (dataItem.gifImgPathMobile == null || dataItem.gifImgPathMobile == '') {
                                                                        return '';
                                                                      }
                                                                      else {
                                                                        return  '<div id="pageMgrButtonArea" class="btn-area textCenter">'
                                                                              + '<button type="button" class="btn-gray btn-s" kind="btnContsPreviewImgGifMobile">미리보기 </button>'
                                                                              + '</div>';
                                                                      }
                                                                    }
                            }
                          , {field: "gifImgPathPc"    , title: "PC gif"       , width:  90, attributes: {style: 'text-align:left;'   }, headerAttributes: {style: "text-align: center;"}, sortable:false, editable:function (dataItem) {return false;}
                                                      , template :  function(dataItem) {
                                                                      if (dataItem.gifImgPathPc == null || dataItem.gifImgPathPc == '') {
                                                                        return '';
                                                                      }
                                                                      else {
                                                                        return  '<div id="pageMgrButtonArea" class="btn-area textCenter">'
                                                                              + '<button type="button" class="btn-gray btn-s" kind="btnContsPreviewImgGifPc">미리보기 </button>'
                                                                              + '</div>';
                                                                      }
                                                                    }
                            }
                          , {field: "text1"           , title: "노출텍스트1"  , width: 150, attributes: {style: 'text-align:left;'   }, headerAttributes: {style: "text-align: center;"}, sortable:false, editable:function (dataItem) {return false;}
                                                      , template :  function(dataItem) {

                                                                      var textVal;

                                                                      if (dataItem.text1 == null || dataItem.text1 == '') {
                                                                        textVal = '';
                                                                      }
                                                                      else {
                                                                        textVal = '['
                                                                                + dataItem.text1Color
                                                                                + '] '
                                                                                + dataItem.text1;
                                                                      }
                                                                      return  textVal;
                                                                    }
                            }
                          , {field: "text2"           , title: "노출텍스트2"  , width: 150, attributes: {style: 'text-align:left;'   }, headerAttributes: {style: "text-align: center;"}, sortable:false, editable:function (dataItem) {return false;}
                                                      , template :  function(dataItem) {

                                                                      var textVal;

                                                                      if (dataItem.text2 == null || dataItem.text2 == '') {
                                                                        textVal = '';
                                                                      }
                                                                      else {
                                                                        textVal = '['
                                                                                + dataItem.text2Color
                                                                                + '] '
                                                                                + dataItem.text2;
                                                                      }
                                                                      return  textVal;
                                                                    }
                            }
                          , {field: "text3"           , title: "노출텍스트3"  , width: 150, attributes: {style: 'text-align:left;'   }, headerAttributes: {style: "text-align: center;"}, sortable:false, editable:function (dataItem) {return false;}
                                                      , template :  function(dataItem) {
                                                                      var textVal;

                                                                      if (dataItem.text3 == null || dataItem.text3 == '') {
                                                                        textVal = '';
                                                                      }
                                                                      else {
                                                                        textVal = '['
                                                                                + dataItem.text3Color
                                                                                + '] '
                                                                                + dataItem.text3;
                                                                      }
                                                                      return  textVal;
                                                                    }
                            }
                          ]
        //, rowTemplate : kendo.template($("#rowTemplate").html())
        };

      contsLv1GridOpt = contsLv1GridOptBanner;

      // ----------------------------------------------------------------------
      // Lv.1-배너-미리보기 클릭이벤트
      // ----------------------------------------------------------------------
      // 이미지1
      $('#contsLv1Grid').on("click", "button[kind=btnContsPreviewImgMobile]", function(e) {
        e.preventDefault();
        e.stopImmediatePropagation();
        let dataItem = contsLv1Grid.dataItem($(e.currentTarget).closest("tr"));
        //selectedContsLv1Data = dataItem;
        //console.log('# Lv1. dataItem :: ' + JSON.stringify(dataItem));
        //alert('# Lv.1 이미지1 Click');
        // 미리보기 팝업오픈
        var path    = dataItem.imgPathMobile;
        var fileNm  = dataItem.imgOriginNmMobile;
        fnInitPopupImagePreview(path, fileNm, 'IMAGE');
      });
      // gif이미지모바일
      $('#contsLv1Grid').on("click", "button[kind=btnContsPreviewImgGifMobile]", function(e) {
        e.preventDefault();
        e.stopImmediatePropagation();
        let dataItem = contsLv1Grid.dataItem($(e.currentTarget).closest("tr"));
        //selectedContsLv1Data = dataItem;
        //console.log('# Lv1. dataItem :: ' + JSON.stringify(dataItem));
        //alert('# Lv.1 이미지1 Click');
        // 미리보기 팝업오픈
        var path    = dataItem.gifImgPathMobile;
        var fileNm  = dataItem.gifImgOrginNmMobile;
        fnInitPopupImagePreview(path, fileNm, 'IMAGE');
      });
      // 이미지2
      $('#contsLv1Grid').on("click", "button[kind=btnContsPreviewImgPc]", function(e) {
        e.preventDefault();
        e.stopImmediatePropagation();
        let dataItem = contsLv1Grid.dataItem($(e.currentTarget).closest("tr"));
        //selectedContsLv1Data = dataItem;
        //console.log('# Lv1. dataItem :: ' + JSON.stringify(dataItem));
        //alert('# Lv.1 이미지2 Click');
        // 미리보기 팝업오픈
        var path    = dataItem.imgPathPc;
        var fileNm  = dataItem.imgOriginNmPc;
        fnInitPopupImagePreview(path, fileNm, 'IMAGE');
      });
      // gif이미지PC
      $('#contsLv1Grid').on("click", "button[kind=btnContsPreviewImgGifPc]", function(e) {
        e.preventDefault();
        e.stopImmediatePropagation();
        let dataItem = contsLv1Grid.dataItem($(e.currentTarget).closest("tr"));
        //selectedContsLv1Data = dataItem;
        //console.log('# Lv1. dataItem :: ' + JSON.stringify(dataItem));
        //alert('# Lv.1 이미지1 Click');
        // 미리보기 팝업오픈
        var path    = dataItem.gifImgPathPc;
        var fileNm  = dataItem.gifImgOrginNmPc;
        fnInitPopupImagePreview(path, fileNm, 'IMAGE');
      });
    }
    else if(tmpContsTp == 'DP_CONTENTS_TP.BRAND') {
      // ----------------------------------------------------------------------
      // 4.BRAND
      // ----------------------------------------------------------------------
      contsLv1GridOptBrand = {
          dataSource  : contsLv1GridDs
        , noRecordMsg : "검색된 목록이 없습니다."
        , navigatable : true
        , scrollable  : true
        , selectable  : true
        , sortable    : true
        , editable    : "incell"
        , resizable   : true
        , columns     : [
                          {field: 'chk'           , headerTemplate : "<input type='checkbox' id='contsLv1CheckAll' />"
                                                                           , width:  40, attributes: {style: "text-align:center;" }, sortable : false, editable:function (dataItem) {return false;}
                                                  , template :  function(dataItem) {
                                                                  return '<input type="checkbox" name="contsLv1Check" class="couponGridChk" />'
                                                                }
                                                  , locked: true, lockable: false
                          }
                        , {field: "sort"          , title: "순번"          , width:  60, attributes: {style: 'text-align:center;' }, headerAttributes: {style: "text-align: center;"}, sortable : false, locked: true, editor: sortEditor}
                        , {field: "dpContsId"     , title: "콘텐츠번호"    , width:  60, attributes: {style: 'text-align:center;' }, headerAttributes: {style: "text-align: center;"}, sortable : false, editable:function (dataItem) {return false;}, locked: true}
                        , {field: "titleNm"       , title: "타이틀명"      , width: 200, attributes: {style: 'text-align:center;' }, headerAttributes: {style: "text-align: center;"}, sortable : false, editable:function (dataItem) {return false;}, locked: true}
                        , {field: "contsNm"       , title: "전시브랜드명"      , width: 200, attributes: {style: 'text-align:center;' }, headerAttributes: {style: "text-align: center;"}, sortable : false, editable:function (dataItem) {return false;}}
                        , {field: "dpStartDt"     , title: "시작일"        , width: 140, attributes: {style: 'text-align:center;' }, headerAttributes: {style: "text-align: center;"}, sortable : true, editable:function (dataItem) {return false;}}
                        , {field: "dpEndDt"       , title: "종료일"        , width: 140, attributes: {style: 'text-align:center;' }, headerAttributes: {style: "text-align: center;"}, sortable : true, editable:function (dataItem) {return false;}}
                        , {field: "dpRangeTpNm"   , title: "전시범위"      , width:  60, attributes: {style: 'text-align:center;' }, headerAttributes: {style: "text-align: center;"}, sortable : false, editable:function (dataItem) {return false;}}
                        , {field: "statusNm"      , title: "진행상태"      , width:  80, attributes: {style: 'text-align:center;' }, headerAttributes: {style: "text-align: center;"}, sortable : false, editable:function (dataItem) {return false;}}
                        , {field: "management"    , title: "관리"            , width: 140, attributes: {style: "text-align:center;" }, sortable : false, editable:function (dataItem) {return false;}
                                                  , template :  function(dataItem) {
                                                              var saveBtn = '<button type="button" class="btn-gray btn-s" kind="btnContsLv1Edit">수정 </button>' + '&nbsp;';
                                                              var deleteBtn = '<button type="button" class="btn-red btn-s" kind="btnContsLv1Del">삭제 </button>';

                                                              var returnStr = '';

                                                              if(fnIsProgramAuth("SAVE")){
                                                                returnStr = saveBtn;
                                                              }
                                                              if(fnIsProgramAuth("DELETE")){
                                                                returnStr += deleteBtn;
                                                              }

                                                              return '<div id="pageMgrButtonArea" class="btn-area textCenter">' + returnStr + '</div>';
                                                  }
                                                  , lockable: false
                        }
                        , {field: "imgPathMobile" , title: "이미지1"       , width:  90, attributes: {style: 'text-align:left;'   }, headerAttributes: {style: "text-align: center;"}, sortable : false, editable:function (dataItem) {return false;}
                                                  , template :  function(dataItem) {

                                                                  if (dataItem.imgPathMobile == null || dataItem.imgPathMobile == '') {
                                                                    return '';
                                                                  }
                                                                  else {
                                                                    return  '<div id="pageMgrButtonArea" class="btn-area textCenter">'
                                                                          + '<button type="button" class="btn-gray btn-s" kind="btnContsPreviewImgMobile">미리보기 </button>'
                                                                          + '</div>';
                                                                  }
                                                                }
                          }
                        , {field: "imgPathPc"     , title: "이미지2"       , width:  90, attributes: {style: 'text-align:left;'   }, headerAttributes: {style: "text-align: center;"}, sortable : false, editable:function (dataItem) {return false;}
                                                  , template :  function(dataItem) {
                                                                  if (dataItem.imgPathPc == null || dataItem.imgPathPc == '') {
                                                                    return '';
                                                                  }
                                                                  else {
                                                                    return  '<div id="pageMgrButtonArea" class="btn-area textCenter">'
                                                                          + '<button type="button" class="btn-gray btn-s" kind="btnContsPreviewImgPc">미리보기 </button>'
                                                                          + '</div>';
                                                                  }
                                                                }
                          }

                        , {field: "text1"         , title: "노출텍스트1"   , width: 150, attributes: {style: 'text-align:left;'   }, headerAttributes: {style: "text-align: center;"}, sortable : false, editable:function (dataItem) {return false;}
                                                  , template : function(dataItem) {

                                                                 var textVal;

                                                                 if (dataItem.text1 == null || dataItem.text1 == '') {
                                                                   textVal = '';
                                                                 }
                                                                 else {
                                                                   textVal = '['
                                                                           + dataItem.text1Color
                                                                           + '] '
                                                                           + dataItem.text1;
                                                                 }
                                                                 return  textVal;
                                                               }
                          }
                        , {field: "text2"         , title: "노출텍스트2"   , width: 150, attributes: {style: 'text-align:left;'   }, headerAttributes: {style: "text-align: center;"}, sortable : false, editable:function (dataItem) {return false;}
                                                  , template : function(dataItem) {

                                                                 var textVal;

                                                                 if (dataItem.text2 == null || dataItem.text2 == '') {
                                                                   textVal = '';
                                                                 }
                                                                 else {
                                                                   textVal = '['
                                                                           + dataItem.text2Color
                                                                           + '] '
                                                                           + dataItem.text2;
                                                                 }
                                                                 return  textVal;
                                                               }
                          }
                        , {field: "text3"         , title: "노출텍스트3"   , width: 150, attributes: {style: 'text-align:left;'   }, headerAttributes: {style: "text-align: center;"}, sortable : false, editable:function (dataItem) {return false;}
                                                  , template : function(dataItem) {
                                                                 var textVal;

                                                                 if (dataItem.text3 == null || dataItem.text3 == '') {
                                                                   textVal = '';
                                                                 }
                                                                 else {
                                                                   textVal = '['
                                                                           + dataItem.text3Color
                                                                           + '] '
                                                                           + dataItem.text3;
                                                                 }
                                                                 return  textVal;
                                                               }
                          }
                        ]
      //, rowTemplate : kendo.template($("#rowTemplate").html())
      };

      contsLv1GridOpt = contsLv1GridOptBrand;

      // ----------------------------------------------------------------------
      // Lv.1-브랜드-미리보기 클릭이벤트
      // ----------------------------------------------------------------------
      // 이미지1
      $('#contsLv1Grid').on("click", "button[kind=btnContsPreviewImgMobile]", function(e) {
        e.preventDefault();
        e.stopImmediatePropagation();
        let dataItem = contsLv1Grid.dataItem($(e.currentTarget).closest("tr"));
        //selectedContsLv1Data = dataItem;
        //console.log('# Lv1. dataItem :: ' + JSON.stringify(dataItem));
        //alert('# Lv.1 이미지1 Click');
        // 미리보기 팝업오픈
        var path    = dataItem.imgPathMobile;
        var fileNm  = dataItem.imgOriginNmMobile;
        fnInitPopupImagePreview(path, fileNm, 'IMAGE');
      });
      // 이미지2
      $('#contsLv1Grid').on("click", "button[kind=btnContsPreviewImgPc]", function(e) {
        e.preventDefault();
        e.stopImmediatePropagation();
        let dataItem = contsLv1Grid.dataItem($(e.currentTarget).closest("tr"));
        //selectedContsLv1Data = dataItem;
        //console.log('# Lv1. dataItem :: ' + JSON.stringify(dataItem));
        //alert('# Lv.1 이미지2 Click');
        // 미리보기 팝업오픈
        var path    = dataItem.imgPathPc;
        var fileNm  = dataItem.imgOriginNmPc;
        fnInitPopupImagePreview(path, fileNm, 'IMAGE');
      });


    }
    else if(tmpContsTp == 'DP_CONTENTS_TP.CATEGORY') {
      // ----------------------------------------------------------------------
      // 5.CATEGORY
      // ----------------------------------------------------------------------
      contsLv1GridOptCategory = {
          dataSource  : contsLv1GridDs
        , noRecordMsg : "검색된 목록이 없습니다."
        , navigatable : true
        , scrollable  : true
        , selectable  : true
        , sortable    : true
        , editable    : "incell"
        , resizable   : true
        , columns     : [
                          {field: 'chk'        , headerTemplate : "<input type='checkbox' id='contsLv1CheckAll' />"
                                                                        , width:  40, attributes: {style: "text-align:center;" }, sortable : false, editable:function (dataItem) {return false;}
                                               , template :  function(dataItem) {
                                                               return '<input type="checkbox" name="contsLv1Check" class="couponGridChk" />'
                                                             }
                                               , locked: true, lockable: false
                          }
                        , {field: "sort"       , title: "순번"          , width:  60, attributes: {style: 'text-align:center;' }, headerAttributes: {style: "text-align: center;"}, sortable : false, locked: true, editor: sortEditor}
                        , {field: "dpContsId"  , title: "콘텐츠번호"    , width:  60, attributes: {style: 'text-align:center;' }, headerAttributes: {style: "text-align: center;"}, sortable : false, editable:function (dataItem) {return false;}, locked: true}
                        , {field: "titleNm"    , title: "타이틀명"      , width: 200, attributes: {style: 'text-align:center;' }, headerAttributes: {style: "text-align: center;"}, sortable : false, editable:function (dataItem) {return false;}, locked: true}
                        , {field: "dpStartDt"  , title: "시작일"        , width: 140, attributes: {style: 'text-align:center;' }, headerAttributes: {style: "text-align: center;"}, sortable : true, editable:function (dataItem) {return false;}}
                        , {field: "dpEndDt"    , title: "종료일"        , width: 140, attributes: {style: 'text-align:center;' }, headerAttributes: {style: "text-align: center;"}, sortable : true, editable:function (dataItem) {return false;}}
                        , {field: "dpRangeTpNm", title: "전시범위"      , width:  60, attributes: {style: 'text-align:center;' }, headerAttributes: {style: "text-align: center;"}, sortable : false, editable:function (dataItem) {return false;}}
                        , {field: "statusNm"   , title: "진행상태"      , width:  80, attributes: {style: 'text-align:center;' }, headerAttributes: {style: "text-align: center;"}, sortable : false, editable:function (dataItem) {return false;}}
                        , {field: "management" , title: "관리"            , width: 140, attributes: {style: "text-align:center;" }, sortable : false, editable:function (dataItem) {return false;}
                                                , template :  function(dataItem) {

                                                              var saveBtn = '<button type="button" class="btn-gray btn-s" kind="btnContsLv1Edit">수정 </button>'+ '&nbsp;';
                                                              var deleteBtn = '<button type="button" class="btn-red btn-s" kind="btnContsLv1Del">삭제 </button>';

                                                              var returnStr = '';

                                                              if(fnIsProgramAuth("SAVE")){
                                                                returnStr = saveBtn;
                                                              }
                                                              if(fnIsProgramAuth("DELETE")){
                                                                returnStr += deleteBtn;
                                                              }

                                                              return '<div id="pageMgrButtonArea" class="btn-area textCenter">' + returnStr + '</div>';
                                                            }
                                                , lockable: false
                        }
                        , {field: "mallDivNm"  , title: "몰인몰구분"    , width: 100, attributes: {style: 'text-align:center;' }, headerAttributes: {style: "text-align: center;"}, sortable : false, editable:function (dataItem) {return false;}}
                        , {field: "ctgryFullNm", title: "전시카테고리"  , width: 100, attributes: {style: 'text-align:center;' }, headerAttributes: {style: "text-align: center;"}, sortable : false, editable:function (dataItem) {return false;}}
                        , {field: "text1"      , title: "노출텍스트1"   , width: 150, attributes: {style: 'text-align:left;'   }, headerAttributes: {style: "text-align: center;"}, sortable : false, editable:function (dataItem) {return false;}
                                               , template : function(dataItem) {

                                                              var textVal;

                                                              if (dataItem.text1 == null || dataItem.text1 == '') {
                                                                textVal = '';
                                                              }
                                                              else {
                                                                textVal = '['
                                                                        + dataItem.text1Color
                                                                        + '] '
                                                                        + dataItem.text1;
                                                              }
                                                              return  textVal;
                                                            }
                          }
                        , {field: "text2"      , title: "노출텍스트2"   , width: 150, attributes: {style: 'text-align:left;'   }, headerAttributes: {style: "text-align: center;"}, sortable : false, editable:function (dataItem) {return false;}
                                               , template : function(dataItem) {

                                                              var textVal;

                                                              if (dataItem.text2 == null || dataItem.text2 == '') {
                                                                textVal = '';
                                                              }
                                                              else {
                                                                textVal = '['
                                                                        + dataItem.text2Color
                                                                        + '] '
                                                                        + dataItem.text2;
                                                              }
                                                              return  textVal;
                                                            }
                          }
                        , {field: "text3"      , title: "노출텍스트3"   , width: 150, attributes: {style: 'text-align:left;'   }, headerAttributes: {style: "text-align: center;"}, sortable : false, editable:function (dataItem) {return false;}
                                               , template : function(dataItem) {
                                                              var textVal;

                                                              if (dataItem.text3 == null || dataItem.text3 == '') {
                                                                textVal = '';
                                                              }
                                                              else {
                                                                textVal = '['
                                                                        + dataItem.text3Color
                                                                        + '] '
                                                                        + dataItem.text3;
                                                              }
                                                              return  textVal;
                                                            }
                          }
                        ]
      //, rowTemplate : kendo.template($("#rowTemplate").html())
      };

      contsLv1GridOpt = contsLv1GridOptCategory;
    }
    else if(tmpContsTp == 'DP_CONTENTS_TP.GOODS') {
      // ----------------------------------------------------------------------
      // GOODS
      // ----------------------------------------------------------------------
      contsLv1GridOptGoods = {
          dataSource  : contsLv1GridDs
        , noRecordMsg : "검색된 목록이 없습니다."
        , navigatable : true
        , scrollable  : true
        , selectable  : true
        , sortable    : true
        , editable    : "incell"
        , resizable   : true
        , columns     : [
                          {field: 'chk'                  , headerTemplate : "<input type='checkbox' id='contsLv1CheckAll' />"
                                                                                 , width:  40, attributes: {style: "text-align:center;" }, sortable : false, editable:function (dataItem) {return false;}
                                                         , template :  function(dataItem) {
                                                                         return '<input type="checkbox" name="contsLv1Check" class="couponGridChk" />'
                                                                       }
                                                         , locked: true, lockable: false
                          }
                        , {field: "sort"                 , title: "순번"         , width:  60, attributes: {style: 'text-align:center;' }, headerAttributes: {style: "text-align: center;"}, sortable : false, locked: true, editor: sortEditor}
                        , {field: "dpContsId"            , title: "콘텐츠번호"   , width:  60, attributes: {style: 'text-align:center;' }, headerAttributes: {style: "text-align: center;"}, sortable : false, editable:function (dataItem) {return false;}, locked: true}
                        , {field: "titleNm"              , title: "타이틀명"     , width: 200, attributes: {style: 'text-align:center;' }, headerAttributes: {style: "text-align: center;"}, sortable : false, editable:function (dataItem) {return false;}, locked: true}
                        , {field: "contsId"              , title: "상품코드"     , width:  60, attributes: {style: 'text-align:center;' }, headerAttributes: {style: "text-align: center;"}, sortable : false, editable:function (dataItem) {return false;}}
                        , {field: "contsNm"              , title: "상품명"       , width: 200, attributes: {style: 'text-align:center;' }, headerAttributes: {style: "text-align: center;"}, sortable : false, editable:function (dataItem) {return false;}}
                        , {field: "dpStartDt"            , title: "시작일"       , width: 140, attributes: {style: 'text-align:center;' }, headerAttributes: {style: "text-align: center;"}, sortable : true, editable:function (dataItem) {return false;}}
                        , {field: "dpEndDt"              , title: "종료일"       , width: 140, attributes: {style: 'text-align:center;' }, headerAttributes: {style: "text-align: center;"}, sortable : true, editable:function (dataItem) {return false;}}
                        , {field: "dpRangeTpNm"          , title: "전시범위"     , width:  60, attributes: {style: 'text-align:center;' }, headerAttributes: {style: "text-align: center;"}, sortable : false, editable:function (dataItem) {return false;}}
                        , {field: "statusNm"             , title: "진행상태"     , width:  80, attributes: {style: 'text-align:center;' }, headerAttributes: {style: "text-align: center;"}, sortable : false, editable:function (dataItem) {return false;}}
                        , {field: "management"           , title: "관리"         , width: 140, attributes: {style: "text-align:center;" }, sortable : false, editable:function (dataItem) {return false;}
                                                        , template :  function(dataItem) {
                                                                  if (dataItem.dpCondTp == 'DP_COND_TP.MANUAL') {
                                                                    // 노출조건이 직접등록 => 수정/삭제 버튼 노출

                                                                    var saveBtn = '<button type="button" class="btn-gray btn-s" kind="btnContsLv1Edit">수정 </button>'+ '&nbsp;';
                                                                    var deleteBtn = '<button type="button" class="btn-red btn-s" kind="btnContsLv1Del">삭제 </button>';

                                                                    var returnStr = '';

                                                                    if(fnIsProgramAuth("SAVE")){
                                                                      returnStr = saveBtn;
                                                                    }
                                                                    if(fnIsProgramAuth("DELETE")){
                                                                      returnStr += deleteBtn;
                                                                    }

                                                                    return '<div id="pageMgrButtonArea" class="btn-area textCenter">' + returnStr + '</div>';
                                                                  }
                                                                  else {
                                                                    // 노출조건이 직접등로 이외 => 수정 버튼 숨김

                                                                    var returnStr = '';
                                                                    if(fnIsProgramAuth("DELETE")){
                                                                      returnStr = '<button type="button" class="btn-red btn-s" kind="btnContsLv1Del">삭제 </button>';
                                                                    }

                                                                    return '<div id="pageMgrButtonArea" class="btn-area textCenter">' + returnStr + '</div>';
                                                                  }
                                                        }
                                                        , lockable: false
                        }
                        , {field: "dpCondTpNm"           , title: "노출조건"     , width:  60, attributes: {style: 'text-align:center;' }, headerAttributes: {style: "text-align: center;"}, sortable : false, editable:function (dataItem) {return false;}}
                        , {field: "dpSortTpNm"           , title: "노출순서"     , width:  60, attributes: {style: 'text-align:center;' }, headerAttributes: {style: "text-align: center;"}, sortable : false, editable:function (dataItem) {return false;}}
                        , {field: "goodsTypeName"        , title: "상품유형"     , width:  60, attributes: {style: 'text-align:center;' }, headerAttributes: {style: "text-align: center;"}, sortable : false, editable:function (dataItem) {return false;}}
                        , {field: "supplierName"         , title: "공급업체"     , width: 100, attributes: {style: 'text-align:center;' }, headerAttributes: {style: "text-align: center;"}, sortable : false, editable:function (dataItem) {return false;}}
                        , {field: "goodsBrandNm"         , title: "브랜드"       , width: 120, attributes: {style: 'text-align:center;' }, headerAttributes: {style: "text-align: center;"}, sortable : false, editable:function (dataItem) {return false;}}
                        , {field: "recommendedPrice"     , title: "정상가"       , width:  70, attributes: {style: 'text-align:center;' }, headerAttributes: {style: "text-align: center;"}, sortable : false, editable:function (dataItem) {return false;}
                                                                                 , format: "{0:\#\#,\#}"
                                                                                 //, template : function(dataItem) {
                                                                                 //               return (dataItem.recommendedPrice).replace(/(\d)(?=(?:\d{3})+(?!\d))/g, '$1,');
                                                                                 //             }
                          }
                        , {field: "salePrice"            , title: "판매가"       , width:  70, attributes: {style: 'text-align:center;' }, headerAttributes: {style: "text-align: center;"}, sortable : false, editable:function (dataItem) {return false;}
                                                                                 , format: "{0:\#\#,\#}"
                                                                                 //, template : function(dataItem) {
                                                                                 //               return (dataItem.salePrice).replace(/(\d)(?=(?:\d{3})+(?!\d))/g, '$1,');
                                                                                 //             }
                          }
                        , {field: "dispRangeNm"          , title: "판매허용범위" , width:  80, attributes: {style: 'text-align:center;' }, headerAttributes: {style: "text-align: center;"}, sortable : false, editable:function (dataItem) {return false;}
                                                                                 , template  : function(dataItem) {

                                                                                                 var dispYnRangeVal = '';
                                                                                                 var yesCnt = 0;
                                                                                                 var sepStr = '';

                                                                                                 // PC
                                                                                                 if (dataItem.dispWebPcYn == 'Y') {
                                                                                                   dispYnRangeVal = dataItem.dispWebPcYnNm;
                                                                                                   yesCnt++;
                                                                                                 }
                                                                                                 // Mobile
                                                                                                 if (dataItem.dispWebMobileYn == 'Y') {

                                                                                                   if (yesCnt > 0) {
                                                                                                     sepStr = '/';
                                                                                                   }

                                                                                                   dispYnRangeVal += (sepStr + dataItem.dispWebMobileYnNm);
                                                                                                   yesCnt++;
                                                                                                 }
                                                                                                 // APP
                                                                                                 if (dataItem.dispAppYn == 'Y') {

                                                                                                   if (yesCnt > 0) {
                                                                                                     sepStr = '/';
                                                                                                   }

                                                                                                   dispYnRangeVal += (sepStr + dataItem.dispAppYnNm);
                                                                                                 }
                                                                                                 return  dispYnRangeVal
                                                                                               }
                                                        }
                        , {field: "purchaseRangeNm"      , title: "구매허용범위" , width: 120, attributes: {style: 'text-align:center;' }, headerAttributes: {style: "text-align: center;"}, sortable : false, editable:function (dataItem) {return false;}
                                                                                 , template  : function(dataItem) {

                                                                                                 var purchaseYnRangeVal = '';
                                                                                                 var yesCnt = 0;
                                                                                                 var sepStr = '';

                                                                                                 // 일반회원
                                                                                                 if (dataItem.purchaseMemberYn == 'Y') {
                                                                                                   purchaseYnRangeVal = dataItem.purchaseMemberYnNm;
                                                                                                   yesCnt++;
                                                                                                 }
                                                                                                 // 임직원
                                                                                                 if (dataItem.purchaseEmployeeYn == 'Y') {

                                                                                                   if (yesCnt > 0) {
                                                                                                     sepStr = '/';
                                                                                                   }

                                                                                                   purchaseYnRangeVal += (sepStr + dataItem.purchaseEmployeeYnNm);
                                                                                                   yesCnt++;
                                                                                                 }
                                                                                                 // 비회원
                                                                                                 if (dataItem.purchaseNonmemberYn == 'Y') {

                                                                                                   if (yesCnt > 0) {
                                                                                                     sepStr = '/';
                                                                                                   }

                                                                                                   purchaseYnRangeVal += (sepStr + dataItem.purchaseNonmemberYnNm);
                                                                                                 }
                                                                                                 return  purchaseYnRangeVal
                                                                                               }
                          }
                        , {field: "goodsBasicCtgryFullNm", title: "전시카테고리" , width: 300, attributes: {style: 'text-align:center;' }, headerAttributes: {style: "text-align: center;"}, sortable : false, editable:function (dataItem) {return false;}}
                        , {field: "saleStatusName"       , title: "판매상태"     , width:  80, attributes: {style: 'text-align:center;' }, headerAttributes: {style: "text-align: center;"}, sortable : false, editable:function (dataItem) {return false;}}
                        , {field: "dispYnNm"             , title: "전시여부"     , width:  60, attributes: {style: 'text-align:center;' }, headerAttributes: {style: "text-align: center;"}, sortable : false, editable:function (dataItem) {return false;}}
                        , {field: "linkUrlPc"            , title: "링크URL"      , width: 420, attributes: {style: 'text-align:left;'   }, headerAttributes: {style: "text-align: center;"}, sortable : false, editable:function (dataItem) {return false;}}
                        , {field: "text1"                , title: "노출텍스트1"  , width: 150, attributes: {style: 'text-align:left;'   }, headerAttributes: {style: "text-align: center;"}, sortable : false, editable:function (dataItem) {return false;}
                                                         , template : function(dataItem) {

                                                                        var textVal;

                                                                        if (dataItem.text1 == null || dataItem.text1 == '') {
                                                                          textVal = '';
                                                                        }
                                                                        else {
                                                                          textVal = '['
                                                                                  + dataItem.text1Color
                                                                                  + '] '
                                                                                  + dataItem.text1;
                                                                        }
                                                                        return  textVal;
                                                                      }
                          }
                        , {field: "text2"                , title: "노출텍스트2"  , width: 150, attributes: {style: 'text-align:left;'   }, headerAttributes: {style: "text-align: center;"}, sortable : false, editable:function (dataItem) {return false;}
                                                         , template : function(dataItem) {

                                                                        var textVal;

                                                                        if (dataItem.text2 == null || dataItem.text2 == '') {
                                                                          textVal = '';
                                                                        }
                                                                        else {
                                                                          textVal = '['
                                                                                  + dataItem.text2Color
                                                                                  + '] '
                                                                                  + dataItem.text2;
                                                                        }
                                                                        return  textVal;
                                                                      }
                          }
                        , {field: "text3"                , title: "노출텍스트3"  , width: 150, attributes: {style: 'text-align:left;'   }, headerAttributes: {style: "text-align: center;"}, sortable : false, editable:function (dataItem) {return false;}
                                                         , template : function(dataItem) {
                                                                        var textVal;

                                                                        if (dataItem.text3 == null || dataItem.text3 == '') {
                                                                          textVal = '';
                                                                        }
                                                                        else {
                                                                          textVal = '['
                                                                                  + dataItem.text3Color
                                                                                  + '] '
                                                                                  + dataItem.text3;
                                                                        }
                                                                        return  textVal;
                                                                      }
                          }
                        ]
      //, rowTemplate : kendo.template($("#rowTemplate").html())
      };

      contsLv1GridOpt = contsLv1GridOptGoods;
    }
    else {
      // ----------------------------------------------------------------------
      // 1.TEXT or 기본
      // ----------------------------------------------------------------------
      contsLv1GridOptText = {
          dataSource  : contsLv1GridDs
        , noRecordMsg : "검색된 목록이 없습니다."
        , navigatable : true
        , scrollable  : true
        , selectable  : true
        , sortable    : true
        , editable    : "incell"
        , resizable   : true
        , columns     : [
                          /*{selectable: true, width: 20, attributes: { style: 'text-align:center;' }, headerAttributes: {style: "text-align: center;"}}*/
                          {field: 'chk'         , headerTemplate : "<input type='checkbox' id='contsLv1CheckAll' />"
                                                                        , width: 40 , attributes: {style: "text-align:center;" }, sortable : false, editable:function (dataItem) {return false;}
                                                , template :  function(dataItem) {
                                                                return '<input type="checkbox" name="contsLv1Check" class="couponGridChk" />'
                                                              }
                                                , locked: true, lockable: false
                          }
                        , {field: "sort"        , title: "순번"         , width:  60, attributes: {style: 'text-align:center;' }, headerAttributes: {style: "text-align: center;"}, sortable : false, locked: true, editor: sortEditor}
                        , {field: "dpContsId"   , title: "콘텐츠번호"   , width:  60, attributes: {style: 'text-align:center;' }, headerAttributes: {style: "text-align: center;"}, sortable : false, editable:function (dataItem) {return false;}, locked: true}
                        , {field: "titleNm"     , title: "타이틀명"     , width: 200, attributes: {style: 'text-align:center;' }, headerAttributes: {style: "text-align: center;"}, sortable : false, editable:function (dataItem) {return false;}, locked: true}
                        , {field: "dpStartDt"   , title: "시작일"       , width: 140, attributes: {style: 'text-align:center;' }, headerAttributes: {style: "text-align: center;"}, sortable : true, editable:function (dataItem) {return false;}}
                        , {field: "dpEndDt"     , title: "종료일"       , width: 140, attributes: {style: 'text-align:center;' }, headerAttributes: {style: "text-align: center;"}, sortable : true, editable:function (dataItem) {return false;}}
                        , {field: "dpRangeTpNm" , title: "전시범위"     , width:  60, attributes: {style: 'text-align:center;' }, headerAttributes: {style: "text-align: center;"}, sortable : false, editable:function (dataItem) {return false;}}
                        , {field: "statusNm"    , title: "진행상태"     , width:  80, attributes: {style: 'text-align:center;' }, headerAttributes: {style: "text-align: center;"}, sortable : false, editable:function (dataItem) {return false;}}
                        , {field: "management"  , title: "관리"         , width: 140, attributes: {style: "text-align:center;" }, sortable : false, editable:function (dataItem) {return false;}
                                                , template :  function(dataItem) {

                                                                var saveBtn = '<button type="button" class="btn-gray btn-s" kind="btnContsLv1Edit">수정 </button>'+ '&nbsp;';
                                                                var deleteBtn = '<button type="button" class="btn-red btn-s" kind="btnContsLv1Del">삭제 </button>';

                                                                var returnStr = '';

                                                                if(fnIsProgramAuth("SAVE")){
                                                                  returnStr = saveBtn;
                                                                }
                                                                if(fnIsProgramAuth("DELETE")){
                                                                  returnStr += deleteBtn;
                                                                }

                                                                return '<div id="pageMgrButtonArea" class="btn-area textCenter">' + returnStr + '</div>';
                                                }
                                                , lockable: false
                        }
                        , {field: "text1"       , title: "노출텍스트1"  , width: 150, attributes: {style: 'text-align:left;'   }, headerAttributes: {style: "text-align: center;"}, sortable : false, editable:function (dataItem) {return false;}
                                                , template :  function(dataItem) {

                                                                var textVal;

                                                                if (dataItem.text1 == null || dataItem.text1 == '') {
                                                                  textVal = '';
                                                                }
                                                                else {
                                                                  textVal = '['
                                                                          + dataItem.text1Color
                                                                          + '] '
                                                                          + dataItem.text1;
                                                                }
                                                                return  textVal;
                                                              }
                          }
                        , {field: "text2"       , title: "노출텍스트2"  , width: 150, attributes: {style: 'text-align:left;'   }, headerAttributes: {style: "text-align: center;"}, sortable : false, editable:function (dataItem) {return false;}
                                                , template :  function(dataItem) {

                                                                var textVal;

                                                                if (dataItem.text2 == null || dataItem.text2 == '') {
                                                                  textVal = '';
                                                                }
                                                                else {
                                                                  textVal = '['
                                                                          + dataItem.text2Color
                                                                          + '] '
                                                                          + dataItem.text2;
                                                                }
                                                                return  textVal;
                                                              }
                          }
                        , {field: "text3"       , title: "노출텍스트3"  , width: 150, attributes: {style: 'text-align:left;'   }, headerAttributes: {style: "text-align: center;"}, sortable : false, editable:function (dataItem) {return false;}
                                                , template :  function(dataItem) {
                                                                var textVal;

                                                                if (dataItem.text3 == null || dataItem.text3 == '') {
                                                                  textVal = '';
                                                                }
                                                                else {
                                                                  textVal = '['
                                                                          + dataItem.text3Color
                                                                          + '] '
                                                                          + dataItem.text3;
                                                                }
                                                                return  textVal;
                                                              }
                          }
                        , {field: "linkUrlPc"   , title: "링크URL"      , width: 300, attributes: {style: 'text-align:left;'   }, headerAttributes: {style: "text-align: center;"}, sortable : false, editable:function (dataItem) {return false;}}

                        ]
      //, rowTemplate : kendo.template($("#rowTemplate").html())
      };

      contsLv1GridOpt = contsLv1GridOptText;
    }


    contsLv1Grid = $('#contsLv1Grid').initializeKendoGrid( contsLv1GridOpt ).cKendoGrid();

    //$("#pageGrid").on("click", "tbody>tr", function () {
    //    //fnGridClick();
    //});
    //pageGrid.bind("dataBound", function() {
    //  $('#totalCnt').text(pageGridDs._total);
    //});

    // ------------------------------------------------------------------------
    // 그리드 버튼 클릭 이벤트 등록 - 그리드의 버튼 실행 이벤트
    // ------------------------------------------------------------------------
    // * 콘텐츠 Lv1 수정
    $('#contsLv1Grid').on("click", "button[kind=btnContsLv1Edit]", function(e) {
      e.preventDefault();
      e.stopImmediatePropagation();
      let dataItem = contsLv1Grid.dataItem($(e.currentTarget).closest("tr"));
      selectedContsLv1Data = dataItem;
      //console.log('# Lv1. dataItem :: ' + JSON.stringify(dataItem));
      fnBtnContsEdit(1, dataItem);
    });
    // * 콘텐츠 Lv1 삭제
    $('#contsLv1Grid').on("click", "button[kind=btnContsLv1Del]", function(e) {
      e.preventDefault();
      e.stopImmediatePropagation();
      let dataItem = contsLv1Grid.dataItem($(e.currentTarget).closest("tr"));
      selectedContsLv1Data = dataItem;
      fnContsDelSingle(1);
      //fnBtnContsDel(1, dataItem);
    });
    // 전체체크박스
    $("#contsLv1CheckAll").on("click",function(index){
      // 개별체크박스 처리
      if($("#contsLv1CheckAll").prop("checked") == true){
        // 전체체크
        // 개별체크 선택
        $('INPUT[name=contsLv1Check]').prop("checked", true);
        // 선택삭제 버튼 활성
        $('#fnBtnContsLv1Del').data('kendoButton').enable(true);
      }
      else{
        // 전체해제
        // 개별체크 선택 해제
        $('INPUT[name=contsLv1Check]').prop("checked",false);
        // 선택삭제 버튼 비활성
        $('#fnBtnContsLv1Del').data('kendoButton').enable(false);
      }
    });
    // 개별체크박스
    $('#ng-view').on("click","input[name=contsLv1Check]",function(index){

      const totalCnt    = $("input[name=contsLv1Check]").length;
      const checkedCnt  = $("input[name=contsLv1Check]:checked").length;
      // 전체체크박스 처리
      if (totalCnt == checkedCnt) {
        $('#contsLv1CheckAll').prop("checked", true);
      }
      else {
        $('#contsLv1CheckAll').prop("checked", false);
      }
      // 선택삭제 버튼 제어
      if (checkedCnt > 0) {
        $('#fnBtnContsLv1Del').data('kendoButton').enable(true);
      }
      else {
        // 선택삭제 버튼 비활성
        $('#fnBtnContsLv1Del').data('kendoButton').enable(false);
      }
    });
  }

  // ==========================================================================
  // # 콘텐츠Lv.2 그리드
  // ==========================================================================
  function fnInitContsLv2Grid(){
    //console.log('# fnInitContsLv2Grid Start');

    // 선택데이터 초기화
    selectedContsLv2Data = null;
    // 선택된 Data가 콘텐츠Lv.1 그리드이면 초기화
    if (selectedContsData != undefined && selectedContsData != null && selectedContsData != '') {
      if (selectedContsData.contsLevel == 2) {
        selectedContsData = null;
      }
    }

    // 콘텐츠그리드 초기화
    if ($("#contsLv2Grid").data("kendoGrid") != undefined ) {
      $("#contsLv2Grid").data("kendoGrid").destroy();
      $("#contsLv2Grid").empty();
    }

    // 콘텐츠타입
    var tmpContsTp = '';

    if (selectedInventoryData != null && selectedInventoryData.contsLevel1Tp != null && selectedInventoryData.contsLevel2Tp != null) {
      tmpContsTp = selectedInventoryData.contsLevel2Tp;
    }
    //console.log('### Lv2 tmpContsTp :: ', tmpContsTp);

    var inParamContsLevel     = 2;
    var inParamDpInventoryId;   // selectedInventoryData.dpInventoryId
    var inParamPrntsContsId;
    var inParamDpStartDt;
    var inParamDpEndDt;
    var inParamDpRangeTp      = $('#contsLv2DpRangeTp').val();
    var inParamIlCtgryId      = '';
    var inParamStatus         = $('#contsLv2Status').val();
    var inParamContsTp        = '';

    var inParamGoodsConds     = '';

    if (selectedInventoryData != undefined && selectedInventoryData != null) {
      //console.log('# selectedInventoryData :: ' + JSON.stringify(selectedInventoryData));
      // 인벤토리ID
      if (selectedInventoryData.dpInventoryId != undefined && selectedInventoryData.dpInventoryId != null) {
        inParamDpInventoryId  = selectedInventoryData.dpInventoryId;
      }

      // 콘텐츠유형
      if (selectedInventoryData.contsLevel2Tp != null) {
        inParamContsTp = selectedInventoryData.contsLevel2Tp;
      }

      // 카테고리ID
      if (selectedInventoryData.pageTp == 'PAGE_TP.CATEGORY') {
        // 페이지유형:카테고리
        if (selectedPageData != undefined && selectedPageData != null) {
          // 선택페이지정보존재
          if (selectedPageData.dpPageId != undefined && selectedPageData.dpPageId != null) {
            inParamIlCtgryId      = selectedPageData.dpPageId;
          }
        }
      }
    }
    else {
      //console.log('# selectedInventoryData is Null');
    }
    // 상위콘텐츠ID
    if (selectedContsLv1Data != undefined && selectedContsLv1Data != null) {
      if (selectedContsLv1Data.dpContsId != undefined && selectedContsLv1Data.dpContsId != null) {
        inParamPrntsContsId  = selectedContsLv1Data.dpContsId;
      }
    }
    else {
      //console.log('# selectedContsLv1Data is Null');
    }

    // 검색기간 조건 - 시작
    if ($('#dpStartDtLv2').val() == null || $('#dpStartDtLv2').val() == undefined || $('#dpStartDtLv2').val() == '') {
      inParamDpStartDt = '';
    }
    else {
      inParamDpStartDt = ($('#dpStartDtLv2').val()).replace(/-/gi, '') + $('#dpStartHourPickerLv2').val() + $('#dpStartMinPickerLv2').val() + '00';
    }
    // 검색기간 조건 - 종료
    if ($('#dpEndDtLv2').val() == null || $('#dpEndDtLv2').val() == undefined || $('#dpEndDtLv2').val() == '') {
      // 년월일이 공백이면 종료일 조건 없음
      inParamDpEndDt = '';
    }
    else {
      inParamDpEndDt = ($('#dpEndDtLv2').val()).replace(/-/gi, '') + $('#dpEndHourPickerLv2').val() + $('#dpEndMinPickerLv2').val() + '59';
    }

    // 상품인경우 조건 추가
    if(tmpContsTp == 'DP_CONTENTS_TP.GOODS') {
      inParamGoodsConds = ''
                        + "&dpCondTp="        + $('#contLv2DpCondTp').val()
                        + "&dpSortTp="        + $('#contLv2DpSortTp').val();
    }

    // 페이징없는 그리드
    contsLv2GridDs = fnGetDataSource({
      url      : "/admin/display/manage/selectDpContsList?" + "dpInventoryId="    + inParamDpInventoryId
                                                            + "&prntsContsId="    + inParamPrntsContsId
                                                            + "&contsLevel="      + inParamContsLevel
                                                            + "&dpStartDt="       + inParamDpStartDt
                                                            + "&dpEndDt="         + inParamDpEndDt
                                                            + "&dpRangeTp="       + inParamDpRangeTp
                                                            + "&ilCtgryId="       + inParamIlCtgryId
                                                            + "&status="          + inParamStatus
                                                            + "&contsTp="         + inParamContsTp
                                                            + inParamGoodsConds
    });

    // ------------------------------------------------------------------------
    // 콘텐츠유형별 처리
    // ------------------------------------------------------------------------
    if(tmpContsTp == 'DP_CONTENTS_TP.HTML') {
      // ----------------------------------------------------------------------
      // 2.HTML
      // ----------------------------------------------------------------------
      contsLv2GridOptHtml = {
          dataSource  : contsLv2GridDs
        , noRecordMsg : "검색된 목록이 없습니다."
        , navigatable : true
        , scrollable  : true
        , selectable  : true
        , sortable    : true
        , editable    : "incell"
        , resizable   : true
        , columns     : [
                          {field: 'chk'        , headerTemplate : "<input type='checkbox' id='contsLv2CheckAll' />"
                                                                          , width:  40, attributes: {style: "text-align:center;" }, sortable : false, editable:function (dataItem) {return false;}
                                               , template :  function(dataItem) {
                                                               return '<input type="checkbox" name="contsLv2Check" class="couponGridChk" />'
                                                             }
                                               , locked: true, lockable: false
                          }
                        , {field: "sort"       , title: "순번"            , width:  60, attributes: {style: 'text-align:center;' }, headerAttributes: {style: "text-align: center;"}, sortable : false, locked: true, editor: sortEditor}
                        , {field: "dpContsId"  , title: "콘텐츠번호"      , width:  60, attributes: {style: 'text-align:center;' }, headerAttributes: {style: "text-align: center;"}, sortable : false, editable:function (dataItem) {return false;}, locked: true}
                        , {field: "titleNm"    , title: "타이틀명"        , width: 200, attributes: {style: 'text-align:center;' }, headerAttributes: {style: "text-align: center;"}, sortable : false, editable:function (dataItem) {return false;}, locked: true}
                        , {field: "dpStartDt"  , title: "시작일"          , width: 140, attributes: {style: 'text-align:center;' }, headerAttributes: {style: "text-align: center;"}, sortable : true, editable:function (dataItem) {return false;}}
                        , {field: "dpEndDt"    , title: "종료일"          , width: 140, attributes: {style: 'text-align:center;' }, headerAttributes: {style: "text-align: center;"}, sortable : true, editable:function (dataItem) {return false;}}
                        , {field: "dpRangeTpNm", title: "전시범위"        , width:  60, attributes: {style: 'text-align:center;' }, headerAttributes: {style: "text-align: center;"}, sortable : false, editable:function (dataItem) {return false;}}
                        , {field: "statusNm"   , title: "진행상태"        , width:  80, attributes: {style: 'text-align:center;' }, headerAttributes: {style: "text-align: center;"}, sortable : false, editable:function (dataItem) {return false;}}
                        , {field: "management" , title: "관리"            , width: 140, attributes: {style: "text-align:center;" }, sortable : false, editable:function (dataItem) {return false;}
                                               , template :  function(dataItem) {
                                                                 var saveBtn = '<button type="button" class="btn-gray btn-s" kind="btnContsLv2Edit">수정 </button>' + '&nbsp;';
                                                                 var deleteBtn = '<button type="button" class="btn-red btn-s" kind="btnContsLv2Del">삭제 </button>';

                                                                 var returnStr = '';

                                                                 if(fnIsProgramAuth("SAVE")){
                                                                   returnStr = saveBtn;
                                                                 }
                                                                 if(fnIsProgramAuth("DELETE")){
                                                                   returnStr += deleteBtn;
                                                                 }

                                                                 return '<div id="pageMgrButtonArea" class="btn-area textCenter">' + returnStr + '</div>';
                                               }
                                               , lockable: false
                        }
                        , {field: "htmlMobile" , title: "Mobile"          , width:  90, attributes: {style: 'text-align:left;'   }, headerAttributes: {style: "text-align: center;"}, sortable : false, editable:function (dataItem) {return false;}
                                               , template :  function(dataItem) {
                                                               if (dataItem.htmlMobile == null || dataItem.htmlMobile == '') {
                                                                 return '';
                                                               }
                                                               else {
                                                                 return  '<div id="pageMgrButtonArea" class="btn-area textCenter">'
                                                                       + '<button type="button" class="btn-gray btn-s" kind="btnContsPreviewHtmlMobile">미리보기 </button>'
                                                                       + '</div>';
                                                               }
                                                             }
                          }
                        , {field: "htmlPc"     , title: "PC"              , width:  90, attributes: {style: 'text-align:left;'   }, headerAttributes: {style: "text-align: center;"}, sortable : false, editable:function (dataItem) {return false;}
                                               , template :  function(dataItem) {
                                                               if (dataItem.htmlPc == null || dataItem.htmlPc == '') {
                                                                 return '';
                                                               }
                                                               else {
                                                                 return  '<div id="pageMgrButtonArea" class="btn-area textCenter">'
                                                                       + '<button type="button" class="btn-gray btn-s" kind="btnContsPreviewHtmlPc">미리보기 </button>'
                                                                       + '</div>';
                                                               }
                                                             }
                          }

                        ]
      //, rowTemplate : kendo.template($("#rowTemplate").html())
      };

      contsLv2GridOpt = contsLv2GridOptHtml;

      // ----------------------------------------------------------------------
      // Lv.2-배너-미리보기 클릭이벤트
      // ----------------------------------------------------------------------
      // HTML-PC-Mobile
      $('#contsLv2Grid').on("click", "button[kind=btnContsPreviewHtmlMobile]", function(e) {
        e.preventDefault();
        e.stopImmediatePropagation();
        let dataItem = contsLv2Grid.dataItem($(e.currentTarget).closest("tr"));
         // 미리보기 팝업오픈
        var html    = dataItem.htmlMobile;
        fnInitPopupImagePreview(html, '', 'HTML');
      });
      // HTML-PC
      $('#contsLv2Grid').on("click", "button[kind=btnContsPreviewHtmlPc]", function(e) {
        e.preventDefault();
        e.stopImmediatePropagation();
        let dataItem = contsLv2Grid.dataItem($(e.currentTarget).closest("tr"));
        // 미리보기 팝업오픈
        var html    = dataItem.htmlPc;
        fnInitPopupImagePreview(html, '', 'HTML');
      });

    }
    else if(tmpContsTp == 'DP_CONTENTS_TP.BANNER') {
      // ----------------------------------------------------------------------
      // 3.BANNER
      // ----------------------------------------------------------------------
      contsLv2GridOptBanner = {
            dataSource  : contsLv2GridDs
          , noRecordMsg : "검색된 목록이 없습니다."
          , navigatable : true
          , scrollable  : true
          , selectable  : true
          , sortable    : true
          , editable    : "incell"
          , resizable   : true
          , columns     : [
                            {field: 'chk'             , headerTemplate : "<input type='checkbox' id='contsLv2CheckAll' />"
                                                                              , width:  40, attributes: {style: "text-align:center;" }, sortable : false, editable:function (dataItem) {return false;}
                                                      , template :  function(dataItem) {
                                                                      return '<input type="checkbox" name="contsLv2Check" class="couponGridChk" />'
                                                                    }
                                                      , locked: true, lockable: false
                            }
                          , {field: "sort"            , title: "순번"         , width:  60, attributes: {style: 'text-align:center;' }, headerAttributes: {style: "text-align: center;"}, sortable : false, locked: true, editor: sortEditor}
                          , {field: "dpContsId"       , title: "콘텐츠번호"   , width:  60, attributes: {style: 'text-align:center;' }, headerAttributes: {style: "text-align: center;"}, sortable : false, editable:function (dataItem) {return false;}, locked: true}
                          , {field: "titleNm"         , title: "타이틀명"     , width: 200, attributes: {style: 'text-align:center;' }, headerAttributes: {style: "text-align: center;"}, sortable : false, editable:function (dataItem) {return false;}, locked: true}
                          , {field: "dpStartDt"       , title: "시작일"       , width: 140, attributes: {style: 'text-align:center;' }, headerAttributes: {style: "text-align: center;"}, sortable : true, editable:function (dataItem) {return false;}}
                          , {field: "dpEndDt"         , title: "종료일"       , width: 140, attributes: {style: 'text-align:center;' }, headerAttributes: {style: "text-align: center;"}, sortable : true, editable:function (dataItem) {return false;}}
                          , {field: "dpRangeTpNm"     , title: "전시범위"     , width:  60, attributes: {style: 'text-align:center;' }, headerAttributes: {style: "text-align: center;"}, sortable : false, editable:function (dataItem) {return false;}}
                          , {field: "statusNm"        , title: "진행상태"     , width:  80, attributes: {style: 'text-align:center;' }, headerAttributes: {style: "text-align: center;"}, sortable : false, editable:function (dataItem) {return false;}}
                          , {field: "management"      , title: "관리"         , width: 140, attributes: {style: "text-align:center;" }, sortable : false, editable:function (dataItem) {return false;}
                                                      , template :  function(dataItem) {
                                                                        var saveBtn = '<button type="button" class="btn-gray btn-s" kind="btnContsLv2Edit">수정 </button>' + '&nbsp;';
                                                                        var deleteBtn = '<button type="button" class="btn-red btn-s" kind="btnContsLv2Del">삭제 </button>';

                                                                        var returnStr = '';

                                                                        if(fnIsProgramAuth("SAVE")){
                                                                          returnStr = saveBtn;
                                                                        }
                                                                        if(fnIsProgramAuth("DELETE")){
                                                                          returnStr += deleteBtn;
                                                                        }

                                                                        return '<div id="pageMgrButtonArea" class="btn-area textCenter">' + returnStr + '</div>';
                                                      }
                                                      , lockable: false
                          }
                          , {field: "imgPathMobile"   , title: "이미지1"      , width:  90, attributes: {style: 'text-align:left;'   }, headerAttributes: {style: "text-align: center;"}, sortable : false, editable:function (dataItem) {return false;}
                                                      , template :  function(dataItem) {
                                                                      if (dataItem.imgPathMobile == null || dataItem.imgPathMobile == '') {
                                                                        return '';
                                                                      }
                                                                      else {
                                                                        return  '<div id="pageMgrButtonArea" class="btn-area textCenter">'
                                                                              + '<button type="button" class="btn-gray btn-s" kind="btnContsPreviewImgMobile">미리보기 </button>'
                                                                              + '</div>';
                                                                      }
                                                                    }
                            }
                          , {field: "imgPathPc"       , title: "이미지2"      , width:  90, attributes: {style: 'text-align:left;'   }, headerAttributes: {style: "text-align: center;"}, sortable : false, editable:function (dataItem) {return false;}
                                                      , template :  function(dataItem) {
                                                                      if (dataItem.imgPathPc == null || dataItem.imgPathPc == '') {
                                                                        return '';
                                                                      }
                                                                      else {
                                                                        return  '<div id="pageMgrButtonArea" class="btn-area textCenter">'
                                                                              + '<button type="button" class="btn-gray btn-s" kind="btnContsPreviewImgPc">미리보기 </button>'
                                                                              + '</div>';
                                                                      }
                                                                    }
                            }
                          , {field: "gifImgPathMobile", title: "Mobile gif"   , width:  90, attributes: {style: 'text-align:left;'   }, headerAttributes: {style: "text-align: center;"}, sortable : false, editable:function (dataItem) {return false;}
                                                      , template :  function(dataItem) {
                                                                      if (dataItem.gifImgPathMobile == null || dataItem.gifImgPathMobile == '') {
                                                                        return '';
                                                                      }
                                                                      else {
                                                                        return  '<div id="pageMgrButtonArea" class="btn-area textCenter">'
                                                                              + '<button type="button" class="btn-gray btn-s" kind="btnContsPreviewImgGifMobile">미리보기 </button>'
                                                                              + '</div>';
                                                                      }
                                                                    }
                            }
                          , {field: "gifImgPathPc"    , title: "PC gif"       , width:  90, attributes: {style: 'text-align:left;'   }, headerAttributes: {style: "text-align: center;"}, sortable : false, editable:function (dataItem) {return false;}
                                                      , template :  function(dataItem) {
                                                                      if (dataItem.gifImgPathPc == null || dataItem.gifImgPathPc == '') {
                                                                        return '';
                                                                      }
                                                                      else {
                                                                        return  '<div id="pageMgrButtonArea" class="btn-area textCenter">'
                                                                              + '<button type="button" class="btn-gray btn-s" kind="btnContsPreviewImgGifPc">미리보기 </button>'
                                                                              + '</div>';
                                                                      }
                                                                    }
                            }
                          , {field: "text1"           , title: "노출텍스트1"  , width: 150, attributes: {style: 'text-align:left;'   }, headerAttributes: {style: "text-align: center;"}, sortable : false, editable:function (dataItem) {return false;}
                                                      , template :  function(dataItem) {

                                                                      var textVal;

                                                                      if (dataItem.text1 == null || dataItem.text1 == '') {
                                                                        textVal = '';
                                                                      }
                                                                      else {
                                                                        textVal = '['
                                                                                + dataItem.text1Color
                                                                                + '] '
                                                                                + dataItem.text1;
                                                                      }
                                                                      return  textVal;
                                                                    }
                            }
                          , {field: "text2"           , title: "노출텍스트2"  , width: 150, attributes: {style: 'text-align:left;'   }, headerAttributes: {style: "text-align: center;"}, sortable : false, editable:function (dataItem) {return false;}
                                                      , template :  function(dataItem) {

                                                                      var textVal;

                                                                      if (dataItem.text2 == null || dataItem.text2 == '') {
                                                                        textVal = '';
                                                                      }
                                                                      else {
                                                                        textVal = '['
                                                                                + dataItem.text2Color
                                                                                + '] '
                                                                                + dataItem.text2;
                                                                      }
                                                                      return  textVal;
                                                                    }
                            }
                          , {field: "text3"           , title: "노출텍스트3"  , width: 150, attributes: {style: 'text-align:left;'   }, headerAttributes: {style: "text-align: center;"}, sortable : false, editable:function (dataItem) {return false;}
                                                      , template :  function(dataItem) {
                                                                      var textVal;

                                                                      if (dataItem.text3 == null || dataItem.text3 == '') {
                                                                        textVal = '';
                                                                      }
                                                                      else {
                                                                        textVal = '['
                                                                                + dataItem.text3Color
                                                                                + '] '
                                                                                + dataItem.text3;
                                                                      }
                                                                      return  textVal;
                                                                    }
                            }
                          ]
      //, rowTemplate : kendo.template($("#rowTemplate").html())
      };
      contsLv2GridOpt = contsLv2GridOptBanner;

      // ----------------------------------------------------------------------
      // Lv.2-배너-미리보기 클릭이벤트
      // ----------------------------------------------------------------------
      // 이미지1
      $('#contsLv2Grid').on("click", "button[kind=btnContsPreviewImgMobile]", function(e) {
        e.preventDefault();
        e.stopImmediatePropagation();
        let dataItem = contsLv2Grid.dataItem($(e.currentTarget).closest("tr"));
        //selectedContsLv1Data = dataItem;
        //console.log('# Lv1. dataItem :: ' + JSON.stringify(dataItem));
        //alert('# Lv.1 이미지1 Click');
        // 미리보기 팝업오픈
        var path    = dataItem.imgPathMobile;
        var fileNm  = dataItem.imgOriginNmMobile;
        fnInitPopupImagePreview(path, fileNm, 'IMAGE');
      });
      // gif이미지모바일
      $('#contsLv2Grid').on("click", "button[kind=btnContsPreviewImgGifMobile]", function(e) {
        e.preventDefault();
        e.stopImmediatePropagation();
        let dataItem = contsLv2Grid.dataItem($(e.currentTarget).closest("tr"));
        //selectedContsLv1Data = dataItem;
        //console.log('# Lv1. dataItem :: ' + JSON.stringify(dataItem));
        //alert('# Lv.1 이미지1 Click');
        // 미리보기 팝업오픈
        var path    = dataItem.gifImgPathMobile;
        var fileNm  = dataItem.gifImgOrginNmMobile;
        fnInitPopupImagePreview(path, fileNm, 'IMAGE');
      });
      // 이미지2
      $('#contsLv2Grid').on("click", "button[kind=btnContsPreviewImgPc]", function(e) {
        e.preventDefault();
        e.stopImmediatePropagation();
        let dataItem = contsLv2Grid.dataItem($(e.currentTarget).closest("tr"));
        //selectedContsLv1Data = dataItem;
        //console.log('# Lv1. dataItem :: ' + JSON.stringify(dataItem));
        //alert('# Lv.1 이미지2 Click');
        // 미리보기 팝업오픈
        var path    = dataItem.imgPathPc;
        var fileNm  = dataItem.imgOriginNmPc;
        fnInitPopupImagePreview(path, fileNm, 'IMAGE');
      });
      // gif이미지PC
      $('#contsLv2Grid').on("click", "button[kind=btnContsPreviewImgGifPc]", function(e) {
        e.preventDefault();
        e.stopImmediatePropagation();
        let dataItem = contsLv2Grid.dataItem($(e.currentTarget).closest("tr"));
        //selectedContsLv1Data = dataItem;
        //console.log('# Lv1. dataItem :: ' + JSON.stringify(dataItem));
        //alert('# Lv.1 이미지1 Click');
        // 미리보기 팝업오픈
        var path    = dataItem.gifImgPathPc;
        var fileNm  = dataItem.gifImgOrginNmPc;
        fnInitPopupImagePreview(path, fileNm, 'IMAGE');
      });
    }
    else if(tmpContsTp == 'DP_CONTENTS_TP.BRAND') {
      // ----------------------------------------------------------------------
      // 4.BRAND
      // ----------------------------------------------------------------------
      contsLv2GridOptBrand = {
          dataSource  : contsLv2GridDs
        , noRecordMsg : "검색된 목록이 없습니다."
        , navigatable : true
        , scrollable  : true
        , selectable  : true
        , sortable    : true
        , editable    : "incell"
        , resizable   : true
        , columns     : [
                          {field: 'chk'           , headerTemplate : "<input type='checkbox' id='contsLv2CheckAll' />"
                                                                           , width:  40, attributes: {style: "text-align:center;" }, sortable : false, editable:function (dataItem) {return false;}
                                                  , template :  function(dataItem) {
                                                                  return '<input type="checkbox" name="contsLv2Check" class="couponGridChk" />'
                                                                }
                                                  , locked: true, lockable: false
                          }
                        , {field: "sort"          , title: "순번"          , width:  60, attributes: {style: 'text-align:center;' }, headerAttributes: {style: "text-align: center;"}, sortable : false, locked: true, editor: sortEditor}
                        , {field: "dpContsId"     , title: "콘텐츠번호"    , width:  60, attributes: {style: 'text-align:center;' }, headerAttributes: {style: "text-align: center;"}, sortable : false, editable:function (dataItem) {return false;}, locked: true}
                        , {field: "titleNm"       , title: "타이틀명"      , width: 200, attributes: {style: 'text-align:center;' }, headerAttributes: {style: "text-align: center;"}, sortable : false, editable:function (dataItem) {return false;}, locked: true}
                        , {field: "contsNm"       , title: "전시브랜드명"      , width: 200, attributes: {style: 'text-align:center;' }, headerAttributes: {style: "text-align: center;"}, sortable : false, editable:function (dataItem) {return false;}}
                        , {field: "dpStartDt"     , title: "시작일"        , width: 140, attributes: {style: 'text-align:center;' }, headerAttributes: {style: "text-align: center;"}, sortable : true, editable:function (dataItem) {return false;}}
                        , {field: "dpEndDt"       , title: "종료일"        , width: 140, attributes: {style: 'text-align:center;' }, headerAttributes: {style: "text-align: center;"}, sortable : true, editable:function (dataItem) {return false;}}
                        , {field: "dpRangeTpNm"   , title: "전시범위"      , width:  60, attributes: {style: 'text-align:center;' }, headerAttributes: {style: "text-align: center;"}, sortable : false, editable:function (dataItem) {return false;}}
                        , {field: "statusNm"      , title: "진행상태"      , width:  80, attributes: {style: 'text-align:center;' }, headerAttributes: {style: "text-align: center;"}, sortable : false, editable:function (dataItem) {return false;}}
                        , {field: "management"    , title: "관리"            , width: 140, attributes: {style: "text-align:center;" }, sortable : false, editable:function (dataItem) {return false;}
                                                  , template :  function(dataItem) {
                                                                    var saveBtn = '<button type="button" class="btn-gray btn-s" kind="btnContsLv2Edit">수정 </button>' + '&nbsp;';
                                                                    var deleteBtn = '<button type="button" class="btn-red btn-s" kind="btnContsLv2Del">삭제 </button>';

                                                                    var returnStr = '';

                                                                    if(fnIsProgramAuth("SAVE")){
                                                                      returnStr = saveBtn;
                                                                    }
                                                                    if(fnIsProgramAuth("DELETE")){
                                                                      returnStr += deleteBtn;
                                                                    }

                                                                    return '<div id="pageMgrButtonArea" class="btn-area textCenter">' + returnStr + '</div>';
                                                  }
                                                  , lockable: false
                        }
                        , {field: "imgPathMobile" , title: "이미지1"       , width:  90, attributes: {style: 'text-align:left;'   }, headerAttributes: {style: "text-align: center;"}, sortable : false, editable:function (dataItem) {return false;}
                                                  , template :  function(dataItem) {
                                                                  if (dataItem.imgPathMobile == null || dataItem.imgPathMobile == '') {
                                                                    return '';
                                                                  }
                                                                  else {
                                                                    return  '<div id="pageMgrButtonArea" class="btn-area textCenter">'
                                                                          + '<button type="button" class="btn-gray btn-s" kind="btnContsPreviewImgMobile">미리보기 </button>'
                                                                          + '</div>';
                                                                  }
                                                                }
                          }
                        , {field: "imgPathPc"     , title: "이미지2"       , width:  90, attributes: {style: 'text-align:left;'   }, headerAttributes: {style: "text-align: center;"}, sortable : false, editable:function (dataItem) {return false;}
                                                  , template :  function(dataItem) {
                                                                  if (dataItem.imgPathPc == null || dataItem.imgPathPc == '') {
                                                                    return '';
                                                                  }
                                                                  else {
                                                                    return  '<div id="pageMgrButtonArea" class="btn-area textCenter">'
                                                                          + '<button type="button" class="btn-gray btn-s" kind="btnContsPreviewImgPc">미리보기 </button>'
                                                                          + '</div>';
                                                                  }
                                                                }
                          }

                        , {field: "text1"         , title: "노출텍스트1"   , width: 150, attributes: {style: 'text-align:left;'   }, headerAttributes: {style: "text-align: center;"}, sortable : false, editable:function (dataItem) {return false;}
                                                  , template : function(dataItem) {

                                                                 var textVal;

                                                                 if (dataItem.text1 == null || dataItem.text1 == '') {
                                                                   textVal = '';
                                                                 }
                                                                 else {
                                                                   textVal = '['
                                                                           + dataItem.text1Color
                                                                           + '] '
                                                                           + dataItem.text1;
                                                                 }
                                                                 return  textVal;
                                                               }
                          }
                        , {field: "text2"         , title: "노출텍스트2"   , width: 150, attributes: {style: 'text-align:left;'   }, headerAttributes: {style: "text-align: center;"}, sortable : false, editable:function (dataItem) {return false;}
                                                  , template : function(dataItem) {

                                                                 var textVal;

                                                                 if (dataItem.text2 == null || dataItem.text2 == '') {
                                                                   textVal = '';
                                                                 }
                                                                 else {
                                                                   textVal = '['
                                                                           + dataItem.text2Color
                                                                           + '] '
                                                                           + dataItem.text2;
                                                                 }
                                                                 return  textVal;
                                                               }
                          }
                        , {field: "text3"         , title: "노출텍스트3"   , width: 150, attributes: {style: 'text-align:left;'   }, headerAttributes: {style: "text-align: center;"}, sortable : false, editable:function (dataItem) {return false;}
                                                  , template : function(dataItem) {
                                                                 var textVal;

                                                                 if (dataItem.text3 == null || dataItem.text3 == '') {
                                                                   textVal = '';
                                                                 }
                                                                 else {
                                                                   textVal = '['
                                                                           + dataItem.text3Color
                                                                           + '] '
                                                                           + dataItem.text3;
                                                                 }
                                                                 return  textVal;
                                                               }
                          }
                        ]
      //, rowTemplate : kendo.template($("#rowTemplate").html())
      };

      contsLv2GridOpt = contsLv2GridOptBrand;

      // ----------------------------------------------------------------------
      // Lv.1-브랜드-미리보기 클릭이벤트
      // ----------------------------------------------------------------------
      // 이미지1
      $('#contsLv2Grid').on("click", "button[kind=btnContsPreviewImgMobile]", function(e) {
        e.preventDefault();
        e.stopImmediatePropagation();
        let dataItem = contsLv2Grid.dataItem($(e.currentTarget).closest("tr"));
        //selectedContsLv1Data = dataItem;
        //console.log('# Lv2. dataItem :: ' + JSON.stringify(dataItem));
        //alert('# Lv.2 이미지1 Click');
        // 미리보기 팝업오픈
        var path    = dataItem.imgPathMobile;
        var fileNm  = dataItem.imgOriginNmMobile;
        fnInitPopupImagePreview(path, fileNm, 'IMAGE');
      });
      // 이미지2
      $('#contsLv2Grid').on("click", "button[kind=btnContsPreviewImgPc]", function(e) {
        e.preventDefault();
        e.stopImmediatePropagation();
        let dataItem = contsLv2Grid.dataItem($(e.currentTarget).closest("tr"));
        //selectedContsLv1Data = dataItem;
        //console.log('# Lv2. dataItem :: ' + JSON.stringify(dataItem));
        //alert('# Lv.2 이미지2 Click');
        // 미리보기 팝업오픈
        var path    = dataItem.imgPathPc;
        var fileNm  = dataItem.imgOriginNmPc;
        fnInitPopupImagePreview(path, fileNm, 'IMAGE');
      });
    }
    else if(tmpContsTp == 'DP_CONTENTS_TP.CATEGORY') {
      // ----------------------------------------------------------------------
      // 5.CATEGORY
      // ----------------------------------------------------------------------
      contsLv2GridOptCategory = {
          dataSource  : contsLv2GridDs
        , noRecordMsg : "검색된 목록이 없습니다."
        , navigatable : true
        , scrollable  : true
        , selectable  : true
        , sortable    : true
        , editable    : "incell"
        , resizable   : true
        , columns     : [
                          {field: 'chk'        , headerTemplate : "<input type='checkbox' id='contsLv2CheckAll' />"
                                                                        , width:  40, attributes: {style: "text-align:center;" }, sortable : false, editable:function (dataItem) {return false;}
                                               , template :  function(dataItem) {
                                                               return '<input type="checkbox" name="contsLv2Check" class="couponGridChk" />'
                                                             }
                                               , locked: true, lockable: false
                          }
                        , {field: "sort"       , title: "순번"          , width:  60, attributes: {style: 'text-align:center;' }, headerAttributes: {style: "text-align: center;"}, sortable : false, locked: true, editor: sortEditor}
                        , {field: "dpContsId"  , title: "콘텐츠번호"    , width:  60, attributes: {style: 'text-align:center;' }, headerAttributes: {style: "text-align: center;"}, sortable : false, editable:function (dataItem) {return false;}, locked: true}
                        , {field: "titleNm"    , title: "타이틀명"      , width: 200, attributes: {style: 'text-align:center;' }, headerAttributes: {style: "text-align: center;"}, sortable : false, editable:function (dataItem) {return false;}, locked: true}
                        , {field: "dpStartDt"  , title: "시작일"        , width: 140, attributes: {style: 'text-align:center;' }, headerAttributes: {style: "text-align: center;"}, sortable : true, editable:function (dataItem) {return false;}}
                        , {field: "dpEndDt"    , title: "종료일"        , width: 140, attributes: {style: 'text-align:center;' }, headerAttributes: {style: "text-align: center;"}, sortable : true, editable:function (dataItem) {return false;}}
                        , {field: "dpRangeTpNm", title: "전시범위"      , width:  60, attributes: {style: 'text-align:center;' }, headerAttributes: {style: "text-align: center;"}, sortable : false, editable:function (dataItem) {return false;}}
                        , {field: "statusNm"   , title: "진행상태"      , width:  80, attributes: {style: 'text-align:center;' }, headerAttributes: {style: "text-align: center;"}, sortable : false, editable:function (dataItem) {return false;}}
                        , {field: "management" , title: "관리"            , width: 140, attributes: {style: "text-align:center;" }, sortable : false, editable:function (dataItem) {return false;}
                                                , template :  function(dataItem) {
                                                                  var saveBtn = '<button type="button" class="btn-gray btn-s" kind="btnContsLv2Edit">수정 </button>' + '&nbsp;';
                                                                  var deleteBtn = '<button type="button" class="btn-red btn-s" kind="btnContsLv2Del">삭제 </button>';

                                                                  var returnStr = '';

                                                                  if(fnIsProgramAuth("SAVE")){
                                                                    returnStr = saveBtn;
                                                                  }
                                                                  if(fnIsProgramAuth("DELETE")){
                                                                    returnStr += deleteBtn;
                                                                  }

                                                                  return '<div id="pageMgrButtonArea" class="btn-area textCenter">' + returnStr + '</div>';
                                                }
                                                , lockable: false
                        }
                        , {field: "mallDivNm"  , title: "몰인몰구분"    , width: 100, attributes: {style: 'text-align:center;' }, headerAttributes: {style: "text-align: center;"}, sortable : false, editable:function (dataItem) {return false;}}
                        , {field: "ctgryFullNm", title: "전시카테고리"  , width: 100, attributes: {style: 'text-align:center;' }, headerAttributes: {style: "text-align: center;"}, sortable : false, editable:function (dataItem) {return false;}}
                        , {field: "text1"      , title: "노출텍스트1"   , width: 150, attributes: {style: 'text-align:left;'   }, headerAttributes: {style: "text-align: center;"}, sortable : false, editable:function (dataItem) {return false;}
                                               , template : function(dataItem) {

                                                              var textVal;

                                                              if (dataItem.text1 == null || dataItem.text1 == '') {
                                                                textVal = '';
                                                              }
                                                              else {
                                                                textVal = '['
                                                                        + dataItem.text1Color
                                                                        + '] '
                                                                        + dataItem.text1;
                                                              }
                                                              return  textVal;
                                                            }
                          }
                        , {field: "text2"      , title: "노출텍스트2"   , width: 150, attributes: {style: 'text-align:left;'   }, headerAttributes: {style: "text-align: center;"}, sortable : false, editable:function (dataItem) {return false;}
                                               , template : function(dataItem) {

                                                              var textVal;

                                                              if (dataItem.text2 == null || dataItem.text2 == '') {
                                                                textVal = '';
                                                              }
                                                              else {
                                                                textVal = '['
                                                                        + dataItem.text2Color
                                                                        + '] '
                                                                        + dataItem.text2;
                                                              }
                                                              return  textVal;
                                                            }
                          }
                        , {field: "text3"      , title: "노출텍스트3"   , width: 150, attributes: {style: 'text-align:left;'   }, headerAttributes: {style: "text-align: center;"}, sortable : false, editable:function (dataItem) {return false;}
                                               , template : function(dataItem) {
                                                              var textVal;

                                                              if (dataItem.text3 == null || dataItem.text3 == '') {
                                                                textVal = '';
                                                              }
                                                              else {
                                                                textVal = '['
                                                                        + dataItem.text3Color
                                                                        + '] '
                                                                        + dataItem.text3;
                                                              }
                                                              return  textVal;
                                                            }
                          }
                        ]
      //, rowTemplate : kendo.template($("#rowTemplate").html())
      };

      contsLv2GridOpt = contsLv2GridOptCategory;
    }
    else if(tmpContsTp == 'DP_CONTENTS_TP.GOODS') {
      // ----------------------------------------------------------------------
      // GOODS
      // ----------------------------------------------------------------------
      contsLv2GridOptGoods = {
          dataSource  : contsLv2GridDs
        , noRecordMsg : "검색된 목록이 없습니다."
        , navigatable : true
        , scrollable  : true
        , selectable  : true
        , sortable    : true
        , editable    : "incell"
        , resizable   : true
        , columns     : [
                          {field: 'chk'                  , headerTemplate : "<input type='checkbox' id='contsLv2CheckAll' />"
                                                                                 , width:  40, attributes: {style: "text-align:center;" }, sortable : false, editable:function (dataItem) {return false;}
                                                         , template :  function(dataItem) {
                                                                         return '<input type="checkbox" name="contsLv2Check" class="couponGridChk" />'
                                                                       }
                                                         , locked: true, lockable: false
                          }
                        , {field: "sort"                 , title: "순번"         , width:  60, attributes: {style: 'text-align:center;' }, headerAttributes: {style: "text-align: center;"}, sortable : false, locked: true, editor: sortEditor}
                        , {field: "dpContsId"            , title: "콘텐츠번호"   , width:  60, attributes: {style: 'text-align:center;' }, headerAttributes: {style: "text-align: center;"}, sortable : false, editable:function (dataItem) {return false;}, locked: true}
                        , {field: "titleNm"              , title: "타이틀명"     , width: 200, attributes: {style: 'text-align:center;' }, headerAttributes: {style: "text-align: center;"}, sortable : false, editable:function (dataItem) {return false;}, locked: true}
                        , {field: "contsId"              , title: "상품코드"     , width:  60, attributes: {style: 'text-align:center;' }, headerAttributes: {style: "text-align: center;"}, sortable : false, editable:function (dataItem) {return false;}}
                        , {field: "contsNm"              , title: "상품명"       , width: 200, attributes: {style: 'text-align:center;' }, headerAttributes: {style: "text-align: center;"}, sortable : false, editable:function (dataItem) {return false;}}
                        , {field: "dpStartDt"            , title: "시작일"       , width: 140, attributes: {style: 'text-align:center;' }, headerAttributes: {style: "text-align: center;"}, sortable : true, editable:function (dataItem) {return false;}}
                        , {field: "dpEndDt"              , title: "종료일"       , width: 140, attributes: {style: 'text-align:center;' }, headerAttributes: {style: "text-align: center;"}, sortable : true, editable:function (dataItem) {return false;}}
                        , {field: "dpRangeTpNm"          , title: "전시범위"     , width:  60, attributes: {style: 'text-align:center;' }, headerAttributes: {style: "text-align: center;"}, sortable : false, editable:function (dataItem) {return false;}}
                        , {field: "statusNm"             , title: "진행상태"     , width:  80, attributes: {style: 'text-align:center;' }, headerAttributes: {style: "text-align: center;"}, sortable : false, editable:function (dataItem) {return false;}}
                        , {field: "management"           , title: "관리"         , width: 140, attributes: {style: "text-align:center;" }, sortable : false, editable:function (dataItem) {return false;}
                                                        , template :  function(dataItem) {
                                                                          if (dataItem.dpCondTp == 'DP_COND_TP.MANUAL') {
                                                                            // 노출조건이 직접등록 => 수정/삭제 버튼 노출
                                                                            var saveBtn = '<button type="button" class="btn-gray btn-s" kind="btnContsLv2Edit">수정 </button>' + '&nbsp;';
                                                                            var deleteBtn = '<button type="button" class="btn-red btn-s" kind="btnContsLv2Del">삭제 </button>';

                                                                            var returnStr = '';

                                                                            if(fnIsProgramAuth("SAVE")){
                                                                              returnStr = saveBtn;
                                                                            }
                                                                            if(fnIsProgramAuth("DELETE")){
                                                                              returnStr += deleteBtn;
                                                                            }

                                                                            return '<div id="pageMgrButtonArea" class="btn-area textCenter">' + returnStr + '</div>';
                                                                          }
                                                                          else {
                                                                            // 노출조건이 직접등로 이외 => 수정 버튼 숨김
                                                                            var returnStr = '';
                                                                            if(fnIsProgramAuth("DELETE")){
                                                                              returnStr = '<button type="button" class="btn-red btn-s" kind="btnContsLv2Del">삭제 </button>';
                                                                            }

                                                                            return '<div id="pageMgrButtonArea" class="btn-area textCenter">' + returnStr + '</div>';
                                                                          }
                                                        }
                                                        , lockable: false
                        }
                        , {field: "dpCondTpNm"           , title: "노출조건"     , width:  60, attributes: {style: 'text-align:center;' }, headerAttributes: {style: "text-align: center;"}, sortable : false, editable:function (dataItem) {return false;}}
                        , {field: "dpSortTpNm"           , title: "노출순서"     , width:  60, attributes: {style: 'text-align:center;' }, headerAttributes: {style: "text-align: center;"}, sortable : false, editable:function (dataItem) {return false;}}
                        , {field: "goodsTypeName"        , title: "상품유형"     , width:  60, attributes: {style: 'text-align:center;' }, headerAttributes: {style: "text-align: center;"}, sortable : false, editable:function (dataItem) {return false;}}
                        , {field: "supplierName"         , title: "공급업체"     , width: 100, attributes: {style: 'text-align:center;' }, headerAttributes: {style: "text-align: center;"}, sortable : false, editable:function (dataItem) {return false;}}
                        , {field: "goodsBrandNm"         , title: "브랜드"       , width: 120, attributes: {style: 'text-align:center;' }, headerAttributes: {style: "text-align: center;"}, sortable : false, editable:function (dataItem) {return false;}}
                        , {field: "recommendedPrice"     , title: "정상가"       , width:  70, attributes: {style: 'text-align:center;' }, headerAttributes: {style: "text-align: center;"}, sortable : false, editable:function (dataItem) {return false;}
                                                                                 , format: "{0:\#\#,\#}"
                                                                                 //, template : function(dataItem) {
                                                                                 //               return (dataItem.recommendedPrice).replace(/(\d)(?=(?:\d{3})+(?!\d))/g, '$1,');
                                                                                 //             }
                          }
                        , {field: "salePrice"            , title: "판매가"       , width:  70, attributes: {style: 'text-align:center;' }, headerAttributes: {style: "text-align: center;"}, sortable : false, editable:function (dataItem) {return false;}
                                                                                 , format: "{0:\#\#,\#}"
                                                                                 //, template : function(dataItem) {
                                                                                 //               return (dataItem.salePrice).replace(/(\d)(?=(?:\d{3})+(?!\d))/g, '$1,');
                                                                                 //             }
                          }
                        , {field: "dispRangeNm"          , title: "판매허용범위" , width:  80, attributes: {style: 'text-align:center;' }, headerAttributes: {style: "text-align: center;"}, sortable : false, editable:function (dataItem) {return false;}
                                                                                 , template  : function(dataItem) {

                                                                                                 var dispYnRangeVal = '';
                                                                                                 var yesCnt = 0;
                                                                                                 var sepStr = '';

                                                                                                 // PC
                                                                                                 if (dataItem.dispWebPcYn == 'Y') {
                                                                                                   dispYnRangeVal = dataItem.dispWebPcYnNm;
                                                                                                   yesCnt++;
                                                                                                 }
                                                                                                 // Mobile
                                                                                                 if (dataItem.dispWebMobileYn == 'Y') {

                                                                                                   if (yesCnt > 0) {
                                                                                                     sepStr = '/';
                                                                                                   }

                                                                                                   dispYnRangeVal += (sepStr + dataItem.dispWebMobileYnNm);
                                                                                                   yesCnt++;
                                                                                                 }
                                                                                                 // APP
                                                                                                 if (dataItem.dispAppYn == 'Y') {

                                                                                                   if (yesCnt > 0) {
                                                                                                     sepStr = '/';
                                                                                                   }

                                                                                                   dispYnRangeVal += (sepStr + dataItem.dispAppYnNm);
                                                                                                 }
                                                                                                 return  dispYnRangeVal
                                                                                               }
                                                        }
                        , {field: "purchaseRangeNm"      , title: "구매허용범위" , width: 120, attributes: {style: 'text-align:center;' }, headerAttributes: {style: "text-align: center;"}, sortable : false, editable:function (dataItem) {return false;}
                                                                                 , template  : function(dataItem) {

                                                                                                 var purchaseYnRangeVal = '';
                                                                                                 var yesCnt = 0;
                                                                                                 var sepStr = '';

                                                                                                 // 일반회원
                                                                                                 if (dataItem.purchaseMemberYn == 'Y') {
                                                                                                   purchaseYnRangeVal = dataItem.purchaseMemberYnNm;
                                                                                                   yesCnt++;
                                                                                                 }
                                                                                                 // 임직원
                                                                                                 if (dataItem.purchaseEmployeeYn == 'Y') {

                                                                                                   if (yesCnt > 0) {
                                                                                                     sepStr = '/';
                                                                                                   }

                                                                                                   purchaseYnRangeVal += (sepStr + dataItem.purchaseEmployeeYnNm);
                                                                                                   yesCnt++;
                                                                                                 }
                                                                                                 // 비회원
                                                                                                 if (dataItem.purchaseNonmemberYn == 'Y') {

                                                                                                   if (yesCnt > 0) {
                                                                                                     sepStr = '/';
                                                                                                   }

                                                                                                   purchaseYnRangeVal += (sepStr + dataItem.purchaseNonmemberYnNm);
                                                                                                 }
                                                                                                 return  purchaseYnRangeVal
                                                                                               }
                          }
                        , {field: "goodsBasicCtgryFullNm", title: "전시카테고리" , width: 300, attributes: {style: 'text-align:center;' }, headerAttributes: {style: "text-align: center;"}, sortable : false, editable:function (dataItem) {return false;}}
                        , {field: "saleStatusName"       , title: "판매상태"     , width:  80, attributes: {style: 'text-align:center;' }, headerAttributes: {style: "text-align: center;"}, sortable : false, editable:function (dataItem) {return false;}}
                        , {field: "dispYnNm"             , title: "전시여부"     , width:  60, attributes: {style: 'text-align:center;' }, headerAttributes: {style: "text-align: center;"}, sortable : false, editable:function (dataItem) {return false;}}
                        , {field: "linkUrlPc"            , title: "링크URL"      , width: 420, attributes: {style: 'text-align:left;'   }, headerAttributes: {style: "text-align: center;"}, sortable : false, editable:function (dataItem) {return false;}}
                        , {field: "text1"                , title: "노출텍스트1"  , width: 150, attributes: {style: 'text-align:left;'   }, headerAttributes: {style: "text-align: center;"}, sortable : false, editable:function (dataItem) {return false;}
                                                         , template : function(dataItem) {

                                                                        var textVal;

                                                                        if (dataItem.text1 == null || dataItem.text1 == '') {
                                                                          textVal = '';
                                                                        }
                                                                        else {
                                                                          textVal = '['
                                                                                  + dataItem.text1Color
                                                                                  + '] '
                                                                                  + dataItem.text1;
                                                                        }
                                                                        return  textVal;
                                                                      }
                          }
                        , {field: "text2"                , title: "노출텍스트2"  , width: 150, attributes: {style: 'text-align:left;'   }, headerAttributes: {style: "text-align: center;"}, sortable : false, editable:function (dataItem) {return false;}
                                                         , template : function(dataItem) {

                                                                        var textVal;

                                                                        if (dataItem.text2 == null || dataItem.text2 == '') {
                                                                          textVal = '';
                                                                        }
                                                                        else {
                                                                          textVal = '['
                                                                                  + dataItem.text2Color
                                                                                  + '] '
                                                                                  + dataItem.text2;
                                                                        }
                                                                        return  textVal;
                                                                      }
                          }
                        , {field: "text3"                , title: "노출텍스트3"  , width: 150, attributes: {style: 'text-align:left;'   }, headerAttributes: {style: "text-align: center;"}, sortable : false, editable:function (dataItem) {return false;}
                                                         , template : function(dataItem) {
                                                                        var textVal;

                                                                        if (dataItem.text3 == null || dataItem.text3 == '') {
                                                                          textVal = '';
                                                                        }
                                                                        else {
                                                                          textVal = '['
                                                                                  + dataItem.text3Color
                                                                                  + '] '
                                                                                  + dataItem.text3;
                                                                        }
                                                                        return  textVal;
                                                                      }
                          }
                        ]
      //, rowTemplate : kendo.template($("#rowTemplate").html())
      };

      contsLv2GridOpt = contsLv2GridOptGoods;
    }
    else {
      // ----------------------------------------------------------------------
      // 1.TEXT or 기본
      // ----------------------------------------------------------------------
      contsLv2GridOptText = {
          dataSource  : contsLv2GridDs
        , noRecordMsg : "검색된 목록이 없습니다."
        , navigatable : true
        , scrollable  : true
        , selectable  : true
        , sortable    : true
        , editable    : "incell"
        , resizable   : true
        , columns     : [
                          /*{selectable: true, width: 20, attributes: { style: 'text-align:center;' }, headerAttributes: {style: "text-align: center;"}}*/
                          {field: 'chk'         , headerTemplate : "<input type='checkbox' id='contsLv2CheckAll' />"
                                                                        , width: 40 , attributes: {style: "text-align:center;" }, sortable : false, editable:function (dataItem) {return false;}
                                                , template :  function(dataItem) {
                                                                return '<input type="checkbox" name="contsLv2Check" class="couponGridChk" />'
                                                              }
                                                , locked: true, lockable: false
                          }
                        , {field: "sort"        , title: "순번"         , width:  60, attributes: {style: 'text-align:center;' }, headerAttributes: {style: "text-align: center;"}, sortable : false, locked: true, editor: sortEditor}
                        , {field: "dpContsId"   , title: "콘텐츠번호"   , width:  60, attributes: {style: 'text-align:center;' }, headerAttributes: {style: "text-align: center;"}, sortable : false, editable:function (dataItem) {return false;}, locked: true}
                        , {field: "titleNm"     , title: "타이틀명"     , width: 200, attributes: {style: 'text-align:center;' }, headerAttributes: {style: "text-align: center;"}, sortable : false, editable:function (dataItem) {return false;}, locked: true}
                        , {field: "dpStartDt"   , title: "시작일"       , width: 140, attributes: {style: 'text-align:center;' }, headerAttributes: {style: "text-align: center;"}, sortable : true, editable:function (dataItem) {return false;}}
                        , {field: "dpEndDt"     , title: "종료일"       , width: 140, attributes: {style: 'text-align:center;' }, headerAttributes: {style: "text-align: center;"}, sortable : true, editable:function (dataItem) {return false;}}
                        , {field: "dpRangeTpNm" , title: "전시범위"     , width:  60, attributes: {style: 'text-align:center;' }, headerAttributes: {style: "text-align: center;"}, sortable : false, editable:function (dataItem) {return false;}}
                        , {field: "statusNm"    , title: "진행상태"     , width:  80, attributes: {style: 'text-align:center;' }, headerAttributes: {style: "text-align: center;"}, sortable : false, editable:function (dataItem) {return false;}}
                        , {field: "management"  , title: "관리"         , width: 140, attributes: {style: "text-align:center;" }, sortable : false, editable:function (dataItem) {return false;}
                                                , template :  function(dataItem) {
                                                                  var saveBtn = '<button type="button" class="btn-gray btn-s" kind="btnContsLv2Edit">수정 </button>' + '&nbsp;';
                                                                  var deleteBtn = '<button type="button" class="btn-red btn-s" kind="btnContsLv2Del">삭제 </button>';

                                                                  var returnStr = '';

                                                                  if(fnIsProgramAuth("SAVE")){
                                                                    returnStr = saveBtn;
                                                                  }
                                                                  if(fnIsProgramAuth("DELETE")){
                                                                    returnStr += deleteBtn;
                                                                  }

                                                                  return '<div id="pageMgrButtonArea" class="btn-area textCenter">' + returnStr + '</div>';
                                                }
                                                , lockable: false
                        }
                        , {field: "text1"       , title: "노출텍스트1"  , width: 150, attributes: {style: 'text-align:left;'   }, headerAttributes: {style: "text-align: center;"}, sortable : false, editable:function (dataItem) {return false;}
                                                , template :  function(dataItem) {

                                                                var textVal;

                                                                if (dataItem.text1 == null || dataItem.text1 == '') {
                                                                  textVal = '';
                                                                }
                                                                else {
                                                                  textVal = '['
                                                                          + dataItem.text1Color
                                                                          + '] '
                                                                          + dataItem.text1;
                                                                }
                                                                return  textVal;
                                                              }
                          }
                        , {field: "text2"       , title: "노출텍스트2"  , width: 150, attributes: {style: 'text-align:left;'   }, headerAttributes: {style: "text-align: center;"}, sortable : false, editable:function (dataItem) {return false;}
                                                , template :  function(dataItem) {

                                                                var textVal;

                                                                if (dataItem.text2 == null || dataItem.text2 == '') {
                                                                  textVal = '';
                                                                }
                                                                else {
                                                                  textVal = '['
                                                                          + dataItem.text2Color
                                                                          + '] '
                                                                          + dataItem.text2;
                                                                }
                                                                return  textVal;
                                                              }
                          }
                        , {field: "text3"       , title: "노출텍스트3"  , width: 150, attributes: {style: 'text-align:left;'   }, headerAttributes: {style: "text-align: center;"}, sortable : false, editable:function (dataItem) {return false;}
                                                , template :  function(dataItem) {
                                                                var textVal;

                                                                if (dataItem.text3 == null || dataItem.text3 == '') {
                                                                  textVal = '';
                                                                }
                                                                else {
                                                                  textVal = '['
                                                                          + dataItem.text3Color
                                                                          + '] '
                                                                          + dataItem.text3;
                                                                }
                                                                return  textVal;
                                                              }
                          }
                        , {field: "linkUrlPc"   , title: "링크URL"      , width: 300, attributes: {style: 'text-align:left;'   }, headerAttributes: {style: "text-align: center;"}, sortable : false, editable:function (dataItem) {return false;}}
                        ]
      //, rowTemplate : kendo.template($("#rowTemplate").html())
      };

      contsLv2GridOpt = contsLv2GridOptText;
    }

    contsLv2Grid = $('#contsLv2Grid').initializeKendoGrid( contsLv2GridOpt ).cKendoGrid();

    //$("#pageGrid").on("click", "tbody>tr", function () {
    //    //fnGridClick();
    //});
    //pageGrid.bind("dataBound", function() {
    //  $('#totalCnt').text(pageGridDs._total);
    //});

    // ------------------------------------------------------------------------
    // 그리드 버튼 클릭 이벤트 등록 - 그리드의 버튼 실행 이벤트
    // ------------------------------------------------------------------------
    // * 콘텐츠 Lv2 수정
    $('#contsLv2Grid').on("click", "button[kind=btnContsLv2Edit]", function(e) {
      e.preventDefault();
      let dataItem = contsLv2Grid.dataItem($(e.currentTarget).closest("tr"));
      selectedContsLv2Data = dataItem;
      fnBtnContsEdit(2, dataItem);
    });
    // * 콘텐츠 Lv2 삭제
    $('#contsLv2Grid').on("click", "button[kind=btnContsLv2Del]", function(e) {
      e.preventDefault();
      let dataItem = contsLv2Grid.dataItem($(e.currentTarget).closest("tr"));
      selectedContsLv2Data = dataItem;
      fnContsDelSingle(2);
      //fnBtnContsDel(2, dataItem);
    });
    // 전체체크박스
    $("#contsLv2CheckAll").on("click",function(index){
      // 개별체크박스 처리
      if($("#contsLv2CheckAll").prop("checked") == true){
        // 전체체크
        // 개별체크 선택
        $('INPUT[name=contsLv2Check]').prop("checked", true);
        // 선택삭제 버튼 활성
        $('#fnBtnContsLv2Del').data('kendoButton').enable(true);
      }
      else{
        // 전체해제
        // 개별체크 선택 해제
        $('INPUT[name=contsLv2Check]').prop("checked",false);
        // 선택삭제 버튼 비활성
        $('#fnBtnContsLv2Del').data('kendoButton').enable(false);
      }
    });
    // 개별체크박스
    $('#ng-view').on("click","input[name=contsLv2Check]",function(index){

      const totalCnt    = $("input[name=contsLv2Check]").length;
      const checkedCnt  = $("input[name=contsLv2Check]:checked").length;
      // 전체체크박스 처리
      if (totalCnt == checkedCnt) {
        $('#contsLv2CheckAll').prop("checked", true);
      }
      else {
        $('#contsLv2CheckAll').prop("checked", false);
      }
      // 선택삭제 버튼 제어
      if (checkedCnt > 0) {
        $('#fnBtnContsLv2Del').data('kendoButton').enable(true);
      }
      else {
        // 선택삭제 버튼 비활성
        $('#fnBtnContsLv2Del').data('kendoButton').enable(false);
      }
    });

  }

  // ==========================================================================
  // # 콘텐츠Lv.3 그리드
  // ==========================================================================
  function fnInitContsLv3Grid(){
    //console.log('# fnInitContsLv3Grid Start');

    // 선택데이터 초기화
    selectedContsLv3Data = null;
    // 선택된 Data가 콘텐츠Lv.1 그리드이면 초기화
    if (selectedContsData != undefined && selectedContsData != null && selectedContsData != '') {
      if (selectedContsData.contsLevel == 3) {
        selectedContsData = null;
      }
    }

    // 콘텐츠그리드 초기화
    if ($("#contsLv3Grid").data("kendoGrid") != undefined ) {
      $("#contsLv3Grid").data("kendoGrid").destroy();
      $("#contsLv3Grid").empty();
    }

    // 콘텐츠타입
    var tmpContsTp = '';
    //console.log('# selectedInventoryData :: ' + JSON.stringify(selectedInventoryData));
    if (selectedInventoryData != null && selectedInventoryData.contsLevel1Tp != null && selectedInventoryData.contsLevel2Tp != null && selectedInventoryData.contsLevel3Tp != null) {
      tmpContsTp = selectedInventoryData.contsLevel3Tp;
    }
    //console.log('### Lv3 tmpContsTp :: ', tmpContsTp);

    var inParamContsLevel     = 3;
    var inParamDpInventoryId;   // selectedInventoryData.dpInventoryId
    var inParamPrntsContsId;
    var inParamDpStartDt;
    var inParamDpEndDt;
    var inParamDpRangeTp      = $('#contsLv3DpRangeTp').val();
    var inParamIlCtgryId      = '';
    var inParamStatus         = $('#contsLv3Status').val();
    var inParamContsTp        = '';

    var inParamGoodsConds     = '';

    if (selectedInventoryData != undefined && selectedInventoryData != null) {
      //console.log('# selectedInventoryData :: ' + JSON.stringify(selectedInventoryData));
      // 인벤토리ID
      if (selectedInventoryData.dpInventoryId != undefined && selectedInventoryData.dpInventoryId != null) {
        inParamDpInventoryId  = selectedInventoryData.dpInventoryId;
      }

      // 콘텐츠유형
      if (selectedInventoryData.contsLevel3Tp != null) {
        inParamContsTp = selectedInventoryData.contsLevel3Tp;
      }

      // 카테고리ID
      if (selectedInventoryData.pageTp == 'PAGE_TP.CATEGORY') {
        // 페이지유형:카테고리
        if (selectedPageData != undefined && selectedPageData != null) {
          // 선택페이지정보존재
          if (selectedPageData.dpPageId != undefined && selectedPageData.dpPageId != null) {
            inParamIlCtgryId      = selectedPageData.dpPageId;
          }
        }
      }
    }
    else {
      //console.log('# selectedInventoryData is Null');
    }
    // 상위콘텐츠ID
    if (selectedContsLv2Data != undefined && selectedContsLv2Data != null) {
      if (selectedContsLv2Data.dpContsId != undefined && selectedContsLv2Data.dpContsId != null) {
        inParamPrntsContsId  = selectedContsLv2Data.dpContsId;
      }
    }
    else {
      //console.log('# selectedContsLv2Data is Null');
    }

    // 검색기간 조건 - 시작
    if ($('#dpStartDtLv3').val() == null || $('#dpStartDtLv3').val() == undefined || $('#dpStartDtLv3').val() == '') {
      inParamDpStartDt = '';
    }
    else {
      inParamDpStartDt = ($('#dpStartDtLv3').val()).replace(/-/gi, '') + $('#dpStartHourPickerLv3').val() + $('#dpStartMinPickerLv3').val() + '00';
    }
    // 검색기간 조건 - 종료
    if ($('#dpEndDtLv3').val() == null || $('#dpEndDtLv3').val() == undefined || $('#dpEndDtLv3').val() == '') {
      // 년월일이 공백이면 종료일 조건 없음
      inParamDpEndDt = '';
    }
    else {
      inParamDpEndDt = ($('#dpEndDtLv3').val()).replace(/-/gi, '') + $('#dpEndHourPickerLv3').val() + $('#dpEndMinPickerLv3').val() + '59';
    }

    // 상품인경우 조건 추가
    if(tmpContsTp == 'DP_CONTENTS_TP.GOODS') {
      inParamGoodsConds = ''
                        + "&dpCondTp="        + $('#contLv3DpCondTp').val()
                        + "&dpSortTp="        + $('#contLv3DpSortTp').val();
    }

    // 페이징없는 그리드
    contsLv3GridDs = fnGetDataSource({
      url      : "/admin/display/manage/selectDpContsList?" + "dpInventoryId="    + inParamDpInventoryId
                                                            + "&prntsContsId="    + inParamPrntsContsId
                                                            + "&contsLevel="      + inParamContsLevel
                                                            + "&dpStartDt="       + inParamDpStartDt
                                                            + "&dpEndDt="         + inParamDpEndDt
                                                            + "&dpRangeTp="       + inParamDpRangeTp
                                                            + "&ilCtgryId="       + inParamIlCtgryId
                                                            + "&status="          + inParamStatus
                                                            + "&contsTp="         + inParamContsTp
                                                            + inParamGoodsConds
    });

    // ------------------------------------------------------------------------
    // 콘텐츠유형별 처리
    // ------------------------------------------------------------------------
    if(tmpContsTp == 'DP_CONTENTS_TP.HTML') {
      // ----------------------------------------------------------------------
      // 2.HTML
      // ----------------------------------------------------------------------
      contsLv3GridOptHtml = {
          dataSource  : contsLv3GridDs
        , noRecordMsg : "검색된 목록이 없습니다."
        , navigatable : true
        , scrollable  : true
        , selectable  : true
        , sortable    : true
        , editable    : "incell"
        , resizable   : true
        , columns     : [
                          {field: 'chk'        , headerTemplate : "<input type='checkbox' id='contsLv3CheckAll' />"
                                                                          , width:  40, attributes: {style: "text-align:center;" }, sortable : false, editable:function (dataItem) {return false;}
                                               , template :  function(dataItem) {
                                                               return '<input type="checkbox" name="contsLv3Check" class="couponGridChk" />'
                                                             }
                                               , locked: true, lockable: false
                          }
                        , {field: "sort"       , title: "순번"            , width:  60, attributes: {style: 'text-align:center;' }, headerAttributes: {style: "text-align: center;"}, sortable : false, locked: true, editor: sortEditor}
                        , {field: "dpContsId"  , title: "콘텐츠번호"      , width:  60, attributes: {style: 'text-align:center;' }, headerAttributes: {style: "text-align: center;"}, sortable : false, editable:function (dataItem) {return false;}, locked: true}
                        , {field: "titleNm"    , title: "타이틀명"        , width: 200, attributes: {style: 'text-align:center;' }, headerAttributes: {style: "text-align: center;"}, sortable : false, editable:function (dataItem) {return false;}, locked: true}
                        , {field: "dpStartDt"  , title: "시작일"          , width: 140, attributes: {style: 'text-align:center;' }, headerAttributes: {style: "text-align: center;"}, sortable : true, editable:function (dataItem) {return false;}}
                        , {field: "dpEndDt"    , title: "종료일"          , width: 140, attributes: {style: 'text-align:center;' }, headerAttributes: {style: "text-align: center;"}, sortable : true, editable:function (dataItem) {return false;}}
                        , {field: "dpRangeTpNm", title: "전시범위"        , width:  60, attributes: {style: 'text-align:center;' }, headerAttributes: {style: "text-align: center;"}, sortable : false, editable:function (dataItem) {return false;}}
                        , {field: "statusNm"   , title: "진행상태"        , width:  80, attributes: {style: 'text-align:center;' }, headerAttributes: {style: "text-align: center;"}, sortable : false, editable:function (dataItem) {return false;}}
                        , {field: "management" , title: "관리"            , width: 140, attributes: {style: "text-align:center;" }, sortable : false, editable:function (dataItem) {return false;}
                                                , template :  function(dataItem) {

                                                                    var saveBtn = '<button type="button" class="btn-gray btn-s" kind="btnContsLv3Edit">수정 </button>' + '&nbsp;';
                                                                    var deleteBtn = '<button type="button" class="btn-red btn-s" kind="btnContsLv3Del">삭제 </button>';

                                                                    var returnStr = '';

                                                                    if(fnIsProgramAuth("SAVE")){
                                                                      returnStr = saveBtn;
                                                                    }
                                                                    if(fnIsProgramAuth("DELETE")){
                                                                      returnStr += deleteBtn;
                                                                    }

                                                                    return '<div id="pageMgrButtonArea" class="btn-area textCenter">' + returnStr + '</div>';
                                                }
                                                , lockable: false
                        }
                        , {field: "htmlMobile" , title: "Mobile"          , width:  90, attributes: {style: 'text-align:left;'   }, headerAttributes: {style: "text-align: center;"}, sortable : false, editable:function (dataItem) {return false;}
                                               , template :  function(dataItem) {
                                                               if (dataItem.htmlMobile == null || dataItem.htmlMobile == '') {
                                                                 return '';
                                                               }
                                                               else {
                                                                 return  '<div id="pageMgrButtonArea" class="btn-area textCenter">'
                                                                       + '<button type="button" class="btn-gray btn-s" kind="btnContsPreviewHtmlMobile">미리보기 </button>'
                                                                       + '</div>';
                                                               }
                                                             }
                          }
                        , {field: "htmlPc"     , title: "PC"              , width:  90, attributes: {style: 'text-align:left;'   }, headerAttributes: {style: "text-align: center;"}, sortable : false, editable:function (dataItem) {return false;}
                                               , template :  function(dataItem) {
                                                               if (dataItem.htmlPc == null || dataItem.htmlPc == '') {
                                                                 return '';
                                                               }
                                                               else {
                                                                 return  '<div id="pageMgrButtonArea" class="btn-area textCenter">'
                                                                       + '<button type="button" class="btn-gray btn-s" kind="btnContsPreviewHtmlPc">미리보기 </button>'
                                                                       + '</div>';
                                                               }
                                                             }
                          }
                        ]
      //, rowTemplate : kendo.template($("#rowTemplate").html())
      };

      contsLv3GridOpt = contsLv3GridOptHtml;

      // ----------------------------------------------------------------------
      // Lv.3-배너-미리보기 클릭이벤트
      // ----------------------------------------------------------------------
      // HTML-PC-Mobile
      $('#contsLv3Grid').on("click", "button[kind=btnContsPreviewHtmlMobile]", function(e) {
        e.preventDefault();
        e.stopImmediatePropagation();
        let dataItem = contsLv3Grid.dataItem($(e.currentTarget).closest("tr"));
         // 미리보기 팝업오픈
        var html    = dataItem.htmlMobile;
        fnInitPopupImagePreview(html, '', 'HTML');
      });
      // HTML-PC
      $('#contsLv3Grid').on("click", "button[kind=btnContsPreviewHtmlPc]", function(e) {
        e.preventDefault();
        e.stopImmediatePropagation();
        let dataItem = contsLv3Grid.dataItem($(e.currentTarget).closest("tr"));
        // 미리보기 팝업오픈
        var html    = dataItem.htmlPc;
        fnInitPopupImagePreview(html, '', 'HTML');
      });

    }
    else if(tmpContsTp == 'DP_CONTENTS_TP.BANNER') {
      // ----------------------------------------------------------------------
      // 3.BANNER
      // ----------------------------------------------------------------------
      contsLv3GridOptBanner = {
            dataSource  : contsLv3GridDs
          , noRecordMsg : "검색된 목록이 없습니다."
          , navigatable : true
          , scrollable  : true
          , selectable  : true
          , sortable    : true
          , editable    : "incell"
          , resizable   : true
          , columns     : [
                            {field: 'chk'             , headerTemplate : "<input type='checkbox' id='contsLv3CheckAll' />"
                                                                              , width:  40, attributes: {style: "text-align:center;" }, sortable : false, editable:function (dataItem) {return false;}
                                                      , template :  function(dataItem) {
                                                                      return '<input type="checkbox" name="contsLv3Check" class="couponGridChk" />'
                                                                    }
                                                      , locked: true, lockable: false
                            }
                          , {field: "sort"            , title: "순번"         , width:  60, attributes: {style: 'text-align:center;' }, headerAttributes: {style: "text-align: center;"}, sortable : false, locked: true, editor: sortEditor}
                          , {field: "dpContsId"       , title: "콘텐츠번호"   , width:  60, attributes: {style: 'text-align:center;' }, headerAttributes: {style: "text-align: center;"}, sortable : false, editable:function (dataItem) {return false;}, locked: true}
                          , {field: "titleNm"         , title: "타이틀명"     , width: 200, attributes: {style: 'text-align:center;' }, headerAttributes: {style: "text-align: center;"}, sortable : false, editable:function (dataItem) {return false;}, locked: true}
                          , {field: "dpStartDt"       , title: "시작일"       , width: 140, attributes: {style: 'text-align:center;' }, headerAttributes: {style: "text-align: center;"}, sortable : true, editable:function (dataItem) {return false;}}
                          , {field: "dpEndDt"         , title: "종료일"       , width: 140, attributes: {style: 'text-align:center;' }, headerAttributes: {style: "text-align: center;"}, sortable : true, editable:function (dataItem) {return false;}}
                          , {field: "dpRangeTpNm"     , title: "전시범위"     , width:  60, attributes: {style: 'text-align:center;' }, headerAttributes: {style: "text-align: center;"}, sortable : false, editable:function (dataItem) {return false;}}
                          , {field: "statusNm"        , title: "진행상태"     , width:  80, attributes: {style: 'text-align:center;' }, headerAttributes: {style: "text-align: center;"}, sortable : false, editable:function (dataItem) {return false;}}
                          , {field: "management"      , title: "관리"         , width: 140, attributes: {style: "text-align:center;" }, sortable : false, editable:function (dataItem) {return false;}
                                                      , template :  function(dataItem) {

                                                                        var saveBtn = '<button type="button" class="btn-gray btn-s" kind="btnContsLv3Edit">수정 </button>' + '&nbsp;';
                                                                        var deleteBtn = '<button type="button" class="btn-red btn-s" kind="btnContsLv3Del">삭제 </button>';

                                                                        var returnStr = '';

                                                                        if(fnIsProgramAuth("SAVE")){
                                                                          returnStr = saveBtn;
                                                                        }
                                                                        if(fnIsProgramAuth("DELETE")){
                                                                          returnStr += deleteBtn;
                                                                        }

                                                                        return '<div id="pageMgrButtonArea" class="btn-area textCenter">' + returnStr + '</div>';
                                                      }
                                                      , lockable: false
                          }
                          , {field: "imgPathMobile"   , title: "이미지1"      , width:  90, attributes: {style: 'text-align:left;'   }, headerAttributes: {style: "text-align: center;"}, sortable : false, editable:function (dataItem) {return false;}
                                                      , template :  function(dataItem) {
                                                                      if (dataItem.imgPathMobile == null || dataItem.imgPathMobile == '') {
                                                                        return '';
                                                                      }
                                                                      else {
                                                                        return  '<div id="pageMgrButtonArea" class="btn-area textCenter">'
                                                                              + '<button type="button" class="btn-gray btn-s" kind="btnContsPreviewImgMobile">미리보기 </button>'
                                                                              + '</div>';
                                                                      }
                                                                    }
                            }
                          , {field: "imgPathPc"       , title: "이미지2"      , width:  90, attributes: {style: 'text-align:left;'   }, headerAttributes: {style: "text-align: center;"}, sortable : false, editable:function (dataItem) {return false;}
                                                      , template :  function(dataItem) {
                                                                      if (dataItem.imgPathPc == null || dataItem.imgPathPc == '') {
                                                                        return '';
                                                                      }
                                                                      else {
                                                                        return  '<div id="pageMgrButtonArea" class="btn-area textCenter">'
                                                                              + '<button type="button" class="btn-gray btn-s" kind="btnContsPreviewImgPc">미리보기 </button>'
                                                                              + '</div>';
                                                                      }
                                                                    }
                            }
                          , {field: "gifImgPathMobile", title: "Mobile gif"   , width:  90, attributes: {style: 'text-align:left;'   }, headerAttributes: {style: "text-align: center;"}, sortable : false, editable:function (dataItem) {return false;}
                                                      , template :  function(dataItem) {
                                                                      if (dataItem.gifImgPathMobile == null || dataItem.gifImgPathMobile == '') {
                                                                        return '';
                                                                      }
                                                                      else {
                                                                        return  '<div id="pageMgrButtonArea" class="btn-area textCenter">'
                                                                              + '<button type="button" class="btn-gray btn-s" kind="btnContsPreviewImgGifMobile">미리보기 </button>'
                                                                              + '</div>';
                                                                      }
                                                                    }
                            }
                          , {field: "gifImgPathPc"    , title: "PC gif"       , width:  90, attributes: {style: 'text-align:left;'   }, headerAttributes: {style: "text-align: center;"}, sortable : false, editable:function (dataItem) {return false;}
                                                      , template :  function(dataItem) {
                                                                      if (dataItem.gifImgPathPc == null || dataItem.gifImgPathPc == '') {
                                                                        return '';
                                                                      }
                                                                      else {
                                                                        return  '<div id="pageMgrButtonArea" class="btn-area textCenter">'
                                                                              + '<button type="button" class="btn-gray btn-s" kind="btnContsPreviewImgGifPc">미리보기 </button>'
                                                                              + '</div>';
                                                                      }
                                                                    }
                        }
                          , {field: "text1"           , title: "노출텍스트1"  , width: 150, attributes: {style: 'text-align:left;'   }, headerAttributes: {style: "text-align: center;"}, sortable : false, editable:function (dataItem) {return false;}
                                                      , template :  function(dataItem) {

                                                                      var textVal;

                                                                      if (dataItem.text1 == null || dataItem.text1 == '') {
                                                                        textVal = '';
                                                                      }
                                                                      else {
                                                                        textVal = '['
                                                                                + dataItem.text1Color
                                                                                + '] '
                                                                                + dataItem.text1;
                                                                      }
                                                                      return  textVal;
                                                                    }
                            }
                          , {field: "text2"           , title: "노출텍스트2"  , width: 150, attributes: {style: 'text-align:left;'   }, headerAttributes: {style: "text-align: center;"}, sortable : false, editable:function (dataItem) {return false;}
                                                      , template :  function(dataItem) {

                                                                      var textVal;

                                                                      if (dataItem.text2 == null || dataItem.text2 == '') {
                                                                        textVal = '';
                                                                      }
                                                                      else {
                                                                        textVal = '['
                                                                                + dataItem.text2Color
                                                                                + '] '
                                                                                + dataItem.text2;
                                                                      }
                                                                      return  textVal;
                                                                    }
                            }
                          , {field: "text3"           , title: "노출텍스트3"  , width: 150, attributes: {style: 'text-align:left;'   }, headerAttributes: {style: "text-align: center;"}, sortable : false, editable:function (dataItem) {return false;}
                                                      , template :  function(dataItem) {
                                                                      var textVal;

                                                                      if (dataItem.text3 == null || dataItem.text3 == '') {
                                                                        textVal = '';
                                                                      }
                                                                      else {
                                                                        textVal = '['
                                                                                + dataItem.text3Color
                                                                                + '] '
                                                                                + dataItem.text3;
                                                                      }
                                                                      return  textVal;
                                                                    }
                            }
                          ]
        //, rowTemplate : kendo.template($("#rowTemplate").html())
        };

      contsLv3GridOpt = contsLv3GridOptBanner;

      // ----------------------------------------------------------------------
      // Lv.3-배너-미리보기 클릭이벤트
      // ----------------------------------------------------------------------
      // 이미지1
      $('#contsLv3Grid').on("click", "button[kind=btnContsPreviewImgMobile]", function(e) {
        e.preventDefault();
        e.stopImmediatePropagation();
        let dataItem = contsLv3Grid.dataItem($(e.currentTarget).closest("tr"));
        //selectedContsLv1Data = dataItem;
        //console.log('# Lv1. dataItem :: ' + JSON.stringify(dataItem));
        //alert('# Lv.1 이미지1 Click');
        // 미리보기 팝업오픈
        var path    = dataItem.imgPathMobile;
        var fileNm  = dataItem.imgOriginNmMobile;
        fnInitPopupImagePreview(path, fileNm, 'IMAGE');
      });
      // gif이미지모바일
      $('#contsLv3Grid').on("click", "button[kind=btnContsPreviewImgGifMobile]", function(e) {
        e.preventDefault();
        e.stopImmediatePropagation();
        let dataItem = contsLv3Grid.dataItem($(e.currentTarget).closest("tr"));
        //selectedContsLv1Data = dataItem;
        //console.log('# Lv1. dataItem :: ' + JSON.stringify(dataItem));
        //alert('# Lv.1 이미지1 Click');
        // 미리보기 팝업오픈
        var path    = dataItem.gifImgPathMobile;
        var fileNm  = dataItem.gifImgOrginNmMobile;
        fnInitPopupImagePreview(path, fileNm, 'IMAGE');
      });
      // 이미지2
      $('#contsLv3Grid').on("click", "button[kind=btnContsPreviewImgPc]", function(e) {
        e.preventDefault();
        e.stopImmediatePropagation();
        let dataItem = contsLv3Grid.dataItem($(e.currentTarget).closest("tr"));
        //selectedContsLv1Data = dataItem;
        //console.log('# Lv1. dataItem :: ' + JSON.stringify(dataItem));
        //alert('# Lv.1 이미지3 Click');
        // 미리보기 팝업오픈
        var path    = dataItem.imgPathPc;
        var fileNm  = dataItem.imgOriginNmPc;
        fnInitPopupImagePreview(path, fileNm, 'IMAGE');
      });
      // gif이미지PC
      $('#contsLv3Grid').on("click", "button[kind=btnContsPreviewImgGifPc]", function(e) {
        e.preventDefault();
        e.stopImmediatePropagation();
        let dataItem = contsLv3Grid.dataItem($(e.currentTarget).closest("tr"));
        //selectedContsLv1Data = dataItem;
        //console.log('# Lv1. dataItem :: ' + JSON.stringify(dataItem));
        //alert('# Lv.1 이미지1 Click');
        // 미리보기 팝업오픈
        var path    = dataItem.gifImgPathPc;
        var fileNm  = dataItem.gifImgOrginNmPc;
        fnInitPopupImagePreview(path, fileNm, 'IMAGE');
      });
    }
    else if(tmpContsTp == 'DP_CONTENTS_TP.BRAND') {
      // ----------------------------------------------------------------------
      // 4.BRAND
      // ----------------------------------------------------------------------
      contsLv3GridOptBrand = {
          dataSource  : contsLv3GridDs
        , noRecordMsg : "검색된 목록이 없습니다."
        , navigatable : true
        , scrollable  : true
        , selectable  : true
        , sortable    : true
        , editable    : "incell"
        , resizable   : true
        , columns     : [
                          {field: 'chk'           , headerTemplate : "<input type='checkbox' id='contsLv3CheckAll' />"
                                                                           , width:  40, attributes: {style: "text-align:center;" }, sortable : false, editable:function (dataItem) {return false;}
                                                  , template :  function(dataItem) {
                                                                  return '<input type="checkbox" name="contsLv3Check" class="couponGridChk" />'
                                                                }
                                                  , locked: true, lockable: false
                          }
                        , {field: "sort"          , title: "순번"          , width:  60, attributes: {style: 'text-align:center;' }, headerAttributes: {style: "text-align: center;"}, sortable : false, locked: true, editor: sortEditor}
                        , {field: "dpContsId"     , title: "콘텐츠번호"    , width:  60, attributes: {style: 'text-align:center;' }, headerAttributes: {style: "text-align: center;"}, sortable : false, editable:function (dataItem) {return false;}, locked: true}
                        , {field: "titleNm"       , title: "타이틀명"      , width: 200, attributes: {style: 'text-align:center;' }, headerAttributes: {style: "text-align: center;"}, sortable : false, editable:function (dataItem) {return false;}, locked: true}
                        , {field: "contsNm"       , title: "전시브랜드명"      , width: 200, attributes: {style: 'text-align:center;' }, headerAttributes: {style: "text-align: center;"}, sortable : false, editable:function (dataItem) {return false;}}
                        , {field: "dpStartDt"     , title: "시작일"        , width: 140, attributes: {style: 'text-align:center;' }, headerAttributes: {style: "text-align: center;"}, sortable : true, editable:function (dataItem) {return false;}}
                        , {field: "dpEndDt"       , title: "종료일"        , width: 140, attributes: {style: 'text-align:center;' }, headerAttributes: {style: "text-align: center;"}, sortable : true, editable:function (dataItem) {return false;}}
                        , {field: "dpRangeTpNm"   , title: "전시범위"      , width:  60, attributes: {style: 'text-align:center;' }, headerAttributes: {style: "text-align: center;"}, sortable : false, editable:function (dataItem) {return false;}}
                        , {field: "statusNm"      , title: "진행상태"      , width:  80, attributes: {style: 'text-align:center;' }, headerAttributes: {style: "text-align: center;"}, sortable : false, editable:function (dataItem) {return false;}}
                        , {field: "management"    , title: "관리"            , width: 140, attributes: {style: "text-align:center;" }, sortable : false, editable:function (dataItem) {return false;}
                                                  , template :  function(dataItem) {

                                                                    var saveBtn = '<button type="button" class="btn-gray btn-s" kind="btnContsLv3Edit">수정 </button>' + '&nbsp;';
                                                                    var deleteBtn = '<button type="button" class="btn-red btn-s" kind="btnContsLv3Del">삭제 </button>';

                                                                    var returnStr = '';

                                                                    if(fnIsProgramAuth("SAVE")){
                                                                      returnStr = saveBtn;
                                                                    }
                                                                    if(fnIsProgramAuth("DELETE")){
                                                                      returnStr += deleteBtn;
                                                                    }

                                                                    return '<div id="pageMgrButtonArea" class="btn-area textCenter">' + returnStr + '</div>';
                                                  }
                                                  , lockable: false
                        }
                        , {field: "imgPathMobile" , title: "이미지1"       , width:  90, attributes: {style: 'text-align:left;'   }, headerAttributes: {style: "text-align: center;"}, sortable : false, editable:function (dataItem) {return false;}
                                                  , template :  function(dataItem) {
                                                                  if (dataItem.imgPathMobile == null || dataItem.imgPathMobile == '') {
                                                                    return '';
                                                                  }
                                                                  else {
                                                                    return  '<div id="pageMgrButtonArea" class="btn-area textCenter">'
                                                                          + '<button type="button" class="btn-gray btn-s" kind="btnContsPreviewImgMobile">미리보기 </button>'
                                                                          + '</div>';
                                                                  }
                                                                }
                          }
                        , {field: "imgPathPc"     , title: "이미지2"       , width:  90, attributes: {style: 'text-align:left;'   }, headerAttributes: {style: "text-align: center;"}, sortable : false, editable:function (dataItem) {return false;}
                                                  , template :  function(dataItem) {
                                                                  if (dataItem.imgPathPc == null || dataItem.imgPathPc == '') {
                                                                    return '';
                                                                  }
                                                                  else {
                                                                    return  '<div id="pageMgrButtonArea" class="btn-area textCenter">'
                                                                          + '<button type="button" class="btn-gray btn-s" kind="btnContsPreviewImgPc">미리보기 </button>'
                                                                          + '</div>';
                                                                  }
                                                                }
                          }

                        , {field: "text1"         , title: "노출텍스트1"   , width: 150, attributes: {style: 'text-align:left;'   }, headerAttributes: {style: "text-align: center;"}, sortable : false, editable:function (dataItem) {return false;}
                                                  , template : function(dataItem) {

                                                                 var textVal;

                                                                 if (dataItem.text1 == null || dataItem.text1 == '') {
                                                                   textVal = '';
                                                                 }
                                                                 else {
                                                                   textVal = '['
                                                                           + dataItem.text1Color
                                                                           + '] '
                                                                           + dataItem.text1;
                                                                 }
                                                                 return  textVal;
                                                               }
                          }
                        , {field: "text2"         , title: "노출텍스트2"   , width: 150, attributes: {style: 'text-align:left;'   }, headerAttributes: {style: "text-align: center;"}, sortable : false, editable:function (dataItem) {return false;}
                                                  , template : function(dataItem) {

                                                                 var textVal;

                                                                 if (dataItem.text2 == null || dataItem.text2 == '') {
                                                                   textVal = '';
                                                                 }
                                                                 else {
                                                                   textVal = '['
                                                                           + dataItem.text2Color
                                                                           + '] '
                                                                           + dataItem.text2;
                                                                 }
                                                                 return  textVal;
                                                               }
                          }
                        , {field: "text3"         , title: "노출텍스트3"   , width: 150, attributes: {style: 'text-align:left;'   }, headerAttributes: {style: "text-align: center;"}, sortable : false, editable:function (dataItem) {return false;}
                                                  , template : function(dataItem) {
                                                                 var textVal;

                                                                 if (dataItem.text3 == null || dataItem.text3 == '') {
                                                                   textVal = '';
                                                                 }
                                                                 else {
                                                                   textVal = '['
                                                                           + dataItem.text3Color
                                                                           + '] '
                                                                           + dataItem.text3;
                                                                 }
                                                                 return  textVal;
                                                               }
                          }
                        ]
      //, rowTemplate : kendo.template($("#rowTemplate").html())
      };

      contsLv3GridOpt = contsLv3GridOptBrand;

      // ----------------------------------------------------------------------
      // Lv.1-브랜드-미리보기 클릭이벤트
      // ----------------------------------------------------------------------
      // 이미지1
      $('#contsLv3Grid').on("click", "button[kind=btnContsPreviewImgMobile]", function(e) {
        e.preventDefault();
        e.stopImmediatePropagation();
        let dataItem = contsLv3Grid.dataItem($(e.currentTarget).closest("tr"));
        //selectedContsLv1Data = dataItem;
        //console.log('# Lv3. dataItem :: ' + JSON.stringify(dataItem));
        //alert('# Lv.3 이미지1 Click');
        // 미리보기 팝업오픈
        var path    = dataItem.imgPathMobile;
        var fileNm  = dataItem.imgOriginNmMobile;
        fnInitPopupImagePreview(path, fileNm, 'IMAGE');
      });
      // 이미지2
      $('#contsLv3Grid').on("click", "button[kind=btnContsPreviewImgPc]", function(e) {
        e.preventDefault();
        e.stopImmediatePropagation();
        let dataItem = contsLv3Grid.dataItem($(e.currentTarget).closest("tr"));
        //selectedContsLv1Data = dataItem;
        //console.log('# Lv3. dataItem :: ' + JSON.stringify(dataItem));
        //alert('# Lv.3 이미지2 Click');
        // 미리보기 팝업오픈
        var path    = dataItem.imgPathPc;
        var fileNm  = dataItem.imgOriginNmPc;
        fnInitPopupImagePreview(path, fileNm, 'IMAGE');
      });
    }
    else if(tmpContsTp == 'DP_CONTENTS_TP.CATEGORY') {
      // ----------------------------------------------------------------------
      // 5.CATEGORY
      // ----------------------------------------------------------------------
      contsLv3GridOptCategory = {
          dataSource  : contsLv3GridDs
        , noRecordMsg : "검색된 목록이 없습니다."
        , navigatable : true
        , scrollable  : true
        , selectable  : true
        , sortable    : true
        , editable    : "incell"
        , resizable   : true
        , columns     : [
                          {field: 'chk'        , headerTemplate : "<input type='checkbox' id='contsLv3CheckAll' />"
                                                                        , width:  40, attributes: {style: "text-align:center;" }, sortable : false, editable:function (dataItem) {return false;}
                                               , template :  function(dataItem) {
                                                               return'<input type="checkbox" name="contsLv3Check" class="couponGridChk" />'
                                                             }
                                               , locked: true, lockable: false
                          }
                        , {field: "sort"       , title: "순번"          , width:  60, attributes: {style: 'text-align:center;' }, headerAttributes: {style: "text-align: center;"}, sortable : false, locked: true, editor: sortEditor}
                        , {field: "dpContsId"  , title: "콘텐츠번호"    , width:  60, attributes: {style: 'text-align:center;' }, headerAttributes: {style: "text-align: center;"}, sortable : false, editable:function (dataItem) {return false;}, locked: true}
                        , {field: "titleNm"    , title: "타이틀명"      , width: 200, attributes: {style: 'text-align:center;' }, headerAttributes: {style: "text-align: center;"}, sortable : false, editable:function (dataItem) {return false;}, locked: true}
                        , {field: "dpStartDt"  , title: "시작일"        , width: 140, attributes: {style: 'text-align:center;' }, headerAttributes: {style: "text-align: center;"}, sortable : true, editable:function (dataItem) {return false;}}
                        , {field: "dpEndDt"    , title: "종료일"        , width: 140, attributes: {style: 'text-align:center;' }, headerAttributes: {style: "text-align: center;"}, sortable : true, editable:function (dataItem) {return false;}}
                        , {field: "dpRangeTpNm", title: "전시범위"      , width:  60, attributes: {style: 'text-align:center;' }, headerAttributes: {style: "text-align: center;"}, sortable : false, editable:function (dataItem) {return false;}}
                        , {field: "statusNm"   , title: "진행상태"      , width:  80, attributes: {style: 'text-align:center;' }, headerAttributes: {style: "text-align: center;"}, sortable : false, editable:function (dataItem) {return false;}}
                        , {field: "management" , title: "관리"            , width: 140, attributes: {style: "text-align:center;" }, sortable : false, editable:function (dataItem) {return false;}
                                                , template :  function(dataItem) {

                                                                  var saveBtn = '<button type="button" class="btn-gray btn-s" kind="btnContsLv3Edit">수정 </button>' + '&nbsp;';
                                                                  var deleteBtn = '<button type="button" class="btn-red btn-s" kind="btnContsLv3Del">삭제 </button>';

                                                                  var returnStr = '';

                                                                  if(fnIsProgramAuth("SAVE")){
                                                                    returnStr = saveBtn;
                                                                  }
                                                                  if(fnIsProgramAuth("DELETE")){
                                                                    returnStr += deleteBtn;
                                                                  }

                                                                  return '<div id="pageMgrButtonArea" class="btn-area textCenter">' + returnStr + '</div>';
                                                }
                                                , lockable: false
                        }
                        , {field: "mallDivNm"  , title: "몰인몰구분"    , width: 100, attributes: {style: 'text-align:center;' }, headerAttributes: {style: "text-align: center;"}, sortable : false, editable:function (dataItem) {return false;}}
                        , {field: "ctgryFullNm", title: "전시카테고리"  , width: 300, attributes: {style: 'text-align:center;' }, headerAttributes: {style: "text-align: left;"}  , sortable : false, editable:function (dataItem) {return false;}}
                        , {field: "text1"      , title: "노출텍스트1"   , width: 150, attributes: {style: 'text-align:left;'   }, headerAttributes: {style: "text-align: center;"}, sortable : false, editable:function (dataItem) {return false;}
                                               , template : function(dataItem) {

                                                              var textVal;

                                                              if (dataItem.text1 == null || dataItem.text1 == '') {
                                                                textVal = '';
                                                              }
                                                              else {
                                                                textVal = '['
                                                                        + dataItem.text1Color
                                                                        + '] '
                                                                        + dataItem.text1;
                                                              }
                                                              return  textVal;
                                                            }
                          }
                        , {field: "text2"      , title: "노출텍스트2"   , width: 150, attributes: {style: 'text-align:left;'   }, headerAttributes: {style: "text-align: center;"}, sortable : false, editable:function (dataItem) {return false;}
                                               , template : function(dataItem) {

                                                              var textVal;

                                                              if (dataItem.text2 == null || dataItem.text2 == '') {
                                                                textVal = '';
                                                              }
                                                              else {
                                                                textVal = '['
                                                                        + dataItem.text2Color
                                                                        + '] '
                                                                        + dataItem.text2;
                                                              }
                                                              return  textVal;
                                                            }
                          }
                        , {field: "text3"      , title: "노출텍스트3"   , width: 150, attributes: {style: 'text-align:left;'   }, headerAttributes: {style: "text-align: center;"}, sortable : false, editable:function (dataItem) {return false;}
                                               , template : function(dataItem) {
                                                              var textVal;

                                                              if (dataItem.text3 == null || dataItem.text3 == '') {
                                                                textVal = '';
                                                              }
                                                              else {
                                                                textVal = '['
                                                                        + dataItem.text3Color
                                                                        + '] '
                                                                        + dataItem.text3;
                                                              }
                                                              return  textVal;
                                                            }
                          }
                        ]
      //, rowTemplate : kendo.template($("#rowTemplate").html())
      };

      contsLv3GridOpt = contsLv3GridOptCategory;
    }
    else if(tmpContsTp == 'DP_CONTENTS_TP.GOODS') {
      // ----------------------------------------------------------------------
      // GOODS
      // ----------------------------------------------------------------------
      contsLv3GridOptGoods = {
          dataSource  : contsLv3GridDs
        , noRecordMsg : "검색된 목록이 없습니다."
        , navigatable : true
        , scrollable  : true
        , selectable  : true
        , sortable    : true
        , editable    : "incell"
        , resizable   : true
        , columns     : [
                          {field: 'chk'                  , headerTemplate : "<input type='checkbox' id='contsLv3CheckAll' />"
                                                                                 , width:  40, attributes: {style: "text-align:center;" }, sortable : false, editable:function (dataItem) {return false;}
                                                         , template :  function(dataItem) {
                                                                         return '<input type="checkbox" name="contsLv3Check" class="couponGridChk" />'
                                                                       }
                                                         , locked: true, lockable: false
                          }
                        , {field: "sort"                 , title: "순번"         , width:  60, attributes: {style: 'text-align:center;' }, headerAttributes: {style: "text-align: center;"}, sortable : false, locked: true, editor: sortEditor}
                        , {field: "dpContsId"            , title: "콘텐츠번호"   , width:  60, attributes: {style: 'text-align:center;' }, headerAttributes: {style: "text-align: center;"}, sortable : false, editable:function (dataItem) {return false;}, locked: true}
                        , {field: "titleNm"              , title: "타이틀명"     , width: 200, attributes: {style: 'text-align:center;' }, headerAttributes: {style: "text-align: center;"}, sortable : false, editable:function (dataItem) {return false;}, locked: true}
                        , {field: "contsId"              , title: "상품코드"     , width:  60, attributes: {style: 'text-align:center;' }, headerAttributes: {style: "text-align: center;"}, sortable : false, editable:function (dataItem) {return false;}}
                        , {field: "contsNm"              , title: "상품명"       , width: 200, attributes: {style: 'text-align:center;' }, headerAttributes: {style: "text-align: center;"}, sortable : false, editable:function (dataItem) {return false;}}
                        , {field: "dpStartDt"            , title: "시작일"       , width: 140, attributes: {style: 'text-align:center;' }, headerAttributes: {style: "text-align: center;"}, sortable : true, editable:function (dataItem) {return false;}}
                        , {field: "dpEndDt"              , title: "종료일"       , width: 140, attributes: {style: 'text-align:center;' }, headerAttributes: {style: "text-align: center;"}, sortable : true, editable:function (dataItem) {return false;}}
                        , {field: "dpRangeTpNm"          , title: "전시범위"     , width:  60, attributes: {style: 'text-align:center;' }, headerAttributes: {style: "text-align: center;"}, sortable : false, editable:function (dataItem) {return false;}}
                        , {field: "statusNm"             , title: "진행상태"     , width:  80, attributes: {style: 'text-align:center;' }, headerAttributes: {style: "text-align: center;"}, sortable : false, editable:function (dataItem) {return false;}}
                        , {field: "management"           , title: "관리"         , width: 140, attributes: {style: "text-align:center;" }, sortable : false, editable:function (dataItem) {return false;}
                                                          , template :  function(dataItem) {
                                                                            if (dataItem.dpCondTp == 'DP_COND_TP.MANUAL') {
                                                                              // 노출조건이 직접등록 => 수정/삭제 버튼 노출
                                                                              var saveBtn = '<button type="button" class="btn-gray btn-s" kind="btnContsLv3Edit">수정 </button>' + '&nbsp;';
                                                                              var deleteBtn = '<button type="button" class="btn-red btn-s" kind="btnContsLv3Del">삭제 </button>';

                                                                              var returnStr = '';

                                                                              if(fnIsProgramAuth("SAVE")){
                                                                                returnStr = saveBtn;
                                                                              }
                                                                              if(fnIsProgramAuth("DELETE")){
                                                                                returnStr += deleteBtn;
                                                                              }

                                                                              return '<div id="pageMgrButtonArea" class="btn-area textCenter">' + returnStr + '</div>';
                                                                            }
                                                                            else {
                                                                              // 노출조건이 직접등로 이외 => 수정 버튼 숨김

                                                                              var returnStr = '';
                                                                              if(fnIsProgramAuth("DELETE")){
                                                                                returnStr = '<button type="button" class="btn-red btn-s" kind="btnContsLv3Del">삭제 </button>';
                                                                              }

                                                                              return '<div id="pageMgrButtonArea" class="btn-area textCenter">' + returnStr + '</div>';
                                                                            }
                                                          }
                                                          , lockable: false
                        }
                        , {field: "dpCondTpNm"           , title: "노출조건"     , width:  60, attributes: {style: 'text-align:center;' }, headerAttributes: {style: "text-align: center;"}, sortable : false, editable:function (dataItem) {return false;}}
                        , {field: "dpSortTpNm"           , title: "노출순서"     , width:  60, attributes: {style: 'text-align:center;' }, headerAttributes: {style: "text-align: center;"}, sortable : false, editable:function (dataItem) {return false;}}
                        , {field: "goodsTypeName"        , title: "상품유형"     , width:  60, attributes: {style: 'text-align:center;' }, headerAttributes: {style: "text-align: center;"}, sortable : false, editable:function (dataItem) {return false;}}
                        , {field: "supplierName"         , title: "공급업체"     , width: 100, attributes: {style: 'text-align:center;' }, headerAttributes: {style: "text-align: center;"}, sortable : false, editable:function (dataItem) {return false;}}
                        , {field: "goodsBrandNm"         , title: "브랜드"       , width: 120, attributes: {style: 'text-align:center;' }, headerAttributes: {style: "text-align: center;"}, sortable : false, editable:function (dataItem) {return false;}}
                        , {field: "recommendedPrice"     , title: "정상가"       , width:  70, attributes: {style: 'text-align:center;' }, headerAttributes: {style: "text-align: center;"}, sortable : false, editable:function (dataItem) {return false;}
                                                                                 , format: "{0:\#\#,\#}"
                                                                                 //, template : function(dataItem) {
                                                                                 //               return (dataItem.recommendedPrice).replace(/(\d)(?=(?:\d{3})+(?!\d))/g, '$1,');
                                                                                 //             }
                          }
                        , {field: "salePrice"            , title: "판매가"       , width:  70, attributes: {style: 'text-align:center;' }, headerAttributes: {style: "text-align: center;"}, sortable : false, editable:function (dataItem) {return false;}
                                                                                 , format: "{0:\#\#,\#}"
                                                                                 //, template : function(dataItem) {
                                                                                 //               return (dataItem.salePrice).replace(/(\d)(?=(?:\d{3})+(?!\d))/g, '$1,');
                                                                                 //             }
                          }
                        , {field: "dispRangeNm"          , title: "판매허용범위" , width:  80, attributes: {style: 'text-align:center;' }, headerAttributes: {style: "text-align: center;"}, sortable : false, editable:function (dataItem) {return false;}
                                                                                 , template  : function(dataItem) {

                                                                                                 var dispYnRangeVal = '';
                                                                                                 var yesCnt = 0;
                                                                                                 var sepStr = '';

                                                                                                 // PC
                                                                                                 if (dataItem.dispWebPcYn == 'Y') {
                                                                                                   dispYnRangeVal = dataItem.dispWebPcYnNm;
                                                                                                   yesCnt++;
                                                                                                 }
                                                                                                 // Mobile
                                                                                                 if (dataItem.dispWebMobileYn == 'Y') {

                                                                                                   if (yesCnt > 0) {
                                                                                                     sepStr = '/';
                                                                                                   }

                                                                                                   dispYnRangeVal += (sepStr + dataItem.dispWebMobileYnNm);
                                                                                                   yesCnt++;
                                                                                                 }
                                                                                                 // APP
                                                                                                 if (dataItem.dispAppYn == 'Y') {

                                                                                                   if (yesCnt > 0) {
                                                                                                     sepStr = '/';
                                                                                                   }

                                                                                                   dispYnRangeVal += (sepStr + dataItem.dispAppYnNm);
                                                                                                 }
                                                                                                 return  dispYnRangeVal
                                                                                               }
                                                        }
                        , {field: "purchaseRangeNm"      , title: "구매허용범위" , width: 120, attributes: {style: 'text-align:center;' }, headerAttributes: {style: "text-align: center;"}, sortable : false, editable:function (dataItem) {return false;}
                                                                                 , template  : function(dataItem) {

                                                                                                 var purchaseYnRangeVal = '';
                                                                                                 var yesCnt = 0;
                                                                                                 var sepStr = '';

                                                                                                 // 일반회원
                                                                                                 if (dataItem.purchaseMemberYn == 'Y') {
                                                                                                   purchaseYnRangeVal = dataItem.purchaseMemberYnNm;
                                                                                                   yesCnt++;
                                                                                                 }
                                                                                                 // 임직원
                                                                                                 if (dataItem.purchaseEmployeeYn == 'Y') {

                                                                                                   if (yesCnt > 0) {
                                                                                                     sepStr = '/';
                                                                                                   }

                                                                                                   purchaseYnRangeVal += (sepStr + dataItem.purchaseEmployeeYnNm);
                                                                                                   yesCnt++;
                                                                                                 }
                                                                                                 // 비회원
                                                                                                 if (dataItem.purchaseNonmemberYn == 'Y') {

                                                                                                   if (yesCnt > 0) {
                                                                                                     sepStr = '/';
                                                                                                   }

                                                                                                   purchaseYnRangeVal += (sepStr + dataItem.purchaseNonmemberYnNm);
                                                                                                 }
                                                                                                 return  purchaseYnRangeVal
                                                                                               }
                          }
                        , {field: "goodsBasicCtgryFullNm", title: "전시카테고리" , width: 300, attributes: {style: 'text-align:center;' }, headerAttributes: {style: "text-align: center;"}, sortable : false, editable:function (dataItem) {return false;}}
                        , {field: "saleStatusName"       , title: "판매상태"     , width:  80, attributes: {style: 'text-align:center;' }, headerAttributes: {style: "text-align: center;"}, sortable : false, editable:function (dataItem) {return false;}}
                        , {field: "dispYnNm"             , title: "전시여부"     , width:  60, attributes: {style: 'text-align:center;' }, headerAttributes: {style: "text-align: center;"}, sortable : false, editable:function (dataItem) {return false;}}
                        , {field: "linkUrlPc"            , title: "링크URL"      , width: 420, attributes: {style: 'text-align:left;'   }, headerAttributes: {style: "text-align: center;"}, sortable : false, editable:function (dataItem) {return false;}}
                        , {field: "text1"                , title: "노출텍스트1"  , width: 150, attributes: {style: 'text-align:left;'   }, headerAttributes: {style: "text-align: center;"}, sortable : false, editable:function (dataItem) {return false;}
                                                         , template : function(dataItem) {

                                                                        var textVal;

                                                                        if (dataItem.text1 == null || dataItem.text1 == '') {
                                                                          textVal = '';
                                                                        }
                                                                        else {
                                                                          textVal = '['
                                                                                  + dataItem.text1Color
                                                                                  + '] '
                                                                                  + dataItem.text1;
                                                                        }
                                                                        return  textVal;
                                                                      }
                          }
                        , {field: "text2"                , title: "노출텍스트2"  , width: 150, attributes: {style: 'text-align:left;'   }, headerAttributes: {style: "text-align: center;"}, sortable : false, editable:function (dataItem) {return false;}
                                                         , template : function(dataItem) {

                                                                        var textVal;

                                                                        if (dataItem.text2 == null || dataItem.text2 == '') {
                                                                          textVal = '';
                                                                        }
                                                                        else {
                                                                          textVal = '['
                                                                                  + dataItem.text2Color
                                                                                  + '] '
                                                                                  + dataItem.text2;
                                                                        }
                                                                        return  textVal;
                                                                      }
                          }
                        , {field: "text3"                , title: "노출텍스트3"  , width: 150, attributes: {style: 'text-align:left;'   }, headerAttributes: {style: "text-align: center;"}, sortable : false, editable:function (dataItem) {return false;}
                                                         , template : function(dataItem) {
                                                                        var textVal;

                                                                        if (dataItem.text3 == null || dataItem.text3 == '') {
                                                                          textVal = '';
                                                                        }
                                                                        else {
                                                                          textVal = '['
                                                                                  + dataItem.text3Color
                                                                                  + '] '
                                                                                  + dataItem.text3;
                                                                        }
                                                                        return  textVal;
                                                                      }
                          }
                        ]
      //, rowTemplate : kendo.template($("#rowTemplate").html())
      };

      contsLv3GridOpt = contsLv3GridOptGoods;
    }
    else {
      // ----------------------------------------------------------------------
      // 1.TEXT or 기본
      // ----------------------------------------------------------------------
      contsLv3GridOptText = {
          dataSource  : contsLv3GridDs
        , noRecordMsg : "검색된 목록이 없습니다."
        , navigatable : true
        , scrollable  : true
        , selectable  : true
        , sortable    : true
        , editable    : "incell"
        , resizable   : true
        , columns     : [
                          /*{selectable: true, width: 20, attributes: { style: 'text-align:center;' }, headerAttributes: {style: "text-align: center;"}}*/
                          {field: 'chk'         , headerTemplate : "<input type='checkbox' id='contsLv3CheckAll' />"
                                                                        , width: 40 , attributes: {style: "text-align:center;" }, sortable : false, editable:function (dataItem) {return false;}
                                                , template :  function(dataItem) {
                                                                return '<input type="checkbox" name="contsLv3Check" class="couponGridChk" />'
                                                              }
                                                , locked: true, lockable: false
                          }
                        , {field: "sort"        , title: "순번"         , width:  60, attributes: {style: 'text-align:center;' }, headerAttributes: {style: "text-align: center;"}, sortable : false, locked: true, editor: sortEditor}
                        , {field: "dpContsId"   , title: "콘텐츠번호"   , width:  60, attributes: {style: 'text-align:center;' }, headerAttributes: {style: "text-align: center;"}, sortable : false, editable:function (dataItem) {return false;}, locked: true}
                        , {field: "titleNm"     , title: "타이틀명"     , width: 200, attributes: {style: 'text-align:center;' }, headerAttributes: {style: "text-align: center;"}, sortable : false, editable:function (dataItem) {return false;}, locked: true}
                        , {field: "dpStartDt"   , title: "시작일"       , width: 140, attributes: {style: 'text-align:center;' }, headerAttributes: {style: "text-align: center;"}, sortable : true, editable:function (dataItem) {return false;}}
                        , {field: "dpEndDt"     , title: "종료일"       , width: 140, attributes: {style: 'text-align:center;' }, headerAttributes: {style: "text-align: center;"}, sortable : true, editable:function (dataItem) {return false;}}
                        , {field: "dpRangeTpNm" , title: "전시범위"     , width:  60, attributes: {style: 'text-align:center;' }, headerAttributes: {style: "text-align: center;"}, sortable : false, editable:function (dataItem) {return false;}}
                        , {field: "statusNm"    , title: "진행상태"     , width:  80, attributes: {style: 'text-align:center;' }, headerAttributes: {style: "text-align: center;"}, sortable : false, editable:function (dataItem) {return false;}}
                        , {field: "management"  , title: "관리"         , width: 140, attributes: {style: "text-align:center;" }, sortable : false, editable:function (dataItem) {return false;}
                                                , template :  function(dataItem) {

                                                                  var saveBtn = '<button type="button" class="btn-gray btn-s" kind="btnContsLv3Edit">수정 </button>' + '&nbsp;';
                                                                  var deleteBtn = '<button type="button" class="btn-red btn-s" kind="btnContsLv3Del">삭제 </button>';

                                                                  var returnStr = '';

                                                                  if(fnIsProgramAuth("SAVE")){
                                                                    returnStr = saveBtn;
                                                                  }
                                                                  if(fnIsProgramAuth("DELETE")){
                                                                    returnStr += deleteBtn;
                                                                  }

                                                                  return '<div id="pageMgrButtonArea" class="btn-area textCenter">' + returnStr + '</div>';
                                                }
                                                , lockable: false
                        }
                        , {field: "text1"       , title: "노출텍스트1"  , width: 150, attributes: {style: 'text-align:left;'   }, headerAttributes: {style: "text-align: center;"}, sortable : false, editable:function (dataItem) {return false;}
                                                , template :  function(dataItem) {

                                                                var textVal;

                                                                if (dataItem.text1 == null || dataItem.text1 == '') {
                                                                  textVal = '';
                                                                }
                                                                else {
                                                                  textVal = '['
                                                                          + dataItem.text1Color
                                                                          + '] '
                                                                          + dataItem.text1;
                                                                }
                                                                return  textVal;
                                                              }
                          }
                        , {field: "text2"       , title: "노출텍스트2"  , width: 150, attributes: {style: 'text-align:left;'   }, headerAttributes: {style: "text-align: center;"}, sortable : false, editable:function (dataItem) {return false;}
                                                , template :  function(dataItem) {

                                                                var textVal;

                                                                if (dataItem.text2 == null || dataItem.text2 == '') {
                                                                  textVal = '';
                                                                }
                                                                else {
                                                                  textVal = '['
                                                                          + dataItem.text2Color
                                                                          + '] '
                                                                          + dataItem.text2;
                                                                }
                                                                return  textVal;
                                                              }
                          }
                        , {field: "text3"       , title: "노출텍스트3"  , width: 150, attributes: {style: 'text-align:left;'   }, headerAttributes: {style: "text-align: center;"}, sortable : false, editable:function (dataItem) {return false;}
                                                , template :  function(dataItem) {
                                                                var textVal;

                                                                if (dataItem.text3 == null || dataItem.text3 == '') {
                                                                  textVal = '';
                                                                }
                                                                else {
                                                                  textVal = '['
                                                                          + dataItem.text3Color
                                                                          + '] '
                                                                          + dataItem.text3;
                                                                }
                                                                return  textVal;
                                                              }
                          }
                        , {field: "linkUrlPc"   , title: "링크URL"      , width: 300, attributes: {style: 'text-align:left;'   }, headerAttributes: {style: "text-align: center;"}, sortable : false, editable:function (dataItem) {return false;}}
                        ]
      //, rowTemplate : kendo.template($("#rowTemplate").html())
      };

      contsLv3GridOpt = contsLv3GridOptText;
    }

    contsLv3Grid = $('#contsLv3Grid').initializeKendoGrid( contsLv3GridOpt ).cKendoGrid();

    //$("#pageGrid").on("click", "tbody>tr", function () {
    //    //fnGridClick();
    //});
    //pageGrid.bind("dataBound", function() {
    //  $('#totalCnt').text(pageGridDs._total);
    //});

    // ------------------------------------------------------------------------
    // 그리드 버튼 클릭 이벤트 등록 - 그리드의 버튼 실행 이벤트
    // ------------------------------------------------------------------------
    // * 콘텐츠 Lv3 수정
    $('#contsLv3Grid').on("click", "button[kind=btnContsLv3Edit]", function(e) {
      e.preventDefault();
      let dataItem = contsLv3Grid.dataItem($(e.currentTarget).closest("tr"));
      selectedContsLv3Data = dataItem;
      fnBtnContsEdit(3, dataItem);
    });
    // * 콘텐츠 Lv3 삭제
    $('#contsLv3Grid').on("click", "button[kind=btnContsLv3Del]", function(e) {
      e.preventDefault();
      let dataItem = contsLv3Grid.dataItem($(e.currentTarget).closest("tr"));
      selectedContsLv3Data = dataItem;
      fnContsDelSingle(3);
      //fnBtnContsDel(3, dataItem);
    });
    // 전체체크박스
    $("#contsLv3CheckAll").on("click",function(index){
      // 개별체크박스 처리
      if($("#contsLv3CheckAll").prop("checked") == true){
        // 전체체크
        // 개별체크 선택
        $('INPUT[name=contsLv3Check]').prop("checked", true);
        // 선택삭제 버튼 활성
        $('#fnBtnContsLv3Del').data('kendoButton').enable(true);
      }
      else{
        // 전체해제
        // 개별체크 선택 해제
        $('INPUT[name=contsLv3Check]').prop("checked",false);
        // 선택삭제 버튼 비활성
        $('#fnBtnContsLv3Del').data('kendoButton').enable(false);
      }
    });
    // 개별체크박스
    $('#ng-view').on("click","input[name=contsLv3Check]",function(index){

      const totalCnt    = $("input[name=contsLv3Check]").length;
      const checkedCnt  = $("input[name=contsLv3Check]:checked").length;
      // 전체체크박스 처리
      if (totalCnt == checkedCnt) {
        $('#contsLv3CheckAll').prop("checked", true);
      }
      else {
        $('#contsLv3CheckAll').prop("checked", false);
      }
      // 선택삭제 버튼 제어
      if (checkedCnt > 0) {
        $('#fnBtnContsLv3Del').data('kendoButton').enable(true);
      }
      else {
        // 선택삭제 버튼 비활성
        $('#fnBtnContsLv3Del').data('kendoButton').enable(false);
      }
    });

  }

  // ==========================================================================
  // # 상품 그리드 - 상품팝업용
  // ==========================================================================
  function fnInitContsGoodsGrid() {
    //console.log('# fnInitContsGoodsGrid Start');

    // 상품그리드 초기화
    if ($("#goodsGrid").data("kendoGrid") != undefined ) {
      $("#goodsGrid").data("kendoGrid").destroy();
      $("#goodsGrid").empty();
    }

    // 페이징없는 그리드
    goodsGridDs = fnGetDataSource({
        url      : "/admin/display/manage/selectGoodsListByKeyword?pageTp=111"
    });

    goodsGridOpt =  {
        dataSource  : goodsGridDs
      , noRecordMsg : "검색된 목록이 없습니다."
      , navigatable : true
      , scrollable  : true
      , selectable  : true
      , editable    : true
      , resizable   : true
      , columns     : [
                        { field     : 'chk'     , headerTemplate : "<input type='checkbox' id='goodsCheckAll' />"
                                                , width: 40 , attributes: {style: "text-align:center;" }, editable:function (dataItem) {return false;}
                                                , template :  function(dataItem) {
                                                                return '<input type="checkbox" name="goodsCheck" class="couponGridChk" />'
                                                              }
                                                , locked: true, lockable: false
                        }
                      , { field     : "sort"
                        , title     : "순번"
                        , width     : "60px", attributes : { style : "text-align:center" }, headerAttributes: {style: "text-align: center;"}
                        , editor    : function(container, options) {
                                        //console.log('# options :: ', JSON.stringify(options));
                                        var input = $("<input/>");
                                        input.attr("name", options.field);
                                        input.attr("type", "text");

                                        // 키업 이벤트 처리
                                        input.keyup(function() {
                                          var inValue = $(this).val();

                                          // 5자리 까지 허용
                                          if (inValue.length > MAX_SORT_LENGTH) {
                                            $(this).val(inValue.slice(0, MAX_SORT_LENGTH));
                                          }
                                          if (inValue < 0 ) {
                                            $(this).val(0);
                                          }
                                          // 숫자만허용
                                          const regExp = /[^0-9]/gi;

                                          if (inValue.match(regExp)) {
                                            $(this).val(inValue.replace(regExp, ""));
                                          }
                                        });
                                        input.appendTo(container);
                                        // 숫자만허용 템플릿
                                        //input.kendoNumericTextBox();
                                      }
                        , locked    : true
                        }
                      , { field     : "ilGoodsId"
                        , title     : "상품코드"
                        , width     : "70px", attributes : { style : "text-align:center" }
                        , editable  : function (dataItem) {return false;}
                        , locked    : true
                        }
                      , { field     : "goodsNm"
                        , title     : "상품명"
                        , width     : "180px", attributes : { style : "text-align:center" }
                        , editable:function (dataItem) {return false;}
                        , locked    : true
                        }
                      , { field     : "text1"
                        , title     : "노출텍스트1"
                        , width     : "140px", attributes : { style : "text-align:center" }
                        , editable  : function (dataItem) {return false;}
                        , template  : function(dataItem) {
                                        return  '<input type="text" id="text1Color" name="text1Color" class="comm-input textColor" maxlength="7"  size="8" />' + '&nbsp;' +
                                                '<input type="text" id="text1"      name="text1"      class="comm-input text1"     maxlength="15" size="30" />'
                                      }
                        }
                      , { field     : "text2"
                        , title     : "노출텍스트2"
                        , width     : "140px", attributes : { style : "text-align:center" }
                        , editable  : function (dataItem) {return false;}
                        , template  : function(dataItem) {
                                        return  '<input type="text" id="text2Color" name="text2Color" class="comm-input textColor" maxlength="7"  size="8" />' + '&nbsp;' +
                                                '<input type="text" id="text2"      name="text2"      class="comm-input text2"     maxlength="15" size="30" />'
                                      }
                        }
                      , { field     : "text3"
                        , title     : "노출텍스트3"
                        , width     : "140px", attributes : { style : "text-align:center" }
                        , editable  : function (dataItem) {return false;}
                        , template  : function(dataItem) {
                                        return  '<input type="text" id="text3Color" name="text3Color" class="comm-input textColor" maxlength="7"  size="8" />' + '&nbsp;' +
                                                '<input type="text" id="text3"      name="text3"      class="comm-input text3"     maxlength="15" size="30" />'
                                      }
                        }
                      , { field     : "goodsTpNm"
                        , title     : "상품유형"
                        , width     : "60px", attributes : { style : "text-align:center" }
                        , editable  : function (dataItem) {return false;}
                        }
                      , { field     : "supplierNm"
                        , title     : "공급업체"
                        , width     : "100px", attributes : { style : "text-align:center" }
                        , editable  : function (dataItem) {return false;}
                        }
                      , { field     : "dpBrandNm"
                        , title     : "브랜드"
                        , width     : "120px", attributes : { style : "text-align:center" }
                        , editable  : function (dataItem) {return false;}
                        }
                      , { field     : "recommendedPrice"
                        , title     : "정상가"
                        , width     : "70px", attributes : { style : "text-align:center" }, format: "{0:\#\#,\#}"
                        , editable  : function (dataItem) {return false;}
                        //, template  : function(dataItem) {
                        //                return (dataItem.recommendedPrice).replace(/(\d)(?=(?:\d{3})+(?!\d))/g, '$1,');
                        //              }
                        }
                      , { field     : "salePrice"
                        , title     : "판매가"
                        , width     : "70px", attributes : { style : "text-align:center" }, format: "{0:n0}"
                        , editable  : function (dataItem) {return false;}
                        //, template  : function(dataItem) {
                        //                return (dataItem.salePrice).replace(/(\d)(?=(?:\d{3})+(?!\d))/g, '$1,');
                        //              }
                        }
                      , { field     : "ctgryFullNm"
                        , title     : "전시카테고리"
                        , width     : "300px", attributes : { style : "text-align:center" }
                        , editable  : function (dataItem) {return false;}
                        }
                      , { field     : "dispYnRange"
                        , title     : "판매허용범위"
                        , width     : "80px", attributes : { style : "text-align:center" }
                        , editable  : function (dataItem) {return false;}
                        , template  : function(dataItem) {

                                        var dispYnRangeVal = '';
                                        var yesCnt = 0;
                                        var sepStr = '';

                                        // PC
                                        if (dataItem.dispWebPcYn == 'Y') {
                                          dispYnRangeVal = dataItem.dispWebPcYnNm;
                                          yesCnt++;
                                        }
                                        // Mobile
                                        if (dataItem.dispWebMobileYn == 'Y') {

                                          if (yesCnt > 0) {
                                            sepStr = '/';
                                          }

                                          dispYnRangeVal += (sepStr + dataItem.dispWebMobileYnNm);
                                          yesCnt++;
                                        }
                                        // APP
                                        if (dataItem.dispAppYn == 'Y') {

                                          if (yesCnt > 0) {
                                            sepStr = '/';
                                          }

                                          dispYnRangeVal += (sepStr + dataItem.dispAppYnNm);
                                        }
                                        return  dispYnRangeVal
                                      }
                        }
                      , { field     : "purchaseYnRange"
                        , title     : "구매허용범위"
                        , width     : "120px", attributes : { style : "text-align:center" }
                        , editable  : function (dataItem) {return false;}
                        , template  : function(dataItem) {

                                        var purchaseYnRangeVal = '';
                                        var yesCnt = 0;
                                        var sepStr = '';

                                        // 일반회원
                                        if (dataItem.purchaseMemberYn == 'Y') {
                                          purchaseYnRangeVal = dataItem.purchaseMemberYnNm;
                                          yesCnt++;
                                        }
                                        // 임직원
                                        if (dataItem.purchaseEmployeeYn == 'Y') {

                                          if (yesCnt > 0) {
                                            sepStr = '/';
                                          }

                                          purchaseYnRangeVal += (sepStr + dataItem.purchaseEmployeeYnNm);
                                          yesCnt++;
                                        }
                                        // 비회원
                                        if (dataItem.purchaseNonmemberYn == 'Y') {

                                          if (yesCnt > 0) {
                                            sepStr = '/';
                                          }

                                          purchaseYnRangeVal += (sepStr + dataItem.purchaseNonmemberYnNm);
                                        }
                                        return  purchaseYnRangeVal
                                      }
                        }
                      , { field     : "saleStatusName"
                        , title     : "판매상태"
                        , width     : "80px", attributes : { style : "text-align:center" }
                        , editable  : function (dataItem) {return false;}
                        }
                      , { field     : "dispYnNm"
                        , title     : "전시여부"
                        , width     : "60px", attributes : { style : "text-align:center" }
                        , lockable  : false
                        }

                      ]
    }
    goodsGrid = $('#goodsGrid').initializeKendoGrid( goodsGridOpt ).cKendoGrid();

    // ------------------------------------------------------------------------
    // 그리드 Total
    // ------------------------------------------------------------------------
    // 전체건수(페이징이 아닌경우)
    $('#totalCntGoods').text(0);

    goodsGrid.bind("dataBound", function() {
      var gridTotalCnt = goodsGridDs._data.length;
      $('#totalCntGoods').text(gridTotalCnt);
      //console.log('# dataBound GoGoGo...:: ', goodsGridDs._data.length);
      //$('#totalCntGoods').text( kendo.toString(goodsGridDs.total, "n0") );
    });
    //// 전체건수(페이징인경우)
    //goodsGrid.bind("dataBound", function() {
    //// 첫번째 방법
    //  //row number
    //  var row_number = goodsGrid._total - ((aGridDs._page - 1) * aGridDs._pageSize);
    //  $("#aGrid tbody > tr .row-number").each(function(){
    //    $(this).html(row_number);
    //    row_number--;
    //  });
    //  //total count
    //  $('#totalCnt').text(aGridDs._total);
    //
    //// 두번째 방법
    // $('#totalCntGoods').text( kendo.toString(goodsGridDs.total, "n0") );
    //});

    // ------------------------------------------------------------------------
    // 그리드 전체선택 클릭 이벤트
    // ------------------------------------------------------------------------
    $("#goodsCheckAll").on("click", function(index){
      if( $("#goodsCheckAll").prop("checked") ){
        $("input[name=goodsCheck]").prop("checked", true);
      }
      else{
        $("input[name=goodsCheck]").prop("checked", false);
      }
    });
    // ------------------------------------------------------------------------
    // 그리드 체크박스 클릭
    // ------------------------------------------------------------------------
    goodsGrid.element.on("click", "[name=goodsCheck]" , function(e){
      if( e.target.checked ){
        if( $("[name=goodsCheck]").length == $("[name=goodsCheck]:checked").length ){
          $("#goodsCheckAll").prop("checked", true);
        }
      }
      else{
        $("#goodsCheckAll").prop("checked", false);
      }
    });
    // ------------------------------------------------------------------------
    // 그리드 텍스트칼라 입력 제한
    // ------------------------------------------------------------------------
    goodsGrid.element.on("keyup", "input.textColor" , function(e){
      //console.log('# bindKeyupEvents.keyup');

      // 문자 제한
      const regExp = /[^0-9a-fA-F\#]/gi;
      const _value = $(this).val();

      if (_value.match(regExp)) {
        $(this).val(_value.replace(regExp, ""));
      }
    });

  } // End of unction fnInitContsGoodsGrid()


  // ==========================================================================
  // # 인벤토리그룹 그리드 - 인벤토리그룹 관리용
  // ==========================================================================
  function fnInitInventoryGroupGrid() {
    //console.log('# fnInitInventoryGroupGrid Start');

    // 상품그리드 초기화
    if ($("#inventoryGroupGrid").data("kendoGrid") != undefined ) {
      $("#inventoryGroupGrid").data("kendoGrid").destroy();
      $("#inventoryGroupGrid").empty();
    }

    // 페이징없는 그리드
    inventoryGroupGridDs = fnGetDataSource({
      url      : "/admin/display/manage/selectDpInventoryGroupList"
    });

    inventoryGroupGridOpt =  {
          dataSource  : inventoryGroupGridDs
        , noRecordMsg : "검색된 목록이 없습니다."
        , navigatable : true
        , scrollable  : true
        , height      : 285
        , selectable  : true
        , editable    : true
        , resizable   : true
        , columns     : [
                          { field     : 'chk'     , headerTemplate : "<input type='checkbox' id='inventoryGroupCheckAll' />"
                          , width     : 20 , attributes: {style: "text-align:center;" }, editable:function (dataItem) {return false;}
                          , template  : function(dataItem) {
                                          return '<input type="checkbox" name="inventoryGroupCheck" class="couponGridChk" />'
                                        }
                          }
                        , { field     : "sort"
                          , title     : "순번"
                          , width     : 30, attributes : { style : "text-align:center" }, headerAttributes: {style: "text-align: center;"}
                          , editor    : function(container, options) {
                                          //console.log('# options :: ', JSON.stringify(options));
                                          var input = $("<input/>");
                                          input.attr("name", options.field);
                                          input.attr("type", "text");

                                          // 키업 이벤트 처리
                                          input.keyup(function() {
                                            var inValue = $(this).val();

                                            // 5자리 까지 허용
                                            if (inValue.length > 5) {
                                              $(this).val(inValue.slice(0, 5));
                                            }
                                            if (inValue < 0 ) {
                                              $(this).val(0);
                                            }
                                            // 숫자만허용
                                            const regExp = /[^0-9]/gi;

                                            if (inValue.match(regExp)) {
                                              $(this).val(inValue.replace(regExp, ""));
                                            }
                                          });
                                          input.appendTo(container);
                                          // 숫자만허용 템플릿
                                          //input.kendoNumericTextBox();
                                        }
                          }
                        , { field     : "dpPageId"
                          , title     : "인벤토리<br/>그룹ID"
                          , width     : 30, attributes : { style : "text-align:center" }
                          , editable  : function (dataItem) {return false;}
                        }
                        , { field     : "pageNm"
                          , title     : "인벤토리 그룹명"
                          , width     : 100, attributes : { style : "text-align:center" }
                          , editable  : function (dataItem) {return false;}
                          }
                        , { field     : "useYn"
                          , title     : "사용여부"
                          , width     : 30, attributes : { style : "text-align:center" }
                          , editable  : function (dataItem) {return false;}
                          , template  : function(dataItem) {
                                          if (dataItem.useYn == 'Y') {
                                            return "예";
                                          }
                                          else {
                                            return "아니오";
                                          }
                                        }
                          }
                        , { field     : "groupDesc"
                          , title     : "그룹설명"
                          , width     : 150, attributes : { style : "text-align:left" }
                          , editable  : function (dataItem) {return false;}
                          }
                        , { field     : "management"
                          , title     : "관리"
                          , width     : 60, attributes: {style: "text-align:center;" }, editable:function (dataItem) {return false;}
                          , template  : function(dataItem) {

			                        	  var saveBtn = '<button type="button" class="btn-gray btn-s" kind="btnInventoryGroupEdit">수정 </button>' + '&nbsp;';
			                        	  var deleteBtn = '<button type="button" class="btn-red btn-s" kind="btnInventoryGroupDel">삭제 </button>';

			                        	  var returnStr = '';

			                        	  if(fnIsProgramAuth("SAVE")){
			                        	      returnStr = saveBtn;
			                        	  }
			                        	  if(fnIsProgramAuth("DELETE")){
			                        	      returnStr += deleteBtn;
			                        	  }

			                        	  return '<div id="pageMgrButtonArea" class="btn-area textCenter" style="margin :0;">' + returnStr + '</div>';

//                                          return  '<div id="pageMgrButtonArea" class="btn-area textCenter" style="margin :0;">'
//                                                + '<button type="button" class="btn-gray btn-s" kind="btnInventoryGroupEdit">수정 </button>'
//                                                + '&nbsp;'
//                                                + '<button type="button" class="btn-red btn-s" kind="btnInventoryGroupDel">삭제 </button>'
//                                                + '</div>';
                                        }
                          }

                        ]
    }

    inventoryGroupGrid = $('#inventoryGroupGrid').initializeKendoGrid( inventoryGroupGridOpt ).cKendoGrid();

    //// 그리드 조회 실행
    //let data = $("#inputInventoryGroupForm").formSerialize(true);
    //inventoryGroupGridDs.read(data);

    // ------------------------------------------------------------------------
    // * $('#fnBtnInventoryGroupDel').data('kendoButton').enable 사용을 위해서는 fnInitButton 에 등록해야 함
    // ------------------------------------------------------------------------
    // 그리드 전체선택 클릭 이벤트
    // ------------------------------------------------------------------------
    $("#inventoryGroupCheckAll").on("click", function(index){
      if( $("#inventoryGroupCheckAll").prop("checked") ){
        // 전체 체크
        $("input[name=inventoryGroupCheck]").prop("checked", true);
        // 선택삭제 버튼 활성
        $('#fnBtnInventoryGroupDel').data('kendoButton').enable(true);
      }
      else{
        // 전체 언체크
        $("input[name=inventoryGroupCheck]").prop("checked", false);
        // 선택삭제 버튼 비활성
        $('#fnBtnInventoryGroupDel').data('kendoButton').enable(false);
      }
    });
    // ------------------------------------------------------------------------
    // 그리드 체크박스 클릭
    // ------------------------------------------------------------------------
    inventoryGroupGrid.element.on("click", "[name=inventoryGroupCheck]" , function(e){
      if( e.target.checked ){
        if( $("[name=inventoryGroupCheck]").length == $("[name=inventoryGroupCheck]:checked").length ){
          $("#inventoryGroupCheckAll").prop("checked", true);
        }
      }
      else{
        $("#inventoryGroupCheckAll").prop("checked", false);
      }
      if ($("[name=inventoryGroupCheck]:checked").length > 0) {
        // 선택삭제 버튼 활성
        $('#fnBtnInventoryGroupDel').data('kendoButton').enable(true);
      }
      else {
        // 선택삭제 버튼 비활성
        $('#fnBtnInventoryGroupDel').data('kendoButton').enable(false);
      }

    });

  } // End of unction fnInitInventoryGroupGrid()

  // -------------------------------  POPUP START  ----------------------------
  // ==========================================================================
  // # fnPopupButton
  // ==========================================================================
  //function fnPopupButton(param) {
  //  fnKendoPopup({
  //        id     : 'SAMPLE_POP',
  //        title  : fnGetLangData({key :"4620",nullMsg :'표준용어 팝업' }),
  //        src    : '#/dicMst001',
  //        success: function( id, data ){
  //            if(data.GB_DIC_MST_ID){
  //              $('#GB_DIC_MST_ID').val(data.GB_DIC_MST_ID);
  //              $('#GB_DIC_MST_NM').val(data.BASE_NAME);
  //            }
  //          }
  //    });
  //};
  // -------------------------------  POPUP End  ------------------------------

  // ==========================================================================
  // # 콘텐츠명
  // ==========================================================================
  function fnGetContsName(contsTp) {
    if(contsTp == 'DP_CONTENTS_TP.TEXT') {
      // 1.TEXT
      return 'TEXT'
    }
    else if(contsTp == 'DP_CONTENTS_TP.HTML') {
      // 2.HTML
      return 'HTML';
    }
    else if(contsTp == 'DP_CONTENTS_TP.BANNER') {
      // 3.BANNER
      return '배너';
    }
    else if(contsTp == 'DP_CONTENTS_TP.BRAND') {
      // 4.BRAND
      return '브랜드';
    }
    else if(contsTp == 'DP_CONTENTS_TP.CATEGORY') {
      // 5.CATEGORY
      return '카테고리';
    }
    else if(contsTp == 'DP_CONTENTS_TP.GOODS') {
      // 6.GOODS
      return '상품';
    }
  }

  // ==========================================================================
  // # 오류메시지처리
  // ==========================================================================
  function fnMessage(key, nullMsg, ID) {
    fnKendoMessage({ message : fnGetLangData({ key : key, nullMsg : nullMsg}), ok : function focusValue(){
      $('#'+ID).focus();
    }});

    return false;
  }

  // ==========================================================================
  // # 그리드 순번 입력 custom Editor 함수
  // # 그리드 column 옵션에 editor: sortEditor로 적용
  // ==========================================================================
  function sortEditor(container, options) {

    const input = $("<input/>");
    input.attr("name", options.field);
    input.attr("type", "text");

    input.on("click", function(e) {
      e.stopPropagation();
      e.stopImmediatePropagation();
    });

    // blur 이벤트 바인딩
    input.on("blur", function(e) {
      const value = $(this).val().trim();

      if(!value.length) {
        fnKendoMessage({
          message: "순번을 입력해주세요.",
          ok: function() {
            // 입력 값이 없을 경우 강제로 editCell 실행하여, 다시 input 태그를 생성하고, focus를 맞춥니다.
            contsLv1Grid.editCell(container);
          }
        });
      }
    })

    input.on("keyup", function(e) {
      const regExp = /[^0-9]/g;
      let value = $(this).val();

      if( value && value.match(regExp) ) {
        value = value.replace(regExp, "");
        $(this).val(value);
      }

      if( value.length > MAX_SORT_LENGTH ) {
        value = value.substring(0, MAX_SORT_LENGTH);
        $(this).val(value);
      }
    })

    input.appendTo(container);
  }

  /* ==========================================================================
   * # 추가된 이미지 함수 & 이벤트
   * ==========================================================================
   */
  // FIXME 이미지 삭제 이벤트
  var $removeBtn = $(".fileUpload__removeBtn");

  $removeBtn.on("click", function(e) {
    e.preventDefault();
    e.stopPropagation();

    if(!window.confirm("이미지를 삭제하시겠습니까?")) return;

    var $wrapper = $(this).closest(wrapper_cn);
    var $container = $(this).closest(container_cn);
    var $img = $wrapper.find("img");
    var $message = $container.find(".fileUpload__message");
    var $title = $container.find(".fileUpload__title");
    var id = $img.attr('id');
    //console.log('# $img     :: ', $img);
    //console.log('# $message :: ', $message);
    //console.log('# $title   :: ', $title);
    //console.log('# id       :: ', id);

    // ------------------------------------------------------------------------
    // 이미지 전역변수 초기화
    // ------------------------------------------------------------------------
    if (id == 'imgPcBannerView') {
      // 배너PC이미지 : imgPcBanner / imgPcBannerView
      imgPathPcBanner             = '';           // 이미지2-풀경로
      imgOriginNmPcBanner         = '';           // 이미지2-원본파일명
    }
    else if (id == 'imgMobileBannerView') {
      // 배너모바일이미지 : imgMobileBanner / imgMobileBannerView
      imgPathMobileBanner         = '';           // 이미지1-풀경로
      imgOriginNmMobileBanner     = '';           // 이미지1-원본파일명
    }
    else if (id == 'gifImgMobileBannerView') {
      // 배너모바일gif이미지 : gifImgMobileBanner / gifImgMobileBannerView
      gifImgPathMobileBanner      = '';           // 이미지gif모바일-풀경로
      gifImgOriginNmMobileBanner  = '';           // 이미지gif모바일-원본파일명
    }
    else if (id == 'gifImgPcBannerView') {
      // 배너PCgif이미지 : gifImgPcBanner / gifImgPcBannerView
      gifImgPathPcBanner          = '';           // 이미지gifPC-풀경로
      gifImgOriginNmPcBanner      = '';           // 이미지gifPC-원본파일명
    }
    else if (id == 'imgMobileBrandView') {
      // 브랜드이미지1(Mobile)이미지 : imgMobileBrand / imgMobileBrandView
      imgPathMobileBrand          = '';           // 이미지1-풀경로
      imgOriginNmMobileBrand      = '';           // 이미지1-원본파일명
    }
    else if (id == 'imgPcBrandView') {
      // 브랜드이미지2(PC)이미지 : imgPcBrand / imgPcBrandView
      imgPathPcBrand              = '';           // 이미지2-풀경로
      imgOriginNmPcBrand          = '';           // 이미지2-원본파일명
    }

    // ------------------------------------------------------------------------
    // 화면노출 Cliear
    // ------------------------------------------------------------------------
    $img.attr("src", "");
    $title.text("");
    $wrapper.hide();
    $title.hide();
    $message.show();
  });

 /**
  *
  * @param {*} e : fnKendoUpload의 select 이벤트 객체
  * e.sender.element로 file input 태그에 접근할 수 있습니다.
  * e.sender.element에 accept속성이 정의되어 있지 않다면, allowedImageExtensionList를 사용합니다.
  */
  function validateExtension(e) {

    var allowedExt = "";
    var ext = e.files[0].extension;
    var $el = e.sender.element;

    if( !$el.length ) return;

    if( $el[0].accept && $el[0].accept.length ) {
      // 공백 제거
      allowedExt = $el[0].accept.replace(/\s/g, "");
      allowedExt = allowedExt.split(",");
    } else {
      allowedExt = allowedImageExtensionList;
    }

    return allowedExt.includes(ext.toLowerCase());
  };

  // 선택된 날짜가 오늘 날짜 이전인지 체크
  function checkIsDatePast(selectedDate) {
    // 신규 생성일 경우 오늘날짜 보다 이전 날짜 선택 불가
    const _todayDate = new Date();
    const _todayDateTime = _todayDate.setHours(0, 0, 0, 0);

    return selectedDate && selectedDate.getTime() < _todayDateTime;
  }

  // ------------------------------- Html 버튼 바인딩  Start -----------------------
  /** Common New Top*/
  $scope.fnBtnClear                  = function( ){  fnBtnClear();};
  /** Common 콘텐츠Lv1조회버튼 */
  $scope.fnBtnSearchContsLv1         = function( ){  fnBtnSearchContsLv1();};
  /** Common 콘텐츠Lv2조회버튼 */
  $scope.fnBtnSearchContsLv2          = function( ){  fnBtnSearchContsLv2();};
  /** Common 콘텐츠Lv3조회버튼 */
  $scope.fnBtnSearchContsLv3          = function( ){  fnBtnSearchContsLv3();};
  /** Common Page Link */
  $scope.fnBtnPageLink                = function( ){  fnBtnPageLink();};
  /** Common Inventory Preview */
  $scope.fnBtnInventoryPreview        = function( ){  fnBtnInventoryPreview();};
  /** Common Conts Lv1 Sort */
  $scope.fnBtnSaveSortContsLv1        = function( ){  fnBtnSaveSortContsLv1();};
  /** Common Conts Lv2 Sort */
  $scope.fnBtnSaveSortContsLv2        = function( ){  fnBtnSaveSortContsLv2();};
  /** Common Conts Lv3 Sort */
  $scope.fnBtnSaveSortContsLv3        = function( ){  fnBtnSaveSortContsLv3();};
  /** Common Conts Lv1 New */
  $scope.fnBtnContsLv1New             = function( ){  fnBtnContsLv1New();};
  /** Common Conts Lv2 New */
  $scope.fnBtnContsLv2New             = function( ){  fnBtnContsLv2New();};
  /** Common Conts Lv3 New */
  $scope.fnBtnContsLv3New             = function( ){  fnBtnContsLv3New();};
  /** Common Conts Lv1 Del */
  $scope.fnBtnContsLv1Del             = function( ){  fnBtnContsLv1Del();};
  /** Common Conts Lv2 Del */
  $scope.fnBtnContsLv2Del             = function( ){  fnBtnContsLv2Del();};
  /** Common Conts Lv3 Del */
  $scope.fnBtnContsLv3Del             = function( ){  fnBtnContsLv3Del();};
  /** Common fnBtnSaveText */
  $scope.fnBtnSaveText                = function( ){  fnBtnSaveText();};
  /** Common fnBtnSaveHtml */
  $scope.fnBtnSaveHtml                = function( ){  fnBtnSaveHtml();};
  /** Common fnBtnSaveBanner */
  $scope.fnBtnSaveBanner              = function( ){  fnBtnSaveBanner();};
  /** Common fnBtnSaveBrand */
  $scope.fnBtnSaveBrand               = function( ){  fnBtnSaveBrand();};
  /** Common fnBtnSaveCategory */
  $scope.fnBtnSaveCategory            = function( ){  fnBtnSaveCategory();};
  /** Common fnBtnSaveGoods */
  $scope.fnBtnSaveGoods               = function( ){  fnBtnSaveGoods();};
  /** Common Goods Search */
  $scope.fnBtnSearchGoods             = function( ){  fnSearchGoods();};  // fnBtnSearchGoods => fnSearchGoods
  /** Common Goods Search */
  $scope.fnBtnClearGoods              = function( ){  fnClearGoods();};   // fnBtnClearGoods => fnClearGoods
  /** Common Inventory Gruop Popup */
  $scope.fnInitPopupInventoryGroup    = function( ){  fnInitPopupInventoryGroup();};
  /** Common Inventory Group Edit */
  $scope.fnBtnInventoryGroupEdit      = function( ){  fnBtnInventoryGroupEdit();};
  /** Common Inventory New */
  $scope.fnBtnInventoryGroupNew       = function( ){  fnBtnInventoryGroupNew();};
  /** Common Inventory Save */
  $scope.fnBtnSaveInventoryGroup      = function( ){  fnBtnSaveInventoryGroup ();};
  /** Common Inventory Del = Multi */
  $scope.fnBtnInventoryGroupDel       = function( ){  fnBtnInventoryGroupDel();};
  /** Common Inventory Sort */
  $scope.fnBtnSaveSortInventoryGroup  = function( ){  fnBtnSaveSortInventoryGroup();};
  /** Common ExcelDown Inventory */
  $scope.fnBtnExcelDownInventory      = function( ){  fnBtnExcelDownInventory();};
  /** Common ExcelDown Conts Lv1 */
  $scope.fnBtnExcelDownContsLv1       = function( ){  fnBtnExcelDownContsLv1();};
  /** Common ExcelDown Conts Lv2 */
  $scope.fnBtnExcelDownContsLv2       = function( ){  fnBtnExcelDownContsLv2();};
  /** Common ExcelDown Conts Lv3 */
  $scope.fnBtnExcelDownContsLv3       = function( ){  fnBtnExcelDownContsLv3();};

  /** Common File Upload */
  $scope.fnBtnImage             = function(imageType) {

    $('#' + imageType).trigger('click');

    // 이미지 유형별 처리가 불가능할 경우 아래 swith문 사용할 것
    //switch (imageType) {
    //
    //  case 'imgMobileBanner':
    //    // 팝업-배너-이미지1
    //    $('#imgMobileBanner').trigger('click');     // 이미지1 input Tag 클릭 이벤트 호출
    //    break;
    //  case 'gifImgMobileBanner':
    //    // 팝업-배너-모바일gif
    //    $('#gifImgMobileBanner').trigger('click');  // Mobile gif input Tag 클릭 이벤트 호출
    //    break;
    //  case 'imgPcBanner':
    //    // 팝업-배너-이미지2
    //    $('#imgPcBanner').trigger('click');         // 이미지2 input Tag 클릭 이벤트 호출
    //    break;
    //  case 'gifImgPcBanner':
    //    // 팝업-배너-PC gif
    //    $('#gifImgPcBanner').trigger('click');      // PC gif input Tag 클릭 이벤트 호출
    //    break;
    //}
  };

  /** popup button **/
  //$scope.fnPopupButton = function( param ){ fnPopupButton(param); };
  // ------------------------------- Html 버튼 바인딩  End -------------------------

}); // document ready - END
