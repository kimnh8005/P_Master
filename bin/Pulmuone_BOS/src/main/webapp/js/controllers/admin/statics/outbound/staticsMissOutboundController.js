/**-----------------------------------------------------------------------------
 * description 		 : 통계관리 > 출고통계 > 미출 통계
 * @
 * @ 수정일			수정자          수정내용
 * @ ------------------------------------------------------
 * @ 2021.05.11		이원호          최초생성
 * @
 * **/
'use strict';

var PAGE_SIZE = 20;
var aGridDs, aGridOpt, aGrid;

$(document).ready(function() {

	fnInitialize();	// Initialize Page Call ---------------------------------

	// Initialize PageR
	function fnInitialize(){
		$scope.$emit('fnIsMenu', { flag : 'true' });

		fnPageInfo({
			PG_ID  : 'staticsMissOutbound',
			callback : fnUI
		});
	}

	function fnUI(){
		fnInitButton();	// Initialize Button ---------------------------------
		fnInitGrid();	// Initialize Grid ------------------------------------
		fnInitOptionBox();// Initialize Option Box
		fnDefaultSet();
	}

	// --------------------------------- Button
	// Start---------------------------------
	function fnInitButton(){
		$('#fnSearch', '#fnClear', '#fnExcelDownload').kendoButton();
	}

	function fnSearch(){
		var data = $('#searchForm').formSerialize(true);

		if(data.startDe == "" || data.endDe == ""){
            fnKendoMessage({message : '기준기간을 입력해주세요.'});
            return;
        }

		var query = {
            page         : 1,
            pageSize     : PAGE_SIZE,
            filterLength : fnSearchData(data).length,
            filter :  {
                filters : fnSearchData(data)
            }
		};
		aGridDs.query( query );
	}

	function fnClear(){
		$('#searchForm').formClear(true);
		fnDefaultSet();
	}

	function fnGetCheckBoxText(id){
	    var value = "";
        $('form[id=searchForm] :checkbox[name='+ id +']:checked').each(function() {
            value += $(this).closest('label').find('span').text() + ","
        });
        if (value == null)
            value = "";
        value = value.substring(0, value.length - 1);
        return value;
	}

    // 옵션 초기화
	function fnInitOptionBox(){

        // 기준기간 시작일/종료일 [날짜]
        fnKendoDatePicker({
            id          : 'startDe'
          , format      : 'yyyy-MM-dd'
          , btnStartId  : 'startDe'
          , btnEndId    : 'endDe'
          , change      : fnOnChangeStartDt
        });
        fnKendoDatePicker({
            id          : 'endDe'
          , format      : 'yyyy-MM-dd'
          , btnStyle    : false     //버튼 숨김
          , btnStartId  : 'startDe'
          , btnEndId    : 'endDe'
          , change      : fnOnChangeEndDt
        });
        function fnOnChangeStartDt(e) {
          fnOnChangeDatePicker(e, 'start', 'startDe', 'endDe');
        }
        function fnOnChangeEndDt(e) {
          fnOnChangeDatePicker(e, 'end', 'startDe', 'endDe');
        }

        // VAT포함여부 [체크]
        fnTagMkChkBox({
            id        : 'includeVatYn'
          , tagId     : 'includeVatYn'
          , data      : [
                          { 'CODE' : 'Y'    , 'NAME' : 'VAT포함'    }
                        ]
          , chkVal    : 'Y'
          , style     : {}
        });

        // 공급업체
        fnTagMkChkBox({
            id          : 'supplierFilter'
          , tagId       : 'supplierFilter'
          , url         : '/admin/comn/getDropDownSupplierList'
          , async       : false
          , isDupUrl    : 'Y'
          , style       : {}
          , beforeData  : [{ 'CODE' : 'ALL', 'NAME' : '전체' }]
          , textField   : "supplierName"
          , valueField  : "supplierId"
        });

        // 출고처그룹
        searchCommonUtil.getDropDownCommCd("urWarehouseGrpCd", "NAME", "CODE", "출고처 그룹 전체", "WAREHOUSE_GROUP", "Y");

        const $warehouseGroup = $("#urWarehouseGrpCd").data("kendoDropDownList");

        if( $warehouseGroup ) {
            $warehouseGroup.bind("change", function (e) {
                const warehouseGroupCode = this.value();

                fnAjax({
                    url     : "/admin/comn/getDropDownWarehouseGroupByWarehouseList",
                    method : "GET",
                    params : { "warehouseGroupCode" : warehouseGroupCode },
                    success : function( data ){
                        let warehouseId = $("#urWarehouseId").data("kendoDropDownList");
                        warehouseId.setDataSource(data.rows);
                    },
                    error : function(xhr, status, strError){
                        fnKendoMessage({ message : xhr.responseText });
                    },
                    isAction : "select"
                });
            });
        }

        // 출고처그룹 별 출고처
        const WAREHOSE_ID = "urWarehouseId";
        searchCommonUtil.getDropDownUrl(WAREHOSE_ID, WAREHOSE_ID, "/admin/comn/getDropDownWarehouseGroupByWarehouseList", "warehouseName", "warehouseId", "출고처 선택", "", "");

        // 검색 판매처그룹
        searchCommonUtil.getDropDownCommCd("sellersGroupCd", "NAME", "CODE", "판매처 그룹 전체", "SELLERS_GROUP", "Y", "", "", "");

        // 검색 판매처그룹 별 외부몰
        const SCH_OM_SELLERS_ID = "omSellersId";

        searchCommonUtil.getDropDownUrl(SCH_OM_SELLERS_ID, SCH_OM_SELLERS_ID, "/admin/comn/getDropDownSellersGroupBySellersList", "sellersNm", "omSellersId", "판매처 전체", "", "");

        const searchSellersGroup = $("#sellersGroupCd").data("kendoDropDownList");

        if( searchSellersGroup ) {
            searchSellersGroup.bind("change", function (e) {
                const searchSellersGroup = this.value();

                fnAjax({
                    url     : "/admin/comn/getDropDownSellersGroupBySellersList",
                    method : "GET",
                    params : { "sellersGroupCd" : searchSellersGroup },
                    success : function( data ){
                        let searchOmSellersId = $("#omSellersId").data("kendoDropDownList");
                        searchOmSellersId.setDataSource(data.rows);
                    },
                    error : function(xhr, status, strError){
                        fnKendoMessage({ message : xhr.responseText });
                    },
                    isAction : "select"
                });
            });
        }


        // 배송유형
        fnTagMkChkBox({
            id          : 'searchDelivFilter'
          , tagId       : 'searchDelivFilter'
          , url         : '/admin/comn/getCodeList'
          , params : {
                  "stCommonCodeMasterCode" : "GOODS_DELIVERY_TYPE",
                  "useYn" : "Y"
              }
          , async       : false
          , isDupUrl    : 'Y'
          , style       : {}
          , beforeData  : [{ 'CODE' : 'ALL', 'NAME' : '전체' }]
        });

        fbCheckboxChange(); //[공통] checkBox
	};


	// 기본 설정
	function fnDefaultSet(){
	    $("input[name=supplierFilter]").eq(0).prop("checked", true).trigger("change");
	    $("input[name=searchDelivFilter]").eq(0).prop("checked", true).trigger("change");
        $("input[name=includeVatYn]").eq(0).prop("checked", true).trigger("change");
	};

    // --------------------------------- Button
	// End---------------------------------

	// ------------------------------- Grid Start
	// -------------------------------
	function fnInitGrid(){
		aGridDs = fnGetPagingDataSource({
			url      : "/admin/statics/outbound/getMissOutboundStaticsList",
			pageSize : PAGE_SIZE
		});
		aGridOpt = {
			dataSource: aGridDs
//			,  pageable  : {
//				pageSizes: [20, 30, 50],
//				buttonCount : 10
//			}
			,navigatable: true
			,columns   : [
                { field:'dt' ,title : '구분'	,width:'90px' ,attributes:{ style:'text-align:center' }}
                ,{ title: '미출상품',
                    columns: [
                        { field:'deliveryGoodsCnt' ,title : '출고 지시상품'	,width:'90px' ,attributes:{ style:'text-align:center' }}
                        ,{ field:'missGoodsCnt' ,title : '미출 발생상품'	,width:'90px' ,attributes:{ style:'text-align:center' }}
                        ,{ field:'missGoodsRateName' ,title : '상품 미출률(%)'	,width:'90px' ,attributes:{ style:'text-align:center' }}
                        ,{ field:'missGoodsPriceName' ,title : '미출 발생금액'	,width:'90px' ,attributes:{ style:'text-align:center' }}
                        ]}
                ,{ title: '미출주문',
                    columns: [
                        { field:'deliveryOrderCnt' ,title : '출고 지시주문'	,width:'90px' ,attributes:{ style:'text-align:center' }}
                        ,{ field:'missOrderCnt' ,title : '미출 발생주문'	,width:'90px' ,attributes:{ style:'text-align:center' }}
                        ,{ field:'missOrderRateName' ,title : '주문 미출률%'	,width:'90px' ,attributes:{ style:'text-align:center' }}
                        ]}
            ]
		};

		aGrid = $('#aGrid').initializeKendoGrid( aGridOpt ).cKendoGrid();

		aGrid.bind("dataBound", function() {
			var row_num = aGridDs._total - ((aGridDs._page - 1) * aGridDs._pageSize);
			$("#aGrid tbody > tr .row-number").each(function(index){
				$(this).html(row_num);
				row_num--;
			});

			if(aGridDs._data.length > 0) {
			    $('#totalDeliveryGoodsCnt').text(aGridDs._data[aGridDs._data.length - 1].deliveryGoodsCnt);
			    $('#totalMissGoodsCnt').text(aGridDs._data[aGridDs._data.length - 1].missGoodsCnt);
			    $('#totalMissGoodsPriceName').text(aGridDs._data[aGridDs._data.length - 1].missGoodsPriceName);
			    $('#totalDeliveryOrderCnt').text(aGridDs._data[aGridDs._data.length - 1].deliveryOrderCnt);
			    $('#totalMissOrderCnt').text(aGridDs._data[aGridDs._data.length - 1].missOrderCnt);
            }
        });
	}

	// ------------------------------- Grid End -------------------------------

	// ------------------------------- Html 버튼 바인딩 Start
	// -------------------------------

	/** Common Search */
	$scope.fnSearch = function(){	fnSearch();	};
	/** Common Clear */
	$scope.fnClear = function(){	 fnClear();	};

	/** Button fnExcelDownload */
    $scope.fnExcelDownload = function() {
        var data = $('#searchForm').formSerialize(true);

        data.supplierFilterName = fnGetCheckBoxText('supplierFilter');
        data.searchDelivFilterName = fnGetCheckBoxText('searchDelivFilter');

        data.urWarehouseGrpCdName = $('#urWarehouseGrpCd').data('kendoDropDownList').text();
        data.urWarehouseIdName = $('#urWarehouseId').data('kendoDropDownList').text();
        data.sellersGroupCdName = $('#sellersGroupCd').data('kendoDropDownList').text();
        data.omSellersIdName = $('#omSellersId').data('kendoDropDownList').text();

        fnExcelDownload('/admin/statics/outbound/getExportExcelMissOutboundStaticsList', data);
    };

	// ------------------------------- Html 버튼 바인딩 End
	// -------------------------------

}); // document ready - END
