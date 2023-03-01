/**-----------------------------------------------------------------------------
 * description 		 : 정산관리 > 임직원정산 > 임직원 지원금 사용현황
 * @
 * @ 수정일			수정자          수정내용
 * @ ------------------------------------------------------
 * @ 2021.03.03		이명수          최초생성
 * **/
"use strict";

var PAGE_SIZE = 20;
var viewModel, defaultGridDs, defaultGridOpt, defaultGrid;
var pageParam = fnGetPageParam();

var defaultGridUrl = "/admin/calculate/employee/getEmployeeUseList"

$(document).ready(function() {

	importScript("/js/service/ca/employee/calEmployeeGridColumns.js", function (){
		importScript("/js/service/ca/employee/calEmployeeSearch.js", function (){
			importScript("/js/service/ca/employee/calEmployeeSubmitUtil.js", function (){
				importScript("/js/service/ca/employee/calEmployeeFunction.js", function (){
					fnInitialize();
				});
			});
		});
	});

	//Initialize PageR
	function fnInitialize(){
		$scope.$emit("fnIsMenu", { flag : true });
		fnPageInfo({
			PG_ID  : "calEmployeeUse",
			callback : fnUI
		});
	};

	// 화면 UI 초기화
	function fnUI(){

		fnTranslate();	// 다국어 변환
		fnInitButton();
		fnInitOptionBox();
		fnViewModelInit();
		fnDefaultSet();
		fnInitGrid();

		// fnSearch();
		//orderSubmitUtil.search();
	};

	//--------------------------------- Button Start---------------------------------
	// 버튼 초기화
	function fnInitButton(){
		$("#fnSearch,  #fnClear").kendoButton();
	};
	//--------------------------------- Button End-----------------------------------

	//------------------------------- Grid Start -------------------------------
	// 그리드 초기화
	function fnInitGrid(){
		calEmployeeGridEventUtil.gridInit(defaultGridUrl, calEmployeeGridUtil.calEmployeeUseList());
	};

	//-------------------------------  Grid End  -------------------------------

	//---------------Initialize Option Box Start ------------------------------------------------

	// 옵션 초기화
	function fnInitOptionBox(){
		// 부문 구분
		calEmployeeSearchUtil.getDropDownUrl("findOuId", "ouNm", "ouId", "전체", "/admin/calculate/employee/getOuIdAllList",{}, function(e) {
			if(PG_SESSION.ouId != '' && PG_SESSION.sectorAllViewYn == 'N'){
				$("#findOuId").data("kendoDropDownList").value(PG_SESSION.ouId);
				$("#findOuId").data("kendoDropDownList").enable(false);
			}
			if(pageParam.ouId !== undefined) {
				var selectData = e.sender.dataSource._pristineData;
				for(var i=0; i<selectData.length; i++) {
					var item = selectData[i];
					if(item.ouId == pageParam.ouId) {
						$("#findOuId").val(item.ouId).siblings("span").find("span:eq(0)").text(item.ouNm);
						break;
					}
				}
			}
		});

		// 확정여부
		calEmployeeSearchUtil.getCheckBoxLocalData(calEmployeeOptionUtil.optFindConfirm);

		// 주문일자
		calEmployeeSearchUtil.dateSearch();

		// 날짜 초기화 이벤트 변경
		calEmployeeSearchUtil.fnInitFirstDateAndLastDate();
		$('[data-id="fnDateBtnC"]').off("mousedown").on("mousedown", function (){
			calEmployeeSearchUtil.fnInitFirstDateAndLastDate();
		});


		calEmployeeSearchUtil.searchTypeKeyword();
		calEmployeeSearchUtil.setFindKeyword();

		if(pageParam.ouId !== undefined) {
			$("#dateSearchStart").val(pageParam.dataSearchStart);
			$("#dateSearchEnd").val(pageParam.dateSearchEnd);
		}
	};




	//---------------Initialize Option Box End ------------------------------------------------
	//-------------------------------  Common Function start ----------------------------------

	// viewModel 초기화
	function fnViewModelInit(){};

	// 기본값 설정
	function fnDefaultSet(){};

	//-------------------------------  Common Function end -------------------------------

	//------------------------------- Html 버튼 바인딩  Start -----------------------------
	/** Common Search*/
	$scope.fnSearch = function( ) {	calEmployeeSubmitUtil.searchUse();	};

	/** Common Clear*/
	$scope.fnClear =function(){	 calEmployeeSubmitUtil.clear();	};

	/** 엑셀 다운로드 */
	$scope.fnExcelDownload = function() {
		var url = "/admin/calculate/employee/getEmployeeUseExcelList";
		calEmployeeSubmitUtil.excelDownload(url);
	};
	//------------------------------- Html 버튼 바인딩  End -------------------------------

	//------------------------------- Validation Start -----------------------------------

	//------------------------------- Validation End -------------------------------------
}); // document ready - END
