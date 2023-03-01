﻿/**-----------------------------------------------------------------------------
 * description 		 : 상품정보고시 분류항목 수정
 * @
 * @ 수정일			수정자          수정내용
 * @ ------------------------------------------------------
 * @ 2020.07.17		박영후          최초생성
 * @ 2020.10.13     손진구          구조변경
 * @
 * **/
"use strict";

var viewModel, specificsFieldGridOpt, specificsFieldGrid;
var pageParam = parent.POP_PARAM["parameter"]; // 파라미터

$(document).ready(function() {
    fnInitialize(); //Initialize Page Call ---------------------------------

    //Initialize PageR
    function fnInitialize(){
        $scope.$emit("fnIsMenu", { flag : false });

        fnPageInfo({
            PG_ID  : "specificMasterFieldAll",
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
        $("#fnSave").kendoButton();
    };

	//--------------------------------- Button End---------------------------------


	//------------------------------- Grid Start -------------------------------

    // 그리드 초기화
    function fnInitGrid(){

        specificsFieldGridOpt = {
              dataSource : viewModel.specificsFieldList
            , height : 530
            , scrollable : true
            , navigatable: true
            , selectable : false
            , columns :
              [
                { template : '<input type="checkbox" name="rowCheck" class="k-checkbox" />',
                  width : "35px", attributes : {style : "text-align:center;"} }
              , { title : "순번", width : "50px", attributes : {style : "text-align:center"}, template : "<span class='row-number'></span>" }
              , { field : "specificsFieldName", title : "정보고시 항목명", width : "150px", attributes : {style:"text-align:center"} }
              , { field : "basicValue", title : "정보고시 항목 기본값", width : "150px", attributes : {style:"text-align:center"} }
              , { field : "specificsDesc", title : "정보고시 항목설명", width : "150px", attributes : {style:"text-align:left"} }
            ]
        };

        specificsFieldGrid = $("#specificsFieldGrid").initializeKendoGrid( specificsFieldGridOpt ).cKendoGrid();

        specificsFieldGrid.table.on("click", "[name=rowCheck]", fnRowCheckClick);

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

                // 선택된 상품정보제공고시항목코드 체크박스 체크
                if( pageParam.specificsMasterFieldList.indexOf(dataItem.specificsFieldId) > -1 ){
                    $(item).find("[name=rowCheck]").prop("checked", true);
                }
            });
        });
    };

	//-------------------------------  Grid End  -------------------------------


	//-------------------------------  Common Function start -------------------------------

    // viewModel 초기화
    function fnInitViewModel(){
        viewModel = new kendo.data.ObservableObject({
            specificsMasterInfo : { // 상품군정보제공고시분류 상세
                specificsMasterId : null // 상품정보제공고시분류 PK
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
                }
            }),
            fnSpecificsFieldSearch : function(){ // 상품정보고시 상세항목 조회
                let params = {};
                params.specificsMasterId = pageParam.specificsMasterId;
                params.specificsFieldIds = pageParam.specificsMasterFieldList.join(",");

                viewModel.specificsFieldList.read(params);
            },
            fnSave : function(e){ // 저장
                e.preventDefault();

                let params = [];

                if( $("input[name=rowCheck]:checked").length == 0 ){
                    fnKendoMessage({ message : "선택된 정보고시 항목이 없습니다." });
                    return;
                }

                let selectRows  = specificsFieldGrid.tbody.find("input[name=rowCheck]:checked").closest("tr");

                for(let i = 0, selectCount = selectRows.length; i < selectCount; i++){
                    let dataItem = specificsFieldGrid.dataItem($(selectRows[i]));
                    params[i] = dataItem;
                }

                parent.POP_PARAM.specificsMasterFieldParams = params;
                parent.LAYER_POPUP_OBJECT.data("kendoWindow").close();
            }
        });

        kendo.bind($("#inputForm"), viewModel);
    };

    // 상품정보고시 정보고시 항목 체크박스 클릭
    function fnRowCheckClick(e){
        let grid = $("#specificsFieldGrid").data("kendoGrid");
        let dataItem = grid.dataItem( $(this).closest("tr") );

        if( !e.target.checked && dataItem.specificsMasterFieldUseYn ){
            fnKendoMessage({
                type    : "confirm",
                message : "상세항목 체크 해제시 이미 등록된 품목정보의 상세항목 내용이 삭제될 수 있습니다, 항목을 제외하시겠습니까?",
                ok      : function(){

                },
                cancel  : function(){
                    e.target.checked = true;
                }
            });
        }
    };

	//-------------------------------  Common Function end -------------------------------

}); // document ready - END
