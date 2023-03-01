/**-----------------------------------------------------------------------------
 * description 		 : 도움말 설정
 * @
 * @ 수정일			수정자          수정내용
 * @ ------------------------------------------------------
 * @ 2020.06.02		천혜현          최초생성
 * @ 2020.11.05		ykk           현행화
 * @
 * **/
'use strict';


var PAGE_SIZE = 20;
var aGridDs, aGridOpt, aGrid;
var kEditor = null;
var curFontSize = "14px";
var curFontName = "Arial";
var UPLOAD_DOMAIN = "cs";

$(document).ready(function() {
	fnInitialize();	//Initialize Page Call ---------------------------------

	//Initialize PageR
	function fnInitialize(){
		$scope.$emit('fnIsMenu', { flag : 'true' });

		fnPageInfo({
			PG_ID  : 'help',
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
		$('#fnSearch, #fnNew, #fnSave, #fnClear').kendoButton();
		$('#fnDel').kendoButton({ enable: false });
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
	}

	function fnClear(){
		$('#searchForm').formClear(true);
		$('span#divisionCode input:radio').eq(0).click();
		$('span#useYn input:radio').eq(0).click();
	}
	function fnNew(){
		//aGrid.clearSelection();
		$('#inputForm').formClear(true);
		$('input:radio[name="inputDivisionCode"]:input[value="HELP_DIV.HELP_MENU"]').prop("checked", true);
		$('input:radio[name="inputUseYn"]:input[value="Y"]').prop("checked", true);
		$("label[for='divisionName']").text("메뉴관리");
		fnKendoInputPoup({height:"750px" ,width:"650px",title:{key :"5869",nullMsg :'도움말' } });

		// kendoEditor 디폴트 값 설정
		// kEditor.exec("fontSize", {
		// 	value : curFontSize,
		// });

		// kEditor.exec("fontName", {
		// 	value : curFontName,
		// });

		/* HGRM-2019 - dgyoun : 신규 인 경우 삭제 버튼 숨김 */
		$('#fnDel').hide();
	}
	function fnSave(){
		var url  = '/admin/st/help/addHelp';
		var cbId = 'insert';

		// 수정인 경우
		if( OPER_TP_CODE == 'U' ){
			url  = '/admin/st/help/putHelp';
			cbId= 'update';
		}

		var data = $('#inputForm').formSerialize(true);
		var content = fnTagConvert(data.content).replace(/<(\/)?([a-zA-Z]*)(\s[a-zA-Z]*=[^>]*)?(\s)*(\/)?>/ig, '');
		content = content.replace(/&nbsp;/g,'');

		if( data.rtnValid ){
			if(content.trim() == '') {
				fnKendoMessage({message : '도움말 내용은 필수입니다.'});
			} else {
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

	}
	function fnDel(){
		fnKendoMessage({message:fnGetLangData({key :"4489",nullMsg :'삭제하시겠습니까?' }), type : "confirm" ,ok :fnDelApply  });
	}
	function fnDelApply(){
		var url  = '/admin/st/help/delHelp';
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
			url      : '/admin/st/help/getHelpList',
			//pageSize : PAGE_SIZE
		});
		aGridOpt = {
			dataSource: aGridDs
			,pageable  : {
				pageSizes: [20, 30, 50],
				buttonCount : 5
			}
			,navigatable: true
			//,height:550
			,columns   : [
					 { title: 'No'		,width:'40px', attributes:{ style:'text-align:center' }, template: function (dataItem){return fnKendoGridPagenation(aGrid.dataSource,dataItem);} }
					,{ field:'id'		,title : 'SEQ'	, width:'50px',attributes:{ style:'text-align:center' }}
					,{ field:'title'	,title : '도움말 제목'	, width:'100px',attributes:{ style:'text-align:center' }}
					/*,{ field:'content' 	 		,title : '도움말 내용'	, width:'150px',attributes:{ style:'text-align:left' }
					 	,	template : function(dataItem){
					 			let data = fnTagConvert(dataItem.content).replace(/<(\/)?([a-zA-Z]*)(\s[a-zA-Z]*=[^>]*)?(\s)*(\/)?>/ig, '');
			                  	return data;
					 		}}*/
					,{ field:'contentPlain' 	,title : '도움말 내용'	, width:'150px',attributes:{ style:'text-align:left' }}
					,{ field:'divisionCodeName'	,title : '구분'		, width:'60px',attributes:{ style:'text-align:center' }}
					,{ field:'useYn'	 		,title : '사용여부'	, width:'60px',attributes:{ style:'text-align:center' }
						,	template : "#=(useYn=='Y')?'예':'아니오'#"}
					,{ field:'createDate'		,title : '등록일'		, width:'65px',attributes:{ style:'text-align:center' }}
					,{ field:'modifyDate' 	 	,title : '수정일'		, width:'65px',attributes:{ style:'text-align:center' }}
			],
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
		var map = aGrid.dataItem(aGrid.select());

		if(map !=null){
			fnAjax({
				url     : '/admin/st/help/getHelp',
				params  : {id : map.id},
				success :
					function( data ){

						fnBizCallback("select",data);

						if(data.rows.inputDivisionCode == 'HELP_DIV.HELP_ST'){
							$("label[for='divisionName']").text("표준용어");
						}else{
							$("label[for='divisionName']").text("메뉴관리");
						}

					},
				isAction : 'select'
			});
		}
	};

	function fnPopupButton( param ){

		if($('form#inputForm input[name=inputDivisionCode]:checked').val() == 'HELP_DIV.HELP_ST'){
			fnKendoPopup({
				id     : 'dicMstPopup',
				title  : fnGetLangData({key :"4363",nullMsg :'표준용어' }),
				src    : '#/dicMstPopup',
				width  : '860px',
				height : '600px',
				param  : { "MENU_TYPE" : "help"},
				success: function( id, data ){
					if(data.id){
						$('#inputDivisionId').val(data.id);
						$('#inputDivisionName').val(data.baseName);
					}
				}
			});
			/* HGRM-2019 - dgyoun : 상세보기 인 경우 삭제 버튼 노출 */
			$('#fnDel').show();
		}else{
			/*fnKendoPopup({
				id     : 'MENU_POP',
				title  : '상위메뉴관리',
				src    : '#/menu001',
				width  : '760px',
				height : '530px',
				param  : { "ST_MENU_GRP_ID" : $('#inputMENU_GROUP').val()},
				success: function( id, data ){
					if(data.ST_MENU_ID){
						$('#inputDivisionId').val(data.ST_MENU_ID);
						$('#inputDivisionName').val(data.MENU_NAME);
					}
				}
			});*/
			fnKendoPopup({
				id     : 'menuPopup',
				title  : '메뉴관리',
				src    : '#/menuPopup',
				width  : '850px',
				height : '600px',
				param  : { "stMenuGroupId" : $('#inputMenuGroup').val()},
				success: function( stMenuId, data ){
					if(data.stMenuId){
						$('#inputDivisionName').val(data.menuName);
						$('#inputDivisionId').val(data.stMenuId);
					}
				}
			});
		}
	};
	//-------------------------------  Grid End  -------------------------------

	//---------------Initialize Option Box Start ------------------------------------------------
	function fnInitOptionBox(){
		$('#kendoPopup').kendoWindow({
			visible: false,
			modal: true
		});

		fnTagMkRadio({
			id    : 'useYn',
			tagId : 'useYn',
			chkVal: '',
			data  : [   { "CODE" : ""	, "NAME":'전체' },
						{ "CODE" : "Y"	, "NAME":'예' },
						{ "CODE" : "N"	, "NAME":'아니오' }
					],
			style : {}
		});

		fnTagMkRadio({
			id    :  'divisionCode',
			tagId : 'divisionCode',
			url   : "/admin/comn/getCodeList",
			params : {"stCommonCodeMasterCode" : "HELP_DIV", "useYn" :"Y"},
			beforeData : [
				{"CODE":"", "NAME":"전체"},
			],
			chkVal: "",
			async : false,
			style : {}
		});

		fnKendoDropDownList({
			id  : 'conditionType',
			data  : [
				{"CODE":"CONTENT"	,"NAME": '도움말 내용' },
				{"CODE":"TITLE"		,"NAME": '도움말 제목' }
			],
			textField :"NAME",
			valueField : "CODE"
		});

		fnTagMkRadio({
			id    : 'inputDivisionCode',
			tagId : 'inputDivisionCode',
			url   : "/admin/comn/getCodeList",
			params : {"stCommonCodeMasterCode" : "HELP_DIV", "useYn" :"Y"},
			async : false,
			chkVal: 'HELP_MENU',
			style : {}
		});


		$('form#inputForm input[name=inputDivisionCode]').bind('change',function(){
			$("#inputDivisionName").val('');
			if($('form#inputForm input[name=inputDivisionCode]:checked').val() == 'HELP_DIV.HELP_ST'){
				$("label[for='divisionName']").text("표준용어");
			}else{
				$("label[for='divisionName']").text("메뉴관리");
			}
		});

		fnTagMkRadioYN({id: "inputUseYn" , tagId : "inputUseYn",chkVal: 'Y'});

		// fnKendoEditor( {id : 'content','domain' :'cs'} );
		kEditor = $("#content").initializeKendoEditor({
			// kendo Editor 파일 업로드용 domain 명 지정
			domain : UPLOAD_DOMAIN,
		}).cKendoEditor();

	}

	//---------------Initialize Option Box End ------------------------------------------------
	//-------------------------------  Common Function start -------------------------------

	function inputFocus(){
		$("#title").focus();
	};
	function condiFocus(){
	};

		/**
		* 콜백합수
		*/
	function fnBizCallback( id, data ){
		switch(id){
			case 'select':
				//form data binding
				$('#inputForm').bindingForm( {'rows':data.rows},'rows', true);
				fnKendoInputPoup({height:"750px" ,width:"650px",title:{key :"5869",nullMsg :'도움말' } });
				break;
			case 'insert':
				fnKendoMessage({message : '저장되었습니다.' ,ok :function(){
					fnClose();
					fnSearch();
				}});
				break;
			case 'save':
				fnKendoMessage({message : '저장되었습니다.'});
				break;
			case 'update':
				fnKendoMessage({message : '수정되었습니다.' ,ok :function(){
					fnClose();
					fnSearch();
				}});
				break;
			case 'delete':
				fnKendoMessage({message : '삭제되었습니다.' ,ok :function(){
					fnClose();
					fnSearch();
				}});
				break;
		}
	}

	//-------------------------------  Common Function end -------------------------------


	//------------------------------- Html 버튼 바인딩  Start -------------------------------
	/** Common Search*/
	$scope.fnSearch = function( ) {	fnSearch();};
	/** Common Clear*/
	$scope.fnClear =function(){	 fnClear();};
	/** Common New*/
	$scope.fnNew = function( ){	fnNew();};
	/** Common Save*/
	$scope.fnSave = function(){	 fnSave();};
	/** Common Delete*/
	$scope.fnDel = function(){	 fnDel();};
	/** Common Close*/
	$scope.fnClose = function( ){  fnClose();};

	$scope.fnPopupButton = function( param ){ fnPopupButton(param); };
	//------------------------------- Html 버튼 바인딩  End -------------------------------

}); // document ready - END
