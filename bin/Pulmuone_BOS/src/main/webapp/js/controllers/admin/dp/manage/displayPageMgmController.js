﻿/**-----------------------------------------------------------------------------
 * description      : 전시관리 - 전시 페이지 설정
 * @
 * @ 수정일          수정자          수정내용
 * @ ------------------------------------------------------
 * @ 2020.09.28    dgyoun        최초생성
 * @
 * **/
'use strict';

var treeView, parentNode, dataSource;
var MAX_SORT_LENGTH = 6;
var pageTp              = 'PAGE_TP.PAGE';         // 페이지유형(기본값:페이지코너별)
var mallDivision        = 'MALL_DIV.PULMUONE';    // 몰구분(기본값:풀무원)
var selectedPageData;                             // 선택된페이지데이터
var selectedPageUid;                              // 선택된 페이지그리드 UID
var selectedDpPageId;                             // 선택된페이지ID
var selectedInventoryData;                        // 선택된인벤토리데이터
var useAllYn  = 'N';                              // 미사용포함여부(기본값:N)
var dpPageId;
var PAGE_SIZE = 10;
//var selectedPageDepth   = 0;                    // 페이지depth

var pageGridDs, pageGridOpt, pageGrid;
var inventoryGridDs, inventoryGridOpt, inventoryGrid;


