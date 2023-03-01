/**-----------------------------------------------------------------------------
 * description 		 : 주문/배송관리 > 배송관리 > 배송중 주문상세 리스트
 * @
 * @ 수정일			수정자          수정내용
 * @ ------------------------------------------------------
 * @ 2020.12.04		김승우          최초생성
 * @ 2020.12.21		석세동			수정
 * **/
"use strict";

var PAGE_SIZE = 20;
var viewModel, orderGridDs, orderGridOpt, orderGrid;
var pageParam = fnGetPageParam();

var sellersGroupDirectMng = "SELLERS_GROUP.DIRECT_MNG";
var sellersGroupVendor		= "SELLERS_GROUP.VENDOR";
$(document).ready(function() {
	importScript("/js/service/od/order/orderCommDetailGridColumns.js", function (){
		importScript("/js/service/od/order/orderCommSearch.js", function (){
			importScript("/js/service/od/order/orderMgmFunction.js", function (){
					fnInitialize();
			});
		});
	});

	//Initialize PageR
	function fnInitialize(){
		$scope.$emit("fnIsMenu", { flag : true });
		fnPageInfo({
			PG_ID  : "deliveryIngOrderDetailList",
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
//			url      : "/admin/order/getOrderDetailList",
			url      : "/admin/order/getDeliveryIngOrderDetailList",
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
			columns : orderDetailGridUtil.deliveryIngList()
		};

		orderGrid = $('#orderGrid').initializeKendoGrid( orderGridOpt ).cKendoGrid();

		orderGridEventUtil.noView();
		orderGridEventUtil.click();
		orderGridEventUtil.ckeckBoxAllClick();
		orderGridEventUtil.checkBoxClick();
	};

	//-------------------------------  Grid End  -------------------------------

	//---------------Initialize Option Box Start ------------------------------------------------

	// 옵션 초기화
	function fnInitOptionBox(){

		var searchData = orderSearchUtil.searchDataMutilOhter();
		var searchDataSingle = orderSearchUtil.searchDataSingle();
		var dateSearchData = [
			{ "CODE" : "DELIVERY_ING_DATE", "NAME" : "배송중일자" },
			{ "CODE" : "CREATE_DATE", "NAME" : "주문일자" },
			{ "CODE" : "ORDER_IF_DATE", "NAME" : "주문 I/F" },
			{ "CODE" : "SHIPPING_DATE", "NAME" : "출고예정일" },
			{ "CODE" : "DELIVERY_DATE", "NAME" : "도착예정일" },
			{ "CODE" : "DELIVERY_ING_DATE", "NAME" : "배송중" },
			{ "CODE" : "BUY_FINALIZED_DATE", "NAME" : "구매확정" },
			{ "CODE" : "CANCEL_COMPLETE_DATE", "NAME" : "취소완료" },
			{ "CODE" : "RETURN_COMPLETE_DATE", "NAME" : "반품완료" },
        ];

		mutilSearchCommonUtil.default();//단일조건,복수조건 radio
		orderSearchUtil.searchTypeKeyword(searchData,searchDataSingle); //dropDown 공통
		orderSearchUtil.dateSearch(dateSearchData); //기간검색

		// 엑셀 양식 유형 - 엑셀다운로드 양식을 위한 공통
		orderSearchUtil.excelDownList();

		//화면설계서 변경 예정
		 // 판매처그룹
		searchCommonUtil.getDropDownCommCd("sellersGroup", "NAME", "CODE", "판매처 그룹 전체", "SELLERS_GROUP", "Y", "", "", "");

		//결제수단
		orderSearchUtil.getCheckBoxCommCd(orderOptionUtil.paymentMethod);

		// 주문유형
		orderSearchUtil.getCheckBoxCommCd(orderOptionUtil.orderType);

		//유형
		orderSearchUtil.getCheckBoxCommCd(orderOptionUtil.agentType);

		// 배송방법
		orderSearchUtil.getCheckBoxCommCd(orderOptionUtil.delivType);

		// 공급업체
		const SUPPLIER_ID = "supplierId";
		searchCommonUtil.getDropDownUrl(SUPPLIER_ID, SUPPLIER_ID, "/admin/comn/getDropDownSupplierList", "supplierName", "supplierId", "공급업체 전체");

		// 출고처그룹
		searchCommonUtil.getDropDownCommCd("warehouseGroup", "NAME", "CODE", "출고처 그룹 전체", "WAREHOUSE_GROUP", "Y");

		const $warehouseGroup = $("#warehouseGroup").data("kendoDropDownList");

		if( $warehouseGroup ) {
			$warehouseGroup.bind("change", function (e) {
				const warehouseGroupCode = this.value();

				fnAjax({
					url     : "/admin/comn/getDropDownWarehouseGroupByWarehouseList",
					method : "GET",
					params : { "warehouseGroupCode" : warehouseGroupCode },
					success : function( data ){
						let warehouseId = $("#warehouseId").data("kendoDropDownList");
						warehouseId.setDataSource(data.rows);
					},
					error : function(xhr, status, strError){
						fnKendoMessage({ message : xhr.responseText });
					},
					isAction : "select"
				});
			});
		}

		// 출고처그룹 별 출고처
		const WAREHOSE_ID = "warehouseId";
		searchCommonUtil.getDropDownUrl(WAREHOSE_ID, WAREHOSE_ID, "/admin/comn/getDropDownWarehouseGroupByWarehouseList", "warehouseName", "warehouseId", "출고처 선택", "", "");

		$("input[name=sellersGroup]").each(function() {
			$(this).bind("change", onPurchaseTargetType);
		});

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

		fbCheckboxChange();
	};


	//판매처 상세
//	function onPurchaseTargetType(){
//		//초기화
//		$("#sellersDetail").html("");
//
//		//$(this).val() 값에 따라 결과값 변경 필요
//		//임시
//		var sellersDetail =  $(this).val();
//		var codeId = "";
//		if(sellersDetail != "") {
//			if(sellersDetail == "SELLERS_GROUP.DIRECT_BUY") {
//				codeId = "AGENT_TYPE";
//			} else {
//				codeId = "DISCOUNT_TYPE";
//			}
//
//			const sellerOption = {
//				optId: "sellersDetail",
//				grpCd: codeId,
//        useYn: "Y",
//			}
//
//			//판매처 상세
//			orderSearchUtil.getCheckBoxCommCd(sellerOption);
//		}
//	}

	//---------------Initialize Option Box End ------------------------------------------------
	//-------------------------------  Common Function start ----------------------------------

    // viewModel 초기화
    function fnViewModelInit(){};

    // 기본값 설정
    function fnDefaultSet(){};

	//-------------------------------  Common Function end -------------------------------

	//------------------------------- Html 버튼 바인딩  Start -----------------------------
	/** Common Search*/
	$scope.fnSearch = function( ) {	orderSubmitUtil.search();	};

	/** Common Clear*/
	$scope.fnClear =function(){	 orderSubmitUtil.clear();	};

	$scope.fnExcelDownload =function(btnObj){
		$("input[name='psExcelTemplateId']").val($("#psExcelTemplateId").val());
		orderSubmitUtil.excelExportDown('/admin/order/getDeliveryIngOrderDetailExcelList', btnObj);
	};

	$scope.fnChangeStatus = function(obj) {
		orderSubmitUtil.changeStatus('/admin/order/status/putOrderDetailStatus', $(obj).data('msg'), $(obj).data('stat'));
	};

	$scope.fnTotalCountView =function(){
		orderSubmitUtil.totalCountView();
	};

	//------------------------------- Html 버튼 바인딩  End -------------------------------

	//------------------------------- Validation Start -----------------------------------

	//fnInputValidationForAlphabetNumberLineBreakComma("codeSearch");

	//------------------------------- Validation End -------------------------------------
}); // document ready - END
