/**-----------------------------------------------------------------------------
 * system           : 신용카드 혜택안내 관리
 * @
 * @ 수정일         수정자          수정내용
 * @ ------------------------------------------------------
 * @ 2020.10.12     박승현          최초생성
 * @
 * **/
"use strict";

var PAGE_SIZE = 20;
var policyPaymentCardBenefitGridDs, policyPaymentCardBenefitGridOpt, policyPaymentCardBenefitGrid;

$(document).ready(function() {
    fnInitialize(); //Initialize Page Call ---------------------------------

    //Initialize PageR
    function fnInitialize(){
        $scope.$emit("fnIsMenu", { flag : "true" });

        fnPageInfo({
            PG_ID  : "policyPaymentCardBenefit",
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
        policyPaymentCardBenefitGridDs.query(query);
    };

    // 신규
    function fnNewAdd() {
    	fnPolicyPaymentCardBenefitMgm();
    };

    //--------------------------------- Button End---------------------------------

    //------------------------------- Grid Start -------------------------------
    // 그리드 초기화
    function fnInitGrid(){

    	policyPaymentCardBenefitGridDs = fnGetPagingDataSource({
            url      : "/admin/policy/payment/getPolicyPaymentCardBenefitList",
            pageSize : PAGE_SIZE
        });

    	policyPaymentCardBenefitGridOpt = {
            dataSource : policyPaymentCardBenefitGridDs,
            pageable : { pageSizes: [20, 30, 50], buttonCount : 20 },
            navigatable: true,
            columns   : [
		            	{ title: "No", width: "50px", attributes : {style : "text-align:center"},
		                    template : "<span class='row-number'></span>"
		                  }
		                , { field : "pgName", title: "혜택 범위", width: "50px", attributes : {style : "text-align:center"} }
		                , { field : "cardBenefitTpName", title: "안내 유형", width: "150px", attributes : {style : "text-align:center"} }
		                , { field : "title", title: "제목", width: "150px", attributes : {style : "text-align:center"} }
		                , { field : "benefitStatus", title : "진행여부", width : "50px", attributes : {style : "text-align:center"},
		                	template : function(dataItem) {
		                		if( dataItem.benefitStatus == "1" ){
                                    return "진행중";
                                }else if( dataItem.benefitStatus == "2" ){
                                    return "진행예정";
                                }else{
                                	return "종료";
                                }
		                	}
		                }
                        , { field : "startDt", title : "기간", width : "150px", attributes : {style : "text-align:center"},
                            template : function(dataItem) {
                                    return dataItem.startDt + "~" + dataItem.endDt;
                            }
                          }
                        ,{ command: { text: "미리보기", className: "btn-gray btn-s", click: fnPolicyPaymentCardBenefitPopUp }, title : '관리', width:'100px' ,attributes:{ style:'text-align:center', class:'forbiz-cell-readonly' }}
                        , { field:'psCardBenefitId'		,hidden:true}
            ]
        };

    	policyPaymentCardBenefitGrid = $("#policyPaymentCardBenefitGrid").initializeKendoGrid( policyPaymentCardBenefitGridOpt ).cKendoGrid();

    	policyPaymentCardBenefitGrid.bind("dataBound", function(){
    		var row_num = policyPaymentCardBenefitGridDs._total - ((policyPaymentCardBenefitGridDs._page - 1) * policyPaymentCardBenefitGridDs._pageSize);
    		$("#policyPaymentCardBenefitGrid tbody > tr .row-number").each(function(index){
    			$(this).html(row_num);
    			row_num--;
    		});
    		$("#countTotalSpan").text(policyPaymentCardBenefitGridDs._total );
    	});
    };

    //------------------------------- Grid End -------------------------------

    //---------------Initialize Option Box Start ------------------------------------------------

    // 옵션 초기화
    function fnInitOptionBox() {

    	fnKendoDropDownList({
            id : "psPgCd",
            url : "/admin/comn/getCodeList",
            tagId : "psPgCd",
            params : { "stCommonCodeMasterCode" : "PG_SERVICE", "useYn" : "Y" },
            chkVal: "",
            blank : "전체",
            async : false
        });
    	fnKendoDropDownList({
    		id : "cardBenefitTp",
    		url : "/admin/comn/getCodeList",
    		tagId : "cardBenefitTp",
    		params : { "stCommonCodeMasterCode" : "CARD_BENEFIT_INFO_TP", "useYn" : "Y" },
    		chkVal: "",
    		blank : "전체",
    		async : false
    	});

    };

    //---------------Initialize Option Box End ------------------------------------------------


    //-------------------------------  Common Function start -------------------------------

    // 신용카드 혜택안내 신규등록 팝업
    function fnPolicyPaymentCardBenefitPopUp(e){
    	e.preventDefault();
    	var dataItem = policyPaymentCardBenefitGrid.dataItem($(e.currentTarget).closest('tr'));
    	var psCardBenefitId = "";
    	if(dataItem != null && dataItem.psCardBenefitId != null) psCardBenefitId = dataItem.psCardBenefitId;
    	var param  = {'psCardBenefitId' : psCardBenefitId };

    	fnKendoPopup({
    		id         : "policyPaymentCardBenefitPopup",
    		title      : "신용카드 혜택안내",
    		width      : "900px",
    		height     : "450px",
    		src        : "#/policyPaymentCardBenefitPopup",
    		param      : param,
    		success    : function( id, data ){
    			policyPaymentCardBenefitGridDs.read();
    		}
    	});

    };
    // 신용카드 혜택안내 수정 팝업
    function fnMgm(psCardBenefitId){
    	LAYER_POPUP_OBJECT.data('kendoWindow').close();
    	fnPolicyPaymentCardBenefitMgm(psCardBenefitId);
    }
    // 신용카드 혜택안내 신규등록 팝업
    function fnPolicyPaymentCardBenefitMgm(psCardBenefitId){
		var param  = {'psCardBenefitId' : psCardBenefitId };

		fnKendoPopup({
            id         : "policyPaymentCardBenefitMgm",
            title      : "신용카드 혜택안내",
            width      : "900px",
            height     : "450px",
            src        : "#/policyPaymentCardBenefitMgm",
            param      : param,
            success    : function( id, data ){
            	policyPaymentCardBenefitGridDs.read();
            }
        });

    };



    //-------------------------------  Common Function end -------------------------------

    //------------------------------- Html 버튼 바인딩  Start -------------------------------

    $scope.fnSearch = function() { fnSearch(); }; // 조회
    $scope.fnClear = function() { fnClear(); }; // 초기화
    $scope.fnNewAdd = function() { fnNewAdd(); }; // 신규
    $scope.fnMgm = function( psCardBenefitId ){  fnMgm( psCardBenefitId);};

    //------------------------------- Html 버튼 바인딩  End -------------------------------
}); // document ready - END
