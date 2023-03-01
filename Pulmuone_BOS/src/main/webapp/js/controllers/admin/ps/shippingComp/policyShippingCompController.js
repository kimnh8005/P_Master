/**-----------------------------------------------------------------------------
 * system           : 택배사 설정
 * @
 * @ 수정일         수정자          수정내용
 * @ ------------------------------------------------------
 * @ 2020.09.08     박승현          최초생성
 * @
 * **/
"use strict";

var PAGE_SIZE = 20;
var policyShippingCompGridDs, policyShippingCompGridOpt, policyShippingCompGrid;

$(document).ready(function() {
    fnInitialize(); //Initialize Page Call ---------------------------------

    //Initialize PageR
    function fnInitialize(){
        $scope.$emit("fnIsMenu", { flag : "true" });

        fnPageInfo({
            PG_ID  : "policyShippingComp",
            callback : fnUI
        });
    };

    // 화면 UI 초기화
    function fnUI(){

        fnInitButton(); //Initialize Button  ---------------------------------
        fnInitOptionBox();//Initialize Option Box ------------------------------------
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
        $("input:radio[name='useYn']").eq(0).prop("checked", true);
    };

    // 조회
    function fnSearch(page){
        let data = $("#searchForm").formSerialize(true);
        let searchData = fnSearchData(data);
        let query = { page : page || 1,
                      pageSize : PAGE_SIZE,
                      filterLength : searchData.length,
                      filter : { filters : searchData }
        };
        policyShippingCompGridDs.query(query);
    };

    function onSubmit(e) {
        e.preventDefault();
        // fnSearch();
    }

    function bindFormEvent(){
        $("#searchForm").on("submit", onSubmit);
    }

    // 신규
    function fnNewAdd() {
        fnPolicyShippingCompPopUp();
    };

    //--------------------------------- Button End---------------------------------

    //------------------------------- Grid Start -------------------------------
    // 그리드 초기화
    function fnInitGrid(){

    	policyShippingCompGridDs = fnGetPagingDataSource({
            url      : "/admin/policy/shippingcomp/getPolicyShippingCompList",
            pageSize : PAGE_SIZE
        });

    	policyShippingCompGridOpt = {
            dataSource : policyShippingCompGridDs,
            pageable : { pageSizes: [20, 30, 50, 100], buttonCount : 10 },
            navigatable: true,
            columns   : [
			               { field: 'rowNumber'				, title: 'No'			, width: '40px'		, attributes : { style: 'text-align:center' }, template: function (dataItem){
			            		return fnKendoGridPagenation(policyShippingCompGrid.dataSource,dataItem); }}
			            , { field : "shippingCompNm"		, title : "택배사 명"		, width : "100px"	, attributes : {style : "text-align:center"} }
                        , { field : "shippingCompCd"		, title : "택배사 코드"		, width: "50px"		, attributes : {style : "text-align:center"},
                            template : function(dataItem){
                            	var str = "<span>";
                            	for(var i = 0 ; i < dataItem.shippingCompCodeList.length ; i++){
                            		str += dataItem.shippingCompCodeList[i].shippingCompCd + "<br>";
                            	}
                            	str += "</span>";
                            	return str;
                            }}
                        , { field : "collectionmallCode"	, title : "수집몰코드"		, width : "50px"	, attributes : {style : "text-align:center"},
                        	template : function(dataItem){
                        		var str = "<span>";
                            	for(var i = 0 ; i < dataItem.shippingCompOutmallList.length ; i++){
                            		if(dataItem.shippingCompOutmallList[i].outmallCode == 'E' ){
                            			str += "이지어드민 : " + dataItem.shippingCompOutmallList[i].outmallShippingCompCode + "<br>";
                            		}else{
                            			str += "사방넷 : " + dataItem.shippingCompOutmallList[i].outmallShippingCompCode;
                            		}
                            	}
                            	str += "</span>";
                            	return str;
                            }}
                        , { field : "trackingUrl"			, title : "송장조회 정보"	, width : "300px"	, attributes : {style : "text-align:left"},
                            template : function(dataItem) { return "<span>추적URL : " + dataItem.trackingUrl + "<br>파라미터 : " + dataItem.invoiceParam; +"</span>";}}
                        , { field : "httpRequestTp"			, title : "연동방법"		, width : "50px"	, attributes : {style : "text-align:center"},
                        	template : function(dataItem) {
                                if( dataItem.httpRequestTp == "G" ){ return "GET";
                                }else if( dataItem.httpRequestTp == "P" ) { return "POST";
                                }else{ return dataItem.httpRequestTp; } }}
                        , { field : "useYn"					, title : "사용여부"		, width : "50px"	, attributes : {style : "text-align:center"},
                            template : function(dataItem) { return dataItem.useYn == "Y" ? "예" : "아니요"}}
                        , { field:'psShippingCompId'		,hidden:true}
            ]
        };

    	policyShippingCompGrid = $("#policyShippingCompGrid").initializeKendoGrid( policyShippingCompGridOpt ).cKendoGrid();
    	policyShippingCompGrid.bind("dataBound", function() {
			//total count
            $('#countTotalSpan').text(policyShippingCompGridDs._total);
        });

        // 그리드 Row 클릭
    	$("#policyShippingCompGrid").on("click", "tbody>tr", function () {
    		fnPolicyShippingCompPopUp();
	    });
    };

    //------------------------------- Grid End -------------------------------

    //---------------Initialize Option Box Start ------------------------------------------------

    // 옵션 초기화
    function fnInitOptionBox() {

    	fnTagMkRadio({
			id    :  'useYn',
			tagId : 'useYn',
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

    // 택배사설정 신규등록 팝업
    function fnPolicyShippingCompPopUp(){

        var dataItem = policyShippingCompGrid.dataItem(policyShippingCompGrid.select());
        var psShippingCompId = "";
        if(dataItem != null && dataItem.psShippingCompId != null) psShippingCompId = dataItem.psShippingCompId;
		var param  = {'psShippingCompId' : psShippingCompId };

		fnKendoPopup({
            id         : "policyShippingCompMgm",
            title      : "택배설정",
            width      : "900px",
            height     : "600px",
            src        : "#/policyShippingCompMgm",
            param      : param,
            success    : function( id, data ){
            	fnSearch(policyShippingCompGrid.dataSource.page());
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
