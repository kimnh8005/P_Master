/**-----------------------------------------------------------------------------
 * description 		 : 주문/배송관리 > 주문관리 > 주문리스트
 * @
 * @ 수정일			수정자          수정내용
 * @ ------------------------------------------------------
 * @ 2020.11.26		강윤경          최초생성
 * @ 2020.11.30		김승우			수정
 * @ 2020.12.15		석세동			수정
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
			PG_ID  : "orderList",
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

		// 파라메타 있을때 조회 3개월 세팅
		let tmp = getCookie("csUserParamData")
		if (tmp !== undefined && tmp !== "") {
			$(".date-controller button").each(function() {
				$(this).attr("fb-btn-active", false);
			})

			$("button[data-id='fnDateBtn7']").attr("fb-btn-active", true);

			var today = fnGetToday();

			$("#dateSearchStart").val(fnGetDayMinus(today, 90));
			$("#dateSearchEnd").val(today);

			//$scope.fnSearch();
		}
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
			columns : orderGridUtil.orderList()
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
		// let searchData;
		//고객명검색
		let searchCustomerData;
		//연락처검색
		let searchContactData;
		//주문검색
		let searchDataForOrder;
		//상품검색
		let searchDataForGoods;

		if(csParamData != undefined){
			if(csParamData.urUserId != undefined && csParamData.urUserId != ''){
				// searchData = orderSearchUtil.csSearchDataMutil();
				searchCustomerData = orderSearchUtil.csSearchDataForCustomerMulti();
				searchContactData = orderSearchUtil.csSearchDataForContactMulti();
				searchDataForOrder = orderSearchUtil.csSearchDataDetailForOrderMutil();
				searchDataForGoods = orderSearchUtil.csSearchDataDetailForGoodsMutil();

			}else{
				// searchData = orderSearchUtil.searchDataMutil();
				searchCustomerData = orderSearchUtil.searchDataForCustomerMulti();
				searchContactData = orderSearchUtil.searchDataForContactMulti();
				searchDataForOrder = orderSearchUtil.searchDataDetailForOrderMutil();
				searchDataForGoods = orderSearchUtil.searchDataDetailForGoodsMutil();
			}
		}else{
			// searchData = orderSearchUtil.searchDataMutil();
			searchCustomerData = orderSearchUtil.searchDataForCustomerMulti();
			searchContactData = orderSearchUtil.searchDataForContactMulti();
			searchDataForOrder = orderSearchUtil.searchDataDetailForOrderMutil();
			searchDataForGoods = orderSearchUtil.searchDataDetailForGoodsMutil();
		}

		var searchDataSingle = orderSearchUtil.searchDataSingle();

		var dateSearchData = [
            { "CODE" : "CREATE_DATE", "NAME" : "주문일자" },
            { "CODE" : "PAY_DATE", "NAME" : "결제일자" }
        ];

		mutilSearchCommonUtil.default();//단일조건,복수조건 radio
		orderSearchUtil.searchTypeKeywordSingle(searchDataSingle); //dropDown
		orderSearchUtil.searchTypeKeywordForOrderDetail("searchMultiTypeForCustomer",searchCustomerData); //dropDown
		orderSearchUtil.searchTypeKeywordForOrderDetail("searchMultiTypeForContact",searchContactData); //dropDown
		orderSearchUtil.searchTypeKeywordForOrderDetail("searchMultiTypeForOrder",searchDataForOrder); //dropDown 공통
		orderSearchUtil.searchTypeKeywordForOrderDetail("searchMultiTypeForGoods",searchDataForGoods); //dropDown 공통
		orderSearchUtil.dateSearch(dateSearchData,-7); //기간검색

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

		//searchCommonUtil.getCheckBoxCommCd("discountTypeCode", "DISCOUNT_TYPE", "Y");	//할인유형
		//searchCommonUtil.dateSearch(); //defVal, defType 사용 필요

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


	};




	//---------------Initialize Option Box End ------------------------------------------------
	//-------------------------------  Common Function start ----------------------------------

    // viewModel 초기화
    function fnViewModelInit(){};

    // 기본값 설정
    function fnDefaultSet(){
        if(pageParam != undefined){
            if(pageParam.selectConditionType == 'singleSection'){
                $('#selectConditionType_1').prop("checked",true);
                $("#singleSection").show();
                $("#multiSection").hide();

                $("#searchSingleType").data('kendoDropDownList').value(pageParam.searchSingleType);
            }
            $('#codeSearch').val(pageParam.codeSearch);

        }
    };


	//-------------------------------  Common Function end -------------------------------

	//------------------------------- Html 버튼 바인딩  Start -----------------------------
	/** Common Search*/
	$scope.fnSearch = function( ) {	orderSubmitUtil.search();	};

	/** Common Clear*/
	$scope.fnClear =function(){	 orderSubmitUtil.clear();	};

	$scope.fnExcelDownload =function(btnObj){
		// 주문리스트는 사유 필수 세팅
		$("#privacyIncludeYn_0").remove();
		$("body").append("<input type='hidden' id='privacyIncludeYn_0' value='Y'/>")
		orderSubmitUtil.excelExportDown('/admin/order/getOrderExcelList', btnObj);
	};

	//------------------------------- Html 버튼 바인딩  End -------------------------------

	//------------------------------- Validation Start -----------------------------------

	//fnInputValidationForAlphabetNumberLineBreakComma("codeSearch");
	// 입력가능 : 한글 & 영어(대,소) & 숫자 & 특수문자  []~!@#$%^&*()_+|<>?:{}
	fnInputValidationByRegexp("findKeywordForAddr", /[^ㄱ-ㅎㅏ-ㅣ가-힣a-zA-Z0-9\{\}\[\]\/?.,;:|\)*~`!^\-_+ <>@\#$%&\'\"\\\(\=]/g);
	fnInputValidationByRegexp("findKeywordForCustomer", /[^ㄱ-ㅎㅏ-ㅣ가-힣a-zA-Z0-9\{\}\[\]\/?.,;:|\)*~`!^\-_+ <>@\#$%&\'\"\\\(\=]/g);
	fnInputValidationByRegexp("findKeywordForOrder", /[^ㄱ-ㅎㅏ-ㅣ가-힣a-zA-Z0-9\{\}\[\]\/?.,;:|\)*~`!^\-_+ <>@\#$%&\'\"\\\(\=]/g);
	fnInputValidationByRegexp("findKeywordForGoods", /[^ㄱ-ㅎㅏ-ㅣ가-힣a-zA-Z0-9\{\}\[\]\/?.,;:|\)*~`!^\-_+ <>@\#$%&\'\"\\\(\=]/g);
	//------------------------------- Validation End -------------------------------------
}); // document ready - END
