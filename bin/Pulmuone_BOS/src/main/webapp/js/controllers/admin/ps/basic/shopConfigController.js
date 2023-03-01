/**-----------------------------------------------------------------------------
 * description         : 상점설정
 * @
 * @ 수정일            수정자          수정내용
 * @ ------------------------------------------------------
 * @ 2017.01.13        최봉석          최초생성
 * @ 2020.10.30        최성현          리팩토링
 * **/
'use strict';

var PAGE_SIZE = 20;
var aGridDs, aGridOpt, aGrid;
$(document).ready(function () {
    fnInitialize();	//Initialize Page Call ---------------------------------

    //Initialize PageR
    function fnInitialize() {
        $scope.$emit('fnIsMenu', {flag: 'true'});
        fnPageInfo({
            PG_ID: 'shopConfig',
            callback: fnUI
        });
    }
    // 화면 UI 초기화
    function fnUI() {

        fnInitButton();
        fnInitGrid();
        fnInitOptionBox();
        fnSearch();

    }

    //--------------------------------- Button Start---------------------------------

    // 버튼 초기화
    function fnInitButton() {
        $('#fnSearch, #fnNew,#fnSave,  #fnClear').kendoButton();
    }

    //--------------------------------- Button End---------------------------------

    //------------------------------- Grid Start -------------------------------
    // 그리드 초기화

    function fnSearch() {
        var data = $('#searchForm').formSerialize(true);
        var query = {
            page: 1,
            pageSize: PAGE_SIZE,
            filterLength: fnSearchData(data).length,
            filter: {
                filters: fnSearchData(data)
            }
        };
        aGridDs.query(query);
    }

    function fnClear() {
        $('#searchForm').formClear(true);
    }

    function fnNew() {
        aGrid.clearSelection();
        $('#inputForm').formClear(true);
        inputFocus();
    }

    function fnSave() {
        var changeCnt = 0;
        var insertData = fnEGridDsExtract('aGrid', 'insert');
        var insertChkData = fnEGridDsExtract('aGrid', 'insert');
        var updateData = fnEGridDsExtract('aGrid', 'update');
        var deleteData = fnEGridDsExtract('aGrid', 'delete');
        changeCnt = insertData.length + updateData.length + deleteData.length;

        for (let i = 0; i < insertData.length; i++) {
            if (insertData[i].psGroupTypeDesc == "" || insertData[i].psName == "" || insertData[i].psKey == "" || insertData[i].psValue == "") {
                fnKendoMessage({message: "필수 데이터를 입력해주세요."});
                return;
            }
        }

        for (let i = 0; i < updateData.length; i++) {
            if (updateData[i].psGroupTypeDesc == "" || updateData[i].psName == "" || updateData[i].psKey == "" || updateData[i].psValue == "") {
                fnKendoMessage({message: "필수 데이터를 입력해주세요."});
                return;
            }
        }

        if (changeCnt == 0) {
            fnKendoMessage({message: fnGetLangData({key: "4355", nullMsg: '데이터 변경사항이 없습니다.'})});
            return;
        } else {
            var data = {
                "insertData": kendo.stringify(insertData)
                , "updateData": kendo.stringify(updateData)
                , "deleteData": kendo.stringify(deleteData)
            };
            fnAjax({
                url: '/admin/system/shopconfig/saveShopConfig',
                params: data,
                success:
                    function (data) {
                        fnBizCallback("save", data);
                    },
                isAction: 'batch'
            });
        }
    }

    function fnClose() {

    }

    //--------------------------------- Button End---------------------------------
    //------------------------------- Grid Start -------------------------------
    function fnInitGrid() {
        aGridDs = fnGetEditPagingDataSource({
            url: '/admin/system/shopconfig/getShopConfigList',
            pageSize: PAGE_SIZE,
            model_id: 'psConfigId',
            model_fields: {
                shopName: {editable: true, type: 'string', validation: {required: true}}
                ,
                psGroupTypeDesc: {
                    editable: true, type: 'string', validation: {
                        required: true,
                        stringValidation(input) {
                            console.log(input)
                            // input이 .k-edit-cell 또는 #psGrpDropDown input가 되는 두가지의 경우가 있습니다.
                            if (input.is("#psGrpDropDown") && input.val() == "") {
                                const $editCell = input.closest("td.k-edit-cell");
                                if ($editCell.length > 0) {
                                    // tooltip box가 dropdownlist span 영역 안에 들어가서 보이지 않음
                                    // td의 overflow 속성을 visible로 바꾸니 해결되었습니다.
                                    // 따라서 동적으로 overflow : visible로 바꿔주고 변경 성공시 hidden으로 만들었습니다.
                                    $editCell.css("overflow", "visible");
                                }
                                input.attr("data-stringValidation-msg", '이 영역을 선택해주세요');
                                return false;
                            }
                            input.closest('.k-edit-cell').css("overflow", "hidden");
                            return true;
                        }
                    }
                }
                ,
                psName: {editable: true, type: 'string', validation: {required: true, maxLength: "20"}}
                ,
                psKey: {
                    editable: true, type: 'string', validation: {
                        required: true
                        , stringValidation: function (input) {
                            if (input.is("[name='psKey']") && input.val() != "") {
                                input.attr("data-stringValidation-msg", '™영문대문자, 숫자, 언더스코어"_"만 등록하실 수 있습니다');
                                return !validateFormat.typeNotUpperEnglishNumberUnderBar.test(input.val());
                            }
                            return true;
                        }
                    }
                }
                ,
                psValue: {editable: true, type: 'string', validation: {required: true, maxLength: "20"}}
                ,
                useYn: {editable: false, type: 'string', validation: {required: false}, defaultValue: 'N'}
                ,
                comment: {
                    editable: true,
                    type: 'stirng',
                    validation: {required: false, maxLength: "255"},
                    defaultValue: ''
                }
            }
        });

        aGridOpt = {
            dataSource: aGridDs,
            toolbar   :
                        fnIsProgramAuth("ADD") && !fnIsProgramAuth("SAVE_DELETE") ? [{ name: 'create', text: '신규', className: "btn-point btn-s"},
                                { name: 'cancel', text: '실행 취소', className: "btn-white btn-s" }]:
                            fnIsProgramAuth("SAVE_DELETE") && !fnIsProgramAuth("ADD") ? [{ name: 'cSave',  text: '저장', imageClass: "k-i-update", className: 'k-custom-save btn-point btn-s'},
                                    { name: 'cancel', text: '실행 취소', className: "btn-white btn-s" }]:
                                fnIsProgramAuth("ADD") && fnIsProgramAuth("SAVE_DELETE") ? [{ name: 'create', text: '신규', className: "btn-point btn-s"},
                                        { name: 'cSave',  text: '저장', imageClass: "k-i-update", className: 'k-custom-save btn-point btn-s'},
                                        { name: 'cancel', text: '실행 취소', className: "btn-white btn-s" }]:
                                    [{ name: 'cancel', text: '실행 취소', className: "btn-white btn-s" }],
            pageable: {
                refresh: true
                , pageSizes: [20, 30, 50]
                , buttonCount: 10
            },
            editable: {
                confirmation: function (model) {
                    return model.psName + ' 삭제하겠습니까?'
                }
            },
            //height: 550,
            columns: [
                {
                    title: 'No',
                    width: '40px',
                    attributes: {style: 'text-align:center'},
                    template: function (dataItem) {
                        return fnKendoGridPagenation(aGrid.dataSource, dataItem);
                    }
                }
                , {
                    field: 'psGroupTypeDesc',
                    title: '정책그룹',
                    width: '75px',
                    attributes: {style: 'text-align:center'/*, class:'forbiz-cell-readonly' */}
                    ,
                    editor: function (container, options) {
                        var input = "";
                        if (options.model.useYn == 'Y') {
                            var input = $("<input id='psGrpDropDown' />");
                        } else {
                            var input = $("<input id='psGrpDropDown' />");
                        }
                        input.appendTo(container);
                        fnKendoDropDownList({
                            id: 'psGrpDropDown',
                            url: "/admin/comn/getCodeList",
                            params: {"stCommonCodeMasterCode": "7"},
                            blank: "선택",
                            /* HGRM-2013 - dgyoun : selectbox 수정 선택 시 기존 값 선택  Start */
                            // Databound : 렌더링 된 후 발생하는 이벤트
                            dataBound: function (e) {
                                // 현재 선택된 값을 선택된 열에서 찾는다
                                const FIELD_NAME = "psGroupTypeDesc";
                                const _currentVal = aGrid.dataItem(aGrid.select())[FIELD_NAME];
                                const self = e.sender;
                                const data = e.sender.dataSource.data();

                                if (!data.length) {
                                    return;
                                }

                                //드롭다운 리스트에서 선택된 값과 같은 값을 가진 데이터의 index값을 찾고,
                                //드롭다운 리스트의 선택된 인덱스를 변경한다.
                                data.forEach(function (d, index) {
                                    if (d.NAME === _currentVal) {
                                        self.select(index + 1);
                                    }
                                })
                            },
                            /* HGRM-2013 - dgyoun : selectbox 수정 선택 시 기존 값 선택  End */

                        });

                        $('#psGrpDropDown').unbind('change').on('change', function () {
                            var dataItem = aGrid.dataItem($(this).closest('tr'));
                            var shopDropDownList = $('#psGrpDropDown').data('kendoDropDownList');
                            dataItem.set('psGroupType', shopDropDownList.value());
                            dataItem.set('psGroupTypeDesc', shopDropDownList.text());
                        });
                    }
                }
                , {field: 'psName', title: '정책명', width: '160px', attributes: {style: 'text-align:center'}}
                , {field: 'psKey', title: '정책키', width: '200px', attributes: {style: 'text-align:center'}}
                , {field: 'psValue', title: '정책값', width: '150px', attributes: {style: 'text-align:center'}}

                , {
                    field: 'useYn',
                    title: '사용여부',
                    width: '75px',
                    attributes: {style: 'text-align:center'}
                    ,
                    template: '<input class="someInputChk" type="checkbox" name="useYn" #= useYn == "Y" ? checked="checked" : "" #/>'
                }
                , {
                    command: [{name:'destroy'	,text:'삭제', className: 'btn-red btn-s',   visible: function() { return fnIsProgramAuth("SAVE_DELETE") }}],
                    title: ' ',
                    width: '120px',
                    attributes: {style: 'text-align:center', class: 'forbiz-cell-readonly'}
                }
                , {field: 'comment', title: '설명', width: '275px', attributes: {style: 'text-align:center'}}
                , {field: 'stShopId', hidden: true}
                , {field: 'psGroupType', hidden: true}
                , {field: 'psConfigId', hidden: true}
            ],
            cellClose: function (e) {
                var cellIndex = e.sender._lastCellIndex;
                if (cellIndex == 1) {
                    var psName = e.model.psName;
                    e.model.psName = psName.substr(0, 20);
                    // aGrid.refresh();
                } else if (cellIndex == 2) {
                    e.model.psKey = e.model.psKey.toUpperCase().replace(/[^A-Z^_]/g, "");
                    // aGrid.refresh();
                } else if (cellIndex == 3) {
                    var psValue = e.model.psValue;
                    e.model.psValue = psValue.substr(0, 20);
                    // aGrid.refresh();
                } else if (cellIndex == 4) {
                    var comment = e.model.comment;
                    e.model.comment = comment.substr(0, 255);
                    // aGrid.refresh();
                }
            },
            forbizEditCustm: fnEditCustm
        };
        aGrid = $('#aGrid').initializeKendoGrid(aGridOpt).cKendoGrid();
        aGrid.bind('dataBound', function () {
            $('#countTotalSpan').text(aGridDs._total);
            $('.someInputChk').unbind('change').on('change', function (e) {
                var dataItem = aGrid.dataItem($(e.target).closest('tr'));
                dataItem.useYn = $(this).is(':checked') ? 'Y' : 'N';
                dataItem.dirty = true;
            });
        });

        $(".k-custom-save").click(function (e) {
            e.preventDefault();
            fnSave();
        });

        $(".k-grid-toolbar").addClass("k-align-with-pager-sizes");
    }


    function fnGridClick() {
        var map = aGrid.dataItem(aGrid.select());
        $('#inputForm').bindingForm({'rows': map}, 'rows', true);
    };

    //Initialize End End
    function fnEditCustm(e) {
        return;
    }


    //-------------------------------  Grid End  -------------------------------

    //---------------Initialize Option Box Start ------------------------------------------------

    // 옵션 초기화
    function fnInitOptionBox() {
        fnShopKendoDropDownList({id: "ST_SHOP_ID", blank: "All"});
        fnKendoDropDownList({
            id: 'psGroupType',
            url: "/admin/comn/getCodeList",
            params: {"stCommonCodeMasterCode": "7"},
            blank: "All"
        });
    }

    //---------------Initialize Option Box End ------------------------------------------------
    //-------------------------------  Common Function start -------------------------------

    function inputFocus() {
        $('#input1').focus();
    };

    function condiFocus() {
        $('#condition1').focus();
    };

    /**
     * 콜백합수
     */
    function fnBizCallback(id, data) {
        switch (id) {
            case 'select':
                //form data binding
                $('#searchForm').bindingForm(data, 'rows', true);
                break;
            case 'save':
                fnSearch();
                fnKendoMessage({message: '저장되었습니다.'});
                break;
        }
    }

    //-------------------------------  Common Function end -------------------------------


    //------------------------------- Html 버튼 바인딩  Start -------------------------------
    /** Common Search*/
    $scope.fnSearch = function () {
        fnSearch();
    };
    /** Common Clear*/
    $scope.fnClear = function () {
        fnClear();
    };
    /** Common New*/
    $scope.fnNew = function () {
        fnNew();
    };
    /** Common Save*/
    $scope.fnSave = function () {
        fnSave();
    };
    /** Common Delete*/
    $scope.fnDel = function () {
        fnDel();
    };
    /** Common Close*/
    $scope.fnClose = function () {
        fnClose();
    };
    //------------------------------- Html 버튼 바인딩  End -------------------------------

}); // document ready - END
