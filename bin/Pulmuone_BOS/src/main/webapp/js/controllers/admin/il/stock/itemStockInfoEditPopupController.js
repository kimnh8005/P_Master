﻿/**--------------------------------------------------------
 * description 		 : 품목별 재고리스트
 * @
 * @ 수정일			수정자          수정내용
 * @ ------------------------------------------------------
 * @ 2020.11.23		이성준          최초생성
 * @
 * **/
'use strict';

var pageParam = parent.POP_PARAM["parameter"];

var ilItemWarehouseId = pageParam.ilItemWarehouseId;
var ilItemCd = '';
var itemNm = '';
var warehouseNm = '';

var stockOperationForm = null;		//재고운영 형태 Text 값

$(document).ready(function() {
	fnInitialize();	//Initialize Page Call ---------------------------------

	//Initialize PageR
	function fnInitialize(){
		$scope.$emit('fnIsMenu', { flag : false });

		fnPageInfo({
			PG_ID  : 'itemStockInfoEditPopup',
			callback : fnUI
		});

	}

	function fnUI(){
		fnInitButton();	//Initialize Button  ---------------------------------
		fnInitOptionBox();
	}

	//--------------------------------- Button Start---------------------------------
	function fnInitButton(){
		$('#fnConfirm, #fnSave').kendoButton();
	}

	function fnConfirm(){
		fnKendoMessage({
			message: ilItemCd + ' ' + itemNm +'<br/>' + warehouseNm + ' 출고처의 폐기 임박 상품을 제외한 모든 상품의 재고운영 형태가 변경됩니다.<br/>진행하시겠습니까?',
			type : "confirm" ,ok : function(){
				fnSave();
			}
		});
	}

	function fnSave(){
		var url  = '/admin/goods/stock/putStockPreOrder';
		var cbId= 'update';
		var paramData = {}

		paramData.popPreOrderTp = $('#popPreOrderTp').val();
		paramData.ilItemWarehouseId = ilItemWarehouseId;
		paramData.stockOperationForm = $('#popPreOrderTp').data('kendoDropDownList').text();

		if( paramData ){
			fnAjax({
				url     : url,
				params  : paramData,
				success :
					function( data ){
						parent.POP_PARAM = paramData

						fnKendoMessage({
							message : '수정되었습니다.',
							ok : function() {
								parent.LAYER_POPUP_OBJECT.data("kendoWindow").close();
							}
						});
					},
					isAction : 'update'
			});
		}
	}

	//--------------------------------- Button End---------------------------------

	//---------------Initialize Option Box Start ------------------------------------------------
	function fnInitOptionBox(){
		var url  = '/admin/goods/stock/getStockPreOrderPopupInfo';
		var cbId = 'selectPopupInfo';
		var paramData = {}

		paramData.ilItemWarehouseId = ilItemWarehouseId;

		fnAjax({
			url     : url,
			params  : paramData,
			success :
				function( data ){
					fnBizCallback(cbId, data);
				},
				isAction : 'select'
		});
	}

	//---------------Initialize Option Box End ------------------------------------------------

	//-------------------------------  Common Function start ----------------------------------
	/**
	* 콜백합수
	*/
	function fnBizCallback( id, data ){
		switch(id){
			case 'selectPopupInfo':
				var preOrderTpData = [];

				preOrderTpData.push({"code": data.defaultPreOrderType	,"name": data.preOrderNm});
				preOrderTpData.push({"code": "PRE_ORDER_TP.NOT_ALLOWED"	,"name": "한정재고"});

				fnKendoDropDownList({
					id : 'popPreOrderTp',
					data : preOrderTpData,
					valueField : 'code',
					textField : 'name'
				});
				$('#popSupplierNm').val(data.supplierNm);
				$('#popPreOrderTp').data('kendoDropDownList').value(data.preOrderType);
				ilItemCd = data.ilItemCd
				itemNm = data.itemNm
				warehouseNm = data.warehouseNm
				break;
		}
	}
	//-------------------------------  Common Function end -------------------------------

	//------------------------------- Html 버튼 바인딩  Start -------------------------------
	/** Common Confirm*/
	$scope.fnConfirm = function(){ fnConfirm(); };
	/** Common Save*/
	$scope.fnSave = function(){	 fnSave();};
	/** Common Close*/
	$scope.fnClose = function( ){  fnClose();};
	//------------------------------- Html 버튼 바인딩  End -------------------------------

}); // document ready - END
