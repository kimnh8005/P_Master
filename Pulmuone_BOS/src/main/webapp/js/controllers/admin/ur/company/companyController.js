/**-----------------------------------------------------------------------------
 * description 		 : 프로그램관리
 * @
 * @ 수정일			수정자          수정내용
 * @ ------------------------------------------------------
 * @ 2016.12.23		최봉석          최초생성
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
			PG_ID  : 'pgm',
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
		$('#fnSearch, #fnNew,#fnSave,  #fnClear, #fnClose').kendoButton();
		$('#fnDel').kendoButton({ enable: false });
	}
		function fnSearch(){
		$('#inputForm').formClear(false);
		var data;
		data = $('#searchForm').formSerialize(true);
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
		$("span#condiActive input:radio").eq(0).click();
	}
	function fnNew(){
		//aGrid.clearSelection();
		$('#inputForm').formClear(true);
		inputFocus();
		$('#input3').val("/");
		$('#htmlPath').val("/");
		fnKendoInputPoup({height:"375px" ,width:"400px", title:{ nullMsg :'프로그램정보 등록' } });
	}
	function fnSave(){


		var url  = '/admin/st/pgm/addProgram';
		var cbId = 'insert';

		if( OPER_TP_CODE == 'U' ){
			url  = '/admin/st/pgm/putProgram';
			cbId= 'update';
		}
		var data = $('#inputForm').formSerialize(true);

//     	if (data.stProgramId =="" ||data.programName ==""){
//			fnKendoMessage({message : fnGetLangData({key :"6700",nullMsg :'프로그램명은 필수 입력입니다' }) ,ok : function(e){}});
//			return;
//		}
		if( data.rtnValid ){
			fnAjax({
				url     : url,
				params  : data,
				success :
					function( data ){
						fnBizCallback(cbId, data);
					},
					isAction : 'batch'
			});
		}else{

		}
	}
	function fnDel(){
		fnKendoMessage({message:'삭제 하시겠습니까?', type : "confirm" ,ok :fnDelApply, cancel:inputFocus  });
	}
	function fnDelApply(){
		var url  = '/admin/st/pgm/delProgram';
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
			url      : "/admin/st/pgm/getProgramList",
			pageSize : PAGE_SIZE
		});
		aGridOpt = {
			dataSource: aGridDs
			,  pageable  : {
				pageSizes: [20, 30, 50],
				buttonCount : 5
			}
			,navigatable: true
			//,height:550
			,columns   : [
				{ field:'stProgramId'			,title : 'NO'	, width:'40px',attributes:{ style:'text-align:center' }}
				,{ field:'programId'			,title : '프로그램 ID'	, width:'120px',attributes:{ style:'text-align:center' }}
				,{ field:'programName'		,title : '프로그램명'		, width:'210px',attributes:{ style:'text-align:center' }}
				,{ field:'url'			,title : 'URL'		, width:'120px',attributes:{ style:'text-align:center' }}
				,{ field:'htmlPath'	,title : '파일경로'		, width:'120px',attributes:{ style:'text-align:center' }}
				,{ field:'useYn'		,title : '사용여부'		, width:'80px',attributes:{ style:'text-align:center' }
				,template : "#=(useYn=='Y')?fnGetLangData({nullMsg :'예' }):fnGetLangData({nullMsg :'아니오' })#"
				}
			]
		};
		aGrid = $('#aGrid').initializeKendoGrid( aGridOpt ).cKendoGrid();
			$("#aGrid").on("click", "tbody>tr", function () {
				fnGridClick();
		});
	}
	function fnGridClick(){
		var aMap = aGrid.dataItem(aGrid.select());
		fnAjax({
			url     : '/admin/st/pgm/getProgram',
			params  : {stProgramId : aMap.stProgramId},
			success :
				function( data ){
					fnBizCallback("select",data);
				},
			isAction : 'select'
		});
	};

	function fnPopupButton( param ){
		fnKendoPopup({
			id     : 'SAMPLE_POP',
			title  : fnGetLangData({key :"4620",nullMsg :'표준용어 팝업' }),
			src    : '#/dicMstPopup',
			width  : '680px',
			height : '585px',
			param  : { "MENU_TYPE" : "word"},
			success: function( id, data ){
				if(data.id){
					$('#input6').val(data.id);
					$('#baseName').val(data.baseName);
				}
			}
		});
	};

	//-------------------------------  Grid End  -------------------------------

	//---------------Initialize Option Box Start ------------------------------------------------
	function fnInitOptionBox(){
		$('#kendoPopup').kendoWindow({
			visible: false,
			modal: true
		});
//		fnTagMkRadioYN({id: "condiActive" , tagId : "useYn",chkVal: 'Y'});

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
		fnTagMkRadioYN({id: "intputActive" , tagId : "useYn",chkVal: 'Y'});

		fnKendoDropDownList({
			id    : 'input4',
			/*url	: "/comn/st/getCodeList",
			params : {"ST_COMN_CODE_MST_ID" : "1"},
			textField :"NAME",
			valueField : "CODE"*/
			url : "/admin/comn/getCodeList",
			params : {"stCommonCodeMasterCode" : "1", "useYn" :"Y"},
			textField :"NAME",
			valueField : "CODE"
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
				$('#inputForm').bindingForm(data, "rows", true);
				fnKendoInputPoup({height:"375px" ,width:"400px",title:{key :"5876",nullMsg :'프로그램정보 수정' } });
				break;
			case 'insert':
			case 'update':
				fnKendoMessage({
						message:"저장되었습니다.",
						ok:function(){
							fnSearch();
							fnClose();
						}
				});
			break;
			case 'delete':
				fnKendoMessage({message : '삭제되었습니다.',
						ok:function(){
							fnSearch();
							fnClose();
							}
				});
				break;

		}
	}

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

	$scope.fnPopupButton = function( param ){ fnPopupButton(param); };

	//마스터코드값 입력제한 - 영문대소문자 & 숫자
	fnInputValidationByRegexp("input1", /[^a-z^A-Z^0-9]/g);

	//마스터코드값 입력제한 - 영문대소문자 & /
	fnInputValidationByRegexp("htmlPath", /[^a-z^A-Z^/]/g);

	//------------------------------- Html 버튼 바인딩  End -------------------------------

}); // document ready - END
