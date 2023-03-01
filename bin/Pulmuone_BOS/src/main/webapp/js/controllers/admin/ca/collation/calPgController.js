/**-----------------------------------------------------------------------------
 * description 		 : 정산관리 > 대사관리 > PG 거래 내역 대사
 * @
 * @ 수정일			수정자          수정내용
 * @ ------------------------------------------------------
 * @ 2021.03.05		이명수          최초생성
 * **/
"use strict";

var PAGE_SIZE = 20;

var PAGE_SIZE = 20;
var viewModel, defaultGridDs, defaultGridOpt, defaultGrid;
var pageParam = fnGetPageParam();

var defaultGridUrl = "/admin/calculate/collation/getPgList";
var uploadUrl = "/admin/calculate/collation/addPgExcelUpload?pgUploadGubun=";
// 판매처그룹.직관리 (외부몰)
var sellersGroupDirectMng = "SELLERS_GROUP.DIRECT_MNG";
var sellersGroupVendor		= "SELLERS_GROUP.VENDOR";
// ----------------------------------------------------------------------------
// 파일업로드 - EXCEL
// ----------------------------------------------------------------------------
var gFileTagId;
var gFile;

$(document).ready(function() {

	importScript("/js/service/ca/collation/calCollationGridColumns.js", function (){
		importScript("/js/service/ca/collation/calCollationSearch.js", function (){
			importScript("/js/service/ca/collation/calCollationSubmitUtil.js", function (){
				importScript("/js/service/ca/collation/calCollationFunction.js", function (){
					fnInitialize();
				});
			});
		});
	});

	//Initialize PageR
	function fnInitialize(){
		$scope.$emit("fnIsMenu", { flag : true });
		fnPageInfo({
			PG_ID  : "calPg",
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
		fnInitKendoUpload();
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
		calCollationGridEventUtil.gridInit(defaultGridUrl, calCollationGridUtil.calPgList());
        calCollationGridEventUtil.calPgClick();

	};
	//-------------------------------  Grid End  -------------------------------
	//------------------------------- Grid Start -------------------------------
	// 업로드 초기화
	function fnInitKendoUpload(){
		calCollationUploadUtil.uploadInit()
	};
	//-------------------------------  Grid End  -------------------------------

	//---------------Initialize Option Box Start ------------------------------------------------

	// 옵션 초기화
	function fnInitOptionBox(){
		// 업로드 구분
		calCollationSearchUtil.getDropDownCommCd("pgUploadGubun", "NAME", "CODE", "PG사 선택", "PG_SERVICE", "Y", "", "", "");

		// 검색 구분
		calCollationSearchUtil.getDropDownCommCd("findPgGubn", "NAME", "CODE", "전체", "PG_SERVICE", "Y", "", "", "");

		// 기간조회
		calCollationSearchUtil.dateSearch();

		// 날짜 초기화 이벤트 변경
		calCollationSearchUtil.fnInitFirstDateAndLastDate("dateSearchStart", "dateSearchEnd");
		$('[data-id="fnDateBtnC"]').off("mousedown").on("mousedown", function (){
			calCollationSearchUtil.fnInitFirstDateAndLastDate("dateSearchStart", "dateSearchEnd");
		});

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
	$scope.fnSearch = function( ) {	calCollationSubmitUtil.search();	};

	/** Common Clear*/
	$scope.fnClear =function(){	 calCollationSubmitUtil.clearPg(); calCollationSearchUtil.fnInitFirstDateAndLastDate("dateSearchStart", "dateSearchEnd"); };

	/** Button excelSelect */
	$scope.fnBtnExcelSelect = function(fileType) {
		if($("#pgUploadGubun").val() == "") {
			fnKendoMessage({message: 'PG사를 선택 해주세요.'});
			return false;
		}
		$('#' + fileType).trigger('click');
	};
	/** Button excelUpload */
	$scope.fnExcelUploadRun = function(){	 calCollationUploadUtil.fnExcelUploadRun(uploadUrl  + $("#pgUploadGubun").val());	};

	/** 관리자 검색 */
	$scope.fnGrantAuthEmployeeSearch = function(){	 fnGrantAuthEmployeeSearch();};

	//------------------------------- Html 버튼 바인딩  End -------------------------------

	//------------------------------- Validation Start -----------------------------------

	//------------------------------- Validation End -------------------------------------
}); // document ready - END
