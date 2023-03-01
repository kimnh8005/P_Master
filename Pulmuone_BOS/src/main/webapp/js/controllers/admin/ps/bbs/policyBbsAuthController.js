﻿﻿/**-----------------------------------------------------------------------------
 * system           : 게시판권한 설정
 * @
 * @ 수정일         수정자          수정내용
 * @ ------------------------------------------------------
 * @ 2020.09.21     박승현          최초생성
 * @
 * **/
"use strict";

var PAGE_SIZE = 20;
var policyBbsAuthGridDs, policyBbsAuthGridOpt, policyBbsAuthGrid;

$(document).ready(function() {
    fnInitialize(); //Initialize Page Call ---------------------------------

    //Initialize PageR
    function fnInitialize(){
        $scope.$emit("fnIsMenu", { flag : "true" });

        fnPageInfo({
            PG_ID  : "policyBbsAuth",
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
        policyBbsAuthGridDs.query(query);
    };

    // 신규
    function fnNewAdd() {
        fnPolicyBbsAuthPopUp();
    };

    //--------------------------------- Button End---------------------------------

    //------------------------------- Grid Start -------------------------------
    // 그리드 초기화
    function fnInitGrid(){

    	policyBbsAuthGridDs = fnGetPagingDataSource({
            url      : "/admin/policy/bbs/getPolicyBbsAuthList",
            pageSize : PAGE_SIZE
        });

    	policyBbsAuthGridOpt = {
            dataSource : policyBbsAuthGridDs,
            pageable : { pageSizes: [20, 30, 50, 100], buttonCount : 10 },
            navigatable: true,
            columns   : [
		            	{ title: "No", width: "50px", attributes : {style : "text-align:center"},
		                    template : "<span class='row-number'></span>"
		                  }
		                , { field : "bbsNm", title: "게시판명", width: "150px", attributes : {style : "text-align:center"} }
                        , { field : "imageYn", title : "이미지", width : "50px", attributes : {style : "text-align:center"},
                            template : function(dataItem) {
                                if( dataItem.imageYn == "Y" ){
                                    return "예";
                                }else{
                                    return "아니요";
                                }
                            }
                          }
                        , { field : "attachYn", title : "첨부파일", width : "50px", attributes : {style : "text-align:center"},
                        	template : function(dataItem) {
                        		if( dataItem.attachYn == "Y" ){
                        			return "예";
                        		}else{
                        			return "아니요";
                        		}
                        	}
                        }
                        , { field : "replyYn", title : "답변", width : "50px", attributes : {style : "text-align:center"},
                        	template : function(dataItem) {
                        		if( dataItem.replyYn == "Y" ){
                        			return "예";
                        		}else{
                        			return "아니요";
                        		}
                        	}
                        }
                        , { field : "commentYn", title : "댓글", width : "50px", attributes : {style : "text-align:center"},
                        	template : function(dataItem) {
                        		if( dataItem.commentYn == "Y" ){
                        			return "예";
                        		}else{
                        			return "아니요";
                        		}
                        	}
                        }
                        , { field : "commentSecretYn", title : "비밀댓글", width : "50px", attributes : {style : "text-align:center"},
                        	template : function(dataItem) {
                        		if( dataItem.commentSecretYn == "Y" ){
                        			return "예";
                        		}else{
                        			return "아니요";
                        		}
                        	}
                        }
                        , { field : "recommendYn", title : "추천", width : "50px", attributes : {style : "text-align:center"},
                        	template : function(dataItem) {
                        		if( dataItem.recommendYn == "Y" ){
                        			return "예";
                        		}else{
                        			return "아니요";
                        		}
                        	}
                        }
                        ,{ field:'createDt'		,title : '등록일'		, width:'50px',attributes:{ style:'text-align:center' }}
                        , { field:'csBbsConfigId'		,hidden:true}
            ]
        };

    	policyBbsAuthGrid = $("#policyBbsAuthGrid").initializeKendoGrid( policyBbsAuthGridOpt ).cKendoGrid();

        // 그리드 Row 클릭
    	$("#policyBbsAuthGrid").on("click", "tbody>tr", function () {
    		fnPolicyBbsAuthPopUp();
	    });

    	policyBbsAuthGrid.bind("dataBound", function(){
    		var row_num = policyBbsAuthGridDs._total - ((policyBbsAuthGridDs._page - 1) * policyBbsAuthGridDs._pageSize);
    		$("#policyBbsAuthGrid tbody > tr .row-number").each(function(index){
    			$(this).html(row_num);
    			row_num--;
    		});
    		$("#countTotalSpan").text(policyBbsAuthGridDs._total );
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

    // 게시판권한설정 신규등록 팝업
    function fnPolicyBbsAuthPopUp(){

        var dataItem = policyBbsAuthGrid.dataItem(policyBbsAuthGrid.select());
        var csBbsConfigId = "";
        if(dataItem != null && dataItem.csBbsConfigId != null) csBbsConfigId = dataItem.csBbsConfigId;
		var param  = {'csBbsConfigId' : csBbsConfigId };

		fnKendoPopup({
            id         : "policyBbsAuthMgm",
            title      : "게시판권한설정",
            width      : "900px",
            height     : "450px",
            src        : "#/policyBbsAuthMgm",
            param      : param,
            success    : function( id, data ){
            	policyBbsAuthGridDs.read();
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
