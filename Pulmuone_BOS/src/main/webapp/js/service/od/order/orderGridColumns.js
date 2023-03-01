/**-----------------------------------------------------------------------------
 * description 		 : 주문 그리드 컬럼 설정
 * @
 * @ 수정일			수정자          수정내용
 * @ ------------------------------------------------------
 * @ 2020.11.20		강윤경   최초생성
 * @
 * **/


var orderGridUtil = {
	orderList: function(){
        return [
              { field : "odid"				, title : "No"						, width: "40px"		, attributes : {style : "text-align:center"}, template : "<span class='row-number'></span>" }
            , { field : "createDt"			, title : "주문일자"					, width: "100px"	, attributes : {style : "text-align:center;"} }
            , { field : "odOrderId"			, title : "주문PK"					, hidden : true }
            , { field : "odid"			    , title : "주문번호"					, width: "100px"	, attributes : {style : "text-align:center;text-decoration: underline;"} }
            , { field : ""					, title : "판매처<br/>(외부몰 주문번호)"	, width: "100px"	, attributes : {style : "text-align:center;"},
                template : function(dataItem){
                    var resultTxt   = $.trim(dataItem.sellersGroupCdNm);
                    var sellersNm   = $.trim(dataItem.sellersNm);

                    if (sellersNm != ""){
                        resultTxt = sellersNm;
                    }

                    var outMallId   = $.trim(dataItem.outMallId);
                    if (dataItem.sellersGroupCd != "SELLERS_GROUP.MALL" && outMallId != ""){
                        resultTxt += "<br/>" + outMallId;
                    }
                    return resultTxt;
                }
            }
            , { field : "buyerNm"			, title : "주문자정보"					, width: "100px"		, attributes : {style : "text-align:center;"} ,
                template : function(dataItem){
                    var resultTxt   = dataItem.buyerNm;
                    // 권한처리
                	if(dataItem.urGroupId != 0) {
                        var loginId   = $.trim(dataItem.loginId);
                        if (loginId != ""){
                            if (fnIsProgramAuth("USER_POPUP_VIEW") == true) {
                                return "<u>" + resultTxt + "<br/>" + loginId + "</u>";
                            } else {
                                return resultTxt + "<br/>" + loginId;
                            }
                        } else {
                            return resultTxt;
                        }
                	} else {
                		return resultTxt + " / 비회원";
                	}
                }
            }
            , { field : "recvNm"			, title : "수취인명"					, width: "100px"	, attributes : {style : "text-align:center;"} }
            , { field : "recvAddr"			, title : "주소"					    , width: "150px"	, attributes : {style : "text-align:left;"} ,
                template : function(dataItem){
                    return dataItem.recvAddr1 + " " + dataItem.recvAddr2;
                }
            }
            , { field : "goodsNm"			, title : "상품명"					, width: "180px"	, attributes : {style : "text-align:center;"} }
            , { field : "statusNm"			, title : "주문상태"					, width: "60px"		, attributes : {style : "text-align:center;"} ,
                template : function(dataItem){
                    let statusNm = dataItem.statusNm;
                    //렌탈주문인경우 주문상태변경 (orderBosJson 참조)
                    if(fnNvl(dataItem.orderBosJson) != ""){
                        let rentalStatusNm = fnNvl(JSON.parse(dataItem.orderBosJson)[1].rows[0].rentalTypeStatusNm);
                        statusNm = rentalStatusNm != "" ? rentalStatusNm : dataItem.statusNm;
                    }
                        return statusNm;
                    }
            }
            , { field : "claimStatusNm"		, title : "클레임상태"					, width: "60px"		, attributes : {style : "text-align:center;"} ,
               template : function(dataItem){
                    let claimStatusNm = dataItem.claimStatusNm;
                    //렌탈주문인경우 주문상태변경 (claimBosJson 참조)
                    if(fnNvl(dataItem.claimBosJson) != ""){
                        let rentalClaimStatusNm = fnNvl(JSON.parse(dataItem.claimBosJson)[1].rows[0].rentalTypeStatusNm);
                        claimStatusNm = rentalClaimStatusNm != "" ? rentalClaimStatusNm : dataItem.claimStatusNm;
                    }
                        return claimStatusNm;
                    }
            }
            , { field : "orderPrice"		, title : "주문금액"					, width: "60px"		, attributes : {style : "text-align:center;"}, format: "{0:n0}"}
            , { field : "shippingPrice"		, title : "배송비<br/>합계"			, width: "60px"		, attributes : {style : "text-align:center;"}, format: "{0:n0}"}
            , { field : "couponPrice"		, title : "쿠폰할인<br/>합계"			, width: "60px"		, attributes : {style : "text-align:center;"}, format: "{0:n0}"}
            , { field : "paidPrice"			, title : "결제금액"					, width: "60px"		, attributes : {style : "text-align:center;"}, format: "{0:n0}"}
            , { field : "orderPaymentType"	, title : "결제수단"					, width: "60px"		, attributes : {style : "text-align:center;"} }
            , {
                field: "agentType", title: "유형", width: "100px", attributes: {
                    style: "text-align:center;"},
                    template: function (dataItem) {
                        var resultTxt = dataItem.agentType;

                        if (dataItem.orderCopyYn === 'Y') {

                            if (dataItem.orderCopySalIfYn === "Y") {
                                return resultTxt + " (주문복사/매출만연동)";
                            } else {
                                resultTxt = resultTxt + " (주문복사)";
                            }
                        } else if (dataItem.orderCreateYn === 'Y') {
                            resultTxt =resultTxt + " (주문생성)";
                        }
                        return resultTxt;
                    }
            }
            , { field : "collectionMallId"	, title : "수집몰<br/>주문번호"			, width: "60px"	    , attributes : {style : "text-align:center;"} }
        ]
    }
    ,
    shopOrderList: function(){
        return [
              { field : "odid"				, title : "No"						, width: "40px"		, attributes : {style : "text-align:center"}, template : "<span class='row-number'></span>" }
            , { field : "createDt"			, title : "주문일자"					, width: "100px"	, attributes : {style : "text-align:center;"} }
            , { field : "odOrderId"			, title : "주문PK"					, hidden : true }
            , { field : "odid"			    , title : "주문번호"					, width: "100px"	, attributes : {style : "text-align:center;text-decoration: underline;"} }
            , { field : "shopOrderInfo"		, title : "매장명 / 배송방법</br>(회차정보)"	, width: "150px"	, attributes : {style : "text-align:center;"},
                 template : function(dataItem){
                         var resultTxt = dataItem.urStoreNm + " / " + dataItem.deliveryTypeNm + "</br>";
                             resultTxt += "( " + dataItem.storeScheduleNo + "회차 " + dataItem.storeStartTime + " ~ " + dataItem.storeEndTime + " )";

                        return resultTxt;
                    }
            }
            , { field : "deliveryDt"		, title : "도착예정일"					, width: "100px"	, attributes : {style : "text-align:center;"} }
            , { field : "buyerNm"			, title : "주문자정보"					, width: "100px"		, attributes : {style : "text-align:center;"} ,
                template : function(dataItem){
                    var resultTxt   = dataItem.buyerNm;
                    // 권한처리
                	if(dataItem.urGroupId != 0) {
                        var loginId   = $.trim(dataItem.loginId);
                        if (loginId != ""){
                            if (fnIsProgramAuth("USER_POPUP_VIEW") == true) {
                                return "<u>" + resultTxt + "<br/>" + loginId + "</u>";
                            } else {
                                return resultTxt + "<br/>" + loginId;
                            }
                        } else {
                            return resultTxt;
                        }
                	} else {
                		return resultTxt + " / 비회원";
                	}
                }
            }
            , { field : "goodsNm"			, title : "상품명"					, width: "180px"	, attributes : {style : "text-align:center;"} }
            // , { field : ""					, title : "판매처<br/>(외부몰 주문번호)"	, width: "100px"	, attributes : {style : "text-align:center;"},
            //     template : function(dataItem){
            //         var resultTxt   = $.trim(dataItem.sellersGroupCdNm);
            //         var sellersNm   = $.trim(dataItem.sellersNm);
            //
            //         if (sellersNm != ""){
            //             resultTxt = sellersNm;
            //         }
            //
            //         var outMallId   = $.trim(dataItem.outMallId);
            //         if (dataItem.sellersGroupCd != "SELLERS_GROUP.MALL" && outMallId != ""){
            //             resultTxt += "<br/>" + outMallId;
            //         }
            //         return resultTxt;
            //     }
            // }
            , { field : "statusNm"			, title : "주문상태"					, width: "60px"		, attributes : {style : "text-align:center;"} }
            , { field : "claimStatusNm"		, title : "클레임상태"					, width: "60px"		, attributes : {style : "text-align:center;"} }
            , { field : "orderPrice"		, title : "주문금액"					, width: "60px"		, attributes : {style : "text-align:center;"}, format: "{0:n0}"}
            , { field : "shippingPrice"		, title : "배송비<br/>합계"			, width: "60px"		, attributes : {style : "text-align:center;"}, format: "{0:n0}"}
            , { field : "couponPrice"		, title : "쿠폰할인<br/>합계"			, width: "60px"		, attributes : {style : "text-align:center;"}, format: "{0:n0}"}
            , { field : "paidPrice"			, title : "결제금액"					, width: "60px"		, attributes : {style : "text-align:center;"}, format: "{0:n0}"}
            , { field : "orderPaymentType"	, title : "결제수단"					, width: "60px"		, attributes : {style : "text-align:center;"} , hidden : true }
            // , {
            //     field: "agentType", title: "유형", width: "100px", attributes: {
            //         style: "text-align:center;" , hidden : true },
            //         template: function (dataItem) {
            //             var resultTxt = dataItem.agentType;
            //
            //             if (dataItem.orderCopyYn === 'Y') {
            //
            //                 if (dataItem.orderCopySalIfYn === "Y") {
            //                     return resultTxt + " (주문복사/매출만연동)";
            //                 } else {
            //                     resultTxt = resultTxt + " (주문복사)";
            //                 }
            //             } else if (dataItem.orderCreateYn === 'Y') {
            //                 resultTxt =resultTxt + " (주문생성)";
            //             }
            //             return resultTxt;
            //         }
            // }
            , { field : "collectionMallId"	, title : "수집몰<br/>주문번호"			, width: "60px"	    , attributes : {style : "text-align:center;"} , hidden : true }
        ]
    }
}
