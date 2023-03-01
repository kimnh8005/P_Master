/**----------------------------------------------------------------------------
 * description 		 : 주문/배송관리 > 환불 관리 > 환불 주문상세리스트
 * @
 * @ 수정일			수정자        수정내용
 * @ --------------------------------------------------------------------------
 * @ 2021.03.08     이규한		최초 생성
 * **/

"use strict";

var PAGE_SIZE = 20;
// 환불 주문상세 그리드
var orderGridDs, orderGridOpt, orderGrid;
// 판매처그룹.직관리 (외부몰)
var sellersGroupDirectMng = "SELLERS_GROUP.DIRECT_MNG";
var sellersGroupVendor		= "SELLERS_GROUP.VENDOR";
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

if(newURL == 'refundOrderDetailList'){
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
			PG_ID  		: 'refundOrderDetailList',
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
		$("#fnSearch,  #fnClear, #fnExcelDownload").kendoButton();
	}

	// 환불 주문상세 리스트 조회
	function fnSearch() {
		orderSubmitUtil.search();
	}

	// 초기화
	function fnClear(gridClearYn) {
		// 공통 초기화 호출
		orderSubmitUtil.clear();
		// 기간검색 - 클레임 승인일자 선택
		$("#dateSearchType").data("kendoDropDownList").value("CLAIM_APPROVED_DATE");
		//if (gridClearYn) $('#orderGrid').gridClear(true);
	}

	// 엑셀 다운로드
	function fnExcelDownload(btnObj) {
		orderSubmitUtil.excelExportDown('/admin/order/getRefundOrderDetailExcelList', btnObj);
	}
	//--------------------------------- Button End---------------------------------------------


	//------------------------------- Grid Start ----------------------------------------------
	// 그리드 초기화
	function fnInitGrid() {
		// 환불정보 그리드 DataSource
		orderGridDs = fnGetPagingDataSource({
			url      	: "/admin/order/getRefundOrderDetailList",
			pageSize 	: PAGE_SIZE
		});

		// 환불정보 그리드 기본옵션
		orderGridOpt = {
			dataSource	: orderGridDs,
			pageable  	: { pageSizes: [20, 30, 50], buttonCount : 10 },
			navigatable	: true,
			scrollable	: true,
			columns 	: orderDetailGridUtil.refundOrderList()
		};
		orderGrid = $('#orderGrid').initializeKendoGrid(orderGridOpt).cKendoGrid();

		orderGridEventUtil.noView();			// 그리드 전체 건수 설정
		orderGridEventUtil.click();				// 그리드 클릭 설정
		orderGridEventUtil.ckeckBoxAllClick();	// 그리드 체크박스 전체 클릭 설정
		orderGridEventUtil.checkBoxClick();		// 그리드 체크박스 클릭 설정

		// 반품 상세 그리드 클릭
		$("#orderGrid").on("click", "tbody > tr > td", function(e) {
			let field 		= e.currentTarget.dataset.field;
			let dataItems 	= orderGrid.dataItem(orderGrid.select()); // 선택데이터 정보

			// 클레임번호
			if ( field === "odClaimId") {

				let isRefundFlag = $(this).find("u.refundList").length > 0;

				let param = new Object();
				param.odOrderId 		= dataItems.odOrderId;			// 주문PK
				param.odClaimId 		= dataItems.odClaimId;			// 주문 클레임PK
				param.orderStatusCd 	= dataItems.claimStatusCd;		// 현재 클레임상태
				param.orderStatus 		= dataItems.orderClaimStatus;	// 현재 클레임상태명

				// 현재 클레임 상태에 따라 변경가능 주문상태값 조회
				let result = orderStatusChangeUtil.fnClaimOrderStatus(dataItems.claimStatusCd);
				//param.putOrderStatusCd 	= result.putOrderStatusCd;		// 변경가능 클레임 상태
				//param.putOrderStatus 	= result.putOrderStatus;		// 변경가능 클레임 상태명
				param.putOrderStatusCd 	= dataItems.claimStatusCd;		// 변경가능 클레임 상태
				param.putOrderStatus 	= dataItems.orderClaimStatus;		// 변경가능 클레임 상태명

				// 주문상세 정보
				let orderDetailList = new Array();
				let orderItem = new Object();
				orderItem.odOrderId 		= dataItems.odOrderId;
				orderItem.odOrderDetlId 	= dataItems.odOrderDetlId;
				orderItem.odClaimDetlId 	= dataItems.odClaimDetlId;
				orderItem.claimCnt 			= 0;
				orderItem.goodsPrice 		= dataItems.claimGoodsPrice;
				orderItem.goodsCouponPrice 	= dataItems.claimGoodsCouponPrice;
				orderItem.cartCouponPrice 	= dataItems.claimCartCouponPrice;
				orderItem.shippingPrice 	= dataItems.claimShippingPrice;
				orderItem.urWarehouseId 	= dataItems.urWarehouseId;
				orderItem.goodsChange 		= 0; // 전체
				orderDetailList.push(orderItem);
				param.goodSearchList 		= orderDetailList;
				param.isRefundFlag			= isRefundFlag;

				orderPopupUtil.open("claimMgmPopup", param);
			}
		});
	}
	//-------------------------------  Grid End  ----------------------------------------------


	//---------------Initialize Option Box Start ----------------------------------------------

	// 옵션 초기화
	function fnInitOptionBox(){

		var searchData = orderSearchUtil.searchDataMutilOhter();
		var searchDataSingle = orderSearchUtil.searchDataSingle();

		if(csParamData != undefined){
			if(csParamData.urUserId != undefined && csParamData.urUserId != ''){
				searchData = orderSearchUtil.csSearchDataMutil();
			}else{
				searchData = orderSearchUtil.searchDataMutil();
			}
		}

		// 기간검색 데이터
		var dateSearchData = [
            { "CODE" : "CLAIM_REQUEST_DATE"	, "NAME" : "클레임요청일자" },
            { "CODE" : "CLAIM_APPROVED_DATE", "NAME" : "클레임승인일자" },
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
		// 기간검색 - 클레임 승인일자 선택
		$("#dateSearchType").data("kendoDropDownList").value("CLAIM_APPROVED_DATE");
		// 클레임 구분(취소완료, 반품완료, CS환불)
		orderSearchUtil.getCheckBoxUrl(orderOptionUtil.orderStatus("claimState", "S"));
		// 공급업체
		searchCommonUtil.getDropDownUrl("supplierId", "supplierId", "/admin/comn/getDropDownSupplierList", "supplierName", "supplierId", "전체");
		// 출고처그룹
		searchCommonUtil.getDropDownCommCd("warehouseGroup", "NAME", "CODE", "출고처 그룹 전체", "WAREHOUSE_GROUP", "Y");
		// 출고처그룹 별 출고처
		searchCommonUtil.getDropDownUrl("warehouseId", "warehouseId", "/admin/comn/getDropDownWarehouseGroupByWarehouseList", "warehouseName", "warehouseId", "출고처 전체", "warehouseGroup", "warehouseGroupCode");

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
	};
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


	//------------------------------- Validation Start ----------------------------------------
	// 단일조건 - 입력값을 영문대소문자 + 숫자 + 엔터 + , 로 제한
	//fnInputValidationForAlphabetNumberLineBreakComma("codeSearch");
	// 복수조건(검색어) - 한글 & 영어(대,소) & 숫자 & 특수문자  []~!@#$%^&*()_+|<>?:{}
	fnInputValidationByRegexp("findKeyword", /[^ㄱ-ㅎㅏ-ㅣ가-힣a-zA-Z0-9\{\}\[\]\/?.,;:|\)*~`!^\-_+ <>@\#$%&\'\"\\\(\=]/g);
	//------------------------------- Validation End ------------------------------------------
}); // document ready - END
