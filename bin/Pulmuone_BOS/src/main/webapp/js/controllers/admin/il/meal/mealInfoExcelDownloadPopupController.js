/**-----------------------------------------------------------------------------
 * description 		 : 식단 스케쥴관리 > 엑셀다운로드 팝업
 * @
 * @ 수정일			수정자          수정내용
 * @ ------------------------------------------------------
 * @ 2021.09.08		최윤지          최초생성
 * **/
"use strict";

var PAGE_SIZE = 20;
var viewModel, gridDs, gridOpt, grid;
var pageParam = fnGetPageParam();

var paramData ;
var odOrderDetlIdList = [];
if(parent.POP_PARAM['parameter']){
	paramData = parent.POP_PARAM['parameter'];
}

$(document).ready(function() {

	fnInitialize();	//Initialize Page Call ---------------------------------

	//Initialize PageR
	function fnInitialize(){
		$scope.$emit("fnIsMenu", { flag : false });
		fnPageInfo({
			PG_ID  : "mealInfoExcelDownloadPopup",
			callback : fnUI
		});
	};

	// 화면 UI 초기화
	function fnUI(){
		fnInitButton();
		fnInitOptionBox();
		fnDefaultSetting();
	};

	//--------------------------------- Button Start---------------------------------
	// 버튼 초기화
	function fnInitButton(){
		$("#fnMealInfoExcelDownload").kendoButton();
	};
	//--------------------------------- Button End-----------------------------------
	function fnDefaultSetting(){
		$('#downloadDateStart').data('kendoDatePicker').enable(false);
		$('#downloadDateEnd').data('kendoDatePicker').enable(false);
		$('.date-controller button').prop('disabled',true);
	}
	//---------------Initialize Option Box Start ------------------------------------------------

	// 옵션 초기화
	function fnInitOptionBox(){

		//다운로드 정보
		fnKendoDropDownList({
            id : "downloadType",
            tagId : "downloadType",
            data : [ 
                     {"CODE" : "DOWNLOAD_TYPE.PATTERN", "NAME" : "패턴"},
                     {"CODE" : "DOWNLOAD_TYPE.SCH", "NAME" : "스케쥴"}
                   ]
        });

		$('input[name="downloadType"]').on('change', function(e) {
			if(this.value == 'DOWNLOAD_TYPE.PATTERN') {
				$('#downloadDateStart').data('kendoDatePicker').enable(false);
				$('#downloadDateEnd').data('kendoDatePicker').enable(false);
				$('.date-controller button').prop('disabled',true);
			} else {
				$('#downloadDateStart').data('kendoDatePicker').enable(true);
				$('#downloadDateEnd').data('kendoDatePicker').enable(true);
				$('.date-controller button').prop('disabled',false);
			}
		});

		//다운로드 시작일
		fnKendoDatePicker({
			id    : 'downloadDateStart',
			format: 'yyyy-MM-dd',
			btnStartId : 'downloadDateStart',
			btnEndId : 'downloadDateEnd',
			defVal : fnGetToday(),
			defType : 'oneMonth',
			nextDate : true,
			change: function() {
				fnInputValidateUse('downloadDateStart', 'downloadDateEnd', 'start');
			}
		});

		//다운로드 종료일
		fnKendoDatePicker({
			id    : 'downloadDateEnd',
			format: 'yyyy-MM-dd',
			btnStyle : true,
			btnStartId : 'downloadDateStart',
			btnEndId : 'downloadDateEnd',
			defVal : fnGetDayAdd(fnGetToday(),30),
			defType : 'oneMonth',
			nextDate : true,
			change: function() {
				fnInputValidateUse('downloadDateStart', 'downloadDateEnd', 'end');
			}
		});

	};

	//---------------Initialize Option Box End ------------------------------------------------
	//-------------------------------  Common Function start ----------------------------------

	//다운로드
	function fnMealInfoExcelDownload(){
		var url = '';
		var data = $("#searchForm").formSerialize(true);

		if(data.downloadType == 'DOWNLOAD_TYPE.PATTERN') { //다운로드정보가 패턴인 경우
			url = '/admin/il/meal/getMealPatternExportExcel';
		} else if(data.downloadType == 'DOWNLOAD_TYPE.SCH') { //다운로드정보가 스케쥴인 경우
			url = '/admin/il/meal/getMealScheduleExportExcel';
		}
		data.downloadType = $('#downloadType').data("kendoDropDownList").text();
		data.mallDivNm = paramData.mallDivNm;
		data.patternCd = paramData.patternCd;
		data.patternNm = paramData.patternNm;
		data.patternNmExcel = paramData.patternNm.replace(/(\s*)/g, "");

		fnExcelDownload(url, data);
	};

	function fnInputValidateUse(downloadDateStart, downloadDateEnd, picker) {
		//달력 조회 기간 체크
		if(!fnIsEmpty($("#"+downloadDateStart).val()) && !fnIsEmpty($("#"+downloadDateEnd).val())) {
			if(fnGetMonthMinus($("#"+downloadDateEnd).val(),3,'yyyy-MM-dd') > $("#"+ downloadDateStart).val() ) {
				fnKendoMessage({message : "최대 3개월까지 다운로드 하실 수 있습니다.", ok : function() {
						$('#'+downloadDateStart).val(fnGetToday());
						$('#'+downloadDateEnd).val(fnGetMonthAdd(fnGetToday(), 3, 'yyyy-MM-dd'));
						picker == 'start' ?  $("#"+downloadDateStart).focus() :  $("#"+downloadDateEnd).focus();
					}});
				return false;
			}
			//달력 시작일 종료일 체크
			if($("#"+ downloadDateStart).val() > $("#"+downloadDateEnd).val()) {
				fnKendoMessage({message : "조회 시작일보다 과거로 설정할 수는 없습니다.", ok : function() {
						$('#'+downloadDateStart).val(fnGetToday());
						$('#'+downloadDateEnd).val(fnGetMonthAdd(fnGetToday(), 3, 'yyyy-MM-dd'));
						picker == 'start' ?  $("#"+downloadDateStart).focus() :  $("#"+downloadDateEnd).focus();
					}});
				return false;
			}
		}
		return true;
	}
	//-------------------------------  Common Function end -------------------------------

	//------------------------------- Html 버튼 바인딩  Start -----------------------------

	$scope.fnMealInfoExcelDownload =function(){	fnMealInfoExcelDownload(); };

	//------------------------------- Html 버튼 바인딩  End -------------------------------

	//------------------------------- Validation Start -----------------------------------

	//------------------------------- Validation End -------------------------------------
}); // document ready - END
