/*******************************************************************************
 * -----------------------------------------------------------------------------
 * description : 회원 유형별 판매/매출 현황 통계
 * @
 * @
 * @ 수정일        수정자        수정내용
 * @ ---------------------------------------------------------------------------
 * @ 2021.07.26    이원호        최초생성
 * @
 ******************************************************************************/
'use strict';


//var LAST_PAGE = null;   // 최종 페이지 (페이징 기억 관련)
//var PAGE_SIZE = 20;
var gbPageParam = '';   // 넘어온 페이지파라미터
var publicStorageUrl = fnGetPublicStorageUrl();
var gridDs, gridOpt, grid;

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
    $scope.$emit('fnIsMenu', { flag : 'true' });

    fnPageInfo({
        PG_ID     : 'saleStatics'
      , callback  : fnUI
    });

    // ------------------------------------------------------------------------
    // 페이지파라미터
    // ------------------------------------------------------------------------
    gbPageParam = fnGetPageParam();
  }

  // ==========================================================================
  // # fnUI
  // ==========================================================================
  function fnUI() {

    fnInitButton();           // Initialize Button ----------------------------

    fnInitGrid();             // Initialize Grid ------------------------------

    fnInitOptionBox();        // Initialize Option Box ------------------------

    fnInitEvent();            // Initialize Event -----------------------------

    //fnSearch();               // 조회 -----------------------------------------

    //fnDefaultSet();           // 기본설정 -------------------------------------
  }

  // ==========================================================================
  // # 초기화 - 버튼
  // ==========================================================================
  function fnInitButton() {
    //$('#fnSearch, #fnConfirm, #fnClear, #fnClose, #fnExcelDownload, #fnShowImage').kendoButton();
  }

  // ==========================================================================
  // # 초기화 - 값
  // ==========================================================================
  function fnClear() {
    //console.log('# fnClear Start');
    // ------------------------------------------------------------------------
    // 조회조건 초기화
    // ------------------------------------------------------------------------
    $('#searchForm').formClear(true);

    // VAT포함 : 체크
    $('INPUT[name=vatSumYn]').prop('checked', true);
    // 판매채널유형 : 전체선택
    $("input[name=agentTypeCd]:checkbox").prop("checked", true);

    // ------------------------------------------------------------------------
    // 그리드 초기화
    // ------------------------------------------------------------------------
    fnInitGrid();
    // 그리드 총건수
    $('#totalCnt').text(0);
    //fnDefaultSet();
  }

  // ==========================================================================
  // # 기본 설정
  // ==========================================================================
  function fnDefaultSet(){

  };

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
    // 검색기준유형 [체크] : 주문일/결제일/매출일
    // ------------------------------------------------------------------------
    fnTagMkRadio({
        id      : 'searchTp'
      , tagId   : 'searchTp'
      // , chkVal  : 'singleSection'
      , tab     : true
      , data    : [ {CODE : 'ODR' , NAME  : '주문일'}
                  , {CODE : 'PAY' , NAME  : '결제일'}
                  , {CODE : 'SAL' , NAME  : '매출일'}
                  ]
      , chkVal  : 'ODR'
      , change  : function (e) {}
      , style   : {}
    });

    // ------------------------------------------------------------------------
    // VAT포함여부 [체크]
    // ------------------------------------------------------------------------
    fnTagMkChkBox({
        id        : 'vatSumYn'
      , tagId     : 'vatSumYn'
      , data      : [
                      { 'CODE' : 'Y'    , 'NAME' : 'VAT포함'    }
                    ]
      , chkVal    : 'Y'
      , style     : {}
    });

    // ------------------------------------------------------------------------
    // 기준기간 시작일/종료일 [날짜]
    // ------------------------------------------------------------------------
    fnKendoDatePicker({
        id          : 'startDe'
      , format      : 'yyyy-MM-dd'
      , btnStartId  : 'startDe'
      , btnEndId    : 'endDe'
      , change      : fnOnChangeStartDt
    });
    fnKendoDatePicker({
        id          : 'endDe'
      , format      : 'yyyy-MM-dd'
      , btnStyle    : false     //버튼 숨김
      , btnStartId  : 'startDe'
      , btnEndId    : 'endDe'
      , change      : fnOnChangeEndDt
    });
    function fnOnChangeStartDt(e) {
      fnOnChangeDatePicker(e, 'start', 'startDe', 'endDe');
    }
    function fnOnChangeEndDt(e) {
      fnOnChangeDatePicker(e, 'end', 'startDe', 'endDe');
    }

    // ------------------------------------------------------------------------
    // 대비기간 시작일/종료일 [날짜]
    // ------------------------------------------------------------------------
    fnKendoDatePicker({
        id          : 'contrastStartDe'
      , format      : 'yyyy-MM-dd'
      , btnStartId  : 'contrastStartDe'
      , btnEndId    : 'contrastEndDe'
      , change      : fnOnChangeContrastStartDt
    });
    fnKendoDatePicker({
        id          : 'contrastEndDe'
      , format      : 'yyyy-MM-dd'
      , btnStyle    : false     //버튼 숨김
      , btnStartId  : 'contrastStartDe'
      , btnEndId    : 'contrastEndDe'
      , change      : fnOnChangeContrastEndDt
    });
    function fnOnChangeContrastStartDt(e) {
      fnOnChangeDatePicker(e, 'start', 'contrastStartDe', 'contrastEndDe');
    }
    function fnOnChangeContrastEndDt(e) {
      fnOnChangeDatePicker(e, 'end', 'contrastStartDe', 'contrastEndDe');
    }

    // ------------------------------------------------------------------------
    // 판매채널유형 [체크] : 전체/PC/MOBILE/APP/관리자주문/외부몰
    // ------------------------------------------------------------------------
    fnTagMkChkBox({
        id          : 'agentTypeCd'
      , tagId       : 'agentTypeCd'
      , url         : '/admin/comn/getCodeList'
      , params      : {'stCommonCodeMasterCode' : 'AGENT_TYPE', 'useYn' : 'Y'}
      , isDupUrl    : 'Y'
      , style       : {}
      , beforeData  : [{ 'CODE' : 'ALL', 'NAME' : '전체' }]
      , success     : function() {
                        // 전체선택
                        $("input[name=agentTypeCd]:checkbox").prop("checked", true);
                      }
    });

  }

  // ==========================================================================
  // # 초기화 - 이벤트
  // ==========================================================================
  function fnInitEvent() {

    // ------------------------------------------------------------------------
    // 체크박스변경(전체선택관련)
    // ------------------------------------------------------------------------
    fbCheckboxChange();

  }


  // ==========================================================================
  // # 조회
  // ==========================================================================
  function fnSearch() {
    //console.log('# fnSearch Start');
    // ------------------------------------------------------------------------
    // Validation
    // ------------------------------------------------------------------------
    let startDe         = $('#startDe').val();
    let endDe           = $('#endDe').val();

    if (fnIsEmpty(startDe)) {
      fnMessage('', '<font color="#FF1A1A">[기준기간 시작일자]</font> 필수 입력입니다.', 'startDe');
      return;
    }
    if (fnIsEmpty(endDe)) {
      fnMessage('', '<font color="#FF1A1A">[기준기간 종료일자]</font> 필수 입력입니다.', 'endDe');
      return;
    }

    // 리스트 조회
    //$('#searchForm').formClear(false);
    var data;
    data = $('#searchForm').formSerialize(true);

    // ----------------------------------------------------------------------
    // 최종페이지 정보 Set (페이징 기억 관련)
    // ----------------------------------------------------------------------
    var query = {
      //page          : curPage
      //, pageSize      : PAGE_SIZE
        filterLength  : fnSearchData(data).length
      , filter        : {
                          filters : fnSearchData(data)
                        }
    };
    gridDs.query(query);
  }

  // ------------------------------- Grid Start -------------------------------
  // ==========================================================================
  // # 그리드
  // ==========================================================================
  function fnInitGrid(){

    // ------------------------------------------------------------------------
    // 그리드 호출
    // ------------------------------------------------------------------------
    gridDs = fnGetPagingDataSource({
        url : '/admin/statics/user/getUserTypeStaticsList'
      //, pageSize : PAGE_SIZE
    });

    gridOpt  = {
        dataSource  : gridDs
      , noRecordMsg : '검색된 목록이 없습니다.'
      //, pageable    : {
      //                  pageSizes   : [20, 30, 50]
      //                , buttonCount : 10
      //                }
      , navigatable : true
      , scrollable  : false
      , height      : 'auto' // '120'
      , selectable  : true
      , editable    : false
      , resizable   : true
      , autobind    : false
      , columns     : [
            { field:'gubun'        , title: '구분'            , width:120 , attributes:{ style:'text-align:center' }}
          , { title   : '기준기간'
            , columns : [
                      { field:'standardUserCount', title: '구매고객 수', width: 70 , attributes:{ style:'text-align:center'  }, format: '{0:\#\#,\#}'}
                    , { field:'standardPaidPrice', title: '매출금액', width: 70 , attributes:{ style:'text-align:center'  }, format: '{0:\#\#,\#}'}
                    , { field:'standardOrderCount', title: '주문건수', width: 70 , attributes:{ style:'text-align:center'  }, format: '{0:\#\#,\#}'}
                    , { field:'standardGoodsCount', title: '주문상품수량', width: 70 , attributes:{ style:'text-align:center'  }, format: '{0:\#\#,\#}'}
                ]
            }
          , { title   : '대비기간'
            , columns : [
                      { field:'contrastUserCount', title: '구매고객 수', width: 70 , attributes:{ style:'text-align:center'  }, format: '{0:\#\#,\#}'}
                    , { field:'contrastPaiPrice', title: '매출금액', width: 70 , attributes:{ style:'text-align:center'  }, format: '{0:\#\#,\#}'}
                    , { field:'contrastOrderCount', title: '주문건수', width: 70 , attributes:{ style:'text-align:center'  }, format: '{0:\#\#,\#}'}
                    , { field:'contrastGoodsCount', title: '주문상품수량', width: 70 , attributes:{ style:'text-align:center'  }, format: '{0:\#\#,\#}'}
                ]
            }
          ]
    };

    grid = $('#grid').initializeKendoGrid( gridOpt ).cKendoGrid();

    // ------------------------------------------------------------------------
    // dataBound
    // ------------------------------------------------------------------------
    grid.bind('dataBound', function(e) {
        $('#totalCnt').text(gridDs._total);
    });
  }

  // ------------------------------- Grid End ---------------------------------

  // ==========================================================================
  // # 콜백함수
  // ==========================================================================
  function fnBizCallback( id, data ){

    switch(id){
      case 'select' :
        // --------------------------------------------------------------------
        // 조회
        // --------------------------------------------------------------------

        break;
    }
  }

  // ==========================================================================
  // # 오류메시지처리
  // ==========================================================================
  function fnMessage(key, nullMsg, ID) {
    fnKendoMessage({
        message : fnGetLangData({ key : key, nullMsg : nullMsg})
      , ok      : function() {
                    if (ID != null && ID != '') {
                      $('#'+ID).focus();
                    }
                  }
    });
  }

    function fnGetCheckBoxText(id){
        var value = "";
        $('form[id=searchForm] :checkbox[name='+ id +']:checked').each(function() {
            value += $(this).closest('label').find('span').text() + ","
        });
        if (value == null)
            value = "";
        value = value.substring(0, value.length - 1);
        return value;
    }

  // ==========================================================================
  // # 엑셀다운로드-참여자목록/당첨자목록
  // ==========================================================================
  function fnExcelDownSaleStaticsList(gubn, keyVal) {
    //console.log('# fnExcelDownJoinList Start [', gubn, '][', keyVal, ']');

    // ------------------------------------------------------------------------
    // Validation
    // ------------------------------------------------------------------------
    let startDe         = $('#startDe').val();
    let endDe           = $('#endDe').val();

    if (fnIsEmpty(startDe)) {
      fnMessage('', '<font color="#FF1A1A">[기준기간 시작일자]</font> 필수 입력입니다.', 'startDe');
      return;
    }
    if (fnIsEmpty(endDe)) {
      fnMessage('', '<font color="#FF1A1A">[기준기간 종료일자]</font> 필수 입력입니다.', 'endDe');
      return;
    }

    // 세션 체크
    //console.log('# PG_SESSION :: ', JSON.stringify(PG_SESSION));
    if (PG_SESSION == null || PG_SESSION == '' || PG_SESSION == undefined || PG_SESSION == 'undefined') {
      fnKendoMessage({ message : "세션이 종료되었습니다. \n다시 로그인 해 주십시오.", ok : function () {
        location.href = "/admVerify.html";
      }});
      return false;
    }

    // ------------------------------------------------------------------------
    // 검색조건정보 생성
    // ------------------------------------------------------------------------
    let inputData   = $('#searchForm').formSerialize(true);
    inputData.agentTypeCdName = fnGetCheckBoxText('agentTypeCd');

    let url         = '/admin/statics/user/getUserTypeStaticsExcelDownload';
    let confirmMsg  = '엑셀파일을 다운로드 하시겠습니까?';

    //fnKendoMessage({
    //    message : fnGetLangData({key :"", nullMsg : confirmMsg })
    //  , type    : "confirm"
    //  , ok      : function() {
    //                fnExcelDownload(url, inputData);
    //              }
    //});

    // ------------------------------------------------------------------------
    // 세션체크
    // ------------------------------------------------------------------------
    fnAjax({
      url       :'/system/getSessionCheck'
    , method    : 'POST'
    //, success   : function(data, status, xhr) {
    , success   : function(data) {
                    if(data.session){
                      // ------------------------------------------------------
                      // 엑셀다운로드실행
                      // ------------------------------------------------------
                      var confirmMsg = '엑셀파일을 다운로드 하시겠습니까?';
                      //console.log('# inParam :: ', JSON.stringify(inParam));
                      //console.log('# inputData :: ', JSON.stringify(inputData));

                      fnKendoMessage({
                          message : fnGetLangData({key :"", nullMsg : confirmMsg })
                        , type    : "confirm"
                        , ok      : function() {
                                      fnExcelDownload(url, inputData);
                                    }
                        });
                    }
                    else {
                      fnKendoMessage({ message : "세션이 종료되었습니다. \n다시 로그인 해 주십시오.", ok : function () {
                        location.href = "/admVerify.html";
                      }});
                      return false;
                    }
                  }
    });

  }

  // ==========================================================================
  // ------------------------------- Html 버튼 바인딩  Start ------------------
  // ==========================================================================
  /** Common Search */
  $scope.fnBtnSearch      = function( )   { fnSearch();         };
  /** Common Clear */
  $scope.fnBtnClear       = function()    { fnClear();          };


  /** 판매현황 통계 엑셀 다운로드 */

  /** 상품별 판매현황 통계 엑셀 다운로드 */
  $scope.fnBtnExcelDownSaleStaticsList   = function() { fnExcelDownSaleStaticsList();};


}); // document ready - END
