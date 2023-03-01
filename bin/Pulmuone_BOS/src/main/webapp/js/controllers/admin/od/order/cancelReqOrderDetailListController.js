/**-----------------------------------------------------------------------------
 * description 		 : 주문/배송관리 > 주문관리 > 취소요청 주문상세 리스트
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

if(newURL == 'cancelReqOrderDetailList'){
	var defaultActivateTab = '';
	csParamData = undefined;
	$('#urUserId').val('');
}

// 판매처그룹.직관리 (외부몰)
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
			PG_ID  : "cancelReqOrderDetailList",
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
			url      : "/admin/order/getCancelReqOrderDetailList",
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
			columns : orderDetailGridUtil.cancelReqList()
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

		if(csParamData != undefined){
            if(csParamData.urUserId != undefined && csParamData.urUserId != ''){
                searchData = orderSearchUtil.csSearchDataMutil();
            }else{
                searchData = orderSearchUtil.searchDataMutil();
            }
        }
		var dateSearchData = [
			{ "CODE" : "CANCELREQ_DATE", "NAME" : "취소요청 일자" },
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
		orderSearchUtil.dateSearch(dateSearchData,-7); //기간검색

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

		// 체크박스 클릭 시
		$('#ng-view').on("click", "input[name=rowCheckbox]", function() {
			var isChecked = false;
			if($(this).is(":checked")) {
				isChecked = true;
			}
			var selectDataItem = orderGrid.dataItem($(this).closest('tr'));
			let odClaimId = selectDataItem.odClaimId;
			let gridDatas = $('#orderGrid').data("kendoGrid").dataSource.data();

			for(var i=0; i<gridDatas.length; i++) {
				let gridData = gridDatas[i];
				if(gridData.odClaimId == odClaimId) {
					$("input[name=rowCheckbox]:eq(" + i + ")").prop("checked", isChecked);
				}
			}
		});

	};


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
		orderSubmitUtil.excelExportDown('/admin/order/getCancelReqOrderDetailExcelList', btnObj);
	};

	$scope.fnChangeStatus = function(obj) {
		//orderSubmitUtil.changeStatus('/admin/order/status/putClaimDetailStatus', $(obj).data('msg'), $(obj).data('stat'), 'odClaimDetlId');
		let selectRows  = $("#orderGrid").find("input[name=rowCheckbox]:checked").closest("tr");

		if( selectRows.length == 0 ){
			fnKendoMessage({ message : "상태 변경 할 주문 상세 내역을 선택해주세요." });
			return;
		}

		fnKendoMessage({
			type    : "confirm",
			message : $(obj).data('msg') + " 일괄 변경하시겠습니까?",
			ok      : function(){

				let submitFlag = true;
				let params = {};
				params.odClaimInfoList = [];

				for(let i = 0, selectCount = selectRows.length; i < selectCount; i++){
					let dataItem = orderGrid.dataItem($(selectRows[i]));
					params.odClaimInfoList[i] = dataItem;
				}

				fnAjax({
					url     : '/admin/order/status/putClaimDetailStatus',
					params  : params,
					contentType: "application/json",
					success : function( data ){

						var msg = "[총 건수] : " + data.totalCount + "<BR>" +
							" [성공 건수] : " + data.successCount + "<BR>" +
							" [실패 건수] : " + data.failCount + "<BR><BR>" ;

						fnKendoMessage({ message : msg + "저장이 완료되었습니다.", ok : orderSubmitUtil.search() });
					},
					error : function(xhr, status, strError){
						fnKendoMessage({ message : xhr.responseText });
					},
					isAction : "update"
				});
			},
			cancel  : function(){

			}
		});
	};

	$scope.fnTotalCountView =function(){
		orderSubmitUtil.totalCountView();
	};

	//------------------------------- Html 버튼 바인딩  End -------------------------------

	//------------------------------- Validation Start -----------------------------------

	//fnInputValidationForAlphabetNumberLineBreakComma("codeSearch");
	// 입력가능 : 한글 & 영어(대,소) & 숫자 & 특수문자  []~!@#$%^&*()_+|<>?:{}
	fnInputValidationByRegexp("findKeyword", /[^ㄱ-ㅎㅏ-ㅣ가-힣a-zA-Z0-9\{\}\[\]\/?.,;:|\)*~`!^\-_+ <>@\#$%&\'\"\\\(\=]/g);
	//------------------------------- Validation End -------------------------------------
}); // document ready - END
