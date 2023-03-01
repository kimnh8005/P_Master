﻿﻿/**-----------------------------------------------------------------------------
 * description 		 : 빌랙리스트 이력
 * @
 * @ 수정일			수정자          수정내용
 * @ ------------------------------------------------------
 * @ 2020.06.26		박영후          최초생성
 * @
 * **/
'use strict';

var aGridDs, aGridOpt, aGrid;
var paramData ;

if(parent.POP_PARAM['parameter']){
	paramData = parent.POP_PARAM['parameter'];
}

$(document).ready(function() {
	fnInitialize();	//Initialize Page Call ---------------------------------

	//Initialize PageR
	function fnInitialize(){
		$scope.$emit('fnIsMenu', { flag : false });

		fnPageInfo({
			PG_ID  : 'ursBlackHist',
			callback : fnUI
		});
	}

	function fnUI(){

		fnTranslate();	// 다국어 변환--------------------------------------------

		fnInitButton();	//Initialize Button  ---------------------------------

		fnInitGrid();	//Initialize Grid ------------------------------------

		fnInitOptionBox();//Initialize Option Box ------------------------------------

	}

	//--------------------------------- Button Start---------------------------------

	function fnInitButton(){
	}

	function fnSearch(){
		var data = {"urUserId": paramData.urUserId};
		var query = {
			filterLength : fnSearchData(data).length,
			filter :  {
				filters : fnSearchData(data)
			}
		};
		aGridDs.query( query );
	}

	function fnClose(params){
		if(params){
			parent.POP_PARAM = params;
		}
		parent.LAYER_POPUP_OBJECT.data('kendoWindow').close();
	}

	//--------------------------------- Button End---------------------------------


	//------------------------------- Grid Start -------------------------------
	function fnInitGrid(){
		aGridDs = fnGetPagingDataSource({
			url      : '/admin/ur/userBlack/getUserBlackHistoryList'
		});
		aGridOpt = {
			dataSource: aGridDs
			//,navigatable: true
			,height: 343
			,scrollable: true
			,columns   : [
					{ field:'rowNumber'			,title : 'No.'		, width:'40px'	,attributes:{ style:'text-align:center' }}
					,{ field:'reason'			,title : '사유'		, width:'200px'	,attributes:{ style:'text-align:left' }}
					,{ field:'createDate'		,title : '등록일'		, width:'60px'	,attributes:{ style:'text-align:center' }}
					,{ field:'registerUserName'	,title : '등록자'		, width:'50px'	,attributes:{ style:'text-align:center' }}
			]
			,selectable : false
		};
		$('#aGrid').initializeKendoGrid( aGridOpt ).cKendoGrid();

		fnSearch();
	}
	//-------------------------------  Grid End  -------------------------------

	//---------------Initialize Option Box Start ------------------------------------------------
	function fnInitOptionBox(){
	}
	//---------------Initialize Option Box End ------------------------------------------------

	//-------------------------------  Common Function start -------------------------------
	//-------------------------------  Common Function end -------------------------------

	//------------------------------- Html 버튼 바인딩  Start -------------------------------
	/** Common Search*/

	/** Common Close*/
	$scope.fnClose = function( ){  fnClose();};
	//------------------------------- Html 버튼 바인딩  End -------------------------------

}); // document ready - END
