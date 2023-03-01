/**-----------------------------------------------------------------------------
 * description 		 : 정책관리 - 약관그룹관리
 * @
 * @ 수정일			수정자          수정내용
 * @ ------------------------------------------------------
 * @ 2016.12.26		최봉석          최초생성
 * @
 * **/
'use strict';


var PAGE_SIZE = 20;
var aGridDs, aGridOpt, aGrid;

$(document).ready(function() {
	fnInitialize();	//Initialize Page Call ---------------------------------

	//Initialize PageR
	function fnInitialize(){
		$scope.$emit('fnIsMenu', { flag : 'true' });

		fnPageInfo({
			PG_ID  : 'codeMst',
			callback : fnUI
		});

	}

	function fnUI(){

		fnTranslate();	// comm.lang.js 안에 있는 공통함수 다국어 변환--------------------------------------------

		fnInitButton();	//Initialize Button  ---------------------------------

		fnInitGrid();	//Initialize Grid ------------------------------------

		fnInitOptionBox();//Initialize Option Box ------------------------------------

		fnSearch();

	}

	//--------------------------------- Button Start---------------------------------

	function fnInitButton(){
		$('#fnSearch, #fnNew, #fnSave, #fnClear, #fnClose').kendoButton();
		$('#fnDel').kendoButton({ enable: false });
	}
		function fnSearch(){
		$('#inputForm').formClear(false);
		var data = $('#searchForm').formSerialize(true);
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
		$("span#condiActive input:radio").eq(0).click();	//radio init
	}
	function fnNew(){
		//aGrid.clearSelection();
		$('#inputForm').formClear(true);
		inputFocus();
		fnKendoInputPoup({height:"500px" ,width:"600px",title:{key :"5872",nullMsg :'공통코드마스터 등록' } });
	}
	function fnSave(){
		var url  = '/admin/st/code/addCodeMaster';
		var cbId = 'insert';

		if( OPER_TP_CODE == 'U' ){
			url  = '/admin/st/code/putCodeMaster';
			cbId= 'update';
		}

		var data = $('#inputForm').formSerialize(true);

		if( data.rtnValid ){
			fnAjax({
				url     : url,
				params  : data,
				success :
					function( data ){
						fnBizCallback(cbId, data );
					},
				isAction : 'batch'
			});
		}
	}
	function fnDelApply(){
		var url  = '/admin/st/code/delCodeMaster';
		var cbId = 'delete';
		var data = $('#inputForm').formSerialize(true);
		var map = aGrid.dataItem(aGrid.select());
		if( data.rtnValid ){
			fnAjax({
				url     : url,
				params  : data,
				success :
					function( data ){
						fnBizCallback(cbId, map);
					},
				isAction : 'delete'
			});
		}else{

		}
	}
	function fnClose(){
		var kendoWindow =$('#kendoPopup').data('kendoWindow');
		kendoWindow.close();
	}

	//--------------------------------- Button End---------------------------------


	//------------------------------- Grid Start -------------------------------
	function fnInitGrid(){
		aGridDs = fnGetPagingDataSource({
			url      : "/admin/st/code/getCodeMasterList",
			pageSize : PAGE_SIZE
		});
		aGridOpt = {
			dataSource: aGridDs
			,  pageable  : {
				pageSizes: [20, 30, 50],
				buttonCount : 5
			}
			,navigatable: true
			,columns   : [
					{ field: 'no'					, title: 'No'		, width: '80px', attributes:{ style:'text-align:center' }}
				,	{ field: 'commonMasterName'		, title: '마스터코드명'	, width: '250px', attributes:{ style:'text-align:center' }}
				,	{ field: 'commonMasterCode'		, title: '마스터코드값'	, width: '250px', attributes:{ style:'text-align:center' }}
				,	{ field: 'comment'				, title: '설명'		, width: '400px', attributes:{ style:'text-align:center' }}
				, 	{ field: 'useYn'				, title: '사용여부'	, width:'80px',attributes:{ style:'text-align:center' }
				,	template : "#=(useYn=='Y')?'예':'아니오'#"
				}
				,   { field: 'stComnCodeMstId', hidden: true}
			]
			,change : fnGridClick
		};
		aGrid = $('#aGrid').initializeKendoGrid( aGridOpt ).cKendoGrid();
		aGrid.bind('dataBound', function(){
			$('#totalCnt').text(aGridDs._total);
		});
		//------------------------------- 왼쪽그리드 E -------------------------------
	}
	function fnGridClick(){
		var map = aGrid.dataItem(aGrid.select());
		fnBizCallback("select",map);
	};
	//-------------------------------  Grid End  -------------------------------

	//---------------Initialize Option Box Start ------------------------------------------------
	function fnInitOptionBox(){
		$('#kendoPopup').kendoWindow({
			visible: false,
			modal: true
		});
		fnKendoDropDownList({
			id    : 'conditionType',
			data  : [
				{"CODE":"COMMON_MASTER_NAME"	,"NAME":"마스터코드명"},
				{"CODE":"COMMON_MASTER_CODE"	,"NAME":"마스터코드값"}
			],
			textField :"NAME",
			valueField : "CODE"
		});
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
		fnTagMkRadioYN({id: "intputActive" , tagId : "useYn", chkVal: 'Y'});
	}
	//---------------Initialize Option Box End ------------------------------------------------
	//-------------------------------  Common Function start -------------------------------

	function inputFocus(){
		$('#input2').focus();
	};
	function inputFocus3(){
		$('#input3').focus();
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
				$('#inputForm').bindingForm( {'rows':data},'rows', true);
				fnKendoInputPoup({height:"500px" ,width:"600px",title:{key :"5871",nullMsg :'공통코드마스터 수정' } });
				break;
			case 'insert':
				fnKendoMessage({
					message:"저장되었습니다.",
					ok:function(){
						fnSearch();
						fnClose();
					}
				});
				break;
			case 'update':
					fnKendoMessage({
							message:"수정되었습니다.",
							ok:function(){
								fnSearch();
								fnClose();
							}
					});
				break;
			case 'delete':
				fnKendoMessage({
						message:"삭제되었습니다.",
						ok:function(){
							fnSearch();
							fnClose();
						}
				});
				break;

		}
	}

	var keyCode = {
			ESC : 27,
			ENTER : 13,
			CAPSLOOK : 20,
			SPACE : 32,
			PAGEUP : 33,
			PAGEDN : 34,
			END : 35,
			HOME : 36,
			INSERT : 45,
			DELETE : 46,
			TAB : 9,
			BAKSPACE : 8,
			ARROW_LEFT : 37,
			ARROW_UP : 38,
			ARROW_RIGHT : 39,
			ARROW_DOWN : 40,
			NUMLOCK : 144,
			SCROLLLOCK : 145,
			KOR_ENG : 21,
			CHINESE : 25
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

	// 마스터코드값 입력제한 - 영문대소문자 & 언더바(_)
	fnInputValidationByRegexp("input3", /[^A-Z_]/g);

	//------------------------------- Html 버튼 바인딩  End -------------------------------

}); // document ready - END

