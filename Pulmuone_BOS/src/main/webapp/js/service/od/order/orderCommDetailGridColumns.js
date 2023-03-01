/**-----------------------------------------------------------------------------
 * description 		 : 주문상세 그리드 컬럼 설정
 * @
 * @ 수정일			수정자          수정내용refundType
 * @ ------------------------------------------------------
 * @ 2020.11.20		강윤경   최초생성
 * @
 * **/

var shippingCompList = [];

function getShippingCompList(){
        var $container = $("<div class='complex-condition'></div>")
        var $ul = $("<ul class='shippingCompList'></ul>")
        fnAjax({
            url     : "/admin/policy/shippingcomp/getDropDownPolicyShippingCompList",
            params  : {},
            async : false,
            success :
                function( data ) {
                    shippingCompList = data.rows;
                }
        });
}

// 택배사 목록 템플릿
function shippingCompListTemplate() {

    var h = "<select name='psShippingCompId' class='shippingCompList'>";
    h += "<option value=''>선택</option>";
    if (Array.isArray(shippingCompList) && shippingCompList.length) {
        for (var i = 0; i < shippingCompList.length; i++) {
            h += "<option value='"+ shippingCompList[i].psShippingCompId +"'>"+ shippingCompList[i].shippingCompNm +"</option>";
        }
    }
    h += "</select>";

    return h;
}
var orderDetailGridColUtil = {
    checkbox: function(row){
        return { field : "chk", headerTemplate : '<input type="checkbox" id="checkBoxAll" />',
            template : '<input type="checkbox" name="rowCheckbox" class="k-checkbox" />',
            width : "32px", attributes : {style : "text-align:center;"}, locked: true, lockable: false
        }
    },
    // 미출주문 상세리스트  체크박스
    unreleasedCheckbox : function(row){
        return { field : "chk", headerTemplate : '<input type="checkbox" id="checkBoxAll" />',
            template :  function(row){
                let str = '<input type="checkbox" name="rowCheckbox" class="k-checkbox" />';
                if ( row.missProcessYn == "NONE") {
                    str = "";
                }
                return str;
            },
            width : "32px", attributes : {style : "text-align:center;"}, locked: true, lockable: false
        }
        /*
    	return { field : "chk", width : "50px", attributes : {style : "text-align:center;"},
    		headerTemplate : '<input type="checkbox" id="'+ headerColName +'" name="checkBoxAll" class="rowAllCheckbox"/>',
    		template : function(row){
                let str = "";
                if (fnIsEmpty(row.odClaimId)) {
                	str = '<input type="checkbox" id="'+colId+'" name="'+ colName +'" class="rowCheckbox k-checkbox" />';
                }
                return str;
    		}
    	}
    	 */
    },
    no: function(row, lockYn){
        return { field : "no", title : "No", width: "60px", attributes : {style : "text-align:center;"}, template : "<span class='row-number'></span>", locked: lockYn != true ? false : true, lockable: lockYn != true ? false : true}
    },
    odid: function(row, lockYn){
        return { field : "odid", title : "주문번호", width: "120px", attributes : {style : "text-align:center; text-decoration: underline; font-weight : bold; cursor: pointer;"}, locked: lockYn != true ? false : true, lockable: lockYn != true ? false : true }
    },
    odOrderDetlSeq: function(row, lockYn){
        return { field : "odOrderDetlSeq", title : "상세번호", width: "60px", attributes : {style : "text-align:center; text-decoration: underline; font-weight : bold; cursor: pointer;"}, locked: lockYn != true ? false : true, lockable: lockYn != true ? false : true }
    },
    orderStatus: function(row){
        return {
            field: "orderStatus", title: "주문상태", width: "80px", attributes: {style: "text-align:center;"},
            template: function (row) {
                let orderStatus = row.orderStatus;
                //렌탈주문인경우 주문상태변경 (orderBosJson 참조)
                if (fnNvl(row.orderBosJson) != "") {
                    let rentalOrderStatus = fnNvl(JSON.parse(row.orderBosJson)[1].rows[0].rentalTypeStatusNm);
                    orderStatus = rentalOrderStatus != "" ? rentalOrderStatus : row.orderStatus;
                }
                return orderStatus;
            }
        }
    },
    orderStatusForOrderDetailList: function(row){
        return { field : "orderStatus", title : "주문</br>상세상태", width: "80px", attributes : {style : "text-align:center;"},
                 template : function(row){
                    let orderStatus = row.orderStatus;
                    //렌탈주문인경우 주문상태변경 (orderBosJson 참조)
                    if(fnNvl(row.orderBosJson) != "") {
                       let rentalOrderStatus = fnNvl(JSON.parse(row.orderBosJson)[1].rows[0].rentalTypeStatusNm);
                        orderStatus = rentalOrderStatus != "" ? rentalOrderStatus : row.orderStatus;
                    }
                return orderStatus;
            }
        }
    },
    claimOrderStatus: function(row) {
        return {field: "orderClaimStatus", title: "클레임구분", width: "80px", attributes: {style: "text-align:center;"}}
    },
    claimClaimOrderStatus: function(row){
        return { field : "orderClaimStatus", title : "클레임상태", width: "80px", attributes : {style : "text-align:center;"},
            template: function (row) {
                let orderClaimStatus = row.orderClaimStatus;
                //렌탈주문인경우 주문상태변경 (claimBosJson 참조)
                if (fnNvl(row.claimBosJson) != "") {
                    let rentalOrderClaimStatus = fnNvl(JSON.parse(row.claimBosJson)[1].rows[0].rentalTypeStatusNm);
                    orderClaimStatus = rentalOrderClaimStatus != "" ? rentalOrderClaimStatus : row.orderClaimStatus;
                }
                return orderClaimStatus;
            }
        }
    },
    claimClaimOrderStatusForOrderDetailList: function(row){
        return { field : "orderClaimStatus", title : "클레임</br>상세상태", width: "80px", attributes : {style : "text-align:center;"} }
    },
    lastOrderClaimStatus: function(row){
        return { field : "lastOrderClaimStatus", title : "클레임</br>상세상태", width: "80px", attributes : {style : "text-align:center;"},
                template : function(row){
                    let lastOrderClaimStatus = fnNvl(row.lastOrderClaimStatus);
                    //렌탈주문인경우 주문상태변경 (claimBosJson 참조)
                    if(fnNvl(row.claimBosJson) != "") {
                        let rentalLastOrderClaimStatus = fnNvl(JSON.parse(row.claimBosJson)[1].rows[0].rentalTypeStatusNm);
                        lastOrderClaimStatus = rentalLastOrderClaimStatus != "" ? rentalLastOrderClaimStatus : fnNvl(row.lastOrderClaimStatus);
                    }
                return lastOrderClaimStatus;
                    }
                }
    },
    orderIfDt: function(row){
        return { field : "", title : "주문I/F<br/>(출고예정일)", width: "100px", attributes : {style : "text-align:center;"},
            template : function(row){
                return row.orderIfDt + "<br/>(" + row.shippingDt + ")";
            }
        }
    },
    deliveryDt: function(row){
        return { field : "deliveryDt", title : "도착예정일", width: "100px", attributes : {style : "text-align:center;"} }
    },
    shippingDt: function(row){
        return { field : "", title : "출고예정일<br/>(주문마감시간)", width: "100px", attributes : {style : "text-align:center;"},
            template : function(row){
                return row.shippingDt + "<br/>(" + row.cutoffTime + ")";
            }
        }
    },
    goodsTp: function(row, lockYn){
        return { field : "", title : "상품유형<br/>(출고처)", width: "150px", attributes : {style : "text-align:center;"}, locked: lockYn != true ? false : true, lockable: lockYn != true ? false : true,
            template : function(row){
                return row.goodsTp + "<br/>(" + row.warehouseNm + ")";
            }
        }
    },
     goodsTpForOrderDetailList: function(row){
        return { field : "goodsTp", title : "상품유형", width: "80px", attributes : {style : "text-align:center;"} }
    },
    deliveryTypeNm: function(row, lockYn){
    	return { field : "", title : "주문유형<br/>(출고처)", width: "150px", attributes : {style : "text-align:center;"}, locked: lockYn != true ? false : true, lockable: lockYn != true ? false : true,
    			template : function(row){
    				return row.deliveryTypeNm + "<br/>(" + row.warehouseNm + ")";
    			}
    	}
    },
    ilItemCd: function(row){
        return { field : "ilItemCd", title : "마스터품목코드<br/>(품목바코드)"	, width: "130px", attributes : {style : "text-align:center; text-decoration: underline; font-weight : bold; cursor: pointer;"},
            template : function(row){
                let itemBarcode = row.itemBarcode;
                let itemBarcodeTxt = "";
                if (itemBarcode != ""){
                    itemBarcodeTxt = "<br/>(" + row.itemBarcode + ")";
                }
                return row.ilItemCd + itemBarcodeTxt;
            }
        }
    },
    ilGoodsId: function(row){
        return { field : "ilGoodsId", title : "상품코드", width: "80px", attributes : {style : "text-align:center; text-decoration: underline; font-weight : bold; cursor: pointer;"} }
    },
    goodsDiscountTp: function(row){
        return { field : "goodsDiscountTpNm", title : "할인<br/>유형", width: "80px", attributes : {style : "text-align:center;"} }
    },
    goodsNm: function(row){
        return {
            field : "goodsNm", title : "상품명", width: "320px", attributes : {style : "text-align: left; padding: 5px 5px; cursor: pointer;"},
            editable: function () {return editFlag == null ? false : editFlag;},
            template : function(row){
                let str = "";
                let addSubTxt = orderMgmFunctionUtil.goodNmAddTextView(row);

                str = "<div class='popGoodsClick' style='font-weight : bold; '><u>" + row.goodsNm + "</u></div>" + addSubTxt;
                if (row.goodsTpCd == 'GOODS_TYPE.DAILY') {
                    //str += '<div style="float: right"><button id="orderScheduleChangeBtn" type="button" class="btn-s btn-point">스케쥴</button></div>';
                }

                return str;
            }
        }
    },
    storageType: function(row){
        return { field : "storageType", title : "보관<br/>방법", width: "60px", attributes : {style : "text-align:center;"} }
    },
    orderCnt: function(row){
        return { field : "orderCnt", title : "수량", width: "60px", attributes : {style : "text-align:center;"} }
    },
    orderCntForOrderDetailList: function(row){
        return { field : "orderCnt", title : "주문수량", width: "60px", attributes : {style : "text-align:center;"} }
    },
    missCnt: function(row){
        return { field : "missCnt", title : "미출수량<br/>(선미출수량)", width: "70px", attributes : {style : "text-align:center;"},
            template: function (row) {
                let returnMissCnt = "";

                if(row.missCnt > 0 && row.missClaimCnt == 0){
                    returnMissCnt = row.missCnt;
                } else if(row.missCnt == 0 && row.missClaimCnt > 0){
                    returnMissCnt = "("+row.missClaimCnt+")";
                } else {
                    returnMissCnt = row.missCnt+"<br/>"+"("+row.missClaimCnt+")";
                    if(row.missCnt != row.missClaimCnt){
                        returnMissCnt = '<div style="color: red;">' + returnMissCnt + '</div>';
                    }
                }

                return returnMissCnt;
            }
        }
    },
    processMissCnt: function(row){
        return { field : "processMissCnt", title : "잔여<br/>처리수량", width: "70px", attributes : {style : "text-align:center;"},
            template: function (row) {
                let returnMissCnt = row.missCnt - row.missClaimCnt;

                if(row.missProcessYn == "NONE") {
                    returnMissCnt = 0;
                }
                else if(row.missCnt > 0 && row.missClaimCnt == 0){
                    returnMissCnt = row.missCnt;
                }

                return returnMissCnt;
            }
        }
    },
    cancelCnt: function(row){
        return { field : "cancelCnt", title : "수량", width: "60px", attributes : {style : "text-align:center;"} }
    },
    cancelCntForOrderDetailList: function(row){
        return { field : "cancelCnt", title : "클레임</br>수량", width: "60px", attributes : {style : "text-align:center;"} }
    },
    calOrderCnt: function(row){
        return {
            field : "orderCnt", title : "주문수량", width: "70px", attributes : {style : "text-align:right;padding-right: 5px;"}, format: "{0:n0}",
            template : function(row){
                return row.orderCnt - row.cancelCnt;
            }
        }
    },
    odShippingZoneId: function(row){
        return { field : "odShippingZoneId", title : "배송<br/>번호", width: "80px", attributes : {style : "text-align:center;"} }
    },
    trackingNo: function(row){
        return { field : "", title : "송장번호(택배사)"	, width: "130px", attributes : {style : "text-align:center;"},
            template : function(row){
                let shippingCompNm = row.shippingCompNm;
                let shippingCompNmTxt = "";
                if (shippingCompNm != ""){
                    shippingCompNmTxt = "<br/>(" + row.shippingCompNm + ")";
                }
                return row.trackingNo + shippingCompNmTxt;
            }
        }
    },
    odShippingZoneId: function(row){
        return { field : "odShippingZoneId", title : "배송<br/>번호", width: "80px", attributes : {style : "text-align:center;"} }
    },
    standardPrice: function(row){
        return { field : "standardPrice", title : "원가", width: "60px", attributes : {style : "text-align: right;"}, format: "{0:n0}"}
    },
    recommendedPrice: function(row){
        return { field : "recommendedPrice", title : "정상가", width: "60px", attributes : {style : "text-align: right;"}, format: "{0:n0}"}
    },
    salePrice: function(row){
        return { field : "salePrice", title : "판매가", width: "60px", attributes : {style : "text-align: right;"}, format: "{0:n0}"}
    },
    couponPrice: function(row){
        return { field : "couponPrice", title : "쿠폰<br/>할인", width: "60px", attributes : {style : "text-align: right;"}, format: "{0:n0}"}
    },
    discountPrice: function(row){
    	return { field : "couponPrice", title : "할인<br/>금액", width: "60px", attributes : {style : "text-align: right;"}, format: "{0:n0}"}
    },
    directPrice: function(row){
    	return { field : "directPrice", title : "즉시<br/>할인", width: "60px", attributes : {style : "text-align: right;"}, format: "{0:n0}"}
    },
    discountEmployeePrice: function(row){
    	return { field : "discountEmployeePrice", title : "임직원<br/>할인", width: "60px", attributes : {style : "text-align: right;"}, format: "{0:n0}"}
    },
    orderPrice: function(row){
        return { field : "orderPrice", title : "주문<br/>금액", width: "60px", attributes : {style : "text-align: right;"}, format: "{0:n0}"}
    },
    paidPrice: function(row){
        return { field : "paidPrice", title : "결제<br/>금액", width: "60px", attributes : {style : "text-align: right;"}, format: "{0:n0}"}
    },
    bosClaimNm: function(row){
        return { field : "bosClaimNm", title : "클레임사유", width: "150px", attributes : {style : "text-align: center;"}, format: "{0:n0}"}
    },
    calPaidPrice: function(row){
        return { field : "paidPrice", title : "결제<br/>금액", width: "60px", attributes : {style : "text-align: right;"}, format: "{0:n0}",
            template : function(row){
                return (row.orderCnt - row.cancelCnt) * row.salePrice;
            }
        }
    },
    cancelPaidPrice: function(row){
        return { field : "paidPrice", title : "결제<br/>금액", width: "60px", attributes : {style : "text-align: right;"}, format: "{0:n0}",
            template : function(row){
                return row.cancelCnt * row.salePrice;
            }
        }
    },
    sellersGroupCdNm: function(row){
        return { field : "omSellersNm", title : "판매처", width: "60px", attributes : {style : "text-align:center;"} }
    },
    idDt: function(row){
        return { field : "icDt", title : "결제완료일자", width: "150px", attributes : {style : "text-align:center;"} }
    },
    caDt: function(row){
        return { field : "caDt", title : "취소요청일자", width: "150px", attributes : {style : "text-align:center;"} }
    },
    crDt: function(row, lockYn){
        return { field : "crDt", title : "클레임요청일자", width: "150px", attributes : {style : "text-align:center;"}, locked: lockYn != true ? false : true, lockable: lockYn != true ? false : true }
    },
    ceDt: function(row){
        return { field : "ceDt", title : "클레임승인일자", width: "150px", attributes : {style : "text-align:center;"}}
    },
    ceDtRefundOrderDetailList: function(row){
        return { field : "ceDt", title : "클레임승인일자", width: "150px", attributes : {style : "text-align:center;"},
            template : function(row){
                let ceDt = row.ceDt;
                if(fnNvl(ceDt) == '') {
                    if(row.claimStatusCd == 'CC') {
                        ceDt = row.ccDt;
                    }else if(row.claimStatusCd == 'RC'){
                        ceDt = row.rcDt;
                    }
                }
                return ceDt;
            }
        }
    },
    diDt: function(row){
        return { field : "diDt", title : "배송중일자", width: "150px", attributes : {style : "text-align:center;"} }
    },
    missDt: function(row){
        return { field : "missDt", title : "미출일자", width: "150px", attributes : {style : "text-align:center;"},
        	template : function(row) {
        		return fnFormatDate(row.missDt, 'yyyy-MM-dd');
            }

        }
    },
    buyerNm: function(row, lockYn){
        return { field : "buyerNm"				, title : "주문자정보"				, width: "80px"	, attributes : {style : "text-align:center;"} , locked: lockYn != true ? false : true, lockable: lockYn != true ? false : true,
            template : function(row) {
                var resultTxt = row.buyerNm;
                if (row.urGroupId != 0) {
                    var loginId = $.trim(row.loginId);
                    if (loginId != "") {

                        if (fnIsProgramAuth("USER_POPUP_VIEW") == true) {
                            return "<u>" + resultTxt + "<br/>" + loginId + "</u>";
                        } else {
                            return resultTxt + "<br/>" + loginId ;
                        }
                    } else {
                        return resultTxt;
                    }
                } else {
                    return resultTxt + " / 비회원";
                }
            }
        }
    },
    recvNm: function(row){
        return { field : "recvNm", title : "수취인명", width: "80px", attributes : {style : "text-align:center;"} }
    },
    omSellersNm: function(row, lockYn){
        return { field : "omSellersNm"				, title : "판매처<br/>(외부몰 주문번호)"				, width: "100px"	, attributes : {style : "text-align:center;"} , locked: lockYn != true ? false : true, lockable: lockYn != true ? false : true,
            template : function(row){
            	let omSellersNm 	= fnIsEmpty(row.omSellersNm) ? row.sellersGroupCdNm : row.omSellersNm;
                let outMallId 		= row.outMallId;
                let outMallIdTxt 	= "";
                if (outMallId != "") {
                    outMallIdTxt = "<br/>(" + row.outMallId + ")";
                }
                return omSellersNm + outMallIdTxt;
            }
        }
    },
    collectionMallId: function(row){
        return { field : "collectionMallId", title : "수집몰<br/>주문번호", width: "100px", attributes : {style : "text-align:center;"},}
    },
    outMallId: function(row){
        return { field : "outMallId", title : "외부몰<br/>주문번호", width: "100px", attributes : {style : "text-align:center;"} }
    },
    claimOdClaimId: function(row, lockYn) {
        return { field : "odClaimId", title : "클레임번호", width: "80px", attributes : {style : "text-align:center; "}, locked: lockYn != true ? false : true, lockable: lockYn != true ? false : true,
            template : function(row){

                var json = JSON.parse(row.bosJson);

                if(stringUtil.getString(json[1].rows[0].actionRows, "") != "") {
                    return "<u style='text-decoration: underline; font-weight : bold; cursor: pointer;'>"+row.odClaimId+"</u>";
                } else {
                    return row.odClaimId;
                }
            }
        }
    },
    refundClaimOdClaimId: function(row) {
        return { field : "odClaimId", title : "클레임번호", width: "80px", attributes : {style : "text-align:center; "},
            template : function(row){
                return "<u style='text-decoration: underline; font-weight : bold; cursor: pointer;' class='refundList'>"+row.odClaimId+"</u>";
            }
        }
    },
    odClaimId: function(row, lockYn){
        return { field : "odClaimId", title : "클레임번호", width: "100px", attributes : {style : "text-align:center; "}, locked: lockYn != true ? false : true, lockable: lockYn != true ? false : true,
            template : function(row){

                var json = JSON.parse(row.bosJson);

                if(stringUtil.getString(json[1].rows[0].actionRows, "") != "") {
                    return "<u style='text-decoration: underline; font-weight : bold; cursor: pointer;'>"+row.odClaimId+"</u>";
                } else {
                    return row.odClaimId;
                }
            }
        }
    },
    missReasonMsg: function(row){
        return { field : "missReasonMsg", title : "미출사유", width: "200px", attributes : {style : "text-align: left; "},
	        template : function(row){
	        	let missReason = fnIsEmpty(row.missReason) ? '' : row.missReason;
	        	let missMsg = fnIsEmpty(row.missMsg) ? '' : row.missMsg;
    			return '미출사유 : ' + missReason + '</br>미출상세사유 : ' + missMsg;
	        }
        }
    },
    psClaimMallMsg: function(row){
        return { field : "psClaimMallMsg", title : "사유", width: "200px", attributes : {style : "text-align:center;"} }
    },
    claimReasonMsg: function(row){
        return { field : "claimReasonMsg", title : "상세사유", width: "200px", attributes : {style : "text-align:center;"} }
    },
    payTp: function(row){
        return { field : "payTp", title : "결제수단", width: "80px", attributes : {style : "text-align:center;"} }
    },
    shippingCompNmEdit: function(row){
        return { field : "shippingCompNm", title : "택배사", width: "150px"	, attributes : {style : "text-align:center;"},
            template: function(row){
                return shippingCompListTemplate();
            },
            editable: function () {
                return false;
            },
        }
    },
    shippingCompNm: function(row){
        return { field : "shippingCompNm", title : "택배사", width: "100px"	, attributes : {style : "text-align:center;"} }
    },
    psShippingCompId: function(row){
        return { field : "psShippingCompId", title : "택배사 PK", hidden: true }
    },
    trackingNoEdit: function(row){
        return { field : "trackingNo", title : "송장번호", width: "150px"	, attributes : {style : "text-align:center;"},
            template: function(row){
                return "<input type='text' name='trackingNo' value='' maxlength='30'/>"
            },
            editable: function () {
                return false;
            }, }
    },
    trackingNo: function(row){
        return { field : "trackingNo", title : "송장번호", width: "100px"	, attributes : {style : "text-align:center;"} }
    },
    agentType: function(row){
        return { field : "agentType", title : "유형", width: "60px"	, attributes : {style : "text-align:center;"} }
    },
    returnsYn: function(row){
        return { field : "returnsYn", title : "회수여부", width: "100px"	, attributes : {style : "text-align:center;"},
        	  template : function(row) {
            	let returnsYn 	= row.returnsYn == 'Y' ? "회수" : "회수안함";
                let returnsFlag = "";
                let recallType = "";
                if (row.returnsYn == "Y") {
                    if(row.recallType == "RECALL_FAIL") returnsFlag = "<br/>(연동실패)";
                    else if(row.recallType == "RECALL_NONE") returnsFlag = "<br/>(연동안함)";
                    else if(row.recallType == "RECALL_SUCCESS") {
                        let shippingInfoStr = stringUtil.getString(row.shippingInfo, "");
                        var shippingInfoArr = []; //배송정보
                        var shippingInfoArrList = [];
                        let arr = [];
                        arr = shippingInfoStr.split('∀'); //'∀'로 split
                        for(i=0; i < arr.length; i ++ ) {
                            let trackingNoList = [];
                            trackingNoList = arr[i].split(',');
                            shippingInfoArr.push(trackingNoList);
                        }

                        //송장번호 + (택배사) 표기
                        //송장번호가 n개 이상인 경우
                        if(shippingInfoArr.length > 1) {
                            for(i=0; i< shippingInfoArr.length; i++) {
                                if(shippingInfoArr[i][1] != '-') {
                                    if(shippingInfoArr[i][0] == '직배송') { //직배송인 경우 링크연결 X
                                        returnsFlag = '<div style="text-align:center; font-weight : bold; ">' + shippingInfoArr[i][1] + "</div>" + '(' + shippingInfoArr[i][0] + ')';
                                    } else {
                                        returnsFlag = '<div name="trackingNoSearch" id= "trackingNoSearch_'+i+'" style="text-align:center; text-decoration: underline; font-weight : bold; cursor: pointer;" data-shipping-comp-Nm="' + shippingInfoArr[i][0] + '" data-shipping-comp-id="' + shippingInfoArr[i][2] + '" data-tracking-url="' + shippingInfoArr[i][3] + '" data-http-request-tp ="' + shippingInfoArr[i][4] + '" data-invoice-param ="' + shippingInfoArr[i][5] + '" data-tracking-Yn ="' + shippingInfoArr[i][6] + '" data-logisticsCd ="' + shippingInfoArr[i][7] + '">'
                                        returnsFlag += shippingInfoArr[i][1] + "</div>" + '(' + shippingInfoArr[i][0] + ')';
                                    }
                                    shippingInfoArrList.push(returnsFlag + '</br>');
                                }
                            }
                            returnsFlag = stringUtil.getString(shippingInfoArrList.join(""));
                        } else {
                            if(shippingInfoArr[0][1] != '-') {
                                if(shippingInfoArr[0][0] == '직배송') { //직배송인 경우 링크연결 X
                                    returnsFlag = '<div style="text-align:center; font-weight : bold;">' + shippingInfoArr[0][1] + "</div>" + '(' + shippingInfoArr[0][0] + ')';
                                } else {
                                    returnsFlag = '<div name="trackingNoSearch" id= "trackingNoSearch" style="text-align:center; text-decoration: underline; font-weight : bold; cursor: pointer;" data-shipping-comp-Nm="' + shippingInfoArr[0][0] + '"  data-shipping-comp-id="' + shippingInfoArr[0][2] + '" data-tracking-url="' + shippingInfoArr[0][3] + '" data-http-request-tp ="' + shippingInfoArr[0][4] + '" data-invoice-param ="' + shippingInfoArr[0][5] + '"  data-tracking-Yn ="' + shippingInfoArr[0][6] + '" data-logisticsCd ="' + shippingInfoArr[0][7] + '">'
                                    returnsFlag += shippingInfoArr[0][1] + "</div>" + '(' + shippingInfoArr[0][0] + ')';
                                }
                            }
                        }

                    }

                    if(returnsFlag != "") recallType = returnsFlag;
                }
                return returnsYn + recallType;
            }
        }
    },
    claimAttcCnt: function(row){
        return { field : "claimAttcCnt", title : "첨부파일", width: "80px", attributes : {style : "text-align:center;"} ,
        	template : function(row){
                if (row.claimAttcCnt === 0) return "X";
                return '<u style="text-align:center; text-decoration: underline; font-weight : bold; cursor: pointer;">(' + row.claimAttcCnt + '개)</u>';
            }
        }
    },
    refundPrice: function(row){
        return { field : "refundPrice", title : "환불금액", width: "80px", attributes : {style : "text-align:center;"}, format: "{0:n0}" }
    },
    refundStatusCd: function(row){
        return { field : "refundStatusCd", title : "환불상태", width: "100px", attributes : {style : "text-align:center;"}}
    },
    lclaimName: function(row){
        return { field : "lclaimName"	, title : "클레임 사유(대)"	, width: "80px", attributes : {style : "text-align:center;"}}
    },
    mclaimName: function(row){
        return { field : "mclaimName"	, title : "클레임 사유(중)"	, width: "80px", attributes : {style : "text-align:center;"}}
    },
    sclaimName: function(row){
        return { field : "sclaimName"	, title : "귀책구분"		, width: "120px", attributes : {style : "text-align:center;"},
        	template : function(row) {
        		if (fnIsEmpty(row.sclaimName)) return '';
        		let targetType = row.sclaimName == 'B' ? '(구매자 귀책)' : '(판매자 귀책)' ;
    			return row.sclaimName + '</br>' + targetType;
            }
        }
    },
    comment: function(row){
        return { field : "comment"		, title : "비고"			, width: "250px", attributes : {style : "text-align:center;"},
        	template : function(row) {
        		let compNm = fnIsEmpty(row.compNm) ? '' : row.compNm;
        		let claimName = fnIsEmpty(row.claimName) ? '' : row.claimName;
				return "<div style='text-align:left;'>" + compNm + " : " + claimName + "</div>";
	        }
        }
    },
    refundType: function(row){
        return { field : "refundType"	, title : "환불수단"		, width: "180px", attributes : {style : "text-align:center;"},
	        template : function(row) {
	    		if (fnIsEmpty(row.refundType)) return '';
	    		let refundType = row.refundType == 'C' ? '무통장 입금' : row.refundType == 'D' ? '원결제 내역' : '';

	    		if (row.refundType == 'D') {
	    			return refundType;
	    		} else if (row.refundType == 'C') {
	    			let refundBankNm 		= fnIsEmpty(row.refundBankNm) ? '' : row.refundBankNm;
	    			let refundAccountNumber = fnIsEmpty(row.refundAccountNumber) ? '' : row.refundAccountNumber;
	    			let refundAccountHolder = fnIsEmpty(row.refundAccountHolder) ? '' : row.refundAccountHolder;
	    			return refundType + '</br>은행명 : ' + row.refundBankNm + '</br>계좌번호 : ' + refundAccountNumber + '</br>예금주 : ' + refundAccountHolder;
	    		}
	        }
        }
    },
	csRefundTp: function(row){
        return { field : "csRefundTp"	, title : "CS환불구분"		, width: "100px", attributes : {style : "text-align:center;"},
	        template : function(row) {
	    		if (fnIsEmpty(row.csRefundTp)) return '';
	    		let csRefundTp = row.csRefundTp == 'CS_REFUND_TP.PAYMENT_PRICE_REFUND' ? '결제금액 환불' : row.csRefundTp == 'CS_REFUND_TP.POINT_PRICE_REFUND' ? '적립금 환불' : '';
	    		let pgConfirm = '';
	    		if (row.csRefundApproveCd == 'APPR_STAT.APPROVED' && row.claimRefundYn != 'Y') {
	    			pgConfirm = '</br>' + '<div style="color: red;">(PG사 확인)</div>';
	    		}
				return csRefundTp + pgConfirm;
	        }
        }
    },
    csRefundApproveNm: function(row){
        return { field : "csRefundApproveNm", title : "승인상태"	, width: "80px", attributes : {style : "text-align:center;"}}
    },
    statusChange: function(row){
    	return { field : "statusChange", title : "상태변경"	, width: "110px", attributes : {style : "text-align:center;"},
	        template : function(row) {
				let returnVal = "";
				let claimStatusTpNm = fnIsEmpty(row.claimStatusTpNm) ? '' : row.claimStatusTpNm;
                if ( row.missProcessYn == "NONE") {
                    if (claimStatusTpNm == '취소') {
                        returnVal = '<div style="color: red;">' + claimStatusTpNm + '</div>'
                    } else {
                        returnVal = claimStatusTpNm;
                    }
                } else {
                    if(stringUtil.getString(row.schDeliveryDt, '') != '') {
                        returnVal = '<button type="button" class="k-button k-button-icontext" style="margin-bottom: 5px;" value="EC" kind="returnDelivery">재배송</button> <button type="button" class="k-button k-button-icontext" style="margin-bottom: 5px; background-color: #80c342;" value="CS" kind="csRefund">CS환불</button>';
                    }else {
                        returnVal = '<button type="button" class="k-button k-button-icontext" style="margin-bottom: 5px; background-color: #f44336;" value="C" kind="cancel">취소</button> <button type="button" class="k-button k-button-icontext" style="margin-bottom: 5px;" value="EC" kind="returnDelivery">재배송</button> <button type="button" class="k-button k-button-icontext" style="margin-bottom: 5px; background-color: #80c342;" value="CS" kind="csRefund">CS환불</button>';
                    }
                }
        	    return returnVal;
	        }
    	}
    },
    management: function(row){
    	return { field : "management", title : "관리"	, width: "110px", attributes : {style : "text-align:center;"},
	        template : function(row) {
				let returnVal = "";
				// 사용자가 등록관리자인 경우, CS환불승인상태에 따라 버튼 셋팅
				if (PG_SESSION.userId == row.apprReqUserId) {
					// 저장 (승인요청, 삭제, 요청취소)
					if (row.csRefundApproveCd == "CS_REFUND_APPR_CD.SAVE") {
						returnVal = '<button type="button" class="k-button k-button-icontext" style="margin-bottom: 5px;" kind="apprStatRequest">승인요청</button> <button type="button" class="k-button k-button-icontext" kind="apprStatDisposal">삭제</button>';
					// 승인요청 (요청취소)
					} else if (row.csRefundApproveCd == "CS_REFUND_APPR_CD.REQUEST") {
						returnVal = '<button type="button" class="k-button k-button-icontext" kind="apprStatCancel">요청취소</button>';
					// 승인반려 (승인요청, 삭제)
					} else if (row.csRefundApproveCd == "CS_REFUND_APPR_CD.DENIED") {
						returnVal = '<button type="button" class="k-button k-button-icontext" style="margin-bottom: 5px;" kind="apprStatRequest">승인요청</button> <button type="button" class="k-button k-button-icontext" kind="apprStatDisposal">삭제</button>';
					}
				}
        	    return returnVal;
	        }
    	}
    },
    odOrderId: function(row){
        return { field : "odOrderId", title : "주문PK", hidden: true }
    },
    odOrderDetlId: function(row){
        return { field : "odOrderDetlId", title : "주문상세PK", hidden: true }
    },
    psClaimMallId: function(row){
        return { field : "psClaimMallId", title : "MALL 클레임 사유 PK", hidden: true }
    },
    claimStatusCd: function(row){
        return { field : "claimStatusCd", title : "클레임 주문상태 코드", hidden: true }
    },
    urWarehouseId: function(row){
        return { field : "urWarehouseId", title : "출고처 PK", hidden: true }
    },
    claimGoodsPrice: function(row){
        return { field : "claimGoodsPrice", title : "클레임 상품금액", hidden: true }
    },
    claimGoodsCouponPrice: function(row){
        return { field : "claimGoodsCouponPrice", title : "클레임 상품쿠폰 금액", hidden: true }
    },
    claimCartCouponPrice: function(row){
        return { field : "claimCartCouponPrice", title : "클레임 장바구니쿠폰금액", hidden: true }
    },
    claimShippingPrice: function(row){
        return { field : "claimShippingPrice", title : "클레임 배송비", hidden: true }
    },
    ifUnreleasedInfoId: function(row){
        return { field : "ifUnreleasedInfoId", title : "미출정보 PK", hidden: true }
    },
    claimAbleCnt : function(row){
        return { field : "claimAbleCnt", title : "클레임</br>가능수량", width: "60px", attributes : {style : "text-align:center;"} }
    },
    buyerTypeCdNm : function(row){
        return { field : "buyerTypeCdNm", title : "주문자유형", width: "60px", attributes : {style : "text-align:center;"} }
    },
    warehouseNmForOrderDetailList : function(row) {
    return {field : "warehouseNm", title : "출고처</br>(배송방법)", width: "100px", attributes : {style :"text-align:center;"},
            template : function(row) {
                let str =  row.warehouseNm;
                if(fnNvl(row.orderStatusDeliTpNm) != '') { //배송방법
                    str += '</br> (' + row.orderStatusDeliTpNm + ')';
                }
                // if(fnNvl(row.urStoreNm) != '') { // 매장명
                //     str += " / </br>" + row.urStoreNm;
                // }
                return str;
                }
            }
    }
}



