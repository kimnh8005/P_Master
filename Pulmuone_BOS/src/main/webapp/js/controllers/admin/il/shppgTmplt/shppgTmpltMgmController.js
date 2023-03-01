﻿/**-----------------------------------------------------------------------------
 * system            : 배송정책 팝업
 * @
 * @ 수정일           수정자          수정내용
 * @ ------------------------------------------------------
 * @ 2020.07.10       손진구          최초생성
 * @ 2020.08.07       손진구          설계변경에 따라 전체수정
 * @
 * **/
"use strict";

var paramData = parent.POP_PARAM["parameter"]; // 파라미터
var viewModel; // viewModel

$(document).ready(function () {

    fnInitialize();	//Initialize Page Call ---------------------------------

    //Initialize PageR
    function fnInitialize() {
        $scope.$emit("fnIsMenu", {flag: false});

        fnPageInfo({
            PG_ID: "shppgTmpltMgm",
            callback: fnUI
        });
    };

    // 화면 UI 초기화
    function fnUI() {

        fnInitButton();	// Initialize Button  ---------------------------------
        fnInitOptionBox(); // Initialize Option Box ------------------------------------
        fnViewModelInit();
        fnInitGrid();
        fnDefaultValue();
    };

    //--------------------------------- Button Start---------------------------------

    // 버튼 초기화
    function fnInitButton() {
        $("#fnSave").kendoButton();
    };

    // 팝업 닫기
    function fnClose() {
        if (viewModel.newCreate) {
            parent.LAYER_POPUP_OBJECT.data("kendoWindow").close();
        } else {
            fnKendoMessage({
                type: "confirm",
                message: "저장 후 변경 된 정보가 반영 됩니다.",
                ok: function () {
                    parent.LAYER_POPUP_OBJECT.data("kendoWindow").close();
                },
                cancel: function () {
                }
            });
        }
    };

    // 저장
    function fnSave() {
        let paramData = $("#inputForm").formSerialize(true);

        if (paramData.rtnValid) {
            if (fnSaveValid()) {
                let shippingWarehouseList = viewModel.shippingWarehouseList.data();
                let basicYnShippingWarehouseList = shippingWarehouseList.filter(function (warehouseInfo) {
                    return warehouseInfo.basicYnCheck;
                });

                if (Object.keys(basicYnShippingWarehouseList).length > 0) {
                    fnShippingWarehouseBasicYnCheck(basicYnShippingWarehouseList);
                } else {
                    fnCreateAndUpdate();
                }
            }
        }
    };

    // 배송정책 출고처 기본여부 체크
    function fnShippingWarehouseBasicYnCheck(basicYnShippingWarehouseList) {
        let paramData = {};
        paramData = viewModel.get("shippingTemplateInfo");
        paramData.shippingWarehouseList = basicYnShippingWarehouseList;

        fnAjax({
            url: "/admin/policy/shippingtemplate/getShippingWarehouseBasicYnCheck",
            params: paramData,
            contentType: "application/json",
            success: function (data) {
                if (data.otherShippingWarehouseBasicYn) {
                    fnKendoMessage({
                        type: "confirm",
                        message: "해당 출고처 정보에 저장된 기본배송정책이 존재합니다. 수정하시겠습니까?",
                        ok: function () {
                            fnCreateAndUpdate();
                        },
                        cancel: function () {
                        }
                    });
                } else {
                    fnCreateAndUpdate();
                }
            },
            error: function (xhr, status, strError) {
                fnKendoMessage({message: xhr.responseText});
            },
            isAction: "select"
        });
    };

    // 배송정책 등록 and 수정
    function fnCreateAndUpdate() {
        let url = "";
        let paramData = {};

        if (viewModel.newCreate) {
            url = "/admin/policy/shippingtemplate/addShippingTemplate";
        } else {
            url = "/admin/policy/shippingtemplate/putShippingTemplate";
        }

        paramData = viewModel.get("shippingTemplateInfo");
        paramData.shippingWarehouseList = viewModel.shippingWarehouseList.data();
        paramData.shippingConditionList = viewModel.shippingConditionList.data();

        fnAjax({
            url: url,
            params: paramData,
            contentType: "application/json",
            success: function (data) {
                if (viewModel.get("newCreate")) {
                    fnKendoMessage({
                        message: "등록되었습니다."
                        , ok: function () {
                            parent.LAYER_POPUP_OBJECT.data("kendoWindow").close();
                        }
                    });
                } else {
                    fnKendoMessage({
                        message: "수정되었습니다."
                        , ok: function () {
                            parent.LAYER_POPUP_OBJECT.data("kendoWindow").close();
                        }
                    });
                }
            },
            error: function (xhr, status, strError) {
                fnKendoMessage({message: xhr.responseText});
            },
            isAction: "insert"
        });
    };

    //--------------------------------- Button End---------------------------------

    //------------------------------- Grid Start -------------------------------
    // 그리드 초기화
    function fnInitGrid() {

    };
    //------------------------------- Grid End -------------------------------

    //---------------Initialize Option Box Start ------------------------------------------------

    // 옵션 초기화
    function fnInitOptionBox() {

        //조건 배송비 구분
        fnKendoDropDownList({
            id: "conditionTypeCode",
            url: "/admin/comn/getCodeList",
            params: {"stCommonCodeMasterCode": "CONDITION_TYPE", "useYn": "Y"},
            async: false
        });

        // 합배송 유무
        fnKendoDropDownList({
            id: "bundleYn",
            data: [{"CODE": "Y", "NAME": "합배송"}
                , {"CODE": "N", "NAME": "합배송제외"}]
        });

        // 지역별 배송비
        fnTagMkRadio({
            id: "areaShippingUseYn",
            data: [{"CODE": "N", "NAME": "미사용"}
                , {"CODE": "Y", "NAME": "사용"}],
            tagId: "areaShippingUseYn",
            chkVal: "NO",
            style: {}
        });

        // 지역별 배송비 라디오 이벤트 연결
        $("input:radio[name=areaShippingUseYn]").attr("data-bind", "checked: shippingTemplateInfo.areaShippingYn, events: {change: fnAreaShippingUseYnChange}");
    };

    //---------------Initialize Option Box End ------------------------------------------------


    //-------------------------------  Common Function start -------------------------------

    // viewModel 초기화
    function fnViewModelInit() {
        viewModel = new kendo.data.ObservableObject({
            shippingTemplateInfo: { // 배송비 탬플릿
                shippingTemplateId: null, // 배송정책 ID
                originalShippingTemplateId: 0, // 원본 배송정책 ID
                shippingTemplateName: "", // 배송정책명
                conditionTypeCode: "CONDITION_TYPE.3", // 조건배송비 구분코드 (기본값 : 결제금액별 배송비)
                shippingPrice: 0, // 기본 배송비
                claimShippingPrice: 0, // 클레임 배송비
                bundleYn: "Y", // 합배송 여부 (기본값 : 합배송)
                paymentMethodType: "PAYMENT_METHOD_TYPE.1", // 선/착불 구분 (기본값 : 선불)
                jejuShippingPrice: 0, // 제주 추가 배송비
                islandShippingPrice: 0, // 도서산간 추가 배송비
                undeliverableAreaTp: "UNDELIVERABLE_AREA_TP.NONE", //배송불가지역
                jejuShppingPriceCheckBox: false, //2권역 체크박스 check
                islandShppingPriceCheckBox: false //1권역 체크박스 check
            },
            shippingWarehouseList: new kendo.data.DataSource({ // 출고처 리스트
                schema: {
                    model: {
                        id: "warehouseId",
                        fields: {
                            shippingTemplateId: {editable: false},
                            shippingWarehouseId: {editable: false},
                            warehouseId: {type: "number", editable: false, validation: {required: true}},
                            warehouseName: {editable: false, validation: {required: true}},
                            basicYnCheck: {type: "boolean"}
                        }
                    }
                }
            }),
            shippingConditionList: new kendo.data.DataSource({ // 조건 리스트
                schema: {
                    model: {
                        id: "conditionValue",
                        fields: {
                            shippingTemplateId: {editable: false},
                            shippingConditionId: {editable: false},
                            shippingPrice: {type: "number", validation: {required: true, min: 0, maxlength: 5}},
                            conditionValue: {type: "number", validation: {required: true, min: 0, maxlength: 5}}
                        }
                    }
                }
            }),
            newCreate: true, // 신규 여부
            shippingConditionGridVisible: true, // 조건 배송비 Grid Visible
            bundleYnDisabled: false, // 합배송 DropDown Disabled
            islandShippingPriceDisabled: true, // island 배송비 입력 Disabled
            islandShippingCheckDisabled: false, // island 체크박스 입력 Disabled
            jejuShippingPriceDisabled: true, // jeju 배송비 입력 Disabled
            jejuShippingCheckDisabled: false, // jeju 체크박스 입력 Disabled
            areaShippingUseYnCheckedValue: "N", // 지역별 배송비 사용유무 Value
            UNDELIVERABLE_AREA_TP: {"NONE":"", "A1":"", "A2":"", "A1_A2":""},
            fnWarehouseSearchPopup: function () { // 출고처 검색 팝업
                fnKendoPopup({
                    id: "warehousePopup",
                    title: "출고처 검색",
                    src: "#/warehousePopup",
                    param: {"supplierCode": ""},
                    width: "760px",
                    height: "585px",
                    success: function (id, data) {
                        if (data.urWarehouseId) {
                            viewModel.fnWarehouseAdd(data);
                        }
                    }
                });
            },
            fnWarehouseAdd: function (dataItem) { // 출고처 추가
                let shippingWarehouseList = viewModel.shippingWarehouseList.data();

                let duplWarehouseId = shippingWarehouseList.filter(function (warehouseInfo) {
                    return warehouseInfo.warehouseId == dataItem.urWarehouseId;
                });

                if (Object.keys(duplWarehouseId).length > 0) {
                    fnKendoMessage({message: "동일한 출고처가 있습니다."});
                    return;
                }

                viewModel.shippingWarehouseList.add({
                    shippingTemplateId: null
                    , shippingWarehouseId: null
                    , warehouseId: dataItem.urWarehouseId
                    , warehouseName: dataItem.warehouseName
                    , basicYnCheck: false
                });
            },
            fnConditionTypeChange: function () { // 조건배송비 구분 변경
                let shippingConditionGrid = $("#shippingConditionGrid").data("kendoGrid");

                viewModel.shippingConditionList.data([{
                    shippingTemplateId: null,
                    shippingConditionId: null,
                    shippingPrice: 0,
                    conditionValue: 0
                }]);

                viewModel.fnConditionTypeViewChange();

                if (viewModel.shippingTemplateInfo.conditionTypeCode == "CONDITION_TYPE.5") {
                    viewModel.shippingTemplateInfo.set("bundleYn", "N");
                }
            },
            fnConditionTypeViewChange: function () { // 조건배송비 화면 제어
                let conditionTypeCodeValue = viewModel.shippingTemplateInfo.conditionTypeCode;
                let shippingConditionGrid = $("#shippingConditionGrid").data("kendoGrid");

                if (conditionTypeCodeValue == "CONDITION_TYPE.1") { // 무료배송비

                    viewModel.set("shippingConditionGridVisible", false);
                    viewModel.set("bundleYnDisabled", false);
                } else if (conditionTypeCodeValue == "CONDITION_TYPE.2") { // 고정배송비

                    viewModel.set("shippingConditionGridVisible", true);
                    shippingConditionGrid.hideColumn("conditionValue");
                    viewModel.set("bundleYnDisabled", false);
                } else if (conditionTypeCodeValue == "CONDITION_TYPE.3") { // 결제금액별배송비

                    viewModel.set("shippingConditionGridVisible", true);
                    shippingConditionGrid.showColumn("conditionValue");
                    viewModel.set("bundleYnDisabled", false);
                } else if (conditionTypeCodeValue == "CONDITION_TYPE.5") { // 상품1개단위별배송비

                    viewModel.set("shippingConditionGridVisible", true);
                    shippingConditionGrid.hideColumn("conditionValue");
                    viewModel.set("bundleYnDisabled", true);
                }
            },
            fnAreaShippingUseYnChange: function () { // 지역별 배송비 사용유무 변경

                if (viewModel.shippingTemplateInfo.areaShippingYn == "N") {

                    viewModel.shippingTemplateInfo.set("areaShippingDeliveryYn", "N");  //삭제예정

                    viewModel.shippingTemplateInfo.set("areaShippingYn", "N");
                    viewModel.shippingTemplateInfo.set("jejuShippingPrice", 0);
                    viewModel.shippingTemplateInfo.set("islandShippingPrice", 0);
                    viewModel.fnSetUndeliverableArea(); //배송 불가지역 설정

                } else if (viewModel.shippingTemplateInfo.areaShippingYn == "Y") {

                    viewModel.shippingTemplateInfo.set("areaShippingDeliveryYn", "Y"); //삭제예정

                    viewModel.shippingTemplateInfo.set("areaShippingYn", "Y");
                    viewModel.fnSetUndeliverableArea(); //배송 불가지역 설정

                }

            },
            fnSetUndeliverableArea: function () {  //배송불가 초기 조회용
                let shippingYn = viewModel.shippingTemplateInfo.areaShippingYn;
                let currentUnableAreaTp = viewModel.shippingTemplateInfo.undeliverableAreaTp;

                if (shippingYn == "Y" && currentUnableAreaTp == viewModel.UNDELIVERABLE_AREA_TP.NONE) {
                    viewModel.set("islandShippingPriceDisabled", false);
                    viewModel.set("jejuShippingPriceDisabled", false);
                    viewModel.shippingTemplateInfo.set("islandShppingPriceCheckBox", false); //체크박스island
                    viewModel.shippingTemplateInfo.set("jejuShppingPriceCheckBox", false); //체크박스jeju
                } else if (shippingYn == "Y" && currentUnableAreaTp == viewModel.UNDELIVERABLE_AREA_TP.A1) {
                    viewModel.set("islandShippingPriceDisabled", true);
                    viewModel.set("jejuShippingPriceDisabled", false);
                    viewModel.shippingTemplateInfo.set("islandShppingPriceCheckBox", true); //체크박스island
                    viewModel.shippingTemplateInfo.set("jejuShppingPriceCheckBox", false); //체크박스jeju
                } else if (shippingYn == "Y" && currentUnableAreaTp == viewModel.UNDELIVERABLE_AREA_TP.A2) {
                    viewModel.set("islandShippingPriceDisabled", false);
                    viewModel.set("jejuShippingPriceDisabled", true);
                    viewModel.shippingTemplateInfo.set("islandShppingPriceCheckBox", false); //체크박스island
                    viewModel.shippingTemplateInfo.set("jejuShppingPriceCheckBox", true); //체크박스jeju
                } else if (shippingYn == "Y" && currentUnableAreaTp == viewModel.UNDELIVERABLE_AREA_TP.A1_A2) {
                    viewModel.set("islandShippingPriceDisabled", true);
                    viewModel.set("jejuShippingPriceDisabled", true);
                    viewModel.shippingTemplateInfo.set("islandShppingPriceCheckBox", true); //체크박스island
                    viewModel.shippingTemplateInfo.set("jejuShppingPriceCheckBox", true); //체크박스jeju
                } else if (shippingYn == "N" && currentUnableAreaTp == viewModel.UNDELIVERABLE_AREA_TP.NONE) {
                    viewModel.set("islandShippingPriceDisabled", true);
                    viewModel.set("jejuShippingPriceDisabled", true);
                    viewModel.shippingTemplateInfo.set("islandShppingPriceCheckBox", false); //체크박스island
                    viewModel.shippingTemplateInfo.set("jejuShppingPriceCheckBox", false); //체크박스jeju
                } else if (shippingYn == "N" && currentUnableAreaTp == viewModel.UNDELIVERABLE_AREA_TP.A1) {
                    viewModel.set("islandShippingPriceDisabled", true);
                    viewModel.set("jejuShippingPriceDisabled", true);
                    viewModel.shippingTemplateInfo.set("islandShppingPriceCheckBox", true); //체크박스island
                    viewModel.shippingTemplateInfo.set("jejuShppingPriceCheckBox", false); //체크박스jeju
                } else if (shippingYn == "N" && currentUnableAreaTp == viewModel.UNDELIVERABLE_AREA_TP.A2) {
                    viewModel.set("islandShippingPriceDisabled", true);
                    viewModel.set("jejuShippingPriceDisabled", true);
                    viewModel.shippingTemplateInfo.set("islandShppingPriceCheckBox", false); //체크박스island
                    viewModel.shippingTemplateInfo.set("jejuShppingPriceCheckBox", true); //체크박스jeju
                } else if (shippingYn == "N" && currentUnableAreaTp == viewModel.UNDELIVERABLE_AREA_TP.A1_A2) {
                    viewModel.set("islandShippingPriceDisabled", true);
                    viewModel.set("jejuShippingPriceDisabled", true);
                    viewModel.shippingTemplateInfo.set("islandShppingPriceCheckBox", true); //체크박스island
                    viewModel.shippingTemplateInfo.set("jejuShppingPriceCheckBox", true); //체크박스jeju
                }
            },
            fnSetUndelivreableAreaTp: function (checkUnableAreaTp) { //배송불가 버튼 클릭용
                let currentUnableAreaTp = viewModel.shippingTemplateInfo.undeliverableAreaTp;

                if (currentUnableAreaTp == viewModel.UNDELIVERABLE_AREA_TP.NONE) {
                    viewModel.shippingTemplateInfo.set("undeliverableAreaTp", checkUnableAreaTp); //배송불가지역

                    if (checkUnableAreaTp == viewModel.UNDELIVERABLE_AREA_TP.A1) {
                        viewModel.shippingTemplateInfo.set("islandShppingPriceCheckBox", true);
                    } else if (checkUnableAreaTp == viewModel.UNDELIVERABLE_AREA_TP.A2) {
                        viewModel.shippingTemplateInfo.set("jejuShppingPriceCheckBox", true);
                    } else if (checkUnableAreaTp == viewModel.UNDELIVERABLE_AREA_TP.A1_A2) {
                        viewModel.shippingTemplateInfo.set("jejuShppingPriceCheckBox", true);
                        viewModel.shippingTemplateInfo.set("islandShppingPriceCheckBox", true);
                    }

                } else if (currentUnableAreaTp == viewModel.UNDELIVERABLE_AREA_TP.A1 && checkUnableAreaTp == viewModel.UNDELIVERABLE_AREA_TP.A2) {
                    viewModel.shippingTemplateInfo.set("jejuShppingPriceCheckBox", true);
                    viewModel.shippingTemplateInfo.set("islandShppingPriceCheckBox", true);
                    viewModel.shippingTemplateInfo.set("undeliverableAreaTp", viewModel.UNDELIVERABLE_AREA_TP.A1_A2);
                } else if (currentUnableAreaTp == viewModel.UNDELIVERABLE_AREA_TP.A1 && checkUnableAreaTp == viewModel.UNDELIVERABLE_AREA_TP.A1) {
                    viewModel.shippingTemplateInfo.set("jejuShppingPriceCheckBox", false);
                    viewModel.shippingTemplateInfo.set("islandShppingPriceCheckBox", false);
                    viewModel.shippingTemplateInfo.set("undeliverableAreaTp", viewModel.UNDELIVERABLE_AREA_TP.NONE);
                } else if (currentUnableAreaTp == viewModel.UNDELIVERABLE_AREA_TP.A2 && checkUnableAreaTp == viewModel.UNDELIVERABLE_AREA_TP.A1) {
                    viewModel.shippingTemplateInfo.set("jejuShppingPriceCheckBox", true);
                    viewModel.shippingTemplateInfo.set("islandShppingPriceCheckBox", true);
                    viewModel.shippingTemplateInfo.set("undeliverableAreaTp", viewModel.UNDELIVERABLE_AREA_TP.A1_A2);
                } else if (currentUnableAreaTp == viewModel.UNDELIVERABLE_AREA_TP.A2 && checkUnableAreaTp == viewModel.UNDELIVERABLE_AREA_TP.A2) {
                    viewModel.shippingTemplateInfo.set("jejuShppingPriceCheckBox", false);
                    viewModel.shippingTemplateInfo.set("islandShppingPriceCheckBox", false);
                    viewModel.shippingTemplateInfo.set("undeliverableAreaTp", viewModel.UNDELIVERABLE_AREA_TP.NONE);
                } else if (currentUnableAreaTp == viewModel.UNDELIVERABLE_AREA_TP.A1_A2 && checkUnableAreaTp == viewModel.UNDELIVERABLE_AREA_TP.A1) {
                    viewModel.shippingTemplateInfo.set("jejuShppingPriceCheckBox", true);
                    viewModel.shippingTemplateInfo.set("undeliverableAreaTp", viewModel.UNDELIVERABLE_AREA_TP.A2);
                } else if (currentUnableAreaTp == viewModel.UNDELIVERABLE_AREA_TP.A1_A2 && checkUnableAreaTp == viewModel.UNDELIVERABLE_AREA_TP.A2) {
                    viewModel.shippingTemplateInfo.set("islandShppingPriceCheckBox", true);
                    viewModel.shippingTemplateInfo.set("undeliverableAreaTp", viewModel.UNDELIVERABLE_AREA_TP.A1);
                }
            },
            fnGoodsShippingTemplateCheck: function (dataItem) { // 상품 배송비 관계 DB 출고처 체크
                let paramData = {};

                paramData.originalShippingTemplateId = viewModel.shippingTemplateInfo.originalShippingTemplateId;
                paramData.warehouseId = dataItem.warehouseId;

                fnAjax({
                    url: "/admin/policy/shippingtemplate/getGoodsShippingTemplateYn",
                    params: paramData,
                    contentType: "application/json",
                    success: function (data) {

                        if (data.goodsShippingTemplateUseYn) {
                            fnKendoMessage({message: "연결된 상품이 있습니다. 상품 배송정책 변경 후 해제 가능합니다."});
                        } else {
                            viewModel.shippingWarehouseList.remove(dataItem);
                        }
                    },
                    isAction: "select"
                });

            },
            fnGetShippingTemplateInfo: function (dataItem) { // 배송정책 상세정보 조회
                fnAjax({
                    url: "/admin/policy/shippingtemplate/getShippingTemplate",
                    params: dataItem,
                    contentType: "application/json",
                    success: function (data) {

                        viewModel.set("shippingTemplateInfo", data.shippingTemplateInfo);
                        viewModel.shippingWarehouseList.data(data.shippingWarehouseList);
                        viewModel.shippingConditionList.data(data.shippingConditionList);
                        viewModel.fnConditionTypeViewChange();
                        viewModel.fnAreaShippingUseYnChange();
                    },
                    isAction: "select"
                });
            },
            fnNumericEditor: function (container, options) { // 그리드 EDIT 숫자 입력 제어
                $('<input data-bind="value:' + options.field + '"/>')
                    .appendTo(container)
                    .kendoNumericTextBox({
                        decimals: 0,
                        min: 0,
                        maxlength: 5,
                        restrictDecimals: true,
                        spinners: false,
                        format: "n0"
                    });
            },
            fnGetUndeliverableAreaTp : function(){ // 승인상태 공통코드 조회
                fnAjax({
                    url	 : "/admin/comn/getCodeList",
                    method : "GET",
                    async : false,
                    params  : {"stCommonCodeMasterCode" : "UNDELIVERABLE_AREA_TP", "useYn" : "Y"},
                    success : function( data ){
                        viewModel.set("UNDELIVERABLE_AREA_TP.NONE", data.rows[0].CODE);
                        viewModel.set("UNDELIVERABLE_AREA_TP.A1", data.rows[1].CODE);
                        viewModel.set("UNDELIVERABLE_AREA_TP.A2", data.rows[2].CODE);
                        viewModel.set("UNDELIVERABLE_AREA_TP.A1_A2", data.rows[3].CODE);
                    },
                    error : function(xhr, status, strError){
                        fnKendoMessage({ message : xhr.responseText });
                    }
                });
            }
        });

        kendo.bind($("#inputForm"), viewModel);
    };

    // 기본값 셋팅
    function fnDefaultValue() {
        if (!paramData.shippingTemplateId) {

            viewModel.set("newCreate", true);
            viewModel.fnGetUndeliverableAreaTp();
            viewModel.fnConditionTypeChange();
            viewModel.fnAreaShippingUseYnChange();
        } else {

            viewModel.set("newCreate", false);
            viewModel.fnGetUndeliverableAreaTp();
            viewModel.fnGetShippingTemplateInfo(paramData);
        }
    };

    // 입력값 검증
    function fnSaveValid() {

        if (viewModel.shippingWarehouseList.total() == 0) {
            fnKendoMessage({message: "출고처는 필수 입니다."});
            return false;
        }

        if (viewModel.shippingTemplateInfo.conditionTypeCode != "CONDITION_TYPE.1") {
            let shippingConditionList = viewModel.shippingConditionList.data();
            let lessShippingPrice = shippingConditionList.filter(function (shippingConditionInfo) {
                return shippingConditionInfo.shippingPrice < 10;
            });

            if (Object.keys(lessShippingPrice).length > 0) {
                fnKendoMessage({message: "배송비는 최소 10원 이상 입력해 주시기 바랍니다."});
                return false;
            }

        }

        return true;
    };

    function fnIslandShppingPriceUseYnChange(checked) {
        viewModel.fnSetUndelivreableAreaTp(viewModel.UNDELIVERABLE_AREA_TP.A1);

        if (viewModel.shippingTemplateInfo.areaShippingYn == "Y" && checked == true) {
            viewModel.set("islandShippingPriceDisabled", true);
            viewModel.shippingTemplateInfo.set("islandShippingPrice", 0);
        } if (viewModel.shippingTemplateInfo.areaShippingYn == "Y" && checked == false) {
            viewModel.set("islandShippingPriceDisabled", false);
        }

    };

    function fnJejuShppingPriceUseYnChange(checked) {
        viewModel.fnSetUndelivreableAreaTp(viewModel.UNDELIVERABLE_AREA_TP.A2);

        if (viewModel.shippingTemplateInfo.areaShippingYn == "Y" && checked == true) {
            viewModel.set("jejuShippingPriceDisabled", true);
            viewModel.shippingTemplateInfo.set("jejuShippingPrice", 0);
        } if (viewModel.shippingTemplateInfo.areaShippingYn == "Y" && checked == false) {
            viewModel.set("jejuShippingPriceDisabled", false);
        }

    };

    //-------------------------------  Common Function end -------------------------------


    //------------------------------- Html 버튼 바인딩  Start -------------------------------

    $scope.fnSave = function () {
        fnSave();
    }; // 저장
    $scope.fnClose = function () {
        fnClose();
    }; // 닫기
    $scope.fnIslandShppingPriceUseYnChange = function(checked) {
        fnIslandShppingPriceUseYnChange(checked);
    }
    $scope.fnJejuShppingPriceUseYnChange = function(checked) {
        fnJejuShppingPriceUseYnChange(checked);
    }

    //------------------------------- Html 버튼 바인딩  End -------------------------------

}); // document ready - END
