/**-----------------------------------------------------------------------------
 * description 		 : 주문 상세 관리 그리드 컬럼 설정
 * @
 * @ 수정일			수정자          수정내용
 * @ ------------------------------------------------------
 * @ 2020.11.25		강윤경   		최초생성
 * @ 2021.01.26		최윤지   		추가생성
 * **/
var claimCntObject = new Object();
var orderGridColUtil = {
    checkbox: function(row, headerColName, colId, colName, area){
        return { field : "chk", width : "50px", attributes : {style : "text-align:center;"},
            headerTemplate : '<input type="checkbox" id="'+ headerColName +'" name="checkBoxAll" class="rowAllCheckbox"/>',
            template : function(row){
                let str = "";

                if((area == "goodsArea" || area == "shippingArea") && row.claimAbleCnt == 0 && row.goodsTpCd != 'GOODS_TYPE.PACKAGE') {
                    return str = '<div><input type="checkbox" data-detailid="'+ row.odOrderDetlParentId +'" class="rowCheckbox" value="'+ row.odOrderDetlId +'"/></div>';
                }
                if (row.goodsTpCd != 'GOODS_TYPE.PACKAGE') {
                    str = '<div style="text-align: right; padding-right: 5px;"><input type="checkbox" id="'+colId+'" name="'+ colName +'" data-detailid="'+ row.odOrderDetlParentId +'" class="rowCheckbox k-checkbox" /></div>';
                } else {
                    str = '<div style="text-align: left; padding-left: 5px;"><input type="checkbox" id="'+colId+'" data-detailid="'+ row.odOrderDetlId +'" class="packageCheckbox k-checkbox" /></div>';
                }

                if(row.claimStatusCode != undefined) { // 클레임상태가 취소완료, 취소거부, 취소철회, 반품완료, 반품거부, 반품철회인경우 체크박스 비노출
                    if(row.claimStatusCode == 'IB' || row.claimStatusCode == 'CC' || row.claimStatusCode == 'CE' || row.claimStatusCode == 'CW' || row.claimStatusCode == 'RC' || row.claimStatusCode == 'RE' || row.claimStatusCode == 'RW') {
                        str = '<div><input type="hidden" data-detailid="'+ row.odOrderDetlParentId +'" class="rowCheckbox"/></div>';
                    }
                }
                return str;
            }
        }
    },
    orderGridCheckbox: function(row, headerColName, colId, colName, area, islock){
        if(islock == null) { islock = false; }
        return { field : "chk", locked: islock, width : "50px", attributes : {style : "text-align:center;"},
            headerTemplate : '<input type="checkbox" id="'+ headerColName +'" name="checkBoxAll" class="rowAllCheckbox"/>',
            template : function(row){
                let str = "";

                if((area == "goodsArea" || area == "shippingArea") && row.claimAbleCnt == 0 && row.goodsTpCd != 'GOODS_TYPE.PACKAGE') {
                    return str = '<div><input type="checkbox" data-detailid="'+ row.odOrderDetlParentId +'" name="'+ colName +'" class="rowCheckbox" value="'+ row.odOrderDetlId +'"/></div>';
                }
                if (row.goodsTpCd != 'GOODS_TYPE.PACKAGE') {
                    str = '<div style="text-align: right; padding-right: 5px;"><input type="checkbox" id="'+colId+'" name="'+ colName +'" data-detailid="'+ row.odOrderDetlParentId +'" class="rowCheckbox k-checkbox" /></div>';
                } else {
                    str = '<div style="text-align: left; padding-left: 5px;"><input type="checkbox" id="'+colId+'" data-detailid="'+ row.odOrderDetlId +'" class="packageCheckbox k-checkbox" /></div>';
                }

                if(row.claimStatusCode != undefined) { // 클레임상태가 취소완료, 취소거부, 취소철회, 반품완료, 반품거부, 반품철회인경우 체크박스 비노출
                    if(row.claimStatusCode == 'IB' || row.claimStatusCode == 'CC' || row.claimStatusCode == 'CE' || row.claimStatusCode == 'CW' || row.claimStatusCode == 'RC' || row.claimStatusCode == 'RE' || row.claimStatusCode == 'RW') {
                        str = '<div><input type="hidden" data-detailid="'+ row.odOrderDetlParentId +'" class="rowCheckbox"/></div>';
                    }
                }
                return str;
            }
        }
    },
    // 주문복사 화면용 체크박스
    orderCopyCheckbox : function(row, headerColName, colId, colName, editFlag) {
        return { field : "chk",	width : "50px",	attributes : { style : "text-align:center;"},
            editable: function () {return editFlag == null ? false : editFlag;},
            headerTemplate : '<input type="checkbox" id="' + headerColName + '" name="checkBoxAll" class="rowAllCheckbox k-checkbox"/>',
            template : function(row) {
                let str = "";
                // 일반상품 또는 묶음상품의 포장상품인 경우 체크박스 활성화
                //if (row.odOrderDetlParentId == 0 || row.odOrderDetlId == row.odOrderDetlParentId) {
                //str = '<input type="checkbox" id="' + colId + '_' + row.rowNum + '" name="' + colName + '" data-detailid="'+ row.odOrderDetlId +'" class="k-checkbox" data-validate-title="복사할 상품 선택"/>';
                //}

                if (row.goodsTpCd != 'GOODS_TYPE.PACKAGE') {
                    str = '<div style="text-align: right; padding-right: 5px;"><input type="checkbox" id="'+colId+'" name="'+ colName +'" data-detailid="'+ row.odOrderDetlParentId +'" class="rowCheckbox k-checkbox" data-validate-title="복사할 상품 선택" /></div>';
                } else {
                    //str = '<div style="text-align: left; padding-left: 5px;"><input type="checkbox" id="'+colId+'" data-detailid="'+ row.odOrderDetlId +'" class="packageCheckbox k-checkbox" data-validate-title="복사할 상품 선택" /></div>';
                }
                return str;
            }
        }
    },
    // 주문생성 화면용 체크박스
    orderCreateCheckbox : function(row, headerColName, colId, colName, editFlag) {
        return { field : "chk",	width : "50px",	attributes : { style : "text-align:center;"},
            editable: function () {return editFlag == null ? false : editFlag;},
            headerTemplate : '<input type="checkbox" id="' + headerColName + '" name="checkBoxAll" class="rowAllCheckbox k-checkbox"/>',
            template : function(row) {
                let str = "";
                // 일반상품 또는 묶음상품의 포장상품인 경우 체크박스 활성화
                //if (row.odOrderDetlParentId == 0 || row.odOrderDetlId == row.odOrderDetlParentId) {
                //str = '<input type="checkbox" id="' + colId + '_' + row.rowNum + '" name="' + colName + '" data-detailid="'+ row.odOrderDetlId +'" class="k-checkbox" data-validate-title="복사할 상품 선택"/>';
                //}

                if (row.goodsTpCd != 'GOODS_TYPE.PACKAGE') {
                    str = '<div style="text-align: right; padding-right: 5px;"><input type="checkbox" id="'+colId+'" name="'+ colName +'" data-detailid="'+ row.odOrderDetlParentId +'" class="rowCheckbox k-checkbox" data-validate-title="복사할 상품 선택" /></div>';
                } else {
                    //str = '<div style="text-align: left; padding-left: 5px;"><input type="checkbox" id="'+colId+'" data-detailid="'+ row.odOrderDetlId +'" class="packageCheckbox k-checkbox" data-validate-title="복사할 상품 선택" /></div>';
                }
                return str;
            }
        }
    },
    odOrderDetlSeq: function(row, editFlag, islock){
        if(islock == null) { islock = false; }
        return {
            field: "odOrderDetlSeq", title: "상세번호", locked: islock, width: "70px", attributes: {style: "text-align:center;"},
            editable: function () {return editFlag == null ? false : editFlag;},
            template: function (row) {
                let str = '<div style="text-align: center;"><button id="orderPackageMore" type="button" class="btn-s btn-point orderPackageMore" data-view="Y" style="font-size: 12px;padding: 5px 0px;">- 접기</button></div>';
                if (row.goodsTpCd != 'GOODS_TYPE.PACKAGE') {
                    str = row.odOrderDetlSeq;
                }
                return str;
            }
        }
    },
    ilItemCd: function(row, editFlag, islock){
        if(islock == null) { islock = false; }
        return {
            field : "ilItemCd", title : "마스터품목코드<br/>품목바코드", locked: islock, width: "140px", attributes: {style : "text-align:center; text-decoration: underline; cursor: pointer;"},
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
    ilGoodsId: function(row, editFlag, islock){
        if(islock == null) { islock = false; }
        return {
            field : "ilGoodsId", title : "상품코드", locked: islock, width: "70px", attributes : {style : "text-align:center; text-decoration: underline; cursor: pointer;"},
            editable: function () {return editFlag == null ? false : editFlag;},
            template : function(row){
                let str = "<div style='display: none;'>-</div>";
                //if (row.goodsTpCd != 'GOODS_TYPE.PACKAGE') {
                    str = "<div class='popGoodsClick'>" + row.ilGoodsId + "</div>";
                //}
                return str;
            }
        }
    },
    ilGoodsId2: function(row, editFlag) {
        return {
            field : "ilGoodsId", title : "상품코드" , width: "70px", attributes : {style : "text-align:center;"},
            editable: function () {return editFlag == null ? false : editFlag;}
        }
    },
    goodsNm: function(row, editFlag, islock){
        if(islock == null) { islock = false; }
        return {
            field : "goodsNm", title : "상품명", locked: islock, width: "350px", attributes : {style : "text-align: left; padding: 5px 5px; cursor: pointer;"},
            editable: function () {return editFlag == null ? false : editFlag;},
            template : function(row){
                let str = "";
                let addSubTxt   = orderMgmFunctionUtil.goodNmAddTextView(row);
                let depthId     = row.odOrderDetlDepthId;
                let blankStr    = "";

                if (depthId > 1){
                    blankStr = "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;ㄴ ";
                }

                if((row.goodsTpCd == 'GOODS_TYPE.GIFT' || row.goodsTpCd == 'GOODS_TYPE.GIFT_FOOD_MARKETING') && depthId <= 1){
                    str += "<div class='popGoodsNmClick' style='float: left; width: 80%; '>" + blankStr + "<u>(증정품) " + row.goodsNm + '</u></div>';
                    str += '<div style="float: right"><button id="orderGiftListPopupBtn" type="button" class="btn-s btn-point" style="font-size: 12px;padding: 5px 3px;">증정품 내역</button></div>';
                }else{

                    if(row.goodsTpCd == 'GOODS_TYPE.GIFT' || row.goodsTpCd == 'GOODS_TYPE.ADDITIONAL' || row.goodsTpCd == 'GOODS_TYPE.GIFT_FOOD_MARKETING') {
                        str = "<div class='popGoodsNmClick' style=' '>" + blankStr + "" + row.goodsNm + "</div>" + addSubTxt;
                    } else {
                        str = "<div class='popGoodsNmClick' style=' '>" + blankStr + "<u>" + row.goodsNm + "</u></div>" + addSubTxt;
                    }
                    if (row.goodsTpCd == 'GOODS_TYPE.DAILY' && row.orderStatusCode != 'IC'&& row.orderStatusCode !=  'IR') {
                        //하이톡 스위치
                        if (isHitokSwitch && row.goodsDailyTp == 'GOODS_DAILY_TP.GREENJUICE') {
                            str += '<div></div>';
                        } else {
                            str += '<div style="float: right"><button id="orderScheduleChangeBtn" type="button" class="btn-s btn-point" style="font-size: 12px;padding: 5px 3px;">스케쥴</button></div>';
                        }
                    }
                }

                return str;
            }
        }
    },
    goodsNm2: function(row, editFlag) {
        return {
            field : "goodsNm", title : "상품명", width: "350px", attributes : {style : "text-align: left;" },
            editable: function () {return editFlag == null ? false : editFlag;},
            template : function(row){

                let goodsNm = row.goodsNm;
                let modifyBtnEl = "<div style='float: right'><button id='dailyGoodsOptionChangeBtn' type='button' class='btn-s btn-white'>수정</button></div>";
                let additionalModifyBtnEl = "<div style='float: right'><button id='additionalGoodsChangeBtn' type='button' class='btn-s btn-white'>추가</button></div>";

                // 일일상품
                if(row.goodsType == "GOODS_TYPE.DAILY"){
                    goodsNm = goodsNm.replace(/\$\{(.*.)\}/g, "<div style='display: flex; align-items: center; justify-content: space-between;'>$1" + modifyBtnEl + "</div>");

                    // 추가상품
                }else if(row.additionalGoodsExistYn == 'Y'){
                    goodsNm = goodsNm.replace(/\$\{(.*.)\}/g, "<div style='display: flex; align-items: center; justify-content: space-between;'>$1" + additionalModifyBtnEl + "</div>");
                }else {
                    goodsNm = goodsNm.replace(/\$\{(.*.)\}/g, "$1");
                }

                return goodsNm;
            }

        }
    },
    goodsNm3: function(row, editFlag) {
        return {
            field : "goodsNm", title : "상품명", width: "350px", attributes : {style : "text-align: center;"},
            template : function(row){
                let str = row.goodsNm + row.goodsNmCnt;
                return str;
            }
        }
    },
    goodsNmInfo: function(row) {
        return {
            field : "goodsNmInfo", title : "상품명", width: "300px", attributes : {style : "text-align: center;"}
        }
    },
    storageType: function(row, editFlag){
        return {
            field : "storageType", title : "보관<br/>방법", width: "50px", attributes : {style : "text-align:center;"},
            editable: function () {return editFlag == null ? false : editFlag;},
            template : function(row){
                let str = "<div style='display: none;'>-</div>";
                if (row.goodsTpCd != 'GOODS_TYPE.PACKAGE') {
                    str = row.storageType;
                }
                return str;
            }
        }
    },
    orderStatus: function(row){
        return {
            field : "orderStatus", title : "주문상태", width: "80px", attributes : {style : "text-align:center;"},
            template : function(row){
                let str = "<div style='display: none;'>-</div>";
                if (row.goodsTpCd != 'GOODS_TYPE.PACKAGE') {
                    str = row.orderStatus;
                }
                //렌탈주문인경우 주문상태변경 (bosJson 참조)
                if(row.orderStatusDeliTpCd == 'ORDER_STATUS_DELI_TP.RENTAL' && fnNvl(row.bosJson) != "") {
                    let rentalStatus = fnNvl(JSON.parse(row.bosJson)[1].rows[0].rentalTypeStatusNm);
                    str = rentalStatus != "" ? rentalStatus : row.orderStatus;
                }

                return str;
            },
            editable : function(row) {return false;}
        }
    },
    orderStatusClaimGoodsList: function(row){
        return {
            field : "orderStatus", title : "주문상태", width: "80px", attributes : {style : "text-align:center;"},
            template : function(row){
                let str = "<div style='display: none;'>-</div>";
                if (row.goodsTpCd != 'GOODS_TYPE.PACKAGE') {
                    str = row.orderStatus;
                }
                //렌탈주문인경우 주문상태변경 (bosJson 참조)
                if( fnNvl(row.orderBosJson) != "") {
                    let rentalStatus = fnNvl(JSON.parse(row.orderBosJson)[1].rows[0].rentalTypeStatusNm);
                    str = rentalStatus != "" ? rentalStatus : row.orderStatus;
                }

                return str;
            },
            editable : function(row) {return false;}
        }
    },
    orderCnt: function(row){
        return {
            field : "orderCnt", title : "주문수량", width: "70px", attributes : {style : "text-align:right;padding-right: 5px;"}, format: "{0:n0}"
        }
    },
    // 주문복사 화면용 주문수량
    orderCopyOrderCnt : function(row, editFlag) {
        return {
            field : "orderCnt", title : "주문수량", width: "70px", attributes : {style : "text-align:right;padding-right: 5px;"}, format : "{0:n0}",
            editable: function (row) {
                if (editFlag == null || !editFlag) return false;
                //if (row.odOrderDetlParentId != 0 && row.odOrderDetlId != row.odOrderDetlParentId) return false;
                return editFlag;
            },
            editor : function(container, option) {
                // 일반상품 또는 묶음상품의 포장상품인 경우만 수정 가능
                //if (option.model.odOrderDetlParentId != 0 && option.model.odOrderDetlId != option.model.odOrderDetlParentId) return false;

                let orderCntArr = new Array();
                for (var i=1; i<=option.model.orgOrderCnt; i++) {
                    let orderCntMap = new Object();
                    orderCntMap.CODE = i;
                    orderCntMap.NAME = i;
                    orderCntArr.push(orderCntMap);
                }

                $('<input id="orderCnt_'+ option.model.rowNum +'" name="orderCnt">').appendTo(container)
                    .kendoDropDownList({
                        dataTextField: "NAME",
                        dataValueField: "CODE",
                        dataSource: {data: orderCntArr},
                        autoWidth: true
                    });
            }
        }
    },
    cancelCnt: function(row){
        return {
            field : "cancelCnt", title : "클레임수량", width: "70px", attributes : {style : "text-align:right;padding-right: 5px; color:red;"}, format: "{0:n0}",
            template : function(row){
                let str = "<div style='display: none;'>-</div>";
                if (row.goodsTpCd != 'GOODS_TYPE.PACKAGE') {
                    str = row.cancelCnt;
                }
                return str;
            }
        }
    },
    claimAbleCnt: function(row){
        return {
            field : "claimAbleCnt", title : "클레임가능<br/>수량", width: "70px", attributes : {style : "text-align:right;padding-right: 5px;"}, format: "{0:n0}",
            template : function(row){
                let str = "<div style='display: none;'>-</div>";
                if (row.goodsTpCd != 'GOODS_TYPE.PACKAGE') {
                    str = row.claimAbleCnt;
                }
                return str;
            }
        }
    },
    missCnt: function(row){
        return {
            field : "missCnt", title : "미출수량", width: "70px", attributes : {style : "text-align:right;padding-right: 5px;"}, format: "{0:n0}",
            template : function(row){
                let str = "<div style='display: none;'>-</div>";
                if (row.goodsTpCd != 'GOODS_TYPE.PACKAGE') {
                    str = row.missCnt;
                }
                return str;
            }
        }
    },
    sendEndDt: function(row, editFlag){
        return { field : "sendEndDt", title : "송장등록일시", width: "110px", attributes : {style : "text-align:center;"},  format: "{0:yyyy-MM-dd}",
            editable: function () {return editFlag == null ? false : editFlag;},
            template : function(row) {
                let str = "";
                if (row.goodsTpCd != 'GOODS_TYPE.PACKAGE') {
                    str = stringUtil.getString(row.sendEndDt, "");
                }
                if(str == "" ) { str = "<div style='display: none;'>-</div>"; }
                return str;
            }
        }
    },
    orgClaimAbleCnt: function(row){
        return {
            field : "orgClaimAbleCnt", title : "클레임가능<br/>수량", width: "70px", attributes : {style : "text-align:right;padding-right: 5px;"}, format: "{0:n0}",
            template : function(row){
                let str = "<div style='display: none;'>-</div>";
                if (row.goodsTpCd != 'GOODS_TYPE.PACKAGE') {
                    str = row.orgClaimAbleCnt;
                }
                return str;
            }
        }
    },
    orderIfDt: function(row, editFlag) {
        return { field : "orderIfDt", title : "주문I/F<br/>(출고예정일)", width: "110px", attributes : {style : "text-align:center;"},
            editable: function () {return editFlag == null ? false : editFlag;},
            template : function(row){
                var actionJson = JSON.parse(row.actionJson);
                let str = "<div style='display: none;'>-</div>";
                if (row.goodsTpCd != 'GOODS_TYPE.PACKAGE') {
                    var ifDayChangeYn = "N";

                    if (actionJson != null){
                        ifDayChangeYn = stringUtil.getString(actionJson.ifDayChangeYn, "N");
                    }
                    // 주문상태가 결제완료인 경우에만 출고예정일 변경 가능 (주문수량 = 취소수량일 경우 변경 X)
                    if (ifDayChangeYn == 'Y' && row.orderStatusCode == 'IC' && (row.orderCnt != row.cancelCnt) && row.orderCnt > 0) {
                        str += "<div type='hidden' name='ifDayChange' value='" + row.odOrderDetlId + "' style='text-align: center;'>";
                        str += "<div class='ifDayChangeDiv' style=' background: #d9d9d9; cursor: pointer; text-align: center; width:80%; display: inline-block;text-align: center;'>" + stringUtil.getString(row.orderIfDt, "") + "</div>(" + stringUtil.getString(row.shippingDt, "")+")";
                        str += "</div>";
                    } else {
                        if(fnNvl(row.promotionTp) != "CART_PROMOTION_TP.GREENJUICE_SELECT") {
                            str += stringUtil.getString(row.orderIfDt, "") + "<br/>(" + stringUtil.getString(row.shippingDt, "")+")";
                        } else { //내맘대로 주문
                            str += "<div class='greenSelectDiv'>"+stringUtil.getString(row.orderIfDt, "") + "<br/>(" + stringUtil.getString(row.shippingDt, "")+")"+"</div>";
                        }
                    }
                }
                return str;
            }
        }
    },
    orderIfDt2: function(row, editFlag) {
        return {
            field : "deliveryDt", title : "주문I/F", width: "90px", attributes : {style : "text-align: center;"},
            template : function(row) {
                let d     = row.deliveryDt.replace(/-/g,'').replace(/\//g,'');
                let year  = d.substring(0,4);
                let month = d.substring(4,6);
                let day   = d.substring(6,8);
                var dt = new Date(year, month-1, day);
                return dt.oFormat('yyyy.MM.dd (E)').replace('요일', '');
            },
            editable: function() {return editFlag == null ? false : editFlag;},
            editor : function(container, option) {
                if (!fnIsEmpty(option.model.choiceArrivalScheduledDateList) && option.model.choiceArrivalScheduledDateList.length > 0) {
                    let arrivalScheduledArr = new Array();
                    for (let i=0; i<option.model.choiceArrivalScheduledDateList.length; i++) {
                        let arrivalScheduledMap = new Object();
                        arrivalScheduledMap.CODE = option.model.choiceArrivalScheduledDateList[i];
                        arrivalScheduledMap.NAME = option.model.choiceArrivalScheduledDateList[i];
                        arrivalScheduledArr.push(arrivalScheduledMap);
                    }
                    $('<input id="deliveryDt_'+ option.model.no +'" name="deliveryDt">').appendTo(container)
                        .kendoDropDownList({
                            dataTextField	: "NAME",
                            dataValueField	: "CODE",
                            dataSource		: {data: arrivalScheduledArr},
                            autoWidth		: true
                        });
                }
            }
        }
    },
    // 주문생성 화면용 주문 I/F
    orderCreateOrderIfDt : function(row, editFlag) {
        return { field : "orderIfDt", title : "주문I/F<br/>(출고예정일)", width: "110px", attributes : {style : "text-align:center;"},
            editable: function () {return editFlag == null ? false : editFlag;},
            template : function(row) {

                let str = "";

                // 주문상태가 결제완료인 경우에만 출고예정일 변경 가능 (주문수량 = 취소수량일 경우 변경 X)

                str = "<div type='hidden' name='ifDayChange' value='" + row.odOrderDetlId + "' style='text-align: center;'>";
                str += "<div style=' background: #d9d9d9; cursor: pointer; text-align: center; width:80%; display: inline-block;text-align: center;text-decoration: underline;font-weight: bold;'>" + stringUtil.getString(row.orderIfDt, "") + "</div>(" + stringUtil.getString(row.shippingDt, "")+")";
                str += "</div>";


                return str;
            }
        }
    },
    // 주문복사 화면용 주문 I/F
    orderCopyOrderIfDt : function(row, editFlag) {
        return { field : "orderIfDt", title : "주문I/F<br/>(출고예정일)", width: "110px", attributes : {style : "text-align:center;"},
            editable: function () {return editFlag == null ? false : editFlag;},
            template : function(row) {
                var actionJson = JSON.parse(row.actionJson);
                let str = "<div style='display: none;'>-</div>";

                if (row.goodsTpCd != 'GOODS_TYPE.PACKAGE') {

                    // 주문상태가 결제완료인 경우에만 출고예정일 변경 가능 (주문수량 = 취소수량일 경우 변경 X)

                        str += "<div type='hidden' name='ifDayChange' value='" + row.odOrderDetlId + "' style='text-align: center;'>";
                        str += "<div style=' background: #d9d9d9; cursor: pointer; text-align: center; width:80%; display: inline-block;text-align: center;'>" + stringUtil.getString(row.orderIfDt, "") + "</div>(" + stringUtil.getString(row.shippingDt, "")+")";
                        str += "</div>";

                }
                return str;
                /*var actionJson = JSON.parse(row.actionJson);
                let str = "<div style='display: none;'>-</div>";
                if (row.goodsTpCd != 'GOODS_TYPE.PACKAGE') {
                    str += stringUtil.getString(row.orderIfDt, "") + "<br/>(" + stringUtil.getString(row.shippingDt, "")+")";
                }
                return str;
                 */
            }
        }
    },
    deliveryDt: function(row, editFlag){
        return { field : "deliveryDt", title : "도착예정일", width: "110px", attributes : {style : "text-align:center;"},  format: "{0:yyyy-MM-dd}",
            editable: function () {return editFlag == null ? false : editFlag;},
            template : function(row) {
                let str = "<div style='display: none;'>-</div>";
                if (row.goodsTpCd != 'GOODS_TYPE.PACKAGE') {
                    str = stringUtil.getString(row.deliveryDt, "");
                }
                return str;
            }
        }
    },
    goodsTpCd: function(row, editFlag){
        return { field : "goodsTpCd", title : "상품유형<br/>(보관방법)", width: "110px", attributes : {style : "text-align:center;"},
            editable: function () {return editFlag == null ? false : editFlag;},
            template : function(row) {
                let str = "<div style='display: none;'>-</div>";
                if (row.goodsTpCd != 'GOODS_TYPE.PACKAGE') {
                    str = row.goodsTpCdNm + "<br/>(" + row.storageType+")";
                }
                return str;
            }
        }
    },
    warehouseNm : function(row) {
        return {field : "warehouseNm", title : "출고처", width: "100px", attributes : {style :"text-align:center;"}, rowspan: true}
    },
    warehouseNmOrderGoodsList : function(row) {
        return {field : "warehouseNm", title : "출고처", width: "100px", attributes : {style :"text-align:center;"},
            template : function(row) {
                let str =  row.warehouseNm;
                if(fnNvl(row.urStoreNm) != '') { // 매장명
                    str += " / </br>" + row.urStoreNm;
                }
                if(fnNvl(row.orderStatusDeliTp) != '') {
                    str += '</br>' + row.orderStatusDeliTp;
                }
                return str;
            }
        }
    },
    trackingNo: function(row){
        return {
            field: "trackingNo", title: "송장번호<br/>(택배사)", width: "110px", attributes: {style: "text-align:center;"},
            template: function (row) {

                let shippingCompNm = stringUtil.getString(row.shippingCompNm, "");
                let trackingNo = stringUtil.getString(row.trackingNo, "");
                if (shippingCompNm != "") {
                    shippingCompNm = "(" + shippingCompNm + ")";
                }

                //직배송인 경우 링크연결 X
                if(trackingNo != "") {
                    if( row.psShippingCompId != 1 ){
                        trackingNo =  "<div name='trackingNoSearch' style='text-align:center; text-decoration: underline; font-weight : bold; cursor: pointer;'>" + row.trackingNo + "</div>";
                    } else {
                        trackingNo =  "<div style='text-align:center; font-weight : bold; cursor: pointer;'>" + row.trackingNo + "</div>";
                    }
                } else {
                    trackingNo = "";
                }

                return trackingNo + shippingCompNm;
            }
        }
    },
    trackingNoOrderClaimList: function(row){
        return {
            field: "trackingNo", title: "택배사 /<br/>송장번호", width: "110px", attributes: {style: "text-align:center;"},
            template: function (row) {

                let shippingCompNm = stringUtil.getString(row.shippingCompNm, "");
                let trackingNo = stringUtil.getString(row.trackingNo, "");
                if (shippingCompNm != "") {
                    shippingCompNm = shippingCompNm + " / </br>";
                }

                //직배송인 경우 링크연결 X
                if(trackingNo != "") {
                    if( row.psShippingCompId != 1 ){
                        trackingNo =  "<div name='trackingNoSearchForOrderClaim' id='trackingNoSearchForOrderClaim' style='text-align:center; text-decoration: underline; font-weight : bold; cursor: pointer;'>" + shippingCompNm + row.trackingNo + "</div>";
                    } else {
                        trackingNo =  "<div style='text-align:center; font-weight : bold;'>" + shippingCompNm + row.trackingNo + "</div>";
                    }
                } else {
                    trackingNo = shippingCompNm;
                }

                return trackingNo;
            }
        }
    },
    trackingNoShippingInfo: function(row){
        return {
            field: "trackingNo", title: "송장번호<br/>(택배사)", width: "110px", attributes: {style: "text-align:center;"},
            template: function (row) {

                let shippingInfoStr = stringUtil.getString(row.shippingInfo, "");
                var shippingInfoArr = []; //배송정보
                var shippingInfoArrList = [];
                let str = '';
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
                                str = '<div style="text-align:center; font-weight : bold; ">' + shippingInfoArr[i][1] + "</div>" + '(' + shippingInfoArr[i][0] + ')';
                            } else {
                                str = '<div name="trackingNoSearch" id= "trackingNoSearch_'+i+'" style="text-align:center; text-decoration: underline; font-weight : bold; cursor: pointer;" data-shipping-comp-Nm="' + shippingInfoArr[i][0] + '" data-shipping-comp-id="' + shippingInfoArr[i][2] + '" data-tracking-url="' + shippingInfoArr[i][3] + '" data-http-request-tp ="' + shippingInfoArr[i][4] + '" data-invoice-param ="' + shippingInfoArr[i][5] + '" data-tracking-Yn ="' + shippingInfoArr[i][6] + '" data-logisticsCd ="' + shippingInfoArr[i][7] + '">'
                                str += shippingInfoArr[i][1] + "</div>" + '(' + shippingInfoArr[i][0] + ')';
                            }
                            shippingInfoArrList.push(str + '</br>');
                        }
                    }
                    str = stringUtil.getString(shippingInfoArrList.join(""));
                } else {
                    if(shippingInfoArr[0][1] != '-') {
                        if(shippingInfoArr[0][0] == '직배송') { //직배송인 경우 링크연결 X
                            str = '<div style="text-align:center; font-weight : bold;">' + shippingInfoArr[0][1] + "</div>" + '(' + shippingInfoArr[0][0] + ')';
                        } else {
                            str = '<div name="trackingNoSearch" id= "trackingNoSearch" style="text-align:center; text-decoration: underline; font-weight : bold; cursor: pointer;" data-shipping-comp-Nm="' + shippingInfoArr[0][0] + '"  data-shipping-comp-id="' + shippingInfoArr[0][2] + '" data-tracking-url="' + shippingInfoArr[0][3] + '" data-http-request-tp ="' + shippingInfoArr[0][4] + '" data-invoice-param ="' + shippingInfoArr[0][5] + '"  data-tracking-Yn ="' + shippingInfoArr[0][6] + '" data-logisticsCd ="' + shippingInfoArr[0][7] + '">'
                            str += shippingInfoArr[0][1] + "</div>" + '(' + shippingInfoArr[0][0] + ')';
                        }
                    }
                }

                // 일일배송일 경우 가맹점으로 표시
                //if(stringUtil.getString(row.deliveryAreaNm, "") != "" && row.orderStatusCode !=  'IC' && row.orderStatusCode !=  'DR' && row.orderStatusCode !=  'IR') {
                //    str = '<div style="text-align:center; font-weight : bold;">' + row.deliveryAreaNm + "</div>" + '(' + row.deliveryAreaTel + ')';
                //}

                return str;
            }
        }
    },
    odShippingZoneId: function(row, rowspanFlag, editFlag){
        return {
            field: "odShippingZoneId", title: "배송번호", width: "80px", attributes: {style: "text-align: center;text-decoration: underline; font-weight : bold;"}, rowspan: rowspanFlag,
            editable: function () {return editFlag == null ? false : editFlag;},
            template: function (row) {
                let str = "<div style='display: none;'>-</div>";
                if (row.goodsTpCd != 'GOODS_TYPE.PACKAGE') {
                    str = row.odShippingZoneId;
                }
                return str;
            }
        }
    },
    simpleOdShippingZoneId: function(row) {
        return { field : "odShippingZoneId", title : "배송번호"	, width: "50px", attributes : {style : "text-align: center"}}
    },
    shippingPrice: function(row, editFlag) {
        return { field : "shippingPrice", title : "배송비", width: "80px", attributes : {style : "text-align: right; padding-right: 5px;"}, format: "{0:n0}",
            editable: function () {return editFlag == null ? false : editFlag;},
            template: function (row) {
                let str = "<div style='display: none;'>-</div>";
                // if (row.goodsTpCd != 'GOODS_TYPE.PACKAGE') {
                //     str = row.shippingPrice;
                // }
                str = fnNumberWithCommas(row.shippingPrice);
                return str;
            }
        }
    },
    shippingPriceOrderGoodsList: function(row, editFlag) {
        return { field : "shippingPrice", title : "배송비", width: "80px", attributes : {style : "text-align: center;"}, rowspan: true,
            editable: function () {return editFlag == null ? false : editFlag;},
            template: function (row) {
                let str = fnNumberWithCommas(row.shippingPrice)+'<span style="display: none;">_'+row.urWarehouseId+'_'+row.ilShippingTmplId+'</span>';
                return str;
            }
        }
    },
    shippingPriceClaimGoodsList: function(row, editFlag) {
        return { field : "shippingPrice", title : "배송비", width: "80px", attributes : {style : "text-align: center; padding-right: 5px;"}, format: "{0:n0}",
            editable: function () {return editFlag == null ? false : editFlag;},
            template: function (row) {
                let str = fnNumberWithCommas(row.shippingPrice);
                if(row.targetTp == 'B') {
                    str = fnNumberWithCommas(row.shippingPrice)+"</br>(구매자부담)";
                }  else if(row.targetTp == 'S') {
                    str = fnNumberWithCommas(row.shippingPrice)+"</br>(판매자부담)";
                }
                return str;
            }
        }
    },
    shippingPriceShippingInfo: function(row, editFlag) {
        return { field : "shippingPrice", title : "배송비", width: "80px", attributes : {style : "text-align:center;"}, format: "{0:n0}",
            editable: function () {return editFlag == null ? false : editFlag;},
            template: function (row) {
                let str = "";
                if(row.shippingDiscountPrice != 0) {
                    str = fnNumberWithCommas(row.shippingPrice) + "</br> (할인 : " + fnNumberWithCommas(row.shippingDiscountPrice) + "원)";
                } else {
                    str = fnNumberWithCommas(row.shippingPrice);
                }

                return str;
            }
        }
    },
    shippingPrice4: function(row, editFlag) {
        return { field : "addPaymentShippingPrice", title : "배송비", width: "80px", attributes : {style : "text-align: center;"}, format: "{0:n0}",
            editable: function () {return editFlag == null ? false : editFlag;},
            template: function (row) {
                let str = fnNumberWithCommas(row.addPaymentShippingPrice);
                if(row.targetTpNm == '구매자') {
                    str = fnNumberWithCommas(row.addPaymentShippingPrice)+"</br>(구매자부담)";
                }  else if(row.targetTpNm == '판매자') {
                    str = fnNumberWithCommas(row.addPaymentShippingPrice)+"</br>(판매자부담)";
                }
                return str;
            }
        }
    },
    shippingNm: function(row, editFlag) {
        return { field : "shippingNm", title : "배송정책명", width: "350px", attributes : {style : "text-align: center;"},
            editable: function () {return editFlag == null ? false : editFlag;},
            template: function (row) {
                let str = "<div style='display: none;'>-</div>";
                if (row.goodsTpCd != 'GOODS_TYPE.PACKAGE') {
                    str = stringUtil.getString(row.shippingNm, "");
                }
                return str;
            }
        }
    },
    deliveryType: function(row) {
        return { field : "deliveryType", title : "배송방법"	, width: "50px", attributes : {style : "text-align: center"}}
    },
    deliveryTypeShippingInfo: function(row) {
        return { field : "deliveryType", title : "배송방법</br>(출고처)"	, width: "80px", attributes : {style : "text-align: center"},
            template: function (row) {
                let str = "";
                if(row.orderStatusDeliTp != "" && row.orderStatusDeliTp != null) {
                    str = row.orderStatusDeliTp + "</br>(" + row.warehouseNm ;

                     if(row.deliveryTypeCode == 'DELIVERY_TYPE.DAILY'
                         || row.deliveryTypeCode == 'DELIVERY_TYPE.SHOP_DELIVERY'
                         || row.deliveryTypeCode == 'DELIVERY_TYPE.SHOP_PICKUP' && fnNvl(row.urStoreNm) != '') {
                         str += "/ </br>" + row.urStoreNm + ")";
                     } else {
                         str += ")";
                     }

                }
                return str;
            }
        }
    },
    newDeliveryType2: function(row) {
        return { field : "warehouseNm", title : "회수방법</br>(출고처)"	, width: "80px", attributes : {style : "text-align: center"},
            template: function (row) {
                let str = "";
                if(row.warehouseNm != "") {
                    str = '회수' + "</br>(" + row.warehouseNm + ")";
                }
                return str;
            }
        }
    },
    recvNm: function(row, titleNm) {
        return { field : "recvNm"	, title : fnIsEmpty(titleNm) ? "받는분" : titleNm, width: "80px", attributes : {style : "text-align: center"}}
    },
    orgRecvNm: function(row, titleNm) {
        return { field : "orgRecvNm"	, title : fnIsEmpty(titleNm) ? "받는분" : titleNm, width: "80px", attributes : {style : "text-align: center"}}
    },
    recvHp: function(row, titleNm) {
        return { field : "recvHp"	, title : fnIsEmpty(titleNm) ? "휴대폰" : titleNm, width: "120px", attributes : {style : "text-align: center"},
            template: function (row) {
                        let str = "";
                        let recvHp = row.recvHp.replace(/\-/g,'') == "" ? "01000000000" : row.recvHp.replace(/\-/g,''); // 휴대폰번호 하이픈(-) 제거;
                        if(recvHp.length < 12) {
							str = fnPhoneNumberHyphen(recvHp);
						} else if(12 <= recvHp.length){
							str = recvHp.slice(0,4) + '-' + recvHp.slice(4,(recvHp.length)-4) + '-' + recvHp.slice((recvHp.length)-4,recvHp.length);
						}
                        return str;
            }

        }
    },
    orgRecvHp: function(row, titleNm) {
        return { field : "orgRecvHp"	, title : fnIsEmpty(titleNm) ? "휴대폰" : titleNm, width: "120px", attributes : {style : "text-align: center"},
            template: function (row) {return fnPhoneNumberHyphen(row.orgRecvHp);}
        }
    },
    recvZipCd: function(row) {
        return { field : "recvZipCd", title : "우편번호"	, width: "70px", attributes : {style : "text-align: center"}}
    },
    recvAddr1: function(row) {
        return { field : "recvAddr1", title : "주소1"		, width: "230px", attributes : {style : "text-align: center"}}
    },
    orgRecvAddr1: function(row) {
        return { field : "orgRecvAddr1", title : "주소1"		, width: "230px", attributes : {style : "text-align: center"}}
    },
    shipRecvAddr1: function(row) {
        return { field : "recvAddr1", title : "배송지 주소"		, width: "230px", attributes : {style : "text-align: center"}}
    },
    returnRecvAddr1: function(row) {
        return { field : "recvAddr1", title : "회수지 주소"		, width: "230px", attributes : {style : "text-align: center"}}
    },
    recvAddr2: function(row) {
        return { field : "recvAddr2", title : "주소2"		, width: "210px", attributes : {style : "text-align: center"}}
    },
    orgRecvAddr2: function(row) {
        return { field : "orgRecvAddr2", title : "주소2"		, width: "210px", attributes : {style : "text-align: center"}}
    },
    shipRecvAddr2: function(row) {
        return { field : "recvAddr2", title : "상세주소"		, width: "210px", attributes : {style : "text-align: center"}}
    },
    returnRecvAddr2: function(row) {
        return { field : "recvAddr2", title : "상세주소"		, width: "210px", attributes : {style : "text-align: center"}}
    },
    deliveryMsg: function(row) {
        return { field : "deliveryMsg", title : "배송요청사항"	, width: "100px", attributes : {style : "text-align: center"},
            template : function(row){
                let str = "-";
                if (row.deliveryMsg) {
                    str = row.deliveryMsg;
                }
                return str;
            }
        }
    },
    doorMsgCdName: function(row) {
        return { field : "doorMsgCdName"			, title : "배송출입정보"			, width: "100px"		, attributes : {style : "text-align:center;"}
            , template : function(dataItem){
                let returnVal;
                if(!dataItem.doorMsgCd){
                    returnVal = "-";
                }else{
                    if(fnNvl(dataItem.doorMsg) != ''){ // 공동현관 비밀번호 입력값이 있다면 같이 출력
                        returnVal = dataItem.doorMsgCdName + "<br/>" + dataItem.doorMsg;
                    } else{ // 그외
                        returnVal = dataItem.doorMsgCdName;
                    }
                }
                return returnVal;
            }
        }
    },
    applyDt: function(row) {
        return { field : "deliveryDt", title : "적용일"	, width: "60px", attributes : {style : "text-align: center"},
            template : function(row) {return row.deliveryType == "일일배송" ? row.deliveryDt : "-";}
        }
    },
    paidPrice: function(row, editFlag, titleNm) {
        return { field : "paidPrice", title : fnIsEmpty(titleNm) ? "총상품금액" : titleNm, width: "100px", attributes : {style : "text-align: right;padding-right: 5px;"} , format: "{0:n0}",
            editable: function () {return editFlag == null ? false : editFlag;}
        }
    },
    totalGoodsPrice : function(row, editFlag, titleNm) {
        return { field : "totalGoodsPrice", title : fnIsEmpty(titleNm) ? "총상품금액" : titleNm, width: "100px", attributes : {style : "text-align: right;padding-right: 5px;"} , format: "{0:n0}",
            editable: function () {return editFlag == null ? false : editFlag;}
        }
    },
    salePrice: function(row, editFlag) {
        return { field : "salePrice", title : "판매가", width: "100px", attributes : {style : "text-align: right;padding-right: 5px;"} , format: "{0:n0}",
            editable: function () {return editFlag == null ? false : editFlag;}
        }
    },
    couponPrice: function(row, editFlag) {
        return { field : "couponPrice", title : "쿠폰할인", width: "100px", attributes : {style : "text-align: right;padding-right: 5px;"} , format: "{0:n0}",
            editable: function () {return editFlag == null ? false : editFlag;}
        }
    },
     directPrice: function(row, editFlag) {
        return { field : "directPrice", title : "즉시할인", width: "100px", attributes : {style : "text-align: right;padding-right: 5px;"} , format: "{0:n0}",
            editable: function () {return editFlag == null ? false : editFlag;}
        }
    },
     discountEmployeePrice: function(row, editFlag) {
        return { field : "discountEmployeePrice", title : "임직원할인", width: "100px", attributes : {style : "text-align: right;padding-right: 5px;"} , format: "{0:n0}",
            editable: function () {return editFlag == null ? false : editFlag;}
        }
    },
    discountPrice: function(row, editFlag) {
        return { field : "discountPrice", title : "상품별<br/>할인금액", width: "100px", attributes : {style : "text-align: right;padding-right: 5px;"} , format: "{0:n0}",
            editable: function () {return editFlag == null ? false : editFlag;}
        }
    },
    discountPriceClaimGoodsList : function(row, editFlag) {
        return { field : "discountPrice", title : "쿠폰할인<br/>차감", width: "100px", attributes : {style : "text-align: right;padding-right: 5px;"} , format: "{0:n0}",
            editable: function () {return editFlag == null ? false : editFlag;}
        }
    },
    collectionMailDetlId: function(row, editFlag) {
        return { field : "collectionMailDetlId", title : "수집몰<br/>주문상세번호", width: "150px" , attributes : {style : "text-align: center;"},
            editable: function () {return editFlag == null ? false : editFlag;},
            template: function (row) {
                let str = "<div style='display: none;'>-</div>";
                if (fnNvl(row.collectionMailDetlId) != '') {
                    str = row.collectionMailDetlId;
                } else {
                    str = '-';
                }
                return str;
            }
        }
    },
    orderMgmCollectionMailDetlId: function(row, editFlag) {
        return { field : "collectionMailDetlId", title : "외부몰 주문번호", width: "150px" , attributes : {style : "text-align: center;"},
            editable: function () {return editFlag == null ? false : editFlag;},
            template: function (row) {
                let str = "<div style='display: none;'>-</div>";
                if (fnNvl(row.outmallDetlId) != '') {
                    str = row.outmallDetlId;
                } else {
                    str = '-';
                }
                return str;
            }
        }
    },
    outmallDetlId: function(row, editFlag){
        return { field : "collectionMailDetlId"    	, title : "외부몰 주문번호"      , width: "150px"        , attributes : {style : "text-align: center;"},
            editable: function () {return editFlag == null ? false : editFlag;},
            template: function (row) {
                let str = "<div style='display: none;'>-</div>";
                if (fnNvl(row.outMallId) != '') {
                    str = row.outMallId;
                }else {
                    str = '-';
                }
                return str;
            }
        }
    },
    orderMgmOutmallDetlId: function(row, editFlag){
        return { lockable: false, field : "outmallDetlId"    	, title : "수집몰 주문상세번호"      , width: "150px"        , attributes : {style : "text-align: center;"},
            editable: function () {return editFlag == null ? false : editFlag;},
            template: function (row) {
                let str = "<div style='display: none;'>-</div>";
                if (fnNvl(row.collectionMailDetlId) != '') {
                    str = row.collectionMailDetlId;
                }else {
                    str = '-';
                }
                return str;
            }
        }
    },
    outmallStatusNm: function(row, editFlag) {
        return { field : "outmallStatusNm"    	, title : "외부몰<br/>주문상태"      , width: "100px"        , attributes : {style : "text-align: center;"},
            editable: function () {return editFlag == null ? false : editFlag;},
            template: function (row) {
                let str = "<div style='display: none;'>-</div>";
                return str;
            }
        }
    },
    //클레임정보 column
    odClaimId: function(row, rowspanFlag){
        return { field : "odClaimId", title : "클레임<br/>번호", width: "80px", rowspan : rowspanFlag
            , template : function(row) {
                let str = "<div style='text-align:center; font-weight : bold;'>" +row.odClaimId+ "</div>";
                    if(row.claimStatusCode == 'CC' || row.claimStatusCode == 'RC') { // 클레임상태가 취소완료 or 반품완료일 때 BOS클레임사유변경 가능
                        str =  "<div class='odClaimIdClick'  style='text-align:center; font-weight : bold; cursor: pointer; text-decoration: underline;'>" +row.odClaimId+ "</div>";
                    }
                return str;
            }
        }
    },
    claimRequester: function (row) {
        return { field : "claimRequester"       , title : "클레임 요청자"             , width: "130px"        , attributes : {style : "text-align:center;"}
            ,template : function(row){
                let str = "";
                if(row.userTp == 'USER_TYPE.BUYER') {
                    str = "주문자";
                } else if(row.userTp == 'USER_TYPE.EMPLOYEE'){
                    str = row.userNm + " / " + row.loginId;
                }
                return str;
            }
        }
    },
    reasonMsg: function(row) {
        return { field : "reasonMsg", title : "쇼핑몰<br/>클레임사유", width: "180px", attributes : {style : "text-align:center;"}}
    },
    claimGoodsListReasonMsg: function(row) {
        return { field : "reasonMsg", title : "쇼핑몰<br/>클레임사유", width: "180px", attributes : {style : "text-align:center;"},
            template : function(row) {
                let str = row.reasonMsg;
                let json = JSON.parse(row.bosJson);
                if(json[1].rows != null && json[1].rows[0].actionRows != null) {
                    str = row.reasonMsg+'<br><button kind="claimDetlBtn" type="button" class="btn-s btn-point">클레임상세</button>';
                }
                return str;
            }
        }
    },
    bosClaimReasons : function(row) {
        return { field : "bosClaimLargeNm"       , title : "BOS<br/>클레임사유"   , width: "180px"  , attributes : {style : "text-align:center;"}
            ,template : function(row){
                let str="";
                if(row.bosClaimLargeNm != "" && row.bosClaimMiddleNm != "" && row.bosClaimSmallNm != "") {
                    str = row.bosClaimLargeNm+" ></br>"+row.bosClaimMiddleNm+" ></br>"+row.bosClaimSmallNm;
                }
                return str;
            }
        }
    },
    claimReasonMsg: function (row) {
        return { field : "claimReasonMsg"       , title : "클레임 상세 사유"             , width: "300px"        , attributes : {style : "text-align:center;"}
            ,template : function(row){
                let str = "";
                if(row.claimReasonMsg == null) {
                    str =  "";
                } else {
                    if (row.odClaimAttcIdExist == 'N') {
                        str = row.claimReasonMsg;
                    } else {
                        str = row.claimReasonMsg+'<br><button id="orderClaimAttcBtn" type="button" class="btn-s btn-point">첨부파일 보기</button>';
                    }
                }
                return str;
            }
        }
    },
    claimDt: function (row) {
        return { field : "claimDt"              , title : "접수일 /<br/>처리일"               , width: "150px"        , attributes : {style : "text-align:center; font-weight : bold;"},
            template : function(row){
                let str = '';
                if(row.claimApprovalDt != null) {
                    str = row.claimDt+'<br>'+row.claimApprovalDt;
                } else {
                    str = row.claimDt;
                }
                return str;
            }
        }
    },
    claimApprovalDt: function (row) {
        return { field : "claimApprovalDt"      , title : "클레임 승인일"               , width: "90px"        , attributes : {style : "text-align:center; font-weight : bold;"}}
    },
    claimStatus: function(row){ //클레임정보
        return {
            field : "claimStatus", title : "클레임상태", width: "70px", attributes : {style : "text-align:center;"},
            template : function(row){
                let str = "<div style='display: none;'>-</div>";
                if (row.goodsTpCd != 'GOODS_TYPE.PACKAGE') {
                    str = row.claimStatus;
                }
                //렌탈주문인경우 주문상태변경 (bosJson 참조)
                if(fnNvl(row.claimBosJson) != "") {
                    let rentalStatus = fnNvl(JSON.parse(row.claimBosJson)[1].rows[0].rentalTypeStatusNm);
                    str = rentalStatus != "" ? rentalStatus : row.claimStatus;
                }
                return str;
            }
        }
    },
    claimDetlStatusNm: function(row){ //상품/결제정보
        return {
            field : "claimDetlStatusNm", title : "클레임상태", width: "70px", attributes : {style : "text-align:center;"},
            template : function(row){
                let str = "<div style='display: none;'>-</div>";
                if (row.goodsTpCd != 'GOODS_TYPE.PACKAGE' && row.claimDetlStatusNm != '') {
                    str = row.claimDetlStatusNm;
                } else {
                    str = "-";
                }
                return str;
            }
        }
    },
    //클레임상세팝업(재배송으로 변경 시)
    claimGoodsNm : function(row){
        return {
            field : "goodsNm", title : "상품명", width: "220px", attributes : {style : "text-align:center; text-decoration: underline; font-weight : bold;"},
            template : function(row){
                let str = "";
                str = "<div class='popGoodsClick'>" + row.goodsNm;
                //대체상품으로 변경 시
                if(row.reShipping != true && row.claimCnt > 0) {
                    str += '<button type="button" class="fb__search-btn marginL10 fnAddCoverageGoods" style="display : none;" onclick="$scope.fnAddCoverageGoods('+row.odOrderDetlId+', this);"></button></div>';
                }
                return str;
            },
            editable : function(row) {return false;}
        }
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
    recommendedPrice : function(row, editFlag) {
        return { field : "recommendedPrice", title : "정상가", width: "60px", attributes : {style : "text-align: right;"}, format: "{0:n0}",
            editable: function () {return editFlag == null ? false : editFlag;}
        }
    },
    recommendedPricOrderGoodsList : function(row, editFlag) {
        return { field : "recommendedPrice", title : "정상가",  width: "100px", attributes : {style : "text-align: right;padding-right: 5px;"} , format: "{0:n0}",
            editable: function () {return editFlag == null ? false : editFlag;}
        }
    },
    stockQty : function(row, editFlag) {
        return { field : "stockQty", title : "재고수량", width: "100px", attributes : {style : "text-align: center;"}, format: "{0:n0}",
            editable: function () {return editFlag == null ? false : editFlag;}
            ,template: function(row) {
                var html = row.stockQty;
                //if (fnIsProgramAuth("EDIT_STOCK")) { // 재고수정권한

                    if(row.unlimitStockYn){
                        html +=	"<BR>(무제한)";
                    }else{
                        if(row.notIfStockCnt > 0){
                            html +=	"<BR>한정재고 : "+row.notIfStockCnt+" 개";
                        }else{
                            html +=	"<BR><a role='button' class='k-button k-button-icontext' kind='detailInfoBtn' href='#'>재고상세보기</a>";
                        }
                    }

                //}
                return html;
            }
        }
    },
    /*
    orderCnt2 : function(row, editFlag) {
        return {
            field : "orderCnt", title : "수량", width: "70px", attributes : {style : "text-align:right;padding-right: 5px;"}, format : "{0:n0}",
            editable: function () {return editFlag == null ? false : editFlag;},
            editor : function(container, option) {
                if (!fnIsEmpty(option.model.stockQty)) {
                    let orderCntArr = new Array();

                    let stockQty = option.model.stockQty > 99 ? 99 : option.model.stockQty;
                    for (let i=1; i<=stockQty; i++) {
                        let orderCntMap = new Object();
                        orderCntMap.CODE = i;
                        orderCntMap.NAME = i;
                        orderCntArr.push(orderCntMap);
                    }
                    $('<input id="orderCnt_'+ option.model.no +'" name="orderCnt">').appendTo(container)
                        .kendoDropDownList({
                            dataTextField	: "NAME",
                            dataValueField	: "CODE",
                            dataSource		: {data: orderCntArr},
                            autoWidth		: true
                        });
                }
            }
        }
    },
    */
    orderCnt2 : function(row) {
        return {
            field : "orderCnt", title : "주문수량", width: "70px", attributes : {style : "text-align:right;"}, format : "{0:n0}",
            editor : function(container, option) {

                if (!fnIsEmpty(option.model.stockQty)) {

                    $('<input id="orderCnt_'+ option.model.no +'" name="orderCnt">').appendTo(container).kendoNumericTextBox({
                        decimals: 0,
                        min: 0,
                        maxlength: 4,
                        restrictDecimals: true,
                        spinners: false,
                        format: "#"
                    });
                }
            }
        }
    },
    paidPrice2 : function(row) {
        return { field : "paidPrice", title : "결제금액", width: "60px", attributes : {style : "text-align: right;"}, format: "{0:n0}"}
    },
    orderPrice : function(row) {
        return { field : "orderPrice", title : "주문금액", width: "60px", attributes : {style : "text-align: right;"}, format: "{0:n0}"}
    },
    no : function(row, editFlag) {
        return { field : "no", title : "No", width: "60px", attributes : {style : "text-align:center;"},
            editable: function () {return editFlag == null ? false : editFlag;},
        }
    },
    choiceArrivalScheduledDateList : function(row) {
        return { field : "choiceArrivalScheduledDateList", title : "도착 예정일 선택일", hidden: true }
    },
    deliveryType2 : function(row) {
        return { field : "deliveryType", title : "배송타입", hidden: true }
    },
    shippingIndex : function(row) {
        return { field : "shippingIndex", title : "배송정책 index", hidden: true }
    },
    spCartId : function(row) {
        return { field : "spCartId", title : "장바구니 PK", hidden: true }
    },
    refundPointPrice : function(row) {
        return { field : "refundPointPrice", title : "적립금<br/>환불",width: "70px", attributes : {style : "text-align:right;padding-right: 5px;"}, format: "{0:n0}"}
    },
    refundPrice : function(row) {
        return { field : "refundPrice", title : "결제수단<br/>환불",width: "70px", attributes : {style : "text-align:right;padding-right: 5px;"},format: "{0:n0}"}
    },
    dawnDeliveryYn : function(row) {
        return { field : "dawnDeliveryYn", title : "새벽배송 가능여부", hidden: true }
    },
    orderIfDawnYn : function(row) {
        return { field : "orderIfDawnYn", title : "주문I/F일자 변경시 새벽배송여부", hidden: true }
    },
    ilItemWarehouseId : function(row) {
        return { field : "ilItemWarehouseId", title : "품목별 출고처PK", hidden: true }
    },
    unlimitStockYn : function(row) {
        return { field : "unlimitStockYn", title : "재고 무제한", hidden: true }
    },
    notIfStockCnt : function(row) {
        return { field : "notIfStockCnt", title : "미연동 재고 수량", hidden: true }
    },
    goodsDailyType : function(row) {
        return { field : "goodsDailyType", title : "일일상품 타입", hidden: true }
    },
}


