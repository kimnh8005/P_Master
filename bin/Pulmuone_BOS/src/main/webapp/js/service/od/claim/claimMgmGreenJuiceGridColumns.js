/**-----------------------------------------------------------------------------
 * description 		 : 주문 상세 관리 그리드 컬럼 설정
 * @
 * @ 수정일			수정자          수정내용
 * @ ------------------------------------------------------
 * @ 2021.01.26		최윤지   		추가생성
 * **/
var orderGreenJuiceGridColUtil = {
    // 상품 추가 정보
    goodNmAddTextView : function(row) {
        let addSubTxt = "";
        let depthId     = row.odOrderDetlDepthId;
        let blankStr    = "";

        if (depthId > 1){
            blankStr = "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;ㄴ ";
        }
        if (row.goodsTpCd == "GOODS_TYPE.DAILY" && row.promotionTp == "CART_PROMOTION_TP.GREENJUICE_SELECT"){

            addSubTxt += blankStr + "&nbsp;&nbsp; 배송기간 : " + row.goodsCycleTermTp;
            addSubTxt += "<br/>"+blankStr+"&nbsp;&nbsp; 배송주기 : " + row.goodsCycleTp;
            if (row.monCnt > 0){ addSubTxt += "<br/>"+blankStr+"&nbsp;&nbsp; 배송요일 : 월요일"; }
            if (row.tueCnt > 0){ addSubTxt += "<br/>"+blankStr+"&nbsp;&nbsp; 배송요일 : 화요일"; }
            if (row.wedCnt > 0){ addSubTxt += "<br/>"+blankStr+"&nbsp;&nbsp; 배송요일 : 수요일"; }
            if (row.thuCnt > 0){ addSubTxt += "<br/>"+blankStr+"&nbsp;&nbsp; 배송요일 : 목요일"; }
            if (row.friCnt > 0){ addSubTxt += "<br/>"+blankStr+"&nbsp;&nbsp; 배송요일 : 금요일"; }


        } else if (row.goodsTpCd == "GOODS_TYPE.DAILY" && row.promotionTp == ""){
            let arrDay =  [];

            if (row.monCnt > 0){ arrDay.push("월"); }
            if (row.tueCnt > 0){ arrDay.push("화"); }
            if (row.wedCnt > 0){ arrDay.push("수"); }
            if (row.thuCnt > 0){ arrDay.push("목"); }
            if (row.friCnt > 0){ arrDay.push("금"); }

            addSubTxt += (stringUtil.getString(row.goodsCycleTermTp, "") != "") ? blankStr + "&nbsp;&nbsp;ㄴ 배송기간 : " + row.goodsCycleTermTp : "";
            addSubTxt += (stringUtil.getString(row.goodsCycleTp, "") != "") ? "<br/>"+blankStr+"&nbsp;&nbsp;ㄴ 배송주기 : " + row.goodsCycleTp : "";
            addSubTxt += (stringUtil.getString(arrDay.join(), "") != "") ? "<br/>"+blankStr+"&nbsp;&nbsp;ㄴ 배송요일 : " + arrDay.join() : "";

        }
        //베이비밀
        if(row.goodsDailyTp == "GOODS_DAILY_TP.BABYMEAL") {
            if (row.dailyBulkYn == 'Y') {//일괄배송일 경우
                addSubTxt += blankStr + "&nbsp;&nbsp;ㄴ 배송유형 : " + ((row.dailyBulkYn == "Y") ? "일괄배송" : "일일배송");
                addSubTxt += "<br/>" + blankStr + "&nbsp;&nbsp;ㄴ 식단유형 : " + ((row.allergyYn == "N") ? "일반식단" : "알러지대체식단");
                addSubTxt += "<br/>" + blankStr + "&nbsp;&nbsp;ㄴ 세트수량 : " + ((row.setCnt != "") ? row.setCnt : 0) + "세트";
            } else {
                addSubTxt += "<br/>" + blankStr + "&nbsp;&nbsp;ㄴ 알러지여부 : " + ((row.allergyYn == "Y") ? "예" : "아니오");
            }
        }
        return addSubTxt;
    },
    goodsTpNm: function(row, editFlag){
        return { field : "goodsTpNm", title : "상품유형<br/>(출고처)", width: "110px", attributes : {style : "text-align:center;"},
            editable: function () {return editFlag == null ? false : editFlag;},
            template : function(row) {
                let str = "<div style='display: none;'>-</div>";
                if (row.goodsTpCd != 'GOODS_TYPE.PACKAGE') {
                    str = row.goodsTpNm + "<br/>(" + row.warehouseNm+")";
                }
                return str;
            }
        }
    },
    ilItemCd: function(row, editFlag){
        return {
            field : "ilItemCd", title : "마스터품목코드<br/>품목바코드", width: "140px", attributes: {style : "text-align:center; text-decoration: underline; cursor: pointer;"},
            editable: function () {return editFlag == null ? false : editFlag;},
            template : function(row){
                let str = "<div style='display: none;'>-</div>";
                if (row.goodsTpCd != 'GOODS_TYPE.PACKAGE') {
                    str = stringUtil.getString(row.ilItemCd, "") + "<br/>" + stringUtil.getString(row.itemBarcode, "");
                }
                return "<div class='popIlItemCdClick'>" + str + "</div>";
            }
        }
    },
    ilGoodsId: function(row, editFlag){
        return {
            field : "ilGoodsId", title : "상품코드" , width: "70px", attributes : {style : "text-align:center; text-decoration: underline; cursor: pointer;"},
            editable: function () {return editFlag == null ? false : editFlag;},
            template : function(row){
                let str = "<div style='display: none;'>-</div>";
                if (row.goodsTpCd != 'GOODS_TYPE.PACKAGE') {
                    str = "<div class='popGoodsClick'>" + row.ilGoodsId + "</div>";
                }
                return str;
            }
        }
    },
    goodsNm: function(row, editFlag) {
        return {
            field: "goodsNm",
            title: "상품명",
            width: "350px",
            attributes: {style: "text-align: left; padding: 5px 5px; cursor: pointer;"},
            editable: function () {
                return editFlag == null ? false : editFlag;
            },
            template: function (row) {
                let str = "";
                let addSubTxt = orderGreenJuiceGridColUtil.goodNmAddTextView(row);
                let depthId = row.odOrderDetlDepthId;
                let blankStr = "";

                // if (depthId > 1) {
                //     blankStr = "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;ㄴ ";
                // }

                if ((row.goodsTpCd == 'GOODS_TYPE.GIFT' || row.goodsTpCd == 'GOODS_TYPE.GIFT_FOOD_MARKETING') && depthId <= 1) {
                    str += "<div class='popGoodsClick' style='float: left; width: 80%; '>" + blankStr + "<u>(증정품) " + row.goodsNm + '</u></div>';
                } else {
                    str = "<div class='popGoodsClick' style=' '>" + blankStr + "<u>" + row.goodsNm + "</u></div>" + addSubTxt;
                }

                return str;
            }
        }
    }
}


