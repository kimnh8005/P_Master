/**-----------------------------------------------------------------------------
 * system           : 상품 검색 팝업
 * @
 * @ 수정일         수정자          수정내용
 * @ ------------------------------------------------------
 * @ 2020.08.14     손진구          최초생성
 * @
 * **/
"use strict";

var PAGE_SIZE = 20;
var goodsGridDs, goodsGridOpt, goodsGrid, viewModel;
var pageParam = parent.POP_PARAM["parameter"];

var parentGridTotal = 0;
var parentPgId = "";
var parentGoodsIdAry = [];

var initializedSeachCond = false;
var prevSearchData;
var goodsAllGridDs, goodsAllGridOpt, goodsAllGrid;

$(document).ready(function() {

    fnInitialize();	//Initialize Page Call ---------------------------------

    //Initialize PageR
    function fnInitialize(){
        $scope.$emit("fnIsMenu", { flag : false });

        fnPageInfo({
            PG_ID  : "goodsSearchPopup",
            callback : fnUI
        });
    };

    // 화면 UI 초기화
    function fnUI(){
        //체크 박스 공통 이벤트
        fbCheckboxChange();

        fnInitButton();
        fnInitOptionBox();
        fnViewModelInit();
        fnDefaultSet();
        fnSetParamValue();
        fnInitGrid();

		//탭 변경 이벤트
        fbTabChange();

    };

    //--------------------------------- Button Start---------------------------------

    // 버튼 초기화
    function fnInitButton(){
        $("#fnSearch, #fnClear, #fnChoiceAdd, #fnSearchChoiceAdd").kendoButton();
    };

    //--------------------------------- Button End---------------------------------


    //------------------------------- Grid Start -------------------------------
    // 그리드 초기화
    function fnInitGrid(){

        goodsGridDs = fnGetPagingDataSource({
            url      : "/admin/comn/popup/getGoodsList",
            pageSize : PAGE_SIZE

        });

        goodsGridOpt = {
            dataSource : goodsGridDs,
            pageable : { pageSizes: [20, 30, 50, 100], buttonCount : 10 },
            height : 470,
            navigatable: true,
            scrollable : true,

            columns   : [
                          { field : "chk", headerTemplate : viewModel.selectMultiRowYn ? '<input type="checkbox" id="checkBoxAll" />' : ' ',
                            template : '<input type="checkbox" name="itemGridChk" class="k-checkbox" />',
                            width : "60px", attributes : {style : "text-align:center;"}, locked: true, lockable: false
                          }
                        , { field : "itemCode", title : "품목코드<BR>품목바코드", width : "170px", attributes : {style : "text-align:center;"},
                            template : function(dataItem){
                                let itemCodeView = dataItem.itemCode;

                                if( dataItem.itemBarcode != "" ){
                                    itemCodeView += "<BR>" + "(" + dataItem.itemBarcode + ")";
                                }

                                return itemCodeView;
                            }, locked: true
                          }
                        , { field : "goodsId", title : "상품코드", width: "100px", attributes : {style : "text-align:center;"}, locked: true }
                        , { field : "goodsName", title : "상품명", width: "300px", attributes : {style : "text-align:center;"},
                            template : function(dataItem){
                                let imageUrl = dataItem.itemImagePath ? viewModel.publicStorageUrl + dataItem.itemImagePath : '/contents/images/noimg.png';
                                return "<img src='" + imageUrl + "' width='50' height='50' align='left' /><br/>" + dataItem.goodsName;
                            }, locked: true
                          }
                        , { field : "goodsTypeName", title : "상품유형", width: "70px", attributes : {style : "text-align:center;"} }
                        , { field : "categoryStandardDepthName", title : "표준 카테고리<BR>전시 카테고리", width: "400px", attributes : {style : "text-align:center;"},
                            template : function(dataItem){
                                return dataItem.categoryStandardDepthName + "<BR>" + dataItem.categoryDepthName;
                            }
                          }
                        , { field : "warehouseName", title : "출고처 정보", width: "120px", attributes : {style : "text-align:center;"} }
                        , { field : "supplierName", title : "공급업체", width: "120px", attributes : {style : "text-align:center;"} }
                        , { field : "brandName", title : "표준 브랜드", width: "150px", attributes : {style : "text-align:center;"} }
                        , { field : "storageMethodTypeName", title : "보관방법", width: "80px", attributes : {style : "text-align:center;"}, hidden:viewModel.columnStorageMethodHidden }

                        , { field : "categoryStandardDepthName", title : "전시 카테고리", width: "400px", attributes : {style : "text-align:center;"}, hidden:viewModel.columnDisplayCategoryHidden,
                            template : function(dataItem){
                                return dataItem.categoryDepthName;
                            }
                          }
                        , { field : "name", title : "배송정책", width: "120px", attributes : {style : "text-align:center;"}, hidden:viewModel.columnNameHidden }
                        , { field : "areaShippingDeliveryYn", title : "도서산간/제주배송", width: "120px", attributes : {style : "text-align:center;"}, hidden:viewModel.columnAreaShippingDeliveryYnHidden,
                        		template : function(dataItem){
                        			return dataItem.areaShippingDeliveryYn == "Y" ? "가능" : "불가능";
                        		}
                  			}
                        , { field : "dpBrandName", title : "전시 브랜드", width: "150px", attributes : {style : "text-align:center;"}, hidden:viewModel.columnDpBrandNameHidden}
                        , { field : "standardPrice", title : "원가", width: "150px", attributes : {style : "text-align:center;"}, hidden:viewModel.columnStardardPriceHidden, format: "{0:n0}" }
                        , { field : "recommendedPrice", title : "정상가", width: "150px", attributes : {style : "text-align:center;"}, hidden:viewModel.columnRecommendedPriceHidden, format: "{0:n0}" }
                        , { field : "salePrice", title : "판매가", width: "150px", attributes : {style : "text-align:center;"}, hidden:viewModel.columnSalePriceHidden, format: "{0:n0}" }
                        , { field : "saleStatusCodeName", title : "판매상태", width: "150px", attributes : {style : "text-align:center;"}, hidden:viewModel.columnSaleStatusCodeNameHidden }
                        , { field : "goodsDisplayYn", title : "전시상태", width: "150px", attributes : {style : "text-align:center;"}, hidden:viewModel.columnGoodsDisplyYnHidden,
                        		template : function(dataItem){
                        			return dataItem.goodsDisplayYn == "Y" ? "전시" : "미전시";
                        		}
                      		}
                        , { field : "storageMethodTypeName", title : "보관방법", width: "80px", attributes : {style : "text-align:center;"}, hidden : true }
                        , { field : "warehouseId", title : "출고처 ID", hidden : true }
                        , { field : "supplierId",  title : "공급업체 ID", hidden : true }
                        , { field : "supplierCompanyId",  title : "공급업체 회사 ID", hidden : true }
                        , { field : "brandId",  title : "브랜드 ID", hidden : true }
                        , { field : "categoryStandardId",  title : "표준 카테고리 ID", hidden : true }
                        , { field : "categoryId",  title : "전시 카테고리 ID", hidden : true }
                        , { field : "goodsTypeCode",  title : "상품유형코드", hidden : true }
                        , { field : "saleTypeCode",  title : "판매유형코드", hidden : true }
                        , { field : "itemTypeCode",  title : "품목유형코드", hidden : true }
                        , { field : "packageUnitDisplayYn",  title : "표장용량 구성정보 노출여부", hidden : true }
                        , { field : "purchaseMemberYn",  title : "회원 구매여부", hidden : true }
                        , { field : "purchaseEmployeeYn",  title : "임직원 구매여부", hidden : true }
                        , { field : "purchaseNonmemberYn",  title : "비회원 구매여부", hidden : true }
                        , { field : "displayWebPcYn",  title : "WEB 전시여부", hidden : true }
                        , { field : "displayWebMobileYn",  title : "WEB 모바일 전시여부", hidden : true }
                        , { field : "displayAppYn",  title : "APP 전시여부", hidden : true }
                        , { field : "saleStartDate",  title : "판매 시작일", hidden : true }
                        , { field : "saleEndDate",  title : "판매 종료일", hidden : true }
                        , { field : "goodsDisplayYn",  title : "상품 전시여부", hidden : true }
                        , { field : "saleStatusCode",  title : "판매상태 공통코드", hidden : true }
                        , { field : "itemGroup",  title : "품목군", hidden : true }
                        , { field : "storageMethodTypeCode",  title : "보관방법 공통코드", hidden : true }
                        , { field : "placeOrderId",  title : "발주유형 ID", hidden : true }
                        , { field : "standardPrice",  title : "원가", hidden : true }
                        , { field : "recommendedPrice",  title : "정상가", hidden : true }
                        , { field : "salePrice",  title : "판매가", hidden : true }
                        , { field : "dpBrandId",  title : "전시브랜드ID", hidden : true }
                        , { field : "ilShippingTmplId",  title : "배송정책ID", hidden : true }
            ]

            , requestStart : function(e) { // 검색이 한번도 실행이 안되었을 경우는 실행하지 않는다.(검색실행전 목록수 변경등에서 조회 방지)
            	if (initializedSeachCond == false) e.preventDefault();
            }
        };

        goodsGrid = $("#goodsGrid").initializeKendoGrid( goodsGridOpt ).cKendoGrid();
//        goodsGrid.tbody.on("click", "input[name=itemGridChk]", viewModel.fnGridcheckboxClick);

        goodsGrid.bind("dataBound", function(e) {
        	//total count
            $('#countTotalSpan').text(kendo.toString(goodsGridDs._total, "n0"));

            // 전체 체크박스 초기화
            if( viewModel.selectMultiRowYn ) {
                $("#checkBoxAll").prop('checked', false);
            }

            if(goodsGridDs._total == 0) {
                $("#goodsGrid").find('#norecords-message').text("검색 결과가 없습니다.");
            }
        });


// [S]검색상품 추가
		goodsAllGridDs = fnGetPagingDataSource({
			url      : "/admin/comn/popup/getGoodsList",
		});

		goodsAllGridOpt = {
			dataSource : goodsAllGridDs
			, columns   : [
				{field : "goodsId", title : "상품코드", hidden : true}
			]
		};
        goodsAllGrid = $("#goodsAllGrid").initializeKendoGrid(goodsAllGridOpt).cKendoGrid();
        goodsAllGrid.bind("dataBound", function() {
        	if(goodsAllGridDs._data != undefined && goodsAllGridDs._data != null) {
        		parent.POP_PARAM = goodsAllGridDs._data;
    			parent.LAYER_POPUP_OBJECT.data("kendoWindow").close();
        	}
        });
// [E]검색상품 추가

        $("#goodsGrid").on("click", "tbody>tr>td", function () {
			//fnGridClick();
			var index = $(this).index();

			if(index == 3) {
				var aMap = goodsGrid.dataItem(goodsGrid.select());
				let option = {};

				switch (aMap.goodsTypeCode) {

					// 일반 상품
	                case "GOODS_TYPE.NORMAL" :

	                    option.url = "#/goodsMgm";
	                    option.menuId = 98;
	                    option.target = "goodsMgm";
	                    option.data = {                  // 일반상품 화면으로 전달할 Data
	                    		ilGoodsId : aMap.goodsId
	                    };
	                    fnGoNewPage(option);

	                    break;

	                case "GOODS_TYPE.GIFT" :
	                    option.url = "#/goodsAdditional";
	                    option.menuId = 865;
	                    option.target = "goodsAdditional";
	                    option.data = {                  // 일반상품 화면으로 전달할 Data
	                    		ilGoodsId : aMap.goodsId
	                    };
	                    fnGoNewPage(option);

	                    break;

                    case "GOODS_TYPE.GIFT_FOOD_MARKETING" :
                        option.url = "#/goodsAdditional";
                        option.menuId = 865;
                        option.target = "goodsAdditional";
                        option.data = {                  // 일반상품 화면으로 전달할 Data
                            ilGoodsId : aMap.goodsId
                        };
                        fnGoNewPage(option);

                        break;

	                case "GOODS_TYPE.ADDITIONAL" :

	                    option.url = "#/goodsAdditional";
	                    option.menuId = 865;
	                    option.target = "goodsAdditional";
	                    option.data = {                  // 일반상품 화면으로 전달할 Data
	                    		ilGoodsId : aMap.goodsId
	                    };
	                    fnGoNewPage(option);
	                    break;

	                case "GOODS_TYPE.DISPOSAL" :
	                    option.url = "#/goodsDisposal";
	                    option.menuId = 921;
	                    option.target = "goodsDisposal";
	                    option.data = {                  // 일반상품 화면으로 전달할 Data
	                    		ilGoodsId : aMap.goodsId
	                    };
	                    fnGoNewPage(option);

	                    break;
	                case "GOODS_TYPE.PACKAGE" :
	                	option.url = "#/goodsPackage";
	                    option.menuId = 768;
	                    option.target = "goodsPackage";
	                    option.data = {                  // 일반상품 화면으로 전달할 Data
	                    		ilGoodsId : aMap.goodsId
	                    };
	                    fnGoNewPage(option);

	                    break;

	                case "GOODS_TYPE.DAILY" :

	                    option.url = "#/goodsDaily";
	                    option.menuId = 902;
	                    option.target = "goodsDaily";
	                    option.data = {                  // 일반상품 화면으로 전달할 Data
	                    		ilGoodsId : aMap.goodsId
	                    };

	                    fnGoNewPage(option);
	                    break;
	                case "GOODS_TYPE.SHOP_ONLY" :

	                    option.url = "#/goodsShopOnly";
	                    option.target = "goodsShopOnly";
	                    option.data = {                  // 일반상품 화면으로 전달할 Data
	                    		ilGoodsId : aMap.goodsId
	                    };

	                    fnGoNewPage(option);
	                    break;

	                case "GOODS_TYPE.RENTAL" :

	                    option.url = "#/goodsRental";
	                    option.target = "goodsRental";
	                    option.data = {                  // 일반상품 화면으로 전달할 Data
	                    		ilGoodsId : aMap.goodsId
	                    };

	                    fnGoNewPage(option);
	                    break;

	                case "GOODS_TYPE.INCORPOREITY" :

	                    option.url = "#/goodsIncorporeal";
	                    option.target = "goodsIncorporeal";
	                    option.data = {                  // 일반상품 화면으로 전달할 Data
	                    		ilGoodsId : aMap.goodsId
	                    };

	                    fnGoNewPage(option);
	                    break;
	            };
			}


		});

     // 그리드 체크박스 클릭
        goodsGrid.element.on("click", "[name=itemGridChk]" , function(e){
            const $checkbox = $("input[name=itemGridChk]");
            const $checkboxAll = $('#checkBoxAll')
            const $this = $(this);
            const self = this;

            if( !viewModel.selectMultiRowYn ){
                let grid = $("#goodsGrid").data("kendoGrid");
                let row = $this.closest("tr");
                let rowIdx = $this.closest("tr").index();
                let rowData = goodsGrid.dataItem(row);
                $checkbox.each(function(index, item) {
                    const _self = $(this);
                    if( rowIdx != index ){
                        _self.prop("checked", false);
                    }
                });
            } else {
                // 여러개 선택 가능할 경우
                const isChecked = self.checked === true;
                const isCheckedAll = $checkbox.length === $checkbox.filter(function() {
                    const _self = this;
                    return _self.checked === true;
                }).length;

                if( !isChecked ) {
                    $checkboxAll.prop('checked', false);
                } else {
                    // 모든 체크박스가 선택되었을 경우
                    if( isCheckedAll ) {
                        $checkboxAll.prop('checked', true);
                    }
                }
            }
        });

        if( viewModel.selectMultiRowYn ){
            $("#checkBoxAll").on("click", function(index){
                if( $("#checkBoxAll").prop("checked") ){
                    $("input[name=itemGridChk]").prop("checked", true);
                }else{
                    $("input[name=itemGridChk]").prop("checked", false);
                }
            });
        }
    };
    //------------------------------- Grid End -------------------------------

    //---------------Initialize Option Box Start ------------------------------------------------

    // 옵션 초기화
    function fnInitOptionBox(){
        $("#kendoPopup").kendoWindow({
            visible: false,
            modal: true
        });

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
       		id: "searchCondition",
            data: [
            	{
            		CODE: "ALL",
            		NAME: "전체",
            	},
            	{
            		CODE: "GOODS_CODE",
            		NAME: "상품코드",
            	},
            	{
            		CODE: "ITEM_CODE",
            		NAME: "마스터 품목코드 (ERP)",
            	},
            	{
            		CODE: "ITEM_BARCODE",
            		NAME: "품목 바코드",
            	}
            ],
            valueField: "CODE",
            textField: "NAME",
        });

        // 공급업체
        fnKendoDropDownList({
            id : "supplierId",
            url : "/admin/comn/getDropDownSupplierList",
            tagId : "supplierId",
            textField :"supplierName",
            valueField : "supplierId",
            chkVal : "",
            blank : "전체",
            async : false
        });

        // 출고처그룹
        fnKendoDropDownList({
            id : "warehouseGroup",
            tagId : "warehouseGroup",
            url : "/admin/comn/getCodeList",
            params : { "stCommonCodeMasterCode" : "WAREHOUSE_GROUP", "useYn" : "Y" },
            blank : "출고처 그룹 전체",
            async : false
        }).bind("change", fnWareHouseGroupChange);

        // 공급업체 별 출고처
        fnKendoDropDownList({
            id : "warehouseId",
            url : "/admin/comn/getDropDownWarehouseGroupByWarehouseList",
            tagId : "warehouseId",
            textField :"warehouseName",
            valueField : "warehouseId",
            params : { "warehouseGroupCode" : "" },
            chkVal : "",
            blank : "전체",
            async : false,
//            cscdId     : "warehouseGroup",
//            cscdField  : "warehouseGroupCode"
        });

        // 브랜드
        fnKendoDropDownList({
            id : "brandId",
            url : "/admin/ur/brand/searchBrandList",
            tagId : "brandId",
            textField :"brandName",
            valueField : "urBrandId",

            chkVal: "",
            blank : "전체",
            async : false
        });

        fnKendoDropDownList({
            id : 'dpBrandId', 	// 전시 브랜드
            url : "/admin/ur/brand/searchDisplayBrandList",
            tagId : 'dpBrandId',
            autoBind : true,
            valueField : 'dpBrandId',
            textField : 'dpBrandName',
            chkVal : '',
            style : {},
            blank : '선택',
            params      : {"useYn" :"Y"}
        });

//        if(pageParam.goodsType.indexOf(',') == -1){
//        	// 상품유형
//	        fnKendoDropDownList({
//	            id : "goodsType",
//	            url : "/admin/comn/getCodeList",
//	            tagId : "goodsType",
//	            params : { "stCommonCodeMasterCode" : "GOODS_TYPE", "useYn" : "Y" },
//	            chkVal: "",
//	            blank : "전체",
//	            async : false
//	        });
//        }

        // 배송불가지역
//        fnKendoDropDownList({
//            id : "undeliverableAreaType",
//            url : "/admin/comn/getCodeList",
//            tagId : "undeliverableAreaType",
//            params : { "stCommonCodeMasterCode" : "UNDELIVERABLE_AREA_TP", "useYn" : "Y" },
//            chkVal: "",
//            blank : "전체",
//            textField :"NAME",
//            valueField : "CODE"
//        });

        // 카테고리 구분
        fnKendoDropDownList({
            id : "categoryType",
            data : [ {"CODE" : "CATEGORY_STANDARD", "NAME" : "표준카테고리"},
                     {"CODE" : "CATEGORY_PULMUONE", "NAME" : "전시카테고리"},
                     {"CODE" : "CATEGORY_ORGA", "NAME" : "몰인몰(올가)"},
                     {"CODE" : "CATEGORY_BABYMEAL", "NAME" : "몰인몰(베이비밀)"},
                     {"CODE" : "CATEGORY_EATSLIM", "NAME" : "몰인몰(잇슬림)"}
                   ],
            tagId : "categoryType"
        });

        // 표준카테고리 대분류
        fnKendoDropDownList({
            id : "categoryStandardDepth1",
            url : "/admin/comn/getDropDownCategoryStandardList",
            tagId : "categoryStandardDepth1",
            params : { "depth" : "1" },
            textField : "categoryName",
            valueField : "categoryId",
            chkVal: "",
            blank : "대분류",
            async : false
        });

        // 표준카테고리 중분류
        fnKendoDropDownList({
            id : "categoryStandardDepth2",
            url : "/admin/comn/getDropDownCategoryStandardList",
            tagId : "categoryStandardDepth2",
            textField : "categoryName",
            valueField : "categoryId",
            chkVal: "",
            blank : "중분류",
            async : false,
            cscdId : "categoryStandardDepth1",
            cscdField : "categoryId"
        });

        // 표준카테고리 소분류
        fnKendoDropDownList({
            id : "categoryStandardDepth3",
            url : "/admin/comn/getDropDownCategoryStandardList",
            tagId : "categoryStandardDepth3",
            textField : "categoryName",
            valueField : "categoryId",
            chkVal: "",
            blank : "소분류",
            async : false,
            cscdId : "categoryStandardDepth2",
            cscdField : "categoryId"
        });

        // 표준카테고리 세분류
        fnKendoDropDownList({
            id : "categoryStandardDepth4",
            url : "/admin/comn/getDropDownCategoryStandardList",
            tagId : "categoryStandardDepth4",
            textField : "categoryName",
            valueField : "categoryId",
            chkVal: "",
            blank : "세분류",
            async : false,
            cscdId : "categoryStandardDepth3",
            cscdField : "categoryId"
        });

        // 전시카테고리 대분류
        fnKendoDropDownList({
            id : "categoryDepth1",
            url : "/admin/comn/getDropDownCategoryList",
            tagId : "categoryDepth1",
            params : { "depth" : "1", "mallDiv" : "MALL_DIV.PULMUONE" },
            textField : "categoryName",
            valueField : "categoryId",
            chkVal: "",
            blank : "대분류",
            async : false
        });

        // 전시카테고리 중분류
        fnKendoDropDownList({
            id : "categoryDepth2",
            url : "/admin/comn/getDropDownCategoryList",
            tagId : "categoryDepth2",
            textField : "categoryName",
            valueField : "categoryId",
            chkVal: "",
            blank : "중분류",
            async : false,
            cscdId : "categoryDepth1",
            cscdField : "categoryId"
        });

        // 전시카테고리 소분류
        fnKendoDropDownList({
            id : "categoryDepth3",
            url : "/admin/comn/getDropDownCategoryList",
            tagId : "categoryDepth3",
            textField : "categoryName",
            valueField : "categoryId",
            chkVal: "",
            blank : "소분류",
            async : false,
            cscdId : "categoryDepth2",
            cscdField : "categoryId"
        });

        // 전시카테고리 세분류
        fnKendoDropDownList({
            id : "categoryDepth4",
            url : "/admin/comn/getDropDownCategoryList",
            tagId : "categoryDepth4",
            textField : "categoryName",
            valueField : "categoryId",
            chkVal: "",
            blank : "세분류",
            async : false,
            cscdId : "categoryDepth3",
            cscdField : "categoryId"
        });

        // 검색기간
        fnKendoDropDownList({
            id : "dateSearchType",
            data : [ {"CODE":"CREATE_DATE"   ,"NAME":"등록일"},
                     {"CODE":"MODIFY_DATE"   ,"NAME":"최근수정일"}
            ],
            textField :"NAME",
            valueField : "CODE"
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

        // 전시여부
        fnTagMkRadio({
            id : "displayYn",
            tagId : "displayYn",
            data  : [ { "CODE" : ""   , "NAME" : "전체" },
                      { "CODE" : "Y"  , "NAME" : "전시" },
                      { "CODE" : "N"  , "NAME" : "미전시" }
                    ]
        });

        $("input[name=displayYn]").attr("data-bind", "checked: searchInfo.displayYn");

        // 상품유형
        fnTagMkChkBox({
            id : "goodsType",
            tagId : "goodsType",
            url : "/admin/comn/getCodeList",
            params : {"stCommonCodeMasterCode" : "GOODS_TYPE", "useYn" : "Y", "attr3":"POP"},
            beforeData : [{ "CODE" : "ALL", "NAME" : "전체" }],
            async : false
        });

        // 판매상태
        fnTagMkChkBox({
            id : "saleStatus",
            tagId : "saleStatus",
            url : "/admin/comn/getCodeList",
            params : {"stCommonCodeMasterCode" : "SALE_STATUS", "useYn" : "Y", "attr2" : "APPROVED"},
            beforeData : [{ "CODE" : "ALL", "NAME" : "전체" }],
            async : false
        });

        // 판매유형
        fnTagMkChkBox({
            id : "saleType",
            tagId : "saleType",
            url : "/admin/comn/getCodeList",
            params : {"stCommonCodeMasterCode" : "SALE_TYPE", "useYn" : "Y"},
            beforeData : [{ "CODE" : "ALL", "NAME" : "전체" }],
            async : false
        });
//    	$("input[name=saleType]:eq(0)").prop("checked", true).trigger("change");

        // 검색기간 초기화 버튼 클릭
        /*
        $('[data-id="fnDateBtnC"]').on("click", function(){
            $('[data-id="fnDateBtn3"]').mousedown();
        });
        */
    };

    //---------------Initialize Option Box End ------------------------------------------------


    //-------------------------------  Common Function start -------------------------------

    // viewModel 초기화
    function fnViewModelInit(){
        viewModel = new kendo.data.ObservableObject({
            searchInfo : { // 조회조건
                searchCondition : "", // 검색조건
                findKeyword : "", // 검색어 (숫자,영문대소문자)
                goodsName : "", // 상품명 (한글,영문대소문자, 숫자)
                supplierId : "", // 공급업체
                warehouseGrpCd : "", // 출고처 그룹
                warehouseId : "", // 출고처
                brandId : "", // 표준 브랜드
                dpBrandId : "", // 전시 브랜드
                goodsType : "", // 상품유형
                displayYn : "", // 전시유형
                undeliverableAreaType : "", // 배송불가지역
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
                bundleYn : "", // 합배송여부
                ilShippingTmplId : "",

            },
            publicStorageUrl : null, // 저장소 URL
            selectMultiRowYn : true, // 그리드 선택 Multi, Single
            findKeywordDisabled : false, // 검색어 Disabled
            supplierIdDisabled : false, // 공급업체 Disabled
            warehouseGrpDisabled : false, // 출고처 그룹 Disabled
            warehouseIdDisabled : false, // 출고처 Disabled
            goodsTypeDisabled : false, // 상품유형 Disabled
            isGoodsTypes : true,		// 상품 유형 라인 Visible
            isSaleTypes  : false,		// 판매 유형 라인 Visible
            goodsTypeMultiName : "", //복수 상품유형명
            undeliverableAreaTypeDisabled : false, // 배송불가지역 Disabled
            categoryStandardVisible : true, // 표준카테고리 Visible
            categoryVisible : false, // 전시카테고리 Visible
            isSearchChoiceBtnVisible : false,
            columnStorageMethodHidden : false,
            columnDisplayCategoryHidden : true,
            columnNameHidden : true,
            columnAreaShippingDeliveryYnHidden : true,
            columnDpBrandNameHidden : true,
            columnStardardPriceHidden : true,
            columnRecommendedPriceHidden : true,
            columnSalePriceHidden : true,
            columnSaleStatusCodeNameHidden : true,
            columnGoodsDisplyYnHidden : true,

            fnSearch : function(e){ // 조회
                e.preventDefault();

                let data = $("#searchForm").formSerialize(true);

				if($("input[name=selectConditionType]:checked").val() == "singleSection") {
					if($.trim($('#findKeyword').val()).length >= 0 && $.trim($('#findKeyword').val()).length < 2 ) {
						fnKendoMessage({
							message : '단일조건 코드 검색은 2글자 이상 입력해야 합니다.',
							ok : function() {
								return;
							}
						});
						return;
					}
					else {
						data['findKeyword'] = $.trim($('#findKeyword').val());
					}
				} else {
					if( data.dateSearchStart == "" && data.dateSearchEnd != "" ){
						fnKendoMessage({ message : "시작일을 선택해주세요." });
						return;
					}

					if( data.dateSearchStart != "" && data.dateSearchEnd == "" ){
						fnKendoMessage({ message : "종료일을 선택해주세요." });
						return;
					}

					if($('#findKeyword').val() != "") {
        				$("#findKeyword").val("");
        			}
				}

                // 기획전 조회 조건 & 체험단이벤트 조회조건 추가
                if( pageParam.goodsCallType != undefined && ( pageParam.goodsCallType == 'exhibitNormal' || pageParam.goodsCallType == 'experienceEvent') ) {
                    data.selectConditionType = $("input[name=selectConditionType]:checked").val();
                    data.goodsCallType = pageParam.goodsCallType;
                }

                if(pageParam.goodsCallType != undefined && pageParam.goodsCallType == 'giftGoods') {
                	var giftChk = false;
                    $("input:checkbox[name='goodsType']").each(function(){
                    	if($(this).is(":checked") == true && ($(this).val() == "GOODS_TYPE.GIFT" || $(this).val() == "GOODS_TYPE.GIFT_FOOD_MARKETING")) {
                    		giftChk = true;
                    	}
                    });

                    if(!giftChk) {
                    	data.goodsType = "ALL";
                    }
                }

                if(pageParam.isGoodsTypes != undefined && pageParam.isGoodsTypes == false) {
                	if (data.goodsType == undefined || data.goodsType == null) data.goodsType = "";
                	if (data.displayYn == undefined || data.displayYn == null) data.displayYn = "";
                }

                if( pageParam.isSaleTypes == undefined || pageParam.isSaleTypes == false) {
                	data.saleType = "";
                }

                if(pageParam.pgId != undefined && pageParam.pgId == "goodsAllModify") {
                	data.bundleYn = viewModel.searchInfo.get("bundleYn");
                	data.ilShippingTmplId = viewModel.searchInfo.get("ilShippingTmplId");
                }

                if ($("input[name=selectConditionType]:checked").val() == "singleSection") {
                	data.goodsType = ''; // 단일조건 검색 시 상품유형 초기화
                    $("input[name=goodsType]").each(function(idx){
                        if($(this).val() == 'ALL') return;
                    	if (data.goodsType == '') {
                    		data.goodsType = $(this).val();
                    	}
                    	else {
                    		data.goodsType = data.goodsType + '∀' + $(this).val();
                    	}
                    });

					if (pageParam.goodsCallType != undefined && pageParam.goodsCallType == 'regularDeliveryReqDetail') { // 정기배송 주문 신청 내역
						// 정기배송 주문 신청 내역의 경우 설정되어 있는 판매유형값만 조회 - data.saleType를 초기화하는 로직을 위에 추가하면 여기에서 다시 설정해줘야된다.
	                    data.goodsCallType = pageParam.goodsCallType;
					}
                }

                let searchData = fnSearchData(data);
                prevSearchData = searchData;
                let query = { page : 1,
                              pageSize : PAGE_SIZE,
                              filterLength : searchData.length,
                              filter : { filters : searchData }
                };
                initializedSeachCond = true;
                goodsGridDs.query(query);

            },
            fnClear : function(e){ // 초기화
                e.preventDefault();
                fnDefaultSet();
            },
            fnChoiceAdd : function(e){ // 선택추가
                e.preventDefault();
                let params = [];

                if( $("input[name=itemGridChk]:checked").length == 0 ){
                    fnKendoMessage({ message : "선택된 상품이 없습니다." });
                    return;
                }

                let grid = $("#goodsGrid").data("kendoGrid");
                let parentGoodsIdAry = pageParam.goodsIds;
                let alertMessage = "";
                var i = 0;
                parent.gblAlertMessage = "";
                $("input[name=itemGridChk]").each(function(index, item) {
                	if($(this).is(":checked") == true) {
                	    if (pageParam.chkGoodsOverlapYn == "Y"){

                	        let row = $(this).closest("tr");
                            let dataItem = goodsGrid.dataItem(row);
                            if (alertMessage == "") {
                                $.each(parentGoodsIdAry, function (index2, item2) {
                                    if ($.trim(item2) == $.trim(dataItem.goodsId)) {
                                        //alertMessage += "중복된 상품 ["+ item2 +"] " + dataItem.goodsName + "<br/>"
                                    	if(pageParam.goodsCallType == 'packageGoods') {	//상품 추가 할때만 해당 경고 메세지가 뜨도록 변경
                                    		alertMessage = "이미 등록된 상품이 존재합니다, 해당 상품을 제외한 상품등록이 완료되었습니다.";
                                    	}
                                    }
                                });
                            }

                            if(parentPgId == "goodsAllModify") {
                                for(var k = 0; k < parentGoodsIdAry.length; k++) {
                                    var goodsId = parentGoodsIdAry[k];
                                }
                                params[i] = dataItem;
                            }else {
                                params[i] = dataItem;
                            }
                        } else {
                            let row = $(this).closest("tr");
                            let dataItem = goodsGrid.dataItem(row);

                            if(parentPgId == "goodsAllModify") {
                                for(var k = 0; k < parentGoodsIdAry.length; k++) {
                                    var goodsId = parentGoodsIdAry[k];
                                }
                                params[i] = dataItem;
                            }else {
                                params[i] = dataItem;
                            }
                	    }

                		i++;

                	}

                });

                parent.gblAlertMessage = alertMessage;
                parent.POP_PARAM = params;

                if(pageParam.pgId != undefined && pageParam.pgId == "goodsAllModify") {
                	fnKendoMessage({
                        type : "confirm",
                        message : "선택 상품 추가 완료 (중복상품 제외), 등록을 종료 하시겠습니까?",
                        ok : function() {
                        	parent.LAYER_POPUP_OBJECT.data("kendoWindow").close();
                        },
                        cancel : function() {
                        	parent.LAYER_POPUP_OBJECT.data("kendoWindow").refresh();
                        }
                    });
                }else {
                	parent.LAYER_POPUP_OBJECT.data("kendoWindow").close();
                }
            },
            fnSearchChoiceAdd : function(e) {
                e.preventDefault();
                let params = [];

                var dataSource = goodsGrid.dataSource;

                if (dataSource.total() == 0) {
                    fnKendoMessage({message: "조회 후 추가가능 합니다."});
                    return;
                }
                if (pageParam.orderCreateYn == "Y") {
                    let grid = $("#goodsGrid").data("kendoGrid");
                    let parentGoodsIdAry = pageParam.goodsIds;
                    let alertMessage = "";
                    var i = 0;
                    parent.gblAlertMessage = "";
                    $("input[name=itemGridChk]").each(function (index, item) {

                        if (pageParam.chkGoodsOverlapYn == "Y") {

                            let row = $(this).closest("tr");
                            let dataItem = goodsGrid.dataItem(row);
                            if (alertMessage == "") {
                                $.each(parentGoodsIdAry, function (index2, item2) {
                                    if ($.trim(item2) == $.trim(dataItem.goodsId)) {
                                        //alertMessage += "중복된 상품 ["+ item2 +"] " + dataItem.goodsName + "<br/>"
                                        if (pageParam.goodsCallType == 'packageGoods') {	//상품 추가 할때만 해당 경고 메세지가 뜨도록 변경
                                            alertMessage = "이미 등록된 상품이 존재합니다, 해당 상품을 제외한 상품등록이 완료되었습니다.";
                                        }
                                    }
                                });
                            }

                            if (parentPgId == "goodsAllModify") {
                                for (var k = 0; k < parentGoodsIdAry.length; k++) {
                                    var goodsId = parentGoodsIdAry[k];
                                }
                                params[i] = dataItem;
                            } else {
                                params[i] = dataItem;
                            }
                        } else {
                            let row = $(this).closest("tr");
                            let dataItem = goodsGrid.dataItem(row);

                            if (parentPgId == "goodsAllModify") {
                                for (var k = 0; k < parentGoodsIdAry.length; k++) {
                                    var goodsId = parentGoodsIdAry[k];
                                }
                                params[i] = dataItem;
                            } else {
                                params[i] = dataItem;
                            }
                        }

                        i++;


                    });

                    parent.gblAlertMessage = alertMessage;
                    parent.POP_PARAM = params;

                    if (pageParam.pgId != undefined && pageParam.pgId == "goodsAllModify") {
                        fnKendoMessage({
                            type: "confirm",
                            message: "선택 상품 추가 완료 (중복상품 제외), 등록을 종료 하시겠습니까?",
                            ok: function () {
                                parent.LAYER_POPUP_OBJECT.data("kendoWindow").close();
                            },
                            cancel: function () {
                                parent.LAYER_POPUP_OBJECT.data("kendoWindow").refresh();
                            }
                        });
                    } else {
                        parent.LAYER_POPUP_OBJECT.data("kendoWindow").close();
                    }
                } else {

                    var dataSource = goodsGrid.dataSource;

                    if (dataSource.total() == 0) {
                        fnKendoMessage({message: "조회 후 추가가능 합니다."});
                        return;
                    }

                    if (dataSource.total() > 0) {
                        let query = {
                            filterLength: prevSearchData.length,
                            filter: {filters: prevSearchData}
                        };
                        goodsAllGridDs.query(query);

                        parent.POP_PARAM = query;
                    }
                }
            },
            fnClose : function( params ){ // 팝업 닫기
            	parent.LAYER_POPUP_OBJECT.data("kendoWindow").close();
            },

            fnGridcheckboxClick : function(e){ // Grid 체크박스 클릭

                if( !viewModel.selectMultiRowYn ){

                    let grid = $("#goodsGrid").data("kendoGrid");
                    let row = $(e.target).closest("tr");
                    let rowIdx = $("tr", grid.tbody).index(row);

                    $("input[name=itemGridChk]").each(function(index, item) {
                        if( rowIdx != index ){
                            $(this).prop("checked", false);
                        }
                    });
                }
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
            }
        });

        viewModel.publicStorageUrl = fnGetPublicStorageUrl();

        kendo.bind($("#searchForm"), viewModel);
    };

    // 파라미터 셋팅
    function fnSetParamValue(){

        if( pageParam != undefined ){
            // 상품유형 : 추가
            if( pageParam.goodsType != undefined ){
                viewModel.searchInfo.set("goodsType", pageParam.goodsType);

                var goodsTypeAry = pageParam.goodsType.split(',');

                $("input[name=goodsType]:gt(0)").each(function(idx){
                	for(var i = 0; i < goodsTypeAry.length; i++) {
                		var goodsTypeVal = goodsTypeAry[i].trim();
                		if(goodsTypeVal == $(this).val()) {
                			$(this).prop("checked", true);
                		}
                	}
                });

//                if(pageParam.goodsType.indexOf(',') != -1){
//                	viewModel.set("goodsTypeMultiName", "일반상품, 폐기임박상품");
//                }

                viewModel.searchInfo.set("supplierId", pageParam.supplierId);
                viewModel.searchInfo.set("warehouseId", pageParam.warehouseId);
                viewModel.searchInfo.set("warehouseGrpCd", pageParam.warehouseGrpCd);
                viewModel.searchInfo.set("undeliverableAreaType", pageParam.undeliverableAreaType);

                setTimeout(() => {
                    viewModel.set("goodsTypeDisabled", true);
                    viewModel.set("supplierIdDisabled", true);

                    //출고처그룹 또는 출고처 값이 있을때 비활성화처리
                    if (pageParam.warehouseId != undefined || pageParam.warehouseGrpCd != undefined) {
                        viewModel.set("warehouseIdDisabled", true);
                        viewModel.set("warehouseGrpDisabled", true);
                    }

                    viewModel.set("undeliverableAreaTypeDisabled", true);
                }, 100);
            }

            // 20210106 공급업체 , 출고처 disalbed 파라미터 있을경우 셋팅.
            if( pageParam.supplierIdDisabled != undefined ){
            	setTimeout(() => {
            		viewModel.set("supplierIdDisabled", pageParam.supplierIdDisabled);
            	}, 100);
            }
            if( pageParam.warehouseIdDisabled != undefined ){
            	setTimeout(() => {
            		viewModel.set("warehouseIdDisabled", pageParam.warehouseIdDisabled);
            	}, 100);
            }
            if( pageParam.warehouseGrpDisabled != undefined ){
            	setTimeout(() => {
            		viewModel.set("warehouseGrpDisabled", pageParam.warehouseGrpDisabled);
            	}, 100);
            }

            // 보관방법 컬럼 Hidden 여부
            if( pageParam.columnStorageMethodHidden != undefined ){
            	viewModel.set("columnStorageMethodHidden", pageParam.columnStorageMethodHidden);
            }
            // 배송정책 컬럼 Hidden 여부
            if( pageParam.columnNameHidden != undefined ){
            	viewModel.set("columnNameHidden", pageParam.columnNameHidden);
            }
            // 전시브랜드 컬럼 Hidden 여부
            if( pageParam.columnDpBrandNameHidden != undefined ){
            	viewModel.set("columnDpBrandNameHidden", pageParam.columnDpBrandNameHidden);
            }
            // 원가 컬럼 Hidden 여부
            if( pageParam.columnStardardPriceHidden != undefined ){
            	viewModel.set("columnStardardPriceHidden", pageParam.columnStardardPriceHidden);
            }
            // 정상가 컬럼 Hidden 여부
            if( pageParam.columnRecommendedPriceHidden != undefined ){
            	viewModel.set("columnRecommendedPriceHidden", pageParam.columnRecommendedPriceHidden);
            }
            // 판매가 컬럼 Hidden 여부
            if( pageParam.columnSalePriceHidden != undefined ){
            	viewModel.set("columnSalePriceHidden", pageParam.columnSalePriceHidden);
            }
            // 판매상태 컬럼 Hidden 여부
            if( pageParam.columnSaleStatusCodeNameHidden != undefined ){
            	viewModel.set("columnSaleStatusCodeNameHidden", pageParam.columnSaleStatusCodeNameHidden);
            }
            // 전시여부 컬럼 Hidden 여부
            if( pageParam.columnGoodsDisplyYnHidden != undefined ){
            	viewModel.set("columnGoodsDisplyYnHidden", pageParam.columnGoodsDisplyYnHidden);
            }
            // 도서산간 컬럼 Hidden 여부
            if( pageParam.columnAreaShippingDeliveryYnHidden != undefined ){
            	viewModel.set("columnAreaShippingDeliveryYnHidden", pageParam.columnAreaShippingDeliveryYnHidden);
            }


            // 상품 유형 다시 셋팅 파라미터 있을 경우 - 상품일괄수정에서 상품 추가 할때 사용
            if( pageParam.goodsTypeReDraw != undefined && pageParam.goodsTypeReDraw == 'Y') {
            	$("#goodsType").empty();

            	fnTagMkChkBox({
            		id : "goodsType",
            		tagId : "goodsType",
            		url : "/admin/comn/getCodeList",
            		params : {"stCommonCodeMasterCode" : "GOODS_TYPE", "useYn" : "Y", "attr2":"POP2"},
            		beforeData : [{ "CODE" : "ALL", "NAME" : "전체" }],
            		async : false
            	});

            	viewModel.searchInfo.set("goodsType", pageParam.goodsType);

                var goodsTypeAry = pageParam.goodsType.split(',');

                $("input[name=goodsType]:gt(0)").each(function(idx){
                	for(var i = 0; i < goodsTypeAry.length; i++) {
                		var goodsTypeVal = goodsTypeAry[i].trim();
                		if(goodsTypeVal == $(this).val()) {
                			$(this).prop("checked", true);
                		}
                	}
                });
            }

            if( pageParam.goodsCallType != undefined && (pageParam.goodsCallType == 'packageGoods' || pageParam.goodsCallType == 'basePackageGoods')) {
            	$("#goodsType").empty();

            	fnTagMkChkBox({
            		id : "goodsType",
            		tagId : "goodsType",
            		data : [
            			{ "CODE" : "ALL", "NAME" : "전체" },
            			{"CODE" : "GOODS_TYPE.NORMAL", "NAME" : "일반"},
                        {"CODE" : "GOODS_TYPE.DISPOSAL", "NAME" : "폐기임박"}
                      ],
            	});

            	viewModel.searchInfo.set("goodsType", pageParam.goodsType);

                var goodsTypeAry = pageParam.goodsType.split(',');

                $("input[name=goodsType]:gt(0)").each(function(idx){
                	for(var i = 0; i < goodsTypeAry.length; i++) {
                		var goodsTypeVal = goodsTypeAry[i].trim();
                		if(goodsTypeVal == $(this).val()) {
                			$(this).prop("checked", true);
                		}
                	}
                });
                $("input[name=goodsType]:eq(0)").prop("checked", true).trigger("change");

                $("#saleStatus").empty();

                fnTagMkChkBox({
                    id : "saleStatus",
                    tagId : "saleStatus",
                    data : [
            			{ "CODE" : "ALL", "NAME" : "전체" },
//            			{"CODE" : "SALE_STATUS.SAVE", "NAME" : "저장"},
                        {"CODE" : "SALE_STATUS.WAIT", "NAME" : "판매대기"},
                        {"CODE" : "SALE_STATUS.ON_SALE", "NAME" : "판매중"},
                        {"CODE" : "SALE_STATUS.OUT_OF_STOCK_BY_SYSTEM", "NAME" : "품절(시스템)"},
                        {"CODE" : "SALE_STATUS.OUT_OF_STOCK_BY_MANAGER", "NAME" : "품절(관리자)"},
                        {"CODE" : "SALE_STATUS.STOP_SALE", "NAME" : "판매중지"}
                      ],
                });

            }

            if( pageParam.goodsCallType != undefined && pageParam.goodsCallType == 'exhibitNormal') {
            	$("#goodsType").empty();

            	fnTagMkChkBox({
                    id : "goodsType",
                    tagId : "goodsType",
                    data : [
                        { "CODE" : "ALL", "NAME" : "전체" },
                        {"CODE" : "GOODS_TYPE.NORMAL", "NAME" : "일반"},
                        {"CODE" : "GOODS_TYPE.DISPOSAL", "NAME" : "폐기임박"},
                        {"CODE" : "GOODS_TYPE.PACKAGE", "NAME" : "묶음"},
                        {"CODE" : "GOODS_TYPE.DAILY", "NAME" : "일일"},
                        {"CODE" : "GOODS_TYPE.RENTAL", "NAME" : "렌탈"},
                        {"CODE" : "GOODS_TYPE.INCORPOREITY", "NAME" : "무형"}
                      ],
                });
                $("input[name=goodsType]").eq(0).prop("checked", true).trigger("change");
            }


            if( pageParam.goodsCallType != undefined && pageParam.goodsCallType == 'giftGoods') {
            	$("#goodsType").empty();

            	fnTagMkChkBox({
            		id : "goodsType",
            		tagId : "goodsType",
            		data : [
           // 			{ "CODE" : "ALL", "NAME" : "전체" },              // HGRM-4306 : 증정품 팝업에서 전체 선택 제외.
            			{"CODE" : "GOODS_TYPE.GIFT", "NAME" : "증정"},
                        {"CODE" : "GOODS_TYPE.GIFT_FOOD_MARKETING", "NAME" : "식품마케팅증정"}
                      ],
            	});
                $("input[name=goodsType]").prop("checked", true).trigger("change");

            	// viewModel.searchInfo.set("goodsType", pageParam.goodsType);
                //
                // var goodsTypeAry = pageParam.goodsType.split(',');
                //
                // $("input[name=goodsType]").each(function(idx){
                // 	for(var i = 0; i < goodsTypeAry.length; i++) {
                // 		var goodsTypeVal = goodsTypeAry[i];
                // 		if(goodsTypeVal == $(this).val()) {
                // 			$(this).prop("checked", true);
                // 		}
                // 	}
                // });

                $('input[name=goodsType]').prop("disabled", "disabled").trigger("change");

                $("#saleStatus").empty();

                fnTagMkChkBox({
                    id : "saleStatus",
                    tagId : "saleStatus",
                    data : [
            			{ "CODE" : "ALL", "NAME" : "전체" },
//            			{"CODE" : "SALE_STATUS.SAVE", "NAME" : "저장"},
                        {"CODE" : "SALE_STATUS.WAIT", "NAME" : "판매대기"},
                        {"CODE" : "SALE_STATUS.ON_SALE", "NAME" : "판매중"},
                        {"CODE" : "SALE_STATUS.OUT_OF_STOCK_BY_SYSTEM", "NAME" : "품절(시스템)"},
                        {"CODE" : "SALE_STATUS.OUT_OF_STOCK_BY_MANAGER", "NAME" : "품절(관리자)"},
                        {"CODE" : "SALE_STATUS.STOP_SALE", "NAME" : "판매중지"}
                      ],
                });

            }

            if( pageParam.goodsCallType != undefined && pageParam.goodsCallType == 'addGoods') {
            	$("#goodsType").empty();

            	fnTagMkChkBox({
            		id : "goodsType",
            		tagId : "goodsType",
            		data : [
           // 			{ "CODE" : "ALL", "NAME" : "전체" },              // HGRM-4306 : 증정품 팝업에서 전체 선택 제외.
            			{"CODE" : "GOODS_TYPE.ADDITIONAL", "NAME" : "추가"}
                      ],
            	});
            	viewModel.searchInfo.set("goodsType", pageParam.goodsType);

                var goodsTypeAry = pageParam.goodsType.split(',');

                $("input[name=goodsType]:gt(0)").each(function(idx){
                	for(var i = 0; i < goodsTypeAry.length; i++) {
                		var goodsTypeVal = goodsTypeAry[i].trim();
                		if(goodsTypeVal == $(this).val()) {
                			$(this).prop("checked", true);
                		}
                	}
                });
                $("input[name=goodsType]:eq(0)").prop("checked", true).trigger("change");
                $('input[name=goodsType]').prop("disabled", "disabled").trigger("change");

                $("#saleStatus").empty();

                fnTagMkChkBox({
                    id : "saleStatus",
                    tagId : "saleStatus",
                    data : [
            			{ "CODE" : "ALL", "NAME" : "전체" },
//            			{"CODE" : "SALE_STATUS.SAVE", "NAME" : "저장"},
                        {"CODE" : "SALE_STATUS.WAIT", "NAME" : "판매대기"},
                        {"CODE" : "SALE_STATUS.ON_SALE", "NAME" : "판매중"},
                        {"CODE" : "SALE_STATUS.OUT_OF_STOCK_BY_SYSTEM", "NAME" : "품절(시스템)"},
                        {"CODE" : "SALE_STATUS.OUT_OF_STOCK_BY_MANAGER", "NAME" : "품절(관리자)"},
                        {"CODE" : "SALE_STATUS.STOP_SALE", "NAME" : "판매중지"}
                      ],
                });

            }

            if( pageParam.goodsCallType != undefined && (pageParam.goodsCallType == 'exhibitSelectTargetGoods' || pageParam.goodsCallType == "exhibitSelectGoods" )) {
            	viewModel.searchInfo.set("bundleYn", "Y");

            	$("#goodsType").empty();

            	fnTagMkChkBox({
                    id : "goodsType",
                    tagId : "goodsType",
                    data : [
                        { "CODE" : "ALL", "NAME" : "전체" },
                        {"CODE" : "GOODS_TYPE.NORMAL", "NAME" : "일반"},
                        {"CODE" : "GOODS_TYPE.DISPOSAL", "NAME" : "폐기임박"}
                      ],
                });
                $("input[name=goodsType]").eq(0).prop("checked", true).trigger("change");

            }

            if( pageParam.goodsCallType != undefined && (pageParam.goodsCallType == 'experienceEvent')) {
            	$("#goodsType").empty();

            	fnTagMkChkBox({
                    id : "goodsType",
                    tagId : "goodsType",
                    data : [
                        { "CODE" : "ALL", "NAME" : "전체" },
                        {"CODE" : "GOODS_TYPE.NORMAL", "NAME" : "일반"},
                        {"CODE" : "GOODS_TYPE.DISPOSAL", "NAME" : "폐기임박"}
                      ],
                });
                $("input[name=goodsType]").eq(0).prop("checked", true).trigger("change");

            }

            if( pageParam.goodsCallType != undefined && pageParam.goodsCallType == 'exhibitAddGoods') {
            	viewModel.searchInfo.set("bundleYn", "Y");

            	$("#goodsType").empty();

            	fnTagMkChkBox({
                    id : "goodsType",
                    tagId : "goodsType",
                    data : [
                    	{"CODE" : "GOODS_TYPE.ADDITIONAL", "NAME" : "추가"}
                      ],
                });

            	viewModel.searchInfo.set("goodsType", pageParam.goodsType);

                var goodsTypeAry = pageParam.goodsType.split(',');

                $("input[name=goodsType]:gt(0)").each(function(idx){
                	for(var i = 0; i < goodsTypeAry.length; i++) {
                		var goodsTypeVal = goodsTypeAry[i].trim();
                		if(goodsTypeVal == $(this).val()) {
                			$(this).prop("checked", true);
                		}
                	}
                });
                $("input[name=goodsType]:eq(0)").prop("checked", true).trigger("change");
                $('input[name=goodsType]').prop("disabled", "disabled").trigger("change");


            }

            if( pageParam.goodsCallType != undefined && pageParam.goodsCallType == 'goodsRecommend') {
            	$("#goodsType").empty();

            	fnTagMkChkBox({
            		id : "goodsType",
            		tagId : "goodsType",
            		data : [
            			{ "CODE" : "ALL", "NAME" : "전체" },              // HGRM-4306 : 증정품 팝업에서 전체 선택 제외.
            			{"CODE" : "GOODS_TYPE.NORMAL", "NAME" : "일반"},
            			{"CODE" : "GOODS_TYPE.DISPOSAL", "NAME" : "폐기임박"},
            			{"CODE" : "GOODS_TYPE.PACKAGE", "NAME" : "묶음"},
            			{"CODE" : "GOODS_TYPE.SHOP_ONLY", "NAME" : "매장전용"},
            			{"CODE" : "GOODS_TYPE.RENTAL", "NAME" : "렌탈"},
            			{"CODE" : "GOODS_TYPE.INCORPOREITY", "NAME" : "무형"},
                      ],
            	});
            	viewModel.searchInfo.set("goodsType", pageParam.goodsType);

                var goodsTypeAry = pageParam.goodsType.split(',');

                $("input[name=goodsType]:gt(0)").each(function(idx){
                	for(var i = 0; i < goodsTypeAry.length; i++) {
                		var goodsTypeVal = goodsTypeAry[i].trim();
                		if(goodsTypeVal == $(this).val()) {
                			$(this).prop("checked", true);
                		}
                	}
                });

                $("input[name=goodsType]:eq(0)").prop("checked", true).trigger("change");
            }



            if( pageParam.undeliverableAreaTp != undefined) {
            	viewModel.searchInfo.set("undeliverableAreaType", pageParam.undeliverableAreaTp);
            }

            if( pageParam.isGoodsTypes != undefined) {
            	viewModel.set("isGoodsTypes", pageParam.isGoodsTypes);
            }

            if( pageParam.isSaleTypes != undefined) {
            	viewModel.set("isSaleTypes", pageParam.isSaleTypes);
            }

            // 부모 Grid 갯수 파라미터 - 상품일괄수정에서 상품 추가할때 사용.
            if(pageParam.goodsTotal != undefined) {
            	parentGridTotal = pageParam.goodsTotal;
            }

            // 부모 PG_ID 파라미터 - 상품일괄수정에서 상품 추가할때 사용.
            if(pageParam.pgId != undefined) {
            	parentPgId = pageParam.pgId;
            }

            // 부모 Grid 정보 - 상품일괄수정에서 상품 추가할때 사용.
            if(pageParam.goodsIds != undefined && pageParam.goodsIds.length > 0) {
            	parentGoodsIdAry = pageParam.goodsIds;
            }

            if( pageParam.bundleYn != undefined) {
            	viewModel.searchInfo.set("bundleYn", pageParam.bundleYn);
            }

            if( pageParam.ilShippingTmplId != undefined) {
            	viewModel.searchInfo.set("ilShippingTmplId", pageParam.ilShippingTmplId);
            }

            // 판매상태 파라미터 정보 있을 경우 셋팅
            if( pageParam.saleStatus != undefined) {
                var saleStatusAry = pageParam.saleStatus.split(',');

            	$("input[name=saleStatus]:gt(0)").each(function(idx){
                	for(var i = 0; i < saleStatusAry.length; i++) {
                		var saleStatusVal = saleStatusAry[i].trim();
                		if(saleStatusVal == $(this).val()) {
                			$(this).prop("checked", true);
                		}
                	}
                });
            }

            // 판매유형 파라미터 정보 있을 경우 셋팅
            if( pageParam.saleType != undefined) {
                var saleTypeAry = pageParam.saleType.split(',');

            	$("input[name=saleType]:gt(0)").each(function(idx){
                	for(var i = 0; i < saleTypeAry.length; i++) {
                		var saleTypeVal = saleTypeAry[i].trim();
                		if(saleTypeVal == $(this).val()) {
                			$(this).prop("checked", true);
                		}
                	}
                });
            }
            else {
            	$("input[name=saleType]:eq(0)").prop("checked", true).trigger("change");
            }

            if(pageParam.promotionDisplay == "Y") {
            	viewModel.set("columnDisplayCategoryHidden", false);
            	viewModel.set("columnNameHidden", false);
            	viewModel.set("columnAreaShippingDeliveryYnHidden", false);
            	viewModel.set("columnDpBrandNameHidden", false);
            	viewModel.set("columnStardardPriceHidden", false);
            	viewModel.set("columnRecommendedPriceHidden", false);
            	viewModel.set("columnSalePriceHidden", false);
            	viewModel.set("columnSaleStatusCodeNameHidden", false);
            	viewModel.set("columnGoodsDisplyYnVisible", false);
            }

            if(pageParam.searchChoiceBtn == "Y") {
            	viewModel.set("isSearchChoiceBtnVisible", true);
            }

            // 체크박스 설정
            if( pageParam.selectType != undefined && pageParam.selectType == "single" ){
                viewModel.set("selectMultiRowYn", false);
            }

			if( pageParam.goodsCallType != undefined && pageParam.goodsCallType == 'regularDeliveryReqDetail') { // 정기배송 주문 신청 내역
				$('input[name=saleType]').prop("disabled", true);
			}
        }

    };

    // 기본 설정
    function fnDefaultSet(){

        // 데이터 초기화
        viewModel.searchInfo.set("searchCondition", "ALL");
        viewModel.searchInfo.set("findKeyword", "");
        viewModel.searchInfo.set("dateSearchType", "CREATE_DATE");
        viewModel.searchInfo.set("goodsName", "");
        viewModel.searchInfo.set("bundleYn", "");
        viewModel.searchInfo.set("undeliverableAreaType", "");

        if( !viewModel.supplierIdDisabled ){
            viewModel.searchInfo.set("supplierId", "");
        }

        if( !viewModel.warehouseIdDisabled ){
            viewModel.searchInfo.set("warehouseId", "");
        }

        if( !viewModel.warehouseGrpDisabled ){
            viewModel.searchInfo.set("warehouseGrpCd", "");
        }

        if( !viewModel.goodsTypeDisabled ){
            viewModel.searchInfo.set("goodsType", "");
        }

        if( !viewModel.undeliverableAreaTypeDisabled ){
            viewModel.searchInfo.set("undeliverableAreaType", "");
        }

        viewModel.searchInfo.set("brandId", "");
        viewModel.searchInfo.set("dpBrandId", "");
        viewModel.searchInfo.set("categoryType", "CATEGORY_STANDARD");
        viewModel.searchInfo.set("saleStatus", []);

        $("input[name=goodsType]:gt(0)").each(function(idx){
        	$(this).prop("checked", false);
        });

        $("input[name=saleStatus]:gt(0)").each(function(idx){
        	$(this).prop("checked", false);
        });

        viewModel.searchInfo.set("displayYn", "");

        fnSetParamValue();

        $('[data-id="fnDateBtnC"]').mousedown();

        // 화면제어
        viewModel.fnCategoryTypeChange();

    };
    //-------------------------------  Common Function end -------------------------------
    function fnWareHouseGroupChange() {
    	fnAjax({
            url     : "/admin/comn/getDropDownWarehouseGroupByWarehouseList",
            method : "GET",
            params : { "warehouseGroupCode" : $("#warehouseGroup").val() },

            success : function( data ){
                let warehouseId = $("#warehouseId").data("kendoDropDownList");
                warehouseId.setDataSource(data.rows);
            },
            error : function(xhr, status, strError){
                fnKendoMessage({ message : xhr.responseText });
            },
            isAction : "select"
        });
    };

    function fnClear() {

    };
	//-------------------------------  Common Function end -------------------------------
    /** Common Clear */
	$scope.fnClear = function() {
		fnClear();
	};
}); // document ready - END
