/**-----------------------------------------------------------------------------
 * description 		 : 상품 할인 일괄 업로드 상세 내역
 * @
 * @ 수정일			수정자          수정내용
 * @ ------------------------------------------------------
 * @ 2020.11.18     정형진			퍼블수정 및 기능개발
 * @
 * **/

'use strict';
var pageParam = fnGetPageParam(); // GET 방식으로 전달된 parameter 객체
var PAGE_SIZE = 20;
var itemGridOpt, itemGrid, itemGridDs;
$(document).ready(function() {

    // Initialize Page Call
	fnInitialize();

	 // Initialize Page Call
	fnInitialize();

	// Initialize PageR
	function fnInitialize() {
		$scope.$emit('fnIsMenu', {
			flag : 'true'
		});

		fnPageInfo({
			PG_ID : 'goodsDiscountUploadDtlList',
			callback : fnUI
		});
	};

	function fnUI() {
		fnTranslate();			// comm.lang.js 안에 있는 공통함수 다국어

		fnInitButton();			// Initialize Button

		fnInitCompont();		// 묶음 상품 컴포넌트 Initialize

		//탭 변경 이벤트
        fbTabChange();

        fnItemGrid();			// 상품 할인 일괄업로드 상세 내역 Grid

        $("#logId").val(pageParam.logId);

        fnSearch();

	};


	function fnInitButton() {
		$('#fnSearch , #fnClear , #EXCEL_DN').kendoButton();
	};

	function fnInitCompont() {
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
                  const mode = e.target.value.trim();
//                  toggleElement(mode);
              }
	      });

		//묶음 상품 코드 검색
        fnKendoDropDownList({
            id: "codeType",
            data: [{
                	CODE: "itemCode",
                	NAME: "마스터 품목 코드",
            	},
            	{
            		CODE: "barcodeCd",
            		NAME: "품목바코드",
            	},
            	{
            		CODE: "goodsId",
            		NAME: "상품코드",
            	}
            ],
            valueField: "CODE",
            textField: "NAME",
        });

        // 등록(가입)일 시작
        fnKendoDatePicker({
            id: "startDate",
            format: "yyyy-MM-dd",
            btnStartId: "startDate",
            btnEndId: "endDate",
            change : function(e) {
                fnStartCalChange("startDate", "endDate");
            }
        });

     // 등록(가입)일 종료
        fnKendoDatePicker({
            id: "endDate",
            format: "yyyy-MM-dd",
            btnStyle: true,
            btnStartId: "startDate",
            btnEndId: "endDate",
            change : function(e) {
                fnEndCalChange("startDate", "endDate");
            }
        });

        fnInputValidationForAlphabetNumberLineBreakComma("goodsCodes"); // 검색어
        fnInputValidationForHangulAlphabetNumber("goodsNm"); // 상품명

	};

	//묶음 상품 리스트 Grid
	function fnItemGrid() {

		itemGridDs = fnGetEditPagingDataSource({
			url : '/admin/goods/discount/getGoodsDiscountUploadDtlList',
			pageSize : PAGE_SIZE,
	           requestEnd : function(e) {
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
			,	{ field : 'itemCd', title : '마스터품목코드<BR>품목바코드', width : '160px', attributes : { style : 'text-align:center' } ,
		          template : function(dataItem){
                      return dataItem.itemCd + "<BR>" + dataItem.itemBarcode;
		          },
                }
            ,   { field : 'goodsId', title : '상품코드', width : '200px', attributes : { style : 'text-align:center' }}
            ,   { field : 'goodsNm', title : '상품명', width : '160px', attributes : { style : 'text-align:center' }}
            ,	{ field : 'successYn', title : '성공여부', width : '80px', attributes : { style : 'text-align:center' } }
			,	{ field : 'discountTpCodeNm', title : '할인구분', width : '80px', attributes : { style : 'text-align:center' } }
			,	{ field : 'discountMethodTpCodeNm', title : '할인유형', width : '120px', attributes : { style : 'text-align:center' } }
			,	{ field : 'discountStartDt', title : '할인 시작일', width : '160px', attributes : { style : 'text-align:center' } }
			,	{ field : 'discountEndDt', title : '할인 종료일', width : '160px', attributes : { style : 'text-align:center' }}
			,	{ field : 'standardPrice', title : '원가', width : '170px', attributes : { style : 'text-align:center' },format: "{0:n0}" }
			,	{ field : 'recommendedPrice', title : '정상가', width : '170px', attributes : { style : 'text-align:center' },format: "{0:n0}"}
			,	{ field : 'discountRatio', title : '할인율', width : '70px', attributes : { style : 'text-align:center' },
		          template : function(dataItem){
                      return dataItem.discountRatio + "%";
		          },
                }
			,	{ field : 'discountPrice', title : '판매가', width : '70px', attributes : { style : 'text-align:center' },format: "{0:n0}"}

            ]
		};

		itemGrid = $('#itemGrid').initializeKendoGrid(itemGridOpt).cKendoGrid();

		itemGrid.bind("dataBound", function() {
			$('#pageTotalText').text( kendo.toString(itemGridDs._total, "n0") );
		});

	};

	//검색
	function fnSearch() {

		var searchVaild = true;

		if($("input[name=selectConditionType]:checked").val() == "singleSection") {
			$("#searchType").val("single");

			// 상품명 : trim 으로 공백 제거, 2글자 이상 입력해야 함
			if( $.trim($('#goodsCodes').val()).length == 1) {
				searchVaild = false;
	            fnKendoMessage({
	                message : '코드 검색 조회시 2글자 이상 입력해야 합니다.',
	                ok : function() {
	                    return false;
	                }
	            });
			};

		}else {
			$("#searchType").val("multi");

			// 상품명 : trim 으로 공백 제거, 2글자 이상 입력해야 함
			if($.trim($('#goodsNm').val()).length == 1) {
				searchVaild = false;
	            fnKendoMessage({
	                message : '상품명 조회시 2글자 이상 입력해야 합니다.',
	                ok : function() {
	                    return;
	                }
	            });
			};

		}

		if(searchVaild) {
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
		}


	};

	function fnClear() {
		$('#searchForm').formClear(true);
		$('[data-id="fnDateBtnC"]').mousedown();
		$("#logId").val(pageParam.logId);
	};

	function fnExcelExport() {
		var data = $('#searchForm').formSerialize(true);
		fnExcelDownload('/admin/goods/discount/createGoodsDiscountUploadDtlListExcel', data);
	}

	/** Common Search */
	$scope.fnSearch = function() {
		fnSearch();
	};

	/** Common Clear */
	$scope.fnClear = function() {
		fnClear();
	};

	/** Common Excel */
	$scope.fnExcelExport = function() {
		fnExcelExport();
	};

});
