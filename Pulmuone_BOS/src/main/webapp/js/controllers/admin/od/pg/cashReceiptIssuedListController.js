/**-----------------------------------------------------------------------------
 * description 		 : 주문/배송관리 > 증빙문서 관리 > 현금영수증 발급 내역
 * @
 * @ 수정일			수정자          수정내용
 * @ ------------------------------------------------------
 * @ 2020.12.07		김승우          최초생성
 * **/
"use strict";

var PAGE_SIZE = 20;
var viewModel, gridDs, gridOpt, grid;
var pageParam = fnGetPageParam();

//cs 관리 조회처리
var csParamData;
if(defaultActivateTab == undefined){
	var defaultActivateTab;
}else{
	var csData;
	if(csData != undefined){
		$('#urUserId').val(csData.urUserId);
		csParamData = {"urUserId" : csData.urUserId};
	}
}

if(newURL == 'cashReceiptIssuedList'){
	var defaultActivateTab = '';
	csParamData = undefined;
	$('#urUserId').val('');
}

$(document).ready(function() {

	importScript("/js/service/od/pg/pgComm.js", function (){
		importScript("/js/service/od/odComm.js", function (){
			importScript("/js/service/od/order/orderCommSearch.js", function (){
					fnInitialize();
			});
		});
	});

	//Initialize PageR
	function fnInitialize(){
		$scope.$emit("fnIsMenu", { flag : true });
		fnPageInfo({
			PG_ID  : "cashReceiptIssuedList",
			callback : fnUI,
		});
	};

	// 화면 UI 초기화
	function fnUI(){
		
		fnTranslate();	// 다국어 변환
		fnInitButton();
		fnInitGrid();
		fnInitOptionBox();
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

		gridDs = fnGetPagingDataSource({
			url      : "/admin/order/document/getCashReceiptIssuedList",
			pageSize : PAGE_SIZE
		});

		gridOpt = {
			dataSource: gridDs,
			pageable  : { pageSizes: [20, 30, 50], buttonCount : 10 },
			navigatable : true,
			height : 755,
			scrollable : true,
			columns :  [
				{ field: 'no'					, title: 'No'		, width: '40px'	, attributes: { style: 'text-align:center' }, template : "<span class='row-number'></span>"}
				, { field: 'cashReceiptType'	, title: '구분'		, width: '60px'	, attributes :{ style: 'text-align:center' }
					,template: function(dataItem){
						if(dataItem.cashReceiptType == 'CASH_RECEIPT.DEDUCTION'){ return '소득공제';
						}else if(dataItem.cashReceiptType == 'CASH_RECEIPT.PROOF'){ return '지출증빙';
						}else if(dataItem.cashReceiptType == 'CASH_RECEIPT.USER'){ return '사용자발급';
						}else{return '미발급';}
					}}
				, { field: 'createDt'			, title: '주문일자'	, width: '60px'	, attributes: { style: 'text-align:center;' } }
				, { field: 'odid'				, title: '주문번호'	, width: '90px'	, attributes: { style: 'text-align:center;text-decoration: underline;' } }
				, { field: 'payTp'				, title: '결제수단'	, width: '80px', attributes: { style: 'text-align:center' }, template: "#=(payTp=='PAY_TP.VIRTUAL_BANK') ? '무통장입금<br>(가상계좌)' : '실시간계좌이체'#"}
				, { field: 'pgService'			, title: 'PG구분'	, width: '60px', attributes: { style: 'text-align:center' }
					, template: function(dataItem){
						if(dataItem.pgService.includes('KCP')){ return 'KCP';
						}else{return '이니시스';}
					}}
				, { field: 'statusNm'			, title: '결제상태'	, width: '60px'	, attributes: { style: 'text-align:center' } }
				, { field: 'taxablePrice'		, title: '과세'		, width: '60px'	, attributes: { style: 'text-align:center' } , format: "{0:n0}" }
				, { field: 'nonTaxablePrice'	, title: '면세'		, width: '60px'	, attributes: { style: 'text-align:center' } , format: "{0:n0}"}
				, { field: 'paymentPrice'		, title: '발급금액'	, width: '60px'	, attributes: { style: 'text-align:center' } , format: "{0:n0}"}
				, { field: 'cashReceiptNo'		, title: '증빙번호'	, width: '80px'	, attributes: { style: 'text-align:center' }
					, template: function(dataItem){
						if(dataItem.cashReceiptNo == ''){ return '-';
						}else{
							// if(dataItem.cashReceiptType == 'CASH_RECEIPT.DEDUCTION'){
							// 	return dataItem.cashReceiptNo.replace(/(?<=.{4})./gi,"*");
							// }
							return dataItem.cashReceiptNo;
						}
					}}
				/*, { field: 'issueType'			, title: '발급여부'	, width: '80px'	, attributes: { style: 'text-align:center' }, template: "#=(cashReceiptNo=='') ? '미발급' : '발급완료'#"}*/
				, { field: 'cashReceiptAuthNo'	, title: '승인번호'	, width: '150px'	, attributes: { style: 'text-align:center' } }
				, { title: '관리'				, width: "100px" , attributes: { style: 'text-align:center', class: 'forbiz-cell-readonly' }
					,command: [{ name: 'cEdit1', text: '발급', imageClass: "k-i-custom", className: "f-grid-setting btn-white btn-s", iconClass: "k-icon", click: fnCashReceiptIssue
						, visible: function(dataItem) { return dataItem.cashReceiptNo == '' }}
						,{ name: 'cEdit2', text: '발급정보', imageClass: "k-i-custom", className: "f-grid-setting btn-white btn-s", iconClass: "k-icon", click: fnCashReceiptIssueInfo
							, visible: function(dataItem) { return dataItem.cashReceiptNo != '' }}]}
				, { field : "odOrderId"			, title : "주문PK"				, hidden : true }
				, { field : "billUrl"			, title : "현금영수증 조회 URL"	, hidden : true }
				, { field : "urUserId"			, title : "회원PK"				, hidden : true }
				, { field : "guestCi"			, title : "비회원 CI"			, hidden : true }
				, { field : "odPaymentMasterId"	, title : "주문결제PK"			, hidden : true }
			]
		};

		grid = $('#grid').initializeKendoGrid( gridOpt ).cKendoGrid();

		grid.bind("dataBound", function(e) {
			$('#countTotalSpan').text( kendo.toString(gridDs._total, "n0") );

			let rowNum = gridDs._total - ((gridDs._page - 1) * gridDs._pageSize);
			$("#grid tbody > tr .row-number").each(function(index){
				$(this).html(rowNum);
				rowNum--;
			});
		});

		$("#grid").on("click", "tbody > tr > td", function(e) {
			let filedId = e.currentTarget.dataset.field;	//컬럼 정보
			let dataItems = grid.dataItem(grid.select()); //선택데이터 정보

			if(filedId === "odid") {

				let odid = dataItems.odid

				var orderMenuId = $("#lnbMenuList li").find("a[data-menu_depth='3'].active").attr("id");
				window.open("#/orderMgm?orderMenuId="+ orderMenuId +"&odid="+odid ,"popForm_"+odid);
			}
		});
	};

    //-------------------------------  Grid End  -------------------------------
    // 현금영수증 발급 정보
    function fnCashReceiptIssueInfo(e){
		e.preventDefault();
		var aMap = grid.dataItem($(e.currentTarget).closest('tr'));

		if(aMap.billUrl != ''){
			window.open(aMap.billUrl,"_blank");
		}
	}

	// 현금영수증 발급
	function fnCashReceiptIssue(e){
		e.preventDefault();
		var aMap = grid.dataItem($(e.currentTarget).closest('tr'));

		pgPopup(aMap, function() {
			pgSubmitUtil.search();
		});
	}

	//---------------Initialize Option Box Start ------------------------------------------------

	// 옵션 초기화
	function fnInitOptionBox(){
        mutilSearchCommonUtil.default();
        initDateTimePicker();
        initSearchCondition();
	};

	//---------------Initialize Option Box End ------------------------------------------------
	//-------------------------------  Common Function start ----------------------------------

    // viewModel 초기화
    function fnViewModelInit(){};

    // 기본값 설정
    function fnDefaultSet(){};

    function fnExcelExport(btnObj) {
		var data = $("#searchForm").formSerialize(true);
		fnExcelDownload('/admin/order/document/getCashReceiptIssuedListExportExcel', data, btnObj);
    }
    
    // 검색 기간
    function initDateTimePicker() {
		//등록일/수정일 시작날짜
		fnKendoDatePicker({
			id : 'startDate',
			format : 'yyyy-MM-dd',
			btnStartId: "startDate",
			btnEndId: "endDate",
			defVal: fnGetDayAdd(fnGetToday(),-6),
			defType : 'oneWeek',
			change : function(e) {
				fnDateValidation(e, "start", "startDate", "endDate", -6);
				fnValidateCal(e)
			}

		});

		//등록일/수정일 종료날짜
		fnKendoDatePicker({
			id : 'endDate',
			format : 'yyyy-MM-dd',
			btnStyle : true,
			btnStartId : 'startDate',
			btnEndId : 'endDate',
			defVal: fnGetToday(),
			defType : 'oneWeek',
			minusCheck: true,
			nextDate: false,
			change : function(e) {
				fnDateValidation(e, "end", "startDate", "endDate");
				fnValidateCal(e)
			}
		});
    }

    // 단일 검색 조건
    function initSearchCondition() {
      pgSearchUtil.searchCondition();
    }


	//-------------------------------  Common Function end -------------------------------

	//------------------------------- Html 버튼 바인딩  Start -----------------------------
	/** Common Search*/
	$scope.fnSearch = function( ) {	pgSubmitUtil.search();	};

	/** Common Clear*/
	$scope.fnClear =function(){	pgSubmitUtil.clear(); };

	// Common ExcelDownload
    $scope.fnExcelDownload =function(btnObj){	fnExcelExport(btnObj); };

	//------------------------------- Html 버튼 바인딩  End -------------------------------

	//------------------------------- Validation Start -----------------------------------

	// fnInputValidationForAlphabetNumberLineBreakComma("codeSearch");

	//------------------------------- Validation End -------------------------------------
}); // document ready - END
