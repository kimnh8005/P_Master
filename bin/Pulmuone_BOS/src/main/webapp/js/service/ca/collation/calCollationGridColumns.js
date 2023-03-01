/**-----------------------------------------------------------------------------
 * description 		 : 주문정산 그리드 컬럼 설정
 * @
 * @ 수정일			수정자          수정내용
 * @ ------------------------------------------------------
 * @ 2021.03.05		이명수   최초생성
 * @
 * **/

var calCollationGridColUtil = {
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
        return { field : "ouNm", title : "부문 구분", width: "100px", attributes : {style : "text-align:center;"} }
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
        return { field : "erpRegalNm", title : "소속", width: "120px", attributes : {style : "text-align:center;text-decoration: underline;"} }
    },
    odid: function(row){
        return { field : "odid", title : "주문번호", width: "120px", attributes : {style : "text-align:center;text-decoration: underline;"} }
    },
    odOrderDetlSeq: function(row){
        return { field : "odOrderDetlSeq", title : "상세번호", width: "60px", attributes : {style : "text-align:center;text-decoration: underline;"} }
    },
    ilGoodsId: function(row){
        return { field : "ilGoodsId", title : "상품코드", width: "80px", attributes : {style : "text-align:center;"} }
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
        return { field : "goodsNm", title : "상품명", width: "250px", attributes : {style : "text-align:center;text-decoration: underline;"} }
    },
    orderStatus: function(row){
        return { field : "orderStatus", title : "주문상태", width: "80px", attributes : {style : "text-align:center;"} }
    },
    orderCnt: function(row){
        return { field : "orderCnt", title : "수량", width: "60px", attributes : {style : "text-align:right;"} , format: "{0:n0}"}
    },
    standardPrice: function(row){
        return { field : "standardPrice", title : "정상가", width: "80px", attributes : {style : "text-align: right;"}, format: "{0:n0}"}
    },
    recommendedPrice: function(row){
        return { field : "recommendedPrice", title : "판매가", width: "80px", attributes : {style : "text-align: right;"}, format: "{0:n0}"}
    },
    supportPrice: function(row){
        return { field : "supportPrice", title : "회사지원액", width: "80px", attributes : {style : "text-align: right;"}, format: "{0:n0}"}
    },
    createDt: function(row){
        return { field : "createDt", title : "주문일자", width: "150px", attributes : {style : "text-align:center;"} }
    },
    icDt: function(row){
        return { field : "icDt", title : "결제일자", width: "150px", attributes : {style : "text-align:center;"} }
    },

    successCnt: function(row){
        return { field : "successCnt", title : "정상", width: "150px", attributes : {style : "text-align:center;"} , format: "{0:n0}",
            template : function(row){
                             return row.successCnt + ' 건';
                         }
        }
    },
    failCnt: function(row){
        return { field : "failCnt", title : "실패", width: "150px", attributes : {style : "text-align:center;"} , format: "{0:n0}",
            template : function(row){
                             return row.failCnt + ' 건';
                         }
        }
    },
    totalCnt: function(row){
        return { field : "totalCnt", title : "대사 상세 내역", width: "200px", attributes : {style : "text-align:center;"} , format: "{0:n0}",
             template : function(row){
                             return row.totalCnt + ' 건';
                         }
        }
    },

    pgTotalCnt: function(row){
        return { field : "pgTotalCnt", title : "PG 대사 상세 내역", width: "200px", attributes : {style : "text-align:center;"} , format: "{0:n0}",
            template : function(row){
                                     return row.pgTotalCnt + ' 건';
                                 }
        }
    },

    createInfo: function(row){
        return { field : "createInfo", title : "관리자", width: "250px", attributes : {style : "text-align:center;"},
        	 template : function(row){
                 return row.userNm + '/' + row.createId;
             }
        }
    },

    createDt: function(row){
        return { field : "createDt", title : "등록일자", width: "250px", attributes : {style : "text-align:center;text-decoration: underline;"} }
    },

    admin: function(row){
        return { field : "admin", title : "관리", width: "200px", attributes : {style : "text-align:center;"} ,
//        	template : kendo.template($("#command-template").html())
            template : function(row){
                let rtnValue = "";
                if(row.successCnt > 0) {
                    rtnValue += $("#command-detail-template").html();
                }
                if(row.failCnt > 0) {
                    rtnValue += $("#command-template").html();
                }

                return rtnValue;
            }

        }
    },


    uploadSellersNm: function(row){
        return { field : "uploadSellersNm", title : "판매처", width: "250px", attributes : {style : "text-align:center;"} }
    },

    uploadOutmallDetailId: function(row){
        return { field : "uploadOutmallDetailId", title : "외부몰<br/> 주문번호", width: "250px", attributes : {style : "text-align:center;"} }
    },

    orderAmt: function(row){
        return { field : "orderAmt", title : "매출금액", width: "250px", attributes : {style : "text-align:right;"} , format: "{0:n0}"}
    },

    contrastAmt: function(row){
        return { field : "contrastAmt", title : "대비금액", width: "250px", attributes : {style : "text-align:right;"} , format: "{0:n0}"}
    },

    settlePrice: function(row){
        return { field : "settlePrice", title : "최종<br/>매출금액", width: "250px", attributes : {style : "text-align:right;"} , format: "{0:n0}"}
    },

    supplierNm: function(row){
        return { field : "supplierNm", title : "공급업체명", width: "250px", attributes : {style : "text-align:center;"} }
    },

    odid: function(row){
        return { field : "odid", title : "주문번호", width: "250px", attributes : {style : "text-align:center;"} }
    },

    sellersNm: function(row){
        return { field : "sellersNm", title : "판매처명", width: "250px", attributes : {style : "text-align:center;"} }
    },

    outmallDetailId: function(row){
        return { field : "outmallDetailId", title : "외부몰<br/> 주문번호", width: "250px", attributes : {style : "text-align:center;"} }
    },

    vatRemovePaidPrice: function(row){
        return { field : "vatRemovePaidPrice", title : "매출금액<br/> (VAT 제외)", width: "250px", attributes : {style : "text-align:right;"} , format: "{0:n0}"}
    },

    vat: function(row){
        return { field : "vat", title : "VAT", width: "250px", attributes : {style : "text-align:right;"} , format: "{0:n0}"}
    },

    paidPrice: function(row){
        return { field : "paidPrice", title : "매출금액<br/> (VAT 포함)", width: "250px", attributes : {style : "text-align:right;"} , format: "{0:n0}"}
    },

    odOutMallCompareUploadInfoId : function(row){
        return { field:'odOutMallCompareUploadInfoId', hidden:'true'}
    },

}

