﻿/**-----------------------------------------------------------------------------
 * description 		 : 검색어관리  - 인기검색어
 * @ 페이징 없이 상위 20개까지만 노출 정책
 * @ 수정일			수정자          수정내용
 * @ ------------------------------------------------------
 * @ 2020.07.01		김경민          최초생성
 * @
 * **/
'use strict';

var aGridDs, aGridOpt, aGrid;

$(document).ready(function() {
	fnInitialize();	//Initialize Page Call ---------------------------------

	//Initialize PageR
	function fnInitialize(){
		$scope.$emit('fnIsMenu', { flag : 'true' });

		fnPageInfo({
			PG_ID  : 'searchWordLog',
			callback : fnUI
		});
	}

	function fnUI(){

		fnInitButton();	//Initialize Button  ---------------------------------

		fnInitGrid();	//Initialize Grid ------------------------------------

		fnInitOptionBox();//Initialize Option Box ------------------------------------

		fnSearch();

	}

	//--------------------------------- Button Start---------------------------------

	function fnInitButton(){
		$('#fnSearch, #fnClear').kendoButton();
	}
	function fnSearch(){

	  if( $('#startCreateDate').val() == "" && $('#endCreateDate').val() == ""){
		  return valueCheck("시작일 또는 종료일을 입력해주세요.", 'endCreateDate');
	  }
	  if( $('#startCreateDate').val() == "" && $('#endCreateDate').val() != ""){
		  return valueCheck("시작일 또는 종료일을 입력해주세요.", 'endCreateDate');
	  }

	  if( $('#startCreateDate').val() != "" && $('#endCreateDate').val() == ""){
		  return valueCheck("시작일 또는 종료일을 입력해주세요.", 'endCreateDate');
	  }

	  if( $('#startCreateDate').val() > $('#endCreateDate').val()){
		  return valueCheck("시작일을 종료일보다 뒤로 설정할 수 없습니다.", 'endCreateDate');
	  }
	  var data;
	  data = $('#searchForm').formSerialize(true);
	  aGridDs.read(data);

	}


    // 값 체크
    function valueCheck(nullMsg, id){
        fnKendoMessage({ message : nullMsg, ok : function focusValue(){
            $('#' + id).focus();
        }});

        return false;
    };

//	function fnSearch(){
//		var data;
//
//        if( $('#startCreateDate').val() == "" && $('#endCreateDate').val() != ""){
//            return valueCheck("시작일 또는 종료일을 입력해주세요.", 'endCreateDate');
//        }
//
//        if( $('#startCreateDate').val() != "" && $('#endCreateDate').val() == ""){
//            return valueCheck("시작일 또는 종료일을 입력해주세요.", 'endCreateDate');
//        }
//
//		if( $('#startCreateDate').val() > $('#endCreateDate').val()){
//			return valueCheck("시작일을 종료일보다 뒤로 설정할 수 없습니다.", 'endCreateDate');
//		}
//
//		data = $('#searchForm').formSerialize(true);
//		console.dir("==>"+JSON.stringify(data))
//		var query = {
//			page : 1,
//			pageSize : PAGE_SIZE,
//
//			filterLength : fnSearchData(data).length,
//			filter :  {
//				filters : fnSearchData(data)
//			}
//		};
//		console.dir("222222222-------------------------------")
//		console.dir(JSON.stringify(query))
//		console.dir("-------------------------------")
//		aGridDs.query( query );
//	}
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

	//--------------------------------- Button End---------------------------------


	//------------------------------- Grid Start -------------------------------
	function fnInitGrid(){
		aGridDs = fnGetPagingDataSource({
     	//aGridDs = fnGetDataSource({
			url      : '/admin/dp/searchWordLog/getSearchWordLogList'

		});
		aGridOpt = {
			dataSource: aGridDs

			,navigatable: false
			,columns   : [
				 { field:'no'	          ,title : '순위'	       , width:'80px',attributes:{ style:'text-align:center' }}
				,{ field:'searchWord'	  ,title : '검색어'	   , width:'300px',attributes:{ style:'text-align:left' }}
				,{ field:'searchItemCnt'  ,title : '검색 상품수'  , width:'80px',attributes:{ style:'text-align:right' }}
				,{ field:'searchCnt'	  ,title : '검색 횟수'	   , width:'80px',attributes:{ style:'text-align:right' }}
			]

		};
		aGrid = $('#aGrid').initializeKendoGrid( aGridOpt ).cKendoGrid();

		aGrid.bind('dataBound',function(e){
			console.dir("aGridDs===========>"+aGridDs._total);
			$('#totalCnt').text(aGridDs._total);

//			$('.gridBtn').on("click", function (e) {
//				e.preventDefault();
//			});
		});
		aGrid = $('#aGrid').initializeKendoGrid( aGridOpt ).cKendoGrid();

//		$("#aGrid").on("click", "tbody>tr", function () {
//				fnGridClick();
//		});
	}

//	function fnGridClick(){
//		var map = aGrid.dataItem(aGrid.select());
//	};
	//-------------------------------  Grid End  -------------------------------

	//---------------Initialize Option Box Start ------------------------------------------------
	function fnInitOptionBox(){
//		fnKendoDropDownList({
//			id  : 'targetSystem',
//			//url	: "/comn/st/getCodeList",
//			//params : {"ST_COMN_CODE_MST_ID" : "58", "USE_YN" :"Y"},
//			url : "/admin/comn/getCodeList",
//			params : {"stCommonCodeMasterCode" : "TARGET_SYSTEM_CD", "useYn" :"Y"},
//			blank : "All"
//		});
		fnKendoDatePicker({
			id    : 'startCreateDate',
			format: 'yyyy-MM-dd',
			defVal: fnGetDayMinus(fnGetToday(),6)
			,defType : 'oneWeek'
		});
		fnKendoDatePicker({
			id    : 'endCreateDate',
			format: 'yyyy-MM-dd',
			btnStyle : true,
			btnStartId : 'startCreateDate',
			btnEndId : 'endCreateDate',
			defVal: fnGetToday()
			,defType : 'oneWeek'
		});
	}

	//---------------Initialize Option Box End ------------------------------------------------
	//-------------------------------  Common Function start -------------------------------

	function inputFocus(){
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
				$('#searchForm').bindingForm(data, 'rows', true);
				break;

		}
	}

	//-------------------------------  Common Function end -------------------------------


	//------------------------------- Html 버튼 바인딩  Start -------------------------------
	/** Common Search*/
	$scope.fnSearch = function( ) {	fnSearch();};
	/** Common Clear*/
	$scope.fnClear =function(){	 fnClear();};
	//------------------------------- Html 버튼 바인딩  End -------------------------------

}); // document ready - END
