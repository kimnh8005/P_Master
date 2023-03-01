/**-----------------------------------------------------------------------------
 * description 		 : 외부몰관리 > 외부몰주문관리 > 외부몰 주문 엑셀업로드 내역
 * @
 * @ 수정일			수정자          수정내용
 * @ ------------------------------------------------------
 * @ 2021.01.20		이원호          최초생성
 * @
 * **/
'use strict';


var PAGE_SIZE = 20;
var aGridDs, aGridOpt, aGrid;
var publicStorageUrl = fnGetPublicStorageUrl();
var gAdminSearchValue = "";

$(document).ready(function() {
	importScript("/js/service/om/order/omOrderUploadListGridColumns.js", function (){
		fnInitialize();
	});

	// Initialize PageR
	function fnInitialize(){
		$scope.$emit('fnIsMenu', { flag : 'true' });

		fnPageInfo({
			PG_ID  : 'omOrderUploadList',
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
		$('#fnSearch, #fnClear, #fnEmployeeSearchPopup').kendoButton();
	}

	function fnSearch(){
		var data = $('#searchForm').formSerialize(true);
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

    // 옵션 초기화
	function fnInitOptionBox(){
	    // 연동 상태
		fnKendoDropDownList({
            id: "outMallType",
            data: [
                { "CODE" : "E", "NAME" : "이지어드민" }
                , { "CODE" : "S", "NAME" : "사방넷" }
            ],
            blank:'양식 전체'
        });

        // 관리자

        // 조회 시작 일자
        fnKendoDatePicker({
            id: "createStartDate",
            format: "yyyy-MM-dd",
            btnStartId: "createStartDate",
            btnEndId: "createEndDate",
            defVal : fnGetDayMinus(fnGetToday(),6),
            defType : 'oneWeek'
        });

        // 조회 종료 일자
        fnKendoDatePicker({
            id: "createEndDate",
            format: "yyyy-MM-dd",
            btnStyle: true,
            btnStartId: "createStartDate",
            btnEndId: "createEndDate",
            defVal: fnGetToday(),
            defType : 'oneWeek'
        });

	};

	// 기본 설정
	function fnDefaultSet(){
		//$("#createStartDate").data("kendoDatePicker").value(fnGetToday());
        //$("#createEndDate").data("kendoDatePicker").value(fnGetToday());
        gAdminSearchValue = "";
	};

	// --------------------------------- Button
	// End---------------------------------


	// ------------------------------- Grid Start
	// -------------------------------
	function fnInitGrid(){
		aGridDs = fnGetPagingDataSource({
			url      : "/admin/outmall/order/getOutMallExcelInfoList",
			pageSize : PAGE_SIZE
		});
		aGridOpt = {
			dataSource: aGridDs
			,  pageable  : {
				pageSizes: [20, 30, 50],
				buttonCount : 10
			}
			,navigatable: true
			,columns   : outmallOrderUploadListGridUtil.orderUploadList()
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

        // 실패내역 다운로드 버튼 이벤트.
        $('#aGrid').on("click", "button[kind=btnExcelDown]", function(e) {
            e.preventDefault();
            let dataItem = aGrid.dataItem($(e.currentTarget).closest("tr"));
            let failType = $(this).data("failtype");

            let data = { "outmallType": dataItem.outMallType, "ifOutmallExcelInfoId" : dataItem.ifOutmallExcelInfoId, "failType": failType};
            fnExcelDownload('/admin/outmall/order/getOutMallFailExcelDownload', data);
        });

		// 업로드 엑셀 파일명 클릭시 다운로드 처리 이벤트.
		$('#aGrid').on("click", "div.divExcelDownClick", function(e) {
			e.preventDefault();
			let dataItem = aGrid.dataItem($(e.currentTarget).closest("tr"));
			let failType = $(this).data("failtype");

			let data = { "outmallType": dataItem.outMallType, "ifOutmallExcelInfoId" : dataItem.ifOutmallExcelInfoId, "failType": failType};
			fnExcelDownload('/admin/outmall/order/getOutMallFailExcelDownload', data);
		});

	}

	// ------------------------------- Grid End -------------------------------

	// ------------------------------- Html 버튼 바인딩 Start
	// -------------------------------

	/** Common Search */
	$scope.fnSearch = function(){	fnSearch();	};
	/** Common Clear */
	$scope.fnClear = function(){	 fnClear();	};
	/** Button fnEmployeeSearchPopup */
	$scope.fnEmployeeSearchPopup = function(){	 fnEmployeeSearchPopup();};

	// ------------------------------- Html 버튼 바인딩 End
	// -------------------------------

}); // document ready - END