$(document).ready(function() {
  //Initialize Page Call
  fnInitialize();

  // ==========================================================================
  // # Initialize PageR
  // ==========================================================================
  function fnInitialize() {
    $scope.$emit('fnIsMenu', { flag : 'true' });

    fnPageInfo({
      PG_ID  : 'displayPageMgm',
      callback : fnUI
    });
  }

  // ==========================================================================
  // # fnUI
  // ==========================================================================
  function fnUI() {

       fnTranslate();         // comm.lang.js 안에 있는 공통함수 다국어 변환--------------

       fnInitButton();        // Initialize Button  ---------------------------

       fnInitOptionBox();     // Initialize radio -----------------------------

       fnInitTree();          // Initialize Tree ------------------------------

       fnInitInventoryGrid(); // Initialize Inventory Grid --------------------

       fnBtnClear();             // Data 초기화 -------------------------------------

       fnSetValidation();     //validation 설정
  }

  // ==========================================================================
  // # 버튼 초기화
  // ==========================================================================
  function fnInitButton() {
    // Top페이지신규/수정팝업오픈, 하위탑페이지신규/수정팝업오픈(하위페이지추가,상세추가), 페이지삭제처리, 인벤토리순번저장처리
    $('#fnBtnPageNewTop, #fnBtnPageNew, #fnBtnPageSave, #fnBtnPageEdit, #fnBtnPageDel, #fnBtnInventorySortSave, #fnBtnInventoryNew, #fnBtnInventorySave, #fnBtnInventoryEdit, #fnBtnInventoryDel, #fnPopupClose').kendoButton();
    // 인벤토리그리드 : 인벤토리신규/수정팝업오픈, 인벤토리삭제
    //$('#fnBtnInventoryEdit').kendoButton({ enable: true });
    //$('#fnBtnInventoryDel').kendoButton({ enable: true });
  }

  // ==========================================================================
  // # 입력랎 초기화
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
  }

  // ---------------Initialize Option Box Start -------------------------------
  // ==========================================================================
  // # fnInitOptionBox
  // ==========================================================================
  function fnInitOptionBox(){

    // ------------------------------------------------------------------------
    // 팝업 초기화
    // ------------------------------------------------------------------------
    $('#kendoPopup').kendoWindow({
        visible : false
      , modal   : true
    });

    // ------------------------------------------------------------------------
    // 페이지유형(페이지코너별, 카테고리코너별)
    // ------------------------------------------------------------------------
    fnKendoDropDownList({
        id          : 'comboPageTp'
      , url         : "/admin/comn/getCodeList"
      , params      : {"stCommonCodeMasterCode" : "PAGE_TP", "useYn" :"Y"}
      , tagId       : 'comboPageTp'
      , autoBind    : true
      , valueField  : 'CODE'
      , textField   : 'NAME'
      , async       : true
      , isDupUrl    : 'Y'
      , chkVal      : pageTp
      , style       : {}
            /*blank : '선택',*/
    });
    // ------------------------------------------------------------------------
    // 몰구분
    // ------------------------------------------------------------------------
    fnKendoDropDownList({
        id          : 'comboMallDivision'
      , url         : "/admin/comn/getCodeList"
      , params      : {"stCommonCodeMasterCode" : "MALL_DIV", "useYn" :"Y"}
      , tagId       : 'comboMallDivision'
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
    // 미사용포함
    // ------------------------------------------------------------------------
    fnTagMkChkBox({
        id    : 'checkUseAllYn',
        data  : [
                  { "CODE" : 'Y' , "NAME" : "미사용포함" }
                ],
        tagId : 'checkUseAllYn',
        chkVal: 'N',
        style : {}
    });
    // ------------------------------------------------------------------------
    // 페이지.사용여부
    // ------------------------------------------------------------------------
    fnKendoDropDownList({
        id          : "pageUseYn"
      , data        : [ {"CODE" : "Y", "NAME" : "예"}
                      , {"CODE" : "N", "NAME" : "아니오"}
                      ]
      , valueField  : "CODE"
      , textField   : "NAME"
      , value       : "Y"
    });
    // ------------------------------------------------------------------------
    // 인벤토리.사용여부-인벤토리그리드 상단 조회조건
    // ------------------------------------------------------------------------
    fnKendoDropDownList({
      id          : "inventoryUseYnSearch"
    , data        : [ {"CODE" : "" , "NAME" : "사용여부 전체"}
                    , {"CODE" : "Y", "NAME" : "예"}
                    , {"CODE" : "N", "NAME" : "아니오"}
                    ]
    , valueField  : "CODE"
    , textField   : "NAME"
    , value       : ""
  });
    // ------------------------------------------------------------------------
    // 인벤토리.사용여부
    // ------------------------------------------------------------------------
    fnKendoDropDownList({
      id          : "inventoryUseYn"
        , data        : [ {"CODE" : "Y", "NAME" : "예"}
        , {"CODE" : "N", "NAME" : "아니오"}
        ]
    , valueField  : "CODE"
      , textField   : "NAME"
        , value       : "Y"
    });
    // ------------------------------------------------------------------------
    // 인벤토리.전시범위
    // ------------------------------------------------------------------------
    fnKendoDropDownList({
        id          : 'dpRangeTp'
      , url         : "/admin/comn/getCodeList"
      , params      : {"stCommonCodeMasterCode" : "DP_RANGE_TP", "useYn" :"Y"}
      , tagId       : 'dpRangeTp'
      , autoBind    : true
      , valueField  : 'CODE'
      , textField   : 'NAME'
      , async       : true
      , isDupUrl    : 'Y'
      //, chkVal      : mallDivision
      , style       : {}
          /*blank : '선택',*/
    });

    // ------------------------------------------------------------------------
    // 이벤트 등록
    // ------------------------------------------------------------------------
    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - --
    // # 페이지유형 이벤트
    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - --
    $("#comboPageTp").change(function() {

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
        // 페이지 추가 버튼 노출
        $('#fnBtnPageNewTop').show();
        $('#fnBtnPageNew').show();
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
        //       // 페이지 추가 버튼 숨김
        $('#fnBtnPageNewTop').hide();
        $('#fnBtnPageNew').hide();
        // 페이지 수정버튼 숨김
        $('#buttonPageUpd').hide();
        $('#buttonPageDel').hide();

      }

      // 몰구분 콤보 Set
      fnKendoDropDownList({
          id          : 'comboMallDivision'
        , url         : "/admin/comn/getCodeList"
        , params      : {"stCommonCodeMasterCode" : "MALL_DIV", "useYn" :"Y"}
        , tagId       : 'comboMallDivision'
        , autoBind    : true
        , valueField  : 'CODE'
        , textField   : 'NAME'
        , async       : true
        , isDupUrl    : 'Y'
        , chkVal      : mallDivision
        , style       : {}
        /*blank : '선택',*/
      });
      // 몰구분 값 Set
      $('#inputForm input[name=comboMallDivision]').val(mallDivision);


      // Tree 데이터 초기화
      treeView.destroy();
      // Tree 재조회
      fnInitTree();

      // 상세영역 초기화 (페이지상세, 인벤토리그리드)
      fnInitDetl();

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
    // # 미사용포함여부
    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - --
    $("input:checkbox[name=checkUseAllYn]").click(function() {
        //console.log('# 클릭이벤트...');

        // Tree 재조회 실행
        if ($("input:checkbox[name=checkUseAllYn]").is(":checked") == true) {
            useAllYn = 'Y';
        }
        else {
            useAllYn = 'N';
        }
        $('#useAllYn').val(useAllYn);

        // 기존 Tree 폐기
        treeView.destroy();
        // Tree 초기화(조회)
        fnInitTree();

        // 상세영역 초기화 (페이지상세, 인벤토리그리드)
        fnInitDetl();
        //fnInitPageDetlGrid('');
    });

    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - --
    // # 입력제한
    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - --
    $(".lengthCheck").on("keyup", function (e) {
      //console.log('# bindKeyupEvents.keyup');
      // 길이 제한
      const $titleLength = $(this).parent().find(".currentInput-length")[0];
      const MAX_LENGTH = !!this.maxLength ? this.maxLength : null;
      const _value = $(this).val();
      if (MAX_LENGTH && _value.length > MAX_LENGTH) {
        $(this).val(_value.slice(0, MAX_LENGTH));
      }
      //$titleLength.innerHTML = $(this).val().length;

      // 문자 제한
      //console.log('# bindKeyupEvents this.name :: ' + this.name);
      // if (this.name == "pagePageCd" || this.name == "inventoryCd") {
      //   const regExp = /[^0-9a-zA-Z.\-]/gi;
      //   const _value = $(this).val();

      //   if (_value.match(regExp)) {
      //     $(this).val(_value.replace(regExp, ""));
      //   }
      // }


    })

    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - --
    // # 인벤토리.사용여부 변경
    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - --
    $("#inventoryUseYnSearch").change(function() {

      // 페이지 선택 체크
      if (selectedPageData == null || selectedPageData == undefined || selectedPageData == 'undefined') {
        fnMessage('', '전시 페이지를 선택해주세요.', '');
        $("#inventoryUseYnSearch").data("kendoDropDownList").value("");
        return false;
      }
      // 인벤토리리스트조회
      selectInventoryList(selectedPageData);
      //console.log('# 요기요기 :: ', $('#inventoryUseYnSearch').val());

    });

  }
  // ---------------Initialize Option Box End ---------------------------------

  // ==========================================================================
  // # Tree 로우 선택
  // ==========================================================================
  function fnClickTree (event) {
    //console.log('# fnClickTree Start');

    // ------------------------------------------------------------------------
    // 선택 Tree Data조회 (selectedPageData Set)
    // ------------------------------------------------------------------------
    getTreeSelectData();
    //$('#inputForm').bindingForm( {'rows':data}, 'rows' );

    if (selectedPageData != null) {
      // 페이지상세(Tree) 선택

      // ----------------------------------------------------------------------
      // # 선택한 Tree 페이지정보 Set
      // ----------------------------------------------------------------------
      fnSelectPageDataSet(selectedPageData);
      //console.log('# >> selectedPageData :: ', JSON.stringify(selectedPageData));
    }
  }


  // ==========================================================================
  // # 버튼-초기화
  // ==========================================================================
  function fnBtnClear() {
      // Tree 선택 초기화
      treeView.select($());
      // 입력값 초기화
      fnInitInput();
  }

  // ==========================================================================
  // # 버튼-페이지추가(최상위Tree)
  // ==========================================================================
  function fnBtnPageNewTop() {
    //console.log('# selectedPageData :: ' + JSON.stringify(selectedPageData));
    // 입력값 초기화
    //fnInitInput();
    // mode
    $('#mode').val("page.insert");
    // parentsPageId
    $('#inputPageForm input[name=parentsPageId]').val(0);
    // depth설정
    $('#inputPageForm input[name=depth]').val(1);
    // 페이지코드
    $('#pagePageCd').prop('disabled', false);

    // 입력항목 초기화
    // 순번
    $('#inputPageForm input[name=pageSort]').val("");
    // 페이지코드
    $('#inputPageForm input[name=pagePageCd]').val("");
    // 페이지명
    $('#inputPageForm input[name=pagePageNm]').val("");
    // 사용여부
    fnKendoDropDownList({
        id          : "pageUseYn"
      , data        : [ {"CODE" : "Y", "NAME" : "예"}
                      , {"CODE" : "N", "NAME" : "아니오"}
                      ]
      , valueField  : "CODE"
      , textField   : "NAME"
    });

    // 팝업오픈
    document.documentElement.scrollTop = 0;
    document.documentElement.scrollTop = 0;
    // 팝업종류 노출/숨김 처리
    $('#pagePopup').show();
    $('#inventoryPopup').hide();
    //$('#inputForm').formClear(true);
    fnKendoInputPoup({height:"auto" ,width:"700px", title:{ nullMsg :'페이지 생성' } } );
  }



  // ==========================================================================
  // # 버튼-하위페이지추가
  // ==========================================================================
  function fnBtnPageNew() {

    //if (selectedDpPageId == null || selectedDpPageId == undefined || selectedDpPageId == 'undefined') {
    //  fnMessage('', '전시 페이지를 선택해주세요.', '');
    //  return false;
    //}
    if (selectedPageData == null || selectedPageData == undefined || selectedPageData == 'undefined') {
      fnMessage('', '전시 페이지를 선택해주세요.', '');
      return false;
    }

    //console.log('# selectedDpPageId :: ' + selectedDpPageId);
    //console.log('# selectedPageData :: ' + JSON.stringify(selectedPageData));

    // mode
    $('#mode').val("page.insert");
    // parentsPageId
    $('#inputPageForm input[name=parentsPageId]').val(Number(selectedPageData.dpPageId));
    // depth설정
    $('#inputPageForm input[name=depth]').val(Number(selectedPageData.depth)+1);
    // 페이지코드
    $('#pagePageCd').prop('disabled', false);

    // 입력항목 초기화
    // 순번
    $('#inputPageForm input[name=pageSort]').val("");
    // 페이지코드
    $('#inputPageForm input[name=pagePageCd]').val("");
    // 페이지명
    $('#inputPageForm input[name=pagePageNm]').val("");
    // 사용여부
    fnKendoDropDownList({
        id          : "pageUseYn"
      , data        : [ {"CODE" : "Y", "NAME" : "예"}
                      , {"CODE" : "N", "NAME" : "아니오"}
                      ]
      , valueField  : "CODE"
      , textField   : "NAME"
    });

    // 팝업오픈
    document.documentElement.scrollTop = 0;
    document.documentElement.scrollTop = 0;
    //$('#inputForm').formClear(true);
    // 팝업종류 노출/숨김 처리
    $('#pagePopup').show();
    $('#inventoryPopup').hide();
    fnKendoInputPoup({height:"auto" ,width:"700px", title:{ nullMsg :'페이지 생성' } } );

  }

  // ==========================================================================
  // # 버튼-페이지수정
  // ==========================================================================
  function fnBtnPageEdit() {

    // selectedPageData 가 널인 경우 조회(Tree 선택 시 이미 값이 존재함)
    if (selectedPageData == undefined || selectedPageData == null || selectedPageData == '') {
      getTreeSelectData();
    }
    //console.log('# selectedPageData :: ' + JSON.stringify(selectedPageData));

    // mode
    $('#mode').val("page.update");
    // 상위페이지ID
    $('#parentsPageId').val(selectedPageData.prntsPageId);
    // depth
    $('#depth').val(Number(selectedPageData.depth));
    // sort
    $('#pageSort').val(selectedPageData.sort);
    // 페이지코드
    $('#pagePageCd').val(selectedPageData.pageCd);
    $('#pagePageCd').prop('disabled', true);
    // 페이지명
    $('#pagePageNm').val(selectedPageData.pageNm);
    // 사용여부
    fnKendoDropDownList({
        id          : "pageUseYn"
      , data        : [ {"CODE" : "Y", "NAME" : "예"}
                      , {"CODE" : "N", "NAME" : "아니오"}
                      ]
      , valueField  : "CODE"
      , textField   : "NAME"
      , value       : selectedPageData.useYn
    });
    //$("#pageUseYn").val(selectedPageData.useYn);
    //$("#pageUseYn").val(selectedPageData.useYn).prop("selected", true); //값이 1인 option 선택

    // 팝업오픈
    document.documentElement.scrollTop = 0;
    document.documentElement.scrollTop = 0;
    //$('#inputForm').formClear(true);
    // 팝업종류 노출/숨김 처리
    $('#pagePopup').show();
    $('#inventoryPopup').hide();
    fnKendoInputPoup({height:"auto" ,width:"700px", title:{ nullMsg :'페이지 수정' } } );
  }

  // ==========================================================================
  // # 버튼-페이지삭제처리
  // ==========================================================================
  function fnBtnPageDel() {

    if (selectedDpPageId == null || selectedDpPageId == undefined || selectedDpPageId == 'undefined') {
      fnMessage('', '전시 페이지를 선택해주세요.', '');
      return false;
    }
    //alert('# selectedDpPageId :: ' + selectedDpPageId);
    // mode
    $('#mode').val("page.delete");
    fnBtnPageSave();
  }

  // ==========================================================================
  // # 버튼-페이지저장
  // ==========================================================================
  function fnBtnPageSave() {

    var url;
    var cbId;
    var isAction;
    var confirmMsg;
    var mode = $('#mode').val();

    if (mode != null && mode == 'page.insert') {
      // ----------------------------------------------------------------------
      // 페이지 등록
      // ----------------------------------------------------------------------
      // 확인메시지
      confirmMsg = '저장하시겠습니까?';
      // 필수여부
      $('#inputPageForm input[name=pageSort]'  ).prop("required", true);
      $('#inputPageForm input[name=pagePageCd]').prop("required", true);
      $('#inputPageForm input[name=pagePageNm]').prop("required", true);
      $('#inputPageForm input[name=pageUseYn]' ).prop("required", true);

      // Param Set
      var data = $('#inputPageForm').formSerialize(true);
      var pageData = new Object();
      pageData.sort         = Number(data.pageSort);
      pageData.pageCd       = data.pagePageCd;        // 페이지코드(사용자입력)
      pageData.pageNm       = data.pagePageNm;        // 페이지명  (사용자입력)
      pageData.useYn        = data.pageUseYn;         // 사용여부  (사용자선택)
      pageData.prntsPageId  = data.parentsPageId;     // 팝업오픈 시 Set
      pageData.depth        = data.depth;             // 팝업오픈 시 Set
      // 접속
      url  = '/admin/display/manage/addPage';
      cbId = mode;
      isAction = 'insert';
    }
    else if (mode != null && mode == 'page.update') {
      // ----------------------------------------------------------------------
      // 페이지 수정
      // ----------------------------------------------------------------------
      // 확인메시지
      confirmMsg = '수정하시겠습니까?';
      // 필수여부
      $('#inputPageForm input[name=pageSort]'  ).prop("required", true);
      $('#inputPageForm input[name=pagePageCd]').prop("required", false);
      $('#inputPageForm input[name=pagePageNm]').prop("required", true);
      $('#inputPageForm input[name=pageUseYn]' ).prop("required", true);
      // Param Set
      var data = $('#inputPageForm').formSerialize(true);

      var pageData = new Object();
      pageData.dpPageId     = Number(selectedPageData.dpPageId);  // tjssxor
      pageData.sort         = Number(data.pageSort);
      pageData.pageNm       = data.pagePageNm;
      pageData.useYn        = data.pageUseYn;
      pageData.prntsPageId  = "0";
      pageData.depth        = 1;
      // 접속
      url  = '/admin/display/manage/putPage';
      cbId = mode;
      isAction = 'update';
    }
    else if (mode != null && mode == 'page.delete') {
      // ----------------------------------------------------------------------
      // 페이지 삭제
      // ----------------------------------------------------------------------
      // 확인메시지
      confirmMsg = '삭제하시겠습니까?';
      // 필수여부
      $('#inputPageForm input[name=pageSort]'  ).prop("required", false);
      $('#inputPageForm input[name=pagePageCd]').prop("required", false);
      $('#inputPageForm input[name=pagePageNm]').prop("required", false);
      $('#inputPageForm input[name=pageUseYn]' ).prop("required", false);
      // Param Set
      var data = $('#inputPageForm').formSerialize(true);

      var pageData = new Object();
      pageData.dpPageId = selectedPageData.dpPageId;
      // 접속
      url  = '/admin/display/manage/delPage';
      cbId = mode;
      isAction = 'update';
    }
    else {
      fnMessage('', '페이지 기능 오류입니다.', '');
      return false;
    }

    // ------------------------------------------------------------------------
    // Validation & Ajax Call
    // ------------------------------------------------------------------------
    if(data.rtnValid) {

      // Ajax Call
      fnKendoMessage({message:fnGetLangData({key :"",nullMsg : confirmMsg }), type : "confirm", ok :
        function(){
          //console.log('# inventoryData :: ' + JSON.stringify(pageData));
          fnAjax({
              url     : url
            , params  : {"pageInfoJsonString" : JSON.stringify(pageData)}
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

  }

  // ==========================================================================
  // # 버튼-인벤토리순번저장처리
  // ==========================================================================
  function fnBtnInventorySortSave() {

    var inventoryGridArr = $("#inventoryGrid").data("kendoGrid").dataSource.data();

    if (inventoryGridArr == null || inventoryGridArr.length <= 0) {
      fnMessage('', '대상 인벤토리가 없습니다.', '');
      return false;
    }

    //console.log('# inventoryGridArr.length :: ' + inventoryGridArr.length);
    //console.log('# inventoryGridArr :: ' + JSON.stringify(inventoryGridArr));

    var sortArr = new Array() ;

    for (var i = 0; i < inventoryGridArr.length; i++) {

      //console.log('# inventoryGridArr['+i+'].sort :: ' + inventoryGridArr[i].sort);
      var sortData = new Object();
      sortData.dpInventoryId  = inventoryGridArr[i].dpInventoryId;
      sortData.sort           = Number(inventoryGridArr[i].sort);

      sortArr.push(sortData);
    }

    var sortJsonData = JSON.stringify(sortArr);
    //console.log('# sortJsonData :: ' + sortJsonData);


    fnKendoMessage({message:'인벤토리 순서를 저장하시겠습니까?', type : "confirm" , ok : function(){
      var url = '/admin/display/manage/putInventorySort';
      var cbId = 'inventory.sort';
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
  // # 버튼-그리드-인벤토리신규
  // ==========================================================================
  function fnBtnInventoryNew() {

    // 전시페이지(Tree) 선택 체크
    if (selectedPageData == null || selectedPageData == undefined || selectedPageData == 'undefined') {
      fnMessage('', '전시 페이지를 선택해주세요.', '');
      return false;
    }
    // 페이지유형.카테고리페이지인 경우 0depth 체크
    if ($('#comboPageTp').val() == 'PAGE_TP.CATEGORY') {
      var selectedDepth = 0;

      if (selectedPageData.depth != null && selectedPageData.depth != 'null' && selectedPageData.depth != undefined && selectedPageData.depth != 'undefined' && selectedPageData.depth != '') {
        selectedDepth   = Number(selectedPageData.depth);
      }

      if (selectedDepth < 1) {
        fnMessage('', '카테고리의 그룹은 인벤토리를 추가할 수 없습니다.', '');
        return false;
      }
    }
    else {
      //gbSelectedDepth     = null;
      //gbSelectedIlCtgryId = null;
    }
    //console.log('# 페이지유형 : ', $('#comboPageTp').val());
    //console.log('# selectedPageData : ', JSON.stringify(selectedPageData));

    // 입력값 초기화
    //fnInitInput();
    // mode
    $('#mode').val("inventory.insert");

    // 입력항목 초기화
    // 순번
    $('#inventorySort').val(999);
    // 인벤토리코드
    $('#inventoryCd').val("");
    $('#inventoryCd').prop('disabled', false);
    // 인벤토리명
    $('#inventoryNm').val("");
    // 인벤토리.사용여부
    fnKendoDropDownList({
        id          : "inventoryUseYn"
      , data        : [ {"CODE" : "Y", "NAME" : "예"}
                      , {"CODE" : "N", "NAME" : "아니오"}
                      ]
      , valueField  : "CODE"
      , textField   : "NAME"
    });
    // 인벤토리.전시범위
    fnKendoDropDownList({
        id          : 'dpRangeTp'
      , url         : "/admin/comn/getCodeList"
      , params      : {"stCommonCodeMasterCode" : "DP_RANGE_TP", "useYn" :"Y"}
      , tagId       : 'dpRangeTp'
      , autoBind    : true
      , valueField  : 'CODE'
      , textField   : 'NAME'
      //, chkVal      : mallDivision
      , style       : {}
          /*blank : '선택',*/
    });
    // 인벤토리.구좌타입Lv1
    fnKendoDropDownList({
        id          : 'contsLevel1Tp'
      , url         : "/admin/comn/getCodeList"
      , params      : {"stCommonCodeMasterCode" : "DP_CONTENTS_TP", "useYn" :"Y"}
      , tagId       : 'contsLevel1Tp'
      , autoBind    : true
      , valueField  : 'CODE'
      , textField   : 'NAME'
      , style       : {}
      /*blank : '선택',*/
    });
    $("#contsLevel1Tp").data("kendoDropDownList").enable(true);
    // 인벤토리.구좌타입Lv2
    fnKendoDropDownList({
        id          : 'contsLevel2Tp'
      , url         : "/admin/comn/getCodeList"
      , params      : {"stCommonCodeMasterCode" : "DP_CONTENTS_TP", "useYn" :"Y"}
      , tagId       : 'contsLevel2Tp'
      , autoBind    : true
      , valueField  : 'CODE'
      , textField   : 'NAME'
      , style       : {}
      /*blank : '선택',*/
    });
    $("#contsLevel2Tp").data("kendoDropDownList").enable(true);
    // 인벤토리.구좌타입Lv3
    fnKendoDropDownList({
        id          : 'contsLevel3Tp'
      , url         : "/admin/comn/getCodeList"
      , params      : {"stCommonCodeMasterCode" : "DP_CONTENTS_TP", "useYn" :"Y"}
      , tagId       : 'contsLevel3Tp'
      , autoBind    : true
      , valueField  : 'CODE'
      , textField   : 'NAME'
      , style       : {}
      /*blank : '선택',*/
    });
    $("#contsLevel3Tp").data("kendoDropDownList").enable(true);
    // 인벤토리.구좌상세정보1
    $('#contsLevel1Desc').val("");
    $('#contsLevel1Desc').prop('disabled', false);
    // 인벤토리.구좌상세정보2
    $('#contsLevel2Desc').val("");
    $('#contsLevel2Desc').prop('disabled', false);
    // 인벤토리.구좌상세정보3
    $('#contsLevel3Desc').val("");
    $('#contsLevel3Desc').prop('disabled', false);

    // 팝업오픈
    document.documentElement.scrollTop = 0;
    document.documentElement.scrollTop = 0;
    // 팝업종류 노출/숨김 처리
    $('#pagePopup').hide();
    $('#inventoryPopup').show();
    //$('#inputForm').formClear(true);
    fnKendoInputPoup({height:"auto" ,width:"860px", title:{ nullMsg :'인벤토리 생성' }, minWidth: 800 } );
  }

  // ==========================================================================
  // # 버튼-그리드-인벤토리수정
  // ==========================================================================
  function fnBtnInventoryEdit(dataItem) {

    if (selectedPageData == null || selectedPageData == undefined || selectedPageData == 'undefined') {
      fnMessage('', '전시 페이지를 선택해주세요.', '');
      return false;
    }

    // 입력값 초기화
    //fnInitInput();
    // mode
    $('#mode').val("inventory.update");

    // 입력항목 초기화
    // 순번
    $('#inventorySort').val(selectedInventoryData.sort);
    // 인벤토리코드
    $('#inventoryCd').val(selectedInventoryData.inventoryCd);
    $('#inventoryCd').prop('disabled', true);
    // 인벤토리명
    $('#inventoryNm').val(selectedInventoryData.inventoryNm);
    // 인벤토리.사용여부
    fnKendoDropDownList({
        id          : "inventoryUseYn"
      , data        : [ {"CODE" : "Y", "NAME" : "예"}
                      , {"CODE" : "N", "NAME" : "아니오"}
                      ]
      , valueField  : "CODE"
      , textField   : "NAME"
      , value       : selectedInventoryData.useYn
    });
    // 인벤토리.전시범위
    fnKendoDropDownList({
        id          : 'dpRangeTp'
      , url         : "/admin/comn/getCodeList"
      , params      : {"stCommonCodeMasterCode" : "DP_RANGE_TP", "useYn" :"Y"}
      , tagId       : 'dpRangeTp'
      , autoBind    : true
      , valueField  : 'CODE'
      , textField   : 'NAME'
      , value       : selectedInventoryData.dpRangeTp
      , style       : {}
    });
    // 인벤토리.구좌타입Lv1
    fnKendoDropDownList({
        id          : 'contsLevel1Tp'
      , url         : "/admin/comn/getCodeList"
      , params      : {"stCommonCodeMasterCode" : "DP_CONTENTS_TP", "useYn" :"Y"}
      , tagId       : 'contsLevel1Tp'
      , autoBind    : true
      , valueField  : 'CODE'
      , textField   : 'NAME'
      , value       : selectedInventoryData.contsLevel1Tp
      , style       : {}
    });
    $("#contsLevel1Tp").data("kendoDropDownList").enable(false);
    // 인벤토리.구좌타입Lv2
    fnKendoDropDownList({
        id          : 'contsLevel2Tp'
      , url         : "/admin/comn/getCodeList"
      , params      : {"stCommonCodeMasterCode" : "DP_CONTENTS_TP", "useYn" :"Y"}
      , tagId       : 'contsLevel1Tp'
      , autoBind    : true
      , valueField  : 'CODE'
      , textField   : 'NAME'
      , value       : selectedInventoryData.contsLevel2Tp
      , style       : {}
    });
    $("#contsLevel2Tp").data("kendoDropDownList").enable(false);
    // 인벤토리.구좌타입Lv3
    fnKendoDropDownList({
        id          : 'contsLevel3Tp'
      , url         : "/admin/comn/getCodeList"
      , params      : {"stCommonCodeMasterCode" : "DP_CONTENTS_TP", "useYn" :"Y"}
      , tagId       : 'contsLevel1Tp'
      , autoBind    : true
      , valueField  : 'CODE'
      , textField   : 'NAME'
      , value       : selectedInventoryData.contsLevel3Tp
      , style       : {}
    });
    $("#contsLevel3Tp").data("kendoDropDownList").enable(false);
    // 인벤토리.구좌상세정보1
    $('#contsLevel1Desc').val(selectedInventoryData.contsLevel1Desc);
    if (selectedInventoryData.contsLevel1Tp != null && selectedInventoryData.contsLevel1Tp != 'null' &&
        selectedInventoryData.contsLevel1Tp != ''   && selectedInventoryData.contsLevel1Tp != 'DP_CONTENTS_TP.NONE') {
      // 활성
      $('#contsLevel1Desc').prop('disabled', false);
    }
    else {
      // 비활성
      $('#contsLevel1Desc').prop('disabled', true);
    }
    // 인벤토리.구좌상세정보2
    $('#contsLevel2Desc').val(selectedInventoryData.contsLevel2Desc);
    if (selectedInventoryData.contsLevel2Tp != null && selectedInventoryData.contsLevel2Tp != 'null' &&
        selectedInventoryData.contsLevel2Tp != ''   && selectedInventoryData.contsLevel2Tp != 'DP_CONTENTS_TP.NONE') {
      // 활성
      $('#contsLevel2Desc').prop('disabled', false);
    }
    else {
      // 비활성
      $('#contsLevel2Desc').prop('disabled', true);
    }
    // 인벤토리.구좌상세정보3
    $('#contsLevel3Desc').val(selectedInventoryData.contsLevel3Desc);
    if (selectedInventoryData.contsLevel3Tp != null && selectedInventoryData.contsLevel3Tp != 'null' &&
        selectedInventoryData.contsLevel3Tp != ''   && selectedInventoryData.contsLevel3Tp != 'DP_CONTENTS_TP.NONE') {
      // 활성
      $('#contsLevel3Desc').prop('disabled', false);
    }
    else {
      // 비활성
      $('#contsLevel3Desc').prop('disabled', true);
    }

    // 팝업오픈
    document.documentElement.scrollTop = 0;
    document.documentElement.scrollTop = 0;
    // 팝업종류 노출/숨김 처리
    $('#pagePopup').hide();
    $('#inventoryPopup').show();
    //$('#inputForm').formClear(true);
    fnKendoInputPoup({height:"auto" ,width:"860px", title:{ nullMsg :'인벤토리 수정' } } );

  }

  // ==========================================================================
  // # 버튼-그리드-인벤토리삭제처리
  // ==========================================================================
  //function fnBtnInventoryDel(inInventoryId) {
  function fnBtnInventoryDel() {


    if (selectedInventoryData == null || selectedInventoryData == '' || selectedInventoryData == undefined || selectedInventoryData == 'undefined') {
      fnMessage('', '전시 인벤토리를 선택해주세요.', '');
      return false;
    }
    if (selectedInventoryData.dpInventoryId == null || selectedInventoryData.dpInventoryId == '' || selectedInventoryData.dpInventoryId == undefined || selectedInventoryData.dpInventoryId == 'undefined') {
      fnMessage('', '전시 인벤토리ID를 확인하세요.', '');
      return false;
    }

    // mode
    $('#mode').val("inventory.delete");

    fnBtnInventorySave();
  }

  // ==========================================================================
  // # 버튼-인벤토리저장
  // ==========================================================================
  function fnBtnInventorySave() {

    var url;
    var cbId;
    var isAction;
    var confirmMsg;
    var mode = $('#mode').val();

    if (mode != null && mode == 'inventory.insert') {
      // ----------------------------------------------------------------------
      // 인벤토리 등록
      // ----------------------------------------------------------------------
      // 확인메시지
      confirmMsg = '저장하시겠습니까?';
      // 필수여부
      $('#inputInventoryForm input[name=inventorySort]' ).prop("required", true);
      $('#inputInventoryForm input[name=inventoryCd]'   ).prop("required", true);
      $('#inputInventoryForm input[name=inventoryNm]'   ).prop("required", true);
      $('#inputInventoryForm input[name=inventoryUseYn]').prop("required", true);
      $('#inputInventoryForm input[name=dpRangeTp]'     ).prop("required", true);
      $('#inputInventoryForm input[name=contsLevel1Tp]' ).prop("required", true);

      // Param Set
      var data = $('#inputInventoryForm').formSerialize(true);

      // var isValid = inventoryCdValidate("inventoryCd");

      // if( !isValid ) {
      //   fnKendoMessage({
      //     message: "인벤토리 코드는 숫자, 영어 대소문자, '-'만 입력가능합니다.",
      //     ok: function() {
      //       $("#inventoryCd").val("").focus();
      //     }
      //   });
      //   return false;
      // }

      var inventoryData = new Object();
      inventoryData.sort            = Number(data.inventorySort);     // 순서
      inventoryData.inventoryCd     = data.inventoryCd;               // 인벤토리코드(사용자입력)
      inventoryData.inventoryNm     = data.inventoryNm;               // 인벤토리명  (사용자입력)
      inventoryData.useYn           = data.inventoryUseYn;            // 사용여부  (사용자선택)
      inventoryData.dpRangeTp       = data.dpRangeTp;                 // 전시범위
      inventoryData.contsLevel1Tp   = data.contsLevel1Tp;             // Lv1.TP
      inventoryData.contsLevel1Desc = data.contsLevel1Desc;           // Lv1.DESC
      inventoryData.contsLevel2Tp   = data.contsLevel2Tp;             // Lv2.TP
      inventoryData.contsLevel2Desc = data.contsLevel2Desc;           // Lv2.DESC
      inventoryData.contsLevel3Tp   = data.contsLevel3Tp;             // Lv3.TP
      inventoryData.contsLevel3Desc = data.contsLevel3Desc;           // Lv3.DESC
      inventoryData.pageTp          = $('#comboPageTp').val();        // 페이지유형
      inventoryData.dpPageId        = selectedDpPageId;               // 선택한페이지ID
      if ($('#comboPageTp').val() == 'PAGE_TP.CATEGORY') {
        // 몰구분(페이지유형.카테고리)
        inventoryData.mallDiv         = $("#comboMallDivision").val();
        // 페이지유형.카테고리페이지인 경우 카테고리depth Set
        inventoryData.ctgryDepth = Number(selectedPageData.depth);
      }

      // 접속
      url  = '/admin/display/manage/addInventory';
      cbId = mode;
      isAction = 'insert';

    }
    else if (mode != null && mode == 'inventory.update') {
      // ----------------------------------------------------------------------
      // 인벤토리 수정
      // ----------------------------------------------------------------------
      // 확인메시지
      confirmMsg = '수정하시겠습니까?';
      // 필수여부
      $('#inputInventoryForm input[name=inventorySort]' ).prop("required", true);
      $('#inputInventoryForm input[name=inventoryCd]'   ).prop("required", false);
      $('#inputInventoryForm input[name=inventoryNm]'   ).prop("required", true);
      $('#inputInventoryForm input[name=inventoryUseYn]').prop("required", true);
      $('#inputInventoryForm input[name=dpRangeTp]'     ).prop("required", true);
      $('#inputInventoryForm input[name=contsLevel1Tp]' ).prop("required", true);
      // Param Set
      var data = $('#inputInventoryForm').formSerialize(true);

      var inventoryData = new Object();
      inventoryData.dpInventoryId   = Number(selectedInventoryData.dpInventoryId);  // 인벤토리ID
      inventoryData.sort            = Number(data.inventorySort);                   // 순서
      inventoryData.inventoryNm     = data.inventoryNm;                             // 인벤토리명  (사용자입력)
      inventoryData.useYn           = data.inventoryUseYn;                          // 사용여부  (사용자선택)
      inventoryData.dpRangeTp       = data.dpRangeTp;                               // 전시범위
      // 접속
      url  = '/admin/display/manage/putInventory';
      cbId = mode;
      isAction = 'update';
    }
    else if (mode != null && mode == 'inventory.delete') {
      // ----------------------------------------------------------------------
      // 인벤토리 삭제
      // ----------------------------------------------------------------------
      // 확인메시지
      confirmMsg = '삭제하시겠습니까?';
      // 필수여부
      $('#inputInventoryForm input[name=inventorySort]' ).prop("required", false);
      $('#inputInventoryForm input[name=inventoryCd]'   ).prop("required", false);
      $('#inputInventoryForm input[name=inventoryNm]'   ).prop("required", false);
      $('#inputInventoryForm input[name=inventoryUseYn]').prop("required", false);
      $('#inputInventoryForm input[name=dpRangeTp]'     ).prop("required", false);
      $('#inputInventoryForm input[name=contsLevel1Tp]' ).prop("required", false);
      // Param Set
      var data = $('#inputInventoryForm').formSerialize(true);

      var inventoryData = new Object();
      inventoryData.dpInventoryId   = Number(selectedInventoryData.dpInventoryId);  // 인벤토리ID
      // 접속
      url  = '/admin/display/manage/delInventory';
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

      // Lv.1 구좌타입 체크
      if (mode != null && mode == 'inventory.insert') {
        if (inventoryData.contsLevel1Tp == null || inventoryData.contsLevel1Tp == 'null' || inventoryData.contsLevel1Tp == '' || inventoryData.contsLevel1Tp == 'DP_CONTENTS_TP.NONE') {
          fnMessage('', 'Lv.1의 구좌타입을 선택해주세요.', '');
          $('#contsLevel1Tp').focus();
          return false;
        }
      }

      // Ajax Call
      fnKendoMessage({message:fnGetLangData({key :"",nullMsg : confirmMsg }), type : "confirm", ok :
        function(){
          //console.log('# inventoryData :: ' + JSON.stringify(inventoryData));
          fnAjax({
              url     : url
            , params  : {"inventoryInfoJsonString" : JSON.stringify(inventoryData)}
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

  }

  // ==========================================================================
  // # 버튼-팝업-닫기
  // ==========================================================================
  function fnPopupClose(){

    var kendoWindow =$('#kendoPopup').data('kendoWindow');
    kendoWindow.close();

  }


  // ==========================================================================
  // # fnInitTree
  // ==========================================================================
  function fnInitTree() {
      //console.log('# fnInitTree Start');
      // 선택페이지 초기화
      selectedPageData = null;

      var callUrl = '';

      if (pageTp == 'PAGE_TP.CATEGORY') {
        // 카테고리
        callUrl = '/admin/display/manage/selectDpCategoryList?'+ "pageTp=" + pageTp + '&mallDiv=' + mallDivision + "&useAllYn=" + useAllYn;
      }
      else {
        // 페이지
        callUrl = '/admin/display/manage/selectDpPageList?' + "pageTp=" + pageTp+ "&useAllYn=" + useAllYn;
      }
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

  }

  // ==========================================================================
  // # 상세영역 초기화
  // ==========================================================================
  function fnInitDetl() {

    // 페이지 영역 초기화
    $('#sort').html("");
    $('#pageCd').html("");
    $('#pageNm').html("");
    $('#pageFullPath').html(" ");
    $('#useYnNm').html("");

    // 인베토리 그리드 초기화
    // 인벤토리 그리드 초기화(이 때 dpPageId 가 Set 됨)
    fnInitInventoryGrid();
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
        dpPageId = inData.dpPageId;

        return false;
      }

      //$('#pageMgrInitArea').hide();
      //$('#pageMgrButtonArea').show();
      // 페이지 정보 영역 노출 초기화
      $('#trPageInit').hide();
      $('#trPageSelected').show();

      // ----------------------------------------------------------------------
      // 1. 페이지정보상세 Set
      // ----------------------------------------------------------------------
      $('#sort').html(inData.sort);
      $('#pageCd').html(inData.pageCd);
      $('#pageNm').html(inData.pageNm);
      $('#pageFullPath').html(inData.pageFullPath);
      $('#useYnNm').html(inData.useYnNm);

      // ------------------------------------------------------------------------
      // 2. 인벤토리리스트조회
      // ------------------------------------------------------------------------
      selectInventoryList(inData);
      //if (inData.dpPageId != null && inData.dpPageId != 0) {
      //  //fnInitInventoryGrid(inData.dpPageId);
      //
      //  // 페이지ID Set
      //  dpPageId = inData.dpPageId;
      //  // 인벤토리 그리드 초기화(이 때 dpPageId 가 Set 됨)
      //  fnInitInventoryGrid();
      //  // 조회 실행
      //  let data = $("#inputForm").formSerialize(true);
      //  inventoryGridDs.read(data);
      //}
    }
  }

  // ==========================================================================
  // # 인벤토리리스트조회
  // ==========================================================================
  function selectInventoryList(inData){

    if (inData.dpPageId != null && inData.dpPageId != 0) {
      //fnInitInventoryGrid(inData.dpPageId);

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
    //console.log('# 인벤토리리스트조회 Start ('+dpPageId+')');

    var inParamDepth;
    var inParamUseYn;

    if (pageTp == 'PAGE_TP.CATEGORY') {
      //console.log('# depthInfo :: ' + JSON.stringify(selectedPageData));
      if (selectedPageData != null && selectedPageData != undefined && selectedPageData != 'undefined') {
        inParamDepth    = selectedPageData.depth;
      }
    }

    inParamUseYn = $('#inventoryUseYnSearch').val();


    // 페이징없는 그리드
    inventoryGridDs = fnGetEditPagingDataSource({
      url      : "/admin/display/manage/selectInventoryList?" + "pageTp=" + pageTp + "&dpPageId="+dpPageId + "&mallDiv=" + mallDivision + "&depth=" + inParamDepth + "&useYn=" + inParamUseYn,
    });

    inventoryGridOpt = {
          dataSource  : inventoryGridDs
        //, toolbar     : [
        //                  { name: 'cSave' ,  text: '순번저장', imageClass: "btn-point btn-m", className: "class-name-save-sort", iconClass: "k-icon" }
        //                , { name: 'cSave' ,  text: '&plus; 인벤토리 추가', imageClass: "btn-point btn-m", className: "class-name-add-inventory", iconClass: "k-icon" }
        //                , { name: 'cSave' ,  text: '저장', imageClass: "k-i-update", className: "k-custom-save", iconClass: "k-icon" }
        //                , { name: 'cancel',  text: '취소' }
        //    ]
        //, pageable    : {pageSizes: [20, 30, 50], buttonCount : 10}
        , navigatable : true
        , scrollable  : true
        , selectable  : true
        , editable    : "incell"
                        //{
                        //  confirmation: function(model) {
                        //                  return '삭제하시겠습니까? \n저장버튼을 클릭해야 반영이 완료됩니다.'
                        //                  //console.log('# model :: ' + JSON.stringify(model))
                        //                  //if (model == null || model == undefined || model == 'undefined') {
                        //                  //  return '값 없네...'
                        //                  //}
                        //                  //else {
                        //                  //  //console.log('# model :: ' + JSON.stringify(model));
                        //                  //  //return '삭제하시겠습니까? \n저장버튼을 클릭해야 반영이 완료됩니다.'
                        //                  //  //return '삭제하시겠습니까?'
                        //                  //  return fnBtnInventoryDel(model)
                        //                  //}
                        //  }
                        //}
        , resizable   : true
        , height      : 550
        , columns     : [
                          { field : "sort"            , title : "순번"          , width: "40px" , attributes : {style : "text-align:center;"}, editable:function (dataItem) {return true; }, editor: sortEditor}
                        , { field : "inventoryCd"     , title : "인벤토리 코드" , width: "120px", attributes : {style : "text-align:center;"}, editable:function (dataItem) {return false;}}
                        , { field : "dpInventoryId"   , title : "인벤토리ID"    , width: "120px", attributes : {style : "text-align:center;"}, editable:function (dataItem) {return false;}, hidden:true}
                        , { field : "inventoryNm"     , title : "인벤토리 명"   , width: "120px", attributes : {style : "text-align:center;"}, editable:function (dataItem) {return false;}}
                        , { field : "contsLevel1TpNm" , title : "Lv.1"          , width: "70px" , attributes : {style : "text-align:center;"}, editable:function (dataItem) {return false;}}
                        , { field : "contsLevel2TpNm" , title : "Lv.2"          , width: "70px" , attributes : {style : "text-align:center;"}, editable:function (dataItem) {return false;}}
                        , { field : "contsLevel3TpNm" , title : "Lv.3"          , width: "70px" , attributes : {style : "text-align:center;"}, editable:function (dataItem) {return false;}}
                        , { field : "useYnNm"         , title : "사용여부"      , width: "40px" , attributes : {style : "text-align:center;"}, editable:function (dataItem) {return false;}}
                        , { field : "management"      , title : "관리"          , width: "80px" , attributes : {style : "text-align:center;"}, editable:function (dataItem) {return false;}
                                                      , template  : function(dataItem) {
                                                          //return  "<a href='#' role='button' class='btn-gray btn-s' kind='btnInventoryEdit' >수정</a>"
                                                          //      + "&nbsp;&nbsp;"
                                                          //      + "<a href='#' role='button' class='btn-red btn-s'  kind='btnInventoryDel' >삭제</a>";

                                                    	  var saveBtn 	  =	'<button type="button" class="btn-gray btn-s" kind="btnInventoryEdit">수정 </button>' + '&nbsp;&nbsp;';
                                                    	  var deleteBtn   =	'<button type="button" class="btn-red btn-s" kind="btnInventoryDel">삭제 </button>';

                                                    	  var returnStr = '';
                                                    	  if(fnIsProgramAuth("SAVE")){
                                                    		  returnStr = saveBtn;
                                                    	  }
                                                    	  if(fnIsProgramAuth("DELETE")){
                                                    		  returnStr += deleteBtn;
                                                    	  }

                                                    	  return '<div id="pageMgrButtonArea" class="btn-area col-2">' + returnStr + '</div>';

//                                                          return  '<div id="pageMgrButtonArea" class="btn-area col-2">'
//                                                                + '<button type="button" class="btn-gray btn-s" kind="btnInventoryEdit">수정 </button>'
//                                                                + '&nbsp;&nbsp;'
//                                                                + '<button type="button" class="btn-red btn-s" kind="btnInventoryDel">삭제 </button>'
//                                                                + '</div>';

                                                        }
                          }
                        //, { command : [
                        //                {
                        //                  name      : "edit"
                        //                , text      : "수정"
                        //                , className : 'btn-gray btn-s'
                        //                , click     : fnBtnInventoryEdit
                        //                }
                        //              , {
                        //                  name      : "destroy"
                        //                , text      : "삭제"
                        //                , className : 'btn-red btn-s'
                        //                /*, click     : fnBtnInventoryDel*/
                        //                }
                        //              ]
                        //  , title           : "관리"
                        //  , width           : "80px"
                        //  , attributes      : {style: 'text-align:center;', class:'forbiz-cell-readonly'}
                        //  , headerAttributes: {style: "text-align:center;"}
                        //  }
                        ]
      //, rowTemplate : kendo.template($("#rowTemplate").html())
    };

    inventoryGrid = $('#inventoryGrid').initializeKendoGrid( inventoryGridOpt ).cKendoGrid();

    //$("#pageGrid").on("click", "tbody>tr", function () {
    //    //fnGridClick();
    //});
    //pageGrid.bind("dataBound", function() {
    //  $('#totalCnt').text(pageGridDs._total);
    //});

    // ------------------------------------------------------------------------
    // 그리드 버튼 클릭 이벤트 등록
    // ------------------------------------------------------------------------
    // * 순번저장
    $(".class-name-save-sort").click(function(e){
      e.preventDefault();
      fnBtnInventorySortSave();
    });
    // * 인벤토리 추가
    $(".class-name-add-inventory").click(function(e){
      e.preventDefault();
      fnBtnInventoryNew();
    });
    // * 인벤토리 수정
    $('#inventoryGrid').on("click", "button[kind=btnInventoryEdit]", function(e) {
      e.preventDefault();
      let dataItem = inventoryGrid.dataItem($(e.currentTarget).closest("tr"));
      selectedInventoryData = dataItem;
      //console.log('# btnInventoryEdit.dataItem :: ' + JSON.stringify(dataItem));
      //fnBtnInventoryEdit(dataItem);
      fnBtnInventoryEdit();
    });
    // * 인벤토리 삭제
    $('#inventoryGrid').on("click", "button[kind=btnInventoryDel]", function(e) {
      e.preventDefault();
      let dataItem = inventoryGrid.dataItem($(e.currentTarget).closest("tr"));
      selectedInventoryData = dataItem;
      //let param = {};
      //param.dpPageId      = dataItem.dpPageId;
      //param.dpInventoryId = dataItem.dpInventoryId;
      //console.log('# btnInventoryDel.dataItem :: ' + JSON.stringify(dataItem));
      //fnBtnInventoryDel(dataItem.dpInventoryId);
      fnBtnInventoryDel();

    });

  }

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
  // # 콜백합수
  // ==========================================================================
  function fnBizCallback(id, result) {
    //console.log('# fnBizCallback Start ('+id+', data)');
    //console.log('# result['+id+'] :: ' + JSON.stringify(result));

    switch(id){
      case 'page.insert':
        // --------------------------------------------------------------------
        // 페이지 등록
        // --------------------------------------------------------------------
        //재조회
        //treeView.destroy();
        //fnInitTree();
        //console.log('# page.insert.result :: ' + JSON.stringify(result));
        if (result.resultCode == '0000') {

          // Tree 추가
          // 하위여부
          result.detail.isleaf = 'false';   // 생략해도 됨, 폴더이미지 관련은 html 파일의 treeview-template 참조할 것
          // 페이지경로
          if (result.detail.depth == '1') {
            result.detail.pageFullPath = result.detail.pageNm;
          }
          else {
            result.detail.pageFullPath = selectedPageData.pageFullPath + ' > ' + result.detail.pageNm;
          }

          fnTreeDataAdd(result.detail);
          //console.log('# result.detail', JSON.stringify(result.detail));
          // 팝업닫기
          fnPopupClose();
          // 완료 메시지
          fnMessage('', '페이지가 등록되었습니다.', '');
        }
        else {
          //fnMessage('', '페이지 등록이 실패하였습니다.\n다시 시도해 주십시오.', '');
          fnMessage('', '['+result.resultCode+'] ' + result.resultMessage, '');
        }

        break;
      case 'page.update':
        // --------------------------------------------------------------------
        // 페이지 수정
        // --------------------------------------------------------------------
        if (result.resultCode == '0000') {

          // Tree 갱신
          fnTreeDataPut(result.detail);
          // 페이지정보 상세 갱신
          fnSelectPageDataSet(result.detail);
          // 팝업닫기
          fnPopupClose();
          // 완료메시지
          fnKendoMessage({message : '페이지가 수정되었습니다.' });
          // 재조회
          //treeView.destroy();
          //fnInitTree();
        }
        else {
          //fnMessage('', '페이지 수정이 실패하였습니다.\n다시 시도해 주십시오.', '');
          fnMessage('', '['+result.resultCode+'] ' + result.resultMessage, '');
        }
        break;
      case 'page.delete':
        // --------------------------------------------------------------------
        // 페이지 삭제
        // --------------------------------------------------------------------
        if (result.resultCode == '0000') {
          // Tree에서 제거
          fnTreeDataDel(result.detail);
          // Tree선택 초기화, 입력값 초기화
          fnBtnClear();
          // 상세정보 노출 초기화
          fnInitDetl();
          // 페이지 데이터영역 초기화
          $('#trPageInit').show();
          $('#trPageSelected').hide();
          // 선택한 페이지ID 초기화
          selectedDpPageId = null;
          // 완료메시지
          fnMessage('', '페이지가 삭제되었습니다.', '');
        }
        else {
          fnMessage('', '['+result.resultCode+'] ' + result.resultMessage, '');
        }
        break;
      case 'inventory.insert':
        // --------------------------------------------------------------------
        // 인벤토리 등록
        // --------------------------------------------------------------------
        if (result.resultCode == '0000') {

          // 팝업닫기
          fnPopupClose();
          // * 인벤토리조회
          fnInitInventoryGrid();
          let data = $("#inputForm").formSerialize(true);
          inventoryGridDs.read(data);
          // 완료 메시지
          fnMessage('', '인벤토리가 등록되었습니다.', '');
        }
        else {
          //fnMessage('', '페이지 등록이 실패하였습니다.\n다시 시도해 주십시오.', '');
          fnMessage('', '['+result.resultCode+'] ' + result.resultMessage, '');
        }
          break;
      case 'inventory.update':
        // --------------------------------------------------------------------
        // 인벤토리 수정
        // --------------------------------------------------------------------
        if (result.resultCode == '0000') {

          fnMessage('', '인벤토리가 수정되었습니다.', '');

          // 팝업닫기
          fnPopupClose();
          // * 인벤토리조회
          // 페이지ID Set
          //dpPageId = selectedPageData.dpPageid;
          // 인벤토리 그리드 초기화(이 때 dpPageId 가 Set 됨)
          fnInitInventoryGrid();
          // 조회 실행
          let data = $("#inputForm").formSerialize(true);
          inventoryGridDs.read(data);
        }
        else {
          fnMessage('', '인벤토리 수정이 실패하였습니다.\n다시 시도해 주십시오.', '');
        }
        break;
      case 'inventory.delete':
        // --------------------------------------------------------------------
        // 인벤토리 삭제
        // --------------------------------------------------------------------
          if (result.resultCode == '0000') {

            fnMessage('', '인벤토리가 삭제되었습니다.', '');

            // * 인벤토리조회
            // 페이지ID Set
            //dpPageId = selectedPageData.dpPageid;
            // 인벤토리 그리드 초기화(이 때 dpPageId 가 Set 됨)
            fnInitInventoryGrid();
            // 조회 실행
            let data = $("#inputForm").formSerialize(true);
            inventoryGridDs.read(data);
          }
          else {
            fnMessage('', '인벤토리 삭제가 실패하였습니다.\n다시 시도해 주십시오.', '');
          }

        break;
      case 'inventory.sort':
        // --------------------------------------------------------------------
        // 인벤토리 정렬
        // --------------------------------------------------------------------
          //console.log('# fnBizCallback.inventory.sort :: ' + JSON.stringify(result));
          if (result.resultCode == '0000') {

            fnMessage('', '인벤토리 순번이 변경되었습니다.', '');

            // * 인벤토리조회
            // 페이지ID Set
            //dpPageId = selectedPageData.dpPageid;
            // 인벤토리 그리드 초기화(이 때 dpPageId 가 Set 됨)
            fnInitInventoryGrid();
            // 조회 실행
            let data = $("#inputForm").formSerialize(true);
            inventoryGridDs.read(data);
          }
          else {
            fnMessage('', '인벤토리 순서변경이 실패하였습니다.\n다시 시도해 주십시오.', '');
          }
        break;
    }

    // mode 초기화
    $('#mode').val("");
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
      console.log('# selected is Null');
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
      {
        field : "sort", dir : "asc", compare : compareTreeSort
      },
      {
        field : "pageCd", dir : "asc"
      }
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
            folderImage.css('display','none');
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
  // # validation 설정
  // ==========================================================================
  function fnSetValidation() {
    fnInputValidationForNumber("pageSort");
    fnInputValidationForNumber("inventorySort");
    fnInputValidationForAlphabetHangulNumberSpace("pagePageNm");
    fnInputValidationForAlphabetHangulNumberSpace("inventoryNm");
    fnInputValidationForAlphabetNumberHyphen("pagePageCd");
    fnInputValidationForAlphabetNumberHyphen("inventoryCd");
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
            inventoryGrid.editCell(container);
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

  function inventoryCdValidate(id) {
    var value = $("#" + id).val().trim();
    var regEx = /[^0-9a-zA-Z\-]/g;

    return value.search(regEx) < 0;
  }

  // ------------------------------- Html 버튼 바인딩  Start -----------------------
  /** Common New Top*/
  $scope.fnBtnClear             = function( ){  fnBtnClear();};
  /** Common Page Save */
  $scope.fnBtnPageSave          = function( ){  fnBtnPageSave();};
  /** Common New Top*/
  $scope.fnBtnPageNewTop        = function( ){  fnBtnPageNewTop();};
  /** Common New Under*/
  $scope.fnBtnPageNew           = function( ){  fnBtnPageNew();};
  /** Common Page Edit Popup Open */
  $scope.fnBtnPageEdit          = function( ){  fnBtnPageEdit();};
  /** Common Page Del */
  $scope.fnBtnPageDel           = function( ){  fnBtnPageDel();};
  /** Common Inventory Save */
  $scope.fnBtnInventorySave     = function( ){  fnBtnInventorySave();};
  /** Common Save Sort*/
  $scope.fnBtnInventorySortSave = function( ){  fnBtnInventorySortSave();};
  /** Common Inventory New */
  $scope.fnBtnInventoryNew      = function( ){  fnBtnInventoryNew();};
  /** Common Inventory Edit */
  $scope.fnBtnInventoryEdit     = function( ){  fnBtnInventoryEdit();};
  /** Common Inventory Del */
  $scope.fnBtnInventoryDel      = function( ){  fnBtnInventoryDel();};
  /** Common Popup Close */
  $scope.fnPopupClose           = function( ){  fnPopupClose();};


  /** popup button **/
  //$scope.fnPopupButton = function( param ){ fnPopupButton(param); };
  // ------------------------------- Html 버튼 바인딩  End -------------------------

}); // document ready - END
