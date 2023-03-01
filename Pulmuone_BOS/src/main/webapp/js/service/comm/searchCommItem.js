/**---------------s--------------------------------------------------------------
 * description 		 : 주문관련 함수
 * @
 * @ 수정일			수정자          수정내용
 * @ ------------------------------------------------------
 * @ 2020.11.12		이명수   최초생성
 * @
 * **/

var mutilSearchCommonUtil = {
        default: function () {
            fnTagMkRadio({
                id: 'selectConditionType',
                tagId: 'selectConditionType',
                chkVal: 'multiSection',
                tab: true,
                data: [{
                    CODE: "multiSection",
                    NAME: "복수조건 검색",
                    TAB_CONTENT_NAME: "multiSection"
                },{
                    CODE: "singleSection",
                    NAME: "단일조건 검색",
                    TAB_CONTENT_NAME: "singleSection"
                }],
            });
            //[공통] 탭 변경 이벤트
            fbTabChange();
        }
    };


var searchCommonUtil = {
    /* 기간 검색 */
    dateSearch: function(){
        fnKendoDropDownList({
            id : "dateSearchType",
            tagId : "dateSearchType",
            data : [
                    { "CODE" : "CREATE_DATE", "NAME" : "주문일자" },
                    { "CODE" : "MODIFY_DATE", "NAME" : "결제완료일" }
            ]
        });
        // 등록(가입)일 시작
        fnKendoDatePicker({
            id: "dateSearchStart",
            format: "yyyy-MM-dd",
            btnStartId: "dateSearchStart",
            btnEndId: "dateSearchEnd",
            change : function(e) {
                fnStartCalChange("dateSearchStart", "dateSearchEnd");
            }
        });
        // 등록(가입)일 종료
        fnKendoDatePicker({
            id: "dateSearchEnd",
            format: "yyyy-MM-dd",
            btnStyle: true,
            btnStartId: "dateSearchStart",
            btnEndId: "dateSearchEnd",
            change : function(e) {
                fnEndCalChange("dateSearchStart", "dateSearchEnd");
            }
        });
    },
    /* 체크박스 공통코드 데이터 조회 */
    getCheckBoxCommCd: function(optId, grpCd, useYn, attr1, attr2, attr3){
        fnTagMkChkBox({
            id          : optId,
            tagId       : optId,
            url         : "/admin/comn/getCodeList",
            params      : {"stCommonCodeMasterCode" : grpCd, "useYn" : useYn, "attr1" : attr1, "attr2" : attr2, "attr3" : attr3},
            beforeData  : [{ "CODE" : "ALL", "NAME" : "전체" }],
            async       : false,
        });
    },




    getDropDownCommCd: function(optId, optText, optValue, optBlank, grpCd, useYn, attr1, attr2, attr3){
        fnKendoDropDownList({
            id          : optId,
            url         : "/admin/comn/getCodeList",
            params      : {"stCommonCodeMasterCode" : grpCd, "useYn" : useYn, "attr1" : attr1, "attr2" : attr2, "attr3" : attr3},
            textField   : optText,
            valueField  : optValue,
            blank       : optBlank
        });
    },
    getDropDownUrl: function(optId, tagId, optUrl, optText, optValue, optBlank, optCscdId, optCscdField){
        fnKendoDropDownList({
            id : optId,
            tagId : tagId,
            url : optUrl,
            textField : optText,
            valueField : optValue,
            blank : optBlank,
            async : false,
            cscdId: optCscdId ? optCscdId : undefined,
            cscdField: optCscdField ? optCscdField : undefined,
        });
    },

    getRadioLocalData: function(optId, optNm, chVal, localData, fnChange){
        fnTagMkRadio({
            id    :  optId,
            tagId : optNm,
            chkVal: '',
            data  : localData,
            style : {},
            change: (fnChange != null) ? fnChange : function(){}
        });
    },
    getSellersSupplier: function(optId, optTagId, optBlank){ /* 공급업체 */
       /* fnKendoDropDownList({
            id: optId,
            tagId: optTagId,
            url: "/admin/comn/getDropDownSupplierList",
            textField: "supplierName",
            valueField: "supplierId",
            blank: optBlank,
            async: false,
            cscdId: "erpSendYn",
            cscdField: "erpSendYn",
        });*/

        fnAjax({
            url     : "/admin/comn/getDropDownSupplierList",
            method : "GET",
            params  : {},
            success :
                function( data ){
                    var arrData = [];
                    $.each(data.rows, function(){
                        let that = $(this);
                        let obj = new Object();
                        let supplierId = that[0].supplierId;
                        let supplierName = that[0].supplierName;
                        obj.CODE = supplierId;
                        obj.NAME = supplierName;

                        $("#divSellersSupplierArea").append("<input type='hidden' id='inputSupplierId_"+ supplierId +"' value='"+ that[0].erpSendYn +"'/>");
                        arrData.push(obj);
                    });

                    fnKendoDropDownList({
                        id : optId,
                        data : arrData,
                        tagId : optTagId,
                        blank: optBlank,
                    });

                    // 공급처 선택
                    changeSellersSupplierUtil.init();
                },
            isAction : 'select'
        });

    },
    getDropDownWarehouseStlmnList: function(optId, optTagId, optBlank){ /* 정산용 출고처 조회 */
        fnKendoDropDownList({
            id: optId,
            tagId: optTagId,
            url: "/admin/comn/getDropDownWarehouseStlmnList",
            textField: "warehouseName",
            valueField: "warehouseId",
            blank: optBlank,
            async: false
        });
    }
}