/**-----------------------------------------------------------------------------
 * description 		 : 정산관리 > POV I/F > POV I/F
 * @
 * @ 수정일			수정자          수정내용
 * @ ------------------------------------------------------
 * @ 2021.03.03		이명수          최초생성
 * **/
"use strict";

var uploadUrl = "/admin/calculate/pov/addPovExcelUpload";
// ----------------------------------------------------------------------------
// 파일업로드 - EXCEL
// ----------------------------------------------------------------------------
var gFileTagId;
var gFile;

$(document).ready(function() {

	importScript("/js/service/ca/pov/calPovGridColumns.js", function (){
		importScript("/js/service/ca/pov/calPovSearch.js", function (){
			importScript("/js/service/ca/pov/calPovSubmitUtil.js", function (){
				importScript("/js/service/ca/pov/calPovFunction.js", function (){
					fnInitialize();	//Initialize Page Call ---------------------------------
				});
			});
		});
	});

	//Initialize PageR
	function fnInitialize(){
		$scope.$emit("fnIsMenu", { flag : true });
		fnPageInfo({
			PG_ID  : "calPov",
			callback : fnUI
		});
	};

	// 화면 UI 초기화
	function fnUI(){
		fnTranslate();	// 다국어 변환
		fnInitButton();
		fnInitOptionBox();
		fnInitGrid();
		fnInitKendoUpload();
	};

	//--------------------------------- Button Start---------------------------------
	// 버튼 초기화
	function fnInitButton(){
		$("#fnSearch,  #fnClear").kendoButton();
		$("#fnExcelUploadRun1,  #btnTmpErpSendClose").kendoButton();
	};
	//--------------------------------- Button End-----------------------------------

	//------------------------------- Grid Start -------------------------------
	// 그리드 초기화
	function fnInitGrid(){
		calPovGridEventUtil.gridInit(calPovGridUtil.calPovList());
	};

	//-------------------------------  Grid End  -------------------------------

	//---------------Initialize Option Box Start ------------------------------------------------

	// 옵션 초기화
	function fnInitOptionBox(){
		// 정산월
		calPovSearchUtil.getCalculateMonth();
	};

	// 업로드 초기화
	function fnInitKendoUpload(){
		calPovUploadUtil.uploadInit()
	};


	//---------------Initialize Option Box End ------------------------------------------------
	//-------------------------------  Common Function start ----------------------------------

	//-------------------------------  Common Function end -------------------------------

	//------------------------------- Html 버튼 바인딩  Start -----------------------------
	/** Common Search*/
	$scope.fnSearch = function( ) {	calPovSubmitUtil.search();	};

	/** Common Clear*/
	$scope.fnClear =function(){	 calPovSubmitUtil.clear();	};

	/** 엑셀다운로드 */
	$scope.fnExcelDownload = function() {
		calPovSubmitUtil.excelDownload();
	};

	/** Button excelSelect */
	$scope.fnBtnExcelSelect = function(fileType) { $('#' + fileType).trigger('click'); };

	/** Button excelUpload */
	$scope.fnExcelUploadRun = function(uploadType){	 calPovUploadUtil.fnExcelUploadRun(uploadUrl, uploadType);	};

	/** Button interface */
	$scope.fnInterfaceRun = function(interfaceType){	 calPovSubmitUtil.fnInterfaceRun(interfaceType); };
	//------------------------------- Html 버튼 바인딩  End -------------------------------

	//------------------------------- Validation Start -----------------------------------

	//------------------------------- Validation End -------------------------------------
}); // document ready - END
