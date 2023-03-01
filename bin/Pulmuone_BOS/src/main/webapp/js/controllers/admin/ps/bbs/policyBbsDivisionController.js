﻿﻿/**-----------------------------------------------------------------------------
 * system           : 게시판분류 설정
 * @
 * @ 수정일         수정자          수정내용
 * @ ------------------------------------------------------
 * @ 2020.09.18     박승현          최초생성
 * @
 * **/
"use strict";

var PAGE_SIZE = 20;
var policyBbsDivisionGridDs, policyBbsDivisionGridOpt, policyBbsDivisionGrid;

$(document).ready(function() {
    fnInitialize(); //Initialize Page Call ---------------------------------

    //Initialize PageR
    function fnInitialize(){
        $scope.$emit("fnIsMenu", { flag : "true" });

        fnPageInfo({
            PG_ID  : "policyBbsDivision",
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
		$("#fnSearch, #fnClear").kendoButton();
	};

    // 초기화
    function fnClear() {
        $("#searchForm").formClear(true);
        $("input:select[name='bbsTp']").eq(0).prop("checked", true);
    };

    // 조회
    function fnSearch(){
        let data = $("#searchForm").formSerialize(true);
        let searchData = fnSearchData(data);
        let query = { page : 1,
                      pageSize : PAGE_SIZE,
                      filterLength : searchData.length,
                      filter : { filters : searchData }
        };
        policyBbsDivisionGridDs.query(query);
    };

    // 신규
    function fnNewAdd() {
        fnPolicyBbsDivisionPopUp();
    };

    //--------------------------------- Button End---------------------------------

    //------------------------------- Grid Start -------------------------------
    // 그리드 초기화
    function fnInitGrid(){

    	policyBbsDivisionGridDs = fnGetPagingDataSource({
            url      : "/admin/policy/bbs/getPolicyBbsDivisionList",
            pageSize : PAGE_SIZE
        });

    	policyBbsDivisionGridOpt = {
            dataSource : policyBbsDivisionGridDs,
            pageable : { pageSizes: [20, 30, 50, 100], buttonCount : 10 },
            navigatable: true,
            columns   : [
		            	{ title: "No", width: "50px", attributes : {style : "text-align:center"},
		                    template : "<span class='row-number'></span>"
		                  }
		                , { field : "bbsTpNm", title: "타입", width: "50px", attributes : {style : "text-align:center"}
                          }
                        , { field : "parentCategoryNm", title : "상위 분류", width : "100px", attributes : {style : "text-align:center"} }
                        , { field : "categoryNm", title : "분류명", width : "300px", attributes : {style : "text-align:center"}
                          }
                        , { field : "useYn", title : "사용여부", width : "50px", attributes : {style : "text-align:center"},
                            template : function(dataItem) {
                                if( dataItem.useYn == "Y" ){
                                    return "예";
                                }else{
                                    return "아니요";
                                }
                            }
                          }
                        ,{ field:'createDt'		,title : '등록일'		, width:'100px',attributes:{ style:'text-align:center' }}
                        , { field:'csCategoryId'		,hidden:true}
            ]
        };

    	policyBbsDivisionGrid = $("#policyBbsDivisionGrid").initializeKendoGrid( policyBbsDivisionGridOpt ).cKendoGrid();

        // 그리드 Row 클릭
    	$("#policyBbsDivisionGrid").on("click", "tbody>tr", function () {
    		fnPolicyBbsDivisionPopUp();
	    });

    	policyBbsDivisionGrid.bind("dataBound", function(){
    		var row_num = policyBbsDivisionGridDs._total - ((policyBbsDivisionGridDs._page - 1) * policyBbsDivisionGridDs._pageSize);
    		$("#policyBbsDivisionGrid tbody > tr .row-number").each(function(index){
    			$(this).html(row_num);
    			row_num--;
    		});
    		$("#countTotalSpan").text(policyBbsDivisionGridDs._total );
    	});
    };

    //------------------------------- Grid End -------------------------------

    //---------------Initialize Option Box Start ------------------------------------------------

    // 옵션 초기화
    function fnInitOptionBox() {

        fnKendoDropDownList({
            id : "bbsTp",
            url : "/admin/comn/getCodeList",
            tagId : "bbsTp",
            params : { "stCommonCodeMasterCode" : "BBS_TP", "useYn" : "Y" },
            chkVal: "",
            blank : "전체",
            async : false
        });


    };

    //---------------Initialize Option Box End ------------------------------------------------


    //-------------------------------  Common Function start -------------------------------

    // 게시판분류 설정 신규등록 팝업
    function fnPolicyBbsDivisionPopUp(){

        var dataItem = policyBbsDivisionGrid.dataItem(policyBbsDivisionGrid.select());
        var csCategoryId = "";
        if(dataItem != null && dataItem.csCategoryId != null) csCategoryId = dataItem.csCategoryId;
		var param  = {'csCategoryId' : csCategoryId };

		fnKendoPopup({
            id         : "policyBbsDivisionMgm",
            title      : "게시판분류설정",
            width      : "900px",
            height     : "300px",
            src        : "#/policyBbsDivisionMgm",
            param      : param,
            success    : function( id, data ){
            	policyBbsDivisionGridDs.read();
            }
        });

    };



    //-------------------------------  Common Function end -------------------------------

    //------------------------------- Html 버튼 바인딩  Start -------------------------------

    $scope.fnSearch = function() { fnSearch(); }; // 조회
    $scope.fnClear = function() { fnClear(); }; // 초기화
    $scope.fnNewAdd = function() { fnNewAdd(); }; // 신규

    //------------------------------- Html 버튼 바인딩  End -------------------------------
}); // document ready - END
