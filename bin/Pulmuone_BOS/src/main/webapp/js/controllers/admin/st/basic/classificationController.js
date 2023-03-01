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
		$scope.$emit('fnIsMenu', { flag : 'true' });

		fnPageInfo({
			PG_ID  : 'classification',
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
		$('#fnSearch, #fnNew,#fnSave,  #fnClear').kendoButton();
		$('#fnDel').kendoButton({ enable: false });
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
	function fnClear(){
		$('#searchForm').formClear(false);
		$("span#condiActive input:radio").eq(0).click();	//radio init
	}
	function fnNew(){
		aGrid.clearSelection();
		$('#inputForm').formClear(true);
		inputFocus();
		fnKendoInputPoup({height:"500px" ,width:"500px",title:{key :"5867",nullMsg :'분류정보 등록' } });
	}
	function fnSave(){
		var url  = '/admin/st/basic/addClassification';
		var cbId = 'insert';

		if( OPER_TP_CODE == 'U' ){
			url  = '/admin/st/basic/putClassification';
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
		fnKendoMessage({message:'삭제 하시겠습니까?', type : "confirm" ,ok :fnDelApply  });
	}
	function fnDelApply(){
		var url  = '/admin/st/basic/delClassification';
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
			}
			,columns   : [
				{ title : 'No', width:'40px', attributes:{ style:'text-align:center' }, template: function (dataItem){return fnKendoGridPagenation(aGrid.dataSource,dataItem);} }
				,{ field:'type'			,title : '분류코드'	, width:'130px',attributes:{ style:'text-align:center' }}
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
		aGrid.bind('dataBound', function(){
			$('#totalCnt').text(aGridDs._total);
		});
		$("#aGrid").on("click", "tbody>tr", function () {
				fnGridClick();
		});
	}
	function fnGridClick(){
		var aMap = aGrid.dataItem(aGrid.select());
		console.log('fnGridClick.aMap', aMap);
		fnAjax({
			url     : '/admin/st/basic/getClassification',
			params  : {stClassificationId : aMap.stClassificationId},
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
		fnTagMkRadio({
			id    :  'condiActive',
			tagId : 'useYn',
			chkVal: '',
			data  : [   { "CODE" : ""	, "NAME":'전체' },
						{ "CODE" : "Y"	, "NAME":'예' },
						{ "CODE" : "N"	, "NAME":'아니오' }
					],
		});

		fnTagMkRadioYN({id: "intputActive" , tagId : "useYn",chkVal: 'Y'});
		$('#depth').forbizMaskTextBox({fn:'onlyNum'});
		$('#sort').forbizMaskTextBox({fn:'onlyNum'});

		fnKendoDropDownList({
			id    : 'typeValue',
			url : "/admin/st/basic/getTypeList",
			params : {},
			textField :"typeName",
			valueField : "typeId",
			blank : "전체"
		});

		fnKendoDropDownList({
			id    : 'typePop',
			url : "/admin/st/basic/getTypeList",
			params : {},
			textField :"typeName",
			valueField : "typeId",
			blank : "선택해주세요. "
		});

	}
	//---------------Initialize Option Box End ------------------------------------------------

	//-------------------------------  UI User Function start -------------------------------

	// 마스터코드값 입력제한 - 영문대소문자 & 언더바(_)
	fnInputValidationForAlphabetNumberUnderbar("typeS");
	fnInputValidationForAlphabetNumberUnderbar("type");

	//-------------------------------  UI User Function start -------------------------------

	//-------------------------------  Common Function start -------------------------------

	function inputFocus(){
		$('#type').focus();
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
				$('#inputForm').bindingForm(data, 'rows', true);
				fnKendoInputPoup({height:"500px" ,width:"500px",title:{key :"5868",nullMsg :'분류정보 수정' } });
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
					message:"저장되었습니다.",
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
	function fnPopupButton( param ){
		fnKendoPopup({
				id     : 'classificationPopup',
				title  : '분류관리',
				src    : '#/classificationPopup',
				width  : '750px',
				height : '600px',
				success: function( id, data ){
					if(data.gbDictionaryMasterId){
						$('#parentsClassificationId').val(data.id);
						$('#parentsName').val(data.typeName);
					}
				}
			});
	};
	function fnPopupDicButton( param ){
		fnKendoPopup({
				id     : 'dicMstPopup',
				title  : '표준용어',
				src    : '#/dicMstPopup',
				width  : '900px',
				height : '600px',
				param  : { "MENU_TYPE" : "word"},
				success: function( id, data ){
					if(data.id){
						$('#input6').val(data.id);
						$('#baseName').val(data.baseName);
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

	$scope.fnPopupDicButton = function( param ){ fnPopupDicButton(param); };
	$scope.fnPopupButton = function( param ){ fnPopupButton(param); };
	//------------------------------- Html 버튼 바인딩  End -------------------------------

}); // document ready - END
