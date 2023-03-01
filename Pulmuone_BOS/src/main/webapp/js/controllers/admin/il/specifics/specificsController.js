﻿/**-----------------------------------------------------------------------------
 * description 		 : 상품정보고시 관리
 * @
 * @ 수정일			수정자          수정내용
 * @ ------------------------------------------------------
 * @ 2020.07.17		박영후          최초생성
 * @ 2020.10.12     손진구          구조변경
 * @
 * **/
"use strict";

var viewModel;
var specificsMasterGridOpt, specificsMasterGrid;
var specificsMasterFieldGridOpt, specificsMasterFieldGrid;

$(document).ready(function() {
	fnInitialize();

	//Initialize PageR
	function fnInitialize(){
		$scope.$emit('fnIsMenu', { flag : 'true' });

		fnPageInfo({
			PG_ID  : "specifics",
			callback : fnUI
		});
	};

	function fnUI(){
		fnInitButton();
		fnInitOptionBox();
		fnInitViewModel();
		fnInitGrid();
		viewModel.fnSpecificsMasterSearch();
	};

	//--------------------------------- Button Start ---------------------------------

	// 버튼 초기화
	function fnInitButton(){
		$("#fnNewCreateSpecificsMaster, #fnDeleteSpecificsMaster, #fnSave").kendoButton();
	};

	//--------------------------------- Button End ---------------------------------

	//--------------------------------- OptionBox Start ---------------------------------

	// 옵션 초기화
	function fnInitOptionBox(){

	    // 사용여부 Radio
        fnTagMkRadio({
            id :  "useYn",
            tagId : "useYn",
            data : [ { "CODE":"Y", "NAME":"예" },
                     { "CODE":"N", "NAME":"아니오" }
                   ]
        });

        // 사용여부 이벤트 연결
        $("input:radio[name=useYn]").attr("data-bind","checked: specificsMasterInfo.useYn");
	};

	//--------------------------------- OptionBox End ---------------------------------

	//--------------------------------- Grid Start ---------------------------------

	// 그리드 초기화
    function fnInitGrid(){

        specificsMasterGridOpt = {
              dataSource: viewModel.specificsMasterList
            , pageable : false
            , navigatable: true
            , columns :
              [
                { title : "NO", width : "50px", attributes : {style : "text-align:center"}, template : "<span class='row-number'></span>" }
              , { field : "specificsMasterName", title : "상품군명", width : "160px", attributes : {style:"text-align:center"} }
              , { field : "sort", title : "노출순서", width : "50px", attributes : {style:"text-align:center"} }
              , { field : "useYnName", title : "사용여부", width : "50px", attributes : {style:"text-align:center"},
                  template : function(dataItem){
                      return dataItem.useYn == "Y" ? "예" : "아니오";
                  }
                }
              ]
        };

        specificsMasterGrid = $("#specificsMasterGrid").initializeKendoGrid( specificsMasterGridOpt ).cKendoGrid();

        specificsMasterGrid.bind("dataBound", function(e){
            let rowNum = e.sender.dataSource.view().length;

            $("#specificsMasterGrid tbody > tr").each(function(index, item){
                // 순번 설정
                $(item).find(".row-number").html(rowNum);
                rowNum--;
            });
        });

        // 상품군 선택
        $("#specificsMasterGrid tbody").on("click", "tr", function(e) {
            e.preventDefault();

            let specificsMaster = specificsMasterGrid.dataItem(this);

            viewModel.specificsMasterInfo.set("specificsMasterId", specificsMaster.specificsMasterId);
            viewModel.specificsMasterInfo.set("specificsMasterCode", specificsMaster.specificsMasterCode);
            viewModel.specificsMasterInfo.set("specificsMasterName", specificsMaster.specificsMasterName);
            viewModel.specificsMasterInfo.set("sort", specificsMaster.sort);
            viewModel.specificsMasterInfo.set("useYn", specificsMaster.useYn);

            viewModel.set("specificsMasterDeleteButtonDisabled", false);
            viewModel.set("specificsMasterNewCreateYn", false);

            let param = {"specificsMasterId" : viewModel.specificsMasterInfo.specificsMasterId};
            viewModel.specificsMasterFieldList.read( param );
        });

        specificsMasterFieldGridOpt = {
              dataSource: viewModel.specificsMasterFieldList
            , pageable  : false
            , navigatable: true
            , selectable: false
            , columns :
              [
                { field : "specificsFieldName", title : "정보고시 항목", attributes : {style:"text-align:left"},
                  template : function(dataItem){
                      if( !fnIsEmpty(dataItem.specificsFieldCode) ){
                          return "<div style='float:left;color:blue'>" + dataItem.specificsFieldName + "</div><div class='grab-icon' style='float:right; cursor:grab'>▒</div>";
                      }else{
                          return "<div style='float:left'>" + dataItem.specificsFieldName + "</div><div class='grab-icon' style='float:right; cursor:grab'>▒</div>";
                      }
                  }
                }
              ]
        };

        specificsMasterFieldGrid = $('#specificsMasterFieldGrid').initializeKendoGrid( specificsMasterFieldGridOpt ).cKendoGrid();

        fnKendoGridSortByDragDrop("specificsMasterFieldGrid");
    };

	//--------------------------------- Grid End ---------------------------------

	//--------------------------------- Common Function start ---------------------------------

	// viewModel 초기화
	function fnInitViewModel(){
	    viewModel = new kendo.data.ObservableObject({
	        specificsMasterInfo : { // 상품군정보제공고시분류 상세
	            specificsMasterId : null, // 상품정보제공고시분류 PK
	            specificsMasterCode : "", // 상품정보제공고시분류 코드
	            specificsMasterName : "", // 상품정보제공고시분류 명
	            sort : null, // 노출순서
	            useYn : "Y" // 사용여부
            },
            specificsMasterList : new kendo.data.DataSource({ // 상품군정보제공고시분류 리스트
                transport:{
                    read: {
                        dataType : "json",
                        type     : "POST",
                        url      : "/admin/goods/specifics/getSpecificsMasterList",
                        beforeSend: function(req) {
                            req.setRequestHeader('authMenuID', CLICKED_MENU_ID);
                        }
                    }, parameterMap: function(data) {
                        data["isAction"]   = 'select';
                     return data;
                   }
                },
                schema: {
                    data: function(response) {
                        return response.data.specificsMasterList;
                    }
                },
                requestEnd: function(e) {
                    if( e.type === "read" && e.response != undefined ){
                        fnInitSpecificsMasterInfoAndField();
                    }
                }
            }),
            specificsMasterFieldList : new kendo.data.DataSource({ // 상품정보제공고시 분류 항목 관계 리스트
                transport:{
                    read: {
                        dataType : "json",
                        type     : "POST",
                        url      : "/admin/goods/specifics/getSpecificsMasterFieldList",
                        beforeSend: function(req) {
                            req.setRequestHeader('authMenuID', CLICKED_MENU_ID);
                        }
                    }, parameterMap: function(data) {
                        data["isAction"]   = 'select';
                     return data;
                   }
                },
                schema: {
                    data: function(response) {
                        return response.data.specificsMasterFieldList;
                    }
                },
                requestEnd: function(e) {
                    if( e.type === "read" && e.response != undefined ){
                        viewModel.originalSpecificsMasterFieldList = e.response.data.specificsMasterFieldList;
                    }
                }
            }),
            originalSpecificsMasterFieldList : [], // 원본 상품정보제공고시 분류 항목 관계 리스트
            specificsMasterNewCreateYn : true, // 상품군 신규등록 유무
            specificsMasterDeleteButtonDisabled : true, // 상품군 삭제 Disabled
            fnSpecificsFieldPopup : function(e){ // 정보고시 항목관리 팝업

                fnKendoPopup({
                    id: "fnSpecificsFieldPopup",
                    title: "상품정보고시 상세항목 관리",
                    src: "#/specificsField",
                    width: "1350px",
                    height: "600px",
                    success: function(id, data) {
                    }
                });
            },
            fnSpecificsMasterSearch : function(){ //상품군 조회
                viewModel.specificsMasterList.read();
            },
            fnNewCreateSpecificsMaster : function(e){ // 상품군 신규등록
                e.preventDefault();
                fnInitSpecificsMasterInfoAndField();
            },
            fnDeleteSpecificsMaster : function(e){ // 상품군 삭제
                e.preventDefault();

                if( fnItemSpecificsUseYn() ){

                    fnKendoMessage({message: "해당 상품군이 선택된 상품이 존재합니다. 정보변경 후 삭제가 가능합니다."});
                }else{

                    fnKendoMessage({message: "삭제 하시겠습니까?", type : "confirm", ok :fnDelete  });
                }
            },
            fnSpecificMasterFieldPopup : function(e){ // 정보고시 항목선택 팝업
                e.preventDefault();

                const specificsMasterFieldList = viewModel.specificsMasterFieldList.data();

                let params = {};
                params.specificsMasterId = viewModel.specificsMasterInfo.specificsMasterId;
                params.specificsMasterFieldList = specificsMasterFieldList.map(specificsMasterFieldInfo => specificsMasterFieldInfo.specificsFieldId);

                fnKendoPopup({
                    id: "fnSpecificMasterFieldPopup",
                    title: "상품정보고시 항목 수정",
                    param: params,
                    src: "#/specificMasterFieldAll",
                    width: "800px",
                    height: "620px",
                    success: function(id, data) {
                        if( data.specificsMasterFieldParams != undefined ){
                            fnSetSpecificsMasterField( data.specificsMasterFieldParams );
                        }
                    }
                });
            },
            fnSave : function(e){ // 저장
                e.preventDefault();

                if( fnValid() ){

                    // 삭제 할 품목별 상품정보제공고시 값 리스트 셋팅
                    let delItemSpecificsValueList = new Array();
                    let originalSpecificsMasterFieldList = viewModel.originalSpecificsMasterFieldList;
                    let specificsMasterFieldList = viewModel.specificsMasterFieldList.view();

                    if( originalSpecificsMasterFieldList.length > 0){
                        for(let i=0, rowCount=originalSpecificsMasterFieldList.length; i<rowCount; i++){
                            let delItemSpecificsValueYn = specificsMasterFieldList.some( specificsMasterFieldInfo =>
                                                            specificsMasterFieldInfo.specificsFieldId == originalSpecificsMasterFieldList[i].specificsFieldId );

                            if( !delItemSpecificsValueYn && originalSpecificsMasterFieldList[i].specificsMasterFieldUseYn ){
                                delItemSpecificsValueList.push(originalSpecificsMasterFieldList[i]);
                            }
                        }
                    }

                    // 저장 실행
                    let paramData = {};
                    paramData = viewModel.specificsMasterInfo;
                    paramData.specificsMasterFieldList = specificsMasterFieldList;

                    if( delItemSpecificsValueList.length > 0 ){
                        paramData.delItemSpecificsValueList = delItemSpecificsValueList;
                    }

                    fnAjax({
                        url     : "/admin/goods/specifics/putSpecificsMaster",
                        params  : paramData,
                        contentType : "application/json",
                        success : function( data ){
                            fnKendoMessage({  message : "저장되었습니다.", ok : viewModel.fnSpecificsMasterSearch });
                        },
                        error : function(xhr, status, strError){
                            fnKendoMessage({ message : xhr.responseText });
                        },
                        isAction : "insert"
                    });
                }
            }
        });

        kendo.bind($("#inputForm"), viewModel);
	};

	// 상품군별 분류정보 초기화
	function fnInitSpecificsMasterInfoAndField(){
	    specificsMasterGrid.clearSelection();

	    viewModel.specificsMasterInfo.set("specificsMasterId", null);
	    viewModel.specificsMasterInfo.set("specificsMasterCode", "");
	    viewModel.specificsMasterInfo.set("specificsMasterName", "");
	    viewModel.specificsMasterInfo.set("sort", null);
	    viewModel.specificsMasterInfo.set("useYn", "Y");
	    viewModel.set("specificsMasterDeleteButtonDisabled", true);
	    viewModel.set("specificsMasterNewCreateYn", true);

	    viewModel.specificsMasterFieldList.data([]);
	    viewModel.originalSpecificsMasterFieldList = [];
	};

	// 선택된 정보고시항목 셋팅
	function fnSetSpecificsMasterField( specificsMasterFieldParams ){
	    let specificsMasterFieldList = viewModel.specificsMasterFieldList.data();

	    // 정보고시 항목 삭제
        if(specificsMasterFieldList.length > 0) {
            for(let i=specificsMasterFieldList.length-1; i >= 0; i--){
                let specificsFieldIds = specificsMasterFieldParams.filter(function(specificsMasterFieldParamInfo){
                    return specificsMasterFieldParamInfo.specificsFieldId == specificsMasterFieldList[i].specificsFieldId;
                });

                if( Object.keys(specificsFieldIds).length == 0 ){
                    viewModel.specificsMasterFieldList.remove(specificsMasterFieldList[i]);
                }
            }
        }

        // 정보고시 항목 추가
        for(let i=0, rowCount=specificsMasterFieldParams.length; i < rowCount; i++){
            let specificsFieldIds = specificsMasterFieldList.filter(function(specificsMasterFieldInfo){
                return specificsMasterFieldInfo.specificsFieldId == specificsMasterFieldParams[i].specificsFieldId;
            });

            if( Object.keys(specificsFieldIds).length == 0 ){
                viewModel.specificsMasterFieldList.add(specificsMasterFieldParams[i]);
            }
        }

        // 신규 일 경우 등록일 기준으로 정렬
        if( viewModel.specificsMasterNewCreateYn ){
            viewModel.specificsMasterFieldList.sort({ field: "createDate", dir: "asc" });
        }
	};

    // 상품에 상품정보제공고시분류 적용 유무
    function fnItemSpecificsUseYn(){
        let itemSpecificsMasterUseYn = false;

        let paramData = {};
        paramData.specificsMasterId = viewModel.specificsMasterInfo.specificsMasterId;

        fnAjax({
            url     : "/admin/goods/specifics/getItemSpecificsMasterUseYn",
            params  : paramData,
            async : false,
            success : function( data ){

                itemSpecificsMasterUseYn = data.itemSpecificsMasterUseYn;
            },
            error : function(xhr, status, strError){
                fnKendoMessage({ message : xhr.responseText });
            },
            isAction : "select"
        });

        return itemSpecificsMasterUseYn;
    };

    // 삭제 실행
    function fnDelete(){
        let paramData = {};
        paramData.specificsMasterId = viewModel.specificsMasterInfo.specificsMasterId;

        fnAjax({
            url : "/admin/goods/specifics/delSpecificsMaster",
            params : paramData,
            success : function( response ){
                fnKendoMessage({ message : "삭제되었습니다.", ok : viewModel.fnSpecificsMasterSearch });
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

        if( viewModel.specificsMasterFieldList.data().length == 0 ){
            fnKendoMessage({ message : "해당 상품군에 노출한 상세항목을 선택해주세요." });
            return false;
        }

        if( fnSpecificsMasterNameDuplicateYn() ){
            fnKendoMessage({ message : "이미 등록된 상품군명이 존재합니다." });
            return false;
        }

        return true;
    };

    // 상품군명 중복 유무
    function fnSpecificsMasterNameDuplicateYn(){
        let specificsMasterNameDuplicateYn = false;

        let paramData = {};
        paramData.specificsMasterName = viewModel.specificsMasterInfo.specificsMasterName;

        if( !viewModel.specificsMasterNewCreateYn ){
            paramData.specificsMasterId = viewModel.specificsMasterInfo.specificsMasterId;
        }

        fnAjax({
            url     : "/admin/goods/specifics/getSpecificsMasterNameDuplicateYn",
            params  : paramData,
            async : false,
            success : function( data ){

                specificsMasterNameDuplicateYn = data.specificsMasterNameDuplicateYn;
            },
            error : function(xhr, status, strError){
                fnKendoMessage({ message : xhr.responseText });
            },
            isAction : "select"
        });

        return specificsMasterNameDuplicateYn;
    };

	//--------------------------------- Common Function  End ---------------------------------

}); // document ready - END
