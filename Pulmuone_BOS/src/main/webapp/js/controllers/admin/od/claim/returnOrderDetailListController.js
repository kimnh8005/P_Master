/**----------------------------------------------------------------------------
 * description 		 : 주문/배송관리 > 클레임 관리 > 반품 주문상세리스트
 * @
 * @ 수정일			수정자        수정내용
 * @ --------------------------------------------------------------------------
 * @ 2021.03.05     이규한		최초 생성
 * **/

"use strict";

var PAGE_SIZE = 20;
// 반품 주문상세 그리드
var orderGridDs, orderGridOpt, orderGrid;
//판매처그룹.직관리 (외부몰)
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

if(newURL == 'returnOrderDetailList'){
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
			PG_ID  		: 'returnOrderDetailList',
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

	// 반품 주문상세 리스트 조회
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
		orderSubmitUtil.excelExportDown('/admin/order/getReturnOrderDetailExcelList', btnObj);
	}
	//--------------------------------- Button End---------------------------------------------


	//------------------------------- Grid Start ----------------------------------------------
	// 그리드 초기화
	function fnInitGrid() {
		// 반품정보 그리드 DataSource
		orderGridDs = fnGetPagingDataSource({
			url      	: "/admin/order/getReturnOrderDetailList",
			pageSize 	: PAGE_SIZE
		});

		// 반품정보 그리드 기본옵션
		orderGridOpt = {
			dataSource	: orderGridDs,
			pageable  	: { pageSizes: [20, 30, 50], buttonCount : 10 },
			navigatable	: true,
			scrollable	: true,
			columns 	: orderDetailGridUtil.returnOrderList()
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
				let param = new Object();
				param.odOrderId 		= dataItems.odOrderId;			// 주문PK
				param.odClaimId 		= dataItems.odClaimId;			// 주문 클레임PK
				param.orderStatusCd 	= dataItems.claimStatusCd;		// 현재 클레임상태
				param.orderStatus       = dataItems.orderClaimStatus;    // 현재 클레임상태명

				// 현재 클레임 상태에 따라 변경가능 주문상태값 조회
				let result = orderStatusChangeUtil.fnClaimOrderStatus(dataItems.claimStatusCd);
				param.putOrderStatusCd 	= result.putOrderStatusCd;		// 변경가능 클레임 상태
				param.putOrderStatus 	= result.putOrderStatus;		// 변경가능 클레임 상태명

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

				//orderPopupUtil.open("claimMgmPopup", param);
			} else if (field === "claimAttcCnt") {
				let param = new Object();
				param.odClaimId = dataItems.odClaimId;
				// 클레임사유 첨부파일 팝업
				orderPopupUtil.open("claimReasonMsgPopup", param, null);
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
				searchData = orderSearchUtil.csSearchDataMutilOhter();
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

		// 단일조건,복수조건 radio
		mutilSearchCommonUtil.default();
		// 주문상태
		orderSearchUtil.getCheckBoxUrl(orderOptionUtil.orderStatus("claimState", "D"));
		// 주문상태 - 단일조건
		orderSearchUtil.getCheckBoxUrl(orderOptionUtil.orderStatus("claimStateSingle", "D"));
		// 판매처그룹
		searchCommonUtil.getDropDownCommCd("sellersGroup", "NAME", "CODE", "판매처 그룹 전체", "SELLERS_GROUP", "Y");
		// 판매처그룹 Change
		$('#sellersGroup').on('change', onPurchaseTargetType);
		// 기간검색 DropDown
		orderSearchUtil.dateSearch(dateSearchData);
		// 반품사유 조회
		searchCommonUtil.getDropDownUrl("returnMallReason", "returnMallReason", "/admin/order/getReturnReasonList", "NAME", "CODE", "전체");
		// 회수여부
		fnTagMkRadio(orderOptionUtil.recallYN);
		// 회수연동 타입
		let targetObj = $('[name="recallYN"][value="Y"]').closest("label");
		$('<input type="text" id="recallType" name="recallType" style="width:150px; margin-left: 5px;">').appendTo(targetObj).kendoDropDownList({
			dataTextField	: "NAME",
			dataValueField	: "CODE",
			optionLabel		: "연동 성공여부 (전체)",
			dataSource		: {data: orderSearchUtil.searchrecallTypeData()},
			autoWidth		: true
		});
		// 회수여부 Change
		$('[name="recallYN"]').on('change', function() {
			let dropdownlist = $("#recallType").data("kendoDropDownList");
			$(this).val() == "Y" && $(this).prop('checked') ? dropdownlist.enable(true) : dropdownlist.enable(false);
		})

		$('[name="recallYN"]').trigger('change');

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

	$('#ng-view').on("click","div[name=trackingNoSearch]",function(e){

		var shippingInfo = $("#"+this.id).data();
		var trackingYn = shippingInfo.trackingYn;
		var shippingCompId = shippingInfo.shippingCompId;
		var trackingNo = $("#"+this.id).text();
		var shippingCompNm = shippingInfo.shippingCompNm;
		var httpRequestTp = shippingInfo.httpRequestTp;
		var trackingUrl = shippingInfo.trackingUrl;
		var invoiceParam = shippingInfo.invoiceParam;
		var logisticsCd = shippingInfo.logisticscd;

		if(this.id == "trackingNoSearch") {
			shippingInfo = $(this).closest('div[data-tracking-url]').data();
			trackingNo = $(this).closest('div[data-tracking-url]').text();
			trackingYn = shippingInfo.trackingYn;
			shippingCompId = shippingInfo.shippingCompId;
			shippingCompNm = shippingInfo.shippingCompNm;
			httpRequestTp = shippingInfo.httpRequestTp;
			trackingUrl = shippingInfo.trackingUrl;
			invoiceParam = shippingInfo.invoiceParam;
			logisticsCd = shippingInfo.logisticscd;

		}

		var url = '/admin/order/delivery/getOrderClaimReturnDeliveryTrackingList';

		if (trackingYn == 'Y') {
			let params	= new Object();

			params.logisticsCd 		= logisticsCd; // psShippingCompId
			params.trackingNo 		= trackingNo; // trackingNo

			fnAjax({
				url     : url,
				params  : params,
				async   : true,
				success : function(resultData) {
					if (resultData.responseCode != 0) {
						fnKendoMessage({message: resultData.responseMessage});
						return false;
					}

					if (fnIsEmpty(resultData.tracking) || resultData.tracking.length < 1) {
						fnKendoMessage({message : "배송정보가 존재하지 않습니다."});
						return false;
					}

					let paramObj = new Object();
					paramObj.shippingCompNm		= shippingCompNm;			// shippingCompNm (택배사명)
					paramObj.trackingNo			= trackingNo;			// trackingNo (송장번호)
					paramObj.psShippingCompId 	= shippingCompId;			// psShippingCompId (택배사코드)
					paramObj.trackingArr		= resultData.tracking;				// 배송상태 타임라인 리스트
					paramObj.iframeYn			= "N";								// Iframe여부
					orderPopupUtil.open("deliverySearchPopup", paramObj);
				},
				fail : function(resultData, resultCode) {
					fnKendoMessage({message : resultCode.message});
				},
				isAction : 'select'
			});
		} else {
			e.preventDefault();
			if (fnIsEmpty(shippingInfo.trackingUrl)) {
				fnKendoMessage({message : "택배사 배송추적 URL정보가 존재하지 않습니다."});
				return false;
			}
			let paramObj = new Object();
			paramObj.httpRequestTp		= httpRequestTp;			// httpRequestTp (HTTP 전송방법)
			paramObj.trackingUrl		= trackingUrl;			// trackingUrl (송장추적 URL)
			paramObj.trackingNo 		= trackingNo;			// trackingNo (송장번호)
			paramObj.invoiceParam		= invoiceParam;			// invoiceParam (송장파라미터 명)
			paramObj.iframeYn			= "Y";								// Iframe여부
			orderPopupUtil.open("deliverySearchPopup", paramObj);
		}
	});


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
