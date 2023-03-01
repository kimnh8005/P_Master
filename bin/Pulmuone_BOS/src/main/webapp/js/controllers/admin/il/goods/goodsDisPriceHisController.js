/*******************************************************************************************************************************************************************************************************
 * -------------------------------------------------------- description : 상품할인 변경내역 - 상품할인 변경 리스트 * @ @ 수정일 수정자 수정내용 @ ------------------------------------------------------ * @ 2020.11.18 정형진 최초생성 @
******************************************************************************************************************************************************************************************************/
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
			PG_ID : 'goodsDisPriceHis',
			callback : fnUI
		});
	};

	//전체화면 구성
	function fnUI() {
		fnTranslate();			// comm.lang.js 안에 있는 공통함수 다국어
		fnInitButton();			// Initialize Button
		fnInitCompont();	// 상품 할인 변경 검색조건 Initialize
        //체크 박스 공통 이벤트
        fbCheckboxChange();

        checkDiscountTp();

        fnItemGrid();			// 상품할인 업데이트 내역 Grid
	}

	function fnInitButton() {
		$('#fnSearch , #fnClear , #EXCEL_DN').kendoButton();
	};

	function fnInitCompont() {

		fnKendoDropDownList({
	        id: "keywordType",
	        data: [
	            { CODE: "goodsNm", NAME: "상품명" },
	            { CODE: "goodsId", NAME: "상품코드" },
	            { CODE: "itemCd", NAME: "품목코드" }
	        ],
	        valueField: "CODE",
	        textField: "NAME"

	    });

	    fnKendoDropDownList({
	        id: "userType1",
	        data: [
	            { CODE: "SYSTEM", NAME: "시스템" },
	            { CODE: "MANAGER_NAME", NAME: "관리자(이름)" },
	            { CODE: "MANAGER_ID", NAME: "관리자(아이디)" }
	        ],
	        valueField: "CODE",
	        textField: "NAME",
	        blank: "전체",
	    });

		fnKendoDropDownList({
	        id: "userType2",
	        data: [
	            { CODE: "SYSTEM", NAME: "시스템" },
	            { CODE: "MANAGER_NAME", NAME: "관리자(이름)" },
	            { CODE: "MANAGER_ID", NAME: "관리자(아이디)" }
	        ],
	        valueField: "CODE",
	        textField: "NAME",
	        blank: "전체",
	    });

		// 공급업체
        fnKendoDropDownList({
            id : "supplierId",
            tagId : "supplierId",
//            url : "/admin/comn/getDropDownSupplierList",
            url : "/admin/comn/getDropDownAuthSupplierList",
            textField :"supplierName",
            valueField : "supplierId",
            blank : "공급업체 전체",
            async : false
        });

        fnTagMkChkBox({
             id: "discountTp",
             tagId: "discountTp",
             data: [
                 {
                     CODE: "ALL",
                     NAME: "전체",
                 },
                 {
                     CODE: "GOODS_DISCOUNT_TP.NONE",
                     NAME: "없음",
                 },
                 {
                     CODE: "GOODS_DISCOUNT_TP.PRIORITY",
                     NAME: "우선할인",
                 },
                 {
                     CODE: "GOODS_DISCOUNT_TP.ERP_EVENT",
                     NAME: "ERP행사",
                 },
                 {
                     CODE: "GOODS_DISCOUNT_TP.IMMEDIATE",
                     NAME: "즉시할인",
                 },
                 {
                     CODE: "GOODS_DISCOUNT_TP.PACKAGE",
                     NAME: "기본할인(묶음상품)",
                 },
                 {
                     CODE: "GOODS_DISCOUNT_TP.NOT_APPLICABLE",
                     NAME: "적용불가",
                 }
             ],
         });

        fnTagMkChkBox({
            id: "goodsSort",
            tagId: "goodsSort",
            data: [
                {
                    CODE: "Y",
                    NAME: "상품코드 순으로 보기",
                },
            ],
        });


		fnTagMkRadio({
            id: "taxYn",
            tagId: "taxYn",
            chkVal: "",
            data: [
                {
                    CODE: "",
                    NAME: "전체",
                },
                {
                    CODE: "Y",
                    NAME: "과세",
                },
                {
                    CODE: "N",
                    NAME: "면세",
                }

            ],
        });

		//등록일/수정일 시작날짜
		fnKendoDatePicker({
			id : 'startDate',
			format : 'yyyy-MM-dd'
		});

		//등록일/수정일 종료날짜
		fnKendoDatePicker({
			id : 'endDate',
			format : 'yyyy-MM-dd',
			btnStyle : true,
			btnStartId : 'startDate',
			btnEndId : 'endDate',
			change : function(e) {
				fnStartCalChange('startDate', 'endDate');
			}
		});

		fnKendoDropDownList({
	        id: "dateSearchType",
	        data: [
	            { CODE: "dateType1", NAME: "할인기간" },
	            { CODE: "dateType2", NAME: "적용일" },
	            { CODE: "dateType3", NAME: "승인완료일" }
	        ],
	        valueField: "CODE",
	        textField: "NAME",
	    });


        // 승인 요청자 DropDown 변경 이벤트
	    $("#userType1").bind("change", function() {
            if( $("#userType1").val() == "" || $("#userType1").val() == "SYSTEM"){
                $("#userTypeVal1").val("").attr("disabled", true);
            }else{
                $("#userTypeVal1").attr("disabled", false);
            }
        });

        // 승인 완료자 DropDown 변경 이벤트
	    $("#userType2").bind("change", function() {
            if( $("#userType2").val() == "" || $("#userType2").val() == "SYSTEM"){
                $("#userTypeVal2").val("").attr("disabled", true);
            }else{
                $("#userTypeVal2").attr("disabled", false);
            }
        });

        // 상품코드 순으로 보기 체크박스 이벤트
	    $("#goodsSort").click(function () {
        	if (this.checked) {
        		$("#sort").val("Y");
        	}else {
        		$("#sort").val("N");
        	}
    		fnSearch();
      });


	}

	//상품할인 업데이트 리스트 Grid
	function fnItemGrid() {

		itemGridDs = fnGetEditPagingDataSource({
			url : '/admin/goods/list/getGoodsDisPriceHisList',
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
				{ field : 'compNm',  title : '공급업체', width : '100px', attributes : { style : 'text-align:center' }}
			,	{ field : 'goodsId', title : '상품코드', width : '80px', attributes : { style : 'text-align:center' } }
            ,   { field : 'itemCd', title : '품목코드', width : '120px', attributes : { style : 'text-align:center' }}
            ,   { field : 'goodsNm', title : '상품명', width : '180px', attributes : { style : 'text-align:center' },format: "{0:n0}" }
            ,	{ field : 'taxYnNm', title : '세금구분', width : '60px', attributes : { style : 'text-align:center' },format: "{0:n0}" }
			,	{ field : 'standardPrice', title : '원가', width : '80px', attributes : { style : 'text-align:center' },format: "{0:n0}" }
			,	{ field : 'recommendedPrice', title : '정상가', width : '80px', attributes : { style : 'text-align:center' },format: "{0:n0}" }
			,	{ field : 'marginRate', title : '정상마진율', width : '80px', attributes : { style : 'text-align:center' },format: "{0:n0}" ,
					template : function(dataItem){
						return dataItem.marginRate + "%";
					}
          		}
			,	{ field : 'salePrice', title : '판매가', width : '80px', attributes : { style : 'text-align:center' },format: "{0:n0}" }
			,	{ field : 'marginRate2', title : '판매마진율', width : '80px', attributes : { style : 'text-align:center' },format: "{0:n0}",
					template : function(dataItem){
						return dataItem.marginRate + "%";
					}
      			}
			,	{ field : 'discountTpNm', title : '할인구분', width : '100px', attributes : { style : 'text-align:center' }}
			,	{ field : 'priceStartDt', title : '적용일', width : '120px', attributes : { style : 'text-align:center' } }
			,	{ field : 'discountStartDt', title : '시작일', width : '120px', attributes : { style : 'text-align:center' } }
			,	{ field : 'discountEndDt', title : '종료일', width : '120px', attributes : { style : 'text-align:center' } }
			,	{ field : 'confirmDt', title : '승인완료일', width : '70px', attributes : { style : 'text-align:center' }}
			,	{ field : 'aproveId', title : '승인요청', width : '70px', attributes : { style : 'text-align:center' }}
			,	{ field : 'confirmId', title : '승인완료', width : '70px', attributes : { style : 'text-align:center' }}

            ]
		};
		itemGrid = $('#itemGrid').initializeKendoGrid(itemGridOpt).cKendoGrid();

		itemGrid.bind("dataBound", function() {
            $('#pageTotalText').text( kendo.toString(itemGridDs._total, "n0") );
        });

	};

	function checkDiscountTp(){
		$('input[name=discountTp]').prop("checked",true);
	}

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
		$('#searchForm').formClear(true);
		checkDiscountTp();
	}

	//엑셀 다운로드
	function fnExcelExport() {
		var data = $('#searchForm').formSerialize(true);

		fnExcelDownload('/admin/goods/list/goodsDisPriceExportExcel', data);
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
