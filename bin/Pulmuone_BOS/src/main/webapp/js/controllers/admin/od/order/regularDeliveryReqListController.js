/**-----------------------------------------------------------------------------
 * description 		 : 정기배송주문 신청 리스트
 * @
 * @ 수정일			수정자          수정내용
 * @ ------------------------------------------------------
 * @ 2021.02.05		김명진          최초생성
 * @
 * **/
'use strict';

var PAGE_SIZE = 20;
var viewModel, regularGridDs, regularGridOpt, regularGrid;

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

if(newURL == 'regularDeliveryReqList'){
	var defaultActivateTab = '';
	csParamData = undefined;
	$('#urUserId').val('');
}

$(document).ready(function() {
	importScript("/js/service/admin/od/order/regularDeliveryReqListGridColumns.js",function (){
		importScript("/js/service/od/order/orderCommSearch.js", function (){
				fnInitialize();	//Initialize Page Call ---------------------------------
		});
	});

	//Initialize PageR
	function fnInitialize(){
		$scope.$emit('fnIsMenu', { flag : 'true' });

		fnPageInfo({
			PG_ID  : 'regularDeliveryReqList',
			callback : fnUI
		});

	}

	// 화면 UI 초기화
	function fnUI(){

		fnTranslate();	// 다국어 변환
		fnInitButton();
		fnInitGrid();
		fnInitOptionBox();
		fnViewModelInit();
		fnDefaultSet();

		// fnSearch();
		//orderSubmitUtil.search();
	};

	//--------------------------------- Button Start---------------------------------

	function fnInitButton(){
		$('#fnSearch, #fnClear').kendoButton();
	}
	//--------------------------------- Button End-----------------------------------

	//------------------------------- Grid Start -------------------------------
	// 그리드 초기화
	function fnInitGrid(){
		regularGridDs = fnGetPagingDataSource({
			url      : '/admin/order/getOrderRegularReqList'
			,pageSize : PAGE_SIZE
		});
		regularGridOpt = {
			dataSource: regularGridDs
			,pageable  : {
				pageSizes: [20, 30, 50],
				buttonCount : 10
			}
			,navigatable : true
			,height : 755
			,scrollable : true
			,columns   : regularDeliveryReqGridUtil.regularDeliveryReqList()
		};

		regularGrid = $('#regularGrid').initializeKendoGrid( regularGridOpt ).cKendoGrid();

		regularGrid.bind("dataBound", function(){
			let rowNum = regularGridDs._total - ((regularGridDs._page - 1) * regularGridDs._pageSize);
			$("#regularGrid tbody > tr .row-number").each(function(index){
				$(this).html(rowNum);
				rowNum--;
			});
			$("#countTotalSpan").text(regularGridDs._total);
		});

		$("#regularGrid").on("click", "tbody > tr > td", function(e) {

			let fieldId = e.currentTarget.dataset.field;	//컬럼 정보
			let dataItems = regularGrid.dataItem(regularGrid.select()); //선택데이터 정보

			if (fieldId == "reqId") {

		 		let odRegularReqId = dataItems.odRegularReqId
		 		let option = {};
		 		option.data = { odRegularReqId : odRegularReqId };
				option.url = "#/regularDeliveryReqDetail";

				var inputMenuId = $("#lnbMenuList li").find("a[data-menu_depth='3'].active").attr("id");

				//페이지 창 open
	            window.open("#/regularDeliveryReqDetail?inputMenuId="+inputMenuId+"&odRegularReqId=" + odRegularReqId, "_blank");
		 		//fnGoPage(option);
			}
			else if (fieldId === "buyerNm") {
				if (fnIsProgramAuth("USER_POPUP_VIEW") == true) {
					let urUserId = dataItems.urUserId
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
        });
	}
	//------------------------------- Grid End -------------------------------

	function fnSearch(){

		var selectConditionType = $('input[name=selectConditionType]:checked').val();
		if(selectConditionType == "singleSection" && $.trim($("#codeSearch").val()) == ""){
            fnKendoMessage({ message : "정기배송 신청 번호를 입력해주세요." });
            return false;
        }

		var data = $('#searchForm').formSerialize(true);

        if(selectConditionType.length > 0) {
        	data['selectConditionType'] = selectConditionType;
        }

		var query = {
					page         : 1,
					pageSize     : PAGE_SIZE,
					filterLength : fnSearchData(data).length,
					filter :  {
						filters : fnSearchData(data)
					}
		};
		regularGridDs.query( query );
	}

	function fnClear(){
		$('#searchForm').formClear(true);
		$('#searchForm input[type=checkbox]').prop("checked", true);
        $('[data-id="fnDateBtnC"]').mousedown();
	}
	//--------------------------------- Button End---------------------------------

	//---------------Initialize Option Box Start ------------------------------------------------
	// 옵션 초기화
	function fnInitOptionBox(){

		var searchData;

		if(csParamData != undefined){
            if(csParamData.urUserId != undefined && csParamData.urUserId != ''){
                searchData = [
                            { "CODE" : "REGULAR_SEQ", "NAME" : "신청번호" },
                            { "CODE" : "GOODS_NAME", "NAME" : "상품명" },
                            { "CODE" : "IL_GOODS_ID", "NAME" : "상품코드" },
                            { "CODE" : "IL_ITEM_CD", "NAME" : "품목코드" },
                            { "CODE" : "ITEM_BARCODE", "NAME" : "품목바코드" }
                        ];
            }else{
                   searchData = [
                             { "CODE" : "REGULAR_SEQ", "NAME" : "신청번호" },
                             { "CODE" : "GOODS_NAME", "NAME" : "상품명" },
                             { "CODE" : "IL_GOODS_ID", "NAME" : "상품코드" },
                             { "CODE" : "IL_ITEM_CD", "NAME" : "품목코드" },
                             { "CODE" : "ITEM_BARCODE", "NAME" : "품목바코드" }
                         ];
                 }
        }else{
          searchData = [
                    { "CODE" : "BUYER_NAME", "NAME" : "주문자명" },
                    { "CODE" : "LOGIN_ID", "NAME" : "주문자ID" },
                    { "CODE" : "REGULAR_SEQ", "NAME" : "신청번호" },
                    { "CODE" : "GOODS_NAME", "NAME" : "상품명" },
                    { "CODE" : "IL_GOODS_ID", "NAME" : "상품코드" },
                    { "CODE" : "IL_ITEM_CD", "NAME" : "품목코드" },
                    { "CODE" : "ITEM_BARCODE", "NAME" : "품목바코드" }
                ];
        }


		var reqRoundTypeData = [
		    { "CODE" : "UP", "NAME" : "이상" },
		    { "CODE" : "DOWN", "NAME" : "이하" }
		];
		mutilSearchCommonUtil.default();
		orderSearchUtil.searchTypeKeyword(searchData, null); //dropDown 공통
		orderSearchUtil.dateSearch(null,-7); //기간검색
		orderSearchUtil.getDropDownComm(reqRoundTypeData, 'reqRoundType');

		// 신청상세상태
		orderSearchUtil.getCheckBoxCommCd(orderOptionUtil.regularStatusDetl);

		// 신청상태
		orderSearchUtil.getCheckBoxCommCd(orderOptionUtil.regularStatus);

		// 신청기간
		orderSearchUtil.getCheckBoxCommCd(orderOptionUtil.regularTermType);

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

		//유형
		orderSearchUtil.getCheckBoxCommCd(orderOptionUtil.regularAgentType);

		fbCheckboxChange();


		fnValidateNum('reqRound');
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
	$scope.fnSearch = function( ) {	fnSearch();	};

	/** Common Clear*/
	$scope.fnClear =function(){	 fnClear();	};

	$scope.fnExcelDownload =function(btnObj){
		var data = $('#searchForm').formSerialize(true);

        if($('input[name=selectConditionType]:checked').length > 0) {
        	data['selectConditionType'] = $('input[name=selectConditionType]:checked').val();
        }

        fnExcelDownload('/admin/order/getRegularReqExcelList', data, btnObj);
	};

	//------------------------------- Html 버튼 바인딩  End -------------------------------

	//------------------------------- Validation Start -----------------------------------

	//fnInputValidationForAlphabetNumberLineBreakComma("codeSearch");

	//------------------------------- Validation End -------------------------------------

}); // document ready - END
