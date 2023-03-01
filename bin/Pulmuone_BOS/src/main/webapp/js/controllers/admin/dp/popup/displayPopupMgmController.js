/**-----------------------------------------------------------------------------
 * system             :  팝업관리
 * @
 * @ 수정일            수정자          수정내용
 * @ ------------------------------------------------------
 * @ 2020.11.17        최성현          최초생성
 * @
 * **/
'use strict';

var PAGE_SIZE = 20;
var aGridDs, aGridOpt, aGrid;

$(document).ready(function () {
    fnInitialize(); //Initialize Page Call ---------------------------------

    //Initialize PageR
    function fnInitialize() {
        $scope.$emit('fnIsMenu', {flag: 'true'});

        fnPageInfo({
            PG_ID: 'displayPopupMgm',
            callback: fnUI
        });
    }

    function fnUI() {


        fnInitButton(); //Initialize Button  ---------------------------------

        fnInitOptionBox(); //Initialize Option Box ------------------------------------

        fnInitDropList();

        fnInitGrid(); //Initialize Grid ------------------------------------

        fnSearch();
    }

    //--------------------------------- Button Start---------------------------------
    function fnInitButton() {
        $('#fnSearch, #fnClear,#fnNew').kendoButton();
    }

    function fnSearch() {

        $('#inputForm').formClear(true);

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

        $("input[name=exposureState]").eq(0).prop("checked", true).trigger("change");
        $("input[name=displayTargetType]").eq(0).prop("checked", true).trigger("change");
        $("input[name=displayRangeType]").eq(0).prop("checked", true).trigger("change");
    }

    function fnClose() {
        var kendoWindow = $('#kendoPopup').data('kendoWindow');
        kendoWindow.close();
    }

    function fnNew(){

    	displayPopupMgmPopup();

    }

    function displayPopupMgmPopup(params) {
    	  if (params == "") {
          	fnKendoMessage({ message : '미리보기가 없습니다.'});
          }

        fnKendoPopup({
            id: 'displayPopupMgmPopup',
            title: '컨텐츠 등록/수정',
            src: '#/displayPopupMgmPopup',
            param: params,
            width: '1000px',
            height: '700px',
            success: function (id, data) {
                fnSearch();
            }
        });
    }

    function displayPopupPreview(params) {
        if (params == "") {
        	fnKendoMessage({ message : '미리보기가 없습니다.'});
        }

        fnKendoPopup({
            id: 'displayPopupPreview',
            title: '컨텐츠 미리보기',
            src: '#/displayPopupPreview',
            param: params,
            width: '1000px',
            height: '600px',
            success: function (id, data) {
                fnSearch();
            }
        });
    }


    function fndelPopup(params) {

        fnKendoMessage({
            message: "삭제하시겠습니까?",
            type: "confirm",
            ok: function (e) {
                fnAjax({
                    url : '/admin/display/popup/delPopup',
                    params : {
                        displayFrontPopupId : params
                    },
                    success : function(data) {
                        fnKendoMessage({ message : '삭제되었습니다.'});
                        fnSearch();
                    },
                    isAction : 'delete'
                });
            },
            cancel: function (e) {
                return false;
            }
        })
    }


    //--------------------------------- Button End---------------------------------

    //------------------------------- Grid Start -------------------------------
    function fnInitGrid() {

        aGridDs = fnGetEditPagingDataSource({
            url: '/admin/display/popup/getPopupList',
            pageSize: PAGE_SIZE,
        });

        aGridOpt =
            {
                dataSource: aGridDs,
                pageable:
                    {
                         pageSizes: [20, 30, 50],
                         buttonCount: 5
                    },
                navigatable: true,
                columns:
                    [
                        {field: 'displayFrontPopupId', title: 'id', width: '5%', attributes: {style: 'text-align:center'},hidden: true},
                    	{ field: 'rowNumber'		, title: 'No.'		, width: '5%'	, attributes: { style: 'text-align:center' }, template: function (dataItem){
                    		return fnKendoGridPagenation(aGrid.dataSource,dataItem);
                    	}},
//                        {field: 'sort', title: '순번', width: '5%', attributes: {style: 'text-align:center'}},
                        {field: 'displayTargetType', title: '팝업 노출대상', width: '10%', attributes: {style: 'text-align:center', class: 'forbiz-cell-readonly'}},
                        {field: 'popupSubject', title: '팝업 제목', width: '12%', attributes: {style: 'text-align:center'}},
//                        {field: 'popupType', title: '팝업유형', width: '10%', attributes: {style: 'text-align:center'}},
                        {field: 'displayRangeType', title: '노출채널', width: '10%', attributes: {style: 'text-align:center'}},
                        {field: 'displayPopupDate', title: '노출기간', width: '18%', attributes: {style: 'text-align:center'}},
                        {field: 'createId', title: '작성자', width: '10%', attributes: {style: 'text-align:center'}},
                        {field: 'exposureState', title: '노출상태', width: '10%', attributes: {style: 'text-align:center'},template: "#=(exposureState=='E') ? '노출예정' : (exposureState=='Y') ? '노출중' : '종료'#"},
                        {field: 'useYn', title: '사용여부', width: '5%', attributes: {style: 'text-align:center'}, template: "#=(useYn=='Y') ? '예' : '아니오'#"},
                        { title:'관리', width: "25%", attributes:{ style:'text-align:center;', class:'forbiz-cell-readonly' }
						,command: [ { text: '미리보기', imageClass: "k-grid-승인요청 btn-point btn-m", className: "btn-point k-margin5", iconClass: "k-icon",
									   click: function(e) {  e.preventDefault();
													            var tr = $(e.target).closest("tr");
													            var data = this.dataItem(tr);
													            displayPopupPreview(data.displayFrontPopupId);}
									}
									,{ text: '수정' , imageClass: "k-i-search", className: "btn-gray k-button", iconClass: "k-icon",
										click: function(e) {  e.preventDefault();
													            var tr = $(e.target).closest("tr");
													            var data = this.dataItem(tr);
													            displayPopupMgmPopup(data.displayFrontPopupId);}
									, visible: function() { return fnIsProgramAuth("SAVE") }}
									,{ text: '삭제' , imageClass: "k-i-search ", className: "k-button btn-red k-button-icontext k-margin5", iconClass: "k-icon",
										click: function(e) {  e.preventDefault();
													            var tr = $(e.target).closest("tr");
													            var data = this.dataItem(tr);
													            fndelPopup(data.displayFrontPopupId);}
									, visible: function() { return fnIsProgramAuth("DELETE") }}
						]
					}

                    ],
            };
        aGrid = $('#aGrid').initializeKendoGrid(aGridOpt).cKendoGrid();
        aGrid.bind('dataBound', function () {
            $('#totalCnt').text(aGridDs._total);
        });
    };


    //-------------------------------  Grid End  -------------------------------

    //-------------------------------  Common Function start -------------------------------
    function fnInitOptionBox() {
        $('#kendoPopup').kendoWindow({
            visible: false,
            modal: true
        });

        fnTagMkRadio({
            id: 'popupType',
            tagId: 'popupType',
            url: "/admin/comn/getCodeList",
            params: {"stCommonCodeMasterCode": "DP_POPUP_TP", "useYn": "Y"},
            async: false,
            beforeData: [
                {'CODE': '', 'NAME': '전체'},
            ],
            chkVal: '',
            style: {}
        });

        fnTagMkRadio({
            id: 'useYn',
            tagId: 'useYn',
            data: [{'CODE': '', 'NAME': '전체'},
                {'CODE': 'Y', 'NAME': '예'},
                {'CODE': 'N', 'NAME': '아니오'}
            ],
            chkVal: '',
            style: {}
        });

        $('#popupSubject').on('keydown',function(e){
            if (e.keyCode == 13) {
                //fnSearch();
                return false;
            }
        });

/*
        fnTagMkRadio({
            id: 'displayRangeType',
            tagId: 'displayRangeType',
            url: '/admin/comn/getCodeList',
            params: {'stCommonCodeMasterCode': 'DP_RANGE_TP', 'useYn': 'Y'},
            async: false,
            chkVal: 'DP_RANGE_TP.ALL',
            style: {}
        });
*/
    };

    function fnInitDropList() {

/*
    	fnKendoDropDownList({
            id: 'displayTargetType',
            url: '/admin/comn/getCodeList',
            params: {'stCommonCodeMasterCode': 'DP_TARGET_TP', 'useYn': 'Y'},
            tagId: 'displayTargetType',
            autoBind: true,
            valueField: 'CODE',
            textField: 'NAME',
            style: {}
        });
*/
    	// 팜업노출대상
    	fnTagMkChkBox({
            id: 'displayTargetType',
            url: '/admin/comn/getCodeList',
            params: {'stCommonCodeMasterCode': 'DP_TARGET_TP', 'useYn': 'Y'},
            tagId: 'displayTargetType',
            autoBind	: true,
	        async : false,
	        chkVal: '',
            style: {marginRight: 20},
            textField: 'NAME',
            valueField: 'CODE',
	        beforeData : [{ "CODE" : "", "NAME" : "전체" }]
        });
        $('#displayTargetType').bind("change", fnCheckboxChange);
        $('input[name=displayTargetType]').each(function(idx, element) {$(element).prop('checked', true);});

        // 노출상태
        fnTagMkChkBox({
            id: 'exposureState',
            data: [
                {'CODE': '', 'NAME': '전체'},
                {'CODE': 'E', 'NAME': '노출예정'},
                {'CODE': 'Y', 'NAME': '노출중'},
                {'CODE': 'N', 'NAME': '종료'}
            ],
            tagId: 'exposureState',
            autoBind	: true,
            async		: false,
            chkVal: '',
            style: {marginRight: 20}
        });
        $('#exposureState').bind("change", fnCheckboxChange);
        $('input[name=exposureState]').each(function(idx, element) {$(element).prop('checked', true);})

        // 노출채널
        fnTagMkChkBox({
            id: 'displayRangeType',
            url: '/admin/comn/getCodeList',
            params: {'stCommonCodeMasterCode': 'DP_RANGE_TP', 'useYn': 'Y'},
            tagId: 'displayRangeType',
            autoBind	: true,
	        async : false,
	        chkVal: '',
            style: {marginRight: 20},
            textField: 'NAME',
            valueField: 'CODE',
	        beforeData : [{ "CODE" : "", "NAME" : "전체" }]
        });
        $('#displayRangeType').bind("change", fnCheckboxChange);
        $('input[name=displayRangeType]').each(function(idx, element) {$(element).prop('checked', true);});

        function fnCheckboxChange(e) {   // 체크박스 change event

            // 첫번째 체크박스가 '전체' 체크박스라 가정함
            var totalCheckedValue = $("input[name=" + e.target.name + "]:eq(0)").attr('value');

            if (e.target.value == totalCheckedValue) {  // '전체' 체크 or 체크 해제시

                if ($("input[name=" + e.target.name + "]:eq(0)").is(":checked")) {  // '전체' 체크시

                    $("input[name=" + e.target.name + "]:gt(0)").each(function (idx, element) {
                        $(element).prop('checked', true);  // 나머지 모두 체크
                    });

                } else { // '전체' 체크 해제시

                    $("input[name=" + e.target.name + "]:gt(0)").each(function (idx, element) {
                        $(element).prop('checked', false);  // 나머지 모두 체크 해제
                    });

                }

            } else { // 나머지 체크 박스 중 하나를 체크 or 체크 해재시

                var allChecked = true; // 나머지 모두 체크 상태인지 flag

                $("input[name=" + e.target.name + "]:gt(0)").each(function (idx, element) {
                    if ($(element).prop('checked') == false) {
                        allChecked = false;  // 하나라도 체크 해제된 상태가 있는 경우 flag 값 false
                    }
                });

                if (allChecked) { // 나머지 모두 체크 상태인 경우
                    $("input[name=" + e.target.name + "]:eq(0)").prop('checked', true);  // 나머지 모두 '전체' 체크
                } else {
                    $("input[name=" + e.target.name + "]:eq(0)").prop('checked', false);  // 나머지 모두 '전체' 체크 해제
                }


            }
        }

    };




    //-------------------------------  Common Function end -------------------------------


    //------------------------------- Html 버튼 바인딩  Start -------------------------------
    /** Common Search*/
    $scope.fnSearch = function () {
        fnSearch();
    };
    $scope.fnNew = function () {
        fnNew();
    };
    /** Common Clear*/
    $scope.fnClear = function () {
        fnClear();
    };
    /** Common Close*/
    $scope.fnClose = function () {
        fnClose();
    };

    $scope.fnPopupButton = function (data) {
        fnPopupButton(data);
    };


    //------------------------------- Html 버튼 바인딩  End -------------------------------
    //
}); // document ready - END