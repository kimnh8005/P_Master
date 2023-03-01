/**-----------------------------------------------------------------------------
 * description 		 : 상품정산 그리드 컬럼 설정
 * @
 * @ 수정일			수정자          수정내용
 * @ ------------------------------------------------------
 * @ 2021.04.13		안치열   최초생성
 * @
 * **/

var calOrderGridColUtil = {
    no: function(row){
        return { field : "no", title : "No", width: "60px", attributes : {style : "text-align:center;"}, template : "<span class='row-number'></span>" }
    },
    orderType: function(row){
        return { field : "orderType", title : "구분", width: "80px", attributes : {style : "text-align:center;"}
        }
    },
    typeNm: function(row){
        return { field : "typeNm", title : "구분", width: "120px", attributes : {style : "text-align:center;"}
        }
    },

    divNm: function(row){
        return { field : "divNm", title : "구분", width: "120px", attributes : {style : "text-align:center;"}
        }
    },

    agentTypeCd: function(row){
        return { field : "agentTypeCd", title : "주문경로(유형)", width: "100px", attributes : {style : "text-align:center;"}
        }
    },

    agentTypeCdNm: function(row){
        return { field : "agentTypeCdNm", title : "주문경로(유형)", width: "130px", attributes : {style : "text-align:center;"}
        }
    },
    goodsSettleDt: function(row){
        return { field : "goodsSettleDt", title : "정산처리일자", width: "100px", attributes : {style : "text-align:center;"} }
    },
    goodsOrderDt: function(row){
        return { field : "goodsOrderDt", title : "주문일자", width: "100px", attributes : {style : "text-align:center;"} }
    },

    orderDt: function(row){
        return { field : "orderDt", title : "주문일자", width: "150px", attributes : {style : "text-align:center;"} }
    },

    paymentDt: function(row){
        return { field : "paymentDt", title : "결제일자<br/>/환불일자", width: "100px", attributes : {style : "text-align: center;"} }
    },
    approvalDt: function(row){
        return { field : "approvalDt", title : "결제일자<br/>/환불일자", width: "150px", attributes : {style : "text-align: center;"} }
    },
    orderPaymentType: function(row){
        return { field : "orderPaymentType", title : "결제수단<br/>/환불수단", width: "80px", attributes : {style : "text-align:center;"} }
    },
    sellersNm: function(row){
        return { field : "sellersNm", title : "판매처", width: "80px", attributes : {style : "text-align:center;"} }
    },
    sellersGroupCd: function(row){
        return { field : "sellersGroupCd", title : "판매처", width: "80px", attributes : {style : "text-align:center;"} }
    },


    odid: function(row){
        return { field : "odid", title : "BOS<br/>주문번호", width: "150px", attributes : {style : "text-align:center;"} }
    },
    odOrderDetlId: function(row){
        return { field : "odOrderDetlId", title : "주문 <br/>상세번호", width: "120px", attributes : {style : "text-align:center;"} }
    },
    urSupplierId: function(row){
        return { field : "urSupplierId", title : "공급업체", width: "120px", attributes : {style : "text-align:center;"} }
    },
    compNm: function(row){
        return { field : "compNm", title : "공급업체", width: "120px", attributes : {style : "text-align:center;"} }
    },

    warehouseName: function(row){
        return { field : "warehouseName", title : "출고처", width: "150px", attributes : {style : "text-align:center;"} }
    },
    ilItemCd: function(row){
        return { field : "ilItemCd", title : "품목코드<br/>/품목바코드", width: "150px", attributes : {style : "text-align:left;"},
		    	template : function(row){
		            let itemBarcode = row.itemBarcode;
		            let itemBarcodeTxt = "";
		            if (itemBarcode != ""){
		                itemBarcodeTxt = "<br/>" + row.itemBarcode + "";
		            }
		            return row.ilItemCd + itemBarcodeTxt;
		        }
        	}
    },
    ilGoodsId: function(row){
        return { field : "ilGoodsId", title : "상품코드", width: "150px", attributes : {style : "text-align:center;"} }
    },
    goodsNm: function(row){
        return { field : "goodsNm", title : "상품명", width: "150px", attributes : {style : "text-align:left;"} }
    },
    goodsTpCd: function(row){
        return { field : "goodsTpCd", title : "상품유형", width: "100px", attributes : {style : "text-align:center;"} }
    },
    orderCnt: function(row){
        return { field : "orderCnt", title : "수량", width: "100px", attributes : {style : "text-align:right;"} , format: "{0:n0}" }
    },
    recommendedPrice: function(row){
        return { field : "recommendedPrice", title : "정상가", width: "150px", attributes : {style : "text-align:right;"} , format: "{0:n0}" }
    },
    discountPrice: function(row){
        return { field : "discountPrice", title : "즉시<br/>할인금액", width: "150px", attributes : {style : "text-align:right;"} , format: "{0:n0}" }
    },
    directPrice: function(row){
        return { field : "directPrice", title : "즉시<br/>할인금액", width: "150px", attributes : {style : "text-align:right;"} , format: "{0:n0}" }
    },
    discountEmployeePrice: function(row){
        return { field : "discountEmployeePrice", title : "임직원<br/>할인", width: "150px", attributes : {style : "text-align:right;"} , format: "{0:n0}" }
    },
    discountType: function(row){
    	  return { field : "discountType", title : "즉시할인<br/>할인유형", width: "150px", attributes : {style : "text-align:left;"} }
    },
    salePrice: function(row){
  	  return { field : "salePrice", title : "판매가", width: "150px", attributes : {style : "text-align:right;"} , format: "{0:n8}" }
    },
    goodsCouponNm: function(row){
    	  return { field : "goodsCouponNm", title : "상품쿠폰명", width: "150px", attributes : {style : "text-align:left;"} }
    },
    goodsCouponPrice: function(row){
  	  return { field : "goodsCouponPrice", title : "상품쿠폰<br/>할인금액", width: "150px", attributes : {style : "text-align:right;"} , format: "{0:n0}" }
    },
    cartCouponNm: function(row){
    	  return { field : "cartCouponNm", title : "장바구니<br/>쿠폰명", width: "150px", attributes : {style : "text-align:left;"} }
    },
    cartCouponPrice: function(row){
    	  return { field : "cartCouponPrice", title : "장바구니쿠폰<br/>할인금액", width: "150px", attributes : {style : "text-align:right;"} , format: "{0:n0}" }
    },
    orderPrice: function(row){
  	  return { field : "orderPrice", title : "주문금액", width: "150px", attributes : {style : "text-align:right;"} , format: "{0:n0}" }
    },
    paidPriceGoods: function(row){
    	  return { field : "paidPriceGoods", title : "결제금액<br/>(배송비제외)", width: "150px", attributes : {style : "text-align:right;"} , format: "{0:n0}" }
    },
    totalSalePrice: function(row){
        return { field : "totalSalePrice", title : "예상상품매출<br/>(VAT포함)", width: "150px", attributes : {style : "text-align:right;"} , format: "{0:n8}" }
    },
    totalSaleNonTaxPrice: function(row){
        return { field : "totalSaleNonTaxPrice", title : "예상상품매출<br/>(VAT별도)", width: "150px", attributes : {style : "text-align:right;"} , format: "{0:n8}" }
    },
    taxablePrice: function(row){
    	  return { field : "taxablePrice", title : "매출금액", width: "150px", attributes : {style : "text-align:right;"} , format: "{0:n8}" }
    },
    settlePrice: function(row){
  	  return { field : "settlePrice", title : "매출금액", width: "150px", attributes : {style : "text-align:right;"} , format: "{0:n8}" }
 	},

    nonTaxablePrice: function(row){
  	  return { field : "nonTaxablePrice", title : "매출금액<br/>(VAT제외)", width: "150px", attributes : {style : "text-align:right;"} , format: "{0:n8}" }
    },
    vatRemoveSettlePrice: function(row){
    	  return { field : "vatRemoveSettlePrice", title : "매출금액<br/>(VAT제외)", width: "150px", attributes : {style : "text-align:right;"} , format: "{0:n0}" }
      },

    vat: function(row){
    	  return { field : "vat", title : "VAT금액", width: "150px", attributes : {style : "text-align:right;"} , format: "{0:n8}" }
      },
    taxYn: function(row){
    	  return { field : "taxYn", title : "과세구분", width: "100px", attributes : {style : "text-align:center;"} }
    },
    orderChangeYn: function(row){
  	  return { field : "orderChangeYn", title : "통합<br/>ERP I/F 여부", width: "100px", attributes : {style : "text-align:center;"} }
    },
    outmallId: function(row){
        return { field : "outmallId", title : "외부몰<br/>주문번호", width: "150px", attributes : {style : "text-align:center;"} }
    },
    outmallDetailId: function(row){
        return { field : "outmallDetailId", title : "외부몰<br/>주문상세번호", width: "150px", attributes : {style : "text-align:center;"} }
    },
    collectionMallId: function(row){
        return { field : "collectionMallId", title : "수집몰<br/>주문번호", width: "150px", attributes : {style : "text-align:center;"} }
    },
    buyerTypeCdNm: function(row){
        return { field : "buyerTypeCdNm", title : "주문자<br/>유형", width: "100px", attributes : {style : "text-align:center;"} }
    },
    payTpNm: function(row){
        return { field : "payTpNm", title : "결제수단", width: "100px", attributes : {style : "text-align:center;"} }
    },
    pgServiceNm: function(row){
        return { field : "pgServiceNm", title : "PG", width: "100px", attributes : {style : "text-align:center;"} }
    },
//    orderPrice: function(row){
//        return { field : "orderPrice", title : "주문금액", width: "150px", attributes : {style : "text-align:right;"}  , format: "{0:n0}"}
//    },
//    salePrice: function(row){
//        return { field : "salePrice", title : "판매가", width: "150px", attributes : {style : "text-align:right;"} , format: "{0:n0}" }
//    },

    shippingPrice: function(row){
        return { field : "shippingPrice", title : "배송비", width: "150px", attributes : {style : "text-align:right;"} , format: "{0:n0}" }
    },
    paymentPrice: function(row){
        return { field : "paymentPrice", title : "결제금액<br/>환불금액", width: "150px", attributes : {style : "text-align:right;"}  , format: "{0:n0}"}
    },
    pointPrice1: function(row){
        return { field : "pointPrice", title : "적립금사용금액", width: "150px", attributes : {style : "text-align:right;"} , format: "{0:n0}" }
    },
    pointPrice2: function(row){
        return { field : "pointPrice", title : "적립금사용금액<br/>(이용권)", width: "150px", attributes : {style : "text-align:right;"} , format: "{0:n0}" }
    },
    pointPrice3: function(row){
        return { field : "pointPrice", title : "적립금사용금액<br/>(무상)", width: "150px", attributes : {style : "text-align:right;"} , format: "{0:n0}" }
    },
    pointPrice: function(row){
        return { field : "pointPrice", title : "적립금사용금액", width: "150px", attributes : {style : "text-align:right;"} , format: "{0:n0}" }
    },
    ticketPointPrice: function(row){
        return { field : "ticketPointPrice", title : "적립금사용금액<br/>(이용권)", width: "150px", attributes : {style : "text-align:right;"} , format: "{0:n0}" }
    },
    freePointPrice: function(row){
        return { field : "freePointPrice", title : "적립금사용금액<br/>(무상)", width: "150px", attributes : {style : "text-align:right;"} , format: "{0:n0}" }
    },
    goodsCouponPrice: function(row){
    	return { field : "goodsCouponPrice", title : "상품쿠폰<br/>할임금액 합계", width: "150px", attributes : {style : "text-align:right;"} , format: "{0:n0}" }
    },
    cartCouponPrice: function(row){
  	  	return { field : "cartCouponPrice", title : "장바구니쿠폰<br/> 할인금액 합계", width: "150px", attributes : {style : "text-align:right;"} , format: "{0:n0}" }
    },
    shippingDiscountPrice: function(row){
  	    return { field : "shippingDiscountPrice", title : "배송비쿠폰<br/>할인금액 합계", width: "150px", attributes : {style : "text-align:right;"} , format: "{0:n0}" }
    },
    odShippingPriceId: function(row){
    	return { field : "odShippingPriceId", title : "배송비번호", width: "150px", attributes : {style : "text-align:center;"} }
    },
    conditionTpNm: function(row){
  	    return { field : "conditionTpNm", title : "조건 배송비 구분", width: "150px", attributes : {style : "text-align:center;"} }
    },
    targetTp: function(row){
    	return { field : "targetTp", title : "귀책 구분", width: "100px", attributes : {style : "text-align:center;"}, template : function(row){
    			let targetTpTxt = "";
    			if(row.targetTp == 'B'){
    				targetTpTxt = "구매자";
    			} else if (row.targetTp == 'S'){
    				targetTpTxt = "판매자";
    			}
	            return targetTpTxt;
	        }
    	}
    },
    shippingPrice: function(row){
  	    return { field : "shippingPrice", title : "배송비<br/>(클레임배송비)", width: "150px", attributes : {style : "text-align:right;"} , format: "{0:n0}" }
    },
    returnShippingPrice: function(row){
        return { field : "returnShippingPrice", title : "환불배송비", width: "150px", attributes : {style : "text-align:right;"} , format: "{0:n0}" }
    },
    shippingDiscountPrice: function(row){
    	return { field : "shippingDiscountPrice", title : "배송비<br/>할인금액", width: "150px", attributes : {style : "text-align:right;"} , format: "{0:n0}" }
    },
    settleDt: function(row){
      	return { field : "settleDt", title : "배송중일자<br/>(반품승인일자)", width: "150px", attributes : {style : "text-align:center;"} }
    },
    storeName: function(row){
      	return { field : "storeName", title : "매장명", width: "100px", attributes : {style : "text-align:center;"} }
    },
    urUserId: function(row){
        return { field : "urUserId", title : "고객 ID", width: "100px", attributes : {style : "text-align:center;"} }
    },
}

