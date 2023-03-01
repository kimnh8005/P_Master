/**-----------------------------------------------------------------------------
 * description 		 : 주문/배송관리 > 배송 정보
 * @
 * @ 수정일			수정자          수정내용
 * @ ------------------------------------------------------
 * @ 2020.12.07		김승우          최초생성
 * **/
"use strict";

var PAGE_SIZE = 20;
var viewModel, gridDs, gridOpt, grid;
var pageParam = fnGetPageParam();

var paramData ;
var odOrderDetlIdList = [];
if(parent.POP_PARAM['parameter']){
	paramData = parent.POP_PARAM['parameter'];
	odOrderDetlIdList = paramData.detlIdList;
}

$(document).ready(function() {
	// importScript("/js/service/od/claim/claimComm.js");

	fnInitialize();	//Initialize Page Call ---------------------------------

	//Initialize PageR
	function fnInitialize(){
		$scope.$emit("fnIsMenu", { flag : false });
		fnPageInfo({
			PG_ID  : "deliveryInfoPopup",
			callback : fnUI
		});
	};

	// 화면 UI 초기화
	function fnUI(){

		fnTranslate();	// 다국어 변환
		fnInitDropList();
		// fnInitButton();
	};

	//--------------------------------- Button Start---------------------------------
	// 버튼 초기화
	function fnInitButton(){
		$("#fnSearch,  #fnClear").kendoButton();
	};
	//--------------------------------- Button End-----------------------------------

	//------------------------------- Grid Start -------------------------------
	// 그리드 초기화
	function fnInitGrid(){};

	//-------------------------------  Grid End  -------------------------------

	//---------------Initialize Option Box Start ------------------------------------------------

	// 옵션 초기화
	function fnInitOptionBox(){};

	function fnInitDropList(){

    	fnKendoDropDownList({
  			id         : 'psShippingCompId',
  			url        : "/admin/policy/shippingcomp/getDropDownPolicyShippingCompList",
  			params	   : {},
  			blank      : '선택해주세요.',
  			valueField : "psShippingCompId",
  			textField  : "shippingCompNm"
  		});

    	//매장배송/픽업인 경우, 직배송 - yyyyMMddHHmm
    	if(paramData.goodsDeliveryType == 'GOODS_DELIVERY_TYPE.SHOP') {
    		deliveryTypeShop();
		}

	};

	function deliveryTypeShop() {
		let psShippingCompId = "1";
		let trackingNo = new Date().oFormat('yyyyMMddHHmm');

		$('#psShippingCompId').data("kendoDropDownList").value(psShippingCompId);
    	$('#trackingNo').val(trackingNo);
	}

	//---------------Initialize Option Box End ------------------------------------------------
	//-------------------------------  Common Function start ----------------------------------

    // viewModel 초기화
    function fnViewModelInit(){};

    // 기본값 설정
    function fnDefaultSet(){};

    function fnSearch() {
    }

    function fnClear() {}

    function fnClose(){
    	parent.LAYER_POPUP_OBJECT.data("kendoWindow").close();
    }

    function fnSave(){

    	var data = $('#deliveryInfoForm').formSerialize(true);

		let params = {};
		params.detlIdList           = [];
		params.psShippingCompIdList = [];
		params.trackingNoList       = [];

		if (data.psShippingCompId == ""){
			fnKendoMessage({message : '택배사를 선택해주세요.'});
			return;
		}
		if (data.trackingNo == ""){
			fnKendoMessage({message : '송장번호를 선택해주세요'});
			return;
		}

    	if(odOrderDetlIdList.length > 0){
    		let i = 0;
    		for(var odOrderDetlId of odOrderDetlIdList){
				params.detlIdList[i]	= odOrderDetlId;
				let psShippingCompId    = data.psShippingCompId;
				let trackingNo          = data.trackingNo;
				if (psShippingCompId != "" && trackingNo != "") {
					params.psShippingCompIdList[i]  = psShippingCompId;
					params.trackingNoList[i]        = trackingNo;
				}
				i++;
    		}

    		fnAjax({
    			url: '/admin/order/status/getOrderDetailStatusInfo',
    			params  : {"detlIdList[]": params.detlIdList},
    			//contentType: "application/json",
    			success :
    				function( data ){
     					var suCnt = 0;
    					if (data.length == 0) {
    						fnKendoMessage({message : "배송중 업데이트 할 데이터가 없습니다."});
    					} else {
    						for (var i = 0; i < data.length; i++) {
    							if (data[0].orderStatusCd == "DR") {
    								suCnt = 1;
    							}
    						}

    						if (suCnt == 1) {
    							fnAjax({
    								url: '/admin/order/status/putOrderDetailStatus',
    								params  : {"detlIdList[]": params.detlIdList, "psShippingCompIdList[]": params.psShippingCompIdList, "trackingNoList[]": params.trackingNoList, "statusCd": "DI"},
    								//contentType: "application/json",
    								success :
    									function( data ){

    										fnKendoMessage({message : fnGetLangData({nullMsg :odOrderDetlIdList.length+'건의 주문상태가 배송중 상태로 변경되었습니다.' })
    											,ok :function(e){
    												paramData.successDo = true;
    												fnClose();
    											}});
    								},
    								isAction : 'select'
    							});
    						} else {
    							fnKendoMessage({message : "주문상태가 배송준비중이 아닌 것이 있습니다. \n다시 확인 하시기 바랍니다."
		    							,ok :function(e){
											paramData.successDo = true;
											fnClose();
										}});
    						}
    					}
    			},
    			isAction : 'select'
    		});
    		/**
			fnAjax({
				url: '/admin/order/status/putOrderDetailStatus',
				params  : {"detlIdList[]": params.detlIdList, "psShippingCompIdList[]": params.psShippingCompIdList, "trackingNoList[]": params.trackingNoList, "statusCd": "DI"},
				//contentType: "application/json",
				success :
					function( data ){

						fnKendoMessage({message : fnGetLangData({nullMsg :odOrderDetlIdList.length+'건의 주문상태가 배송중 상태로 변경되었습니다.' })
							,ok :function(e){
								paramData.successDo = true;
								fnClose();
							}});
				},
				isAction : 'select'
			});
			*/
    	}
    }


	//-------------------------------  Common Function end -------------------------------

	//------------------------------- Html 버튼 바인딩  Start -----------------------------
	/** Common Search*/
	$scope.fnSearch = function( ) {	fnSearch();	};

	/** Common Clear*/
	$scope.fnClear =function(){	fnClear(); };
	$scope.fnSave =function(){	fnSave(); };
	$scope.fnClose = function( ){  fnClose();};

	//------------------------------- Html 버튼 바인딩  End -------------------------------

	//------------------------------- Validation Start -----------------------------------

	fnInputValidationForAlphabetNumberLineBreakComma("trackingNo");

	//------------------------------- Validation End -------------------------------------
}); // document ready - END
