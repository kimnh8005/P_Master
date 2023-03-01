/**----------------------------------------------------------------------------
 * description 		 : 주문/배송관리 > 클레임 관리 > 미출 주문상세리스트
 * @
 * @ 수정일			수정자        수정내용
 * @ --------------------------------------------------------------------------
 * @ 2021.03.09     이규한		최초 생성
 * **/

"use strict";

var PAGE_SIZE = 20;
// 미출 주문상세 그리드
var orderGridDs, orderGridOpt, orderGrid;
// 판매처그룹.직관리 (외부몰)
var sellersGroupDirectMng = "SELLERS_GROUP.DIRECT_MNG";
var sellersGroupVendor		= "SELLERS_GROUP.VENDOR";

// CS 관리 조회
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

if(newURL == 'unreleasedOrderDetailList'){
	var defaultActivateTab = '';
	csParamData = undefined;
	$('#urUserId').val('');
}

$(document).ready(function() {

	// 공통 스크립트 import
	importScript("/js/service/od/order/orderCommDetailGridColumns.js", function (){
		importScript("/js/service/od/order/orderCommSearch.js", function (){
			importScript("/js/service/od/order/orderMgmFunction.js", function (){
				importScript("/js/service/od/order/orderMgmPopups.js", function (){
					fnInitialize();
				});
			});
		});
	});

	// Initialize PageR
	function fnInitialize() {
		$scope.$emit('fnIsMenu', { flag : true });
		fnPageInfo({
			PG_ID  		: 'unreleasedOrderDetailList',
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
		$("#fnSearch,  #fnClear").kendoButton();
	}

	// 미출 주문상세 리스트 조회
	function fnSearch() {
		orderSubmitUtil.search();
	}

	// 초기화
	function fnClear(gridClearYn) {
		orderSubmitUtil.clear();
		if (gridClearYn) $('#orderGrid').gridClear(true);
	}

	// 엑셀 다운로드
	function fnExcelDownload(btnObj) {
		orderSubmitUtil.excelExportDown('/admin/order/getUnreleasedOrderDetailExcelList', btnObj);
	}

	// 상태변경
	function fnChangeStatus(dataItem, btnOrderStatusCd, btnOrderStatus) {

		let claimData = {};
		claimData.odid = dataItem.odid //odid
		claimData.urUserId = dataItem.urUserId; //urUserId
		claimData.guestCi = dataItem.guestCi; //guestCi

		var goodSearchList = new Array();

		let goodOrgData = {};
		goodOrgData.odOrderId = dataItem.odOrderId;
		goodOrgData.odOrderDetlId = dataItem.odOrderDetlId;
		goodOrgData.odClaimDetlId = 0;
		goodOrgData.urWarehouseId = dataItem.urWarehouseId;
		goodOrgData.orderCnt = dataItem.orderCnt;
		goodOrgData.orgOrderCnt = dataItem.orderCnt;
		goodOrgData.claimCnt = dataItem.missCnt - dataItem.missClaimCnt;
		goodOrgData.claimGoodsYn = "N";
		goodSearchList.push(goodOrgData);

		claimData.goodSearchList = goodSearchList;

		claimData.odOrderId = dataItem.odOrderId;
		claimData.odClaimId = 0; //주문클레임pk
		claimData.goodsChange = 0;

		claimData.orderStatusCd = dataItem.orderStatus == "결제완료" ? "IC" : dataItem.orderStatus == "배송준비중" ? "DR" : "DI";  //주문상태코드(현재)
		claimData.orderStatus = dataItem.orderStatus; //주문상태코드명(현재)

		claimData.btnOrderStatusCd = btnOrderStatusCd; //버튼주문상태코드
		claimData.btnOrderStatus = btnOrderStatus; //버튼주문상태코드명
		claimData.putOrderStatusCd = dataItem.orderStatus == "결제완료" ? "CC" : dataItem.orderStatus == "배송준비중" ? "CC" : "RC"; //주문상태코드(변경)

		claimData.putOrderStatus = dataItem.orderStatus == "결제완료" ? "취소완료" : dataItem.orderStatus == "배송준비중" ? "취소완료" : "반품완료"; //주문상태코드명(변경)

		claimData.ifUnreleasedInfoId = dataItem.ifUnreleasedInfoId; // 미출정보PK
		claimData.undeliveredClaimYn = "Y";

		if(btnOrderStatus == "재배송") {
			claimData.putOrderStatusCd = "EC";
			claimData.putOrderStatus = "재배송";
		}

		if (btnOrderStatusCd == 'CS') {
			claimData.putOrderStatusCd = btnOrderStatusCd;
			claimData.putOrderStatus = "CS환불";

			fnKendoPopup({
				id: 'claimMgmCSPopup',
				title: '클레임 상세',
				param: claimData,
				src: '#/claimMgmCSPopup',
				width: '1800px',
				height: '1500px',
				success: function(id, data) {
					if(data.parameter != undefined){
						if(data.parameter.successDo == true) {
							fnSearch();
						} else {
							return false;
						}
					} else {
						return false;
					}
				}
			});
		} else {
			if(stringUtil.getString(dataItem.schDeliveryDt, '') != '') {

				claimData.odOrderDetlDailySchSeq = dataItem.odOrderDetlDailySchSeq;

				fnKendoPopup({
					id: 'claimMgmGreenJuicePopup',
					title: '클레임 상세',
					param: claimData,
					src: '#/claimMgmGreenJuicePopup',
					width: '1800px',
					height: '1500px',
					success: function(id, data) {
						fnSearch();
					}
				});
			} else {
				fnKendoPopup({
					id: 'claimMgmPopup',
					title: '클레임 상세',
					param: claimData,
					src: '#/claimMgmPopup',
					width: '1800px',
					height: '1500px',
					success: function(id, data) {
						fnSearch();
					}
				});
			}
		}
	}
	//--------------------------------- Button End---------------------------------------------


	//------------------------------- Grid Start ----------------------------------------------
	// 그리드 초기화
	function fnInitGrid() {
		// 미출정보 그리드 DataSource
		orderGridDs = fnGetPagingDataSource({
			url      	: "/admin/order/getUnreleasedOrderDetailList",
			pageSize 	: PAGE_SIZE
		});

		// 미출정보 그리드 기본옵션
		orderGridOpt = {
			dataSource	: orderGridDs,
			pageable  	: { pageSizes: [20, 30, 50], buttonCount : 10 },
			navigatable	: true,
			scrollable	: true,
			columns 	: orderDetailGridUtil.unreleasedOrderList()
		};
		orderGrid = $('#orderGrid').initializeKendoGrid(orderGridOpt).cKendoGrid();

		orderGridEventUtil.noView();			// 그리드 전체 건수 설정
		orderGridEventUtil.click();				// 그리드 클릭 설정
		orderGridEventUtil.ckeckBoxAllClick();	// 그리드 체크박스 전체 클릭 설정
		orderGridEventUtil.checkBoxClick();		// 그리드 체크박스 클릭 설정

		// 취소 처리 버튼 이벤트
		$('#orderGrid').on("click", "button[kind=cancel]", function(e) {
			e.preventDefault();
			let dataItem = orderGrid.dataItem($(e.currentTarget).closest("tr"));
			var btnOrderStatusCd = $(this).val();
			var btnOrderStatus = $(this).text();
			fnChangeStatus(dataItem, btnOrderStatusCd, btnOrderStatus);
		});

		// 재배송 처리 버튼 이벤트
		$('#orderGrid').on("click", "button[kind=returnDelivery]", function(e) {
			e.preventDefault();
			let dataItem = orderGrid.dataItem($(e.currentTarget).closest("tr"));
			var btnOrderStatusCd = $(this).val();
			var btnOrderStatus = $(this).text();
			fnChangeStatus(dataItem, btnOrderStatusCd, btnOrderStatus);
		});

		// CS환불 버튼 이벤트
		$('#orderGrid').on("click", "button[kind=csRefund]", function(e) {
			e.preventDefault();
			let dataItem = orderGrid.dataItem($(e.currentTarget).closest("tr"));
			var btnOrderStatusCd = $(this).val();
			var btnOrderStatus = $(this).text();
			fnChangeStatus(dataItem, btnOrderStatusCd, btnOrderStatus);
		});

	}
	//-------------------------------  Grid End  ----------------------------------------------


	//---------------Initialize Option Box Start ----------------------------------------------
	// 옵션 초기화
	function fnInitOptionBox() {

		var searchData = orderSearchUtil.searchDataMutil();
		var searchDataSingle = orderSearchUtil.searchDataSingle();

		if(csParamData != undefined){
			if(csParamData.urUserId != undefined && csParamData.urUserId != ''){
				searchData = orderSearchUtil.csSearchDataMutil();
			}else{
				searchData = orderSearchUtil.searchDataMutil();
			}
		}else{
			searchData = orderSearchUtil.searchDataMutil();
		}

		// 기간검색 데이터
		var dateSearchData = [
			{ "CODE" : "UNRELEASED_DATE"	, "NAME" : "미출일자" },
            { "CODE" : "CREATE_DATE"		, "NAME" : "주문일자" },
            { "CODE" : "PAY_DATE"			, "NAME" : "결제일자" }
        ];

		mutilSearchCommonUtil.default();				// 단일조건,복수조건 radio
		orderSearchUtil.searchTypeKeyword(searchData,searchDataSingle);	// 검색어(복수, 단일) DropDown

		orderSearchUtil.excelDownList(); 				// 엑셀양식유형

		// 판매처그룹
		searchCommonUtil.getDropDownCommCd("sellersGroup", "NAME", "CODE", "판매처 그룹 전체", "SELLERS_GROUP", "Y");
		// 판매처그룹 Change
		$('#sellersGroup').on('change', onPurchaseTargetType);
		// 기간검색 DropDown
		orderSearchUtil.dateSearch(dateSearchData);
		// 미출상태
		orderSearchUtil.getRadioCommData(orderOptionUtil.missStockStatus);
		// 미출사유
		searchCommonUtil.getDropDownCommCd("missStockReason", "NAME", "CODE", "전체", "MISS_STOCK_REASON", "Y");
		// 공급업체
		searchCommonUtil.getDropDownUrl("supplierId", "supplierId", "/admin/comn/getDropDownSupplierList", "supplierName", "supplierId", "전체");
		// 출고처그룹
		searchCommonUtil.getDropDownCommCd("warehouseGroup", "NAME", "CODE", "출고처 그룹 전체", "WAREHOUSE_GROUP", "Y");
		// 출고처그룹 별 출고처
		searchCommonUtil.getDropDownUrl("warehouseId", "warehouseId", "/admin/comn/getDropDownWarehouseGroupByWarehouseList", "warehouseName", "warehouseId", "출고처 전체", "warehouseGroup", "warehouseGroupCode");

		fnTagMkChkBox({
			id    : 'searchEtcFilter',
			tagId : 'searchEtc',
			chkVal: '',
			data: [
				{CODE: "U", NAME: "미처리 내역만 조회"},
				{CODE: "R", NAME: "반품 미출"}
			],
			style : {},
			async : false
		});

		fbCheckboxChange();

		$('#findKeyword').on('keydown',function(e) {
			if (e.keyCode == 13) {
				if ($.trim($(this).val()) == "") {
					fnKendoMessage({message: "검색어를 입력하세요."});
				} else {
					fnSearch();
				}
				return false;
			}
		});
	}
	//---------------Initialize Option Box End ------------------------------------------------


	//-------------------------------  Common Function start ----------------------------------
	//-------------------------------  Common Function end ------------------------------------

	//------------------------------- Html 버튼 바인딩  Start --------------------------------------
	/** Common Search*/
	$scope.fnSearch			= function() { fnSearch(); };
	/** Common Clear*/
	$scope.fnClear			= function(gridClearYn) { fnClear(gridClearYn); };
	/** Common ExcelDownload*/
	$scope.fnExcelDownload 	= function(btnObj) { fnExcelDownload(btnObj); };

	$scope.fnTotalCountView = function() { orderSubmitUtil.totalCountView(); };
	//------------------------------- Html 버튼 바인딩  End ----------------------------------------

	$scope.fnTotalCountView =function(){
		orderSubmitUtil.totalCountView();
	};
	$scope.fnChangeClaimStatusCCPopup =function(){ orderSubmitUtil.changeClaimStatusCCPopup();};

	$scope.fnChangeClaimStatusECPopup =function(){ orderSubmitUtil.changeClaimStatusECPopup();};


	//------------------------------- Html 버튼 바인딩  End -------------------------------

	//------------------------------- Validation Start -----------------------------------

	//------------------------------- Validation Start ----------------------------------------
	// 단일조건 - 입력값을 영문대소문자 + 숫자 + 엔터 + , 로 제한
	//fnInputValidationForAlphabetNumberLineBreakComma("codeSearch");
	// 복수조건(검색어) - 한글 & 영어(대,소) & 숫자 & 특수문자  []~!@#$%^&*()_+|<>?:{}
	fnInputValidationByRegexp("findKeyword", /[^ㄱ-ㅎㅏ-ㅣ가-힣a-zA-Z0-9\{\}\[\]\/?.,;:|\)*~`!^\-_+ <>@\#$%&\'\"\\\(\=]/g);
	//------------------------------- Validation End ------------------------------------------
}); // document ready - END
