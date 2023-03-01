/**-----------------------------------------------------------------------------
 * description 		 : 시스템 - 표준용어사전
 * @
 * @ 수정일			수정자          수정내용
 * @ ------------------------------------------------------
 * @ 2020.06.05		천혜현          최초생성
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
			PG_ID    : 'dicMst',
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
	}
	function fnSearch(){
		$('#inputForm').formClear(true);
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
		condiFocus();
	}
	function fnClear(){
		$('#searchForm').formClear(true);
		$('span#dictionaryType input:radio').eq(0).click();
	}
	function fnNew(){
		aGrid.clearSelection();
		$('#inputForm').formClear(true);
		$("form#inputForm input[name=id]").attr('disabled',false);
		$('input:radio[name="inputDictionaryType"]:input[value="1"]').prop("checked", true);
		$("form#inputForm input[name=inputDictionaryType]").attr('disabled',false);
		inputFocus();
		fnKendoInputPoup({height:"410px" ,width:"550px",title:{key :"5967",nullMsg :'표준용어 등록' } });
	}
	function fnSave(){
		var url  = '/admin/system/help/nation/saveDictionaryMaster';
		var cbId = 'insert';
		var isAction = 'batch';

		if( OPER_TP_CODE == 'U' ){
			url  = '/admin/system/help/nation/updateDictionaryMaster';
			cbId= 'update';
			isAction = 'batch';
		}
		var data = $('#inputForm').formSerialize(true);
		data.operatorTypeCode = OPER_TP_CODE;
		if( data.rtnValid ){
			fnAjax({
				url     : url,
				params  : data,
				success :
					function( data ){
						fnBizCallback(cbId, data);
					},
				isAction : isAction
			});
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
			url      : '/admin/system/help/nation/getDictionaryMasterList',
			pageSize : PAGE_SIZE
		});

		aGridOpt = {
			dataSource: aGridDs
			,  pageable  : {
				pageSizes: [20, 30, 50],
				buttonCount : 10
			}
			,navigatable: true
			,columns   : [
					{ title : 'No', width:'10%', attributes:{ style:'text-align:center' }, template: function (dataItem){return fnKendoGridPagenation(aGrid.dataSource,dataItem);} }
					,{ field:'id'			,title : 'SEQ'		, width:'15%'	,attributes:{ style:'text-align:center' }}
					,{ field:'dictionaryType'	,title : '표준용어 구분'	, width:'20%'	,attributes:{ style:'text-align:center' }}
					,{ field:'baseName'			,title : '표준용어'		, width:'55%'	,attributes:{ style:'text-align:left' }}
			]
		};
		aGrid = $('#aGrid').initializeKendoGrid( aGridOpt ).cKendoGrid();
		aGrid.bind('dataBound', function(){
			$('#totalCnt').text(aGridDs._total);
		});

		$("#aGrid").on("click", "tbody>tr", function () {
				fnGridClick();
		});


	}
	function fnGridClick(){
		var aMap = aGrid.dataItem(aGrid.select());
		fnAjax({
			url     : '/admin/system/help/nation/getDictionaryMaster',
			params  : {id : aMap.id},
			success :
				function( data ){
					fnBizCallback("select",data);
					$("form#inputForm input[name=id]").attr('disabled',true);
					$("form#inputForm input[name=inputDictionaryType]").attr('disabled',true);
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

		fnTagMkRadio({
			id    :  'inputDictionaryType',
			tagId : 'inputDictionaryType',
			url   : "/admin/comn/getCodeList",
			params : {"stCommonCodeMasterCode" : "DIC_TP", "useYn" :"Y"},
			chkVal : "DIC_TP.1",
			async : false
		});

        // 입력 제어
		fnInputValidationForNumber("id"); // Seq
	}

	//---------------Initialize Option Box End ------------------------------------------------
	//-------------------------------  Common Function start -------------------------------

	function inputFocus(){
		$('#input4').focus();
	};

	function condiFocus(){
		$('#condition5').focus();
	};

	/**
	 * 콜백합수
	 */
	function fnBizCallback( id, data ){
		switch(id){
			case 'select':
				//form data binding
				$('#inputForm').bindingForm(data, 'rows', true);
				fnKendoInputPoup({height:"410px" ,width:"550px",title:{key :"5967",nullMsg :'표준용어 등록' } });
				break;
			case 'insert':
				fnKendoMessage({message : '저장되었습니다.' ,ok :function(){
					fnClose();
					fnSearch();
				}});
				break;
			case 'save':
				fnKendoMessage({message : fnGetLangData({key :"368",nullMsg :'저장되었습니다.' })});
				break;
			case 'update':
				fnKendoMessage({message : '저장되었습니다.' ,ok :function(){
					fnClose();
					fnSearch();
				}});
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
	//$scope.fnDel = function(){	 fnDel();};
	/** Common Close*/
	$scope.fnClose = function( ){  fnClose();};

	//마스터코드값 입력제한 - 영문대소문자 & 숫자
	fnInputValidationByRegexp("seq", /[^0-9]/g);

	//------------------------------- Html 버튼 바인딩  End -------------------------------

}); // document ready - END

