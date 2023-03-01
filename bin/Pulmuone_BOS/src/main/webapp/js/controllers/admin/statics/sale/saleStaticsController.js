/*******************************************************************************
 * -----------------------------------------------------------------------------
 * description : 통계관리 매출통계
 * @
 * @
 * @ 수정일        수정자        수정내용
 * @ ---------------------------------------------------------------------------
 * @ 2021.03.24    dgyoun        최초생성
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
    // 그리드 합산정보
    $('#totalInfo').text('');
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
    // 공급업체 [콤보]
    // ------------------------------------------------------------------------
    fnKendoDropDownList({
        id          : 'urSupplierId'
      , tagId       : 'urSupplierId'
      , url         : '/admin/comn/getDropDownSupplierList'
      //, params      : {'stCommonCodeMasterCode' : '', 'useYn' : 'Y'}
      , async       : false
      , isDupUrl    : 'Y'
      , textField   : 'supplierName'
      , valueField  : 'supplierId'
      , style       : {}
      , blank       : '공급업체 전체'
    });

    // ------------------------------------------------------------------------
    // 판매처그룹 [콤보]
    // ------------------------------------------------------------------------
    fnKendoDropDownList({
        id        : 'sellersGroupCd'
      , tagId     : 'sellersGroupCd'
      , url       : '/admin/comn/getCodeList'
      , params    : { 'stCommonCodeMasterCode' : 'SELLERS_GROUP', 'useYn' : 'Y' }
      , blank     : '판매처 그룹 전체'
      , async     : false
      , isDupUrl  : 'Y'
    });

    // ------------------------------------------------------------------------
    // 판매처ID [콤보]
    // ------------------------------------------------------------------------
    fnKendoDropDownList({
        id          : 'omSellersId'
      , tagId       : 'omSellersId'
      , url         : '/admin/comn/getDropDownSellersGroupBySellersList'
      , params      : { 'sellersGroupCd' : '' }
      , textField   : 'sellersNm'
      , valueField  : 'omSellersId'
      , blank       : '판매처 전체'
    });

    // ------------------------------------------------------------------------
    // 판매채널유형 [체크] : 전체/PC/MOBILE/APP/관리자주문/외부몰
    // ------------------------------------------------------------------------
    fnTagMkChkBox({
        id          : 'agentTypeCd'
      , tagId       : 'agentTypeCd'
      , url         : '/admin/comn/getCodeList'
      , params      : {'stCommonCodeMasterCode' : 'AGENT_TYPE', 'useYn' : 'Y'}
      , async       : false
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

    // ------------------------------------------------------------------------
    // 판매처그룹 선택 이벤트 : bind 확인
    // ------------------------------------------------------------------------
    $('#sellersGroupCd').on('change', function (e) {
      //console.log('# 판매처 그룹 변경');
      fnSellerGroupChange(e);
    });

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

  // ==========================================================================
  // # 판매처그룹 변경 이벤트 처리
  // ==========================================================================
  function fnSellerGroupChange() {
    //console.log('# fnSellerGroupChange Start');
    let sellersGroupCd = $('#sellersGroupCd').val();

    fnAjax({
        method    : 'GET'
      , url       : '/admin/comn/getDropDownSellersGroupBySellersList'
      , params    : { 'sellersGroupCd' : sellersGroupCd }
      , success   : function( data ){
                      let sellerDetail = $('#omSellersId').data('kendoDropDownList');
                      sellerDetail.setDataSource(data.rows);
                    }
      , error     : function(xhr, status, strError){
                      fnKendoMessage({ message : xhr.responseText });
                    }
      , isAction  : 'select'
      });
  };


  // ------------------------------- Grid Start -------------------------------
  // ==========================================================================
  // # 그리드
  // ==========================================================================
  function fnInitGrid(){

    // ------------------------------------------------------------------------
    // 그리드 호출
    // ------------------------------------------------------------------------
    gridDs = fnGetPagingDataSource({
        url : '/admin/statics/sale/selectSaleStaticsList'
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
                        { field:'supplierNm'        , title: '공급업체'            , width:120 , attributes:{ style:'text-align:left' }}
                      , { field:'sellersGroupCdNm'  , title: '판매처그룹'          , width: 60 , attributes:{ style:'text-align:left' }}
                      , { field:'sellersNm'         , title: '판매처'              , width: 80 , attributes:{ style:'text-align:left' }}

                      , { title   : '기준기간'
                        , columns : [
                                      { field:'standardPaidPrice' , title: '매출금액(VAT포함)'            , width: 70 , attributes:{ style:'text-align:right'  }, format: '{0:\#\#,\#}'}
                                    , { field:'standardPaidPriceNonTax'  , title: '매출금액(VAT별도)'            , width: 70 , attributes:{ style:'text-align:right'  }, format: '{0:\#\#,\#}'}
                                    , { field:'standardOrderCnt'  , title: '주문건수'            , width: 50 , attributes:{ style:'text-align:right'  }, format: '{0:\#\#,\#}'}
                                    , { field:'standardGoodsCnt'  , title: '주문상품수량'        , width: 50 , attributes:{ style:'text-align:right'  }, format: '{0:\#\#,\#}'}
                                    ]
                        }
                      , { title   : '대비기간'
                        , columns : [
                                      { field:'contrastPaidPrice' , title: '매출금액(VAT포함)'            , width: 70 , attributes:{ style:'text-align:right'  }, format: '{0:\#\#,\#}'}
                                    , { field:'contrastPaidPriceNonTax'  , title: '매출금액(VAT별도)'            , width: 70 , attributes:{ style:'text-align:right'  }, format: '{0:\#\#,\#}'}
                                    , { field:'contrastOrderCnt'  , title: '주문건수'            , width: 50 , attributes:{ style:'text-align:right'  }, format: '{0:\#\#,\#}'}
                                    , { field:'contrastGoodsCnt'  , title: '주문상품수량'        , width: 50 , attributes:{ style:'text-align:right'  }, format: '{0:\#\#,\#}'}
                                    ]
                        }
                      , { field:'stretchRate'       , title: '매출금액(VAT포함)<br/>신장율' , width: 70 , attributes:{ style:'text-align:center' }, format: '{0:\#\#,\#}'
                                                                                               , template : function(dataItem) {
                                                                                                             return String(dataItem.stretchRate).replace(/(\d)(?=(?:\d{3})+(?!\d))/g, '$1,') + '%';
                                                                                                            }
                        }
                      ]
    };

    grid = $('#grid').initializeKendoGrid( gridOpt ).cKendoGrid();

    // ------------------------------------------------------------------------
    // dataBound
    // ------------------------------------------------------------------------
    grid.bind('dataBound', function(e) {
      // ----------------------------------------------------------------------
      // NO 항목 및 전체건수 Set
      // ----------------------------------------------------------------------
      //var row_num = gridDs._total - ((gridDs._page - 1) * gridDs._pageSize);
      //let row_num = 1;    // 순번
      //$('#grid tbody > tr .row-number').each(function(index){
      //  $(this).html(row_num);
      //  row_num++;
      //});

      // ----------------------------------------------------------------------
      // 총판매금액 합산 금액
      // ----------------------------------------------------------------------
      let totalStandardPaidPrice  = 0;  // 그리드상단 합산금액
      let totalStandardPaidPriceNonTax  = 0;  // 그리드상단 합산금액
      let targetGrid    = $('#grid').data('kendoGrid');
      let targetGridDs  = targetGrid.dataSource;
      let targetGridArr = targetGridDs.data();

      //for (var i = 0; i < targetGridArr.length ; i++) {
      //  totalStandardPaidPrice     += targetGridArr[i].standardPaidPrice;
      //}
      // 마지막 row가 합산임
      totalStandardPaidPrice = targetGridArr[targetGridArr.length-1].standardPaidPrice
      totalStandardPaidPriceNonTax = targetGridArr[targetGridArr.length-1].standardPaidPriceNonTax

      // ----------------------------------------------------------------------
      // 그리드상단 합산 정보
      // ----------------------------------------------------------------------
      //console.log('# totalStandardPaidPrice    :: ', totalStandardPaidPrice);
      let totalStandardPaidPriceStr = String(totalStandardPaidPrice);
      let totalStandardPaidPriceStrComma = totalStandardPaidPriceStr.replace(/(\d)(?=(?:\d{3})+(?!\d))/g, '$1,');
      let totalStandardPaidPriceNonTaxStr = String(totalStandardPaidPriceNonTax);
      let totalStandardPaidPriceNonTaxStrComma = totalStandardPaidPriceNonTaxStr.replace(/(\d)(?=(?:\d{3})+(?!\d))/g, '$1,');
      let vatSumYnStr = '';
      // if($('input:checkbox[name=vatSumYn]').is(':checked') == true) {
      //   vatSumYnStr = '(VAT포함)';
      // }
      // else {
      //   vatSumYnStr = '(VAT별도)';
      // }
      let totalInfoStr = ' | ' + '매출금액 합산 : ' + totalStandardPaidPriceStrComma + '원 (VAT포함) / ' + totalStandardPaidPriceNonTaxStrComma + '원 (VAT별도)';
      $('#totalInfo').text(totalInfoStr);

      // ----------------------------------------------------------------------
      // 전체건수 Set
      // ----------------------------------------------------------------------
      $('#totalCnt').text(gridDs._total-1);

      // ----------------------------------------------------------------------
      // rowspan
      // ----------------------------------------------------------------------
      mergeGridRows(
          // div 로 지정한 그리드 ID
          'grid'
          // 그리드에서 셀 머지할 컬럼들의 data-field 목록
        , [
            'supplierNm'
          , 'sellersGroupCdNm'
          ]
          // group by 할 컬럼들의 data-field 목록
        , [ 'supplierNm']
      );

    });


  }

  // ==========================================================================
  // # Kendo Grid 전용 rowSpan 메서드
  //  - @param gridId : div 로 지정한 그리드 ID, 해당 div 내 table 태그를 찾아감
  //  - @param mergeColumns : 그리드에서 셀 머지할 컬럼들의 data-field 목록
  //  - @param groupByColumns : group by 할 컬럼들의 data-field 목록, 해당 group 내에서만 셀 머지가 일어남
  // ==========================================================================
  function mergeGridRows(gridId, mergeColumns, groupByColumns) {
    // 데이터 1건 이하인 경우 : rowSpan 불필요하므로 return
    if( $('#' + gridId + ' > table > tbody > tr').length <= 1 ) {
      return;
    }


    var groupByColumnIndexArray = [];   // group by 할 컬럼들의 th 헤더 내 column index 목록
    var tdArray = [];                   // 해당 컬럼의 모든 td 배열, 개수 / 순서는 그리드 내 tr 개수 / 순서와 같음
    var groupNoArray = [];              // 파라미터로 전달된 groupByColumns 에 따라 계산된 그룹번호 배열, 같은 그룹인 경우 그룹번호 같음, 개수 / 순서는 tdArray 와 같음

    var groupNo;            // 각 tr 별 그룹번호, 같은 그룹인 경우 그룹번호 같음
    var beforeTr = null;    // 이전 tr
    var beforeTd = null;    // 이전 td
    var rowspan = null;     // rowspan 할 개수, 1 인경우 rowspan 하지 않음

    // 해당 그리드의 th 헤더 row
    var thRow = $('#' + gridId + ' > table > thead > tr')[0];

    // 셀 머지시 group by 할 컬럼들의 data-field 목록이 Array 형태의 파라미터로 전달시
    if( groupByColumns && Array.isArray(groupByColumns) && groupByColumns.length > 0 ) {

      $(thRow).children('th').each(function (thIndex, th) {  // thead 의 th 반복문 시작 : table 내 각 컬럼별로 반복
        // groupByColumns => groupByColumnIndexArray 로 변환
        if( groupByColumns.includes( $(th).attr('data-field') ) ) {
          groupByColumnIndexArray.push(thIndex);
        }

      });
    } // if 문 끝

    // ------------------------------------------------------------------------
    // tbody 내 tr 반복문 시작
    // ------------------------------------------------------------------------
    $('#' + gridId + ' > table > tbody > tr').each(function() {
      beforeTr = $(this).prev();        // 이전 tr
      // 첫번째 tr 인 경우 : 이전 tr 없음
      if( beforeTr.length == 0 ) {
        groupNo = 0;                    // 그룹번호는 0 부터 시작
        groupNoArray.push(groupNo);     // 첫번째 tr 의 그룹번호 push
      }
      else {
        var sameGroupFlag = true;       // 이전 tr 과 비교하여 같은 그룹인지 여부 flag, 기본값 true

        for( var i in groupByColumnIndexArray ) {
          var groupByColumnIndex = groupByColumnIndexArray[i];  // groupByColumns 로 전달된 각 column 의 index
          // 이전 tr 과 현재 tr 비교하여 group by 기준 컬럼의 html 값이 하나라도 다른 경우 flag 값 false ( 같은 그룹 아님 )
          if( $(this).children().eq(groupByColumnIndex).html() != $(beforeTr).children().eq(groupByColumnIndex).html() ) {
            sameGroupFlag = false;
          }
        }

        // 이전 tr 의 값과 비교하여 같은 그룹이 아닌 경우 : groupNo 1 증가시킴
        if( ! sameGroupFlag ) {
          groupNo++;
        }

        groupNoArray.push(groupNo); // 해당 tr 의 그룹번호 push
      }

    });
    // tbody 내 tr 반복문 끝
    // ------------------------------------------------------------------------

    // ------------------------------------------------------------------------
    // thead 의 th 반복문 시작 : table 내 각 컬럼별로 반복
    // ------------------------------------------------------------------------
    $(thRow).children('th').each(function (thIndex, th) {
      if( ! mergeColumns.includes( $(th).attr('data-field') ) ) {
        return true;    // mergeColumns 에 포함되지 않은 컬럼인 경우 continue
      }

      tdArray = [];  // 값 초기화
      beforeTd = null;
      rowspan = null;

      var colIdx = $("th", thRow).index(this);  // 해당 컬럼 index

      $('#' + gridId + ' > table > tbody > tr').each(function() {  // tbody 내 tr 반복문 시작
        var td = $(this).children().eq(colIdx);
        tdArray.push(td);
      });  // tbody 내 tr 반복문 끝

      // ----------------------------------------------------------------------
      // 해당 컬럼의 td 배열 반복문 시작
      // ----------------------------------------------------------------------
      for( var i in tdArray ) {
        var td = tdArray[i];

        if ( i > 0 && groupNoArray[i-1] == groupNoArray[i] && $(td).html() == $(beforeTd).html() ) {
          rowspan = $(beforeTd).attr("rowSpan");

          if ( rowspan == null || rowspan == undefined ) {
            $(beforeTd).attr("rowSpan", 1);
            rowspan = $(beforeTd).attr("rowSpan");
          }

          rowspan = Number(rowspan) + 1;

          $(beforeTd).attr("rowSpan",rowspan);
          $(td).hide(); // .remove(); // do your action for the old cell here
        }
        else {
          beforeTd = td;
        }

        beforeTd = ( beforeTd == null || beforeTd == undefined ) ? td : beforeTd; // set the that if not already set
      }
      // 해당 컬럼의 td 배열 반복문 끝
      // ----------------------------------------------------------------------
    });
    // thead 의 th 반복문 끝
    // ------------------------------------------------------------------------

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

  // ==========================================================================
  // # 조회조건 정보 문자열 생성
  // ==========================================================================
  function fnGenConditionInfo() {

    let infoStr = '';
    let tmpStr  = '';

    // ------------------------------------------------------------------------
    // 검색기준 : 라디오
    // ------------------------------------------------------------------------
    $('input[name="searchTp"]:checked').each(function() {
      //var value = $(this).val();
      var text = $(this).closest('label').find('span').text();
      tmpStr = text;
    });
    if (fnIsEmpty(tmpStr) != true) {
      // 값이 존재하면
      tmpStr = '검색기준: ' + tmpStr;
    }
    infoStr += tmpStr;

    // ------------------------------------------------------------------------
    // VAT포함여부 : 체크박스(단일)
    // ------------------------------------------------------------------------
    // tmpStr = '';
    // if($('input:checkbox[name=vatSumYn]').is(':checked') == true) {
    //   tmpStr = 'VAT포함';
    // }
    // else {
    //   tmpStr = 'VAT미포함';
    // }
    // if (fnIsEmpty(infoStr) == true) {
    //   infoStr += tmpStr;
    // }
    // else {
    //   infoStr += ' / ' + tmpStr;
    // }

    // ------------------------------------------------------------------------
    // 기준기간
    // ------------------------------------------------------------------------
    tmpStr = '';
    tmpStr = '기준기간: ' + $('#startDe').val() + '~' + $('#endDe').val();
    if (fnIsEmpty(infoStr) == true) {
      infoStr += tmpStr;
    }
    else {
      infoStr += ' / ' + tmpStr;
    }

    // ------------------------------------------------------------------------
    // 대비기간
    // ------------------------------------------------------------------------

    tmpStr = '';
    tmpStr =  $('#contrastStartDe').val() + '~' + $('#contrastEndDe').val();
    if (fnIsEmpty($('#contrastStartDe').val()) != true || fnIsEmpty($('#contrastEndDe').val()) != true) {
         tmpStr = '대비기간: ' + tmpStr;
         if (fnIsEmpty(infoStr) == true) {
           infoStr += tmpStr;
         }
         else {
           infoStr += ' / ' + tmpStr;
         }
    }

    // ------------------------------------------------------------------------
    // 공급업체
    // ------------------------------------------------------------------------
    tmpStr = '';
    tmpStr = '공급업체: ' + $('#urSupplierId').data('kendoDropDownList').text();
    if (fnIsEmpty(infoStr) == true) {
      infoStr += tmpStr;
    }
    else {
      infoStr += ' / ' + tmpStr;
    }

    // ------------------------------------------------------------------------
    // 판매처그룹
    // ------------------------------------------------------------------------
    tmpStr = '';
    tmpStr = '판매처그룹: ' + $('#sellersGroupCd').data('kendoDropDownList').text();
    if (fnIsEmpty(infoStr) == true) {
      infoStr += tmpStr;
    }
    else {
      infoStr += ' / ' + tmpStr;
    }

    // ------------------------------------------------------------------------
    // 판매처
    // ------------------------------------------------------------------------
    tmpStr = '';
    tmpStr = '판매처: ' + $('#omSellersId').data('kendoDropDownList').text();
    if (fnIsEmpty(infoStr) == true) {
      infoStr += tmpStr;
    }
    else {
      infoStr += ' / ' + tmpStr;
    }

    // ------------------------------------------------------------------------
    // 판매채널유형
    // ------------------------------------------------------------------------
    tmpStr = '';
    let i = 0;
    $('input[name="agentTypeCd"]:checked').each(function() {
      var id = $(this).val();
      var text = $(this).closest('label').find('span').text();
      if (id == 'ALL') {
        tmpStr = text;
        return false;
      }
      else {
        if (i == 0) {
          tmpStr += text;
        }
        else {
          tmpStr += ',' + text;
        }
      }
      i++;
    });
    if (fnIsEmpty(tmpStr) == true) {
      tmpStr = '판매처유형: 전체';
    }
    else {
      tmpStr = '판매처유형: ' + tmpStr
    }
    if (fnIsEmpty(infoStr) == true) {
      infoStr += tmpStr;
    }
    else {
      infoStr += ' / ' + tmpStr;
    }
    //console.log('# 검색조건 :: ', infoStr);
    // hidden에 Set
    $('#searchInfo').val(infoStr);
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
    fnGenConditionInfo();

    let inputData   = $('#searchForm').formSerialize(true);
    //console.log('# inputData :: ', JSON.stringify(inputData));
    let url         = '/admin/statics/sale/getExportExcelSaleStaticsList';
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