var calCollationGridUtil = {
    /** 통합몰 매출 대사 내역 */
    calSalesList: function(){
        return [
            { field:'no'                    ,title : 'No'		,width:'50px' ,attributes:{ style:'text-align:center' }, template: "<span class='row-number'></span>"}
            ,{ field:'erpSettleTypeNm'      ,title : 'ERP 구분'	,width:'90px' ,attributes:{ style:'text-align:center' }}
            ,{ field:'erpOdid'	            ,title : 'ERP 주문번호'	,width:'90px' ,attributes:{ style:'text-align:center' }}
            ,{ field:'erpOdOrderDetlSeq'    ,title : 'ERP 주문<BR>상세번호'	,width:'90px' ,attributes:{ style:'text-align:center' }}
            ,{ field:'erpSettleDt'          ,title : 'ERP 정산<BR>처리일자'	,width:'90px' ,attributes:{ style:'text-align:center' }}
            ,{ field:'settleItemCnt'        ,title : 'ERP 수량'	,width:'90px' ,attributes:{ style:'text-align:right' }, format: "{0:n0}"}
            ,{ field:'settlePrice'      ,title : 'ERP 매출금액<BR>(VAT 포함)'	,width:'90px' ,attributes:{ style:'text-align:right' }, format: "{0:n0}"}
            ,{ field:'vatRemoveSettlePrice'    ,title : '매출금액<BR>(VAT 제외)'	,width:'90px' ,attributes:{ style:'text-align:right' }, format: "{0:n0}"}
            ,{ field:'vat'                  ,title : 'VAT'	,width:'90px' ,attributes:{ style:'text-align:center' }}

            ,{ field:''   ,title : ''	,width:'30px' ,attributes:{ style:'text-align:center; background-color: lightgray;' }}

            ,{ field:'odid'             ,title : '주문번호'	,width:'90px' ,attributes:{ style:'text-align:center' }}
            ,{ field:'odOrderDetlSeq'   ,title : '주문<BR>상세번호'	,width:'90px' ,attributes:{ style:'text-align:center' }}
            ,{ field:'sellersNm'        ,title : '판매처명'	,width:'90px' ,attributes:{ style:'text-align:center' }}
            ,{ field:'warehouseNm'      ,title : '출고처명'	,width:'90px' ,attributes:{ style:'text-align:left' }}
            ,{ field:'settleDt'         ,title : '정산처리일자'	,width:'90px' ,attributes:{ style:'text-align:center' }}
            ,{ field:'ilItemCd'         ,title : '품목코드'	,width:'90px' ,attributes:{ style:'text-align:left' }}
            ,{ field:'itemBarcode'      ,title : '품목바코드'	,width:'90px' ,attributes:{ style:'text-align:left' }}
            ,{ field:'goodsNm'          ,title : '상품명'	,width:'90px' ,attributes:{ style:'text-align:left' }}
            ,{ field:'cnt'              ,title : '수량'	,width:'90px' ,attributes:{ style:'text-align:right' }, format: "{0:n0}"}
            ,{ field:'orderStatusCdNm'  ,title : '주문상태'	,width:'90px' ,attributes:{ style:'text-align:center' }}
            ,{ field:'recommendedPrice' ,title : '정상가'	,width:'90px' ,attributes:{ style:'text-align:right'}, format: "{0:n0}"}
            ,{ field:'directPrice'      ,title : '즉시<BR>할인금액'	,width:'90px' ,attributes:{ style:'text-align:right'}, format: "{0:n0}"}
            ,{ field:'directDiscountInfo'   ,title : '즉시<BR>할인유형'	,width:'90px' ,attributes:{ style:'text-align:center' }}
            ,{ field:'salePrice'            ,title : '판매가'	,width:'90px' ,attributes:{ style:'text-align:right'}, format: "{0:n0}"}
            ,{ field:'paidPrice'            ,title : '매출금액'	,width:'90px' ,attributes:{ style:'text-align:right'}, format: "{0:n0}"}
        ]
    },
    /** 외부몰 주문 대사 */
    calOutmallList: function(){
        return [
            calCollationGridColUtil.no(this),
            calCollationGridColUtil.successCnt(this),
            calCollationGridColUtil.failCnt(this),
            calCollationGridColUtil.totalCnt(this),
            calCollationGridColUtil.createInfo(this),
            calCollationGridColUtil.createDt(this),
            calCollationGridColUtil.admin(this),
            calCollationGridColUtil.odOutMallCompareUploadInfoId(this),
        ]
    },
    /** 외부몰 주문 상세내역 대사 */
    calOutmallDetlList: function(){
        return [
            calCollationGridColUtil.no(this),
            calCollationGridColUtil.uploadSellersNm(this),
            calCollationGridColUtil.uploadOutmallDetailId(this),
            calCollationGridColUtil.orderAmt(this),
            calCollationGridColUtil.contrastAmt(this),
            calCollationGridColUtil.settlePrice(this),
            calCollationGridColUtil.supplierNm(this),
            calCollationGridColUtil.odid(this),
            calCollationGridColUtil.sellersNm(this),
            calCollationGridColUtil.outmallDetailId(this),
            calCollationGridColUtil.vatRemovePaidPrice(this),
            calCollationGridColUtil.vat(this),
            calCollationGridColUtil.paidPrice(this),
        ]
    },

    /** PG 거래 내역 대사 */
    calPgList: function(){
        return [
            calCollationGridColUtil.no(this),
            calCollationGridColUtil.successCnt(this),
            calCollationGridColUtil.failCnt(this),
            calCollationGridColUtil.pgTotalCnt(this),
            calCollationGridColUtil.createInfo(this),
            calCollationGridColUtil.createDt(this),
            calCollationGridColUtil.admin(this),
        ]
    },
    /** PG 거래 내역 대사 - 상세 */
    calPgDetlList: function(){
        return [
            { field:'no'                    ,title : 'No'		,width:'50px' ,attributes:{ style:'text-align:center' }, template: "<span class='row-number'></span>"}
            ,{ field:'uploadPgServiceNm'    ,title : 'PG구분'	,width:'90px' ,attributes:{ style:'text-align:center' }}
            ,{ field:'uploadTypeNm'	        ,title : '구분'	,width:'90px' ,attributes:{ style:'text-align:center' }}
            ,{ field:'uploadOdid'	        ,title : '주문번호'	,width:'90px' ,attributes:{ style:'text-align:center' }}
            ,{ field:'uploadTid'            ,title : 'TID'	,width:'90px' ,attributes:{ style:'text-align:center' }}
            ,{ field:'uploadApprovalDt'     ,title : '결제일자<BR>/환불일자'	,width:'90px' ,attributes:{ style:'text-align:center' }}
            ,{ field:'transAmt'     ,title : '결제금액<BR>/환불금액'	,width:'90px' ,attributes:{ style:'text-align:right' }, format: "{0:n0}"}
            ,{ field:'deductAmt'    ,title : '공제금액'	,width:'90px' ,attributes:{ style:'text-align:right' }, format: "{0:n0}"}
            ,{ field:'accountAmt'   ,title : '정산금액'	,width:'90px' ,attributes:{ style:'text-align:right' }, format: "{0:n0}"}

            ,{ field:''   ,title : ''	,width:'30px' ,attributes:{ style:'text-align:center; background-color: lightgray;' }}

            ,{ field:'typeNm'       ,title : '구분'	,width:'90px' ,attributes:{ style:'text-align:center' }}
            ,{ field:'pgServiceNm'  ,title : 'PG구분'	,width:'90px' ,attributes:{ style:'text-align:center' }}
            ,{ field:'payTpNm'      ,title : '결제수단'	,width:'90px' ,attributes:{ style:'text-align:center' }}
            ,{ field:'odid'         ,title : '주문번호'	,width:'90px' ,attributes:{ style:'text-align:center' }}
            ,{ field:'createDt'     ,title : '주문일자'	,width:'90px' ,attributes:{ style:'text-align:center' }}
            ,{ field:'approvalDt'   ,title : '결제일자<BR>(환불일자)'	,width:'90px' ,attributes:{ style:'text-align:center' }}
            ,{ field:'cnt'          ,title : '수량'	,width:'90px' ,attributes:{ style:'text-align:right' }, format: "{0:n0}"}
            ,{ field:'salePrice'    ,title : '주문금액'	,width:'90px' ,attributes:{ style:'text-align:right' }, format: "{0:n0}"}
            ,{ field:'shippingPrice'    ,title : '배송비<BR>합계'	,width:'90px' ,attributes:{ style:'text-align:right' }, format: "{0:n0}"}
            ,{ field:'couponPrice'      ,title : '쿠폰할인금액<BR>합계'	,width:'90px' ,attributes:{ style:'text-align:right' }, format: "{0:n0}"}
            ,{ field:'pointPrice'       ,title : '적립금'	,width:'90px' ,attributes:{ style:'text-align:right' }, format: "{0:n0}"}
            ,{ field:'paymentPrice'     ,title : '결제금액'	,width:'90px' ,attributes:{ style:'text-align:right' }, format: "{0:n0}"}
        ]
    },
}