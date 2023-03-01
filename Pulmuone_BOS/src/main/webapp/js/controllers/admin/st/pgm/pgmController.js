/**-----------------------------------------------------------------------------
 * description 		 : 프로그램관리
 * @
 * @ 수정일			수정자          수정내용
 * @ ------------------------------------------------------
 * @ 2016.12.23		최봉석          최초생성
 * @ 2020.12.03		최성현			url, 파일경로 수정
 * @
 * **/
'use strict';


var PAGE_SIZE = 20;
var aGridDs, aGridOpt, aGrid;
var authGridDs, authGridOpt, authGrid;

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
		authGridDs.data([ {
			programAuthCode : 'DEFAULT',
			programAuthCodeName : '기본',
			useYn: 'Y'
		} ]);
		fnKendoInputPoup({height:"650PX" ,width:"600px", title:{ nullMsg :'프로그램정보 등록' } });
	}
	function fnSave(){

		var url  = '/admin/st/pgm/addProgram';
		var cbId = 'insert';

		if( OPER_TP_CODE == 'U' ){
			url  = '/admin/st/pgm/putProgram';
			cbId= 'update';
		}

		var programAuthCodeList = [];
		$.each(authGridDs.data(), function (key, data){
			programAuthCodeList.push(data.programAuthCode);
		});

		var uniqProgramAuthCodeList = programAuthCodeList.reduce(function(a,b){
			if (a.indexOf(b) < 0 ) a.push(b);
			return a;
		  },[]);

		if(uniqProgramAuthCodeList.length != programAuthCodeList.length){
			fnKendoMessage({message:'중복되는 권한코드가 있습니다. 재확인 후 다시 시도 바랍니다.', type : "alert"});
			return false;
		}

		var data = $('#inputForm').formSerialize(true);

		data.url = domainsplit(data.url);
		data.htmlPath = domainsplit(data.htmlPath);

		data.authInsertData = kendo.stringify(fnEGridDsExtract('authGrid', 'insert'));
		data.authUpdateData = kendo.stringify(fnEGridDsExtract('authGrid', 'update'));
//		data.authDeleteData = fnEGridDsExtract('authGrid', 'delete');

		var authInsertData = fnEGridDsExtract('authGrid', 'insert');
		var authUpdateData = fnEGridDsExtract('authGrid', 'update');
		var checkDataList = authInsertData.concat(authUpdateData);

		var isSubmit = true;
		if(checkDataList.length > 0){
			$.each(checkDataList, function (i, data){
				// 권한코드, 권한코드명 필수체크
				if(data.programAuthCode == ''){
					fnKendoMessage({message : "권한코드를 입력해주세요." });
					isSubmit = false;
					return false;
				}
				if(data.programAuthCodeName == ''){
					fnKendoMessage({message :  "권한코드명을 입력해주세요." });
					isSubmit = false;
					return false;
				}

			});
		}

		if( data.rtnValid && isSubmit){
			fnAjax({
				url     : url,
				params  : data,
				success :
					function( data ){
						fnBizCallback(cbId, data);
					},
					isAction : 'batch'
			});
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
				{ title : 'No', width:'40px', attributes:{ style:'text-align:center' }, template: function (dataItem){return fnKendoGridPagenation(aGrid.dataSource,dataItem);} }
				,{ field:'programId'			,title : '프로그램 ID'	, width:'120px',attributes:{ style:'text-align:left' }}
				,{ field:'programName'		,title : '프로그램명'		, width:'210px',attributes:{ style:'text-align:left' }}
				,{ field:'url'			,title : 'URL'		, width:'120px',attributes:{ style:'text-align:left' }}
				,{ field:'htmlPath'	,title : '파일경로'		, width:'120px',attributes:{ style:'text-align:left' }}
				,{ field:'useYn'		,title : '사용여부'		, width:'80px',attributes:{ style:'text-align:center' }
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

		//권한관련 그리드
		authGridDs = fnGetEditDataSource({
			url      : "",
			model_id     : 'stProgramAuthId',
			model_fields : {
				stProgramAuthId		: { editable: false	, type: 'number', validation: { required: false  } }
				,programAuthCode	: { editable: true	, type: 'string', validation: { required: false, maxLength:"30" } }
				,programAuthCodeName 		: { editable: true	, type: 'string', validation: { required: false, maxLength:"255" }}
				,programAuthCodeNameMemo	: { editable: true	, type: 'string', validation: { required: false, maxLength:"255" }}
				,useYn					: { editable: false	, type: 'string', validation: { required: true  }	, defaultValue : 'Y' }
			}
		});
		authGridOpt = {
			dataSource: authGridDs,
			toolbar   :[
				{ name: 'create', text: '신규 입력 생성', className: "btn-point btn-s"},
				{ name: 'cancel', text: '취소', className: "btn-white btn-s" }
			],
			editable:{confirmation: function(model) {
		        return model.programAuthCodeName+' 삭제하겠습니까?'
		    }},
			height: 200,
			scrollable: true,
			columns   : [
							{ field: 'programAuthCode'		, title: '권한코드'	, width: '140px', attributes:{ style:'text-align:left' }, validation: { required: true, maxLength:"30" }}
							,{ field: 'programAuthCodeName'		, title: '권한코드명'		, width: '140Px' , attributes:{ style:'text-align:left' }, validation: { required: true, maxLength:"255" }}
							,{ field: 'programAuthCodeNameMemo'			, title: '메모'	, width: '110px', attributes:{ style:'text-align:left' }, validation: { required: false, maxLength:"255" }}
							,{ field: 'useYn'			, title: '사용여부'	, width: '60px', attributes:{ style:'text-align:center'}, template : '<input class="useYnInput" type="checkbox" name="useYn" #= useYn == "Y" ? checked="checked" : "" #/>'}
						]
		    //,forbizEditCustm : fnEditCustm
		};
		authGrid = $('#authGrid').initializeKendoGrid( authGridOpt ).cKendoGrid();
		authGrid.bind('dataBound', function(){
			$('.useYnInput').unbind('change').on('change', function(e){
				var dataItem = authGrid.dataItem($(e.target).closest('tr'));
				dataItem.useYn = $(this).is(':checked') ? 'Y' : 'N';
				dataItem.dirty = true;
			});
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

	function domainsplit(str){

		str = str.trim();

		str = str.replace(/^\/+/,"");

		return "/" + str;
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
				authGridDs.data(data.authList);
				fnKendoInputPoup({height:"650px" ,width:"600px",title:{key :"5876",nullMsg :'프로그램정보 수정' } });
				break;
			case 'insert':
				fnKendoMessage({
					message:"등록되었습니다.",
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
	fnInputValidationByRegexp("input1", /[^a-zA-Z0-9]/g);

	//마스터코드값 입력제한 - 영문대소문자 & /
	fnInputValidationByRegexp("htmlPath", /[^a-zA-Z/]/g);

	//------------------------------- Html 버튼 바인딩  End -------------------------------

}); // document ready - END
