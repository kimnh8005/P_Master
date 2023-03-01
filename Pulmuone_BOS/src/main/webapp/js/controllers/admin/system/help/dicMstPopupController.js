/**-----------------------------------------------------------------------------
 * description 		 : 글로벌 - 표준용어 팝업
 * @
 * @ 수정일			수정자          수정내용
 * @ ------------------------------------------------------
 * @ 2020.06.05		천혜현          최초생성
 * @
 * **/
'use strict';

var PAGE_SIZE = 20;
var aGridDs, aGridOpt, aGrid;
var paramData ;
var isHideDictionaryType = false;

if(parent.POP_PARAM['parameter']){
	paramData = parent.POP_PARAM['parameter'];

	switch(paramData.MENU_TYPE) {
		case 'all':
			break;
		case 'word':
			isHideDictionaryType = true;
			break;
		case 'help':
			break;
		default:
			break;
	}
}

$(document).ready(function() {
	fnInitialize();	//Initialize Page Call ---------------------------------

	//Initialize PageR
	function fnInitialize(){
		$scope.$emit('fnIsMenu', { flag : false });

		fnPageInfo({
			PG_ID    : 'dicMstPopup',
			callback : fnUI
		});
	}

	function fnUI(){

		fnTranslate();	// comm.lang.js 안에 있는 공통함수 다국어 변환--------------------------------------------

		fnInitButton();	//Initialize Button  ---------------------------------

		fnInitGrid();	//Initialize Grid ------------------------------------

		fnInitOptionBox();//Initialize Option Box ------------------------------------

		fnSearch();

		fnPreventSubmit();
	}

	//--------------------------------- Button Start---------------------------------

	function fnInitButton(){
		$('#fnSearch, #fnClear').kendoButton();
		$('#fnDel').kendoButton({ enable: false });
	}
	function fnSearch(){
		var data;
		data = $('#searchForm').formSerialize(true);

		if(paramData){
			switch(paramData.MENU_TYPE) {
				case 'all':
					data.dictionaryType = "";
					break;
				case 'word':
					data.dictionaryType = "DIC_TP.1";
					break;
				case 'help':
					data.dictionaryType = "DIC_TP.4";
					break;
				default:
					data.dictionaryType = "";
					break;
			}
		}

		var query = {
					page         : 1,
					pageSize     : PAGE_SIZE,
					filterLength : fnSearchData(data).length,
					filter :  {
						filters : fnSearchData(data)
					},
		};
		aGridDs.query( query );
		condiFocus();
	}
	function fnClear(){
		$('#searchForm').formClear(true);
		$('input:radio[name="dictionaryType"]:input[value=""]').prop("checked", true);
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
			url      : '/admin/system/help/nation/getDictionaryMasterList',
			pageSize : PAGE_SIZE
		});

		aGridOpt = {
			dataSource: aGridDs
			,  pageable  : {
				pageSizes: [20, 30, 50],
				buttonCount : 5,
				responsive : false
			}
			,navigatable: true
			,height:380
			//,scrollable: true,
			,columns   : [
					{ field:'baseName'			,title : '표준용어'	, width:'515px',attributes:{ style:'text-align:left' }}
					, {
						field:'dictionaryType'
						, title : '표준용어 구분'
						, width:'100px'
						, attributes:{ style:'text-align:center' }
						, hidden : isHideDictionaryType
					}
			]
		};
		aGrid = $('#aGrid').initializeKendoGrid( aGridOpt ).cKendoGrid();

		$("#aGrid").on("dblclick", "tbody>tr", function () {
				fnGridClick();
		});

		aGrid.bind("dataBound", function() {
			hideGridResizeHandler();
		})
	}
	function fnGridClick(){
		var map = aGrid.dataItem(aGrid.select());
		fnClose(map);
	};
	//-------------------------------  Grid End  -------------------------------

	//---------------Initialize Option Box Start ------------------------------------------------
	function fnInitOptionBox(){
		//$('#searchForm').enterTab({'search' : 'true'});
		$('#baseName').on('keydown',function(e){
			if (e.keyCode == 13) {
				fnSearch();
			}
		});

		if(paramData){
			if(paramData.MENU_TYPE){
				$("#dicTypeTh").css("display","none");
				$("#dicTypeTd").css("display","none");
				$("#baseNmTd").attr("colSpan","3");
			}else{
				fnTagMkRadio({
					id    :  'dictionaryType',
					tagId : 'dictionaryType',
					url   : "/admin/comn/getCodeList",
					params : {"stCommonCodeMasterCode" : "DIC_TP", "useYn" :"Y"},
					beforeData : [
						{"CODE":"", "NAME":"전체"},
					],
					chkVal : "",
					async : false
				});
			}
		}
	}

	//---------------Initialize Option Box End ------------------------------------------------
	//-------------------------------  Common Function start -------------------------------

	function condiFocus(){
		$('#baseName').focus();
	};

	/**
	 * 콜백합수
	 */
	function fnBizCallback( id, data ){
		switch(id){
			case 'select':
				//form data binding
				$('#searchForm').bindingForm(data, 'rows', true);
				break;
		}
	}

	function hideGridResizeHandler() {
		$("#aGrid").find(".k-resize-handle").hide();
	}

	//-------------------------------  Common Function end -------------------------------


	//------------------------------- Html 버튼 바인딩  Start -------------------------------
	/** Common Search*/
	$scope.fnSearch = function( ) {	fnSearch();	};
	/** Common Clear*/
	$scope.fnClear =function(){	 fnClear();	};
	/** Common Close*/
	$scope.fnClose = function( ){  fnClose();};
	//------------------------------- Html 버튼 바인딩  End -------------------------------

}); // document ready - END
