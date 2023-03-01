/**-----------------------------------------------------------------------------
 * description 		 : 직연동 카테고리 관리
 * @
 * @ 수정일			수정자		수정내용
 * @ ------------------------------------------------------
 * @ 2021.12.27		송지윤		최초생성
 * @
 * **/
"use strict";

var LAST_PAGE = null;   // 최종 페이지 (페이징 기억 관련)
var PAGE_SIZE = 20;
var viewModel, aGridDs, aGridOpt, aGrid;
var pageParam = fnGetPageParam();													//GET Parameter 받기
var paramDataSort = pageParam.paramDataSort;
var publicStorageUrl = fnGetPublicStorageUrl();

var CUR_SERVER_URL = fnGetServerUrl().mallUrl;

var initSearch = true;

$(document).ready(function() {

	fnInitialize();	//Initialize Page Call ---------------------------------

	//Initialize PageR
	function fnInitialize(){
		$scope.$emit("fnIsMenu", { flag : true });

		fnPageInfo({
			PG_ID  : "goodsDirectLinkCategoryMapping",
			callback : fnUI
		});
	};

	// 화면 UI 초기화
	function fnUI(){

		fnInitButton();
		fnInitOptionBox();
		fnInitGrid();
		fnSearch();
		// fnViewModelInit();PageMethod.startPage
		// fnDefaultSet();

	};

	//----------------------------------------------------- Button -----------------------------------------------------

	// 버튼 초기화
	function fnInitButton(){
		$("#fnSearch,  #fnExcelDownload").kendoButton();

		$('#kendoPopupImagePreview').kendoWindow({
            visible: false,
            modal: true
        });
	};

	//----------------------------------------------------- Button -----------------------------------------------------

	//--------------------------------------------------- Grid Start ---------------------------------------------------

	// 그리드 초기화
	function fnInitGrid(){
		// ------------------------------------------------------------------------
		// 최종 페이지 관련 (페이징 기억 관련)
		// ------------------------------------------------------------------------
		// var lastPage = sessionStorage.getItem('lastPage');
		// LAST_PAGE = lastPage ? JSON.parse(lastPage) : null;

		aGridDs = fnGetPagingDataSource({
			url	  : "/admin/goods/list/getGoodsGearCategoryMappingList",
			pageSize : PAGE_SIZE
		});

		aGridOpt = {
			dataSource: aGridDs,
			pageable: { buttonCount: 10, pageSizes: [20, 30, 50] },
			noRecordMsg : '검색된 목록이 없습니다.',
			navigatable : true,
			scrollable  : true,
			columns : [
						{ field : "No" 					, title : "No" 						, width : 10, attributes : {style : "text-align:center"}, template : "<span class='row-number'></span>" }
					, 	{ field : 'gearCateType'		, title : "직연동몰"					, width : 20, attributes : {style : "text-align:center;"} }
					,	{ field : 'ilCtgryStdId'		, title : "표준 카테고리 코드"			, width : 20, attributes : {style : "text-align:center;"} }
					,	{ field : 'ilCtgryStdFullName'	, title : "풀무원 표준 카테고리"		, width : 80, attributes : {style : "text-align:center;"} }
					,	{ field : 'categoryId'			, title : "카테고리 코드"				, width : 40, attributes : {style : "text-align:center;"} }
					,	{ field : 'categoryName'		, title : "직연동몰 카테고리"			, width : 40, attributes : {style : "text-align:center;"} }
					,	{ field : 'mappingYn'			, title : "직연동몰 카테고리 매핑 여부", width : 40, attributes : {style : "text-align:center;"} }
					, 	{ field : 'management'			, title : '관리' 					, width : 20, attributes : {style:'text-align:center;'}
							, template: function(dataItem) {
								var btnStr ='<div id="pageMgrButtonArea" class="textCenter">' +
											'	<button type="button" class="btn-lightgray btn-s"  kind="btnEdit" >수정</button>' +
											'</div>';
								return  btnStr;
							}
						}
			]
		};

		aGrid = $('#aGrid').initializeKendoGrid( aGridOpt ).cKendoGrid();

		aGrid.bind("dataBound", function() {
			$('#countTotalSpan').text(aGridDs._total);

			var row_num = aGridDs._total - ((aGridDs._page - 1) * aGridDs._pageSize);
			$('#aGrid tbody > tr .row-number').each(function(index){
				$(this).html(row_num);
				row_num--;
			});
		});

		// ------------------------------------------------------------------------
		// 수정 버튼 클릭
		// ------------------------------------------------------------------------
		$('#aGrid').on('click', 'button[kind=btnEdit]', function(e) {
			let dataItem = aGrid.dataItem($(e.currentTarget).closest('tr'));
			fnKendoPopup({
				id			: "goodsDirectLinkCategoryMappingPopup",
				title		: "표준 카테고리 매핑",		// 해당되는 Title 명 작성
				width		: "1200px",
				height		: "500px",
				src			: "#/goodsDirectLinkCategoryMappingPopup",
				param		: dataItem,
				success		: function(id, data) {
				}
			});
		});
	};

	//---------------------------------------------------  Grid End  ---------------------------------------------------

	//------------------------------------------------ Option Box Start ------------------------------------------------

	// 옵션 초기화
	function fnInitOptionBox(){

		// 카테고리 구분
		fnKendoDropDownList({
			id : "categoryType",
			tagId : "categoryType",
			data : [ {"CODE" : "CATEGORY_STANDARD", "NAME" : "표준카테고리"}
			]
		});

		// 표준카테고리 대분류
		fnKendoDropDownList({
			id : "categoryStandardDepth1",
			tagId : "categoryStandardDepth1",
			url : "/admin/comn/getDropDownCategoryStandardList",
			params : { "depth" : "1" },
			textField : "categoryName",
			valueField : "categoryId",
			blank : "대분류",
			async : false
		});

		// 표준카테고리 중분류
		fnKendoDropDownList({
			id : "categoryStandardDepth2",
			tagId : "categoryStandardDepth2",
			url : "/admin/comn/getDropDownCategoryStandardList",
			textField : "categoryName",
			valueField : "categoryId",
			blank : "중분류",
			async : false,
			cscdId : "categoryStandardDepth1",
			cscdField : "categoryId"
		});

		// 표준카테고리 소분류
		fnKendoDropDownList({
			id : "categoryStandardDepth3",
			tagId : "categoryStandardDepth3",
			url : "/admin/comn/getDropDownCategoryStandardList",
			textField : "categoryName",
			valueField : "categoryId",
			blank : "소분류",
			async : false,
			cscdId : "categoryStandardDepth2",
			cscdField : "categoryId"
		});

		// 표준카테고리 세분류
		fnKendoDropDownList({
			id : "categoryStandardDepth4",
			tagId : "categoryStandardDepth4",
			url : "/admin/comn/getDropDownCategoryStandardList",
			textField : "categoryName",
			valueField : "categoryId",
			blank : "세분류",
			async : false,
			cscdId : "categoryStandardDepth3",
			cscdField : "categoryId"
		});

		// ------------------------------------------------------------------------
		// 매핑여부 - 공통코드 아님
		// ------------------------------------------------------------------------
		fnTagMkRadio({
			id      : 'mappingYn'
		,	tagId   : 'mappingYn'
		,	data    : 	[
						{ 'CODE' : 'ALL'   , 'NAME' : '전체'   }
					, 	{ 'CODE' : 'Y'  , 'NAME' : '설정'     }
					, 	{ 'CODE' : 'N'  , 'NAME' : '미설정' }
						]
		, 	chkVal  : 'ALL'
		, 	style   : {}
		});

		// 직원동몰 구분
		fnKendoDropDownList({
			id 			: 'gearMappingGubun',
			data 		: [
							{ 'CODE' : 'NAVER', 'NAME' : '네이버장보기' }
						 ],
			textField 	: "NAME",
			valueField 	: "CODE"
		});

		
	};

	//----------------------------------------------Initialize Option Box End ------------------------------------------
	//-------------------------------------------------- Function start ------------------------------------------------

	// 조회
	function fnSearch (){
		var data = $("#searchForm").formSerialize(true);
		aGridDs.read(data);
	}

	// 엑셀다운로드
	function fnExcelExport (){
		var data = $('#searchForm').formSerialize(true);
		fnExcelDownload("/admin/goods/list/getGoodsDirectLinkCategoryMappingListExcel", data);
	}

	function fnClear() {
		$('#searchForm').formClear(true);
		fnDefaultSet(); // before
	};

	//-------------------------------  Common Function end -------------------------------
	/* 초기화 */
	$scope.fnClear = function() { fnClear(); };
	/* 조회 */
	$scope.fnSearch = function() { fnSearch(); };
	/* 엑셀 다운로드 */
	$scope.fnExcelExport = function( ){ fnExcelExport(); };

}); // document ready - END
