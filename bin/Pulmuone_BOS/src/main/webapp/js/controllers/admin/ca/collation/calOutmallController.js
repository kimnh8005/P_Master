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

var defaultGridUrl = "/admin/calculate/collation/getOutmallList";
var uploadUrl = "/admin/calculate/collation/addOutmallExcelUpload";
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
//		fnSearch();
//		orderSubmitUtil.search();
	};

	//--------------------------------- Button Start---------------------------------
	// 버튼 초기화
	function fnInitButton(){
		$("#fnSearch,  #fnClear").kendoButton();
	};

	// 업로드 파일첨부 버튼 클릭
	function fnExcelUpload(file, fileTagId) {

		var formData = new FormData();
		formData.append('bannerImage', file);
		formData.append("originNm",		$("#fileInfoDiv").text());
		formData.append("sendSmsYn",		($("#sendSmsYn").is(":checked") == true) ? "Y" : "N");

		$.ajax({
			url         : '/admin/calculate/collation/addOutmallExcelUpload'
			, data        : formData
			, type        : 'POST'
			, contentType : false
			, processData : false
			, async       : false
			, beforeSend : function(xhr) {
				xhr.setRequestHeader('authMenuID', CLICKED_MENU_ID);
			}
			, success     : function(data) {
				let localMessage = "";
				if(data.code == 'FILE_ERROR' || data.code == 'EXCEL_TRANSFORM_FAIL' || data.code == 'EXCEL_UPLOAD_NONE'){
					localMessage = data.message;
				}else{
					localMessage = "[결과] : " + data.message + "<BR>" +
						" [총 건수] : " + data.data.totalCnt + "<BR>" +
						" [성공 건수] : " + data.data.successCnt + "<BR>" +
						" [실패 건수] : " + data.data.failureCnt;

					if (data.code != "0000"){
						localMessage += "<BR>" +" [실패 메세지] : " + data.failMessage;
					}
				}
				gFile = "";
				$("#fileInfoDiv").empty();


				fnKendoMessage({
					message : localMessage,
					ok : function(e) {}
				});
			}
		});
	}
	//--------------------------------- Button End-----------------------------------

	//------------------------------- Grid Start -------------------------------
	// 그리드 초기화
	function fnInitGrid(){
		calCollationGridEventUtil.gridInit(defaultGridUrl, calCollationGridUtil.calOutmallList());
		calCollationGridEventUtil.click();
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

		// 구분
		calCollationSearchUtil.findCollectionMallType(calCollationOptionUtil.findCollectionMallTypeData())

		// 기간조회
		calCollationSearchUtil.dateSearch();

		// 날짜 초기화 이벤트 변경
		calCollationSearchUtil.fnInitFirstDateAndLastDate("dateSearchStart", "dateSearchEnd");
		$('[data-id="fnDateBtnC"]').off("mousedown").on("mousedown", function (){
			calCollationSearchUtil.fnInitFirstDateAndLastDate("dateSearchStart", "dateSearchEnd");
		});

		$("input[name=sellersGroup]").each(function() {
			$(this).bind("change", onPurchaseTargetType);
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
	$scope.fnClear =function(){	 calCollationSubmitUtil.clearPg(); calCollationSearchUtil.fnInitFirstDateAndLastDate("dateSearchStart", "dateSearchEnd");	};

	/** Button excelSelect */
	$scope.fnBtnExcelSelect = function(fileType) {$('#' + fileType).trigger('click');};
	/** Button excelUpload */
	$scope.fnExcelUploadRun = function(){	 calCollationUploadUtil.fnExcelUploadRun(uploadUrl);	};

	/** 관리자 검색 */
	$scope.fnGrantAuthEmployeeSearch = function(){	 fnGrantAuthEmployeeSearch();};

	/** 샘플 다운로드 */
	$scope.fnSamepleFormDownload = function(event) { calCollationUploadUtil.sampleDownload("/contents/excelsample/caloutmall/calOutmallExcelSample.xlsx") }

	//------------------------------- Html 버튼 바인딩  End -------------------------------

	//------------------------------- Validation Start -----------------------------------

	//------------------------------- Validation End -------------------------------------
}); // document ready - END
