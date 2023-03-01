/*******************************************************************************************************************************************************************************************************
 * -------------------------------------------------------- description : 마스터 품목 관리 - 가격 정보 전체이력 보기 팝업 * @ @ 수정일 수정자 수정내용 @ ------------------------------------------------------ * @ 2017.02.13 신혁 최초생성 @
 ******************************************************************************************************************************************************************************************************/
'use strict';

var PAGE_SIZE = 10;
var itemPriceHistGridOpt, itemPriceHistGrid, itemPriceHistGridDs;

var popupParameter = parent.POP_PARAM["parameter"]; // 팝업 파라미터 전역변수
var ilItemCode = popupParameter['ilItemCode']; // 모화면 ( 마스터 품목 수정 ) 의 품목코드

$(document).ready(function() {
    // Initialize Page Call
    fnInitialize();

    // Initialize PageR
    function fnInitialize() {
        $scope.$emit('fnIsMenu', {
            flag : false
        });

        fnPageInfo({
            PG_ID : 'item',
            callback : fnUI
        });
    };

    //전체화면 구성
    function fnUI() {
        fnTranslate(); // comm.lang.js 안에 있는 공통함수 다국어
        fnItemPriceHistoryGrid(); // 가격정보 전체 이력 Grid 세팅
        fnSearch(); // 조회
    };

    function fnSearch() {

        var data = $('#searchForm').formSerialize(true);

        data.ilItemCode = ilItemCode; // 품목코드 추가
        data.includePastPriceHistory = 'Y'; // 과거 가격이력까지 모두 조회

        var query = {
            page : 1,
            pageSize : PAGE_SIZE,
            filterLength : fnSearchData(data).length,
            filter : {
                filters : fnSearchData(data)
            }
        };

        itemPriceHistGridDs.query(query);
    };

    //가격정보 전체 이력 Grid
    function fnItemPriceHistoryGrid() {

        itemPriceHistGridDs = fnGetEditPagingDataSource({
            url : '/admin/item/master/modify/getItemPriceHistory',
            pageSize : PAGE_SIZE,
        });

        itemPriceHistGridOpt = {
            dataSource : itemPriceHistGridDs,
            pageable : {
                buttonCount : 5,
                responsive : false
            },
            editable : false,
            navigatable : true,
            columns : [
                {field : 'updateTypeName', title : '구분', width : '10%'}
                ,{field : 'approvalStatusCodeName', title : '승인상태', width : '10%'}
                ,{field : 'priceApplyStartDate', title : '시작일', width : '10%'}
                ,{field : 'priceApplyEndDate', title : '종료일', width : '10%' }
                ,{field : 'standardPrice', title : '원가', width : '10%' }
                ,{field : 'recommendedPrice', title : '정상가', width : '10%' }
                ,{field : 'priceRatio', title : '마진율', width : '10%' }
                ,{field : 'approvalRequestUserInfo', title : '승인요청자', width : '10%' }
                ,{field : 'approvalAdminUserInfo', title : '승인관리자(1차/최종)', width : '10%' }
                ],
            rowTemplate : kendo.template($("#rowTemplate").html()),

        }

        itemPriceHistGrid = $('#itemPriceHistGrid').initializeKendoGrid(itemPriceHistGridOpt).cKendoGrid();

    };

    // ------------------------------- Html 버튼 바인딩 Start
    // -------------------------------
    /** Common Search */
    $scope.fnSearch = function() {
        fnSearch();
    };

    $scope.fnClose = function() {
        fnClose();
    };

    // ------------------------------- Html 버튼 바인딩 End
    // -------------------------------

}); // document ready - END
