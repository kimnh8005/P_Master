/**-----------------------------------------------------------------------------
 * @
 * @ 수정일			수정자          수정내용
 * @ ------------------------------------------------------
 * @ 2017.01.10		최봉석          최초생성
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
			PG_ID  : 'menuGrp',
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
		$('#fnSearch, #fnNew,#fnSave,  #fnClear').kendoButton();
		$('#fnDel').kendoButton({ enable: false });
	}
		function fnSearch(){
		$('#inputForm').formClear(false);
		var data;
		data = $('#searchForm').formSerialize(true);
		//aGridDs.read(data);
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
		fnKendoInputPoup({height:"400px" ,width:"800px",title:{key :"5874",nullMsg :'메뉴그룹관리 등록' } });
	}
	function fnSave(){
		var url  = '/admin/st/menu/addMenuGroup';
		var cbId = 'insert';

		if( OPER_TP_CODE == 'U' ){
			url  = '/admin/st/menu/putMenuGroup';
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
					},
					isAction : 'batch'
			});
		}else{

		}
	}
	function fnDel(){
		//fnKendoMessage({message:fnGetLangData({key :"4489",nullMsg :'삭제 하시겠습니까?' }), type : "confirm" ,ok :fnDelApply  });
		fnKendoMessage({message:'삭제 하시겠습니까?', type : "confirm" ,ok :fnDelApply  });
	}
	function fnDelApply(){
		var url  = '/admin/st/menu/delMenuGroup' ;
		var cbId = 'delete';
		var data = $('#inputForm').formSerialize(true);
		console.dir("data====>"+JSON.stringify(data));

		var map = aGrid.dataItem(aGrid.select());
		console.dir("map====>"+JSON.stringify(map));
		console.dir("url====>"+url);

		console.dir("url====>"+url);

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

	}

	//--------------------------------- Button End---------------------------------


	//------------------------------- Grid Start -------------------------------
	function fnInitGrid(){
		aGridDs = fnGetPagingDataSource({
			url      : 	'/admin/st/menu/getMenuGroupList',
			pageSize : PAGE_SIZE
		});
		aGridOpt = {
			dataSource: aGridDs
			,  pageable  : {
				pageSizes: [20, 30, 50],
				buttonCount : 5
			}
			,navigatable: true
//			        ,height:550
			,columns   : [
				 { title : 'No', width:'50px', attributes:{ style:'text-align:center' }, template: function (dataItem){return fnKendoGridPagenation(aGrid.dataSource,dataItem);} }
				,{ field:'menuGroupName'	,title : '메뉴그룹명'			, width:'150px'	,attributes:{ style:'text-align:center' }}
				,{ field:'programName'			,title : '기본호출 프로그램명'	, width:'150px'	,attributes:{ style:'text-align:center' }}
				,{ field:'sort'				,title : '정렬'			, width:'50px'	,attributes:{ style:'text-align:center' }}
				,{ field:'useYn'			,title : '사용여부'			, width:'80px',attributes:{ style:'text-align:center' }
				,template : "#=(useYn=='Y')?fnGetLangData({nullMsg :'예' }):fnGetLangData({nullMsg :'아니오' })#"
				}
			]
		};
		aGrid = $('#aGrid').initializeKendoGrid( aGridOpt ).cKendoGrid();
			$("#aGrid").on("click", "tbody>tr", function () {
				fnGridClick();
		});

		aGrid.bind('dataBound', function(){
			$('#totalCnt').text(aGridDs._total);
		});
	}
	function fnGridClick(){
		var aMap = aGrid.dataItem(aGrid.select());
		fnAjax({
			url     : '/admin/st/menu/getMenuGroup',
			params  : {'stMenuGroupId' : aMap.stMenuGroupId},
			success :
				function( data ){
					fnBizCallback("select",data);
				},
			isAction : 'select'
		});
	};
	//-------------------------------  Grid End  -------------------------------

	//---------------Initialize Option Box Start ------------------------------------------------
	function fnInitOptionBox(){
		$('#kendoPopup').kendoWindow({
			visible: false,
			modal: true
		});
		//fnTagMkRadioYN({id: "condiActive" , tagId : "USE_YN",chkVal: 'Y'});
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
		fnTagMkRadioYN({id: "intputActive" , tagId : "inputUseYn",chkVal: 'Y'});// 팝업에서 쓰는 값 셋팅
		$('#sort').forbizMaskTextBox({fn:'onlyNum'});

	}

	//---------------Initialize Option Box End ------------------------------------------------
	//-------------------------------  Common Function start -------------------------------

	function inputFocus(){
		$('#input1').focus();
	};
		function condiFocus(){
		$('#condition1').focus();
	};
	function fnPopupButton( param ){
		fnKendoPopup({
			id     : 'SAMPLE_POP',
			title  : '프로그램',
			src    : '#/pgmPopup',
			width  : '750px',
			height : '650px',
			success: function( id, data ){
				if(param =="search"){
					if(data.programId){
						$('#inputStProgramId').val(data.stProgramId);
					}
				}else{
					if(data.programId){
						$('#inputStProgramId').val(data.stProgramId);
						$('#inputProgramName').val(data.programName);
					}
				}
			}
		});
	};
	function fnDicPopupButton( param ){
		fnKendoPopup({
			id     : 'SAMPLE_POP',
			title  : fnGetLangData({key :"4620",nullMsg :'표준용어 팝업' }),
			src    : '#/dicMstPopup',
			param  : { "MENU_TYPE" : "word"},
			width  : '680px',
			height : '585px',
			success: function( id, data ){
				if(data.id){
					$('#input6').val(data.id);
					$('#BASE_NAME').val(data.baseName);
				}
			}
		});
	};
		/**
		* 콜백합수
		*/
	function fnBizCallback( id, data ){
		switch(id){
			case 'select':
				//form data binding
				$('#inputForm').bindingForm(data, 'rows', true);
				fnKendoInputPoup({height:"400px" ,width:"800px",title:{key :"5873",nullMsg :'메뉴그룹관리 수정' } });
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
				fnKendoMessage({message : '삭제되었습니다.',
					ok:function(){
						fnSearch();
						fnClose();
						}
				});
				break;

		}
	}

	function fnClose(){
		var kendoWindow =$('#kendoPopup').data('kendoWindow');
		kendoWindow.close();
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

	$scope.fnDicPopupButton = function( param ){ fnDicPopupButton(param); };
	//------------------------------- Html 버튼 바인딩  End -------------------------------

	//------------------------------- validation Start -------------------------------
	fnInputValidationByRegexp("inputSort", /[^0-9]/g);
	//------------------------------- validation End -------------------------------

}); // document ready - END

