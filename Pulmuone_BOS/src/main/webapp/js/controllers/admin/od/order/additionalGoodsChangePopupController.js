/**-----------------------------------------------------------------------------
 * description 		 : 주문생성 > 추가상품 옵션 변경
 * @
 * @ 수정일			수정자          수정내용
 * @ ------------------------------------------------------
 * @ 2021.07.30		천혜현          최초생성
 * **/
"use strict";

var PAGE_SIZE = 20;
var viewModel, gridDs, gridOpt, grid;
var pageParam = fnGetPageParam();
var paramData ;
var additionalGoodsData = []; 		// 추가상품 셀렉트박스 data
if(parent.POP_PARAM['parameter']){
	paramData = parent.POP_PARAM['parameter'];
}

$(document).ready(function() {

	fnInitialize();	//Initialize Page Call ---------------------------------

	function fnInitialize(){
		$scope.$emit("fnIsMenu", { flag : false });
		fnPageInfo({
			PG_ID  : "additionalGoodsChangePopup",
			callback : fnUI
		});
	};

	// 화면 UI 초기화
	function fnUI(){
		fnInitGrid();
	};

	//--------------------------------- Button Start---------------------------------
	//--------------------------------- Button End-----------------------------------

	//------------------------------- Grid Start -------------------------------
	// 그리드 초기화
	function fnInitGrid(){

		// 상품명
		$(".title__subTitle").html(paramData.goodsName);

		fnAjax({
			url     : '/admin/order/create/getAdditionalGoodsInfoList',
			params  : {"ilGoodsId":paramData.ilGoodsId},
			success :
				function( data ){

					if(data != null){
						let additionalGoodsList = [];
						for(let i=0; i < data.length; i++){
							let additionalGoodsObj = new Object();
							additionalGoodsObj.CODE = data[i].ilGoodsId;
							additionalGoodsObj.NAME = data[i].goodsName;
							additionalGoodsList[i] = additionalGoodsObj;
						}
						additionalGoodsData = additionalGoodsList;

					}
					fnInitOptionBox();
				},
			isAction : 'batch'
		});

	}

	//-------------------------------  Grid End  -------------------------------

	//---------------Initialize Option Box Start ------------------------------------------------
	function fnInitOptionBox(){

		fnKendoDropDownList({
			id    : 'additionalGoods',
			data  : additionalGoodsData,
			textField :"NAME",
			valueField : "CODE",
			blank :"선택해주세요"
		});
	}

	function fnBindEvents(){
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

    	let additionalGoodsOptionChangeData = $('#additionalGoodsOptionChangeForm').formSerialize(true);
		additionalGoodsOptionChangeData.goodsType = "GOODS_TYPE.ADDITIONAL";	/* 판매유형 : 추가 */
		parent.POP_PARAM["parameter"].additionalGoodsOptionChangeData = additionalGoodsOptionChangeData;

		fnKendoMessage({
			message: "추가되었습니다.", ok: function () {
				fnClose();
			}
		});

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

	//------------------------------- Validation End -------------------------------------
}); // document ready - END
