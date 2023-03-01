/**-----------------------------------------------------------------------------
 * system           : 추가상품 검색 팝업
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
        $("#fnSearch, #fnClear").kendoButton();
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
            				{ title : '선택' , width:'80px'	,attributes:{ style:'text-align:center' }
            					,template: function(dataItem) {
            						var html =	"<a role='button' class='k-button k-button-icontext' kind='choiceGoods' href='#'>선택</a>";
            						return html;
            					}, locked: true
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

        goodsGrid.bind("dataBound", function() {
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

        $('#goodsGrid').on("click", "a[kind=choiceGoods]", function(e) {
			e.preventDefault();
			let params = [];
			var dataItem = goodsGrid.dataItem($(e.currentTarget).closest("tr"));
			params[0] = dataItem;
			parent.POP_PARAM = params;
            parent.LAYER_POPUP_OBJECT.data("kendoWindow").close();
		});
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
    	$("input[name=saleType]:eq(0)").prop("checked", true).trigger("change");

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

                data.goodsType = viewModel.searchInfo.get("goodsType");


                let searchData = fnSearchData(data);
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
            fnClose : function( params ){ // 팝업 닫기
            	parent.LAYER_POPUP_OBJECT.data("kendoWindow").close();
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
                viewModel.searchInfo.set("supplierId", pageParam.supplierId);
                viewModel.searchInfo.set("warehouseId", pageParam.warehouseId);
                viewModel.searchInfo.set("warehouseGrpCd", pageParam.warehouseGrpCd);

                setTimeout(() => {
                    viewModel.set("supplierIdDisabled", true);

                    //출고처그룹 또는 출고처 값이 있을때 비활성화처리
                    if (pageParam.warehouseId != undefined || pageParam.warehouseGrpCd != undefined) {
                        viewModel.set("warehouseIdDisabled", true);
                        viewModel.set("warehouseGrpDisabled", true);
                    }
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

            // 판매상태 파라미터 정보 있을 경우 셋팅
            if( pageParam.saleStatus != undefined) {
                var saleStatusAry = pageParam.saleStatus.split(',');

            	$("input[name=saleStatus]:gt(0)").each(function(idx){
                	for(var i = 0; i < saleStatusAry.length; i++) {
                		var saleStatusVal = saleStatusAry[i];
                		if(saleStatusVal == $(this).val()) {
                			$(this).prop("checked", true);
                		}
                	}
                });
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

        $("input[name=saleStatus]:gt(0)").each(function(idx){
        	$(this).prop("checked", false);
        });

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
