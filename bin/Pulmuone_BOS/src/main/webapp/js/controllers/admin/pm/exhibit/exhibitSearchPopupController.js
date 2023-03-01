/*******************************************************************************
 * -----------------------------------------------------------------------------
 * description : 프로모션 기획전리스트 처리
 * @
 * @ 아래 기획전리스트 공통 처리
 * @  - 일반     기획전리스트 : exhibitNormalListController.js, exhibitNormalList.html
 * @  - 골라담기 기획전리스트 : exhibitSelectListController.js, exhibitSelectList.html
 * @  - 증정행사 기획전리스트 : exhibitGiftListController.js  , exhibitGiftList.html
 * @
 * @ 수정일        수정자        수정내용
 * @ ---------------------------------------------------------------------------
 * @ 2020.12.01    dgyoun        최초생성
 * @
 ******************************************************************************/
'use strict';


var LAST_PAGE = null;   // 최종 페이지 (페이징 기억 관련)
var PAGE_SIZE = 20;
var exhibitGridDs, exhibitGridOpt, exhibitGrid;
var publicStorageUrl = fnGetPublicStorageUrl();
var pageParam = parent.POP_PARAM["parameter"];

var gbPageParam = '';   // 넘어온 페이지파라미터
var gbMode      = '';
var delEvExhibitIdArr;  // 삭제 EvExhibitId 리스트
//var gbExhibitTp = '';   // 기획전유형  <- 기획전 유형별 js 페이지에서 선언 및 정의 됨
//var gbGiftGoodsSortYn = 'N';    // 증정행사 대표상품정렬여부

var gbBtnDetailSearchViewYn = false;  // 증정행사 상세검색 노출여부(기본값 접힘)
var gbSearchInfoObj = new Object();   // 조회조건 객체
const gbExhibitTp = "EXHIBIT_TP.NORMAL";

const pageId = "exhibitSearchPopup";

