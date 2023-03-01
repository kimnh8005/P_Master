/**-----------------------------------------------------------------------------
 * description       : 상품정보고시 상세항목 관리 팝업
 * @
 * @ 수정일            수정자          수정내용
 * @ ------------------------------------------------------
 * @ 2020.07.17     박영후          최초생성
 * @ 2020.10.13     손진구          구조변경
 * @
 * **/
"use strict";

var viewModel, specificsFieldGridOpt, specificsFieldGrid;

$(document).ready(function() {
    fnInitialize();

    function fnInitialize(){
        $scope.$emit("fnIsMenu", { flag : false });

        fnPageInfo({
            PG_ID  : "specificsField",
            callback : fnUI
        });
    };

    function fnUI(){
        fnInitButton();
        fnInitViewModel();
        fnInitGrid();
        viewModel.fnSpecificsFieldSearch();
    };

    //--------------------------------- Button Start---------------------------------

    // 버튼 초기화
    function fnInitButton(){
        $("#fnNewCreateSpecificsField, #fnDeleteSpecificsField, #fnSave, #fnClose").kendoButton();
    };

    //--------------------------------- Button End---------------------------------


    //------------------------------- Grid Start -------------------------------

    // 그리드 초기화
    function fnInitGrid(){

        specificsFieldGridOpt = {
              dataSource : viewModel.specificsFieldList
            , navigatable: true
            , height : 460
            , scrollable : true
            , columns :
              [
                { title : "순번", width : "50px", attributes : {style : "text-align:center"}, template : "<span class='row-number'></span>" }
              , { field : "specificsFieldName", title : "정보고시 항목명", width : "150px", attributes : {style:"text-align:center"} }
              , { field : "basicValue", title : "정보고시 항목 기본값", width : "150px", attributes : {style:"text-align:center"} }
              , { field : "specificsDesc", title : "정보고시 항목설명", width : "150px", attributes : {style:"text-align:left"} }
              ]
        };

        specificsFieldGrid = $("#specificsFieldGrid").initializeKendoGrid( specificsFieldGridOpt ).cKendoGrid();

        specificsFieldGrid.bind("dataBound", function(e){
            let rowNum = e.sender.dataSource.view().length;
            let grid = $("#specificsFieldGrid").data("kendoGrid");

            $("#specificsFieldGrid tbody > tr").each(function(index, item){
                let dataItem = grid.dataItem(item);

                // 순번 설정
                $(item).find(".row-number").html(rowNum);
                rowNum--;

                // 상품정보제공고시항목코드 체크하여 폰트 색상 변경
                if( dataItem.specificsFieldCode ){
                    $(item).css("color", "blue");
                }
            });
        });

        // 상품정보고시 상세항목 선택
        $("#specificsFieldGrid tbody").on("click", "tr", function(e) {
            e.preventDefault();
            let specificsField = specificsFieldGrid.dataItem(this);

            viewModel.specificsFieldInfo.set("specificsFieldId", specificsField.specificsFieldId);
            viewModel.specificsFieldInfo.set("specificsFieldCode", specificsField.specificsFieldCode);
            viewModel.specificsFieldInfo.set("specificsFieldName", specificsField.specificsFieldName);
            viewModel.specificsFieldInfo.set("basicValue", specificsField.basicValue);
            viewModel.specificsFieldInfo.set("specificsDesc", specificsField.specificsDesc);

            viewModel.set("specificsFieldDeleteButtonDisabled", false);
            viewModel.set("specificsFieldNewCreateYn", false);
        });
    };

    //-------------------------------  Grid End  -------------------------------

    //-------------------------------  Common Function start -------------------------------

    // viewModel 초기화
    function fnInitViewModel(){
        viewModel = new kendo.data.ObservableObject({
            specificsFieldInfo : { // 상품정보제공고시항목 상세
                specificsFieldId : null, // 상품정보제공고시항목 PK
                specificsFieldCode : "", // 상품정보제공고시항목 코드
                specificsFieldName : "", // 상품정보제공고시항목 명
                basicValue : "상품 상세 정보를 참조하세요.", // 기본 값
                specificsDesc : "" // 상세 설명
            },
            specificsFieldList : new kendo.data.DataSource({ // 상품정보고시 상세항목 리스트
                transport:{
                    read: {
                        dataType : "json",
                        type     : "POST",
                        url      : "/admin/goods/specifics/getSpecificsFieldList",
                        beforeSend: function(req) {
                            req.setRequestHeader('authMenuID', CLICKED_MENU_ID);
                        }
                    }, parameterMap: function(data) {
                        data["isAction"]   = "select";
                     return data;
                   }
                },
                schema: {
                    data: function(response) {
                        return response.data.specificsFieldList;
                    }
                },
                requestEnd: function(e) {
                    if( e.type === "read" && e.response != undefined ){
                        fnInitSpecificsField();
                    }
                }
            }),
            specificsFieldNewCreateYn : true, // 상품정보고시 상세항목 신규등록 유무
            specificsFieldDeleteButtonDisabled : true, // 상품정보고시 상세항목 삭제 Disabled
            fnSpecificsFieldSearch : function(){ // 상품정보고시 상세항목 조회
                viewModel.specificsFieldList.read();
            },
            fnNewCreateSpecificsField : function(e){ // 신규등록
                e.preventDefault();
                fnInitSpecificsField();
            },
            fnDeleteSpecificsField : function(e){ // 삭제
                e.preventDefault();

                if( !fnIsEmpty(viewModel.specificsFieldInfo.specificsFieldCode) ){
                    fnKendoMessage({ message : "삭제가 불가한 고정 상세항목입니다." });
                    return;
                }

                if( fnItemSpecificsValueUseYn() ){

                    fnKendoMessage({message: "해당 분류정보가 등록된 상품이 존재합니다. 삭제하시겠습니까?", type : "confirm", ok : fnDelete });
                }else{

                    fnKendoMessage({message: "삭제 하시겠습니까?", type : "confirm", ok : fnDelete });
                }
            },
            fnSave : function(e){ // 저장
                e.preventDefault();

                if( fnValid() ){

                    fnAjax({
                        url     : "/admin/goods/specifics/putSpecificsField",
                        params  : viewModel.specificsFieldInfo,
                        contentType : "application/json",
                        success : function( data ){
                            let messageText = "";

                            if( viewModel.specificsFieldNewCreateYn ){
                                messageText = "저장되었습니다.";
                            }else{
                                messageText = "수정되었습니다.";
                            }

                            fnKendoMessage({ message : messageText, ok : viewModel.fnSpecificsFieldSearch });
                        },
                        error : function(xhr, status, strError){
                            fnKendoMessage({ message : xhr.responseText });
                        },
                        isAction : "update"
                    });
                }
            },
            fnClose : function(e){ // 닫기
                e.preventDefault();
                parent.LAYER_POPUP_OBJECT.data("kendoWindow").close();
            }
        });

        kendo.bind($("#inputForm"), viewModel);
    };

    // 상품정보고시 상세항목 초기화
    function fnInitSpecificsField(){
        specificsFieldGrid.clearSelection();

        viewModel.specificsFieldInfo.set("specificsFieldId", null);
        viewModel.specificsFieldInfo.set("specificsFieldCode", "");
        viewModel.specificsFieldInfo.set("specificsFieldName", "");
        viewModel.specificsFieldInfo.set("basicValue", "상품 상세 정보를 참조하세요.");
        viewModel.specificsFieldInfo.set("specificsDesc", "");

        viewModel.set("specificsFieldDeleteButtonDisabled", true);
        viewModel.set("specificsFieldNewCreateYn", true);
    };

    // 품목별 상품정보제공고시 값 사용유무
    function fnItemSpecificsValueUseYn(){
        let itemSpecificsValueUseYn = false;

        let paramData = {};
        paramData.specificsFieldId = viewModel.specificsFieldInfo.specificsFieldId;

        fnAjax({
            url     : "/admin/goods/specifics/getItemSpecificsValueUseYn",
            params  : paramData,
            async : false,
            success : function( data ){
                itemSpecificsValueUseYn = data.itemSpecificsValueUseYn;
            },
            error : function(xhr, status, strError){
                fnKendoMessage({ message : xhr.responseText });
            },
            isAction : "select"
        });

        return itemSpecificsValueUseYn;
    };

    // 삭제 실행
    function fnDelete(){
        let paramData = {};
        paramData.specificsFieldId = viewModel.specificsFieldInfo.specificsFieldId;

        fnAjax({
            url : "/admin/goods/specifics/delSpecificsField",
            params : paramData,
            success : function( data ){
                fnKendoMessage({ message : "삭제되었습니다.", ok : viewModel.fnSpecificsFieldSearch });
            },
            error : function(xhr, status, strError){
                fnKendoMessage({ message : xhr.responseText });
            },
            isAction : "delete"
        });
    };

    // 데이터 검증
    function fnValid(){
        let formValid = $("#inputForm").formSerialize(true);

        if( !formValid.rtnValid ){
            return false;
        }

        if( fnSpecificsFieldNameDuplicateYn() ){
            fnKendoMessage({ message : "이미 등록된 정보고시 항목명이 존재합니다." });
            return false;
        }

        return true;
    };

    // 정보고시항목명 중복 유무
    function fnSpecificsFieldNameDuplicateYn(){
        let specificsFieldNameDuplicateYn = false;

        let paramData = {};
        paramData.specificsFieldName = viewModel.specificsFieldInfo.specificsFieldName;

        if( !viewModel.specificsFieldNewCreateYn ){
            paramData.specificsFieldId = viewModel.specificsFieldInfo.specificsFieldId;
        }

        fnAjax({
            url     : "/admin/goods/specifics/getSpecificsFieldNameDuplicateYn",
            params  : paramData,
            async : false,
            success : function( data ){

                specificsFieldNameDuplicateYn = data.specificsFieldNameDuplicateYn;
            },
            error : function(xhr, status, strError){
                fnKendoMessage({ message : xhr.responseText });
            },
            isAction : "select"
        });

        return specificsFieldNameDuplicateYn;
    };

    //-------------------------------  Common Function end -------------------------------

}); // document ready - END