/**-----------------------------------------------------------------------------
 * description 		 : 통합몰 정책 > 수수료 관리 > 물류 수수료 관리
 * **/
'use strict';

var PAGE_SIZE = 20;
var aGridDs, aGridOpt, aGrid;

$(document).ready(function() {
    fnInitialize();

    //Initialize PageR
    function fnInitialize(){
        $scope.$emit('fnIsMenu', { flag : 'true' });

        fnPageInfo({
            PG_ID  : 'omLogisticsFee',
            callback : fnUI
        });
    };

    // 화면 UI 초기화
    function fnUI(){

        fnInitButton(); //Initialize Button  ---------------------------------
        fnInitGrid();   //Initialize Grid ------------------------------------
        fnSearch();
        fnInitOptionBox();

    };

    //--------------------------------- Button Start---------------------------------
    // 버튼 초기화
    function fnInitButton(){
        $('#fnSearch, #fnNew, #fnSave').kendoButton();
    };

    function fnClear() {
        $("#searchForm").formClear(true);
        $("input:checkbox[name='searchCalcType']").prop('checked', true);
    };


    // 조회
    function fnSearch(){
        $('#inputForm').formClear(true);

        var data = $('#searchForm').formSerialize(true);
        var query = {
            page         : 1,
            pageSize     : PAGE_SIZE,
            filterLength : fnSearchData(data).length,
            filter :  {
                filters : fnSearchData(data)
            }
        };
        aGridDs.query( query );
    };

    //신규등록
    function fnNew(){
        fnMoveAddOrPutPopup();
    };

    function fnMoveAddOrPutPopup(params){
        if( params == undefined ){
            params = { omBasicFeeId : "",  startDt : ""};
        }

        fnKendoPopup({
            id     : 'omLogisticsFeePopup',
            title  : '물류 수수료 등록',
            src    : '#/omLogisticsFeePopup',
            param  : params,
            width  : '900px',
            height : '600px',
            success: function( id, data ){
                fnSearch();
            }
        });
    };

    //--------------------------------- Button End---------------------------------

    //------------------------------- Grid Start -------------------------------
    // 그리드 초기화
    function fnInitGrid(){
        aGridDs = fnGetPagingDataSource({
            url      : '/admin/ps/fee/getOmLogisticsFeeList',
            pageSize : PAGE_SIZE
        });

        aGridOpt = {
            dataSource: aGridDs
            ,   pageable  : {
                pageSizes: [20, 30, 50],
                buttonCount : 10
            }
            ,   navigatable: true
            ,   columns   : [
                { field : "rowNumber"				, title : "No"				, width: "20px"		    , attributes : {style : "text-align:center;"}
                    , template: function (dataItem){ return fnKendoGridPagenation(aGrid.dataSource, dataItem)}}
                , { field : "warehouseNm"		    , title : "출고지"			, width: "80px"		    , attributes : {style : "text-align:center;text-decoration: underline;color: blue;"} }
                , { field : "supplierNm"		    , title : "공급업체"			, width: "80px"		    , attributes : {style : "text-align:center;"} }
                , { field : "calcType"				, title : "정산 방식"			, width: "60px"		    , attributes : {style : "text-align:center;"}
                    , template: function(dataItem){ return dataItem.calcType == "S" ? "판매가정산" : "공급가정산" ;}}
                , { field : "fee"	                , title : "수수료"			, width: "40px"		    , attributes : {style : "text-align:right;"}
                    , template: function(dataItem){ return dataItem.fee + "%"; }}
                , { field : "startDt"			    , title : "시작일자"			, width: "60px"		    , attributes : {style : "text-align:right;"}
                	, template: function(dataItem){ return dataItem.startDt+'~' ;}}
                , { field : "createDt"			    , title : "등록일자"			, width: "100px"	    , attributes : {style : "text-align:center;"} }
            ]
        };
        aGrid = $('#aGrid').initializeKendoGrid( aGridOpt ).cKendoGrid();

        //총 건수
        aGrid.bind("dataBound", function() {
            //total count
            $('#totalCnt').text(aGridDs._total);
        });

        $("#aGrid").on("click", "tbody>tr", function () {
            var aMap = aGrid.dataItem(aGrid.select());
            fnMoveAddOrPutPopup(aMap);
        });
    };

    //-------------------------------  Grid End  -------------------------------


    function fnInitOptionBox(){

        // 검색 정산방식
    	fnTagMkChkBox({
            id    : 'searchCalcTypeFilter',
            tagId : 'searchCalcType',
            chkVal: '',
            data: [
                {CODE: "S", NAME: "판매가 정산"},
                {CODE: "B", NAME: "공급가 정산"}
            ],
            style : {},
            async : false
        });

    	$("input:checkbox[name='searchCalcType']").prop('checked', true);

        // 검색 공급업체
        searchCommonUtil.getSellersSupplier("searchUrSupplierId", "searchUrSupplierId", "전체");

        // 검색 출고처
        searchCommonUtil.getDropDownWarehouseStlmnList("searchUrWarehouseId", "searchUrWarehouseId", "출고처 전체");

    };

    //------------------------------- Html 버튼 바인딩  Start -------------------------------
    /** Common Search*/
    $scope.fnSearch = function( ) { fnSearch(); }; // 조회
    $scope.fnClear  = function() { fnClear(); }; // 초기화
    /** Common New*/
    $scope.fnNew = function( ){ fnNew();}; // 신규
    //------------------------------- Html 버튼 바인딩  End -------------------------------

}); // document ready - END