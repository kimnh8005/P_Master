/**-----------------------------------------------------------------------------
 * description 		 : 제품 상세 정보 팝업
 * @
 * @ 수정일			수정자        수정내용
 * @ ------------------------------------------------------
 * @ 2021.06.22		신선미		최초생성
 * **/
'use strict';

var PAGE_SIZE = 20;
var aGridDs, aGridOpt, aGrid;
var paramGoodsId  = "";

$(document).ready(function() {
	//품목 재고리스트에서 넘어온 param
	paramGoodsId				= parent.POP_PARAM["parameter"].goodsId;

	fnInitialize();	//Initialize Page Call ---------------------------------

	//Initialize PageR
	function fnInitialize(){
		$scope.$emit('fnIsMenu', { flag : false });

		fnPageInfo({
			PG_ID  : 'packageGoodsStockMgm',
			callback : fnUI
		});
	}

	function fnUI(){
		fnTranslate();	// 다국어 변환--------------------------------------------
		fnSearch();
	}

	function fnSearch(){
		var url  = '/admin/goods/stock/getStockInfo';
		var cbId = 'search';
		var data = $('#inputForm').formSerialize(true);

		data.goodsId	=	paramGoodsId	// 묶음 상품 ID
		data.goodsType 	= 	'GOODS_TYPE.PACKAGE'

		fnAjax({
			url     : url,
			params  : data,
			success :
				function( data ){
					fnBizCallback(cbId, data);
				},
			isAction : 'select'
		});
	}

	/**
	 * 콜백합수
	 */
	function fnBizCallback( id, data ){
		switch(id){
			case 'select':
				break;

			case 'search':
				$('#inputForm').bindingForm(data, 'stockListResultVo', true);
				var detailData = data.stockListResultVo;

				// 출고처|묶음상품코드|묶음상품명
				$("#warehouseNm").html(detailData.warehouseNm);
				$("#goodsId").html(detailData.ilGoodsId);
				$("#goodsNm").html(detailData.goodsNm)

				// 출고예정수량
				if(detailData.orderQtyList[0] == null) {
					$(".quantity td span").html(0)
				} else {
					$("#d0Quantity").html(detailData.orderQtyList[0].d0Quantity);
					$("#d1Quantity").html(detailData.orderQtyList[0].d1Quantity);
					$("#d2Quantity").html(detailData.orderQtyList[0].d2Quantity);
					$("#d3Quantity").html(detailData.orderQtyList[0].d3Quantity);
					$("#d4Quantity").html(detailData.orderQtyList[0].d4Quantity);
					$("#d5Quantity").html(detailData.orderQtyList[0].d5Quantity);
					$("#d6Quantity").html(detailData.orderQtyList[0].d6Quantity);
					$("#d7Quantity").html(detailData.orderQtyList[0].d7Quantity);
					$("#d8Quantity").html(detailData.orderQtyList[0].d8Quantity);
					$("#d9Quantity").html(detailData.orderQtyList[0].d9Quantity);
					$("#d10Quantity").html(detailData.orderQtyList[0].d10Quantity);
					$("#d11Quantity").html(detailData.orderQtyList[0].d11Quantity);
					$("#d12Quantity").html(detailData.orderQtyList[0].d12Quantity);
					$("#d13Quantity").html(detailData.orderQtyList[0].d13Quantity);
					$("#d14Quantity").html(detailData.orderQtyList[0].d14Quantity);
					$("#d15Quantity").html(detailData.orderQtyList[0].d15Quantity);
				}

				// 출고가능수량
				$("#d0orderQuantity").html(detailData.d0orderQuantity);
				$("#d1orderQuantity").html(detailData.d1orderQuantity);
				$("#d2orderQuantity").html(detailData.d2orderQuantity);
				$("#d3orderQuantity").html(detailData.d3orderQuantity);
				$("#d4orderQuantity").html(detailData.d4orderQuantity);
				$("#d5orderQuantity").html(detailData.d5orderQuantity);
				$("#d6orderQuantity").html(detailData.d6orderQuantity);
				$("#d7orderQuantity").html(detailData.d7orderQuantity);
				$("#d8orderQuantity").html(detailData.d8orderQuantity);
				$("#d9orderQuantity").html(detailData.d9orderQuantity);
				$("#d10orderQuantity").html(detailData.d10orderQuantity);
				$("#d11orderQuantity").html(detailData.d11orderQuantity);
				$("#d12orderQuantity").html(detailData.d12orderQuantity);
				$("#d13orderQuantity").html(detailData.d13orderQuantity);
				$("#d14orderQuantity").html(detailData.d14orderQuantity);
				$("#d15orderQuantity").html(detailData.d15orderQuantity);

				break;
		}
	}
});
