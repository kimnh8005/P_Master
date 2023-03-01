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
			PG_ID  : "csRefundAccountInfoPopup",
			callback : fnUI
		});
	};

	// 화면 UI 초기화
	function fnUI(){

		fnTranslate();	// 다국어 변환
		fnInitTable();
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

	function fnInitTable(){
		$("#bankNm").text(paramData.bankNm);
		$("#accountHolder").text(paramData.accountHolder);
		$("#accountNumber").text(paramData.accountNumber);
	};

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