/* 주문상세리스트 Grid Col*/
var orderDetailGridUtil = {
    orderDetailList: function(){
        return [
            orderDetailGridColUtil.no(this),
            orderDetailGridColUtil.odid(this),
            orderDetailGridColUtil.odOrderDetlSeq(this),
            orderDetailGridColUtil.sellersGroupCdNm(this),
            orderDetailGridColUtil.buyerTypeCdNm(this),
            orderDetailGridColUtil.orderStatusForOrderDetailList(this),
            orderDetailGridColUtil.lastOrderClaimStatus(this),
            orderDetailGridColUtil.warehouseNmForOrderDetailList(this),
            orderDetailGridColUtil.goodsTpForOrderDetailList(this),
            orderDetailGridColUtil.ilItemCd(this),
            orderDetailGridColUtil.ilGoodsId(this),
            orderDetailGridColUtil.goodsNm(this),
            orderDetailGridColUtil.storageType(this),
            orderDetailGridColUtil.orderCntForOrderDetailList(this),
            orderDetailGridColUtil.cancelCntForOrderDetailList(this),
            orderDetailGridColUtil.claimAbleCnt(this),
            orderDetailGridColUtil.orderIfDt(this),
            orderDetailGridColUtil.deliveryDt(this),
            orderDetailGridColUtil.trackingNo(this),
            orderDetailGridColUtil.outMallId(this),
            //orderDetailGridColUtil.standardPrice(this),
            orderDetailGridColUtil.recommendedPrice(this),
            orderDetailGridColUtil.salePrice(this),
            orderDetailGridColUtil.goodsDiscountTp(this),
            orderDetailGridColUtil.directPrice(this),
            orderDetailGridColUtil.discountEmployeePrice(this),
            orderDetailGridColUtil.couponPrice(this),
            orderDetailGridColUtil.orderPrice(this),
            orderDetailGridColUtil.paidPrice(this),
            
            // orderDetailGridColUtil.odShippingZoneId(this),
            // orderDetailGridColUtil.odOrderId(this),
            // orderDetailGridColUtil.odOrderDetlId(this),
        ]
    },
    payCompleteList: function(){
        return [
            orderDetailGridColUtil.checkbox(this),
            orderDetailGridColUtil.no(this),
            orderDetailGridColUtil.idDt(this),
            orderDetailGridColUtil.odid(this),
            orderDetailGridColUtil.odOrderDetlSeq(this),
            orderDetailGridColUtil.omSellersNm(this),
            orderDetailGridColUtil.deliveryTypeNm(this),
            orderDetailGridColUtil.buyerNm(this),
            orderDetailGridColUtil.recvNm(this),
            orderDetailGridColUtil.goodsNm(this),
            orderDetailGridColUtil.calOrderCnt(this),
            orderDetailGridColUtil.orderStatus(this),
            orderDetailGridColUtil.salePrice(this),
            orderDetailGridColUtil.discountPrice(this),
            orderDetailGridColUtil.calPaidPrice(this),
            orderDetailGridColUtil.collectionMallId(this),
            orderDetailGridColUtil.payTp(this),
            orderDetailGridColUtil.agentType(this),
        ]
    },
    cancelReqList: function(){
        return [
            orderDetailGridColUtil.checkbox(this),
            orderDetailGridColUtil.no(this),
            orderDetailGridColUtil.caDt(this),
            orderDetailGridColUtil.odClaimId(this),
            orderDetailGridColUtil.odid(this),
            orderDetailGridColUtil.odOrderDetlSeq(this),
            orderDetailGridColUtil.omSellersNm(this),
            orderDetailGridColUtil.goodsTp(this),
            orderDetailGridColUtil.buyerNm(this),
            orderDetailGridColUtil.recvNm(this),
            orderDetailGridColUtil.goodsNm(this),
            orderDetailGridColUtil.cancelCnt(this),
            orderDetailGridColUtil.claimOrderStatus(this),
            orderDetailGridColUtil.cancelPaidPrice(this),
            orderDetailGridColUtil.claimReasonMsg(this),
            orderDetailGridColUtil.collectionMallId(this),
            orderDetailGridColUtil.payTp(this),
            orderDetailGridColUtil.sellersGroupCdNm(this),
            orderDetailGridColUtil.odOrderId(this),
            orderDetailGridColUtil.odOrderDetlId(this),
        ]
    },deliveryReadyList: function(){
        return [
            orderDetailGridColUtil.checkbox(this),
            orderDetailGridColUtil.no(this),
            orderDetailGridColUtil.shippingDt(this),
            orderDetailGridColUtil.odid(this),
            orderDetailGridColUtil.odOrderDetlSeq(this),
            orderDetailGridColUtil.omSellersNm(this),
            orderDetailGridColUtil.goodsTp(this),
            orderDetailGridColUtil.buyerNm(this),
            orderDetailGridColUtil.recvNm(this),
            orderDetailGridColUtil.goodsNm(this),
            orderDetailGridColUtil.calOrderCnt(this),
            orderDetailGridColUtil.orderStatus(this),
            orderDetailGridColUtil.shippingCompNmEdit(this),
            orderDetailGridColUtil.trackingNoEdit(this),
            orderDetailGridColUtil.collectionMallId(this),
            orderDetailGridColUtil.payTp(this),
            orderDetailGridColUtil.agentType(this),
            orderDetailGridColUtil.odOrderId(this),
            orderDetailGridColUtil.odOrderDetlId(this),
            orderDetailGridColUtil.psShippingCompId(this),
        ]
    },deliveryIngList: function(){
        return [
            orderDetailGridColUtil.checkbox(this),
            orderDetailGridColUtil.no(this),
            orderDetailGridColUtil.diDt(this),
            orderDetailGridColUtil.odid(this),
            orderDetailGridColUtil.odOrderDetlSeq(this),
            orderDetailGridColUtil.omSellersNm(this),
            orderDetailGridColUtil.goodsTp(this),
            orderDetailGridColUtil.buyerNm(this),
            orderDetailGridColUtil.recvNm(this),
            orderDetailGridColUtil.goodsNm(this),
            orderDetailGridColUtil.calOrderCnt(this),
            orderDetailGridColUtil.shippingCompNm(this),
            orderDetailGridColUtil.trackingNo(this),
            orderDetailGridColUtil.collectionMallId(this),
            orderDetailGridColUtil.payTp(this),
            orderDetailGridColUtil.agentType(this),
            orderDetailGridColUtil.odOrderId(this),
            orderDetailGridColUtil.odOrderDetlId(this),
        ]
    }
    // 미출 주문상세리스트
    ,unreleasedOrderList: function(){
        return [
            orderDetailGridColUtil.unreleasedCheckbox(this),	// 체크박스
            orderDetailGridColUtil.no(this),					// 순번
            orderDetailGridColUtil.missDt(this),				// 미출일자
            orderDetailGridColUtil.missReasonMsg(this),			// 미출사유
            orderDetailGridColUtil.odid(this),					// 주문번호
            orderDetailGridColUtil.odOrderDetlSeq(this),		// 상세번호
            orderDetailGridColUtil.omSellersNm(this),			// 판매처(외부몰 주문번호)
            orderDetailGridColUtil.goodsTp(this),				// 상품유형(출고처)
            orderDetailGridColUtil.buyerNm(this),				// 주문자정보
            orderDetailGridColUtil.goodsNm(this),				// 상품명
            orderDetailGridColUtil.missCnt(this),				// 수량
            orderDetailGridColUtil.processMissCnt(this),				// 처리가능수량
            orderDetailGridColUtil.orderStatus(this),			// 주문상태
            orderDetailGridColUtil.recommendedPrice(this),		// 정상가
            orderDetailGridColUtil.salePrice(this),				// 판매가
            orderDetailGridColUtil.orderPrice(this),			// 주문금액
            orderDetailGridColUtil.couponPrice(this),			// 쿠폰할인
            orderDetailGridColUtil.paidPrice(this),				// 결제금액
            orderDetailGridColUtil.bosClaimNm(this),				// 클레임사유
            orderDetailGridColUtil.statusChange(this),			// 상태변경
            orderDetailGridColUtil.collectionMallId(this),		// 수집몰 주문번호
            orderDetailGridColUtil.odOrderId(this),				// 주문PK
            orderDetailGridColUtil.odOrderDetlId(this),			// 주문상세 PK
            orderDetailGridColUtil.ifUnreleasedInfoId(this)		// 미출정보 PK
        ]
    },
    // 반품 주문상세리스트
    returnOrderList: function(){
        return [
            orderDetailGridColUtil.no(this, true),				// 순번
            orderDetailGridColUtil.crDt(this, true),			// 클레임 요청일자
            orderDetailGridColUtil.claimOdClaimId(this, true),	// 클레임번호
            orderDetailGridColUtil.odid(this, true),			// 주문번호
            orderDetailGridColUtil.odOrderDetlSeq(this, true),	// 상세번호
            orderDetailGridColUtil.omSellersNm(this, true),		// 판매처(외부몰 주문번호)
            orderDetailGridColUtil.deliveryTypeNm(this, true),	// 주문유형(출고처)
            orderDetailGridColUtil.buyerNm(this, true),			// 주문자정보
            orderDetailGridColUtil.recvNm(this),				// 수취인명
            orderDetailGridColUtil.goodsNm(this),				// 상품명
            orderDetailGridColUtil.cancelCnt(this),				// 수량
            orderDetailGridColUtil.claimClaimOrderStatus(this),	// 클레임상태
            orderDetailGridColUtil.cancelPaidPrice(this),				// 결제금액
            orderDetailGridColUtil.psClaimMallMsg(this),		// MALL 클레임 사유
            orderDetailGridColUtil.claimReasonMsg(this),		// 상세사유
            orderDetailGridColUtil.returnsYn(this),				// 회수여부
            orderDetailGridColUtil.claimAttcCnt(this),			// 첨부파일
            orderDetailGridColUtil.collectionMallId(this),		// 수집몰 주문번호
            orderDetailGridColUtil.odOrderId(this),				// 주문PK
            orderDetailGridColUtil.odOrderDetlId(this),			// 주문상세 PK
            orderDetailGridColUtil.psClaimMallId(this),			// MALL 클레임 사유 PK
            orderDetailGridColUtil.claimStatusCd(this),			// 클레임 주문상태 코드
            orderDetailGridColUtil.urWarehouseId(this),			// 출고처 PK
            orderDetailGridColUtil.claimGoodsPrice(this),		// 클레임 상품금액
            orderDetailGridColUtil.claimGoodsCouponPrice(this),	// 클레임 상품쿠폰 금액
            orderDetailGridColUtil.claimCartCouponPrice(this),	// 클레임 장바구니쿠폰금액
            orderDetailGridColUtil.claimShippingPrice(this)		// 클레임 배송비
        ]
    },
    // 환불 주문상세리스트
    refundOrderList: function(){
        return [
            orderDetailGridColUtil.no(this),					// 순번
            orderDetailGridColUtil.ceDtRefundOrderDetailList(this),	// 클레임 승인일자
            orderDetailGridColUtil.refundClaimOdClaimId(this),	// 클레임번호
            orderDetailGridColUtil.odid(this),					// 주문번호
            orderDetailGridColUtil.omSellersNm(this),			// 판매처(외부몰 주문번호)
            orderDetailGridColUtil.deliveryTypeNm(this),		// 주문유형(출고처)
            orderDetailGridColUtil.buyerNm(this),				// 주문자정보
            orderDetailGridColUtil.goodsNm(this),				// 상품명
            orderDetailGridColUtil.orderCnt(this),				// 수량
            orderDetailGridColUtil.claimOrderStatus(this),		// 클레임상태
            orderDetailGridColUtil.recommendedPrice(this),		// 정상가
            orderDetailGridColUtil.salePrice(this),				// 판매가
            orderDetailGridColUtil.orderPrice(this),			// 주문금액
            orderDetailGridColUtil.couponPrice(this),			// 쿠폰할인
            orderDetailGridColUtil.refundPrice(this),			// 환불금액
            orderDetailGridColUtil.refundStatusCd(this),		// 환불상태
            orderDetailGridColUtil.odOrderId(this),				// 주문PK
            orderDetailGridColUtil.odOrderDetlId(this),			// 주문상세 PK
            orderDetailGridColUtil.claimStatusCd(this),			// 클레임 주문상태 코드
            orderDetailGridColUtil.urWarehouseId(this),			// 출고처 PK
            orderDetailGridColUtil.claimGoodsPrice(this),		// 클레임 상품금액
            orderDetailGridColUtil.claimGoodsCouponPrice(this),	// 클레임 상품쿠폰 금액
            orderDetailGridColUtil.claimCartCouponPrice(this),	// 클레임 장바구니쿠폰금액
            orderDetailGridColUtil.claimShippingPrice(this)		// 클레임 배송비
        ]
    },
    // CS 환불 승인리스트
    csRefundApprovalOrderList: function() {
    	return [
                { field : "no"                      , title : "No"                  , width: "60px"         , attributes : {style : "text-align:center;"}, template : "<span class='row-number'></span>"    , editable : function(row) {return false;}},
                { field : "apprReqDt"			    , title : "요청일자"				, width: "180px"		, attributes : {style : "text-align:center;"}, editable : function(row) {return false;}},
                { field : "apprChgDt"			    , title : "승인일자"				, width: "180px"		, attributes : {style : "text-align:center;"}, editable : function(row) {return false;}},
                { field : "omSellersNm"			    , title : "판매처"				, width: "100px"		, attributes : {style : "text-align:center;"}, editable : function(row) {return false;}},
                { field : "odid"			        , title : "주문번호"				, width: "180px"		, attributes : {style : "text-align:center; text-decoration: underline; font-weight : bold; cursor: pointer;"}, editable : function(row) {return false;}},
                { field : "odOrderDetlSeq"			, title : "주문상세번호"			, width: "80px"		    , attributes : {style : "text-align:center;"}, editable : function(row) {return false;}},
                { field : "ilItemCd"			    , title : "마스터품목코드"		, width: "100px"		, attributes : {style : "text-align:center;"}, editable : function(row) {return false;}},
                { field : "ilGoodsId"			    , title : "상품코드"				, width: "100px"		, attributes : {style : "text-align:center;"}, editable : function(row) {return false;}},
                { field : "compNm"			        , title : "공급업체"				, width: "120px"		, attributes : {style : "text-align:center;"}, editable : function(row) {return false;}},
                { field : "goodsNm"			        , title : "상품명"				, width: "300px"	    , attributes : {style : "text-align:center;"}, editable : function(row) {return false;}},
                { field : "salesSettleYn"	        , title : "매출여부"				, width: "120px"	    , attributes : {style : "text-align:center;"}, editable : function(row) {return false;}},
                { field : "taxYn"			        , title : "과세구분"				, width: "120px"	    , attributes : {style : "text-align:center;"}, editable : function(row) {return false;}},
                { field : "unreleasedYn"	        , title : "미출여부"				, width: "120px"	    , attributes : {style : "text-align:center;"}, editable : function(row) {return false;}},
                { field : "warehouseNm"		        , title : "출고처"				, width: "180px"		, attributes : {style : "text-align:center;"}, editable : function(row) {return false;}},
                { field : "csRefundTpNm"			, title : "CS환불구분"			, width: "120px"		, attributes : {style : "text-align:center;"}, editable : function(row) {return false;},
                    template : function(row) {
                        let returnVal = row.csRefundTpNm;
                        if (row.csRefundTp == "CS_REFUND_TP.PAYMENT_PRICE_REFUND") {
                            returnVal += '<br /><button type="button" class="k-button k-button-icontext" style="margin-bottom: 5px;background-color: #80c342;color: #ffffff;" kind="refundAccountInfo">환불계좌</button>';
                        }
                        return returnVal;
                    }
                },
                { field : "refundPrice"	            , title : "CS환불금액"			, width: "120px"		, attributes : {style : "text-align:center;"}, format: "{0:n0}",editable : function(row) {return false;}},
                { field : "bosClaimLargeNm"			, title : "클레임사유(대)"		, width: "120px"		, attributes : {style : "text-align:center;"}, editable : function(row) {return false;}},
                { field : "bosClaimMiddleNm"		, title : "클레임사유(중)"		, width: "120px"		, attributes : {style : "text-align:center;"}, editable : function(row) {return false;}},
                { field : "bosClaimSmallNm"         , title : "귀책구분"             , width: "120px"	    , attributes : {style : "text-align:center;"}, editable : function(row) {return false;}},
                { field : "csReasonMsg"			    , title : "상세사유"			    , width: "100px"	    , attributes : {style : "text-align:center;"}, editable : function(row) {return false;}},
                { field : "csRefundApproveCdNm"     , title : "처리상태"             , width: "120px"        , attributes : {style : "text-align:center;"}, editable : function(row) {return false;},
                    template : function(row) {
                        let returnVal = row.csRefundApproveCdNm;
                        if(row.csRefundTp == 'CS_REFUND_TP.PAYMENT_PRICE_REFUND') {
                            let responseData = fnNvl(row.responseData);
                            if(responseData != "") {
                                responseData = JSON.parse(responseData);
                                if(!responseData.success) {
                                    returnVal += "<br/>/실패 : " + responseData.message;
                                }
                            }
                        }
                        return returnVal;
                    }
                },
                orderDetailGridColUtil.management(this),
                { field : "odCsId"                  , title : "CS환불정보PK"         , hidden: true }
    	]
    }
}