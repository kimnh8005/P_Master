/**-----------------------------------------------------------------------------
 * description 		 : 시스템관리 - 메뉴관리(팝업)
 * @
 * @ 수정일			수정자          수정내용
 * @ ------------------------------------------------------
 * @ 2016.12.21		최봉석          최초생성
 * @
 * **/
'use strict';


var PAGE_SIZE = 20;
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
			PG_ID  : 'pMenuPopup',
			callback : fnUI
		});

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
		$('#fnSearch, #fnNew,#fnSave,  #fnClear, #fnClose').kendoButton();
		$('#fnDel').kendoButton({ enable: false });
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
	function fnNew(){
		var pgId = $('#PG_ID').val();
		$('#inputForm').formClear(true);
		$('#PG_ID').val(pgId);
		inputFocus();
	}
	function fnSave(){
		var url  = '/biz/st/mn/stmn002/insert';
		var cbId = 'insert';

		if( OPER_TP_CODE == 'U' ){
			url  = '/biz/st/mn/stmn002/update';
			cbId= 'update';
		}

		var data = $('#inputForm').formSerialize(true);

		if( data.rtnValid ){
			fnAjax({
				url     : url,
				params  : data,
				success :
					function( data ){
						fnBizCallback(cbId, data);
					}
			});
		}else{

		}
	}
	function fnDel(){
		fnKendoMessage({message:fnGetLangData({key :"4489",nullMsg :'삭제 하시겠습니까?' }), type : "confirm" ,ok :fnDelApply  });
	}
	function fnDelApply(){
		var url  = '/biz/st/mn/stmn002/delete';
		var cbId = 'delete';
		var data = $('#inputForm').formSerialize(true);
		if( data.rtnValid ){
			fnAjax({
				url     : url,
				params  : data,
				success :
					function( data ){
						fnBizCallback(cbId);
					}
			});
		}else{

		}
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
			url      : '/admin/st/menu/getParentMenuList',
			pageSize : PAGE_SIZE
		});

		aGridOpt = {
			dataSource: aGridDs
			,  pageable  : {
				pageSizes: [20, 30, 50],
				buttonCount : 10,
				responsive : false
			}
			,navigatable: true
			,height:380
			,scrollable: true
			,columns   : [
					{ field:'menuGroupName'	,title : '메뉴그룹명'	, width:'90px',attributes:{ style:'text-align:left' }}
				,{ field:'stMenuId'		,title : '메뉴아이디'	, width:'80px',attributes:{ style:'text-align:center' }}
				,{ field:'treeMenuName'	,title : '메뉴명'		, width:'200px'	,attributes:{ style:'text-align:left' }, template: kendo.template($("#menuNameTpl").html()) }
				,{ field:'parentMenuName'		,title : '상위메뉴명'	, width:'100PX',attributes:{ style:'text-align:center' }}
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

		var stMenuGroupId = paramData.stMenuGroupId;
		fnKendoDropDownList({
			id    : 'schMenuGroup',
			url : "/admin/st/menu/getMenuGroupNamePopList",
			params : {"stMenuGroupId": stMenuGroupId, "useYn" :"Y"},
			textField :"menuGroupName",
			valueField : "stMenuGroupId"
		});

		if(paramData){
			if(paramData.menuType){
				$('#condi6').val(paramData.menuType);
			}
			if(paramData.stMenuGroupId){
				$('#schMenuGroup').data("kendoDropDownList").value(paramData.stMenuGroupId);
				//$('#schMenuGroup').val(paramData.stMenuGroupId);
			}
		}

		fnTagMkRadio({
			id    :  'condiActive',
			tagId : 'condiActive',
			chkVal: '',
			data  : [   { "CODE" : ""	, "NAME":'전체' },
						{ "CODE" : "Y"	, "NAME":'예' },
						{ "CODE" : "N"	, "NAME":'아니오' }
					],
			style : {}
		});

		$('#condition2').on('keydown',function(e){
			if (e.keyCode == 13) {
				fnSearch();
			}
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

		/**
	 * 콜백합수
	 */
	function fnBizCallback( id, data ){
		switch(id){
			case 'select':
				//form data binding
				$('#searchForm').bindingForm(data, 'rows', true);
				break;
			case 'insert':
				if(data.rows =="DUP_DATA"){
					fnKendoMessage({message : fnGetLangData({key :"370",nullMsg :'중복입니다.' })  ,ok :factory.inputFocus});
				}else{
					aGridDs.insert(data.rows);;
					fnKendoMessage({message : fnGetLangData({key :"369",nullMsg :'입력되었습니다.' }) ,ok :fnNew});
				}
				break;
			case 'save':
				fnKendoMessage({message : fnGetLangData({key :"368",nullMsg :'저장되었습니다.' })});
				break;
			case 'update':
				fnKendoMessage({message : fnGetLangData({key :"367",nullMsg :'수정되었습니다.' }) });
				break;
			case 'delete':
				fnClose();
				//aGridDs.total = aGridDs.total-1;
				fnKendoMessage({message : fnGetLangData({key :"366",nullMsg :'삭제되었습니다.' })});
				break;

		}
	}
	function fnPopupButton(param ){
		fnKendoPopup({
			id     : 'SAMPLE_POP',
			title  : '프로그램 팝업',
			src    : '#/pgm001',
			success: function( id, data ){
				if(param =="input"){
					$('#PG_ID').val(data.ST_PROGRAM_ID);
				}
			}
	});
};
	//-------------------------------  Common Function end -------------------------------


	//------------------------------- Html 버튼 바인딩  Start -------------------------------
	/** Common Search*/
	$scope.fnSearch = function( ) {	fnSearch();	};
	/** Common Clear*/
	$scope.fnClear =function(){	 fnClear();	};
	/** Common New*/
	$scope.fnNew = function( ){	fnNew();};
	/** Common Save*/
	$scope.fnSave = function(){	 fnSave();};
	/** Common Delete*/
	$scope.fnDel = function(){	 fnDel();};
	/** Common Close*/
	$scope.fnClose = function( ){  fnClose();};

	$scope.fnPopupButton = function(data){ fnPopupButton(data); };
	//------------------------------- Html 버튼 바인딩  End -------------------------------

}); // document ready - END
