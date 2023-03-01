/**-----------------------------------------------------------------------------
 * @
 * @ 수정일			수정자          수정내용
 * @ ------------------------------------------------------
 * @ 2020.09.02		박승현          최초생성
 * @
 * **/
'use strict';

$(document).ready(function() {
	fnInitialize();	//Initialize Page Call ---------------------------------

	//Initialize PageR
	function fnInitialize(){
		$scope.$emit('fnIsMenu', { flag : 'true' });

		fnPageInfo({
			PG_ID  : 'policyRegularShipping',
			callback : fnUI
		});

	}

	function fnUI(){

		fnTranslate();	// 다국어 변환--------------------------------------------

		fnInitButton();	//Initialize Button  ---------------------------------

		fnInitOptionBox();

	}

	//--------------------------------- Button Start---------------------------------
	function fnInitButton(){
		$('#fnBack, #fnSave, #fnClear').kendoButton();
	}
	function fnBack() {
		history.back();
	}
	function fnClear(){
		//$('#regularShippingSettingInputForm').formClear(true);
		fnInitOptionBox();
	}
	function fnSave(){
		let paramData = $("#policyRegularShippingInputForm").formSerialize(true);
        if( paramData.rtnValid && fnInputValidation() ){
        	fnAjax({
                 url     : "/admin/policy/regularshipping/putPolicyRegularShipping",
                 params  : paramData,
                 success : function( successData ){
                     fnKendoMessage({message : "저장이 완료되었습니다."});
                 },
                 isAction : "insert"
             });
        }
	}

	// 입력값 검증
	function fnInputValidation(){
        if( $("#regularShippingPaymentFailTerminate").val().length < 1 ){
            fnKendoMessage({ message : "실패 회차 수 값 오류",
                             ok : function(e) { $("#regularShippingPaymentFailTerminate").focus(); }
            });
            return false;
        }
        $("#regularShippingPaymentFailTerminate").val(Number($("#regularShippingPaymentFailTerminate").val()));
	    return true;
	};

	function fnDel(){

	}
	function fnClose(){

	}

	//--------------------------------- Button End---------------------------------


	//------------------------------- Grid Start -------------------------------
	function fnInitGrid(){

	}
	function fnGridClick(){

	};
	//Initialize End End
	function fnEditCustm(e){
		return;
	}
	//-------------------------------  Grid End  -------------------------------

	//---------------Initialize Option Box Start ------------------------------------------------
	function fnInitOptionBox(){
		function fnDefaultSearch(){
			fnAjax({
				url     : '/admin/policy/regularshipping/getPolicyRegularShipping',
				params	: {"ST_SHOP_ID" :'1'},
				success :
					function( data, resultcode ){
						if (data.RETURN_CODE == '0000' && data.RETURN_MSG != '' && data.RETURN_MSG != null ) {
							fnKendoMessage({
					            message : fnGetLangData({
					                key : data.RETURN_CODE,
					                nullMsg : data.RETURN_MSG
					            })
					        });
					    }
						let regularShippingBasicDiscountRate = data.regularShippingBasicDiscountRate || "5";
						let regularShippingAdditionalDiscountApplicationTimes = data.regularShippingAdditionalDiscountApplicationTimes || "3";
						let regularShippingAdditionalDiscountRate = data.regularShippingAdditionalDiscountRate || "5";
						let regularShippingMaxCustomerCycle = data.regularShippingMaxCustomerCycle || "4";
						let regularShippingPaymentFailTerminate = data.regularShippingPaymentFailTerminate + "" || "1";
						$("#regularShippingBasicDiscountRate").data("kendoDropDownList").value(regularShippingBasicDiscountRate);
						$("#regularShippingAdditionalDiscountApplicationTimes").data("kendoDropDownList").value(regularShippingAdditionalDiscountApplicationTimes);
						$("#regularShippingAdditionalDiscountRate").data("kendoDropDownList").value(regularShippingAdditionalDiscountRate);
						$("#regularShippingMaxCustomerCycle").data("kendoDropDownList").value(regularShippingMaxCustomerCycle);
						$("#regularShippingPaymentFailTerminate").val(regularShippingPaymentFailTerminate);
					}
			});
		}
		fnDefaultSearch();
		var basicDscntRt = [];
		var addDscntApplctnTm = [];
		var addDscntRt = [];
		var maxCstmrCcl = [];

		for (var i = 1; i < 100; i++) {
			var rate = i;
			basicDscntRt.push({"NAME":rate, "CODE":rate});
		}
		for (var i = 1; i <= 30; i++) {
			var rate = i;
			addDscntApplctnTm.push({"NAME":rate, "CODE":rate});
		}
		for (var i = 1; i <= 10; i++) {
			var rate = i;
			addDscntRt.push({"NAME":rate, "CODE":rate});
		}
		for (var i = 1; i <= 4; i++) {
			var rate = i;
			maxCstmrCcl.push({"NAME":rate, "CODE":rate});
		}

		fnKendoDropDownList({ id : 'regularShippingBasicDiscountRate'	, data : basicDscntRt});
		fnKendoDropDownList({ id : 'regularShippingAdditionalDiscountApplicationTimes'	, data : addDscntApplctnTm});
		fnKendoDropDownList({ id : 'regularShippingAdditionalDiscountRate'		, data : addDscntRt});
		fnKendoDropDownList({ id : 'regularShippingMaxCustomerCycle'		, data : maxCstmrCcl});

		fnInputValidationForNumber("regularShippingPaymentFailTerminate");
		/*
		fnKendoDropDownList({
			id    : 'poDeadlineHour',
			tagId : 'poDeadlineHour',
			blank : "선택"
		}).setDataSource(poDeadlineHourDatas);

		// 팝업 - 발주마감시간(분)
		var poDeadlineMinDatas = [];
		for (var i = 0; i < 60; i++) {
			var min = i;
			if (i < 10)
				min = "0" + i;
			poDeadlineMinDatas.push({"NAME":min, "CODE":min});
		}

		fnKendoDropDownList({
			id    : 'poDeadlineMin',
			tagId : 'poDeadlineMin',
			blank : "선택"
		}).setDataSource(poDeadlineMinDatas);

		*/
	}
	//---------------Initialize Option Box End ------------------------------------------------
	//-------------------------------  Common Function start -------------------------------


	var _URL = window.URL || window.webkitURL;

	function inputFocus(){

	};

	function condiFocus(){

	};

	//-------------------------------  Common Function end -------------------------------


	//------------------------------- Html 버튼 바인딩  Start -------------------------------

	/** Common Save*/
	$scope.fnSave = function(){	 fnSave();};

	$scope.fnClear =function(){	 fnClear();	};

	$scope.fnBack = function(){	 fnBack();};

	//------------------------------- Html 버튼 바인딩  End -------------------------------

}); // document ready - END
