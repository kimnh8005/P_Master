/**-----------------------------------------------------------------------------
 * description 		 : 매장 재고리스트
 * @
 * @ 수정일			수정자          수정내용
 * @ ------------------------------------------------------
 * @ 2020.07.07		박영후          최초생성
 * @
 * **/
'use strict';


var PAGE_SIZE = 20;
var viewModel, aGridDs, aGridOpt, aGrid;

$(document).ready(function() {
	fnInitialize();	//Initialize Page Call ---------------------------------

	//Initialize PageR
	function fnInitialize(){
		$scope.$emit('fnIsMenu', { flag : 'true' });

		fnPageInfo({
			PG_ID  : 'itemShopStock',
			callback : fnUI
		});

	}

	function fnUI(){

		fnTranslate();	// 다국어 변환--------------------------------------------

		fnInitButton();	//Initialize Button  ---------------------------------

		fnInitOptionBox();//Initialize Option Box ------------------------------------

		fnViewModelInit();

		fnDefaultSet();

		fbTabChange();

		fnInitGrid();	//Initialize Grid ------------------------------------

	}

	//--------------------------------- Button Start---------------------------------

	function fnInitButton(){
		$('#fnSearch, #fnClear, #EXCEL_DN').kendoButton();
	}
	function fnSearch(){
		var data = $('#searchForm').formSerialize(true);

		var itemNameLengthChk = false;

		if($("input[name=selectConditionType]:checked").val() == "singleSection") {
			if($.trim($('#itemCodes').val()).length >= 0 && $.trim($('#itemCodes').val()).length < 2 ) {
				itemNameLengthChk = true;
				fnKendoMessage({
					message : '단일조건 코드 검색은 2글자 이상 입력해야 합니다.',
					ok : function() {
						return;
					}
				});
			} else {
				data['itemCodes'] = $.trim($('#itemCodes').val());
			}
		}

		if(!itemNameLengthChk) {
			const	_pageSize = aGrid && aGrid.dataSource ? aGrid.dataSource.pageSize() : PAGE_SIZE;

			var query = {
				page : 1,
				pageSize : _pageSize,
				filterLength : fnSearchData(data).length,
				filter : {
					filters : fnSearchData(data)
				}
			};

			aGridDs.query(query);
		}
	}
	function fnClear(){};

	function fnClose(){
		var kendoWindow =$('#kendoPopup').data('kendoWindow');
		kendoWindow.close();
	}

	//--------------------------------- Button End---------------------------------


	//------------------------------- Grid Start -------------------------------
	function fnInitGrid(){
		aGridDs = fnGetEditPagingDataSource({
			url      : '/admin/item/store/getItemStoreStockList',
			pageSize : PAGE_SIZE
		});

		aGridOpt = {
			dataSource: aGridDs
			,  pageable  : {
				pageSizes: [20, 30, 50, 100],
				buttonCount : 10
			}
			,navigatable: true
			,columns   : [
				{title : 'No'		, width:'80px'	,attributes:{ style:'text-align:center' }, template: "<span class='row-number'></span>"}
				,{ field:'itemCode'			,title : '품목코드'				, width:'65px'	,attributes:{ style:'text-align:center' }}
				,{ field:'itemNm'			,title : '마스터품목명'				, width:'170px'	,attributes:{ style:'text-align:center' }}
				,{ field:'companyNm'			,title : '공급업체'				, width:'100px'	,attributes:{ style:'text-align:center' }}
				,{ field:'brandNm'		,title : '표준브랜드'					, width:'100px'	,attributes:{ style:'text-align:center' }}
				,{ field:'dispBrandNm'		,title : '전시브랜드'					, width:'100px'	,attributes:{ style:'text-align:center' }}
				,{ field:'storeNm'			,title : '매장'					, width:'100px'	,attributes:{ style:'text-align:center' }}
				,{ field:'category'		,title : '표준카테고리<br>(대분류)'	, width:'100px'	,attributes:{ style:'text-align:center' }}
				,{ field:'itemStoreType'		,title : '유형'				, width:'80px'	,attributes:{ style:'text-align:center' }}
				,{ field:'storeStock'		,title : '재고'			, width:'50px'	,attributes:{ style:'text-align:center' }}
			]

		};
		aGrid = $('#aGrid').initializeKendoGrid( aGridOpt ).cKendoGrid();
        aGrid.bind("dataBound", function() {
			//total count
            $('#countTotalSpan').text(kendo.toString(aGridDs._total, "n0"));
			var row_number = aGridDs._total - ((aGridDs._page - 1) * aGridDs._pageSize);
			$("#aGrid tbody > tr .row-number").each(function(index){
				$(this).html(row_number);
				row_number--;
			});
        });

        // 품목 수정 페이지 이동
		$("#aGrid").on("click", "tbody>tr>td", function () {
			var index = $(this).index();

			if(index == 1 || index == 2) {
				var aMap = aGrid.dataItem(aGrid.select());
				var option = new Object();
				option.url = "#/itemMgmModify";  // 마스터 품목 수정 화면 URL
				option.target = "itemMgmModify";
				option.data = {                  // 마스터 품목 수정 화면으로 전달할 Data
					ilItemCode : aMap.itemCode,
					isErpItemLink : aMap.erpIfYn,
					masterItemType : aMap.itemType
				};
				fnGoNewPage(option);
			}
		});
	}

	//-------------------------------  Grid End  -------------------------------

	//---------------Initialize Option Box Start ------------------------------------------------
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

		// 매장명
		fnKendoDropDownList({
			id : "storeNm",
			tagId : "storeNm",
			url : "/admin/ur/urCompany/getStoreList",
			textField :"storeName",
			valueField : "urStoreId",
			blank : "전체",
			async : false
		});

		// 매장상품 유형
		fnTagMkChkBox({
			id : "itemStoreType",
			tagId : "itemStoreType",
			chkVal : "",
			data  : [ { "CODE" : ""   , "NAME" : "전체" },
				{ "CODE" : "저울코드(손질)"  , "NAME" : "저울코드(손질)" },
				{ "CODE" : "직제조(FRM)"  , "NAME" : "직제조(FRM)" },
				{ "CODE" : "사간거래"  , "NAME" : "사간거래" },
				{ "CODE" : "미대상(일반)"  , "NAME" : "미대상(일반)" }
			]
		});

		$("input[name=itemStoreType]").attr("data-bind", "checked: searchInfo.itemStoreType, events: {change: fnCheckboxChange}");

		if($("input[name=selectConditionType]:checked").val() == "multiSection") viewModel.searchInfo.set("itemCodes", "");

	}
	//---------------Initialize Option Box End ------------------------------------------------
	//-------------------------------  Common Function start -------------------------------

	// 기본값 설정
	function fnDefaultSet(){
		viewModel.searchInfo.set("itemCodes", "");
		viewModel.searchInfo.set("itemNm", "");
		viewModel.searchInfo.set("storeNm", "");
		viewModel.searchInfo.set("itemStoreType", [])
		$("input[name=itemStoreType]:eq(0)").prop("checked", true).trigger("change");
	};

	// viewModel 초기화
	function fnViewModelInit() {
		viewModel = new kendo.data.ObservableObject({
			searchInfo : {
				itemCodes : "",
				itemNm : "",
 				storeNm : "",
				itemStoreType : []
			},
			fnSearch : function(e){
				fnSearch();
			},
			fnClear: function (e) { // 초기화

				e.preventDefault();
				fnDefaultSet();
			},
			fnCheckboxChange : function(e){ // 체크박스 변경
				e.preventDefault();

				if( e.target.value == "" ){
					if( $("input[name=" + e.target.name + "]:eq(0)").is(":checked") ){

						$("input[name=" + e.target.name + "]:gt(0)").each(function(idx){
							if( viewModel.searchInfo.get(e.target.name).indexOf($(this).val()) < 0 ){
								viewModel.searchInfo.get(e.target.name).push($(this).val());
							}
						});
					}else{

						$("input[name=" + e.target.name + "]:gt(0)").each(function(idx){
							viewModel.searchInfo.get(e.target.name).remove($(this).val());
						});
					}
				}else{

					if( !$("#" + e.target.id).is(":checked") && $("input[name=" + e.target.name + "]:eq(0)").is(":checked") ){
						viewModel.searchInfo.get(e.target.name).remove($("input[name=" + e.target.name + "]:eq(0)").val());
					}
					else if( $("#" + e.target.id).is(":checked")
						&& ($("input[name=" + e.target.name + "]").length - 1) == viewModel.searchInfo.get(e.target.name).length )
					{
						viewModel.searchInfo.get(e.target.name).push($("input[name=" + e.target.name + "]:eq(0)").val());
					}
				}
			}
		});

		viewModel.publicStorageUrl = fnGetPublicStorageUrl();
		kendo.bind($("#searchForm"), viewModel);
	}

	//엑셀 다운로드
	function fnExcelExport() {
		var data = $('#searchForm').formSerialize(true);
		fnExcelDownload('/admin/item/store/itemStoreStockListExcel', data);
	}

	//-------------------------------  Common Function end -------------------------------


	//------------------------------- Html 버튼 바인딩  Start -------------------------------
	/** Common Search*/
	$scope.fnSearch = function( ) {	fnSearch();	};
	/** Common Clear*/
	$scope.fnClear =function(){	 fnClear();	};
	/** Common Close*/
	$scope.fnClose = function( ){  fnClose();};
	/** Common Excel */
	$scope.fnExcelExport = function() { fnExcelExport(); };
	//------------------------------- Html 버튼 바인딩  End -------------------------------

}); // document ready - END
