/**-----------------------------------------------------------------------------
 * description 		 : 외부몰관리 > 외부몰주문관리 > 수집몰 연동 내역
 * @
 * @ 수정일			수정자          수정내용
 * @ ------------------------------------------------------
 * @ 2021.01.12		이원호          최초생성
 * @
 * **/
'use strict';


var PAGE_SIZE = 20;
var aGridDs, aGridOpt, aGrid;
var publicStorageUrl = fnGetPublicStorageUrl();

$(document).ready(function() {
    importScript("/js/service/om/order/collectionMallInterfaceGridColumns.js", function (){
        fnInitialize();
    });

	// Initialize PageR
	function fnInitialize(){
		$scope.$emit('fnIsMenu', { flag : 'true' });

		fnPageInfo({
			PG_ID  : 'collectionMallInterface',
			callback : fnUI
		});
	}

	function fnUI(){
		fnInitButton();	// Initialize Button ---------------------------------
		fnInitGrid();	// Initialize Grid ------------------------------------
		fnInitOptionBox();// Initialize Option Box
		fnDefaultSet();
		fnSearch();
	}

	// --------------------------------- Button
	// Start---------------------------------
	function fnInitButton(){
		$('#fnSearch, #fnClear, #fnProcessCodeCheck').kendoButton();
	}

	function fnSearch(){
		var data = $('#searchForm').formSerialize(true);
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

    // 처리완료 - grid
    function fnProcessCodeCheck(){
		let selectRows = $("#aGrid").find('input[name=aGridCheck]:checked').closest('tr');
		if(selectRows.length == 0) {
            return false;
        }

		let idList = new Array();
		for (let selectRow of selectRows){
		    idList.push(aGrid.dataItem($(selectRow)).ifEasyadminInfoId);
		}

        fnKendoMessage({
            message: "처리완료로 상태를 변경하시겠습니까?",
            type: "confirm",
            ok: function (event) {
                fnPutProcessList(idList);
                return true;
            },
            cancel: function (event) {
                return false;
            }
        })
	}

    function fnPutProcessList(param){
        var url  = '/admin/outmall/order/putCollectionMallInterfaceProgressList';
        fnAjax({
            url     : url,
            params  : { "ifEasyadminInfoIdParam" : JSON.stringify(param) },
            isAction : 'update',
            success :
                function( data ){
                    $("#aGrid").data("kendoGrid").dataSource.read();
                }
        });
    }

    // 처리완료 - cell
    function fnPutProcess(data, processCode){
		var url  = '/admin/outmall/order/putCollectionMallInterfaceProgress';

		fnAjax({
            url     : url,
            params  : { "processCode" : processCode, "ifEasyadminInfoId" : data.ifEasyadminInfoId },
            success :
                function( data ){
                    $("#aGrid").data("kendoGrid").dataSource.read();
                },
            isAction : 'batch'
        });
	}

    // 옵션 초기화
	function fnInitOptionBox(){
	    // 연동 상태
		fnKendoDropDownList({
            id: "syncCode",
            data: [
                { "CODE" : "I", "NAME" : "진행중" }
                , { "CODE" : "E", "NAME" : "완료" }
            ],
            blank:'전체'
        });

        // 처리 상태
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

        // 조회 시작 일자
        fnKendoDatePicker({
            id: "startDate",
            format: "yyyy-MM-dd",
            btnStartId: "startDate",
            btnEndId: "endDate",
            defVal: fnGetToday(),
        });

        // 조회 종료 일자
        fnKendoDatePicker({
            id: "endDate",
            format: "yyyy-MM-dd",
            btnStyle: true,
            btnStartId: "startDate",
            btnEndId: "endDate",
            defVal: fnGetToday(),
        });

        fnTagMkRadio({
            id: 'actionNm',
            tagId: 'actionNm',
            chkVal: 'get_order_info',
            tab: true,
            data: [{
                CODE: "get_order_info",
                NAME: "주문연동"
            }, {
                CODE: "set_trans_no",
                NAME: "송장연동"
            }],
            change: function (e) {
            }
        });

        fbCheckboxChange(); //[공통] checkBox
	};

	// 기본 설정
	function fnDefaultSet(){
	    $("input[name=processCodeFilter]").eq(0).prop("checked", true).trigger("change");

		$("#startDate").data("kendoDatePicker").value(fnGetToday());
        $("#endDate").data("kendoDatePicker").value(fnGetToday());
	};

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
			url      : "/admin/outmall/order/getCollectionMallInterfaceList",
			pageSize : PAGE_SIZE
		});
		aGridOpt = {
			dataSource: aGridDs
			,  pageable  : {
				pageSizes: [20, 30, 50],
				buttonCount : 10
			}
			,navigatable: true
			,columns   : outmallOrderGridUtil.orderList()
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

            if(dataItem.processCode == "I"){
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

        // 실패내역 다운로드 버튼 이벤트.
        $('#aGrid').on("click", "button[kind=btnExcelDown]", function(e) {
            e.preventDefault();
            let dataItem = aGrid.dataItem($(e.currentTarget).closest("tr"));
            let failType = $(this).data("failtype");
            if(dataItem.processCode == "W") {
                fnKendoMessage({
                    message: "실패내역 다운로드 시 처리중으로 상태가 변경됩니다.",
                    type: "confirm",
                    ok: function (event) {
                        let data = {"ifEasyadminInfoId": dataItem.ifEasyadminInfoId, "failType": failType};
                        fnExcelDownload('/admin/outmall/order/getCollectionMallFailExcelDownload', data);
                        fnPutProcess(dataItem, "I");
                        return true;
                    },
                    cancel: function (event) {
                        return false;
                    }
                })
            } else if(dataItem.processCode == "T") {
                // 송장전송 실패내역 다운로드
                let data = { "batchStartDateTime" : dataItem.batchStartDateTime, "failType": "T"};
                fnExcelDownload('/admin/outmall/order/getCollectionMallFailExcelDownload', data);
            }else{
                let data = { "ifEasyadminInfoId" : dataItem.ifEasyadminInfoId, "failType": failType};
                fnExcelDownload('/admin/outmall/order/getCollectionMallFailExcelDownload', data);
            }
        });

	}

	// ------------------------------- Grid End -------------------------------

	// ------------------------------- Html 버튼 바인딩 Start
	// -------------------------------

	/** Common Search */
	$scope.fnSearch = function(){	fnSearch();	};
	/** Common Clear */
	$scope.fnClear = function(){	 fnClear();	};
	/** Button ProcessCodeCheck */
	$scope.fnProcessCodeCheck = function() { fnProcessCodeCheck(); };

	// ------------------------------- Html 버튼 바인딩 End
	// -------------------------------

}); // document ready - END
