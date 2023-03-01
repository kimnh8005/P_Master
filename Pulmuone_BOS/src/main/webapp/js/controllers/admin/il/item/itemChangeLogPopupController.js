/**-----------------------------------------------------------------------------
 * description	 : 업데이트 상세 내역 Popup
 * @
 * @ 수정일	  수정자		  수정내용
 * @ ------------------------------------------------------
 * @ 2021.04.21	임상건		  최초생성
 * @
 * **/
'use strict';

var itemChangeLogGirdDs, itemChangeLogGirdOpt, itemChangeLogGird;
var pageParam = parent.POP_PARAM["parameter"];

$(document).ready(function() {

	fnInitialize(); //Initialize Page Call ---------------------------------

	//Initialize PageR
	function fnInitialize(){
		$scope.$emit('fnIsMenu', { flag : false });

		fnPageInfo({
			PG_ID  : 'itemChangeLogPopup',
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
		itemChangeLogGirdDs.query( query );
	}

	function fnClose(){
		var kendoWindow =$('#kendoPopup').data('kendoWindow');
		kendoWindow.close();
	}
// --------------------------------- Button End------------------------------


// ------------------------------- Grid Start -------------------------------
	function fnInitGrid(){
		var ilItemCode;
		var createDate;
		var columns;

		if( pageParam != undefined ){
			ilItemCode = pageParam.ilItemCode;
			createDate = pageParam.createDate;
		}

		itemChangeLogGirdDs = fnGetDataSource({
			url			: "/admin/item/master/getItemChangeLogPopup?ilItemCode="+ilItemCode+"&createDate="+createDate
		});

		columns = [
			{ field : "columnName"		, title : "컬럼명"	, width: "10%", attributes : {style : "text-align:center;"} }
			, { field : "columnLabel"	, title : "컬럼항목명"	, width: "20%", attributes : {style : "text-align:center;"} }
			, { field : "beforeData"	, title : "변경 전"   , width: "35%", hidden : false, attributes : {style : "text-align:center;"} }
			, { field : "afterData"		, title : "변경 후"   , width: "35%", hidden : false, attributes : {style : "text-align:center;"} }
			]

		itemChangeLogGirdOpt = {
			dataSource  : itemChangeLogGirdDs
		  , navigatable : true
		  , scrollable  : true
		  , columns	 : columns
		  , height	  : 550
		};

		itemChangeLogGird = $('#itemChangeLogGird').initializeKendoGrid( itemChangeLogGirdOpt ).cKendoGrid();

		$("#itemChangeLogGird").on("click", "tbody>tr", function () {

		});

		itemChangeLogGird.bind("dataBound", function(e) {
			$('#countTotalSpan').text(itemChangeLogGirdDs._total);

			var rows = e.sender.tbody.children();
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
