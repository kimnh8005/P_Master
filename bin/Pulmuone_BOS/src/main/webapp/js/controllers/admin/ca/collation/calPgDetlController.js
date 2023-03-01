/**-----------------------------------------------------------------------------
 * description 		 : 정산관리 > 대사관리 > PG 대사 상세내역
 * @
 * @ 수정일			수정자          수정내용
 * @ ------------------------------------------------------
 * @ 2021.04.28		이원호          최초생성
 * **/
"use strict";

var PAGE_SIZE = 20;
var viewModel, defaultGridDs, defaultGridOpt, defaultGrid;
var pageParam = fnGetPageParam();

$(document).ready(function() {

	importScript("/js/service/ca/collation/calCollationGridColumns.js");
	importScript("/js/service/ca/collation/calCollationSearch.js");
	importScript("/js/service/ca/collation/calCollationSubmitUtil.js");
	importScript("/js/service/ca/collation/calCollationFunction.js");


	fnInitialize();	//Initialize Page Call ---------------------------------

	//Initialize PageR
	function fnInitialize(){
		$scope.$emit("fnIsMenu", { flag : true });
		fnPageInfo({
			PG_ID  : "calSales",
			callback : fnUI
		});
	};

	// 화면 UI 초기화
	function fnUI(){

		fnTranslate();	// 다국어 변환
		fnInitButton();
		fnInitOptionBox();
		fnViewModelInit();
		fnDefaultSet();
		fnInitGrid();

		// fnSearch();
		//orderSubmitUtil.search();
	};

	//--------------------------------- Button Start---------------------------------
	// 버튼 초기화
	function fnInitButton(){
		$("#fnSearch,  #fnClear").kendoButton();
	};
	//--------------------------------- Button End-----------------------------------

	//------------------------------- Grid Start -------------------------------
	// 그리드 초기화
	function fnInitGrid(){
		defaultGridDs = fnGetPagingDataSource({
            url      : "/admin/calculate/collation/getPgDetlList",
            pageSize : PAGE_SIZE,
            requestEnd : function(e){
                $("#emSumPrice").text(e.response.data.totalAmt);
            }
        });
        defaultGridOpt = {
            dataSource: defaultGridDs
            ,  pageable  : {
                pageSizes: [20, 30, 50],
                buttonCount : 10
            }
            ,navigatable: true
            ,columns   : calCollationGridUtil.calPgDetlList()
        };

        defaultGrid = $('#defaultGrid').initializeKendoGrid( defaultGridOpt ).cKendoGrid();

		defaultGrid.bind("dataBound", function() {
		    var row_num = defaultGridDs._total - ((defaultGridDs._page - 1) * defaultGridDs._pageSize);
            $("#defaultGrid tbody > tr .row-number").each(function(index){
                $(this).html(row_num);
                row_num--;
            });

            $('#totalCnt').text(defaultGridDs._total);
        });

        //지연건만 보기
        $('#ng-view').on("click", "#summaryList" ,function(index){
            if($("#summaryList").prop("checked")==true){
                $('#summaryListView').val('Y');
                calCollationSubmitUtil.search();
            }else{
                $('#summaryListView').val('N');
                calCollationSubmitUtil.search();
            }
        });
	};

	//-------------------------------  Grid End  -------------------------------

	//---------------Initialize Option Box Start ------------------------------------------------

	// 옵션 초기화
	function fnInitOptionBox(){

		// 기간조회
		calCollationSearchUtil.dateSearchType(calCollationOptionUtil.dateSearchPgDetl())
		calCollationSearchUtil.dateSearch();

		// 구분
		calCollationSearchUtil.getCheckBoxLocalData(calCollationOptionUtil.optSalesOrderGubun);

		// PG 구분
		calCollationSearchUtil.getDropDownCommCd("findPgGubn", "NAME", "CODE", "전체", "PG_SERVICE", "Y", "", "", "");

		// 매칭 여부
		calCollationSearchUtil.getRadioLocalData(calCollationOptionUtil.optPgDetlMatchingType);

		// 결제수단
		calCollationSearchUtil.getCheckBoxCommUrl(calCollationOptionUtil.paymentMethod);

		// 검색어 조회
		calCollationSearchUtil.searchTypeKeywordPgDetl();
		calCollationSearchUtil.setFindKeyword();

        fbCheckboxChange(); //[공통] checkBox
	};

	//---------------Initialize Option Box End ------------------------------------------------
	//-------------------------------  Common Function start ----------------------------------

	// viewModel 초기화
	function fnViewModelInit(){};

	// 기본값 설정
	function fnDefaultSet(){};

	//-------------------------------  Common Function end -------------------------------

	//------------------------------- Html 버튼 바인딩  Start -----------------------------
	/** Common Search*/
	$scope.fnSearch = function( ) {
	    if($('#summaryListView').val() == '' || $('#summaryListView').val() == null){
	        $('#summaryListView').val('N');
	    }
	    calCollationSubmitUtil.search();
	};

	/** Common Clear*/
	$scope.fnClear =function(){	 calCollationSubmitUtil.clear();	};

	/** 확정완료 */
	$scope.fnExcelDownload = function() {
		var data = $('#searchForm').formSerialize(true);
        fnExcelDownload('/admin/calculate/collation/getPgDetlListExportExcel', data);
	};
	//------------------------------- Html 버튼 바인딩  End -------------------------------

	//------------------------------- Validation Start -----------------------------------

	//------------------------------- Validation End -------------------------------------
}); // document ready - END
