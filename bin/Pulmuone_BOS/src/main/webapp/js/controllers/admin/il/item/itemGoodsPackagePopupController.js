/*******************************************************************************************************************************************************************************************************
 * -------------------------------------------------------- description : 마스터 품목 관리 - 묶음상품보기 보기 팝업 * @ @ 수정일 수정자 수정내용 @ ------------------------------------------------------ * @ 2017.02.13 신혁 최초생성 @
 ******************************************************************************************************************************************************************************************************/
'use strict';

var PAGE_SIZE = 10;
var itemGoodsPackageGridOpt, itemPriceHistGrid, itemGoodsPackageGridDs;

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
            PG_ID : 'itemGoodsPackagePopup',
            callback : fnUI
        });
    };

    //전체화면 구성
    function fnUI() {
        fnTranslate(); // comm.lang.js 안에 있는 공통함수 다국어
        fnItemGoodsPackageGrid(); // 가격정보 전체 이력 Grid 세팅
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

        itemGoodsPackageGridDs.query(query);
    };

    //가격정보 전체 이력 Grid
    function fnItemGoodsPackageGrid() {

        itemGoodsPackageGridDs = fnGetEditPagingDataSource({
            url : '/admin/item/master/getItemGoodsPackageList',
            pageSize : PAGE_SIZE,
        });

        itemGoodsPackageGridOpt = {
            dataSource : itemGoodsPackageGridDs,
            pageable : {
                buttonCount : 5,
                responsive : false
            },
            editable : false,
            navigatable : true,
            columns : [ {
                field : 'ilGoodsId',
                title : '상품코드',
                width : '35%'
            }, {
                field : 'goodsNm',
                title : '상품명',
                width : '65%'
            }],
            rowTemplate : kendo.template($("#rowTemplate").html()),

        }

        itemPriceHistGrid = $('#itemGoodsPackageGrid').initializeKendoGrid(itemGoodsPackageGridOpt).cKendoGrid();

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
