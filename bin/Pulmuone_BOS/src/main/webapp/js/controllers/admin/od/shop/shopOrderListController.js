/**-----------------------------------------------------------------------------
 * description 		 : 주문/배송관리 > 매장 주문관리 > 매장 주문리스트
 * @
 * @ 수정일			수정자          수정내용
 * @ ------------------------------------------------------
 * @ 2021.06.18		최윤지          최초생성
 * **/
"use strict";

var PAGE_SIZE = 20;
var viewModel, orderGridDs, orderGridOpt, orderGrid;
var pageParam = fnGetPageParam();

// 판매처그룹.직관리 (외부몰)
var sellersGroupDirectMng	= "SELLERS_GROUP.DIRECT_MNG";
var sellersGroupVendor		= "SELLERS_GROUP.VENDOR";

var csParamData;
if(defaultActivateTab == undefined ){
	var defaultActivateTab;
}else{
	var csData;
	if(csData != undefined){
		$('#urUserId').val(csData.urUserId);
		csParamData = {"urUserId" : csData.urUserId};
	}
}

if(newURL == 'orderList'){
	var defaultActivateTab = '';
	csParamData = undefined;
	$('#urUserId').val('');

}

$(document).ready(function() {

	importScript("/js/service/od/order/orderGridColumns.js", function (){
		importScript("/js/service/od/order/orderCommSearch.js", function (){
			fnInitialize();	//Initialize Page Call ---------------------------------
		});
	});

	//Initialize PageR
	function fnInitialize(){
		$scope.$emit("fnIsMenu", { flag : true });
		fnPageInfo({
			PG_ID  : "shopOrderList",
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
		var form = $("#searchForm");

		// form.formClear(false);
		var data = form.formSerialize(true);

		if($('input[name=selectConditionType]:checked').length > 0) {
			data['selectConditionType'] = $('input[name=selectConditionType]:checked').val();
		}
		orderGridDs = fnGetPagingDataSource({
			url      : "/admin/order/getOrderList",
			pageSize : PAGE_SIZE,
			filterLength: fnSearchData(data).length,
			filter: {
				filters: fnSearchData(data)
			}
		});

		orderGridOpt = {
			dataSource: orderGridDs,
			pageable  : { pageSizes: [20, 30, 50], buttonCount : 10 },
			navigatable : true,
			height : 755,
			scrollable : true,
			columns : orderGridUtil.shopOrderList()
		};

		orderGrid = $('#orderGrid').initializeKendoGrid( orderGridOpt ).cKendoGrid();

		orderGrid.bind("dataBound", function(e) {
			$('#countTotalSpan').text( kendo.toString(orderGridDs._total, "n0") );

			let rowNum = orderGridDs._total - ((orderGridDs._page - 1) * orderGridDs._pageSize);
 		    $("#orderGrid tbody > tr .row-number").each(function(index){
 		      $(this).html(rowNum);
 		      rowNum--;
 		    });
		});


		$("#orderGrid").on("click", "tbody > tr > td", function(e) {
			 	let filedId = e.currentTarget.dataset.field;	//컬럼 정보
			 	let dataItems = orderGrid.dataItem(orderGrid.select()); //선택데이터 정보

			 	if(filedId === "odid") {

			 		//let odid = dataItems.odid
			 		let odid = dataItems.odid

					var orderMenuId = $("#lnbMenuList li").find("a[data-menu_depth='3'].active").attr("id");
					//setItem( "orderMenuId", inputMenuId );
					//localStorageUtil.put("orderMenuId", orderMenuId);
					window.open("#/orderMgm?orderMenuId="+ orderMenuId +"&odid="+odid ,"popForm_"+odid);

			 		//fnGoPage(option);

			 	//회원상세 (임시 1. brandName -> 주문자정보(urUserId)로 변경 필요 2.purchaseNonmemberYn -> 회원주문 조건 변경 필요 )
//			 	} else if (filedId === "buyerNm" && dataItems.purchaseNonmemberYn === 'Y' ) {
			 	} else if (filedId === "buyerNm" ) {
					if (fnIsProgramAuth("USER_POPUP_VIEW") == true) {
						let urUserId = dataItems.urUserId;
						if (urUserId > 0) {
							fnKendoPopup({
								id: 'buyerPopup',
								title: fnGetLangData({nullMsg: '회원상세'}),
								param: {"urUserId": urUserId},
								src: '#/buyerPopup',
								width: '1200px',
								height: '640px',
								success: function (id, data) {

								}
							});
						}
					}
			 	}
	        });
	};

	//-------------------------------  Grid End  -------------------------------

	//---------------Initialize Option Box Start ------------------------------------------------

	// 옵션 초기화
	function fnInitOptionBox(){
		var searchData;

		if(csParamData != undefined){
			if(csParamData.urUserId != undefined && csParamData.urUserId != ''){
				searchData = orderSearchUtil.csSearchDataMutil();
			}else{
				searchData = orderSearchUtil.searchDataMutil();
			}
		}else{
			searchData = orderSearchUtil.searchDataMutil();
		}

		var searchDataSingle = orderSearchUtil.searchDataSingle();

		var dateSearchData = [
			{ "CODE" : "DELIVERY_DATE", "NAME" : "도착예정일" }, // 도착예정일 추가(매장 주문리스트)
            { "CODE" : "CREATE_DATE", "NAME" : "주문일자" },
            { "CODE" : "PAY_DATE", "NAME" : "결제일자" }
        ];

		mutilSearchCommonUtil.default();//단일조건,복수조건 radio
		orderSearchUtil.searchTypeKeyword(searchData,searchDataSingle); //dropDown 공통
		orderSearchUtil.shopOrderDateSearch(dateSearchData); //기간검색
		$('[data-id="fnDateBtnC"]').off("mousedown").on("mousedown", function (){
			$("#dateSearchStart").data("kendoDatePicker").value( fnGetToday() ); // 시작 기본날짜
        	$("#dateSearchEnd").data("kendoDatePicker").value( fnGetDayAdd(fnGetToday(), 1) ); // 종료 기본날짜
			$(".date-controller button[fb-btn-active=true]").attr("fb-btn-active", false);
		});

		//화면설계서 변경 예정
		 // 판매처그룹
		searchCommonUtil.getDropDownCommCd("sellersGroup", "NAME", "CODE", "판매처 그룹 전체", "SELLERS_GROUP", "Y", "", "", "");

		//주문상태
		orderSearchUtil.getCheckBoxUrl(orderOptionUtil.orderStatus("orderState", "O"));

		//클레임
		orderSearchUtil.getCheckBoxUrl(orderOptionUtil.orderStatus("claimState", "C"));

		//결제수단
		orderSearchUtil.getCheckBoxCommCd(orderOptionUtil.paymentMethod);

		//주문자유형
		orderSearchUtil.getCheckBoxCommCd(orderOptionUtil.buyerType);

		//유형
		orderSearchUtil.getCheckBoxCommCd(orderOptionUtil.agentType);

		// 공급업체
		const SUPPLIER_ID = "supplierId";
		searchCommonUtil.getDropDownUrl(SUPPLIER_ID, SUPPLIER_ID, "/admin/comn/getDropDownSupplierList", "supplierName", "supplierId", "공급업체 전체");

		// 출고처그룹
		searchCommonUtil.getDropDownCommCd("warehouseGroup", "NAME", "CODE", "출고처 그룹 전체", "WAREHOUSE_GROUP", "Y");

		// 출고처그룹 별 출고처
		const WAREHOSE_ID = "warehouseId";
		searchCommonUtil.getDropDownUrl(WAREHOSE_ID, WAREHOSE_ID, "/admin/comn/getDropDownWarehouseGroupByWarehouseList", "warehouseName", "warehouseId", "출고처 선택", "", "");
		
		//매장명 그룹
		const UR_STORE_ID = "urStoreId";
		searchCommonUtil.getDropDownUrl(UR_STORE_ID, UR_STORE_ID, "/admin/ur/urCompany/getStoreList", "storeName", "urStoreId", "전체", "", "", "");
		//searchCommonUtil.getCheckBoxCommCd("discountTypeCode", "DISCOUNT_TYPE", "Y");	//할인유형
		//searchCommonUtil.dateSearch(); //defVal, defType 사용 필요

		const $urStoreId = $("#urStoreId").data("kendoDropDownList");
		if( $urStoreId ) {
			$urStoreId.bind("change", function (e) {
				var urStoreId = $('#urStoreId').val();

				if(fnNvl(urStoreId) != ''){
					fnAjax({
						url     : "/admin/ur/store/getStoreDeliveryDropDownList",
						method : "GET",
						params : { "urStoreId" : $('#urStoreId').val() , "shopStockStatus" : "shopStockStatus"},
						success : function( data ){
							let deliveryType = $("#deliveryType").data("kendoDropDownList");
							deliveryType.setDataSource(data.storeDeliveryArea);
							$("#deliveryType").data("kendoDropDownList").enable( true );
						},
						error : function(xhr, status, strError){
							fnKendoMessage({ message : xhr.responseText });
						},
						isAction : "select"
					});
				} else {
					dropDownListDisabled();
				}

			});
		}
		// 배송방법
		const DELIVERY_TYPE = "deliveryType";
		searchCommonUtil.getDropDownUrl(DELIVERY_TYPE, DELIVERY_TYPE, "/admin/ur/store/getStoreDeliveryDropDownList", "storeDeliveryTpNm", "storeDeliveryTp", "배송방법 전체", "", "");

		const $deliveryType = $("#deliveryType").data("kendoDropDownList");

		if( $deliveryType ) {
			$deliveryType.bind("change", function (e) {
				if(fnNvl($('#deliveryType').val()) != '') {
					fnAjax({
						url: "/admin/ur/store/getStoreDeliveryDropDownList",
						method: "GET",
						params: {"urStoreId": $('#urStoreId').val(), "storeDeliveryTp": $('#deliveryType').val()},
						success: function (data) {
							let scheduleNo = $("#scheduleNo").data("kendoDropDownList");
							scheduleNo.setDataSource(data.storeDeliveryArea);
							$("#scheduleNo").data("kendoDropDownList").enable(true);
						},
						error: function (xhr, status, strError) {
							fnKendoMessage({message: xhr.responseText});
						},
						isAction: "select"
					});
				} else {
					$("#scheduleNo").data("kendoDropDownList").value("");
					$("#scheduleNo").data("kendoDropDownList").enable(false);
				}
			});
		}
		
		// 회차
		const SCHEDULE_NO = "scheduleNo";
		searchCommonUtil.getDropDownUrl(SCHEDULE_NO, SCHEDULE_NO, "/admin/ur/store/getStoreDeliveryDropDownList", "scheduleNo", "scheduleNo", "회차 전체", "", "");

		$("input[name=sellersGroup]").each(function() {
			$(this).bind("change", onPurchaseTargetType);
		});

		fbCheckboxChange();

		$('#findKeyword').on('keydown',function(e){
			if (e.keyCode == 13) {
				if ($.trim($(this).val()) == ""){
					fnKendoMessage({message: "검색어를 입력하세요."});
				} else {
					orderSubmitUtil.search();
				}
				return false;
			}
		});

		dropDownListDisabled();
	};

	//배송방법 / 회차 드롭다운리스트 비활성화
	function dropDownListDisabled() {
		$("#deliveryType").data("kendoDropDownList").value("");
		$("#deliveryType").data("kendoDropDownList").enable(false);
		$("#scheduleNo").data("kendoDropDownList").value("");
		$("#scheduleNo").data("kendoDropDownList").enable(false);
	}



	//---------------Initialize Option Box End ------------------------------------------------
	//-------------------------------  Common Function start ----------------------------------

    // viewModel 초기화
    function fnViewModelInit(){};

    // 기본값 설정
    function fnDefaultSet(){};

	//-------------------------------  Common Function end -------------------------------

	//------------------------------- Html 버튼 바인딩  Start -----------------------------
	/** Common Search*/
	$scope.fnSearch = function( ) {	orderSubmitUtil.search(true);	};

	/** Common Clear*/
	$scope.fnClear =function(){	 orderSubmitUtil.shopOrderListclear();	};

	$scope.fnExcelDownload =function(btnObj){
		orderSubmitUtil.excelExportDown('/admin/order/getShopOrderExcelList', btnObj);
	};



	//------------------------------- Html 버튼 바인딩  End -------------------------------

	//------------------------------- Validation Start -----------------------------------

	//fnInputValidationForAlphabetNumberLineBreakComma("codeSearch");
	// 입력가능 : 한글 & 영어(대,소) & 숫자 & 특수문자  []~!@#$%^&*()_+|<>?:{}
	fnInputValidationByRegexp("findKeyword", /[^ㄱ-ㅎㅏ-ㅣ가-힣a-zA-Z0-9\{\}\[\]\/?.,;:|\)*~`!^\-_+ <>@\#$%&\'\"\\\(\=]/g);
	//------------------------------- Validation End -------------------------------------
}); // document ready - END
