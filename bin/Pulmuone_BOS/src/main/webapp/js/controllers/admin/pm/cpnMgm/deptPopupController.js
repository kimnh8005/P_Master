/**-----------------------------------------------------------------------------
 * description 		 : 조직정보 조회
 * @
 * @ 수정일			수정자          수정내용
 * @ ------------------------------------------------------
 * @ 2020.08.21		안치열          최초생성
 * @
 * **/
'use strict';


var PAGE_SIZE = 20;
var aGridDs, aGridOpt, aGrid;
var paramData ;
if(parent.POP_PARAM['parameter']){

	paramData = parent.POP_PARAM['parameter'];
	paramData.menuType = 'MENU_TYPE.PAGE';
}

$(document).ready(function() {
	fnInitialize();	//Initialize Page Call ---------------------------------

	//Initialize PageR
	function fnInitialize(){
		$scope.$emit('fnIsMenu', { flag : false });

		fnPageInfo({
			PG_ID  : 'deptPopup',
			callback : fnUI
		});

	}

	function fnUI(){

		fnInitButton();	//Initialize Button  ---------------------------------

		fnInitGrid();	//Initialize Grid ------------------------------------

		fnInitOptionBox();//Initialize Option Box ------------------------------------

		fnSearch();
	}

	//--------------------------------- Button Start---------------------------------

	function fnInitButton(){
		$('#fnSearch,  #fnClear, #fnClose').kendoButton();
	}

	function fnSearch(){

		var data;
		data = $('#searchForm').formSerialize(true);
		$('#schMenuGroup').val(data.stMenuGroupId);
		var query = {
					page         : 1,
					pageSize     : PAGE_SIZE,
					filterLength : fnSearchData(data).length,
					filter :  {
						filters : fnSearchData(data)
					}
		};
		aGridDs.query( query );
	}
	function fnClear(){
		$('#searchForm').formClear(true);
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
			url      : '/admin/pm/cpnMgm/getOrganizationPopupList',
			pageSize : PAGE_SIZE
		});

		aGridOpt = {
			dataSource: aGridDs
			,  pageable  : {
				pageSizes: [20, 30, 50],
				buttonCount : 10,
				responsive: false
			}
			,navigatable: true
			,height:380
			,scrollable: true
			,columns   : [
				{ field:'no'	,title : 'No'	, width:'80px',attributes:{ style:'text-align:center' }}
				,{ field:'erpRegalCode'		,title : '법인코드'	, width:'90px',attributes:{ style:'text-align:center' }}
				,{ field:'erpRegalName'	,title : '법인명'		, width:'90px'	,attributes:{ style:'text-align:center' }}
				,{ field:'erpOrganizationCode'	,title : '조직코드'		, width:'90px'	,attributes:{ style:'text-align:center' }}
				,{ field:'erpOrganizationName'	,title : '조직명'		, width:'150px'	,attributes:{ style:'text-align:center' }}
				,{ field:'finOrganizationCode'	,title : '회계코드'		, width:'150px'	,attributes:{ style:'text-align:center' }}
				,{ title:  fnGetLangData({key :"660",nullMsg :'관리' }), width: "90px", attributes:{ style:'text-align:center;'  , class:'forbiz-cell-readonly' }
				,command: [ { name: 'cEdit',  text: '선택'		, imageClass: "k-i-add", className: "f-grid-add k-margin5", iconClass: "k-icon", click: fnSelectOrganization,
							click: function(e) {
								 e.preventDefault();
						            var tr = $(e.target).closest("tr"); // get the current table row (tr)
						            var data = this.dataItem(tr);

						            fnSelectOrganization(data);
							}
					}]
				}
			]
		};
		aGrid = $('#aGrid').initializeKendoGrid( aGridOpt ).cKendoGrid();
        aGrid.bind("dataBound", function() {
			//total count
            $('#countTotalSpan').text(aGridDs._total);
        });
		$("#aGrid").on("dblclick", "tbody>tr", function () {
				//fnGridClick();
		});

	}
	function fnSelectOrganization(param){

		fnClose(param);
	};
	//-------------------------------  Grid End  -------------------------------

	//---------------Initialize Option Box Start ------------------------------------------------
	function fnInitOptionBox(){

		fnKendoDropDownList({
			id : 'codeType',
			data : [ {
				"CODE" : "CODE_TYPE.ORGANIZATION",
				"NAME" : "조직코드"
			}, {
				"CODE" : "CODE_TYPE.REGAL",
				"NAME" : "법인코드"
			}],
			chkVal : 'CODE_TYPE.ORGANIZATION'
		});

		fnKendoDropDownList({
			id : 'nameType',
			data : [ {
				"CODE" : "NAME_TYPE.ORGANIZATION",
				"NAME" : "조직명"
			}, {
				"CODE" : "NAME_TYPE.REGAL",
				"NAME" : "법인명"
			}],
			chkVal : 'CODE_TYPE.ORGANIZATION'
		});
	}
	//---------------Initialize Option Box End ------------------------------------------------
	//-------------------------------  Common Function start -------------------------------

	function inputFocus(){
		$('#input1').focus();
	};
		function condiFocus(){
		$('#condition1').focus();
	};

	//-------------------------------  Common Function end -------------------------------


	//------------------------------- Html 버튼 바인딩  Start -------------------------------
	/** Common Search*/
	$scope.fnSearch = function( ) {	fnSearch();	};
	/** Common Clear*/
	$scope.fnClear =function(){	 fnClear();	};
	/** Common Close*/
	$scope.fnClose = function( ){  fnClose();};

	$scope.fnPopupButton = function(data){ fnPopupButton(data); };
	//------------------------------- Html 버튼 바인딩  End -------------------------------

}); // document ready - END
