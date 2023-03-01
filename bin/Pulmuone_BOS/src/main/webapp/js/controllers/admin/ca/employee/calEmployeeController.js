/**-----------------------------------------------------------------------------
 * description 		 : 정산관리 > 임직원정산 > 임직원 지원 정산
 * @
 * @ 수정일			수정자          수정내용
 * @ ------------------------------------------------------
 * @ 2021.03.03		이명수          최초생성
 * **/
"use strict";

var PAGE_SIZE = 20;
var viewModel, defaultGridDs, defaultGridOpt, defaultGrid;
var pageParam = fnGetPageParam();

var defaultGridUrl = "/admin/calculate/employee/getEmployeeList";

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
			PG_ID  : "calEmployee",
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
		calEmployeeGridEventUtil.gridInit(defaultGridUrl, calEmployeeGridUtil.calEmployeeList());


		calEmployeeGridEventUtil.ckeckBoxAllClick();
		calEmployeeGridEventUtil.checkBoxClick();
		calEmployeeGridEventUtil.click();
	};

	//-------------------------------  Grid End  -------------------------------

	//---------------Initialize Option Box Start ------------------------------------------------

	// 옵션 초기화
	function fnInitOptionBox(){
		// 부문 구분
		calEmployeeSearchUtil.getDropDownUrl("findOuId", "ouNm", "ouId", "전체", "/admin/calculate/employee/getOuIdAllList",{});

        if(PG_SESSION.ouId != '' && PG_SESSION.sectorAllViewYn == 'N'){
			$("#findOuId").data("kendoDropDownList").value(PG_SESSION.ouId);
			$("#findOuId").data("kendoDropDownList").enable(false);
		}
		// 확정여부
		calEmployeeSearchUtil.getCheckBoxLocalData(calEmployeeOptionUtil.optFindConfirm);

		// 정산월
		calEmployeeSearchUtil.getCalculateMonth();

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
	$scope.fnSearch = function( ) {	calEmployeeSubmitUtil.search();	};

	/** Common Clear*/
	$scope.fnClear =function(){	 calEmployeeSubmitUtil.clear();	};

	/** 확정완료 */
	$scope.fnConfirmProc = function() {
		calEmployeeSubmitUtil.confirmProc();
	};
	//------------------------------- Html 버튼 바인딩  End -------------------------------

	//------------------------------- Validation Start -----------------------------------

	//------------------------------- Validation End -------------------------------------
}); // document ready - END
