﻿﻿/**-----------------------------------------------------------------------------
 * system           : 일일상품 골라담기 설정
 * @
 * @ 수정일         수정자          수정내용
 * @ ------------------------------------------------------
 * @ 2020.10.15     박승현          최초생성
 * @
 * **/
"use strict";

var PAGE_SIZE = 20;
var policyDailyGoodsPickGridDs, policyDailyGoodsPickGridOpt, policyDailyGoodsPickGrid;

$(document).ready(function() {
    fnInitialize(); //Initialize Page Call ---------------------------------

    //Initialize PageR
    function fnInitialize(){
        $scope.$emit("fnIsMenu", { flag : "true" });

        fnPageInfo({
            PG_ID  : "policyDailyGoodsPick",
            callback : fnUI
        });
    };

    // 화면 UI 초기화
    function fnUI(){

        fnInitButton(); //Initialize Button  ---------------------------------
        fnInitOptionBox();//Initialize Option Box ------------------------------------
        fnInitGrid();   //Initialize Grid ------------------------------------
    };

	//--------------------------------- Button Start---------------------------------
    // 버튼 초기화
	function fnInitButton(){
		$("#fnSearch, #fnClear, #fnExcelExport").kendoButton();
	};

    // 초기화
    function fnClear() {
        $("#searchForm").formClear(true);
        $("#checkBoxAll").prop("checked", false);
    };

    function fnExcelExport(){
        var data = $('#searchForm').formSerialize(true);
        fnExcelDownload("/admin/policy/dailygoods/getPolicyDailyGoodsPickListExportExcel", data);
    }

    // 조회
    function fnSearch(){
        let data = $("#searchForm").formSerialize(true);
        let searchData = fnSearchData(data);
        $("#checkBoxAll").prop("checked", false);
        let query = { page : 1,
                      pageSize : PAGE_SIZE,
                      filterLength : searchData.length,
                      filter : { filters : searchData }
        };
        policyDailyGoodsPickGridDs.query(query);
    };

    // 신규
    function fnNewAdd() {
    };

    //--------------------------------- Button End---------------------------------

    //------------------------------- Grid Start -------------------------------
    // 그리드 초기화
    function fnInitGrid(){

    	policyDailyGoodsPickGridDs = fnGetPagingDataSource({
            url      : "/admin/policy/dailygoods/getPolicyDailyGoodsPickList",
            pageSize : PAGE_SIZE
        });

    	policyDailyGoodsPickGridOpt = {
            dataSource : policyDailyGoodsPickGridDs,
            pageable : { pageSizes: [20, 30, 50], buttonCount : 20 },
            navigatable: true,
            columns   : [
            			{ field : 'chk', headerTemplate : "<input type='checkbox' id='checkBoxAll' />"
							, template: function(dataItem) {
								var html;
								if (dataItem.greenJuiceCleanseOptYn == 'Y') { // 녹즙클렌즈랩 옵션이 사용인 경우.
									html = '<input type="checkbox" name="itemGridChk" value="#: goodsId #" class="itemGridChk" disabled />'
								} else {
									html = '<input type="checkbox" name="itemGridChk" value="#: goodsId #" class="itemGridChk" />';
								}
								return html;
							}

        					, width:'50px', attributes : {style : "text-align:center;"}
            			}
		                , { field : "pickableYn", title: "골라담기 허용여부", width: "100px", attributes : {style : "text-align:center"} }
		                , { field : "goodsId", title: "상품코드", width: "100px", attributes : {style : "text-align:center"} }
		                , { field : "goodsName", title: "상품명", width: "150px", attributes : {style : "text-align:center"} }
		                , { field : "supplierName", title: "공급업체", width: "150px", attributes : {style : "text-align:center"} }
		                , { field : "brandName", title: "브랜드", width: "150px", attributes : {style : "text-align:center"} }
		                , { field : "createDt",title : "등록일", width : "100px", attributes : {style : "text-align:center" }}
            ]
        };

    	policyDailyGoodsPickGrid = $("#policyDailyGoodsPickGrid").initializeKendoGrid( policyDailyGoodsPickGridOpt ).cKendoGrid();

    	policyDailyGoodsPickGrid.bind("dataBound", function(){
    		$("#countTotalSpan").text(policyDailyGoodsPickGridDs._total );
    	});
    	$("#checkBoxAll").prop("checked", false);

    	// 그리드 전체선택 클릭
        $("#checkBoxAll").on("click", function(index){
            if( $("#checkBoxAll").prop("checked") ){
                $("input[name=itemGridChk]").not(":disabled").prop("checked", true);
            }else{
                $("input[name=itemGridChk]").prop("checked", false);
            }
        });

        // 그리드 체크박스 클릭
        policyDailyGoodsPickGrid.element.on("click", "[name=itemGridChk]" , function(e){
            if( e.target.checked ){
                if( $("[name=itemGridChk]").not(":disabled").length == $("[name=itemGridChk]:checked").length ){
                    $("#checkBoxAll").prop("checked", true);
                }
            }else{
                $("#checkBoxAll").prop("checked", false);
            }
        });


    };

    //------------------------------- Grid End -------------------------------

    //---------------Initialize Option Box Start ------------------------------------------------

    // 옵션 초기화
    function fnInitOptionBox() {

    	// 검색어 종류
    	fnKendoDropDownList({
    		id : "searchType",
    		data : [ {"CODE" : "goodsName", "NAME" : "상품명"},
    				{"CODE" : "goodsId", "NAME" : "상품코드"}
    			],
    		value: "goodsName",
//    		blank : "전체",
    		tagId : "searchType"
        });
    	fnTagMkRadio({
			id    :  'pickableYn',
			tagId : 'pickableYn',
			data : [
                { "CODE" : "", "NAME" : "전체" },
                { "CODE" : "Y", "NAME" : "예" },
                { "CODE" : "N", "NAME" : "아니오" }
            ],
			chkVal: '',
			style : {}
		});

    };

    //---------------Initialize Option Box End ------------------------------------------------


    //-------------------------------  Common Function start -------------------------------

    // 일일상품 골라담기 허용여부 수정
    function fnPolicyDailyGoodsPickable(able){
    	let selectRows  = $("#policyDailyGoodsPickGrid").find("input[name=itemGridChk]:checked").closest("tr");
    	let params = {};
        params.goodsIdList = [];
        if( selectRows.length == 0 ){
            fnKendoMessage({ message : "선택된 상품이 없습니다." });
            return;
        }
        for(let i = 0, selectCount = selectRows.length; i < selectCount; i++){
        	 let dataItem = policyDailyGoodsPickGrid.dataItem($(selectRows[i]));
             params.goodsIdList[i] = dataItem.goodsId;
        }
        params.pickableYn = able;
        fnAjax({
            url     : "/admin/policy/dailygoods/putPolicyDailyGoodsPick",
            params  : params,
            contentType : "application/json",
            success : function( data ){
            	let msg = "허용제외";
            	if(able == "Y") msg = "허용처리";
            	fnKendoMessage({  message : "선택한 " + selectRows.length + "개 상품 중 "+ data.updateCount + "개 상품이 정상적으로 " + msg + "되었습니다."
					, ok : function(){
						fnSearch();
					}
				});
            },
            error : function(xhr, status, strError){
                fnKendoMessage({ message : xhr.responseText });
            },
            isAction : "update"
        });

    };

    //-------------------------------  Common Function end -------------------------------

    //------------------------------- Html 버튼 바인딩  Start -------------------------------

    $scope.fnSearch = function() { fnSearch(); }; // 조회
    $scope.fnClear = function() { fnClear(); }; // 초기화
    $scope.fnPolicyDailyGoodsPickable = function( able ){  fnPolicyDailyGoodsPickable( able);};
    $scope.fnExcelExport = function() { fnExcelExport(); };

    //------------------------------- Html 버튼 바인딩  End -------------------------------
}); // document ready - END
