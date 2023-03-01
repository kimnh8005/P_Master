﻿/**-----------------------------------------------------------------------------
 * description 		 : 개인정보 이력관리
 * @
 * @ 수정일			수정자          수정내용
 * @ ------------------------------------------------------
 * @ 2017.06.29		박영후          최초생성
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
			PG_ID  : 'urChangeHist',
			callback : fnUI
		});

	}

	function fnUI(){

		fnInitButton();	//Initialize Button  ---------------------------------

		fnInitGrid();	//Initialize Grid ------------------------------------

		fnInitOptionBox();//Initialize Option Box ------------------------------------
	//	fnSearch();

		//탭 변경 이벤트
        fbTabChange();			// fbCommonController.js - fbTabChange 이벤트 호출

	}

	//--------------------------------- Button Start---------------------------------

	function fnInitButton(){
		$('#fnSearch, #fnNew,#fnSave,  #fnClear').kendoButton();
		$('#fnDel').kendoButton({ enable: false });
	}

	function fnSearch(){

//		if( $('#condiValue').val() == ""){
//			return valueCheck("", "필수값을 입력 후 조회하시기 바랍니다.", 'condiValue');
//		}

		if( $('#startModifyDate').val() == "" && $('#endModifyDate').val() != ""){
			return valueCheck("6495", "시작일 또는 종료일을 입력해주세요.", 'startModifyDate');
		}
		if( $('#startModifyDate').val() != "" && $('#endModifyDate').val() == ""){
			return valueCheck("6495", "시작일 또는 종료일을 입력해주세요.", 'endModifyDate');
		}
		if( $('#startModifyDate').val() > $('#endModifyDate').val()){
			return valueCheck("6410", "시작일을 종료일보다 뒤로 설정할 수 없습니다.", 'startModifyDate');
		}

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
	function valueCheck(key, nullMsg, ID){
		fnKendoMessage({ message : fnGetLangData({ key : key, nullMsg : nullMsg}), ok : function focusValue(){
			$('#'+ID).focus();
		}});
		return false;
	}
	function fnClear(){
		$('#searchForm').formClear(true);

		$(".set-btn-type6").attr("fb-btn-active" , false );
	}
	function fnNew(){
	}
	function fnSave(){
	}
	function fnDel(){
	}
	function fnDelApply(){
	}
	function fnClose(){
	}

	//--------------------------------- Button End---------------------------------


	//------------------------------- Grid Start -------------------------------
	function fnInitGrid(){
		aGridDs = fnGetPagingDataSource({
			url      : '/admin/ur/user/getUserChangeHistoryList',
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
				{title : 'No.'			, width:'40px'	,attributes:{ style:'text-align:center' }, template: "<span class='row-number'></span>"}
				,{ field:'userName'			,title : '회원명'			, width:'50px'	,attributes:{ style:'text-align:center' }}
				,{ field:'loginId'			,title : '회원ID'			, width:'60px'	,attributes:{ style:'text-align:center' }}
				,{ field:'columnLabel'		,title : '컬럼 라벨'		, width:'80px'	,attributes:{ style:'text-align:center' }}
				,{ field:'beforeData'		,title : '변경 전 데이터'	, width:'80px'	,attributes:{ style:'text-align:center' }
						,template: function(dataItem){
							if(dataItem.columnName == 'gender'){ return fnChangeGenderType(dataItem.beforeData);
							}else{ return dataItem.beforeData; }}}
				,{ field:'afterData'		,title : '변경 후 데이터'	, width:'80px'	,attributes:{ style:'text-align:center' }
						,template: function(dataItem){
							if(dataItem.columnName == 'gender'){ return fnChangeGenderType(dataItem.afterData);
							}else{ return dataItem.afterData; }}}
				,{ field:'columnName'		,title : '컬럼명'			, width:'80px'	,attributes:{ style:'text-align:center' }}
				,{ field:'changeUserName'	,title : '수정자'			, width:'50px'	,attributes:{ style:'text-align:center' }}
				,{ field:'changedDate	'	,title : '수정일시'		, width:'80px'	,attributes:{ style:'text-align:center' }}
				,{ field:'urChangeLogId'	,hidden: true}
			]
			,selectable : false
		};
		aGrid = $('#aGrid').initializeKendoGrid( aGridOpt ).cKendoGrid();
			$("#aGrid").on("click", "tbody>tr", function () {
				fnGridClick();
		});

		aGrid.bind("dataBound", function(){
			let rowNum = aGridDs._total - ((aGridDs._page - 1) * aGridDs._pageSize);

			$("#aGrid tbody > tr .row-number").each(function(index){
				$(this).html(rowNum);
				rowNum--;
			});

			$("#countTotalSpan").text(aGridDs._total);
		});
	}
	function fnGridClick(){
	};
	//-------------------------------  Grid End  -------------------------------

	//---------------Initialize Option Box Start ------------------------------------------------
	function fnInitOptionBox(){

		fnTagMkRadio({
	        id: 'selectConditionType',
	        tagId: 'selectConditionType',
	        chkVal: 'singleSection',
	        tab: true,
	        data: [{
	            CODE: "singleSection",
	            NAME: "단일조건 검색",
	            TAB_CONTENT_NAME: "singleSection"
	        }, {
	            CODE: "multiSection",
	            NAME: "복수조건 검색",
	            TAB_CONTENT_NAME: "multiSection"
	        }],
	    });
	    //[공통] 탭 변경 이벤트
	    fbTabChange();

	  //전체회원 단일조건
		fnKendoDropDownList({
			id    : 'condiType',
			data  : [{ "CODE" : "userName" , "NAME":"회원명"},
				     { "CODE" : "loginId" , "NAME":"회원ID"}
					]
		});
		/*fnKendoDropDownList({
			id    : 'condiType',
			data  : [
				{"CODE":"USER_NAME"	,"NAME":"회원명"},
				{"CODE":"LOGIN_ID"	,"NAME":"회원ID"}
			],
			textField :"NAME",
			valueField : "CODE",
			value : "USER_NAME"
		});*/

		fnKendoDatePicker({
			id    : 'startModifyDate',
			format: 'yyyy-MM-dd',
			defVal : fnGetDayMinus(fnGetToday(),6)
			,defType : 'oneWeek'
		});

		fnKendoDatePicker({
			id    : 'endModifyDate',
			format: 'yyyy-MM-dd',
			btnStyle : true,
			btnStartId : 'startModifyDate',
			btnEndId : 'endModifyDate',
			defVal : fnGetToday()
			,defType : 'oneWeek'
			,change: function(e){
	        	   if ($('#startModifyDate').val() == "" ) {
	                   return valueCheck("6495", "시작일을 선택해주세요.", 'endModifyDate');
	               }
				}
		});
	}
	//---------------Initialize Option Box End ------------------------------------------------
	//-------------------------------  Common Function start -------------------------------

	function inputFocus(){
		$('#inputSHOP_NAME').focus();
	};

	function fnChangeGenderType(beforeGender){
		var afterGender = '기타';

		if(beforeGender == "M") {
			afterGender = '남자';
		}else if(beforeGender == "F") {
			afterGender = '여자';
		}

		return afterGender;
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
					fnKendoMessage({message : fnGetLangData({key :"370",nullMsg :'중복입니다.' })  ,ok :inputFocus});
				}else{
					aGridDs.insert(data.rows);;
					fnKendoMessage({message : fnGetLangData({key :"369",nullMsg :'입력되었습니다.' }) ,ok :fnNew});
				}
				break;
			case 'save':
				fnKendoMessage({message : fnGetLangData({key :"368",nullMsg :'저장되었습니다.' })});
				break;
			case 'update':
				fnUpdateGrid(data,$("#aGrid"),"rows");
				fnKendoMessage({message : fnGetLangData({key :"367",nullMsg :'수정되었습니다.' }) });
				break;
			case 'delete':
				aGridDs.remove(data);
				fnNew();
				//aGridDs.total = aGridDs.total-1;
				fnKendoMessage({message : fnGetLangData({key :"366",nullMsg :'삭제되었습니다.' })});
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
	//------------------------------- Html 버튼 바인딩  End -------------------------------

}); // document ready - END
