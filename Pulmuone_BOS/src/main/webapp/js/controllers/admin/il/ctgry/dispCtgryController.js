﻿/**-----------------------------------------------------------------------------
 * description      : 시스템관리 - 공통코드
 * @
 * @ 수정일      수정자          수정내용
 * @ ------------------------------------------------------
 * @ 2017.02.03    홍진영          최초생성
 * @
 * **/
'use strict';

var treeView, parentNode, dataSource;

var selectedDepth = 0;
var initDepth = 0;
var selectedUid;
var selectedCategoryId;
var selectedData;                           /* HGRM-2382 - dgyoun : selectedData 초기화관련 */
var mallDivision   = 'MALL_DIV.PULMUONE';   // 기본값:풀무원
var useAllYn  = 'N';
var upDepthNm;

function compareSort(a, b) {
  return Number(a.sort) - Number(b.sort);
}

$(document).ready(function() {
  //Initialize Page Call
  fnInitialize();

  // ==========================================================================
  // # Initialize PageR
  // ==========================================================================
  function fnInitialize() {
    $scope.$emit('fnIsMenu', { flag : 'true' });

    fnPageInfo({
      PG_ID  : 'dispCtgry',
      callback : fnUI
    });
  }

  // ==========================================================================
  // # fnUI
  // ==========================================================================
  function fnUI() {

       fnTranslate();     // comm.lang.js 안에 있는 공통함수 다국어 변환-------

       fnInitButton();    // Initialize Button  -------------------------------

       fnInitOptionBox(); // Initialize radio ---------------------------------

       fnInitTree();      // Initialize Tree ----------------------------------

       fnInitInput();     // Data 초기화 --------------------------------------
  }

  // ==========================================================================
  // # 버튼 초기화
  // ==========================================================================
  function fnInitButton() {
    $('#fnClear, #fnNewGroup, #fnNew, #fnSave, #fnExcelDown').kendoButton();
    $('#fnDel').kendoButton({ enable: false });
  }

  // ==========================================================================
  // # 입력랎 초기화
  // ==========================================================================
  function fnInitInput() {

      $('#inputForm').formClear(true);
      // * 상위카테고리ID
      $('#inputForm input[name=parentsCategoryId]').val(0);
      // * depth
      $('* #inputForm input[name=depth]').val(0);
      // * ?
      $('#inputForm input[name=gbDicMstId]').val(null);
      // * ?
      $('#inputForm input[name=gbDicMstNm]').val(null);
      // * 몰구분
      $('#inputForm input[name=comboMallDivision]').val(mallDivision);
      $('#mallDivision').val(mallDivision);
      // * 미사용포함 체크/언체크 설정
      if (useAllYn == 'Y') {
          $('INPUT[name=checkUseAllYn]').prop("checked", true);
      }
      else {
          $('INPUT[name=checkUseAllYn]').prop("checked", false);
      }
      $('#useAllYn').val(useAllYn);
  }

  // ---------------Initialize Option Box Start -------------------------------
  // ==========================================================================
  // # fnInitOptionBox
  // ==========================================================================
  function fnInitOptionBox(){

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
    // 기본값사용여부
    // ------------------------------------------------------------------------
    fnTagMkRadio({
      id    :  "defaultUseYn",
      data  : [ { "CODE" : "Y" , "NAME":fnGetLangData({key :"com.msg.yes",nullMsg :'예' })}
              , { "CODE" : "N" , "NAME":fnGetLangData({key :"com.msg.no",nullMsg :'아니오' })}],
      tagId : "defaultUseYn",
      chkVal: 'Y',
      style : {}
    });
    // ------------------------------------------------------------------------
    // 사용여부
    // ------------------------------------------------------------------------
    fnTagMkRadio({
        id    :  "useYn",
        data  : [ { "CODE" : "Y" , "NAME":fnGetLangData({key :"com.msg.yes",nullMsg :'예' })}
                , { "CODE" : "N" , "NAME":fnGetLangData({key :"com.msg.no",nullMsg :'아니오' })}],
        tagId : "useYn",
        chkVal: 'Y',
        style : {}
    });
    // ------------------------------------------------------------------------
    // 전시여부
    // ------------------------------------------------------------------------
    fnTagMkRadio({
        id    :  "displayYn",
        data  : [ { "CODE" : "Y" , "NAME":fnGetLangData({key :"com.msg.yes",nullMsg :'예' })}
                , { "CODE" : "N" , "NAME":fnGetLangData({key :"com.msg.no",nullMsg :'아니오' })}],
        tagId : "displayYn",
        chkVal: 'Y',
        style : {}
    });
    // ------------------------------------------------------------------------
    // 성인여부
    // ------------------------------------------------------------------------
    fnTagMkRadio({
        id    :  "adultYn",
        data  : [ { "CODE" : "Y" , "NAME":fnGetLangData({key :"com.msg.yes",nullMsg :'예' })}
                , { "CODE" : "N" , "NAME":fnGetLangData({key :"com.msg.no",nullMsg :'아니오' })}],
        tagId : "adultYn",
        chkVal: 'N',
        style : {}
    });
    // ------------------------------------------------------------------------
    // 후기작성여부
    // ------------------------------------------------------------------------
    fnTagMkRadio({
        id    :  "feedbackYn",
        data  : [ { "CODE" : "Y" , "NAME":fnGetLangData({key :"com.msg.yes",nullMsg :'예' })}
                , { "CODE" : "N" , "NAME":fnGetLangData({key :"com.msg.no",nullMsg :'아니오' })}],
        tagId : "feedbackYn",
        chkVal: 'Y',
        style : {}
    });
    // ------------------------------------------------------------------------
    // 연결방식
    // ------------------------------------------------------------------------
    fnTagMkRadio({
        id    :  "linkYn",
        data  : [ { "CODE" : "N" , "NAME":"카테고리"}
                , { "CODE" : "Y" , "NAME":"URL"}],
        tagId : "linkYn",
        chkVal: 'N',
        style : {}
    });
    //  URL 숨김
    $('#divUrl').hide();

    // ------------------------------------------------------------------------
    // 이벤트 등록
    // ------------------------------------------------------------------------
    // 연결방식 이벤트 등록
    $("input:radio[name=linkYn]").click(function() {


      var inDepth = 0;
      if (selectedData == null || selectedData == 'null' || selectedData == undefined || selectedData == 'undefined' || selectedData == '') {
        //console.log('# selectedData is Null');
        inDepth = Number(selectedDepth) + 1;
      }
      else {
        //console.log('# selectedData Not Null :: ', JSON.stringify(selectedData));
        inDepth = Number(selectedDepth);
      }
      fnClicklinkYnRadio(inDepth);
    });
    // 몰구분변경
    $("#comboMallDivision").change(function() {

        mallDivision = $("#comboMallDivision").val();
        //console.log("# mallDivision click :: " + mallDivision);
        treeView.destroy();
        fnInitTree();
    });
    // 미사용포함여부
    $("input:checkbox[name=checkUseAllYn]").click(function() {
        //console.log('# 클릭이벤트...');

        // 트리 재조회 실행
        if ($("input:checkbox[name=checkUseAllYn]").is(":checked") == true) {
            useAllYn = 'Y';
        }
        else {
            useAllYn = 'N';
        }
        $('#useAllYn').val(useAllYn);

        treeView.destroy();
        fnInitTree();

    });

    //// 탭선택(몰구분) 이벤트 처리
    //$("#pdTabTitle td").click(function() {
    //    var $this = $(this);
    //    fnClickMallDiv($this);
    //    //$("#pdTabTitle td").css({"background":"#fff", "color":"#666"});
    //    //$this.css({"background":"#258ffc", "color":"#fff"});
    //    ////console.log('# $this :: ' + $this.attr("mall-div"));
    //    //// 몰구분  Set
    //    //mallDiv = $this.attr("mall-div");
    //})

    //fnTagMkRadioYN({id: "intputActive" , tagId : "USE_YN",chkVal: 'Y'});
  }
  // ---------------Initialize Option Box End ---------------------------------

  // ==========================================================================
  // # 연결방식 선택 이벤트 처리
  // ==========================================================================
  function fnClicklinkYnRadio(inDepth) {
    // console.log('# inDepth :: ', inDepth);

    if (inDepth == 0) {
      $('#trLinkYn').hide();
    }
    else if (inDepth == 1) {
      $('#trLinkYn').hide();
    }
    else {
      $('#trLinkYn').show();
    }

    var linkYnVal = $(':radio[name="linkYn"]:checked').val();
    //console.log('# linkYnVal :: ', linkYnVal);
    if (linkYnVal == 'N') {
      // 카테고리 선택
      $('#divUrl').hide();
      $("#linkUrl").prop("required", false);   // URL입력창 필수해제
      $("label[for='linkYn']").removeClass("req-star-front");
    }
    else if (linkYnVal == 'Y') {
      // URL 선택
      $('#divUrl').show();
      $("#linkUrl").prop("required", true);   // URL입력창 필수설정
      $("label[for='linkYn']").addClass("req-star-front");
    }

  }

  // ==========================================================================
  // # 탭선택 이벤트 처리
  // ==========================================================================
  //function fnClickMallDiv(obj) {
  //
  //    $("#pdTabTitle td").css({"background":"#fff", "color":"#666"});
  //    obj.css({"background":"#258ffc", "color":"#fff"});
  //    //console.log('# obj :: ' + obj.attr("mall-div"));
  //    // 몰구분  Set
  //    mallDiv = obj.attr("mall-div");
  //    fnInitTree();
  //}

  // ==========================================================================
  // # 상세정보 노출/비노출 처리
  // ==========================================================================
  function fnDetlShowHide(inDepth) {
      //console.log('# fnDetlShowHide Start');
      // alert('# 노출숨김 : selectedDepth :: ' + selectedDepth);
      if (inDepth == 0) {
        // 그룹
          $('#trCategory').show();
          $('#trSort').show();
          $('#trDefaultUseYn').hide();
          $('#trUseYn').show();
          $('#trDisplayYn').hide();
          $('#trAdultYn').hide();
          $('#trFeedbackYn').hide();
      }
      else if (inDepth == 1) {
        // 대분류
          $('#trCategory').show();
          $('#trSort').show();
          $('#trDefaultUseYn').show();
          $('#trUseYn').show();
          $('#trDisplayYn').show();
          $('#trAdultYn').show();
          $('#trFeedbackYn').show();
      }
      else {
        // 중부류/소분류/세분류
          $('#trCategory').show();
          $('#trSort').show();
          $('#trDefaultUseYn').hide();
          $('#trUseYn').show();
          $('#trDisplayYn').show();
          $('#trAdultYn').show();
          $('#trFeedbackYn').show();
      }

      // 연결방식 선택 이벤트
      fnClicklinkYnRadio(inDepth);

      //console.log('# selectedDepth :: ' + selectedDepth);
      // 분류단계명 노출
      //console.log('# fnDetlShowHideyinDepth :: ' + inDepth);
      //fnDepthNameSet(inDepth, '');
      fnDepthNameSet(inDepth, upDepthNm);

  }

  // ==========================================================================
  // # 분류단계 Set
  // ==========================================================================
  function fnDepthNameSet(depth, addStr) {
    //console.log('# fnDepthNameSet Start ['+depth+']['+addStr+']');

    //if (addStr == null || addStr == undefined || addStr == 'undefined') {
    //  addStr ='';
    //}
    var depthNm;

    if (depth == 0) {
        depthNm = '그룹';
    }
    else if (depth == 1) {
        //depthNm = addStr + '대분류';
        depthNm = '대분류';
    }
    else if (depth == 2) {
        //depthNm = addStr + '중분류';
        depthNm = '대분류 > 중분류';
    }
    else if (depth == 3) {
        //depthNm = addStr + '소분류';
        depthNm = '대분류 > 중분류 > 소분류';
    }
    else if (depth == 4) {
        //depthNm = addStr + '세분류';
        depthNm = '대분류 > 중분류 > 소분류 > 세분류';
    }
    else {
      depthNm = '';
    }
    $("#depthNm").text(depthNm);
    //console.log('# fnDepthNameSet End');
  }

  // ==========================================================================
  // # fnClear
  // ==========================================================================
  function fnClear() {
    // 트리 선택해제
    //treeView.select($());
    // 초기화
    //fnInitInput();

    /* HGRM-2382 - dgyoun : selectedData 초기화관련 */
    if (selectedData != null && selectedData != undefined && selectedData != 'undefined') {
      // ----------------------------------------------------------------------
      // 조회된 상태
      // ----------------------------------------------------------------------
      // 선택된 데이터가 있는 경우 조회한 값으로 Set (지우지 않음)
      $('#inputForm').bindingForm( {'rows':selectedData}, 'rows' );
    }
    else {
      // ----------------------------------------------------------------------
      // 그룹생성 또는 하위분류생성  신규 상태
      // ----------------------------------------------------------------------
      // 노출숨김처리
      fnDetlShowHide(Number(selectedDepth)+1);    /* HGRM-2382 - dgyoun : selectedData 초기화관련 */
      // * 값 초기화
      // 카테고리명
      $('#categoryName').val('');
      // 카테고리 순서
      $('#sort').val('');
      // 기본값사용여부
      $('input:radio[name="defaultUseYn"]:input[value="Y"]').prop("checked", true);
      // 사용여부
      $('input:radio[name="useYn"]:input[value="Y"]').prop("checked", true);
      // 전시여부
      $('input:radio[name="displayYn"]:input[value="Y"]').prop("checked", true);
      // 성인여부 기본값 N Set
      $('input:radio[name="adultYn"]:input[value="N"]').prop("checked", true);
      // 후기작성여부
      $('input:radio[name="feedbackYn"]:input[value="Y"]').prop("checked", true);
      // 연결방식
      $('input:radio[name="y"]:input[value="N"]').prop("checked", true);
      $('#linkUrl').val('');
    }
    // 상세 항목 노출 설정
    //alert('# initDepth :: ' + initDepth);
    //fnDetlShowHide(initDepth);
  }


  // ==========================================================================
  // # 그룹생성
  // ==========================================================================
  function fnNewGroup() {

      // 트리 선택해제
      treeView.select($());
      // 분류상세정보  그룹 설정
      fnDetlShowHide(0);
      // 입력값 초기화
      fnInitInput();
      // 그룹 설정
      $('#inputForm input[name=parentsCategoryId]').val(0);
      $('#inputForm input[name=depth]').val(0);
      // 포커스 설정
      inputFocus();
      // 분류단계
      $("#depthNm").text('그룹');

      /* HGRM-2382 - dgyoun : selectedData 초기화관련 */
      selectedDepth = 0;
      selectedData = null;
      /* HGRM-2391 - dgyoun : 초기화 처리 */
      initDepth = 0;

      // ----------------------------------------------------------------------
      // 그룹 추가 시 사용여부 Y 불가 처리
      // ----------------------------------------------------------------------
      // 사용여부 : 미사용 선택
      $('input:radio[name="useYn"]:radio[value="N"]').prop('checked',true);
      // 사용여부 : 비활성
      $('input[name=useYn]').attr('disabled', true);                      // radio
  }

  // ==========================================================================
  // # 하위분류생성
  // ==========================================================================
  function fnNew() {

    if (selectedDepth >= 4) {
      fnKendoMessage({message : '세분류 카테고리 하위분류를 생성하실 수 없습니다.' });
      return false;
    }

    var data = getTreeSelectData();
    //var depth;

    if (data == null || data == undefined || data == 'undefined') {
      // 그룹 생성은 fnNewGroup 에서 하므로 fnNew에서는 모든 호출에 대해 다 상위 카테고리 체크함
      //console.log('# selectedDepth :: ' + selectedDepth);
      fnKendoMessage({message : '상위 카테고리를 선택해 주세요.' });
      return false;
    }

    if( data != undefined ){

      // ----------------------------------------------------------------------
      // 선택한 분류의 연결방식이  URL인 경우 하위 분류 생성 불가 체크
      // ----------------------------------------------------------------------
      var linkYn = data.linkYn;
      //console.log('# linkYn['+selectedDepth+'] :: ' + linkYn);

      if (selectedDepth >= 2) {
        if (linkYn != null || linkYn != undefined || linkYn != 'undefined') {
          if (linkYn == 'Y') {
            fnKendoMessage({message : '해당 분류는 하위분류를 생성할 수 없습니다.' });
            return false;
          }
        }
      }

      /* HGRM-2390 - dgyoun : validation 체크 후 초기화 처리 */
      // 입력값 초기화
      fnInitInput();

      //alert(111);
      // 성인여부 기본값 N Set
      $('input:radio[name="adultYn"]:input[value="N"]').prop("checked", true);

      // 상세 항목 노출 설정
      fnDetlShowHide(Number(data.depth) + 1);
      // 생성대상의 상위 카테고리
      $('#inputForm input[name=parentsCategoryId]').val(data.categoryId);
      // 생성대상의 depth
      //depth = Number(data.depth);
      $('#inputForm input[name=depth]').val( Number(data.depth)+1 );

      // 분류단계명 Set
      fnDepthNameSet(Number(data.depth)+1, data.categoryName + ' > ');

      // 상위명(초기화에 사용)
      upDepthNm = data.categoryName + ' > ';

      // ----------------------------------------------------------------------
      // 하위분류생성 선택 시 사용여부 초기화(활성화/Y)
      // ----------------------------------------------------------------------
      // 사용여부 : 사용 선택
      $('input:radio[name="useYn"]:radio[value="Y"]').prop('checked',true);
      // 사용여부 : 활성
      $('input[name=useYn]').attr('disabled', false);                      // radio
    }
    inputFocus();

    /* HGRM-2382 - dgyoun : selectedData 초기화관련 */
    selectedData = null;

    /* HGRM-2391 - dgyoun : 초기화 처리 */
    initDepth = Number(selectedDepth)+1;

  }

  // ==========================================================================
  // # fnSave
  // ==========================================================================
  function fnSave() {
      //console.log('# fnSave Start');
    fnSaveProcess();
    //if($('input[name=useYn]:checked').val() == 'N'){
    //  fnKendoMessage( {
    //      message : fnGetLangData({key :'', nullMsg : '<div>해당카테고리로 선택된 상품이 검색되지 않을수 있습니다, 진행하시겠습니까?</div>' })
    //    , type    : 'confirm'
    //    , ok      : function(){
    //                  fnSaveProcess();
    //                }
    //  });
    //}else{
    //  fnSaveProcess();
    //}
  }

  function fnSaveProcess() {
    // ----------------------------------------------------------------------
    // 등록/수정 호출 정보
    // ----------------------------------------------------------------------
    var url  = '/admin/goods/category/addCategory';
    var cbId = 'insert';
    var isAction = 'insert';

    if( OPER_TP_CODE == 'U' ){
        url  = '/admin/goods/category/putCategory';
        cbId= 'update';
        isAction = 'update';
    }
    //console.log('# selectedDepth :: ' + selectedDepth);

    // ----------------------------------------------------------------------
    // 변경전 사용상태 Set
    // ----------------------------------------------------------------------
    //console.log('# selectedData :: ', JSON.stringify(selectedData));
    if (selectedData != null) {
      $('#befUseYn').val(selectedData.useYn);
    }


    // ----------------------------------------------------------------------
    // 화면 동기화
    // ----------------------------------------------------------------------
    var data = $('#inputForm').formSerialize(true);
    //console.log('# data :: ' + JSON.stringify(data));
    //console.log('# fnSave 1111');

    // ----------------------------------------------------------------------
    // Vlidation  Check
    // ----------------------------------------------------------------------
    // 순서
    var sortVal = $('#sort').val();
    if (sortVal != null && sortVal != '' && sortVal != undefined && sortVal != 'undefined') {
      if (isNaN(sortVal) == true) {
        fnMessage("", "순서는 숫자만 입력 가능합니다.", 'sort');
        return false;
      }
    }

    // ----------------------------------------------------------------------
    // 공통Validation Check + 실행
    // ----------------------------------------------------------------------
    if(data.rtnValid){

      let confirmMsg = '';
      let useYn = $('input[name=useYn]:checked').val();

      if (cbId == 'insert') {
        // ------------------------------------------------------------------
        // 신규
        // ------------------------------------------------------------------
        confirmMsg = '저장하시겠습니까?'
      }
      else {
        // ------------------------------------------------------------------
        // 수정
        // ------------------------------------------------------------------
        if (selectedData.depth == '0') {
          // ----------------------------------------------------------------
          // 그룹인 경우
          // ----------------------------------------------------------------
          if (selectedData.useYn == 'Y' && useYn == 'N') {
            // 사용여부 Y -> N 으로 변경인 경우 : 변경불가
            fnMessage('', '<div>사용중인 그룹은 사용안함으로 변경하실 수 없습니다.</div><div>다른 그룹의 사용여부를 사용으로 변경해주세요.</div>', '');
            return false;
          }
          if (selectedData.useYn == 'N' && useYn == 'Y') {
            // 사용여부 N -> Y 으로 변경인 경우 : 경고메시지
            confirmMsg = '<div>현재 사용중인 그룹이 사용안함으로 자동변경 됩니다.</div> <div>진행하시겠습니까?</div>';
          }
          else {
            confirmMsg = '<div>수정하시겠습니까?</div>';
          }
        }
        else {
          // ----------------------------------------------------------------
          // 그룹이 아닌 경우
          // ----------------------------------------------------------------
          if (selectedData.useYn == 'Y' && useYn == 'N') {
            // 사용여부 Y -> N 으로 변경인 경우
            confirmMsg = '<div>해당카테고리로 선택된 상품이 검색되지 않을수 있습니다.</div><div>진행하시겠습니까?</div>';
          }
          else {
            confirmMsg = '<div>수정하시겠습니까?</div>';
          }
        }
      }

      fnKendoMessage( {
          type    : 'confirm'
        , message : confirmMsg
        , ok      : function(){
                      fnAjax({
                          url       : url
                        , params    : data
                        , success   : function(result){
                                        //console.log('# fnSave success... :: ' + JSON.stringify(result));
                                        fnBizCallback(cbId, result);
                                      }
                        , isAction  : isAction
                      });
                    }
      });
    }
  }

  // ==========================================================================
  // # fnDel
  // ==========================================================================
  function fnDel() {
      fnKendoMessage({message:fnGetLangData({key :"102938",nullMsg :'카테고리를 삭제하면 데이터 복구가 불가능 합니다. 삭제 하시겠습니까?' }), type : "confirm"
      ,ok : function(){
              var url  = '/admin/goods/category/delCategory';
                var cbId = 'delete';
                var data = $('#inputForm').formSerialize(true);
                if( data.rtnValid ){
                    fnAjax({
                        url     : url,
                        params  : data,
                        success :
                            function(result){
                                fnBizCallback(cbId, result);
                            },
                        isAction : 'update'
                    });
                }
          }
       });
  }

  // ==========================================================================
  // # fnExcelDown
  // ==========================================================================
  function fnExcelDown() {

      //console.log('# PG_SESSION :: ' + JSON.stringify(PG_SESSION));
      // 세션 체크
      if (PG_SESSION == null || PG_SESSION == '' || PG_SESSION == undefined || PG_SESSION == 'undefined') {
        //console.log('# PG_SESSION :: ' + JSON.stringify(PG_SESSION));
        //location.href = "/layout.html#/goodsMgm?ilGoodsId="+data.ilGoodsId;
        fnKendoMessage({ message : "세션이 종료되었습니다. \n다시 로그인 해 주십시오.", ok : function (){
          location.href = "/admVerify.html";
        }});
        return false;
      }
      // 몰구분 Set
      $('#mallDivision').val(mallDivision);
      // 필수체크 해제
      $("#categoryName").prop("required", false);
      $("#sort").prop("required", false);
      // 엑셀다운로드실행
      var data = $('#inputForm').formSerialize(true);
      fnExcelDownload('/admin/goods/category/getExportExcelCategoryList', data);
      // 필수체크 원복
      $("#categoryName").prop("required", true);
      $("#sort").prop("required", true);
  }

  // ==========================================================================
  // # inputFocus
  // ==========================================================================
  function inputFocus() {
    $('#categoryName').focus();
  }

  // ==========================================================================
  // # fnInitTree
  // ==========================================================================
  function fnInitTree() {
      //console.log('# fnInitTree Start');

      dataSource = fnKendoTreeDS({
              url      : '/admin/goods/category/getCategoryList?mallDivision=' + mallDivision + "&useAllYn=" + useAllYn
            , model_id : 'categoryId'
      });

      fnKendoTreeView({
            id            : 'Tree'
          , dataSource    : dataSource
          , dataTextField : 'categoryName'
          , template      : kendo.template($("#treeview-template").html())
          , autoBind      : false
          , dragAndDrop   : false
          , autoScroll    : true
          , change        : function(e){
                              //console.log('# change Event');
                              //console.log('# mallDivision :: ' + mallDivision);
                              var data = getTreeSelectData();
                              //console.log('# data :: ' + JSON.stringify(data));
                              $('#inputForm').bindingForm( {'rows':data}, 'rows' );

                              if (data != null) {

                                /* HGRM-2382 - dgyoun : selectedData 초기화관련 */
                                selectedData = data;

                                if (data.depth != null) {
                                  selectedDepth = Number(data.depth);
                                  /* HGRM-2382 - dgyoun : selectedData 초기화관련 버그 수정 */
                                  initDepth     = selectedDepth;
                                }
                              }
                              // 상세정보 노출/비노출 처리
                              fnDetlShowHide(initDepth);
                              // 연결방식 노출 설정
                              fnClicklinkYnRadio(selectedDepth);

                              // ----------------------------------------------
                              // 하위분류생성 선택 시 사용여부 활성화
                              // ----------------------------------------------
                              // 사용여부 : 활성
                              $('input[name=useYn]').attr('disabled', false);                      // radio

                            }
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
          //                        sortData[i] = childrenData[i].categoryId;
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
      // 초기 트리 조회
      //console.log('# 초기 트리 조회');
      dataSource.read({'categoryId':0});

      // 분류상세정보 그룹 정보 노출
      fnDetlShowHide(0);
  }

  // ==========================================================================
  // # putSort
  // ==========================================================================
  function putSort(data) {
        var url  = '/admin/goods/category/putCategorySort';
        var cbId = 'sortUpdate';

        if( data.length > 1 ){

            var dataParams = {"updateData":kendo.stringify(data)};

            fnAjax({
                url     : url,
                params  : dataParams,
                success :
                    function(result){
                        fnBizCallback(cbId, result);
                    },
                isAction : 'update'
            });
        }
  }

  // -------------------------------  POPUP START  ----------------------------
  // ==========================================================================
  // # fnPopupButton
  // ==========================================================================
  function fnPopupButton(param) {
    fnKendoPopup({
          id     : 'SAMPLE_POP',
          title  : fnGetLangData({key :"4620",nullMsg :'표준용어 팝업' }),
          src    : '#/dicMst001',
          success: function( id, data ){
              if(data.GB_DIC_MST_ID){
                $('#GB_DIC_MST_ID').val(data.GB_DIC_MST_ID);
                $('#GB_DIC_MST_NM').val(data.BASE_NAME);
              }
            }
      });
  };
  // -------------------------------  POPUP End  ------------------------------



  // ==========================================================================
  // # 콜백합수
  // ==========================================================================
  function fnBizCallback(id, result) {
    switch(id){
      case 'insert':
          //console.log('# fnBizCallback.insert');
          //console.log('# detail :: ' + JSON.stringify(result.detail));
          addTreeData(result.detail);

          if (result.detail.depth == 0) {
            fnKendoMessage({message : "입력되었습니다." ,ok :fnNewGroup});
          }
          else {
            fnKendoMessage({message : "입력되었습니다." ,ok :fnNew});
          }

          //재조회
          //treeView.destroy();
          //fnInitTree();
          break;
      case 'update':
          //console.log('# fnBizCallback.update');
          putTreeData(result.detail);

          if (result.detail.linkYn == 'N') {
              $('#inputForm input[name=linkUrl]').val(null);
          }
          fnKendoMessage({message : '수정되었습니다.' });
          // 재조회
          //treeView.destroy();
          //fnInitTree();
          break;
      case 'sortUpdate':
          //console.log('# fnBizCallback.sortUpdate');
          break;
      case 'delete':
          //console.log('# fnBizCallback.delete');
          delTreeData(result.detail);
          fnKendoMessage({message : fnGetLangData({key :"366",nullMsg :'삭제되었습니다.' }) });
          break;
    }
  }

  // ==========================================================================
  // # 트리 항목 선택
  // ==========================================================================
  function getTreeSelectData() {
      //console.log('# Tree 선택');
      var uid = treeView.select().data('uid');
      selectedUid = uid;
      var selectedRowData = dataSource.getByUid(uid);   // 선택한 로우 데이터

      if (selectedRowData != null) {
          selectedCategoryId = selectedRowData.categoryId;
          selectedDepth      = Number(selectedRowData.depth);
      }
      else {
        //console.log('# selected is Null');
      }
      //console.log('# selected :: [uid:'+uid+'][categoryId:'+selectedCategoryId+'][depth:'+selectedDepth+']');
      //console.log('# uid data :: ' + JSON.stringify(dataSource.getByUid(uid)));
      return selectedRowData;
  }

  // ==========================================================================
  // # 트리 항목 추가
  // ==========================================================================
  function addTreeData(data){

    const sortOpt = [
      {
        field : "sort", dir : "asc", compare : compareSort
      },
      {
        field : "categoryName", dir : "asc"
      }
    ];

    data.hasChildren = false;
    var uid = treeView.select().data('uid');
    if( uid != undefined ){
      if (data.depth == '0') {
        // 그룹생성인 경우 최상단에 추가
        //treeView.append(data);
        treeView.append(data, null, function() {
          treeView.dataSource.sort(sortOpt);
        });
      }
      else {
        // 하위 분류 생성 시 빈 폴더 없애기
        const target = $(treeView.select());

        if(target.length !== 0){
          const isleaf = treeView.dataItem(treeView.select()).isleaf;

          if(isleaf === 'false'){
            const folderImage = target.find('.k-icon.k-no-expand');
            folderImage.css('display','none');
            treeView.dataItem(treeView.select()).isleaf = "true";
            treeView.append(data, treeView.select());
          } else {
            treeView.append(data, treeView.select(), function(){
              const $target = treeView.select();

              if($target.length <= 0) return;

              treeView.dataItem($target).children.sort(sortOpt);
            });
          }
        }
          // 하위분류생성이면 선택한 카테고리 하위에 생성
          // treeView.append(data, treeView.select());
      }
    }
    else{
      // 값이 없을 경우 최상단에 생성
      treeView.append(data, null, function() {
        treeView.dataSource.sort(sortOpt);
      });
    }
  }

  // ==========================================================================
  // # putTreeData - row는 단건
  // ==========================================================================
  function putTreeData(row) {
      dataSource.pushUpdate(row);

      const sortOpt = [
        {
          field : "sort", dir : "asc", compare : compareSort
        },
        {
          field : "categoryName", dir : "asc"
        }
      ];

      const $parent = treeView.parent(treeView.select()).length > 0 ? treeView.parent(treeView.select()) : null;

      if($parent) {
        treeView.dataItem($parent).children.sort(sortOpt);
      } else {
        treeView.dataSource.sort(sortOpt);
      }

  }

  // ==========================================================================
  // # delTreeData
  // ==========================================================================
  function delTreeData() {
      treeView.remove(treeView.select());
  }

  // ==========================================================================
  // # 오류메시지처리
  // ==========================================================================
  function fnMessage(key, nullMsg, ID){
    fnKendoMessage({ message : fnGetLangData({ key : key, nullMsg : nullMsg}), ok : function focusValue(){
      $('#'+ID).focus();
    }});

    return false;
  }

  // ------------------------------- Html 버튼 바인딩  Start -----------------------
  /** Common Clear*/
  $scope.fnClear = function( ){ fnClear();};
  /** Common NewGroup*/
  $scope.fnNewGroup = function( ){  fnNewGroup();};
  /** Common ExcelDown*/
  $scope.fnExcelDown = function( ){  fnExcelDown();};
  /** Common New*/
  $scope.fnNew = function( ){  fnNew();};
  /** Common Save*/
  $scope.fnSave = function(){   fnSave();};
  /** Common Delete*/
  $scope.fnDel = function(){   fnDel();};
  /** popup button **/
  $scope.fnPopupButton = function( param ){ fnPopupButton(param); };
  // ------------------------------- Html 버튼 바인딩  End -------------------------

}); // document ready - END