$(document).ready(function() {

  // ==========================================================================
  // # Initialize Page Call
  // ==========================================================================
  fnInitialize();

  // ==========================================================================
  // # Initialize PageR
  // ==========================================================================
  function fnInitialize() {

    // ------------------------------------------------------------------------
    // 화면기본설정
    // ------------------------------------------------------------------------
    $scope.$emit('fnIsMenu', { flag : false });

    fnPageInfo({
        PG_ID     : pageId // 'exhibitList'
      , callback  : fnUI
    });

    // ------------------------------------------------------------------------
    // 페이지파라미터
    // ------------------------------------------------------------------------
    gbPageParam = fnGetPageParam();
    //console.log('# gbPageParam :: ', JSON.stringify(gbPageParam));

    // ------------------------------------------------------------------------
    // 기획전유형 Set
    //   - exhibitNormalListController.js, exhibitSelectListController.js, exhibitGiftListController.js 에서 설정 됨
    // ------------------------------------------------------------------------
    //gbExhibitTp = gbPageParam.exhibitTp;
    //console.log('# gbExhibitTp :: ', gbExhibitTp);
    $('#exhibitTp').val(gbExhibitTp);

    // ------------------------------------------------------------------------
    // 상위타이틀
    // ------------------------------------------------------------------------
    if (gbExhibitTp != null && gbExhibitTp != 'null') {

      if (gbExhibitTp == 'EXHIBIT_TP.NORMAL') {
        //$('#pageTitleSpan').text('');
      }
      else if (gbExhibitTp == 'EXHIBIT_TP.SELECT') {
        $('#pageTitleSpan').text('골라담기(균일가) 목록');
      }
      else if (gbExhibitTp == 'EXHIBIT_TP.GIFT') {
        $('#pageTitleSpan').text('증정행사 기획전 목록');
      }
    }

    // ------------------------------------------------------------------------
    // 조회조건 정보 Set
    // ------------------------------------------------------------------------
    //console.log('# ================================================');
    //console.log('# [리스트] 조회조건 Set');
    //console.log('# ================================================');
    //console.log('# ------------------------------------------------');
//    let searchInfo    = sessionStorage.getItem('searchInfo');
//    if (searchInfo != null) {
//      gbSearchInfoObj = JSON.parse(searchInfo);
//    }
    //let isFromDetl = gbSearchInfoObj.isFromDetl;
    //console.log('# searchInfoObj :: ', JSON.stringify(gbSearchInfoObj));
    //console.log('# isFromDetl :: ', isFromDetl);

  }

  // ==========================================================================
  // # fnUI
  // ==========================================================================
  function fnUI() {

    fnInitButton();           // Initialize Button ----------------------------

    fnInitGrid();             // Initialize Grid ------------------------------

    fnInitOptionBox();        // Initialize Option Box ------------------------

    fnInitEvent();            // Initialize Event -----------------------------

    fnSetDefaultPre();           // 기본설정 ----------------------------------

    //조회조건 Set을 위한 딜레이 설정
    //setTimeout(function() {
      fnSearch();               // 조회 ---------------------------------------
    //}, 1000);   // 모두 처리 안될 경우 시간 늘릴 것
  }

  // ==========================================================================
  // # 초기화 - 버튼
  // ==========================================================================
  function fnInitButton() {
    $('#fnSearch, #fnConfirm, #fnClear, #fnClose, #fnExcelDownload, #fnShowImage').kendoButton();
  }

  // ==========================================================================
  // # 초기화 - 값
  // ==========================================================================
  function fnClear() {

    $('#searchForm').formClear(true);
    fnSetDefaultPre();
    // 기획전유형 Set
    $('#exhibitTp').val(gbExhibitTp);
  }

  // ==========================================================================
  // # 초기화 - 옵션박스
  // ==========================================================================
  function fnInitOptionBox() {

    // ------------------------------------------------------------------------
    // 팝업
    // ------------------------------------------------------------------------
    //$('#kendoPopup').kendoWindow({
    //    visible: false
    //  , modal: true
    //});

    // ------------------------------------------------------------------------
    // 검색구분(S) - 공통코드 아님
    // ------------------------------------------------------------------------
    fnKendoDropDownList({
        id      : 'searchSe'
      , tagId   : 'searchSe'
      , data    : [
                    {'CODE':'NAME','NAME':'기획전명'}
                  , {'CODE':'ID'  ,'NAME':'기획전ID'}
                  ]
      , chkVal  : 'NAME'
    });

    // ------------------------------------------------------------------------
    // 몰구분(C)
    // ------------------------------------------------------------------------
    fnTagMkChkBox({
        id          : 'mallDiv'
      , tagId       : 'mallDiv'
      , url         : '/admin/comn/getCodeList'
      , params      : {'stCommonCodeMasterCode' : 'MALL_DIV', 'useYn' : 'Y'}
      , async       : false
      , isDupUrl    : 'Y'
      , style       : {}
      , beforeData  : [{ 'CODE' : 'ALL', 'NAME' : '전체' }]
    });

    // ------------------------------------------------------------------------
    // 담당자구분
    // ------------------------------------------------------------------------
    fnKendoDropDownList({
        id        : 'managerId'
      , tagId     : 'managerId'
      , url       : '/admin/pm/exhibit/selectExhibitManagerList'
      , params    : { 'exhibitTp' : gbExhibitTp}
      , autoBind  : true
      , valueField: 'createId'
      , textField : 'userNmLoginId'
      , async     : true
      , isDupUrl  : 'Y'
      , chkVal    : ''
      , blank     : '전체'
    });
//    fnKendoDropDownList({
//        id          : 'manageSe'
//      , tagId       : 'manageSe'
//      , data        : [
//                        { 'CODE' : ''   , 'NAME' : '전체'        }
//                      , { 'CODE' : 'M'  , 'NAME' : '계정본인정보'}
//                      , { 'CODE' : 'T'  , 'NAME' : '작성자전체'  }
//                      ]
//      , valueField  : 'CODE'
//      , textField   : 'NAME'
//      , chkVal      : '0'
//      , style       : {}
//            /*blank : '선택',*/
//    });

    // ------------------------------------------------------------------------
    // 승인상태
    // ------------------------------------------------------------------------
    fnTagMkChkBox({
        id          : 'approvalStatus'
      , tagId       : 'approvalStatus'
      , url         : '/admin/comn/getCodeList'
      , params      : {'stCommonCodeMasterCode' : 'APPR_STAT', 'useYn' : 'Y'}
      , async       : true
      , isDupUrl    : 'Y'
      , style       : {}
      , beforeData  : [{ 'CODE' : 'ALL', 'NAME' : '전체' }]
      , success     : function (response) {
                        // ----------------------------------------------------
                        // 항목제거
                        // ----------------------------------------------------
                        //if( response && response.data && response.data.rows ) {
                        let $checkbox = $('#approvalStatus').find('input[value="APPR_STAT.APPROVED_BY_SYSTEM"]');
                        let $label = $checkbox.closest('label');
                        $label.remove();
                        //$label.hide();
                        $checkbox = $('#approvalStatus').find('input[value="APPR_STAT.DISPOSAL"]');
                        $label = $checkbox.closest('label');
                        $label.remove();
                        //$label.hide();
                        //}
                        //승인상태 전체 체크
                        $('input[name=approvalStatus]').eq(0).prop('checked', true).trigger('change');

                        // ----------------------------------------------------
                        // 조회조건 Set : 시간차로 인해 fnSetSearchInfo 에서 처리가 안되는 경우
                        // ----------------------------------------------------
                        if (gbExhibitTp == 'EXHIBIT_TP.SELECT' || gbExhibitTp == 'EXHIBIT_TP.GIFT') {
                          // --------------------------------------------------
                          // 승인상태 : 체크
                          // --------------------------------------------------
                          if (fnIsEmpty(gbSearchInfoObj.approvalStatus) == false) {
                            let approvalStatusArr = (gbSearchInfoObj.approvalStatus).split('∀');
                            if (fnIsEmpty(approvalStatusArr) == false && approvalStatusArr.length > 0) {
                              $('input[name=approvalStatus]:checkbox').prop('checked', false);
                              for (var i = 0; i < approvalStatusArr.length; i++) {
                                $('input:checkbox[name="approvalStatus"]:input[value="'+approvalStatusArr[i]+'"]').prop('checked', true);
                              }
                            }
                          }
                        }
                      }
    });
    //fnTagMkChkBox({
    //    id          : 'exhibitStatus'
    //  , tagId       : 'exhibitStatus'
    //  , url         : '/admin/comn/getCodeList'
    //  , params      : {'stCommonCodeMasterCode' : 'EXHIBIT_STATUS', 'useYn' : 'Y'}
    //  , async       : true
    //  , isDupUrl    : 'Y'
    //  , style       : {}
    //  , beforeData  : [{ 'CODE' : 'ALL', 'NAME' : '전체' }]
    //});
    // ------------------------------------------------------------------------
    // 사용여부(R)  - 공통코드 아님
    // ------------------------------------------------------------------------
    fnTagMkRadio({
        id      : 'dispYn'
      , tagId   : 'dispYn'
      , data    : [
                    { 'CODE' : ''   , 'NAME' : '전체'   }
                  , { 'CODE' : 'Y'  , 'NAME' : '예'     }
                  , { 'CODE' : 'N'  , 'NAME' : '아니오' }
                  ]
      , chkVal  : ''
      , style   : {}
    });

    // ------------------------------------------------------------------------
    // 진행상태구분(C) - 공통코드 아님
    // ------------------------------------------------------------------------
    fnTagMkChkBox({
        id        : 'statusYnSe'
      , tagId     : 'statusYnSe'
      , data      : [
                      { 'CODE' : 'ALL'    , 'NAME' : '전체'    }
                    , { 'CODE' : 'BEF'   , 'NAME' : '진행예정'}
                    , { 'CODE' : 'ING'    , 'NAME' : '진행중'  }
                    , { 'CODE' : 'END'    , 'NAME' : '진행종료'}
                    ]
      , chkVal    : ''
      , style     : {}
    });

    // ------------------------------------------------------------------------
    // 사용여부(R)  - 공통코드 아님
    // ------------------------------------------------------------------------
    fnTagMkRadio({
        id      : 'useYn'
      , tagId   : 'useYn'
      , data    : [
                    { 'CODE' : ''   , 'NAME' : '전체'   }
                  , { 'CODE' : 'Y'  , 'NAME' : '예'     }
                  , { 'CODE' : 'N'  , 'NAME' : '아니오' }
                  ]
      , chkVal  : ''
      , style   : {}
    });

    // ------------------------------------------------------------------------
    // 노출범위(디바이스)(C)
    // ------------------------------------------------------------------------
    fnTagMkChkBox({
        id          : 'goodsDisplayType'
      , tagId       : 'goodsDisplayType'
      , url         : '/admin/comn/getCodeList'
      , params      : {'stCommonCodeMasterCode' : 'GOODS_DISPLAY_TYPE', 'useYn' : 'Y'}
      , async       : false
      , isDupUrl    : 'Y'
      , style       : {}
      , beforeData  : [{ 'CODE' : 'ALL', 'NAME' : '전체' }]
    });

    // ------------------------------------------------------------------------
    // 비회원노출여부(R)  - 공통코드 아님
    // ------------------------------------------------------------------------
    fnTagMkRadio({
        id      : 'dispNonmemberYn'
      , tagId   : 'dispNonmemberYn'
      , data    : [
                    { 'CODE' : 'ALL', 'NAME' : '전체'   }
                  , { 'CODE' : 'Y'  , 'NAME' : '예'     }
                  , { 'CODE' : 'N'  , 'NAME' : '아니오' }
                  ]
      , chkVal  : 'ALL'
      , style   : {}
    });

    // ------------------------------------------------------------------------
    // 임직원전용여부(C)
    // ------------------------------------------------------------------------
    fnTagMkChkBox({
        id          : 'evEmployeeTp'
      , tagId       : 'evEmployeeTp'
      , url         : '/admin/comn/getCodeList'
      , params      : {'stCommonCodeMasterCode' : 'EV_EMPLOYEE_TP', 'useYn' : 'Y'}
      , async       : false
      , isDupUrl    : 'Y'
      , style       : {}
      , beforeData  : [{ 'CODE' : 'ALL', 'NAME' : '전체' }]
    });

    // ------------------------------------------------------------------------
    // 접근권한설정(회원등급레벨)(C)
    // ------------------------------------------------------------------------
    // 회원마스터
    fnKendoDropDownList({
        id          : 'userMaster'
      , tagId       : 'userMaster'
      , url         : '/admin/comn/getUserMasterCategoryList'
      , textField   : 'groupMasterName'
      , valueField  : 'urGroupMasterId'
      , blank       : '회원그룹전체'
      , async       : false
    });

    // 회원마스터 change event
    $('#userMaster').unbind('change').on('change', function(){
        fnSetUserGroupFilter();
    });

    // ------------------------------------------------------------------------
    // 지급방식(증정방식)(C)
    // ------------------------------------------------------------------------
    fnTagMkChkBox({
        id          : 'giftGiveTp'
      , tagId       : 'giftGiveTp'
      , url         : '/admin/comn/getCodeList'
      , params      : {'stCommonCodeMasterCode' : 'GIFT_GIVE_TP', 'useYn' : 'Y'}
      , async       : true
      , isDupUrl    : 'Y'
      , style       : {}
      , beforeData  : [{ 'CODE' : 'ALL', 'NAME' : '전체' }]
      , success     : function() {
                        $('input[name=giftGiveTp]').eq(0).prop('checked', true).trigger('change');

                        // ----------------------------------------------------
                        // 조회조건 Set : 시간차로 인해 fnSetSearchInfo 에서 처리가 안되는 경우
                        // ----------------------------------------------------
                        if (gbExhibitTp == 'EXHIBIT_TP.GIFT') {
                          // --------------------------------------------------
                          // 증정방식 : 체크
                          // --------------------------------------------------
                          if (fnIsEmpty(gbSearchInfoObj.giftGiveTp) == false) {
                            let giftGiveTpArr = (gbSearchInfoObj.giftGiveTp).split('∀');
                            if (fnIsEmpty(giftGiveTpArr) == false && giftGiveTpArr.length > 0) {
                              $('input[name=giftGiveTp]:checkbox').prop('checked', false);
                              for (var i = 0; i < giftGiveTpArr.length; i++) {
                                $('input:checkbox[name="giftGiveTp"]:input[value="'+giftGiveTpArr[i]+'"]').prop('checked', true);
                              }
                            }
                          }
                        }
                      }
    });

    // ------------------------------------------------------------------------
    // 증정품배송조건(C)
    // ------------------------------------------------------------------------
    fnTagMkRadio({
        id          : 'giftShippingTp'
      , tagId       : 'giftShippingTp'
      , url         : '/admin/comn/getCodeList'
      , params      : {'stCommonCodeMasterCode' : 'GIFT_SHIPPING_TP', 'useYn' :'Y'}
      , async       : true
      , isDupUrl    : 'Y'
      , chkVal      : ''
      , style       : {}
      , beforeData  : [{'CODE':'', 'NAME':'전체'}]
    });

    // ------------------------------------------------------------------------
    // 증정조건(C)
    // ------------------------------------------------------------------------
    fnTagMkRadio({
        id          : 'giftTp'
      , tagId       : 'giftTp'
      , url         : '/admin/comn/getCodeList'
      , params      : {'stCommonCodeMasterCode' : 'GIFT_TP', 'useYn' : 'Y'}
      , async       : true
      , isDupUrl    : 'Y'
      , chkVal      : ''
      , style       : {}
    , beforeData    : [{'CODE':'', 'NAME':'전체'}]
    });

    // ------------------------------------------------------------------------
    // 증정범위(C)
    // ------------------------------------------------------------------------
    fnTagMkRadio({
        id          : 'giftRangeTp'
      , tagId       : 'giftRangeTp'
      , url         : '/admin/comn/getCodeList'
      , params      : {'stCommonCodeMasterCode' : 'GIFT_RANGE_TP', 'useYn' : 'Y'}
      , async       : true
      , isDupUrl    : 'Y'
      , chkVal      : ''
      , style       : {}
    , beforeData    : [{'CODE':'', 'NAME':'전체'}]
    });

    // ------------------------------------------------------------------------
    // 진행기간-시작일자
    // ------------------------------------------------------------------------
    fnKendoDatePicker({
        id          : 'startDt'
      , format      : 'yyyy-MM-dd'
      , btnStartId  : 'startDt'
      , btnEndId    : 'endDt'
      , change      : onChangeStartDt
    });
    fnKendoDatePicker({
        id          : 'endDt'
      , format      : 'yyyy-MM-dd'
      , btnStyle    : true
      , btnStartId  : 'startDt'
      , btnEndId    : 'endDt'
      , change      : onChangeEndDt
    });
    function onChangeStartDt(e) {
      fnOnChangeDatePicker(e, 'start', 'startDt', 'endDt');
    }
    function onChangeEndDt(e) {
      fnOnChangeDatePicker(e, 'end', 'startDt', 'endDt');
    }

    // ------------------------------------------------------------------------
    // 상시진행여부(C)
    // ------------------------------------------------------------------------
    fnTagMkChkBox({
        id        : 'alwaysYn'
      , tagId     : 'alwaysYn'
      , data      : [
                      { 'CODE' : 'Y'   , 'NAME' : '상시진행'}
                    ]
      , chkVal    : ''
      , style     : {}
    });



    //// ------------------------------------------------------------------------
    //// 증정상품기준정렬여부(C)
    //// ------------------------------------------------------------------------
    //fnTagMkChkBox({
    //    id        : 'giftGoodsSortYn'
    //  , tagId     : 'giftGoodsSortYn'
    //  , data      : [
    //                  { 'CODE' : 'Y'   , 'NAME' : '증정상품 기준으로 정렬'}
    //                ]
    //  , chkVal    : ''
    //  , style     : {}
    //});
    //
    //// 증정상품기준정렬여부 노출/숨김 처리
    //if (gbExhibitTp == 'EXHIBIT_TP.GIFT') {
    //  $('#giftGoodsSortYn').show();
    //}
    //else {
    //  $('#giftGoodsSortYn').hide();
    //}

  }

  // ==========================================================================
  // # 초기화 - 이벤트
  // ==========================================================================
  function fnInitEvent() {

    // ------------------------------------------------------------------------
    // 체크박스변경(전체선택관련)
    // ------------------------------------------------------------------------
    fbCheckboxChange();

    // ------------------------------------------------------------------------
    // 상시진행 체크 이벤트
    // ------------------------------------------------------------------------
    $('#alwaysYn').on('click', function(e) {
      if($('input:checkbox[name=alwaysYn]').is(':checked') == true) {
        // 시작일자/종료일자 공백
        $('#startDt').val('');
        $('#endDt').val('');
        // 시작일자/종료일자 비활성
        $('#startDt').data('kendoDatePicker').enable(false);
        $('#endDt').data('kendoDatePicker').enable(false);
        // 일자버튼 비활성
        $('.set-btn-type6').prop('disabled', true);
      }
      else {
        // 시작일자/종료일자 활성
        $('#startDt').data('kendoDatePicker').enable(true);
        $('#endDt').data('kendoDatePicker').enable(true);
        // 일자버튼 활성
        $('.set-btn-type6').prop('disabled', false);
      }
    });


    // ------------------------------------------------------------------------
    // 조회조건 상세검색 버튼 클릭 이벤트
    // ------------------------------------------------------------------------
    // 상세검색에 따른 조건 영역 노출/숨김 처리
    //if (gbExhibitTp == 'EXHIBIT_TP.GIFT') {
    //  // 증정행사 기본값
    //  //$('#btnDetailSearch').show();     // 상세검색버튼
    //  $('#goodsDisplayTypeTr').hide();  // 노출범위(디바이스)/비회원노출여부 Tr
    //  $('#evEmployeeTpTr').hide();      // 임직원전용여부/접근권한설정(회원등급레벨) Tr
    //}
    //else {
    //  $('#btnDetailSearch').hide();
    //}

    // 상세버튼 클릭 이벤트
    //$('#btnDetailSearch').on('click', function() {
    //
    //  if (gbExhibitTp == 'EXHIBIT_TP.GIFT') {
    //    // 증정행사인 경우
    //    if (gbBtnDetailSearchViewYn == true) {
    //      gbBtnDetailSearchViewYn = false;
    //    }
    //    else {
    //      gbBtnDetailSearchViewYn = true;
    //    }
    //
    //    if (gbBtnDetailSearchViewYn == true) {
    //      // 상세검색 노출
    //      $('#goodsDisplayTypeTr').show();  // 노출범위(디바이스)/비회원노출여부 Tr
    //      $('#evEmployeeTpTr').show();      // 임직원전용여부/접근권한설정(회원등급레벨) Tr
    //    }
    //    else {
    //      // 상세검색 숨김
    //      $('#goodsDisplayTypeTr').hide();  // 노출범위(디바이스)/비회원노출여부 Tr
    //      $('#evEmployeeTpTr').hide();      // 임직원전용여부/접근권한설정(회원등급레벨) Tr
    //
    //      // 숨길경우 조건값 초기화
    //      //$('input[name=goodsDisplayType]').eq(0).prop('checked', true).trigger('change');    // 노출범위
    //      //$('#dispNonmemberYn_0').attr("checked", true);                                      // 비회원노출여부
    //      //$('#userMaster').data('kendoDropDownList').select(0);                               // 접근권한설정
    //      //$('input[name=evEmployeeTp]').eq(0).prop('checked', true).trigger('change');        // 임직원전용여부
    //    }
    //  }
    //});
    // ------------------------------------------------------------------------
    // 검색기간 초기화 버튼 클릭
    // ------------------------------------------------------------------------
    //$('[data-id="fnDateBtnC"]').on("click", function(){
    //    $('[data-id="fnDateBtn3"]').mousedown();
    //});

    // ------------------------------------------------------------------------
    // 품목코드 호출 시 품목 상세페이지 새창 호출
    // ------------------------------------------------------------------------
    //$("#itemDetailLink").on("click", function(e){
    //  e.preventDefault();
    //  window.open("#/itemMgmModify?ilItemCode="+ $('#itemCode').val() +"&isErpItemLink=true&masterItemType=MASTER_ITEM_TYPE.COMMON","_blank","width=1800, height=1000, resizable=no, fullscreen=no");
    //});

    // ------------------------------------------------------------------------
    // 증정상품기준정렬여부 체크 이벤트처리
    // ------------------------------------------------------------------------
    //$('#giftGoodsSortYn').on('click', function (e) {
    //
    //  var bGiftGoodsSortYnCheck = $('input:checkbox[name=giftGoodsSortYn]').is(':checked');
    //  if (bGiftGoodsSortYnCheck == true) {
    //    gbGiftGoodsSortYn = 'Y';
    //  }
    //  else {
    //    gbGiftGoodsSortYn = 'N';
    //  }
    //  console.log('# gbGiftGoodsSortYn :: ', gbGiftGoodsSortYn);
    //
    //  // ----------------------------------------------------------------------
    //  // 조회실행
    //  // ----------------------------------------------------------------------
    //  // 그리드 초기화(새로 초기화 시 반듯이 destroy 해야 함)
    //  exhibitGrid.destroy();
    //  // 그리드 초기화
    //  fnInitGrid();
    //  // 조회 실행
    //  fnSearch();
    //});

  }

  // ==========================================================================
  // # 기본 설정
  // ==========================================================================
  function fnSetDefaultPre(){

    if (fnIsEmpty(gbPageParam.evExhibitId) == false) {
        // ----------------------------------------------------------------------
        // 검색구분 : 콤보
        // ----------------------------------------------------------------------
        $('#searchSe').data('kendoDropDownList').value("ID");
        // ----------------------------------------------------------------------
        // 검색어 : Text
        // ----------------------------------------------------------------------
        $('#keyWord').val(gbPageParam.evExhibitId);
    }

    // 기획전유형에 따른 조회조건 노출/숨김 처리
    if (gbExhibitTp == 'EXHIBIT_TP.NORMAL') {
      //$('#showSelectGiftTr').hide();
      $('#showApprovalTr').hide();
      $('#showGift1Tr').hide();
      $('#showGift2Tr').hide();
    }
    else if (gbExhibitTp == 'EXHIBIT_TP.SELECT') {
      //$('#showSelectGiftTr').show();
      $('#showApprovalTr').show();
      $('#showGift1Tr').hide();
      $('#showGift2Tr').hide();
    }
    else if (gbExhibitTp == 'EXHIBIT_TP.GIFT') {
      //$('#showSelectGiftTr').show();
      $('#showApprovalTr').show();
      $('#showGift1Tr').show();
      $('#showGift2Tr').show();
    }

    // ------------------------------------------------------------------------
    // 조회조건 처리
    // ------------------------------------------------------------------------
    //console.log('# ================================================');
    //console.log('# [리스트] 조회조건 Set');
    //console.log('# ================================================');
    //console.log('# ------------------------------------------------');
    //let isFromDetl = '';
    //let searchInfo    = sessionStorage.getItem('searchInfo');
    //let searchInfoObj = new Object();
    //if (searchInfo != null) {
    //  searchInfoObj = JSON.parse(searchInfo);
    //  isFromDetl = searchInfoObj.isFromDetl;
    //  console.log('# searchInfoObj :: ', JSON.stringify(searchInfoObj));
    //}
    //console.log('# isFromDetl :: ', isFromDetl);
    if (fnIsEmpty(gbSearchInfoObj.isFromDetl) == false && gbSearchInfoObj.isFromDetl == 'Y') {
      // ----------------------------------------------------------------------
      // 상세에서 넘어온 경우 : 조회조건 Set
      // ----------------------------------------------------------------------
      fnSetSearchInfo(gbSearchInfoObj);
    }
    else {
      // ----------------------------------------------------------------------
      // 리스트 신규 진입인 경우 : 조회조건 초기화
      // ----------------------------------------------------------------------
      // 접근권한 - 회원등급 체크박스 초기화
      $('input[name=userGroupFilter]').eq(0).prop('checked', true).trigger('change');
      // 몰구분 - 초기화(체크 ALL)
      $('input[name=mallDiv]').eq(0).prop('checked', true).trigger('change');
      // 진행상태 - 초기화(체크 ALL)
      $('input[name=statusYnSe]').eq(0).prop('checked', true).trigger('change');
      // 노출범위 - 초기화(체크 ALL)
      $('input[name=goodsDisplayType]').eq(0).prop('checked', true).trigger('change');
      // 임직원 전용여부 - 초기화(체크 ALL)
      $('input[name=evEmployeeTp]').eq(0).prop('checked', true).trigger('change');
      // 승인상태 전체 체크
      $('input[name=approvalStatus]').eq(0).prop('checked', true).trigger('change');
      // 증정방식 전체 체크
      $('input[name=giftGiveTp]').eq(0).prop('checked', true).trigger('change');
      // 진행기간 - 초기화
      $('.set-btn-type6').attr('fb-btn-active' , false );

      //$('input[name=reviewFilter]').eq(0).prop('checked', true).trigger('change');
      //$('input[name=displayYn]').eq(0).prop('checked', true).trigger('change');
      //$('#searchOneDiv').hide();
      //$('#searchProductDiv').show();
      //$('#searchScoreDiv').show();
      //$('#searchReviewDiv').show();
      //$('#searchDateDiv').show();
      //$('#companyStandardType_1').prop('checked',true);
    }

  };

  // ==========================================================================
  // # 조회조건 Set
  // ==========================================================================
  function fnSetSearchInfo(searchInfoObj) {

    if (fnIsEmpty(searchInfoObj) == false) {
      // ----------------------------------------------------------------------
      // 검색구분 : 콤보
      // ----------------------------------------------------------------------
      $('#searchSe').data('kendoDropDownList').value(searchInfoObj.searchSe);
      // ----------------------------------------------------------------------
      // 검색어 : Text
      // ----------------------------------------------------------------------
      $('#keyWord').val(searchInfoObj.keyWord);
      // ----------------------------------------------------------------------
      // 몰구분 : 체크
      // ----------------------------------------------------------------------
      if (fnIsEmpty(searchInfoObj.mallDiv) == false) {
        let mallDivArr = (searchInfoObj.mallDiv).split('∀');
        if (fnIsEmpty(mallDivArr) == false && mallDivArr.length > 0) {
          $('input[name=mallDiv]:checkbox').prop('checked', false);
          for (var i = 0; i < mallDivArr.length; i++) {
            $('input:checkbox[name="mallDiv"]:input[value="'+mallDivArr[i]+'"]').prop('checked', true);
          }
        }
      }
      // ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
      // 골라담기/증정행사 기획전인 경우
      // ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
      if (gbExhibitTp == 'EXHIBIT_TP.SELECT' || gbExhibitTp == 'EXHIBIT_TP.GIFT') {
        // --------------------------------------------------------------------
        // 담당자ID : 콤보
        // --------------------------------------------------------------------
        $('#managerId').data('kendoDropDownList').value(searchInfoObj.managerId);
        // --------------------------------------------------------------------
        // 승인상태 : 체크
        // --------------------------------------------------------------------
        if (fnIsEmpty(searchInfoObj.approvalStatus) == false) {
          let approvalStatusArr = (searchInfoObj.approvalStatus).split('∀');
          if (fnIsEmpty(approvalStatusArr) == false && approvalStatusArr.length > 0) {
            $('input[name=approvalStatus]:checkbox').prop('checked', false);
            for (var i = 0; i < approvalStatusArr.length; i++) {
              $('input:checkbox[name="approvalStatus"]:input[value="'+approvalStatusArr[i]+'"]').prop('checked', true);
            }
          }
        }
        // ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
        // 증정행사 기획전인 경우
        // ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
        if (gbExhibitTp == 'EXHIBIT_TP.GIFT') {
          // ------------------------------------------------------------------
          // 증정방식 : 체크
          // ------------------------------------------------------------------
          if (fnIsEmpty(searchInfoObj.giftGiveTp) == false) {
            let giftGiveTpArr = (searchInfoObj.giftGiveTp).split('∀');
            if (fnIsEmpty(giftGiveTpArr) == false && giftGiveTpArr.length > 0) {
              $('input[name=giftGiveTp]:checkbox').prop('checked', false);
              for (var i = 0; i < giftGiveTpArr.length; i++) {
                $('input:checkbox[name="giftGiveTp"]:input[value="'+giftGiveTpArr[i]+'"]').prop('checked', true);
              }
            }
          }
          // ------------------------------------------------------------------
          // 증정품 배송 조건 : 라디오
          // ------------------------------------------------------------------
          $('input:radio[name="giftShippingTp"]:radio[value="'+searchInfoObj.giftShippingTp+'"]').prop('checked', true);
          // ------------------------------------------------------------------
          // 증정 조건 : 라디오
          // ------------------------------------------------------------------
          $('input:radio[name="giftTp"]:radio[value="'+searchInfoObj.giftTp+'"]').prop('checked', true);
          // ------------------------------------------------------------------
          // 증정 범위 : 라디오
          // ------------------------------------------------------------------
          $('input:radio[name="giftRangeTp"]:radio[value="'+searchInfoObj.giftRangeTp+'"]').prop('checked', true);
        }
      }
      // ----------------------------------------------------------------------
      // 진행상태 : 체크
      // ----------------------------------------------------------------------
      if (fnIsEmpty(searchInfoObj.statusYnSe) == false) {
        let statusYnSeArr = (searchInfoObj.statusYnSe).split('∀');
        if (fnIsEmpty(statusYnSeArr) == false && statusYnSeArr.length > 0) {
          $('input[name=statusYnSe]:checkbox').prop('checked', false);
          for (var i = 0; i < statusYnSeArr.length; i++) {
            $('input:checkbox[name="statusYnSe"]:input[value="'+statusYnSeArr[i]+'"]').prop('checked', true);
          }
        }
      }
      // ----------------------------------------------------------------------
      // 사용여부 : 라디오
      // ----------------------------------------------------------------------
      $('input:radio[name="useYn"]:radio[value="'+searchInfoObj.useYn+'"]').prop('checked', true);
      // ----------------------------------------------------------------------
      // 노출범위(디바이스) : 체크
      // ----------------------------------------------------------------------
      if (fnIsEmpty(searchInfoObj.goodsDisplayType) == false) {
        let goodsDisplayTypeArr = (searchInfoObj.goodsDisplayType).split('∀');
        if (fnIsEmpty(goodsDisplayTypeArr) == false && goodsDisplayTypeArr.length > 0) {
          $('input[name=goodsDisplayType]:checkbox').prop('checked', false);
          for (var i = 0; i < goodsDisplayTypeArr.length; i++) {
            $('input:checkbox[name="goodsDisplayType"]:input[value="'+goodsDisplayTypeArr[i]+'"]').prop('checked', true);
          }
        }
      }
      // ----------------------------------------------------------------------
      // 비회원 접근권한 : 라디오
      // ----------------------------------------------------------------------
      $('input:radio[name="dispNonmemberYn"]:radio[value="'+searchInfoObj.dispNonmemberYn+'"]').prop('checked', true);
      // ----------------------------------------------------------------------
      // 임직원 전용 여부 : 체크
      // ----------------------------------------------------------------------
      if (fnIsEmpty(searchInfoObj.evEmployeeTp) == false) {
        let evEmployeeTpArr = (searchInfoObj.evEmployeeTp).split('∀');
        if (fnIsEmpty(evEmployeeTpArr) == false && evEmployeeTpArr.length > 0) {
          $('input[name=evEmployeeTp]:checkbox').prop('checked', false);
          for (var i = 0; i < evEmployeeTpArr.length; i++) {
            $('input:checkbox[name="evEmployeeTp"]:input[value="'+evEmployeeTpArr[i]+'"]').prop('checked', true);
          }
        }
      }
      // ----------------------------------------------------------------------
      // 접근권한설정(회원등급레벨) : 콤보+체크
      // ----------------------------------------------------------------------
      // 접근권한그룹
      $('#userMaster').data('kendoDropDownList').value(searchInfoObj.userMaster);
      // 회원등급레벨
      fnSetUserGroupFilter();
      $('input[name=userGroupFilter]').eq(0).prop('checked', false).trigger('change');
      if (fnIsEmpty(searchInfoObj.userGroupFilter) == false) {
        let userGroupFilterArr = (searchInfoObj.userGroupFilter).split('∀');
        if (fnIsEmpty(userGroupFilterArr) == false && userGroupFilterArr.length > 0) {
          $('input[name=userGroupFilter]:checkbox').prop('checked', false);
          for (var i = 0; i < userGroupFilterArr.length; i++) {
            $('input:checkbox[name="userGroupFilter"]:input[value="'+userGroupFilterArr[i]+'"]').prop('checked', true);
          }
        }
      }
      // ----------------------------------------------------------------------
      // 진행기간-시작일자
      // ----------------------------------------------------------------------
      $('#startDt').val(searchInfoObj.startDt);
      // ----------------------------------------------------------------------
      // 진행기간-종료일자
      // ----------------------------------------------------------------------
      $('#endDt').val(searchInfoObj.endDt);
    }

  }


  // ==========================================================================
  // # 조회
  // ==========================================================================
  function fnSearch() {
    //console.log('# fnSearch Start');

    // ------------------------------------------------------------------------
    // Validation
    // ------------------------------------------------------------------------
    if ($('#searchSe').val() == 'NAME') {

      var keyword = $('#keyWord').val();

      if (keyword != null && keyword != 'null' && keyword != '') {
        keyword = $.trim(keyword);

        if (keyword.length < 2) {
          fnMessage('', '기획전명을 두글자 이상 입력하세요.', '');
          return false;
        }
      }
    }

    // ------------------------------------------------------------------------
    // 증정행사 && 상세검색 숨김인 경우 값 초기화
    // ------------------------------------------------------------------------
    //if (gbExhibitTp == 'EXHIBIT_TP.GIFT') {
    //  // 증정행사
    //  if (gbBtnDetailSearchViewYn == false) {
    //    // 상세검색 숨김
    //    $('input[name=goodsDisplayType]').eq(0).prop('checked', true).trigger('change');    // 노출범위
    //    $('input:radio[name="dispNonmemberYn"]:input[value="ALL"]').prop("checked", true);  // 비회원노출여부
    //    $('input[name=evEmployeeTp]').eq(0).prop('checked', true).trigger('change');        // 임직원전용여부
    //    $('#userMaster').data('kendoDropDownList').select(0);                               // 접근권한설정
    //    $("[id=userGroupFilter]").children().each(function(){
    //      $(this).remove();
    //  });
    //  }
    //}

    // 기획전 리스트 조회
    $('#inputForm').formClear(false);
    var data;
    data = $('#searchForm').formSerialize(true);


    // ------------------------------------------------------------------------
    // 조회조건 Set
    // ------------------------------------------------------------------------
    let searchInfoObj = new Object();
    let searchData = JSON.stringify(fnSearchData(data));
    let searchDataObj = JSON.parse(searchData);
    //console.log('# searchDataObj :: ', JSON.stringify(searchDataObj));
    if (fnIsEmpty(searchDataObj) == false) {
      for (var i = 0; i < searchDataObj.length; i++) {
        let field = searchDataObj[i].field;
        let value = searchDataObj[i].value;
        //console.log('# [', field, '] :: [', value, ']');
        searchInfoObj[field] = value;
      }
      //console.log('# searchInfoObj[1] :: ', JSON.stringify(searchInfoObj));
      searchInfoObj.isFromDetl = '';
    }
    //console.log('# searchInfoObj[2] :: ', JSON.stringify(searchInfoObj));
    sessionStorage.setItem('searchInfo', JSON.stringify(searchInfoObj));
    //sessionStorage.setItem('searchInfo', JSON.stringify(fnSearchData(data)));

    // ------------------------------------------------------------------------
    // 최종페이지 정보 Set (페이징 기억 관련)
    // ------------------------------------------------------------------------
    var curPage = LAST_PAGE ? LAST_PAGE : 1;

    var query = {
        page          : curPage
      , pageSize      : PAGE_SIZE
      , filterLength  : fnSearchData(data).length
      , filter        : {
                          filters : fnSearchData(data)
                        }
    };
    exhibitGridDs.query(query);
  }

  // ==========================================================================
  // # 삭제 버튼 클릭
  // ==========================================================================
  function fnBtnExhibitGridDel(evExhibitId) {

    gbMode = 'delete';
    delEvExhibitIdArr = new Array() ;
    delEvExhibitIdArr.push(evExhibitId);

    // 삭제 실행
    fnSave();
  }

  // ==========================================================================
  // # 저장처리
  // ==========================================================================
  function fnSave() {

    var url = '';
    var inParam;
    var confirmMsg = '';

    if (gbMode == 'insert') {
      // ----------------------------------------------------------------------
      // 등록
      // ----------------------------------------------------------------------
      confirmMsg = '등록하시겠습니까?';

    }
    else if (gbMode == 'update') {
      // ----------------------------------------------------------------------
      // 수정
      // ----------------------------------------------------------------------

    }
    else if (gbMode == 'delete') {
      // ----------------------------------------------------------------------
      // 삭제
      // ----------------------------------------------------------------------
      url = '/admin/pm/exhibit/delExhibit';

      if (delEvExhibitIdArr == undefined || delEvExhibitIdArr == null ||
          delEvExhibitIdArr == '' || delEvExhibitIdArr.length <= 0) {

        fnMessage('', '삭제대상 기획전을 확인해주세요.', '');
        return false;
      }

      // ----------------------------------------------------------------------
      // 삭제 Validataion 체크
      // ----------------------------------------------------------------------
      var isCheck = false;

      for (var i = 0; i <delEvExhibitIdArr.length; i++) {

        isCheck = fnCheckValidationEdit(delEvExhibitIdArr[i]);

        if (isCheck == false) {
          break;
        }
      }

      //console.log('# isCheck :: ', isCheck);

      if (isCheck == false) {
        return false;
      }

      // ----------------------------------------------------------------------
      // 삭제 실행
      // ----------------------------------------------------------------------

      confirmMsg = '삭제하시겠습니까?';
      //console.log('#>>>>> JSON.stringify(delEvExhibitIdArr) :: ', JSON.stringify(delEvExhibitIdArr));

      // ----------------------------------------------------------------------
      // 서비스에서의 처리 사항
      // 1. ExhibitManageRequestDto.java 에 String evExhibitIdListString 와 List<String> evExhibitIdListList 선언
      // 2. ExhibitManageController.java 에서 exhibitManageRequestDto.setEvExhibitIdList(BindUtil.convertJsonArrayToDtoList(exhibitManageRequestDto.getEvExhibitIdListString(), String.class)); 처리
      // ----------------------------------------------------------------------
      inParam = {'evExhibitIdListString'  : JSON.stringify(delEvExhibitIdArr)};

      fnKendoMessage({message : confirmMsg, type : 'confirm' , ok : function(){
        fnAjax({
            url     : url
          , params  : inParam
          , success : function( result ){
            fnBizCallback(gbMode, result);
          }
        , isAction : gbMode
        });

      }});

    } // End of else if (gbMode == 'delete')

  }

  // ==========================================================================
  // # 기획전 삭제 Validation Check
  // ==========================================================================
  function fnCheckValidationEdit(inEvExhibitId) {

    var isCheck = false;
    // ------------------------------------------------------------------------
    // 상세조회
    // ------------------------------------------------------------------------
    fnAjax({
        url       : '/admin/pm/exhibit/selectExhibitInfo?evExhibitId=' + inEvExhibitId            // 주소줄에서 ID 보기위해 params 사용안함
      , method    : 'POST'
      , isAction  : 'select'
      , async     : false     // 결과에 의해 진행을 멈춰야 하므로 false 처리
      , success   : function(data, status, xhr) {
                      //console.log('# success data   :: ', JSON.stringify(data));
                      //console.log('# success status :: ', JSON.stringify(status));
                      //console.log('# success xhr    :: ', JSON.stringify(xhr));
                      // ------------------------------------------------------
                      // 상세조회 결과로 삭제 Validation 체크
                      // ------------------------------------------------------
                      isCheck = fnChecDelkValidationExhibit(data.detail);
                    }
      , error     : function(xhr, status, strError) {
                      //console.log('# error xhr      :: ', JSON.stringify(xhr));
                      //console.log('# error status   :: ', JSON.stringify(status));
                      //console.log('# error strError :: ', JSON.stringify(strError));
                      fnMessage('', '기획전 정보를 확인하세요.', '');
                      return false;
                    }
    });

    return isCheck;
  }

  // ==========================================================================
  // # 기획전 수정/삭제 Validation Check
  // ==========================================================================
  function fnChecDelkValidationExhibit(detail) {

    var useYn     = '';
    var statusSe  = '';

    // ------------------------------------------------------------------------
    // 상세조회 결과 체크 - 기획전정보, 기획전유형, 사용여부, 진행상태
    // ------------------------------------------------------------------------
    if (detail == undefined || detail == 'undefined' || detail == null || detail == 'null' || detail == '') {
      fnMessage('', '기획전 정보가 존재하지 않습니다.', '');
      return false;
    }

    if (detail.exhibitTp == undefined || detail.exhibitTp == 'undefined' || detail.exhibitTp == null || detail.exhibitTp == 'null' || detail.exhibitTp == '') {
      fnMessage('', '기획전 유형 정보가 존재하지 않습니다.', '');
      return false;
    }

    if (detail.useYn == undefined || detail.useYn == 'undefined' || detail.useYn == null || detail.useYn == 'null' || detail.useYn == '') {
      fnMessage('', '사용여부 정보를 확인하세요.', '');
      return false;
    }
    if (detail.statusSe == undefined || detail.statusSe == 'undefined' || detail.statusSe == null || detail.statusSe == 'null' || detail.statusSe == '') {
      fnMessage('', '진행상태 정보를 확인하세요.', '');
      return false;
    }

    // ------------------------------------------------------------------------
    // 기획전유형별 삭제 Validataion Check 호출
    // ------------------------------------------------------------------------
    if (detail.exhibitTp == 'EXHIBIT_TP.NORMAL') {
      // 일반기획전
      return fnCheckDelValidationExhibitNormal(detail);
    }
    else if (detail.exhibitTp == 'EXHIBIT_TP.SELECT') {
      // 골라담기
      return fnCheckDelValidationExhibitSelect(detail);
    }
    else if (detail.exhibitTp == 'EXHIBIT_TP.GIFT') {
      // 증정행사
      return fnCheckDelValidationExhibitGift(detail);
    }
    else {
      fnMessage('', '기획전 유형을 확인하세요.', '');
      return false;
    }

  }

  // ==========================================================================
  // # 기획전 삭제 Validation Check - 일반
  // ==========================================================================
  function fnCheckDelValidationExhibitNormal(detail) {

    // ------------------------------------------------------------------------
    // 삭제 - 사용여부
    // ------------------------------------------------------------------------
    if (detail.useYn == 'Y') {

      // ----------------------------------------------------------------------
      // 삭제 - 사용여부(Y) - 진행상태
      // ----------------------------------------------------------------------
      if(detail.statusSe == 'BEF') {
        // 진행예정 : 삭제가능
      }
      else if(detail.statusSe == 'ING') {
        // 진행중   : 삭제불가
        fnMessage('', '삭제가 불가한 게시물입니다.', '');
        return false;
      }
      else if(detail.statusSe == 'END') {
        // 진행종료 : 삭제불가
        fnMessage('', '삭제가 불가한 게시물입니다.', '');
        return false;
      }
      else {
        fnMessage('', '진행상태를 확인하세요.', '');
        return false;
      }
    }
    else if (detail.useYn == 'N') {

      // ----------------------------------------------------------------------
      // 삭제 - 사용여부(N) - 진행상태
      // ----------------------------------------------------------------------
      if(detail.statusSe == 'BEF') {
        // 진행예정 : 삭제가능
      }
      else if(detail.statusSe == 'ING') {
        // 진행중  : 삭제불가
        fnMessage('', '삭제가 불가한 게시물입니다.', '');
        return false;
      }
      else if(detail.statusSe == 'END') {
        // 진행종료 : 삭제불가
        fnMessage('', '삭제가 불가한 게시물입니다.', '');
        return false;
      }
      else {
        fnMessage('', '진행상태를 확인하세요.', '');
        return false;
      }
    }
    else {
      fnMessage('', '삭제가 불가한 게시물입니다.', '');
      return false;
    }
  }

  // ==========================================================================
  // # 기획전 삭제 Validation Check - 골라담기
  // ==========================================================================
  function fnCheckDelValidationExhibitSelect(detail) {

    // ------------------------------------------------------------------------
    // 상세조회 결과 체크 - 승인상태
    // ------------------------------------------------------------------------
    if (detail.approvalStatus == undefined || detail.approvalStatus == 'undefined' || detail.approvalStatus == null || detail.approvalStatus == 'null' || detail.approvalStatus == '') {
      fnMessage('', '승인상태 정보를 확인하세요.', '');
      return false;
    }

    if (detail.approvalStatus == 'APPR_STAT.NONE') {
      // **********************************************************************
      // 1. 승인상태(승인대기(저장)) - 사용여부
      // **********************************************************************
      if (detail.useYn == 'Y') {
        // --------------------------------------------------------------------
        // 승인상태(저장) - 사용여부(사용)
        // --------------------------------------------------------------------
        // 모든 진행상태에 대해 삭제 가능
        if(detail.statusSe == 'BEF') {
          // 진행예정 : 삭제가능
        }
        else if(detail.statusSe == 'ING') {
          // 진행중  : 삭제가능
        }
        else if(detail.statusSe == 'END') {
          // 진행종료 : 삭제가능
        }
        else {
          fnMessage('', '진행상태를 확인하세요.[SAVE-Y]', '');
          return false;
        }
      }
      else if (detail.useYn == 'N') {
        // --------------------------------------------------------------------
        // 승인상태(저장) - 사용여부(미사용)
        // --------------------------------------------------------------------
        // 모든 진행상태에 대해 삭제 가능
        if(detail.statusSe == 'BEF') {
          // 진행예정 : 삭제가능
        }
        else if(detail.statusSe == 'ING') {
          // 진행중  : 삭제가능
        }
        else if(detail.statusSe == 'END') {
          // 진행종료 : 삭제가능
        }
        else {
          fnMessage('', '진행상태를 확인하세요.[SAVE-N]', '');
          return false;
        }
      }
      else {
        fnMessage('', '삭제가 불가한 게시물입니다.', '');
        return false;
      }
    }
    else if (detail.approvalStatus == 'APPR_STAT.REQUEST' || detail.approvalStatus == 'APPR_STAT.SUB_APPROVED') {
      // **********************************************************************
      // 2. 승인상태(승인요청) - 사용여부
      // **********************************************************************
      if (detail.useYn == 'Y') {
        // --------------------------------------------------------------------
        // 승인상태(승인요청) - 사용여부(사용)
        // --------------------------------------------------------------------
        // 모든 진행상태에 대해 삭제 불가
        if(detail.statusSe == 'BEF') {
          // 진행예정 : 삭제불가
          fnMessage('', '삭제가 불가한 게시물입니다.', '');
          return false;
        }
        else if(detail.statusSe == 'ING') {
          // 진행중  : 삭제불가
          fnMessage('', '삭제가 불가한 게시물입니다.', '');
          return false;
        }
        else if(detail.statusSe == 'END') {
          // 진행종료 : 삭제불가
          fnMessage('', '삭제가 불가한 게시물입니다.', '');
          return false;
        }
        else {
          fnMessage('', '진행상태를 확인하세요.', '');
          return false;
        }
      }
      else if (detail.useYn == 'N') {
        // --------------------------------------------------------------------
        // 승인상태(승인요청) - 사용여부(미사용)
        // --------------------------------------------------------------------
        // 모든 진행상태에 대해 삭제 불가
        if(detail.statusSe == 'BEF') {
          // 진행예정 : 삭제불가
          fnMessage('', '삭제가 불가한 게시물입니다.', '');
          return false;
        }
        else if(detail.statusSe == 'ING') {
          // 진행중  : 삭제불가
          fnMessage('', '삭제가 불가한 게시물입니다.', '');
          return false;
        }
        else if(detail.statusSe == 'END') {
          // 진행종료 : 삭제불가
          fnMessage('', '삭제가 불가한 게시물입니다.', '');
          return false;
        }
        else {
          fnMessage('', '진행상태를 확인하세요.', '');
          return false;
        }
      }
      else {
        fnMessage('', '삭제가 불가한 게시물입니다.', '');
        return false;
      }
    }
    else if (detail.approvalStatus == 'APPR_STAT.CANCEL') {
      // **********************************************************************
      // 3. 승인상태(요청철회) - 사용여부
      // **********************************************************************
      if (detail.useYn == 'Y') {
        // --------------------------------------------------------------------
        // 승인상태(저장) - 사용여부(사용)
        // --------------------------------------------------------------------
        // 모든 진행상태에 대해 삭제 가능
        if(detail.statusSe == 'BEF') {
          // 진행예정 : 삭제가능
        }
        else if(detail.statusSe == 'ING') {
          // 진행중  : 삭제가능
        }
        else if(detail.statusSe == 'END') {
          // 진행종료 : 삭제가능
        }
        else {
          fnMessage('', '진행상태를 확인하세요.', '');
          return false;
        }
      }
      else if (detail.useYn == 'N') {
        // --------------------------------------------------------------------
        // 승인상태(저장) - 사용여부(미사용)
        // --------------------------------------------------------------------
        // 모든 진행상태에 대해 삭제 가능
        if(detail.statusSe == 'BEF') {
          // 진행예정 : 삭제가능
        }
        else if(detail.statusSe == 'ING') {
          // 진행중  : 삭제가능
        }
        else if(detail.statusSe == 'END') {
          // 진행종료 : 삭제가능
        }
        else {
          fnMessage('', '진행상태를 확인하세요.', '');
          return false;
        }
      }
      else {
        fnMessage('', '삭제가 불가한 게시물입니다.', '');
        return false;
      }
    }
    else if (detail.approvalStatus == 'APPR_STAT.DENIED') {
      // **********************************************************************
      // 4. 승인상태(승인반려) - 사용여부
      // **********************************************************************
      if (detail.useYn == 'Y') {
        // --------------------------------------------------------------------
        // 승인상태(저장) - 사용여부(사용)
        // --------------------------------------------------------------------
        // 모든 진행상태에 대해 삭제 가능
        if(detail.statusSe == 'BEF') {
          // 진행예정 : 삭제가능
        }
        else if(detail.statusSe == 'ING') {
          // 진행중  : 삭제가능
        }
        else if(detail.statusSe == 'END') {
          // 진행종료 : 삭제가능
        }
        else {
          fnMessage('', '진행상태를 확인하세요.', '');
          return false;
        }
      }
      else if (detail.useYn == 'N') {
        // --------------------------------------------------------------------
        // 승인상태(저장) - 사용여부(미사용)
        // --------------------------------------------------------------------
        // 모든 진행상태에 대해 삭제 가능
        if(detail.statusSe == 'BEF') {
          // 진행예정 : 삭제가능
        }
        else if(detail.statusSe == 'ING') {
          // 진행중  : 삭제가능
        }
        else if(detail.statusSe == 'END') {
          // 진행종료 : 삭제가능
        }
        else {
          fnMessage('', '진행상태를 확인하세요.', '');
          return false;
        }
      }
      else {
        fnMessage('', '삭제가 불가한 게시물입니다.', '');
        return false;
      }
    }
    else if (detail.approvalStatus == 'APPR_STAT.APPROVED') {
      // **********************************************************************
      // 5. 승인상태(승인완료) - 사용여부
      // **********************************************************************
      if (detail.useYn == 'Y') {
        // --------------------------------------------------------------------
        // 승인상태(승인완료) - 사용여부(사용)
        // --------------------------------------------------------------------
        // 모든 진행상태에 대해 삭제 불가
        if(detail.statusSe == 'BEF') {
          // 진행예정 : 삭제불가
          fnMessage('', '삭제가 불가한 게시물입니다.', '');
          return false;
        }
        else if(detail.statusSe == 'ING') {
          // 진행중  : 삭제불가
          fnMessage('', '삭제가 불가한 게시물입니다.', '');
          return false;
        }
        else if(detail.statusSe == 'END') {
          // 진행종료 : 삭제불가
          fnMessage('', '삭제가 불가한 게시물입니다.', '');
          return false;
        }
        else {
          fnMessage('', '진행상태를 확인하세요.', '');
          return false;
        }
      }
      else if (detail.useYn == 'N') {
        // --------------------------------------------------------------------
        // 승인상태(승인완료) - 사용여부(미사용)
        // --------------------------------------------------------------------
        // 모든 진행상태에 대해 삭제 불가
        if(detail.statusSe == 'BEF') {
          // 진행예정 : 삭제불가
          fnMessage('', '삭제가 불가한 게시물입니다.', '');
          return false;
        }
        else if(detail.statusSe == 'ING') {
          // 진행중  : 삭제불가
          fnMessage('', '삭제가 불가한 게시물입니다.', '');
          return false;
        }
        else if(detail.statusSe == 'END') {
          // 진행종료 : 삭제불가
          fnMessage('', '삭제가 불가한 게시물입니다.', '');
          return false;
        }
        else {
          fnMessage('', '진행상태를 확인하세요.', '');
          return false;
        }
      }
      else {
        fnMessage('', '삭제가 불가한 게시물입니다.', '');
        return false;
      }
    }
    else {
      fnMessage('', '유효하지 않은 승인상태입니다.', '');
      return false;
    }
  }

  // ==========================================================================
  // # 기획전 수정/삭제 Validation Check - 증정행사
  // ==========================================================================
  function fnCheckDelValidationExhibitGift(detail) {

    // 골라담기와 동일하여 골라담기 호출
    return fnCheckDelValidationExhibitSelect(detail);
  }

  // ------------------------------- Grid Start -------------------------------
  // ==========================================================================
  // # 그리드
  // ==========================================================================
  function fnInitGrid(){

    // ------------------------------------------------------------------------
    // 최종 페이지 관련 (페이징 기억 관련)
    // ------------------------------------------------------------------------
    var lastPage = sessionStorage.getItem('lastPage');
    LAST_PAGE = lastPage ? JSON.parse(lastPage) : null;

    // ------------------------------------------------------------------------
    // 컬럼 노출/숨김 설정
    // ------------------------------------------------------------------------
    var bStatusManageHiddenYn = true;   // 상태관리 컬럼 숨김여부
    var bGiftColHiddenYn      = true;   // 증정행사 관련 컬럼 숨김여부
    var userNmTitle           = '';     // 담당자/승인요청자 타이틀 이름

    if (gbExhibitTp == 'EXHIBIT_TP.NORMAL') {
      bStatusManageHiddenYn = true;
      bGiftColHiddenYn      = true;
      userNmTitle           = '담당자';
    }
    else if (gbExhibitTp == 'EXHIBIT_TP.SELECT') {
      bStatusManageHiddenYn = false;
      bGiftColHiddenYn      = true;
      userNmTitle           = '담당자';
    }
    else if (gbExhibitTp == 'EXHIBIT_TP.GIFT') {
      bStatusManageHiddenYn = false;
      bGiftColHiddenYn      = false;
      userNmTitle           = '담당자';     // '승인요청자';
    }

    // ------------------------------------------------------------------------
    // 가로스크롤
    // ------------------------------------------------------------------------
    var bScrollable = false;
    var bLocked     = false;
    var bLockable   = true;

    if (gbExhibitTp == 'EXHIBIT_TP.GIFT') {
      // 증정행사인 경우만 가로 스크롤
      bScrollable = true;
      bLocked     = true;
      bLockable   = false;
    }
    //console.log('# scrollable :: ', bScrollable);
    //console.log('# locked     :: ', bLocked);
    //console.log('# lockable   :: ', bLockable);

    // ------------------------------------------------------------------------
    // 그리드 호출
    // ------------------------------------------------------------------------
    exhibitGridDs = fnGetPagingDataSource({
        url      : '/admin/pm/exhibit/selectExhibitList'
      , pageSize : PAGE_SIZE
    });

    exhibitGridOpt  = {
        dataSource  : exhibitGridDs
      , pageable    : {
                        pageSizes   : [20, 30, 50]
                      , buttonCount : 10
                      }
      , height      : 400
      , navigatable : true
      , scrollable  : true
      , columns     : [
                        {                           title: 'No'           , width: 50 , attributes:{ style:'text-align:center' }
                                                                          , template: '<span class="row-number"></span>'
                                                                          , locked: bLocked, lockable:bLockable
                        }
                      , { field:'exhibitTpNm'     , title: '기획전유형'   , width:120 , attributes:{ style:'text-align:center' }, hidden:true, locked: bLocked}
                      , { field:'evExhibitId'     , title: '기획전ID'     , width: 60 , attributes:{ style:'text-align:center' }, locked: bLocked}
                      , { field:'mallDivNm'       , title: '몰구분'       , width: 80 , attributes:{ style:'text-align:center' }, locked: bLocked}
                      , { field:'title'           , title: '기획전제목'   , width:200 , attributes:{ style:'text-align:left'   }, locked: bLocked}

                      //, { field:'repGoodsYn'      , title: '대표상품여부' , width: 80 , attributes:{ style:'text-align:center' }, hidden:bGiftColHiddenYn  }
                      , { field:'repGoodsId'      , title: '대표증정상품코드' , width:100 , attributes:{ style:'text-align:center' }, hidden:bGiftColHiddenYn  }
                      , { field:'repGoodsNm'      , title: '대표증정상품명'   , width:160 , attributes:{ style:'text-align:center' }, hidden:bGiftColHiddenYn  }

                      , { field:'giftTpNm'        , title: '증정조건'     , width: 80 , attributes:{ style:'text-align:center' }, hidden:bGiftColHiddenYn  }
                      , { field:'giftRangeTpNm'   , title: '증정범위'     , width:140 , attributes:{ style:'text-align:center' }, hidden:bGiftColHiddenYn
                                                                          , template: function(dataItem) {
                                                                                      if (gbExhibitTp == 'EXHIBIT_TP.GIFT') {
                                                                                        if (dataItem.giftTp == 'GIFT_TP.CART') {
                                                                                          // TODO replace 오류확인할 것
                                                                                          //var overPriceCom = (dataItem.overPrice).replace(/(\d)(?=(?:\d{3})+(?!\d))/g, '$1,');
                                                                                          var overPriceCom = dataItem.overPrice;
                                                                                          //return overPriceCom + '원 이상 ' + dataItem.giftRangeTpNm;
                                                                                          if (overPriceCom != undefined && overPriceCom != null && overPriceCom != '') {
                                                                                            return fnNumberWithCommas(overPriceCom) + '원 이상 ' + dataItem.giftRangeTpNm;
                                                                                          }
                                                                                          else {
                                                                                            return '';
                                                                                          }
                                                                                        }
                                                                                        else {
                                                                                          return '';
                                                                                        }
                                                                                      }
                                                                                      else {
                                                                                        return '';
                                                                                      }
                                                                                    }
                        }
                      , { field:'startDt'         , title: '시작일자'     , width:140 , attributes:{ style:'text-align:center' }
                        , template: function(dataItem) {
                            if(dataItem.alwaysYn == 'Y'){
                                return '상시진행';
                            }
                            return dataItem.startDt;
                        }
                      }
                      , { field:'endDt'           , title: '종료일자'     , width:140 , attributes:{ style:'text-align:center' }
                        , template: function(dataItem) {
                            if(dataItem.alwaysYn == 'Y'){
                                return '상시진행';
                            }
                            return dataItem.endDt;
                        }
                      }
                      , { field:'giftGiveTpNm'    , title: '증정방식'     , width: 80 , attributes:{ style:'text-align:center' }, hidden:bGiftColHiddenYn  }
                      , { field:'giftShippingTpNm', title: '배송조건'     , width: 80 , attributes:{ style:'text-align:center' }, hidden:bGiftColHiddenYn  }
                      , { field:'device'          , title: '디바이스'     , width:140 , attributes:{ style:'text-align:center' }, hidden:true
                                                                          , template: function(dataItem) {
                                                                                        var rtnStr = '';
                                                                                        var yesCnt = 0;
                                                                                        var sepStr = '';

                                                                                        // PC
                                                                                        if (dataItem.dispWebPcYn == 'Y') {
                                                                                          rtnStr = dataItem.dispWebPcYnNm;
                                                                                          yesCnt++;
                                                                                        }
                                                                                        // Mobile
                                                                                        if (dataItem.dispWebMobileYn == 'Y') {

                                                                                          if (yesCnt > 0) {
                                                                                            sepStr = '/';
                                                                                          }

                                                                                          rtnStr += (sepStr + dataItem.dispWebMobileYnNm);
                                                                                          yesCnt++;
                                                                                        }
                                                                                        // APP
                                                                                        if (dataItem.dispAppYn == 'Y') {

                                                                                          if (yesCnt > 0) {
                                                                                            sepStr = '/';
                                                                                          }

                                                                                          rtnStr += (sepStr + dataItem.dispAppYnNm);
                                                                                        }

                                                                                        if (yesCnt == 3) {
                                                                                          rtnStr = '전체';
                                                                                        }
                                                                                        return  rtnStr;
                                                                                      }
                        }
                      , { field:'evEmployeeTpNm'  , title: '회원유형'     , width: 80 , attributes:{ style:'text-align:center' }}
                      //, { field:'exhibitUserGroup', title: '회원등급'     , width: 60 , attributes:{ style:'text-align:center' }
                      //                                                    , template: function(dataItem) {
                      //                                                                  var userGroupList = dataItem.userGroupList;
                      //                                                                  if(userGroupList == null || userGroupList.length == 0){
                      //                                                                      return '전체';
                      //                                                                  }
                      //                                                                  var result = '';
                      //                                                                  for(var userGroup of userGroupList){
                      //                                                                      result += '[' + userGroup.groupName + ']<BR> ';
                      //                                                                  }
                      //                                                                  return result;
                      //                                                                }
                      //}
                      , { field:'statusNm'        , title: '진행상태'     , width: 60 , attributes:{ style:'text-align:center' }
                                                                          //, template: function(dataItem) {
                                                                          //              if(dataItem.alwaysYn == 'Y'){
                                                                          //                return '진행중';
                                                                          //              }
                                                                          //              return dataItem.statusNm;
                                                                          //            }
                        }
                      , { field:'useYn'           , title: '사용여부'     , width: 50 , attributes:{ style:'text-align:center' }
                                                                          , template: function(dataItem) {
                                                                                        if (dataItem.useYn == 'Y') {
                                                                                          return '예';
                                                                                        }
                                                                                        else {
                                                                                          return '아니오';
                                                                                        }
                                                                                      }
                        }
                      , { field:'dispYn'          , title: '목록노출'     , width: 50 , attributes:{ style:'text-align:center' }, hidden:true
                                                                          , template: function(dataItem) {
                                                                                        if (dataItem.dispYn == 'Y') {
                                                                                          return '예';
                                                                                        }
                                                                                        else {
                                                                                          return '아니오';
                                                                                        }
                                                                                      }
                        }
                      , { field:'userNm'          , title: userNmTitle    , width:120 , attributes:{ style:'text-align:center' }
                                                                        , template: function(dataItem) {
                                                                                  return  dataItem.userNm + '/' + dataItem.createLoginId;
                                                                                }
                        }
                      , { field:'manageStatus'    , title: '승인상태'     , width:140 , attributes: {style:'text-align:center;'}, hidden:bStatusManageHiddenYn
                                                                          , template: function(dataItem) {
                                                                                        var result= '<div id="pageMgrButtonArea" class="textCenter">' + fnNvl(dataItem.approvalStatusName);

                                                                                        if(fnIsProgramAuth("SAVE")) {

                                                                                          if(dataItem.approvalStatus == 'APPR_STAT.NONE' ||
                                                                                              dataItem.approvalStatus == 'APPR_STAT.CANCEL' ||
                                                                                              dataItem.approvalStatus == 'APPR_STAT.DENIED') {
                                                                                            // --------------------------------------------------
                                                                                            // 저장/요청철회/승인반려
                                                                                            // --------------------------------------------------
                                                                                            result += ' <button type="button" class="btn-gray btn-s"  kind="approvalRequest" >승인요청</button>'
                                                                                          }
                                                                                          else if(dataItem.approvalStatus == 'APPR_STAT.REQUEST') {
                                                                                            // --------------------------------------------------
                                                                                            // 승인요청
                                                                                            // --------------------------------------------------
                                                                                            let sessionUserId = PG_SESSION.userId;  // 세션사용자ID
                                                                                            let createId      = dataItem.createId;  // 등록자ID
                                                                                            //console.log('# sessionUserId :: ', sessionUserId);
                                                                                            //console.log('# createId      :: ', createId);
                                                                                            // 요청철회 버튼
                                                                                            if (sessionUserId != undefined && sessionUserId != null && sessionUserId != '' &&
                                                                                                createId      != undefined && createId      != null && createId != '' ) {
                                                                                              if (sessionUserId == createId) {
                                                                                                // 로그인.사용자ID = 기획전등록자ID
                                                                                                result += ' <button type="button" class="btn-gray btn-s"  kind="approvalCancel" >요청철회</button>'
                                                                                              }
                                                                                              else {
                                                                                                // 미노출
                                                                                              }
                                                                                            }
                                                                                            else {
                                                                                            }
                                                                                            //result += ' <button type="button" class="btn-gray btn-s"  kind="approvalCancel" >요청철회</button>'
                                                                                          }

                                                                                        }
                                                                                        result += '</div>';

                                                                                        return result;
                                                                                      }
                        }
                      , { field:'management'      , title: '관리'         , width:140 , attributes: {style:'text-align:center;'}, hidden:true
                                                                          , template: function(dataItem) {
                                                                                        let delAbleYn = fnExhibitDelYn(dataItem.exhibitTp, dataItem.statusSe, dataItem.approvalStatus);
                                                                                        let delDisableStr = '';
                                                                                        let btnStr = '<div id="pageMgrButtonArea" class="textCenter">';
                                                                                        //console.log('# 삭제가능여부 :: ', delAbleYn);
                                                                                        if (delAbleYn == true) {
                                                                                          delDisableStr = '';
                                                                                        }
                                                                                        else {
                                                                                          delDisableStr = 'disabled';
                                                                                        }

                                                                                       if(fnIsProgramAuth("SAVE")) {
                                                                                          btnStr += '&nbsp;'
                                                                                                  +'<button type="button" class="btn-gray btn-s" kind="btnExhibitGridEdit">수정 </button>';
                                                                                       }
                                                                                       if(fnIsProgramAuth("DELETE")) {
                                                                                          btnStr +='&nbsp;'
                                                                                                + '<button type="button" name="btnExhibitDel" class="btn-red btn-s"  kind="btnExhibitGridDel" '+delDisableStr+'>삭제 </button>';
                                                                                       }
                                                                                        btnStr += '</div>';

                                                                                        return btnStr;
                                                                                      }
                                                                          , lockable: bLockable
                        }
                      ]
    };

    exhibitGrid = $('#exhibitGrid').initializeKendoGrid( exhibitGridOpt ).cKendoGrid();

    //// ------------------------------------------------------------------------
    //// 그리드 클릭 이벤트
    //// ------------------------------------------------------------------------
    //$('#exhibitGrid').on('click', 'tbody>tr', function () {
    //  fnGridClick();
    //});

    // ------------------------------------------------------------------------
    // NO 항목 및 전체건수 Set
    // ------------------------------------------------------------------------
    exhibitGrid.bind('dataBound', function(e) {
      var row_num = exhibitGridDs._total - ((exhibitGridDs._page - 1) * exhibitGridDs._pageSize);
      $('#exhibitGrid tbody > tr .row-number').each(function(index){
        $(this).html(row_num);
        row_num--;
      });
      // ----------------------------------------------------------------------
      // 전체건수 Set
      // ----------------------------------------------------------------------
      $('#totalCnt').text(exhibitGridDs._total);

      // ----------------------------------------------------------------------
      // 세션 lastPage 삭제(페이징 기억 관련)
      // ----------------------------------------------------------------------
      if( sessionStorage.getItem('lastPage') ) {
        delete sessionStorage.lastPage;
      }

    });

    // ------------------------------------------------------------------------
    // 승인요청 버튼 클릭
    // ------------------------------------------------------------------------
    $('#exhibitGrid').on('click', 'button[kind=approvalRequest]', function(e) {
      e.preventDefault();
      let dataItem = exhibitGrid.dataItem($(e.currentTarget).closest('tr'));
      fnSetManagerByTaskAppr(dataItem);
    });

    // ------------------------------------------------------------------------
    // 요청철회 버튼 클릭
    // ------------------------------------------------------------------------
    $('#exhibitGrid').on('click', 'button[kind=approvalCancel]', function(e) {
      e.preventDefault();
      let dataItem = exhibitGrid.dataItem($(e.currentTarget).closest('tr'));
      let params = {};
        params.evExhibitIdList = [];
        params.evExhibitIdList[0] = dataItem.evExhibitId;
      fnCancelRequest(params);
    });

    // ------------------------------------------------------------------------
    // 수정 버튼 클릭
    // ------------------------------------------------------------------------
    $('#exhibitGrid').on('click', 'button[kind=btnExhibitGridEdit]', function(e) {
      e.preventDefault();

      // ----------------------------------------------------------------------
      // 세션 lastPage 삭제(페이징 기억 관련)
      // ----------------------------------------------------------------------
      var curPage = exhibitGridDs._page;
      sessionStorage.setItem('lastPage', JSON.stringify(curPage));

      let dataItem = exhibitGrid.dataItem($(e.currentTarget).closest('tr'));

      if (dataItem != null && dataItem != 'null' && dataItem != '') {
        //console.log('# dataItem :: ', JSON.stringify(dataItem));
      }
      // 참고 : 아래의 경우는 버튼 클릭시 값을 가져오지 못함, 로우를 선택해야 함
      //let dataItem2 = exhibitGrid.dataItem(exhibitGrid.select());
      //console.log('# btnInventoryEdit.dataItem :: ' + JSON.stringify(dataItem));
      //fnBtnInventoryEdit(dataItem);

      // 수정 버튼 클릭
      fnBtnExhibitEdit(dataItem);
    });

    // ------------------------------------------------------------------------
    // 삭제 버튼 클릭
    // ------------------------------------------------------------------------
    $('#exhibitGrid').on('click', 'button[kind=btnExhibitGridDel]', function(e) {
      e.preventDefault();
      let dataItem = exhibitGrid.dataItem($(e.currentTarget).closest('tr'));
      //let param = {};
      //param.dpPageId      = dataItem.dpPageId;
      //param.dpInventoryId = dataItem.dpInventoryId;
      //console.log('# btnInventoryDel.dataItem :: ' + JSON.stringify(dataItem));
      //fnBtnInventoryDel(dataItem.dpInventoryId);
      //fnBtnExhibitDel(dataItem);    // -> fnExhibitDel 에서 삭제 처리
      fnBtnExhibitGridDel(dataItem.evExhibitId);

    });

  }
  // ------------------------------- Grid End ---------------------------------

  // ==========================================================================
  // # 삭제가능여부
  // ==========================================================================
  function fnExhibitDelYn(exhibitTp, statusSe, approvalStatus) {
    //console.log('# ------------------------------');
    //console.log('# exhibitTp      :: ', exhibitTp);
    //console.log('# statusSe       :: ', statusSe);
    //console.log('# approvalStatus :: ', approvalStatus);
    let delAbleYn;

    if (exhibitTp == 'EXHIBIT_TP.NORMAL') {
      // ----------------------------------------------------------------------
      // 일반기획전
      // ----------------------------------------------------------------------
      if (statusSe != undefined && statusSe != null && statusSe == 'BEF') {
        // 진행상태 : 진행예정인
        delAbleYn = true;
      }
      else {
        // 진행상태 : 진행중/진행완료
        delAbleYn = false;
      }
    }
    else {
      // ----------------------------------------------------------------------
      // 골라담기/증정행사
      // ----------------------------------------------------------------------
      if ( approvalStatus == 'APPR_STAT.NONE' || approvalStatus == 'APPR_STAT.CANCEL' || approvalStatus == 'APPR_STAT.DENIED' ||
          (approvalStatus == 'APPR_STAT.APPROVED' && (statusSe == 'BEF' || statusSe == 'ING')) ){
        // 승인상태:승인대기/요청철회/승인반려/(승인완료 중 진행예정/진행중) : 수정가능
        delAbleYn = true;
      }
      else {
        delAbleYn = false;
      }
      // ----------------------------------------------------------------------
      // 삭제버튼
      // ----------------------------------------------------------------------
      if (approvalStatus == 'APPR_STAT.NONE' || approvalStatus == 'APPR_STAT.CANCEL' || approvalStatus == 'APPR_STAT.DENIED' ) {
        // 승인상태 : 승인대기/요청철회/승인반려 : 삭제 가능
        // 진행상태가 진행예정인 경우
        delAbleYn = true;
      }
      else {
        // 승인상태 : 승인요청/승인완료(부)/승인완료
        delAbleYn = false;
      }
    }
    return delAbleYn;
  }



  // ==========================================================================
  // 신규등록 버튼 클릭
  // ==========================================================================
  function fnBtnExhibitNew() {

    // ----------------------------------------------------------------------
    // 세션 lastPage 삭제(페이징 기억 관련)
    // ----------------------------------------------------------------------
    var curPage = exhibitGridDs._page;
    sessionStorage.setItem('lastPage', JSON.stringify(curPage));

    // 링크정보
    let option = {};

    //option.url    = '#/exhibitMgm?exhibitTp='+gbExhibitTp;
    if (pageId == 'exhibitSelectList') {
        option.url    = '#/exhibitSelectMgm';
    } else {
        option.url    = '#/exhibitMgm';
    }
    // 기획전 등록/수정 : 100008059 (ST_MENU.ST_MENU_ID/GB_DIC_MST_ID 참조)
    option.menuId = 959;
    //option.menuId = 100008059;
    option.target = '_blank';
    option.data = { exhibitTp : gbExhibitTp, mode : 'insert' };
    // 화면이동
    fnGoNewPage(option);  // 새페이지(탭)
//    fnGoPage(option);
  }

  // ==========================================================================
  // 승인 담당자 지정
  // ==========================================================================
  function fnSetManagerByTaskAppr(dataItem){
    var param = '';
    if (gbExhibitTp == 'EXHIBIT_TP.SELECT') {
      param  = {'taskCode' : 'APPR_KIND_TP.EXHIBIT_SELECT' };
    }
    else if (gbExhibitTp == 'EXHIBIT_TP.GIFT') {
      param  = {'taskCode' : 'APPR_KIND_TP.EXHIBIT_GIFT' };
    }

    fnKendoPopup({
        id          : 'approvalManagerSearchPopup'
      , title       : '승인관리자 선택'
      , src         : '#/approvalManagerSearchPopup'
      , param       : param
      , width       : '1600px'
      , height      : '800px'
      , scrollable  : 'yes'
      , success     : function(stMenuId, data) {
                        if(data && !fnIsEmpty(data) && data.authManager2nd) {
                          if(data.authManager1st != undefined) {
                            dataItem.approvalSubUserId = data.authManager1st.apprUserId;
                          }
                          dataItem.approvalUserId = data.authManager2nd.apprUserId;

                          fnApprovalRequest(dataItem);
                        }
                      }
    });
  }

  // ==========================================================================
  // 승인 요청
  // ==========================================================================
  function fnApprovalRequest(dataItem){
    var confirmMsg = '승인요청을 처리하시겠습니까?';

    // ------------------------------------------------------------------------
    // Param Set
    // ------------------------------------------------------------------------
    fnKendoMessage({
        message : fnGetLangData({key :'', nullMsg : confirmMsg })
      , type    : 'confirm'
      , ok      : function() {
                    fnAjax({
                        url       : '/admin/pm/exhibit/putApprovalRequestExhibit'
                      , params    : { 'exhibitDataJsonString' : JSON.stringify(dataItem) }
                      , success   : function( data ) {
                                      fnKendoMessage({
                                          message : '저장이 완료되었습니다.'
                                        , ok      : function() {
                                                      fnSearch();
                                                    }
                                      });
                                    }
                      , error     : function(xhr, status, strError) {
                                      fnKendoMessage({ message : xhr.responseText });
                                    }
                      , isAction  : "update"
                    });
                  }
    });
  }

  // ==========================================================================
  // 요청 철회
  // ==========================================================================
    function fnCancelRequest(params){
      if( fnIsEmpty(params) || fnIsEmpty(params.evExhibitIdList) || params.evExhibitIdList.length < 1) {
        fnKendoMessage({ message : '선택된 기획전이 없습니다.' });
        return;
      }
      params.exhibitTp = gbExhibitTp;

      var confirmMsg = '승인요청을 철회하시겠습니까?';

      // ----------------------------------------------------------------------
      // Param Set
      // ----------------------------------------------------------------------
      fnKendoMessage({
          message : fnGetLangData({key :'', nullMsg : confirmMsg })
        , type    : 'confirm'
        , ok      : function() {
                      fnAjax({
                          url         : '/admin/approval/exhibit/putCancelRequestApprovalExhibit'
                        , params      : params
                        , contentType : 'application/json'
                        , success     : function( data ) {
                                          fnKendoMessage({
                                              message : '저장이 완료되었습니다.'
                                            , ok      : function(){
                                                          fnSearch();
                                                        }
                                          });
                                        }
                        , error       : function(xhr, status, strError) {
                                          fnKendoMessage({ message : xhr.responseText });
                                        }
                        , isAction    : 'update'
                      });
                    }
      });
    };

  // ==========================================================================
  // 수정 버튼 클릭
  // ==========================================================================
  function fnBtnExhibitEdit(dataItem) {

    // ------------------------------------------------------------------------
    // 수정 화면으로 이동
    // ------------------------------------------------------------------------
    // 링크정보
    let option = {};

    if (pageId == 'exhibitSelectList') {
        option.url    = '#/exhibitSelectMgm';
    } else {
        option.url    = '#/exhibitMgm';
    }
    // 기획전 등록/수정 : 100008059 (ST_MENU.ST_MENU_ID/GB_DIC_MST_ID 참조)
    option.menuId = 959;;
    //option.menuId = 100008059;
    option.target = '_blank';
    option.data = { exhibitTp : gbExhibitTp, evExhibitId : dataItem.evExhibitId, mode : 'update'};
    // 화면이동
    fnGoNewPage(option);  // 새페이지(탭)
    //fnGoPage(option);

    // 등록/수정/상세 화면으로 이동(exhibitMgm)
    // 새탭으로 열기
    //window.open('#/exhibitMgm?exhibitTp='+gbExhibitTp+'&evExhibitId='+dataItem.evExhibitId+'&mode=update', '_blank');
    //window.open('#/exhibitList?exhibitTp='+gbExhibitTp+'&evExhibitId='+dataItem.evExhibitId+'&mode=update', '_blank');
    //새창으로 열기
    // window.open('#/exhibitMgm?exhibitTp='+gbExhibitTp+'&evExhibitId='+dataItem.evExhibitId+'&mode=update', '_blank','width=1200, height=1000, resizable=no, fullscreen=no');
  }

  // ==========================================================================
  // 후기관리 상세화면 호출
  // ==========================================================================
  //function fnGridClick() {
  //
  //  var aMap = exhibitGrid.dataItem(exhibitGrid.select());
  //  //console.log('# aMap :: ', JSON.stringify(aMap))
  //
  //  //alert('# 새창열기 준비 중 :: ' + aMap.evExhibitId);
  //  //var aMap = exhibitGrid.dataItem(exhibitGrid.select());
  //  //fnAjax({
  //  //  url     : '/admin/customer/feedback/getDetailFeedback',
  //  //  params  : {feedbackId : aMap.feedbackId},
  //  //  success :
  //  //    function( data ){
  //  //      fnBizCallback('select',data);
  //  //    },
  //  //  isAction : 'select'
  //  //});
  //
  //  // 새창열기 샘플
  //  //$('#itemDetailLink').on('click', function(e){
  //  //  e.preventDefault();
  //  //  window.open("#/itemMgmModify?ilItemCode="+ $('#itemCode').val() +"&isErpItemLink=true&masterItemType=MASTER_ITEM_TYPE.COMMON","_blank","width=1800, height=1000, resizable=no, fullscreen=no");
  //};

  //// ==========================================================================
  //// # 저장
  //// ==========================================================================
  //function fnConfirm(psShippingPattern){
  //  fnKendoMessage({message:'수정된 정보를 저장하시겠습니까?', type : "confirm" ,ok : function(){ fnSave() } });
  //
  //}

  //// ==========================================================================
  //// # 저장
  //// ==========================================================================
  //function fnSave(){
  //
  //  var url = '/admin/customer/feedback/putFeedbackInfo';
  //  var data = $('#inputForm').formSerialize(true);
  //
  //  if(data.adminExcellentYnCheck == 'on'){
  //    data.adminExcellentYnCheck = 'Y';
  //  }
  //
  //  if( data.rtnValid ){
  //    fnAjax({
  //      url     : url,
  //      params  : data,
  //      success :
  //        function( data ){
  //          fnBizCallback('confirm', data);
  //        },
  //        isAction : 'batch'
  //      });
  //  }
  //}
  //

  // ==========================================================================
  // # 접근권한 - 체크박스 설정
  // ==========================================================================
  function fnSetUserGroupFilter(){
    // 기존 체크박스 삭제
    $('[id=userGroupFilter]').children().each(function(){
        $(this).remove();
    });

    if($('#userMaster').data('kendoDropDownList').value() == ''){
        return;
    }

    // 복수조건.외부몰 체크박스 그룹 ALL
    fnTagMkChkBox({
        id          : 'userGroupFilter'
      , url         : '/admin/comn/getUserGroupCategoryList'
      , params      : {'urGroupMasterId' : $('#userMaster').data('kendoDropDownList').value()}
      , tagId       : 'userGroupFilter'
      , async       : false
      , chkVal      : ''
      , style       : {}
      , textField   : 'groupName'
      , valueField  : 'urGroupId'
      , beforeData  : [{ 'CODE' : 'ALL', 'NAME' : '전체' }]
    });

    $('input[name=userGroupFilter]').eq(0).prop('checked', true).trigger('change');
  }

  function fnBtnSelectExhibit() {
    if(exhibitGrid.select() == undefined || exhibitGrid.select() == null || exhibitGrid.dataItem(exhibitGrid.select()) == null) {
      fnKendoMessage({ message : "선택된 기획전이 없습니다." });
      return;
    }
    const selectEvExhibitId = exhibitGrid.dataItem(exhibitGrid.select()).evExhibitId;

    fnAjax({
      url       : '/admin/pm/exhibit/selectExhibitDetlInfo?evExhibitId=' + selectEvExhibitId       // 주소줄에서 ID 보기위해 params 사용안함
      , method    : 'POST'
      , isAction  : 'select'
      , async     : false     // 결과에 의해 진행을 멈춰야 하므로 false 처리
      , success   : function(data, status, xhr) {
        //console.log('# success data   :: ', JSON.stringify(data));
        //console.log('# success status :: ', JSON.stringify(status));
        //console.log('# success xhr    :: ', JSON.stringify(xhr));
        // ------------------------------------------------------
        // 상세조회 결과로 삭제 Validation 체크
        // ------------------------------------------------------
        parent.POP_PARAM = data;

        parent.LAYER_POPUP_OBJECT.data("kendoWindow").close();
      }
      , error     : function(xhr, status, strError) {
        //console.log('# error xhr      :: ', JSON.stringify(xhr));
        //console.log('# error status   :: ', JSON.stringify(status));
        //console.log('# error strError :: ', JSON.stringify(strError));
        fnMessage('', '기획전 정보를 확인하세요.', '');
        return false;
      }
    });

// //      exhibitGrid.selected
//     parent.POP_PARAM = exhibitGrid.dataItem(exhibitGrid.select());
//
//     parent.LAYER_POPUP_OBJECT.data("kendoWindow").close();
  }

  // ==========================================================================
  // # 콜백함수
  // ==========================================================================
  function fnBizCallback( id, data ){

    switch(id){
      case 'insert' :
        // --------------------------------------------------------------------
        // 등록
        // --------------------------------------------------------------------

        break;
      case 'update' :
        // --------------------------------------------------------------------
        // 수정
        // --------------------------------------------------------------------

        break;
      case 'delete' :
        // --------------------------------------------------------------------
        // 삭제
        // --------------------------------------------------------------------
        //alert('삭제 callback Start');
        fnKendoMessage({
            message : '삭제되었습니다.'
          , ok      : function(){
                        // 삭제 후 재조회
                        fnSearch();
                      }
        });

        break;

      //case 'select':
      //  fnKendoInputPoup({height:'auto' ,width:'800px',title:{ nullMsg : '후기 상세 정보'} });
      //  $('#kendoPopup').scrollTop(0);
      //  $('#inputForm').bindingForm(data, 'row', true);
      //  if(data.row.evEventId != null){
      //    $('#adminExcellentYnDiv').show();
      //    $('#adminExcellentYn').show();
      //    $('#adminExcellentView').show();
      //  }else{
      //    $('#adminExcellentYnDiv').hide();
      //    $('#adminExcellentYn').hide();
      //    $('#adminExcellentView').hide();
      //  }
      //  // 베스트후기(자동)
      //  if(data.row.bestCnt > 10){
      //    $('#bestCntYn').val('예');
      //  }else{
      //    $('#bestCntYn').val('아니오');
      //  }
      //  // 베스트후기(관리자)
      //  if(data.row.adminBestYn == 'Y'){
      //    $('#adminBestYn_0').prop('checked',true);
      //  }else{
      //    $('#adminBestYn_1').prop('checked',true);
      //  }
      //  // 공개설정
      //  if(data.row.displayYn == 'Y'){
      //    $('#popupDisplayYn_0').prop('checked',true);
      //  }else{
      //    $('#popupDisplayYn_1').prop('checked',true);
      //  }
      //  // 우수후기선정 체크시 표시처리
      //  if(data.row.adminExcellentYn == 'Y'){
      //    $('#adminExcellentYnCheckDiv').hide();
      //    $('#adminExcellentYnView').show();
      //    $('#adminExcellentYnView').val('우수후기');
      //    $('#adminExcellentYn').val('우수후기');
      //  }else{
      //    $('#adminExcellentYnCheckDiv').show();
      //    $('#adminExcellentYnView').hide();
      //  }
      //
      //  fnAjax({
      //    url    : '/admin/customer/feedback/getImageList',
      //    params  : {feedbackId : data.row.feedbackId},
      //    isAction : 'select',
      //    success  :
      //      function( data ){
      //        var imageHtml = '';
      //        var testurl = 'http://localhost:8280/pulmuone/public/BOS/ur/test/2020/10/29/9F20E0D4B3274952A727.png';
      //        if(data.rows.length>0){
      //          imageHtml += '      <div style="position:relative; display: flex; flex-direction: row; flex-wrap: wrap; justify-content: left;">';
      //          for(var i=0; i < data.rows.length; i++){
      //
      //            var imageUrl = publicStorageUrl + data.rows[i].imageName;
      //            imageHtml += '      <span style="width:150px; height:150px; margin-bottom:10px; margin-right:10px; border-color:#a9a9a9; border-style:solid; border-width:1px">';
      //            imageHtml += '          <img src="'+ imageUrl +'" style="max-width: 100%; max-height: 100%;" onclick="$scope.fnShowImage(\''+imageUrl+'\')">';
      //            imageHtml += '      </span>';
      //          }
      //          imageHtml += '      </div>';
      //
      //          $('#imageContent').html(imageHtml);
      //        }
      //        else{
      //          $('#imageContent').html("");
      //        }
      //      }
      //  });
      //
      //  break;


      case 'confirm':
        fnKendoMessage({
            message : '저장되었습니다.'
          , ok      : function(){
                        fnSearch();
                        fnClose();
                      }
        });
    }
  }

  // ==========================================================================
  // # 팝업창 닫기
  // ==========================================================================
  function fnClose(){
    var kendoWindow =$('#kendoPopup').data('kendoWindow');
    kendoWindow.close();
  }


  // ?? 이벤트
  $('#clear').click(function(){
    $('.resultingarticles').empty();
    $('#searchbox').val('');
  });

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
  // ------------------------------- Html 버튼 바인딩  Start ------------------
  // ==========================================================================
  /** Common Search */
  $scope.fnSearch         = function( )   { fnSearch();         };
  /** Common New */
  $scope.fnBtnExhibitNew  = function( )   { fnBtnExhibitNew();  };
  /** Common Clear */
  $scope.fnClear          = function()    { fnClear();          };
  /** Common Confirm */
  $scope.fnConfirm        = function()    { fnConfirm();        };
  /** Common Close */
  $scope.fnClose          = function( )   { fnClose();          };
  /** Common ShowImage */
  $scope.fnShowImage      = function(url) { fnShowImage(url);   };

  $scope.fnBtnSelectExhibit = function()  { fnBtnSelectExhibit(); };
}); // document ready - END