var calOrderGridUtil = {
    /** 상품 정산 내역 */
    calGoodsList: function(){
        return [
            calOrderGridColUtil.no(this),
            calOrderGridColUtil.orderType(this),
            calOrderGridColUtil.agentTypeCd(this),
            calOrderGridColUtil.goodsSettleDt(this),
            calOrderGridColUtil.goodsOrderDt(this),
            calOrderGridColUtil.paymentDt(this),
            calOrderGridColUtil.pgServiceNm(this),
            calOrderGridColUtil.orderPaymentType(this),
            calOrderGridColUtil.sellersNm(this),
            calOrderGridColUtil.odid(this),
            calOrderGridColUtil.odOrderDetlId(this),
            calOrderGridColUtil.urSupplierId(this),
            calOrderGridColUtil.warehouseName(this),
            calOrderGridColUtil.ilItemCd(this),
            calOrderGridColUtil.ilGoodsId(this),
            calOrderGridColUtil.goodsNm(this),
            calOrderGridColUtil.goodsTpCd(this),
            calOrderGridColUtil.orderCnt(this),
            calOrderGridColUtil.recommendedPrice(this),
            //calOrderGridColUtil.discountPrice(this),
            calOrderGridColUtil.directPrice(this),
            calOrderGridColUtil.discountEmployeePrice(this),
            //calOrderGridColUtil.discountType(this),
            calOrderGridColUtil.salePrice(this),
            //calOrderGridColUtil.goodsCouponNm(this),
            calOrderGridColUtil.goodsCouponPrice(this),
            //calOrderGridColUtil.cartCouponNm(this),
            calOrderGridColUtil.cartCouponPrice(this),
            calOrderGridColUtil.orderPrice(this),
            calOrderGridColUtil.paidPriceGoods(this),
            calOrderGridColUtil.totalSalePrice(this),
            calOrderGridColUtil.totalSaleNonTaxPrice(this),
            calOrderGridColUtil.taxablePrice(this),
            calOrderGridColUtil.nonTaxablePrice(this),
            calOrderGridColUtil.vat(this),
            calOrderGridColUtil.taxYn(this),
            calOrderGridColUtil.orderChangeYn(this),
            calOrderGridColUtil.outmallId(this),
            calOrderGridColUtil.outmallDetailId(this),
            //calOrderGridColUtil.urUserId(this),
        ]
    },
    /** 상품 정산 (IF 아닌) 내역 */
    calGoodsNotIfList: function(){
        return [
            calOrderGridColUtil.no(this),
            calOrderGridColUtil.orderType(this),
            calOrderGridColUtil.agentTypeCd(this),
            calOrderGridColUtil.goodsSettleDt(this),
            calOrderGridColUtil.goodsOrderDt(this),
            calOrderGridColUtil.paymentDt(this),
            calOrderGridColUtil.pgServiceNm(this),
            calOrderGridColUtil.orderPaymentType(this),
            calOrderGridColUtil.sellersNm(this),
            calOrderGridColUtil.odid(this),
            calOrderGridColUtil.odOrderDetlId(this),
            calOrderGridColUtil.urSupplierId(this),
            calOrderGridColUtil.warehouseName(this),
            calOrderGridColUtil.ilItemCd(this),
            calOrderGridColUtil.ilGoodsId(this),
            calOrderGridColUtil.goodsNm(this),
            calOrderGridColUtil.goodsTpCd(this),
            calOrderGridColUtil.orderCnt(this),
            calOrderGridColUtil.recommendedPrice(this),
            //calOrderGridColUtil.discountPrice(this),
            calOrderGridColUtil.directPrice(this),
            calOrderGridColUtil.discountEmployeePrice(this),
            //calOrderGridColUtil.discountType(this),
            calOrderGridColUtil.salePrice(this),
            //calOrderGridColUtil.goodsCouponNm(this),
            calOrderGridColUtil.goodsCouponPrice(this),
            //calOrderGridColUtil.cartCouponNm(this),
            calOrderGridColUtil.cartCouponPrice(this),
            calOrderGridColUtil.orderPrice(this),
            calOrderGridColUtil.paidPriceGoods(this),
            calOrderGridColUtil.totalSalePrice(this),
            calOrderGridColUtil.totalSaleNonTaxPrice(this),
            //calOrderGridColUtil.taxablePrice(this),
            //calOrderGridColUtil.nonTaxablePrice(this),
            //calOrderGridColUtil.vat(this),
            calOrderGridColUtil.taxYn(this),
            calOrderGridColUtil.orderChangeYn(this),
            calOrderGridColUtil.outmallId(this),
            calOrderGridColUtil.outmallDetailId(this),
            //calOrderGridColUtil.urUserId(this),
        ]
    },
    /** 매장 상품 정산 내역 */
    storeCalGoodsList: function(){
        return [
            calOrderGridColUtil.no(this),
            calOrderGridColUtil.orderType(this),
            calOrderGridColUtil.agentTypeCd(this),
            calOrderGridColUtil.goodsSettleDt(this),
            calOrderGridColUtil.goodsOrderDt(this),
            calOrderGridColUtil.paymentDt(this),
            calOrderGridColUtil.pgServiceNm(this),
            calOrderGridColUtil.orderPaymentType(this),
            calOrderGridColUtil.sellersNm(this),
            calOrderGridColUtil.odid(this),
            calOrderGridColUtil.odOrderDetlId(this),
            calOrderGridColUtil.urSupplierId(this),
            calOrderGridColUtil.warehouseName(this),
            calOrderGridColUtil.storeName(this),
            calOrderGridColUtil.ilItemCd(this),
            calOrderGridColUtil.ilGoodsId(this),
            calOrderGridColUtil.goodsNm(this),
            calOrderGridColUtil.goodsTpCd(this),
            calOrderGridColUtil.orderCnt(this),
            calOrderGridColUtil.recommendedPrice(this),
            //calOrderGridColUtil.discountPrice(this),
            calOrderGridColUtil.directPrice(this),
            calOrderGridColUtil.discountEmployeePrice(this),
            //calOrderGridColUtil.discountType(this),
            calOrderGridColUtil.salePrice(this),
            calOrderGridColUtil.goodsCouponNm(this),
            calOrderGridColUtil.goodsCouponPrice(this),
            calOrderGridColUtil.cartCouponNm(this),
            calOrderGridColUtil.cartCouponPrice(this),
            calOrderGridColUtil.orderPrice(this),
            calOrderGridColUtil.paidPriceGoods(this),
            calOrderGridColUtil.totalSalePrice(this),
            calOrderGridColUtil.totalSaleNonTaxPrice(this),
            calOrderGridColUtil.taxablePrice(this),
            calOrderGridColUtil.nonTaxablePrice(this),
            calOrderGridColUtil.vat(this),
            calOrderGridColUtil.taxYn(this),
            calOrderGridColUtil.orderChangeYn(this),
            calOrderGridColUtil.outmallId(this),
            calOrderGridColUtil.outmallDetailId(this),
            calOrderGridColUtil.urUserId(this),
        ]
    },
    /** 주문정산 */
    calOrderList: function(){
        return [
        	calOrderGridColUtil.no(this),
            calOrderGridColUtil.typeNm(this),
            calOrderGridColUtil.agentTypeCdNm(this),
            calOrderGridColUtil.odid(this),
            calOrderGridColUtil.outmallId(this),
            calOrderGridColUtil.collectionMallId(this),
            calOrderGridColUtil.sellersNm(this),
            calOrderGridColUtil.storeName(this),
            calOrderGridColUtil.buyerTypeCdNm(this),
            calOrderGridColUtil.payTpNm(this),
            calOrderGridColUtil.pgServiceNm(this),
            calOrderGridColUtil.orderDt(this),
            calOrderGridColUtil.approvalDt(this),
            calOrderGridColUtil.salePrice(this),
            calOrderGridColUtil.shippingPrice(this),
            calOrderGridColUtil.paymentPrice(this),
            calOrderGridColUtil.settlePrice(this),
            calOrderGridColUtil.vatRemoveSettlePrice(this),
            calOrderGridColUtil.vat(this),
            calOrderGridColUtil.pointPrice(this),
            calOrderGridColUtil.ticketPointPrice(this),
            calOrderGridColUtil.freePointPrice(this),
            calOrderGridColUtil.goodsCouponPrice(this),
            calOrderGridColUtil.cartCouponPrice(this),
            calOrderGridColUtil.shippingDiscountPrice(this),
        ]
    },
    /** 택배비정산 */
    calDeliveryList: function(){
        return [
            calOrderGridColUtil.no(this),
            calOrderGridColUtil.divNm(this),
            calOrderGridColUtil.odid(this),
            calOrderGridColUtil.pgServiceNm(this),
            calOrderGridColUtil.outmallId(this),
            calOrderGridColUtil.collectionMallId(this),
            calOrderGridColUtil.sellersNm(this),
            calOrderGridColUtil.warehouseName(this),
            calOrderGridColUtil.storeName(this),
            calOrderGridColUtil.compNm(this),
            calOrderGridColUtil.odShippingPriceId(this),
            calOrderGridColUtil.conditionTpNm(this),
            calOrderGridColUtil.targetTp(this),
            calOrderGridColUtil.shippingPrice(this),
            calOrderGridColUtil.returnShippingPrice(this),
            calOrderGridColUtil.shippingDiscountPrice(this),
            calOrderGridColUtil.settleDt(this),
        ]
    },
}