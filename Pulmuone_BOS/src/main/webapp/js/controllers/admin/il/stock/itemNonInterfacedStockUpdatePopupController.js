﻿/**-----------------------------------------------------------------------------
 * description 		 : 재고 미연동 품목리스트
 * @
 * @ 수정일			수정자          수정내용
 * @ ------------------------------------------------------
 * @ 2020.07.08		박영후          최초생성
 * @ 2020.12.10		이성준          기능추가
 * **/
'use strict';

var pageParam = parent.POP_PARAM["parameter"];
var PAGE_SIZE = 20;
var aGridDs, aGridOpt, aGrid;

var ilItemCd			= pageParam.ilItemCd;
var itemNm				= pageParam.itemNm;
var ilItemWarehouseId	= pageParam.ilItemWarehouseId;
var unlimitStockYn		= pageParam.unlimitStockYn;
var notIfStockCnt		= pageParam.notIfStockCnt;

$(document).ready(function() {
	fnInitialize();	//Initialize Page Call ---------------------------------

	//Initialize PageR
	function fnInitialize(){
		$scope.$emit('fnIsMenu', { flag : false });

		fnPageInfo({
			PG_ID  : 'itemNonInterfacedStockUpdatePopup',
			callback : fnUI
		});
	}

	function fnUI(){
		fnInitButton();	//Initialize Button  ---------------------------------
		fnInitOptionBox();//Initialize Option Box ------------------------------------
		fnInitMaskTextBox();
	}

	//--------------------------------- Button Start---------------------------------

	function fnInitButton(){
		$('#fnSave').kendoButton();
	}

	//재고수정
	function fnSave(){

		let url  = '/admin/goods/stock/putStockNonErp';
		let cbId= 'update';

		var paramData = $('#inputForm').formSerialize(true);

		if(paramData.popUnlimitStockYn == "N" && paramData.popStockCnt == '') {
			fnKendoMessage({
				message : '재고관리 수량을 입력해 주시기 바랍니다.',
				ok : function() {
					$("#popStockCnt").focus();

				}
			});

			paramData.rtnValid = false;
		}

		if(paramData.popUnlimitStockYn == "Y" && paramData.popStockCnt == ''){
			paramData.popStockCnt = 0;
			paramData.popStockCntText = "무제한";
			paramData.stockOperationForm = "재고미연동(무제한)";
		}
		else if(paramData.popUnlimitStockYn == "N" && paramData.popStockCnt != '') {
			paramData.popStockCntText = paramData.popStockCnt;
			paramData.stockOperationForm = "재고미연동(한정재고)";
		}

		if( paramData.rtnValid ){
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


	//------------------------------- Grid Start -------------------------------
	//-------------------------------  Grid End  -------------------------------

	//---------------Initialize Option Box Start -------------------------------
	function fnInitMaskTextBox() {
		$('#popStockCnt').forbizMaskTextBox({fn:'onlyNum'});
	}

	function fnInitOptionBox(){
		// 재고관리여부(재고운영형태랑 같은것임)
		fnTagMkRadio({
			id    : 'popUnlimitStockYn',
			tagId : 'popUnlimitStockYn',
			chkVal: '',
			data  : [
						{ "CODE" : "Y" , "NAME":'무제한' },
						{ "CODE" : "N" , "NAME":'재고관리'}
					],
			style : {}
		});

		$('#popIlItemCd').val(ilItemCd);
		$('#popItemName').val(itemNm);
		$('#popIlItemWarehouseId').val(ilItemWarehouseId);

		if(unlimitStockYn == 'Y'){
			$("input:radio[name='popUnlimitStockYn']:radio[value='Y']").prop('checked', true);
			$('#popStockCnt').attr('disabled', true);
		}else{
			$("input:radio[name='popUnlimitStockYn']:radio[value='N']").prop('checked', true);
			$('#popStockCnt').attr('disabled', false);
			$('#popStockCnt').val(notIfStockCnt);
		}

		$('#popUnlimitStockYn').bind("change", onRadioChange);
	}
	//---------------Initialize Option Box End ------------------------------------------------

	//-------------------------------  Common Function start -------------------------------
	//라디오 버튼 change event
	function onRadioChange(e){
		if(e.target.value == 'Y'){
			$('#popStockCnt').val('');
			$('#popStockCnt').attr('disabled', true);
		}else{
			$('#popStockCnt').attr('disabled', false);
		}
	}
	//-------------------------------  Common Function end -------------------------------


	//------------------------------- Html 버튼 바인딩  Start -------------------------------
	/** Common Save*/
	$scope.fnSave = function(){	 fnSave();};

	/** Common Close*/
	$scope.fnClose = function( ){  fnClose();};
	//------------------------------- Html 버튼 바인딩  End -------------------------------

}); // document ready - END
