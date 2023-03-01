/**-----------------------------------------------------------------------------
 * description 		 : 식단 상세정보
 * @
 * @ 수정일			수정자          수정내용
 * @ ------------------------------------------------------
 * @ 2021.09.13		천혜현          최초생성
 * @
 * **/
'use strict';


var patternInfoGridDs, patternInfoGridOpt, patternInfoGrid;	// 패턴 그리드
var scheduleInfoGridDs, scheduleInfoGridOpt, scheduleInfoGrid; // 스케쥴 그리드
var paramData ;
if(parent.POP_PARAM['parameter']){
	paramData = parent.POP_PARAM['parameter'];
}

$(document).ready(function() {
	fnInitialize();	//Initialize Page Call ---------------------------------

	//Initialize PageR
	function fnInitialize(){
		$scope.$emit('fnIsMenu', { flag : false });

		fnPageInfo({
			PG_ID  : 'goodsMealMgm',
			callback : fnUI
		});

	}


	function fnUI(){

		fnInitButton();	//Initialize Button  ---------------------------------

		fnInitGrid();

		fnInitOptionBox();//Initialize Option Box ------------------------------------

		fnInitBindEvents();

		fnSearch();

	}




	//--------------------------------- Button Start---------------------------------

	function fnInitButton(){
		$('#fnSave, #fnClose, #fnMealPatternDownload, #fnMealScheduleDownload').kendoButton();
	}

	function fnInitGrid(){
		$("input[name='mealTab'][value='mealPattern']").trigger("click");

		// 패턴 기본 정보
		fnAjax({
			url : '/admin/item/meal/getMealPatternInfo',
			params : {
				patternCd : paramData.patternCd
			},
			success : function(data) {

				$('#inputForm').bindingForm({ data : data }, "data");

				// 최초 생성일 / 최근 수정일
				let modifyInfo = '';
				if(data.modifyDt != '' && data.modifyDt != null){
					modifyInfo += ' /'+"<BR>" + data.modifyDt + ' (' +data.modifyId+ ')' + data.modifyNm;
				}
				let createInfo = data.createDt +' ('+data.createId+')' + data.createNm + modifyInfo;
				$('#createInfo').html(createInfo);

			},
			isAction : 'select'
		});

		// 패턴정보 그리드
		patternInfoGridDs = fnGetPagingDataSource({
			url      : '/admin/item/meal/getMealPatternDetailList'
		});
		patternInfoGridOpt = {
			dataSource: patternInfoGridDs
			 , height: 300
			 , scrollable  : true
			,columns   : [
				{ field:'patternNo'		,title : '패턴순번'		, width:'30px'	,attributes:{ style:'text-align:center' }}
				,{ field:'setNo'		,title : '세트순번'		, width:'30px'	,attributes:{ style:'text-align:center' }}
				,{ field:'setCd'		,title : '세트코드'		, width:'60px'	,attributes:{ style:'text-align:center' }}
				,{ field:'setNm'		,title : '세트명'		, width:'100px'	,attributes:{ style:'text-align:center' }}
				,{ field:'mealContsCd'	,title : '식단품목코드'	, width:'80px'	,attributes:{ style:'text-align:center' }}
				,{ field:'mealNm'		,title : '식단품목명'		, width:'250px'	,attributes:{ style:'text-align:center' }}
				,{ field:'allergyYn'	,title : '알러지식단'		, width:'30px'	,attributes:{ style:'text-align:center' },
					template : function(dataItem) {
						return dataItem.allergyYn != 'Y' ? '' : 'Y';
					}}
				,{ field:'modifyDt'		,title : '최근 개별 수정일', width:'80px'	,attributes:{ style:'text-align:center' }}
			]
		};
		patternInfoGrid = $('#patternInfoGrid').initializeKendoGrid( patternInfoGridOpt ).cKendoGrid();

		patternInfoGrid.bind("dataBound", function() {
			$('#mealPatternTotalCnt').text(patternInfoGridDs._total);
			$("#patternInfoGrid .k-grid-content").css('height','380px');
		});

		// 패턴 스케쥴 그리드
		scheduleInfoGridDs = fnGetPagingDataSource({
			url      : '/admin/item/meal/getMealScheduleDetailList'
		});
		scheduleInfoGridOpt = {
			dataSource: scheduleInfoGridDs
			 , height: 300
			 , scrollable  : true
			,columns   : [
				{ field:'deliveryDateStr'	,title : '날짜'			, width:'60px'	,attributes:{ style:'text-align:center' }}
				,{ field:'deliveryWeekCode'	,title : '요일'			, width:'30px'	,attributes:{ style:'text-align:center' }}
				,{ field:'mealContsCd'		,title : '식단품목코드'	, width:'60px'	,attributes:{ style:'text-align:center' }}
				,{ field:'mealNm'			,title : '식단품목명'		, width:'100px'	,attributes:{ style:'text-align:center' }}
				,{ field:'allergyYn'		,title : '알러지식단'		, width:'60px'	,attributes:{ style:'text-align:center' },
					template : function(dataItem) {
						return dataItem.allergyYn != 'Y' ? '' : 'Y';
					}}
				,{ field:'modifyDt'			,title : '최근 개별 수정일', width:'80px'	,attributes:{ style:'text-align:center' }}
			]
		};
		scheduleInfoGrid = $('#scheduleInfoGrid').initializeKendoGrid( scheduleInfoGridOpt ).cKendoGrid();

		scheduleInfoGrid.bind("dataBound", function() {
			$('#mealScheduleTotalCnt').text(scheduleInfoGridDs._total);
			$("#scheduleInfoGrid .k-grid-content").css('height','300px');
		});

	}

	function fnSave(){
		var url  = '/admin/il/meal/addMealConts';
		var cbId = 'insert';

		if (OPER_TP_CODE == 'U') {
			url = '/admin/il/meal/putMealConts';
			cbId = 'update';
		}

		var data = $('#inputForm').formSerialize(true);

		fnAjaxSubmit({
			form    : 'inputForm',
			fileUrl : '/fileUpload',
			url     : url,
			params  : data,
			storageType : "public",
			domain : "il",
			success : function(data){
				fnBizCallback(cbId, data);
			},
			isAction : 'batch'
		});
	}

    function fnClose(){
        parent.POP_PARAM = 'SAVE';
        parent.LAYER_POPUP_OBJECT.data('kendoWindow').close();
    }



	//--------------------------------- Button End---------------------------------


	//------------------------------- Grid Start -------------------------------
	function fnSearch(){
		let data = new Object();

		let dateSearchStart = $("#dateSearchStart").val();
		let dateSearchEnd = $("#dateSearchEnd").val();
		//달력 시작일, 종료일 빈값 체크
		if(fnIsEmpty(dateSearchStart) || fnIsEmpty(dateSearchEnd)) {
			fnKendoMessage({message : "기간을 입력해주세요.", ok : function() {
					$('#dateSearchStart').val(fnGetToday());
					$('#dateSearchEnd').val(fnGetMonthAdd(fnGetToday(), 3, 'yyyy-MM-dd'));
				}});
			return false;
		}
		data.patternCd = paramData.patternCd;
		data.dateSearchStart = dateSearchStart.replace(/(\d{4})(\d{2})(\d{2})/g, '$1-$2-$3');
		data.dateSearchEnd = dateSearchEnd.replace(/(\d{4})(\d{2})(\d{2})/g, '$1-$2-$3');

		// 패턴 그리드
		patternInfoGridDs.read(data);

		// 스케쥴 그리드
		let searchData = fnSearchData(data);
		let query = {
			filterLength : searchData.length,
			filter : { filters : searchData }
		};
		scheduleInfoGridDs.query(query);
	}




	//-------------------------------  Grid End  -------------------------------

	//---------------Initialize Option Box Start ------------------------------------------------
	function fnInitOptionBox(){

		// 등록일 시작
		fnKendoDatePicker({
			id: "dateSearchStart",
			format: "yyyy-MM-dd",
			btnStartId: "dateSearchStart",
			btnEndId: "dateSearchEnd",
			defVal : fnGetToday(),
			defType : 'threeMonth',
			nextDate : true,
			change : function(e) {
				fnInputValidateUse('dateSearchStart', 'dateSearchEnd', 'start');
			}
		});

		// 등록일 종료
		fnKendoDatePicker({
			id: "dateSearchEnd",
			format: "yyyy-MM-dd",
			btnStyle: true,
			btnStartId: "dateSearchStart",
			btnEndId: "dateSearchEnd",
			defVal : fnGetMonthAdd(fnGetToday(), 3, 'yyyy-MM-dd'),
			defType : 'threeMonth',
			nextDate : true,
			change : function(e) {
				fnInputValidateUse('dateSearchStart', 'dateSearchEnd', 'end');
			}
		});
	}

	function fnInitBindEvents() {

		// 일반,새벽 탭 클릭시
		$("input[name='mealTab']").on("click", function(e) {

			let mealType = $("input:radio[name=mealTab]:checked").val();

			// 식단 패턴
			if(mealType == 'mealPattern'){
				$("#mealPatternDiv").css('display', 'block');
				$("#mealScheduleDiv").css('display', 'none');
			// 식단 스케쥴
			}else if(mealType == 'mealSchedule'){
				$("#mealPatternDiv").css('display', 'none');
				$("#mealScheduleDiv").css('display', 'block');
			}

		});

	}

	// 패턴 다운로드
	function fnMealPatternDownload(){
		var data = {};
		data.downloadType = '패턴';
		data.mallDivNm = $('#mallDivNm').val();
		data.patternCd = $('#patternCd').val();
		data.patternNm = $('#patternNm').val().replace(/(\s*)/g, "");
		data.patternNmExcel = $('#patternNm').val().replace(/(\s*)/g, "");

		fnExcelDownload('/admin/il/meal/getMealPatternExportExcel', data);
	}
	
	// 스케쥴 다운로드
	function fnMealScheduleDownload(){
		var data = {};
		data.downloadType = '스케쥴';
		data.mallDivNm = $('#mallDivNm').val();
		data.patternCd = $('#patternCd').val();
		data.patternNmExcel = $('#patternNm').val().replace(/(\s*)/g, "");
		data.patternNm = $('#patternNm').val().replace(/(\s*)/g, "");
		data.downloadDateStart = $('#dateSearchStart').val().replaceAll("-", "");
		data.downloadDateEnd = $('#dateSearchEnd').val().replaceAll("-", "");

		fnExcelDownload('/admin/il/meal/getMealScheduleExportExcel', data);
	}
	//---------------Initialize Option Box End ------------------------------------------------
	//-------------------------------  Common Function start -------------------------------



	/**
	* 콜백합수
	*/
	function fnBizCallback( id, data ){
		switch(id){
			case 'insert':
                fnKendoMessage({
                        message : '저장되었습니다.',
                        ok      : function(){ fnClose();}
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

		}
	}


	//-------------------------------  Common Function end -------------------------------


	//------------------------------- Html 버튼 바인딩  Start -------------------------------
	/** Common Save*/
	$scope.fnSave = function(){	 fnSave();};
	/** Common Close*/
	$scope.fnClose = function( ){  fnClose();};
	$scope.fnSearch = function( ){  fnSearch();};
	$scope.fnMealPatternDownload =function(){ fnMealPatternDownload(); }; // 패턴 다운로드
	$scope.fnMealScheduleDownload =function(){ fnMealScheduleDownload(); }; // 스케쥴 다운로드
	
	//------------------------------- Html 버튼 바인딩  End -------------------------------

}); // document ready - END
