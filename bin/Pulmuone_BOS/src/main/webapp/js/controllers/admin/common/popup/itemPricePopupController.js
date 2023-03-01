/**-----------------------------------------------------------------------------
 * description     : 상품할인리스트조회Popup
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
      PG_ID  : 'itemPricePopup',
      callback : fnUI
    });


  }

  function fnUI(){

    fnInitButton(); //Initialize Button  ---------------------------------

    fnInitGrid(); //Initialize Grid ------------------------------------

    fnInitOptionBox();//Initialize Option Box ------------------------------------

    fnSearch();

  }

  // --------------------------------- Button Start----------------------------

  function fnInitButton(){

    //$('#fnSearch, #fnClose, #fnWarehousePopupButton').kendoButton();
  }

  function fnSearch(){

    var data;
    data = $('#searchForm').formSerialize(true);

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

  function fnClose(){
    var kendoWindow =$('#kendoPopup').data('kendoWindow');
    kendoWindow.close();
  }
  // --------------------------------- Button End------------------------------


  // ------------------------------- Grid Start -------------------------------
  function fnInitGrid(){


    var ilItemCd;

    if( pageParam != undefined ){
      ilItemCd = pageParam.ilItemCd;
    }

    itemPriceGridDs = fnGetPagingDataSource({
      url      : "/admin/comn/popup/getItemPriceList?ilItemCd="+ilItemCd,
      pageSize : PAGE_SIZE
    });

    itemPriceGridOpt = {
        dataSource  : itemPriceGridDs
      , pageable    : {pageSizes: [20, 30, 50], buttonCount : 10}
      , navigatable : true
      , scrollable  : true
      , height      : 550
      , columns     : [
                        { field : "regTpNm"               , title : "구분"    , width: "100px", attributes : {style : "text-align:center;"} }
                      , { field : "statusNm"              , title : "상태"    , width: "60px", attributes : {style : "text-align:center;"} }
                      , { field : "startDt"               , title : "시작일"   , width: "100px", hidden : false, attributes : {style : "text-align:center;"} }
                      , { field : "endDt"                 , title : "종료일"   , width: "100px", hidden : false, attributes : {style : "text-align:center;"} }
                      , { field : "approveNm"             , title : "승인담당자", width: "100px", attributes : {style : "text-align:center;"} }
                      , { field : "standardPrice"         , title : "원가" , width: "60px", attributes : {style : "text-align:right;"}
                                                          //, template : function(result){
                                                          //    return kendo.format("{0:n0}", Number(result.standardPrice)) + " 원";
                                                          //    //if (result.standardPrice == undefined || result.standardPrice == null) {
                                                          //    //  return "- 원";
                                                          //    //}
                                                          //    //else {
                                                          //    //  return kendo.format("{0:n0}", result.standardPrice) + " 원";
                                                          //    //}
                                                          //  }
                        }
                      , { field : "recommendedPrice"      , title : "정상가" , width: "60px", attributes : {style : "text-align:right;"}
                                                          //, template : function(result){
                                                          //    return kendo.format("{0:n0}", Number(result.recommendedPrice)) + " 원";
                                                          //    //if (result.recommendedPrice == undefined || result.recommendedPrice == null) {
                                                          //    //  return "- 원";
                                                          //    //}
                                                          //    //else {
                                                          //    //  return result.recommendedPrice + " 원";
                                                          //    //}
                                                          //  }
                        }
                      ]
      , rowTemplate : kendo.template($("#rowTemplate").html())
    };

    itemPriceGrid = $('#itemPriceGrid').initializeKendoGrid( itemPriceGridOpt ).cKendoGrid();

    $("#itemPriceGrid").on("click", "tbody>tr", function () {
        //fnGridClick();
    });

    itemPriceGrid.bind("dataBound", function() {
      $('#countTotalSpan').text(itemPriceGridDs._total);
    });
  }

  // -------------------------------  Grid End  -------------------------------

  // ---------------Initialize Option Box Start -------------------------------
  function fnInitOptionBox(){

    $('#kendoPopup').kendoWindow({
      visible: false,
      modal: true
    });
  }
  // ---------------Initialize Option Box End ---------------------------------

  // -------------------------------  Common Function start -------------------

  /**
  * 콜백합수
  */
  function fnBizCallback( id, data ){
    switch(id){
      case 'select':
//        $('#inputForm').formClear(true);
//        if(data != null && data.rows.inputTpCode == "CLIENT_TYPE.SHOP"){
//          //fnShop(data.rows.urSupplierId);
//        }
//        $('#inputForm').bindingForm(data, "rows", true);
//        fnKendoInputPoup({height:"900px" ,width:"500px",title:{key :"5876",nullMsg :'거래처 수정' } });
//
//        $("#inputTypeCodeView").attr("disabled", true);
//        $("input:radio[name='inputTpCode']").attr('disabled',true);
//
//        if(data.rows.inputTpCode == "CLIENT_TYPE.CLIENT"){
//          $('#clientCompanyDiv').show();
//          $('#clientWarehouseDiv').show();
//          $('#shopDiv').hide();
//          $('#vendorChannelDiv').hide();
//          $('#vendorErpDiv').hide();
//          $('#telBaseView').show();
//          $('#telShopView').hide();
//
//
//
//        }else if(data.rows.inputTpCode == "CLIENT_TYPE.SHOP"){
//          $('#clientCompanyDiv').show();
//          $('#clientWarehouseDiv').hide();
//          $('#shopDiv').show();
//          $('#vendorChannelDiv').hide();
//          $('#vendorErpDiv').hide();
//          $('#telBaseView').hide();
//          $('#telShopView').show();
//
//          $("#shopTelephone1").attr('disabled',true);
//          $("#shopTelephone2").attr('disabled',true);
//          $("#shopTelephone3").attr('disabled',true);
//
//          $("#supplierCompany").data("kendoDropDownList").enable( false );
//          $("#store").data("kendoDropDownList").enable( false );
//
//        }else{
//          $('#clientCompanyDiv').hide();
//          $('#clientWarehouseDiv').hide();
//          $('#shopDiv').hide();
//          $('#vendorChannelDiv').show();
//          $('#vendorErpDiv').show();
//          $('#telBaseView').show();
//          $('#telShopView').hide();
//
//        }
//        break;
//      case 'insert':
//      case 'update':
//        fnKendoMessage({
//            message:"저장되었습니다.",
//            ok:function(){
//              fnSearch();
//              fnClose();
//            }
//        });
      break;

    }
  }

  // -------------------------------  Common Function end ---------------------


  // ------------------------------- Html 버튼 바인딩  Start -----------------------
  /** Common Search*/
  $scope.fnSearch = function( ) { fnSearch(); };
  /** Common Close*/
  $scope.fnClose = function( ){  fnClose();};

  //마스터코드값 입력제한 - 숫자 & -
  //fnInputValidationByRegexp("accountTelephone1", /[^0-9]/g);
  //fnInputValidationByRegexp("accountTelephone2", /[^0-9]/g);
  //fnInputValidationByRegexp("accountTelephone3", /[^0-9]/g);
  //fnInputValidationByRegexp("channelCode", /[^0-9]/g);

  // ------------------------------- Html 버튼 바인딩  End -------------------------

}); // document ready - END
