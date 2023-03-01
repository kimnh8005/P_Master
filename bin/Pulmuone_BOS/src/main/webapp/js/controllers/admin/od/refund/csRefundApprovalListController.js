/**----------------------------------------------------------------------------
 * description 		 : 주문/배송관리 > 환불 관리 > CS 환불 승인리스트
 * @
 * @ 수정일			수정자        수정내용
 * @ --------------------------------------------------------------------------
 * @ 2021.03.10     이규한		최초 생성
 * **/

"use strict";

var PAGE_SIZE = 20;
// CS환불 승인 주문상세 그리드
var orderGridDs, orderGridOpt, orderGrid;
// 머지할 필드 목록
var orderMergeFieldArr = ['odClaimId', 'refundType', 'refundPrice', 'csRefundTp', 'csRefundApproveNm', 'management'];
// 그룹할 필드 목록
var orderMergeGroupFieldArr = ['odClaimId'];
var LClaimCtgryDropDownList, MClaimCtgryDropDownList, SClaimCtgryDropDownList; //BOS클레임사유 드롭다운리스트


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

if(newURL == 'csRefundApprovalOrderDetailList'){
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
					importScript("/js/controllers/admin/ps/claim/searchCtgryComm.js", function() {
						fnInitialize();
					});
				});
			});
		});
	});

	// Initialize PageR
	function fnInitialize() {
		$scope.$emit('fnIsMenu', { flag : true });
		fnPageInfo({
			PG_ID  		: 'csRefundApprovalOrderDetailList',
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

	// CS환불 승인 주문상세 리스트 조회
	function fnSearch() {

		var form = $("#searchForm");

		// form.formClear(false);
		var data = form.formSerialize(true);
		orderSubmitUtil.search();
	}

	// 초기화
	function fnClear(gridClearYn) {
		// 공통 초기화 호출
		orderSubmitUtil.clear();
		if (gridClearYn) $('#orderGrid').gridClear(true);
	}

	// 엑셀 다운로드
	function fnExcelDownload(btnObj) {
		// 주문리스트는 사유 필수 세팅
		$("#privacyIncludeYn_0").remove();
		$("body").append("<input type='text' id='privacyIncludeYn_0' value='Y'/>")
		orderSubmitUtil.excelExportDown('/admin/order/getCsRefundApprovalExcelList', btnObj);
	}
	//--------------------------------- Button End---------------------------------------------


	//------------------------------- Grid Start ----------------------------------------------
	// 그리드 초기화
	function fnInitGrid() {
		// CS환불 승인 정보 그리드 DataSource
		orderGridDs = fnGetPagingDataSource({
			url      	: "/admin/order/getCsRefundApprovalList",
			pageSize 	: PAGE_SIZE,
			requestEnd	: function(e) {
				if(e.response.code == "0000") {
					let data = e.response.data;
					if(data.totalPrice > 0) {
						$('#totalRefundPrice').text( kendo.toString(data.totalPrice, "n0") );
					}
					else {
						$('#totalRefundPrice').text(0);
					}
				}
			}
		});

		// CS환불 승인 정보 그리드 기본옵션
		orderGridOpt = {
			dataSource	: orderGridDs,
			pageable  	: { pageSizes: [20, 30, 50], buttonCount : 10 },
			navigatable	: true,
			scrollable	: true,
			columns 	: orderDetailGridUtil.csRefundApprovalOrderList()
		};
		orderGrid = $('#orderGrid').initializeKendoGrid(orderGridOpt).cKendoGrid();

		//orderGridEventUtil.noView(function () {fnMergeGridRows('orderGrid', orderMergeFieldArr, orderMergeGroupFieldArr);});	// 그리드 전체 건수 설정
		orderGrid.bind("dataBound", function(e) {
			$('#countTotalSpan').text( kendo.toString(orderGridDs._total, "n0") );

			let rowNum = orderGridDs._total - ((orderGridDs._page - 1) * orderGridDs._pageSize);
			$("#orderGrid tbody > tr .row-number").each(function(index){
				$(this).html(rowNum);
				rowNum--;
			});

			mergeGridRows('orderGrid',['apprReqDt', 'apprChgDt', 'omSellersNm', 'odid', 'csRefundTpNm', 'bosClaimLargeNm', 'bosClaimMiddleNm', 'bosClaimSmallNm', 'csReasonMsg', 'csRefundApproveCdNm', 'management']
											,['odCsId'] );
		});
		// orderGridEventUtil.click();					// 그리드 클릭 설정

		// CS환불 승인 정보 그리드 클릭
		$("#orderGrid").on("click", "tbody > tr > td", function(e) {
			let field 		= e.currentTarget.dataset.field;
			let dataItems 	= orderGrid.dataItem(orderGrid.select()); // 선택데이터 정보

			// 클레임번호
			if ( field === "odid") {
				window.open("#/orderMgm?odid=" + dataItems.odid ,"csPopForm_" + dataItems.odid);
			}
		});

		// 저장
		$('#orderGrid').on("click", "button[kind=apprStatSave]", function(e) {
			e.preventDefault();
			let dataItem = orderGrid.dataItem($(e.currentTarget).closest('tr'));
			let paramData = new Object();
			paramData.odCsId = dataItem.odCsId;
			paramData.csRefundApproveCd = 'CS_REFUND_APPR_CD.SAVE';

			putOrderClaimCsRefundApproveCd(paramData);
		});

		// 승인요청
		$('#orderGrid').on("click", "button[kind=apprStatRequest]", function(e) {
			e.preventDefault();
			let dataItem = orderGrid.dataItem($(e.currentTarget).closest('tr'));
			let paramData = new Object();
			paramData.odCsId = dataItem.odCsId;
			paramData.csRefundApproveCd = 'CS_REFUND_APPR_CD.REQUEST';

			putOrderClaimCsRefundApproveCd(paramData);
		});

		// 요청취소
		$('#orderGrid').on("click", "button[kind=apprStatCancel]", function(e) {
			e.preventDefault();
			let dataItem = orderGrid.dataItem($(e.currentTarget).closest('tr'));
			let paramData = new Object();
			paramData.odCsId = dataItem.odCsId;
			paramData.csRefundApproveCd = 'CS_REFUND_APPR_CD.CANCEL';

			putOrderClaimCsRefundApproveCd(paramData);
		});

		// 삭제
		$('#orderGrid').on("click", "button[kind=apprStatDisposal]", function(e) {
			e.preventDefault();
			let dataItem = orderGrid.dataItem($(e.currentTarget).closest('tr'));
			let paramData = new Object();
			paramData.odCsId = dataItem.odCsId;
			paramData.odCsYn = 'N';

			putOrderClaimCsRefundApproveCd(paramData);
		});

		// 환불계좌 확인
		$('#orderGrid').on("click", "button[kind=refundAccountInfo]", function(e) {
			e.preventDefault();
			let dataItem = orderGrid.dataItem($(e.currentTarget).closest('tr'));

			let paramObj = new Object();
			paramObj.bankNm			= dataItem.bankNm;			// 은행명
			paramObj.accountHolder	= dataItem.accountHolder;	// 예금주명
			paramObj.accountNumber 	= dataItem.accountNumber;	// 계좌번호
			orderPopupUtil.open("csRefundAccountInfoPopup", paramObj);
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
        }

		// 기간검색 데이터
		var dateSearchData = [
            { "CODE" : "CLAIM_REQUEST_DATE"	, "NAME" : "클레임요청일자" },
            { "CODE" : "CREATE_DATE"		, "NAME" : "주문일자" },
            { "CODE" : "PAY_DATE"			, "NAME" : "결제일자" }
        ];

		// 단일조건,복수조건 radio
		mutilSearchCommonUtil.default();
		// 기간검색 DropDown
		orderSearchUtil.dateSearch(dateSearchData);

		// 판매처그룹
		searchCommonUtil.getDropDownCommCd("sellersGroup", "NAME", "CODE", "판매처 그룹 전체", "SELLERS_GROUP", "Y", "", "", "");

		$("input[name=sellersGroup]").each(function() {
			$(this).bind("change", onPurchaseTargetType);
		});

		// 공급업체
		const SUPPLIER_ID = "supplierId";
		searchCommonUtil.getDropDownUrl(SUPPLIER_ID, SUPPLIER_ID, "/admin/comn/getDropDownSupplierList", "supplierName", "supplierId", "공급업체 전체");

		// 출고처그룹
		setWarehouseGroup();

		// BOS클레임 사유
		setBosClaimReason();

		// 승인상태(저장, 승인요청) CheckBox
		orderSearchUtil.getCheckBoxCommCd(orderOptionUtil.csRefundApproveCd);
		// CS환불구분(결제금액 환불, 적립금 환불) CheckBox
	    orderSearchUtil.getCheckBoxLocalData(orderOptionUtil.csRefundTp);
		// 검색어(복수, 단일) DropDown
		orderSearchUtil.searchTypeKeyword(searchData,searchDataSingle);
		// 체크박스 공통함수 Call
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

	// 출고처 Set
	function setWarehouseGroup() {
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
	}

	// BOS클레임사유 Set
	function setBosClaimReason() {
		LClaimCtgryDropDownList = searchCtgryCommonUtil.LClaimCtgryDropDownList();
		MClaimCtgryDropDownList = searchCtgryCommonUtil.MClaimCtgryDropDownList();
		SClaimCtgryDropDownList = searchCtgryCommonUtil.SClaimCtgryDropDownList();

		MClaimCtgryDropDownList.enable(false);
		SClaimCtgryDropDownList.enable(false);

		LClaimCtgryDropDownList.bind('change', function(e) {
			if(0 === this.selectedIndex) {
				MClaimCtgryDropDownList.enable(false);
				SClaimCtgryDropDownList.enable(false);
			}
			else {
				MClaimCtgryDropDownList.enable(true);
			}

			$("#targetType").val("");
		});

		MClaimCtgryDropDownList.bind('change', function(e) {
			if(0 === this.selectedIndex) SClaimCtgryDropDownList.enable(false);
			else SClaimCtgryDropDownList.enable(true);

			$("#targetType").val("");
		});

		SClaimCtgryDropDownList.bind('change', function(e){
			var psClaimCtgryId = this.value();
			var claimName = this.text();

			for(var i = 0; i < sClaimCtgryDropDownListData.length; i++){
				if(sClaimCtgryDropDownListData[i].psClaimCtgryId == psClaimCtgryId){
					$("#targetType").val(sClaimCtgryDropDownListData[i].targetTypeName);
					$("#psClaimBosId").val(sClaimCtgryDropDownListData[i].psClaimBosId);
				}
			}
		});
	}
	//---------------Initialize Option Box End ------------------------------------------------


	//-------------------------------  Common Function start ----------------------------------
	function putOrderClaimCsRefundApproveCd(paramData){
		fnAjax({
			url     : "/admin/order/claim/putOrderClaimCsRefundApproveCd",
			params  : paramData,
			success :
				function( data ){
					fnKendoMessage({message : fnGetLangData({nullMsg :'수정되었습니다.' }),ok:function(e){
							fnSearch();
						}});
				},
			isAction : 'batch'
		});
	}
	//-------------------------------  Common Function end ------------------------------------


	//------------------------------- Html 버튼 바인딩  Start --------------------------------------
	/** Common Search */
	$scope.fnSearch			= function() { fnSearch(); };
	/** Common Clear */
	$scope.fnClear			= function(gridClearYn) { fnClear(gridClearYn); };
	/** Common ExcelDownload*/
	$scope.fnExcelDownload 	= function(btnObj) { fnExcelDownload(btnObj); };

	$scope.fnTotalCountView = function() { orderSubmitUtil.totalCountView(); };
	//------------------------------- Html 버튼 바인딩  End ----------------------------------------


	//------------------------------- Validation Start ----------------------------------------
	// 단일조건 - 입력값을 영문대소문자 + 숫자 + 엔터 + , 로 제한
	//fnInputValidationForAlphabetNumberLineBreakComma("codeSearch");
	// 복수조건(검색어) - 한글 & 영어(대,소) & 숫자 & 특수문자 []~!@#$%^&*()_+|<>?:{}
	fnInputValidationByRegexp("findKeyword", /[^ㄱ-ㅎㅏ-ㅣ가-힣a-zA-Z0-9\{\}\[\]\/?.,;:|\)*~`!^\-_+ <>@\#$%&\'\"\\\(\=]/g);
	//------------------------------- Validation End ------------------------------------------
}); // document ready - END
