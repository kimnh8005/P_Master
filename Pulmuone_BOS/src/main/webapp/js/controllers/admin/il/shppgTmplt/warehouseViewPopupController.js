﻿﻿/**-----------------------------------------------------------------------------
 * system           : 배송정책 설정 > 출고처 보기
 * @
 * @ 수정일         수정자          수정내용
 * @ ------------------------------------------------------
 * @ 2020.08.07     손진구          최초생성
 * @
 * **/
"use strict";

var viewModel, warehouseGridOpt, warehouseGrid;
var paramData = parent.POP_PARAM["parameter"];

$(document).ready(function() {

    fnInitialize(); //Initialize Page Call ---------------------------------

    //Initialize PageR
    function fnInitialize(){
        $scope.$emit("fnIsMenu", { flag : false });

        fnPageInfo({
            PG_ID  : "warehouseViewPopup",
            callback : fnUI
        });
    };

    // 화면 UI 초기화
    function fnUI(){

        fnViewModelInit();
        fnSetData();
    };

    //--------------------------------- Button Start---------------------------------

    // 닫기버튼 함수
    function fnClose() {
        parent.LAYER_POPUP_OBJECT.data("kendoWindow").close();
    };
    //--------------------------------- Button End---------------------------------

    //-------------------------------  Common Function start -------------------------------

    // ViewModel 초기화
    function fnViewModelInit(){

        viewModel = new kendo.data.ObservableObject({
            shippingTemplateInfo : { // 배송정책설정
                shippingTemplateName : "",
                conditionTypeName : "",
                shippingPrice : 0,
                claimShippingPrice : 0,
                bundleYn : "",
                areaShippingDeliveryYn : "",
                areaShippingYn : ""
            },
            warehouseList : [], // 출고처 리스트
            fnGetShippingPrice : function(){ // 배송비 View
                if( viewModel.shippingTemplateInfo.shippingPrice == 0 ){
                    return "무료";
                }else{
                    return kendo.format("{0:n0}", viewModel.shippingTemplateInfo.shippingPrice) + " 원";
                }
            },
            fnGetClaimShippingPrice : function(){ // 클레임 배송비 View
                return kendo.format("{0:n0}", viewModel.shippingTemplateInfo.claimShippingPrice) + " 원";
            },
            fnGetBundleYn : function(){ // 합배송여부 View
                if( viewModel.shippingTemplateInfo.bundleYn == "Y" ){
                    return "합배송";
                }else{
                    return "합배송 제외";
                }
            },
            fnGetAreaShipping : function(){ // 지역별 배송비 View
                if( viewModel.shippingTemplateInfo.areaShippingDeliveryYn == "N" ){
                    return "배송 불가";
                }else{
                    if( viewModel.shippingTemplateInfo.areaShippingYn == "Y" ){
                        return "예";
                    }else{
                        return "아니오";
                    }
                }
            }
        });

        kendo.bind($("#searchForm"), viewModel);
    };

    // 데이터 셋팅
    function fnSetData(){
        let searchParam = {};

        searchParam.shippingTemplateId = paramData.shippingTemplateId;

        fnAjax({
            url     : "/admin/policy/shippingtemplate/getShippingTemplate",
            params  : searchParam,
            contentType : "application/json",
            success :
                function( data ){
                    viewModel.set("shippingTemplateInfo", data.shippingTemplateInfo);
                    viewModel.set("warehouseList", data.shippingWarehouseList);
                },
            isAction : "select"
        });
    };

    //-------------------------------  Common Function end -------------------------------

    //------------------------------- Html 버튼 바인딩  Start -------------------------------
    $scope.fnClose = function() { fnClose(); };
    //------------------------------- Html 버튼 바인딩  End -------------------------------
}); // document ready - END
