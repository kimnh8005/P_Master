/**-----------------------------------------------------------------------------
 * description 		 : 임직원상품 할인 일괄 업로드 내역
 * @
 * @ 수정일			수정자          수정내용
 * @ ------------------------------------------------------
 * @ 2020.12.08     정형진			퍼블수정 및 기능개발
 * @
 * **/

'use strict';
var pageParam = fnGetPageParam(); // GET 방식으로 전달된 parameter 객체
var PAGE_SIZE = 20;
var itemGridOpt, itemGrid, itemGridDs;

$(document).ready(function() {

    // Initialize Page Call
	fnInitialize();

	// Initialize PageR
	function fnInitialize() {
		$scope.$emit('fnIsMenu', {
			flag : 'true'
		});

		fnPageInfo({
			PG_ID : 'goodsDiscountUploadList',
			callback : fnUI
		});
	};

	function fnUI() {
		fnTranslate();			// comm.lang.js 안에 있는 공통함수 다국어

    	fnInitButton();			// Initialize Button
    	fnInitCompont();			// 라디오 , 체크박스 셋팅.
    	fnItemGrid();			// 상품할인 일괄 업로드 내역 Grid

	}

	function fnInitButton() {
		$('#fnSearch , #fnClear , #EXCEL_DN').kendoButton();
	};

	function fnInitCompont() {
		//등록일/수정일 시작날짜
		fnKendoDatePicker({
			id : 'startDate',
			defVal : fnGetDayMinus(fnGetToday(),6),
			defType : 'oneWeek',
			format : 'yyyy-MM-dd'
		});

		//등록일/수정일 종료날짜
		fnKendoDatePicker({
			id : 'endDate',
			format : 'yyyy-MM-dd',
			btnStyle : true,
			btnStartId : 'startDate',
			btnEndId : 'endDate',
			defVal : fnGetToday(),
			defType : 'oneWeek',
			change : function(e) {
				fnStartCalChange('startDate', 'endDate');
			}
		});
	};

	//상품할인 업데이트 리스트 Grid
	function fnItemGrid() {

		itemGridDs = fnGetEditPagingDataSource({
			url : '/admin/goods/discount/getGoodsDisEmpUploadList',
			pageSize : PAGE_SIZE,
			requestEnd : function(e){

			}
		});

		itemGridOpt = {
			dataSource : itemGridDs,
			pageable : {
				pageSizes : [ 20, 30, 50 ],
				buttonCount : 5
			},
			editable : false,
			navigatable: true,
			columns : [
				{ field : 'rownum',  title : 'No', width : '40px', attributes : { style : 'text-align:center' }}
			,	{ field : 'createDt', title : '등록일자', width : '80px', attributes : { style : 'text-align:center' } }
            ,   { field : 'successCnt', title : '정상', width : '100px', attributes : { style : 'text-align:center' }}
            ,   { field : 'failCnt', title : '실패', width : '100px', attributes : { style : 'text-align:center' },format: "{0:n0}" }
            ,   { field : 'failCnt', title : '관리', width : '100px', attributes : { style : 'text-align:center' },
            		template : function(dataItem){
            			if(dataItem.failCnt > 0) {
            				if(fnIsProgramAuth("EXCELDOWN")) {
            					return '<button type="button" class="k-button k-button-icontext k-grid-상세보기" kind="btnDtl">상세보기 </button> <button type="button" class="k-button k-button-icontext k-grid-실패내역다운로드" kind="btnExcelDown">실패내역 다운로드 </button>';
            				}else {
            					return '<button type="button" class="k-button k-button-icontext k-grid-상세보기" kind="btnDtl">상세보기 </button>'
            				}
            			}else {
            				return '<button type="button" class="k-button k-button-icontext k-grid-상세보기" kind="btnDtl">상세보기 </button>'
            			}

            		}
          		}
            ]
		};
		itemGrid = $('#itemGrid').initializeKendoGrid(itemGridOpt).cKendoGrid();

		itemGrid.bind("dataBound", function() {
            $('#pageTotalText').text( kendo.toString(itemGridDs._total, "n0") );
        });

	};

    // 상세 내역 버튼 이벤트
    $('#itemGrid').on("click", "button[kind=btnDtl]", function(e) {
      e.preventDefault();
      let dataItem = itemGrid.dataItem($(e.currentTarget).closest("tr"));

      var option = new Object();

      option.url = "#/goodsDisEmpUploadDtlList";	//
      option.menuId = 1026;// ERP 재고 엑셀 업로드 내역 화면 메뉴 ID
      option.data = {                  // 일반상품 화면으로 전달할 Data
              logId : dataItem.ilGoodsDiscountUploadLogId
      };

      fnGoPage(option);

    });

    // 실패내역 다운로드 버튼 이벤트.
    $('#itemGrid').on("click", "button[kind=btnExcelDown]", function(e) {
        e.preventDefault();
        let dataItem = itemGrid.dataItem($(e.currentTarget).closest("tr"));
        console.log("## dataItem ->"+ JSON.stringify(dataItem));

        $("#logId").val(dataItem.ilGoodsDiscountUploadLogId);

        var data = $('#searchForm').formSerialize(true);

		fnExcelDownload('/admin/goods/discount/createGoodsDiscountEmpUploadFailList', data);

      });


	//검색
	function fnSearch() {
		var data = $('#searchForm').formSerialize(true);

		 const	_pageSize = itemGrid && itemGrid.dataSource ? itemGrid.dataSource.pageSize() : PAGE_SIZE;

		var query = {
			page : 1,
			pageSize : _pageSize,
			filterLength : fnSearchData(data).length,
			filter : {
				filters : fnSearchData(data)
			}
		};

		itemGridDs.query(query);
	};

	function fnClear() {
		$('[data-id="fnDateBtnC"]').mousedown();
		$('#searchForm').formClear(true);
	};

	/** Common Search */
	$scope.fnSearch = function() {
		fnSearch();
	};

	/** Common Clear */
	$scope.fnClear = function() {
		fnClear();
	};

});