var orderMgmGridUtil = {

    //상품정보 > 상품정보
    orderGoodsList: function(){
        return [
            orderGridColUtil.orderGridCheckbox(this, "checkBoxAll1", "rowCheckbox1", "rowCheckbox", "goodsArea", true), /* 체크박스 */
            orderGridColUtil.odOrderDetlSeq(this, null, true),                          /* 라인번호 */
            orderGridColUtil.ilItemCd(this, null, true),                                /* 마스터품목코드/품목바코드 */
            orderGridColUtil.ilGoodsId(this, null, true),                               /* 상품코드 */
            orderGridColUtil.goodsNm(this, null, true),                                 /* 상품명 */
            orderGridColUtil.goodsTpCd(this),                               /* 상품유형 / (보관방법) */
            orderGridColUtil.orderStatus(this),                             /* 주문상태 */
            orderGridColUtil.recommendedPricOrderGoodsList(this),           /* 정상가 */
            orderGridColUtil.salePrice(this),                               /* 판매가 */
            orderGridColUtil.orderCnt(this),                                /* 주문수량 */
            orderGridColUtil.totalGoodsPrice(this),                         /* 총상품금액 */
            orderGridColUtil.directPrice(this),                             /* 즉시할인 */
            orderGridColUtil.discountEmployeePrice(this),                   /* 임직원할인 */
            orderGridColUtil.couponPrice(this),                             /* 쿠폰할인 */
            //orderGridColUtil.discountPrice(this),                           /* 상품별할인금액 */
            orderGridColUtil.shippingPriceOrderGoodsList(this),             /* 배송비 */
            orderGridColUtil.warehouseNmOrderGoodsList(this),               /* 출고처 + 배송방법 */
            orderGridColUtil.orderIfDt(this),                               /* 주문I/F / 출고예정일 */
            orderGridColUtil.deliveryDt(this),                              /* 도착예정일 */
            orderGridColUtil.trackingNoOrderClaimList(this),               /* 택배사 / 송장번호 */
            orderGridColUtil.sendEndDt(this),                                 /* 송장등록일시 */
            orderGridColUtil.claimDetlStatusNm(this),                       /* (주문상세번호 별) 클레임상태 */
            orderGridColUtil.cancelCnt(this),                               /* 클레임수량 */
            orderGridColUtil.claimAbleCnt(this),                            /* 클레임가능수량 */
            orderGridColUtil.missCnt(this),                                 /* 미출수량 */
            orderGridColUtil.orderMgmCollectionMailDetlId(this),            /* 외부몰 주문상세번호 */
            orderGridColUtil.orderMgmOutmallDetlId(this),                   /* 판매처 주문상세번호 */
            // orderGridColUtil.storageType(this),                          /* 보관방법 */
           // orderGridColUtil.odShippingZoneId(this, false),              /* 배송번호 */
            // orderGridColUtil.shippingNm(this),                           /* 배송정책명 */
            { field : "orderStatusCode"		, hidden : true},
            { field : "odOrderDetlId"		, hidden : true},
            { field : "urWarehouseId"		, hidden : true},
            { field : "bosJons"		        , hidden : true},
            { field : "bundleYn"	        , hidden : true}
        ]
    }
    ,
    // 주문자정보 > 상품정보
    orderGoodsOrderList: function(){
        return [
            orderGridColUtil.checkbox(this, "checkBoxAll2", "rowCheckbox2", "rowCheckbox", "orderArea"), /* 체크박스 */
            orderGridColUtil.odOrderDetlSeq(this),                          /* 라인번호 */
            orderGridColUtil.ilItemCd(this),                                /* 마스터품목코드/품목바코드 */
            orderGridColUtil.ilGoodsId(this),                               /* 상품코드 */
            orderGridColUtil.goodsNm(this),                                 /* 상품명 */
            orderGridColUtil.storageType(this),                             /* 보관방법 */
            orderGridColUtil.orderStatus(this),                             /* 주문상태 */
            orderGridColUtil.orderCnt(this),                                /* 주문수량 */
            orderGridColUtil.cancelCnt(this),                               /* 클레임수량 */
            orderGridColUtil.claimAbleCnt(this),                            /* 클레임가능수량 */
            orderGridColUtil.orderIfDt(this),                               /* 주문I/F / 출고예정일 */
            orderGridColUtil.deliveryDt(this),                              /* 도착예정일 */
            orderGridColUtil.goodsTpCd(this),                               /* 상품유형 / 출고처 */
            orderGridColUtil.trackingNo(this),                              /* 송장번호 / 택배사 */
            orderGridColUtil.odShippingZoneId(this, false),      /* 배송번호 */
            orderGridColUtil.shippingPrice(this),                            /* 배송비 */
            orderGridColUtil.shippingNm(this),                              /* 배송정책명 */
            orderGridColUtil.paidPrice(this),                               /* 총상품금액 */
            orderGridColUtil.discountPrice(this),                           /* 상품별할인금액 */
            orderGridColUtil.collectionMailDetlId(this),                    /* 수집몰 주문상세번호 */
            orderGridColUtil.outmallDetlId(this),                           /* 외부몰 주문상세번호 */
            { field : "orderStatusCode"		, hidden : true},
            { field : "odOrderDetlId"		, hidden : true},
            { field : "urWarehouseId"		, hidden : true},
            { field : "bosJons"		        , hidden : true}
        ]
    }
    ,
    //상품/결제정보 > 쿠폰할인정보
    couponDiscountList: function(){
        return [
            { field : "discountTypeName"		, title : "종류"				, width: "30px"		, attributes : {style : "text-align:center;"},
                template : function(row){
                    let returnVal = row.discountTp;
                    if(row.odClaimId > 0) returnVal += "</br>(재발행 / 클레임 번호: "+row.odClaimId+")"
                    return returnVal;
                }
            }
            , { field : "couponNm"			    , title : "쿠폰명"				, width: "70px"		, attributes : {style : "text-align:center;"}}
            , { field : "discountPrice"		, title : "할인적용금액"			, width: "30px"		, attributes : {style : "text-align:center;"},
                template : function(row){
                    let returnVal = row.discountPrice;
                    if(row.odClaimId > 0) returnVal = "-"
                    return returnVal;
                }
            }
            , { field : "discountValue"		, title : "할인기준"				, width: "30px"		, attributes : {style : "text-align:center;"}}
            , { field : "goodsNm"			    , title : "할인적용상품"			, width: "70px"		, attributes : {style : "text-align:center;"},
                template : function(row){
                    let str = row.goodsNm + row.goodsNmCnt;
                    return str;
                }
               }
            , { field : "couponId"			    , title : "쿠폰번호"				, width: "30px"		, attributes : {style : "text-align:center;"}}
        ]
    }
    ,
    // 처리이력
    orderHistoryList: function(){
        return [
            { field : "historyId"			, title : "No"					, width: "30px"		, attributes : {style : "text-align:center;"}, template : "<span class='row-number'></span>" }
            , { field : "orderDetailId"		, title : "주문상세번호"		, width: "40px"		, attributes : {style : "text-align:center;"}}
            , { field : "regDate"			, title : "변경일자"			, width: "40px"		, attributes : {style : "text-align:center;"}}
            , { field : "procStatus"			, title : "처리상태"			, width: "40px"		, attributes : {style : "text-align:center;"}}
            , { field : "userNm"				, title : "작업자"				, width: "40px"		, attributes : {style : "text-align:center;"}}
            , { field : "histMsg"			, title : "비고"				, width: "60px"		, attributes : {style : "text-align:center;"}}
        ]
    },
    //배송정보 > 상품정보
    orderGoodsShippingList: function(){
        return [
            orderGridColUtil.checkbox(this, "checkBoxAll3", "rowCheckbox3", "rowCheckbox", "shippingArea"), /* 체크박스 */
            orderGridColUtil.odShippingZoneId(this, true),      			/* 배송번호 */
            orderGridColUtil.odOrderDetlSeq(this),                          /* 라인번호 */
            orderGridColUtil.ilItemCd(this),                                /* 마스터품목코드/품목바코드 */
            orderGridColUtil.ilGoodsId(this),                               /* 상품코드 */
            orderGridColUtil.goodsNm(this),                                 /* 상품명 */
            orderGridColUtil.storageType(this),                             /* 보관방법 */
            orderGridColUtil.orderStatus(this),                             /* 주문상태 */
            orderGridColUtil.orderCnt(this),                                /* 주문수량 */
            orderGridColUtil.cancelCnt(this),                               /* 클레임수량 */
            orderGridColUtil.claimAbleCnt(this),                            /* 클레임가능수량 */
            orderGridColUtil.orderIfDt(this),                               /* 주문I/F / 출고예정일 */
            orderGridColUtil.deliveryDt(this),                              /* 도착예정일 */
            orderGridColUtil.goodsTpCd(this),                               /* 상품유형 / 출고처 */
            orderGridColUtil.trackingNo(this),                              /* 송장번호 / 택배사 */
            orderGridColUtil.shippingPrice(this),                           /* 배송비 */
            orderGridColUtil.shippingNm(this),                              /* 배송정책명 */
            orderGridColUtil.paidPrice(this),                               /* 총상품금액 */
            orderGridColUtil.discountPrice(this),                           /* 상품별할인금액 */
            orderGridColUtil.collectionMailDetlId(this),                    /* 수집몰 주문상세번호 */
            orderGridColUtil.outmallDetlId(this),                           /* 외부몰 주문상세번호 */
            { field : "orderStatusCode"	, hidden : true},
            { field : "odOrderDetlId"		, hidden : true}
        ]
    },
    //클레임정보 > 클레임상품정보
    claimGoodsList: function(){
        return [
            orderGridColUtil.checkbox(this, "checkBoxAll4", "rowCheckbox4", "rowCheckbox", "claimArea"), /* 체크박스 */
            orderGridColUtil.claimDt(this),                                 /* 접수일/처리일 */
            orderGridColUtil.odClaimId(this,true),               /* 클레임 번호  */
            orderGridColUtil.claimGoodsListReasonMsg(this),                 /* 쇼핑몰 클레임사유 */
            orderGridColUtil.bosClaimReasons(this),                         /* BOS 클레임사유 */
            orderGridColUtil.odOrderDetlSeq(this),                          /* 라인번호 */
            orderGridColUtil.ilItemCd(this),                                /* 마스터품목코드/품목바코드 */
            orderGridColUtil.ilGoodsId(this),                               /* 상품코드 */
            orderGridColUtil.goodsNm(this),                                 /* 상품명 */
            orderGridColUtil.goodsTpCd(this),                               /* 상품유형(보관방법) */
            orderGridColUtil.orderStatusClaimGoodsList(this),                /* 주문상태 */
            orderGridColUtil.claimStatus(this),                             /* 클레임상태 */
            orderGridColUtil.orderCnt(this),                                /* 주문수량 */
            orderGridColUtil.cancelCnt(this),                               /* 클레임수량 */
            orderGridColUtil.orgClaimAbleCnt(this),                         /* 클레임가능수량 */
            orderGridColUtil.totalGoodsPrice(this),                         /* 총상품금액 */
            orderGridColUtil.shippingPriceClaimGoodsList(this),             /* 배송비 */
            orderGridColUtil.warehouseNm(this),                              /* 출고처 */
            orderGridColUtil.trackingNoOrderClaimList(this),
            orderGridColUtil.discountPriceClaimGoodsList(this),             /* 쿠폰할인차감 */
            orderGridColUtil.refundPointPrice(this),                         /* 적립금환불 */
            orderGridColUtil.refundPrice(this),                             /* 결제수단환불 */
            orderGridColUtil.orderMgmCollectionMailDetlId(this),            /* 외부몰 주문상세번호 */
            orderGridColUtil.orderMgmOutmallDetlId(this),                   /* 판매처 주문상세번호 */

            // orderGridColUtil.claimRequester(this),                          /* 클레임 요청자  */
            // orderGridColUtil.claimReasonMsg(this),                          /* 클레임 상세사유 */
            // orderGridColUtil.claimApprovalDt(this),                         /* 클레임 승인일 */
            // orderGridColUtil.ilItemCd(this),                                /* 마스터품목코드/품목바코드 */
            // orderGridColUtil.ilGoodsId(this),                               /* 상품코드 */
            // orderGridColUtil.goodsNm(this),                                 /* 상품명 */
            // orderGridColUtil.storageType(this),                             /* 보관방법 */
            // orderGridColUtil.orderStatus(this),                             /* 주문상태 */
            // orderGridColUtil.claimStatus(this),                             /* 클레임상태 */
            // orderGridColUtil.orderCnt(this),                                /* 주문수량 */
            // orderGridColUtil.cancelCnt(this),                               /* 클레임수량 */
            // orderGridColUtil.claimAbleCnt(this),                            /* 클레임가능수량 */
            // orderGridColUtil.orderIfDt(this),                               /* 주문I/F / 출고예정일 */
            // orderGridColUtil.deliveryDt(this),                              /* 도착예정일 */
            // orderGridColUtil.goodsTpCd(this),                               /* 상품유형 / 출고처 */
            // orderGridColUtil.trackingNo(this),                              /* 송장번호 / 택배사 */
            // orderGridColUtil.odShippingZoneId(this, false),     			 /* 배송번호 */
            // orderGridColUtil.shippingPrice(this),                           /* 배송비 */
            // orderGridColUtil.shippingNm(this),                              /* 배송정책명 */
            // orderGridColUtil.paidPrice(this),                               /* 총상품금액 */
            // orderGridColUtil.discountPrice(this),                           /* 상품별할인금액 */
            // orderGridColUtil.collectionMailDetlId(this),                    /* 수집몰 주문상세번호 */
            // orderGridColUtil.outmallDetlId(this),                            /* 외부몰 주문상세번호 */
            { field : "claimStatusCode"		, hidden : true},				/*클레임상태코드*/
            { field : "psClaimMallId"		, hidden : true}				/*클레임사유ID*/
        ]
    },
    //배송정보 > 배송정보
    shippingList: function(){
        return [
            orderGridColUtil.deliveryTypeShippingInfo(this), //배송방법(출고처)
            orderGridColUtil.trackingNoShippingInfo(this), //송장번호(택배사)
            orderGridColUtil.shippingPriceShippingInfo(this), //배송비
            orderGridColUtil.goodsNmInfo(this), //상품명(n건 이상인경우 {상품명} 외 00건)
            orderGridColUtil.recvNm(this), //받는분
            orderGridColUtil.recvHp(this), //휴대폰
            orderGridColUtil.recvZipCd(this), //우편번호
            orderGridColUtil.shipRecvAddr1(this), //배송지주소
            orderGridColUtil.shipRecvAddr2(this), //상세주소
            orderGridColUtil.deliveryMsg(this), //배송요청사항
            orderGridColUtil.doorMsgCdName(this), //배송출입정보
            /*{ field : "deliveryDt"		, title : "적용일"		, width: "60px"	, attributes : {style : "text-align:center;"}
               , template :
                  function(dataItem){ // 일일배송일 경우 노출 (도착예정일)
                      let returnVal;
                      if(dataItem.deliveryType == "일일배송") {
                          returnVal = dataItem.deliveryDt;
                      } else {
                          returnVal = "-";
                      }
                      return returnVal;
                   }
              }
          , */
            { field:'management'        , title: '관리'         , width:"130px" , attributes: {style:'text-align:center;'}
                , template:
                    function(dataItem){
                        let returnVal;

                        let orderStatusInfoStr = stringUtil.getString(dataItem.orderStatusInfo, "");
                        let arr = [];
                        arr = orderStatusInfoStr.split('∀'); //'∀'로 split

                        if(dataItem.deliveryTypeCode == 'DELIVERY_TYPE.SHOP_DELIVERY' ||
                            dataItem.deliveryTypeCode == 'DELIVERY_TYPE.SHOP_PICKUP') { // 매장주문인 경우 배송정보 변경 X
                           return '<div></div>';
                        }

                        if(dataItem.deliveryTypeCode == 'DELIVERY_TYPE.DAILY') { //일일배송은 주문상태와 무관하게 변경 가능
                            // 하이톡 스위치
                            if( isHitokSwitch
                                && dataItem.goodsDailyType == 'GOODS_DAILY_TP.GREENJUICE' 
                                && dataItem.orderStatusCode != 'IR' 
                                && dataItem.orderStatusCode != 'IC') { //입금대기중 , 결제완료일 시 변경 가능
                                returnVal = '<div></div>';
                            } else {
                                //잇슬림, 베이비밀 변경버튼 비활성화
                                if((dataItem.goodsDailyType == 'GOODS_DAILY_TP.EATSSLIM' || dataItem.goodsDailyType == 'GOODS_DAILY_TP.BABYMEAL')
                                    && dataItem.orderStatusCode != 'IR' && dataItem.orderStatusCode != 'IC') {
                                      returnVal = '<div></div>';
                                } else {
                                     if (dataItem.histCnt < 2) {
                                        returnVal = '<button type="button" class="btn-white btn-s" kind="shippingListEdit">수정</button>';
                                     } else {
                                        returnVal = '<button type="button" class="btn-white btn-s" kind="shippingListEdit">수정</button> <button type="button" class="btn-white btn-s" kind="shippingHistList">변경 내역</button>';
                                     }
                                }
                            }
                        } else {
                            if(arr.every(elem => elem == 'IR') || arr.every(elem => elem == 'IC')) { //입금대기중 , 결제완료일 시 변경 가능
                                if(dataItem.histCnt < 2){
                                    returnVal = '<button type="button" class="btn-white btn-s normalDeliveryDiv" kind="shippingListEdit">수정</button>';
                                }else{
                                    returnVal = '<button type="button" class="btn-white btn-s normalDeliveryDiv" kind="shippingListEdit">수정</button> <button type="button" class="btn-white btn-s" kind="shippingHistList">변경 내역</button>';
                                }
                            } else {
                                returnVal = '<div class="normalDeliveryHideDiv"></div>';
                            }
                        }

                        return returnVal;
                    }
            }
            , { field : "odOrderDetlId"       , hidden : true}
            , { field : "recvBldNo"			, hidden : true}
            , { field : "orderStatusCode"		, hidden : true}
            , { field : "deliveryTypeCode"		, hidden : true}
        ]
    }
    ,
    //클레임정보 > 클레임 회수정보
    claimReturnShippingList: function(){
        return [
            orderGridColUtil.newDeliveryType2(this), //회수방법(출고처)
            orderGridColUtil.trackingNoShippingInfo(this), //송장번호(택배사)
            orderGridColUtil.shippingPrice4(this), //배송비 (귀책)
            orderGridColUtil.goodsNm3(this), //상품명(n건 이상인경우 {상품명} 외 00건)
            orderGridColUtil.recvNm(this), //받는분
            orderGridColUtil.recvHp(this), //휴대폰
            orderGridColUtil.recvZipCd(this), //우편번호
            orderGridColUtil.returnRecvAddr1(this), //배송지주소
            orderGridColUtil.returnRecvAddr2(this), //상세주소
            orderGridColUtil.deliveryMsg(this) //배송요청사항
        ]
    }
    ,
    //배송정보 > 변경내역 > 수취정보
    orderShippingZoneList : function() {

        return [
            orderGridColUtil.simpleOdShippingZoneId(this),
            orderGridColUtil.deliveryType(this),
            orderGridColUtil.recvNm(this),
            orderGridColUtil.recvHp(this),
            orderGridColUtil.recvZipCd(this),
            orderGridColUtil.recvAddr1(this),
            orderGridColUtil.recvAddr2(this),
            orderGridColUtil.deliveryMsg(this),
            orderGridColUtil.doorMsgCdName(this),
            { field : "createDt"			, title : "등록일자"				, width: "50px"		, attributes : {style : "text-align:center;"}}
            , { field : "deliveryAreaNm"		, title : "권역정보"				, width: "50px"		, attributes : {style : "text-align:center;"}}
            , { field : "deliveryDt"			, title : "적용일자"				, width: "50px"		, attributes : {style : "text-align:center;"}}
            , { field : "recvBldNo"			, hidden : true}
        ]
    }
    ,
    //배송정보 > 변경내역 > 변경정보
    orderShippingZoneHistList : function() {
        return [
            orderGridColUtil.simpleOdShippingZoneId(this),
            orderGridColUtil.deliveryType(this),
            orderGridColUtil.recvNm(this),
            orderGridColUtil.recvHp(this),
            orderGridColUtil.recvZipCd(this),
            orderGridColUtil.recvAddr1(this),
            orderGridColUtil.recvAddr2(this),
            orderGridColUtil.deliveryMsg(this),
            orderGridColUtil.doorMsgCdName(this),
            { field : "createDt"			, title : "등록일자"				, width: "50px"		, attributes : {style : "text-align:center;"}}
            , { field : "deliveryAreaNm"		, title : "권역정보"				, width: "50px"		, attributes : {style : "text-align:center;"}}
            , { field : "recvBldNo"			, hidden : true}
        ]
    }
    ,
    //결제정보 > 결제상세정보 > 즉시할인내역
    orderDirectDiscountHistList : function() {
        return [
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
        ]
    }
    ,
    //클레임상세팝업 > 상품정보 (기본)
    claimMgmPopupOrderGoodsList : function() {
        return [
            { field : "odOrderDetlSeq"			, title : "주문순번"				, width: "50px"		, attributes : {style : "text-align:center;"}, editable : function(row) {return false;}},
            { field : "orderStatusNm"			, title : "주문상태"				, width: "50px"		, attributes : {style : "text-align:center;"}, editable : function(row) {return false;}},
            orderGridColUtil.goodsTpNm(this),                               /* 상품유형 / 출고처 */
            orderGridColUtil.ilItemCd(this),                                /* 마스터품목코드/품목바코드 */
            orderGridColUtil.ilGoodsId(this),                               /* 상품코드 */
            orderGridColUtil.claimGoodsNm(this),                                 /* 상품명 */
            { field : "storageTypeNm"		, title : "보관방법"				, width: "50px"		, attributes : {style : "text-align:center;"}, format: "{0:n0}", editable : function(row) {return false;}}
            , { field : "claimCnt"			, title : "클레임수량"				, width: "50px"		, attributes : {style : "text-align:center;"}
                ,template: function(dataItem) {
                    return "<div class='claimCnt-wrapper'><input type='text' class='comm-input' style='width: 100%;' value='" + (dataItem.odOrderDetlSeq != 0 ? dataItem.claimCnt : 0) + "' /></div>"
                }
                ,editor: function (container, options) {

                    var selectedRow = $(container).closest("tr");
                    var selectedRowData = claimMgmPopupOrderGoodsGrid.dataItem(selectedRow);
                    var firstClaimCnt = 0;
                    if(selectedRowData.odOrderDetlSeq > 0) {
                        if (claimCntObject[selectedRowData.odOrderDetlId] == undefined) {
                            claimCntObject[selectedRowData.odOrderDetlId] = selectedRowData.claimCnt;
                            firstClaimCnt = selectedRowData.claimCnt;
                        } else {
                            firstClaimCnt = claimCntObject[selectedRowData.odOrderDetlId];
                        }
                    }

                    var input = "";
                    if (options.model.useYn == 'Y') {
                        var input = $("<input id='targetTypeDropDown' />");
                    } else {
                        var input = $("<input id='targetTypeDropDown' />");
                    }
                    input.appendTo(container);

                    var data = [];
                    // 취소 요청 / 반품 요청일 경우 클레임 수량이 변경 되면 안됨
                    if(paramData.orderStatusCd == "CA" || paramData.orderStatusCd == "RA" || paramData.orderStatusCd == "RI" || paramData.undeliveredClaimYn == "Y") {
                        data.push({
                            CODE : selectedRowData.claimCnt,
                            NAME : selectedRowData.claimCnt,
                        });
                    }
                    else {
                        if(options.model.reShipping == true) {
                            for( var i = 1; i <= 99; i++ ) {
                                data.push({
                                    CODE : i,
                                    NAME : i,
                                });
                            }
                        }
                        else {
                            for( var i = firstClaimCnt; i > 0; i-- ) {
                                data.push({
                                    CODE : i,
                                    NAME : i,
                                });
                            }
                        }
                    }
                    fnKendoDropDownList({
                        id: 'targetTypeDropDown',
                        data : data,
                        textField :"NAME",
                        valueField : "CODE",
                        /* HGRM-2013 - dgyoun : selectbox 수정 선택 시 기존 값 선택  Start */
                        // Databound : 렌더링 된 후 발생하는 이벤트
                        dataBound: function (e) {

                            // 현재 선택된 값을 선택된 열에서 찾는다
                            const FIELD_NAME = "claimCnt";
                            const _currentVal = claimMgmPopupOrderGoodsGrid.dataItem(selectedRow)[FIELD_NAME];
                            const self = e.sender;
                            const data = e.sender.dataSource.data();

                            if (!data.length) {
                                return;
                            }

                            //드롭다운 리스트에서 선택된 값과 같은 값을 가진 데이터의 index값을 찾고,
                            //드롭다운 리스트의 선택된 인덱스를 변경한다.
                            data.forEach(function (d, index) {
                                if (d.NAME == _currentVal) {
                                    self.select(index);
                                }
                            });

                            self.open();
                            //claimMgmFunctionUtil.claimCountChangeEvent(options);
                        },
                        /* HGRM-2013 - dgyoun : selectbox 수정 선택 시 기존 값 선택  End */

                    });
                }}
            , { field : "recommendedPrice"	, title : "정상가"				, width: "50px"		, attributes : {style : "text-align:center;"}, format: "{0:n0}",editable : function(row) {return false;}}
            , { field : "salePrice"			, title : "판매가"				, width: "50px"		, attributes : {style : "text-align:center;"}, format: "{0:n0}",editable : function(row) {return false;}}
            , { field : "orderPrice"		, title : "주문금액"				, width: "50px"		, attributes : {style : "text-align:center;"}, format: "{0:n0}",editable : function(row) {return false;}}
            , { field : "couponPrice"       , title : "쿠폰할인"              , width: "50px"	, attributes : {style : "text-align:center;"}, format: "{0:n0}",editable : function(row) {return false;}}
            , { field : "paidPrice"			, title : "결제금액"				, width: "50px"		, attributes : {style : "text-align:center;"}, format: "{0:n0}",editable : function(row) {return false;}}
            , { field : "odOrderDetlId"		, hidden : true}						    /* 주문 상세 pk */
            , { field : "odOrderDetlParentId"	, hidden : true}						/* 주문상세 부모 ID */
            , { field : "urWarehouseId"		    , hidden : true}						/* 출고처ID(출고처PK) */
            , { field : "totSalePrice"		    , hidden : true}						/* 총상품금액 */
            , { field : "goodsDeliveryType"		, hidden : true}						/* 상품배송타입 */
            , { field : "orderStatusDeliTp"		, hidden : true}						/* 주문상태 배송유형 */
        ]
    }
    ,
    //클레임상세팝업 > 미출정보 (기본)
    claimMgmPopupOrderUndeliveredList : function() {
        return [
            { field : "odOrderDetlSeq"			, title : "주문순번"				, width: "50px"		, attributes : {style : "text-align:center;"}, editable : function(row) {return false;}},
            { field : "orderStatusNm"			, title : "주문상태"				, width: "50px"		, attributes : {style : "text-align:center;"}, editable : function(row) {return false;}},
            orderGridColUtil.goodsTpNm(this),                               /* 상품유형 / 출고처 */
            orderGridColUtil.ilItemCd(this),                                /* 마스터품목코드/품목바코드 */
            orderGridColUtil.ilGoodsId(this),                               /* 상품코드 */
            orderGridColUtil.claimGoodsNm(this),                                 /* 상품명 */
            { field : "storageTypeNm"		, title : "보관방법"				, width: "50px"		, attributes : {style : "text-align:center;"}, format: "{0:n0}", editable : function(row) {return false;}}
            , { field : "missCnt"			, title : "미출수량"				, width: "50px"		, attributes : {style : "text-align:center;"}}
            , { field : "recommendedPrice"	, title : "정상가"				, width: "50px"		, attributes : {style : "text-align:center;"}, format: "{0:n0}",editable : function(row) {return false;}}
            , { field : "salePrice"			, title : "판매가"				, width: "50px"		, attributes : {style : "text-align:center;"}, format: "{0:n0}",editable : function(row) {return false;}}
            , { field : "orderPrice"			, title : "주문금액"				, width: "50px"		, attributes : {style : "text-align:center;"}, format: "{0:n0}",editable : function(row) {return false;}}
            , { field : "couponPrice"         , title : "쿠폰할인"              , width: "50px"	, attributes : {style : "text-align:center;"}, format: "{0:n0}",editable : function(row) {return false;}}
            , { field : "paidPrice"			, title : "결제금액"				, width: "50px"		, attributes : {style : "text-align:center;"}, format: "{0:n0}",editable : function(row) {return false;}}
            ,	{ field : "odOrderDetlId"		, hidden : true}						/* 주문 상세 pk */
            , { field : "odOrderDetlParentId"	, hidden : true}						/* 주문상세 부모 ID */
            , { field : "urWarehouseId"		, hidden : true}						/* 출고처ID(출고처PK) */
        ]
    }
    ,
    // 승인관리자정보
    orderClaimApprManageList : function () {
        return [
             { field   : 'apprAdminInfo'      , title     : '승인관리자 정보'           , width : '100px' , attributes  : {style : 'text-align:center'}}
            , { field   : 'adminTypeName'     , title     : '계정<BR>유형'              , width : '100px' , attributes  : {style : 'text-align:center'}}
            , { field   : 'apprUserNm'        , title     : '관리자<BR>(이름/ID)'       , width : '100px' , attributes  : {style : 'text-align:center'}
                , template  : function(dataItem) {
                    let returnValue;
                    returnValue = dataItem.apprUserName + '/' + dataItem.apprLoginId;
                    return returnValue;
                }
            }
            , { field : 'organizationName'    , title     : '조직/거래처 정보'          , width : '100px' , attributes  : {style : 'text-align:center'}}
            , { field : 'teamLeaderYn'        , title     : '조직장여부'               , width :  '80px' , attributes  : {style : 'text-align:center'}}
            , { field : 'userStatusName'      , title     : 'BOS<BR>계정상태'          , width :  '80px' , attributes  : {style : 'text-align:center'}}
            , { field : 'grantUserInfo'       , title     : '권한위임정보<BR>(이름/ID)' , width : '100px' , attributes  : {style : 'text-align:center'}
                , template  : function(dataItem) {
                    let returnValue;
                    if(dataItem.grantAuthYn == 'Y') {
                        returnValue = dataItem.grantUserName + '/' + dataItem.grantLoginId;
                    }
                    else {
                        returnValue = '';
                    }
                    return returnValue;
                }
            }
            , { field : 'grantAuthPeriod'     , title     : '권한위임기간'            , width : '150px' , attributes : {style : 'text-align:left'}
                , template  : function(dataItem) {
                    let returnValue;
                    if(dataItem.grantAuthYn == 'Y') {
                        returnValue = dataItem.grantAuthStartDt + '~' + dataItem.grantAuthEndDt;
                    }
                    else {
                        returnValue = '';
                    }
                    return returnValue;
                }
            }
            , { field : 'grantUserStatusName' , title     : 'BOS<BR>계정상태' , width : '100px' , attributes : {style : 'text-align:left'}
                , template  : function(dataItem){
                    let returnValue;
                    if(dataItem.grantAuthYn == 'Y') {
                        returnValue = dataItem.grantUserStatusName;
                    }
                    else {
                        returnValue = '';
                    }
                    return returnValue;
                }
            }
            , { field:'addCoverageId', hidden:true}
            , { field:'includeYn', hidden:true}
        ];
    }
    ,
    // 주문복사 > 상품 정보
    orderCopyGoodsList: function() {
        return [
            //orderGridColUtil.checkbox(this, "checkBoxAll5", "rowCheckbox5", "rowCheckbox", "goodsArea"), /* 체크박스 */
            orderGridColUtil.orderCopyCheckbox(this, "checkBoxAll", "rowCheckbox", "rowCheckbox", false), /* 체크박스 */
            orderGridColUtil.odOrderDetlSeq(this, false),                          	/* 라인번호 */
            orderGridColUtil.ilItemCd(this, false),                                	/* 마스터품목코드/품목바코드 */
            orderGridColUtil.ilGoodsId(this, false),                               	/* 상품코드 */
            orderGridColUtil.goodsNm(this, false),                                 	/* 상품명 */
            orderGridColUtil.goodsTpCd(this, false),                               	/* 상품유형 / 출고처 */
            orderGridColUtil.orderStatus(this),                             		/* 주문상태 */
            orderGridColUtil.salePrice(this),                               		/* 판매가 */
            orderGridColUtil.orderCopyOrderCnt(this, true),                       	/* 주문수량 */
            orderGridColUtil.totalGoodsPrice(this, false),                          /* 총상품금액 */
            orderGridColUtil.couponPrice(this),                             		/* 쿠폰할인 */
            { field : "paidPrice"	, title : "상품별<br>결제금액"	, width: "90px"	, attributes : {style : "text-align:right;"}, format: "{0:n0}",editable : function(row) {return false;}},
            //orderGridColUtil.discountPrice(this, false),                           	/* 상품별할인금액 */
            orderGridColUtil.shippingPrice(this, false),                         	/* 배송비 */
            orderGridColUtil.orderCopyOrderIfDt(this, false),                      	/* 주문I/F / 출고예정일 */
            orderGridColUtil.deliveryDt(this, false),                              	/* 도착예정일 */
            //orderGridColUtil.claimDetlStatusNm(this),                            	/* 클레임상태 */
	        {field : "claimDetlStatusNm", title : "클레임상태", width: "70px", attributes : {style : "text-align:center;"}, editable : function () {return editFlag == null ? false : editFlag;}},	/* 클레임상태 */
            //orderGridColUtil.cancelCnt(this),                               		/* 클레임수량 */
	        {field : "cancelCnt", title : "클레임수량", width: "70px", attributes : {style : "text-align:right;padding-right: 5px; color:red;"}, format: "{0:n0}", editable : function () {return editFlag == null ? false : editFlag;}}, 		/* 클레임수량 */
            {field : "orgClaimAbleCnt", title : "클레임가능<br/>수량", width: "80px", attributes : {style : "text-align:right;padding-right: 5px;"}, format: "{0:n0}", editable : function () {return editFlag == null ? false : editFlag;}},	/* 클레임가능수량 */
            orderGridColUtil.collectionMailDetlId(this, false),                    	/* 수집몰 주문상세번호 */
            orderGridColUtil.outmallDetlId(this, false),                           	/* 외부몰 주문상세번호 */
            { field : "odOrderDetlId"		, hidden : true},						/* 주문 상세 pk */
            { field : "odOrderDetlParentId"	, hidden : true},						/* 주문상세 부모 ID */
            { field : "urWarehouseId"		, hidden : true},						/* 출고처ID(출고처PK) */
            { field : "odShippingZoneId"	, hidden : true},						/* 배송번호 */
            { field : "shippingNm"			, hidden : true},						/* 배송정책명 */
            { field : "orderStatusCode"		, hidden : true},
            { field : "bundleYn"	        , hidden : true},
            { field : "warehouseNm"	        , hidden : true},
            { field : "trackingNo"	        , hidden : true}
        ]
    },
    // 주문복사 > 배송 정보
    orderCopyshippingList: function() {
        return [
            orderGridColUtil.simpleOdShippingZoneId(this),			/* 배송번호 */
            orderGridColUtil.deliveryType(this),					/* 배송방법 */
            orderGridColUtil.orgRecvNm(this),							/* 받는분 */
            orderGridColUtil.orgRecvHp(this),							/* 휴대폰 */
            orderGridColUtil.recvZipCd(this),						/* 우편번호 */
            orderGridColUtil.orgRecvAddr1(this),						/* 주소1 */
            orderGridColUtil.orgRecvAddr2(this),						/* 주소2 */
            orderGridColUtil.deliveryMsg(this),					/* 배송요청사항 */
            orderGridColUtil.doorMsgCdName(this),					/* 배송출입정보 */
            orderGridColUtil.applyDt(this),						/* 적용일 */
            //{ field	: 'management'	, title: '관리'	, width:"60px" , attributes: {style:'text-align:center;'}},
            { field : 'management'	, title: '관리'  , width:"60px" , attributes: {style:'text-align:center;'}
	            , template:
	                function(dataItem){
	                    let returnVal;
	                    //결제완료, 배송준비중인 경우 변경 가능
	                    //if(dataItem.orderStatusCode ==  'IC' || dataItem.orderStatusCode ==  'DR'){
	                        //if(dataItem.histCnt < 2){
	                            returnVal = '<button type="button" class="btn-white btn-s" kind="shippingListEdit">수정</button>';
	                        //}else{
	                        //    returnVal = '<button type="button" class="btn-white btn-s" kind="shippingListEdit">수정</button> <button type="button" class="btn-white btn-s" kind="shippingHistList">변경 내역</button>';
	                        //}
	                    //} else {
	                    //    returnVal = '';
	                    //}
	                    return returnVal;
	                }
            },
            { field 	: 'recvBldNo'	, hidden : true}
        ]
    }
    ,
    //상품정보 > 증정품 내역 팝업
    orderGiftList : function() {
        return [
            { field : "ilGoodsId"		, title : "상품코드"			, width: "30px"		, attributes : {style : "text-align:center;"}}
            ,	{ field : "goodsNm"			, title : "상품명"			, width: "30px"		, attributes : {style : "text-align:center;"}}
            , { field : "remarks"			, title : "비고"				, width: "50px"		, attributes : {style : "text-align:center;"}
                ,	template:
                    function(dataItem){ // [비고] 기획전명 / 증정조건
                        let str = "";
                        str += dataItem.title == null ? '' : dataItem.title;
                        if(dataItem.exhibitInfo != ''){
                            str += " / " + dataItem.exhibitInfo;
                        }
                        return str;
                    }
            }
        ]
    }
    ,
    // 주문생성 > 상품 정보
    orderCreateGoodsList: function() {
        return [
            orderGridColUtil.orderCreateCheckbox(this, "checkBoxAll", "rowCheckbox", "rowCheckbox", false), /* 체크박스 */
            orderGridColUtil.no(this, false),					/* No */
            orderGridColUtil.ilGoodsId2(this, false),			/* 상품코드 */
            orderGridColUtil.goodsNm2(this, false),				/* 상품명 */
            orderGridColUtil.recommendedPrice(this, false),		/* 정상가 */
            orderGridColUtil.salePrice(this, false),			/* 판매가 */
            orderGridColUtil.stockQty(this, false),				/* 재고수량 */
            orderGridColUtil.orderCnt2(this, true),				/* 수량 */
            orderGridColUtil.paidPrice(this, false, '총상품금액'),	/* 결제금액 */
            orderGridColUtil.shippingPrice(this, false),		/* 배송비 */
            //orderGridColUtil.orderIfDt2(this, true),			/* 주문I/F */
            orderGridColUtil.warehouseNm(this),                         /* 출고처명 */
            orderGridColUtil.orderCreateOrderIfDt(this, false),                               /* 주문I/F / 출고예정일 */
            orderGridColUtil.deliveryDt(this),                              /* 도착예정일 */

            //orderGridColUtil.choiceArrivalScheduledDateList(this),	/* 도착 예정일 선택일 */
            orderGridColUtil.deliveryType2(this),				/* 배송타입 */
            orderGridColUtil.shippingIndex(this),				/* 배송정책 index */
            orderGridColUtil.spCartId(this),					/* 장바구니 PK */
            orderGridColUtil.dawnDeliveryYn(this),              /* 새벽배송 가능여부 */
            orderGridColUtil.orderIfDawnYn(this),               /* 주문I/F 변경시 새벽배송여부 */
            orderGridColUtil.ilItemWarehouseId(this),            /* 품목별 출고처 PK */
            orderGridColUtil.unlimitStockYn(this),            /* 재고 무제한 여부 */
            orderGridColUtil.notIfStockCnt(this),            /* 미연동 재고 한정수량 */
            orderGridColUtil.goodsDailyType(this),              /* 일일상품 타입 */
            { field : "goodsDailyCycleType"		                , hidden : true},   /* 일일상품 배송주기 */
            { field : "goodsDailyCycleGreenJuiceWeekType"		, hidden : true},   /* 일일상품 배송요일 */
            { field : "goodsDailyCycleTermType"		            , hidden : true},   /* 일일상품 배송기간 */
            { field : "goodsBulkType"		                    , hidden : true},   /* 일일상품 세트수량 */
            { field : "goodsDailyBulkYn"		                , hidden : true},   /* 일일상품 배송유형 */
            { field : "goodsDailyAllergyYn"		                , hidden : true},   /* 일일상품 알러지식단여부 */
            { field : "storeDeliveryIntervalType"		        , hidden : true},   /* 배송가능한 스토어 배송방식(매일,격일,택배) */
            { field : "spCartAddGoodsId"		                , hidden : true},   /* 장바구니 추가 구성상품 PK */
            { field : "goodsType"		                        , hidden : true}    /* 상품타입 */

            
        ]
    }
    ,
    // 주문생성 > 주문 정보
    orderCreateList: function() {
        return [
            orderGridColUtil.no(this),						/* No */
            orderGridColUtil.recvNm(this, '수취인명'),			/* 수취인 명 */
            orderGridColUtil.recvHp(this, '수취인\n연락처'),	/* 수취인 연락처 */
            orderGridColUtil.recvZipCd(this),				/* 수취인 우편번호 */
            orderGridColUtil.recvAddr1(this),				/* 수취인 주소1 */
            orderGridColUtil.recvAddr2(this),				/* 수취인 주소2 */
            orderGridColUtil.ilItemCd(this),				/* 마스터품목코드/품목바코드 */
            orderGridColUtil.ilGoodsId(this),				/* 상품코드 */
            orderGridColUtil.goodsNm(this),					/* 상품명 */
            orderGridColUtil.storageType(this),             /* 보관방법 */
            orderGridColUtil.orderCnt(this),                /* 주문수량 */
            orderGridColUtil.recommendedPrice(this),		/* 정상가 */
            orderGridColUtil.salePrice(this),				/* 판매가 */
            orderGridColUtil.orderPrice(this),				/* 주문금액 */
            orderGridColUtil.shippingPrice(this),			/* 배송비 */
            { field : "ilShippingTmplId", hidden : true},   /* 배송정책 PK  */
            { field : "urWarehouseId", hidden : true},       /* 출고처 PK  */
            { field : "goodsTp", hidden : true},             /* 상품타입 */
            { field : "orgSalePrice", hidden : true}
        ]
    }
    ,
    //클레임상세 CS환불 팝업 > 상품정보 (기본)
    claimMgmCSPopupOrderGoodsList : function() {
        return [
            { field : "odOrderDetlSeq"			, title : "주문순번"				, width: "50px"		, attributes : {style : "text-align:center;"}, editable : function(row) {return false;}},
            { field : "orderStatusNm"			, title : "주문상태"				, width: "50px"		, attributes : {style : "text-align:center;"}, editable : function(row) {return false;}},
            orderGridColUtil.goodsTpNm(this),                               /* 상품유형 / 출고처 */
            orderGridColUtil.ilItemCd(this),                                /* 마스터품목코드/품목바코드 */
            orderGridColUtil.ilGoodsId(this),                               /* 상품코드 */
            orderGridColUtil.claimGoodsNm(this),                                 /* 상품명 */
            { field : "storageTypeNm"		, title : "보관방법"				, width: "50px"		, attributes : {style : "text-align:center;"}, format: "{0:n0}", editable : function(row) {return false;}}
            , { field : "claimCnt"			, title : "클레임수량"			, width: "50px"		, attributes : {style : "text-align:center;"}, format: "{0:n0}",editable : function(row) {return false;}}
            , { field : "recommendedPrice"	, title : "정상가"				, width: "50px"		, attributes : {style : "text-align:center;"}, format: "{0:n0}",editable : function(row) {return false;}}
            , { field : "salePrice"			, title : "판매가"				, width: "50px"		, attributes : {style : "text-align:center;"}, format: "{0:n0}",editable : function(row) {return false;}}
            , { field : "orderPrice"		, title : "주문금액"				, width: "50px"		, attributes : {style : "text-align:center;"}, format: "{0:n0}",editable : function(row) {return false;}}
            , { field : "couponPrice"       , title : "쿠폰할인"              , width: "50px"	, attributes : {style : "text-align:center;"}, format: "{0:n0}",editable : function(row) {return false;}}
            , { field : "paidPrice"			, title : "결제금액"				, width: "50px"	    , attributes : {style : "text-align:center;"}, format: "{0:n0}",editable : function(row) {return false;}}
            , { field : "csRefundPrice"     , title : "CS환불금액"           , width: "50px"     , attributes : {style : "text-align:center;"}, format: "{0:n0}",editable : function(row) {return false;},
                    template : function(row){
                        let str = "";
                        if(row.odOrderDetlSeq > 0) {
                            str = '<input type="number" id="csRefundPrice" name="csRefundPrice" class="comm-input num_only" style="width : 100%;" value="0" onblur="$scope.fnInputCSRefundPrice(this)">';
                        }
                        return str;
                    }
                }
            , { field : "odOrderDetlId"		, hidden : true}						/* 주문 상세 pk */
            , { field : "odOrderDetlParentId"	, hidden : true}						/* 주문상세 부모 ID */
            , { field : "urWarehouseId"		, hidden : true}						/* 출고처ID(출고처PK) */
        ]
    }


}

