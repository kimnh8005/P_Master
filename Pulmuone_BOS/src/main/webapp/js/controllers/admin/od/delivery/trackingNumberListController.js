/**----------------------------------------------------------------------------
 * description 		 : 주문/배송관리 > 배송 관리 > 일괄 송장 입력 내역
 * @
 * @ 수정일			수정자          수정내용
 * @ --------------------------------------------------------------------------
 * @ 2020.12.28     이규한		퍼블수정 및 기능개발
 * **/

'use strict';

var PAGE_SIZE = 20;
var TRACKING_NUMBER_DETAIL_LIST_MENU_ID	= 937;	// 일괄 송장 입력 내역 상세 메뉴ID
var trackingNumberGridDs, trackingNumberGridOpt, trackingNumberGrid;

$(document).ready(function() {
	importScript("/js/service/od/order/orderCommSearch.js", function (){
		fnInitialize();
	});

	// Initialize PageR
	function fnInitialize() {
		$scope.$emit('fnIsMenu', { flag : true });
		fnPageInfo({
			PG_ID  		: 'trackingNumberList',
			callback 	: fnUI
		});
	}

	// 화면 UI 초기화
	function fnUI() {
		fnTranslate();		// 다국어 변환
		fnInitButton();		// 버튼 초기화
		fnInitGrid();		// Initialize Grid
		fnInitOptionBox();	// Initialize Option Box
	}

	//--------------------------------- Button Start-------------------------------------------
	// 버튼 초기화
	function fnInitButton() {
		$('#fnSearch, #fnClear').kendoButton();
	}

	// 일괄 송장 입력 내역 목록 조회
	function fnSearch() {
		if(!validInput()) {
			return false;
		}
		var data = $('#searchForm').formSerialize(true);
		const _pageSize = trackingNumberGrid && trackingNumberGrid.dataSource ? trackingNumberGrid.dataSource.pageSize() : PAGE_SIZE;

		var query = {
				page 		 : 1,
				pageSize 	 : _pageSize,
				filterLength : fnSearchData(data).length,
				filter 		 : {
					filters : fnSearchData(data)
				}
		};
		trackingNumberGridDs.query(query);
	}

	// 초기화
	function fnClear() {
		// 검색 조건 및 그리드 내용 초기화
		$('#searchForm').formClear(true);
//		$('#trackingNumberGrid').data("kendoGrid").dataSource.data([]);
		$('[data-id="fnDateBtn5"]').mousedown();
	}
	//--------------------------------- Button End---------------------------------------------


	//------------------------------- Grid Start ----------------------------------------------
	// 그리드 초기화
	function fnInitGrid() {

		trackingNumberGridDs = fnGetEditPagingDataSource({
			url 		: '/admin/order/delivery/getOrderBulkTrackingNumberList',
			pageSize 	: PAGE_SIZE,
			requestEnd 	: function(e) {}
		});

		trackingNumberGridOpt = {
				dataSource	: trackingNumberGridDs,

				pageable	: {
					pageSizes 	: [ 20, 30, 50 ],
					buttonCount : 5
				},
				editable 	: false,
				navigatable	: true,
				columns 	: [
					{ field : 'rowNum'		, title : 'No'		, width : '40px'	, attributes : { style : 'text-align:center' }}
				,	{ field : 'createDt'	, title : '등록일자'	, width : '80px'	, attributes : { style : 'text-align:center' }}
	            ,   { field : 'successCnt'	, title : '정상'		, width : '100px'	, attributes : { style : 'text-align:center' },format: "{0:n0}" }
	            ,   { field : 'failureCnt'	, title : '실패'		, width : '100px'	, attributes : { style : 'text-align:center' },format: "{0:n0}" }
	            ,	{ field : 'createDt'	, title : '관리자'	, width : '80px'	, attributes : { style : 'text-align:center' },
            			template : function(dataItem) {
            				return dataItem.createNm + ' / ' + dataItem.loginId;
            			}
	            	}
	            ,   { field : 'btn'			, title : '관리'		, width : '100px'	, attributes : { style : 'text-align:center' },
	            		template : function(dataItem) {
	            			// TO-DO 이규한  엑셀 다운로드 권한 관련 부분은 필요시 추가
	            			// 성공, 실패 둘다 존재시
	            			if (dataItem.successCnt > 0 && dataItem.failureCnt > 0) {
	            				return '<button type="button" class="k-button k-button-icontext k-grid-상세보기" kind="btnDtlView">상세보기</button> <button type="button" class="k-button k-button-icontext k-grid-실패내역다운로드" kind="btnFailExcelDown">실패내역 다운로드 </button>';
	            			// 성공만 존재시
	            			} else if (dataItem.successCnt > 0 && dataItem.failureCnt < 1) {
	            				return '<button type="button" class="k-button k-button-icontext k-grid-상세보기" kind="btnDtlView">상세보기</button>'
	            			// 실패만 존재시
	            			} else {
	            				return '<button type="button" class="k-button k-button-icontext k-grid-실패내역다운로드" kind="btnFailExcelDown">실패내역 다운로드 </button>'
	            			}
	            		}
	          		}
	            ,	{ field	: "odBulkTrackingNumberId"	, hidden:true }
	            ,	{ field	: "originNm"				, hidden:true }
	            ]
			};

		trackingNumberGrid = $('#trackingNumberGrid').initializeKendoGrid(trackingNumberGridOpt).cKendoGrid();
		trackingNumberGrid.bind("dataBound", function() {
			$('#pageTotalText').text( kendo.toString(trackingNumberGridDs._total, "n0"));
		});
	}

	// 상세 내역 버튼 이벤트
	$('#trackingNumberGrid').on("click", "button[kind=btnDtlView]", function(e) {
		e.preventDefault();
		var dataItem = trackingNumberGrid.dataItem($(e.currentTarget).closest("tr"));
		var option = new Object();
		option.url = "#/trackingNumberDetailList";				// 일괄 송장 입력 내역 상세 화면 URL
		option.menuId = TRACKING_NUMBER_DETAIL_LIST_MENU_ID;	// 일괄 송장 입력 내역 상세 화면 메뉴 ID

		// 일괄 송장 입력 내역 상세 화면 파라미터
		option.data = {
              paramId : dataItem.odBulkTrackingNumberId		// 일괄송장입력 PK
		};
		fnGoPage(option);
    });

	// 실패내역 다운로드 버튼 이벤트
    $('#trackingNumberGrid').on("click", "button[kind=btnFailExcelDown]", function(e) {
        e.preventDefault();
        let btnObj = $(this);
        var dataItem = trackingNumberGrid.dataItem($(e.currentTarget).closest("tr"));
        var paramData = new Object();
        paramData.odBulkTrackingNumberId = dataItem.odBulkTrackingNumberId;
		fnExcelDownload('/admin/order/delivery/orderBulkTrackingNumberFailExcelDownload', paramData, btnObj);
    });
	//-------------------------------  Grid End  ----------------------------------------------


	//---------------Initialize Option Box Start ----------------------------------------------
	function fnInitOptionBox() {

		// 시작일
		fnKendoDatePicker({
			id: "startDate",
			format: "yyyy-MM-dd",
			btnStartId: "startDate",
			btnEndId: "endDate",
			defVal : fnGetDayAdd(fnGetToday(),-30),
			defType : 'oneMonth',
			change : function(e) {
				fnDateValidation(e, "start", "startDate", "endDate", -30);
				fnValidateCal(e)
			}
		});
		// 종료일
		fnKendoDatePicker({
			id: "endDate",
			format: "yyyy-MM-dd",
			btnStyle: true,
			btnStartId: "startDate",
			btnEndId: "endDate",
			defVal : fnGetToday(),
			defType : 'oneMonth',
			minusCheck: true,
			nextDate: false,
			change : function(e) {
				fnDateValidation(e, "end", "startDate", "endDate");
				fnValidateCal(e)
			}
		});
	}
	//---------------Initialize Option Box End ------------------------------------------------


	//-------------------------------  Common Function start ----------------------------------
	function validInput() {
		var startDate = $("#startDate").val().replace(/\-/ig, '');
		var endDate = $("#endDate").val().replace(/\-/ig, '');
		if(endDate < startDate) {
			fnKendoMessage({message: "시작일보다 종료일이 빠를 수 없습니다."});
			return false;
		}
		return true;
	}
	//-------------------------------  Common Function end ------------------------------------


	//------------------------------- Html 버튼 바인딩  Start --------------------------------------
	/** Common Search */
	$scope.fnSearch 			= function() { fnSearch(); };
	/** Common Clear */
	$scope.fnClear 				= function() { fnClear(); };
	//------------------------------- Html 버튼 바인딩  End ----------------------------------------

}); // document ready - END
