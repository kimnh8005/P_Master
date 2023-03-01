/**-----------------------------------------------------------------------------
 * description 		 : 외부몰관리 > 외부몰주문관리 > 외부몰 클레임 주문리스트
 * @
 * @ 수정일			수정자          수정내용
 * @ ------------------------------------------------------
 * @ 2021.01.13		이원호          최초생성
 * @
 * **/
'use strict';


var PAGE_SIZE = 20;
var aGridDs, aGridOpt, aGrid;
var publicStorageUrl = fnGetPublicStorageUrl();
var gAdminSearchValue = "";

$(document).ready(function() {
    importScript("/js/service/om/order/claimOrderGridColumns.js", function (){
        fnInitialize();
    });

	// Initialize PageR
	function fnInitialize(){
		$scope.$emit('fnIsMenu', { flag : 'true' });

		fnPageInfo({
			PG_ID  : 'claimOrder',
			callback : fnUI
		});
	}

	function fnUI(){
		fnInitButton();	// Initialize Button ---------------------------------
		fnInitGrid();	// Initialize Grid ------------------------------------
		fnInitOptionBox();// Initialize Option Box
		fnDefaultSet();
//		fnSearch();
	}

	// --------------------------------- Button
	// Start---------------------------------
	function fnInitButton(){
		$('#fnSearch, #fnClear, #fnEmployeeSearchPopup').kendoButton();
    }

	function fnSearch(){
		var data = $('#searchForm').formSerialize(true);
		data.selectConditionType = $("input[name=selectConditionType]:checked").val();
		data.adminSearchValue = gAdminSearchValue;

		var query = {
            page         : 1,
            pageSize     : PAGE_SIZE,
            filterLength : fnSearchData(data).length,
            filter :  {
                filters : fnSearchData(data)
            }
		};
		aGridDs.query( query );
	}

	function fnClear(){
		$('#searchForm').formClear(true);

        fnDefaultSet();
	}

    function fnEmployeeSearchPopup(apprManagerType){

    	document.getElementById("searchAdmin").value = '';

        fnKendoPopup({
            id     : 'employeeSearchPopup',
            title  : 'BOS 계정 선택',
            src    : '#/employeeSearchPopup',
            width  : '1050px',
            height : '800px',
            scrollable : "yes",
            success: function( stMenuId, data ){
            	if(data.userId != undefined){
            		document.getElementById("searchAdmin").value = "[" + data.userId + "] " + data.userName;
            		gAdminSearchValue = data.userId;
            	}
            }
        });
    }

    function fnExcelDown() {
        var data = $('#searchForm').formSerialize(true);
        data.selectConditionType = $("input[name=selectConditionType]:checked").val();
        data.adminSearchValue = gAdminSearchValue;

        var query = {
            page         : 1,
            pageSize     : 10000,
            filterLength : fnSearchData(data).length,
            filter :  {
                filters : fnSearchData(data)
            }
        };

        fnExcelDownload('/admin/outmall/order/getClaimOrderListExportExcel', data);
    }

    // 처리상태 변경 - Grid
    function fnProcessICheck(){
		let selectRows = $("#aGrid").find('input[name=aGridCheck]:checked').closest('tr');
		if(selectRows.length == 0) {
            return false;
        }

		let idList = new Array();
		for (let selectRow of selectRows){
		    let dataItem = aGrid.dataItem($(selectRow));
		    if(dataItem.processCode != "W"){
		        fnKendoMessage({ message : "미처리 상태만 처리중으로 변경 가능합니다."});
		        return false;
		    }
		    idList.push(dataItem.ifEasyadminOrderClaimId);
		}

        fnKendoMessage({
            message: "처리중으로 상태를 변경하시겠습니까?",
            type: "confirm",
            ok: function (event) {
                fnPutProcessList(idList, 'I');
                return true;
            },
            cancel: function (event) {
                return false;
            }
        })
	}

	function fnProcessECheck(){
	    let selectRows = $("#aGrid").find('input[name=aGridCheck]:checked').closest('tr');
        if(selectRows.length == 0) {
            return false;
        }

        let idList = new Array();
        for (let selectRow of selectRows){
            let dataItem = aGrid.dataItem($(selectRow));
            if(dataItem.processCode != "I"){
                fnKendoMessage({ message : "처리중 상태만 처리완료로 변경 가능합니다."});
                return false;
            }
            idList.push(dataItem.ifEasyadminOrderClaimId);
        }

        fnKendoMessage({
            message: "처리완료로 상태를 변경하시겠습니까?",
            type: "confirm",
            ok: function (event) {
                fnPutProcessList(idList, 'E');
                return true;
            },
            cancel: function (event) {
                return false;
            }
        })
	}

    // 처리상태 변경
    function fnPutProcessList(param, processCode){
        var url  = '/admin/outmall/order/putClaimOrderProgressList';
        fnAjax({
            url     : url,
            params  : { "processCode" : processCode, "ifEasyadminOrderClaimIdParam" : JSON.stringify(param) },
            isAction : 'update',
            success :
                function( data ){
                    $("#aGrid").data("kendoGrid").dataSource.read();
                }
        });
    }

    // 처리상태 변경
    function fnPutProcess(data, processCode){
		var url  = '/admin/outmall/order/putClaimOrderProgress';

		fnAjax({
            url     : url,
            params  : { "processCode" : processCode, "ifEasyadminOrderClaimId" : data.ifEasyadminOrderClaimId },
            isAction : 'update',
            success :
                function( data ){
                    $("#aGrid").data("kendoGrid").dataSource.read();
                }
        });
	}

    // 옵션 초기화
	function fnInitOptionBox(){
        // 단일/복수조건 검색 구분
        fnTagMkRadio({
            id: 'selectConditionType',
            tagId: 'selectConditionType',
            chkVal: 'multiSection',
            tab: true,
            data: [{
                CODE: "multiSection",
                NAME: "복수조건 검색",
                TAB_CONTENT_NAME: "multiSection"
            }, {
                CODE: "singleSection",
                NAME: "단일조건 검색",
                TAB_CONTENT_NAME: "singleSection"
            }],
        });

        // 단일조건.검색유형
        fnKendoDropDownList({
            id: "singleSearchType",
            data: [
                { "CODE" : "SEARCH_TYPE_1", "NAME" : "외부몰 주문번호" }
                , { "CODE" : "SEARCH_TYPE_2", "NAME" : "수집몰 주문번호" }
                , { "CODE" : "SEARCH_TYPE_3", "NAME" : "주문번호" }
                , { "CODE" : "SEARCH_TYPE_4", "NAME" : "상품코드" }
                , { "CODE" : "SEARCH_TYPE_5", "NAME" : "수집몰 상품코드" }
                , { "CODE" : "SEARCH_TYPE_6", "NAME" : "마스터 품목코드" }
            ],
            blank : '선택해주세요'
        });

        // 복수조건.외부몰유형
        fnKendoDropDownList({
            id  : 'outMallType',
            tagId : 'outMallType',
            url   : "/admin/comn/getCodeList",
            params : {"stCommonCodeMasterCode" : "SELLERS_GROUP", "useYn" :"Y"},
            blank : '외부몰 그룹 전체',
            async : false
        });

        // 복수조건.외부몰 체크박스 그룹 ALL
        fnTagMkChkBox({
            id    : "outMallFilterAll",
            url : "/admin/outmall/order/getSellersList",
            params : {"sellersGroupCode" : ""},
            tagId : 'outMallFilterAll',
            async : false,
            chkVal: '',
            style : {},
            textField :"NAME",
            valueField : "CODE",
            beforeData : [{ "CODE" : "ALL", "NAME" : "전체" }]
        });

        // 복수조건.외부몰 체크박스 그룹 DirectMng
        fnTagMkChkBox({
            id    : "outMallFilterVendor",
            url : "/admin/outmall/order/getSellersList",
            params : {"sellersGroupCode" : "SELLERS_GROUP.VENDOR"},
            tagId : 'outMallFilterVendor',
            async : false,
            chkVal: '',
            style : {},
            textField :"NAME",
            valueField : "CODE",
            beforeData : [{ "CODE" : "ALL", "NAME" : "전체" }]
        });

        // 복수조건.외부몰 체크박스 그룹 DirectMng
        fnTagMkChkBox({
            id    : "outMallFilterDirectMng",
            url : "/admin/outmall/order/getSellersList",
            params : {"sellersGroupCode" : "SELLERS_GROUP.DIRECT_MNG"},
            tagId : 'outMallFilterDirectMng',
            async : false,
            chkVal: '',
            style : {},
            textField :"NAME",
            valueField : "CODE",
            beforeData : [{ "CODE" : "ALL", "NAME" : "전체" }]
        });

        // 복수조건.외부몰 체크박스 그룹 DirectBuy
        fnTagMkChkBox({
            id    : "outMallFilterDirectBuy",
            url : "/admin/outmall/order/getSellersList",
            params : {"sellersGroupCode" : "SELLERS_GROUP.DIRECT_BUY"},
            tagId : 'outMallFilterDirectBuy',
            async : false,
            chkVal: '',
            style : {},
            textField :"NAME",
            valueField : "CODE",
            beforeData : [{ "CODE" : "ALL", "NAME" : "전체" }]
        });

        $('#outMallType').unbind('change').on('change', function(){
            fnDefaultSetOutMallFilter();
        });

        // 복수조건.외부몰유형
        fnKendoDropDownList({
            id: "searchDateType",
            data: [
                { "CODE" : "COLLECT_DATE", "NAME" : "주문수집일자" },
                { "CODE" : "ORDER_DATE", "NAME" : "주문일자" }
            ]
        });

        // 복수조건.기간검색 시작 일자
        fnKendoDatePicker({
            id: "startDate",
            format: "yyyy-MM-dd",
            btnStartId: "startDate",
            btnEndId: "endDate",
            defVal: fnGetToday(),
        });

        // 복수조건.기간검색 종료 일자
        fnKendoDatePicker({
            id: "endDate",
            format: "yyyy-MM-dd",
            btnStyle: true,
            btnStartId: "startDate",
            btnEndId: "endDate",
            defVal: fnGetToday(),
        });

        // 복수조건.CS상태
	    fnTagMkChkBox({
            id: 'orderCsFilter',
            tagId: 'orderCsFilter',
            data: [
                {CODE: "ALL", NAME: "전체"},
                {CODE: "1", NAME: "배송전 전체 취소"},
                {CODE: "2", NAME: "배송전 부분 취소"},
                {CODE: "3", NAME: "배송후 전체 취소"},
                {CODE: "4", NAME: "배송후 부분 취소"},
                {CODE: "5", NAME: "배송전 전체 교환"},
                {CODE: "6", NAME: "배송전 부분 교환"},
                {CODE: "7", NAME: "배송후 전체 교환"},
                {CODE: "8", NAME: "배송후 부분 교환"}
                ]
        });

        // 복수조건.주문상태
	    fnTagMkChkBox({
            id: 'orderStatusFilter',
            tagId: 'orderStatusFilter',
            data: [
                {CODE: "ALL", NAME: "전체"},
                {CODE: "NULL", NAME: "미생성"},
                {CODE: "IC", NAME: "결제완료"},
                {CODE: "DR", NAME: "배송준비중"},
                {CODE: "DI", NAME: "배송중"},
                {CODE: "DC", NAME: "배송완료"},
                {CODE: "BF", NAME: "구매확정"}
                ]
        });

        // 복수조건.처리자(관리자)

        // 복수조건.처리상태
	    fnTagMkChkBox({
            id: 'processCodeFilter',
            tagId: 'processCodeFilter',
            data: [
                {CODE: "ALL", NAME: "전체"},
                {CODE: "W", NAME: "미처리"},
                {CODE: "I", NAME: "처리중"},
                {CODE: "E", NAME: "처리완료"}
                ]
        });

        // 복수조건.검색어유형
        fnKendoDropDownList({
            id: "multiSearchType",
            data: [
                { "CODE" : "SEARCH_TYPE_1", "NAME" : "외부몰 주문번호" },
                { "CODE" : "SEARCH_TYPE_2", "NAME" : "수집몰 주문번호" },
                { "CODE" : "SEARCH_TYPE_3", "NAME" : "주문번호" },
                { "CODE" : "SEARCH_TYPE_4", "NAME" : "상품코드"},
                { "CODE" : "SEARCH_TYPE_5", "NAME" : "수집몰 상품코드" },
                { "CODE" : "SEARCH_TYPE_6", "NAME" : "마스터품목코드" },
                { "CODE" : "SEARCH_TYPE_7", "NAME" : "주문자명" }
            ],
            //blank : '선택해주세요'
        });

        fbTabChange();      //[공통] 탭 변경 이벤트
        fbCheckboxChange(); //[공통] checkBox
	};

	// 기본 설정
	function fnDefaultSet(){
	    $("#startDate").data("kendoDatePicker").value(fnGetToday());
        $("#endDate").data("kendoDatePicker").value(fnGetToday());

		if($("input[name=selectConditionType]:checked").val() == "singleSection") {
            $("input[name=orderCsFilter]").eq(0).prop("checked", false).trigger("change");
            $("input[name=orderStatusFilter]").eq(0).prop("checked", false).trigger("change");
            $("input[name=processCodeFilter]").eq(0).prop("checked", false).trigger("change");
        }else {
            $("input[name=orderCsFilter]").eq(0).prop("checked", true).trigger("change");
            $("input[name=orderStatusFilter]").eq(0).prop("checked", true).trigger("change");
            $("input[name=processCodeFilter]").eq(0).prop("checked", true).trigger("change");
        }

        gAdminSearchValue = "";
        fnDefaultSetOutMallFilter();
	};

    function fnDefaultSetOutMallFilter(){
        var dropDownList = $('#outMallType').data('kendoDropDownList');
        if(dropDownList.value() == ''){
            $('#outMallFilterAll').hide();
            $('#outMallFilterVendor').hide();
            $('#outMallFilterDirectMng').hide();
            $('#outMallFilterDirectBuy').hide();
            $("input[name=outMallFilterAll]").eq(0).prop("checked", true).trigger("change");
            $("input[name=outMallFilterVendor]").eq(0).prop("checked", false).trigger("change");
            $("input[name=outMallFilterDirectMng]").eq(0).prop("checked", false).trigger("change");
            $("input[name=outMallFilterDirectBuy]").eq(0).prop("checked", false).trigger("change");
        }else if(dropDownList.value() == 'SELLERS_GROUP.VENDOR'){
            $('#outMallFilterAll').hide();
            $('#outMallFilterVendor').show();
            $('#outMallFilterDirectMng').hide();
            $('#outMallFilterDirectBuy').hide();
            $("input[name=outMallFilterAll]").eq(0).prop("checked", false).trigger("change");
            $("input[name=outMallFilterVendor]").eq(0).prop("checked", true).trigger("change");
            $("input[name=outMallFilterDirectMng]").eq(0).prop("checked", false).trigger("change");
            $("input[name=outMallFilterDirectBuy]").eq(0).prop("checked", false).trigger("change");
        }else if(dropDownList.value() == 'SELLERS_GROUP.DIRECT_BUY'){
            $('#outMallFilterAll').hide();
            $('#outMallFilterVendor').hide();
            $('#outMallFilterDirectMng').hide();
            $('#outMallFilterDirectBuy').show();
            $("input[name=outMallFilterAll]").eq(0).prop("checked", false).trigger("change");
            $("input[name=outMallFilterVendor]").eq(0).prop("checked", false).trigger("change");
            $("input[name=outMallFilterDirectMng]").eq(0).prop("checked", false).trigger("change");
            $("input[name=outMallFilterDirectBuy]").eq(0).prop("checked", true).trigger("change");
        }else if(dropDownList.value() == 'SELLERS_GROUP.DIRECT_MNG'){
            $('#outMallFilterAll').hide();
            $('#outMallFilterVendor').hide();
            $('#outMallFilterDirectMng').show();
            $('#outMallFilterDirectBuy').hide();
            $("input[name=outMallFilterAll]").eq(0).prop("checked", false).trigger("change");
            $("input[name=outMallFilterVendor]").eq(0).prop("checked", false).trigger("change");
            $("input[name=outMallFilterDirectMng]").eq(0).prop("checked", true).trigger("change");
            $("input[name=outMallFilterDirectBuy]").eq(0).prop("checked", false).trigger("change");
        }
    }

    function fnAGridClick(param){
        var clickRowCheckBox = param.find('input[type=checkbox]');
        if(clickRowCheckBox.prop('checked')){
            clickRowCheckBox.prop('checked', false);
        }else{
            clickRowCheckBox.prop('checked', true);
        }
        fnSetAllCheckbox('aGridCheckbox','checkBoxAll1');
    }

	// --------------------------------- Button
	// End---------------------------------


	// ------------------------------- Grid Start
	// -------------------------------
	function fnInitGrid(){
		aGridDs = fnGetPagingDataSource({
			url      : "/admin/outmall/order/getClaimOrderList",
			pageSize : PAGE_SIZE
		});
		aGridOpt = {
			dataSource: aGridDs
			,  pageable  : {
				pageSizes: [20, 30, 50],
				buttonCount : 10
			}
			,navigatable: true
			,columns   : outmallClaimOrderGridUtil.claimOrderList()
		};

		aGrid = $('#aGrid').initializeKendoGrid( aGridOpt ).cKendoGrid();

		aGrid.bind("dataBound", function() {
			var row_num = aGridDs._total - ((aGridDs._page - 1) * aGridDs._pageSize);
			$("#aGrid tbody > tr .row-number").each(function(index){
				$(this).html(row_num);
				row_num--;
			});

        	$('#totalCnt').text(aGridDs._total);
        });

        aGrid.bind("dataBound", function(){
            if(aGrid.dataSource && aGrid.dataSource._view.length > 0){
                fnSetAllCheckbox('aGridCheckbox','checkBoxAll1');
                $('input[name=aGrid]').on("change", function (){
                    fnSetAllCheckbox('aGridCheckbox','checkBoxAll1');
                });
            }
        });

        $(aGrid.tbody).on("click", "td", function (e) {
            var row = $(this).closest("tr");
            var colIdx = $("td", row).index(this);
            if(colIdx > 0){
                fnAGridClick($(e.target).closest('tr'));
            }
        });

        // 처리 버튼 이벤트
        $('#aGrid').on("click", "button[kind=btnProgress]", function(e) {
            e.preventDefault();
            let dataItem = aGrid.dataItem($(e.currentTarget).closest("tr"));

            if(dataItem.processCode == "W"){
                fnKendoMessage({
                    message: "처리중으로 상태를 변경하시겠습니까?",
                    type: "confirm",
                    ok: function (event) {
                        fnPutProcess(dataItem, "I");
                        return true;
                    },
                    cancel: function (event) {
                        return false;
                    }
                })
            }else if(dataItem.processCode == "I"){
                  fnKendoMessage({
                      message: "처리완료로 상태를 변경하시겠습니까?",
                      type: "confirm",
                      ok: function (event) {
                          fnPutProcess(dataItem, "E");
                          return true;
                      },
                      cancel: function (event) {
                          return false;
                      }
                  })
              }
        });
	}

	// ------------------------------- Grid End -------------------------------

	// ------------------------------- Html 버튼 바인딩 Start
	// -------------------------------

	/** Common Search */
	$scope.fnSearch = function( ) {	fnSearch();	};
	/** Common Clear */
	$scope.fnClear =function(){	 fnClear();	};
    /** Button fnProcessICheck */
	$scope.fnProcessICheck = function( ){	fnProcessICheck();};
    /** Button fnProcessECheck */
	$scope.fnProcessECheck = function( ){	fnProcessECheck();};
    /** Button fnEmployeeSearchPopup */
	$scope.fnEmployeeSearchPopup = function(){	 fnEmployeeSearchPopup();};
    /** Button fnExcelDownload */
    $scope.fnExcelDown = function(){	 fnExcelDown();};

	// ------------------------------- Html 버튼 바인딩 End
	// -------------------------------

}); // document ready - END
