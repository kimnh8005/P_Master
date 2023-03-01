/**-----------------------------------------------------------------------------
 * description 		 : 정산관리 > 대사관리 > 통합몰 매출 대사 내역
 * @
 * @ 수정일			수정자          수정내용
 * @ ------------------------------------------------------
 * @ 2021.03.05		이명수          최초생성
 * **/
"use strict";

var PAGE_SIZE = 20;

var PAGE_SIZE = 20;
var viewModel, defaultGridDs, defaultGridOpt, defaultGrid;
var pageParam = fnGetPageParam();

var defaultGridUrl = "/admin/calculate/collation/getOutmallDetlList";

// 판매처그룹.직관리 (외부몰)
var sellersGroupDirectMng = "SELLERS_GROUP.DIRECT_MNG";
var sellersGroupVendor		= "SELLERS_GROUP.VENDOR";
$(document).ready(function() {

	importScript("/js/service/od/order/orderGridColumns.js", function (){
		importScript("/js/service/od/order/orderCommSearch.js", function (){
//			fnInitialize();	//Initialize Page Call ---------------------------------
		});
	});
	importScript("/js/service/ca/collation/calCollationGridColumns.js");
	importScript("/js/service/ca/collation/calCollationSearch.js");
	importScript("/js/service/ca/collation/calCollationSubmitUtil.js");
	importScript("/js/service/ca/collation/calCollationFunction.js");

	fnInitialize();	//Initialize Page Call ---------------------------------

	//Initialize PageR
	function fnInitialize(){
		$scope.$emit("fnIsMenu", { flag : true });
		fnPageInfo({
			PG_ID  : "calOutmallDetl",
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
		calCollationGridEventUtil.gridInit(defaultGridUrl, calCollationGridUtil.calOutmallDetlList());
	};

	//-------------------------------  Grid End  -------------------------------

	//---------------Initialize Option Box Start ------------------------------------------------

	// 옵션 초기화
	function fnInitOptionBox(){

		// 기간조회
		calCollationSearchUtil.dateSearchType(calCollationOptionUtil.dateSearchPgDetl())
		calCollationSearchUtil.dateSearch();

		 // 판매처그룹
		searchCommonUtil.getDropDownCommCd("sellersGroup", "NAME", "CODE", "판매처 그룹 전체", "SELLERS_GROUP", "Y", "", "", "");

		// 매칭 여부
		calCollationSearchUtil.getRadioLocalData(calCollationOptionUtil.outmallMatchingType);

		// 검색어 조회
		calCollationSearchUtil.searchTypeKeywordOutmallDetl();
		calCollationSearchUtil.setFindKeyword();


		$("input[name=sellersGroup]").each(function() {
			$(this).bind("change", onPurchaseTargetType);
		});

        fbCheckboxChange();
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
	$scope.fnSearch = function( ) {	calCollationSubmitUtil.search();	};

	/** Common Clear*/
	$scope.fnClear =function(){	 calCollationSubmitUtil.clear();	};

	/** 확정완료 */
	$scope.fnExcelDownload = function() {
		var url = "/admin/calculate/collation/getOutmallDetlExcelList";
		calCollationSubmitUtil.excelDownload(url);
	};
	//------------------------------- Html 버튼 바인딩  End -------------------------------

	//------------------------------- Validation Start -----------------------------------

	//------------------------------- Validation End -------------------------------------
}); // document ready - END
