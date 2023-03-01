/*******************************************************************************************************************************************************************************************************
 * -------------------------------------------------------- description : 상품일괄수정 추가상품목록 보기 팝업 * @ @ 수정일 수정자 수정내용 @ ------------------------------------------------------ * @ 2021.04.07 정형진 최초생성 @
 ******************************************************************************************************************************************************************************************************/
'use strict';

var PAGE_SIZE = 10;
var goodsAddGridOpt, goodsAddGrid, goodsAddGridDs;

var popupParameter = parent.POP_PARAM["parameter"]; // 팝업 파라미터 전역변수
var ilGoodsId = popupParameter['ilGoodsId']; // 상품코드
var warehouseId = popupParameter['warehouseId']; // 출고처


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
        fnGoodsAddGrid(); // 추가상품  Grid 세팅
        fnSearch(); // 조회
    };

    function fnSearch() {

        var data = $('#searchForm').formSerialize(true);

        data.ilGoodsId = ilGoodsId; // 상품코드
        data.urWarehouseId = warehouseId; // 출고처ID

        var url  = '/admin/goods/list/getGoodsAdditionList';
		var cbId = 'goodsAddList';

		fnAjax({
			url	 : url,
			params  : data,
			async	: false,
			success :
				function( data ){
					fnBizCallback(cbId, data);
				},
				isAction : 'batch'
		});
    };

	function fnBizCallback(cbId, data) {

		var dataRows = data.goodsAdditionalGoodsMappingList;

		for (var i in dataRows) {
			goodsAddGridDs.add(dataRows[i]);
		}


	};

    //상품일괄수정 추가상품 Grid
    function fnGoodsAddGrid() {

        goodsAddGridDs = fnGetEditPagingDataSource({
            url : '/admin/goods/list/getGoodsAdditionList',
            pageSize : PAGE_SIZE,
        });

        goodsAddGridOpt = {
            dataSource : goodsAddGridDs,
            editable : false,
            navigatable : true,
            columns : [ {
                field : 'targetGoodsId',
                title : '상품코드',
                width : '20%'
            }, {
                field : 'goodsName',
                title : '상품명',
                width : '20%'
            }
            , {
                field : 'standardPrice',
                title : '원가',
                width : '20%'
            }
            , {
                field : 'recommendedPrice',
                title : '정상가',
                width : '20%'
            }
            , {
                field : 'salePrice',
                title : '판매가',
                width : '20%'
            }
            ],
            rowTemplate : kendo.template($("#rowTemplate").html()),

        }

        goodsAddGrid = $('#itemGoodsPackageGrid').initializeKendoGrid(goodsAddGridOpt).cKendoGrid();

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
