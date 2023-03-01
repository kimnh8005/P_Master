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
var goodsDiscountGridDs, goodsDiscountGridOpt, goodsDiscountGrid;
var pageParam = parent.POP_PARAM["parameter"];

$(document).ready(function() {

  fnInitialize(); //Initialize Page Call ---------------------------------

  //Initialize PageR
  function fnInitialize(){
    $scope.$emit('fnIsMenu', { flag : false });

    fnPageInfo({
      PG_ID  : 'goodsDiscountPopup',
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
    goodsDiscountGridDs.query( query );
  }

  function fnClose(){
    var kendoWindow =$('#kendoPopup').data('kendoWindow');
    kendoWindow.close();
  }
  // --------------------------------- Button End------------------------------


  // ------------------------------- Grid Start -------------------------------
  function fnInitGrid(){


    var ilGoodsId;
    var discountType;
    var goodsType;
    //var standardPriceTitle, recommendedPriceTitle, discountSalePriceTitle;
    var columns;

    if( pageParam != undefined ){
      ilGoodsId = pageParam.ilGoodsId;
      discountType = pageParam.discountType;
      goodsType = pageParam.goodsType;
    }

    goodsDiscountGridDs = fnGetPagingDataSource({
      url      : "/admin/comn/popup/getGoodsDiscountList?ilGoodsId="+ilGoodsId+"&discountTp="+discountType,
      pageSize : PAGE_SIZE
    });

//    console.log("discountType : ", discountType);
//    console.log("goodsType : ", goodsType);
    if(goodsType == "GOODS_TYPE.PACKAGE") {
    	columns = [
            { field : "discountTpNm"          , title : "구분"    , width: "5%", attributes : {style : "text-align:center;"} }
            , { field : "statusNm"              , title : "상태"    , width: "5%", attributes : {style : "text-align:center;"} }
            , { field : "discountStartDt"       , title : "시작일"   , width: "7%", hidden : false, attributes : {style : "text-align:center;"} }
            , { field : "discountEndDt"         , title : "종료일"   , width: "7%", hidden : false, attributes : {style : "text-align:center;"} }
            , { field : "apprReqInfo"             , title : "승인요청자", width: "10%", attributes : {style : "text-align:center;"} }
            , { field : "apprInfo"             , title : "승인관리자", width: "10%", attributes : {style : "text-align:center;"}
                            , template : function(result){
                                return result.apprInfo;
                             }
                          }
            , { field : "discountMethodTpNm"    , title : "할인유형" , width: "8%", attributes : {style : "text-align:center;"} }
            , { field : "standardPrice"         , title : "원가 총액"   , width: "8%", attributes : {style : "text-align:right;"} ,format: "{0:n0}"
          	  , template : function(result){
          		  return kendo.format("{0:\#\#,\#}", Number(result.standardPrice));
          	  }
          	}
            , { field : "recommendedPrice"      , title : "정상가 총액"   , width: "8%", attributes : {style : "text-align:right;"} ,format: "{0:n0}"
          	  , template : function(result){
          		  return kendo.format("{0:\#\#,\#}", Number(result.recommendedPrice));
          	  }
          	}
            , { field : "discountPrice"      , title : "할인액"   , width: "8%", attributes : {style : "text-align:right;"} ,format: "{0:n0}"
          	  , template : function(result){
          		  return kendo.format("{0:\#\#,\#}", Number(result.discountPrice));
          	  }
          	}
            , { field : "discountRatio"       , title : "할인율" , width: "8%", attributes : {style : "text-align:right;"}
                , template : function(result){
                      return kendo.format("{0:\#\#,\#}", Number(result.discountRatio)) + " %";
                  }
              }
            , { field : "discountSalePrice"     , title : "묶음상품 판매가"   , width: "8%", attributes : {style : "text-align:right;"} ,format: "{0:n0}"
                , template : function(result){
                    if (result.discountSalePrice == undefined || result.discountSalePrice == null || result.discountSalePrice == '' || result.discountSalePrice == 0) {
                      return "";
                    }
                    else {
                      return kendo.format("{0:\#\#,\#}", Number(result.discountSalePrice));
                    }
                  }
              }
            , { field : "detail"             , title : "상세" , width: "8%", attributes : {style : "text-align:center;"}
            	, template : function(result){
            		return "<span style='text-decoration: underline;color:blue;'>상세보기</span>";
            	}
            }

            ]
    }
    else{
    	columns = [
            { field : "discountTpNm"          , title : "구분"    , width: "5%", attributes : {style : "text-align:center;"} }
            , { field : "statusNm"              , title : "상태"    , width: "5%", attributes : {style : "text-align:center;"} }
            , { field : "discountStartDt"       , title : "시작일"   , width: "9%", hidden : false, attributes : {style : "text-align:center;"} }
            , { field : "discountEndDt"         , title : "종료일"   , width: "9%", hidden : false, attributes : {style : "text-align:center;"} }
            , { field : "apprReqInfo"             , title : "승인요청자", width: "12%", attributes : {style : "text-align:center;"} }
            , { field : "apprInfo"             , title : "승인관리자", width: "12%", attributes : {style : "text-align:center;"}
                , template : function(result){
                    return result.apprInfo;
                 }
              }
            , { field : "discountMethodTpNm"    , title : "할인유형" , width: "8%", attributes : {style : "text-align:center;"} }
            , { field : "standardPrice"         , title : "원가"   , width: "8%", attributes : {style : "text-align:right;"} ,format: "{0:n0}"
          	  , template : function(result){
          		  return kendo.format("{0:\#\#,\#}", Number(result.standardPrice));
          	  }
          	}
            , { field : "recommendedPrice"      , title : "정상가"   , width: "8%", attributes : {style : "text-align:right;"} ,format: "{0:n0}"
          	  , template : function(result){
          		  return kendo.format("{0:\#\#,\#}", Number(result.recommendedPrice));
          	  }
          	}
            , { field : "discountPrice"      , title : "할인액"   , width: "8%", attributes : {style : "text-align:right;"} ,format: "{0:n0}"
          	  , template : function(result){
          		  return kendo.format("{0:\#\#,\#}", Number(result.discountPrice));
          	  }
          	}
            , { field : "discountRatio"       , title : "할인율" , width: "8%", attributes : {style : "text-align:right;"}
                , template : function(result){
                      return kendo.format("{0:\#\#,\#}", Number(result.discountRatio)) + " %";
                  }
              }
            , { field : "discountSalePrice"     , title : "판매가"   , width: "8%", attributes : {style : "text-align:right;"} ,format: "{0:n0}"
                , template : function(result){
                    if (result.discountSalePrice == undefined || result.discountSalePrice == null || result.discountSalePrice == '' || result.discountSalePrice == 0) {
                      return "";
                    }
                    else {
                      return kendo.format("{0:\#\#,\#}", Number(result.discountSalePrice));
                    }
                  }
            }

            ]
    }

    if(discountType == "GOODS_DISCOUNT_TP.EMPLOYEE"){
	    goodsDiscountGridOpt = {
	        dataSource  : goodsDiscountGridDs
	      , pageable    : {pageSizes: [20, 30, 50], buttonCount : 10}
	      , navigatable : true
	      , scrollable  : true
	      , height      : 550
	      , columns     : [
	                        { field : "discountTpNm"          , title : "구분"    , width: "100px", attributes : {style : "text-align:center;"} }
	                      , { field : "statusNm"              , title : "상태"    , width: "60px", attributes : {style : "text-align:center;"} }
	                      , { field : "discountStartDt"       , title : "시작일"   , width: "100px", hidden : false, attributes : {style : "text-align:center;"} }
	                      , { field : "discountEndDt"         , title : "종료일"   , width: "100px", hidden : false, attributes : {style : "text-align:center;"} }
	                      , { field : "recommendedPrice"      , title : "정상가"   , width: "60px", attributes : {style : "text-align:right;"} ,format: "{0:n0}"
	                    	  , template : function(result){
	                                return kendo.format("{0:\#\#,\#}", Number(result.recommendedPrice));
	                              }
	                      }
	                      , { field : "apprReqInfo"             , title : "승인요청자", width: "100px", attributes : {style : "text-align:center;"} }
	                      , { field : "apprInfo"             , title : "승인관리자", width: "100px", attributes : {style : "text-align:center;"}
                                          , template : function(result){
                                              return result.apprInfo;
                                           }
                                        }
	                      , { field : "discountRatio"         , title : "임직원 할인" , width: "80px", attributes : {style : "text-align:center;"}
                              , template : function(result){
                                return kendo.format("{0:\#\#,\#}", Number(result.discountRatio)) + " %";
                              }
                          }
	                      , { field : "discountSalePrice", title : "판매가"   , width: "60px", attributes : {style : "text-align:right;"} ,format: "{0:n0}"
	                                                          , template : function(result){
	                                                                return kendo.format("{0:\#\#,\#}", Number(result.discountSalePrice));
	                                                            }
	                      }

	                      ]
	      //, rowTemplate : kendo.template($("#rowTemplate").html())
	    };
    }
    else {
    	goodsDiscountGridOpt = {
    	        dataSource  : goodsDiscountGridDs
    	      , pageable    : {pageSizes: [20, 30, 50], buttonCount : 10}
    	      , navigatable : true
    	      , scrollable  : true
    	      , height      : 550
    	      , columns     : columns
    	      //, rowTemplate : kendo.template($("#rowTemplate").html())
    	    };
    }

    goodsDiscountGrid = $('#goodsDiscountGrid').initializeKendoGrid( goodsDiscountGridOpt ).cKendoGrid();

    $("#goodsDiscountGrid").on("click", "tbody>tr", function () {
        //fnGridClick();
    });

    if(goodsType == "GOODS_TYPE.PACKAGE") {
    	$(goodsDiscountGrid.tbody).on("click", "td", function (e) {
    		var row = $(this).closest("tr");
    		var rowIdx = $("tr", goodsDiscountGrid.tbody).index(row);
    		var colIdx = $("td", row).index(this);
    		if(colIdx == 12){
    			fnGoodsMappingPopup(e);
    		}
    	});
    }

    goodsDiscountGrid.bind("dataBound", function(e) {
      $('#countTotalSpan').text(goodsDiscountGridDs._total);

      var rows = e.sender.tbody.children();
      if(row != null && row.length > 0){
	      for (var j = 0; j < rows.length; j++) {
	          var row = $(rows[j]);
	          var dataItem = e.sender.dataItem(row);

	          if ( fnFormatDate( dataItem.get("discountStartDt"), 'yyyyMMddHHmmss') <= fnGetTodayTime('yyyyMMddHHmmss') && fnFormatDate( dataItem.get("discountEndDt"), 'yyyyMMddHHmmss') >= fnGetTodayTime('yyyyMMddHHmmss') ) {
	        	  row.css("background-color", "#D2E7A9");
	          }
	      }
      }
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
  function fnGoodsMappingPopup(e){
	var dataItem = goodsDiscountGrid.dataItem($(e.target).closest('tr'));
	let option = {};
	console.log("dataItem.ilGoodsDiscountId : ", dataItem.ilGoodsDiscountId);
	console.log("dataItem.ilGoodsPriceId : ", dataItem.ilGoodsPriceId);
	console.log("pageParam.discountType : ", pageParam.discountType);

	let params = {};
	params.ilGoodsDiscountId = dataItem.ilGoodsDiscountId;

	var GoodsPricePopupName = "구성상품 판매상세 정보";

	fnKendoPopup({
		id			: "goodsPackageGoodsMappingPricePopup",
		title		: GoodsPricePopupName, // 해당되는 Title 명 작성
		width		: "1700px",
		height		: "380px",
		scrollable	: "yes",
		src			: "#/goodsPackageGoodsMappingPricePopup",
		param		: params,
		success		: function( id, data ){
		}
	});
  }
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
