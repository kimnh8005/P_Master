/**-----------------------------------------------------------------------------
 * description 		 : 그리드 컬럼 설정
 * @
 * @ 수정일			수정자          수정내용
 * @ ------------------------------------------------------
 * @ 2021.03.03		이명수   최초생성
 * @
 * **/

var calEmployeeGridColUtil = {
    checkbox: function(row){
        return { field : "chk", headerTemplate : '<input type="checkbox" id="checkBoxAll" />',
            width : "32px", attributes : {style : "text-align:center;"}, locked: true, lockable: false,
            template  : function(row){
                return (row.confirmYn == "N") ? '<input type="checkbox" name="rowCheckbox" class="k-checkbox" />': ''
            }
        }
    },
    no: function(row){
        return { field : "no", title : "No", width: "60px", attributes : {style : "text-align:center;"}, template : "<span class='row-number'></span>" }
    },
    settleMonth: function(row){
        return { field : "settleMonth", title : "정산월", width: "120px", attributes : {style : "text-align:center;"},
            template : function(row){
                return row.settleMonth.substring(0, 4) + "년 " + row.settleMonth.substring(4, 6) + "월" ;
            }
        }
    },
    startDt: function(row){
        return { field : "startDt", title : "정산 주문 기간", width: "240px", attributes : {style : "text-align:center;"},
            template : function(row){
                return row.startDt.substring(0, 10) + " ~ " + row.endDt.substring(0, 10);
            }
        }
    },
    ouNm: function(row){
        return { field : "ouNm", title : "부문 구분", width: "200px", attributes : {style : "text-align:center;"} }
    },
    sessionId: function(row){
        return { field : "sessionId", title : "SESSION_ID", width: "100px", attributes : {style : "text-align:center;"} }
    },
    salePrice: function(row){
        return { field : "salePrice", title : "총 금액", width: "100px", attributes : {style : "text-align: right;"}, format: "{0:n0}"}
    },
    confirmYn: function(row){
        return { field : "confirmYn", title : "상태", width: "80px", attributes : {style : "text-align:center;"},
            template : function(row){
                return (row.confirmYn == "Y") ? "확정완료" : "확정대기" ;
            }
        }
    },
    manage: function(row){
        return { field : "manage", title : "관리", width: "auto", attributes : {style : "text-align:center;"},
            template : kendo.template($("#command-template").html())
        }
    },

    buyerNm: function(row){
        return { field : "buyerNm", title : "주문자명<br/>(사번)", width: "100px", attributes : {style : "text-align:center;"},
            template : function(row){
                return row.buyerNm + "<br/>" + row.urEmployeeCd ;
            } }
    },
    erpRegalNm: function(row){
        return { field : "erpRegalNm", title : "소속", width: "120px", attributes : {style : "text-align:center;"} }
    },
    odid: function(row){
        return { field : "odid", title : "주문번호", width: "120px", attributes : {style : "text-align:center;"} }
    },
    odOrderDetlSeq: function(row){
        return { field : "odOrderDetlSeq", title : "상세번호", width: "60px", attributes : {style : "text-align:center;"} }
    },
    ilGoodsId: function(row){
        return { field : "ilGoodsId", title : "상품코드", width: "80px", attributes : {style : "text-align:center;"} }
    },
    masterNm: function(row){
        return { field : "masterNm", title : "임직원<br/>할인그룹", width: "80px", attributes : {style : "text-align:center;"} }
    },
    ilItemCd: function(row){
        return { field : "", title : "마스터품목코드<br/>품목바코드"	, width: "130px", attributes : {style : "text-align:center;"},
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
    goodsNm: function(row){
        return { field : "goodsNm", title : "상품명", width: "250px", attributes : {style : "text-align:center;"} }
    },
    orderStatus: function(row){
        return { field : "orderStatus", title : "주문상태", width: "80px", attributes : {style : "text-align:center;"} }
    },
    orderCnt: function(row){
        return { field : "orderCnt", title : "수량", width: "60px", attributes : {style : "text-align:center;"} }
    },
    standardPrice: function(row){
        return { field : "recommendedPrice", title : "정상가", width: "80px", attributes : {style : "text-align: right;"}, format: "{0:n0}"}
    },
    recommendedPrice: function(row){
        return { field : "salePrice", title : "판매가", width: "80px", attributes : {style : "text-align: right;"}, format: "{0:n0}"}
    },
    supportPrice: function(row){
        return { field : "supportPrice", title : "회사지원액", width: "80px", attributes : {style : "text-align: right;"}, format: "{0:n0}"}
    },
    createDt: function(row){
        return { field : "orderDt", title : "주문일자", width: "110px", attributes : {style : "text-align:center;"} }
    },
    icDt: function(row){
        return { field : "paymentDt", title : "결제일자", width: "110px", attributes : {style : "text-align:center;"} }
    },
}




var calEmployeeGridUtil = {
    /** 임직원 지원금 정산 */
    calEmployeeList: function(){
        return [
            calEmployeeGridColUtil.checkbox(this),
            calEmployeeGridColUtil.no(this),
            calEmployeeGridColUtil.settleMonth(this),
            calEmployeeGridColUtil.startDt(this),
            calEmployeeGridColUtil.ouNm(this),
            calEmployeeGridColUtil.sessionId(this),
            calEmployeeGridColUtil.salePrice(this),
            calEmployeeGridColUtil.confirmYn(this),
            calEmployeeGridColUtil.manage(this),
        ]
    },
    /** 임직원 포인트 사용 현황 */
    calEmployeeUseList: function(){
        return [
            calEmployeeGridColUtil.no(this),
            calEmployeeGridColUtil.ouNm(this),
            calEmployeeGridColUtil.buyerNm(this),
            calEmployeeGridColUtil.erpRegalNm(this),
            calEmployeeGridColUtil.odid(this),
            calEmployeeGridColUtil.odOrderDetlSeq(this),
            calEmployeeGridColUtil.ilGoodsId(this),
            calEmployeeGridColUtil.ilItemCd(this),
            calEmployeeGridColUtil.masterNm(this),
            calEmployeeGridColUtil.goodsNm(this),
            calEmployeeGridColUtil.orderStatus(this),
            calEmployeeGridColUtil.orderCnt(this),
            calEmployeeGridColUtil.standardPrice(this),
            calEmployeeGridColUtil.recommendedPrice(this),
            calEmployeeGridColUtil.supportPrice(this),
            calEmployeeGridColUtil.createDt(this),
            calEmployeeGridColUtil.icDt(this),
        ]
    },
}