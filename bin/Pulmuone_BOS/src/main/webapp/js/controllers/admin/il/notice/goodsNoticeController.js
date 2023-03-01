﻿﻿/**-----------------------------------------------------------------------------
 * system           : 상품공통공지사항관리
 * @
 * @ 수정일         수정자          수정내용
 * @ ------------------------------------------------------
 * @ 2020.12.03     박승현          최초생성
 * @
 * **/
"use strict";

var PAGE_SIZE = 20;
var aGridDs, aGridOpt, aGrid;

$(document).ready(function() {
    fnInitialize(); //Initialize Page Call ---------------------------------

    //Initialize PageR
    function fnInitialize(){
        $scope.$emit("fnIsMenu", { flag : "true" });

        fnPageInfo({
            PG_ID  : "goodsNotice",
            callback : fnUI
        });
    };

    // 화면 UI 초기화
    function fnUI(){

        fnInitButton(); //Initialize Button  ---------------------------------
        fnInitOptionBox();//Initialize Option Box ------------------------------------
        fnInitGrid();   //Initialize Grid ------------------------------------
        fnSearch();
    };

	//--------------------------------- Button Start---------------------------------
    // 버튼 초기화
	function fnInitButton(){
		$("#fnSearch, #fnClear").kendoButton();
	};

    // 초기화
    function fnClear() {
        $("#searchForm").formClear(true);
		setDefaultDatePicker();
    };

	// 데이트피커 컨트롤러 초기화 함수
	function setDefaultDatePicker() {
		$(".date-controller button").each(function() {
			$(this).attr("fb-btn-active", false);
		})

		$("button[data-id='fnDateBtn3']").attr("fb-btn-active", true);
	}

    // 조회
    function fnSearch(){
        let data = $("#searchForm").formSerialize(true);
        if(data['conditionType'] != '' && data['conditionValue'].length < 2){
        	fnKendoMessage({ message : "검색어를 최소1글자 이상 입력해주세요." });
			return false;
        }
        let searchData = fnSearchData(data);
        let query = { page : 1,
                      pageSize : PAGE_SIZE,
                      filterLength : searchData.length,
                      filter : { filters : searchData }
        };
        aGridDs.query(query);
    };

    // 신규
    function fnNewAdd() {
    	fnGoodsNoticePopUp();
    };

    //--------------------------------- Button End---------------------------------

    //------------------------------- Grid Start -------------------------------
    // 그리드 초기화
    function fnInitGrid(){

    	aGridDs = fnGetPagingDataSource({
            url      : "/admin/goods/notice/getGoodsNoticeList",
            pageSize : PAGE_SIZE
        });

    	aGridOpt = {
            dataSource : aGridDs,
            pageable : { pageSizes: [20, 30, 50], buttonCount : 20 },
            navigatable: true,
            columns   : [
		            	{ title: "No", width: "50px", attributes : {style : "text-align:center"},
		                    template : "<span class='row-number'></span>"
		                  }
		                , { field : "goodsNoticeTpName", title: "공지구분", width: "100px", attributes : {style : "text-align:center"} }
		                , { field : "dispAllYn", title : "노출범위", width : "100px", attributes : {style : "text-align:center"},
		                	template : function(dataItem) {
		                		if( dataItem.dispAllYn == "Y" ){
		                			return "전체";
		                		}else if( dataItem.dispAllYn == "N" ){
		                			return "출고지별";
		                		}else{
		                			return "";
		                		}
		                	}
		                }
		                , { field : "noticeNm", title: "공지 제목", width: "150px", attributes : {style : "text-align:center"} }
		                , { field : "noticeStartDt", title: "노출 시작일", width: "150px", attributes : {style : "text-align:center"} }
		                , { field : "noticeEndDt", title: "노출 종료일", width: "150px", attributes : {style : "text-align:center"} }
		                , { field : "createDt", title: "등록일자", width: "150px", attributes : {style : "text-align:center"} }
                        , { field : "createId", title : "작성자 / 아이디", width : "150px", attributes : {style : "text-align:center"},
                            template : function(dataItem) {
                                    return dataItem.createUserName + "(" + dataItem.createId + ")";
                            }
                          }
                        , { field : "useYn", title: "사용여부", width: "100px", attributes : {style : "text-align:center"} }
                        ,{ command: [{ text: "수정", className: "btn-gray btn-s", click: fnGoodsNoticeMgm, visible: function() { return fnIsProgramAuth("SAVE")} }]
                        	, title : '관리', width:'50px' ,attributes:{ style:'text-align:center', class:'forbiz-cell-readonly' }}
                        , { field:'ilNoticeId'		,hidden:true}
            ]
        };

    	aGrid = $("#aGrid").initializeKendoGrid( aGridOpt ).cKendoGrid();

    	aGrid.bind("dataBound", function(){
    		var row_num = aGridDs._total - ((aGridDs._page - 1) * aGridDs._pageSize);
    		$("#aGrid tbody > tr .row-number").each(function(index){
    			$(this).html(row_num);
    			row_num--;
    		});
    		$("#countTotalSpan").text(aGridDs._total );
    	});
    };

    //------------------------------- Grid End -------------------------------

    //---------------Initialize Option Box Start ------------------------------------------------

    // 옵션 초기화
    function fnInitOptionBox() {

    	fnTagMkRadio({
			id    :  'goodsNoticeTp',
			tagId : 'goodsNoticeTp',
			url   : "/admin/comn/getCodeList",
			params : {"stCommonCodeMasterCode" : "GOODS_NOTICE_TP", "useYn" :"Y"},
			beforeData : [
				{"CODE":"", "NAME":"전체"},
			],
			async : false,
			chkVal: "",
			style : {}
		});
		fnTagMkRadio({
			id    :  'useYn',
			tagId : 'useYn',
			data  : [   { "CODE" : ""	, "NAME":'전체' },
						{ "CODE" : "Y"	, "NAME":'예' },
						{ "CODE" : "N"	, "NAME":'아니오' }
			],
			chkVal: "",
			style : {}
		});
		fnKendoDropDownList({
			id    : 'conditionType',
			data  : [
				{"CODE":"CREATE_NAME"	,"NAME":"작성자"},
				{"CODE":"CREATE_ID"	,"NAME":"작성자아이디"},
				{"CODE":"TITLE"		,"NAME":"제목"}
			],
			blank: "선택",
			textField :"NAME",
			valueField : "CODE"
		});
		fnKendoDatePicker({
			id    : 'startCreateDate',
			format: 'yyyy-MM-dd',
			btnStartId : 'startCreateDate',
			btnEndId : 'endCreateDate',
			defVal : fnGetDayMinus(fnGetToday(),6),
			defType : 'oneWeek',
			change: function() {
				$("#endCreateDate").data("kendoDatePicker").min($("#startCreateDate").val());
			}
		});
		fnKendoDatePicker({
			id    : 'endCreateDate',
			format: 'yyyy-MM-dd',
			btnStyle : true,
			btnStartId : 'startCreateDate',
			btnEndId : 'endCreateDate',
			defVal : fnGetToday(),
			defType : 'oneWeek',
			change: function() {
				$("#startCreateDate").data("kendoDatePicker").max($("#endCreateDate").val());
			}
		});

    };

    //---------------Initialize Option Box End ------------------------------------------------


    //-------------------------------  Common Function start -------------------------------

    // 상품공통공지 신규등록 팝업
    function fnGoodsNoticeMgm(e){
    	e.preventDefault();
    	var dataItem = aGrid.dataItem($(e.currentTarget).closest('tr'));
    	var ilNoticeId = "";
    	if(dataItem != null && dataItem.ilNoticeId != null){
    		fnGoodsNoticePopUp(dataItem.ilNoticeId);
    	}
    };
    function fnGoodsNoticePopUp(ilNoticeId){
		var param  = {'ilNoticeId' : ilNoticeId };
		fnKendoPopup({
            id         : "goodsNoticeMgm",
    		title      : "상품공통공지",
            width      : "900px",
            height     : "550px",
            src        : "#/goodsNoticeMgm",
            param      : param,
            success    : function( id, data ){
            	aGridDs.read();
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