/* 아래 확인 후 정리 예정*/
var imSiUtil = {

    // 주문복사 > 배송 정보
    orderCopyshippingList: function() {
    return [
        //orderGridColUtil.simpleOdShippingZoneId(this),			/* 배송번호 */
        orderGridColUtil.orderCopyDeliveryType(this),			/* 배송방법 */
        orderGridColUtil.trackingNo(this),						/* 송장번호 */
        orderGridColUtil.shippingPrice(this, false),			/* 배송비 */
        orderGridColUtil.goodsNm(this, false),					/* 상품명 */
        orderGridColUtil.recvNm(this),							/* 받는분 */
        orderGridColUtil.recvHp(this),							/* 휴대폰 */
        orderGridColUtil.recvZipCd(this),						/* 우편번호 */
        orderGridColUtil.recvAddr1(this),						/* 배송지주소 */
        orderGridColUtil.recvAddr2(this),						/* 상세주소 */
        orderGridColUtil.deliveryMsg(this),					/* 배송요청사항 */
        orderGridColUtil.doorMsgCdName(this),					/* 배송출입정보 */
        { field	: 'management'	, title: '관리'	, width:"60px" , attributes: {style:'text-align:center;'}},
        { field 	: 'odShippingZoneId'	, hidden : true},			/* 주문 배송지 PK */
        { field 	: 'odShippingPriceId'	, hidden : true},			/* 주문 배송비 PK */
        { field 	: 'odOrderDetlId'		, hidden : true},			/* 주문 상세 pk */
        { field 	: 'urWarehouseId'	, hidden : true},				/* 출고지 PK */
        { field 	: 'odShippingZoneId'	, hidden : true},			/* 주문 배송지 PK */
        { field 	: 'ilGoodsShippingTemplateId'	, hidden : true},	/* 배송비정책 PK */
        { field 	: 'ilGoodsId'	, hidden : true},					/* 상품 PK */
        { field 	: 'recvTel'	, hidden : true},						/* 받는사람 전화번호 */
        { field 	: 'recvMail'	, hidden : true},					/* 받는사람 메일주소 */
        { field 	: 'recvBldNo'	, hidden : true}					/* 건물번호 */
    ]
}
,
//상품정보 > 증정품 내역 팝업
orderGiftList : function() {
    return [
        { field : "ilGoodsId"		, title : "상품코드"			, width: "30px"		, attributes : {style : "text-align:center;"}}
        ,	{ field : "goodsNm"			, title : "상품명"			, width: "30px"		, attributes : {style : "text-align:center;"}}
        , { field : "remarks"			, title : "비고"				, width: "50px"		, attributes : {style : "text-align:center;"}
            ,	template:
                function(dataItem){ // [비고] 기획전명 / 증정조건
                    let str = "";
                    str += dataItem.title == null ? '' : dataItem.title;
                    if(dataItem.exhibitInfo != ''){
                        str += " / " + dataItem.exhibitInfo;
                    }
                    return str;
                }
        }
    ]
}
,
// 주문생성 > 상품 정보
orderCreateGoodsList: function() {
    return [
        orderGridColUtil.no(this, false),					/* No */
        orderGridColUtil.ilGoodsId2(this, false),			/* 상품코드 */
        orderGridColUtil.goodsNm2(this, false),				/* 상품명 */
        orderGridColUtil.recommendedPrice(this, false),		/* 정상가 */
        orderGridColUtil.salePrice(this, false),			/* 판매가 */
        orderGridColUtil.stockQty(this, false),				/* 재고수량 */
        orderGridColUtil.orderCnt2(this, true),				/* 수량 */
        orderGridColUtil.paidPrice(this, false, '결제금액'),	/* 결제금액 */
        orderGridColUtil.shippingPrice(this, false),		/* 배송비 */
        orderGridColUtil.orderIfDt2(this, true),			/* 주문I/F */
        orderGridColUtil.choiceArrivalScheduledDateList(this),	/* 도착 예정일 선택일 */
        orderGridColUtil.deliveryType2(this),				/* 배송타입 */
        orderGridColUtil.shippingIndex(this),				/* 배송정책 index */
        orderGridColUtil.spCartId(this)						/* 장바구니 PK */
    ]
}
,
// 주문생성 > 주문 정보
orderCreateList: function() {
    return [
        orderGridColUtil.no(this),						/* No */
        { field : "recvNm"	, title : "수취인명"		, width: "80px"	, attributes : {style : "text-align: center"}},	/* 수취인 명 */
        { field : "recvHp"	, title : "수취인\n연락처"	, width: "120px", attributes : {style : "text-align: center"},	/* 수취인 연락처 */
            template: function (row) {return fnPhoneNumberHyphen(row.recvHp);}
        },
        { field : "recvZipCd", title : "우편번호"		, width: "70px"	, attributes : {style : "text-align: center"}},	/* 수취인 우편번호 */
        { field : "recvAddr1", title : "주소1"		, width: "180px", attributes : {style : "text-align: center"}},	/* 수취인 주소1 */
        { field : "recvAddr2", title : "주소2"		, width: "150px", attributes : {style : "text-align: center"}},	/* 수취인 주소2 */
        { field : "ilItemCd" , title : "마스터품목코드<br/>품목바코드", width: "140px", attributes: {style : "text-align:center;"},	/* 마스터품목코드/품목바코드 */
            editable: function() {return editFlag == null ? false : editFlag;},
            template : function(row) {
                let str = "<div style='display: none;'>-</div>";
                if (row.goodsTpCd != 'GOODS_TYPE.PACKAGE') {
                    str = stringUtil.getString(row.ilItemCd, "") + "<br/>" + stringUtil.getString(row.itemBarcode, "");
                }
                return str;
            }
        },
        orderGridColUtil.ilGoodsId(this),				/* 상품코드 */
        { field : "goodsNm"	, title : "상품명"		, width: "350px", attributes : {style : "text-align: left;"},	/* 상품명 */
            template : function(row) {
                if (fnIsEmpty(row.goodsNm)) return '<div style="color: red;">['+ row.errSaleStatus +']</div>';
                let saleStatusNm = '';
                // 판매상태가 판매중이 아닌경우 판매상태명 표시
                if (row.saleStatus != 'SALE_STATUS.ON_SALE') {
                    saleStatusNm = '</br>' + '<div style="color: red;">['+ row.saleStatusNm +']</div>';
                }
                return row.goodsNm + saleStatusNm;
            }
        },
        { field : "storageMethodTypeName", title : "보관<br/>방법", width: "50px", attributes : {style : "text-align:center;"},	/* 보관방법 */
            template : function(row) {
                if (fnIsEmpty(row.storageType)) return '';
                let str = "<div style='display: none;'>-</div>";
                if (row.goodsTpCd != 'GOODS_TYPE.PACKAGE') {
                    str = row.storageMethodTypeName;
                }
                return str;
            }
        },
        orderGridColUtil.orderCnt(this),                /* 주문수량 */
        { field : "recommendedPrice", title : "정상가", 	width: "80px", attributes : {style : "text-align: right;padding-right: 5px;"} , format: "{0:n0}"},	/* 정상가 */
        { field : "salePrice"		, title : "판매가", 	width: "120px", attributes : {style : "text-align: right;padding-right: 5px;"} , format: "{0:n0}",
            template : function(row) {
                if (fnIsEmpty(row.salePrice) || fnIsEmpty(row.recommendedPrice)) return fnNumberWithCommas(row.salePrice);
                let priceConfirm = '';
                if (row.salePrice <= (row.recommendedPrice * 0.5)) {
                    priceConfirm = '</br>' + '<div style="color: #ff0000;">(판매가 확인)</div>';
                }
                return fnNumberWithCommas(row.salePrice) + priceConfirm;
            }
        },	/* 판매가 */
        { field : "orderPrice"		, title : "주문금액", 	width: "80px", attributes : {style : "text-align: right;padding-right: 5px;"} , format: "{0:n0}"},	/* 주문금액 */
    ]
}
}