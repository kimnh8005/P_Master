﻿/**-----------------------------------------------------------------------------
 * system            : 거래처 검색 팝업
 * @
 * @ 수정일            수정자          수정내용
 * @ ------------------------------------------------------
 * @ 2020.07.31     손진구          최초생성
 * @
 * **/
"use strict";

var clientGridDs, clientGridOpt, clientGrid;
var pageParam = parent.POP_PARAM["parameter"];

$(document).ready(function() {
    fnInitialize();	//Initialize Page Call ---------------------------------

    //Initialize PageR
    function fnInitialize(){
        $scope.$emit("fnIsMenu", { flag : false });

        fnPageInfo({
            PG_ID  : "clientSearchPopup",
            callback : fnUI
        });
    };

    // 화면 UI 초기화
    function fnUI(){

        fnInitButton();	// Initialize Button  ---------------------------------
        fnInitOptionBox(); // Initialize Option Box ------------------------------------
        fnInitGrid();
        fnDefaultValue();
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
        parent.LAYER_POPUP_OBJECT.data('kendoWindow').close();
    };

    // 조회
    function fnSearch(){
        let data = $("#searchForm").formSerialize(true);

        clientGridDs.read(data);
    };

    //--------------------------------- Button End---------------------------------


    //------------------------------- Grid Start -------------------------------
    // 그리드 초기화
    function fnInitGrid(){
        clientGridDs = fnGetDataSource({
            url : "/admin/comn/popup/getClientList"
        });

        clientGridOpt = {
            dataSource : clientGridDs,
            navigatable : true,
            scrollable : true,
            height: 350,
            columns : [
                        { field : "clientId", title : "거래처코드", width : "60px", attributes : {style : "text-align:center"} }
                      , { field : "clientName", title : "거래처명", width : "100px", attributes : {style : "text-align:center"} }
                      , { field : "clientTypeName", title : "거래처유형", width : "100px", attributes : {style : "text-align:center"} }
                      , { title : "", width : "100px", attributes : { style:"text-align:center", class : "forbiz-cell-readonly #:#" },
                          command: { text : "선택", click : function(e) {
                              e.preventDefault();

                              let tr = $(e.target).closest("tr");
                              let rowData = this.dataItem(tr);

                              fnClose(rowData);
                          }}
                        }
                      , { field : "companyId", hidden:true }
                      , { field : "clientType", hidden:true }
                      , { field : "supplierId", hidden:true }
                      , { field : "storeId", hidden:true }
                      , { field : "channelId", hidden:true }
                      , { field : "erpCode", hidden:true }
                      ]
        };

        clientGrid = $("#clientGrid").initializeKendoGrid(clientGridOpt).cKendoGrid();
    };

    //------------------------------- Grid End -------------------------------

    //---------------Initialize Option Box Start ------------------------------------------------

    // 옵션 초기화
    function fnInitOptionBox(){
        $("#kendoPopup").kendoWindow({
            visible: false,
            modal: true
        });

        // 거래처 유형
        fnTagMkChkBox({
            id : "clientType",
            url : "/admin/comn/getCodeList",
            tagId : "clientType",
            style : {},
            async : false,
            params : {"stCommonCodeMasterCode" : "CLIENT_TYPE", "useYn" :"Y"},
            beforeData : [{ "CODE" : "", "NAME" : "전체" }],
            change : function(e) {

               var tagName = "clientType";
               var $checkbox = $("input[name=" + tagName + "]");
               var $chkAll = $checkbox.eq(0);
               var $siblings = $checkbox.not(":eq(0)");

               var checkedLength = $siblings.filter(function() {
                   return $(this).is(":checked");
               }).length;

               var isCheckedAll = checkedLength === $siblings.length;

               var checked = $(e).is(":checked");

                // 전체 선택
               if( e === $chkAll[0] ) {
                    $siblings.prop("checked", checked);
                    return;
               } 
                    
                // 체크되었을 경우
                if( checked && isCheckedAll ) {
                    $chkAll.prop("checked", true);
                } else {
                    // 해제 되었을 경우
                    $chkAll.prop("checked", false);
                }
                
                // if($("input[name=clientType]:eq(0)").is(":checked") == true){
                //     $("input[name=clientType]").each(function(idx) {
                //         if(idx != 0){
                //             $(this).prop("checked", true);
                //             $(this).prop("disabled", true);
                //         }
                //     });
                // } else {
                //     $("input[name=clientType]").each(function(idx) {
                //         if(idx != 0){
                //             $(this).prop("disabled", false);
                //         }
                //     });
                // }
            }
        });

        // 거래처 유형 전체선택
        $("input[name=clientType]").eq(0).prop("checked", true).trigger("change"); // 관리자유형 전체선택
    };

    //---------------Initialize Option Box End ------------------------------------------------


    //-------------------------------  Common Function start -------------------------------

    // 초기화
    function fnClear() {
        $("#searchForm").formClear(true);
        $("input[name=clientType]").eq(0).prop("checked", true).trigger("change"); // 거래처 유형 전체선택
        $("#clientGrid").data("kendoGrid").dataSource.data([]);
    };

    // 파라미터 셋팅
    function fnDefaultValue(){

        if( pageParam != null && Object.keys( pageParam ).length > 0 ){

            if( pageParam.clientCode != undefined){
                $("#clientCode").val( pageParam.clientCode );
                $("#clientCode, #fnClear").attr("disabled", true);
            }

            if( pageParam.clientName != undefined){
                $("#clientName").val( pageParam.clientName );
                $("#clientName, #fnClear").attr("disabled", true);
            }

            if( pageParam.clientTypeArr != undefined){
                if( ($("input[name=clientType]").length -1) != pageParam.clientTypeArr.length ){
                    $("input[name=clientType]").prop("checked", false).trigger("change");

                    pageParam.clientTypeArr.forEach(function( paramClientType ){
                        $("input[name=clientType]").each(function() {
                            if( paramClientType == $(this).val() ){
                                $(this).prop("checked", true);
                            }
                        });
                    });

                    $("input[name=clientType]").attr("disabled", true);
                }
            }
        }
    };

    //-------------------------------  Common Function end -------------------------------


    //------------------------------- Html 버튼 바인딩  Start -------------------------------

    $scope.fnSearch = function() { fnSearch(); }; /* 조회 */
    $scope.fnClear = function() { fnClear(); }; /* 초기화 */
    $scope.fnClose = function(){ fnClose(); }; // 닫기

    //------------------------------- Html 버튼 바인딩  End -------------------------------

}); // document ready - END
