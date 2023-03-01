/**-----------------------------------------------------------------------------
 * description 		 : 주문/배송관리 > 증정품 내역 팝업
 * @
 * @ 수정일			수정자          수정내용
 * @ ------------------------------------------------------
 * @ 2021.03.19		천혜현          최초생성
 * @
 * **/
"use strict";

var orderGiftListGridDs, orderGiftListGridOpt, orderGiftListGrid;
var pageParam = fnGetPageParam();
var paramData = parent.POP_PARAM['parameter'];

$(document).ready(function() {
	// importScript("/js/service/od/claim/claimComm.js");
	importScript("/js/service/od/order/orderMgmGridColumns.js");

	fnInitialize();	//Initialize Page Call ---------------------------------

	//Initialize PageR
	function fnInitialize(){
		$scope.$emit("fnIsMenu", { flag : false });
		fnPageInfo({
			PG_ID  : "orderGiftListPopup",
			callback : fnUI
		});
	};

	// 화면 UI 초기화
	function fnUI(){
		fnInitGrid();
	};

	//------------------------------- Grid Start -------------------------------
	function fnInitGrid(){
		orderGiftListGridDs = fnGetDataSource({
			url      : "/admin/order/getOrderGiftList?odOrderId="+paramData.odOrderId,
		});

		orderGiftListGridOpt = {
			dataSource: orderGiftListGridDs,
			navigatable : true,
			scrollable : true,
			columns : orderMgmGridUtil.orderGiftList(),
		};

		orderGiftListGrid = $('#orderGiftListGrid').initializeKendoGrid( orderGiftListGridOpt ).cKendoGrid();
		orderGiftListGridDs.query();
	};

	//-------------------------------  Grid End  -------------------------------

}); // document ready - END
