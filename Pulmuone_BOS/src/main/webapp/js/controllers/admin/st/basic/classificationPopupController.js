/**-----------------------------------------------------------------------------
 * system 			 : 분류관리
 * @
 * @ 수정일			수정자          수정내용
 * @ ------------------------------------------------------
 * @ 2017.01.13		최봉석          최초생성
 * @
 * **/
'use strict';


var PAGE_SIZE = 20;
var aGridDs, aGridOpt, aGrid;
var pageParam = new Object();
var csId = 0;

$(document).ready(function() {
	fnInitialize();	//Initialize Page Call ---------------------------------

	//Initialize PageR
	function fnInitialize(){
		$scope.$emit('fnIsMenu', { flag : false });

		fnPageInfo({
			PG_ID  : 'classificationPopup',
			callback : fnUI
		});

		// 파라미터 설정하기 --------------------------------------------------------------
		pageParam = fnGetPageParam();
		csId = pageParam.csId;
		// ------------------------------------------------------------------------

	}

	function fnUI(){

		fnTranslate();	// 다국어 변환--------------------------------------------

		fnInitButton();	//Initialize Button  ---------------------------------

		fnInitGrid();	//Initialize Grid ------------------------------------

		fnInitOptionBox();//Initialize Option Box ------------------------------------

		fnSearch();
	}

	//--------------------------------- Button Start---------------------------------

	function fnInitButton(){
		$('#fnSearch,#fnClose,#fnClear').kendoButton();
	}
		function fnSearch(){
		var data = $('#searchForm').formSerialize(true);
		var query = {
				page         : 1,
				pageSize     : PAGE_SIZE,
				filterLength : fnSearchData(data).length,
				filter :  {
					filters : fnSearchData(data)
				}
		};

		aGridDs.query(query);
	}
	function fnClose(params){
		if(params){
			parent.POP_PARAM = params;
		}
		parent.LAYER_POPUP_OBJECT.data('kendoWindow').close();
	}

	function fnClear(){
		$('#searchForm').formClear(true);
		$("span#condiActive input:radio").eq(0).click();	//radio init
	}

	//--------------------------------- Button End---------------------------------


	//------------------------------- Grid Start -------------------------------
	function fnInitGrid(){

		aGridDs = fnGetPagingDataSource({
			url      : '/admin/st/basic/getClassificationList'
			,pageSize       : PAGE_SIZE
		});

		aGridOpt = {
			dataSource: aGridDs
			,navigatable: true
			,pageable :	{
				refresh: true
				,pageSizes : false
				,pageSizes: [20, 30, 50]
				,buttonCount: 5
				,responsive : false
			}
			,columns   : [
				{ field:'type'			,title : '분류코드'	, width:'130px',attributes:{ style:'text-align:center' }}
				,{ field:'typeName'		,title : '구분명'	, width:'160px',attributes:{ style:'text-align:center' }, template: kendo.template($("#classificationNameTpl").html())}
				,{ field:'depth'		,title : '깊이'	, width:'70px',attributes:{ style:'text-align:center' }}
				,{ field:'parentsName'	,title : '상위구분'	, width:'160px',attributes:{ style:'text-align:center' }}
				,{ field:'sort'			,title : '정렬'	, width:'80px',attributes:{ style:'text-align:center' }}
				,{ field:'useYn'		,title : '사용여부'	, width:'90px',attributes:{ style:'text-align:center' }
					,template : "#=(useYn=='Y')?'예':'아니오'#"
				}
			]

		};
		aGrid = $('#aGrid').initializeKendoGrid( aGridOpt ).cKendoGrid();

		$("#aGrid").on("dblclick", "tbody>tr", function () {
				fnGridClick();
		});
	}
	function fnGridClick(){
		var map = aGrid.dataItem(aGrid.select());
		fnClose(map);
	};
	//-------------------------------  Grid End  -------------------------------

	//---------------Initialize Option Box Start ------------------------------------------------
	function fnInitOptionBox(){
		fnTagMkRadio({
			id    :  'condiActive',
			tagId : 'useYn',
			chkVal: '',
			data  : [   { "CODE" : ""	, "NAME":'전체' },
						{ "CODE" : "Y"	, "NAME":'예' },
						{ "CODE" : "N"	, "NAME":'아니오' }
					],
			style : {}
		});
		$('#type').on('keydown',function(e){
			if (e.keyCode == 13) {
				fnSearch();
				condiFocus();
			}
		});

		fnKendoDropDownList({
			id    : 'type',
			url : "/admin/st/basic/getTypeList",
			params : {},
			textField :"typeName",
			valueField : "typeId",
			blank : "전체"
		});

	}
	//---------------Initialize Option Box End ------------------------------------------------
	//-------------------------------  Common Function start -------------------------------

	function condiFocus(){
		$('#type').focus();
	};

	//-------------------------------  Common Function end -------------------------------


	//------------------------------- Html 버튼 바인딩  Start -------------------------------
	/** Common Search*/
	$scope.fnSearch = function( ) {	fnSearch();	};
	/** Common Close*/
	$scope.fnClose = function( ){  fnClose();};
	/** Common Clean*/
	$scope.fnClear = function( ){  fnClear();};
	//------------------------------- Html 버튼 바인딩  End -------------------------------

}); // document ready - END
