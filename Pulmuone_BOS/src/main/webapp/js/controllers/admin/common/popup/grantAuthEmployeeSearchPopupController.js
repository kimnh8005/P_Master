/**-----------------------------------------------------------------------------
 * system            : 담당자 검색 팝업
 * @
 * @ 수정일            수정자          수정내용
 * @ ------------------------------------------------------
 * @ 2020.08.03     손진구          최초생성
 * @
 * **/
"use strict";

var grantAuthEmployeeGridDs, grantAuthEmployeeGridOpt, grantAuthEmployeeGrid;
var pageParam = parent.POP_PARAM["parameter"];

$(document).ready(function() {
    fnInitialize();	//Initialize Page Call ---------------------------------

    //Initialize PageR
    function fnInitialize(){
        $scope.$emit("fnIsMenu", { flag : false });

        fnPageInfo({
            PG_ID  : "grantAuthEmployeeSearchPopup",
            callback : fnUI
        });
    };

    // 화면 UI 초기화
    function fnUI(){

        fnInitButton();	// Initialize Button  ---------------------------------
        fnInitOptionBox(); // Initialize Option Box ------------------------------------
        fnInitGrid(); // 그리드 초기화
        fnDefaultValue(); // 기본값 셋팅
    };

    //--------------------------------- Button Start---------------------------------

    // 버튼 초기화
    function fnInitButton(){
        $("#fnSearch, #fnClear").kendoButton();
    };

    // 팝업 닫기
    function fnClose(params){
        if(params){
            parent.POP_PARAM = params;
        }
        parent.LAYER_POPUP_OBJECT.data("kendoWindow").close();
    };

    // 조회
    function fnSearch(){
        let data = $("#searchForm").formSerialize(true);

        grantAuthEmployeeGridDs.read(data);
    };

    //--------------------------------- Button End---------------------------------


    //------------------------------- Grid Start -------------------------------
    // 그리드 초기화
    function fnInitGrid(){
        grantAuthEmployeeGridDs = fnGetDataSource({
            url : "/admin/comn/popup/getGrantAuthEmployeeList"
        });

        grantAuthEmployeeGridOpt = {
            dataSource : grantAuthEmployeeGridDs,
            navigatable : true,
            scrollable : true,
            height: 350,
            columns : [
                        { field : "employeeNumber", title : "ID", width : "60px", attributes : {style : "text-align:center"} }
                      , { field : "employeeName", title : "직원명", width : "80px", attributes : {style : "text-align:center"} }
                      , { field : "organizationName", title : "조직정보", width : "150px", attributes : {style : "text-align:center"} }
                      , { field : "regalName", title : "법인명", width : "80px", attributes : {style : "text-align:center"} }
                      , { title : "", width : "100px", attributes : { style:"text-align:center", class : "forbiz-cell-readonly #:#" },
                          command: { text : "선택", click : function(e) {
                              e.preventDefault();

                              let tr = $(e.target).closest("tr");
                              let rowData = this.dataItem(tr);

                              fnClose(rowData);
                          }}
                        }
                      , { field : "organizationCode", hidden:true }
                      , { field : "regalCode", hidden:true }
                      , { field : "userId", hidden:true }
                      , { field : "companyId", hidden:true }
                      , { field : "positionName", hidden:true }
                      , { field : "teamLeaderYn", hidden:true }
                      , { field : "personalInfoAccessYn", hidden:true }
                      ]
        };

        grantAuthEmployeeGrid = $("#grantAuthEmployeeGrid").initializeKendoGrid(grantAuthEmployeeGridOpt).cKendoGrid();
    };

    //------------------------------- Grid End -------------------------------

    //---------------Initialize Option Box Start ------------------------------------------------

    // 옵션 초기화
    function fnInitOptionBox(){
        $("#kendoPopup").kendoWindow({
            visible: false,
            modal: true
        });

        // 담당자 검색 조건
        fnKendoDropDownList(
        {
          id : "searchCondition",
          data : [ {"CODE" : "NAME", "NAME" : "담당자명"}
                 , {"CODE" : "ID", "NAME" : "담당자ID"}
                 ],
          valueField : "CODE",
          textField : "NAME"
        });

        // 법인정보
        fnKendoDropDownList({
            id    : "regal",
            tagId : "regal",
            url : "/admin/user/employee/getPulmuoneRegalListWithoutPaging",
            textField :"erpRegalName",
            valueField : "erpRegalCode",
            chkVal : "",
            blank: "전체"
        });

        // 조직정보
        fnKendoDropDownList({
            id    : "organization",
            url : "/admin/user/employee/getPulmuoneOrganizationList",
            tagId : "organization",
            textField :"erpOrganizationName",
            valueField : "erpOrganizationCode",
            cscdId : "regal",
            cscdField : "erpRegalCode",
            chkVal : "",
            blank : "전체"
        });

    };

    //---------------Initialize Option Box End ------------------------------------------------


    //-------------------------------  Common Function start -------------------------------

    // 초기화
    function fnClear() {
        $("#searchForm").formClear(true);
        $("#grantAuthEmployeeGrid").data("kendoGrid").dataSource.data([]);
    };

    // 파라미터 셋팅
    function fnDefaultValue(){

        if( pageParam != null && Object.keys( pageParam ).length > 0 ){
            // 담당자 검색 조건
            if( pageParam.searchCondition != undefined){
                let searchConditionDropDown = $("#searchCondition").data("kendoDropDownList");
                searchConditionDropDown.select( pageParam.searchCondition );
                searchConditionDropDown.trigger("change");
                searchConditionDropDown.readonly();

                // 담당자 검색 키워드
                if( pageParam.findKeyword != undefined){
                    $("#findKeyword").val( pageParam.findKeyword );
                }
            }

            // 법인 정보
            if( pageParam.regalCode != undefined){
                let regalDropDown = $("#regal").data("kendoDropDownList");
                regalDropDown.select( pageParam.regalCode );
                regalDropDown.trigger("change");
                regalDropDown.readonly();


                // 조직 정보
                if( pageParam.organizationCode != undefined){
                    let organizationDropDown = $("#organization").data("kendoDropDownList");
                    organizationDropDown.select( pageParam.organizationCode );
                    organizationDropDown.readonly();
                }
            }

            $("#fnClear").attr("disabled", true);
        }
    };

    //-------------------------------  Common Function end -------------------------------


    //------------------------------- Html 버튼 바인딩  Start -------------------------------

    $scope.fnSearch = function() { fnSearch(); }; /* 조회 */
    $scope.fnClear = function() { fnClear(); }; /* 초기화 */
    $scope.fnClose = function(){ fnClose(); }; // 닫기

    //------------------------------- Html 버튼 바인딩  End -------------------------------

}); // document ready - END
