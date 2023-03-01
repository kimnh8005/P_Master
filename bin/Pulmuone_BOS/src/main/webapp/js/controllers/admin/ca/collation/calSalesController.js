/**-----------------------------------------------------------------------------
 * description 		 : 정산관리 > 대사관리 > 통합몰 매출 대사 내역
 * @
 * @ 수정일			수정자          수정내용
 * @ ------------------------------------------------------
 * @ 2021.04.28		이원호          최초생성
 * **/
"use strict";

var PAGE_SIZE = 20;
var viewModel, defaultGridDs, defaultGridOpt, defaultGrid;
var pageParam = fnGetPageParam();

$(document).ready(function() {

	importScript("/js/service/ca/collation/calCollationGridColumns.js", function (){
		importScript("/js/service/ca/collation/calCollationSearch.js", function (){
			importScript("/js/service/ca/collation/calCollationSubmitUtil.js", function (){
				importScript("/js/service/ca/collation/calCollationFunction.js", function (){
					fnInitialize();
				});
			});
		});
	});

	//Initialize PageR
	function fnInitialize(){
		$scope.$emit("fnIsMenu", { flag : true });
		fnPageInfo({
			PG_ID  : "calSales",
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
		defaultGridDs = fnGetPagingDataSource({
            url      : "/admin/calculate/collation/getSalesList",
            pageSize : PAGE_SIZE,
            requestEnd : function(e){
                $("#erpSumPrice").text(kendo.toString(e.response.data.erpSumPrice, "n0"));
                $("#bosSumPrice").text(kendo.toString(e.response.data.bosSumPrice, "n0"));
            }
        });
        defaultGridOpt = {
            dataSource: defaultGridDs
            ,  pageable  : {
                pageSizes: [20, 30, 50],
                buttonCount : 10
            }
            ,navigatable: true
            ,columns   : calCollationGridUtil.calSalesList()
        };

        defaultGrid = $('#defaultGrid').initializeKendoGrid( defaultGridOpt ).cKendoGrid();

        defaultGrid.bind("dataBound", function() {
            var row_num = defaultGridDs._total - ((defaultGridDs._page - 1) * defaultGridDs._pageSize);
            $("#defaultGrid tbody > tr .row-number").each(function(index){
                $(this).html(row_num);
                row_num--;
            });

            $('#totalCnt').text(kendo.toString(defaultGridDs._total, "n0"));
        });
	};

	//-------------------------------  Grid End  -------------------------------

	//---------------Initialize Option Box Start ------------------------------------------------

	// 옵션 초기화
	function fnInitOptionBox(){
	    // ERP 기간조회
	    // 시작일
        fnKendoDatePicker({
            id: "erpDateSearchStart",
            format: "yyyy-MM-dd",
            btnStartId: "erpDateSearchStart",
            btnEndId: "erpDateSearchEnd",
            defVal : fnGetDayAdd(fnGetToday(),-30),
            defType : 'oneMonth',
            change : function(e) {
                fnStartCalChange("erpDateSearchStart", "erpDateSearchEnd");
                fnValidateCal(e)
            }
        });
        // 종료일
        fnKendoDatePicker({
            id: "erpDateSearchEnd",
            format: "yyyy-MM-dd",
            btnStyle: true,
            btnStartId: "erpDateSearchStart",
            btnEndId: "erpDateSearchEnd",
            defVal : fnGetToday(),
            defType : 'oneMonth',
            minusCheck: true,
            nextDate: false,
            change : function(e) {
                fnEndCalChange("erpDateSearchStart", "erpDateSearchEnd");
                fnValidateCal(e)
            }
        });

		// 기간조회
		calCollationSearchUtil.dateSearchType(calCollationOptionUtil.dateSearchGoodsData())
		calCollationSearchUtil.dateSearch();

		// 날짜 초기화 이벤트 변경
		calCollationSearchUtil.fnInitFirstDateAndLastDate("erpDateSearchStart", "erpDateSearchEnd");
		calCollationSearchUtil.fnInitFirstDateAndLastDate("dateSearchStart", "dateSearchEnd");
		$("#erpDateSearchStart").closest('.complex-condition').find('[data-id="fnDateBtnC"]').off("mousedown").on("mousedown", function (){
			calCollationSearchUtil.fnInitFirstDateAndLastDate("erpDateSearchStart", "erpDateSearchEnd");
		});
		$("#dateSearchStart").closest('.complex-condition').find('[data-id="fnDateBtnC"]').off("mousedown").on("mousedown", function (){
			calCollationSearchUtil.fnInitFirstDateAndLastDate("dateSearchStart", "dateSearchEnd");
		});

		// 구분
		calCollationSearchUtil.getCheckBoxLocalData(calCollationOptionUtil.optSalesGubun);

		// 매칭 여부
		calCollationSearchUtil.getRadioLocalData(calCollationOptionUtil.optSalesMatchingType);

		// 공급업체
		const SUPPLIER_ID = "supplierId";
		calCollationSearchUtil.getDropDownUrl(SUPPLIER_ID, SUPPLIER_ID, "/admin/comn/getDropDownSupplierList", "supplierName", "supplierId", "전체");

		// 출고처
		calCollationSearchUtil.getDropDownCommCd("warehouseGroup", "NAME", "CODE", "출고처 그룹 전체", "WAREHOUSE_GROUP", "Y");

		// 출고처그룹 별 출고처
		const WAREHOSE_ID = "warehouseId";
		calCollationSearchUtil.getDropDownUrl(WAREHOSE_ID, WAREHOSE_ID, "/admin/comn/getDropDownWarehouseGroupByWarehouseList", "warehouseName", "warehouseId", "출고처 전체", "warehouseGroup", "warehouseGroupCode");

		// BOS 주문 상태
		calCollationSearchUtil.getCheckBoxUrl(calCollationOptionUtil.orderStatus("orderState", "O"));

		// BOS 클레임 상태
		calCollationSearchUtil.getCheckBoxUrl(calCollationOptionUtil.orderStatus("claimState", "C"));

		// 검색어 조회
		calCollationSearchUtil.searchTypeKeyword();
		calCollationSearchUtil.setFindKeyword();

        fbCheckboxChange(); //[공통] checkBox
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
	$scope.fnSearch = function( ) {	calCollationSubmitUtil.search();	};

	/** Common Clear*/
	$scope.fnClear =function(){	 calCollationSubmitUtil.clear();	};

	/** 확정완료 */
	$scope.fnExcelDownload = function() {
		var data = $('#searchForm').formSerialize(true);
        fnExcelDownload('/admin/calculate/collation/getSalesListExportExcel', data);
	};
	//------------------------------- Html 버튼 바인딩  End -------------------------------

	//------------------------------- Validation Start -----------------------------------

	//------------------------------- Validation End -------------------------------------
}); // document ready - END
