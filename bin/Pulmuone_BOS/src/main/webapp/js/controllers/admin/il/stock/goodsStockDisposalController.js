/**-----------------------------------------------------------------------------
 * description 		 : 통합ERP 재고 연동 내역 관리

 * @
 * @ 수정일			수정자          수정내용
 * @ ------------------------------------------------------
 * @ 2020.07.08		박영후          최초생성
 * @ 2020.12.09		이성준          기능추가
 * **/
'use strict';

var aGridDs, aGridOpt, aGrid;
var PAGE_SIZE = 20;

$(document).ready(function() {
    fnInitialize();	//Initialize Page Call ---------------------------------

    //Initialize PageR
    function fnInitialize() {
        $scope.$emit('fnIsMenu', {flag: 'true'});

        fnPageInfo({
            PG_ID: 'goodsStockDisposal',
            callback: fnUI
        });

    }

    function fnUI(){

        fnTranslate();	// 다국어 변환--------------------------------------------

        fnInitGrid();	//Initialize Grid ------------------------------------

        fnInitButton();	//Initialize Button  ---------------------------------

        fnInitOptionBox();//Initialize Option Box ------------------------------------

        setDefaultDatePicker();

    }

    //--------------------------------- Button Start---------------------------------

    function fnInitButton(){
        $('#fnSearch, #fnClear, #fnExcelDown').kendoButton();
    }

    function fnSearch(){

        if (fnValidationCheck() == false) {
            fnInitGrid();
            return;
        }

        let _pageSize = aGrid && aGrid.dataSource && aGrid.dataSource.pageSize() > 0 ? aGrid.dataSource.pageSize() : PAGE_SIZE;
        let data = $('#searchForm').formSerialize(true);

        var query = {
            page         : 1,
            pageSize     : _pageSize,
            filterLength : fnSearchData(data).length,
            filter :  {
                filters : fnSearchData(data)
            }
        };

        aGridDs.query( query );
    }

    function fnValidationCheck() {

        if ($('#urWarehouseId').val() == "") {
            valueCheck("출고처 구분은 필수 선택 입니다.", "urWarehouseId");
            return false;
        }

        return true;
    }

    /*
     * 메시지 팝업 호출 함수
     */
    function valueCheck(nullMsg, id) {
        fnKendoMessage({
            message : nullMsg,
            ok : function focusValue() {
                var $obj = $('#' + id);
                if($obj.data("kendoDropDownList")){
                    $obj.data("kendoDropDownList").focus();
                } else if ($obj.data("kendoNumericTextBox")){
                    $obj.data("kendoNumericTextBox").focus();
                } else {
                    $('#' + id).focus();
                }
            }
        });

        return false;
    };

    function fnClear() {
        $('#searchForm').formClear(true);
        $('[data-id="fnDateBtn1"]').mousedown();
        $('#urSupplierId').data('kendoDropDownList').select('0');
        $('#urWarehouseId').data('kendoDropDownList').select('0');
        $('#stockExprTp > label > input:eq(2)').trigger('click');
        $('#stockExprTp > label > input:eq(3)').trigger('click');
        $("#searchBaseDt").data("kendoDatePicker").max(fnGetToday());
    }

    //엑셀다운로드
    function fnExcelDown(){
        var data = $('#searchForm').formSerialize(true);
        fnExcelDownload('/admin/goods/stock/getGoodsStockDisposalExcelList', data);
    }

    function fnMergeGrid() {
        $('#aGrid > table > tbody > tr').each(function () {
            let cTd = $(this).children();
            if (cTd == null || cTd.length == 1)
                return;

            let cId = cTd[2].textContent;
            let pEl = $('#aGrid > table > tbody > tr').filter(function() {
                return this["status"] == "normal"
            }).last();

            $(this).prop("status", "normal");
            if (pEl == null || pEl == undefined)
                return;

            let pTd = pEl.children();
            if (pTd.length == 0)
                return;

            let pId = pTd[2].textContent;
            if (pId == undefined && cId == undefined)
                return;

            if (cId === pId) {
                let rowCnt = $(pTd[1]).attr('rowspan');
                rowCnt = rowCnt == undefined ? 2 : Number(rowCnt) + 1;

                $(cTd[0]).remove();
                $(cTd[1]).remove();
                $(cTd[2]).remove();
                $(cTd[3]).remove();
                $(cTd[4]).remove();
                $(cTd[5]).remove();

                $(pTd[0]).attr('rowspan', rowCnt);
                $(pTd[1]).attr('rowspan', rowCnt);
                $(pTd[2]).attr('rowspan', rowCnt);
                $(pTd[3]).attr('rowspan', rowCnt);
                $(pTd[4]).attr('rowspan', rowCnt);
                $(pTd[5]).attr('rowspan', rowCnt);

                $(this).prop("status", "merged");
            }
        });
    }

    function setDefaultDatePicker() {
        $(".date-controller button").each(function() {
            $(this).attr("fb-btn-active", false);
        })

        $('#kendoDummyDtDiv').css('display', 'none')
        $("button[data-id='fnDateBtn3']").remove();
        $("button[data-id='fnDateBtn4']").remove();
        $("button[data-id='fnDateBtn5']").remove();
        $("button[data-id='fnDateBtn6']").remove();
        $("button[data-id='fnDateBtn7']").remove();

        var today = fnGetToday();

        $("#searchBaseDt").data("kendoDatePicker").value(today);
    }

    function fnGetToday( fmt ){
        if( fmt == undefined ) fmt = 'yyyy-MM-dd';
        return new Date().oFormat( fmt );
    }

    // 체크 박스 전체 선택
    function onCheckboxWithTotalChange(e) {   // 체크박스 change event

        // 첫번째 체크박스가 '전체' 체크박스라 가정함
        var totalCheckedValue = $("input[name=" + e.target.name + "]:eq(0)").attr('value');

        if (e.target.value == totalCheckedValue) {  // '전체' 체크 or 체크 해제시
            if ($("input[name=" + e.target.name + "]:eq(0)").is(":checked")) {  // '전체' 체크시
                $("input[name=" + e.target.name + "]:gt(0)").each(function (idx, element) {
                    $(element).prop('checked', true);  // 나머지 모두 체크
                });
            } else { // '전체' 체크 해제시
                $("input[name=" + e.target.name + "]:gt(0)").each(function (idx, element) {
                    $(element).prop('checked', false);  // 나머지 모두 체크 해제
                });
            }

        } else { // 나머지 체크 박스 중 하나를 체크 or 체크 해재시

            var allChecked = true; // 나머지 모두 체크 상태인지 flag

            $("input[name=" + e.target.name + "]:gt(0)").each(function (idx, element) {
                if ($(element).prop('checked') == false) {
                    allChecked = false;  // 하나라도 체크 해제된 상태가 있는 경우 flag 값 false
                }
            });

            if (allChecked) { // 나머지 모두 체크 상태인 경우
                $("input[name=" + e.target.name + "]:eq(0)").prop('checked', true);  // 나머지 모두 '전체' 체크
            } else {
                $("input[name=" + e.target.name + "]:eq(0)").prop('checked', false);  // 나머지 모두 '전체' 체크 해제
            }
        }
    }

    //--------------------------------- Button End---------------------------------
    //------------------------------- Grid Start -------------------------------
    function fnInitGrid(){
        aGridDs = fnGetPagingDataSource({
            url      : '/admin/goods/stock/getGoodsStockDisposalList',
            pageSize : PAGE_SIZE
        });
        aGridOpt = {
            dataSource: aGridDs
            ,pageable  : {
                pageSizes: [20, 30, 50],
                buttonCount : 5
            }
            ,navigatable: true
            ,columns   : [
                { title : 'No.'			,width:'50px'	,attributes:{ style:'text-align:center' }, template: function(dataItem) {return "<span class='row-number'>" + dataItem.ranking + "</span>"}}
                , { title : '품목정보',
                    columns: [
                        { field:'supplierName' ,title : '공급업체'		      , width:'90px'	,attributes:{ style:'text-align:center' }},
                        { field:'ilItemInfo' , title : '품목코드<br/>/품목바코드', width:'90px', class:'a', name:'a'	,attributes:{ style:'text-align:center'}
                            , template : function(dataItem) {
                                var templateString = dataItem.ilItemCd + "<br/>/" + dataItem.itemBarcode;
                                return templateString;
                            }},
                        { field:'ilGoodsInfo' ,title : '상품유형<br/>상품코드'		      , width:'90px'	,attributes:{ style:'text-align:center' }
                            , template : function(dataItem) {
                                let gtp = dataItem.goodsTp == null ? "" : dataItem.goodsTp;
                                let igi = dataItem.ilGoodsId == null ? "" : dataItem.ilGoodsId;
                                return gtp + "<br/>" + igi;
                            }},
                        { field:'saleStatus' ,title : '상품<br/>판매상태'		      , width:'90px'	,attributes:{ style:'text-align:center' }}
                        , { field:'disposalGoodsId' ,title : '폐기임박상품<br/>코드'		      , width:'90px'	,attributes:{ style:'text-align:center' }}
                        , { field:'disposalSaleStatus' ,title : '폐기임박상품<br/>판매상태'		  , width:'90px'	,attributes:{ style:'text-align:center' }}
                        , { field:'itemNm' ,title : '마스터품목명'		      , width:'90px'	,attributes:{ style:'text-align:center' }}
                    ]}
              , { title : '유통기한정보',
                  columns: [
                      { field:'stockTp' ,title : '재고구분'		      , width:'90px'	,attributes:{ style:'text-align:center' }
                      , template : function(dataItem) {
                          if (dataItem.stockTp == "폐기") {
                              return "<span style='color:red'>" + dataItem.stockTp + "</span>";
                          } else if (dataItem.stockTp == "임박") {
                              return "<span style='color:blue'>" + dataItem.stockTp + "</span>";
                          } else {
                              return dataItem.stockTp;
                          }
                          }},
                      { field:'expirationDt' ,title : '유통기한'		      , width:'90px'	,attributes:{ style:'text-align:center' }},
                      { field:'leftDays' ,title : '유통기한<br/>잔여일'		      , width:'90px'	,attributes:{ style:'text-align:center' }},
                      { field:'disposalLeftDays' ,title : '폐기예정<br/>전환잔여일'		      , width:'90px'	,attributes:{ style:'text-align:center' }
                          , template : function(dataItem) {

                              var disposalLeftDays = stringUtil.getString(dataItem.disposalLeftDays, "");
                              if (disposalLeftDays != "") {
                                  disposalLeftDays = disposalLeftDays.replaceAll("+", "");
                                  disposalLeftDays = disposalLeftDays.replaceAll("-", "");
                              }
                              if (dataItem.stockTp == "폐기") {
                                  return "<span style='color:red'>D+" + disposalLeftDays + "</span>";
                              } else if (dataItem.stockTp == "임박") {
                                  return "<span style='color:blue'>D-" + disposalLeftDays + "</span>";
                              } else {
                                  return "<span style='color:black'>D-" + disposalLeftDays + "</span>";
                              }
                          }},
                      { field:'stockQty' ,title : '수량'		      , width:'90px'	,attributes:{ style:'text-align:center' }}
                ]}
            ]
        };

        aGrid = $('#aGrid').initializeKendoGrid( aGridOpt ).cKendoGrid();

        aGrid.bind("dataBound", function() {
            //kendogrid row 병합
            fnMergeGrid();

            $('#baseTimestamp').text($('#searchBaseDt').val());
            if ($('#urWarehouseId').data("kendoDropDownList").text().trim() != "") {

                $('#supplierName').text($('#urWarehouseId').data("kendoDropDownList").text());
            }

            $('#countTotalSpan').text(aGridDs._total);

        });
    }

    //-------------------------------  Grid End  -------------------------------

    //---------------Initialize Option Box Start ------------------------------------------------
    function fnInitOptionBox(){

        // 출고처
        fnKendoDropDownList({
            id    : 'urWarehouseId',
            url : "/admin/ur/urCompany/getWarehouseList",
            params : {"stockOrderYn": "Y"},
            tagId : 'urWarehouseId',
            chkVal: '',
            style : {},
            blank : "선택",
            textField :"warehouseName",
            valueField : "urWarehouseId"
        });

        fnKendoDropDownList({
            id    : 'urSupplierId',
            url : "/admin/ur/urCompany/getSupplierCompanyListByWhareHouse",
            tagId : 'urSupplierId',
            textField :"supplierName",
            valueField : "urSupplierId",
            cscdId     : "urWarehouseId",
            cscdField  : "urWarehouseId",
            blank : "전체"
        });

        //달력
        fnKendoDatePicker({
            id    : 'searchBaseDt',
            format: 'yyyy-MM-dd',
            max : fnGetToday(),
            defVal : fnGetToday(),
            btnStyle : true,
            btnStartId: 'searchBaseDt',
            btnEndId: 'kendoDummyDt',
            change : function() {
                $(".date-controller button").each(function() {
                    $(this).attr("fb-btn-active", false);
                });
            }
        });
        fnKendoDatePicker({
            id    : 'kendoDummyDt',
            format: 'yyyy-MM-dd',
            max : fnGetToday(),
            defVal : fnGetToday(),
            btnStartId: 'searchBaseDt',
            btnEndId: 'kendoDummyDt',
            defType : 'yesterday',
            change : function() {
                $(".date-controller button").each(function() {
                    $(this).attr("fb-btn-active", false);
                });
            }
        });

        //판매/전시 > 판매 상태
        fnTagMkChkBox({
            id : 'stockExprTp',
            url : "/admin/comn/getCodeList",
            tagId : 'stockExprTp',
            async : false,
            style : {},
            beforeData  : [{ "CODE" : "STOCK_EXPR_TP.ALL", "NAME" : "전체" }],
            params : {"stCommonCodeMasterCode" : "STOCK_EXPR_TP", "useYn" :"Y"},
            change : function(e) {
            }
        });

        // ------------------------------------------------------------------------
        // 검색기간 초기화 버튼 클릭
        // ------------------------------------------------------------------------
        $('[data-id="fnDateBtn1"]').on('click', function(){
            $("#searchBaseDt").data("kendoDatePicker").max(fnGetToday());
        });

        $('[data-id="fnDateBtn2"]').on('click', function(){
            $("#searchBaseDt").data("kendoDatePicker").max(fnGetToday());
        });

        $('[data-id="fnDateBtnC"]').on('click', function(){
            $('[data-id="fnDateBtn1"]').mousedown();
            $("#searchBaseDt").data("kendoDatePicker").max(fnGetToday());
        });

        $('#stockExprTp').bind("change", onCheckboxWithTotalChange);
        $('#stockExprTp > label > input:eq(2)').trigger('click');
        $('#stockExprTp > label > input:eq(3)').trigger('click');
    }
    //---------------Initialize Option Box End ------------------------------------------------
    //-------------------------------  Common Function start -------------------------------

    //------------------------------- Html 버튼 바인딩  Start -------------------------------
    /** Common Search*/
    $scope.fnSearch = function( ) {	fnSearch();	};
    /** Common Clear*/
    $scope.fnClear =function(){	 fnClear();	};
    /** Common ExcelDown*/
    $scope.fnExcelDown = function(){ fnExcelDown();};
    //------------------------------- Html 버튼 바인딩  End -------------------------------

});