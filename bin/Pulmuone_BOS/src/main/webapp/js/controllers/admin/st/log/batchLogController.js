/**-----------------------------------------------------------------------------
 * description 		 : 로그관리 - 배치로그
 * @
 * @ 수정일			수정자          수정내용
 * @ ------------------------------------------------------
 * @ 2017.03.15		김아란          최초생성
 * @ 2020.10.30		강윤경          현행화
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
			PG_ID  : 'batchLog',
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
		$('#fnSearch, #fnClear').kendoButton();
	}
	function fnSearch(){
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

	//--------------------------------- Button End---------------------------------


	//------------------------------- Grid Start -------------------------------
	function fnInitGrid(){
		aGridDs = fnGetPagingDataSource({
			url      : '/admin/st/log/getBatchLogList',
			pageSize : PAGE_SIZE
		});
		aGridOpt = {
			dataSource: aGridDs
			,pageable  : {
				pageSizes: [20, 30, 50],
				buttonCount : 5
			}
			,navigatable: true
			,columns   : [
				 { field:'no'			,title : 'No'	   	, width:'20px',attributes : {style : "text-align:center"}, template : "<span class='row-number'></span>" }
				,{ field:'batchName'	,title : '배치명'		, width:'50px',attributes:{ style:'text-align:center' }}
				/*
				,{ field:'description' 		,title : '설명'		, width:'60px',attributes:{ style:'text-align:center' }}
				,{ field:'jobClassFullPath' ,title : '실행 class'	, width:'110px',attributes:{ style:'text-align:left' }}
				,{ field:'schedule'		,title : '스케쥴정보'	, width:'60px',attributes:{ style:'text-align:center' }}*/
				,{ field:'startTime'	,title : '배치시작일시'	, width:'50px',attributes:{ style:'text-align:center' }}
				,{ field:'endTime'		,title : '배치종료일시'	, width:'50px',attributes:{ style:'text-align:center' }}
				,{ field:'status'		,title : '배치결과'	, width:'30px',attributes:{ style:'text-align:center' }
					, template: "#=(status=='COMPLETED') ? '성공' : (status=='STARTED') ? '진행중' : '실패'#"}
				,{ field:'errorMessage'	,title : '오류메시지'	, width:'200px',attributes:{ style:'text-align:center' }}
			],
			change : fnGridClick
		};
		aGrid = $('#aGrid').initializeKendoGrid( aGridOpt ).cKendoGrid();
		aGrid.bind('dataBound',function(e){
			/*
			$('.gridBtn').on("click", function (e) {
				e.preventDefault();
			});*/

			//total count
            $('#countTotalSpan').text(aGridDs._total);

            let rowNum = aGridDs._total - ((aGridDs._page - 1) * aGridDs._pageSize);

 		   $("#aGrid tbody > tr .row-number").each(function(index){
 		      $(this).html(rowNum);
 		      rowNum--;
 		   });
		});
	}

	function fnGridClick(){
		var map = aGrid.dataItem(aGrid.select());
	};
	//-------------------------------  Grid End  -------------------------------

	//---------------Initialize Option Box Start ------------------------------------------------
	function fnInitOptionBox(){
		/*
		fnKendoDropDownList({
			id  : 'targetSystem',
			//url	: "/comn/st/getCodeList",
			//params : {"ST_COMN_CODE_MST_ID" : "58", "USE_YN" :"Y"},
			url : "/admin/comn/getCodeList",
			params : {"stCommonCodeMasterCode" : "TARGET_SYSTEM_CD", "useYn" :"Y"},
			blank : "All"
		});*/


		//배치결과
		fnKendoDropDownList({ id : 'status'	,
			data :
				[
					{"CODE":""	,"NAME": '전체'}
					, {"CODE":"COMPLETED"	,"NAME": '성공'}
					, {"CODE":"FAILED"		,"NAME": '실패'}
				],
				value : "FAILED",
			}
		);
		//검색어 구분
		fnKendoDropDownList({ id : 'searchType'	,
			data :
				[
					  {"CODE":"batchName"	,"NAME": '배치명'}
					, {"CODE":"errorMsg"	,"NAME": '오류메세지'}
				]}
		);
		fnKendoDatePicker({
			id    : 'startCreateDate',
			format: 'yyyy-MM-dd',
			defVal: fnGetDayMinus(fnGetToday(),6),
			defType : 'oneWeek'
		});
		fnKendoDatePicker({
			id    : 'endCreateDate',
			format: 'yyyy-MM-dd',
			btnStyle : true,
			btnStartId : 'startCreateDate',
			btnEndId : 'endCreateDate',
			defVal: fnGetToday(),
			defType : 'oneWeek'
		});
	}

	//---------------Initialize Option Box End ------------------------------------------------
	//-------------------------------  Common Function start -------------------------------

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
