﻿/**-----------------------------------------------------------------------------
 * description 		 : 검색어관리  - 인기검색어
 * @ 페이징 없이 상위 20개까지만 노출 정책
 * @ 수정일			수정자          수정내용
 * @ ------------------------------------------------------
 * @ 2020.07.01		김경민          최초생성
 * @
 * **/
'use strict';

var aGridDs, aGridOpt, aGrid;

$(document).ready(function() {
	fnInitialize();	//Initialize Page Call ---------------------------------

	//Initialize PageR
	function fnInitialize(){
		$scope.$emit('fnIsMenu', { flag : 'true' });

		fnPageInfo({
			PG_ID  : 'searchIndexMgm',
			callback : fnUI
		});
	}

	function fnUI(){
		fnInitButton();	//Initialize Button  ---------------------------------
		//fnIndex();
	}

	//--------------------------------- Button Start---------------------------------

	function fnInitButton(){
		$('#fnIndex').kendoButton();
	}
	function fnIndex(){
		fnAjax({
			url     : '/admin/dp/searchIndex/callItemIndex',
			success :
				function( data ){
					fnBizCallback("itemIndex");
				},
			isAction : 'insert'
		});
	}

	function fnSuggestionIndex(){
		fnAjax({
			url     : '/admin/dp/searchIndex/callItemSuggestionIndex',
			success :
				function( data ){
					fnBizCallback("suggestionIndex");
				},
			isAction : 'insert'
		});
	}

	function fnPromotionSuggestionIndex(){
		fnAjax({
			url     : '/admin/dp/searchIndex/callPromotionSuggestionIndex',
			success :
				function( data ){
					fnBizCallback("promotionSuggestionIndex");
				},
			isAction : 'insert'
		});
	}

	function fnStoreIndex(){
		fnAjax({
			url     : '/admin/dp/searchIndex/callStoreItemIndex',
			success :
				function( data ){
					fnBizCallback("storeItemIndex");
				},
			isAction : 'insert'
		});
	}

	function fnBizCallback( id, data ){
		switch(id){
			case 'itemIndex':
				fnKendoMessage({message :  '상품색인이 완료되었습니다.' });
				break;
			case 'suggestionIndex':
				fnKendoMessage({message :  '자동완성색인이 완료되었습니다.' });
				break;
			case 'promotionSuggestionIndex':
				fnKendoMessage({message :  '이벤트/기획전 자동완성색인이 완료되었습니다.' });
				break;
			case 'storeItemIndex' :
				fnKendoMessage({message :  '매장상품색인이 완료되었습니다.' });
				break;
		}
	}

	//--------------------------------- Button End---------------------------------
	//---------------Initialize Option Box End ------------------------------------------------
	//-------------------------------  Common Function start -------------------------------
	//-------------------------------  Common Function end -------------------------------


	//------------------------------- Html 버튼 바인딩  Start -------------------------------
	/** Common Search*/
	$scope.fnIndex = function( ) {	fnIndex();};
	$scope.fnSuggestionIndex = function( ) {	fnSuggestionIndex();};
	$scope.fnPromotionSuggestionIndex = function( ) {	fnPromotionSuggestionIndex();};
	$scope.fnStoreIndex = function( ) {	fnStoreIndex();};


	//------------------------------- Html 버튼 바인딩  End -------------------------------

}); // document ready - END
