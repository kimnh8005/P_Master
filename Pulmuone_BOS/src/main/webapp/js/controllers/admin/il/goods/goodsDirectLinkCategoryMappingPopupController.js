/**-----------------------------------------------------------------------------
 * description 		 : 통계관리 > 쇼핑라이브 > 편성 콘텐츠 목록
 * @
 * @ 수정일			수정자          수정내용
 * @ ------------------------------------------------------
 * @ 2021.10.08		송지윤          최초생성
 * @
 * **/
'use strict';
// ----------------------- NEW
var LAST_PAGE = null;   // 최종 페이지 (페이징 기억 관련)
var PAGE_SIZE = 20;
var publicStorageUrl = fnGetPublicStorageUrl();
var pageParam = parent.POP_PARAM["parameter"];
// ----------------------------------------------------
var aGridDs, aGridOpt, aGrid, viewModel;

var gbPageParam = '';   // 넘어온 페이지파라미터
var gbMode      = '';

$(document).ready(function() {
    // ==========================================================================
    // # Initialize Page Call
    // ==========================================================================
    fnInitialize();

    // ==========================================================================
    // # Initialize PageR
    // ==========================================================================
    function fnInitialize(){
        $scope.$emit('fnIsMenu', { flag : false });

        fnPageInfo({
            PG_ID  : 'goodsDirectLinkCategoryMappingPopup',
            callback : fnUI
        });
    }

    // ==========================================================================
    // # fnUI
    // ==========================================================================
    function fnUI(){

        fnInitButton();		// Initialize Button ---------------------------------

        fnInitOptionBox();	// Initialize Option Box

    }

    // ==========================================================================
    // # 초기화 - 버튼
    // ==========================================================================
    function fnInitButton(){
        $('#fnSave').kendoButton();
    }

    // ==========================================================================
    // # 초기화 - 옵션박스
    // ==========================================================================
    function fnInitOptionBox(){

        // 표준카테고리 전체 내역
        $('#ilCtgryStdFullName').val(pageParam.ilCtgryStdFullName);

        var categoryId = '';
        if(pageParam.categoryId != 'undefined' && pageParam.categoryId != null && pageParam.categoryId != ''){
            categoryId = pageParam.categoryId;
        }

        // 표준카테고리 대분류
        fnKendoDropDownList({
            id : "categoryId1",
            tagId : "categoryId1",
            url : "/admin/goods/list/getIfNaverCategoryList",
            params : { "depth" : "1"}, // , "categoryId1":pageParam.categoryId
            textField : "name",
            valueField : "code",
            blank : "대분류",
            async : true
        });

        // 표준카테고리 중분류
        fnKendoDropDownList({
            id : "categoryId2",
            tagId : "categoryId2",
            url : "/admin/goods/list/getIfNaverCategoryList",
            params : { "depth" : "2" },
            textField : "name",
            valueField : "code",
            blank : "중분류",
            async : false,
            cscdId : "categoryId1",
            cscdField : "categoryId1"
        });

        // 표준카테고리 소분류
        fnKendoDropDownList({
            id : "categoryId3",
            tagId : "categoryId3",
            url : "/admin/goods/list/getIfNaverCategoryList",
            params : { "depth" : "3" },
            textField : "name",
            valueField : "code",
            blank : "소분류",
            async : false,
            cscdId : "categoryId2",
            cscdField : "categoryId2"
        });

        // 표준카테고리 세분류
        fnKendoDropDownList({
            id : "categoryId4",
            tagId : "categoryStandardDepth4",
            url : "/admin/goods/list/getIfNaverCategoryList",
            params : { "depth" : "4" },
            textField : "name",
            valueField : "code",
            blank : "세분류",
            async : false,
            cscdId : "categoryId3",
            cscdField : "categoryId3"
        });
    };

    // ==========================================================================
    // # 저장
    // ==========================================================================
    function fnSave(){

        var isAction = '';
        var url = '';
        var nvCtgryStdId = pageParam.categoryId;
        var data = new Object();

        // 등록/수정 구분
        if(nvCtgryStdId != '' && nvCtgryStdId != undefined && nvCtgryStdId != null){
            url = '/admin/goods/list/putGoodsDirectLinkCategoryMapping';
            isAction = 'update';
        }else{
            url = '/admin/goods/list/addGoodsDirectLinkCategoryMapping';
            isAction = 'insert';
        }

        // 네이버카테고리 맵핑대상 ID
        var depth = 0;
        depth = $('input[name=categoryId1]').val()=='undefined'?0:1;
        depth = $('input[name=categoryId2]').val()=='undefined'?0:2;
        depth = $('input[name=categoryId3]').val()=='undefined'?0:3;
        depth = $('input[name=categoryId4]').val()=='undefined'?0:4;

        data.categoryId = $('input[name=categoryId'+parseInt(depth)+']').val();
        data.ilCtgryId = pageParam.ilCtgryStdId;

        fnAjax({
            url     : url,
            params  : data,
            async   : true,
            contentType: "application/json",
            success :
                function( data ){
                    fnBizCallback('confirm', data);
                },
            fail : function(resultData, resultCode){
                fnKendoMessage({
                    message : resultCode.message,
                    ok : function(e) {
                        fnBizCallback("fail", resultCode);
                    }
                });
            },
            isAction : isAction
        });
    }

    // ==========================================================================
    // # 콜백합수
    // ==========================================================================
    function fnBizCallback( id, data ){
        switch(id) {
            case 'confirm':
                fnKendoMessage({
                    message: "저장되었습니다.",
                    ok: function () {
                        fnClose();
                    }
                });
        }
    }


    // ==========================================================================
    // # 팝업닫음
    // ==========================================================================
    function fnClose() {
        parent.LAYER_POPUP_OBJECT.data("kendoWindow").close();
    }
    // ------------------------------- Grid Start -------------------------------

    // ------------------------------- Html 버튼 바인딩 Start
    /** Common Clear */
    $scope.fnSave = function(){	 fnSave();	};

    $scope.fnClose = function() { fnClose(); };
    // ------------------------------- Html 버튼 바인딩 End

}); // document ready - END
