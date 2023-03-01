/**-----------------------------------------------------------------------------
 * description 		 : 로그관리 - 엑셀 다운로드 로그
 * @
 * @ 수정일			수정자          수정내용
 * @ ------------------------------------------------------
 * @ 2020.11.02		강윤경          최초생성
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
			PG_ID  : 'excelDownLog',
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
			url      : '/admin/st/log/getExcelDownloadLogList',
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
				 { field:'no'				, title : 'No'	   		, width:'30px',attributes : {style : "text-align:center"}, template : "<span class='row-number'></span>" }
				,{ field:'loginId'			, title : '관리자ID'	   	, width:'50px',attributes:{ style:'text-align:center' }}
				,{ field:'userName'			, title : '관리자명'	   	, width:'40px',attributes:{ style:'text-align:center' }}
				,{ field:'ip'				, title : '접속IP'	   	, width:'50px',attributes:{ style:'text-align:center' }}
				,{ field:'excelDownloadTypeName'	, title : '다운로드 데이터'	, width:'80px',attributes:{ style:'text-align:center' }}
				,{ field:'downloadReason'	, title : '다운로드 사유'	, width:'250px',attributes:{ style:'text-align:center' }}
				,{ field:'createDate'		, title : '다운로드 일자'	, width:'80px',attributes:{ style:'text-align:center' }}
			],
			change : fnGridClick
		};
		aGrid = $('#aGrid').initializeKendoGrid( aGridOpt ).cKendoGrid();
        aGrid.bind("dataBound", function() {
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
		//검색어 구분
		fnKendoDropDownList({ id : 'searchType'	,
			data :
				[
					  {"CODE":"id"		,"NAME": '관리자ID'}
					, {"CODE":"name"	,"NAME": '관리자명'}
				]}
		);
		fnKendoDropDownList({
			id  : 'excelDownType',
			url : "/admin/comn/getCodeList",
			params : {"stCommonCodeMasterCode" : "EXCEL_DOWN_TP", "useYn" :"Y"},
			blank : "전체"
		});
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

	let successCondition =
	[
		{"CODE":""	,"NAME": 'All'}
		, {"CODE":"Y"	,"NAME": '성공'}
		, {"CODE":"N"	,"NAME": '실패'}
	];

	fnKendoDropDownList({ id : 'successCondition'	, data : successCondition });
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
			case 'insert':
				if(data.rows =="DUP_DATA"){
					fnKendoMessage({message : fnGetLangData({key :"370",nullMsg :'중복입니다.' })  ,ok :inputFocus});
				}else{
					aGridDs.insert(data.rows);;
					fnKendoMessage({message : fnGetLangData({key :"369",nullMsg :'입력되었습니다.' }) ,ok :fnNew});
				}
				fnInitOptionBox();
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
	$scope.fnSearch = function( ) {	fnSearch();};
	/** Common Clear*/
	$scope.fnClear =function(){	 fnClear();};
	//------------------------------- Html 버튼 바인딩  End -------------------------------

}); // document ready - END