var orderMgmGreenJuiceGridUtil = {
    //클레임상세팝업 > 녹즙상품정보 (기본)
    claimGreenjuiceMgmPopupOrderGoodsList : function() {
        return [
            { field : "odOrderDetlSeq"			, title : "주문순번"				, width: "50px"		, attributes : {style : "text-align:center;"}, editable : function(row) {return false;}},
            { field : "orderStatusNm"			, title : "주문상태"				, width: "50px"		, attributes : {style : "text-align:center;"}, editable : function(row) {return false;}},
            orderGreenJuiceGridColUtil.goodsTpNm(this),                               /* 상품유형 / 출고처 */
            orderGreenJuiceGridColUtil.ilItemCd(this),                                /* 마스터품목코드/품목바코드 */
            orderGreenJuiceGridColUtil.ilGoodsId(this),                               /* 상품코드 */
            orderGreenJuiceGridColUtil.goodsNm(this),                                 /* 상품명 */
            { field : "storageTypeNm"		    , title : "보관방법"				, width: "50px"		, attributes : {style : "text-align:center;"}, format: "{0:n0}", editable : function(row) {return false;}}
            , { field : "claimCnt"			    , title : "클레임수량"			, width: "50px"		, attributes : {style : "text-align:center;"}, format: "{0:n0}",editable : function(row) {return false;}}
            , { field : "recommendedPrice"	    , title : "정상가"				, width: "50px"		, attributes : {style : "text-align:center;"}, format: "{0:n0}",editable : function(row) {return false;}}
            , { field : "salePrice"			    , title : "판매가"				, width: "50px"		, attributes : {style : "text-align:center;"}, format: "{0:n0}",editable : function(row) {return false;}}
            , { field : "orderPrice"			, title : "주문금액"				, width: "50px"		, attributes : {style : "text-align:center;"}, format: "{0:n0}",editable : function(row) {return false;}}
            , { field : "couponPrice"           , title : "쿠폰할인"             , width: "50px"	, attributes : {style : "text-align:center;"}, format: "{0:n0}",editable : function(row) {return false;}}
            , { field : "paidPrice"			    , title : "결제금액"				, width: "50px"		, attributes : {style : "text-align:center;"}, format: "{0:n0}",editable : function(row) {return false;}}
            , { field : "odOrderDetlId"		    , hidden : true}						/* 주문 상세 pk */
            , { field : "odOrderDetlParentId"	, hidden : true}						/* 주문상세 부모 ID */
            , { field : "urWarehouseId"		    , hidden : true}						/* 출고처ID(출고처PK) */
        ]
    }
}