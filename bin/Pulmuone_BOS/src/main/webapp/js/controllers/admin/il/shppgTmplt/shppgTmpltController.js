/**-----------------------------------------------------------------------------
 * system           : 배송정책 설정
 * @
 * @ 수정일         수정자          수정내용
 * @ ------------------------------------------------------
 * @ 2020.07.08     손진구          최초생성
 * @
 * **/
"use strict";

var PAGE_SIZE = 20;
var viewModel; // viewModel
var shippingTemplateGridDs, shippingTemplateGridOpt, shippingTemplateGrid;

$(document).ready(function() {
    fnInitialize(); //Initialize Page Call ---------------------------------

    //Initialize PageR
    function fnInitialize(){
        $scope.$emit("fnIsMenu", { flag : "true" });

        fnPageInfo({
            PG_ID  : "shppgTmplt",
            callback : fnUI
        });
    };

    // 화면 UI 초기화
    function fnUI(){

        fnInitButton(); //Initialize Button  ---------------------------------
        fnInitOptionBox();//Initialize Option Box ------------------------------------
        fnViewModelInit();
        fnInitGrid();   //Initialize Grid ------------------------------------
        bindFormEvent();
    };

	//--------------------------------- Button Start---------------------------------
    // 버튼 초기화
	function fnInitButton(){
		$("#fnSearch, #fnClear").kendoButton();
	};

    // 초기화
    function fnClear() {
        $("#searchForm").formClear(true);
        $("#warehouseViewYnCheck").prop("checked", false);
        $("input:radio[name='conditionType']").eq(0).prop("checked", true);
    };

    // 조회
    function fnSearch(){
        viewModel.fnGetUndeliverableAreaTp();

        let data = $("#searchForm").formSerialize(true);
        let searchData = fnSearchData(data);
        let query = { page : 1,
                      pageSize : PAGE_SIZE,
                      filterLength : searchData.length,
                      filter : { filters : searchData }
        };

        shippingTemplateGridDs.query(query);
    };

    function onSubmit(e) {
        e.preventDefault();
        // fnSearch();
    }

    function bindFormEvent(){
        $("#searchForm").on("submit", onSubmit);
        console.log("bindFormEvent");
    }

    // 신규
    function fnNewAdd() {
        fnShippingTemplatePopUp();
    };

    //--------------------------------- Button End---------------------------------

    // viewModel 초기화
    function fnViewModelInit() {
        viewModel = new kendo.data.ObservableObject({
            UNDELIVERABLE_AREA_TP: {"NONE":"", "A1":"", "A2":"", "A1_A2":""},
            fnGetUndeliverableAreaTp : function() { // 승인상태 공통코드 조회
                fnAjax({
                    url: "/admin/comn/getCodeList",
                    method: "GET",
                    async: false,
                    params: {"stCommonCodeMasterCode": "UNDELIVERABLE_AREA_TP", "useYn": "Y"},
                    success: function (data) {
                        viewModel.set("UNDELIVERABLE_AREA_TP.NONE", data.rows[0].CODE);
                        viewModel.set("UNDELIVERABLE_AREA_TP.A1", data.rows[1].CODE);
                        viewModel.set("UNDELIVERABLE_AREA_TP.A2", data.rows[2].CODE);
                        viewModel.set("UNDELIVERABLE_AREA_TP.A1_A2", data.rows[3].CODE);
                    },
                    error: function (xhr, status, strError) {
                        fnKendoMessage({message: xhr.responseText});
                    }
                });
            }
        }
    )}

    //------------------------------- Grid Start -------------------------------
    // 그리드 초기화
    function fnInitGrid(){

        shippingTemplateGridDs = fnGetPagingDataSource({
            url      : "/admin/policy/shippingtemplate/getShippingTemplateList",
            pageSize : PAGE_SIZE
        });

        shippingTemplateGridOpt = {
            dataSource : shippingTemplateGridDs,
            pageable : { pageSizes: [20, 30, 50, 100], buttonCount : 10 },
            navigatable: true,
            columns   : [
                          { field : "shippingTemplateName", title: "배송정책명", width: "250px", attributes : {style : "text-align:center;color:blue;text-decoration:underline"},
                            template : function(dataItem){
                                return "<span kind='modify'>" + dataItem.shippingTemplateName + "</span>";
                            }
                          }
                        , { field : "conditionTypeName", title : "조건배송비 구분", width : "200px", attributes : {style : "text-align:center"} }
                        , { field : "shippingPrice", title : "배송비", width : "70px", attributes : {style : "text-align:center"},
                            template : function(dataItem) {
                                if( dataItem.shippingPrice == 0 ){
                                    return "무료";
                                }else{
                                    return kendo.format("{0:n0}", dataItem.shippingPrice) + " 원";
                                }
                            }
                          }
                        , { field : "claimShippingPrice", title : "클레임 배송비", width : "70px", attributes : {style : "text-align:center"},
                            template : function(dataItem) {
                                return kendo.format("{0:n0}", dataItem.claimShippingPrice) + " 원";
                            }
                          }
                        , { field : "bundleYn", title : "합배송여부", width : "80px", attributes : {style : "text-align:center"},
                            template : function(dataItem) {
                                if( dataItem.bundleYn == "Y" ){
                                    return "합배송";
                                }else{
                                    return "합배송 제외";
                                }
                            }
                          }
                        , { field : "areaShippingPrice", title : "지역별배송비", width : "75px", attributes : {style : "text-align:center"},
                            template : function(dataItem) {
                                let shippingYn = "미사용";
                                let unableArea = null;

                                if (dataItem.areaShippingYn == "Y") {
                                    shippingYn = "사용";
                                } else if(dataItem.areaShippingYn == "N") {
                                    shippingYn = "미사용";
                                }

                                if (dataItem.undeliverableAreaTp == viewModel.UNDELIVERABLE_AREA_TP.A1) {
                                    unableArea = "(1권역/배송불가)"
                                } else if (dataItem.undeliverableAreaTp == viewModel.UNDELIVERABLE_AREA_TP.A2) {
                                    unableArea = "(2권역/배송불가)"
                                } else if (dataItem.undeliverableAreaTp == viewModel.UNDELIVERABLE_AREA_TP.A1_A2) {
                                    unableArea = "(1,2권역/배송불가)"
                                }

                                if (unableArea == null)
                                    return shippingYn;
                                else
                                    return shippingYn + "<br/>" + unableArea;
                            }
                          }
                        , { field : "warehouse", title : "출고처", width : "100px", attributes : {style : "text-align:center"},
                            template : function(dataItem) {
                                if( dataItem.warehouseCount == 1 ){
                                    let warehouseName = dataItem.warehouseName;

                                    if( dataItem.basicYn == "Y" ){
                                        warehouseName += "<span style='color:#ff0000;'>" + "(기본)" + "</span>";
                                    }
                                    return warehouseName;
                                }else{
                                    return '<a href="#" role="button" class="k-button k-button-icontext" kind="warehouseView">출고처보기</a>';
                                }
                            }
                          }
                        , { field : "management", title : "관리", width : "60px", attributes : {style : "text-align:center"},
                            template : function(dataItem){
                                return "<a href='#' role='button' class='k-button k-button-icontext' kind='modify'>수정</a>";
                            }
                          }
            ]
        };

        shippingTemplateGrid = $("#shippingTemplateGrid").initializeKendoGrid( shippingTemplateGridOpt ).cKendoGrid();

        shippingTemplateGrid.bind("dataBound", function() {
			//total count
            $('#countTotalSpan').text( kendo.toString(shippingTemplateGridDs._total, "n0") );
        });
        // 그리드 Row 클릭
        $($("#shippingTemplateGrid").data("kendoGrid").tbody).on("click", "[kind]", function(e) {
            e.preventDefault();
            let grid = $("#shippingTemplateGrid").data("kendoGrid");
            let row = $(this).closest("tr");
            let colIdx = $("td", row).index(this);
            let kindValue = e.currentTarget.attributes.kind.value;
            let rowData = grid.dataItem(row);

            switch (kindValue) {
                case "modify" :
                     fnShippingTemplatePopUp(rowData);
                     break;
                case "warehouseView" :
                     fnWarehouseViewPopUp(rowData);
                     break;
            };
        });

    };
    //------------------------------- Grid End -------------------------------

    //---------------Initialize Option Box Start ------------------------------------------------

    // 옵션 초기화
    function fnInitOptionBox() {

        // 조건배송비 구분
        fnTagMkRadio({
            id : "conditionType",
            url : "/admin/comn/getCodeList",
            tagId : "conditionType",
            style : {},
            params : {"stCommonCodeMasterCode" : "CONDITION_TYPE", "useYn" :"Y"},
            chkVal : "",
            beforeData : [{ "CODE" : "", "NAME" : "전체" }]
        });
    };

    //---------------Initialize Option Box End ------------------------------------------------


    //-------------------------------  Common Function start -------------------------------

    // 배송정책 팝업
    function fnShippingTemplatePopUp( params ){
        if( params == undefined ){
            params = {};
        }

        fnKendoPopup({
            id         : "shppgTmpltMgm",
            title      : "배송정책",
            width      : "900px",
            height     : "800px",
            scrollable : "yes",
            src        : "#/shppgTmpltMgm",
            param      : params,
            success    : function( id, data ){
                shippingTemplateGridDs.read();
            }
        });
    };

    // 출고처보기 팝업
    function fnWarehouseViewPopUp( params ){
        if( params == undefined ){
            fnKendoMessage({ message : "필수값이 없습니다." });
            return;
        }

        fnKendoPopup({
            id         : "warehouseViewPopup",
            title      : "출고처 보기",
            width      : "900px",
            height     : "800px",
            scrollable : "yes",
            src        : "#/warehouseViewPopup",
            param      : params,
            success    : function( id, data ){
            }
        });
    };

    // 출고처 기준보기 체크박스 클릭
    function fnWarehouseViewYnChange(){
        if( $("input[name=warehouseViewYnCheck]").is(":checked") ){
            $("#warehouseViewYn").val("Y");
        }else{
            $("#warehouseViewYn").val("N");
        }

        fnSearch();
    };

    // 출고처 검색 팝업
    function fnWarehouseSearchPopup(){
        fnKendoPopup({
            id     : "warehousePopup",
            title  : "출고처 검색",
            src    : "#/warehousePopup",
            param  : { "supplierCode" : ""},
            width  : "760px",
            height : "585px",
            success: function( id, data ){
                if( data.urWarehouseId ){
                    $("#warehouseId").val(data.urWarehouseId);
                    $("#warehouseName").val(data.warehouseName);
                }
            }
        });
    };

    //-------------------------------  Common Function end -------------------------------

    //------------------------------- Html 버튼 바인딩  Start -------------------------------

    $scope.fnSearch = function() { fnSearch(); }; // 조회
    $scope.fnClear = function() { fnClear(); }; // 초기화
    $scope.fnNewAdd = function() { fnNewAdd(); }; // 신규
    $scope.fnWarehouseViewYnChange = function() { fnWarehouseViewYnChange(); }; // 출고처 기준 보기 체크
    $scope.fnWarehouseSearchPopup = function() { fnWarehouseSearchPopup(); }; // 출고처 검색 팝업

    //------------------------------- Html 버튼 바인딩  End -------------------------------
}); // document ready - END
