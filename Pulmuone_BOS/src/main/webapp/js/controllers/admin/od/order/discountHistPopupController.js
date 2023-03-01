/**-----------------------------------------------------------------------------
 * description 		 : 주문/배송관리 > 즉시 할인 내역
 * @
 * @ 수정일			수정자          수정내용
 * @ ------------------------------------------------------
 * @ 2020.12.07		김승우          최초생성
 * @ 2021.02.08		최윤지			추가작성
 * **/
"use strict";

var directDiscountHistGridDs, directDiscountHistGridOpt, directDiscountHistGrid;
var pageParam = fnGetPageParam();
var paramData = parent.POP_PARAM['parameter'];

$(document).ready(function() {
	fnInitialize();
	//Initialize PageR
	function fnInitialize(){
		$scope.$emit("fnIsMenu", { flag : false });
		fnPageInfo({
			PG_ID  : "discountHistPopup",
			callback : fnUI
		});
	};

	// 화면 UI 초기화
	function fnUI(){
		fnInitGrid();
	};

	//------------------------------- Grid Start -------------------------------
	function fnInitGrid(){
		directDiscountHistGridDs = fnGetDataSource({
			url      : "/admin/order/getOrderDirectDiscountList?odOrderId="+paramData.odOrderId,
		});

		directDiscountHistGridOpt = {
			dataSource: directDiscountHistGridDs,
			navigatable : true,
			scrollable : true,
			columns : [
				{ field : "odOrderDetlId"	, title : "주문상세코드"		, width: "30px"		, attributes : {style : "text-align:center;"}, hidden:true}
				,	{ field : "ilGoodsId"		, title : "상품코드"			, width: "30px"		, attributes : {style : "text-align:center;"}}
				, { field : "goodsNm"			, title : "상품명"				, width: "60px"		, attributes : {style : "text-align:center;"} }
				, { field : "recommendedPrice", title : "정상가"				, width: "30px"		, attributes : {style : "text-align:center;"}, format: "{0:n0}"}
				, { field : "salePrice"		, title : "판매가"				, width: "30px"		, attributes : {style : "text-align:center;"}, format: "{0:n0}" }
				, { field : "remarks"			, title : "비고"				, width: "50px"		, attributes : {style : "text-align:center;"}
					,	template:
						function(dataItem){ // [비고] 상품할인유형 : 할인가격
							let str = "";
							str = stringUtil.getString(dataItem.discountTp, "") + " : "
								+ stringUtil.getString(fnNumberWithCommas(dataItem.discountPrice), "");
							return str;
						}
				}
			],
		};

		directDiscountHistGrid = $('#directDiscountHistGrid').initializeKendoGrid( directDiscountHistGridOpt ).cKendoGrid();
		directDiscountHistGridDs.query();
	};

	//-------------------------------  Grid End  -------------------------------

}); // document ready - END
