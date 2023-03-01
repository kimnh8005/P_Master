/**----------------------------------------------------------------------------
 * description 		 : 주문/배송관리 > 배송 관리 > 일괄 송장 입력 내역 상세
 * @
 * @ 수정일			수정자          수정내용
 * @ --------------------------------------------------------------------------
 * @ 2020.12.29     이규한		퍼블수정 및 기능개발
 * **/

"use strict";

var PAGE_SIZE 			= 20;
var trackingNumberDetlGridDs, trackingNumberDetlGridOpt, trackingNumberDetlGrid;
var pageParam 			= fnGetPageParam();

$(document).ready(function() {

	importScript("/js/service/od/odComm.js");

	// Initialize Page Call
	fnInitialize();

	// Initialize PageR
	function fnInitialize() {
		$scope.$emit('fnIsMenu', { flag : true });
		fnPageInfo({
			PG_ID  		: 'trackingNumberDetailList',
			callback 	: fnUI
		});
	}

	// 화면 UI 초기화
	function fnUI() {
		fnTranslate();		// 다국어 변환
		fnInitButton();		// 버튼 초기화
		fnInitGrid();		// Initialize Grid
		fnInitOptionBox();	// Initialize Option Box

		if (fnNvl(pageParam) != '' && fnNvl(pageParam.paramId) != '') {
			$("#odBulkTrackingNumberId").val(pageParam.paramId);
			fnSearch();
		}
	}

	//--------------------------------- Button Start-------------------------------------------
	// 버튼 초기화
	function fnInitButton(){
		$('#fnSearch, #fnClear, #fnDetailExcelDownload').kendoButton();
	}

	// 일괄 송장 입력 내역 상세 목록 조회
	function fnSearch() {

		var searchVaild = true;

		// trim 으로 공백 제거, 2글자 이상 입력해야 함
		if ($.trim($('#codeSearch').val()).length == 1) {
			searchVaild = false;
		    fnKendoMessage({
		        message : '코드 검색 조회시 2글자 이상 입력해야 합니다.',
		        ok : function() {
		            return false;
		        }
		    });
		}


		if (searchVaild) {
			var data = $('#searchForm').formSerialize(true);
			data.odBulkTrackingNumberId = Number(data.odBulkTrackingNumberId);
			const _pageSize = trackingNumberDetlGrid && trackingNumberDetlGrid.dataSource ? trackingNumberDetlGrid.dataSource.pageSize() : PAGE_SIZE;

			var query = {
					page 		 : 1,
					pageSize 	 : _pageSize,
					filterLength : fnSearchData(data).length,
					filter 		 : {
						filters : fnSearchData(data)
					}
			};
			trackingNumberDetlGridDs.query(query);
		}
	}

	// 초기화
	function fnClear() {
		// 검색 조건 및 그리드 내용 초기화
		$('#searchForm').formClear(true);
		$("#odBulkTrackingNumberId").val(pageParam.paramId);
		$('#trackingNumberDetlGrid').data("kendoGrid").dataSource.data([]);
		$("#createDt").text('');
	}

	// 엑셀다운로드
	function fnDetailExcelDownload(btnObj) {
		var data = $('#searchForm').formSerialize(true);
		data.odBulkTrackingNumberId = Number(data.odBulkTrackingNumberId);

		fnExcelDownload('/admin/order/delivery/orderBulkTrackingNumberDetlExcelDownload', data, btnObj);
	}
	//--------------------------------- Button End---------------------------------------------


	//------------------------------- Grid Start ----------------------------------------------
	// 그리드 초기화
	function fnInitGrid() {
		trackingNumberDetlGridDs = fnGetEditPagingDataSource({
			url 		: '/admin/order/delivery/getOrderBulkTrackingNumberDetlList',
			pageSize 	: PAGE_SIZE,
			requestEnd 	: function(e) {}
		});

		trackingNumberDetlGridOpt = {
				dataSource	: trackingNumberDetlGridDs,

				pageable	: {
					pageSizes 	: [ 20, 30, 50 ],
					buttonCount : 5
				},
				editable 	: false,
				navigatable	: true,
				columns 	: [
					{ field : 'rowNum'			, title : 'No'			, width : '40px'	, attributes : { style : 'text-align:center' }}
				,	{ field : 'odid'			, title : '주문번호'		, width : '80px'	, attributes : { style : 'text-align:center' }}
					,   { field : 'odOrderDetlSeq'	, title : '주문순번'	, width : '100px'	, attributes : { style : 'text-align:center' }}
	            //,   { field : 'odOrderDetlId'	, title : '주문상세번호'	, width : '100px'	, attributes : { style : 'text-align:center' }}
	            ,   { field : 'shippingCompNm'		, title : '택배사'		, width : '100px'	, attributes : { style : 'text-align:center' }}
	            ,   { field : 'trackingNo'		, title : '송장번호'		, width : '100px'	, attributes : { style : 'text-align:center' }}
	            ,	{ field	: "odOrderId"		, hidden:true }
	            ,	{ field	: "logisticsCd"		, hidden:true }
	            ]
		};

		trackingNumberDetlGrid = $('#trackingNumberDetlGrid').initializeKendoGrid( trackingNumberDetlGridOpt ).cKendoGrid();
		trackingNumberDetlGrid.bind("dataBound", function() {
			$('#pageTotalText').text( kendo.toString(trackingNumberDetlGridDs._total, "n0"));

			// 등록일자
			trackingNumberDetlGridDs._total > 0 ? $("#createDt").text(trackingNumberDetlGridDs._data[0].createDt) : $("#createDt").text("");
		});
	}
	//-------------------------------  Grid End  ----------------------------------------------

	//---------------Initialize Option Box Start ----------------------------------------------
	// 옵션 초기화
	function fnInitOptionBox() {

		// 검색조건 Select
		fnKendoDropDownList ({
			id 		: "searchType",
			data 	:
				[
					{"CODE" : "orderNo"			, "NAME" : "주문번호"},
					{"CODE" : "orderDetailNo"	, "NAME" : "주문상세번호"},
					{"CODE" : "trackingNo"		, "NAME" : "송장번호"},
				],
					valueField 	: "CODE",
					textField 	: "NAME",
					blank 		: "선택해주세요."
		});

		// 검색조건 Textarea (숫자, 엔터, 콤마(,)만 입력 가능)
		var objectId		= "codeSearch";					// 컴퍼넌트ID
		var regexpFilter 	= /[^0-9,\n]/g;					// 제약조건 정규식
		var message 		= "숫자, 엔터, ','만 입력 가능합니다.";	// 메세지
		fnInputValidationByRegexp(objectId, regexpFilter, message);
	}
	//---------------Initialize Option Box End ------------------------------------------------


	//-------------------------------  Common Function start ----------------------------------

	//-------------------------------  Common Function end ------------------------------------


	//------------------------------- Html 버튼 바인딩  Start --------------------------------------
	/** Common Search */
	$scope.fnSearch 			 = function() { fnSearch(); };
	/** Common Clear */
	$scope.fnClear 				 = function() { fnClear(); };
	/** Common ExcelDownload */
	$scope.fnDetailExcelDownload = function(btnObj) { fnDetailExcelDownload(btnObj); };
	//------------------------------- Html 버튼 바인딩  End ----------------------------------------
}); // document ready - END
