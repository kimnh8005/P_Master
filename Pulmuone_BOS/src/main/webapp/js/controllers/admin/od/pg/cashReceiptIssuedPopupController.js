/**-----------------------------------------------------------------------------
 * description 		 : 주문/배송관리 > 증빙문서 관리 > 현금영수증 발급 팝업
 * @
 * @ 수정일			수정자          수정내용
 * @ ------------------------------------------------------
 * @ 2020.12.07		김승우          최초생성
 * **/
"use strict";

var param = parent.POP_PARAM.parameter;
var parent = window.parent;

$(document).ready(function() {
	importScript("/js/service/od/pg/pgComm.js", function (){
		fnInitialize();
	});

	//Initialize PageR
	function fnInitialize(){
		$scope.$emit("fnIsMenu", { flag : false });
		fnPageInfo({
			PG_ID  : "cashReceiptIssuedPopup",
			callback : fnUI,
		});
	};

	// 화면 UI 초기화
	function fnUI(){

		fnDefaultSet();
		fnInitButton();
		fnInitOptionBox();
	};

	//--------------------------------- Button Start---------------------------------
	// 버튼 초기화
	function fnInitButton(){
		$("#fnSave").kendoButton();
	};
	//--------------------------------- Button End-----------------------------------

	//---------------Initialize Option Box Start ------------------------------------------------

	// 옵션 초기화
	function fnInitOptionBox(){
		fnKendoDropDownList({
			id : "receiptIssueNoType",
			data : [
				{"CODE" : "mobile"		, "NAME" : "휴대폰번호"},
				{"CODE" : "businessNo"	, "NAME" : "사업자번호"},
			],
			valueField : "CODE",
			textField : "NAME"
		});

		fnTagMkRadio({
			id    : 'receiptType',
			data  : [
				{ "CODE" : "CASH_RECEIPT.DEDUCTION"	, "NAME":"소득공제용"},
				{ "CODE" : "CASH_RECEIPT.PROOF" 	, "NAME":"지출증빙용"}
			],
			tagId : 'receiptType',
			chkVal : 'CASH_RECEIPT.DEDUCTION',
			change : function(e){
				var dropdownlist = $("#receiptIssueNoType").data("kendoDropDownList");

				if($("#receiptType").getRadioVal() == "CASH_RECEIPT.DEDUCTION"){
					dropdownlist.select(dropdownlist.ul.children().eq(0));
				}else{
					dropdownlist.select(dropdownlist.ul.children().eq(1));
				}
			}
		});

  };

	//---------------Initialize Option Box End ------------------------------------------------
	//-------------------------------  Common Function start ----------------------------------

    // viewModel 초기화
    function fnViewModelInit(){};

    // 기본값 설정
	function fnDefaultSet(){
		var paymentPrice = fnNumberWithCommas(param.paymentPrice);
		$(".paymentPrice").text(paymentPrice);

	};
		
	// 저장
	function fnSave() {
		const $form = $("#saveForm");
		const formData = $form.formSerialize(true);

		if( formData.rtnValid ){

			formData.taxablePrice = param.taxablePrice;
			formData.odOrderId = param.odOrderId;
			formData.paymentPrice = param.paymentPrice;
			formData.pgService = param.pgService;
			formData.guestCi = param.guestCi;
			formData.urUserId = param.urUserId;
			formData.odPaymentMasterId = param.odPaymentMasterId;

			fnAjax({
				url     : '/admin/order/document/cashReceiptIssue',
				params  : formData,
				success :
					function( data ){

						if(data.code == '0000'){
							fnKendoMessage({message : fnGetLangData({nullMsg :'저장되었습니다.' }),ok:function(e){
									const windowObj = parent.LAYER_POPUP_OBJECT;
									windowObj.data("kendoWindow").close();
								}});
						}else{
							fnKendoMessage({message : '현금영수증 발급이 실패했습니다.<br> 사유 :: ' + data.data.message});
						}
					},
				isAction : 'batch'
			});
		}
	}


	//-------------------------------  Common Function end -------------------------------

	//------------------------------- Html 버튼 바인딩  Start -----------------------------
	/** Common Save*/
	$scope.fnSave = function() { fnSave(); };

	//------------------------------- Html 버튼 바인딩  End -------------------------------

	//------------------------------- Validation Start -----------------------------------

	fnInputValidationForNumber("regNumber");

	//------------------------------- Validation End -------------------------------------
}); // document ready - END
