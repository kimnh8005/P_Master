/**-----------------------------------------------------------------------------
 * description 	: 배송정책 설정 > 도서산간/배송불가 권역 관리 조회
 * @
 * @ 수정일			수정자          수정내용
 * @ ------------------------------------------------------
 * @ 2021.10.12		남기승          최초생성
 * @
 * **/
'use strict';

var PAGE_SIZE = 10;
var bGridDs, bGridOpt, bGrid;
var publicStorageUrl = fnGetPublicStorageUrl();
var gFileTagId;
var gFile;


$(document).ready(function() {

    fnInitialize();	// Initialize Page Call ---------------------------------

    // Initialize PageR
    function fnInitialize(){
        $scope.$emit('fnIsMenu', { flag : false });         // 팝업의 경우 GNB영역 비노출 위해 false 로 해준다.

        fnPageInfo({
            PG_ID  : 'policyShippingAreaPopup',
            callback : fnUI
        });
    }

    function fnUI(){
        fnInitButton();	// Initialize Button ---------------------------------
        fnInitGrid();	// Initialize Grid ------------------------------------
        fnInitOptionBox();// Initialize Option Box
        fnSearch();
    }

    // --------------------------------- Button Start---------------------------------
    function fnInitButton(){
        $('#fnSearch').kendoButton();
    }

    // 엑셀 적용내역 조회
    function fnSearch(){
        var data = $('#searchForm').formSerialize(true);

        var query = {
            page         : 1,
            pageSize     : PAGE_SIZE,
            filterLength : fnSearchData(data).length,
            filter :  {
                filters : fnSearchData(data)
            }
        };
        bGridDs.query( query );
    }

    // 옵션 초기화
    function fnInitOptionBox(){

        // 엑셀적용내역 조회 권역 선택
        fnKendoDropDownList({
            id  : 'undeliverableTp',
            tagId : 'undeliverableTp',
            url   : "/admin/comn/getCodeList",
            params : {"stCommonCodeMasterCode" : "UNDELIVERABLE_TP", "useYn" :"Y"},
            textField :"NAME",
            valueField : "CODE",
            value : "",
            blank : "전체"
        });
    };

    // --------------------------------- Button End---------------------------------



    // ------------------------------- Grid Start -------------------------------
    function fnInitGrid(){
        bGridDs = fnGetPagingDataSource({
            url      : "/admin/policy/getShippingAreaInfoList",
            pageSize : PAGE_SIZE
        });
        bGridOpt = {
            dataSource: bGridDs
            , pageable  : { buttonCount : 5, responsive : false }
            , navigatable: true
            , columns   : [
                { field : "no", title : "No", width : "50px", attributes : { style:'text-align:center' }, template: "<span class='row-number'></span>" }
                , { field : "psShippingAreaExcelInfoId", hidden : true }
                , { field : "zipCd", title : "우편번호", width : "100px", attributes : {style : "text-align:center" }}
                , { field : "undeliverableNm", title: "권역", width : "100px", attributes : {style : "text-align:center"}}
                , { field : "keyword", title : "키워드 등록", width : "300px", attributes : { style : "text-align:center" }}
                , { field : "alternateDeliveryTp", title : "대체배송", width : "100px", attributes : { style : "text-align:center" }}
                , { field : "createNm", title : "등록자", width : "100px", attributes : { style : "text-align:center" }}
                , { field : "createDt", title : "등록일자", width : "100px", attributes : { style : "text-align:center" }}
            ]
        };

        bGrid = $('#bGrid').initializeKendoGrid( bGridOpt ).cKendoGrid();

        bGrid.bind("dataBound", function() {
            var row_num = bGridDs._total - ((bGridDs._page - 1) * bGridDs._pageSize);
            $("#bGrid tbody > tr .row-number").each(function(index){
                $(this).html(row_num);
                row_num--;
            });

            $('#totalCnt').text(bGridDs._total);
        });

    }
    // ------------------------------- Grid End -------------------------------

    // ------------------------------- Html 버튼 바인딩 Start
    // -------------------------------


    /** Common Search */
    $scope.fnSearch = function(){	fnSearch();	};

}); // document ready - END
