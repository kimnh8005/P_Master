/**-----------------------------------------------------------------------------
 * description 		 : 묶음 상품 리스트
 * @
 * @ 수정일			수정자          수정내용
 * @ ------------------------------------------------------
 * @ 2020.11.18     정형진			퍼블수정 및 기능개발
 * @
 * **/

'use strict';
var pageParam = fnGetPageParam(); // GET 방식으로 전달된 parameter 객체

var PAGE_SIZE = 20;
var viewModel, itemGridOpt, itemGrid, itemGridDs;

$(document).ready(function() {

    // Initialize Page Call
	fnInitialize();

	// Initialize PageR
	function fnInitialize() {
		$scope.$emit('fnIsMenu', {
			flag : 'true'
		});

		fnPageInfo({
			PG_ID : 'goodsPackageList',
			callback : fnUI
		});
	};

	//전체화면 구성
	function fnUI() {
		fnTranslate();			// comm.lang.js 안에 있는 공통함수 다국어
		fnInitButton();			// Initialize Button

		fnInitCompont();		// 묶음 상품 컴포넌트 Initialize
		fnViewModelInit();

		fnDefaultSet();

		//탭 변경 이벤트
        fbTabChange();

        //체크 박스 공통 이벤트
        fbCheckboxChange();

        fnItemGrid();			// 묶음상품 내역 Grid
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
                  toggleElement(mode);
              }
	      });

		//묶음 상품 코드 검색
        fnKendoDropDownList({
            id: "codeType",
            data: [{
                	CODE: "goodsCode",
                	NAME: "묶음상품 코드",
            	},
            	{
            		CODE: "targetGoodsCode",
            		NAME: "구성상품 코드",
            	},
            	{
            		CODE: "itemCode",
            		NAME: "구성상품 품목코드",
            	}
            ],
            valueField: "CODE",
            textField: "NAME",
        });

        // 업체기준
        fnTagMkRadio({
            id : "companyStandardType",
            tagId : "companyStandardType",

            data  : [ { "CODE" : "SUPPLIER"  , "NAME" : "공급업체 기준" },
                      { "CODE" : "WAREHOUSE"  , "NAME" : "출고처 그룹 기준(자사)" }
                    ],
            style : {}
        });

        $("input[name=companyStandardType]").attr("data-bind", "checked: searchInfo.companyStandardType, events: {change: fnCompanyStandardTypeChange}");

     // 공급업체
        fnKendoDropDownList({
            id : "supplierId",
            tagId : "supplierId",
            url : "/admin/comn/getDropDownSupplierList",
            textField :"supplierName",
            valueField : "supplierId",
            blank : "공급업체 전체",

            async : false
        });

        // 공급업체 별 출고처
        fnKendoDropDownList({
            id : "supplierByWarehouseId",
            tagId : "supplierByWarehouseId",
            url : "/admin/comn/getDropDownSupplierByWarehouseList",
            textField :"warehouseName",
            valueField : "warehouseId",
            blank : "출고처 전체",
            async : false,
            cscdId     : "supplierId",
            cscdField  : "supplierId"
        });

     // 출고처그룹
        fnKendoDropDownList({
            id : "warehouseGroup",
            tagId : "warehouseGroup",
            url : "/admin/comn/getCodeList",
            params : { "stCommonCodeMasterCode" : "WAREHOUSE_GROUP", "useYn" : "Y" },
            blank : "출고처 그룹 전체",
            async : false
        });

        // 출고처그룹 별 출고처
        fnKendoDropDownList({
            id : "warehouseId",
            tagId : "warehouseId",
//            url : "/admin/comn/getDropDownWarehouseGroupByWarehouseList",
            url : "/admin/comn/getDropDownAuthWarehouseGroupByWarehouseList",
            textField :"warehouseName",
            valueField : "warehouseId",
            blank : "출고처 선택",
            async : false,
            params : { "warehouseGroupCode" : "" },
//            autoBind : true,
//            cscdId     : "warehouseGroup",
//            cscdField  : "warehouseGroupCode"
        });

        // 브랜드
        fnKendoDropDownList({
            id : "brandId",
            tagId : "brandId",
            url : "/admin/ur/brand/searchBrandList",
            params : {"searchUseYn": "Y"},
            textField :"brandName",
            valueField : "urBrandId",
            blank : "전체",
            async : false
        });

        fnKendoDropDownList({
            id : 'dpBrandId', 	// 전시 브랜드
            url : "/admin/ur/brand/searchDisplayBrandList",
            params : {"searchUseYn": "Y"},
            tagId : 'dpBrandId',
            autoBind : true,
            valueField : 'dpBrandId',
            textField : 'dpBrandName',
            chkVal : '',
            style : {},
            blank : '선택',
        });

        // 카테고리 구분
        fnKendoDropDownList({
            id : "categoryType",
            tagId : "categoryType",
            data : [ {"CODE" : "CATEGORY_STANDARD", "NAME" : "표준카테고리"},
                     {"CODE" : "CATEGORY_PULMUONE", "NAME" : "전시카테고리"},
                     {"CODE" : "CATEGORY_ORGA", "NAME" : "몰인몰(올가)"},
                     {"CODE" : "CATEGORY_BABYMEAL", "NAME" : "몰인몰(베이비밀)"},
                     {"CODE" : "CATEGORY_EATSLIM", "NAME" : "몰인몰(잇슬림)"}
                   ]
        });

        // 카테고리 구분
        fnKendoDropDownList({
            id : "excelType",
            tagId : "excelType",
            data : [ {"CODE" : "STANDARD_LIST", "NAME" : "기본정보 양식"},
                     {"CODE" : "DETAIL_LIST", "NAME" : "구성상품 정보 양식"}
                   ]
        });



        // 표준카테고리 대분류
        fnKendoDropDownList({
            id : "categoryStandardDepth1",
            tagId : "categoryStandardDepth1",
            url : "/admin/comn/getDropDownCategoryStandardList",
            params : { "depth" : "1" },
            textField : "categoryName",
            valueField : "categoryId",
            blank : "대분류",
            async : false
        });

        // 표준카테고리 중분류
        fnKendoDropDownList({
            id : "categoryStandardDepth2",
            tagId : "categoryStandardDepth2",
            url : "/admin/comn/getDropDownCategoryStandardList",
            textField : "categoryName",
            valueField : "categoryId",
            blank : "중분류",
            async : false,
            cscdId : "categoryStandardDepth1",
            cscdField : "categoryId"
        });

        // 표준카테고리 소분류
        fnKendoDropDownList({
            id : "categoryStandardDepth3",
            tagId : "categoryStandardDepth3",
            url : "/admin/comn/getDropDownCategoryStandardList",
            textField : "categoryName",
            valueField : "categoryId",
            blank : "소분류",
            async : false,
            cscdId : "categoryStandardDepth2",
            cscdField : "categoryId"
        });

        // 표준카테고리 세분류
        fnKendoDropDownList({
            id : "categoryStandardDepth4",
            tagId : "categoryStandardDepth4",
            url : "/admin/comn/getDropDownCategoryStandardList",
            textField : "categoryName",
            valueField : "categoryId",
            blank : "세분류",
            async : false,
            cscdId : "categoryStandardDepth3",
            cscdField : "categoryId"
        });

        // 전시카테고리 대분류
        fnKendoDropDownList({
            id : "categoryDepth1",
            tagId : "categoryDepth1",
            url : "/admin/comn/getDropDownCategoryList",
            params : { "depth" : "1", "mallDiv" : "MALL_DIV.PULMUONE" },
            textField : "categoryName",
            valueField : "categoryId",
            blank : "대분류",
            async : false
        });

        // 전시카테고리 중분류
        fnKendoDropDownList({
            id : "categoryDepth2",
            tagId : "categoryDepth2",
            url : "/admin/comn/getDropDownCategoryList",
            textField : "categoryName",
            valueField : "categoryId",
            blank : "중분류",
            async : false,
            cscdId : "categoryDepth1",
            cscdField : "categoryId"
        });

        // 전시카테고리 소분류
        fnKendoDropDownList({
            id : "categoryDepth3",
            tagId : "categoryDepth3",
            url : "/admin/comn/getDropDownCategoryList",
            textField : "categoryName",
            valueField : "categoryId",
            blank : "소분류",
            async : false,
            cscdId : "categoryDepth2",
            cscdField : "categoryId"
        });

        // 전시카테고리 세분류
        fnKendoDropDownList({
            id : "categoryDepth4",
            tagId : "categoryDepth4",
            url : "/admin/comn/getDropDownCategoryList",
            textField : "categoryName",
            valueField : "categoryId",
            blank : "세분류",
            async : false,
            cscdId : "categoryDepth3",
            cscdField : "categoryId"
        });

     // 검색기간
        fnKendoDropDownList({
            id : "dateSearchType",
            tagId : "dateSearchType",
            data : [ { "CODE" : "CREATE_DATE", "NAME" : "등록일" },
                     { "CODE" : "MODIFY_DATE", "NAME" : "최근수정일" },
                     { "CODE" : "SALE_DATE", "NAME" : "판매기간" }
                   ]
        });

        // 등록(가입)일 시작
        fnKendoDatePicker({
            id: "dateSearchStart",
            format: "yyyy-MM-dd",
            btnStartId: "dateSearchStart",
            btnEndId: "dateSearchEnd",
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
            change : function(e) {
                fnEndCalChange("dateSearchStart", "dateSearchEnd");
            }
        });

     // 판매상태
        fnTagMkChkBox({
            id: "saleStatus",
            tagId: "saleStatus",
            url: "/admin/comn/getCodeList",
            params: { stCommonCodeMasterCode: "SALE_STATUS", useYn: "Y" },
            beforeData: [{ CODE: "ALL", NAME: "전체" }],
            async: false,
        });

	};

	 function toggleElement(mode) {
         const $targetEl = $('[data-include="' + mode + '"]');

         $("[data-include]").toggleDisableInTag(true);
         $targetEl.toggleDisableInTag(false);
     };

	// viewModel 초기화
    function fnViewModelInit(){
        viewModel = new kendo.data.ObservableObject({
            searchInfo : { // 조회조건
            	searchType : "single", // 검색조건
            	codeType : "goodsCode",
            	goodsCodes : "",
                goodsName : "", // 상품명 (한글,영문대소문자, 숫자)
                companyStandardType : "", // 업체 기준 (공급처 or 출고처)
                supplierId : "", // 공급업체
                supplierByWarehouseId : "", // 공급업체 기준 출고처
                warehouseGroup : "", // 출고처 그룹
                warehouseGroupByWarehouseId : "", // 출고처 그룹 기준 출고처
                brandId : "", // 브랜드
                dpBrandId : "", // 전시브랜드
                categoryType : "", // 카테고리 구분
                categoryStandardDepth1 : "", // 표준카테고리 대분류
                categoryStandardDepth2 : "", // 표준카테고리 중분류
                categoryStandardDepth3 : "", // 표준카테고리 소분류
                categoryStandardDepth4 : "", // 표준카테고리 세분류
                categoryDepth1 : "", // 전시카테고리 대분류
                categoryDepth2 : "", // 전시카테고리 중분류
                categoryDepth3 : "", // 전시카테고리 소분류
                categoryDepth4 : "", // 전시카테고리 세분류
                dateSearchType : "", // 기간검색유형
                dateSearchStart : "", // 기간검색 시작일자
                dateSearchEnd : "", // 기간검색 종료일자
                saleStatus : [], // 판매상태
                publicStorageUrl : ""

            },
            supplierStandardVisible : true, // 공급처기준 Visible
            warehouseStandardVisible : false, // 출고처기준 Visible
            categoryStandardVisible : true, // 표준카테고리 Visible
            categoryVisible : false, // 전시카테고리 Visible
            gridSelectionForm : "", // 그리드 선택양식
            fnWareHouseGroupChange : function() {
            	fnAjax({
//                    url     : "/admin/comn/getDropDownWarehouseGroupByWarehouseList",
                    url : "/admin/comn/getDropDownAuthWarehouseGroupByWarehouseList",
                    method : "GET",
                    params : { "warehouseGroupCode" : viewModel.searchInfo.get("warehouseGroup") },

                    success : function( data ){
                        let warehouseId = $("#warehouseId").data("kendoDropDownList");
                        warehouseId.setDataSource(data.rows);
                    },
                    error : function(xhr, status, strError){
                        fnKendoMessage({ message : xhr.responseText });
                    },
                    isAction : "select"
                });
            },

            fnCompanyStandardTypeChange : function(){ // 업체기준 변경

                if( viewModel.searchInfo.companyStandardType == "SUPPLIER" ){

                    viewModel.set("supplierStandardVisible", true);
                    viewModel.set("warehouseStandardVisible", false);
                }else{

                    viewModel.set("supplierStandardVisible", false);
                    viewModel.set("warehouseStandardVisible", true);
                }

                viewModel.searchInfo.set("supplierId", "");
                viewModel.searchInfo.set("supplierByWarehouseId", "");
                viewModel.searchInfo.set("warehouseGroup", "");
                viewModel.searchInfo.set("warehouseGroupByWarehouseId", "");
            },
            fnCategoryTypeChange : function(){ // 카테고리 타입 변경

                let categoryType = viewModel.searchInfo.get("categoryType");

                if( categoryType == "CATEGORY_STANDARD" ){
                    viewModel.set("categoryStandardVisible", true);
                    viewModel.set("categoryVisible", false);
                }else{
                    viewModel.set("categoryStandardVisible", false);
                    viewModel.set("categoryVisible", true);

                    let mallDiv = "";

                    switch(categoryType){
                        case "CATEGORY_PULMUONE" : // 전시카테고리
                            mallDiv = "MALL_DIV.PULMUONE";
                            break;
                        case "CATEGORY_ORGA" : // 몰인몰(올가)
                            mallDiv = "MALL_DIV.ORGA";
                            break;
                        case "CATEGORY_BABYMEAL" : // 몰인몰(베이비밀)
                            mallDiv = "MALL_DIV.BABYMEAL";
                            break;
                        case "CATEGORY_EATSLIM" : // 몰인몰(잇슬림)
                            mallDiv = "MALL_DIV.EATSLIM";
                            break;
                    };

                    viewModel.fnGetDropDownCategoryList(mallDiv);
                }

                viewModel.searchInfo.set("categoryStandardDepth1", "");
                viewModel.searchInfo.set("categoryStandardDepth2", "");
                viewModel.searchInfo.set("categoryStandardDepth3", "");
                viewModel.searchInfo.set("categoryStandardDepth4", "");
                viewModel.searchInfo.set("categoryDepth1", "");
                viewModel.searchInfo.set("categoryDepth2", "");
                viewModel.searchInfo.set("categoryDepth3", "");
                viewModel.searchInfo.set("categoryDepth4", "");
            },
            fnGetDropDownCategoryList : function(mallDiv){ // 전시카테고리 정보 조회

                fnAjax({
                    url     : "/admin/comn/getDropDownCategoryList",
                    method : "GET",
                    params  : {"depth" : "1", "mallDiv" : mallDiv},
                    success : function( data ){
                        let categoryDepth1 = $("#categoryDepth1").data("kendoDropDownList");
                        categoryDepth1.setDataSource(data.rows);
                    },
                    error : function(xhr, status, strError){
                        fnKendoMessage({ message : xhr.responseText });
                    },
                    isAction : "select"
                });
            },
            fnGoodsInfo : function(rowData, ctrlKey){ // 묶음 상품 상세

                // 상품유형에 따라 상품상세화면 이동
                let option = {};
                let goodsId = rowData.goodsId;

                fnKendoPopup({
    			    id: 'goodsPackageDetailListPopup',
    			    title: '상품조합 상세정보',
    			    param: { "goodsId":  rowData.goodsId },
    			    src: '#/goodsPackageDetailListPopup',
    			    width: '1100px',
    			    height: '800px',
    			    success: function(id, data) {
    			        //fnSearch();
    			    }
    			});

            },
            fnGoodsLink  : function(rowData, ctrlKey){ // 묶음 상품 상세이동

            	 // 상품유형에 따라 상품상세화면 이동
                let option = {};
                option.data = { ilGoodsId : rowData.goodsId };
                option.url = "#/goodsPackage";
                option.menuId = 768;

/*
            	if(ctrlKey){
                	option.target = '_blank';
                	fnGoNewPage(option);
                } else {
                	fnGoPage(option);
                }
*/
            	option.target = '_blank';
            	fnGoNewPage(option);
            },
        });
        viewModel.publicStorageUrl = fnGetPublicStorageUrl();
        kendo.bind($("#searchForm"), viewModel);
    };

    // 기본값 설정
    function fnDefaultSet(){

        // 데이터 초기화
//        viewModel.searchInfo.set("searchType", "single");

    	viewModel.searchInfo.set("codeType", "goodsCode");
    	viewModel.searchInfo.set("goodsCodes", "");
        viewModel.searchInfo.set("goodsName", "");
        viewModel.searchInfo.set("companyStandardType", "SUPPLIER");
        viewModel.searchInfo.set("supplierId", "");
        viewModel.searchInfo.set("supplierByWarehouseId", "");
        viewModel.searchInfo.set("warehouseGroup", "");
        viewModel.searchInfo.set("warehouseId", "");
        viewModel.searchInfo.set("brandId", "");
        viewModel.searchInfo.set("dpBrandId", "");
        viewModel.searchInfo.set("categoryType", "CATEGORY_STANDARD");
        viewModel.searchInfo.set("dateSearchType", "CREATE_DATE");
        viewModel.searchInfo.set("saleStatus", []);
        $('[data-id="fnDateBtnC"]').mousedown();

        // 화면제어
        viewModel.fnWareHouseGroupChange();
        viewModel.fnCategoryTypeChange();
        viewModel.fnCompanyStandardTypeChange();
        $("input[name=saleStatus]:eq(0)").prop("checked", true).trigger("change");

    };

  //묶음 상품 리스트 Grid
  function fnItemGrid() {

		itemGridDs = fnGetEditPagingDataSource({
			url : '/admin/goods/list/getGoodsPackageList',
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
				{ field : 'goodsId',  title : '상품코드', width : '100px', attributes : { style : 'text-align:center' }}
			,	{ field : 'goodsTpNm', title : '상품<BR>유형', width : '40px', attributes : { style : 'text-align:center' } }

            ,   { field : 'promotionName', title : '프로모션명 <BR> 상품명', width : '200px', attributes : { style : 'text-align:center' },
	            	template : function(dataItem){
	                    let imageUrl = dataItem.goodsImagePath ? viewModel.publicStorageUrl + dataItem.goodsImagePath : '/contents/images/noimg.png';
	                    return "<img src='" + imageUrl + "' width='50' height='50' align='left' />" + dataItem.promotionName + "<BR>" + dataItem.goodsNm;
			          },
                }
            ,   { field : 'standardPrice', title : '원가<BR>총액', width : '80px', attributes : { style : 'text-align:center' },format: "{0:n0}" }
            ,	{ field : 'recommendedPrice', title : '정상가 <BR>총액', width : '80px', attributes : { style : 'text-align:center' },format: "{0:n0}" }
			,	{ field : 'salePrice', title : '묶음상품<BR>판매가', width : '80px', attributes : { style : 'text-align:center' },format: "{0:n0}" }
			,	{ field : 'discountTypeName', title : '할인유형', width : '120px', attributes : { style : 'text-align:center' } }
			,	{ field : 'salePrice', title : '판매가', width : '80px', attributes : { style : 'text-align:center' },format: "{0:n0}" }
			,	{ field : 'goodsCreateDate', title : '상품등록일<BR>최근수정일', width : '170px', attributes : { style : 'text-align:center' },
	                template : function(dataItem){
	                    return dataItem.goodsCreateDate + "<BR>" + fnNvl(dataItem.goodsModifyDate);
	                }
              	}
			,	{ field : 'categoryDepthName', title : '전시 카테고리', width : '170px', attributes : { style : 'text-align:center' } }
			,	{ field : 'saleStartDate', title : '판매기간', width : '170px', attributes : { style : 'text-align:center' },
	                template : function(dataItem){
	                    return dataItem.saleStartDate + "~<BR>" + dataItem.saleEndDate;
	                }
              	}
			,	{ field : 'saleStatusName', title : '판매상태', width : '70px', attributes : { style : 'text-align:center' } }
			,	{ field : 'dispYn', title : '전시상태', width : '70px', attributes : { style : 'text-align:center' },
                	template : function(dataItem){
                		return dataItem.dispYn == "Y" ? "전시" : "미전시";
                	}
              	}
			, { command : [ { name : "목록정보",
	                click : function(e) {
	                    e.preventDefault();
	                    let row = $(e.target).closest("tr");
	                    let rowData = this.dataItem(row);

	                    viewModel.fnGoodsInfo(rowData, e.ctrlKey);
	                }, template: "<a role=\"button\" class=\"k-button k-button-icontext k-grid-목록정보\">목록정보</a>"
	              }
	            ], title : "상품조합상세", width : "110px", attributes : { style : "text-align:center" },
			}
			,  { command : [ { name : "상세보기",
	                click : function(e) {
	                    e.preventDefault();
	                    let row = $(e.target).closest("tr");
	                    let rowData = this.dataItem(row);

	                    viewModel.fnGoodsLink(rowData, e.ctrlKey);
	                }, template: "<a role=\"button\" class=\"k-button k-button-icontext k-grid-상세보기\">상세보기</a>"
	              }
	            ], title : "관리", width : "110px", attributes : { style : "text-align:center" },
			}



            ]
		};

		itemGrid = $('#itemGrid').initializeKendoGrid(itemGridOpt).cKendoGrid();

		itemGrid.bind("dataBound", function() {
            $('#pageTotalText').text( kendo.toString(itemGridDs._total, "n0") );
        });

	};

	//검색
	function fnSearch() {

		if($("input[name=selectConditionType]:checked").val() == "singleSection") {
			viewModel.searchInfo.set("searchType", "single");
		}else {
			viewModel.searchInfo.set("searchType", "multi");

		}

		var data = $('#searchForm').formSerialize(true);

		var itemNameLengthChk = false;

		if($("input[name=selectConditionType]:checked").val() == "singleSection") {
           	if($.trim($('#goodsCodes').val()).length >= 0 && $.trim($('#goodsCodes').val()).length < 2 ) {
        		itemNameLengthChk = true;
        			fnKendoMessage({
        				message : '단일조건 코드 검색은 2글자 이상 입력해야 합니다.',
        				ok : function() {
        					return;
        				}
        			});
           	} else {
           		data['goodsCodes'] = $.trim($('#goodsCodes').val());
           	}
		}


		if(!itemNameLengthChk) {
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
		fnDefaultSet();

//		if($("input[name=selectConditionType]:checked").val() == "singleSection") {
//			$('#singleCodeForm').formClear(true);
//		}else {
//
//		}
	}

	//엑셀 다운로드
	function fnExcelExport() {

		var excelType = $("#excelType").val();

		$("#excelDom").val(excelType);

		var data = $('#searchForm').formSerialize(true);

		fnExcelDownload('/admin/goods/list/goodPackageExportExcel', data);
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