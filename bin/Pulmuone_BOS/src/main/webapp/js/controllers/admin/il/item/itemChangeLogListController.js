/**-----------------------------------------------------------------------------
 * description 		 : 상품 업데이트 내역
 * @
 * @ 수정일			수정자		수정내용
 * @ ------------------------------------------------------
 * @ 2021.04.21		임상건		최초생성
 * @
 * **/
"use strict";


var PAGE_SIZE = 20;
var viewModel, itemChangeLogGridDs, itemChangeLogGridOpt, itemChangeLogGrid;
var pageParam = fnGetPageParam();													//GET Parameter 받기
var paramGoodsId = pageParam.paramIlGoodsId;
var paramDataSort = pageParam.paramDataSort;
var paramIlItemCode = pageParam.paramIlItemCode;

var initSearch = true;

$(document).ready(function() {

	fnInitialize();	//Initialize Page Call ---------------------------------

	//Initialize PageR
	function fnInitialize(){
		$scope.$emit("fnIsMenu", { flag : true });

		fnPageInfo({
			PG_ID  : "goodsChangeLogList",
			callback : fnUI
		});
	};

	// 화면 UI 초기화
	function fnUI(){

		fnInitButton();
		fnInitGrid();
		fnInitOptionBox();
		fnViewModelInit();
		fnDefaultSet();

		//탭 변경 이벤트
		fbTabChange();
//		viewModel.fnGridDataSort();
		if(paramGoodsId){ //상품 저장에서 넘어온 상태라면
			viewModel.searchInfo.set("searchCondition", "GOODS_CODE");
			if(paramGoodsId != undefined) {
				viewModel.searchInfo.set("findKeyword", paramGoodsId);
			}
			if(paramDataSort != undefined) {
				viewModel.searchInfo.set("gridDataSort", paramDataSort);
			}
			viewModel.searchInfo.saleStatus.push("SALE_STATUS.SAVE");
			var renewURL = window.location.href.slice(0, window.location.href.indexOf('?'));		//URL 파라미터 초기화(삭제)
			history.pushState(null, null, renewURL);

			$("#fnSearch").trigger("click");

			paramGoodsId = null; //상품 저장에서 넘어온 처음만 검색되도록 paramGoodsId를 초기화 한다.
		}
		if(paramIlItemCode){ //마스터 상품 저장에서 넘어온 상태라면
			viewModel.searchInfo.set("searchCondition", "ALL");
			if(paramIlItemCode != undefined) {
				viewModel.searchInfo.set("ilItemCode", paramIlItemCode);
			}

			var renewURL = window.location.href.slice(0, window.location.href.indexOf('?'));		//URL 파라미터 초기화(삭제)
			history.pushState(null, null, renewURL);

			$("#fnSearch").trigger("click");

			paramIlItemCode = null; //상품 저장에서 넘어온 처음만 검색되도록 paramGoodsId를 초기화 한다.
		}

	};

	//--------------------------------- Button Start---------------------------------

	// 버튼 초기화
	function fnInitButton(){
		$("#fnSearch,  #fnLogClear").kendoButton();
	};

	//--------------------------------- Button End---------------------------------


	//------------------------------- Grid Start -------------------------------
	// 그리드 초기화
	function fnInitGrid(){

		itemChangeLogGridDs = fnGetPagingDataSource({
			url	  : "/admin/item/master/getItemChangeLogList",
			pageSize : PAGE_SIZE
		});

		itemChangeLogGridOpt = {
			dataSource: itemChangeLogGridDs,
			pageable  : { pageSizes: [20, 30, 50, 100], buttonCount : 10 },
			navigatable : true,
			height : 755,
			scrollable : true,
			columns : [ { field : "createDate", title : "업데이트 일자", width : "10%", attributes : {style : "text-align:center;"} }
						, { field : "ilItemCode", title : "마스터 품목코드/<BR>품목바코드", width : "10%", attributes : {style : "text-align:center;  text-decoration: underline;color:blue;"},
						  template : function(dataItem){
							  let itemCodeView = dataItem.ilItemCode;

							  if( dataItem.itemBarcode != "" ){
									itemCodeView += "/<br>" + dataItem.itemBarcode;
							  } else {
									itemCodeView += "/ - ";
							  }

							  return itemCodeView;
						  }
						}
						, { field : "erpLinkIfYn", title : "ERP연동여부", width : "0%", attributes : { style : "text-align:center" }, hidden:true }
						, { field : "itemType", title : "품목유형코드", width : "0%", attributes : { style : "text-align:center" }, hidden:true }
						, { field : "itemTypeName", title : "품목유형", width : "5%", attributes : { style : "text-align:center" } }
						, { field : "itemName", title : "마스터품목명", width : "55%", attributes : { style : "text-align:center; text-decoration: underline;color:blue;" } }
						, { field : "chargeName", title : "수정 담당자", width : "10%", attributes : { style : "text-align:center" },
							  template : function(dataItem){
								  return "관리자<BR/>" + dataItem.chargeName + "{" + dataItem.chargeId + "}";
							  }
						  }
						, { field : "management", title : "관리", width : "10%", attributes : { style : "text-align:center" }
							, template: function(dataItem) {
									return	'<div id="buttonArea" class="textCenter">'
											+ '<button type="button" class="k-button k-button-icontext" kind="btnChangeLog">업데이트 내역</button>'
											+ '</div>';
							}
						  }
			]
		};

		itemChangeLogGrid = $('#itemChangeLogGrid').initializeKendoGrid( itemChangeLogGridOpt ).cKendoGrid();

		itemChangeLogGrid.bind("dataBound", function() {
			$('#countTotalSpan').text( kendo.toString(itemChangeLogGridDs._total, "n0") );
		});

		$('#itemChangeLogGrid').on("click", "a[kind=itemInfoEditBtn]", function(e) {
			e.preventDefault();
			var dataItem = itemChangeLogGrid.dataItem($(e.currentTarget).closest("tr"));

			let params = {};

			params.ilItemWarehouseId	= dataItem.ilItemWarehouseId;

			fnKendoPopup({
				id			: "itemStockInfoEditPopup",
				title		: "선주문 여부",  // 해당되는 Title 명 작성
				width		: "525px",
				height		: "200px",
				scrollable	: "yes",
				src			: "#/itemStockInfoEditPopup",
				param		: params,
				success		: function( id, data ){
					if(data.parameter == undefined){
						$("#fnSearch").trigger("click");
					}
				}
			});
		});

		$('#itemChangeLogGrid').on("click", "a[kind=itemStockEditBtn]", function(e) {
			e.preventDefault();
			var dataItem = itemChangeLogGrid.dataItem($(e.currentTarget).closest("tr"));

			let params = {};

			params.ilItemCd = dataItem.itemCode;
			params.itemNm = dataItem.itemNm;
			params.ilItemWarehouseId = dataItem.ilItemWarehouseId;
			params.unlimitStockYn = dataItem.unlimitStockYn;
			params.notIfStockCnt = dataItem.notIfStockCnt;

			fnKendoPopup({
				id			: "itemNonInterfacedStockUpdatePopup",
				title		: "재고수정",  // 해당되는 Title 명 작성
				width		: "700px",
				height		: "250px",
				scrollable	: "yes",
				src			: "#/itemNonInterfacedStockUpdatePopup",
				param		: params,
				success		: function( id, data ){
					if(data.parameter == undefined){
						$("#fnSearch").trigger("click");
					}
				}
			});
		});

		$("#itemChangeLogGrid").on("click", "tbody>tr>td", function () {
			var index = $(this).index();

			if(index == 1 || index == 5) {
				var aMap = itemChangeLogGrid.dataItem(itemChangeLogGrid.select());
				let erpLinkIfYn = aMap.erpLinkIfYn == "Y" ? true : false;

				// 품목 상세로 이동
				var option = new Object();
				option.url = "#/itemMgmModify";		// 마스터 품목 수정 화면 URL
				option.target = "itemMgmModify";
				option.data = {						// 마스터 품목 수정 화면으로 전달할 Data
						ilItemCode : aMap.ilItemCode,
						isErpItemLink : erpLinkIfYn,
						masterItemType : aMap.itemType
				};
				option.target = '_blank';
				fnGoNewPage(option);
			}
		});
	};

	//-------------------------------  Grid End  -------------------------------

	//---------------Initialize Option Box Start ------------------------------------------------

	// 옵션 초기화
	function fnInitOptionBox(){

		fnTagMkRadio({
			id: 'selectConditionType',
			tagId: 'selectConditionType',
			chkVal: 'singleSection',
			tab: true,
			data: [{
				CODE: "singleSection",
				NAME: "단일조건 검색",
				TAB_CONTENT_NAME: "singleSection"
			}, {
				CODE: "multiSection",
				NAME: "복수조건 검색",
				TAB_CONTENT_NAME: "multiSection"
			}],
			change: function (e) {
			}
		});

		//상품 코드 검색
		fnKendoDropDownList({
			id: "ilItemCodeKind",
			data: [
				{
					CODE: "1",
					NAME: "ERP품목코드(마스터품목코드)",
				},
				{
					CODE: "2",
					NAME: "품목바코드",
				}
			],
			valueField: "CODE",
			textField: "NAME",
		});

		//담당자 검색 조건
		fnKendoDropDownList({
			id: "chargeType",
			data: [
				{ "CODE" : "ALL", "NAME" : "전체" }
				, { "CODE" : "createName", "NAME" : "관리자(이름)" }
				, { "CODE" : "createId", "NAME" : "관리자(아이디)" }
			],
			valueField: "CODE",
			textField: "NAME",
		});

		// 등록(가입)일 시작
		fnKendoDatePicker({
			id: "dateSearchStart",
			format: "yyyy-MM-dd",
			btnStartId: "dateSearchStart",
			btnEndId: "dateSearchEnd",
			defVal : fnGetDayMinus(fnGetToday(),6),
			defType : 'oneWeek',
			change : function(e) {
				fnStartCalChange("dateSearchStart", "dateSearchEnd");
			}
		});

		// 등록(가입)일 종료
		fnKendoDatePicker({
			id: "dateSearchEnd",
			format: "yyyy-MM-dd",
			btnStyle: true,
			btnStartId: "dateSearchStart",
			btnEndId: "dateSearchEnd",
			defVal : fnGetToday(),
			defType : 'oneWeek',
			change : function(e) {
				fnEndCalChange("dateSearchStart", "dateSearchEnd");
			}
		});

		// 검색기간 초기화 버튼 클릭
		/*
		$('[data-id="fnDateBtnC"]').on("click", function(){
			$('[data-id="fnDateBtn3"]').mousedown();
		});
		*/

//		fnInputValidationForAlphabetNumberLineBreakComma("findKeyword"); // 검색어
//		fnInputValidationForHangulAlphabetNumber("itemName"); // 상품명
	};

	//---------------Initialize Option Box End ------------------------------------------------
	//-------------------------------  Common Function start -------------------------------

	// viewModel 초기화
	function fnViewModelInit(){
		viewModel = new kendo.data.ObservableObject({
			searchInfo : {	// 조회조건
				searchCondition : "",	// 검색조건
				findKeyword : "",		// 검색어 (숫자,영문대소문자)
				ilItemCodeKind : "1", 	// 검색조건
				ilItemCode : "", 		// 검색어 (숫자,영문대소문자)
				charge : ""				// 담당자 (아이디, 이름)
			},
			publicStorageUrl : null, // 저장소 URL
			findKeywordDisabled : false, // 검색어 Disabled
			supplierStandardVisible : true, // 공급처기준 Visible
			warehouseStandardVisible : false, // 출고처기준 Visible
			categoryStandardVisible : true, // 표준카테고리 Visible
			categoryVisible : false, // 전시카테고리 Visible
			purchaseTargetTypeVisible : false, // 구매허용범위 Visible
			salesAllowanceVisible : false, // 판매허용범위 Visible
			gridSaleStatus : "", // 그리드 판매상태
			gridSelectionForm : "", // 그리드 선택양식
			chargeDisbled : true,
			fnSearch : function(e){ // 조회
				e.preventDefault();

				if($("input[name=selectConditionType]:checked").val() == "multiSection") {
					viewModel.searchInfo.set("ilItemCode", "");
				}

				let data = $("#searchForm").formSerialize(true);

				var itemNameLengthChk = false;


				if($("input[name=selectConditionType]:checked").val() == "singleSection") {
					if( $.trim($('#ilItemCode').val()).length >= 0 && $.trim($('#ilItemCode').val()).length < 2 ) {

						itemNameLengthChk = true;
						fnKendoMessage({
							message : '단일조건 코드 검색은 2글자 이상 입력해야 합니다.',
							ok : function() {
								return;
							}
						});
					}else {
						data['ilItemCode'] = $.trim($('#ilItemCode').val());
					}
				}else {
					if( data.findKeyword == "" ){
						if( data.dateSearchStart == "" && data.dateSearchEnd != "" ){
							fnKendoMessage({ message : "시작일을 선택해주세요." });
							return;
						}

						if( data.dateSearchStart != "" && data.dateSearchEnd == "" ){
							fnKendoMessage({ message : "종료일을 선택해주세요." });
							return;
						}
					}
				}

				if(!itemNameLengthChk) {
					const _pageSize = itemChangeLogGrid && itemChangeLogGrid.dataSource ? itemChangeLogGrid.dataSource.pageSize() : PAGE_SIZE;


					let searchData = fnSearchData(data);
					let query = { page : 1,
								  pageSize : _pageSize,
								  filterLength : searchData.length,
								  filter : { filters : searchData }
					};

					itemChangeLogGridDs.query(query);
					initSearch = false;
				}
			},
			fnLogClear : function(e){ // 초기화

				e.preventDefault();
				fnDefaultSet();
			},
			fnExcelDownload : function(e){ // 엑셀다운로드
				e.preventDefault();

				var data = $('#searchForm').formSerialize(true);

				//엑셀다운로드 양식을 위한 공통
				if( data["psExcelTemplateId"].length < 1 || data["psExcelTemplateId"] == "[]"){
					fnKendoMessage({ message : "다운로드 양식을 선택해주세요."});
					return false;
				}

				fnExcelDownload('/admin/item/master/itemChangeLogListExportExcel', data);
			},
			fnChargeTypeChange : function() {
				switch(viewModel.searchInfo.chargeType.CODE) {
					case 'ALL' :
						viewModel.set("chargeDisbled", true);
						break;
					case 'createName' :
						viewModel.set("chargeDisbled", false);
						break;
					case 'createId' :
						viewModel.set("chargeDisbled", false);
						break;
				}
			}
		});

		viewModel.publicStorageUrl = fnGetPublicStorageUrl();

		kendo.bind($("#searchForm"), viewModel);
	};

	// 기본값 설정
	function fnDefaultSet(){

		// 데이터 초기화
		viewModel.searchInfo.set("searchCondition", "ALL");
		viewModel.searchInfo.set("findKeyword", "");
		viewModel.searchInfo.set("ilItemCodeKind", "1");
		viewModel.searchInfo.set("ilItemCode", "");

		viewModel.searchInfo.set("itemName", "");
		viewModel.searchInfo.set("charge", "");
		viewModel.searchInfo.set("chargeType", "ALL");


		viewModel.searchInfo.set("storageMethodType", []);
		viewModel.set("gridSaleStatus", "");
		viewModel.set("gridSelectionForm", "");
		$('[data-id="fnDateBtnC"]').mousedown();
	};

	$('#itemChangeLogGrid').on('click', 'button[kind=btnChangeLog]', function(e) {
		e.preventDefault();
		let dataItem = itemChangeLogGrid.dataItem($(e.currentTarget).closest('tr'));

		let ilItemCode = dataItem.ilItemCode;
		let createDate = dataItem.createDate;

		if(ilItemCode != ""){
			let params = {};
			params.ilItemCode = ilItemCode;
			params.createDate = createDate;

			fnKendoPopup({
				id			: "itemChangeLogPopup",
				title		: "업데이트 상세 내역", // 해당되는 Title 명 작성
				width		: "1700px",
				height		: "800px",
				scrollable	: "yes",
				src			: "#/itemChangeLogPopup",
				param		: params,
				success		: function( id, data ){
				}
			});
		}
	});

	function fnClear() {

	};
	//-------------------------------  Common Function end -------------------------------
	/** Common Clear */
	$scope.fnClear = function() {
		fnClear();
	};
}); // document ready - END
