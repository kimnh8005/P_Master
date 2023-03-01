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
			PG_ID  : "deliveryReadyOrderDetailList",
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
		getShippingCompList()
		fnInitGrid();
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
		orderGridDs = fnGetEditPagingDataSource({
//			url      : "/admin/order/getOrderDetailList",
			url      : "/admin/order/getDeliveryReadyOrderDetailList",
			pageSize : PAGE_SIZE,
			filterLength: fnSearchData(data).length,
			filter: {
				filters: fnSearchData(data)
			},
			model_id: 'odOrderDetlId',
			model_fields: {
				chk: {editable: false},
				no: {editable: false},
				shippingDt: {editable: false},
				odid: {editable: false},
				odOrderDetlSeq: {editable: false},
				omSellersNm: {editable: false},
				goodsTp: {editable: false},
				buyerNm: {editable: false},
				recvNm: {editable: false},
				goodsNm: {editable: false},
				orderCnt: {editable: false},
				orderStatus: {editable: false},
				collectionMallId: {editable: false},
				payTp: {editable: false},
				agentType: {editable: false},
				odOrderId: {editable: false},
				shippingCompNm: {
					editable: true, type: 'string', validation: {
						required: true,
						stringValidation(input) {

							// input이 .k-edit-cell 또는 #psGrpDropDown input가 되는 두가지의 경우가 있습니다.
							if (input.is("#shippingCompNm") && input.val() == "") {
								const $editCell = input.closest("td.k-edit-cell");

								if ($editCell.length > 0) {
									// tooltip box가 dropdownlist span 영역 안에 들어가서 보이지 않음
									// td의 overflow 속성을 visible로 바꾸니 해결되었습니다.
									// 따라서 동적으로 overflow : visible로 바꿔주고 변경 성공시 hidden으로 만들었습니다.
									$editCell.css("overflow", "visible");
								}
								input.attr("data-stringValidation-msg", '이 영역을 선택해주세요');
								return false;
							}
							input.closest('.k-edit-cell').css("overflow", "hidden");
							return true;
						}
					}
				},
				trackingNo: {editable: true, type: 'string', validation: {required: true, maxLength: "30"}}
			}
		});

		orderGridOpt = {
			dataSource: orderGridDs,
			pageable  : { pageSizes: [20, 30, 50], buttonCount : 10 },
			navigatable : true,
			height : 755,
			scrollable : true,
			editable: true,
			columns : orderDetailGridUtil.deliveryReadyList()
		};

		orderGrid = $('#orderGrid').initializeKendoGrid( orderGridOpt ).cKendoGrid();

		orderGridEventUtil.noView();
		orderGridEventUtil.click();
		orderGridEventUtil.ckeckBoxAllClick();
		orderGridEventUtil.checkBoxClick();

		orderGrid.bind("dataBound", function() {
			$('.shippingCompList').on('change',function(e){
				var trNum = $(this).closest('tr').prevAll().length;
				var flag = true;
				var trackingNo = $.trim($("input[name='trackingNo']:eq("+trNum+")").val());

				if ($(this).val() == "" && trackingNo == ""){
					flag = false;
				}
				$("input[name='rowCheckbox']:eq("+trNum+")").prop("checked", flag);
			});

			$('input[name="trackingNo"]').on('blur',function(e){
				var trNum = $(this).closest('tr').prevAll().length;
				var flag = true;
				var shippingComp = $.trim($(".shippingCompList:eq("+trNum+")").val());
				
				if ($(this).val() == "" && shippingComp == ""){
					flag = false;
				}
				$("input[name='rowCheckbox']:eq("+trNum+")").prop("checked", flag);
			});
		});

	};

	//-------------------------------  Grid End  -------------------------------

	//---------------Initialize Option Box Start ------------------------------------------------

	// 옵션 초기화
	function fnInitOptionBox(){

		var searchData = orderSearchUtil.searchDataMutilOhter();
		var searchDataSingle = orderSearchUtil.searchDataSingle();
		var dateSearchData = [
			//{ "CODE" : "DELIVERYREADY_DATE", "NAME" : "배송준비중일자" },
			//{ "CODE" : "CREATE_DATE", "NAME" : "주문일자" },
			{ "CODE" : "ORDER_IF_DATE", "NAME" : "주문 I/F" },
			//{ "CODE" : "SHIPPING_DATE", "NAME" : "출고예정일" },
			//{ "CODE" : "DELIVERY_DATE", "NAME" : "도착예정일" },
			//{ "CODE" : "DELIVERY_ING_DATE", "NAME" : "배송중" },
			//{ "CODE" : "BUY_FINALIZED_DATE", "NAME" : "구매확정" },
			//{ "CODE" : "CANCEL_COMPLETE_DATE", "NAME" : "취소완료" },
			//{ "CODE" : "RETURN_COMPLETE_DATE", "NAME" : "반품완료" },
		];

		mutilSearchCommonUtil.default();//단일조건,복수조건 radio
		orderSearchUtil.searchTypeKeyword(searchData,searchDataSingle); //dropDown 공통
		orderSearchUtil.dateSearch(dateSearchData, -15); //기간검색

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

		// 송장없음
		orderSearchUtil.getCheckBoxLocalData(orderOptionUtil.trackingNoYn);

		$("input[name='trackingNoYn']").prop("checked", true);


		// 송장없음 체크박스 강제 체크 로직
		$("input[name='trackingNoYn']").on("click", function(){
			$("input[name='trackingNoYn']").prop("checked", true);
		});

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



	//판매처 상세
	/*
	function onPurchaseTargetType(){
		//초기화
		$("#sellersDetail").html("");

		//$(this).val() 값에 따라 결과값 변경 필요
		//임시
		var sellersDetail =  $(this).val();
		var codeId = "";
		if(sellersDetail != "") {
			if(sellersDetail == "SELLERS_GROUP.DIRECT_BUY") {
				codeId = "AGENT_TYPE";
			} else {
				codeId = "DISCOUNT_TYPE";
			}

			const sellerOption = {
				optId: "sellersDetail",
				grpCd: codeId,
				useYn: "Y",
			}

			//판매처 상세
			orderSearchUtil.getCheckBoxCommCd(sellerOption);
		}
	}
	*/

	//---------------Initialize Option Box End ------------------------------------------------
	//-------------------------------  Common Function start ----------------------------------

	// viewModel 초기화
	function fnViewModelInit(){};

	// 기본값 설정
	function fnDefaultSet(){};

	//-------------------------------  Common Function end -------------------------------

	//------------------------------- Html 버튼 바인딩  Start -----------------------------
	/** Common Search*/
	$scope.fnSearch = function( ) {	$("#orderCntYn").val("Y"); orderSubmitUtil.search();	};

	/** Common Clear*/
	$scope.fnClear =function(){	 orderSubmitUtil.clear(-15);	};

	$scope.fnExcelDownload =function(btnObj){
		$("input[name='psExcelTemplateId']").val($("#psExcelTemplateId").val());
		orderSubmitUtil.excelExportDown('/admin/order/getDeliveryReadyOrderDetailExcelList', btnObj);
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
	// 입력가능 : 한글 & 영어(대,소) & 숫자 & 특수문자  []~!@#$%^&*()_+|<>?:{}
	fnInputValidationByRegexp("findKeyword", /[^ㄱ-ㅎㅏ-ㅣ가-힣a-zA-Z0-9\{\}\[\]\/?.,;:|\)*~`!^\-_+ <>@\#$%&\'\"\\\(\=]/g);
	//------------------------------- Validation End -------------------------------------
}); // document ready - END
