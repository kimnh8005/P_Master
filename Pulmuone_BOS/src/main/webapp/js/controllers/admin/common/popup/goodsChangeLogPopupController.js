/**-----------------------------------------------------------------------------
 * description	 : 업데이트 상세 내역 Popup
 * @
 * @ 수정일	  수정자		  수정내용
 * @ ------------------------------------------------------
 * @ 2021.04.21	임상건		  최초생성
 * @
 * **/
'use strict';

var goodsChangeLogGirdDs, goodsChangeLogGirdOpt, goodsChangeLogGird;
var pageParam = parent.POP_PARAM["parameter"];

$(document).ready(function() {

	fnInitialize(); //Initialize Page Call ---------------------------------

	//Initialize PageR
	function fnInitialize(){
		$scope.$emit('fnIsMenu', { flag : false });

		fnPageInfo({
			PG_ID  : 'goodsChangeLogPopup',
			callback : fnUI
		});
	}

	function fnUI(){
		fnInitButton();
		fnInitGrid();
		fnInitOptionBox();
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
			filterLength	: fnSearchData(data).length,
			filter : {
				filters : fnSearchData(data)
			}
		};
		goodsChangeLogGirdDs.query( query );
	}

	function fnClose(){
		var kendoWindow =$('#kendoPopup').data('kendoWindow');
		kendoWindow.close();
	}
// --------------------------------- Button End------------------------------


// ------------------------------- Grid Start -------------------------------
	function fnInitGrid(){
		var ilGoodsId;
		var createDate;
		var columns;

		if( pageParam != undefined ){
			ilGoodsId = pageParam.ilGoodsId;
			createDate = pageParam.createDate;
		}

		goodsChangeLogGirdDs = fnGetDataSource({
			url			: "/admin/comn/popup/getGoodsChangeLogPopup?ilGoodsId="+ilGoodsId+"&createDate="+createDate
		});

		columns = [
			{ field : "columnName"		, title : "컬럼명"	, width: "10%", attributes : {style : "text-align:center;"} }
			, { field : "columnLabel"	, title : "컬럼항목명"	, width: "20%", attributes : {style : "text-align:center;"} }
			, { field : "beforeData"	, title : "변경 전"   , width: "35%", hidden : false, attributes : {style : "text-align:center;"} }
			, { field : "afterData"		, title : "변경 후"   , width: "35%", hidden : false, attributes : {style : "text-align:center;"} }
			]

		goodsChangeLogGirdOpt = {
			dataSource  : goodsChangeLogGirdDs
		  , navigatable : true
		  , scrollable  : true
		  , columns	 : columns
		  , height	  : 550
		};

		goodsChangeLogGird = $('#goodsChangeLogGird').initializeKendoGrid( goodsChangeLogGirdOpt ).cKendoGrid();

		$("#goodsChangeLogGird").on("click", "tbody>tr", function () {

		});

		goodsChangeLogGird.bind("dataBound", function(e) {
			$('#countTotalSpan').text(goodsChangeLogGirdDs._total);

			var rows = e.sender.tbody.children();
			if(row != null && row.length > 0){
				for (var j = 0; j < rows.length; j++) {
					var row = $(rows[j]);
					var dataItem = e.sender.dataItem(row);

					if ( fnFormatDate( dataItem.get("discountStartDt"), 'yyyyMMddHHmmss') <= fnGetTodayTime('yyyyMMddHHmmss') && fnFormatDate( dataItem.get("discountEndDt"), 'yyyyMMddHHmmss') >= fnGetTodayTime('yyyyMMddHHmmss') ) {
						row.css("background-color", "#D2E7A9");
					}
				}
			}
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
