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
var goodsPriceGridDs, goodsPriceGridOpt, goodsPriceGrid;
var pageParam = parent.POP_PARAM["parameter"];

$(document).ready(function() {

  fnInitialize(); //Initialize Page Call ---------------------------------

  //Initialize PageR
  function fnInitialize(){
    $scope.$emit('fnIsMenu', { flag : false });

    fnPageInfo({
      PG_ID  : 'goodsPricePopup',
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
    goodsPriceGridDs.query( query );
  }

  function fnClose(){
    var kendoWindow =$('#kendoPopup').data('kendoWindow');
    kendoWindow.close();
  }
  // --------------------------------- Button End------------------------------


  // ------------------------------- Grid Start -------------------------------
  function fnInitGrid(){


    var ilGoodsId;
    var priceKind;
    var columnsData;
    var rowData;

    if( pageParam != undefined ){
      ilGoodsId = pageParam.ilGoodsId;
      priceKind = pageParam.priceKind;
    }

    if(priceKind == "price") {
    	columnsData = [
            { field : "discountTpNm"            , title : "구분"    , width: "60px", attributes : {style : "text-align:center;"} }
            , { field : "statusNm"              , title : "상태"    , width: "60px", attributes : {style : "text-align:center;"} }
            , { field : "priceStartDt"          , title : "시작일"   , width: "100px", hidden : false, attributes : {style : "text-align:center;"} }
            , { field : "priceEndDt"            , title : "종료일"   , width: "100px", hidden : false, attributes : {style : "text-align:center;"} }
            , { field : "apprReqInfo"           , title : "승인요청자", width: "100px", attributes : {style : "text-align:center;"} }
            , { field : "apprInfo"              , title : "승인관리자", width: "100px", attributes : {style : "text-align:center;"} }
            , { field : "discountMethodTpNm"    , title : "할인유형" , width: "60px", attributes : {style : "text-align:center;"} }
            , { field : "standardPrice"         , title : "원가" , width: "60px", attributes : {style : "text-align:right;"}
              }
            , { field : "recommendedPrice"      , title : "정상가" , width: "60px", attributes : {style : "text-align:right;"}
              }
            , { field : "discountPrice"         , title : "할인액" , width: "60px", attributes : {style : "text-align:right;"}
              }
            , { field : "discountRatio"         , title : "할인율" , width: "60px", attributes : {style : "text-align:right;"}
              }
            , { field : "salePrice"             , title : "판매가" , width: "60px", attributes : {style : "text-align:right;"}
              }
            ];
    	rowData = $("#rowTemplate").html();
    }
    else if(priceKind == "employeePrice") {
    	columnsData = [
            { field : "discountTpNm"            , title : "구분"    , width: "100px", attributes : {style : "text-align:center;"} }
            , { field : "statusNm"              , title : "상태"    , width: "60px", attributes : {style : "text-align:center;"} }
            , { field : "priceStartDt"          , title : "시작일"   , width: "190px", hidden : false, attributes : {style : "text-align:center;"} }
            , { field : "priceEndDt"            , title : "종료일"   , width: "190px", hidden : false, attributes : {style : "text-align:center;"} }
            , { field : "recommendedPrice"      , title : "정상가" , width: "60px", attributes : {style : "text-align:right;"} }
            , { field : "apprReqInfo"           , title : "승인요청자", width: "100px", attributes : {style : "text-align:center;"} }
            , { field : "apprInfo"              , title : "승인관리자", width: "100px", attributes : {style : "text-align:center;"} }
            , { field : "discountRatio"         , title : "임직원 할인" , width: "100px", attributes : {style : "text-align:right;"}
              }
            , { field : "salePrice"             , title : "판매가" , width: "60px", attributes : {style : "text-align:right;"}
              }
            ];
    	rowData = $("#employeeRowTemplate").html();
    }
    else if(priceKind == "packagePrice") {
    	columnsData = [
            { field : "discountTpNm"            , title : "구분"    , width: "100px", attributes : {style : "text-align:center;"} }
            , { field : "statusNm"              , title : "상태"    , width: "60px", attributes : {style : "text-align:center;"} }
            , { field : "priceStartDt"          , title : "시작일"   , width: "100px", hidden : false, attributes : {style : "text-align:center;"} }
            , { field : "priceEndDt"            , title : "종료일"   , width: "100px", hidden : false, attributes : {style : "text-align:center;"} }
            , { field : "apprReqInfo"           , title : "승인요청자", width: "70px", attributes : {style : "text-align:center;"} }
            , { field : "apprInfo"              , title : "승인담당자", width: "70px", attributes : {style : "text-align:center;"} }
            , { field : "discountMethodTpNm"    , title : "할인유형" , width: "60px", attributes : {style : "text-align:center;"} }
            , { field : "standardPrice"         , title : "원가 총액" , width: "90px", attributes : {style : "text-align:right;"}
              }
            , { field : "recommendedPrice"      , title : "정상가 총액" , width: "90px", attributes : {style : "text-align:right;"}
              }
            , { field : "salePrice"             , title : "묶음상품판매가" , width: "60px", attributes : {style : "text-align:right;"}
              }
            , { field : "detail"                , title : "상세" , width: "60px", attributes : {style : "text-align:center;"}
            }
            ];
    	rowData = $("#packageRowTemplate").html();
    }

    goodsPriceGridDs = fnGetPagingDataSource({
      url      : "/admin/comn/popup/getGoodsPriceList?ilGoodsId="+ilGoodsId+"&priceKind="+priceKind,
      pageSize : PAGE_SIZE
    });

    goodsPriceGridOpt = {
        dataSource  : goodsPriceGridDs
      , pageable    : {pageSizes: [20, 30, 50], buttonCount : 10}
      , navigatable : true
      , scrollable  : true
      , height      : 550
      , columns     : columnsData
      , rowTemplate : kendo.template(rowData)
    };

    goodsPriceGrid = $('#goodsPriceGrid').initializeKendoGrid( goodsPriceGridOpt ).cKendoGrid();

    if(priceKind == "packagePrice") {
    	$(goodsPriceGrid.tbody).on("click", "td", function (e) {
    		var row = $(this).closest("tr");
    		var rowIdx = $("tr", goodsPriceGrid.tbody).index(row);
    		var colIdx = $("td", row).index(this);
    		if(colIdx == 10){
    			fnGoodsMappingPopup(e);
    		}
    	});
    }

    goodsPriceGrid.bind("dataBound", function() {
      $('#countTotalSpan').text(goodsPriceGridDs._total);
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
	var dataItem = goodsPriceGrid.dataItem($(e.target).closest('tr'));
	let option = {};
	console.log("dataItem.ilGoodsPriceId : ", dataItem.ilGoodsPriceId);

	let params = {};
	params.ilGoodsPriceId = dataItem.ilGoodsPriceId;

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
