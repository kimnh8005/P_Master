/**-----------------------------------------------------------------------------
 * description     : 품목매장가격리스트조회Popup
 * @
 * @ 수정일      수정자          수정내용
 * @ ------------------------------------------------------
 * @ 2020.9.9    안치열          최초생성
 * @
 * **/
'use strict';


var PAGE_SIZE = 10;
var itemPriceGridDs, itemPriceGridOpt, itemPriceGrid;
var pageParam = parent.POP_PARAM["parameter"];

$(document).ready(function() {

  fnInitialize(); //Initialize Page Call ---------------------------------

  //Initialize PageR
  function fnInitialize(){
    $scope.$emit('fnIsMenu', { flag : false });

    fnPageInfo({
      PG_ID  : 'itemStorePriceLog',
      callback : fnUI
    });


  }

  function fnUI(){

    fnInitGrid(); //Initialize Grid ------------------------------------

    fnSearch();

  }

  // --------------------------------- Button Start----------------------------

  function fnSearch(){

    var data = pageParam;

    var query = {
          page         : 1,
          pageSize     : PAGE_SIZE,
          filterLength : fnSearchData(data).length,
          filter :  {
            filters : fnSearchData(data)
          }
    };
    itemPriceGridDs.query( query );
  }

  // --------------------------------- Button End------------------------------


  // ------------------------------- Grid Start -------------------------------
  function fnInitGrid(){

    itemPriceGridDs = fnGetPagingDataSource({
      url      : "/admin/item/store/getStorePriceLogList",
      pageSize : PAGE_SIZE
    });

    itemPriceGridOpt = {
        dataSource  : itemPriceGridDs
      , pageable    : {pageSizes: [20, 30, 50], buttonCount : 10}
      , navigatable : true
      , scrollable  : true
      , height      : 550
      , columns     : [
                        { field : "storeName" , title : "매장정보"    , width: "100px", attributes : {style : "text-align:center;"} }
                      , { field : "priceStartDate" , title : "시작일"    , width: "100px", attributes : {style : "text-align:center;"} }
                      , { field : "priceEndDate" , title : "종료일"   , width: "100px", attributes : {style : "text-align:center;"} }
                      , { field : "storeSalePrice" , title : "O2o 매장 판매가"   , width: "100px", format: "{0:n0}", attributes : {style : "text-align:center;"}}
                     ]
    };

    itemPriceGrid = $('#itemPriceGrid').initializeKendoGrid( itemPriceGridOpt ).cKendoGrid();

    itemPriceGrid.bind("dataBound", function() {
      $('#countTotalSpan').text(itemPriceGridDs._total);
    });
  }

  // -------------------------------  Grid End  -------------------------------
}); // document ready - END
