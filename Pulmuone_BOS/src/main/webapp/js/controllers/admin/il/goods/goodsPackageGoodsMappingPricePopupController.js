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
var goodsPackageGoodsMappingPriceGridDs, goodsPackageGoodsMappingPriceGridOpt, goodsPackageGoodsMappingPriceGrid;
var pageParam = parent.POP_PARAM["parameter"];

$(document).ready(function() {

	fnInitialize(); //Initialize Page Call ---------------------------------

	//Initialize PageR
	function fnInitialize(){
		$scope.$emit('fnIsMenu', { flag : false });

		fnPageInfo({
			PG_ID  : 'goodsPackageGoodsMappingPriceGridPopup',
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
		goodsPackageGoodsMappingPriceGridDs.query( query );
	}

	function fnClose(){
		var kendoWindow =$('#kendoPopup').data('kendoWindow');
		kendoWindow.close();
	}
// --------------------------------- Button End------------------------------


// ------------------------------- Grid Start -------------------------------
	function fnInitGrid(){
		var ilGoodsPriceId, ilGoodsDiscountId

		if( pageParam != undefined ){
			ilGoodsPriceId = pageParam.ilGoodsPriceId;
			ilGoodsDiscountId = pageParam.ilGoodsDiscountId;
		}

		if(ilGoodsPriceId == undefined) {
			ilGoodsPriceId = "";
		}

		if(ilGoodsDiscountId == undefined) {
			ilGoodsDiscountId = "";
		}

		goodsPackageGoodsMappingPriceGridDs = fnGetPagingDataSource({
			url : "/admin/goods/regist/goodsPackageGoodsMappingPrice?ilGoodsPriceId="+ilGoodsPriceId+"&ilGoodsDiscountId="+ilGoodsDiscountId
		});

		goodsPackageGoodsMappingPriceGridOpt = {
			dataSource	: goodsPackageGoodsMappingPriceGridDs
		,	navigatable: true
		,	scrollable: false
		,	selectable: false
		,	height		: 350
		,	columns		: [
				{ field : "goodsId"					, title : "상품코드"				, width: "5%"	, attributes : {style : "text-align:center;"} }
			,	{ field : "goodsTypeName"			, title : "상품유형"				, width: "5%"	, attributes : {style : "text-align:center;"} }
			,	{ field : "goodsName"				, title : "상품명"					, width: "27%"	, attributes : {style : "text-align:center;"} }
			,	{ field : "taxName"					, title : "과세구분"				, width: "5%"	, attributes : {style : "text-align:center;"} }
			,	{ field : "goodsQuantity"			, title : "구성<BR>수량"			, width: "5%"	, attributes : {style : "text-align:center;"} }
			,	{ field : "recommendedTotalPrice"	, title : "정상가총액"				, width: "12%"	, attributes : {style : "text-align:right;"} ,format: "{0:n0}" }
			,	{ field : "discountTotalPrice"		, title : "할인총액"				, width: "12%"	, attributes : {style : "text-align:right;"} ,format: "{0:n0}" }
			,	{ field : "discountUnitSalePrice"	, title : "묶음상품<BR>판매단가"	, width: "12%"	, attributes : {style : "text-align:right;"} }
			,	{ field : "discountSalePrice"		, title : "묶음상품<BR>판매가"		, width: "12%"	, attributes : {style : "text-align:right;"} ,format: "{0:n0}" }
			,	{ field : "discountRatio"			, title : "묶음상품<BR>할인율"		, width: "5%"	, attributes : {style : "text-align:center;"} ,format: "{0:n0}" }
			]
		,	rowTemplate : kendo.template($("#rowTemplate").html())
		};

		goodsPackageGoodsMappingPriceGrid = $('#goodsPackageGoodsMappingPriceGrid').initializeKendoGrid( goodsPackageGoodsMappingPriceGridOpt ).cKendoGrid();

		$("#goodsPackageGoodsMappingPriceGrid").on("click", "tbody>tr", function () {
			//fnGridClick();
		});

		goodsPackageGoodsMappingPriceGrid.bind("dataBound", function() {
			//$('#countTotalSpan').text(goodsPackageGoodsMappingPriceGridDs._total);
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
