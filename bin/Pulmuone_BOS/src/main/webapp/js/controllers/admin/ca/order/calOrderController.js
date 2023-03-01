/**-----------------------------------------------------------------------------
 * description 		 : 정산관리 > 주문정산 > 주문 정산 내역
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

var defaultGridUrl = "/admin/calculate/order/getOrderList";

// 판매처그룹.직관리 (외부몰)
var sellersGroupDirectMng = "SELLERS_GROUP.DIRECT_MNG";
var sellersGroupVendor		= "SELLERS_GROUP.VENDOR";
$(document).ready(function() {

	importScript("/js/service/ca/order/calOrderGridColumns.js", function (){
		importScript("/js/service/ca/order/calOrderSearch.js", function (){
			importScript("/js/service/ca/order/calOrderSubmitUtil.js", function (){
				importScript("/js/service/ca/order/calOrderFunction.js", function (){
					fnInitialize();
				});
			});
		});
	});

	//Initialize PageR
	function fnInitialize(){
		$scope.$emit("fnIsMenu", { flag : true });
		fnPageInfo({
			PG_ID  : "calEmployeeUse",
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
		calOrderGridEventUtil.gridInit(defaultGridUrl, calOrderGridUtil.calOrderList());

	};

	//-------------------------------  Grid End  -------------------------------

	//---------------Initialize Option Box Start ------------------------------------------------

	// 옵션 초기화
	function fnInitOptionBox(){

		// 기간조회
		calOrderSearchUtil.dateSearchType(calOrderOptionUtil.dateSearchOrderData())
		calOrderSearchUtil.dateSearch();

		// 날짜 초기화 이벤트 변경
		calOrderSearchUtil.fnInitFirstDateAndLastDate();
		$('[data-id="fnDateBtnC"]').off("mousedown").on("mousedown", function (){
			calOrderSearchUtil.fnInitFirstDateAndLastDate();
		});

		// 판매처
		calOrderSearchUtil.getDropDownCommCd("sellersGroup", "NAME", "CODE", "판매처 그룹 전체", "SELLERS_GROUP", "Y", "", "", "");

		// 구분
		calOrderSearchUtil.getCheckBoxLocalData(calOrderOptionUtil.optSalesOrderGubun);

		//주문자유형
		calOrderSearchUtil.getCheckBoxCommUrl(calOrderOptionUtil.buyerType);

		// 상품유형
		calOrderSearchUtil.getCheckBoxCommUrl(calOrderOptionUtil.goodsType);

		// 결제수단
		calOrderSearchUtil.getCheckBoxCommUrl(calOrderOptionUtil.paymentMethod);

		//searchCommonUtil.getCheckBoxCommCd("discountTypeCode", "DISCOUNT_TYPE", "Y");	//할인유형
		//searchCommonUtil.dateSearch(); //defVal, defType 사용 필요

		//매장명 그룹
		const UR_STORE_ID = "urStoreId";
		searchCommonUtil.getDropDownUrl(UR_STORE_ID, UR_STORE_ID, "/admin/ur/urCompany/getStoreList", "storeName", "urStoreId", "전체", "", "", "");

		$("input[name=sellersGroup]").each(function() {
			$(this).bind("change", onPurchaseTargetType);
		});

		fbCheckboxChange(); //[공통] checkBox

	};




	//---------------Initialize Option Box End ------------------------------------------------
	//-------------------------------  Common Function start ----------------------------------

	// viewModel 초기화
	function fnViewModelInit(){};

	// 기본값 설정
	function fnDefaultSet(){};

	// 엑셀 다운로드
//	function fnExcelDownload(){
//		var data = $('#searchForm').formSerialize(true);
//		fnExcelDownload('/admin/calculate/order/getOrderListExportExcel', data);
//	};

	//-------------------------------  Common Function end -------------------------------

	//------------------------------- Html 버튼 바인딩  Start -----------------------------
	/** Common Search*/
	$scope.fnSearch = function( ) {	calOrderSubmitUtil.search();	};

	/** Common Clear*/
	$scope.fnClear =function(){	 calOrderSubmitUtil.clear();	};

	/** 엑셀 다운로드*/
	$scope.fnExcelDownload =function(){
		var url = "/admin/calculate/order/getOrderListExportExcel";
		calOrderSubmitUtil.excelDownload(url);
//		fnExcelDownload();
	};

	/** 확정완료 */
//	$scope.fnExcelDownload = function() {
//		calPovSubmitUtil.excelDownload();
//	};
	//------------------------------- Html 버튼 바인딩  End -------------------------------

	//------------------------------- Validation Start -----------------------------------

	//------------------------------- Validation End -------------------------------------
}); // document ready - END
