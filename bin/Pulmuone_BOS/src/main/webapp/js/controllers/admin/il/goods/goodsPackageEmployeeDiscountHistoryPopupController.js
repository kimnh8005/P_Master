/**-----------------------------------------------------------------------------
 * description     : 상품할인리스트조회Popup
 * @
 * @ 수정일      수정자          수정내용
 * @ ------------------------------------------------------
 * @ 2020.9.9    안치열          최초생성
 * @
 * **/
'use strict';


var PAGE_SIZE = 3;
var goodsPackageEmployeeDiscountHistoryGridDs, goodsPackageEmployeeDiscountHistoryGridOpt, goodsPackageEmployeeDiscountHistoryGrid;
var pageParam = parent.POP_PARAM["parameter"];

$(document).ready(function() {

	fnInitialize(); //Initialize Page Call ---------------------------------

	//Initialize PageR
	function fnInitialize(){
		$scope.$emit('fnIsMenu', { flag : false });

		fnPageInfo({
			PG_ID  : 'goodsPackageEmployeeDiscountHistoryGridPopup',
			callback : fnUI
		});
	}

	function fnUI(){
		fnInitButton(); //Initialize Button  ---------------------------------
		fnInitGrid(); //Initialize Grid ------------------------------------
		fnInitOptionBox();//Initialize Option Box ------------------------------------
		fnSearch();
	}

// --------------------------------- Button Start----------------------------
	function fnInitButton(){
		//$('#fnSearch, #fnClose, #fnWarehousePopupButton').kendoButton();
	}

	function fnSearch(){
		var data;
		data = $('#searchForm').formSerialize(true);

		var query = {
			page			: 1,
			pageSize		: PAGE_SIZE,
			filterLength	: fnSearchData(data).length,
			filter :  {
				filters : fnSearchData(data)
			}
		};
		goodsPackageEmployeeDiscountHistoryGridDs.query( query );
	}

	function fnClose(){
		var kendoWindow =$('#kendoPopup').data('kendoWindow');
		kendoWindow.close();
	}
// --------------------------------- Button End------------------------------


// ------------------------------- Grid Start -------------------------------
	function fnInitGrid(){
		var ilGoodsId;
		var baseRowCount = 0;
		var discountType;
		var historyKind;

		if( pageParam != undefined ){
			ilGoodsId = pageParam.ilGoodsId;
			baseRowCount = pageParam.baseRowCount+1;
			historyKind = pageParam.historyKind
		}

		goodsPackageEmployeeDiscountHistoryGridDs = fnGetPagingDataSource({
			url : "/admin/goods/regist/goodsPackageEmployeeDiscountHistoryGridList?ilGoodsId="+ilGoodsId+"&baseRowCount="+baseRowCount+"&historyKind="+historyKind,
			pageSize : PAGE_SIZE
		});

		goodsPackageEmployeeDiscountHistoryGridOpt = {
			dataSource	: goodsPackageEmployeeDiscountHistoryGridDs
		,	pageable	: {pageSizes: [3, 6, 9], buttonCount : 10}
		,	navigatable	: true
		,	scrollable	: true
		,	height		: 550
		,	columns		: [
				{ field : "discountTypeCodeName"	, title : "구분"				, width: "5%"	, attributes : {style : "text-align:center;"} }
			,	{ field : "approvalStatusCodeName"	, title : "상태"				, width: "5%"	, attributes : {style : "text-align:center;"} }
			,	{ field : "discountStartDateTime"	, title : "시작일"				, width: "8%"	, attributes : {style : "text-align:center;"} }
			,	{ field : "discountEndDateTime"		, title : "종료일"				, width: "8%"	, attributes : {style : "text-align:center;"} }
			,	{ field : "apprReqInfo"				, title : "승인요청자"			, width: "8%"	, attributes : {style : "text-align:center;"} }
			,	{ field : "apprInfo"				, title : "승인관리자"			, width: "8%"	, attributes : {style : "text-align:center;"} }
			,	{ field : "goodsName"				, title : "상품명"				, width: "15%"	, attributes : {style : "text-align:center;"} }
			,	{ field : "recommendedPrice"		, title : "정상가"				, width: "10%"	, attributes : {style : "text-align:center;"} ,format: "{0:n0}" }
			,	{ field : "goodsQuantity"			, title : "구성수량"			, width: "7%"	, attributes : {style : "text-align:center;"} ,format: "{0:n0}" }
			,	{ field : "recommendedTotalPrice"	, title : "정상가총액"			, width: "10%"	, attributes : {style : "text-align:center;"} ,format: "{0:n0}" }
			,	{ field : "discountRatio"			, title : "할인율"				, width: "6%"	, attributes : {style : "text-align:center;"} ,format: "{0:n0}" }
			,	{ field : "discountSalePrice"		, title : "임직원할인가"		, width: "10%"	, attributes : {style : "text-align:center;"} ,format: "{0:n0}" }
			]
		,	rowTemplate : kendo.template($("#rowTemplate").html())
		};

		goodsPackageEmployeeDiscountHistoryGrid = $('#goodsPackageEmployeeDiscountHistoryGrid').initializeKendoGrid( goodsPackageEmployeeDiscountHistoryGridOpt ).cKendoGrid();

		$("#goodsPackageEmployeeDiscountHistoryGrid").on("click", "tbody>tr", function () {
			//fnGridClick();
		});

		goodsPackageEmployeeDiscountHistoryGrid.bind("dataBound", function() {
			$('#countTotalSpan').text(goodsPackageEmployeeDiscountHistoryGridDs._total);
		});
	}
// -------------------------------  Grid End  -------------------------------

// ---------------Initialize Option Box Start -------------------------------
	function fnInitOptionBox(){
		$('#kendoPopup').kendoWindow({
			visible: false,
			modal: true
		});
	}
// ---------------Initialize Option Box End ---------------------------------

// -------------------------------  Common Function start -------------------

// -------------------------------  Common Function end ---------------------


// ------------------------------- Html 버튼 바인딩  Start -----------------------
	/** Common Search*/
	$scope.fnSearch = function( ) { fnSearch(); };

	/** Common Close*/
	$scope.fnClose = function( ){  fnClose();};
// ------------------------------- Html 버튼 바인딩  End -------------------------

}); // document ready - END
