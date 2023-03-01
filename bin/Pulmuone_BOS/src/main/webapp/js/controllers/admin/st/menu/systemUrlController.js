/**-----------------------------------------------------------------------------
 * description 		 : 시스템 > 시스템 URL 관리
 * @
 * @ 수정일			수정자          수정내용
 * @ ------------------------------------------------------
 * @ 2018.05.15		choo   최초생성
 * @ 2020.11.10     최성현   시스템 URL 저장 변경
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
			PG_ID    : 'systemUrl',
			callback : fnUI
		});
	}

	function fnUI(){

		fnInitButton();	//Initialize Button  ---------------------------------

		fnInitGrid();	//Initialize Grid ------------------------------------

		fnInitOptionBox();//Initialize Option Box ------------------------------------

		fnSearch();

		replaceChar();
	}

	//--------------------------------- Button Start---------------------------------

	function fnInitButton(){
		$('#fnSearch, #fnNew, #fnSave, #fnClear').kendoButton();
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
	}
	function fnNew(){
		aGrid.clearSelection();
		$('#inputForm').formClear(true);
		fnFormPopup();
	}

	function fnFormPopup(){
		fnKendoInputPoup({height:'400px', width:'550px', title:{key : 'XXXX', nullMsg : '시스템 URL'}});
		$('#URL').focus();
	}

	function fnSave(){
		var url  = '/admin/st/menu/addSystemUrl';
		var cbId = 'insert';
		var isAction = 'insert';

		if( OPER_TP_CODE == 'U' ){
			url  = '/admin/st/menu/putSystemUrl';
			cbId = 'update';
			isAction = 'update';
		}
		var data = $('#inputForm').formSerialize(true);

		if( data.rtnValid ){
			fnAjax({
				url     : url,
				params  : data,
				success :
					function(){
						fnBizCallback(cbId, data);
					},
				isAction : isAction
			});
		}
	}



	function fnClose(){}

	//--------------------------------- Button End---------------------------------


	//------------------------------- Grid Start -------------------------------
	function fnInitGrid(){

		aGridDs = fnGetPagingDataSource({
			url      : '/admin/st/menu/getSystemUrlList',
			pageSize : PAGE_SIZE
		});

		aGridOpt = {
			dataSource: aGridDs
			,pageable  : {
					pageSizes: [20, 30, 50]
				,buttonCount : 10
			}
			,navigatable: true
			,columns   : [
				{ field:'id'			,title : 'No'			, width:'80px'	,attributes:{ style:'text-align:center' }}
				,{ field:'urlName'		,title : '시스템명'		, width:'380px'	,attributes:{ style:'text-align:left; white-space:pre' }}
				,{ field:'url'			,title : '시스템 URL'	, width:'380px'	,attributes:{ style:'text-align:left; white-space:pre' }}
				,{ field:'content'		,title : '설명'			, width:'270px'	,attributes:{ style:'text-align:left; white-space:pre' }}
				,{ field:'createDate'	,title : '등록일'		, width:'150px'	,attributes:{ style:'text-align:center' }}
				,{ field:'modifyDate'	,title : '수정일'		, width:'150px'	,attributes:{ style:'text-align:center' }}
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
			url     : '/admin/st/menu/getSystemUrl',
			params  : {id : aMap.id},
			success :
				function( data ){
					fnBizCallback("select", data);
				},
			isAction : 'select'
		});
	}
	//-------------------------------  Grid End  -------------------------------

	//---------------Initialize Option Box Start ------------------------------------------------
	function fnInitOptionBox(){
		$('#kendoPopup').kendoWindow({
			visible: false,
			modal: true
		});

		fnKendoDropDownList({
			id    : 'inputAuthority',
			url : "/admin/st/menu/getSystemAuthority",
			textField :"comment",
			valueField : "code"
		});

		// 권한관리 DropDown 변경 이벤트
		searchAuthorityDropDown.bind("change", function() {
			if( $("#inputAuthority").val() == "" ){
				$("#findKeyword").val("").attr("disabled", true);
			}else{
				$("#findKeyword").attr("disabled", false);
			}
		});

	}
	//---------------Initialize Option Box End ------------------------------------------------

	//---------------Replace Char Start ----------------------------------------------------
	function replaceChar() {
		//영대소문자, 특수문자 /, 숫자 입력 가능 기획서 픽스 되는대로 수정필요
		const replaceRule = /[~!@\#$%^&*\()\-=+_'\;<>\.\`:\"\\,\[\]?|{}]/gi;
		const replaceKorean = /[ㄱ-ㅎㅏ-ㅣ가-힣]/gi;

		$(".url-input").on("focusout", function() {
            let inputValue = $(this).val();
            if (inputValue.length > 0) {
                if (inputValue.match(replaceRule) || inputValue.match(replaceKorean)) {
                	inputValue = inputValue.replace(replaceRule, "").replace(replaceKorean, "");
                }
                $(this).val(inputValue);
            }
		}).on("keyup", function() {
			$(this).val($(this).val().replace(replaceRule, ""));
			$(this).val($(this).val().replace(replaceKorean, ""))
		});
	}
	//---------------Replace Char End ----------------------------------------------------

	//-------------------------------  Common Function start -------------------------------

		/**
		* 콜백합수
		*/
	function fnBizCallback( id, data ){
		switch(id){
			case 'select':
				$('#inputForm').bindingForm(data, 'rows', true);
				fnFormPopup();
				break;
			case 'insert':
				fnKendoMessage({message :"저장되었습니다.", ok :function(e){
					$('#searchForm').formClear(true);
					fnSearch();
					$('#kendoPopup').data('kendoWindow').close();
				}});
				break;
			case 'save':
				fnKendoMessage({message :"저장되었습니다."});
				break;
			case 'update':
				fnKendoMessage({message : "저장되었습니다.", ok: function(e) {
					$('#searchForm').formClear(true);
					fnSearch();
					$('#kendoPopup').data('kendoWindow').close();
				}});
				break;
			case 'delete':
				var map = aGrid.dataItem(aGrid.select());
				aGridDs.remove(map);
				fnKendoMessage({ message :'삭제되었습니다.'
								,ok :function(e){
									fnNew();
									$('#kendoPopup').data('kendoWindow').close();
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
	/** Common Close*/
	$scope.fnClose = function( ){  fnClose();};
	//------------------------------- Html 버튼 바인딩  End -------------------------------

}); // document ready - END