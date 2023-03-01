/*******************************************************************************************************************************************************************************************************
 * ------------------- description : ERP 품목 검색 팝업 @ ------------------- @
 ******************************************************************************************************************************************************************************************************/
'use strict';

var erpItemListGrid, erpItemListGridOpt, erpItemListGridDs;

var popupParameter = parent.POP_PARAM["parameter"]; // 팝업 파라미터 전역변수
var masterItemType = popupParameter['masterItemType']; // 모화면 ( 마스터 품목 등록 ) 의 마스터 품목 유형

$(document).ready(function() {

    fnInitialize(); //Initialize Page Call ---------------------------------

    function fnInitialize() { //Initialize PageR
        $scope.$emit('fnIsMenu', {
            flag : false
        });

        fnPageInfo({
            PG_ID : 'erpItemSearchPopup',
            callback : fnUI
        });

    }

    function fnUI() {

        fnTranslate(); // comm.lang.js 안에 있는 공통함수 다국어 변환------------------

        fnInitButton(); // Initialize Button

        fnInitOptionBox(); // Initialize Option

        fnErpItemListGrid(); // Initialize Grid

    };

    function fnInitButton() {
        $('#fnSearch ,#fnClear').kendoButton();
    };

    function fnInitOptionBox() {

        fnKendoDropDownList({
            id : 'erpSearchItemOption', // EPR 품목 검색 옵션 : ErpItemSearchOptionEnum 참조
            data : [ {
                code : 'SEARCH_BY_CODE',
                name : 'EPR 품목코드'
            }, {
                code : 'SEARCH_BY_BARCODE',
                name : 'EPR 품목바코드'
            },{
                code : 'SEARCH_BY_NAME',
                name : 'EPR 품목명'
            } ],
            valueField : 'code',
            textField : 'name'
        });
    }

    function fnErpItemListGrid() { // ERP 품목 검색 그리드

        erpItemListGridDs = fnGetDataSource({
            url : '/admin/item/master/register/getErpItemPopupList', // ERP 품목 검색 API 호출
        });

        erpItemListGridOpt = {
            dataSource : erpItemListGridDs,
            navigatable : true,
            scrollable : true,
            height : 550,
            columns : [ {
                field : 'erpItemNo',
                title : 'ERP 품목코드',
                width : '10%',
                attributes : {
                    style : 'text-align:center'
                }
            }, {
                field : 'erpItemName',
                title : 'ERP 품목명',
                width : '25%',
                attributes : {
                    style : 'text-align:center'
                }
            }, {
                field : 'bosSupplierName',
                title : '공급업체',
                width : '12%',
                attributes : {
                    style : 'text-align:center'
                }
            }, {
                field : 'erpBrandName',
                title : '브랜드',
                width : '17%',
                attributes : {
                    style : 'text-align:center'
                }
            }, {
                field : 'erpStandardPrice',
                title : '원가',
                width : '15%',
                attributes : {
                    style : 'text-align:center'
                }, format : "{0:n0}"
            },{
                field : 'erpRecommendedPrice',
                title : '정상가',
                width : '15%',
                attributes : {
                    style : 'text-align:center'
                }, format : "{0:n0}"
            },{
                field : 'erpOriginName',
                title : '원산지',
                width : '15%',
                attributes : {
                    style : 'text-align:center'
                },
                hidden : true
            }, {
                field : 'erpDistributionPeriod',
                title : '유통기간',
                width : '8%',
                attributes : {
                    style : 'text-align:center'
                },
                hidden : true,
                template : "#: erpDistributionPeriod # 일", // 유통기간 뒤에 ' 일' 붙임
            }, {
                title : '품목 선택',
                width : '10%',
                attributes : {
                    style : 'text-align:center'
                },
                command : [ {
                    className : "k-grid-customCommand",
                    name : "chooseBtn",
                    text : "  선택  ",
                    click : function(e) {
                        e.preventDefault(); // prevent page scroll position change
                        // e.target is the DOM element representing the button
                        var tr = $(e.target).closest("tr"); // get the current table row (tr)
                        var data = this.dataItem(tr); // get the data bound to the current table row

                        fnClose(data); // 현재 선택한 행의 데이터를 모화면에 전달하고 팝업 닫힘
                    }
                } ]
            },{
                field : 'registerationAvailableMsg',
                title : '선택 불가사유',
                width : '10%',
                attributes : {
                    style : 'text-align:center'
                },
            }

            ]
        };

        erpItemListGrid = $('#erpItemListGrid').initializeKendoGrid(erpItemListGridOpt).cKendoGrid();

        erpItemListGrid.bind('dataBound', function(e) {
            $('#totalCnt').text(erpItemListGridDs._total); // 전체 조회건수 출력

            // 등록가능여부에 따라 "선택" 버튼 활성화 / 비활성화 처리
            var grid = this;

            grid.tbody.find("tr[role='row']").each(function() {
                var data = grid.dataItem(this);

                if (data['isRegistrationAvailable'] == false) { // 등록 가능하지 않은 품목코드인 경우

                    // 연동 품목인 경우 선택 버튼 비활성화 처리
                    $(this).find(".k-grid-customCommand").addClass("k-state-disabled");
                    $(this).find(".k-grid-customCommand").prop('text', '선택 불가');

                }
            });

        });

    };

    function fnClose(params) { // 팝업 닫기 Event Listener

        if (params) {
            parent.POP_PARAM = params;
        }
        parent.LAYER_POPUP_OBJECT.data('kendoWindow').close();
    }

    function fnSearch() { // 조회 함수

        var searchOption = $('#erpSearchItemOption').val();
        var searchValue = $.trim($('#searchValue').val());

        if (!searchValue) {
            return valueCheck("검색할 값을 입력해 주세요.", 'searchValue');
        }

        // 품목명으로 검색시 validation 체크
        if ($('#erpSearchItemOption').val() == 'SEARCH_BY_NAME') {

            if (searchValue.length < 3) {
                return valueCheck("품목명으로 검색시 3자리 이상 입력해야 합니다.", 'searchValue');
            }

        }

        /*
         * ERP 품목 검색 API 상세 명세 확인 후, validation 추가 예정
         */

        fnOpenLoading();

        fnAjax({ // ERP 품목 검색 조회
            url : erpItemListGridDs.transport.options.read.url,
            method : 'GET',
            params : {
                'searchOption' : searchOption,
                'searchValue' : searchValue,
                'masterItemType' : masterItemType,
            },
            isAction : 'select',
            success : function(data, status, xhr) {
                // 이미 등록된 품목코드인 경우 return
                if (data['isRegisterdItemCode'] == true) {

                    fnKendoMessage({
                        type : "confirm",
                        message : "이미 등록된 품목코드입니다.<br>품목 정보를 수정하시겠습니까?",
                        ok : function() {
                            fnClose(data);
                        },
                        cancel : function() {
                            fnClear();
                        }
                    });

                }

                if (!data['erpItemApiList'] || data['erpItemApiList'].length == 0) { // 조회된 데이터 없는 경우 empty list 출력
                    erpItemListGridDs.data([]); // empty list 출력
                }

                if (data['erpItemApiList']) {

                    erpItemListGridDs.data(data['erpItemApiList']); // 그리드에 조회된 데이터 출력

                }

            },
            error : function(xhr, status, strError) {
                fnKendoMessage({
                    message : xhr.responseText
                });
            },

        });

    }

    function fnClear() { // 초기화 함수

        erpItemListGridDs.data([]);
        $("#erpSearchItemOption").data("kendoDropDownList").value('SEARCH_BY_CODE');
        $('#searchValue').val('')

    }

    function valueCheck(nullMsg, id) { // 메시지 팝업 호출 함수
        fnKendoMessage({
            message : nullMsg,
            ok : function focusValue() {
                $('#' + id).focus();
            }
        });

        return false;
    };

    // ------------------------------- Html 버튼 바인딩 Start
    // -------------------------------
    $scope.fnSearch = function() { // 조회 버튼 Event
        fnSearch();
    };

    $scope.fnClear = function() { // 초기화 버튼 Event
        fnClear();
    };

    $scope.fnKeypress = function() { // 엔터키 이벤트
        if (window.event.keyCode == 13) {
            fnSearch();
        }
    }

    // ------------------------------- Html 버튼 바인딩 End
    // -------------------------------

}); // document ready - END